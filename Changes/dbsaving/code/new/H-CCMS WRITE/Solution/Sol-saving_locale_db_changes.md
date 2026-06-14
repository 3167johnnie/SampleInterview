Add **language code** in same quote flow.

## 1) DB change

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);
```

---

## 2) `ApplicationFormHomeLoanQuote.java`

Add near your existing privacy fields:

```java
@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;
```

So final block becomes:

```java
//For Privacy Concent Ntb
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;

@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;
```

Your entity already has quote privacy fields in quote table entity. 

Add getter/setter:

```java
public String getQuotePrivacyLocale() {
	return quotePrivacyLocale;
}

public void setQuotePrivacyLocale(String quotePrivacyLocale) {
	this.quotePrivacyLocale = quotePrivacyLocale;
}
```

---

## 3) `HomeRightContent.jsp`

Inside form, where you added:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
```

Add one more:

```jsp
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
```

Final:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
```

---

## 4) `ConsentPopup.jsp`

In `shown.bs.modal`, clear this also:

```javascript
$("#quotePrivacyLocale").val("");
```

Final block:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
	$("#quotePrivacyConsentFlag").val("");
	$("#quoteNtbId").val("");
	$("#quotePrivacyLocale").val("");
```

Your dropdown already has language code as option value from `#lang.lannguageCode`. 

---

## 5) `ConsentPopup.jsp`

Inside `acceptPrivacyConsent()`, after NTB id:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

Final part:

```javascript
var cleanMobile = $.trim(mobile).replace(/\D/g, "");
var cleanDob = $.trim(dob).replace(/\D/g, "");
var ntbId = cleanMobile + cleanDob + loanTypeId;
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

---

## 6) `HomeFirstPageSession.jsp`

Add one more validation:

```javascript
if($.trim($("#quotePrivacyLocale").val()) == ""){
	alert("Invalid privacy language details. Please accept SBI Privacy Notice again.");
	return false;
}
```

Final submit validation:

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

	if($.trim($("#quotePrivacyLocale").val()) == ""){
		alert("Invalid privacy language details. Please accept SBI Privacy Notice again.");
		return false;
	}

	return true;
});
```

---

## 7) `HomeProcessManagerImpl.java`

After your existing backend validation:

```java
if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
	return loanScenarioBean;
}
```

Add:

```java
if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Invalid privacy language details. Please accept SBI Privacy Notice again.");
	return loanScenarioBean;
}
```

Done. This will store selected language code like `eng`, `hin`, `mar`, `mal` in `QUOTE_PRIVACY_LOCALE`.
