package com.mintstreet.loan.autoloan.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.LoanPurposeDao;
import com.mintstreet.common.dao.MasterCBSCallDao;
import com.mintstreet.common.dao.MasterCBSResponseDao;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.loan.autoloan.dao.ApplicationFormAutoLoanCallsDao;
import com.mintstreet.loan.autoloan.dao.ApplicationFormAutoLoanDao;
import com.mintstreet.loan.autoloan.dao.ApplicationFormAutoLoanQuoteDao;
import com.mintstreet.loan.autoloan.dao.CarCompanyDao;
import com.mintstreet.loan.autoloan.dao.CarModelDao;
import com.mintstreet.loan.autoloan.dao.CarVariantDao;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanCalls;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.entity.MasterCarCompany;
import com.mintstreet.loan.autoloan.entity.MasterCarModel;
import com.mintstreet.loan.autoloan.entity.MasterCarVariant;
import com.mintstreet.loan.product.dao.AlProductDao;
import com.mintstreet.loan.product.entity.AlProduct;

public class AutoLoanService{
	private static final Logger logger = LogManager.getLogger(AutoLoanService.class.getName());
	private CarCompanyDao carCompanyDao;
	private CarModelDao carModelDao;
	private CarVariantDao carVariantDao;
	private ApplicationFormAutoLoanDao applicationFormAutoLoanDao;
	private ApplicationFormAutoLoanQuoteDao applicationFormAutoLoanQuoteDao;
	private ApplicationFormAutoLoanCallsDao applicationFormAutoLoanCallsDao;
	private AlProductDao alProductDao;
	private LoanPurposeDao loanPurposeDao;
	private MasterCBSResponseDao masterCBSResponseDao;
	private MasterCBSCallDao masterCBSCallDao;
	
	public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormAutoLoanDao.isAppFoundForDedupInDropOffStage(isdCode, appMobileNo, loanPurposeId);
	}
	
	public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormAutoLoanDao.isAppFoundForDedupInApplicationStage(appReferenceId, isdCode, appMobileNo, loanPurposeId);
	}
	
	public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{
		return applicationFormAutoLoanDao.isAppFoundForDedupInDropRejectStage(isdCode, appMobileNo, loanPurposeId);
	}
	
	
	
	public ApplicationFormAutoLoan convert(ApplicationFormAutoLoan application){
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
		} catch (NullPointerException e){
			logger.info("AutoLoanService.java LNo : 83 : Exception Caught",e);
		} catch (Exception e){
			logger.info("AutoLoanService.java LNo : 83 : Exception Caught",e);
		}
		return application;
	}
	
	public ApplicationFormAutoLoan getApplicationFormAutoLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo)throws SQLException {
		ApplicationFormAutoLoan application = applicationFormAutoLoanDao.getApplicationFormAutoLoanByAppReferenceIdAndMobileNo(appReferenceId, appMobileNo);
 		if(application==null){
 			return null;
 		}
 		application = convert(application);
		return application;
	}
	
	public MasterCarCompany getCarCompanyById(Integer carCompanyId) throws SQLException{
		return carCompanyDao.findById(carCompanyId, MasterCarCompany.class);
	}
	public MasterCarModel getCarModelById(Integer carCompanyId) throws SQLException{
		return carModelDao.findById(carCompanyId, MasterCarModel.class);
	}
	public MasterCarVariant getCarVariantById(Integer carModelId) throws SQLException, NoResultException{
		return carVariantDao.findById(carModelId, MasterCarVariant.class);
	}
	
	public List<MasterCarCompany>getCarCompany() throws SQLException, NoResultException{
		return carCompanyDao.getAllCarcompany();
	}
	
	public List<MasterCarModel>getCarModelByCompany(Integer companyId) throws SQLException, NoResultException{
		return carModelDao.getCarModelByCompany(companyId);
	}
	
	//changed by Hakeem on 01/09/2022
	public List<MasterCarVariant>getCarVariantByCarModelId(Integer modelId, Integer companyId) throws SQLException, NoResultException{
		return carVariantDao.getCarVariantByCarmodel(modelId, companyId);
	}
	
	public ApplicationFormAutoLoanDao getApplicationFormAutoLoanDao() {
		return applicationFormAutoLoanDao;
	}

	public void setApplicationFormAutoLoanDao(
			ApplicationFormAutoLoanDao applicationFormAutoLoanDao) {
		this.applicationFormAutoLoanDao = applicationFormAutoLoanDao;
	}
	
	public ApplicationFormAutoLoan save(ApplicationFormAutoLoan applicationFormAutoLoan) throws SQLException {
		applicationFormAutoLoan.setAppLeadUpdateTime(new Date());
		return applicationFormAutoLoanDao.save(applicationFormAutoLoan.getAppSeqId(), applicationFormAutoLoan);
	}

	public ApplicationFormAutoLoanQuote save(ApplicationFormAutoLoanQuote applicationFormAutoLoanQuote) throws SQLException {
		return applicationFormAutoLoanQuoteDao.save(applicationFormAutoLoanQuote.getLoanQuoteId(), applicationFormAutoLoanQuote);
	}
	
	public ApplicationFormAutoLoanCalls save(ApplicationFormAutoLoanCalls  callsLog)throws SQLException{
		return applicationFormAutoLoanCallsDao.save(callsLog.getCallId(), callsLog);
	}
	
	public Integer getOldVisitId(Integer quoteId) throws SQLException{
		return applicationFormAutoLoanQuoteDao.getOldVisitId(quoteId);
	}
	
	public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException {
		return applicationFormAutoLoanDao.getVisitByAppSeqId(appSeqId);
	}
	public ApplicationFormAutoLoanQuoteDao getApplicationFormAutoLoanQuoteDao() {
		return applicationFormAutoLoanQuoteDao;
	}

	public void setApplicationFormAutoLoanQuoteDao(
			ApplicationFormAutoLoanQuoteDao applicationFormAutoLoanQuoteDao) {
		this.applicationFormAutoLoanQuoteDao = applicationFormAutoLoanQuoteDao;
	}




	public ApplicationFormAutoLoanQuote getApplicationFormAutoLoanQuoteByQuoteId(Integer loanQuoteId) throws SQLException, NoResultException {
		ApplicationFormAutoLoanQuote quote = applicationFormAutoLoanQuoteDao.findById(loanQuoteId, ApplicationFormAutoLoanQuote.class);
		if(quote==null){
			return null;
		}
		if(quote.getLoanQuoteDateOfBirthDT()!=null){
			quote.setLoanQuoteAge(DateUtil.getAge(quote.getLoanQuoteDateOfBirthDT()));
			quote.setLoanQuoteDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteDateOfBirthDT(), "dd-MM-yyyy"));

		}
		if(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT()!=null){
			quote.setLoanQuoteCoapplicantFirstAge(DateUtil.getAge(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT()));
			quote.setLoanQuoteCoapplicantFirstDateOfBirth(DateUtil.convertDateToFormattedType(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT(), "dd-MM-yyyy"));
		}
		
		
		Date date =  null;
		if(quote.getLoanQuoteStartDateOfCurrentLoanDT()!=null){
			date = quote.getLoanQuoteStartDateOfCurrentLoanDT();
			quote.setLoanQuoteStartDateOfCurrentLoan(DateUtil.convertDateToFormattedType(quote.getLoanQuoteStartDateOfCurrentLoanDT(), "dd-MM-yyyy"));
			if(date != null){
				quote.setMonthloanQuoteStartDateOfCurrentLoan(DateUtil.getMonthFromDate(date));
				quote.setYearloanQuoteStartDateOfCurrentLoan(DateUtil.getYearFromDate(date));
			}
		}
		
		if(quote.getLoanQuoteDopDT()!=null){
			date = quote.getLoanQuoteDopDT();
			quote.setLoanQuoteDop(DateUtil.convertDateToFormattedType(quote.getLoanQuoteDopDT(), "dd-MM-yyyy"));
			if(date != null){
				quote.setMonthloanQuoteDop(DateUtil.getMonthFromDate(date));
				quote.setYearloanQuoteDop(DateUtil.getYearFromDate(date));
			}
		}

		if(Constants.BANK_ID == Constants.BANK_ID_SBI){
			Double loanRequestedAmountCar= 0.0;
			loanRequestedAmountCar = (quote.getLoanQuoteOnRoadPrice()!=null?quote.getLoanQuoteOnRoadPrice():0);
			Double loanRequestedAmountBike = 0.0;
			loanRequestedAmountBike =(quote.getLoanQuoteRoadTaxBike()!=null?quote.getLoanQuoteRoadTaxBike():0);
			quote.setLoanQuoteVehicleCost(loanRequestedAmountCar+ loanRequestedAmountBike);
		}else{
			Double loanRequestedAmountCar = 0.0;
			loanRequestedAmountCar = (quote.getLoanQuoteExshowroomPriceCar()!=null?quote.getLoanQuoteExshowroomPriceCar():0)+
					(quote.getLoanQuoteRoadTaxCar()!=null?quote.getLoanQuoteRoadTaxCar():0) 
					+ (quote.getLoanQuoteInsuranceChargeCar()!=null?quote.getLoanQuoteInsuranceChargeCar():0)+
					(quote.getLoanQuoteRegistrationChargeCar()!=null?quote.getLoanQuoteRegistrationChargeCar():0)
					+ (quote.getLoanQuoteAccessoriesChargeCar()!=null?quote.getLoanQuoteAccessoriesChargeCar():0)+
					(quote.getLoanQuoteOtherMiscChargeCar()!=null?quote.getLoanQuoteOtherMiscChargeCar():0)-
					(quote.getLoanQuoteDiscountCar()!=null?quote.getLoanQuoteDiscountCar():0);
			     Double loanRequestedAmountBike = 0.0;
			        loanRequestedAmountBike = (quote.getLoanQuoteExshowroomPriceBike()!=null?quote.getLoanQuoteExshowroomPriceBike():0)+
					(quote.getLoanQuoteRoadTaxBike()!=null?quote.getLoanQuoteRoadTaxBike():0) 
					+ (quote.getLoanQuoteInsuranceChargeBike()!=null?quote.getLoanQuoteInsuranceChargeBike():0)+
					(quote.getLoanQuoteRegistrationChargeBike()!=null?quote.getLoanQuoteRegistrationChargeBike():0)
					+ (quote.getLoanQuoteAccessoriesChargeBike()!=null?quote.getLoanQuoteAccessoriesChargeBike():0)+
					(quote.getLoanQuoteOtherMiscChargeBike()!=null?quote.getLoanQuoteOtherMiscChargeBike():0)-
					(quote.getLoanQuoteDiscountBike()!=null?quote.getLoanQuoteDiscountBike():0);
			        quote.setLoanQuoteVehicleCost(loanRequestedAmountCar+ loanRequestedAmountBike);
			        } 
		double netAnnualIncome = 0;
		if(quote.getLoanQuoteEmploymentTypeId()!=null){
			if(quote.getLoanQuoteEmploymentTypeId()==9){
				netAnnualIncome = (quote.getLoanQuoteNetMonthlySalary()!=null?quote.getLoanQuoteNetMonthlySalary():0)*12
						+(quote.getLoanQuoteVariableMonthPay()!=null?quote.getLoanQuoteVariableMonthPay():0)*12
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)*12
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0)*12;
			}else if(quote.getLoanQuoteEmploymentTypeId()==10 ||quote.getLoanQuoteEmploymentTypeId()==11){
				netAnnualIncome =(quote.getLoanQuoteProfitAfterTax()!=null?quote.getLoanQuoteProfitAfterTax():0) 
						+(quote.getLoanQuoteDepreciation()!=null?quote.getLoanQuoteDepreciation():0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)*12
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0)*12;
						
			}else if(quote.getLoanQuoteEmploymentTypeId()==12){
				netAnnualIncome = (quote.getLoanQuoteNetAnnualIncome()!=null?quote.getLoanQuoteNetAnnualIncome():0)
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)*12
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0)*12;
						
			}else if(quote.getLoanQuoteEmploymentTypeId()==13 ){
				netAnnualIncome = (quote.getLoanQuoteNetMonthlyPension()!=null?quote.getLoanQuoteNetMonthlyPension():0)*12
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)*12
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0)*12;
	
			}else if(quote.getLoanQuoteEmploymentTypeId()==14 ){
				netAnnualIncome = (quote.getLoanQuoteIncomeFromRegularSource()!=null?quote.getLoanQuoteIncomeFromRegularSource():0)*12
						+(quote.getLoanQuoteExpectedRentalIncome()!=null?quote.getLoanQuoteExpectedRentalIncome():0)*12
						+(quote.getLoanQuoteOtherIncome()!=null?quote.getLoanQuoteOtherIncome():0)*12;
			}
		}
		double netAnnualIncomeCoapplicant=0;
		if(quote.getLoanQuoteCoapplicantFirstId()!=null){
			netAnnualIncomeCoapplicant = (quote.getLoanQuoteCoapplicantFirstMonthlySalary()!=null?quote.getLoanQuoteCoapplicantFirstMonthlySalary():0)*12
					+(quote.getLoanQuoteCoapplicantFirstVariableMonthPay()!=null?quote.getLoanQuoteCoapplicantFirstVariableMonthPay():0)*12
					+(quote.getLoanQuoteCoapplicantFirstOtherIncome()!=null?quote.getLoanQuoteCoapplicantFirstOtherIncome():0)*12
					
					+(quote.getLoanQuoteCoapplicantFirstProfitAfterTax()!=null?quote.getLoanQuoteCoapplicantFirstProfitAfterTax():0)
					+(quote.getLoanQuoteCoapplicantFirstDepreciatiation()!=null?quote.getLoanQuoteCoapplicantFirstDepreciatiation():0)
					
					+(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome()!=null?quote.getLoanQuoteCoapplicantFirstNetAnnualIncome():0)
					+(quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome()!=null?quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome():0)*12
					
					+(quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome()!=null?quote.getLoanQuoteCoapplicantFirstExpectedRentalIncome():0)*12
					
					+(quote.getLoanQuoteCoapplicantFirstNetMonthlyPension()!=null?quote.getLoanQuoteCoapplicantFirstNetMonthlyPension():0)*12
					
					+(quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource()!=null?quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource():0)*12;
		}
		if(quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1){
			quote.setLoanQuoteNetIncome(netAnnualIncome +netAnnualIncomeCoapplicant);
		}else{
			quote.setLoanQuoteNetIncome(netAnnualIncome);
		}
		date = quote.getLoanQuoteCompanyJoiningDate();
		if(date!=null) {
			quote.setLoanQuoteYearCompanyJoining(DateUtil.getYearFromDate(date));
			quote.setLoanQuoteMonthCompanyJoining(DateUtil.getMonthFromDate(date));
		}
		return quote;
	}

	public ApplicationFormAutoLoan getApplicationFormAutoLoanByAppSeqId(Integer appSeqId) throws SQLException {
		ApplicationFormAutoLoan application = applicationFormAutoLoanDao.findById(appSeqId, ApplicationFormAutoLoan.class);
		if(application==null){
 			return null;
 		}
		application = convert(application);
		return application;
	}
	public ApplicationFormAutoLoan getApplicationFormAutoLoanByAppReferenceId(String appReferenceId)throws SQLException {
		ApplicationFormAutoLoan application = applicationFormAutoLoanDao.getApplicationFormAutoLoanByAppReferenceId(appReferenceId);
		if(application==null){
 			return null;
 		}
		application = convert(application);
		return application;
	}
	
	public ApplicationFormAutoLoan getApplicationFormAutoLoanByMobileAndSmsOtp(String appMobileNo,Integer appMobileVerificationCode) throws SQLException{ 
		return applicationFormAutoLoanDao.getApplicationFormAutoLoanByMobileAndSmsOtp(appMobileNo,appMobileVerificationCode);
	}
	
	public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException{
		return applicationFormAutoLoanDao.getLastGeneratedReferenceNumber(loanTypeId);
	}
	public boolean isReferenceIdAvailable(String appReferenceId) throws SQLException{
		return applicationFormAutoLoanDao.isReferenceIdAvailable(appReferenceId);
	}
	
	public AlProduct getAlProductById(Integer alProductId) throws SQLException{
		return alProductDao.getAlProductById(alProductId);
	}
	
	public String getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(Integer loanPurposeId,Integer loanTypeId) throws SQLException{
		return loanPurposeDao.getLoanPurposeNameByLoanPurposeIdAndLoanTypeId(loanPurposeId, loanTypeId);
	}
	
	public MasterCBSResponse getMasterCBSResponseObjectByCbsAppSeqId(Integer appSeqId) throws SQLException{
		return masterCBSResponseDao.getMasterCBSResponseObjectByCbsAppSeqId(appSeqId);
	}
	
	public MasterCBSResponse getMasterCBSResponseObjectByLoanTypeAppSeqId(Integer appSeqId) throws SQLException{
		return masterCBSResponseDao.getMasterCBSResponseObjectByLoanTypeAppSeqId(Constants.AUTO_LOAN_ID,appSeqId);
	}
	
	public MasterCBSCall getMasterCBSCallObjectByCbsResponseId(Integer cbsResponseId) throws SQLException, NoResultException{	
		return masterCBSCallDao.getMasterCBSCallObjectByCbsResponseId(cbsResponseId);
	}
	
	public CarCompanyDao getCarCompanyDao() {
		return carCompanyDao;
	}

	public void setCarCompanyDao(CarCompanyDao carCompanyDao) {
		this.carCompanyDao = carCompanyDao;
	}

	public CarModelDao getCarModelDao() {
		return carModelDao;
	}

	public void setCarModelDao(CarModelDao carModelDao) {
		this.carModelDao = carModelDao;
	}

	public CarVariantDao getCarVariantDao() {
		return carVariantDao;
	}

	public void setCarVariantDao(CarVariantDao carVariantDao) {
		this.carVariantDao = carVariantDao;
	}

	public ApplicationFormAutoLoanCallsDao getApplicationFormAutoLoanCallsDao() {
		return applicationFormAutoLoanCallsDao;
	}

	public void setApplicationFormAutoLoanCallsDao(
			ApplicationFormAutoLoanCallsDao applicationFormAutoLoanCallsDao) {
		this.applicationFormAutoLoanCallsDao = applicationFormAutoLoanCallsDao;
	}

	public AlProductDao getAlProductDao() {
		return alProductDao;
	}

	public void setAlProductDao(AlProductDao alProductDao) {
		this.alProductDao = alProductDao;
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
