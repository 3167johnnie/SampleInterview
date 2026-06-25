package com.mintstreet.consent.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.CcmsUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.UuidV7Generator;
import com.mintstreet.consent.bo.ConsentPurposeContainer;
import com.mintstreet.consent.bo.ConsentPurposeEnquiryBody;
import com.mintstreet.consent.bo.ConsentPurposeEnquiryPayload;
import com.mintstreet.consent.bo.ConsentPurposeEnquiryRequest;
import com.mintstreet.consent.bo.ConsentPurposeEnquiryResponse;
import com.mintstreet.consent.bo.ConsentPurposeMaster;
import com.mintstreet.consent.bo.ConsentReadResponse;
import com.mintstreet.consent.bo.ConsentRequest;
import com.mintstreet.consent.bo.ConsentRequestConsent;
import com.mintstreet.consent.bo.ConsentRequestDpData;
import com.mintstreet.consent.bo.ConsentRequestEisPayload;
import com.mintstreet.consent.bo.ConsentRequestPayloadBody;
import com.mintstreet.consent.bo.ConsentRequestPayloadHeaders;
import com.mintstreet.consent.bo.ConsentResponseConsentWrite;
import com.mintstreet.consent.bo.ConsentResponsePurposeEnquiry;
import com.mintstreet.consent.dao.ConsentPurposeDao;
import com.mintstreet.consent.dao.ConsentReadDao;
import com.mintstreet.consent.dao.ConsentWriteDao;
import com.mintstreet.consent.entity.CCMSConfig;
import com.mintstreet.consent.entity.ConsentPurposeLog;
import com.mintstreet.consent.entity.ConsentReadLog;
import com.mintstreet.consent.entity.ConsentWriteLog;

public class ConsentService {

	//private static final Logger logger = LogManager.getLogger(ConsentService.class.getName());

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

	public void readPurposeFromCCMS(ConsentPurposeLog purpose) throws JSONException {

		//call EIS for Purpose API
		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(purpose.getPuposeRequest());
		
		//String responseStr = "{\"EIS_RESPONSE\":{\"success\":true,\"statusCode\":200,\"messages\":null,\"locale\":\"eng\",\"errors\":null,\"body\":[{\"containerTitle\":\"\",\"containerDescription\":\"\",\"purposes\":[{\"code\":\"PURP-DEMOPURP-000068\",\"version\":1,\"title\":\"Demo Purpose 1\",\"description\":\"Demo purpose 1 description\",\"validityDays\":365,\"dataPoints\":[\"Mobile\",\"email3\"],\"bankProducts\":[],\"isEssential\":true,\"isProductSpecific\":false,\"isNTB\":true,\"isETB\":true}],\"isContainer\":false},{\"containerTitle\":\"Test Container\",\"containerDescription\":\"This is a sample container.\",\"purposes\":[{\"code\":\"PURP-DEMOPURP-000069\",\"version\":1,\"title\":\"Demo Purpose 2\",\"description\":\"Demo purpose 2 description\",\"validityDays\":365,\"dataPoints\":[\"Mobile\",\"email3\"],\"bankProducts\":[],\"isEssential\":true,\"isProductSpecific\":false,\"isNTB\":true,\"isETB\":true},{\"code\":\"PURP-DEMOPURP-000071\",\"version\":1,\"title\":\"Demo Purpose 4\",\"description\":\"Demo purpose 4 description\",\"validityDays\":365,\"dataPoints\":[\"Mobile\",\"email3\"],\"bankProducts\":[{\"code\":\"HOME_LOAN\",\"label\":\"Home Loan\"}],\"isEssential\":true,\"isProductSpecific\":true,\"isNTB\":true,\"isETB\":true}],\"isContainer\":true}],\"timestamp\":\"2025-12-23T14:25:32.016028\",\"correlationId\":\"f2bcebd5-9058-4d04-95ad-b9f9f76b5d58\"},\"ERROR_CODE\":\"\",\"ERROR_DESCRIPTION\":\"\",\"RESPONSE_STATUS\":\"0\"}";
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
		
	}
    
    public ConsentReadLog generateConsentReadRequest() {
		
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
		payloadBody.setDpCif("");
		payloadBody.setNtbId("8691919417SanjivaniTest160003");
		
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
		read.setConsentReadDpCIF("");
		read.setConsentReadRequest(consentRequest.toString());
		read.setRequestEntryTime(new Date());
		
		read = consentReadDao.save(read.getConentReadId(),read);
		
		return read;
	}

	public void consentReadFromCCMS(ConsentReadLog consentRead) throws JSONException {
		
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
	
	
	
	public ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale) throws Exception {

		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		ConsentRequestDpData dpData = new ConsentRequestDpData();

//		ConsentRequestConsent consent1 = new ConsentRequestConsent();
//		ConsentRequestConsent consent2 = new ConsentRequestConsent();

//		List<ConsentRequestConsent> consents = new ArrayList<>();
		//List<String> consentedProducts = new ArrayList<>();

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
		
		
		ConsentPurposeEnquiryResponse purposeResponse = getPurposeEnquiryFromCCMS(locale);

		List<ConsentRequestConsent> consents = prepareConsentsFromPurposeEnquiry(purposeResponse,"HOME_LOAN");

		if (consents == null || consents.isEmpty()) {
			throw new RuntimeException("No purposes found from CCMS Purpose Enquiry API.");
		}
//		consent1.setPurposeCode("PR-FACILITA-000099");
//		consent1.setPurposeVersion("1");
//		consent1.setConsented("true");
//
////		consentedProducts.add("HOME_LOAN");
//
////		consent2.setPurposeCode("PURP-DEMOPURP-000071");
//		
////		consent2.setPurposeCode("PR-PROMOTIO-000106");
////		consent2.setPurposeVersion("1");
////		consent2.setConsented("true");
////		consent2.setConsentedProducts(consentedProducts);
//		
//		consents.add(consent1);
////		consents.add(consent2);

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

		ConsentWriteLog consentWrite = new ConsentWriteLog();
		consentWrite.setxCorrelationId(correlationId);
		consentWrite.setConsentWriteRequest(consentRequest.toString());
		consentWrite.setRequestEntryTime(new Date());

		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

		return consentWrite;
	}

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
	
	
	public ConsentWriteLog writeConsentToCCMS(ConsentWriteLog consentWrite) throws JSONException {

		JSONObject responseJson = ccmsUtil.callingEISServiceForCcms(consentWrite.getConsentWriteRequest());

		String responseStr = responseJson.toString();

		ConsentResponseConsentWrite writeResponse = new ConsentResponseConsentWrite();

		writeResponse = (ConsentResponseConsentWrite) JSONUtil.getObjctFromJSON(writeResponse, responseStr);

		consentWrite.setResponseCode(writeResponse.getEisResponse().getStatusCode());
		consentWrite.setResponseMsg(responseStr);
		consentWrite.setResponseStatus(writeResponse.getEisResponse().getSuccess());
		consentWrite.setResponseEntryTime(new Date());

		consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

		return consentWrite;
	}
	
	public ConsentPurposeEnquiryResponse getPurposeEnquiryFromCCMS(String locale) throws Exception {

		CCMSConfig config = commonService.getCcmsConfigById(1);

		if (locale == null || locale.trim().equals("")) {
			locale = "eng";
		}

		String correlationId = UuidV7Generator.generateV7().toString();

		ConsentPurposeEnquiryRequest request = new ConsentPurposeEnquiryRequest();
		ConsentPurposeEnquiryPayload eisPayload = new ConsentPurposeEnquiryPayload();
		ConsentRequestPayloadHeaders headers = new ConsentRequestPayloadHeaders();
		ConsentPurposeEnquiryBody body = new ConsentPurposeEnquiryBody();

		headers.setAcceptLanguage(locale);
		headers.setxCorelationId(correlationId);
		headers.setxApiVersion(config.getApiVersion());

		body.setTouchPointId(config.getTouchPointId());
		body.setPurposeSetId(config.getPurposeSetId());

		eisPayload.setHeaders(headers);
		eisPayload.setBody(body);

		request.setSourceId(config.getSourceId());
		request.setEisPayload(eisPayload);
		request.setDestination(config.getDestination());
		request.setTransactionType(config.getTransactionType());
		request.setTransactionSubType("PURPOSE_ENQUIRY");

		JSONObject requestJson = JSONUtil.beanObjectToJSONObjct(request);

		JSONObject responseJson = ccmsUtil.callingEISServiceForPurposeEnquiry(requestJson.toString());

		ConsentPurposeEnquiryResponse response = new ConsentPurposeEnquiryResponse();

		response = (ConsentPurposeEnquiryResponse) JSONUtil.getObjctFromJSON(response, responseJson.toString());

		return response;
	}
	
	private List<ConsentRequestConsent> prepareConsentsFromPurposeEnquiry(ConsentPurposeEnquiryResponse purposeResponse,
			String productCode) {

		List<ConsentRequestConsent> consents = new ArrayList<ConsentRequestConsent>();

		if (purposeResponse == null || purposeResponse.getEisResponse() == null
				|| purposeResponse.getEisResponse().getBody() == null) {
			return consents;
		}

		for (ConsentPurposeContainer container : purposeResponse.getEisResponse().getBody()) {

			if (container.getPurposes() == null) {
				continue;
			}

			for (ConsentPurposeMaster purpose : container.getPurposes()) {

				ConsentRequestConsent consent = new ConsentRequestConsent();

				consent.setPurposeCode(purpose.getCode());
				consent.setPurposeVersion(String.valueOf(purpose.getVersion()));
				consent.setConsented("true");

				if (Boolean.TRUE.equals(purpose.getIsProductSpecific())) {

					List<String> consentedProducts = new ArrayList<String>();
					//List<String> notConsentedProducts = new ArrayList<String>();

					consentedProducts.add(productCode);

					consent.setConsentedProducts(consentedProducts);
					// consent.setNotConsentedProducts(notConsentedProducts);
				}

				consents.add(consent);
			}
		}

		return consents;
	}
	
	private String getCurrentIsoTimestamp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		return sdf.format(new Date());
	}

}
