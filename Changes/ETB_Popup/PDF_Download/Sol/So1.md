Yes. Based on your existing code, I would implement this server-side so the browser only requests the PDF and the Tomcat/Java application generates it.

Your DB already stores the privacy notice as HTML, so we will reuse that HTML, render it as HTML in the PDF, and add a controlled SBI cover page before it.

Below is the complete implementation pattern.

⸻

1. Final flow

Your existing flow is:

DB
 ↓
Privacy HTML
 ↓
getPrivacyNoticeByLocale
 ↓
AJAX
 ↓
#consentHomeLoanDiv

We will add:

Download PDF
      ↓
Struts Action
      ↓
privacyLocale
      ↓
Existing Privacy Service
      ↓
Existing DAO
      ↓
HTML from DB
      ↓
PDF Generator
      ↓
SBI Cover Page
      ↓
Privacy HTML
      ↓
PDF download

There will be no html2pdf() and no external JavaScript URL.

⸻

2. Maven dependency

First check your existing pom.xml.

If you don’t already have an HTML-to-PDF library, one option is OpenHTMLToPDF PDFBox.

For a Java 8-compatible application:

<dependency>
    <groupId>com.openhtmltopdf</groupId>
    <artifactId>openhtmltopdf-pdfbox</artifactId>
    <version>1.0.10</version>
</dependency>

OpenHTMLToPDF’s PDFBox renderer is available through Maven Central and supports HTML/CSS rendering to PDF.

Before adding this to production, have ISD approve the exact version and its transitive dependencies. Do not replace an already-approved PDF library if your project already has one.

⸻

3. Put SBI logo inside the application

Do NOT use:

<img src="https://....">

For the PDF.

Put the logo in:

src/main/resources/pdf/sbi-logo.png

Your project becomes:

src
 └── main
      ├── java
      │    └── com
      │         └── mintstreet
      │              └── consent
      │
      ├── resources
      │    └── pdf
      │         └── sbi-logo.png
      │
      └── webapp

Maven will package this into:

WEB-INF/classes/pdf/sbi-logo.png

inside your WAR.

⸻

4. Create PDF service

Create:

src/main/java/com/mintstreet/consent/service/PrivacyNoticePdfService.java

Use:

package com.mintstreet.consent.service;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.stereotype.Service;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
@Service
public class PrivacyNoticePdfService {
    public byte[] generatePrivacyNoticePdf(String privacyHtml)
            throws Exception {
        if (privacyHtml == null ||
                privacyHtml.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Privacy notice content is empty");
        }
        String documentHtml =
                buildCompletePdfHtml(privacyHtml);
        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();
        PdfRendererBuilder builder =
                new PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(
                documentHtml,
                null
        );
        builder.toStream(outputStream);
        builder.run();
        return outputStream.toByteArray();
    }
    private String buildCompletePdfHtml(String privacyHtml)
            throws Exception {
        String logoDataUri =
                getSbiLogoDataUri();
        StringBuilder html =
                new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'/>");
        html.append("<style>");
        /*
         * ==============================
         * PAGE SETTINGS
         * ==============================
         */
        html.append("@page {");
        html.append("size: A4;");
        html.append("margin-top: 20mm;");
        html.append("margin-bottom: 20mm;");
        html.append("margin-left: 15mm;");
        html.append("margin-right: 15mm;");
        html.append("}");
        /*
         * ==============================
         * GENERAL
         * ==============================
         */
        html.append("body {");
        html.append("font-family: Arial, Helvetica, sans-serif;");
        html.append("font-size: 11pt;");
        html.append("line-height: 1.5;");
        html.append("color: #000000;");
        html.append("background: #ffffff;");
        html.append("margin: 0;");
        html.append("padding: 0;");
        html.append("}");
        /*
         * ==============================
         * COVER PAGE
         * ==============================
         */
        html.append(".cover-page {");
        html.append("height: 240mm;");
        html.append("text-align: center;");
        html.append("page-break-after: always;");
        html.append("}");
        html.append(".logo-container {");
        html.append("padding-top: 35mm;");
        html.append("}");
        html.append(".sbi-logo {");
        html.append("width: 65mm;");
        html.append("height: auto;");
        html.append("}");
        html.append(".bank-name {");
        html.append("font-size: 20pt;");
        html.append("font-weight: bold;");
        html.append("margin-top: 25mm;");
        html.append("}");
        html.append(".document-title {");
        html.append("font-size: 16pt;");
        html.append("font-weight: bold;");
        html.append("margin-top: 15mm;");
        html.append("}");
        /*
         * ==============================
         * PRIVACY CONTENT
         * ==============================
         */
        html.append(".privacy-content {");
        html.append("page-break-before: always;");
        html.append("font-family: Arial, Helvetica, sans-serif;");
        html.append("font-size: 11pt;");
        html.append("line-height: 1.5;");
        html.append("}");
        /*
         * Existing HTML tables
         */
        html.append(".privacy-content table {");
        html.append("width: 100%;");
        html.append("border-collapse: collapse;");
        html.append("}");
        html.append(".privacy-content td,");
        html.append(".privacy-content th {");
        html.append("padding: 5px;");
        html.append("vertical-align: top;");
        html.append("}");
        /*
         * Existing images
         */
        html.append(".privacy-content img {");
        html.append("max-width: 100%;");
        html.append("height: auto;");
        html.append("}");
        /*
         * Paragraph
         */
        html.append(".privacy-content p {");
        html.append("margin-top: 5px;");
        html.append("margin-bottom: 8px;");
        html.append("}");
        /*
         * Headings should not be separated
         */
        html.append(".privacy-content h1,");
        html.append(".privacy-content h2,");
        html.append(".privacy-content h3,");
        html.append(".privacy-content h4 {");
        html.append("page-break-after: avoid;");
        html.append("}");
        /*
         * Lists
         */
        html.append(".privacy-content ul,");
        html.append(".privacy-content ol {");
        html.append("margin-top: 5px;");
        html.append("margin-bottom: 10px;");
        html.append("}");
        /*
         * Avoid breaking table rows
         */
        html.append(".privacy-content tr {");
        html.append("page-break-inside: avoid;");
        html.append("}");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        /*
         * =================================
         * PAGE 1 - SBI COVER PAGE
         * =================================
         */
        html.append("<div class='cover-page'>");
        html.append("<div class='logo-container'>");
        html.append("<img ");
        html.append("class='sbi-logo' ");
        html.append("src='");
        html.append(logoDataUri);
        html.append("'/>");
        html.append("</div>");
        html.append("<div class='bank-name'>");
        html.append("STATE BANK OF INDIA");
        html.append("</div>");
        html.append("<div class='document-title'>");
        html.append("PRIVACY NOTICE");
        html.append("</div>");
        html.append("</div>");
        /*
         * =================================
         * PAGE 2+ - PRIVACY CONTENT
         * =================================
         */
        html.append("<div class='privacy-content'>");
        /*
         * IMPORTANT:
         *
         * This is the HTML retrieved from
         * your database.
         */
        html.append(privacyHtml);
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");
        return html.toString();
    }
    private String getSbiLogoDataUri()
            throws Exception {
        InputStream inputStream =
                getClass()
                .getClassLoader()
                .getResourceAsStream(
                        "pdf/sbi-logo.png"
                );
        if (inputStream == null) {
            throw new IllegalStateException(
                    "SBI logo not found in classpath: "
                    + "pdf/sbi-logo.png"
            );
        }
        ByteArrayOutputStream output =
                new ByteArrayOutputStream();
        byte[] buffer =
                new byte[4096];
        int length;
        while ((length =
                inputStream.read(buffer)) != -1) {
            output.write(
                    buffer,
                    0,
                    length
            );
        }
        inputStream.close();
        String base64 =
                Base64.getEncoder()
                      .encodeToString(
                              output.toByteArray()
                      );
        return "data:image/png;base64,"
                + base64;
    }
}

⸻

5. Why Base64 logo?

Instead of:

<img src="/images/sbi-logo.png">

we embed it into the PDF HTML:

<img src="data:image/png;base64,...">

This means the PDF renderer doesn’t need to access another URL.

That gives you:

No CDN
No external URL
No image-server dependency
No CORS

which is preferable for your use case.

⸻

6. Create the Struts Action

Create:

src/main/java/com/mintstreet/consent/action/PrivacyNoticePdfAction.java

Use your existing BaseAction pattern.

package com.mintstreet.consent.action;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.mintstreet.common.action.BaseAction;
import com.mintstreet.consent.service.PrivacyNoticePdfService;
import com.mintstreet.consent.service.PrivacyService;
public class PrivacyNoticePdfAction
        extends BaseAction {
    private static final long serialVersionUID = 1L;
    private static final Logger logger =
            LogManager.getLogger(
                    PrivacyNoticePdfAction.class
            );
    private String privacyLocale;
    private InputStream inputStream;
    @Autowired
    private PrivacyService privacyService;
    @Autowired
    private PrivacyNoticePdfService
            privacyNoticePdfService;
    public String downloadPrivacyNoticePDF() {
        try {
            /*
             * ==================================
             * Validate locale
             * ==================================
             */
            if (privacyLocale == null
                    || privacyLocale.trim().isEmpty()) {
                logger.warn(
                        "Privacy PDF requested without locale"
                );
                return ERROR;
            }
            privacyLocale =
                    privacyLocale.trim();
            /*
             * ==================================
             * Get privacy HTML
             * ==================================
             *
             * Reuse the SAME service used by
             * getPrivacyNoticeByLocale.
             */
            String privacyHtml =
                    privacyService
                    .getPrivacyNoticeByLocale(
                            privacyLocale
                    );
            if (privacyHtml == null
                    || privacyHtml.trim().isEmpty()) {
                logger.warn(
                        "Privacy notice not found for locale: {}",
                        privacyLocale
                );
                return ERROR;
            }
            /*
             * ==================================
             * Generate PDF
             * ==================================
             */
            byte[] pdfBytes =
                    privacyNoticePdfService
                    .generatePrivacyNoticePdf(
                            privacyHtml
                    );
            if (pdfBytes == null
                    || pdfBytes.length == 0) {
                logger.error(
                        "Generated PDF is empty"
                );
                return ERROR;
            }
            /*
             * ==================================
             * Stream PDF to browser
             * ==================================
             */
            inputStream =
                    new ByteArrayInputStream(
                            pdfBytes
                    );
            logger.info(
                    "Privacy PDF generated successfully "
                    + "for locale: {}",
                    privacyLocale
            );
            return SUCCESS;
        } catch (Exception e) {
            logger.error(
                    "Error generating privacy notice PDF",
                    e
            );
            return ERROR;
        }
    }
    public String getPrivacyLocale() {
        return privacyLocale;
    }
    public void setPrivacyLocale(
            String privacyLocale) {
        this.privacyLocale =
                privacyLocale;
    }
    public InputStream getInputStream() {
        return inputStream;
    }
}

⸻

7. Important: reuse your existing service

You currently have something like:

$.ajax({
    url: "getPrivacyNoticeByLocale",
    ...
});

That action is already retrieving the privacy notice.

Don’t duplicate the DAO query.

Suppose your existing service has:

public String getPrivacyNoticeByLocale(
        String locale) {
    return privacyRequestResponseDao
            .getPrivacyByLocale(locale);
}

Then use exactly that.

If your actual method has a different name, replace:

privacyService.getPrivacyNoticeByLocale(...)

with your existing method.

⸻

8. If your existing service returns JSONObject

If your existing implementation currently returns something like:

JSONObject response

instead of String, then extract:

String privacyHtml =
        response.getString("privacyNotice");

The PDF generator ultimately needs:

String privacyHtml

containing:

<p>Privacy notice...</p>
<table>...</table>

⸻

9. Struts configuration

Add this to your existing struts.xml package.

<action
    name="downloadPrivacyNoticePDF"
    class="com.mintstreet.consent.action.PrivacyNoticePdfAction"
    method="downloadPrivacyNoticePDF">
    <result name="success"
            type="stream">
        <param name="contentType">
            application/pdf
        </param>
        <param name="inputName">
            inputStream
        </param>
        <param name="contentDisposition">
            attachment;filename="SBI_Privacy_Consent.pdf"
        </param>
        <param name="bufferSize">
            4096
        </param>
    </result>
    <result name="error">
        /error.jsp
    </result>
</action>

If your application uses a common extends package, put the action there rather than creating a new package.

⸻

10. Change your JSP button

Remove:

<button onclick="downloadPDF()">Download PDF</button>

Use:

<button type="button"
        id="downloadPrivacyPdfBtn"
        class="btn btn-secondary"
        onclick="downloadPrivacyPDF();">
    Download PDF
</button>

⸻

11. Change your JavaScript

Delete your current:

function downloadPDF() {
    var element = document.getElementById(
        'consentHomeLoanDiv'
    );
    var opt = {
        ...
    };
    html2pdf().set(opt).from(element).save();
}

Delete it completely.

Replace it with:

function downloadPrivacyPDF() {
    var selectedLocale =
        $("#privacyLocaleDropdown").val();
    if (selectedLocale == null
            || $.trim(selectedLocale) === "") {
        alert("Please select a language.");
        return false;
    }
    /*
     * The server retrieves the privacy HTML
     * using the selected locale.
     */
    var downloadUrl =
        "downloadPrivacyNoticePDF"
        + "?privacyLocale="
        + encodeURIComponent(selectedLocale);
    /*
     * Browser receives application/pdf and
     * downloads the file.
     */
    window.location.href =
        downloadUrl;
    return false;
}

Now there is zero html2pdf dependency.

⸻

12. Your existing loadPrivacyByLocale() stays

This part remains:

success : function(response) {
    var json = JSON.parse(response);
    if (json.status == "success") {
        $("#consentHomeLoanDiv")
            .html(json.privacyNotice);
        $("#consentHomeLoanDiv")
            .scrollTop(0);
        resetConsentScrollValidation();
    } else {
        $("#consentHomeLoanDiv")
            .html("Privacy Notice Not Found");
    }
}

So UI behavior doesn’t change.

⸻

13. Fix your existing .html0() bug

You currently have:

error : function() {
    $("#consentHomeLoanDiv").html0(
        "Unable To Load Privacy Notice"
    );
}

Change to:

error : function() {
    $("#consentHomeLoanDiv").html(
        "Unable To Load Privacy Notice"
    );
}

⸻

14. What happens when user selects Hindi?

Suppose:

privacyLocale = hin

UI:

Hindi
 ↓
getPrivacyNoticeByLocale
 ↓
DB
 ↓
Hindi HTML
 ↓
#consentHomeLoanDiv

Then user clicks Download:

privacyLocale=hin
 ↓
downloadPrivacyNoticePDF
 ↓
DB
 ↓
Hindi HTML
 ↓
PDF

So the PDF is Hindi.

Likewise:

eng → English PDF
hin → Hindi PDF
mar → Marathi PDF

⸻

15. The PDF HTML can contain HTML tags

For example, DB:

<h2>Privacy Notice</h2>
<p>
State Bank of India respects your privacy.
</p>
<p>
The information collected may include:
</p>
<ul>
    <li>Personal information</li>
    <li>Contact information</li>
    <li>Financial information</li>
</ul>
<table>
    <tr>
        <th>Purpose</th>
        <th>Description</th>
    </tr>
    <tr>
        <td>Processing</td>
        <td>Processing the application</td>
    </tr>
</table>

The PDF renderer will render that as:

Privacy Notice
State Bank of India respects your privacy.
The information collected may include:
• Personal information
• Contact information
• Financial information
┌─────────────┬──────────────────────────┐
│ Purpose     │ Description              │
├─────────────┼──────────────────────────┤
│ Processing  │ Processing the application│
└─────────────┴──────────────────────────┘

It will not print <p> or <table> tags.

⸻

16. Security: don’t execute scripts stored in DB

This is important.

If your DB HTML contains:

<script>
    ...
</script>

we don’t want that executed.

I recommend stripping executable elements before PDF rendering.

Add this method to PrivacyNoticePdfService:

private String sanitizePrivacyHtml(
        String privacyHtml) {
    if (privacyHtml == null) {
        return "";
    }
    String sanitized =
            privacyHtml;
    /*
     * Remove script blocks
     */
    sanitized =
            sanitized.replaceAll(
                    "(?is)<script.*?>.*?</script>",
                    ""
            );
    /*
     * Remove iframe
     */
    sanitized =
            sanitized.replaceAll(
                    "(?is)<iframe.*?>.*?</iframe>",
                    ""
            );
    /*
     * Remove object
     */
    sanitized =
            sanitized.replaceAll(
                    "(?is)<object.*?>.*?</object>",
                    ""
            );
    /*
     * Remove embed
     */
    sanitized =
            sanitized.replaceAll(
                    "(?is)<embed.*?>",
                    ""
            );
    return sanitized;
}

Then:

String sanitizedHtml =
        sanitizePrivacyHtml(privacyHtml);
String documentHtml =
        buildCompletePdfHtml(
                sanitizedHtml
        );

But

If your DB content is already controlled and approved privacy content, don’t unnecessarily modify valid HTML. Ideally, use a proper HTML sanitizer library approved by ISD rather than relying on regex if you expect arbitrary HTML.

For your controlled banking privacy content, the most important thing is don’t execute JavaScript or allow external resource fetching.

⸻

17. External images inside DB HTML

Suppose DB contains:

<img src="https://example.com/image.png">

Don’t allow that for the PDF.

The PDF should ideally contain:

<img src="data:image/png;base64,...">

or locally available resources.

For your privacy notice, if there are no images inside the DB content, even better.

Your SBI logo is separately embedded by our PDF template.

⸻

18. Add page footer/page numbers later

Your future requirement may be:

Page 1 of 5

or:

State Bank of India
Privacy Notice
Page 2

We can add that through PDF CSS/template without changing the JSP.

For example, later:

@page {
    @bottom-center {
        content: "State Bank of India | Privacy Notice";
    }
}

But don’t add advanced CSS until we verify which CSS features your selected renderer supports.

⸻

19. WAR verification

After:

mvn clean package

check:

jar tf target/your-application.war \
    | grep sbi-logo.png

You should see:

WEB-INF/classes/pdf/sbi-logo.png

Check the PDF library:

jar tf target/your-application.war \
    | grep openhtmltopdf

You should see the required JAR under:

WEB-INF/lib/

⸻

20. Linux/Tomcat deployment

You don’t need to install Node.js.

You don’t need npm.

You don’t need Chrome.

You don’t need html2pdf.js.

The server only needs your normal Java/Tomcat environment.

Deploy:

your-application.war
        ↓
Tomcat
        ↓
WEB-INF/lib
        ↓
OpenHTMLToPDF
        ↓
Java PDF generation

⸻

21. Final project structure

Your relevant project should look approximately like:

project
│
├── pom.xml
│
└── src
    │
    └── main
        │
        ├── java
        │   │
        │   └── com
        │       └── mintstreet
        │           └── consent
        │               │
        │               ├── action
        │               │   └── PrivacyNoticePdfAction.java
        │               │
        │               └── service
        │                   └── PrivacyNoticePdfService.java
        │
        ├── resources
        │   │
        │   └── pdf
        │       └── sbi-logo.png
        │
        └── webapp
            │
            ├── js
            ├── css
            │
            └── WEB-INF
                └── struts.xml

⸻

22. Your final user experience

The user opens:

Privacy Consent

┌───────────────────────────────┐
│ Language: [ English ▼ ]       │
│                               │
│ Privacy Notice                │
│                               │
│ Your privacy content...       │
│                               │
│        [Download PDF]         │
│                               │
│          [Accept]             │
└───────────────────────────────┘

Clicks:

Download PDF

The browser calls:

POST/GET downloadPrivacyNoticePDF
privacyLocale=eng

Tomcat:

Get locale
   ↓
Existing Privacy Service
   ↓
Existing DAO
   ↓
DB HTML
   ↓
PDF template
   ↓
SBI logo
   ↓
Cover page
   ↓
Privacy HTML
   ↓
PDF bytes

Browser downloads:

SBI_Privacy_Consent.pdf

⸻

One thing I would change before you implement this

Because you said “complete code changes in existing code”, I don’t want to invent the names/signatures of your actual service and DAO.

You already have code behind:

getPrivacyNoticeByLocale

and you previously showed code involving PrivacyRequestResponseDao.

Send me these existing Java files:

1. getPrivacyNoticeByLocale Action class
2. Its Service class
3. PrivacyRequestResponseDao
4. Your relevant pom.xml
5. Your relevant struts.xml action/package section

Then I can merge the PDF functionality directly into your actual classes, preserving your existing naming, Spring injection, Struts configuration, DAO calls, and Java version rather than giving you parallel placeholder classes.
