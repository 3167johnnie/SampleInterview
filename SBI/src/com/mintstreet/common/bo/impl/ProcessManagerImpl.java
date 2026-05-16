package com.mintstreet.common.bo.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.callback.service.CallBackService;
import com.mintstreet.campaign.dao.MarTechDao;
import com.mintstreet.campaign.entity.MarTech;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.service.AgriLoanService;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.card.entity.ApplicationFormCreditCard;
import com.mintstreet.loan.card.service.CreditCardService;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.service.PersonalLoanService;

public class ProcessManagerImpl {
  private static final Logger logger = LogManager.getLogger(ProcessManagerImpl.class.getName());
  
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private CommunicationManagerImpl communicationManagerImpl;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  @Autowired
  private AutoLoanService autoLoanService;
  
  @Autowired
  private HomeLoanService homeLoanService;
  
  @Autowired
  private PersonalLoanService personalLoanService;
  
  @Autowired
  private EducationLoanService educationLoanService;
  
  @Autowired
  private CallBackService callBackService;
  
  @Autowired
  private CreditCardService creditCardService;
  
  @Autowired
  private AgriLoanService agriLoanService;
  
  @Autowired
  private MarTechDao marTechDao;
  
  public JSONObject processWantUsToCallYou(Integer leadId, Integer stateId, ApplicationFormLead lead, Integer trackVisitId, Integer bankLMSUserId, String inputOtp1) {
    JSONObject json = new JSONObject();
    try {
    	//new code for decrypting otp
    	String inputOtp = null;
    	
    	if(inputOtp1 !=null && inputOtp1.length() >6) {
    		SbiUtil sbiu=new SbiUtil();
    		inputOtp=sbiu.getDecryptedRequest(inputOtp1);
    	}else if(inputOtp1 !=null && inputOtp1.length()==6) {
    		inputOtp=inputOtp1;
    	}
    	
    	logger.info("ProcessManagerImpl : 86 product id " + lead.getLeadLoanPurposeId());
    	
		if (lead.getLeadLoanPurposeId() != null && (lead.getLeadLoanPurposeId() == 8 || lead.getLeadLoanPurposeId() == 9
				|| lead.getLeadLoanPurposeId() == 11)) {
			Optional<MarTech> marTechDetails = Optional.ofNullable(marTechDao.getDetailsByVisitId(trackVisitId));
			if (marTechDetails.isPresent()) {
				MarTech marTech = marTechDetails.get();
				marTech.setCallBackProductId(lead.getLeadLoanPurposeId());
				marTechDao.save(marTech.getMartechId(), marTech);
			}
		}
    	
      if (lead != null) {
        if (leadId != null) {
          ApplicationFormLead leadPreserve = this.commonService.getLeadById(leadId);
          if (leadPreserve == null) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", 1);
            return json;
          } 
          lead.setLeadMobileVerificationCodeReceived(leadPreserve.getLeadMobileVerificationCodeReceived());
          if (leadPreserve.getLeadProductTypeId() != null && lead.getLeadProductTypeId() != null && 
            leadPreserve.getLeadProductTypeId().toString().equalsIgnoreCase(lead.getLeadProductTypeId().toString())) {
            if (lead.getLeadApplyingFrom() == 2) {
              if (leadPreserve.getLeadMobileNo().equalsIgnoreCase(lead.getLeadMobileNo()) && 
                "Y".equalsIgnoreCase(leadPreserve.getLeadMobileVerificationCodeVerified())) {
                json.put("status", "error");
                json.put("message", "You have already made request to call you with number " + leadPreserve.getLeadMobileNo());
                json.put("alertCount", (lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
                return json;
              } 
            } else if (lead.getLeadMobileNo() != null && 
              leadPreserve.getLeadMobileNo() != null && 
              leadPreserve.getLeadMobileNo().equalsIgnoreCase(lead.getLeadMobileNo()) && 
              "Y".equalsIgnoreCase(leadPreserve.getLeadMobileVerificationCodeVerified())) {
              json.put("status", "error");
              json.put("message", "You have already made request to call you with number " + lead.getLeadMobileNo());
              json.put("alertCount", (lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
              return json;
            } 
            if (!lead.getLeadMobileNo().equalsIgnoreCase(leadPreserve.getLeadMobileNo()))
              lead.setLeadMobileAlertCount(Integer.valueOf(0)); 
          } 
        } 
      } else if (leadId != null) {
        lead = this.commonService.getLeadById(leadId);
      } 
      if (lead == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 1);
        return json;
      } 
      if (!ValidatorUtil.isValid(lead.getLeadIsdCode()))
        lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA); 
      int count = this.commonService.getCallBackLeadCount(lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo());
      logger.info("ProcessManagerImpl.java LNo : 99 count : " + count);
      if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
        json.put("status", "error");
        json.put("message", Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
        json.put("alertCount", 1);
        return json;
      } 
      Integer alertCount = Integer.valueOf(0);
      if (stateId.intValue() == 3) {
        if (!ValidatorUtil.isValid(lead.getLeadFirstName())) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
          json.put("alertCount", 1);
          return json;
        } 
        if (ValidatorUtil.isValid(lead.getLeadFirstName()))
          SessionUtil.setApplicantName(lead.getLeadFirstName()); 
        if (ValidatorUtil.isValid(lead.getLeadWorkEmail()))
          SessionUtil.setEmail(lead.getLeadWorkEmail()); 
        SessionUtil.setMobile(lead.getLeadMobileNo());
        if (ValidatorUtil.isValid(lead.getLeadLoanPurposeId()))
          if (lead.getLeadLoanPurposeId().intValue() == 1 || lead.getLeadLoanPurposeId().intValue() == 2) {
            lead.setLeadProductTypeId(Integer.valueOf(1));
          } else if (lead.getLeadLoanPurposeId().intValue() == 3) {
            lead.setLeadProductTypeId(Integer.valueOf(2));
          } else if (lead.getLeadLoanPurposeId().intValue() == 4 || lead.getLeadLoanPurposeId().intValue() == 6 || lead.getLeadLoanPurposeId().intValue() == 7) {
            lead.setLeadProductTypeId(Integer.valueOf(3));
          } else if (lead.getLeadLoanPurposeId().intValue() == 8 || lead.getLeadLoanPurposeId().intValue() == 9 || lead.getLeadLoanPurposeId().intValue() == 11 || lead.getLeadLoanPurposeId().intValue() == 13) {
            lead.setLeadProductTypeId(Integer.valueOf(4));
          } else if (lead.getLeadLoanPurposeId().intValue() == 10) {
            lead.setLeadProductTypeId(Integer.valueOf(17));
          } else if (lead.getLeadLoanPurposeId().intValue() == 12) {
            lead.setLeadProductTypeId(Integer.valueOf(19));
          } else if (lead.getLeadLoanPurposeId().intValue() == 15) {
            lead.setLeadProductTypeId(Integer.valueOf(15));
              logger.info("ProcessManagerImpl.java LNo : 157 lead : " + lead.getLeadLoanPurposeId().intValue()+"******"+lead.getLeadProductTypeId());
              logger.info("ProcessManagerImpl.java LNo : 158 lead : " + lead);
          } 
        
        if (lead.getLeadProductTypeId().equals(Constants.EDUCATION_LOAN_ID)) {
          lead.setLeadApplyingFrom(1);
          lead.setLeadCountryId(null);
        } 
        if (lead.getLeadApplyingFrom() == 2) {
          if (!ValidatorUtil.isValidNRIMobile(lead.getLeadMobileNo())) {
            json.put("status", "error");
            json.put("message", "Invalid mobile number");
            json.put("alertCount", alertCount);
            return json;
          } 
          MasterCountry country = this.commonService.getCountryById(lead.getLeadCountryId());
          lead.setLeadIsdCode(country.getCountryCode().toString());
        } else {
          lead.setLeadApplyingFrom(1);
          lead.setLeadCountryId(null);
          lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA);
          if (!ValidatorUtil.isValidMobile(lead.getLeadMobileNo().trim())) {
            json.put("status", "error");
            json.put("message", "Invalid mobile number");
            json.put("alertCount", alertCount);
            return json;
          } 
        } 
      } 
      if (stateId.intValue() == 3 || stateId.intValue() == 4) {
        if (stateId.intValue() == 3) {
          if (!Constants.DUMMY_MOBILE_NO.contains(lead.getLeadMobileNo()) && !Constants.INQUIRY_DUPLICATION_CHECK.equals("0")) {
            boolean isLeadExists = false;
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(lead.getLeadProductTypeId(), lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo(), Integer.valueOf(0));
            logger.info("isLeadExists resend " + isLeadExists);
            if (isLeadExists) {
              json.put("status", "error");
              json.put("message", Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              json.put("alertCount", alertCount);
              return json;
            } 
          } 
          if ("Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified())) {
            json.put("status", "error");
            json.put("message", "You have already made request to call you with number " + lead.getLeadMobileNo());
            json.put("alertCount", alertCount);
            return json;
          } 
          alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
          if (alertCount.intValue() >= 5) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
            json.put("alertCount", alertCount);
            return json;
          } 
          
			//save consentId
			Integer consentId = 0;
			if (lead.getLeadConsentId()== null) {
				Consent consent = commonService.getConsentByLoanType(Constants.CALLBACK_ID);
				consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
			} else {
				consentId = lead.getLeadConsentId();
			}
			
          lead.setLeadMobileVerificationCodeVerified("N");
          lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID));
          if (lead.getLeadSourceId() == null)
            lead.setLeadSourceId(Constants.LEAD_SOURCE_ID); 
          if (lead.getLeadDataSourceId() == null && 
            lead.getLeadDataSourceId() == null)
            lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK); 
          lead.setLeadReceiveDatetime(new Date());
          lead.setLeadEntryTime(new Date());
          lead.setLeadEntryDate(new Date());
          lead.setLeadLastUpdated(new Date());
          lead.setLeadAppContactCenterLocation(1);
          lead.setLeadActive("Y");
          lead.setLeadDeleted("N");
          lead.setLeadIntermediaryId(bankLMSUserId);
          lead.setLeadVisitId(trackVisitId);
          lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(lead.getLeadMobileNo()));
          lead.setLeadConsentId(consentId);
        } else if (stateId.intValue() == 4) {
          if (!Constants.DUMMY_MOBILE_NO.contains(lead.getLeadMobileNo()) && !Constants.INQUIRY_DUPLICATION_CHECK.equals("0")) {
            boolean isLeadExists = false;
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(lead.getLeadProductTypeId(), lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo(), Integer.valueOf(0));
            logger.info("isLeadExists resend " + isLeadExists);
            if (isLeadExists) {
              json.put("status", "error");
              json.put("message", Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              json.put("alertCount", alertCount);
              return json;
            } 
          } 
          alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
          if (alertCount.intValue() >= 5) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
            json.put("alertCount", alertCount);
            return json;
          } 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
          lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(lead.getLeadMobileNo()));
        } 
        alertCount = Integer.valueOf(alertCount.intValue() + 1);
        lead.setLeadMobileAlertCount(alertCount);
        lead.setLeadLastUpdated(new Date());
        try {
          lead = this.commonService.save(lead);
          logger.info("Lead Id " + lead.getLeadId() + " having visit id " + lead.getLeadVisitId());
          if (stateId.intValue() == 3)
            this.commonService.insertCallLog(lead.getLeadId(), bankLMSUserId, Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID), null); 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
        } catch (SQLException e) {
            logger.info("ProcessManagerImpl.java LNo : 227 : Exception Caught", e);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", 1);
            return json;
          } catch (Exception e) {
          logger.info("ProcessManagerImpl.java LNo : 227 : Exception Caught", e);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 1);
          return json;
        } 
        SessionUtil.setLeadId(lead.getLeadId());
        if (stateId.intValue() == 4 || lead.getLeadMobileVerificationCodeReceived() == null) {
            String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
            msgBody = this.SbiUtil.urlEncode(msgBody);
            
            String SMS_TEXT = null;
            
            if (lead.getLeadApplyingFrom() == 2) {
                SMS_TEXT = Constants.SMS_STRING_NRI;
            } else {
          	  SMS_TEXT = Constants.SMS_STRING_INDIAN;
            }
            
            SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
            SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", lead.getLeadMobileVerificationCode().toString());
            SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
            
            if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
  				logger.info("OTP for Mobile Number: " + (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim() + " is " + lead.getLeadMobileVerificationCode().toString());
            }
            
            if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
              json.put("status", "error");
              json.put("message", "OTP service is down");
              return json;
            } 
            lead.setLeadMobileVerificationCodeReceived("Y");
            lead = this.commonService.save(lead);
          } 
          return json;
        } 

      if (stateId.intValue() == 5) {
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Please enter valid OTP.");
          json.put("alertCount", alertCount);
          return json;
        } 
        if (lead.getLeadOTPAttempCount() == null) {
          lead.setLeadOTPAttempCount(Integer.valueOf(0));
        } else {
          alertCount = lead.getLeadOTPAttempCount();
          if (alertCount.intValue() >= 5) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
            json.put("alertCount", alertCount);
            return json;
          } 
        } 
        
        lead.setLeadOTPAttempCount(Integer.valueOf(lead.getLeadOTPAttempCount().intValue() + 1));
        if (lead.getLeadMobileVerificationCode() != null && 
          lead.getLeadMobileVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
          lead.setLeadMobileVerificationCodeVerified("Y");
          lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID));
          lead.setLeadLastUpdated(new Date());
          try {
        	  
        	  /* Code started for SSL Development */
        	  /*   lead.getLeadCityId();
              boolean pilot = false;
              int productType = lead.getLeadProductTypeId();
              logger.info("ProcessManagerImpl.java LNO :: 417 lead.getLeadCityId() "+lead.getLeadCityId()+" productType "+productType);
              List<MasterCity> list = applicationFormHomeLoanDao.getCitiesForSSL();
              //logger.info("ProcessManagerImpl.java LNO :: 419 list "+list);
         
              if(list!=null && !list.isEmpty())
              {	
              for (int i = 0; i < list.size(); i++) 
              {
        	       MasterCity vlpStatus = new MasterCity();
        	       MasterCity masterCity = list.get(i);
        	       masterCity.setCityId(masterCity.getCityId());
        	      
        	      if (productType == 1 || productType == 2) {
        	      if (lead.getLeadCityId().equals(masterCity.getCityId()))
        	      {
        	    	 lead.setLeadSSLHomeActive(1); 
        	    	 pilot = true;
        	    	break;
        	       }
                 }
        	      else {
        	    	 lead.setLeadSSLHomeActive(0);
        	    	 pilot = false;
        	      } 
               }
              }else {
           	   logger.info("ProcessManagerImpl.java LNO :: 378 city series is NULL ");
              }*/
              /* Code ended for SSL Development */

            lead = this.commonService.save(lead);
            this.commonService.insertCallLog(lead.getLeadId(), bankLMSUserId, Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID), null);
            if (!Constants.CLICK_TO_CALL_BYPASS && 
              lead != null && lead.getLeadLoanPurposeId() != null && lead.getLeadLoanPurposeId().intValue() != 10) {
              logger.info("----Service Response message----");
              this.callBackService.getcallBackService(lead);
              lead = this.commonService.save(lead);
            } 
            logger.info(" after Calling Call Center Service ----" + lead);
          } catch (SQLException e) {
              logger.info("ProcessManagerImpl.java LNo : 307 : Exception Caught", e);
              json.put("status", "error");
              json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
              json.put("alertCount", alertCount);
              return json;
            } catch (Exception e) {
            logger.info("ProcessManagerImpl.java LNo : 307 : Exception Caught", e);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
          } 
          
          if (Constants.CALLBACK_SMS_CONSENT) {
            logger.info("ProcessManagerImpl LNO ::343 , log for CALLBACK_SMS_CONSENT " + lead.getLeadMobileVerificationCodeVerified());
            if ("Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified())) {
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              
              String SMS_TEXT = null;
              if (lead.getLeadApplyingFrom() == 2) {
                SMS_TEXT = Constants.SMS_STRING_NRI;
              } else {
                SMS_TEXT = Constants.SMS_STRING_INDIAN;
              } 
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
              
              if (!Constants.DEPLOYMENT_MODE.equals("live")) {
					//logger.info("Mobile Number: " + (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
              }
				
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                logger.info("ProcessManagerImpl LNO ::361 , OTP service is down:: msg not send");
                json.put("status", "error");
                json.put("message", "OTP service is down");
                return json;
              } 
            } 
          } 
          
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
            logger.info("OTP verfied for mobileNo ::" + lead.getLeadMobileNo());
            json.put("message", "OTP authentication successful");
          } 
          SessionUtil.setLeadId(null);
          SessionUtil.setMobile(null);
          SessionUtil.setISDCode(null);
          SessionUtil.setApplicantName(null);
          SessionUtil.setEmail(null);
          json.put("alertCount", alertCount);
          return json;
        } 
   //     logger.info("OTP is incorrect for mobileNo ::" + lead.getLeadMobileNo());
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == lead.getLeadDataSourceId().intValue() || Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP_MICROSITE.intValue() == lead.getLeadDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtpWantUs|2");
        } 
        json.put("alertCount", alertCount);
        return json;
      } 
      json.put("status", "error");
      json.put("message", "OTP process is down.");
      json.put("alertCount", 0);
    } catch (JSONException e) {
        logger.info("ProcessManagerImpl.java LNo : 317 : Exception Caught", e);
        try {
          json.put("status", "error");
          json.put("message", "OTP process is down");
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("ProcessManagerImpl.java LNo : 323 : Exception Caught", (Throwable)e1);
        } 
      } catch (Exception e) {
      logger.info("ProcessManagerImpl.java LNo : 317 : Exception Caught", e);
      try {
        json.put("status", "error");
        json.put("message", "OTP process is down");
        json.put("alertCount", 0);
      } catch (JSONException e1) {
        logger.info("ProcessManagerImpl.java LNo : 323 : Exception Caught", (Throwable)e1);
      } 
    } 
    return json;
  }
  
  public JSONObject getSmsVerified(String appMobileNo, Integer otp) throws JSONException {
    JSONObject json = new JSONObject();
    logger.info("ProcessManagerImpl LNO :: 575 inside SMS Verification");
    try {
      ApplicationFormPersonalLoan applicationFormPersonalLoan = this.personalLoanService.getApplicationFormPersonalLoanByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormPersonalLoan != null) {
        applicationFormPersonalLoan.setAppMobileVerified("Y");
        applicationFormPersonalLoan = this.personalLoanService.save(applicationFormPersonalLoan);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 570 , found Personal loan application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 573 , not found Personal loan application returning");
      ApplicationFormHomeLoan applicationFormHomeLoan = this.homeLoanService.getApplicationFormHomeLoanByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormHomeLoan != null) {
        logger.info("ProcessManagerImpl LNO :: 591");
        applicationFormHomeLoan.setAppMobileVerified("Y");
        applicationFormHomeLoan = this.homeLoanService.save(applicationFormHomeLoan);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 595 , found home loan application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 598 , not found home loan application");
      ApplicationFormAutoLoan applicationFormAutoLoan = this.autoLoanService.getApplicationFormAutoLoanByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormAutoLoan != null) {
        logger.info("ProcessManagerImpl LNO :: 572");
        applicationFormAutoLoan.setAppMobileVerified("Y");
        applicationFormAutoLoan = this.autoLoanService.save(applicationFormAutoLoan);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 576 , found auto loan application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 579 , not found auto loan application");
      ApplicationFormEducationLoan applicationFormEducationLoan = this.educationLoanService.getApplicationFormEducationLoanByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormEducationLoan != null) {
        logger.info("ProcessManagerImpl LNO :: 596");
        applicationFormEducationLoan.setAppMobileVerified("Y");
        applicationFormEducationLoan = this.educationLoanService.save(applicationFormEducationLoan);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 600 , found education loan application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 603 , not found education loan application returning");
      ApplicationFormCreditCard applicationFormCreditCard = this.creditCardService.getApplicationFormCreditCardByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormCreditCard != null) {
        applicationFormCreditCard.setAppMobileVerified("Y");
        applicationFormCreditCard = this.creditCardService.save(applicationFormCreditCard);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 610 , found Credit Card application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 613 , not found Credit Card application returning");
      ApplicationFormAgriLoan applicationFormAgriLoan = this.agriLoanService.getApplicationFormAgriLoanByMobileAndSmsOtp(appMobileNo, otp);
      if (applicationFormAgriLoan != null) {
        applicationFormAgriLoan.setAppMobileVerified("Y");
        applicationFormAgriLoan = this.agriLoanService.save(applicationFormAgriLoan);
        json.put("status", "success");
        logger.info("ProcessManagerImpl LNO :: 620 , found agri loan application returning");
        return json;
      } 
      logger.info("ProcessManagerImpl LNO :: 623 , not found agri loan application returning");
    } catch (SQLException ex) {
        logger.info("ProcessManagerImpl LNO :: 626");
        json.put("status", "error");
        return json;
    } catch (Exception ex) {
      logger.info("ProcessManagerImpl LNO :: 626");
      json.put("status", "error");
      return json;
    } 
    return json;
  }
  
  public JSONObject getOTPVerified(String loanType, String appMobileNo, Integer otp) throws JSONException {
    JSONObject json = new JSONObject();
    json.put("status", "FAIL");
    json.put("error", "Invalid otp");
    json.put("mobileNumber", appMobileNo);
    try {
      if (loanType.equalsIgnoreCase("01")) {
        ApplicationFormHomeLoan applicationFormHomeLoan = this.homeLoanService.getApplicationFormHomeLoanByMobileAndSmsOtp(appMobileNo, otp);
        if (applicationFormHomeLoan != null) {
          logger.info("ProcessManagerImpl LNO :: 596");
          applicationFormHomeLoan.setAppMobileVerified("Y");
          applicationFormHomeLoan = this.homeLoanService.save(applicationFormHomeLoan);
          json.put("status", "SUCCESS");
          json.put("error", "");
          return json;
        } 
      } else if (loanType.equalsIgnoreCase("03")) {
        ApplicationFormAutoLoan applicationFormAutoLoan = this.autoLoanService.getApplicationFormAutoLoanByMobileAndSmsOtp(appMobileNo, otp);
        if (applicationFormAutoLoan != null) {
          logger.info("ProcessManagerImpl LNO :: 578");
          applicationFormAutoLoan.setAppMobileVerified("Y");
          applicationFormAutoLoan = this.autoLoanService.save(applicationFormAutoLoan);
          json.put("status", "SUCCESS");
          json.put("error", "");
          return json;
        } 
      } else if (loanType.equalsIgnoreCase("02")) {
        ApplicationFormPersonalLoan applicationFormPersonalLoan = this.personalLoanService.getApplicationFormPersonalLoanByMobileAndSmsOtp(appMobileNo, otp);
        if (applicationFormPersonalLoan != null) {
          logger.info("ProcessManagerImpl LNO :: 597");
          applicationFormPersonalLoan.setAppMobileVerified("Y");
          applicationFormPersonalLoan = this.personalLoanService.save(applicationFormPersonalLoan);
          json.put("status", "SUCCESS");
          json.put("error", "");
          return json;
        } 
      } else if (loanType.equalsIgnoreCase("04")) {
        ApplicationFormEducationLoan applicationFormEducationLoan = this.educationLoanService.getApplicationFormEducationLoanByMobileAndSmsOtp(appMobileNo, otp);
        if (applicationFormEducationLoan != null) {
          logger.info("ProcessManagerImpl LNO :: 587");
          applicationFormEducationLoan.setAppMobileVerified("Y");
          applicationFormEducationLoan = this.educationLoanService.save(applicationFormEducationLoan);
          json.put("status", "SUCCESS");
          json.put("error", "");
          return json;
        } 
      } else {
        json.put("status", "FAIL");
        json.put("error", "Invalid otp");
      } 
    } catch (SQLException ex) {
        logger.info("ProcessManagerImpl LNO :: 606", ex);
        json.put("status", "FAIL");
        json.put("error", "Error");
        return json;
    } catch (Exception ex) {
      logger.info("ProcessManagerImpl LNO :: 606", ex);
      json.put("status", "FAIL");
      json.put("error", "Error");
      return json;
    } 
    return json;
  }
}

