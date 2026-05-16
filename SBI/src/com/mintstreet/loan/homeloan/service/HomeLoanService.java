package com.mintstreet.loan.homeloan.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mintstreet.common.dao.LoanPurposeDao;
import com.mintstreet.common.dao.MasterCBSCallDao;
import com.mintstreet.common.dao.MasterCBSResponseDao;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.loan.homeloan.dao.ApplicationFormHomeLoanCallsDao;
import com.mintstreet.loan.homeloan.dao.ApplicationFormHomeLoanDao;
import com.mintstreet.loan.homeloan.dao.ApplicationFormHomeLoanQuoteDao;
import com.mintstreet.loan.homeloan.dao.MasterProjectDao;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanCalls;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.entity.MasterProject;
import com.mintstreet.loan.product.dao.HlProductDao;
import com.mintstreet.loan.product.entity.HlProduct;

@Component
public class HomeLoanService {
  private static final Logger logger = LogManager.getLogger(HomeLoanService.class.getName());
  
  private ApplicationFormHomeLoanDao applicationFormHomeLoanDao;
  
  private ApplicationFormHomeLoanQuoteDao applicationFormHomeLoanQuoteDao;
  
  private ApplicationFormHomeLoanCallsDao applicationFormHomeLoanCallsDao;
  
  private MasterProjectDao masterProjectDao;
  
  private HlProductDao hlProductDao;
  
  private LoanPurposeDao loanPurposeDao;
  
  private MasterCBSResponseDao masterCBSResponseDao;
  
  private MasterCBSCallDao masterCBSCallDao;
  
  public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException {
    return this.applicationFormHomeLoanDao.isAppFoundForDedupInDropOffStage(isdCode, appMobileNo, loanPurposeId);
  }
  
  public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException {
    return this.applicationFormHomeLoanDao.isAppFoundForDedupInApplicationStage(appReferenceId, isdCode, appMobileNo, loanPurposeId);
  }
  
  public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException {
    return this.applicationFormHomeLoanDao.isAppFoundForDedupInDropRejectStage(isdCode, appMobileNo, loanPurposeId);
  }
  
  public ApplicationFormHomeLoan save(ApplicationFormHomeLoan applicationFormHomeLoan) throws SQLException {
      applicationFormHomeLoan.setAppLeadUpdateTime(new Date());
	  
      //return (ApplicationFormHomeLoan)this.applicationFormHomeLoanDao.save(applicationFormHomeLoan.getAppSeqId(), (Domain)applicationFormHomeLoan);
      return applicationFormHomeLoanDao.save(applicationFormHomeLoan.getAppSeqId(), applicationFormHomeLoan);
  }
  
  public ApplicationFormHomeLoanQuote save(ApplicationFormHomeLoanQuote applicationFormHomeLoanQuote)throws SQLException {
      //return (ApplicationFormHomeLoanQuote)this.applicationFormHomeLoanQuoteDao.save(applicationFormHomeLoanQuote.getLoanQuoteId(), (Domain)applicationFormHomeLoanQuote);
      return applicationFormHomeLoanQuoteDao.save(applicationFormHomeLoanQuote.getLoanQuoteId(), applicationFormHomeLoanQuote);
      
  }
  
  public ApplicationFormHomeLoanCalls save(ApplicationFormHomeLoanCalls callsLog) throws SQLException {
      //return (ApplicationFormHomeLoanCalls)this.applicationFormHomeLoanCallsDao.save(callsLog.getCallId(), (Domain)callsLog);
      return applicationFormHomeLoanCallsDao.save(callsLog.getCallId(), callsLog);
      
  }
  
  public ApplicationFormHomeLoanQuote getApplicationFromHomeLoanQuoteByQuoteId(Integer quoteId)throws SQLException  {
    ApplicationFormHomeLoanQuote quote = (ApplicationFormHomeLoanQuote)this.applicationFormHomeLoanQuoteDao.findById(quoteId, ApplicationFormHomeLoanQuote.class);
    Date date = null;
    if (quote.getLoanQuoteDateOfBirthDT() != null) {
      quote.setLoanQuoteAge(Integer.valueOf(DateUtil.getAge(quote.getLoanQuoteDateOfBirthDT())));
      quote.setLoanQuoteDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteDateOfBirthDT(), "dd-MM-yyyy"));
    } 
    if (quote.getLoanQuoteCoapplicantFirstDateOfBirthDT() != null) {
      quote.setLoanQuoteCoapplicantFirstAge(Integer.valueOf(DateUtil.getAge(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT())));
      quote.setLoanQuoteCoapplicantFirstDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT(), "dd-MM-yyyy"));
    } 
    if (quote.getLoanQuoteCoapplicantSecondDateOfBirthDT() != null) {
      quote.setLoanQuoteCoapplicantSecondAge(Integer.valueOf(DateUtil.getAge(quote.getLoanQuoteCoapplicantSecondDateOfBirthDT())));
      quote.setLoanQuoteCoapplicantSecondDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteCoapplicantSecondDateOfBirthDT(), "dd-MM-yyyy"));
    } 
    date = quote.getLoanQuoteEmiStartDateOfExistingTopupLoan();
    if (date != null) {
      quote.setLoanQuoteEmiStartDateOfExistingTopupLoanYear(Integer.valueOf(DateUtil.getYearFromDate(date)));
      quote.setLoanQuoteEmiStartDateOfExistingTopupLoanMonth(Integer.valueOf(DateUtil.getMonthFromDate(date)));
    } 
    date = quote.getLoanQuoteExistingHomeLoanStartDate();
    if (date != null) {
      quote.setLoanQuoteYearExistingHomeLoanStartDate(Integer.valueOf(DateUtil.getYearFromDate(date)));
      quote.setLoanQuoteMonthExistingHomeLoanStartDate(Integer.valueOf(DateUtil.getMonthFromDate(date)));
    } 
    date = quote.getLoanQuoteExistingHomeLoanEndDate();
    if (date != null) {
      quote.setLoanQuoteYearExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getYearFromDate(date)));
      quote.setLoanQuoteMonthExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getMonthFromDate(date)));
    } 
    date = quote.getLoanQuoteEmiStartDateOfExistingTopupLoan();
    if (date != null) {
      quote.setLoanQuoteYearExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getYearFromDate(date)));
      quote.setLoanQuoteMonthExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getMonthFromDate(date)));
    } 
    date = quote.getLoanQuoteCompletionDate();
    if (date != null) {
      quote.setLoanQuoteYearCompletionDate(Integer.valueOf(DateUtil.getYearFromDate(date)));
      quote.setLoanQuoteMonthCompletionDate(Integer.valueOf(DateUtil.getMonthFromDate(date)));
    } 
    if (quote.getLoanQuoteLoanAccountType() != null)
      quote.setLoanQuoteLoanAccountType(quote.getLoanQuoteLoanAccountType()); 
    if (quote.getLoanQuoteCheckOffType() != null)
      quote.setLoanQuoteCheckOffType(quote.getLoanQuoteCheckOffType()); 
    if (quote.getLoanQuoteLoanPurposeId() != null)
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 3) {
        quote.setLoanType("Balance Takeover");
        quote.setLoanPurpose(quote.getLoanQuotePropertyType());
      } else {
        quote.setLoanType("New");
        if (quote.getLoanQuoteLoanPurposeId().intValue() == 2) {
          quote.setLoanPurpose(Integer.valueOf(11));
        } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 4) {
          quote.setLoanPurpose(Integer.valueOf(12));
        } else if (quote.getLoanQuoteLoanPurposeId().intValue() != 5) {
          if (quote.getLoanQuoteLoanPurposeId().intValue() == 1)
            quote.setLoanPurpose(quote.getLoanQuoteLoanCategoryId()); 
        } 
      }  
    return quote;
  }
  
  public ApplicationFormHomeLoan convert(ApplicationFormHomeLoan application) {
    try {
      if (application.getAppDobDT() != null)
        application.setAppDob(DateUtil.convertDateToFormattedType(application.getAppDobDT(), "dd-MM-yyyy")); 
      if (application.getAppDocPickupDateDT() != null)
        application.setAppDocPickupDate(DateUtil.convertDateToFormattedType(application.getAppDocPickupDateDT(), "dd-MM-yyyy")); 
      if (application.getAppCompanyJoiningDate() != null) {
        Date date = null;
        date = application.getAppCompanyJoiningDate();
        if (date != null) {
          application.setAppCompanyJoiningMonth(Integer.valueOf(DateUtil.getMonthFromDate(date)));
          application.setAppCompanyJoiningYear(Integer.valueOf(DateUtil.getYearFromDate(date)));
        } 
      } 
      if (application.getAppDocPickupTime() != null)
        application.setAppDocPickupTimeString(DateUtil.getStringDateFromDate(application.getAppDocPickupTime(), "HH:mm")); 
    } catch (NullPointerException e) {
        logger.info("HomeLoanService.java LNo : 145 : Exception Caught", e);
    } catch (Exception e) {
      logger.info("HomeLoanService.java LNo : 145 : Exception Caught", e);
    } 
    return application;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByAppSeqId(Integer appSeqId) throws SQLException, NoResultException {
    ApplicationFormHomeLoan application = (ApplicationFormHomeLoan)this.applicationFormHomeLoanDao.findById(appSeqId, ApplicationFormHomeLoan.class);
    if (application == null)
      return null; 
    application = convert(application);
    return application;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByAppReferenceId(String appReferenceId) throws SQLException, NoResultException {
    ApplicationFormHomeLoan application = this.applicationFormHomeLoanDao.getApplicationFormHomeLoanByAppReferenceId(appReferenceId);
    if (application == null)
      return null; 
    application = convert(application);
    return application;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo) throws SQLException, NoResultException {
    ApplicationFormHomeLoan application = this.applicationFormHomeLoanDao.getApplicationFormHomeLoanByAppReferenceIdAndMobileNo(appReferenceId, appMobileNo);
    if (application == null)
      return null; 
    application = convert(application);
    return application;
  }
  
  public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException, NoResultException {
    return this.applicationFormHomeLoanDao.getLastGeneratedReferenceNumber(loanTypeId);
  }
  
  public boolean isReferenceIdAvailable(String appReferenceId) throws SQLException, NoResultException {
    return this.applicationFormHomeLoanDao.isReferenceIdAvailable(appReferenceId);
  }
  
  public List<MasterProject> findProjectByBuilderId(Integer projectBuilderId) throws NoResultException, SQLException {
    return this.masterProjectDao.findProjectByBuilderId(projectBuilderId);
  }
  
  public MasterProject findProjectById(Integer projectId)throws SQLException  {
    return (MasterProject)this.masterProjectDao.findById(projectId, MasterProject.class);
  }
  
  public List<MasterProject> getAllProjectByQuerySting(String queryString) throws NoResultException, SQLException {
    return this.masterProjectDao.getAllProject(queryString);
  }
  
  public List<MasterProject> getBuilderName() throws NoResultException {
    return this.masterProjectDao.getBuilderName();
  }
  
  public Integer getProjectIdByProjectName(String builderName) throws SQLException, NoResultException {
    List<MasterProject> projectList = this.masterProjectDao.getProjectByProjectName(builderName);
    if (projectList == null || projectList.size() == 0)
      return null; 
    if (projectList != null && projectList.size() > 0) {
      MasterProject obj = projectList.get(0);
      return obj.getProjectId();
    } 
    return null;
  }
  
  public MasterProject getProjectByProjectName(String builderName) throws SQLException {
    List<MasterProject> projectList = this.masterProjectDao.getProjectByProjectName(builderName);
    if (projectList == null || projectList.size() == 0)
      return null; 
    return projectList.get(0);
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByMobileAndSmsOtp(String appMobileNo, Integer appMobileVerificationCode) throws SQLException, NoResultException {
    return this.applicationFormHomeLoanDao.getApplicationFormHomeLoanByMobileAndSmsOtp(appMobileNo, appMobileVerificationCode);
  }
  
  public String getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(Integer loanPurposeId, Integer loanTypeId) throws SQLException, NoResultException {
    return this.loanPurposeDao.getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(loanPurposeId, loanTypeId);
  }
  
  public Integer getOldVisitId(Integer quoteId) throws SQLException, NoResultException {
    return this.applicationFormHomeLoanQuoteDao.getOldVisitId(quoteId);
  }
  
  public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException, NoResultException {
    return this.applicationFormHomeLoanDao.getVisitByAppSeqId(appSeqId);
  }
  
  public MasterCBSResponse getMasterCBSResponseObjectByCbsAppSeqId(Integer appSeqId) throws NoResultException, SQLException  {
    return this.masterCBSResponseDao.getMasterCBSResponseObjectByCbsAppSeqId(appSeqId);
  }
  
  public MasterCBSResponse getMasterCBSResponseObjectByLoanTypeAppSeqId(Integer appSeqId) throws NoResultException, SQLException {
    return this.masterCBSResponseDao.getMasterCBSResponseObjectByLoanTypeAppSeqId(Constants.HOME_LOAN_ID, appSeqId);
  }
  
  public MasterCBSCall getMasterCBSCallObjectByCbsResponseId(Integer cbsResponseId) throws NoResultException, SQLException  {
    return this.masterCBSCallDao.getMasterCBSCallObjectByCbsResponseId(cbsResponseId);
  }
  
  public ApplicationFormHomeLoanDao getApplicationFormHomeLoanDao() {
    return this.applicationFormHomeLoanDao;
  }
  
  public void setApplicationFormHomeLoanDao(ApplicationFormHomeLoanDao applicationFormHomeLoanDao) {
    this.applicationFormHomeLoanDao = applicationFormHomeLoanDao;
  }
  
  public ApplicationFormHomeLoanQuoteDao getApplicationFormHomeLoanQuoteDao() {
    return this.applicationFormHomeLoanQuoteDao;
  }
  
  public void setApplicationFormHomeLoanQuoteDao(ApplicationFormHomeLoanQuoteDao applicationFormHomeLoanQuoteDao) {
    this.applicationFormHomeLoanQuoteDao = applicationFormHomeLoanQuoteDao;
  }
  
  public ApplicationFormHomeLoanCallsDao getApplicationFormHomeLoanCallsDao() {
    return this.applicationFormHomeLoanCallsDao;
  }
  
  public void setApplicationFormHomeLoanCallsDao(ApplicationFormHomeLoanCallsDao applicationFormHomeLoanCallsDao) {
    this.applicationFormHomeLoanCallsDao = applicationFormHomeLoanCallsDao;
  }
  
  public MasterProjectDao getMasterProjectDao() {
    return this.masterProjectDao;
  }
  
  public void setMasterProjectDao(MasterProjectDao masterProjectDao) {
    this.masterProjectDao = masterProjectDao;
  }
  
  public HlProduct gethlProductById(Integer hlProductId) throws SQLException, NoResultException {
    return this.hlProductDao.getHlProductById(hlProductId);
  }
  
  public void setHlProductDao(HlProductDao hlProductDao) {
    this.hlProductDao = hlProductDao;
  }
  
  public HlProductDao getHlProductDao() {
    return this.hlProductDao;
  }
  
  public LoanPurposeDao getLoanPurposeDao() {
    return this.loanPurposeDao;
  }
  
  public void setLoanPurposeDao(LoanPurposeDao loanPurposeDao) {
    this.loanPurposeDao = loanPurposeDao;
  }
  
  public MasterCBSResponseDao getMasterCBSResponseDao() {
    return this.masterCBSResponseDao;
  }
  
  public void setMasterCBSResponseDao(MasterCBSResponseDao masterCBSResponseDao) {
    this.masterCBSResponseDao = masterCBSResponseDao;
  }
  
  public MasterCBSCallDao getMasterCBSCallDao() {
    return this.masterCBSCallDao;
  }
  
  public void setMasterCBSCallDao(MasterCBSCallDao masterCBSCallDao) {
    this.masterCBSCallDao = masterCBSCallDao;
  }
}
