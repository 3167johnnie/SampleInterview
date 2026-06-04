package com.mintstreet.loan.homeloan.entity;

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
@Table(name="RUPEEPOWER_OCAS_T_00255")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="G3", sequenceName="RUPEEPOWER_OCAS_SEQ_00099" ,allocationSize=1)
public class ApplicationFormHomeLoanQuote extends Domain<Integer> implements Serializable  {
	
	private static final long serialVersionUID = -6019579785855286119L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G3")
	@Column(name="QUOTE_SEQUENCE_ID")
	private Integer loanQuoteId;
	
	@Column(name="QUOTE_ACTIVE")
	private String loanQuoteActive;
	
	@Column(name="QUOTE_BROWSER_NAME")
	private String loanQuoteBrowserName;

	@Column(name="QUOTE_APPLIED_COUPON")
	private String loanQuoteAppliedCoupon;

	

	@Column(name="QUOTE_BUILDER_NAME")
	private String loanQuoteBuilderName;
	
	@Column(name="QUOTE_PROJECT_ID")
	private Integer loanQuoteProjectId;
	
	@Column(name="QUOTE_BUILDER_ID")
	private Integer loanQuoteBuilderId;

	@Column(name="QUOTE_CITY_ID")
	private Integer loanQuoteCityId;

	@Column(name="QUOTE_COAPP_FST_ID")
	private Integer loanQuoteCoapplicantFirstId;

	@Column(name="QUOTE_COAPP_FST_CITY_ID")
	private Integer loanQuoteCoapplicantFirstCityId;

	@Column(name="QUOTE_COAPP_FST_EMP_NAME")
	private String loanQuoteCoapplicantFirstCoEmplyerName;
	
	@Column(name="QUOTE_COAPP_FST_EMP_ID")
	private Integer loanQuoteCoapplicantFirstCoEmpId;
	
	@Column(name="QUOTE_COAPP_FST_SA_WITH_SBI")
	private String loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;

	@Column(name="QUOTE_COAPP_FST_COUNTRY_ID")
	private Integer loanQuoteCoapplicantFirstCountryId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_COAPP_FST_DATE_OF_BIRTH")
	private Date loanQuoteCoapplicantFirstDateOfBirthDT;
	
	@Column(name="QUOTE_COAPP_FST_GENDER")
	private String loanQuoteCoapplicantFirstGender;
	
	@Transient
	private String loanQuoteCoapplicantFirstDateOfBirth;
	@Transient
	private Integer loanQuoteCoapplicantFirstAge;
	
	@Column(name="QUOTE_COAPP_FST_DEP")
	private Double loanQuoteCoapplicantFirstDepreciatiation;

	@Column(name="QUOTE_COAPP_FST_EMP_TYPE_ID")
	private Integer loanQuoteCoapplicantFirstEmploymentTypeId;

	@Column(name="QUOTE_COAPP_FST_EXP_RENT_INC")
	private Double loanQuoteCoapplicantFirstExpectedRentalIncome;

	@Column(name="QUOTE_COAPP_FST_INC_OTH_SRC")
	private Double loanQuoteCoapplicantFirstIncomeFromOtherSource;

	@Column(name="QUOTE_COAPP_FST_MON_SAL")
	private Double loanQuoteCoapplicantFirstMonthlySalary;
	
	@Column(name="QUOTE_COAPP_FST_NET_ANN_INC")
	private Double loanQuoteCoapplicantFirstNetAnnualIncome;

	@Column(name="QUOTE_COAPP_FST_NET_MON_PEN")
	private Double loanQuoteCoapplicantFirstNetMonthlyPension;
	
	@Column(name="QUOTE_COAPP_FST_OTH_INCOME")
	private Double loanQuoteCoapplicantFirstOtherIncome;
	
	@Column(name="QUOTE_COAPP_FST_PREEMIS")
	private Double loanQuoteCoapplicantFirstPreEMIs;

	@Column(name="QUOTE_COAPP_FST_PAT")
	private Double loanQuoteCoapplicantFirstProfitAfterTax;

	@Column(name="QUOTE_COAPP_FST_REL_ID")
	private Integer loanQuoteCoapplicantFirstRelationshipId;

	@Column(name="QUOTE_COAPP_FST_RES_TYPE_ID")
	private Integer loanQuoteCoapplicantFirstResidentTypeId;

	@Column(name="QUOTE_COAPP_FST_RET_AGE")
	private Integer loanQuoteCoapplicantFirstRetirementAge;

	@Column(name="QUOTE_COAPP_FST_VAR_MON_PAYON")
	private Double loanQuoteCoapplicantFirstVariableMonthPayon;

	@Column(name="QUOTE_COAPP_FST_WORK_EXP")
	private Integer loanQuoteCoapplicantFirstWorkExperience;

	@Column(name="QUOTE_COAPP_SEC_ID")
	private Integer loanQuoteCoapplicantSecondId;

	@Column(name="QUOTE_COAPP_SEC_CITY_ID")
	private Integer loanQuoteCoapplicantSecondCityId;

	@Column(name="QUOTE_COAPP_SEC_EMP_NAME")
	private String loanQuoteCoapplicantSecondCoEmplyerName;
	
	@Column(name="QUOTE_COAPP_SEC_EMP_ID")
	private Integer loanQuoteCoapplicantSecondCoEmplyerId;
	
	@Column(name="QUOTE_COAPP_SEC_SA_WITH_SBI")
	private String loanQuoteCoapplicantSecondCoSalaryAccountWithSbi;
	
	@Column(name="QUOTE_COAPP_SEC_COUNTRY_ID")
	private Integer loanQuoteCoapplicantSecondCountryId;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_COAPP_SEC_DATE_OF_BIRTH")
	private Date loanQuoteCoapplicantSecondDateOfBirthDT;
	
	@Column(name="QUOTE_COAPP_SEC_GENDER")
	private String loanQuoteCoapplicantSecondGender;
	
	@Transient
	private String loanQuoteCoapplicantSecondDateOfBirth;
	
	@Transient
	private Integer loanQuoteCoapplicantSecondAge;
	
	@Column(name="QUOTE_COAPP_SEC_DEP")
	private Double loanQuoteCoapplicantSecondDepreciatiation;

	@Column(name="QUOTE_COAPP_SEC_EMP_TYPE_ID")
	private Integer loanQuoteCoapplicantSecondEmploymentTypeId;

	@Column(name="QUOTE_COAPP_SEC_EXP_REN_INC")
	private Double loanQuoteCoapplicantSecondExpectedRentalIncome;

	@Column(name="QUOTE_COAPP_SEC_INC_OTH_SRC")
	private Double loanQuoteCoapplicantSecondIncomeFromOtherSource;
	
	@Column(name="QUOTE_COAPP_SEC_MON_SAL")
	private Double loanQuoteCoapplicantSecondMonthlySalary;
	
	@Column(name="QUOTE_COAPP_SEC_NET_ANN_INC")
	private Double loanQuoteCoapplicantSecondNetAnnualIncome;

	@Column(name="QUOTE_COAPP_SEC_NET_MON_PEN")
	private Double loanQuoteCoapplicantSecondNetMonthlyPension;

	@Column(name="QUOTE_COAPP_SEC_OTH_INCOME")
	private Double loanQuoteCoapplicantSecondOtherIncome;

	@Column(name="QUOTE_COAPP_SEC_PREEMIS")
	private Double loanQuoteCoapplicantSecondPreEMIs;
	
	@Column(name="QUOTE_COAPP_SEC_PAT")
	private Double loanQuoteCoapplicantSecondProfitAfterTax;

	@Column(name="QUOTE_COAPP_SEC_REL_ID")
	private Integer loanQuoteCoapplicantSecondRelationshipId;

	@Column(name="QUOTE_COAPP_SEC_RES_TYPE_ID")
	private Integer loanQuoteCoapplicantSecondResidentTypeId;

	@Column(name="QUOTE_COAPP_SEC_RET_AGE")
	private Integer loanQuoteCoapplicantSecondRetirementAge;

	@Column(name="QUOTE_COAPP_SEC_VAR_MON_PAYON")
	private Double loanQuoteCoapplicantSecondVariableMonthPayon;

	@Column(name="QUOTE_COAPP_SEC_WORK_EXP")
	private Integer loanQuoteCoapplicantSecondWorkExperience;
	
	@Column(name="QUOTE_COST_HOME_FLAT")
	private Double loanQuoteCostHomeFlat;

	@Column(name="QUOTE_COST_OF_CONS")
	private Double loanQuoteCostOfConstruction;

	@Column(name="QUOTE_COUNTRY_ID")
	private Integer loanQuoteCountryId;
	
	@Column(name="QUOTE_LMS_USER_ID")
	private Integer loanQuoteCreatedLmsUserId;

	@Column(name="QUOTE_CURR_VAL_OF_PRO")
	private Double loanQuoteCurrentValueOfProperty;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_DATE_OF_BIRTH")
	private Date loanQuoteDateOfBirthDT;
	
	@Transient
	private String loanQuoteDateOfBirth;
	
	@Transient
	private Integer loanQuoteAge;
	
	@Column(name="QUOTE_DELETED")
	private String loanQuoteDeleted;

	@Column(name="QUOTE_DEPRECIATION")
	private Double loanQuoteDepreciation;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_EMI_STD_EXIT_TOPUP_LOAN")
	private Date loanQuoteEmiStartDateOfExistingTopupLoan;
	
	@Transient
	private Integer loanQuoteEmiStartDateOfExistingTopupLoanMonth;
	@Transient
	private Integer loanQuoteEmiStartDateOfExistingTopupLoanYear;

	@Column(name="QUOTE_EMPLOYER_COMPANY_ID")
	private Integer loanQuoteEmployerCompanyId;

	@Column(name="QUOTE_EMPLOYER_NAME")
	private String loanQuoteEmployerName;

	@Column(name="QUOTE_EMPLOYMENT_TYPE_ID")
	private Integer loanQuoteEmploymentTypeId;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ENTRY_TIME")
	private Date loanQuoteEntryTime;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_EXIT_HOME_LOAN_ST_DATE")
	private Date loanQuoteExistingHomeLoanStartDate;
	@Column(name="QUOTE_EXIT_HL_ST_DATE_MONTH")
	private Integer loanQuoteMonthExistingHomeLoanStartDate;
	@Column(name="QUOTE_EXIT_HL_ST_DATE_YEAR")
	private Integer loanQuoteYearExistingHomeLoanStartDate;

	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_EXIT_HOME_LOAN_END_DATE")
	private Date loanQuoteExistingHomeLoanEndDate;
	@Column(name="QUOTE_EXIT_HL_END_DATE_MONTH")
	private Integer loanQuoteMonthExistingHomeLoanEndDate;
	@Column(name="QUOTE_EXIT_HL_END_DATE_YEAR")
	private Integer loanQuoteYearExistingHomeLoanEndDate;
	

	@Column(name="QUOTE_EXIT_HOME_LOAN_WITH_SBI")
	private String loanQuoteExistingHomeLoanWithSbi;

	@Column(name="QUOTE_EXPECTED_RENTAL_INCOME")
	private Double loanQuoteExpectedRentalIncome;

	@Column(name="QUOTE_GENDER")
	private String loanQuoteGender;

	@Column(name="QUOTE_HAVE_SAL_ACC_WITH_SBI")
	private String loanQuoteHaveSalaryAccountWithSbi;

	@Column(name="QUOTE_HTML_CONTENT")
	private String loanQuoteHtmlContent;

	@Column(name="QUOTE_INCOME_FROM_REGULAR_SRC")
	private Double loanQuoteIncomeFromRegularSource;

	@Column(name="QUOTE_IP_ADDRESS")
	private String loanQuoteIpAddress;

	@Column(name="QUOTE_LAND_COST")
	private Double loanQuoteLandCost;

	@Column(name="QUOTE_LEAD_ID")
	private Integer loanQuoteLeadId;

	@Column(name="QUOTE_LOAN_AMOUNT")
	private Double loanQuoteLoanAmount;
	
	@Column(name="QUOTE_LOAN_ACC_TYPE")
	private Integer loanQuoteLoanAccountType;
	
	@Column(name="QUOTE_LOAN_PRODUCT_ID")
	private Integer loanQuoteLoanProductId;
	
	@Column(name="QUOTE_LOAN_CATEGORY_ID")
	private Integer loanQuoteLoanCategoryId;

	@Column(name="QUOTE_LOAN_OUTSTAND_BAL")
	private Double loanQuoteLoanOutstandingBalance;

	@Column(name="QUOTE_LOAN_PURPOSE_ID")
	private Integer loanQuoteLoanPurposeId;

	@Column(name="QUOTE_LOAN_TENURE")
	private Integer loanQuoteLoanTenure;

	@Column(name="QUOTE_LOAN_TOPUP_AMT_REQ")
	private Double loanQuoteLoanTopupAmountRequested;

	@Column(name="QUOTE_LOAN_WITH_BANK_ID")
	private Integer loanQuoteLoanWithBankId;

	@Column(name="QUOTE_MORTGAGE_CREATED")
	private String loanQuoteMortgageCreated;

	@Column(name="QUOTE_NET_ANNUAL_INCOME")
	private Double loanQuoteNetAnnualIncome;

	@Column(name="QUOTE_NET_MONTHLY_SALARY")
	private Double loanQuoteNetMonthlySalary;

	@Column(name="QUOTE_NET_MONTHLY_PENSION")
	private Double loanQuoteNetMonthlyPension;

	

	@Column(name="QUOTE_OTHER_INCOME")
	private Double loanQuoteOtherIncome;

	@Column(name="QUOTE_OUTSTAND_LOAN_AMOUNT")
	private Double loanQuoteOutstandingLoanAmount;

	@Column(name="QUOTE_POSSESSION_COMPLETED")
	private String loanQuotePossessionCompleted;
	
	@Column(name="QUOTE_PREEMIS")
	private Double loanQuotePreEMIs;

	@Column(name="QUOTE_PREF_CITY_CATEGORY")
	private String loanQuotePreferredCityCategory;

	@Column(name="QUOTE_PREF_CITY_ID")
	private Integer loanQuotePreferredCityId;

	@Column(name="QUOTE_PREF_DISTRICT_ID")
	private Integer loanQuotePreferredDistrictId;

	@Column(name="QUOTE_PREF_LOCALITY_ID")
	private Integer loanQuotePreferredLocalityId;

	@Column(name="QUOTE_PREF_LOC_OF_AVL_LOAN")
	private Integer loanQuotePreferredLocationOfAvailingLoan;

	@Column(name="QUOTE_PREF_STATE_ID")
	private Integer loanQuotePreferredStateId;

	@Column(name="QUOTE_PRES_VALUE_OF_PROPERTY")
	private Double loanQuotePresentValueOfProperty;

	@Column(name="QUOTE_PREF_BRANCH_ID")
	private Integer loanQuotePrferredBranchId;
	
	@Column(name="QUOTE_PROFIT_AFTER_TAX")
	private Double loanQuoteProfitAfterTax;

	@Column(name="QUOTE_PROPERTY_TYPE")
	private Integer loanQuotePropertyType;

	

	@Column(name="QUOTE_RENOVATION_COST")
	private Double loanQuoteRenovationCost;

	@Column(name="QUOTE_RESIDENT_TYPE_ID")
	private Integer loanQuoteResidentTypeId;

	@Column(name="QUOTE_RET_AGE_APPLICANT")
	private Integer loanQuoteRetirementAgeApplicant;
	
	@Column(name="QUOTE_INDUSTRY_TYPE")
	private Integer loanQuoteIndustryType;

	@Column(name="QUOTE_SALARY_PACKAGE")
	private Integer loanQuoteSalaryPackage;
	
	@Column(name="QUOTE_FIRST_OWNER_OF_PROPERTY")
	private String loanQuoteFirstOwnerOfProperty;
		
	@Column(name="QUOTE_SOURCE_ID")
	private Integer loanQuoteSourceId;

	@Column(name="QUOTE_TENOR_EXIT_HOME_LOAN")
	private Integer loanQuoteTenorExistingHomeLoan;
	@Column(name="QUOTE_TENOR_EXIT_HL_MONTH")
	private Integer loanQuoteMonthTenorExistingHomeLoan;
	@Column(name="QUOTE_TENOR_EXIT_HL_YEAR")
	private Integer loanQuoteYearTenorExistingHomeLoan;
	
	@Column(name="QUOTE_TOT_TOPUP_AMOUNT_TAKEN")
	private Double loanQuoteTotalTopupAmountTaken;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="QUOTE_UPDATED_TIME")
	private Date loanQuoteUpdatedTime;

	@Column(name="QUOTE_VARIABLE_MON_PAY")
	private Double loanQuoteVariableMonthPay;

	@Column(name="QUOTE_VISIT_ID")
	private Integer loanQuoteVisitId;

	@Column(name = "QUOTE_NEW_VISIT_ID")
	private Integer loanQuoteNewVisitId;

	@Column(name="QUOTE_WORK_EXPERIENCE")
	private Integer loanQuoteWorkExperience;

	
	
	@Column(name="QUOTE_YEAR_TENURE_EX_HOME_LOAN")
	private Integer yearTenureOfExistingHomeLoan;
	
	@Column(name="QUOTE_MNTH_TENURE_EX_HOME_LOAN")
	private Integer monthTenureOfExistingHomeLoan;
	
	@Column(name="QUOTE_IS_REIMBURSE_PROPERTY")
	private String loanQuoteReimburse;
	
	@Column(name="QUOTE_AMT_INVESTED_REIMBURSE")
	private Double loanQuoteAmountInvested;
	
	@Column(name="QUOTE_PROP_MARKET_VAL")
	private Double loanQuotePropertyMarketValue;
	
    // new code for cibil 
	@Column(name="QUOTE_CIBIL_SCORE")
	private Integer loanQuoteCibilScore;
	
	@Column(name = "QUOTE_FIRST_NAME")
	private String loanQuoteAppFirstName;
	
	@Column(name="QUOTE_MIDDLE_NAME")
	private String loanQuoteMiddleName;
	
	@Column(name="QUOTE_LAST_NAME")
	private String loanQuoteLastName;
	
	@Column(name="QUOTE_ADDRESS_1")
	private String loanQuoteAddress1;
	
	@Column(name="QUOTE_ADDRESS_2")
	private String loanQuoteAddress2;
	
	@Column(name="QUOTE_LANDMARK")
	private String loanQuoteAddressLandmark;
	
	@Column(name="QUOTE_STATE_ID")
	private Integer loanQuoteStateId;

	@Column(name="QUOTE_PINCODE")
	private Integer loanQuotePincode;

	@Column(name="QUOTE_PANCARD_NO")
	private String loanQuotePanCardNo;
	

	

	//@Transient
	@Column(name="QUOTE_APP_MOBILE")
	private String appMobile;
	
	
	
	@Column(name="QUOTE_APP_EMAIL")
	private String appEmail;


	@Column(name="QUOTE_LOAN_AMOUNT_TAKEN ")
	private Double loanQuoteLoanAmountTaken;
	
	
	
	
	
	//end cibil

	@Transient
	private int appApplyingFrom;
	
	@Transient
	private String appISDCode;
	
	@Transient
	private String appNRIMobileNo;
	
	//@Transient
	//private String loanQuoteAppFirstName;
	
	@Transient
	private String loanQuoteAppMobileNo;
	
	@Transient
	private String loanQuoteAppWorkEmail;
	
	@Column(name="QUOTE_PROJECT_COST")
	private Double loanQuoteProjectCost;
	
	@Transient
	private Double loanQuoteNetAnnualIncomeOfApplicant;
	
	@Column(name="QUOTE_IS_ELIGIBLE_FOR_HAR_GHAR")
	private Integer loanQuoteIsEligibleForHarGhar;
	
	@Transient
	private Integer loanQuoteYearCompletionDate;
	
	@Transient
	private Integer loanQuoteMonthCompletionDate;
	
	@Column(name="QUOTE_COMPLETION_DATE")
	private Date loanQuoteCompletionDate;
	
	@Column(name="QUOTE_AVAIL_REIMBURSEMENT")
	private String loanQuoteIsAvailReimbursement;
	
	@Column(name="QUOTE_ACTUAL_INVESTMENT_MADE")
	private Integer loanQuoteActualInvestmentMade;
	
	@Column(name="QUOTE_MARKET_VALUE_PROPERTY")
	private Double loanQuoteMarketValueOfProperty;
	
	@Column(name="QUOTE_PREEMIS_OTHER")
	private Double loanQuotePreEMIsOther;
	
	@Column(name="QUOTE_CHECK_OFF_TYPE")
	private Integer loanQuoteCheckOffType;
	
	//@Transient
	//private String loanQuoteMiddleName;
	
	//@Transient
	//private String loanQuoteLastName;
	
	@Transient
	private Integer loanPurpose;
	
	@Transient
	private String loanType;
	
	@Column(name="QUOTE_MARITAL_STATUS")
	private Integer loanQuoteMaritalStatus;
	
	@Column(name="QUOTE_PUCCA_HOUSE")
	private String loanQuotePuccaHouse;
	
	@Column(name="QUOTE_PMAY_ITR")
	private String loanQuotePmayItr;
	
	@Column(name="QUOTE_IS_ELIGIBLE_PMAY")
	private Integer loanQuoteEligiblePmay;
	
	@Transient
	private String alternateMobileNumber;
	  
	@Transient
	private String appAltISDCode;
	
	@Transient
	private Integer loanQuoteConsentId;
	
	//For Privacy Concent Ntb
	@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
	private String quotePrivacyConsentFlag;

	@Column(name = "QUOTE_NTB_ID")
	private String quoteNtbId;
	
	public Double getLoanQuoteMarketValueOfProperty() {
		return loanQuoteMarketValueOfProperty;
	}

	public void setLoanQuoteMarketValueOfProperty(
			Double loanQuoteMarketValueOfProperty) {
		this.loanQuoteMarketValueOfProperty = loanQuoteMarketValueOfProperty;
	}

	public Double getLoanQuoteProjectCost() {
		return loanQuoteProjectCost;
	}

	public void setLoanQuoteProjectCost(Double loanQuoteProjectCost) {
		this.loanQuoteProjectCost = loanQuoteProjectCost;
	}

	public Double getLoanQuoteNetAnnualIncomeOfApplicant() {
		return loanQuoteNetAnnualIncomeOfApplicant;
	}

	public void setLoanQuoteNetAnnualIncomeOfApplicant(
			Double loanQuoteNetAnnualIncomeOfApplicant) {
		this.loanQuoteNetAnnualIncomeOfApplicant = loanQuoteNetAnnualIncomeOfApplicant;
	}

	public Integer getLoanQuoteMonthExistingHomeLoanEndDate() {
		return loanQuoteMonthExistingHomeLoanEndDate;
	}

	public void setLoanQuoteMonthExistingHomeLoanEndDate(
			Integer loanQuoteMonthExistingHomeLoanEndDate) {
		this.loanQuoteMonthExistingHomeLoanEndDate = loanQuoteMonthExistingHomeLoanEndDate;
	}

	public Integer getLoanQuoteYearExistingHomeLoanEndDate() {
		return loanQuoteYearExistingHomeLoanEndDate;
	}

	public void setLoanQuoteYearExistingHomeLoanEndDate(
			Integer loanQuoteYearExistingHomeLoanEndDate) {
		this.loanQuoteYearExistingHomeLoanEndDate = loanQuoteYearExistingHomeLoanEndDate;
	}

	public Integer getLoanQuotePrferredBranchId() {
		return loanQuotePrferredBranchId;
	}

	public void setLoanQuotePrferredBranchId(Integer loanQuotePrferredBranchId) {
		this.loanQuotePrferredBranchId = loanQuotePrferredBranchId;
	}

	public ApplicationFormHomeLoanQuote() {
	}

	public Integer getLoanQuoteId() {
		return this.loanQuoteId;
	}

	public void setLoanQuoteId(Integer loanQuoteId) {
		this.loanQuoteId = loanQuoteId;
	}

	public String getLoanQuoteActive() {
		return this.loanQuoteActive;
	}

	public void setLoanQuoteActive(String loanQuoteActive) {
		this.loanQuoteActive = loanQuoteActive;
	}

	public String getLoanQuoteBrowserName() {
		return this.loanQuoteBrowserName;
	}

	public void setLoanQuoteBrowserName(String loanQuoteBrowserName) {
		this.loanQuoteBrowserName = loanQuoteBrowserName;
	}

	public String getLoanQuoteBuilderName() {
		return this.loanQuoteBuilderName;
	}

	public void setLoanQuoteBuilderName(String loanQuoteBuilderName) {
		this.loanQuoteBuilderName = loanQuoteBuilderName;
	}

	

	public Integer getLoanQuoteCityId() {
		return this.loanQuoteCityId;
	}

	public void setLoanQuoteCityId(Integer loanQuoteCityId) {
		this.loanQuoteCityId = loanQuoteCityId;
	}

	public Integer getLoanQuoteCoapplicantFirstCityId() {
		return this.loanQuoteCoapplicantFirstCityId;
	}

	public void setLoanQuoteCoapplicantFirstCityId(Integer loanQuoteCoapplicantFirstCityId) {
		this.loanQuoteCoapplicantFirstCityId = loanQuoteCoapplicantFirstCityId;
	}

	public Integer getLoanQuoteCoapplicantFirstCountryId() {
		return this.loanQuoteCoapplicantFirstCountryId;
	}

	public void setLoanQuoteCoapplicantFirstCountryId(Integer loanQuoteCoapplicantFirstCountryId) {
		this.loanQuoteCoapplicantFirstCountryId = loanQuoteCoapplicantFirstCountryId;
	}

	public Double getLoanQuoteCoapplicantFirstDepreciatiation() {
		return this.loanQuoteCoapplicantFirstDepreciatiation;
	}

	public void setLoanQuoteCoapplicantFirstDepreciatiation(Double loanQuoteCoapplicantFirstDepreciatiation) {
		this.loanQuoteCoapplicantFirstDepreciatiation = loanQuoteCoapplicantFirstDepreciatiation;
	}

	public Integer getLoanQuoteCoapplicantFirstEmploymentTypeId() {
		return this.loanQuoteCoapplicantFirstEmploymentTypeId;
	}

	public void setLoanQuoteCoapplicantFirstEmploymentTypeId(Integer loanQuoteCoapplicantFirstEmploymentTypeId) {
		this.loanQuoteCoapplicantFirstEmploymentTypeId = loanQuoteCoapplicantFirstEmploymentTypeId;
	}

	public Double getLoanQuoteCoapplicantFirstExpectedRentalIncome() {
		return this.loanQuoteCoapplicantFirstExpectedRentalIncome;
	}

	public void setLoanQuoteCoapplicantFirstExpectedRentalIncome(Double loanQuoteCoapplicantFirstExpectedRentalIncome) {
		this.loanQuoteCoapplicantFirstExpectedRentalIncome = loanQuoteCoapplicantFirstExpectedRentalIncome;
	}

	public Integer getLoanQuoteCoapplicantFirstId() {
		return this.loanQuoteCoapplicantFirstId;
	}

	public void setLoanQuoteCoapplicantFirstId(Integer loanQuoteCoapplicantFirstId) {
		this.loanQuoteCoapplicantFirstId = loanQuoteCoapplicantFirstId;
	}

	public Double getLoanQuoteCoapplicantFirstIncomeFromOtherSource() {
		return this.loanQuoteCoapplicantFirstIncomeFromOtherSource;
	}

	public void setLoanQuoteCoapplicantFirstIncomeFromOtherSource(Double loanQuoteCoapplicantFirstIncomeFromOtherSource) {
		this.loanQuoteCoapplicantFirstIncomeFromOtherSource = loanQuoteCoapplicantFirstIncomeFromOtherSource;
	}

	public Double getLoanQuoteCoapplicantFirstNetAnnualIncome() {
		return this.loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public void setLoanQuoteCoapplicantFirstNetAnnualIncome(Double loanQuoteCoapplicantFirstNetAnnualIncome) {
		this.loanQuoteCoapplicantFirstNetAnnualIncome = loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public Double getLoanQuoteCoapplicantFirstOtherIncome() {
		return this.loanQuoteCoapplicantFirstOtherIncome;
	}

	public void setLoanQuoteCoapplicantFirstOtherIncome(Double loanQuoteCoapplicantFirstOtherIncome) {
		this.loanQuoteCoapplicantFirstOtherIncome = loanQuoteCoapplicantFirstOtherIncome;
	}

	public Double getLoanQuoteCoapplicantFirstPreEMIs() {
		return loanQuoteCoapplicantFirstPreEMIs;
	}

	public void setLoanQuoteCoapplicantFirstPreEMIs(
			Double loanQuoteCoapplicantFirstPreEMIs) {
		this.loanQuoteCoapplicantFirstPreEMIs = loanQuoteCoapplicantFirstPreEMIs;
	}

	public Double getLoanQuoteCoapplicantFirstProfitAfterTax() {
		return this.loanQuoteCoapplicantFirstProfitAfterTax;
	}

	public void setLoanQuoteCoapplicantFirstProfitAfterTax(Double loanQuoteCoapplicantFirstProfitAfterTax) {
		this.loanQuoteCoapplicantFirstProfitAfterTax = loanQuoteCoapplicantFirstProfitAfterTax;
	}

	public Integer getLoanQuoteCoapplicantFirstRelationshipId() {
		return this.loanQuoteCoapplicantFirstRelationshipId;
	}

	public void setLoanQuoteCoapplicantFirstRelationshipId(Integer loanQuoteCoapplicantFirstRelationshipId) {
		this.loanQuoteCoapplicantFirstRelationshipId = loanQuoteCoapplicantFirstRelationshipId;
	}

	public Integer getLoanQuoteCoapplicantFirstResidentTypeId() {
		return this.loanQuoteCoapplicantFirstResidentTypeId;
	}

	public void setLoanQuoteCoapplicantFirstResidentTypeId(Integer loanQuoteCoapplicantFirstResidentTypeId) {
		this.loanQuoteCoapplicantFirstResidentTypeId = loanQuoteCoapplicantFirstResidentTypeId;
	}

	public Integer getLoanQuoteCoapplicantFirstRetirementAge() {
		return this.loanQuoteCoapplicantFirstRetirementAge;
	}

	public void setLoanQuoteCoapplicantFirstRetirementAge(Integer loanQuoteCoapplicantFirstRetirementAge) {
		this.loanQuoteCoapplicantFirstRetirementAge = loanQuoteCoapplicantFirstRetirementAge;
	}

	public Double getLoanQuoteCoapplicantFirstVariableMonthPayon() {
		return this.loanQuoteCoapplicantFirstVariableMonthPayon;
	}

	public void setLoanQuoteCoapplicantFirstVariableMonthPayon(Double loanQuoteCoapplicantFirstVariableMonthPayon) {
		this.loanQuoteCoapplicantFirstVariableMonthPayon = loanQuoteCoapplicantFirstVariableMonthPayon;
	}

	public Integer getLoanQuoteCoapplicantSecondCityId() {
		return this.loanQuoteCoapplicantSecondCityId;
	}

	public void setLoanQuoteCoapplicantSecondCityId(Integer loanQuoteCoapplicantSecondCityId) {
		this.loanQuoteCoapplicantSecondCityId = loanQuoteCoapplicantSecondCityId;
	}

	public Integer getLoanQuoteCoapplicantSecondCountryId() {
		return this.loanQuoteCoapplicantSecondCountryId;
	}

	public void setLoanQuoteCoapplicantSecondCountryId(Integer loanQuoteCoapplicantSecondCountryId) {
		this.loanQuoteCoapplicantSecondCountryId = loanQuoteCoapplicantSecondCountryId;
	}

	public Double getLoanQuoteCoapplicantSecondDepreciatiation() {
		return this.loanQuoteCoapplicantSecondDepreciatiation;
	}

	public void setLoanQuoteCoapplicantSecondDepreciatiation(Double loanQuoteCoapplicantSecondDepreciatiation) {
		this.loanQuoteCoapplicantSecondDepreciatiation = loanQuoteCoapplicantSecondDepreciatiation;
	}

	public Integer getLoanQuoteCoapplicantSecondEmploymentTypeId() {
		return this.loanQuoteCoapplicantSecondEmploymentTypeId;
	}

	public void setLoanQuoteCoapplicantSecondEmploymentTypeId(Integer loanQuoteCoapplicantSecondEmploymentTypeId) {
		this.loanQuoteCoapplicantSecondEmploymentTypeId = loanQuoteCoapplicantSecondEmploymentTypeId;
	}

	public Double getLoanQuoteCoapplicantSecondExpectedRentalIncome() {
		return this.loanQuoteCoapplicantSecondExpectedRentalIncome;
	}

	public void setLoanQuoteCoapplicantSecondExpectedRentalIncome(Double loanQuoteCoapplicantSecondExpectedRentalIncome) {
		this.loanQuoteCoapplicantSecondExpectedRentalIncome = loanQuoteCoapplicantSecondExpectedRentalIncome;
	}

	public Integer getLoanQuoteCoapplicantSecondId() {
		return this.loanQuoteCoapplicantSecondId;
	}

	public void setLoanQuoteCoapplicantSecondId(Integer loanQuoteCoapplicantSecondId) {
		this.loanQuoteCoapplicantSecondId = loanQuoteCoapplicantSecondId;
	}

	public Double getLoanQuoteCoapplicantSecondIncomeFromOtherSource() {
		return this.loanQuoteCoapplicantSecondIncomeFromOtherSource;
	}

	public void setLoanQuoteCoapplicantSecondIncomeFromOtherSource(Double loanQuoteCoapplicantSecondIncomeFromOtherSource) {
		this.loanQuoteCoapplicantSecondIncomeFromOtherSource = loanQuoteCoapplicantSecondIncomeFromOtherSource;
	}

	public Double getLoanQuoteCoapplicantSecondNetAnnualIncome() {
		return this.loanQuoteCoapplicantSecondNetAnnualIncome;
	}

	public void setLoanQuoteCoapplicantSecondNetAnnualIncome(Double loanQuoteCoapplicantSecondNetAnnualIncome) {
		this.loanQuoteCoapplicantSecondNetAnnualIncome = loanQuoteCoapplicantSecondNetAnnualIncome;
	}

	public Double getLoanQuoteCoapplicantSecondOtherIncome() {
		return this.loanQuoteCoapplicantSecondOtherIncome;
	}

	public void setLoanQuoteCoapplicantSecondOtherIncome(Double loanQuoteCoapplicantSecondOtherIncome) {
		this.loanQuoteCoapplicantSecondOtherIncome = loanQuoteCoapplicantSecondOtherIncome;
	}


	public Double getLoanQuoteCoapplicantSecondPreEMIs() {
		return loanQuoteCoapplicantSecondPreEMIs;
	}

	public void setLoanQuoteCoapplicantSecondPreEMIs(
			Double loanQuoteCoapplicantSecondPreEMIs) {
		this.loanQuoteCoapplicantSecondPreEMIs = loanQuoteCoapplicantSecondPreEMIs;
	}

	public Double getLoanQuoteCoapplicantSecondProfitAfterTax() {
		return this.loanQuoteCoapplicantSecondProfitAfterTax;
	}

	public void setLoanQuoteCoapplicantSecondProfitAfterTax(Double loanQuoteCoapplicantSecondProfitAfterTax) {
		this.loanQuoteCoapplicantSecondProfitAfterTax = loanQuoteCoapplicantSecondProfitAfterTax;
	}

	public Integer getLoanQuoteCoapplicantSecondRelationshipId() {
		return this.loanQuoteCoapplicantSecondRelationshipId;
	}

	public void setLoanQuoteCoapplicantSecondRelationshipId(Integer loanQuoteCoapplicantSecondRelationshipId) {
		this.loanQuoteCoapplicantSecondRelationshipId = loanQuoteCoapplicantSecondRelationshipId;
	}

	public Integer getLoanQuoteCoapplicantSecondResidentTypeId() {
		return this.loanQuoteCoapplicantSecondResidentTypeId;
	}

	public void setLoanQuoteCoapplicantSecondResidentTypeId(Integer loanQuoteCoapplicantSecondResidentTypeId) {
		this.loanQuoteCoapplicantSecondResidentTypeId = loanQuoteCoapplicantSecondResidentTypeId;
	}

	public Integer getLoanQuoteCoapplicantSecondRetirementAge() {
		return this.loanQuoteCoapplicantSecondRetirementAge;
	}

	public void setLoanQuoteCoapplicantSecondRetirementAge(Integer loanQuoteCoapplicantSecondRetirementAge) {
		this.loanQuoteCoapplicantSecondRetirementAge = loanQuoteCoapplicantSecondRetirementAge;
	}

	public Double getLoanQuoteCoapplicantSecondVariableMonthPayon() {
		return this.loanQuoteCoapplicantSecondVariableMonthPayon;
	}

	public void setLoanQuoteCoapplicantSecondVariableMonthPayon(Double loanQuoteCoapplicantSecondVariableMonthPayon) {
		this.loanQuoteCoapplicantSecondVariableMonthPayon = loanQuoteCoapplicantSecondVariableMonthPayon;
	}

	public Double getLoanQuoteCostHomeFlat() {
		return this.loanQuoteCostHomeFlat;
	}

	public void setLoanQuoteCostHomeFlat(Double loanQuoteCostHomeFlat) {
		this.loanQuoteCostHomeFlat = loanQuoteCostHomeFlat;
	}

	public Double getLoanQuoteCostOfConstruction() {
		return this.loanQuoteCostOfConstruction;
	}

	public void setLoanQuoteCostOfConstruction(Double loanQuoteCostOfConstruction) {
		this.loanQuoteCostOfConstruction = loanQuoteCostOfConstruction;
	}

	public Integer getLoanQuoteCountryId() {
		return this.loanQuoteCountryId;
	}

	public void setLoanQuoteCountryId(Integer loanQuoteCountryId) {
		this.loanQuoteCountryId = loanQuoteCountryId;
	}

	public Integer getLoanQuoteCreatedLmsUserId() {
		return this.loanQuoteCreatedLmsUserId;
	}

	public void setLoanQuoteCreatedLmsUserId(Integer loanQuoteCreatedLmsUserId) {
		this.loanQuoteCreatedLmsUserId = loanQuoteCreatedLmsUserId;
	}

	public Double getLoanQuoteCurrentValueOfProperty() {
		return this.loanQuoteCurrentValueOfProperty;
	}

	public void setLoanQuoteCurrentValueOfProperty(Double loanQuoteCurrentValueOfProperty) {
		this.loanQuoteCurrentValueOfProperty = loanQuoteCurrentValueOfProperty;
	}

	public String getLoanQuoteDeleted() {
		return this.loanQuoteDeleted;
	}

	public void setLoanQuoteDeleted(String loanQuoteDeleted) {
		this.loanQuoteDeleted = loanQuoteDeleted;
	}

	public Double getLoanQuoteDepreciation() {
		return this.loanQuoteDepreciation;
	}

	public void setLoanQuoteDepreciation(Double loanQuoteDepreciation) {
		this.loanQuoteDepreciation = loanQuoteDepreciation;
	}

	public Integer getLoanQuoteEmployerCompanyId() {
		return this.loanQuoteEmployerCompanyId;
	}

	public void setLoanQuoteEmployerCompanyId(Integer loanQuoteEmployerCompanyId) {
		this.loanQuoteEmployerCompanyId = loanQuoteEmployerCompanyId;
	}

	public String getLoanQuoteEmployerName() {
		return this.loanQuoteEmployerName;
	}

	public void setLoanQuoteEmployerName(String loanQuoteEmployerName) {
		this.loanQuoteEmployerName = loanQuoteEmployerName;
	}

	public Integer getLoanQuoteEmploymentTypeId() {
		return this.loanQuoteEmploymentTypeId;
	}

	public void setLoanQuoteEmploymentTypeId(Integer loanQuoteEmploymentTypeId) {
		this.loanQuoteEmploymentTypeId = loanQuoteEmploymentTypeId;
	}

	public Date getLoanQuoteEntryTime() {
		return this.loanQuoteEntryTime;
	}

	public void setLoanQuoteEntryTime(Date loanQuoteEntryTime) {
		this.loanQuoteEntryTime = loanQuoteEntryTime;
	}

	public Date getLoanQuoteExistingHomeLoanStartDate() {
		return this.loanQuoteExistingHomeLoanStartDate;
	}

	public void setLoanQuoteExistingHomeLoanStartDate(Date loanQuoteExistingHomeLoanStartDate) {
		this.loanQuoteExistingHomeLoanStartDate = loanQuoteExistingHomeLoanStartDate;
	}

	public String getLoanQuoteExistingHomeLoanWithSbi() {
		return this.loanQuoteExistingHomeLoanWithSbi;
	}

	public void setLoanQuoteExistingHomeLoanWithSbi(String loanQuoteExistingHomeLoanWithSbi) {
		this.loanQuoteExistingHomeLoanWithSbi = loanQuoteExistingHomeLoanWithSbi;
	}

	public Double getLoanQuoteExpectedRentalIncome() {
		return this.loanQuoteExpectedRentalIncome;
	}

	public void setLoanQuoteExpectedRentalIncome(Double loanQuoteExpectedRentalIncome) {
		this.loanQuoteExpectedRentalIncome = loanQuoteExpectedRentalIncome;
	}

	public String getLoanQuoteGender() {
		return this.loanQuoteGender;
	}

	public void setLoanQuoteGender(String loanQuoteGender) {
		this.loanQuoteGender = loanQuoteGender;
	}

	public String getLoanQuoteHaveSalaryAccountWithSbi() {
		return this.loanQuoteHaveSalaryAccountWithSbi;
	}

	public void setLoanQuoteHaveSalaryAccountWithSbi(String loanQuoteHaveSalaryAccountWithSbi) {
		this.loanQuoteHaveSalaryAccountWithSbi = loanQuoteHaveSalaryAccountWithSbi;
	}

	public String getLoanQuoteHtmlContent() {
		return this.loanQuoteHtmlContent;
	}

	public void setLoanQuoteHtmlContent(String loanQuoteHtmlContent) {
		this.loanQuoteHtmlContent = loanQuoteHtmlContent;
	}

	public Double getLoanQuoteIncomeFromRegularSource() {
		return this.loanQuoteIncomeFromRegularSource;
	}

	public void setLoanQuoteIncomeFromRegularSource(Double loanQuoteIncomeFromRegularSource) {
		this.loanQuoteIncomeFromRegularSource = loanQuoteIncomeFromRegularSource;
	}

	public String getLoanQuoteIpAddress() {
		return this.loanQuoteIpAddress;
	}

	public void setLoanQuoteIpAddress(String loanQuoteIpAddress) {
		this.loanQuoteIpAddress = loanQuoteIpAddress;
	}

	public Double getLoanQuoteLandCost() {
		return this.loanQuoteLandCost;
	}

	public void setLoanQuoteLandCost(Double loanQuoteLandCost) {
		this.loanQuoteLandCost = loanQuoteLandCost;
	}

	public Integer getLoanQuoteLeadId() {
		return this.loanQuoteLeadId;
	}

	public void setLoanQuoteLeadId(Integer loanQuoteLeadId) {
		this.loanQuoteLeadId = loanQuoteLeadId;
	}

	public Double getLoanQuoteLoanAmount() {
		return this.loanQuoteLoanAmount;
	}

	public void setLoanQuoteLoanAmount(Double loanQuoteLoanAmount) {
		this.loanQuoteLoanAmount = loanQuoteLoanAmount;
	}

	public Integer getLoanQuoteLoanCategoryId() {
		return this.loanQuoteLoanCategoryId;
	}

	public void setLoanQuoteLoanCategoryId(Integer loanQuoteLoanCategoryId) {
		this.loanQuoteLoanCategoryId = loanQuoteLoanCategoryId;
	}

	public Double getLoanQuoteLoanOutstandingBalance() {
		return this.loanQuoteLoanOutstandingBalance;
	}

	public void setLoanQuoteLoanOutstandingBalance(Double loanQuoteLoanOutstandingBalance) {
		this.loanQuoteLoanOutstandingBalance = loanQuoteLoanOutstandingBalance;
	}

	public Integer getLoanQuoteLoanPurposeId() {
		return this.loanQuoteLoanPurposeId;
	}

	public void setLoanQuoteLoanPurposeId(Integer loanQuoteLoanPurposeId) {
		this.loanQuoteLoanPurposeId = loanQuoteLoanPurposeId;
	}

	public Integer getLoanQuoteLoanTenure() {
		return this.loanQuoteLoanTenure;
	}

	public void setLoanQuoteLoanTenure(Integer loanQuoteLoanTenure) {
		this.loanQuoteLoanTenure = loanQuoteLoanTenure;
	}

	public Double getLoanQuoteLoanTopupAmountRequested() {
		return this.loanQuoteLoanTopupAmountRequested;
	}

	public void setLoanQuoteLoanTopupAmountRequested(Double loanQuoteLoanTopupAmountRequested) {
		this.loanQuoteLoanTopupAmountRequested = loanQuoteLoanTopupAmountRequested;
	}

	public Integer getLoanQuoteLoanWithBankId() {
		return this.loanQuoteLoanWithBankId;
	}

	public void setLoanQuoteLoanWithBankId(Integer loanQuoteLoanWithBankId) {
		this.loanQuoteLoanWithBankId = loanQuoteLoanWithBankId;
	}

	public Integer getLoanQuoteMonthExistingHomeLoanStartDate() {
		return this.loanQuoteMonthExistingHomeLoanStartDate;
	}

	public void setLoanQuoteMonthExistingHomeLoanStartDate(Integer loanQuoteMonthExistingHomeLoanStartDate) {
		this.loanQuoteMonthExistingHomeLoanStartDate = loanQuoteMonthExistingHomeLoanStartDate;
	}

	public Integer getLoanQuoteMonthTenorExistingHomeLoan() {
		return this.loanQuoteMonthTenorExistingHomeLoan;
	}

	public void setLoanQuoteMonthTenorExistingHomeLoan(Integer loanQuoteMonthTenorExistingHomeLoan) {
		this.loanQuoteMonthTenorExistingHomeLoan = loanQuoteMonthTenorExistingHomeLoan;
	}

	public String getLoanQuoteMortgageCreated() {
		return this.loanQuoteMortgageCreated;
	}

	public void setLoanQuoteMortgageCreated(String loanQuoteMortgageCreated) {
		this.loanQuoteMortgageCreated = loanQuoteMortgageCreated;
	}

	public Double getLoanQuoteNetAnnualIncome() {
		return this.loanQuoteNetAnnualIncome;
	}

	public void setLoanQuoteNetAnnualIncome(Double loanQuoteNetAnnualIncome) {
		this.loanQuoteNetAnnualIncome = loanQuoteNetAnnualIncome;
	}

	public Double getLoanQuoteNetMonthlySalary() {
		return this.loanQuoteNetMonthlySalary;
	}

	public void setLoanQuoteNetMonthlySalary(Double loanQuoteNetMonthlySalary) {
		this.loanQuoteNetMonthlySalary = loanQuoteNetMonthlySalary;
	}

	public Double getLoanQuoteOtherIncome() {
		return this.loanQuoteOtherIncome;
	}

	public void setLoanQuoteOtherIncome(Double loanQuoteOtherIncome) {
		this.loanQuoteOtherIncome = loanQuoteOtherIncome;
	}

	public Double getLoanQuoteOutstandingLoanAmount() {
		return this.loanQuoteOutstandingLoanAmount;
	}

	public void setLoanQuoteOutstandingLoanAmount(Double loanQuoteOutstandingLoanAmount) {
		this.loanQuoteOutstandingLoanAmount = loanQuoteOutstandingLoanAmount;
	}

	public String getLoanQuotePossessionCompleted() {
		return this.loanQuotePossessionCompleted;
	}

	public void setLoanQuotePossessionCompleted(String loanQuotePossessionCompleted) {
		this.loanQuotePossessionCompleted = loanQuotePossessionCompleted;
	}


	public Double getLoanQuotePreEMIs() {
		return loanQuotePreEMIs;
	}

	public void setLoanQuotePreEMIs(Double loanQuotePreEMIs) {
		this.loanQuotePreEMIs = loanQuotePreEMIs;
	}

	public Integer getLoanQuotePreferredCityId() {
		return this.loanQuotePreferredCityId;
	}

	public void setLoanQuotePreferredCityId(Integer loanQuotePreferredCityId) {
		this.loanQuotePreferredCityId = loanQuotePreferredCityId;
	}

	public Integer getLoanQuotePreferredDistrictId() {
		return this.loanQuotePreferredDistrictId;
	}

	public void setLoanQuotePreferredDistrictId(Integer loanQuotePreferredDistrictId) {
		this.loanQuotePreferredDistrictId = loanQuotePreferredDistrictId;
	}

	public Integer getLoanQuotePreferredLocalityId() {
		return this.loanQuotePreferredLocalityId;
	}

	public void setLoanQuotePreferredLocalityId(Integer loanQuotePreferredLocalityId) {
		this.loanQuotePreferredLocalityId = loanQuotePreferredLocalityId;
	}

	public Integer getLoanQuotePreferredLocationOfAvailingLoan() {
		return this.loanQuotePreferredLocationOfAvailingLoan;
	}

	public void setLoanQuotePreferredLocationOfAvailingLoan(Integer loanQuotePreferredLocationOfAvailingLoan) {
		this.loanQuotePreferredLocationOfAvailingLoan = loanQuotePreferredLocationOfAvailingLoan;
	}

	public Integer getLoanQuotePreferredStateId() {
		return this.loanQuotePreferredStateId;
	}

	public void setLoanQuotePreferredStateId(Integer loanQuotePreferredStateId) {
		this.loanQuotePreferredStateId = loanQuotePreferredStateId;
	}

	public Double getLoanQuotePresentValueOfProperty() {
		return this.loanQuotePresentValueOfProperty;
	}

	public void setLoanQuotePresentValueOfProperty(Double loanQuotePresentValueOfProperty) {
		this.loanQuotePresentValueOfProperty = loanQuotePresentValueOfProperty;
	}

	public Double getLoanQuoteProfitAfterTax() {
		return this.loanQuoteProfitAfterTax;
	}

	public void setLoanQuoteProfitAfterTax(Double loanQuoteProfitAfterTax) {
		this.loanQuoteProfitAfterTax = loanQuoteProfitAfterTax;
	}

	public Integer getLoanQuotePropertyType() {
		return this.loanQuotePropertyType;
	}

	public void setLoanQuotePropertyType(Integer loanQuotePropertyType) {
		this.loanQuotePropertyType = loanQuotePropertyType;
	}

	

	public Double getLoanQuoteRenovationCost() {
		return this.loanQuoteRenovationCost;
	}

	public void setLoanQuoteRenovationCost(Double loanQuoteRenovationCost) {
		this.loanQuoteRenovationCost = loanQuoteRenovationCost;
	}

	public Integer getLoanQuoteResidentTypeId() {
		return this.loanQuoteResidentTypeId;
	}

	public void setLoanQuoteResidentTypeId(Integer loanQuoteResidentTypeId) {
		this.loanQuoteResidentTypeId = loanQuoteResidentTypeId;
	}

	public Integer getLoanQuoteRetirementAgeApplicant() {
		return this.loanQuoteRetirementAgeApplicant;
	}

	public void setLoanQuoteRetirementAgeApplicant(Integer loanQuoteRetirementAgeApplicant) {
		this.loanQuoteRetirementAgeApplicant = loanQuoteRetirementAgeApplicant;
	}

	public Integer getLoanQuoteSourceId() {
		return this.loanQuoteSourceId;
	}

	public void setLoanQuoteSourceId(Integer loanQuoteSourceId) {
		this.loanQuoteSourceId = loanQuoteSourceId;
	}

	public Integer getLoanQuoteTenorExistingHomeLoan() {
		return this.loanQuoteTenorExistingHomeLoan;
	}

	public void setLoanQuoteTenorExistingHomeLoan(Integer loanQuoteTenorExistingHomeLoan) {
		this.loanQuoteTenorExistingHomeLoan = loanQuoteTenorExistingHomeLoan;
	}

	public Double getLoanQuoteTotalTopupAmountTaken() {
		return this.loanQuoteTotalTopupAmountTaken;
	}

	public void setLoanQuoteTotalTopupAmountTaken(Double loanQuoteTotalTopupAmountTaken) {
		this.loanQuoteTotalTopupAmountTaken = loanQuoteTotalTopupAmountTaken;
	}

	public Date getLoanQuoteUpdatedTime() {
		return this.loanQuoteUpdatedTime;
	}

	public void setLoanQuoteUpdatedTime(Date loanQuoteUpdatedTime) {
		this.loanQuoteUpdatedTime = loanQuoteUpdatedTime;
	}

	public Double getLoanQuoteVariableMonthPay() {
		return this.loanQuoteVariableMonthPay;
	}

	public void setLoanQuoteVariableMonthPay(Double loanQuoteVariableMonthPay) {
		this.loanQuoteVariableMonthPay = loanQuoteVariableMonthPay;
	}

	public Integer getLoanQuoteVisitId() {
		return this.loanQuoteVisitId;
	}

	public void setLoanQuoteVisitId(Integer loanQuoteVisitId) {
		this.loanQuoteVisitId = loanQuoteVisitId;
	}

	public Integer getLoanQuoteYearExistingHomeLoanStartDate() {
		return this.loanQuoteYearExistingHomeLoanStartDate;
	}

	public void setLoanQuoteYearExistingHomeLoanStartDate(Integer loanQuoteYearExistingHomeLoanStartDate) {
		this.loanQuoteYearExistingHomeLoanStartDate = loanQuoteYearExistingHomeLoanStartDate;
	}

	public Integer getLoanQuoteYearTenorExistingHomeLoan() {
		return this.loanQuoteYearTenorExistingHomeLoan;
	}

	public void setLoanQuoteYearTenorExistingHomeLoan(Integer loanQuoteYearTenorExistingHomeLoan) {
		this.loanQuoteYearTenorExistingHomeLoan = loanQuoteYearTenorExistingHomeLoan;
	}

	

	public Integer getLoanQuoteWorkExperience() {
		return loanQuoteWorkExperience;
	}

	public void setLoanQuoteWorkExperience(Integer loanQuoteWorkExperience) {
		this.loanQuoteWorkExperience = loanQuoteWorkExperience;
	}

	public Double getLoanQuoteNetMonthlyPension() {
		return loanQuoteNetMonthlyPension;
	}

	public void setLoanQuoteNetMonthlyPension(Double loanQuoteNetMonthlyPension) {
		this.loanQuoteNetMonthlyPension = loanQuoteNetMonthlyPension;
	}
	public Double getLoanQuoteCoapplicantFirstMonthlySalary() {
		return loanQuoteCoapplicantFirstMonthlySalary;
	}

	public void setLoanQuoteCoapplicantFirstMonthlySalary(Double loanQuoteCoapplicantFirstMonthlySalary) {
		this.loanQuoteCoapplicantFirstMonthlySalary = loanQuoteCoapplicantFirstMonthlySalary;
	}

	public Double getLoanQuoteCoapplicantSecondMonthlySalary() {
		return loanQuoteCoapplicantSecondMonthlySalary;
	}

	public void setLoanQuoteCoapplicantSecondMonthlySalary(
			Double loanQuoteCoapplicantSecondMonthlySalary) {
		this.loanQuoteCoapplicantSecondMonthlySalary = loanQuoteCoapplicantSecondMonthlySalary;
	}
	public Date getLoanQuoteEmiStartDateOfExistingTopupLoan() {
		return loanQuoteEmiStartDateOfExistingTopupLoan;
	}

	public void setLoanQuoteEmiStartDateOfExistingTopupLoan(
			Date loanQuoteEmiStartDateOfExistingTopupLoan) {
		this.loanQuoteEmiStartDateOfExistingTopupLoan = loanQuoteEmiStartDateOfExistingTopupLoan;
	}

	public Integer getLoanQuoteCoapplicantFirstWorkExperience() {
		return loanQuoteCoapplicantFirstWorkExperience;
	}

	public Integer getLoanQuoteCoapplicantSecondWorkExperience() {
		return loanQuoteCoapplicantSecondWorkExperience;
	}

	public void setLoanQuoteCoapplicantFirstWorkExperience(
			Integer loanQuoteCoapplicantFirstWorkExperience) {
		this.loanQuoteCoapplicantFirstWorkExperience = loanQuoteCoapplicantFirstWorkExperience;
	}

	public void setLoanQuoteCoapplicantSecondWorkExperience(
			Integer loanQuoteCoapplicantSecondWorkExperience) {
		this.loanQuoteCoapplicantSecondWorkExperience = loanQuoteCoapplicantSecondWorkExperience;
	}

	public Date getLoanQuoteExistingHomeLoanEndDate() {
		return loanQuoteExistingHomeLoanEndDate;
	}

	public void setLoanQuoteExistingHomeLoanEndDate(
			Date loanQuoteExistingHomeLoanEndDate) {
		this.loanQuoteExistingHomeLoanEndDate = loanQuoteExistingHomeLoanEndDate;
	}
	

	public Double getLoanQuoteCoapplicantFirstNetMonthlyPension() {
		return loanQuoteCoapplicantFirstNetMonthlyPension;
	}

	public void setLoanQuoteCoapplicantFirstNetMonthlyPension(
			Double loanQuoteCoapplicantFirstNetMonthlyPension) {
		this.loanQuoteCoapplicantFirstNetMonthlyPension = loanQuoteCoapplicantFirstNetMonthlyPension;
	}
	public Double getLoanQuoteCoapplicantSecondNetMonthlyPension() {
		return loanQuoteCoapplicantSecondNetMonthlyPension;
	}

	public void setLoanQuoteCoapplicantSecondNetMonthlyPension(
			Double loanQuoteCoapplicantSecondNetMonthlyPension) {
		this.loanQuoteCoapplicantSecondNetMonthlyPension = loanQuoteCoapplicantSecondNetMonthlyPension;
	}

	

	public String getLoanQuotePreferredCityCategory() {
		return loanQuotePreferredCityCategory;
	}

	public void setLoanQuotePreferredCityCategory(
			String loanQuotePreferredCityCategory) {
		this.loanQuotePreferredCityCategory = loanQuotePreferredCityCategory;
	}

	

	public String getLoanQuoteCoapplicantFirstCoEmplyerName() {
		return loanQuoteCoapplicantFirstCoEmplyerName;
	}

	public String getLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi() {
		return loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;
	}

	public void setLoanQuoteCoapplicantFirstCoEmplyerName(
	        String loanQuoteCoapplicantFirstCoEmplyerName) {
		this.loanQuoteCoapplicantFirstCoEmplyerName = loanQuoteCoapplicantFirstCoEmplyerName;
	}

	public void setLoanQuoteCoapplicantFirstCoSalaryAccountWithSbi(
	        String loanQuoteCoapplicantFirstCoSalaryAccountWithSbi) {
		this.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi = loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;
	}

	public String getLoanQuoteCoapplicantSecondCoEmplyerName() {
		return loanQuoteCoapplicantSecondCoEmplyerName;
	}

	public void setLoanQuoteCoapplicantSecondCoEmplyerName(
			String loanQuoteCoapplicantSecondCoEmplyerName) {
		this.loanQuoteCoapplicantSecondCoEmplyerName = loanQuoteCoapplicantSecondCoEmplyerName;
	}

	public String getLoanQuoteCoapplicantSecondCoSalaryAccountWithSbi() {
		return loanQuoteCoapplicantSecondCoSalaryAccountWithSbi;
	}

	public void setLoanQuoteCoapplicantSecondCoSalaryAccountWithSbi(
			String loanQuoteCoapplicantSecondCoSalaryAccountWithSbi) {
		this.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi = loanQuoteCoapplicantSecondCoSalaryAccountWithSbi;
	}

	public String getLoanQuoteAppliedCoupon() {
		return loanQuoteAppliedCoupon;
	}

	public void setLoanQuoteAppliedCoupon(String loanQuoteAppliedCoupon) {
		this.loanQuoteAppliedCoupon = loanQuoteAppliedCoupon;
	}
	public Integer getLoanQuoteEmiStartDateOfExistingTopupLoanMonth() {
		return loanQuoteEmiStartDateOfExistingTopupLoanMonth;
	}

	public void setLoanQuoteEmiStartDateOfExistingTopupLoanMonth(
			Integer loanQuoteEmiStartDateOfExistingTopupLoanMonth) {
		this.loanQuoteEmiStartDateOfExistingTopupLoanMonth = loanQuoteEmiStartDateOfExistingTopupLoanMonth;
	}

	public Integer getLoanQuoteEmiStartDateOfExistingTopupLoanYear() {
		return loanQuoteEmiStartDateOfExistingTopupLoanYear;
	}

	public void setLoanQuoteEmiStartDateOfExistingTopupLoanYear(
			Integer loanQuoteEmiStartDateOfExistingTopupLoanYear) {
		this.loanQuoteEmiStartDateOfExistingTopupLoanYear = loanQuoteEmiStartDateOfExistingTopupLoanYear;
	}

	public String getLoanQuoteAppFirstName() {
		return loanQuoteAppFirstName;
	}

	public void setLoanQuoteAppFirstName(String loanQuoteAppFirstName) {
		this.loanQuoteAppFirstName = loanQuoteAppFirstName;
	}

	public String getLoanQuoteAppMobileNo() {
		return loanQuoteAppMobileNo;
	}

	public void setLoanQuoteAppMobileNo(String loanQuoteAppMobileNo) {
		this.loanQuoteAppMobileNo = loanQuoteAppMobileNo;
	}

	public String getLoanQuoteAppWorkEmail() {
		return loanQuoteAppWorkEmail;
	}

	public void setLoanQuoteAppWorkEmail(String loanQuoteAppWorkEmail) {
		this.loanQuoteAppWorkEmail = loanQuoteAppWorkEmail;
	}
	public Integer getLoanQuoteCoapplicantFirstCoEmpId() {
		return loanQuoteCoapplicantFirstCoEmpId;
	}

	public void setLoanQuoteCoapplicantFirstCoEmpId(
			Integer loanQuoteCoapplicantFirstCoEmpId) {
		this.loanQuoteCoapplicantFirstCoEmpId = loanQuoteCoapplicantFirstCoEmpId;
	}

	public Integer getLoanQuoteCoapplicantSecondCoEmplyerId() {
		return loanQuoteCoapplicantSecondCoEmplyerId;
	}

	public void setLoanQuoteCoapplicantSecondCoEmplyerId(
			Integer loanQuoteCoapplicantSecondCoEmplyerId) {
		this.loanQuoteCoapplicantSecondCoEmplyerId = loanQuoteCoapplicantSecondCoEmplyerId;
	}
	
	public Integer getLoanQuoteBuilderId() {
		return loanQuoteBuilderId;
	}

	public void setLoanQuoteBuilderId(Integer loanQuoteBuilderId) {
		this.loanQuoteBuilderId = loanQuoteBuilderId;
	}

	public Integer getLoanQuoteCoapplicantFirstAge() {
		return loanQuoteCoapplicantFirstAge;
	}

	public void setLoanQuoteCoapplicantFirstAge(Integer loanQuoteCoapplicantFirstAge) {
		this.loanQuoteCoapplicantFirstAge = loanQuoteCoapplicantFirstAge;
	}

	public Integer getLoanQuoteCoapplicantSecondAge() {
		return loanQuoteCoapplicantSecondAge;
	}

	public void setLoanQuoteCoapplicantSecondAge(
			Integer loanQuoteCoapplicantSecondAge) {
		this.loanQuoteCoapplicantSecondAge = loanQuoteCoapplicantSecondAge;
	}

	public Integer getLoanQuoteAge() {
		return loanQuoteAge;
	}

	public void setLoanQuoteAge(Integer loanQuoteAge) {
		this.loanQuoteAge = loanQuoteAge;
	}

	public Integer getLoanQuoteIndustryType() {
		return loanQuoteIndustryType;
	}

	public void setLoanQuoteIndustryType(Integer loanQuoteIndustryType) {
		this.loanQuoteIndustryType = loanQuoteIndustryType;
	}

	public Integer getLoanQuoteSalaryPackage() {
		return loanQuoteSalaryPackage;
	}

	public void setLoanQuoteSalaryPackage(Integer loanQuoteSalaryPackage) {
		this.loanQuoteSalaryPackage = loanQuoteSalaryPackage;
	}

	public String getLoanQuoteFirstOwnerOfProperty() {
		return loanQuoteFirstOwnerOfProperty;
	}

	public void setLoanQuoteFirstOwnerOfProperty(
			String loanQuoteFirstOwnerOfProperty) {
		this.loanQuoteFirstOwnerOfProperty = loanQuoteFirstOwnerOfProperty;
	}

	public Integer getYearTenureOfExistingHomeLoan() {
		return yearTenureOfExistingHomeLoan;
	}

	public void setYearTenureOfExistingHomeLoan(Integer yearTenureOfExistingHomeLoan) {
		this.yearTenureOfExistingHomeLoan = yearTenureOfExistingHomeLoan;
	}

	public Integer getMonthTenureOfExistingHomeLoan() {
		return monthTenureOfExistingHomeLoan;
	}

	public void setMonthTenureOfExistingHomeLoan(
			Integer monthTenureOfExistingHomeLoan) {
		this.monthTenureOfExistingHomeLoan = monthTenureOfExistingHomeLoan;
	}

	public Integer getLoanQuoteLoanAccountType() {
		return loanQuoteLoanAccountType;
	}

	public void setLoanQuoteLoanAccountType(Integer loanQuoteLoanAccountType) {
		this.loanQuoteLoanAccountType = loanQuoteLoanAccountType;
	}

	public Integer getLoanQuoteLoanProductId() {
		return loanQuoteLoanProductId;
	}

	public void setLoanQuoteLoanProductId(Integer loanQuoteLoanProductId) {
		this.loanQuoteLoanProductId = loanQuoteLoanProductId;
	}

	public Integer getLoanQuoteProjectId() {
		return loanQuoteProjectId;
	}

	public void setLoanQuoteProjectId(Integer loanQuoteProjectId) {
		this.loanQuoteProjectId = loanQuoteProjectId;
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

	public Date getLoanQuoteCoapplicantFirstDateOfBirthDT() {
		return loanQuoteCoapplicantFirstDateOfBirthDT;
	}

	public void setLoanQuoteCoapplicantFirstDateOfBirthDT(
			Date loanQuoteCoapplicantFirstDateOfBirthDT) {
		this.loanQuoteCoapplicantFirstDateOfBirthDT = loanQuoteCoapplicantFirstDateOfBirthDT;
	}

	public String getLoanQuoteCoapplicantFirstDateOfBirth() {
		return loanQuoteCoapplicantFirstDateOfBirth;
	}

	public void setLoanQuoteCoapplicantFirstDateOfBirth(
			String loanQuoteCoapplicantFirstDateOfBirth) {
		this.loanQuoteCoapplicantFirstDateOfBirth = loanQuoteCoapplicantFirstDateOfBirth;
	}

	public Date getLoanQuoteCoapplicantSecondDateOfBirthDT() {
		return loanQuoteCoapplicantSecondDateOfBirthDT;
	}

	public void setLoanQuoteCoapplicantSecondDateOfBirthDT(
			Date loanQuoteCoapplicantSecondDateOfBirthDT) {
		this.loanQuoteCoapplicantSecondDateOfBirthDT = loanQuoteCoapplicantSecondDateOfBirthDT;
	}

	public String getLoanQuoteCoapplicantSecondDateOfBirth() {
		return loanQuoteCoapplicantSecondDateOfBirth;
	}

	public void setLoanQuoteCoapplicantSecondDateOfBirth(
			String loanQuoteCoapplicantSecondDateOfBirth) {
		this.loanQuoteCoapplicantSecondDateOfBirth = loanQuoteCoapplicantSecondDateOfBirth;
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

	public String getLoanQuoteCoapplicantFirstGender() {
		return loanQuoteCoapplicantFirstGender;
	}

	public void setLoanQuoteCoapplicantFirstGender(
			String loanQuoteCoapplicantFirstGender) {
		this.loanQuoteCoapplicantFirstGender = loanQuoteCoapplicantFirstGender;
	}

	public String getLoanQuoteCoapplicantSecondGender() {
		return loanQuoteCoapplicantSecondGender;
	}

	public void setLoanQuoteCoapplicantSecondGender(
			String loanQuoteCoapplicantSecondGender) {
		this.loanQuoteCoapplicantSecondGender = loanQuoteCoapplicantSecondGender;
	}

	public Integer getLoanQuoteIsEligibleForHarGhar() {
		return loanQuoteIsEligibleForHarGhar;
	}

	public void setLoanQuoteIsEligibleForHarGhar(
			Integer loanQuoteIsEligibleForHarGhar) {
		this.loanQuoteIsEligibleForHarGhar = loanQuoteIsEligibleForHarGhar;
	}

	public Integer getLoanQuoteYearCompletionDate() {
		return loanQuoteYearCompletionDate;
	}

	public void setLoanQuoteYearCompletionDate(Integer loanQuoteYearCompletionDate) {
		this.loanQuoteYearCompletionDate = loanQuoteYearCompletionDate;
	}

	public Integer getLoanQuoteMonthCompletionDate() {
		return loanQuoteMonthCompletionDate;
	}

	public void setLoanQuoteMonthCompletionDate(Integer loanQuoteMonthCompletionDate) {
		this.loanQuoteMonthCompletionDate = loanQuoteMonthCompletionDate;
	}

	public Date getLoanQuoteCompletionDate() {
		return loanQuoteCompletionDate;
	}

	public void setLoanQuoteCompletionDate(Date loanQuoteCompletionDate) {
		this.loanQuoteCompletionDate = loanQuoteCompletionDate;
	}

	public String getLoanQuoteIsAvailReimbursement() {
		return loanQuoteIsAvailReimbursement;
	}

	public void setLoanQuoteIsAvailReimbursement(
			String loanQuoteIsAvailReimbursement) {
		this.loanQuoteIsAvailReimbursement = loanQuoteIsAvailReimbursement;
	}

	public Integer getLoanQuoteActualInvestmentMade() {
		return loanQuoteActualInvestmentMade;
	}

	public void setLoanQuoteActualInvestmentMade(
			Integer loanQuoteActualInvestmentMade) {
		this.loanQuoteActualInvestmentMade = loanQuoteActualInvestmentMade;
	}

	public Double getLoanQuotePreEMIsOther() {
		return loanQuotePreEMIsOther;
	}

	public void setLoanQuotePreEMIsOther(Double loanQuotePreEMIsOther) {
		this.loanQuotePreEMIsOther = loanQuotePreEMIsOther;
	}
	
	public Integer getLoanQuoteCheckOffType() {
		return loanQuoteCheckOffType;
	}

	public void setLoanQuoteCheckOffType(Integer loanQuoteCheckOffType) {
		this.loanQuoteCheckOffType = loanQuoteCheckOffType;
	}
	
	public Integer getLoanPurpose() {
		return loanPurpose;
	}

	public void setLoanPurpose(Integer loanPurpose) {
		this.loanPurpose = loanPurpose;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanQuoteMiddleName() {
		return loanQuoteMiddleName;
	}

	public void setLoanQuoteMiddleName(String loanQuoteMiddleName) {
		this.loanQuoteMiddleName = loanQuoteMiddleName;
	}

	public String getLoanQuoteLastName() {
		return loanQuoteLastName;
	}
	public void setLoanQuoteLastName(String loanQuoteLastName) {
		this.loanQuoteLastName = loanQuoteLastName;
	}

	public String getLoanQuoteReimburse() {
		return loanQuoteReimburse;
	}

	public void setLoanQuoteReimburse(String loanQuoteReimburse) {
		this.loanQuoteReimburse = loanQuoteReimburse;
	}

	public Double getLoanQuoteAmountInvested() {
		return loanQuoteAmountInvested;
	}

	public void setLoanQuoteAmountInvested(Double loanQuoteAmountInvested) {
		this.loanQuoteAmountInvested = loanQuoteAmountInvested;
	}

	public Double getLoanQuotePropertyMarketValue() {
		return loanQuotePropertyMarketValue;
	}

	public void setLoanQuotePropertyMarketValue(Double loanQuotePropertyMarketValue) {
		this.loanQuotePropertyMarketValue = loanQuotePropertyMarketValue;
	}

	public Integer getLoanQuoteMaritalStatus() {
		return loanQuoteMaritalStatus;
	}

	public void setLoanQuoteMaritalStatus(Integer loanQuoteMaritalStatus) {
		this.loanQuoteMaritalStatus = loanQuoteMaritalStatus;
	}
	
	public int getAppApplyingFrom() {
		return appApplyingFrom;
	}

	public void setAppApplyingFrom(int appApplyingFrom) {
		this.appApplyingFrom = appApplyingFrom;
	}
	
	
	public String getLoanQuotePuccaHouse() {
		return loanQuotePuccaHouse;
	}

	public void setLoanQuotePuccaHouse(String loanQuotePuccaHouse) {
		this.loanQuotePuccaHouse = loanQuotePuccaHouse;
	}
	

	public String getLoanQuotePmayItr() {
		return loanQuotePmayItr;
	}

	public void setLoanQuotePmayItr(String loanQuotePmayItr) {
		this.loanQuotePmayItr = loanQuotePmayItr;
	}
	

	public Integer getLoanQuoteEligiblePmay() {
		return loanQuoteEligiblePmay;
	}

	public void setLoanQuoteEligiblePmay(Integer loanQuoteEligiblePmay) {
		this.loanQuoteEligiblePmay = loanQuoteEligiblePmay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public Integer getLoanQuoteCibilScore() {
		return loanQuoteCibilScore;
	}

	public void setLoanQuoteCibilScore(Integer loanQuoteCibilScore) {
		this.loanQuoteCibilScore = loanQuoteCibilScore;
	}
   
	public String getLoanQuotePanCardNo() {
		return loanQuotePanCardNo;
	}

	public void setLoanQuotePanCardNo(String loanQuotePanCardNo) {
		this.loanQuotePanCardNo = loanQuotePanCardNo;
	}

	public String getLoanQuoteAddress1() {
		return loanQuoteAddress1;
	}

	public void setLoanQuoteAddress1(String loanQuoteAddress1) {
		this.loanQuoteAddress1 = loanQuoteAddress1;
	}

	public String getLoanQuoteAddress2() {
		return loanQuoteAddress2;
	}

	public void setLoanQuoteAddress2(String loanQuoteAddress2) {
		this.loanQuoteAddress2 = loanQuoteAddress2;
	}

	public String getLoanQuoteAddressLandmark() {
		return loanQuoteAddressLandmark;
	}

	public void setLoanQuoteAddressLandmark(String loanQuoteAddressLandmark) {
		this.loanQuoteAddressLandmark = loanQuoteAddressLandmark;
	}

	public Integer getLoanQuoteStateId() {
		return loanQuoteStateId;
	}

	public void setLoanQuoteStateId(Integer loanQuoteStateId) {
		this.loanQuoteStateId = loanQuoteStateId;
	}

	public Integer getLoanQuotePincode() {
		return loanQuotePincode;
	}

	public void setLoanQuotePincode(Integer loanQuotePincode) {
		this.loanQuotePincode = loanQuotePincode;
	}
	public String getAppMobile() {
		return appMobile;
	}

	public void setAppMobile(String appMobile) {
		this.appMobile = appMobile;
	}

	public Double getLoanQuoteLoanAmountTaken() {
		return loanQuoteLoanAmountTaken;
	}

	public void setLoanQuoteLoanAmountTaken(Double loanQuoteLoanAmountTaken) {
		this.loanQuoteLoanAmountTaken = loanQuoteLoanAmountTaken;
	}
	
	public String getAppEmail() {
		return appEmail;
	}

	public void setAppEmail(String appEmail) {
		this.appEmail = appEmail;
	}

	public Integer getLoanQuoteConsentId() {
		return loanQuoteConsentId;
	}

	public void setLoanQuoteConsentId(Integer loanQuoteConsentId) {
		this.loanQuoteConsentId = loanQuoteConsentId;
	}
	
	public String getQuotePrivacyConsentFlag() {
		return quotePrivacyConsentFlag;
	}

	public void setQuotePrivacyConsentFlag(String quotePrivacyConsentFlag) {
		this.quotePrivacyConsentFlag = quotePrivacyConsentFlag;
	}

	public String getQuoteNtbId() {
		return quoteNtbId;
	}

	public void setQuoteNtbId(String quoteNtbId) {
		this.quoteNtbId = quoteNtbId;
	}
   
}
