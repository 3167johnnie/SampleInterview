package com.mintstreet.consent.service;


import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.CcmsUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.UuidV7Generator;
import com.mintstreet.consent.bo.ConsentReadResponse;
import com.mintstreet.consent.bo.ConsentRequest;
import com.mintstreet.consent.bo.ConsentRequestConsent;
import com.mintstreet.consent.bo.ConsentRequestDpData;
import com.mintstreet.consent.bo.ConsentRequestEisPayload;
import com.mintstreet.consent.bo.ConsentRequestPayloadBody;
import com.mintstreet.consent.bo.ConsentRequestPayloadHeaders;
import com.mintstreet.consent.bo.ConsentResponseBankProducts;
import com.mintstreet.consent.bo.ConsentResponseConsentWrite;
import com.mintstreet.consent.bo.ConsentResponsePurposeEnquiry;
import com.mintstreet.consent.dao.ConsentPurposeDao;
import com.mintstreet.consent.dao.ConsentReadDao;
import com.mintstreet.consent.dao.ConsentWriteDao;
import com.mintstreet.consent.entity.CCMSConfig;
import com.mintstreet.consent.entity.ConsentPurposeLog;
import com.mintstreet.consent.entity.ConsentReadLog;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.bo.ConsentResponsePurposeEisBody;
import com.mintstreet.consent.bo.ConsentResponsePurposes;

public class ConsentService {

	private static final Logger logger = LogManager.getLogger(ConsentService.class.getName());

	@Autowired
	protected CommonService commonService; 

	@Autowired
	private CcmsUtil ccmsUtil;
	  
	private ConsentPurposeDao consentPurposeDao;
	
	private ConsentWriteDao consentWriteDao;
	
	private ConsentReadDao consentReadDao;

	public ConsentPurposeDao getConsentPurposeDao() {
		return consentPurposeDao;
	}

	public void setConsentPurposeDao(ConsentPurposeDao consentPurposeDao) {
		this.consentPurposeDao = consentPurposeDao;
	}
	
	public ConsentWriteDao getConsentWriteDao() {
		return consentWriteDao;
	}

	public void setConsentWriteDao(ConsentWriteDao consentWriteDao) {
		this.consentWriteDao = consentWriteDao;
	} 

	public ConsentReadDao getConsentReadDao() {
		return consentReadDao;
	}

	public void setConsentReadDao(ConsentReadDao consentReadDao) {
		this.consentReadDao = consentReadDao;
	}

	public ConsentPurposeLog generatePurposeRequest() {
		
		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		
		String corelationId = UuidV7Generator.generateV7().toString();
		CCMSConfig config = commonService.getCcmsConfigById(1);
		
		payloadHeaders.setAcceptLanguage(config.getAcceptLanguage());
		payloadHeaders.setxCorelationId(corelationId);
		payloadHeaders.setxApiVersion(config.getApiVersion());
		
		payloadBody.setTouchPointId(config.getTouchPointId());
		payloadBody.setPurposeSetId(config.getPurposeSetId());
		
		eisPayload.setHeaders(payloadHeaders);
		eisPayload.setBody(payloadBody);
		
		request.setSourceId(config.getSourceId());
		request.setEisPayload(eisPayload);
		request.setDestination(config.getDestination());
		request.setTransactionType(config.getTransactionType());
		request.setTransactionSubType("READ_PURPOSES");
		
		//convert request into JSON object
		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);
		
		//save request
		ConsentPurposeLog purpose = new ConsentPurposeLog();
		purpose.setxCorrelationId(corelationId);
		purpose.setPuposeRequest(consentRequest.toString());
		purpose.setRequestEntryTime(new Date());
		purpose = consentPurposeDao.save(purpose.getPurposeId(), purpose);
		return purpose;
	}

	public ConsentResponsePurposeEnquiry readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {

		//call EIS for Purpose API
		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(purpose.getPuposeRequest());
		
		String responseStr = responseJson.toString();
		
		//convert response into JSON object
		ConsentResponsePurposeEnquiry purposeResponse = new ConsentResponsePurposeEnquiry();
		
		purposeResponse = (ConsentResponsePurposeEnquiry) JSONUtil.getObjctFromJSON(purposeResponse, responseStr);
		
		//store response
		purpose.setResponseCode(purposeResponse.getEisResponse().getStatusCode());
		purpose.setResponseMsg(responseStr);
		purpose.setResponseStatus(purposeResponse.getEisResponse().getSuccess());
		purpose.setResponseEntryTime(new Date());
		
		consentPurposeDao.save(purpose.getPurposeId(), purpose);
		
		return purposeResponse;
		
	}
	
	private ConsentResponsePurposeEnquiry getPurposeResponseForWrite() throws JSONException {

		ConsentPurposeLog purposeLog = generatePurposeRequest();

		ConsentResponsePurposeEnquiry purposeResponse =
				readPurposeFromCCMS(purposeLog);

		if (purposeResponse == null
				|| purposeResponse.getEisResponse() == null
				|| purposeResponse.getEisResponse().getBody() == null
				|| purposeResponse.getEisResponse().getBody().isEmpty()) {

			throw new RuntimeException("Purpose Enquiry response body is empty.");
		}

		return purposeResponse;
	}
    
	private List<ConsentRequestConsent> prepareConsentsFromPurposeResponse(ConsentResponsePurposeEnquiry purposeResponse) {
		List<ConsentRequestConsent> consents = new ArrayList<ConsentRequestConsent>();
		if (purposeResponse == null || purposeResponse.getEisResponse() == null
				|| purposeResponse.getEisResponse().getBody() == null
				|| purposeResponse.getEisResponse().getBody().isEmpty()) {
			logger.warn("Purpose Enquiry response/body is empty.");
			return consents;
		}
		for (ConsentResponsePurposeEisBody container : purposeResponse.getEisResponse().getBody()) {
			if (container == null || container.getPurposes() == null || container.getPurposes().isEmpty()) {
				continue;
			}
			for (ConsentResponsePurposes purpose : container.getPurposes()) {
				if (purpose == null || isBlank(purpose.getCode()) || isBlank(purpose.getVersion())) {
					logger.warn("Skipping invalid Purpose Enquiry purpose. " + "Purpose data: {}", purpose);
					continue;
				}
				ConsentRequestConsent consent = new ConsentRequestConsent();
				consent.setPurposeCode(purpose.getCode());
				consent.setPurposeVersion(purpose.getVersion());
				/*
				 * Temporarily true for your current testing. Later use actual customer consent.
				 */
				consent.setConsented("true");
				/*
				 * Both lists are initialized. Therefore, if no bank products are returned, JSON
				 * will contain empty arrays [].
				 */
				List<String> consentedProducts = new ArrayList<String>();
				List<String> notConsentedProducts = new ArrayList<String>();
				/*
				 * Never hardcode HOME_LOAN. Use only bankProducts returned from Purpose
				 * Enquiry.
				 */
				if (purpose.getBankProducts() != null && !purpose.getBankProducts().isEmpty()) {
					for (ConsentResponseBankProducts bankProduct : purpose.getBankProducts()) {
						if (bankProduct == null || isBlank(bankProduct.getCode())) {
							continue;
						}
						String bankProductCode = bankProduct.getCode().trim();
						/*
						 * Current testing: all purposes are consented=true.
						 *
						 * Therefore, all configured product codes go into consentedProducts.
						 */
						if ("true".equalsIgnoreCase(consent.getConsented())) {
							consentedProducts.add(bankProductCode);
						} else {
							notConsentedProducts.add(bankProductCode);
						}
					}
				}
				/*
				 * Add product lists only for product-specific purposes.
				 */
				if ("true".equalsIgnoreCase(purpose.getIsProductSpecific())) {
					consent.setConsentedProducts(consentedProducts);
					consent.setNotConsentedProducts(notConsentedProducts);
				}
				consents.add(consent);
				logger.info(
						"Prepared CCMS consent. " + "purposeCode: {}, " + "purposeVersion: {}, " + "consented: {}, "
								+ "productSpecific: {}, " + "consentedProducts: {}, " + "notConsentedProducts: {}",
						consent.getPurposeCode(), consent.getPurposeVersion(), consent.getConsented(),
						purpose.getIsProductSpecific(), consent.getConsentedProducts(),
						consent.getNotConsentedProducts());
			}
		}
		return consents;
	}

	
    public ConsentReadLog generateConsentReadRequest(String cifNumber) {
		
		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		
		String corelationId = UuidV7Generator.generateV7().toString();
		CCMSConfig config = commonService.getCcmsConfigById(1);
		
		payloadHeaders.setAcceptLanguage(config.getAcceptLanguage());
		payloadHeaders.setxCorelationId(corelationId);
		payloadHeaders.setxApiVersion(config.getApiVersion());
		
		payloadBody.setTouchPointId(config.getTouchPointId());
		payloadBody.setPurposeSetId(config.getPurposeSetId());
		
		if (cifNumber != null && !"".equals(cifNumber)) {
			payloadBody.setDpCif(cifNumber);
		}
		payloadBody.setNtbId("");
		
		eisPayload.setHeaders(payloadHeaders);
		eisPayload.setBody(payloadBody);
		
		request.setSourceId(config.getSourceId());
		request.setEisPayload(eisPayload);
		request.setDestination(config.getDestination());
		request.setTransactionType(config.getTransactionType());
		request.setTransactionSubType("READ_CONSENTS");
		
		//convert request into JSON object
		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);
		
		//save request
		ConsentReadLog read = new ConsentReadLog();
		//read.setConentReadId(1);
		read.setConsentReadxCorrelationId(corelationId);
		read.setConsentReadDpCIF(cifNumber);
		read.setConsentReadRequest(consentRequest.toString());
		read.setRequestEntryTime(new Date());
		
		read = consentReadDao.save(read.getConentReadId(),read);
		
		return read;
	}

	public ConsentReadResponse consentReadFromCCMS(ConsentReadLog consentRead) throws JSONException {
		
		//call EIS for Consent read API
		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(consentRead.getConsentReadRequest());
		
		String responseStr = responseJson.toString();
		
		//convert response into JSON object
		ConsentReadResponse readResponse = new ConsentReadResponse();
		
		readResponse = (ConsentReadResponse) JSONUtil.getObjctFromJSON(readResponse, responseStr);
		
		//store response
		consentRead.setResponseCode(readResponse.getEisResponse().getStatusCode().toString());
		consentRead.setResponseMsg(responseStr);
		consentRead.setResponseStatus(readResponse.getEisResponse().getSuccess());
		consentRead.setResponseEntryTime(new Date());
		
		consentReadDao.save(consentRead.getConentReadId(), consentRead);
		
		return readResponse;
	}

	public ConsentWriteLog generateConsentWriteRequest(ConsentRequestDpData dpData, List<ConsentRequestConsent> consents, String loanType) {
		
		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		
		String corelationId = UuidV7Generator.generateV7().toString();
		CCMSConfig config = commonService.getCcmsConfigById(1);
		
		payloadHeaders.setAcceptLanguage("eng");
		payloadHeaders.setxCorelationId(corelationId);
		payloadHeaders.setxApiVersion("1");
		
		payloadBody.setTouchPointId(config.getTouchPointId());
		payloadBody.setPurposeSetId(config.getPurposeSetId());
		payloadBody.setDpData(dpData);
		payloadBody.setConsents(consents);
		
		eisPayload.setHeaders(payloadHeaders);
		eisPayload.setBody(payloadBody);
		
		request.setSourceId("OC");
		request.setEisPayload(eisPayload);
		request.setDestination("CRM");
		request.setTransactionType("CCMS");
		request.setTransactionSubType("UPDATE_CONSENTS");
		
		//convert request into JSON object
		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);
		
		//save request
		ConsentWriteLog consentWrite = new ConsentWriteLog();
		consentWrite.setxCorrelationId(corelationId);
		consentWrite.setConsentWriteRequest(consentRequest.toString());
		consentWrite.setRequestEntryTime(new Date());
		consentWrite.setCifNumber(dpData.getDpCIF());
		consentWrite.setLoanType(loanType);
		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
		return consentWrite;
	}
	
	public ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress,
			String locale) {

		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		ConsentRequestDpData dpData = new ConsentRequestDpData();

		// ConsentRequestConsent consent1 = new ConsentRequestConsent();
		// ConsentRequestConsent consent2 = new ConsentRequestConsent();

		// List<ConsentRequestConsent> consents = new ArrayList<>();
		// List<String> consentedProducts = new ArrayList<>();

		String correlationId = UuidV7Generator.generateV7().toString();
		CCMSConfig config = commonService.getCcmsConfigById(1);

		if (locale == null || locale.trim().equals("")) {
			locale = "eng";
		}

		payloadHeaders.setAcceptLanguage(config.getAcceptLanguage());
		payloadHeaders.setxCorelationId(correlationId);
		payloadHeaders.setxApiVersion(config.getApiVersion());

		dpData.setDpCIF("");
		dpData.setNtbId(ntbId);
		dpData.setDpMobile(mobile);
		dpData.setDpEmail(email);
		dpData.setDpIPAddress(ipAddress);
		dpData.setLocale(locale);
		dpData.setTimestamp(getCurrentIsoTimestamp());

//		consent1.setPurposeCode("PR-FACILITA-000099");
//		consent1.setPurposeVersion("1");
//		consent1.setConsented("true");

//		PR-FACILITA-000099  1	(PR-REGULATO-000100, 1); (PR-INCIDENT-000101, 2); (PR-MANAGEYO-000102, 1);
//		(PR-SERVICEI-000077, 1); (PR-ANALYTIC-000105, 2);
//		(PR-PROMOTIO-000104, 1); (PR-PROMOTIO-000106, 3); (PR-USERPROF-000118, 2)"

//		consentedProducts.add("HOME_LOAN");

//		consent2.setPurposeCode("PURP-DEMOPURP-000071");

//		consent2.setPurposeCode("PR-PROMOTIO-000106");
//		consent2.setPurposeVersion("1");
//		consent2.setConsented("true");
//		consent2.setConsentedProducts(consentedProducts);

//		consents.add(consent1);
//		consents.add(consent2);

		ConsentResponsePurposeEnquiry purposeResponse;

		try {
			purposeResponse = getPurposeResponseForWrite();
		} catch (JSONException e) {
			throw new RuntimeException("Unable to get Purpose Enquiry response from CCMS.", e);
		}

		List<ConsentRequestConsent> consents = prepareConsentsFromPurposeResponse(purposeResponse);

		if (consents == null || consents.isEmpty()) {
			throw new RuntimeException("No purpose found from CCMS Purpose Enquiry response.");
		}

		payloadBody.setTouchPointId(config.getTouchPointId());
		payloadBody.setPurposeSetId(config.getPurposeSetId());
		payloadBody.setDpData(dpData);
		payloadBody.setConsents(consents);

		eisPayload.setHeaders(payloadHeaders);
		eisPayload.setBody(payloadBody);

		request.setSourceId(config.getSourceId());
		request.setEisPayload(eisPayload);
		request.setDestination(config.getDestination());
		request.setTransactionType(config.getTransactionType());
		request.setTransactionSubType("UPDATE_CONSENTS");

		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);

//		ConsentWriteLog consentWrite = new ConsentWriteLog();
//		consentWrite.setxCorrelationId(correlationId);
//		consentWrite.setConsentWriteRequest(consentRequest.toString());
//		consentWrite.setNtbNumber(ntbId);
//		consentWrite.setRequestEntryTime(new Date());
//
//		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
//
//		return consentWrite;
		
		ConsentWriteLog consentWrite = new ConsentWriteLog();

		consentWrite.setxCorrelationId(correlationId);
		consentWrite.setNtbNumber(ntbId);
		consentWrite.setConsentWriteRequest(consentRequest.toString());
		consentWrite.setRequestEntryTime(new Date());

		logger.info("Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}", correlationId, ntbId);

		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

		if (consentWrite == null || consentWrite.getConsentWriteId() == null) {

			throw new RuntimeException("Unable to save CCMS Consent Write request log.");
		}

		logger.info("CCMS Consent Write request log saved. writeId: {}, correlationId: {}", consentWrite.getConsentWriteId(), correlationId);

		return consentWrite;
	}
	
	
//	public ConsentWriteLog generateConsentWriteRequest() {
//		
//		ConsentRequest request = new ConsentRequest();
//		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
//		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
//		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
//		ConsentRequestDpData dpData = new ConsentRequestDpData();
//		ConsentRequestConsent consent1 = new ConsentRequestConsent();
//		ConsentRequestConsent consent2 = new ConsentRequestConsent();
//		List<ConsentRequestConsent> consents = new ArrayList<>();
//		List<String> consentedProducts = new ArrayList<>();
//		
//		String corelationId = UuidV7Generator.generateV7().toString();
//		CCMSConfig config = commonService.getCcmsConfigById(1);
//		
//		payloadHeaders.setAcceptLanguage("eng");
//		payloadHeaders.setxCorelationId(corelationId);
//		payloadHeaders.setxApiVersion("1");
//		
//		dpData.setDpCIF("123456");
//		dpData.setDpMobile("998877554466");
//		dpData.setDpEmail("test@gmail.com");
//		dpData.setDpIPAddress("10.0.0.1");
//		dpData.setLocale("eng");
//		dpData.setTimestamp("2025-04-15T14:30:00+05:30");
//		
//		consent1.setPurposeCode("PURP-DEMOPURP-000068");
//		consent1.setPurposeVersion("1");
//		consent1.setConsented("true");
//
//		consentedProducts.add("HOME_LOAN");
//		
//		consent2.setPurposeCode("PURP-DEMOPURP-000071");
//		consent2.setPurposeVersion("1");
//		consent2.setConsented("true");
//		consent2.setConsentedProducts(consentedProducts);
//		
//		consents.add(consent1);
//		consents.add(consent2);
//		
//		payloadBody.setTouchPointId(config.getTouchPointId());
//		payloadBody.setPurposeSetId(config.getPurposeSetId());
//		payloadBody.setDpData(dpData);
//		payloadBody.setConsents(consents);
//		
//		eisPayload.setHeaders(payloadHeaders);
//		eisPayload.setBody(payloadBody);
//		
//		request.setSourceId("OC");
//		request.setEisPayload(eisPayload);
//		request.setDestination("CRM");
//		request.setTransactionType("CCMS");
//		request.setTransactionSubType("UPDATE_CONSENTS");
//		
//		//convert request into JSON object
//		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);
//		
//		//save request
//		ConsentWriteLog consentWrite = new ConsentWriteLog();
//		consentWrite.setxCorrelationId(corelationId);
//		consentWrite.setConsentWriteRequest(consentRequest.toString());
//		consentWrite.setRequestEntryTime(new Date());
//		
//		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
//		
//		return consentWrite;
//	}
	
	
	

//	public void writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {
//
//		//call EIS for Write API
//		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(consentWrite.getConsentWriteRequest());
//		
//		//String responseStr = "{\"EIS_RESPONSE\": {\"success\": true,    \"statusCode\": 200,    \"messages\": [],    \"locale\": \"eng\",    \"errors\": {},    \"body\": {      \"dpCIF\": \"12345678901234568\",      \"dpMobile\": \"9898989898\",      \"dpEmail\": \"a@b.c\",      \"ntbId\": null,      \"consentId\": \"019b4a6e-fed3-7fda-97f2-a716d6280128\"    },    \"timestamp\": \"2025-12-23T14:29:06.908153600\",    \"correlationId\": \"f2bcebd5-9058-4d04-95ad-b9f9f76b5d58\"  },  \"ERROR_CODE\": \"\",  \"ERROR_DESCRIPTION\": \"\",  \"RESPONSE_STATUS\": \"0\"}";
//		String responseStr = responseJson.toString();
//		
//		//convert response into JSON object
//		ConsentResponseConsentWrite writeResponse = new ConsentResponseConsentWrite();
//		
//		writeResponse = (ConsentResponseConsentWrite) JSONUtil.getObjctFromJSON(writeResponse, responseStr);
//		
//		//store response
//		consentWrite.setResponseCode(writeResponse.getEisResponse().getStatusCode());
//		consentWrite.setResponseMsg(responseStr);
//		consentWrite.setResponseStatus(writeResponse.getEisResponse().getSuccess());
//		consentWrite.setResponseEntryTime(new Date());
//		
//		consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
//		
//	}
	
	
//	public ConsentWriteLog writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {
//
//		//call EIS for Write API
//		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(consentWrite.getConsentWriteRequest());
//		logger.info("responseJson::" + responseJson);
//		
//		String responseStr = responseJson.toString();
//		
//		//convert response into JSON object
//		ConsentResponseConsentWrite writeResponse = new ConsentResponseConsentWrite();
//		
//		writeResponse = (ConsentResponseConsentWrite) JSONUtil.getObjctFromJSON(writeResponse, responseStr);
//		logger.info("writeResponse::" + writeResponse);
//
//		String statusCode = writeResponse.getEisResponse().getStatusCode();
//		String consentId = "";
//		if (statusCode != null && statusCode.equals("200")) {
//			consentId = writeResponse.getEisResponse().getEisBody().getConsentId();
//		}
//		
//		//store response
//		consentWrite.setResponseCode(statusCode);
//		consentWrite.setResponseMsg(responseStr);
//		consentWrite.setResponseStatus(writeResponse.getEisResponse().getSuccess());
//		consentWrite.setResponseEntryTime(new Date());
//		consentWrite.setConsentId(consentId);
//		consentWrite.setIsActive("Y");
//		
//		consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
//		
//		return consentWrite;
//	}
	
	public ConsentWriteLog writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {

		if (consentWrite == null) {
			throw new IllegalArgumentException("Consent Write log cannot be null.");
		}

		if (consentWrite.getConsentWriteId() == null) {
			throw new IllegalArgumentException("Consent Write log ID cannot be null.");
		}

		if (consentWrite.getConsentWriteRequest() == null || consentWrite.getConsentWriteRequest().trim().isEmpty()) {

			throw new IllegalArgumentException("Consent Write request cannot be empty.");
		}

		logger.info("Calling CCMS Consent Write API. writeId: {}, correlationId: {}, ntbId: {}",
				consentWrite.getConsentWriteId(), consentWrite.getxCorrelationId(),consentWrite.getNtbNumber());

		try {

			JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(consentWrite.getConsentWriteRequest());

			if (responseJson == null || responseJson.length() == 0) {

				consentWrite.setResponseCode("EMPTY_RESPONSE");
				consentWrite.setResponseStatus("false");
				consentWrite.setResponseMsg("Empty response received from CCMS Consent Write API.");
				consentWrite.setResponseEntryTime(new Date());

				consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

				throw new RuntimeException("Empty response received from CCMS Consent Write API.");
			}

			String responseStr = responseJson.toString();

			logger.info("CCMS Consent Write response received. writeId: {}, response: {}",
					consentWrite.getConsentWriteId(), responseStr);

			ConsentResponseConsentWrite writeResponse = new ConsentResponseConsentWrite();

			writeResponse = (ConsentResponseConsentWrite) JSONUtil.getObjctFromJSON(writeResponse, responseStr);

			if (writeResponse == null || writeResponse.getEisResponse() == null) {

				consentWrite.setResponseCode("INVALID_RESPONSE");
				consentWrite.setResponseStatus("false");
				consentWrite.setResponseMsg(responseStr);
				consentWrite.setResponseEntryTime(new Date());

				consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

				throw new RuntimeException("EIS_RESPONSE not found in CCMS Consent Write response.");
			}

			String responseCode = String.valueOf(writeResponse.getEisResponse().getStatusCode());

			String responseStatus = String.valueOf(writeResponse.getEisResponse().getSuccess());

			consentWrite.setResponseCode(responseCode);
			consentWrite.setResponseStatus(responseStatus);
			consentWrite.setResponseMsg(responseStr);
			consentWrite.setResponseEntryTime(new Date());

			
			if (consentWrite.getNtbNumber() == null || consentWrite.getNtbNumber().trim().isEmpty()) {

				logger.warn("NTB ID is missing in Consent Write log. writeId: {}", consentWrite.getConsentWriteId());
			}

			consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

			boolean success = "200".equals(responseCode) && "true".equalsIgnoreCase(responseStatus);

			if (success) {

				logger.info("CCMS Consent Write successful. writeId: {}, correlationId: {}, ntbId: {}",
						consentWrite.getConsentWriteId(), consentWrite.getxCorrelationId(),
						consentWrite.getNtbNumber());

			} else {

				logger.warn("CCMS Consent Write failed. writeId: {}, responseCode: {}, responseStatus: {}",
						consentWrite.getConsentWriteId(), responseCode, responseStatus);
			}

			return consentWrite;

		} catch (JSONException e) {

			updateConsentWriteFailure(consentWrite, "JSON_ERROR", e.getMessage());

			logger.error("JSON error while processing CCMS Consent Write response. writeId: {}",
					consentWrite.getConsentWriteId(), e);

			throw e;

		} catch (RuntimeException e) {

			/*
			 * Avoid overwriting a detailed failure already saved above.
			 */
			if (consentWrite.getResponseEntryTime() == null) {

				updateConsentWriteFailure(consentWrite, "PROCESSING_ERROR", e.getMessage());
			}

			logger.error("Error while processing CCMS Consent Write. writeId: {}", consentWrite.getConsentWriteId(), e);

			throw e;
		}
	}
	
	private String getCurrentIsoTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		return sdf.format(new Date());
	}
	public ConsentWriteLog getConsentWriteLog(String cifNumber,String consentId) {
		return consentWriteDao.getConsentWriteLog(cifNumber,consentId);
	
		
	}
	public List<ConsentWriteLog> getConsentRevokeData(String cifNumber,String loanType) {
		List<ConsentWriteLog> clist=null;
		try {
			clist=consentWriteDao.getConsentRevokeData(cifNumber,loanType);
			return clist;
		} catch (NoResultException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clist;
		
	}
	
	private void updateConsentWriteFailure(ConsentWriteLog consentWrite, String responseCode, String responseMessage) {

		try {

			consentWrite.setResponseCode(responseCode);
			consentWrite.setResponseStatus("false");
			consentWrite.setResponseMsg(responseMessage);
			consentWrite.setResponseEntryTime(new Date());

			consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

		} catch (Exception saveException) {

			logger.error("Unable to update failed CCMS Consent Write log. writeId: {}",
					consentWrite.getConsentWriteId(), saveException);
		}
	}
	
	private boolean isBlank(String value) {
		return value == null || value.trim().isEmpty();
	}
}
