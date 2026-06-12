package com.mintstreet.integration.pan.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.RandomStringUtils;

import com.mintstreet.common.util.Constants;

public class PanApiAESEncryption {
  
  private static final String prviate_key = Constants.CBS_SI_PAN_AES_PRIVATE_KEY;

  private String cipherAlgo = "AES/CBC/PKCS5PADDING";
  private String cipherType = "AES";
  
  public String getRandomAESKey() {
    return RandomStringUtils.random(32, 0, prviate_key.length(), true, true, prviate_key.toCharArray());
  }
  
  public String AESEncrypt(String message, String key) {
    try {
      byte[] keyBytes = key.getBytes("UTF-8");
      byte[] ivKey = Arrays.copyOf(keyBytes, 16);
      IvParameterSpec iv = new IvParameterSpec(ivKey);
      SecretKeySpec secKey = new SecretKeySpec(keyBytes, this.cipherType);
      Cipher c = Cipher.getInstance(this.cipherAlgo);
      c.init(1, secKey, iv);
      byte[] encVal = c.doFinal(message.getBytes("UTF-8"));
      String encryptedVal = Base64.getEncoder().encodeToString(encVal);
      return encryptedVal;
    } catch (NoSuchAlgorithmException e) {
        return "X-JavaError" + e.toString();
    } catch (InvalidAlgorithmParameterException e) {
          return "X-JavaError" + e.toString();
    } catch (Exception e) {
      return "X-JavaError" + e.toString();
    } 
  }
  
  public String AESDecrypt(String message, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    String decryptedVal = null;
    if (message.trim().length() > 0) {
      byte[] keyByte = key.getBytes("UTF-8");
      byte[] ivKey = Arrays.copyOf(keyByte, 16);
      IvParameterSpec iv = new IvParameterSpec(ivKey);
      byte[] encVal = Base64.getDecoder().decode(message);
      SecretKeySpec secKey = new SecretKeySpec(keyByte, this.cipherType);
      Cipher c = Cipher.getInstance(this.cipherAlgo);
      c.init(2, secKey, iv);
      byte[] decVal = c.doFinal(encVal);
      decryptedVal = new String(decVal);
    } 
    return decryptedVal;
  }
  
  	public String AESEncryptGCM(String data, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
  		
  		byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
  		if (keyBytes.length != 32) {
  			throw new RuntimeException("AES key must be exactly 32 bytes");
  		}
  		
  		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
  		
  		//IV - first 12 bytes of key
  		byte[] iv = Arrays.copyOfRange(keyBytes, 0, 12);
  		GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
  		
  		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
  		cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
  		
  		byte [] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
  		return Base64.getEncoder().encodeToString(encryptedBytes);
  	}
  	
  	public String AESDecryptGCM(String strToDecrypt, String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
  		
  		byte [] keyBytes = key.getBytes(StandardCharsets.UTF_8);
  		if (keyBytes.length != 32) {
  			throw new RuntimeException("AES key must be exactly 32 bytes");
  		}
  		
  		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
  		
  		//IV - first 12 bytes of key
  		byte[] iv = Arrays.copyOfRange(keyBytes, 0, 12);
  		GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
  		
  		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
  		cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec);
  		
  		byte [] decodedBytes = Base64.getDecoder().decode(strToDecrypt);
  		byte [] decryptedBytes = cipher.doFinal(decodedBytes);
  		
  		return new String(decryptedBytes, StandardCharsets.UTF_8);
  	}
}
