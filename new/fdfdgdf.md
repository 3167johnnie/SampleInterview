6	NTB page	Change existing text to new hardcoded text as suggested in document  (for all loans and Callback page)
		Create a new popup page to display Privacy Policy (default in English) fetched from OCAS DB
		Open popup page on click of checkbox
		Add dropdown to fetch list from language master in DB
		Fetch Privacy Policy based on language selected
		Add validations to force customer to scroll till the end to Submit and then close the popup
		Capture consent and store in db against that lead details (on click of Submit button for all loans)
		Generate ntbId based on pre-decided logic to send to CCMS
		Change existing logic to call CCMS API for write Consent after verification of OTP (for all loans and Callback page)
		Store CCMS response (consent id) in OCAS DB for future reference
		Change existing HL logic to verify OTP after 1st page (on Get Loan Quotes button)
