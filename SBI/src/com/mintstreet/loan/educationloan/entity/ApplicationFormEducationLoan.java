package com.mintstreet.loan.educationloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.SequenceGenerator;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.mintstreet.common.entity.Domain;

import java.util.Date;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00120")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="ApplicationFormEducationLoan.findAll", query="SELECT a FROM ApplicationFormEducationLoan a"),
	@NamedQuery(name="ApplicationFormEducationLoan.getApplicationFormEducationLoanByAppReferenceId", 
		query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appReferenceId =:appReferenceId "),
	@NamedQuery(name="ApplicationFormEducationLoan.getApplicationFormEducationLoanByAppReferenceIdAndMobileNo", 
	query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appMobileNo)=:appMobileNo "),
	
	@NamedQuery(name="ApplicationFormEducationLoan.getApplicationFormEducationLoanByAppReferenceIdAndMobileNoALt", 
	query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appAlternateMobileNumber)=:appAlternateMobileNumber "),
	 

	@NamedQuery(name="ApplicationFormEducationLoan.getApplicationFormEducationLoanByMobileAndSmsOtp", 
	query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N' and a.appMobileVerificationCode =:appMobileVerificationCode and a.appMobileNo=:appMobileNo order by a.appEntryTime desc"),
	
	@NamedQuery(name="ApplicationFormEducationLoan.getApplicationFormEducationLoanByMobileAndSmsOtpAlt", 
	query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N' and a.appMobileVerificationCode =:appMobileVerificationCode and a.appAlternateMobileNumber=:appAlternateMobileNumber order by a.appEntryTime desc"),

})
@SequenceGenerator(name="G6", sequenceName="RUPEEPOWER_OCAS_SEQ_00024" ,allocationSize=1)
public class ApplicationFormEducationLoan extends Domain<Integer> implements Serializable  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G6")
	@Column(name="APP_SEQ_ID")
	private Integer appSeqId;

	@Column(name="APP_QUOTE_ID")
	private Integer appQuoteId;

	@Column(name="APP_REFERENCE_ID")
	private String appReferenceId;

	@Column(name="APP_INTERMEDIARY_ID")
	private Integer appIntermediaryId;

	@Column(name="APP_PRODUCT_VARIANT_ID")
	private Integer appEducationLoanId;

	@Column(name="APP_BANK_ID")
	private Integer appBankId;

	@Column(name="APP_DATA_SOURCE_ID")
	private Integer appDataSourceId;

	@Column(name="APP_FULFILLMENT_GROUP_ID")
	private Integer appFulfillmentGroupId;

	@Column(name="APP_COMPANY_CATEGORY_ID")
	private Integer appCompanyCategoryId;

	@Column(name="APP_LOAN_APPLIED_COUPON")
	private String appLoanAppliedCoupon;

	@Column(name="APP_LOAN_MAX_AMOUNT")
	private Double appLoanMaxAmount;

	@Column(name="APP_LOAN_AMOUNT")
	private Double appLoanAmount;

	@Column(name="APP_LOAN_TENURE")
	private Integer appLoanTenure;

	@Column(name="APP_LOAN_ACCOUNT_TYPE")
	private Integer appLoanAccountType;

	@Column(name="APP_LOAN_INTEREST_RATE")
	private Float appLoanInterestRate;

	@Column(name="APP_LOAN_INTEREST_RATE_TYPE")
	private Integer appLoanInterestRateType;

	@Column(name="APP_LOAN_PROCESSING_FEE")
	private Double appLoanProcessingFee;

	@Column(name="APP_LOAN_EMI")
	private Double appLoanEmi;

	@Column(name="APP_LOAN_STATUS_ID")
	private Integer appLoanStatusId;
	
	@Column(name="APP_LOAN_INTEREST_RATE_DISC")
	private Float appLoanInterestRateDiscount;

	@Column(name="APP_LOAN_PROCESSING_FEE_DICS")
	private Double appLoanProcessingFeeDiscount;

	@Column(name="APP_LOAN_EMI_DISC")
	private Double appLoanEmiDiscount;

	@Column(name="APP_WORK_EMAIL")
	private String appWorkEmail;

	@Column(name="APP_EMAIL_VERIFIED")
	private String appEmailVerified;

	@Column(name="APP_EML_VERIFY_CODE")
	private String appEmailVerificationCode;

	@Column(name="APP_NON_OTP_EML_ALERTS_COUNT")
	private Integer appNonOtpEmailAlertsCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_NON_OTP_EML_AL_LAST_TIME")
	private Date appNonOtpEmailAlertsLastTime;

	@Column(name="APP_MOBILE_NO")
	private String appMobileNo;

	@Column(name="APP_APPLYING_FROM")
	private int appApplyingFrom;
	
	@Column(name="APP_ISD_CODE")
	private String appISDCode;
	
	@Column(name="APP_MOBILE_VERIFY_CODE")
	private Integer appMobileVerificationCode;

	@Column(name="APP_MOB_VERIFYCODE_REC")
	private String appMobileVerificationCodeReceived;

	@Column(name="APP_MOBILE_VERIFIED")
	private String appMobileVerified;

	@Column(name="APP_OTP_MOBILE_ALERTS_COUNT")
	private Integer appOtpMobileAlertsCount;

	@Column(name="APP_FIRST_NAME")
	private String appFirstName;

	@Column(name="APP_MIDDLE_NAME")
	private String appMiddleName;

	@Column(name="APP_LAST_NAME")
	private String appLastName;

	@Column(name="APP_GENDER")
	private String appGender;

	@Column(name="APP_ADDRESS_1")
	private String appAddress1;

	@Column(name="APP_ADDRESS_2")
	private String appAddress2;

	@Column(name="APP_ADDRESS_LANDMARK")
	private String appAddressLandmark;

	@Column(name="APP_STATE_ID")
	private Integer appStateId;

	@Column(name="APP_CITY_ID")
	private Integer appCityId;

	@Column(name="APP_BRANCH_ID")
	private Integer appBranchId;
	
	@Column(name="APP_PREV_BRANCH_ID")
	private Integer appPreviousBranchId;
	
	@Column(name="APP_DISTRICT_ID")
	private Integer appDistrictId;
	
	@Column(name="APP_LOCALITY_ID")
	private Integer appLocalityId;
	
	@Column(name="APP_SALES_TEAM_ID")
	private Integer appSalesTeamId;
	
	@Column(name="APP_PINCODE")
	private Integer appPincode;

	@Column(name="APP_WORK_EXPERIENCE")
	private Integer appWorkExperience;
	
	@Column(name="APP_ADMISSION_QUOTA")
	private Integer appAdmissionQuota;

	@Column(name="APP_QUALIFICATION_ID")
	private Integer appQualificationId;

	@Column(name="APP_EDU_QUALIFICATION_ID")
	private Integer appEducationalQualificationId;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_DATE_OF_BIRTH")
	private Date appDobDT;

	@Transient
	private String appDob;
	
	@Column(name="APP_EMPLOYMENT_TYPE")
	private Integer appEmploymentType;

	@Column(name="APP_LOAN_EMPLOYER_NAME")
	private String appLoanEmployerName;

	@Column(name="APP_PERMANENT_ADDRESS1")
	private String appPermanentAddress1;

	@Column(name="APP_PERMANENT_ADDRESS2")
	private String appPermanentAddress2;
	
	@Column(name="APP_PERMANENT_ADDR_LAND_MARK")
	private String appPermanentAddressLandMark;

	@Column(name="APP_PERMANENT_STATE_ID")
	private Integer appPermanentStateId ;
	
	@Column(name="APP_PERMANENT_CITY_ID")
	private Integer appPermanentCityId ;
	
	@Column(name="APP_PERMANENT_DISTRICT_ID")
	private Integer appPermanentDistrictId ;
	
	@Column(name="APP_PERMANENT_PINCODE")
	private Integer appPermanentPincode;
	
	@Column(name="APP_OFFICE_ADDRESS_1")
	private String appOfficeAddress1;

	@Column(name="APP_OFFICE_ADDRESS_2")
	private String appOfficeAddress2;

	@Column(name="APP_OFFICE_STATE_ID")
	private Integer appOfficeStateId;
	
	@Column(name="APP_OFFICE_CITY_ID")
	private Integer appOfficeCityId;
	
	@Column(name="APP_OFFICE_BRANCH_ID")
	private Integer appOfficeBranchId;
	
	@Column(name="APP_OFFICE_PINCODE")
	private Integer appOfficePincode;
	
	@Column(name="APP_OFFICE_PHONE_STD_CODE")
	private String appOfficePhoneStdCode;

	@Column(name="APP_OFFICE_PHONE_NUMBER")
	private Long appOfficePhoneNumber;
	
	@Column(name="APP_PAN_CARD_NO")
	private String appPanCardNo;

	@Column(name="APP_PAN_CARD_VERIFIED")
	private String appPanCardVerified;

	@Column(name="APP_PAN_CARD_LATER")
	private Boolean appPanCardLater;
	
	@Column(name="APP_PAN_UPDATED_DATETIME")
	private Integer appPanUpdatedDatetime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_PAN_VERIFIED_DATETIME")
	private Date appPanVerifiedDatetime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_COMPANY_JOINING_DATE")
	private Date appCompanyJoiningDate;

	@Column(name="APP_RESIDENCE_TYPE")
	private Integer appResidenceType;

	@Column(name="APP_EVER_DEF_CREDIT_CARD")
	private String appEverDefaultedCreditCard;

	@Column(name="APP_INST_SBI_LIFE_INSURANCE")
	private String appInterestedSbiLifeInsurance;
	
	@Column(name="APP_IS_EXISTING_BANK_ID")
	private Integer appIsExistingBankId;

	@Column(name="APP_EXISTING_REL_TYPE_ID")
	private String appExistingRelationTypeId;
    
	@Column(name="APP_COAPP_FIRST_NAME")
	private String appCoapplicantFirstName;

	@Column(name="APP_COAPP_MIDDLE_NAME")
	private String appCoapplicantMiddleName;

	@Column(name="APP_COAPP_LAST_NAME")
	private String appCoapplicantLastName;
	
	@Column(name="APP_COAPP_ADDRESS_1")
	private String appCoapplicantAddress1;

	@Column(name="APP_COAPP_ADDRESS_2")
	private String appCoapplicantAddress2;

	@Column(name="APP_COAPP_LANDMARK")
	private String appCoapplicantLandmark;

	@Column(name="APP_COAPP_STATE_ID")
	private Integer appCoapplicantStateId;

	@Column(name="APP_COAPP_CITY_ID")
	private Integer appCoapplicantCityId;
	
	@Column(name="APP_COAPP_DISTRICT_ID")
	private Integer appCoapplicantDistrictId;

	@Column(name="APP_COAPP_PINCODE")
	private Integer appCoapplicantPincode;
	
	@Column(name="APP_COAPP_PAN_CARD_NO")
	private String appCoapplicantPanCardNo;
	
	@Column(name="APP_COAPP_PAN_CARD_VERIFIED")
	private String appCoappliantPanCardVerified;
	
	@Column(name="APP_COAPP_PAN_CARD_LATER")
	private Boolean appCoapplicantPanCardLater;
	
	@Column(name="APP_COAPP_MARKET_VAL_OF_COLL")
	private Integer appCoapplicantMarketValueOfCollateral;
	
	@Column(name="APP_COAPP_EMPLOYMENT_TYPE")
	private Integer appCoapplicantEmploymentTypeId;

	@Column(name="APP_COAPP_NMI_AND_NAI")
	private Long appCoapplicantNmiAndNai;	
	
	@Column(name="APP_COAPP_COLLATERAL")
	private Integer appCoapplicantCollateral;
	
	@Column(name="APP_COAPP_PHONE")
	private Long appCoapplicantPhone;
	
	@Column(name="APP_COAPP_RESIDENCE_TYPE")
	private Integer appCoapplicantResidenceType;

	@Column(name="APP_COAPP_IS_COBORROWER")
	private String appCoapplicantIsCoborrower;

	@Column(name="APP_COAPP_EMPLOYER_NAME")
	private String appCoapplicantEmployerName1;

	@Column(name="APP_COAPP_RELATION_TYPE_ID")
 	private Integer appCoapplicantRelationTypeId;
	
	@Column(name="APP_COAPP_EMPLOYMENT_TYPE_ID")
	private Integer appCoapplicantEmloymentType;
	
	@Column(name="APP_DOWNLOAD_PDF_FILE_NAME")
	private String appDownloadPdfFileName;

	@Column(name="APP_DOC_PICK_UP_CHECK")
	private Integer appDocPickupCheck;

	@Column(name="APP_PICKUP_ADDRESS_1")
	private String appPickupAddress1;

	@Column(name="APP_PICKUP_ADDRESS_2")
	private String appPickupAddress2;

	@Column(name="APP_PICKUP_STATE_ID")
	private Integer appPickupStateId;

	@Column(name="APP_PICKUP_CITY_ID")
	private Integer appPickupCityId;
	
	@Column(name="APP_PICKUP_DISTRICT_ID")
	private Integer appPickupDistrictId;

	@Column(name="APP_PICKUP_PINCODE")
	private Integer appPickupPincode;

	@Column(name="APP_DOC_PICKUP_TIME")
	private Date appDocPickupTime;

	@Column(name="APP_DOC_PICKUP_TIME_STR")
	private String appDocPickupTimeString;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APP_DOC_PICKUP_DATE")
	private Date appDocPickupDateDT;

	@Transient
	private String appDocPickupDate;
	
	@Column(name="APP_PHOTO_ID")
	private Integer appPhotoId;
	
	@Column(name="APP_PHOTO_ID_NAME")
	private String appPhotoIdName;

	@Column(name="APP_IDENTITY_PROOF_ID")
	private Integer appIdentityProofId;

	@Column(name="APP_IDENTITY_PROOF_NAME")
	private String appIdentityProofName;

	@Column(name="APP_RESIDENCE_PROOF_ID")
	private Integer appResidenceProofId;

	@Column(name="APP_RESIDENCE_PROOF_NAME")
	private String appResidenceProofName;

	@Column(name="APP_INCOME_PROOF_ID")
	private Integer appIncomeProofId;

	@Column(name="APP_INCOME_PROOF_NAME")
	private String appIncomeProofName;

	@Column(name="APP_EMPLOYMENT_PROOF_ID")
	private Integer appEmploymentProofId;

	@Column(name="APP_EMPLOYMENT_PROOF_NAME")
	private String appEmploymentProofName;

	@Column(name="APP_ACADEMIC_PROOF_ID")
	private Integer appAcademicProofId;

	@Column(name="APP_ACADEMIC_PROOF_NAME")
	private String appAcademicProofName;
	
	@Column(name="APP_MARKET_VALUE")
	private Integer appMarketValue;

	@Column(name="APP_SALARY_BANK_ID")
	private Integer appSalaryBankId;

	@Column(name="APP_ALERT_STATUS_TYPE")
	private Integer appAlertStatusType;

	@Column(name="APP_AMENDED_LMS_USER_ID")
	private Integer appAmendedLmsUserId;
	
	@Column(name="APP_ASS_LMS_SALES_USER_ID")
	private Integer appAssignedLmsSalesUserId;
	
	@Column(name="APP_CREATED_LMS_USER_ID")
	private Integer appCreatedLmsUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_DOCS_ENTERED_TIME")
	private Date appDocsEnteredTime;

	@Column(name="APP_LEAD_UPDATE_TIME")
	private Date appLeadUpdateTime;

	@Transient
	private String appOfferJsonData;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_SALES_ENTERED_TIME")
	private Date appSalesEnteredTime;

	@Column(name="APP_CLONE_COAPP_ADDRESS_1")
	private boolean cloneCoapplicantAddress1;
	
	@Column(name="app_clone_permanent_address")
	private boolean clonePermanentAddress;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_FILLED_AT")
	private Date appFilledAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_ENTRY_TIME")
	private Date appEntryTime;

	@Column(name="APP_RSM_DECISION")
	private Integer appRSMdecision;
	
	@Column(name="APP_ACTIVE")
	private String appActive;
	
	@Column(name="APP_DELETED")
	private String appDeleted;
	
	@Column(name="APP_PRODUCT_TENURE_FLAG")
	private Integer appProductTenureFlag;
	
	@Column(name="APP_HOT_LEAD_CREATED_USER_ID")
	private Integer appHotLeadCreatedLmsUserId;
	
	@Column(name="APP_MORATORIUM_PERIOD")
	private Integer appMoratoriumPeriod;
	
	@Column(name="APP_EMI_NMI_RATIO")
	private Float appEmiNmiRatio;
		
	@Column(name="APP_TOTAL_CALL_ATTEMPT")
	private Integer appTotalCallAttempt;

	@Column(name="APP_MOBILE_DEDUP")
	private Integer appMobileDedup;
	
	@Column(name="APP_CIRCLE_ID")
	private Integer appCircleId;
	
	@Column(name="APP_NETWORK_ID")
	private Integer appNetworkId;
	
	@Column(name="APP_MODULE_ID")
	private Integer appModuleId;
	
	@Column(name="APP_REGION_ID")
	private Integer appRegionId;
	
	@Column(name="APP_CONTACT_CENTER_LOCATION")
	private int appContactCenterLocation;
	
	@Column(name="APP_EDVANTAGE_STATE_ID")
	private Integer appEdvantageStateId;
	
	@Column(name="APP_EDVANTAGE_CITY_ID")
	private Integer appEdvantageCityId;
	
	@Column(name="APP_EDVANTEGE_DISTRICT_ID")
	private Integer appEdvantageDistrictId;
	
	@Column(name="APP_EDVANTEGE_BRANCH_ID")
	private Integer appEdvantageBranchId;
	
	@Column(name="APP_EKYC_REQUEST")
	private String appEkycRequest;
	
	@Column(name="APP_EKYC_RESPONSE")
	private String appEkycResponse;
	
	@Column(name="APP_APP_SUB_TYPE_ID")
	private Integer appSubTypeId;

	@Column(name="APP_OTHER_ID_NUMBER")
	private String appOtherIdNumber;
	
	@Column(name="APP_OTHER_ID")
	private Integer appOtherId;
	
	@Column(name="APP_CO_OTHER_ID")
	private String appCoIdProof;
	
	@Column(name="APP_CO_OTHER_ID_NUMBER")
	private String appCoOtherIdNumber;
	
	@Column(name="APP_CIBIL_SCORE")
	private Integer appCibilScore;
	
	@Column(name="APP_EL_TYPE_ID")
	private Integer appElTypeId ;
	
	@Lob
	@Column(name="APP_CUSTOMER_SIGNATURE")
	private String appCustomerSignature;
	
	@Column(name="APP_CRM_LEAD_ID")
	private Integer appCRMLeadId;
	
	@Column(name="APP_OFFICE_DISTRICT_ID")
	private Integer appOfficeDistrictId;

	@Column(name="APP_ABROAD_ADDRESS_1")
	private String appAbroadAddress1;

	@Column(name="APP_ABROAD_ADDRESS_2")
	private String appAbroadAddress2;

	@Column(name="APP_ABROAD_ADDRESS_LANDMARK")
	private String appAbroadAddressLandMark;

	@Column(name="APP_ABROAD_STATE")
	private String appAbroadState;

	@Column(name="APP_ABROAD_CITY")
	private String appAbroadCity;
	
	@Column(name="APP_ABROAD_COUNTRY_ID")
	private String appAbroadCountryId;
	
	@Column(name="APP_ABROAD_PINCODE")
	private Integer appAbroadPincode;
	
	@Column(name="APP_ANNUAL_INCOME")
	private Double appNetAnnualIncome;
	
	@Column(name="APP_COAPP_GENDER")
	private String appCoapplicantGender;
	
	@Transient
	private String appRequestData;
	
	@Column(name="APP_OTP_ATTEMPT_COUNT")
	private Integer appOTPAttemptCount;
	
	@Column(name="APP_OTP_VERIFY_TYPE")
	private Integer appOTPVerifyType;
	
	@Column(name="APP_RES_TYPE_AT_VERIFIED")
	private Integer appResTypeAtVerified;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APP_ENTRY_DATE")
	private Date appEntryDate;
	
	@Column(name="APP_EMAIL_ATTEMPT_COUNT")
	private Integer appEmailAttemptCount;
	
	@Column(name="APP_AIP_MAIL_SEND_STATUS")
	private String appAipMailSendStatus;
	
	@Column(name="APP_HAVE_AADHAAR_NUMBER")
	private Integer appHaveAadhaarNumber;
	
	@Column(name="APP_HAVE_COAPP_AADHAAR_NUMBER")
	private Integer appCoAppHaveAadhaarNumber;
	
	@Column(name = "APP_ALTERNATE_MOBILE_NUMBER") 
	private String appAlternateMobileNumber;
    
	@Column(name = "APP_ALT_ISD_CODE")
	private String appAltISDCode;
	
	@Column(name = "APP_ALT_MOB_VERIFYCODE_REC")
	private String appAltMobileVerificationCodeReceived;
	
	@Column(name = "APP_ALT_MOBILE_VERIFY_CODE")
	private Integer appAltMobileVerificationCode;
	 
	@Column(name = "APP_ALT_OTP_MOB_ALERTS_COUNT")
	private Integer appAltOtpMobAlertsCount;
	
	@Column(name = "APP_ALT_MOBILE_VERIFIED")
	private String appAltMobileVerified;
	
	@Column(name = "CRM_LEAD_SOURCE")
	private String crmLeadSource;
	
	@Column(name = "CRM_LEAD_SOURCE_ID")
	private Integer crmLeadSourceId;
	
	@Column(name = "OCAS_ID")
	private String ocasID;
	
	@Column(name="APP_CONSENT_ID")
	private Integer appConsentId;
	
	@Transient
	private String appMobileNumberMask;
	
	public String getOcasID() {
		return ocasID;
	}

	public void setOcasID(String ocasID) {
		this.ocasID = ocasID;
	}
	public String getAppEkycRequest() {
		return appEkycRequest;
	}

	public void setAppEkycRequest(String appEkycRequest) {
		this.appEkycRequest = appEkycRequest;
	}

	public String getAppEkycResponse() {
		return appEkycResponse;
	}

	public void setAppEkycResponse(String appEkycResponse) {
		this.appEkycResponse = appEkycResponse;
	}

	public Integer getAppEdvantageStateId() {
		return appEdvantageStateId;
	}

	public void setAppEdvantageStateId(Integer appEdvantageStateId) {
		this.appEdvantageStateId = appEdvantageStateId;
	}

	public Integer getAppEdvantageCityId() {
		return appEdvantageCityId;
	}

	public void setAppEdvantageCityId(Integer appEdvantageCityId) {
		this.appEdvantageCityId = appEdvantageCityId;
	}

	public Integer getAppEdvantageDistrictId() {
		return appEdvantageDistrictId;
	}

	public void setAppEdvantageDistrictId(Integer appEdvantageDistrictId) {
		this.appEdvantageDistrictId = appEdvantageDistrictId;
	}

	public Integer getAppEdvantageBranchId() {
		return appEdvantageBranchId;
	}

	public void setAppEdvantageBranchId(Integer appEdvantageBranchId) {
		this.appEdvantageBranchId = appEdvantageBranchId;
	}

	public Integer getAppSeqId() {
		return appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public Integer getAppQuoteId() {
		return appQuoteId;
	}

	public void setAppQuoteId(Integer appQuoteId) {
		this.appQuoteId = appQuoteId;
	}

	public String getAppReferenceId() {
		return appReferenceId;
	}

	public void setAppReferenceId(String appReferenceId) {
		this.appReferenceId = appReferenceId;
	}

	public Integer getAppIntermediaryId() {
		return appIntermediaryId;
	}

	public void setAppIntermediaryId(Integer appIntermediaryId) {
		this.appIntermediaryId = appIntermediaryId;
	}

	public Integer getAppEducationLoanId() {
		return appEducationLoanId;
	}

	public void setAppEducationLoanId(Integer appEducationLoanId) {
		this.appEducationLoanId = appEducationLoanId;
	}

	public Integer getAppBankId() {
		return appBankId;
	}

	public void setAppBankId(Integer appBankId) {
		this.appBankId = appBankId;
	}

	public Integer getAppDataSourceId() {
		return appDataSourceId;
	}

	public void setAppDataSourceId(Integer appDataSourceId) {
		this.appDataSourceId = appDataSourceId;
	}

	public Integer getAppFulfillmentGroupId() {
		return appFulfillmentGroupId;
	}

	public void setAppFulfillmentGroupId(Integer appFulfillmentGroupId) {
		this.appFulfillmentGroupId = appFulfillmentGroupId;
	}

	public Integer getAppCompanyCategoryId() {
		return appCompanyCategoryId;
	}

	public void setAppCompanyCategoryId(Integer appCompanyCategoryId) {
		this.appCompanyCategoryId = appCompanyCategoryId;
	}

	public String getAppLoanAppliedCoupon() {
		return appLoanAppliedCoupon;
	}

	public void setAppLoanAppliedCoupon(String appLoanAppliedCoupon) {
		this.appLoanAppliedCoupon = appLoanAppliedCoupon;
	}

	public Double getAppLoanMaxAmount() {
		return appLoanMaxAmount;
	}

	public void setAppLoanMaxAmount(Double appLoanMaxAmount) {
		this.appLoanMaxAmount = appLoanMaxAmount;
	}

	public Double getAppLoanAmount() {
		return appLoanAmount;
	}

	public void setAppLoanAmount(Double appLoanAmount) {
		this.appLoanAmount = appLoanAmount;
	}

	public Integer getAppLoanTenure() {
		return appLoanTenure;
	}

	public void setAppLoanTenure(Integer appLoanTenure) {
		this.appLoanTenure = appLoanTenure;
	}

	public Integer getAppLoanAccountType() {
		return appLoanAccountType;
	}

	public void setAppLoanAccountType(Integer appLoanAccountType) {
		this.appLoanAccountType = appLoanAccountType;
	}

	public Float getAppLoanInterestRate() {
		return appLoanInterestRate;
	}

	public void setAppLoanInterestRate(Float appLoanInterestRate) {
		this.appLoanInterestRate = appLoanInterestRate;
	}

	public Integer getAppLoanInterestRateType() {
		return appLoanInterestRateType;
	}

	public void setAppLoanInterestRateType(Integer appLoanInterestRateType) {
		this.appLoanInterestRateType = appLoanInterestRateType;
	}

	public Double getAppLoanProcessingFee() {
		return appLoanProcessingFee;
	}

	public void setAppLoanProcessingFee(Double appLoanProcessingFee) {
		this.appLoanProcessingFee = appLoanProcessingFee;
	}

	public Double getAppLoanEmi() {
		return appLoanEmi;
	}

	public void setAppLoanEmi(Double appLoanEmi) {
		this.appLoanEmi = appLoanEmi;
	}

	public Integer getAppLoanStatusId() {
		return appLoanStatusId;
	}

	public void setAppLoanStatusId(Integer appLoanStatusId) {
		this.appLoanStatusId = appLoanStatusId;
	}

	public String getAppWorkEmail() {
		return appWorkEmail;
	}

	public void setAppWorkEmail(String appWorkEmail) {
		this.appWorkEmail = appWorkEmail;
	}

	public String getAppEmailVerified() {
		return appEmailVerified;
	}

	public void setAppEmailVerified(String appEmailVerified) {
		this.appEmailVerified = appEmailVerified;
	}

	public String getAppEmailVerificationCode() {
		return appEmailVerificationCode;
	}

	public void setAppEmailVerificationCode(String appEmailVerificationCode) {
		this.appEmailVerificationCode = appEmailVerificationCode;
	}

	public Integer getAppNonOtpEmailAlertsCount() {
		return appNonOtpEmailAlertsCount;
	}

	public void setAppNonOtpEmailAlertsCount(Integer appNonOtpEmailAlertsCount) {
		this.appNonOtpEmailAlertsCount = appNonOtpEmailAlertsCount;
	}

	public Date getAppNonOtpEmailAlertsLastTime() {
		return appNonOtpEmailAlertsLastTime;
	}

	public void setAppNonOtpEmailAlertsLastTime(Date appNonOtpEmailAlertsLastTime) {
		this.appNonOtpEmailAlertsLastTime = appNonOtpEmailAlertsLastTime;
	}

	public String getAppMobileNo() {
		return appMobileNo;
	}

	public void setAppMobileNo(String appMobileNo) {
		this.appMobileNo = appMobileNo;
	}

	public Integer getAppMobileVerificationCode() {
		return appMobileVerificationCode;
	}

	public void setAppMobileVerificationCode(Integer appMobileVerificationCode) {
		this.appMobileVerificationCode = appMobileVerificationCode;
	}

	public String getAppMobileVerificationCodeReceived() {
		return appMobileVerificationCodeReceived;
	}

	public void setAppMobileVerificationCodeReceived(
			String appMobileVerificationCodeReceived) {
		this.appMobileVerificationCodeReceived = appMobileVerificationCodeReceived;
	}

	public String getAppMobileVerified() {
		return appMobileVerified;
	}

	public void setAppMobileVerified(String appMobileVerified) {
		this.appMobileVerified = appMobileVerified;
	}

	public Integer getAppOtpMobileAlertsCount() {
		return appOtpMobileAlertsCount;
	}

	public void setAppOtpMobileAlertsCount(Integer appOtpMobileAlertsCount) {
		this.appOtpMobileAlertsCount = appOtpMobileAlertsCount;
	}

	public String getAppFirstName() {
		return appFirstName;
	}

	public void setAppFirstName(String appFirstName) {
		this.appFirstName = appFirstName;
	}

	public String getAppMiddleName() {
		return appMiddleName;
	}

	public void setAppMiddleName(String appMiddleName) {
		this.appMiddleName = appMiddleName;
	}

	public String getAppLastName() {
		return appLastName;
	}

	public void setAppLastName(String appLastName) {
		this.appLastName = appLastName;
	}

	public String getAppGender() {
		return appGender;
	}

	public void setAppGender(String appGender) {
		this.appGender = appGender;
	}

	public String getAppAddress1() {
		return appAddress1;
	}

	public void setAppAddress1(String appAddress1) {
		this.appAddress1 = appAddress1;
	}

	public String getAppAddress2() {
		return appAddress2;
	}

	public void setAppAddress2(String appAddress2) {
		this.appAddress2 = appAddress2;
	}

	public String getAppAddressLandmark() {
		return appAddressLandmark;
	}

	public void setAppAddressLandmark(String appAddressLandmark) {
		this.appAddressLandmark = appAddressLandmark;
	}

	public Integer getAppStateId() {
		return appStateId;
	}

	public void setAppStateId(Integer appStateId) {
		this.appStateId = appStateId;
	}

	public Integer getAppCityId() {
		return appCityId;
	}

	public void setAppCityId(Integer appCityId) {
		this.appCityId = appCityId;
	}

	public Integer getAppWorkExperience() {
		return appWorkExperience;
	}

	public void setAppWorkExperience(Integer appWorkExperience) {
		this.appWorkExperience = appWorkExperience;
	}

	public Integer getAppBranchId() {
		return appBranchId;
	}

	public void setAppBranchId(Integer appBranchId) {
		this.appBranchId = appBranchId;
	}

	public Integer getAppPincode() {
		return appPincode;
	}

	public void setAppPincode(Integer appPincode) {
		this.appPincode = appPincode;
	}

	public Integer getAppAdmissionQuota() {
		return appAdmissionQuota;
	}

	public void setAppAdmissionQuota(Integer appAdmissionQuota) {
		this.appAdmissionQuota = appAdmissionQuota;
	}

	public Integer getAppQualificationId() {
		return appQualificationId;
	}

	public void setAppQualificationId(Integer appQualificationId) {
		this.appQualificationId = appQualificationId;
	}

	public Integer getAppEducationalQualificationId() {
		return appEducationalQualificationId;
	}

	public void setAppEducationalQualificationId(
			Integer appEducationalQualificationId) {
		this.appEducationalQualificationId = appEducationalQualificationId;
	}

	public Integer getAppEmploymentType() {
		return appEmploymentType;
	}

	public void setAppEmploymentType(Integer appEmploymentType) {
		this.appEmploymentType = appEmploymentType;
	}

	public String getAppLoanEmployerName() {
		return appLoanEmployerName;
	}

	public void setAppLoanEmployerName(String appLoanEmployerName) {
		this.appLoanEmployerName = appLoanEmployerName;
	}

	public String getAppPermanentAddress1() {
		return appPermanentAddress1;
	}

	public void setAppPermanentAddress1(String appPermanentAddress1) {
		this.appPermanentAddress1 = appPermanentAddress1;
	}

	public String getAppPermanentAddress2() {
		return appPermanentAddress2;
	}

	public void setAppPermanentAddress2(String appPermanentAddress2) {
		this.appPermanentAddress2 = appPermanentAddress2;
	}

	public String getAppPermanentAddressLandMark() {
		return appPermanentAddressLandMark;
	}

	public void setAppPermanentAddressLandMark(String appPermanentAddressLandMark) {
		this.appPermanentAddressLandMark = appPermanentAddressLandMark;
	}

	public Integer getAppPermanentStateId() {
		return appPermanentStateId;
	}

	public void setAppPermanentStateId(Integer appPermanentStateId) {
		this.appPermanentStateId = appPermanentStateId;
	}

	public Integer getAppPermanentCityId() {
		return appPermanentCityId;
	}

	public void setAppPermanentCityId(Integer appPermanentCityId) {
		this.appPermanentCityId = appPermanentCityId;
	}

	public Integer getAppPermanentPincode() {
		return appPermanentPincode;
	}

	public void setAppPermanentPincode(Integer appPermanentPincode) {
		this.appPermanentPincode = appPermanentPincode;
	}

	public Integer getAppOfficeStateId() {
		return appOfficeStateId;
	}

	public void setAppOfficeStateId(Integer appOfficeStateId) {
		this.appOfficeStateId = appOfficeStateId;
	}

	public String getAppPanCardNo() {
		return appPanCardNo;
	}

	public void setAppPanCardNo(String appPanCardNo) {
		this.appPanCardNo = appPanCardNo;
	}

	public String getAppPanCardVerified() {
		return appPanCardVerified;
	}

	public void setAppPanCardVerified(String appPanCardVerified) {
		this.appPanCardVerified = appPanCardVerified;
	}

	public Integer getAppPanUpdatedDatetime() {
		return appPanUpdatedDatetime;
	}

	public void setAppPanUpdatedDatetime(Integer appPanUpdatedDatetime) {
		this.appPanUpdatedDatetime = appPanUpdatedDatetime;
	}

	public Date getAppPanVerifiedDatetime() {
		return appPanVerifiedDatetime;
	}

	public void setAppPanVerifiedDatetime(Date appPanVerifiedDatetime) {
		this.appPanVerifiedDatetime = appPanVerifiedDatetime;
	}

	public Date getAppCompanyJoiningDate() {
		return appCompanyJoiningDate;
	}

	public void setAppCompanyJoiningDate(Date appCompanyJoiningDate) {
		this.appCompanyJoiningDate = appCompanyJoiningDate;
	}

	public Integer getAppResidenceType() {
		return appResidenceType;
	}

	public void setAppResidenceType(Integer appResidenceType) {
		this.appResidenceType = appResidenceType;
	}

	public String getAppEverDefaultedCreditCard() {
		return appEverDefaultedCreditCard;
	}

	public void setAppEverDefaultedCreditCard(String appEverDefaultedCreditCard) {
		this.appEverDefaultedCreditCard = appEverDefaultedCreditCard;
	}

	public Integer getAppIsExistingBankId() {
		return appIsExistingBankId;
	}

	public void setAppIsExistingBankId(Integer appIsExistingBankId) {
		this.appIsExistingBankId = appIsExistingBankId;
	}

	public String getAppExistingRelationTypeId() {
		return appExistingRelationTypeId;
	}

	public void setAppExistingRelationTypeId(String appExistingRelationTypeId) {
		this.appExistingRelationTypeId = appExistingRelationTypeId;
	}

	public String getAppCoapplicantFirstName() {
		return appCoapplicantFirstName;
	}

	public void setAppCoapplicantFirstName(String appCoapplicantFirstName) {
		this.appCoapplicantFirstName = appCoapplicantFirstName;
	}

	public String getAppCoapplicantMiddleName() {
		return appCoapplicantMiddleName;
	}

	public void setAppCoapplicantMiddleName(String appCoapplicantMiddleName) {
		this.appCoapplicantMiddleName = appCoapplicantMiddleName;
	}

	public String getAppCoapplicantLastName() {
		return appCoapplicantLastName;
	}

	public void setAppCoapplicantLastName(String appCoapplicantLastName) {
		this.appCoapplicantLastName = appCoapplicantLastName;
	}

	public String getAppCoapplicantAddress1() {
		return appCoapplicantAddress1;
	}

	public void setAppCoapplicantAddress1(String appCoapplicantAddress1) {
		this.appCoapplicantAddress1 = appCoapplicantAddress1;
	}

	public String getAppCoapplicantAddress2() {
		return appCoapplicantAddress2;
	}

	public void setAppCoapplicantAddress2(String appCoapplicantAddress2) {
		this.appCoapplicantAddress2 = appCoapplicantAddress2;
	}

	public String getAppCoapplicantLandmark() {
		return appCoapplicantLandmark;
	}

	public void setAppCoapplicantLandmark(String appCoapplicantLandmark) {
		this.appCoapplicantLandmark = appCoapplicantLandmark;
	}

	public Integer getAppCoapplicantStateId() {
		return appCoapplicantStateId;
	}

	public void setAppCoapplicantStateId(Integer appCoapplicantStateId) {
		this.appCoapplicantStateId = appCoapplicantStateId;
	}

	public Integer getAppCoapplicantCityId() {
		return appCoapplicantCityId;
	}

	public void setAppCoapplicantCityId(Integer appCoapplicantCityId) {
		this.appCoapplicantCityId = appCoapplicantCityId;
	}

	public Integer getAppCoapplicantPincode() {
		return appCoapplicantPincode;
	}

	public void setAppCoapplicantPincode(Integer appCoapplicantPincode) {
		this.appCoapplicantPincode = appCoapplicantPincode;
	}

	public String getAppCoapplicantPanCardNo() {
		return appCoapplicantPanCardNo;
	}

	public void setAppCoapplicantPanCardNo(String appCoapplicantPanCardNo) {
		this.appCoapplicantPanCardNo = appCoapplicantPanCardNo;
	}

	public Integer getAppCoapplicantMarketValueOfCollateral() {
		return appCoapplicantMarketValueOfCollateral;
	}

	public void setAppCoapplicantMarketValueOfCollateral(
			Integer appCoapplicantMarketValueOfCollateral) {
		this.appCoapplicantMarketValueOfCollateral = appCoapplicantMarketValueOfCollateral;
	}

	public Integer getAppCoapplicantEmploymentTypeId() {
		return appCoapplicantEmploymentTypeId;
	}

	public void setAppCoapplicantEmploymentTypeId(
			Integer appCoapplicantEmploymentTypeId) {
		this.appCoapplicantEmploymentTypeId = appCoapplicantEmploymentTypeId;
	}

	public Long getAppCoapplicantNmiAndNai() {
		return appCoapplicantNmiAndNai;
	}

	public void setAppCoapplicantNmiAndNai(Long appCoapplicantNmiAndNai) {
		this.appCoapplicantNmiAndNai = appCoapplicantNmiAndNai;
	}

	public Integer getAppCoapplicantCollateral() {
		return appCoapplicantCollateral;
	}

	public void setAppCoapplicantCollateral(Integer appCoapplicantCollateral) {
		this.appCoapplicantCollateral = appCoapplicantCollateral;
	}

	public Long getAppCoapplicantPhone() {
		return appCoapplicantPhone;
	}

	public void setAppCoapplicantPhone(Long appCoapplicantPhone) {
		this.appCoapplicantPhone = appCoapplicantPhone;
	}

	public Integer getAppCoapplicantResidenceType() {
		return appCoapplicantResidenceType;
	}

	public void setAppCoapplicantResidenceType(Integer appCoapplicantResidenceType) {
		this.appCoapplicantResidenceType = appCoapplicantResidenceType;
	}

	public String getAppCoapplicantIsCoborrower() {
		return appCoapplicantIsCoborrower;
	}

	public void setAppCoapplicantIsCoborrower(String appCoapplicantIsCoborrower) {
		this.appCoapplicantIsCoborrower = appCoapplicantIsCoborrower;
	}

	public String getAppCoapplicantEmployerName1() {
		return appCoapplicantEmployerName1;
	}

	public void setAppCoapplicantEmployerName1(String appCoapplicantEmployerName1) {
		this.appCoapplicantEmployerName1 = appCoapplicantEmployerName1;
	}

	public Integer getAppCoapplicantEmloymentType() {
		return appCoapplicantEmloymentType;
	}

	public void setAppCoapplicantEmloymentType(Integer appCoapplicantEmloymentType) {
		this.appCoapplicantEmloymentType = appCoapplicantEmloymentType;
	}

	public String getAppDownloadPdfFileName() {
		return appDownloadPdfFileName;
	}

	public void setAppDownloadPdfFileName(String appDownloadPdfFileName) {
		this.appDownloadPdfFileName = appDownloadPdfFileName;
	}

	public Integer getAppDocPickupCheck() {
		return appDocPickupCheck;
	}

	public void setAppDocPickupCheck(Integer appDocPickupCheck) {
		this.appDocPickupCheck = appDocPickupCheck;
	}

	public String getAppPickupAddress1() {
		return appPickupAddress1;
	}

	public void setAppPickupAddress1(String appPickupAddress1) {
		this.appPickupAddress1 = appPickupAddress1;
	}

	public String getAppPickupAddress2() {
		return appPickupAddress2;
	}

	public void setAppPickupAddress2(String appPickupAddress2) {
		this.appPickupAddress2 = appPickupAddress2;
	}

	public Integer getAppPickupStateId() {
		return appPickupStateId;
	}

	public void setAppPickupStateId(Integer appPickupStateId) {
		this.appPickupStateId = appPickupStateId;
	}

	public Integer getAppPickupCityId() {
		return appPickupCityId;
	}

	public void setAppPickupCityId(Integer appPickupCityId) {
		this.appPickupCityId = appPickupCityId;
	}
	
	public Integer getAppPickupDistrictId() {
		return appPickupDistrictId;
	}

	public void setAppPickupDistrictId(Integer appPickupDistrictId) {
		this.appPickupDistrictId = appPickupDistrictId;
	}

	public Integer getAppPickupPincode() {
		return appPickupPincode;
	}

	public void setAppPickupPincode(Integer appPickupPincode) {
		this.appPickupPincode = appPickupPincode;
	}

	public Date getAppDocPickupTime() {
		return appDocPickupTime;
	}

	public void setAppDocPickupTime(Date appDocPickupTime) {
		this.appDocPickupTime = appDocPickupTime;
	}

	public Integer getAppPhotoId() {
		return appPhotoId;
	}

	public void setAppPhotoId(Integer appPhotoId) {
		this.appPhotoId = appPhotoId;
	}

	public String getAppPhotoIdName() {
		return appPhotoIdName;
	}

	public void setAppPhotoIdName(String appPhotoIdName) {
		this.appPhotoIdName = appPhotoIdName;
	}

	public Integer getAppIdentityProofId() {
		return appIdentityProofId;
	}

	public void setAppIdentityProofId(Integer appIdentityProofId) {
		this.appIdentityProofId = appIdentityProofId;
	}

	public String getAppIdentityProofName() {
		return appIdentityProofName;
	}

	public void setAppIdentityProofName(String appIdentityProofName) {
		this.appIdentityProofName = appIdentityProofName;
	}

	public Integer getAppResidenceProofId() {
		return appResidenceProofId;
	}

	public void setAppResidenceProofId(Integer appResidenceProofId) {
		this.appResidenceProofId = appResidenceProofId;
	}

	public String getAppResidenceProofName() {
		return appResidenceProofName;
	}

	public void setAppResidenceProofName(String appResidenceProofName) {
		this.appResidenceProofName = appResidenceProofName;
	}

	public Integer getAppIncomeProofId() {
		return appIncomeProofId;
	}

	public void setAppIncomeProofId(Integer appIncomeProofId) {
		this.appIncomeProofId = appIncomeProofId;
	}

	public String getAppIncomeProofName() {
		return appIncomeProofName;
	}

	public void setAppIncomeProofName(String appIncomeProofName) {
		this.appIncomeProofName = appIncomeProofName;
	}

	public Integer getAppEmploymentProofId() {
		return appEmploymentProofId;
	}

	public void setAppEmploymentProofId(Integer appEmploymentProofId) {
		this.appEmploymentProofId = appEmploymentProofId;
	}

	public String getAppEmploymentProofName() {
		return appEmploymentProofName;
	}

	public void setAppEmploymentProofName(String appEmploymentProofName) {
		this.appEmploymentProofName = appEmploymentProofName;
	}

	public Integer getAppMarketValue() {
		return appMarketValue;
	}

	public void setAppMarketValue(Integer appMarketValue) {
		this.appMarketValue = appMarketValue;
	}

	public Integer getAppSalaryBankId() {
		return appSalaryBankId;
	}

	public void setAppSalaryBankId(Integer appSalaryBankId) {
		this.appSalaryBankId = appSalaryBankId;
	}

	public Integer getAppAlertStatusType() {
		return appAlertStatusType;
	}

	public void setAppAlertStatusType(Integer appAlertStatusType) {
		this.appAlertStatusType = appAlertStatusType;
	}

	public Integer getAppAmendedLmsUserId() {
		return appAmendedLmsUserId;
	}

	public void setAppAmendedLmsUserId(Integer appAmendedLmsUserId) {
		this.appAmendedLmsUserId = appAmendedLmsUserId;
	}

	public Integer getAppCreatedLmsUserId() {
		return appCreatedLmsUserId;
	}

	public void setAppCreatedLmsUserId(Integer appCreatedLmsUserId) {
		this.appCreatedLmsUserId = appCreatedLmsUserId;
	}

	public Date getAppDocsEnteredTime() {
		return appDocsEnteredTime;
	}

	public void setAppDocsEnteredTime(Date appDocsEnteredTime) {
		this.appDocsEnteredTime = appDocsEnteredTime;
	}

	public Integer getAppHotLeadCreatedLmsUserId() {
		return appHotLeadCreatedLmsUserId;
	}

	public void setAppHotLeadCreatedLmsUserId(Integer appHotLeadCreatedLmsUserId) {
		this.appHotLeadCreatedLmsUserId = appHotLeadCreatedLmsUserId;
	}

	public Integer getAppCoapplicantRelationTypeId() {
		return appCoapplicantRelationTypeId;
	}

	public void setAppCoapplicantRelationTypeId(Integer appCoapplicantRelationTypeId) {
		this.appCoapplicantRelationTypeId = appCoapplicantRelationTypeId;
	}

	public Date getAppLeadUpdateTime() {
		return appLeadUpdateTime;
	}

	public void setAppLeadUpdateTime(Date appLeadUpdateTime) {
		this.appLeadUpdateTime = appLeadUpdateTime;
	}

	public String getAppOfferJsonData() {
		return appOfferJsonData;
	}

	public void setAppOfferJsonData(String appOfferJsonData) {
		this.appOfferJsonData = appOfferJsonData;
	}

	public Date getAppSalesEnteredTime() {
		return appSalesEnteredTime;
	}

	public void setAppSalesEnteredTime(Date appSalesEnteredTime) {
		this.appSalesEnteredTime = appSalesEnteredTime;
	}

	public boolean isCloneCoapplicantAddress1() {
		return cloneCoapplicantAddress1;
	}

	public void setCloneCoapplicantAddress1(boolean cloneCoapplicantAddress1) {
		this.cloneCoapplicantAddress1 = cloneCoapplicantAddress1;
	}

	public boolean isClonePermanentAddress() {
		return clonePermanentAddress;
	}

	public void setClonePermanentAddress(boolean clonePermanentAddress) {
		this.clonePermanentAddress = clonePermanentAddress;
	}

	public Date getAppFilledAt() {
		return appFilledAt;
	}

	public void setAppFilledAt(Date appFilledAt) {
		this.appFilledAt = appFilledAt;
	}

	public Date getAppEntryTime() {
		return appEntryTime;
	}

	public void setAppEntryTime(Date appEntryTime) {
		this.appEntryTime = appEntryTime;
	}

	public String getAppActive() {
		return appActive;
	}

	public void setAppActive(String appActive) {
		this.appActive = appActive;
	}

	public String getAppDeleted() {
		return appDeleted;
	}

	public void setAppDeleted(String appDeleted) {
		this.appDeleted = appDeleted;
	}

	public Integer getAppDistrictId() {
		return appDistrictId;
	}

	public void setAppDistrictId(Integer appDistrictId) {
		this.appDistrictId = appDistrictId;
	}

	public Integer getAppLocalityId() {
		return appLocalityId;
	}

	public void setAppLocalityId(Integer appLocalityId) {
		this.appLocalityId = appLocalityId;
	}

	public Integer getAppRSMdecision() {
		return appRSMdecision;
	}

	public void setAppRSMdecision(Integer appRSMdecision) {
		this.appRSMdecision = appRSMdecision;
	}

	public String getAppInterestedSbiLifeInsurance() {
		return appInterestedSbiLifeInsurance;
	}

	public void setAppInterestedSbiLifeInsurance(
			String appInterestedSbiLifeInsurance) {
		this.appInterestedSbiLifeInsurance = appInterestedSbiLifeInsurance;
	}

	public String getAppCoappliantPanCardVerified() {
		return appCoappliantPanCardVerified;
	}

	public void setAppCoappliantPanCardVerified(String appCoappliantPanCardVerified) {
		this.appCoappliantPanCardVerified = appCoappliantPanCardVerified;
	}

	public Integer getAppSalesTeamId() {
		return appSalesTeamId;
	}

	public void setAppSalesTeamId(Integer appSalesTeamId) {
		this.appSalesTeamId = appSalesTeamId;
	}

	public String getAppDocPickupTimeString() {
		return appDocPickupTimeString;
	}

	public void setAppDocPickupTimeString(String appDocPickupTimeString) {
		this.appDocPickupTimeString = appDocPickupTimeString;
	}

	public Integer getAppProductTenureFlag() {
		return appProductTenureFlag;
	}

	public void setAppProductTenureFlag(Integer appProductTenureFlag) {
		this.appProductTenureFlag = appProductTenureFlag;
	}

	public Float getAppLoanInterestRateDiscount() {
		return appLoanInterestRateDiscount;
	}

	public void setAppLoanInterestRateDiscount(Float appLoanInterestRateDiscount) {
		this.appLoanInterestRateDiscount = appLoanInterestRateDiscount;
	}

	public Double getAppLoanProcessingFeeDiscount() {
		return appLoanProcessingFeeDiscount;
	}

	public void setAppLoanProcessingFeeDiscount(Double appLoanProcessingFeeDiscount) {
		this.appLoanProcessingFeeDiscount = appLoanProcessingFeeDiscount;
	}

	public Double getAppLoanEmiDiscount() {
		return appLoanEmiDiscount;
	}

	public void setAppLoanEmiDiscount(Double appLoanEmiDiscount) {
		this.appLoanEmiDiscount = appLoanEmiDiscount;
	}

	public Integer getAppPermanentDistrictId() {
		return appPermanentDistrictId;
	}

	public void setAppPermanentDistrictId(Integer appPermanentDistrictId) {
		this.appPermanentDistrictId = appPermanentDistrictId;
	}

	public Integer getAppCoapplicantDistrictId() {
		return appCoapplicantDistrictId;
	}

	public void setAppCoapplicantDistrictId(Integer appCoapplicantDistrictId) {
		this.appCoapplicantDistrictId = appCoapplicantDistrictId;
	}

	public String getAppOfficeAddress1() {
		return appOfficeAddress1;
	}

	public void setAppOfficeAddress1(String appOfficeAddress1) {
		this.appOfficeAddress1 = appOfficeAddress1;
	}

	public String getAppOfficeAddress2() {
		return appOfficeAddress2;
	}

	public void setAppOfficeAddress2(String appOfficeAddress2) {
		this.appOfficeAddress2 = appOfficeAddress2;
	}

	public Integer getAppOfficeCityId() {
		return appOfficeCityId;
	}

	public void setAppOfficeCityId(Integer appOfficeCityId) {
		this.appOfficeCityId = appOfficeCityId;
	}

	public Integer getAppOfficeBranchId() {
		return appOfficeBranchId;
	}

	public void setAppOfficeBranchId(Integer appOfficeBranchId) {
		this.appOfficeBranchId = appOfficeBranchId;
	}

	public Integer getAppOfficePincode() {
		return appOfficePincode;
	}

	public void setAppOfficePincode(Integer appOfficePincode) {
		this.appOfficePincode = appOfficePincode;
	}

	public String getAppOfficePhoneStdCode() {
		return appOfficePhoneStdCode;
	}

	public void setAppOfficePhoneStdCode(String appOfficePhoneStdCode) {
		this.appOfficePhoneStdCode = appOfficePhoneStdCode;
	}

	public Long getAppOfficePhoneNumber() {
		return appOfficePhoneNumber;
	}

	public void setAppOfficePhoneNumber(Long appOfficePhoneNumber) {
		this.appOfficePhoneNumber = appOfficePhoneNumber;
	}
	
	public Boolean getAppPanCardLater() {
		return appPanCardLater;
	}
	
	public void setAppPanCardLater(Boolean appPanCardLater) {
		this.appPanCardLater = appPanCardLater;
	}

	public Boolean isAppCoapplicantPanCardLater() {
		return appCoapplicantPanCardLater;
	}

	public void setAppCoapplicantPanCardLater(Boolean appCoapplicantPanCardLater) {
		this.appCoapplicantPanCardLater = appCoapplicantPanCardLater;
	}

	public Integer getAppMoratoriumPeriod() {
		return appMoratoriumPeriod;
	}

	public void setAppMoratoriumPeriod(Integer appMoratoriumPeriod) {
		this.appMoratoriumPeriod = appMoratoriumPeriod;
	}

	public Integer getAppAcademicProofId() {
		return appAcademicProofId;
	}

	public void setAppAcademicProofId(Integer appAcademicProofId) {
		this.appAcademicProofId = appAcademicProofId;
	}

	public String getAppAcademicProofName() {
		return appAcademicProofName;
	}

	public void setAppAcademicProofName(String appAcademicProofName) {
		this.appAcademicProofName = appAcademicProofName;
	}

	public Integer getAppAssignedLmsSalesUserId() {
		return appAssignedLmsSalesUserId;
	}

	public void setAppAssignedLmsSalesUserId(Integer appAssignedLmsSalesUserId) {
		this.appAssignedLmsSalesUserId = appAssignedLmsSalesUserId;
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

	public Integer getAppPreviousBranchId() {
		return appPreviousBranchId;
	}

	public void setAppPreviousBranchId(Integer appPreviousBranchId) {
		this.appPreviousBranchId = appPreviousBranchId;
	}
	
	public Float getAppEmiNmiRatio() {
		return appEmiNmiRatio;
	}

	public void setAppEmiNmiRatio(Float appEmiNmiRatio) {
		this.appEmiNmiRatio = appEmiNmiRatio;
	}

	public Integer getAppTotalCallAttempt() {
		return appTotalCallAttempt;
	}

	public void setAppTotalCallAttempt(Integer appTotalCallAttempt) {
		this.appTotalCallAttempt = appTotalCallAttempt;
	}

	public Integer getAppMobileDedup() {
		return appMobileDedup;
	}

	public void setAppMobileDedup(Integer appMobileDedup) {
		this.appMobileDedup = appMobileDedup;
	}

	public Date getAppDobDT() {
		return appDobDT;
	}

	public void setAppDobDT(Date appDobDT) {
		this.appDobDT = appDobDT;
	}

	public String getAppDob() {
		return appDob;
	}

	public void setAppDob(String appDob) {
		this.appDob = appDob;
	}

	public Date getAppDocPickupDateDT() {
		return appDocPickupDateDT;
	}

	public void setAppDocPickupDateDT(Date appDocPickupDateDT) {
		this.appDocPickupDateDT = appDocPickupDateDT;
	}

	public String getAppDocPickupDate() {
		return appDocPickupDate;
	}

	public void setAppDocPickupDate(String appDocPickupDate) {
		this.appDocPickupDate = appDocPickupDate;
	}

	public Integer getAppCircleId() {
		return appCircleId;
	}

	public void setAppCircleId(Integer appCircleId) {
		this.appCircleId = appCircleId;
	}

	public Integer getAppNetworkId() {
		return appNetworkId;
	}

	public void setAppNetworkId(Integer appNetworkId) {
		this.appNetworkId = appNetworkId;
	}

	public Integer getAppModuleId() {
		return appModuleId;
	}

	public void setAppModuleId(Integer appModuleId) {
		this.appModuleId = appModuleId;
	}

	public Integer getAppRegionId() {
		return appRegionId;
	}

	public void setAppRegionId(Integer appRegionId) {
		this.appRegionId = appRegionId;
	}

	public int getAppContactCenterLocation() {
		return appContactCenterLocation;
	}

	public void setAppContactCenterLocation(int appContactCenterLocation) {
		this.appContactCenterLocation = appContactCenterLocation;
	}

	public String getAppOtherIdNumber() {
		return appOtherIdNumber;
	}

	public void setAppOtherIdNumber(String appOtherIdNumber) {
		this.appOtherIdNumber = appOtherIdNumber;
	}

	public Integer getAppOtherId() {
		return appOtherId;
	}

	public void setAppOtherId(Integer appOtherId) {
		this.appOtherId = appOtherId;
	}

	public Integer getAppCibilScore() {
		return appCibilScore;
	}

	public void setAppCibilScore(Integer appCibilScore) {
		this.appCibilScore = appCibilScore;
	}
	
	public Integer getAppSubTypeId() {
		return appSubTypeId;
	}

	public void setAppSubTypeId(Integer appSubTypeId) {
		this.appSubTypeId = appSubTypeId;
	}

	public String getAppCoIdProof() {
		return appCoIdProof;
	}

	public void setAppCoIdProof(String appCoIdProof) {
		this.appCoIdProof = appCoIdProof;
	}

	public String getAppCoOtherIdNumber() {
		return appCoOtherIdNumber;
	}

	public void setAppCoOtherIdNumber(String appCoOtherIdNumber) {
		this.appCoOtherIdNumber = appCoOtherIdNumber;
	}
	
	public Integer getAppElTypeId() {
		return appElTypeId;
	}

	public void setAppElTypeId(Integer appElTypeId) {
		this.appElTypeId = appElTypeId;
	}

	public String getAppCustomerSignature() {
		return appCustomerSignature;
	}

	public void setAppCustomerSignature(String appCustomerSignature) {
		this.appCustomerSignature = appCustomerSignature;
	}

	public Integer getAppCRMLeadId() {
		return appCRMLeadId;
	}

	public void setAppCRMLeadId(Integer appCRMLeadId) {
		this.appCRMLeadId = appCRMLeadId;
	}
	public Integer getAppOfficeDistrictId() {
		return appOfficeDistrictId;
	}

	public void setAppOfficeDistrictId(Integer appOfficeDistrictId) {
		this.appOfficeDistrictId = appOfficeDistrictId;
	}

	public String getAppAbroadAddress1() {
		return appAbroadAddress1;
	}

	public void setAppAbroadAddress1(String appAbroadAddress1) {
		this.appAbroadAddress1 = appAbroadAddress1;
	}

	public String getAppAbroadAddress2() {
		return appAbroadAddress2;
	}

	public void setAppAbroadAddress2(String appAbroadAddress2) {
		this.appAbroadAddress2 = appAbroadAddress2;
	}

	public String getAppAbroadAddressLandMark() {
		return appAbroadAddressLandMark;
	}

	public void setAppAbroadAddressLandMark(String appAbroadAddressLandMark) {
		this.appAbroadAddressLandMark = appAbroadAddressLandMark;
	}

	public String getAppAbroadState() {
		return appAbroadState;
	}

	public void setAppAbroadState(String appAbroadState) {
		this.appAbroadState = appAbroadState;
	}

	public String getAppAbroadCity() {
		return appAbroadCity;
	}

	public void setAppAbroadCity(String appAbroadCity) {
		this.appAbroadCity = appAbroadCity;
	}

	public String getAppAbroadCountryId() {
		return appAbroadCountryId;
	}

	public void setAppAbroadCountryId(String appAbroadCountryId) {
		this.appAbroadCountryId = appAbroadCountryId;
	}
	
	public Integer getAppAbroadPincode() {
		return appAbroadPincode;
	}

	public void setAppAbroadPincode(Integer appAbroadPincode) {
		this.appAbroadPincode = appAbroadPincode;
	}

	public Double getAppNetAnnualIncome() {
		return appNetAnnualIncome;
	}

	public void setAppNetAnnualIncome(Double appNetAnnualIncome) {
		this.appNetAnnualIncome = appNetAnnualIncome;
	}

	public String getAppCoapplicantGender() {
		return appCoapplicantGender;
	}

	public void setAppCoapplicantGender(String appCoapplicantGender) {
		this.appCoapplicantGender = appCoapplicantGender;
	}

	public String getAppRequestData() {
		return appRequestData;
	}

	public void setAppRequestData(String appRequestData) {
		this.appRequestData = appRequestData;
	}

	public Integer getAppOTPAttemptCount() {
		return appOTPAttemptCount;
	}

	public void setAppOTPAttemptCount(Integer appOTPAttemptCount) {
		this.appOTPAttemptCount = appOTPAttemptCount;
	}

	public Integer getAppOTPVerifyType() {
		return appOTPVerifyType;
	}

	public void setAppOTPVerifyType(Integer appOTPVerifyType) {
		this.appOTPVerifyType = appOTPVerifyType;
	}

	public Integer getAppResTypeAtVerified() {
		return appResTypeAtVerified;
	}

	public void setAppResTypeAtVerified(Integer appResTypeAtVerified) {
		this.appResTypeAtVerified = appResTypeAtVerified;
	}
	
	public Date getAppEntryDate() {
		return appEntryDate;
	}

	public void setAppEntryDate(Date appEntryDate) {
		this.appEntryDate = appEntryDate;
	}

	public Integer getAppEmailAttemptCount() {
		return appEmailAttemptCount;
	}

	public void setAppEmailAttemptCount(Integer appEmailAttemptCount) {
		this.appEmailAttemptCount = appEmailAttemptCount;
	}
	
	public String getAppAipMailSendStatus() {
		return appAipMailSendStatus;
	}

	public void setAppAipMailSendStatus(String appAipMailSendStatus) {
		this.appAipMailSendStatus = appAipMailSendStatus;
	}
	
	public Integer getAppHaveAadhaarNumber() {
		return appHaveAadhaarNumber;
	}

	public void setAppHaveAadhaarNumber(Integer appHaveAadhaarNumber) {
		this.appHaveAadhaarNumber = appHaveAadhaarNumber;
	}

	public Integer getAppCoAppHaveAadhaarNumber() {
		return appCoAppHaveAadhaarNumber;
	}

	public void setAppCoAppHaveAadhaarNumber(Integer appCoAppHaveAadhaarNumber) {
		this.appCoAppHaveAadhaarNumber = appCoAppHaveAadhaarNumber;
	}

	public String getAppAlternateMobileNumber() {
		return appAlternateMobileNumber;
	}

	public void setAppAlternateMobileNumber(String appAlternateMobileNumber) {
		this.appAlternateMobileNumber = appAlternateMobileNumber;
	}

	public String getAppAltISDCode() {
		return appAltISDCode;
	}

	public void setAppAltISDCode(String appAltISDCode) {
		this.appAltISDCode = appAltISDCode;
	}

	public String getAppAltMobileVerificationCodeReceived() {
		return appAltMobileVerificationCodeReceived;
	}

	public void setAppAltMobileVerificationCodeReceived(String appAltMobileVerificationCodeReceived) {
		this.appAltMobileVerificationCodeReceived = appAltMobileVerificationCodeReceived;
	}

	public Integer getAppAltMobileVerificationCode() {
		return appAltMobileVerificationCode;
	}

	public void setAppAltMobileVerificationCode(Integer appAltMobileVerificationCode) {
		this.appAltMobileVerificationCode = appAltMobileVerificationCode;
	}

	public Integer getAppAltOtpMobAlertsCount() {
		return appAltOtpMobAlertsCount;
	}

	public void setAppAltOtpMobAlertsCount(Integer appAltOtpMobAlertsCount) {
		this.appAltOtpMobAlertsCount = appAltOtpMobAlertsCount;
	}

	public String getAppAltMobileVerified() {
		return appAltMobileVerified;
	}

	public void setAppAltMobileVerified(String appAltMobileVerified) {
		this.appAltMobileVerified = appAltMobileVerified;
	}

	public Boolean getAppCoapplicantPanCardLater() {
		return appCoapplicantPanCardLater;
	}

	public String getCrmLeadSource() {
		return crmLeadSource;
	}

	public void setCrmLeadSource(String crmLeadSource) {
		this.crmLeadSource = crmLeadSource;
	}

	public Integer getCrmLeadSourceId() {
		return crmLeadSourceId;
	}

	public void setCrmLeadSourceId(Integer crmLeadSourceId) {
		this.crmLeadSourceId = crmLeadSourceId;
	}

	public Integer getAppConsentId() {
		return appConsentId;
	}

	public void setAppConsentId(Integer appConsentId) {
		this.appConsentId = appConsentId;
	}

	public String getAppMobileNumberMask() {
		return appMobileNumberMask;
	}

	public void setAppMobileNumberMask(String appMobileNumberMask) {
		this.appMobileNumberMask = appMobileNumberMask;
	}
}
