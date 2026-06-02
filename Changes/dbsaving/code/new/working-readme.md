Use **Quote flow** because your first page **Get Loan Quote** saves `ApplicationFormHomeLoanQuote` in `processGetQuotes(...)`. That method receives `ApplicationFormHomeLoanQuote quote`, so hidden fields should bind into `quote`, not `appForm`. 

## 1) DB change

Run on quote table:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_NTB_ID VARCHAR2(100);
```

## 2) `ApplicationFormHomeLoanQuote.java`

Add fields near other quote columns:

```java
@Column(name="QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name="QUOTE_NTB_ID")
private String quoteNtbId;
```

Add getters/setters near existing getters/setters:

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

## 3) `HomeRightContent.jsp`

Inside form, below CSRF hidden field, add:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
```

## 4) `ConsentPopup.jsp`

Replace broken button with:

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity:0.6; cursor:not-allowed;">
	Accept
</button>
```

Remove old `savePrivacyConsent()` AJAX function.

Add this:

```javascript
function acceptPrivacyConsent(){

	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();

	if($.trim(mobile) == ""){
		alert("Please enter mobile number before accepting consent.");
		return false;
	}

	if($.trim(dob) == ""){
		alert("Please enter date of birth before accepting consent.");
		return false;
	}

	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = $.trim(dob).replace(/\D/g, "");

	var ntbId = cleanMobile + cleanDob + loanTypeId;

	$("#quotePrivacyConsentFlag").val("Y");
	$("#quoteNtbId").val(ntbId);

	$("#infoprovide")
		.prop("disabled", false)
		.prop("checked", true);

	if($("#infoprovideCBS").length > 0){
		$("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", true);
	}

	$("#consentHomeLoan").modal("hide");
}
```

## 5) `HomeFirstPageSession.jsp`

Inside existing bottom script, add at top:

```javascript
$(document).on("click", "#subtn", function(){

	if($("#quotePrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}

});
```

## 6) `HomeLoanAction.java`

Do **not** use this old AJAX method anymore:

```java
savePrivacyConsent()
generateHomeLoanNtbId(...)
```

Your current `savePrivacyConsent()` tries to save using `appSeqId`, but at first-page quote stage `appSeqId` may not exist, so it is wrong for this flow. 

## 7) `HomeProcessManagerImpl.java`

No manual setter needed if Struts binding works.

Because `processGetQuotes(...)` already receives:

```java
ApplicationFormHomeLoanQuote quote
```

and later uses/saves that quote in Get Quote flow. 

Final flow:

```text
Accept Privacy Notice
→ quotePrivacyConsentFlag = Y
→ quoteNtbId = mobile + dob + loanTypeId
→ Get Loan Quote
→ quote object binds automatically
→ RUPEEPOWER_OCAS_T_00255 saves consent values
```
------------------------------------------------------------------------------------------------------------------------------------

##  Other   step 

Add the backend change **inside `processGetQuotes(...)`**, not in OTP/document/delete methods.

In your uploaded code, `processGetQuotes(...)` receives:

```java
ApplicationFormHomeLoanQuote quote
```

and later reloads the saved quote using:

```java
quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(quote.getLoanQuoteId());
```

So add validation/setters **before BRE/callBRE area**, before this reload/save flow. 

## Exact place in `HomeProcessManagerImpl.java`

Find this block inside `processGetQuotes(...)`:

```java
quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
quote.setLoanQuoteNewVisitId(trackVisitId);
quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
```

## Add below it

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

## Final code should look like this

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

if (quote.getLoanQuoteProjectId() != null) {
	MasterProject masterProject = this.homeLoanService.findProjectById(quote.getLoanQuoteProjectId());
	if (masterProject != null)
		quote.setLoanQuoteBuilderName(masterProject.getProjectName());
}
```

That is the safest exact backend place. It ensures Get Quote cannot proceed unless hidden consent fields came from popup.
