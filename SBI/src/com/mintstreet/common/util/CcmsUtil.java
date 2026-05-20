package com.mintstreet.common.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.mintstreet.integration.pan.util.PanApiAESEncryption;
import com.mintstreet.integration.pan.util.PanApiRSAEncryption;

public class CcmsUtil {
	private static final Logger logger = LogManager.getLogger(CcmsUtil.class.getName());
	private final static String USER_AGENT = "Mozilla/5.0";
	
	private String plainRequestCcms = null;
	private String eisFinalRequest = null;
	private String eisEncryptedResponseStr = null;  

	public JSONObject callingEISServiceForCcms(String plainRequest, String engineUrl) throws JSONException {
		JSONObject json= new JSONObject();
		
		try {
			
			engineUrl = "https://eissiuat.sbi.co.in/gen6/gateway/misc/thirdParty/wrapper/services";
			
			PanApiAESEncryption panApiAESEncryption = new PanApiAESEncryption();
			PanApiRSAEncryption panApiRSAEncryption = new PanApiRSAEncryption();
			
			plainRequestCcms = null;
			eisFinalRequest = null;
			eisEncryptedResponseStr = null;  
			
			//step 1
			String randomAesKey = panApiAESEncryption.getRandomAESKey(); 
			
			//step 2
			plainRequestCcms = plainRequest;
			String encryptedRequestCcms = panApiAESEncryption.AESEncryptGCM(plainRequestCcms, randomAesKey);
			
			//step 3
			String digiSignature = panApiRSAEncryption.getDigitalSignature(plainRequestCcms);   
			
			//step 4
			String headerSecretKey = panApiRSAEncryption.RSAEncrypt(randomAesKey);
			
			//step 5
			String referenceNumber = SbiUtil.getRandomAlphaNumericReferenceNumber();
			
			
			logger.info("encryptedRequestCcms "+encryptedRequestCcms);
			logger.info("plainRequest "+plainRequest);
			logger.info("digiSignature "+digiSignature);
			logger.info("referenceNumber "+referenceNumber);
			logger.info("headerSecretKey "+headerSecretKey);
			logger.info("randomAesKey "+randomAesKey);
			
			JSONObject apiRequest = new JSONObject();
			apiRequest.put("REQUEST_REFERENCE_NUMBER", referenceNumber);
			apiRequest.put("REQUEST", encryptedRequestCcms);
			apiRequest.put("DIGI_SIGN", digiSignature);
			
			eisFinalRequest = apiRequest.toString();
			logger.info("eisFinalRequest "+eisFinalRequest);
			logger.info("engineUrl "+engineUrl);
			
			URL url = new URL(engineUrl);
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setConnectTimeout(Constants.EXTERNAL_SERVICE_TIMED_OUT);
			con.setReadTimeout(Constants.EXTERNAL_SERVICE_TIMED_OUT);
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Content-Type","application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestProperty("AccessToken", headerSecretKey);        
			con.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(eisFinalRequest);    
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			logger.info("CCMS Read Consent Purpose: " + plainRequestCcms);
			logger.info("Response Code : " + responseCode);
			BufferedReader in = null;
			boolean status = true;

			StringBuffer apiResponse = new StringBuffer(3000);      

			//step 10
			if (con.getInputStream() != null) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()),3000);
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					apiResponse.append(inputLine);
				}
				eisEncryptedResponseStr = apiResponse.toString();
				logger.info("eisEncryptedResponseStr : " + eisEncryptedResponseStr);
			}
			
			//step 11
			JSONObject eisEncryptedResponseJson = new JSONObject();
			eisEncryptedResponseJson = JSONUtil.getJONObjctFromJSONString(eisEncryptedResponseStr);
			
			String ccmsEncryptedResponseStr = eisEncryptedResponseJson.optString("RESPONSE");
			
			String ccmsDecryptedResponseStr =  panApiAESEncryption.AESDecryptGCM(ccmsEncryptedResponseStr, randomAesKey);
			JSONObject ccmsDecryptedResponseJson = JSONUtil.getJONObjctFromJSONString(ccmsDecryptedResponseStr);      
			
			json = JSONUtil.getJONObjctFromJSONString(ccmsDecryptedResponseJson.toString());
			
			String errorDescription = ccmsDecryptedResponseJson.optString("ERROR_DESCRIPTION");
			logger.info("ERROR_DESCRIPTION: " + errorDescription);
			if(errorDescription.length()>1){ 
				status=false;
			}

			in.close();
			con.disconnect();
    
			if (status){
				json.put("error_status", 1);        
			}else{   
				json.put("error_status", 0);
			}
			json.put("REQUEST_REFERENCE_NUMBER",eisEncryptedResponseJson.optString("REQUEST_REFERENCE_NUMBER"));
			logger.info("Response received json from CustomerEnquiry :: "+EncryptDecryptUtil.encrypt(json.toString()));

		}catch(MalformedURLException e){
			logger.info("CcmsUtil.java LNo: 126 ::", e);
			json.put("error_status", "0");
		}catch (Exception e) {
			logger.info("CcmsUtil.java LNo: 130::", e);
			json.put("error_status", "0");
		}  

		return json;
	}

}
