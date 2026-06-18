package com.mintstreet.loan.personal.entity;

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

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00440")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="G9", sequenceName="RUPEEPOWER_OCAS_SEQ_00255" ,allocationSize=1)
public class ApplicationFormPersonalLoanQuote extends com.mintstreet.common.entity.Domain<java.lang.Integer>  {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G9")
	@Column(name="QUOTE_SEQUENCE_ID")
	private Integer loanQuoteId;
	
	@Transient
	private Double loanQuoteLoanRequestedAmount;

	@Column(name="quote_active")
	private String loanQuoteActive;

	@Column(name="quote_applied_coupon")
	private String loanQuoteAppliedCoupon;

	@Column(name="quote_browser_name")
	private String loanQuoteBrowserName;

	@Column(name="quote_coapp_employer_name")
	private String loanQuoteCoapplicantEmployerName;
	
	@Column(name="QUOTE_COAPP_SAL_ACC_WITH_SBI")
	private String loanQuoteCoapplicantSalaryAccountWithSbi;

	@Column(name="quote_coapp_employment_type_id")
	private Integer loanQuoteCoapplicantEmploymentTypeId;

	@Column(name="quote_coapp_net_annual_income")
	private Double loanQuoteCoapplicantNetAnnualIncome;

	@Column(name="quote_coapp_relationship_id")
	private Integer loanQuoteCoapplicantRelationshipId;
	
	@Column(name="quote_coapp_profit_after_tax")
	private Double loanQuoteCoapplicantProfitAfterTax  ;
	
	@Column(name="quote_coapp_preEMIs")
	private Double loanQuoteCoapplicantPreEMIs ;
	
	@Column(name="QUOTE_COAPP_NET_MONTH_INCOME")
	private Double loanQuoteCoapplicantNetMonthlyIncome;
	
	@Column(name="QUOTE_COAPP_EXP_MON_REN_INCOME")
	private Double loanQuoteCoapplicantExpMonthlyRentalIncome;
	
	@Column(name="QUOTE_COAPP_OTHER_MON_INCOME")
	private Double loanQuoteCoapplicantOtherMonthlyIncome;
	
	@Column(name="QUOTE_COAPP_GROSS_MON_INCOME")
	private Double loanQuoteCoapplicantGrossMonthlySalary;
	
	@Column(name="quote_employerName")
	private String loanQuoteEmployerName;
	
	@Column(name="QUOTE_EMPLOYER_CATAGORY")
	private  Integer loanQuoteEmployerCat;
	
	@Column(name="QUOTE_EMPLOYER_NAME_ID")
	private Integer loanQuoteEmployerNameId;
	
	@Column(name="QUOTE_CHECK_OFF_VALUE")
	private Integer loanQuoteCheckOffValue;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_coapp_st_dt_of_cur_buss")
	private Date loanQuoteCoapplicantStartDateOfCurrentBusiness;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_coapp_st_dt_of_cur_prof")
	private Date loanQuoteCoapplicantStartDateOfCurrentProfession;
	
	@Transient
	private Integer loanQuoteYearCoapplicantJoining;
	@Transient
	private Integer loanQuoteMonthCoapplicantJoining;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_coapp_joining_date")
	private Date loanQuoteCoapplicantJoiningDate ;
	
	@Column(name="quote_coapp_industry_type")
	private Integer loanQuoteCoapplicantIndustryType;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_company_joining_date")
	private Date loanQuoteCompanyJoiningDate;
	
	@Column(name="quote_created_lms_user_id")
	private Integer loanQuoteCreatedLmsUserId;

	@Temporal(TemporalType.DATE)
	@Column(name="quote_date_of_birth")
	private Date loanQuoteDateOfBirthDT;
	
	@Transient
	private String loanQuoteDateOfBirth;

	@Transient
	private Integer loanQuoteAge;

	@Column(name="quote_deleted")
	private String loanQuoteDeleted;
	

	@Temporal(TemporalType.DATE)
	@Column(name="quote_entry_time")
	private Date loanQuoteEntryTime;

	@Column(name="QUOTE_EXIST_OR_EXP_MOTH_RENT")
	private Double loanQuoteExistingOrExpectedMothlyRent;

	@Column(name="quote_gender")
	private String loanQuoteGender;

	@Column(name="quote_has_property_rented")
	private String loanQuoteHasPropertyRented;

	@Column(name="QUOTE_HAVE_SAL_ACC_WITH_SBI")
	private String loanQuoteHaveSalaryAccountWithSbi;
	
	@Column(name="QUOTE_HAVE_PL_WITH_BANK")
	private String loanQuoteAvailPersonalLoanWithBankEarlier;

	@Column(name="QUOTE_SALARY_PACKAGE")
	private Integer loanQuoteSalaryPackage;
	
	@Column(name="quote_ip_address")
	private String loanQuoteIpAddress;

	@Column(name="quote_is_sbi_employee")
	private String loanQuoteIsSbiEmployee;

	@Column(name="quote_lead_id")
	private Integer loanQuoteLeadId;

	@Column(name="quote_loan_account_type")
	private Integer loanQuoteLoanAccountType;
	
	@Column(name="quote_loan_term_odtype")
	private Integer loanQuoteTermODType;

	@Column(name="quote_loan_amount")
	private Double loanQuoteLoanAmount;
	
	@Column(name="quote_loan_amount_taken")
	private Double loanQuoteLoanAmountTaken;

	@Column(name="quote_loan_opted_loan")
	private String loanQuoteLoanOptedLoan;

	@Column(name="quote_loan_product_id")
	private Integer loanQuoteLoanProductId;

	@Column(name="quote_loan_purpose_id")
	private Integer loanQuoteLoanPurposeId;

	@Column(name="quote_loan_tenure")
	private Integer loanQuoteLoanTenure;

	@Column(name="quote_loan_type")
	private Integer loanQuoteLoanType;

	@Column(name="quote_net_monthly_salary")
	private Double loanQuoteNetMonthlySalary;
	
	@Column(name="quote_net_annual_income")
	private Double loanQuoteAnnualIncome;
	
	@Column(name="QUOTE_GROSS_MONTHLY_SALARY")
	private Double loanQuoteGrossMonthlySalary;
	
	@Column(name="QUOTE_EXP_MON_REN_INCOME")
	private Double loanQuoteExpMonthlyRentalIncome;
	
	@Column(name="QUOTE_OTHER_MON_INCOME")
	private Double loanQuoteOtherMonthlyIncome;
	
	@Column(name="QUOTE_INCOME_FROM_REGULAR_SRC")
	private Double loanQuoteIncomeFromRegaularSource;
	
	@Column(name="quote_preEMIs")
	private Double loanQuotePreEMIs;
	
	@Column(name="quote_retirement_age_applicant")
	private Integer loanQuoteRetirementAgeApplicant;
	
	@Column(name="quote_employment_type")
	private Integer loanQuoteEmploymentType;

	@Column(name="quote_property_advance_deposit")
	private Double loanQuotePropertyAdvanceDeposit;

	@Column(name="quote_property_cost")
	private Double loanQuotePropertyCost;

	@Column(name="QUOTE_PROPERTY_OTH_LIABILITIES")
	private Double loanQuotePropertyOtherLiabilities;

	@Column(name="quote_property_state_id")
	private Integer loanQuotePropertyStateId;

	@Column(name="quote_property_district_id")
	private Integer loanQuotePropertyDistrictId;

	@Column(name="quote_property_branch_id")
	private Integer loanQuotePropertyBranchId;

	@Column(name="quote_property_owned")
	private String loanQuotePropertyOwnedBySpouse;
	
	@Column(name="quote_check_off_type")
	private Integer loanQuoteCheckOffType;
	
	@Column(name="quote_no_of_owner")
	private Integer loanQuoteNoOfOwner;
	
	@Column(name="quote_property_city_id")
	private Integer loanQuotePropertyCityId;

	@Column(name="quote_property_tax_payable")
	private Double loanQuotePropertyTaxPayable;

	@Column(name="quote_property_type")
	private Integer loanQuotePropertyType;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ST_DT_OF_CUR_BUIS")
	private Date loanQuoteStartDateOfCurrentBusiness;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_ST_DT_OF_CUR_PROFESS")
	private Date loanQuoteStartDateOfCurrentProfession;

	@Column(name="QUOTE_PROFESSION_ID")
	private String loanQuoteProfessionId;

	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_COAPP_DATE_OF_BIRTH")
	private Date loanQuoteCoapplicantDateOfBirthDT;
	
	@Transient
	private String loanQuoteCoapplicantDateOfBirth;
	
	@Column(name="QUOTE_COAPP_PROFESSON_ID")
	private Integer loanQuoteCoapplicantProfessionId;
	
	@Column(name="QUOTE_COAPP_RETIREMENT_AGE")
	private Integer loanQuoteCoapplicantRetirementAge;
	
	@Column(name="quote_coapp_resident_type_id")
	private Integer loanQuoteCoapplicantResidentTypeId;
	
	@Column(name="quote_resident_city_id")
	private Integer loanQuoteResidentCityId;

	@Column(name="quote_resident_country_id")
	private Integer loanQuoteResidentCountryId;

	@Column(name="quote_resident_state_id")
	private Integer loanQuoteResidentStateId;

	@Column(name="quote_resident_type_id")
	private Integer loanQuoteResidentTypeId;
	
	@Column(name="quote_source_id")
	private Integer loanQuoteSourceId;

	@Temporal(TemporalType.DATE)
	@Column(name="quote_st_dt_of_lease")
	private Date loanQuoteSTDOfLease;

	@Transient
	private String loanQuoteStartDateOfLease;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_end_date_of_lease")
	private Date loanQuoteEDOfLease;

	@Transient
	private String loanQuoteEndDateOfLease;
	
	@Temporal(TemporalType.DATE)
	@Column(name="quote_updated_time")
	private Date loanQuoteUpdatedTime;

	@Column(name="QUOTE_VISIT_ID")
	private Integer loanQuoteVisitId;

	@Column(name = "QUOTE_NEW_VISIT_ID")
	private Integer loanQuoteNewVisitId;

	@Column(name="quote_work_experience")
	private Integer loanQuoteWorkExperience;

	@Column(name="quote_work_state_id")
	private Integer loanQuoteWorkStateId;
	
	@Column(name="quote_work_city_id")
	private Integer loanQuoteWorkCityId;
	
	@Column(name="quote_work_district_id")
	private Integer loanQuoteWorkDistrictId;
	
	@Column(name="quote_work_branch_id")
	private Integer loanQuoteWorkBranchId;
	
	@Column(name="quote_profit_after_tax")
	private Double loanQuoteProfitAfterTax;
	
	@Column(name="quote_industry_type")
	private Integer loanQuoteIndustryType;
	
	@Column(name="QUOTE_INSTITUTE_NAME_ID")
	private Integer loanQuoteInstituteNameId;
	
	@Column(name="QUOTE_INSTITUTE_NAME")
	private String loanQuoteInstituteName;
	
	@Column(name="QUOTE_INSTITUTE_CATAGORY")
	private  String loanQuoteInstituteCat;
	
	@Column(name="QUOTE_COAPP_INSTITUTE_NAME")
	private String loanQuoteCoapplicantInstituteName;
	
	@Column(name="QUOTE_BRANCH_IS_CAPTIVE")
	private Integer loanQuoteBranchIsCaptive;
	
	@Transient
	private Integer loanQuotePensionPayingBranchId;
	
	@Transient
	private Integer loanQuotePensionPayingStateId;
	
	@Transient
	private Integer loanQuotePensionPayingCityId;
	
	@Transient
	private Integer loanQuotePensionPayingDistrId;
	
	@Transient
	private Integer loanQuotePreferredLoanBranch;
	
	@Transient
	private Integer loanQuotePreferredStateId;
	
	@Transient
	private Integer loanQuotePreferredCityId;
	
	@Transient
	private Integer loanQuotePreferredDistrictId;	
	
	@Column(name="QUOTE_PENSION_TYPE")
	private Integer loanQuotePensionType;
	
	@Column(name="QUOTE_PENSION_PAY_ORD_NUMBER")
	private String loanQuotePensionPayOrdNumber;
	
	@Column(name="QUOTE_MONTHLY_PENSION_AMT")
	private Integer loanQuoteMonthPensionAmt;
	
	@Column(name="QUOTE_MODE_OF_REPAYMENT")
	private Integer loanQuoteModeOfRepayment; 
	
	@Column(name="QUOTE_PREFERRED_LOAN_BRANCH_ID")
	private Integer loanQuotePreferredLoanBranchId;
	
	@Column(name="QUOTE_PENSION_ACCOUNT_NUMBER")
	private String loanQuotePensionAccountNumber;
	
	@Column(name="QUOTE_EMPLOYMENT_TYPE")
	private Integer loanQuoteEmplymentType;
	
	@Column(name="QUOTE_ORG_NAME")
	private Integer loanQuoteOrgName;
	
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
	private Integer loanQuoteYearStartDateOflease;
	@Transient
	private Integer loanQuoteMonthStartDateOflease;
	@Transient
	private Integer loanQuoteYearEndDateOfLease;
	@Transient
	private Integer loanQuoteMonthEndDateOfLease;
	@Transient
	private Integer loanQuoteYearCompanyJoining;
	@Transient
	private Integer loanQuoteMonthCompanyJoining;
	@Transient
	private Integer loanQuoteYearStartDateOfCurrentProfession;
	@Transient
	private Integer loanQuoteMonthStartDateOfCurrentProfession;
	@Transient
	private Integer loanQuoteYearStartDateOfCurrentBusiness;
	@Transient
	private Integer loanQuoteMonthStartDateOfCurrentBusiness;
	@Transient
	private Integer loanQuoteYearCoapplicantStartDateOfCurrentProfession;
	@Transient
	private Integer loanQuoteMonthCoapplicantStartDateOfCurrentProfession;
	@Transient
	private Integer loanQuoteYearCoapplicantStartDateOfCurrentBusiness;
	@Transient
	private Integer loanQuoteMonthCoapplicantStartDateOfCurrentBusiness;
	
	@Column(name="QUOTE_PREEMIS_OTHER")
	private Double loanQuotePreEMIsOther;
	
	@Column(name="QUOTE_EMPLOYMENT_NATURE")
	private Integer loanQuoteEmploymentNature;
	
	@Column(name="QUOTE_EXIST_SANCTION_AMOUNT")
	private Double loanQuoteExistingSanctionAmount;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_EXIST_PL_SANCTION_DATE")
	private Date loanQuoteExistingPLSanctionDT;
	
	@Transient
	private String loanQuoteExistingPLSanctionDate;

	
	@Column(name="QUOTE_EXIST_XPRESS_CREDIT_NO")
	private Integer loanQuoteExistingXpressCreditNo;
	
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
	
	@Column(name="QUOTE_CITY_ID")
	private Integer loanQuoteCityId;
	
	@Column(name="QUOTE_PINCODE")
	private Integer loanQuotePincode;
	
	@Column(name="QUOTE_PANCARD_NO")
	private String loanQuotePanCardNo;
	
	@Column(name="QUOTE_PANCARD_LATER")
	private Boolean loanQuotePanCardLater;
	
	@Column(name="QUOTE_OTHER_ID")
	private Integer loanQuoteOtherId;
	
	@Column(name="QUOTE_OTHER_ID_NUMBER")
	private String loanQuoteOtherIdNumber;
	
	@Column(name="QUOTE_AADHAAR_NUMBER")
	private String loanQuoteAadhaarNumber;
	
	@Column(name="QUOTE_CIBIL_SCORE")
	private Integer loanQuoteCibilScore;
	
	@Column(name="QUOTE_DISTRICT_ID")
	private Integer loanQuoteDistrictId;
	 
	@Column(name="QUOTE_BRANCH_LOCATION_ID")
	private Integer loanQuoteBranchLocationId;
	
	@Transient
	private float loanQuoteExistingPLRepaymentYear;
	
	@Column(name="QUOTE_COAPP_OTH_SOURCE_INCOME")
	private Double loanQuoteCoapplicantIncomeFromOtherSource;
	
	@Column(name="QUOTE_COAPP_NET_MONTH_PENSION")
	private Double loanQuoteCoapplicantNetMonthlyPension;
	
	@Transient
	private String requestStr;
	
	@Transient
	private String responseStr;
	
	@Column(name="QUOTE_OUTSTAND_LOAN_AMOUNT")
	private Double loanQuoteOutstandingLoanAmount;
	
	@Column(name="QUOTE_ORGANISATION_NAME")
	private String loanQuoteOrganisationName;
	
	@Column(name="QUOTE_LAST_MONTH_SAL_CREDITED")
	private Double loanQuoteLastMonthSalaryCredited;
	
	@Column(name="QUOTE_AVERAGE_LAST_SALARY")
	private Double loanQuoteAverageOfLastSalary;
	
	@Column(name="QUOTE_CURR_CONT_START_YEAR")
	private Integer loanQuoteCurrentContractStartYear;
	
	@Column(name="QUOTE_CURR_CONT_START_MONTH")
	private Integer loanQuoteCurrentContractStartMonth;
	
	@Column(name="QUOTE_CURR_CONT_END_YEAR")
	private Integer loanQuoteCurrentContractEndYear;
	
	@Column(name="QUOTE_CURR_CONT_END_MONTH")
	private Integer loanQuoteCurrentContractEndMonth;
	
	@Column(name="QUOTE_VARIABLE_MONTH_PAY")
	private Double loanQuoteVariableMonthlyPay;
	
	@Column(name="QUOTE_COAPP_VARIABLE_MONTH_PAY")
	private Double loanQuoteCoappVariableMonthPay;
	
	@Column(name="QUOTE_SALARY_PACKAGE_RANK")
	private Integer salaryPackageRank;

	@Column(name="QUOTE_COAPP_NAME")
	private String loanQuoteCoapplicantName;
	
	@Column(name="QUOTE_SALARY_ACCOUNT_SINCE")
	private Integer loanQuoteSalaryAccountSince;
	
	@Column(name="QUOTE_MODE_OF_SALARY_CREDIT")
	private Integer loanQuoteModeOfSalaryCredit;
	
	@Column(name= "QUOTE_OCCUPATION_TYPE_ID")
	private Integer loanQuoteOccupationTypeId;
	
	@Column(name="QUOTE_RESIDENCE_TYPE_ID")
	private Integer loanQuoteResidenceTypeId;
	
	@Column(name="QUOTE_RELATIONSHIP_WITH_BANK")
	private Integer loanQuoteRelationshipWithBank;
	
	@Column(name="QUOTE_DEPRECIATION")
	private Double loanQuoteDepreciation;
	
	@Column(name="QUOTE_NET_MONTHLY_PENSION")
	private Double loanQuoteNetMonthlyPension;
	
	@Column(name="QUOTE_INCOME_REGULAR_SOURCE")
	private Double loanQuoteIncomeFromRegularSource;

	@Column(name="QUOTE_HAVE_AADHAAR_NUMBER")
	private Integer loanQuoteHaveAadhaarNumber;
	
	@Column(name="QUOTE_HAVE_CO_OPERATIVE_BAK_AC")
	private Integer loanQuoteHaveCoOperativeBankAcc;
	
	@Column(name="QUOTE_INDUSTRY_TYPE_CAT")
	private Integer loanQuoteIndustryTypeCat;
	
	@Column(name="QUOTE_CBS_RELATIONSHIP_ID")
	private Integer cbsRelationShipId;
	
	  @Transient
	  private String alternateMobileNumber;
	  
	  @Transient
	  private String appAltISDCode;
	  
	@Column(name="QUOTE_GRAM_OF_GOLD")
	private Integer loanQuoteGramOfGold;
	
	@Transient
	private String loanQuotePreferredBranchName;
	
	@Transient
	private Integer loanQuoteConsentId;
	  
	
	public String getLoanQuotePreferredBranchName() {
		return loanQuotePreferredBranchName;
	}
	public void setLoanQuotePreferredBranchName(String loanQuotePreferredBranchName) {
		this.loanQuotePreferredBranchName = loanQuotePreferredBranchName;
	}
	public Integer getLoanQuoteGramOfGold() {
		return loanQuoteGramOfGold;
	}
	public void setLoanQuoteGramOfGold(Integer loanQuoteGramOfGold) {
		this.loanQuoteGramOfGold = loanQuoteGramOfGold;
	}
	public Integer getLoanQuoteId() {
		return loanQuoteId;
	}
	public void setLoanQuoteId(Integer loanQuoteId) {
		this.loanQuoteId = loanQuoteId;
	}
	public Double getLoanQuoteLoanRequestedAmount() {
		return loanQuoteLoanRequestedAmount;
	}
	public void setLoanQuoteLoanRequestedAmount(Double loanQuoteLoanRequestedAmount) {
		this.loanQuoteLoanRequestedAmount = loanQuoteLoanRequestedAmount;
	}
	public String getLoanQuoteActive() {
		return loanQuoteActive;
	}
	public void setLoanQuoteActive(String loanQuoteActive) {
		this.loanQuoteActive = loanQuoteActive;
	}
	public String getLoanQuoteAppliedCoupon() {
		return loanQuoteAppliedCoupon;
	}
	public void setLoanQuoteAppliedCoupon(String loanQuoteAppliedCoupon) {
		this.loanQuoteAppliedCoupon = loanQuoteAppliedCoupon;
	}
	public String getLoanQuoteBrowserName() {
		return loanQuoteBrowserName;
	}
	public void setLoanQuoteBrowserName(String loanQuoteBrowserName) {
		this.loanQuoteBrowserName = loanQuoteBrowserName;
	}
	
	public String getLoanQuoteCoapplicantEmployerName() {
		return loanQuoteCoapplicantEmployerName;
	}
	public void setLoanQuoteCoapplicantEmployerName(
			String loanQuoteCoapplicantEmployerName) {
		this.loanQuoteCoapplicantEmployerName = loanQuoteCoapplicantEmployerName;
	}
	public String getLoanQuoteCoapplicantSalaryAccountWithSbi() {
		return loanQuoteCoapplicantSalaryAccountWithSbi;
	}
	public void setLoanQuoteCoapplicantSalaryAccountWithSbi(
			String loanQuoteCoapplicantSalaryAccountWithSbi) {
		this.loanQuoteCoapplicantSalaryAccountWithSbi = loanQuoteCoapplicantSalaryAccountWithSbi;
	}

	public Integer getLoanQuoteCoapplicantEmploymentTypeId() {
		return loanQuoteCoapplicantEmploymentTypeId;
	}
	public void setLoanQuoteCoapplicantEmploymentTypeId(
			Integer loanQuoteCoapplicantEmploymentTypeId) {
		this.loanQuoteCoapplicantEmploymentTypeId = loanQuoteCoapplicantEmploymentTypeId;
	}
	public Double getLoanQuoteCoapplicantNetAnnualIncome() {
		return loanQuoteCoapplicantNetAnnualIncome;
	}
	public void setLoanQuoteCoapplicantNetAnnualIncome(
			Double loanQuoteCoapplicantNetAnnualIncome) {
		this.loanQuoteCoapplicantNetAnnualIncome = loanQuoteCoapplicantNetAnnualIncome;
	}
	public Integer getLoanQuoteCoapplicantRelationshipId() {
		return loanQuoteCoapplicantRelationshipId;
	}
	public void setLoanQuoteCoapplicantRelationshipId(
			Integer loanQuoteCoapplicantRelationshipId) {
		this.loanQuoteCoapplicantRelationshipId = loanQuoteCoapplicantRelationshipId;
	}
	public Double getLoanQuoteCoapplicantProfitAfterTax() {
		return loanQuoteCoapplicantProfitAfterTax;
	}
	public void setLoanQuoteCoapplicantProfitAfterTax(
			Double loanQuoteCoapplicantProfitAfterTax) {
		this.loanQuoteCoapplicantProfitAfterTax = loanQuoteCoapplicantProfitAfterTax;
	}
	public Double getLoanQuoteCoapplicantPreEMIs() {
		return loanQuoteCoapplicantPreEMIs;
	}
	public void setLoanQuoteCoapplicantPreEMIs(Double loanQuoteCoapplicantPreEMIs) {
		this.loanQuoteCoapplicantPreEMIs = loanQuoteCoapplicantPreEMIs;
	}
	public Double getLoanQuoteCoapplicantNetMonthlyIncome() {
		return loanQuoteCoapplicantNetMonthlyIncome;
	}
	public void setLoanQuoteCoapplicantNetMonthlyIncome(
			Double loanQuoteCoapplicantNetMonthlyIncome) {
		this.loanQuoteCoapplicantNetMonthlyIncome = loanQuoteCoapplicantNetMonthlyIncome;
	}
	public String getLoanQuoteEmployerName() {
		return loanQuoteEmployerName;
	}
	public void setLoanQuoteEmployerName(String loanQuoteEmployerName) {
		this.loanQuoteEmployerName = loanQuoteEmployerName;
	}
	public Date getLoanQuoteCoapplicantStartDateOfCurrentBusiness() {
		return loanQuoteCoapplicantStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteCoapplicantStartDateOfCurrentBusiness(
			Date loanQuoteCoapplicantStartDateOfCurrentBusiness) {
		this.loanQuoteCoapplicantStartDateOfCurrentBusiness = loanQuoteCoapplicantStartDateOfCurrentBusiness;
	}
	public Date getLoanQuoteCoapplicantStartDateOfCurrentProfession() {
		return loanQuoteCoapplicantStartDateOfCurrentProfession;
	}
	public void setLoanQuoteCoapplicantStartDateOfCurrentProfession(
			Date loanQuoteCoapplicantStartDateOfCurrentProfession) {
		this.loanQuoteCoapplicantStartDateOfCurrentProfession = loanQuoteCoapplicantStartDateOfCurrentProfession;
	}
	public Date getLoanQuoteCoapplicantJoiningDate() {
		return loanQuoteCoapplicantJoiningDate;
	}
	public void setLoanQuoteCoapplicantJoiningDate(
			Date loanQuoteCoapplicantJoiningDate) {
		this.loanQuoteCoapplicantJoiningDate = loanQuoteCoapplicantJoiningDate;
	}
	public Integer getLoanQuoteCoapplicantIndustryType() {
		return loanQuoteCoapplicantIndustryType;
	}
	public void setLoanQuoteCoapplicantIndustryType(
			Integer loanQuoteCoapplicantIndustryType) {
		this.loanQuoteCoapplicantIndustryType = loanQuoteCoapplicantIndustryType;
	}
	public Date getLoanQuoteCompanyJoiningDate() {
		return loanQuoteCompanyJoiningDate;
	}
	public void setLoanQuoteCompanyJoiningDate(Date loanQuoteCompanyJoiningDate) {
		this.loanQuoteCompanyJoiningDate = loanQuoteCompanyJoiningDate;
	}
	public Integer getLoanQuoteCreatedLmsUserId() {
		return loanQuoteCreatedLmsUserId;
	}
	public void setLoanQuoteCreatedLmsUserId(Integer loanQuoteCreatedLmsUserId) {
		this.loanQuoteCreatedLmsUserId = loanQuoteCreatedLmsUserId;
	}
	
	public Integer getLoanQuoteAge() {
		return loanQuoteAge;
	}
	public void setLoanQuoteAge(Integer loanQuoteAge) {
		this.loanQuoteAge = loanQuoteAge;
	}
	public String getLoanQuoteDeleted() {
		return loanQuoteDeleted;
	}
	public void setLoanQuoteDeleted(String loanQuoteDeleted) {
		this.loanQuoteDeleted = loanQuoteDeleted;
	}
	public Date getLoanQuoteEntryTime() {
		return loanQuoteEntryTime;
	}
	public void setLoanQuoteEntryTime(Date loanQuoteEntryTime) {
		this.loanQuoteEntryTime = loanQuoteEntryTime;
	}
	public Double getLoanQuoteExistingOrExpectedMothlyRent() {
		return loanQuoteExistingOrExpectedMothlyRent;
	}
	public void setLoanQuoteExistingOrExpectedMothlyRent(
			Double loanQuoteExistingOrExpectedMothlyRent) {
		this.loanQuoteExistingOrExpectedMothlyRent = loanQuoteExistingOrExpectedMothlyRent;
	}
	public String getLoanQuoteGender() {
		return loanQuoteGender;
	}
	public void setLoanQuoteGender(String loanQuoteGender) {
		this.loanQuoteGender = loanQuoteGender;
	}
	public String getLoanQuoteHasPropertyRented() {
		return loanQuoteHasPropertyRented;
	}
	public void setLoanQuoteHasPropertyRented(String loanQuoteHasPropertyRented) {
		this.loanQuoteHasPropertyRented = loanQuoteHasPropertyRented;
	}
	public String getLoanQuoteHaveSalaryAccountWithSbi() {
		return loanQuoteHaveSalaryAccountWithSbi;
	}
	public void setLoanQuoteHaveSalaryAccountWithSbi(
			String loanQuoteHaveSalaryAccountWithSbi) {
		this.loanQuoteHaveSalaryAccountWithSbi = loanQuoteHaveSalaryAccountWithSbi;
	}
	public String getLoanQuoteIpAddress() {
		return loanQuoteIpAddress;
	}
	public void setLoanQuoteIpAddress(String loanQuoteIpAddress) {
		this.loanQuoteIpAddress = loanQuoteIpAddress;
	}
	public String getLoanQuoteIsSbiEmployee() {
		return loanQuoteIsSbiEmployee;
	}
	public void setLoanQuoteIsSbiEmployee(String loanQuoteIsSbiEmployee) {
		this.loanQuoteIsSbiEmployee = loanQuoteIsSbiEmployee;
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
	public String getLoanQuoteLoanOptedLoan() {
		return loanQuoteLoanOptedLoan;
	}
	public void setLoanQuoteLoanOptedLoan(String loanQuoteLoanOptedLoan) {
		this.loanQuoteLoanOptedLoan = loanQuoteLoanOptedLoan;
	}
	public Integer getLoanQuoteLoanProductId() {
		return loanQuoteLoanProductId;
	}
	public void setLoanQuoteLoanProductId(Integer loanQuoteLoanProductId) {
		this.loanQuoteLoanProductId = loanQuoteLoanProductId;
	}
	public Integer getLoanQuoteLoanPurposeId() {
		return loanQuoteLoanPurposeId;
	}
	public void setLoanQuoteLoanPurposeId(Integer loanQuoteLoanPurposeId) {
		this.loanQuoteLoanPurposeId = loanQuoteLoanPurposeId;
	}
	public Integer getLoanQuoteLoanTenure() {
		return loanQuoteLoanTenure;
	}
	public void setLoanQuoteLoanTenure(Integer loanQuoteLoanTenure) {
		this.loanQuoteLoanTenure = loanQuoteLoanTenure;
	}
	public Integer getLoanQuoteLoanType() {
		return loanQuoteLoanType;
	}
	public void setLoanQuoteLoanType(Integer loanQuoteLoanType) {
		this.loanQuoteLoanType = loanQuoteLoanType;
	}
	public Double getLoanQuoteNetMonthlySalary() {
		return loanQuoteNetMonthlySalary;
	}
	public void setLoanQuoteNetMonthlySalary(Double loanQuoteNetMonthlySalary) {
		this.loanQuoteNetMonthlySalary = loanQuoteNetMonthlySalary;
	}
	
	public Double getLoanQuotePreEMIs() {
		return loanQuotePreEMIs;
	}
	public void setLoanQuotePreEMIs(Double loanQuotePreEMIs) {
		this.loanQuotePreEMIs = loanQuotePreEMIs;
	}

	public Integer getLoanQuoteRetirementAgeApplicant() {
		return loanQuoteRetirementAgeApplicant;
	}
	public void setLoanQuoteRetirementAgeApplicant(
			Integer loanQuoteRetirementAgeApplicant) {
		this.loanQuoteRetirementAgeApplicant = loanQuoteRetirementAgeApplicant;
	}
	public Integer getLoanQuoteEmploymentType() {
		return loanQuoteEmploymentType;
	}
	public void setLoanQuoteEmploymentType(Integer loanQuoteEmploymentType) {
		this.loanQuoteEmploymentType = loanQuoteEmploymentType;
	}
	public Double getLoanQuotePropertyAdvanceDeposit() {
		return loanQuotePropertyAdvanceDeposit;
	}
	public void setLoanQuotePropertyAdvanceDeposit(
			Double loanQuotePropertyAdvanceDeposit) {
		this.loanQuotePropertyAdvanceDeposit = loanQuotePropertyAdvanceDeposit;
	}
	public Integer getLoanQuotePropertyCityId() {
		return loanQuotePropertyCityId;
	}
	public void setLoanQuotePropertyCityId(Integer loanQuotePropertyCityId) {
		this.loanQuotePropertyCityId = loanQuotePropertyCityId;
	}
	public Double getLoanQuotePropertyCost() {
		return loanQuotePropertyCost;
	}
	public void setLoanQuotePropertyCost(Double loanQuotePropertyCost) {
		this.loanQuotePropertyCost = loanQuotePropertyCost;
	}
	public Double getLoanQuotePropertyOtherLiabilities() {
		return loanQuotePropertyOtherLiabilities;
	}
	public void setLoanQuotePropertyOtherLiabilities(
			Double loanQuotePropertyOtherLiabilities) {
		this.loanQuotePropertyOtherLiabilities = loanQuotePropertyOtherLiabilities;
	}
	public Integer getLoanQuotePropertyStateId() {
		return loanQuotePropertyStateId;
	}
	public void setLoanQuotePropertyStateId(Integer loanQuotePropertyStateId) {
		this.loanQuotePropertyStateId = loanQuotePropertyStateId;
	}
	public Double getLoanQuotePropertyTaxPayable() {
		return loanQuotePropertyTaxPayable;
	}
	public void setLoanQuotePropertyTaxPayable(Double loanQuotePropertyTaxPayable) {
		this.loanQuotePropertyTaxPayable = loanQuotePropertyTaxPayable;
	}
	public Integer getLoanQuotePropertyType() {
		return loanQuotePropertyType;
	}
	public void setLoanQuotePropertyType(Integer loanQuotePropertyType) {
		this.loanQuotePropertyType = loanQuotePropertyType;
	}
	public Date getLoanQuoteStartDateOfCurrentProfession() {
		return loanQuoteStartDateOfCurrentProfession;
	}
	public void setLoanQuoteStartDateOfCurrentProfession(
			Date loanQuoteStartDateOfCurrentProfession) {
		this.loanQuoteStartDateOfCurrentProfession = loanQuoteStartDateOfCurrentProfession;
	}
	public Integer getLoanQuoteResidentCityId() {
		return loanQuoteResidentCityId;
	}
	public void setLoanQuoteResidentCityId(Integer loanQuoteResidentCityId) {
		this.loanQuoteResidentCityId = loanQuoteResidentCityId;
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
	public Integer getLoanQuoteResidentTypeId() {
		return loanQuoteResidentTypeId;
	}
	public void setLoanQuoteResidentTypeId(Integer loanQuoteResidentTypeId) {
		this.loanQuoteResidentTypeId = loanQuoteResidentTypeId;
	}
	public Integer getLoanQuoteSourceId() {
		return loanQuoteSourceId;
	}
	public void setLoanQuoteSourceId(Integer loanQuoteSourceId) {
		this.loanQuoteSourceId = loanQuoteSourceId;
	}
	public Date getLoanQuoteSTDOfLease() {
		return loanQuoteSTDOfLease;
	}
	public void setLoanQuoteSTDOfLease(Date loanQuoteSTDOfLease) {
		this.loanQuoteSTDOfLease = loanQuoteSTDOfLease;
	}
	public String getLoanQuoteStartDateOfLease() {
		return loanQuoteStartDateOfLease;
	}
	public void setLoanQuoteStartDateOfLease(String loanQuoteStartDateOfLease) {
		this.loanQuoteStartDateOfLease = loanQuoteStartDateOfLease;
	}
	public Date getLoanQuoteEDOfLease() {
		return loanQuoteEDOfLease;
	}
	public void setLoanQuoteEDOfLease(Date loanQuoteEDOfLease) {
		this.loanQuoteEDOfLease = loanQuoteEDOfLease;
	}
	public String getLoanQuoteEndDateOfLease() {
		return loanQuoteEndDateOfLease;
	}
	public void setLoanQuoteEndDateOfLease(String loanQuoteEndDateOfLease) {
		this.loanQuoteEndDateOfLease = loanQuoteEndDateOfLease;
	}
	public Date getLoanQuoteUpdatedTime() {
		return loanQuoteUpdatedTime;
	}
	public void setLoanQuoteUpdatedTime(Date loanQuoteUpdatedTime) {
		this.loanQuoteUpdatedTime = loanQuoteUpdatedTime;
	}
	public Integer getLoanQuoteVisitId() {
		return loanQuoteVisitId;
	}
	public void setLoanQuoteVisitId(Integer loanQuoteVisitId) {
		this.loanQuoteVisitId = loanQuoteVisitId;
	}
	public Integer getLoanQuoteWorkCityId() {
		return loanQuoteWorkCityId;
	}
	public void setLoanQuoteWorkCityId(Integer loanQuoteWorkCityId) {
		this.loanQuoteWorkCityId = loanQuoteWorkCityId;
	}
	public Integer getLoanQuoteWorkExperience() {
		return loanQuoteWorkExperience;
	}
	public void setLoanQuoteWorkExperience(Integer loanQuoteWorkExperience) {
		this.loanQuoteWorkExperience = loanQuoteWorkExperience;
	}
	public Integer getLoanQuoteWorkStateId() {
		return loanQuoteWorkStateId;
	}
	public void setLoanQuoteWorkStateId(Integer loanQuoteWorkStateId) {
		this.loanQuoteWorkStateId = loanQuoteWorkStateId;
	}
	public Double getLoanQuoteProfitAfterTax() {
		return loanQuoteProfitAfterTax;
	}
	public void setLoanQuoteProfitAfterTax(Double loanQuoteProfitAfterTax) {
		this.loanQuoteProfitAfterTax = loanQuoteProfitAfterTax;
	}
	public Integer getLoanQuoteIndustryType() {
		return loanQuoteIndustryType;
	}
	public void setLoanQuoteIndustryType(Integer loanQuoteIndustryType) {
		this.loanQuoteIndustryType = loanQuoteIndustryType;
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
	public Integer getLoanQuoteYearStartDateOflease() {
		return loanQuoteYearStartDateOflease;
	}
	public void setLoanQuoteYearStartDateOflease(
			Integer loanQuoteYearStartDateOflease) {
		this.loanQuoteYearStartDateOflease = loanQuoteYearStartDateOflease;
	}
	public Integer getLoanQuoteMonthStartDateOflease() {
		return loanQuoteMonthStartDateOflease;
	}
	public void setLoanQuoteMonthStartDateOflease(
			Integer loanQuoteMonthStartDateOflease) {
		this.loanQuoteMonthStartDateOflease = loanQuoteMonthStartDateOflease;
	}
	public Integer getLoanQuoteYearEndDateOfLease() {
		return loanQuoteYearEndDateOfLease;
	}
	public void setLoanQuoteYearEndDateOfLease(Integer loanQuoteYearEndDateOfLease) {
		this.loanQuoteYearEndDateOfLease = loanQuoteYearEndDateOfLease;
	}
	public Integer getLoanQuoteMonthEndDateOfLease() {
		return loanQuoteMonthEndDateOfLease;
	}
	public void setLoanQuoteMonthEndDateOfLease(Integer loanQuoteMonthEndDateOfLease) {
		this.loanQuoteMonthEndDateOfLease = loanQuoteMonthEndDateOfLease;
	}
	public Integer getLoanQuoteYearCompanyJoining() {
		return loanQuoteYearCompanyJoining;
	}
	public void setLoanQuoteYearCompanyJoining(Integer loanQuoteYearCompanyJoining) {
		this.loanQuoteYearCompanyJoining = loanQuoteYearCompanyJoining;
	}
	public Integer getLoanQuoteMonthCompanyJoining() {
		return loanQuoteMonthCompanyJoining;
	}
	public void setLoanQuoteMonthCompanyJoining(Integer loanQuoteMonthCompanyJoining) {
		this.loanQuoteMonthCompanyJoining = loanQuoteMonthCompanyJoining;
	}
	public Integer getLoanQuoteYearStartDateOfCurrentProfession() {
		return loanQuoteYearStartDateOfCurrentProfession;
	}
	public void setLoanQuoteYearStartDateOfCurrentProfession(
			Integer loanQuoteYearStartDateOfCurrentProfession) {
		this.loanQuoteYearStartDateOfCurrentProfession = loanQuoteYearStartDateOfCurrentProfession;
	}
	public Integer getLoanQuoteMonthStartDateOfCurrentProfession() {
		return loanQuoteMonthStartDateOfCurrentProfession;
	}
	public void setLoanQuoteMonthStartDateOfCurrentProfession(
			Integer loanQuoteMonthStartDateOfCurrentProfession) {
		this.loanQuoteMonthStartDateOfCurrentProfession = loanQuoteMonthStartDateOfCurrentProfession;
	}
	public Integer getLoanQuoteYearCoapplicantStartDateOfCurrentProfession() {
		return loanQuoteYearCoapplicantStartDateOfCurrentProfession;
	}
	public void setLoanQuoteYearCoapplicantStartDateOfCurrentProfession(
			Integer loanQuoteYearCoapplicantStartDateOfCurrentProfession) {
		this.loanQuoteYearCoapplicantStartDateOfCurrentProfession = loanQuoteYearCoapplicantStartDateOfCurrentProfession;
	}
	public Integer getLoanQuoteMonthCoapplicantStartDateOfCurrentProfession() {
		return loanQuoteMonthCoapplicantStartDateOfCurrentProfession;
	}
	public void setLoanQuoteMonthCoapplicantStartDateOfCurrentProfession(
			Integer loanQuoteMonthCoapplicantStartDateOfCurrentProfession) {
		this.loanQuoteMonthCoapplicantStartDateOfCurrentProfession = loanQuoteMonthCoapplicantStartDateOfCurrentProfession;
	}
	
	public Integer getLoanQuoteYearCoapplicantStartDateOfCurrentBusiness() {
		return loanQuoteYearCoapplicantStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteYearCoapplicantStartDateOfCurrentBusiness(
			Integer loanQuoteYearCoapplicantStartDateOfCurrentBusiness) {
		this.loanQuoteYearCoapplicantStartDateOfCurrentBusiness = loanQuoteYearCoapplicantStartDateOfCurrentBusiness;
	}
	public Integer getLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness() {
		return loanQuoteMonthCoapplicantStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteMonthCoapplicantStartDateOfCurrentBusiness(
			Integer loanQuoteMonthCoapplicantStartDateOfCurrentBusiness) {
		this.loanQuoteMonthCoapplicantStartDateOfCurrentBusiness = loanQuoteMonthCoapplicantStartDateOfCurrentBusiness;
	}
	public Integer getLoanQuoteYearCoapplicantJoining() {
		return loanQuoteYearCoapplicantJoining;
	}
	public void setLoanQuoteYearCoapplicantJoining(
			Integer loanQuoteYearCoapplicantJoining) {
		this.loanQuoteYearCoapplicantJoining = loanQuoteYearCoapplicantJoining;
	}
	public Integer getLoanQuoteMonthCoapplicantJoining() {
		return loanQuoteMonthCoapplicantJoining;
	}
	public void setLoanQuoteMonthCoapplicantJoining(
			Integer loanQuoteMonthCoapplicantJoining) {
		this.loanQuoteMonthCoapplicantJoining = loanQuoteMonthCoapplicantJoining;
	}
	public Integer getLoanQuoteCheckOffValue() {
		return loanQuoteCheckOffValue;
	}
	public void setLoanQuoteCheckOffValue(Integer loanQuoteCheckOffValue) {
		this.loanQuoteCheckOffValue = loanQuoteCheckOffValue;
	}
	public String getLoanQuoteProfessionId() {
		return loanQuoteProfessionId;
	}
	public void setLoanQuoteProfessionId(String loanQuoteProfessionId) {
		this.loanQuoteProfessionId = loanQuoteProfessionId;
	}
	
	public Integer getLoanQuoteCoapplicantProfessionId() {
		return loanQuoteCoapplicantProfessionId;
	}
	public void setLoanQuoteCoapplicantProfessionId(
			Integer loanQuoteCoapplicantProfessionId) {
		this.loanQuoteCoapplicantProfessionId = loanQuoteCoapplicantProfessionId;
	}
	public Integer getLoanQuoteCoapplicantRetirementAge() {
		return loanQuoteCoapplicantRetirementAge;
	}
	public void setLoanQuoteCoapplicantRetirementAge(
			Integer loanQuoteCoapplicantRetirementAge) {
		this.loanQuoteCoapplicantRetirementAge = loanQuoteCoapplicantRetirementAge;
	}
	public Integer getLoanQuoteSalaryPackage() {
		return loanQuoteSalaryPackage;
	}
	public void setLoanQuoteSalaryPackage(Integer loanQuoteSalaryPackage) {
		this.loanQuoteSalaryPackage = loanQuoteSalaryPackage;
	}
	public Double getLoanQuoteGrossMonthlySalary() {
		return loanQuoteGrossMonthlySalary;
	}
	public void setLoanQuoteGrossMonthlySalary(Double loanQuoteGrossMonthlySalary) {
		this.loanQuoteGrossMonthlySalary = loanQuoteGrossMonthlySalary;
	}
	public String getLoanQuoteInstituteName() {
		return loanQuoteInstituteName;
	}
	public void setLoanQuoteInstituteName(String loanQuoteInstituteName) {
		this.loanQuoteInstituteName = loanQuoteInstituteName;
	}
	public Integer getLoanQuoteWorkDistrictId() {
		return loanQuoteWorkDistrictId;
	}
	public void setLoanQuoteWorkDistrictId(Integer loanQuoteWorkDistrictId) {
		this.loanQuoteWorkDistrictId = loanQuoteWorkDistrictId;
	}
	public Integer getLoanQuoteWorkBranchId() {
		return loanQuoteWorkBranchId;
	}
	public void setLoanQuoteWorkBranchId(Integer loanQuoteWorkBranchId) {
		this.loanQuoteWorkBranchId = loanQuoteWorkBranchId;
	}
	public Integer getLoanQuotePropertyDistrictId() {
		return loanQuotePropertyDistrictId;
	}
	public void setLoanQuotePropertyDistrictId(Integer loanQuotePropertyDistrictId) {
		this.loanQuotePropertyDistrictId = loanQuotePropertyDistrictId;
	}
	public Integer getLoanQuotePropertyBranchId() {
		return loanQuotePropertyBranchId;
	}
	public void setLoanQuotePropertyBranchId(Integer loanQuotePropertyBranchId) {
		this.loanQuotePropertyBranchId = loanQuotePropertyBranchId;
	}
	public Integer getLoanQuoteNoOfOwner() {
		return loanQuoteNoOfOwner;
	}
	public void setLoanQuoteNoOfOwner(Integer loanQuoteNoOfOwner) {
		this.loanQuoteNoOfOwner = loanQuoteNoOfOwner;
	}
	public Double getLoanQuoteAnnualIncome() {
		return loanQuoteAnnualIncome;
	}
	public void setLoanQuoteAnnualIncome(Double loanQuoteAnnualIncome) {
		this.loanQuoteAnnualIncome = loanQuoteAnnualIncome;
	}
	public Date getLoanQuoteStartDateOfCurrentBusiness() {
		return loanQuoteStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteStartDateOfCurrentBusiness(
			Date loanQuoteStartDateOfCurrentBusiness) {
		this.loanQuoteStartDateOfCurrentBusiness = loanQuoteStartDateOfCurrentBusiness;
	}
	public Integer getLoanQuoteYearStartDateOfCurrentBusiness() {
		return loanQuoteYearStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteYearStartDateOfCurrentBusiness(
			Integer loanQuoteYearStartDateOfCurrentBusiness) {
		this.loanQuoteYearStartDateOfCurrentBusiness = loanQuoteYearStartDateOfCurrentBusiness;
	}
	public Integer getLoanQuoteMonthStartDateOfCurrentBusiness() {
		return loanQuoteMonthStartDateOfCurrentBusiness;
	}
	public void setLoanQuoteMonthStartDateOfCurrentBusiness(
			Integer loanQuoteMonthStartDateOfCurrentBusiness) {
		this.loanQuoteMonthStartDateOfCurrentBusiness = loanQuoteMonthStartDateOfCurrentBusiness;
	}
	public Integer getLoanQuoteEmployerCat() {
		return loanQuoteEmployerCat;
	}
	public void setLoanQuoteEmployerCat(Integer loanQuoteEmployerCat) {
		this.loanQuoteEmployerCat = loanQuoteEmployerCat;
	}
	public String getLoanQuoteInstituteCat() {
		return loanQuoteInstituteCat;
	}
	public void setLoanQuoteInstituteCat(String loanQuoteInstituteCat) {
		this.loanQuoteInstituteCat = loanQuoteInstituteCat;
	}
	public Integer getLoanQuoteEmployerNameId() {
		return loanQuoteEmployerNameId;
	}
	public void setLoanQuoteEmployerNameId(Integer loanQuoteEmployerNameId) {
		this.loanQuoteEmployerNameId = loanQuoteEmployerNameId;
	}
	public Integer getLoanQuoteInstituteNameId() {
		return loanQuoteInstituteNameId;
	}
	public void setLoanQuoteInstituteNameId(Integer loanQuoteInstituteNameId) {
		this.loanQuoteInstituteNameId = loanQuoteInstituteNameId;
	}
	public String getLoanQuotePropertyOwnedBySpouse() {
		return loanQuotePropertyOwnedBySpouse;
	}
	public void setLoanQuotePropertyOwnedBySpouse(
			String loanQuotePropertyOwnedBySpouse) {
		this.loanQuotePropertyOwnedBySpouse = loanQuotePropertyOwnedBySpouse;
	}
	public Integer getLoanQuoteCheckOffType() {
		return loanQuoteCheckOffType;
	}
	public void setLoanQuoteCheckOffType(Integer loanQuoteCheckOffType) {
		this.loanQuoteCheckOffType = loanQuoteCheckOffType;
	}
	public Integer getLoanQuoteCoapplicantResidentTypeId() {
		return loanQuoteCoapplicantResidentTypeId;
	}
	public void setLoanQuoteCoapplicantResidentTypeId(
			Integer loanQuoteCoapplicantResidentTypeId) {
		this.loanQuoteCoapplicantResidentTypeId = loanQuoteCoapplicantResidentTypeId;
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
	public Date getLoanQuoteCoapplicantDateOfBirthDT() {
		return loanQuoteCoapplicantDateOfBirthDT;
	}
	public void setLoanQuoteCoapplicantDateOfBirthDT(
			Date loanQuoteCoapplicantDateOfBirthDT) {
		this.loanQuoteCoapplicantDateOfBirthDT = loanQuoteCoapplicantDateOfBirthDT;
	}
	public String getLoanQuoteCoapplicantDateOfBirth() {
		return loanQuoteCoapplicantDateOfBirth;
	}
	public void setLoanQuoteCoapplicantDateOfBirth(
			String loanQuoteCoapplicantDateOfBirth) {
		this.loanQuoteCoapplicantDateOfBirth = loanQuoteCoapplicantDateOfBirth;
	}
	public Integer getLoanQuoteNewVisitId() {
		return loanQuoteNewVisitId;
	}
	public void setLoanQuoteNewVisitId(Integer loanQuoteNewVisitId) {
		this.loanQuoteNewVisitId = loanQuoteNewVisitId;
	}
	public String getLoanQuoteAvailPersonalLoanWithBankEarlier() {
		return loanQuoteAvailPersonalLoanWithBankEarlier;
	}
	public void setLoanQuoteAvailPersonalLoanWithBankEarlier(
			String loanQuoteAvailPersonalLoanWithBankEarlier) {
		this.loanQuoteAvailPersonalLoanWithBankEarlier = loanQuoteAvailPersonalLoanWithBankEarlier;
	}
	public String getLoanQuoteCoapplicantInstituteName() {
		return loanQuoteCoapplicantInstituteName;
	}
	public void setLoanQuoteCoapplicantInstituteName(
			String loanQuoteCoapplicantInstituteName) {
		this.loanQuoteCoapplicantInstituteName = loanQuoteCoapplicantInstituteName;
	}
	public Integer getLoanQuoteBranchIsCaptive() {
		return loanQuoteBranchIsCaptive;
	}
	public void setLoanQuoteBranchIsCaptive(Integer loanQuoteBranchIsCaptive) {
		this.loanQuoteBranchIsCaptive = loanQuoteBranchIsCaptive;
	}
	public Integer getLoanQuoteTermODType() {
		return loanQuoteTermODType;
	}
	public void setLoanQuoteTermODType(Integer loanQuoteTermODType) {
		this.loanQuoteTermODType = loanQuoteTermODType;
	}
	public Double getLoanQuotePreEMIsOther() {
		return loanQuotePreEMIsOther;
	}
	public void setLoanQuotePreEMIsOther(Double loanQuotePreEMIsOther) {
		this.loanQuotePreEMIsOther = loanQuotePreEMIsOther;
	}
	public Integer getLoanQuoteEmploymentNature() {
		return loanQuoteEmploymentNature;
	}
	public void setLoanQuoteEmploymentNature(Integer loanQuoteEmploymentNature) {
		this.loanQuoteEmploymentNature = loanQuoteEmploymentNature;
	}
	public Double getLoanQuoteExistingSanctionAmount() {
		return loanQuoteExistingSanctionAmount;
	}
	public void setLoanQuoteExistingSanctionAmount(Double loanQuoteExistingSanctionAmount) {
		this.loanQuoteExistingSanctionAmount = loanQuoteExistingSanctionAmount;
	}
	
	public Date getLoanQuoteExistingPLSanctionDT() {
		return loanQuoteExistingPLSanctionDT;
	}
	public void setLoanQuoteExistingPLSanctionDT(Date loanQuoteExistingPLSanctionDT) {
		this.loanQuoteExistingPLSanctionDT = loanQuoteExistingPLSanctionDT;
	}
	public String getLoanQuoteExistingPLSanctionDate() {
		return loanQuoteExistingPLSanctionDate;
	}
	public void setLoanQuoteExistingPLSanctionDate(
			String loanQuoteExistingPLSanctionDate) {
		this.loanQuoteExistingPLSanctionDate = loanQuoteExistingPLSanctionDate;
	}
	public Integer getLoanQuoteExistingXpressCreditNo() {
		return loanQuoteExistingXpressCreditNo;
	}
	public void setLoanQuoteExistingXpressCreditNo(Integer loanQuoteExistingXpressCreditNo) {
		this.loanQuoteExistingXpressCreditNo = loanQuoteExistingXpressCreditNo;
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
	public Integer getLoanQuoteCityId() {
		return loanQuoteCityId;
	}
	public void setLoanQuoteCityId(Integer loanQuoteCityId) {
		this.loanQuoteCityId = loanQuoteCityId;
	}
	public Integer getLoanQuotePincode() {
		return loanQuotePincode;
	}
	public void setLoanQuotePincode(Integer loanQuotePincode) {
		this.loanQuotePincode = loanQuotePincode;
	}
	public String getLoanQuotePanCardNo() {
		return loanQuotePanCardNo;
	}
	public void setLoanQuotePanCardNo(String loanQuotePanCardNo) {
		this.loanQuotePanCardNo = loanQuotePanCardNo;
	}
	public Boolean getLoanQuotePanCardLater() {
		return loanQuotePanCardLater;
	}
	public void setLoanQuotePanCardLater(Boolean loanQuotePanCardLater) {
		this.loanQuotePanCardLater = loanQuotePanCardLater;
	}
	public Integer getLoanQuoteOtherId() {
		return loanQuoteOtherId;
	}
	public void setLoanQuoteOtherId(Integer loanQuoteOtherId) {
		this.loanQuoteOtherId = loanQuoteOtherId;
	}
	public String getLoanQuoteOtherIdNumber() {
		return loanQuoteOtherIdNumber;
	}
	public void setLoanQuoteOtherIdNumber(String loanQuoteOtherIdNumber) {
		this.loanQuoteOtherIdNumber = loanQuoteOtherIdNumber;
	}
	
	public Integer getLoanQuoteCibilScore() {
		return loanQuoteCibilScore;
	}
	public void setLoanQuoteCibilScore(Integer loanQuoteCibilScore) {
		this.loanQuoteCibilScore = loanQuoteCibilScore;
	}
	public Integer getLoanQuoteDistrictId() {
		return loanQuoteDistrictId;
	}
	public void setLoanQuoteDistrictId(Integer loanQuoteDistrictId) {
		this.loanQuoteDistrictId = loanQuoteDistrictId;
	}

	public Double getLoanQuoteExpMonthlyRentalIncome() {
		return loanQuoteExpMonthlyRentalIncome;
	}
	public void setLoanQuoteExpMonthlyRentalIncome(Double loanQuoteExpMonthlyRentalIncome) {
		this.loanQuoteExpMonthlyRentalIncome = loanQuoteExpMonthlyRentalIncome;
	}
	public Double getLoanQuoteOtherMonthlyIncome() {
		return loanQuoteOtherMonthlyIncome;
	}
	public void setLoanQuoteOtherMonthlyIncome(Double loanQuoteOtherMonthlyIncome) {
		this.loanQuoteOtherMonthlyIncome = loanQuoteOtherMonthlyIncome;
	}
	public Double getLoanQuoteCoapplicantExpMonthlyRentalIncome() {
		return loanQuoteCoapplicantExpMonthlyRentalIncome;
	}
	public void setLoanQuoteCoapplicantExpMonthlyRentalIncome(Double loanQuoteCoapplicantExpMonthlyRentalIncome) {
		this.loanQuoteCoapplicantExpMonthlyRentalIncome = loanQuoteCoapplicantExpMonthlyRentalIncome;
	}
	public Double getLoanQuoteCoapplicantOtherMonthlyIncome() {
		return loanQuoteCoapplicantOtherMonthlyIncome;
	}
	public void setLoanQuoteCoapplicantOtherMonthlyIncome(Double loanQuoteCoapplicantOtherMonthlyIncome) {
		this.loanQuoteCoapplicantOtherMonthlyIncome = loanQuoteCoapplicantOtherMonthlyIncome;
	}
	public Double getLoanQuoteCoapplicantGrossMonthlySalary() {
		return loanQuoteCoapplicantGrossMonthlySalary;
	}
	public void setLoanQuoteCoapplicantGrossMonthlySalary(Double loanQuoteCoapplicantGrossMonthlySalary) {
		this.loanQuoteCoapplicantGrossMonthlySalary = loanQuoteCoapplicantGrossMonthlySalary;
	}
	public float getLoanQuoteExistingPLRepaymentYear() {
		return loanQuoteExistingPLRepaymentYear;
	}
	public void setLoanQuoteExistingPLRepaymentYear(float loanQuoteExistingPLRepaymentYear) {
		this.loanQuoteExistingPLRepaymentYear = loanQuoteExistingPLRepaymentYear;
	}
	public Integer getLoanQuoteBranchLocationId() {
		return loanQuoteBranchLocationId;
	}
	public void setLoanQuoteBranchLocationId(Integer loanQuoteBranchLocationId) {
		this.loanQuoteBranchLocationId = loanQuoteBranchLocationId;
	}
	public Integer getLoanQuotePensionPayingBranchId() {
		return loanQuotePensionPayingBranchId;
	}
	public void setLoanQuotePensionPayingBranchId(
			Integer loanQuotePensionPayingBranchId) {
		this.loanQuotePensionPayingBranchId = loanQuotePensionPayingBranchId;
	}
	public Integer getLoanQuotePensionPayingStateId() {
		return loanQuotePensionPayingStateId;
	}
	public void setLoanQuotePensionPayingStateId(
			Integer loanQuotePensionPayingStateId) {
		this.loanQuotePensionPayingStateId = loanQuotePensionPayingStateId;
	}
	public Integer getLoanQuotePensionPayingCityId() {
		return loanQuotePensionPayingCityId;
	}
	public void setLoanQuotePensionPayingCityId(Integer loanQuotePensionPayingCityId) {
		this.loanQuotePensionPayingCityId = loanQuotePensionPayingCityId;
	}
	public Integer getLoanQuotePensionPayingDistrId() {
		return loanQuotePensionPayingDistrId;
	}
	public void setLoanQuotePensionPayingDistrId(
			Integer loanQuotePensionPayingDistrId) {
		this.loanQuotePensionPayingDistrId = loanQuotePensionPayingDistrId;
	}
	public Integer getLoanQuotePreferredLoanBranch() {
		return loanQuotePreferredLoanBranch;
	}
	public void setLoanQuotePreferredLoanBranch(Integer loanQuotePreferredLoanBranch) {
		this.loanQuotePreferredLoanBranch = loanQuotePreferredLoanBranch;
	}
	public Integer getLoanQuotePreferredStateId() {
		return loanQuotePreferredStateId;
	}
	public void setLoanQuotePreferredStateId(Integer loanQuotePreferredStateId) {
		this.loanQuotePreferredStateId = loanQuotePreferredStateId;
	}
	public Integer getLoanQuotePensionType() {
		return loanQuotePensionType;
	}
	public void setLoanQuotePensionType(Integer loanQuotePensionType) {
		this.loanQuotePensionType = loanQuotePensionType;
	}
	public Integer getLoanQuotePreferredCityId() {
		return loanQuotePreferredCityId;
	}
	public void setLoanQuotePreferredCityId(Integer loanQuotePreferredCityId) {
		this.loanQuotePreferredCityId = loanQuotePreferredCityId;
	}
	public Integer getLoanQuotePreferredDistrictId() {
		return loanQuotePreferredDistrictId;
	}
	public void setLoanQuotePreferredDistrictId(Integer loanQuotePreferredDistrictId) {
		this.loanQuotePreferredDistrictId = loanQuotePreferredDistrictId;
	}
	public String getLoanQuotePensionPayOrdNumber() {
		return loanQuotePensionPayOrdNumber;
	}
	public void setLoanQuotePensionPayOrdNumber(String loanQuotePensionPayOrdNumber) {
		this.loanQuotePensionPayOrdNumber = loanQuotePensionPayOrdNumber;
	}
	public Integer getLoanQuoteMonthPensionAmt() {
		return loanQuoteMonthPensionAmt;
	}
	public void setLoanQuoteMonthPensionAmt(Integer loanQuoteMonthPensionAmt) {
		this.loanQuoteMonthPensionAmt = loanQuoteMonthPensionAmt;
	}	 
	public Integer getLoanQuotePreferredLoanBranchId() {
		return loanQuotePreferredLoanBranchId;
	}
	public void setLoanQuotePreferredLoanBranchId(
			Integer loanQuotePreferredLoanBranchId) {
		this.loanQuotePreferredLoanBranchId = loanQuotePreferredLoanBranchId;
	}
	public Integer getLoanQuoteModeOfRepayment() {
		return loanQuoteModeOfRepayment;
	}
	public void setLoanQuoteModeOfRepayment(Integer loanQuoteModeOfRepayment) {
		this.loanQuoteModeOfRepayment = loanQuoteModeOfRepayment;
	}
	public String getLoanQuotePensionAccountNumber() {
		return loanQuotePensionAccountNumber;
	}
	public void setLoanQuotePensionAccountNumber(
			String loanQuotePensionAccountNumber) {
		this.loanQuotePensionAccountNumber = loanQuotePensionAccountNumber;
	}
	public Integer getLoanQuoteOrgName() {
		return loanQuoteOrgName;
	}
	public void setLoanQuoteOrgName(Integer loanQuoteOrgName) {
		this.loanQuoteOrgName = loanQuoteOrgName;
	}
	public Double getLoanQuoteCoapplicantIncomeFromOtherSource() {
		return loanQuoteCoapplicantIncomeFromOtherSource;
	}
	public void setLoanQuoteCoapplicantIncomeFromOtherSource(Double loanQuoteCoapplicantIncomeFromOtherSource) {
		this.loanQuoteCoapplicantIncomeFromOtherSource = loanQuoteCoapplicantIncomeFromOtherSource;
	}
	public Double getLoanQuoteCoapplicantNetMonthlyPension() {
		return loanQuoteCoapplicantNetMonthlyPension;
	}
	public void setLoanQuoteCoapplicantNetMonthlyPension(Double loanQuoteCoapplicantNetMonthlyPension) {
		this.loanQuoteCoapplicantNetMonthlyPension = loanQuoteCoapplicantNetMonthlyPension;
	}

	public Double getLoanQuoteOutstandingLoanAmount() {
		return loanQuoteOutstandingLoanAmount;
	}
	public void setLoanQuoteOutstandingLoanAmount(Double loanQuoteOutstandingLoanAmount) {
		this.loanQuoteOutstandingLoanAmount = loanQuoteOutstandingLoanAmount;
	}

	public String getRequestStr() {
		return requestStr;
	}
	public void setRequestStr(String requestStr) {
		this.requestStr = requestStr;
	}
	public String getResponseStr() {
		return responseStr;
	}
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}

	public String getLoanQuoteOrganisationName() {
		return loanQuoteOrganisationName;
	}
	public void setLoanQuoteOrganisationName(String loanQuoteOrganisationName) {
		this.loanQuoteOrganisationName = loanQuoteOrganisationName;
	}
	public Double getLoanQuoteLastMonthSalaryCredited() {
		return loanQuoteLastMonthSalaryCredited;
	}
	public void setLoanQuoteLastMonthSalaryCredited(Double loanQuoteLastMonthSalaryCredited) {
		this.loanQuoteLastMonthSalaryCredited = loanQuoteLastMonthSalaryCredited;
	}
	public Double getLoanQuoteAverageOfLastSalary() {
		return loanQuoteAverageOfLastSalary;
	}
	public void setLoanQuoteAverageOfLastSalary(Double loanQuoteAverageOfLastSalary) {
		this.loanQuoteAverageOfLastSalary = loanQuoteAverageOfLastSalary;
	}
	public Integer getLoanQuoteCurrentContractStartYear() {
		return loanQuoteCurrentContractStartYear;
	}
	public void setLoanQuoteCurrentContractStartYear(Integer loanQuoteCurrentContractStartYear) {
		this.loanQuoteCurrentContractStartYear = loanQuoteCurrentContractStartYear;
	}
	public Integer getLoanQuoteCurrentContractStartMonth() {
		return loanQuoteCurrentContractStartMonth;
	}
	public void setLoanQuoteCurrentContractStartMonth(Integer loanQuoteCurrentContractStartMonth) {
		this.loanQuoteCurrentContractStartMonth = loanQuoteCurrentContractStartMonth;
	}
	public Integer getLoanQuoteCurrentContractEndYear() {
		return loanQuoteCurrentContractEndYear;
	}
	public void setLoanQuoteCurrentContractEndYear(Integer loanQuoteCurrentContractEndYear) {
		this.loanQuoteCurrentContractEndYear = loanQuoteCurrentContractEndYear;
	}
	public Integer getLoanQuoteCurrentContractEndMonth() {
		return loanQuoteCurrentContractEndMonth;
	}
	public void setLoanQuoteCurrentContractEndMonth(Integer loanQuoteCurrentContractEndMonth) {
		this.loanQuoteCurrentContractEndMonth = loanQuoteCurrentContractEndMonth;
	}
	public Integer getSalaryPackageRank() {
		return salaryPackageRank;
	}
	public void setSalaryPackageRank(Integer salaryPackageRank) {
		this.salaryPackageRank = salaryPackageRank;
	}
	public String getLoanQuoteCoapplicantName() {
		return loanQuoteCoapplicantName;
	}
	public void setLoanQuoteCoapplicantName(String loanQuoteCoapplicantName) {
		this.loanQuoteCoapplicantName = loanQuoteCoapplicantName;
	}

	public Integer getLoanQuoteEmplymentType() {
		return loanQuoteEmplymentType;
	}
	public void setLoanQuoteEmplymentType(Integer loanQuoteEmplymentType) {
		this.loanQuoteEmplymentType = loanQuoteEmplymentType;
	}
	public Integer getLoanQuoteSalaryAccountSince() {
		return loanQuoteSalaryAccountSince;
	}
	public void setLoanQuoteSalaryAccountSince(Integer loanQuoteSalaryAccountSince) {
		this.loanQuoteSalaryAccountSince = loanQuoteSalaryAccountSince;
	}
	public Integer getLoanQuoteModeOfSalaryCredit() {
		return loanQuoteModeOfSalaryCredit;
	}
	public void setLoanQuoteModeOfSalaryCredit(Integer loanQuoteModeOfSalaryCredit) {
		this.loanQuoteModeOfSalaryCredit = loanQuoteModeOfSalaryCredit;
	}
	public Double getLoanQuoteLoanAmountTaken() {
		return loanQuoteLoanAmountTaken;
	}
	public void setLoanQuoteLoanAmountTaken(Double loanQuoteLoanAmountTaken) {
		this.loanQuoteLoanAmountTaken = loanQuoteLoanAmountTaken;
	}
	public Integer getLoanQuoteOccupationTypeId() {
		return loanQuoteOccupationTypeId;
	}
	public void setLoanQuoteOccupationTypeId(Integer loanQuoteOccupationTypeId) {
		this.loanQuoteOccupationTypeId = loanQuoteOccupationTypeId;
	}
	public Integer getLoanQuoteResidenceTypeId() {
		return loanQuoteResidenceTypeId;
	}
	public void setLoanQuoteResidenceTypeId(Integer loanQuoteResidenceTypeId) {
		this.loanQuoteResidenceTypeId = loanQuoteResidenceTypeId;
	}
	public Integer getLoanQuoteRelationshipWithBank() {
		return loanQuoteRelationshipWithBank;
	}
	public void setLoanQuoteRelationshipWithBank(Integer loanQuoteRelationshipWithBank) {
		this.loanQuoteRelationshipWithBank = loanQuoteRelationshipWithBank;
	}
	public Double getLoanQuoteDepreciation() {
		return loanQuoteDepreciation;
	}
	public void setLoanQuoteDepreciation(Double loanQuoteDepreciation) {
		this.loanQuoteDepreciation = loanQuoteDepreciation;
	}
	public Double getLoanQuoteNetMonthlyPension() {
		return loanQuoteNetMonthlyPension;
	}
	public void setLoanQuoteNetMonthlyPension(Double loanQuoteNetMonthlyPension) {
		this.loanQuoteNetMonthlyPension = loanQuoteNetMonthlyPension;
	}
	public Double getLoanQuoteIncomeFromRegularSource() {
		return loanQuoteIncomeFromRegularSource;
	}
	public void setLoanQuoteIncomeFromRegularSource(Double loanQuoteIncomeFromRegularSource) {
		this.loanQuoteIncomeFromRegularSource = loanQuoteIncomeFromRegularSource;
	}
	public Integer getLoanQuoteHaveAadhaarNumber() {
		return loanQuoteHaveAadhaarNumber;
	}
	public void setLoanQuoteHaveAadhaarNumber(Integer loanQuoteHaveAadhaarNumber) {
		this.loanQuoteHaveAadhaarNumber = loanQuoteHaveAadhaarNumber;
	}
	public Double getLoanQuoteIncomeFromRegaularSource() {
		return loanQuoteIncomeFromRegaularSource;
	}
	public void setLoanQuoteIncomeFromRegaularSource(Double loanQuoteIncomeFromRegaularSource) {
		this.loanQuoteIncomeFromRegaularSource = loanQuoteIncomeFromRegaularSource;
	}
	public Double getLoanQuoteCoappVariableMonthPay() {
		return loanQuoteCoappVariableMonthPay;
	}
	public void setLoanQuoteCoappVariableMonthPay(Double loanQuoteCoappVariableMonthPay) {
		this.loanQuoteCoappVariableMonthPay = loanQuoteCoappVariableMonthPay;
	}
	public Double getLoanQuoteVariableMonthlyPay() {
		return loanQuoteVariableMonthlyPay;
	}
	public void setLoanQuoteVariableMonthlyPay(Double loanQuoteVariableMonthlyPay) {
		this.loanQuoteVariableMonthlyPay = loanQuoteVariableMonthlyPay;
	}
	public Integer getLoanQuoteHaveCoOperativeBankAcc() {
		return loanQuoteHaveCoOperativeBankAcc;
	}
	public void setLoanQuoteHaveCoOperativeBankAcc(Integer loanQuoteHaveCoOperativeBankAcc) {
		this.loanQuoteHaveCoOperativeBankAcc = loanQuoteHaveCoOperativeBankAcc;
	}
	public Integer getLoanQuoteIndustryTypeCat() {
		return loanQuoteIndustryTypeCat;
	}
	public void setLoanQuoteIndustryTypeCat(Integer loanQuoteIndustryTypeCat) {
		this.loanQuoteIndustryTypeCat = loanQuoteIndustryTypeCat;
	}
	public Integer getCbsRelationShipId() {
		return cbsRelationShipId;
	}
	public void setCbsRelationShipId(Integer cbsRelationShipId) {
		this.cbsRelationShipId = cbsRelationShipId;
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
