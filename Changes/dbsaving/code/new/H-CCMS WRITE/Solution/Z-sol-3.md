Your build error is because ConsentUtil.java is still calling old method:

consentService.generateConsentWriteRequest();

But now method needs 5 parameters:

generateConsentWriteRequest(ntbId, mobile, email, ipAddress, locale)

Since CCMS Write should not be called from ConsentUtil, remove/disable this old write method.

1) Change ConsentUtil.java

Replace this import section

import com.mintstreet.consent.entity.ConsentWriteLog;

With nothing

Remove it completely if no longer used.

⸻

2) Replace old callCCMSConsentWriteAPI() method

Remove this old method

public void callCCMSConsentWriteAPI() {
	
	try {
		// generate plain request
		ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest();
		
		// send request and read purpose from CCMS
		consentService.writeConsentToCCMS(consentWrite);
	} catch (JSONException e) {
		e.printStackTrace();// TODO add loggers
	}
}

Add this disabled method instead

/*
 * CCMS Write API should not be called from this util/test flow.
 * It is now called from HomeProcessManagerImpl.processGetQuotes()
 * after quote/application creation and before BRE call.
 */
public void callCCMSConsentWriteAPI() {
	throw new UnsupportedOperationException(
			"CCMS Write API must be called from Get Quote backend flow only."
	);
}

This will fix your compilation error.

⸻

Final ConsentUtil.java

package com.mintstreet.common.util;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import com.mintstreet.consent.entity.ConsentPurposeLog;
import com.mintstreet.consent.entity.ConsentReadLog;
import com.mintstreet.consent.service.ConsentService;
public class ConsentUtil {
	@Autowired
	private ConsentService consentService;
	public void callCCMSPurposeEnquiryAPI() {
		try {
			ConsentPurposeLog purpose = consentService.generatePurposeRequest();
			consentService.readPurposeFromCCMS(purpose);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public void callCCMSConsentReadAPI() {
		try {
			ConsentReadLog consentRead = consentService.generateConsentReadRequest();
			consentService.consentReadFromCCMS(consentRead);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/*
	 * CCMS Write API should not be called from this util/test flow.
	 * It is now called from HomeProcessManagerImpl.processGetQuotes()
	 * after quote/application creation and before BRE call.
	 */
	public void callCCMSConsentWriteAPI() {
		throw new UnsupportedOperationException(
				"CCMS Write API must be called from Get Quote backend flow only."
		);
	}
}

Why this is correct

Your new CCMS Write flow needs real values:

ntbId
mobile
email
ipAddress
locale

These values are available only during Get Quote backend flow, after quote/application is created. So old utility method without parameters should not call CCMS Write anymore.
