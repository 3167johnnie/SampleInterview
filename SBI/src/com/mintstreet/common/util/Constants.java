package com.mintstreet.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
  private static Logger logger = LogManager.getLogger(Constants.class.getName());
  
  public static String DIP = null;
  
  public static String DPT = null;
  
  public static String DN = null;
  
  public static String DU = null;
  
  public static String DP = null;
  
  public static String USER_NAME = null;
  
  public static String PASSWORD = null;
  
  public static String SMTP_SERVER_HOST_NAME = null;
  
  public static String SMTP_PORT = null;
  
  public static String AUTH_REQUIRED = null;
  
  public static boolean DEBUG = false;
  
  public static String FROM_ADDRESS = null;
  
  public static String STAFF_ADDRESS = null;
  
  public static String ALTERNATE_ADDRESS = null;
  
  public static String MODE = null;
  
  public static String DEPLOYMENT_MODE = null;
  
  public static String DEPLOYMENT_ENVIRONMENT = null;
  
  public static String SCRIPT_VERSION = null;
  
  public static String SCRIPT_COMPRESSION = null;
  
  public static String HL_PRICING_ENGINE_URL = null;
  
  public static String AL_PRICING_ENGINE_URL = null;
  
  public static String PL_PRICING_ENGINE_URL = null;
  
  public static String EL_PRICING_ENGINE_URL = null;
  
  public static String SCC_PRICING_ENGINE_URL = null;
  
  public static String HL_PDF_GENRATION_URL = null;
  
  public static String AL_PDF_GENRATION_URL = null;
  
  public static String EL_PDF_GENRATION_URL = null;
  
  public static String PL_PDF_GENRATION_URL = null;
  
  public static String AGL_PDF_GENRATION_URL = null;
  
  public static String HL_RSM_ENGINE_URL = null;
  
  public static String AL_RSM_ENGINE_URL = null;
  
  public static String PL_RSM_ENGINE_URL = null;
  
  public static String EL_RSM_ENGINE_URL = null;
  
  public static String CBS_CIF_LEVEL_API_URL = null;
  
  public static String CBS_LOAN_ACCOUNT_API_URL = null;
  
  public static Integer APP_APP_SUB_TYPE_ID_NORMAL = Integer.valueOf(0);
  
  public static Integer APP_APP_SUB_TYPE_ID_EKYC = Integer.valueOf(1);
  
  public static Integer APP_APP_SUB_TYPE_ID_CBS = Integer.valueOf(2);
  
  public static boolean CBS_IRAC_STATUS_BYPASS = false;
  
  public static boolean CBS_SERVICE_CALL_BYPASS = false;
  
  public static String PDF_GENRATION_BASE_PATH = null;
  
  public static String PDF_DOWNLOAD_BASE_PATH = null;
  
  public static String HL_PDF_GENRATION_LOCATION = null;
  
  public static String AL_PDF_GENRATION_LOCATION = null;
  
  public static String PL_PDF_GENRATION_LOCATION = null;
  
  public static String EL_PDF_GENRATION_LOCATION = null;
  
  public static String AGL_PDF_GENRATION_LOCATION = null;
  
  public static String SBI_TATA_PDF_GENRATION_LOCATION = null;
  
  public static String SBI_TATA_PDF_GENERATION_URL = null;
  
  public static String HOME_ACTION = null;
  
  public static String HOME_LOAN_ACTION = null;
  
  public static String HOME_TOP_UP_LOAN_ACTION = null;
  
  public static String HOME_TOP_UP_LOAN_ACTION_DSR = null;
  
  public static String AUTO_LOAN_ACTION = null;
  
  public static String EDUCATION_LOAN_ACTION = null;
  
  public static String SCHOLAR_LOAN_ACTION = null;
  
  public static String GLOBAL_EDVANTAGE_ACTION = null;
  
  public static String PERSONAL_LOAN_ACTION = null;
  
  public static String APPLICATION_STATUS_ACTION = null;
  
  public static String AGRI_LOAN_ACTION = null;
  
  public static String HOME_LOAN_ACTION_DSR = null;
  
  public static String AUTO_LOAN_ACTION_DSR = null;
  
  public static String EDUCATION_LOAN_ACTION_DSR = null;
  
  public static String PERSONAL_LOAN_ACTION_DSR = null;
  
  public static String AGRI_LOAN_ACTION_DSR = null;
  
  public static Integer AUTO_DEALER_ID = Integer.valueOf(33);
  
  public static Integer HOME_BUILDER_ID = Integer.valueOf(204);
  
  public static String DATE_FORMAT = "dd-MM-yyyy";
  
  public static String DATE_TIME_FORMAT = "dd-MM-yyyy hh:mm:ss";
  
  public static String DATE_FORMAT_WITHOUT_DELIMITER = "ddMMyyyy";
  
  public static String SENDER_EMAIL = null;
  
  public static String ALIAS_EMAIL = null;
  
  public static String APPLICATION_STATUS_TITLE = null;
  
  public static String APPLICATION_STATUS_KEYWORDS = null;
  
  public static String APPLICATION_STATUS_DESCRIPTION = null;
  
  public static String HOME_TITLE = null;
  
  public static String HOME_KEYWORDS = null;
  
  public static String HOME_DESCRIPTION = null;
  
  public static String HOME_LOAN_TITLE = null;
  
  public static String HOME_LOAN_KEYWORDS = null;
  
  public static String HOME_LOAN_DESCRIPTION = null;
  
  public static String AUTO_LOAN_TITLE = null;
  
  public static String AUTO_LOAN_KEYWORDS = null;
  
  public static String AUTO_LOAN_DESCRIPTION = null;
  
  public static String EDUCATION_LOAN_TITLE = null;
  
  public static String EDUCATION_LOAN_KEYWORDS = null;
  
  public static String EDUCATION_LOAN_DESCRIPTION = null;
  
  public static String PERSONAL_LOAN_TITLE = null;
  
  public static String PERSONAL_LOAN_KEYWORDS = null;
  
  public static String PERSONAL_LOAN_DESCRIPTION = null;
  
  public static String AGRI_LOAN_TITLE = null;
  
  public static String AGRI_LOAN_KEYWORDS = null;
  
  public static String AGRI_LOAN_DESCRIPTION = null;
  
  public static String DUMMY_MOBILE_NO = null;
  
  public static String DUMMY_EMAIL_ID = null;
  
  public static String DUMMY_MOBILE_OTP = "123456";
  
  public static String DUMMY_EMAIL_OTP = "abc123";
  
  public static Double SERVICE_TAX = null;
  
  public static String INTIAL_STRING_HL = null;
  
  public static String INTIAL_STRING_AL = null;
  
  public static String INTIAL_STRING_PL = null;
  
  public static String INTIAL_STRING_EL = null;
  
  public static String INTIAL_STRING_AGL = null;
  
  public static String SOURCE_STRING_HL = null;
  
  public static String SOURCE_STRING_AL = null;
  
  public static String SOURCE_STRING_PL = null;
  
  public static String SOURCE_STRING_EL = null;
  
  public static String SOURCE_STRING_AGL = null;
  
  public static String CONTACT_NO_1 = null;
  
  public static String CONTACT_NO_2 = null;
  
  public static String CONTACT_NO_3 = null;
  
  public static String MESSAGE_HL_1 = null;
  
  public static String CONTACT_MAIL = null;
  
  public static String PROTOCOL = null;
  
  public static String PORT = null;
  
  public static String CONTEXT = null;
  
  public static String IP_URL_INTERNET = null;
  
  public static String URL_QUTOE_TOKEN_HL = "HLQuoteToken";
  
  public static String URL_QUTOE_TOKEN_HLTOPUP = "HLTopupQuoteToken";
  
  public static String URL_QUTOE_TOKEN_AL = "ALQuoteToken";
  
  public static String URL_QUTOE_TOKEN_EL = "ELQuoteToken";
  
  public static String URL_QUTOE_TOKEN_PL = "PLQuoteToken";
  
  public static String URL_QUTOE_TOKEN_AGL = "AGLQuoteToken";
  
  public static String URL_QUTOE_TOKEN_SCC = "SCCQuoteToken";
  
  public static Integer STATUS_INACTIVE = Integer.valueOf(0);
  
  public static Integer STATUS_ACTIVE = Integer.valueOf(1);
  
  public static Integer HOME_ID = Integer.valueOf(0);
  
  public static Integer HOME_LOAN_ID = Integer.valueOf(1);
  
  public static Integer HOME_TOP_UP_LOAN_ID = Integer.valueOf(11);
  
  public static Integer AUTO_LOAN_ID = Integer.valueOf(2);
  
  public static Integer PERSONAL_LOAN_ID = Integer.valueOf(3);
  
  public static Integer EDUCATION_LOAN_ID = Integer.valueOf(4);
  
  public static Integer EDVANTAGE_LOAN_ID = Integer.valueOf(13);
  
  public static Integer SCHOLAR_LOAN_ID = Integer.valueOf(12);
  
  public static Integer HOME_LOAN_DSR_ID = Integer.valueOf(5);
  
  public static Integer AUTO_LOAN_DSR_ID = Integer.valueOf(6);
  
  public static Integer PERSONAL_LOAN_DSR_ID = Integer.valueOf(7);
  
  public static Integer EDUCATION_LOAN_DSR_ID = Integer.valueOf(8);
  
  public static Integer APP_LOAN_TRACK_ID = Integer.valueOf(10);
  
  public static Integer AGRI_LOAN_ID = Integer.valueOf(15);
  
  public static Integer AGRI_LOAN_DSR_ID = Integer.valueOf(16);
  
  public static Integer CREDIT_CARD_ID = Integer.valueOf(17);
  
  public static Integer CREDIT_CARD_DSR_ID = Integer.valueOf(18);
  
  public static Integer APP_PL_TYPE_PERSONAL = Integer.valueOf(1);
  
  public static Integer APP_PL_TYPE_PENSION = Integer.valueOf(2);
  
  public static Integer APP_PL_TYPE_GOLD = Integer.valueOf(27);
  
  public static Integer APP_EL_TYPE_ID_NORMAL = Integer.valueOf(0);
  
  public static Integer APP_EL_TYPE_ID_SCHOLAR = Integer.valueOf(1);
  
  public static Integer APP_EL_TYPE_ID_EDVANTAGE = Integer.valueOf(2);
  
  public static Integer APP_EL_TYPE_ID_TAKE_OVER = Integer.valueOf(3);
 
  public static Integer CALLBACK_ID = Integer.valueOf(18);
  
  public static Integer CALLBACK_MICROSITE_ID = Integer.valueOf(19);
  
  public static Integer LEAD_SOURCE_ID = Integer.valueOf(1);
  
  public static Integer LEAD_INTERMEDIARY_ID = Integer.valueOf(2);
  
  public static Integer LEAD_DATA_SOURCE_ID_WEB_LEAD = Integer.valueOf(1);
  
  public static Integer LEAD_DATA_SOURCE_ID_UPLOAD = Integer.valueOf(2);
  
  public static Integer LEAD_DATA_SOURCE_ID_DIRECT = Integer.valueOf(3);
  
  public static Integer LEAD_DATA_SOURCE_ID_CALL_BACK = Integer.valueOf(4);
  
  public static Integer LEAD_FULFILLMENT_GROUP_ID = Integer.valueOf(5);
  
  public static Integer LEAD_DATA_SOURCE_ID_MOBILE_APP = Integer.valueOf(7);
  
  public static Integer LEAD_DATA_SOURCE_ID_MOBILE_APP_MICROSITE = Integer.valueOf(8);
  
  public static Integer LEAD_BANK_ID = Integer.valueOf(44);
  
  public static Integer LEAD_STATUS_ID = Integer.valueOf(105);
  
  public static int DATA_TYPE_INTEGER = 1;
  
  public static int DATA_TYPE_STRING = 2;
  
  public static int DATA_TYPE_DOUBLE = 3;
  
  public static int DATA_TYPE_MOBILE = 4;
  
  public static int DATA_TYPE_DATE = 7;
  
  public static int DATA_TYPE_EMAIL = 8;
  
  public static int DATA_TYPE_PAN = 11;
  
  public static int DATA_TYPE_PIN = 12;
  
  public static int DATA_TYPE_NRIMOBILE = 13;
  
  public static int DATA_TYPE_AADHAAR_NUMBER = 15;
  
  public static int DATA_TYPE_ACCOUNT_NUMBER = 16;
  
  public static int DATA_TYPE_BOOLEAN = 10;
  
  public static byte FETCH_TYPE_SQL = 1;
  
  public static byte FETCH_TYPE_JSON = 2;
  
  public static byte FETCH_TYPE_STATIC = 3;
  
  public static Integer LOAN_CATEGORY_ID = Integer.valueOf(55);
  
  public static Integer OTHER_USER_ID = Integer.valueOf(9999999);
  
  public static String OTHER_ID = "9999999";
  
  public static Integer OTHER_ID_INTEGER = Integer.valueOf(9999999);
  
  public static String OTHER_VALUE = "Others";
  
  public static String BRANCH_CODE = "00000";
  
  public static String CALL_LOGS_MESSAGE_STATE59 = "Not Eligible";
  
  public static String CALL_LOGS_MESSAGE_STATE43 = "OTP not done";
  
  public static String CALL_LOGS_MESSAGE_STATE1 = "Partial";
  
  public static String CALL_LOGS_MESSAGE_STATE1CB = "Partial : Call Back";
  
  public static String CALL_LOGS_MESSAGE_STATE73 = "Partial confirmed";
  
  public static String CALL_LOGS_MESSAGE_STATE19 = "AIP Rejected";
  
  public static String CALL_LOGS_MESSAGE_STATE147 = "AIP Approved/Referred";
  
  public static String CALL_LOGS_MESSAGE_STATE29 = "Appointment Scheduled";
  
  public static String CALL_LOGS_MESSAGE_STATE6 = "Duplicate";
  
  public static String CALL_LOGS_MESSAGE_STATE161 = "Pre OTP Ineligible";
  
  public static String CALL_LOGS_MESSAGE_STATE160 = "Not Eligible";
  
  public static String CALL_LOGS_MESSAGE_STATE22 = "Not Eligible";
  
  public static String CALL_LOGS_MESSAGE_STATE32 = "Appointment Rescheduled";
  
  public static String CALL_LOGS_MESSAGE_STATE87 = "Follow-up";
  
  public static String CALL_LOGS_MESSAGE_STATE94 = "Document pending";
  
  public static String CALL_LOGS_MESSAGE_STATE122 = "Docs Collected";
  
  public static String CALL_LOGS_MESSAGE_STATE164 = "AIP Rejected";
  
  public static String CALL_LOGS_MESSAGE_STATE165 = "AIP Rejected";
  
  public static String CALL_LOGS_MESSAGE_STATE166 = "AIP Rejected";
  
  public static String CALL_LOGS_MESSAGE_STATE167 = "CBS-Data captured";
  
  public static String CALL_LOGS_MESSAGE_STATE_EKYC_SAVED = "EKYC service called";
  
  public static String CALL_LOGS_MESSAGE_STATE_DATA_SAVED = "Data saved";
  
  public static String CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS = "Applicant PAN - SUCCESS";
  
  public static String CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL = "Applicant PAN - FAIL";
  
  public static String CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_SUCCESS = "Co-applicant PAN - SUCCESS";
  
  public static String CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_FAIL = "Co-applicant PAN - FAIL";
  
  public static String CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_147 = "RSM : APPROVED/REFFERED";
  
  public static String CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_19 = "RSM : REJECT";
  
  public static String CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON = "RSM : RE-INITIATE";
  
  public static String CALL_LOGS_MESSAGE_STATE2 = "Fresh no PAN";
  
  public static String CALL_LOGS_MESSAGE_STATE100 = "Fresh";
  
  public static String CALL_LOGS_MESSAGE_STATE101 = "Fresh Lead (appt tentative)";
  
  public static String CALL_LOGS_MESSAGE_STATE104 = "Application submitted";
  
  public static String CALL_LOGS_MESSAGE_STATE124 = "OTP not done";
  
  public static String CALL_LOGS_MESSAGE_STATE105 = "New lead";
  
  public static String CALL_LOGS_MESSAGE_STATE141 = "Lead Partial Entry";
  
  public static String CALL_LOGS_MESSAGE_STATE153 = "Lead Partial Entry Confirmed";
  
  public static String CALL_LOGS_MESSAGE_STATE152 = "Lead Not Eligible";
  
  public static String CALL_LOGS_MESSAGE_STATE169 = "EKYC-Biometric captured";
  
  public static String CALL_LOGS_MESSAGE_STATE168 = "CBS OTP verified";
  
  public static String CALL_LOGS_MESSAGE_STATE170 = "Prima facie ineligible";
  
  public static String CALL_LOGS_MESSAGE_STATE171 = "Mobile OTP done";
  
  public static String CALL_LOGS_MESSAGE_STATE172 = "CBS OTP not verified";
  
  public static String CALL_LOGS_MESSAGE_STATE173 = "OTP verified - CBS";
  
  public static String CALL_LOGS_MESSAGE_STATE174 = "Application submitted";
  
  public static String CALL_LOGS_MESSAGE_STATE175 = "Lead CBS OTP not verified";
  
  public static String CALL_LOGS_MESSAGE_STATE176 = "Lead CBS OTP verified";
  
  public static String CALL_LOGS_MESSAGE_STATE177 = "OTP verified";
  
  public static String CALL_LOGS_MESSAGE_STATE178 = "Lead OTP Verified";
  
  public static String CALL_LOGS_MESSAGE_STATE347 = "Post OTP Ineligible";
  
  public static int CALL_LOGS_MESSAGE_STATE59_ID = 59;
  
  public static int CALL_LOGS_MESSAGE_STATE43_ID = 43;
  
  public static int CALL_LOGS_MESSAGE_STATE1_ID = 1;
  
  public static int CALL_LOGS_MESSAGE_STATE73_ID = 73;
  
  public static int CALL_LOGS_MESSAGE_STATE19_ID = 19;
  
  public static int CALL_LOGS_MESSAGE_STATE147_ID = 147;
  
  public static int CALL_LOGS_MESSAGE_STATE29_ID = 29;
  
  public static int CALL_LOGS_MESSAGE_STATE6_ID = 6;
  
  public static int CALL_LOGS_MESSAGE_STATE161_ID = 161;
  
  public static int CALL_LOGS_MESSAGE_STATE2_ID = 2;
  
  public static int CALL_LOGS_MESSAGE_STATE100_ID = 100;
  
  public static int CALL_LOGS_MESSAGE_STATE101_ID = 101;
  
  public static int CALL_LOGS_MESSAGE_STATE104_ID = 104;
  
  public static int CALL_LOGS_MESSAGE_STATE124_ID = 124;
  
  public static int CALL_LOGS_MESSAGE_STATE105_ID = 105;
  
  public static int CALL_LOGS_MESSAGE_STATE141_ID = 141;
  
  public static int CALL_LOGS_MESSAGE_STATE143_ID = 143;
  
  public static int CALL_LOGS_MESSAGE_STATE153_ID = 153;
  
  public static int CALL_LOGS_MESSAGE_STATE152_ID = 152;
  
  public static int CALL_LOGS_MESSAGE_STATE169_ID = 169;
  
  public static int CALL_LOGS_MESSAGE_STATE22_ID = 22;
  
  public static int CALL_LOGS_MESSAGE_STATE160_ID = 160;
  
  public static int CALL_LOGS_MESSAGE_STATE164_ID = 164;
  
  public static int CALL_LOGS_MESSAGE_STATE165_ID = 165;
  
  public static int CALL_LOGS_MESSAGE_STATE166_ID = 166;
  
  public static int CALL_LOGS_MESSAGE_STATE167_ID = 167;
  
  public static int CALL_LOGS_MESSAGE_STATE168_ID = 168;
  
  public static int CALL_LOGS_MESSAGE_STATE170_ID = 170;
  
  public static int CALL_LOGS_MESSAGE_STATE171_ID = 171;
  
  public static int CALL_LOGS_MESSAGE_STATE172_ID = 172;
  
  public static int CALL_LOGS_MESSAGE_STATE173_ID = 173;
  
  public static int CALL_LOGS_MESSAGE_STATE174_ID = 174;
  
  public static int CALL_LOGS_MESSAGE_STATE175_ID = 175;
  
  public static int CALL_LOGS_MESSAGE_STATE176_ID = 176;
  
  public static int CALL_LOGS_MESSAGE_STATE177_ID = 177;
  
  public static int CALL_LOGS_MESSAGE_STATE178_ID = 178;
  
  public static int CALL_LOGS_MESSAGE_STATE347_ID = 347;
  
  public static int CALL_LOGS_MESSAGE_STATE384_ID = 384;
  
  public static int CALL_LOGS_MESSAGE_STATE32_ID = 32;
  
  public static int CALL_LOGS_MESSAGE_STATE87_ID = 87;
  
  public static int CALL_LOGS_MESSAGE_STATE94_ID = 94;
  
  public static int CALL_LOGS_MESSAGE_STATE122_ID = 122;
  
  public static String COPY_PERMISSION_COMMAND = null;
  
  public static String FILE_PERMISSION_COMMAND = null;
  
  public static String UPLOAD_INITIAL = null;
  
  public static String UPLOAD_PATH_LIVE = null;
  
  public static byte UPLOAD_DOCGRAPH = 1;
  
  public static byte UPLOAD_IDENTITYPROOF = 2;
  
  public static byte UPLOAD_RESIDENSEPROOF = 3;
  
  public static byte UPLOAD_INCOMEPROOF = 4;
  
  public static byte UPLOAD_EMPLOYMENTPROOF = 5;
  
  public static String IMAGE_FILE_EXTENSION = ".jpg";
  
  public static Double LOAN_ELIGIBILITY_MULTIPLER_FACTOR = Double.valueOf(100000.0D);
  
  public static Integer LOAN_TENURE_MULTIPLER_FACTOR = Integer.valueOf(12);
  
  public static Integer MESSAGE_TYPE_EMAIL = Integer.valueOf(1);
  
  public static Integer MESSAGE_TYPE_SMS = Integer.valueOf(2);
  
  public static Integer MESSAGE_TYPE_SMS_CONSENT = Integer.valueOf(3);
  
  public static Integer MESSAGE_TYPE_FLIPKART_OTP = Integer.valueOf(6);
  
  public static Integer MESSAGE_TYPE_FLIPKART_ELIGIBILITY = Integer.valueOf(7);
  
  public static Integer REQUEST_INDEX_DOCUMENT_SCHEDULE = Integer.valueOf(18);
  
  public static Integer REQUEST_INDEX_APPLICATION_SUBMIT = Integer.valueOf(17);
  
  public static Integer REQUEST_INDEX_SAVE_QUOTE = Integer.valueOf(6);
  
  public static Integer DUPLICATION_MULTIFICATION_FACTOR = Integer.valueOf(86400000);
  
  public static String APP_DUPLICATION_CHECK = null;
  
  public static String APP_DUPLICATION_TIME_PERIOD = null;
  
  public static String APP_DEDUPLICATION_MESSAGE = null;
  
  public static String APP_DUPLICATION_TIME_PERIOD_CARDS = null;
  
  public static String INQUIRY_DUPLICATION_TIME_PERIOD_CARDS = null;
  
  public static String INQUIRY_DUPLICATION_CHECK = null;
  
  public static String INQUIRY_DUPLICATION_TIME_PERIOD = null;
  
  public static String INQUIRY_DEDUPLICATION_MESSAGE = null;
  
  public static String BANK_FULL_NAME = null;
  
  public static String FIRST_EMAIL_PART = "<html><head><title>" + BANK_FULL_NAME + "</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>" + 
    "<style type='text/css'>" + 
    "@media only screen and (max-width:600px){table.body{width:600px!important; padding:0 4px;}}" + 
    "@media only screen and (max-width:320px){table.body{width:320px!important; padding:0 4px;}}" + 
    "@media only screen and (max-width:480px){table.body{width:480px!important; padding:0 4px;}}" + 
    "</style></head><body>";
  
  public static String FLEXI_TEMPLATE = "<table align='' border='0' cellpadding='0' cellspacing='0' width='100%' ><tbody><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-top:none; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI1_DURATION]</td><td style='border:1px solid #aaa;border-left:0px; border-top:none;border-right:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; ' >[EMI1_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '> [EMI2_DURATION]</td><td style='border:1px solid #aaa; border-left:0px;border-right:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img src='[BASE_URL]rupee-icon.jpg' style='vertical-align:bottom' />[EMI2_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI3_DURATION]</td><td style='border:1px solid #aaa;border-right:0px;border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img style='vertical-align:bottom' src='[BASE_URL]rupee-icon.jpg'>[EMI3_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; border-bottom:none; padding:4px 4px 4px 8px; color:#333333; text-align:left; font-size:12px; '>[EMI4_DURATION]</td><td style='border:1px solid #aaa; border-right:0px;border-bottom:none;border-left:0px; padding:4px 4px 4px 8px; color:#333333; text-align:left; font-size:12px; '><img style='vertical-align:bottom' src='[BASE_URL]rupee-icon.jpg'>[EMI4_AMOUNT]</td></tr></tbody></table>";
  
  public static String THIRD_EMAIL_PART = "</body></html>";
  
  public static String AUTO_DISPOSE_TIME = "2014-11-13 23:59:59";
  
  public static boolean IS_AUTO_DISPOSE = true;
  
  public static boolean HL_CAPTCHA_BY_PASS = false;
  
  public static boolean EL_CAPTCHA_BY_PASS = false;
  
  public static boolean AL_CAPTCHA_BY_PASS = false;
  
  public static boolean AGRI_CAPTCHA_BY_PASS = false;
  
  public static boolean CREDIT_CAPTCHA_BY_PASS = false;
  
  public static boolean AUTO_LOAN_OFFER_ENABLE = false;
  
  public static boolean BUREAULINK_BYPASS_HL = false;
  
  public static boolean BUREAULINK_BYPASS_PL = false;
  
  public static boolean BUREAULINK_BYPASS_AL = false;
  
  public static boolean BUREAULINK_BYPASS_EL = false;
  
  public static String BUREAU = null;
  
  public static String COUNTRY_CODE_INDIA = "91";
  
  public static String SAVE_QUOTE_REDIRECTION = null;
  
  public static String PDF_SESSION_TIMEOUT_REDIRECTION = null;
  
  public static String ALREADY_LOGGEDIN_REDIRECTION = null;
  
  public static String BRANCH_NOT_FOUND_MSG = "Sorry, we did not find any branch for your current selection of city/district. Please select a branch from your nearby city/district or visit any of our branches to apply.";
  
  public static String[] month = new String[] { 
      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
      "Nov", "Dec" };
  
  public static Map<String, String> PICKUP_TIME_LIST = SbiUtil.generatePickupTimeList();
  
  public static String BANK_IMAGE_FOLDER = null;
  
  public static String BANK_CSS_FOLDER = null;
  
  public static String BANK_JS_FOLDER = null;
  
  public static int BANK_ID_SBI = 1;
  
  public static int BANK_ID_SBBJ = 2;
  
  public static int BANK_ID_SBH = 3;
  
  public static int BANK_ID_SBM = 4;
  
  public static int BANK_ID_SBP = 5;
  
  public static int BANK_ID_SBT = 6;
  
  public static Integer BANK_ID = null;
  
  public static String BANK_NAME = null;
  
  public static String BANK_URL = null;
  
  public static String BANK_FULL_URL = null;
  
  public static String BANK_COPYRIGHT = null;
  
  public static String OTP_SUBJECT_LINE = null;
  
  public static String HOME_LOAN_PRODUCT_NAME = null;
  
  public static String AUTO_LOAN_PRODUCT_NAME = null;
  
  public static String PERSONAL_LOAN_PRODUCT_NAME = null;
  
  public static String EDUCATION_LOAN_PRODUCT_NAME = null;
  
  public static Integer FLEXI_PAY_PRODUCT_ID = null;
  
  public static Integer SBI_PRIVILEGE_PRODUCT_ID = null;
  
  public static Integer SBI_SHAURYA_PRODUCT_ID = null;
  
  public static Integer XPRESS_CREDIT_IT_PRODUCT_ID = null;
  
  public static Integer XPRESS_CREDIT_ELITE_PRODUCT_ID = null;
  
  public static Integer XPRESS_CREDIT_NPE_PRODUCT_ID = null;
  
  public static Integer REGULAR_PENSION = null;
  
  public static Integer JAI_JAWAN_PENSION_LOAN = null;
  
  public static Integer FAMILY_PENSION = null;
  
  public static String BANK_ONLINE_URL = null;
  
  public static String BANK_FINDER_URL = null;
  
  public static String BANK_SUPPORT_EMAIL = null;
  
  public static String EKYC_CHANNEL = "OC";
  
  public static boolean EKYC_BIOMETRIC_DEDUPE_BYPASS = false;
  
  public static String CBS_LOAN_TYPE_HOME_TOP_UP = "HL";
  
  public static String CBS_LOAN_TYPE_AUTO_LOAN = "HL";
  
  public static String CBS_LOAN_TYPE_XPRESS_CREDIT_TOP_UP = "XS";
  
  public static String CBS_LOAN_TYPE_EDUCATION_TOP_UP = "ED";
  
  public static String CBS_LOAN_TYPE_AGRI_LOAN = "AGL";
  
  public static String CBS_LOAN_TYPE_CREDIT_CARD = "SCC";
  
  public static Integer CBS_IRAC_STATUS = Integer.valueOf(4);
  
  public static String CBS_BANK_CODE = "0";
  
  public static String CBS_LOAN_TYPE = "CI";
  
  public static Boolean CBS_OTP_BYPASS = Boolean.valueOf(false);
  
  public static String OCAS_KEYWORD = "OCASY";
  
  public static String SERVER_SEQ = null;
  
  public static String NO_LOAN_OFFER_EL = "Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.";
  
  public static String SBI_PRIVILEGE_SHAURYA_EMAIL_TEMPLATE = "<table align='' border='0' cellpadding='0' cellspacing='0' width='100%' ><tbody><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-top:none; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI1_DURATION]</td><td style='border:1px solid #aaa;border-left:0px; border-top:none;border-right:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; ' >[EMI1_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '> [EMI2_DURATION]</td><td style='border:1px solid #aaa; border-left:0px;border-right:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img src='[BASE_URL]rupee-icon.jpg' style='vertical-align:bottom' />[EMI2_AMOUNT]</td></tr></tbody></table>";
  
  public static String NO_LOAN_OFFER_EL_NEW = "Dear customer, based on the inputs provided, you are not eligible under this scheme. Kindly login to <a href='https://www.vidyalakshmi.co.in/Students/' target='blank'>www.vidyalakshmi.co.in/Students</a> for applying online and select SBI as preferred bank.";
  
  public static String COMMON_SORRY_MSG = "Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.";
  
  public static String AUTH_KEY = null;
  
  public static String APP_MOBILE_HASH_KEY = null;
  
  public static String SBI_TATA_CONSTANT = "SBTA";
  
  public static String DIP_LOCAL = null;
  
  public static String DPT_LOCAL = null;
  
  public static String CALLBACKSERVICEURL = null;
  
  public static boolean CLICK_TO_CALL_BYPASS = false;
  
  public static Boolean IS_PROXY_REQUIRED = Boolean.valueOf(false);
  
  public static String PROXY_HOST = null;
  
  public static String PROXY_PORT = null;
  
  public static String PROXY_USER = null;
  
  public static String PROXY_PASSWORD = null;
  
  public static String TNC_URL_HL = null;
  
  public static String TNC_URL_AL = null;
  
  public static String TNC_URL_PL = null;
  
  public static String SCC_PDF_GENRATION_LOCATION = null;
  
  public static String SCC_LOAN_ACTION = null;
  
  public static String SCC_LOAN_ACTION_DSR = null;
  
  public static String SCC_LOAN_TITLE = null;
  
  public static String SCC_LOAN_KEYWORDS = null;
  
  public static String SCC_LOAN_DESCRIPTION = null;
  
  public static String INTIAL_STRING_SCC = null;
  
  public static String SOURCE_STRING_SCC = null;
  
  public static String PDF_TEMPLATE_BASE_PATH = null;
  
  public static boolean SCC_WEBSERVICE_BYPASS = false;
  
  public static String MAGIC_SALES_INITIAL = null;
  
  public static String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  
  public static String CRM_NEW_WEBSERVICE = null;
  
  public static String CRM_NEW_API_TOKEN = null;
  
  public static String CRM_NEW_API_ExpiresOn = null;
  
  public static String CRM_NEW_AES_KEY = null;
  
  public static String CRM_NEW_AES_IV = null;
  
  public static String CRM_NEW_API_Username = null;
  
  public static String CRM_NEW_API_Password = null;
  
  public static Integer PHOTOGRAPH_DOC = Integer.valueOf(1);
  
  public static Integer IDENTITY_PROOF_DOC = Integer.valueOf(2);
  
  public static Integer RESIDANCE_PROOF_DOC = Integer.valueOf(3);
  
  public static Integer INCOME_PROOF_DOC = Integer.valueOf(4);
  
  public static Integer EMPLOYMENT_PROOF_DOC = Integer.valueOf(5);
  
  public static Integer SIGNATURE_DOC = Integer.valueOf(6);
  
  public static String BANK_IMAGE_FOLDER_NEWUI = null;
  
  public static String BANK_CSS_FOLDER_NEWUI = null;
  
  public static String BANK_JS_FOLDER_NEWUI = null;
  
  public static String SERVER_ID = "1";
  
  public static boolean CRM_BYPASS = false;
  
  public static boolean CBS_DEDUPE_BYPASS = false;
  
  public static String IVRS_SALT = null;
  
  public static boolean PL_CAPTCHA_BY_PASS = false;
  
  public static String CBS_LOAN_TYPE_PENSION = "PNL";
  
  public static Integer EMPLOYMENT_TYPE_PENSIONERS = Integer.valueOf(21);
  
  public static String URL_QUTOE_TOKEN_PNL = "PNLQuoteToken";
  
  public static String PENSION_LOAN_ACTION_DSR = "pension-loan-dsr";
  
  public static String PENSION_LOAN_ACTION = null;
  
  public static String SCHEMENAME = null;
  
  public static String EDUCATION_LOAN_TAKEOVER_ACTION = null;
  
  public static String URL_QUTOE_TOKEN_EL_TAKE_OVER = "ELTakeOverQuoteToken";
  
  public static String EDUCATION_TAKEOVER_LOAN = "EDUCATION TAKEOVER LOAN";
  
  public static Integer PENSION_LOAN_PURPOSE_ID = Integer.valueOf(23);
  
  public static Integer EDUCATION_TAKEOVER_LOAN_PURPOSE_ID = Integer.valueOf(24);
  
  public static String SORRY_FOR_INCONVENIENCE = null;
  
  public static String SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT = null;
  
  public static Integer APP_RETRY_DUPLICATION_TIME_PERIOD = null;
  
  public static String APP_MAXIMUM_OTP_REQUEST_MSG = null;
  
  public static Integer APP_MAXIMUM_OTP_REQUEST_COUNT = null;
  
  public static String APP_MAXIMUM_OTP_ATTEMPT_MSG = null;
  
  public static Integer APP_MAXIMUM_OTP_ATTEMPT_COUNT = null;
  
  public static String APP_MAXIMUM_RESEND_OTP_REQUEST_MSG = null;
  
  public static String APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG = null;
  
  public static Integer TOKEN_REQUEST_TIME_PERIOD = null;
  
  public static Integer BHL_PRODUCT_ID = null;
  
  public static Double BHL_SPREAD_RATE = null;
  
  public static CharSequence CHAR_SEQ = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
  
  public static Integer APP_EL_TYPE_ID_BIDYALAKHMI = Integer.valueOf(4);
  
  public static Integer EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID = Integer.valueOf(26);
  
  public static boolean IS_ENGINE_OBF = false;
  
  public static boolean RAC_SETTING_ENABLE = false;
  
  public static String RAC_DTY = null;
  
  public static String RAC_DN_PERSONAL = null;
  
  public static String RAC_DIP = null;
  
  public static String RAC_DPT = null;
  
  public static String RAC_DN = null;
  
  public static boolean REFERENCE_NUMBER_BASED_ON_CLUSTER = false;
  
  public static boolean CALLBACK_SMS_CONSENT = false;
  
  public static boolean NONCBS_SMS_CONSENT = false;
  
  public static boolean PL_TYPE_GOLD = true;
  
  public static boolean PL_TYPE_CVE = true;
  
  public static String INTIAL_STRING_CVE = null;
  
  public static String SOURCE_STRING_CVE = null;
  
  public static Integer EXTERNAL_SERVICE_TIMED_OUT = null;
  
  public static Integer APP_MAXIMUM_EMAIL_ATTEMPT_COUNT = null;
  
  public static String APP_MAXIMUM_EMAIL_ATTEMPT_MSG = null;
  
  public static Integer PGL_PRODUCT_ID = null;
  
  public static String PMAY_SCHEME1 = null;
  
  public static String PMAY_SCHEME2 = null;
  
  public static String PMAY_SCHEME3 = null;
  
  public static String PMAY_SCHEME4 = null;
  
  public static Boolean SMS_SERVICE_BYPASS = null;
  
  public static String UI_TYPE = null;
  
  public static Boolean SECRET_KEY_BYPASS = Boolean.valueOf(false);
  
  public static String GOLD_LOAN_ACTION = null;
  
  public static Boolean CBS_SI_PAN_VALIDATE_API_BY_PASS = Boolean.valueOf(false);
  
  public static String CBS_SI_PAN_VALIDATE_API_URL = null;
  
  public static String CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH = null;
  
  public static String CBS_SI_PAN_AES_PRIVATE_KEY = null;
  
  public static String CRM_REST_RSA_ENCRYPTION_CERTIFICATE_PATH = null;
  
  public static String CRM_REST_AES_PRIVATE_KEY = null;
  
  public static String CRM_REST_RSA_KEYSTORE_PATH = null;
  
  public static String CRM_REST_RSA_KEYSTORE_PASS = null;
  
  public static String CRM_REST_RSA_KEYSTORE_ALIAS = null;
  
  public static Integer CVE_ID = Integer.valueOf(24);
  
  public static String CVE_ACTION = null;
  
  public static Integer APP_PL_TYPE_CVE = Integer.valueOf(130);
  
  public static Integer APP_PL_TYPE_CVE_REVOKE = Integer.valueOf(130);
  
  public static String CRM_REST_API_TOKEN_URL_CVE = null;
  
  public static String CRM_REST_API_URL_CVE = null;
  
  public static String SMS_APPLIANCE_BASED_URL = null;
  
  public static String SMS_APPLIANCE_BASED_MESSAGE = null;
  
  public static String SMS_DOM_APPLIANCE_BASED_USERNAME = null;
  
  public static String SMS_DOM_APPLIANCE_BASED_PASSWORD = null;
  
  public static String SMS_INTER_APPLIANCE_BASED_USERNAME = null;
  
  public static String SMS_INTER_APPLIANCE_BASED_PASSWORD = null;
  
  public static String RSA_KEYSTORE_PATH = null;
  
  public static String RSA_KEYSTORE_PASS = null;
  
  public static String RSA_KEYSTORE_ALIAS = null;
  
  public static String CBS_ACCOUNT_LEVEL_API_URL = null;
  
  public static String CBS_EDU_LOAN_ACCOUNT_API_URL = null;
  
  public static String EDMS_CERTIFICATE = null;
  
  public static String EDMS_PRIVATE_KEY = null;
  
  public static String EDMS_URL = null;
  
  public static String UPLOAD_PATH_LIVE_EDMS = null;
  
  public static String CVE_TITLE = null;
  
  public static String CVE_KEYWORDS = null;
  
  public static String CVE_DESCRIPTION = null;
  
  public static String CRM_REST_API_URL = null;
  
  public static String CASE_RESPONSE = null;
  
  public static String CRM_REST_API_URL_CVE_CONSENT = null;
  
  public static String CRM_TOKEN_REQUEST_FIELD1 = null;
  
  public static String CRM_TOKEN_REQUEST_FIELD2 = null;
  
  public static String CRM_FETCH_LEAD_STATUS_URL = null;

  public static String SMS_STRING_INDIAN = "pno=MOBILE_CODE&msgtxt=MESSAGE_TEXT&msgtype=S&intflag=1";
  public static String SMS_STRING_NRI = "pno=MOBILE_CODE&msgtxt=MESSAGE_TEXT&msgtype=S&intflag=2";
  
  public static String UPLOADLEAD_SECRETKEY = null;
  
  public static String SBIUTIL_PASSPHRASE = null;
  
  public static String SBI_HOMELOANS = null;
  
  public static String SBI_SSO = null;
  
  public static String SBI_ONLINE_PROD = null;
  
  public static String IP_PR = null;
  
  public static String IP_DR = null;
  
  public static String BANK_ONLINE_NEW_DOMAIN_URL = null;
  
  public static String BANK_ONLINE_OLD_DOMAIN = null;
  
  static {
	  FileInputStream configStream = null;
	  FileInputStream constantStream = null;
    try {
      configStream = new FileInputStream(System.getProperty("catalina.home").replaceAll("\\\\","/")+"/ocas_configuration/config.properties");
      constantStream = new FileInputStream(System.getProperty("catalina.home").replaceAll("\\\\","/")+"/ocas_configuration/Constants.properties");
	  ResourceBundle bundleConfig = new PropertyResourceBundle(configStream);
	  ResourceBundle bundle = new PropertyResourceBundle(constantStream);
      DIP = bundleConfig.getString("jdbc.ip");
      DPT = bundleConfig.getString("jdbc.port");
      DN = bundleConfig.getString("jdbc.name");
      DU = bundleConfig.getString("jdbc.username");
      DP = bundleConfig.getString("jdbc.password");
      USER_NAME = bundleConfig.getString("sendMail.userName");
      PASSWORD = bundleConfig.getString("sendMail.password");
      SMTP_SERVER_HOST_NAME = bundleConfig.getString("sendMail.smtpServerHostName");
      SMTP_PORT = bundleConfig.getString("sendMail.smtpPort");
      AUTH_REQUIRED = bundleConfig.getString("sendMail.authenticationReqd");
      DEBUG = (new Boolean(bundleConfig.getString("sendMail.debug"))).booleanValue();
      FROM_ADDRESS = bundleConfig.getString("sendMail.fromAddress");
      STAFF_ADDRESS = bundleConfig.getString("sendMail.staffAddress");
      ALTERNATE_ADDRESS = bundleConfig.getString("sendMail.alternateAddress");
      MODE = bundleConfig.getString("sendMail.mode");
      DEPLOYMENT_MODE = bundleConfig.getString("settings.deploymentMode");
      DEPLOYMENT_ENVIRONMENT = bundleConfig.getString("settings.deploymentEnvironment");
      SCRIPT_VERSION = bundleConfig.getString("settings.scriptVersion");
      SCRIPT_COMPRESSION = bundleConfig.getString("settings.scriptCompression");
      HL_PRICING_ENGINE_URL = bundle.getString("HL_PRICING_ENGINE_URL");
      AL_PRICING_ENGINE_URL = bundle.getString("AL_PRICING_ENGINE_URL");
      PL_PRICING_ENGINE_URL = bundle.getString("PL_PRICING_ENGINE_URL");
      EL_PRICING_ENGINE_URL = bundle.getString("EL_PRICING_ENGINE_URL");
      SCC_PRICING_ENGINE_URL = bundle.getString("SCC_PRICING_ENGINE_URL");
      HL_PDF_GENRATION_URL = bundle.getString("HL_PDF_GENRATION_URL");
      AL_PDF_GENRATION_URL = bundle.getString("AL_PDF_GENRATION_URL");
      EL_PDF_GENRATION_URL = bundle.getString("EL_PDF_GENRATION_URL");
      PL_PDF_GENRATION_URL = bundle.getString("PL_PDF_GENRATION_URL");
      AGL_PDF_GENRATION_URL = bundle.getString("AGL_PDF_GENRATION_URL");
      HL_RSM_ENGINE_URL = bundle.getString("HL_RSM_ENGINE_URL");
      AL_RSM_ENGINE_URL = bundle.getString("AL_RSM_ENGINE_URL");
      PL_RSM_ENGINE_URL = bundle.getString("PL_RSM_ENGINE_URL");
      EL_RSM_ENGINE_URL = bundle.getString("EL_RSM_ENGINE_URL");
      CBS_CIF_LEVEL_API_URL = bundle.getString("CBS_CIF_LEVEL_API_URL");
      CBS_LOAN_ACCOUNT_API_URL = bundle.getString("CBS_LOAN_ACCOUNT_API_URL");
      APP_APP_SUB_TYPE_ID_NORMAL = Integer.valueOf(0);
      APP_APP_SUB_TYPE_ID_EKYC = Integer.valueOf(1);
      APP_APP_SUB_TYPE_ID_CBS = Integer.valueOf(2);
      CBS_IRAC_STATUS_BYPASS = (new Boolean(bundle.getString("CBS_IRAC_STATUS_BYPASS"))).booleanValue();
      CBS_SERVICE_CALL_BYPASS = (new Boolean(bundle.getString("CBS_SERVICE_CALL_BYPASS"))).booleanValue();
      PDF_GENRATION_BASE_PATH = bundle.getString("PDF_GENRATION_BASE_PATH");
      PDF_DOWNLOAD_BASE_PATH = bundle.getString("PDF_DOWNLOAD_BASE_PATH");
      HL_PDF_GENRATION_LOCATION = bundle.getString("HL_PDF_GENRATION_LOCATION");
      AL_PDF_GENRATION_LOCATION = bundle.getString("AL_PDF_GENRATION_LOCATION");
      PL_PDF_GENRATION_LOCATION = bundle.getString("PL_PDF_GENRATION_LOCATION");
      EL_PDF_GENRATION_LOCATION = bundle.getString("EL_PDF_GENRATION_LOCATION");
      AGL_PDF_GENRATION_LOCATION = bundle.getString("AGL_PDF_GENRATION_LOCATION");
      SBI_TATA_PDF_GENRATION_LOCATION = bundle.getString("SBI_TATA_PDF_GENRATION_LOCATION");
      SBI_TATA_PDF_GENERATION_URL = bundle.getString("SBI_TATA_PDF_GENERATION_URL");
      HOME_ACTION = bundle.getString("HOME_ACTION");
      HOME_LOAN_ACTION = bundle.getString("HOME_LOAN_ACTION");
      HOME_TOP_UP_LOAN_ACTION = bundle.getString("HOME_TOP_UP_LOAN_ACTION");
      HOME_TOP_UP_LOAN_ACTION_DSR = "home-top-up-loan-dsr";
      AUTO_LOAN_ACTION = bundle.getString("AUTO_LOAN_ACTION");
      EDUCATION_LOAN_ACTION = bundle.getString("EDUCATION_LOAN_ACTION");
      SCHOLAR_LOAN_ACTION = bundle.getString("SCHOLAR_LOAN_ACTION");
      GLOBAL_EDVANTAGE_ACTION = bundle.getString("GLOBAL_EDVANTAGE_ACTION");
      PERSONAL_LOAN_ACTION = bundle.getString("PERSONAL_LOAN_ACTION");
      APPLICATION_STATUS_ACTION = bundle.getString("APPLICATION_STATUS_ACTION");
      AGRI_LOAN_ACTION = bundle.getString("AGRI_LOAN_ACTION");
      HOME_LOAN_ACTION_DSR = bundle.getString("HOME_LOAN_ACTION_DSR");
      AUTO_LOAN_ACTION_DSR = bundle.getString("AUTO_LOAN_ACTION_DSR");
      EDUCATION_LOAN_ACTION_DSR = bundle.getString("EDUCATION_LOAN_ACTION_DSR");
      PERSONAL_LOAN_ACTION_DSR = bundle.getString("PERSONAL_LOAN_ACTION_DSR");
      AGRI_LOAN_ACTION_DSR = bundle.getString("AGRI_LOAN_ACTION_DSR");
      AUTO_DEALER_ID = Integer.valueOf(33);
      HOME_BUILDER_ID = Integer.valueOf(204);
      DATE_FORMAT = "dd-MM-yyyy";
      DATE_FORMAT_WITHOUT_DELIMITER = "ddMMyyyy";
      SENDER_EMAIL = bundle.getString("SENDER_EMAIL");
      ALIAS_EMAIL = bundle.getString("ALIAS_EMAIL");
      APPLICATION_STATUS_TITLE = bundle.getString("APPLICATION_STATUS_TITLE");
      APPLICATION_STATUS_KEYWORDS = bundle.getString("APPLICATION_STATUS_KEYWORDS");
      APPLICATION_STATUS_DESCRIPTION = bundle.getString("APPLICATION_STATUS_DESCRIPTION");
      HOME_TITLE = bundle.getString("HOME_TITLE");
      HOME_KEYWORDS = bundle.getString("HOME_KEYWORDS");
      HOME_DESCRIPTION = bundle.getString("HOME_DESCRIPTION");
      HOME_LOAN_TITLE = bundle.getString("HOME_LOAN_TITLE");
      HOME_LOAN_KEYWORDS = bundle.getString("HOME_LOAN_KEYWORDS");
      HOME_LOAN_DESCRIPTION = bundle.getString("HOME_LOAN_DESCRIPTION");
      AUTO_LOAN_TITLE = bundle.getString("AUTO_LOAN_TITLE");
      AUTO_LOAN_KEYWORDS = bundle.getString("AUTO_LOAN_KEYWORDS");
      AUTO_LOAN_DESCRIPTION = bundle.getString("AUTO_LOAN_DESCRIPTION");
      EDUCATION_LOAN_TITLE = bundle.getString("EDUCATION_LOAN_TITLE");
      EDUCATION_LOAN_KEYWORDS = bundle.getString("EDUCATION_LOAN_KEYWORDS");
      EDUCATION_LOAN_DESCRIPTION = bundle.getString("EDUCATION_LOAN_DESCRIPTION");
      PERSONAL_LOAN_TITLE = bundle.getString("PERSONAL_LOAN_TITLE");
      PERSONAL_LOAN_KEYWORDS = bundle.getString("PERSONAL_LOAN_KEYWORDS");
      PERSONAL_LOAN_DESCRIPTION = bundle.getString("PERSONAL_LOAN_DESCRIPTION");
      AGRI_LOAN_TITLE = bundle.getString("AGRI_LOAN_TITLE");
      AGRI_LOAN_KEYWORDS = bundle.getString("AGRI_LOAN_KEYWORDS");
      AGRI_LOAN_DESCRIPTION = bundle.getString("AGRI_LOAN_DESCRIPTION");
      DUMMY_MOBILE_NO = String.valueOf(bundle.getString("DUMMY_MOBILE_NO")) + ";";
      DUMMY_EMAIL_ID = String.valueOf(bundle.getString("DUMMY_EMAIL_ID")) + ";";
      DUMMY_MOBILE_OTP = "123456";
      DUMMY_EMAIL_OTP = "abc123";
      SERVICE_TAX = new Double(bundle.getString("SERVICE_TAX"));
      INTIAL_STRING_HL = bundle.getString("INTIAL_STRING_HL");
      INTIAL_STRING_AL = bundle.getString("INTIAL_STRING_AL");
      INTIAL_STRING_PL = bundle.getString("INTIAL_STRING_PL");
      INTIAL_STRING_EL = bundle.getString("INTIAL_STRING_EL");
      INTIAL_STRING_AGL = bundle.getString("INTIAL_STRING_AGL");
      SOURCE_STRING_HL = bundle.getString("SOURCE_STRING_HL");
      SOURCE_STRING_AL = bundle.getString("SOURCE_STRING_AL");
      SOURCE_STRING_PL = bundle.getString("SOURCE_STRING_PL");
      SOURCE_STRING_EL = bundle.getString("SOURCE_STRING_EL");
      SOURCE_STRING_AGL = bundle.getString("SOURCE_STRING_AGL");
      CONTACT_NO_1 = bundle.getString("CONTACT_NO_1");
      CONTACT_NO_2 = bundle.getString("CONTACT_NO_2");
      CONTACT_NO_3 = bundle.getString("CONTACT_NO_3");
      MESSAGE_HL_1 = bundle.getString("MESSAGE_HL_1");
      CONTACT_MAIL = bundle.getString("CONTACT_MAIL");
      PROTOCOL = bundle.getString("PROTOCOL");
      PORT = bundle.getString("PORT");
      CONTEXT = bundle.getString("CONTEXT");
      IP_URL_INTERNET = bundle.getString("IP_URL_INTERNET");
      URL_QUTOE_TOKEN_HL = "HLQuoteToken";
      URL_QUTOE_TOKEN_HLTOPUP = "HLTopupQuoteToken";
      URL_QUTOE_TOKEN_AL = "ALQuoteToken";
      URL_QUTOE_TOKEN_EL = "ELQuoteToken";
      URL_QUTOE_TOKEN_PL = "PLQuoteToken";
      URL_QUTOE_TOKEN_AGL = "AGLQuoteToken";
      URL_QUTOE_TOKEN_SCC = "SCCQuoteToken";
      STATUS_INACTIVE = Integer.valueOf(0);
      STATUS_ACTIVE = Integer.valueOf(1);
      HOME_ID = Integer.valueOf(0);
      HOME_LOAN_ID = Integer.valueOf(1);
      HOME_TOP_UP_LOAN_ID = Integer.valueOf(11);
      AUTO_LOAN_ID = Integer.valueOf(2);
      PERSONAL_LOAN_ID = Integer.valueOf(3);
      EDUCATION_LOAN_ID = Integer.valueOf(4);
      EDVANTAGE_LOAN_ID = Integer.valueOf(13);
      SCHOLAR_LOAN_ID = Integer.valueOf(12);
      HOME_LOAN_DSR_ID = Integer.valueOf(5);
      AUTO_LOAN_DSR_ID = Integer.valueOf(6);
      PERSONAL_LOAN_DSR_ID = Integer.valueOf(7);
      EDUCATION_LOAN_DSR_ID = Integer.valueOf(8);
      APP_LOAN_TRACK_ID = Integer.valueOf(10);
      AGRI_LOAN_ID = Integer.valueOf(15);
      AGRI_LOAN_DSR_ID = Integer.valueOf(16);
      CREDIT_CARD_ID = Integer.valueOf(17);
      CREDIT_CARD_DSR_ID = Integer.valueOf(18);
      //INSTANT_LOAN_ID = Integer.valueOf(20);
      APP_PL_TYPE_PERSONAL = Integer.valueOf(1);
      APP_PL_TYPE_PENSION = Integer.valueOf(2);
      APP_EL_TYPE_ID_NORMAL = Integer.valueOf(0);
      APP_EL_TYPE_ID_SCHOLAR = Integer.valueOf(1);
      APP_EL_TYPE_ID_EDVANTAGE = Integer.valueOf(2);
      APP_EL_TYPE_ID_TAKE_OVER = Integer.valueOf(3);
      LEAD_SOURCE_ID = Integer.valueOf(1);
      LEAD_INTERMEDIARY_ID = Integer.valueOf(2);
      LEAD_DATA_SOURCE_ID_WEB_LEAD = Integer.valueOf(1);
      LEAD_DATA_SOURCE_ID_UPLOAD = Integer.valueOf(2);
      LEAD_DATA_SOURCE_ID_DIRECT = Integer.valueOf(3);
      LEAD_DATA_SOURCE_ID_CALL_BACK = Integer.valueOf(4);
      LEAD_FULFILLMENT_GROUP_ID = Integer.valueOf(5);
      LEAD_DATA_SOURCE_ID_MOBILE_APP = Integer.valueOf(7);
      LEAD_BANK_ID = Integer.valueOf(44);
      LEAD_STATUS_ID = Integer.valueOf(105);
      DATA_TYPE_INTEGER = 1;
      DATA_TYPE_STRING = 2;
      DATA_TYPE_DOUBLE = 3;
      DATA_TYPE_MOBILE = 4;
      DATA_TYPE_DATE = 7;
      DATA_TYPE_EMAIL = 8;
      DATA_TYPE_PAN = 11;
      DATA_TYPE_PIN = 12;
      DATA_TYPE_NRIMOBILE = 13;
      DATA_TYPE_AADHAAR_NUMBER = 15;
      DATA_TYPE_ACCOUNT_NUMBER = 16;
      DATA_TYPE_BOOLEAN = 10;
      FETCH_TYPE_SQL = 1;
      FETCH_TYPE_JSON = 2;
      FETCH_TYPE_STATIC = 3;
      LOAN_CATEGORY_ID = Integer.valueOf(55);
      OTHER_USER_ID = Integer.valueOf(9999999);
      OTHER_ID = "9999999";
      OTHER_ID_INTEGER = Integer.valueOf(9999999);
      OTHER_VALUE = "Others";
      BRANCH_CODE = "00000";
      CALL_LOGS_MESSAGE_STATE59 = "Not Eligible";
      CALL_LOGS_MESSAGE_STATE43 = "OTP not done";
      CALL_LOGS_MESSAGE_STATE1 = "Partial";
      CALL_LOGS_MESSAGE_STATE1CB = "Partial : Call Back";
      CALL_LOGS_MESSAGE_STATE73 = "Partial confirmed";
      CALL_LOGS_MESSAGE_STATE19 = "AIP Rejected";
      CALL_LOGS_MESSAGE_STATE147 = "AIP Approved/Referred";
      CALL_LOGS_MESSAGE_STATE29 = "Appointment Scheduled";
      CALL_LOGS_MESSAGE_STATE6 = "Duplicate";
      CALL_LOGS_MESSAGE_STATE161 = "Pre OTP Ineligible";
      CALL_LOGS_MESSAGE_STATE160 = "Not Eligible";
      CALL_LOGS_MESSAGE_STATE22 = "Not Eligible";
      CALL_LOGS_MESSAGE_STATE32 = "Appointment Rescheduled";
      CALL_LOGS_MESSAGE_STATE87 = "Follow-up";
      CALL_LOGS_MESSAGE_STATE94 = "Document pending";
      CALL_LOGS_MESSAGE_STATE122 = "Docs Collected";
      CALL_LOGS_MESSAGE_STATE164 = "AIP Rejected";
      CALL_LOGS_MESSAGE_STATE165 = "AIP Rejected";
      CALL_LOGS_MESSAGE_STATE166 = "AIP Rejected";
      CALL_LOGS_MESSAGE_STATE167 = "CBS-Data captured";
      CALL_LOGS_MESSAGE_STATE_EKYC_SAVED = "EKYC service called";
      CALL_LOGS_MESSAGE_STATE_DATA_SAVED = "Data saved";
      CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS = "Applicant PAN - SUCCESS";
      CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL = "Applicant PAN - FAIL";
      CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_SUCCESS = "Co-applicant PAN - SUCCESS";
      CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_FAIL = "Co-applicant PAN - FAIL";
      CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_147 = "RSM : APPROVED/REFFERED";
      CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_19 = "RSM : REJECT";
      CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON = "RSM : RE-INITIATE";
      CALL_LOGS_MESSAGE_STATE2 = "Fresh no PAN";
      CALL_LOGS_MESSAGE_STATE100 = "Fresh";
      CALL_LOGS_MESSAGE_STATE101 = "Fresh Lead (appt tentative)";
      CALL_LOGS_MESSAGE_STATE104 = "Application submitted";
      CALL_LOGS_MESSAGE_STATE124 = "OTP not done";
      CALL_LOGS_MESSAGE_STATE105 = "New lead";
      CALL_LOGS_MESSAGE_STATE141 = "Lead Partial Entry";
      CALL_LOGS_MESSAGE_STATE153 = "Lead Partial Entry Confirmed";
      CALL_LOGS_MESSAGE_STATE152 = "Lead Not Eligible";
      CALL_LOGS_MESSAGE_STATE169 = "EKYC-Biometric captured";
      CALL_LOGS_MESSAGE_STATE168 = "CBS OTP verified";
      CALL_LOGS_MESSAGE_STATE170 = "Prima facie ineligible";
      CALL_LOGS_MESSAGE_STATE171 = "Mobile OTP done";
      CALL_LOGS_MESSAGE_STATE172 = "CBS OTP not verified";
      CALL_LOGS_MESSAGE_STATE173 = "OTP verified - CBS";
      CALL_LOGS_MESSAGE_STATE174 = "Application submitted";
      CALL_LOGS_MESSAGE_STATE175 = "Lead CBS OTP not verified";
      CALL_LOGS_MESSAGE_STATE176 = "Lead CBS OTP verified";
      CALL_LOGS_MESSAGE_STATE177 = "OTP verified";
      CALL_LOGS_MESSAGE_STATE178 = "Lead OTP Verified";
      CALL_LOGS_MESSAGE_STATE347 = "Post OTP Ineligible";
      CALL_LOGS_MESSAGE_STATE59_ID = 59;
      CALL_LOGS_MESSAGE_STATE43_ID = 43;
      CALL_LOGS_MESSAGE_STATE1_ID = 1;
      CALL_LOGS_MESSAGE_STATE73_ID = 73;
      CALL_LOGS_MESSAGE_STATE19_ID = 19;
      CALL_LOGS_MESSAGE_STATE147_ID = 147;
      CALL_LOGS_MESSAGE_STATE29_ID = 29;
      CALL_LOGS_MESSAGE_STATE6_ID = 6;
      CALL_LOGS_MESSAGE_STATE161_ID = 161;
      CALL_LOGS_MESSAGE_STATE2_ID = 2;
      CALL_LOGS_MESSAGE_STATE100_ID = 100;
      CALL_LOGS_MESSAGE_STATE101_ID = 101;
      CALL_LOGS_MESSAGE_STATE104_ID = 104;
      CALL_LOGS_MESSAGE_STATE124_ID = 124;
      CALL_LOGS_MESSAGE_STATE105_ID = 105;
      CALL_LOGS_MESSAGE_STATE141_ID = 141;
      CALL_LOGS_MESSAGE_STATE143_ID = 143;
      CALL_LOGS_MESSAGE_STATE153_ID = 153;
      CALL_LOGS_MESSAGE_STATE152_ID = 152;
      CALL_LOGS_MESSAGE_STATE169_ID = 169;
      CALL_LOGS_MESSAGE_STATE22_ID = 22;
      CALL_LOGS_MESSAGE_STATE160_ID = 160;
      CALL_LOGS_MESSAGE_STATE164_ID = 164;
      CALL_LOGS_MESSAGE_STATE165_ID = 165;
      CALL_LOGS_MESSAGE_STATE166_ID = 166;
      CALL_LOGS_MESSAGE_STATE167_ID = 167;
      CALL_LOGS_MESSAGE_STATE168_ID = 168;
      CALL_LOGS_MESSAGE_STATE170_ID = 170;
      CALL_LOGS_MESSAGE_STATE171_ID = 171;
      CALL_LOGS_MESSAGE_STATE172_ID = 172;
      CALL_LOGS_MESSAGE_STATE173_ID = 173;
      CALL_LOGS_MESSAGE_STATE174_ID = 174;
      CALL_LOGS_MESSAGE_STATE175_ID = 175;
      CALL_LOGS_MESSAGE_STATE176_ID = 176;
      CALL_LOGS_MESSAGE_STATE177_ID = 177;
      CALL_LOGS_MESSAGE_STATE178_ID = 178;
      CALL_LOGS_MESSAGE_STATE347_ID = 347;
      CALL_LOGS_MESSAGE_STATE384_ID = 384;
      CALL_LOGS_MESSAGE_STATE32_ID = 32;
      CALL_LOGS_MESSAGE_STATE87_ID = 87;
      CALL_LOGS_MESSAGE_STATE94_ID = 94;
      CALL_LOGS_MESSAGE_STATE122_ID = 122;
      COPY_PERMISSION_COMMAND = bundle.getString("COPY_PERMISSION_COMMAND");
      FILE_PERMISSION_COMMAND = bundle.getString("FILE_PERMISSION_COMMAND");
      UPLOAD_INITIAL = bundle.getString("UPLOAD_INITIAL");
      UPLOAD_PATH_LIVE = bundle.getString("UPLOAD_PATH_LIVE");
      UPLOAD_DOCGRAPH = 1;
      UPLOAD_IDENTITYPROOF = 2;
      UPLOAD_RESIDENSEPROOF = 3;
      UPLOAD_INCOMEPROOF = 4;
      UPLOAD_EMPLOYMENTPROOF = 5;
      IMAGE_FILE_EXTENSION = ".jpg";
      LOAN_ELIGIBILITY_MULTIPLER_FACTOR = Double.valueOf(100000.0D);
      LOAN_TENURE_MULTIPLER_FACTOR = Integer.valueOf(12);
      MESSAGE_TYPE_EMAIL = Integer.valueOf(1);
      MESSAGE_TYPE_SMS = Integer.valueOf(2);
      MESSAGE_TYPE_SMS_CONSENT = Integer.valueOf(3);
      MESSAGE_TYPE_FLIPKART_OTP = Integer.valueOf(6);
      MESSAGE_TYPE_FLIPKART_ELIGIBILITY = Integer.valueOf(7);
      REQUEST_INDEX_DOCUMENT_SCHEDULE = Integer.valueOf(18);
      REQUEST_INDEX_APPLICATION_SUBMIT = Integer.valueOf(17);
      REQUEST_INDEX_SAVE_QUOTE = Integer.valueOf(6);
      DUPLICATION_MULTIFICATION_FACTOR = Integer.valueOf(86400000);
      APP_DUPLICATION_CHECK = bundle.getString("APP_DUPLICATION_CHECK");
      APP_DUPLICATION_TIME_PERIOD = bundle.getString("APP_DUPLICATION_TIME_PERIOD");
      APP_DEDUPLICATION_MESSAGE = bundle.getString("APP_DEDUPLICATION_MESSAGE");
      APP_DUPLICATION_TIME_PERIOD_CARDS = bundle.getString("APP_DUPLICATION_TIME_PERIOD_CARDS");
      INQUIRY_DUPLICATION_TIME_PERIOD_CARDS = bundle.getString("INQUIRY_DUPLICATION_TIME_PERIOD_CARDS");
      INQUIRY_DUPLICATION_CHECK = bundle.getString("ENQUIRY_DUPLICATION_CHECK");
      INQUIRY_DUPLICATION_TIME_PERIOD = bundle.getString("ENQUIRY_DUPLICATION_TIME_PERIOD");
      INQUIRY_DEDUPLICATION_MESSAGE = bundle.getString("ENQUIRY_DEDUPLICATION_MESSAGE");
      FIRST_EMAIL_PART = "<html><head><title>" + BANK_FULL_NAME + "</title><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>" + 
        "<style type='text/css'>" + 
        "@media only screen and (max-width:600px){table.body{width:600px!important; padding:0 4px;}}" + 
        "@media only screen and (max-width:320px){table.body{width:320px!important; padding:0 4px;}}" + 
        "@media only screen and (max-width:480px){table.body{width:480px!important; padding:0 4px;}}" + 
        "</style></head><body>";
      FLEXI_TEMPLATE = "<table align='' border='0' cellpadding='0' cellspacing='0' width='100%' ><tbody><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-top:none; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI1_DURATION]</td><td style='border:1px solid #aaa;border-left:0px; border-top:none;border-right:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; ' >[EMI1_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '> [EMI2_DURATION]</td><td style='border:1px solid #aaa; border-left:0px;border-right:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img src='[BASE_URL]rupee-icon.jpg' style='vertical-align:bottom' />[EMI2_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI3_DURATION]</td><td style='border:1px solid #aaa;border-right:0px;border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img style='vertical-align:bottom' src='[BASE_URL]rupee-icon.jpg'>[EMI3_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; border-bottom:none; padding:4px 4px 4px 8px; color:#333333; text-align:left; font-size:12px; '>[EMI4_DURATION]</td><td style='border:1px solid #aaa; border-right:0px;border-bottom:none;border-left:0px; padding:4px 4px 4px 8px; color:#333333; text-align:left; font-size:12px; '><img style='vertical-align:bottom' src='[BASE_URL]rupee-icon.jpg'>[EMI4_AMOUNT]</td></tr></tbody></table>";
      THIRD_EMAIL_PART = "</body></html>";
      AUTO_DISPOSE_TIME = "2014-11-13 23:59:59";
      IS_AUTO_DISPOSE = true;
      HL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("HL_CAPTCHA_BY_PASS"));
      EL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("EL_CAPTCHA_BY_PASS"));
      AL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("AL_CAPTCHA_BY_PASS"));
      AGRI_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("AGRI_CAPTCHA_BY_PASS"));
      CREDIT_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("CREDIT_CAPTCHA_BY_PASS"));
      AUTO_LOAN_OFFER_ENABLE = Boolean.parseBoolean(bundle.getString("AUTO_LOAN_OFFER_ENABLE"));
      BUREAULINK_BYPASS_HL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_HL"));
      BUREAULINK_BYPASS_PL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_PL"));
      BUREAULINK_BYPASS_AL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_AL"));
      BUREAULINK_BYPASS_EL = Boolean.parseBoolean(bundle.getString("BUREAULINK_BYPASS_EL"));
      BUREAU = bundle.getString("BUREAU");
      COUNTRY_CODE_INDIA = "91";
      SAVE_QUOTE_REDIRECTION = bundle.getString("SAVE_QUOTE_REDIRECTION");
      PDF_SESSION_TIMEOUT_REDIRECTION = bundle.getString("PDF_SESSION_TIMEOUT_REDIRECTION");
      ALREADY_LOGGEDIN_REDIRECTION = bundle.getString("ALREADY_LOGGEDIN_REDIRECTION");
      BRANCH_NOT_FOUND_MSG = "Sorry, we did not find any branch for your current selection of city/district. Please select a branch from your nearby city/district or visit any of our branches to apply.";
      PICKUP_TIME_LIST = SbiUtil.generatePickupTimeList();
      BANK_IMAGE_FOLDER = bundle.getString("BANK_IMAGE_FOLDER");
      BANK_CSS_FOLDER = bundle.getString("BANK_CSS_FOLDER");
      BANK_JS_FOLDER = bundle.getString("BANK_JS_FOLDER");
      BANK_ID_SBI = 1;
      BANK_ID_SBBJ = 2;
      BANK_ID_SBH = 3;
      BANK_ID_SBM = 4;
      BANK_ID_SBP = 5;
      BANK_ID_SBT = 6;
      BANK_ID = Integer.valueOf(Integer.parseInt(bundle.getString("BANK_ID")));
      BANK_NAME = bundle.getString("BANK_NAME");
      BANK_FULL_NAME = bundle.getString("BANK_FULL_NAME");
      BANK_URL = bundle.getString("BANK_URL");
      BANK_FULL_URL = bundle.getString("BANK_FULL_URL");
      BANK_COPYRIGHT = bundle.getString("BANK_COPYRIGHT");
      OTP_SUBJECT_LINE = bundle.getString("OTP_SUBJECT_LINE");
      HOME_LOAN_PRODUCT_NAME = bundle.getString("HOME_LOAN_PRODUCT_NAME");
      AUTO_LOAN_PRODUCT_NAME = bundle.getString("AUTO_LOAN_PRODUCT_NAME");
      PERSONAL_LOAN_PRODUCT_NAME = bundle.getString("PERSONAL_LOAN_PRODUCT_NAME");
      EDUCATION_LOAN_PRODUCT_NAME = bundle.getString("EDUCATION_LOAN_PRODUCT_NAME");
      FLEXI_PAY_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("FLEXI_PAY_PRODUCT_ID")));
      SBI_PRIVILEGE_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("SBI_PRIVILEGE_PRODUCT_ID")));
      SBI_SHAURYA_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("SBI_SHAURYA_PRODUCT_ID")));
      XPRESS_CREDIT_IT_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("XPRESS_CREDIT_IT_PRODUCT_ID")));
      XPRESS_CREDIT_ELITE_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("XPRESS_CREDIT_ELITE_PRODUCT_ID")));
      XPRESS_CREDIT_NPE_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("XPRESS_CREDIT_NPE_PRODUCT_ID")));
      REGULAR_PENSION = Integer.valueOf(Integer.parseInt(bundle.getString("REGULAR_PENSION")));
      JAI_JAWAN_PENSION_LOAN = Integer.valueOf(Integer.parseInt(bundle.getString("JAI_JAWAN_PENSION_LOAN")));
      FAMILY_PENSION = Integer.valueOf(Integer.parseInt(bundle.getString("FAMILY_PENSION")));
      BANK_ONLINE_URL = bundle.getString("BANK_ONLINE_URL");
      BANK_FINDER_URL = bundle.getString("BANK_FINDER_URL");
      BANK_SUPPORT_EMAIL = bundle.getString("BANK_SUPPORT_EMAIL");
      EKYC_CHANNEL = "OC";
      EKYC_BIOMETRIC_DEDUPE_BYPASS = (new Boolean(bundle.getString("EKYC_BIOMETRIC_DEDUPE_BYPASS"))).booleanValue();
      CBS_LOAN_TYPE_HOME_TOP_UP = "HL";
      CBS_LOAN_TYPE_AUTO_LOAN = "HL";
      CBS_LOAN_TYPE_XPRESS_CREDIT_TOP_UP = "XS";
      CBS_LOAN_TYPE_EDUCATION_TOP_UP = "ED";
      CBS_LOAN_TYPE_AGRI_LOAN = "AGL";
      CBS_LOAN_TYPE_CREDIT_CARD = "SCC";
      CBS_IRAC_STATUS = Integer.valueOf(4);
      CBS_BANK_CODE = "0";
      CBS_LOAN_TYPE = "CI";
      CBS_OTP_BYPASS = new Boolean(bundle.getString("CBS_OTP_BYPASS"));
      OCAS_KEYWORD = "OCASY";
      SERVER_SEQ = bundle.getString("SERVER_SEQ");
      NO_LOAN_OFFER_EL = "Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.";
      SBI_PRIVILEGE_SHAURYA_EMAIL_TEMPLATE = "<table align='' border='0' cellpadding='0' cellspacing='0' width='100%' ><tbody><tr><td style='border:1px solid #aaa; border-left:0px;padding:4px 4px 4px 8px; border-top:none; border-bottom:none; color:#333333; text-align:left; font-size:12px; '>[EMI1_DURATION]</td><td style='border:1px solid #aaa;border-left:0px; border-top:none;border-right:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; ' >[EMI1_AMOUNT]</td></tr><tr><td style='border:1px solid #aaa; border-left:0px; padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '> [EMI2_DURATION]</td><td style='border:1px solid #aaa; border-left:0px;border-right:0px;padding:4px 4px 4px 8px; border-bottom:none; color:#333333; text-align:left; font-size:12px; '><img src='[BASE_URL]rupee-icon.jpg' style='vertical-align:bottom' />[EMI2_AMOUNT]</td></tr></tbody></table>";
      NO_LOAN_OFFER_EL_NEW = "Dear customer, based on the inputs provided, you are not eligible under this scheme. Kindly login to <a href='https://www.vidyalakshmi.co.in/Students/' target='blank'>www.vidyalakshmi.co.in/Students</a> for applying online and select SBI as preferred bank.";
      COMMON_SORRY_MSG = "Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.";
      AUTH_KEY = bundle.getString("AUTH_KEY");
      APP_MOBILE_HASH_KEY = bundle.getString("APP_MOBILE_HASH_KEY");
      SBI_TATA_CONSTANT = "SBTA";
      DIP_LOCAL = bundleConfig.getString("DIP_LOCAL");
      DPT_LOCAL = bundleConfig.getString("DPT_LOCAL");
      CALLBACKSERVICEURL = bundle.getString("CALLBACKSERVICEURL");
      CLICK_TO_CALL_BYPASS = Boolean.parseBoolean(bundle.getString("CLICK_TO_CALL_BYPASS"));
      IS_PROXY_REQUIRED = new Boolean(bundle.getString("IS_PROXY_REQUIRED"));
      PROXY_HOST = bundle.getString("PROXY_HOST");
      PROXY_PORT = bundle.getString("PROXY_PORT");
      PROXY_USER = bundle.getString("PROXY_USER");
      PROXY_PASSWORD = bundle.getString("PROXY_PASSWORD");
      TNC_URL_HL = bundle.getString("TNC_URL_HL");
      TNC_URL_AL = bundle.getString("TNC_URL_AL");
      TNC_URL_PL = bundle.getString("TNC_URL_PL");
      SCC_PDF_GENRATION_LOCATION = bundle.getString("SCC_PDF_GENRATION_LOCATION");
      SCC_LOAN_ACTION = bundle.getString("SCC_LOAN_ACTION");
      CVE_ID = Integer.valueOf(24);
      CVE_ACTION = bundle.getString("CVE_ACTION");
      SCC_LOAN_ACTION_DSR = bundle.getString("SCC_LOAN_ACTION_DSR");
      SCC_LOAN_TITLE = bundle.getString("SCC_LOAN_TITLE");
      SCC_LOAN_KEYWORDS = bundle.getString("SCC_LOAN_KEYWORDS");
      SCC_LOAN_DESCRIPTION = bundle.getString("SCC_LOAN_DESCRIPTION");
      INTIAL_STRING_SCC = bundle.getString("INTIAL_STRING_SCC");
      SOURCE_STRING_SCC = bundle.getString("SOURCE_STRING_SCC");
      PDF_TEMPLATE_BASE_PATH = bundle.getString("PDF_TEMPLATE_BASE_PATH");
      SCC_WEBSERVICE_BYPASS = (new Boolean(bundle.getString("SCC_WEBSERVICE_BYPASS"))).booleanValue();
      MAGIC_SALES_INITIAL = bundle.getString("MAGIC_SALES_INITIAL");
      ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
      CRM_NEW_WEBSERVICE = bundle.getString("CRM_NEW_WEBSERVICE");
      CRM_NEW_API_TOKEN = bundle.getString("CRM_NEW_API_TOKEN");
      CRM_NEW_AES_KEY = bundle.getString("CRM_NEW_AES_KEY");
      CRM_NEW_AES_IV = bundle.getString("CRM_NEW_AES_IV");
      CRM_NEW_API_ExpiresOn = bundle.getString("CRM_NEW_API_ExpiresOn");
      CRM_NEW_API_Username = bundle.getString("CRM_NEW_API_Username");
      CRM_NEW_API_Password = bundle.getString("CRM_NEW_API_Password");
      PHOTOGRAPH_DOC = Integer.valueOf(1);
      IDENTITY_PROOF_DOC = Integer.valueOf(2);
      RESIDANCE_PROOF_DOC = Integer.valueOf(3);
      INCOME_PROOF_DOC = Integer.valueOf(4);
      EMPLOYMENT_PROOF_DOC = Integer.valueOf(5);
      SIGNATURE_DOC = Integer.valueOf(6);
      BANK_IMAGE_FOLDER_NEWUI = bundle.getString("BANK_IMAGE_FOLDER_NEWUI");
      BANK_CSS_FOLDER_NEWUI = bundle.getString("BANK_CSS_FOLDER_NEWUI");
      BANK_JS_FOLDER_NEWUI = bundle.getString("BANK_JS_FOLDER_NEWUI");
      SERVER_ID = "1";
      CRM_BYPASS = (new Boolean(bundle.getString("CRM_BYPASS"))).booleanValue();
      CBS_DEDUPE_BYPASS = (new Boolean(bundle.getString("CBS_DEDUPE_BYPASS"))).booleanValue();
      IVRS_SALT = bundle.getString("IVRS_SALT");
      PL_CAPTCHA_BY_PASS = Boolean.parseBoolean(bundle.getString("PL_CAPTCHA_BY_PASS"));
      CBS_LOAN_TYPE_PENSION = "PNL";
      EMPLOYMENT_TYPE_PENSIONERS = Integer.valueOf(21);
      URL_QUTOE_TOKEN_PNL = "PNLQuoteToken";
      PENSION_LOAN_ACTION_DSR = "pension-loan-dsr";
      PENSION_LOAN_ACTION = bundle.getString("PENSION_LOAN_ACTION");
      SCHEMENAME = bundle.getString("SCHEMENAME");
      EDUCATION_LOAN_TAKEOVER_ACTION = bundle.getString("EDUCATION_LOAN_TAKEOVER_ACTION");
      URL_QUTOE_TOKEN_EL_TAKE_OVER = "ELTakeOverQuoteToken";
      EDUCATION_TAKEOVER_LOAN = "EDUCATION TAKEOVER LOAN";
      PENSION_LOAN_PURPOSE_ID = Integer.valueOf(23);
      EDUCATION_TAKEOVER_LOAN_PURPOSE_ID = Integer.valueOf(24);
      SORRY_FOR_INCONVENIENCE = bundle.getString("SORRY_FOR_INCONVENIENCE");
      SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT = bundle.getString("SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT");
      APP_RETRY_DUPLICATION_TIME_PERIOD = Integer.valueOf(Integer.parseInt(bundle.getString("APP_RETRY_DUPLICATION_TIME_PERIOD")));
      APP_MAXIMUM_OTP_REQUEST_MSG = bundle.getString("APP_MAXIMUM_OTP_REQUEST_MSG");
      APP_MAXIMUM_OTP_REQUEST_COUNT = Integer.valueOf(Integer.parseInt(bundle.getString("APP_MAXIMUM_OTP_REQUEST_COUNT")));
      APP_MAXIMUM_OTP_ATTEMPT_MSG = bundle.getString("APP_MAXIMUM_OTP_ATTEMPT_MSG");
      APP_MAXIMUM_OTP_ATTEMPT_COUNT = Integer.valueOf(Integer.parseInt(bundle.getString("APP_MAXIMUM_OTP_ATTEMPT_COUNT")));
      APP_MAXIMUM_RESEND_OTP_REQUEST_MSG = bundle.getString("APP_MAXIMUM_RESEND_OTP_REQUEST_MSG");
      APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG = bundle.getString("APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG");
      TOKEN_REQUEST_TIME_PERIOD = Integer.valueOf(Integer.parseInt(bundle.getString("TOKEN_REQUEST_TIME_PERIOD")));
      BHL_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("BHL_PRODUCT_ID")));
      BHL_SPREAD_RATE = Double.valueOf(Double.parseDouble(bundle.getString("BHL_SPREAD_RATE")));
      CHAR_SEQ = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
      APP_EL_TYPE_ID_BIDYALAKHMI = Integer.valueOf(4);
      EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID = Integer.valueOf(26);
      IS_ENGINE_OBF = (new Boolean(bundle.getString("IS_ENGINE_OBF"))).booleanValue();
      RAC_SETTING_ENABLE = (new Boolean(bundle.getString("RAC_SETTING_ENABLE"))).booleanValue();
      RAC_DTY = bundleConfig.getString("jdbc.servertype");
      RAC_DN_PERSONAL = bundleConfig.getString("jdbc.name_personal");
      RAC_DIP = bundleConfig.getString("jdbc.ip");
      RAC_DPT = bundleConfig.getString("jdbc.port");
      RAC_DN = bundleConfig.getString("jdbc.name");
      REFERENCE_NUMBER_BASED_ON_CLUSTER = (new Boolean(bundle.getString("REFERENCE_NUMBER_BASED_ON_CLUSTER"))).booleanValue();
      CALLBACK_SMS_CONSENT = (new Boolean(bundle.getString("CALLBACK_SMS_CONSENT"))).booleanValue();
      NONCBS_SMS_CONSENT = (new Boolean(bundle.getString("NONCBS_SMS_CONSENT"))).booleanValue();
      EXTERNAL_SERVICE_TIMED_OUT = Integer.valueOf(Integer.parseInt(bundle.getString("EXTERNAL_SERVICE_TIMED_OUT")));
      APP_MAXIMUM_EMAIL_ATTEMPT_COUNT = Integer.valueOf(Integer.parseInt(bundle.getString("APP_MAXIMUM_EMAIL_ATTEMPT_COUNT")));
      APP_MAXIMUM_EMAIL_ATTEMPT_MSG = bundle.getString("APP_MAXIMUM_EMAIL_ATTEMPT_MSG");
      PGL_PRODUCT_ID = Integer.valueOf(Integer.parseInt(bundle.getString("PGL_PRODUCT_ID")));
      PMAY_SCHEME1 = bundle.getString("PMAY_SCHEME1");
      PMAY_SCHEME2 = bundle.getString("PMAY_SCHEME2");
      PMAY_SCHEME3 = bundle.getString("PMAY_SCHEME3");
      PMAY_SCHEME4 = bundle.getString("PMAY_SCHEME4");
      SMS_SERVICE_BYPASS = new Boolean(bundle.getString("SMS_SERVICE_BYPASS"));
      UI_TYPE = bundle.getString("UI_TYPE");
      SECRET_KEY_BYPASS = new Boolean(bundle.getString("SECRET_KEY_BYPASS"));
      GOLD_LOAN_ACTION = bundle.getString("GOLD_LOAN_ACTION");
      CBS_SI_PAN_VALIDATE_API_BY_PASS = new Boolean(bundle.getString("CBS_SI_PAN_VALIDATE_API_BY_PASS"));
      CBS_SI_PAN_VALIDATE_API_URL = bundle.getString("CBS_SI_PAN_VALIDATE_API_URL");
      CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH = bundle.getString("CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH");
      CBS_SI_PAN_AES_PRIVATE_KEY = bundle.getString("CBS_SI_PAN_AES_PRIVATE_KEY");
      SMS_APPLIANCE_BASED_URL = bundle.getString("SMS_APPLIANCE_BASED_URL");
      SMS_APPLIANCE_BASED_MESSAGE = bundle.getString("SMS_APPLIANCE_BASED_MESSAGE");
      SMS_DOM_APPLIANCE_BASED_USERNAME = bundle.getString("SMS_DOM_APPLIANCE_BASED_USERNAME");
      SMS_DOM_APPLIANCE_BASED_PASSWORD = bundle.getString("SMS_DOM_APPLIANCE_BASED_PASSWORD");
      SMS_INTER_APPLIANCE_BASED_USERNAME = bundle.getString("SMS_INTER_APPLIANCE_BASED_USERNAME");
      SMS_INTER_APPLIANCE_BASED_PASSWORD = bundle.getString("SMS_INTER_APPLIANCE_BASED_PASSWORD");
      RSA_KEYSTORE_PATH = bundle.getString("RSA_KEYSTORE_PATH");
      RSA_KEYSTORE_PASS = bundle.getString("RSA_KEYSTORE_PASS");
      RSA_KEYSTORE_ALIAS = bundle.getString("RSA_KEYSTORE_ALIAS");
      CBS_ACCOUNT_LEVEL_API_URL = bundle.getString("CBS_ACCOUNT_LEVEL_API_URL");
      CBS_EDU_LOAN_ACCOUNT_API_URL = bundle.getString("CBS_EDU_LOAN_ACCOUNT_API_URL");
      EDMS_CERTIFICATE = bundle.getString("EDMS_CERTIFICATE");
      EDMS_PRIVATE_KEY = bundle.getString("EDMS_PRIVATE_KEY");
      EDMS_URL = bundle.getString("EDMS_URL");
      UPLOAD_PATH_LIVE_EDMS = bundle.getString("UPLOAD_PATH_LIVE_EDMS");
      CRM_REST_API_TOKEN_URL_CVE = bundle.getString("CRM_REST_API_TOKEN_URL_CVE");
      CRM_REST_API_URL_CVE = bundle.getString("CRM_REST_API_URL_CVE");
      CRM_REST_RSA_ENCRYPTION_CERTIFICATE_PATH = bundle.getString("CRM_REST_RSA_ENCRYPTION_CERTIFICATE_PATH");
      CRM_REST_RSA_KEYSTORE_PATH = bundle.getString("CRM_REST_RSA_KEYSTORE_PATH");
      CRM_REST_RSA_KEYSTORE_PASS = bundle.getString("CRM_REST_RSA_KEYSTORE_PASS");
      CRM_REST_RSA_KEYSTORE_ALIAS = bundle.getString("CRM_REST_RSA_KEYSTORE_ALIAS");
      CRM_REST_AES_PRIVATE_KEY = bundle.getString("CRM_REST_AES_PRIVATE_KEY");
      CASE_RESPONSE = "FAIL";
	  CRM_REST_API_URL_CVE_CONSENT = bundle.getString("CRM_REST_API_URL_CVE_CONSENT");
      INTIAL_STRING_CVE = "2";
      SOURCE_STRING_CVE = "4";
      CVE_TITLE = bundle.getString("CVE_TITLE");
      CVE_KEYWORDS = bundle.getString("CVE_KEYWORDS");
      CVE_DESCRIPTION = bundle.getString("CVE_DESCRIPTION");
	  CRM_REST_API_URL = bundle.getString("CRM_REST_API_URL");
	  CRM_TOKEN_REQUEST_FIELD1 = bundle.getString("CRM_TOKEN_REQUEST_FIELD1");
	  CRM_TOKEN_REQUEST_FIELD2 = bundle.getString("CRM_TOKEN_REQUEST_FIELD2");
	  CRM_FETCH_LEAD_STATUS_URL = bundle.getString("CRM_FETCH_LEAD_STATUS_URL");
	  UPLOADLEAD_SECRETKEY = bundle.getString("UPLOADLEAD_SECRETKEY");
	  SBIUTIL_PASSPHRASE = bundle.getString("SBIUTIL_PASSPHRASE");
	  SBI_HOMELOANS = bundle.getString("SBI_HOMELOANS");
	  SBI_SSO = bundle.getString("SBI_SSO");
	  SBI_ONLINE_PROD = bundle.getString("SBI_ONLINE_PROD");
	  IP_PR=bundle.getString("IP_PR");
	  IP_DR=bundle.getString("IP_DR");
	  BANK_ONLINE_NEW_DOMAIN_URL=bundle.getString("BANK_ONLINE_NEW_DOMAIN_URL");
	  BANK_ONLINE_OLD_DOMAIN=bundle.getString("BANK_ONLINE_OLD_DOMAIN");

    } catch (IOException e) {
        logger.info("Constants.java LN 19 exception :: static", e);
    } catch (Exception e) {
      logger.info("Constants.java LN 19 exception :: static", e);
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
