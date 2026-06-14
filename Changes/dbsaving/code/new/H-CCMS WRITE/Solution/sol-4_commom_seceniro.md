Make CCMS write common in **ConsentService** and call it from each loan’s process manager before BRE/offer.

## 1) Create common request bean

```java
package com.mintstreet.consent.bo;

public class ConsentWriteInput {

	private String ntbId;
	private String mobile;
	private String email;
	private String ipAddress;
	private String locale;
	private String productCode;

	// getters/setters
}
```

`productCode` values:

```text
HOME_LOAN
AUTO_LOAN
PERSONAL_LOAN
EDUCATION_LOAN
```

---

## 2) `ConsentService.java`

Add this common method:

```java
public ConsentWriteLog writeConsentToCCMSForLoan(ConsentWriteInput input) throws JSONException {

	ConsentWriteLog consentWrite =
		generateConsentWriteRequest(input);

	consentWrite =
		writeConsentToCCMS(consentWrite);

	return consentWrite;
}
```

Replace `generateConsentWriteRequest(...)` with common input:

```java
public ConsentWriteLog generateConsentWriteRequest(ConsentWriteInput input) {

	ConsentRequest request = new ConsentRequest();
	ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
	ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
	ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
	ConsentRequestDpData dpData = new ConsentRequestDpData();

	ConsentRequestConsent consent1 = new ConsentRequestConsent();
	ConsentRequestConsent consent2 = new ConsentRequestConsent();

	List<ConsentRequestConsent> consents = new ArrayList<>();
	List<String> consentedProducts = new ArrayList<>();

	String correlationId = UuidV7Generator.generateV7().toString();
	CCMSConfig config = commonService.getCcmsConfigById(1);

	String locale = input.getLocale();
	if(locale == null || locale.trim().equals("")){
		locale = "eng";
	}

	payloadHeaders.setAcceptLanguage(locale);
	payloadHeaders.setxCorelationId(correlationId);
	payloadHeaders.setxApiVersion(config.getApiVersion());

	dpData.setNtbId(input.getNtbId());
	dpData.setDpMobile(input.getMobile());
	dpData.setDpEmail(input.getEmail());
	dpData.setDpIPAddress(input.getIpAddress());
	dpData.setLocale(locale);
	dpData.setTimestamp(getCurrentIsoTimestamp());

	consent1.setPurposeCode("PURP-DEMOPURP-000068");
	consent1.setPurposeVersion("1");
	consent1.setConsented("true");

	consentedProducts.add(input.getProductCode());

	consent2.setPurposeCode("PURP-DEMOPURP-000071");
	consent2.setPurposeVersion("1");
	consent2.setConsented("true");
	consent2.setConsentedProducts(consentedProducts);

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

	return consentWriteDao.save(
		consentWrite.getConsentWriteId(),
		consentWrite
	);
}
```

Add timestamp helper:

```java
private String getCurrentIsoTimestamp() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	return sdf.format(new Date());
}
```

Your current `ConsentService` already has write-log save and EIS call structure, so this only makes it dynamic/common. 

---

## 3) Create common util/helper

Create:

```java
package com.mintstreet.consent.util;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;

public class LoanConsentCcmsHelper {

	@Autowired
	private ConsentService consentService;

	public boolean writeConsent(ConsentWriteInput input) throws JSONException {

		if(input == null){
			return false;
		}

		if(!ValidatorUtil.isValid(input.getNtbId())){
			return false;
		}

		if(!ValidatorUtil.isValid(input.getMobile())){
			return false;
		}

		if(!ValidatorUtil.isValid(input.getLocale())){
			input.setLocale("eng");
		}

		ConsentWriteLog log =
			consentService.writeConsentToCCMSForLoan(input);

		return log != null
			&& "200".equalsIgnoreCase(log.getResponseCode())
			&& "true".equalsIgnoreCase(log.getResponseStatus());
	}
}
```

---

## 4) Spring config

Add:

```xml
<bean id="loanConsentCcmsHelper"
	class="com.mintstreet.consent.util.LoanConsentCcmsHelper" />
```

---

## 5) Use in `HomeProcessManagerImpl.java`

Add imports:

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.util.LoanConsentCcmsHelper;
```

Add autowire:

```java
@Autowired
private LoanConsentCcmsHelper loanConsentCcmsHelper;
```

Before `callBRE(...)`, add:

```java
ConsentWriteInput consentInput = new ConsentWriteInput();

consentInput.setNtbId(quote.getQuoteNtbId());
consentInput.setMobile(application.getAppMobileNo());
consentInput.setEmail(application.getAppWorkEmail());
consentInput.setIpAddress(this.SbiUtil.getIPAddress());
consentInput.setLocale(quote.getQuotePrivacyLocale());
consentInput.setProductCode("HOME_LOAN");

if(!loanConsentCcmsHelper.writeConsent(consentInput)){
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return loanScenarioBean;
}
```

`HomeProcessManagerImpl` is the right place because Home Loan Get Quote flow uses `processGetQuotes(...)`, and quote/app are available before BRE. 

---

## 6) For Auto Loan

In Auto Loan process manager before BRE/offer:

```java
ConsentWriteInput consentInput = new ConsentWriteInput();

consentInput.setNtbId(autoQuote.getQuoteNtbId());
consentInput.setMobile(application.getAppMobileNo());
consentInput.setEmail(application.getAppWorkEmail());
consentInput.setIpAddress(this.SbiUtil.getIPAddress());
consentInput.setLocale(autoQuote.getQuotePrivacyLocale());
consentInput.setProductCode("AUTO_LOAN");

if(!loanConsentCcmsHelper.writeConsent(consentInput)){
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return loanScenarioBean;
}
```

---

## 7) For Personal Loan

```java
ConsentWriteInput consentInput = new ConsentWriteInput();

consentInput.setNtbId(personalQuote.getQuoteNtbId());
consentInput.setMobile(application.getAppMobileNo());
consentInput.setEmail(application.getAppWorkEmail());
consentInput.setIpAddress(this.SbiUtil.getIPAddress());
consentInput.setLocale(personalQuote.getQuotePrivacyLocale());
consentInput.setProductCode("PERSONAL_LOAN");

if(!loanConsentCcmsHelper.writeConsent(consentInput)){
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return loanScenarioBean;
}
```

---

## 8) JSP common hidden fields

For every loan first-page form:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
```

---

## 9) Common popup JS

On Accept:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();
var ntbId = cleanMobile + cleanDob + loanTypeId;

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

---

Final common flow:

```text
Any Loan Accept Consent
→ quote consent fields set
→ Get Quote submits
→ Java creates product-specific ConsentWriteInput
→ common LoanConsentCcmsHelper calls ConsentService
→ request/response saved in RUPEEPOWER_OCAS_T_13701
→ CCMS success then BRE/offer continues
```
