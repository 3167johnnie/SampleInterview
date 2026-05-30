package com.mintstreet.loan.autoloan.entity;

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
@Table(name="RUPEEPOWER_OCAS_T_00099")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="G5", sequenceName="RUPEEPOWER_OCAS_SEQ_00015" ,allocationSize=1)
public class ApplicationFormAutoLoanQuote extends Domain<Integer> implements Serializable {
	private static final long serialVersionUID = -9103487389950154666L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G5")
	@Column(name = "QUOTE_SEQUENCE_ID")
	private Integer loanQuoteId;

  @Transient
  private String loanQuoteAppAlternateMobileNo;
	@Column(name = "QUOTE_LOAN_PURPOSE_ID")
	private Integer loanQuoteLoanPurposeId;

	@Column(name = "QUOTE_LOAN_CATEGORY_ID")
	private Integer loanQuoteLoanCategoryId;

	@Temporal(TemporalType.DATE)
	@Column(name = "QUOTE_Date_of_purchase")
	private Date loanQuoteDopDT;
	
	@Transient
	private String loanQuoteDop;

	@Transient
	private Integer monthloanQuoteDop;
	
	@Transient
	private Integer yearloanQuoteDop;
	
	@Column(name = "QUOTE_LOAN_WITH_BANK_ID")
	private Integer loanQuoteLoanWithBankId;

	@Temporal(TemporalType.DATE)
	@Column(name = "QUOTE_ST_DATE_OF_CURR_LOAN")
	private Date loanQuoteStartDateOfCurrentLoanDT;

	@Transient
	private String loanQuoteStartDateOfCurrentLoan;
	
	@Transient
	private Integer monthloanQuoteStartDateOfCurrentLoan;
	
	@Transient
	private Integer yearloanQuoteStartDateOfCurrentLoan;
	
	@Column(name = "QUOTE_OUTSTANDING_LOAN_AMOUNT")
	private Double loanQuoteOutstandingLoanAmount;

	@Column(name = "QUOTE_IS_INSU_CLAIM_AVAILED")
	private String loanQuoteIsInsuranceClaimAvailed;
	
	@Column(name = "QUOTE_IS_PRE_OWNED_USED_CAR")
	private String loanQuoteIsPreOwnedUsedCar;
	
	@Column(name = "QUOTE_IS_DEALER_OR_INDIVIDUAL")
	private String loanQuoteIsDealerOrIndividual;

	@Column(name = "QUOTE_EXISTING_DEALER_NAME")
	private String loanQuoteExistingDealerName;

	@Column(name = "QUOTE_TWO_WHEELER_TYPE_ID")
	private Integer loanQuoteTwoWheelerTypeId;

	@Column(name = "QUOTE_BIKE_MAKE_ID")
	private Integer loanQuoteBikeMakeId;

	@Column(name = "QUOTE_BIKE_MODEL_ID")
	private Integer loanQuoteBikeModelId;

	@Column(name = "QUOTE_BIKE_VARIANT_ID")
	private Integer loanQuoteBikeVariantId;

	@Column(name = "QUOTE_EXSHOWROOM_PRICE_BIKE")
	private Double loanQuoteExshowroomPriceBike;

	@Column(name = "QUOTE_ROAD_TAX_BIKE")
	private Double loanQuoteRoadTaxBike;
	
	@Column(name = "QUOTE_INSURANCE_CHARGE_BIKE")
	private Double loanQuoteInsuranceChargeBike;

	@Column(name = "QUOTE_REG_CHARGE_BIKE")
	private Double loanQuoteRegistrationChargeBike;

	@Column(name = "QUOTE_ACC_CHARGE_BIKE")
	private Double loanQuoteAccessoriesChargeBike;	

	@Column(name = "QUOTE_OTHER_MISC_CHARGE_BIKE")
	private Double loanQuoteOtherMiscChargeBike;

	
	@Column(name = "QUOTE_CAR_MAKE_ID")
	private Integer loanQuoteCarMakeId;

	@Column(name = "QUOTE_CAR_MODEL_ID")
	private Integer loanQuoteCarModelId;

	@Column(name = "QUOTE_CAR_VARIANT_ID")
	private Integer loanQuoteCarVariantId;
	
	@Column(name= "QUOTE_CAR_TYPE_IS_SUV_MPV_MUV")
	private String loanQuoteCarTypeId;
	

	
	@Column(name= "QUOTE_OCCUPATION_TYPE_ID")
	private Integer loanQuoteOccupationTypeId;


	@Column(name= "QUOTE_OCCUPATION_CATEGORY")
	private Integer loanQuoteOccupationCategory;




	@Column(name = "QUOTE_EXSHOWROOM_PRICE_CAR")
	private Double loanQuoteExshowroomPriceCar;

	@Column(name = "QUOTE_ROAD_TAX_CAR")
	private Double loanQuoteRoadTaxCar;

	@Column(name = "QUOTE_INSURANCE_CHARGE_CAR")
	private Double loanQuoteInsuranceChargeCar;

	@Column(name = "QUOTE_REG_CHARGE_CAR")
	private Double loanQuoteRegistrationChargeCar;

	@Column(name = "QUOTE_ACC_CHARGE_CAR")
	private Double loanQuoteAccessoriesChargeCar;

	@Column(name = "QUOTE_OTH_MISC_CHARGE_CAR")
	private Double loanQuoteOtherMiscChargeCar;

	
	
	@Column(name = "QUOTE_DISCOUNT_CAR")
	private Double loanQuoteDiscountCar;
	
	@Column(name = "QUOTE_DISCOUNT_BIKE")
	private Double loanQuoteDiscountBike;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "QUOTE_DATE_OF_BIRTH")
	private Date loanQuoteDateOfBirthDT;
	
	@Transient
	private String loanQuoteDateOfBirth;

	@Transient
	private Integer loanQuoteAge;
	
	@Column(name = "QUOTE_GENDER")
	private String loanQuoteGender;

	@Column(name = "QUOTE_RESIDENT_TYPE_ID")
	private Integer loanQuoteResidentTypeId;

	@Column(name = "QUOTE_COUNTRY_ID")
	private Integer loanQuoteCountryId;

	@Column(name = "QUOTE_STATE_ID")
	private Integer loanQuoteStateId;

	@Column(name = "QUOTE_CITY_ID")
	private Integer loanQuoteCityId;

	@Column(name = "QUOTE_LOCALITY_ID")
	private Integer loanQuoteLocalityId;

	@Column(name = "QUOTE_DISTRICT_ID")
	private Integer loanQuoteDistrictId;

	@Column(name = "QUOTE_BRANCH_ID")
	private Integer loanQuoteBranchId;
	
	@Column(name = "QUOTE_WORK_EXPERIENCE")
	private Integer loanQuoteWorkExperience;

	

	@Column(name = "QUOTE_EMPLOYMENT_TYPE_ID")
	private Integer loanQuoteEmploymentTypeId;

	@Column(name = "QUOTE_HAVE_SAL_ACC_WITH_SBI")
	private String loanQuoteHaveSalaryAccountWithSbi;
	
	@Column(name="QUOTE_SALARY_PACKAGE")
	private Integer loanQuoteSalaryPackage;
	
	@Column(name = "QUOTE_EMPLOYER_NAME")
	private String loanQuoteEmployerName;

	@Column(name = "QUOTE_EMPLOYER_COMPANY_ID")
	private Integer loanQuoteEmployerCompanyId;

	@Column(name = "QUOTE_GROSS_MONTHLY_INCOME")
	private Double loanQuoteGrossMonthlyIncome;
	
	@Column(name = "QUOTE_NET_MONTHLY_SALARY")
	private Double loanQuoteNetMonthlySalary;
	
	@Column(name = "QUOTE_VARIABLE_MONTH_PAY")
	private Double loanQuoteVariableMonthPay;

	@Column(name = "QUOTE_EXPECTED_RENTAL_INCOME")
	private Double loanQuoteExpectedRentalIncome;

	@Column(name = "QUOTE_OTHER_INCOME")
	private Double loanQuoteOtherIncome;
	
	@Column(name = "QUOTE_NET_MONTHLY_PEN")
	private Double loanQuoteNetMonthlyPension;

	@Column(name ="QUOTE_INDUSTRY_TYPE")
	private Integer loanQuoteIndustryType;

	@Column(name = "QUOTE_PREEMIS")
	private Double loanQuotePreEMIs;

	@Column(name = "QUOTE_RETIREMENT_AGE_APP")
	private Integer loanQuoteRetirementAgeApplicant;

	@Column(name = "QUOTE_PROFIT_AFTER_TAX")
	private Double loanQuoteProfitAfterTax;

	@Column(name = "QUOTE_DEPRECIATION")
	private Double loanQuoteDepreciation;

	@Column(name = "QUOTE_NET_ANNUAL_INCOME")
	private Double loanQuoteNetAnnualIncome;

	@Column(name = "QUOTE_INC_FROM_REGULAR_SOURCE")
	private Double loanQuoteIncomeFromRegularSource;

	@Column(name = "QUOTE_PENSION_ACC_WITH_SBI")
	private String loanQuotePensionAccountWithSbi;

	@Column(name = "QUOTE_COAPP_FST_ID")
	private Integer loanQuoteCoapplicantFirstId;

	@Column(name = "QUOTE_COAPP_FST_REL_ID")
	private Integer loanQuoteCoapplicantFirstRelationshipId;

	@Temporal(TemporalType.DATE)
	@Column(name = "QUOTE_COAPP_FST_DATE_OF_BIRTH")
	private Date loanQuoteCoapplicantFirstDateOfBirthDT;
	
	@Transient
	private String loanQuoteCoapplicantFirstDateOfBirth;
	
	@Transient
	private Integer loanQuoteCoapplicantFirstAge;
	
	@Column(name = "QUOTE_COAPP_FST_RES_TYPE_ID")
	private Integer loanQuoteCoapplicantFirstResidentTypeId;

	@Column(name = "QUOTE_COAPP_FST_COUNTRY_ID")
	private Integer loanQuoteCoapplicantFirstCountryId;

	@Column(name = "QUOTE_COAPP_FST_WORK_EXP")
	private Integer loanQuoteCoapplicantFirstWorkExperience;

	@Column(name = "QUOTE_COAPP_FST_CITY_ID")
	private Integer loanQuoteCoapplicantFirstCityId;

	@Column(name = "QUOTE_COAPP_FST_EMP_TYPE_ID")
	private Integer loanQuoteCoapplicantFirstEmploymentTypeId;

	@Column(name = "QUOTE_COAPP_FST_SA_WITH_SBI")
	private String loanQuoteCoapplicantFirstCoSalaryAccountWithSbi;

	@Column(name = "QUOTE_COAPP_FST_EMPLOYER_NAME")
	private String loanQuoteCoapplicantFirstCoEmployerName;

	@Column(name = "QUOTE_COAPP_FST_MON_SALARY")
	private Double loanQuoteCoapplicantFirstMonthlySalary;

	@Column(name = "QUOTE_COAPP_FST_VAR_MONTH_PAY")
	private Double loanQuoteCoapplicantFirstVariableMonthPay;

	@Column(name = "QUOTE_COAPP_FST_EXP_REN_INC")
	private Double loanQuoteCoapplicantFirstExpectedRentalIncome;

	@Column(name = "QUOTE_COAPP_FST_OTH_INCOME")
	private Double loanQuoteCoapplicantFirstOtherIncome;
	
	@Column(name = "QUOTE_COAPP_FST_PREEMIS")
	private Double loanQuoteCoapplicantFirstPreEMIs;
	
	@Column(name = "QUOTE_COAPP_FST_RET_AGE")
	private Integer loanQuoteCoapplicantFirstRetirementAge;
	
	@Column(name = "QUOTE_COAPP_FST_PAT")
	private Double loanQuoteCoapplicantFirstProfitAfterTax;
	
	@Column(name = "QUOTE_COAPP_FST_DEPRECIATION")
	private Double loanQuoteCoapplicantFirstDepreciatiation;

	@Column(name = "QUOTE_COAPP_FST_GROSS_MON_INC")
	private Double loanQuoteCoapplicantFirstGrossMonthlyIncome;
	
	@Column(name = "QUOTE_COAPP_FST_NET_ANN_INC")
	private Double loanQuoteCoapplicantFirstNetAnnualIncome;

	@Column(name = "QUOTE_COAPP_FST_NET_MON_PEN")
	private Double loanQuoteCoapplicantFirstNetMonthlyPension;
	
	@Column(name = "QUOTE_COAPP_PEN_ACC_WITH_SBI")
	private String loanQuoteCoapplicantpensionAccountWithSbi;
	
	@Column(name = "QUOTE_COAPP_FST_INC_OTH_SRC")
	private Double loanQuoteCoapplicantFirstIncomeFromOtherSource;

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
	private String alternateMobileNumber;
  
	@Transient
	private String appAltISDCode;
	  
	@Column(name = "QUOTE_REQUEST_AMOUNT")
	private Double loanQuoteLoanRequestedAmount;
	
	@Transient
	private Double loanQuoteNetIncome;
	
	@Column(name = "QUOTE_LOAN_AMOUNT")
	private Double loanQuoteLoanAmount;

	@Column(name = "QUOTE_LOAN_PRODUCT_ID")
	private Integer loanQuoteLoanProductId;
	
	@Column(name = "QUOTE_LOAN_ACCOUNT_TYPE")
	private Integer loanQuoteLoanAccountType;
	
	@Column(name = "QUOTE_LOAN_TENURE")
	private Integer loanQuoteLoanTenure;

	@Column(name = "QUOTE_APPLIED_COUPON")
	private String loanQuoteAppliedCoupon;

	@Column(name = "QUOTE_BROWSER_NAME")
	private String loanQuoteBrowserName;

	

	@Column(name = "QUOTE_CREATED_LMS_USER_ID")
	private Integer loanQuoteCreatedLmsUserId;

	@Column(name = "QUOTE_SOURCE_ID")
	private Integer loanQuoteSourceId;

	@Column(name = "QUOTE_VISIT_ID")
	private Integer loanQuoteVisitId;

	@Column(name = "QUOTE_NEW_VISIT_ID")
	private Integer loanQuoteNewVisitId;

	@Temporal(TemporalType.DATE)
	@Column(name = "QUOTE_ENTRY_TIME")
	private Date loanQuoteEntryTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "QUOTE_UPDATED_TIME")
	private Date loanQuoteUpdatedTime;
	
	@Column(name = "QUOTE_IP_ADDRESS")
	private String loanQuoteIpAddress;

	@Column(name = "QUOTE_LEAD_ID")
	private Integer loanQuoteLeadId;

	

	@Column(name = "QUOTE_ACTIVE")
	private String loanQuoteActive;

	@Column(name = "QUOTE_DELETED")
	private String loanQuoteDeleted;

	@Transient
	private Double loanQuoteVehicleCost;
	
	@Transient
	private Double loanQuoteNetAnnualIncomeOfApplicant;

	@Column(name = "QUOTE_MAX_NO_OF_OWNERSHIP")
	private Integer loanQuoteMaxNoOfOwnership;
	
	@Column(name="QUOTE_DEALER_EXSHOWROOM_PRICE")
	private Double loanQuoteDealerExshowroomPrice;
	
	@Column(name="QUOTE_INSURED_DECLARED_VALUE")
	private Double loanQuoteInsuredDeclaredValue;
	
	@Column(name="QUOTE_ON_ROAD_PRICE")
	private Double loanQuoteOnRoadPrice;
	
	@Transient
	private Integer loanQuoteYearCompanyJoining;
	@Transient
	private Integer loanQuoteMonthCompanyJoining;
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_COMPANY_JOINING_DATE")
	private Date loanQuoteCompanyJoiningDate;
	
	@Column(name="QUOTE_EXISTING_HL_BORROWER")
	private String loanQuoteExistingHomeLoanBorrower;
	
	@Column(name="QUOTE_PREEMIS_OTHER")
	private Double loanQuotePreEMIsOther;
	
	@Column(name="QUOTE_PRES_VAL_OF_PROPERTY")
	private Double loanQuotePresentValueOfProperty;
	
	@Column(name="QUOTE_OUT_TOPUP_LOAN_AMOUNT")
	private Double loanQuoteoutstandingTopupLoanAmount;
	
	@Column(name="QUOTE_IS_POSSESSION_COMPLETE")
	private String loanQuoteIsPossessionComplete;
	
	@Temporal(TemporalType.DATE)
	@Column(name="QUOTE_REPAYMENT_STARTDATE")
	private Date loanQuoteRepaymentStartDate;
	
	@Column(name="QUOTE_OUT_HOME_LOAN_AMOUNT")
	private Double loanQuoteoutstandingHomeLoanAmount;
	
	@Column(name="QUOTE_IS_MORTGAGE_CREATED")
	private String loanQuoteIsMortagageCreated;
	
	@Transient
	private String loanQuoteMiddleName;
	
	@Transient
	private String loanQuoteLastName;
	
	@Column(name="QUOTE_CBS_RELATIONSHIP_ID")
	private Integer cbsRelationShipId;
	
	@Transient
	private Integer loanQuoteConsentId;

	
  public String getLoanQuoteAppAlternateMobileNo() {
    return this.loanQuoteAppAlternateMobileNo;
  }
  
  public void setLoanQuoteAppAlternateMobileNo(String loanQuoteAppAlternateMobileNo) {
    this.loanQuoteAppAlternateMobileNo = loanQuoteAppAlternateMobileNo;
  }
  
  public String getLoanQuoteCarTypeId() {
    return this.loanQuoteCarTypeId;
  }
  
  public void setLoanQuoteCarTypeId(String loanQuoteCarTypeId) {
    this.loanQuoteCarTypeId = loanQuoteCarTypeId;
  }
  
  public Integer getLoanQuoteOccupationTypeId() {
    return this.loanQuoteOccupationTypeId;
  }
  
  public void setLoanQuoteOccupationTypeId(Integer loanQuoteOccupationTypeId) {
    this.loanQuoteOccupationTypeId = loanQuoteOccupationTypeId;
  }
  
  public Integer getLoanQuoteOccupationCategory() {
    return this.loanQuoteOccupationCategory;
  }
  
  public void setLoanQuoteOccupationCategory(Integer loanQuoteOccupationCategory) {
    this.loanQuoteOccupationCategory = loanQuoteOccupationCategory;
  }
  
	public Double getLoanQuoteVehicleCost() {
		return loanQuoteVehicleCost;
	}

	public void setLoanQuoteVehicleCost(Double loanQuoteVehicleCost) {
		this.loanQuoteVehicleCost = loanQuoteVehicleCost;
	}

	public Double getLoanQuoteNetAnnualIncomeOfApplicant() {
		return loanQuoteNetAnnualIncomeOfApplicant;
	}

	public void setLoanQuoteNetAnnualIncomeOfApplicant(
			Double loanQuoteNetAnnualIncomeOfApplicant) {
		this.loanQuoteNetAnnualIncomeOfApplicant = loanQuoteNetAnnualIncomeOfApplicant;
	}



	public Integer getLoanQuoteId() {
		return loanQuoteId;
	}

	public void setLoanQuoteId(Integer loanQuoteId) {
		this.loanQuoteId = loanQuoteId;
	}

	

	public Integer getLoanQuoteLoanPurposeId() {
		return loanQuoteLoanPurposeId;
	}

	public void setLoanQuoteLoanPurposeId(Integer loanQuoteLoanPurposeId) {
		this.loanQuoteLoanPurposeId = loanQuoteLoanPurposeId;
	}

	public Integer getLoanQuoteLoanCategoryId() {
		return loanQuoteLoanCategoryId;
	}

	public void setLoanQuoteLoanCategoryId(Integer loanQuoteLoanCategoryId) {
		this.loanQuoteLoanCategoryId = loanQuoteLoanCategoryId;
	}

	public Integer getMonthloanQuoteDop() {
		return monthloanQuoteDop;
	}

	public void setMonthloanQuoteDop(Integer monthloanQuoteDop) {
		this.monthloanQuoteDop = monthloanQuoteDop;
	}

	public Integer getYearloanQuoteDop() {
		return yearloanQuoteDop;
	}

	public void setYearloanQuoteDop(Integer yearloanQuoteDop) {
		this.yearloanQuoteDop = yearloanQuoteDop;
	}

	public Integer getLoanQuoteLoanWithBankId() {
		return loanQuoteLoanWithBankId;
	}

	public void setLoanQuoteLoanWithBankId(Integer loanQuoteLoanWithBankId) {
		this.loanQuoteLoanWithBankId = loanQuoteLoanWithBankId;
	}

	public Integer getMonthloanQuoteStartDateOfCurrentLoan() {
		return monthloanQuoteStartDateOfCurrentLoan;
	}

	public void setMonthloanQuoteStartDateOfCurrentLoan(
			Integer monthloanQuoteStartDateOfCurrentLoan) {
		this.monthloanQuoteStartDateOfCurrentLoan = monthloanQuoteStartDateOfCurrentLoan;
	}

	public Integer getYearloanQuoteStartDateOfCurrentLoan() {
		return yearloanQuoteStartDateOfCurrentLoan;
	}

	public void setYearloanQuoteStartDateOfCurrentLoan(
			Integer yearloanQuoteStartDateOfCurrentLoan) {
		this.yearloanQuoteStartDateOfCurrentLoan = yearloanQuoteStartDateOfCurrentLoan;
	}

	public Double getLoanQuoteOutstandingLoanAmount() {
		return loanQuoteOutstandingLoanAmount;
	}

	public void setLoanQuoteOutstandingLoanAmount(
			Double loanQuoteOutstandingLoanAmount) {
		this.loanQuoteOutstandingLoanAmount = loanQuoteOutstandingLoanAmount;
	}

	public String getLoanQuoteIsDealerOrIndividual() {
		return loanQuoteIsDealerOrIndividual;
	}

	public void setLoanQuoteIsDealerOrIndividual(
			String loanQuoteIsDealerOrIndividual) {
		this.loanQuoteIsDealerOrIndividual = loanQuoteIsDealerOrIndividual;
	}

	public String getLoanQuoteExistingDealerName() {
		return loanQuoteExistingDealerName;
	}

	public void setLoanQuoteExistingDealerName(String loanQuoteExistingDealerName) {
		this.loanQuoteExistingDealerName = loanQuoteExistingDealerName;
	}

	public Integer getLoanQuoteTwoWheelerTypeId() {
		return loanQuoteTwoWheelerTypeId;
	}

	public void setLoanQuoteTwoWheelerTypeId(Integer loanQuoteTwoWheelerTypeId) {
		this.loanQuoteTwoWheelerTypeId = loanQuoteTwoWheelerTypeId;
	}

	public Integer getLoanQuoteBikeMakeId() {
		return loanQuoteBikeMakeId;
	}

	public void setLoanQuoteBikeMakeId(Integer loanQuoteBikeMakeId) {
		this.loanQuoteBikeMakeId = loanQuoteBikeMakeId;
	}

	public Integer getLoanQuoteBikeModelId() {
		return loanQuoteBikeModelId;
	}

	public void setLoanQuoteBikeModelId(Integer loanQuoteBikeModelId) {
		this.loanQuoteBikeModelId = loanQuoteBikeModelId;
	}

	public Integer getLoanQuoteBikeVariantId() {
		return loanQuoteBikeVariantId;
	}

	public void setLoanQuoteBikeVariantId(Integer loanQuoteBikeVariantId) {
		this.loanQuoteBikeVariantId = loanQuoteBikeVariantId;
	}

	public Double getLoanQuoteExshowroomPriceBike() {
		return loanQuoteExshowroomPriceBike;
	}

	public void setLoanQuoteExshowroomPriceBike(Double loanQuoteExshowroomPriceBike) {
		this.loanQuoteExshowroomPriceBike = loanQuoteExshowroomPriceBike;
	}

	public Double getLoanQuoteRoadTaxBike() {
		return loanQuoteRoadTaxBike;
	}

	public void setLoanQuoteRoadTaxBike(Double loanQuoteRoadTaxBike) {
		this.loanQuoteRoadTaxBike = loanQuoteRoadTaxBike;
	}

	public Double getLoanQuoteInsuranceChargeBike() {
		return loanQuoteInsuranceChargeBike;
	}

	public void setLoanQuoteInsuranceChargeBike(Double loanQuoteInsuranceChargeBike) {
		this.loanQuoteInsuranceChargeBike = loanQuoteInsuranceChargeBike;
	}

	public Double getLoanQuoteRegistrationChargeBike() {
		return loanQuoteRegistrationChargeBike;
	}

	public void setLoanQuoteRegistrationChargeBike(
			Double loanQuoteRegistrationChargeBike) {
		this.loanQuoteRegistrationChargeBike = loanQuoteRegistrationChargeBike;
	}

	public Double getLoanQuoteAccessoriesChargeBike() {
		return loanQuoteAccessoriesChargeBike;
	}

	public void setLoanQuoteAccessoriesChargeBike(
			Double loanQuoteAccessoriesChargeBike) {
		this.loanQuoteAccessoriesChargeBike = loanQuoteAccessoriesChargeBike;
	}

	public Double getLoanQuoteOtherMiscChargeBike() {
		return loanQuoteOtherMiscChargeBike;
	}

	public void setLoanQuoteOtherMiscChargeBike(Double loanQuoteOtherMiscChargeBike) {
		this.loanQuoteOtherMiscChargeBike = loanQuoteOtherMiscChargeBike;
	}

	public Integer getLoanQuoteCarMakeId() {
		return loanQuoteCarMakeId;
	}

	public void setLoanQuoteCarMakeId(Integer loanQuoteCarMakeId) {
		this.loanQuoteCarMakeId = loanQuoteCarMakeId;
	}

	public Integer getLoanQuoteCarModelId() {
		return loanQuoteCarModelId;
	}

	public void setLoanQuoteCarModelId(Integer loanQuoteCarModelId) {
		this.loanQuoteCarModelId = loanQuoteCarModelId;
	}

	public Integer getLoanQuoteCarVariantId() {
		return loanQuoteCarVariantId;
	}

	public void setLoanQuoteCarVariantId(Integer loanQuoteCarVariantId) {
		this.loanQuoteCarVariantId = loanQuoteCarVariantId;
	}

	public Double getLoanQuoteExshowroomPriceCar() {
		return loanQuoteExshowroomPriceCar;
	}

	public void setLoanQuoteExshowroomPriceCar(Double loanQuoteExshowroomPriceCar) {
		this.loanQuoteExshowroomPriceCar = loanQuoteExshowroomPriceCar;
	}

	public Double getLoanQuoteRoadTaxCar() {
		return loanQuoteRoadTaxCar;
	}

	public void setLoanQuoteRoadTaxCar(Double loanQuoteRoadTaxCar) {
		this.loanQuoteRoadTaxCar = loanQuoteRoadTaxCar;
	}

	public Double getLoanQuoteInsuranceChargeCar() {
		return loanQuoteInsuranceChargeCar;
	}

	public void setLoanQuoteInsuranceChargeCar(Double loanQuoteInsuranceChargeCar) {
		this.loanQuoteInsuranceChargeCar = loanQuoteInsuranceChargeCar;
	}

	public Double getLoanQuoteRegistrationChargeCar() {
		return loanQuoteRegistrationChargeCar;
	}

	public void setLoanQuoteRegistrationChargeCar(
			Double loanQuoteRegistrationChargeCar) {
		this.loanQuoteRegistrationChargeCar = loanQuoteRegistrationChargeCar;
	}

	public Double getLoanQuoteAccessoriesChargeCar() {
		return loanQuoteAccessoriesChargeCar;
	}

	public void setLoanQuoteAccessoriesChargeCar(
			Double loanQuoteAccessoriesChargeCar) {
		this.loanQuoteAccessoriesChargeCar = loanQuoteAccessoriesChargeCar;
	}

	public Double getLoanQuoteOtherMiscChargeCar() {
		return loanQuoteOtherMiscChargeCar;
	}

	public void setLoanQuoteOtherMiscChargeCar(Double loanQuoteOtherMiscChargeCar) {
		this.loanQuoteOtherMiscChargeCar = loanQuoteOtherMiscChargeCar;
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

	public Integer getLoanQuoteCountryId() {
		return loanQuoteCountryId;
	}

	public void setLoanQuoteCountryId(Integer loanQuoteCountryId) {
		this.loanQuoteCountryId = loanQuoteCountryId;
	}

	public Integer getLoanQuoteCityId() {
		return loanQuoteCityId;
	}

	public void setLoanQuoteCityId(Integer loanQuoteCityId) {
		this.loanQuoteCityId = loanQuoteCityId;
	}

	public Integer getLoanQuoteWorkExperience() {
		return loanQuoteWorkExperience;
	}

	public void setLoanQuoteWorkExperience(Integer loanQuoteWorkExperience) {
		this.loanQuoteWorkExperience = loanQuoteWorkExperience;
	}

	

	public Integer getLoanQuoteEmploymentTypeId() {
		return loanQuoteEmploymentTypeId;
	}

	public void setLoanQuoteEmploymentTypeId(Integer loanQuoteEmploymentTypeId) {
		this.loanQuoteEmploymentTypeId = loanQuoteEmploymentTypeId;
	}

	public String getLoanQuoteHaveSalaryAccountWithSbi() {
		return loanQuoteHaveSalaryAccountWithSbi;
	}

	public void setLoanQuoteHaveSalaryAccountWithSbi(
			String loanQuoteHaveSalaryAccountWithSbi) {
		this.loanQuoteHaveSalaryAccountWithSbi = loanQuoteHaveSalaryAccountWithSbi;
	}

	public String getLoanQuoteEmployerName() {
		return loanQuoteEmployerName;
	}

	public void setLoanQuoteEmployerName(String loanQuoteEmployerName) {
		this.loanQuoteEmployerName = loanQuoteEmployerName;
	}

	public Integer getLoanQuoteEmployerCompanyId() {
		return loanQuoteEmployerCompanyId;
	}

	public void setLoanQuoteEmployerCompanyId(Integer loanQuoteEmployerCompanyId) {
		this.loanQuoteEmployerCompanyId = loanQuoteEmployerCompanyId;
	}

	public Double getLoanQuoteNetMonthlySalary() {
		return loanQuoteNetMonthlySalary;
	}

	public void setLoanQuoteNetMonthlySalary(Double loanQuoteNetMonthlySalary) {
		this.loanQuoteNetMonthlySalary = loanQuoteNetMonthlySalary;
	}

	public Double getLoanQuoteVariableMonthPay() {
		return loanQuoteVariableMonthPay;
	}

	public void setLoanQuoteVariableMonthPay(Double loanQuoteVariableMonthPay) {
		this.loanQuoteVariableMonthPay = loanQuoteVariableMonthPay;
	}

	public Double getLoanQuoteExpectedRentalIncome() {
		return loanQuoteExpectedRentalIncome;
	}

	public void setLoanQuoteExpectedRentalIncome(
			Double loanQuoteExpectedRentalIncome) {
		this.loanQuoteExpectedRentalIncome = loanQuoteExpectedRentalIncome;
	}

	public Double getLoanQuoteOtherIncome() {
		return loanQuoteOtherIncome;
	}

	public void setLoanQuoteOtherIncome(Double loanQuoteOtherIncome) {
		this.loanQuoteOtherIncome = loanQuoteOtherIncome;
	}

	public Double getLoanQuoteNetMonthlyPension() {
		return loanQuoteNetMonthlyPension;
	}

	public void setLoanQuoteNetMonthlyPension(Double loanQuoteNetMonthlyPension) {
		this.loanQuoteNetMonthlyPension = loanQuoteNetMonthlyPension;
	}

	public Integer getLoanQuoteIndustryType() {
		return loanQuoteIndustryType;
	}

	public void setLoanQuoteIndustryType(Integer loanQuoteIndustryType) {
		this.loanQuoteIndustryType = loanQuoteIndustryType;
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

	public Double getLoanQuoteProfitAfterTax() {
		return loanQuoteProfitAfterTax;
	}

	public void setLoanQuoteProfitAfterTax(Double loanQuoteProfitAfterTax) {
		this.loanQuoteProfitAfterTax = loanQuoteProfitAfterTax;
	}

	public Double getLoanQuoteDepreciation() {
		return loanQuoteDepreciation;
	}

	public void setLoanQuoteDepreciation(Double loanQuoteDepreciation) {
		this.loanQuoteDepreciation = loanQuoteDepreciation;
	}

	public Double getLoanQuoteNetAnnualIncome() {
		return loanQuoteNetAnnualIncome;
	}

	public void setLoanQuoteNetAnnualIncome(Double loanQuoteNetAnnualIncome) {
		this.loanQuoteNetAnnualIncome = loanQuoteNetAnnualIncome;
	}

	public Double getLoanQuoteIncomeFromRegularSource() {
		return loanQuoteIncomeFromRegularSource;
	}

	public void setLoanQuoteIncomeFromRegularSource(
			Double loanQuoteIncomeFromRegularSource) {
		this.loanQuoteIncomeFromRegularSource = loanQuoteIncomeFromRegularSource;
	}

	public String getLoanQuotePensionAccountWithSbi() {
		return loanQuotePensionAccountWithSbi;
	}

	public void setLoanQuotePensionAccountWithSbi(
			String loanQuotePensionAccountWithSbi) {
		this.loanQuotePensionAccountWithSbi = loanQuotePensionAccountWithSbi;
	}

	public Integer getLoanQuoteCoapplicantFirstId() {
		return loanQuoteCoapplicantFirstId;
	}

	public void setLoanQuoteCoapplicantFirstId(Integer loanQuoteCoapplicantFirstId) {
		this.loanQuoteCoapplicantFirstId = loanQuoteCoapplicantFirstId;
	}

	public Integer getLoanQuoteCoapplicantFirstRelationshipId() {
		return loanQuoteCoapplicantFirstRelationshipId;
	}

	public void setLoanQuoteCoapplicantFirstRelationshipId(
			Integer loanQuoteCoapplicantFirstRelationshipId) {
		this.loanQuoteCoapplicantFirstRelationshipId = loanQuoteCoapplicantFirstRelationshipId;
	}

	public Integer getLoanQuoteCoapplicantFirstResidentTypeId() {
		return loanQuoteCoapplicantFirstResidentTypeId;
	}

	public void setLoanQuoteCoapplicantFirstResidentTypeId(
			Integer loanQuoteCoapplicantFirstResidentTypeId) {
		this.loanQuoteCoapplicantFirstResidentTypeId = loanQuoteCoapplicantFirstResidentTypeId;
	}

	public Integer getLoanQuoteCoapplicantFirstCountryId() {
		return loanQuoteCoapplicantFirstCountryId;
	}

	public void setLoanQuoteCoapplicantFirstCountryId(
			Integer loanQuoteCoapplicantFirstCountryId) {
		this.loanQuoteCoapplicantFirstCountryId = loanQuoteCoapplicantFirstCountryId;
	}

	public Integer getLoanQuoteCoapplicantFirstWorkExperience() {
		return loanQuoteCoapplicantFirstWorkExperience;
	}

	public void setLoanQuoteCoapplicantFirstWorkExperience(
			Integer loanQuoteCoapplicantFirstWorkExperience) {
		this.loanQuoteCoapplicantFirstWorkExperience = loanQuoteCoapplicantFirstWorkExperience;
	}

	public Integer getLoanQuoteCoapplicantFirstCityId() {
		return loanQuoteCoapplicantFirstCityId;
	}

	public void setLoanQuoteCoapplicantFirstCityId(
			Integer loanQuoteCoapplicantFirstCityId) {
		this.loanQuoteCoapplicantFirstCityId = loanQuoteCoapplicantFirstCityId;
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

	public Double getLoanQuoteCoapplicantFirstMonthlySalary() {
		return loanQuoteCoapplicantFirstMonthlySalary;
	}

	public void setLoanQuoteCoapplicantFirstMonthlySalary(
			Double loanQuoteCoapplicantFirstMonthlySalary) {
		this.loanQuoteCoapplicantFirstMonthlySalary = loanQuoteCoapplicantFirstMonthlySalary;
	}

	public Double getLoanQuoteCoapplicantFirstVariableMonthPay() {
		return loanQuoteCoapplicantFirstVariableMonthPay;
	}

	public void setLoanQuoteCoapplicantFirstVariableMonthPay(
			Double loanQuoteCoapplicantFirstVariableMonthPay) {
		this.loanQuoteCoapplicantFirstVariableMonthPay = loanQuoteCoapplicantFirstVariableMonthPay;
	}

	public Double getLoanQuoteCoapplicantFirstExpectedRentalIncome() {
		return loanQuoteCoapplicantFirstExpectedRentalIncome;
	}

	public void setLoanQuoteCoapplicantFirstExpectedRentalIncome(
			Double loanQuoteCoapplicantFirstExpectedRentalIncome) {
		this.loanQuoteCoapplicantFirstExpectedRentalIncome = loanQuoteCoapplicantFirstExpectedRentalIncome;
	}

	public Double getLoanQuoteCoapplicantFirstOtherIncome() {
		return loanQuoteCoapplicantFirstOtherIncome;
	}

	public void setLoanQuoteCoapplicantFirstOtherIncome(
			Double loanQuoteCoapplicantFirstOtherIncome) {
		this.loanQuoteCoapplicantFirstOtherIncome = loanQuoteCoapplicantFirstOtherIncome;
	}

	public Double getLoanQuoteCoapplicantFirstPreEMIs() {
		return loanQuoteCoapplicantFirstPreEMIs;
	}

	public void setLoanQuoteCoapplicantFirstPreEMIs(
			Double loanQuoteCoapplicantFirstPreEMIs) {
		this.loanQuoteCoapplicantFirstPreEMIs = loanQuoteCoapplicantFirstPreEMIs;
	}

	public Integer getLoanQuoteCoapplicantFirstRetirementAge() {
		return loanQuoteCoapplicantFirstRetirementAge;
	}

	public void setLoanQuoteCoapplicantFirstRetirementAge(
			Integer loanQuoteCoapplicantFirstRetirementAge) {
		this.loanQuoteCoapplicantFirstRetirementAge = loanQuoteCoapplicantFirstRetirementAge;
	}

	public Double getLoanQuoteCoapplicantFirstProfitAfterTax() {
		return loanQuoteCoapplicantFirstProfitAfterTax;
	}

	public void setLoanQuoteCoapplicantFirstProfitAfterTax(
			Double loanQuoteCoapplicantFirstProfitAfterTax) {
		this.loanQuoteCoapplicantFirstProfitAfterTax = loanQuoteCoapplicantFirstProfitAfterTax;
	}

	

	public Double getLoanQuoteCoapplicantFirstDepreciatiation() {
		return loanQuoteCoapplicantFirstDepreciatiation;
	}

	public void setLoanQuoteCoapplicantFirstDepreciatiation(
			Double loanQuoteCoapplicantFirstDepreciatiation) {
		this.loanQuoteCoapplicantFirstDepreciatiation = loanQuoteCoapplicantFirstDepreciatiation;
	}

	public Double getLoanQuoteCoapplicantFirstNetAnnualIncome() {
		return loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public void setLoanQuoteCoapplicantFirstNetAnnualIncome(
			Double loanQuoteCoapplicantFirstNetAnnualIncome) {
		this.loanQuoteCoapplicantFirstNetAnnualIncome = loanQuoteCoapplicantFirstNetAnnualIncome;
	}

	public Double getLoanQuoteCoapplicantFirstNetMonthlyPension() {
		return loanQuoteCoapplicantFirstNetMonthlyPension;
	}

	public void setLoanQuoteCoapplicantFirstNetMonthlyPension(
			Double loanQuoteCoapplicantFirstNetMonthlyPension) {
		this.loanQuoteCoapplicantFirstNetMonthlyPension = loanQuoteCoapplicantFirstNetMonthlyPension;
	}

	public String getLoanQuoteCoapplicantpensionAccountWithSbi() {
		return loanQuoteCoapplicantpensionAccountWithSbi;
	}

	public void setLoanQuoteCoapplicantpensionAccountWithSbi(
			String loanQuoteCoapplicantpensionAccountWithSbi) {
		this.loanQuoteCoapplicantpensionAccountWithSbi = loanQuoteCoapplicantpensionAccountWithSbi;
	}

	public Double getLoanQuoteCoapplicantFirstIncomeFromOtherSource() {
		return loanQuoteCoapplicantFirstIncomeFromOtherSource;
	}

	public void setLoanQuoteCoapplicantFirstIncomeFromOtherSource(
			Double loanQuoteCoapplicantFirstIncomeFromOtherSource) {
		this.loanQuoteCoapplicantFirstIncomeFromOtherSource = loanQuoteCoapplicantFirstIncomeFromOtherSource;
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

	public Double getLoanQuoteLoanRequestedAmount() {
		return loanQuoteLoanRequestedAmount;
	}

	public void setLoanQuoteLoanRequestedAmount(Double loanQuoteLoanRequestedAmount) {
		this.loanQuoteLoanRequestedAmount = loanQuoteLoanRequestedAmount;
	}

	public Double getLoanQuoteNetIncome() {
		return loanQuoteNetIncome;
	}

	public void setLoanQuoteNetIncome(Double loanQuoteNetIncome) {
		this.loanQuoteNetIncome = loanQuoteNetIncome;
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

	public Integer getLoanQuoteLoanAccountType() {
		return loanQuoteLoanAccountType;
	}

	public void setLoanQuoteLoanAccountType(Integer loanQuoteLoanAccountType) {
		this.loanQuoteLoanAccountType = loanQuoteLoanAccountType;
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

	public String getLoanQuoteBrowserName() {
		return loanQuoteBrowserName;
	}

	public void setLoanQuoteBrowserName(String loanQuoteBrowserName) {
		this.loanQuoteBrowserName = loanQuoteBrowserName;
	}

	

	public Integer getLoanQuoteCreatedLmsUserId() {
		return loanQuoteCreatedLmsUserId;
	}

	public void setLoanQuoteCreatedLmsUserId(Integer loanQuoteCreatedLmsUserId) {
		this.loanQuoteCreatedLmsUserId = loanQuoteCreatedLmsUserId;
	}

	public Integer getLoanQuoteSourceId() {
		return loanQuoteSourceId;
	}

	public void setLoanQuoteSourceId(Integer loanQuoteSourceId) {
		this.loanQuoteSourceId = loanQuoteSourceId;
	}

	public Integer getLoanQuoteVisitId() {
		return loanQuoteVisitId;
	}

	public void setLoanQuoteVisitId(Integer loanQuoteVisitId) {
		this.loanQuoteVisitId = loanQuoteVisitId;
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

	public String getLoanQuoteIpAddress() {
		return loanQuoteIpAddress;
	}

	public void setLoanQuoteIpAddress(String loanQuoteIpAddress) {
		this.loanQuoteIpAddress = loanQuoteIpAddress;
	}

	public Integer getLoanQuoteLeadId() {
		return loanQuoteLeadId;
	}

	public void setLoanQuoteLeadId(Integer loanQuoteLeadId) {
		this.loanQuoteLeadId = loanQuoteLeadId;
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

	public Integer getLoanQuoteAge() {
		return loanQuoteAge;
	}

	public void setLoanQuoteAge(Integer loanQuoteAge) {
		this.loanQuoteAge = loanQuoteAge;
	}

	public Integer getLoanQuoteCoapplicantFirstAge() {
		return loanQuoteCoapplicantFirstAge;
	}

	public void setLoanQuoteCoapplicantFirstAge(Integer loanQuoteCoapplicantFirstAge) {
		this.loanQuoteCoapplicantFirstAge = loanQuoteCoapplicantFirstAge;
	}

	public String getLoanQuoteIsInsuranceClaimAvailed() {
		return loanQuoteIsInsuranceClaimAvailed;
	}

	public void setLoanQuoteIsInsuranceClaimAvailed(
			String loanQuoteIsInsuranceClaimAvailed) {
		this.loanQuoteIsInsuranceClaimAvailed = loanQuoteIsInsuranceClaimAvailed;
	}

	public String getLoanQuoteIsPreOwnedUsedCar() {
		return loanQuoteIsPreOwnedUsedCar;
	}

	public void setLoanQuoteIsPreOwnedUsedCar(String loanQuoteIsPreOwnedUsedCar) {
		this.loanQuoteIsPreOwnedUsedCar = loanQuoteIsPreOwnedUsedCar;
	}

	public Integer getLoanQuoteStateId() {
		return loanQuoteStateId;
	}

	public void setLoanQuoteStateId(Integer loanQuoteStateId) {
		this.loanQuoteStateId = loanQuoteStateId;
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

	public Double getLoanQuoteDiscountCar() {
		return loanQuoteDiscountCar;
	}

	public void setLoanQuoteDiscountCar(Double loanQuoteDiscountCar) {
		this.loanQuoteDiscountCar = loanQuoteDiscountCar;
	}

	public Double getLoanQuoteDiscountBike() {
		return loanQuoteDiscountBike;
	}

	public void setLoanQuoteDiscountBike(Double loanQuoteDiscountBike) {
		this.loanQuoteDiscountBike = loanQuoteDiscountBike;
	}

	public Integer getLoanQuoteSalaryPackage() {
		return loanQuoteSalaryPackage;
	}

	public void setLoanQuoteSalaryPackage(Integer loanQuoteSalaryPackage) {
		this.loanQuoteSalaryPackage = loanQuoteSalaryPackage;
	}

	public String getLoanQuoteFirstName() {
		return loanQuoteFirstName;
	}

	public void setLoanQuoteFirstName(String loanQuoteFirstName) {
		this.loanQuoteFirstName = loanQuoteFirstName;
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

	public int getAppApplyingFrom() {
		return appApplyingFrom;
	}

	public void setAppApplyingFrom(int appApplyingFrom) {
		this.appApplyingFrom = appApplyingFrom;
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

	public Date getLoanQuoteDopDT() {
		return loanQuoteDopDT;
	}

	public void setLoanQuoteDopDT(Date loanQuoteDopDT) {
		this.loanQuoteDopDT = loanQuoteDopDT;
	}

	public String getLoanQuoteDop() {
		return loanQuoteDop;
	}

	public void setLoanQuoteDop(String loanQuoteDop) {
		this.loanQuoteDop = loanQuoteDop;
	}

	public Date getLoanQuoteStartDateOfCurrentLoanDT() {
		return loanQuoteStartDateOfCurrentLoanDT;
	}

	public void setLoanQuoteStartDateOfCurrentLoanDT(
			Date loanQuoteStartDateOfCurrentLoanDT) {
		this.loanQuoteStartDateOfCurrentLoanDT = loanQuoteStartDateOfCurrentLoanDT;
	}

	public String getLoanQuoteStartDateOfCurrentLoan() {
		return loanQuoteStartDateOfCurrentLoan;
	}

	public void setLoanQuoteStartDateOfCurrentLoan(
			String loanQuoteStartDateOfCurrentLoan) {
		this.loanQuoteStartDateOfCurrentLoan = loanQuoteStartDateOfCurrentLoan;
	}
	public Integer getLoanQuoteNewVisitId() {
		return loanQuoteNewVisitId;
	}
	public void setLoanQuoteNewVisitId(Integer loanQuoteNewVisitId) {
		this.loanQuoteNewVisitId = loanQuoteNewVisitId;
	}

	public Double getLoanQuoteGrossMonthlyIncome() {
		return loanQuoteGrossMonthlyIncome;
	}

	public void setLoanQuoteGrossMonthlyIncome(Double loanQuoteGrossMonthlyIncome) {
		this.loanQuoteGrossMonthlyIncome = loanQuoteGrossMonthlyIncome;
	}

	public Double getLoanQuoteCoapplicantFirstGrossMonthlyIncome() {
		return loanQuoteCoapplicantFirstGrossMonthlyIncome;
	}

	public void setLoanQuoteCoapplicantFirstGrossMonthlyIncome(
			Double loanQuoteCoapplicantFirstGrossMonthlyIncome) {
		this.loanQuoteCoapplicantFirstGrossMonthlyIncome = loanQuoteCoapplicantFirstGrossMonthlyIncome;
	}

	public Integer getLoanQuoteMaxNoOfOwnership() {
		return loanQuoteMaxNoOfOwnership;
	}

	public void setLoanQuoteMaxNoOfOwnership(Integer loanQuoteMaxNoOfOwnership) {
		this.loanQuoteMaxNoOfOwnership = loanQuoteMaxNoOfOwnership;
	}

	public Double getLoanQuoteDealerExshowroomPrice() {
		return loanQuoteDealerExshowroomPrice;
	}

	public void setLoanQuoteDealerExshowroomPrice(
			Double loanQuoteDealerExshowroomPrice) {
		this.loanQuoteDealerExshowroomPrice = loanQuoteDealerExshowroomPrice;
	}

	public Double getLoanQuoteInsuredDeclaredValue() {
		return loanQuoteInsuredDeclaredValue;
	}

	public void setLoanQuoteInsuredDeclaredValue(
			Double loanQuoteInsuredDeclaredValue) {
		this.loanQuoteInsuredDeclaredValue = loanQuoteInsuredDeclaredValue;
	}

	public Double getLoanQuoteOnRoadPrice() {
		return loanQuoteOnRoadPrice;
	}

	public void setLoanQuoteOnRoadPrice(Double loanQuoteOnRoadPrice) {
		this.loanQuoteOnRoadPrice = loanQuoteOnRoadPrice;
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

	public Date getLoanQuoteCompanyJoiningDate() {
		return loanQuoteCompanyJoiningDate;
	}

	public void setLoanQuoteCompanyJoiningDate(Date loanQuoteCompanyJoiningDate) {
		this.loanQuoteCompanyJoiningDate = loanQuoteCompanyJoiningDate;
	}

	public String getLoanQuoteExistingHomeLoanBorrower() {
		return loanQuoteExistingHomeLoanBorrower;
	}

	public void setLoanQuoteExistingHomeLoanBorrower(
			String loanQuoteExistingHomeLoanBorrower) {
		this.loanQuoteExistingHomeLoanBorrower = loanQuoteExistingHomeLoanBorrower;
	}

	public Double getLoanQuotePreEMIsOther() {
		return loanQuotePreEMIsOther;
	}

	public void setLoanQuotePreEMIsOther(Double loanQuotePreEMIsOther) {
		this.loanQuotePreEMIsOther = loanQuotePreEMIsOther;
	}

	public Double getLoanQuotePresentValueOfProperty() {
		return loanQuotePresentValueOfProperty;
	}

	public void setLoanQuotePresentValueOfProperty(
			Double loanQuotePresentValueOfProperty) {
		this.loanQuotePresentValueOfProperty = loanQuotePresentValueOfProperty;
	}

	public Double getLoanQuoteoutstandingTopupLoanAmount() {
		return loanQuoteoutstandingTopupLoanAmount;
	}

	public void setLoanQuoteoutstandingTopupLoanAmount(
			Double loanQuoteoutstandingTopupLoanAmount) {
		this.loanQuoteoutstandingTopupLoanAmount = loanQuoteoutstandingTopupLoanAmount;
	}

	public String getLoanQuoteIsPossessionComplete() {
		return loanQuoteIsPossessionComplete;
	}

	public void setLoanQuoteIsPossessionComplete(
			String loanQuoteIsPossessionComplete) {
		this.loanQuoteIsPossessionComplete = loanQuoteIsPossessionComplete;
	}

	public String getLoanQuoteIsMortagageCreated() {
		return loanQuoteIsMortagageCreated;
	}

	public void setLoanQuoteIsMortagageCreated(String loanQuoteIsMortagageCreated) {
		this.loanQuoteIsMortagageCreated = loanQuoteIsMortagageCreated;
	}

	public Double getLoanQuoteoutstandingHomeLoanAmount() {
		return loanQuoteoutstandingHomeLoanAmount;
	}

	public void setLoanQuoteoutstandingHomeLoanAmount(
			Double loanQuoteoutstandingHomeLoanAmount) {
		this.loanQuoteoutstandingHomeLoanAmount = loanQuoteoutstandingHomeLoanAmount;
	}
	
	public Integer getCbsRelationShipId() {
		return cbsRelationShipId;
	}

	public void setCbsRelationShipId(Integer cbsRelationShipId) {
		this.cbsRelationShipId = cbsRelationShipId;
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

	public Date getLoanQuoteRepaymentStartDate() {
		return loanQuoteRepaymentStartDate;
	}

	public void setLoanQuoteRepaymentStartDate(Date loanQuoteRepaymentStartDate) {
		this.loanQuoteRepaymentStartDate = loanQuoteRepaymentStartDate;
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
