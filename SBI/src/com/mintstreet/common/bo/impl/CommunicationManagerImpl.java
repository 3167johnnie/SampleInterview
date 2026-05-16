package com.mintstreet.common.bo.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.Email;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.SendMail;

public class CommunicationManagerImpl{
	private static final Logger logger = LogManager.getLogger(CommunicationManagerImpl.class.getName());

	@Autowired
	private CommonService commonService;

	@Autowired
	private SendMail sendMail; 

	public String setEmailBody(Integer loanType, Integer requestIndex, Integer templateType, Integer havePan) {
		return commonService.getTemplateByLoanTypeRequestIndexTemplateTypeId(loanType, requestIndex, templateType, havePan);
	}

	public boolean sendSms(String smsText) {
		try {
			if (!Constants.SMS_SERVICE_BYPASS) {
				boolean returnValue = postSMS(smsText);
				return returnValue;
			} else {
				logger.info("SMS Send Bypassed");
				return true;
			}
		} catch (NullPointerException e) {
			logger.info("CommunicationManagerImpl.java LNo : 104 : Exception Caught" + e);
			logger.info("CommunicationManagerImpl.java LNo : 105 : Bypass status is " + Constants.SMS_SERVICE_BYPASS);
			return false;
		} catch (Exception e) {
			logger.info("CommunicationManagerImpl.java LNo : 104 : Exception Caught" + e);
			logger.info("CommunicationManagerImpl.java LNo : 105 : Bypass status is " + Constants.SMS_SERVICE_BYPASS);
			return false;
		}
	}


	/*public boolean sendSms(String msg, boolean infronicsStatus) throws Exception {
		boolean sendStatus = false;
		try {
			sendStatus = sendSms(msg);
		} catch (Exception e) {
			logger.error("CommunicationManagerImpl.java LNo : 121 : Exception Caught", e);
		}
		return sendStatus;
	}*/

	
	public boolean sendEmail(String recieverEmail[], String msgBody, String subject) {
//		logger.info("sendEmail() LNo : 209 :: Receiver Email " + recieverEmail);
		//logger.info("sendEmail() LNo : 210 :: " + msgBody);
		if (Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
			return true;
		}
		logger.info("sendEmail() LNo : 215 :: ");
		Email eml = new Email();
		eml.setAlias(Constants.ALIAS_EMAIL);
		eml.setContentType("text/html");
		eml.setSenderAddress(Constants.SENDER_EMAIL);
		eml.setSubject(subject);
		eml.setRecepientAddresses(recieverEmail);
		eml.setMessage(msgBody);
		sendMail.postMail(eml);
		return true;
	}


	public boolean sendEmailCsv(String recieverEmail[], String msgBody, String subject ,File[] attachment)  {
//		logger.info("sendEmail() LNo : 233 :: Receiver Email "+recieverEmail);
		//logger.info("sendEmail() LNo : 234 :: "+msgBody);
			if( Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
				return true;
			}
			logger.info("sendEmail() LNo : 234 :: ");
			Email eml=new Email();
			eml.setAlias(Constants.ALIAS_EMAIL);
			eml.setContentType("text/html");
			eml.setSenderAddress(Constants.SENDER_EMAIL); 
			eml.setSubject(subject);
			eml.setRecepientAddresses(recieverEmail);
			eml.setAttachements(attachment);
			eml.setMessage(msgBody); 
			sendMail.postMail(eml);
			return true;
	}

	private boolean postSMS(String incomingMessage) {
		
		BufferedReader in = null;
		InputStreamReader isr = null;
		try {
    
			logger.info("-----------Inside postSMS msgText : "+EncryptDecryptUtil.encrypt(incomingMessage));
			
			String message = Constants.SMS_APPLIANCE_BASED_MESSAGE;

			String intFlag = incomingMessage.substring((incomingMessage.indexOf("&intflag=")+ "&intflag=".length()));

			String urlApplianceBasedSMS = Constants.SMS_APPLIANCE_BASED_URL;  
			String userCredentials = null;

			if ("1".equals(intFlag)) {
				urlApplianceBasedSMS = urlApplianceBasedSMS+"/"+Constants.SMS_DOM_APPLIANCE_BASED_USERNAME;
				userCredentials = Constants.SMS_DOM_APPLIANCE_BASED_USERNAME+":"+EncryptDecryptUtil.decrypt(Constants.SMS_DOM_APPLIANCE_BASED_PASSWORD);
				message = message.replaceAll("INT_FLAG", "0");
			} else if ("2".equals(intFlag)) {
				urlApplianceBasedSMS = urlApplianceBasedSMS+"/"+EncryptDecryptUtil.decrypt(Constants.SMS_INTER_APPLIANCE_BASED_USERNAME);
				userCredentials = EncryptDecryptUtil.decrypt(Constants.SMS_INTER_APPLIANCE_BASED_USERNAME)+":"+EncryptDecryptUtil.decrypt(Constants.SMS_INTER_APPLIANCE_BASED_PASSWORD);
				message = message.replaceAll("INT_FLAG", "1");
			}

			//set actual message text
			String messageText = incomingMessage.substring((incomingMessage.indexOf("&msgtxt=")+ "&msgtxt=".length()), incomingMessage.indexOf("&msgtype="));
			message = message.replaceAll("MESSAGE_TEXT", messageText);

			//set mobile number
			String mobileNumber = incomingMessage.substring((incomingMessage.indexOf("&pno=")+ "&pno=".length()), incomingMessage.indexOf("&msgtxt="));
			message = message.replaceAll("MOBILE_CODE", mobileNumber);

			logger.info("CommunicationManagerImpl urlApplianceBasedSMS="+urlApplianceBasedSMS);  
//			logger.info("CommunicationManagerImpl Outgoing ###message="+message);		    	

			URL url = new URL(urlApplianceBasedSMS);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();

			String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

			http.setRequestProperty ("Authorization", basicAuth);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			http.setRequestProperty("Content-Language", "en-US");
			http.setUseCaches(false);
			http.setDoInput(true);
			http.setDoOutput(true);


			DataOutputStream wr = new DataOutputStream(http.getOutputStream());

			wr.writeBytes(message);
			wr.flush();
			wr.close();
			http.disconnect();

			isr = new InputStreamReader(http.getInputStream());
			in = new BufferedReader(isr);
			
			String inputLine;
			if ((inputLine = in.readLine()) != null) {
				//logger.info("Return value after sending SMS, inputLine="+inputLine+" For Mobile No : "+mobileNumber);
				logger.info("Return value after sending SMS, inputLine="+inputLine+" For Mobile No : "+EncryptDecryptUtil.encrypt(mobileNumber));
				in.close();
				isr.close();
				return true;    
			} else {
				//logger.info("in else block of input lines For Mobile No : "+mobileNumber);      
				logger.info("in else block of input lines For Mobile No : "+EncryptDecryptUtil.encrypt(mobileNumber));      
				in.close();
				isr.close();
				return false;
			}

		} catch(IOException e) {
			logger.error("Error in sending SMS", e);
			return false;
		} catch(Exception e) {
			logger.error("Error in sending SMS", e);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
					isr.close();
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(isr);
				} catch (IOException e) {
					logger.info("CommunicationManagerImpl.java LNo 187 :: postSMS() ", e);
				} finally {
					in = null;
					isr = null;
				}
			}
		}
	}


}
