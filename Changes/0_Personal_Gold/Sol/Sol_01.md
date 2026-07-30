Gold Loan is already implemented inside the **Personal Loan module**:

```text
ApplicationFormPersonalLoanQuote
ApplicationFormPersonalLoan
PersonalLoanAction
PersonalProcessManagerImpl
PersonalLoanService
```

Gold Loan is identified using:

```java
Constants.APP_PL_TYPE_GOLD
```

and loan-purpose ID:

```java
27
```

The Gold Loan action still sets:

```java
loanTypeId = Constants.PERSONAL_LOAN_ID;
```

but for CCMS consent, use the **Gold Loan-specific ID**:

```java
Constants.APP_PL_TYPE_GOLD
```

The existing Gold Loan page uses `#dateOfBirth`, `#infoprovide`, and `#subtn`.  

Most backend consent fields are already available in your shared Personal Loan entities:

```java
QUOTE_PRIVACY_CONSENT_FLAG
QUOTE_NTB_ID
QUOTE_PRIVACY_LOCALE
QUOTE_CCMS_CONSENT_ID
```



And application already contains:

```java
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
APP_CCMS_CONSENT_ID
```



Therefore, Gold Loan does **not require new Gold-specific tables or entities**. It reuses the Personal Loan quote and application tables.

---

# 1. Database changes

Based on the supplied entities, verify these columns exist.

## Quote table

```sql
DESC SBI_TEST.RUPEEPOWER_OCAS_T_00440;
```

Required columns:

```sql
QUOTE_PRIVACY_CONSENT_FLAG
QUOTE_NTB_ID
QUOTE_PRIVACY_LOCALE
QUOTE_CCMS_CONSENT_ID
```

Add only missing columns:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00440
ADD QUOTE_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00440
ADD QUOTE_NTB_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00440
ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00440
ADD QUOTE_CCMS_CONSENT_ID VARCHAR2(255);
```

## Application table

```sql
DESC SBI_TEST.RUPEEPOWER_OCAS_T_00360;
```

Required columns:

```sql
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
APP_CCMS_CONSENT_ID
```

Add only missing columns:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00360
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00360
ADD APP_NTB_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00360
ADD APP_PRIVACY_LOCALE VARCHAR2(20);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00360
ADD APP_CCMS_CONSENT_ID VARCHAR2(255);
```

Add unique constraints on NTB ID:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00440
ADD CONSTRAINT UK_PL_QUOTE_NTB_ID
UNIQUE (QUOTE_NTB_ID);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00360
ADD CONSTRAINT UK_PL_APP_NTB_ID
UNIQUE (APP_NTB_ID);
```

The same NTB ID is stored once in the quote table and once in the application table. Separate unique constraints are valid.

---

# 2. Common NTB generator

Use the common method already discussed.

## `ConsentUtil.java`

Add imports:

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
```

Add formatter:

```java
private static final DateTimeFormatter NTB_TIMESTAMP_FORMATTER =
		DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
```

Add common method:

```java
public String generateNtbId(
		String mobileNumber,
		Integer loanTypeId) {

	if (!ValidatorUtil.isValid(mobileNumber)) {
		throw new IllegalArgumentException(
				"Mobile number is mandatory for NTB ID generation"
		);
	}

	if (loanTypeId == null) {
		throw new IllegalArgumentException(
				"Loan type ID is mandatory for NTB ID generation"
		);
	}

	String cleanMobile =
			mobileNumber.replaceAll("[^0-9]", "");

	if (!ValidatorUtil.isValid(cleanMobile)) {
		throw new IllegalArgumentException(
				"Valid mobile number is mandatory for NTB ID generation"
		);
	}

	String timestamp =
			LocalDateTime.now().format(
					NTB_TIMESTAMP_FORMATTER
			);

	return cleanMobile
			+ timestamp
			+ loanTypeId;
}
```

Gold Loan example:

```text
Mobile      : 9619155147
Timestamp   : 20260731014530123
Loan Type   : 27

NTB ID:
96191551472026073101453012327
```

---

# 3. Gold Loan popup JSP

Create:

```text
SBI/WebContent/appNew/loan/personal/ConsentPopupGoldLoan.jsp
```

Use this complete code:

```jsp
<%@ page language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="goldLoanPrivacyPopupWrapper">

	<div class="modal fade otp-box"
		id="consentGoldLoan"
		tabindex="-1"
		role="dialog"
		aria-labelledby="goldLoanPrivacyTitle"
		data-bs-backdrop="static"
		data-bs-keyboard="false">

		<div class="modal-dialog modal-lg" role="document">

			<div class="modal-content">

				<div class="privacy-modal-body">

					<div class="modal-header">

						<h4 class="modal-title"
							id="goldLoanPrivacyTitle">
							SBI Privacy Notice
						</h4>

					</div>

					<div class="privacy-consent-dropdown"
						style="margin-bottom:15px;">

						<label for="goldPrivacyLocaleDropdown">
							Select language
						</label>

						<select id="goldPrivacyLocaleDropdown"
							class="form-select privacy-consent-dropdown"
							onchange="loadGoldPrivacyByLocale(this.value);">

							<s:if test="%{languages != null && languages.size() > 0}">

								<s:iterator value="languages" var="lang">

									<option
										value="<s:property value="#lang.lannguageCode"/>"
										<s:if test="#lang.lannguageCode == 'eng'">
											selected="selected"
										</s:if>>

										<s:property value="#lang.languageName"/>

									</option>

								</s:iterator>

							</s:if>

							<s:else>

								<option value="eng"
									selected="selected">
									English
								</option>

							</s:else>

						</select>

					</div>

					<div id="goldConsentNoticeDiv"
						class="privacy-consent-pop-content"
						style="
							max-height:380px;
							overflow-y:auto;
							padding:15px;
							border:1px solid #dddddd;
							background:#ffffff;
						">

						Loading Privacy Notice...

					</div>

					<div id="goldPrivacyError"
						style="
							display:none;
							margin-top:10px;
							color:#b00020;
							text-align:center;
						">
					</div>

					<div style="
						margin-top:15px;
						text-align:center;
					">

						<button type="button"
							id="acceptGoldConsentBtn"
							class="btn btn-primary"
							disabled="disabled"
							onclick="acceptGoldPrivacyConsent();"
							style="
								opacity:0.6;
								cursor:not-allowed;
							">

							Accept

						</button>

					</div>

				</div>

			</div>

		</div>

	</div>

</div>

<script type="text/javascript">

(function($) {

	"use strict";

	var goldConsentScrollCompleted = false;

	function getGoldMobileNumber() {

		var mobile = "";

		/*
		 * ContactsDetails.jsp generally uses #mobile.
		 */
		if ($("#mobile").length > 0) {
			mobile = $("#mobile").val();
		}

		if ((mobile == null || $.trim(mobile) === "")
				&& $("#appMobileNo").length > 0) {

			mobile = $("#appMobileNo").val();
		}

		if ((mobile == null || $.trim(mobile) === "")
				&& $("input[name='quote.appMobile']").length > 0) {

			mobile =
				$("input[name='quote.appMobile']").val();
		}

		if ((mobile == null || $.trim(mobile) === "")
				&& $("input[name='appForm.appMobileNo']").length > 0) {

			mobile =
				$("input[name='appForm.appMobileNo']").val();
		}

		return mobile;
	}

	function getGoldDateOfBirth() {

		if ($("#dateOfBirth").length > 0) {
			return $("#dateOfBirth").val();
		}

		return "";
	}

	function resetGoldConsentState() {

		goldConsentScrollCompleted = false;

		$("#acceptGoldConsentBtn")
			.prop("disabled", true)
			.css({
				"opacity" : "0.6",
				"cursor" : "not-allowed"
			});

		$("#goldPrivacyError")
			.hide()
			.html("");

		$("#infoprovide")
			.prop("checked", false);

		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		$("#quotePrivacyLocale").val("");

		$("#goldConsentNoticeDiv").scrollTop(0);
	}

	window.canOpenGoldPrivacyPopup =
			function() {

		var mobile = getGoldMobileNumber();
		var dob = getGoldDateOfBirth();

		if (mobile == null
				|| $.trim(mobile) === "") {

			alert(
				"Please enter mobile number before viewing privacy notice."
			);

			$("#infoprovide")
				.prop("checked", false);

			return false;
		}

		var cleanMobile =
				$.trim(mobile).replace(/\D/g, "");

		if (cleanMobile.length !== 10) {

			alert(
				"Please enter a valid 10-digit mobile number before viewing privacy notice."
			);

			$("#infoprovide")
				.prop("checked", false);

			return false;
		}

		if (dob == null || $.trim(dob) === "") {

			alert(
				"Please enter date of birth before viewing privacy notice."
			);

			$("#infoprovide")
				.prop("checked", false);

			return false;
		}

		return true;
	};

	window.openGoldPrivacyPopup =
			function() {

		if (!canOpenGoldPrivacyPopup()) {
			return false;
		}

		resetGoldConsentState();

		$("#consentGoldLoan").modal("show");

		return false;
	};

	window.loadGoldPrivacyByLocale =
			function(locale) {

		if (locale == null
				|| $.trim(locale) === "") {

			locale =
				$("#goldPrivacyLocaleDropdown").val();
		}

		resetGoldConsentState();

		$("#goldConsentNoticeDiv").html(
			"Loading Privacy Notice..."
		);

		$.ajax({

			url : "getPrivacyNoticeByLocalePersonal",

			type : "POST",

			dataType : "json",

			data : {
				privacyLocale : locale
			},

			success : function(response) {

				if (response != null
					&& response.status === "success") {

					$("#goldConsentNoticeDiv")
						.html(response.privacyNotice);

					$("#goldConsentNoticeDiv")
						.scrollTop(0);

					bindGoldConsentScroll();

				} else {

					$("#goldConsentNoticeDiv")
						.html(
							"Privacy Notice Not Found"
						);

					$("#goldPrivacyError")
						.html(
							"Unable to load the selected privacy notice."
						)
						.show();
				}
			},

			error : function() {

				$("#goldConsentNoticeDiv")
					.html(
						"Unable to Load Privacy Notice"
					);

				$("#goldPrivacyError")
					.html(
						"Unable to load privacy notice. Please try again."
					)
					.show();
			}

		});
	};

	function bindGoldConsentScroll() {

		var $notice =
				$("#goldConsentNoticeDiv");

		$notice
			.off("scroll.goldConsent")
			.on(
				"scroll.goldConsent",
				function() {

					var notice = this;

					if (notice.scrollTop
							+ notice.clientHeight
							>= notice.scrollHeight - 5) {

						goldConsentScrollCompleted =
								true;

						$("#acceptGoldConsentBtn")
							.prop("disabled", false)
							.css({
								"opacity" : "1",
								"cursor" : "pointer"
							});
					}
				}
			);

		var notice = $notice[0];

		/*
		 * If notice content does not require scrolling,
		 * enable Accept directly.
		 */
		if (notice
			&& notice.scrollHeight
				<= notice.clientHeight + 5) {

			goldConsentScrollCompleted = true;

			$("#acceptGoldConsentBtn")
				.prop("disabled", false)
				.css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
		}
	}

	window.acceptGoldPrivacyConsent =
			function() {

		if (!canOpenGoldPrivacyPopup()) {
			return false;
		}

		var notice =
				$("#goldConsentNoticeDiv")[0];

		if (notice
			&& notice.scrollHeight
				> notice.clientHeight + 5
			&& !goldConsentScrollCompleted) {

			alert(
				"Please read the privacy notice till the end before accepting."
			);

			return false;
		}

		var locale =
				$("#goldPrivacyLocaleDropdown").val();

		if (locale == null
			|| $.trim(locale) === "") {

			alert(
				"Please select a privacy notice language."
			);

			return false;
		}

		/*
		 * NTB ID is generated securely in backend.
		 * Do not generate it in JavaScript.
		 */
		$("#quotePrivacyConsentFlag").val("Y");
		$("#quotePrivacyLocale").val(locale);
		$("#quoteNtbId").val("");

		$("#infoprovide")
			.prop("checked", true);

		$("#consentGoldLoan").modal("hide");

		return false;
	};

	$(document).on(
		"show.bs.modal",
		"#consentGoldLoan",
		function(event) {

			if (!canOpenGoldPrivacyPopup()) {

				event.preventDefault();
				return false;
			}

			return true;
		}
	);

	$(document).on(
		"shown.bs.modal",
		"#consentGoldLoan",
		function() {

			var locale = "eng";

			if ($("#goldPrivacyLocaleDropdown option[value='eng']")
					.length > 0) {

				$("#goldPrivacyLocaleDropdown")
					.val("eng");

			} else {

				locale =
					$("#goldPrivacyLocaleDropdown")
						.find("option:first")
						.val();

				$("#goldPrivacyLocaleDropdown")
					.val(locale);
			}

			loadGoldPrivacyByLocale(locale);
		}
	);

	/*
	 * If customer changes consent-dependent information
	 * after accepting, require consent again.
	 */
	$(document).on(
		"change input",
		"#mobile, #appMobileNo, #dateOfBirth",
		function() {

			if ($("#quotePrivacyConsentFlag").val()
					=== "Y") {

				resetGoldConsentState();
			}
		}
	);

})(jQuery);

</script>
```

Your existing Struts action already maps:

```text
getPrivacyNoticeByLocalePersonal
```

to `PersonalLoanAction.getPrivacyNoticeByLocalePersonal()`. 

Therefore, do not create another Gold-specific backend API.

---

# 4. Modify `Gold.jsp`

The main Gold page currently includes `GoldFirstPage.jsp`, followed by common popup/footer content. 

Find:

```jsp
<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
```

Add immediately before it:

```jsp
<s:include value="/appNew/loan/personal/ConsentPopupGoldLoan.jsp"></s:include>
```

Final section:

```jsp
<s:include value="/appNew/loan/personal/CommonContent.jsp"></s:include>

<s:include value="/appNew/loan/personal/ConsentPopupGoldLoan.jsp"></s:include>

<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>

<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
```

Do not add the popup inside an AJAX-refreshable form section. Add it once in `Gold.jsp`.

---

# 5. Modify `GoldFirstPageSession.jsp`

## Add hidden fields

Add before:

```jsp
<div id="termsAndConditionFirst" class="sbi-trms-div">
```

Paste:

```jsp
<s:hidden
	name="quote.quotePrivacyConsentFlag"
	id="quotePrivacyConsentFlag"/>

<s:hidden
	name="quote.quoteNtbId"
	id="quoteNtbId"/>

<s:hidden
	name="quote.quotePrivacyLocale"
	id="quotePrivacyLocale"/>
```

Because the entity fields are:

```java
quotePrivacyConsentFlag
quoteNtbId
quotePrivacyLocale
```

and not:

```java
loanQuotePrivacyConsentFlag
```

---

## Replace existing unchecked consent block

Existing block:

```jsp
<s:else>

	<div class="trms-section gold-conscent">

		<input type="checkbox"
			class="blue-css-checkbox"
			name="infoprovide"
			id="infoprovide"
			value="on">

		<label for="infoprovide"
			class="label-content">

			<s:property escapeHtml="false"
				value="%{beanList.consentGoldLoanNTB}" />

			&nbsp;<b class="req">*</b>

		</label>

	</div>

</s:else>
```

Replace with:

```jsp
<s:else>

	<div class="trms-section gold-conscent">

		<input type="checkbox"
			class="blue-css-checkbox"
			name="infoprovide"
			id="infoprovide"
			value="on"
			disabled="disabled">

		<label for="infoprovide"
			class="label-content">

			I/We certify that the information and
			particulars provided by me/us are true,
			correct, complete and up to date.

			<b>
				<a href="javascript:void(0);"
					onclick="return openGoldPrivacyPopup();">

					Read SBI Privacy Notice

				</a>
			</b>

			<b class="req">*</b>

		</label>

	</div>

</s:else>
```

The checkbox should not be manually selectable. It becomes checked only after popup acceptance.

---

## Existing verified-user block

Keep:

```jsp
<s:if test="%{appForm.appMobileVerified!=null
		&& appForm.appMobileVerified.equalsIgnoreCase('Y')}">

	<input type="checkbox"
		class="blue-css-checkbox"
		name="infoprovide"
		id="infoprovide"
		value="on"
		checked="checked"
		style="display:none">

</s:if>
```

However, also add the saved values for an already-created quote:

```jsp
<s:if test="%{appForm.appMobileVerified!=null
		&& appForm.appMobileVerified.equalsIgnoreCase('Y')}">

	<input type="checkbox"
		class="blue-css-checkbox"
		name="infoprovide"
		id="infoprovide"
		value="on"
		checked="checked"
		style="display:none">

	<script type="text/javascript">
		$(document).ready(function() {
			$("#quotePrivacyConsentFlag").val(
				"<s:property value='%{quote.quotePrivacyConsentFlag}'/>"
			);

			$("#quoteNtbId").val(
				"<s:property value='%{quote.quoteNtbId}'/>"
			);

			$("#quotePrivacyLocale").val(
				"<s:property value='%{quote.quotePrivacyLocale}'/>"
			);
		});
	</script>

</s:if>
```

---

# 6. Gold submit validation

Add at the bottom of `GoldFirstPageSession.jsp`:

```jsp
<script type="text/javascript">

$(document).on(
	"click.goldConsent",
	"#subtn",
	function(event) {

		var mobileVerified =
				"<s:property value='%{appForm.appMobileVerified}'/>";

		/*
		 * Existing verified application may already have consent.
		 */
		if (mobileVerified === "Y"
			&& $("#quotePrivacyConsentFlag").val() === "Y"
			&& $.trim($("#quoteNtbId").val()) !== "") {

			return true;
		}

		if ($("#quotePrivacyConsentFlag").val()
				!== "Y") {

			event.preventDefault();

			alert(
				"Please read and accept SBI Privacy Notice before proceeding."
			);

			return false;
		}

		if (!$("#infoprovide").is(":checked")) {

			event.preventDefault();

			alert(
				"Please accept SBI Privacy Notice before proceeding."
			);

			return false;
		}

		if ($.trim(
				$("#quotePrivacyLocale").val()
			) === "") {

			event.preventDefault();

			alert(
				"Privacy language details are missing. Please accept the privacy notice again."
			);

			return false;
		}

		/*
		 * Do not validate quoteNtbId here.
		 * NTB ID is generated in Java backend.
		 */
		return true;
	}
);

</script>
```

Do not require `quoteNtbId` for a new submission because it is generated in the backend.

---

# 7. `PersonalLoanAction.java`

The Gold Loan action currently sets:

```java
appPLTypeId = Constants.APP_PL_TYPE_GOLD;
goldType = 27;
loanTypeId = Constants.PERSONAL_LOAN_ID;
ajaxPostUrl = Constants.GOLD_LOAN_ACTION;
```



Keep these values because the Gold flow runs through the Personal Loan module.

The action already contains:

```java
private String privacyLocale;
private List<MasterLanguage> languages;
```

and imports `MasterLanguage`.  

## Load languages for Gold Loan NTB page

Inside `getPersonalLoan(...)`, in the state that loads Gold Loan first page, add:

```java
if (SessionUtil.getPersonalTypeId() != null
		&& SessionUtil.getPersonalTypeId()
				.equals(Constants.APP_PL_TYPE_GOLD)) {

	try {
		languages =
				commonService.getAllActiveLanguages();

		logger.info(
				"Gold Loan privacy language count : "
				+ (languages == null
					? 0
					: languages.size())
		);

		Integer privacyId =
				commonService.getPrivacyIdByLocale(
						"eng"
				);

		String privacyText = null;

		if (privacyId != null) {
			privacyText =
					commonService.getNClobdata(
							"RUPEEPOWER_OCAS_T_13703",
							"PRIVACY_NOTICE",
							"PRIVACY_ID",
							privacyId
					);
		}

		if (privacyText != null) {
			beanList.setConsentGoldLoanNtb(
					privacyText
			);
		} else {
			beanList.setConsentGoldLoanNtb(
					"Privacy Notice Not Available"
			);
		}

	} catch (Exception e) {

		logger.info(
				"Exception while loading Gold Loan privacy languages",
				e
		);

		beanList.setConsentGoldLoanNtb(
				"Privacy Notice Not Available"
		);
	}
}
```

Your action already loads language and privacy data in related Personal/Gold consent logic, so this is reuse rather than a new API. 

---

# 8. Do not create a Gold-specific privacy API

Keep the existing method:

```java
public StreamResult getPrivacyNoticeByLocalePersonal()
```

It already reads privacy text by locale from:

```text
RUPEEPOWER_OCAS_T_13703
```



The popup calls:

```javascript
url: "getPrivacyNoticeByLocalePersonal"
```

No additional Struts mapping is needed.

---

# 9. `PersonalLoanHelper.insertLoanQuote(...)`

Gold Loan uses the same quote table. Before saving quote, normalize frontend values.

Add after quote null-check and before validations:

```java
if (quote.getLoanQuoteLoanPurposeId() != null
		&& quote.getLoanQuoteLoanPurposeId()
				.equals(Constants.APP_PL_TYPE_GOLD)) {

	if ("Y".equalsIgnoreCase(
			quote.getQuotePrivacyConsentFlag())) {

		quote.setQuotePrivacyConsentFlag("Y");

	} else {

		quote.setQuotePrivacyConsentFlag("N");
	}

	if (!ValidatorUtil.isValid(
			quote.getQuotePrivacyLocale())) {

		quote.setQuotePrivacyLocale("eng");
	}
}
```

Do not generate NTB ID here unless the mobile number is guaranteed to be final and verified. The best location is the process manager just before CCMS Write.

---

# 10. `PersonalProcessManagerImpl.java`

This is the main backend change.

Your existing process already calls:

```java
writePrivacyConsentToCCMS(
	application,
	quote,
	loanScenarioBean,
	loanTypeId
);
```

after mobile verification. 

However, the currently passed `loanTypeId` may be:

```java
Constants.PERSONAL_LOAN_ID
```

even for Gold Loan.

Replace the existing block:

```java
logger.info(
		"application.getAppMobileVerified()...."
		+ application.getAppMobileVerified()
);

if (application.getAppMobileVerified() != null
		&& application.getAppMobileVerified()
				.equalsIgnoreCase("Y")) {

	logger.info("inside if cond");

	boolean ccmsWriteStatus =
			writePrivacyConsentToCCMS(
					application,
					quote,
					loanScenarioBean,
					loanTypeId
			);

	SessionUtil.setConsentSubmitNTBPersonal(
			"true"
	);

	if (!ccmsWriteStatus) {
		return loanScenarioBean;
	}
}
```

With:

```java
logger.info(
		"application.getAppMobileVerified()...."
		+ application.getAppMobileVerified()
);

if (application.getAppMobileVerified() != null
		&& application.getAppMobileVerified()
				.equalsIgnoreCase("Y")) {

	logger.info(
			"Calling common CCMS consent write flow"
	);

	Integer consentLoanTypeId = loanTypeId;

	/*
	 * Gold Loan uses loan-purpose ID 27.
	 * CCMS log and NTB ID must identify it as Gold Loan,
	 * not as generic Personal Loan.
	 */
	if (quote.getLoanQuoteLoanPurposeId() != null
			&& quote.getLoanQuoteLoanPurposeId()
					.equals(Constants.APP_PL_TYPE_GOLD)) {

		consentLoanTypeId =
				Constants.APP_PL_TYPE_GOLD;
	}

	boolean ccmsWriteStatus =
			writePrivacyConsentToCCMS(
					application,
					quote,
					loanScenarioBean,
					consentLoanTypeId
			);

	SessionUtil.setConsentSubmitNTBPersonal(
			"true"
	);

	if (!ccmsWriteStatus) {
		return loanScenarioBean;
	}
}
```

This is the key Gold-specific change.

---

# 11. Replace `writePrivacyConsentToCCMS(...)`

Your existing method already:

1. Validates consent
2. Calls common `ConsentUtil`
3. Receives CCMS consent ID
4. Saves quote and application
5. Sends SMS



Replace it with this corrected implementation:

```java
private boolean writePrivacyConsentToCCMS(
		ApplicationFormPersonalLoan application,
		ApplicationFormPersonalLoanQuote quote,
		LoanScenarioBean loanScenarioBean,
		Integer loanTypeId) {

	logger.info(
			"writePrivacyConsentToCCMS called. loanTypeId={}",
			loanTypeId
	);

	try {

		if (application == null || quote == null) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					Constants.SORRY_FOR_INCONVENIENCE
			);

			return false;
		}

		if (!"Y".equalsIgnoreCase(
				quote.getQuotePrivacyConsentFlag())) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Please read and accept SBI Privacy Notice before proceeding."
			);

			return false;
		}

		if (loanTypeId == null) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Unable to identify the loan type for consent."
			);

			return false;
		}

		if (!ValidatorUtil.isValid(
				quote.getQuotePrivacyLocale())) {

			quote.setQuotePrivacyLocale("eng");
		}

		String mobile =
				application.getAppMobileNo();

		String email =
				application.getAppWorkEmail();

		if (!ValidatorUtil.isValid(mobile)) {
			mobile = quote.getAppMobile();
		}

		if (!ValidatorUtil.isValid(email)) {
			email = quote.getAppEmail();
		}

		if (!ValidatorUtil.isValid(mobile)) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Mobile number is required for consent write."
			);

			return false;
		}

		/*
		 * Avoid duplicate CCMS Write on retries.
		 */
		if (ValidatorUtil.isValid(
				application.getAppCcmsConsentId())) {

			logger.info(
					"Skipping duplicate CCMS Write. "
					+ "Existing application consentId={}, appSeqId={}",
					application.getAppCcmsConsentId(),
					application.getAppSeqId()
			);

			if (!ValidatorUtil.isValid(
					quote.getQuoteCcmsConsentId())) {

				quote.setQuoteCcmsConsentId(
						application
								.getAppCcmsConsentId()
				);

				personalLoanService.save(quote);
			}

			return true;
		}

		if (ValidatorUtil.isValid(
				quote.getQuoteCcmsConsentId())) {

			logger.info(
					"Skipping duplicate CCMS Write. "
					+ "Existing quote consentId={}, quoteId={}",
					quote.getQuoteCcmsConsentId(),
					quote.getLoanQuoteId()
			);

			application.setAppCcmsConsentId(
					quote.getQuoteCcmsConsentId()
			);

			personalLoanService.save(application);

			return true;
		}

		/*
		 * Generate one backend NTB ID:
		 * Mobile + yyyyMMddHHmmssSSS + LoanTypeId
		 */
		String ntbId =
				consentUtil.generateNtbId(
						mobile,
						loanTypeId
				);

		quote.setQuoteNtbId(ntbId);
		application.setAppNtbId(ntbId);

		quote.setQuotePrivacyConsentFlag("Y");
		application.setAppPrivacyConsentFlag("Y");

		application.setAppPrivacyLocale(
				quote.getQuotePrivacyLocale()
		);

		/*
		 * Save NTB ID and consent metadata before external call.
		 */
		quote =
				personalLoanService.save(quote);

		application =
				personalLoanService.save(application);

		if (quote == null || application == null) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Unable to save consent details."
			);

			return false;
		}

		String rawIpAddress =
				this.SbiUtil.getIPAddress();

		String ipAddress =
				rawIpAddress != null
						? rawIpAddress.replace(",", "")
						: "";

		/*
		 * One common CCMS API call.
		 */
		ConsentWriteLog consentWrite =
				consentUtil.callCCMSConsentWriteAPI(
						ntbId,
						mobile,
						email,
						ipAddress,
						quote.getQuotePrivacyLocale(),
						loanTypeId
				);

		if (consentWrite == null
				|| !"true".equalsIgnoreCase(
						consentWrite.getResponseStatus())
				|| !"200".equalsIgnoreCase(
						consentWrite.getResponseCode())
				|| !ValidatorUtil.isValid(
						consentWrite.getConsentId())
				|| !"Y".equalsIgnoreCase(
						consentWrite.getIsActive())) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Unable to write consent to CCMS. Please try again."
			);

			return false;
		}

		String ccmsConsentId =
				consentWrite.getConsentId().trim();

		quote.setQuoteCcmsConsentId(
				ccmsConsentId
		);

		application.setAppCcmsConsentId(
				ccmsConsentId
		);

		quote =
				personalLoanService.save(quote);

		application =
				personalLoanService.save(application);

		if (quote == null || application == null) {

			loanScenarioBean.setStatus(
					Integer.valueOf(0)
			);

			loanScenarioBean.setMessage(
					"Consent was recorded, but application details could not be updated."
			);

			return false;
		}

		logger.info(
				"CCMS consent saved successfully. "
				+ "consentId={}, ntbId={}, quoteId={}, "
				+ "appSeqId={}, loanTypeId={}",
				ccmsConsentId,
				ntbId,
				quote.getLoanQuoteId(),
				application.getAppSeqId(),
				loanTypeId
		);

		sendPrivacyConsentSms(
				mobile,
				ntbId,
				loanTypeId
		);

		return true;

	} catch (IllegalArgumentException e) {

		logger.info(
				"Validation exception while calling CCMS Write API",
				e
		);

		loanScenarioBean.setStatus(
				Integer.valueOf(0)
		);

		loanScenarioBean.setMessage(
				"Unable to write consent to CCMS. Please try again."
		);

		return false;

	} catch (Exception e) {

		logger.info(
				"Exception while calling CCMS Write API",
				e
		);

		loanScenarioBean.setStatus(
				Integer.valueOf(0)
		);

		loanScenarioBean.setMessage(
				"Unable to write consent to CCMS. Please try again."
		);

		return false;
	}
}
```

---

# 12. Add common SMS helper in `PersonalProcessManagerImpl`

Add below `writePrivacyConsentToCCMS(...)`:

```java
private void sendPrivacyConsentSms(
		String mobile,
		String ntbId,
		Integer loanTypeId) {

	try {

		String msgBody =
				communicationManagerImpl.setEmailBody(
						25,
						0,
						Constants.MESSAGE_TYPE_SMS,
						0
				);

		if (!ValidatorUtil.isValid(msgBody)) {
			logger.info(
					"Consent SMS template not available"
			);
			return;
		}

		msgBody =
				SbiUtil.urlEncode(msgBody);

		String smsText =
				Constants.SMS_STRING_INDIAN;

		smsText =
				smsText.replaceAll(
						"MESSAGE_TEXT",
						msgBody
				);

		smsText =
				smsText.replaceAll(
						"MOBILE_CODE",
						"91" + mobile
				);

		String loanCode = "PL";

		if (Constants.APP_PL_TYPE_GOLD
				.equals(loanTypeId)) {

			loanCode = "GL";
		}

		smsText =
				smsText.replaceAll(
						"LOAN_TYPE",
						loanCode
				);

		smsText =
				smsText.replaceAll(
						"CONSENT_ID",
						ntbId
				);

		logger.info(
				"Sending privacy consent SMS. loanType={}",
				loanCode
		);

		communicationManagerImpl.sendSms(
				smsText
		);

	} catch (Exception e) {

		/*
		 * Do not fail consent write because SMS failed.
		 */
		logger.info(
				"Exception while sending consent SMS",
				e
		);
	}
}
```

This also corrects the existing hardcoded:

```java
LOAN_TYPE = PL
```

For Gold Loan it sends:

```text
GL
```

---

# 13. Copy quote consent values into application

Before calling `writePrivacyConsentToCCMS(...)`, or immediately after the application is created, add:

```java
if (quote != null && application != null) {

	application.setAppPrivacyConsentFlag(
			"Y".equalsIgnoreCase(
					quote.getQuotePrivacyConsentFlag())
					? "Y"
					: "N"
	);

	if (ValidatorUtil.isValid(
			quote.getQuotePrivacyLocale())) {

		application.setAppPrivacyLocale(
				quote.getQuotePrivacyLocale()
		);
	}
}
```

Do not copy frontend NTB ID. The backend creates the NTB ID inside the common write method.

---

# 14. Ensure Gold uses Gold loan type for CCMS

The required mapping is:

```java
private Integer resolveConsentLoanTypeId(
		ApplicationFormPersonalLoanQuote quote,
		Integer defaultLoanTypeId) {

	if (quote != null
			&& quote.getLoanQuoteLoanPurposeId() != null
			&& quote.getLoanQuoteLoanPurposeId()
					.equals(Constants.APP_PL_TYPE_GOLD)) {

		return Constants.APP_PL_TYPE_GOLD;
	}

	return defaultLoanTypeId;
}
```

Then the call can be simplified:

```java
Integer consentLoanTypeId =
		resolveConsentLoanTypeId(
				quote,
				loanTypeId
		);

boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean,
				consentLoanTypeId
		);
```

This avoids repeating the Gold check.

---

# 15. Files that do not need changes

No changes are required in:

```text
ApplicationFormPersonalLoanCalls.java
ApplicationFormPersonalLoanCallsDao.java
ApplicationFormPersonalLoanQuoteDao.java
```

`ApplicationFormPersonalLoanCalls` is only for call-history logging. It does not store consent data.

No new Gold-specific DAO is required because Gold Loan uses:

```text
ApplicationFormPersonalLoanDao
ApplicationFormPersonalLoanQuoteDao
PersonalLoanService
```

---

# 16. Final Gold Loan flow

```text
GoldFirstPageSession.jsp
        ↓
Customer enters mobile and DOB
        ↓
Read SBI Privacy Notice
        ↓
ConsentPopupGoldLoan.jsp
        ↓
Language-specific privacy notice
        ↓
Scroll to bottom
        ↓
Accept
        ↓
QUOTE_PRIVACY_CONSENT_FLAG = Y
QUOTE_PRIVACY_LOCALE = selected locale
        ↓
Submit Gold Loan
        ↓
PersonalProcessManagerImpl
        ↓
Detect loan-purpose ID 27
        ↓
Use consent loan type 27
        ↓
Generate backend NTB ID
mobile + yyyyMMddHHmmssSSS + 27
        ↓
Save NTB ID in quote and application
        ↓
Call same common CCMS Write API
        ↓
200 + success=true + consentId present
        ↓
Save CCMS consentId in:
QUOTE_CCMS_CONSENT_ID
APP_CCMS_CONSENT_ID
        ↓
Continue Gold Loan BRE/application flow
```

The existing CCMS method is already positioned before BRE, so Gold Loan can reuse it with only the corrected loan-type resolution and backend NTB generation. 
