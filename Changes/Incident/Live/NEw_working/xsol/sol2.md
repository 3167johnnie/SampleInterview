Below are the changes in an easy Find → Replace → Add format, with the exact file names, Java class names and method names.

To keep changes minimal, we will reuse your existing working getCryptoConfig() API, SessionUtil and SbiUtil. There is no need to introduce another Java class, encryption algorithm or Maven dependency.

1. Files to modify

File / Java class	Changes
jquery.homeloanOTP.js	Dynamic primary and alternate OTP encryption
jquery.homeloan_quote.js	Dynamic callback OTP encryption
HomeSecondPage.jsp	Remove duplicate OTP parameter
CommonLoanAction.java	Reuse existing getCryptoConfig()
SessionUtil.java	Null-safe crypto session getters
SbiUtil.java	Dynamic encryption/decryption
HomeLoanAction.java	Reuse existing response encryption
HomeProcessManagerImpl.java	Verify OTP decryption implementation

I recommend retaining the existing JavaScript helper method names, such as getEncryptedStr(), isEncrypted() and getDecryptedResponse(). This reduces the changes required in your existing AJAX success blocks.

2. jquery.homeloanOTP.js

File path:

JS/ocas/sbiNew/js/app/loan/homeloan/js/jquery.homeloanOTP.js

Change 2.1 — Replace static encryption values

Find around line 999:

var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

Replace with:

var _hlCryptoConfigCache = null;
var _hlCryptoConfigRequest = null;
function resetCryptoConfig() {
    _hlCryptoConfigCache = null;
}
function getCryptoConfigOnce() {
    if (_hlCryptoConfigCache != null) {
        return jQuery.when(_hlCryptoConfigCache);
    }
    if (_hlCryptoConfigRequest != null) {
        return _hlCryptoConfigRequest;
    }
    _hlCryptoConfigRequest = jQuery.ajax({
        type: "POST",
        url: ctxPath + "/getCryptoConfig",
        dataType: "json",
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

This is similar to the configuration method in your previously working code.

The configuration will now be fetched from Java instead of using hardcoded values.

ctxPath must be available on this page, as it is in your working reference module.

Change 2.2 — Replace getEncryptedStr()

Find around line 1454:

function getEncryptedStr(str) {
    try {
        var aesUtil = new AesUtil(keySize, iterationCount);
        var enc1 = aesUtil.encrypt(
            salt, iv, passPhrase, str
        );
        return enc1;
    } catch (err) {
        return false;
    }
}

Replace the complete method with:

function getEncryptedStr(str) {
    return getCryptoConfigOnce().then(function(json) {
        var aesUtil = new AesUtil(
            json.keySize,
            json.iterationCount
        );
        var enc1 = aesUtil.encrypt(
            json.salt,
            json.iv,
            json.passPhrase,
            str
        );
        if (!enc1) {
            throw new Error("Encryption failed");
        }
        return enc1;
    });
}

The method name remains unchanged.

The only difference is that it now obtains dynamic values and returns a Promise.

Change 2.3 — Replace getDecryptedResponse()

Find around line 1169:

function getDecryptedResponse(htmlContent) {
    var aesUtil = new AesUtil(keySize, iterationCount);
    var dec1 = aesUtil.decrypt(
        salt, iv, passPhrase, htmlContent
    );
    return dec1;
}

Replace with:

function getDecryptedResponse(htmlContent) {
    var json = _hlCryptoConfigCache;
    if (!json) {
        throw new Error("Crypto configuration unavailable");
    }
    var aesUtil = new AesUtil(
        json.keySize,
        json.iterationCount
    );
    var dec1 = aesUtil.decrypt(
        json.salt,
        json.iv,
        json.passPhrase,
        htmlContent
    );
    return dec1;
}

We are using the cached configuration here instead of calling the API again.

This ensures the response is decrypted using the same configuration that Java used to encrypt it.

Change 2.4 — Replace isEncrypted()

Find around line 1176:

function isEncrypted(str) {
    if (str === '' || str.trim() === '') {
        return false;
    }
    try {
        var aesUtil = new AesUtil(keySize, iterationCount);
        var dec1 = aesUtil.decrypt(
            salt, iv, passPhrase, str
        );
        var enc1 = aesUtil.encrypt(
            salt, iv, passPhrase, dec1
        );
        return enc1 == str;
    } catch (err) {
        return false;
    }
}

Replace with:

function isEncrypted(str) {
    if (!str || str.trim() === '') {
        return false;
    }
    try {
        var json = _hlCryptoConfigCache;
        if (!json) {
            return false;
        }
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

This method remains synchronous, so you do not need to convert every existing AJAX response handler into a Promise.

Note that this method is retained for compatibility, not as a cryptographic integrity check.

Change 2.5 — Add common response parsing method

Immediately after isEncrypted(), add:

function getCryptoResponseMessage(htmlContent) {
    if (typeof htmlContent !== "string" ||
        !htmlContent.trim()) {
        return [
            "error",
            SORRY_FOR_INCONVENIENCE
        ];
    }
    try {
        if (isEncrypted(htmlContent)) {
            htmlContent =
                getDecryptedResponse(htmlContent);
        }
        var stringMessage = htmlContent.split("|");
        if (stringMessage[0] === "success" ||
            stringMessage[0] === "error") {
            return stringMessage;
        }
    } catch (err) {
        // Do not log decrypted OTP or crypto values.
    }
    return [
        "error",
        SORRY_FOR_INCONVENIENCE
    ];
}

This supports both encrypted responses and the existing plaintext responses returned by Java.

Change 2.6 — Send OTP (requestIndex 14)

Search for:

jQuery("#sendOpt").unbind("click").bind("click",function(){

Inside this function, around line 1320:

Find:

var paramData = getRequest('otpApplication', 0);

Replace with:

resetCryptoConfig();
getCryptoConfigOnce().then(function(json) {
    var paramData = getRequest('otpApplication', 0);

Keep the remaining existing code unchanged, including:

var formdata = "params=" + paramData;
formdata += "&requestIndex=14&requestType=2";

Change the AJAX response block

Inside the same function:

Find:

var stringMessage = [];
if (isEncrypted(htmlContent)) {
    console.log('content is ' + htmlContent);
    decryptedResponse = getDecryptedResponse(htmlContent);
    console.log('decryptedResponse is ' + decryptedResponse);
    stringMessage = decryptedResponse.split("|");
} else {
    stringMessage = htmlContent.split("|");
}

Replace with:

var stringMessage =
    getCryptoResponseMessage(htmlContent);

Keep the existing success and error conditions unchanged.

Close the newly added Promise

At the end of the sendOpt AJAX call:

Find:

            });
        }
        return false;
    });

Replace with:

            });
        }).fail(function() {
            removeDynamicLoader('sendOpt');
            $("#opt-loader-application").hide();
            showPOPMsg(
                'errorOTPMsg',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });
        }
        return false;
    });

Now the configuration will be generated before sending requestIndex 14.

Change 2.7 — Confirm OTP (requestIndex 16)

Search for:

jQuery("#confirmOtp").unbind("click").bind("click",function(){

Around lines 1079–1102:

Find the complete block:

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
var paramData = getRequest('otpApplication', 0);

Remove any commented-out form serialization lines between these statements as well.

Replace with:

var otpEncryptionPromise;
if (isEmailOtp == false) {
    var inputOtp =
        document.getElementById('inputOtp').value;
    if (!inputOtp) {
        removeDynamicLoader('confirmOtp');
        showPOPMsg(
            'errorOTPMsg',
            'Please enter OTP',
            false
        );
        return false;
    }
    otpEncryptionPromise = getEncryptedStr(inputOtp);
} else {
    otpEncryptionPromise =
        getCryptoConfigOnce().then(function() {
            return null;
        });
}
otpEncryptionPromise.then(function(encryptedOtp) {
    if (isEmailOtp == false) {
        document.getElementById('inputOtp1').value =
            encryptedOtp;
    }
    var paramData = getRequest('otpApplication', 0);

Keep the existing code after paramData, including the AJAX call.

Replace response decryption

Find:

var stringMessage = [];
if (isEncrypted(htmlContent)) {
    console.log('content is ' + htmlContent);
    decryptedResponse = getDecryptedResponse(htmlContent);
    console.log('decryptedResponse is ' + decryptedResponse);
    stringMessage = decryptedResponse.split("|");
} else {
    stringMessage = htmlContent.split("|");
}

Replace with:

var stringMessage =
    getCryptoResponseMessage(htmlContent);

Close the Promise

At the end of the confirm OTP AJAX call:

Find:

            });
        }else{
            $("#confirmOtp").removeAttr('disabled');
            removeDynamicLoader('confirmOtp');
        }
        return false;
    });

Replace with:

            });
        }).fail(function() {
            $("#confirmOtp").removeAttr('disabled');
            removeDynamicLoader('confirmOtp');
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

Important: Do not call resetCryptoConfig() during verification in this compatibility implementation. The configuration obtained during Send OTP is reused.

Change 2.8 — Send Alternate Mobile OTP

Search for:

jQuery("#sendOptAlt").unbind("click").bind("click",function(){

Around line 1738:

Find:

var formdata = "params="+getRequest('otpApplicationAlt', 0);
formdata +="&requestIndex=14&requestType=2";
var paramData= getRequest('otpApplicationAlt', 0);

Replace with:

resetCryptoConfig();
getCryptoConfigOnce().then(function(json) {
    var paramData = getRequest('otpApplicationAlt', 0);

Keep the remaining existing AJAX code unchanged.

At the end of the AJAX call:

Find:

            });
        }
        return false;
    });

Replace with:

            });
        }).fail(function() {
            removeDynamicLoader('sendOptAlt');
            showPOPMsg(
                'errorOTPMsg1',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });
        }
        return false;
    });

The alternate mobile OTP response is currently plaintext in your Java implementation, so no mandatory response decryption change is required.

Change 2.9 — Confirm Alternate Mobile OTP

Search for:

jQuery("#confirmOtpAlt")

Inside its click handler, around lines 1539–1546:

Find:

var formdata = "params="+getRequest('otpApplication', 0);
formdata +="&requestIndex=16&requestType=2";
var inputOtpAlt =
    document.getElementById('inputOtpAlt').value;
var encryptedOtp = getEncryptedStr(inputOtpAlt);
document.getElementById('inputOtpAlt1').value =
    encryptedOtp;
var paramData= getRequest('otpApplicationAlt', 0);

Replace with:

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
getEncryptedStr(inputOtpAlt).then(function(encryptedOtp) {
    document.getElementById('inputOtpAlt1').value =
        encryptedOtp;
    var paramData = getRequest('otpApplicationAlt', 0);

Keep the existing AJAX call inside this Promise.

Immediately after the closing statement of the AJAX call:

});

Add:

}).fail(function() {
    removeDynamicLoader('confirmOtpAlt');
    $("#confirmOtpAlt").removeAttr('disabled');
    showPOPMsg(
        'errorOTPMsg1',
        SORRY_FOR_INCONVENIENCE,
        false
    );
});

Do not change the existing success and error messages.

Change 2.10 — Resend OTP

The following handlers do not require mandatory crypto changes:

jQuery("#resendOtp")
jQuery("#resendOtpAlt")

Both use requestIndex 15.

Keep their existing response parsing:

stringMessage = htmlContent.split("|");

Your current backend does not encrypt these resend responses.

3. jquery.homeloan_quote.js

File path:

JS/ocas/sbiNew/js/app/loan/homeloan/js/jquery.homeloan_quote.js

Change 3.1 — Remove static values

Find around line 1895:

var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

Delete these five lines.

If jquery.homeloanOTP.js is already loaded on this page, reuse the methods defined in that file.

Otherwise, move the following methods from Section 2 into one common JavaScript file and include it on both pages:

resetCryptoConfig()
getCryptoConfigOnce()
getEncryptedStr()
getDecryptedResponse()
isEncrypted()
getCryptoResponseMessage()

Also move the two cache variables into that common file.

Do not maintain two independent caches on the same page.

Change 3.2 — Remove duplicate methods

Remove the complete existing implementations of:

function getDecryptedResponse(htmlContent)

Around line 2317.

function isEncrypted(str)

Around line 2323.

function getEncryptedStr(str)

Around line 2534.

These methods will now use the common dynamic implementation.

Change 3.3 — Send Callback OTP (requestIndex 3)

Around line 2242:

Find:

var paramData= getRequest('call_back', 0);

Replace with:

resetCryptoConfig();
getCryptoConfigOnce().then(function(json) {
    var paramData = getRequest('call_back', 0);

Keep the remaining code unchanged:

var formdata = "params=" + paramData;
formdata += "&requestIndex=3&requestType=2";

Replace response parsing

Find:

var stringMessage = [];
if (isEncrypted(htmlContent)) {
    decryptedResponse = getDecryptedResponse(htmlContent);
    stringMessage = decryptedResponse.split("|");
} else {
    stringMessage = htmlContent.split("|");
}

Replace with:

var stringMessage =
    getCryptoResponseMessage(htmlContent);

Close the Promise

At the end of the AJAX call:

Find:

            });
            return false;
        }
        return false;
    });

Replace with:

            });
        }).fail(function() {
            removeDynamicLoader('callBankSubmit');
            showPOPMsg(
                'errorWantUsToCall',
                SORRY_FOR_INCONVENIENCE,
                false
            );
        });
            return false;
        }
        return false;
    });

Change 3.4 — Confirm Callback OTP (requestIndex 5)

Search for:

jQuery("#confirmOtpWantUs")

Around lines 2405–2411:

Find:

var inputOtpWantUs =
    document.getElementById('inputOtpWantUs').value;
if (inputOtpWantUs != "" || inputOtpWantUs != '') {
    var encryptedOtp = getEncryptedStr(inputOtpWantUs);
    document.getElementById('inputOtpWantUs1').value =
        encryptedOtp;
}
var paramData= getRequest('call_back', 1);

Replace with:

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
getEncryptedStr(inputOtpWantUs).then(function(encryptedOtp) {
    document.getElementById('inputOtpWantUs1').value =
        encryptedOtp;
    var paramData = getRequest('call_back', 1);

Keep the existing code:

var formdata = "params=" + paramData;
formdata += "&requestIndex=5&requestType=2";

Replace response parsing

Find:

if (isEncrypted(htmlContent)) {
    decryptedResponse = getDecryptedResponse(htmlContent);
    stringMessage = decryptedResponse.split("|");
} else {
    stringMessage = htmlContent.split("|");
}

Replace with:

stringMessage =
    getCryptoResponseMessage(htmlContent);

Close the Promise

At the end of the confirm OTP AJAX call:

Find:

        });
        return false;
    });

Replace with:

        });
    }).fail(function() {
        removeDynamicLoader('confirmOtpWantUs');
        showPOPMsg(
            'errorWantUsToCall',
            SORRY_FOR_INCONVENIENCE,
            false
        );
    });
        return false;
    });

Change 3.5 — Resend Callback OTP

No changes required in:

jQuery(document).on("click","#resendOtpWantUs", function(){

The existing requestIndex 4 response is plaintext.

4. HomeSecondPage.jsp

File path:

SBI/WebContent/appNew/loan/homeloan/HomeSecondPage.jsp

Change 4.1 — Replace duplicate OTP fields

Around lines 907–908:

Find:

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

Replace with:

<input type="text"
       id="inputOtpWantUs"
       class="form-control secure-otp"
       maxlength="6"
       onkeydown="return M.digit(event);"/>
<input type="hidden"
       id="inputOtpWantUs1"
       name="inputOtpWantUs"/>

This ensures the visible and hidden OTP fields do not submit the same parameter twice.

Before

Visible input

123456

name = inputOtpWantUs

Hidden input

AES ciphertext

name = inputOtpWantUs

Duplicate parameters

After

Visible input

123456

No submitted name

Hidden input

AES ciphertext

name = inputOtpWantUs

Only encrypted parameter

Change 4.2 — Primary mobile OTP fields

In jquery.homeloanOTP.js, around lines 1871–1872:

Find:

+'<input id="inputOtp" name="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control">'
+'<input id="inputOtp1" name="inputOtp" type="hidden">'

Replace with:

+'<input id="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);">'
+'<input id="inputOtp1" name="inputOtp" type="hidden">'

Apply the same pattern to the primary mobile OTP field generated around line 1419.

Ensure each popup contains only one visible and one hidden OTP field.

Change 4.3 — Alternate mobile OTP fields

In the JSP containing inputOtpAlt, ensure the fields are:

<input type="text"
       id="inputOtpAlt"
       class="form-control secure-otp"
       maxlength="6"
       onkeydown="return M.digit(event);"/>
<input type="hidden"
       id="inputOtpAlt1"
       name="inputOtpAlt"/>

Wherever the visible field is accessed using:

jQuery("input[name='inputOtpAlt']")

Replace with:

jQuery("#inputOtpAlt")

Similarly, for primary mobile OTP:

jQuery("input[name='inputOtp']")

Replace visible-field selectors with:

jQuery("#inputOtp")

Validation precaution: Your application uses addValidationsRules() and getRequest(). Verify that removing the visible input’s name does not prevent the existing validation rules from validating the entered OTP. Their implementations were not supplied.

5. CommonLoanAction.java

Java class:

com.mintstreet.common.action.CommonLoanAction

Method:

public StreamResult getCryptoConfig()

You already have this method in your previously working implementation.

For easier replacement and to remove unnecessary session clearing, use the following complete method.

Change 5.1 — Required imports

Verify these imports are available:

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;
import org.apache.struts2.result.StreamResult;
import org.json.JSONObject;
import com.mintstreet.common.session.SessionUtil;

Do not add duplicate imports.

Change 5.2 — Replace complete getCryptoConfig() method

public StreamResult getCryptoConfig() {
    logger.info("Inside getCryptoConfig()");
    JSONObject json = new JSONObject();
    try {
        SecureRandom secureRandom = new SecureRandom();
        byte[] saltBytes = new byte[16];
        secureRandom.nextBytes(saltBytes);
        byte[] ivBytes = new byte[16];
        secureRandom.nextBytes(ivBytes);
        String passPhrase =
                UUID.randomUUID().toString()
                + UUID.randomUUID().toString();
        String salt = Hex.encodeHexString(saltBytes);
        String iv = Hex.encodeHexString(ivBytes);
        SessionUtil.setCryptoSalt(salt);
        SessionUtil.setCryptoIv(iv);
        SessionUtil.setCryptoPassPhrase(passPhrase);
        json.put("salt", salt);
        json.put("iv", iv);
        json.put("passPhrase", passPhrase);
        json.put("keySize", "256");
        json.put("iterationCount", "100000");
        logger.info("Crypto configuration generated");
    } catch (Exception e) {
        logger.error("getCryptoConfig() error", e);
        json = new JSONObject();
    }
    return new StreamResult(
            new ByteArrayInputStream(
                    json.toString().getBytes(
                            StandardCharsets.UTF_8
                    )
            )
    );
}

What has changed?

The following unnecessary statements have been removed:

SessionUtil.setCryptoSalt(null);
SessionUtil.setCryptoIv(null);
SessionUtil.setCryptoPassPhrase(null);

The new session values automatically overwrite the previous values.

Also, the method no longer returns a valid-looking configuration if storing it in the session throws an exception.

Configure the existing endpoint to return Cache-Control: no-store.

6. SessionUtil.java

Java class:

com.mintstreet.common.session.SessionUtil

File path:

SBI/src/com/mintstreet/common/session/SessionUtil.java

Change 6.1 — Replace all six crypto methods

Find your existing six methods:

setCryptoSalt()
getCryptoSalt()
setCryptoIv()
getCryptoIv()
setCryptoPassPhrase()
getCryptoPassPhrase()

Replace all six methods with the following complete block:

public static void setCryptoSalt(String cryptoSalt) {
    getSessionAction().setAttribute(
            CRYPTO_SALT,
            cryptoSalt
    );
}
public static String getCryptoSalt() {
    Object value = getSession().get(CRYPTO_SALT);
    if (value == null) {
        value = getSessionAction().getAttribute(
                CRYPTO_SALT
        );
    }
    return value != null ? value.toString() : null;
}
public static void setCryptoIv(String cryptoIv) {
    getSessionAction().setAttribute(
            CRYPTO_IV,
            cryptoIv
    );
}
public static String getCryptoIv() {
    Object value = getSession().get(CRYPTO_IV);
    if (value == null) {
        value = getSessionAction().getAttribute(
                CRYPTO_IV
        );
    }
    return value != null ? value.toString() : null;
}
public static void setCryptoPassPhrase(
        String cryptoPassPhrase) {
    getSessionAction().setAttribute(
            CRYPTO_PASSPHRASE,
            cryptoPassPhrase
    );
}
public static String getCryptoPassPhrase() {
    Object value = getSession().get(
            CRYPTO_PASSPHRASE
    );
    if (value == null) {
        value = getSessionAction().getAttribute(
                CRYPTO_PASSPHRASE
        );
    }
    return value != null ? value.toString() : null;
}

Keep your existing constants:

CRYPTO_SALT
CRYPTO_IV
CRYPTO_PASSPHRASE

No new constants are required.

This preserves your existing session lookup approach while removing unsafe .toString() calls on potentially null attributes.

It also removes logging of sensitive crypto configuration and complete session contents.

7. SbiUtil.java

Java class:

com.mintstreet.common.util.SbiUtil

File path:

SBI/src/com/mintstreet/common/util/SbiUtil.java

This is the main backend class responsible for OTP encryption and decryption.

Change 7.1 — Replace getEncryptedResponse()

Find the complete method:

public String getEncryptedResponse(String response)
        throws UnsupportedEncodingException

Replace with:

public String getEncryptedResponse(String response)
        throws UnsupportedEncodingException {
    String encryptedResponse = null;
    try {
        String salt = SessionUtil.getCryptoSalt();
        String iv = SessionUtil.getCryptoIv();
        String pass = SessionUtil.getCryptoPassPhrase();
        if (salt == null || iv == null || pass == null) {
            logger.error(
                    "Crypto configuration missing in session"
            );
            return null;
        }
        String plainResponse = response + "|"
                + RandomStringUtils
                        .randomAlphanumeric(6)
                        .toUpperCase();
        AesUtil util = new AesUtil(256, 100000);
        encryptedResponse = util.encrypt(
                salt,
                iv,
                pass,
                plainResponse
        );
    } catch (Exception e) {
        logger.error(
                "SbiUtil getEncryptedResponse() error",
                e
        );
    }
    return encryptedResponse;
}

Main change

The following static fallback has been removed:

if (salt == null || iv == null || pass == null) {
    salt = EncryptDecryptUtil.decrypt(
            Constants.CRM_NEW_AES_KEY
    ).substring(18, 50);
    iv = salt;
    pass = EncryptDecryptUtil.decrypt(
            String.valueOf(passphrase)
    );
}

Now the method uses only the dynamic session configuration.

Change 7.2 — Replace getDecryptedRequest()

Find the complete method:

public String getDecryptedRequest(String request)

Replace with:

public String getDecryptedRequest(String request) {
    String decryptedRequest1 = null;
    try {
        String salt = SessionUtil.getCryptoSalt();
        String iv = SessionUtil.getCryptoIv();
        String pass = SessionUtil.getCryptoPassPhrase();
        if (salt == null || iv == null || pass == null) {
            logger.error(
                    "Crypto configuration missing in session"
            );
            return null;
        }
        if (request == null || request.trim().isEmpty()) {
            return null;
        }
        AesUtil util = new AesUtil(256, 100000);
        decryptedRequest1 = util.decrypt(
                salt,
                iv,
                pass,
                request
        );
    } catch (Exception e) {
        logger.error(
                "SbiUtil getDecryptedRequest() error",
                e
        );
    }
    return decryptedRequest1;
}

The static fallback, decrypted OTP logging and legacy Arrays.fill(passphrase, ' ') operation have been removed from this implementation.

Important for deployment: SbiUtil is a common class. Replace these two methods globally only after confirming that every module calling them has migrated to dynamic encryption. Otherwise, add the above implementations as getDynamicEncryptedResponse() and getDynamicDecryptedRequest() and use those methods only in migrated modules.

This prevents existing Auto Loan, Personal Loan, Education Loan or other legacy flows from breaking.

8. HomeLoanAction.java

Java class:

com.mintstreet.loan.homeloan.action.HomeLoanAction

File path:

SBI/src/com/mintstreet/loan/homeloan/action/HomeLoanAction.java

Method:

public String getHomeLoan(Integer moduleId)

Your existing class already uses SbiUtil.getEncryptedResponse().

Therefore, only minimal changes are required.

Change 8.1 — Validate crypto configuration for mobile OTP

Find the block handling:

stateManagerBean.getState() == 14
stateManagerBean.getState() == 15
stateManagerBean.getState() == 16

Inside that block, find:

OtherRequest otherRequest =
        stateManagerBean.getOtherRequest();

Immediately after it, add:

if (stateManagerBean.getState() == 14
        || stateManagerBean.getState() == 16) {
    if (SessionUtil.getCryptoSalt() == null
            || SessionUtil.getCryptoIv() == null
            || SessionUtil.getCryptoPassPhrase() == null) {
        SessionUtil.setHomeLoanState16("false");
        responseMessage =
                "error|Your session has expired. "
                + "Please try again.";
        return "jsonResponsePage";
    }
}

This prevents migrated OTP requests from proceeding without dynamic configuration.

Change 8.2 — Validate crypto configuration for callback OTP

Find around line 1853:

if (stateManagerBean.getState() == 3
        || stateManagerBean.getState() == 4
        || stateManagerBean.getState() == 5) {

Inside this block, find:

OtherRequest otherRequest =
        stateManagerBean.getOtherRequest();

Immediately after it, add:

if (stateManagerBean.getState() == 3
        || stateManagerBean.getState() == 5) {
    if (SessionUtil.getCryptoSalt() == null
            || SessionUtil.getCryptoIv() == null
            || SessionUtil.getCryptoPassPhrase() == null) {
        responseMessage =
                "error|Your session has expired. "
                + "Please try again.";
        return "jsonResponsePage";
    }
}

Apply these guards only after confirming all callers of the corresponding Home Loan states, including DSR callers, initialize the crypto configuration.

Change 8.3 — Handle response encryption failure

Your attached HomeLoanAction.java contains four calls to:

responseMessage =
        SbiUtil.getEncryptedResponse(responseMessage);

They are located around attachment lines:

* 1094
* 1106
* 1122
* 1888

Replace each occurrence with:

responseMessage =
        SbiUtil.getEncryptedResponse(responseMessage);
if (responseMessage == null) {
    responseMessage =
            "error|" + Constants.SORRY_FOR_INCONVENIENCE;
    return "jsonResponsePage";
}

If you created the separate dynamic method in SbiUtil.java, use:

responseMessage =
        SbiUtil.getDynamicEncryptedResponse(responseMessage);

Instead.

Remove the surrounding log statements that print complete plaintext or encrypted response packets.

Change 8.4 — No changes to existing business logic

Keep the following existing calls unchanged:

processManagerHomeImpl.processMobileOTP(...)
processManagerHomeImpl.processWantUsToCallYou(...)

Also, do not modify unrelated encryption calls such as:

aesEncryption.decrypt(...)
aesEncryption.encrypt(...)

Those calls are used for other loan-related tokens and application references.

9. HomeProcessManagerImpl.java

Java class:

com.mintstreet.loan.homeloan.bo.impl.HomeProcessManagerImpl

File path:

SBI/src/com/mintstreet/loan/homeloan/bo/impl/HomeProcessManagerImpl.java

Change 9.1 — Check processMobileOTP()

Search for the method:

processMobileOTP(

Inside it, search for:

SbiUtil.getDecryptedRequest(

If it already uses this common method to decrypt the mobile OTP, no additional changes are required when the common method has been migrated.

If you created a separate dynamic method, replace the applicable OTP decryption call:

Existing:

SbiUtil.getDecryptedRequest(encryptedOtp);

New:

SbiUtil.getDynamicDecryptedRequest(encryptedOtp);

Change 9.2 — Check processWantUsToCallYou()

Search for:

processWantUsToCallYou(

Similarly, check whether callback OTP decryption uses:

SbiUtil.getDecryptedRequest(

If yes, reuse it.

After decryption, the existing verification logic must reject a null or invalid decrypted OTP.

For example:

if (decryptedOtp == null
        || !decryptedOtp.matches("[0-9]{6}")) {
    // Use existing invalid OTP error handling.
}

Do not decrypt the OTP twice.

This Java class was not included in the attachments, so the exact existing decryption statement and variable names still need verification.

10. struts-common-loan.xml

File path:

SBI/src/struts-common-loan.xml

No changes required if your existing getCryptoConfig action is already working.

Reuse the same existing action mapping and stream response configuration.

Do not create a duplicate action.

11. web.xml

No changes required.

12. AesUtil.java

No changes required for this migration.

Keep the existing AES algorithm, key derivation and encryption/decryption implementation.

13. pom.xml

No changes required.

All the proposed Java code is compatible with Java 8 and uses the libraries already present in your working implementation.

14. Complete flow after changes

15. Implementation checklist

Code changes tracker

0 / 15

CommonLoanAction.java — reuse or replace getCryptoConfig()

SessionUtil.java — replace six crypto methods

SbiUtil.java — replace or isolate two crypto methods

HomeLoanAction.java — add session validation and null handling

HomeProcessManagerImpl.java — verify OTP decryption

jquery.homeloanOTP.js — replace static configuration and helper methods

jquery.homeloanOTP.js — modify Send and Confirm OTP

jquery.homeloanOTP.js — modify alternate OTP

jquery.homeloan_quote.js — remove static configuration and duplicate methods

jquery.homeloan_quote.js — modify callback OTP

HomeSecondPage.jsp — remove duplicate OTP names

Verify JavaScript loading order and ctxPath

Verify first-attempt OTP, resend and incorrect OTP

Verify no plaintext mobile OTP in request parameters

Verify other loan modules and concurrent OTP flows

Reset checklist

16. Final points to keep in mind

With these changes:

Requirement	Result
Static salt removed	Yes
Static IV removed	Yes
Static passphrase removed	Yes
Existing common API reused	Yes
Existing Java session reused	Yes
Existing request indexes retained	Yes
New Java classes required	No
New JAR files required	No
Java 8 compatible	Yes
Fresh configuration for each new Send OTP flow	Yes
Unique ciphertext for every verification retry	Not guaranteed

Important: This implementation follows your existing working encryption architecture. It generates fresh configuration for each new Send OTP flow, not every individual packet. Concurrent OTP flows in the same session can overwrite each other’s configuration, and reusing an AES-CBC IV is not recommended for production-grade cryptographic security.

I have intentionally preserved your existing email OTP, resend OTP and unrelated loan-token encryption logic to minimize changes. The proposed replacements have been reviewed against your attachments but have not been compiled or tested in your application. The remaining verification points are HomeProcessManagerImpl.java, the primary and alternate OTP JSPs, and the existing getRequest() serialization and validation logic.