Use **separate AutoLoan consent popup JSP** with unique IDs, so it will not conflict with HomeLoan.

Create this new file:

```text
/WebContent/appNew/common/AutoLoanConsentPopup.jsp
```

---

## 1. Create `AutoLoanConsentPopup.jsp`

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="autoLoanTermAndConditionHTML">

	<div class="modal fade otp-box" id="autoLoanConsentPopup" tabindex="-1"
		role="dialog" aria-labelledby="autoLoanConsentPopupLabel"
		data-bs-backdrop="static"
		data-bs-keyboard="false">

		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">

					<div class="privacy-consent-dropdown">
						<select id="autoLoanPrivacyLocaleDropdown"
							class="privacy-consent-dropdown"
							onchange="loadAutoLoanPrivacyByLocale();">

							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>

						</select>
					</div>

					<div id="autoLoanConsentDiv" class="privacy-consent-pop-content">
						Loading Privacy Notice...
					</div>

					<div style="margin-top: 15px; text-align: center;">
						<button type="button"
							id="autoLoanAcceptConsentBtn"
							class="btn btn-primary"
							disabled="disabled"
							onclick="acceptAutoLoanPrivacyConsent();"
							style="opacity: 0.6; cursor: not-allowed;">
							Accept
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>

<script>

	function canOpenAutoLoanPrivacyPopup() {
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

	function loadAutoLoanPrivacyByLocale(locale) {
		if (locale == null || locale == "") {
			locale = $("#autoLoanPrivacyLocaleDropdown").val();
		}

		$("#autoLoanAcceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		$("#infoprovide").prop("checked", false);
		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		$("#quotePrivacyLocale").val(locale);

		$.ajax({
			url : "autoLoanGetPrivacyNoticeByLocale",
			type : "POST",
			data : {
				privacyLocale : locale
			},
			success : function(response) {
				var json = typeof response === "string" ? JSON.parse(response) : response;

				if (json.status == "success") {
					$("#autoLoanConsentDiv").html(json.privacyNotice);
					$("#autoLoanConsentDiv").scrollTop(0);
					resetAutoLoanConsentScrollValidation();
				} else {
					$("#autoLoanConsentDiv").html("Privacy Notice Not Found");
				}
			},
			error : function(xhr) {
				console.log("autoLoanGetPrivacyNoticeByLocale failed : ", xhr.status, xhr.responseText);
				$("#autoLoanConsentDiv").html("Unable To Load Privacy Notice");
			}
		});
	}

	$(document).on("show.bs.modal", "#autoLoanConsentPopup", function(e) {
		if (!canOpenAutoLoanPrivacyPopup()) {
			e.preventDefault();
			return false;
		}
	});

	$(document).on("shown.bs.modal", "#autoLoanConsentPopup", function() {
		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		$("#quotePrivacyLocale").val("");
		$("#infoprovide").prop("checked", false);

		if ($("#autoLoanPrivacyLocaleDropdown option[value='eng']").length > 0) {
			$("#autoLoanPrivacyLocaleDropdown").val("eng");
			loadAutoLoanPrivacyByLocale("eng");
		} else {
			var firstLocale = $("#autoLoanPrivacyLocaleDropdown option:first").val();
			loadAutoLoanPrivacyByLocale(firstLocale);
		}
	});

	function resetAutoLoanConsentScrollValidation() {
		$("#autoLoanAcceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		$("#autoLoanConsentDiv").off("scroll").on("scroll", function() {
			var div = $(this)[0];

			if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
				$("#autoLoanAcceptConsentBtn").prop("disabled", false).css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
			}
		});

		var div = $("#autoLoanConsentDiv")[0];

		if (div && div.scrollHeight <= div.clientHeight + 5) {
			$("#autoLoanAcceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	}

	function acceptAutoLoanPrivacyConsent() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();

		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before accepting consent.");
			return false;
		}

		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before accepting consent.");
			return false;
		}

		var div = $("#autoLoanConsentDiv")[0];

		if (div && div.scrollTop + div.clientHeight < div.scrollHeight - 5) {
			alert("Please read the privacy notice till the end before accepting.");
			return false;
		}

		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");
		var ntbId = cleanMobile + cleanDob + autoLoanPrivacyLoanTypeId;

		$("#quotePrivacyConsentFlag").val("Y");
		$("#quoteNtbId").val(ntbId);
		$("#quotePrivacyLocale").val($("#autoLoanPrivacyLocaleDropdown").val());

		$("#infoprovide").prop("disabled", false).prop("checked", true);

		$.ajax({
			url : "saveAutoLoanPrivacyConsent",
			type : "POST",
			data : {
				privacyConsentFlag : "Y",
				ntbId : ntbId,
				privacyLocale : $("#autoLoanPrivacyLocaleDropdown").val()
			},
			success : function(response) {
				console.log("AutoLoan privacy consent save response : ", response);
			},
			error : function(xhr) {
				console.log("AutoLoan privacy consent save failed : ", xhr.status, xhr.responseText);
			}
		});

		$("#autoLoanConsentPopup").modal("hide");
	}

</script>
```

---

## 2. `AutoFirstPageSession.jsp`

Replace old AutoLoan checkbox block:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide"  value="on">

<label for="infoprovide" class="label-content consentScrollForAuto scollerClass" id="autoID">
	<s:property escapeHtml="false" value="%{beanList.consentAutoLoanNTB}" />
	&nbsp;<b class="req">*</b>
</label>
```

with:

```jsp
<input type="checkbox" class="blue-css-checkbox"
	name="infoprovide"
	id="infoprovide"
	value="on"
	disabled="disabled">

<label for="infoprovide" class="label-content" id="autoID">
	I hereby authorize State Bank of India and/or its representative to contact me with reference to my application.
	For more details please read
	<b>
		<a href="javascript:void(0);"
			onclick="javascript:openPopups('autoLoanConsentPopup','1');">
			SBI’s Privacy Notice
		</a>
	</b>
	<b class="req">*</b>
</label>
```

Remove old scrollbar script:

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

Add at bottom of `AutoFirstPageSession.jsp`:

```jsp
<script>
	var autoLoanPrivacyLoanTypeId = "<s:property value='%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ID}'/>";
</script>

<s:include value="/appNew/common/AutoLoanConsentPopup.jsp"></s:include>
```

---

## 3. `AutoRightContent.jsp`

Inside AutoLoan form, add hidden fields before including `AutoFirstPageSession.jsp`.

Find:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
<s:include value="/appNew/loan/autoloan/AutoFirstPageSession.jsp"></s:include>
```

Replace:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />

<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />

<s:include value="/appNew/loan/autoloan/AutoFirstPageSession.jsp"></s:include>
```

---

## 4. `AutoLoanAction.java`

### Add imports

```java
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
```

### Add fields

Near:

```java
private ApplicationFormAutoLoanQuote quote;
private ApplicationFormAutoLoan appForm;
```

add:

```java
private String privacyLocale;
private String privacyConsentFlag;
private String ntbId;
private List<MasterLanguage> languages;
```

---

## 5. Change content loading in `generateUIBeanList(Integer moduleId, int pageNo)`

Find existing AutoLoan NTB consent load:

```java
Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentId();
String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
beanList.setConsentAutoLoanNtb(consentTextNtb);
```

Replace:

```java
languages = commonService.getAllActiveLanguages();
logger.info("AutoLoan privacy dropdown language count : " + (languages == null ? 0 : languages.size()));

PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");

if (privacyObj != null) {
	beanList.setConsentAutoLoanNtb(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentAutoLoanNtb("Privacy Notice Not Available");
}
```

---

## 6. Add AutoLoan-specific privacy read method

In `AutoLoanAction.java`, add:

```java
public StreamResult autoLoanGetPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();

	try {
		if (privacyLocale == null || "".equals(privacyLocale)) {
			privacyLocale = "eng";
		}

		PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale(privacyLocale);

		if (privacyObj != null) {
			json.put("status", "success");
			json.put("privacyNotice", privacyObj.getPrivacyNotice());
		} else {
			json.put("status", "fail");
			json.put("privacyNotice", "Privacy Notice Not Found");
		}

	} catch (Exception e) {
		logger.info("Exception in AutoLoan autoLoanGetPrivacyNoticeByLocale", e);
		try {
			json.put("status", "fail");
			json.put("privacyNotice", "Unable To Load Privacy Notice");
		} catch (JSONException e1) {
			logger.info("JSONException in AutoLoan autoLoanGetPrivacyNoticeByLocale", e1);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

---

## 7. Add AutoLoan-specific save method

```java
public StreamResult saveAutoLoanPrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();

		if (appSeqId == null) {
			json.put("status", "fail");
			json.put("message", "AutoLoan application session not found.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		ApplicationFormAutoLoan application =
				autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);

		if (application == null) {
			json.put("status", "fail");
			json.put("message", "AutoLoan application not found.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		if (!"Y".equalsIgnoreCase(privacyConsentFlag)) {
			json.put("status", "fail");
			json.put("message", "Privacy consent not accepted.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		if (!ValidatorUtil.isValid(ntbId)) {
			json.put("status", "fail");
			json.put("message", "NTB ID is missing.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		application.setAppPrivacyConsentFlag("Y");
		application.setAppNtbId(ntbId);
		application.setAppPrivacyLocale(privacyLocale);

		autoLoanService.save(application);

		json.put("status", "success");
		json.put("message", "AutoLoan privacy consent saved successfully.");
		json.put("ntbId", ntbId);

	} catch (Exception e) {
		logger.info("Exception in saveAutoLoanPrivacyConsent", e);
		try {
			json.put("status", "fail");
			json.put("message", "Unable to save AutoLoan privacy consent.");
		} catch (JSONException je) {
			logger.info("JSONException in saveAutoLoanPrivacyConsent", je);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

---

## 8. Add getters/setters in `AutoLoanAction.java`

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

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

public List<MasterLanguage> getLanguages() {
	return languages;
}

public void setLanguages(List<MasterLanguage> languages) {
	this.languages = languages;
}
```

---

## 9. `struts-auto-loan-actions.xml`

Add:

```xml
<action name="autoLoanGetPrivacyNoticeByLocale" class="autoLoanAction"
	method="autoLoanGetPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

<action name="saveAutoLoanPrivacyConsent" class="autoLoanAction"
	method="saveAutoLoanPrivacyConsent">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

---

## 10. Entity changes for quote

`ApplicationFormAutoLoanQuote.java`

Add:

```java
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;

@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;
```

Getters/setters:

```java
public String getQuotePrivacyConsentFlag() {
	return quotePrivacyConsentFlag;
}

public void setQuotePrivacyConsentFlag(String quotePrivacyConsentFlag) {
	this.quotePrivacyConsentFlag = quotePrivacyConsentFlag;
}

public String getQuoteNtbId() {
	return quoteNtbId;
}

public void setQuoteNtbId(String quoteNtbId) {
	this.quoteNtbId = quoteNtbId;
}

public String getQuotePrivacyLocale() {
	return quotePrivacyLocale;
}

public void setQuotePrivacyLocale(String quotePrivacyLocale) {
	this.quotePrivacyLocale = quotePrivacyLocale;
}
```

---

## 11. Entity changes for application

`ApplicationFormAutoLoan.java`

Add:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

Getters/setters:

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

public String getAppPrivacyLocale() {
	return appPrivacyLocale;
}

public void setAppPrivacyLocale(String appPrivacyLocale) {
	this.appPrivacyLocale = appPrivacyLocale;
}
```

---

## 12. DB changes

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_NTB_ID VARCHAR2(100);
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);

ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_NTB_ID VARCHAR2(100);
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_PRIVACY_LOCALE VARCHAR2(20);
```

---

## Unique AutoLoan names used

```text
autoLoanConsentPopup
autoLoanConsentDiv
autoLoanPrivacyLocaleDropdown
autoLoanAcceptConsentBtn

canOpenAutoLoanPrivacyPopup()
loadAutoLoanPrivacyByLocale()
resetAutoLoanConsentScrollValidation()
acceptAutoLoanPrivacyConsent()

autoLoanGetPrivacyNoticeByLocale()
saveAutoLoanPrivacyConsent()
```

No conflict with HomeLoan `consentHomeLoan` popup.
