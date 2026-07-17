package com.mintstreet.loan.homeloan.util;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.NoResultException;

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
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterSalesTeam;
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
import com.mintstreet.loan.homeloan.bo.impl.HomeProcessManagerImpl;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanCalls;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.product.entity.HlProduct;

public class HomeLoanHelper  {
	private static final  Logger logger=LogManager.getLogger(HomeLoanHelper.class.getName());

	@Autowired
	private HomeLoanService homeLoanService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private SbiUtil SbiUtil;
    
	@Autowired
	private CommonEngine commonEngine;
	@Autowired
	private HomeProcessManagerImpl homeProcessManagerImpl;
	
	public ApplicationFormHomeLoanQuote insertLoanQuote(ApplicationFormHomeLoanQuote quote, Integer bankLmsUserId, Integer trackVisitId) throws NullPointerException, RuntimeException, ParseException, SQLException {
			if(quote==null){
				quote = new ApplicationFormHomeLoanQuote();
				quote.setError("Please re-initiate application.|quote.loanQuotePreferredLocationOfAvailingLoan|1");
				return quote;
			}
			if(!ValidatorUtil.isValid(quote.getLoanQuotePreferredStateId())){
				quote.setError("Please select state.|quote.loanQuotePreferredLocationOfAvailingLoan|1");
				return quote;
			}
			if(!ValidatorUtil.isValid(quote.getLoanQuotePreferredCityId())){
				quote.setError("Please select state.|quote.loanQuotePreferredStateId|1");    
				return quote;
			}
			if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
				if(!ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())){
					quote.setError("Please select city.|quote.loanQuotePreferredCityId|1");
					return quote;
				}   
			}
			if(!ValidatorUtil.isValid(quote.getLoanQuotePrferredBranchId())){
				if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
					quote.setError(Constants.BRANCH_NOT_FOUND_MSG+"|quote.loanQuotePreferredDistrictId|1");
				}else{
					quote.setError(Constants.BRANCH_NOT_FOUND_MSG+"|quote.loanQuotePreferredCityId|1");
				}
				return quote;
			}
			  if (ValidatorUtil.isValidMobile(quote.getAppMobile()))
		            quote.setAppMobile(quote.getAppMobile());
			  
			  if (ValidatorUtil.isValidEmail(quote.getAppEmail()))
		            quote.setAppEmail(quote.getAppEmail()); 
			
			  if(quote.getAppEmail()!=null){
				  SessionUtil.setEmail(quote.getAppEmail());
			  }

			  if(quote.getAppMobile()!=null){
					SessionUtil.setMobile(quote.getAppMobile());
			  }

			quote.setLoanQuoteActive("Y");
			quote.setLoanQuoteDeleted("N");
			
			quote = getUpdateQuote(quote);
			
			if(quote.getLoanQuoteResidentTypeId().intValue()==2){
				if(quote.getLoanQuoteCountryId()==null){
					quote.setError("Please select resident type.|quote.loanQuoteResidentTypeId|1");
					return quote;
				}
			}
			if(quote.getLoanQuoteCoapplicantFirstResidentTypeId()!=null){
				if(quote.getLoanQuoteCoapplicantFirstResidentTypeId().intValue() == 2){
					if(quote.getLoanQuoteCoapplicantFirstCountryId()==null){
						quote.setError("Please select coapplicant resident type.|quote.loanQuoteCoapplicantFirstResidentTypeId|1");
						return quote;
					}
				}
			}
			if(quote.getLoanQuoteCoapplicantSecondResidentTypeId()!=null){
				if(quote.getLoanQuoteCoapplicantSecondResidentTypeId().intValue() == 2){
					if(quote.getLoanQuoteCoapplicantSecondCountryId()==null){
						quote.setError("Please select coapplicant resident type.|quote.loanQuoteCoapplicantSecondResidentTypeId|1");
						return quote;
					}
				}
			}
			if(ValidatorUtil.isValid(quote.getLoanQuoteFirstOwnerOfProperty())){
				quote.setLoanQuoteFirstOwnerOfProperty(quote.getLoanQuoteFirstOwnerOfProperty());
			}else{
				quote.setLoanQuoteFirstOwnerOfProperty("N");
			}
			quote.setLoanQuoteLeadId(SessionUtil.getLeadId());
			quote.setLoanQuoteCreatedLmsUserId((bankLmsUserId!=null?bankLmsUserId:Constants.OTHER_USER_ID));
			quote.setLoanQuoteEntryTime(new Date());
			quote.setLoanQuoteUpdatedTime(new Date());
			quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
			quote.setLoanQuoteVisitId(trackVisitId);
			if("F".equalsIgnoreCase(quote.getLoanQuoteGender()) || "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender()) || "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender()) ){
				quote.setLoanQuoteIsEligibleForHarGhar(1);
			}else{
				quote.setLoanQuoteIsEligibleForHarGhar(0);
			}
			if(quote.getLoanQuoteLoanAccountType()!=null){
				quote.setLoanQuoteLoanAccountType(quote.getLoanQuoteLoanAccountType());
			}
			if(quote.getLoanQuoteCheckOffType()!=null){
				quote.setLoanQuoteCheckOffType(quote.getLoanQuoteCheckOffType());
			}
			quote.setLoanQuoteReimburse(quote.getLoanQuoteReimburse());
			quote.setLoanQuoteAmountInvested(quote.getLoanQuoteAmountInvested());
			quote.setLoanQuotePropertyMarketValue(quote.getLoanQuotePropertyMarketValue());
			
			if(quote.getLoanQuoteMaritalStatus()!=null){
				quote.setLoanQuoteMaritalStatus(quote.getLoanQuoteMaritalStatus());
			}
			if(quote.getLoanQuotePuccaHouse()!=null){
				quote.setLoanQuotePuccaHouse(quote.getLoanQuotePuccaHouse());
			}
			if(quote.getLoanQuotePmayItr()!=null){
				quote.setLoanQuotePmayItr(quote.getLoanQuotePmayItr());
			}
			
			 if(quote.getAppMobile() !=null) {
				 quote.setAppMobile(quote.getAppMobile() ) ;
			 }

			 if(quote.getAppEmail() !=null) {
				 quote.setAppEmail(quote.getAppEmail() ) ;
			 }
			 
			 
			 if(quote.getAppEmail() !=null) {
				 quote.setAppEmail(quote.getAppEmail() ) ;
			 }
			 
			 
			 if(quote.getLoanQuoteLoanAmountTaken() !=null) {
				 quote.setLoanQuoteLoanAmountTaken(quote.getLoanQuoteLoanAmountTaken()) ;
			 }
			 
			 
			 logger.info("bankLmsUserId:: " + bankLmsUserId + "::: and quote consentId is:: " + quote.getLoanQuoteConsentId());
			 //get active consentId for direct web leads (other than dsr page)
			 if (Constants.OTHER_USER_ID.equals(bankLmsUserId)) {
//				 Consent consent = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID);
//				 Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
				 Integer consentId = commonService.getConsentIdByLoanType(Constants.HOME_LOAN_ID);
				 quote.setLoanQuoteConsentId(consentId); 
			 } else {
				 if (quote.getLoanQuoteConsentId() != null) {
					 quote.setLoanQuoteConsentId(quote.getLoanQuoteConsentId()); 
				 }
			 }
			 
			 //logger.info("ConsentId before save in quote table:::" + quote.getLoanQuoteConsentId());
			 quote = homeLoanService.save(quote);
			 return quote;
			 
	}
	
	public ApplicationFormHomeLoan insertAppLoan(ApplicationFormHomeLoanQuote quote, ApplicationFormHomeLoan application, Integer bankLmsUserId, Integer lmsUserIntermediaryId) throws ParseException, NoResultException, SQLException {
			boolean isNRI=true;
			boolean isMagicBricksCampaign=false;
			if(quote==null){
				if(application == null){
					application = new ApplicationFormHomeLoan();
				}
				application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
				return application;
			}
			if (application != null) {
				
			} else {
				application = new ApplicationFormHomeLoan();
				application.setAppOtpMobileAlertsCount(0);
				application.setAppBankId(Constants.LEAD_BANK_ID);
				
				application.setAppFulfillmentGroupId(Constants.LEAD_FULFILLMENT_GROUP_ID);
				if(!"Y".equalsIgnoreCase(application.getAppMobileVerified())){
					application.setAppMobileVerified("N");
					application.setAppEmailVerified("N");
				}
				if ("Y".equals(quote.getLoanQuoteExistingHomeLoanWithSbi())) {
					application.setAppSalaryBankId(Constants.LEAD_BANK_ID);
				}
			}
			if(ValidatorUtil.isValid(quote.getLoanQuotePreferredLocationOfAvailingLoan()) && quote.getLoanQuotePreferredLocationOfAvailingLoan() == 1){
				/*if(ValidatorUtil.isValid(quote.getLoanQuotePreferredStateId())){
					application.setAppStateId(quote.getLoanQuotePreferredStateId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredLocationOfAvailingLoan|1");
					return application;
				}
				if(ValidatorUtil.isValid(quote.getLoanQuotePreferredCityId())){
					application.setAppCityId(quote.getLoanQuotePreferredCityId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredStateId|1");
					return application;
				}
				if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
					if(ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())){
						application.setAppDistrictId(quote.getLoanQuotePreferredDistrictId());
					}else{
						application.setError("Please select city.|quote.loanQuotePreferredCityId|1");
						return application;
					}
				}*/
				
				
				if(ValidatorUtil.isValid(quote.getLoanQuoteStateId())){
					application.setAppStateId(quote.getLoanQuoteStateId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredLocationOfAvailingLoan|1");
					return application;
				}
				if(ValidatorUtil.isValid(quote.getLoanQuoteCityId())){
					application.setAppCityId(quote.getLoanQuoteCityId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredStateId|1");
					return application;
				}
				if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
					if(ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())){
						application.setAppDistrictId(quote.getLoanQuotePreferredDistrictId());
					}else{
						application.setError("Please select city.|quote.loanQuotePreferredCityId|1");
						return application;
					}
				}
				
			} else if(ValidatorUtil.isValid(quote.getLoanQuotePreferredLocationOfAvailingLoan()) && quote.getLoanQuotePreferredLocationOfAvailingLoan() == 2){
			
				if(ValidatorUtil.isValid(quote.getLoanQuoteStateId())){
					application.setAppStateId(quote.getLoanQuoteStateId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredLocationOfAvailingLoan|1");
					return application;
				}
				if(ValidatorUtil.isValid(quote.getLoanQuoteCityId())){
					application.setAppCityId(quote.getLoanQuoteCityId());
				}else{
					application.setError("Please select state.|quote.loanQuotePreferredStateId|1");
					return application;
				}
				if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
					if(ValidatorUtil.isValid(quote.getLoanQuotePreferredDistrictId())){
						application.setAppDistrictId(quote.getLoanQuotePreferredDistrictId());
					}else{
						application.setError("Please select city.|quote.loanQuotePreferredCityId|1");
						return application;
					}
				}
				
			}
			
				if(ValidatorUtil.isValid(quote.getLoanQuotePrferredBranchId()) ){
					application.setAppBranchId(quote.getLoanQuotePrferredBranchId());
				}else{
					if(quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
						application.setError(Constants.BRANCH_NOT_FOUND_MSG+"|quote.loanQuotePreferredDistrictId|1");
					}else{
						application.setError(Constants.BRANCH_NOT_FOUND_MSG+"|quote.loanQuotePreferredCityId|1");
					}
					return application;
				}
			
			if(application.getAppEntryTime()==null){
				application.setAppEntryTime(new Date());
			}
			if (application.getAppEntryDate()==null) {
				application.setAppEntryDate(new Date());
			}
			if(!ValidatorUtil.isValid(application.getAppLoanStatusId())){
				if(SessionUtil.getApplicationType()!=null){
					if(SessionUtil.getApplicationType()==0){
						application.setAppContactCenterLocation(1);
						if(SessionUtil.getBankLMSUser()==null){
							application.setAppLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE43_ID);
						}else{
							application.setAppLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE141_ID);
						}
					}else if(SessionUtil.getApplicationType()==1){
						application.setAppLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE141_ID);
					}else if(SessionUtil.getApplicationType()==2){
						application.setAppLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE141_ID);
					}
				}else{
					application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
					return application;
				}
			}
			if(!ValidatorUtil.isValid(application.getAppLoanStatusId())){
				application.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return application;
			}
			if(!ValidatorUtil.isValid(application.getAppDataSourceId())){
				if(SessionUtil.getApplicationType()!=null){
					if(SessionUtil.getApplicationType()==0){
						application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
					}else if(SessionUtil.getApplicationType()==1){
						if(SessionUtil.getLeadId()!=null){
							ApplicationFormLead lead=commonService.getLeadById(SessionUtil.getLeadId());
							if(lead!=null && lead.getLeadDataSourceId()!=null){
								application.setAppDataSourceId(lead.getLeadDataSourceId());
							}else{
								application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);	
							}
						}else{
							application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
						}
					}else if(SessionUtil.getApplicationType()==2){
						application.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
					}
				}else{
					application.setError(Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
					return application;
				}
			}
			if(!ValidatorUtil.isValid(application.getAppDataSourceId())){
				application.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return application;
			}
			if(application.getAppSubTypeId()==null){
				application.setAppSubTypeId(Constants.APP_APP_SUB_TYPE_ID_NORMAL);
			}
			if(application.getAppHotLeadCreatedLmsUserId()==null){
				if(SessionUtil.getApplicationType()!=null){
					if(SessionUtil.getApplicationType()==2){
						application.setAppHotLeadCreatedLmsUserId((bankLmsUserId!=null?bankLmsUserId:0));
					}
				}
			}
			application.setAppQuoteId(quote.getLoanQuoteId());
			if(quote.getLoanQuoteDateOfBirthDT()!=null){
				application.setAppDobDT(quote.getLoanQuoteDateOfBirthDT());
			}
			application.setAppGender(quote.getLoanQuoteGender());
			if(quote.getLoanQuotePreferredLocationOfAvailingLoan()!=null && quote.getLoanQuotePreferredLocationOfAvailingLoan()==1){
				if(quote.getLoanQuotePrferredBranchId()!=null){
					if(bankLmsUserId!=null){
						if(application.getAppPreviousBranchId()!=null && application.getAppBranchId()!=null){
							if(!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppBranchId().toString())){
								StringBuilder message = new StringBuilder("Application moved from ");
								MasterBranch masterBranch = commonService.getBranchById(application.getAppPreviousBranchId());
								if(masterBranch!=null){
									message.append(masterBranch.getBranchName()+ " ("+masterBranch.getBranchCode()+") " );
								}
								application.setAppPreviousBranchId(application.getAppBranchId());
								masterBranch = commonService.getBranchById(application.getAppBranchId());
								if(masterBranch!=null){
									message.append(" to "+masterBranch.getBranchName()+ " ("+masterBranch.getBranchCode()+") " );
								}
								if(application.getAppDataSourceId()!=null && !application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)){
									if(SessionUtil.getLeadId()==null){
										ApplicationFormLead lead = commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.HOME_LOAN_ID);
										commonService.insertCallLog(lead!=null?lead.getLeadId():null, (bankLmsUserId!=null?bankLmsUserId:Constants.OTHER_USER_ID) , application.getAppLoanStatusId(), message.toString());
									}
								}else{
									insertCallLog(application.getAppSeqId(), (bankLmsUserId!=null?bankLmsUserId:Constants.OTHER_USER_ID), application.getAppLoanStatusId(), message.toString(), null, true);
								}
								application.setAppAssignedLmsSalesUserId(null);
							}
						}else{
							application.setAppPreviousBranchId(application.getAppBranchId());
						}
					}else{
						application.setAppPreviousBranchId(application.getAppBranchId());
					}
				}
			}
			/*
			else {
				if(bankLmsUserId==null){
					application.setAppStateId(0);
					application.setAppCityId(0);

					application.setAppDistrictId(0);
				}
			}
			 */
			
			if(quote.getLoanQuotePreferredLocationOfAvailingLoan()!=null && quote.getLoanQuotePreferredLocationOfAvailingLoan()==1){
				if(quote.getLoanQuotePreferredDistrictId()==null){
					if(quote.getLoanQuotePreferredCityId()!=null && quote.getLoanQuotePreferredCityId()>0 && !Constants.OTHER_ID_INTEGER.equals(quote.getLoanQuotePreferredCityId())){
						MasterCity city = commonService.getCityById(quote.getLoanQuotePreferredCityId());
						if(city!=null){
							application.setAppDistrictId(city.getCityDistrictId());
						}
					}
				}else{
					application.setAppDistrictId(quote.getLoanQuotePreferredDistrictId());
				}
				if(bankLmsUserId==null){
					application.setAppPropertyStateId(0);
					application.setAppPropertyCityId(0);
					application.setAppPropertyDistrictId(0);
				}
			}else if(quote.getLoanQuotePreferredLocationOfAvailingLoan()!=null && quote.getLoanQuotePreferredLocationOfAvailingLoan()==2){
				if(bankLmsUserId!=null){
					if(application.getAppPreviousBranchId()!=null && application.getAppBranchId()!=null){
						if(!application.getAppPreviousBranchId().toString().equalsIgnoreCase(application.getAppBranchId().toString())){
							StringBuilder message = new StringBuilder("Application moved from ");
							MasterBranch masterBranch = commonService.getBranchById(application.getAppPreviousBranchId());
							if(masterBranch!=null){
								message.append(masterBranch.getBranchName()+ " ("+masterBranch.getBranchCode()+") " );
							}
							application.setAppPreviousBranchId(application.getAppBranchId());
							masterBranch = commonService.getBranchById(application.getAppBranchId());
							if(masterBranch!=null){
								message.append(" to "+masterBranch.getBranchName()+ " ("+masterBranch.getBranchCode()+") " );
							}
							if(!application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)){
								if(SessionUtil.getLeadId()==null){
									ApplicationFormLead lead = commonService.getLeadByAppSeqId(application.getAppSeqId(), Constants.HOME_LOAN_ID);
									commonService.insertCallLog(lead!=null?lead.getLeadId():null, (bankLmsUserId!=null?bankLmsUserId:Constants.OTHER_USER_ID) , application.getAppLoanStatusId(), message.toString());
								}
							}else{
								insertCallLog(application.getAppSeqId(), (bankLmsUserId!=null?bankLmsUserId:Constants.OTHER_USER_ID), application.getAppLoanStatusId(), message.toString(), null, true);
							}
							application.setAppAssignedLmsSalesUserId(null);
						}
					}else{
						application.setAppPreviousBranchId(application.getAppBranchId());
					}
				}else{
					application.setAppPreviousBranchId(application.getAppBranchId());
				}
				application.setAppPropertyStateId(quote.getLoanQuotePreferredStateId());
				application.setAppPropertyCityId(quote.getLoanQuotePreferredCityId());
				if(quote.getLoanQuotePreferredDistrictId()==null){
					if(!quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER) && quote.getLoanQuotePreferredCityId()>0){
						MasterCity city = commonService.getCityById(quote.getLoanQuotePreferredCityId());
						if(city!=null){
							application.setAppDistrictId(city.getCityDistrictId());
						}
					}
				}else{
					application.setAppPropertyDistrictId(quote.getLoanQuotePreferredDistrictId());				
				}
			}
			if((quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId() == 2 ) || (quote.getLoanQuoteCoapplicantFirstResidentTypeId() != null && quote.getLoanQuoteCoapplicantFirstResidentTypeId() == 2 ) || (quote.getLoanQuoteCoapplicantSecondResidentTypeId() != null && quote.getLoanQuoteCoapplicantSecondResidentTypeId() == 2)){
				isNRI=false;				
			}
			/*if(new Date().before(new SimpleDateFormat(Constants.DATE_FORMAT).parse(Constants.MAGICBRICK_END_DATE)) && new Date().after(new SimpleDateFormat(Constants.DATE_FORMAT).parse(Constants.MAGICBRICK_START_DATE))){
				String CampaignName="";
				if(ValidatorUtil.isValid(quote.getLoanQuoteNewVisitId())){
					CampaignName=commonService.getCampaignByVisitId(quote.getLoanQuoteNewVisitId());
					logger.info("HomeLoanHelper.java :: LNO : 517 : campaign name is :" +CampaignName);
				}
				if(CampaignName.equalsIgnoreCase(Constants.MAGICBRICKS_CAMPAIGN_NAME)){
					isMagicBricksCampaign=true;
				}
			}*/
			if(isNRI){
				if(isMagicBricksCampaign){
					logger.info("HomeLoanHelper.java :: LNO : 525 : inside MagicBricks");
					Integer MagicBricksalesTeamId=SalesTeamIdforMagicBricks(quote);
					logger.info("HomeLoanHelper.java :: LNO : 526 : MagicBricksalesTeamId is"+MagicBricksalesTeamId);
					application.setAppSalesTeamId(MagicBricksalesTeamId);
				}
				else{
					if(quote.getLoanQuotePreferredCityId()!=null && !quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
						List<MasterBranch> branches = null;
						branches=commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.HOME_LOAN_ID, null, quote.getLoanQuotePreferredCityId(), null, null, null,null,null);
						if(branches!=null && !branches.isEmpty()){
							if(branches.size()==1){
								MasterBranch masterBranch = branches.get(0);
								if(masterBranch!=null){
									Integer salesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
									application.setAppSalesTeamId(salesTeamId);
								}
							}
						}else{
							if(quote.getLoanQuotePrferredBranchId()!=null){
								MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
								if(masterBranch!=null){
									Integer salesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
									application.setAppSalesTeamId(salesTeamId);
								}
							}
						}
					}else{
						if(quote.getLoanQuotePrferredBranchId()!=null){
							MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
							if(masterBranch!=null){
								Integer salesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
								application.setAppSalesTeamId(salesTeamId);
							}
						}
					}
					if(quote.getLoanQuotePrferredBranchId()!=null){
						MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
						if(masterBranch!=null){
							Integer salesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
							application.setAppSalesTeamId(salesTeamId);
						}
					}
				}
			  }
			application.setAppCoapplicantTypeId_1(quote.getLoanQuoteCoapplicantFirstRelationshipId());
			application.setAppCoapplicantTypeId_2(quote.getLoanQuoteCoapplicantSecondRelationshipId());
			if(quote.getLoanQuoteEmployerName()!=null){
				application.setAppLoanEmployerName(quote.getLoanQuoteEmployerName());
			}
			application.setAppEmploymentType(quote.getLoanQuoteEmploymentTypeId());
			application.setAppCoapplicantEmployerName2(quote.getLoanQuoteCoapplicantSecondCoEmplyerName());
			application.setAppCoapplicantEmployerName1( quote.getLoanQuoteCoapplicantFirstCoEmplyerName());
			application.setAppLeadUpdateTime(new Date());
			
			
			application.setAppActive("Y");
			application.setAppDeleted("N");
			if(application.getAppApplyingFrom()==2){
				if(quote.getAppNRIMobileNo()!=null){
					application.setAppMobileNo(quote.getAppNRIMobileNo());
				}
			}else{
				if(quote.getLoanQuoteAppMobileNo()!=null){
					application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
					application.setAppMobileNo(quote.getLoanQuoteAppMobileNo());
				}
			}
			
			   if(quote.getLoanQuotePincode()!=null){
		  			application.setAppPincode(quote.getLoanQuotePincode());
		  		}
			if(quote.getLoanQuoteAddress1()!=null){
				application.setAppAddress1(quote.getLoanQuoteAddress1());
			}
			if(quote.getLoanQuoteAddress2()!=null){
				application.setAppAddress2(quote.getLoanQuoteAddress2());
			}
			if(quote.getLoanQuoteAppWorkEmail()!=null){
				application.setAppWorkEmail(quote.getLoanQuoteAppWorkEmail());
			}
			if(quote.getLoanQuoteAppFirstName()!=null){
				application.setAppFirstName(quote.getLoanQuoteAppFirstName());
			}
			 if (quote.getLoanQuotePanCardNo() != null) {
	              application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
	            }
	          
			if(bankLmsUserId!=null){
				if(application.getAppCreatedLmsUserId()==null){
					application.setAppAmendedLmsUserId(bankLmsUserId);
				}else{
					application.setAppAmendedLmsUserId(bankLmsUserId);
				}
				if(lmsUserIntermediaryId!=null){
					application.setAppIntermediaryId(lmsUserIntermediaryId);
				}
			}else{
			}
			if(quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
				application.setAppCountryId(quote.getLoanQuoteCountryId());
				application.setAppEMPCountryId(quote.getLoanQuoteCountryId());
				MasterCountry country = commonService.getCountrieById(quote.getLoanQuoteCountryId());
				if(country!=null){
					application.setAppISDCode(country.getCountryCode().toString());
				}
				application.setAppApplyingFrom(2);
			}else{
				application.setAppApplyingFrom(1);
				application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
			}
				if(ValidatorUtil.isValid(application.getAppBranchId())){
					if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBBJ){
						Integer[] circleIdNetworkModuleRegionId = commonService.getCircleIdNetworkModuleRegionByBranchId(application.getAppBranchId());
						if(ValidatorUtil.isValid(circleIdNetworkModuleRegionId[0]) 
								&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[1])
								&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[2])
								&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[3])){
							application.setAppCircleId(circleIdNetworkModuleRegionId[0]);
							application.setAppNetworkId(circleIdNetworkModuleRegionId[1]);
							application.setAppModuleId(circleIdNetworkModuleRegionId[2]);
							application.setAppRegionId(circleIdNetworkModuleRegionId[3]);
						}else{
							application.setError(Constants.SORRY_FOR_INCONVENIENCE);
							return application;
						}
					}
				}else{
					application.setError(Constants.SORRY_FOR_INCONVENIENCE);
					return application;
				}
			if(!ValidatorUtil.isValid(application.getAppLoanStatusId())){
				application.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return application;
			}
			if(!ValidatorUtil.isValid(application.getAppDataSourceId())){
				application.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return application;
			}
			  if (quote.getLoanQuoteAppFirstName() != null)
		          application.setAppFirstName(quote.getLoanQuoteAppFirstName()); 
			
			if(quote.getLoanQuoteMiddleName()!=null){
				application.setAppMiddleName(quote.getLoanQuoteMiddleName());
			}

			if(quote.getLoanQuoteLastName()!=null){
				application.setAppLastName(quote.getLoanQuoteLastName());
			}
			if(quote.getLoanQuoteIpAddress()!=null){
				application.setAppIpAddress(quote.getLoanQuoteIpAddress());
			}
			
			if(bankLmsUserId!=null){
				if(commonService.isBankUser(bankLmsUserId)){
					application.setAppAssignedLmsSalesUserId(bankLmsUserId);
				}
			}else if(SessionUtil.getBankLMSUser()!=null && SessionUtil.getBankLMSUser().getLmsUserId()!=null){
				if(commonService.isBankUser(bankLmsUserId)){
					application.setAppAssignedLmsSalesUserId(SessionUtil.getBankLMSUser().getLmsUserId());
				}
			}
			logger.info(" cheking the value alternate mobile number inside the helper class  " + quote.getAlternateMobileNumber());
		    if (quote.getAlternateMobileNumber() != null) {
		    	application.setAppAlternateMobileNumber(quote.getAlternateMobileNumber());
		        application.setAppAltISDCode(quote.getAppAltISDCode());
		    }
		    
		    //save consent id in main table
		    if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
		    	application.setAppConsentId(quote.getLoanQuoteConsentId());
		    }
		    //set flag for consent - NTB 
		    if (quote.getQuotePrivacyConsentFlag() != null && application.getAppPrivacyConsentFlag() == null) {
		    	application.setAppPrivacyConsentFlag(quote.getQuotePrivacyConsentFlag());
		    }
		    
		    if (quote.getQuoteNtbId() != null && application.getAppNtbId() == null) {
		    	application.setAppNtbId(quote.getQuoteNtbId());
		    }
		    
		    if (quote.getQuotePrivacyLocale() != null && application.getAppPrivacyLocale() == null) {
		    	application.setAppPrivacyLocale(quote.getQuotePrivacyLocale());
		    }
		    
		    application.setOcasID(SbiUtil.getOcasID());
			application = homeLoanService.save(application);
			return application;	
		}
	
	public ApplicationFormHomeLoanQuote saveCoApplicantDetails(ApplicationFormHomeLoanQuote quotePrev, ApplicationFormHomeLoanQuote quote) throws SQLException {
			if(quotePrev==null){
				return null;
			}
			if(quote==null){
				return null;
			}
			if (quote.getLoanQuoteCoapplicantFirstRelationshipId() != null) {
				quotePrev.setLoanQuoteCoapplicantFirstId(1);
				quotePrev.setLoanQuoteCoapplicantFirstRelationshipId(quote.getLoanQuoteCoapplicantFirstRelationshipId());
				quotePrev.setLoanQuoteCoapplicantFirstResidentTypeId(quote.getLoanQuoteCoapplicantFirstResidentTypeId());
				quotePrev.setLoanQuoteCoapplicantFirstDateOfBirth(quote.getLoanQuoteCoapplicantFirstDateOfBirth());
				quotePrev.setLoanQuoteCoapplicantFirstGender(quote.getLoanQuoteCoapplicantFirstGender());
				if(quote.getLoanQuoteCoapplicantFirstDateOfBirth()!=null){
					quotePrev.setLoanQuoteCoapplicantFirstDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantFirstDateOfBirth(), "MM/dd/yyyy"));
				}
				if(quote.getLoanQuoteCoapplicantSecondDateOfBirth()!=null){
					quotePrev.setLoanQuoteCoapplicantSecondDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantSecondDateOfBirth(), "MM/dd/yyyy"));
				}
				quotePrev.setLoanQuoteCoapplicantFirstCountryId(quote.getLoanQuoteCoapplicantFirstCountryId());
				quotePrev.setLoanQuoteCoapplicantFirstWorkExperience(quote.getLoanQuoteCoapplicantFirstWorkExperience());
				quotePrev.setLoanQuoteCoapplicantFirstCityId(quote.getLoanQuoteCoapplicantFirstCityId());
				quotePrev.setLoanQuoteCoapplicantFirstEmploymentTypeId(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId());
				if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()!=null && quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==1){
					quotePrev.setLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi(quote.getLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi());
					quotePrev.setLoanQuoteCoapplicantFirstCoEmplyerName(quote.getLoanQuoteCoapplicantFirstCoEmplyerName());
					quotePrev.setLoanQuoteCoapplicantFirstMonthlySalary(quote.getLoanQuoteCoapplicantFirstMonthlySalary());
					quotePrev.setLoanQuoteCoapplicantFirstVariableMonthPayon(quote.getLoanQuoteCoapplicantFirstVariableMonthPayon());
					quotePrev.setLoanQuoteCoapplicantFirstOtherIncome(quote.getLoanQuoteCoapplicantFirstOtherIncome());
					quotePrev.setLoanQuoteCoapplicantFirstPreEMIs(quote.getLoanQuoteCoapplicantFirstPreEMIs());
					quotePrev.setLoanQuoteCoapplicantFirstRetirementAge(quote.getLoanQuoteCoapplicantFirstRetirementAge());
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() ==2 || quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() ==3 ){
					quotePrev.setLoanQuoteCoapplicantFirstProfitAfterTax(quote.getLoanQuoteCoapplicantFirstProfitAfterTax());
					quotePrev.setLoanQuoteCoapplicantFirstDepreciatiation(quote.getLoanQuoteCoapplicantFirstDepreciatiation());
					quotePrev.setLoanQuoteCoapplicantFirstPreEMIs(quote.getLoanQuoteCoapplicantFirstPreEMIs());
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==4){
					quotePrev.setLoanQuoteCoapplicantFirstNetAnnualIncome(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome());	
					quotePrev.setLoanQuoteCoapplicantFirstOtherIncome(quote.getLoanQuoteCoapplicantFirstOtherIncome());
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==5){
					quotePrev.setLoanQuoteCoapplicantFirstNetMonthlyPension(quote.getLoanQuoteCoapplicantFirstNetMonthlyPension());
					quotePrev.setLoanQuoteCoapplicantFirstNetAnnualIncome(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome());
					quotePrev.setLoanQuoteCoapplicantFirstPreEMIs(quote.getLoanQuoteCoapplicantFirstPreEMIs());
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==7){
					quotePrev.setLoanQuoteCoapplicantFirstIncomeFromOtherSource(quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource());
				}
				quotePrev.setLoanQuoteCoapplicantFirstPreEMIs(quote.getLoanQuoteCoapplicantFirstPreEMIs());
			}

			if (quote.getLoanQuoteCoapplicantSecondRelationshipId() != null) {
				quotePrev.setLoanQuoteCoapplicantSecondId(1);
				quotePrev.setLoanQuoteCoapplicantSecondRelationshipId(quote.getLoanQuoteCoapplicantSecondRelationshipId());
				quotePrev.setLoanQuoteCoapplicantSecondResidentTypeId(quote.getLoanQuoteCoapplicantSecondResidentTypeId());
				quotePrev.setLoanQuoteCoapplicantSecondDateOfBirth(quote.getLoanQuoteCoapplicantSecondDateOfBirth());
				quotePrev.setLoanQuoteCoapplicantSecondGender(quote.getLoanQuoteCoapplicantSecondGender());
				quotePrev.setLoanQuoteCoapplicantSecondCountryId(quote.getLoanQuoteCoapplicantSecondCountryId());
				quotePrev.setLoanQuoteCoapplicantSecondWorkExperience(quote.getLoanQuoteCoapplicantSecondWorkExperience());
				quotePrev.setLoanQuoteCoapplicantSecondCityId(quote.getLoanQuoteCoapplicantSecondCityId());
				quotePrev.setLoanQuoteCoapplicantSecondEmploymentTypeId(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId());
				if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()!=null && quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==1){
					quotePrev.setLoanQuoteCoapplicantSecondCoSalaryAccountWithSbi(quote.getLoanQuoteCoapplicantSecondCoSalaryAccountWithSbi());
					quotePrev.setLoanQuoteCoapplicantSecondCoEmplyerName(quote.getLoanQuoteCoapplicantSecondCoEmplyerName());
					quotePrev.setLoanQuoteCoapplicantSecondMonthlySalary(quote.getLoanQuoteCoapplicantSecondMonthlySalary());
					quotePrev.setLoanQuoteCoapplicantSecondVariableMonthPayon(quote.getLoanQuoteCoapplicantSecondVariableMonthPayon());
					quotePrev.setLoanQuoteCoapplicantSecondOtherIncome(quote.getLoanQuoteCoapplicantSecondOtherIncome());
					quotePrev.setLoanQuoteCoapplicantSecondPreEMIs(quote.getLoanQuoteCoapplicantSecondPreEMIs());
					quotePrev.setLoanQuoteCoapplicantSecondRetirementAge(quote.getLoanQuoteCoapplicantSecondRetirementAge());
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() ==2 || quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() ==3 ){
					quotePrev.setLoanQuoteCoapplicantSecondProfitAfterTax(quote.getLoanQuoteCoapplicantSecondProfitAfterTax());
					quotePrev.setLoanQuoteCoapplicantSecondDepreciatiation(quote.getLoanQuoteCoapplicantSecondDepreciatiation());
					quotePrev.setLoanQuoteCoapplicantSecondPreEMIs(quote.getLoanQuoteCoapplicantSecondPreEMIs());
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==4){
					quotePrev.setLoanQuoteCoapplicantSecondNetAnnualIncome(quote.getLoanQuoteCoapplicantSecondNetAnnualIncome());	
					quotePrev.setLoanQuoteCoapplicantSecondOtherIncome(quote.getLoanQuoteCoapplicantSecondOtherIncome());
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==5){
					quotePrev.setLoanQuoteCoapplicantSecondNetMonthlyPension(quote.getLoanQuoteCoapplicantSecondNetMonthlyPension());
					quotePrev.setLoanQuoteCoapplicantSecondNetAnnualIncome(quote.getLoanQuoteCoapplicantSecondNetAnnualIncome());
					quotePrev.setLoanQuoteCoapplicantSecondPreEMIs(quote.getLoanQuoteCoapplicantSecondPreEMIs());
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==7){
					quotePrev.setLoanQuoteCoapplicantSecondIncomeFromOtherSource(quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource());
				}
				quotePrev.setLoanQuoteCoapplicantSecondPreEMIs(quote.getLoanQuoteCoapplicantSecondPreEMIs());
			}
			if("F".equalsIgnoreCase(quote.getLoanQuoteGender()) || "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender()) || "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender()) ){
				quotePrev.setLoanQuoteIsEligibleForHarGhar(1);
			}else{
				quotePrev.setLoanQuoteIsEligibleForHarGhar(0);
			}
			quotePrev = homeLoanService.save(quotePrev);
			return quotePrev;
		}
	
	
	public void insertCallLog(Integer callApplicationId, int callUserId, int callStatusId, String message, Date callDocPickupTime, boolean displayLog) throws SQLException {
			ApplicationFormHomeLoanCalls callsLog = new ApplicationFormHomeLoanCalls();
			callsLog.setCallApplicationId(callApplicationId);
			if(displayLog){
				callsLog.setCallActive("Y");
			}else{
				callsLog.setCallActive("N");
			}
			callsLog.setCallDeleted("N");
			callsLog.setCallIsGenericAlert("N");
			callsLog.setCallEndTime(new Date());
			if(callUserId==0){
				callsLog.setCallLmsUserId(Constants.OTHER_ID_INTEGER);
			}else{
				callsLog.setCallLmsUserId(callUserId);
			}
			callsLog.setCallStatusId(callStatusId);
			callsLog.setCallApplicationBankId(Constants.LEAD_BANK_ID);
			if(message==null){
				LoanStatus loanStatus=commonService.getLoanStatusByLoanStatusId(callStatusId);
				callsLog.setCallDescription(loanStatus!=null?loanStatus.getLoanStatusTitle():null);
			}else{

				callsLog.setCallDescription(message);
			}
			if(callDocPickupTime!=null){
				callsLog.setCallDocPickupTime(callDocPickupTime);
			}
			callsLog = homeLoanService.save(callsLog);		
	}
	public Integer SalesTeamIdforMagicBricks(ApplicationFormHomeLoanQuote quote) throws SQLException {
		Integer MagicBricksalesTeamId=0;
		List<MasterSalesTeam> salesTeam = null;
		salesTeam=commonService.getHLSTMPSTByBranchId(quote.getLoanQuotePrferredBranchId(),quote.getLoanQuoteLoanPurposeId());
		if(quote.getLoanQuotePreferredCityId()!=null && !quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
			List<MasterBranch> branches = null;
			branches=commonService.getBranchByLoanTypeStateDistrictRACPCScholar(Constants.HOME_LOAN_ID, null, quote.getLoanQuotePreferredCityId(), null, null, null,null,null);
			if(branches!=null && !branches.isEmpty()){
				if(branches.size()==1){
					MasterBranch masterBranch = branches.get(0);
					if(salesTeam!=null && !salesTeam.isEmpty()){
						for(int i=0;i<salesTeam.size();i++){
								MagicBricksalesTeamId=salesTeam.get(i).getSalesTeamId();
						}
					}else{
						if(masterBranch!=null){
							MagicBricksalesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
							
						}
					}
					
				}else{
					if(quote.getLoanQuotePrferredBranchId()!=null){
						MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
						if(salesTeam!=null && !salesTeam.isEmpty()){
							for(int i=0;i<salesTeam.size();i++){
									MagicBricksalesTeamId=salesTeam.get(i).getSalesTeamId();
							}
						}else{
							if(masterBranch!=null){
								MagicBricksalesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
								
							}
						}
					}
				}
			}else{
				if(quote.getLoanQuotePrferredBranchId()!=null){
					MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
					if(salesTeam!=null && !salesTeam.isEmpty()){
						for(int i=0;i<salesTeam.size();i++){
								MagicBricksalesTeamId=salesTeam.get(i).getSalesTeamId();
						}
					}else{
						if(masterBranch!=null){
							MagicBricksalesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
							
						}
					}
				}
			}
		}else{
			if(quote.getLoanQuotePrferredBranchId()!=null){
				MasterBranch masterBranch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
				if(salesTeam!=null && !salesTeam.isEmpty()){
					for(int i=0;i<salesTeam.size();i++){
							MagicBricksalesTeamId=salesTeam.get(i).getSalesTeamId();
					}
				}else{
					if(masterBranch!=null){
						MagicBricksalesTeamId = commonService.getHLSTMPSTByLoanTypeAndBranchId(Constants.HOME_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
						
					}
				}
			}
		}
		return MagicBricksalesTeamId;
	}

	public LoanScenarioBean callBREAgain(ApplicationFormHomeLoan application, ApplicationFormHomeLoanQuote quote){
		LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
		try{

			String previousGender = null;
			Integer previousCoapplicantResidetTypeId1 = null;
			Integer previousCoapplicantResidetTypeId2 = null;
			Double  previousPreEmi = null;
			Double  coappFirstDepreciatiation = null;
			Double  coappSecondDepreciatiation = null;

			if(Constants.BANK_ID == Constants.BANK_ID_SBBJ){
				previousGender = quote.getLoanQuoteGender();
				previousCoapplicantResidetTypeId1 = quote.getLoanQuoteCoapplicantFirstResidentTypeId();
				previousCoapplicantResidetTypeId2 = quote.getLoanQuoteCoapplicantSecondResidentTypeId();

				quote.setLoanQuoteGender("M");
				quote.setLoanQuoteCoapplicantFirstResidentTypeId(1);
				quote.setLoanQuoteCoapplicantSecondResidentTypeId(1);
			}
			if(Constants.BANK_ID == Constants.BANK_ID_SBI){
				if(ValidatorUtil.isValid(quote.getLoanQuotePreEMIs())){
					previousPreEmi = quote.getLoanQuotePreEMIs();
				}
				if(ValidatorUtil.isValid(quote.getLoanQuotePreEMIsOther())){
					quote.setLoanQuotePreEMIs((previousPreEmi!=null?previousPreEmi:0)+quote.getLoanQuotePreEMIsOther());
				}
				if(ValidatorUtil.isValid(quote.getLoanQuoteCoapplicantFirstDepreciatiation())){
					coappFirstDepreciatiation=quote.getLoanQuoteCoapplicantFirstDepreciatiation();
					quote.setLoanQuoteCoapplicantFirstDepreciatiation(quote.getLoanQuoteCoapplicantFirstDepreciatiation()/12);
				}
				if(ValidatorUtil.isValid(quote.getLoanQuoteCoapplicantSecondDepreciatiation())){
					coappSecondDepreciatiation=quote.getLoanQuoteCoapplicantSecondDepreciatiation();
					quote.setLoanQuoteCoapplicantSecondDepreciatiation(quote.getLoanQuoteCoapplicantSecondDepreciatiation()/12);
				}
			}
			quote = getUpdateQuote(quote);
			JSONObject engineRequest = JSONUtil.beanObjectToJSONObjct(quote);
			if(Constants.BANK_ID == Constants.BANK_ID_SBBJ){
				quote.setLoanQuoteGender(previousGender);
				quote.setLoanQuoteCoapplicantFirstResidentTypeId(previousCoapplicantResidetTypeId1);
				quote.setLoanQuoteCoapplicantSecondResidentTypeId(previousCoapplicantResidetTypeId2);
			}
			if(Constants.BANK_ID == Constants.BANK_ID_SBI){
				quote.setLoanQuotePreEMIs(previousPreEmi);
				quote.setLoanQuoteCoapplicantFirstDepreciatiation(coappFirstDepreciatiation);
				quote.setLoanQuoteCoapplicantSecondDepreciatiation(coappSecondDepreciatiation);
			}
			  
			  Integer count = this.commonService.getCountByAppIdAndLoanType(application.getAppSeqId().toString(), "A06", "SUCCESS");
		       logger.info("Checkinfo if Cibil request already made for App SEQ Id :: " + application.getAppSeqId());
		       if (count != null && count <= 0) {
		    	   try {
			           if (quote != null) {
				           quote = homeProcessManagerImpl.cibilCall(quote, application);
				           
				           Integer appCibilScore = Integer.valueOf((quote.getLoanQuoteCibilScore() != null) ? quote.getLoanQuoteCibilScore().intValue() : -1);
				           logger.info("HomeProcessManagerImpl.java :: appCibilScore :: " + appCibilScore);
				           application.setAppCibilScore(appCibilScore);
			           }
		    	   } catch (Exception eob) {
		    		   logger.info("PersonalProcessManagerImpl.java LNo: 1675 :: Calling :: ", eob);
		    	   }
		       }
		       engineRequest = this.SbiUtil.getDBCredentialForHelper(engineRequest); 
		       //if (application != null && application.getAppCibilScore() != null)
		       //  engineRequest.put("appCibilScore", application.getAppCibilScore()); 
		       
		       logger.info("quote.getLoanQuoteCibilScore():: "+quote.getLoanQuoteCibilScore());
		       logger.info("application.getAppCibilScore():: "+application.getAppCibilScore());
		       engineRequest.put("appCibilScore", 
		    		   (quote.getLoanQuoteCibilScore() != null) ? quote.getLoanQuoteCibilScore().intValue() : (application.getAppCibilScore() !=null ? application.getAppCibilScore().intValue() : -1));
		       //end
			
			JSONObject engineResponseJson = null;
			if(Constants.IS_ENGINE_OBF){
				JSONObject finalEngineRequest = MapperUtil.convertHomeLoan(engineRequest);
				engineResponseJson = commonEngine.callingRuleEngine("requestData="+finalEngineRequest, Constants.HOME_LOAN_ID);
			}else{
				engineResponseJson = commonEngine.callingRuleEngine("requestData="+engineRequest, Constants.HOME_LOAN_ID);
			}
			loanScenarioBean = (LoanScenarioBean) JSONUtil.getObjctFromJSON(loanScenarioBean, engineResponseJson.toString());
			application.setAppOfferJsonData(engineResponseJson.toString());
			loanScenarioBean.setApplicationHL(application);
		} catch (JSONException e){
			logger.info("HomeProcessManagerImpl.java LNo: 964 :: calling rule engine :: ", e);
			loanScenarioBean.setStatus(0);
			loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
		} catch (Exception e){
			logger.info("HomeProcessManagerImpl.java LNo: 964 :: calling rule engine :: ", e);
			loanScenarioBean.setStatus(0);
			loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
		}
		return loanScenarioBean;
	}
	
	public LoanScenarioBean callBRE(ApplicationFormHomeLoan application, ApplicationFormHomeLoanQuote quote, BankLmsUser bankLmsUser,
			Integer previousQuoteId, Integer trackVisitId, String ajaxPostUrl, boolean isLoggin) throws SQLException {
		LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
		
		loanScenarioBean = callBREAgain(application, quote);
		if(loanScenarioBean.getStatus()==0){
			return loanScenarioBean;
		}
		
		if(!isLoggin){
			return loanScenarioBean;
		}
		if(loanScenarioBean.getStatus()!=1){
			if(isLoggin){

				String noOfferReason =  loanScenarioBean.getNoOfferReason();
				StatusRequest statusRequest = new StatusRequest();
				statusRequest.setCurrentStatus(application.getAppLoanStatusId());
				statusRequest.setHaveLoanOffer(false);
				statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
				statusRequest.setState(1);
				statusRequest.setBankLMSUserId(bankLmsUser!=null? bankLmsUser.getLmsUserId() : 0);
				statusRequest.setRsmDecision(0);
				statusRequest.setApplicationType(SessionUtil.getApplicationType()!=null?SessionUtil.getApplicationType():0);
				statusRequest.setAppMobileVerified(application.getAppMobileVerified());
				statusRequest.setApplicationLeadType(application.getAppDataSourceId());
				statusRequest.setApplicationSubTypeId(application.getAppSubTypeId());
				StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
				if(statusManagerResponse.getStatus()!=0){
					application.setAppLoanStatusId(statusManagerResponse.getStatus());

					if(!statusManagerResponse.isPreserveData()){
						application.setAppLoanAmount(null);
						application.setAppLoanAccountType(null);
						application.setAppLoanInterestRate(null);
						application.setAppLoanTenure(null);
						application.setAppLoanProcessingFee(null);
						application.setAppHomeLoanId(null);
						application = homeLoanService.save(application);
					}
				} else if(application.getAppLoanStatusId() == 0 ){
					loanScenarioBean.setStatus(0);
					loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
					return loanScenarioBean;
				}
				if(statusManagerResponse.isPreserveData() && previousQuoteId>0){
					application.setAppQuoteId(previousQuoteId);
					application = homeLoanService.save(application);
				}
				if(statusManagerResponse.isEligibleToInsertLog()){
					insertCallLog(application.getAppSeqId(), (bankLmsUser!=null?bankLmsUser.getLmsUserId():0), statusManagerResponse.getStatusCallLogs(), noOfferReason, null, true);
				}
				if(SessionUtil.getApplicationType()!=null){

					if(SessionUtil.getApplicationType()==0){
					} else if(SessionUtil.getApplicationType()==1){
						if(SessionUtil.getLeadId()!=null){
							ApplicationFormLead lead=commonService.getLeadById(SessionUtil.getLeadId());
							if(statusManagerResponse.getStatusLead()!=0){
								lead.setLeadLoanStatusId(statusManagerResponse.getStatusLead());
								lead.setLeadAppSeqId(application.getAppSeqId());
								lead=commonService.save(lead);

							}
							application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
							application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
							if(lead.getLeadCreatedLmsUserId()!=null){
								if(application.getAppCreatedLmsUserId()==null){
									application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId());
								}
							}else{
								if(application.getAppCreatedLmsUserId()==null){
									if(bankLmsUser!=null){
										application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
									}
								}
							}
						}
					} else if(SessionUtil.getApplicationType()==2){

						SessionUtil.setApplicationType(1);
						
						ApplicationFormLead lead = null;
						if(SessionUtil.getLeadId()!=null){
							lead = commonService.getLeadById(SessionUtil.getLeadId());
						}else{
							lead = new ApplicationFormLead();
						}
						lead.setLeadFirstName((application.getAppFirstName()!=null?application.getAppFirstName():null));
						lead.setLeadApplyingFrom(application.getAppApplyingFrom());
						lead.setLeadIsdCode(((application.getAppISDCode()!=null?application.getAppISDCode():Constants.COUNTRY_CODE_INDIA)));
						lead.setLeadMobileNo(((application.getAppMobileNo()!=null?application.getAppMobileNo():null)));
						lead.setLeadCityId((quote.getLoanQuotePreferredCityId()!=null?quote.getLoanQuotePreferredCityId():Constants.OTHER_ID_INTEGER));
						lead.setLeadWorkEmail(application.getAppWorkEmail()!=null?application.getAppWorkEmail():"" );
						lead.setLeadProductTypeId(Constants.HOME_LOAN_ID);
						lead.setLeadLoanStatusId(statusManagerResponse.getStatusLead());
						lead.setLeadMobileAlertCount(1);
						lead.setLeadMobileVerificationCode(Integer.parseInt(Constants.DUMMY_MOBILE_OTP));
						lead.setLeadVisitId(trackVisitId);
						lead.setLeadActive("Y");
						lead.setLeadDeleted("N");

						lead.setLeadIntermediaryId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						lead.setLeadCreatedLmsUserId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						lead.setLeadMobileVerificationCodeVerified("N");
						lead.setLeadReceiveDatetime(new Date());
						lead.setLeadEntryTime(new Date());
						lead.setLeadEntryDate(new Date());
						lead.setLeadLastUpdated(new Date());
						if(lead.getLeadSourceId()==null){
							lead.setLeadSourceId(Constants.LEAD_SOURCE_ID);
						}
						if(lead.getLeadDataSourceId()==null){
							lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
						}
						if(bankLmsUser!=null && bankLmsUser.getLmsUserId() != null ){
							List<BankLmsUserRole> lmsUserRole = commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
							if(lmsUserRole!=null){
								for(BankLmsUserRole bankLmsUserRole : lmsUserRole){
									if(lmsUserRole!= null && bankLmsUserRole.getLmsRoleTypeId()!=null && (3 == bankLmsUserRole.getLmsRoleTypeId()||4 == bankLmsUserRole.getLmsRoleTypeId())){
										lead.setLeadAssignedLmsUserId(bankLmsUser.getLmsUserId());
									}
								}
							}
						}
						lead.setLeadAppSeqId(application.getAppSeqId());
						if(bankLmsUser!=null){
							if(bankLmsUser.getLmsUserType() == 1 || bankLmsUser.getLmsUserType() == 3){
								lead.setLeadAppContactCenterLocation(0);
							}else{
								Integer lmsUserLocationId = commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
								if(lmsUserLocationId!=null){
									lead.setLeadAppContactCenterLocation(lmsUserLocationId);
								}else{
									lead.setLeadAppContactCenterLocation(1);
								}
							}
						}
						lead=commonService.save(lead);
						
						SessionUtil.setLeadId(lead.getLeadId());

						application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
						application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
						if(lead.getLeadCreatedLmsUserId()!=null){
							if(application.getAppCreatedLmsUserId()==null){
								application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId());
							}
						}else{
							if(application.getAppCreatedLmsUserId()==null){
								if(bankLmsUser!=null){
									application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
								}
							}
						}
					}
				}
				return loanScenarioBean;
			}else{
				quote.setLoanQuoteLoanProductId(null);
				quote.setLoanQuoteLoanTenure(null);
				quote.setLoanQuoteLoanAmount(null);
				quote.setLoanQuoteAppliedCoupon(null);
				quote.setLoanQuoteLoanAccountType(1);
				loanScenarioBean=callBREAgain(application, quote);
				if(loanScenarioBean.getStatus()==0){
					return loanScenarioBean;
				}
			}
		}

		if(SessionUtil.getApplicationType()!=null){
			if(isLoggin){
				StatusRequest statusRequest = new StatusRequest();
				statusRequest.setCurrentStatus(application.getAppLoanStatusId());
				statusRequest.setHaveLoanOffer(true);
				statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
				statusRequest.setState(1);
				statusRequest.setBankLMSUserId(bankLmsUser!=null? bankLmsUser.getLmsUserId() : 0);
				statusRequest.setRsmDecision(0);
				statusRequest.setAppMobileVerified(application.getAppMobileVerified());
				statusRequest.setApplicationType(SessionUtil.getApplicationType()!=null?SessionUtil.getApplicationType():0);
				statusRequest.setApplicationLeadType(application.getAppDataSourceId());
				statusRequest.setRequestFirstTime(previousQuoteId == 0 ?true:false);
				statusRequest.setApplicationSubTypeId(application.getAppSubTypeId());
				StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
				if(statusManagerResponse.getStatus()!=0){
					application.setAppLoanStatusId(statusManagerResponse.getStatus());
				} else if(application.getAppLoanStatusId() == 0 ){
					loanScenarioBean.setStatus(0);
					loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
					return loanScenarioBean;
				}
				if(statusManagerResponse.isEligibleToInsertLog()){
					insertCallLog(application.getAppSeqId(), (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);
				}
				
				

				if(SessionUtil.getApplicationType()==0){

				}else if(SessionUtil.getApplicationType()==1){

					if(SessionUtil.getLeadId()!=null){
						ApplicationFormLead lead=commonService.getLeadById(SessionUtil.getLeadId());
						if(statusManagerResponse.getStatusLead()!=0){
							lead.setLeadLoanStatusId(statusManagerResponse.getStatusLead());
							lead.setLeadAppSeqId(application.getAppSeqId());
							lead=commonService.save(lead);

						}
						application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
						application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
						if(lead.getLeadCreatedLmsUserId()!=null){
							if(application.getAppCreatedLmsUserId()==null){
								application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId());
							}
						}else{
							if(application.getAppCreatedLmsUserId()==null){
								if(bankLmsUser!=null){
									application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
								}
							}
						}
					}
				}else if(SessionUtil.getApplicationType()==2){

					SessionUtil.setApplicationType(1);
					
					ApplicationFormLead lead = null;
					if(SessionUtil.getLeadId()!=null){
						lead = commonService.getLeadById(SessionUtil.getLeadId());
					}else{
						lead = new ApplicationFormLead();
					}
					lead.setLeadFirstName((application.getAppFirstName()!=null?application.getAppFirstName():null));
					lead.setLeadApplyingFrom(application.getAppApplyingFrom());
					lead.setLeadIsdCode(((application.getAppISDCode()!=null?application.getAppISDCode():Constants.COUNTRY_CODE_INDIA)));
					lead.setLeadMobileNo(((application.getAppMobileNo()!=null?application.getAppMobileNo():null)));
					lead.setLeadCityId((quote.getLoanQuotePreferredCityId()!=null?quote.getLoanQuotePreferredCityId():Constants.OTHER_ID_INTEGER));
					lead.setLeadWorkEmail(application.getAppWorkEmail()!=null?application.getAppWorkEmail():"" );
					lead.setLeadProductTypeId(Constants.HOME_LOAN_ID);
					lead.setLeadLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE105_ID);
					lead.setLeadMobileAlertCount(1);
					lead.setLeadMobileVerificationCode(Integer.parseInt(Constants.DUMMY_MOBILE_OTP));
					lead.setLeadVisitId(trackVisitId);
					lead.setLeadActive("Y");
					lead.setLeadDeleted("N");

					lead.setLeadIntermediaryId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
					lead.setLeadCreatedLmsUserId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
					if(application.getAppSubTypeId()!=null && application.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)){
						if(application.getAppMobileVerified()!=null && application.getAppMobileVerified().equalsIgnoreCase("Y")){
							lead.setLeadMobileVerificationCodeVerified("Y");
						}
					}else{
						lead.setLeadMobileVerificationCodeVerified("N");
					}
					lead.setLeadReceiveDatetime(new Date());
					lead.setLeadEntryTime(new Date());
					lead.setLeadEntryDate(new Date());
					lead.setLeadLastUpdated(new Date());
					if(lead.getLeadSourceId()==null){
						lead.setLeadSourceId(Constants.LEAD_SOURCE_ID);
					}
					if(lead.getLeadDataSourceId()==null){
						lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
					}
					if(bankLmsUser!=null && bankLmsUser.getLmsUserId() != null ){
						List<BankLmsUserRole> lmsUserRole = commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
						if(lmsUserRole!=null){
							for(BankLmsUserRole bankLmsUserRole : lmsUserRole){
								if(lmsUserRole!= null && bankLmsUserRole.getLmsRoleTypeId()!=null && (3 == bankLmsUserRole.getLmsRoleTypeId()||4 == bankLmsUserRole.getLmsRoleTypeId())){
									lead.setLeadAssignedLmsUserId(bankLmsUser.getLmsUserId());
								}
							}
						}
					}
					lead.setLeadAppSeqId(application.getAppSeqId());
					if(bankLmsUser!=null){
						if(bankLmsUser.getLmsUserType() == 1 || bankLmsUser.getLmsUserType() == 3){
							lead.setLeadAppContactCenterLocation(0);
						}else{
							Integer lmsUserLocationId = commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
							if(lmsUserLocationId!=null){
								lead.setLeadAppContactCenterLocation(lmsUserLocationId);
							}else{
								lead.setLeadAppContactCenterLocation(1);
							}
						}
					}
					lead=commonService.save(lead);
					
					SessionUtil.setLeadId(lead.getLeadId());

					application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
					application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
					if(lead.getLeadCreatedLmsUserId()!=null){
						if(application.getAppCreatedLmsUserId()==null){
							application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId());
						}
					}else{
						if(application.getAppCreatedLmsUserId()==null){
							if(bankLmsUser!=null){
								application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
							}
						}
					}
				}
			}
		}

		loanScenarioBean.setNetAnnualIncome(quote.getLoanQuoteNetAnnualIncomeOfApplicant());
		loanScenarioBean.setProjectCost(quote.getLoanQuoteProjectCost());


		loanScenarioBean.setShowCoApplicant(1);
		if(quote.getLoanQuoteCoapplicantFirstId()!=null && quote.getLoanQuoteCoapplicantSecondId()!=null  ){
			loanScenarioBean.setShowCoApplicant(0);
		}
		
		if(quote.getLoanQuoteLoanCategoryId()!=null){
			MasterLoanCategory loanCategory = commonService.getLoanCategoryById(quote.getLoanQuoteLoanCategoryId());
			loanScenarioBean.setLoanPurpose(loanCategory.getCategoryType());
		}else if(quote.getLoanQuoteLoanPurposeId()!=null){
			MasterLoanPurpose loanPurpose = commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
			loanScenarioBean.setLoanPurpose(loanPurpose.getLpTypeValue());
		}
		if(quote.getLoanQuoteLoanAccountType()!=null && (quote.getLoanQuoteLoanAccountType() == 2 && loanScenarioBean.getQuoteAccountType()==1) ){
			loanScenarioBean.setStatus(3);
			loanScenarioBean.setMessage(Constants.MESSAGE_HL_1);
		}
		loanScenarioBean.setShowTermOverDraftCheck(1);
		loanScenarioBean.setShowOffer(0);
	
		CommonQuote termPreserve = null;
		CommonQuote overDraftPreserve = null;
		Map<Integer,String> productName=new HashMap<Integer,String>();

		List<CommonQuote> allQuotes = new ArrayList<CommonQuote>();
		Map<Integer, AccountTypeTermOverDraft>loanQuotes = loanScenarioBean.getLoanQuotes();
		int count=0;
		double flexiPayInterestRate = 0.0;

		loanScenarioBean.setThirdProductUrl(null);
		loanScenarioBean.setSecondProductUrl(null);
		loanScenarioBean.setFirstProductUrl(null);
		if(loanQuotes.entrySet().size()==2){
			loanScenarioBean.setShowFirstTermOverDraftCheck(1);
			loanScenarioBean.setShowSecondTermOverDraftCheck(1);
		}else{
			loanScenarioBean.setShowFirstTermOverDraftCheck(1);
		}
	
		Double calculatedSecondEmi = 0.0;
		for(Map.Entry<Integer, AccountTypeTermOverDraft> entry : loanQuotes.entrySet()){
			count=count+1;
			AccountTypeTermOverDraft typeTermOverDraft = entry.getValue();
			CommonQuote term = (CommonQuote)typeTermOverDraft.getAccountTypeTerm();
			if(term!=null){
				HlProduct product =  commonService.getHomeLoanProductById(term.getProductTypeId());
				if(product!=null){
					term.setProductTypeName(product.getHlProductName());
					productName.put(product.getHlProductId(), product.getHlProductName());
					loanScenarioBean.setProductSliderDigit(product.getHlProductSliderAmtMul()/100000);
					loanScenarioBean.setProductSliderDigitExact(product.getHlProductSliderDigit());
					loanScenarioBean.setProductSliderTenure(product.getHlProductSliderTenure());
					loanScenarioBean.setProductSliderAmtMul(product.getHlProductSliderAmtMul());
					loanScenarioBean.setProductSliderTenureChn(product.getHlProductSliderTenureChn());
					loanScenarioBean.setSecondProductName(product.getHlProductName());
					if(loanScenarioBean.getFirstProductUrl()==null){
						loanScenarioBean.setFirstProductUrl(product.getProductUrl());
					}else if(loanScenarioBean.getSecondProductUrl()==null){
						loanScenarioBean.setSecondProductUrl(product.getProductUrl());
					}else{
						loanScenarioBean.setSecondProductUrl(product.getProductUrl());
					}
				}else{
					term.setProductTypeName("N/A");
				}

				
				if(loanScenarioBean.getShowOffer()==0 && product.getHlProductId()== 3){
					loanScenarioBean.setShowOffer(1);
				}
			
				term.setAccountTypeId(1);
				int appMoratorimMonths = 0;
				if(Constants.BANK_ID == Constants.BANK_ID_SBI){
					if(Constants.FLEXI_PAY_PRODUCT_ID.equals(term.getProductTypeId()) ){
						if(term.getDiscountedInterestRate()!=null && term.getDiscountedInterestRate()>0){
							flexiPayInterestRate = term.getDiscountedInterestRate();
						}else{
							flexiPayInterestRate = term.getInterestRate();
						}
						appMoratorimMonths = 36;
						if(quote.getLoanQuoteLoanCategoryId()!=null
								&& ( quote.getLoanQuoteLoanCategoryId()==2 || quote.getLoanQuoteLoanCategoryId() ==4)
								&&  quote.getLoanQuoteCompletionDate()!=null ){
							appMoratorimMonths = appMoratorimMonths+ DateUtil.numberOfMonthsBewteenTwoDate(new Date(), quote.getLoanQuoteCompletionDate());
							if(appMoratorimMonths > 60){
								appMoratorimMonths = 60;
							}
						}
						application.setAppMoratorimMonths(appMoratorimMonths);
					}
				}else{
					if(Constants.FLEXI_PAY_PRODUCT_ID.equals(term.getProductTypeId()) ){
						appMoratorimMonths = 36;
					}
					application.setAppMoratorimMonths(appMoratorimMonths);
				}

				


				if(Constants.BHL_PRODUCT_ID.equals(term.getProductTypeId()) ){
					Double interestRate = 0.0;
					DecimalFormat df = new DecimalFormat("#.##");
					if(term.getDiscountedInterestRate()!=null){
						interestRate =term.getDiscountedInterestRate();
					}else{
						if ((quote.getLoanQuoteGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteGender())) || (quote.getLoanQuoteCoapplicantFirstGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender())) || (quote.getLoanQuoteCoapplicantSecondGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender()))) {
							BigDecimal recievedinterestRate = new BigDecimal(term.getInterestRate());
							BigDecimal substractBps = new BigDecimal("0.05");
							interestRate=recievedinterestRate.subtract(substractBps).doubleValue();
						}else{
							interestRate = term.getInterestRate();
						}
					}
					
					if (quote.getLoanQuoteCoapplicantFirstGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender())) {
						application.setAppCoapplicantGender_1(quote.getLoanQuoteCoapplicantFirstGender());
					}
					if (quote.getLoanQuoteCoapplicantSecondGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender())) {
						application.setAppCoapplicantGender_2(quote.getLoanQuoteCoapplicantSecondGender());
					}
					calculatedSecondEmi = (loanScenarioBean.getChosenEligibility()*interestRate)/(100*12);
					Long calculatedSecondEmiLong = Math.round(calculatedSecondEmi);
					term.setInterestRate(Double.valueOf(df.format(interestRate)));
					term.setEmi(calculatedSecondEmiLong.doubleValue());
					application.setAppLoanInterestRate(interestRate.floatValue());
				}
				if(isLoggin){
					term = commonEngine.getCalculatedQuote(term, loanScenarioBean.getChosenTenure(), ajaxPostUrl, appMoratorimMonths, loanScenarioBean.getProductSliderTenure(), false);
				}

				term.setIsDiscountApplied(0);
				term.setChosenAccountTypeId(ValidatorUtil.isValid(quote.getLoanQuoteLoanAccountType())?quote.getLoanQuoteLoanAccountType():1);
				term.setChosenProductTypeId(quote.getLoanQuoteLoanProductId()!=null?quote.getLoanQuoteLoanProductId():1);
				allQuotes.add(term);
				if(termPreserve==null){
					termPreserve = term;
				}
				if(loanScenarioBean.getChosenProductId()==null){
					loanScenarioBean.setChosenProductId(term.getProductTypeId());
				}
				typeTermOverDraft.setAccountTypeTerm(term);
			}

			CommonQuote overDraft = (CommonQuote)typeTermOverDraft.getAccountTypeOverDraft();
			if(overDraft!=null){
				HlProduct product =  commonService.getHomeLoanProductById(overDraft.getProductTypeId());
				if(product!=null){

					loanScenarioBean.setProductSliderDigit(product.getHlProductSliderAmtMul()/100000);
					loanScenarioBean.setProductSliderDigitExact(product.getHlProductSliderDigit());
					loanScenarioBean.setProductSliderTenure(product.getHlProductSliderTenure());
					loanScenarioBean.setProductSliderAmtMul(product.getHlProductSliderAmtMul());
					loanScenarioBean.setProductSliderTenureChn(product.getHlProductSliderTenureChn());
					overDraft.setProductTypeName(product.getHlProductName());
					productName.put(product.getHlProductId(), product.getHlProductName());
					
					loanScenarioBean.setFirstProductName(product.getHlProductName());

					if(loanScenarioBean.getFirstProductUrl()==null){
						loanScenarioBean.setFirstProductUrl(product.getProductUrl());
					}else if(loanScenarioBean.getSecondProductUrl()==null){
						loanScenarioBean.setSecondProductUrl(product.getProductUrl());
					}else{
						loanScenarioBean.setThirdProductUrl(product.getProductUrl());
					}

				}else{
					overDraft.setProductTypeName("N/A");
					loanScenarioBean.setMinEligibility(loanScenarioBean.getMinEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
					loanScenarioBean.setChosenEligibility(loanScenarioBean.getChosenEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
					loanScenarioBean.setMaxEligibility(loanScenarioBean.getMaxEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
					loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure()/Constants.LOAN_TENURE_MULTIPLER_FACTOR);
					loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure()/Constants.LOAN_TENURE_MULTIPLER_FACTOR);
					loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure()/Constants.LOAN_TENURE_MULTIPLER_FACTOR);
				}
				if(loanScenarioBean.getShowOffer()==0 && product.getHlProductId()== 3){
					loanScenarioBean.setShowOffer(1);
				}
			
				overDraft.setAccountTypeId(2);

				int appMoratorimMonths = 0;
				if(Constants.BANK_ID == Constants.BANK_ID_SBI){
					if(Constants.FLEXI_PAY_PRODUCT_ID.equals(overDraft.getProductTypeId()) ){
						if(overDraft!=null && overDraft.getDiscountedInterestRate()!=null && overDraft.getDiscountedInterestRate()>0){
							flexiPayInterestRate = overDraft.getDiscountedInterestRate();
						}else{
							flexiPayInterestRate = overDraft.getInterestRate();
						}
						appMoratorimMonths = 36;
						if(quote.getLoanQuoteLoanCategoryId()!=null
								&& ( quote.getLoanQuoteLoanCategoryId()==2 || quote.getLoanQuoteLoanCategoryId() ==4)
								&&  quote.getLoanQuoteCompletionDate()!=null ){
							appMoratorimMonths = appMoratorimMonths+ DateUtil.numberOfMonthsBewteenTwoDate(new Date(), quote.getLoanQuoteCompletionDate());
							if(appMoratorimMonths > 60){
								appMoratorimMonths = 60;
							}
						}
						application.setAppMoratorimMonths(appMoratorimMonths);
					}
				}else{
					if(Constants.FLEXI_PAY_PRODUCT_ID.equals(overDraft.getProductTypeId()) ){
						appMoratorimMonths = 36;
					}
					application.setAppMoratorimMonths(appMoratorimMonths);
				}

				
				if(isLoggin){
					overDraft = commonEngine.getCalculatedQuote(overDraft, loanScenarioBean.getChosenTenure(), ajaxPostUrl, appMoratorimMonths, loanScenarioBean.getProductSliderTenure(), false);
				}

				overDraft.setIsDiscountApplied(0);
				overDraft.setChosenAccountTypeId(ValidatorUtil.isValid(quote.getLoanQuoteLoanAccountType())?quote.getLoanQuoteLoanAccountType():1);
				overDraft.setChosenProductTypeId(quote.getLoanQuoteLoanProductId()!=null?quote.getLoanQuoteLoanProductId():1);
				allQuotes.add(overDraft);
				if(overDraftPreserve==null){
					overDraftPreserve = overDraft;
				}
				if(loanScenarioBean.getChosenProductId()==null){
					loanScenarioBean.setChosenProductId(overDraftPreserve.getProductTypeId());
				}
				typeTermOverDraft.setAccountTypeOverDraft(overDraft);
			}

			loanQuotes.put(entry.getKey(), typeTermOverDraft);
			if(isLoggin){
				if(term == null || overDraft == null){
					loanScenarioBean.setShowTermOverDraftCheck(0);
				}
				if(loanQuotes.entrySet().size()==2){
					if((term == null || overDraft == null) && (count==2)){
						loanScenarioBean.setShowSecondTermOverDraftCheck(0);
					}
				}else{
					if((term == null || overDraft == null) && count==1){
						loanScenarioBean.setShowFirstTermOverDraftCheck(0);
					}
				}
			}
		}
		if(isLoggin){
			int index =1;
			for(Map.Entry<Integer, String> entry : productName.entrySet()){
				if(index==1){
					loanScenarioBean.setFirstProductName(entry.getValue());
					loanScenarioBean.setSecondProductName(null);
					loanScenarioBean.setThirdProductName(null);
				}if(index==2){
					loanScenarioBean.setSecondProductName(entry.getValue());
					loanScenarioBean.setThirdProductName(null);
				}if(index==3){
					loanScenarioBean.setThirdProductName(entry.getValue());
				}
				index++;
			}
		}
		loanScenarioBean.setAllQuotes(allQuotes);
		loanScenarioBean.setLoanQuotes(loanQuotes);
		if(isLoggin){
			if(application.getAppWorkEmail()!=null){
				if(SessionUtil.getEmail()==null){
					SessionUtil.setEmail(application.getAppWorkEmail());
				}
			}
			if(application.getAppMobileNo()!=null){
				if(SessionUtil.getMobile()==null){
					SessionUtil.setMobile(application.getAppMobileNo());
				}
			}
			if(application.getAppFirstName()!=null){
				if(SessionUtil.getApplicantName()==null){
					SessionUtil.setApplicantName(application.getAppFirstName());
				}
			}
		}


		application.setAppLoanMaxAmont(loanScenarioBean.getMaxEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
		application.setAppLoanAmount(loanScenarioBean.getChosenEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
		application.setAppLoanTenure(loanScenarioBean.getProductSliderTenure()==1? loanScenarioBean.getChosenTenure():loanScenarioBean.getChosenTenure()/12);

		if(overDraftPreserve!=null){
			application.setAppLoanEmi(overDraftPreserve.getEmi());
			application.setAppLoanInterestRate(overDraftPreserve.getInterestRate().floatValue());
			application.setAppLoanInterestRateType(overDraftPreserve.getRateType());
			application.setAppLoanProcessingFee((double) (overDraftPreserve.getProcessingFee()));
			application.setAppHomeLoanId(overDraftPreserve.getProductTypeId());
			application.setAppLoanAccountType(2);
			application.setAppEmiNmiRatio(overDraftPreserve.getEmiNmiRatio());
			
			if(overDraftPreserve.getDiscountedEmi()!=null){
				application.setAppLoanEmiDiscount(overDraftPreserve.getDiscountedEmi());
			}else{
				application.setAppLoanEmiDiscount(null);
			}
			if(overDraftPreserve.getDiscountedInterestRate()!=null){
				application.setAppLoanInterestRateDiscount(overDraftPreserve.getDiscountedInterestRate().floatValue());
			}else{
				application.setAppLoanInterestRateDiscount(null);
			}
			if(overDraftPreserve.getDiscountedProcessingFee()!=null){
				application.setAppLoanProcessingFeeDiscount((double) new Double(overDraftPreserve.getDiscountedProcessingFee()));
			}else{
				application.setAppLoanProcessingFeeDiscount(null);
			}

			
		}
		if(overDraftPreserve == null && termPreserve!=null){
			application.setAppLoanEmi(termPreserve.getEmi());
			application.setAppLoanInterestRate(termPreserve.getInterestRate().floatValue());
			application.setAppLoanInterestRateType(termPreserve.getRateType());
			application.setAppLoanProcessingFee((double) termPreserve.getProcessingFee());
			if(loanScenarioBean.getChosenProductId()!=null){
				application.setAppHomeLoanId(loanScenarioBean.getChosenProductId());
			}else{
				application.setAppHomeLoanId(termPreserve.getProductTypeId());
			}
			application.setAppLoanAccountType(1);
			application.setAppEmiNmiRatio(termPreserve.getEmiNmiRatio());


			if(termPreserve.getDiscountedEmi()!=null){
				application.setAppLoanEmiDiscount(termPreserve.getDiscountedEmi());
			}else{
				application.setAppLoanEmiDiscount(null);
			}
			if(termPreserve.getDiscountedInterestRate()!=null){
				application.setAppLoanInterestRateDiscount(termPreserve.getDiscountedInterestRate().floatValue());
			}else{
				application.setAppLoanInterestRateDiscount(null);
			}
			if(termPreserve.getDiscountedProcessingFee()!=null){
				application.setAppLoanProcessingFeeDiscount((double) new Double(termPreserve.getDiscountedProcessingFee()));
			}else{
				application.setAppLoanProcessingFeeDiscount(null);
			}

			
			
			if(Constants.BHL_PRODUCT_ID.equals(termPreserve.getProductTypeId()) ){
				Double choosenEligibility = application.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR; 

				Double baseRate = 8.75;
				Double spreadRate = Constants.BHL_SPREAD_RATE;
				Double firstInterest=0.0;
				if (baseRate!=null) {
					firstInterest = baseRate+spreadRate;
				}/*else {
					firstInterest = spreadRate;
				}*/
				
				
				if ((quote.getLoanQuoteGender()!=null && ("F").equalsIgnoreCase(quote.getLoanQuoteGender())) || (quote.getLoanQuoteCoapplicantFirstGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender())) || (quote.getLoanQuoteCoapplicantSecondGender()!=null && "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender()))) {
					BigDecimal recievedinterestRate = new BigDecimal(firstInterest);
					BigDecimal substractBps = new BigDecimal("0.05");
					firstInterest=recievedinterestRate.subtract(substractBps).doubleValue();
				}
				Double calculatedFirstEmi = (choosenEligibility*firstInterest)/(100*12);
				Long calculatedFirstEmiLong = Math.round(calculatedFirstEmi);
				application.setAppBhlFirstInterestEmi(calculatedFirstEmiLong.doubleValue());
				termPreserve.setAppBhlFirstInterestEmi(calculatedFirstEmiLong.doubleValue());
				application.setAppBhlInterestRate(firstInterest);
				termPreserve.setAppBhlInterestRate(application.getAppBhlInterestRate());
				Long calculatedSecondEmiLong = Math.round(calculatedSecondEmi);
				application.setAppLoanEmi(calculatedSecondEmiLong.doubleValue());
				termPreserve.setEmi(calculatedSecondEmiLong.doubleValue());
			}
		}


		if(loanScenarioBean.getProductSliderTenure()==1){
			loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure());
			loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure());
			loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure());
		}else {
			loanScenarioBean.setMaxTenure(loanScenarioBean.getMaxTenure()/12);
			loanScenarioBean.setChosenTenure(loanScenarioBean.getChosenTenure()/12);
			loanScenarioBean.setMinTenure(loanScenarioBean.getMinTenure()/12);
		}


		loanScenarioBean.setMinEligibility(loanScenarioBean.getMinEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
		loanScenarioBean.setChosenEligibility(loanScenarioBean.getChosenEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
		loanScenarioBean.setMaxEligibility(loanScenarioBean.getMaxEligibility()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
		loanScenarioBean.setChosenLoanAccountType(ValidatorUtil.isValid(quote.getLoanQuoteLoanAccountType())?quote.getLoanQuoteLoanAccountType():1);
		loanScenarioBean.setChosenProductId(loanScenarioBean.getChosenProductId());

		loanScenarioBean.setRetirementAgeDisclaimer(0);
		if(quote.getLoanQuoteRetirementAgeApplicant()!=null){
			int currentAge = DateUtil.getAge(quote.getLoanQuoteDateOfBirthDT());
			int ageDifference = quote.getLoanQuoteRetirementAgeApplicant()-currentAge;
			if(loanScenarioBean.getChosenTenure() > ageDifference){
				loanScenarioBean.setRetirementAgeDisclaimer(1);
			}
		}
		if(loanScenarioBean.getRetirementAgeDisclaimer()==0){
			if(quote.getLoanQuoteCoapplicantFirstRetirementAge()!=null){
				int currentAge = DateUtil.getAge(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT());
				int ageDifference = quote.getLoanQuoteCoapplicantFirstRetirementAge()-currentAge;
				if(loanScenarioBean.getChosenTenure() > ageDifference){
					loanScenarioBean.setRetirementAgeDisclaimer(1);
				}
			}
		}
		if(loanScenarioBean.getRetirementAgeDisclaimer()==0){
			if(quote.getLoanQuoteCoapplicantSecondRetirementAge()!=null){
				int currentAge = DateUtil.getAge(quote.getLoanQuoteCoapplicantSecondDateOfBirthDT());
				int ageDifference = quote.getLoanQuoteCoapplicantSecondRetirementAge()-currentAge;
				if(loanScenarioBean.getChosenTenure() > ageDifference){
					loanScenarioBean.setRetirementAgeDisclaimer(1);
				}
			}
		}
		if(Constants.BANK_ID == Constants.BANK_ID_SBI){
			if(application.getAppMoratorimMonths()>0){
				String appFlexiPayDetails=null;
				appFlexiPayDetails= SbiUtil.getFlexiPayEmis(application.getAppLoanAmount(), application.getAppLoanTenure()*12, flexiPayInterestRate, 0, 1, 1, application.getAppMoratorimMonths());
				application.setAppFlexiPayDetails(appFlexiPayDetails);
			}else{
				application.setAppFlexiPayDetails(null);
			}
		}

		if(loanScenarioBean.getLoanQuotes()!=null){
			Set<Map.Entry<Integer,AccountTypeTermOverDraft>> entrySet = loanScenarioBean.getLoanQuotes().entrySet();
			for(Entry<Integer, AccountTypeTermOverDraft> entry : entrySet){
				AccountTypeTermOverDraft accountTypeTermOverDraft = entry.getValue();
				double emi1 = 0.0;
				double emi2 = 0.0;
				double discountedEmi1 = 0.0;
				double discountedEmi2 = 0.0;
				if(accountTypeTermOverDraft.getAccountTypeTerm()!=null && accountTypeTermOverDraft.getAccountTypeTerm().getEmiArray()!=null){
					Set<Map.Entry<Integer,Double>> emiEntrySet= accountTypeTermOverDraft.getAccountTypeTerm().getEmiArray().entrySet();
					int i=1;
					for (Entry<Integer, Double> emiEntry : emiEntrySet) {
						if (i==1){
							emi1 = emiEntry.getValue();
							application.setAppLoanEmi1(emi1);
							application.setAppLoanEmi1Duration(emiEntry.getKey());
							i++;
						}else{
							emi2 = emiEntry.getValue();
							application.setAppLoanEmi2(emi2);
							application.setAppLoanEmi2Duration(emiEntry.getKey());
							break;
						}

					}

				}
				if(accountTypeTermOverDraft.getAccountTypeTerm()!=null && accountTypeTermOverDraft.getAccountTypeTerm().getDiscountedEmiArray()!=null){
					Set<Map.Entry<Integer,Double>> discountedEmiArray= accountTypeTermOverDraft.getAccountTypeTerm().getDiscountedEmiArray().entrySet();
					int i=1;
					for (Entry<Integer, Double> discountedEmiEntry : discountedEmiArray) {
						if (i==1){
							discountedEmi1 = discountedEmiEntry.getValue();
							application.setAppLoanEmiDiscount1(discountedEmi1);
							application.setAppLoanEmi1Duration(discountedEmiEntry.getKey());
							i++;
						}else{
							discountedEmi2 = discountedEmiEntry.getValue();
							application.setAppLoanEmiDiscount2(discountedEmi2);
							application.setAppLoanEmi2Duration(discountedEmiEntry.getKey());
							break;
						}

					}
				}
			}
		}
		application.setAppSchemePmay(null);
		quote.setLoanQuoteEligiblePmay(0);

		MasterBranch branch = commonService.getBranchById(quote.getLoanQuotePrferredBranchId());

			if(branch!=null && branch.getBranchIsType2()!=4 ){
			if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanCategoryId()!=null 
				&& quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteGender()!=null 
				&& quote.getLoanQuotePuccaHouse()!=null){
		
				boolean	residentTypePmayEligible= quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1;
				boolean	residentTypePmayEligible1=quote.getLoanQuoteCoapplicantFirstResidentTypeId()==null || quote.getLoanQuoteCoapplicantFirstResidentTypeId()==1;
				boolean	residentTypePmayEligible2=quote.getLoanQuoteCoapplicantSecondResidentTypeId()==null || quote.getLoanQuoteCoapplicantSecondResidentTypeId()==1;
	
				boolean pmayEligibleGender=("F".equalsIgnoreCase(quote.getLoanQuoteGender())
					|| "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantFirstGender()) 
					|| "F".equalsIgnoreCase(quote.getLoanQuoteCoapplicantSecondGender()));
			
				boolean pucccaHouseNo="2".equalsIgnoreCase(quote.getLoanQuotePuccaHouse());
			
				double netMonthlyIncome = 0.0;
				double netMonthlyIncomeCoapp = 0.0;
				double netMonthlyIncomeCoapp2 = 0.0;
				if (quote.getLoanQuoteEmploymentTypeId() == 1) {
					netMonthlyIncome = ((quote.getLoanQuoteNetMonthlySalary()!=null?quote.getLoanQuoteNetMonthlySalary():0)
							+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)
							+(quote.getLoanQuoteVariableMonthPay()!=null?quote.getLoanQuoteVariableMonthPay():0)
							+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0))*12;
					
				} else if (quote.getLoanQuoteEmploymentTypeId() == 2 || quote.getLoanQuoteEmploymentTypeId() == 3) {
					netMonthlyIncome = (quote.getLoanQuoteProfitAfterTax()!=null?quote.getLoanQuoteProfitAfterTax():0)
							+(quote.getLoanQuoteDepreciation()!=null?quote.getLoanQuoteDepreciation():0)
							+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome()*12:0);
				} else if (quote.getLoanQuoteEmploymentTypeId() == 4) {
					netMonthlyIncome = (quote.getLoanQuoteNetAnnualIncome() !=null?quote.getLoanQuoteNetAnnualIncome():0)
							+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome()*12:0)
							+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome()*12:0);
				} else if (quote.getLoanQuoteEmploymentTypeId() == 5) {
					netMonthlyIncome = ((quote.getLoanQuoteNetMonthlyPension() !=null?quote.getLoanQuoteNetMonthlyPension():0)
							+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)
							+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0))*12;
				} else if (quote.getLoanQuoteEmploymentTypeId() == 6 || quote.getLoanQuoteEmploymentTypeId() == 7 ) {
					netMonthlyIncome =  (quote.getLoanQuoteIncomeFromRegularSource()!=null?quote.getLoanQuoteIncomeFromRegularSource()*12:0);
				}

				if(quote.getLoanQuoteCoapplicantFirstCoEmplyerName()!=null){
					quote.setLoanQuoteCoapplicantFirstCoEmpId(commonService.getAllEmployerIdByName(quote.getLoanQuoteCoapplicantFirstCoEmplyerName()));
				}
				if(quote.getLoanQuoteCoapplicantSecondCoEmplyerName()!=null){
					quote.setLoanQuoteCoapplicantSecondCoEmplyerId(commonService.getAllEmployerIdByName(quote.getLoanQuoteCoapplicantSecondCoEmplyerName()));
				}
				if (quote.getLoanQuoteCoapplicantFirstRelationshipId()!= null) {
					quote.setLoanQuoteCoapplicantFirstId(1);
					if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() == 1){
						netMonthlyIncomeCoapp= 
								((quote.getLoanQuoteCoapplicantFirstMonthlySalary()!=null?quote.getLoanQuoteCoapplicantFirstMonthlySalary():0)
								+(quote.getLoanQuoteCoapplicantFirstVariableMonthPayon()!=null?quote.getLoanQuoteCoapplicantFirstVariableMonthPayon():0)
								+(quote.getLoanQuoteCoapplicantFirstOtherIncome()!=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0))*12;
					}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==2||quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==3){
						netMonthlyIncomeCoapp= 
								(quote.getLoanQuoteCoapplicantFirstProfitAfterTax() !=null?quote.getLoanQuoteCoapplicantFirstProfitAfterTax():0)
								+(quote.getLoanQuoteCoapplicantFirstDepreciatiation() !=null?quote.getLoanQuoteCoapplicantFirstDepreciatiation():0);
					}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==4){
						netMonthlyIncomeCoapp = 
								(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome() !=null?quote.getLoanQuoteCoapplicantFirstNetAnnualIncome():0)
								+(quote.getLoanQuoteCoapplicantFirstOtherIncome() !=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0)*12;
					}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==5){
						netMonthlyIncomeCoapp = 
								((quote.getLoanQuoteCoapplicantFirstNetMonthlyPension() !=null?quote.getLoanQuoteCoapplicantFirstNetMonthlyPension():0)
								+(quote.getLoanQuoteCoapplicantFirstOtherIncome() !=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0))*12;
					}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==7){
						netMonthlyIncomeCoapp = (quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource() !=null?quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource():0)*12; 
					}
				}
				if (quote.getLoanQuoteCoapplicantSecondRelationshipId() != null) {
					quote.setLoanQuoteCoapplicantSecondId(1);
					if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() == 1){
						netMonthlyIncomeCoapp2= 
								((quote.getLoanQuoteCoapplicantSecondMonthlySalary()!=null?quote.getLoanQuoteCoapplicantSecondMonthlySalary():0)
								+(quote.getLoanQuoteCoapplicantSecondVariableMonthPayon()!=null?quote.getLoanQuoteCoapplicantSecondVariableMonthPayon():0)
								+(quote.getLoanQuoteCoapplicantSecondOtherIncome()!=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0))*12;
					}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==2||quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==3){
						netMonthlyIncomeCoapp2= 
								(quote.getLoanQuoteCoapplicantSecondProfitAfterTax() !=null?quote.getLoanQuoteCoapplicantSecondProfitAfterTax():0)
								+(quote.getLoanQuoteCoapplicantSecondDepreciatiation() !=null?quote.getLoanQuoteCoapplicantSecondDepreciatiation():0);
					}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==4){
						netMonthlyIncomeCoapp2 = 
								(quote.getLoanQuoteCoapplicantSecondNetAnnualIncome() !=null?quote.getLoanQuoteCoapplicantSecondNetAnnualIncome():0)
								+(quote.getLoanQuoteCoapplicantSecondOtherIncome() !=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0)*12;
					}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==5){
						netMonthlyIncomeCoapp2 = 
								((quote.getLoanQuoteCoapplicantSecondNetMonthlyPension() !=null?quote.getLoanQuoteCoapplicantSecondNetMonthlyPension():0)
								+(quote.getLoanQuoteCoapplicantSecondOtherIncome() !=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0))*12;
					}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==7){
						netMonthlyIncomeCoapp2 = (quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource() !=null?quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource():0)*12; 
					}
				}
			
			quote.setLoanQuoteNetAnnualIncomeOfApplicant(netMonthlyIncome+netMonthlyIncomeCoapp+netMonthlyIncomeCoapp2);
				if(quote.getLoanQuoteLoanPurposeId()==1 ){
					if (quote.getLoanQuoteLoanCategoryId()==1 || quote.getLoanQuoteLoanCategoryId()==2){
						if((residentTypePmayEligible & residentTypePmayEligible2 & residentTypePmayEligible1) && pucccaHouseNo && pmayEligibleGender){
							if(quote.getLoanQuoteNetAnnualIncomeOfApplicant()!=null){
								
								if( quote.getLoanQuoteNetAnnualIncomeOfApplicant() <= 300000.0){
									quote.setLoanQuoteEligiblePmay(2);
									if( loanScenarioBean.getChosenTenure()<=20){
										application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME1));
									}
								}
								
									if( quote.getLoanQuoteNetAnnualIncomeOfApplicant() > 300000.0 && quote.getLoanQuoteNetAnnualIncomeOfApplicant() <= 600000.0){
										quote.setLoanQuoteEligiblePmay(2);
										if( loanScenarioBean.getChosenTenure()<=20){
											application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME2));
										}
									}
									
								
							}
						}
					}
					if (quote.getLoanQuoteLoanCategoryId()==4){
						if((residentTypePmayEligible & residentTypePmayEligible2 & residentTypePmayEligible1) && pucccaHouseNo){
							if( quote.getLoanQuoteNetAnnualIncomeOfApplicant()  <= 300000.0){
								quote.setLoanQuoteEligiblePmay(1);
								if( loanScenarioBean.getChosenTenure()<=20){
									application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME1));
								}
								
							}
						}
						
						if((residentTypePmayEligible & residentTypePmayEligible2 & residentTypePmayEligible1) && pucccaHouseNo){
							if( (quote.getLoanQuoteNetAnnualIncomeOfApplicant() > 300000.0 && quote.getLoanQuoteNetAnnualIncomeOfApplicant() <= 600000.0)){
								quote.setLoanQuoteEligiblePmay(1);
								if(loanScenarioBean.getChosenTenure()<=20){
									application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME2));
								}
								
							}
						}
						
					}
					if (quote.getLoanQuoteLoanCategoryId()==1 || quote.getLoanQuoteLoanCategoryId()==2 || quote.getLoanQuoteLoanCategoryId()==4){
						if(residentTypePmayEligible & residentTypePmayEligible2 & residentTypePmayEligible1 && pucccaHouseNo){
							if( (quote.getLoanQuoteNetAnnualIncomeOfApplicant() > 600000.0  && quote.getLoanQuoteNetAnnualIncomeOfApplicant() <= 1200000.0)){
								quote.setLoanQuoteEligiblePmay(1);
								if(loanScenarioBean.getChosenTenure()<=20){
								application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME3));
								
							}
						}
					}
						if(residentTypePmayEligible & residentTypePmayEligible2 & residentTypePmayEligible1 && pucccaHouseNo){
							if((quote.getLoanQuoteNetAnnualIncomeOfApplicant() > 1200000.0 && quote.getLoanQuoteNetAnnualIncomeOfApplicant() <= 1800000.0)){
								quote.setLoanQuoteEligiblePmay(1);
								if( loanScenarioBean.getChosenTenure()<=20){
									application.setAppSchemePmay(Integer.parseInt(Constants.PMAY_SCHEME4));
								}
								
							}
						}
					}
				}		
			}	
		}
		application = homeLoanService.save(application);


		loanScenarioBean.setApplicationHL(application);
		return loanScenarioBean;
	}
	public ApplicationFormHomeLoanQuote getUpdateQuote(ApplicationFormHomeLoanQuote quote) throws NullPointerException, RuntimeException, ParseException, SQLException {
		double reqloanamt = 0.0;
		double netMonthlyIncome = 0.0;
		double netMonthlyIncomeCoapp = 0.0;
		double netMonthlyIncomeCoapp2 = 0.0;
		if (quote.getLoanQuoteLoanPurposeId() != null) {
			if (quote.getLoanQuoteLoanPurposeId() == 1 || quote.getLoanQuoteLoanPurposeId() == 3) {
				if(quote.getLoanQuoteLoanCategoryId()!=null){
					if (quote.getLoanQuoteLoanCategoryId() == 1) {
						reqloanamt = (quote.getLoanQuoteCostHomeFlat()!=null?quote.getLoanQuoteCostHomeFlat():0)
								+(quote.getLoanQuoteCostOfConstruction()!=null?quote.getLoanQuoteCostOfConstruction():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 2) {
						reqloanamt = (quote.getLoanQuoteCostHomeFlat()!=null?quote.getLoanQuoteCostHomeFlat():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 3) {
						reqloanamt = (quote.getLoanQuoteLandCost()!=null?quote.getLoanQuoteLandCost():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 4) {
						reqloanamt = (quote.getLoanQuoteLandCost()!=null?quote.getLoanQuoteLandCost():0) 
								+ (quote.getLoanQuoteCostOfConstruction()!=null?quote.getLoanQuoteCostOfConstruction():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 5) {
						reqloanamt = (quote.getLoanQuoteCostOfConstruction()!=null?quote.getLoanQuoteCostOfConstruction():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 6) {
						reqloanamt = (quote.getLoanQuoteCurrentValueOfProperty()!=null?quote.getLoanQuoteCurrentValueOfProperty():0);
					} else if (quote.getLoanQuoteLoanCategoryId() == 7) {
						reqloanamt = (quote.getLoanQuoteOutstandingLoanAmount()!=null?quote.getLoanQuoteOutstandingLoanAmount():0)
								+(quote.getLoanQuoteTotalTopupAmountTaken()!=null?quote.getLoanQuoteTotalTopupAmountTaken():0);
					}
				}
				
			} else if (quote.getLoanQuoteLoanPurposeId() == 2) {
				reqloanamt = quote.getLoanQuoteRenovationCost()==null?0:quote.getLoanQuoteRenovationCost();
			} else if (quote.getLoanQuoteLoanPurposeId() == 4) {

			} else if (quote.getLoanQuoteLoanPurposeId() == 5) {
				reqloanamt = (quote.getLoanQuotePresentValueOfProperty()!=null?quote.getLoanQuotePresentValueOfProperty():0);
				String stdate = "";
				if (quote.getLoanQuoteMonthExistingHomeLoanStartDate() != null && quote.getLoanQuoteYearExistingHomeLoanStartDate() != null) {
					stdate = "01-"+ quote.getLoanQuoteMonthExistingHomeLoanStartDate() + "-"+ quote.getLoanQuoteYearExistingHomeLoanStartDate();
					quote.setLoanQuoteExistingHomeLoanStartDate(DateUtil.convertStringToDate(stdate));
				} else {

				}
				if (quote.getLoanQuoteYearExistingHomeLoanEndDate() != null && quote.getLoanQuoteMonthExistingHomeLoanEndDate() != null) {
					stdate = "01-"+ quote.getLoanQuoteMonthExistingHomeLoanEndDate()+ "-"+ quote.getLoanQuoteYearExistingHomeLoanEndDate();
					quote.setLoanQuoteExistingHomeLoanEndDate(DateUtil.convertStringToDate(stdate));
				}
			}else if (quote.getLoanQuoteLoanPurposeId() == 25) {
				Double calLoanQuoteCostHomeFlat = 0.0;
				Double caloanQuotePropertyMarketValue = 0.0;
			
				if (quote.getLoanQuoteCostHomeFlat()!=null) {
					calLoanQuoteCostHomeFlat = (quote.getLoanQuoteCostHomeFlat()*90)/100;
				}
				
				if (quote.getLoanQuotePropertyMarketValue()!=null && quote.getLoanQuotePropertyMarketValue()>=10000000) {
					caloanQuotePropertyMarketValue = (quote.getLoanQuotePropertyMarketValue()*60)/100;
				}else{
					caloanQuotePropertyMarketValue = (quote.getLoanQuotePropertyMarketValue()*65)/100;
				}
			  
				if (calLoanQuoteCostHomeFlat<caloanQuotePropertyMarketValue) {
					reqloanamt = (calLoanQuoteCostHomeFlat);
				}else{
					reqloanamt = (caloanQuotePropertyMarketValue);
				}
			}
			
			quote.setLoanQuoteProjectCost(reqloanamt);
			
			if(quote.getLoanQuoteBuilderName()!=null){

				quote.setLoanQuoteBuilderId(homeLoanService.getProjectIdByProjectName(quote.getLoanQuoteBuilderName()));
			}
			if (quote.getLoanQuoteLoanCategoryId() != null) {
				if (quote.getLoanQuoteLoanCategoryId() == 2 || quote.getLoanQuoteLoanCategoryId() == 4) {
					String stdate = "";
					if (quote.getLoanQuoteMonthCompletionDate() != null && quote.getLoanQuoteYearCompletionDate() != null) {
						logger.info("quote.getLoanQuoteMonthCompletionDate()::"  + quote.getLoanQuoteMonthCompletionDate());
						logger.info("quote.getLoanQuoteYearCompletionDate()::"  + quote.getLoanQuoteYearCompletionDate());
						String month=null;
						month = quote.getLoanQuoteMonthCompletionDate()<10 ? "0"+quote.getLoanQuoteMonthCompletionDate() : ""+quote.getLoanQuoteMonthCompletionDate();
						logger.info("month::"+month);
						stdate = "01-"+ month + "-"+ quote.getLoanQuoteYearCompletionDate();
						logger.info("stdate::"+stdate);
						//New Changes added:-Tech Mahindra:::::::::::::::::Start
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
						LocalDate endDay = Year.of(LocalDate.parse(stdate, formatter).getYear()).atMonth(LocalDate.parse(stdate, formatter).getMonth()).atEndOfMonth(); 
						logger.info("endDay::"+endDay);
						String formattedDate = endDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
						logger.info("formattedDate::"+formattedDate);
						Date newdate=DateUtil.convertStringToDateandTime(formattedDate+" 23:59:00");
						logger.info("newdate::"+newdate);
						String NewValue=DateUtil.getDateInISO8601Format(newdate);
						logger.info("NewValue::"+NewValue);
						//quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(stdate));
						//quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(NewValue));
						quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(formattedDate));
						logger.info("New Loan Quote Completion Date For Loan Category ID 2 & 4 :::::::::::::::"+quote.getLoanQuoteCompletionDate());
						//New Changes added:-Tech Mahindra:::::::::::::::::End
					}
				} else if (quote.getLoanQuoteLoanCategoryId() == 6) {
					String stdate = "";
					if (quote.getLoanQuoteMonthExistingHomeLoanStartDate() != null && quote.getLoanQuoteYearExistingHomeLoanStartDate() != null) {
						stdate = "01-"+ quote.getLoanQuoteMonthExistingHomeLoanStartDate()+ "-"+ quote.getLoanQuoteYearExistingHomeLoanStartDate();
						quote.setLoanQuoteExistingHomeLoanStartDate(DateUtil.convertStringToDate(stdate));
					}
					if (quote.getLoanQuotePropertyType()!=null && (quote.getLoanQuotePropertyType() == 2 || quote.getLoanQuotePropertyType() == 4) ) {
						stdate = "";
						if (quote.getLoanQuoteMonthCompletionDate() != null && quote.getLoanQuoteYearCompletionDate() != null) {
							String month=null;
							month = quote.getLoanQuoteMonthCompletionDate()<10 ? "0"+quote.getLoanQuoteMonthCompletionDate() : ""+quote.getLoanQuoteMonthCompletionDate();
							stdate = "01-"+ month + "-"+ quote.getLoanQuoteYearCompletionDate();
							//stdate = "01-"+ quote.getLoanQuoteMonthCompletionDate()+ "-"+ quote.getLoanQuoteYearCompletionDate();
							//New Changes added:-Tech Mahindra:::::::::::::::::Start
							//logger.info("OLD Loan Quote Completion Date For Loan Property Type ID 2 & 4 ::::::::Loan Category ID 6:::::::"+quote.getLoanQuoteCompletionDate());
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
							LocalDate endDay = Year.of(LocalDate.parse(stdate, formatter).getYear()).atMonth(LocalDate.parse(stdate, formatter).getMonth()).atEndOfMonth(); 
							String formattedDate = endDay.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
							//Date newdate=DateUtil.convertStringToDateandTime(formattedDate+" 23:59:00");
							//String NewValue=DateUtil.getDateInISO8601Format(newdate);
							//quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(stdate));
							//quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(NewValue));
							quote.setLoanQuoteCompletionDate(DateUtil.convertStringToDate(formattedDate));
							//logger.info("New Loan Quote Completion Date For Loan Property Type 2 & 4 :::::::Loan Category ID 6::::::::"+quote.getLoanQuoteCompletionDate());
							//New Changes added:-Tech Mahindra:::::::::::::::::End
						}
					}
				} else if (quote.getLoanQuoteLoanCategoryId() == 7) {
					String stdate = "";
					if (quote.getLoanQuoteMonthExistingHomeLoanStartDate() != null && quote.getLoanQuoteYearExistingHomeLoanStartDate() != null) {
						stdate = "01-"+ quote.getLoanQuoteMonthExistingHomeLoanStartDate()+ "-"+ quote.getLoanQuoteYearExistingHomeLoanStartDate();
						quote.setLoanQuoteExistingHomeLoanStartDate(DateUtil.convertStringToDate(stdate));
					}
					if (quote.getLoanQuoteEmiStartDateOfExistingTopupLoanYear() != null && quote.getLoanQuoteEmiStartDateOfExistingTopupLoanMonth() != null ) {
						stdate = "01-"+ quote.getLoanQuoteEmiStartDateOfExistingTopupLoanMonth()+ "-"+ quote.getLoanQuoteEmiStartDateOfExistingTopupLoanYear();
						quote.setLoanQuoteEmiStartDateOfExistingTopupLoan(DateUtil.convertStringToDate(stdate));
					}
				}
			}
			
			if(quote.getLoanQuoteLoanPurposeId()==2){
				quote.setLoanQuoteLoanCategoryId(11);
			}else if(quote.getLoanQuoteLoanPurposeId()==4){
				quote.setLoanQuoteLoanCategoryId(12);
			}else if(quote.getLoanQuoteLoanPurposeId()==5){
				quote.setLoanQuoteLoanCategoryId(13);
			}else if(quote.getLoanQuoteLoanPurposeId()==25){
				quote.setLoanQuoteLoanCategoryId(14);
			}
			
			if(quote.getLoanQuoteEmployerName()!=null){
				quote.setLoanQuoteEmployerCompanyId(commonService.getAllEmployerIdByName(quote.getLoanQuoteEmployerName()));
			}
			
			if (quote.getLoanQuoteEmploymentTypeId() == 1) {
				netMonthlyIncome = ((quote.getLoanQuoteNetMonthlySalary()!=null?quote.getLoanQuoteNetMonthlySalary():0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)
						+(quote.getLoanQuoteVariableMonthPay()!=null?quote.getLoanQuoteVariableMonthPay():0)
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0))*12;
				
			} else if (quote.getLoanQuoteEmploymentTypeId() == 2 || quote.getLoanQuoteEmploymentTypeId() == 3) {
				netMonthlyIncome = (quote.getLoanQuoteProfitAfterTax()!=null?quote.getLoanQuoteProfitAfterTax():0)
						+(quote.getLoanQuoteDepreciation()!=null?quote.getLoanQuoteDepreciation():0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome()*12:0);
			} else if (quote.getLoanQuoteEmploymentTypeId() == 4) {
				netMonthlyIncome = (quote.getLoanQuoteNetAnnualIncome() !=null?quote.getLoanQuoteNetAnnualIncome():0)
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome()*12:0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome()*12:0);
			} else if (quote.getLoanQuoteEmploymentTypeId() == 5) {
				netMonthlyIncome = ((quote.getLoanQuoteNetMonthlyPension() !=null?quote.getLoanQuoteNetMonthlyPension():0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0))*12;
			} else if (quote.getLoanQuoteEmploymentTypeId() == 6 || quote.getLoanQuoteEmploymentTypeId() == 7 ) {
				netMonthlyIncome =  (quote.getLoanQuoteIncomeFromRegularSource()!=null?quote.getLoanQuoteIncomeFromRegularSource()*12:0);
			}

			if(quote.getLoanQuoteCoapplicantFirstCoEmplyerName()!=null){
				quote.setLoanQuoteCoapplicantFirstCoEmpId(commonService.getAllEmployerIdByName(quote.getLoanQuoteCoapplicantFirstCoEmplyerName()));
			}
			if(quote.getLoanQuoteCoapplicantSecondCoEmplyerName()!=null){
				quote.setLoanQuoteCoapplicantSecondCoEmplyerId(commonService.getAllEmployerIdByName(quote.getLoanQuoteCoapplicantSecondCoEmplyerName()));
			}
			if (quote.getLoanQuoteCoapplicantFirstRelationshipId()!= null) {
				quote.setLoanQuoteCoapplicantFirstId(1);
				if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() == 1){
					netMonthlyIncomeCoapp= 
							((quote.getLoanQuoteCoapplicantFirstMonthlySalary()!=null?quote.getLoanQuoteCoapplicantFirstMonthlySalary():0)
							+(quote.getLoanQuoteCoapplicantFirstVariableMonthPayon()!=null?quote.getLoanQuoteCoapplicantFirstVariableMonthPayon():0)
							+(quote.getLoanQuoteCoapplicantFirstOtherIncome()!=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0))*12;
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==2||quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==3){
					netMonthlyIncomeCoapp= 
							(quote.getLoanQuoteCoapplicantFirstProfitAfterTax() !=null?quote.getLoanQuoteCoapplicantFirstProfitAfterTax():0)
							+(quote.getLoanQuoteCoapplicantFirstDepreciatiation() !=null?quote.getLoanQuoteCoapplicantFirstDepreciatiation():0);
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==4){
					netMonthlyIncomeCoapp = 
							(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome() !=null?quote.getLoanQuoteCoapplicantFirstNetAnnualIncome():0)
							+(quote.getLoanQuoteCoapplicantFirstOtherIncome() !=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0)*12;
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==5){
					netMonthlyIncomeCoapp = 
							((quote.getLoanQuoteCoapplicantFirstNetMonthlyPension() !=null?quote.getLoanQuoteCoapplicantFirstNetMonthlyPension():0)
							+(quote.getLoanQuoteCoapplicantFirstOtherIncome() !=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0))*12;
				}else if(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantFirstEmploymentTypeId()==7){
					netMonthlyIncomeCoapp = (quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource() !=null?quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource():0)*12; 
				}
			}
			if (quote.getLoanQuoteCoapplicantSecondRelationshipId() != null) {
				quote.setLoanQuoteCoapplicantSecondId(1);
				if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() == 1){
					netMonthlyIncomeCoapp2= 
							((quote.getLoanQuoteCoapplicantSecondMonthlySalary()!=null?quote.getLoanQuoteCoapplicantSecondMonthlySalary():0)
							+(quote.getLoanQuoteCoapplicantSecondVariableMonthPayon()!=null?quote.getLoanQuoteCoapplicantSecondVariableMonthPayon():0)
							+(quote.getLoanQuoteCoapplicantSecondOtherIncome()!=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0))*12;
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==2||quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==3){
					netMonthlyIncomeCoapp2= 
							(quote.getLoanQuoteCoapplicantSecondProfitAfterTax() !=null?quote.getLoanQuoteCoapplicantSecondProfitAfterTax():0)
							+(quote.getLoanQuoteCoapplicantSecondDepreciatiation() !=null?quote.getLoanQuoteCoapplicantSecondDepreciatiation():0);
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==4){
					netMonthlyIncomeCoapp2 = 
							(quote.getLoanQuoteCoapplicantSecondNetAnnualIncome() !=null?quote.getLoanQuoteCoapplicantSecondNetAnnualIncome():0)
							+(quote.getLoanQuoteCoapplicantSecondOtherIncome() !=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0)*12;
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==5){
					netMonthlyIncomeCoapp2 = 
							((quote.getLoanQuoteCoapplicantSecondNetMonthlyPension() !=null?quote.getLoanQuoteCoapplicantSecondNetMonthlyPension():0)
							+(quote.getLoanQuoteCoapplicantSecondOtherIncome() !=null?quote.getLoanQuoteCoapplicantSecondOtherIncome():0))*12;
				}else if(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==6 ||quote.getLoanQuoteCoapplicantSecondEmploymentTypeId()==7){
					netMonthlyIncomeCoapp2 = (quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource() !=null?quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource():0)*12; 
				}
			}
		}
		quote.setLoanQuoteNetAnnualIncomeOfApplicant(netMonthlyIncome+netMonthlyIncomeCoapp+netMonthlyIncomeCoapp2);
		return quote;
	}
	
}
