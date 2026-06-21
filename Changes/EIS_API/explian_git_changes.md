These commits mainly changed privacy notice API from old `auth_Key/request` encryption flow to EIS format: `REQUEST_REFERENCE_NUMBER`, encrypted `REQUEST`, `DIGI_SIGN`, and `AccessToken` header. 

## What changes were made

### 1. `XSSRequestWrapper.java`

`+` was removed from blocked XSS characters.

Before:

```java
String regexValue="[`|;|:|(|)|<|>|{|}|^|!|*|%|+|\"|']";
```

After:

```java
String regexValue="[`|;|:|(|)|<|>|{|}|^|!|*|%|\"|']";
```

Meaning: now `+` will not be stripped from request values. This was likely needed because Base64 encrypted data/signature may contain `+`.

---

### 2. `CcmsUtil.java`

Added logs for:

```java
logger.info("encryptedRequestCcms "+encryptedRequestCcms);
logger.info("headerSecretKey "+headerSecretKey);
```

Meaning: CCMS/EIS request encryption was already happening, but now encrypted payload and RSA encrypted AES key are logged.

Be careful: do **not** keep AES key/header token logs in production.

---

### 3. `PrivacyRequest.java`

Old request fields removed/replaced:

Old:

```java
auth_Key
request
```

New:

```java
REQUEST_REFERENCE_NUMBER
REQUEST
DIGI_SIGN
```

Meaning: request body now follows EIS format:

```json
{
  "REQUEST_REFERENCE_NUMBER": "...",
  "REQUEST": "AES encrypted payload",
  "DIGI_SIGN": "RSA signature of plain payload"
}
```

AccessToken is not in body. It comes from HTTP header.

---

### 4. `PrivacyResponseEIS.java`

New response wrapper added:

```java
private String RESPONSE;
private String REQUEST_REFERENCE_NUMBER;
private String RESPONSE_DATE;
private String DIGI_SIGN;
```

Meaning: your API now returns EIS-style encrypted response:

```json
{
  "RESPONSE": "encrypted response",
  "REQUEST_REFERENCE_NUMBER": "...",
  "DIGI_SIGN": "signature of plain response"
}
```

---

### 5. `PanApiRSAEncryption.java`

Two important methods added.

#### RSA decrypt AccessToken

```java
public static String RSADecrypt(String encryptedData)
```

Purpose: decrypt `AccessToken` header using channel private key and get AES key.

Flow:

```java
AccessToken header
    ↓ RSA decrypt using private key
AES secret key
```

#### Verify digital signature

```java
public static boolean verifyDigitalSignature(String plainRequest, String receivedSignature)
```

Purpose: verify `DIGI_SIGN` using public key.

Flow:

```java
plainRequest + received DIGI_SIGN + public key
    ↓ SHA256withRSA verify
true / false
```

---

### 6. `PrivacyNoticeAction.java`

This is the main change.

New flow:

```java
String accessToken = request.getHeader("AccessToken");

aesKey = PanApiRSAEncryption.RSADecrypt(accessToken);

String plainRequest = aesEncryption.AESDecryptGCM(
        privacyRequest.getREQUEST(),
        aesKey
);

boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(
        plainRequest,
        privacyRequest.getDIGI_SIGN()
);
```

Meaning:

1. Read encrypted AES key from `AccessToken`.
2. Decrypt AES key using private key.
3. Use AES key to decrypt `REQUEST`.
4. Verify `DIGI_SIGN` against decrypted plain request.
5. Convert plain request JSON into `PrivacyRequestBean`.
6. Process validation/save.
7. Build response.
8. Encrypt response again using same AES key.
9. Sign plain response.
10. Return EIS response format.

---

## Big issue in current code

This line is wrong and dangerous:

```java
isValid = true;
```

Your code does verify DIGI_SIGN, but then immediately overrides the result.

Current code:

```java
boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(
        plainRequest,
        privacyRequest.getDIGI_SIGN()
);

isValid = true;
```

So even if signature fails, request will still continue.

Remove this line:

```java
isValid = true;
```

Correct code:

```java
boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(
        plainRequest,
        privacyRequest.getDIGI_SIGN()
);

logger.info("DIGI_SIGN valid:: " + isValid);

if (!isValid) {
    ServletActionContext.getResponse().setStatus(401);

    messsage.add("Unauthorized Request");

    PrivacyResponseBean responseBean = buildJsonResponseBean(
            false,
            401,
            messsage,
            null,
            null,
            privacyRequestBean.getCorrelationId()
    );

    String finalResponse = bulidFinalResponseJsonObjEIS(
            JSONUtil.beanObjectToJSONObjct(responseBean).toString()
    );

    return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
}
```

---

## How DIGI_SIGN verification works

DIGI_SIGN is **not decrypted**.

It is verified.

### At sender side

Sender signs the **plain JSON request**:

```java
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initSign(channelPrivateKey);
signature.update(plainRequest.getBytes("UTF-8"));

String digiSign = Base64.getEncoder().encodeToString(signature.sign());
```

### At receiver side

Receiver verifies using sender public key:

```java
Signature signature = Signature.getInstance("SHA256withRSA");
signature.initVerify(channelPublicKey);
signature.update(plainRequest.getBytes("UTF-8"));

boolean isValid = signature.verify(Base64.getDecoder().decode(receivedDigiSign));
```

So verification checks:

```text
Was this plain request really signed by the holder of the matching private key?
Was the request changed after signing?
```

If even one character changes in `plainRequest`, verification returns `false`.

---

## Correct verification method

```java
public static boolean verifyDigitalSignature(String plainRequest, String receivedSignature) 
        throws Exception {

    PublicKey publicKey = getPublicKey();

    Signature signature = Signature.getInstance("SHA256withRSA");
    signature.initVerify(publicKey);
    signature.update(plainRequest.getBytes("UTF-8"));

    byte[] signatureBytes = Base64.getDecoder().decode(receivedSignature);

    return signature.verify(signatureBytes);
}
```

## How to test DIGI_SIGN

For testing, generate signature from same plain request:

```java
String digiSign = rsaEncryption.getDigitalSignature(plainRequest);

boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(
        plainRequest,
        digiSign
);

System.out.println("Valid signature = " + isValid);
```

Expected:

```text
Valid signature = true
```

Now modify request slightly:

```java
String tamperedRequest = plainRequest.replace("OCAS000001", "OCAS000002");

boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(
        tamperedRequest,
        digiSign
);

System.out.println("Valid signature = " + isValid);
```

Expected:

```text
Valid signature = false
```

## Final correction needed

Remove this line immediately:

```java
isValid = true;
```

Otherwise DIGI_SIGN validation is only cosmetic and has no security effect.
