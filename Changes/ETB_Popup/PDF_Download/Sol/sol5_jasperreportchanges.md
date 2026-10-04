Yes. With ~40 Indian/regional languages, I would change the design slightly: **do not create 40 JRXML files and do not hard-code 40 fonts/languages in Java**.

Use **one common Jasper template**, make language/font metadata DB-driven, and make the PDF action common to every loan.

One caution: Indian scripts such as Devanagari, Bengali, Gujarati, Gurmukhi, Tamil, Telugu, Kannada, Malayalam, Odia, Urdu, etc. require different Unicode coverage and, for many scripts, correct text shaping. JasperReports + an appropriate PDF exporter/font setup can work, but you must test your exact Jasper version and PDF exporter with all required scripts. A Unicode font existing on the server alone is not sufficient if shaping isn't handled correctly.

## Recommended architecture

```text
                 ALL LOANS

 Home Loan ──────┐
 Auto Loan ──────┤
 CVE ────────────┤
 Personal Loan ──┼── ConsentPopup.jsp
 Other Loans ────┘         │
                            │
                   Selected locale
                       "mar"
                            │
                            ▼
              downloadPrivacyNoticePdf
                            │
                            ▼
                 PrivacyConsentAction
                            │
             ┌──────────────┴──────────────┐
             │                             │
             ▼                             ▼
   PrivacyRequestResponse            MasterLanguage
   privacyLocale = "mar"             code = "mar"
             │                             │
             │                      language/font info
             └──────────────┬──────────────┘
                            ▼
                 PrivacyPdfService
                            │
                            ▼
                  privacyNotice.jrxml
                     ONE TEMPLATE
                            │
                            ▼
                       Jasper PDF
```

### 1. Extend your existing language master

You already have `RUPEEPOWER_OCAS_T_13704` / `MasterLanguage`.

Instead of Java code like:

```java
if ("hin".equals(locale)) {
	font = "...";
} else if ("mar".equals(locale)) {
	font = "...";
}
```

store the PDF configuration with the language.

For example, conceptually:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13704
ADD PDF_FONT_FAMILY VARCHAR2(100);

ALTER TABLE RUPEEPOWER_OCAS_T_13704
ADD PDF_DIRECTION VARCHAR2(3);
```

Example data:

| Code | Language | PDF font family | Direction |
|---|---|---|---|
| eng | English | NotoSans | LTR |
| hin | Hindi | NotoSansDevanagari | LTR |
| mar | Marathi | NotoSansDevanagari | LTR |
| ben | Bengali | NotoSansBengali | LTR |
| guj | Gujarati | NotoSansGujarati | LTR |
| pan | Punjabi | NotoSansGurmukhi | LTR |
| tam | Tamil | NotoSansTamil | LTR |
| tel | Telugu | NotoSansTelugu | LTR |
| kan | Kannada | NotoSansKannada | LTR |
| mal | Malayalam | NotoSansMalayalam | LTR |
| ori | Odia | NotoSansOriya | LTR |
| urd | Urdu | suitable approved Arabic-script font | RTL |

The exact font names should correspond to fonts **approved and packaged by your application**, not arbitrary fonts assumed to exist on the OS.

If modifying the table isn't desirable, you can put this mapping in an application configuration file instead. I prefer configuration over a 40-case Java switch.

---

# 2. Update `MasterLanguage.java`

Add:

```java
@Column(name = "PDF_FONT_FAMILY")
private String pdfFontFamily;

@Column(name = "PDF_DIRECTION")
private String pdfDirection;
```

Getters/setters:

```java
public String getPdfFontFamily() {
	return pdfFontFamily;
}

public void setPdfFontFamily(String pdfFontFamily) {
	this.pdfFontFamily = pdfFontFamily;
}

public String getPdfDirection() {
	return pdfDirection;
}

public void setPdfDirection(String pdfDirection) {
	this.pdfDirection = pdfDirection;
}
```

Your existing:

```java
getAllActiveLanguages()
```

doesn't need to change because Hibernate will populate the new columns.

But add a direct locale lookup rather than retrieving all 40 languages every time.

---

# 3. Add locale lookup to `MasterLanguage`

Add another named query:

```java
@NamedQuery(
	name = "MasterLanguage.getActiveLanguageByCode",
	query = "SELECT l FROM MasterLanguage l "
		  + "WHERE l.isActive = 'Y' "
		  + "AND l.lannguageCode = :lannguageCode"
)
```

Then `MasterLanguageDao`:

```java
public MasterLanguage getActiveLanguageByCode(String languageCode) {

	Map<String, Object> params =
			new HashMap<String, Object>();

	params.put(
			"lannguageCode",
			languageCode);

	try {

		return (MasterLanguage) getSingleResult(
				"MasterLanguage.getActiveLanguageByCode",
				params);

	} catch (Exception e) {

		logger.error(
				"Unable to get language for code : "
				+ languageCode,
				e);

		return null;
	}
}
```

And `CommonService`:

```java
public MasterLanguage getActiveLanguageByCode(
		String languageCode) {

	return masterLanguageDao
			.getActiveLanguageByCode(languageCode);
}
```

---

# 4. Create ONE Jasper template

Create:

```text
/WEB-INF/reports/privacy/privacyNotice.jrxml
```

Don't create:

```text
privacyNoticeEnglish.jrxml
privacyNoticeHindi.jrxml
privacyNoticeMarathi.jrxml
...
```

One template handles everything.

Your parameters should be approximately:

```xml
<parameter name="SBI_LOGO" class="java.io.InputStream"/>

<parameter name="BANK_NAME" class="java.lang.String"/>

<parameter name="DOCUMENT_TITLE" class="java.lang.String"/>

<parameter name="LANGUAGE_NAME" class="java.lang.String"/>

<parameter name="PRIVACY_NOTICE" class="java.lang.String"/>

<parameter name="PDF_FONT" class="java.lang.String"/>
```

I'd keep `BANK_NAME` and `DOCUMENT_TITLE` parameters even though they're currently fixed. It keeps the template reusable.

---

# 5. Cover page

Page 1 should contain:

```text
                 [SBI LOGO]


             STATE BANK OF INDIA


                PRIVACY NOTICE


               Language: Marathi
```

Then page break.

For example, conceptually:

```xml
<image>
	<reportElement
		x="197"
		y="80"
		width="150"
		height="80"/>

	<imageExpression>
		<![CDATA[$P{SBI_LOGO}]]>
	</imageExpression>
</image>
```

Title:

```xml
<textField>
	<reportElement
		x="20"
		y="200"
		width="515"
		height="35"/>

	<textElement textAlignment="Center">
		<font
			fontName="NotoSans"
			size="20"
			isBold="true"/>
	</textElement>

	<textFieldExpression>
		<![CDATA[$P{BANK_NAME}]]>
	</textFieldExpression>
</textField>
```

And:

```xml
<textField>
	<reportElement
		x="20"
		y="260"
		width="515"
		height="40"/>

	<textElement textAlignment="Center">
		<font
			fontName="NotoSans"
			size="24"
			isBold="true"/>
	</textElement>

	<textFieldExpression>
		<![CDATA[$P{DOCUMENT_TITLE}]]>
	</textFieldExpression>
</textField>
```

---

# 6. Force privacy notice onto page 2

Don't rely on whitespace to push content to another page.

Use a Jasper page break between the cover and content.

Conceptually:

```xml
<break>
	<reportElement
		x="0"
		y="0"
		width="1"
		height="1"/>

	<breakExpression>
		<![CDATA[Boolean.TRUE]]>
	</breakExpression>
</break>
```

Depending on the Jasper version/template structure you're using, the exact placement will differ. The important requirement is a real Jasper `PageBreak`.

---

# 7. Privacy notice content must stretch

Your privacy notice could be 1 page or 30 pages.

The content field therefore must stretch/overflow rather than have a fixed-height clipping area.

Conceptually:

```xml
<textField
	isStretchWithOverflow="true">

	<reportElement
		x="20"
		y="20"
		width="515"
		height="100"/>

	<textElement markup="html">
		<font
			fontName="NotoSans"
			size="10"/>
	</textElement>

	<textFieldExpression>
		<![CDATA[$P{PRIVACY_NOTICE}]]>
	</textFieldExpression>

</textField>
```

But there is a major multilingual consideration here: **don't assume `markup="html"` plus a dynamic font name will correctly render every Indian script**. Test the actual stored markup and exporter.

---

# 8. Create `PrivacyPdfService.java`

Don't put all Jasper code inside the Struts action.

Create:

```text
com.mintstreet.consent.service.PrivacyPdfService
```

For example:

```java
public class PrivacyPdfService {

	private static final Logger logger =
			LogManager.getLogger(
					PrivacyPdfService.class);


	public byte[] generatePrivacyPdf(
			PrivacyRequestResponse privacy,
			MasterLanguage language,
			InputStream logoStream,
			InputStream reportStream)
			throws Exception {


		Map<String, Object> parameters =
				new HashMap<String, Object>();


		parameters.put(
				"SBI_LOGO",
				logoStream);

		parameters.put(
				"BANK_NAME",
				"STATE BANK OF INDIA");

		parameters.put(
				"DOCUMENT_TITLE",
				"PRIVACY NOTICE");

		parameters.put(
				"LANGUAGE_NAME",
				language.getLanguageName());

		parameters.put(
				"PRIVACY_NOTICE",
				privacy.getPrivacyNotice());

		parameters.put(
				"PDF_FONT",
				language.getPdfFontFamily());


		JasperReport jasperReport =
				JasperCompileManager
					.compileReport(
							reportStream);


		JasperPrint jasperPrint =
				JasperFillManager.fillReport(
						jasperReport,
						parameters,
						new JREmptyDataSource());


		return JasperExportManager
				.exportReportToPdf(
						jasperPrint);
	}
}
```

Imports:

```java
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
```

---

# 9. Don't compile JRXML on every download in production

This:

```java
JasperCompileManager.compileReport(reportStream);
```

is convenient during development but isn't what I'd use for a high-traffic production banking application.

Compile:

```text
privacyNotice.jrxml
```

into:

```text
privacyNotice.jasper
```

during build/deployment.

Then production does:

```java
JasperReport jasperReport =
	(JasperReport) JRLoader.loadObject(
		reportStream
	);
```

So production flow becomes:

```text
privacyNotice.jasper
        ↓
JRLoader
        ↓
JasperFillManager
        ↓
JasperExportManager
```

rather than compiling every request.

---

# 10. Common `PrivacyConsentAction`

Your action remains completely loan-independent:

```java
public StreamResult downloadPrivacyNoticePdf() {

	try {

		if (privacyLocale == null
				|| privacyLocale.trim().isEmpty()) {

			privacyLocale = "eng";
		}


		/*
		 * Validate locale against DB.
		 */
		MasterLanguage language =
				commonService
					.getActiveLanguageByCode(
							privacyLocale);


		if (language == null) {

			logger.warn(
					"Invalid Privacy locale : "
					+ privacyLocale);

			return null;
		}


		/*
		 * Get active Privacy Notice.
		 */
		PrivacyRequestResponse privacy =
				commonService
					.getPrivacyByLocale(
							privacyLocale);


		if (privacy == null
				|| privacy.getPrivacyNotice() == null) {

			logger.warn(
					"Privacy Notice unavailable for locale : "
					+ privacyLocale);

			return null;
		}


		InputStream logoStream =
				ServletActionContext
					.getServletContext()
					.getResourceAsStream(
						"/images/sbi-logo.png");


		InputStream reportStream =
				ServletActionContext
					.getServletContext()
					.getResourceAsStream(
						"/WEB-INF/reports/privacy/privacyNotice.jasper");


		if (reportStream == null) {

			logger.error(
					"Privacy Jasper template not found.");

			return null;
		}


		byte[] pdfBytes =
				privacyPdfService
					.generatePrivacyPdf(
							privacy,
							language,
							logoStream,
							reportStream);


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
				"Privacy PDF generation failed.",
				e);

		return null;
	}
}
```

---

# 11. Font handling is the critical part

For 40 languages, **do not rely on server-installed fonts**.

Your application should package approved fonts as resources.

For JasperReports, the better approach is a **Jasper font extension**.

Structure can look like:

```text
src/main/resources/
    jasperreports_extension.properties

    fonts/
        fonts.xml

        NotoSans-Regular.ttf
        NotoSans-Bold.ttf

        NotoSansDevanagari-Regular.ttf
        NotoSansDevanagari-Bold.ttf

        NotoSansBengali-Regular.ttf
        NotoSansGujarati-Regular.ttf
        NotoSansGurmukhi-Regular.ttf
        NotoSansTamil-Regular.ttf
        NotoSansTelugu-Regular.ttf
        NotoSansKannada-Regular.ttf
        NotoSansMalayalam-Regular.ttf
        NotoSansOriya-Regular.ttf

        ...
```

Do not package 40 fonts simply because there are 40 languages. Several languages share scripts.

For example:

```text
Hindi ─────┐
Marathi ───┼── Devanagari font
Konkani ───┘

Bengali ───┐
Assamese ──┴── Bengali/Assamese-compatible font

English ────── Latin font

Tamil ──────── Tamil font

Telugu ─────── Telugu font

Kannada ────── Kannada font

Malayalam ──── Malayalam font

Gujarati ───── Gujarati font

Punjabi ────── Gurmukhi font

Odia ───────── Odia font

Urdu ───────── Arabic-script/Urdu font
```

So think in terms of **scripts**, not number of languages.

---

# 12. Jasper font extension

`jasperreports_extension.properties`:

```properties
net.sf.jasperreports.extension.registry.factory.fonts=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory
net.sf.jasperreports.extension.simple.font.families.sbiFonts=fonts/fonts.xml
```

Then `fonts.xml` defines the approved families.

Example:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<fontFamilies>

	<fontFamily name="NotoSans">

		<normal>
			fonts/NotoSans-Regular.ttf
		</normal>

		<bold>
			fonts/NotoSans-Bold.ttf
		</bold>

		<pdfEncoding>
			Identity-H
		</pdfEncoding>

		<pdfEmbedded>
			true
		</pdfEmbedded>

	</fontFamily>


	<fontFamily name="NotoSansDevanagari">

		<normal>
			fonts/NotoSansDevanagari-Regular.ttf
		</normal>

		<bold>
			fonts/NotoSansDevanagari-Bold.ttf
		</bold>

		<pdfEncoding>
			Identity-H
		</pdfEncoding>

		<pdfEmbedded>
			true
		</pdfEmbedded>

	</fontFamily>

</fontFamilies>
```

Then add the other **required script families**, not a separate family per language.

---

# 13. Why `Identity-H` and embedding matter

For multilingual PDFs, you want:

```xml
<pdfEncoding>Identity-H</pdfEncoding>
<pdfEmbedded>true</pdfEmbedded>
```

Embedding means the recipient doesn't need the same font installed locally.

That's particularly important for a bank document that could be downloaded on:

```text
Windows
Android
iPhone
macOS
Linux
```

The PDF should look the same everywhere.

---

# 14. But don't assume this solves shaping

This is the most important warning.

A string like Hindi:

```text
भारतीय स्टेट बैंक
```

isn't simply a sequence of independently drawn Unicode glyphs. Indic scripts use shaping, combining marks, conjuncts, reordering, etc.

So before declaring support for all 40 languages, create an automated/manual certification matrix.

At minimum test:

```text
English
Hindi
Marathi
Bengali
Assamese
Gujarati
Punjabi/Gurmukhi
Odia
Tamil
Telugu
Kannada
Malayalam
Urdu
```

and every additional script represented by your 40 language records.

Verify:

```text
✓ Characters visible
✓ Matras positioned correctly
✓ Conjunct characters correct
✓ No boxes □□□
✓ No broken characters
✓ Line wrapping correct
✓ Bold works
✓ HTML formatting preserved
✓ Page breaks correct
✓ PDF opens in Adobe Reader
✓ Chrome PDF viewer
✓ Android
✓ iOS
✓ Printed output
```

If your current JasperReports/PDF exporter fails shaping for any required script, **don't try to fix it by adding more fonts**. That's an exporter/rendering issue, and we should select/configure a PDF rendering stack with proper complex-text shaping.

---

# 15. Don't trust locale coming from AJAX

Your frontend sends:

```javascript
privacyLocale : selectedLocale
```

but backend must validate it.

That's why I recommend:

```java
MasterLanguage language =
	commonService.getActiveLanguageByCode(privacyLocale);
```

before:

```java
commonService.getPrivacyByLocale(privacyLocale);
```

This prevents arbitrary client values being used for font/template selection.

---

# 16. Frontend remains extremely simple

All loans use the same:

```javascript
function downloadPrivacyNoticePdf() {

	var locale =
		$("#privacyLocaleDropdown").val();

	if (!locale) {
		locale = "eng";
	}

	$("#downloadPrivacyPdfBtn")
		.prop("disabled", true)
		.text("Downloading...");

	$.ajax({

		url : "downloadPrivacyNoticePdf",

		type : "POST",

		data : {
			privacyLocale : locale
		},

		xhrFields : {
			responseType : "blob"
		},

		success : function(data) {

			var blob =
				new Blob(
					[data],
					{
						type : "application/pdf"
					});

			var url =
				window.URL
					.createObjectURL(blob);

			var link =
				document.createElement("a");

			link.href = url;

			link.download =
				"SBI_Privacy_Notice_"
				+ locale
				+ ".pdf";

			document.body.appendChild(link);

			link.click();

			document.body.removeChild(link);

			window.URL.revokeObjectURL(url);
		},

		error : function() {

			showPrivacyError(
				"Unable to download Privacy Notice. Please try again."
			);
		},

		complete : function() {

			$("#downloadPrivacyPdfBtn")
				.prop("disabled", false)
				.text("Download PDF");
		}
	});
}
```

Notice it uses your inline UI error approach rather than a browser `alert()`.

---

## Final implementation structure

```text
DB
│
├── PrivacyRequestResponse
│      ├── eng → English HTML
│      ├── hin → Hindi HTML
│      ├── mar → Marathi HTML
│      ├── tam → Tamil HTML
│      └── ... ~40 languages
│
└── MasterLanguage
       ├── languageCode
       ├── languageName
       ├── PDF_FONT_FAMILY
       └── PDF_DIRECTION
                  │
                  ▼
            CommonService
                  │
                  ▼
         PrivacyConsentAction
                  │
                  ▼
          PrivacyPdfService
                  │
                  ▼
         privacyNotice.jasper
             ONE TEMPLATE
                  │
        ┌─────────┴──────────┐
        │                    │
     PAGE 1              PAGE 2+
        │                    │
     SBI Logo          Privacy Notice
     SBI Name          selected language
     Privacy Notice    DB content
     Language          auto page flow
        │                    │
        └─────────┬──────────┘
                  ▼
           Embedded Fonts
                  │
                  ▼
          Downloadable PDF
```

**This is the architecture I'd use for your project.** The one thing I would verify before writing the final JRXML is the exact **JasperReports version and PDF exporter dependencies already present in your SBI application**. That determines the correct solution for Indic shaping and whether Urdu/RTL needs additional exporter configuration.
