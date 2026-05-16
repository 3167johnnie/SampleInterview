package com.mintstreet.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConstantUtil {
	private static final Logger logger = LogManager.getLogger(ConstantUtil.class.getName());

	public static String updateConstants(){
		FileInputStream configStream = null;
		FileInputStream constantStream = null;
		try {
			configStream = new FileInputStream(System.getProperty("catalina.home").replaceAll("\\\\","/")+"/ocas_configuration/config.properties");
		    constantStream = new FileInputStream(System.getProperty("catalina.home").replaceAll("\\\\","/")+"/ocas_configuration/Constants.properties");
			ResourceBundle bundleConfig = new PropertyResourceBundle(configStream);
			ResourceBundle bundle = new PropertyResourceBundle(constantStream);
			Constants.DIP = bundleConfig.getString("jdbc.ip");
			Constants.DPT = bundleConfig.getString("jdbc.port");
			Constants.DN = bundleConfig.getString("jdbc.name");
			Constants.DU = bundleConfig.getString("jdbc.username");
			Constants.DP = bundleConfig.getString("jdbc.password");
			
			Constants.USER_NAME = bundleConfig.getString("sendMail.userName");
			Constants.PASSWORD = bundleConfig.getString("sendMail.password");
			Constants.SMTP_SERVER_HOST_NAME = bundleConfig.getString("sendMail.smtpServerHostName");
			Constants.SMTP_PORT = bundleConfig.getString("sendMail.smtpPort");
			Constants.AUTH_REQUIRED = bundleConfig.getString("sendMail.authenticationReqd");
			Constants.DEBUG = new Boolean(bundleConfig.getString("sendMail.debug"));
			Constants.FROM_ADDRESS = bundleConfig.getString("sendMail.fromAddress");
			Constants.STAFF_ADDRESS = bundleConfig.getString("sendMail.staffAddress");
			Constants.ALTERNATE_ADDRESS = bundleConfig.getString("sendMail.alternateAddress");
			Constants.MODE = bundleConfig.getString("sendMail.mode");
			
			Constants.DEPLOYMENT_MODE = bundleConfig.getString("settings.deploymentMode");
			Constants.DEPLOYMENT_ENVIRONMENT = bundleConfig.getString("settings.deploymentEnvironment");
			Constants.SCRIPT_VERSION = bundleConfig.getString("settings.scriptVersion");
			Constants.SCRIPT_COMPRESSION = bundleConfig.getString("settings.scriptCompression");
			
			Constants.HL_PRICING_ENGINE_URL = bundle.getString("HL_PRICING_ENGINE_URL");
			Constants.AL_PRICING_ENGINE_URL = bundle.getString("AL_PRICING_ENGINE_URL");
			Constants.PL_PRICING_ENGINE_URL = bundle.getString("PL_PRICING_ENGINE_URL");
			Constants.EL_PRICING_ENGINE_URL = bundle.getString("EL_PRICING_ENGINE_URL");
			Constants.SCC_PRICING_ENGINE_URL =bundle.getString("SCC_PRICING_ENGINE_URL");
			
			Constants.HL_PDF_GENRATION_URL = bundle.getString("HL_PDF_GENRATION_URL");
			Constants.AL_PDF_GENRATION_URL = bundle.getString("AL_PDF_GENRATION_URL");
			Constants.EL_PDF_GENRATION_URL = bundle.getString("EL_PDF_GENRATION_URL");
			Constants.PL_PDF_GENRATION_URL = bundle.getString("PL_PDF_GENRATION_URL");
			Constants.AGL_PDF_GENRATION_URL = bundle.getString("AGL_PDF_GENRATION_URL");
			
			Constants.HL_RSM_ENGINE_URL = bundle.getString("HL_RSM_ENGINE_URL");
			Constants.AL_RSM_ENGINE_URL = bundle.getString("AL_RSM_ENGINE_URL");
			Constants.PL_RSM_ENGINE_URL = bundle.getString("PL_RSM_ENGINE_URL");
			Constants.EL_RSM_ENGINE_URL = bundle.getString("EL_RSM_ENGINE_URL");
			Constants.CBS_CIF_LEVEL_API_URL = bundle.getString("CBS_CIF_LEVEL_API_URL");
			Constants.CBS_LOAN_ACCOUNT_API_URL = bundle.getString("CBS_LOAN_ACCOUNT_API_URL");
			Constants.CBS_IRAC_STATUS_BYPASS  = new Boolean(bundle.getString("CBS_IRAC_STATUS_BYPASS"));
			Constants.CBS_SERVICE_CALL_BYPASS  = new Boolean(bundle.getString("CBS_SERVICE_CALL_BYPASS"));
			
			Constants.PDF_GENRATION_BASE_PATH = bundle.getString("PDF_GENRATION_BASE_PATH");
			Constants.PDF_DOWNLOAD_BASE_PATH = bundle.getString("PDF_DOWNLOAD_BASE_PATH");
			Constants.HL_PDF_GENRATION_LOCATION = bundle.getString("HL_PDF_GENRATION_LOCATION");
			Constants.AL_PDF_GENRATION_LOCATION = bundle.getString("AL_PDF_GENRATION_LOCATION");
			Constants.PL_PDF_GENRATION_LOCATION = bundle.getString("PL_PDF_GENRATION_LOCATION");
			Constants.EL_PDF_GENRATION_LOCATION = bundle.getString("EL_PDF_GENRATION_LOCATION");
			Constants.AGL_PDF_GENRATION_LOCATION = bundle.getString("AGL_PDF_GENRATION_LOCATION");
			Constants.SBI_TATA_PDF_GENRATION_LOCATION = bundle.getString("SBI_TATA_PDF_GENRATION_LOCATION");
			Constants.SBI_TATA_PDF_GENERATION_URL = bundle.getString("SBI_TATA_PDF_GENERATION_URL");
			
			
			Constants.HOME_ACTION = bundle.getString("HOME_ACTION");
			Constants.HOME_LOAN_ACTION = bundle.getString("HOME_LOAN_ACTION");
			Constants.HOME_TOP_UP_LOAN_ACTION = bundle.getString("HOME_TOP_UP_LOAN_ACTION");
			Constants.HOME_TOP_UP_LOAN_ACTION_DSR = "home-top-up-loan-dsr";
			Constants.AUTO_LOAN_ACTION = bundle.getString("AUTO_LOAN_ACTION");
			Constants.EDUCATION_LOAN_ACTION = bundle.getString("EDUCATION_LOAN_ACTION");
			Constants.SCHOLAR_LOAN_ACTION = bundle.getString("SCHOLAR_LOAN_ACTION");
			Constants.GLOBAL_EDVANTAGE_ACTION =bundle.getString("GLOBAL_EDVANTAGE_ACTION");
			Constants.PERSONAL_LOAN_ACTION = bundle.getString("PERSONAL_LOAN_ACTION");
			Constants.APPLICATION_STATUS_ACTION = bundle.getString("APPLICATION_STATUS_ACTION");
			Constants.AGRI_LOAN_ACTION = bundle.getString("AGRI_LOAN_ACTION");
			
			Constants.HOME_LOAN_ACTION_DSR = bundle.getString("HOME_LOAN_ACTION_DSR");
			Constants.AUTO_LOAN_ACTION_DSR = bundle.getString("AUTO_LOAN_ACTION_DSR");
			Constants.EDUCATION_LOAN_ACTION_DSR = bundle.getString("EDUCATION_LOAN_ACTION_DSR");
			Constants.PERSONAL_LOAN_ACTION_DSR = bundle.getString("PERSONAL_LOAN_ACTION_DSR");
			Constants.AGRI_LOAN_ACTION_DSR = bundle.getString("AGRI_LOAN_ACTION_DSR");
			
			Constants.DATE_FORMAT = "dd-MM-yyyy";
			Constants.DATE_FORMAT_WITHOUT_DELIMITER = "ddMMyyyy";
			Constants.SENDER_EMAIL = bundle.getString("SENDER_EMAIL");
			Constants.ALIAS_EMAIL = bundle.getString("ALIAS_EMAIL");
			
			Constants.APPLICATION_STATUS_TITLE =bundle.getString("APPLICATION_STATUS_TITLE");
			Constants.APPLICATION_STATUS_KEYWORDS =bundle.getString("APPLICATION_STATUS_KEYWORDS");
			Constants.APPLICATION_STATUS_DESCRIPTION =bundle.getString("APPLICATION_STATUS_DESCRIPTION");
			
			Constants.HOME_TITLE =bundle.getString("HOME_TITLE");
			Constants.HOME_KEYWORDS =bundle.getString("HOME_KEYWORDS");
			Constants.HOME_DESCRIPTION =bundle.getString("HOME_DESCRIPTION");
			
			Constants.HOME_LOAN_TITLE =bundle.getString("HOME_LOAN_TITLE");
			Constants.HOME_LOAN_KEYWORDS =bundle.getString("HOME_LOAN_KEYWORDS");
			Constants.HOME_LOAN_DESCRIPTION =bundle.getString("HOME_LOAN_DESCRIPTION");

			Constants.AUTO_LOAN_TITLE =bundle.getString("AUTO_LOAN_TITLE");
			Constants.AUTO_LOAN_KEYWORDS =bundle.getString("AUTO_LOAN_KEYWORDS");
			Constants.AUTO_LOAN_DESCRIPTION =bundle.getString("AUTO_LOAN_DESCRIPTION");
			
			Constants.EDUCATION_LOAN_TITLE =bundle.getString("EDUCATION_LOAN_TITLE");
			Constants.EDUCATION_LOAN_KEYWORDS =bundle.getString("EDUCATION_LOAN_KEYWORDS");
			Constants.EDUCATION_LOAN_DESCRIPTION =bundle.getString("EDUCATION_LOAN_DESCRIPTION");
			
			Constants.PERSONAL_LOAN_TITLE =bundle.getString("PERSONAL_LOAN_TITLE");
			Constants.PERSONAL_LOAN_KEYWORDS =bundle.getString("PERSONAL_LOAN_KEYWORDS");
			Constants.PERSONAL_LOAN_DESCRIPTION =bundle.getString("PERSONAL_LOAN_DESCRIPTION");

			Constants.AGRI_LOAN_TITLE =bundle.getString("AGRI_LOAN_TITLE");
			Constants.AGRI_LOAN_KEYWORDS =bundle.getString("AGRI_LOAN_KEYWORDS");
			Constants.AGRI_LOAN_DESCRIPTION =bundle.getString("AGRI_LOAN_DESCRIPTION");

			Constants.DUMMY_MOBILE_NO = bundle.getString("DUMMY_MOBILE_NO")+";";
			Constants.DUMMY_EMAIL_ID = bundle.getString("DUMMY_EMAIL_ID")+";";
			
			Constants.SERVICE_TAX = new Double(bundle.getString("SERVICE_TAX"));
			
			Constants.INTIAL_STRING_HL=bundle.getString("INTIAL_STRING_HL");
			Constants.INTIAL_STRING_AL=bundle.getString("INTIAL_STRING_AL");
			Constants.INTIAL_STRING_PL=bundle.getString("INTIAL_STRING_PL");
			Constants.INTIAL_STRING_EL=bundle.getString("INTIAL_STRING_EL");
			Constants.INTIAL_STRING_AGL=bundle.getString("INTIAL_STRING_AGL");
			
			Constants.SOURCE_STRING_HL=bundle.getString("SOURCE_STRING_HL");
			Constants.SOURCE_STRING_AL=bundle.getString("SOURCE_STRING_AL");
			Constants.SOURCE_STRING_PL=bundle.getString("SOURCE_STRING_PL");
			Constants.SOURCE_STRING_EL=bundle.getString("SOURCE_STRING_EL");
			Constants.SOURCE_STRING_AGL=bundle.getString("SOURCE_STRING_AGL");
			
			Constants.CONTACT_NO_1=bundle.getString("CONTACT_NO_1");
			Constants.CONTACT_NO_2=bundle.getString("CONTACT_NO_2");
			Constants.CONTACT_NO_3=bundle.getString("CONTACT_NO_3");
			
			Constants.MESSAGE_HL_1=bundle.getString("MESSAGE_HL_1");

			Constants.CONTACT_MAIL=bundle.getString("CONTACT_MAIL");

			Constants.PROTOCOL=bundle.getString("PROTOCOL");
			Constants.PORT=bundle.getString("PORT");
			Constants.CONTEXT=bundle.getString("CONTEXT");
			Constants.IP_URL_INTERNET=bundle.getString("IP_URL_INTERNET");
			
			Constants.COPY_PERMISSION_COMMAND = bundle.getString("COPY_PERMISSION_COMMAND");
			Constants.FILE_PERMISSION_COMMAND = bundle.getString("FILE_PERMISSION_COMMAND");
			Constants.UPLOAD_INITIAL = bundle.getString("UPLOAD_INITIAL");
			Constants.UPLOAD_PATH_LIVE = bundle.getString("UPLOAD_PATH_LIVE");
			
			Constants.APP_DUPLICATION_CHECK = bundle.getString("APP_DUPLICATION_CHECK");
			Constants.APP_DUPLICATION_TIME_PERIOD = bundle.getString("APP_DUPLICATION_TIME_PERIOD");
			Constants.APP_DEDUPLICATION_MESSAGE = bundle.getString("APP_DEDUPLICATION_MESSAGE");
			
			Constants.APP_DUPLICATION_TIME_PERIOD_CARDS=bundle.getString("APP_DUPLICATION_TIME_PERIOD_CARDS");
			Constants.INQUIRY_DUPLICATION_TIME_PERIOD_CARDS = bundle.getString("INQUIRY_DUPLICATION_TIME_PERIOD_CARDS");

			Constants.INQUIRY_DUPLICATION_CHECK = bundle.getString("ENQUIRY_DUPLICATION_CHECK");
			Constants.INQUIRY_DUPLICATION_TIME_PERIOD = bundle.getString("ENQUIRY_DUPLICATION_TIME_PERIOD");
			Constants.INQUIRY_DEDUPLICATION_MESSAGE = bundle.getString("ENQUIRY_DEDUPLICATION_MESSAGE");
			
			Constants.HL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("HL_CAPTCHA_BY_PASS"));
			Constants.EL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("EL_CAPTCHA_BY_PASS"));
			Constants.AL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("AL_CAPTCHA_BY_PASS"));
			Constants.AGRI_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("AGRI_CAPTCHA_BY_PASS"));
			Constants.CREDIT_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("CREDIT_CAPTCHA_BY_PASS"));
			
			Constants.AUTO_LOAN_OFFER_ENABLE = Boolean.parseBoolean(bundle.getString("AUTO_LOAN_OFFER_ENABLE"));

			Constants.BUREAULINK_BYPASS_HL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_HL"));
			Constants.BUREAULINK_BYPASS_PL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_PL"));
			Constants.BUREAULINK_BYPASS_AL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_AL"));
			Constants.BUREAULINK_BYPASS_EL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_EL"));
			Constants.BUREAU=bundle.getString("BUREAU");
			
			Constants.SAVE_QUOTE_REDIRECTION= bundle.getString("SAVE_QUOTE_REDIRECTION");
			Constants.PDF_SESSION_TIMEOUT_REDIRECTION= bundle.getString("PDF_SESSION_TIMEOUT_REDIRECTION");
			Constants.ALREADY_LOGGEDIN_REDIRECTION= bundle.getString("ALREADY_LOGGEDIN_REDIRECTION");

			Constants.PICKUP_TIME_LIST = SbiUtil.generatePickupTimeList();

			Constants.BANK_IMAGE_FOLDER= bundle.getString("BANK_IMAGE_FOLDER");
			Constants.BANK_CSS_FOLDER= bundle.getString("BANK_CSS_FOLDER");
			Constants.BANK_JS_FOLDER= bundle.getString("BANK_JS_FOLDER");

			Constants.BANK_ID = Integer.parseInt(bundle.getString("BANK_ID"));
			Constants.BANK_NAME= bundle.getString("BANK_NAME");
			Constants.BANK_FULL_NAME= bundle.getString("BANK_FULL_NAME");
			Constants.BANK_URL= bundle.getString("BANK_URL");
			Constants.BANK_FULL_URL= bundle.getString("BANK_FULL_URL");
			Constants.BANK_COPYRIGHT= bundle.getString("BANK_COPYRIGHT");
			Constants.OTP_SUBJECT_LINE= bundle.getString("OTP_SUBJECT_LINE");

			Constants.HOME_LOAN_PRODUCT_NAME = bundle.getString("HOME_LOAN_PRODUCT_NAME");
			Constants.AUTO_LOAN_PRODUCT_NAME = bundle.getString("AUTO_LOAN_PRODUCT_NAME");
			Constants.PERSONAL_LOAN_PRODUCT_NAME = bundle.getString("PERSONAL_LOAN_PRODUCT_NAME");
			Constants.EDUCATION_LOAN_PRODUCT_NAME = bundle.getString("EDUCATION_LOAN_PRODUCT_NAME");
			Constants.FLEXI_PAY_PRODUCT_ID = Integer.parseInt(bundle.getString("FLEXI_PAY_PRODUCT_ID"));
			Constants.SBI_PRIVILEGE_PRODUCT_ID = Integer.parseInt(bundle.getString("SBI_PRIVILEGE_PRODUCT_ID"));
			Constants.SBI_SHAURYA_PRODUCT_ID = Integer.parseInt(bundle.getString("SBI_SHAURYA_PRODUCT_ID"));
			Constants.XPRESS_CREDIT_IT_PRODUCT_ID = Integer.parseInt(bundle.getString("XPRESS_CREDIT_IT_PRODUCT_ID"));
			Constants.XPRESS_CREDIT_ELITE_PRODUCT_ID = Integer.parseInt(bundle.getString("XPRESS_CREDIT_ELITE_PRODUCT_ID"));
			Constants.XPRESS_CREDIT_NPE_PRODUCT_ID = Integer.parseInt(bundle.getString("XPRESS_CREDIT_NPE_PRODUCT_ID"));

			Constants.REGULAR_PENSION = Integer.parseInt(bundle.getString("REGULAR_PENSION"));
			Constants.JAI_JAWAN_PENSION_LOAN = Integer.parseInt(bundle.getString("JAI_JAWAN_PENSION_LOAN"));
			Constants.FAMILY_PENSION = Integer.parseInt(bundle.getString("FAMILY_PENSION"));
			
			Constants.BANK_ONLINE_URL = bundle.getString("BANK_ONLINE_URL");
			Constants.BANK_FINDER_URL = bundle.getString("BANK_FINDER_URL");
			Constants.BANK_SUPPORT_EMAIL = bundle.getString("BANK_SUPPORT_EMAIL");

			Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS=new Boolean(bundle.getString("EKYC_BIOMETRIC_DEDUPE_BYPASS"));
			
			Constants.CBS_OTP_BYPASS = new Boolean(bundle.getString("CBS_OTP_BYPASS"));

			Constants.SERVER_SEQ=bundle.getString("SERVER_SEQ");
			
			Constants.AUTH_KEY=bundle.getString("AUTH_KEY");
			Constants.APP_MOBILE_HASH_KEY = bundle.getString("APP_MOBILE_HASH_KEY");
			
			Constants.DIP_LOCAL = bundleConfig.getString("DIP_LOCAL");
			Constants.DPT_LOCAL = bundleConfig.getString("DPT_LOCAL");
			Constants.CALLBACKSERVICEURL=bundle.getString("CALLBACKSERVICEURL");

			Constants.CLICK_TO_CALL_BYPASS = Boolean.parseBoolean(bundle.getString("CLICK_TO_CALL_BYPASS"));

			Constants.IS_PROXY_REQUIRED = new Boolean(bundle.getString("IS_PROXY_REQUIRED"));
			Constants.PROXY_HOST=bundle.getString("PROXY_HOST");
			Constants.PROXY_PORT=bundle.getString("PROXY_PORT");
			Constants.PROXY_USER=bundle.getString("PROXY_USER");
			Constants.PROXY_PASSWORD=bundle.getString("PROXY_PASSWORD");

			Constants.TNC_URL_HL = bundle.getString("TNC_URL_HL");
			Constants.TNC_URL_AL = bundle.getString("TNC_URL_AL");
			Constants.TNC_URL_PL = bundle.getString("TNC_URL_PL");

			Constants.SCC_PDF_GENRATION_LOCATION = bundle.getString("SCC_PDF_GENRATION_LOCATION");
			Constants.SCC_LOAN_ACTION = bundle.getString("SCC_LOAN_ACTION");
			Constants.SCC_LOAN_ACTION_DSR = bundle.getString("SCC_LOAN_ACTION_DSR");
			Constants.SCC_LOAN_TITLE = bundle.getString("SCC_LOAN_TITLE");
			Constants.SCC_LOAN_KEYWORDS = bundle.getString("SCC_LOAN_KEYWORDS");
			Constants.SCC_LOAN_DESCRIPTION = bundle.getString("SCC_LOAN_DESCRIPTION");
			Constants.INTIAL_STRING_SCC = bundle.getString("INTIAL_STRING_SCC");
			Constants.SOURCE_STRING_SCC = bundle.getString("SOURCE_STRING_SCC");

			Constants.PDF_TEMPLATE_BASE_PATH = bundle.getString("PDF_TEMPLATE_BASE_PATH");
			Constants.SCC_WEBSERVICE_BYPASS = new Boolean(bundle.getString("SCC_WEBSERVICE_BYPASS"));
			Constants.MAGIC_SALES_INITIAL = bundle.getString("MAGIC_SALES_INITIAL");
			Constants.ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
			
			Constants.CRM_NEW_WEBSERVICE =bundle.getString("CRM_NEW_WEBSERVICE");
			Constants.CRM_NEW_API_TOKEN=bundle.getString("CRM_NEW_API_TOKEN");
			Constants.CRM_NEW_AES_KEY = bundle.getString("CRM_NEW_AES_KEY");
			Constants.CRM_NEW_AES_IV = bundle.getString("CRM_NEW_AES_IV");
			Constants.CRM_NEW_API_ExpiresOn = bundle.getString("CRM_NEW_API_ExpiresOn");
			Constants.CRM_NEW_API_Username = bundle.getString("CRM_NEW_API_Username");
			Constants.CRM_NEW_API_Password = bundle.getString("CRM_NEW_API_Password");

			Constants.BANK_IMAGE_FOLDER_NEWUI= bundle.getString("BANK_IMAGE_FOLDER_NEWUI");
			Constants.BANK_CSS_FOLDER_NEWUI= bundle.getString("BANK_CSS_FOLDER_NEWUI");
			Constants.BANK_JS_FOLDER_NEWUI= bundle.getString("BANK_JS_FOLDER_NEWUI");

			Constants.CRM_BYPASS=new Boolean(bundle.getString("CRM_BYPASS"));
			Constants.CBS_DEDUPE_BYPASS=new Boolean(bundle.getString("CBS_DEDUPE_BYPASS"));

			Constants.IVRS_SALT=bundle.getString("IVRS_SALT");
			Constants.PL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("PL_CAPTCHA_BY_PASS"));
			Constants.PENSION_LOAN_ACTION = bundle.getString("PENSION_LOAN_ACTION");
			Constants.SCHEMENAME=bundle.getString("SCHEMENAME");
			
			Constants.EDUCATION_LOAN_TAKEOVER_ACTION =bundle.getString("EDUCATION_LOAN_TAKEOVER_ACTION");
			Constants.SORRY_FOR_INCONVENIENCE = bundle.getString("SORRY_FOR_INCONVENIENCE");
			Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT = bundle.getString("SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT");
			Constants.APP_RETRY_DUPLICATION_TIME_PERIOD = Integer.parseInt(bundle.getString("APP_RETRY_DUPLICATION_TIME_PERIOD"));
			Constants.APP_MAXIMUM_OTP_REQUEST_MSG = bundle.getString("APP_MAXIMUM_OTP_REQUEST_MSG");
			Constants.APP_MAXIMUM_OTP_REQUEST_COUNT = Integer.parseInt(bundle.getString("APP_MAXIMUM_OTP_REQUEST_COUNT"));
			Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG = bundle.getString("APP_MAXIMUM_OTP_ATTEMPT_MSG");
			Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT = Integer.parseInt(bundle.getString("APP_MAXIMUM_OTP_ATTEMPT_COUNT"));
			Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG = bundle.getString("APP_MAXIMUM_RESEND_OTP_REQUEST_MSG");
			Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG = bundle.getString("APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG");
			
			Constants.TOKEN_REQUEST_TIME_PERIOD = Integer.parseInt(bundle.getString("TOKEN_REQUEST_TIME_PERIOD"));

			Constants.BHL_PRODUCT_ID = Integer.parseInt(bundle.getString("BHL_PRODUCT_ID"));	
			Constants.BHL_SPREAD_RATE = Double.parseDouble(bundle.getString("BHL_SPREAD_RATE"));
			
			Constants.IS_ENGINE_OBF=new Boolean(bundle.getString("IS_ENGINE_OBF"));
			
			Constants.RAC_SETTING_ENABLE = new Boolean(bundle.getString("RAC_SETTING_ENABLE"));
			Constants.RAC_DTY = bundleConfig.getString("jdbc.servertype");
			Constants.RAC_DN_PERSONAL = bundleConfig.getString("jdbc.name_personal");
			Constants.RAC_DIP = bundleConfig.getString("jdbc.ip");
			Constants.RAC_DPT = bundleConfig.getString("jdbc.port");
			Constants.RAC_DN = bundleConfig.getString("jdbc.name");
			
			Constants.REFERENCE_NUMBER_BASED_ON_CLUSTER = new Boolean(bundle.getString("REFERENCE_NUMBER_BASED_ON_CLUSTER"));
			Constants.CALLBACK_SMS_CONSENT = new Boolean(bundle.getString("CALLBACK_SMS_CONSENT"));
			Constants.NONCBS_SMS_CONSENT = new Boolean(bundle.getString("NONCBS_SMS_CONSENT"));
			Constants.EXTERNAL_SERVICE_TIMED_OUT = Integer.parseInt(bundle.getString("EXTERNAL_SERVICE_TIMED_OUT"));
			Constants.SMS_SERVICE_BYPASS = new Boolean(bundle.getString("SMS_SERVICE_BYPASS"));
			Constants.PMAY_SCHEME1 =  bundle.getString("PMAY_SCHEME1");
			Constants.PMAY_SCHEME2 =  bundle.getString("PMAY_SCHEME2");
			Constants.PMAY_SCHEME3 =  bundle.getString("PMAY_SCHEME3");
			Constants.PMAY_SCHEME4 =  bundle.getString("PMAY_SCHEME4");
			Constants.PGL_PRODUCT_ID = Integer.parseInt(bundle.getString("PGL_PRODUCT_ID"));
			
			Constants.UI_TYPE = bundle.getString("UI_TYPE");
			Constants.SECRET_KEY_BYPASS = new Boolean(bundle.getString("SECRET_KEY_BYPASS"));
			Constants.GOLD_LOAN_ACTION = bundle.getString("GOLD_LOAN_ACTION");
			return "Sucess";
		} catch (NullPointerException e) {
			logger.info("ConstantUtil.java LNO 488 :: ",e);
			return e.toString();
		} catch (Exception e) {
			logger.info("ConstantUtil.java LNO 488 :: ",e);
			return e.toString();
		} finally {
	    	try {
				configStream.close();
				constantStream.close();
				IOUtils.closeQuietly(configStream);
				IOUtils.closeQuietly(constantStream);
			} catch (IOException e) {
				logger.info("Constants.java LN 1596 Error while closing resource :: ", e);
			};
	    }
	}
}
