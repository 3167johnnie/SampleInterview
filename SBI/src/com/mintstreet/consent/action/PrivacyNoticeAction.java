package com.mintstreet.consent.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.action.CommonLoanAction;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.consent.bo.PrivacyRequest;
import com.mintstreet.consent.bo.PrivacyResponseBean;
import com.mintstreet.consent.entity.Body;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
import com.opensymphony.xwork2.ModelDriven;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


//120526 changes for locale null issue
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
	  
	  
	  
	  private PrivacyRequest privacyRequest=new PrivacyRequest();
	 
	  
	  
	  public StreamResult storePrivacyNotice() throws JSONException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException, SQLException  {
		  StringBuilder str = new StringBuilder();
		  
		  String privacyReq=decryptData(privacyRequest.toString(), EncryptDecryptUtil.decrypt(secreteKey));
		  PrivacyRequest privacyRequest1 = (PrivacyRequest) JSONUtil.getObjctFromJSON(privacyReq, privacyReq);
		  String locale=privacyRequest1.getLocale();
		  String version=privacyRequest1.getVersion().toString();
		  String correlationId=privacyRequest1.getCorrelationId();
		  
		 //String tochPointId= decryptData(privacyRequest.getTouchPointId(), EncryptDecryptUtil.decrypt(secreteKey));
		 //String privacyNotice= decryptData(privacyRequest.getPrivacyNotice(), EncryptDecryptUtil.decrypt(secreteKey));
		 //String locale= decryptData(privacyRequest.getLocale(), EncryptDecryptUtil.decrypt(secreteKey));
		 //String version= decryptData(privacyRequest.getVersion().toString(), EncryptDecryptUtil.decrypt(secreteKey));
		 //String timestamp= decryptData(privacyRequest.getTimestamp(), EncryptDecryptUtil.decrypt(secreteKey));
		 //String correlationId= decryptData(privacyRequest.getCorrelationId(), EncryptDecryptUtil.decrypt(secreteKey));
		 
		 
		
		  
		  logger.info("locale "+locale);
	    
		 // str = str.append("{");
		  List<String> messsage=new ArrayList<String>();
		  if(RequestUtil.getServletRequest().getMethod().equalsIgnoreCase("POST")) {
			  try {
                  
				  logger.info("privacyRequest "+privacyRequest);
				  logger.info("privacyRequest1 "+privacyRequest1);
				  
				 
				  
				  //TODO need to decrypt request
		    	  boolean isValidateReq=false;
		    	  Map<String,String>  validateReq = null;
				  //need to do preValidation
				  //List<String> errors = validateMandatoryParameters();
				  List<String> errors = validateMandatoryParameters(privacyRequest1);
				  logger.info("errors.size()111:: " + errors.size());
				  logger.info("errors:: " + errors);
				  if(errors.size() == 0) {
					  //validateReq = validateRequest();
					  validateReq = validateRequest(privacyRequest1);
					  if(validateReq.size() > 0) {
						  isValidateReq = true;
				      }
				  }
				  
				  logger.info("errors.size()222:: " + errors.size());
				  if (errors.size() > 0) {
					  logger.info("errors:: " + errors);
					  PrivacyResponseBean obj=buildJsonResponseBean(false,400,errors,null,null,privacyRequest1.getCorrelationId()); 
					  //saveRequestAndResponse(obj);
					  saveRequestAndResponse(obj,privacyRequest1);
					  JSONObject jsonObj=JSONUtil.beanObjectToJSONObjct(obj);
					  str.append(jsonObj.toString());
					  //String res = encryptData(str.toString(), EncryptDecryptUtil.decrypt("xyzneed to encrypt request "));
					  return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
				  } else if (isValidateReq) {
					  
					  logger.info("isValidateReq " + isValidateReq);
					  // 422 validation
					  PrivacyResponseBean obj = buildJsonResponseBean(false, 422, messsage, validateReq, null,correlationId); // Done need to send error code in key value pair  //Done
					  //saveRequestAndResponse(obj);
					  saveRequestAndResponse(obj,privacyRequest1);
					  JSONObject jsonObj=JSONUtil.beanObjectToJSONObjct(obj);
					  
					  str.append(jsonObj.toString());
					  //String res = encryptData(str.toString(), EncryptDecryptUtil.decrypt("xyzneed to encrypt request "));
					  return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
				  } else { // TODO 200 status code to be sent at end
							// 200 success
						Body body1=new Body();
						body1.setAck(true);
						
						//body1.setLocale(getLocale());
						body1.setVersion(Integer.valueOf(version));
						messsage.add("Success");
						PrivacyResponseBean obj = buildJsonResponseBean(true, 200, messsage, null, body1,correlationId); 
						//saveRequestAndResponse(obj);
						saveRequestAndResponse(obj,privacyRequest1);
						JSONObject jsonObj=JSONUtil.beanObjectToJSONObjct(obj);
						str.append(jsonObj.toString());
						
						//String res = encryptData(str.toString(), EncryptDecryptUtil.decrypt("xyzneed to encrypt request "));
						return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
					}
		    	    
			    } catch (JSONException e) {
			        logger.info("UploadLead. java :: LNO: 417 : Exception Caught " + e);
			        messsage.add("Internal Server Error");
			        PrivacyResponseBean obj=buildJsonResponseBean(false, 500, messsage, null, null,correlationId); ////TODO change 3rd parameter of "Internal Server Error" //Done
			        //saveRequestAndResponse(obj);
			        saveRequestAndResponse(obj,privacyRequest1);
			        JSONObject jsonObj=JSONUtil.beanObjectToJSONObjct(obj);
					str.append(jsonObj.toString());
			        //String res = encryptData(str.toString(), EncryptDecryptUtil.decrypt("xyzneed to encrypt request "));
					//logger.info("UploadLead. java :: LNO: 101 encrypted responses is : " + res);
					return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
			     } catch (Exception e) {
			    	 logger.info("UploadLead. java :: LNO: 420 : Exception Caught " + e.getStackTrace());
			    	 logger.info("UploadLead. java :: LNO: 420 : Exception Caught " + e.getMessage());
			     }
		    } else {
	    	messsage.add("Method not allowed");
	    	PrivacyResponseBean obj = buildJsonResponseBean(false, 405, messsage, null, null,correlationId); 
	    	//saveRequestAndResponse(obj);
	    	saveRequestAndResponse(obj,privacyRequest1);
	    	JSONObject jsonObj=JSONUtil.beanObjectToJSONObjct(obj);
			str.append(jsonObj.toString());
	        //String res = encryptData(str.toString(), EncryptDecryptUtil.decrypt("xyzneed to encrypt request "));
			return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
	    
	    }
	    // str.append("}");
	    return new StreamResult(new ByteArrayInputStream(str.toString().getBytes()));
	  }



	   public  void saveRequestAndResponse(PrivacyResponseBean privacyResponseBean, PrivacyRequest privacyRequest)  {
		   
		 PrivacyRequestResponse privacyRequestResponse=new PrivacyRequestResponse();
		 String touchPointId=privacyRequest.getTouchPointId();
		  String privacyNotice=privacyRequest.getPrivacyNotice();
		  String locale=privacyRequest.getLocale();
		  String version=privacyRequest.getVersion().toString();
		  String timestamp=privacyRequest.getTimestamp();
		  String correlationId=privacyRequest.getCorrelationId();
		 //request save into db
		 String touchPointId2 = touchPointId != null ? touchPointId : "";
		 String privacyNotice2 = privacyNotice != null ? privacyNotice : "";
		 //String privacyLocale2 = privacyLocale != null ? privacyLocale : "";
		 String timestamp2 = timestamp != null ? timestamp : "";
		 //String correlationId2 = correlationId != null ? correlationId : "";
		 
		 privacyRequestResponse.setPrivacyTouchPointId(touchPointId2);
		 privacyRequestResponse.setPrivacyNotice(privacyNotice2);
		 //privacyRequestResponse.setPrivacyLocale(privacyLocale2);
		 privacyRequestResponse.setPrivacyLocale(locale);
		 privacyRequestResponse.setPrivacyApiVersion(version);
		 privacyRequestResponse.setPrivacyTimeStampReq(timestamp2);
		 privacyRequestResponse.setPrivacyCorrelationId(correlationId);
		 
		 //response save into db
		String privacyAck = privacyResponseBean.getBody() != null ? String.valueOf(privacyResponseBean.getBody().isAck()) : "";
		//String timestamp1 = privacyResponseBean.getTimestamp() != null ? privacyResponseBean.getTimestamp() : "";
		String sucesss = String.valueOf(privacyResponseBean.isSuccess()) != null ? String.valueOf(privacyResponseBean.isSuccess()) : "";
		Integer statusCode = privacyResponseBean.getStatusCode() != null ? privacyResponseBean.getStatusCode() : 0;
		
		logger.info("privacyResponseBean.getMessage()::" + privacyResponseBean.getMessage());
		String message = privacyResponseBean.getMessage().toString() != null ? privacyResponseBean.getMessage().toString() : "";
		//String error = privacyResponseBean.getErrors().toString() != null ? privacyResponseBean.getErrors().toString() : "";
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	     sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
	     String formattedDate = sdf.format(new Date());
		 privacyRequestResponse.setPrivacyAck(privacyAck);
		 privacyRequestResponse.setPrivacyTimeStampRes(formattedDate);
		 privacyRequestResponse.setPrivacyStatus(String.valueOf(sucesss));
		 privacyRequestResponse.setPrivacyStatusCode(statusCode);
		 logger.info("message::" + message);
		 privacyRequestResponse.setPrivacyMessage(message); 
		 logger.info("privacyResponseBean.getErrors() using vlue of  "+String.valueOf(privacyResponseBean.getErrors()));
		 String errror=String.valueOf(privacyResponseBean.getErrors());
		 logger.info("errors "+ errror);
		 privacyRequestResponse.setPrivacyErrorMsg(errror);
		 
		 
		try {
			
			logger.info("privacyRequestResponse "+privacyRequestResponse);
			commonService.save(privacyRequestResponse);
			logger.info("saved privacyRequestRes in db");
		} catch (SQLException e) {
			logger.info(" SQLException caught ", e);
		}

	   }
	   
	

	   private Map<String,String>  validateRequest(PrivacyRequest privacyRequest) {
		   String touchPointId=privacyRequest.getTouchPointId();
			  String privacyNotice=privacyRequest.getPrivacyNotice();
			  String version=privacyRequest.getVersion().toString();
			  String timestamp=privacyRequest.getTimestamp();
			  String correlationId=privacyRequest.getCorrelationId();
			Map<String,String> errors = new HashMap<>();
				if (!(touchPointId.length()>=1 && touchPointId.length()<=10)) {
					 errors.put("touchPointId","invalid touch Point ID");
				}
				if (!(privacyNotice.length()>=3 && privacyNotice.length()<=64000)) {
					 errors.put("privacyNotice","invalid Privacy Notice");
				}
				if (!(privacyRequest.getLocale().length() ==3)) {
					logger.info("privacyLead. java :: LNO: 467 invalid locale ");

					 errors.put("locale","invalid locale");
				}
				try {
					
					if (!(version.length() >=1 && version.length() <= 6) || Integer.parseInt(version)==0) {
						errors.put("version","invalid Version Number");
					}
				} catch(NumberFormatException e){
					errors.put("version","invalid Version Number");
				}
				
			    if (!timestamp.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$")) {
					 errors.put("timeStamp","invalid time stamp");
				}
			    
				if (!correlationId.matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
					 errors.put("correlationId","invalid correlation Id");
				}
		  
				return errors;
			
		}
		
		private static PrivacyResponseBean buildJsonResponseBean(Boolean status, Integer statusCode, List<String> messsage, Map<String, String> validateReq, Body body,String correlationId) throws JSONException {
			PrivacyResponseBean privacyResponseBean=new  PrivacyResponseBean(); 
			
			privacyResponseBean.setSuccess(status);
			privacyResponseBean.setStatusCode(statusCode);
			privacyResponseBean.setMessage(messsage);
			privacyResponseBean.setErrors(validateReq);
		    privacyResponseBean.setBody(body);
			privacyResponseBean.setTimestamp("2026-05-06T13:47:19Z");
			privacyResponseBean.setCorrelationId(correlationId);
	         
	         return privacyResponseBean;
		}

		private List<String> validateMandatoryParameters(PrivacyRequest privacyRequest) {
	    	List<String> errors = new ArrayList<>();
			  String version=privacyRequest.getVersion().toString();
			
			  
	    	if(!(privacyRequest.getTouchPointId() != null && !privacyRequest.getTouchPointId().equals("")))
	    		errors.add("touchPointId is Mandatory");
	    	
	    	if(!(privacyRequest.getPrivacyNotice() != null && !privacyRequest.getPrivacyNotice().equals("")))	
	    		errors.add("privacyNotice is Mandatory");
	    	
	    	//if(getLocale().isEmpty() || getLocale()==null)
	    	if(!(privacyRequest.getLocale() != null && !privacyRequest.getLocale().equals("")))
	    		errors.add("locale is mandatory");
	    	
	    	if(!(version!=null && !version.equals("")))
	    		errors.add("version is mandatory");
	    	
	    	if(!(privacyRequest.getTimestamp() != null && !privacyRequest.getTimestamp().equals("")))		
	    		errors.add("timestamp is mandatory");
	    	
	    	if(!(privacyRequest.getCorrelationId() != null && !privacyRequest.getCorrelationId().equals("")))	
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
		

}
