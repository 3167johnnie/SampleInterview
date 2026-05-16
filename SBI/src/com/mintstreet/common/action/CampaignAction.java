package com.mintstreet.common.action;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.SbiCampaignMaster;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.GeneratePDF;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.validation.ValidatorUtil;


public class CampaignAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(CampaignAction.class.getName());
	private static final long serialVersionUID = 1L;

	@Autowired
	protected CommonService commonService; 
	
	@Autowired
	CommunicationManagerImpl communicationManagerImpl;
	@Autowired
	private GeneratePDF generatePDF;
	
	private SbiCampaignMaster sbiCamp = null;
	
	private String fileName;
	
	public StreamResult setCampaignDetails() throws JSONException{
		String responseMessage="";
		JSONObject json = new JSONObject();

		HttpServletRequest request = RequestUtil.getServletRequest();
		String sbiHlCampName = request.getParameter("sbiHlCampName");
		if(!ValidatorUtil.isValid(sbiHlCampName)){
			responseMessage = "error|Enter valid name.";
		}
		String sbiHlCampSalaryAcntNumber= request.getParameter("sbiHlCampSalaryAcntNumber");
		if(!ValidatorUtil.isValid(sbiHlCampSalaryAcntNumber) ||sbiHlCampSalaryAcntNumber.length()<11){
			responseMessage = "error|Enter valid salary account number.";
		}
		String sbiHlCampMobileNo=request.getParameter("sbiHlCampMobileNo");
		if(!ValidatorUtil.isValidMobile(sbiHlCampMobileNo)){
			responseMessage = "error|Enter valid mobile number.";
		}
		String sbiHlCampProjectName=request.getParameter("sbiHlCampProjectName");
		if(!ValidatorUtil.isValid(sbiHlCampProjectName)){
			responseMessage = "error|Enter valid project.";
		}
		String sbiHlCampEmail=request.getParameter("sbiHlCampEmail");
		if(!ValidatorUtil.isValidEmail(sbiHlCampEmail)){
			responseMessage = "error|Enter valid Emailid.";
		}
		String employement = request.getParameter("govtEmp");
		if(!ValidatorUtil.isValid(employement)){
			responseMessage = "error|Enter valid employementId.";
		}
		
		SbiCampaignMaster sbiCamp = new SbiCampaignMaster();
		try{
			sbiCamp.setSbiHlCampSalaryAcntNumber(Long.parseLong(sbiHlCampSalaryAcntNumber));
			sbiCamp.setSbiHlCampName(sbiHlCampName);
			sbiCamp.setSbiHlCampMobileNo(sbiHlCampMobileNo);
			if(request.getParameter("sbiHlCampEmail")!=null){
				sbiCamp.setSbiHlCampEmail(sbiHlCampEmail);
			}
			sbiCamp.setSbiHlCampProjectName(sbiHlCampProjectName);
			sbiCamp.setSbiHlCampMobileVerified("N");
			sbiCamp.setSbiHlCampCreatedOn(new Date());
			sbiCamp.setIsGovtEmployement(new Boolean(employement));
			SessionUtil.setMobile(sbiHlCampMobileNo);

			json=sendOtp(sbiCamp);
			
		} catch(JSONException e){
			logger.info("Exception occure while saving sbiCamp", e);
			json.put("status", "error");
		} catch(Exception e){
			logger.info("Exception occure while saving sbiCamp", e);
			json.put("status", "error");
		}
		if(responseMessage!=""){
			return new StreamResult(new ByteArrayInputStream(responseMessage.getBytes()));
		}else{
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}
	}
	
	public StreamResult confirmOtp() throws JSONException {
		JSONObject json = new JSONObject();
		
		HttpServletRequest request = RequestUtil.getServletRequest();
		String mobileOtp = null;
		if(request.getParameter("inputOtpWantUs")!=null){
			mobileOtp = request.getParameter("inputOtpWantUs");
		}
		
		String mobileNumber = SessionUtil.getMobile();
		SbiCampaignMaster sbiCampignMaster =commonService.getOtpByMobileNumber(mobileNumber);
		if(sbiCampignMaster!=null){
			if(sbiCampignMaster.getSbiHlCampMobileOtp()!=null && mobileOtp.equals(sbiCampignMaster.getSbiHlCampMobileOtp()+"")){
				String generatedSequence=generateSequence(sbiCampignMaster.getWebCampaignId().longValue());
				sbiCampignMaster.setSbiHlCampMobileVerified("Y");
				sbiCampignMaster.setWebCampaignSeqId(generatedSequence);
				sbiCampignMaster=commonService.save(sbiCampignMaster);
				
				json.put("status", "success");
				json.put("message", "OTP authentication successful");
				if(sbiCampignMaster.getWebCampaignId()!=null){
					json.put("sbta", generatedSequence);
				}
				sbiCampignMaster=commonService.save(sbiCampignMaster);
				

				JSONObject json1 = generatePDF.generatePdfForSbiTata(generatedSequence);
				
				if(json1.getString("fileName")!=null){
					SessionUtil.setSbiTataPdfName(json1.getString("fileName"));
					String pdfName = json1.getString("fileName");
					logger.info("Pdf file name "+pdfName);
					sbiCampignMaster.setSbiHlCampPdfFileName(pdfName);
					sbiCampignMaster=commonService.save(sbiCampignMaster);
					
					if(sbiCampignMaster==null){
						json.put("status", "error");
						json.put("message", "Currently PDF Service is unavailable");
					}
				}else{
					json.put("status", "error");
					json.put("message", "Currently PDF Service is unavailable");
				}
			
				logger.info("json response is::"+json);
			
			}
			else{
				json.put("status", "error");
				json.put("message", "OTP is incorrect! Try again.");
				
			}
		}else{
			json.put("status", "error");
		}
		
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));

	}
	
	private JSONObject sendOtp(SbiCampaignMaster sbiCampignMaster) throws JSONException {
		JSONObject json = new JSONObject();
		if(ValidatorUtil.isValidMobile(sbiCampignMaster.getSbiHlCampMobileNo())){
			sbiCampignMaster.setSbiHlCampMobileOtp(SbiUtil.getVerificationCode(sbiCampignMaster.getSbiHlCampMobileNo()));
		}else{
			json.put("status", "error");
		}

		String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
		msgBody = SbiUtil.urlEncode(msgBody);
		
		String SMS_TEXT = null;
		SMS_TEXT=Constants.SMS_STRING_INDIAN;
		SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
		SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", sbiCampignMaster.getSbiHlCampMobileOtp().toString());
		SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+sbiCampignMaster.getSbiHlCampMobileNo()).trim());
		
		if (!Constants.DEPLOYMENT_MODE.equals("live")) {
			logger.info("OTP for Mobile Number: " + (Constants.COUNTRY_CODE_INDIA+sbiCampignMaster.getSbiHlCampMobileNo()).trim() + " is " + sbiCampignMaster.getSbiHlCampMobileOtp().toString());
		}
		
		if(!communicationManagerImpl.sendSms(SMS_TEXT)){
			json.put("status", "error");
			json.put("message", "OTP service is down");
			
		}
		sbiCampignMaster.setSbiHlCampOtpCount(1);
		sbiCampignMaster=commonService.save(sbiCampignMaster);
		if(sbiCampignMaster!=null){
			json.put("status", "success");
		}
		return json;
	}

	
	public StreamResult resendOtp() throws JSONException {
		logger.info("Resend Otp");
		JSONObject json = new JSONObject();
		HttpServletRequest request = RequestUtil.getServletRequest();
		SbiCampaignMaster sbiCampignMaster = null;
		if(request.getParameter("mobile")==null){
			sbiCampignMaster =commonService.getOtpByMobileNumber(SessionUtil.getMobile());
		}else{
			sbiCampignMaster =commonService.getOtpByMobileNumber(SessionUtil.getMobile());
			sbiCampignMaster.setSbiHlCampMobileNo(request.getParameter("mobile"));
			SessionUtil.setMobile(request.getParameter("mobile"));
		}
		if(sbiCampignMaster == null){
			json.put("status", "error");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}


		Integer alertCount = sbiCampignMaster.getSbiHlCampOtpCount();
		try {
			if(alertCount!=null && alertCount > 5){
				json.put("status", "error");
				json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
				json.put("alertCount", alertCount);
				return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
			}else{
				sbiCampignMaster.setSbiHlCampMobileOtp(SbiUtil.getVerificationCode(sbiCampignMaster.getSbiHlCampMobileNo()));
			}
			
			String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
			msgBody = SbiUtil.urlEncode(msgBody);
			
			String SMS_TEXT = null;
			SMS_TEXT=Constants.SMS_STRING_INDIAN;
			
			SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", sbiCampignMaster.getSbiHlCampMobileOtp().toString());
			SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+sbiCampignMaster.getSbiHlCampMobileNo()).trim());
			
			if (!Constants.DEPLOYMENT_MODE.equals("live")) {
				logger.info("OTP for Mobile Number: " + (Constants.COUNTRY_CODE_INDIA+sbiCampignMaster.getSbiHlCampMobileNo()).trim() + " is " + sbiCampignMaster.getSbiHlCampMobileOtp().toString());
			}
			
			if(!communicationManagerImpl.sendSms(SMS_TEXT)){
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience.");
				json.put("alertCount", alertCount);
				return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
			}
			alertCount++;
			sbiCampignMaster.setSbiHlCampOtpCount(alertCount);
			sbiCampignMaster = commonService.save(sbiCampignMaster);
			if(sbiCampignMaster==null){
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
			}
			json.put("status", "success");
			json.put("message", "OTP code sent");
		} catch(NoResultException e){
			logger.info("CampaignAction.java LNO 232::", e);
			json.put("status", "error");
			json.put("message", "Sorry for the inconvenience");
			
		} catch(Exception e){
			logger.info("CampaignAction.java LNO 232::", e);
			json.put("status", "error");
			json.put("message", "Sorry for the inconvenience");
			
		}
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public String getDownloadPdf() {
		try {
			String filePath = Constants.PDF_DOWNLOAD_BASE_PATH+Constants.SBI_TATA_PDF_GENRATION_LOCATION + SessionUtil.getSbiTataPdfName();

			File fileToDownload = new File(filePath);
			fileName = fileToDownload.getName();
			inputStream = new FileInputStream(fileToDownload);
		} catch (FileNotFoundException e) {
			logger.info("CampaignAction.java LNo 283 :: getDownloadPdf() ", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					IOUtils.closeQuietly(inputStream);
				} catch (IOException e) {
					logger.info("CampaignAction.java LNo 674 :: closeInputStream() ", e);
				} finally {
					inputStream = null;
				}
			}
		}
		return "downLoadTataPDF";
	}
	
	
	private String generateSequence(long number){
		
		String zeros="";
		while(number<=99999){
			if (number<=9){
				zeros="00000";
				break;
			}else if (number>9 && number<=99){
				zeros="0000";
				break;
			}else if (number>99 && number<=999){
				zeros="000";
				break;
			}else if (number>999 && number<=9999){
				zeros="00";
				break;
			}else if (number>9999 && number<=99999){
				zeros="0";
				break;
			}else if (number>99999 && number<=999999){
				zeros="";
				break;
			}
			
		}
		
		return Constants.SBI_TATA_CONSTANT+zeros+number;
	}
	
	
		
	public String setCampaignPage(){
		isDsrPage="false";
		
		return SUCCESS;
	}
	
	public SbiCampaignMaster getSbiCamp() {
		return sbiCamp;
	}
	
	
	public void setSbiCamp(SbiCampaignMaster sbiCamp) {
		this.sbiCamp = sbiCamp;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
