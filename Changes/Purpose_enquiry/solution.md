Yes. Create **Purpose Enquiry API first**, then use its response to prepare this part dynamically:

```json
"consents": [...]
```

Your existing CCMS Write flow is already backend-based and should remain inside Get Quote backend before BRE, not JS. 

## 1. Create Purpose Enquiry request DTO

### `ConsentPurposeEnquiryRequest.java`

```java
package com.mintstreet.consent.bean;

public class ConsentPurposeEnquiryRequest {

	private String sourceId;
	private ConsentPurposeEnquiryPayload eisPayload;
	private String destination;
	private String transactionType;
	private String transactionSubType;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public ConsentPurposeEnquiryPayload getEisPayload() {
		return eisPayload;
	}

	public void setEisPayload(ConsentPurposeEnquiryPayload eisPayload) {
		this.eisPayload = eisPayload;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionSubType() {
		return transactionSubType;
	}

	public void setTransactionSubType(String transactionSubType) {
		this.transactionSubType = transactionSubType;
	}
}
```

---

## 2. Create Purpose Enquiry payload DTO

### `ConsentPurposeEnquiryPayload.java`

```java
package com.mintstreet.consent.bean;

public class ConsentPurposeEnquiryPayload {

	private ConsentRequestPayloadHeaders headers;
	private ConsentPurposeEnquiryBody body;

	public ConsentRequestPayloadHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(ConsentRequestPayloadHeaders headers) {
		this.headers = headers;
	}

	public ConsentPurposeEnquiryBody getBody() {
		return body;
	}

	public void setBody(ConsentPurposeEnquiryBody body) {
		this.body = body;
	}
}
```

---

## 3. Create Purpose Enquiry body DTO

### `ConsentPurposeEnquiryBody.java`

```java
package com.mintstreet.consent.bean;

public class ConsentPurposeEnquiryBody {

	private String touchPointId;
	private String purposeSetId;

	public String getTouchPointId() {
		return touchPointId;
	}

	public void setTouchPointId(String touchPointId) {
		this.touchPointId = touchPointId;
	}

	public String getPurposeSetId() {
		return purposeSetId;
	}

	public void setPurposeSetId(String purposeSetId) {
		this.purposeSetId = purposeSetId;
	}
}
```

---

## 4. Create response DTOs

### `ConsentPurposeEnquiryResponse.java`

```java
package com.mintstreet.consent.bean;

public class ConsentPurposeEnquiryResponse {

	private ConsentPurposeEnquiryEisResponse eisResponse;

	public ConsentPurposeEnquiryEisResponse getEisResponse() {
		return eisResponse;
	}

	public void setEisResponse(ConsentPurposeEnquiryEisResponse eisResponse) {
		this.eisResponse = eisResponse;
	}
}
```

### `ConsentPurposeEnquiryEisResponse.java`

```java
package com.mintstreet.consent.bean;

import java.util.List;

public class ConsentPurposeEnquiryEisResponse {

	private String statusCode;
	private String success;
	private String message;
	private List<ConsentPurposeContainer> body;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<ConsentPurposeContainer> getBody() {
		return body;
	}

	public void setBody(List<ConsentPurposeContainer> body) {
		this.body = body;
	}
}
```

### `ConsentPurposeContainer.java`

```java
package com.mintstreet.consent.bean;

import java.util.List;

public class ConsentPurposeContainer {

	private Boolean isContainer;
	private String containerTitle;
	private String containerDescription;
	private List<ConsentPurposeMaster> purposes;

	public Boolean getIsContainer() {
		return isContainer;
	}

	public void setIsContainer(Boolean isContainer) {
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

	public List<ConsentPurposeMaster> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<ConsentPurposeMaster> purposes) {
		this.purposes = purposes;
	}
}
```

### `ConsentPurposeMaster.java`

```java
package com.mintstreet.consent.bean;

import java.util.List;

public class ConsentPurposeMaster {

	private String code;
	private Integer version;
	private String title;
	private String description;
	private Boolean isEssential;
	private Boolean isProductSpecific;
	private Boolean isNTB;
	private Boolean isETB;
	private Boolean hasValidity;
	private Integer validityDays;
	private List<String> dataPoints;
	private List<ConsentBankProduct> bankProducts;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Boolean getIsProductSpecific() {
		return isProductSpecific;
	}

	public void setIsProductSpecific(Boolean isProductSpecific) {
		this.isProductSpecific = isProductSpecific;
	}

	public List<ConsentBankProduct> getBankProducts() {
		return bankProducts;
	}

	public void setBankProducts(List<ConsentBankProduct> bankProducts) {
		this.bankProducts = bankProducts;
	}

	// add remaining getters/setters similarly
}
```

### `ConsentBankProduct.java`

```java
package com.mintstreet.consent.bean;

public class ConsentBankProduct {

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

## 5. Add Purpose Enquiry method in `CcmsUtil.java`

```java
public JSONObject callingEISServiceForPurposeEnquiry(String requestJson) {

	JSONObject responseJson = new JSONObject();

	try {
		logger.info("Purpose Enquiry request : " + requestJson);

		responseJson = callingEISServiceForCcms(requestJson);

		logger.info("Purpose Enquiry response : " + responseJson);

	} catch (Exception e) {
		logger.info("Exception in callingEISServiceForPurposeEnquiry", e);
	}

	return responseJson;
}
```

Reuse your existing EIS call logic because CCMS Write is already using `callingEISServiceForCcms(...)`.

---

## 6. Add method in `ConsentService.java`

```java
public ConsentPurposeEnquiryResponse getPurposeEnquiryFromCCMS(String locale) throws Exception {

	CCMSConfig config = commonService.getCcmsConfigById(1);

	if (locale == null || locale.trim().equals("")) {
		locale = "eng";
	}

	String correlationId = UuidV7Generator.generateV7().toString();

	ConsentPurposeEnquiryRequest request = new ConsentPurposeEnquiryRequest();
	ConsentPurposeEnquiryPayload eisPayload = new ConsentPurposeEnquiryPayload();
	ConsentRequestPayloadHeaders headers = new ConsentRequestPayloadHeaders();
	ConsentPurposeEnquiryBody body = new ConsentPurposeEnquiryBody();

	headers.setAcceptLanguage(locale);
	headers.setxCorelationId(correlationId);
	headers.setxApiVersion(config.getApiVersion());

	body.setTouchPointId(config.getTouchPointId());
	body.setPurposeSetId(config.getPurposeSetId());

	eisPayload.setHeaders(headers);
	eisPayload.setBody(body);

	request.setSourceId(config.getSourceId());
	request.setEisPayload(eisPayload);
	request.setDestination(config.getDestination());
	request.setTransactionType(config.getTransactionType());
	request.setTransactionSubType("PURPOSE_ENQUIRY");

	JSONObject requestJson = JSONUtil.beanObjectToJSONObjct(request);

	JSONObject responseJson =
			ccmsUtil.callingEISServiceForPurposeEnquiry(requestJson.toString());

	ConsentPurposeEnquiryResponse response = new ConsentPurposeEnquiryResponse();

	response =
			(ConsentPurposeEnquiryResponse) JSONUtil.getObjctFromJSON(
					response,
					responseJson.toString()
			);

	return response;
}
```

---

## 7. Create dynamic consent list from Purpose Enquiry

Add this in `ConsentService.java`:

```java
private List<ConsentRequestConsent> prepareConsentsFromPurposeEnquiry(
		ConsentPurposeEnquiryResponse purposeResponse,
		String productCode) {

	List<ConsentRequestConsent> consents = new ArrayList<ConsentRequestConsent>();

	if (purposeResponse == null
			|| purposeResponse.getEisResponse() == null
			|| purposeResponse.getEisResponse().getBody() == null) {
		return consents;
	}

	for (ConsentPurposeContainer container : purposeResponse.getEisResponse().getBody()) {

		if (container.getPurposes() == null) {
			continue;
		}

		for (ConsentPurposeMaster purpose : container.getPurposes()) {

			ConsentRequestConsent consent = new ConsentRequestConsent();

			consent.setPurposeCode(purpose.getCode());
			consent.setPurposeVersion(String.valueOf(purpose.getVersion()));
			consent.setConsented("true");

			if (Boolean.TRUE.equals(purpose.getIsProductSpecific())) {

				List<String> consentedProducts = new ArrayList<String>();
				List<String> notConsentedProducts = new ArrayList<String>();

				consentedProducts.add(productCode);

				consent.setConsentedProducts(consentedProducts);

				// add only if setter exists
				// consent.setNotConsentedProducts(notConsentedProducts);
			}

			consents.add(consent);
		}
	}

	return consents;
}
```

---

## 8. Modify existing `generateConsentWriteRequest(...)`

Replace hardcoded consent part:

```java
ConsentRequestConsent consent1 = new ConsentRequestConsent();
ConsentRequestConsent consent2 = new ConsentRequestConsent();

List<ConsentRequestConsent> consents = new ArrayList<>();

consent1.setPurposeCode("PURP-DEMOPURP-000068");
consent1.setPurposeVersion("1");
consent1.setConsented("true");

consent2.setPurposeCode("PURP-DEMOPURP-000071");
consent2.setPurposeVersion("1");
consent2.setConsented("true");
consent2.setConsentedProducts(consentedProducts);

consents.add(consent1);
consents.add(consent2);
```

with this:

```java
ConsentPurposeEnquiryResponse purposeResponse =
		getPurposeEnquiryFromCCMS(locale);

List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeEnquiry(
				purposeResponse,
				"HOME_LOAN"
		);

if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
}
```

Then keep this same:

```java
payloadBody.setConsents(consents);
```

---

## Final flow

```text
Get Quote
→ validate privacy consent flag, ntbId, locale
→ call Purpose Enquiry API
→ get purposeCode, version, product specific flag
→ prepare dynamic consents array
→ build CCMS Write request
→ call CCMS Write API
→ save request/response log
→ if success, continue BRE
```

Main change: remove hardcoded purpose codes from Write API and populate them from Purpose Enquiry response.
