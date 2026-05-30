package com.mintstreet.loan.autoloan.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.bo.AccountTypeTermOverDraft;
import com.mintstreet.common.bo.CommonQuote;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.engine.CommonEngine;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.BankLmsUserRole;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.status.StatusManager;
import com.mintstreet.common.status.StatusManagerResponse;
import com.mintstreet.common.status.StatusRequest;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.MapperUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanCalls;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.product.entity.AlProduct;

public class AutoLoanHelper {
  private static final Logger logger = LogManager.getLogger(AutoLoanHelper.class.getName());
  
  @Autowired
  private AutoLoanService autoLoanService;
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  @Autowired
  private CommonEngine commonEngine;
  
  public ApplicationFormAutoLoanQuote insertLoanQuote(ApplicationFormAutoLoanQuote quote, Integer bankLmsUser, Integer trackVisitId) {
    try {
      if (quote == null) {
        quote = new ApplicationFormAutoLoanQuote();
        quote.setError("Sorry for the inconvenience, Please start again.");
        return quote;
      } 
      if (!ValidatorUtil.isValid(quote.getLoanQuoteResidentTypeId())) {
        quote.setError("Please select the resident type.|quote.loanQuoteResidentTypeId|1");
        return quote;
      } 
      if (quote.getLoanQuoteResidentTypeId().intValue() == 1) {
        if (!ValidatorUtil.isValid(quote.getLoanQuoteStateId())) {
          quote.setError("Please select the state.|quote.loanQuoteResidentTypeId|1");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuoteCityId())) {
          quote.setError("Please select the state.|quote.loanQuoteStateId|1");
          return quote;
        } 
        if (quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER) && 
          !ValidatorUtil.isValid(quote.getLoanQuoteDistrictId())) {
          quote.setError("Please select the city.|quote.loanQuoteCityId|1");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuoteBranchId())) {
          if (quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER)) {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteDistrictId|1");
          } else {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteCityId|1");
          } 
          return quote;
        } 
      } 
      if (quote.getLoanQuoteCoapplicantFirstRelationshipId() != null)
        quote.setLoanQuoteCoapplicantFirstId(Integer.valueOf(1)); 
      if (quote.getLoanQuoteCarTypeId() != null)
        quote.setLoanQuoteCarTypeId(quote.getLoanQuoteCarTypeId()); 
      quote = getUpdateQuote(quote);
      if (quote.getLoanQuoteResidentTypeId().intValue() == 2 && 
        quote.getLoanQuoteCountryId() == null) {
        quote.setError("Please select resident type.|quote.loanQuoteResidentTypeId|1");
        return quote;
      } 
      if (quote.getLoanQuoteCoapplicantFirstResidentTypeId() != null && 
        quote.getLoanQuoteCoapplicantFirstResidentTypeId().intValue() == 2 && 
        quote.getLoanQuoteCoapplicantFirstCountryId() == null) {
        quote.setError("Please select coapplicant resident type.|quote.loanQuoteCoapplicantFirstResidentTypeId|1");
        return quote;
      } 
      quote.setLoanQuoteLeadId(SessionUtil.getLeadId());
      quote.setLoanQuoteCreatedLmsUserId((bankLmsUser != null) ? bankLmsUser : Constants.OTHER_USER_ID);
      quote.setLoanQuoteEntryTime(new Date());
      quote.setLoanQuoteUpdatedTime(new Date());
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      quote.setLoanQuoteVisitId(trackVisitId);
      quote.setLoanQuoteActive("Y");
      quote.setLoanQuoteDeleted("N");

      logger.info("bankLmsUserId:: " + bankLmsUser + "::: and quote consentId is:: " + quote.getLoanQuoteConsentId());
      //get active consentId for direct web leads (other than dsr page)
      if (Constants.OTHER_USER_ID.equals(bankLmsUser)) {
    	  Consent consent = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB");
    	  Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
    	  quote.setLoanQuoteConsentId(consentId); 
      } else {
    	  if (quote.getLoanQuoteConsentId() != null) {
    		  quote.setLoanQuoteConsentId(quote.getLoanQuoteConsentId()); 
    	  }
      }
      logger.info("ConsentId before save in quote table:::" + quote.getLoanQuoteConsentId());
      
      return this.autoLoanService.save(quote);
    } catch (SQLException e) {
      logger.info("AutoLoanHelper.java LNo : 164 : Exception Caught", e);
      return null;
    } catch (Exception e) {
      logger.info("AutoLoanHelper.java LNo : 164 : Exception Caught", e);
      return null;
    } 
  }
  
  public ApplicationFormAutoLoan insertAppLoan(ApplicationFormAutoLoanQuote quote, ApplicationFormAutoLoan application, Integer bankLmsUserId, Integer lmsUserIntermediaryId) {
    try {
      if (quote == null) {
        if (application == null)
          application = new ApplicationFormAutoLoan(); 
        application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
        return application;
      } 
      if (application != null) {
        SessionUtil.setISDCode(application.getAppISDCode());
        SessionUtil.setMobile(application.getAppMobileNo());
        quote.setAppApplyingFrom(application.getAppApplyingFrom());
        if (!"Y".equalsIgnoreCase(application.getAppMobileVerified())) {
          application.setAppMobileVerified("N");
          String isdCode = application.getAppISDCode();
          if (isdCode != null && "91".equals(isdCode)) {
            application.setAppResTypeAtVerified(Integer.valueOf(1));
          } else {
            application.setAppResTypeAtVerified(Integer.valueOf(2));
          } 
          application.setAppOfficeCityId(quote.getLoanQuoteCityId());
          if (application.getAppApplyingFrom() == 2) {
            if (quote.getAppNRIMobileNo() != null) {
              application.setAppISDCode(quote.getAppISDCode());
              application.setAppMobileNo(quote.getAppNRIMobileNo());
              SessionUtil.setISDCode(quote.getAppISDCode());
              SessionUtil.setMobile(quote.getAppNRIMobileNo());
            } 
          } else if (quote.getAppMobile() != null) {
            application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
            application.setAppMobileNo(quote.getAppMobile());
            SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
            SessionUtil.setMobile(quote.getAppMobile());
          } 
        } 
        if (!"Y".equalsIgnoreCase(application.getAppEmailVerified()))
          application.setAppEmailVerified("N"); 
      } else {
        application = new ApplicationFormAutoLoan();
        if (quote.getAppApplyingFrom() == 2) {
          if (quote.getAppNRIMobileNo() != null) {
            application.setAppISDCode(quote.getAppISDCode());
            application.setAppMobileNo(quote.getAppNRIMobileNo());
            SessionUtil.setISDCode(quote.getAppISDCode());
            SessionUtil.setMobile(quote.getAppNRIMobileNo());
          } 
        } else if (quote.getAppMobile() != null) {
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          application.setAppMobileNo(quote.getAppMobile());
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setMobile(quote.getAppMobile());
        } 
        application.setAppOtpMobileAlertsCount(Integer.valueOf(0));
        application.setAppBankId(Constants.LEAD_BANK_ID);
        application.setAppFulfillmentGroupId(Constants.LEAD_FULFILLMENT_GROUP_ID);
        application.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(application.getAppMobileNo()));
        application.setAppMobileVerified("N");
        application.setAppEmailVerificationCode(this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
        application.setAppEmailVerified("N");
        if (application.getAppISDCode() != null && "91".equals(application.getAppISDCode())) {
          application.setAppResTypeAtVerified(Integer.valueOf(1));
        } else {
          application.setAppResTypeAtVerified(Integer.valueOf(2));
        } 
        if ("Y".equals(quote.getLoanQuoteHaveSalaryAccountWithSbi()))
          application.setAppSalaryBankId(Constants.LEAD_BANK_ID); 
      } 
      if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 1) {
        if (ValidatorUtil.isValid(quote.getLoanQuoteStateId())) {
          application.setAppStateId(quote.getLoanQuoteStateId());
        } else {
          application.setError("Please select the resident type.|quote.loanQuoteResidentTypeId|1");
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuoteCityId())) {
          application.setAppCityId(quote.getLoanQuoteCityId());
        } else {
          application.setError("Please select the State.|quote.loanQuoteStateId|1");
          return application;
        } 
        if (quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuoteDistrictId())) {
            application.setAppDistrictId(quote.getLoanQuoteDistrictId());
          } else {
            application.setError("Please select the city.|quote.getLoanQuoteCityId|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuoteBranchId())) {
          application.setAppBranchId(quote.getLoanQuoteBranchId());
        } else {
          if (quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|appForm.getLoanQuoteDistrictId|1");
          } else {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.getLoanQuoteCityId|1");
          } 
          return application;
        } 
      } 
      application.setAppGender(quote.getLoanQuoteGender());
      application.setAppApplyingFrom(quote.getAppApplyingFrom());
      if (application.getAppEntryTime() == null)
        application.setAppEntryTime(new Date()); 
      if (application.getAppEntryDate() == null)
        application.setAppEntryDate(new Date()); 
      if (!ValidatorUtil.isValid(application.getAppLoanStatusId()))
        if (SessionUtil.getApplicationType() != null) {
          if (SessionUtil.getApplicationType().intValue() == 0) {
            application.setAppContactCenterLocation(1);
            if (SessionUtil.getBankLMSUser() == null) {
              application.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE43_ID));
            } else {
              application.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE141_ID));
            } 
          } else if (SessionUtil.getApplicationType().intValue() == 1) {
            application.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE141_ID));
          } else if (SessionUtil.getApplicationType().intValue() == 2) {
            application.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE141_ID));
          } 
        } else {
          application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return application;
        }  
      if (!ValidatorUtil.isValid(application.getAppLoanStatusId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (!ValidatorUtil.isValid(application.getAppDataSourceId()))
        if (SessionUtil.getApplicationType() != null) {
          if (SessionUtil.getApplicationType().intValue() == 0) {
            application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
          } else if (SessionUtil.getApplicationType().intValue() == 1) {
            if (SessionUtil.getLeadId() != null) {
              ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
              if (lead != null && lead.getLeadDataSourceId() != null) {
                application.setAppDataSourceId(lead.getLeadDataSourceId());
              } else {
                application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
              } 
            } else {
              application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
            } 
          } else if (SessionUtil.getApplicationType().intValue() == 2) {
            application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
          } 
        } else {
          application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return application;
        }  
      if (!ValidatorUtil.isValid(application.getAppDataSourceId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (application.getAppSubTypeId() == null)
        application.setAppSubTypeId(Constants.APP_APP_SUB_TYPE_ID_NORMAL); 
      if (SessionUtil.getLMSIntermediaryRelation() != null && !SessionUtil.getLMSIntermediaryRelation().equalsIgnoreCase("null"))
        application.setAppLoanDealerName(SessionUtil.getLMSIntermediaryRelation()); 
      if (quote.getLoanQuoteFirstName() != null)
        application.setAppFirstName(quote.getLoanQuoteFirstName()); 
      if (application.getAppHotLeadCreatedLmsUserId() == null && 
        SessionUtil.getApplicationType() != null && 
        SessionUtil.getApplicationType().intValue() == 2)
        application.setAppHotLeadCreatedLmsUserId(Integer.valueOf((bankLmsUserId != null) ? bankLmsUserId.intValue() : 0)); 
      if (!"Y".equalsIgnoreCase(application.getAppMobileVerified()) && 
        SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 1 && 
        SessionUtil.getLeadId() != null) {
        ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
        if (lead != null && 
          lead.getLeadMobileVerificationCodeVerified() != null && "Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified()))
          application.setAppMobileVerified("Y"); 
      } 
      if (quote.getLoanQuoteLoanAccountType() != null && quote.getLoanQuoteLoanAccountType().intValue() > 0) {
        application.setAppLoanAccountType(quote.getLoanQuoteLoanAccountType());
      } else {
        application.setAppLoanAccountType(Integer.valueOf(1));
      } 
      if (quote.getLoanQuoteCoapplicantFirstRelationshipId() != null) {
        application.setAppCoapplicantTypeId_1(Integer.valueOf(1));
      } else {
        application.setAppCoapplicantTypeId_1(Integer.valueOf(0));
      }
      if(quote.getAppMobile() != null) {
    	  application.setAppMobileNumberMask(quote.getAppMobile().replaceAll("\\d(?=\\d{4})", "*"));
      }
      if(quote.getAppNRIMobileNo() != null) {
    	  if(quote.getAppNRIMobileNo().length() <= 8)
    		  application.setAppMobileNumberMask(quote.getAppNRIMobileNo().replaceAll("\\d(?=\\d{2})", "*"));
    	  else
    		  application.setAppMobileNumberMask(quote.getAppNRIMobileNo().replaceAll("\\d(?=\\d{4})", "*"));
      }
      application.setAppEmploymentType(quote.getLoanQuoteEmploymentTypeId());
      if (quote.getLoanQuoteEmployerName() != null)
        application.setAppLoanEmployerName(quote.getLoanQuoteEmployerName()); 
      application.setAppLeadUpdateTime(new Date());
      if (quote.getLoanQuoteDateOfBirthDT() != null)
        application.setAppDobDT(quote.getLoanQuoteDateOfBirthDT()); 
      application.setAppQuoteId(quote.getLoanQuoteId());
      if (quote.getLoanQuoteLoanTenure() != null)
        application.setAppLoanTenure(quote.getLoanQuoteLoanTenure()); 
      application.setAppActive("Y");
      application.setAppDeleted("N");
      if (quote.getAppEmail() != null) {
        application.setAppWorkEmail(quote.getAppEmail());
        SessionUtil.setEmail(quote.getAppEmail());
      } 
      if (quote.getLoanQuoteCoapplicantFirstCityId() != null && 
        !quote.getLoanQuoteCoapplicantFirstCityId().equals(Constants.OTHER_ID_INTEGER)) {
        application.setAppCoapplicantCity_id_1(quote.getLoanQuoteCoapplicantFirstCityId());
        if (quote.getLoanQuoteCoapplicantFirstCityId() != null && quote.getLoanQuoteCoapplicantFirstCityId().intValue() > 0 && !Constants.OTHER_ID_INTEGER.equals(quote.getLoanQuoteCoapplicantFirstCityId())) {
          MasterCity city = this.commonService.getCityById(quote.getLoanQuoteCoapplicantFirstCityId());
          if (city != null)
            application.setAppCoapplicantState_id_1(city.getCityStateId()); 
        } 
      } 
      if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 1) {
        if (quote.getLoanQuoteCityId() != null && 
          !quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER)) {
          List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.AUTO_LOAN_ID, null, quote.getLoanQuoteCityId(), null, null, null, null, null);
          if (branches != null && !branches.isEmpty()) {
            if (branches.size() == 1) {
              MasterBranch masterBranch = branches.get(0);
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.AUTO_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
              } 
            } 
          } else if (quote.getLoanQuoteBranchId() != null) {
            MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuoteBranchId());
            if (masterBranch != null) {
              Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.AUTO_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
              application.setAppSalesTeamId(salesTeamId);
            } 
          } 
        } 
        if (quote.getLoanQuoteBranchId() != null) {
          if (bankLmsUserId != null) {
            if (application.getAppPreviousBranchId() != null && application.getAppBranchId() != null) {
              if (!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppBranchId().toString())) {
                StringBuilder message = new StringBuilder("Application moved from ");
                MasterBranch masterBranch1 = this.commonService.getBranchById(application.getAppPreviousBranchId());
                if (masterBranch1 != null)
                  message.append(String.valueOf(masterBranch1.getBranchName()) + " (" + masterBranch1.getBranchCode() + ") "); 
                application.setAppPreviousBranchId(application.getAppBranchId());
                masterBranch1 = this.commonService.getBranchById(application.getAppBranchId());
                if (masterBranch1 != null)
                  message.append(" to " + masterBranch1.getBranchName() + " (" + masterBranch1.getBranchCode() + ") "); 
                if (application.getAppDataSourceId() != null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)) {
                  if (SessionUtil.getLeadId() == null) {
                    ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.AUTO_LOAN_ID);
                    this.commonService.insertCallLog((lead != null) ? lead.getLeadId() : null, (bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID, application.getAppLoanStatusId(), message.toString());
                  } 
                } else {
                  insertCallLog(application.getAppSeqId(), ((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID).intValue(), application.getAppLoanStatusId().intValue(), message.toString(), null, true);
                } 
                application.setAppAssignedLmsSalesUserId(null);
              } 
            } else {
              application.setAppPreviousBranchId(application.getAppBranchId());
            } 
          } else {
            application.setAppPreviousBranchId(application.getAppBranchId());
          } 
          MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuoteBranchId());
          if (masterBranch != null) {
            Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.AUTO_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
            application.setAppSalesTeamId(salesTeamId);
          } 
        } 
      } else if (quote.getLoanQuoteResidentTypeId().intValue() == 2 && 
        bankLmsUserId == null) {
        application.setAppStateId(Integer.valueOf(0));
        application.setAppCityId(Integer.valueOf(0));
        application.setAppDistrictId(Integer.valueOf(0));
      } 
      if (bankLmsUserId != null) {
        if (application.getAppCreatedLmsUserId() == null) {
          application.setAppAmendedLmsUserId(bankLmsUserId);
        } else {
          application.setAppAmendedLmsUserId(bankLmsUserId);
        } 
        if (lmsUserIntermediaryId != null)
          application.setAppIntermediaryId(lmsUserIntermediaryId); 
        if (SessionUtil.getLeadId() == null)
          application.setAppHotLeadCreatedLmsUserId(bankLmsUserId); 
      } 
      if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2)
        application.setAppCountryId(quote.getLoanQuoteCountryId()); 
      if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 1) {
        if (ValidatorUtil.isValid(application.getAppBranchId())) {
          Integer[] circleIdNetworkModuleRegionId = this.commonService.getCircleIdNetworkModuleRegionByBranchId(application.getAppBranchId());
          if (ValidatorUtil.isValid(circleIdNetworkModuleRegionId[0]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[1]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[2]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[3])) {
            application.setAppCircleId(circleIdNetworkModuleRegionId[0]);
            application.setAppNetworkId(circleIdNetworkModuleRegionId[1]);
            application.setAppModuleId(circleIdNetworkModuleRegionId[2]);
            application.setAppRegionId(circleIdNetworkModuleRegionId[3]);
          } else {
            application.setError(Constants.SORRY_FOR_INCONVENIENCE);
            return application;
          } 
        } else {
          logger.info("AutoLoanHelper.java LNo :: 494 :: 1");
          application.setError(Constants.SORRY_FOR_INCONVENIENCE);
          return application;
        } 
      } else {
        application.setAppCircleId(Integer.valueOf(0));
        application.setAppNetworkId(Integer.valueOf(0));
        application.setAppModuleId(Integer.valueOf(0));
        application.setAppRegionId(Integer.valueOf(0));
      } 
      if (!ValidatorUtil.isValid(application.getAppLoanStatusId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (!ValidatorUtil.isValid(application.getAppDataSourceId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (quote.getLoanQuoteMiddleName() != null)
        application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
      if (quote.getLoanQuoteLastName() != null)
        application.setAppLastName(quote.getLoanQuoteLastName()); 
      if (bankLmsUserId != null) {
        if (this.commonService.isBankUser(bankLmsUserId))
          application.setAppAssignedLmsSalesUserId(bankLmsUserId); 
      } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null && 
        this.commonService.isBankUser(bankLmsUserId)) {
        application.setAppAssignedLmsSalesUserId(SessionUtil.getBankLMSUser().getLmsUserId());
      } 
      
		logger.info(" cheking the value alternate mobile number inside the helper class  " +quote.getAlternateMobileNumber());
		if(quote.getAlternateMobileNumber() !=null) {
			application.setAppAlternateMobileNumber(quote.getAlternateMobileNumber());
			application.setAppAltISDCode(quote.getAppAltISDCode());
			SessionUtil.setalternateMobileNumber(quote.getAppMobile());
	
		}
		
	    //save consent id in main table
	    if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
	    	application.setAppConsentId(quote.getLoanQuoteConsentId());
	    }

      application = this.autoLoanService.save(application);
      return application;
    } catch (SQLException e) {
      logger.info("AutoLoanHelper.java LNo : 542 : Exception Caught", e);
      return null;
    } catch (Exception e) {
      logger.info("AutoLoanHelper.java LNo : 542 : Exception Caught", e);
      return null;
    } 
  }
  
  public ApplicationFormAutoLoanQuote saveCoApplicantDetails(ApplicationFormAutoLoanQuote quoteNew) {
    try {
      if (quoteNew == null)
        return null; 
      Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
      if (appSeqId == null)
        return null; 
      ApplicationFormAutoLoan application = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
      if (application == null)
        return null; 
      ApplicationFormAutoLoanQuote quote = this.autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(application.getAppQuoteId());
      if (quote == null)
        return null; 
      if (quoteNew.getLoanQuoteCoapplicantFirstRelationshipId() != null) {
        quote.setLoanQuoteCoapplicantFirstId(Integer.valueOf(1));
        quote.setLoanQuoteCoapplicantFirstRelationshipId(quoteNew.getLoanQuoteCoapplicantFirstRelationshipId());
        quote.setLoanQuoteCoapplicantFirstResidentTypeId(quoteNew.getLoanQuoteCoapplicantFirstResidentTypeId());
        if (quoteNew != null && quoteNew.getLoanQuoteCoapplicantFirstResidentTypeId() != null && quoteNew.getLoanQuoteCoapplicantFirstResidentTypeId().intValue() == 1) {
          quote.setLoanQuoteCoapplicantFirstCityId(quoteNew.getLoanQuoteCoapplicantFirstCityId());
        } else if (quoteNew != null && quoteNew.getLoanQuoteCoapplicantFirstResidentTypeId() != null && quoteNew.getLoanQuoteCoapplicantFirstResidentTypeId().intValue() == 2) {
          quote.setLoanQuoteCoapplicantFirstCityId(null);
          quote.setLoanQuoteCoapplicantFirstCountryId(quoteNew.getLoanQuoteCoapplicantFirstCountryId());
        } 
        quote.setLoanQuoteCoapplicantFirstDateOfBirth(quoteNew.getLoanQuoteCoapplicantFirstDateOfBirth());
        if (quote.getLoanQuoteCoapplicantFirstDateOfBirth() != null)
          quote.setLoanQuoteCoapplicantFirstDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantFirstDateOfBirth(), "MM/dd/yyyy")); 
        quote.setLoanQuoteCoapplicantFirstWorkExperience(quoteNew.getLoanQuoteCoapplicantFirstWorkExperience());
        quote.setLoanQuoteCoapplicantFirstEmploymentTypeId(quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId());
        if (quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 9) {
          quote.setLoanQuoteCoapplicantFirstGrossMonthlyIncome(quoteNew.getLoanQuoteCoapplicantFirstGrossMonthlyIncome());
          quote.setLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi(quoteNew.getLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi());
          quote.setLoanQuoteCoapplicantFirstCoEmployerName(quoteNew.getLoanQuoteCoapplicantFirstCoEmployerName());
          quote.setLoanQuoteCoapplicantFirstMonthlySalary(quoteNew.getLoanQuoteCoapplicantFirstMonthlySalary());
          quote.setLoanQuoteCoapplicantFirstVariableMonthPay(quoteNew.getLoanQuoteCoapplicantFirstVariableMonthPay());
          quote.setLoanQuoteCoapplicantFirstOtherIncome(quoteNew.getLoanQuoteCoapplicantFirstOtherIncome());
          quote.setLoanQuoteCoapplicantFirstPreEMIs(quoteNew.getLoanQuoteCoapplicantFirstPreEMIs());
          quote.setLoanQuoteCoapplicantFirstRetirementAge(quoteNew.getLoanQuoteCoapplicantFirstRetirementAge());
        } else if (quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 10 || quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 11) {
          quote.setLoanQuoteCoapplicantFirstProfitAfterTax(quoteNew.getLoanQuoteCoapplicantFirstProfitAfterTax());
          quote.setLoanQuoteCoapplicantFirstDepreciatiation(quoteNew.getLoanQuoteCoapplicantFirstDepreciatiation());
          quote.setLoanQuoteCoapplicantFirstPreEMIs(quoteNew.getLoanQuoteCoapplicantFirstPreEMIs());
        } else if (quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 12) {
          quote.setLoanQuoteCoapplicantFirstNetAnnualIncome(quoteNew.getLoanQuoteCoapplicantFirstNetAnnualIncome());
          quote.setLoanQuoteCoapplicantFirstOtherIncome(quoteNew.getLoanQuoteCoapplicantFirstOtherIncome());
        } else if (quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 13) {
          quote.setLoanQuoteCoapplicantFirstNetMonthlyPension(quoteNew.getLoanQuoteCoapplicantFirstNetMonthlyPension());
          quote.setLoanQuoteCoapplicantFirstNetAnnualIncome(quoteNew.getLoanQuoteCoapplicantFirstNetAnnualIncome());
          quote.setLoanQuoteCoapplicantFirstPreEMIs(quoteNew.getLoanQuoteCoapplicantFirstPreEMIs());
        } else if (quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 14 || quoteNew.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 15) {
          quote.setLoanQuoteCoapplicantFirstIncomeFromOtherSource(quoteNew.getLoanQuoteCoapplicantFirstIncomeFromOtherSource());
        } 
        if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 9) {
          quote.setLoanQuoteCoapplicantFirstId(null);
          quote.setLoanQuoteCoapplicantFirstRelationshipId(null);
          quote.setLoanQuoteCoapplicantFirstResidentTypeId(null);
          quote.setLoanQuoteCoapplicantFirstCityId(null);
          quote.setLoanQuoteCoapplicantFirstCountryId(null);
          quote.setLoanQuoteCoapplicantFirstDateOfBirth(null);
          quote.setLoanQuoteCoapplicantFirstWorkExperience(null);
          quote.setLoanQuoteCoapplicantFirstEmploymentTypeId(null);
          quote.setLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi(null);
          quote.setLoanQuoteCoapplicantFirstCoEmployerName(null);
          quote.setLoanQuoteCoapplicantFirstMonthlySalary(null);
          quote.setLoanQuoteCoapplicantFirstVariableMonthPay(null);
          quote.setLoanQuoteCoapplicantFirstOtherIncome(null);
          quote.setLoanQuoteCoapplicantFirstPreEMIs(null);
          quote.setLoanQuoteCoapplicantFirstRetirementAge(null);
        } 
        quote.setLoanQuoteCoapplicantFirstPreEMIs(quoteNew.getLoanQuoteCoapplicantFirstPreEMIs());
      } 
      quote = this.autoLoanService.save(quote);
      return quote;
    } catch (SQLException e) {
      logger.info("AutoLoanHelper.java LNo : 632 : Exception Caught", e);
      return null;
    } catch (Exception e) {
      logger.info("AutoLoanHelper.java LNo : 632 : Exception Caught", e);
      return null;
    } 
  }
  
  public void insertCallLog(Integer callApplicationId, int callUserId, int callStatusId, String message, Date callDocPickupTime, boolean displayLog) {
    try {
      ApplicationFormAutoLoanCalls callsLog = new ApplicationFormAutoLoanCalls();
      callsLog.setCallApplicationId(callApplicationId);
      if (displayLog) {
        callsLog.setCallActive("Y");
      } else {
        callsLog.setCallActive("N");
      } 
      callsLog.setCallDeleted("N");
      callsLog.setCallIsGenericAlert("N");
      callsLog.setCallEndTime(new Date());
      if (callUserId == 0) {
        callsLog.setCallLmsUserId(Constants.OTHER_ID_INTEGER);
      } else {
        callsLog.setCallLmsUserId(Integer.valueOf(callUserId));
      } 
      callsLog.setCallStatusId(Integer.valueOf(callStatusId));
      callsLog.setCallApplicationBankId(Constants.LEAD_BANK_ID);
      if (message == null) {
        LoanStatus loanStatus = this.commonService.getLoanStatusByLoanStatusId(Integer.valueOf(callStatusId));
        callsLog.setCallDescription((loanStatus != null) ? loanStatus.getLoanStatusTitle() : null);
      } else {
        callsLog.setCallDescription(message);
      } 
      if (callDocPickupTime != null)
        callsLog.setCallDocPickupTime(callDocPickupTime); 
      callsLog = this.autoLoanService.save(callsLog);
    } catch (SQLException e) {
        logger.info("AutoLoanHelper.java LNo : 673 : Exception Caught", e);
    } catch (Exception e) {
      logger.info("AutoLoanHelper.java LNo : 673 : Exception Caught", e);
    } 
  }
  
  public LoanScenarioBean callBREAgain(ApplicationFormAutoLoan application, ApplicationFormAutoLoanQuote quote) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      Double previousPreEmi = null;
      Double coapplicantFirstDepreciatiation = null;
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
        if (ValidatorUtil.isValid(quote.getLoanQuotePreEMIs()))
          previousPreEmi = quote.getLoanQuotePreEMIs(); 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreEMIsOther()))
          quote.setLoanQuotePreEMIs(Double.valueOf(((previousPreEmi != null) ? previousPreEmi.doubleValue() : 0.0D) + quote.getLoanQuotePreEMIsOther().doubleValue())); 
        if (ValidatorUtil.isValid(quote.getLoanQuoteCoapplicantFirstDepreciatiation())) {
          coapplicantFirstDepreciatiation = quote.getLoanQuoteCoapplicantFirstDepreciatiation();
          quote.setLoanQuoteCoapplicantFirstDepreciatiation(Double.valueOf(quote.getLoanQuoteCoapplicantFirstDepreciatiation().doubleValue() / 12.0D));
        } 
        Integer preserveLoanAccountType = Integer.valueOf(1);
        if (quote.getLoanQuoteLoanAccountType() != null && quote.getLoanQuoteLoanAccountType().intValue() == 2) {
          preserveLoanAccountType = quote.getLoanQuoteLoanAccountType();
          quote.setLoanQuoteLoanAccountType(Integer.valueOf(1));
        } 
        quote = getUpdateQuote(quote);
        JSONObject engineRequest = JSONUtil.beanObjectToJSONObjct(quote);
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
          quote.setLoanQuotePreEMIs(previousPreEmi);
          quote.setLoanQuoteCoapplicantFirstDepreciatiation(coapplicantFirstDepreciatiation);
        } 
        if (quote.getLoanQuoteLoanAccountType() != null)
          quote.setLoanQuoteLoanAccountType(preserveLoanAccountType); 
        engineRequest = this.SbiUtil.getDBCredentialForHelper(engineRequest);
        JSONObject engineResponseJson = null;
        if (Constants.IS_ENGINE_OBF) {
          JSONObject finalEngineRequest = MapperUtil.convertAutoLoan(engineRequest);
          engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + finalEngineRequest, Constants.AUTO_LOAN_ID);
        } else {
          engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + engineRequest, Constants.AUTO_LOAN_ID);
        } 
        loanScenarioBean = (LoanScenarioBean)JSONUtil.getObjctFromJSON(loanScenarioBean, engineResponseJson.toString());
        application.setAppOfferJsonData(engineResponseJson.toString());
        loanScenarioBean.setApplicationAL(application);
      } 
    } catch (JSONException e) {
        logger.info("AutoLoanProcessImpl.java LNo: 1620 :: Rule Calling ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
    } catch (Exception e) {
      logger.info("AutoLoanProcessImpl.java LNo: 1620 :: Rule Calling ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
    } 
    return loanScenarioBean;
  }
  
  public LoanScenarioBean callBRE(ApplicationFormAutoLoan application, ApplicationFormAutoLoanQuote quote, BankLmsUser bankLmsUser, Integer previousQuoteId, Integer trackVisitId, String ajaxPostUrl, boolean isLoggin) throws SQLException {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    loanScenarioBean = callBREAgain(application, quote);
    if (loanScenarioBean.getStatus().intValue() == 0)
      return loanScenarioBean; 
    if (!isLoggin)
      return loanScenarioBean; 
    if (loanScenarioBean.getStatus().intValue() != 1 && 
      isLoggin) {
      String noOfferReason = loanScenarioBean.getNoOfferReason();
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(false);
      statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
      statusRequest.setState(1);
      statusRequest.setBankLMSUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0);
      statusRequest.setRsmDecision(0);
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
      statusRequest.setAppMobileVerified(application.getAppMobileVerified());
      statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
      statusRequest.setApplicationSubTypeId(application.getAppSubTypeId().intValue());
      StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
      if (statusManagerResponse.getStatus() != 0) {
        application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
        if (!statusManagerResponse.isPreserveData()) {
          application.setAppLoanAmount(null);
          application.setAppLoanAccountType(null);
          application.setAppLoanInterestRate(null);
          application.setAppLoanTenure(null);
          application.setAppLoanProcessingFee(null);
          application.setAppAutoLoanId(null);
          application = this.autoLoanService.save(application);
        } 
      } else if (application.getAppLoanStatusId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage("Sorry for the inconvenience, Please click <a href='" + Constants.PORT + Constants.CONTEXT + ajaxPostUrl + "'>here</a> to start again.");
        return loanScenarioBean;
      } 
      if (statusManagerResponse.isPreserveData() && previousQuoteId.intValue() > 0) {
        application.setAppQuoteId(previousQuoteId);
        application = this.autoLoanService.save(application);
      } 
      if (statusManagerResponse.isEligibleToInsertLog())
        insertCallLog(application.getAppSeqId(), (bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0, statusManagerResponse.getStatusCallLogs(), noOfferReason, null, true); 
      if (SessionUtil.getApplicationType() != null)
        if (SessionUtil.getApplicationType().intValue() != 0)
          if (SessionUtil.getApplicationType().intValue() == 1) {
            if (SessionUtil.getLeadId() != null) {
              ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
              if (statusManagerResponse.getStatusLead() != 0) {
                lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
                lead.setLeadAppSeqId(application.getAppSeqId());
                lead = this.commonService.save(lead);
              } 
              application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
              application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
              if (lead.getLeadCreatedLmsUserId() != null) {
                if (application.getAppCreatedLmsUserId() == null)
                  application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
              } else if (application.getAppCreatedLmsUserId() == null && 
                bankLmsUser != null) {
                application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
              } 
            } 
          } else if (SessionUtil.getApplicationType().intValue() == 2) {
            SessionUtil.setApplicationType(Integer.valueOf(1));
            ApplicationFormLead lead = null;
            if (SessionUtil.getLeadId() != null) {
              lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            } else {
              lead = new ApplicationFormLead();
            } 
            lead.setLeadFirstName((application.getAppFirstName() != null) ? application.getAppFirstName() : null);
            lead.setLeadApplyingFrom(application.getAppApplyingFrom());
            lead.setLeadIsdCode((application.getAppISDCode() != null) ? application.getAppISDCode() : Constants.COUNTRY_CODE_INDIA);
            lead.setLeadMobileNo((application.getAppMobileNo() != null) ? application.getAppMobileNo() : null);
            lead.setLeadCityId((quote.getLoanQuoteCityId() != null) ? quote.getLoanQuoteCityId() : Constants.OTHER_ID_INTEGER);
            lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
            lead.setLeadProductTypeId(Constants.AUTO_LOAN_ID);
            if (statusManagerResponse.getStatus() != 0)
              lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead())); 
            lead.setLeadMobileAlertCount(Integer.valueOf(1));
            lead.setLeadMobileVerificationCode(Integer.valueOf(Integer.parseInt(Constants.DUMMY_MOBILE_OTP)));
            lead.setLeadVisitId(trackVisitId);
            lead.setLeadActive("Y");
            lead.setLeadDeleted("N");
            lead.setLeadIntermediaryId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
            lead.setLeadCreatedLmsUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
            lead.setLeadMobileVerificationCodeVerified("N");
            lead.setLeadReceiveDatetime(new Date());
            lead.setLeadEntryTime(new Date());
            lead.setLeadEntryDate(new Date());
            lead.setLeadLastUpdated(new Date());
            if (lead.getLeadSourceId() == null)
              lead.setLeadSourceId(Constants.LEAD_SOURCE_ID); 
            if (lead.getLeadDataSourceId() == null)
              lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT); 
            if (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) {
              List<BankLmsUserRole> lmsUserRole = this.commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
              if (lmsUserRole != null)
                for (BankLmsUserRole bankLmsUserRole : lmsUserRole) {
                  if (lmsUserRole != null && bankLmsUserRole.getLmsRoleTypeId() != null && (3 == bankLmsUserRole.getLmsRoleTypeId().intValue() || 4 == bankLmsUserRole.getLmsRoleTypeId().intValue()))
                    lead.setLeadAssignedLmsUserId(bankLmsUser.getLmsUserId()); 
                }  
            } 
            lead.setLeadAppSeqId(application.getAppSeqId());
            if (bankLmsUser != null)
              if (bankLmsUser.getLmsUserType().intValue() == 1 || bankLmsUser.getLmsUserType().intValue() == 3) {
                lead.setLeadAppContactCenterLocation(0);
              } else {
                Integer lmsUserLocationId = this.commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
                if (lmsUserLocationId != null) {
                  lead.setLeadAppContactCenterLocation(lmsUserLocationId.intValue());
                } else {
                  lead.setLeadAppContactCenterLocation(1);
                } 
              }  
            lead = this.commonService.save(lead);
            SessionUtil.setLeadId(lead.getLeadId());
            application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
            application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
            if (lead.getLeadCreatedLmsUserId() != null) {
              if (application.getAppCreatedLmsUserId() == null)
                application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
            } else if (application.getAppCreatedLmsUserId() == null && 
              bankLmsUser != null) {
              application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
            } 
          }   
      return loanScenarioBean;
    } 
    if (SessionUtil.getApplicationType() != null && 
      isLoggin) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
      statusRequest.setState(1);
      statusRequest.setBankLMSUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0);
      statusRequest.setRsmDecision(0);
      statusRequest.setAppMobileVerified(application.getAppMobileVerified());
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
      statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
      statusRequest.setRequestFirstTime((previousQuoteId.intValue() == 0));
      statusRequest.setApplicationSubTypeId(application.getAppSubTypeId().intValue());
      StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
      if (statusManagerResponse.getStatus() != 0) {
        application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
      } else if (application.getAppLoanStatusId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (statusManagerResponse.isEligibleToInsertLog())
        insertCallLog(application.getAppSeqId(), ((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID).intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
      if (SessionUtil.getApplicationType().intValue() != 0)
        if (SessionUtil.getApplicationType().intValue() == 1) {
          if (SessionUtil.getLeadId() != null) {
            ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            if (statusManagerResponse.getStatusLead() != 0) {
              lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
              lead.setLeadAppSeqId(application.getAppSeqId());
              lead = this.commonService.save(lead);
            } 
            application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
            application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
            if (lead.getLeadCreatedLmsUserId() != null) {
              if (application.getAppCreatedLmsUserId() == null)
                application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
            } else if (application.getAppCreatedLmsUserId() == null && 
              bankLmsUser != null) {
              application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
            } 
          } 
        } else if (SessionUtil.getApplicationType().intValue() == 2) {
          SessionUtil.setApplicationType(Integer.valueOf(1));
          ApplicationFormLead lead = null;
          if (SessionUtil.getLeadId() != null) {
            lead = this.commonService.getLeadById(SessionUtil.getLeadId());
          } else {
            lead = new ApplicationFormLead();
          } 
          lead.setLeadFirstName((application.getAppFirstName() != null) ? application.getAppFirstName() : null);
          lead.setLeadApplyingFrom(application.getAppApplyingFrom());
          lead.setLeadIsdCode((application.getAppISDCode() != null) ? application.getAppISDCode() : Constants.COUNTRY_CODE_INDIA);
          lead.setLeadMobileNo((application.getAppMobileNo() != null) ? application.getAppMobileNo() : null);
          lead.setLeadCityId((quote.getLoanQuoteCityId() != null) ? quote.getLoanQuoteCityId() : Constants.OTHER_ID_INTEGER);
          lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
          lead.setLeadProductTypeId(Constants.AUTO_LOAN_ID);
          lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID));
          lead.setLeadMobileAlertCount(Integer.valueOf(1));
          lead.setLeadMobileVerificationCode(Integer.valueOf(Integer.parseInt(Constants.DUMMY_MOBILE_OTP)));
          lead.setLeadVisitId(trackVisitId);
          lead.setLeadActive("Y");
          lead.setLeadDeleted("N");
          lead.setLeadIntermediaryId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
          lead.setLeadCreatedLmsUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
          if (application.getAppSubTypeId() != null && application.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)) {
            if (application.getAppMobileVerified() != null && application.getAppMobileVerified().equalsIgnoreCase("Y"))
              lead.setLeadMobileVerificationCodeVerified("Y"); 
          } else {
            lead.setLeadMobileVerificationCodeVerified("N");
          } 
          lead.setLeadReceiveDatetime(new Date());
          lead.setLeadEntryTime(new Date());
          lead.setLeadEntryDate(new Date());
          lead.setLeadLastUpdated(new Date());
          if (lead.getLeadSourceId() == null)
            lead.setLeadSourceId(Constants.LEAD_SOURCE_ID); 
          if (lead.getLeadDataSourceId() == null)
            lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT); 
          if (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) {
            List<BankLmsUserRole> lmsUserRole = this.commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
            if (lmsUserRole != null)
              for (BankLmsUserRole bankLmsUserRole : lmsUserRole) {
                if (lmsUserRole != null && bankLmsUserRole.getLmsRoleTypeId() != null && (3 == bankLmsUserRole.getLmsRoleTypeId().intValue() || 4 == bankLmsUserRole.getLmsRoleTypeId().intValue()))
                  lead.setLeadAssignedLmsUserId(bankLmsUser.getLmsUserId()); 
              }  
          } 
          lead.setLeadAppSeqId(application.getAppSeqId());
          if (bankLmsUser != null)
            if (bankLmsUser.getLmsUserType().intValue() == 1 || bankLmsUser.getLmsUserType().intValue() == 3) {
              lead.setLeadAppContactCenterLocation(0);
            } else {
              Integer lmsUserLocationId = this.commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
              if (lmsUserLocationId != null) {
                lead.setLeadAppContactCenterLocation(lmsUserLocationId.intValue());
              } else {
                lead.setLeadAppContactCenterLocation(1);
              } 
            }  
          lead = this.commonService.save(lead);
          SessionUtil.setLeadId(lead.getLeadId());
          application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
          application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
          if (lead.getLeadCreatedLmsUserId() != null) {
            if (application.getAppCreatedLmsUserId() == null)
              application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
          } else if (application.getAppCreatedLmsUserId() == null && 
            bankLmsUser != null) {
            application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
          } 
        }  
    } 
    loanScenarioBean.setMinEligibility(Double.valueOf(loanScenarioBean.getMinEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    loanScenarioBean.setChosenEligibility(Double.valueOf(loanScenarioBean.getChosenEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    loanScenarioBean.setMaxEligibility(Double.valueOf(loanScenarioBean.getMaxEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    loanScenarioBean.setNetAnnualIncome(quote.getLoanQuoteNetAnnualIncomeOfApplicant());
    loanScenarioBean.setProjectCost(quote.getLoanQuoteVehicleCost());
    loanScenarioBean.setShowCoApplicant(Integer.valueOf(1));
    if (quote.getLoanQuoteCoapplicantFirstId() != null)
      loanScenarioBean.setShowCoApplicant(Integer.valueOf(0)); 
    if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 9)
      loanScenarioBean.setShowCoApplicant(Integer.valueOf(0)); 
    if (quote.getLoanQuoteLoanCategoryId() != null) {
      MasterLoanCategory loanCategory = this.commonService.getLoanCategoryById(quote.getLoanQuoteLoanCategoryId());
      if (loanCategory != null)
        loanScenarioBean.setLoanPurpose(loanCategory.getCategoryTypeSmall()); 
    } else if (quote.getLoanQuoteLoanPurposeId() != null) {
      MasterLoanPurpose loanPurpose = this.commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
      if (loanPurpose != null)
        loanScenarioBean.setLoanPurpose(loanPurpose.getLpTypeValueSmall()); 
    } 
    loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(1));
    loanScenarioBean.setShowOffer(Integer.valueOf(0));
    boolean isPFWaiver = false;
    if (Constants.AUTO_LOAN_OFFER_ENABLE)
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 6) {
        Integer circleId = this.commonService.getCircleIdByBranchId(quote.getLoanQuoteBranchId());
        if (circleId != null && circleId.intValue() == 11)
          isPFWaiver = true; 
      }  
    CommonQuote termPreserve = null;
    CommonQuote overDraftPreserve = null;
    Map<Integer, AccountTypeTermOverDraft> loanQuotes = loanScenarioBean.getLoanQuotes();
    loanScenarioBean.setSecondProductUrl(null);
    loanScenarioBean.setFirstProductUrl(null);
    int count = 0;
    if (loanQuotes.entrySet().size() == 2) {
      loanScenarioBean.setShowFirstTermOverDraftCheck(Integer.valueOf(1));
      loanScenarioBean.setShowSecondTermOverDraftCheck(Integer.valueOf(1));
    } else {
      loanScenarioBean.setShowFirstTermOverDraftCheck(Integer.valueOf(1));
    } 
    for (Map.Entry<Integer, AccountTypeTermOverDraft> entry : loanQuotes.entrySet()) {
      count++;
      AccountTypeTermOverDraft typeTermOverDraft = entry.getValue();
      CommonQuote term = typeTermOverDraft.getAccountTypeTerm();
      if (term != null) {
        if (termPreserve == null)
          termPreserve = term; 
        AlProduct product = this.commonService.getAutoLoanProductById(term.getProductTypeId());
        if (product != null) {
          term.setProductTypeName(product.getAlProductName());
          if (count == 1) {
            loanScenarioBean.setFirstProductName(product.getAlProductName());
          } else if (count == 2) {
            loanScenarioBean.setSecondProductName(product.getAlProductName());
          } 
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getALProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getALProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getALProductSliderAmtMul());
          loanScenarioBean.setProductSliderTenureChn(product.getALProductSliderTenureChn());
          loanScenarioBean.setProductSliderDigitExact(product.getALProductSliderDigit());
          if (loanScenarioBean.getFirstProductUrl() == null) {
            loanScenarioBean.setFirstProductUrl(product.getProductUrl());
          } else {
            loanScenarioBean.setSecondProductUrl(product.getProductUrl());
          } 
        } else {
          term.setProductTypeName("N/A");
          loanScenarioBean.setSecondProductName("N/A");
        } 
        int moratoriumMonths = 0;
        term.setAccountTypeId(Integer.valueOf(1));
        if (isLoggin)
          term = this.commonEngine.getCalculatedQuote(term, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), isPFWaiver); 
        term.setIsDiscountApplied(Integer.valueOf(0));
        term.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        term.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        typeTermOverDraft.setAccountTypeTerm(term);
      } 
      CommonQuote overDraft = typeTermOverDraft.getAccountTypeOverDraft();
      if (overDraft != null) {
        if (overDraftPreserve == null)
          overDraftPreserve = overDraft; 
        AlProduct product = this.commonService.getAutoLoanProductById(overDraft.getProductTypeId());
        if (product != null) {
          overDraft.setProductTypeName(product.getAlProductName());
          if (count == 1) {
            loanScenarioBean.setFirstProductName(product.getAlProductName());
          } else if (count == 2) {
            loanScenarioBean.setSecondProductName(product.getAlProductName());
          } 
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getALProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getALProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getALProductSliderAmtMul());
          loanScenarioBean.setProductSliderTenureChn(product.getALProductSliderTenureChn());
          loanScenarioBean.setProductSliderDigitExact(product.getALProductSliderDigit());
          if (loanScenarioBean.getFirstProductUrl() == null) {
            loanScenarioBean.setFirstProductUrl(product.getProductUrl());
          } else {
            loanScenarioBean.setSecondProductUrl(product.getProductUrl());
          } 
        } else {
          overDraft.setProductTypeName("N/A");
          loanScenarioBean.setFirstProductName("N/A");
        } 
        int moratoriumMonths = 0;
        overDraft.setAccountTypeId(Integer.valueOf(2));
        if (isLoggin)
          overDraft = this.commonEngine.getCalculatedQuote(overDraft, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), isPFWaiver); 
        overDraft.setIsDiscountApplied(Integer.valueOf(0));
        overDraft.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        overDraft.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        typeTermOverDraft.setAccountTypeOverDraft(overDraft);
      } 
      loanQuotes.put(entry.getKey(), typeTermOverDraft);
      if (isLoggin) {
        if (term == null || overDraft == null)
          loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(0)); 
        if (loanQuotes.entrySet().size() == 2) {
          if ((term == null || overDraft == null) && term.getProductTypeId().intValue() >= 7)
            loanScenarioBean.setShowSecondTermOverDraftCheck(Integer.valueOf(0)); 
          if ((term == null || overDraft == null) && term.getProductTypeId().intValue() < 7)
            loanScenarioBean.setShowFirstTermOverDraftCheck(Integer.valueOf(0)); 
          continue;
        } 
        if ((term == null || overDraft == null) && count == 1)
          loanScenarioBean.setShowFirstTermOverDraftCheck(Integer.valueOf(0)); 
      } 
    } 
    loanScenarioBean.setLoanQuotes(loanQuotes);
    if (isLoggin) {
      if (application.getAppWorkEmail() != null && 
        SessionUtil.getEmail() == null)
        SessionUtil.setEmail(application.getAppWorkEmail()); 
      if (SessionUtil.getMobile() == null)
        if (application.getAppApplyingFrom() == 2) {
          if (application.getAppMobileNo() != null)
            SessionUtil.setMobile(application.getAppMobileNo()); 
        } else if (application.getAppMobileNo() != null) {
          SessionUtil.setMobile(application.getAppMobileNo());
        }  
      if (application.getAppFirstName() != null && 
        SessionUtil.getApplicantName() == null)
        SessionUtil.setApplicantName(application.getAppFirstName()); 
    } 
    application.setAppLoanMaxAmont(loanScenarioBean.getMaxEligibility());
    application.setAppLoanAmount(loanScenarioBean.getChosenEligibility());
    application.setAppLoanTenure(loanScenarioBean.getChosenTenure());
    if (termPreserve != null) {
      application.setAppLoanEmi(termPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(termPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(termPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(termPreserve.getProcessingFee().doubleValue())).doubleValue()));
      if (loanScenarioBean.getChosenProductId() != null) {
        application.setAppAutoLoanId(loanScenarioBean.getChosenProductId());
      } else {
        application.setAppAutoLoanId(termPreserve.getProductTypeId());
      } 
      application.setAppLoanAccountType(termPreserve.getAccountTypeId());
      if (termPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(termPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (termPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(termPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (termPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(termPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
      application.setAppEmiNmiRatio(Float.valueOf(termPreserve.getEmiNmiRatio()));
    } else if (overDraftPreserve != null) {
      application.setAppLoanEmi(overDraftPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(overDraftPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(overDraftPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(overDraftPreserve.getProcessingFee().doubleValue())).doubleValue()));
      if (loanScenarioBean.getChosenProductId() != null) {
        application.setAppAutoLoanId(loanScenarioBean.getChosenProductId());
      } else {
        application.setAppAutoLoanId(overDraftPreserve.getProductTypeId());
      } 
      application.setAppLoanAccountType(overDraftPreserve.getAccountTypeId());
      if (overDraftPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(overDraftPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (overDraftPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(overDraftPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (overDraftPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(overDraftPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
      application.setAppEmiNmiRatio(Float.valueOf(overDraftPreserve.getEmiNmiRatio()));
    } 
    if (loanScenarioBean.getProductSliderTenure() != null && loanScenarioBean.getProductSliderTenure().intValue() == 1) {
      loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure());
      loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure());
      loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure());
    } else {
      loanScenarioBean.setMaxTenure(Integer.valueOf(loanScenarioBean.getMaxTenure().intValue() / 12));
      loanScenarioBean.setChosenTenure(Integer.valueOf(loanScenarioBean.getChosenTenure().intValue() / 12));
      loanScenarioBean.setMinTenure(Integer.valueOf(loanScenarioBean.getMinTenure().intValue() / 12));
    } 
    
    application.setOcasID(SbiUtil.getOcasID());
    
    application = this.autoLoanService.save(application);
    loanScenarioBean.setApplicationAL(application);
    return loanScenarioBean;
  }
  
  public ApplicationFormAutoLoanQuote getUpdateQuote(ApplicationFormAutoLoanQuote quote) throws NullPointerException, RuntimeException, ParseException {
    String stdate = "";
    if (quote.getMonthloanQuoteStartDateOfCurrentLoan() != null) {
      if (quote.getYearloanQuoteStartDateOfCurrentLoan() != null) {
        stdate = "01-" + quote.getMonthloanQuoteStartDateOfCurrentLoan() + "-" + quote.getYearloanQuoteStartDateOfCurrentLoan();
      } else {
        stdate = "01-01-" + quote.getYearloanQuoteStartDateOfCurrentLoan();
      } 
      quote.setLoanQuoteStartDateOfCurrentLoanDT(DateUtil.convertStringToDate(stdate));
    } 
    if (quote.getMonthloanQuoteDop() != null) {
      if (quote.getYearloanQuoteDop() != null) {
        stdate = "01-" + quote.getMonthloanQuoteDop() + "-" + quote.getYearloanQuoteDop();
      } else {
        stdate = "01-01-" + quote.getYearloanQuoteDop();
      } 
      quote.setLoanQuoteDopDT(DateUtil.convertStringToDate(stdate));
    } 
    if (quote.getLoanQuoteMonthCompanyJoining() != null && quote.getLoanQuoteYearCompanyJoining() != null) {
      stdate = "01-" + quote.getLoanQuoteMonthCompanyJoining() + "-" + quote.getLoanQuoteYearCompanyJoining();
      quote.setLoanQuoteCompanyJoiningDate(DateUtil.convertStringToDate(stdate));
    } 
    Double loanRequestedAmountCar = Double.valueOf(0.0D);
    if (quote.getLoanQuoteLoanPurposeId().intValue() == 8) {
      loanRequestedAmountCar = Double.valueOf(((quote.getLoanQuoteDealerExshowroomPrice() != null) ? quote.getLoanQuoteDealerExshowroomPrice().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteInsuredDeclaredValue() != null) ? quote.getLoanQuoteInsuredDeclaredValue().doubleValue() : 0.0D));
    } else {
      loanRequestedAmountCar = Double.valueOf(((quote.getLoanQuoteDealerExshowroomPrice() != null) ? quote.getLoanQuoteDealerExshowroomPrice().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteOnRoadPrice() != null) ? quote.getLoanQuoteOnRoadPrice().doubleValue() : 0.0D));
    } 
    quote.setLoanQuoteLoanRequestedAmount(loanRequestedAmountCar);
    if (quote.getLoanQuoteBikeVariantId() != null && quote.getLoanQuoteBikeVariantId().intValue() > 0) {
      Double loanRequestedAmountBike = Double.valueOf(0.0D);
      loanRequestedAmountBike = Double.valueOf(((quote.getLoanQuoteExshowroomPriceBike() != null) ? quote.getLoanQuoteExshowroomPriceBike().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteRoadTaxBike() != null) ? quote.getLoanQuoteRoadTaxBike().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteInsuranceChargeBike() != null) ? quote.getLoanQuoteInsuranceChargeBike().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteRegistrationChargeBike() != null) ? quote.getLoanQuoteRegistrationChargeBike().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteAccessoriesChargeBike() != null) ? quote.getLoanQuoteAccessoriesChargeBike().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteOtherMiscChargeBike() != null) ? quote.getLoanQuoteOtherMiscChargeBike().doubleValue() : 0.0D));
      quote.setLoanQuoteLoanRequestedAmount(Double.valueOf(loanRequestedAmountCar.doubleValue() + loanRequestedAmountBike.doubleValue()));
    } 
    double netAnnualIncome = 0.0D;
    if (quote.getLoanQuoteEmploymentTypeId().intValue() == 9) {
      netAnnualIncome = ((quote.getLoanQuoteNetMonthlySalary() != null) ? quote.getLoanQuoteNetMonthlySalary().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteVariableMonthPay() != null) ? quote.getLoanQuoteVariableMonthPay().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteExpectedRentalIncome() != null) ? quote.getLoanQuoteExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteOtherIncome() != null) ? quote.getLoanQuoteOtherIncome().doubleValue() : 0.0D) * 12.0D;
    } else if (quote.getLoanQuoteEmploymentTypeId().intValue() == 10 || quote.getLoanQuoteEmploymentTypeId().intValue() == 11) {
      netAnnualIncome = ((quote.getLoanQuoteProfitAfterTax() != null) ? quote.getLoanQuoteProfitAfterTax().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteDepreciation() != null) ? quote.getLoanQuoteDepreciation().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteExpectedRentalIncome() != null) ? quote.getLoanQuoteExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteOtherIncome() != null) ? quote.getLoanQuoteOtherIncome().doubleValue() : 0.0D) * 12.0D;
    } else if (quote.getLoanQuoteEmploymentTypeId().intValue() == 12) {
      netAnnualIncome = ((quote.getLoanQuoteNetAnnualIncome() != null) ? quote.getLoanQuoteNetAnnualIncome().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteExpectedRentalIncome() != null) ? quote.getLoanQuoteExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteOtherIncome() != null) ? quote.getLoanQuoteOtherIncome().doubleValue() : 0.0D) * 12.0D;
    } else if (quote.getLoanQuoteEmploymentTypeId().intValue() == 13) {
      netAnnualIncome = ((quote.getLoanQuoteNetMonthlyPension() != null) ? quote.getLoanQuoteNetMonthlyPension().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteExpectedRentalIncome() != null) ? quote.getLoanQuoteExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteOtherIncome() != null) ? quote.getLoanQuoteOtherIncome().doubleValue() : 0.0D) * 12.0D;
    } else if (quote.getLoanQuoteEmploymentTypeId().intValue() == 14) {
      netAnnualIncome = ((quote.getLoanQuoteIncomeFromRegularSource() != null) ? quote.getLoanQuoteIncomeFromRegularSource().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteExpectedRentalIncome() != null) ? quote.getLoanQuoteExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteOtherIncome() != null) ? quote.getLoanQuoteOtherIncome().doubleValue() : 0.0D) * 12.0D;
    } 
    double netAnnualIncomeCoapplicant = 0.0D;
    if (quote.getLoanQuoteCoapplicantFirstId() != null)
      netAnnualIncomeCoapplicant = ((quote.getLoanQuoteCoapplicantFirstMonthlySalary() != null) ? quote.getLoanQuoteCoapplicantFirstMonthlySalary().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteCoapplicantFirstVariableMonthPay() != null) ? quote.getLoanQuoteCoapplicantFirstVariableMonthPay().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteCoapplicantFirstOtherIncome() != null) ? quote.getLoanQuoteCoapplicantFirstOtherIncome().doubleValue() : 0.0D) * 12.0D + (
        
        (quote.getLoanQuoteCoapplicantFirstProfitAfterTax() != null) ? quote.getLoanQuoteCoapplicantFirstProfitAfterTax().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteCoapplicantFirstDepreciatiation() != null) ? quote.getLoanQuoteCoapplicantFirstDepreciatiation().doubleValue() : 0.0D) + (
        
        (quote.getLoanQuoteCoapplicantFirstNetAnnualIncome() != null) ? quote.getLoanQuoteCoapplicantFirstNetAnnualIncome().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome() != null) ? quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        (quote.getLoanQuoteCoapplicantFirstNetMonthlyPension() != null) ? quote.getLoanQuoteCoapplicantFirstNetMonthlyPension().doubleValue() : 0.0D) * 12.0D + (
        
        (quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome() != null) ? quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome().doubleValue() : 0.0D) * 12.0D + (
        
        (quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource() != null) ? quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource().doubleValue() : 0.0D) * 12.0D; 
    if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 1) {
      quote.setLoanQuoteNetIncome(Double.valueOf(netAnnualIncome + netAnnualIncomeCoapplicant));
    } else {
      quote.setLoanQuoteNetIncome(Double.valueOf(netAnnualIncome));
    } 
    return quote;
  }
}
