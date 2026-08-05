You are correct. For Gold Loan, the correct place is not the processGetQuotes() CCMS block.

Gold Loan has one customer-facing form. The final SUBMIT APPLICATION flow calls:

processSubmitQuote(...)

and that method saves ApplicationFormPersonalLoan, generates the application reference number, and returns the thank-you page. Pasted text (2)(17).txt

The correct Gold flow is:

Gold form submit
→ processSubmitQuote()
→ validate OTP/application
→ save application data
→ call CCMS Write for Gold only
→ save NTB ID and CCMS consent ID
→ generate reference number
→ continue existing thank-you flow

This isolates Gold Loan and does not change Personal Loan processing.

1. Remove Gold consent call from processGetQuotes()

In PersonalProcessManagerImpl.processGetQuotes(), you currently have a common call around:

if (application.getAppMobileVerified() != null
		&& application.getAppMobileVerified().equalsIgnoreCase("Y")) {
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
}

That location is before BRE and is suitable for the normal quote-driven Personal Loan flow, but it is not the correct final Gold submission point. Pasted text (3)(12).txt

Change it so Gold Loan is excluded:

boolean isGoldLoan =
		quote != null
		&& Constants.APP_PL_TYPE_GOLD.equals(
				quote.getLoanQuoteLoanPurposeId()
		);
if (!isGoldLoan
		&& application.getAppMobileVerified() != null
		&& application.getAppMobileVerified()
				.equalsIgnoreCase("Y")) {
	boolean ccmsWriteStatus =
			writePrivacyConsentToCCMS(
					application,
					quote,
					loanScenarioBean,
					loanTypeId
			);
	SessionUtil.setConsentSubmitNTBPersonal("true");
	if (!ccmsWriteStatus) {
		return loanScenarioBean;
	}
}

This preserves existing Personal Loan processing and prevents Gold CCMS Write from firing too early.

⸻

2. Correct location: processSubmitQuote()

Open:

PersonalProcessManagerImpl.java

Find:

public ApplicationFormPersonalLoan processSubmitQuote(
		Integer appSeqId,
		Integer requestIndex,
		ApplicationFormPersonalLoan appForm,
		String isDsrPage,
		Integer bankLMSUserId)

This method already loads:

ApplicationFormPersonalLoan appFormData

and:

ApplicationFormPersonalLoanQuote quote

It also validates that OTP has been completed before continuing. Pasted text (3)(12).txt

Later, the method has:

if(quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
	appFormData.setAppPersonalLoanId(Integer.valueOf(13));
	appFormData.setAppLoanAmount(
			quote.getLoanQuoteLoanAmountTaken() / 100000
	);
}
appFormData = this.personalLoanService.save(appFormData);

Pasted text (3)(12).txt

The Gold CCMS call should be inserted immediately after this save and before PAN/CIBIL/reference-number processing.

⸻

3. Replace the Gold application-save block

Find this existing code:

if(quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
	appFormData.setAppPersonalLoanId(Integer.valueOf(13));
	appFormData.setAppLoanAmount(
			quote.getLoanQuoteLoanAmountTaken()/100000
	);
}
appFormData = this.personalLoanService.save(appFormData);
logger.info(
		"PLProcessImpl.java :: LNo :: 3034 :: "
		+ "appFormData.getAppLoanStatusId() :: "
		+ appFormData.getAppLoanStatusId()
		+ " with AppSeqId "
		+ appSeqId
);

Replace with:

boolean isGoldLoan =
		quote != null
		&& Constants.APP_PL_TYPE_GOLD.equals(
				quote.getLoanQuoteLoanPurposeId()
		);
if (isGoldLoan) {
	appFormData.setAppPersonalLoanId(
			Integer.valueOf(13)
	);
	appFormData.setAppLoanAmount(
			quote.getLoanQuoteLoanAmountTaken()
					/ 100000
	);
	/*
	 * Copy privacy acceptance information submitted
	 * from GoldFirstPageSession.jsp.
	 */
	appFormData.setAppPrivacyConsentFlag(
			"Y".equalsIgnoreCase(
					quote.getQuotePrivacyConsentFlag()
			) ? "Y" : "N"
	);
	if (ValidatorUtil.isValid(
			quote.getQuotePrivacyLocale())) {
		appFormData.setAppPrivacyLocale(
				quote.getQuotePrivacyLocale()
		);
	} else {
		appFormData.setAppPrivacyLocale("eng");
	}
}
appFormData =
		this.personalLoanService.save(appFormData);
if (appFormData == null) {
	logger.info(
			"Gold/Personal application save failed. appSeqId={}",
			appSeqId
	);
	return null;
}
logger.info(
		"PLProcessImpl.java :: application saved. "
		+ "appSeqId={}, loanPurposeId={}",
		appFormData.getAppSeqId(),
		quote.getLoanQuoteLoanPurposeId()
);
/*
 * Gold is a one-page application flow.
 * Call CCMS only here, after the application record exists
 * and before application reference number generation.
 */
if (isGoldLoan) {
	boolean goldConsentStatus =
			writeGoldLoanConsentToCCMS(
					appFormData,
					quote
			);
	if (!goldConsentStatus) {
		appFormData.setError(
				"Unable to write consent to CCMS. "
				+ "Please try again."
		);
		return appFormData;
	}
}

⸻

4. Add the Gold-only common method

Add this method near the bottom of PersonalProcessManagerImpl, before the final class }:

private boolean writeGoldLoanConsentToCCMS(
		ApplicationFormPersonalLoan application,
		ApplicationFormPersonalLoanQuote quote) {
	logger.info(
			"writeGoldLoanConsentToCCMS started. "
			+ "appSeqId={}, quoteId={}",
			application != null
					? application.getAppSeqId()
					: null,
			quote != null
					? quote.getLoanQuoteId()
					: null
	);
	try {
		if (application == null || quote == null) {
			logger.info(
					"Gold consent application/quote is null"
			);
			return false;
		}
		if (!Constants.APP_PL_TYPE_GOLD.equals(
				quote.getLoanQuoteLoanPurposeId())) {
			logger.info(
					"Gold consent skipped because "
					+ "loan purpose is not Gold Loan"
			);
			return true;
		}
		if (!"Y".equalsIgnoreCase(
				quote.getQuotePrivacyConsentFlag())) {
			logger.info(
					"Gold privacy consent was not accepted"
			);
			return false;
		}
		if (!ValidatorUtil.isValid(
				quote.getQuotePrivacyLocale())) {
			quote.setQuotePrivacyLocale("eng");
		}
		String mobile =
				application.getAppMobileNo();
		if (!ValidatorUtil.isValid(mobile)) {
			mobile = quote.getAppMobile();
		}
		if (!ValidatorUtil.isValid(mobile)) {
			logger.info(
					"Gold consent mobile number is missing"
			);
			return false;
		}
		String email =
				application.getAppWorkEmail();
		if (!ValidatorUtil.isValid(email)) {
			email = quote.getAppEmail();
		}
		/*
		 * Prevent duplicate CCMS calls when customer retries,
		 * refreshes or resubmits the Gold Loan page.
		 */
		if (ValidatorUtil.isValid(
				application.getAppCcmsConsentId())) {
			logger.info(
					"Gold CCMS consent already exists. "
					+ "Skipping API call. consentId={}, appSeqId={}",
					application.getAppCcmsConsentId(),
					application.getAppSeqId()
			);
			if (!ValidatorUtil.isValid(
					quote.getQuoteCcmsConsentId())) {
				quote.setQuoteCcmsConsentId(
						application
								.getAppCcmsConsentId()
				);
				personalLoanService.save(quote);
			}
			return true;
		}
		if (ValidatorUtil.isValid(
				quote.getQuoteCcmsConsentId())) {
			application.setAppCcmsConsentId(
					quote.getQuoteCcmsConsentId()
			);
			application.setAppNtbId(
					quote.getQuoteNtbId()
			);
			application.setAppPrivacyConsentFlag("Y");
			application.setAppPrivacyLocale(
					quote.getQuotePrivacyLocale()
			);
			application =
					personalLoanService.save(
							application
					);
			return application != null;
		}
		/*
		 * Gold Loan must use 27, not PERSONAL_LOAN_ID,
		 * when generating the NTB ID.
		 */
		Integer goldLoanTypeId =
				Constants.APP_PL_TYPE_GOLD;
		String ntbId =
				consentUtil.generateNtbId(
						mobile,
						goldLoanTypeId
				);
		/*
		 * Save generated backend NTB ID before CCMS call.
		 */
		quote.setQuoteNtbId(ntbId);
		quote.setQuotePrivacyConsentFlag("Y");
		application.setAppNtbId(ntbId);
		application.setAppPrivacyConsentFlag("Y");
		application.setAppPrivacyLocale(
				quote.getQuotePrivacyLocale()
		);
		quote =
				personalLoanService.save(quote);
		application =
				personalLoanService.save(application);
		if (quote == null || application == null) {
			logger.info(
					"Unable to save Gold consent metadata "
					+ "before CCMS call"
			);
			return false;
		}
		String rawIpAddress =
				this.SbiUtil.getIPAddress();
		String ipAddress =
				rawIpAddress != null
						? rawIpAddress.replace(",", "")
						: "";
		/*
		 * Reuse the same common CCMS API used by all loans.
		 */
		ConsentWriteLog consentWrite =
				consentUtil.callCCMSConsentWriteAPI(
						ntbId,
						mobile,
						email,
						ipAddress,
						quote.getQuotePrivacyLocale(),
						goldLoanTypeId
				);
		if (consentWrite == null) {
			logger.info(
					"Gold CCMS consent response is null"
			);
			return false;
		}
		boolean ccmsSuccess =
				"200".equalsIgnoreCase(
						consentWrite.getResponseCode()
				)
				&& "true".equalsIgnoreCase(
						consentWrite.getResponseStatus()
				)
				&& "Y".equalsIgnoreCase(
						consentWrite.getIsActive()
				)
				&& ValidatorUtil.isValid(
						consentWrite.getConsentId()
				);
		if (!ccmsSuccess) {
			logger.info(
					"Gold CCMS consent failed. "
					+ "responseCode={}, responseStatus={}, active={}",
					consentWrite.getResponseCode(),
					consentWrite.getResponseStatus(),
					consentWrite.getIsActive()
			);
			return false;
		}
		String ccmsConsentId =
				consentWrite.getConsentId().trim();
		quote.setQuoteCcmsConsentId(
				ccmsConsentId
		);
		application.setAppCcmsConsentId(
				ccmsConsentId
		);
		quote =
				personalLoanService.save(quote);
		application =
				personalLoanService.save(application);
		if (quote == null || application == null) {
			logger.info(
					"Gold CCMS succeeded but consent ID "
					+ "could not be saved locally"
			);
			return false;
		}
		logger.info(
				"Gold CCMS consent successful. "
				+ "appSeqId={}, quoteId={}, ntbId={}, consentId={}",
				application.getAppSeqId(),
				quote.getLoanQuoteId(),
				ntbId,
				ccmsConsentId
		);
		return true;
	} catch (Exception e) {
		logger.info(
				"Exception in Gold Loan CCMS consent flow",
				e
		);
		return false;
	}
}

Add imports if missing:

import com.mintstreet.consent.entity.ConsentWriteLog;

The existing class already uses consentUtil, ValidatorUtil, Constants, and SbiUtil, so no duplicate dependency is needed.

⸻

5. Reference number generation stays after CCMS

Later in processSubmitQuote(), your code generates the reference number when:

!ValidatorUtil.isValid(
		appFormData.getAppReferenceId()
)

and then saves appFormData. Pasted text (3)(12).txt

Keep that code unchanged.

Because the Gold CCMS block is now placed before it, the final sequence becomes:

save app data
→ Gold CCMS write
→ save NTB ID and consent ID
→ generate app reference ID
→ save reference ID
→ continue PDF/CRM/thank-you flow

This matches your one-page Gold Loan requirement.

⸻

6. Frontend hidden fields

In GoldFirstPageSession.jsp, the submit button is:

<input type="submit"
	class="submit-btn"
	name="subtn"
	id="subtn"
	value="SUBMIT APPLICATION">

Pasted text(43).txt

Add these fields before the terms section:

<s:hidden
	name="quote.quotePrivacyConsentFlag"
	id="quotePrivacyConsentFlag"/>
<s:hidden
	name="quote.quotePrivacyLocale"
	id="quotePrivacyLocale"/>
<s:hidden
	name="quote.quoteNtbId"
	id="quoteNtbId"/>

Do not generate NTB ID in JavaScript. quoteNtbId can remain blank during frontend submit.

On popup Accept, set only:

$("#quotePrivacyConsentFlag").val("Y");
$("#quotePrivacyLocale").val(
	$("#goldPrivacyLocaleDropdown").val()
);
$("#infoprovide").prop("checked", true);

⸻

7. Gold submit validation

At the bottom of GoldFirstPageSession.jsp:

<script type="text/javascript">
$(document).on(
	"click.goldPrivacyConsent",
	"#subtn",
	function(event) {
		if ($("#quotePrivacyConsentFlag").val()
				!== "Y") {
			event.preventDefault();
			alert(
				"Please read and accept SBI Privacy Notice before proceeding."
			);
			return false;
		}
		if (!$("#infoprovide").is(":checked")) {
			event.preventDefault();
			alert(
				"Please accept SBI Privacy Notice before proceeding."
			);
			return false;
		}
		if ($.trim(
				$("#quotePrivacyLocale").val()
			) === "") {
			event.preventDefault();
			alert(
				"Please select and accept the privacy notice language."
			);
			return false;
		}
		return true;
	}
);
</script>

Do not validate quoteNtbId; the backend creates it.

⸻

Why the earlier solution did not work

The earlier solution used the consent call inside:

processGetQuotes()

That is designed around the eligibility/quote/BRE flow.

Gold Loan’s final submission instead enters:

processSubmitQuote()

from the Gold-specific state-17 branch, and then immediately returns the thank-you page after processing. Pasted text (2)(17).txt

Therefore, the reliable Gold integration point is:

processSubmitQuote()

immediately after:

appFormData =
		this.personalLoanService.save(appFormData);

and before reference-number generation.
