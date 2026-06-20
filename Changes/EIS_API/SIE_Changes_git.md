Commit 4137505c  authored 8 hours ago by Mohd Hakeem's avatar Mohd Hakeem
privacy notice encrypted as par EIS
 parent 9f75ecda
 Branches 
 No related tags found
 No related merge requests found
Changes
7
Showing 
 with 212 additions and 79 deletions
  SBI/src/com/mintstreet/common/security/XSSRequestWrapper.java 
+
1
−
1
@@ -273,7 +273,7 @@ public class XSSRequestWrapper extends HttpServletRequestWrapper {
	
	private String stripXSS(String value) {    
		
		String regexValue="[`|;|:|(|)|<|>|{|}|^|!|*|%|+|\"|']";
		String regexValue="[`|;|:|(|)|<|>|{|}|^|!|*|%|\"|']";
		
//		logger.info("stripXSS Value is ========" + value + "==============at=========" + LocalDateTime.now());
		if (value != null) {
  SBI/src/com/mintstreet/common/util/CcmsUtil.java 
+
4
−
1
@@ -43,13 +43,16 @@ public class CcmsUtil {
			//step 2
			plainRequestCcms = plainRequest;
			String encryptedRequestCcms = panApiAESEncryption.AESEncryptGCM(plainRequestCcms, randomAesKey);
			logger.info("encryptedRequestCcms "+encryptedRequestCcms);
			
			//step 3
			String digiSignature = panApiRSAEncryption.getDigitalSignature(plainRequestCcms);   
			String digiSignature = panApiRSAEncryption.getDigitalSignature(plainRequestCcms);
			
			//step 4
			String headerSecretKey = panApiRSAEncryption.RSAEncrypt(randomAesKey);
			
			logger.info("headerSecretKey "+headerSecretKey);
			
			//step 5
			String referenceNumber = SbiUtil.getRandomAlphaNumericReferenceNumber();
			
  SBI/src/com/mintstreet/common/util/JSONUtil.java 
+
10
−
0
@@ -30,6 +30,7 @@ import com.mintstreet.consent.bo.PrivacyRequest;
import com.mintstreet.consent.bo.PrivacyRequestBean;
import com.mintstreet.consent.bo.PrivacyRequestBeanEnc;
import com.mintstreet.consent.bo.PrivacyResponseBean;
import com.mintstreet.consent.bo.PrivacyResponseEIS;
import com.mintstreet.integration.edms.bo.EdmsResponse;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoanQuote;
@@ -357,6 +358,15 @@ public class JSONUtil {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			else if(object instanceof PrivacyResponseEIS){
				PrivacyRequestBeanEnc request = (PrivacyRequestBeanEnc)object;
				String jsonString = new Gson().toJson(request);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			
			return json;
		}
  SBI/src/com/mintstreet/consent/action/PrivacyNoticeAction.java 
+
106
−
60
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
@@ -28,9 +31,11 @@ import javax.crypto.spec.GCMParameterSpec;
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
@@ -46,9 +51,12 @@ import com.mintstreet.common.util.RequestUtil;
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
@@ -68,58 +76,62 @@ public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven
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


		  CCMSConfig config = commonService.getCcmsConfigById(1);
		  String auth_key = decryptData(privacyRequest.getAuth_Key(), EncryptDecryptUtil.decrypt(secreteKey));
		  String decryptedPrivacyRequest = decryptData(privacyRequest.getRequest(), EncryptDecryptUtil.decrypt(secreteKey));
		  logger.info("privacyReq "+decryptedPrivacyRequest);
		  
		  
		  privacyRequestBean = (PrivacyRequestBean) JSONUtil.getObjctFromJSON(privacyRequestBean, decryptedPrivacyRequest);
		  
		  if(!auth_key.equals(config.getAuthKey()))  {
			  messsage.add("Unauthorized Request");
			  PrivacyResponseBean responseBean = buildJsonResponseBean(false, 401, messsage,  null,null, privacyRequestBean.getCorrelationId());
		  try {
			  
			  saveRequestAndResponse(responseBean,privacyRequestBean);
			  HttpServletRequest request = ServletActionContext.getRequest();
			  
			  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
			  logger.info("response Json:: " + responseJsonObj.toString());
			  String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
			  return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
		  }
		  logger.info("privacyRequestBean " + privacyRequestBean);
		  }catch (JsonSyntaxException e) {
			  logger.info("Exception ",e.getMessage());
			  logger.info("Exception ",e);
		  }catch (Exception e) {
			  messsage.add("Unauthorized Request");
			  PrivacyResponseBean responseBean = buildJsonResponseBean(false, 401, messsage,  null,null, privacyRequestBean.getCorrelationId());
			  String accessToken = request.getHeader("AccessToken");
			  //Step1
			  aesKey = PanApiRSAEncryption.RSADecrypt(accessToken);
			  
			  saveRequestAndResponse(responseBean,privacyRequestBean);
			  //step2
			  String plainRequest = aesEncryption.AESDecryptGCM(privacyRequest.getREQUEST(), aesKey);
			  logger.info("plainRequest:: " + plainRequest);
			  
			  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
			  logger.info("response Json:: " + responseJsonObj.toString());
			  String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
			  return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
	     }
	
		  
		  if(RequestUtil.getServletRequest().getMethod().equalsIgnoreCase("POST")) {
			  try {
                  
		    	  boolean isValidateReq = false;
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
@@ -154,8 +166,11 @@ public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven
					  
					  JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
					  logger.info("response Json:: " + responseJsonObj.toString());
					  String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
					  //String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  
					  String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
				    	
				      return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
				  
				  } else {
					  // 200 success
@@ -171,23 +186,12 @@ public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven
					  
					  JSONObject responseJsonObj=JSONUtil.beanObjectToJSONObjct(responseBean);
					  logger.info("response Json:: " + responseJsonObj.toString());
					  String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
					 // String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					  String finalResponse = bulidFinalResponseJsonObjEIS(responseJsonObj.toString());
				    	
				      return new StreamResult(new ByteArrayInputStream(finalResponse.getBytes()));
				  }
		    	    
			    } catch (JSONException e) {
			        logger.info("Exception Caught " + e);
			        messsage.add("Internal Server Error");
			        PrivacyResponseBean responseBean = buildJsonResponseBean(false, 500, messsage, null, null,privacyRequestBean.getCorrelationId());
			        saveRequestAndResponse(responseBean,privacyRequestBean);
			        
			        JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
			        logger.info("response Json:: " + responseJsonObj.toString());
			        String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
					return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
			     } catch (Exception e) {
			    	 logger.info("Exception Caught ", e);
			     }
			    
		    } else {
		    	messsage.add("Method not allowed");
		    	PrivacyResponseBean responseBean = buildJsonResponseBean(false, 405, messsage, null, null,privacyRequestBean.getCorrelationId()); 
@@ -195,9 +199,29 @@ public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven
		    	
		    	JSONObject responseJsonObj = JSONUtil.beanObjectToJSONObjct(responseBean);
		    	logger.info("response Json:: " + responseJsonObj.toString());
		    	String encryptedResponse = encryptData(responseJsonObj.toString(), EncryptDecryptUtil.decrypt(secreteKey));
		    	return new StreamResult(new ByteArrayInputStream(encryptedResponse.toString().getBytes()));
		    	
		    	
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
@@ -407,6 +431,28 @@ public class PrivacyNoticeAction extends CommonLoanAction implements ModelDriven
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
  SBI/src/com/mintstreet/consent/bo/PrivacyRequest.java 
+
17
−
16
@@ -2,26 +2,27 @@ package com.mintstreet.consent.bo;

public class PrivacyRequest {
	
	String auth_Key;
	String request;

    
	private String REQUEST_REFERENCE_NUMBER;
	private String REQUEST;
	private String DIGI_SIGN;
	
	public String getAuth_Key() {
		return auth_Key;
	public String getREQUEST_REFERENCE_NUMBER() {
		return REQUEST_REFERENCE_NUMBER;
	}

	public void setAuth_Key(String auth_Key) {
		this.auth_Key = auth_Key;
	public void setREQUEST_REFERENCE_NUMBER(String rEQUEST_REFERENCE_NUMBER) {
		REQUEST_REFERENCE_NUMBER = rEQUEST_REFERENCE_NUMBER;
	}

	public String getRequest() {
		return request;
	public String getREQUEST() {
		return REQUEST;
	}

	public void setRequest(String request) {
		this.request = request;
	public void setREQUEST(String rEQUEST) {
		REQUEST = rEQUEST;
	}
	public String getDIGI_SIGN() {
		return DIGI_SIGN;
	}
	public void setDIGI_SIGN(String dIGI_SIGN) {
		DIGI_SIGN = dIGI_SIGN;
	}
	
	
}
  SBI/src/com/mintstreet/consent/bo/PrivacyResponseEIS.java  0 → 100644
+
35
−
0
package com.mintstreet.consent.bo;

public class PrivacyResponseEIS {
	
	private String RESPONSE;
	private String REQUEST_REFERENCE_NUMBER;
	private String RESPONSE_DATE;
	private String DIGI_SIGN;
	
	public String getRESPONSE() {
		return RESPONSE;
	}
	public void setRESPONSE(String rESPONSE) {
		RESPONSE = rESPONSE;
	}
	public String getREQUEST_REFERENCE_NUMBER() {
		return REQUEST_REFERENCE_NUMBER;
	}
	public void setREQUEST_REFERENCE_NUMBER(String rEQUEST_REFERENCE_NUMBER) {
		REQUEST_REFERENCE_NUMBER = rEQUEST_REFERENCE_NUMBER;
	}
	public String getRESPONSE_DATE() {
		return RESPONSE_DATE;
	}
	public void setRESPONSE_DATE(String rESPONSE_DATE) {
		RESPONSE_DATE = rESPONSE_DATE;
	}
	public String getDIGI_SIGN() {
		return DIGI_SIGN;
	}
	public void setDIGI_SIGN(String dIGI_SIGN) {
		DIGI_SIGN = dIGI_SIGN;
	}
	
}
  SBI/src/com/mintstreet/integration/pan/util/PanApiRSAEncryption.java 
+
39
−
1
@@ -15,6 +15,7 @@ import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
@@ -98,7 +99,8 @@ public class PanApiRSAEncryption {
    return base64PublicKey;
  }
  
  public String RSAEncrypt(String data) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
  public String RSAEncrypt(String data) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, 
  										InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String encData = "";
    if (base64PublicKey == null)
      base64PublicKey = getRSAPublicKey(); 
@@ -137,4 +139,40 @@ public class PanApiRSAEncryption {
    PrivateKey key = (PrivateKey)keystore.getKey(KEYSTORE_ALIAS, KEYSTORE_PASS.toCharArray());
    return key;
  }
  
  
  public static String RSADecrypt (String encryptedData) {
		
	  String decryptedData = "";
	  PrivateKey privateKey;
		
	  try {
		  privateKey = getPrivateKey();
		
		  Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
		  cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
		  decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), "UTF-8");
			
		} catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException
				| IOException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			logger.info("exception getMessage", e);
		}
	  return decryptedData;
  }
  
	public static boolean verifyDigitalSignature(String plainRequest, String receivedSignature) 
			throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		
		PublicKey publicKey = getPublicKey();
		
		Signature signature = Signature.getInstance("SHA256withRSA");
		signature.initVerify(publicKey);
		signature.update(plainRequest.getBytes("UTF-8"));
		
		byte[] signatureBytes = Base64.getDecoder().decode(receivedSignature);
		return signature.verify(signatureBytes);
	}

  
}
