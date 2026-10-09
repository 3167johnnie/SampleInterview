Use a **JSON response containing the generated PDF as Base64**. This lets the new function use the same `$.ajax()` and `json.status` pattern as your existing language-loading function.

The browser converts the Base64 into a PDF download. Your consent validation, checkbox handling, and Accept button remain unchanged.

**Apply the frontend and backend changes together.** This replaces the earlier endpoint’s raw-PDF response with JSON.

### 1. `ConsentPopup.jsp` — add the download button

Find:

```jsp
<div style="margin-top: 15px; text-align: center;">
    <button type="button" id="acceptConsentBtn"
```

Add the download button immediately before the existing Accept button:

```jsp
<div style="margin-top: 15px; text-align: center;">

    <!-- ADD: Download button -->
    <button type="button"
            id="downloadPrivacyPdfBtn"
            class="btn btn-primary"
            onclick="downloadPDF();"
            style="margin-right: 10px;">
        Download PDF
    </button>

    <!-- EXISTING: Keep Accept button unchanged -->
    <button type="button" id="acceptConsentBtn"
        class="btn btn-primary" disabled="disabled"
        onclick="acceptPrivacyConsent();"
        style="opacity: 0.6; cursor: not-allowed;">Accept</button>
</div>
```

### 2. `ConsentPopup.jsp` — add the AJAX function

Immediately before the final `</script>`, add this function.

If an older `downloadPDF()` remains in your deployed JSP, replace it rather than defining the function twice.

```javascript
function downloadPDF() {

    var selectedLocale = $("#privacyLocaleDropdown").val();
    var downloadBtn = $("#downloadPrivacyPdfBtn");

    if (downloadBtn.prop("disabled")) {
        return;
    }

    if (selectedLocale == null || $.trim(selectedLocale) === "") {
        alert("Please select a language before downloading.");
        return;
    }

    $
        .ajax({
            // Same relative URL pattern as your existing AJAX functions.
            // No ".action" suffix.
            url : "downloadPrivacyNoticePdf",
            type : "POST",
            dataType : "json",
            timeout : 60000,

            data : {
                privacyLocale : selectedLocale
            },

            beforeSend : function() {
                downloadBtn.prop("disabled", true)
                           .text("Downloading...");
            },

            success : function(response) {

                try {
                    var json = typeof response === "string"
                            ? JSON.parse(response)
                            : response;

                    if (!json ||
                            json.status !== "success" ||
                            json.contentType !== "application/pdf" ||
                            !json.pdfBase64) {

                        alert(json && json.message
                                ? json.message
                                : "Unable to generate Privacy Notice PDF.");
                        return;
                    }

                    // Decode the PDF returned by the Java action.
                    var binary = window.atob(json.pdfBase64);

                    // Check the PDF signature before saving.
                    if (binary.substring(0, 5) !== "%PDF-") {
                        throw new Error("Invalid PDF response.");
                    }

                    var bytes = new Uint8Array(binary.length);

                    for (var i = 0; i < binary.length; i++) {
                        bytes[i] = binary.charCodeAt(i);
                    }

                    var blob = new Blob([bytes], {
                        type : "application/pdf"
                    });

                    var downloadUrl = window.URL.createObjectURL(blob);

                    try {
                        var link = document.createElement("a");

                        link.href = downloadUrl;
                        link.download = "SBI_Privacy_Notice.pdf";
                        link.style.display = "none";

                        document.body.appendChild(link);

                        try {
                            link.click();
                        } finally {
                            document.body.removeChild(link);
                        }

                    } finally {
                        window.setTimeout(function() {
                            window.URL.revokeObjectURL(downloadUrl);
                        }, 10000);
                    }

                } catch (e) {
                    // Do not log the Base64 PDF or notice content.
                    console.error("Privacy PDF download failed:", e.message);
                    alert("Unable to save Privacy Notice PDF. Please try again.");
                }
            },

            error : function(xhr, textStatus) {

                var json = xhr.responseJSON;

                // Compatibility with jQuery versions without responseJSON.
                if (!json && xhr.responseText) {
                    try {
                        json = JSON.parse(xhr.responseText);
                    } catch (ignore) {
                        // An HTML error page is not a JSON API response.
                    }
                }

                console.error(
                    "Privacy PDF request failed. HTTP status:",
                    xhr.status,
                    "Request status:",
                    textStatus
                );

                if (json && json.message) {
                    alert(json.message);

                } else if (textStatus === "timeout") {
                    alert("PDF download timed out. Please try again.");

                } else if (xhr.status === 404) {
                    alert(
                        "PDF download endpoint was not found. " +
                        "Please check the deployed Struts and web.xml mappings."
                    );

                } else {
                    alert("Unable to download Privacy Notice PDF. Please try again.");
                }
            },

            complete : function() {
                downloadBtn.prop("disabled", false)
                           .text("Download PDF");
            }
        });
}
```

`dataType : "json"` tells jQuery to parse the JSON response. The string/object check matches your existing coding pattern. No Blob-specific AJAX transport is needed. [jQuery API Documentation](https://api.jquery.com/jQuery.ajax/?utm_source=chatgpt.com)

Downloading does not set the consent flag, check any checkbox, enable Accept, or close the popup.

### 3. Fix the existing error-handler typo

This is separate from the download addition.

Find:

```javascript
$("#consentHomeLoanDiv").html0(
        "Unable To Load Privacy Notice");
```

Replace with:

```javascript
$("#consentHomeLoanDiv").html(
        "Unable To Load Privacy Notice");
```

Keep the other existing functions unchanged.

### 4. `CommonLoanAction.java` — add imports

Your newly attached Java file has **no PDF method**. Its existing `privacyLocale`, `commonService`, logger, `Map`, `LinkedHashMap`, and `StreamResult` can be reused.

After the existing `ByteArrayInputStream` import near line **2**, add:

```java
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
```

After the existing servlet imports, add:

```java
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
```

After the existing Struts imports, add:

```java
import org.apache.struts2.ServletActionContext;
```

Add these imports with the other third-party imports:

```java
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
```

Keep this existing import:

```java
import org.apache.struts2.result.StreamResult;
```

The code below requires **Java 8 or later**, matching your existing Java environment.

### 5. Add two size-limit constants

Below:

```java
private static final long serialVersionUID = 1L;
```

Add:

```java
// Limits for the small Privacy Notice download endpoint.
private static final int MAX_PRIVACY_HTML_BYTES = 1024 * 1024;
private static final int MAX_PRIVACY_PDF_BYTES = 5 * 1024 * 1024;
```

These limits reject oversized notice content and oversized generated responses. Base64 adds approximately one-third to the PDF response size, so this approach is suitable for small notice documents.

### 6. Add the backend method and helpers

In your attached Java file, find the **uncommented** method at original line **838**:

```java
public StreamResult getPrivacyNoticeByLocaleCom() {
```

Insert the following methods immediately **before** it.

If you merge this into a different copy that already has an older PDF method, replace that older method and its error helper.

```java
public StreamResult downloadPrivacyNoticePdf() {

    HttpServletRequest request = ServletActionContext.getRequest();

    if (!"POST".equalsIgnoreCase(request.getMethod())) {
        ServletActionContext.getResponse().setHeader("Allow", "POST");

        return privacyPdfError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Use POST to download the Privacy Notice.");
    }

    String selectedLocale = privacyLocale == null
            ? ""
            : privacyLocale.trim();

    if (!selectedLocale.matches("[A-Za-z0-9_-]{1,20}")) {
        return privacyPdfError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Please select a valid privacy notice language.");
    }

    Document document = null;

    try {
        logger.info(
                "Entered downloadPrivacyNoticePdf; locale={}",
                selectedLocale);

        // Validate against your existing active language master.
        boolean activeLocale = false;

        List<MasterLanguage> activeLanguages =
                commonService.getAllActiveLanguages();

        if (activeLanguages != null) {
            for (MasterLanguage language : activeLanguages) {
                if (language != null &&
                        selectedLocale.equals(language.getLannguageCode())) {

                    activeLocale = true;
                    break;
                }
            }
        }

        if (!activeLocale) {
            return privacyPdfError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "The selected privacy notice language is unavailable.");
        }

        // Use the same database retrieval as the existing notice method.
        Integer privacyId =
                commonService.getPrivacyIdByLocale(selectedLocale);

        if (privacyId == null) {
            return privacyPdfError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Privacy Notice Not Found for the selected language.");
        }

        String privacyText = commonService.getNClobdata(
                "RUPEEPOWER_OCAS_T_13703",
                "PRIVACY_NOTICE",
                "PRIVACY_ID",
                privacyId);

        if (privacyText == null || privacyText.trim().isEmpty()) {
            return privacyPdfError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Privacy Notice Not Found for the selected language.");
        }

        // Check size before conversion.
        if (privacyText.length() > MAX_PRIVACY_HTML_BYTES ||
                privacyText.getBytes(StandardCharsets.UTF_8).length
                        > MAX_PRIVACY_HTML_BYTES) {

            return privacyPdfError(
                    HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    "The Privacy Notice exceeds the PDF download limit.");
        }

        /*
         * Validate the database template as XHTML.
         * No browser-submitted HTML is accepted.
         * Resource-loading tags and external CSS are rejected.
         */
        String xhtml = preparePrivacyPdfXhtml(privacyText);

        byte[] pdfBytes;

        try (ByteArrayOutputStream output =
                new ByteArrayOutputStream()) {

            document = new Document(PageSize.A4, 36, 36, 36, 36);

            PdfWriter writer = PdfWriter.getInstance(document, output);

            document.open();

            try (ByteArrayInputStream htmlStream =
                    new ByteArrayInputStream(
                            xhtml.getBytes(StandardCharsets.UTF_8))) {

                XMLWorkerHelper.getInstance().parseXHtml(
                        writer,
                        document,
                        htmlStream,
                        StandardCharsets.UTF_8);
            }

            // Finalize the PDF before reading the output.
            document.close();
            document = null;

            pdfBytes = output.toByteArray();
        }

        if (pdfBytes.length == 0) {
            throw new IllegalStateException("Generated PDF is empty.");
        }

        if (pdfBytes.length > MAX_PRIVACY_PDF_BYTES) {
            return privacyPdfError(
                    HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                    "The generated PDF exceeds the download limit.");
        }

        Map<String, Object> payload =
                new LinkedHashMap<String, Object>();

        payload.put("status", "success");
        payload.put("contentType", "application/pdf");
        payload.put("pdfBase64",
                Base64.getEncoder().encodeToString(pdfBytes));

        logger.info(
                "Privacy PDF generated; locale={}, bytes={}",
                selectedLocale,
                pdfBytes.length);

        return privacyPdfJson(
                HttpServletResponse.SC_OK,
                payload);

    } catch (NoResultException e) {
        return privacyPdfError(
                HttpServletResponse.SC_NOT_FOUND,
                "Privacy Notice Not Found for the selected language.");

    } catch (Exception e) {
        logger.error(
                "Privacy PDF generation failed; locale={}",
                selectedLocale,
                e);

        return privacyPdfError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to generate Privacy Notice PDF. Please try again.");

    } finally {
        if (document != null && document.isOpen()) {
            try {
                document.close();
            } catch (Exception closeException) {
                logger.warn(
                        "Unable to close Privacy PDF document",
                        closeException);
            }
        }
    }
}

private StreamResult privacyPdfError(int status, String message) {

    logger.warn(
            "Privacy PDF request failed; status={}, reason={}",
            status,
            message);

    Map<String, Object> payload =
            new LinkedHashMap<String, Object>();

    payload.put("status", "fail");
    payload.put("message", message);

    return privacyPdfJson(status, payload);
}

private StreamResult privacyPdfJson(
        int status,
        Map<String, Object> payload) {

    byte[] jsonBytes = new JSONObject(payload)
            .toString()
            .getBytes(StandardCharsets.UTF_8);

    HttpServletResponse response =
            ServletActionContext.getResponse();

    response.setStatus(status);
    response.setHeader("Cache-Control", "no-store");
    response.setHeader("X-Content-Type-Options", "nosniff");

    StreamResult result = new StreamResult(
            new ByteArrayInputStream(jsonBytes));

    result.setContentType("application/json;charset=UTF-8");
    result.setContentLength(String.valueOf(jsonBytes.length));
    result.setAllowCaching(false);

    return result;
}

/*
 * XML Worker expects XHTML, rather than arbitrary browser HTML.
 * This helper accepts a fragment or an <html> document.
 *
 * It normalizes common <br>, <hr>, and &nbsp; forms, validates XML
 * without external entities, and rejects resource-loading markup.
 */
private String preparePrivacyPdfXhtml(String html) throws Exception {

    String xhtml = html.trim()
            .replace("&nbsp;", "&#160;")
            .replaceAll("(?i)<br\\s*>", "<br />")
            .replaceAll("(?i)<hr\\s*>", "<hr />");

    if (!xhtml.matches("(?is)^<html(?:\\s|>).*")) {
        xhtml = "<html><head></head><body>"
                + xhtml
                + "</body></html>";
    }

    DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();

    factory.setNamespaceAware(true);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature(
            "http://apache.org/xml/features/disallow-doctype-decl",
            true);
    factory.setFeature(
            "http://xml.org/sax/features/external-general-entities",
            false);
    factory.setFeature(
            "http://xml.org/sax/features/external-parameter-entities",
            false);

    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

    org.w3c.dom.Document parsed;

    try (ByteArrayInputStream input = new ByteArrayInputStream(
            xhtml.getBytes(StandardCharsets.UTF_8))) {

        parsed = factory.newDocumentBuilder().parse(input);
    }

    NodeList elements = parsed.getElementsByTagName("*");

    for (int i = 0; i < elements.getLength(); i++) {
        Element element = (Element) elements.item(i);

        String tag = element.getLocalName();

        if (tag == null) {
            tag = element.getTagName();
        }

        if ("img".equalsIgnoreCase(tag) ||
                "link".equalsIgnoreCase(tag) ||
                "script".equalsIgnoreCase(tag) ||
                "iframe".equalsIgnoreCase(tag) ||
                "object".equalsIgnoreCase(tag) ||
                "embed".equalsIgnoreCase(tag) ||
                "base".equalsIgnoreCase(tag)) {

            throw new IllegalArgumentException(
                    "Resource-loading markup is unsupported in PDF notices.");
        }

        String css = element.getAttribute("style");

        if ("style".equalsIgnoreCase(tag)) {
            css += element.getTextContent();
        }

        // Reject URL/import CSS forms, including escaped variants.
        if (css.indexOf('\\') >= 0 ||
                css.contains("/*") ||
                css.matches("(?is).*(?:url\\s*\\(|@import).*")) {

            throw new IllegalArgumentException(
                    "External or escaped CSS is unsupported in PDF notices.");
        }
    }

    return xhtml;
}
```

The XHTML validation protects the backend conversion from loading resources referenced by the database HTML. It does not alter the stored notice. XML Worker’s documented API converts XHTML through the UTF-8 `parseXHtml()` overload. [api.itextpdf.com](https://api.itextpdf.com/iText5/java/5.5.13/com/itextpdf/tool/xml/XMLWorkerHelper.html?utm_source=chatgpt.com)

**Template requirement:** this version supports text, lists, tables, and compatible inline CSS. It deliberately rejects images, external stylesheets, and resource-loading tags. Other HTML named entities, such as `&copy;`, should be stored as literal Unicode or numeric entities such as `&#169;`. Malformed templates produce a logged HTTP 500 rather than a damaged PDF.

### 7. Keep existing fields and service wiring

Do not add a second `privacyLocale` or another `commonService`.

Keep your existing:

```java
private String privacyLocale;
```

```java
public String getPrivacyLocale() {
    return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
    this.privacyLocale = privacyLocale;
}
```

No new Spring action bean is required.

### 8. `struts-common-loan.xml` — add or retain the mapping

Inside the existing package, before `</package>`:

```xml
<action name="downloadPrivacyNoticePdf"
        method="downloadPrivacyNoticePdf"
        class="commonLoanAction">
</action>
```

Do not add a `<result type="json">` block: the method already returns a configured `StreamResult` containing JSON.

Ensure `struts-common-loan.xml` remains included by your deployed Struts configuration.

### 9. `web.xml` — add or retain the filter mapping

Inside the existing mapping whose filter name is `struts2`, add:

```xml
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>
```

Keep it before that mapping’s `<dispatcher>` entries.

The three names must match:

| Location | Name |
|---|---|
| JSP AJAX URL | `downloadPrivacyNoticePdf` |
| Struts action name | `downloadPrivacyNoticePdf` |
| `web.xml` URL pattern | `/downloadPrivacyNoticePdf` |

There is no `.action` suffix in this implementation.

### 10. Deployment and acceptance checks

Keep the approved matching `itextpdf` and `xmlworker` JARs in the WAR’s `WEB-INF/lib`, then rebuild and redeploy.

After deployment:

1. Open the popup and select English.
2. Click Download PDF.
3. In Network, confirm `POST .../sbi/downloadPrivacyNoticePdf`.
4. Confirm HTTP 200 with JSON containing `status: "success"`.
5. Open the downloaded PDF and compare its content with the popup.
6. Confirm downloading leaves the popup open and the consent state unchanged.
7. Repeat for every supported language.

For a missing notice, the API returns HTTP 404 with a JSON message. A routing 404 normally returns an HTML error page; the AJAX error handler distinguishes these.

**Before calling this production-ready:** confirm that `getPrivacyIdByLocale()` selects only active notices, verify every database template, and test language rendering. UTF-8 alone does not supply embedded Hindi/Malayalam fonts or guarantee correct Indic shaping with iText 5. The download retrieves the selected locale’s current database notice; if that notice changes after the popup loads, the downloaded content can differ.

I have checked the changes against the attached Java structure, but could not compile or run them against your application dependencies and database. The separate Discover collector CORS errors are unaffected by this PDF endpoint.
