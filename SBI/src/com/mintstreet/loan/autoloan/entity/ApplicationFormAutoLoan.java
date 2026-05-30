package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;
import java.math.BigInteger;

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

import java.util.Date;

@Entity
@Table(name = "RUPEEPOWER_OCAS_T_00063")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name = "G4", sequenceName = "RUPEEPOWER_OCAS_SEQ_00003", allocationSize = 1)

@NamedQueries({
		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByQuoteId", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appQuoteId =:appQuoteId "),
		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByAppReferenceId", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appReferenceId =:appReferenceId "),

		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByAppReferenceIdAndMobileNo", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' "
				+ "and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appMobileNo)=:appMobileNo"),
		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByAppReferenceIdAndMobileNoAlt", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' "
				+ "and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appAlternateMobileNumber)=:appAlternateMobileNumber"),
		
		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByMobileAndSmsOtp", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N'"
				+ "and a.appMobileVerificationCode =:appMobileVerificationCode and a.appMobileNo=:appMobileNo order by a.appEntryTime desc"),
		@NamedQuery(name = "ApplicationFormAutoLoan.getApplicationFormAutoLoanByMobileAndSmsOtpAlt", query = "Select a from ApplicationFormAutoLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N'"
				+ "and a.appAltMobileVerificationCode =:appAltMobileVerificationCode and a.appAlternateMobileNumber=:appAlternateMobileNumber order by a.appEntryTime desc")

})

public class ApplicationFormAutoLoan extends com.mintstreet.common.entity.Domain<BigInteger> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "G4")
	@Column(name = "APP_SEQ_ID")
	private Integer appSeqId;

	@Column(name = "APP_QUOTE_ID")
	private Integer appQuoteId;

	@Column(name = "APP_REFERENCE_ID")
	private String appReferenceId;

	@Column(name = "APP_INTERMEDIARY_ID")
	private Integer appIntermediaryId;

	@Column(name = "APP_PRODUCT_VARIANT_ID")
	private Integer appAutoLoanId;

	@Column(name = "APP_LOAN_STATUS_ID")
	private Integer appLoanStatusId;

	@Column(name = "APP_BANK_ID")
	private Integer appBankId;

	@Column(name = "APP_DATA_SOURCE_ID")
	private Integer appDataSourceId;

	@Column(name = "APP_FULFILLMENT_GROUP_ID")
	private Integer appFulfillmentGroupId;

	@Column(name = "APP_COMPANY_CATEGORY_ID")
	private Integer appCompanyCategoryId;

	@Column(name = "APP_LOAN_APPLIED_COUPON")
	private String apploanappliedCoupon;

	@Column(name = "APP_LOAN_MAX_AMOUNT")
	private Double appLoanMaxAmont;

	@Column(name = "APP_LOAN_TENURE")
	private Integer appLoanTenure;

	@Column(name = "APP_LOAN_ACCOUNT_TYPE")
	private Integer appLoanAccountType;

	@Column(name = "APP_LOAN_AMOUNT")
	private Double appLoanAmount;

	@Column(name = "APP_LOAN_INTEREST_RATE")
	private Float appLoanInterestRate;

	@Column(name = "APP_LOAN_INTEREST_RATE_TYPE")
	private Integer appLoanInterestRateType;

	@Column(name = "APP_LOAN_EMI")
	private Double appLoanEmi;

	@Column(name = "APP_LOAN_PROCESSING_FEE")
	private Double appLoanProcessingFee;

	@Column(name = "APP_LOAN_INTEREST_RATE_DISC")
	private Float appLoanInterestRateDiscount;

	@Column(name = "APP_LOAN_PROCESSING_FEE_DICS")
	private Double appLoanProcessingFeeDiscount;

	@Column(name = "APP_LOAN_EMI_DISC")
	private Double appLoanEmiDiscount;

	@Column(name = "APP_WORK_EMAIL")
	private String appWorkEmail;

	@Column(name = "APP_EMAIL_VERIFY_CODE")
	private String appEmailVerificationCode;
		
	@Column(name = "APP_EMAIL_VERIFIED")
	private String appEmailVerified;

	@Column(name = "APP_NON_OTP_EML_ALERTS_COUNT")
	private Integer appNonOtpEmailAlertsCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_NON_OTP_EML_ALT_LAST_TIME")
	private Date appNonOtpEmailAlertsLastTime;

	@Column(name = "APP_MOBILE_NO")
	private String appMobileNo;

	@Column(name = "APP_APPLYING_FROM")
	private int appApplyingFrom;

	@Column(name = "APP_ISD_CODE")
	private String appISDCode;

	@Column(name = "APP_MOBILE_VERIFY_CODE")
	private Integer appMobileVerificationCode;

	@Column(name = "APP_MOB_VERIFYCODE_REC")
	private String appMobileVerificationCodeReceived;

	@Column(name = "APP_MOBILE_VERIFIED")
	private String appMobileVerified;
	
	@Column(name = "APP_OTP_MOBILEL_ALERTS_COUNT")
	private Integer appOtpMobileAlertsCount;

	@Column(name = "APP_FIRST_NAME")
	private String appFirstName;

	@Column(name = "APP_MIDDLE_NAME")
	private String appMiddleName;

	@Column(name = "APP_LAST_NAME")
	private String appLastName;

	@Column(name = "APP_GENDER")
	private String appGender;

	@Temporal(TemporalType.DATE)
	@Column(name = "APP_DATE_OF_BIRTH")
	private Date appDobDT;

	@Transient
	private String appDob;

	@Column(name = "APP_QUALIFICATION_ID")
	private Integer appQualificationId;

	@Column(name = "APP_RESIDENCE_TYPE")
	private Integer appResidenceType;

	@Column(name = "APP_ADDRESS_1")
	private String appAddress1;

	@Column(name = "APP_ADDRESS_2")
	private String appAddress2;

	@Column(name = "APP_ADDRESS_LANDMARK")
	private String appAddressLandmark;

	@Column(name = "APP_STATE_ID")
	private Integer appStateId;

	@Column(name = "APP_CITY_ID")
	private Integer appCityId;

	@Column(name = "APP_DISTRICT_ID")
	private Integer appDistrictId;

	@Column(name = "APP_BRANCH_ID")
	private Integer appBranchId;

	@Column(name = "APP_PREV_BRANCH_ID")
	private Integer appPreviousBranchId;

	@Column(name = "APP_LOCALITY_ID")
	private Integer appLocalityId;

	@Column(name = "APP_SALES_TEAM_ID")
	private Integer appSalesTeamId;

	@Column(name = "APP_PINCODE")
	private Integer appPincode;

	@Column(name = "APP_HIGHEST_QUALIFICATION")
	private Integer appHighestQualification;

	@Column(name = "APP_RELAT_WITH_BANK")
	private Integer appRelationshipWithBank;

	@Column(name = "APP_SALARY_ACC_NO")
	private String appSalaryAccNo;

	@Column(name = "APP_LOAN_EMPLOYER_NAME")
	private String appLoanEmployerName;

	@Column(name = "APP_EMPLOYMENT_TYPE")
	private Integer appEmploymentType;

	@Column(name = "APP_DEALER_NAME")
	private String appLoanDealerName;

	@Column(name = "APP_DEALER_ID")
	private Integer appLoanDealerId;

	@Column(name = "APP_SALARY_BANK_ID")
	private Integer appSalaryBankId;

	@Column(name = "APP_OFFICE_ADDRESS_1")
	private String appOfficeAddress1;

	@Column(name = "APP_OFFICE_ADDRESS_2")
	private String appOfficeAddress2;

	@Column(name = "APP_OFFICE_STATE_ID")
	private Integer appOfficeStateId;

	@Column(name = "APP_OFFICE_CITY_ID")
	private Integer appOfficeCityId;

	@Column(name = "APP_OFFICE_BRANCH_ID")
	private Integer appOfficeBranchId;

	@Column(name = "APP_OFFICE_PINCODE")
	private Integer appOfficePincode;

	@Column(name = "APP_OFFICE_PHONE_STD_CODE")
	private String appOfficePhoneStdCode;

	@Column(name = "APP_OFFICE_PHONE_NUMBER")
	private Long appOfficePhoneNumber;

	@Column(name = "APP_OFFICE_DISTRICT")
	private Integer appOfficeDistrictId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_COMPANY_JOINING_DATE")
	private Date appCompanyJoiningDate;

	@Transient
	private Integer appCompanyJoiningMonth;

	@Transient
	private Integer appCompanyJoiningYear;

	@Column(name = "APP_WORK_EXPERIENCE_YEAR")
	private Integer appWorkExperienceYear;

	@Column(name = "APP_WORK_EXPERIENCE_MONTH")
	private Integer appWorkExperienceMonth;

	@Column(name = "APP_PAN_CARD_NO")
	private String appPanCardNo;

	@Column(name = "APP_PAN_CARD_VERIFIED")
	private String appPanCardVerified;

	@Column(name = "APP_PAN_CARD_LATER")
	private Boolean appPanCardLater;

	@Column(name = "APP_PAN_UPDATED_DATETIME")
	private Date appPanUpdatedDatetime;

	@Column(name = "APP_PAN_VERIFIED_DATETIME")
	private Date appPanVerifiedDatetime;

	@Column(name = "APP_EVER_DEFAULTED_CREDIT_CARD")
	private String appEverDefaultedCreditCard;

	@Column(name = "APP_IS_EXISTING_BANK_ID")
	private Integer appIsExistingBankId;

	@Column(name = "APP_EXISTING_RELATION_TYPE_ID")
	private String appExistingRelationTypeId;

	@Column(name = "APP_IMMOVABLE_PROPERTY")
	private Double appImmovableProperty;

	@Column(name = "APP_BANK_DEPOSIT")
	private Double appBankDeposit;

	@Column(name = "APP_NSC")
	private Double appNsc;

	@Column(name = "APP_PF_OR_PPF")
	private Double appPFOrPPF;

	@Column(name = "APP_GOLD_ORNAMENTS")
	private Double appGoldOrnaments;

	@Column(name = "APP_MUTUAL_ASSET")
	private Double appMutualAsset;

	@Column(name = "APP_EXISTING_TOT_LOAN_AMOUNT")
	private Double appExistingTotalLoanAmount;

	@Column(name = "APP_COAPP_FST_TYPE_ID")
	private Integer appCoapplicantTypeId_1;

	@Column(name = "APP_COAPP_FST_EMP_TYPE_ID")
	private Integer appCoapplicantEmployment_type_id_1;

	@Column(name = "APP_COAPP_FST_FIRST_NAME")
	private String appCoapplicantFirstName_1;

	@Column(name = "APP_COAPP_FST_MIDDLE_NAME")
	private String appCoapplicantMiddleName1;

	@Column(name = "APP_COAPP_FST_LAST_NAME")
	private String appCoapplicantLastName_1;

	@Column(name = "APP_COAPP_FST_GENDER")
	private String appCoapplicantGender_1;

	@Column(name = "APP_COAPP_FST_DATE_OF_BIRTH")
	private Date appCoapplicantDateofBirth1;

	@Column(name = "APP_COAPP_FST_ADDRESS_1")
	private String appCoapplicantAddress_1_1;

	@Column(name = "APP_COAPP_FST_ADDRESS_2")
	private String appCoapplicantAddress_2_1;

	@Column(name = "APP_COAPP_FST_LANDMARK")
	private String appCoapplicantLandmark_1;

	@Column(name = "APP_COAPP_FST_RES_TYPE_ID")
	private String appCoapplicantFirstResidanceId;

	@Column(name = "APP_COAPP_FST_IS_COBORROWER")
	private Integer appCoapplicantIscoborrower_1;

	@Column(name = "APP_COAPP_FST_STATE_ID")
	private Integer appCoapplicantState_id_1;

	@Column(name = "APP_COAPP_FST_CITY_ID")
	private Integer appCoapplicantCity_id_1;

	@Column(name = "APP_COAPP_FST_DISTRICT_ID")
	private Integer appCoapplicantDistrictId1;

	@Column(name = "APP_COAPP_FST_PINCODE")
	private Integer appCoapplicantPincode_1;

	@Column(name = "APP_COAPP_FST_PAN_CARD_NO")
	private String appCoapplicantPanCardNo_1;

	@Column(name = "APP_COAPP_FST_EMPLOYER_NAME")
	private String appCoapplicantEmployerName1;

	@Column(name = "APP_COAPP_FST_HIGHEST_QUALI")
	private Integer appCoapplicantHighestQualification;

	@Column(name = "APP_COAPP_FST_RELAT_WITH_BANK")
	private Integer appCoapplicantRelationShipWithBank;

	@Column(name = "APP_COAPP_FST_IMMOVABLE_PROP")
	private Double appCoapplicantImmovableProperty;

	@Column(name = "APP_COAPP_FST_BANK_DEPOSIT")
	private Double appCoapplicantBankDeposit;

	@Column(name = "APP_COAPP_FST_NSC")
	private Double appCoapplicantNsc;

	@Column(name = "APP_COAPP_FST_PF_OR_PPF")
	private Double appCoapplicantPFOrPPF;

	@Column(name = "APP_COAPP_FST_GOLD_ORNAMENTS")
	private Double appCoapplicantGoldOrnaments;

	@Column(name = "APP_COAPP_FST_MUTUAL_ASSET")
	private Double appCoapplicantMutualAsset;

	@Column(name = "APP_COAPP_FST_EXISTING_TOT_LA")
	private Double appCoapplicantExistingTotalLoanAmount;

	@Column(name = "APP_PERMANENT_ADDRESS1")
	private String appPermanentAddress1;

	@Column(name = "APP_PERMANENT_ADDRESS2")
	private String appPermanentAddress2;

	@Column(name = "APP_PERMANENT_ADDRESS_LANDMARK")
	private String appPermanentAddressLandMark;

	@Column(name = "APP_PERMANENT_STATE_ID")
	private Integer appPermanentStateId;

	@Column(name = "APP_PERMANENT_CITY_ID")
	private Integer appPermanentCityId;

	@Column(name = "APP_PERMANENT_DISTRICT_ID")
	private Integer appPermanentDistrictId;

	@Column(name = "APP_PERMANENT_PINCODE")
	private Integer appPermanentPincode;

	@Column(name = "APP_DOWNLOAD_PDF_FILE_NAME")
	private String appDownloadPdfFileName;

	@Column(name = "APP_DOC_PICK_UP_CHECK")
	private Integer appDocPickupCheck;

	@Column(name = "APP_PICKUP_ADDRESS_1")
	private String appPickupAddress1;

	@Column(name = "APP_PICKUP_ADDRESS_2")
	private String appPickupAddress2;

	@Column(name = "APP_PICKUP_STATE_ID")
	private Integer appPickupStateId;

	@Column(name = "APP_PICKUP_CITY_ID")
	private Integer appPickupCityId;

	@Column(name = "APP_PICKUP_DISTRICT_ID")
	private Integer appPickupDistrictId;

	@Column(name = "APP_PICKUP_PINCODE")
	private Integer appPickupPincode;

	@Column(name = "APP_DOC_PICKUP_TIME")
	private Date appDocPickupTime;

	@Column(name = "APP_DOC_PICKUP_TIME_STR")
	private String appDocPickupTimeString;

	@Temporal(TemporalType.DATE)
	@Column(name = "APP_DOC_PICKUP_DATE")
	private Date appDocPickupDateDT;

	@Transient
	private String appDocPickupDate;

	@Column(name = "APP_PHOTO_ID")
	private Integer appPhotoId;

	@Column(name = "APP_PHOTO_ID_NAME")
	private String appPhotoIdName;

	@Column(name = "APP_IDENTITY_PROOF_ID")
	private Integer appIdentityProofId;

	@Column(name = "APP_IDENTITY_PROOF_NAME")
	private String appIdentityProofName;

	@Column(name = "APP_RESIDENCE_PROOF_ID")
	private Integer appResidenceProofId;

	@Column(name = "APP_RESIDENCE_PROOF_NAME")
	private String appResidenceProofName;

	@Column(name = "APP_INCOME_PROOF_ID")
	private Integer appIncomeProofId;

	@Column(name = "APP_INCOME_PROOF_NAME")
	private String appIncomeProofName;

	@Column(name = "APP_EMPLOYMENT_PROOF_ID")
	private Integer appEmploymentProofId;

	@Column(name = "APP_EMPLOYMENT_PROOF_NAME")
	private String appEmploymentProofName;

	@Column(name = "APP_AUTO_DETAILS_PROOF_ID")
	private Integer appAutoDetailsProofId;

	@Column(name = "APP_AUTO_DETAILS_PROOF_NAME")
	private String appAutoDetailsProofName;

	@Column(name = "APP_ALERT_STATUS_TYPE")
	private Integer appAlertStatusType;

	@Column(name = "APP_AMENDED_LMS_USER_ID")
	private Integer appAmendedLmsUserId;

	@Column(name = "APP_ASS_LMS_SALES_USER_ID")
	private Integer appAssignedLmsSalesUserId;

	@Column(name = "APP_CREATED_LMS_USER_ID")
	private Integer appCreatedLmsUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_DOCS_ENTERED_TIME")
	private Date appDocsEnteredTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_ENTRY_TIME")
	private Date appEntryTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_FILLED_AT")
	private Date appFilledAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_LEAD_UPDATE_TIME")
	private Date appLeadUpdateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_SALES_ENTERED_TIME")
	private Date appSalesEnteredTime;

	@Column(name = "APP_OFFER_JSON_DATA")
	private String appOfferJsonData;

	@Column(name = "APP_ACTIVE")
	private String appActive;

	@Column(name = "APP_DELETED")
	private String appDeleted;

	@Column(name = "APP_clone_Premanent_Add")
	private boolean clonePermanentAddress;

	@Column(name = "APP_clone_Co_Applicant_Add")
	private boolean cloneCoapplicantAddress1;

	@Column(name = "APP_ANNUAL_INCOME")
	private Double appAnnualIncome;

	@Column(name = "APP_CO_APP_ANNUAL_INCOME")
	private Double appCoAppAnnualIncome;

	@Column(name = "APP_RSM_RESPONSE")
	private String appRSMResponse;

	@Column(name = "APP_RSM_STATUS")
	private Integer appRSMStatus;

	@Column(name = "APP_RSM_SCORE")
	private float appRSMScore;

	@Column(name = "APP_RSM_DECISION")
	private Integer appRSMdecision;

	@Column(name = "APP_RSM_GRADE")
	private Integer appRSMGrade;

	@Column(name = "APP_PRODUCT_TENURE_FLAG")
	private Integer appProductTenureFlag;

	@Column(name = "APP_NRI_ADDRESS_1")
	private String appNRIAddress1;

	@Column(name = "APP_NRI_ADDRESS_2")
	private String appNRIAddress2;

	@Column(name = "APP_NRI_ADDRESS_LANDMARK")
	private String appNRIAddressLandmark;

	@Column(name = "APP_COUNTRY_ID")
	private Integer appCountryId;

	@Column(name = "APP_NRI_STATE")
	private String appNRIState;

	@Column(name = "APP_NRI_CITY")
	private String appNRICity;

	@Column(name = "APP_NRI_ZIPCODE")
	private String appNRIZipcode;

	@Column(name = "APP_EMP_NRI_ADDRESS_1")
	private String appEMPNRIAddress1;

	@Column(name = "APP_EMP_NRI_ADDRESS_2")
	private String appEMPNRIAddress2;

	@Column(name = "APP_EMP_NRI_ADDRESS_LANDMARK")
	private String appEMPNRIAddressLandmark;

	@Column(name = "APP_EMP_COUNTRY_ID")
	private Integer appEMPCountryId;

	@Column(name = "APP_EMP_NRI_STATE")
	private String appEMPNRIState;

	@Column(name = "APP_EMP_NRI_CITY")
	private String appEMPNRICity;

	@Column(name = "APP_EMP_NRI_ZIPCODE")
	private String appEMPNRIZipcode;

	@Column(name = "APP_HOT_LEAD_CREATED_USER_ID")
	private Integer appHotLeadCreatedLmsUserId;

	@Column(name = "APP_EMI_NMI_RATIO")
	private Float appEmiNmiRatio;

	@Column(name = "APP_TOTAL_CALL_ATTEMPT")
	private Integer appTotalCallAttempt;

	@Column(name = "APP_MOBILE_DEDUP")
	private Integer appMobileDedup;
	@Column(name = "APP_CIRCLE_ID")
	private Integer appCircleId;

	@Column(name = "APP_NETWORK_ID")
	private Integer appNetworkId;

	@Column(name = "APP_MODULE_ID")
	private Integer appModuleId;

	@Column(name = "APP_REGION_ID")
	private Integer appRegionId;

	@Column(name = "APP_CONTACT_CENTER_LOCATION")
	private int appContactCenterLocation;

	@Column(name = "APP_OTP_VERIFY_TYPE")
	private int appOTPVerifyType;

	@Column(name = "APP_BUREAU_STATUS")
	private String appBureauStatus;

	@Column(name = "APP_BUREAU_REMARK")
	private String appBureauRemark;

	@Column(name = "APP_EKYC_REQUEST")
	private String appEkycRequest;

	@Column(name = "APP_EKYC_RESPONSE")
	private String appEkycResponse;

	@Column(name = "APP_OTHER_ID_NUMBER")
	private String appOtherIdNumber;

	@Column(name = "APP_OTHER_ID")
	private Integer appOtherId;

	@Column(name = "APP_CIBIL_SCORE")
	private Integer appCibilScore;

	@Column(name = "APP_APP_SUB_TYPE_ID")
	private Integer appSubTypeId;

	@Column(name = "APP_MOBILE_DEVICE_ID")
	private Integer appMobileDeviceId;

	@Column(name = "APP_SOCIAL_MEDIA_ID")
	private Integer appSocialMediaId;

	@Transient
	private Boolean appCallRSMService;

	@Lob
	@Column(name = "APP_CUSTOMER_SIGNATURE")
	private String appCustomerSignature;

	@Column(name = "APP_CRM_LEAD_ID")
	private Integer appCRMLeadId;

	@Column(name = "APP_CBS_RELATIONSHIP_ID")
	private Integer appCbsRelationShipId;

	@Column(name = "APP_OTP_ATTEMPT_COUNT")
	private Integer appOTPAttemptCount;

	@Column(name = "APP_HAVE_AADHAAR_NUMBER")
	private Integer appHaveAadhaarNumber;

	@Column(name = "APP_RES_TYPE_AT_VERIFIED")
	private Integer appResTypeAtVerified;

	@Temporal(TemporalType.DATE)
	@Column(name = "APP_ENTRY_DATE")
	private Date appEntryDate;

	@Column(name = "APP_EMAIL_ATTEMPT_COUNT")
	private Integer appEmailAttemptCount;

	@Column(name = "APP_AIP_MAIL_SEND_STATUS")
	private String appAipMailSendStatus;

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
		
	public Integer getAppAltOtpMobAlertsCount() {
		return appAltOtpMobAlertsCount;
	}

	public void setAppAltOtpMobAlertsCount(Integer appAltOtpMobAlertsCount) {
		this.appAltOtpMobAlertsCount = appAltOtpMobAlertsCount;
	}

	public Integer getAppAltMobileVerificationCode() {
		return appAltMobileVerificationCode;
	}

	public void setAppAltMobileVerificationCode(Integer appAltMobileVerificationCode) {
		this.appAltMobileVerificationCode = appAltMobileVerificationCode;
	}
	
	public String getAppOtherIdNumber() {
		return appOtherIdNumber;
	}

	public void setAppOtherIdNumber(String appOtherIdNumber) {
		this.appOtherIdNumber = appOtherIdNumber;
	}

  public static long getSerialversionuid() {
    return 1L;
  }

	public Double getAppCoAppAnnualIncome() {
		return appCoAppAnnualIncome;
	}

	public void setAppCoAppAnnualIncome(Double appCoAppAnnualIncome) {
		this.appCoAppAnnualIncome = appCoAppAnnualIncome;
	}

	public Double getAppAnnualIncome() {
		return appAnnualIncome;
	}

	public void setAppAnnualIncome(Double appAnnualIncome) {
		this.appAnnualIncome = appAnnualIncome;
	}

	public boolean isClonePermanentAddress() {
		return clonePermanentAddress;
	}

	public void setClonePermanentAddress(boolean clonePermanentAddress) {
		this.clonePermanentAddress = clonePermanentAddress;
	}

	public boolean isCloneCoapplicantAddress1() {
		return cloneCoapplicantAddress1;
	}

	public void setCloneCoapplicantAddress1(boolean cloneCoapplicantAddress1) {
		this.cloneCoapplicantAddress1 = cloneCoapplicantAddress1;
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

	public Integer getAppAutoLoanId() {
		return appAutoLoanId;
	}

	public void setAppAutoLoanId(Integer appAutoLoanId) {
		this.appAutoLoanId = appAutoLoanId;
	}

	public Integer getAppLoanStatusId() {
		return appLoanStatusId;
	}

	public void setAppLoanStatusId(Integer appLoanStatusId) {
		this.appLoanStatusId = appLoanStatusId;
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

	public String getApploanappliedCoupon() {
		return apploanappliedCoupon;
	}

	public void setApploanappliedCoupon(String apploanappliedCoupon) {
		this.apploanappliedCoupon = apploanappliedCoupon;
	}

	public Double getAppLoanMaxAmont() {
		return appLoanMaxAmont;
	}

	public void setAppLoanMaxAmont(Double appLoanMaxAmont) {
		this.appLoanMaxAmont = appLoanMaxAmont;
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

	public Double getAppLoanAmount() {
		return appLoanAmount;
	}

	public void setAppLoanAmount(Double appLoanAmount) {
		this.appLoanAmount = appLoanAmount;
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

	public Double getAppLoanEmi() {
		return appLoanEmi;
	}

	public void setAppLoanEmi(Double appLoanEmi) {
		this.appLoanEmi = appLoanEmi;
	}

	public Double getAppLoanProcessingFee() {
		return appLoanProcessingFee;
	}

	public void setAppLoanProcessingFee(Double appLoanProcessingFee) {
		this.appLoanProcessingFee = appLoanProcessingFee;
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

	public void setAppMobileVerificationCodeReceived(String appMobileVerificationCodeReceived) {
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

	public Integer getAppQualificationId() {
		return appQualificationId;
	}

	public void setAppQualificationId(Integer appQualificationId) {
		this.appQualificationId = appQualificationId;
	}

	public Integer getAppResidenceType() {
		return appResidenceType;
	}

	public void setAppResidenceType(Integer appResidenceType) {
		this.appResidenceType = appResidenceType;
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

	public Integer getAppHighestQualification() {
		return appHighestQualification;
	}

	public void setAppHighestQualification(Integer appHighestQualification) {
		this.appHighestQualification = appHighestQualification;
	}

	public Integer getAppRelationshipWithBank() {
		return appRelationshipWithBank;
	}

	public void setAppRelationshipWithBank(Integer appRelationshipWithBank) {
		this.appRelationshipWithBank = appRelationshipWithBank;
	}

	public String getAppSalaryAccNo() {
		return appSalaryAccNo;
	}

	public void setAppSalaryAccNo(String appSalaryAccNo) {
		this.appSalaryAccNo = appSalaryAccNo;
	}

	public String getAppLoanEmployerName() {
		return appLoanEmployerName;
	}

	public void setAppLoanEmployerName(String appLoanEmployerName) {
		this.appLoanEmployerName = appLoanEmployerName;
	}

	public Integer getAppEmploymentType() {
		return appEmploymentType;
	}

	public void setAppEmploymentType(Integer appEmploymentType) {
		this.appEmploymentType = appEmploymentType;
	}

	public String getAppLoanDealerName() {
		return appLoanDealerName;
	}

	public void setAppLoanDealerName(String appLoanDealerName) {
		this.appLoanDealerName = appLoanDealerName;
	}

	public Integer getAppSalaryBankId() {
		return appSalaryBankId;
	}

	public void setAppSalaryBankId(Integer appSalaryBankId) {
		this.appSalaryBankId = appSalaryBankId;
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

	public Integer getAppOfficeStateId() {
		return appOfficeStateId;
	}

	public void setAppOfficeStateId(Integer appOfficeStateId) {
		this.appOfficeStateId = appOfficeStateId;
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

	public Date getAppCompanyJoiningDate() {
		return appCompanyJoiningDate;
	}

	public void setAppCompanyJoiningDate(Date appCompanyJoiningDate) {
		this.appCompanyJoiningDate = appCompanyJoiningDate;
	}

	public Integer getAppCompanyJoiningMonth() {
		return appCompanyJoiningMonth;
	}

	public void setAppCompanyJoiningMonth(Integer appCompanyJoiningMonth) {
		this.appCompanyJoiningMonth = appCompanyJoiningMonth;
	}

	public Integer getAppCompanyJoiningYear() {
		return appCompanyJoiningYear;
	}

	public void setAppCompanyJoiningYear(Integer appCompanyJoiningYear) {
		this.appCompanyJoiningYear = appCompanyJoiningYear;
	}

	public Integer getAppWorkExperienceYear() {
		return appWorkExperienceYear;
	}

	public void setAppWorkExperienceYear(Integer appWorkExperienceYear) {
		this.appWorkExperienceYear = appWorkExperienceYear;
	}

	public Integer getAppWorkExperienceMonth() {
		return appWorkExperienceMonth;
	}

	public void setAppWorkExperienceMonth(Integer appWorkExperienceMonth) {
		this.appWorkExperienceMonth = appWorkExperienceMonth;
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

	public Date getAppPanUpdatedDatetime() {
		return appPanUpdatedDatetime;
	}

	public void setAppPanUpdatedDatetime(Date appPanUpdatedDatetime) {
		this.appPanUpdatedDatetime = appPanUpdatedDatetime;
	}

	public Date getAppPanVerifiedDatetime() {
		return appPanVerifiedDatetime;
	}

	public void setAppPanVerifiedDatetime(Date appPanVerifiedDatetime) {
		this.appPanVerifiedDatetime = appPanVerifiedDatetime;
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

	public Double getAppImmovableProperty() {
		return appImmovableProperty;
	}

	public void setAppImmovableProperty(Double appImmovableProperty) {
		this.appImmovableProperty = appImmovableProperty;
	}

	public Double getAppBankDeposit() {
		return appBankDeposit;
	}

	public void setAppBankDeposit(Double appBankDeposit) {
		this.appBankDeposit = appBankDeposit;
	}

	public Double getAppNsc() {
		return appNsc;
	}

	public void setAppNsc(Double appNsc) {
		this.appNsc = appNsc;
	}

	public Double getAppPFOrPPF() {
		return appPFOrPPF;
	}

	public void setAppPFOrPPF(Double appPFOrPPF) {
		this.appPFOrPPF = appPFOrPPF;
	}

	public Double getAppGoldOrnaments() {
		return appGoldOrnaments;
	}

	public void setAppGoldOrnaments(Double appGoldOrnaments) {
		this.appGoldOrnaments = appGoldOrnaments;
	}

	public Double getAppMutualAsset() {
		return appMutualAsset;
	}

	public void setAppMutualAsset(Double appMutualAsset) {
		this.appMutualAsset = appMutualAsset;
	}

	public Double getAppExistingTotalLoanAmount() {
		return appExistingTotalLoanAmount;
	}

	public void setAppExistingTotalLoanAmount(Double appExistingTotalLoanAmount) {
		this.appExistingTotalLoanAmount = appExistingTotalLoanAmount;
	}

	public Integer getAppCoapplicantTypeId_1() {
		return appCoapplicantTypeId_1;
	}

	public void setAppCoapplicantTypeId_1(Integer appCoapplicantTypeId_1) {
		this.appCoapplicantTypeId_1 = appCoapplicantTypeId_1;
	}

	public Integer getAppCoapplicantEmployment_type_id_1() {
		return appCoapplicantEmployment_type_id_1;
	}

	public void setAppCoapplicantEmployment_type_id_1(Integer appCoapplicantEmployment_type_id_1) {
		this.appCoapplicantEmployment_type_id_1 = appCoapplicantEmployment_type_id_1;
	}

	public String getAppCoapplicantFirstName_1() {
		return appCoapplicantFirstName_1;
	}

	public void setAppCoapplicantFirstName_1(String appCoapplicantFirstName_1) {
		this.appCoapplicantFirstName_1 = appCoapplicantFirstName_1;
	}

	public String getAppCoapplicantMiddleName1() {
		return appCoapplicantMiddleName1;
	}

	public void setAppCoapplicantMiddleName1(String appCoapplicantMiddleName1) {
		this.appCoapplicantMiddleName1 = appCoapplicantMiddleName1;
	}

	public String getAppCoapplicantLastName_1() {
		return appCoapplicantLastName_1;
	}

	public void setAppCoapplicantLastName_1(String appCoapplicantLastName_1) {
		this.appCoapplicantLastName_1 = appCoapplicantLastName_1;
	}

	public String getAppCoapplicantGender_1() {
		return appCoapplicantGender_1;
	}

	public void setAppCoapplicantGender_1(String appCoapplicantGender_1) {
		this.appCoapplicantGender_1 = appCoapplicantGender_1;
	}

	public Date getAppCoapplicantDateofBirth1() {
		return appCoapplicantDateofBirth1;
	}

	public void setAppCoapplicantDateofBirth1(Date appCoapplicantDateofBirth1) {
		this.appCoapplicantDateofBirth1 = appCoapplicantDateofBirth1;
	}

	public String getAppCoapplicantAddress_1_1() {
		return appCoapplicantAddress_1_1;
	}

	public void setAppCoapplicantAddress_1_1(String appCoapplicantAddress_1_1) {
		this.appCoapplicantAddress_1_1 = appCoapplicantAddress_1_1;
	}

	public String getAppCoapplicantAddress_2_1() {
		return appCoapplicantAddress_2_1;
	}

	public void setAppCoapplicantAddress_2_1(String appCoapplicantAddress_2_1) {
		this.appCoapplicantAddress_2_1 = appCoapplicantAddress_2_1;
	}

	public String getAppCoapplicantLandmark_1() {
		return appCoapplicantLandmark_1;
	}

	public void setAppCoapplicantLandmark_1(String appCoapplicantLandmark_1) {
		this.appCoapplicantLandmark_1 = appCoapplicantLandmark_1;
	}

	public String getAppCoapplicantFirstResidanceId() {
		return appCoapplicantFirstResidanceId;
	}

	public void setAppCoapplicantFirstResidanceId(String appCoapplicantFirstResidanceId) {
		this.appCoapplicantFirstResidanceId = appCoapplicantFirstResidanceId;
	}

	public Integer getAppCoapplicantIscoborrower_1() {
		return appCoapplicantIscoborrower_1;
	}

	public void setAppCoapplicantIscoborrower_1(Integer appCoapplicantIscoborrower_1) {
		this.appCoapplicantIscoborrower_1 = appCoapplicantIscoborrower_1;
	}

	public Integer getAppCoapplicantState_id_1() {
		return appCoapplicantState_id_1;
	}

	public void setAppCoapplicantState_id_1(Integer appCoapplicantState_id_1) {
		this.appCoapplicantState_id_1 = appCoapplicantState_id_1;
	}

	public Integer getAppCoapplicantCity_id_1() {
		return appCoapplicantCity_id_1;
	}

	public void setAppCoapplicantCity_id_1(Integer appCoapplicantCity_id_1) {
		this.appCoapplicantCity_id_1 = appCoapplicantCity_id_1;
	}

	public Integer getAppCoapplicantDistrictId1() {
		return appCoapplicantDistrictId1;
	}

	public void setAppCoapplicantDistrictId1(Integer appCoapplicantDistrictId1) {
		this.appCoapplicantDistrictId1 = appCoapplicantDistrictId1;
	}

	public Integer getAppCoapplicantPincode_1() {
		return appCoapplicantPincode_1;
	}

	public void setAppCoapplicantPincode_1(Integer appCoapplicantPincode_1) {
		this.appCoapplicantPincode_1 = appCoapplicantPincode_1;
	}

	public String getAppCoapplicantPanCardNo_1() {
		return appCoapplicantPanCardNo_1;
	}

	public void setAppCoapplicantPanCardNo_1(String appCoapplicantPanCardNo_1) {
		this.appCoapplicantPanCardNo_1 = appCoapplicantPanCardNo_1;
	}

	public String getAppCoapplicantEmployerName1() {
		return appCoapplicantEmployerName1;
	}

	public void setAppCoapplicantEmployerName1(String appCoapplicantEmployerName1) {
		this.appCoapplicantEmployerName1 = appCoapplicantEmployerName1;
	}

	public Integer getAppCoapplicantHighestQualification() {
		return appCoapplicantHighestQualification;
	}

	public void setAppCoapplicantHighestQualification(Integer appCoapplicantHighestQualification) {
		this.appCoapplicantHighestQualification = appCoapplicantHighestQualification;
	}

	public Integer getAppCoapplicantRelationShipWithBank() {
		return appCoapplicantRelationShipWithBank;
	}

	public void setAppCoapplicantRelationShipWithBank(Integer appCoapplicantRelationShipWithBank) {
		this.appCoapplicantRelationShipWithBank = appCoapplicantRelationShipWithBank;
	}

	public Double getAppCoapplicantImmovableProperty() {
		return appCoapplicantImmovableProperty;
	}

	public void setAppCoapplicantImmovableProperty(Double appCoapplicantImmovableProperty) {
		this.appCoapplicantImmovableProperty = appCoapplicantImmovableProperty;
	}

	public Double getAppCoapplicantBankDeposit() {
		return appCoapplicantBankDeposit;
	}

	public void setAppCoapplicantBankDeposit(Double appCoapplicantBankDeposit) {
		this.appCoapplicantBankDeposit = appCoapplicantBankDeposit;
	}

	public Double getAppCoapplicantNsc() {
		return appCoapplicantNsc;
	}

	public void setAppCoapplicantNsc(Double appCoapplicantNsc) {
		this.appCoapplicantNsc = appCoapplicantNsc;
	}

	public Double getAppCoapplicantPFOrPPF() {
		return appCoapplicantPFOrPPF;
	}

	public void setAppCoapplicantPFOrPPF(Double appCoapplicantPFOrPPF) {
		this.appCoapplicantPFOrPPF = appCoapplicantPFOrPPF;
	}

	public Double getAppCoapplicantGoldOrnaments() {
		return appCoapplicantGoldOrnaments;
	}

	public void setAppCoapplicantGoldOrnaments(Double appCoapplicantGoldOrnaments) {
		this.appCoapplicantGoldOrnaments = appCoapplicantGoldOrnaments;
	}

	public Double getAppCoapplicantMutualAsset() {
		return appCoapplicantMutualAsset;
	}

	public void setAppCoapplicantMutualAsset(Double appCoapplicantMutualAsset) {
		this.appCoapplicantMutualAsset = appCoapplicantMutualAsset;
	}

	public Double getAppCoapplicantExistingTotalLoanAmount() {
		return appCoapplicantExistingTotalLoanAmount;
	}

	public void setAppCoapplicantExistingTotalLoanAmount(Double appCoapplicantExistingTotalLoanAmount) {
		this.appCoapplicantExistingTotalLoanAmount = appCoapplicantExistingTotalLoanAmount;
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

	public Integer getAppPermanentDistrictId() {
		return appPermanentDistrictId;
	}

	public void setAppPermanentDistrictId(Integer appPermanentDistrictId) {
		this.appPermanentDistrictId = appPermanentDistrictId;
	}

	public Integer getAppPermanentPincode() {
		return appPermanentPincode;
	}

	public void setAppPermanentPincode(Integer appPermanentPincode) {
		this.appPermanentPincode = appPermanentPincode;
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

	public Date getAppEntryTime() {
		return appEntryTime;
	}

	public void setAppEntryTime(Date appEntryTime) {
		this.appEntryTime = appEntryTime;
	}

	public Date getAppFilledAt() {
		return appFilledAt;
	}

	public void setAppFilledAt(Date appFilledAt) {
		this.appFilledAt = appFilledAt;
	}

	public Integer getAppHotLeadCreatedLmsUserId() {
		return appHotLeadCreatedLmsUserId;
	}

	public void setAppHotLeadCreatedLmsUserId(Integer appHotLeadCreatedLmsUserId) {
		this.appHotLeadCreatedLmsUserId = appHotLeadCreatedLmsUserId;
	}

	public Date getAppLeadUpdateTime() {
		return appLeadUpdateTime;
	}

	public void setAppLeadUpdateTime(Date appLeadUpdateTime) {
		this.appLeadUpdateTime = appLeadUpdateTime;
	}

	public Date getAppSalesEnteredTime() {
		return appSalesEnteredTime;
	}

	public void setAppSalesEnteredTime(Date appSalesEnteredTime) {
		this.appSalesEnteredTime = appSalesEnteredTime;
	}

	public String getAppOfferJsonData() {
		return appOfferJsonData;
	}

	public void setAppOfferJsonData(String appOfferJsonData) {
		this.appOfferJsonData = appOfferJsonData;
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

	public Integer getAppLocalityId() {
		return appLocalityId;
	}

	public void setAppLocalityId(Integer appLocalityId) {
		this.appLocalityId = appLocalityId;
	}

	public String getAppRSMResponse() {
		return appRSMResponse;
	}

	public void setAppRSMResponse(String appRSMResponse) {
		this.appRSMResponse = appRSMResponse;
	}

	public Integer getAppRSMStatus() {
		return appRSMStatus;
	}

	public void setAppRSMStatus(Integer appRSMStatus) {
		this.appRSMStatus = appRSMStatus;
	}

	public float getAppRSMScore() {
		return appRSMScore;
	}

	public void setAppRSMScore(float appRSMScore) {
		this.appRSMScore = appRSMScore;
	}

	public Integer getAppRSMdecision() {
		return appRSMdecision;
	}

	public void setAppRSMdecision(Integer appRSMdecision) {
		this.appRSMdecision = appRSMdecision;
	}

	public Integer getAppRSMGrade() {
		return appRSMGrade;
	}

	public void setAppRSMGrade(Integer appRSMGrade) {
		this.appRSMGrade = appRSMGrade;
	}

	public Integer getAppDistrictId() {
		return appDistrictId;
	}

	public void setAppDistrictId(Integer appDistrictId) {
		this.appDistrictId = appDistrictId;
	}

	public Integer getAppLoanDealerId() {
		return appLoanDealerId;
	}

	public void setAppLoanDealerId(Integer appLoanDealerId) {
		this.appLoanDealerId = appLoanDealerId;
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

	public String getAppNRIAddress1() {
		return appNRIAddress1;
	}

	public void setAppNRIAddress1(String appNRIAddress1) {
		this.appNRIAddress1 = appNRIAddress1;
	}

	public String getAppNRIAddress2() {
		return appNRIAddress2;
	}

	public void setAppNRIAddress2(String appNRIAddress2) {
		this.appNRIAddress2 = appNRIAddress2;
	}

	public String getAppNRIAddressLandmark() {
		return appNRIAddressLandmark;
	}

	public void setAppNRIAddressLandmark(String appNRIAddressLandmark) {
		this.appNRIAddressLandmark = appNRIAddressLandmark;
	}

	public Integer getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(Integer appCountryId) {
		this.appCountryId = appCountryId;
	}

	public String getAppNRIState() {
		return appNRIState;
	}

	public void setAppNRIState(String appNRIState) {
		this.appNRIState = appNRIState;
	}

	public String getAppNRICity() {
		return appNRICity;
	}

	public void setAppNRICity(String appNRICity) {
		this.appNRICity = appNRICity;
	}

	public String getAppNRIZipcode() {
		return appNRIZipcode;
	}

	public void setAppNRIZipcode(String appNRIZipcode) {
		this.appNRIZipcode = appNRIZipcode;
	}

	public String getAppEMPNRIAddress1() {
		return appEMPNRIAddress1;
	}

	public void setAppEMPNRIAddress1(String appEMPNRIAddress1) {
		this.appEMPNRIAddress1 = appEMPNRIAddress1;
	}

	public String getAppEMPNRIAddress2() {
		return appEMPNRIAddress2;
	}

	public void setAppEMPNRIAddress2(String appEMPNRIAddress2) {
		this.appEMPNRIAddress2 = appEMPNRIAddress2;
	}

	public String getAppEMPNRIAddressLandmark() {
		return appEMPNRIAddressLandmark;
	}

	public void setAppEMPNRIAddressLandmark(String appEMPNRIAddressLandmark) {
		this.appEMPNRIAddressLandmark = appEMPNRIAddressLandmark;
	}

	public Integer getAppEMPCountryId() {
		return appEMPCountryId;
	}

	public void setAppEMPCountryId(Integer appEMPCountryId) {
		this.appEMPCountryId = appEMPCountryId;
	}

	public String getAppEMPNRIState() {
		return appEMPNRIState;
	}

	public void setAppEMPNRIState(String appEMPNRIState) {
		this.appEMPNRIState = appEMPNRIState;
	}

	public String getAppEMPNRICity() {
		return appEMPNRICity;
	}

	public void setAppEMPNRICity(String appEMPNRICity) {
		this.appEMPNRICity = appEMPNRICity;
	}

	public String getAppEMPNRIZipcode() {
		return appEMPNRIZipcode;
	}

	public void setAppEMPNRIZipcode(String appEMPNRIZipcode) {
		this.appEMPNRIZipcode = appEMPNRIZipcode;
	}

	public Integer getAppOfficeDistrictId() {
		return appOfficeDistrictId;
	}

	public Boolean getAppPanCardLater() {
		return appPanCardLater;
	}

	public void setAppPanCardLater(Boolean appPanCardLater) {
		this.appPanCardLater = appPanCardLater;
	}

	public void setAppOfficeDistrictId(Integer appOfficeDistrictId) {
		this.appOfficeDistrictId = appOfficeDistrictId;
	}

	public String getAppISDCode() {
		return appISDCode;
	}

	public void setAppISDCode(String appISDCode) {
		this.appISDCode = appISDCode;
	}

	public int getAppApplyingFrom() {
		return appApplyingFrom;
	}

	public void setAppApplyingFrom(int appApplyingFrom) {
		this.appApplyingFrom = appApplyingFrom;
	}

	public Integer getAppAutoDetailsProofId() {
		return appAutoDetailsProofId;
	}

	public void setAppAutoDetailsProofId(Integer appAutoDetailsProofId) {
		this.appAutoDetailsProofId = appAutoDetailsProofId;
	}

	public String getAppAutoDetailsProofName() {
		return appAutoDetailsProofName;
	}

	public void setAppAutoDetailsProofName(String appAutoDetailsProofName) {
		this.appAutoDetailsProofName = appAutoDetailsProofName;
	}

	public Integer getAppAssignedLmsSalesUserId() {
		return appAssignedLmsSalesUserId;
	}

	public void setAppAssignedLmsSalesUserId(Integer appAssignedLmsSalesUserId) {
		this.appAssignedLmsSalesUserId = appAssignedLmsSalesUserId;
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

	public int getAppOTPVerifyType() {
		return appOTPVerifyType;
	}

	public void setAppOTPVerifyType(int appOTPVerifyType) {
		this.appOTPVerifyType = appOTPVerifyType;
	}

	public String getAppEmailVerificationCode() {
		return appEmailVerificationCode;
	}

	public void setAppEmailVerificationCode(String appEmailVerificationCode) {
		this.appEmailVerificationCode = appEmailVerificationCode;
	}

	public String getAppBureauStatus() {
		return appBureauStatus;
	}

	public void setAppBureauStatus(String appBureauStatus) {
		this.appBureauStatus = appBureauStatus;
	}

	public String getAppBureauRemark() {
		return appBureauRemark;
	}

	public void setAppBureauRemark(String appBureauRemark) {
		this.appBureauRemark = appBureauRemark;
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

	public Boolean getAppCallRSMService() {
		return appCallRSMService;
	}

	public void setAppCallRSMService(Boolean appCallRSMService) {
		this.appCallRSMService = appCallRSMService;
	}

	public Integer getAppMobileDeviceId() {
		return appMobileDeviceId;
	}

	public void setAppMobileDeviceId(Integer appMobileDeviceId) {
		this.appMobileDeviceId = appMobileDeviceId;
	}

	public Integer getAppSocialMediaId() {
		return appSocialMediaId;
	}

	public void setAppSocialMediaId(Integer appSocialMediaId) {
		this.appSocialMediaId = appSocialMediaId;
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

	public Integer getAppCbsRelationShipId() {
		return appCbsRelationShipId;
	}

	public void setAppCbsRelationShipId(Integer appCbsRelationShipId) {
		this.appCbsRelationShipId = appCbsRelationShipId;
	}

	public Integer getAppOTPAttemptCount() {
		return appOTPAttemptCount;
	}

	public void setAppOTPAttemptCount(Integer appOTPAttemptCount) {
		this.appOTPAttemptCount = appOTPAttemptCount;
	}

	public Integer getAppResTypeAtVerified() {
		return appResTypeAtVerified;
	}

	public void setAppResTypeAtVerified(Integer appResTypeAtVerified) {
		this.appResTypeAtVerified = appResTypeAtVerified;
	}

	public Integer getAppHaveAadhaarNumber() {
		return appHaveAadhaarNumber;
	}

	public void setAppHaveAadhaarNumber(Integer appHaveAadhaarNumber) {
		this.appHaveAadhaarNumber = appHaveAadhaarNumber;
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
	
	public String getAppAltMobileVerified() {
		return appAltMobileVerified;
	}

	public void setAppAltMobileVerified(String appAltMobileVerified) {
		this.appAltMobileVerified = appAltMobileVerified;
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
