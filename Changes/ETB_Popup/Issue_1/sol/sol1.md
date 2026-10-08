The PDF request is going to **`/sbi/downloadPrivacyNoticePdf.action`**, but the `web.xml` mapping supplied earlier covers **`/downloadPrivacyNoticePdf` without `.action`**.

Your current JSP line generates the `.action` suffix. This is the likely routing problem. My earlier suggestion to use `<s:url action="..."/>` introduced that mismatch.

The Discover errors come from a separate analytics endpoint.

### 1. `ConsentPopup.jsp` — replace one line

At **line 275** of your attached JSP, find:

```jsp
xhr.open("POST", '<s:url action="downloadPrivacyNoticePdf"/>', true);
```

Replace with:

```jsp
xhr.open(
    "POST",
    '<s:url value="/downloadPrivacyNoticePdf" includeContext="true" includeParams="none"/>',
    true
);
```

This should produce:

```text
/sbi/downloadPrivacyNoticePdf
```

Using `value` supplies the explicit path, while `includeContext="true"` includes the application context. [struts.apache.org](https://struts.apache.org/tag-developers/url-tag?utm_source=chatgpt.com)

**Keep the rest of `downloadPDF()` unchanged.**

### 2. Verify the existing XML mappings

You did not attach the updated XML files this time, so verify that the deployed versions contain these entries.

Inside the existing **`struts2` filter mapping** in `web.xml`:

```xml
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>
```

Inside the existing package in `struts-common-loan.xml`:

```xml
<action name="downloadPrivacyNoticePdf"
        method="downloadPrivacyNoticePdf"
        class="commonLoanAction">
</action>
```

If these entries already exist, **no XML change is required**. Rebuild and redeploy the updated application through your normal process.

### 3. Add one diagnostic log in `CommonLoanAction.java`

Your attached Java method starts at **line 880**:

```java
public StreamResult downloadPrivacyNoticePdf() {

    Document document = null;
```

Add one line:

```java
public StreamResult downloadPrivacyNoticePdf() {

    logger.info("Entered downloadPrivacyNoticePdf; locale={}", privacyLocale);

    Document document = null;
```

Your Java method also deliberately returns **404** when the notice is missing. Therefore, a 404 alone does not prove a routing error.

After applying the JSP change, check:

| Result | Meaning |
|---|---|
| No entry log and HTTP 404 | Request still is not reaching the action; check deployed XML mappings |
| Entry log and response text `Privacy Notice Not Found.` | Action works, but the locale has no notice ID or content |
| Entry log and HTTP 500 | Read the server exception logged by the PDF method |
| HTTP 200 with `application/pdf` | PDF generation succeeded; browser should download it |

In browser Developer Tools → **Network**, the request should now be:

```text
POST https://testonlineapply.sbi.co.in/sbi/downloadPrivacyNoticePdf
```

**It should no longer end with `.action`.**

### 4. Fix the existing JSP typo

At **line 117**, find:

```javascript
$("#consentHomeLoanDiv").html0(
    "Unable To Load Privacy Notice");
```

Replace with:

```javascript
$("#consentHomeLoanDiv").html(
    "Unable To Load Privacy Notice");
```

`html0()` is a typo. It affects your notice-loading error handler, independently of the PDF request.

### 5. `Discover_UIC_OCAS.js` — handle the separate console errors

At **line 14477**, your file contains:

```javascript
endpoint: "https://mardiscpr.dwhmartr.sbi.bank.in/DiscoverUIPost.php",
```

The reported errors mean:

| Error | Meaning |
|---|---|
| `ERR_NAME_NOT_RESOLVED` | The browser cannot resolve the hostname |
| `ERR_CONNECTION_RESET` | The connection was reset while contacting the endpoint |

These errors are separate from the PDF endpoint’s 404. Changing PDF code cannot repair that hostname or connection.

**If Discover should remain active on UAT:** keep its code unchanged and have the Discover/network team verify the configured UAT collector URL, client DNS/VPN access, and HTTPS connectivity. Do not replace it with `"DiscoverUIPost.php"` unless a collector or proxy actually exists at that application path.

**If Discover should be disabled on this UAT host:** add this guard inside the final configuration function, immediately after `"use strict";` near **line 14330**:

```javascript
// Default configuration
(function () {
    "use strict";

    // Skip Discover collection on this UAT host.
    if (window.location.hostname === "testonlineapply.sbi.co.in") {
        return;
    }

    // Keep all existing code below unchanged.
    var config, isReinitialized = false,
        DCX = window.DCX,
```

This skips the `DCX.init(config)` call in that configuration block and stops it starting Discover collection on that host. It **disables UAT analytics**, rather than repairing the collector connection.

After deploying the JSP and any intended Discover change, hard-refresh with browser caching disabled. The essential PDF fix is the **single URL replacement in step 1**; the log distinguishes any remaining routing issue from a missing database notice.
