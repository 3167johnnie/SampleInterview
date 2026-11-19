# **Complete step-by-step code changes to fix OTP verification on the first attempt**

I checked your uploaded code, including `homesbi.js`, `HomePopups.jsp`, `SessionUtil.java`, `CommonLoanAction.java`, and the response-encryption method in `SbiUtil.java`.

Your existing OTP process is:

OTP + captcha

AES encrypt

Java verify

The AJAX request must wait until OTP encryption finishes.

## **Root cause in your existing code**

Your `homesbi.js`, around line 1400, contains:

```
getEncryptedString(inputOtp).then(function(encryptedOtp) {
    document.getElementById('inputOtpWantUs1').value = encryptedOtp;
});

var paramData = getRequest('call_back', 1);
```

**Problem:** `getEncryptedString()` makes an asynchronous AJAX call, but `getRequest()` executes immediately.

Therefore, on the first submission, `getRequest()` can read an empty hidden OTP field. By the second submission, encryption from the previous attempt may have populated it.

Your code also has three additional issues:

- Two OTP inputs share the same `name="inputOtpWantUs"`.
- `SessionUtil` calls `.toString()` on potentially null session attributes.
- `CommonLoanAction` may generate new crypto values during state 5, even though the OTP must be decrypted using the original configuration.

We will correct these while preserving your existing Java 8, Struts, JSP, jQuery and OTP verification flow.

## **Files to modify**

| File                    | Changes                                                 |
| ----------------------- | ------------------------------------------------------- |
| `homesbi.js`            | Fix asynchronous OTP submission and response decryption |
| `HomePopups.jsp`        | Correct duplicate OTP parameter                         |
| `SessionUtil.java`      | Correct session getters and remove sensitive logging    |
| `CommonLoanAction.java` | Preserve session crypto during OTP verification         |
| `SbiUtil.java`          | Recommended: remove incompatible encryption fallback    |

No new JAR or Struts action mapping is required for these changes.

## **Step 1 —&#x20;**`HomePopups.jsp`

**File:** `/SBI/WebContent/appNew/common/HomePopups.jsp`

Find the OTP input fields around line 1927 of your uploaded code.

### **Existing code**

```
<input type="text" id="inputOtpWantUs"
       name="inputOtpWantUs"
       class="form-control secure-otp"
       maxlength="6" "/>

<input type="hidden" id="inputOtpWantUs1"
       name="inputOtpWantUs"
       class="form-control secure-otp" />
```

### **Replace with**

```
<input type="text"
       id="inputOtpWantUs"
       class="form-control secure-otp"
       maxlength="6"
       autocomplete="one-time-code" />

<input type="hidden"
       id="inputOtpWantUs1"
       name="inputOtpWantUs"
       value="" />
```

### **What changed?**

The visible input no longer has `name="inputOtpWantUs"`. Only the hidden input has that name, so normal form serialization submits only the encrypted OTP.

I also removed the stray `"` after `maxlength="6"`.

**Important:** Your `getRequest()` function must collect the hidden input's value. This change is correct for normal name-based form serialization. If your custom `getRequest()` uses fixed IDs, it must specifically read `inputOtpWantUs1` rather than `inputOtpWantUs`.

## **Step 2 —&#x20;**`homesbi.js`**: Fix first-attempt OTP submission**

**File:** `/JS/ocas/sbiNew/js/homesbi.js`

Find the click handler around line 1385:

```
jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
```

Replace the **entire existing&#x20;**`#confirmOtpWantUs`**&#x20;click handler** with the code below.

This retains your success screen, thank-you message, lead tracking, and existing `home` endpoint.

```

jQuery("#confirmOtpWantUs").unbind("click").bind("click", function(event) {

    event.preventDefault();

    hidePOPMsg('errorOTPMsg');

    // Step 1: Validate OTP and captcha
    if (!validateWantUstoCallyou(this, 3)) {
        return false;
    }

    var inputOtp = jQuery("#inputOtpWantUs").val();

    if (!/^[0-9]{6}$/.test(inputOtp)) {
        showPOPMsg('errorOTPMsg', 'Please enter valid OTP', false);
        return false;
    }

    // Step 2: Prevent duplicate submissions
    if (jQuery("#confirmOtpWantUs").prop("disabled")) {
        return false;
    }

    jQuery("#confirmOtpWantUs").prop("disabled", true);
    showDynamicLoader('confirmOtpWantUs');

    // Clear any OTP ciphertext from a previous attempt
    jQuery("#inputOtpWantUs1").val("");

    // Step 3: Wait for encryption to complete
    getEncryptedString(inputOtp).then(function(encryptedOtp) {

        if (!encryptedOtp || typeof encryptedOtp !== "string") {
            throw new Error("OTP encryption failed");
        }

        // Step 4: Populate encrypted OTP
        jQuery("#inputOtpWantUs1").val(encryptedOtp);

        // Step 5: Prepare request AFTER encryption
        var paramData = getRequest('call_back', 1);

        if (!paramData) {
            throw new Error("Unable to prepare OTP request");
        }

        // Preserve existing getRequest() encoding contract
        var formdata = "params=" + paramData;
        formdata += "&requestIndex=5&requestType=2";

        if (typeof uiType !== "undefined" && uiType !== "") {
            formdata += "&uiType=" + uiType;
        }

        // Step 6: Send OTP verification request
        return jQuery.ajax({
            type: "POST",
            url: ctxPath + "/" + ajaxPostUrl,
            data: formdata

        }).then(function(htmlContent) {

            if (!htmlContent) {
                throw new Error("Empty OTP verification response");
            }

            // Step 7: Decrypt encrypted response
            // Some existing Java error paths return plaintext.
            var responsePromise;

            if (/^(success|error)\|/.test(htmlContent)) {
                responsePromise = Promise.resolve(htmlContent);
            } else {
                responsePromise = getDecryptedResponse(htmlContent);
            }

            return responsePromise.then(function(responseText) {

                var stringMessage = responseText.split("|");

                if (stringMessage[0] === "success") {

                    // Existing lead tracking
                    if (jQuery("#leadLoanPurposeId").val() == 1) {
                        if (DEPLOYMENT_MODE != 'local' &&
                            DEPLOYMENT_MODE != 'uat_rp') {

                            M.byId('go2CloudforcallBack').innerHTML =
                                '<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=HOME" width="1" height="1" />';
                        }

                    } else if (jQuery("#leadLoanPurposeId").val() == 3) {

                        if (DEPLOYMENT_MODE != 'local' &&
                            DEPLOYMENT_MODE != 'uat_rp') {

                            M.byId('go2CloudforcallBack').innerHTML =
                                '<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=AUTO" width="1" height="1" />';
                        }
                    }

                    jQuery("#wantus_row_confirm").hide();
                    jQuery("#afterGetACallBack").hide();

                    var successMsg =
                        '<div class="thnk-msg">' +
                        '<h2><span class="smile-icon"></span><br>Thank you </h2>' +
                        '<p>Thank You for your interest. Our representative will contact you shortly.</p>' +
                        '</div>';

                    jQuery("#errorOTPMsg").removeClass('error-msg-cbs');
                    jQuery("#errorOTPMsg").html(successMsg).show();
                    jQuery("#resend_box").hide();

                    setTimeout(function() {
                        jQuery(".close").trigger("click");
                        reinitiateCallback();
                    }, 2000);

                    if (DEPLOYMENT_MODE != 'local' &&
                        DEPLOYMENT_MODE != 'uat_rp') {

                        fbq('track', 'Lead', { loan: 'Callback' });
                    }

                } else if (stringMessage[0] === "error") {

                    showPOPMsg(
                        'errorOTPMsg',
                        stringMessage[1] || 'OTP verification failed',
                        false
                    );

                } else {
                    throw new Error("Unexpected OTP verification response");
                }
            });
        });

    }).catch(function(error) {

        // Do not log OTP or encryption configuration
        console.error("OTP processing failed:", error.message);

        showPOPMsg(
            'errorOTPMsg',
            'Unable to process OTP verification. Please try again.',
            false
        );

    }).then(function() {

        // Step 8: Cleanup after completion
        removeDynamicLoader('confirmOtpWantUs');

        jQuery("#confirmOtpWantUs").prop("disabled", false);

        // Clear old encrypted OTP
        jQuery("#inputOtpWantUs1").val("");

    });

    return false;
});

```

### **Why this fixes the first attempt**

The previous execution order was:

`Start encryption → prepare request → submit request → encryption finishes`

The new execution order is:

`Start encryption → encryption finishes → set hidden OTP → prepare request → submit request`

The backend should now receive the encrypted OTP generated for the current submission, provided `getRequest()` serializes the hidden field correctly.

The code above retains the original form-data construction because your `getRequest()` implementation is not included in the inspected section. Do not add a second encoding layer without checking that function.

## **Step 3 —&#x20;**`SessionUtil.java`**: Correct all three crypto getters**

**File:** `/SBI/src/com/mintstreet/common/session/SessionUtil.java`

In your uploaded code, the crypto methods appear near lines 7132–7188.

### **3.1 Replace&#x20;**`getCryptoSalt()`

Your current method logs the session and calls `.toString()` before checking whether the attribute exists.

Replace the complete method with:

```
public static String getCryptoSalt() {

    Object value = getSessionAction().getAttribute(CRYPTO_SALT);

    if (value != null) {
        return value.toString();
    }

    return null;
}
```

### **3.2 Replace&#x20;**`getCryptoIv()`

Replace the complete method with:

```
public static String getCryptoIv() {

    Object value = getSessionAction().getAttribute(CRYPTO_IV);

    if (value != null) {
        return value.toString();
    }

    return null;
}
```

### **3.3 Replace&#x20;**`getCryptoPassPhrase()`

Replace the complete method with:

```
public static String getCryptoPassPhrase() {

    Object value = getSessionAction()
            .getAttribute(CRYPTO_PASSPHRASE);

    if (value != null) {
        return value.toString();
    }

    return null;
}
```

### **3.4 Update the existing setters**

Your setters currently log the actual salt, IV and passphrase. Remove these logging statements.

For example, replace the three setter methods with:

```
public static void setCryptoSalt(String cryptoSalt) {
    getSessionAction().setAttribute(CRYPTO_SALT, cryptoSalt);
}

public static void setCryptoIv(String cryptoIv) {
    getSessionAction().setAttribute(CRYPTO_IV, cryptoIv);
}

public static void setCryptoPassPhrase(String cryptoPassPhrase) {
    getSessionAction().setAttribute(CRYPTO_PASSPHRASE, cryptoPassPhrase);
}
```

### **Why these changes matter**

Previously, `getCryptoSalt()` could throw a `NullPointerException` before reaching the null check. Your `getCryptoConfig()` catches that exception and may regenerate configuration values.

The new getters return `null` safely when the configuration has not yet been initialized.

They also consistently read the same session store used by the setters. This assumes `getSessionAction()` resolves the current HTTP session for both AJAX requests. Verify that both endpoints receive the same session cookie and that session affinity is configured if the application runs on multiple Tomcat nodes.

## **Step 4 —&#x20;**`CommonLoanAction.java`**: Prevent crypto regeneration during OTP verification**

**File:** `/SBI/src/com/mintstreet/common/action/CommonLoanAction.java`

There are two places to change:

1. The `home()` method.
2. The `getCryptoConfig()` method.

### **4.1 Update&#x20;**`home()`**&#x20;— state 3, 4 and 5 block**

Find the following existing code near line 177:

```
if(stateManagerBean.getState()==3 || stateManagerBean.getState()==4 || stateManagerBean.getState()==5){
      // Ensure crypto keys exist in session before encrypting response
    if (SessionUtil.getCryptoSalt() == null) {
        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        String passPhrase = UUID.randomUUID().toString() + UUID.randomUUID().toString();

        SessionUtil.setCryptoSalt(Hex.encodeHexString(saltBytes));
        SessionUtil.setCryptoIv(Hex.encodeHexString(ivBytes));
        SessionUtil.setCryptoPassPhrase(passPhrase);
    }
    try {
```

Replace that opening section with:

```
if (stateManagerBean.getState() == 3
        || stateManagerBean.getState() == 4
        || stateManagerBean.getState() == 5) {

    // Do not generate new crypto values during OTP verification.
    if (stateManagerBean.getState() == 5) {

        if (SessionUtil.getCryptoSalt() == null
                || SessionUtil.getCryptoIv() == null
                || SessionUtil.getCryptoPassPhrase() == null) {

            logger.error("OTP verification crypto session unavailable");

            responseMessage =
                "error|Session expired. Please restart OTP verification.";

            return "jsonResponsePage";
        }
    }

    try {
```

**Keep the rest of your existing&#x20;**`try`**&#x20;block unchanged**, except for the OTP validation changes in the next step.

For states 3 and 4, the normal response-encryption path still requires an initialized crypto session. Because your JavaScript calls `getCryptoConfig()` to decrypt the response, you should initialize the configuration at the start of a fresh callback session or explicitly initialize it in those states before producing encrypted responses. Do not initialize it during state 5.

### **4.2 Update state 5 OTP decryption**

Find the following code inside `home()`:

```
String encryptedOtp = stateManagerBean.getOtherRequest().getInputOtpWantUs();

try {
    String saltHex = SessionUtil.getCryptoSalt();
    String ivHex = SessionUtil.getCryptoIv();
    String passPhrase = SessionUtil.getCryptoPassPhrase();
    AesUtil aesUtil = new AesUtil(256, 100000);
    inputOtp = aesUtil.decrypt(saltHex, ivHex, passPhrase, encryptedOtp);
} catch (Exception e) {
    logger.error("OTP decryption failed", e);
    responseMessage = "error|OTP verification failed. Please try again.";
    return "jsonResponsePage";
}
```

Replace with:

```
String encryptedOtp =
    stateManagerBean.getOtherRequest().getInputOtpWantUs();

if (encryptedOtp == null || encryptedOtp.trim().isEmpty()) {

    logger.warn("Encrypted OTP missing in state 5 request");

    responseMessage = "error|Invalid OTP request";
    return "jsonResponsePage";
}

try {

    String saltHex = SessionUtil.getCryptoSalt();
    String ivHex = SessionUtil.getCryptoIv();
    String passPhrase = SessionUtil.getCryptoPassPhrase();

    AesUtil aesUtil = new AesUtil(256, 100000);

    inputOtp = aesUtil.decrypt(
        saltHex,
        ivHex,
        passPhrase,
        encryptedOtp
    );

    if (inputOtp == null || !inputOtp.matches("[0-9]{6}")) {

        logger.warn("Decrypted OTP format invalid");

        responseMessage = "error|Invalid OTP request";
        return "jsonResponsePage";
    }

} catch (Exception e) {

    logger.error("OTP decryption failed", e);

    responseMessage =
        "error|Unable to process OTP verification. Please try again.";

    return "jsonResponsePage";
}
```

Keep the following existing lines after this block:

```
lead = commonService.getLeadById(SessionUtil.getLeadId());
```

And keep the existing OTP verification call:

```
json = processManagerImpl.processWantUsToCallYou(
    SessionUtil.getLeadId(),
    stateManagerBean.getState(),
    lead,
    visitId,
    Constants.OTHER_ID_INTEGER,
    inputOtp
);
```

This is important because your existing business logic already handles OTP verification. We are only correcting how the OTP reaches it.

### **4.3 Replace&#x20;**`getCryptoConfig()`

Your current implementation contains two hardcoded fallback sections. These can silently return keys that do not match the values stored in the session.

Replace the complete existing `getCryptoConfig()` method with:

```

public StreamResult getCryptoConfig() {

    JSONObject json = new JSONObject();

    try {

        String salt = SessionUtil.getCryptoSalt();
        String iv = SessionUtil.getCryptoIv();
        String passPhrase = SessionUtil.getCryptoPassPhrase();

        // Initialize only when no configuration exists.
        if (salt == null && iv == null && passPhrase == null) {

            byte[] saltBytes = new byte[16];
            byte[] ivBytes = new byte[16];

            SecureRandom secureRandom = new SecureRandom();

            secureRandom.nextBytes(saltBytes);
            secureRandom.nextBytes(ivBytes);

            salt = Hex.encodeHexString(saltBytes);
            iv = Hex.encodeHexString(ivBytes);

            passPhrase = UUID.randomUUID().toString()
                    + UUID.randomUUID().toString();

            SessionUtil.setCryptoSalt(salt);
            SessionUtil.setCryptoIv(iv);
            SessionUtil.setCryptoPassPhrase(passPhrase);

            logger.info("Crypto configuration initialized for session");

        } else if (salt == null || iv == null || passPhrase == null) {

            // Partial configuration is invalid.
            // Never silently replace keys during an OTP flow.
            throw new IllegalStateException(
                "Incomplete session crypto configuration"
            );
        }

        json.put("salt", salt);
        json.put("iv", iv);
        json.put("passPhrase", passPhrase);
        json.put("keySize", 256);
        json.put("iterationCount", 100000);

    } catch (Exception e) {

        logger.error("getCryptoConfig failed", e);

        json = new JSONObject();
        json.put("status", "error");
        json.put("message", "Crypto configuration unavailable");
    }

    return new StreamResult(
        new ByteArrayInputStream(
            json.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8)
        )
    );
}

```

### **Important distinction**

This change generates crypto configuration once per session, not once per OTP attempt.

That is necessary for compatibility with your current code because `AesUtil.decrypt()` must use the same parameters used by `AesUtil.encrypt()`.

However, a **session-wide reused IV is not suitable for production AES-CBC encryption**. This code is an interim compatibility fix for your existing implementation, not a production cryptographic design. In particular, returning the passphrase in `getCryptoConfig()` exposes it in the browser Network tab.

A production implementation should use a reviewed protocol with fresh unpredictable IVs/nonces per encryption, authenticated encryption, and no browser-exposed symmetric secret.

## **Step 5 —&#x20;**`homesbi.js`**: Correct&#x20;**`getEncryptedString()`**&#x20;and response decryption**

At the bottom of your existing `homesbi.js`, around lines 1603–1650, you have three methods:

```
getEncryptedString(str)
isEncrypted(str)
getDecryptedResponse(htmlContent)
```

Currently, `isEncrypted()` and `getDecryptedResponse()` both make separate calls to `getCryptoConfig()`.

For the callback flow, you can eliminate the extra `isEncrypted()` call.

### **5.1 Replace&#x20;**`getEncryptedString()`

Find:

```
function getEncryptedString(str) {
```

Replace the complete method with:

```
function getEncryptedString(str) {

    return jQuery.ajax({
        type: "POST",
        url: ctxPath + "/getCryptoConfig",
        dataType: "json",
        cache: false

    }).then(function(json) {

        if (!json ||
            !json.salt ||
            !json.iv ||
            !json.passPhrase) {

            throw new Error("Invalid crypto configuration");
        }

        var aesUtil = new AesUtil(
            Number(json.keySize),
            Number(json.iterationCount)
        );

        var encryptedOtp = aesUtil.encrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            str
        );

        if (!encryptedOtp) {
            throw new Error("OTP encryption failed");
        }

        return encryptedOtp;
    });
}
```

**Difference from your existing code:** The old method catches encryption errors and returns `false`. The new method rejects the promise so that the OTP submission is stopped before reaching Java.

### **5.2 Replace&#x20;**`getDecryptedResponse()`

Find:

```
function getDecryptedResponse(htmlContent) {
```

Replace the complete method with:

```
function getDecryptedResponse(htmlContent) {

    return jQuery.ajax({
        type: "POST",
        url: ctxPath + "/getCryptoConfig",
        dataType: "json",
        cache: false

    }).then(function(json) {

        if (!json ||
            !json.salt ||
            !json.iv ||
            !json.passPhrase) {

            throw new Error("Crypto configuration unavailable");
        }

        var aesUtil = new AesUtil(
            Number(json.keySize),
            Number(json.iterationCount)
        );

        var decryptedResponse = aesUtil.decrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            htmlContent
        );

        if (typeof decryptedResponse !== "string" ||
            !/^(success|error)\|/.test(decryptedResponse)) {

            throw new Error("Invalid decrypted response");
        }

        return decryptedResponse;
    });
}
```

### **5.3 Update the first OTP-generation response**

Your `submit_first_page_bind()` function around line 1129 currently uses:

```
isEncrypted(htmlContent).then(function(result) {

    if (result) {

        getDecryptedResponse(htmlContent).then(function(decryptedResponse) {

            stringMessage = decryptedResponse.split("|");

            // Existing success/error handling
        });
    }
});
```

Replace the outer `isEncrypted()` structure with:

```
var responsePromise;

if (/^(success|error)\|/.test(htmlContent)) {
    responsePromise = Promise.resolve(htmlContent);
} else {
    responsePromise = getDecryptedResponse(htmlContent);
}

responsePromise.then(function(decryptedResponse) {

    stringMessage = decryptedResponse.split("|");

    // KEEP your existing success/error handling here.

}).catch(function() {

    showPOPMsg(
        'errorOTPMsg',
        'Unable to process OTP response',
        false
    );
});
```

Keep all your existing UI logic that displays the OTP section, masks the mobile number, refreshes captcha and shows the resend option.

The plaintext check is needed because your existing Java `home()` returns some validation errors without encrypting them.

### **5.4 Correct the resend OTP handler**

In your `#resendOtp` handler around line 1350, you have:

```
if (isEncrypted(htmlContent)) {

    decryptedResponse = getDecryptedResponse(htmlContent);
    stringMessage = decryptedResponse.split("|");

} else {

    stringMessage = htmlContent.split("|");
}
```

This is incorrect because both `isEncrypted()` and `getDecryptedResponse()` return promises.

Your Java `home()` method currently does not encrypt state 4 responses:

```
if(stateManagerBean.getState()!=4) {
    responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
}
```

Therefore, replace the above JavaScript block with:

```
var stringMessage = htmlContent.split("|");
```

Keep the rest of the existing resend success/error logic unchanged.

### **5.5 What about&#x20;**`isEncrypted()`**?**

After updating the callback handlers, `isEncrypted()` is no longer needed in those paths.

Do not delete the function until you search the entire project and confirm no other code calls it.

## **Step 6 —&#x20;**`SbiUtil.java`**: Remove incompatible fallback encryption**

**File:** `/SBI/src/com/mintstreet/common/util/SbiUtil.java`

Your existing `getEncryptedResponse()` method contains:

```
if (salt == null || iv == null || pass == null) {
    salt = EncryptDecryptUtil.decrypt(Constants.CRM_NEW_AES_KEY)
            .substring(18, 50);
    iv = salt;
    pass = EncryptDecryptUtil.decrypt(String.valueOf(passphrase));
}
```

This fallback uses different keys from your new session configuration.

For the current session-based flow, replace that block with:

```
if (salt == null || iv == null || pass == null) {

    logger.error("Crypto session unavailable for response encryption");

    throw new IllegalStateException(
        "Crypto session unavailable"
    );
}
```

Keep the remaining encryption code unchanged.

This prevents Java from silently encrypting a response using keys that JavaScript does not have.

Because this method may be shared by other loan flows, check its callers before deploying the change globally.

## **Step 7 — Testing the changes**

After updating the files, rebuild and deploy the application, clear the browser's cached JavaScript, and start a fresh session.

Use the following checklist:

| Test case                        | Expected result                               |
| -------------------------------- | --------------------------------------------- |
| Enter correct OTP and captcha    | Successful on first attempt                   |
| Enter incorrect OTP              | Verification rejected                         |
| Submit empty OTP                 | No verification AJAX call                     |
| Click Confirm twice quickly      | Only one request                              |
| Delay `getCryptoConfig` response | `home` verification request waits             |
| Resend OTP                       | Existing resend flow works                    |
| Session expires                  | Controlled error                              |
| Decryption fails                 | Error shown without exposing sensitive values |

### **How to confirm the first-attempt fix**

Open Chrome DevTools → Network.

On clicking Confirm, you should observe:

1. `getCryptoConfig` request completes.
2. The `home` request with `requestIndex=5` is sent.
3. The serialized request contains the encrypted OTP field.
4. Java decrypts the OTP and passes it to `processWantUsToCallYou()`.
5. The response is processed and the success screen appears.

Do not log the OTP, encryption passphrase or decrypted values.

If the first attempt still fails after these changes, inspect the `getRequest('call_back', 1)` implementation and the Java state-manager mapping for `inputOtpWantUs`. Those are the remaining important points that can cause an incorrect value to reach `OtherRequest`.

## **Final summary**

The most important correction is in `homesbi.js`:

**Before:**

```
getEncryptedString(inputOtp).then(function(encryptedOtp) {
    jQuery("#inputOtpWantUs1").val(encryptedOtp);
});

var paramData = getRequest('call_back', 1);
```

**After:**

```
getEncryptedString(inputOtp).then(function(encryptedOtp) {

    jQuery("#inputOtpWantUs1").val(encryptedOtp);

    var paramData = getRequest('call_back', 1);

    // Submit your existing AJAX request here.
});
```

Together with removing the duplicate OTP parameter and preventing crypto regeneration during state 5, this addresses the main defects identified in your existing code.

**One important qualification:** Your current encryption scheme still exposes the symmetric passphrase to the browser and reuses session cryptographic parameters. These changes target the first-attempt functional bug; they should not be treated as sufficient for banking security approval.
