Below is the **complete common production-style flow**.

No JS CCMS call.
CCMS Write API will be called from **Java backend** during Get Quote submit flow.

---

# Final flow

```text
User accepts privacy notice
→ quotePrivacyConsentFlag = Y
→ quoteNtbId = mobile + dob + loanTypeId
→ quotePrivacyLocale = selected language
→ Get Quote submit
→ quote saved
→ application created/updated
→ common Java CCMS Write API called
→ request saved in CONSENT_WRITE_LOG table
→ response saved in CONSENT_WRITE_LOG table
→ only then BRE/offer continues
```

---

# 1) DB changes

For each loan quote table, add these columns.

For Home Loan quote table:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_NTB_ID VARCHAR2(100);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);
```

For Auto / Personal / Education quote tables, add same type of columns in their quote table also.

---

# 2) Create common input bean

## New file

```text
com.mintstreet.consent.bo.ConsentWriteInput.java
```

```java
package com.mintstreet.consent.bo;

public class ConsentWriteInput {

	private String ntbId;
	private String mobile;
	private String email;
	private String ipAddress;
	private String locale;
	private String productCode;
	private String consentFlag;

	public String getNtbId() {
		return ntbId;
	}

	public void setNtbId(String ntbId) {
		this.ntbId = ntbId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getConsentFlag() {
		return consentFlag;
	}

	public void setConsentFlag(String consentFlag) {
		this.consentFlag = consentFlag;
	}
}
```

---

# 3) `ConsentWriteDao.java`

If not already present, create:

```text
com.mintstreet.consent.dao.ConsentWriteDao.java
```

```java
package com.mintstreet.consent.dao;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.ConsentWriteLog;

public class ConsentWriteDao extends GenericDao<Integer, ConsentWriteLog> {

	private static final long serialVersionUID = 1L;

}
```

Your `ConsentWriteLog` already stores request, response code, response body, status, and timestamps. 

---

# 4) `ConsentService.java`

Your current `generateConsentWriteRequest()` has hardcoded test values. Replace it with common dynamic code. 

## Add imports

```java
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import com.mintstreet.consent.bo.ConsentWriteInput;
```

## Add helper method

```java
private String getCurrentIsoTimestamp() {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	return sdf.format(new Date());
}
```

## Replace old `generateConsentWriteRequest()` with this

```java
public ConsentWriteLog generateConsentWriteRequest(ConsentWriteInput input) {

	ConsentRequest request = new ConsentRequest();
	ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
	ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
	ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
	ConsentRequestDpData dpData = new ConsentRequestDpData();

	ConsentRequestConsent consent1 = new ConsentRequestConsent();
	ConsentRequestConsent consent2 = new ConsentRequestConsent();

	List<ConsentRequestConsent> consents = new ArrayList<ConsentRequestConsent>();
	List<String> consentedProducts = new ArrayList<String>();
	List<String> notConsentedProducts = new ArrayList<String>();

	String correlationId = UuidV7Generator.generateV7().toString();
	CCMSConfig config = commonService.getCcmsConfigById(1);

	String locale = input.getLocale();
	if (locale == null || locale.trim().equals("")) {
		locale = "eng";
	}

	boolean consented = "Y".equalsIgnoreCase(input.getConsentFlag());

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
	consent1.setConsented(String.valueOf(consented));

	consentedProducts.add(input.getProductCode());

	consent2.setPurposeCode("PURP-DEMOPURP-000071");
	consent2.setPurposeVersion("1");
	consent2.setConsented(String.valueOf(consented));
	consent2.setConsentedProducts(consentedProducts);

	/*
	 * Uncomment only if your ConsentRequestConsent class has setter.
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

## Replace `writeConsentToCCMS()` return type

Change from:

```java
public void writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {
```

To:

```java
public ConsentWriteLog writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {
```

At end of method, add:

```java
return consentWrite;
```

Final method ending:

```java
consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

return consentWrite;
```

---

# 5) `ConsentUtil.java`

Your current method still calls old no-argument `generateConsentWriteRequest()`. Replace it. Existing util already has purpose/read/write wrappers.

## Add import

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
```

## Replace old method

```java
public void callCCMSConsentWriteAPI() {
```

with:

```java
public ConsentWriteLog callCCMSConsentWriteAPI(ConsentWriteInput input) {

	try {
		ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(input);

		consentWrite = consentService.writeConsentToCCMS(consentWrite);

		return consentWrite;

	} catch (JSONException e) {
		e.printStackTrace();
		return null;
	}
}
```

---

# 6) Create common helper for all loans

## New file

```text
com.mintstreet.consent.util.LoanConsentCcmsHelper.java
```

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

			if (log == null) {
				return false;
			}

			return "200".equalsIgnoreCase(log.getResponseCode())
					&& "true".equalsIgnoreCase(log.getResponseStatus());

		} catch (Exception e) {
			logger.info("Exception while writing consent to CCMS", e);
			return false;
		}
	}
}
```

---

# 7) Spring config

Add helper bean and DAO wiring.

Example:

```xml
<bean id="consentWriteDao"
	class="com.mintstreet.consent.dao.ConsentWriteDao">
</bean>

<bean id="loanConsentCcmsHelper"
	class="com.mintstreet.consent.util.LoanConsentCcmsHelper">
</bean>
```

If your project wires DAOs by property, also confirm this exists in `ConsentService` bean:

```xml
<property name="consentWriteDao" ref="consentWriteDao" />
```

---

# 8) `ApplicationFormHomeLoanQuote.java`

You already added privacy fields in Home quote entity. Keep/add this block near quote fields:

```java
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;

@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;
```

Add getters/setters:

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

Do same in Auto / Personal / Education quote entities also.

---

# 9) `HomeRightContent.jsp`

Inside `<form id="homeloanform">`, after CSRF hidden field, add:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
```

---

# 10) `ConsentPopup.jsp`

Inside `acceptPrivacyConsent()`, set locale and flag.

Replace:

```javascript
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
```

With:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
```

---

# 11) `HomeFirstPageSession.jsp`

Only frontend validation. No CCMS call in JS.

Add/keep this:

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

# 12) `HomeProcessManagerImpl.java`

## Add imports

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.util.LoanConsentCcmsHelper;
```

## Add autowire

Near other `@Autowired` fields:

```java
@Autowired
private LoanConsentCcmsHelper loanConsentCcmsHelper;
```

## Add common private method

```java
private boolean writeLoanConsentToCCMS(
		ApplicationFormHomeLoan application,
		ApplicationFormHomeLoanQuote quote,
		String productCode,
		LoanScenarioBean loanScenarioBean) {

	if (application == null || quote == null) {
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
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

	ConsentWriteInput input = new ConsentWriteInput();
	input.setNtbId(quote.getQuoteNtbId());
	input.setMobile(mobile);
	input.setEmail(email);
	input.setIpAddress(this.SbiUtil.getIPAddress());
	input.setLocale(quote.getQuotePrivacyLocale());
	input.setProductCode(productCode);
	input.setConsentFlag(quote.getQuotePrivacyConsentFlag());

	boolean ccmsStatus = loanConsentCcmsHelper.writeConsent(input);

	if (!ccmsStatus) {
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
		return false;
	}

	return true;
}
```

## Add call before BRE

Find:

```java
loanScenarioBean = this.homeLoanHelper.callBRE(
```

Add immediately before it:

```java
boolean ccmsWriteStatus =
		writeLoanConsentToCCMS(
				application,
				quote,
				"HOME_LOAN",
				loanScenarioBean
		);

if (!ccmsWriteStatus) {
	return loanScenarioBean;
}
```

---

# 13) For Auto Loan process manager

Before Auto BRE/offer call:

```java
ConsentWriteInput input = new ConsentWriteInput();
input.setNtbId(autoQuote.getQuoteNtbId());
input.setMobile(application.getAppMobileNo());
input.setEmail(application.getAppWorkEmail());
input.setIpAddress(this.SbiUtil.getIPAddress());
input.setLocale(autoQuote.getQuotePrivacyLocale());
input.setProductCode("AUTO_LOAN");
input.setConsentFlag(autoQuote.getQuotePrivacyConsentFlag());

if (!loanConsentCcmsHelper.writeConsent(input)) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return loanScenarioBean;
}
```

---

# 14) For Personal Loan process manager

```java
ConsentWriteInput input = new ConsentWriteInput();
input.setNtbId(personalQuote.getQuoteNtbId());
input.setMobile(application.getAppMobileNo());
input.setEmail(application.getAppWorkEmail());
input.setIpAddress(this.SbiUtil.getIPAddress());
input.setLocale(personalQuote.getQuotePrivacyLocale());
input.setProductCode("PERSONAL_LOAN");
input.setConsentFlag(personalQuote.getQuotePrivacyConsentFlag());

if (!loanConsentCcmsHelper.writeConsent(input)) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
	return loanScenarioBean;
}
```

---

# Important correction for `"consented"`

Your requirement:

```text
Y → true
N / blank / null → false
```

This line handles it:

```java
boolean consented = "Y".equalsIgnoreCase(input.getConsentFlag());
```

Then request becomes:

```json
"consented": true
```

or:

```json
"consented": false
```

depending on flag.
