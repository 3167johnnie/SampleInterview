Yes. Remove the hardcoded "HOME_LOAN" completely.

The correct rule will be:

Purpose Enquiry bankProducts[]
        ↓
Copy every bankProducts.code
        ↓
If consented = true
    put all codes in consentedProducts
    keep notConsentedProducts empty
If consented = false
    keep consentedProducts empty
    put all codes in notConsentedProducts
If bankProducts is empty
    both lists remain empty

For example, Purpose Enquiry returns:

"bankProducts": [
  {
    "code": "CREDIT_CARD",
    "label": "Credit Card"
  },
  {
    "code": "LIFE_INSURANCE",
    "label": "Life Insurance"
  }
]

The Write request will use the product code values directly. No loan product will be added manually.

⸻

File 1: ConsentRequestConsent.java

Make sure this class has both lists.

package com.mintstreet.consent.bo;
import java.util.List;
public class ConsentRequestConsent {
	private String purposeCode;
	private String purposeVersion;
	private String consented;
	private List<String> consentedProducts;
	private List<String> notConsentedProducts;
	public String getPurposeCode() {
		return purposeCode;
	}
	public void setPurposeCode(String purposeCode) {
		this.purposeCode = purposeCode;
	}
	public String getPurposeVersion() {
		return purposeVersion;
	}
	public void setPurposeVersion(String purposeVersion) {
		this.purposeVersion = purposeVersion;
	}
	public String getConsented() {
		return consented;
	}
	public void setConsented(String consented) {
		this.consented = consented;
	}
	public List<String> getConsentedProducts() {
		return consentedProducts;
	}
	public void setConsentedProducts(
			List<String> consentedProducts) {
		this.consentedProducts = consentedProducts;
	}
	public List<String> getNotConsentedProducts() {
		return notConsentedProducts;
	}
	public void setNotConsentedProducts(
			List<String> notConsentedProducts) {
		this.notConsentedProducts = notConsentedProducts;
	}
}

If notConsentedProducts already exists, do not add it again.

⸻

File 2: ConsentService.java

Step 1: Remove the product-code parameter

Your current helper may be:

private List<ConsentRequestConsent> prepareConsentsFromPurposeResponse(
		ConsentResponsePurposeEnquiry purposeResponse,
		String productCode) {

Replace it with:

private List<ConsentRequestConsent> prepareConsentsFromPurposeResponse(
		ConsentResponsePurposeEnquiry purposeResponse) {

There should no longer be any "HOME_LOAN" or productCode parameter.

⸻

Step 2: Replace the complete helper method

Replace the existing prepareConsentsFromPurposeResponse(...) method with:

private List<ConsentRequestConsent> prepareConsentsFromPurposeResponse(
		ConsentResponsePurposeEnquiry purposeResponse) {
	List<ConsentRequestConsent> consents =
			new ArrayList<ConsentRequestConsent>();
	if (purposeResponse == null
			|| purposeResponse.getEisResponse() == null
			|| purposeResponse.getEisResponse().getBody() == null
			|| purposeResponse.getEisResponse().getBody().isEmpty()) {
		logger.warn(
				"Purpose Enquiry response/body is empty."
		);
		return consents;
	}
	for (ConsentResponsePurposeEisBody container :
			purposeResponse.getEisResponse().getBody()) {
		if (container == null
				|| container.getPurposes() == null
				|| container.getPurposes().isEmpty()) {
			continue;
		}
		for (ConsentResponsePurposes purpose :
				container.getPurposes()) {
			if (purpose == null
					|| isBlank(purpose.getCode())
					|| isBlank(purpose.getVersion())) {
				logger.warn(
						"Skipping invalid Purpose Enquiry purpose. "
						+ "Purpose data: {}",
						purpose
				);
				continue;
			}
			ConsentRequestConsent consent =
					new ConsentRequestConsent();
			consent.setPurposeCode(
					purpose.getCode()
			);
			consent.setPurposeVersion(
					purpose.getVersion()
			);
			/*
			 * Temporarily true for your current testing.
			 * Later use actual customer consent.
			 */
			consent.setConsented("true");
			/*
			 * Both lists are initialized.
			 * Therefore, if no bank products are returned,
			 * JSON will contain empty arrays [].
			 */
			List<String> consentedProducts =
					new ArrayList<String>();
			List<String> notConsentedProducts =
					new ArrayList<String>();
			/*
			 * Never hardcode HOME_LOAN.
			 * Use only bankProducts returned from Purpose Enquiry.
			 */
			if (purpose.getBankProducts() != null
					&& !purpose.getBankProducts().isEmpty()) {
				for (ConsentResponseBankProducts bankProduct :
						purpose.getBankProducts()) {
					if (bankProduct == null
							|| isBlank(bankProduct.getCode())) {
						continue;
					}
					String bankProductCode =
							bankProduct.getCode().trim();
					/*
					 * Current testing:
					 * all purposes are consented=true.
					 *
					 * Therefore, all configured product codes
					 * go into consentedProducts.
					 */
					if ("true".equalsIgnoreCase(
							consent.getConsented())) {
						consentedProducts.add(
								bankProductCode
						);
					} else {
						notConsentedProducts.add(
								bankProductCode
						);
					}
				}
			}
			/*
			 * Add product lists only for product-specific purposes.
			 */
			if ("true".equalsIgnoreCase(
					purpose.getIsProductSpecific())) {
				consent.setConsentedProducts(
						consentedProducts
				);
				consent.setNotConsentedProducts(
						notConsentedProducts
				);
			}
			consents.add(consent);
			logger.info(
					"Prepared CCMS consent. "
					+ "purposeCode: {}, "
					+ "purposeVersion: {}, "
					+ "consented: {}, "
					+ "productSpecific: {}, "
					+ "consentedProducts: {}, "
					+ "notConsentedProducts: {}",
					consent.getPurposeCode(),
					consent.getPurposeVersion(),
					consent.getConsented(),
					purpose.getIsProductSpecific(),
					consent.getConsentedProducts(),
					consent.getNotConsentedProducts()
			);
		}
	}
	return consents;
}

⸻

Step 3: Add the blank helper

Add this method anywhere near the bottom of ConsentService.java:

private boolean isBlank(String value) {
	return value == null || value.trim().isEmpty();
}

⸻

Step 4: Change the call inside generateConsentWriteRequest(...)

Find:

List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse,
				"HOME_LOAN"
		);

Replace with:

List<ConsentRequestConsent> consents =
		prepareConsentsFromPurposeResponse(
				purposeResponse
		);

This is the main place where "HOME_LOAN" is removed.

⸻

Step 5: Delete any old product hardcoding

Search inside ConsentService.java for:

consentedProducts.add("HOME_LOAN");

Delete it.

Also search for:

prepareConsentsFromPurposeResponse(
		purposeResponse,
		"HOME_LOAN"
);

Replace it with:

prepareConsentsFromPurposeResponse(
		purposeResponse
);

Also remove any variable such as:

String productCode = "HOME_LOAN";

if it was used only for CCMS product mapping.

⸻

Expected result for PR-PROMOTIO-000106

Purpose Enquiry returned:

"bankProducts": [
  {
    "code": "CREDIT_CARD",
    "label": "Credit Card"
  },
  {
    "code": "WILL_TRUSTEESHIP_SERVICES",
    "label": "Will Trusteeship Services"
  },
  {
    "code": "DEMAT_AND_SECURITIES",
    "label": "Demat and securities"
  },
  {
    "code": "MUTUAL_FUND_AND_ASSET_MANAGEMENT",
    "label": "Mutual Fund / Asset Management"
  },
  {
    "code": "GENERAL_INSURANCE",
    "label": "General Insurance"
  },
  {
    "code": "LIFE_INSURANCE",
    "label": "Life Insurance"
  }
]

Since you are currently setting:

consent.setConsented("true");

the Write request becomes:

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
  ],
  "notConsentedProducts": []
}

This matches the CCMS error requirement because CCMS previously said those six products were missing.

⸻

Expected result when bankProducts is empty

For:

{
  "purposeCode": "PR-INCIDENT-000101",
  "isProductSpecific": true,
  "bankProducts": []
}

the generated request becomes:

{
  "purposeCode": "PR-INCIDENT-000101",
  "purposeVersion": "2",
  "consented": "true",
  "consentedProducts": [],
  "notConsentedProducts": []
}

There will be no "HOME_LOAN".

⸻

If CCMS rejects empty arrays

Some APIs accept:

"consentedProducts": [],
"notConsentedProducts": []

while others require these fields to be omitted when no products exist.

If CCMS gives an error for empty arrays, change this section:

if ("true".equalsIgnoreCase(
		purpose.getIsProductSpecific())) {
	consent.setConsentedProducts(
			consentedProducts
	);
	consent.setNotConsentedProducts(
			notConsentedProducts
	);
}

to:

if ("true".equalsIgnoreCase(
		purpose.getIsProductSpecific())
		&& purpose.getBankProducts() != null
		&& !purpose.getBankProducts().isEmpty()) {
	consent.setConsentedProducts(
			consentedProducts
	);
	consent.setNotConsentedProducts(
			notConsentedProducts
	);
}

Then, when bankProducts is empty, the fields will not be added.

Based on the previous CCMS error, this second behavior—omitting product fields when bankProducts is empty—is safer.

Recommended production-safe product block

Use this version:

if ("true".equalsIgnoreCase(
		purpose.getIsProductSpecific())
		&& purpose.getBankProducts() != null
		&& !purpose.getBankProducts().isEmpty()) {
	consent.setConsentedProducts(
			consentedProducts
	);
	consent.setNotConsentedProducts(
			notConsentedProducts
	);
}

This gives:

{
  "purposeCode": "PR-INCIDENT-000101",
  "purposeVersion": "2",
  "consented": "true"
}

when no product codes are returned, and gives full product arrays when CCMS provides products.
