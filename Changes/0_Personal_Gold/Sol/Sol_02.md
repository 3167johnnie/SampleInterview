Yes, it can work for both Personal Loan and Gold Loan without affecting the existing flow, but only if you keep the Gold-specific logic behind the loan-purpose check.

The shared flow is safe because both products already use:

```java
ApplicationFormPersonalLoanQuote
ApplicationFormPersonalLoan
PersonalProcessManagerImpl
PersonalLoanService
```

Gold is distinguished by:

```java
quote.getLoanQuoteLoanPurposeId() == 27
```

or:

```java
SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_GOLD)
```

## The critical change

Do not directly replace the Personal Loan `loanTypeId` with `27` globally.

Use a separate variable only for consent:

```java
Integer consentLoanTypeId = loanTypeId;

if (quote != null
		&& Constants.APP_PL_TYPE_GOLD.equals(
				quote.getLoanQuoteLoanPurposeId())) {

	consentLoanTypeId = Constants.APP_PL_TYPE_GOLD;
}
```

Then pass only this variable to the common consent method:

```java
boolean ccmsWriteStatus =
		writePrivacyConsentToCCMS(
				application,
				quote,
				loanScenarioBean,
				consentLoanTypeId
		);
```

This means:

```text
Personal Loan:
loanTypeId remains PERSONAL_LOAN_ID

Gold Loan:
consentLoanTypeId becomes APP_PL_TYPE_GOLD / 27
```

The existing Personal Loan processing variable remains unchanged.

## Safe final block

Use this at the existing CCMS call position:

```java
logger.info(
		"application.getAppMobileVerified() : "
		+ application.getAppMobileVerified()
);

if (application.getAppMobileVerified() != null
		&& application.getAppMobileVerified()
				.equalsIgnoreCase("Y")) {

	Integer consentLoanTypeId = loanTypeId;

	if (quote != null
			&& Constants.APP_PL_TYPE_GOLD.equals(
					quote.getLoanQuoteLoanPurposeId())) {

		consentLoanTypeId =
				Constants.APP_PL_TYPE_GOLD;

		logger.info(
				"Gold Loan consent flow selected. "
				+ "consentLoanTypeId : "
				+ consentLoanTypeId
		);

	} else {

		logger.info(
				"Personal/Pension Loan consent flow selected. "
				+ "consentLoanTypeId : "
				+ consentLoanTypeId
		);
	}

	boolean ccmsWriteStatus =
			writePrivacyConsentToCCMS(
					application,
					quote,
					loanScenarioBean,
					consentLoanTypeId
			);

	SessionUtil.setConsentSubmitNTBPersonal(
			"true"
	);

	if (!ccmsWriteStatus) {
		return loanScenarioBean;
	}
}
```

Your existing consent call is already before BRE, so only the consent loan-type resolution changes. 

## What remains unchanged

These existing flows do not need to change:

```java
loanTypeId = Constants.PERSONAL_LOAN_ID;
```

inside `goldLoan()` can remain because Gold still runs under the Personal Loan module.

Keep unchanged:

```java
return getPersonalLoan(Constants.PERSONAL_LOAN_ID);
```

Keep unchanged:

```java
personalLoanHelper.callBRE(...)
```

Keep unchanged:

```java
processSubmitQuote(...)
```

Keep unchanged:

```java
ApplicationFormPersonalLoan
ApplicationFormPersonalLoanQuote
PersonalLoanService
```

Gold already uses the Personal Loan action while setting `APP_PL_TYPE_GOLD` and `goldType = 27`. 

## Popup isolation

The Gold popup uses unique IDs:

```text
consentGoldLoan
goldPrivacyLocaleDropdown
goldConsentNoticeDiv
acceptGoldConsentBtn
```

Therefore, it will not clash with a Personal Loan popup.

Include `ConsentPopupGoldLoan.jsp` only in:

```text
Gold.jsp
```

Do not include it in:

```text
PersonalLoan.jsp
PensionLoan.jsp
```

## One correction to the Gold popup

In the popup acceptance code, do not disable or enable a shared Personal Loan checkbox globally.

Gold-only code should use:

```javascript
$("#infoprovide").prop("checked", true);
```

That is safe because `ConsentPopupGoldLoan.jsp` is loaded only on `Gold.jsp`.

## Duplicate API protection

This protection works for both products:

```java
if (ValidatorUtil.isValid(
		application.getAppCcmsConsentId())) {

	logger.info(
			"CCMS consent already available. "
			+ "Skipping duplicate write. appSeqId={}, consentId={}",
			application.getAppSeqId(),
			application.getAppCcmsConsentId()
	);

	if (!ValidatorUtil.isValid(
			quote.getQuoteCcmsConsentId())) {

		quote.setQuoteCcmsConsentId(
				application.getAppCcmsConsentId()
		);

		personalLoanService.save(quote);
	}

	return true;
}
```

However, generate a new NTB ID only when no CCMS consent ID already exists:

```java
String ntbId =
		consentUtil.generateNtbId(
				mobile,
				loanTypeId
		);
```

This produces:

```text
Personal Loan:
mobile + timestamp + PERSONAL_LOAN_ID

Gold Loan:
mobile + timestamp + 27
```

## Product code inside the CCMS request

Verify your common `ConsentUtil.callCCMSConsentWriteAPI(...)` or request-generation method maps the product using `loanTypeId`.

Use:

```java
String productCode;

if (Constants.APP_PL_TYPE_GOLD.equals(
		loanTypeId)) {

	productCode = "GOLD_LOAN";

} else {

	productCode = "PERSONAL_LOAN";
}
```

Better still, use your actual CCMS-configured product values instead of hardcoded display names.

Do not let Gold inherit the Personal Loan CCMS product code.

## One potential problem in the current flow

Your shared code calls consent whenever:

```java
application.getAppMobileVerified().equalsIgnoreCase("Y")
```

That may also trigger during retries or later page requests. The CCMS consent-ID duplicate check must remain inside `writePrivacyConsentToCCMS()`.

Also restrict the call to products that have privacy consent enabled:

```java
boolean consentSupportedProduct =
		Constants.APP_PL_TYPE_GOLD.equals(
				quote.getLoanQuoteLoanPurposeId())
		|| Constants.PERSONAL_LOAN_PURPOSE_ID.equals(
				quote.getLoanQuoteLoanPurposeId());

if (application.getAppMobileVerified() != null
		&& application.getAppMobileVerified()
				.equalsIgnoreCase("Y")
		&& consentSupportedProduct) {

	// Call common consent method
}
```

Use the actual Personal Loan purpose constant from your project.

## Expected behavior

| Product       | Existing processing            |       Consent type | Popup                   |
| ------------- | ------------------------------ | -----------------: | ----------------------- |
| Personal Loan | Unchanged                      |   Personal Loan ID | Existing Personal popup |
| Gold Loan     | Unchanged Personal module flow |               `27` | Gold popup              |
| Pension Loan  | Unchanged                      | Existing mapped ID | Existing flow           |
| CVE           | Separate CVE branch            |             CVE ID | CVE popup               |

So the answer is: **yes, the common backend method can safely serve both Personal and Gold Loan**, provided you only override `consentLoanTypeId`, not the action’s main `loanTypeId`, and include the Gold popup only on the Gold page.
