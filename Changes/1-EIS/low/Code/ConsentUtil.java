//package com.mintstreet.common.util;
//
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.mintstreet.consent.entity.ConsentPurposeLog;
//import com.mintstreet.consent.entity.ConsentReadLog;
//import com.mintstreet.consent.entity.ConsentWriteLog;
//import com.mintstreet.consent.service.ConsentService;
//
//public class ConsentUtil {
//
//	@Autowired
//	private ConsentService consentService;
//
//
//	public ConsentPurposeLog callCCMSPurposeEnquiryAPI() {
//		try {
//			// generate plain request
//			ConsentPurposeLog purpose = consentService.generatePurposeRequest();
//
//			// send request and read purpose from CCMS
//			consentService.readPurposeFromCCMS(purpose);
//		} catch (JSONException e) {
//			e.printStackTrace();//TODO add loggers
//		}
//		return null;
//
//	}
//
//	public void callCCMSConsentReadAPI() {
//        
//		try {
//			// generate plain request
//			ConsentReadLog consentRead = consentService.generateConsentReadRequest();
//	
//			// send request and consent read from CCMS
//			consentService.consentReadFromCCMS(consentRead);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
////	public void callCCMSConsentWriteAPI(String ntbId, String mobile, String email, String ipAddress, String locale) throws Exception {
////		
////		try {
////			// generate plain request
////			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(ntbId, mobile, email, ipAddress, locale);
//////			ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale)
////			
////			// send request and read purpose from CCMS
////			consentService.writeConsentToCCMS(consentWrite);
////		} catch (JSONException e) {
////			e.printStackTrace();// TODO add loggers
////		}
////
////	}
//	
//	public void callCCMSConsentWriteAPI(String ntbId, String mobile, String email, String ipAddress, String locale, String productCode) throws Exception {
//
//		try {
//			ConsentPurposeLog purpose = callCCMSPurposeEnquiryAPI();
//
//			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(ntbId, mobile, email, ipAddress,locale, productCode, purpose);
//
//			consentService.writeConsentToCCMS(consentWrite);
//
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//}



package com.mintstreet.common.util;

import java.util.List;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.consent.bo.ConsentReadResponse;
import com.mintstreet.consent.bo.ConsentRequestConsent;
import com.mintstreet.consent.bo.ConsentRequestDpData;
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

	public ConsentReadResponse callCCMSConsentReadAPI(String cifNumber) {
        
		ConsentReadResponse readResponse = new ConsentReadResponse();
		try {
			// generate plain request
			ConsentReadLog consentRead = consentService.generateConsentReadRequest(cifNumber);
			
			// send request and consent read from CCMS
			readResponse = consentService.consentReadFromCCMS(consentRead);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return readResponse;

	}
	
	public ConsentWriteLog callCCMSConsentWriteAPI(ConsentRequestDpData dpData, List<ConsentRequestConsent> consents, String loanType) {
		
		try {
			// generate plain request
			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(dpData, consents, loanType);
			
			// send request and read purpose from CCMS
			return consentService.writeConsentToCCMS(consentWrite);
		} catch (JSONException e) {
			e.printStackTrace();// TODO add loggers
			return null;
		}

	}

	public void callCCMSConsentWriteAPI(String ntbId, String mobile, String email, String ipAddress, String locale)
			throws Exception {
		try {
			// generate plain request
			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(ntbId, mobile, email, ipAddress,
					locale);
//		ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale)
			// send request and read purpose from CCMS
			consentService.writeConsentToCCMS(consentWrite);
		} catch (JSONException e) {
			e.printStackTrace();// TODO add loggers
		}
	}
	
	
	public ConsentWriteLog getConsentWriteLog(String cifNumber,String consentId) {
		 return consentService.getConsentWriteLog(cifNumber,consentId);
	}
	
	public List<ConsentWriteLog> getConsentRevokeData(String cifNumber,String loanType) {
		 List<ConsentWriteLog> clist=null;
		try {
			
			clist =  consentService.getConsentRevokeData(cifNumber,loanType);
			
			 return clist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return clist;

	}
		
}


