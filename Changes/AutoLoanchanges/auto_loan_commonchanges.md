Below are **minimal reusable changes** for AutoLoan, using same privacy table/language flow from HomeLoan. AutoLoan first page currently has `infoprovide` checkbox and old `beanList.consentAutoLoanNTB` text, and form submit uses `submit_first_page_bind()` . AutoLoan backend already saves quote/app through `AutoLoanHelper.insertLoanQuote()` and `insertAppLoan()` .

## 1. DB change

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_NTB_ID VARCHAR2(100);
```

## 2. `ApplicationFormAutoLoan.java`

Add fields near `appSeqId`:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;
```

Add getters/setters:

```java
public String getAppPrivacyConsentFlag() {
	return appPrivacyConsentFlag;
}

public void setAppPrivacyConsentFlag(String appPrivacyConsentFlag) {
	this.appPrivacyConsentFlag = appPrivacyConsentFlag;
}

public String getAppNtbId() {
	return appNtbId;
}

public void setAppNtbId(String appNtbId) {
	this.appNtbId = appNtbId;
}
```

## 3. `ApplicationFormAutoLoanQuote.java`

Add transient fields:

```java
@Transient
private String privacyConsentFlag;

@Transient
private String ntbId;

@Transient
private String privacyLocale;
```

Add getters/setters:

```java
public String getPrivacyConsentFlag() {
	return privacyConsentFlag;
}

public void setPrivacyConsentFlag(String privacyConsentFlag) {
	this.privacyConsentFlag = privacyConsentFlag;
}

public String getNtbId() {
	return ntbId;
}

public void setNtbId(String ntbId) {
	this.ntbId = ntbId;
}

public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}
```

## 4. Common reusable JSP

Create:

`/WebContent/appNew/common/PrivacyConsentPopup.jsp`

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="privacyConsentContainer">
	<div class="modal fade otp-box" id="privacyConsentModal" tabindex="-1"
		role="dialog" data-bs-backdrop="static" data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">

					<div class="privacy-consent-dropdown">
						<select id="privacyLocaleDropdown" class="privacy-consent-dropdown"
							onchange="loadPrivacyByLocale();">
							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>
						</select>
					</div>

					<div id="privacyConsentDiv" class="privacy-consent-pop-content">
						Loading Privacy Notice...
					</div>

					<div style="margin-top:15px;text-align:center;">
						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary"
							disabled="disabled"
							onclick="acceptPrivacyConsent();"
							style="opacity:0.6;cursor:not-allowed;">
							Accept
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>
```

## 5. Common reusable JS

Create:

`/WebContent/appNew/common/PrivacyConsent.js`

```javascript
function resetPrivacyConsent() {
	$("#acceptConsentBtn").prop("disabled", true).css({
		"opacity": "0.6",
		"cursor": "not-allowed"
	});
}

function canOpenPrivacyPopup() {
	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();

	if (mobile == null || $.trim(mobile) == "") {
		alert("Please enter mobile number before viewing privacy notice.");
		$("#infoprovide").prop("checked", false);
		return false;
	}

	if (dob == null || $.trim(dob) == "") {
		alert("Please enter date of birth before viewing privacy notice.");
		$("#infoprovide").prop("checked", false);
		return false;
	}

	return true;
}

function loadPrivacyByLocale(locale) {
	if (locale == null) {
		locale = $("#privacyLocaleDropdown").val();
	}

	resetPrivacyConsent();
	$("#infoprovide").prop("checked", false);
	$("#quotePrivacyConsentFlag").val("");
	$("#quoteNtbId").val("");
	$("#quotePrivacyLocale").val(locale);

	$.ajax({
		url: "getPrivacyNoticeByLocale",
		type: "POST",
		data: {
			privacyLocale: locale
		},
		success: function(response) {
			var json = typeof response === "string" ? JSON.parse(response) : response;

			if (json.status == "success") {
				$("#privacyConsentDiv").html(json.privacyNotice);
				$("#privacyConsentDiv").scrollTop(0);
				resetConsentScrollValidation();
			} else {
				$("#privacyConsentDiv").html("Privacy Notice Not Found");
			}
		},
		error: function() {
			$("#privacyConsentDiv").html("Unable To Load Privacy Notice");
		}
	});
}

function resetConsentScrollValidation() {
	resetPrivacyConsent();

	setTimeout(function() {
		var div = $("#privacyConsentDiv")[0];

		if (div && div.scrollHeight <= div.clientHeight + 5) {
			enableAcceptConsent();
		}
	}, 300);
}

function enableAcceptConsent() {
	$("#acceptConsentBtn").prop("disabled", false).css({
		"opacity": "1",
		"cursor": "pointer"
	});
}

$(document).on("scroll", "#privacyConsentDiv", function() {
	var div = $(this)[0];

	if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
		enableAcceptConsent();
	}
});

$(document).on("show.bs.modal", "#privacyConsentModal", function(e) {
	if (!canOpenPrivacyPopup()) {
		e.preventDefault();
		return false;
	}
});

$(document).on("shown.bs.modal", "#privacyConsentModal", function() {
	$("#quotePrivacyConsentFlag").val("");
	$("#quoteNtbId").val("");
	$("#infoprovide").prop("checked", false);

	if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	} else {
		loadPrivacyByLocale($("#privacyLocaleDropdown option:first").val());
	}
});

function generateNtbIdForLoan(loanTypeId) {
	var mobile = $.trim($("#mobile").val());
	var dob = $.trim($("#date_of_birth").val()).replaceAll("/", "").replaceAll("-", "");
	return mobile + dob + loanTypeId;
}

function acceptPrivacyConsent() {
	var loanTypeId = $("#privacyLoanTypeId").val();
	var locale = $("#privacyLocaleDropdown").val();
	var ntbId = generateNtbIdForLoan(loanTypeId);

	$("#infoprovide").prop("checked", true);
	$("#quotePrivacyConsentFlag").val("Y");
	$("#quoteNtbId").val(ntbId);
	$("#quotePrivacyLocale").val(locale);

	$("#privacyConsentModal").modal("hide");
}

function validatePrivacyConsentBeforeSubmit() {
	if ($("#quotePrivacyConsentFlag").val() != "Y" || $("#quoteNtbId").val() == "") {
		alert("Please read and accept the privacy notice before submitting.");
		return false;
	}
	return true;
}
```

## 6. `AutoFirstPageSession.jsp`

Replace old terms block:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide"  value="on">

<label for="infoprovide" class="label-content consentScrollForAuto scollerClass" id="autoID">
	<s:property escapeHtml="false" value="%{beanList.consentAutoLoanNTB}" />
	&nbsp;<b class="req">*</b>
</label>
```

with this:

```jsp
<input type="checkbox" class="blue-css-checkbox"
	name="infoprovide" id="infoprovide" value="on"
	data-bs-toggle="modal" data-bs-target="#privacyConsentModal"
	onclick="return canOpenPrivacyPopup();">

<label for="infoprovide" class="label-content" id="autoID">
	I/We have read and accepted the Privacy Notice
	&nbsp;<b class="req">*</b>
</label>

<input type="hidden" id="privacyLoanTypeId" value="<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ID}"/>" />

<input type="hidden" name="quote.privacyConsentFlag" id="quotePrivacyConsentFlag" value="" />
<input type="hidden" name="quote.ntbId" id="quoteNtbId" value="" />
<input type="hidden" name="quote.privacyLocale" id="quotePrivacyLocale" value="" />
```

Remove this old scrollbar script:

```jsp
<script>
$(".consentScrollForAuto").mCustomScrollbar({
	theme:"rounded",
	scrollInertia:5,
	mouseWheel:{scrollAmount:30},
	scrollButtons:{
		enable:true
	}
});
</script>
```

Add common popup include after terms block:

```jsp
<s:include value="/appNew/common/PrivacyConsentPopup.jsp"></s:include>
<script src="<%=request.getContextPath()%>/appNew/common/PrivacyConsent.js"></script>
```

## 7. `AutoRightContent.jsp`

No major change. Your form already calls:

```jsp
onsubmit="return submit_first_page_bind();"
```

Keep it as-is. 

## 8. `AutoCommonScript.jsp`

At the **start** of `submit_first_page_bind()` add:

```javascript
if (!validatePrivacyConsentBeforeSubmit()) {
	return false;
}
```

## 9. `AutoLoanAction.java`

Add imports:

```java
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
```

Add fields:

```java
private String privacyLocale;
private List<MasterLanguage> languages;
```

In `getAutoLoan(...)`, before returning first page, load languages. Add near:

```java
populateFirstPageContent(Constants.AUTO_LOAN_ID,1);
```

add:

```java
loadPrivacyLanguages();
```

Add methods:

```java
private void loadPrivacyLanguages() {
	try {
		languages = commonService.getAllActiveLanguages();
		logger.info("Privacy language dropdown count : " + (languages == null ? 0 : languages.size()));
	} catch (Exception e) {
		logger.info("Exception in loadPrivacyLanguages", e);
	}
}

public StreamResult getPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();

	try {
		if (privacyLocale == null || privacyLocale.trim().isEmpty()) {
			privacyLocale = "eng";
		}

		PrivacyRequestResponse privacy =
				commonService.getPrivacyByLocale(privacyLocale, "Y");

		if (privacy != null && privacy.getPrivacyNotice() != null) {
			json.put("status", "success");
			json.put("privacyNotice", privacy.getPrivacyNotice());
		} else {
			json.put("status", "fail");
			json.put("message", "Privacy Notice Not Found");
		}
	} catch (Exception e) {
		logger.info("Exception in getPrivacyNoticeByLocale", e);
		try {
			json.put("status", "fail");
			json.put("message", "Unable to load privacy notice.");
		} catch (JSONException je) {
			logger.info("JSONException in getPrivacyNoticeByLocale", je);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

Add getters/setters:

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

public List<MasterLanguage> getLanguages() {
	return languages;
}

public void setLanguages(List<MasterLanguage> languages) {
	this.languages = languages;
}
```

## 10. `AutoLoanHelper.java`

Inside `insertAppLoan(...)`, just before:

```java
application = this.autoLoanService.save(application);
```

add:

```java
if ("Y".equalsIgnoreCase(quote.getPrivacyConsentFlag())) {
	application.setAppPrivacyConsentFlag("Y");
	application.setAppNtbId(quote.getNtbId());
}
```

Keep existing consent id logic as-is:

```java
if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
	application.setAppConsentId(quote.getLoanQuoteConsentId());
}
```

## 11. `AutoProcessManagerImpl.java`

Inside `processGetQuotes(...)`, after null quote check, add backend validation:

```java
if (!"Y".equalsIgnoreCase(quote.getPrivacyConsentFlag())
		|| quote.getNtbId() == null
		|| quote.getNtbId().trim().isEmpty()) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Please read and accept the privacy notice before submitting.");
	return loanScenarioBean;
}
```

This prevents bypass from browser/dev tools before `insertLoanQuote()` / `insertAppLoan()` flow. The AutoLoan process already has `processGetQuotes(...)` as the Get Quote backend entry point .

## 12. Struts mapping

Add action method mapping if not already common:

```xml
<action name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.autoloan.action.AutoLoanAction"
	method="getPrivacyNoticeByLocale">
	<result name="success" type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

Done. Minimal flow:

`AutoFirstPageSession.jsp → popup → hidden quote fields → submit_first_page_bind validation → processGetQuotes validation → AutoLoanHelper.insertAppLoan save in RUPEEPOWER_OCAS_T_00063`.









Yes, it can work like HomeLoan **if these 4 things match**:

1. AutoLoan page has same IDs:

```js
#mobile
#date_of_birth
#infoprovide
```

2. AutoLoan action loads:

```java
languages = commonService.getAllActiveLanguages();
```

3. AutoLoan action has AJAX method:

```java
getPrivacyNoticeByLocale()
```

4. AutoLoan submit posts hidden fields:

```jsp
quote.privacyConsentFlag
quote.ntbId
quote.privacyLocale
```

And yes, you can use the same popup for other loans also. Best reusable design:

```text
/appNew/common/PrivacyConsentPopup.jsp
/appNew/common/PrivacyConsent.js
```

Only change per loan:

```jsp
<input type="hidden" id="privacyLoanTypeId" value="AUTO_LOAN_ID / HOME_LOAN_ID / PERSONAL_LOAN_ID" />
<input type="hidden" name="quote.privacyConsentFlag" id="quotePrivacyConsentFlag" />
<input type="hidden" name="quote.ntbId" id="quoteNtbId" />
<input type="hidden" name="quote.privacyLocale" id="quotePrivacyLocale" />
```

For each loan backend, only save in its own app table:

```java
application.setAppPrivacyConsentFlag("Y");
application.setAppNtbId(quote.getNtbId());
```

Important: make popup id common like `privacyConsentModal`, not `consentHomeLoan`. Then same JSP works for AutoLoan, HomeLoan, PersonalLoan, etc.

