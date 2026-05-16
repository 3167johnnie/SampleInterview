package com.mintstreet.loan.homeloan.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "RUPEEPOWER_OCAS_T_00195")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name = "G2", sequenceName = "RUPEEPOWER_OCAS_SEQ_00063", allocationSize = 1)

@SuppressWarnings("rawtypes")
@NamedQueries({
		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByQuoteId", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appQuoteId =:appQuoteId "),

		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByAppReferenceId", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appReferenceId =:appReferenceId "),
		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByAppReferenceIdAndMobileNo", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' "
				+ " and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appMobileNo)=:appMobileNo  "),
		
		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByAppReferenceIdAndMobileNoAlt", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' "
				+ " and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appAlternateMobileNumber)=:appAlternateMobileNumber  "),
	
		
		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByMobileAndSmsOtp", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N'"
				+ " and a.appMobileVerificationCode =:appMobileVerificationCode and a.appMobileNo=:appMobileNo order by a.appEntryTime desc"),

		@NamedQuery(name = "ApplicationFormHomeLoan.getApplicationFormHomeLoanByMobileAndSmsOtpAlt", query = "Select a from ApplicationFormHomeLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N'"
				+ " and a.appAltMobileVerificationCode =:appAltMobileVerificationCode and a.appAlternateMobileNumber=:appAlternateMobileNumber order by a.appEntryTime desc")
	
		
})

public class ApplicationFormHomeLoan extends com.mintstreet.common.entity.Domain<java.lang.Integer> implements Serializable {
	private static final long serialVersionUID = 5028013487734290815L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "G2")
	@Column(name = "APP_SEQ_ID")
	private Integer appSeqId;

	@Column(name = "APP_QUOTE_ID")
	private Integer appQuoteId;

	@Column(name = "APP_REFERENCE_ID")
	private String appReferenceId;

	@Column(name = "APP_INTERMEDIARY_ID")
	private Integer appIntermediaryId;

	@Column(name = "APP_PRODUCT_VARIANT_ID")
	private Integer appHomeLoanId;

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

	@Column(name = "APP_LOAN_AMOUNT")
	private Double appLoanAmount;

	

	

	@Column(name = "APP_LOAN_TENURE")
	private Integer appLoanTenure;

	@Column(name = "APP_LOAN_INTEREST_RATE_TYPE")
	private Integer appLoanInterestRateType;

	@Column(name = "APP_PERMANENT_ADD_LAND_MARK")
	private String appPermanentAddressLandMark;

	@Column(name = "app_permanent_address1")
	private String appPermanentAddress1;

	@Column(name = "app_permanent_address2")
	private String appPermanentAddress2;

	@Column(name = "app_permanent_state")
	private Integer appPermanentStateId;

	@Column(name = "app_permanent_city")
	private Integer appPermanentCityId;

	@Column(name = "app_permanent_district_id")
	private Integer appPermanentDistrictId;

	@Column(name = "app_permanent_pincode")
	private Integer appPermanentPincode;

	@Column(name = "app_clone_permanent_address")
	private boolean clonePermanentAddress;

	@Column(name = "APP_LOAN_INTEREST_RATE")
	private Float appLoanInterestRate;

	@Column(name = "APP_LOAN_PROCESSING_FEE")
	private Double appLoanProcessingFee;

	@Column(name = "APP_LOAN_EMI")
	private Double appLoanEmi;

	@Column(name = "APP_LOAN_MORATORIUM_PERIOD")
	private Integer appLoanMoratoriumPeriod;

	@Column(name = "APP_LOAN_INTEREST_RATE_DISC")
	private Float appLoanInterestRateDiscount;

	@Column(name = "APP_LOAN_PROCESSING_FEE_DICS")
	private Double appLoanProcessingFeeDiscount;

	@Column(name = "APP_LOAN_EMI_DISC")
	private Double appLoanEmiDiscount;

	

	@Column(name = "APP_LOAN_ACCOUNT_TYPE")
	private Integer appLoanAccountType;

	@Column(name = "APP_LOAN_STATUS_ID")
	private Integer appLoanStatusId;

	@Column(name = "APP_WORK_EMAIL")
	private String appWorkEmail;

	

	@Column(name = "APP_EMAIL_VERIFIED")
	private String appEmailVerified;

	@Column(name = "APP_NON_OTP_EML_ALERTS_COUNT")
	private Integer appNonOtpEmailAlertsCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_NON_OTP_EML_ALERTS_TIME")
	private Date appNonOtpEmailAlertsLastTime;

	@Column(name = "APP_EMAIL_VERIFY_CODE")
	private String appEmailVerificationCode;

	

	@Column(name = "APP_MOBILE_NO")
	private String appMobileNo;

	@Column(name = "APP_APPLYING_FROM")
	private int appApplyingFrom;

	@Column(name = "APP_ISD_CODE")
	private String appISDCode;

	

	@Column(name = "APP_MOBILE_VERIFIED")
	private String appMobileVerified;

	@Column(name = "APP_MOBILE_VERIFY_CODE")
	private Integer appMobileVerificationCode;

	

	@Column(name = "APP_MOB_VERIFYCODE_REC")
	private String appMobileVerificationCodeReceived;

	

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

	@Column(name = "APP_STATE_ID")
	private Integer appStateId;

	@Column(name = "APP_ADDRESS_1")
	private String appAddress1;

	@Column(name = "APP_ADDRESS_2")
	private String appAddress2;

	@Column(name = "APP_ADDRESS_LANDMARK")
	private String appAddressLandmark;

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

	@Column(name = "APP_PROPERTY_ADDRESS1")
	private String appPropertyAddress1;

	@Column(name = "APP_PROPERTY_ADDRESS2")
	private String appPropertyAddress2;

	@Column(name = "APP_PROPERTY_ADDRESS_LANDMARK")
	private String appPropertyAddressLandmark;

	@Column(name = "APP_PROPERTY_STATE_ID")
	private Integer appPropertyStateId;

	@Column(name = "APP_PROPERTY_CITY_ID")
	private Integer appPropertyCityId;

	@Column(name = "APP_PROPERTY_DISTRICT_ID")
	private Integer appPropertyDistrictId;

	@Column(name = "APP_PROPERTY_PINCODE")
	private Integer appPropertyPincode;

	

	@Column(name = "APP_LOAN_EMPLOYER_NAME")
	private String appLoanEmployerName;

	@Column(name = "APP_EMPLOYMENT_TYPE")
	private Integer appEmploymentType;

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

	@Column(name = "APP_OFFICE_DISTRICT_ID")
	private Integer appOfficeDistrictId;

	@Column(name = "APP_OFFICE_PINCODE")
	private Integer appOfficePincode;

	@Column(name = "APP_OFFICE_PHONE_STD_CODE")
	private String appOfficePhoneStdCode;

	@Column(name = "APP_OFFICE_PHONE_NUMBER")
	private Long appOfficePhoneNumber;

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

	@Column(name = "APP_RESIDENCE_TYPE")
	private Integer appResidenceType;

	@Column(name = "APP_EVER_DEFAULTED_CREDIT_CARD")
	private String appEverDefaultedCreditCard;

	@Column(name = "APP_IS_EXISTING_BANK_ID")
	private Integer appIsExistingBankId;

	@Column(name = "APP_EXISTING_RELATION_TYPE_ID")
	private String appExistingRelationTypeId;

	@Column(name = "APP_COAPP_TYPE_ID_1")
	private Integer appCoapplicantTypeId_1;

	@Column(name = "APP_COAPP_FIRST_NAME_1")
	private String appCoapplicantFirstName_1;

	@Column(name = "APP_COAPP_MIDDLE_NAME_1")
	private String appCoapplicantMiddleName1;

	@Column(name = "APP_COAPP_LAST_NAME_1")
	private String appCoapplicantLastName_1;

	@Column(name = "APP_COAPP_GENDER_1")
	private String appCoapplicantGender_1;

	@Column(name = "APP_COAPP_DATE_OF_BIRTH_1")
	private Date appCoapplicantDateofBirth1;

	@Column(name = "APP_COAPP_ADDRESS_1_1")
	private String appCoapplicantAddress_1_1;

	@Column(name = "APP_COAPP_ADDRESS_1_2")
	private String appCoapplicantAddress_1_2;

	@Column(name = "APP_COAPP_LANDMARK_1")
	private String appCoapplicantLandmark_1;

	@Column(name = "APP_COAPP_STATE_ID_1")
	private Integer appCoapplicantState_id_1;

	@Column(name = "APP_COAPP_CITY_ID_1")
	private Integer appCoapplicantCity_id_1;

	@Column(name = "APP_COAPP_DISTRICT_ID1")
	private Integer appCoapplicantDistrictId1;

	@Column(name = "APP_COAPP_PINCODE_1")
	private Integer appCoapplicantPincode_1;

	@Column(name = "APP_COAPP_IS_COBORROWER_1")
	private Integer appCoapplicantIscoborrower_1;

	@Column(name = "APP_COAPP_EMPLOYER_NAME_1")
	private String appCoapplicantEmployerName1;

	@Column(name = "APP_COAPP_EMPLOYMENT_TYPE_ID_1")
	private Integer appCoapplicantEmployment_type_id_1;

	@Column(name = "APP_COAPP_PAN_CARD_NO_1")
	private String appCoapplicantPanCardNo_1;

	@Column(name = "APP_COAPP_TYPE_ID_2")
	private Integer appCoapplicantTypeId_2;

	@Column(name = "APP_COAPP_FIRST_NAME_2")
	private String appCoapplicantFirstName_2;

	@Column(name = "APP_COAPP_MIDDLE_NAME_2")
	private String appCoapplicantMiddleName2;

	@Column(name = "APP_COAPP_LAST_NAME_2")
	private String appCoapplicantLastName_2;

	@Column(name = "APP_COAPP_GENDER_2")
	private String appCoapplicantGender_2;

	@Column(name = "APP_COAPP_DATE_OF_BIRTH_2")
	private Date appCoapplicantDateofBirth2;

	@Column(name = "APP_COAPP_ADDRESS_2_1")
	private String appCoapplicantAddress_2_1;

	@Column(name = "APP_COAPP_ADDRESS_2_2")
	private String appCoapplicantAddress_2_2;

	@Column(name = "APP_COAPP_LANDMARK_2")
	private String appCoapplicantLandmark_2;

	@Column(name = "APP_COAPP_STATE_ID_2")
	private Integer appCoapplicantState_id_2;

	@Column(name = "APP_COAPP_CITY_ID_2")
	private Integer appCoapplicantCity_id_2;

	@Column(name = "APP_COAPP_DISTRICT_ID2")
	private Integer appCoapplicantDistrictId2;

	@Column(name = "APP_COAPP_PINCODE_2")
	private Integer appCoapplicantPincode_2;

	@Column(name = "APP_COAPP_IS_COBORROWER_2")
	private Integer appCoapplicantIscoborrower_2;

	@Column(name = "APP_COAPP_EMPLOYER_NAME_2")
	private String appCoapplicantEmployerName2;

	@Column(name = "APP_COAPP_EMPLOYMENT_TYPE_ID_2")
	private Integer appCoapplicantEmployment_type_id_2;

	@Column(name = "APP_COAPP_PAN_CARD_NO_2")
	private String appCoapplicantPanCardNo_2;

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

	@Temporal(TemporalType.DATE)
	@Column(name = "APP_DOC_PICKUP_DATE")
	private Date appDocPickupDateDT;

	@Transient
	private String appDocPickupDate;

	@Column(name = "APP_DOC_PICKUP_TIME")
	private Date appDocPickupTime;

	@Column(name = "APP_DOC_PICKUP_TIME_STR")
	private String appDocPickupTimeString;

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

	

	

	@Column(name = "APP_ALERT_STATUS_TYPE")
	private Integer appAlertStatusType;

	

	

	@Column(name = "APP_AMENDED_LMS_USER_ID")
	private Integer appAmendedLmsUserId;

	

	

	

	

	

	

	@Column(name = "APP_ASS_LMS_SALES_USER_ID")
	private Integer appAssignedLmsSalesUserId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_DOCS_ENTERED_TIME")
	private Date appDocsEnteredTime;

	@Column(name = "APP_CREATED_LMS_USER_ID")
	private Integer appCreatedLmsUserId;

	

	@Column(name = "APP_HOT_LEAD_CREATED_USER_ID")
	private Integer appHotLeadCreatedLmsUserId;

	

	

	

	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_LEAD_UPDATE_TIME")
	private Date appLeadUpdateTime;

	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_NEXT_CALL_TIME")
	private Date appNextCallTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APP_SALES_ENTERED_TIME")
	private Date appSalesEnteredTime;

	

	

	

	

	@Column(name = "APP_OFFER_JSON_DATA")
	private String appOfferJsonData;

	@Column(name = "APP_ENTRY_TIME")
	private Date appEntryTime;

	@Column(name = "APP_FILLED_AT")
	private Date appFilledAt;

	@Column(name = "APP_ACTIVE")
	private String appActive;

	@Column(name = "APP_DELETED")
	private String appDeleted;

	@Column(name = "APP_RSM_DECISION")
	private Integer appRSMdecision;

	@Column(name = "APP_PRODUCT_TENURE_FLAG")
	private Integer appProductTenureFlag;

	@Column(name = "APP_INST_SBI_LIFE_INSURANCE")
	private String appInterestedSbiLifeInsurance;

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

	@Column(name = "APP_CLONE_COAPP_ADDRESS_1")
	private boolean cloneCoapplicantAddress1;

	@Column(name = "APP_CLONE_COAPP_ADDRESS_2")
	private boolean cloneCoapplicantAddress2;

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

	@Column(name = "APP_LEAD_AS_CALL_BACK")
	private Integer appLeadAsCallBack;

	@Column(name = "APP_CONTACT_CENTER_LOCATION")
	private int appContactCenterLocation;

	@Column(name = "APP_OTP_VERIFY_TYPE")
	private int appOTPVerifyType;

	@Transient
	private int appMoratorimMonths;

	@Column(name = "APP_FLEXI_PAY_DETAILS")
	private String appFlexiPayDetails;

	@Column(name = "APP_EKYC_REQUEST")
	private String appEkycRequest;

	@Column(name = "APP_EKYC_RESPONSE")
	private String appEkycResponse;

	@Column(name = "APP_APP_SUB_TYPE_ID")
	private Integer appSubTypeId;

	@Column(name = "APP_OTHER_ID_NUMBER")
	private String appOtherIdNumber;

	@Column(name = "APP_OTHER_ID")
	private Integer appOtherId;

	@Column(name = "APP_CIBIL_SCORE")
	private Integer appCibilScore;

	@Column(name = "APP_LOAN_EMI1")
	private Double appLoanEmi1;

	@Column(name = "APP_LOAN_EMI2")
	private Double appLoanEmi2;

	@Column(name = "APP_LOAN_EMI_DISC1")
	private Double appLoanEmiDiscount1;

	@Column(name = "APP_LOAN_EMI_DISC2")
	private Double appLoanEmiDiscount2;

	@Column(name = "APP_LOAN_EMI1_DURATION")
	private Integer appLoanEmi1Duration;

	@Column(name = "APP_LOAN_EMI2_DURATION")
	private Integer appLoanEmi2Duration;

	@Column(name = "APP_MOBILE_DEVICE_ID")
	private Integer appMobileDeviceId;

	@Column(name = "APP_SOCIAL_MEDIA_ID")
	private Integer appSocialMediaId;

	@Lob
	@Column(name = "APP_CUSTOMER_SIGNATURE")
	private String appCustomerSignature;

	@Column(name = "APP_CRM_LEAD_ID")
	private Integer appCRMLeadId;

	@Column(name = "APP_OTP_ATTEMPT_COUNT")
	private Integer appOTPAttemptCount;




	@Column(name = "APP_NO_OF_YEARS_CURRENT_ADD")
	private Integer appNoOfYearsCurrentAdd;

	@Column(name = "APP_RSM_RESPONSE")
	private String appRSMResponse;

	@Column(name = "APP_RSM_STATUS")
	private Integer appRSMStatus;

	@Column(name = "APP_RSM_SCORE")
	private Float appRSMScore;

	@Column(name = "APP_RSM_GRADE")
	private Integer appRSMGrade;

	@Column(name = "APP_RELAT_WITH_BANK")
	private Integer appRelationshipWithBank;

	@Column(name = "APP_MODE_OF_REPAYMENT")
	private Integer appModeOfRepayment;

	@Transient
	private String engineRequest;

	@Transient
	private String engineResponse;

	@Column(name = "APP_BHL_FIRST_INTEREST_EMI")
	private Double appBhlFirstInterestEmi;

	@Column(name = "APP_BHL_INTEREST_RATE")
	private Double appBhlInterestRate;

	@Temporal(TemporalType.DATE)
	@Column(name = "APP_ENTRY_DATE")
	private Date appEntryDate;

	@Column(name = "APP_EMAIL_ATTEMPT_COUNT")
	private Integer appEmailAttemptCount;

	@Column(name = "APP_SCHEME_PMAY")
	private Integer appSchemePmay;

	@Column(name = "APP_AIP_MAIL_SEND_STATUS")
	private String appAipMailSendStatus;

	@Column(name = "APP_HAVE_AADHAAR_NUMBER")
	private Integer appHaveAadhaarNumber;
	
    //new code created by hakeem on 28 sep 22
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
	//end new code 
	
	@Column(name = "OCAS_ID")
	private String ocasID;
	
	@Column(name="APP_CONSENT_ID")
	private Integer appConsentId;
	
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

	public Integer getAppEmploymentType() {
		return appEmploymentType;
	}

	public void setAppEmploymentType(Integer appEmploymentType) {
		this.appEmploymentType = appEmploymentType;
	}

	public String getAppCoapplicantMiddleName2() {
		return appCoapplicantMiddleName2;
	}

	public void setAppCoapplicantMiddleName2(String appCoapplicantMiddleName2) {
		this.appCoapplicantMiddleName2 = appCoapplicantMiddleName2;
	}

	public String getAppCoapplicantMiddleName1() {
		return appCoapplicantMiddleName1;
	}

	public void setAppCoapplicantMiddleName1(String appCoapplicantMiddleName1) {
		this.appCoapplicantMiddleName1 = appCoapplicantMiddleName1;
	}

	public String getAppCoapplicantLandmark_1() {
		return appCoapplicantLandmark_1;
	}

	public void setAppCoapplicantLandmark_1(String appCoapplicantLandmark_1) {
		this.appCoapplicantLandmark_1 = appCoapplicantLandmark_1;
	}

	public String getAppCoapplicantGender_1() {
		return appCoapplicantGender_1;
	}

	public void setAppCoapplicantGender_1(String appCoapplicantGender_1) {
		this.appCoapplicantGender_1 = appCoapplicantGender_1;
	}

	public String getAppCoapplicantLandmark_2() {
		return appCoapplicantLandmark_2;
	}

	public void setAppCoapplicantLandmark_2(String appCoapplicantLandmark_2) {
		this.appCoapplicantLandmark_2 = appCoapplicantLandmark_2;
	}

	public String getAppCoapplicantGender_2() {
		return appCoapplicantGender_2;
	}

	public void setAppCoapplicantGender_2(String appCoapplicantGender_2) {
		this.appCoapplicantGender_2 = appCoapplicantGender_2;
	}

	public Date getAppCoapplicantDateofBirth1() {
		return appCoapplicantDateofBirth1;
	}

	public void setAppCoapplicantDateofBirth1(Date appCoapplicantDateofBirth1) {
		this.appCoapplicantDateofBirth1 = appCoapplicantDateofBirth1;
	}

	public Date getAppCoapplicantDateofBirth2() {
		return appCoapplicantDateofBirth2;
	}

	public void setAppCoapplicantDateofBirth2(Date appCoapplicantDateofBirth2) {
		this.appCoapplicantDateofBirth2 = appCoapplicantDateofBirth2;
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

	public String getAppCoapplicantLastName_1() {
		return appCoapplicantLastName_1;
	}

	public void setAppCoapplicantLastName_1(String appCoapplicantLastName_1) {
		this.appCoapplicantLastName_1 = appCoapplicantLastName_1;
	}

	public String getAppCoapplicantAddress_2_1() {
		return appCoapplicantAddress_2_1;
	}

	public void setAppCoapplicantAddress_2_1(String appCoapplicantAddress_2_1) {
		this.appCoapplicantAddress_2_1 = appCoapplicantAddress_2_1;
	}

	public String getAppCoapplicantAddress_1_1() {
		return appCoapplicantAddress_1_1;
	}

	public void setAppCoapplicantAddress_1_1(String appCoapplicantAddress_1_1) {
		this.appCoapplicantAddress_1_1 = appCoapplicantAddress_1_1;
	}

	public Integer getAppCoapplicantIscoborrower_1() {
		return appCoapplicantIscoborrower_1;
	}

	public void setAppCoapplicantIscoborrower_1(Integer appCoapplicantIscoborrower_1) {
		this.appCoapplicantIscoborrower_1 = appCoapplicantIscoborrower_1;
	}

	public Integer getAppCoapplicantCity_id_1() {
		return appCoapplicantCity_id_1;
	}

	public void setAppCoapplicantCity_id_1(Integer appCoapplicantCity_id_1) {
		this.appCoapplicantCity_id_1 = appCoapplicantCity_id_1;
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

	public Integer getAppCoapplicantTypeId_2() {
		return appCoapplicantTypeId_2;
	}

	public void setAppCoapplicantTypeId_2(Integer appCoapplicantTypeId_2) {
		this.appCoapplicantTypeId_2 = appCoapplicantTypeId_2;
	}

	public Integer getAppCoapplicantEmployment_type_id_2() {
		return appCoapplicantEmployment_type_id_2;
	}

	public void setAppCoapplicantEmployment_type_id_2(Integer appCoapplicantEmployment_type_id_2) {
		this.appCoapplicantEmployment_type_id_2 = appCoapplicantEmployment_type_id_2;
	}

	public String getAppCoapplicantFirstName_2() {
		return appCoapplicantFirstName_2;
	}

	public void setAppCoapplicantFirstName_2(String appCoapplicantFirstName_2) {
		this.appCoapplicantFirstName_2 = appCoapplicantFirstName_2;
	}

	public String getAppCoapplicantLastName_2() {
		return appCoapplicantLastName_2;
	}

	public void setAppCoapplicantLastName_2(String appCoapplicantLastName_2) {
		this.appCoapplicantLastName_2 = appCoapplicantLastName_2;
	}

	public String getAppCoapplicantAddress_2_2() {
		return appCoapplicantAddress_2_2;
	}

	public void setAppCoapplicantAddress_2_2(String appCoapplicantAddress_2_2) {
		this.appCoapplicantAddress_2_2 = appCoapplicantAddress_2_2;
	}

	public String getAppCoapplicantAddress_1_2() {
		return appCoapplicantAddress_1_2;
	}

	public void setAppCoapplicantAddress_1_2(String appCoapplicantAddress_1_2) {
		this.appCoapplicantAddress_1_2 = appCoapplicantAddress_1_2;
	}

	public Integer getAppCoapplicantIscoborrower_2() {
		return appCoapplicantIscoborrower_2;
	}

	public void setAppCoapplicantIscoborrower_2(Integer appCoapplicantIscoborrower_2) {
		this.appCoapplicantIscoborrower_2 = appCoapplicantIscoborrower_2;
	}

	public Integer getAppCoapplicantState_id_2() {
		return appCoapplicantState_id_2;
	}

	public void setAppCoapplicantState_id_2(Integer appCoapplicantState_id_2) {
		this.appCoapplicantState_id_2 = appCoapplicantState_id_2;
	}

	public Integer getAppCoapplicantCity_id_2() {
		return appCoapplicantCity_id_2;
	}

	public void setAppCoapplicantCity_id_2(Integer appCoapplicantCity_id_2) {
		this.appCoapplicantCity_id_2 = appCoapplicantCity_id_2;
	}

	public Integer getAppCoapplicantPincode_2() {
		return appCoapplicantPincode_2;
	}

	public void setAppCoapplicantPincode_2(Integer appCoapplicantPincode_2) {
		this.appCoapplicantPincode_2 = appCoapplicantPincode_2;
	}

	public String getAppCoapplicantPanCardNo_2() {
		return appCoapplicantPanCardNo_2;
	}

	public void setAppCoapplicantPanCardNo_2(String appCoapplicantPanCardNo_2) {
		this.appCoapplicantPanCardNo_2 = appCoapplicantPanCardNo_2;
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

	public String getAppLoanEmployerName() {
		return appLoanEmployerName;
	}

	public void setAppLoanEmployerName(String appLoanEmployerName) {
		this.appLoanEmployerName = appLoanEmployerName;
	}
	
	public Integer getAppSeqId() {
		return this.appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public String getAppActive() {
		return this.appActive;
	}

	public void setAppActive(String appActive) {
		this.appActive = appActive;
	}

	public String getAppAddress1() {
		return this.appAddress1;
	}

	public void setAppAddress1(String appAddress1) {
		this.appAddress1 = appAddress1;
	}

	public String getAppAddress2() {
		return this.appAddress2;
	}

	public void setAppAddress2(String appAddress2) {
		this.appAddress2 = appAddress2;
	}

	public String getAppAddressLandmark() {
		return this.appAddressLandmark;
	}

	public void setAppAddressLandmark(String appAddressLandmark) {
		this.appAddressLandmark = appAddressLandmark;
	}

	

	

	public Integer getAppAlertStatusType() {
		return this.appAlertStatusType;
	}

	public void setAppAlertStatusType(Integer appAlertStatusType) {
		this.appAlertStatusType = appAlertStatusType;
	}

	

	

	public Integer getAppAmendedLmsUserId() {
		return this.appAmendedLmsUserId;
	}

	public void setAppAmendedLmsUserId(Integer appAmendedLmsUserId) {
		this.appAmendedLmsUserId = appAmendedLmsUserId;
	}

	

	

	

	

	

	

	public Integer getAppBankId() {
		return this.appBankId;
	}

	public void setAppBankId(Integer appBankId) {
		this.appBankId = appBankId;
	}

	public Integer getAppBranchId() {
		return this.appBranchId;
	}

	public void setAppBranchId(Integer appBranchId) {
		this.appBranchId = appBranchId;
	}

	public Integer getAppCityId() {
		return this.appCityId;
	}

	public void setAppCityId(Integer appCityId) {
		this.appCityId = appCityId;
	}

	public Integer getAppCompanyCategoryId() {
		return this.appCompanyCategoryId;
	}

	public void setAppCompanyCategoryId(Integer appCompanyCategoryId) {
		this.appCompanyCategoryId = appCompanyCategoryId;
	}

	public Date getAppCompanyJoiningDate() {
		return this.appCompanyJoiningDate;
	}

	public void setAppCompanyJoiningDate(Date appCompanyJoiningDate) {
		this.appCompanyJoiningDate = appCompanyJoiningDate;
	}

	public Integer getAppCreatedLmsUserId() {
		return this.appCreatedLmsUserId;
	}

	public void setAppCreatedLmsUserId(Integer appCreatedLmsUserId) {
		this.appCreatedLmsUserId = appCreatedLmsUserId;
	}

	public Integer getAppDataSourceId() {
		return this.appDataSourceId;
	}

	public void setAppDataSourceId(Integer appDataSourceId) {
		this.appDataSourceId = appDataSourceId;
	}

	public String getAppDeleted() {
		return this.appDeleted;
	}

	public void setAppDeleted(String appDeleted) {
		this.appDeleted = appDeleted;
	}

	

	public Date getAppDocPickupTime() {
		return this.appDocPickupTime;
	}

	public void setAppDocPickupTime(Date appDocPickupTime) {
		this.appDocPickupTime = appDocPickupTime;
	}

	public Date getAppDocsEnteredTime() {
		return this.appDocsEnteredTime;
	}

	public void setAppDocsEnteredTime(Date appDocsEnteredTime) {
		this.appDocsEnteredTime = appDocsEnteredTime;
	}

	

	public String getAppEmailVerified() {
		return this.appEmailVerified;
	}

	public void setAppEmailVerified(String appEmailVerified) {
		this.appEmailVerified = appEmailVerified;
	}

	public Date getAppEntryTime() {
		return this.appEntryTime;
	}

	public void setAppEntryTime(Date appEntryTime) {
		this.appEntryTime = appEntryTime;
	}

	public String getAppEverDefaultedCreditCard() {
		return this.appEverDefaultedCreditCard;
	}

	public void setAppEverDefaultedCreditCard(String appEverDefaultedCreditCard) {
		this.appEverDefaultedCreditCard = appEverDefaultedCreditCard;
	}

	public String getAppExistingRelationTypeId() {
		return this.appExistingRelationTypeId;
	}

	public void setAppExistingRelationTypeId(String appExistingRelationTypeId) {
		this.appExistingRelationTypeId = appExistingRelationTypeId;
	}

	public Date getAppFilledAt() {
		return this.appFilledAt;
	}

	public void setAppFilledAt(Date appFilledAt) {
		this.appFilledAt = appFilledAt;
	}

	public String getAppFirstName() {
		return this.appFirstName;
	}

	public void setAppFirstName(String appFirstName) {
		this.appFirstName = appFirstName;
	}

	public Integer getAppFulfillmentGroupId() {
		return this.appFulfillmentGroupId;
	}

	public void setAppFulfillmentGroupId(Integer appFulfillmentGroupId) {
		this.appFulfillmentGroupId = appFulfillmentGroupId;
	}

	public String getAppGender() {
		return this.appGender;
	}

	public void setAppGender(String appGender) {
		this.appGender = appGender;
	}

	public Integer getAppHomeLoanId() {
		return this.appHomeLoanId;
	}

	public void setAppHomeLoanId(Integer appHomeLoanId) {
		this.appHomeLoanId = appHomeLoanId;
	}

	public Integer getAppHotLeadCreatedLmsUserId() {
		return this.appHotLeadCreatedLmsUserId;
	}

	public void setAppHotLeadCreatedLmsUserId(Integer appHotLeadCreatedLmsUserId) {
		this.appHotLeadCreatedLmsUserId = appHotLeadCreatedLmsUserId;
	}

	public Integer getAppIntermediaryId() {
		return this.appIntermediaryId;
	}

	public void setAppIntermediaryId(Integer appIntermediaryId) {
		this.appIntermediaryId = appIntermediaryId;
	}

	

	

	public Integer getAppIsExistingBankId() {
		return this.appIsExistingBankId;
	}

	public void setAppIsExistingBankId(Integer appIsExistingBankId) {
		this.appIsExistingBankId = appIsExistingBankId;
	}

	public String getAppLastName() {
		return this.appLastName;
	}

	public void setAppLastName(String appLastName) {
		this.appLastName = appLastName;
	}

	

	

	public Date getAppLeadUpdateTime() {
		return this.appLeadUpdateTime;
	}

	public void setAppLeadUpdateTime(Date appLeadUpdateTime) {
		this.appLeadUpdateTime = appLeadUpdateTime;
	}

	public Double getAppLoanAmount() {
		return this.appLoanAmount;
	}

	public void setAppLoanAmount(Double appLoanAmount) {
		this.appLoanAmount = appLoanAmount;
	}

	

	public Double getAppLoanEmi() {
		return this.appLoanEmi;
	}

	public void setAppLoanEmi(Double appLoanEmi) {
		this.appLoanEmi = appLoanEmi;
	}

	public Float getAppLoanInterestRate() {
		return this.appLoanInterestRate;
	}

	public void setAppLoanInterestRate(Float appLoanInterestRate) {
		this.appLoanInterestRate = appLoanInterestRate;
	}

	public Integer getAppLoanInterestRateType() {
		return this.appLoanInterestRateType;
	}

	public void setAppLoanInterestRateType(Integer appLoanInterestRateType) {
		this.appLoanInterestRateType = appLoanInterestRateType;
	}

	

	public Double getAppLoanProcessingFee() {
		return this.appLoanProcessingFee;
	}

	public void setAppLoanProcessingFee(Double appLoanProcessingFee) {
		this.appLoanProcessingFee = appLoanProcessingFee;
	}

	

	public Integer getAppLoanStatusId() {
		return this.appLoanStatusId;
	}

	public void setAppLoanStatusId(Integer appLoanStatusId) {
		this.appLoanStatusId = appLoanStatusId;
	}

	public Integer getAppLoanTenure() {
		return this.appLoanTenure;
	}

	public void setAppLoanTenure(Integer appLoanTenure) {
		this.appLoanTenure = appLoanTenure;
	}

	public String getAppMiddleName() {
		return this.appMiddleName;
	}

	public void setAppMiddleName(String appMiddleName) {
		this.appMiddleName = appMiddleName;
	}

	

	public String getAppMobileNo() {
		return this.appMobileNo;
	}

	public void setAppMobileNo(String appMobileNo) {
		this.appMobileNo = appMobileNo;
	}

	public Integer getAppMobileVerificationCode() {
		return this.appMobileVerificationCode;
	}

	public void setAppMobileVerificationCode(Integer appMobileVerificationCode) {
		this.appMobileVerificationCode = appMobileVerificationCode;
	}

	public String getAppMobileVerificationCodeReceived() {
		return this.appMobileVerificationCodeReceived;
	}

	public void setAppMobileVerificationCodeReceived(String appMobileVerificationCodeReceived) {
		this.appMobileVerificationCodeReceived = appMobileVerificationCodeReceived;
	}

	

	

	public String getAppMobileVerified() {
		return this.appMobileVerified;
	}

	public void setAppMobileVerified(String appMobileVerified) {
		this.appMobileVerified = appMobileVerified;
	}

	

	public Date getAppNextCallTime() {
		return this.appNextCallTime;
	}

	public void setAppNextCallTime(Date appNextCallTime) {
		this.appNextCallTime = appNextCallTime;
	}

	public Integer getAppNonOtpEmailAlertsCount() {
		return this.appNonOtpEmailAlertsCount;
	}

	public void setAppNonOtpEmailAlertsCount(Integer appNonOtpEmailAlertsCount) {
		this.appNonOtpEmailAlertsCount = appNonOtpEmailAlertsCount;
	}

	public Date getAppNonOtpEmailAlertsLastTime() {
		return this.appNonOtpEmailAlertsLastTime;
	}

	public void setAppNonOtpEmailAlertsLastTime(Date appNonOtpEmailAlertsLastTime) {
		this.appNonOtpEmailAlertsLastTime = appNonOtpEmailAlertsLastTime;
	}

	public String getAppOfficeAddress1() {
		return this.appOfficeAddress1;
	}

	public void setAppOfficeAddress1(String appOfficeAddress1) {
		this.appOfficeAddress1 = appOfficeAddress1;
	}

	public String getAppOfficeAddress2() {
		return this.appOfficeAddress2;
	}

	public void setAppOfficeAddress2(String appOfficeAddress2) {
		this.appOfficeAddress2 = appOfficeAddress2;
	}

	public Integer getAppOfficeCityId() {
		return this.appOfficeCityId;
	}

	public void setAppOfficeCityId(Integer appOfficeCityId) {
		this.appOfficeCityId = appOfficeCityId;
	}

	public Integer getAppOfficePincode() {
		return this.appOfficePincode;
	}

	public void setAppOfficePincode(Integer appOfficePincode) {
		this.appOfficePincode = appOfficePincode;
	}

	public String getAppPanCardNo() {
		return this.appPanCardNo;
	}

	public void setAppPanCardNo(String appPanCardNo) {
		this.appPanCardNo = appPanCardNo;
	}

	public String getAppPanCardVerified() {
		return this.appPanCardVerified;
	}

	public void setAppPanCardVerified(String appPanCardVerified) {
		this.appPanCardVerified = appPanCardVerified;
	}

	public Date getAppPanUpdatedDatetime() {
		return this.appPanUpdatedDatetime;
	}

	public void setAppPanUpdatedDatetime(Date appPanUpdatedDatetime) {
		this.appPanUpdatedDatetime = appPanUpdatedDatetime;
	}

	public Date getAppPanVerifiedDatetime() {
		return this.appPanVerifiedDatetime;
	}

	public void setAppPanVerifiedDatetime(Date appPanVerifiedDatetime) {
		this.appPanVerifiedDatetime = appPanVerifiedDatetime;
	}

	public String getAppPickupAddress1() {
		return this.appPickupAddress1;
	}

	public void setAppPickupAddress1(String appPickupAddress1) {
		this.appPickupAddress1 = appPickupAddress1;
	}

	public String getAppPickupAddress2() {
		return this.appPickupAddress2;
	}

	public void setAppPickupAddress2(String appPickupAddress2) {
		this.appPickupAddress2 = appPickupAddress2;
	}

	public Integer getAppPickupCityId() {
		return this.appPickupCityId;
	}

	public void setAppPickupCityId(Integer appPickupCityId) {
		this.appPickupCityId = appPickupCityId;
	}

	public Integer getAppPickupPincode() {
		return this.appPickupPincode;
	}

	public void setAppPickupPincode(Integer appPickupPincode) {
		this.appPickupPincode = appPickupPincode;
	}

	public Integer getAppPincode() {
		return this.appPincode;
	}

	public void setAppPincode(Integer appPincode) {
		this.appPincode = appPincode;
	}

	public Integer getAppQualificationId() {
		return this.appQualificationId;
	}

	public void setAppQualificationId(Integer appQualificationId) {
		this.appQualificationId = appQualificationId;
	}

	public Integer getAppQuoteId() {
		return this.appQuoteId;
	}

	public void setAppQuoteId(Integer appQuoteId) {
		this.appQuoteId = appQuoteId;
	}

	public String getAppReferenceId() {
		return this.appReferenceId;
	}

	public void setAppReferenceId(String appReferenceId) {
		this.appReferenceId = appReferenceId;
	}

	
	public Integer getAppResidenceType() {
		return this.appResidenceType;
	}

	public void setAppResidenceType(Integer appResidenceType) {
		this.appResidenceType = appResidenceType;
	}

	public Integer getAppSalaryBankId() {
		return this.appSalaryBankId;
	}

	public void setAppSalaryBankId(Integer appSalaryBankId) {
		this.appSalaryBankId = appSalaryBankId;
	}

	public Date getAppSalesEnteredTime() {
		return this.appSalesEnteredTime;
	}

	public void setAppSalesEnteredTime(Date appSalesEnteredTime) {
		this.appSalesEnteredTime = appSalesEnteredTime;
	}

	

	public String getAppWorkEmail() {
		return this.appWorkEmail;
	}

	public void setAppWorkEmail(String appWorkEmail) {
		this.appWorkEmail = appWorkEmail;
	}

	

	public Double getAppLoanMaxAmont() {
		return appLoanMaxAmont;
	}

	public void setAppLoanMaxAmont(Double appLoanMaxAmont) {
		this.appLoanMaxAmont = appLoanMaxAmont;
	}

	public String getAppOfferJsonData() {
		return appOfferJsonData;
	}

	public void setAppOfferJsonData(String appOfferJsonData) {
		this.appOfferJsonData = appOfferJsonData;
	}

	public Integer getAppLoanAccountType() {
		return appLoanAccountType;
	}

	public void setAppLoanAccountType(Integer appLoanAccountType) {
		this.appLoanAccountType = appLoanAccountType;
	}

	public Integer getAppOtpMobileAlertsCount() {
		return appOtpMobileAlertsCount;
	}

	public void setAppOtpMobileAlertsCount(Integer appOtpMobileAlertsCount) {
		this.appOtpMobileAlertsCount = appOtpMobileAlertsCount;
	}
	

	public String getApploanappliedCoupon() {
		return apploanappliedCoupon;
	}

	public void setApploanappliedCoupon(String apploanappliedCoupon) {
		this.apploanappliedCoupon = apploanappliedCoupon;
	}

	public Integer getAppOfficeStateId() {
		return appOfficeStateId;
	}

	public void setAppOfficeStateId(Integer appOfficeStateId) {
		this.appOfficeStateId = appOfficeStateId;
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

	public String getAppCoapplicantEmployerName1() {
		return appCoapplicantEmployerName1;
	}

	public void setAppCoapplicantEmployerName1(String appCoapplicantEmployerName1) {
		this.appCoapplicantEmployerName1 = appCoapplicantEmployerName1;
	}

	public String getAppCoapplicantEmployerName2() {
		return appCoapplicantEmployerName2;
	}

	public void setAppCoapplicantEmployerName2(String appCoapplicantEmployerName2) {
		this.appCoapplicantEmployerName2 = appCoapplicantEmployerName2;
	}

	public Integer getAppStateId() {
		return appStateId;
	}

	public void setAppStateId(Integer appStateId) {
		this.appStateId = appStateId;
	}

	public Integer getAppCoapplicantState_id_1() {
		return appCoapplicantState_id_1;
	}

	public void setAppCoapplicantState_id_1(Integer appCoapplicantState_id_1) {
		this.appCoapplicantState_id_1 = appCoapplicantState_id_1;
	}

	public Integer getAppPickupStateId() {
		return appPickupStateId;
	}

	public void setAppPickupStateId(Integer appPickupStateId) {
		this.appPickupStateId = appPickupStateId;
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

	public Integer getAppDistrictId() {
		return appDistrictId;
	}

	public void setAppDistrictId(Integer appDistrictId) {
		this.appDistrictId = appDistrictId;
	}

	public Integer getAppPhotoId() {
		return appPhotoId;
	}

	public void setAppPhotoId(Integer appPhotoId) {
		this.appPhotoId = appPhotoId;
	}

	

	

	

	

	public Integer getAppOfficeDistrictId() {
		return appOfficeDistrictId;
	}

	public void setAppOfficeDistrictId(Integer appOfficeDistrictId) {
		this.appOfficeDistrictId = appOfficeDistrictId;
	}

	public Integer getAppPickupDistrictId() {
		return appPickupDistrictId;
	}

	public void setAppPickupDistrictId(Integer appPickupDistrictId) {
		this.appPickupDistrictId = appPickupDistrictId;
	}

	public Integer getAppCoapplicantDistrictId1() {
		return appCoapplicantDistrictId1;
	}

	public void setAppCoapplicantDistrictId1(Integer appCoapplicantDistrictId1) {
		this.appCoapplicantDistrictId1 = appCoapplicantDistrictId1;
	}

	public Integer getAppCoapplicantDistrictId2() {
		return appCoapplicantDistrictId2;
	}

	public void setAppCoapplicantDistrictId2(Integer appCoapplicantDistrictId2) {
		this.appCoapplicantDistrictId2 = appCoapplicantDistrictId2;
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

	public String getAppPropertyAddress1() {
		return appPropertyAddress1;
	}

	public void setAppPropertyAddress1(String appPropertyAddress1) {
		this.appPropertyAddress1 = appPropertyAddress1;
	}

	public String getAppPropertyAddress2() {
		return appPropertyAddress2;
	}

	public void setAppPropertyAddress2(String appPropertyAddress2) {
		this.appPropertyAddress2 = appPropertyAddress2;
	}

	public String getAppPropertyAddressLandmark() {
		return appPropertyAddressLandmark;
	}

	public void setAppPropertyAddressLandmark(String appPropertyAddressLandmark) {
		this.appPropertyAddressLandmark = appPropertyAddressLandmark;
	}

	public Integer getAppPropertyStateId() {
		return appPropertyStateId;
	}

	public void setAppPropertyStateId(Integer appPropertyStateId) {
		this.appPropertyStateId = appPropertyStateId;
	}

	public Integer getAppPropertyCityId() {
		return appPropertyCityId;
	}

	public void setAppPropertyCityId(Integer appPropertyCityId) {
		this.appPropertyCityId = appPropertyCityId;
	}

	public Integer getAppPropertyDistrictId() {
		return appPropertyDistrictId;
	}

	public void setAppPropertyDistrictId(Integer appPropertyDistrictId) {
		this.appPropertyDistrictId = appPropertyDistrictId;
	}

	public Integer getAppPropertyPincode() {
		return appPropertyPincode;
	}

	public void setAppPropertyPincode(Integer appPropertyPincode) {
		this.appPropertyPincode = appPropertyPincode;
	}

	public String getAppInterestedSbiLifeInsurance() {
		return appInterestedSbiLifeInsurance;
	}

	public void setAppInterestedSbiLifeInsurance(String appInterestedSbiLifeInsurance) {
		this.appInterestedSbiLifeInsurance = appInterestedSbiLifeInsurance;
	}

	public String getAppPermanentAddressLandMark() {
		return appPermanentAddressLandMark;
	}

	public void setAppPermanentAddressLandMark(String appPermanentAddressLandMark) {
		this.appPermanentAddressLandMark = appPermanentAddressLandMark;
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

	public boolean isClonePermanentAddress() {
		return clonePermanentAddress;
	}

	public void setClonePermanentAddress(boolean clonePermanentAddress) {
		this.clonePermanentAddress = clonePermanentAddress;
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

	public Integer getAppCountryId() {
		return appCountryId;
	}

	public void setAppCountryId(Integer appCountryId) {
		this.appCountryId = appCountryId;
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

	public Integer getAppLoanMoratoriumPeriod() {
		return appLoanMoratoriumPeriod;
	}

	public void setAppLoanMoratoriumPeriod(Integer appLoanMoratoriumPeriod) {
		this.appLoanMoratoriumPeriod = appLoanMoratoriumPeriod;
	}

	public boolean isCloneCoapplicantAddress1() {
		return cloneCoapplicantAddress1;
	}

	public void setCloneCoapplicantAddress1(boolean cloneCoapplicantAddress1) {
		this.cloneCoapplicantAddress1 = cloneCoapplicantAddress1;
	}

	public boolean isCloneCoapplicantAddress2() {
		return cloneCoapplicantAddress2;
	}

	public void setCloneCoapplicantAddress2(boolean cloneCoapplicantAddress2) {
		this.cloneCoapplicantAddress2 = cloneCoapplicantAddress2;
	}

	public Boolean getAppPanCardLater() {
		return appPanCardLater;
	}

	public void setAppPanCardLater(Boolean appPanCardLater) {
		this.appPanCardLater = appPanCardLater;
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

	public Integer getAppLeadAsCallBack() {
		return appLeadAsCallBack;
	}

	public void setAppLeadAsCallBack(Integer appLeadAsCallBack) {
		this.appLeadAsCallBack = appLeadAsCallBack;
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

	public int getAppMoratorimMonths() {
		return appMoratorimMonths;
	}

	public void setAppMoratorimMonths(int appMoratorimMonths) {
		this.appMoratorimMonths = appMoratorimMonths;
	}

	public String getAppFlexiPayDetails() {
		return appFlexiPayDetails;
	}

	public void setAppFlexiPayDetails(String appFlexiPayDetails) {
		this.appFlexiPayDetails = appFlexiPayDetails;
	}

	public Integer getAppSubTypeId() {
		return appSubTypeId;
	}

	public void setAppSubTypeId(Integer appSubTypeId) {
		this.appSubTypeId = appSubTypeId;
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

	public Double getAppLoanEmi1() {
		return appLoanEmi1;
	}

	public void setAppLoanEmi1(Double appLoanEmi1) {
		this.appLoanEmi1 = appLoanEmi1;
	}

	public Double getAppLoanEmi2() {
		return appLoanEmi2;
	}

	public void setAppLoanEmi2(Double appLoanEmi2) {
		this.appLoanEmi2 = appLoanEmi2;
	}

	public Double getAppLoanEmiDiscount1() {
		return appLoanEmiDiscount1;
	}

	public void setAppLoanEmiDiscount1(Double appLoanEmiDiscount1) {
		this.appLoanEmiDiscount1 = appLoanEmiDiscount1;
	}

	public Double getAppLoanEmiDiscount2() {
		return appLoanEmiDiscount2;
	}

	public void setAppLoanEmiDiscount2(Double appLoanEmiDiscount2) {
		this.appLoanEmiDiscount2 = appLoanEmiDiscount2;
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

	public Integer getAppLoanEmi1Duration() {
		return appLoanEmi1Duration;
	}

	public void setAppLoanEmi1Duration(Integer appLoanEmi1Duration) {
		this.appLoanEmi1Duration = appLoanEmi1Duration;
	}

	public Integer getAppLoanEmi2Duration() {
		return appLoanEmi2Duration;
	}

	public void setAppLoanEmi2Duration(Integer appLoanEmi2Duration) {
		this.appLoanEmi2Duration = appLoanEmi2Duration;
	}

	public Integer getAppCRMLeadId() {
		return appCRMLeadId;
	}

	public void setAppCRMLeadId(Integer appCRMLeadId) {
		this.appCRMLeadId = appCRMLeadId;
	}

	public Integer getAppOTPAttemptCount() {
		return appOTPAttemptCount;
	}

	public void setAppOTPAttemptCount(Integer appOTPAttemptCount) {
		this.appOTPAttemptCount = appOTPAttemptCount;
	}









	public Integer getAppNoOfYearsCurrentAdd() {
		return appNoOfYearsCurrentAdd;
	}

	public void setAppNoOfYearsCurrentAdd(Integer appNoOfYearsCurrentAdd) {
		this.appNoOfYearsCurrentAdd = appNoOfYearsCurrentAdd;
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

	public Float getAppRSMScore() {
		return appRSMScore;
	}

	public void setAppRSMScore(Float appRSMScore) {
		this.appRSMScore = appRSMScore;
	}

	public Integer getAppRSMGrade() {
		return appRSMGrade;
	}

	public void setAppRSMGrade(Integer appRSMGrade) {
		this.appRSMGrade = appRSMGrade;
	}

	public Integer getAppRelationshipWithBank() {
		return appRelationshipWithBank;
	}

	public void setAppRelationshipWithBank(Integer appRelationshipWithBank) {
		this.appRelationshipWithBank = appRelationshipWithBank;
	}

	public Integer getAppModeOfRepayment() {
		return appModeOfRepayment;
	}

	public void setAppModeOfRepayment(Integer appModeOfRepayment) {
		this.appModeOfRepayment = appModeOfRepayment;
	}

	public String getEngineRequest() {
		return engineRequest;
	}

	public void setEngineRequest(String engineRequest) {
		this.engineRequest = engineRequest;
	}

	public String getEngineResponse() {
		return engineResponse;
	}

	public void setEngineResponse(String engineResponse) {
		this.engineResponse = engineResponse;
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

	public Double getAppBhlFirstInterestEmi() {
		return appBhlFirstInterestEmi;
	}

	public void setAppBhlFirstInterestEmi(Double appBhlFirstInterestEmi) {
		this.appBhlFirstInterestEmi = appBhlFirstInterestEmi;
	}

	public Double getAppBhlInterestRate() {
		return appBhlInterestRate;
	}

	public void setAppBhlInterestRate(Double appBhlInterestRate) {
		this.appBhlInterestRate = appBhlInterestRate;
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

	public Integer getAppSchemePmay() {
		return appSchemePmay;
	}

	public void setAppSchemePmay(Integer appSchemePmay) {
		this.appSchemePmay = appSchemePmay;
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

}
