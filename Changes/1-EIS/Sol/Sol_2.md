Below changes use your existing BOs:

* `ConsentResponsePurposeEnquiry` has `EIS_RESPONSE` mapping. 
* `ConsentResponsePurposes` already has `code`, `version`, `isProductSpecific`, `bankProducts`. 
* `ConsentRequestPayloadBody` already supports `dpData` and `consents`. 

## File 1: `ConsentResponsePurposeEisResponse.java`

Create/update this file:

```java
package com.mintstreet.consent.bo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ConsentResponsePurposeEisResponse {

	private String success;
	private String correlationId;
	private String locale;
	private String messages;
	private String errors;
	private Integer statusCode;
	private String timestamp;

	@SerializedName("body")
	private List<ConsentResponsePurposeEisBody> body;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<ConsentResponsePurposeEisBody> getBody() {
		return body;
	}

	public void setBody(List<ConsentResponsePurposeEisBody> body) {
		this.body = body;
	}
}
```

---

## File 2: `ConsentResponsePurposeEisBody.java`

Create/update:

```java
package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentResponsePurposeEisBody {

	private String isContainer;
	private String containerTitle;
	private String containerDescription;
	private List<ConsentResponsePurposes> purposes;

	public String getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(String isContainer) {
		this.isContainer = isContainer;
	}

	public String getContainerTitle() {
		return containerTitle;
	}

	public void setContainerTitle(String containerTitle) {
		this.containerTitle = containerTitle;
	}

	public String getContainerDescription() {
		return containerDescription;
	}

	public void setContainerDescription(String containerDescription) {
		this.containerDescription = containerDescription;
	}

	public List<ConsentResponsePurposes> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<ConsentResponsePurposes> purposes) {
		this.purposes = purposes;
	}
}
```

---

## File 3: `ConsentRequestPayloadBody.java`

Add only this alias method, because your code uses `setDpCIF(...)` also:

```java
public String getDpCIF() {
	return dpCif;
}

public void setDpCIF(String dpCif) {
	this.dpCif = dpCif;
}
```

Keep existing `getDpCif()` and `setDpCif()`.

---

## File 4: `ConsentService.java`

### 4.1 Add imports

```java
import com.mintstreet.consent.bo.ConsentResponsePurposeEisBody;
import com.mintstreet.consent.bo.ConsentResponsePurposes;
```

---

### 4.2 Change `readPurposeFromCCMS`

Replace:

```java
public void readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {
```

with:

```java
public ConsentResponsePurposeEnquiry readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {
```

At end of method, replace:

```java
consentPurposeDao.save(purpose.getPurposeId(), purpose);
```

with:

```java
consentPurposeDao.save(purpose.getPurposeId(), purpose);

return purposeResponse;
```

---

### 4.3 Add this helper below `readPurposeFromCCMS`

```java
private ConsentResponsePurposeEnquiry getPurposeResponseForWrite() throws JSONException {

	ConsentPurposeLog purposeLog = generatePurposeRequest();

	ConsentResponsePurposeEnquiry purposeResponse =
			readPurposeFromCCMS(purposeLog);

	if (purposeResponse == null
			|| purposeResponse.getEisResponse() == null
			|| purposeResponse.getEisResponse().getBody() == null
			|| purposeResponse.getEisResponse().getBody().isEmpty()) {

		throw new RuntimeException("Purpose Enquiry response body is empty.");
	}

	return purposeResponse;
}
```

---

### 4.4 Add this helper below that

```java
private List<ConsentRequestConsent> prepareConsentsFromPurposeResponse(
		ConsentResponsePurposeEnquiry purposeResponse,
		String productCode) {

	List<ConsentRequestConsent> consents =
			new ArrayList<ConsentRequestConsent>();

	for (ConsentResponsePurposeEisBody body :
			purposeResponse.getEisResponse().getBody()) {

		if (body == null || body.getPurposes() == null) {
			continue;
		}

		for (ConsentResponsePurposes purpose : body.getPurposes()) {

			if (purpose == null
					|| purpose.getCode() == null
					|| purpose.getVersion() == null) {
				continue;
			}

			ConsentRequestConsent consent =
					new ConsentRequestConsent();

			consent.setPurposeCode(purpose.getCode());
			consent.setPurposeVersion(purpose.getVersion());
			consent.setConsented("true");

			if ("true".equalsIgnoreCase(purpose.getIsProductSpecific())) {

				List<String> consentedProducts =
						new ArrayList<String>();

				consentedProducts.add(productCode);

				consent.setConsentedProducts(consentedProducts);

				// Enable only if this setter exists:
				// consent.setNotConsentedProducts(new ArrayList<String>());
			}

			consents.add(consent);
		}
	}

	return consents;
}
```

---

### 4.5 Update `generateConsentWriteRequest(...)`

Inside:

```java
public ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale)
```

Remove this block:

```java
ConsentRequestConsent consent1 = new ConsentRequestConsent();

List<ConsentRequestConsent> consents = new ArrayList<>();

consent1.setPurposeCode("PR-FACILITA-000099");
consent1.setPurposeVersion("1");
consent1.setConsented("true");

consents.add(consent1);
```

Add this instead:

```java
ConsentResponsePurposeEnquiry purposeResponse;

try {
	purposeResponse = getPurposeResponseForWrite();
} catch (JSONException e) {
	throw new RuntimeException("Unable to get Purpose Enquiry response from CCMS.", e);
}

List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse,
				"HOME_LOAN"
		);

if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purpose found from CCMS Purpose Enquiry response.");
}
```

Keep this unchanged:

```java
payloadBody.setConsents(consents);
```

---

## What will happen now

```text
Get Quote
→ generateConsentWriteRequest()
→ generatePurposeRequest()
→ READ_PURPOSES API
→ save Purpose request/response log
→ parse EIS_RESPONSE.body[].purposes[]
→ create consents[]
→ set into Write API BODY
→ save Write request log
→ UPDATE_CONSENTS API
→ save Write response log
```

Final Write request will now get purpose codes dynamically from CCMS instead of hardcoded `PR-FACILITA-000099`.
