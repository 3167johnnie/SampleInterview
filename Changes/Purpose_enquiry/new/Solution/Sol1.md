Yes. Minimal change: **ConsentUtil will call Purpose Enquiry first, get saved response, then pass that response to Consent Write request builder.**

Your uploaded requirement says Purpose Enquiry response `body[].purposes[]` must be converted into Consent Write `consents[]`, and then Write API is called. 

# 1) Change `ConsentUtil.java`

## Replace `callCCMSPurposeEnquiryAPI()`

### Existing

```java
public void callCCMSPurposeEnquiryAPI() {
	try {
		// generate plain request
		ConsentPurposeLog purpose = consentService.generatePurposeRequest();

		// send request and read purpose from CCMS
		consentService.readPurposeFromCCMS(purpose);
	} catch (JSONException e) {
		e.printStackTrace();//TODO add loggers
	}
}
```

### Replace with

```java
public ConsentPurposeLog callCCMSPurposeEnquiryAPI() throws JSONException {
	ConsentPurposeLog purpose = consentService.generatePurposeRequest();

	purpose = consentService.readPurposeFromCCMS(purpose);

	return purpose;
}
```

---

## Replace `callCCMSConsentWriteAPI(...)`

### Existing

```java
public void callCCMSConsentWriteAPI(String ntbId, String mobile, String email, String ipAddress, String locale) throws Exception {
	
	try {
		ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(ntbId, mobile, email, ipAddress, locale);
		
		consentService.writeConsentToCCMS(consentWrite);
	} catch (JSONException e) {
		e.printStackTrace();// TODO add loggers
	}

}
```

### Replace with

```java
public void callCCMSConsentWriteAPI(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale,
		String productCode) throws Exception {

	try {
		ConsentPurposeLog purpose = callCCMSPurposeEnquiryAPI();

		ConsentWriteLog consentWrite =
				consentService.generateConsentWriteRequest(
						ntbId,
						mobile,
						email,
						ipAddress,
						locale,
						productCode,
						purpose
				);

		consentService.writeConsentToCCMS(consentWrite);

	} catch (JSONException e) {
		e.printStackTrace();
	}
}
```

---

# 2) Change `ConsentService.java`

Your `readPurposeFromCCMS()` is already correctly returning `ConsentPurposeLog` in uploaded file. It saves response into `purpose.responseMsg`. 

Now change Write request method.

## Replace method signature

### Existing

```java
public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) throws Exception {
```

### Replace with

```java
public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale,
		String productCode,
		ConsentPurposeLog purposeLog) throws Exception {
```

---

## Inside `generateConsentWriteRequest(...)`

### Remove this block

```java
ConsentPurposeEnquiryResponse purposeResponse = getPurposeEnquiryFromCCMS(locale);

List<ConsentRequestConsent> consents = prepareConsentsFromPurposeEnquiry(purposeResponse,"HOME_LOAN");

if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
}
```

### Add this block

```java
if (purposeLog == null || purposeLog.getResponseMsg() == null) {
	throw new RuntimeException("Purpose Enquiry response not found.");
}

ConsentResponsePurposeEnquiry purposeResponse =
		new ConsentResponsePurposeEnquiry();

purposeResponse =
		(ConsentResponsePurposeEnquiry) JSONUtil.getObjctFromJSON(
				purposeResponse,
				purposeLog.getResponseMsg()
		);

List<ConsentRequestConsent> consents =
		prepareConsentsFromExistingPurposeResponse(
				purposeResponse,
				productCode
		);

if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
}
```

---

# 3) Add this helper method in `ConsentService.java`

Add below `generateConsentWriteRequest(...)`:

```java
private List<ConsentRequestConsent> prepareConsentsFromExistingPurposeResponse(
		ConsentResponsePurposeEnquiry purposeResponse,
		String productCode) {

	List<ConsentRequestConsent> consents =
			new ArrayList<ConsentRequestConsent>();

	if (purposeResponse == null
			|| purposeResponse.getEisResponse() == null
			|| purposeResponse.getEisResponse().getEisBody() == null) {
		return consents;
	}

	for (ConsentResponsePurposeEisBody body
			: purposeResponse.getEisResponse().getEisBody()) {

		if (body == null || body.getPurposes() == null) {
			continue;
		}

		for (ConsentResponsePurposes purpose : body.getPurposes()) {

			if (purpose == null || purpose.getCode() == null) {
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
				 * Add only if setter exists:
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

# 4) In Home Loan backend call

Where you currently call:

```java
consentService.generateConsentWriteRequest(
		quote.getQuoteNtbId(),
		mobile,
		email,
		ipAddress,
		quote.getQuotePrivacyLocale()
);
```

Change to call through `ConsentUtil` or directly with product code.

## If using `ConsentUtil`

```java
consentUtil.callCCMSConsentWriteAPI(
		quote.getQuoteNtbId(),
		mobile,
		email,
		ipAddress,
		quote.getQuotePrivacyLocale(),
		"HOME_LOAN"
);
```

## If using `ConsentService` directly

```java
ConsentPurposeLog purposeLog = consentService.generatePurposeRequest();
purposeLog = consentService.readPurposeFromCCMS(purposeLog);

ConsentWriteLog consentWrite =
		consentService.generateConsentWriteRequest(
				quote.getQuoteNtbId(),
				mobile,
				email,
				ipAddress,
				quote.getQuotePrivacyLocale(),
				"HOME_LOAN",
				purposeLog
		);

consentWrite = consentService.writeConsentToCCMS(consentWrite);
```

---

# 5) For Auto Loan later

Use same method, only product code changes:

```java
"AUTO_LOAN"
```

Example:

```java
consentUtil.callCCMSConsentWriteAPI(
		quote.getQuoteNtbId(),
		mobile,
		email,
		ipAddress,
		quote.getQuotePrivacyLocale(),
		"AUTO_LOAN"
);
```

---

# Final flow

```text
Get Quote backend
→ callCCMSPurposeEnquiryAPI()
→ generatePurposeRequest()
→ readPurposeFromCCMS()
→ Purpose response saved in DB
→ responseMsg parsed
→ body[].purposes[] converted to consents[]
→ generateConsentWriteRequest(...)
→ Write request saved in DB
→ writeConsentToCCMS(...)
→ Write response saved in DB
→ continue BRE
```
# HomeProcessManagerImpl   HomeProcessManagerImpl


You **can use `ConsentUtil`**, but in your current code `HomeProcessManagerImpl` already has:

```java
@Autowired
private ConsentService consentService;
```

So I suggested direct `ConsentService` to avoid adding another dependency.

But if you want clean flow, yes, use `ConsentUtil`. Then `HomeProcessManagerImpl` calls only one method.

## Better common approach

Use `ConsentUtil` as wrapper:

```text
HomeProcessManagerImpl
→ ConsentUtil.callCCMSConsentWriteAPI(...)
→ internally calls Purpose Enquiry
→ prepares Write request
→ calls Write API
```

## Change 1: Inject `ConsentUtil`

In `HomeProcessManagerImpl.java`, add import:

```java
import com.mintstreet.common.util.ConsentUtil;
```

Add autowired field:

```java
@Autowired
private ConsentUtil consentUtil;
```

Then you can remove direct `ConsentService` call from this method if not used elsewhere.

---

## Change 2: Update `ConsentUtil.java`

Replace your current `callCCMSConsentWriteAPI(...)` with:

```java
public ConsentWriteLog callCCMSConsentWriteAPI(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale,
		String productCode) throws Exception {

	ConsentPurposeLog purpose = consentService.generatePurposeRequest();

	purpose = consentService.readPurposeFromCCMS(purpose);

	ConsentWriteLog consentWrite =
			consentService.generateConsentWriteRequest(
					ntbId,
					mobile,
					email,
					ipAddress,
					locale,
					productCode,
					purpose
			);

	consentWrite = consentService.writeConsentToCCMS(consentWrite);

	return consentWrite;
}
```

---

## Change 3: In `writePrivacyConsentToCCMS(...)`

Replace this:

```java
ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(quote.getQuoteNtbId(), mobile,
		email, ipAddress, quote.getQuotePrivacyLocale());

consentWrite = consentService.writeConsentToCCMS(consentWrite);
```

With this:

```java
ConsentWriteLog consentWrite =
		consentUtil.callCCMSConsentWriteAPI(
				quote.getQuoteNtbId(),
				mobile,
				email,
				ipAddress,
				quote.getQuotePrivacyLocale(),
				"HOME_LOAN"
		);
```

---

## Why `ConsentUtil` is okay here

Use `ConsentUtil` if you want one common method for all loans:

```java
consentUtil.callCCMSConsentWriteAPI(..., "HOME_LOAN");
consentUtil.callCCMSConsentWriteAPI(..., "AUTO_LOAN");
consentUtil.callCCMSConsentWriteAPI(..., "PERSONAL_LOAN");
```

That is cleaner for reusable loan flow.

Final answer: **Yes, use `ConsentUtil` if you want common reusable orchestration.** Direct `ConsentService` is also correct, but `ConsentUtil` is better for common loan integration.
