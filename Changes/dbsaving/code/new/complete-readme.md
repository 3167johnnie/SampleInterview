Below are the **complete start-to-end changes** for your final flow:

```text
Privacy popup Accept
→ set hidden fields in quote object
→ Get Loan Quote submit
→ Struts binds to ApplicationFormHomeLoanQuote quote
→ processGetQuotes() validates consent
→ quote table saves consent
```

Your Get Quote flow uses `processGetQuotes(Integer appSeqId, ApplicationFormHomeLoanQuote quote, ...)`, so changes are on **quote**, not `appForm`. 

---

# 1) DB changes

Run this on quote table:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_NTB_ID VARCHAR2(100);
```

---

# 2) `ApplicationFormHomeLoanQuote.java`

## Add fields

File:

```text
/sbi/src/com/mintstreet/loan/homeloan/entity/ApplicationFormHomeLoanQuote.java
```

Add near other `@Column` fields:

```java
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;
```

## Add getters/setters

Add near other getter/setter methods:

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

---

# 3) `HomeRightContent.jsp`

File:

```text
/sbi/WebContent/appNew/loan/homeloan/HomeRightContent.jsp
```

Find inside form:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>
```

Replace with:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />

<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />

<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>
```

---

# 4) `ConsentPopup.jsp`

File:

```text
/sbi/WebContent/appNew/common/ConsentPopup.jsp
```

## Replace Accept button

Remove this broken button:

```html
<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="savePrivacyConsent(); style="opacity: 0.6; cursor: not-allowed;">Accept</button>
```

Add this:

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

## Remove old AJAX save function

Delete full function:

```javascript
function savePrivacyConsent(){
	$.ajax({
		url : "savePrivacyConsent",
		type : "POST",
		success : function(response){
			var json = JSON.parse(response);

			if(json.status == "success"){
				$("#consentHomeLoan").modal("hide");
			}else{
				alert(json.message);
			}
		},
		error : function(){
			alert("Unable to save consent. Please try again.");
		}
	});
}
```

## Add new function

Add at bottom of script:

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
}
```

Keep your existing:

```javascript
loadPrivacyByLocale()
resetConsentScrollValidation()
scroll validation
```

---

# 5) `HomeFirstPageSession.jsp`

File:

```text
/sbi/WebContent/appNew/loan/homeloan/HomeFirstPageSession.jsp
```

Your checkbox is already correct:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">
```

Your Get Quote button is:

```jsp
<input type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
```

At bottom, inside existing:

```jsp
<script type="text/javascript">
```

Add this first:

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

});
```

So script starts like:

```jsp
<script type="text/javascript">

	$(document).on("click", "#subtn", function(){

		if($("#quotePrivacyConsentFlag").val() != "Y"){
			alert("Please read and accept SBI Privacy Notice before proceeding.");
			return false;
		}

		if($.trim($("#quoteNtbId").val()) == ""){
			alert("Invalid consent details. Please accept SBI Privacy Notice again.");
			return false;
		}

	});

	function showCrossBtn() {
		...
	}
```

---

# 6) `HomeProcessManagerImpl.java`

File:

```text
/sbi/src/com/mintstreet/loan/homeloan/bo/impl/HomeProcessManagerImpl.java
```

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

if (quote.getLoanQuoteProjectId() != null) {
	MasterProject masterProject = this.homeLoanService.findProjectById(quote.getLoanQuoteProjectId());
	if (masterProject != null)
		quote.setLoanQuoteBuilderName(masterProject.getProjectName());
}
```

---

# 7) `HomeLoanAction.java`

No new action needed.

Do **not** call:

```java
savePrivacyConsent()
```

Do **not** add Struts mapping:

```xml
<action name="savePrivacyConsent" ...>
```

This flow saves consent with normal Get Quote submit.

---

## Final checklist

Change these files only:

```text
1. ApplicationFormHomeLoanQuote.java
2. HomeRightContent.jsp
3. ConsentPopup.jsp
4. HomeFirstPageSession.jsp
5. HomeProcessManagerImpl.java
```

No separate AJAX. No separate DAO. No separate service.
