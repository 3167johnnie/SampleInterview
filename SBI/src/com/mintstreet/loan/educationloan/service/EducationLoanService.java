package com.mintstreet.loan.educationloan.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.LockTimeoutException;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;
import javax.persistence.PessimisticLockException;
import javax.persistence.TransactionRequiredException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.LoanPurposeDao;
import com.mintstreet.common.dao.MasterCBSCallDao;
import com.mintstreet.common.dao.MasterCBSResponseDao;
import com.mintstreet.common.dao.MasterCertificateDao;
import com.mintstreet.common.dao.MasterGraduationDao;
import com.mintstreet.common.dao.MasterInstituteDao;
import com.mintstreet.common.dao.MasterUniversityDao;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCertificate;
import com.mintstreet.common.entity.MasterGraduation;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.entity.MasterUniversity;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.educationloan.dao.ApplicationFormEducationLoanCallsDao;
import com.mintstreet.loan.educationloan.dao.ApplicationFormEducationLoanDao;
import com.mintstreet.loan.educationloan.dao.ApplicationFormEducationLoanQuoteDao;
import com.mintstreet.loan.educationloan.dao.MasterCourseTypeDao;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanCalls;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.entity.MasterCourseType;
import com.mintstreet.loan.product.dao.ElProductDao;
import com.mintstreet.loan.product.entity.MasterElProduct;

public class EducationLoanService{
	private static final Logger logger = LogManager.getLogger(EducationLoanService.class.getName());
	private ApplicationFormEducationLoanDao applicationFormEducationLoanDao;
	private ApplicationFormEducationLoanQuoteDao applicationFormEducationLoanQuoteDao;
	private ApplicationFormEducationLoanCallsDao applicationFormEducationLoanCallsDao;
	private MasterInstituteDao masterInstituteDao;
	private MasterGraduationDao masterGraduationDao ;
	private MasterCertificateDao masterCertificateDao;
	private MasterUniversityDao masterUniversityDao;
	private MasterCourseTypeDao masterCourseTypeDao;
	private ElProductDao elProductDao;
	private LoanPurposeDao loanPurposeDao;
	private MasterCBSResponseDao masterCBSResponseDao;
	private MasterCBSCallDao masterCBSCallDao;
	
	public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
		return applicationFormEducationLoanDao.isAppFoundForDedupInDropOffStage(isdCode, appMobileNo, loanPurposeId);
	}
	public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
		return applicationFormEducationLoanDao.isAppFoundForDedupInApplicationStage(appReferenceId, isdCode, appMobileNo, loanPurposeId);
	}
	public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
		return applicationFormEducationLoanDao.isAppFoundForDedupInDropRejectStage(isdCode, appMobileNo, loanPurposeId);
	}
	
	
	public ApplicationFormEducationLoan convert(ApplicationFormEducationLoan application){
		try{
			if(application.getAppDobDT()!=null){
	 			application.setAppDob(DateUtil.convertDateToFormattedType(application.getAppDobDT(), "dd-MM-yyyy"));
			}
	 		if(application.getAppDocPickupDateDT()!=null){
	 			application.setAppDocPickupDate(DateUtil.convertDateToFormattedType(application.getAppDocPickupDateDT(), "dd-MM-yyyy"));
			}
			if(application.getAppDocPickupTime()!=null){
				application.setAppDocPickupTimeString(DateUtil.getStringDateFromDate(application.getAppDocPickupTime(), "HH:mm"));
			}
		} catch(NullPointerException e) {
			logger.info("EducationLoanService.java LNo : 59 : Exception Caught",e);
		} catch(Exception e) {
			logger.info("EducationLoanService.java LNo : 59 : Exception Caught",e);
		}
		return application;
	}
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo)throws SQLException, NoResultException {
		ApplicationFormEducationLoan application = applicationFormEducationLoanDao.getApplicationFormEducationLoanByAppReferenceIdAndMobileNo(appReferenceId, appMobileNo);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}

	public ApplicationFormEducationLoan getApplicationFormEducationLoanByAppReferenceId(String appReferenceId)throws SQLException, NoResultException {
 		ApplicationFormEducationLoan application = applicationFormEducationLoanDao.getApplicationFormEducationLoanByAppReferenceId(appReferenceId);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}
	public ApplicationFormEducationLoan save(ApplicationFormEducationLoan applicationFormEducationLoan) throws SQLException {
		applicationFormEducationLoan.setAppLeadUpdateTime(new Date());
		return applicationFormEducationLoanDao.save(applicationFormEducationLoan.getAppSeqId(), applicationFormEducationLoan);
	}
	
	public ApplicationFormEducationLoanQuote save(ApplicationFormEducationLoanQuote applicationFormEducationLoanQuote)throws SQLException {
		return applicationFormEducationLoanQuoteDao.save(applicationFormEducationLoanQuote.getLoanQuoteId(),applicationFormEducationLoanQuote);
	}
	
	public ApplicationFormEducationLoanCalls save(ApplicationFormEducationLoanCalls  callsLog)throws SQLException{
		return applicationFormEducationLoanCallsDao.save(callsLog.getCallId(), callsLog);
	}
	

	public ApplicationFormEducationLoanQuote getApplicationFormEducationLoanQuoteByQuoteId(Integer loanQuoteId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
		ApplicationFormEducationLoanQuote quote = applicationFormEducationLoanQuoteDao.findById(loanQuoteId, ApplicationFormEducationLoanQuote.class);
		if(quote==null){
			return null;
		}
		if(quote.getLoanQuoteDateOfBirthDT()!=null){
			quote.setLoanQuoteAge(DateUtil.getAge(quote.getLoanQuoteDateOfBirthDT()));
			quote.setLoanQuoteDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteDateOfBirthDT(), "dd-MM-yyyy"));
		}
		Date date =  null;

		if("Y".equals(quote.getLoanQuoteLoanOptedLoan())){
			if(quote.getLoanQuoteSTDOfLoanRepayment()!=null){
				date = quote.getLoanQuoteSTDOfLoanRepayment();
				if(date != null){
					quote.setLoanQuoteYearstartDateOfloanRepayment(DateUtil.getYearFromDate(date));
					quote.setLoanQuoteMonthstartDateOfloanRepayment(DateUtil.getMonthFromDate(date));
				}
				quote.setLoanQuoteStartDateOfLoanRepayment(quote.getLoanQuoteYearstartDateOfloanRepayment()+"-"+(quote.getLoanQuoteMonthstartDateOfloanRepayment()>9?quote.getLoanQuoteMonthstartDateOfloanRepayment():"0"+quote.getLoanQuoteMonthstartDateOfloanRepayment())+"-01");
			}
		if(quote.getLoanQuoteSTDOfAppliedCourse()!=null){
			date = quote.getLoanQuoteSTDOfAppliedCourse();
			 if(date != null){
					quote.setLoanQuoteYeardateOfAppliendCourse(DateUtil.getYearFromDate(date));
					quote.setLoanQuoteMonthdateOfAppliendCourse(DateUtil.getMonthFromDate(date));
			}
			 quote.setLoanQuoteStartDateOfAppliedCourse(quote.getLoanQuoteYeardateOfAppliendCourse()+"-"+(quote.getLoanQuoteMonthdateOfAppliendCourse()>9?quote.getLoanQuoteMonthdateOfAppliendCourse():"0"+quote.getLoanQuoteMonthdateOfAppliendCourse())+"-01");
		}
		if(quote.getLoanQuoteEndDateOfCurrentLoan()!=null){
			date = quote.getLoanQuoteEndDateOfCurrentLoan();
			 if(date != null){
					quote.setLoanQuoteYearloanTenureEndDate(DateUtil.getYearFromDate(date));
					quote.setLoanQuoteMonthloanTenureEndDate(DateUtil.getMonthFromDate(date));
			}
	    }	
		}
		if(ValidatorUtil.isValid(quote.getLoanQuoteCourseDuration())){
			int years=quote.getLoanQuoteCourseDuration()/12;
			quote.setLoanQuoteCourseDurationYears(years);
		}
		if(ValidatorUtil.isValid(quote.getLoanQuoteCourseDuration())){
			int months=quote.getLoanQuoteCourseDuration()%12;
			quote.setLoanQuoteCourseDurationMonth(months);
		}

		
		Double loanRequestedAmount = 0.0;
		if(quote.getLoanQuoteLoanType()!=null && (quote.getLoanQuoteLoanType()==1 ||quote.getLoanQuoteLoanType()==2) ){
			loanRequestedAmount =  (quote.getLoanQuoteTuitionFee()!=null?quote.getLoanQuoteTuitionFee():0)+
					(quote.getLoanQuoteAdmissionFee()!=null?quote.getLoanQuoteAdmissionFee():0) +
					(quote.getLoanQuoteHostelFee()!=null?quote.getLoanQuoteHostelFee():0)+
					(quote.getLoanQuoteExaminationLabOrLibFee()!=null?quote.getLoanQuoteExaminationLabOrLibFee():0)+
					(quote.getLoanQuotePurchaseOfBookAndEquipment()!=null?quote.getLoanQuotePurchaseOfBookAndEquipment():0)+
					(quote.getLoanQuotedepositFee()!=null?quote.getLoanQuotedepositFee():0)+
					(quote.getLoanQuoteTravelExpenses()!=null?quote.getLoanQuoteTravelExpenses():0)+
					(quote.getLoanQuotePurchaseOfComputer()!=null?quote.getLoanQuotePurchaseOfComputer():0)+ 
					(quote.getLoanQuoteOtherExpenses()!=null?quote.getLoanQuoteOtherExpenses():0);
			
		}else if(quote.getLoanQuoteLoanType()!=null &&  quote.getLoanQuoteLoanType()==3){
			loanRequestedAmount =  (quote.getLoanQuoteTuitionFee()!=null?quote.getLoanQuoteTuitionFee():0)+
					(quote.getLoanQuoteAdmissionFee()!=null?quote.getLoanQuoteAdmissionFee():0) +
					(quote.getLoanQuoteHostelFee()!=null?quote.getLoanQuoteHostelFee():0)+
					(quote.getLoanQuoteExaminationLabOrLibFee()!=null?quote.getLoanQuoteExaminationLabOrLibFee():0)+
					(quote.getLoanQuotePurchaseOfBookAndEquipment()!=null?quote.getLoanQuotePurchaseOfBookAndEquipment():0)+
					(quote.getLoanQuotedepositFee()!=null?quote.getLoanQuotedepositFee():0)+
					(quote.getLoanQuoteTravelExpenses()!=null?quote.getLoanQuoteTravelExpenses():0)+
					(quote.getLoanQuotePurchaseOfComputer()!=null?quote.getLoanQuotePurchaseOfComputer():0)+ 
					(quote.getLoanQuotePremium()!=null?quote.getLoanQuotePremium():0);
		}else if(quote.getLoanQuoteLoanType()!=null &&  quote.getLoanQuoteLoanType()==4){
			loanRequestedAmount =  (quote.getLoanQuoteTuitionFee()!=null?quote.getLoanQuoteTuitionFee():0)+
					(quote.getLoanQuoteExaminationLabOrLibFee()!=null?quote.getLoanQuoteExaminationLabOrLibFee():0)+
					(quote.getLoanQuotePurchaseOfBookAndEquipment()!=null?quote.getLoanQuotePurchaseOfBookAndEquipment():0)+
					(quote.getLoanQuotedepositFee()!=null?quote.getLoanQuotedepositFee():0)+
					(quote.getLoanQuoteOtherExpenses()!=null?quote.getLoanQuoteOtherExpenses():0);
		}

		
		
		loanRequestedAmount =  (quote.getLoanQuoteTuitionFee()!=null?quote.getLoanQuoteTuitionFee():0)+
				(quote.getLoanQuoteAdmissionFee()!=null?quote.getLoanQuoteAdmissionFee():0) +
				(quote.getLoanQuoteHostelFee()!=null?quote.getLoanQuoteHostelFee():0)+
				(quote.getLoanQuoteExaminationLabOrLibFee()!=null?quote.getLoanQuoteExaminationLabOrLibFee():0)+
				(quote.getLoanQuotePurchaseOfBookAndEquipment()!=null?quote.getLoanQuotePurchaseOfBookAndEquipment():0)+

				(quote.getLoanQuoteCautionFee()!=null?quote.getLoanQuoteCautionFee():0)+
				(quote.getLoanQuotePurchaseOfTwoWheeler()!=null?quote.getLoanQuotePurchaseOfTwoWheeler():0)+
				(quote.getLoanQuoteTravelExpenses()!=null?quote.getLoanQuoteTravelExpenses():0)+
				(quote.getLoanQuotePurchaseOfComputer()!=null?quote.getLoanQuotePurchaseOfComputer():0)+ 
				(quote.getLoanQuoteOtherExpenses()!=null?quote.getLoanQuoteOtherExpenses():0);
		
		
		if(quote.getLoanQuoteOverseasHealthInsuranceFee()>0 ){
			loanRequestedAmount = loanRequestedAmount + quote.getLoanQuoteOverseasHealthInsuranceFee();
		}
		
		if(quote.getLoanQuoteScholarship()!=null && quote.getLoanQuoteScholarship()>0 ){
			loanRequestedAmount = loanRequestedAmount - quote.getLoanQuoteScholarship();
		}
		quote.setLoanQuoteLoanRequestedAmount(loanRequestedAmount);
		return quote;
	}
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByAppSeqId(Integer appSeqId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException {
		ApplicationFormEducationLoan application = applicationFormEducationLoanDao.findById(appSeqId, ApplicationFormEducationLoan.class);
		if(application==null){
 			return null;
 		}
		application = convert(application);
		return application;
	}
	
	public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException{
		return applicationFormEducationLoanDao.getLastGeneratedReferenceNumber(loanTypeId);
	}
	public boolean isReferenceIdAvailable(String appReferenceId) throws SQLException{
		return applicationFormEducationLoanDao.isReferenceIdAvailable(appReferenceId);
	}
	public List <MasterInstitute> instituteByCourseType(Integer courseType) throws NoResultException{
		return masterInstituteDao.getInstituteByCourseType(courseType);
	}
	
	public List <MasterInstitute> getInstituteNameList() throws NoResultException{
		return masterInstituteDao.getInstituteNameList();
	}
	
	public List <MasterInstitute> getAllInstituteByQuerySting(String queryString, Integer courseTypeId, Integer instituteIsIndia, Integer loanTypeId) throws NoResultException{
		return masterInstituteDao.getAllInstituteByQuerySting(queryString, courseTypeId, instituteIsIndia, loanTypeId);
	}
	
	public MasterInstitute instituteCategoryById(Integer instituteId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException{
		return masterInstituteDao.findById(instituteId, MasterInstitute.class);
	}
	
	public MasterInstitute getInstituteByInstituteName(String instituteName) throws NoResultException{
		return masterInstituteDao.getInstituteByInstituteName(instituteName);
	}
	
	public MasterInstitute getInstituteByInstituteId(Integer instituteId) throws NoResultException{
		return masterInstituteDao.getInstituteByInstituteID(instituteId);
	}
	public List <MasterGraduation> gradutionByCourseType(Integer courseType) throws NoResultException{
		return masterGraduationDao.getGraduationByCourseType(courseType);
	}
	public List <MasterGraduation> getAllCourseByQuerySting(String courseName, Integer courseTypeId, Integer graduationIsInIndia) throws NoResultException{
		return masterGraduationDao.getAllCourseByQuerySting(courseName, courseTypeId, graduationIsInIndia);
	}
	
	public MasterGraduation gradutionById(Integer graduationId) throws IllegalArgumentException,TransactionRequiredException,OptimisticLockException,PessimisticLockException,LockTimeoutException,PersistenceException{
		return masterGraduationDao.findById(graduationId, MasterGraduation.class);
	}

	public MasterGraduation getGraduationByGraduationName(String graduationName) throws NoResultException{
		return masterGraduationDao.getGraduationByGraduationName(graduationName);
	}

	public List <MasterCertificate> getCertificateByCourseType(Integer courseType) throws SQLException, NoResultException{
		return masterCertificateDao.getCerfificateByCourseType(courseType);
		
	}
	
	public List <MasterUniversity> getAllUniversityByQuerySting(String queryString, Integer universityIsInIndia) throws SQLException, NoResultException{
		return masterUniversityDao.getAllUniversityByQuerySting(queryString, universityIsInIndia);
	}
	
	public Map<Integer, String> getCourseType(Integer isAbroad) throws NoResultException, SQLException{
		return masterCourseTypeDao.getCourseType(isAbroad);
	}
	public List<BigDecimal> getCourseTypeList(Integer isAbroad) throws NoResultException, SQLException{
		return masterCourseTypeDao.getCourseTypeList(isAbroad);
	}
	
	public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException, NoResultException {
		return applicationFormEducationLoanDao.getVisitByAppSeqId(appSeqId);
	}
	public Integer getOldVisitId(Integer quoteId) throws SQLException{
		return applicationFormEducationLoanQuoteDao.getOldVisitId(quoteId);
	}
	/*public boolean isVLPStudentIdExists(int appVLPStudentAppId) throws SQLException, NoResultException{
		return applicationFormEducationLoanDao.isVLPStudentIdExists(appVLPStudentAppId);
	}*/
	public MasterCourseType getAllCourseTypeId(Integer courseTypeId) throws SQLException{
		return masterCourseTypeDao.findById(courseTypeId, MasterCourseType.class);
	}
	public String getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(Integer loanPurposeId,Integer loanTypeId) throws NoResultException{
		return loanPurposeDao.getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(loanPurposeId, loanTypeId);
	}
	
	public MasterCBSResponse getMasterCBSResponseObjectByCbsAppSeqId(Integer appSeqId) throws SQLException, NoResultException{
		return masterCBSResponseDao.getMasterCBSResponseObjectByCbsAppSeqId(appSeqId);
	}
	
	public MasterCBSResponse getMasterCBSResponseObjectByLoanTypeAppSeqId(Integer appSeqId) throws SQLException, NoResultException{
		return masterCBSResponseDao.getMasterCBSResponseObjectByLoanTypeAppSeqId(Constants.EDUCATION_LOAN_ID,appSeqId);
	}
	
	public MasterCBSCall getMasterCBSCallObjectByCbsResponseId(Integer cbsResponseId) throws SQLException, NoResultException{	
		return masterCBSCallDao.getMasterCBSCallObjectByCbsResponseId(cbsResponseId);
	}
	
	public ApplicationFormEducationLoanDao getApplicationFormEducationLoanDao() {
		return applicationFormEducationLoanDao;
	}

	public void setApplicationFormEducationLoanDao(
			ApplicationFormEducationLoanDao applicationFormEducationLoanDao) {
		this.applicationFormEducationLoanDao = applicationFormEducationLoanDao;
	}

	public ApplicationFormEducationLoanQuoteDao getApplicationFormEducationLoanQuoteDao() {
		return applicationFormEducationLoanQuoteDao;
	}

	public void setApplicationFormEducationLoanQuoteDao(
			ApplicationFormEducationLoanQuoteDao applicationFormEducationLoanQuoteDao) {
		this.applicationFormEducationLoanQuoteDao = applicationFormEducationLoanQuoteDao;
	}

	public ApplicationFormEducationLoanCallsDao getApplicationFormEducationLoanCallsDao() {
		return applicationFormEducationLoanCallsDao;
	}

	public void setApplicationFormEducationLoanCallsDao(
			ApplicationFormEducationLoanCallsDao applicationFormEducationLoanCallsDao) {
		this.applicationFormEducationLoanCallsDao = applicationFormEducationLoanCallsDao;
	}

	public MasterInstituteDao getMasterInstituteDao() {
		return masterInstituteDao;
	}

	public void setMasterInstituteDao(MasterInstituteDao masterInstituteDao) {
		this.masterInstituteDao = masterInstituteDao;
	}

	public MasterGraduationDao getMasterGraduationDao() {
		return masterGraduationDao;
	}

	public void setMasterGraduationDao(MasterGraduationDao masterGraduationDao) {
		this.masterGraduationDao = masterGraduationDao;
	}

	public MasterCertificateDao getMasterCertificateDao() {
		return masterCertificateDao;
	}

	public void setMasterCertificateDao(MasterCertificateDao masterCertificateDao) {
		this.masterCertificateDao = masterCertificateDao;
	}

	public MasterUniversityDao getMasterUniversityDao() {
		return masterUniversityDao;
	}

	public void setMasterUniversityDao(MasterUniversityDao masterUniversityDao) {
		this.masterUniversityDao = masterUniversityDao;
	}

	public MasterCourseTypeDao getMasterCourseTypeDao() {
		return masterCourseTypeDao;
	}

	public void setMasterCourseTypeDao(MasterCourseTypeDao masterCourseTypeDao) {
		this.masterCourseTypeDao = masterCourseTypeDao;
	}

	public MasterElProduct gethlProductById(Integer elProductId) throws SQLException{
		return elProductDao.getElProductById(elProductId);
	}
		
	public void setElProductDao(ElProductDao elProductDao) {
		this.elProductDao = elProductDao;
	}
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByMobileAndSmsOtp(String appMobileNo,Integer appMobileVerificationCode) throws NoResultException{ 
		return applicationFormEducationLoanDao.getApplicationFormEducationLoanByMobileAndSmsOtp(appMobileNo,appMobileVerificationCode);
	}
	public ElProductDao getElProductDao() {
		return elProductDao;
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
	
}
