Add the NTB ID to `ConsentWriteLog` when creating the Write request, and retain it when the CCMS response is successful.

The best approach is to save `ntbId` in the Write log **before calling CCMS**, so the request can still be traced even when CCMS fails. After receiving HTTP/business success, the same row is updated with response details.

## Files requiring changes

```text
1. Database table for Consent Write Log
2. ConsentWriteLog.java
3. ConsentService.java
```

No change is required in:

```text
CcmsUtil.java
ConsentRequestPayloadBody.java
ConsentRequestDpData.java
Purpose Enquiry response classes
```

---

# 1. Database change

Your consent Write log table appears to be:

```text
RUPEEPOWER_OCAS_T_13701
```

Add an NTB ID column.

### Oracle

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_NTB_ID VARCHAR2(100);
```

You can use a smaller size if the NTB ID has a fixed maximum length:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13701
ADD CONSENT_WRITE_NTB_ID VARCHAR2(50);
```

Verify:

```sql
SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = 'RUPEEPOWER_OCAS_T_13701'
AND COLUMN_NAME = 'CONSENT_WRITE_NTB_ID';
```

Use your actual Write log table name if it is different.

---

# 2. Update `ConsentWriteLog.java`

## 2.1 Add the column field

Inside the `ConsentWriteLog` entity, add:

```java
@Column(name = "CONSENT_WRITE_NTB_ID")
private String consentWriteNtbId;
```

For example:

```java
@Entity
@Table(name = "RUPEEPOWER_OCAS_T_13701")
public class ConsentWriteLog extends Domain<Integer> implements Serializable {

	// Existing fields...

	@Column(name = "CONSENT_WRITE_NTB_ID")
	private String consentWriteNtbId;

	// Existing fields...
}
```

## 2.2 Add getter and setter

```java
public String getConsentWriteNtbId() {
	return consentWriteNtbId;
}

public void setConsentWriteNtbId(String consentWriteNtbId) {
	this.consentWriteNtbId = consentWriteNtbId;
}
```

Use one consistent Java property name throughout:

```text
consentWriteNtbId
```

---

# 3. Update `ConsentService.java`

Your current `generateConsentWriteRequest(...)` creates `ConsentWriteLog` and saves the Write request before calling CCMS. 

## 3.1 Add logger if currently commented out

Your logger is currently commented:

```java
//private static final Logger logger = LogManager.getLogger(ConsentService.class.getName());
```

Replace it with:

```java
private static final Logger logger =
		LogManager.getLogger(ConsentService.class.getName());
```

Add imports if missing:

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
```

---

## 3.2 Update `generateConsentWriteRequest(...)`

Find this existing section:

```java
ConsentWriteLog consentWrite = new ConsentWriteLog();
consentWrite.setxCorrelationId(correlationId);
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());

consentWrite = consentWriteDao.save(
		consentWrite.getConsentWriteId(),
		consentWrite
);

return consentWrite;
```

Replace it with:

```java
ConsentWriteLog consentWrite = new ConsentWriteLog();

consentWrite.setxCorrelationId(correlationId);
consentWrite.setConsentWriteNtbId(ntbId);
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());

logger.info(
		"Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}",
		correlationId,
		maskNtbId(ntbId)
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

logger.info(
		"CCMS Consent Write request log saved. writeId: {}, correlationId: {}",
		consentWrite.getConsentWriteId(),
		correlationId
);

return consentWrite;
```

This saves NTB ID in the Write log before the API call.

---

# 4. Add NTB masking helper

Do not print the full NTB ID in production logs.

Add this private method inside `ConsentService.java`:

```java
private String maskNtbId(String ntbId) {

	if (ntbId == null || ntbId.trim().isEmpty()) {
		return "";
	}

	String value = ntbId.trim();

	if (value.length() <= 4) {
		return "****";
	}

	return "****" + value.substring(value.length() - 4);
}
```

Database stores the complete NTB ID, but application logs display only the last four characters.

---

# 5. Replace `writeConsentToCCMS(...)`

Your current method assumes that `EIS_RESPONSE` and all response fields are always available. 

Replace the complete method with:

```java
public ConsentWriteLog writeConsentToCCMS(
		ConsentWriteLog consentWrite) throws JSONException {

	if (consentWrite == null) {
		throw new IllegalArgumentException(
				"Consent Write log cannot be null."
		);
	}

	if (consentWrite.getConsentWriteId() == null) {
		throw new IllegalArgumentException(
				"Consent Write log ID cannot be null."
		);
	}

	if (consentWrite.getConsentWriteRequest() == null
			|| consentWrite.getConsentWriteRequest().trim().isEmpty()) {

		throw new IllegalArgumentException(
				"Consent Write request cannot be empty."
		);
	}

	logger.info(
			"Calling CCMS Consent Write API. writeId: {}, correlationId: {}, ntbId: {}",
			consentWrite.getConsentWriteId(),
			consentWrite.getxCorrelationId(),
			maskNtbId(consentWrite.getConsentWriteNtbId())
	);

	try {

		JSONObject responseJson =
				ccmsUtil.callingEISServiceForCcms(
						consentWrite.getConsentWriteRequest()
				);

		if (responseJson == null || responseJson.length() == 0) {

			consentWrite.setResponseCode("EMPTY_RESPONSE");
			consentWrite.setResponseStatus("false");
			consentWrite.setResponseMsg(
					"Empty response received from CCMS Consent Write API."
			);
			consentWrite.setResponseEntryTime(new Date());

			consentWriteDao.save(
					consentWrite.getConsentWriteId(),
					consentWrite
			);

			throw new RuntimeException(
					"Empty response received from CCMS Consent Write API."
			);
		}

		String responseStr = responseJson.toString();

		logger.info(
				"CCMS Consent Write response received. writeId: {}, response: {}",
				consentWrite.getConsentWriteId(),
				responseStr
		);

		ConsentResponseConsentWrite writeResponse =
				new ConsentResponseConsentWrite();

		writeResponse =
				(ConsentResponseConsentWrite)
						JSONUtil.getObjctFromJSON(
								writeResponse,
								responseStr
						);

		if (writeResponse == null
				|| writeResponse.getEisResponse() == null) {

			consentWrite.setResponseCode("INVALID_RESPONSE");
			consentWrite.setResponseStatus("false");
			consentWrite.setResponseMsg(responseStr);
			consentWrite.setResponseEntryTime(new Date());

			consentWriteDao.save(
					consentWrite.getConsentWriteId(),
					consentWrite
			);

			throw new RuntimeException(
					"EIS_RESPONSE not found in CCMS Consent Write response."
			);
		}

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

		/*
		 * NTB ID was already saved while creating the Write log.
		 * Retain it while updating the response.
		 */
		if (consentWrite.getConsentWriteNtbId() == null
				|| consentWrite.getConsentWriteNtbId().trim().isEmpty()) {

			logger.warn(
					"NTB ID is missing in Consent Write log. writeId: {}",
					consentWrite.getConsentWriteId()
			);
		}

		consentWrite =
				consentWriteDao.save(
						consentWrite.getConsentWriteId(),
						consentWrite
				);

		boolean success =
				"200".equals(responseCode)
				&& "true".equalsIgnoreCase(responseStatus);

		if (success) {

			logger.info(
					"CCMS Consent Write successful. writeId: {}, correlationId: {}, ntbId: {}",
					consentWrite.getConsentWriteId(),
					consentWrite.getxCorrelationId(),
					maskNtbId(consentWrite.getConsentWriteNtbId())
			);

		} else {

			logger.warn(
					"CCMS Consent Write failed. writeId: {}, responseCode: {}, responseStatus: {}",
					consentWrite.getConsentWriteId(),
					responseCode,
					responseStatus
			);
		}

		return consentWrite;

	} catch (JSONException e) {

		updateConsentWriteFailure(
				consentWrite,
				"JSON_ERROR",
				e.getMessage()
		);

		logger.error(
				"JSON error while processing CCMS Consent Write response. writeId: {}",
				consentWrite.getConsentWriteId(),
				e
		);

		throw e;

	} catch (RuntimeException e) {

		/*
		 * Avoid overwriting a detailed failure already saved above.
		 */
		if (consentWrite.getResponseEntryTime() == null) {

			updateConsentWriteFailure(
					consentWrite,
					"PROCESSING_ERROR",
					e.getMessage()
			);
		}

		logger.error(
				"Error while processing CCMS Consent Write. writeId: {}",
				consentWrite.getConsentWriteId(),
				e
		);

		throw e;
	}
}
```

---

# 6. Add failure-update helper

Add this method inside `ConsentService.java`:

```java
private void updateConsentWriteFailure(
		ConsentWriteLog consentWrite,
		String responseCode,
		String responseMessage) {

	try {

		consentWrite.setResponseCode(responseCode);
		consentWrite.setResponseStatus("false");
		consentWrite.setResponseMsg(responseMessage);
		consentWrite.setResponseEntryTime(new Date());

		consentWriteDao.save(
				consentWrite.getConsentWriteId(),
				consentWrite
		);

	} catch (Exception saveException) {

		logger.error(
				"Unable to update failed CCMS Consent Write log. writeId: {}",
				consentWrite.getConsentWriteId(),
				saveException
		);
	}
}
```

---

# 7. Success check in caller

Your caller can continue using:

```java
if (consentWrite == null
		|| !"true".equalsIgnoreCase(
				consentWrite.getResponseStatus()
		)
		|| !"200".equalsIgnoreCase(
				consentWrite.getResponseCode()
		)) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Unable to write consent to CCMS. Please try again."
	);

	return false;
}
```

You may also confirm NTB ID was persisted:

```java
if (consentWrite.getConsentWriteNtbId() == null
		|| consentWrite.getConsentWriteNtbId().trim().isEmpty()) {

	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage(
			"Unable to store the consent reference details."
	);

	return false;
}
```

---

# 8. Final `generateConsentWriteRequest(...)` log section

The final bottom part of your method should look like this:

```java
JSONObject consentRequest =
		JSONUtil.beanObjectToJSONObjct(request);

ConsentWriteLog consentWrite =
		new ConsentWriteLog();

consentWrite.setxCorrelationId(correlationId);
consentWrite.setConsentWriteNtbId(ntbId);
consentWrite.setConsentWriteRequest(
		consentRequest.toString()
);
consentWrite.setRequestEntryTime(new Date());

consentWrite =
		consentWriteDao.save(
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

## Resulting database lifecycle

```text
Before CCMS call:
CONSENT_WRITE_ID         = generated ID
CONSENT_WRITE_NTB_ID     = customer NTB ID
X_CORRELATION_ID         = request correlation ID
CONSENT_WRITE_REQUEST    = UPDATE_CONSENTS JSON
REQUEST_ENTRY_TIME       = request timestamp

After successful CCMS response:
RESPONSE_CODE            = 200
RESPONSE_STATUS          = true
RESPONSE_MSG             = complete decrypted CCMS response
RESPONSE_ENTRY_TIME      = response timestamp
CONSENT_WRITE_NTB_ID     = remains stored in same row
```

The NTB ID should not be inserted only after success. Saving it before the API call provides complete auditability for successful, rejected, timed-out, and failed requests.
