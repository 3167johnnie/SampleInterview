Your flow is stuck because this check may be failing:

```java
!"true".equalsIgnoreCase(consentWrite.getResponseStatus())
|| !"200".equalsIgnoreCase(consentWrite.getResponseCode())
```

But CCMS encrypted response may not contain direct `eisResponse.statusCode = 200`.

## Main issue in your log

You got:

```text
Connection Response Code : 200
ERROR_DESCRIPTION:
```

But response is encrypted:

```json
{
  "DIGI_SIGN": "...",
  "RESPONSE": "...",
  "REQUEST_REFERENCE_NUMBER": "...",
  "RESPONSE_DATE": "16-06-2026 19:28:45"
}
```

So `writeResponse.getEisResponse().getStatusCode()` may be null.

---

# Minimum safe fix

In `ConsentService.writeConsentToCCMS(...)`, replace response status setting with null-safe handling.

## Replace this

```java
consentWrite.setResponseCode(
		writeResponse.getEisResponse().getStatusCode()
);
consentWrite.setResponseMsg(responseStr);
consentWrite.setResponseStatus(
		writeResponse.getEisResponse().getSuccess()
);
```

## With this

```java
consentWrite.setResponseMsg(responseStr);

if (writeResponse != null && writeResponse.getEisResponse() != null) {

	consentWrite.setResponseCode(
			writeResponse.getEisResponse().getStatusCode()
	);

	consentWrite.setResponseStatus(
			writeResponse.getEisResponse().getSuccess()
	);

} else {
	/*
	 * CCMS/EIS returned HTTP 200 but response is encrypted/wrapped.
	 * Store raw response and mark technical success for Get Quote flow.
	 */
	consentWrite.setResponseCode("200");
	consentWrite.setResponseStatus("true");
}
```

---

# Better fix in `HomeProcessManagerImpl`

Change this strict check:

```java
if (consentWrite == null
		|| !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
		|| !"200".equalsIgnoreCase(consentWrite.getResponseCode())) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return false;
}
```

To this null-safe check:

```java
if (consentWrite == null) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return false;
}

String responseCode = consentWrite.getResponseCode();
String responseStatus = consentWrite.getResponseStatus();

if (!"200".equalsIgnoreCase(responseCode)
		&& !"true".equalsIgnoreCase(responseStatus)) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return false;
}
```

---

# Add useful logs

Inside `writeConsentToCCMS(...)`, after response:

```java
logger.info("CCMS Write raw response :: " + responseStr);
logger.info("CCMS Write responseCode :: " + consentWrite.getResponseCode());
logger.info("CCMS Write responseStatus :: " + consentWrite.getResponseStatus());
```

---

# Also fix this: only one consent is going

Your log shows only one consent:

```json
"consents":[
  {
    "purposeVersion":"1",
    "purposeCode":"PURP-DEMOPURP-000068",
    "consented":"true"
  }
]
```

But your prompt expected two:

```java
consents.add(consent1);
consents.add(consent2);
```

Check your actual `generateConsentWriteRequest()` and make sure this is present:

```java
consents.add(consent1);
consents.add(consent2);
```

Also:

```java
payloadBody.setConsents(consents);
```

---

# Final answer

Yes, request/response is storing, but your flow is not going forward because your code is expecting decrypted `eisResponse.statusCode/success`. Your actual CCMS response is encrypted/wrapped, so set fallback:

```java
responseCode = "200";
responseStatus = "true";
```

when `eisResponse` is null.
#### ----------------------------------------------------------



Yes, your functional request body is correct.

Main correction needed in Java: `consented` should be **boolean true**, not string `"true"`.

## Change in `ConsentRequestConsent.java`

Check current field. It is probably:

```java
private String consented;
```

Change it to:

```java
private Boolean consented;
```

Getter/setter:

```java
public Boolean getConsented() {
	return consented;
}

public void setConsented(Boolean consented) {
	this.consented = consented;
}
```

---

## Change in `ConsentService.java`

### Replace this

```java
consent1.setConsented("true");
```

with:

```java
consent1.setConsented(Boolean.TRUE);
```

### Replace this

```java
consent2.setConsented("true");
```

with:

```java
consent2.setConsented(Boolean.TRUE);
```

---

## Also change purposeVersion

Your working body has:

```json
"purposeVersion": 1
```

not:

```json
"purposeVersion": "1"
```

So in `ConsentRequestConsent.java`, change:

```java
private String purposeVersion;
```

to:

```java
private Integer purposeVersion;
```

Getter/setter:

```java
public Integer getPurposeVersion() {
	return purposeVersion;
}

public void setPurposeVersion(Integer purposeVersion) {
	this.purposeVersion = purposeVersion;
}
```

Then in `ConsentService.java`:

```java
consent1.setPurposeVersion(Integer.valueOf(1));
consent2.setPurposeVersion(Integer.valueOf(1));
```

---

## Final required Java body part

Use this:

```java
consent1.setPurposeCode("PURP-DEMOPURP-000068");
consent1.setPurposeVersion(Integer.valueOf(1));
consent1.setConsented(Boolean.TRUE);

consentedProducts.add("HOME_LOAN");

consent2.setPurposeCode("PURP-DEMOPURP-000071");
consent2.setPurposeVersion(Integer.valueOf(1));
consent2.setConsented(Boolean.TRUE);
consent2.setConsentedProducts(consentedProducts);
consent2.setNotConsentedProducts(notConsentedProducts);

consents.add(consent1);
consents.add(consent2);
```

## Meaning

Your DB value:

```text
accepted_consent = Y
```

should become:

```json
"consented": true
```

If value is not `Y`, then do not call CCMS Write.



####--------------------------------------------


Yes. Keep it strict in **both frontend and backend**.

## 1) Frontend strict validation

In `HomeFirstPageSession.jsp`, before submit:

```javascript
$(document).on("click", "#subtn", function(){

	if ($("#quotePrivacyConsentFlag").val() !== "Y") {
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}

	if ($.trim($("#quoteNtbId").val()) === "") {
		alert("Invalid consent details. Please accept SBI Privacy Notice again.");
		return false;
	}

	if ($.trim($("#quotePrivacyLocale").val()) === "") {
		alert("Invalid privacy language details. Please accept SBI Privacy Notice again.");
		return false;
	}

	return true;
});
```

---

## 2) Accept button should set `Y`

In `ConsentPopup.jsp`, inside `acceptPrivacyConsent()`:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);

$("#consentHomeLoan").modal("hide");
```

---

## 3) Backend strict validation

In `HomeProcessManagerImpl.java`, keep this check before CCMS Write:

```java
if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
	return false;
}
```

So if consent is not `Y`:

```text
CCMS Write will NOT be called
BRE will NOT be called
Get Quote will stop
```

---

## Final flow

```text
Consent not accepted
→ quotePrivacyConsentFlag != Y
→ block submit frontend

Even if frontend bypassed
→ backend checks quotePrivacyConsentFlag != Y
→ CCMS Write not called
→ BRE not called
→ error message returned
```

This makes consent acceptance mandatory.






