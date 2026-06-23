package com.mintstreet.integration.pan.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.util.Constants;

public class PanApiRSAEncryption {
	
	private static final Logger logger = LogManager.getLogger(PanApiRSAEncryption.class.getName());
	
  private static String X509 = "X.509";
  
  private static String RSA = "RSA/ECB/OAEPPadding";
  
  private static String filePath;
  
  public PanApiRSAEncryption() {
    if (Constants.CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH != null)
      filePath = Constants.CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH.trim(); 
    	logger.info("PanApiRSAEncryption ( ) CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH : :  "+filePath);
  }
  
  private static PublicKey getPublicKey() throws IOException, CertificateException {
    byte[] fileContent = FileUtils.readFileToByteArray(new File(filePath));
    if (fileContent.length == 0)
      throw new IOException("Invalid Public Key Certificate it seems!"); 
    
    logger.info("PanApiRSAEncryption ( ) "+fileContent);
    		
    CertificateFactory fact = CertificateFactory.getInstance(X509);
    X509Certificate cer = (X509Certificate)fact.generateCertificate(new ByteArrayInputStream(fileContent));
    logger.info("PanApiRSAEncryption ( ) "+cer);
    return cer.getPublicKey();
  }
  
  public String encryptWithPublicKey(String rawData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, CertificateException, IOException, IllegalBlockSizeException, BadPaddingException {
    Cipher encryptCipher = Cipher.getInstance(RSA);
    encryptCipher.init(1, getPublicKey());
    byte[] cipherText = encryptCipher.doFinal(rawData.getBytes(StandardCharsets.UTF_8));
    logger.info("PanApiRSAEncryption ( ) "+cipherText);
    return Base64.getEncoder().encodeToString(cipherText);
  }
  
  public static String base64PublicKey = null;
  
  public static String base64PrivateKey = null;
  
  //private static final String KEYSTORE_TYPE = "PKCS12";
  //private static final String DIGI_SIGN_ALGO = "SHA256withRSA";
  //private static final String CIPHER_ALGO = "RSA";
  //private static final String CIPHER_PADDING = "RSA/ECB/OAEPPadding";
  
  private static final String KEYSTORE_PATH = (Constants.RSA_KEYSTORE_PATH == null) ? null : Constants.RSA_KEYSTORE_PATH;
  
  private static final String KEYSTORE_PASS = (Constants.RSA_KEYSTORE_PASS == null) ? null : Constants.RSA_KEYSTORE_PASS;
  
  private static final String KEYSTORE_ALIAS = (Constants.RSA_KEYSTORE_ALIAS == null) ? null : Constants.RSA_KEYSTORE_ALIAS;
  
  public static String getRSAPublicKey() throws CertificateException, IOException {
    FileInputStream fin = new FileInputStream(filePath);
    CertificateFactory f = CertificateFactory.getInstance("X.509");
    X509Certificate cet = (X509Certificate)f.generateCertificate(fin);
    PublicKey publicKey = cet.getPublicKey();
    byte[] pk = publicKey.getEncoded();
    base64PublicKey = DatatypeConverter.printBase64Binary(pk);
    fin.close();
    return base64PublicKey;
  }
  
  public String RSAEncrypt(String data) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, 
  										InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String encData = "";
    if (base64PublicKey == null)
      base64PublicKey = getRSAPublicKey(); 
    byte[] base64decPublicKey = Base64.getDecoder().decode(base64PublicKey);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(base64decPublicKey);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey pubKey = keyFactory.generatePublic(keySpec);
    Cipher cip = Cipher.getInstance("RSA/ECB/OAEPPadding");
    cip.init(1, pubKey);
    byte[] encDataByte = cip.doFinal(data.getBytes("UTF-8"));
    encData = Base64.getEncoder().encodeToString(encDataByte);
    logger.info("RSA Encrypted Data "+encData);
    return encData;
  }
  
  public String getDigitalSignature(String data) {
    String encData = "";
    try {
      PrivateKey privateKey = getPrivateKey();
      Signature signature = Signature.getInstance("SHA256withRSA");
      signature.initSign(privateKey);
      signature.update(data.getBytes("UTF-8"));
      byte[] s = signature.sign();
      encData = Base64.getEncoder().encodeToString(s);
    } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
    	logger.info("PanApiRSAEncryption.java getDigitalSignature ( ) " + e);
    } catch (Exception e) {
    	logger.info("PanAPIRSA LNo: 130 :: exception caught ", e);
    } 
    return encData;
  }
  
  public static PrivateKey getPrivateKey() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException {
    KeyStore keystore = KeyStore.getInstance("PKCS12");
    keystore.load(new FileInputStream(KEYSTORE_PATH), KEYSTORE_PASS.toCharArray());
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
