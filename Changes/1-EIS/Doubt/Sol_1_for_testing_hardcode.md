For testing, bypass only the dynamic consent construction and hardcode the CCMS consents list inside your existing generateConsentWriteRequest(...).

Keep the existing EIS encryption/call unchanged.

File: ConsentService.java

Inside:

public ConsentWriteLog generateConsentWriteRequest(
		String ntbId,
		String mobile,
		String email,
		String ipAddress,
		String locale) {

Find the current block where you prepare consents from Purpose Enquiry:

ConsentResponsePurposeEnquiry purposeResponse;
try {
	purposeResponse = getPurposeResponseForWrite();
} catch (JSONException e) {
	throw new RuntimeException(
			"Unable to get Purpose Enquiry response from CCMS.",
			e
	);
}
List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse,
				"HOME_LOAN"
		);

Temporarily comment it:

/*
ConsentResponsePurposeEnquiry purposeResponse;
try {
	purposeResponse = getPurposeResponseForWrite();
} catch (JSONException e) {
	throw new RuntimeException(
			"Unable to get Purpose Enquiry response from CCMS.",
			e
	);
}
List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse,
				"HOME_LOAN"
		);
*/

Then add this hardcoded test block.

Test case 1: simplest request

Start with only non-product-specific purposes:

List<ConsentRequestConsent> consents =
		new ArrayList<ConsentRequestConsent>();
ConsentRequestConsent consent1 =
		new ConsentRequestConsent();
consent1.setPurposeCode("PR-FACILITA-000099");
consent1.setPurposeVersion("1");
consent1.setConsented("true");
ConsentRequestConsent consent2 =
		new ConsentRequestConsent();
consent2.setPurposeCode("PR-REGULATO-000100");
consent2.setPurposeVersion("1");
consent2.setConsented("true");
ConsentRequestConsent consent3 =
		new ConsentRequestConsent();
consent3.setPurposeCode("PR-MANAGEYO-000102");
consent3.setPurposeVersion("1");
consent3.setConsented("true");
ConsentRequestConsent consent4 =
		new ConsentRequestConsent();
consent4.setPurposeCode("PR-SERVICEI-000077");
consent4.setPurposeVersion("1");
consent4.setConsented("true");
consents.add(consent1);
consents.add(consent2);
consents.add(consent3);
consents.add(consent4);

Keep your existing line:

payloadBody.setConsents(consents);

The generated CCMS body will contain:

"consents": [
  {
    "purposeCode": "PR-FACILITA-000099",
    "purposeVersion": "1",
    "consented": "true"
  },
  {
    "purposeCode": "PR-REGULATO-000100",
    "purposeVersion": "1",
    "consented": "true"
  },
  {
    "purposeCode": "PR-MANAGEYO-000102",
    "purposeVersion": "1",
    "consented": "true"
  },
  {
    "purposeCode": "PR-SERVICEI-000077",
    "purposeVersion": "1",
    "consented": "true"
  }
]

This is the best first test because no consentedProducts or notConsentedProducts are involved.

⸻

Test case 2: test PR-INCIDENT-000101

After test case 1 succeeds, add:

ConsentRequestConsent consent5 =
		new ConsentRequestConsent();
consent5.setPurposeCode("PR-INCIDENT-000101");
consent5.setPurposeVersion("2");
consent5.setConsented("true");
/*
 * Do not set HOME_LOAN because Purpose Enquiry returned
 * bankProducts as empty for this purpose.
 */
consents.add(consent5);

Expected JSON:

{
  "purposeCode": "PR-INCIDENT-000101",
  "purposeVersion": "2",
  "consented": "true"
}

Do not add:

consent5.setConsentedProducts(...);

⸻

Test case 3: test product-specific promotional purpose

For PR-PROMOTIO-000106, use the exact products from Purpose Enquiry.

ConsentRequestConsent consent6 =
		new ConsentRequestConsent();
consent6.setPurposeCode("PR-PROMOTIO-000106");
consent6.setPurposeVersion("3");
consent6.setConsented("true");
List<String> promotionalProducts =
		new ArrayList<String>();
promotionalProducts.add("CREDIT_CARD");
promotionalProducts.add("WILL_TRUSTEESHIP_SERVICES");
promotionalProducts.add("DEMAT_AND_SECURITIES");
promotionalProducts.add("MUTUAL_FUND_AND_ASSET_MANAGEMENT");
promotionalProducts.add("GENERAL_INSURANCE");
promotionalProducts.add("LIFE_INSURANCE");
consent6.setConsentedProducts(promotionalProducts);
consents.add(consent6);

This generates:

{
  "purposeCode": "PR-PROMOTIO-000106",
  "purposeVersion": "3",
  "consented": "true",
  "consentedProducts": [
    "CREDIT_CARD",
    "WILL_TRUSTEESHIP_SERVICES",
    "DEMAT_AND_SECURITIES",
    "MUTUAL_FUND_AND_ASSET_MANAGEMENT",
    "GENERAL_INSURANCE",
    "LIFE_INSURANCE"
  ]
}

Do not add HOME_LOAN to this purpose.

⸻

Complete temporary test block

For a likely successful full test, use:

List<ConsentRequestConsent> consents =
		new ArrayList<ConsentRequestConsent>();
ConsentRequestConsent consent1 =
		new ConsentRequestConsent();
consent1.setPurposeCode("PR-FACILITA-000099");
consent1.setPurposeVersion("1");
consent1.setConsented("true");
consents.add(consent1);
ConsentRequestConsent consent2 =
		new ConsentRequestConsent();
consent2.setPurposeCode("PR-REGULATO-000100");
consent2.setPurposeVersion("1");
consent2.setConsented("true");
consents.add(consent2);
ConsentRequestConsent consent3 =
		new ConsentRequestConsent();
consent3.setPurposeCode("PR-INCIDENT-000101");
consent3.setPurposeVersion("2");
consent3.setConsented("true");
consents.add(consent3);
ConsentRequestConsent consent4 =
		new ConsentRequestConsent();
consent4.setPurposeCode("PR-MANAGEYO-000102");
consent4.setPurposeVersion("1");
consent4.setConsented("true");
consents.add(consent4);
ConsentRequestConsent consent5 =
		new ConsentRequestConsent();
consent5.setPurposeCode("PR-SERVICEI-000077");
consent5.setPurposeVersion("1");
consent5.setConsented("true");
consents.add(consent5);
ConsentRequestConsent consent6 =
		new ConsentRequestConsent();
consent6.setPurposeCode("PR-ANALYTIC-000105");
consent6.setPurposeVersion("2");
consent6.setConsented("true");
consents.add(consent6);
ConsentRequestConsent consent7 =
		new ConsentRequestConsent();
consent7.setPurposeCode("PR-PROMOTIO-000104");
consent7.setPurposeVersion("1");
consent7.setConsented("true");
consents.add(consent7);
ConsentRequestConsent consent8 =
		new ConsentRequestConsent();
consent8.setPurposeCode("PR-PROMOTIO-000106");
consent8.setPurposeVersion("3");
consent8.setConsented("true");
List<String> promotionalProducts =
		new ArrayList<String>();
promotionalProducts.add("CREDIT_CARD");
promotionalProducts.add("WILL_TRUSTEESHIP_SERVICES");
promotionalProducts.add("DEMAT_AND_SECURITIES");
promotionalProducts.add("MUTUAL_FUND_AND_ASSET_MANAGEMENT");
promotionalProducts.add("GENERAL_INSURANCE");
promotionalProducts.add("LIFE_INSURANCE");
consent8.setConsentedProducts(promotionalProducts);
consents.add(consent8);
payloadBody.setConsents(consents);

Do not include PR-USERPROF-000118 in this first test because Purpose Enquiry returned:

"isNTB": false

while your request is an NTB flow.

Where exactly to add it

Inside generateConsentWriteRequest(...), place the test block:

dpData.setTimestamp(getCurrentIsoTimestamp());
// ADD HARDCODED TEST CONSENTS HERE
payloadBody.setTouchPointId(config.getTouchPointId());
payloadBody.setPurposeSetId(config.getPurposeSetId());
payloadBody.setDpData(dpData);
payloadBody.setConsents(consents);

Testing order

First test only:

PR-FACILITA-000099

Then test four non-product-specific essential purposes.

Then add:

PR-INCIDENT-000101

without product arrays.

Finally add:

PR-PROMOTIO-000106

with exactly its six configured product codes.

This isolates which consent structure CCMS accepts without changing your EIS integration.
