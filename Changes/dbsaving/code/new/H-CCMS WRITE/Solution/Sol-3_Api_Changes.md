Yes. Do **not** call CCMS Write API from JS.

Do it in Java inside **Get Quote backend flow**, after quote/application is created and before BRE/offer flow. Your `insertLoanQuote()` already saves quote request data into quote table, and `processGetQuotes()` later calls `callBRE(...)`; so CCMS Write should be called **before `callBRE(...)`**.  

## Files to change

```text
1. ConsentService.java
2. ConsentWriteDao.java
3. HomeProcessManagerImpl.java
4. ApplicationFormHomeLoanQuote.java
5. HomeRightContent.jsp
6. ConsentPopup.jsp
```

---

# 1) `ConsentWriteDao.java`

Create if not already available:

```java
package com.mintstreet.consent.dao;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.ConsentWriteLog;

public class ConsentWriteDao extends GenericDao<Integer, ConsentWriteLog> {

	private static final long serialVersionUID = 1L;

}
```

Your `ConsentService` already has `ConsentWriteDao consentWriteDao`, so this DAO is needed for saving request/response log. 

---

# 2) `ConsentService.java`

Your current `generateConsentWriteRequest()` has hardcoded test values like mobile, email, IP, locale, timestamp. Replace it with dynamic values from quote/application. 

## Add imports

```java
import java.text.SimpleDateFormat;
import java.util.TimeZone;
```

## Add helper method inside `ConsentService`

```java
private String getCurrentIsoTimestamp() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	return sdf.format(new Date());
}
```

## Replace old `generateConsentWriteRequest()` with this

```java
public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) {

	ConsentRequest request = new ConsentRequest();
	ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
	ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
	ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
	ConsentRequestDpData dpData = new ConsentRequestDpData();

	ConsentRequestConsent consent1 = new ConsentRequestConsent();
	ConsentRequestConsent consent2 = new ConsentRequestConsent();

	List<ConsentRequestConsent> consents = new ArrayList<>();
	List<String> consentedProducts = new ArrayList<>();
	List<String> notConsentedProducts = new ArrayList<>();

	String correlationId = UuidV7Generator.generateV7().toString();
	CCMSConfig config = commonService.getCcmsConfigById(1);

	if (locale == null || locale.trim().equals("")) {
		locale = "eng";
	}

	payloadHeaders.setAcceptLanguage(locale);
	payloadHeaders.setxCorelationId(correlationId);
	payloadHeaders.setxApiVersion(config.getApiVersion());

	dpData.setNtbId(ntbId);
	dpData.setDpMobile(mobile);
	dpData.setDpEmail(email);
	dpData.setDpIPAddress(ipAddress);
	dpData.setLocale(locale);
	dpData.setTimestamp(getCurrentIsoTimestamp());

	consent1.setPurposeCode("PURP-DEMOPURP-000068");
	consent1.setPurposeVersion("1");
	consent1.setConsented("true");

	consentedProducts.add("HOME_LOAN");

	consent2.setPurposeCode("PURP-DEMOPURP-000071");
	consent2.setPurposeVersion("1");
	consent2.setConsented("true");
	consent2.setConsentedProducts(consentedProducts);

	/*
	 * Add this only if ConsentRequestConsent class has this setter.
	 * consent2.setNotConsentedProducts(notConsentedProducts);
	 */

	consents.add(consent1);
	consents.add(consent2);

	payloadBody.setTouchPointId(config.getTouchPointId());
	payloadBody.setPurposeSetId(config.getPurposeSetId());
	payloadBody.setDpData(dpData);
	payloadBody.setConsents(consents);

	eisPayload.setHeaders(payloadHeaders);
	eisPayload.setBody(payloadBody);

	request.setSourceId(config.getSourceId());
	request.setEisPayload(eisPayload);
	request.setDestination(config.getDestination());
	request.setTransactionType(config.getTransactionType());
	request.setTransactionSubType("UPDATE_CONSENTS");

	JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);

	ConsentWriteLog consentWrite = new ConsentWriteLog();
	consentWrite.setxCorrelationId(correlationId);
	consentWrite.setConsentWriteRequest(consentRequest.toString());
	consentWrite.setRequestEntryTime(new Date());

	consentWrite = consentWriteDao.save(
			consentWrite.getConsentWriteId(),
			consentWrite
	);

	return consentWrite;
}
```

## Replace `writeConsentToCCMS(...)` with this

```java
public ConsentWriteLog writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {

	JSONObject responseJson =
			ccmsUtil.callingEISServiceForCcms(
					consentWrite.getConsentWriteRequest()
			);

	String responseStr = responseJson.toString();

	ConsentResponseConsentWrite writeResponse =
			new ConsentResponseConsentWrite();

	writeResponse =
			(ConsentResponseConsentWrite) JSONUtil.getObjctFromJSON(
					writeResponse,
					responseStr
			);

	consentWrite.setResponseCode(
			writeResponse.getEisResponse().getStatusCode()
	);
	consentWrite.setResponseMsg(responseStr);
	consentWrite.setResponseStatus(
			writeResponse.getEisResponse().getSuccess()
	);
	consentWrite.setResponseEntryTime(new Date());

	consentWriteDao.save(
			consentWrite.getConsentWriteId(),
			consentWrite
	);

	return consentWrite;
}
```

---

# 3) Spring config

Make sure DAO is injected like your read/purpose DAO.

Example:

```xml
<bean id="consentWriteDao"
	class="com.mintstreet.consent.dao.ConsentWriteDao">
	<property name="entityManagerFactory" ref="entityManagerFactory" />
</bean>

<bean id="consentService"
	class="com.mintstreet.consent.service.ConsentService">
	<property name="consentWriteDao" ref="consentWriteDao" />
</bean>
```

Adjust property names as per your existing DAO wiring.

---

# 4) `HomeProcessManagerImpl.java`

## Add import

```java
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;
```

## Add autowired service

Near other `@Autowired` fields:

```java
@Autowired
private ConsentService consentService;
```

## Add private method inside class

```java
private boolean writePrivacyConsentToCCMS(
		ApplicationFormHomeLoan application,
		ApplicationFormHomeLoanQuote quote,
		LoanScenarioBean loanScenarioBean) {

	try {
		if (application == null || quote == null) {
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
			return false;
		}

		if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
			return false;
		}

		if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
			return false;
		}

		if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Invalid privacy language details. Please accept SBI Privacy Notice again.");
			return false;
		}

		String mobile = application.getAppMobileNo();
		String email = application.getAppWorkEmail();

		if (!ValidatorUtil.isValid(mobile)) {
			mobile = quote.getAppMobile();
		}

		if (!ValidatorUtil.isValid(email)) {
			email = quote.getAppEmail();
		}

		if (!ValidatorUtil.isValid(mobile)) {
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Mobile number is required for consent write.");
			return false;
		}

		String ipAddress = this.SbiUtil.getIPAddress();

		ConsentWriteLog consentWrite =
				consentService.generateConsentWriteRequest(
						quote.getQuoteNtbId(),
						mobile,
						email,
						ipAddress,
						quote.getQuotePrivacyLocale()
				);

		consentWrite =
				consentService.writeConsentToCCMS(consentWrite);

		if (consentWrite == null
				|| !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
				|| !"200".equalsIgnoreCase(consentWrite.getResponseCode())) {

			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
			return false;
		}

		return true;

	} catch (Exception e) {
		logger.info("Exception while calling CCMS Write API", e);
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
		return false;
	}
}
```

---

# 5) Add call inside `processGetQuotes(...)`

Inside `processGetQuotes(...)`, find where `callBRE(...)` is called:

```java
loanScenarioBean = this.homeLoanHelper.callBRE(application, quote, bankLmsUser, Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
```

Before this line, add:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean
		);

if (!ccmsWriteStatus) {
	return loanScenarioBean;
}
```

Final:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean
		);

if (!ccmsWriteStatus) {
	return loanScenarioBean;
}

loanScenarioBean = this.homeLoanHelper.callBRE(
		application,
		quote,
		bankLmsUser,
		Integer.valueOf(previousQuoteId),
		trackVisitId,
		ajaxPostUrl,
		true
);
```

This is the correct place because it is still inside Get Quote backend flow and before BRE/offer processing. 

---

# 6) `ApplicationFormHomeLoanQuote.java`

Make sure these fields exist:

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

# 7) `HomeRightContent.jsp`

Inside `<form id="homeloanform">`, after CSRF:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
```

---

# 8) `ConsentPopup.jsp`

Inside `acceptPrivacyConsent()`, set locale also:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

No CCMS AJAX here.

---

# 9) `HomeFirstPageSession.jsp`

Only validate hidden fields. Do **not** call CCMS from JS.

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

# Final production flow

```text
Accept privacy notice
→ quote hidden fields set:
   QUOTE_PRIVACY_CONSENT_FLAG = Y
   QUOTE_NTB_ID = mobile + dob + loanTypeId
   QUOTE_PRIVACY_LOCALE = selected dropdown language
→ Get Quote submits normally
→ insertLoanQuote saves quote
→ insertAppLoan creates/updates application
→ Java builds CCMS Write API request
→ saves request in RUPEEPOWER_OCAS_T_13701
→ calls EIS/CCMS
→ saves response in RUPEEPOWER_OCAS_T_13701
→ only if CCMS success, continue to BRE / offer
```

Do not add any separate JS API call for CCMS.
