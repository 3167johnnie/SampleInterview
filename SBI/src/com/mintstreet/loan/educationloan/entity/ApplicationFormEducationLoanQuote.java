package com.mintstreet.loan.educationloan.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mintstreet.common.entity.Domain;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00168")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="G7", sequenceName="RUPEEPOWER_OCAS_SEQ_00048" ,allocationSize=1)
public class ApplicationFormEducationLoanQuote  extends Domain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Transient
	private Double loanQuoteLoanRequestedAmount;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G7")
	@Column(name="QUOTE_SEQUENCE_ID")
	private Integer loanQuoteId;

	

	@Column(name="QUOTE_LOAN_TYPE")
	private  Integer loanQuoteLoanType;
	
	@Column(name="QUOTE_INSTITUTE_CATAGORY")
	private  String loanQuoteInstituteCat;

	@Column(name="QUOTE_LOAN_PURPOSE_ID")
	private Integer loanQuoteLoanPurposeId;

	@Column(name="QUOTE_LOAN_OPTED_LOAN")
	private String loanQuoteLoanOptedLoan;

	@Column(name="QUOTE_LOAN_WITH_MORA_PERIOD")
	private String loanQuoteLoanWithInMoratoriumPeriod;
	
	@Column(name="QUOTE_LOAN_WITH_BANK_ID")
	private Integer loanQuoteLoanWithBankId;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ST_DT_OF_LOAN_REPAYMENT")
	private Date loanQuoteSTDOfLoanRepayment;
	
	@Transient     
	private Integer loanQuoteYearstartDateOfloanRepayment;
	@Transient
	private Integer loanQuoteMonthstartDateOfloanRepayment;
	@Transient
	private String loanQuoteStartDateOfLoanRepayment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ST_DT_OF_APP_COURSE")
	private Date loanQuoteSTDOfAppliedCourse;
	@Transient
	private Integer loanQuoteYeardateOfAppliendCourse;
	@Transient 
	private Integer loanQuoteMonthdateOfAppliendCourse;
	@Transient
	private String loanQuoteStartDateOfAppliedCourse;
	
	@Column(name="QUOTE_COURSE_APPLIED")
	private String loanQuoteCourseApplied;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ST_DT_OF_CURRENT_LOAN")
	private Date loanQuoteStartDateOfCurrentLoan;

	
	

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_END_DATE_OF_CURR_LOAN")
	private Date loanQuoteEndDateOfCurrentLoan;

	@Transient 
	private Integer loanQuoteYearloanTenureEndDate;
	@Transient 
	private Integer loanQuoteMonthloanTenureEndDate;
	

	@Column(name="QUOTE_OUTSTANDING_LOAN_AMOUNT")
	private Double loanQuoteOutstandingLoanAmount;
	
	@Column(name="QUOTE_PREVIOUS_INSTITUTE_NAME")
	private String loanQuotePreviousInstituteName;
	
	@Column(name="QUOTE_COURSE_DURATION")
	private Integer loanQuoteCourseDuration;

	@Column(name="QUOTE_COUNTRY_OF_STUDY")
	private Integer loanQuoteCountryOfStudy;

	@Column(name="QUOTE_COUNTRY_OF_STUDY_ID")
	private Integer loanQuoteCountryOfStudyId;
	
	@Column(name="QUOTE_COURSE_TYPE_ID")
	private Integer loanQuoteCourseTypeId;
	
	@Column(name="QUOTE_COURSE_NAME")
	private String loanQuoteCourseName;
	
	@Column(name="QUOTE_COURSE_NAME_ID")
	private Integer loanQuoteCourseNameId;
	
	@Column(name="QUOTE_INSTITUTE_NAME")
	private String loanQuoteInstituteName;
	
	@Column(name="QUOTE_INSTITUTE_NAME_ID")
	private Integer loanQuoteInstituteNameId;
	
	@Column(name="QUOTE_UNIVERSITY_NAME_ID")
	private Integer loanQuoteUniversityNameId ;

	@Column(name="QUOTE_UNIVERSITY_NAME")
	private String loanQuoteUniversityName;
	
	@Column(name="QUOTE_NATURE_OF_COURSE_ID")
	private Integer loanQuoteNatureOfCourseId;

	@Column(name="QUOTE_COURSE_DURATION_YEAR")
	private Integer loanQuoteCourseDurationYears;

	@Column(name="QUOTE_COURSE_DURATION_MONTH")
	private Integer loanQuoteCourseDurationMonth;
	
	@Column(name="QUOTE_LAST_EDU_QUAL_ID")
	private Integer loanQuoteLastEducationalQualificationId;
	
	@Column(name="QUOTE_STATE_OF_STUDY")
	private Integer loanQuoteStateOfStudy;

	@Column(name="QUOTE_CITY_OF_STUDY")
	private Integer loanQuoteCityOfStudy;

	@Column(name="QUOTE_LOAN_QUTOTE_CERTI_ID")
	private Integer loanQuoteCertificateId;
	
	@Column(name="QUOTE_GRADUATION_TYPE_ID")
	private Integer loanQuoteGraduationTypeId;

	@Column(name="QUOTE_ADMISSION_FEE")
	private Double loanQuoteAdmissionFee;

	@Column(name="QUOTE_TRAVEL_EXPENSES")
	private Double loanQuoteTravelExpenses;
	
	@Column(name="QUOTE_DEPOSIT_FEE")
	private Double loanQuotedepositFee;
	
	@Column(name="QUOTE_TUITION_FEE")
	private Double loanQuoteTuitionFee;

	@Column(name="QUOTE_OTHER_MISC_CHARGE_BIKE")
	private Double loanQuoteOtherMiscChargeBike;

	@Column(name="QUOTE_SCHOLARSHIP")
	private Double loanQuoteScholarship;

	@Column(name="QUOTE_COLLATERAL")
	private Double loanQuoteCollateral;

	@Column(name="QUOTE_PUR_OF_BOOK_AND_EQUP")
	private Double loanQuotePurchaseOfBookAndEquipment;

	@Column(name="QUOTE_CAUTION_FEE")
	private Double loanQuoteCautionFee;
	
	@Column(name="QUOTE_PUR_OF_TWO_WHEELER")
	private Double loanQuotePurchaseOfTwoWheeler;
	
	@Column(name="QUOTE_PUR_OF_COMPUTER")
	private Double loanQuotePurchaseOfComputer;

	@Column(name="QUOTE_HOSTEL_FEE")
	private Double loanQuoteHostelFee;
	
	@Column(name="QUOTE_PREMIUM")
	private Double loanQuotePremium;
	
	@Column(name="QUOTE_OTHER_EXPENSES")
	private Double loanQuoteOtherExpenses;

	@Column(name="QUOTE_EXAMINATION_LAB_LIB_FEE")
	private Double loanQuoteExaminationLabOrLibFee;

	@Column(name="QUOTE_OVERSEAS_INSURANCE_FEE")
	private double loanQuoteOverseasHealthInsuranceFee;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_DATE_OF_BIRTH")
	private Date loanQuoteDateOfBirthDT;
	
	@Transient
	private String loanQuoteDateOfBirth;
	
	@Transient
	private Integer loanQuoteAge;

	@Column(name="QUOTE_GENDER")
	private String loanQuoteGender;
	
	@Column(name="QUOTE_RESIDENT_TYPE_ID")
	private Integer loanQuoteResidentTypeId;

	@Column(name="QUOTE_RESIDENT_COUNTRY_ID")
	private Integer loanQuoteResidentCountryId;
	
	@Column(name="QUOTE_RESIDENT_STATE_ID")
	private Integer loanQuoteResidentStateId;
	
	@Column(name="QUOTE_RESIDENT_CITY_ID")
	private Integer loanQuoteResidentCityId;
	
	@Column(name = "QUOTE_LOCALITY_ID")
	private Integer loanQuoteLocalityId;

	@Column(name = "QUOTE_DISTRICT_ID")
	private Integer loanQuoteDistrictId;

	@Column(name = "QUOTE_BRANCH_ID")
	private Integer loanQuoteBranchId;

	@Column(name="QUOTE_WORK_EXPERIENCE")
	private Integer loanQuoteWorkExperience;

	

	@Column(name="QUOTE_IS_SBI_EMPLOYEE")
	private String loanQuoteisSbiEmployee;
	
	@Column(name="QUOTE_COAPP_FST_EMP_TYPE_ID")
	private Integer loanQuoteCoapplicantFirstEmploymentTypeId;

	@Column(name="QUOTE_COAPP_FST_SA_WITH_SBI")
	private String loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;

	@Column(name="QUOTE_COAPP_FST_EMPLOYER_NAME")
	private String loanQuoteCoapplicantFirstCoEmployerName;

	@Column(name="QUOTE_COAPP_FST_NET_ANN_INC")
	private Double loanQuoteCoapplicantFirstNetAnnualIncome;

	@Column(name="QUOTE_COAPP_FST_REL_ID")
	private Integer loanQuoteCoapplicantFirstRelationshipId;

	@Column(name="QUOTE_CREATED_LMS_USER_ID")
	private Integer loanQuoteCreatedLmsUserId;

	@Column(name="QUOTE_LEAD_ID")
	private Integer loanQuoteLeadId;

	@Column(name="QUOTE_LOAN_ACC_TYPE")
	private Integer loanQuoteLoanAccountType;

	@Column(name="QUOTE_LOAN_AMOUNT")
	private Double loanQuoteLoanAmount;
	
	@Column(name="QUOTE_LOAN_PRODUCT_ID")
	private Integer loanQuoteLoanProductId;

	@Column(name="QUOTE_LOAN_TENURE")
	private Integer loanQuoteLoanTenure;
	
	@Transient
	private String loanQuoteSbiLifeInsuranceCheck;
	
	@Transient
	private String loanQuoteMoratoriumCheck;

	@Column(name="QUOTE_APPLIED_COUPON")
	private String loanQuoteAppliedCoupon;

	@Column(name="QUOTE_VISIT_ID")
	private Integer loanQuoteVisitId;

	@Column(name = "QUOTE_NEW_VISIT_ID")
	private Integer loanQuoteNewVisitId;

	@Column(name="QUOTE_IP_ADDRESS")
	private String loanQuoteIpAddress;
	
	@Column(name="QUOTE_BROWSER_NAME")
	private String loanQuoteBrowserName;

	

	@Column(name="QUOTE_SOURCE_ID")
	private Integer loanQuoteSourceId;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ENTRY_TIME")
	private Date loanQuoteEntryTime;
	
	@Transient
	private Integer residentDistrictId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="QUOTE_UPDATED_TIME")
	private Date loanQuoteUpdatedTime;	


	

	@Column(name="QUOTE_ACTIVE")
	private String loanQuoteActive;

	@Column(name="QUOTE_DELETED")
	private String loanQuoteDeleted;

	@Column(name="QUOTE_PREFERRED_LOCATION")
	private Integer loanQuotePreferredLocation;
	
	@Transient
	private Double loanQuoteNetIncome;
	
	@Column(name = "QUOTE_FIRST_NAME")
	private String loanQuoteFirstName;
	
	@Transient
	private int appApplyingFrom;
	
	@Transient
	private String appISDCode;
	
	@Transient
	private String appNRIMobileNo;
	
	@Transient
	private String appMobile;
	
	@Transient
	private String appEmail;

	@Transient
	private double loanQuoteRequestLoanAmount;
	
	@Column(name = "QUOTE_ADMISSION_QUOTA")
	private Integer loanQuoteAdmissionQuota;
	
	@Column(name="QUOTE_EXIT_EL_ST_DATE_YEAR")
	private Integer loanQuoteYearExistingEducationLoanStartDate;
					
	@Column(name="QUOTE_EXIT_EL_ST_DATE_MONTH")
	private Integer loanQuoteMonthExistingEducationLoanStartDate;
	
	
	@Column(name="QUOTE_EXIT_EL_TENOR_DATE_YEAR")
	private Integer loanQuoteYearExistingEducationLoanEndDate;
	
	
	@Column(name="QUOTE_EXIT_EL_END_DATE_MONTH")
	private Integer loanQuoteMonthExistingEducationLoanEndDate;
					
	@Column(name="QUOTE_CURRENT_EMI_BEING_PAYED")
	private Double loanQuoteCurrentEMIBeingPayed;
	
	@Column(name="QUOTE_EXIT_EL_TOPUP_LOAN_AMT")
	private Double loanQuoteExistingEducationTopupLoanAmount;
	
	@Column(name="QUOTE_PRE_PAYMENT_PENALTY")
	private Double loanQuotePrePaymentPenalty;
	
	@Column(name="QUOTE_LOAN_TRANSFERRED")
	private String loanQuoteLoanTransferred;
	
	@Column(name="QUOTE_EXIST_LOAN_DISBURSED")
	private String loanQuoteExistingLoanDisbursed;
	
	@Column(name="QUOTE_COLLETRAL_TYPE_ID")
	private Integer loanQuoteCollateralTypeId;
	
	@Column(name="QUOTE_COLLETRAL_AMOUNT")
	private Double loanQuoteCollateralAmount;
	
	@Column(name="QUOTE_BANK_EXIST_EL_LOAN_ID")
	private Integer loanQuoteBankWhichHasExistingEducationLoan;
	
	@Column(name="QUOTE_LOAN_COUNTRY_ID")
	private Integer loanQuoteCountryId;
	
	@Column(name="QUOTE_LOAN_APPLYING_FROM")
	private Integer loanQuoteApplyingFrom;
	
	@Column(name="QUOTE_PRE_EMI")
	private Double loanQuotePreEmi;
	
	@Column(name="QUOTE_WARD_ASSAM_EMPLOYEE")
	private Integer loanQuoteAssamEmp;

	@Column(name="QUOTE_BRANCH_IS_BIDYA")
	private Integer loanQuoteIsBranchEligibleForBidyalakmi;

	@Transient
	private String alternateMobileNumber;
	  
	@Transient
	private String appAltISDCode;
	  
	@Transient
	private Integer loanQuoteConsentId;

	  
	public double getLoanQuoteRequestLoanAmount() {
		return loanQuoteRequestLoanAmount;
	}

	public void setLoanQuoteRequestLoanAmount(double loanQuoteRequestLoanAmount) {
		this.loanQuoteRequestLoanAmount = loanQuoteRequestLoanAmount;
	}

	public Integer getLoanQuoteAge() {
		return loanQuoteAge;
	}

	public void setLoanQuoteAge(Integer loanQuoteAge) {
		this.loanQuoteAge = loanQuoteAge;
	}

	public ApplicationFormEducationLoanQuote() {
		
	}

	public Double getLoanQuoteLoanRequestedAmount() {
		return loanQuoteLoanRequestedAmount;
	}

	public void setLoanQuoteLoanRequestedAmount(Double loanQuoteLoanRequestedAmount) {
		this.loanQuoteLoanRequestedAmount = loanQuoteLoanRequestedAmount;
	}

	public Integer getLoanQuoteId() {
		return loanQuoteId;
	}

	public void setLoanQuoteId(Integer loanQuoteId) {
		this.loanQuoteId = loanQuoteId;
	}

	

	public Integer getLoanQuoteLoanType() {
		return loanQuoteLoanType;
	}

	public void setLoanQuoteLoanType(Integer loanQuoteLoanType) {
		this.loanQuoteLoanType = loanQuoteLoanType;
	}

	public String getLoanQuoteInstituteCat() {
		return loanQuoteInstituteCat;
	}

	public void setLoanQuoteInstituteCat(String loanQuoteInstituteCat) {
		this.loanQuoteInstituteCat = loanQuoteInstituteCat;
	}

	public Integer getLoanQuoteLoanPurposeId() {
		return loanQuoteLoanPurposeId;
	}

	public void setLoanQuoteLoanPurposeId(Integer loanQuoteLoanPurposeId) {
		this.loanQuoteLoanPurposeId = loanQuoteLoanPurposeId;
	}

	public String getLoanQuoteLoanOptedLoan() {
		return loanQuoteLoanOptedLoan;
	}

	public void setLoanQuoteLoanOptedLoan(String loanQuoteLoanOptedLoan) {
		this.loanQuoteLoanOptedLoan = loanQuoteLoanOptedLoan;
	}

	public Integer getLoanQuoteLoanWithBankId() {
		return loanQuoteLoanWithBankId;
	}

	public void setLoanQuoteLoanWithBankId(Integer loanQuoteLoanWithBankId) {
		this.loanQuoteLoanWithBankId = loanQuoteLoanWithBankId;
	}

	public Integer getLoanQuoteYearstartDateOfloanRepayment() {
		return loanQuoteYearstartDateOfloanRepayment;
	}

	public void setLoanQuoteYearstartDateOfloanRepayment(
			Integer loanQuoteYearstartDateOfloanRepayment) {
		this.loanQuoteYearstartDateOfloanRepayment = loanQuoteYearstartDateOfloanRepayment;
	}

	public Integer getLoanQuoteMonthstartDateOfloanRepayment() {
		return loanQuoteMonthstartDateOfloanRepayment;
	}

	public void setLoanQuoteMonthstartDateOfloanRepayment(
			Integer loanQuoteMonthstartDateOfloanRepayment) {
		this.loanQuoteMonthstartDateOfloanRepayment = loanQuoteMonthstartDateOfloanRepayment;
	}

	public Integer getLoanQuoteYeardateOfAppliendCourse() {
		return loanQuoteYeardateOfAppliendCourse;
	}

	public void setLoanQuoteYeardateOfAppliendCourse(
			Integer loanQuoteYeardateOfAppliendCourse) {
		this.loanQuoteYeardateOfAppliendCourse = loanQuoteYeardateOfAppliendCourse;
	}

	public Integer getLoanQuoteMonthdateOfAppliendCourse() {
		return loanQuoteMonthdateOfAppliendCourse;
	}

	public void setLoanQuoteMonthdateOfAppliendCourse(
			Integer loanQuoteMonthdateOfAppliendCourse) {
		this.loanQuoteMonthdateOfAppliendCourse = loanQuoteMonthdateOfAppliendCourse;
	}

	public String getLoanQuoteCourseApplied() {
		return loanQuoteCourseApplied;
	}

	public void setLoanQuoteCourseApplied(String loanQuoteCourseApplied) {
		this.loanQuoteCourseApplied = loanQuoteCourseApplied;
	}

	public Date getLoanQuoteStartDateOfCurrentLoan() {
		return loanQuoteStartDateOfCurrentLoan;
	}

	public void setLoanQuoteStartDateOfCurrentLoan(
			Date loanQuoteStartDateOfCurrentLoan) {
		this.loanQuoteStartDateOfCurrentLoan = loanQuoteStartDateOfCurrentLoan;
	}

	public Integer getLoanQuoteCourseDurationYears() {
		return loanQuoteCourseDurationYears;
	}

	public void setLoanQuoteCourseDurationYears(Integer loanQuoteCourseDurationYears) {
		this.loanQuoteCourseDurationYears = loanQuoteCourseDurationYears;
	}

	public Integer getLoanQuoteCourseDurationMonth() {
		return loanQuoteCourseDurationMonth;
	}

	public void setLoanQuoteCourseDurationMonth(Integer loanQuoteCourseDurationMonth) {
		this.loanQuoteCourseDurationMonth = loanQuoteCourseDurationMonth;
	}

	public Date getLoanQuoteEndDateOfCurrentLoan() {
		return loanQuoteEndDateOfCurrentLoan;
	}

	public void setLoanQuoteEndDateOfCurrentLoan(Date loanQuoteEndDateOfCurrentLoan) {
		this.loanQuoteEndDateOfCurrentLoan = loanQuoteEndDateOfCurrentLoan;
	}

	public Integer getLoanQuoteYearloanTenureEndDate() {
		return loanQuoteYearloanTenureEndDate;
	}

	public void setLoanQuoteYearloanTenureEndDate(
			Integer loanQuoteYearloanTenureEndDate) {
		this.loanQuoteYearloanTenureEndDate = loanQuoteYearloanTenureEndDate;
	}

	public Integer getLoanQuoteMonthloanTenureEndDate() {
		return loanQuoteMonthloanTenureEndDate;
	}

	public void setLoanQuoteMonthloanTenureEndDate(
			Integer loanQuoteMonthloanTenureEndDate) {
		this.loanQuoteMonthloanTenureEndDate = loanQuoteMonthloanTenureEndDate;
	}

	public Double getLoanQuoteOutstandingLoanAmount() {
		return loanQuoteOutstandingLoanAmount;
	}

	public void setLoanQuoteOutstandingLoanAmount(
			Double loanQuoteOutstandingLoanAmount) {
		this.loanQuoteOutstandingLoanAmount = loanQuoteOutstandingLoanAmount;
	}

	public Integer getLoanQuoteCourseDuration() {
		return loanQuoteCourseDuration;
	}

	public void setLoanQuoteCourseDuration(Integer loanQuoteCourseDuration) {
		this.loanQuoteCourseDuration = loanQuoteCourseDuration;
	}

	public Integer getLoanQuoteCountryOfStudy() {
		return loanQuoteCountryOfStudy;
	}

	public void setLoanQuoteCountryOfStudy(Integer loanQuoteCountryOfStudy) {
		this.loanQuoteCountryOfStudy = loanQuoteCountryOfStudy;
	}

	public Integer getLoanQuoteCountryOfStudyId() {
		return loanQuoteCountryOfStudyId;
	}

	public void setLoanQuoteCountryOfStudyId(Integer loanQuoteCountryOfStudyId) {
		this.loanQuoteCountryOfStudyId = loanQuoteCountryOfStudyId;
	}

	public Integer getLoanQuoteStateOfStudy() {
		return loanQuoteStateOfStudy;
	}

	public void setLoanQuoteStateOfStudy(Integer loanQuoteStateOfStudy) {
		this.loanQuoteStateOfStudy = loanQuoteStateOfStudy;
	}

	public Integer getLoanQuoteCityOfStudy() {
		return loanQuoteCityOfStudy;
	}

	public void setLoanQuoteCityOfStudy(Integer loanQuoteCityOfStudy) {
		this.loanQuoteCityOfStudy = loanQuoteCityOfStudy;
	}

	public Integer getLoanQuoteLastEducationalQualificationId() {
		return loanQuoteLastEducationalQualificationId;
	}

	public void setLoanQuoteLastEducationalQualificationId(
			Integer loanQuoteLastEducationalQualificationId) {
		this.loanQuoteLastEducationalQualificationId = loanQuoteLastEducationalQualificationId;
	}

	public Integer getLoanQuoteCourseTypeId() {
		return loanQuoteCourseTypeId;
	}

	public void setLoanQuoteCourseTypeId(Integer loanQuoteCourseTypeId) {
		this.loanQuoteCourseTypeId = loanQuoteCourseTypeId;
	}

	public Integer getLoanQuoteInstituteNameId() {
		return loanQuoteInstituteNameId;
	}

	public void setLoanQuoteInstituteNameId(Integer loanQuoteInstituteNameId) {
		this.loanQuoteInstituteNameId = loanQuoteInstituteNameId;
	}

	public Integer getLoanQuoteCertificateId() {
		return loanQuoteCertificateId;
	}

	public void setLoanQuoteCertificateId(Integer loanQuoteCertificateId) {
		this.loanQuoteCertificateId = loanQuoteCertificateId;
	}

	public Integer getLoanQuoteGraduationTypeId() {
		return loanQuoteGraduationTypeId;
	}

	public void setLoanQuoteGraduationTypeId(Integer loanQuoteGraduationTypeId) {
		this.loanQuoteGraduationTypeId = loanQuoteGraduationTypeId;
	}

	public Integer getLoanQuoteNatureOfCourseId() {
		return loanQuoteNatureOfCourseId;
	}

	public void setLoanQuoteNatureOfCourseId(Integer loanQuoteNatureOfCourseId) {
		this.loanQuoteNatureOfCourseId = loanQuoteNatureOfCourseId;
	}

	public Double getLoanQuoteAdmissionFee() {
		return loanQuoteAdmissionFee;
	}

	public void setLoanQuoteAdmissionFee(Double loanQuoteAdmissionFee) {
		this.loanQuoteAdmissionFee = loanQuoteAdmissionFee;
	}

	public Double getLoanQuoteTravelExpenses() {
		return loanQuoteTravelExpenses;
	}

	public void setLoanQuoteTravelExpenses(Double loanQuoteTravelExpenses) {
		this.loanQuoteTravelExpenses = loanQuoteTravelExpenses;
	}

	public Double getLoanQuotedepositFee() {
		return loanQuotedepositFee;
	}

	public void setLoanQuotedepositFee(Double loanQuotedepositFee) {
		this.loanQuotedepositFee = loanQuotedepositFee;
	}

	public Double getLoanQuoteTuitionFee() {
		return loanQuoteTuitionFee;
	}

	public void setLoanQuoteTuitionFee(Double loanQuoteTuitionFee) {
		this.loanQuoteTuitionFee = loanQuoteTuitionFee;
	}

	public Double getLoanQuoteOtherMiscChargeBike() {
		return loanQuoteOtherMiscChargeBike;
	}

	public void setLoanQuoteOtherMiscChargeBike(Double loanQuoteOtherMiscChargeBike) {
		this.loanQuoteOtherMiscChargeBike = loanQuoteOtherMiscChargeBike;
	}

	public Double getLoanQuotePurchaseOfBookAndEquipment() {
		return loanQuotePurchaseOfBookAndEquipment;
	}

	public void setLoanQuotePurchaseOfBookAndEquipment(
			Double loanQuotePurchaseOfBookAndEquipment) {
		this.loanQuotePurchaseOfBookAndEquipment = loanQuotePurchaseOfBookAndEquipment;
	}

	public Double getLoanQuotePurchaseOfTwoWheeler() {
		return loanQuotePurchaseOfTwoWheeler;
	}

	public void setLoanQuotePurchaseOfTwoWheeler(
			Double loanQuotePurchaseOfTwoWheeler) {
		this.loanQuotePurchaseOfTwoWheeler = loanQuotePurchaseOfTwoWheeler;
	}

	public Double getLoanQuotePurchaseOfComputer() {
		return loanQuotePurchaseOfComputer;
	}

	public void setLoanQuotePurchaseOfComputer(Double loanQuotePurchaseOfComputer) {
		this.loanQuotePurchaseOfComputer = loanQuotePurchaseOfComputer;
	}

	public Double getLoanQuoteHostelFee() {
		return loanQuoteHostelFee;
	}

	public void setLoanQuoteHostelFee(Double loanQuoteHostelFee) {
		this.loanQuoteHostelFee = loanQuoteHostelFee;
	}

	public Double getLoanQuotePremium() {
		return loanQuotePremium;
	}

	public void setLoanQuotePremium(Double loanQuotePremium) {
		this.loanQuotePremium = loanQuotePremium;
	}

	public Double getLoanQuoteOtherExpenses() {
		return loanQuoteOtherExpenses;
	}

	public void setLoanQuoteOtherExpenses(Double loanQuoteOtherExpenses) {
		this.loanQuoteOtherExpenses = loanQuoteOtherExpenses;
	}

	public Double getLoanQuoteExaminationLabOrLibFee() {
		return loanQuoteExaminationLabOrLibFee;
	}

	public void setLoanQuoteExaminationLabOrLibFee(
			Double loanQuoteExaminationLabOrLibFee) {
		this.loanQuoteExaminationLabOrLibFee = loanQuoteExaminationLabOrLibFee;
	}

	public String getLoanQuoteGender() {
		return loanQuoteGender;
	}

	public void setLoanQuoteGender(String loanQuoteGender) {
		this.loanQuoteGender = loanQuoteGender;
	}

	public Integer getLoanQuoteResidentTypeId() {
		return loanQuoteResidentTypeId;
	}

	public void setLoanQuoteResidentTypeId(Integer loanQuoteResidentTypeId) {
		this.loanQuoteResidentTypeId = loanQuoteResidentTypeId;
	}

	public Integer getLoanQuoteResidentCountryId() {
		return loanQuoteResidentCountryId;
	}

	public void setLoanQuoteResidentCountryId(Integer loanQuoteResidentCountryId) {
		this.loanQuoteResidentCountryId = loanQuoteResidentCountryId;
	}

	public Integer getLoanQuoteResidentStateId() {
		return loanQuoteResidentStateId;
	}

	public void setLoanQuoteResidentStateId(Integer loanQuoteResidentStateId) {
		this.loanQuoteResidentStateId = loanQuoteResidentStateId;
	}

	public Integer getLoanQuoteResidentCityId() {
		return loanQuoteResidentCityId;
	}

	public void setLoanQuoteResidentCityId(Integer loanQuoteResidentCityId) {
		this.loanQuoteResidentCityId = loanQuoteResidentCityId;
	}

	public Integer getLoanQuoteWorkExperience() {
		return loanQuoteWorkExperience;
	}

	public void setLoanQuoteWorkExperience(Integer loanQuoteWorkExperience) {
		this.loanQuoteWorkExperience = loanQuoteWorkExperience;
	}

	

	public String getLoanQuoteisSbiEmployee() {
		return loanQuoteisSbiEmployee;
	}

	public void setLoanQuoteisSbiEmployee(String loanQuoteisSbiEmployee) {
		this.loanQuoteisSbiEmployee = loanQuoteisSbiEmployee;
	}

	public Integer getLoanQuoteCoapplicantFirstEmploymentTypeId() {
		return loanQuoteCoapplicantFirstEmploymentTypeId;
	}

	public void setLoanQuoteCoapplicantFirstEmploymentTypeId(
			Integer loanQuoteCoapplicantFirstEmploymentTypeId) {
		this.loanQuoteCoapplicantFirstEmploymentTypeId = loanQuoteCoapplicantFirstEmploymentTypeId;
	}

	public String getLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi() {
		return loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;
	}

	public void setLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi(
			String loanQuoteCoapplicantFirstCoSalaryAccountWithSbi) {
		this.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi = loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;
	}

	public String getLoanQuoteCoapplicantFirstCoEmployerName() {
		return loanQuoteCoapplicantFirstCoEmployerName;
	}

	public void setLoanQuoteCoapplicantFirstCoEmployerName(
			String loanQuoteCoapplicantFirstCoEmployerName) {
		this.loanQuoteCoapplicantFirstCoEmployerName = loanQuoteCoapplicantFirstCoEmployerName;
	}

	public Double getLoanQuoteCoapplicantFirstNetAnnualIncome() {
		return loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public void setLoanQuoteCoapplicantFirstNetAnnualIncome(
			Double loanQuoteCoapplicantFirstNetAnnualIncome) {
		this.loanQuoteCoapplicantFirstNetAnnualIncome = loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public Integer getLoanQuoteCoapplicantFirstRelationshipId() {
		return loanQuoteCoapplicantFirstRelationshipId;
	}

	public void setLoanQuoteCoapplicantFirstRelationshipId(
			Integer loanQuoteCoapplicantFirstRelationshipId) {
		this.loanQuoteCoapplicantFirstRelationshipId = loanQuoteCoapplicantFirstRelationshipId;
	}

	public Integer getLoanQuoteCreatedLmsUserId() {
		return loanQuoteCreatedLmsUserId;
	}

	public void setLoanQuoteCreatedLmsUserId(Integer loanQuoteCreatedLmsUserId) {
		this.loanQuoteCreatedLmsUserId = loanQuoteCreatedLmsUserId;
	}

	public Integer getLoanQuoteLeadId() {
		return loanQuoteLeadId;
	}

	public void setLoanQuoteLeadId(Integer loanQuoteLeadId) {
		this.loanQuoteLeadId = loanQuoteLeadId;
	}

	public Integer getLoanQuoteLoanAccountType() {
		return loanQuoteLoanAccountType;
	}

	public void setLoanQuoteLoanAccountType(Integer loanQuoteLoanAccountType) {
		this.loanQuoteLoanAccountType = loanQuoteLoanAccountType;
	}

	public Double getLoanQuoteLoanAmount() {
		return loanQuoteLoanAmount;
	}

	public void setLoanQuoteLoanAmount(Double loanQuoteLoanAmount) {
		this.loanQuoteLoanAmount = loanQuoteLoanAmount;
	}

	public Integer getLoanQuoteLoanProductId() {
		return loanQuoteLoanProductId;
	}

	public void setLoanQuoteLoanProductId(Integer loanQuoteLoanProductId) {
		this.loanQuoteLoanProductId = loanQuoteLoanProductId;
	}

	public Integer getLoanQuoteLoanTenure() {
		return loanQuoteLoanTenure;
	}

	public void setLoanQuoteLoanTenure(Integer loanQuoteLoanTenure) {
		this.loanQuoteLoanTenure = loanQuoteLoanTenure;
	}

	public String getLoanQuoteAppliedCoupon() {
		return loanQuoteAppliedCoupon;
	}

	public void setLoanQuoteAppliedCoupon(String loanQuoteAppliedCoupon) {
		this.loanQuoteAppliedCoupon = loanQuoteAppliedCoupon;
	}

	public Integer getLoanQuoteVisitId() {
		return loanQuoteVisitId;
	}

	public void setLoanQuoteVisitId(Integer loanQuoteVisitId) {
		this.loanQuoteVisitId = loanQuoteVisitId;
	}

	public String getLoanQuoteIpAddress() {
		return loanQuoteIpAddress;
	}

	public void setLoanQuoteIpAddress(String loanQuoteIpAddress) {
		this.loanQuoteIpAddress = loanQuoteIpAddress;
	}

	public String getLoanQuoteBrowserName() {
		return loanQuoteBrowserName;
	}

	public void setLoanQuoteBrowserName(String loanQuoteBrowserName) {
		this.loanQuoteBrowserName = loanQuoteBrowserName;
	}

	

	public Integer getLoanQuoteSourceId() {
		return loanQuoteSourceId;
	}

	public void setLoanQuoteSourceId(Integer loanQuoteSourceId) {
		this.loanQuoteSourceId = loanQuoteSourceId;
	}

	public Date getLoanQuoteEntryTime() {
		return loanQuoteEntryTime;
	}

	public void setLoanQuoteEntryTime(Date loanQuoteEntryTime) {
		this.loanQuoteEntryTime = loanQuoteEntryTime;
	}

	public Date getLoanQuoteUpdatedTime() {
		return loanQuoteUpdatedTime;
	}

	public void setLoanQuoteUpdatedTime(Date loanQuoteUpdatedTime) {
		this.loanQuoteUpdatedTime = loanQuoteUpdatedTime;
	}

	

	public String getLoanQuoteActive() {
		return loanQuoteActive;
	}

	public void setLoanQuoteActive(String loanQuoteActive) {
		this.loanQuoteActive = loanQuoteActive;
	}

	public String getLoanQuoteDeleted() {
		return loanQuoteDeleted;
	}

	public void setLoanQuoteDeleted(String loanQuoteDeleted) {
		this.loanQuoteDeleted = loanQuoteDeleted;
	}

	public Double getLoanQuoteNetIncome() {
		return loanQuoteNetIncome;
	}

	public void setLoanQuoteNetIncome(Double loanQuoteNetIncome) {
		this.loanQuoteNetIncome = loanQuoteNetIncome;
	}

	public String getAppMobile() {
		return appMobile;
	}

	public void setAppMobile(String appMobile) {
		this.appMobile = appMobile;
	}

	public String getAppEmail() {
		return appEmail;
	}

	public void setAppEmail(String appEmail) {
		this.appEmail = appEmail;
	}

	public Integer getResidentDistrictId() {
		return residentDistrictId;
	}

	public void setResidentDistrictId(Integer residentDistrictId) {
		this.residentDistrictId = residentDistrictId;
	}


	public Date getLoanQuoteSTDOfLoanRepayment() {
		return loanQuoteSTDOfLoanRepayment;
	}

	public void setLoanQuoteSTDOfLoanRepayment(Date loanQuoteSTDOfLoanRepayment) {
		this.loanQuoteSTDOfLoanRepayment = loanQuoteSTDOfLoanRepayment;
	}

	public String getLoanQuoteStartDateOfLoanRepayment() {
		return loanQuoteStartDateOfLoanRepayment;
	}

	public void setLoanQuoteStartDateOfLoanRepayment(
			String loanQuoteStartDateOfLoanRepayment) {
		this.loanQuoteStartDateOfLoanRepayment = loanQuoteStartDateOfLoanRepayment;
	}

	public Date getLoanQuoteSTDOfAppliedCourse() {
		return loanQuoteSTDOfAppliedCourse;
	}

	public void setLoanQuoteSTDOfAppliedCourse(Date loanQuoteSTDOfAppliedCourse) {
		this.loanQuoteSTDOfAppliedCourse = loanQuoteSTDOfAppliedCourse;
	}

	public String getLoanQuoteStartDateOfAppliedCourse() {
		return loanQuoteStartDateOfAppliedCourse;
	}

	public void setLoanQuoteStartDateOfAppliedCourse(
			String loanQuoteStartDateOfAppliedCourse) {
		this.loanQuoteStartDateOfAppliedCourse = loanQuoteStartDateOfAppliedCourse;
	}

	public String getLoanQuoteSbiLifeInsuranceCheck() {
		return loanQuoteSbiLifeInsuranceCheck;
	}

	public void setLoanQuoteSbiLifeInsuranceCheck(
			String loanQuoteSbiLifeInsuranceCheck) {
		this.loanQuoteSbiLifeInsuranceCheck = loanQuoteSbiLifeInsuranceCheck;
	}

	public String getLoanQuoteMoratoriumCheck() {
		return loanQuoteMoratoriumCheck;
	}

	public void setLoanQuoteMoratoriumCheck(String loanQuoteMoratoriumCheck) {
		this.loanQuoteMoratoriumCheck = loanQuoteMoratoriumCheck;
	}

	public String getLoanQuoteCourseName() {
		return loanQuoteCourseName;
	}

	public void setLoanQuoteCourseName(String loanQuoteCourseName) {
		this.loanQuoteCourseName = loanQuoteCourseName;
	}

	public Integer getLoanQuoteCourseNameId() {
		return loanQuoteCourseNameId;
	}

	public void setLoanQuoteCourseNameId(Integer loanQuoteCourseNameId) {
		this.loanQuoteCourseNameId = loanQuoteCourseNameId;
	}

	public String getLoanQuoteInstituteName() {
		return loanQuoteInstituteName;
	}

	public void setLoanQuoteInstituteName(String loanQuoteInstituteName) {
		this.loanQuoteInstituteName = loanQuoteInstituteName;
	}

	public Integer getLoanQuoteUniversityNameId() {
		return loanQuoteUniversityNameId;
	}

	public void setLoanQuoteUniversityNameId(Integer loanQuoteUniversityNameId) {
		this.loanQuoteUniversityNameId = loanQuoteUniversityNameId;
	}

	public String getLoanQuoteUniversityName() {
		return loanQuoteUniversityName;
	}

	public void setLoanQuoteUniversityName(String loanQuoteUniversityName) {
		this.loanQuoteUniversityName = loanQuoteUniversityName;
	}

	public Double getLoanQuoteCautionFee() {
		return loanQuoteCautionFee;
	}

	public void setLoanQuoteCautionFee(Double loanQuoteCautionFee) {
		this.loanQuoteCautionFee = loanQuoteCautionFee;
	}

	public String getLoanQuoteLoanWithInMoratoriumPeriod() {
		return loanQuoteLoanWithInMoratoriumPeriod;
	}

	public void setLoanQuoteLoanWithInMoratoriumPeriod(
			String loanQuoteLoanWithInMoratoriumPeriod) {
		this.loanQuoteLoanWithInMoratoriumPeriod = loanQuoteLoanWithInMoratoriumPeriod;
	}

	public double getLoanQuoteOverseasHealthInsuranceFee() {
		return loanQuoteOverseasHealthInsuranceFee;
	}

	public void setLoanQuoteOverseasHealthInsuranceFee(
			double loanQuoteOverseasHealthInsuranceFee) {
		this.loanQuoteOverseasHealthInsuranceFee = loanQuoteOverseasHealthInsuranceFee;
	}

	public String getLoanQuotePreviousInstituteName() {
		return loanQuotePreviousInstituteName;
	}

	public void setLoanQuotePreviousInstituteName(
			String loanQuotePreviousInstituteName) {
		this.loanQuotePreviousInstituteName = loanQuotePreviousInstituteName;
	}

	public Integer getLoanQuoteLocalityId() {
		return loanQuoteLocalityId;
	}

	public void setLoanQuoteLocalityId(Integer loanQuoteLocalityId) {
		this.loanQuoteLocalityId = loanQuoteLocalityId;
	}

	public Integer getLoanQuoteDistrictId() {
		return loanQuoteDistrictId;
	}

	public void setLoanQuoteDistrictId(Integer loanQuoteDistrictId) {
		this.loanQuoteDistrictId = loanQuoteDistrictId;
	}

	public Integer getLoanQuoteBranchId() {
		return loanQuoteBranchId;
	}

	public void setLoanQuoteBranchId(Integer loanQuoteBranchId) {
		this.loanQuoteBranchId = loanQuoteBranchId;
	}

	public Double getLoanQuoteScholarship() {
		return loanQuoteScholarship;
	}

	public void setLoanQuoteScholarship(Double loanQuoteScholarship) {
		this.loanQuoteScholarship = loanQuoteScholarship;
	}

	public Double getLoanQuoteCollateral() {
		return loanQuoteCollateral;
	}

	public void setLoanQuoteCollateral(Double loanQuoteCollateral) {
		this.loanQuoteCollateral = loanQuoteCollateral;
	}

	public Integer getLoanQuotePreferredLocation() {
		return loanQuotePreferredLocation;
	}

	public void setLoanQuotePreferredLocation(Integer loanQuotePreferredLocation) {
		this.loanQuotePreferredLocation = loanQuotePreferredLocation;
	}

	public String getLoanQuoteFirstName() {
		return loanQuoteFirstName;
	}

	public void setLoanQuoteFirstName(String loanQuoteFirstName) {
		this.loanQuoteFirstName = loanQuoteFirstName;
	}

	public int getAppApplyingFrom() {
		return appApplyingFrom;
	}

	public void setAppApplyingFrom(int appApplyingFrom) {
		this.appApplyingFrom = appApplyingFrom;
	}

	public String getAppISDCode() {
		return appISDCode;
	}

	public void setAppISDCode(String appISDCode) {
		this.appISDCode = appISDCode;
	}

	public String getAppNRIMobileNo() {
		return appNRIMobileNo;
	}

	public void setAppNRIMobileNo(String appNRIMobileNo) {
		this.appNRIMobileNo = appNRIMobileNo;
	}

	public Date getLoanQuoteDateOfBirthDT() {
		return loanQuoteDateOfBirthDT;
	}

	public void setLoanQuoteDateOfBirthDT(Date loanQuoteDateOfBirthDT) {
		this.loanQuoteDateOfBirthDT = loanQuoteDateOfBirthDT;
	}

	public String getLoanQuoteDateOfBirth() {
		return loanQuoteDateOfBirth;
	}

	public void setLoanQuoteDateOfBirth(String loanQuoteDateOfBirth) {
		this.loanQuoteDateOfBirth = loanQuoteDateOfBirth;
	}
	public Integer getLoanQuoteNewVisitId() {
		return loanQuoteNewVisitId;
	}
	public void setLoanQuoteNewVisitId(Integer loanQuoteNewVisitId) {
		this.loanQuoteNewVisitId = loanQuoteNewVisitId;
	}

	public Integer getLoanQuoteAdmissionQuota() {
		return loanQuoteAdmissionQuota;
	}

	public void setLoanQuoteAdmissionQuota(Integer loanQuoteAdmissionQuota) {
		this.loanQuoteAdmissionQuota = loanQuoteAdmissionQuota;
	}

	public Integer getLoanQuoteYearExistingEducationLoanStartDate() {
		return loanQuoteYearExistingEducationLoanStartDate;
	}

	public void setLoanQuoteYearExistingEducationLoanStartDate(
			Integer loanQuoteYearExistingEducationLoanStartDate) {
		this.loanQuoteYearExistingEducationLoanStartDate = loanQuoteYearExistingEducationLoanStartDate;
	}

	public Integer getLoanQuoteMonthExistingEducationLoanStartDate() {
		return loanQuoteMonthExistingEducationLoanStartDate;
	}

	public void setLoanQuoteMonthExistingEducationLoanStartDate(
			Integer loanQuoteMonthExistingEducationLoanStartDate) {
		this.loanQuoteMonthExistingEducationLoanStartDate = loanQuoteMonthExistingEducationLoanStartDate;
	}

	
	public Integer getLoanQuoteYearExistingEducationLoanEndDate() {
		return loanQuoteYearExistingEducationLoanEndDate;
	}

	public void setLoanQuoteYearExistingEducationLoanEndDate(
			Integer loanQuoteYearExistingEducationLoanEndDate) {
		this.loanQuoteYearExistingEducationLoanEndDate = loanQuoteYearExistingEducationLoanEndDate;
	}

	

	public Double getLoanQuoteCurrentEMIBeingPayed() {
		return loanQuoteCurrentEMIBeingPayed;
	}

	public void setLoanQuoteCurrentEMIBeingPayed(
			Double loanQuoteCurrentEMIBeingPayed) {
		this.loanQuoteCurrentEMIBeingPayed = loanQuoteCurrentEMIBeingPayed;
	}

	public Double getLoanQuoteExistingEducationTopupLoanAmount() {
		return loanQuoteExistingEducationTopupLoanAmount;
	}

	public void setLoanQuoteExistingEducationTopupLoanAmount(
			Double loanQuoteExistingEducationTopupLoanAmount) {
		this.loanQuoteExistingEducationTopupLoanAmount = loanQuoteExistingEducationTopupLoanAmount;
	}

	public Double getLoanQuotePrePaymentPenalty() {
		return loanQuotePrePaymentPenalty;
	}

	public void setLoanQuotePrePaymentPenalty(Double loanQuotePrePaymentPenalty) {
		this.loanQuotePrePaymentPenalty = loanQuotePrePaymentPenalty;
	}

	public String getLoanQuoteLoanTransferred() {
		return loanQuoteLoanTransferred;
	}

	public void setLoanQuoteLoanTransferred(String loanQuoteLoanTransferred) {
		this.loanQuoteLoanTransferred = loanQuoteLoanTransferred;
	}

	public String getLoanQuoteExistingLoanDisbursed() {
		return loanQuoteExistingLoanDisbursed;
	}

	public void setLoanQuoteExistingLoanDisbursed(
			String loanQuoteExistingLoanDisbursed) {
		this.loanQuoteExistingLoanDisbursed = loanQuoteExistingLoanDisbursed;
	}
	
	public Integer getLoanQuoteCollateralTypeId() {
		return loanQuoteCollateralTypeId;
	}

	public void setLoanQuoteCollateralTypeId(Integer loanQuoteCollateralTypeId) {
		this.loanQuoteCollateralTypeId = loanQuoteCollateralTypeId;
	}

	public Double getLoanQuoteCollateralAmount() {
		return loanQuoteCollateralAmount;
	}

	public void setLoanQuoteCollateralAmount(Double loanQuoteCollateralAmount) {
		this.loanQuoteCollateralAmount = loanQuoteCollateralAmount;
	}

	public Integer getLoanQuoteBankWhichHasExistingEducationLoan() {
		return loanQuoteBankWhichHasExistingEducationLoan;
	}

	public void setLoanQuoteBankWhichHasExistingEducationLoan(
			Integer loanQuoteBankWhichHasExistingEducationLoan) {
		this.loanQuoteBankWhichHasExistingEducationLoan = loanQuoteBankWhichHasExistingEducationLoan;
	}

	public Integer getLoanQuoteCountryId() {
		return loanQuoteCountryId;
	}

	public void setLoanQuoteCountryId(Integer loanQuoteCountryId) {
		this.loanQuoteCountryId = loanQuoteCountryId;
	}

	public Integer getLoanQuoteMonthExistingEducationLoanEndDate() {
		return loanQuoteMonthExistingEducationLoanEndDate;
	}

	public void setLoanQuoteMonthExistingEducationLoanEndDate(
			Integer loanQuoteMonthExistingEducationLoanEndDate) {
		this.loanQuoteMonthExistingEducationLoanEndDate = loanQuoteMonthExistingEducationLoanEndDate;
	}

	public Integer getLoanQuoteApplyingFrom() {
		return loanQuoteApplyingFrom;
	}

	public void setLoanQuoteApplyingFrom(Integer loanQuoteApplyingFrom) {
		this.loanQuoteApplyingFrom = loanQuoteApplyingFrom;
	}

	public Double getLoanQuotePreEmi() {
		return loanQuotePreEmi;
	}

	public void setLoanQuotePreEmi(Double loanQuotePreEmi) {
		this.loanQuotePreEmi = loanQuotePreEmi;
	}

	public Integer getLoanQuoteAssamEmp() {
		return loanQuoteAssamEmp;
	}

	public void setLoanQuoteAssamEmp(Integer loanQuoteAssamEmp) {
		this.loanQuoteAssamEmp = loanQuoteAssamEmp;
	}

	public Integer getLoanQuoteIsBranchEligibleForBidyalakmi() {
		return loanQuoteIsBranchEligibleForBidyalakmi;
	}

	public void setLoanQuoteIsBranchEligibleForBidyalakmi(Integer loanQuoteIsBranchEligibleForBidyalakmi) {
		this.loanQuoteIsBranchEligibleForBidyalakmi = loanQuoteIsBranchEligibleForBidyalakmi;
	}

	public String getAlternateMobileNumber() {
		return alternateMobileNumber;
	}

	public void setAlternateMobileNumber(String alternateMobileNumber) {
		this.alternateMobileNumber = alternateMobileNumber;
	}

	public String getAppAltISDCode() {
		return appAltISDCode;
	}

	public void setAppAltISDCode(String appAltISDCode) {
		this.appAltISDCode = appAltISDCode;
	}

	public Integer getLoanQuoteConsentId() {
		return loanQuoteConsentId;
	}

	public void setLoanQuoteConsentId(Integer loanQuoteConsentId) {
		this.loanQuoteConsentId = loanQuoteConsentId;
	}

	
	
}
