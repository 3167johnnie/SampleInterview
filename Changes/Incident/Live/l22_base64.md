For your existing Java 8 + Struts + JSP OTP encryption flow, you can add **one extra Base64 encoding layer after AES encryption**, and remove that layer before AES decryption.

You do not need to change your existing salt, IV, passphrase, iteration count, or OTP verification business logic.

## **1. Updated encryption/decryption flow**

Encryption

**Original JSON / OTP**

**Existing AES encryption**

**Base64 encode encrypted output**

**Send final encoded payload**

Decryption

**Receive Base64 payload**

**Base64 decode**

**Existing AES decryption**

**Original JSON / OTP**

**Important:** Your existing `AesUtil.encrypt()` may already return Base64-encoded AES ciphertext. The changes below add a second Base64 transport layer, as requested. Base64 is encoding, not additional encryption.

## **2.&#x20;**`homesbi.js`**&#x20;— Base64 encode after AES encryption**

Find your existing method:

```
function getEncryptedString(str) {
```

Inside it, you currently have:

```
var encryptedOtp = aesUtil.encrypt(
    json.salt,
    json.iv,
    json.passPhrase,
    str
);

return encryptedOtp;
```

Replace only the return statement:

```
var encryptedOtp = aesUtil.encrypt(
    json.salt,
    json.iv,
    json.passPhrase,
    str
);

// Add Base64 encoding after AES encryption
return btoa(encryptedOtp);
```

This assumes your existing `AesUtil.encrypt()` returns an ASCII Base64 or hex string, as is typical for this utility. `btoa()` cannot directly encode arbitrary Unicode text.

Your OTP submission code remains:

```
getEncryptedString(inputOtp).then(function(encryptedOtp) {

    jQuery("#inputOtpWantUs1").val(encryptedOtp);

    var paramData = getRequest('call_back', 1);

    // Existing AJAX request
});
```

The first-attempt asynchronous fix must remain in place.

## **3.&#x20;**`CommonLoanAction.java`**&#x20;— Base64 decode before AES decryption**

Find your state 5 OTP decryption block:

```
String encryptedOtp =
    stateManagerBean.getOtherRequest().getInputOtpWantUs();

AesUtil aesUtil = new AesUtil(256, 100000);

inputOtp = aesUtil.decrypt(
    saltHex,
    ivHex,
    passPhrase,
    encryptedOtp
);
```

Add the Java 8 Base64 import:

```
import java.util.Base64;
import java.nio.charset.StandardCharsets;
```

Then replace the decryption section with:

```
String encryptedOtp =
    stateManagerBean.getOtherRequest().getInputOtpWantUs();

try {

    // Step 1: Decode outer Base64
    byte[] decodedBytes = Base64.getDecoder().decode(encryptedOtp);

    String decodedEncryptedOtp = new String(
        decodedBytes,
        StandardCharsets.UTF_8
    );

    // Step 2: Existing AES decryption
    String saltHex = SessionUtil.getCryptoSalt();
    String ivHex = SessionUtil.getCryptoIv();
    String passPhrase = SessionUtil.getCryptoPassPhrase();

    AesUtil aesUtil = new AesUtil(256, 100000);

    inputOtp = aesUtil.decrypt(
        saltHex,
        ivHex,
        passPhrase,
        decodedEncryptedOtp
    );

    if (inputOtp == null || !inputOtp.matches("[0-9]{6}")) {
        responseMessage = "error|Invalid OTP request";
        return "jsonResponsePage";
    }

} catch (Exception e) {

    logger.error("OTP decoding/decryption failed", e);

    responseMessage = "error|OTP verification failed";
    return "jsonResponsePage";
}
```

Your existing `processWantUsToCallYou()` call does not need any changes.

## **4.&#x20;**`SbiUtil.java`**&#x20;— Base64 encode final encrypted response**

Find your existing method:

```
public static String getEncryptedResponse(String response)
```

Inside it, locate the final AES encryption operation. It will be similar to:

```
String encryptedResponse = aesUtil.encrypt(
    salt,
    iv,
    pass,
    response
);
```

Instead of returning the encrypted string directly, add Base64 encoding:

```
String encryptedResponse = aesUtil.encrypt(
    salt,
    iv,
    pass,
    response
);

// Base64 encode the final encrypted response
String finalResponse = Base64.getEncoder().encodeToString(
    encryptedResponse.getBytes(StandardCharsets.UTF_8)
);

return finalResponse;
```

Ensure the following imports are present:

```
import java.util.Base64;
import java.nio.charset.StandardCharsets;
```

If your existing method has a `try/catch` or additional return logic, preserve it and modify only the successful encrypted return path.

**Scope warning:** `SbiUtil.getEncryptedResponse()` may be used by other banking APIs. Changing it globally will require every consumer to decode the extra Base64 layer. If other consumers exist, create a callback-specific wrapper instead:

```
public static String getBase64EncryptedResponse(String response) {

    String encrypted = getEncryptedResponse(response);

    return Base64.getEncoder().encodeToString(
        encrypted.getBytes(StandardCharsets.UTF_8)
    );
}
```

Then change only the callback response calls in `CommonLoanAction.home()`:

```
responseMessage = SbiUtil.getBase64EncryptedResponse(responseMessage);
```

This is the safer minimal change for a shared banking application.

## **5.&#x20;**`homesbi.js`**&#x20;— Base64 decode before AES response decryption**

Find your existing:

```
function getDecryptedResponse(htmlContent) {
```

Inside it, you currently have:

```
var decryptedResponse = aesUtil.decrypt(
    json.salt,
    json.iv,
    json.passPhrase,
    htmlContent
);
```

Replace with:

```
// Step 1: Decode the outer Base64 layer
var decodedResponse = atob(htmlContent.trim());

// Step 2: Existing AES decryption
var decryptedResponse = aesUtil.decrypt(
    json.salt,
    json.iv,
    json.passPhrase,
    decodedResponse
);

return decryptedResponse;
```

The rest of the response handling remains:

```
getDecryptedResponse(htmlContent).then(function(decryptedResponse) {

    var stringMessage = decryptedResponse.split("|");

    if (stringMessage[0] === "success") {
        // Existing success logic
    } else if (stringMessage[0] === "error") {
        // Existing error logic
    }
});
```

Do not Base64-decode plaintext responses from state 4 or the existing unencrypted error paths. Your current `home()` method has mixed encrypted and plaintext responses, so retain the plaintext detection before calling `getDecryptedResponse()`.

## **6. If you are encrypting a JSON object instead of an OTP**

For a JSON object, stringify it before passing it to the existing encryption method:

```
var requestJson = {
    requestReferenceNo: "REF12345",
    mobileNo: "XXXXXXXXXX"
};

var jsonString = JSON.stringify(requestJson);

getEncryptedString(jsonString).then(function(encodedPayload) {
    // Send encodedPayload using your existing AJAX flow
});
```

On the Java side, after Base64 decoding and AES decryption:

```
String decryptedJson = aesUtil.decrypt(
    saltHex,
    ivHex,
    passPhrase,
    decodedEncryptedPayload
);

JSONObject requestJson = new JSONObject(decryptedJson);
```

If the server sends encrypted JSON back to JavaScript, after decoding and decrypting:

```
var responseObject = JSON.parse(decryptedResponse);
```

Your existing OTP callback responses use pipe-delimited strings such as `success|message`, not JSON, so do not add `JSON.parse()` to those responses.

## **7. Summary of minimal changes**

| File                                    | Change                                                   |
| --------------------------------------- | -------------------------------------------------------- |
| `homesbi.js` → `getEncryptedString()`   | Add `btoa()` after AES encryption                        |
| `CommonLoanAction.java` → state 5       | Add `Base64.getDecoder().decode()` before AES decryption |
| `SbiUtil.java`                          | Base64 encode final AES-encrypted response               |
| `homesbi.js` → `getDecryptedResponse()` | Add `atob()` before AES decryption                       |
| Existing AJAX submission                | No changes, provided it waits for encryption             |
| `SessionUtil.java`                      | No additional changes                                    |
| Struts XML / `web.xml`                  | No changes                                               |
| JAR dependencies                        | No new dependencies                                      |

### **One security point**

Base64 encoding does not hide the payload from someone who has access to the browser. Your current `getCryptoConfig()` also returns the AES passphrase to JavaScript. The proposed changes preserve the current encryption flow but do not resolve that security weakness. Additionally, your current session-wide IV reuse should be replaced with a fresh IV per encryption in a production design.

**My recommendation:** Add Base64 only to the final AES-encrypted output and decode it immediately before AES decryption. Keep the existing OTP verification and captcha flow unchanged, and scope the new response format to the callback endpoint to avoid breaking other APIs.
