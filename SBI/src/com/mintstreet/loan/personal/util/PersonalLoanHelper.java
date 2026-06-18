package com.mintstreet.loan.personal.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
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
import com.mintstreet.common.entity.MasterEmployer;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterState;
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
import com.mintstreet.loan.personal.bo.impl.PersonalProcessManagerImpl;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanCalls;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.loan.product.entity.MasterPlProduct;

public class PersonalLoanHelper {
  private static final Logger logger = LogManager.getLogger(PersonalLoanHelper.class.getName());
  
  @Autowired
  private PersonalLoanService personalLoanService;
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  @Autowired
  private CommonEngine commonEngine;
  
  @Autowired
  private PersonalProcessManagerImpl processManagerPersonalImpl;
  
  public double loanMaxAmount;
  
  public ApplicationFormPersonalLoanQuote insertLoanQuote(ApplicationFormPersonalLoanQuote quote, Integer bankLmsUserId, Integer trackVisitId) throws SQLException {
    try {
      boolean isContactCenterId = commonService.getBankLmsUserRole(bankLmsUserId);
      if (quote == null) {
        quote = new ApplicationFormPersonalLoanQuote();
        quote.setError("Sorry for the inconvenience, Please start again.");
        return quote;
      } 
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
        if (!ValidatorUtil.isValid(quote.getLoanQuoteWorkStateId())) {
          quote.setError("Please select work state.|quote.loanQuoteStateId");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuoteWorkCityId())) {
          quote.setError("Please select work city.|quote.loanQuoteWorkCityId");
          return quote;
        } 
        if (quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER) && 
          !ValidatorUtil.isValid(quote.getLoanQuoteWorkDistrictId())) {
          quote.setError("Please select work district.|quote.loanQuoteWorkDistrictId");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuoteWorkBranchId())) {
          if (quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER)) {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteWorkDistrictId|1");
          } else {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteWorkCityId|1");
          } 
          return quote;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuoteEmployerName()) && ValidatorUtil.isValidAlpha(quote.getLoanQuoteEmployerName())) {
          quote.setError("Please select alphanumeric value.|quote.loanQuoteEmploayerName");
          return quote;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuoteEmployerName()) && quote.getLoanQuoteEmployerName().contains(Constants.CHAR_SEQ)) {
          quote.setError("Please select alphanumeric value.|quote.loanQuoteEmploayerName");
          return quote;
        } 
        if (quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2 && 
          quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteIndustryType() != null && (quote.getLoanQuoteIndustryType().intValue() == 84 || 
          quote.getLoanQuoteIndustryType().intValue() == 1 || quote.getLoanQuoteIndustryType().intValue() == 2 || quote.getLoanQuoteIndustryType().intValue() == 3 || 
          quote.getLoanQuoteIndustryType().intValue() == 4 || quote.getLoanQuoteIndustryType().intValue() == 15 || quote.getLoanQuoteIndustryType().intValue() == 16 || 
          quote.getLoanQuoteIndustryType().intValue() == 17 || quote.getLoanQuoteIndustryType().intValue() == 19 || quote.getLoanQuoteIndustryType().intValue() == 25 || quote.getLoanQuoteIndustryType().intValue() == 87) && 
          quote.getLoanQuoteSourceId() != null && quote.getLoanQuoteSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue()) {
          if (quote.getLoanQuotePincode() != null) {
            int pincodeInitial = quote.getLoanQuotePincode().intValue() / 10000;
            String pinlastfix = quote.getLoanQuotePincode().toString().substring(3, 6);
            MasterState state = this.commonService.getStateById((quote.getLoanQuoteStateId() != null) ? quote.getLoanQuoteStateId() : null);
            if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null && (
              pincodeInitial < state.getStatePinMinStartPrefix().intValue() || pincodeInitial > state.getStatePinMaxStartPrefix().intValue() || pinlastfix.equals("000"))) {
              if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == quote.getLoanQuoteSourceId().intValue()) {
                quote.setError("Entered pincode does not belong to entered state.");
              } else {
                quote.setError("Entered pincode does not belong to entered state.|quote.loanQuotePincode|2");
              } 
              return quote;
            } 
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteLoanAmountTaken())) {
            quote.setError("Please enter requested loan amount.|quote.loanQuoteLoanAmount|2");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteFirstName())) {
            quote.setError("Please enter first name.|quote.loanQuoteFirstName|2");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteLastName())) {
            quote.setError("Please enter last name.|quote.loanQuoteLastName|2");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteGender())) {
            quote.setError("Please select gender.|quote.loanQuoteGender|1");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteAddress1())) {
            quote.setError("Please enter address1.|quote.loanQuoteAddress1|2");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteAddress2())) {
            quote.setError("Please enter address2.|quote.loanQuoteAddress2|2");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteStateId())) {
            quote.setError("Please select state.|quote.loanQuoteStateId|1");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuoteCityId())) {
            quote.setError("Please select city.|quote.loanQuoteCityId|1");
            return quote;
          } 
          if (!ValidatorUtil.isValid(quote.getLoanQuotePincode())) {
            quote.setError("Please enter pincode.|quote.loanQuotePincode|2");
            return quote;
          } 
          if (quote.getLoanQuotePanCardLater() != null && quote.getLoanQuotePanCardLater().booleanValue()) {
            quote.setLoanQuotePanCardLater(quote.getLoanQuotePanCardLater());
          } else {
            quote.setLoanQuotePanCardLater(Boolean.valueOf(false));
          } 
          if (ValidatorUtil.isValidMobile(quote.getAppMobile()))
            quote.setAppMobile(quote.getAppMobile()); 
          if (quote.getLoanQuoteIndustryType() != null && quote.getLoanQuoteIndustryType().intValue() == 84) {
            if (quote.getLoanQuoteEmployerName() != null) {
              MasterEmployer masterEmployer = this.commonService.getEmployerIdByName(quote.getLoanQuoteEmployerName());
              if (masterEmployer == null) {
                quote.setError("Please enter only registered Nasscom companies.|quote.loanQuoteEmployerName");
              } else if (masterEmployer != null && masterEmployer.getEmployerCategory() != null && masterEmployer.getEmployerCategory().intValue() != quote.getLoanQuoteIndustryType().intValue()) {
                quote.setError("Please enter only registered Nasscom companies.|quote.loanQuoteEmployerName");
              } 
            } 
          } else if ((quote.getLoanQuoteIndustryType() == null || quote.getLoanQuoteIndustryType().intValue() != 1) && quote.getLoanQuoteIndustryType().intValue() != 15 && 
            quote.getLoanQuoteIndustryType().intValue() != 16 && quote.getLoanQuoteIndustryType().intValue() != 17 && 
            quote.getLoanQuoteIndustryType().intValue() != 19 && quote.getLoanQuoteIndustryType().intValue() != 25) {
            quote.getLoanQuoteIndustryType().intValue();
          } 
        } 
        if (quote != null && quote.getLoanQuoteEmploymentNature().intValue() == 2) {
          String identityValidation = this.SbiUtil.getIdentityValidation(quote.getLoanQuotePanCardLater(), quote.getLoanQuotePanCardNo(), quote.getLoanQuoteHaveAadhaarNumber(), quote.getLoanQuoteOtherId(), quote.getLoanQuoteOtherIdNumber(), isContactCenterId);
          if (!"pass".equalsIgnoreCase(identityValidation)) {
            quote.setError(identityValidation);
            return quote;
          } 
        } 
        ValidatorUtil.isValidPanNo(quote.getLoanQuotePanCardNo());
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 13 || quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
        if (!ValidatorUtil.isValid(quote.getLoanQuotePropertyStateId())) {
          quote.setError("Please select work state.|quote.loanQuotePropertyStateId|1");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuotePropertyCityId())) {
          quote.setError("Please select work state.|quote.loanQuotePropertyStateId|1");
          return quote;
        } 
        if (quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER) && 
          !ValidatorUtil.isValid(quote.getLoanQuotePropertyDistrictId())) {
          quote.setError("Please select work city.|quote.loanQuotePropertyCityId|1");
          return quote;
        } 
        if (!ValidatorUtil.isValid(quote.getLoanQuotePropertyBranchId())) {
          if (quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER)) {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePropertyDistrictId|1");
          } else {
            quote.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePropertyCityId|1");
          } 
          return quote;
        } 
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
        String identityValidation = this.SbiUtil.getIdentityValidation(quote.getLoanQuotePanCardLater(), quote.getLoanQuotePanCardNo(), quote.getLoanQuoteHaveAadhaarNumber(), quote.getLoanQuoteOtherId(), quote.getLoanQuoteOtherIdNumber(), isContactCenterId);
        if (!"pass".equalsIgnoreCase(identityValidation)) {
          quote.setError(identityValidation);
          return quote;
        } 
      } else if (quote.getLoanQuoteOrgName() != null && (quote.getLoanQuoteOrgName().equals("") || quote.getLoanQuoteOrgName().equals("0"))) {
        quote.setError("Organization Name is required.|quote.loanQuoteOrgName|2");
        return quote;
      } 
      quote = getUpdateQuote(quote);
      quote.setLoanQuoteMonthStartDateOflease(Integer.valueOf(1));
      quote.setLoanQuoteLeadId(SessionUtil.getLeadId());
      quote.setLoanQuoteCreatedLmsUserId((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID);
      quote.setLoanQuoteEntryTime(new Date());
      quote.setLoanQuoteUpdatedTime(new Date());
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      quote.setLoanQuoteVisitId(trackVisitId);
      Double loanRequestedAmount = Double.valueOf(0.0D);
      quote.setLoanQuoteLoanRequestedAmount(loanRequestedAmount);
      quote.setLoanQuoteActive("Y");
      quote.setLoanQuoteDeleted("N");
      if (bankLmsUserId != null && 
        quote.getLoanQuoteCreatedLmsUserId() == null)
        quote.setLoanQuoteCreatedLmsUserId((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_ID_INTEGER); 
      if (quote.getLoanQuoteYearStartDateOfCurrentBusiness() != null && quote.getLoanQuoteMonthStartDateOfCurrentBusiness() != null) {
        String dateInString = quote.getLoanQuoteYearStartDateOfCurrentBusiness() + "-" + ((quote.getLoanQuoteMonthStartDateOfCurrentBusiness().intValue() > 9) ? ""+quote.getLoanQuoteMonthStartDateOfCurrentBusiness() : ("0" + quote.getLoanQuoteMonthStartDateOfCurrentBusiness())) + "-01";
        Date dateFormat = DateUtil.changeDateFormatToDate(dateInString, "dd-MM-yyyy");
        quote.setLoanQuoteWorkExperience(Integer.valueOf(DateUtil.getAge(dateFormat)));
      } 
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBM) {
        MasterBranch masterBranch = null;
        if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
          masterBranch = this.commonService.getBranchById(quote.getLoanQuoteWorkBranchId());
          quote.setLoanQuoteBranchIsCaptive(masterBranch.getBranchIsCaptive());
        } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 12 || quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
          masterBranch = this.commonService.getBranchById(quote.getLoanQuotePropertyBranchId());
          quote.setLoanQuoteBranchIsCaptive(masterBranch.getBranchIsCaptive());
        } 
      } 
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT && 
        quote.getLoanQuoteCheckOffType() != null) {
        quote.setLoanQuoteLoanAccountType(quote.getLoanQuoteCheckOffType());
        quote.setLoanQuoteCheckOffType(null);
      } 
      if (quote.getLoanQuoteLoanAccountType() != null && quote.getLoanQuoteLoanAccountType().intValue() > 0) {
        quote.setLoanQuoteLoanAccountType(quote.getLoanQuoteLoanAccountType());
      } 
      if (quote.getLoanQuotePanCardNo() != null)
        quote.setLoanQuotePanCardNo(quote.getLoanQuotePanCardNo()); 
      if (quote.getLoanQuoteOtherIdNumber() != null)
        quote.setLoanQuoteOtherIdNumber(quote.getLoanQuoteOtherIdNumber()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteFirstName()))
        if (quote.getLoanQuoteFirstName().contains(",")) {
          String[] firstName = quote.getLoanQuoteFirstName().split(",");
          quote.setLoanQuoteFirstName(firstName[0]);
        } else {
          quote.setLoanQuoteFirstName(quote.getLoanQuoteFirstName());
        }  
      if (ValidatorUtil.isValid(quote.getLoanQuoteLastName()))
        quote.setLoanQuoteLastName(quote.getLoanQuoteLastName()); 
      Integer branchId = null;
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
        branchId = quote.getLoanQuoteWorkBranchId();
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 12 || quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
        branchId = quote.getLoanQuotePropertyBranchId();
      } 
      MasterBranch branch = null;
      if (branchId != null) {
        branch = commonService.getBranchById(branchId);
        if (branch != null && branch.getBranchLocationTypeLap() != null) {
          quote.setLoanQuoteBranchLocationId(branch.getBranchLocationTypeLap());
        } else {
          quote.setLoanQuoteBranchLocationId(Integer.valueOf(0));
        } 
      } 
      if (quote.getLoanQuotePensionType() != null && quote.getLoanQuotePensionType().intValue() != 6 && quote.getLoanQuotePensionType().intValue() != 7 && quote.getLoanQuotePensionType().intValue() != 8)
        quote.getLoanQuotePensionType().intValue(); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionType()))
        quote.setLoanQuotePensionType(quote.getLoanQuotePensionType()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingStateId())) {
        quote.setLoanQuotePensionPayingStateId(quote.getLoanQuotePensionPayingStateId());
        quote.setLoanQuoteStateId(quote.getLoanQuotePensionPayingStateId());
      } 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingCityId())) {
        quote.setLoanQuotePensionPayingCityId(quote.getLoanQuotePensionPayingCityId());
        quote.setLoanQuoteCityId(quote.getLoanQuotePensionPayingCityId());
      } 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingDistrId()))
        quote.setLoanQuotePensionPayingDistrId(quote.getLoanQuotePensionPayingDistrId()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingBranchId()))
        quote.setLoanQuotePensionPayingBranchId(quote.getLoanQuotePensionPayingBranchId()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayOrdNumber()))
        quote.setLoanQuotePensionPayOrdNumber(quote.getLoanQuotePensionPayOrdNumber()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteMonthPensionAmt()))
        quote.setLoanQuoteMonthPensionAmt(quote.getLoanQuoteMonthPensionAmt()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreEMIsOther()))
        quote.setLoanQuotePreEMIsOther(quote.getLoanQuotePreEMIsOther()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteIndustryType()))
        quote.setLoanQuoteIndustryType(quote.getLoanQuoteIndustryType()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteEmployerName()))
        quote.setLoanQuoteEmployerName(quote.getLoanQuoteEmployerName()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreferredStateId())) {
        quote.setLoanQuotePreferredStateId(quote.getLoanQuotePreferredStateId());
        quote.setLoanQuoteStateId(quote.getLoanQuotePreferredStateId());
      } 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreferredCityId())) {
        quote.setLoanQuotePreferredCityId(quote.getLoanQuotePreferredCityId());
        quote.setLoanQuoteCityId(quote.getLoanQuotePreferredCityId());
      } 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId()))
        quote.setLoanQuotePreferredDistrictId(quote.getLoanQuotePreferredDistrictId()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranch()))
        quote.setLoanQuotePreferredLoanBranch(quote.getLoanQuotePreferredLoanBranch()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranchId()))
        quote.setLoanQuotePreferredLoanBranchId(quote.getLoanQuotePreferredLoanBranchId()); 
      if (ValidatorUtil.isValid(quote.getLoanQuotePensionAccountNumber()))
        quote.setLoanQuotePensionAccountNumber(quote.getLoanQuotePensionAccountNumber()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteEmploymentNature()))
        quote.setLoanQuoteEmploymentNature(quote.getLoanQuoteEmploymentNature()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteLastMonthSalaryCredited()))
        quote.setLoanQuoteLastMonthSalaryCredited(quote.getLoanQuoteLastMonthSalaryCredited()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteAverageOfLastSalary()))
        quote.setLoanQuoteAverageOfLastSalary(quote.getLoanQuoteAverageOfLastSalary()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCurrentContractStartYear()))
        quote.setLoanQuoteCurrentContractStartYear(quote.getLoanQuoteCurrentContractStartYear()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCurrentContractStartMonth()))
        quote.setLoanQuoteCurrentContractStartMonth(quote.getLoanQuoteCurrentContractStartMonth()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCurrentContractEndYear()))
        quote.setLoanQuoteCurrentContractEndYear(quote.getLoanQuoteCurrentContractEndYear()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCurrentContractEndMonth()))
        quote.setLoanQuoteCurrentContractEndMonth(quote.getLoanQuoteCurrentContractEndMonth()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteVariableMonthlyPay()))
        quote.setLoanQuoteVariableMonthlyPay(quote.getLoanQuoteVariableMonthlyPay()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCoapplicantRelationshipId()))
        quote.setLoanQuoteCoapplicantRelationshipId(quote.getLoanQuoteCoapplicantRelationshipId()); 
      if (ValidatorUtil.isValid(quote.getLoanQuoteCoapplicantName()))
        quote.setLoanQuoteCoapplicantName(quote.getLoanQuoteCoapplicantName()); 
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 1)
        quote.setLoanQuoteNetMonthlySalary(Double.valueOf(Math.min(quote.getLoanQuoteLastMonthSalaryCredited().doubleValue(), quote.getLoanQuoteAverageOfLastSalary().doubleValue()))); 
      if (quote.getLoanQuoteIncomeFromRegaularSource() != null)
        quote.setLoanQuoteIncomeFromRegularSource(quote.getLoanQuoteIncomeFromRegaularSource()); 
      if (quote.getCbsRelationShipId() != null)
        quote.setCbsRelationShipId(quote.getCbsRelationShipId()); 
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2 && quote.getLoanQuoteIndustryType() != null)
        if (quote.getLoanQuoteIndustryType().intValue() == 2 || quote.getLoanQuoteIndustryType().intValue() == 3 || 
          quote.getLoanQuoteIndustryType().intValue() == 4 || quote.getLoanQuoteIndustryType().intValue() == 25) {
          quote.setLoanQuoteIndustryTypeCat(Integer.valueOf(2));
        } else if (quote.getLoanQuoteIndustryType().intValue() == 87) {
          quote.setLoanQuoteIndustryTypeCat(Integer.valueOf(0));
        } else {
          quote.setLoanQuoteIndustryTypeCat(Integer.valueOf(1));
        }  
      
      logger.info("bankLmsUserId:: " + bankLmsUserId + "::: and quote consentId is:: " + quote.getLoanQuoteConsentId());
      Integer personalTypeId = 0;
      if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_PERSONAL)) {
    	  personalTypeId = Constants.PERSONAL_LOAN_ID;
      } else if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_GOLD)) {
    	  personalTypeId = Constants.APP_PL_TYPE_GOLD;
      }
      if (Constants.OTHER_USER_ID.equals(bankLmsUserId)) {
    	  Consent consent = commonService.getConsentByLoanAndCustomerType(personalTypeId, "NTB");
    	  Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
    	  quote.setLoanQuoteConsentId(consentId); 
      } else {
    	  if (quote.getLoanQuoteConsentId() != null) {
    		  quote.setLoanQuoteConsentId(quote.getLoanQuoteConsentId()); 
    	  }
      }
      logger.info("ConsentId before save in quote table:::" + quote.getLoanQuoteConsentId());
      
      quote = this.personalLoanService.save(quote);
      return quote;
    } catch (SQLException e) {
        logger.info("PersonalLoanHelper.java LNo : 227 : Exception Caught", e);
        return null;
      } catch (Exception e) {
      logger.info("PersonalLoanHelper.java LNo : 227 : Exception Caught", e);
      return null;
    } 
  }
  
  public ApplicationFormPersonalLoan insertAppLoan(ApplicationFormPersonalLoanQuote quote, ApplicationFormPersonalLoan application, Integer bankLmsUserId, Integer lmsUserIntermediaryId) throws SQLException {
    try {
      if (quote == null) {
        if (application == null)
          application = new ApplicationFormPersonalLoan(); 
        application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
        return application;
      } 
      if (application != null) {
        boolean isExistingMobile = false;
        if (application.getAppApplyingFrom() == 2) {
          if (quote.getAppNRIMobileNo() != null && application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo())) {
            application.setAppISDCode(quote.getAppISDCode());
            application.setAppMobileNo(quote.getAppNRIMobileNo());
            quote.setAppApplyingFrom(application.getAppApplyingFrom());
            SessionUtil.setISDCode(quote.getAppISDCode());
            SessionUtil.setMobile(quote.getAppNRIMobileNo());
            isExistingMobile = true;
          } else {
            application.setAppISDCode(quote.getAppISDCode());
            application.setAppMobileNo(quote.getAppNRIMobileNo());
            SessionUtil.setISDCode(quote.getAppISDCode());
            SessionUtil.setMobile(quote.getAppNRIMobileNo());
            quote.setAppApplyingFrom(application.getAppApplyingFrom());
          } 
        } else if (quote.getAppMobile() != null && application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppMobile())) {
          if (quote.getAppMobile() != null) {
            application.setAppMobileNo(quote.getAppMobile());
            SessionUtil.setMobile(quote.getAppMobile());
          } 
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          isExistingMobile = true;
        } else {
          if (quote.getAppMobile() != null) {
            application.setAppMobileNo(quote.getAppMobile());
            SessionUtil.setMobile(quote.getAppMobile());
          } 
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
        } 
        if (isExistingMobile && !"Y".equalsIgnoreCase(application.getAppMobileVerified())) {
          application.setAppMobileVerified("N");
          String isdCode = application.getAppISDCode();
          if (isdCode != null && "91".equals(isdCode)) {
            application.setAppResTypeAtVerified(Integer.valueOf(1));
          } else {
            application.setAppResTypeAtVerified(Integer.valueOf(2));
          } 
        } 
        if (!"Y".equalsIgnoreCase(application.getAppEmailVerified()))
          application.setAppEmailVerified("N"); 
      } else {
        application = new ApplicationFormPersonalLoan();
        if (quote.getAppApplyingFrom() == 2) {
          if (quote.getAppNRIMobileNo() != null && quote.getAppISDCode() != null) {
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
        String isdCode = application.getAppISDCode();
        if (isdCode != null && "91".equals(isdCode)) {
          application.setAppResTypeAtVerified(Integer.valueOf(1));
        } else {
          application.setAppResTypeAtVerified(Integer.valueOf(2));
        } 
      } 
      
      application.setAppApplyingFrom(quote.getAppApplyingFrom());
      if (quote.getLoanQuoteDateOfBirthDT() != null)
        application.setAppDobDT(quote.getLoanQuoteDateOfBirthDT()); 
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
              ApplicationFormLead lead = commonService.getLeadById(SessionUtil.getLeadId());
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
        if (quote.getLoanQuoteFirstName().contains(",")) {
          String[] firstName = quote.getLoanQuoteFirstName().split(",");
          application.setAppFirstName(firstName[0]);
        } else {
          application.setAppFirstName(quote.getLoanQuoteFirstName());
        }  
      
      if (quote.getLoanQuoteGender() != null)
          application.setAppGender(quote.getLoanQuoteGender()); 
      
      if (quote.getLoanQuotePanCardNo() != null)
        application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
      if (quote.getLoanQuoteOtherId() != null)
        application.setAppOtherId(quote.getLoanQuoteOtherId()); 
      if (quote.getLoanQuoteOtherIdNumber() != null)
        application.setAppOtherIdNumber(quote.getLoanQuoteOtherIdNumber()); 
      if (quote.getLoanQuoteHaveCoOperativeBankAcc() != null)
        application.setAppHaveCoOperativeBankAcc(quote.getLoanQuoteHaveCoOperativeBankAcc()); 
      if ((quote.getLoanQuoteIndustryType() != null && (quote.getLoanQuoteIndustryType().intValue() == 84 || 
        quote.getLoanQuoteIndustryType().intValue() == 1 || quote.getLoanQuoteIndustryType().intValue() == 2 || quote.getLoanQuoteIndustryType().intValue() == 3 || 
        quote.getLoanQuoteIndustryType().intValue() == 4 || quote.getLoanQuoteIndustryType().intValue() == 15 || quote.getLoanQuoteIndustryType().intValue() == 16 || 
        quote.getLoanQuoteIndustryType().intValue() == 17 || quote.getLoanQuoteIndustryType().intValue() == 19 || quote.getLoanQuoteIndustryType().intValue() == 25 || quote.getLoanQuoteIndustryType().intValue() == 87)) || (
        quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 27)) {
        if (quote.getLoanQuoteEmployerName() != null) {
          MasterEmployer masterEmployer = commonService.getEmployerIdByName(quote.getLoanQuoteEmployerName());
          if (masterEmployer != null && masterEmployer.getEmployerCategory() != null) {
            masterEmployer.getEmployerCategory().intValue();
            quote.getLoanQuoteIndustryType().intValue();
          } 
        } 
        if (quote.getLoanQuoteNetMonthlySalary() != null)
          quote.getLoanQuoteNetMonthlySalary().intValue(); 
        if (quote.getLoanQuoteMiddleName() != null)
          application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
        if (quote.getLoanQuoteLastName() != null)
          application.setAppLastName(quote.getLoanQuoteLastName()); 
        if (quote.getLoanQuoteGender() != null)
          application.setAppGender(quote.getLoanQuoteGender()); 
        if (quote.getLoanQuoteAddress1() != null)
          application.setAppAddress1(quote.getLoanQuoteAddress1()); 
        if (quote.getLoanQuoteAddress2() != null)
          application.setAppAddress2(quote.getLoanQuoteAddress2()); 
        if (quote.getLoanQuoteAddressLandmark() != null)
          application.setAppAddressLandmark(quote.getLoanQuoteAddressLandmark()); 
        if (quote.getLoanQuoteStateId() != null)
          application.setAppStateId(quote.getLoanQuoteStateId()); 
        if (quote.getLoanQuoteCityId() != null)
          application.setAppCityId(quote.getLoanQuoteCityId()); 
        if (quote.getLoanQuoteDistrictId() != null)
          application.setAppDistrictId(quote.getLoanQuoteDistrictId()); 
        if (quote.getLoanQuotePincode() != null)
          application.setAppPincode(quote.getLoanQuotePincode()); 
        if (quote.getLoanQuotePanCardNo() != null) {
          application.setAppPanCardNo(quote.getLoanQuotePanCardNo());
        } else {
          application.setAppPanCardNo(null);
        } 
        if (quote.getLoanQuoteOtherId() != null)
          application.setAppOtherId(quote.getLoanQuoteOtherId()); 
        if (quote.getLoanQuoteOtherIdNumber() != null)
          application.setAppOtherIdNumber(quote.getLoanQuoteOtherIdNumber()); 
        if (quote.getLoanQuoteIndustryType() != null)
          application.setAppIndustryTypeId(quote.getLoanQuoteIndustryType()); 
        if (quote.getLoanQuotePanCardLater() != null) {
          application.setAppPanCardLater(quote.getLoanQuotePanCardLater());
        } else {
          application.setAppPanCardLater(Boolean.valueOf(false));
        } 
      } 
      if (application.getAppHotLeadCreatedLmsUserId() == null && 
        SessionUtil.getApplicationType() != null && 
        SessionUtil.getApplicationType().intValue() == 2)
        application.setAppHotLeadCreatedLmsUserId(Integer.valueOf((bankLmsUserId != null) ? bankLmsUserId.intValue() : 0)); 
      if (!"Y".equalsIgnoreCase(application.getAppMobileVerified()) && 
        SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 1 && 
        SessionUtil.getLeadId() != null) {
        ApplicationFormLead lead = commonService.getLeadById(SessionUtil.getLeadId());
        if (lead != null && 
          lead.getLeadMobileVerificationCodeVerified() != null && "Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified()))
          application.setAppMobileVerified("Y"); 
      } 
      if (quote.getLoanQuoteEmployerName() != null) {
        application.setAppLoanEmployerName(quote.getLoanQuoteEmployerName());
        MasterEmployer employer = commonService.getEmployerIdByName(quote.getLoanQuoteEmployerName());
        if (employer != null) {
          quote.setLoanQuoteEmployerNameId(employer.getEmployerCompanyId());
          quote.setLoanQuoteEmployerCat(employer.getEmployerCategory());
        } else {
          quote.setLoanQuoteEmployerCat(Integer.valueOf(0));
        } 
      } 
      if (quote.getLoanQuoteInstituteName() != null) {
        application.setAppLoanEmployerName(quote.getLoanQuoteInstituteName());
        MasterInstitute institute = personalLoanService.getInstituteByInstituteName(quote.getLoanQuoteInstituteName());
        if (institute != null) {
          quote.setLoanQuoteInstituteNameId(institute.getInstituteId());
          quote.setLoanQuoteInstituteCat(institute.getInstituteCategeroy());
        } else {
          quote.setLoanQuoteInstituteCat("");
        } 
      } 
      if (quote.getAppEmail() != null) {
        application.setAppWorkEmail(quote.getAppEmail());
        SessionUtil.setEmail(quote.getAppEmail());
      } 
      if (quote.getAppMobile() != null) {
    	  application.setAppMobileNo(quote.getAppMobile());
    	  application.setAppMobileNumberMask(quote.getAppMobile().replaceAll("\\d(?=\\d{4})", "*"));
      }
      application.setAppLeadUpdateTime(new Date());
      application.setAppCoapplicantRelationTypeId(quote.getLoanQuoteCoapplicantRelationshipId());
      application.setAppQuoteId(quote.getLoanQuoteId());
      if (quote.getLoanQuoteLoanTenure() != null)
        application.setAppLoanTenure(quote.getLoanQuoteLoanTenure()); 
      application.setAppActive("Y");
      application.setAppDeleted("N");
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 13 || quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
        
    	  if (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
    	   	  if (ValidatorUtil.isValid(quote.getLoanQuoteStateId())) {
    	          application.setAppStateId(quote.getLoanQuoteStateId());
    	        } else {
    	          application.setError("Please select state.|quote.loanQuoteStateId|1");
    	          return application;
    	        } 
    	        if (ValidatorUtil.isValid(quote.getLoanQuoteCityId())) {
    	          application.setAppCityId(quote.getLoanQuoteCityId());
    	        } else {
    	          application.setError("Please select city.|quote.loanQuoteStateId|1");
    	          return application;
    	        }
    	  } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
    	   	  if (ValidatorUtil.isValid(quote.getLoanQuotePropertyStateId())) {
    	          application.setAppStateId(quote.getLoanQuotePropertyStateId());
    	        } else {
    	          application.setError("Please select property state.|quote.loanQuotePropertyStateId|1");
    	          return application;
    	        } 
    	        if (ValidatorUtil.isValid(quote.getLoanQuotePropertyCityId())) {
    	          application.setAppCityId(quote.getLoanQuotePropertyCityId());
    	        } else {
    	          application.setError("Please select property city.|quote.loanQuotePropertyCityId|1");
    	          return application;
    	        }
    	  }
        
        if (quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuotePropertyDistrictId())) {
            application.setAppDistrictId(quote.getLoanQuotePropertyDistrictId());
          } else {
            application.setError("Please select property city.|quote.loanQuotePropertyCityId|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuotePropertyBranchId())) {
          application.setAppBranchId(quote.getLoanQuotePropertyBranchId());
        } else {
          if (quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePropertyDistrictId|1");
          } else {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePropertyCityId|1");
          } 
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePropertyBranchId()))
          if (bankLmsUserId != null) {
            if (application.getAppPreviousBranchId() != null && application.getAppBranchId() != null) {
              if (!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppBranchId().toString())) {
                StringBuilder message = new StringBuilder("Application moved from ");
                MasterBranch masterBranch = this.commonService.getBranchById(application.getAppPreviousBranchId());
                if (masterBranch != null)
                  message.append(String.valueOf(masterBranch.getBranchName()) + " (" + masterBranch.getBranchCode() + ") "); 
                application.setAppPreviousBranchId(application.getAppBranchId());
                masterBranch = this.commonService.getBranchById(application.getAppBranchId());
                if (masterBranch != null)
                  message.append(" to " + masterBranch.getBranchName() + " (" + masterBranch.getBranchCode() + ") "); 
                if (application.getAppDataSourceId() != null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)) {
                  if (SessionUtil.getLeadId() == null) {
                    ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.PERSONAL_LOAN_ID);
                    this.commonService.insertCallLog((lead != null) ? lead.getLeadId() : null, (bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID, application.getAppLoanStatusId(), message.toString());
                  } 
                } else {
                  insertCallLog(application.getAppSeqId(), ((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID).intValue(), application.getAppLoanStatusId().intValue(), message.toString(), null, true);
                } 
              } 
            } else {
              application.setAppPreviousBranchId(application.getAppBranchId());
            } 
          } else {
            application.setAppPreviousBranchId(application.getAppBranchId());
          }  
        if (bankLmsUserId == null) {
          application.setAppOfficeCityId(Integer.valueOf(0));
          application.setAppOfficeStateId(Integer.valueOf(0));
          application.setAppOfficeDistrictId(Integer.valueOf(0));
          if (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
            if (quote.getLoanQuotePropertyBranchId() != null) {
              MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePropertyBranchId());
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
              } 
            } 
          } else if (quote.getLoanQuotePropertyCityId() != null) {
            if (!quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER)) {
              List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuotePropertyCityId(), null, null, null, null, null);
              if (branches != null && !branches.isEmpty()) {
                if (branches.size() == 1) {
                  MasterBranch masterBranch = branches.get(0);
                  if (masterBranch != null) {
                    Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                    application.setAppSalesTeamId(salesTeamId);
                  } 
                } 
              } else if (quote.getLoanQuotePropertyBranchId() != null) {
                MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePropertyBranchId());
                if (masterBranch != null) {
                  Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                  application.setAppSalesTeamId(salesTeamId);
                } 
              } 
            } else if (quote.getLoanQuotePropertyBranchId() != null) {
              MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePropertyBranchId());
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
              } 
            } 
          } 
        } 
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 23) {
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranch()))
          application.setAppPreferredLoanBranch(quote.getLoanQuotePreferredLoanBranch()); 
        if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingStateId())) {
          application.setAppPensionPayingStateId(quote.getLoanQuotePensionPayingStateId());
        } else {
          application.setError("Please select pension paying state.|quote.loanQuotePensionPayingStateId|1");
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingCityId())) {
          application.setAppPensionPayingCityId(quote.getLoanQuotePensionPayingCityId());
        } else {
          application.setError("Please select pension paying state.|quote.loanQuotePensionPayingStateId|1");
          return application;
        } 
        if (application.getAppPensionPayingCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingDistrId())) {
            application.setAppPensionPayingDistrId(quote.getLoanQuotePensionPayingDistrId());
          } else {
            application.setError("Please select pension city.|quote.loanQuotePensionPayingCityId|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuotePensionPayingBranchId())) {
          application.setAppPensionPayingBranchId(quote.getLoanQuotePensionPayingBranchId());
        } else {
          if (quote.getLoanQuotePensionPayingCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePensionPayingDistrictId|1");
          } else {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePensionPayingCityId|1");
          } 
          return application;
        } 
        if (ValidatorUtil.isValid(application.getAppSalaryAccNo()))
          application.setAppPensionAccountNumber(application.getAppSalaryAccNo()); 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranchId()))
          if (bankLmsUserId != null) {
            if (application.getAppPreviousBranchId() != null && application.getAppPreferredLoanBranchId() != null) {
              if (!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppPreferredLoanBranchId().toString())) {
                StringBuilder message = new StringBuilder("Application moved from ");
                MasterBranch masterBranch = this.commonService.getBranchById(application.getAppPreviousBranchId());
                if (masterBranch != null) {
                  message.append(String.valueOf(masterBranch.getBranchName()) + " (" + masterBranch.getBranchCode() + ") ");
                  application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
                } 
                application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
                masterBranch = this.commonService.getBranchById(application.getAppPreferredLoanBranchId());
                if (masterBranch != null)
                  message.append(" to " + masterBranch.getBranchName() + " (" + masterBranch.getBranchCode() + ") "); 
                if (application.getAppDataSourceId() != null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)) {
                  if (SessionUtil.getLeadId() == null) {
                    ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.PERSONAL_LOAN_ID);
                    this.commonService.insertCallLog((lead != null) ? lead.getLeadId() : null, (bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID, application.getAppLoanStatusId(), message.toString());
                  } 
                } else {
                  insertCallLog(application.getAppSeqId(), ((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID).intValue(), application.getAppLoanStatusId().intValue(), message.toString(), null, true);
                } 
              } 
            } else {
              application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
              application.setAppBranchId(application.getAppPreviousBranchId());
            } 
          } else {
            application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
            application.setAppBranchId(application.getAppPreviousBranchId());
          }  
        if (bankLmsUserId == null && 
          quote.getLoanQuotePensionPayingCityId() != null) {
          application.setAppPensionPayingCityId(quote.getLoanQuotePensionPayingCityId());
          if (!quote.getLoanQuotePensionPayingCityId().equals(Constants.OTHER_ID_INTEGER)) {
            List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuotePensionPayingCityId(), null, null, null, null, null);
            if (branches != null && !branches.isEmpty()) {
              if (branches.size() == 1) {
                MasterBranch masterBranch = branches.get(0);
                if (masterBranch != null) {
                  Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                  application.setAppSalesTeamId(salesTeamId);
                  application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
                } 
              } 
            } else if (quote.getLoanQuotePreferredLoanBranchId() != null) {
              MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePreferredLoanBranchId());
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
                application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
              } 
            } 
          } else if (quote.getLoanQuotePreferredLoanBranchId() != null) {
            MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePreferredLoanBranchId());
            if (masterBranch != null) {
              Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
              application.setAppSalesTeamId(salesTeamId);
              application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
            } 
          } 
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredStateId())) {
          application.setAppPreferredStateId(quote.getLoanQuotePreferredStateId());
          application.setAppStateId(quote.getLoanQuotePreferredStateId());
        } else {
          application.setError("Please select preferred state.|quote.loanQuotePreferredStateId|1");
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredCityId())) {
          application.setAppPreferredCityId(quote.getLoanQuotePreferredCityId());
          application.setAppCityId(quote.getLoanQuotePreferredCityId());
        } else {
          application.setError("Please select preferred city.|quote.loanQuotePreferredCityId|1");
          return application;
        } 
        if (quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())) {
            application.setAppPreferredDistrictId(quote.getLoanQuotePreferredDistrictId());
          } else {
            application.setError("Please select preferred city.|quote.getLoanQuotePreferredDistrictId()|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranchId())) {
          application.setAppPreferredLoanBranchId(quote.getLoanQuotePreferredLoanBranchId());
          application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
        } else {
          if (quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError("3" + Constants.BRANCH_NOT_FOUND_MSG + "|quote.getLoanQuotePreferredLoanBranchId()|1");
          } else {
            application.setError("4" + Constants.BRANCH_NOT_FOUND_MSG + "|quote.getLoanQuotePreferredLoanBranchId()|1");
          } 
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())) {
          application.setAppDistrictId(quote.getLoanQuotePreferredDistrictId());
          application.setAppPreferredDistrictId(quote.getLoanQuotePreferredDistrictId());
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreferredLoanBranchId()))
          if (bankLmsUserId != null) {
            if (application.getAppPreviousBranchId() != null && application.getAppPreferredLoanBranchId() != null) {
              if (!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppPreferredLoanBranchId().toString())) {
                StringBuilder message = new StringBuilder("Application moved from ");
                MasterBranch masterBranch = this.commonService.getBranchById(application.getAppPreviousBranchId());
                if (masterBranch != null)
                  message.append(String.valueOf(masterBranch.getBranchName()) + " (" + masterBranch.getBranchCode() + ") "); 
                application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
                masterBranch = this.commonService.getBranchById(application.getAppPreferredLoanBranchId());
                if (masterBranch != null) {
                  message.append(" to " + masterBranch.getBranchName() + " (" + masterBranch.getBranchCode() + ") ");
                  application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
                } 
                if (application.getAppDataSourceId() != null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)) {
                  if (SessionUtil.getLeadId() == null) {
                    ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.PERSONAL_LOAN_ID);
                    this.commonService.insertCallLog((lead != null) ? lead.getLeadId() : null, (bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID, application.getAppLoanStatusId(), message.toString());
                  } 
                } else {
                  insertCallLog(application.getAppSeqId(), ((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID).intValue(), application.getAppLoanStatusId().intValue(), message.toString(), null, true);
                } 
              } 
            } else {
              application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
              application.setAppBranchId(application.getAppPreviousBranchId());
            } 
          } else {
            application.setAppPreviousBranchId(application.getAppPreferredLoanBranchId());
            application.setAppBranchId(application.getAppPreviousBranchId());
          }  
        if (bankLmsUserId == null && 
          quote.getLoanQuotePreferredCityId() != null)
          if (!quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)) {
            List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuotePreferredCityId(), null, null, null, null, null);
            if (branches != null && !branches.isEmpty()) {
              if (branches.size() == 1) {
                MasterBranch masterBranch = branches.get(0);
                if (masterBranch != null) {
                  Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                  application.setAppSalesTeamId(salesTeamId);
                  application.setAppBranchId(quote.getLoanQuotePreferredCityId());
                } 
              } 
            } else if (quote.getLoanQuotePreferredLoanBranchId() != null) {
              MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePreferredLoanBranchId());
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
                application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
                application.setAppPreferredLoanBranchId(quote.getLoanQuotePreferredLoanBranchId());
              } 
            } 
          } else if (quote.getLoanQuotePreferredLoanBranchId() != null) {
            MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuotePreferredLoanBranchId());
            if (masterBranch != null) {
              Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
              application.setAppSalesTeamId(salesTeamId);
              application.setAppBranchId(quote.getLoanQuotePreferredLoanBranchId());
            } 
          }  
        if (quote.getLoanQuotePensionPayOrdNumber() != null)
          application.setAppPensionPayOrdNumber(quote.getLoanQuotePensionPayOrdNumber()); 
        if (quote.getLoanQuotePensionType() != null)
          application.setAppPensionType(quote.getLoanQuotePensionType()); 
        int pensioners = 1;
        if (quote.getLoanQuoteResidentTypeId() != null)
          application.setAppResidenceTypeId(Integer.valueOf(1)); 
        if (quote.getLoanQuoteEmploymentType() != null)
          application.setAppEmploymentType(Integer.valueOf(pensioners)); 
        if (quote.getLoanQuoteEmploymentNature() != null)
          application.setAppEmploymentNature(Integer.valueOf(1)); 
        if (quote.getLoanQuoteModeOfRepayment() != null)
          application.setAppModeOfRepayment(quote.getLoanQuoteModeOfRepayment()); 
        if (quote.getLoanQuoteMonthPensionAmt() != null)
          application.setAppMonthlyPensionAmt(quote.getLoanQuoteMonthPensionAmt()); 
        if (quote.getLoanQuotePensionAccountNumber() != null)
          application.setAppPensionAccountNumber(quote.getLoanQuotePensionAccountNumber()); 
        if (quote.getLoanQuoteGender() != null)
          application.setAppGender(quote.getLoanQuoteGender()); 
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
        if (ValidatorUtil.isValid(quote.getLoanQuoteWorkStateId())) {
          application.setAppOfficeStateId(quote.getLoanQuoteWorkStateId());
        } else {
          application.setError("Please select work state.|quote.loanQuoteWorkStateId|1");
          return application;
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuoteWorkCityId())) {
          application.setAppOfficeCityId(quote.getLoanQuoteWorkCityId());
        } else {
          application.setError("Please select work state.|quote.loanQuoteWorkStateId|1");
          return application;
        } 
        if (quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER))
          if (ValidatorUtil.isValid(quote.getLoanQuoteWorkDistrictId())) {
            application.setAppOfficeDistrictId(quote.getLoanQuoteWorkDistrictId());
          } else {
            application.setError("Please select work city.|quote.loanQuoteWorkCityId|1");
            return application;
          }  
        if (ValidatorUtil.isValid(quote.getLoanQuoteWorkBranchId())) {
          application.setAppBranchId(quote.getLoanQuoteWorkBranchId());
          application.setAppOfficeBranchId(quote.getLoanQuoteWorkBranchId());
        } else {
          if (quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER)) {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteWorkDistirctId|1");
          } else {
            application.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuoteWorkCityId|1");
          } 
          return application;
        } 
        if (bankLmsUserId != null) {
          if (application.getAppPreviousBranchId() != null && application.getAppBranchId() != null) {
            if (!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppBranchId().toString())) {
              StringBuilder message = new StringBuilder("Application moved from ");
              MasterBranch masterBranch = this.commonService.getBranchById(application.getAppPreviousBranchId());
              if (masterBranch != null)
                message.append(String.valueOf(masterBranch.getBranchName()) + " (" + masterBranch.getBranchCode() + ") "); 
              application.setAppPreviousBranchId(application.getAppBranchId());
              masterBranch = this.commonService.getBranchById(application.getAppBranchId());
              if (masterBranch != null)
                message.append(" to " + masterBranch.getBranchName() + " (" + masterBranch.getBranchCode() + ") "); 
              if (application.getAppDataSourceId() != null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)) {
                if (SessionUtil.getLeadId() == null) {
                  ApplicationFormLead lead = this.commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.PERSONAL_LOAN_ID);
                  this.commonService.insertCallLog((lead != null) ? lead.getLeadId() : null, (bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID, application.getAppLoanStatusId(), message.toString());
                } 
              } else {
                insertCallLog(application.getAppSeqId(), ((bankLmsUserId != null) ? bankLmsUserId : Constants.OTHER_USER_ID).intValue(), application.getAppLoanStatusId().intValue(), message.toString(), null, true);
              } 
            } 
          } else {
            application.setAppPreviousBranchId(application.getAppBranchId());
          } 
        } else {
          application.setAppPreviousBranchId(application.getAppBranchId());
        } 
        if (quote.getLoanQuoteWorkCityId() != null)
          if (!quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER)) {
            List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuoteWorkCityId(), null, null, null, null, null);
            if (branches != null && !branches.isEmpty()) {
              if (branches.size() == 1) {
                MasterBranch masterBranch = branches.get(0);
                if (masterBranch != null) {
                  Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                  application.setAppSalesTeamId(salesTeamId);
                } 
              } 
            } else if (quote.getLoanQuoteWorkBranchId() != null) {
              MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuoteWorkBranchId());
              if (masterBranch != null) {
                Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
                application.setAppSalesTeamId(salesTeamId);
              } 
            } 
          } else if (quote.getLoanQuoteWorkBranchId() != null) {
            MasterBranch masterBranch = this.commonService.getBranchById(quote.getLoanQuoteWorkBranchId());
            if (masterBranch != null) {
              Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.PERSONAL_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
              application.setAppSalesTeamId(salesTeamId);
            } 
          }  
        if (bankLmsUserId == null) {
          application.setAppCityId(Integer.valueOf(0));
          application.setAppStateId(Integer.valueOf(0));
          application.setAppDistrictId(Integer.valueOf(0));
        } 
        if (quote.getLoanQuoteStateId() != null)
          application.setAppStateId(quote.getLoanQuoteStateId()); 
        if (quote.getLoanQuoteCityId() != null)
          application.setAppCityId(quote.getLoanQuoteCityId()); 
        if (quote.getLoanQuoteDistrictId() != null)
          application.setAppDistrictId(quote.getLoanQuoteDistrictId()); 
        if (quote.getLoanQuoteIndustryType() != null)
          application.setAppIndustryTypeId(quote.getLoanQuoteIndustryType()); 
        if (quote.getLoanQuotePanCardNo() != null)
          application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
        if (quote.getLoanQuoteAddressLandmark() != null)
          application.setAppAddressLandmark(quote.getLoanQuoteAddressLandmark()); 
        if (quote.getLoanQuoteOtherId() != null)
          application.setAppOtherId(quote.getLoanQuoteOtherId()); 
        if (quote.getLoanQuoteOtherIdNumber() != null)
          application.setAppOtherIdNumber(quote.getLoanQuoteOtherIdNumber()); 
        if (quote.getLoanQuoteGender() != null)
          application.setAppGender(quote.getLoanQuoteGender()); 
        if (quote.getLoanQuotePanCardLater() != null && quote.getLoanQuotePanCardLater().booleanValue()) {
          application.setAppPanCardLater(quote.getLoanQuotePanCardLater());
        } else {
          application.setAppPanCardLater(Boolean.valueOf(false));
        } 
        if (ValidatorUtil.isValid(application.getAppSalaryAccNo()))
          application.setAppSalaryAccNo(application.getAppSalaryAccNo()); 
      } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
        if (quote.getLoanQuotePanCardNo() != null)
          application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
        if (ValidatorUtil.isValid(application.getAppSalaryAccNo()))
          application.setAppSalaryAccNo(application.getAppSalaryAccNo()); 
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
      if (quote.getLoanQuoteLoanPurposeId().intValue() != 27)
        if (ValidatorUtil.isValid(application.getAppBranchId())) {
          Integer[] circleIdNetworkModuleRegionId = commonService.getCircleIdNetworkModuleRegionByBranchId(application.getAppBranchId());
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
      if (quote.getLoanQuoteMiddleName() != null)
        application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
      if (quote.getLoanQuoteLastName() != null)
        application.setAppLastName(quote.getLoanQuoteLastName()); 
      if (quote.getLoanQuoteAddress1() != null)
        application.setAppAddress1(quote.getLoanQuoteAddress1()); 
      if (quote.getLoanQuoteAddress2() != null)
        application.setAppAddress2(quote.getLoanQuoteAddress2()); 
      if (quote.getLoanQuotePincode() != null)
        application.setAppPincode(quote.getLoanQuotePincode()); 
      if (quote.getLoanQuoteRelationshipWithBank() != null)
        application.setAppRelationshipWithBank(quote.getLoanQuoteRelationshipWithBank()); 
      if (quote.getLoanQuoteResidenceTypeId() != null)
        application.setAppResidenceTypeId(quote.getLoanQuoteResidenceTypeId()); 
      if (bankLmsUserId != null) {
        if (this.commonService.isBankUser(bankLmsUserId))
          application.setAppAssignedLmsSalesUserId(bankLmsUserId); 
      } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null && 
        this.commonService.isBankUser(bankLmsUserId)) {
        application.setAppAssignedLmsSalesUserId(SessionUtil.getBankLMSUser().getLmsUserId());
      } 
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
        application.setAppIsSecuredLoan(Integer.valueOf(1));
      } else {
        application.setAppIsSecuredLoan(Integer.valueOf(0));
      } 
			if(quote.getAlternateMobileNumber() !=null) {
				application.setAppAlternateMobileNumber(quote.getAlternateMobileNumber());
				application.setAppAltISDCode(quote.getAppAltISDCode());
				SessionUtil.setalternateMobileNumber(quote.getAppMobile());
		
			}
			application.setOcasID(SbiUtil.getOcasID());
	
			//save consent id in main table
			if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
				application.setAppConsentId(quote.getLoanQuoteConsentId());
			}
	
      application = this.personalLoanService.save(application);
      return application;
    } catch (SQLException e) {
        logger.info("PersonalLoanHelper.java LNo : 655 : Exception Caught", e);
        return null;
      }  catch (Exception e) {
      logger.info("PersonalLoanHelper.java LNo : 655 : Exception Caught", e);
      return null;
    } 
  }
  
  public ApplicationFormPersonalLoanQuote saveCoApplicantDetails(ApplicationFormPersonalLoanQuote quoteNew) throws SQLException {
    try {
      if (quoteNew == null)
        return null; 
      Integer appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
      if (appSeqId == null)
        return null; 
      ApplicationFormPersonalLoan application = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
      if (application == null)
        return null; 
      ApplicationFormPersonalLoanQuote quote = this.personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(application.getAppQuoteId());
      if (quote == null)
        return null; 
      if (quoteNew.getLoanQuoteCoapplicantRelationshipId() != null)
        quote.setLoanQuoteCoapplicantRelationshipId(quoteNew.getLoanQuoteCoapplicantRelationshipId()); 
      quote = this.personalLoanService.save(quote);
      return quote;
    } catch (SQLException e) {
        logger.info("PersonalLoanHelper.java LNo : 685 : Exception Caught", e);
        return null;
      } catch (Exception e) {
      logger.info("PersonalLoanHelper.java LNo : 685 : Exception Caught", e);
      return null;
    } 
  }
  
  public void insertCallLog(Integer callApplicationId, int callUserId, int callStatusId, String message, Date callDocPickupTime, boolean displayLog) {
    try {
      ApplicationFormPersonalLoanCalls callsLog = new ApplicationFormPersonalLoanCalls();
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
      if (callStatusId == 0)
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
      callsLog = this.personalLoanService.save(callsLog);
    } catch (SQLException e) {
        logger.info("PersonalLoanHelper.java LNo : 690 : Exception Caught", e);
      }  catch (Exception e) {
      logger.info("PersonalLoanHelper.java LNo : 690 : Exception Caught", e);
    } 
  }
  
  public LoanScenarioBean callBREAgain(ApplicationFormPersonalLoan application, ApplicationFormPersonalLoanQuote quote, String ajaxPostUrl, Integer bankLMSUserId, boolean isLoggin) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      Integer previousCompanyCategory = null;
      Double previousPreEmi = null;
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBBJ) {
        previousCompanyCategory = quote.getLoanQuoteEmployerCat();
        quote.setLoanQuoteEmployerCat(Integer.valueOf(1));
      }  
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
        if (ValidatorUtil.isValid(quote.getLoanQuotePreEMIs())) {
          previousPreEmi = quote.getLoanQuotePreEMIs();
        } 
        if (ValidatorUtil.isValid(quote.getLoanQuotePreEMIsOther())) {
          quote.setLoanQuotePreEMIs(Double.valueOf(((previousPreEmi != null) ? previousPreEmi.doubleValue() : 0.0D) + quote.getLoanQuotePreEMIsOther().doubleValue()));
        } 
      } 
      boolean isEligibleToCallCIBIL = false;
      if (isLoggin)
        if (((application.getAppCbsRelationShipId() != null && application.getAppCbsRelationShipId().intValue() == 2) || (quote.getCbsRelationShipId() != null && quote.getCbsRelationShipId().intValue() == 2)) && 
          quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteEmploymentNature().intValue() == 2 && quote.getLoanQuoteSourceId() != null && quote.getLoanQuoteSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue()) {
          isEligibleToCallCIBIL = true;
        } else if (quote.getLoanQuoteSourceId() != null && quote.getLoanQuoteSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() && 
          quote.getLoanQuoteLoanPurposeId() != null) {
          if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 && 
            quote.getLoanQuoteIndustryType() != null) {
            if (quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2)
              isEligibleToCallCIBIL = true; 
          }
        }
      if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
    	  isEligibleToCallCIBIL = true;
      }
      if (isEligibleToCallCIBIL) {
    	  Integer count = this.commonService.getCountByAppIdAndLoanType(application.getAppSeqId().toString(), "A12", "SUCCESS");
    	  if (count != null && count <= 0) {
    		  try {
	            if (quote != null) {
	              //new code for cibil call
	              quote = processManagerPersonalImpl.cibilCallForPersonalLoan(quote, application);
	              Integer appCibilScore = quote.getLoanQuoteCibilScore() != null ? quote.getLoanQuoteCibilScore() : -1 ;
	              logger.info("PersonalProcessManagerImpl.java LNo: 1273 :: appCibilScore :: " + appCibilScore);
	              application.setAppCibilScore(appCibilScore);
	            }
	          } catch (Exception eob) {
	            logger.info("PersonalProcessManagerImpl.java LNo: 1675 :: Calling :: ", eob);
	          }
    	  }
      }
      quote = getUpdateQuote(quote);
      if(quote.getLoanQuoteLoanPurposeId().intValue() == 27)
    	  previousPreEmi = 0.0D;
      JSONObject engineRequest = JSONUtil.beanObjectToJSONObjct(quote);
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBBJ)
        quote.setLoanQuoteEmployerCat(previousCompanyCategory); 
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
        quote.setLoanQuotePreEMIs(previousPreEmi);
      } 
      engineRequest.put("appSubTypeId", application.getAppSubTypeId());
      engineRequest = SbiUtil.getDBCredentialForHelper(engineRequest);
      if (quote.getLoanQuoteSourceId() != null && quote.getLoanQuoteSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue()) {
       
    	  if (application != null && application.getAppCibilScore() != null)
        	engineRequest.put("appCibilScore", application.getAppCibilScore());
        
        if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteIndustryType() != null && (quote.getLoanQuoteIndustryType().intValue() == 84 || 
          quote.getLoanQuoteIndustryType().intValue() == 1 || quote.getLoanQuoteIndustryType().intValue() == 15 || quote.getLoanQuoteIndustryType().intValue() == 16 || 
          quote.getLoanQuoteIndustryType().intValue() == 17 || quote.getLoanQuoteIndustryType().intValue() == 19 || quote.getLoanQuoteIndustryType().intValue() == 25 || 
          quote.getLoanQuoteIndustryType().intValue() == 5 || quote.getLoanQuoteIndustryType().intValue() == 18 || quote.getLoanQuoteIndustryType().intValue() == 87))
          if (quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2) {
            //engineRequest.put("appCibilScore", ((quote.getLoanQuoteCibilScore() != null) ? quote.getLoanQuoteCibilScore().intValue() : -1));
            engineRequest.put("appCibilScore", 
		    		   (quote.getLoanQuoteCibilScore() != null) ? quote.getLoanQuoteCibilScore().intValue() : (application.getAppCibilScore() !=null ? application.getAppCibilScore().intValue() : -1));
          } else {
            if (engineRequest.has("appCibilScore"))
              engineRequest.remove("appCibilScore"); 
            if (engineRequest.has("loanQuoteCoapplicantDateOfBirth"))
              engineRequest.remove("loanQuoteCoapplicantDateOfBirth"); 
            if (engineRequest.has("loanQuoteCoapplicantEmploymentTypeId"))
              engineRequest.remove("loanQuoteCoapplicantEmploymentTypeId"); 
            if (engineRequest.has("loanQuoteCoapplicantNetMonthlyIncome"))
              engineRequest.remove("loanQuoteCoapplicantNetMonthlyIncome"); 
          }  
        if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteEmploymentNature().intValue() == 2) {
          if (quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1)
            engineRequest.put("loanQuoteIsAadhaarNumber", quote.getLoanQuoteHaveAadhaarNumber()); 
          if (quote.getLoanQuoteLoanAccountType() != null)
            engineRequest.put("loanQuoteCheckOffValue", quote.getLoanQuoteLoanAccountType()); 
          if (quote.getCbsRelationShipId() != null) {
            engineRequest.put("cbsRelationShipId", quote.getCbsRelationShipId());
          } else if (application.getAppCbsRelationShipId() != null) {
            engineRequest.put("cbsRelationShipId", application.getAppCbsRelationShipId());
            quote.setCbsRelationShipId(application.getAppCbsRelationShipId());
          } 
        } 
        if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
          if (quote.getLoanQuotePanCardNo() != null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1) {
            engineRequest.put("loanQuoteAadhaarPanStatus", 1);
          } else if ((quote.getLoanQuotePanCardNo() == null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1) || (quote.getLoanQuotePanCardNo() != null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 2)) {
            engineRequest.put("loanQuoteAadhaarPanStatus", 2);
          } else {
            engineRequest.put("loanQuoteAadhaarPanStatus", 3);
          } 
          engineRequest.put("loanQuoteLoanRequestedAmount", quote.getLoanQuoteLoanAmountTaken());
          engineRequest.put("loanQuotePreEMIs", quote.getLoanQuotePreEMIs());
          engineRequest.put("loanQuoteIncomeFromRegaularSource", quote.getLoanQuoteIncomeFromRegaularSource());
        } 
      } 
      JSONObject engineResponseJson = null;
      if (quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
	      if (Constants.IS_ENGINE_OBF) {
	        JSONObject finalEngineRequest = MapperUtil.convertPersonalLoan(engineRequest);
	        engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + finalEngineRequest, Constants.PERSONAL_LOAN_ID);
	      } else {
	        engineResponseJson = this.commonEngine.callingRuleEngine("requestData=" + engineRequest, Constants.PERSONAL_LOAN_ID);
	      } 
	      loanScenarioBean = (LoanScenarioBean)JSONUtil.getObjctFromJSON(loanScenarioBean, engineResponseJson.toString());
	      application.setAppOfferJsonData(engineResponseJson.toString());
      }
      loanScenarioBean.setApplicationPL(application);
    } catch (JSONException e) {
        logger.info("PersonalProcessManagerImpl.java LNo: 1702 :: Calling Rule Engine :: ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
     } catch (Exception e) {
      logger.info("PersonalProcessManagerImpl.java LNo: 1702 :: Calling Rule Engine :: ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
    } 
    return loanScenarioBean;
  }
  
  public LoanScenarioBean callBRE(ApplicationFormPersonalLoan application, ApplicationFormPersonalLoanQuote quote, BankLmsUser bankLmsUser, Integer previousQuoteId, Integer trackVisitId, String ajaxPostUrl, boolean isLoggin) throws SQLException {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    loanScenarioBean = callBREAgain(application, quote, ajaxPostUrl, (bankLmsUser == null) ? Constants.OTHER_ID_INTEGER : bankLmsUser.getLmsUserId(), isLoggin);
    if (quote.getLoanQuoteLoanPurposeId().intValue() != 27 && loanScenarioBean.getStatus().intValue() == 0) {
    	return loanScenarioBean;
    }
 
    if (!isLoggin) {
    	return loanScenarioBean;
    }
       
    if (quote.getLoanQuoteLoanPurposeId().intValue() != 27 && loanScenarioBean.getStatus().intValue() != 1 && isLoggin) {
      String noOfferReason = loanScenarioBean.getNoOfferReason();
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(false);
      if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } else {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } 
      statusRequest.setState(1);
      statusRequest.setBankLMSUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0);
      statusRequest.setRsmDecision(0);
      statusRequest.setAppMobileVerified(application.getAppMobileVerified());
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
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
          application.setAppPersonalLoanId(null);
          application = personalLoanService.save(application);
        } 
      } else if (application.getAppLoanStatusId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (statusManagerResponse.isPreserveData() && previousQuoteId.intValue() > 0) {
        application.setAppQuoteId(previousQuoteId);
        application = personalLoanService.save(application);
      } 
      if (statusManagerResponse.isEligibleToInsertLog())
        insertCallLog(application.getAppSeqId(), (bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0, statusManagerResponse.getStatusCallLogs(), noOfferReason, null, true); 
      if (SessionUtil.getApplicationType() != null)
        if (SessionUtil.getApplicationType().intValue() != 0)
          if (SessionUtil.getApplicationType().intValue() == 1) {
            if (SessionUtil.getLeadId() != null) {
              ApplicationFormLead lead = commonService.getLeadById(SessionUtil.getLeadId());
              if (statusManagerResponse.getStatusLead() != 0) {
                lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
                lead.setLeadAppSeqId(application.getAppSeqId());
                lead = commonService.save(lead);
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
              lead = commonService.getLeadById(SessionUtil.getLeadId());
            } else {
              lead = new ApplicationFormLead();
            } 
            lead.setLeadFirstName((application.getAppFirstName() != null) ? application.getAppFirstName() : null);
            lead.setLeadApplyingFrom(application.getAppApplyingFrom());
            lead.setLeadIsdCode((application.getAppISDCode() != null) ? application.getAppISDCode() : Constants.COUNTRY_CODE_INDIA);
            lead.setLeadMobileNo((application.getAppMobileNo() != null) ? application.getAppMobileNo() : null);
            lead.setLeadCityId((quote.getLoanQuoteResidentCityId() != null) ? quote.getLoanQuoteResidentCityId() : Constants.OTHER_ID_INTEGER);
            lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
            if (lead.getLeadProductTypeId() == Constants.PERSONAL_LOAN_ID) {
              lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
            } else {
              lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
            } 
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
              List<BankLmsUserRole> lmsUserRole = commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
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
                Integer lmsUserLocationId = commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
                if (lmsUserLocationId != null) {
                  lead.setLeadAppContactCenterLocation(lmsUserLocationId.intValue());
                } else {
                  lead.setLeadAppContactCenterLocation(1);
                } 
              }  
            lead = commonService.save(lead);
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
      loanScenarioBean.setApplicationPL(application);
      return loanScenarioBean;
    } 
    if (SessionUtil.getApplicationType() != null && 
      isLoggin) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } else {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } 
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
            ApplicationFormLead lead = commonService.getLeadById(SessionUtil.getLeadId());
            if (statusManagerResponse.getStatusLead() != 0)
              lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead())); 
            lead.setLeadAppSeqId(application.getAppSeqId());
            lead = commonService.save(lead);
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
            lead = commonService.getLeadById(SessionUtil.getLeadId());
          } else {
            lead = new ApplicationFormLead();
          } 
          lead.setLeadFirstName((application.getAppFirstName() != null) ? application.getAppFirstName() : null);
          lead.setLeadApplyingFrom(application.getAppApplyingFrom());
          lead.setLeadIsdCode((application.getAppISDCode() != null) ? application.getAppISDCode() : Constants.COUNTRY_CODE_INDIA);
          lead.setLeadMobileNo((application.getAppMobileNo() != null) ? application.getAppMobileNo() : null);
          lead.setLeadCityId((quote.getLoanQuoteResidentCityId() != null) ? quote.getLoanQuoteResidentCityId() : Constants.OTHER_ID_INTEGER);
          lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
          if (lead.getLeadProductTypeId() == Constants.PERSONAL_LOAN_ID) {
            lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
          } else {
            lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
          } 
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
          lead = commonService.save(lead);
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
    if(quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
    loanScenarioBean.setMinEligibility(Double.valueOf(loanScenarioBean.getMinEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    loanScenarioBean.setChosenEligibility(Double.valueOf(loanScenarioBean.getChosenEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    loanScenarioBean.setMaxEligibility(Double.valueOf(loanScenarioBean.getMaxEligibility().doubleValue() / Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR.doubleValue()));
    if (quote.getLoanQuoteLoanPurposeId() != null) {
      MasterLoanPurpose loanPurpose = commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
      loanScenarioBean.setLoanPurpose(loanPurpose.getLpTypeValueSmall());
    } 
    loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(1));
    loanScenarioBean.setShowOffer(Integer.valueOf(0));
    CommonQuote termPreserve = null;
    CommonQuote checkOffPreserve = null;
    CommonQuote partialCheckOffPreserve = null;
    CommonQuote noCheckOffPreserve = null;
    List<CommonQuote> allQuotes = new ArrayList<>();
    List<String> accountTypeName = new ArrayList<>();
    Map<Integer, AccountTypeTermOverDraft> loanQuotes = loanScenarioBean.getLoanQuotes();
    for (Map.Entry<Integer, AccountTypeTermOverDraft> entry : loanQuotes.entrySet()) {
      AccountTypeTermOverDraft typeTermOverDraft = entry.getValue();
      CommonQuote term = typeTermOverDraft.getAccountTypeTerm();
      if (term != null) {
        if (termPreserve == null)
          termPreserve = term; 
        MasterPlProduct product = commonService.getPersonalLoanProductById(term.getProductTypeId());
        if (product != null) {
        	
        	if (product.getPlProductId() != null && product.getPlProductActive() != null && product.getPlProductDeleted() != null && 
        			(product.getPlProductActive().equals("N") || product.getPlProductDeleted().equals("Y"))) {
        		loanScenarioBean.setStatus(Integer.valueOf(0));
        	    loanScenarioBean.setMessage("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details");
        	    return loanScenarioBean;
        	}
        	
          if (product.getPlProductId() != null && product.getPlProductId().intValue() == 9) {
            term.setProductTypeName(product.getPlProductName());
            loanScenarioBean.setFirstProductName(product.getPlProductName());
            loanScenarioBean.setFirstProductUrl(product.getProductUrl());
            loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
            loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
            loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
            loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
            loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
            if (loanScenarioBean.getChosenProductId() == null)
              loanScenarioBean.setChosenProductId(product.getPlProductId()); 
          } else if (product.getPlProductId() != null && product.getPlProductId().intValue() == 14) {
            term.setProductTypeName(product.getPlProductName());
            loanScenarioBean.setSecondProductName(product.getPlProductName());
            loanScenarioBean.setSecondProductUrl(product.getProductUrl());
            loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
            loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
            loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
            loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
            loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
            if (loanScenarioBean.getChosenProductId() == null)
              loanScenarioBean.setChosenProductId(product.getPlProductId()); 
          } else {
            term.setProductTypeName(product.getPlProductName());
            loanScenarioBean.setSecondProductName(product.getPlProductName());
            loanScenarioBean.setSecondProductUrl(product.getProductUrl());
            loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
            loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
            loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
            loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
            loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
            if (loanScenarioBean.getChosenProductId() == null)
              loanScenarioBean.setChosenProductId(product.getPlProductId()); 
          } 
        } else {
          term.setProductTypeName("N/A");
          loanScenarioBean.setSecondProductName("N/A");
        } 
        int moratoriumMonths = 0;
        if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
          term.setAccountTypeId(quote.getLoanQuoteLoanAccountType());
        } else {
          term.setAccountTypeId(Integer.valueOf(1));
        } 
        if (isLoggin)
          term = commonEngine.getCalculatedQuote(term, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), false); 
        term.setIsDiscountApplied(Integer.valueOf(0));
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT || (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && quote.getLoanQuoteCheckOffType() != null)) {
          term.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteTermODType() != null) ? quote.getLoanQuoteTermODType().intValue() : 1));
        } else {
          term.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        } 
        term.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        accountTypeName.add(term.getDisplayName());
        allQuotes.add(term);
        typeTermOverDraft.setAccountTypeTerm(term);
        if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == term.getProductTypeId().intValue())
          quote.setLoanQuoteTermODType(Integer.valueOf(2)); 
      } 
      CommonQuote checkOff = typeTermOverDraft.getCheckOff();
      if (checkOff != null) {
        if (checkOffPreserve == null)
          checkOffPreserve = checkOff; 
        MasterPlProduct product = commonService.getPersonalLoanProductById(checkOff.getProductTypeId());
        if (product != null) {
        	
        	if (product.getPlProductId() != null && product.getPlProductActive() != null && product.getPlProductDeleted() != null && 
        			(product.getPlProductActive().equals("N") || product.getPlProductDeleted().equals("Y"))) {
        		loanScenarioBean.setStatus(Integer.valueOf(0));
        	    loanScenarioBean.setMessage("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details");
        	    return loanScenarioBean;
        	}
        	
          checkOff.setProductTypeName(product.getPlProductName());
          loanScenarioBean.setFirstProductName(product.getPlProductName());
          loanScenarioBean.setFirstProductUrl(product.getProductUrl());
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
          loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
          loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
          if (loanScenarioBean.getChosenProductId() == null)
            loanScenarioBean.setChosenProductId(product.getPlProductId()); 
        } else {
          checkOff.setProductTypeName("N/A");
          loanScenarioBean.setFirstProductName("N/A");
        } 
        int moratoriumMonths = 0;
        checkOff.setAccountTypeId(Integer.valueOf(1));
        if (isLoggin)
          checkOff = this.commonEngine.getCalculatedQuote(checkOff, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), false); 
        checkOff.setIsDiscountApplied(Integer.valueOf(0));
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT || (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && quote.getLoanQuoteCheckOffType() != null)) {
          checkOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteTermODType() != null) ? quote.getLoanQuoteTermODType().intValue() : 1));
        } else {
          checkOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        } 
        checkOff.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        accountTypeName.add(checkOff.getDisplayName());
        allQuotes.add(checkOff);
        typeTermOverDraft.setAccountTypeOverDraft(checkOff);
        if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == checkOff.getProductTypeId().intValue())
          quote.setLoanQuoteTermODType(Integer.valueOf(2)); 
      } 
      CommonQuote partialCheckOff = typeTermOverDraft.getPartialCheckOff();
      if (partialCheckOff != null) {
        if (partialCheckOffPreserve == null)
          partialCheckOffPreserve = partialCheckOff; 
        MasterPlProduct product = this.commonService.getPersonalLoanProductById(partialCheckOff.getProductTypeId());
        if (product != null) {
        	
        	if (product.getPlProductId() != null && product.getPlProductActive() != null && product.getPlProductDeleted() != null && 
        			(product.getPlProductActive().equals("N") || product.getPlProductDeleted().equals("Y"))) {
        		loanScenarioBean.setStatus(Integer.valueOf(0));
        	    loanScenarioBean.setMessage("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details");
        	    return loanScenarioBean;
        	}
        	
          partialCheckOff.setProductTypeName(product.getPlProductName());
          loanScenarioBean.setFirstProductName(product.getPlProductName());
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
          loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
          loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
          if (loanScenarioBean.getChosenProductId() == null)
            loanScenarioBean.setChosenProductId(product.getPlProductId()); 
        } else {
          partialCheckOff.setProductTypeName("N/A");
          loanScenarioBean.setFirstProductName("N/A");
        } 
        int moratoriumMonths = 0;
        partialCheckOff.setAccountTypeId(Integer.valueOf(2));
        if (isLoggin)
          partialCheckOff = this.commonEngine.getCalculatedQuote(partialCheckOff, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), false); 
        partialCheckOff.setIsDiscountApplied(Integer.valueOf(0));
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT || (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && quote.getLoanQuoteCheckOffType() != null)) {
          partialCheckOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteTermODType() != null) ? quote.getLoanQuoteTermODType().intValue() : 1));
        } else {
          partialCheckOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        } 
        partialCheckOff.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        allQuotes.add(partialCheckOff);
        accountTypeName.add(partialCheckOff.getDisplayName());
        typeTermOverDraft.setAccountTypeOverDraft(partialCheckOff);
        if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == partialCheckOff.getProductTypeId().intValue())
          quote.setLoanQuoteTermODType(Integer.valueOf(2)); 
      } 
      CommonQuote noCheckOff = typeTermOverDraft.getNoCheckOff();
      if (noCheckOff != null) {
        if (noCheckOffPreserve == null)
          noCheckOffPreserve = noCheckOff; 
        MasterPlProduct product = this.commonService.getPersonalLoanProductById(noCheckOff.getProductTypeId());
        if (product != null) {
        	
        	if (product.getPlProductId() != null && product.getPlProductActive() != null && product.getPlProductDeleted() != null && 
        			(product.getPlProductActive().equals("N") || product.getPlProductDeleted().equals("Y"))) {
        		loanScenarioBean.setStatus(Integer.valueOf(0));
        	    loanScenarioBean.setMessage("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details");
        	    return loanScenarioBean;
        	}
        	
          noCheckOff.setProductTypeName(product.getPlProductName());
          loanScenarioBean.setFirstProductName(product.getPlProductName());
          loanScenarioBean.setProductSliderDigit(Double.valueOf(product.getPLProductSliderAmtMul().doubleValue() / 100000.0D));
          loanScenarioBean.setProductSliderTenure(product.getPLProductSliderTenure());
          loanScenarioBean.setProductSliderAmtMul(product.getPLProductSliderAmtMul());
          loanScenarioBean.setProductSliderDigitExact(product.getPLProductSliderDigit());
          loanScenarioBean.setProductSliderTenureChn(product.getPLProductSliderTenureChn());
          if (loanScenarioBean.getChosenProductId() == null)
            loanScenarioBean.setChosenProductId(product.getPlProductId()); 
        } else {
          noCheckOff.setProductTypeName("N/A");
          loanScenarioBean.setFirstProductName("N/A");
        } 
        int moratoriumMonths = 0;
        noCheckOff.setAccountTypeId(Integer.valueOf(3));
        if (isLoggin)
          noCheckOff = this.commonEngine.getCalculatedQuote(noCheckOff, loanScenarioBean.getChosenTenure(), ajaxPostUrl, moratoriumMonths, loanScenarioBean.getProductSliderTenure().intValue(), false); 
        noCheckOff.setIsDiscountApplied(Integer.valueOf(0));
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT || (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && quote.getLoanQuoteCheckOffType() != null)) {
          noCheckOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteTermODType() != null) ? quote.getLoanQuoteTermODType().intValue() : 1));
        } else {
          noCheckOff.setChosenAccountTypeId(Integer.valueOf((quote.getLoanQuoteLoanAccountType() != null) ? quote.getLoanQuoteLoanAccountType().intValue() : 1));
        } 
        noCheckOff.setChosenProductTypeId(Integer.valueOf((quote.getLoanQuoteLoanProductId() != null) ? quote.getLoanQuoteLoanProductId().intValue() : 1));
        allQuotes.add(noCheckOff);
        accountTypeName.add(noCheckOff.getDisplayName());
        typeTermOverDraft.setAccountTypeOverDraft(noCheckOff);
        if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == noCheckOff.getProductTypeId().intValue())
          quote.setLoanQuoteTermODType(Integer.valueOf(2)); 
      } 
      loanQuotes.put(entry.getKey(), typeTermOverDraft);
      if (isLoggin && (
        term == null || checkOff == null || partialCheckOff == null || noCheckOff == null))
        loanScenarioBean.setShowTermOverDraftCheck(Integer.valueOf(0)); 
    } 
    loanScenarioBean.setAllQuotes(allQuotes);
    loanScenarioBean.setLoanQuotes(loanQuotes);
    loanScenarioBean.setAccountTypeName(accountTypeName);
    if (isLoggin) {
      if (application.getAppWorkEmail() != null && 
        SessionUtil.getEmail() == null)
        SessionUtil.setEmail(application.getAppWorkEmail()); 
      if (application.getAppMobileNo() != null && 
        SessionUtil.getMobile() == null)
        SessionUtil.setMobile(application.getAppMobileNo()); 
      if (application.getAppFirstName() != null && 
        SessionUtil.getApplicantName() == null)
        SessionUtil.setApplicantName(application.getAppFirstName()); 
    } 
    application.setAppLoanMaxAmount(loanScenarioBean.getMaxEligibility());
    application.setAppLoanAmount(loanScenarioBean.getChosenEligibility());
    application.setAppLoanTenure(loanScenarioBean.getChosenTenure());
    application.setAppCountryId(quote.getLoanQuoteResidentCountryId());
	loanMaxAmount = application.getAppLoanMaxAmount();
    if (checkOffPreserve != null) {
      application.setAppLoanEmi(checkOffPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(checkOffPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(checkOffPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(checkOffPreserve.getProcessingFee().doubleValue())).doubleValue()));
      application.setAppPersonalLoanId(checkOffPreserve.getProductTypeId());
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT && noCheckOffPreserve != null) {
        application.setAppLoanAccountType(noCheckOffPreserve.getChosenAccountTypeId());
      } else if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == checkOffPreserve.getProductTypeId().intValue()) {
        application.setAppLoanAccountType(Integer.valueOf(2));
        quote.setLoanQuoteTermODType(Integer.valueOf(2));
      } else {
        application.setAppLoanAccountType(Integer.valueOf(1));
      } 
      application.setAppEmiNmiRatio(Float.valueOf(checkOffPreserve.getEmiNmiRatio()));
      if (checkOffPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(checkOffPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (checkOffPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(checkOffPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (checkOffPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(checkOffPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
    } else if (noCheckOffPreserve != null) {
      application.setAppLoanEmi(noCheckOffPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(noCheckOffPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(noCheckOffPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(noCheckOffPreserve.getProcessingFee().doubleValue())).doubleValue()));
      application.setAppPersonalLoanId(noCheckOffPreserve.getProductTypeId());
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT) {
        application.setAppLoanAccountType(noCheckOffPreserve.getChosenAccountTypeId());
      } else if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == noCheckOffPreserve.getProductTypeId().intValue()) {
        application.setAppLoanAccountType(Integer.valueOf(2));
        quote.setLoanQuoteTermODType(Integer.valueOf(2));
      } else {
        application.setAppLoanAccountType(Integer.valueOf(3));
      } 
      application.setAppEmiNmiRatio(Float.valueOf(noCheckOffPreserve.getEmiNmiRatio()));
      if (noCheckOffPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(noCheckOffPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (noCheckOffPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(noCheckOffPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (noCheckOffPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(noCheckOffPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
    } else if (partialCheckOffPreserve != null) {
      application.setAppLoanEmi(partialCheckOffPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(partialCheckOffPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(partialCheckOffPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(partialCheckOffPreserve.getProcessingFee().doubleValue())).doubleValue()));
      application.setAppPersonalLoanId(partialCheckOffPreserve.getProductTypeId());
      if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBT) {
        application.setAppLoanAccountType(partialCheckOffPreserve.getChosenAccountTypeId());
      } else {
        application.setAppLoanAccountType(Integer.valueOf(2));
      } 
      application.setAppEmiNmiRatio(Float.valueOf(partialCheckOffPreserve.getEmiNmiRatio()));
      if (partialCheckOffPreserve.getDiscountedEmi() != null) {
        application.setAppLoanEmiDiscount(partialCheckOffPreserve.getDiscountedEmi());
      } else {
        application.setAppLoanEmiDiscount(null);
      } 
      if (partialCheckOffPreserve.getDiscountedInterestRate() != null) {
        application.setAppLoanInterestRateDiscount(Float.valueOf(partialCheckOffPreserve.getDiscountedInterestRate().floatValue()));
      } else {
        application.setAppLoanInterestRateDiscount(null);
      } 
      if (partialCheckOffPreserve.getDiscountedProcessingFee() != null) {
        application.setAppLoanProcessingFeeDiscount(Double.valueOf((new Double(partialCheckOffPreserve.getDiscountedProcessingFee().doubleValue())).doubleValue()));
      } else {
        application.setAppLoanProcessingFeeDiscount(null);
      } 
    } else if (checkOffPreserve == null && termPreserve != null) {
      application.setAppLoanEmi(termPreserve.getEmi());
      application.setAppLoanInterestRate(Float.valueOf(termPreserve.getInterestRate().floatValue()));
      application.setAppLoanInterestRateType(termPreserve.getRateType());
      application.setAppLoanProcessingFee(Double.valueOf((new Double(termPreserve.getProcessingFee().doubleValue())).doubleValue()));
      application.setAppPersonalLoanId(termPreserve.getProductTypeId());
      application.setAppLoanAccountType(Integer.valueOf(2));
      application.setAppEmiNmiRatio(Float.valueOf(termPreserve.getEmiNmiRatio()));
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
      if (Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue() == termPreserve.getProductTypeId().intValue())
        quote.setLoanQuoteTermODType(Integer.valueOf(2)); 
    } 
    if (loanScenarioBean.getProductSliderTenure().intValue() == 1) {
      loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure());
      loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure());
      loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure());
    } else {
      loanScenarioBean.setMaxTenure(Integer.valueOf(loanScenarioBean.getMaxTenure().intValue() / 12));
      loanScenarioBean.setChosenTenure(Integer.valueOf(loanScenarioBean.getChosenTenure().intValue() / 12));
      loanScenarioBean.setMinTenure(Integer.valueOf(loanScenarioBean.getMinTenure().intValue() / 12));
    } 
    loanScenarioBean.setChosenLoanAccountType(application.getAppLoanAccountType());
    }
    quote = personalLoanService.save(quote);
    application = personalLoanService.save(application);
    loanScenarioBean.setApplicationPL(application);
    return loanScenarioBean;
  }
  
  public ApplicationFormPersonalLoanQuote getUpdateQuote(ApplicationFormPersonalLoanQuote quote) throws NullPointerException, RuntimeException, ParseException  {
    double netAnnualIncome = 0.0D;
    double netAnnualCoapplicantIncome = 0.0D;
    String date = "";
    if (quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
      if (quote.getLoanQuoteMonthStartDateOflease() == null)
        quote.setLoanQuoteMonthStartDateOflease(Integer.valueOf(1)); 
      if (quote.getLoanQuoteMonthEndDateOfLease() == null)
        quote.setLoanQuoteMonthEndDateOfLease(Integer.valueOf(1)); 
      date = "01-" + quote.getLoanQuoteMonthStartDateOflease() + "-" + quote.getLoanQuoteYearStartDateOflease();
      quote.setLoanQuoteSTDOfLease(DateUtil.convertStringToDate(date));
      quote.setLoanQuoteStartDateOfLease(quote.getLoanQuoteYearStartDateOflease() + "-" + ((quote.getLoanQuoteMonthStartDateOflease().intValue() > 9) ? ""+quote.getLoanQuoteMonthStartDateOflease() : ("0" + quote.getLoanQuoteMonthStartDateOflease())) + "-01");
      date = "01-" + quote.getLoanQuoteMonthEndDateOfLease() + "-" + quote.getLoanQuoteYearEndDateOfLease();
      quote.setLoanQuoteEDOfLease(DateUtil.convertStringToDate(date));
      quote.setLoanQuoteEndDateOfLease(quote.getLoanQuoteYearEndDateOfLease() + "-" + ((quote.getLoanQuoteMonthEndDateOfLease().intValue() > 9) ? ""+quote.getLoanQuoteMonthEndDateOfLease() : ("0" + quote.getLoanQuoteMonthEndDateOfLease())) + "-01");
      quote.setLoanQuoteResidentTypeId(Integer.valueOf(1));
    } 
    if (quote.getLoanQuoteLoanPurposeId().intValue() == 12 && 
      !quote.getLoanQuoteHasPropertyRented().equalsIgnoreCase("N")) {
      if (quote.getLoanQuoteMonthStartDateOflease() == null)
        quote.setLoanQuoteMonthStartDateOflease(Integer.valueOf(1)); 
      if (quote.getLoanQuoteMonthEndDateOfLease() == null)
        quote.setLoanQuoteMonthEndDateOfLease(Integer.valueOf(1)); 
      date = "01-" + quote.getLoanQuoteMonthStartDateOflease() + "-" + quote.getLoanQuoteYearStartDateOflease();
      quote.setLoanQuoteSTDOfLease(DateUtil.convertStringToDate(date));
      quote.setLoanQuoteStartDateOfLease(quote.getLoanQuoteYearStartDateOflease() + "-" + ((quote.getLoanQuoteMonthStartDateOflease().intValue() > 9) ? ""+quote.getLoanQuoteMonthStartDateOflease() : ("0" + quote.getLoanQuoteMonthStartDateOflease())) + "-01");
      date = "01-" + quote.getLoanQuoteMonthEndDateOfLease() + "-" + quote.getLoanQuoteYearEndDateOfLease();
      quote.setLoanQuoteEDOfLease(DateUtil.convertStringToDate(date));
      quote.setLoanQuoteEndDateOfLease(quote.getLoanQuoteYearEndDateOfLease() + "-" + ((quote.getLoanQuoteMonthEndDateOfLease().intValue() > 9) ? ""+quote.getLoanQuoteMonthEndDateOfLease() : ("0" + quote.getLoanQuoteMonthEndDateOfLease())) + "-01");
    } 
    if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 || quote.getLoanQuoteLoanPurposeId().intValue() == 12)
      if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 15) {
        if (quote.getLoanQuoteCoapplicantNetMonthlyIncome() != null)
          netAnnualCoapplicantIncome = quote.getLoanQuoteCoapplicantNetMonthlyIncome().doubleValue() * 12.0D; 
        if (quote.getLoanQuoteYearCoapplicantJoining() != null && quote.getLoanQuoteMonthCoapplicantJoining() != null) {
          date = "01-" + ((quote.getLoanQuoteMonthCoapplicantJoining() != null) ? ""+quote.getLoanQuoteMonthCoapplicantJoining() : "01") + "-" + quote.getLoanQuoteYearCoapplicantJoining();
          quote.setLoanQuoteCoapplicantJoiningDate(DateUtil.convertStringToDate(date));
        } 
      } else if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 16) {
        netAnnualCoapplicantIncome = quote.getLoanQuoteCoapplicantProfitAfterTax().doubleValue();
        if (quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession() != null && quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() != null) {
          date = "01-" + ((quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() != null) ? ""+quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() : "01") + "-" + quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession();
          quote.setLoanQuoteCoapplicantStartDateOfCurrentProfession(DateUtil.convertStringToDate(date));
        } 
      } else if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 17) {
        netAnnualCoapplicantIncome = quote.getLoanQuoteCoapplicantProfitAfterTax().doubleValue();
        if (quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() != null && quote.getLoanQuoteYearCoapplicantStartDateOfCurrentBusiness() != null) {
          date = "01-" + ((quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() != null) ? ""+quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() : "01") + "-" + quote.getLoanQuoteYearCoapplicantStartDateOfCurrentBusiness();
          quote.setLoanQuoteCoapplicantStartDateOfCurrentBusiness(DateUtil.convertStringToDate(date));
        } 
      } else if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 18) {
        netAnnualCoapplicantIncome = quote.getLoanQuoteCoapplicantNetAnnualIncome().doubleValue();
      } else if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 19 && 
        quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
        netAnnualCoapplicantIncome = quote.getLoanQuoteCoapplicantNetMonthlyIncome().doubleValue() * 12.0D;
      }  
    if (quote.getLoanQuoteEmploymentType() != null && (quote.getLoanQuoteEmploymentType().intValue() == 15 || quote.getLoanQuoteEmploymentType().intValue() == 20)) {
      if (quote.getLoanQuoteMonthCompanyJoining() == null)
        quote.setLoanQuoteMonthCompanyJoining(Integer.valueOf(1)); 
      if (quote.getLoanQuoteMonthCompanyJoining() != null && quote.getLoanQuoteYearCompanyJoining() != null) {
        date = "01-" + quote.getLoanQuoteMonthCompanyJoining() + "-" + quote.getLoanQuoteYearCompanyJoining();
        quote.setLoanQuoteCompanyJoiningDate(DateUtil.convertStringToDate(date));
      } 
      netAnnualIncome = (quote.getLoanQuoteNetMonthlySalary() != null) ? (12.0D * quote.getLoanQuoteNetMonthlySalary().doubleValue()) : (0.0D + netAnnualCoapplicantIncome);
    } else if (quote.getLoanQuoteEmploymentType() != null && quote.getLoanQuoteEmploymentType().intValue() == 16) {
      if (quote.getLoanQuoteYearStartDateOfCurrentProfession() != null) {
        if (quote.getLoanQuoteMonthStartDateOfCurrentProfession() == null)
          quote.setLoanQuoteMonthStartDateOfCurrentProfession(Integer.valueOf(1)); 
        date = "01-" + quote.getLoanQuoteMonthStartDateOfCurrentProfession() + "-" + quote.getLoanQuoteYearStartDateOfCurrentProfession();
        quote.setLoanQuoteStartDateOfCurrentProfession(DateUtil.convertStringToDate(date));
      } 
      netAnnualIncome = (quote.getLoanQuoteProfitAfterTax() != null) ? quote.getLoanQuoteProfitAfterTax().doubleValue() : (0.0D + netAnnualCoapplicantIncome);
    } else if (quote.getLoanQuoteEmploymentType() != null && quote.getLoanQuoteEmploymentType().intValue() == 17) {
      if (quote.getLoanQuoteYearStartDateOfCurrentBusiness() != null) {
        if (quote.getLoanQuoteMonthStartDateOfCurrentBusiness() == null)
          quote.setLoanQuoteMonthStartDateOfCurrentBusiness(Integer.valueOf(1)); 
        date = "01-" + quote.getLoanQuoteMonthStartDateOfCurrentBusiness() + "-" + quote.getLoanQuoteYearStartDateOfCurrentBusiness();
        quote.setLoanQuoteStartDateOfCurrentBusiness(DateUtil.convertStringToDate(date));
      } 
      netAnnualIncome = (quote.getLoanQuoteProfitAfterTax() != null) ? quote.getLoanQuoteProfitAfterTax().doubleValue() : (0.0D + netAnnualCoapplicantIncome);
    } else if (quote.getLoanQuoteEmploymentType() != null && quote.getLoanQuoteEmploymentType().intValue() == 18) {
      if (quote.getLoanQuoteYearStartDateOfCurrentBusiness() != null) {
        if (quote.getLoanQuoteMonthStartDateOfCurrentBusiness() == null)
          quote.setLoanQuoteMonthStartDateOfCurrentBusiness(Integer.valueOf(1)); 
        date = "01-" + quote.getLoanQuoteMonthStartDateOfCurrentBusiness() + "-" + quote.getLoanQuoteYearStartDateOfCurrentBusiness();
        quote.setLoanQuoteStartDateOfCurrentBusiness(DateUtil.convertStringToDate(date));
      } 
      netAnnualIncome = (quote.getLoanQuoteAnnualIncome() != null) ? quote.getLoanQuoteAnnualIncome().doubleValue() : (0.0D + netAnnualCoapplicantIncome);
    } else {
      netAnnualIncome = (quote.getLoanQuoteExistingOrExpectedMothlyRent() != null) ? (quote.getLoanQuoteExistingOrExpectedMothlyRent().doubleValue() * 12.0D) : 0.0D;
    } 
    if (quote.getLoanQuoteLoanPurposeId().intValue() == 11)
      netAnnualIncome = (quote.getLoanQuoteNetMonthlySalary() != null) ? (quote.getLoanQuoteNetMonthlySalary().doubleValue() * 12.0D) : 0.0D; 
    quote.setLoanQuoteNetIncome(Double.valueOf(netAnnualIncome + netAnnualCoapplicantIncome));
    if (quote.getLoanQuoteCoapplicantEmploymentTypeId() != null) {
      if (quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 15 && 
        quote.getLoanQuoteYearCoapplicantJoining() != null) {
        if (quote.getLoanQuoteMonthCoapplicantJoining() == null)
          quote.setLoanQuoteMonthCoapplicantJoining(Integer.valueOf(1)); 
        date = "01-" + ((quote.getLoanQuoteMonthCoapplicantJoining() != null) ? ""+quote.getLoanQuoteMonthCoapplicantJoining() : "01") + "-" + quote.getLoanQuoteYearCoapplicantJoining();
        quote.setLoanQuoteCoapplicantJoiningDate(DateUtil.convertStringToDate(date));
      } 
      if (quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 16 && 
        quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession() != null) {
        if (quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() == null)
          quote.setLoanQuoteMonthCoapplicantStartDateOfCurrentProfession(Integer.valueOf(1)); 
        date = "01-" + quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() + "-" + quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession();
        quote.setLoanQuoteCoapplicantStartDateOfCurrentProfession(DateUtil.convertStringToDate(date));
      } 
      if (quote.getLoanQuoteCoapplicantEmploymentTypeId().intValue() == 17 && 
        quote.getLoanQuoteYearCoapplicantStartDateOfCurrentBusiness() != null) {
        if (quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() == null)
          quote.setLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness(Integer.valueOf(1)); 
        date = "01-" + quote.getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() + "-" + quote.getLoanQuoteYearCoapplicantStartDateOfCurrentBusiness();
        quote.setLoanQuoteCoapplicantStartDateOfCurrentBusiness(DateUtil.convertStringToDate(date));
      } 
    } 
    if (quote.getLoanQuoteYearCompanyJoining() != null && quote.getLoanQuoteMonthCompanyJoining() != null) {
      date = "01-" + quote.getLoanQuoteMonthCompanyJoining() + "-" + quote.getLoanQuoteYearCompanyJoining();
      quote.setLoanQuoteCompanyJoiningDate(DateUtil.convertStringToDate(date));
    } 
    return quote;
  }

	public double getMaxAmount() {
		double amount = 0;
		amount = loanMaxAmount;
		logger.info("amount ::" + amount);
		return amount;
	}
}
