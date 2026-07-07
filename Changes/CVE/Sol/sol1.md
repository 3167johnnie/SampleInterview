Yes. For CVE, use **same consent popup**, but store values directly in:

```java
ApplicationFormCveLoan
RUPEEPOWER_OCAS_T_00291
```

because CVE has one page and no separate quote table.

## 1) DB change

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_NTB_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_PRIVACY_LOCALE VARCHAR2(20);
```

Do **not** add mobile/email/ip/product columns.

---

## 2) `ApplicationFormCveLoan.java`

Add after existing:

```java
@Column(name="APP_CONSENT_ID")
private Integer loanQuoteConsentId;
```

Add:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

Add getters/setters at bottom:

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

## 3) `CommonCbsCveNonConsent.jsp`

CVE form fields are in this file: mobile is `#cbsMobileNumber`, consent checkbox is `#infoprovideCBScve`, and submit button is `#submitBtn`.  

Add hidden fields near:

```jsp
<input type="hidden" id="consentRevocation1" name="cbs.cveAppConsentRevokeYes" value="N">
```

Add below it:

```jsp
<s:hidden name="cbs.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="cbs.appNtbId" id="appNtbId" />
<s:hidden name="cbs.appPrivacyLocale" id="appPrivacyLocale" />
```

Replace checkbox:

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

Replace label consent content:

```jsp
<label for="infoprovideCBScve" class="label-content" style="font-size: 10px;">
	<s:property escapeHtml="false" value="%{beanList.consentCveLoan}" />
	&nbsp;<b class="req">*</b>
</label>
```

With:

```jsp
<label for="infoprovideCBScve" class="label-content" style="font-size: 10px;">
	I/We certify that the information and particulars provided by me/us are true and correct.
	<b>
		<a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');">
			SBI Privacy Notice
		</a>
	</b>
	<b class="req">*</b>
</label>
```

Add submit validation at bottom:

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

## 4) `ConsentPopup.jsp`

Update `acceptPrivacyConsent()` so it works for Home Loan and CVE.

Replace mobile line:

```javascript
var mobile = $("#mobile").val();
```

With:

```javascript
var mobile = $("#mobile").val();

if((mobile == null || $.trim(mobile) == "") && $("#cbsMobileNumber").length > 0){
	mobile = $("#cbsMobileNumber").val();
}
```

Replace DOB requirement block with CVE-safe logic:

```javascript
var dob = $("#date_of_birth").val();

if((dob == null || $.trim(dob) == "") && $("#date_of_birth").length > 0){
	alert("Please enter date of birth before accepting consent.");
	return false;
}
```

Replace NTB generation:

```javascript
var cleanMobile = $.trim(mobile).replace(/\D/g, "");
var cleanDob = $.trim(dob).replace(/\D/g, "");
var ntbId = cleanMobile + cleanDob + loanTypeId;
```

With:

```javascript
var cleanMobile = $.trim(mobile).replace(/\D/g, "");
var cleanDob = "";

if($("#date_of_birth").length > 0){
	cleanDob = $.trim($("#date_of_birth").val()).replace(/\D/g, "");
}

var ntbId = cleanMobile + cleanDob + loanTypeId;
var selectedLocale = $("#privacyLocaleDropdown").val();
```

After Home Loan hidden fields:

```javascript
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

Add CVE hidden fields:

```javascript
$("#appPrivacyConsentFlag").val("Y");
$("#appNtbId").val(ntbId);
$("#appPrivacyLocale").val(selectedLocale);
```

Add CVE checkbox:

```javascript
if($("#infoprovideCBScve").length > 0){
	$("#infoprovideCBScve")
		.prop("disabled", false)
		.prop("checked", true);
}
```

Final important part:

```javascript
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);

$("#appPrivacyConsentFlag").val("Y");
$("#appNtbId").val(ntbId);
$("#appPrivacyLocale").val(selectedLocale);

if($("#infoprovide").length > 0){
	$("#infoprovide").prop("disabled", false).prop("checked", true);
}

if($("#infoprovideCBS").length > 0){
	$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
}

if($("#infoprovideCBScve").length > 0){
	$("#infoprovideCBScve").prop("disabled", false).prop("checked", true);
}
```

---

## 5) `CveProcessManagerImpl.java`

CVE creates/saves `ApplicationFormCveLoan` inside `processCbsCall(...)` after CBS response. The CVE app object is created/loaded around `cveApp = new ApplicationFormCveLoan()` / `getApplicationFormCveLoanByAppSeqId(...)`. 

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

Inside `processCbsCall(...)`, after this line:

```java
cveApp.setAppLeadUpdateTime(new Date());
```

Add:

```java
if (masterCbsCall.getInfoprovideCBS() != null
		&& "on".equalsIgnoreCase(masterCbsCall.getInfoprovideCBS())) {
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

If `MasterCBSCall` does not have these fields yet, add them there also because CVE form submits under `cbs.*`.

---

## 6) `MasterCBSCall.java`

Add transient or mapped fields depending on how your CBS object is used.

Safer minimal option:

```java
@Transient
private String appPrivacyConsentFlag;

@Transient
private String appNtbId;

@Transient
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

Then change hidden fields to bind to `cbs`:

```jsp
<s:hidden name="cbs.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="cbs.appNtbId" id="appNtbId" />
<s:hidden name="cbs.appPrivacyLocale" id="appPrivacyLocale" />
```

---

## 7) CCMS Write common call for CVE

After `cveApp` is saved successfully, call common CCMS helper.

Find save block where:

```java
cveApp = cveLoanService.save(cveApp);
```

or equivalent save call.

Immediately after successful save, add:

```java
if ("N".equalsIgnoreCase(cveApp.getCveAppConsentRevokeYes())) {

	ConsentWriteInput input = new ConsentWriteInput();
	input.setNtbId(cveApp.getAppNtbId());
	input.setMobile(cveApp.getCbsMobileNumber());
	input.setEmail(cveApp.getCveAppEmail());
	input.setIpAddress(this.SbiUtil.getIPAddress());
	input.setLocale(cveApp.getAppPrivacyLocale());
	input.setProductCode(cveApp.getCveProductCategory());
	input.setConsentFlag(cveApp.getAppPrivacyConsentFlag());

	if (!loanConsentCcmsHelper.writeConsent(input)) {
		cbsCallResponse.setResponseMsg("Unable to write consent to CCMS. Please try again.");
		cbsCallResponse.setStatus(0);
		return cbsCallResponse;
	}
}
```

For CVE product code, using `cveApp.getCveProductCategory()` is safer because CVE has multiple product categories from dropdown (`cveProductCategory`). The dropdown exists in `CommonCbsCveNonConsent.jsp`. 

---

## 8) `CveLoanAction.java`

Load languages just like Home Loan.

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

Inside page load before returning JSP, add:

```java
languages = commonService.getAllActiveLanguages();

PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
if (privacyObj != null) {
	beanList.setConsentCveLoan(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentCveLoan("Privacy Notice Not Available");
}
```

Add same two actions if CVE Struts maps separately:

```java
public StreamResult getPrivacyNoticeByLocale() {
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
		try {
			json.put("status", "fail");
			json.put("privacyNotice", "Unable To Load Privacy Notice");
		} catch (JSONException e1) {
			logger.info("JSONException in getPrivacyNoticeByLocale", e1);
		}
	}
	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

If Home Loan action mapping is globally available for `getPrivacyNoticeByLocale`, you can reuse it and skip CVE duplicate.

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

## Final CVE behavior

```text
CVE page opens
→ user enters CBS mobile/account/product details
→ user clicks SBI Privacy Notice
→ popup opens
→ user scrolls and accepts
→ appPrivacyConsentFlag=Y
→ appNtbId=mobile+loanTypeId
→ appPrivacyLocale=selected language
→ checkbox gets checked
→ Submit
→ CVE backend saves ApplicationFormCveLoan
→ Java calls common CCMS Write API
→ request/response saved in ConsentWriteLog
```
