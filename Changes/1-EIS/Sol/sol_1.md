Your current code already has:

* `generatePurposeRequest()` creating `READ_PURPOSES` request and saving request log. 
* `readPurposeFromCCMS()` calling EIS and saving Purpose response. 
* `generateConsentWriteRequest(...)` still hardcoding only `PR-FACILITA-000099`. 

So only change needed: **call Purpose Enquiry inside Write generation and convert `EIS_RESPONSE.body[].purposes[]` into Write API `consents[]`.**

## 1. Add alias setter in `ConsentRequestPayloadBody.java`

Because your service uses `setDpCIF(...)`, add this method also:

```java
public void setDpCIF(String dpCif) {
	this.dpCif = dpCif;
}

public String getDpCIF() {
	return dpCif;
}
```

Keep existing `setDpCif(...)` also.

---

## 2. Add / update `ConsentResponsePurposeEisResponse.java`

```java
package com.mintstreet.consent.bo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ConsentResponsePurposeEisResponse {

	private String success;
	private String statusCode;
	private String correlationId;
	private String locale;

	@SerializedName("body")
	private List<ConsentResponsePurposeEisBody> body;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
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

	public List<ConsentResponsePurposeEisBody> getBody() {
		return body;
	}

	public void setBody(List<ConsentResponsePurposeEisBody> body) {
		this.body = body;
	}
}
```

---

## 3. Add `ConsentResponsePurposeEisBody.java`

```java
package com.mintstreet.consent.bo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ConsentResponsePurposeEisBody {

	private String containerDescription;
	private String containerTitle;
	private String isContainer;

	@SerializedName("purposes")
	private List<ConsentResponsePurposes> purposes;

	public String getContainerDescription() {
		return containerDescription;
	}

	public void setContainerDescription(String containerDescription) {
		this.containerDescription = containerDescription;
	}

	public String getContainerTitle() {
		return containerTitle;
	}

	public void setContainerTitle(String containerTitle) {
		this.containerTitle = containerTitle;
	}

	public String getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(String isContainer) {
		this.isContainer = isContainer;
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

## 4. Add `ConsentResponsePurposes.java`

```java
package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentResponsePurposes {

	private String code;
	private String version;
	private String isProductSpecific;
	private String isEssential;
	private List<ConsentResponseBankProducts> bankProducts;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getIsProductSpecific() {
		return isProductSpecific;
	}

	public void setIsProductSpecific(String isProductSpecific) {
		this.isProductSpecific = isProductSpecific;
	}

	public String getIsEssential() {
		return isEssential;
	}

	public void setIsEssential(String isEssential) {
		this.isEssential = isEssential;
	}

	public List<ConsentResponseBankProducts> getBankProducts() {
		return bankProducts;
	}

	public void setBankProducts(List<ConsentResponseBankProducts> bankProducts) {
		this.bankProducts = bankProducts;
	}
}
```

---

## 5. Add `ConsentResponseBankProducts.java`

```java
package com.mintstreet.consent.bo;

public class ConsentResponseBankProducts {

	private String code;
	private String label;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
```

---

## 6. Update imports in `ConsentService.java`

Add these imports:

```java
import com.mintstreet.consent.bo.ConsentResponsePurposeEisBody;
import com.mintstreet.consent.bo.ConsentResponsePurposes;
```

---

## 7. Change `readPurposeFromCCMS(...)`

Replace this:

```java
public void readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {
```

with:

```java
public ConsentResponsePurposeEnquiry readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {
```

At the end, after save:

```java
consentPurposeDao.save(purpose.getPurposeId(), purpose);

return purposeResponse;
```

---

## 8. Add helper method in `ConsentService.java`

Add below `readPurposeFromCCMS(...)`:

```java
private ConsentResponsePurposeEnquiry getPurposeResponseForWrite() throws JSONException {

	ConsentPurposeLog purposeLog = generatePurposeRequest();

	ConsentResponsePurposeEnquiry purposeResponse =
			readPurposeFromCCMS(purposeLog);

	if (purposeResponse == null
			|| purposeResponse.getEisResponse() == null
			|| purposeResponse.getEisResponse().getBody() == null) {

		throw new RuntimeException("Purpose Enquiry response body is empty.");
	}

	return purposeResponse;
}
```

---

## 9. Add conversion helper in `ConsentService.java`

Add below the above helper:

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

				/*
				 * If your ConsentRequestConsent has this setter, enable it:
				 * consent.setNotConsentedProducts(new ArrayList<String>());
				 */
			}

			consents.add(consent);
		}
	}

	return consents;
}
```

---

## 10. Replace hardcoded consent block in `generateConsentWriteRequest(...)`

Inside this method:

```java
public ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale)
```

Remove this full block:

```java
ConsentRequestConsent consent1 = new ConsentRequestConsent();

List<ConsentRequestConsent> consents = new ArrayList<>();

consent1.setPurposeCode("PR-FACILITA-000099");
consent1.setPurposeVersion("1");
consent1.setConsented("true");

consents.add(consent1);
```

Replace with:

```java
ConsentResponsePurposeEnquiry purposeResponse = null;

try {
	purposeResponse = getPurposeResponseForWrite();
} catch (JSONException e) {
	throw new RuntimeException("Unable to read Purpose Enquiry response from CCMS.", e);
}

List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse,
				"HOME_LOAN"
		);

if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from Purpose Enquiry API.");
}
```

Keep this existing line unchanged:

```java
payloadBody.setConsents(consents);
```

---

## Final working flow

```text
generateConsentWriteRequest(...)
 ↓
generatePurposeRequest()
 ↓
READ_PURPOSES call
 ↓
Purpose response saved in ConsentPurposeLog
 ↓
EIS_RESPONSE.body[].purposes[] parsed
 ↓
Each purpose becomes ConsentRequestConsent
 ↓
Product-specific purpose gets consentedProducts=["HOME_LOAN"]
 ↓
payloadBody.setConsents(consents)
 ↓
UPDATE_CONSENTS request saved in ConsentWriteLog
 ↓
writeConsentToCCMS(...)
```

After this, your Write API request will dynamically contain all Purpose Enquiry purpose codes like:

```json
"consents": [
  {
    "purposeCode": "PR-FACILITA-000099",
    "purposeVersion": "1",
    "consented": "true"
  },
  {
    "purposeCode": "PR-INCIDENT-000101",
    "purposeVersion": "2",
    "consented": "true",
    "consentedProducts": ["HOME_LOAN"]
  }
]
```
