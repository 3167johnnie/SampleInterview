Yes, now clear. Your current `ConsentPopup.jsp` still has **duplicate Accept logic**:

1. Button calls:

```html
onclick="acceptPrivacyConsent();"
```

2. Again you have:

```javascript
$(document).on("click", "#acceptConsentBtn", function() {
   ...
});
```

Remove the second one. Otherwise popup can close through duplicate logic and your hidden fields/flow becomes confusing.

Also your `infoprovide` checkbox is inside `HomeFirstPageSession.jsp`, and `Get Loan Quote` button is also there. 

Your quote entity already has `QUOTE_PRIVACY_CONSENT_FLAG` and `QUOTE_NTB_ID` fields with getter/setter, so entity side is mostly done.

---

# Final required changes

## 1. `ConsentPopup.jsp`

### Replace current Accept button

Your current button:

```jsp
<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="acceptPrivacyConsent();" style="opacity: 0.6; cursor: not-allowed;">Accept</button>
```

Replace with cleaner version:

```jsp
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity:0.6; cursor:not-allowed;">
	Accept
</button>
```

---

## 2. `ConsentPopup.jsp`

### Delete this complete duplicate click block

Remove fully:

```javascript
// for checkbox 
$(document).on("click", "#acceptConsentBtn", function() {
	if ($("#infoprovide").length > 0) {
		$("#infoprovide")
			.prop("disabled", false)
			.prop("checked", true);
	}
	if ($("#infoprovideCBS").length > 0) {
		$("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", true);
	}
	var consentModalEl = document.getElementById("consentHomeLoan");
	var consentModal = bootstrap.Modal.getInstance(consentModalEl);
	if (consentModal != null) {
		consentModal.hide();
	} else {
		$("#consentHomeLoan").modal("hide");
	}
});
```

You do **not** need it because `acceptPrivacyConsent()` will do all this.

---

## 3. `ConsentPopup.jsp`

### Replace `acceptPrivacyConsent()` with this final version

```javascript
function acceptPrivacyConsent(){

	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();

	if(mobile == null || $.trim(mobile) == ""){
		alert("Please enter mobile number before accepting consent.");
		return false;
	}

	if(dob == null || $.trim(dob) == ""){
		alert("Please enter date of birth before accepting consent.");
		return false;
	}

	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = $.trim(dob).replace(/\D/g, "");

	var ntbId = cleanMobile + cleanDob + loanTypeId;

	$("#quotePrivacyConsentFlag").val("Y");
	$("#quoteNtbId").val(ntbId);

	console.log("quotePrivacyConsentFlag : " + $("#quotePrivacyConsentFlag").val());
	console.log("quoteNtbId : " + $("#quoteNtbId").val());

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

	$("#consentHomeLoan").modal("hide");

	return false;
}
```

---

## 4. `HomeRightContent.jsp`

Hidden fields must be **inside** this form:

```jsp
<form name="homeloanform" id="homeloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded"
	style="display :<s:property value="%{showForm?'block':'none'}"/>;">
```

After this existing CSRF field:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
```

Add:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
```

Final:

```jsp
<form name="homeloanform" id="homeloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded"
	style="display :<s:property value="%{showForm?'block':'none'}"/>;">

	<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />

	<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
	<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />

	<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>
</form>
```

This is important because Struts will bind only fields submitted inside the form.

---

## 5. `HomeFirstPageSession.jsp`

Your checkbox and submit button are here. 

Find:

```jsp
<script type="text/javascript">
```

Immediately after it, add:

```javascript
$(document).on("click", "#subtn", function(){

	if($("#quotePrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}

	if($.trim($("#quoteNtbId").val()) == ""){
		alert("Invalid consent details. Please accept SBI Privacy Notice again.");
		return false;
	}

	return true;
});
```

Do **not** manually call OTP here. Existing Get Quote flow should continue and open OTP verification as per current project flow.

---

## 6. `ApplicationFormHomeLoanQuote.java`

You already added this correctly:

```java
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;
```

And getters/setters are present:

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
```

No more change needed here.

---

## 7. `HomeProcessManagerImpl.java`

Inside:

```java
public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormHomeLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId)
```

Find this block:

```java
quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
quote.setLoanQuoteNewVisitId(trackVisitId);
quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
```

Add below it:

```java
if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
	return loanScenarioBean;
}

if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
	return loanScenarioBean;
}
```

Final:

```java
quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
quote.setLoanQuoteNewVisitId(trackVisitId);
quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());

if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
	return loanScenarioBean;
}

if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
	return loanScenarioBean;
}
```

---

# 8. CCMS Write API after OTP verification

Do **not** call CCMS Write API on Accept.

Do **not** call CCMS Write API on Get Quote submit.

Call it only **after OTP is successfully verified**.

In `HomeProcessManagerImpl.java`, search for OTP success block where code does this:

```java
json.put("status", "success");
json.put("message", "OTP authentication successful");
```

Before that success response, add:

```java
try {
	ApplicationFormHomeLoanQuote consentQuote =
		this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(application.getAppQuoteId());

	if (consentQuote != null
			&& "Y".equalsIgnoreCase(consentQuote.getQuotePrivacyConsentFlag())
			&& ValidatorUtil.isValid(consentQuote.getQuoteNtbId())) {

		consentUtil.callCCMSConsentWriteAPI();
	}
} catch (Exception e) {
	logger.info("Exception while calling CCMS Consent Write API after OTP success", e);
}
```

Also add dependency at top of class:

```java
@Autowired
private ConsentUtil consentUtil;
```

And import:

```java
import com.mintstreet.common.util.ConsentUtil;
```

If your project already has another CCMS consent utility/service class name, use that instead of `ConsentUtil`.

---

# Final flow after these changes

```text
Customer clicks Read more
→ popup opens
→ language-wise privacy notice loads
→ customer scrolls to bottom
→ Accept button enables
→ Accept sets:
   quotePrivacyConsentFlag = Y
   quoteNtbId = mobile + dob + loanTypeId
   infoprovide checkbox checked
→ customer clicks Get Loan Quote
→ frontend validates hidden fields
→ existing Get Quote flow continues
→ existing OTP verification opens
→ after OTP success, backend calls CCMS Write API
```

Most important fix in your current code: **delete the duplicate `$(document).on("click", "#acceptConsentBtn"...` block from `ConsentPopup.jsp`.**
