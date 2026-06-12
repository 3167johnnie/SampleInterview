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
import java.util.Base64;

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

import com.google.gson.JsonSyntaxException;
import com.mintstreet.common.action.CommonLoanAction;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.consent.bo.PrivacyRequestBeanEnc;
import com.opensymphony.xwork2.ModelDriven;

public class PrivacyNoticeActionEncrypt extends CommonLoanAction implements ModelDriven<PrivacyRequestBeanEnc> {

	 
	private static final long serialVersionUID = 1L;
	
	  private static final int KEY_LENGTH = 256;
	  private static final int ITERATION_COUNT = 65536;
	  private static final int DEFAULT_GCM_IV_NONCE_SIZE_BYTES = 12;
	  private static final int DEFAULT_GCM_AUTHENTICATION_TAG_SIZE_BITS = 128;
	  private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
	  private String secreteKey = Constants.UPLOADLEAD_SECRETKEY;	

	private PrivacyRequestBeanEnc privacyRequestBeanEnc = new PrivacyRequestBeanEnc();
	
	  
	  private static final Logger logger = LogManager.getLogger(PrivacyNoticeActionEncrypt.class.getName());
	    
	  public StreamResult encryptPrivacyNoticeRequest() 
			  	throws JSONException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, 
			  	BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, UnsupportedEncodingException, SQLException  {

	  try {
		  
		  
		  
		  JSONObject requestJsonObj = JSONUtil.beanObjectToJSONObjct(privacyRequestBeanEnc);
		  logger.info("request Json:: " + requestJsonObj.toString());
		  
		  String encryptedPrivacyRequest = encryptData(requestJsonObj.toString(),EncryptDecryptUtil.decrypt(secreteKey));
		  logger.info("encryptedPrivacyRequest "+encryptedPrivacyRequest);
		  
		  return new StreamResult(new ByteArrayInputStream(encryptedPrivacyRequest.toString().getBytes()));

		  }catch (Exception e) {
		     logger.info("Exception ",e.getMessage());
		     logger.info("Exception ",e);
		     e.printStackTrace();
	     }
	  
	  return new StreamResult(new ByteArrayInputStream("HELLO ".toString().getBytes()));
	
	  }	  
	  
	  
	@Override
	public PrivacyRequestBeanEnc getModel() {
		// TODO Auto-generated method stub
		return privacyRequestBeanEnc;
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
