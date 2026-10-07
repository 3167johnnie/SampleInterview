Use your existing **`CommonLoanAction.java`** for the common PDF endpoint. Your shared code already has `privacyLocale`, its getter/setter, and access to `commonService`.

The main change from the earlier prompt is to fetch the notice using your existing **`getPrivacyIdByLocale()` + `getNClobdata()`** calls.

Line numbers below refer to the original uploaded files; they will shift after insertion.

### 1. `CommonLoanAction.java` — add imports

After line **2**:

```java
import java.io.ByteArrayInputStream;
```

Add:

```java
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
```

After line **17**:

```java
import javax.servlet.http.HttpServletRequest;
```

Add:

```java
import javax.servlet.http.HttpServletResponse;
```

After the existing import:

```java
import org.apache.struts2.result.StreamResult;
```

Add:

```java
import org.apache.struts2.ServletActionContext;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
```

**Keep your existing `org.apache.struts2.result.StreamResult` import.** Do not copy the older `org.apache.struts2.dispatcher.StreamResult` import from the previous prompt.

### 2. `CommonLoanAction.java` — add the PDF method

Find the **active**, uncommented method near line **837**:

```java
public StreamResult getPrivacyNoticeByLocaleCom() {
```

Insert these two methods immediately **before** it:

```java
public StreamResult downloadPrivacyNoticePdf() {

    Document document = null;

    try {
        // 1. Default to English when no locale is supplied.
        String selectedLocale = privacyLocale == null
                || privacyLocale.trim().isEmpty()
                ? "eng"
                : privacyLocale.trim();

        // 2. Validate against your existing active language list.
        boolean supportedLocale = false;

        List<MasterLanguage> activeLanguages =
                commonService.getAllActiveLanguages();

        if (activeLanguages != null) {
            for (MasterLanguage language : activeLanguages) {
                if (selectedLocale.equals(language.getLannguageCode())) {
                    supportedLocale = true;
                    break;
                }
            }
        }

        if (!supportedLocale) {
            return privacyPdfError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid or inactive privacy notice language.");
        }

        // 3. Fetch the notice using the same calls as your popup.
        Integer privacyId =
                commonService.getPrivacyIdByLocale(selectedLocale);

        if (privacyId == null) {
            return privacyPdfError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Privacy Notice Not Found.");
        }

        String privacyText = commonService.getNClobdata(
                "RUPEEPOWER_OCAS_T_13703",
                "PRIVACY_NOTICE",
                "PRIVACY_ID",
                privacyId);

        if (privacyText == null || privacyText.trim().isEmpty()) {
            return privacyPdfError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Privacy Notice Not Found.");
        }

        // 4. Generate the PDF entirely in memory.
        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        document = new Document(PageSize.A4, 30, 30, 30, 30);

        PdfWriter writer =
                PdfWriter.getInstance(document, outputStream);

        document.open();

        try (ByteArrayInputStream htmlStream =
                new ByteArrayInputStream(
                        privacyText.getBytes(StandardCharsets.UTF_8))) {

            XMLWorkerHelper.getInstance().parseXHtml(
                    writer,
                    document,
                    htmlStream,
                    StandardCharsets.UTF_8);
        }

        // Close before reading bytes so the PDF is complete.
        document.close();
        document = null;

        byte[] pdfBytes = outputStream.toByteArray();

        // 5. Return the file to the browser.
        StreamResult result = new StreamResult(
                new ByteArrayInputStream(pdfBytes));

        result.setContentType("application/pdf");

        // Fixed filename avoids inserting request values into a header.
        result.setContentDisposition(
                "attachment;filename=\"SBI_Privacy_Notice.pdf\"");

        result.setContentLength(String.valueOf(pdfBytes.length));
        result.setAllowCaching(false);

        return result;

    } catch (Exception e) {
        logger.error("Exception while generating Privacy Notice PDF", e);

        return privacyPdfError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                "Unable to generate Privacy Notice PDF. Please try again.");

    } finally {
        if (document != null && document.isOpen()) {
            try {
                document.close();
            } catch (Exception closeException) {
                logger.warn(
                        "Exception while closing Privacy Notice PDF",
                        closeException);
            }
        }
    }
}

private StreamResult privacyPdfError(int status, String message) {

    ServletActionContext.getResponse().setStatus(status);

    byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);

    StreamResult result = new StreamResult(
            new ByteArrayInputStream(messageBytes));

    result.setContentType("text/plain;charset=UTF-8");
    result.setContentLength(String.valueOf(messageBytes.length));
    result.setAllowCaching(false);

    return result;
}
```

Two corrections to the previous prompt:

- `setContentLength()` takes a **String**, so use `String.valueOf(pdfBytes.length)`.
- Return an explicit error response when generation fails, instead of returning `null`. The stream result supports content type, attachment disposition, and content length. [struts.apache.org](https://struts.apache.org/core-developers/stream-result?utm_source=chatgpt.com)

Keep these existing members unchanged:

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

Also keep your existing privacy-notice and language-list methods.

### 3. `struts-common-loan.xml` — add the action

At the bottom of your shared file, immediately **before `</package>`**, near line **285**, add:

```xml
<!-- Common Privacy Notice PDF download for all loan products -->
<action name="downloadPrivacyNoticePdf"
        method="downloadPrivacyNoticePdf"
        class="commonLoanAction">
</action>
```

The bottom becomes:

```xml
        <action name="submitConsent"
                method="submitConsent"
                class="commonLoanAction">
            <result name="success" type="json"/>
            <interceptor-ref name="defaultStack"/>
            <interceptor-ref name="json">
                <param name="enableSMD">true</param>
            </interceptor-ref>
        </action>

        <!-- ADD THIS ACTION -->
        <action name="downloadPrivacyNoticePdf"
                method="downloadPrivacyNoticePdf"
                class="commonLoanAction">
        </action>

    </package>
</struts>
```

A `<result type="stream">` block is unnecessary here because the Java method returns a configured `StreamResult` object directly. Struts supports returning result objects from action methods. [struts.apache.org](https://struts.apache.org/core-developers/result-configuration.html?utm_source=chatgpt.com)

This uses your existing `commonLoanAction` bean, so **no new Spring bean or `PrivacyConsentAction` class is required**.

### 4. `web.xml` — add the URL to the existing Struts mapping

Your `struts2` filter mapping starts at line **62** and lists individual URLs.

Near line **288**, find:

```xml
<url-pattern>/getPrivacyLanguageListCve</url-pattern>
<dispatcher>FORWARD</dispatcher>
<dispatcher>REQUEST</dispatcher>
```

Replace that portion with:

```xml
<url-pattern>/getPrivacyLanguageListCve</url-pattern>

<!-- Common Privacy Notice PDF download -->
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>

<dispatcher>FORWARD</dispatcher>
<dispatcher>REQUEST</dispatcher>
```

**Add it inside the existing mapping whose filter name is `struts2`.**

Your existing `jpaFilter` mapping already contains:

```xml
<filter-mapping>
    <filter-name>jpaFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

That already covers the PDF URL; no extra JPA mapping is needed.

### 5. Frontend URL

The download function from your earlier prompt should call:

```javascript
url: "downloadPrivacyNoticePdf",
type: "POST",
data: {
    privacyLocale: $("#privacyLocaleDropdown").val() || "eng"
}
```

For a context-aware URL generated in a JSP, use:

```jsp
url: '<s:url action="downloadPrivacyNoticePdf"/>',
```

The endpoint is shared by Home Loan, Auto Loan, CVE, and the other loan popups.

### 6. Dependencies and checks

The Java code requires **iText 5 and XML Worker** on the application classpath:

```text
com.itextpdf:itextpdf
com.itextpdf.tool:xmlworker
```

Use matching versions approved for your project. The UTF-8 `parseXHtml()` overload used above is documented by iText. [api.itextpdf.com](https://api.itextpdf.com/iText5/java/5.5.9/com/itextpdf/tool/xml/XMLWorkerHelper.html?utm_source=chatgpt.com)

Before deployment, check:

| Check | Expected result |
|---|---|
| Download with `privacyLocale=eng` | HTTP 200, `application/pdf`, readable attachment |
| Download another active locale | Notice for the selected language |
| Unsupported locale | HTTP 400 |
| Active language with no notice | HTTP 404 |
| PDF generation error | HTTP 500 |

**Limits:** I could not compile this against your application because the dependencies and service implementations were not attached. Confirm that `getPrivacyIdByLocale()` filters `PRIVACY_IS_ACTIVE='Y'`. XML Worker needs compatible XHTML; UTF-8 alone does not ensure Hindi/Malayalam font rendering. This implements the attached prompt’s notice-only PDF; an SBI logo/header needs the actual local logo asset and a separate layout addition.
