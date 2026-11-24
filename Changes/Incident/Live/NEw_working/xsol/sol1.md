Based on the attached existing Home Loan code and your previously shared working OTP encryption implementation, you can implement dynamic encryption and decryption with minimal changes by reusing the existing `getCryptoConfig()`, `SessionUtil.java`, `SbiUtil.java` and `AesUtil`.

**There is no need to create another encryption API specifically for Home Loan.** We can reuse the existing common API and retain the current Struts request flow, OTP verification logic and AJAX endpoints.

# **1. Existing problem in the Home Loan module**

Currently, both JavaScript files contain the following hardcoded encryption configuration:

```
var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";
```

These values are present in:

- `jquery.homeloanOTP.js` — around line 999 of the first attachment.
- `jquery.homeloan_quote.js` — around line 1895 of the first attachment.

Because the salt, IV and passphrase are static, encrypting the same OTP with the same configuration produces the same ciphertext.

Additionally, your previously implemented `getCryptoConfig()` generates fresh values whenever it is called. Therefore, the frontend and backend must use the corresponding configuration throughout each encryption/decryption operation.

Calling `getCryptoConfig()` again before decrypting a response could overwrite the session values and cause OTP verification or response decryption to fail.

# **2. Files requiring changes**

| File name                     | Changes required                                                                 |
| ----------------------------- | -------------------------------------------------------------------------------- |
| `jquery.homeloanOTP.js`       | Mandatory                                                                        |
| `jquery.homeloan_quote.js`    | Mandatory                                                                        |
| `HomeSecondPage.jsp`          | OTP input field changes                                                          |
| `HomeLoanAction.java`         | Minimal changes and verification                                                 |
| `HomeProcessManagerImpl.java` | Verify existing OTP decryption                                                   |
| `CommonLoanAction.java`       | Reuse existing API                                                               |
| `SessionUtil.java`            | Reuse existing methods; null-safety corrections                                  |
| `SbiUtil.java`                | Reuse existing encryption/decryption; prevent static fallback for migrated flows |
| `struts-common-loan.xml`      | No new mapping required if existing mapping is deployed                          |
| `web.xml`                     | No new changes expected                                                          |
| `AesUtil.java`                | No changes for this compatibility migration                                      |

## **Existing request indexes**

Your Home Loan module uses two different OTP flows.

| Request index | Operation                  |
| ------------- | -------------------------- |
| 3             | Callback OTP generation    |
| 4             | Callback OTP resend        |
| 5             | Callback OTP verification  |
| 14            | Home Loan OTP generation   |
| 15            | Home Loan OTP resend       |
| 16            | Home Loan OTP verification |

We will preserve all these request indexes.

## **Proposed execution flow**

The following changes preserve the existing distinction between encrypted responses and plaintext responses, particularly for resend OTP and alternate mobile OTP.

# **3. Add a reusable JavaScript crypto configuration**

Instead of duplicating the encryption methods in both Home Loan JavaScript files, create one common JavaScript file for the Home Loan module.

This is the only new file I recommend. It avoids maintaining two separate caches that could become inconsistent with the Java session.

### **Step 3.1 — Create the file**

**File path:**

```
JS/ocas/sbiNew/js/app/loan/homeloan/js/
jquery.homeloan.crypto.js
```

Add the following code:

```
var _hlCryptoConfigCache = null;
var _hlCryptoConfigRequest = null;

function resetHLCryptoConfig() {
    _hlCryptoConfigCache = null;
}

function getHLCryptoConfigOnce() {

    if (_hlCryptoConfigCache) {
        return jQuery.when(_hlCryptoConfigCache);
    }

    if (_hlCryptoConfigRequest) {
        return _hlCryptoConfigRequest;
    }

    _hlCryptoConfigRequest = jQuery.ajax({
        type: 'POST',
        url: HL_CRYPTO_CONFIG_URL,
        dataType: 'json',
        cache: false
    }).then(function(json) {

        if (!json || !json.salt ||
            !json.iv || !json.passPhrase) {

            throw new Error("Invalid crypto configuration");
        }

        _hlCryptoConfigCache = json;

        return json;
    });

    _hlCryptoConfigRequest.always(function() {
        _hlCryptoConfigRequest = null;
    });

    return _hlCryptoConfigRequest;
}

function getHLEncryptedString(str) {

    return getHLCryptoConfigOnce().then(function(json) {

        var aesUtil = new AesUtil(
            json.keySize,
            json.iterationCount
        );

        var encryptedValue = aesUtil.encrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            str
        );

        if (!encryptedValue) {
            throw new Error("Encryption failed");
        }

        return encryptedValue;
    });
}

function getHLDecryptedResponse(htmlContent) {

    if (!_hlCryptoConfigCache) {
        throw new Error("Crypto configuration unavailable");
    }

    var json = _hlCryptoConfigCache;

    var aesUtil = new AesUtil(
        json.keySize,
        json.iterationCount
    );

    return aesUtil.decrypt(
        json.salt,
        json.iv,
        json.passPhrase,
        htmlContent
    );
}

function isHLEncrypted(str) {

    if (!str || !str.trim() || !_hlCryptoConfigCache) {
        return false;
    }

    try {

        var json = _hlCryptoConfigCache;

        var aesUtil = new AesUtil(
            json.keySize,
            json.iterationCount
        );

        var dec1 = aesUtil.decrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            str
        );

        var enc1 = aesUtil.encrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            dec1
        );

        return enc1 === str;

    } catch (err) {
        return false;
    }
}

function getHLResponseMessage(htmlContent) {

    if (typeof htmlContent !== "string" || !htmlContent) {
        throw new Error("Empty response");
    }

    // Existing backend returns some errors in plain text.
    if (htmlContent.indexOf("error|") === 0) {
        return htmlContent.split("|");
    }

    if (isHLEncrypted(htmlContent)) {

        var decryptedResponse =
            getHLDecryptedResponse(htmlContent);

        var stringMessage = decryptedResponse.split("|");

        if (stringMessage[0] === "success" ||
            stringMessage[0] === "error") {

            return stringMessage;
        }
    }

    throw new Error("Invalid encrypted response");
}
```

### **Why am I making this small change to your working approach?**

Your working implementation makes `isEncrypted()` and `getDecryptedResponse()` asynchronous.

However, your existing Home Loan code uses:

```
if (isEncrypted(htmlContent)) {
```

This expects a synchronous boolean value.

If we directly copy your previous implementation, this condition would receive a Promise instead of a boolean, which could cause incorrect execution.

Therefore:

- Configuration fetching remains asynchronous.
- OTP encryption remains asynchronous.
- Response decryption uses the already cached configuration synchronously.
- No additional `getCryptoConfig()` call occurs during response decryption.
- Existing plaintext error responses remain supported.
- Both Home Loan JavaScript files share the same configuration.

This reduces the number of changes required in existing AJAX success functions.

# **4. Changes in HomeSecondPage.jsp**

**File:**

```
/SBI/WebContent/appNew/loan/homeloan/HomeSecondPage.jsp
```

## **Step 4.1 — Include the common crypto JavaScript**

Around line 984, find the existing script inclusion:

```
<script type="text/javascript"
    src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>">
</script>
```

Add the following **before the existing script inclusion**:

```
<script type="text/javascript">
    var HL_CRYPTO_CONFIG_URL =
        '<s:url action="getCryptoConfig" />';
</script>

<script type="text/javascript"
    src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js/jquery.homeloan.crypto.js?v=<s:property value="%{#SCRIPT_VERSION}"/>">
</script>
```

Do not remove the existing `jquery.homeloan_quote.js` inclusion.

**Important:** The common crypto JavaScript must also be included on the JSP that loads `jquery.homeloanOTP.js`, before its OTP handlers are used. That JSP inclusion was not identifiable in the supplied files. If both scripts are loaded through a common Home Loan parent JSP, include the new file there only once instead.

Also verify that the generated Struts URL points to your existing working `getCryptoConfig` endpoint.

## **Step 4.2 — Remove duplicate OTP field names**

Around lines 907–908, your existing code is:

```
<input type="text"
    id="inputOtpWantUs"
    name="inputOtpWantUs"
    class="form-control secure-otp"
    maxlength="6"
    onkeydown="return M.digit(event);"/>

<input type="hidden"
    id="inputOtpWantUs1"
    name="inputOtpWantUs"
    class="form-control secure-otp"
    maxlength="6"
    onkeydown="return M.digit(event);"/>
```

Both fields have the same name.

### **Replace with:**

```
<input type="text"
    id="inputOtpWantUs"
    class="form-control secure-otp"
    maxlength="6"
    onkeydown="return M.digit(event);"/>

<input type="hidden"
    id="inputOtpWantUs1"
    name="inputOtpWantUs"/>
```

The visible field is used to enter the OTP. The hidden field is used to submit the encrypted OTP.

| Field                   | Purpose                    |
| ----------------------- | -------------------------- |
| `inputOtpWantUs`        | Visible OTP input          |
| `inputOtpWantUs1`       | Encrypted OTP              |
| `name="inputOtpWantUs"` | Existing backend parameter |

If your jQuery validation configuration depends on the visible field's name, use a distinct UI-only name and explicitly exclude it from `getRequest()` serialization, or validate the visible field by ID.

Do not allow both the plaintext and encrypted OTP to be serialized.

# **5. Changes in jquery.homeloanOTP.js**

**File path:**

```
JS/ocas/sbiNew/js/app/loan/homeloan/js/
jquery.homeloanOTP.js
```

This file contains the primary mobile OTP, alternate mobile OTP and email OTP flows.

## **Step 5.1 — Remove static encryption configuration**

Around lines 999–1003, remove:

```
var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";
```

No replacement is required here because the configuration will be obtained from the common crypto JavaScript.

## **Step 5.2 — Modify Send OTP — requestIndex 14**

Around line 1266, find:

```
jQuery("#sendOpt").unbind("click").bind("click",function(){
```

Keep all existing mobile number, email, CAPTCHA and form validations.

Around line 1320, find:

```
var paramData = getRequest('otpApplication', 0);
```

Immediately **before this line**, add:

```
resetHLCryptoConfig();

getHLCryptoConfigOnce().then(function(json) {
```

Your code will now look like:

```
resetHLCryptoConfig();

getHLCryptoConfigOnce().then(function(json) {

    var paramData = getRequest('otpApplication', 0);

    if (!paramData) {

        var errorMsg =
            "Sorry for the inconvenience, please try again later.";

        showPOPMsg('errorOTPMsg', errorMsg, false);

        removeDynamicLoader('sendOpt');

        return false;
    }

    var formdata = "params=" + paramData;

    formdata += "&requestIndex=14&requestType=2";

    if (uiType != undefined && uiType != "") {
        formdata += "&uiType=" + uiType;
    }

    // Existing AJAX call continues here.
```

Do not remove the existing AJAX call.

### **Step 5.3 — Modify the Send OTP response**

Inside the same AJAX success function, around lines 1344–1354, find:

```
var stringMessage = [];

if (isEncrypted(htmlContent)) {

    console.log('content is ' + htmlContent);

    decryptedResponse = getDecryptedResponse(htmlContent);

    console.log('decryptedResponse is ' + decryptedResponse);

    stringMessage = decryptedResponse.split("|");

} else {

    stringMessage = htmlContent.split("|");
}
```

Replace the entire block with:

```
var stringMessage = [];

try {

    stringMessage = getHLResponseMessage(htmlContent);

} catch (err) {

    removeDynamicLoader('sendOpt');

    showPOPMsg(
        'errorOTPMsg',
        SORRY_FOR_INCONVENIENCE,
        false
    );

    return false;
}
```

Keep the existing business logic starting from:

```
if (stringMessage[0] == "success") {
```

No changes are required in the existing popup display, CAPTCHA refresh or OTP confirmation section.

### **Step 5.4 — Close the configuration Promise**

At the end of the existing Send OTP AJAX call, find:

```
            });
        }
        return false;
    });
```

Replace with:

```
            });

        }).fail(function() {

            removeDynamicLoader('sendOpt');

            showPOPMsg(
                'errorOTPMsg',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });

        }
        return false;
    });
```

The additional closing block belongs to:

```
getHLCryptoConfigOnce().then(function(json) {
```

Now the backend session configuration will be generated before requestIndex 14 is submitted.

## **Step 5.5 — Modify Confirm OTP — requestIndex 16**

Around line 1040, find:

```
jQuery("#confirmOtp").unbind("click").bind("click",function(){
```

Keep all existing validation logic.

Around lines 1079–1093, find:

```
if (isEmailOtp == false) {

    var inputOtp =
        document.getElementById('inputOtp').value;

    if (inputOtp != "" || inputOtp != '') {

        var encryptedOtp = getEncryptedStr(inputOtp);

        document.getElementById('inputOtp1').value =
            encryptedOtp;
    }

} else if (isEmailOtp == true) {

    var inputOtpEmail =
        document.getElementById('inputOtpEmail').value;

    document.getElementById('inputOtpEmail').value =
        inputOtpEmail;
}
```

### **Replace the entire block with:**

```
var otpEncryptionPromise;

if (isEmailOtp == false) {

    var inputOtp =
        document.getElementById('inputOtp').value;

    if (!inputOtp) {

        removeDynamicLoader('confirmOtp');

        showPOPMsg(
            'errorOTPMsg',
            'Please enter mobile OTP',
            false
        );

        return false;
    }

    otpEncryptionPromise =
        getHLEncryptedString(inputOtp);

} else {

    // Preserve existing email OTP behaviour.
    otpEncryptionPromise =
        getHLCryptoConfigOnce().then(function() {
            return null;
        });
}

otpEncryptionPromise.then(function(encryptedOtp) {

    if (isEmailOtp == false) {

        document.getElementById('inputOtp1').value =
            encryptedOtp;
    }
```

Immediately after this, keep your existing code:

```
var paramData = getRequest('otpApplication', 0);
```

All the existing form serialization and AJAX code must now be inside the `otpEncryptionPromise.then()` block.

### **The updated structure will be:**

```
otpEncryptionPromise.then(function(encryptedOtp) {

    if (isEmailOtp == false) {

        document.getElementById('inputOtp1').value =
            encryptedOtp;
    }

    var paramData = getRequest('otpApplication', 0);

    if (!paramData) {

        removeDynamicLoader('confirmOtp');

        showPOPMsg(
            'errorOTPMsg',
            SORRY_FOR_INCONVENIENCE,
            false
        );

        return false;
    }

    var formdata = "params=" + paramData;

    formdata += "&requestIndex=16&requestType=2";

    // Continue existing code and AJAX call.
```

**This is the most important change for OTP verification.** The AJAX request must wait until encryption completes.

## **Step 5.6 — Modify Confirm OTP response decryption**

Around lines 1125–1135, find:

```
var stringMessage = [];

if (isEncrypted(htmlContent)) {

    console.log('content is ' + htmlContent);

    decryptedResponse = getDecryptedResponse(htmlContent);

    console.log('decryptedResponse is ' + decryptedResponse);

    stringMessage = decryptedResponse.split("|");

} else {

    stringMessage = htmlContent.split("|");
}
```

Replace with:

```
var stringMessage = [];

try {

    stringMessage = getHLResponseMessage(htmlContent);

} catch (err) {

    removeDynamicLoader('confirmOtp');

    $("#confirmOtp").removeAttr('disabled');

    showPOPMsg(
        'errorOTPMsg',
        SORRY_FOR_INCONVENIENCE,
        false
    );

    return false;
}
```

Keep all existing success and error handling after this block.

## **Step 5.7 — Close the encryption Promise**

At the end of the Confirm OTP AJAX call, immediately before the existing form-validation `else` block, add:

```
        }).fail(function() {

            removeDynamicLoader('confirmOtp');

            $("#confirmOtp").removeAttr('disabled');

            showPOPMsg(
                'errorOTPMsg',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });
```

The resulting closing structure should look like:

```
            }); // Existing AJAX ends

        }).fail(function() {

            removeDynamicLoader('confirmOtp');

            $("#confirmOtp").removeAttr('disabled');

            showPOPMsg(
                'errorOTPMsg',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });

    } else {

        $("#confirmOtp").removeAttr('disabled');

        removeDynamicLoader('confirmOtp');
    }

    return false;
});
```

## **Step 5.8 — Remove old encryption/decryption methods**

Delete the complete existing implementations of the following three functions:

Around line 1169:

```
function getDecryptedResponse(htmlContent) {
    // Old static implementation
}
```

Around line 1176:

```
function isEncrypted(str) {
    // Old static implementation
}
```

Around line 1454:

```
function getEncryptedStr(str) {
    // Old static implementation
}
```

These methods are replaced by the common Home Loan crypto methods.

## **Step 5.9 — Modify dynamically generated OTP input fields**

Around line 1419, find:

```
+'<input id="inputOtp" name="inputOtp" class="form-control" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control">'
```

Replace with:

```
+'<input id="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);">'
+'<input id="inputOtp1" name="inputOtp" type="hidden">'
```

Around lines 1871–1872, find:

```
+'<input id="inputOtp" name="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control">'
+'<input id="inputOtp1" name="inputOtp" type="hidden">'
```

Replace with:

```
+'<input id="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);">'
+'<input id="inputOtp1" name="inputOtp" type="hidden">'
```

Ensure the OTP popup contains only one `inputOtp1` element at a time.

Also replace any visible OTP styling selectors such as:

```
jQuery("input[name='inputOtp']")
```

With:

```
jQuery("#inputOtp")
```

Otherwise, the styling would be applied to the hidden field.

## **Step 5.10 — Resend OTP — requestIndex 15**

Around line 1192, find:

```
jQuery("#resendOtp").unbind("click").bind("click",function(){
```

Your existing backend does not encrypt the normal resend response.

Therefore, **no mandatory encryption change is required for this handler**.

Keep:

```
stringMessage = htmlContent.split("|");
```

Do not reset the crypto configuration during resend unless you also fetch a fresh configuration and ensure all subsequent requests use it.

# **6. Alternate mobile OTP changes**

These changes are also inside `jquery.homeloanOTP.js`.

Your alternate mobile OTP verification currently encrypts the OTP using the same static `getEncryptedStr()` method.

## **Step 6.1 — Modify Send Alternate OTP**

Around line 1678, find:

```
jQuery("#sendOptAlt").unbind("click").bind("click",function(){
```

Around lines 1738–1740, you have duplicate form serialization:

```
var formdata = "params="+getRequest('otpApplicationAlt', 0);

formdata += "&requestIndex=14&requestType=2";

var paramData = getRequest('otpApplicationAlt', 0);
```

Replace these three statements with:

```
resetHLCryptoConfig();

getHLCryptoConfigOnce().then(function(json) {

    var paramData = getRequest('otpApplicationAlt', 0);
```

Keep the remaining formdata and AJAX logic.

After the existing AJAX closing statement, add:

```
}).fail(function() {

    removeDynamicLoader('sendOptAlt');

    showPOPMsg(
        'errorOTPMsg1',
        SORRY_FOR_INCONVENIENCE,
        false
    );
});
```

Keep the existing alternate OTP response parsing:

```
stringMessage = htmlContent.split("|");
```

The reason is that your `HomeLoanAction.java` currently excludes alternate mobile OTP responses from encryption.

## **Step 6.2 — Modify Confirm Alternate OTP**

Around lines 1539–1546, find:

```
var formdata = "params="+getRequest('otpApplication', 0);

formdata += "&requestIndex=16&requestType=2";

var inputOtpAlt =
    document.getElementById('inputOtpAlt').value;

var encryptedOtp = getEncryptedStr(inputOtpAlt);

document.getElementById('inputOtpAlt1').value =
    encryptedOtp;

var paramData = getRequest('otpApplicationAlt', 0);
```

Replace the complete block with:

```
var inputOtpAlt =
    document.getElementById('inputOtpAlt').value;

if (!inputOtpAlt) {

    removeDynamicLoader('confirmOtpAlt');

    showPOPMsg(
        'errorOTPMsg1',
        'Please enter OTP',
        false
    );

    return false;
}

getHLEncryptedString(inputOtpAlt).then(function(encryptedOtp) {

    document.getElementById('inputOtpAlt1').value =
        encryptedOtp;

    var paramData = getRequest('otpApplicationAlt', 0);
```

Keep the remaining form serialization and AJAX code inside the Promise.

After the existing AJAX closing statement, add:

```
}).fail(function() {

    removeDynamicLoader('confirmOtpAlt');

    $("#confirmOtpAlt").removeAttr('disabled');

    showPOPMsg(
        'errorOTPMsg1',
        SORRY_FOR_INCONVENIENCE,
        false
    );
});
```

## **Step 6.3 — Verify alternate OTP JSP fields**

The JSP containing `inputOtpAlt` and `inputOtpAlt1` was not included in the attachments.

Find the existing fields in your project and ensure they follow this structure, preserving any other required UI attributes:

```
<input type="text"
       id="inputOtpAlt"
       maxlength="6"
       class="form-control secure-otp"/>

<input type="hidden"
       id="inputOtpAlt1"
       name="inputOtpAlt"/>
```

Only the encrypted OTP should be submitted.

Replace visible-field styling selectors:

```
jQuery("input[name='inputOtpAlt']")
```

With:

```
jQuery("#inputOtpAlt")
```

# **7. Changes in jquery.homeloan_quote.js**

**File path:**

```
JS/ocas/sbiNew/js/app/loan/homeloan/js/
jquery.homeloan_quote.js
```

This file handles the Home Loan quote page and the Want Us To Call You OTP functionality.

## **Step 7.1 — Remove static values**

Around lines 1895–1899 of the attachment, remove:

```
var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";
```

## **Step 7.2 — Modify Send Callback OTP — requestIndex 3**

Around line 2242, find:

```
var paramData = getRequest('call_back', 0);
```

Immediately before this line, add:

```
resetHLCryptoConfig();

getHLCryptoConfigOnce().then(function(json) {
```

The resulting code will look like:

```
resetHLCryptoConfig();

getHLCryptoConfigOnce().then(function(json) {

    var paramData = getRequest('call_back', 0);

    if (!paramData) {

        showPOPMsg(
            'errorWantUsToCall',
            SORRY_FOR_INCONVENIENCE,
            false
        );

        return false;
    }

    showDynamicLoader('callBankSubmit');

    var formdata = "params=" + paramData;

    formdata += "&requestIndex=3&requestType=2";

    if (uiType != undefined && uiType != "") {
        formdata += "&uiType=" + uiType;
    }

    // Existing AJAX call.
```

## **Step 7.3 — Modify Callback OTP response**

Around lines 2270–2277, find:

```
var stringMessage = [];

if (isEncrypted(htmlContent)) {

    decryptedResponse = getDecryptedResponse(htmlContent);

    stringMessage = decryptedResponse.split("|");

} else {

    stringMessage = htmlContent.split("|");
}
```

Replace with:

```
var stringMessage = [];

try {

    stringMessage = getHLResponseMessage(htmlContent);

} catch (err) {

    removeDynamicLoader('callBankSubmit');

    showPOPMsg(
        'errorWantUsToCall',
        SORRY_FOR_INCONVENIENCE,
        false
    );

    return false;
}
```

Keep the existing logic for:

```
if (stringMessage[0] == "success") {
```

After the existing AJAX call closes, add:

```
}).fail(function() {

    removeDynamicLoader('callBankSubmit');

    showPOPMsg(
        'errorWantUsToCall',
        SORRY_FOR_INCONVENIENCE,
        false
    );
});
```

## **Step 7.4 — Modify Confirm Callback OTP — requestIndex 5**

Around line 2396, find:

```
jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
```

Keep the existing validation:

```
if (!validateWantUstoCallyou(this, 3)) {
```

Around lines 2405–2411, find:

```
var inputOtpWantUs =
    document.getElementById('inputOtpWantUs').value;

if (inputOtpWantUs != "" || inputOtpWantUs != '') {

    var encryptedOtp = getEncryptedStr(inputOtpWantUs);

    document.getElementById('inputOtpWantUs1').value =
        encryptedOtp;
}

var paramData = getRequest('call_back', 1);
```

Replace with:

```
var inputOtpWantUs =
    document.getElementById('inputOtpWantUs').value;

if (!inputOtpWantUs) {

    removeDynamicLoader('confirmOtpWantUs');

    showPOPMsg(
        'errorWantUsToCall',
        'Please enter OTP',
        false
    );

    return false;
}

getHLEncryptedString(inputOtpWantUs).then(function(encryptedOtp) {

    document.getElementById('inputOtpWantUs1').value =
        encryptedOtp;

    var paramData = getRequest('call_back', 1);
```

Keep the existing AJAX call and requestIndex 5 inside this Promise.

## **Step 7.5 — Modify Confirm Callback OTP response**

Around lines 2446–2452, find:

```
if (isEncrypted(htmlContent)) {

    decryptedResponse = getDecryptedResponse(htmlContent);

    stringMessage = decryptedResponse.split("|");

} else {

    stringMessage = htmlContent.split("|");
}
```

Replace with:

```
try {

    stringMessage = getHLResponseMessage(htmlContent);

} catch (err) {

    removeDynamicLoader('confirmOtpWantUs');

    showPOPMsg(
        'errorWantUsToCall',
        SORRY_FOR_INCONVENIENCE,
        false
    );

    return false;
}
```

Keep the existing Thank You message, popup closing and other success handling unchanged.

After the existing AJAX closing statement, add:

```
}).fail(function() {

    removeDynamicLoader('confirmOtpWantUs');

    showPOPMsg(
        'errorWantUsToCall',
        SORRY_FOR_INCONVENIENCE,
        false
    );
});
```

## **Step 7.6 — Remove old helper methods**

Remove the complete old implementations:

```
function getDecryptedResponse(htmlContent)
```

```
function isEncrypted(str)
```

```
function getEncryptedStr(str)
```

These are located around lines 2317, 2323 and 2534 of the attachment.

## **Step 7.7 — Callback resend OTP — requestIndex 4**

Around line 2484, find:

```
jQuery(document).on("click","#resendOtpWantUs", function(){
```

No mandatory encryption changes are required because the existing Java backend explicitly excludes state 4 from response encryption.

# **8. Changes in HomeLoanAction.java**

**File path:**

```
SBI/src/com/mintstreet/loan/homeloan/action/
HomeLoanAction.java
```

After checking your attached Java code, the good part is that response encryption is already implemented using:

```
SbiUtil.getEncryptedResponse(responseMessage);
```

You do not need to implement another AES encryption method inside `HomeLoanAction.java`.

## **Step 8.1 — Existing states 14, 15 and 16**

Around line 1044, find:

```
if ((stateManagerBean.getState() == 14
        || stateManagerBean.getState() == 15
        || stateManagerBean.getState() == 16)
        && SessionUtil.getHomeLoanState16() == "false") {
```

The existing response encryption for state 16 is:

```
if (otherRequest.getAlternateMobileNumber() == null) {

    responseMessage =
        SbiUtil.getEncryptedResponse(responseMessage);
}
```

Similarly, for state 14:

```
if (stateManagerBean.getState() == 14
        && otherRequest.getAlternateMobileNumber() == null) {

    responseMessage =
        SbiUtil.getEncryptedResponse(responseMessage);
}
```

Keep these conditions unchanged for the minimal migration.

Once the common `SbiUtil` method retrieves the dynamic values from the session, these responses will use dynamic encryption.

## **Step 8.2 — Validate the session crypto configuration**

Inside the state 14/15/16 block, find:

```
OtherRequest otherRequest =
    stateManagerBean.getOtherRequest();
```

Immediately after this line, add:

```
if (stateManagerBean.getState() == 14
        || stateManagerBean.getState() == 16) {

    if (SessionUtil.getCryptoSalt() == null
            || SessionUtil.getCryptoIv() == null
            || SessionUtil.getCryptoPassPhrase() == null) {

        SessionUtil.setHomeLoanState16("false");

        responseMessage =
            "error|Your session has expired. Please try again.";

        return "jsonResponsePage";
    }
}
```

Apply this guard to migrated callers only if there are other Home Loan/DSR callers that do not yet initialize the crypto configuration. Otherwise, those callers would stop working.

## **Step 8.3 — Existing states 3, 4 and 5**

Around line 1853, find:

```
if (stateManagerBean.getState() == 3
        || stateManagerBean.getState() == 4
        || stateManagerBean.getState() == 5) {
```

Inside this block, immediately after:

```
OtherRequest otherRequest =
    stateManagerBean.getOtherRequest();
```

Add:

```
if (stateManagerBean.getState() == 3
        || stateManagerBean.getState() == 5) {

    if (SessionUtil.getCryptoSalt() == null
            || SessionUtil.getCryptoIv() == null
            || SessionUtil.getCryptoPassPhrase() == null) {

        responseMessage =
            "error|Your session has expired. Please try again.";

        return "jsonResponsePage";
    }
}
```

Again, verify all callers are migrated before making this guard unconditional.

Keep your existing response encryption:

```
if (stateManagerBean.getState() != 4) {

    responseMessage =
        SbiUtil.getEncryptedResponse(responseMessage);
}
```

## **Step 8.4 — Do not generate new crypto values inside these states**

Do not add code such as:

```
new SecureRandom().nextBytes(salt);
```

Inside states 5 or 16.

The frontend has already encrypted the OTP using the values returned by `getCryptoConfig()`. Generating new values at this point would invalidate the encrypted OTP.

## **Step 8.5 — Do not modify unrelated encryption**

Your `HomeLoanAction.java` also contains:

```
aesEncryption.decrypt(
    otherRequest.getAppliedLoanId().toString()
);
```

```
aesEncryption.decrypt(
    request.getParameter("HLQuoteToken").toString()
);
```

```
aesEncryption.encrypt(
    appForm.getAppReferenceId()
);
```

These are related to loan quote tokens, application references and other application flows.

**Do not replace them with OTP session encryption.**

Some of these tokens may need to remain valid across sessions. Changing their encryption without reviewing their generation and consumption could break saved loan quotes and application tracking.

# **9. Changes in HomeProcessManagerImpl.java**

**File path:**

```
SBI/src/com/mintstreet/loan/homeloan/bo/impl/
HomeProcessManagerImpl.java
```

Your attached `HomeLoanAction.java` delegates OTP verification to:

```
processManagerHomeImpl.processMobileOTP(
    appSeqId,
    moduleId,
    stateManagerBean.getState(),
    (bankLmsUser != null
        ? bankLmsUser.getLmsUserId()
        : Constants.OTHER_USER_ID),
    ajaxPostUrl,
    otherRequest
);
```

And callback verification to:

```
processManagerHomeImpl.processWantUsToCallYou(
    appSeqId,
    moduleId,
    stateManagerBean.getState(),
    (bankLmsUser != null
        ? bankLmsUser.getLmsUserId()
        : Constants.OTHER_USER_ID),
    ajaxPostUrl,
    otherRequest
);
```

## **Step 9.1 — Check the existing decryption**

Inside `processMobileOTP()`, search for:

```
SbiUtil.getDecryptedRequest(
```

Also search inside `processWantUsToCallYou()`.

If the methods already use:

```
String inputOtp =
    SbiUtil.getDecryptedRequest(encryptedOtp);
```

Then **no additional decryption change is required**, provided `SbiUtil` is using the dynamic session values.

If static AES configuration is being used directly, replace that specific OTP decryption with the existing common method.

For example:

```
String encryptedOtp = otherRequest.getInputOtp();

String inputOtp =
    SbiUtil.getDecryptedRequest(encryptedOtp);
```

Then validate the decrypted value before proceeding with existing OTP verification:

```
if (inputOtp == null || !inputOtp.matches("[0-9]{6}")) {

    // Return existing invalid OTP error response.
}
```

Apply equivalent handling for alternate mobile and callback OTP using their actual request fields.

**Important:** `HomeProcessManagerImpl.java` was not included in your attachments. Therefore, I cannot confirm the exact decryption line or whether decryption already happens in this class, a validator or another utility. Do not decrypt the same OTP twice.

# **10. Changes in CommonLoanAction.java**

You already implemented:

```
public StreamResult getCryptoConfig()
```

In your working reference code.

The existing implementation generates:

```
byte[] saltBytes = new byte[16];

new SecureRandom().nextBytes(saltBytes);

byte[] ivBytes = new byte[16];

new SecureRandom().nextBytes(ivBytes);

String passPhrase =
    UUID.randomUUID().toString()
    + UUID.randomUUID().toString();
```

And stores them using:

```
SessionUtil.setCryptoSalt(salt);

SessionUtil.setCryptoIv(iv);

SessionUtil.setCryptoPassPhrase(passPhrase);
```

Reuse this method.

## **Step 10.1 — Small recommended correction**

Your working implementation first clears the session values and then sets the new values.

Remove these three unnecessary statements:

```
SessionUtil.setCryptoSalt(null);

SessionUtil.setCryptoIv(null);

SessionUtil.setCryptoPassPhrase(null);
```

The new values will overwrite the previous session attributes.

Keep:

```
SessionUtil.setCryptoSalt(salt);

SessionUtil.setCryptoIv(iv);

SessionUtil.setCryptoPassPhrase(passPhrase);
```

Also ensure the method does not return a successful configuration if storing the session values fails.

The crypto configuration response should have a `Cache-Control: no-store` header, since it contains the encryption passphrase.

# **11. Changes in SessionUtil.java**

Your previously shared working implementation contains potentially unsafe logging statements.

For example:

```
logger.info(
    "getCryptoIv Session: {}",
    getSession().get(CRYPTO_IV).toString()
);
```

If the value is null, this will throw `NullPointerException` before reaching your null check.

## **Step 11.1 — Replace the crypto session methods**

Replace the existing six crypto getter/setter methods with:

```
public static void setCryptoSalt(String cryptoSalt) {

    getSessionAction().setAttribute(
        CRYPTO_SALT,
        cryptoSalt
    );
}

public static String getCryptoSalt() {

    Object value =
        getSessionAction().getAttribute(CRYPTO_SALT);

    return value != null ? value.toString() : null;
}

public static void setCryptoIv(String cryptoIv) {

    getSessionAction().setAttribute(
        CRYPTO_IV,
        cryptoIv
    );
}

public static String getCryptoIv() {

    Object value =
        getSessionAction().getAttribute(CRYPTO_IV);

    return value != null ? value.toString() : null;
}

public static void setCryptoPassPhrase(String cryptoPassPhrase) {

    getSessionAction().setAttribute(
        CRYPTO_PASSPHRASE,
        cryptoPassPhrase
    );
}

public static String getCryptoPassPhrase() {

    Object value =
        getSessionAction().getAttribute(CRYPTO_PASSPHRASE);

    return value != null ? value.toString() : null;
}
```

These changes assume `getSessionAction()` returns the current valid HTTP session, as used by your existing setters.

Keep your existing constants unchanged.

Do not log salt, IV, passphrase, complete session contents or decrypted OTP values.

# **12. Changes in SbiUtil.java**

This is an important backend change because your working reference still contains a static fallback.

## **Step 12.1 — Existing encryption fallback**

Inside:

```
public String getEncryptedResponse(String response)
```

You currently have:

```
if (salt == null || iv == null || pass == null) {

    salt = EncryptDecryptUtil.decrypt(
        Constants.CRM_NEW_AES_KEY
    ).substring(18, 50);

    iv = salt;

    pass = EncryptDecryptUtil.decrypt(
        String.valueOf(passphrase)
    );
}
```

This means that even after implementing dynamic configuration, the application can still use static encryption values if the session configuration is unavailable.

### **Replace with:**

```
if (salt == null || iv == null || pass == null) {

    logger.error("Crypto configuration missing in session");

    return null;
}
```

Only remove this fallback globally after confirming all modules using `getEncryptedResponse()` have migrated. Otherwise, keep the legacy method for those callers and introduce a strict session-only variant for migrated OTP flows.

## **Step 12.2 — Existing decryption fallback**

Inside:

```
public String getDecryptedRequest(String request)
```

Find the same fallback:

```
if (salt == null || iv == null || pass == null) {

    salt = EncryptDecryptUtil.decrypt(
        Constants.CRM_NEW_AES_KEY
    ).substring(18, 50);

    iv = salt;

    pass = EncryptDecryptUtil.decrypt(
        String.valueOf(passphrase)
    );
}
```

Replace with:

```
if (salt == null || iv == null || pass == null) {

    logger.error("Crypto configuration missing in session");

    return null;
}
```

The same migration precaution applies here.

## **Step 12.3 — Remove sensitive logging**

Remove:

```
logger.info(
    "DECRYPTED OTP decryptedRequest1 = "
    + decryptedRequest1
);
```

Remove:

```
logger.info(
    "SESSION SALT = "
    + SessionUtil.getCryptoSalt()
);
```

Also remove the `finally` block that clears the shared legacy `passphrase` array from the session-only implementation:

```
finally {
    Arrays.fill(passphrase, ' ');
}
```

The dynamic implementation does not need that legacy passphrase. Clearing a shared field could also affect other requests.

## **Step 12.4 — Handle encryption failure**

Wherever `HomeLoanAction.java` uses:

```
responseMessage =
    SbiUtil.getEncryptedResponse(responseMessage);
```

Add a null check:

```
responseMessage =
    SbiUtil.getEncryptedResponse(responseMessage);

if (responseMessage == null) {

    responseMessage =
        "error|" + Constants.SORRY_FOR_INCONVENIENCE;

    return "jsonResponsePage";
}
```

This prevents the frontend from receiving an empty response when encryption fails.

# **13. Changes in Struts XML and web.xml**

## **struts-common-loan.xml**

Your previously shared working code already contains:

```
<action name="getCryptoConfig"
        method="getCryptoConfig"
        class="commonLoanAction">

    <result name="success" type="json">
        <param name="root">json</param>
    </result>

</action>
```

Since this endpoint is already working in your existing module, reuse the deployed mapping.

No duplicate action is required.

## **web.xml**

No additional changes are expected.

## **AesUtil.java**

No changes are required to maintain compatibility with your existing AES implementation.

## **Maven dependencies**

No new JAR files or Maven dependencies are required for this migration.

# **14. Expected behaviour after implementation**

### **Example OTP execution**

Illustrative values, not actual encrypted output

STEP 1 — getCryptoConfig

```
{
  "salt": "<random salt>",
  "iv": "<random IV>",
  "passPhrase": "<random passphrase>",
  "keySize": "256",
  "iterationCount": "100000"
}
```

STEP 2 — USER ENTERS OTP

# **1 2 3 4 5 6**

STEP 3 — ENCRYPTED OTP SUBMITTED

```
inputOtp=<AES ciphertext>
requestIndex=16
requestType=2
```

**Java backend**

Retrieve matching session values, decrypt OTP and execute existing verification logic.

STEP 4 — ENCRYPTED RESPONSE

```
<AES encrypted response>
```

**OTP verified successfully**

# **15. Testing checklist**

### **Home Loan regression testing**

0 / 18 completed

Primary mobile Send OTP — state 14

Correct OTP verification on first attempt — state 16

Incorrect OTP followed by correct OTP

Resend OTP — state 15

Alternate mobile Send OTP

Alternate mobile OTP verification

Callback Send OTP — state 3

Callback OTP verification — state 5

Callback resend OTP — state 4

NRI email OTP existing behaviour

Invalid CAPTCHA response

Expired session handling

Same OTP encrypted in two fresh OTP flows produces different ciphertext

Plaintext mobile OTP absent from submitted form parameters

No static crypto fallback in migrated OTP flows

Multiple popups and browser tabs do not cause session key mismatch

Existing DSR and ETB flows continue working

Saved loan quote and application reference functionality unaffected

Reset checklist

# **16. Important points regarding ISD security**

The proposed changes remove hardcoded frontend salt, IV and passphrase values while retaining your existing encryption architecture.

However, there are some limitations in the existing design that should be considered before production deployment.

| Security requirement                                  | Status with proposed changes                |
| ----------------------------------------------------- | ------------------------------------------- |
| Remove static frontend passphrase                     | Implemented                                 |
| Dynamic salt and IV                                   | Implemented                                 |
| Store configuration in session                        | Implemented                                 |
| Avoid plaintext mobile OTP submission                 | Implemented with field changes              |
| Wait for encryption before AJAX                       | Implemented                                 |
| Different ciphertext across fresh configurations      | Implemented                                 |
| Unique ciphertext on every retry                      | Not guaranteed                              |
| Multiple concurrent OTP flows                         | Requires further session isolation          |
| Prevent browser owner from inspecting encryption keys | Not provided                                |
| Cryptographic message authentication                  | Not provided by this compatibility approach |

Your existing `getCryptoConfig()` sends the passphrase to the browser. Therefore, dynamic encryption does not prevent someone controlling that browser from inspecting the values or decrypting its traffic.

Also, reusing the same IV and key for multiple AES-CBC messages is not recommended. For a stronger implementation, use authenticated encryption such as AES-GCM with a unique nonce per message and flow-specific configuration.

I have retained your current architecture to meet your requirement for minimal changes, rather than introducing a completely different encryption mechanism.

# **17. Final summary of changes**

| File                          | Final action                                       |
| ----------------------------- | -------------------------------------------------- |
| `jquery.homeloan.crypto.js`   | Add reusable dynamic crypto methods                |
| `HomeSecondPage.jsp`          | Include common JS and remove duplicate OTP names   |
| `jquery.homeloanOTP.js`       | Modify primary and alternate OTP flows             |
| `jquery.homeloan_quote.js`    | Modify callback OTP flow                           |
| `HomeLoanAction.java`         | Session validation and encryption failure handling |
| `HomeProcessManagerImpl.java` | Verify existing OTP decryption                     |
| `CommonLoanAction.java`       | Reuse dynamic configuration API                    |
| `SessionUtil.java`            | Null-safe getters and setters                      |
| `SbiUtil.java`                | Prevent static fallback for migrated flows         |
| `struts-common-loan.xml`      | Reuse existing mapping                             |
| `web.xml`                     | No changes expected                                |

**Remaining code required for complete backend verification:**

- `HomeProcessManagerImpl.java`, particularly `processMobileOTP()` and `processWantUsToCallYou()`.
- JSP containing the primary and alternate mobile OTP popup fields.
- `getRequest()` implementation and the relevant OTP validation configuration, to verify that only encrypted OTP values are serialized and accepted.

I have reviewed both new attachments against your previous working implementation, but have not compiled or executed these changes in your application. The existing email OTP behaviour has intentionally been preserved because its backend decryption contract has not been established.
