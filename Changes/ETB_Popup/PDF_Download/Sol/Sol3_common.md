Yes. Since Privacy Notice is common across Home Loan, Auto Loan, CVE, etc., don't put PDF generation in `HomeLoanAction`. Put it in a **common consent action** and let every loan call the same endpoint.

The clean design is:

```text
Home Loan ─────┐
Auto Loan ─────┤
CVE Loan ──────┼──> Common downloadPrivacyNoticePdf
Personal Loan ─┤             ↓
Other Loans ───┘       CommonService
                         ↓
                PrivacyRequestResponseDao
                         ↓
              Privacy Notice DB by locale
                         ↓
                    PDF response
```

The selected locale is enough. You don't need separate PDF actions for every loan.

## 1. Create common `PrivacyConsentAction.java`

Create:

```text
SBI/src/com/mintstreet/consent/action/PrivacyConsentAction.java
```

Use your common `BaseAction` and `CommonService`:

```java
package com.mintstreet.consent.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.dispatcher.StreamResult;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import com.mintstreet.common.action.BaseAction;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

public class PrivacyConsentAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private static final Logger logger =
			LogManager.getLogger(PrivacyConsentAction.class);

	private CommonService commonService;

	private String privacyLocale;


	public StreamResult downloadPrivacyNoticePdf() {

		ByteArrayOutputStream outputStream =
				new ByteArrayOutputStream();

		try {

			/* Default language */
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

			String privacyNotice =
					privacyObj.getPrivacyNotice();

			/*
			 * Generate PDF
			 */
			Document document =
					new Document(
							PageSize.A4,
							30,
							30,
							30,
							30);

			PdfWriter writer =
					PdfWriter.getInstance(
							document,
							outputStream);

			document.open();

			XMLWorkerHelper.getInstance()
					.parseXHtml(
							writer,
							document,
							new ByteArrayInputStream(
									privacyNotice.getBytes(
											StandardCharsets.UTF_8)),
							StandardCharsets.UTF_8);

			document.close();

			byte[] pdfBytes =
					outputStream.toByteArray();

			logger.info(
					"Privacy Notice PDF generated successfully. Locale : "
					+ privacyLocale
					+ ", size : "
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
				outputStream.close();
			} catch (Exception e) {
				logger.error(
						"Exception while closing PDF stream",
						e);
			}
		}
	}


	public String getPrivacyLocale() {
		return privacyLocale;
	}

	public void setPrivacyLocale(
			String privacyLocale) {

		this.privacyLocale = privacyLocale;
	}


	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(
			CommonService commonService) {

		this.commonService = commonService;
	}
}
```

Now this class has **zero Home Loan dependency**.

---

# 2. `applicationContext.xml`

Register one common bean.

Add:

```xml
<bean id="privacyConsentAction"
	class="com.mintstreet.consent.action.PrivacyConsentAction">

	<property name="commonService"
		ref="commonService" />

</bean>
```

However, check how your existing Struts action beans are created. If Spring already performs autowiring/injection into actions, follow that existing pattern rather than duplicating configuration.

The important part is that:

```java
commonService
```

must be available to `PrivacyConsentAction`.

---

# 3. Put action in common Struts XML

Don't put this in:

```text
struts-home-loan-actions.xml
```

because then the supposedly common functionality remains under Home Loan configuration.

If you already have something like:

```text
struts-common-actions.xml
```

add it there.

For example:

```xml
<action name="downloadPrivacyNoticePdf"
	class="privacyConsentAction"
	method="downloadPrivacyNoticePdf">

	<result type="stream">
		<param name="contentType">
			application/pdf
		</param>
	</result>

</action>
```

Then every loan calls:

```text
downloadPrivacyNoticePdf
```

There is only one endpoint.

---

# 4. `web.xml`

Only one common URL is required:

```xml
<url-pattern>/downloadPrivacyNoticePdf</url-pattern>
```

You don't need:

```text
/downloadHomeLoanPrivacyPdf
/downloadAutoLoanPrivacyPdf
/downloadCvePrivacyPdf
```

Keep one:

```text
/downloadPrivacyNoticePdf
```

---

# 5. Make JavaScript common too

This is another important improvement.

Don't copy this function into:

```text
HomeLoan ConsentPopup.jsp
AutoLoan ConsentPopup.jsp
CVE ConsentPopup.jsp
```

Create a common JS function.

For example, if you already have a common JS file for loan flows, add it there.

Otherwise:

```text
JS/ocas/sbiNew/js/privacyConsent.js
```

Add:

```javascript
function downloadPrivacyNoticePdf() {

	var selectedLocale =
			$("#privacyLocaleDropdown").val();

	if (selectedLocale == null
			|| $.trim(selectedLocale) == "") {

		selectedLocale = "eng";
	}

	var downloadBtn =
			$("#downloadPrivacyPdfBtn");

	downloadBtn
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

			var contentType =
					xhr.getResponseHeader(
							"Content-Type");

			if (contentType == null
					|| contentType.indexOf(
							"application/pdf") === -1) {

				alert(
					"Unable to download Privacy Notice.");

				return;
			}

			var blob =
					new Blob(
							[data],
							{
								type : "application/pdf"
							});

			var downloadUrl =
					window.URL.createObjectURL(
							blob);

			var link =
					document.createElement("a");

			link.href = downloadUrl;

			link.download =
					"SBI_Privacy_Notice_"
					+ selectedLocale
					+ ".pdf";

			document.body.appendChild(
					link);

			link.click();

			document.body.removeChild(
					link);

			window.URL.revokeObjectURL(
					downloadUrl);
		},

		error : function(xhr) {

			console.log(
					"Privacy PDF download failed : ",
					xhr.status);

			alert(
					"Unable to download Privacy Notice. Please try again.");
		},

		complete : function() {

			downloadBtn
				.prop("disabled", false)
				.text("Download PDF");
		}
	});
}
```

Then include this common JS wherever Privacy Consent is used.

---

# 6. Make Download button common

In your common `ConsentPopup.jsp`, you only need:

```jsp
<button type="button"
	id="downloadPrivacyPdfBtn"
	class="btn btn-primary"
	onclick="downloadPrivacyNoticePdf();">
	Download PDF
</button>
```

Since you're already trying to make Privacy Consent reusable across loans, ideally the **same `ConsentPopup.jsp`** should be included by Home Loan, Auto Loan, CVE, etc.

---

# 7. Backend doesn't need loan type

Notice we're only sending:

```javascript
data : {
	privacyLocale : selectedLocale
}
```

We're deliberately **not sending**:

```javascript
loanType : "HL"
```

because your current Privacy Notice retrieval is:

```java
commonService.getPrivacyByLocale(privacyLocale);
```

and your query is based on:

```java
privacyLocale
privacyIsActive = "Y"
```

So if the Privacy Notice is genuinely common for all products, loan type is unnecessary.

If later CCMS stores different privacy notices per product, then you can extend the common method to:

```java
getPrivacyByLocale(locale, loanType)
```

without creating separate actions.

---

# 8. You can make the existing privacy AJAX common too

Right now you have Home Loan-specific:

```java
HomeLoanAction.getPrivacyNoticeByLocale()
```

But that method actually contains **nothing Home Loan-specific**:

```java
PrivacyRequestResponse privacyObj =
	commonService.getPrivacyByLocale(privacyLocale);
```

Therefore, I'd move these three methods:

```java
getPrivacyNoticeByLocale()

getPrivacyLanguageList()

downloadPrivacyNoticePdf()
```

into the same:

```java
PrivacyConsentAction
```

Then your common action becomes responsible for:

```text
PrivacyConsentAction
       │
       ├── getPrivacyLanguageList()
       │
       ├── getPrivacyNoticeByLocale()
       │
       └── downloadPrivacyNoticePdf()
```

That's cleaner than leaving the first two in `HomeLoanAction` and only making PDF common.

---

## Final structure I recommend

```text
                        FRONT END

                Common ConsentPopup.jsp
                         │
          ┌──────────────┼──────────────┐
          │              │              │
      Home Loan      Auto Loan        CVE
          │              │              │
          └──────────────┼──────────────┘
                         │
                privacyConsent.js
                         │
        ┌────────────────┼─────────────────┐
        │                │                 │
 Language List      Load Notice       Download PDF
        │                │                 │
        └────────────────┼─────────────────┘
                         ↓

                       BACKEND

               PrivacyConsentAction
                         │
       ┌─────────────────┼─────────────────┐
       │                 │                 │
getPrivacyLanguage  getPrivacyNotice  downloadPrivacyPDF
       │                 │                 │
       └─────────────────┼─────────────────┘
                         ↓
                    CommonService
                         ↓
              PrivacyRequestResponseDao
                         ↓
                        DB
```

### Files changed

| File | Change |
|---|---|
| `PrivacyConsentAction.java` | **New common action** |
| Common Struts actions XML | Add 3 common endpoints |
| `applicationContext.xml` | Register/inject common action if required by your project's Spring pattern |
| `web.xml` | Add common PDF URL if your filter requires explicit mappings |
| `ConsentPopup.jsp` | Download button only |
| `privacyConsent.js` | Common AJAX/download logic |
| `HomeLoanAction.java` | Eventually remove privacy methods after migration |
| DAO | **No change** |
| Entity | **No change** |
| DB | **No change** |
| Home Loan service | **No change** |
| Auto Loan service | **No change** |
| CVE service | **No change** |

I would actually move **language list + notice retrieval + PDF download together now**, rather than introducing another Home Loan-specific implementation that you later have to refactor.
