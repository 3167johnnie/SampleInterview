**Keep your existing iText implementation; no Playwright changes are needed.** However, the exact dependency set you posted has an incompatible `typography` version, and `scope=system` prevents Maven from resolving required supporting libraries.

Also, **handling `parsererror` alone cannot create a PDF**. The server must successfully generate it and return the expected JSON.

### 1. Correct the dependencies

Replace your posted block with this. It retains the same seven artifact names, changes `typography` to **5.1.1**, and lets Maven resolve their supporting dependencies:

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
    <artifactId>licensing-remote</artifactId>
    <version>4.2.9</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>io</artifactId>
    <version>9.8.0</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>kernel</artifactId>
    <version>9.8.0</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>layout</artifactId>
    <version>9.8.0</version>
</dependency>

<dependency>
    <groupId>com.itextpdf</groupId>
    <artifactId>styled-xml-parser</artifactId>
    <version>9.8.0</version>
</dependency>
```

The documented combination is Core **9.8.0**, pdfHTML **6.3.4**, and pdfCalligraph **5.1.1**. Your `typography-5.0.2.jar` belongs to a different release combination. [iText Knowledge Base](https://kb.itextpdf.com/itext/compatibility-matrix?utm_source=chatgpt.com)

Add this repository if it is not already configured:

```xml
<repositories>
    <repository>
        <id>itext-releases</id>
        <url>https://repo.itextsupport.com/releases</url>
    </repository>
</repositories>
```

**Important:** this keeps the same renderer and direct dependencies, but supporting transitive JARs are still required. If you are restricted to exactly the seven physical JARs shown, I cannot guarantee this renderer will run.

### 2. Remove conflicting manually copied versions

After switching these dependencies to normal Maven scope:

1. Move their manually copied versions out of `WebContent/WEB-INF/lib`.
2. Ensure `typography-5.0.2.jar` is absent from the deployed application.
3. Build and inspect the dependency tree:

```bash
mvn clean package
```

```bash
mvn dependency:tree -Dincludes=com.itextpdf:*,com.itextpdf.licensing:*
```

Your WAR plugin packages normal runtime dependencies automatically.

Keep unrelated application JARs and legacy report dependencies unchanged.

### 3. Keep your existing PDF-generation code

Keep:

```java
HtmlConverter.convertToPdf(pdfHtml, output, properties);

pdfBytes = output.toByteArray();
```

Keep the existing JSON response:

```java
payload.put("status", "success");
payload.put("contentType", "application/pdf");
payload.put("pdfBase64",
        Base64.getEncoder().encodeToString(pdfBytes));

return privacyPdfJson(HttpServletResponse.SC_OK, payload);
```

Your attached `privacyPdfJson()` already sets the JSON content type correctly.

The font files and configured pdfCalligraph licence must also be available. **Adding the licensing JAR does not itself activate a licence.**

### 4. Add dependency-error handling in Java

Before the existing:

```java
} catch (Exception e) {
```

inside `downloadPrivacyNoticePdf()`, insert:

```java
} catch (LinkageError e) {

    logger.error(
            "Privacy PDF dependency failure; locale={}",
            selectedLocale,
            e);

    return privacyPdfError(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
            "PDF generation is unavailable due to a server configuration error.");

} catch (Exception e) {
```

Keep the existing `Exception` handler body unchanged.

This handles errors such as `NoClassDefFoundError` and `NoSuchMethodError` arising inside the method. It returns a JSON failure response instead of allowing those errors to escape into the application's general error handling.

### 5. Keep JSON AJAX; add a specific parser-error message

Keep:

```javascript
dataType : "json",
```

In the existing AJAX error callback, before the timeout condition, add:

```javascript
} else if (textStatus === "parsererror") {

    console.error(
        "PDF endpoint returned invalid JSON.",
        "HTTP:", xhr.status,
        "Content-Type:", xhr.getResponseHeader("Content-Type"),
        "Response length:", (xhr.responseText || "").length
    );

    alert(
        "The PDF server returned an unexpected response. " +
        "Please check the server logs."
    );

} else if (textStatus === "timeout") {
```

This improves the error message; it does not turn an unsuccessful server response into a download.

### 6. Verify the actual response

After redeployment, inspect **Network → downloadPrivacyNoticePdf → Response**:

| Response | What needs fixing |
|---|---|
| JSON with `status: "success"` and `pdfBase64` | Existing download callback should save the PDF |
| JSON with `status: "fail"` | Read its message and server exception |
| HTML login/error page | Fix the redirect, filter, or server failure |
| Empty or truncated body | Investigate response handling/server logs |

**The dependency correction addresses a confirmed problem. If HTTP 200 `parsererror` remains, share the response body’s error/login text and the corresponding server exception—not the Base64 PDF.** Without that evidence, changing more JSP code would be guessing.

The separate Discover JavaScript MIME error still requires correcting that script’s URL or server response.
