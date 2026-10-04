Yes. Keep the PDF generation **common for every loan**, but change the generated document structure to:

```text
PAGE 1
┌──────────────────────────────────────────┐
│               SBI LOGO                   │
│                                          │
│          STATE BANK OF INDIA             │
│                                          │
│             PRIVACY NOTICE               │
│                                          │
│       Official Privacy Notice            │
│                                          │
│       Language: English                  │
│                                          │
└──────────────────────────────────────────┘

PAGE 2 ONWARDS
┌──────────────────────────────────────────┐
│ PRIVACY NOTICE                           │
│ ──────────────────────────────────────── │
│                                          │
│ Actual privacy notice content from DB... │
│                                          │
│                                          │
└──────────────────────────────────────────┘
```

You don't need different implementations for HL, Auto Loan, CVE, etc.

## 1. Keep the logo on the server

Do **not** send the logo from JavaScript.

You already have an SBI image folder in your JSP through:

```jsp
@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI
```

For backend PDF generation, however, you need the **physical/server resource path**, not the browser URL.

For example, suppose your logo is:

```text
WebContent/JS/ocas/sbiNew/images/sbi-logo.png
```

In the common action, obtain it through the servlet context.

Add imports:

```java
import java.io.InputStream;

import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Element;
```

---

# 2. Modify common `PrivacyConsentAction`

Instead of putting all PDF code inside `downloadPrivacyNoticePdf()`, create separate common methods.

That gives you:

```java
downloadPrivacyNoticePdf()
createPdfCoverPage()
addPrivacyNoticeContent()
```

This is reusable for every loan.

## Main PDF method

```java
public StreamResult downloadPrivacyNoticePdf() {

	ByteArrayOutputStream outputStream =
			new ByteArrayOutputStream();

	Document document = null;

	try {

		/*
		 * Default locale
		 */
		if (privacyLocale == null
				|| "".equals(privacyLocale.trim())) {

			privacyLocale = "eng";
		}

		logger.info(
				"Privacy Notice PDF requested for locale : "
				+ privacyLocale);

		/*
		 * Get Privacy Notice from DB
		 */
		PrivacyRequestResponse privacyObj =
				commonService.getPrivacyByLocale(
						privacyLocale);

		if (privacyObj == null
				|| privacyObj.getPrivacyNotice() == null
				|| "".equals(
					privacyObj.getPrivacyNotice().trim())) {

			logger.info(
					"Privacy Notice not found for locale : "
					+ privacyLocale);

			return null;
		}

		/*
		 * Create PDF
		 */
		document = new Document(
				PageSize.A4,
				40,
				40,
				40,
				40);

		PdfWriter writer =
				PdfWriter.getInstance(
						document,
						outputStream);

		document.open();


		/*
		 * PAGE 1
		 *
		 * SBI Logo +
		 * State Bank of India +
		 * Privacy Notice heading
		 */
		createPdfCoverPage(
				document,
				privacyLocale);


		/*
		 * PAGE 2
		 */
		document.newPage();


		/*
		 * Privacy Notice from DB
		 */
		addPrivacyNoticeContent(
				writer,
				document,
				privacyObj.getPrivacyNotice());


		document.close();

		byte[] pdfBytes =
				outputStream.toByteArray();


		logger.info(
				"Privacy Notice PDF generated successfully. "
				+ "Locale : "
				+ privacyLocale
				+ ", Size : "
				+ pdfBytes.length);


		StreamResult result =
				new StreamResult(
						new ByteArrayInputStream(
								pdfBytes));


		result.setContentType(
				"application/pdf");


		result.setContentDisposition(
				"attachment;filename=\"SBI_Privacy_Notice_"
				+ privacyLocale
				+ ".pdf\"");


		result.setContentLength(
				pdfBytes.length);


		return result;

	} catch (Exception e) {

		logger.error(
				"Exception while generating Privacy Notice PDF",
				e);

		return null;

	} finally {

		try {

			if (document != null
					&& document.isOpen()) {

				document.close();
			}

			outputStream.close();

		} catch (Exception e) {

			logger.error(
					"Exception while closing PDF resources",
					e);
		}
	}
}
```

---

# 3. Create common first-page method

Add this method in the same `PrivacyConsentAction.java`.

```java
private void createPdfCoverPage(
		Document document,
		String locale) throws Exception {


	/*
	 * Space from top
	 */
	document.add(
			new Paragraph("\n\n"));


	/*
	 * SBI LOGO
	 */
	try {

		InputStream logoStream =
				ServletActionContext
					.getServletContext()
					.getResourceAsStream(
						"/JS/ocas/sbiNew/images/sbi-logo.png");


		if (logoStream != null) {

			ByteArrayOutputStream logoBuffer =
					new ByteArrayOutputStream();

			byte[] buffer =
					new byte[4096];

			int length;

			while ((length =
					logoStream.read(buffer)) != -1) {

				logoBuffer.write(
						buffer,
						0,
						length);
			}


			Image logo =
					Image.getInstance(
							logoBuffer.toByteArray());


			/*
			 * Maintain aspect ratio
			 */
			logo.scaleToFit(
					110,
					110);


			logo.setAlignment(
					Element.ALIGN_CENTER);


			document.add(logo);


			logoStream.close();
			logoBuffer.close();
		}

	} catch (Exception e) {

		/*
		 * Do not fail entire PDF because
		 * logo could not be loaded.
		 */
		logger.error(
				"Unable to load SBI logo for Privacy PDF",
				e);
	}


	document.add(
			new Paragraph("\n"));


	/*
	 * STATE BANK OF INDIA
	 */
	Font bankFont =
			FontFactory.getFont(
					FontFactory.HELVETICA_BOLD,
					20);


	Paragraph bankName =
			new Paragraph(
					"STATE BANK OF INDIA",
					bankFont);


	bankName.setAlignment(
			Element.ALIGN_CENTER);


	document.add(bankName);


	document.add(
			new Paragraph("\n\n"));


	/*
	 * PRIVACY NOTICE
	 */
	Font titleFont =
			FontFactory.getFont(
					FontFactory.HELVETICA_BOLD,
					24);


	Paragraph title =
			new Paragraph(
					"PRIVACY NOTICE",
					titleFont);


	title.setAlignment(
			Element.ALIGN_CENTER);


	document.add(title);


	document.add(
			new Paragraph("\n\n"));


	/*
	 * Description
	 */
	Font normalFont =
			FontFactory.getFont(
					FontFactory.HELVETICA,
					12);


	Paragraph description =
			new Paragraph(
					"State Bank of India\n"
					+ "Official Privacy Notice",
					normalFont);


	description.setAlignment(
			Element.ALIGN_CENTER);


	document.add(description);


	document.add(
			new Paragraph("\n\n"));


	/*
	 * Language
	 */
	Paragraph language =
			new Paragraph(
					"Language: "
					+ getPrivacyLanguageName(locale),
					normalFont);


	language.setAlignment(
			Element.ALIGN_CENTER);


	document.add(language);
}
```

You'll need:

```java
import org.apache.struts2.ServletActionContext;
```

---

# 4. Don't hardcode `English`

Because this is multilingual, if the user selects:

```text
eng → English
hin → Hindi
mar → Marathi
```

the cover should reflect it.

Create:

```java
private String getPrivacyLanguageName(
		String locale) {

	try {

		List<MasterLanguage> languages =
				commonService.getAllActiveLanguages();

		if (languages != null) {

			for (MasterLanguage language
					: languages) {

				if (language.getLannguageCode() != null
						&& language.getLannguageCode()
							.equalsIgnoreCase(locale)) {

					return language.getLanguageName();
				}
			}
		}

	} catch (Exception e) {

		logger.error(
				"Unable to get Privacy language name",
				e);
	}

	return locale;
}
```

Add:

```java
import java.util.List;

import com.mintstreet.consent.entity.MasterLanguage;
```

You're reusing your existing:

```java
commonService.getAllActiveLanguages();
```

so no new DAO is required.

---

# 5. Add privacy content from page 2

Create another common method:

```java
private void addPrivacyNoticeContent(
		PdfWriter writer,
		Document document,
		String privacyNotice) throws Exception {


	/*
	 * Page heading
	 */
	Font headingFont =
			FontFactory.getFont(
					FontFactory.HELVETICA_BOLD,
					16);


	Paragraph heading =
			new Paragraph(
					"Privacy Notice",
					headingFont);


	heading.setAlignment(
			Element.ALIGN_CENTER);


	document.add(heading);


	document.add(
			new Paragraph("\n"));


	/*
	 * Privacy Notice HTML
	 * fetched from DB
	 */
	XMLWorkerHelper
			.getInstance()
			.parseXHtml(
					writer,
					document,
					new ByteArrayInputStream(
						privacyNotice.getBytes(
							StandardCharsets.UTF_8)),
					StandardCharsets.UTF_8);
}
```

Now the responsibilities are separated:

```text
downloadPrivacyNoticePdf()
        │
        ├── DB lookup
        ├── PDF initialization
        │
        ├── createPdfCoverPage()
        │          ↓
        │      Page 1
        │
        ├── document.newPage()
        │
        └── addPrivacyNoticeContent()
                   ↓
              Page 2 onwards
```

---

# 6. Frontend doesn't change

Your common AJAX remains exactly the same:

```javascript
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

		var blob = new Blob(
				[data],
				{
					type : "application/pdf"
				});

		var downloadUrl =
				window.URL.createObjectURL(blob);

		var link =
				document.createElement("a");

		link.href = downloadUrl;

		link.download =
				"SBI_Privacy_Notice_"
				+ selectedLocale
				+ ".pdf";

		document.body.appendChild(link);

		link.click();

		document.body.removeChild(link);

		window.URL.revokeObjectURL(
				downloadUrl);
	}
});
```

No Home Loan-specific parameter is necessary.

---

# 7. One important issue: Hindi/Marathi PDF

There is one thing you should address **before implementing this exact version**.

Using:

```java
FontFactory.HELVETICA
```

works for English but **does not support Devanagari properly**.

Your privacy system is multilingual. If your DB contains Hindi/Marathi:

```text
हिंदी
मराठी
```

you need a Unicode font capable of Devanagari rendering, and depending on your existing PDF library, proper complex-script shaping support.

Therefore, don't use Helvetica for the actual privacy content if Hindi/Marathi are required.

The best implementation should use an **approved Unicode font already packaged with your SBI application/server**, rather than adding a random font file.

Also, XMLWorker/iText 5 has limitations with complex-script HTML rendering. If multilingual Hindi/Marathi PDF output is mandatory, tell me what PDF libraries/JARs your project already has, and I'll tailor the implementation accordingly.

## Final files affected

For this new cover-page requirement, you still don't need separate loan changes.

```text
PrivacyConsentAction.java
        │
        ├── downloadPrivacyNoticePdf()
        ├── createPdfCoverPage()
        ├── addPrivacyNoticeContent()
        └── getPrivacyLanguageName()

ConsentPopup.jsp
        │
        └── Download PDF button

privacyConsent.js
        │
        └── Common AJAX

Common Struts XML
        │
        └── downloadPrivacyNoticePdf

web.xml
        │
        └── /downloadPrivacyNoticePdf
```

The **Home Loan, Auto Loan, CVE, Personal Loan, etc. do not require separate PDF backend implementations**. They all use the same selected locale → common action → common DB notice → branded PDF flow.
