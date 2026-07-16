Use **one common CCMS Write implementation** for every product. Each loan module only prepares its data and updates its own quote/application tables after the common service returns a successful `consentId`.

One important correction: your existing fields such as:

```java
private Integer appConsentId;
private Integer loanQuoteConsentId;
```

are legacy numeric consent-master IDs. The CCMS response returns a UUID-like value:

```text
019f6b4f-399a-71be-85a3-46cebcb4f751
```

Do **not** store that value in an `Integer` column. Add separate `VARCHAR2` CCMS consent-ID columns.

---

# A. Common changes for every loan

## 1. DB: Consent Write Log table

Table:

```text
RUPEEPOWER_OCAS_T_13701
```

Run only for columns not already present:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_NTB_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_CONSENT_ID VARCHAR2(255);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_IS_ACTIVE CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_LOAN_TYPE NUMBER;
```

Recommended index:

```sql
CREATE INDEX SBI_TEST.IDX_CCW_NTB_LOAN_ACTIVE
ON SBI_TEST.RUPEEPOWER_OCAS_T_13701
(
    CONSENT_WRITE_NTB_ID,
    CONSENT_WRITE_LOAN_TYPE,
    CONSENT_WRITE_IS_ACTIVE
);
```

---

# 2. `ConsentResponseConsentWriteBody.java`

Add:

```java
private String ntbId;

private String consentId;
```

Add getters/setters:

```java
public String getNtbId() {
	return ntbId;
}

public void setNtbId(String ntbId) {
	this.ntbId = ntbId;
}

public String getConsentId() {
	return consentId;
}

public void setConsentId(String consentId) {
	this.consentId = consentId;
}
```

The JSON mapper will map:

```json
{
  "ntbId": "...",
  "consentId": "..."
}
```

into these properties.

---

# 3. `ConsentWriteLog.java`

Add these fields below the existing request/response fields:

```java
@Column(name="CONSENT_WRITE_NTB_ID")
private String consentWriteNtbId;

@Column(name="CONSENT_WRITE_CONSENT_ID")
private String consentWriteConsentId;

@Column(name="CONSENT_WRITE_IS_ACTIVE")
private String consentWriteIsActive;

@Column(name="CONSENT_WRITE_LOAN_TYPE")
private Integer consentWriteLoanType;
```

Add getters/setters:

```java
public String getConsentWriteNtbId() {
	return consentWriteNtbId;
}

public void setConsentWriteNtbId(String consentWriteNtbId) {
	this.consentWriteNtbId = consentWriteNtbId;
}

public String getConsentWriteConsentId() {
	return consentWriteConsentId;
}

public void setConsentWriteConsentId(String consentWriteConsentId) {
	this.consentWriteConsentId = consentWriteConsentId;
}

public String getConsentWriteIsActive() {
	return consentWriteIsActive;
}

public void setConsentWriteIsActive(String consentWriteIsActive) {
	this.consentWriteIsActive = consentWriteIsActive;
}

public Integer getConsentWriteLoanType() {
	return consentWriteLoanType;
}

public void setConsentWriteLoanType(Integer consentWriteLoanType) {
	this.consentWriteLoanType = consentWriteLoanType;
}
```

---

# 4. `ConsentWriteInput.java`

Keep one common input object:

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
	private Integer loanTypeId;

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

	public Integer getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(Integer loanTypeId) {
		this.loanTypeId = loanTypeId;
	}
}
```

`mobile`, `email`, `ipAddress`, and `productCode` are runtime fields. They do not require new columns in every loan table.

---

# 5. `ConsentService.generateConsentWriteRequest(...)`

Inside the existing request-generation method, save loan type and NTB ID in the log before the initial save.

Use:

```java
public ConsentWriteLog generateConsentWriteRequest(
		ConsentWriteInput input) throws JSONException {

	// Existing request-building code remains here.

	boolean consented =
			"Y".equalsIgnoreCase(input.getConsentFlag());

	/*
	 * Use Boolean setter if ConsentRequestConsent.consented is Boolean.
	 */
	consent1.setConsented(consented);
	consent2.setConsented(consented);

	/*
	 * If your current property is String, use:
	 *
	 * consent1.setConsented(String.valueOf(consented));
	 * consent2.setConsented(String.valueOf(consented));
	 */

	consentedProducts.add(input.getProductCode());
	consent2.setConsentedProducts(consentedProducts);
	consent2.setNotConsentedProducts(
			new ArrayList<String>()
	);

	// Existing BODY, HEADERS and EIS request construction remains.

	JSONObject consentRequest =
			JSONUtil.beanObjectToJSONObjct(request);

	ConsentWriteLog consentWrite =
			new ConsentWriteLog();

	consentWrite.setxCorrelationId(correlationId);
	consentWrite.setConsentWriteRequest(
			consentRequest.toString()
	);
	consentWrite.setRequestEntryTime(new Date());

	consentWrite.setConsentWriteNtbId(
			input.getNtbId()
	);
	consentWrite.setConsentWriteLoanType(
			input.getLoanTypeId()
	);
	consentWrite.setConsentWriteIsActive("N");

	consentWrite =
			consentWriteDao.save(
					consentWrite.getConsentWriteId(),
					consentWrite
			);

	return consentWrite;
}
```

For the generated JSON to contain a real boolean:

```json
"consented": true
```

the bean property should ideally be:

```java
private Boolean consented;
```

and setter:

```java
public void setConsented(Boolean consented) {
	this.consented = consented;
}
```

Do not use `String` if you can safely change the request bean, because it may generate:

```json
"consented": "true"
```

instead of:

```json
"consented": true
```

---

# 6. `ConsentService.writeConsentToCCMS(...)`

Replace the existing method with this pattern:

```java
public ConsentWriteLog writeConsentToCCMS(
		ConsentWriteLog consentWrite,
		Integer loanTypeId) throws JSONException {

	if (consentWrite == null
			|| !ValidatorUtil.isValid(
					consentWrite.getConsentWriteRequest())) {

		throw new IllegalArgumentException(
				"Consent write request cannot be null or blank"
		);
	}

	try {
		JSONObject responseJson =
				ccmsUtil.callingEISServiceForCcms(
						consentWrite.getConsentWriteRequest()
				);

		String responseStr =
				responseJson != null
						? responseJson.toString()
						: "";

		ConsentResponseConsentWrite response =
				new ConsentResponseConsentWrite();

		response =
				(ConsentResponseConsentWrite)
						JSONUtil.getObjctFromJSON(
								response,
								responseStr
						);

		String responseCode = null;
		String responseStatus = null;

		if (response != null
				&& response.getEisResponse() != null) {

			responseCode =
					String.valueOf(
							response.getEisResponse()
									.getStatusCode()
					);

			responseStatus =
					String.valueOf(
							response.getEisResponse()
									.getSuccess()
					);
		}

		consentWrite.setResponseCode(responseCode);
		consentWrite.setResponseStatus(responseStatus);
		consentWrite.setResponseMsg(responseStr);
		consentWrite.setResponseEntryTime(new Date());
		consentWrite.setConsentWriteLoanType(loanTypeId);

		boolean success =
				"200".equals(responseCode)
				&& "true".equalsIgnoreCase(
						responseStatus
				);

		if (success
				&& response.getEisResponse().getBody() != null
				&& ValidatorUtil.isValid(
						response.getEisResponse()
								.getBody()
								.getConsentId()
				)) {

			ConsentResponseConsentWriteBody body =
					response.getEisResponse().getBody();

			consentWrite.setConsentWriteConsentId(
					body.getConsentId()
			);

			consentWrite.setConsentWriteIsActive("Y");

			if (ValidatorUtil.isValid(body.getNtbId())) {
				consentWrite.setConsentWriteNtbId(
						body.getNtbId()
				);
			}

			logger.info(
					"CCMS Consent Write successful. "
					+ "loanTypeId={}, ntbId={}, consentId={}",
					loanTypeId,
					body.getNtbId(),
					body.getConsentId()
			);

		} else {
			consentWrite.setConsentWriteIsActive("N");

			logger.info(
					"CCMS Consent Write failed. "
					+ "loanTypeId={}, responseCode={}, responseStatus={}",
					loanTypeId,
					responseCode,
					responseStatus
			);
		}

		consentWrite =
				consentWriteDao.save(
						consentWrite.getConsentWriteId(),
						consentWrite
				);

		return consentWrite;

	} catch (Exception e) {

		logger.info(
				"Exception while calling CCMS Write API",
				e
		);

		consentWrite.setResponseCode("500");
		consentWrite.setResponseStatus("false");
		consentWrite.setConsentWriteIsActive("N");
		consentWrite.setResponseMsg(
				e.getMessage() != null
						? e.getMessage()
						: "CCMS Write API exception"
		);
		consentWrite.setResponseEntryTime(new Date());

		consentWrite =
				consentWriteDao.save(
						consentWrite.getConsentWriteId(),
						consentWrite
				);

		return consentWrite;
	}
}
```

Add imports if missing:

```java
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.bo.ConsentResponseConsentWriteBody;
```

---

# 7. Common CCMS helper

Change the common helper so it returns the populated log instead of only a boolean. Every loan needs the returned `consentId`.

## `LoanConsentCcmsHelper.java`

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
			LogManager.getLogger(
					LoanConsentCcmsHelper.class.getName()
			);

	@Autowired
	private ConsentService consentService;

	public ConsentWriteLog writeConsent(
			ConsentWriteInput input) {

		try {
			validateInput(input);

			ConsentWriteLog consentWrite =
					consentService
							.generateConsentWriteRequest(input);

			consentWrite =
					consentService.writeConsentToCCMS(
							consentWrite,
							input.getLoanTypeId()
					);

			if (!isSuccessful(consentWrite)) {
				logger.info(
						"CCMS consent write unsuccessful. "
						+ "loanTypeId={}, ntbId={}",
						input.getLoanTypeId(),
						input.getNtbId()
				);
			}

			return consentWrite;

		} catch (Exception e) {
			logger.info(
					"Exception in common CCMS consent helper",
					e
			);
			return null;
		}
	}

	public boolean isSuccessful(
			ConsentWriteLog consentWrite) {

		return consentWrite != null
				&& "200".equals(
						consentWrite.getResponseCode()
				)
				&& "true".equalsIgnoreCase(
						consentWrite.getResponseStatus()
				)
				&& "Y".equalsIgnoreCase(
						consentWrite.getConsentWriteIsActive()
				)
				&& ValidatorUtil.isValid(
						consentWrite
								.getConsentWriteConsentId()
				);
	}

	private void validateInput(
			ConsentWriteInput input) {

		if (input == null) {
			throw new IllegalArgumentException(
					"Consent input cannot be null"
			);
		}

		if (!ValidatorUtil.isValid(input.getNtbId())) {
			throw new IllegalArgumentException(
					"NTB ID is mandatory"
			);
		}

		if (!ValidatorUtil.isValid(input.getMobile())) {
			throw new IllegalArgumentException(
					"Mobile number is mandatory"
			);
		}

		if (!ValidatorUtil.isValid(input.getLocale())) {
			input.setLocale("eng");
		}

		if (!ValidatorUtil.isValid(
				input.getProductCode())) {

			throw new IllegalArgumentException(
					"Product code is mandatory"
			);
		}

		if (input.getLoanTypeId() == null) {
			throw new IllegalArgumentException(
					"Loan type ID is mandatory"
			);
		}
	}
}
```

This is the only class that calls:

```java
generateConsentWriteRequest(...)
writeConsentToCCMS(...)
```

No loan-specific manager should duplicate the CCMS API-call code.

---

# 8. `ConsentUtil.java`

Replace old no-argument write method:

```java
public void callCCMSConsentWriteAPI()
```

with:

```java
public ConsentWriteLog callCCMSConsentWriteAPI(
		ConsentWriteInput input) {

	try {
		ConsentWriteLog consentWrite =
				consentService
						.generateConsentWriteRequest(input);

		return consentService.writeConsentToCCMS(
				consentWrite,
				input.getLoanTypeId()
		);

	} catch (Exception e) {
		e.printStackTrace();
		return null;
	}
}
```

Add import:

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
```

However, if every loan now uses `LoanConsentCcmsHelper`, this `ConsentUtil` method is not needed in loan process managers.

---

# B. Home Loan changes

Home currently copies privacy flag and NTB ID from quote into application, but the commit did not originally copy locale. 

## 9. Home Loan DB columns

Quote table:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_CCMS_CONSENT_ID VARCHAR2(255);
```

Application table:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_CCMS_CONSENT_ID VARCHAR2(255);
```

Do not reuse the existing numeric:

```text
APP_CONSENT_ID
```

---

# 10. `ApplicationFormHomeLoanQuote.java`

Add:

```java
@Column(name="QUOTE_CCMS_CONSENT_ID")
private String quoteCcmsConsentId;
```

Getter/setter:

```java
public String getQuoteCcmsConsentId() {
	return quoteCcmsConsentId;
}

public void setQuoteCcmsConsentId(
		String quoteCcmsConsentId) {
	this.quoteCcmsConsentId = quoteCcmsConsentId;
}
```

---

# 11. `ApplicationFormHomeLoan.java`

Add:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Getter/setter:

```java
public String getAppCcmsConsentId() {
	return appCcmsConsentId;
}

public void setAppCcmsConsentId(
		String appCcmsConsentId) {
	this.appCcmsConsentId = appCcmsConsentId;
}
```

---

# 12. `HomeLoanHelper.java`

Where quote values are copied into application, also add locale:

```java
if (ValidatorUtil.isValid(
		quote.getQuotePrivacyLocale())
		&& !ValidatorUtil.isValid(
				application.getAppPrivacyLocale())) {

	application.setAppPrivacyLocale(
			quote.getQuotePrivacyLocale()
	);
}
```

The existing code already copies consent flag and NTB ID before saving the application. 

---

# 13. `HomeProcessManagerImpl.java`

Add imports:

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.util.LoanConsentCcmsHelper;
```

Add dependency:

```java
@Autowired
private LoanConsentCcmsHelper loanConsentCcmsHelper;
```

Immediately before BRE, after both `quote` and `application` are available, add:

```java
ConsentWriteInput consentInput =
		new ConsentWriteInput();

consentInput.setNtbId(
		quote.getQuoteNtbId()
);

consentInput.setMobile(
		ValidatorUtil.isValid(
				application.getAppMobileNo())
				? application.getAppMobileNo()
				: quote.getAppMobile()
);

consentInput.setEmail(
		ValidatorUtil.isValid(
				application.getAppWorkEmail())
				? application.getAppWorkEmail()
				: quote.getAppEmail()
);

consentInput.setIpAddress(
		this.SbiUtil.getIPAddress()
);

consentInput.setLocale(
		quote.getQuotePrivacyLocale()
);

consentInput.setProductCode("HOME_LOAN");

consentInput.setConsentFlag(
		quote.getQuotePrivacyConsentFlag()
);

consentInput.setLoanTypeId(
		Constants.HOME_LOAN_ID
);

ConsentWriteLog consentWrite =
		loanConsentCcmsHelper.writeConsent(
				consentInput
		);

if (!loanConsentCcmsHelper.isSuccessful(
		consentWrite)) {

	loanScenarioBean.setStatus(
			Integer.valueOf(0)
	);
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. "
			+ "Please try again."
	);
	return loanScenarioBean;
}

String ccmsConsentId =
		consentWrite.getConsentWriteConsentId();

quote.setQuoteCcmsConsentId(
		ccmsConsentId
);

application.setAppCcmsConsentId(
		ccmsConsentId
);

/*
 * Use the existing service save method signatures
 * already used in your project.
 */
quote = homeLoanService.save(quote);
application = homeLoanService.save(application);

if (quote == null || application == null) {
	loanScenarioBean.setStatus(
			Integer.valueOf(0)
	);
	loanScenarioBean.setMessage(
			Constants.SORRY_FOR_INCONVENIENCE
	);
	return loanScenarioBean;
}
```

Then continue existing:

```java
loanScenarioBean =
		this.homeLoanHelper.callBRE(...);
```

---

# C. CVE changes

CVE has no quote entity in the supplied flow. It directly saves `ApplicationFormCveLoan` to `RUPEEPOWER_OCAS_T_00291`. The confirmed save statement is:

```java
cveApp = personalLoanService.save(cveApp);
```



## 14. CVE DB column

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD APP_CCMS_CONSENT_ID VARCHAR2(255);
```

You should already have or add:

```sql
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
```

---

# 15. `ApplicationFormCveLoan.java`

Add:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Getter/setter:

```java
public String getAppCcmsConsentId() {
	return appCcmsConsentId;
}

public void setAppCcmsConsentId(
		String appCcmsConsentId) {
	this.appCcmsConsentId = appCcmsConsentId;
}
```

---

# 16. `CveProcessManagerImpl.java`

Add imports:

```java
import com.mintstreet.consent.bo.ConsentWriteInput;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.util.LoanConsentCcmsHelper;
```

Add dependency:

```java
@Autowired
private LoanConsentCcmsHelper loanConsentCcmsHelper;
```

Your code sets the CVE application data and then saves it using `personalLoanService.save(cveApp)`. 

Before the first save, ensure consent values are copied:

```java
cveApp.setAppPrivacyConsentFlag(
		"Y".equalsIgnoreCase(
				masterCbsCall
						.getAppPrivacyConsentFlag())
				? "Y"
				: "N"
);

if (ValidatorUtil.isValid(
		masterCbsCall.getAppNtbId())) {

	cveApp.setAppNtbId(
			masterCbsCall.getAppNtbId()
	);
}

if (ValidatorUtil.isValid(
		masterCbsCall.getAppPrivacyLocale())) {

	cveApp.setAppPrivacyLocale(
			masterCbsCall
					.getAppPrivacyLocale()
	);
}
```

Keep the existing initial save:

```java
cveApp =
		personalLoanService.save(cveApp);
```

Immediately after that save, add:

```java
if (cveApp == null) {
	cbsCallResponse.setResponseMsg(
			Constants.SORRY_FOR_INCONVENIENCE
	);
	cbsCallResponse.setStatus(0);
	return cbsCallResponse;
}

/*
 * Revocation flow is separate.
 * Call consent write only for normal consent acceptance.
 */
if (!"Y".equalsIgnoreCase(
		cveApp.getCveAppConsentRevokeYes())) {

	ConsentWriteInput consentInput =
			new ConsentWriteInput();

	consentInput.setNtbId(
			cveApp.getAppNtbId()
	);

	consentInput.setMobile(
			cveApp.getCbsMobileNumber()
	);

	consentInput.setEmail(
			cveApp.getCveAppEmail()
	);

	consentInput.setIpAddress(
			this.SbiUtil.getIPAddress()
	);

	consentInput.setLocale(
			cveApp.getAppPrivacyLocale()
	);

	/*
	 * CVE has multiple selectable product categories.
	 * Use the selected CVE CRM/product category code.
	 */
	consentInput.setProductCode(
			cveApp.getCveProductCategory()
	);

	consentInput.setConsentFlag(
			cveApp.getAppPrivacyConsentFlag()
	);

	consentInput.setLoanTypeId(
			Constants.CVE_ID
	);

	ConsentWriteLog consentWrite =
			loanConsentCcmsHelper.writeConsent(
					consentInput
			);

	if (!loanConsentCcmsHelper.isSuccessful(
			consentWrite)) {

		cbsCallResponse.setResponseMsg(
				"Unable to write consent to CCMS. "
				+ "Please try again."
		);
		cbsCallResponse.setStatus(0);
		return cbsCallResponse;
	}

	cveApp.setAppCcmsConsentId(
			consentWrite
					.getConsentWriteConsentId()
	);

	cveApp =
			personalLoanService.save(cveApp);

	if (cveApp == null) {
		cbsCallResponse.setResponseMsg(
				Constants.SORRY_FOR_INCONVENIENCE
		);
		cbsCallResponse.setStatus(0);
		return cbsCallResponse;
	}
}
```

Then continue your existing session and OTP flow:

```java
SessionUtil.setPersonalLoanTypeSequenceId(
		cveApp.getAppSeqId()
);
```

---

# D. Auto Loan changes

Use the same common helper. Only the table/entity names change.

## 17. Auto DB

Add to Auto quote table:

```sql
ALTER TABLE <AUTO_QUOTE_TABLE>
ADD QUOTE_CCMS_CONSENT_ID VARCHAR2(255);
```

Add to Auto application table:

```sql
ALTER TABLE <AUTO_APPLICATION_TABLE>
ADD APP_CCMS_CONSENT_ID VARCHAR2(255);
```

Do not reuse an existing numeric consent-master field.

---

# 18. Auto quote entity

Add:

```java
@Column(name="QUOTE_CCMS_CONSENT_ID")
private String quoteCcmsConsentId;
```

Getter/setter:

```java
public String getQuoteCcmsConsentId() {
	return quoteCcmsConsentId;
}

public void setQuoteCcmsConsentId(
		String quoteCcmsConsentId) {
	this.quoteCcmsConsentId = quoteCcmsConsentId;
}
```

---

# 19. Auto application entity

Add:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Getter/setter:

```java
public String getAppCcmsConsentId() {
	return appCcmsConsentId;
}

public void setAppCcmsConsentId(
		String appCcmsConsentId) {
	this.appCcmsConsentId = appCcmsConsentId;
}
```

---

# 20. Auto process manager

After quote and application exist, before BRE/offer:

```java
ConsentWriteInput consentInput =
		new ConsentWriteInput();

consentInput.setNtbId(
		autoQuote.getQuoteNtbId()
);

consentInput.setMobile(
		autoApplication.getAppMobileNo()
);

consentInput.setEmail(
		autoApplication.getAppEmailId()
);

consentInput.setIpAddress(
		this.SbiUtil.getIPAddress()
);

consentInput.setLocale(
		autoQuote.getQuotePrivacyLocale()
);

consentInput.setProductCode("AUTO_LOAN");

consentInput.setConsentFlag(
		autoQuote.getQuotePrivacyConsentFlag()
);

consentInput.setLoanTypeId(
		Constants.AUTO_LOAN_ID
);

ConsentWriteLog consentWrite =
		loanConsentCcmsHelper.writeConsent(
				consentInput
		);

if (!loanConsentCcmsHelper.isSuccessful(
		consentWrite)) {

	loanScenarioBean.setStatus(0);
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. "
			+ "Please try again."
	);
	return loanScenarioBean;
}

String ccmsConsentId =
		consentWrite.getConsentWriteConsentId();

autoQuote.setQuoteCcmsConsentId(
		ccmsConsentId
);

autoApplication.setAppCcmsConsentId(
		ccmsConsentId
);

autoQuote =
		autoLoanService.save(autoQuote);

autoApplication =
		autoLoanService.save(autoApplication);
```

Replace getter names only where your actual Auto entity names differ.

---

# E. Personal Loan changes

## 21. Personal Loan DB

```sql
ALTER TABLE <PERSONAL_QUOTE_TABLE>
ADD QUOTE_CCMS_CONSENT_ID VARCHAR2(255);

ALTER TABLE <PERSONAL_APPLICATION_TABLE>
ADD APP_CCMS_CONSENT_ID VARCHAR2(255);
```

---

# 22. Personal entities

Quote:

```java
@Column(name="QUOTE_CCMS_CONSENT_ID")
private String quoteCcmsConsentId;
```

Application:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Add standard getters/setters.

---

# 23. Personal Process Manager

After quote/application save and before BRE:

```java
ConsentWriteInput consentInput =
		new ConsentWriteInput();

consentInput.setNtbId(
		personalQuote.getQuoteNtbId()
);

consentInput.setMobile(
		personalApplication.getAppMobileNo()
);

consentInput.setEmail(
		personalApplication.getAppEmailId()
);

consentInput.setIpAddress(
		this.SbiUtil.getIPAddress()
);

consentInput.setLocale(
		personalQuote.getQuotePrivacyLocale()
);

consentInput.setProductCode(
		"PERSONAL_LOAN"
);

consentInput.setConsentFlag(
		personalQuote
				.getQuotePrivacyConsentFlag()
);

consentInput.setLoanTypeId(
		Constants.PERSONAL_LOAN_ID
);

ConsentWriteLog consentWrite =
		loanConsentCcmsHelper.writeConsent(
				consentInput
		);

if (!loanConsentCcmsHelper.isSuccessful(
		consentWrite)) {

	loanScenarioBean.setStatus(0);
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. "
			+ "Please try again."
	);
	return loanScenarioBean;
}

String ccmsConsentId =
		consentWrite.getConsentWriteConsentId();

personalQuote.setQuoteCcmsConsentId(
		ccmsConsentId
);

personalApplication.setAppCcmsConsentId(
		ccmsConsentId
);

personalQuote =
		personalLoanService.save(personalQuote);

personalApplication =
		personalLoanService.save(
				personalApplication
	);
```

---

# F. Common duplicate-call protection

Get Quote or OTP methods may be called again. Do not call CCMS repeatedly when a successful consent ID already exists.

Before calling the helper in each product, add:

```java
if (ValidatorUtil.isValid(
		application.getAppCcmsConsentId())) {

	logger.info(
			"CCMS consent already written. "
			+ "Skipping duplicate API call. consentId={}",
			application.getAppCcmsConsentId()
	);

	// Continue normal processing.
} else {
	// Build ConsentWriteInput and call common helper.
}
```

For quote-based products, also check quote:

```java
if (ValidatorUtil.isValid(
		quote.getQuoteCcmsConsentId())) {

	application.setAppCcmsConsentId(
			quote.getQuoteCcmsConsentId()
	);

	application =
			loanService.save(application);

	// Skip CCMS API.
} else {
	// Call common helper.
}
```

This protects production from double-clicks, retries, OTP resends, and repeated state-manager calls.

---

# G. Spring wiring

Ensure these beans exist only once:

```xml
<bean id="consentWriteDao"
	class="com.mintstreet.consent.dao.ConsentWriteDao">
	<property name="entityManagerFactory"
		ref="entityManagerFactory" />
</bean>

<bean id="loanConsentCcmsHelper"
	class="com.mintstreet.consent.util.LoanConsentCcmsHelper" />
```

If `ConsentService` uses setter injection:

```xml
<property name="consentWriteDao"
	ref="consentWriteDao" />
```

No separate helper bean is required per product.

---

# Final common flow

```text
Loan-specific frontend
        ↓
Loan-specific quote/application save
        ↓
Build ConsentWriteInput
        ↓
One common LoanConsentCcmsHelper
        ↓
One common ConsentService
        ↓
Save request in ConsentWriteLog
        ↓
Call EIS/CCMS
        ↓
200 + success=true + body.consentId present
        ↓
Update ConsentWriteLog
        ↓
Return populated ConsentWriteLog
        ↓
Loan process manager updates its own quote/app table
```

The common service never imports Home, Auto, Personal, or CVE entity classes. That is what prevents product coupling and code duplication.
