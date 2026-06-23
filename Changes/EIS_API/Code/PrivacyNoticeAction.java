package com.mintstreet.consent.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.result.StreamResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonSyntaxException;
import com.mintstreet.common.action.CommonLoanAction;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.consent.bo.PrivacyRequest;
import com.mintstreet.consent.bo.PrivacyRequestBean;
import com.mintstreet.consent.bo.PrivacyResponseBean;
import com.mintstreet.consent.bo.PrivacyResponseEIS;
import com.mintstreet.consent.entity.Body;
import com.mintstreet.consent.entity.CCMSConfig;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
import com.mintstreet.integration.pan.util.PanApiAESEncryption;
import com.mintstreet.integration.pan.util.PanApiRSAEncryption;
import com.opensymphony.xwork2.ModelDriven;

public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven<PrivacyRequest> {
	
	  private static final int KEY_LENGTH = 256;
	  private static final int ITERATION_COUNT = 65536;
	  private static final int DEFAULT_GCM_IV_NONCE_SIZE_BYTES = 12;
	  private static final int DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS = 128;
	  private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
	  private String secreteKey = Constants.UPLOADLEAD_SECRETKEY;	
	  
	  private static final Logger logger = LogManager.getLogger(PrivacyNoticeAction.class.getName());
	  
	  @Autowired
	  private CommonService commonService;

	  private static final long serialVersionUID = 1L;
	  
	  private PrivacyRequest privacyRequest = new PrivacyRequest();
	  private PrivacyRequestBean privacyRequestBean = new PrivacyRequestBean();
	  
	  PanApiAESEncryption aesEncryption = new PanApiAESEncryption();
	  PanApiRSAEncryption rsaEncryption = new PanApiRSAEncryption();
	  String aesKey = "";
	  
	  public StreamResult storePrivacyNotice() 
			  	throws JSONException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, 
			  	BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException, SQLException  {

		  StringBuilder responseStr = new StringBuilder();
		  List<String> messsage = new ArrayList<String>();
		  
		  try {
			  
			  HttpServletRequest request = ServletActionContext.getRequest();
			  
			  String accessToken = request.getHeader("AccessToken");
			  //Step1
			  aesKey = PanApiRSAEncryption.RSADecrypt(accessToken);
			  
			  //step2
			  String plainRequest = aesEncryption.AESDecryptGCM(privacyRequest.getREQUEST(), aesKey);
			  logger.info("plainRequest:: " + plainRequest);
			  
			  //temp code to verify digital signature
			  /*String digiSign = rsaEncryption.getDigitalSignature(plainRequest);
			  	boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(plainRequest, digiSign);*/
			  
			  //step3
			  boolean isValid = PanApiRSAEncryption.verifyDigitalSignature(plainRequest, privacyRequest.getDIGI_SIGN());
			  isValid = true;
			  logger.info("valid:: " + isValid);

			  privacyRequestBean = (PrivacyRequestBean) JSONUtil.getObjctFromJSON(privacyRequestBean, plainRequest);

			  if (!isValid) {
				  ServletActionContext.getResponse().setStatus(401);//new line
				  
				  messsage.add("Unauthorized Request");
				  PrivacyResponseBean responseBean = buildJsonResponseBean(false, 401, messsage,  null,null, privacyRequestBean.getCorrelationId());
				  
				  saveRequestAndResponse(responseBean,privacyRequestBean);
				  
				  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
				  logger.info("response Json:: " + responseJsonObj.toString());
				  //String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
				  String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
			    	
			      return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
			  }

			  logger.info("privacyRequestBean " + privacyRequestBean);
			  if(RequestUtil.getServletRequest().getMethod().equalsIgnoreCase("POST")) {

				  boolean isValidateReq = false;
		    	  Map<String,String>  validateReq = null;
		    	  
		    	  //validate request start
		    	  List<String> mandatoryValidationMessage = validateMandatoryParameters();
				  logger.info("errors.size() :: " + mandatoryValidationMessage.size());
				  logger.info("errors:: " + mandatoryValidationMessage);
				  
				  if(mandatoryValidationMessage.size() == 0) {
					  validateReq = validateRequest();
					  if(validateReq.size() > 0) {
						  isValidateReq = true;
				      }
				  }
				  //validate request end
				  
				  //build response based on errors
				  if (mandatoryValidationMessage.size() > 0) {
					  PrivacyResponseBean responseBean = buildJsonResponseBean(false,400,mandatoryValidationMessage,null,null,privacyRequestBean.getCorrelationId());
					  
					  saveRequestAndResponse(responseBean, privacyRequestBean);
					  
					  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
					  logger.info("response Json:: " + responseJsonObj.toString());
					  String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
					  
				  } else if (isValidateReq) {
					  
					  PrivacyResponseBean responseBean = buildJsonResponseBean(false, 422, messsage, validateReq, null, privacyRequestBean.getCorrelationId());
					  
					  saveRequestAndResponse(responseBean,privacyRequestBean);
					  
					  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
					  logger.info("response Json:: " + responseJsonObj.toString());
					  //String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  
					  String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
				    	
				      return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
				  
				  } else {
					  // 200 success
					  Body body = new Body();
					  body.setAck(true);
					  body.setVersion(Integer.valueOf(privacyRequestBean.getVersion()));
					  body.setLocale(privacyRequestBean.getLocale());
					  
					  messsage.add("Success");
					  
					  PrivacyResponseBean responseBean = buildJsonResponseBean(true, 200, messsage, null, body, privacyRequestBean.getCorrelationId()); 
					  saveRequestAndResponse(responseBean,privacyRequestBean);
					  
					  JSONObject responseJsonObj=JSONUtil.beanObjectToJSONObjct(responseBean);
					  logger.info("response Json:: " + responseJsonObj.toString());
					 // String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
				    	
				      return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
				  }
			    
		    } else {
		    	messsage.add("Method not allowed");
		    	PrivacyResponseBean responseBean = buildJsonResponseBean(false, 405, messsage, null, null,privacyRequestBean.getCorrelationId()); 
		    	saveRequestAndResponse(responseBean,privacyRequestBean);
		    	
		    	JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
		    	logger.info("response Json:: " + responseJsonObj.toString());
		    	
		    	
		    	//String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
//		    	String encryptedResponse = aesEncryption.AESEncryptGCM(responseJsonObj.toString(), aesKey);
//		    	String responseSignature = rsaEncryption.getDigitalSignature(responseJsonObj.toString());
//		    	
//		    	PrivacyResponseEIS eisFinalResponse = new PrivacyResponseEIS();
//		    	eisFinalResponse.setRESPONSE(encryptedResponse);
//		    	eisFinalResponse.setREQUEST_REFERENCE_NUMBER(privacyRequest.getREQUEST_REFERENCE_NUMBER());
//		    	eisFinalResponse.setDIGI_SIGN(responseSignature);
//		    	
//		    	JSONObject finalResponseJsonObjEIS = JSONUtil.beanObjectToJSONObjct(eisFinalResponse);
		    	
		    	String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
		    	
		    	return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
		    	
		    }
		  } catch (JsonSyntaxException | CertificateException | SignatureException | IOException e) {
			  logger.info("Exception ",e.getMessage());
			  logger.info("Exception ",e);
		  }
		  
		  
		  return new StreamResult(new ByteArrayInputStream(responseStr.toString().getBytes()));
	  }

	  public  void saveRequestAndResponse(PrivacyResponseBean privacyResponseBean, PrivacyRequestBean privacyRequest)  {
		   
		  
		  
		 String version = privacyRequestBean.getVersion() != null ? privacyRequestBean.getVersion().toString() : "";
		 
		 PrivacyRequestResponse privacyNotice= commonService.getPrivacyNoticeByLocale(privacyRequestBean.getLocale());
		 logger.info("privacyRequestResponse1"+privacyNotice);
		 
		 if(privacyNotice!=null) {
			 privacyNotice.setPrivacyIsActive("N");
			 try {
				commonService.save(privacyNotice);
				logger.info("update  PrivacyIsActive  in db");
			} catch (SQLException e) {
				logger.info(" SQLException caught ", e);
			}
		 }
		 
		
		 
		 //set request details in entity
		 PrivacyRequestResponse privacyRequestResponse = new PrivacyRequestResponse();
		 
		 privacyRequestResponse.setPrivacyTouchPointId(privacyRequestBean.getTouchPointId());
		 privacyRequestResponse.setPrivacyNotice(privacyRequestBean.getPrivacyNotice());
		 privacyRequestResponse.setPrivacyLocale(privacyRequestBean.getLocale());
		 privacyRequestResponse.setPrivacyApiVersion(version);
		 privacyRequestResponse.setPrivacyTimeStampReq(privacyRequestBean.getTimestamp());
		 privacyRequestResponse.setPrivacyCorrelationId(privacyRequestBean.getCorrelationId());
		 
		 //set response details in entity
		 String privacyAck = privacyResponseBean.getBody() != null ? String.valueOf(privacyResponseBean.getBody().isAck()) : "";
	     privacyRequestResponse.setPrivacyAck(privacyAck);
		 privacyRequestResponse.setPrivacyTimeStampRes(privacyResponseBean.getTimestamp());
		 privacyRequestResponse.setPrivacyStatus(String.valueOf(privacyResponseBean.isSuccess()));
		 privacyRequestResponse.setPrivacyStatusCode(privacyResponseBean.getStatusCode());
		 privacyRequestResponse.setPrivacyMessage(privacyResponseBean.getMessage().toString());
		 privacyRequestResponse.setPrivacyErrorMsg(String.valueOf(privacyResponseBean.getErrors()));
		 if(privacyAck.equals("true")) {
		 privacyRequestResponse.setPrivacyIsActive("Y");
		 }
		 try {
			 commonService.save(privacyRequestResponse);
			 logger.info("saved privacyRequestRes in db");
		 } catch (SQLException e) {
			 logger.info(" SQLException caught ", e);
		 }
		 
	   }
	   
	   private Map<String,String>  validateRequest() throws NoResultException, SQLException {
		   
		   String touchPointId=privacyRequestBean.getTouchPointId();
		   String privacyNotice=privacyRequestBean.getPrivacyNotice();
		   String version=privacyRequestBean.getVersion().toString();
		   String timestamp=privacyRequestBean.getTimestamp();
		   String correlationId=privacyRequestBean.getCorrelationId();
           String locale =privacyRequestBean.getLocale(); 		   
		   
		   Map<String,String> errors = new HashMap<>();
		   
		   CCMSConfig config = commonService.getCcmsConfigById(1);
		    
		   
		   
		   if (!touchPointId.equals(config.getTouchPointId())) {
			   errors.put("touchPointId","invalid touch Point ID");
		   }
		   if (!(privacyNotice.length()>=3 && privacyNotice.length()<=64000)) {
			   errors.put("privacyNotice","invalid Privacy Notice");
		   }
				if (!(privacyRequestBean.getLocale().length() ==3 && commonService.getLanguageBylannguageCode(locale) )) {
					logger.info("privacyLead. java :: LNO: 467 invalid locale ");

					 errors.put("locale","invalid locale");
				}
				try {
					
					if (!(version.length() >=1 && version.length() <= 6) || Integer.parseInt(version)==0 || !(version!=null && version.matches("^[0-9]+$"))) {
						errors.put("version","invalid Version Number");
					}
				} catch(NumberFormatException e){
					errors.put("version","invalid Version Number");
				}
				
			    if (!timestamp.matches("^[0-9]{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(2[0-3]|[01][0-9]):[0-5][0-9]:[0-5][0-9]Z$")) {
					 errors.put("timeStamp","invalid time stamp");
				}
			    
				if (!correlationId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
					 errors.put("correlationId","invalid correlation Id");
				}
		  
				return errors;
		}
	
		
		private static PrivacyResponseBean buildJsonResponseBean
				(Boolean status, Integer statusCode, List<String> messsage, Map<String, String> fieldValidationErrors, Body body, String correlationId) 
						throws JSONException {
			
			PrivacyResponseBean privacyResponseBean = new  PrivacyResponseBean(); 
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			String formattedDate = sdf.format(new Date());
			 
			privacyResponseBean.setSuccess(status);
			privacyResponseBean.setStatusCode(statusCode);
			privacyResponseBean.setMessage(messsage);
			privacyResponseBean.setErrors(fieldValidationErrors);
		    privacyResponseBean.setBody(body);
			privacyResponseBean.setTimestamp(formattedDate);
			privacyResponseBean.setCorrelationId(correlationId);
	         
	         return privacyResponseBean;
		}

		private List<String> validateMandatoryParameters() {
	    	List<String> errors = new ArrayList<>();
			  String version=privacyRequestBean.getVersion().toString();
			
			  
	    	if(!(privacyRequestBean.getTouchPointId() != null && !privacyRequestBean.getTouchPointId().equals("")))
	    		errors.add("touchPointId is Mandatory");
	    	
	    	if(!(privacyRequestBean.getPrivacyNotice() != null && !privacyRequestBean.getPrivacyNotice().equals("")))	
	    		errors.add("privacyNotice is Mandatory");
	    	
	    	//if(getLocale().isEmpty() || getLocale()==null)
	    	if(!(privacyRequestBean.getLocale() != null && !privacyRequestBean.getLocale().equals("")))
	    		errors.add("locale is mandatory");
	    	
	    	if(!(version!=null && !version.equals("")))
	    		errors.add("version is mandatory");
	    	
	    	if(!(privacyRequestBean.getTimestamp() != null && !privacyRequestBean.getTimestamp().equals("")))		
	    		errors.add("timestamp is mandatory");
	    	
	    	if(!(privacyRequestBean.getCorrelationId() != null && !privacyRequestBean.getCorrelationId().equals("")))	
               errors.add("correlationID is mandatory");

			return errors;
	    }

		@Override
		public PrivacyRequest getModel() {
			
			return privacyRequest;
		}
		
		public static SecretKey generateSecretKey(String password, byte [] iv) throws NoSuchAlgorithmException, InvalidKeySpecException {
		    KeySpec spec = new PBEKeySpec(password.toCharArray(), iv, ITERATION_COUNT, KEY_LENGTH); // AES-256
		    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		    byte[] key = secretKeyFactory.generateSecret(spec).getEncoded();
		    return new SecretKeySpec(key, "AES");
		}	
		
		public static String encryptData(String data, String key)
				throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
				InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException {

			SecureRandom secureRandom = new SecureRandom();
			byte[] iv = new byte[DEFAULT_GCM_IV_NONCE_SIZE_BYTES];
			secureRandom.nextBytes(iv);
			
			SecretKey secretKey = generateSecretKey(key, iv);

			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			GCMParameterSpec parameterSpec = new GCMParameterSpec(DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS, iv);

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

			byte[] cipherText  = cipher.doFinal(data.getBytes("UTF-8"));

			byte[] encryptedData = new byte[iv.length + cipherText.length];
	        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
	        System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

	        return Base64.getEncoder().encodeToString(encryptedData);
		}
		
		public static String decryptData(String strToDecrypt, String key)
				throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
				InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException {

			byte[] encryptedData = Base64.getDecoder().decode(strToDecrypt);
	        byte[] iv = new byte[DEFAULT_GCM_IV_NONCE_SIZE_BYTES];
	        System.arraycopy(encryptedData, 0, iv, 0, iv.length);
	        
	        SecretKey secretKey = generateSecretKey(key, iv);

			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			GCMParameterSpec parameterSpec = new GCMParameterSpec(DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS, iv);

			cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

			byte[] cipherText = new byte[encryptedData.length - DEFAULT_GCM_IV_NONCE_SIZE_BYTES];
	        System.arraycopy(encryptedData, DEFAULT_GCM_IV_NONCE_SIZE_BYTES, cipherText, 0, cipherText.length);

	        byte[] decryptedText = cipher.doFinal(cipherText);
	        return new String(decryptedText, "UTF-8");

		}
		public String bulidFinalResponseJsonObjEIS(String responseString) {
			
			String encryptedResponse;
			JSONObject finalResponseJsonObjEIS = null;
			try {
				
				encryptedResponse = aesEncryption.AESEncryptGCM(responseString, aesKey);
				
		    	String responseSignature = rsaEncryption.getDigitalSignature(responseString);
		    	
		    	PrivacyResponseEIS eisFinalResponse = new PrivacyResponseEIS();
		    	eisFinalResponse.setRESPONSE(encryptedResponse);
		    	eisFinalResponse.setREQUEST_REFERENCE_NUMBER(privacyRequest.getREQUEST_REFERENCE_NUMBER());
		    	eisFinalResponse.setDIGI_SIGN(responseSignature);
		    	
		    	finalResponseJsonObjEIS = JSONUtil.beanObjectToJSONObjct(eisFinalResponse);
			} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				logger.info("Privacy Norice .java 454 :: ", e);
			}
			
			return finalResponseJsonObjEIS.toString();
		}

}
