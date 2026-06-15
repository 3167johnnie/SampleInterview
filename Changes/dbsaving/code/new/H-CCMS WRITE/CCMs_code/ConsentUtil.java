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

}
