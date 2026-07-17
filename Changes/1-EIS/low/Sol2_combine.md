Your existing response parsing is already successful. The remaining work is:

1. Pass `loanTypeId` dynamically from `processGetQuotes(...)`.
2. Store it in `CONSENT_WRITE_LOAN_TYPE` when the request log is created.
3. On CCMS `200 + success=true`, extract `consentId`.
4. Update the same `ConsentWriteLog` row with:

   * `CONSENT_WRITE_CONSENT_ID`
   * `CONSENT_WRITE_IS_ACTIVE = 'Y'`
5. Update Quote and Application with the CCMS consent UUID.

One important point: your existing `loanQuoteConsentId` and `appConsentId` appear to hold the old numeric consent-master ID. Your current quote insertion loads that value using `commonService.getConsentIdByLoanType(...)`, which returns an `Integer`. 

Therefore, **do not overwrite those fields with the CCMS UUID**:

```text
019f6b4f-399a-71be-85a3-46cebcb4f751
```

Create separate `String` fields in Quote and Application.

# Files to change

```text
1. ConsentService.java
2. ConsentUtil.java
3. HomeProcessManagerImpl.java
4. ApplicationFormHomeLoanQuote.java
5. ApplicationFormHomeLoan.java
6. Database tables
```

Your `ConsentWriteLog.java` already contains all required fields:

```java
private String ntbNumber;
private String consentId;
private String isActive;
private String loanType;
```

So no entity change is needed there.

---

# 1. Database changes

## 1.1 Consent Write log table

Your entity already maps these columns:

```text
CONSENT_WRITE_CONSENT_ID
CONSENT_WRITE_IS_ACTIVE
CONSENT_WRITE_LOAN_TYPE
```

Only verify they exist:

```sql
SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'RUPEEPOWER_OCAS_T_13701'
AND COLUMN_NAME IN (
    'CONSENT_WRITE_CONSENT_ID',
    'CONSENT_WRITE_IS_ACTIVE',
    'CONSENT_WRITE_LOAN_TYPE',
    'CONSENT_WRITE_NTB_NUMBER'
);
```

If missing:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_CONSENT_ID VARCHAR2(100);

ALTER TABLE RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_IS_ACTIVE VARCHAR2(1);

ALTER TABLE RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_LOAN_TYPE VARCHAR2(20);
```

---

## 1.2 Quote table

Add a separate CCMS consent-ID column:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_00195
ADD QUOTE_CCMS_CONSENT_ID VARCHAR2(100);
```

Use your actual Quote table name if different.

---

## 1.3 Application table

Add:

```sql
ALTER TABLE <HOME_LOAN_APPLICATION_TABLE>
ADD APP_CCMS_CONSENT_ID VARCHAR2(100);
```

Use the actual table name mapped by `ApplicationFormHomeLoan`.

---

# 2. `ApplicationFormHomeLoanQuote.java`

Add this field near your privacy fields:

```java
@Column(name = "QUOTE_CCMS_CONSENT_ID")
private String quoteCcmsConsentId;
```

Add getter/setter:

```java
public String getQuoteCcmsConsentId() {
	return quoteCcmsConsentId;
}

public void setQuoteCcmsConsentId(String quoteCcmsConsentId) {
	this.quoteCcmsConsentId = quoteCcmsConsentId;
}
```

Do not replace or modify:

```java
private Integer loanQuoteConsentId;
```

That field is already being used for the existing numeric consent master.

---

# 3. `ApplicationFormHomeLoan.java`

Add:

```java
@Column(name = "APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Add getter/setter:

```java
public String getAppCcmsConsentId() {
	return appCcmsConsentId;
}

public void setAppCcmsConsentId(String appCcmsConsentId) {
	this.appCcmsConsentId = appCcmsConsentId;
}
```

Do not overwrite the existing `appConsentId`, because your current code copies the old numeric Quote consent ID into `application.setAppConsentId(...)`. 

---

# 4. `ConsentService.java`

## 4.1 Change the current method signature

Your current method is:

```java
public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) {
```

Change to:

```java
public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale,
		Integer loanTypeId) {
```

---

## 4.2 Validate the dynamic loan type

At the beginning of the method, add:

```java
if (loanTypeId == null) {
	throw new IllegalArgumentException(
			"Loan type ID is required for CCMS Consent Write."
	);
}
```

---

## 4.3 Store dynamic loan type when request log is created

At the bottom of this method, you currently have:

```java
ConsentWriteLog consentWrite = new ConsentWriteLog();

consentWrite.setxCorrelationId(correlationId);
consentWrite.setNtbNumber(ntbId);
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());
```

Add:

```java
consentWrite.setLoanType(String.valueOf(loanTypeId));
```

Final block:

```java
ConsentWriteLog consentWrite = new ConsentWriteLog();

consentWrite.setxCorrelationId(correlationId);
consentWrite.setNtbNumber(ntbId);
consentWrite.setLoanType(String.valueOf(loanTypeId));
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());

logger.info(
		"Saving CCMS Consent Write request log. "
		+ "correlationId: {}, ntbId: {}, loanTypeId: {}",
		correlationId,
		ntbId,
		loanTypeId
);

consentWrite = consentWriteDao.save(
		consentWrite.getConsentWriteId(),
		consentWrite
);

if (consentWrite == null
		|| consentWrite.getConsentWriteId() == null) {

	throw new RuntimeException(
			"Unable to save CCMS Consent Write request log."
	);
}

return consentWrite;
```

Your other overload already stores `loanType` when creating the request log. 

---

## 4.4 Update `writeConsentToCCMS(...)`

Your current method saves the normal response fields before checking business success. 

Find this section:

```java
String responseCode =
		String.valueOf(
				writeResponse.getEisResponse().getStatusCode()
		);

String responseStatus =
		String.valueOf(
				writeResponse.getEisResponse().getSuccess()
		);

consentWrite.setResponseCode(responseCode);
consentWrite.setResponseStatus(responseStatus);
consentWrite.setResponseMsg(responseStr);
consentWrite.setResponseEntryTime(new Date());

if (consentWrite.getNtbNumber() == null
		|| consentWrite.getNtbNumber().trim().isEmpty()) {

	logger.warn(
			"NTB ID is missing in Consent Write log. writeId: {}",
			consentWrite.getConsentWriteId()
	);
}

consentWrite = consentWriteDao.save(
		consentWrite.getConsentWriteId(),
		consentWrite
);

boolean success =
		"200".equals(responseCode)
		&& "true".equalsIgnoreCase(responseStatus);
```

Replace that entire section with:

```java
String responseCode =
		String.valueOf(
				writeResponse.getEisResponse().getStatusCode()
		);

String responseStatus =
		String.valueOf(
				writeResponse.getEisResponse().getSuccess()
		);

boolean success =
		"200".equals(responseCode)
		&& "true".equalsIgnoreCase(responseStatus);

consentWrite.setResponseCode(responseCode);
consentWrite.setResponseStatus(responseStatus);
consentWrite.setResponseMsg(responseStr);
consentWrite.setResponseEntryTime(new Date());

if (consentWrite.getNtbNumber() == null
		|| consentWrite.getNtbNumber().trim().isEmpty()) {

	logger.warn(
			"NTB ID is missing in Consent Write log. writeId: {}",
			consentWrite.getConsentWriteId()
	);
}

if (success) {

	if (writeResponse.getEisResponse().getEisBody() == null) {

		throw new RuntimeException(
				"CCMS Consent Write returned success but response body is null."
		);
	}

	String consentId =
			writeResponse
					.getEisResponse()
					.getEisBody()
					.getConsentId();

	if (consentId == null
			|| consentId.trim().isEmpty()) {

		throw new RuntimeException(
				"CCMS Consent Write returned success but consentId is empty."
		);
	}

	String responseNtbId =
			writeResponse
					.getEisResponse()
					.getEisBody()
					.getNtbId();

	consentWrite.setConsentId(consentId.trim());
	consentWrite.setIsActive("Y");

	/*
	 * Prefer the NTB ID returned by CCMS when present.
	 */
	if (responseNtbId != null
			&& !responseNtbId.trim().isEmpty()) {

		consentWrite.setNtbNumber(
				responseNtbId.trim()
		);
	}

	logger.info(
			"CCMS Consent Write successful. "
			+ "writeId: {}, consentId: {}, ntbId: {}, loanType: {}",
			consentWrite.getConsentWriteId(),
			consentWrite.getConsentId(),
			consentWrite.getNtbNumber(),
			consentWrite.getLoanType()
	);

} else {

	/*
	 * A failed response must not be marked active.
	 */
	consentWrite.setIsActive("N");

	logger.warn(
			"CCMS Consent Write failed. "
			+ "writeId: {}, responseCode: {}, responseStatus: {}",
			consentWrite.getConsentWriteId(),
			responseCode,
			responseStatus
	);
}

/*
 * One save updates response and success-specific fields together.
 */
consentWrite = consentWriteDao.save(
		consentWrite.getConsentWriteId(),
		consentWrite
);

return consentWrite;
```

### Important DTO naming

Your older implementation already used:

```java
writeResponse.getEisResponse().getEisBody().getConsentId();
```

for extracting `consentId`. 

Therefore, use `getEisBody()` based on your existing response BO.

If your final BO renamed it to `getBody()`, only change:

```java
getEisBody()
```

to:

```java
getBody()
```

Do not use raw JSON parsing when your DTO already maps this body.

---

# 5. `ConsentUtil.java`

Your current NTB overload returns `void` and does not receive loan type:

```java
public void callCCMSConsentWriteAPI(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) throws Exception
```

Change it to return `ConsentWriteLog` and receive `loanTypeId`.

Replace the whole method with:

```java
public ConsentWriteLog callCCMSConsentWriteAPI(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale,
		Integer loanTypeId) throws Exception {

	try {

		ConsentWriteLog consentWrite =
				consentService.generateConsentWriteRequest(
						ntbId,
						mobile,
						email,
						ipAddress,
						locale,
						loanTypeId
				);

		return consentService.writeConsentToCCMS(
				consentWrite
		);

	} catch (JSONException e) {

		throw e;
	}
}
```

Do not swallow the error and return success. Let the caller stop the loan flow.

You can leave this existing CIF overload unchanged:

```java
public ConsentWriteLog callCCMSConsentWriteAPI(
		ConsentRequestDpData dpData,
		List<ConsentRequestConsent> consents,
		String loanType)
```

because it already passes and stores `loanType`.

---

# 6. `HomeProcessManagerImpl.java`

## 6.1 Change method signature to receive loan type dynamically

Current:

```java
private boolean writePrivacyConsentToCCMS(
		ApplicationFormHomeLoan application,
		ApplicationFormHomeLoanQuote quote,
		LoanScenarioBean loanScenarioBean) {
```

Replace with:

```java
private boolean writePrivacyConsentToCCMS(
		ApplicationFormHomeLoan application,
		ApplicationFormHomeLoanQuote quote,
		LoanScenarioBean loanScenarioBean,
		Integer loanTypeId) {
```

---

## 6.2 Validate loan type

At the start of the method, after checking application/quote, add:

```java
if (loanTypeId == null) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Unable to identify the loan type for consent."
	);
	return false;
}
```

---

## 6.3 Replace direct service calls

Your current code directly calls:

```java
ConsentWriteLog consentWrite =
		consentService.generateConsentWriteRequest(
				quote.getQuoteNtbId(),
				mobile,
				email,
				ipAddress,
				quote.getQuotePrivacyLocale()
		);

consentWrite =
		consentService.writeConsentToCCMS(
				consentWrite
		);
```

This current location is visible in your uploaded implementation. 

Replace with:

```java
ConsentWriteLog consentWrite =
		consentUtil.callCCMSConsentWriteAPI(
				quote.getQuoteNtbId(),
				mobile,
				email,
				ipAddress,
				quote.getQuotePrivacyLocale(),
				loanTypeId
		);
```

---

## 6.4 Keep the response success check

```java
if (consentWrite == null
		|| !"true".equalsIgnoreCase(
				consentWrite.getResponseStatus()
		)
		|| !"200".equalsIgnoreCase(
				consentWrite.getResponseCode()
		)
		|| consentWrite.getConsentId() == null
		|| consentWrite.getConsentId().trim().isEmpty()
		|| !"Y".equalsIgnoreCase(
				consentWrite.getIsActive()
		)) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. Please try again."
	);

	return false;
}
```

---

## 6.5 Update Quote and Application after success

Immediately after the success validation, add:

```java
String ccmsConsentId =
		consentWrite.getConsentId().trim();

quote.setQuoteCcmsConsentId(
		ccmsConsentId
);

application.setAppCcmsConsentId(
		ccmsConsentId
);

quote = homeLoanService.save(quote);
application = homeLoanService.save(application);

if (quote == null || application == null) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Consent was recorded, but application details could not be updated."
	);

	return false;
}

logger.info(
		"CCMS consent ID saved in Quote and Application. "
		+ "consentId: {}, quoteId: {}, appSeqId: {}, loanTypeId: {}",
		ccmsConsentId,
		quote.getLoanQuoteId(),
		application.getAppSeqId(),
		loanTypeId
);

return true;
```

Your `HomeLoanService` already provides overloaded `save(...)` methods for both Quote and Application. 

### Final method ending

The final successful section should be:

```java
if (consentWrite == null
		|| !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
		|| !"200".equalsIgnoreCase(consentWrite.getResponseCode())
		|| consentWrite.getConsentId() == null
		|| consentWrite.getConsentId().trim().isEmpty()
		|| !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. Please try again."
	);

	return false;
}

String ccmsConsentId =
		consentWrite.getConsentId().trim();

quote.setQuoteCcmsConsentId(ccmsConsentId);
application.setAppCcmsConsentId(ccmsConsentId);

quote = homeLoanService.save(quote);
application = homeLoanService.save(application);

if (quote == null || application == null) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Consent was recorded, but application details could not be updated."
	);

	return false;
}

logger.info(
		"CCMS consent persistence completed. "
		+ "writeId: {}, consentId: {}, quoteId: {}, "
		+ "appSeqId: {}, loanTypeId: {}",
		consentWrite.getConsentWriteId(),
		ccmsConsentId,
		quote.getLoanQuoteId(),
		application.getAppSeqId(),
		loanTypeId
);

return true;
```

---

# 7. Change the call in `processGetQuotes(...)`

Your `processGetQuotes(...)` already receives:

```java
Integer loanTypeId
```

in its method signature. 

Current call:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean
		);
```

Replace with:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean,
				loanTypeId
		);
```

This fetches the loan type dynamically from the current Get Quote flow. Do not hardcode `HOME_LOAN_ID`.

---

# 8. Remove the forced-success testing line

Your current `processGetQuotes(...)` contains:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean
		);

ccmsWriteStatus = true;
```

That second line bypasses every CCMS failure. It is present in the supplied code. 

Delete:

```java
ccmsWriteStatus = true;
```

Final code:

```java
if (!(application.getAppMobileVerified() != null
		&& application.getAppMobileVerified().equalsIgnoreCase("Y"))) {

	boolean ccmsWriteStatus =
			writePrivacyConsentToCCMS(
					application,
					quote,
					loanScenarioBean,
					loanTypeId
			);

	if (!ccmsWriteStatus) {
		return loanScenarioBean;
	}

	SessionUtil.setConsentSubmitNTBHome("true");
}
```

Set the session flag only after successful persistence.

---

# 9. Final database result

After successful response, the same `RUPEEPOWER_OCAS_T_13701` row will contain:

```text
CONSENT_WRITE_RESPONSE_CODE    = 200
CONSENT_WRITE_RESPONSE_STATUS  = true
CONSENT_WRITE_CONSENT_ID       = 019f6b4f-399a-71be-85a3-46cebcb4f751
CONSENT_WRITE_IS_ACTIVE        = Y
CONSENT_WRITE_LOAN_TYPE        = dynamically received loanTypeId
CONSENT_WRITE_NTB_NUMBER       = 9619155147290620081
```

Quote table:

```text
QUOTE_CCMS_CONSENT_ID =
019f6b4f-399a-71be-85a3-46cebcb4f751
```

Application table:

```text
APP_CCMS_CONSENT_ID =
019f6b4f-399a-71be-85a3-46cebcb4f751
```

## Final flow

```text
processGetQuotes(..., loanTypeId)
→ writePrivacyConsentToCCMS(..., loanTypeId)
→ generateConsentWriteRequest(..., loanTypeId)
→ request log stores NTB ID + loan type
→ CCMS Write API
→ statusCode 200 and success true
→ extract consentId
→ update ConsentWriteLog: consentId + active Y
→ update Quote CCMS consent ID
→ update Application CCMS consent ID
→ continue BRE
```

For stronger transactional consistency, the Quote and Application saves should ideally participate in the same Spring transaction. With your current architecture, the code above is the smallest safe change without moving CCMS integration into the loan service layer.
