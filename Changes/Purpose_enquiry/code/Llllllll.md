Below changes use your existing generatePurposeRequest() and readPurposeFromCCMS() response inside Write API request.

1) ConsentService.java imports

Add these imports:

import com.mintstreet.consent.bo.ConsentResponsePurposeEisBody;
import com.mintstreet.consent.bo.ConsentResponsePurposes;

⸻

2) Change readPurposeFromCCMS() return type

Existing

public void readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {

Change to

public ConsentPurposeLog readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {

⸻

Existing ending

consentPurposeDao.save(purpose.getPurposeId(), purpose);

Replace with

purpose = consentPurposeDao.save(purpose.getPurposeId(), purpose);
return purpose;

⸻

Final method should be

public ConsentPurposeLog readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {
	JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(
			purpose.getPuposeRequest()
	);
	String responseStr = responseJson.toString();
	ConsentResponsePurposeEnquiry purposeResponse =
			new ConsentResponsePurposeEnquiry();
	purposeResponse =
			(ConsentResponsePurposeEnquiry) JSONUtil.getObjctFromJSON(
					purposeResponse,
					responseStr
			);
	purpose.setResponseCode(
			purposeResponse.getEisResponse().getStatusCode()
	);
	purpose.setResponseMsg(responseStr);
	purpose.setResponseStatus(
			purposeResponse.getEisResponse().getSuccess()
	);
	purpose.setResponseEntryTime(new Date());
	purpose = consentPurposeDao.save(
			purpose.getPurposeId(),
			purpose
	);
	return purpose;
}

⸻

3) Add helper method in ConsentService.java

Add this method below generateConsentWriteRequest(...):

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
			}
			consents.add(consent);
		}
	}
	return consents;
}

⸻

4) Change generateConsentWriteRequest(...)

Inside this method:

public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) throws Exception {

Find this code:

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

Replace with this:

ConsentPurposeLog purposeLog = generatePurposeRequest();
purposeLog = readPurposeFromCCMS(purposeLog);
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
				"HOME_LOAN"
		);
if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
}

⸻

5) Final generateConsentWriteRequest(...) important part

Your method should now contain this flow:

dpData.setDpCIF("");
dpData.setNtbId(ntbId);
dpData.setDpMobile(mobile);
dpData.setDpEmail(email);
dpData.setDpIPAddress(ipAddress);
dpData.setLocale(locale);
dpData.setTimestamp(getCurrentIsoTimestamp());
ConsentPurposeLog purposeLog = generatePurposeRequest();
purposeLog = readPurposeFromCCMS(purposeLog);
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
				"HOME_LOAN"
		);
if (consents == null || consents.isEmpty()) {
	throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
}
payloadBody.setTouchPointId(config.getTouchPointId());
payloadBody.setPurposeSetId(config.getPurposeSetId());
payloadBody.setDpData(dpData);
payloadBody.setConsents(consents);

⸻

6) ConsentUtil.java

No major change required.

Keep this method:

public void callCCMSConsentWriteAPI(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) throws Exception {
	try {
		ConsentWriteLog consentWrite =
				consentService.generateConsentWriteRequest(
						ntbId,
						mobile,
						email,
						ipAddress,
						locale
				);
		consentService.writeConsentToCCMS(consentWrite);
	} catch (JSONException e) {
		e.printStackTrace();
	}
}

Now this automatically does:

generatePurposeRequest()
→ readPurposeFromCCMS()
→ read Purpose response
→ create consents[]
→ add consents[] in Write request
→ call Write API

⸻

7) Keep callCCMSPurposeEnquiryAPI() only for testing

This can remain:

public void callCCMSPurposeEnquiryAPI() {
	try {
		ConsentPurposeLog purpose = consentService.generatePurposeRequest();
		consentService.readPurposeFromCCMS(purpose);
	} catch (JSONException e) {
		e.printStackTrace();
	}
}

But production Get Quote should call only:

callCCMSConsentWriteAPI(ntbId, mobile, email, ipAddress, locale);

⸻

Final flow

Get Quote backend
→ generateConsentWriteRequest(...)
→ Purpose Enquiry request saved in DB
→ Purpose Enquiry API called
→ Purpose Enquiry response saved in DB
→ Purpose response parsed
→ consents[] created dynamically
→ Write API request prepared with dynamic consents[]
→ Write request saved in DB
→ Write API called
→ Write response saved in DB
→ continue BRE
