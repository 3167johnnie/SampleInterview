Yes. In your case, a **backend-generated PDF is better** than `html2pdf.js`.

You already have the privacy notice in DB and already have `getPrivacyNoticeByLocale()`. So the clean flow is:

```text
Download PDF button
       ↓
AJAX sends selected privacyLocale
       ↓
Struts action: generatePrivacyNoticePdf()
       ↓
CommonService → PrivacyRequestResponseDao
       ↓
Fetch privacy notice HTML from DB
       ↓
Backend converts HTML/text → PDF
       ↓
Return PDF bytes
       ↓
JavaScript receives Blob
       ↓
Browser downloads SBI_Privacy_Notice_eng.pdf
```

There is one important dependency question: Java itself has **no built-in HTML-to-PDF renderer**. Since you don't want a JS PDF library, the backend needs a PDF library. If your project already has **iText / OpenPDF / Flying Saucer** in its dependencies, use that. Based on the code you've shown so far, I cannot confirm which one your project already has.

For the **minimal implementation**, if iText 5 is already available (`com.itextpdf.*`), make these changes.

## 1. `ConsentPopup.jsp` — Download button

Remove:

```jsp
<button onclick="downloadPDF()">Download PDF</button>
```

and use:

```jsp
<button type="button"
		id="downloadPrivacyPdfBtn"
		class="btn btn-primary"
		onclick="downloadPrivacyNoticePdf();">
	Download PDF
</button>
```

Do not use `html2pdf()` anymore.

Delete your existing:

```javascript
function downloadPDF() {
	...
}
```

completely.

## 2. Add AJAX download function

Add this to the existing `<script>` in `ConsentPopup.jsp`:

```javascript
function downloadPrivacyNoticePdf() {

	var selectedLocale = $("#privacyLocaleDropdown").val();

	if (selectedLocale == null || $.trim(selectedLocale) == "") {
		selectedLocale = "eng";
	}

	$("#downloadPrivacyPdfBtn")
		.prop("disabled", true)
		.text("Downloading...");

	$.ajax({
		url : "downloadPrivacyNoticePdf",
		type : "POST",

		data : {
			privacyLocale : selectedLocale
		},

		xhrFields : {
			responseType : "blob"
		},

		success : function(data, status, xhr) {

			var contentType = xhr.getResponseHeader("Content-Type");

			if (contentType == null ||
					contentType.indexOf("application/pdf") === -1) {

				alert("Unable to download Privacy Notice.");
				return;
			}

			var blob = new Blob([data], {
				type : "application/pdf"
			});

			var downloadUrl = window.URL.createObjectURL(blob);

			var link = document.createElement("a");

			link.href = downloadUrl;
			link.download = "SBI_Privacy_Notice_"
					+ selectedLocale + ".pdf";

			document.body.appendChild(link);

			link.click();

			document.body.removeChild(link);

			window.URL.revokeObjectURL(downloadUrl);
		},

		error : function(xhr) {

			console.log(
				"Privacy Notice PDF download failed:",
				xhr.status
			);

			alert("Unable to download Privacy Notice. Please try again.");
		},

		complete : function() {

			$("#downloadPrivacyPdfBtn")
				.prop("disabled", false)
				.text("Download PDF");
		}
	});
}
```

No external JavaScript PDF library is required.

---

# 3. `HomeLoanAction.java` — add PDF imports

You already have:

```java
import java.io.ByteArrayInputStream;
```

Add:

```java
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
```

The last import:

```java
com.itextpdf.tool.xml.XMLWorkerHelper
```

requires iText XMLWorker.

If that class isn't available in your project, don't add a random JAR yet—tell me which PDF dependencies are already in your `pom.xml` / `WEB-INF/lib`, and I'll adapt the implementation to them.

---

# 4. Add action method to `HomeLoanAction.java`

You already have:

```java
public StreamResult getPrivacyNoticeByLocale()
```

and:

```java
public StreamResult getPrivacyLanguageList()
```

Add this method close to them:

```java
public StreamResult downloadPrivacyNoticePdf() {

	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	try {

		if (privacyLocale == null ||
				"".equals(privacyLocale.trim())) {

			privacyLocale = "eng";
		}

		logger.info(
			"Downloading Privacy Notice PDF for locale : "
			+ privacyLocale
		);

		PrivacyRequestResponse privacyObj =
				commonService.getPrivacyByLocale(privacyLocale);

		if (privacyObj == null ||
				privacyObj.getPrivacyNotice() == null) {

			logger.info(
				"Privacy Notice not found for locale : "
				+ privacyLocale
			);

			return null;
		}

		String privacyNotice =
				privacyObj.getPrivacyNotice();

		Document document =
				new Document(PageSize.A4);

		PdfWriter writer =
				PdfWriter.getInstance(
					document,
					outputStream
				);

		document.open();

		XMLWorkerHelper.getInstance()
			.parseXHtml(
				writer,
				document,
				new ByteArrayInputStream(
					privacyNotice.getBytes(
						StandardCharsets.UTF_8
					)
				),
				StandardCharsets.UTF_8
			);

		document.close();

		byte[] pdfBytes =
				outputStream.toByteArray();

		logger.info(
			"Privacy Notice PDF generated successfully. Locale : "
			+ privacyLocale
			+ ", PDF size : "
			+ pdfBytes.length
		);

		StreamResult result =
				new StreamResult(
					new ByteArrayInputStream(pdfBytes)
				);

		result.setContentType("application/pdf");

		result.setContentDisposition(
			"attachment;filename=\"SBI_Privacy_Notice_"
			+ privacyLocale
			+ ".pdf\""
		);

		result.setContentLength(pdfBytes.length);

		return result;

	} catch (Exception e) {

		logger.error(
			"Exception while generating Privacy Notice PDF",
			e
		);

		return null;

	} finally {

		try {
			outputStream.close();
		} catch (Exception e) {
			logger.error(
				"Exception while closing PDF output stream",
				e
			);
		}
	}
}
```

Notice that we are **not sending the HTML from the browser to the backend**.

The browser sends only:

```text
privacyLocale=eng
```

Backend again fetches the authoritative notice from DB:

```java
PrivacyRequestResponse privacyObj =
	commonService.getPrivacyByLocale(privacyLocale);
```

That's safer and cleaner.

---

# 5. `struts-home-loan-actions.xml`

You currently have:

```xml
<action name="getPrivacyNoticeByLocale"
	class="homeLoanAction"
	method="getPrivacyNoticeByLocale">

	<result type="stream">
		<param name="contentType">application/json</param>
	</result>

</action>
```

Add another action immediately after it:

```xml
<action name="downloadPrivacyNoticePdf"
	class="homeLoanAction"
	method="downloadPrivacyNoticePdf">

	<result type="stream">
		<param name="contentType">application/pdf</param>
		<param name="inputName">inputStream</param>
		<param name="bufferSize">4096</param>
	</result>

</action>
```

However, there is an important point with your current method design: you're returning `StreamResult` directly rather than the conventional Struts `String` + `InputStream` property approach.

Since your existing actions already successfully use:

```java
public StreamResult getPrivacyNoticeByLocale()
```

we can stay consistent with your project and simplify the Struts declaration to:

```xml
<action name="downloadPrivacyNoticePdf"
	class="homeLoanAction"
	method="downloadPrivacyNoticePdf">

	<result type="stream">
		<param name="contentType">application/pdf</param>
	</result>

</action>
```

I'd use this first because it matches the pattern already working in your application.

---

# 6. `web.xml`

You previously added:

```xml
<url-pattern>/getPrivacyNoticeByLocale</url-pattern>
<url-pattern>/savePrivacyConsent</url-pattern>
<url-pattern>/getPrivacyLanguageList</url-pattern>
```

Add:

```xml
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>
```

So:

```xml
<url-pattern>/getPrivacyNoticeByLocale</url-pattern>
<url-pattern>/savePrivacyConsent</url-pattern>
<url-pattern>/getPrivacyLanguageList</url-pattern>
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>
```

This is necessary because your application explicitly lists these action URLs in the filter mapping.

---

# 7. No DAO change

You already have exactly what we need:

```java
public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	Map<String, Object> params = new HashMap<String, Object>();

	params.put("privacyLocale", locale);
	params.put("privacyIsActive", "Y");

	try {

		PrivacyRequestResponse result =
			(PrivacyRequestResponse) getSingleResult(
				"PrivacyRequestResponse.getPrivacyByLocale",
				params
			);

		return result;

	} catch (Exception e) {

		logger.info(
			"Exception in getPrivacyByLocale",
			e
		);
	}

	return null;
}
```

No change.

---

# 8. No `CommonService` change

You already have:

```java
public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	return privacyRequestResponseDao.getPrivacyByLocale(locale);
}
```

No change.

---

# 9. No database changes

Your existing:

```text
RUPEEPOWER_OCAS_T_13703
```

already contains the privacy notice data retrieved through:

```java
PrivacyRequestResponse
```

So there are no DB changes.

---

# 10. Important: privacy HTML may not be valid XML

Your DB appears to contain HTML because you currently do:

```javascript
$("#consentHomeLoanDiv").html(json.privacyNotice);
```

If the HTML is simple:

```html
<p>...</p>
<div>...</div>
<b>...</b>
<br/>
<ul>
	<li>...</li>
</ul>
```

XMLWorker should generally handle it.

But malformed HTML such as:

```html
<br>
```

instead of:

```html
<br/>
```

or unclosed tags can cause XMLWorker problems.

Therefore, if your stored privacy notices contain complex HTML, I would prefer **OpenPDF + Flying Saucer** or the PDF library already approved in your SBI project rather than introducing iText/XMLWorker solely for this feature.

---

## Final files changed

For the backend solution, your changes are only:

```text
ConsentPopup.jsp
    ↓
Add Download PDF button
Add downloadPrivacyNoticePdf()
Remove old html2pdf downloadPDF()


HomeLoanAction.java
    ↓
Add downloadPrivacyNoticePdf()
Add PDF imports


struts-home-loan-actions.xml
    ↓
Add downloadPrivacyNoticePdf action


web.xml
    ↓
Add /downloadPrivacyNoticePdf
```

Existing code reused unchanged:

```text
PrivacyRequestResponseDao.java
        ↓
getPrivacyByLocale()

CommonService.java
        ↓
getPrivacyByLocale()

PrivacyRequestResponse.java
        ↓
getPrivacyNotice()

Database
        ↓
Existing privacy notice
```

One thing I would check **before you implement the Java method** is your project's existing `pom.xml` or the PDF-related JARs under `WEB-INF/lib`. If you paste the PDF-related dependencies/JAR names, I can give you the exact backend code using what your SBI application **already has**, rather than adding an incompatible iText dependency.
