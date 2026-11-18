Apply these changes in order to `CommonLoanAction.java`, `HomePopups.jsp`, and `homesbi.js`. The line numbers refer to the **original combined attachment**. Use the method names and surrounding code to find the same locations in your project.

The existing AJAX route stays `POST home` with `requestIndex=5`. The browser encrypts the OTP before sending it. Java decrypts the OTP, runs the existing verification, and encrypts the normal success or wrong-OTP response. Each encryption uses a fresh IV, so submitting the same OTP twice produces different ciphertext. The application continues using its existing HTTPS setup, with no application-level certificate handling or new JARs. The session key is available to the browser, so a person inspecting that session can still decrypt its packets.

## Step 1 — `CommonLoanAction.java`

### 1.1 Add imports near lines 4–45

Add these imports. `SecureRandom` and `ByteArrayInputStream` are already in the attachment.

```java
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
```

### 1.2 Add two fields below the class declaration at line 47

```java
private static final SecureRandom OTP_RANDOM = new SecureRandom();
private static final String OTP_KEY_ATTRIBUTE = "callbackOtpGcmKey";
```

### 1.3 Update the active `getCryptoConfig()` at line 680

Insert these lines immediately after `public StreamResult getCryptoConfig() {`, before the existing `JSONObject json` line. Leave the existing method body below them for the other callers.

```java
ServletActionContext.getResponse().setHeader("Cache-Control", "no-store");

if ("otp-gcm".equals(ServletActionContext.getRequest()
        .getHeader("X-Crypto-Mode"))) {
    return getOtpCryptoConfig();
}
```

The OTP handler selects the new format with `X-Crypto-Mode: otp-gcm`. This lets it reuse the existing Struts action URL while the other callers continue receiving their current config format.

### 1.4 Add these methods after `getCryptoConfig()` and before `logout()`

```java
private StreamResult getOtpCryptoConfig() {
    JSONObject json = new JSONObject();

    try {
        HttpSession session = ServletActionContext.getRequest().getSession();
        byte[] key;

        synchronized (session) {
            key = (byte[]) session.getAttribute(OTP_KEY_ATTRIBUTE);
            if (key == null) {
                key = new byte[32];
                OTP_RANDOM.nextBytes(key);
                session.setAttribute(OTP_KEY_ATTRIBUTE, key);
            }
        }

        json.put("key", Base64.getEncoder().encodeToString(key));
    } catch (JSONException e) {
        logger.error("Unable to create OTP crypto configuration", e);
        ServletActionContext.getResponse().setStatus(500);
    }

    return new StreamResult(new ByteArrayInputStream(
            json.toString().getBytes(StandardCharsets.UTF_8)));
}

private static Cipher createOtpCipher(int mode, byte[] key, byte[] iv,
        String direction) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
    cipher.init(mode, new SecretKeySpec(key, "AES"),
            new GCMParameterSpec(128, iv));
    cipher.updateAAD(direction.getBytes(StandardCharsets.US_ASCII));
    return cipher;
}

private static String decryptCallbackOtp(String packet, byte[] key)
        throws GeneralSecurityException {
    // Unpadded Base64URL for 12 IV bytes + 6 OTP bytes + 16 tag bytes.
    if (packet == null || packet.length() != 46) {
        throw new IllegalArgumentException("Invalid OTP packet length");
    }

    byte[] bytes = Base64.getUrlDecoder().decode(packet);
    if (bytes.length != 34) {
        throw new IllegalArgumentException("Invalid OTP packet");
    }

    byte[] iv = Arrays.copyOf(bytes, 12);
    byte[] decrypted = createOtpCipher(Cipher.DECRYPT_MODE, key, iv, "otp:req")
            .doFinal(bytes, 12, bytes.length - 12);

    String otp = new String(decrypted, StandardCharsets.UTF_8);
    if (!otp.matches("[0-9]{6}")) {
        throw new IllegalArgumentException("Invalid OTP format");
    }

    return otp;
}

private static String encryptCallbackOtpResponse(String message, byte[] key)
        throws GeneralSecurityException {
    byte[] iv = new byte[12];
    OTP_RANDOM.nextBytes(iv);

    byte[] encrypted = createOtpCipher(Cipher.ENCRYPT_MODE, key, iv, "otp:res")
            .doFinal(message.getBytes(StandardCharsets.UTF_8));

    byte[] packet = Arrays.copyOf(iv, iv.length + encrypted.length);
    System.arraycopy(encrypted, 0, packet, iv.length, encrypted.length);

    return "gcm1." + Base64.getUrlEncoder().withoutPadding()
            .encodeToString(packet);
}
```

The OTP mode generates the AES key directly. Its config response contains only `key`. Each encrypted packet carries its own IV.

### 1.5 Declare `otpKey` inside `home()` at line 181

Immediately after the existing `String inputOtp = null;`, add:

```java
byte[] otpKey = null;
```

### 1.6 Replace lines 194–213 inside `home()` state 5

Keep the CAPTCHA validation above this block and the `lead = commonService.getLeadById(...)` line below it. Replace the old `SessionUtil.getCryptoSalt()`, `getCryptoIv()`, `getCryptoPassPhrase()`, and `AesUtil.decrypt(...)` block with:

```java
HttpSession session = ServletActionContext.getRequest().getSession(false);
otpKey = session == null
        ? null
        : (byte[]) session.getAttribute(OTP_KEY_ATTRIBUTE);

if (otpKey == null) {
    responseMessage = "error|Session expired. Please try again.";
    return "jsonResponsePage";
}

try {
    inputOtp = decryptCallbackOtp(otherRequest.getInputOtpWantUsEnc(), otpKey);
} catch (GeneralSecurityException | IllegalArgumentException e) {
    responseMessage = "error|OTP verification failed. Please try again.";
    return "jsonResponsePage";
}
```

Keep the existing `processWantUsToCallYou(...)` call. It receives the decrypted six-digit `inputOtp`, including any leading zero.

### 1.7 Replace the two response-encryption blocks at lines 262–264 and 272–274

In both the success branch and the error branch, replace the existing `if (stateManagerBean.getState() != 4)` encryption block with:

```java
if (stateManagerBean.getState() == 5) {
    responseMessage = encryptCallbackOtpResponse(responseMessage, otpKey);
} else if (stateManagerBean.getState() != 4) {
    responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
}
```

Keep the existing code that builds `responseMessage`, appends `alertCount`, and clears CAPTCHA after success. The old `AesUtil` Java import can be removed if the file has no other active use of it.

## Step 2 — `HomePopups.jsp`

### 2.1 Replace lines 1112–1114 inside the `call_back` form

Replace the visible OTP input, the malformed commented input, and the hidden input with:

```jsp
<input type="text" id="inputOtpWantUs"
       class="form-control secure-otp"
       maxlength="6" inputmode="numeric" autocomplete="one-time-code" />

<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUsEnc" />
```

The visible input keeps its ID for validation but has no `name`, so normal form serialization skips it. The hidden field carries the encrypted value under `inputOtpWantUsEnc`.

## Step 3 — `homesbi.js`

### 3.1 Add three helpers after `getCryptoConfigOnce()` at line 1221

```javascript
async function loadCallbackOtpKey() {
    var config = await jQuery.ajax({
        type: 'POST',
        url: ctxPath + '/getCryptoConfig',
        headers: { 'X-Crypto-Mode': 'otp-gcm' },
        dataType: 'json'
    });

    var keyBytes = Uint8Array.from(atob(config.key), function (character) {
        return character.charCodeAt(0);
    });

    return window.crypto.subtle.importKey(
        'raw', keyBytes, 'AES-GCM', false, ['encrypt', 'decrypt']
    );
}

async function encryptCallbackOtp(key, otp) {
    var iv = window.crypto.getRandomValues(new Uint8Array(12));
    var encoder = new TextEncoder();

    var encrypted = new Uint8Array(await window.crypto.subtle.encrypt({
        name: 'AES-GCM',
        iv: iv,
        additionalData: encoder.encode('otp:req'),
        tagLength: 128
    }, key, encoder.encode(otp)));

    var packet = new Uint8Array(iv.length + encrypted.length);
    packet.set(iv);
    packet.set(encrypted, iv.length);

    return btoa(String.fromCharCode.apply(null, packet))
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=+$/, '');
}

async function readCallbackOtpResponse(key, response) {
    // Early CAPTCHA, session and validation errors use the existing format.
    if (response.indexOf('error|') === 0) {
        return response.split('|');
    }

    if (response.indexOf('gcm1.') !== 0) {
        throw new Error('Invalid OTP response format');
    }

    var encoded = response.substring(5).replace(/-/g, '+').replace(/_/g, '/');
    var packet = Uint8Array.from(atob(encoded), function (character) {
        return character.charCodeAt(0);
    });

    if (packet.length <= 28) {
        throw new Error('Invalid OTP response packet');
    }

    var decrypted = await window.crypto.subtle.decrypt({
        name: 'AES-GCM',
        iv: packet.subarray(0, 12),
        additionalData: new TextEncoder().encode('otp:res'),
        tagLength: 128
    }, key, packet.subarray(12));

    return new TextDecoder().decode(decrypted).split('|');
}
```

### 3.2 Fix the `#resendOtp` callback at lines 1545–1556

Replace the block from `var stringMessage = [];` through the `isEncrypted(...)` branch and its trailing commented line with:

```javascript
var stringMessage = htmlContent.split('|');
```

Keep the existing success and error UI directly below it. `requestIndex=4` already returns this format. The original branch treated asynchronous helpers as synchronous, which could cause `.split()` to run on a Promise.

### 3.3 Replace the entire `#confirmOtpWantUs` click handler at lines 1582–1676

Keep the next handler, `#leadApplyingFromIndiaWantUsNo`, directly below this replacement.

```javascript
jQuery('#confirmOtpWantUs').unbind('click').bind('click', async function (event) {
    event.preventDefault();

    var button = jQuery(this);
    if (button.data('otpPending')) {
        return;
    }

    hidePOPMsg('errorOTPMsg');
    if (!validateWantUstoCallyou(this, 3)) {
        return;
    }

    var otp = jQuery('#inputOtpWantUs').val();
    if (!/^[0-9]{6}$/.test(otp)) {
        showPOPMsg('errorOTPMsg', 'Please enter a valid six-digit OTP.', false);
        return;
    }

    var encryptedField = jQuery('#inputOtpWantUs1');
    button.data('otpPending', true);
    showDynamicLoader('confirmOtpWantUs');

    try {
        var key = await loadCallbackOtpKey();
        encryptedField.val(await encryptCallbackOtp(key, otp));

        var paramData = getRequest('call_back', 1);
        if (!paramData) {
            throw new Error('Unable to build OTP request');
        }

        var formdata = 'params=' + paramData + '&requestIndex=5&requestType=2';
        if (typeof uiType !== 'undefined' && uiType !== '') {
            formdata += '&uiType=' + encodeURIComponent(uiType);
        }

        var response = await jQuery.ajax({
            type: 'POST',
            url: ctxPath + '/' + ajaxPostUrl,
            data: formdata,
            dataType: 'text'
        });

        var stringMessage = await readCallbackOtpResponse(key, response);

        // Paste the original success/error UI block here,
        // from attachment line 1632 through line 1660 inclusive.
    } catch (e) {
        showPOPMsg('errorOTPMsg', SORRY_FOR_INCONVENIENCE, false);
    } finally {
        encryptedField.val('');
        button.removeData('otpPending');
        removeDynamicLoader('confirmOtpWantUs');
    }
});
```

Before replacing the old handler, copy **original lines 1632–1660**. That block starts with `if (stringMessage[0]=="success"){` and ends with the closing brace after `showPOPMsg('errorOTPMsg',stringMessage[1], false);`. Paste it at the marked location. This preserves your existing thank-you popup, tracking calls, close behavior, and wrong-OTP message.

Keep `getCryptoConfigOnce()`, `isEncrypted()`, and `getDecryptedResponse()` for the existing state-3 callback. After replacing the confirmation handler, you can remove `getEncryptedString()` at original lines 1834–1840 if a project-wide search shows no remaining callers.

## Step 4 — Check the two pieces outside the attachment

| File or component | Exact check |
|---|---|
| JavaScript file that defines `getRequest()` | If `getRequest('call_back', 1)` serializes named form fields, the JSP change handles it. If it explicitly builds `params`, read `inputOtpWantUs1` and put its value under `inputOtpWantUsEnc`. Remove any explicit read or serialization of the visible `inputOtpWantUs` in that branch. Preserve the existing `params` encoding. |
| State-5 request validation | If the config requires `inputOtpWantUs`, move that requirement to `inputOtpWantUsEnc`. If it validates the encrypted field’s format, use `^[A-Za-z0-9_-]{46}$`. Keep the six-digit check on the decrypted OTP in Java. |
| Struts mapping and result | Reuse the existing `getCryptoConfig`, `home`, and `jsonResponsePage` mappings. The config route must return JSON, and `jsonResponsePage` must emit the `responseMessage` text consumed by the existing AJAX code. No new `web.xml` or `SessionUtil` entries are needed for this patch. |

Deploy the Java and JavaScript changes together and bump the existing `SCRIPT_VERSION` used by the `homesbi.js` include so browsers pick up the updated handler.

## Step 5 — Verify in UAT

1. Submit the same **test** OTP twice within the permitted attempt limit. Inspect the two `home` requests in Network. The `inputOtpWantUsEnc` values should differ, and the decoded form parameters should contain no plaintext OTP field.
2. Verify a correct OTP and a wrong OTP. Both normal verification responses should start with `gcm1.` and display the appropriate existing UI.
3. Test stale CAPTCHA, session expiry, and both resend buttons. Early validation errors still use the existing `error|...` format.
4. Confirm the Tomcat JVM supports AES-256 with `Cipher.getMaxAllowedKeyLength("AES") >= 256`.

I mapped the edits to the supplied code. The attachment does not include `getRequest()`, the state-5 validation config, or the Struts result definition, so those integration checks still need to be verified in your project.
