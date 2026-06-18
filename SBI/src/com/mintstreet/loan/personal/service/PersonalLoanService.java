package com.mintstreet.loan.personal.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.LoanPurposeDao;
import com.mintstreet.common.dao.MasterCBSCallDao;
import com.mintstreet.common.dao.MasterCBSResponseDao;
import com.mintstreet.common.dao.MasterInstituteDao;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.loan.cveloan.dao.ApplicationFormCveLoanDao;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.personal.dao.ApplicationFormPersonalLoanCallsDao;
import com.mintstreet.loan.personal.dao.ApplicationFormPersonalLoanDao;
import com.mintstreet.loan.personal.dao.ApplicationFormPersonalLoanQuoteDao;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanCalls;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.product.dao.PlProductDao;
import com.mintstreet.loan.product.entity.MasterPlProduct;

public class PersonalLoanService{
	private static final Logger logger = LogManager.getLogger(PersonalLoanService.class.getName());
	private ApplicationFormPersonalLoanDao applicationFormPersonalLoanDao;
	private ApplicationFormPersonalLoanQuoteDao applicationFormPersonalLoanQuoteDao;
	private ApplicationFormPersonalLoanCallsDao applicationFormPersonalLoanCallsDao;
	private MasterInstituteDao masterInstituteDao;
	private PlProductDao plProductDao;
	private LoanPurposeDao loanPurposeDao;
	private MasterCBSResponseDao masterCBSResponseDao;
	private MasterCBSCallDao masterCBSCallDao;
	
	//Added by Pratima for CVE
	private ApplicationFormCveLoanDao applicationFormCveLoanDao;

	public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormPersonalLoanDao.isAppFoundForDedupInDropOffStage(isdCode, appMobileNo, loanPurposeId);
	}
	
	public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormPersonalLoanDao.isAppFoundForDedupInApplicationStage(appReferenceId, isdCode, appMobileNo, loanPurposeId);
	}
	
	public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormPersonalLoanDao.isAppFoundForDedupInDropRejectStage(isdCode, appMobileNo, loanPurposeId);
	}
	
	
	
	public ApplicationFormPersonalLoan convert(ApplicationFormPersonalLoan application){
		try{
			if(application.getAppDobDT()!=null){
	 			application.setAppDob(DateUtil.convertDateToFormattedType(application.getAppDobDT(), "dd-MM-yyyy"));
			}
	 		if(application.getAppDocPickupDateDT()!=null){
	 			application.setAppDocPickupDate(DateUtil.convertDateToFormattedType(application.getAppDocPickupDateDT(), "dd-MM-yyyy"));
			}
			if(application.getAppCompanyJoiningDate()!=null){
				Date date =  null;
				date = application.getAppCompanyJoiningDate();
				if(date != null){
					application.setAppCompanyJoiningMonth(DateUtil.getMonthFromDate(date));
					application.setAppCompanyJoiningYear(DateUtil.getYearFromDate(date));
				}
			}
			if(application.getAppDocPickupTime()!=null){
				application.setAppDocPickupTimeString(DateUtil.getStringDateFromDate(application.getAppDocPickupTime(), "HH:mm"));
			}
		} catch(NullPointerException e){
			logger.info("PersonalLoanService.java LNo : 60 : Exception Caught",e);
		} catch(Exception e){
			logger.info("PersonalLoanService.java LNo : 60 : Exception Caught",e);
		}
		return application;
	}
	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo)throws SQLException {
		ApplicationFormPersonalLoan application = applicationFormPersonalLoanDao.getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo(appReferenceId, appMobileNo);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}
	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByAppReferenceId(String appReferenceId)throws SQLException {
 		ApplicationFormPersonalLoan application = applicationFormPersonalLoanDao.getApplicationFormPersonalLoanByAppReferenceId(appReferenceId);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}
	
	public ApplicationFormPersonalLoan save(ApplicationFormPersonalLoan applicationFormPersonalLoan) {
		applicationFormPersonalLoan.setAppLeadUpdateTime(new Date());
		return applicationFormPersonalLoanDao.save(applicationFormPersonalLoan.getAppSeqId(), applicationFormPersonalLoan);
	}
	
	public ApplicationFormPersonalLoanQuote save(ApplicationFormPersonalLoanQuote applicationFormPersonalLoanQuote) throws SQLException {
		return applicationFormPersonalLoanQuoteDao.save(applicationFormPersonalLoanQuote.getLoanQuoteId(),applicationFormPersonalLoanQuote);
	}
	
	public ApplicationFormPersonalLoanCalls save(ApplicationFormPersonalLoanCalls  callsLog)throws SQLException{
		return applicationFormPersonalLoanCallsDao.save(callsLog.getCallId(), callsLog);
	}

	public ApplicationFormPersonalLoanQuote getApplicationFormPersonalLoanQuoteByQuoteId(Integer loanQuoteId) throws SQLException, NoResultException {
		ApplicationFormPersonalLoanQuote quote = applicationFormPersonalLoanQuoteDao.findById(loanQuoteId, ApplicationFormPersonalLoanQuote.class);
		Date date =  null;
		if(quote.getLoanQuoteDateOfBirthDT()!=null){
			quote.setLoanQuoteAge(DateUtil.getAge(quote.getLoanQuoteDateOfBirthDT()));
			quote.setLoanQuoteDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteDateOfBirthDT(), "dd-MM-yyyy"));
		}
		
		if(quote.getLoanQuoteExistingPLSanctionDT()!=null){
			quote.setLoanQuoteExistingPLSanctionDate(DateUtil.convertDateToFormattedType(quote.getLoanQuoteExistingPLSanctionDT(), "dd-MM-yyyy"));
		}
		
		if(quote.getLoanQuoteCoapplicantDateOfBirthDT()!=null){
			quote.setLoanQuoteCoapplicantDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteCoapplicantDateOfBirthDT(), "dd-MM-yyyy"));
		}
		date = quote.getLoanQuoteSTDOfLease();
		if(date != null){
			quote.setLoanQuoteYearStartDateOflease(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthStartDateOflease(DateUtil.getMonthFromDate(date));
			quote.setLoanQuoteStartDateOfLease(quote.getLoanQuoteYearStartDateOflease()+"-"+(quote.getLoanQuoteMonthStartDateOflease()>9?quote.getLoanQuoteMonthStartDateOflease():"0"+quote.getLoanQuoteMonthStartDateOflease())+"-01");
		}
		date = quote.getLoanQuoteEDOfLease();
		if(date != null){
			quote.setLoanQuoteYearEndDateOfLease(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthEndDateOfLease(DateUtil.getMonthFromDate(date));
			quote.setLoanQuoteEndDateOfLease(quote.getLoanQuoteYearEndDateOfLease()+"-"+(quote.getLoanQuoteMonthEndDateOfLease()>9?quote.getLoanQuoteMonthEndDateOfLease():"0"+quote.getLoanQuoteMonthEndDateOfLease())+"-01");
		}
		
		date = quote.getLoanQuoteCompanyJoiningDate();
		if(date!=null) {
			quote.setLoanQuoteYearCompanyJoining(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthCompanyJoining(DateUtil.getMonthFromDate(date));
		}
   
	    date = quote.getLoanQuoteStartDateOfCurrentBusiness();
	    if(date!=null) {
	    	quote.setLoanQuoteYearStartDateOfCurrentBusiness(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthStartDateOfCurrentBusiness(DateUtil.getMonthFromDate(date));
		}
   
	    date = quote.getLoanQuoteStartDateOfCurrentProfession();
	    if(date!=null) {
	    	quote.setLoanQuoteYearStartDateOfCurrentProfession(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthStartDateOfCurrentProfession(DateUtil.getMonthFromDate(date));
		}
	    if(quote.getLoanQuoteCoapplicantEmploymentTypeId()!=null && quote.getLoanQuoteCoapplicantEmploymentTypeId()==15){
	    	date = quote.getLoanQuoteCoapplicantJoiningDate();
	    	if(date!=null) {
				quote.setLoanQuoteYearCoapplicantJoining(DateUtil.getYearFromDate(date));
				quote.setLoanQuoteMonthCoapplicantJoining(DateUtil.getMonthFromDate(date));
			}
    	}else if(quote.getLoanQuoteCoapplicantEmploymentTypeId()!=null && quote.getLoanQuoteCoapplicantEmploymentTypeId()==16){
			date = quote.getLoanQuoteCoapplicantStartDateOfCurrentProfession();
			if(date!=null) {
				quote.setLoanQuoteYearCoapplicantStartDateOfCurrentProfession(DateUtil.getYearFromDate(date));
				quote.setLoanQuoteMonthCoapplicantStartDateOfCurrentProfession(DateUtil.getMonthFromDate(date));
			}
		}else if(quote.getLoanQuoteCoapplicantEmploymentTypeId()!=null && quote.getLoanQuoteCoapplicantEmploymentTypeId()==17){
			date = quote.getLoanQuoteCoapplicantStartDateOfCurrentBusiness();
			if(date!=null) {
				quote.setLoanQuoteYearCoapplicantStartDateOfCurrentBusiness(DateUtil.getYearFromDate(date));
				quote.setLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness(DateUtil.getMonthFromDate(date));
			}
		}
	    if(quote.getLoanQuoteYearCompanyJoining()!=null && quote.getLoanQuoteMonthCompanyJoining()!=null){
			String dateInString = quote.getLoanQuoteYearCompanyJoining()+"-"+(quote.getLoanQuoteMonthCompanyJoining()>9?quote.getLoanQuoteMonthCompanyJoining():"0"+quote.getLoanQuoteMonthCompanyJoining())+"-01";
			Date dateFormat = DateUtil.changeDateFormatToDate(dateInString, "dd-MM-yyyy");
			quote.setLoanQuoteWorkExperience(DateUtil.getAge(dateFormat));
		}
	    if(quote.getLoanQuoteYearStartDateOfCurrentBusiness()!=null && quote.getLoanQuoteMonthStartDateOfCurrentBusiness()!=null){
			String dateInString = quote.getLoanQuoteYearStartDateOfCurrentBusiness()+"-"+(quote.getLoanQuoteMonthStartDateOfCurrentBusiness()>9?quote.getLoanQuoteMonthStartDateOfCurrentBusiness():"0"+quote.getLoanQuoteMonthStartDateOfCurrentBusiness())+"-01";
			Date dateFormat = DateUtil.changeDateFormatToDate(dateInString, "dd-MM-yyyy");
			quote.setLoanQuoteWorkExperience(DateUtil.getAge(dateFormat));
		}
	    if(Constants.BANK_ID == Constants.BANK_ID_SBT || Constants.BANK_ID == Constants.BANK_ID_SBI){
			if(quote.getLoanQuoteLoanAccountType()!=null){
				quote.setLoanQuoteCheckOffType(quote.getLoanQuoteLoanAccountType());
			}
		}
	    

	    if(quote.getLoanQuoteFirstName()!=null){
	    	quote.setLoanQuoteFirstName(quote.getLoanQuoteFirstName());
	    }
	    if(quote.getLoanQuoteMiddleName()!=null){
	    	quote.setLoanQuoteMiddleName(quote.getLoanQuoteMiddleName());
	    }
	    if(quote.getLoanQuoteLastName()!=null){
	    	quote.setLoanQuoteLastName(quote.getLoanQuoteLastName());
	    }
	    if(quote.getLoanQuoteGender()!=null){
	    	quote.setLoanQuoteGender(quote.getLoanQuoteGender());
	    }
	    if(quote.getLoanQuoteAddress1()!=null){
	    	quote.setLoanQuoteAddress1(quote.getLoanQuoteAddress1());
	    }
	    if(quote.getLoanQuoteAddress2()!=null){
	    	quote.setLoanQuoteAddress2(quote.getLoanQuoteAddress2());
	    }
	    if(quote.getLoanQuoteAddressLandmark()!=null){
	    	quote.setLoanQuoteAddressLandmark(quote.getLoanQuoteAddressLandmark());
	    }
	    if(quote.getLoanQuoteStateId()!=null){
	    	quote.setLoanQuoteStateId(quote.getLoanQuoteStateId());
	    }
	    if(quote.getLoanQuoteCityId()!=null){
	    	quote.setLoanQuoteCityId(quote.getLoanQuoteCityId());
	    }
	    if(quote.getLoanQuotePincode()!=null){
	    	quote.setLoanQuotePincode(quote.getLoanQuotePincode());
	    }
	    if(quote.getLoanQuotePanCardNo()!=null){
	    	quote.setLoanQuotePanCardNo(quote.getLoanQuotePanCardNo());
	    }
	   
	    if(quote.getLoanQuoteOtherId()!=null){
	    	quote.setLoanQuoteOtherId(quote.getLoanQuoteOtherId());
	    }
	    if(quote.getLoanQuoteOtherIdNumber()!=null){
	    	quote.setLoanQuoteOtherIdNumber(quote.getLoanQuoteOtherIdNumber());
	    }
	    if(quote.getLoanQuoteLoanAmount()!=null){
	    	quote.setLoanQuoteLoanAmount(quote.getLoanQuoteLoanAmount());
	    }
	   

	    if(quote.getLoanQuoteLoanPurposeId()!=null){
	    	quote.setLoanQuoteLoanPurposeId(quote.getLoanQuoteLoanPurposeId());
	    }
	     if(quote.getLoanQuotePensionType()!=null){
	    	 quote.setLoanQuotePensionType(quote.getLoanQuotePensionType());
	     }
	     if(quote.getLoanQuotePensionPayOrdNumber()!=null){
	    	 quote.setLoanQuotePensionPayOrdNumber(quote.getLoanQuotePensionPayOrdNumber());
	     }
	     if(quote.getLoanQuoteMonthPensionAmt()!=null){
	    	 quote.setLoanQuoteMonthPensionAmt(quote.getLoanQuoteMonthPensionAmt());
	     }
	     if(quote.getLoanQuotePreEMIsOther()!=null){
	    	 quote.setLoanQuotePreEMIsOther(quote.getLoanQuotePreEMIsOther());
	     }
	     
	     if(quote.getLoanQuoteIndustryType()!=null){
	    	 quote.setLoanQuoteIndustryType(quote.getLoanQuoteIndustryType());	    	 
	     }
	     if(quote.getLoanQuoteOrgName()!=null){
	    	 quote.setLoanQuoteOrgName(quote.getLoanQuoteOrgName());
	     }
	     if(quote.getLoanQuoteEmployerName()!=null){
	    	 quote.setLoanQuoteEmployerName(quote.getLoanQuoteEmployerName());
	     }
	     if(quote.getLoanQuotePensionPayingStateId()!=null){	    	 
	    	 quote.setLoanQuotePensionPayingStateId(quote.getLoanQuotePensionPayingStateId());
	    	 quote.setLoanQuoteStateId(quote.getLoanQuotePensionPayingStateId());
	     }
	     if(quote.getLoanQuotePensionPayingCityId()!=null){
	    	 quote.setLoanQuotePensionPayingCityId(quote.getLoanQuotePensionPayingCityId());
	    	 quote.setLoanQuoteCityId(quote.getLoanQuotePensionPayingCityId());
	     }
	     if(quote.getLoanQuotePensionPayingDistrId()!=null){
	    	 quote.setLoanQuotePensionPayingDistrId(quote.getLoanQuotePensionPayingDistrId());
	     }
	     if(quote.getLoanQuotePensionPayingBranchId()!=null){
	    	 quote.setLoanQuotePensionPayingBranchId(quote.getLoanQuotePensionPayingBranchId());
	     }	
	     if(quote.getLoanQuotePreferredLoanBranch()!=null){
	    	 quote.setLoanQuotePreferredLoanBranch(quote.getLoanQuotePreferredLoanBranch());	    	
	     }
	     if(quote.getLoanQuotePreferredStateId()!=null){
	    	 quote.setLoanQuotePreferredStateId(quote.getLoanQuotePreferredStateId());
	    	 quote.setLoanQuoteStateId(quote.getLoanQuotePensionPayingStateId());
	     }
	     if(quote.getLoanQuotePreferredCityId()!=null){
	    	 quote.setLoanQuotePreferredCityId(quote.getLoanQuotePreferredCityId());
	    	 quote.setLoanQuoteCityId(quote.getLoanQuotePensionPayingCityId());
	     }
	     if(quote.getLoanQuotePreferredDistrictId()!=null){
	    	 quote.setLoanQuotePreferredDistrictId(quote.getLoanQuotePreferredDistrictId());
	     }
	     if(quote.getLoanQuotePreferredLoanBranchId()!=null){
	    	 quote.setLoanQuotePreferredLoanBranchId(quote.getLoanQuotePreferredLoanBranchId());
	     }
	     if(quote.getAppMobile()!=null){
	    	 quote.setAppMobile(quote.getAppMobile());
	     }
	     if(quote.getAppEmail()!=null){
	    	 quote.setAppEmail(quote.getAppEmail());
	     }
	     if(quote.getLoanQuoteEmploymentType()!=null){
	    	 quote.setLoanQuoteEmploymentType(quote.getLoanQuoteEmploymentType());
	     }else{
	    	 quote.setLoanQuoteEmploymentType(15);
	     }
	     if(quote.getLoanQuoteEmployerName()!=null){
	    	 quote.setLoanQuoteEmployerName(quote.getLoanQuoteEmployerName());
	     }
	     if(quote.getLoanQuoteModeOfRepayment()!=null){
	    	 quote.setLoanQuoteModeOfRepayment(quote.getLoanQuoteModeOfRepayment());
	     }
	     if(quote.getLoanQuotePensionAccountNumber()!=null){
	    	 quote.setLoanQuotePensionAccountNumber(quote.getLoanQuotePensionAccountNumber());
	     }
	     if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==128){
	    	 if(quote.getLoanQuoteEmploymentType()!=null){ 
		    	 quote.setLoanQuoteEmploymentType(Constants.EMPLOYMENT_TYPE_PENSIONERS);
		     }
		     if(quote.getLoanQuoteEmploymentNature()!=null){ 
		    	 quote.setLoanQuoteEmploymentNature(Constants.EMPLOYMENT_TYPE_PENSIONERS);
		     }
	     }
	     if(quote.getLoanQuoteResidentTypeId()!=null){  
	    	 quote.setLoanQuoteResidentTypeId(quote.getLoanQuoteResidentTypeId());
	     }
	    return quote;
	}
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByAppSeqId(Integer appSeqId) throws SQLException{
		ApplicationFormPersonalLoan application = applicationFormPersonalLoanDao.findById(appSeqId, ApplicationFormPersonalLoan.class);
		if(application==null){
 			return null;
 		}
		application = convert(application);
		return application;
	}
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByUniqueRefNummber(String appCustomerUniqueId) {
 		ApplicationFormPersonalLoan application = applicationFormPersonalLoanDao.getApplicationFormPersonalLoanByUniqueRefNummber(appCustomerUniqueId);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByMobileAndSmsOtp(String appMobileNo,Integer appMobileVerificationCode) throws SQLException { 
		return applicationFormPersonalLoanDao.getApplicationFormPersonalLoanByMobileAndSmsOtp(appMobileNo,appMobileVerificationCode);
	}

	public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException{
		return applicationFormPersonalLoanDao.getLastGeneratedReferenceNumber(loanTypeId);
	}
	public boolean isReferenceIdAvailable(String appReferenceId) throws SQLException{
		return applicationFormPersonalLoanDao.isReferenceIdAvailable(appReferenceId);
	}
	
	public MasterInstitute getInstituteByInstituteName(String instituteName) {
		return masterInstituteDao.getInstituteByInstituteName(instituteName);
	}
	
	public String getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(Integer loanPurposeId,Integer loanTypeId) {
		return loanPurposeDao.getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(loanPurposeId, loanTypeId);
	}
	
	public MasterCBSResponse getMasterCBSResponseObjectByCbsAppSeqId(Integer appSeqId) throws SQLException, NoResultException{
		return masterCBSResponseDao.getMasterCBSResponseObjectByCbsAppSeqId(appSeqId);
	}

	public Integer getOldVisitId(Integer quoteId) throws SQLException{
		return applicationFormPersonalLoanQuoteDao.getOldVisitId(quoteId);
	}
	public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException {
		return applicationFormPersonalLoanDao.getVisitByAppSeqId(appSeqId);
	}
	
	public MasterCBSCall getMasterCBSCallObjectByCbsResponseId(Integer cbsResponseId) throws SQLException, NoResultException{
		return masterCBSCallDao.getMasterCBSCallObjectByCbsResponseId(cbsResponseId);
	}
	public Integer getInstantLoanCount(Integer appPersonalLoanId) throws SQLException {
		return applicationFormPersonalLoanDao.getInstantLoanCount(appPersonalLoanId);
	}
	public ApplicationFormPersonalLoanDao getApplicationFormPersonalLoanDao() {
		return applicationFormPersonalLoanDao;
	}

	public void setApplicationFormPersonalLoanDao(
			ApplicationFormPersonalLoanDao applicationFormPersonalLoanDao) {
		this.applicationFormPersonalLoanDao = applicationFormPersonalLoanDao;
	}

	public ApplicationFormPersonalLoanQuoteDao getApplicationFormPersonalLoanQuoteDao() {
		return applicationFormPersonalLoanQuoteDao;
	}

	public void setApplicationFormPersonalLoanQuoteDao(
			ApplicationFormPersonalLoanQuoteDao applicationFormPersonalLoanQuoteDao) {
		this.applicationFormPersonalLoanQuoteDao = applicationFormPersonalLoanQuoteDao;
	}

	public ApplicationFormPersonalLoanCallsDao getApplicationFormPersonalLoanCallsDao() {
		return applicationFormPersonalLoanCallsDao;
	}

	public void setApplicationFormPersonalLoanCallsDao(
			ApplicationFormPersonalLoanCallsDao applicationFormPersonalLoanCallsDao) {
		this.applicationFormPersonalLoanCallsDao = applicationFormPersonalLoanCallsDao;
	}

	public MasterInstituteDao getMasterInstituteDao() {
		return masterInstituteDao;
	}

	public void setMasterInstituteDao(MasterInstituteDao masterInstituteDao) {
		this.masterInstituteDao = masterInstituteDao;
	}

	public MasterPlProduct getPlProductById(Integer plProductId) {
		return plProductDao.getPlProductById(plProductId);
	}
	
	public List<MasterPlProduct> getAllPlProduct() throws SQLException{
		return plProductDao.getAllPlProduct();
	}
	public void setPlProductDao(PlProductDao plProductDao) {
		this.plProductDao = plProductDao;
	}

	public PlProductDao getPlProductDao() {
		return plProductDao;
	}
	public LoanPurposeDao getLoanPurposeDao() {
		return loanPurposeDao;
	}

	public void setLoanPurposeDao(LoanPurposeDao loanPurposeDao) {
		this.loanPurposeDao = loanPurposeDao;
	}

	public MasterCBSResponseDao getMasterCBSResponseDao() {
		return masterCBSResponseDao;
	}

	public void setMasterCBSResponseDao(MasterCBSResponseDao masterCBSResponseDao) {
		this.masterCBSResponseDao = masterCBSResponseDao;
	}

	public MasterCBSCallDao getMasterCBSCallDao() {
		return masterCBSCallDao;
	}

	public void setMasterCBSCallDao(MasterCBSCallDao masterCBSCallDao) {
		this.masterCBSCallDao = masterCBSCallDao;
	}
	
	
	// Working by Hakim for CVE -------------- >
	  /* public ApplicationFormCveLoan getApplicationFormCveLoanByRefId(String appReferenceId)throws Exception {
	
		logger.info("PersonalLoanService.java LNo : 101 : getApplicationFormCveLoanByRefId:::::"+appReferenceId);
		
		ApplicationFormCveLoan application = applicationFormCveLoanDao.getApplicationFormCveLoanByRefId(appReferenceId);
		logger.info("PersonalLoanService.java LNo : 104 : application.toString()"+application.toString());
		
		
 		//application = convert(application);
		return application;
	} */

	//Added for CVE
	public ApplicationFormCveLoan getApplicationFormCveLoanByAppSeqId(Integer appSeqId) {
	
		logger.info("INSIDE PersonalLoanService.java BEFORE getApplicationFormCveLoanByAppSeqId ( ) ");
		ApplicationFormCveLoan application = applicationFormCveLoanDao.findById(appSeqId, ApplicationFormCveLoan.class);
		logger.info("INSIDE PersonalLoanService.java AFTER getApplicationFormCveLoanByAppSeqId ( ) "+appSeqId);
		
		if(application==null){
 			return null;
 		}
		//application = convert(application);
		return application;
		}
	
	public String getLastGeneratedReferenceNumberCVE(Integer loanTypeId) throws SQLException{
		return applicationFormPersonalLoanDao.getLastGeneratedReferenceNumberCVE(loanTypeId);
		}
	public boolean isReferenceIdAvailableCVE(String appReferenceId) throws SQLException{
		return applicationFormPersonalLoanDao.isReferenceIdAvailableCVE(appReferenceId);
	}
	
	
	public ApplicationFormCveLoan getAppReferenceIdListFromCVEtable() throws SQLException {
		logger.info("INSIDE PersonalLoanService.java getAppReferenceIdListFromCVEtable ( ) ");
		return applicationFormPersonalLoanDao.getAppReferenceIdListFromCVEtable();
	} 
	
	public List<ApplicationFormCveLoan> updateCRMFlagCVEtable() throws SQLException {
		logger.info("INSIDE PersonalLoanService.java updateCRMFlagCVEtable ( ) ");
		return applicationFormPersonalLoanDao.updateCRMFlagCVEtable();
	}
		
		
	public ApplicationFormCveLoan save(ApplicationFormCveLoan applicationFormCveLoan) {
			ApplicationFormCveLoan loan = new ApplicationFormCveLoan();

				applicationFormCveLoan.setAppLeadUpdateTime(new Date());
				
				logger.info("ApplicationFormCveLoan>save>applicationFormCveLoan.getAppSeqId():::"+applicationFormCveLoan.getAppSeqId());
				loan = applicationFormCveLoanDao.save(applicationFormCveLoan.getAppSeqId(), applicationFormCveLoan);
				logger.info("ApplicationFormCveLoan>save>loan:::"+loan);
			
			return loan;
		}
		
		public ApplicationFormCveLoan getApplicationFormCveByAppSeqId(Integer appSeqId) throws SQLException {
			logger.info("INSIDE PersonalLoanService.java BEFORE getApplicationFormCveByAppSeqId ( ) ");
			ApplicationFormCveLoan application = applicationFormCveLoanDao.findById(appSeqId, ApplicationFormCveLoan.class);
			logger.info("INSIDE PersonalLoanService.java AFTER getApplicationFormCveByAppSeqId ( ) ");
			if(application==null){
	 			return null;
	 		}
			//application = convert(application);
			logger.info("INSIDE PersonalLoanService.java application ( ) "+application.toString());
			return application;
		}	
		
		public ApplicationFormCveLoanDao getApplicationFormCveLoanDao() {
			return applicationFormCveLoanDao;
		}

		public void setApplicationFormCveLoanDao(ApplicationFormCveLoanDao applicationFormCveLoanDao) {
			this.applicationFormCveLoanDao = applicationFormCveLoanDao;
		}	
		//Ended by Pratima for CVE
		
	
}
