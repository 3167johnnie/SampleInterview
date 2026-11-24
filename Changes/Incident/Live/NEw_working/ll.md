The NPE is clear: `SessionUtil.getCryptoSalt()` at line 1288 is returning **null** because the crypto salt was never populated in the session before `SbiUtil.getEncryptedResponse()` is called at state 14.

## Root Cause

You changed the salt/IV to come from a crypto service, but the value is **never set into the session** before the OTP response encryption path is hit. The call chain is:

```
state 14 → processMobileOTP() succeeds → SbiUtil.getEncryptedResponse(responseMessage)
  → SessionUtil.getCryptoSalt()  // ← NULL, NPE here
```

## Minimal Fix

You have two options (pick one):

### Option A — Set the salt in the session early (recommended)

In your `homeLoan()` or `homeLoanDSR()` method (or in an interceptor that runs before every request), fetch the crypto params **once** and store them in the session:

```java
// In homeLoan() or homeLoanDSR(), right after session setup:
if (SessionUtil.getCryptoSalt() == null) {
    // Call your crypto service to get salt + iv
    String salt = cryptoService.getCryptoSalt();   // your existing service
    String iv   = cryptoService.getCryptoIv();
    SessionUtil.setCryptoSalt(salt);
    SessionUtil.setCryptoIv(iv);
}
```

This ensures the salt is available for **all** downstream `getEncryptedResponse()` calls (states 3, 5, 14, 16, etc.).

### Option B — Lazy-load inside `SessionUtil.getCryptoSalt()`

If you can't guarantee the session is populated early, add a lazy-fetch in `SessionUtil.getCryptoSalt()` itself:

```java
public static String getCryptoSalt() {
    String salt = (String) getSession().getAttribute("cryptoSalt");
    if (salt == null) {
        // Fetch from crypto service
        salt = cryptoService.getCryptoSalt();
        getSession().setAttribute("cryptoSalt", salt);
    }
    return salt;
}
```

Same for `getCryptoIv()` if it's also fetched dynamically.

## Why it breaks at state 14 specifically

State 14 is the **send OTP** request. The response is encrypted via `SbiUtil.getEncryptedResponse()` (line 1106 in your action). If the crypto salt was only supposed to be set when the JSP renders (as per the previous approach of injecting via `<script>`), the **server-side** encryption path never had it. The JSP injection only helps the **client-side** JS — it does **not** populate the server session.

**You need the salt in the server session for `SbiUtil.getEncryptedResponse()` to work.** The JSP `<script>` injection and the server-side session value are two separate things — both are needed:

| Where | Purpose |
|-------|---------|
| `SessionUtil` (server) | `SbiUtil.getEncryptedResponse()` encrypts the AJAX response |
| JSP `<script>` (client) | JS `AesUtil.decrypt()` decrypts the AJAX response |

Both must use the **same** salt/IV values from your crypto service.

