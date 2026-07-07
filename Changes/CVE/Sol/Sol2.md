Below is **CVE-only ready-to-paste code**. It follows the Home Loan commit pattern, but does **not affect other products**.

CVE uses `CommonCbsCveNonConsent.jsp` inside `CommonCbs.jsp`, and the CVE form submit button is `#submitBtn`.  

---

## 1) DB changes

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_NTB_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_PRIVACY_LOCALE VARCHAR2(20);
```

---

## 2) `ApplicationFormCveLoan.java`

Add after:

```java
@Column(name="APP_CONSENT_ID")
private Integer loanQuoteConsentId;
```

Paste:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

Add at bottom before final `}`:

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

## 3) `MasterCBSCall.java`

Add import:

```java
import javax.persistence.Transient;
```

Add fields:

```java
@Transient
private String appPrivacyConsentFlag;

@Transient
private String appNtbId;

@Transient
private String appPrivacyLocale;
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

public String getAppPrivacyLocale() {
	return appPrivacyLocale;
}

public void setAppPrivacyLocale(String appPrivacyLocale) {
	this.appPrivacyLocale = appPrivacyLocale;
}
```

These are `@Transient`, so no CBS table change.

---

## 4) `CommonCbsCveNonConsent.jsp`

After this line:

```jsp
<input type="hidden" id="consentRevocation1" name="cbs.cveAppConsentRevokeYes" value="N">
```

Add:

```jsp
<s:hidden name="cbs.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="cbs.appNtbId" id="appNtbId" />
<s:hidden name="cbs.appPrivacyLocale" id="appPrivacyLocale" />
```

Replace this checkbox:

```jsp
<input type="checkbox" class="blue-css-checkbox colorCve" id="infoprovideCBScve" name="infoprovideCBS">
```

With:

```jsp
<input type="checkbox"
	class="blue-css-checkbox colorCve"
	id="infoprovideCBScve"
	name="infoprovideCBS"
	disabled="disabled">
```

Replace CVE consent label block:

```jsp
<label for="infoprovideCBScve" class="label-content" style="font-size: 10px;">
	<s:property escapeHtml="false" value="%{beanList.consentCveLoan}" />
	&nbsp;<b class="req">*</b>
</label>
```

With:

```jsp
<label for="infoprovideCBScve" class="label-content" style="font-size: 10px;">
	I/We certify that the information and particulars provided by me/us are true, correct,
	complete and up to date in all respects. I/We authorize State Bank of India to make
	inquiries related to or verify said information directly or through any third party.
	<b>
		<a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');">
			Read SBI Privacy Notice
		</a>
	</b>
	<b class="req">*</b>
</label>
```

At bottom of same JSP, add:

```jsp
<script type="text/javascript">
$(document).on("click", "#submitBtn", function(){

	if($("#consentRevocation1").val() == "Y"){
		return true;
	}

	if($("#appPrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}

	if($.trim($("#appNtbId").val()) == ""){
		alert("Invalid consent details. Please accept SBI Privacy Notice again.");
		return false;
	}

	if($.trim($("#appPrivacyLocale").val()) == ""){
		alert("Invalid privacy language details. Please accept SBI Privacy Notice again.");
		return false;
	}

	return true;
});
</script>
```

---

## 5) Full `ConsentPopup.jsp`

Replace full file:

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="termAndConditionHTML">
	<div class="modal fade otp-box"
		id="consentHomeLoan"
		tabindex="-1"
		role="dialog"
		aria-labelledby="myModalLabel"
		data-bs-backdrop="static"
		data-bs-keyboard="false">

		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">

					<div class="privacy-consent-dropdown">
						<select id="privacyLocaleDropdown"
							class="privacy-consent-dropdown"
							onchange="loadPrivacyByLocale();">

							<s:if test="%{languages != null && languages.size() > 0}">
								<s:iterator value="languages" var="lang">
									<option value="<s:property value="#lang.lannguageCode" />"
										<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
										<s:property value="#lang.languageName" />
									</option>
								</s:iterator>
							</s:if>
							<s:else>
								<option value="eng" selected="selected">English</option>
							</s:else>

						</select>
					</div>

					<div id="consentHomeLoanDiv"
						class="privacy-consent-pop-content">
						Loading Privacy Notice...
					</div>

					<div style="margin-top: 15px; text-align: center;">
						<button type="button"
							id="acceptConsentBtn"
							class="btn btn-primary"
							disabled="disabled"
							onclick="acceptPrivacyConsent();"
							style="opacity:0.6; cursor:not-allowed;">
							Accept
						</button>
					</div>

				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">

function getPrivacyMobileNumber(){

	var mobile = "";

	if($("#mobile").length > 0){
		mobile = $("#mobile").val();
	}

	if((mobile == null || $.trim(mobile) == "")
			&& $("#cbsMobileNumber").length > 0){
		mobile = $("#cbsMobileNumber").val();
	}

	if((mobile == null || $.trim(mobile) == "")
			&& $("#cbsMobileNumber1").length > 0){
		mobile = $("#cbsMobileNumber1").val();
	}

	return mobile;
}

function canOpenPrivacyPopup(){

	var mobile = getPrivacyMobileNumber();

	if(mobile == null || $.trim(mobile) == ""){
		alert("Please enter mobile number before viewing privacy notice.");

		if($("#infoprovide").length > 0){
			$("#infoprovide").prop("checked", false);
		}

		if($("#infoprovideCBS").length > 0){
			$("#infoprovideCBS").prop("checked", false);
		}

		if($("#infoprovideCBScve").length > 0){
			$("#infoprovideCBScve").prop("checked", false);
		}

		return false;
	}

	if($("#date_of_birth").length > 0){
		var dob = $("#date_of_birth").val();

		if(dob == null || $.trim(dob) == ""){
			alert("Please enter date of birth before viewing privacy notice.");

			if($("#infoprovide").length > 0){
				$("#infoprovide").prop("checked", false);
			}

			if($("#infoprovideCBS").length > 0){
				$("#infoprovideCBS").prop("checked", false);
			}

			return false;
		}
	}

	return true;
}

function clearPrivacyConsentValues(){

	$("#quotePrivacyConsentFlag").val("");
	$("#quoteNtbId").val("");
	$("#quotePrivacyLocale").val("");

	$("#appPrivacyConsentFlag").val("");
	$("#appNtbId").val("");
	$("#appPrivacyLocale").val("");

	if($("#infoprovide").length > 0){
		$("#infoprovide").prop("checked", false);
	}

	if($("#infoprovideCBS").length > 0){
		$("#infoprovideCBS").prop("checked", false);
	}

	if($("#infoprovideCBScve").length > 0){
		$("#infoprovideCBScve").prop("checked", false);
	}
}

function loadPrivacyByLocale(locale){

	if(locale == null || $.trim(locale) == ""){
		locale = $("#privacyLocaleDropdown").val();
	}

	$("#acceptConsentBtn").prop("disabled", true).css({
		"opacity" : "0.6",
		"cursor" : "not-allowed"
	});

	clearPrivacyConsentValues();

	$.ajax({
		url : "getPrivacyNoticeByLocale",
		type : "POST",
		data : {
			privacyLocale : locale
		},
		success : function(response){

			var json = typeof response === "string"
				? JSON.parse(response)
				: response;

			if(json.status == "success"){
				$("#consentHomeLoanDiv").html(json.privacyNotice);
				$("#consentHomeLoanDiv").scrollTop(0);
				resetConsentScrollValidation();
			}else{
				$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
			}
		},
		error : function(){
			$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
		}
	});
}

$(document).on("show.bs.modal", "#consentHomeLoan", function(e){
	if(!canOpenPrivacyPopup()){
		e.preventDefault();
		return false;
	}
});

$(document).on("shown.bs.modal", "#consentHomeLoan", function(){

	clearPrivacyConsentValues();

	if($("#privacyLocaleDropdown option[value='eng']").length > 0){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	}else{
		var firstLocale = $("#privacyLocaleDropdown option:first").val();
		loadPrivacyByLocale(firstLocale);
	}
});

function resetConsentScrollValidation(){

	$("#acceptConsentBtn").prop("disabled", true).css({
		"opacity" : "0.6",
		"cursor" : "not-allowed"
	});

	$("#consentHomeLoanDiv").off("scroll").on("scroll", function(){

		var div = $(this)[0];

		if(div.scrollTop + div.clientHeight >= div.scrollHeight - 5){
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	});

	var div = $("#consentHomeLoanDiv")[0];

	if(div && div.scrollHeight <= div.clientHeight + 5){
		$("#acceptConsentBtn").prop("disabled", false).css({
			"opacity" : "1",
			"cursor" : "pointer"
		});
	}
}

function acceptPrivacyConsent(){

	var mobile = getPrivacyMobileNumber();

	if(mobile == null || $.trim(mobile) == ""){
		alert("Please enter mobile number before accepting consent.");
		return false;
	}

	if($("#date_of_birth").length > 0){
		var dob = $("#date_of_birth").val();

		if(dob == null || $.trim(dob) == ""){
			alert("Please enter date of birth before accepting consent.");
			return false;
		}
	}

	var div = $("#consentHomeLoanDiv")[0];

	if(div && div.scrollTop + div.clientHeight < div.scrollHeight - 5){
		alert("Please read the privacy notice till the end before accepting.");
		return false;
	}

	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = "";

	if($("#date_of_birth").length > 0){
		cleanDob = $.trim($("#date_of_birth").val()).replace(/\D/g, "");
	}

	var loanTypeValue = "";

	if(typeof loanTypeId !== "undefined" && loanTypeId != null){
		loanTypeValue = loanTypeId;
	}

	var ntbId = cleanMobile + cleanDob + loanTypeValue;
	var selectedLocale = $("#privacyLocaleDropdown").val();

	/*
	 * Home Loan / quote based products
	 */
	if($("#quotePrivacyConsentFlag").length > 0){
		$("#quotePrivacyConsentFlag").val("Y");
	}

	if($("#quoteNtbId").length > 0){
		$("#quoteNtbId").val(ntbId);
	}

	if($("#quotePrivacyLocale").length > 0){
		$("#quotePrivacyLocale").val(selectedLocale);
	}

	/*
	 * CVE / app based products
	 */
	if($("#appPrivacyConsentFlag").length > 0){
		$("#appPrivacyConsentFlag").val("Y");
	}

	if($("#appNtbId").length > 0){
		$("#appNtbId").val(ntbId);
	}

	if($("#appPrivacyLocale").length > 0){
		$("#appPrivacyLocale").val(selectedLocale);
	}

	if($("#infoprovide").length > 0){
		$("#infoprovide")
			.prop("disabled", false)
			.prop("checked", true);
	}

	if($("#infoprovideCBS").length > 0){
		$("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", true);
	}

	if($("#infoprovideCBScve").length > 0){
		$("#infoprovideCBScve")
			.prop("disabled", false)
			.prop("checked", true);
	}

	$("#consentHomeLoan").modal("hide");

	return false;
}

</script>
```

---

## 6) Include popup on CVE page

In the main JSP that renders CVE page, add before footer scripts:

```jsp
<s:include value="/appNew/common/ConsentPopup.jsp"></s:include>
```

Only add if not already included.

---

## 7) `CveLoanAction.java`

Add imports:

```java
import java.io.ByteArrayInputStream;
import org.apache.struts2.result.StreamResult;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
```

Add fields near existing fields:

```java
private String privacyLocale;
private List<MasterLanguage> languages;
```

Inside CVE page load, before returning page, add:

```java
languages = commonService.getAllActiveLanguages();

PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
if (privacyObj != null) {
	beanList.setConsentCveLoan(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentCveLoan("Privacy Notice Not Available");
}
```

Add method:

```java
public StreamResult getPrivacyNoticeByLocale() {

	JSONObject json = new JSONObject();

	try {
		if (privacyLocale == null || "".equals(privacyLocale)) {
			privacyLocale = "eng";
		}

		PrivacyRequestResponse privacyObj =
				commonService.getPrivacyByLocale(privacyLocale);

		if (privacyObj != null) {
			json.put("status", "success");
			json.put("privacyNotice", privacyObj.getPrivacyNotice());
		} else {
			json.put("status", "fail");
			json.put("privacyNotice", "Privacy Notice Not Found");
		}

	} catch (Exception e) {
		logger.info("Exception in getPrivacyNoticeByLocale for CVE", e);

		try {
			json.put("status", "fail");
			json.put("privacyNotice", "Unable To Load Privacy Notice");
		} catch (JSONException je) {
			logger.info("JSONException in getPrivacyNoticeByLocale for CVE", je);
		}
	}

	return new StreamResult(
			new ByteArrayInputStream(json.toString().getBytes())
	);
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

---

## 8) Struts CVE action XML

Add:

```xml
<action name="getPrivacyNoticeByLocale"
	class="cveLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

If this action is already globally mapped to HomeLoanAction, do not duplicate with same namespace.

---

## 9) `CveProcessManagerImpl.java`

Add imports:

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.util.LoanConsentCcmsHelper;
```

Add autowired field:

```java
@Autowired
private LoanConsentCcmsHelper loanConsentCcmsHelper;
```

Find this block:

```java
cveApp.setCveAppConsentRevokeYes(masterCbsCall.getCveAppConsentRevokeYes());
logger.info("getCveAppConsentRevokeYes :: "+cveApp.getCveAppConsentRevokeYes()); 
```

Below it add:

```java
if ("Y".equalsIgnoreCase(masterCbsCall.getAppPrivacyConsentFlag())) {
	cveApp.setAppPrivacyConsentFlag("Y");
} else {
	cveApp.setAppPrivacyConsentFlag("N");
}

if (ValidatorUtil.isValid(masterCbsCall.getAppNtbId())) {
	cveApp.setAppNtbId(masterCbsCall.getAppNtbId());
}

if (ValidatorUtil.isValid(masterCbsCall.getAppPrivacyLocale())) {
	cveApp.setAppPrivacyLocale(masterCbsCall.getAppPrivacyLocale());
}
```

This is the correct area because CVE app fields are being copied from `masterCbsCall` into `cveApp` just before saving. 

Find save line:

```java
cveApp = personalLoanService.save(cveApp); // INSERT into CVE TABLE.
```

Immediately after it add:

```java
if ("N".equalsIgnoreCase(cveApp.getCveAppConsentRevokeYes())) {

	ConsentWriteInput consentInput = new ConsentWriteInput();

	consentInput.setNtbId(cveApp.getAppNtbId());
	consentInput.setMobile(cveApp.getCbsMobileNumber());
	consentInput.setEmail(cveApp.getCveAppEmail());
	consentInput.setIpAddress(this.SbiUtil.getIPAddress());
	consentInput.setLocale(cveApp.getAppPrivacyLocale());
	consentInput.setProductCode(cveApp.getCveProductCategory());
	consentInput.setConsentFlag(cveApp.getAppPrivacyConsentFlag());

	if (!loanConsentCcmsHelper.writeConsent(consentInput)) {
		cbsCallResponse.setResponseMsg("Unable to write consent to CCMS. Please try again.");
		cbsCallResponse.setStatus(0);
		return cbsCallResponse;
	}
}
```

This save location is confirmed in your CVE process manager. 

---

## 10) Common CCMS helper already required

If not already created, create:

```java
package com.mintstreet.consent.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;

public class LoanConsentCcmsHelper {

	private static final Logger logger =
			LogManager.getLogger(LoanConsentCcmsHelper.class.getName());

	@Autowired
	private ConsentService consentService;

	public boolean writeConsent(ConsentWriteInput input) {

		try {
			if (input == null) {
				logger.info("CCMS consent input is null");
				return false;
			}

			if (!ValidatorUtil.isValid(input.getNtbId())) {
				logger.info("CCMS consent ntbId is blank");
				return false;
			}

			if (!ValidatorUtil.isValid(input.getMobile())) {
				logger.info("CCMS consent mobile is blank");
				return false;
			}

			if (!ValidatorUtil.isValid(input.getLocale())) {
				input.setLocale("eng");
			}

			if (!ValidatorUtil.isValid(input.getProductCode())) {
				logger.info("CCMS consent product code is blank");
				return false;
			}

			ConsentWriteLog log =
					consentService.generateConsentWriteRequest(input);

			log = consentService.writeConsentToCCMS(log);

			return log != null
					&& "200".equalsIgnoreCase(log.getResponseCode())
					&& "true".equalsIgnoreCase(log.getResponseStatus());

		} catch (Exception e) {
			logger.info("Exception while writing consent to CCMS", e);
			return false;
		}
	}
}
```

Add Spring bean:

```xml
<bean id="loanConsentCcmsHelper"
	class="com.mintstreet.consent.util.LoanConsentCcmsHelper" />
```

---

That is the complete CVE implementation.
