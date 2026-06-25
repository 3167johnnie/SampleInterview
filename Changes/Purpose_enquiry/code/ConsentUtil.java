package com.mintstreet.common.util;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.consent.entity.ConsentPurposeLog;
import com.mintstreet.consent.entity.ConsentReadLog;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;

public class ConsentUtil {

	@Autowired
	private ConsentService consentService;


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

	public void callCCMSConsentReadAPI() {
        
		try {
			// generate plain request
			ConsentReadLog consentRead = consentService.generateConsentReadRequest();
	
			// send request and consent read from CCMS
			consentService.consentReadFromCCMS(consentRead);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void callCCMSConsentWriteAPI(String ntbId, String mobile, String email, String ipAddress, String locale) throws Exception {
		
		try {
			// generate plain request
			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(ntbId, mobile, email, ipAddress, locale);
//			ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale)
			
			// send request and read purpose from CCMS
			consentService.writeConsentToCCMS(consentWrite);
		} catch (JSONException e) {
			e.printStackTrace();// TODO add loggers
		}

	}

}
