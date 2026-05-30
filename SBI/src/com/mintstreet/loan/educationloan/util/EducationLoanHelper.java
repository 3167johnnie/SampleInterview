package com.mintstreet.loan.educationloan.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterGraduation;
import com.mintstreet.common.entity.MasterInstitute;
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
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanCalls;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.product.entity.MasterElProduct;


public class EducationLoanHelper {
  private static final Logger logger = LogManager.getLogger(EducationLoanHelper.class.getName());
  
  @Autowired
  private EducationLoanService educationLoanService;
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private CommonEngine commonEngine;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  public double loanMaxAmount;
  public double loanChosenAmount;
  public int loanTenure;
  
  private static boolean isAbroad(int bankId, int countryOfStudyId) {
    if (bankId == Constants.BANK_ID_SBI && countryOfStudyId == 2)
      return false; 
    return true;
  }
  
  public ApplicationFormEducationLoanQuote insertLoanQuote(ApplicationFormEducationLoanQuote quote, Integer bankLmsUserId, Integer trackVisitId, boolean isVLPLead) throws NullPointerException, RuntimeException, ParseException, SQLException {
      if (quote == null) {
        quote = new ApplicationFormEducationLoanQuote();
        quote.setError("Please re-initiate application.|quote.loanQuoteResidentStateId|1");
        return quote;
      } 
      if (!isVLPLead) {
        if (!ValidatorUtil.isValid(quote.getLoanQuoteResidentStateId())) {
          quote.setError("Please select state.|quote.loanQuoteResidentStateId|1");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuoteResidentCityId())) {
          quote.setError("Please select state.|quote.loanQuoteResidentStateId|1");
          return quote;
        } 
        if (quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER) && 
          !ValidatorUtil.isValid(quote.getLoanQuoteDistrictId())) {
          quote.setError("Please select city.|quote.loanQuoteResidentCityId|1");
          return quote;
        } 
        if (quote.getLoanQuoteLoanProductId() != null && quote.getLoanQuoteLoanProductId().intValue() != 24 && 
          quote.getLoanQuoteCountryOfStudyId() != null && isAbroad(Constants.BANK_ID.intValue(), quote.getLoanQuoteCountryOfStudyId().intValue()) && 
          !ValidatorUtil.isValid(quote.getLoanQuoteBranchId())) {
          if (quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER)) {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteResidentDistrictId|1");
          } else {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteResidentCityId|1");
          } 
          return quote;
        } 
      } 
      quote = getUpdateQuote(quote);
      if (quote.getLoanQuoteCourseDurationYears() != null)
        quote.setLoanQuoteCourseDuration(Integer.valueOf(quote.getLoanQuoteCourseDurationYears().intValue() * 12 + quote.getLoanQuoteCourseDurationMonth().intValue())); 
      MasterInstitute institute = null;
      if (ValidatorUtil.isValid(quote.getLoanQuoteInstituteName())) {
        institute = this.educationLoanService.getInstituteByInstituteName(quote.getLoanQuoteInstituteName());
      } else if (ValidatorUtil.isValid(quote.getLoanQuoteInstituteNameId())) {
        institute = this.educationLoanService.getInstituteByInstituteId(quote.getLoanQuoteInstituteNameId());
      } 
      if (institute != null) {
        quote.setLoanQuoteInstituteNameId(institute.getInstituteId());
        if (institute.getInstituteCategeroy() != null && institute.getInstituteCategeroy().equalsIgnoreCase("none")) {
          quote.setLoanQuoteInstituteCat("D");
        } else {
          quote.setLoanQuoteInstituteCat(institute.getInstituteCategeroy());
        } 
        quote.setLoanQuoteInstituteName(institute.getInstituteName());
        if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId().intValue() == 1 && quote.getLoanQuotePreferredLocation() != null && quote.getLoanQuotePreferredLocation().intValue() == 2 && ((quote.getLoanQuoteInstituteCat() != null && 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("AA")) || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("A") || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("B") || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("C")))
          quote.setLoanQuoteBranchId(institute.getInstituteDefaultBranch()); 
      } else {
        quote.setLoanQuoteInstituteCat("D");
      } 
      if (!isVLPLead)
        if (ValidatorUtil.isValid(quote.getLoanQuoteCourseName())) {
          MasterGraduation graduation = this.educationLoanService.getGraduationByGraduationName(quote.getLoanQuoteCourseName());
          if (graduation != null)
            quote.setLoanQuoteCourseNameId(graduation.getGraduationId()); 
        } else {
          quote.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return quote;
        }  
      quote.setLoanQuoteLeadId(SessionUtil.getLeadId());
      quote.setLoanQuoteCreatedLmsUserId((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID);
      quote.setLoanQuoteEntryTime(new Date());
      quote.setLoanQuoteUpdatedTime(new Date());
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      if (quote.getLoanQuoteVisitId() == null)
        quote.setLoanQuoteVisitId(trackVisitId); 
      quote.setLoanQuoteActive("Y");
      quote.setLoanQuoteDeleted("N");
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBBJ)
        quote.setLoanQuotePreferredLocation(Integer.valueOf(1)); 
      if (quote.getLoanQuoteBankWhichHasExistingEducationLoan() != null)
        quote.setLoanQuoteBankWhichHasExistingEducationLoan(quote.getLoanQuoteBankWhichHasExistingEducationLoan()); 
      if (quote.getLoanQuoteGender() != null)
        quote.setLoanQuoteGender(quote.getLoanQuoteGender()); 
      if (quote.getLoanQuoteCountryId() != null)
        quote.setLoanQuoteCountryId(quote.getLoanQuoteCountryId()); 
      if (quote.getAppApplyingFrom() > 0)
        quote.setLoanQuoteApplyingFrom(Integer.valueOf(quote.getAppApplyingFrom())); 
      
      logger.info("bankLmsUserId:: " + bankLmsUserId + "::: and quote consentId is:: " + quote.getLoanQuoteConsentId());
      //get active consentId for direct web leads (other than dsr page)
      if (Constants.OTHER_USER_ID.equals(bankLmsUserId)) {
    	  Consent consent = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "NTB");
    	  Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
    	  quote.setLoanQuoteConsentId(consentId); 
      } else {
    	  if (quote.getLoanQuoteConsentId() != null) {
    		  quote.setLoanQuoteConsentId(quote.getLoanQuoteConsentId()); 
    	  }
      }
      logger.info("ConsentId before save in quote table:::" + quote.getLoanQuoteConsentId());
      
      quote = this.educationLoanService.save(quote);
      return quote;
    }
  
  public ApplicationFormEducationLoan insertAppLoan(ApplicationFormEducationLoanQuote quote, ApplicationFormEducationLoan application, Integer bankLmsUserId, Integer lmsUserIntermediaryId, boolean isVLPLead) throws SQLException {
      if (quote == null) {
        if (application == null)
          application = new ApplicationFormEducationLoan(); 
        application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
        return application;
      } 
      if (application != null) {
        boolean isExistingMobile = false;
        if (application.getAppApplyingFrom() == 2) {
          if (quote.getAppNRIMobileNo() != null && application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo())) {
            application.setAppISDCode(quote.getAppISDCode());
            application.setAppMobileNo(quote.getAppNRIMobileNo());
            SessionUtil.setISDCode(quote.getAppISDCode());
            SessionUtil.setMobile(quote.getAppNRIMobileNo());
            isExistingMobile = true;
          } else {
            application.setAppISDCode(quote.getAppISDCode());
            application.setAppMobileNo(quote.getAppNRIMobileNo());
            SessionUtil.setISDCode(quote.getAppISDCode());
            SessionUtil.setMobile(quote.getAppNRIMobileNo());
          } 
        } else if (quote.getAppMobile() != null && application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppMobile())) {
          application.setAppMobileNo(quote.getAppMobile());
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setMobile(quote.getAppMobile());
          isExistingMobile = true;
        } else {
          application.setAppMobileNo(quote.getAppMobile());
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setMobile(quote.getAppMobile());
        } 
        if (application.getAppISDCode() != null && "91".equals(application.getAppISDCode())) {
          application.setAppResTypeAtVerified(Integer.valueOf(1));
        } else {
          application.setAppResTypeAtVerified(Integer.valueOf(2));
        } 
        if (!"Y".equalsIgnoreCase(application.getAppEmailVerified()))
          application.setAppEmailVerified("N"); 
      } else {
        application = new ApplicationFormEducationLoan();
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
        if ("Y".equals(quote.getLoanQuoteisSbiEmployee()))
          application.setAppLoanEmployerName(Constants.BANK_FULL_NAME); 
        if (quote.getAppApplyingFrom() == 2) {
          application.setAppApplyingFrom(2);
        } else {
          application.setAppApplyingFrom(1);
        } 
        application.setAppMobileNo(quote.getAppMobile());
        if(quote.getAppMobile() != null) {
        	application.setAppMobileNumberMask(quote.getAppMobile().replaceAll("\\d(?=\\d{4})", "*"));
        }
        if(quote.getAppNRIMobileNo() != null) {
        	if(quote.getAppNRIMobileNo().length() <= 8)
        		application.setAppMobileNumberMask(quote.getAppNRIMobileNo().replaceAll("\\d(?=\\d{2})", "*"));
        	else
        		application.setAppMobileNumberMask(quote.getAppNRIMobileNo().replaceAll("\\d(?=\\d{4})", "*"));
        }
        application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
        SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
      } 
      if (application.getAppElTypeId() == null)
        application.setAppElTypeId(SessionUtil.getEducationTypeId()); 
      if (!isVLPLead) {
        if (ValidatorUtil.isValid(quote.getLoanQuoteResidentStateId())) {
          application.setAppStateId(quote.getLoanQuoteResidentStateId());
        } else {
          application.setError("Please select state.|quote.loanQuoteResidentStateId|1");
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuoteResidentCityId())) {
          application.setAppCityId(quote.getLoanQuoteResidentCityId());
        } else {
          application.setError("Please select state.|quote.loanQuoteResidentStateId|1");
          return application;
        } 
        if (quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuoteDistrictId())) {
            application.setAppDistrictId(quote.getLoanQuoteDistrictId());
          } else {
            application.setError("Please select city.|quote.loanQuoteResidentCityId|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuoteBranchId())) {
          application.setAppBranchId(quote.getLoanQuoteBranchId());
        } else if (quote.getLoanQuoteCountryOfStudyId() != null && 
          isAbroad(Constants.BANK_ID.intValue(), quote.getLoanQuoteCountryOfStudyId().intValue())) {
          if (quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteResidentDistrictId|1");
          } else {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteResidentCityId|1");
          } 
          return application;
        } 
      } 
      if ("Y".equalsIgnoreCase(quote.getLoanQuoteisSbiEmployee())) {
        application.setAppCoapplicantEmploymentTypeId(Integer.valueOf(22));
      } else if ("N".equalsIgnoreCase(quote.getLoanQuoteisSbiEmployee()) && 
        bankLmsUserId == null) {
        application.setAppCoapplicantEmploymentTypeId(Integer.valueOf(0));
      } 
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
      if (quote.getLoanQuoteDateOfBirthDT() != null)
        application.setAppDobDT(quote.getLoanQuoteDateOfBirthDT()); 
      if (quote.getLoanQuoteGender() != null)
        application.setAppGender(quote.getLoanQuoteGender()); 
      if (quote.getAppMobile() != null) {
        application.setAppMobileNo(quote.getAppMobile());
        SessionUtil.setMobile(quote.getAppMobile().toString());
      } 
      if (quote.getAppEmail() != null) {
        application.setAppWorkEmail(quote.getAppEmail());
        SessionUtil.setEmail(quote.getAppEmail());
      } 
      application.setAppLeadUpdateTime(new Date());
      if (quote.getLoanQuoteLoanPurposeId() == null || quote.getLoanQuoteLoanPurposeId().intValue() != 24)
        application.setAppCoapplicantRelationTypeId(quote.getLoanQuoteCoapplicantFirstRelationshipId()); 
      application.setAppQuoteId(quote.getLoanQuoteId());
      if (quote.getLoanQuoteLoanAmount() != null)
        application.setAppLoanAmount(Double.valueOf(quote.getLoanQuoteLoanAmount().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue())); 
      if (quote.getLoanQuoteLoanTenure() != null)
        application.setAppLoanTenure(quote.getLoanQuoteLoanTenure()); 
      application.setAppActive("Y");
      application.setAppDeleted("N");
      if (quote.getAppMobile() != null)
        application.setAppMobileNo(quote.getAppMobile()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteResidentCityId()))
        if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId().intValue() == 1 && quote.getLoanQuotePreferredLocation() != null && quote.getLoanQuotePreferredLocation().intValue() == 2 && ((
          quote.getLoanQuoteInstituteCat() != null && 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("AA")) || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("A") || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("B") || 
          quote.getLoanQuoteInstituteCat().equalsIgnoreCase("C"))) {
          quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER);
        } else if (!quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER)) {
          List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.EDUCATION_LOAN_ID, null, quote.getLoanQuoteResidentCityId(), null, null, null, null, null);
          if (branches != null && !branches.isEmpty()) {
            if (branches.size() == 1) {
              MasterBranch masterBranch = branches.get(0);
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.EDUCATION_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
              } 
            } 
          } else if (quote.getLoanQuoteBranchId() != null) {
            MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuoteBranchId());
            if (masterBranch != null) {
              Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.EDUCATION_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
              application.setAppSalesTeamId(salesTeamId);
            } 
          } 
        }  
      if (ValidatorUtil.isValid(quote.getLoanQuoteBranchId())) {
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
                  ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.EDUCATION_LOAN_ID);
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
          Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.EDUCATION_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
          application.setAppSalesTeamId(salesTeamId);
        } 
      } 
      if (quote.getLoanQuoteCityOfStudy() != null)
        application.setAppCoapplicantCityId(quote.getLoanQuoteCityOfStudy()); 
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
      if (!isVLPLead && 
        quote.getLoanQuoteCountryOfStudyId() != null && 
        isAbroad(Constants.BANK_ID.intValue(), quote.getLoanQuoteCountryOfStudyId().intValue()))
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
          application.setError(Constants.SORRY_FOR_INCONVENIENCE);
          return application;
        }  
      if (!ValidatorUtil.isValid(application.getAppLoanStatusId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (!ValidatorUtil.isValid(application.getAppDataSourceId())) {
        application.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return application;
      } 
      if (application.getAppAssignedLmsSalesUserId() == null)
        if (bankLmsUserId != null) {
          if (this.commonService.isBankUser(bankLmsUserId))
            application.setAppAssignedLmsSalesUserId(bankLmsUserId); 
        } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null && 
          this.commonService.isBankUser(bankLmsUserId)) {
          application.setAppAssignedLmsSalesUserId(SessionUtil.getBankLMSUser().getLmsUserId());
        }  
      if (quote.getLoanQuoteCountryId() != null) {
        MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
        if (country != null)
          application.setAppAbroadCountryId(country.getCountryName()); 
      } 
      logger.info(" quote.getAlternateMobileNumber() 111 inside the educationLoanHelper  " + quote.getAlternateMobileNumber());
      if (quote.getAlternateMobileNumber() != null) {
        logger.info(" quote.getAlternateMobileNumber() inside the if  22 2  " + quote.getAlternateMobileNumber());
        application.setAppAlternateMobileNumber(quote.getAlternateMobileNumber());
        application.setAppAltISDCode(quote.getAppAltISDCode());
        SessionUtil.setalternateMobileNumber(quote.getAppMobile());
      } 
      	
	    //save consent id in main table
	    if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
	    	application.setAppConsentId(quote.getLoanQuoteConsentId());
	    }
	    
      application = this.educationLoanService.save(application);
      return application;
      
  }
  
  public Integer saveCoApplicantDetails(ApplicationFormEducationLoanQuote quoteNew) throws SQLException {
      Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
      if (quoteNew == null)
        return null; 
      if (appSeqId == null)
        return null; 
      ApplicationFormEducationLoan application = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
      if (application == null)
        return null; 
      ApplicationFormEducationLoanQuote quote = this.educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(application.getAppQuoteId());
      if (quote == null)
        return null; 
      if (quoteNew.getLoanQuoteCoapplicantFirstRelationshipId() != null)
        quote.setLoanQuoteCoapplicantFirstRelationshipId(quoteNew.getLoanQuoteCoapplicantFirstRelationshipId()); 
      quote = this.educationLoanService.save(quote);
      return quote.getLoanQuoteId();
  }
  
  public void insertCallLog(Integer callApplicationId, int callUserId, int callStatusId, String message, Date callDocPickupTime, boolean displayLog) {
    try {
      ApplicationFormEducationLoanCalls callsLog = new ApplicationFormEducationLoanCalls();
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
      callsLog = this.educationLoanService.save(callsLog);
    } catch (SQLException e) {
      logger.info("EducationLoanService.java LNo : 524 : Exception Caught", e);
    }  catch (Exception e) {
        logger.info("EducationLoanService.java LNo : 524 : Exception Caught", e);
      } 
  }
  
  public LoanScenarioBean callBREAgain(ApplicationFormEducationLoan application, ApplicationFormEducationLoanQuote quote) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      Integer tempCountryOfStudy = quote.getLoanQuoteCountryOfStudy();
      if (tempCountryOfStudy != null && !tempCountryOfStudy.equals("")) {
        quote.setLoanQuoteCountryOfStudy(Integer.valueOf(225));
        quote.getLoanQuoteCountryOfStudy();
        logger.info("ELProcessManagerImpl.java LNo: 1500 :: Rule set for Country opted for education :: (United States) in Rule Enigne");
      } 
      quote = getUpdateQuote(quote);
      JSONObject engineRequest = JSONUtil.beanObjectToJSONObjct(quote);
      engineRequest = this.SbiUtil.getDBCredentialForHelper(engineRequest);
      JSONObject engineResponseJson = null;
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 26) {
        engineResponseJson = new JSONObject("{\"status\":1,\"message\":\"\",\"minEligibility\":10000,\"chosenEligibility\":435000,\"maxEligibility\":435000,\"chosenTenure\":180,\"minTenure\":\"12\",\"maxTenure\":180,\"loanQuotes\":{\"1\":{\"interestDuringMoratorium\":{\"productTypeId\":9,\"loanAmount\":435000,\"interestRate\":8.9,\"rateType\":1,\"emi\":4386.2199178176,\"processingFee\":\"0\",\"displayName\":\"Interest paid during moratorium\"},\"noInterestDuringMoratorium\":{\"productTypeId\":9,\"loanAmount\":435000,\"interestRate\":9.9,\"rateType\":1,\"emi\":6948.6944730566,\"processingFee\":\"0\",\"displayName\":\"No payment during moratorium\"}}}}");
      } else if (Constants.IS_ENGINE_OBF) {
        JSONObject finalEngineRequest = MapperUtil.convertEducationLoan(engineRequest);
        engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + finalEngineRequest, Constants.EDUCATION_LOAN_ID);
      } else {
        engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + engineRequest, Constants.EDUCATION_LOAN_ID);
      } 
      loanScenarioBean = (LoanScenarioBean)JSONUtil.getObjctFromJSON(loanScenarioBean, engineResponseJson.toString());
      application.setAppRequestData(engineRequest.toString());
      application.setAppOfferJsonData(engineResponseJson.toString());
      quote.setLoanQuoteCountryOfStudy(tempCountryOfStudy);
      loanScenarioBean.setApplicationEL(application);
    } catch (JSONException  e) {
        logger.info("ELProcessManagerImpl.java LNo: 1510 :: Calling Rule Enigne  :: ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
      }  catch (Exception e) {
      logger.info("ELProcessManagerImpl.java LNo: 1510 :: Calling Rule Enigne  :: ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
    } 
    return loanScenarioBean;
  }
  
  public LoanScenarioBean callBRE(ApplicationFormEducationLoan application, ApplicationFormEducationLoanQuote quote, BankLmsUser bankLmsUser, Integer previousQuoteId, Integer trackVisitId, String ajaxPostUrl, boolean isLoggin) throws SQLException {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    loanScenarioBean = callBREAgain(application, quote);
    if (loanScenarioBean.getStatus().intValue() == 0)
      return loanScenarioBean; 
    if (loanScenarioBean.getStatus().intValue() != 1 && 
      isLoggin) {
      String noOfferReason = loanScenarioBean.getNoOfferReason();
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(false);
      statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
          application.setAppEducationLoanId(null);
          application = this.educationLoanService.save(application);
        } 
      } else if (application.getAppLoanStatusId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (statusManagerResponse.isPreserveData() && previousQuoteId.intValue() > 0) {
        application.setAppQuoteId(previousQuoteId);
        application = this.educationLoanService.save(application);
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
            lead.setLeadCityId((quote.getLoanQuoteResidentCityId() != null) ? quote.getLoanQuoteResidentCityId() : Constants.OTHER_ID_INTEGER);
            lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
            lead.setLeadProductTypeId(Constants.EDUCATION_LOAN_ID);
            if (statusManagerResponse.getStatusLead() != 0)
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
      if (loanScenarioBean != null && loanScenarioBean.getStatus().intValue() == 2)
        if (quote.getLoanQuoteLoanPurposeId().equals(Integer.valueOf(24))) {
          loanScenarioBean.setMessage(Constants.NO_LOAN_OFFER_EL);
        } else if (loanScenarioBean.getMessage().equalsIgnoreCase(Constants.NO_LOAN_OFFER_EL)) {
          loanScenarioBean.setMessage(Constants.NO_LOAN_OFFER_EL_NEW);
        }  
      loanScenarioBean.setApplicationEL(application);
      return loanScenarioBean;
    } 
    if (SessionUtil.getApplicationType() != null && 
      isLoggin) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
          lead.setLeadCityId((quote.getLoanQuoteResidentCityId() != null) ? quote.getLoanQuoteResidentCityId() : Constants.OTHER_ID_INTEGER);
          lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
          lead.setLeadProductTypeId(Constants.EDUCATION_LOAN_ID);
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
          if (bankLmsUser != null && 
            bankLmsUser.getLmsUserType() != null)
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
    if (application.getAppLoanAmount() != null) {
      this.loanChosenAmount = application.getAppLoanAmount().doubleValue();
      logger.info("EducationLoanHelper.java loanChosenAmount::::" + this.loanChosenAmount);
      logger.info("EducationLoanHelper.java setAppLoanAmount::" + loanScenarioBean.getChosenEligibility() + "..getAppLoanAmount.." + application.getAppLoanAmount());
    } 
    logger.info("EducationLoanHelper.java setAppLoanTenure::" + loanScenarioBean.getChosenTenure() + "..getAppLoanTenure.." + application.getAppLoanTenure());
    if (quote.getLoanQuoteLoanPurposeId() != null) {
      MasterLoanPurpose loanPurpose = commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
      loanScenarioBean.setLoanPurpose(loanPurpose.getLpTypeValueSmall());
    } 
    loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(1));
    loanScenarioBean.setShowOffer(Integer.valueOf(0));
    CommonQuote moratoriumPreserve = null;
    CommonQuote noMoratoriumPreserve = null;
    Map<Integer, AccountTypeTermOverDraft> loanQuotes = loanScenarioBean.getLoanQuotes();
    List<CommonQuote> allQuotes = new ArrayList<>();
    List<String> accountTypeName = new ArrayList<>();
    Set<Integer> productsIdList = new HashSet<>(4);
    for (Map.Entry<Integer, AccountTypeTermOverDraft> entry : loanQuotes.entrySet()) {
      AccountTypeTermOverDraft typeTermOverDraft = entry.getValue();
      CommonQuote moratorium = typeTermOverDraft.getInterestDuringMoratorium();
      if (moratorium != null) {
        if (moratoriumPreserve == null)
          moratoriumPreserve = moratorium; 
        MasterElProduct product = commonService.getEducationLoanProductById(moratorium.getProductTypeId());
        if (product != null) {
          productsIdList.add(product.getElProductId());
          moratorium.setProductTypeName(product.getElProductName());
          if (((Integer)entry.getKey()).intValue() == 1) {
            loanScenarioBean.setFirstProductName(product.getElProductName());
            loanScenarioBean.setFirstProductUrl(product.getProductUrl());
          } 
          if (((Integer)entry.getKey()).intValue() == 2) {
            loanScenarioBean.setSecondProductName(product.getElProductName());
            loanScenarioBean.setSecondProductUrl(product.getProductUrl());
          } 
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getELProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getELProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getELProductSliderAmtMul());
          loanScenarioBean.setProductSliderDigitExact(product.getELProductSliderDigit());
          loanScenarioBean.setProductSliderTenureChn(product.getELProductSliderTenureChn());
        } else {
          moratorium.setProductTypeName("N/A");
          loanScenarioBean.setSecondProductName("N/A");
        } 
        int moratoriumMonths = 0;
        if (quote.getLoanQuoteCourseDuration() != null)
          if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
            if (moratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } else if (moratorium.getProductTypeId().intValue() == 5) {
              if (quote.getLoanQuoteCourseDuration().intValue() <= 12) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } 
            } else if (moratorium.getProductTypeId().intValue() == 7) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else if (quote.getLoanQuoteCourseTypeId().intValue() == 3) {
              if (quote.getLoanQuoteCourseDuration().intValue() <= 6) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } 
            } else {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } 
          } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBH) {
            if (moratorium.getProductTypeId().intValue() == 1) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } else if (moratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else if (moratorium.getProductTypeId().intValue() != 3 && 
              moratorium.getProductTypeId().intValue() != 4 && 
              moratorium.getProductTypeId().intValue() == 5) {
              if (quote.getLoanQuoteCourseDuration().intValue() <= 6) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } 
            } 
          } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBM) {
            if (moratorium.getProductTypeId().intValue() == 1) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } else if (moratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else {
              moratorium.getProductTypeId().intValue();
            } 
          } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBP) {
            if (moratorium.getProductTypeId().intValue() == 1) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } else if (moratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else {
              moratorium.getProductTypeId().intValue();
            } 
          } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT) {
            if (moratorium.getProductTypeId().intValue() == 1) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 24;
            } else if (moratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } else if (moratorium.getProductTypeId().intValue() != 3) {
              if (moratorium.getProductTypeId().intValue() == 4) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (moratorium.getProductTypeId().intValue() == 5) {
                if (quote.getLoanQuoteCourseDuration().intValue() <= 12) {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
                } else {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                } 
              } else if (moratorium.getProductTypeId().intValue() == 6) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } 
            } 
          } else if (moratorium.getProductTypeId().intValue() == 2) {
            moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
          } else if (quote.getLoanQuoteCourseTypeId().intValue() == 3) {
            if (quote.getLoanQuoteCourseDuration().intValue() <= 6) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            } 
          } else {
            moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
          }  
        moratorium.setAccountTypeId(Integer.valueOf(1));
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
          if (moratorium.getProductTypeId().intValue() == 7) {
            moratorium = this.commonEngine.getCalculatedQuote(moratorium, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, 2, false);
          } else {
            moratorium = this.commonEngine.getCalculatedQuote(moratorium, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, 1, loanScenarioBean.getProductSliderTenure().intValue());
          } 
        } else {
          moratorium = this.commonEngine.getCalculatedQuote(moratorium, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, 1, loanScenarioBean.getProductSliderTenure().intValue());
        } 
        moratorium.setIsDiscountApplied(Integer.valueOf(0));
        moratorium.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        moratorium.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        allQuotes.add(moratorium);
        if (moratorium.getDisplayName() != null)
          accountTypeName.add(moratorium.getDisplayName()); 
        typeTermOverDraft.setInterestDuringMoratorium(moratorium);
      } 
      loanQuotes.put(entry.getKey(), typeTermOverDraft);
      CommonQuote noMoratorium = null;
      if (quote.getLoanQuoteLoanPurposeId() != Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID) {
        noMoratorium = typeTermOverDraft.getNoInterestDuringMoratorium();
        if (noMoratorium != null) {
          if (noMoratoriumPreserve == null)
            noMoratoriumPreserve = noMoratorium; 
          MasterElProduct product = this.commonService.getEducationLoanProductById(noMoratorium.getProductTypeId());
          if (product != null) {
            productsIdList.add(product.getElProductId());
            noMoratorium.setProductTypeName(product.getElProductName());
            if (((Integer)entry.getKey()).intValue() == 1) {
              loanScenarioBean.setFirstProductName(product.getElProductName());
              loanScenarioBean.setFirstProductUrl(product.getProductUrl());
            } 
            if (((Integer)entry.getKey()).intValue() == 2) {
              loanScenarioBean.setSecondProductName(product.getElProductName());
              loanScenarioBean.setSecondProductUrl(product.getProductUrl());
            } 
            loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getELProductSliderAmtMul().doubleValue() / 100000.0D));
            loanScenarioBean.setProductSliderTenure(product.getELProductSliderTenure());
            loanScenarioBean.setProductSliderAmtMul(product.getELProductSliderAmtMul());
            loanScenarioBean.setProductSliderDigitExact(product.getELProductSliderDigit());
            loanScenarioBean.setProductSliderTenureChn(product.getELProductSliderTenureChn());
          } else {
            noMoratorium.setProductTypeName("N/A");
            loanScenarioBean.setFirstProductName("N/A");
          } 
          int moratoriumMonths = 0;
          if (quote.getLoanQuoteCourseDuration() != null)
            if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
              if (noMoratorium.getProductTypeId().intValue() == 2) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (noMoratorium.getProductTypeId().intValue() == 5) {
                if (quote.getLoanQuoteCourseDuration().intValue() <= 12) {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
                } else {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                } 
              } else if (noMoratorium.getProductTypeId().intValue() == 7) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } 
            } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBH) {
              if (noMoratorium.getProductTypeId().intValue() == 1) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (noMoratorium.getProductTypeId().intValue() == 2) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else if (noMoratorium.getProductTypeId().intValue() != 3 && 
                noMoratorium.getProductTypeId().intValue() != 4 && 
                noMoratorium.getProductTypeId().intValue() == 5) {
                if (quote.getLoanQuoteCourseDuration().intValue() <= 6) {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
                } else {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                } 
              } 
            } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBM) {
              if (noMoratorium.getProductTypeId().intValue() == 1) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (noMoratorium.getProductTypeId().intValue() == 2) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else {
                noMoratorium.getProductTypeId().intValue();
              } 
            } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBP) {
              if (noMoratorium.getProductTypeId().intValue() == 1) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (noMoratorium.getProductTypeId().intValue() == 2) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
              } else {
                noMoratorium.getProductTypeId().intValue();
              } 
            } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT) {
              if (noMoratorium.getProductTypeId().intValue() == 1) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 24;
              } else if (noMoratorium.getProductTypeId().intValue() == 2) {
                moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
              } else if (noMoratorium.getProductTypeId().intValue() != 3) {
                if (noMoratorium.getProductTypeId().intValue() == 4) {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                } else if (noMoratorium.getProductTypeId().intValue() == 5) {
                  if (quote.getLoanQuoteCourseDuration().intValue() <= 12) {
                    moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
                  } else {
                    moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                  } 
                } else if (noMoratorium.getProductTypeId().intValue() == 6) {
                  moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
                } 
              } 
            } else if (noMoratorium.getProductTypeId().intValue() == 2) {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 6;
            } else {
              moratoriumMonths = quote.getLoanQuoteCourseDuration().intValue() + 12;
            }  
          noMoratorium.setAccountTypeId(Integer.valueOf(2));
          noMoratorium = this.commonEngine.getCalculatedQuote(noMoratorium, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, 2, false);
          noMoratorium.setIsDiscountApplied(Integer.valueOf(0));
          noMoratorium.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
          noMoratorium.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
          allQuotes.add(noMoratorium);
          if (noMoratorium.getDisplayName() != null)
            accountTypeName.add(noMoratorium.getDisplayName()); 
          typeTermOverDraft.setNoInterestDuringMoratorium(noMoratorium);
        } 
      } 
      if (moratorium == null || noMoratorium == null)
        loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(0)); 
    } 
    loanScenarioBean.setLoanQuotes(loanQuotes);
    loanScenarioBean.setAllQuotes(allQuotes);
    loanScenarioBean.setAccountTypeName(accountTypeName);
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
    JSONObject loanScenarioJson = JSONUtil.beanObjectToJSONObjct(loanScenarioBean);
    application.setAppOfferJsonData(loanScenarioJson.toString());
    application.setAppLoanMaxAmount(loanScenarioBean.getMaxEligibility());
    application.setAppLoanAmount(loanScenarioBean.getChosenEligibility());
    application.setAppLoanTenure(loanScenarioBean.getChosenTenure());
	logger.info("EducationLoanHelper.java setAppLoanMaxAmount::"+loanScenarioBean.getMaxEligibility()+"..getAppLoanMaxAmount.."+application.getAppLoanMaxAmount());
	logger.info("EducationLoanHelper.java setAppLoanAmount::"+loanScenarioBean.getChosenEligibility()+"..getAppLoanAmount.."+application.getAppLoanAmount());
	logger.info("EducationLoanHelper.java setAppLoanTenure::"+loanScenarioBean.getChosenTenure()+"..getAppLoanTenure.."+application.getAppLoanTenure());
	
	loanMaxAmount = application.getAppLoanMaxAmount();
	logger.info("EducationLoanHelper.java loanMaxAmount::::"+loanMaxAmount);   
	
	loanChosenAmount = application.getAppLoanAmount();
	logger.info("EducationLoanHelper.java loanChosenAmount::::"+loanChosenAmount);
	
	loanTenure = application.getAppLoanTenure();
	logger.info("EducationLoanHelper.java loanTenure::::"+loanTenure);
		
    if (moratoriumPreserve != null) {
      application.setAppLoanEmi(moratoriumPreserve.getEmi());
		logger.info("EducationLoanHelper.java getEmi::"+moratoriumPreserve.getEmi()+"..getAppLoanEmi.."+application.getAppLoanEmi());
      application.setAppLoanInterestRate(Float.valueOf(moratoriumPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(moratoriumPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(moratoriumPreserve.getProcessingFee().doubleValue())).doubleValue()));
		logger.info("EducationLoanHelper.java "+moratoriumPreserve.getProcessingFee()+"..getAppLoanProcessingFee.."+application.getAppLoanProcessingFee());
      application.setAppEducationLoanId(moratoriumPreserve.getProductTypeId());
      application.setAppLoanAccountType(Integer.valueOf(1));
      application.setAppEmiNmiRatio(Float.valueOf(moratoriumPreserve.getEmiNmiRatio()));
      if (moratoriumPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(moratoriumPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (moratoriumPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(moratoriumPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (moratoriumPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(moratoriumPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
    } else if (noMoratoriumPreserve != null) {
      application.setAppLoanEmi(noMoratoriumPreserve.getEmi());
			logger.info("EducationLoanHelper.java getEmi::"+noMoratoriumPreserve.getEmi()+"..getAppLoanEmi.."+application.getAppLoanEmi());
      application.setAppLoanInterestRate(Float.valueOf(noMoratoriumPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(noMoratoriumPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(noMoratoriumPreserve.getProcessingFee().doubleValue())).doubleValue()));
      application.setAppEducationLoanId(noMoratoriumPreserve.getProductTypeId());
      application.setAppLoanAccountType(Integer.valueOf(2));
      application.setAppEmiNmiRatio(Float.valueOf(noMoratoriumPreserve.getEmiNmiRatio()));
      if (noMoratoriumPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(noMoratoriumPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (noMoratoriumPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(noMoratoriumPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (noMoratoriumPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(noMoratoriumPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
    } 
    
    logger.info("call BRE Line 1189 :: " + loanScenarioBean.getProductSliderTenure());
    if (loanScenarioBean.getProductSliderTenure() != null && loanScenarioBean.getProductSliderTenure().intValue() == 1) {
      loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure());
      loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure());
      loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure());
    } else {
      loanScenarioBean.setMaxTenure(Integer.valueOf(loanScenarioBean.getMaxTenure().intValue() / 12));
      loanScenarioBean.setChosenTenure(Integer.valueOf(loanScenarioBean.getChosenTenure().intValue() / 12));
      loanScenarioBean.setMinTenure(Integer.valueOf(loanScenarioBean.getMinTenure().intValue() / 12));
    } 
    logger.info("ocasID is :: " + SbiUtil.OcasID);
    
    application.setOcasID(SbiUtil.getOcasID());
    
    application = this.educationLoanService.save(application);
    logger.info("application saved");
    
    loanScenarioBean.setApplicationEL(application);
    logger.info("before return");
    return loanScenarioBean;
  }
  
  public ApplicationFormEducationLoanQuote getUpdateQuote(ApplicationFormEducationLoanQuote quote) throws NullPointerException, RuntimeException, ParseException {
    double netAnnualIncomeCoapplicant = 0.0D;
    if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() == 24) {
      if (quote.getLoanQuoteResidentTypeId() != null)
        quote.setLoanQuoteResidentTypeId(quote.getLoanQuoteResidentTypeId()); 
    } else {
      quote.setLoanQuoteResidentTypeId(Integer.valueOf(1));
    } 
    quote.setLoanQuoteNetIncome(Double.valueOf(netAnnualIncomeCoapplicant));
    String date = "";
    if (quote.getLoanQuoteLoanOptedLoan() != null && quote.getLoanQuoteLoanOptedLoan().equals("Y") && 
      quote.getLoanQuoteLoanWithInMoratoriumPeriod() != null && quote.getLoanQuoteLoanWithInMoratoriumPeriod().equalsIgnoreCase("Y")) {
      date = "01-" + quote.getLoanQuoteMonthstartDateOfloanRepayment() + "-" + quote.getLoanQuoteYearstartDateOfloanRepayment();
      quote.setLoanQuoteStartDateOfLoanRepayment(quote.getLoanQuoteYearstartDateOfloanRepayment() + "-" + ((quote.getLoanQuoteMonthstartDateOfloanRepayment().intValue() > 9) ? ""+quote.getLoanQuoteMonthstartDateOfloanRepayment() : ("0" + quote.getLoanQuoteMonthstartDateOfloanRepayment())) + "-01");
      quote.setLoanQuoteSTDOfLoanRepayment(DateUtil.convertStringToDate(date));
      date = "01-" + quote.getLoanQuoteMonthdateOfAppliendCourse() + "-" + quote.getLoanQuoteYeardateOfAppliendCourse();
      quote.setLoanQuoteSTDOfAppliedCourse(DateUtil.convertStringToDate(date));
      quote.setLoanQuoteStartDateOfAppliedCourse(quote.getLoanQuoteYeardateOfAppliendCourse() + "-" + ((quote.getLoanQuoteMonthdateOfAppliendCourse().intValue() > 9) ? ""+quote.getLoanQuoteMonthdateOfAppliendCourse() : ("0" + quote.getLoanQuoteMonthdateOfAppliendCourse())) + "-01");
      if (quote.getLoanQuoteYearloanTenureEndDate() != null && quote.getLoanQuoteMonthloanTenureEndDate() != null) {
        date = "01-" + quote.getLoanQuoteMonthloanTenureEndDate() + "-" + quote.getLoanQuoteYearloanTenureEndDate();
        quote.setLoanQuoteEndDateOfCurrentLoan(DateUtil.convertStringToDate(date));
      } 
    } 
    Double loanRequestedAmount = Double.valueOf(0.0D);
    if (quote.getLoanQuoteLoanType() != null && (quote.getLoanQuoteLoanType().intValue() == 1 || quote.getLoanQuoteLoanType().intValue() == 2)) {
      loanRequestedAmount = Double.valueOf(((quote.getLoanQuoteTuitionFee() != null) ? quote.getLoanQuoteTuitionFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteAdmissionFee() != null) ? quote.getLoanQuoteAdmissionFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteHostelFee() != null) ? quote.getLoanQuoteHostelFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteExaminationLabOrLibFee() != null) ? quote.getLoanQuoteExaminationLabOrLibFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePurchaseOfBookAndEquipment() != null) ? quote.getLoanQuotePurchaseOfBookAndEquipment().doubleValue() : 0.0D) + (
          (quote.getLoanQuotedepositFee() != null) ? quote.getLoanQuotedepositFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteTravelExpenses() != null) ? quote.getLoanQuoteTravelExpenses().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePurchaseOfComputer() != null) ? quote.getLoanQuotePurchaseOfComputer().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteOtherExpenses() != null) ? quote.getLoanQuoteOtherExpenses().doubleValue() : 0.0D));
    } else if (quote.getLoanQuoteLoanType() != null && quote.getLoanQuoteLoanType().intValue() == 3) {
      loanRequestedAmount = Double.valueOf(((quote.getLoanQuoteTuitionFee() != null) ? quote.getLoanQuoteTuitionFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteAdmissionFee() != null) ? quote.getLoanQuoteAdmissionFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteHostelFee() != null) ? quote.getLoanQuoteHostelFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteExaminationLabOrLibFee() != null) ? quote.getLoanQuoteExaminationLabOrLibFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePurchaseOfBookAndEquipment() != null) ? quote.getLoanQuotePurchaseOfBookAndEquipment().doubleValue() : 0.0D) + (
          (quote.getLoanQuotedepositFee() != null) ? quote.getLoanQuotedepositFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteTravelExpenses() != null) ? quote.getLoanQuoteTravelExpenses().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePurchaseOfComputer() != null) ? quote.getLoanQuotePurchaseOfComputer().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePremium() != null) ? quote.getLoanQuotePremium().doubleValue() : 0.0D));
    } else if (quote.getLoanQuoteLoanType() != null && quote.getLoanQuoteLoanType().intValue() == 4) {
      loanRequestedAmount = Double.valueOf(((quote.getLoanQuoteTuitionFee() != null) ? quote.getLoanQuoteTuitionFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteExaminationLabOrLibFee() != null) ? quote.getLoanQuoteExaminationLabOrLibFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuotePurchaseOfBookAndEquipment() != null) ? quote.getLoanQuotePurchaseOfBookAndEquipment().doubleValue() : 0.0D) + (
          (quote.getLoanQuotedepositFee() != null) ? quote.getLoanQuotedepositFee().doubleValue() : 0.0D) + (
          (quote.getLoanQuoteOtherExpenses() != null) ? quote.getLoanQuoteOtherExpenses().doubleValue() : 0.0D));
    } 
    loanRequestedAmount = Double.valueOf(((quote.getLoanQuoteTuitionFee() != null) ? quote.getLoanQuoteTuitionFee().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteAdmissionFee() != null) ? quote.getLoanQuoteAdmissionFee().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteHostelFee() != null) ? quote.getLoanQuoteHostelFee().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteExaminationLabOrLibFee() != null) ? quote.getLoanQuoteExaminationLabOrLibFee().doubleValue() : 0.0D) + (
        (quote.getLoanQuotePurchaseOfBookAndEquipment() != null) ? quote.getLoanQuotePurchaseOfBookAndEquipment().doubleValue() : 0.0D) + (
        
        (quote.getLoanQuoteCautionFee() != null) ? quote.getLoanQuoteCautionFee().doubleValue() : 0.0D) + (
        (quote.getLoanQuotePurchaseOfTwoWheeler() != null) ? quote.getLoanQuotePurchaseOfTwoWheeler().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteTravelExpenses() != null) ? quote.getLoanQuoteTravelExpenses().doubleValue() : 0.0D) + (
        (quote.getLoanQuotePurchaseOfComputer() != null) ? quote.getLoanQuotePurchaseOfComputer().doubleValue() : 0.0D) + (
        (quote.getLoanQuoteOtherExpenses() != null) ? quote.getLoanQuoteOtherExpenses().doubleValue() : 0.0D));
    if (quote.getLoanQuoteOverseasHealthInsuranceFee() > 0.0D)
      loanRequestedAmount = Double.valueOf(loanRequestedAmount.doubleValue() + quote.getLoanQuoteOverseasHealthInsuranceFee()); 
    if (quote.getLoanQuoteScholarship() != null && quote.getLoanQuoteScholarship().doubleValue() > 0.0D)
      loanRequestedAmount = Double.valueOf(loanRequestedAmount.doubleValue() - quote.getLoanQuoteScholarship().doubleValue()); 
    quote.setLoanQuoteLoanRequestedAmount(loanRequestedAmount);
    return quote;
  }

//Added for Slider changes
  public double getChosenAmount() {
    logger.info("CALLING getCaseResponse()");
    double amount = 0.0D;
      logger.info("CALLING1 getChosenAmount()::--loanChosenAmount--" + this.loanChosenAmount);
      amount = this.loanChosenAmount;
      logger.info("CALLING2 getChosenAmount()::" + amount);
    return amount;
  }
  
	public int getLoanTenure() {
		logger.info("CALLING getLoanTenure()");	
		int loanT = 0;
		logger.info("CALLING1 getLoanTenure()::loanTenure::"+loanTenure);		
		loanT = loanTenure;		 
		logger.info("CALLING2 getLoanTenure()::loanTenure::"+loanT);		
		return loanT;
	}
}
