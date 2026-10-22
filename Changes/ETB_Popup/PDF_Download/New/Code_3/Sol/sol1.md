I checked all three attachments. **Your AJAX expects JSON, and your Java helper already generates JSON correctly.** The HTTP 200 `parsererror` means the response arriving at the browser is empty, malformed JSON, or something else—often an HTML error/login page. The response body is needed to identify which. [jQuery API Documentation](https://api.jquery.com/jQuery.ajax/?utm_source=chatgpt.com)

I also found a definite backend dependency problem: **your POM mixes incompatible iText versions.**

### 1. Fix the PDF dependencies in `pom.xml`

Your attached POM contains:

| Component | Current version | Required matching version |
|---|---:|---:|
| `html2pdf` | 6.3.4 | 6.3.4 |
| Core modules | 9.8.0 | 9.8.0 |
| `typography` | **5.0.2** | **5.1.1** |
| `licensing-base` | Not explicitly listed | 4.2.9 |

The official compatibility matrix specifies this combination. [iText Knowledge Base](https://kb.itextpdf.com/itext/compatibility-matrix?utm_source=chatgpt.com)

Replace the **new-renderer dependency block at original lines 536–594** with:

```xml
<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>html2pdf</artifactId>
    <version>6.3.4</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>typography</artifactId>
    <version>5.1.1</version>
</dependency>

<dependency>
    <groupId>com.itextpdf.licensing</groupId>
    <artifactId>licensing-base</artifactId>
    <version>4.2.9</version>
</dependency>
```

Add this repository inside your existing `<repositories>` section, or create that section directly under `<project>`:

```xml
<repositories>
    <repository>
        <id>itext-releases</id>
        <url>https://repo.itextsupport.com/releases</url>
    </repository>
</repositories>
```

**Why remove `scope=system` here?** Those declarations do not resolve transitive dependencies. Your manually listed Core modules do not establish that the complete runtime dependency set is present.

After switching to these Maven dependencies:

- Move the manually copied **new-renderer JARs** out of `WebContent/WEB-INF/lib` to avoid packaging conflicting copies.
- In particular, remove the old `typography-5.0.2.jar` from the application package.
- Keep legacy iText 5 JARs if other reports need them.

Build again:

```bash
mvn clean package
```

**Licence requirement:** the attached code uses pdfCalligraph, which needs its commercial licence setup. The attachments do not show that initialization. Correcting dependencies does not remove that requirement. The free Playwright implementation discussed earlier is a different renderer; it is not present in these attachments.

### 2. Catch PDF dependency errors explicitly

Your Java method currently catches:

```java
catch (Exception e)
```

That does **not** catch `NoClassDefFoundError` or `NoSuchMethodError`, which can arise from missing or mismatched JARs.

In `HomeLoanAction.java`, immediately before the existing `catch (Exception e)` near **line 4293**, add:

```java
} catch (LinkageError e) {

    logger.error(
            "Privacy PDF runtime dependency failure; locale={}",
            selectedLocale,
            e);

    return privacyPdfError(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "PDF generation is unavailable due to a server configuration error.");

} catch (Exception e) {
```

Keep the existing exception-handler body below it unchanged.

This catches dependency failures occurring inside the method. If the action class itself cannot load, the application/server logs must identify that failure.

### 3. Keep `privacyPdfJson()` unchanged; add one log

Your helper at **line 4314** already uses:

```java
result.setContentType("application/json;charset=UTF-8");
```

Do not replace it with a PDF stream or add a JSON result mapping.

Immediately before its existing:

```java
return result;
```

Add:

```java
logger.info(
        "Privacy PDF JSON response prepared; status={}, bytes={}",
        status,
        jsonBytes.length);
```

This records whether the action prepared the expected response without logging the PDF contents.

### 4. JSP — improve the existing error diagnostic

Inside `downloadPDF()`, change:

```javascript
error : function(xhr, textStatus) {
```

To:

```javascript
error : function(xhr, textStatus, errorThrown) {
```

Immediately inside that callback, add:

```javascript
var body = xhr.responseText || "";

console.error(
    "Privacy PDF response diagnostic:",
    "HTTP:", xhr.status,
    "Content-Type:", xhr.getResponseHeader("Content-Type"),
    "Request status:", textStatus,
    "Parser error:", errorThrown && errorThrown.message
        ? errorThrown.message
        : String(errorThrown || ""),
    "Body length:", body.length,
    "First non-space character:", body.replace(/^\s+/, "").charAt(0)
);
```

Keep the existing JSON error-message parsing.

Before its existing timeout condition:

```javascript
} else if (textStatus === "timeout") {
```

Insert:

```javascript
} else if (textStatus === "parsererror") {
    alert(
        "The server returned an invalid PDF API response. " +
        "Please check the Network response and server logs."
    );

} else if (textStatus === "timeout") {
```

Keep this existing AJAX setting:

```javascript
dataType : "json",
```

Changing it to `"text"` would hide the JSON parsing error rather than fix the response.

### 5. Check the response after redeployment

Open **Developer Tools → Network → downloadPrivacyNoticePdf → Response**.

| Actual response | Required action |
|---|---|
| HTML login/session page | Fix the session/authentication redirect for this request |
| HTML error page | Read the corresponding server exception |
| Empty response | Check whether the action ran and whether response processing failed |
| Starts with `%PDF-` | Deployed backend is returning a raw PDF instead of the expected JSON |
| Valid JSON with `status: "fail"` | Follow its message and server exception |
| Truncated JSON | Check server/filter/proxy response handling |

Compare the request with these server logs:

```text
Entered downloadPrivacyNoticePdf
Privacy PDF generated
Privacy PDF JSON response prepared
```

If the last log exists but the browser receives HTML, investigate a filter, redirect, proxy, or a different deployed server instance. **The dependency mismatch is confirmed; its being the cause of this specific HTTP 200 response remains unconfirmed until the body/logs are checked.**

### 6. Fix the Discover script separately

This error:

```text
Discover_UIC_OCAS.js ... MIME type ('text/html')
```

means the script URL is being served as HTML. Inspect its Network response:

- If it contains an error/login page, correct the static-resource path, deployment, or authentication handling.
- If it contains actual JavaScript, correct the server response header to a JavaScript MIME type, such as `text/javascript`.

The failing URL is:

```text
/support/ocas/sbiNew/js/Discover_UIC_OCAS.js
```

It is outside the `/sbi` context in your PDF URL. That may be intentional, but verify that the server actually serves the file there. The script include is not present in these three attachments, so an exact replacement path cannot be established from them.

Do not disable strict MIME checking or label an HTML error page as JavaScript. [MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Headers/X-Content-Type-Options?isappinstalled=0\&utm_source=chatgpt.com)

Your supplied Struts download mapping can remain unchanged. The smallest justified changes are **aligning/resolving the PDF dependencies, catching linkage failures, and exposing the actual response failure**; the dropdown and PDF decoding flow need no rewrite.
