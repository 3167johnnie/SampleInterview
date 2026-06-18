package com.mintstreet.loan.personal.entity;

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
@Table(name="RUPEEPOWER_OCAS_T_00360")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="ApplicationFormPersonalLoan.findAll", query="SELECT a FROM ApplicationFormPersonalLoan a"),
	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByAppReferenceId", 
	    query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appReferenceId =:appReferenceId "),
	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo", 
		query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' and a.appDeleted ='N' "
				+ " and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appMobileNo)=:appMobileNo "),
	
	
	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByAppReferenceIdAndMobileNoALt", 
	query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.appAlternateMobileNumber)=:appAlternateMobileNumber "),

	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByMobileAndSmsOtp", 
	query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N'"
			+ " and a.appMobileVerificationCode =:appMobileVerificationCode and a.appMobileNo=:appMobileNo order by a.appEntryTime desc"),
	
	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByMobileAndSmsOtpAlt", 
	query = "Select a from ApplicationFormEducationLoan a where  a.appActive = 'Y' "
			+ " and a.appDeleted ='N' and a.appDataSourceId =3 and a.appMobileVerified = 'N' and a.appMobileVerificationCode =:appMobileVerificationCode and a.appAlternateMobileNumber=:appAlternateMobileNumber order by a.appEntryTime desc"),

	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByUniqueRefNummber", 
	query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appCustomerUniqueId =:appCustomerUniqueId order by a.appEntryTime desc"),			
				
	@NamedQuery(name="ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByIDSPMId", 
	query = "Select a from ApplicationFormPersonalLoan a where  a.appActive = 'Y' and a.appDeleted ='N' and a.appIDSPMDumpId =:appIDSPMDumpId order by a.appEntryTime desc")			
	
})
@SequenceGenerator(name="G8", sequenceName="RUPEEPOWER_OCAS_SEQ_00195" ,allocationSize=1)
public class ApplicationFormPersonalLoan extends com.mintstreet.common.entity.Domain<java.lang.Integer>  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G8")
	@Column(name="app_seq_id")
	private Integer appSeqId;

	@Column(name="APP_active")
	private String appActive;

	@Column(name="app_address_1")
	private String appAddress1;

	@Column(name="app_address_2")
	private String appAddress2;

	@Column(name="app_address_landmark")
	private String appAddressLandmark;

	@Column(name="app_alert_status_type")
	private Integer appAlertStatusType;

	@Column(name="app_amended_lms_user_id")
	private Integer appAmendedLmsUserId;

	@Column(name="APP_ASS_LMS_SALES_USER_ID")
	private Integer appAssignedLmsSalesUserId;
	
	@Column(name="app_bank_deposit")
	private Double appBankDeposit;
	
	@Column(name="app_EXISTING_TOT_LA")
	private Double appExistingTotalLoanAmount;

	@Column(name="app_bank_id")
	private Integer appBankId;

	@Column(name="app_branch_id")
	private Integer appBranchId;
	
	@Column(name="APP_PREV_BRANCH_ID")
	private Integer appPreviousBranchId;
	
	@Column(name="app_city_id")
	private Integer appCityId;
	
	@Column(name="APP_LOCALITY_ID")
	private Integer appLocalityId;
	
	@Column(name="APP_DISTRICT_ID")
	private Integer appDistrictId;
	
	@Column(name="APP_SALES_TEAM_ID")
	private Integer appSalesTeamId;
	
	@Column(name="app_loan_type")
	private Byte appLoanType;

	@Column(name="app_coapp_first_name")
	private String appCoapplicantFirstName;
	
	@Column(name="app_coapp_middle_name")
	private String appCoapplicantMiddleName;

	@Column(name="app_coapp_last_name")
	private String appCoapplicantLastName;
	
	@Column(name="app_coapp_address_1")
	private String appCoapplicantAddress1;

	@Column(name="app_coapp_address_2")
	private String appCoapplicantAddress2;

	@Column(name="app_coapp_bank_deposit")
	private Double appCoapplicantBankDeposit;

	@Column(name="app_coapplicant_state_id")
	private Integer appCoapplicantStateId;
	
	@Column(name="app_coapp_city_id")
	private Integer appCoapplicantCityId;

	@Column(name="app_coapp_district_id")
	private Integer appCoapplicantDistrictId;
	
	@Column(name="app_coapp_employer_name_1")
	private String appCoapplicantEmployerName1;

	@Column(name="app_coapp_employment_type_id")
	private Integer appCoapplicantEmploymentTypeId;

	@Column(name="app_coapp_industry_type_id")
	private Integer appCoapplicantIndustryTypeId;
	
	@Column(name="app_coapp_gold_ornaments")
	private Double appCoapplicantGoldOrnaments;

	@Column(name="app_coapp_immovable_property")
	private Double appCoapplicantImmovableProperty;

	@Column(name="app_coapp_is_coborrower")
	private String appCoapplicantIsCoborrower;

	@Column(name="app_coapp_landmark")
	private String appCoapplicantLandmark;

	@Column(name="app_coapp_mutual_fund_units")
	private Double appCoapplicantMutualFundUnits;

	@Column(name="app_coapp_nscs")
	private Double appCoapplicantNscs;

	@Column(name="app_coapp_other_assets")
	private Double appCoapplicantOtherAssets;

	@Column(name="app_coapp_pan_card_no")
	private String appCoapplicantPanCardNo;

	@Column(name="app_coapp_pf_or_ppf")
	private Double appCoapplicantPfOrPpf;

	@Column(name="app_coapp_pincode")
	private Integer appCoapplicantPincode;

	@Column(name="app_coapp_relation_type_id")
	private Integer appCoapplicantRelationTypeId;

	@Column(name="app_coapp_residenceType")
	private Integer appCoapplicantResidenceType;

	@Column(name="app_coapp_shares_or_debentures")
	private Double appCoapplicantSharesOrDebentures;
	
	@Column(name="app_coapp_existing_tot_la")
	private Double appCoapplicantExistingTotalLoanAmount;

	@Column(name="app_coapp2_first_name")
	private String appCoapplicant2FirstName;
	
	@Column(name="app_coapp2_middle_name")
	private String appCoapplicant2MiddleName;
	
	@Column(name="app_coapp2_last_name")
	private String appCoapplicant2LastName;
	
	@Column(name="app_coapp2_employment_type_id")
	private Integer appCoapplicant2EmploymentTypeId;
	
	@Column(name="app_coapp2_industry_type_id")
	private Integer appCoapplicant2IndustryTypeId;
	
	@Column(name="app_coapp2_net_annual_income")
	private Integer appCoapplicant2NetAnnualIncome;
	
	@Column(name="app_coapp2_no_of_dependent")
	private Integer appCoapplicant2NoOfDependent;
	
	@Column(name="app_coapp2_immovable_property")
	private Double appCoapplicant2ImmovableProperty;
	
	@Column(name="app_coapp2_bank_deposit")
	private Double appCoapplicant2BankDeposit;
	
	@Column(name="app_coapp2_nscs")
	private Double appCoapplicant2Nscs;
	
	@Column(name="app_coapp2_pf_or_ppf")
	private Double appCoapplicant2PfOrPpf;
	
	@Column(name="app_coapp2_gold_ornaments")
	private Double appCoapplicant2GoldOrnaments;
	
	@Column(name="app_coapp2_mutual_fund_units")
	private Double appCoapplicant2MutualFundUnits;
	
	@Column(name="app_coapp2_existing_tot_la")
	private Double appCoapplicant2ExistingTotalLoanAmount;
	
	@Column(name="app_coapp3_first_name")
	private String appCoapplicant3FirstName;
	
	@Column(name="app_coapp3_middle_name")
	private String appCoapplicant3MiddleName;
	
	@Column(name="app_coapp3_last_name")
	private String appCoapplicant3LastName;
	
	@Column(name="app_coapp3_employment_type_id")
	private Integer appCoapplicant3EmploymentTypeId;
	
	@Column(name="app_coapp3_industry_type_id")
	private Integer appCoapplicant3IndustryTypeId;
	
	@Column(name="app_coapp3_net_annual_income")
	private Integer appCoapplicant3NetAnnualIncome;
	
	@Column(name="app_coapp3_no_of_dependent")
	private Integer appCoapplicant3NoOfDependent;
	
	@Column(name="app_coapp3_immovable_property")
	private Double appCoapplicant3ImmovableProperty;
	
	@Column(name="app_coapp3_bank_deposit")
	private Double appCoapplicant3BankDeposit;
	
	@Column(name="app_coapp3_nscs")
	private Double appCoapplicant3Nscs;
	
	@Column(name="app_coapp3_pf_or_ppf")
	private Double appCoapplicant3PfOrPpf;
	
	@Column(name="app_coapp3_gold_ornaments")
	private Double appCoapplicant3GoldOrnaments;
	
	@Column(name="app_coapp3_mutual_fund_units")
	private Double appCoapplicant3MutualFundUnits;
	
	@Column(name="app_coapp3_existing_tot_la")
	private Double appCoapplicant3ExistingTotalLoanAmount;
	
	@Column(name="app_coapp_pre_emis")
	private Double appCoapplicantPreEmis;
	
	@Column(name="app_coapp2_pre_emis")
	private Double appCoapplicant2PreEmis;
	
	@Column(name="app_coapp3_pre_emis")
	private Double appCoapplicant3PreEmis;
	
	@Column(name="app_coapp_net_annual_income")
	private Integer appCoapplicantNetAnnualIncome;

	@Column(name="app_coapp_no_of_dependent")
	private Integer appCoapplicantNoOfDependent;
	
	@Column(name="app_company_category_id")
	private Integer appCompanyCategoryId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_company_joining_date")
	private Date appCompanyJoiningDate;

	@Column(name="app_created_lms_user_id")
	private Integer appCreatedLmsUserId;

	@Column(name="app_data_source_id")
	private Integer appDataSourceId;

	@Column(name="app_deleted")
	private String appDeleted;

	@Temporal(TemporalType.DATE)
	@Column(name="app_dob")
	private Date appDobDT;

	@Transient
	private String appDob;
	
	@Column(name="app_doc_pick_up_check")
	private Integer appDocPickupCheck;

	@Temporal(TemporalType.DATE)
	@Column(name="app_doc_pickup_date")
	private Date appDocPickupDateDT;

	@Transient
	private String appDocPickupDate;
	
	@Column(name="app_doc_pickup_time")
	private Date appDocPickupTime;

	@Column(name="APP_DOC_PICKUP_TIME_STR")
	private String appDocPickupTimeString;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_docs_entered_time")
	private Date appDocsEnteredTime;

	@Column(name="app_download_pdf_file_name")
	private String appDownloadPdfFileName;

	@Column(name="app_edu_qualification_id")
	private Integer appEducationalQualificationId;

	@Column(name="APP_EMAIL_VERIFY_CODE")
	private String appEmailVerificationCode;
		
	@Column(name="APP_EMAIL_VERIFIED")
	private String appEmailVerified;

	@Column(name="app_employment_proof_id")
	private Integer appEmploymentProofId;

	@Column(name="app_employment_proof_name")
	private String appEmploymentProofName;

	@Column(name="APP_PROPERTY_PROOF_ID")
	private Integer appPropertyProofId;

	@Column(name="APP_PROPERTY_PROOF_NAME")
	private String appPropertyProofName;
	
	@Column(name="app_employment_type")
	private Integer appEmploymentType;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_entry_time")
	private Date appEntryTime;

	@Column(name="APP_EVER_DFLT_CREDIT_CARD")
	private String appEverDefaultedCreditCard;

	@Column(name="app_existing_relation_type_id")
	private String appExistingRelationTypeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_filled_at")
	private Date appFilledAt;

	@Column(name="app_first_name")
	private String appFirstName;

	@Column(name="app_fulfillment_group_id")
	private Integer appFulfillmentGroupId;

	@Column(name="app_gender")
	private String appGender;
	
	@Column(name="app_marital_status")
	private Integer appMaritalStatus;

	@Column(name="app_gold_ornaments")
	private Double appGoldOrnaments;

	@Column(name="app_identity_proof_id")
	private Integer appIdentityProofId;

	@Column(name="app_identity_proof_name")
	private String appIdentityProofName;

	@Column(name="app_immovable_property")
	private Double appImmovableProperty;

	@Column(name="app_income_proof_id")
	private Integer appIncomeProofId;

	@Column(name="app_income_proof_name")
	private String appIncomeProofName;
	
	@Column(name="APP_PENSION_PROOF_ID")
	private Integer appPensionProofId;
	
	@Column(name="APP_PENSION_PROOF_NAME")
	private String appPensionProofName;

	@Column(name="app_intermediary_id")
	private Integer appIntermediaryId;

	@Column(name="app_is_existing_bank_id")
	private Integer appIsExistingBankId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_joining_date")
	private Date appJoiningDate;

	@Column(name="app_last_name")
	private String appLastName;

	@Column(name="app_lead_update_time")
	private Date appLeadUpdateTime;

	@Column(name="app_loan_account_type")
	private Integer appLoanAccountType;

	@Column(name="app_loan_amount")
	private Double appLoanAmount;

	@Column(name="app_loan_applied_coupon")
	private String appLoanAppliedCoupon;

	@Column(name="app_loan_emi")
	private Double appLoanEmi;

	@Column(name="app_loan_employer_name")
	private String appLoanEmployerName;

	@Column(name="app_loan_interest_rate")
	private Float appLoanInterestRate;

	@Column(name="app_loan_interest_rate_type")
	private Integer appLoanInterestRateType;

	@Column(name="app_loan_max_amount")
	private Double appLoanMaxAmount;

	@Column(name="APP_LOAN_INTEREST_RATE_DISC")
	private Float appLoanInterestRateDiscount;

	@Column(name="APP_LOAN_PROCESSING_FEE_DICS")
	private Double appLoanProcessingFeeDiscount;

	@Column(name="APP_LOAN_EMI_DISC")
	private Double appLoanEmiDiscount;
	
	@Column(name="app_loan_processing_fee")
	private Double appLoanProcessingFee;

	@Column(name="app_loan_status_id")
	private Integer appLoanStatusId;

	@Column(name="app_loan_tenure")
	private Integer appLoanTenure;

	@Column(name="app_market_value")
	private Integer appMarketValue;

	@Column(name="app_middle_name")
	private String appMiddleName;

	@Column(name="app_mobile_no")
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
	
	@Column(name="app_mode_of_repayment")
	private Integer appModeOfRepayment;

	@Column(name="app_mutual_fund_units")
	private Double appMutualFundUnits;

	@Column(name="APP_NON_OTP_EML_ALT_COUNT")
	private Integer appNonOtpEmailAlertsCount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="NON_OTP_EML_ALERTS_LAST_TM")
	private Date appNonOtpEmailAlertsLastTime;

	@Column(name="app_nscs")
	private Double appNscs;

	@Lob
	@Column(name="app_offer_json_data")
	private String appOfferJsonData;

	@Column(name="app_office_address1")
	private String appOfficeAddress1;

	@Column(name="app_office_address2")
	private String appOfficeAddress2;

	@Column(name="app_office_state_id")
	private Integer appOfficeStateId;
	
	@Column(name="app_office_city_id")
	private Integer appOfficeCityId;

	@Column(name="app_office_district_id")
	private Integer appOfficeDistrictId;

	@Column(name="app_office_branch_id")
	private Integer appOfficeBranchId;

	@Column(name="app_office_phone")
	private Long appOfficePhone;

	@Column(name="app_office_pincode")
	private Integer appOfficePincode;
	
	@Column(name="app_office_phone_std_code")
	private Integer appOfficePhoneStdCode;
	
	@Column(name="app_other_assets")
	private Double appOtherAssets;

	@Column(name="app_otp_mobilel_alerts_count")
	private Integer appOtpMobileAlertsCount;

	@Column(name="app_pan_card_no")
	private String appPanCardNo;

	@Column(name="app_pan_card_verify")
	private String appPanCardVerified;

	@Column(name="APP_PAN_CARD_LATER")
	private Boolean appPanCardLater;
	
	@Column(name="app_pan_updated_datetime")
	private Integer appPanUpdatedDatetime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_pan_verify_datetime")
	private Date appPanVerifiedDatetime;

	@Column(name="APP_PERMANENT_ADD_LAND_MARK")
	private String appPermanentAddressLandMark;

	@Column(name="app_permanent_address1")
	private String appPermanentAddress1;

	@Column(name="app_permanent_address2")
	private String appPermanentAddress2;

	@Column(name="app_permanent_state")
	private Integer appPermanentStateId;
	
	@Column(name="app_permanent_city")
	private Integer appPermanentCityId;
	
	@Column(name="app_permanent_district_id")
	private Integer appPermanentDistrictId;

	@Column(name="app_permanent_pincode")
	private Integer appPermanentPincode;

	@Column(name="APP_PRODUCT_VARIANT_ID")
	private Integer appPersonalLoanId;

	@Column(name="app_pf_or_ppf")
	private Double appPfOrPpf;

	@Column(name="app_photo_id")
	private Integer appPhotoId;

	@Column(name="app_photo_id_name")
	private String appPhotoIdName;

	@Column(name="app_pickup_address_1")
	private String appPickupAddress1;

	@Column(name="app_pickup_address_2")
	private String appPickupAddress2;

	@Column(name="app_pickup_city_id")
	private Integer appPickupCityId;
	
	@Column(name="APP_PICKUP_DISTRICT_ID")
	private Integer appPickupDistrictId;
	
	@Column(name="app_pickup_pincode")
	private Integer appPickupPincode;

	@Column(name="app_pickup_state_id")
	private Integer appPickupStateId;

	@Column(name="app_pincode")
	private Integer appPincode;

	@Column(name="app_quote_id")
	private Integer appQuoteId;

	@Column(name="app_reference_id")
	private String appReferenceId;

	@Column(name="app_relationship_with_bank")
	private Integer appRelationshipWithBank;

	@Column(name="app_residence_proof_id")
	private Integer appResidenceProofId;

	@Column(name="app_residence_proof_name")
	private String appResidenceProofName;

	@Column(name="app_residence_type")
	private Integer appResidenceType;

	@Column(name="app_salary_acc_no")
	private String appSalaryAccNo;

	@Column(name="app_salary_bank_id")
	private Integer appSalaryBankId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="app_sales_entered_tm")
	private Date appSalesEnteredTime;

	@Column(name="app_shares_or_debentures")
	private Double appSharesOrDebentures;

	@Column(name="app_state_id")
	private Integer appStateId;

	@Column(name="app_work_email")
	private String appWorkEmail;

	@Column(name="app_work_experience")
	private Integer appWorkExperience;
	
	@Column(name="app_work_experience_year")
	private Integer appWorkExperienceYear;
	
	@Column(name="app_work_experience_month")
	private Integer appWorkExperienceMonth;
	
	@Column(name="APP_CLONE_COAPP_ADD")
	private boolean cloneCoapplicantAddress1;
	
	@Column(name="APP_CLONE_PERMA_ADDRESS")
	private boolean clonePermanentAddress;
	
	@Transient 
	Integer appCompanyJoiningMonth;
		
	@Transient 
	Integer appCompanyJoiningYear;
	
	@Column(name="APP_RSM_RESPONSE")
	private String appRSMResponse;
	
	@Column(name="APP_RSM_STATUS")
	private Integer appRSMStatus;
	
	@Column(name="APP_RSM_SCORE")
	private float appRSMScore;
	
	@Column(name="APP_RSM_DECISION")
	private Integer appRSMdecision;
	
	@Column(name="APP_RSM_GRADE")
	private Integer appRSMGrade;
	
	@Column(name="APP_NO_OF_DEPENDENT")
	private Integer appNoOfDependent;
	
	@Column(name="APP_PRODUCT_TENURE_FLAG")
	private Integer appProductTenureFlag;
	
	@Column(name="APP_EMPLOYMENT_TYPE_ID")
	private Integer appEmploymentTypeId;
	
	@Column(name="app_industry_type_id")
	private Integer appIndustryTypeId;
	
	@Column(name="APP_ANNUAL_INCOME")
	private Double appNetAnnualIncome;
	
	@Column(name="APP_PRE_EMIS")
	private Double appPreEmis;
	
	@Column(name="APP_NRI_ADDRESS_1")
	private String appNRIAddress1;

	@Column(name="APP_NRI_ADDRESS_2")
	private String appNRIAddress2;

	@Column(name="APP_NRI_ADDRESS_LANDMARK")
	private String appNRIAddressLandmark;

	@Column(name="APP_COUNTRY_ID")
	private Integer appCountryId;
	
	@Column(name="APP_NRI_STATE")
	private String appNRIState;

	@Column(name="APP_NRI_CITY")
	private String appNRICity;
	
	@Column(name="APP_NRI_ZIPCODE")
	private String appNRIZipcode;
	
	@Column(name="APP_EMP_NRI_ADDRESS_1")
	private String appEMPNRIAddress1;

	@Column(name="APP_EMP_NRI_ADDRESS_2")
	private String appEMPNRIAddress2;

	@Column(name="APP_EMP_NRI_ADDRESS_LANDMARK")
	private String appEMPNRIAddressLandmark;

	@Column(name="APP_EMP_COUNTRY_ID")
	private Integer appEMPCountryId;
	
	@Column(name="APP_EMP_NRI_STATE")
	private String appEMPNRIState;

	@Column(name="APP_EMP_NRI_CITY")
	private String appEMPNRICity;
	
	@Column(name="APP_EMP_NRI_ZIPCODE")
	private String appEMPNRIZipcode;
	
	@Column(name="APP_HOT_LEAD_CREATED_USER_ID")
	private Integer appHotLeadCreatedLmsUserId;
	
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
	
	@Column(name="APP_OTP_VERIFY_TYPE")
	private int appOTPVerifyType;
	
	@Column(name="APP_EKYC_REQUEST")
	private String appEkycRequest;
	
	@Column(name="APP_EKYC_RESPONSE")
	private String appEkycResponse;
	
	@Column(name="APP_OTHER_ID_NUMBER")
	private String appOtherIdNumber;
	
	@Column(name="APP_OTHER_ID")
	private Integer appOtherId;
	
	@Column(name="APP_CIBIL_SCORE")
	private Integer appCibilScore;

	@Column(name="APP_APP_SUB_TYPE_ID")
	private Integer appSubTypeId;
	
	@Column(name="APP_RESIDENCE_TYPE_ID")
	private Integer appResidenceTypeId;
	
	@Column(name="APP_MOBILE_DEVICE_ID")
	private Integer appMobileDeviceId;
	
	@Column(name="APP_SOCIAL_MEDIA_ID")
	private Integer appSocialMediaId;
	
	@Lob
	@Column(name="APP_CUSTOMER_SIGNATURE")
	private String appCustomerSignature;
	
	@Column(name="APP_CRM_LEAD_ID")
	private Integer appCRMLeadId;

	@Column(name="APP_IS_SECURED_LOAN")
	private Integer appIsSecuredLoan;
	
	@Column(name="APP_PENSION_PAY_ORD_NUMBER")
	private String appPensionPayOrdNumber;
	
	@Column(name="APP_PENSION_PAYING_STATE_ID")
	private Integer appPensionPayingStateId;
	
	@Column(name="APP_PENSION_PAYING_CITY_ID")
	private Integer appPensionPayingCityId;
	
	@Column(name="APP_PENSION_PAYING_DISTR_ID")
	private Integer appPensionPayingDistrId;
	
	@Column(name="APP_PENSION_PAYING_BRANCH_ID")
	private Integer appPensionPayingBranchId;
	
	@Column(name="APP_PREFERRED_LOAN_BRANCH")
	private Integer appPreferredLoanBranch;
	
	@Column(name="APP_PREFERRED_STATE_ID")
	private Integer appPreferredStateId;
	
	@Column(name="APP_PREFERRED_CITY_ID")
	private Integer appPreferredCityId;
	
	@Column(name="APP_PREFERRED_DISTRICT_ID")
	private Integer appPreferredDistrictId;
	
	@Column(name="APP_PENSION_TYPE")
	private Integer appPensionType;
	
	@Column(name="APP_MONTHLY_PENSION_AMT")
	private Integer appMonthlyPensionAmt;

	@Column(name="APP_EMPLOYMENT_NATURE")
	private Integer appEmploymentNature;
	
	@Column(name="APP_PREFERRED_LOAN_BRANCH_ID")
	private Integer appPreferredLoanBranchId;	
		
	@Column(name="APP_PENSION_ACCOUNT_NUMBER")
	private String appPensionAccountNumber;

	@Column(name="APP_ORG_NAME")
	private String appOrgName;
	
	@Transient
	private String appRequestData;
	
	@Column(name="APP_OTP_ATTEMPT_COUNT")
	private Integer appOTPAttemptCount;
	
	@Transient
	private Boolean appCallRSMService;
	
	@Column(name="APP_RES_TYPE_AT_VERIFIED")
	private Integer appResTypeAtVerified;
	
	@Column(name="APP_IL_ACCOUNT_NUMBER")
	private String appInstantLoanAccountNumber;
	
	@Column(name="APP_IDSPM_DUMP_ID")
	private Integer appIDSPMDumpId;
	
	@Column(name="APP_CUSTOMER_UNIQUE_ID")
	private String appCustomerUniqueId;
	
	@Column(name="APP_INSTANT_LOAN_STAGE_ID")
	private Integer appInstantLoanStageId;
	
	@Lob
	@Column(name="APP_INSTANT_LOAN_REQUEST")
	private String appInstantLoanRequest;
	
	@Column(name="APP_INSTANT_LOAN_RESPONSE")
	private String appInstantLoanResponse;
	
	@Column(name="APP_DEMAND_LOAN_ACC_REF")
	private String appDemandLoanAccontReference;
	
	@Column(name="APP_DEMAND_LOAN_ACC_NO")
	private String appDemandLoanAccontNumber;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="APP_OTP_LAST_ATTEMPT_TIME")
	private Date appOTPLastAttemptTime;
	
	@Temporal(TemporalType.DATE)
	@Column(name="APP_ENTRY_DATE")
	private Date appEntryDate;
	
	@Column(name="APP_INSTANT_LOAN_OTP_CONSCENT")
	private Integer appInstantLoanOTPConscent;
	
	@Column(name="APP_INSTANT_LOAN_EULA_CONSCENT")
	private Integer appInstantLoanEULAConscent;
	
	@Column(name="APP_INSTANT_LOAN_APP_CONSCENT")
	private Integer appInstantLoanApplyConscent;
	
	@Column(name="APP_EMAIL_ATTEMPT_COUNT")
	private Integer appEmailAttemptCount;
	
	@Transient 
	Integer productId;
	
	@Column(name="APP_EMPLOYER_LANDMARK")
	private String appEmployerLandmark;
	
	@Column(name="APP_AIP_MAIL_SEND_STATUS")
	private String appAipMailSendStatus;
	
	@Column(name="APP_HAVE_AADHAAR_NUMBER")
	private Integer appHaveAadhaarNumber;
	
	@Column(name="APP_HAVE_CO_OPERATIVE_BAK_AC")
	private Integer appHaveCoOperativeBankAcc;
	
	@Column(name="APP_CBS_RELATIONSHIP_ID")
	private Integer appCbsRelationShipId;
	
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
	
	@Column(name = "CVE_PRODUCT_CATEGORY")
  private String cveProductCategory;
  
  @Column(name = "CVE_SALUTATION")
  private String cveSalutation;
  
  @Column(name = "CVE_APP_FIRST_NAME")
  private String cveAppFirstName;
  
  @Column(name = "CVE_APP_MIDDLE_NAME")
  private String cveAppMiddleName;
  
  @Column(name = "CVE_APP_LAST_NAME")
  private String cveAppLastName;
  
  @Column(name = "CVE_APP_PREV_BRANCH_ID")
  private String cveAppPrevBranchId;
  
  @Column(name = "CVE_APP_EMAIL")
  private String cveAppEmail;
  
  @Column(name = "CVE_APP_CONSENT_REVOKE")
  private String cveAppConsentRevoke;
  
  @Column(name = "CVE_APP_CONSENT_REVOKE_YES")
  private String cveAppConsentRevokeYes;
  
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
	
	public Integer getAppSeqId() {
		return appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public String getAppActive() {
		return appActive;
	}

	public void setAppActive(String appActive) {
		this.appActive = appActive;
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

	public Double getAppBankDeposit() {
		return appBankDeposit;
	}

	public void setAppBankDeposit(Double appBankDeposit) {
		this.appBankDeposit = appBankDeposit;
	}

	public Double getAppExistingTotalLoanAmount() {
		return appExistingTotalLoanAmount;
	}

	public void setAppExistingTotalLoanAmount(Double appExistingTotalLoanAmount) {
		this.appExistingTotalLoanAmount = appExistingTotalLoanAmount;
	}

	public Integer getAppBankId() {
		return appBankId;
	}

	public void setAppBankId(Integer appBankId) {
		this.appBankId = appBankId;
	}

	public Integer getAppBranchId() {
		return appBranchId;
	}

	public void setAppBranchId(Integer appBranchId) {
		this.appBranchId = appBranchId;
	}

	public Integer getAppCityId() {
		return appCityId;
	}

	public void setAppCityId(Integer appCityId) {
		this.appCityId = appCityId;
	}

	public Integer getAppLocalityId() {
		return appLocalityId;
	}

	public void setAppLocalityId(Integer appLocalityId) {
		this.appLocalityId = appLocalityId;
	}

	public Integer getAppDistrictId() {
		return appDistrictId;
	}

	public void setAppDistrictId(Integer appDistrictId) {
		this.appDistrictId = appDistrictId;
	}

	public Byte getAppLoanType() {
		return appLoanType;
	}

	public void setAppLoanType(Byte appLoanType) {
		this.appLoanType = appLoanType;
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

	public Double getAppCoapplicantBankDeposit() {
		return appCoapplicantBankDeposit;
	}

	public void setAppCoapplicantBankDeposit(Double appCoapplicantBankDeposit) {
		this.appCoapplicantBankDeposit = appCoapplicantBankDeposit;
	}

	public Integer getAppCoapplicantCityId() {
		return appCoapplicantCityId;
	}

	public void setAppCoapplicantCityId(Integer appCoapplicantCityId) {
		this.appCoapplicantCityId = appCoapplicantCityId;
	}

	public String getAppCoapplicantEmployerName1() {
		return appCoapplicantEmployerName1;
	}

	public void setAppCoapplicantEmployerName1(String appCoapplicantEmployerName1) {
		this.appCoapplicantEmployerName1 = appCoapplicantEmployerName1;
	}

	public Integer getAppCoapplicantEmploymentTypeId() {
		return appCoapplicantEmploymentTypeId;
	}

	public void setAppCoapplicantEmploymentTypeId(
			Integer appCoapplicantEmploymentTypeId) {
		this.appCoapplicantEmploymentTypeId = appCoapplicantEmploymentTypeId;
	}

	public String getAppCoapplicantFirstName() {
		return appCoapplicantFirstName;
	}

	public void setAppCoapplicantFirstName(String appCoapplicantFirstName) {
		this.appCoapplicantFirstName = appCoapplicantFirstName;
	}

	public Double getAppCoapplicantGoldOrnaments() {
		return appCoapplicantGoldOrnaments;
	}

	public void setAppCoapplicantGoldOrnaments(Double appCoapplicantGoldOrnaments) {
		this.appCoapplicantGoldOrnaments = appCoapplicantGoldOrnaments;
	}

	public Double getAppCoapplicantImmovableProperty() {
		return appCoapplicantImmovableProperty;
	}

	public void setAppCoapplicantImmovableProperty(
			Double appCoapplicantImmovableProperty) {
		this.appCoapplicantImmovableProperty = appCoapplicantImmovableProperty;
	}

	public String getAppCoapplicantIsCoborrower() {
		return appCoapplicantIsCoborrower;
	}

	public void setAppCoapplicantIsCoborrower(String appCoapplicantIsCoborrower) {
		this.appCoapplicantIsCoborrower = appCoapplicantIsCoborrower;
	}

	public String getAppCoapplicantLandmark() {
		return appCoapplicantLandmark;
	}

	public void setAppCoapplicantLandmark(String appCoapplicantLandmark) {
		this.appCoapplicantLandmark = appCoapplicantLandmark;
	}

	public String getAppCoapplicantLastName() {
		return appCoapplicantLastName;
	}

	public void setAppCoapplicantLastName(String appCoapplicantLastName) {
		this.appCoapplicantLastName = appCoapplicantLastName;
	}

	public String getAppCoapplicantMiddleName() {
		return appCoapplicantMiddleName;
	}

	public void setAppCoapplicantMiddleName(String appCoapplicantMiddleName) {
		this.appCoapplicantMiddleName = appCoapplicantMiddleName;
	}

	public Double getAppCoapplicantMutualFundUnits() {
		return appCoapplicantMutualFundUnits;
	}

	public void setAppCoapplicantMutualFundUnits(
			Double appCoapplicantMutualFundUnits) {
		this.appCoapplicantMutualFundUnits = appCoapplicantMutualFundUnits;
	}

	public Double getAppCoapplicantNscs() {
		return appCoapplicantNscs;
	}

	public void setAppCoapplicantNscs(Double appCoapplicantNscs) {
		this.appCoapplicantNscs = appCoapplicantNscs;
	}

	public Double getAppCoapplicantOtherAssets() {
		return appCoapplicantOtherAssets;
	}

	public void setAppCoapplicantOtherAssets(Double appCoapplicantOtherAssets) {
		this.appCoapplicantOtherAssets = appCoapplicantOtherAssets;
	}

	public String getAppCoapplicantPanCardNo() {
		return appCoapplicantPanCardNo;
	}

	public void setAppCoapplicantPanCardNo(String appCoapplicantPanCardNo) {
		this.appCoapplicantPanCardNo = appCoapplicantPanCardNo;
	}

	public Double getAppCoapplicantPfOrPpf() {
		return appCoapplicantPfOrPpf;
	}

	public void setAppCoapplicantPfOrPpf(Double appCoapplicantPfOrPpf) {
		this.appCoapplicantPfOrPpf = appCoapplicantPfOrPpf;
	}

	public Integer getAppCoapplicantPincode() {
		return appCoapplicantPincode;
	}

	public void setAppCoapplicantPincode(Integer appCoapplicantPincode) {
		this.appCoapplicantPincode = appCoapplicantPincode;
	}

	public Integer getAppCoapplicantRelationTypeId() {
		return appCoapplicantRelationTypeId;
	}

	public void setAppCoapplicantRelationTypeId(Integer appCoapplicantRelationTypeId) {
		this.appCoapplicantRelationTypeId = appCoapplicantRelationTypeId;
	}

	public Integer getAppCoapplicantResidenceType() {
		return appCoapplicantResidenceType;
	}

	public void setAppCoapplicantResidenceType(Integer appCoapplicantResidenceType) {
		this.appCoapplicantResidenceType = appCoapplicantResidenceType;
	}

	public Double getAppCoapplicantSharesOrDebentures() {
		return appCoapplicantSharesOrDebentures;
	}

	public void setAppCoapplicantSharesOrDebentures(
			Double appCoapplicantSharesOrDebentures) {
		this.appCoapplicantSharesOrDebentures = appCoapplicantSharesOrDebentures;
	}

	public Double getAppCoapplicantExistingTotalLoanAmount() {
		return appCoapplicantExistingTotalLoanAmount;
	}

	public void setAppCoapplicantExistingTotalLoanAmount(
			Double appCoapplicantExistingTotalLoanAmount) {
		this.appCoapplicantExistingTotalLoanAmount = appCoapplicantExistingTotalLoanAmount;
	}

	public Integer getAppCoapplicantStateId() {
		return appCoapplicantStateId;
	}

	public void setAppCoapplicantStateId(Integer appCoapplicantStateId) {
		this.appCoapplicantStateId = appCoapplicantStateId;
	}

	public Integer getAppCompanyCategoryId() {
		return appCompanyCategoryId;
	}

	public void setAppCompanyCategoryId(Integer appCompanyCategoryId) {
		this.appCompanyCategoryId = appCompanyCategoryId;
	}

	public Date getAppCompanyJoiningDate() {
		return appCompanyJoiningDate;
	}

	public void setAppCompanyJoiningDate(Date appCompanyJoiningDate) {
		this.appCompanyJoiningDate = appCompanyJoiningDate;
	}

	public Integer getAppCreatedLmsUserId() {
		return appCreatedLmsUserId;
	}

	public void setAppCreatedLmsUserId(Integer appCreatedLmsUserId) {
		this.appCreatedLmsUserId = appCreatedLmsUserId;
	}

	public Integer getAppDataSourceId() {
		return appDataSourceId;
	}

	public void setAppDataSourceId(Integer appDataSourceId) {
		this.appDataSourceId = appDataSourceId;
	}

	public String getAppDeleted() {
		return appDeleted;
	}

	public void setAppDeleted(String appDeleted) {
		this.appDeleted = appDeleted;
	}

	public Integer getAppDocPickupCheck() {
		return appDocPickupCheck;
	}

	public void setAppDocPickupCheck(Integer appDocPickupCheck) {
		this.appDocPickupCheck = appDocPickupCheck;
	}

	public Date getAppDocPickupTime() {
		return appDocPickupTime;
	}

	public void setAppDocPickupTime(Date appDocPickupTime) {
		this.appDocPickupTime = appDocPickupTime;
	}

	public Date getAppDocsEnteredTime() {
		return appDocsEnteredTime;
	}

	public void setAppDocsEnteredTime(Date appDocsEnteredTime) {
		this.appDocsEnteredTime = appDocsEnteredTime;
	}

	public String getAppDownloadPdfFileName() {
		return appDownloadPdfFileName;
	}

	public void setAppDownloadPdfFileName(String appDownloadPdfFileName) {
		this.appDownloadPdfFileName = appDownloadPdfFileName;
	}

	public Integer getAppEducationalQualificationId() {
		return appEducationalQualificationId;
	}

	public void setAppEducationalQualificationId(
			Integer appEducationalQualificationId) {
		this.appEducationalQualificationId = appEducationalQualificationId;
	}

	public String getAppEmailVerified() {
		return appEmailVerified;
	}

	public void setAppEmailVerified(String appEmailVerified) {
		this.appEmailVerified = appEmailVerified;
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

	public Integer getAppEmploymentType() {
		return appEmploymentType;
	}

	public void setAppEmploymentType(Integer appEmploymentType) {
		this.appEmploymentType = appEmploymentType;
	}

	public Date getAppEntryTime() {
		return appEntryTime;
	}

	public void setAppEntryTime(Date appEntryTime) {
		this.appEntryTime = appEntryTime;
	}

	public String getAppEverDefaultedCreditCard() {
		return appEverDefaultedCreditCard;
	}

	public void setAppEverDefaultedCreditCard(String appEverDefaultedCreditCard) {
		this.appEverDefaultedCreditCard = appEverDefaultedCreditCard;
	}

	public String getAppExistingRelationTypeId() {
		return appExistingRelationTypeId;
	}

	public void setAppExistingRelationTypeId(String appExistingRelationTypeId) {
		this.appExistingRelationTypeId = appExistingRelationTypeId;
	}

	public Date getAppFilledAt() {
		return appFilledAt;
	}

	public void setAppFilledAt(Date appFilledAt) {
		this.appFilledAt = appFilledAt;
	}

	public String getAppFirstName() {
		return appFirstName;
	}

	public void setAppFirstName(String appFirstName) {
		this.appFirstName = appFirstName;
	}

	public Integer getAppFulfillmentGroupId() {
		return appFulfillmentGroupId;
	}

	public void setAppFulfillmentGroupId(Integer appFulfillmentGroupId) {
		this.appFulfillmentGroupId = appFulfillmentGroupId;
	}

	public String getAppGender() {
		return appGender;
	}

	public void setAppGender(String appGender) {
		this.appGender = appGender;
	}

	public Double getAppGoldOrnaments() {
		return appGoldOrnaments;
	}

	public void setAppGoldOrnaments(Double appGoldOrnaments) {
		this.appGoldOrnaments = appGoldOrnaments;
	}

	public Integer getAppHotLeadCreatedLmsUserId() {
		return appHotLeadCreatedLmsUserId;
	}

	public void setAppHotLeadCreatedLmsUserId(Integer appHotLeadCreatedLmsUserId) {
		this.appHotLeadCreatedLmsUserId = appHotLeadCreatedLmsUserId;
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

	public Double getAppImmovableProperty() {
		return appImmovableProperty;
	}

	public void setAppImmovableProperty(Double appImmovableProperty) {
		this.appImmovableProperty = appImmovableProperty;
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

	public Integer getAppIntermediaryId() {
		return appIntermediaryId;
	}

	public void setAppIntermediaryId(Integer appIntermediaryId) {
		this.appIntermediaryId = appIntermediaryId;
	}

	public Integer getAppIsExistingBankId() {
		return appIsExistingBankId;
	}

	public void setAppIsExistingBankId(Integer appIsExistingBankId) {
		this.appIsExistingBankId = appIsExistingBankId;
	}

	public Date getAppJoiningDate() {
		return appJoiningDate;
	}

	public void setAppJoiningDate(Date appJoiningDate) {
		this.appJoiningDate = appJoiningDate;
	}

	public String getAppLastName() {
		return appLastName;
	}

	public void setAppLastName(String appLastName) {
		this.appLastName = appLastName;
	}

	public Date getAppLeadUpdateTime() {
		return appLeadUpdateTime;
	}

	public void setAppLeadUpdateTime(Date appLeadUpdateTime) {
		this.appLeadUpdateTime = appLeadUpdateTime;
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

	public String getAppLoanAppliedCoupon() {
		return appLoanAppliedCoupon;
	}

	public void setAppLoanAppliedCoupon(String appLoanAppliedCoupon) {
		this.appLoanAppliedCoupon = appLoanAppliedCoupon;
	}

	public Double getAppLoanEmi() {
		return appLoanEmi;
	}

	public void setAppLoanEmi(Double appLoanEmi) {
		this.appLoanEmi = appLoanEmi;
	}

	public String getAppLoanEmployerName() {
		return appLoanEmployerName;
	}

	public void setAppLoanEmployerName(String appLoanEmployerName) {
		this.appLoanEmployerName = appLoanEmployerName;
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

	public Double getAppLoanMaxAmount() {
		return appLoanMaxAmount;
	}

	public void setAppLoanMaxAmount(Double appLoanMaxAmount) {
		this.appLoanMaxAmount = appLoanMaxAmount;
	}

	public Double getAppLoanProcessingFee() {
		return appLoanProcessingFee;
	}

	public void setAppLoanProcessingFee(Double appLoanProcessingFee) {
		this.appLoanProcessingFee = appLoanProcessingFee;
	}

	public Integer getAppLoanStatusId() {
		return appLoanStatusId;
	}

	public void setAppLoanStatusId(Integer appLoanStatusId) {
		this.appLoanStatusId = appLoanStatusId;
	}

	public Integer getAppLoanTenure() {
		return appLoanTenure;
	}

	public void setAppLoanTenure(Integer appLoanTenure) {
		this.appLoanTenure = appLoanTenure;
	}

	public Integer getAppMarketValue() {
		return appMarketValue;
	}

	public void setAppMarketValue(Integer appMarketValue) {
		this.appMarketValue = appMarketValue;
	}

	public String getAppMiddleName() {
		return appMiddleName;
	}

	public void setAppMiddleName(String appMiddleName) {
		this.appMiddleName = appMiddleName;
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

	public Integer getAppModeOfRepayment() {
		return appModeOfRepayment;
	}

	public void setAppModeOfRepayment(Integer appModeOfRepayment) {
		this.appModeOfRepayment = appModeOfRepayment;
	}

	public Double getAppMutualFundUnits() {
		return appMutualFundUnits;
	}

	public void setAppMutualFundUnits(Double appMutualFundUnits) {
		this.appMutualFundUnits = appMutualFundUnits;
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

	public Double getAppNscs() {
		return appNscs;
	}

	public void setAppNscs(Double appNscs) {
		this.appNscs = appNscs;
	}

	public String getAppOfferJsonData() {
		return appOfferJsonData;
	}

	public void setAppOfferJsonData(String appOfferJsonData) {
		this.appOfferJsonData = appOfferJsonData;
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

	public Long getAppOfficePhone() {
		return appOfficePhone;
	}

	public void setAppOfficePhone(Long appOfficePhone) {
		this.appOfficePhone = appOfficePhone;
	}

	public Integer getAppOfficePincode() {
		return appOfficePincode;
	}

	public void setAppOfficePincode(Integer appOfficePincode) {
		this.appOfficePincode = appOfficePincode;
	}

	public Integer getAppOfficeStateId() {
		return appOfficeStateId;
	}

	public void setAppOfficeStateId(Integer appOfficeStateId) {
		this.appOfficeStateId = appOfficeStateId;
	}

	public Double getAppOtherAssets() {
		return appOtherAssets;
	}

	public void setAppOtherAssets(Double appOtherAssets) {
		this.appOtherAssets = appOtherAssets;
	}

	public Integer getAppOtpMobileAlertsCount() {
		return appOtpMobileAlertsCount;
	}

	public void setAppOtpMobileAlertsCount(Integer appOtpMobileAlertsCount) {
		this.appOtpMobileAlertsCount = appOtpMobileAlertsCount;
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

	public Integer getAppPermanentStateId() {
		return appPermanentStateId;
	}

	public void setAppPermanentStateId(Integer appPermanentStateId) {
		this.appPermanentStateId = appPermanentStateId;
	}

	public Integer getAppPersonalLoanId() {
		return appPersonalLoanId;
	}

	public void setAppPersonalLoanId(Integer appPersonalLoanId) {
		this.appPersonalLoanId = appPersonalLoanId;
	}

	public Double getAppPfOrPpf() {
		return appPfOrPpf;
	}

	public void setAppPfOrPpf(Double appPfOrPpf) {
		this.appPfOrPpf = appPfOrPpf;
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

	public Integer getAppPickupStateId() {
		return appPickupStateId;
	}

	public void setAppPickupStateId(Integer appPickupStateId) {
		this.appPickupStateId = appPickupStateId;
	}

	public Integer getAppPincode() {
		return appPincode;
	}

	public void setAppPincode(Integer appPincode) {
		this.appPincode = appPincode;
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

	public Integer getAppRelationshipWithBank() {
		return appRelationshipWithBank;
	}

	public void setAppRelationshipWithBank(Integer appRelationshipWithBank) {
		this.appRelationshipWithBank = appRelationshipWithBank;
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

	public Integer getAppResidenceType() {
		return appResidenceType;
	}

	public void setAppResidenceType(Integer appResidenceType) {
		this.appResidenceType = appResidenceType;
	}

	public String getAppSalaryAccNo() {
		return appSalaryAccNo;
	}

	public void setAppSalaryAccNo(String appSalaryAccNo) {
		this.appSalaryAccNo = appSalaryAccNo;
	}

	public Integer getAppSalaryBankId() {
		return appSalaryBankId;
	}

	public void setAppSalaryBankId(Integer appSalaryBankId) {
		this.appSalaryBankId = appSalaryBankId;
	}

	public Date getAppSalesEnteredTime() {
		return appSalesEnteredTime;
	}

	public void setAppSalesEnteredTime(Date appSalesEnteredTime) {
		this.appSalesEnteredTime = appSalesEnteredTime;
	}

	public Double getAppSharesOrDebentures() {
		return appSharesOrDebentures;
	}

	public void setAppSharesOrDebentures(Double appSharesOrDebentures) {
		this.appSharesOrDebentures = appSharesOrDebentures;
	}

	public Integer getAppStateId() {
		return appStateId;
	}

	public void setAppStateId(Integer appStateId) {
		this.appStateId = appStateId;
	}

	public String getAppWorkEmail() {
		return appWorkEmail;
	}

	public void setAppWorkEmail(String appWorkEmail) {
		this.appWorkEmail = appWorkEmail;
	}

	public Integer getAppWorkExperience() {
		return appWorkExperience;
	}

	public void setAppWorkExperience(Integer appWorkExperience) {
		this.appWorkExperience = appWorkExperience;
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

	public Integer getAppNoOfDependent() {
		return appNoOfDependent;
	}

	public void setAppNoOfDependent(Integer appNoOfDependent) {
		this.appNoOfDependent = appNoOfDependent;
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

	public Integer getAppOfficeDistrictId() {
		return appOfficeDistrictId;
	}

	public void setAppOfficeDistrictId(Integer appOfficeDistrictId) {
		this.appOfficeDistrictId = appOfficeDistrictId;
	}

	public Integer getAppOfficeBranchId() {
		return appOfficeBranchId;
	}

	public void setAppOfficeBranchId(Integer appOfficeBranchId) {
		this.appOfficeBranchId = appOfficeBranchId;
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

	public Integer getAppOfficePhoneStdCode() {
		return appOfficePhoneStdCode;
	}

	public void setAppOfficePhoneStdCode(Integer appOfficePhoneStdCode) {
		this.appOfficePhoneStdCode = appOfficePhoneStdCode;
	}

	public Integer getAppProductTenureFlag() {
		return appProductTenureFlag;
	}

	public void setAppProductTenureFlag(Integer appProductTenureFlag) {
		this.appProductTenureFlag = appProductTenureFlag;
	}

	public String getAppCoapplicant2FirstName() {
		return appCoapplicant2FirstName;
	}

	public void setAppCoapplicant2FirstName(String appCoapplicant2FirstName) {
		this.appCoapplicant2FirstName = appCoapplicant2FirstName;
	}

	public String getAppCoapplicant2MiddleName() {
		return appCoapplicant2MiddleName;
	}

	public void setAppCoapplicant2MiddleName(String appCoapplicant2MiddleName) {
		this.appCoapplicant2MiddleName = appCoapplicant2MiddleName;
	}

	public String getAppCoapplicant2LastName() {
		return appCoapplicant2LastName;
	}

	public void setAppCoapplicant2LastName(String appCoapplicant2LastName) {
		this.appCoapplicant2LastName = appCoapplicant2LastName;
	}

	public Integer getAppCoapplicant2EmploymentTypeId() {
		return appCoapplicant2EmploymentTypeId;
	}

	public void setAppCoapplicant2EmploymentTypeId(
			Integer appCoapplicant2EmploymentTypeId) {
		this.appCoapplicant2EmploymentTypeId = appCoapplicant2EmploymentTypeId;
	}

	public Integer getAppCoapplicant2NetAnnualIncome() {
		return appCoapplicant2NetAnnualIncome;
	}

	public void setAppCoapplicant2NetAnnualIncome(
			Integer appCoapplicant2NetAnnualIncome) {
		this.appCoapplicant2NetAnnualIncome = appCoapplicant2NetAnnualIncome;
	}

	public Integer getAppCoapplicant2NoOfDependent() {
		return appCoapplicant2NoOfDependent;
	}

	public void setAppCoapplicant2NoOfDependent(Integer appCoapplicant2NoOfDependent) {
		this.appCoapplicant2NoOfDependent = appCoapplicant2NoOfDependent;
	}

	public Double getAppCoapplicant2ImmovableProperty() {
		return appCoapplicant2ImmovableProperty;
	}

	public void setAppCoapplicant2ImmovableProperty(
			Double appCoapplicant2ImmovableProperty) {
		this.appCoapplicant2ImmovableProperty = appCoapplicant2ImmovableProperty;
	}

	public Double getAppCoapplicant2BankDeposit() {
		return appCoapplicant2BankDeposit;
	}

	public void setAppCoapplicant2BankDeposit(Double appCoapplicant2BankDeposit) {
		this.appCoapplicant2BankDeposit = appCoapplicant2BankDeposit;
	}

	public Double getAppCoapplicant2Nscs() {
		return appCoapplicant2Nscs;
	}

	public void setAppCoapplicant2Nscs(Double appCoapplicant2Nscs) {
		this.appCoapplicant2Nscs = appCoapplicant2Nscs;
	}

	public Double getAppCoapplicant2PfOrPpf() {
		return appCoapplicant2PfOrPpf;
	}

	public void setAppCoapplicant2PfOrPpf(Double appCoapplicant2PfOrPpf) {
		this.appCoapplicant2PfOrPpf = appCoapplicant2PfOrPpf;
	}

	public Double getAppCoapplicant2GoldOrnaments() {
		return appCoapplicant2GoldOrnaments;
	}

	public void setAppCoapplicant2GoldOrnaments(Double appCoapplicant2GoldOrnaments) {
		this.appCoapplicant2GoldOrnaments = appCoapplicant2GoldOrnaments;
	}

	public Double getAppCoapplicant2MutualFundUnits() {
		return appCoapplicant2MutualFundUnits;
	}

	public void setAppCoapplicant2MutualFundUnits(
			Double appCoapplicant2MutualFundUnits) {
		this.appCoapplicant2MutualFundUnits = appCoapplicant2MutualFundUnits;
	}

	public Double getAppCoapplicant2ExistingTotalLoanAmount() {
		return appCoapplicant2ExistingTotalLoanAmount;
	}

	public void setAppCoapplicant2ExistingTotalLoanAmount(
			Double appCoapplicant2ExistingTotalLoanAmount) {
		this.appCoapplicant2ExistingTotalLoanAmount = appCoapplicant2ExistingTotalLoanAmount;
	}

	public String getAppCoapplicant3FirstName() {
		return appCoapplicant3FirstName;
	}

	public void setAppCoapplicant3FirstName(String appCoapplicant3FirstName) {
		this.appCoapplicant3FirstName = appCoapplicant3FirstName;
	}

	public String getAppCoapplicant3MiddleName() {
		return appCoapplicant3MiddleName;
	}

	public void setAppCoapplicant3MiddleName(String appCoapplicant3MiddleName) {
		this.appCoapplicant3MiddleName = appCoapplicant3MiddleName;
	}

	public String getAppCoapplicant3LastName() {
		return appCoapplicant3LastName;
	}

	public void setAppCoapplicant3LastName(String appCoapplicant3LastName) {
		this.appCoapplicant3LastName = appCoapplicant3LastName;
	}

	public Integer getAppCoapplicant3EmploymentTypeId() {
		return appCoapplicant3EmploymentTypeId;
	}

	public void setAppCoapplicant3EmploymentTypeId(
			Integer appCoapplicant3EmploymentTypeId) {
		this.appCoapplicant3EmploymentTypeId = appCoapplicant3EmploymentTypeId;
	}

	public Integer getAppCoapplicant3NetAnnualIncome() {
		return appCoapplicant3NetAnnualIncome;
	}

	public void setAppCoapplicant3NetAnnualIncome(
			Integer appCoapplicant3NetAnnualIncome) {
		this.appCoapplicant3NetAnnualIncome = appCoapplicant3NetAnnualIncome;
	}

	public Integer getAppCoapplicant3NoOfDependent() {
		return appCoapplicant3NoOfDependent;
	}

	public void setAppCoapplicant3NoOfDependent(Integer appCoapplicant3NoOfDependent) {
		this.appCoapplicant3NoOfDependent = appCoapplicant3NoOfDependent;
	}

	public Double getAppCoapplicant3ImmovableProperty() {
		return appCoapplicant3ImmovableProperty;
	}

	public void setAppCoapplicant3ImmovableProperty(
			Double appCoapplicant3ImmovableProperty) {
		this.appCoapplicant3ImmovableProperty = appCoapplicant3ImmovableProperty;
	}

	public Double getAppCoapplicant3BankDeposit() {
		return appCoapplicant3BankDeposit;
	}

	public void setAppCoapplicant3BankDeposit(Double appCoapplicant3BankDeposit) {
		this.appCoapplicant3BankDeposit = appCoapplicant3BankDeposit;
	}

	public Double getAppCoapplicant3Nscs() {
		return appCoapplicant3Nscs;
	}

	public void setAppCoapplicant3Nscs(Double appCoapplicant3Nscs) {
		this.appCoapplicant3Nscs = appCoapplicant3Nscs;
	}

	public Double getAppCoapplicant3PfOrPpf() {
		return appCoapplicant3PfOrPpf;
	}

	public void setAppCoapplicant3PfOrPpf(Double appCoapplicant3PfOrPpf) {
		this.appCoapplicant3PfOrPpf = appCoapplicant3PfOrPpf;
	}

	public Double getAppCoapplicant3GoldOrnaments() {
		return appCoapplicant3GoldOrnaments;
	}

	public void setAppCoapplicant3GoldOrnaments(Double appCoapplicant3GoldOrnaments) {
		this.appCoapplicant3GoldOrnaments = appCoapplicant3GoldOrnaments;
	}

	public Double getAppCoapplicant3MutualFundUnits() {
		return appCoapplicant3MutualFundUnits;
	}

	public void setAppCoapplicant3MutualFundUnits(
			Double appCoapplicant3MutualFundUnits) {
		this.appCoapplicant3MutualFundUnits = appCoapplicant3MutualFundUnits;
	}

	public Double getAppCoapplicant3ExistingTotalLoanAmount() {
		return appCoapplicant3ExistingTotalLoanAmount;
	}

	public void setAppCoapplicant3ExistingTotalLoanAmount(
			Double appCoapplicant3ExistingTotalLoanAmount) {
		this.appCoapplicant3ExistingTotalLoanAmount = appCoapplicant3ExistingTotalLoanAmount;
	}

	public Double getAppCoapplicantPreEmis() {
		return appCoapplicantPreEmis;
	}

	public void setAppCoapplicantPreEmis(Double appCoapplicantPreEmis) {
		this.appCoapplicantPreEmis = appCoapplicantPreEmis;
	}

	public Double getAppCoapplicant2PreEmis() {
		return appCoapplicant2PreEmis;
	}

	public void setAppCoapplicant2PreEmis(Double appCoapplicant2PreEmis) {
		this.appCoapplicant2PreEmis = appCoapplicant2PreEmis;
	}

	public Double getAppCoapplicant3PreEmis() {
		return appCoapplicant3PreEmis;
	}

	public void setAppCoapplicant3PreEmis(Double appCoapplicant3PreEmis) {
		this.appCoapplicant3PreEmis = appCoapplicant3PreEmis;
	}

	public Integer getAppCoapplicantNetAnnualIncome() {
		return appCoapplicantNetAnnualIncome;
	}

	public void setAppCoapplicantNetAnnualIncome(
			Integer appCoapplicantNetAnnualIncome) {
		this.appCoapplicantNetAnnualIncome = appCoapplicantNetAnnualIncome;
	}

	public Integer getAppCoapplicantNoOfDependent() {
		return appCoapplicantNoOfDependent;
	}

	public void setAppCoapplicantNoOfDependent(Integer appCoapplicantNoOfDependent) {
		this.appCoapplicantNoOfDependent = appCoapplicantNoOfDependent;
	}

	public Integer getAppEmploymentTypeId() {
		return appEmploymentTypeId;
	}

	public void setAppEmploymentTypeId(Integer appEmploymentTypeId) {
		this.appEmploymentTypeId = appEmploymentTypeId;
	}

	public Double getAppNetAnnualIncome() {
		return appNetAnnualIncome;
	}

	public void setAppNetAnnualIncome(Double appNetAnnualIncome) {
		this.appNetAnnualIncome = appNetAnnualIncome;
	}

	public Double getAppPreEmis() {
		return appPreEmis;
	}

	public void setAppPreEmis(Double appPreEmis) {
		this.appPreEmis = appPreEmis;
	}

	public Integer getAppCoapplicantIndustryTypeId() {
		return appCoapplicantIndustryTypeId;
	}

	public void setAppCoapplicantIndustryTypeId(Integer appCoapplicantIndustryTypeId) {
		this.appCoapplicantIndustryTypeId = appCoapplicantIndustryTypeId;
	}

	public Integer getAppCoapplicant2IndustryTypeId() {
		return appCoapplicant2IndustryTypeId;
	}

	public void setAppCoapplicant2IndustryTypeId(
			Integer appCoapplicant2IndustryTypeId) {
		this.appCoapplicant2IndustryTypeId = appCoapplicant2IndustryTypeId;
	}

	public Integer getAppCoapplicant3IndustryTypeId() {
		return appCoapplicant3IndustryTypeId;
	}

	public void setAppCoapplicant3IndustryTypeId(
			Integer appCoapplicant3IndustryTypeId) {
		this.appCoapplicant3IndustryTypeId = appCoapplicant3IndustryTypeId;
	}

	public Integer getAppIndustryTypeId() {
		return appIndustryTypeId;
	}

	public void setAppIndustryTypeId(Integer appIndustryTypeId) {
		this.appIndustryTypeId = appIndustryTypeId;
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

	public Boolean getAppPanCardLater() {
		return appPanCardLater;
	}

	public void setAppPanCardLater(Boolean appPanCardLater) {
		this.appPanCardLater = appPanCardLater;
	}

	public Integer getAppPropertyProofId() {
		return appPropertyProofId;
	}

	public void setAppPropertyProofId(Integer appPropertyProofId) {
		this.appPropertyProofId = appPropertyProofId;
	}

	public String getAppPropertyProofName() {
		return appPropertyProofName;
	}

	public void setAppPropertyProofName(String appPropertyProofName) {
		this.appPropertyProofName = appPropertyProofName;
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

	public Integer getAppResidenceTypeId() {
		return appResidenceTypeId;
	}

	public void setAppResidenceTypeId(Integer appResidenceTypeId) {
		this.appResidenceTypeId = appResidenceTypeId;
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

	public String getAppPensionPayOrdNumber() {
		return appPensionPayOrdNumber;
	}

	public void setAppPensionPayOrdNumber(String appPensionPayOrdNumber) {
		this.appPensionPayOrdNumber = appPensionPayOrdNumber;
	}

	public Integer getAppPensionPayingStateId() {
		return appPensionPayingStateId;
	}

	public void setAppPensionPayingStateId(Integer appPensionPayingStateId) {
		this.appPensionPayingStateId = appPensionPayingStateId;
	}

	public Integer getAppPensionPayingCityId() {
		return appPensionPayingCityId;
	}

	public void setAppPensionPayingCityId(Integer appPensionPayingCityId) {
		this.appPensionPayingCityId = appPensionPayingCityId;
	}

	public Integer getAppPensionPayingDistrId() {
		return appPensionPayingDistrId;
	}

	public void setAppPensionPayingDistrId(Integer appPensionPayingDistrId) {
		this.appPensionPayingDistrId = appPensionPayingDistrId;
	}
	public Integer getAppPensionPayingBranchId() {
		return appPensionPayingBranchId;
	}

	public void setAppPensionPayingBranchId(Integer appPensionPayingBranchId) {
		this.appPensionPayingBranchId = appPensionPayingBranchId;
	}

	public Integer getAppPreferredLoanBranch() {
		return appPreferredLoanBranch;
	}

	public void setAppPreferredLoanBranch(Integer appPreferredLoanBranch) {
		this.appPreferredLoanBranch = appPreferredLoanBranch;
	}

	public Integer getAppPreferredStateId() {
		return appPreferredStateId;
	}

	public void setAppPreferredStateId(Integer appPreferredStateId) {
		this.appPreferredStateId = appPreferredStateId;
	}

	public Integer getAppPreferredCityId() {
		return appPreferredCityId;
	}

	public void setAppPreferredCityId(Integer appPreferredCityId) {
		this.appPreferredCityId = appPreferredCityId;
	}

	public Integer getAppPreferredDistrictId() {
		return appPreferredDistrictId;
	}

	public void setAppPreferredDistrictId(Integer appPreferredDistrictId) {
		this.appPreferredDistrictId = appPreferredDistrictId;
	}
	public Integer getAppPensionType() {
		return appPensionType;
	}

	public void setAppPensionType(Integer appPensionType) {
		this.appPensionType = appPensionType;
	}

	public Integer getAppMonthlyPensionAmt() {
		return appMonthlyPensionAmt;
	}

	public void setAppMonthlyPensionAmt(Integer appMonthlyPensionAmt) {
		this.appMonthlyPensionAmt = appMonthlyPensionAmt;
	}

	public Integer getAppPreferredLoanBranchId() {
		return appPreferredLoanBranchId;
	}

	public void setAppPreferredLoanBranchId(Integer appPreferredLoanBranchId) {
		this.appPreferredLoanBranchId = appPreferredLoanBranchId;
	}

	public Integer getAppEmploymentNature() {
		return appEmploymentNature;
	}

	public void setAppEmploymentNature(Integer appEmploymentNature) {
		this.appEmploymentNature = appEmploymentNature;
	}

	public String getAppPensionAccountNumber() {
		return appPensionAccountNumber;
	}

	public void setAppPensionAccountNumber(String appPensionAccountNumber) {
		this.appPensionAccountNumber = appPensionAccountNumber;
	}

	public String getAppOrgName() {
		return appOrgName;
	}

	public void setAppOrgName(String appOrgName) {
		this.appOrgName = appOrgName;
	}
	
	public void setAppMaritalStatus(Integer appMaritalStatus) {
		this.appMaritalStatus = appMaritalStatus;
	}
	
	public Integer getAppIsSecuredLoan() {
		return appIsSecuredLoan;
	}

	public void setAppIsSecuredLoan(Integer appIsSecuredLoan) {
		this.appIsSecuredLoan = appIsSecuredLoan;
	}
	

	public Integer getAppPensionProofId() {
		return appPensionProofId;
	}

	public void setAppPensionProofId(Integer appPensionProofId) {
		this.appPensionProofId = appPensionProofId;
	}

	public String getAppPensionProofName() {
		return appPensionProofName;
	}

	public void setAppPensionProofName(String appPensionProofName) {
		this.appPensionProofName = appPensionProofName;
	}
	
	public Integer getAppMaritalStatus() {
		return appMaritalStatus;
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
	
	public Boolean isAppCallRSMService() {
		return appCallRSMService;
	}

	public void setAppCallRSMService(Boolean appCallRSMService) {
		this.appCallRSMService = appCallRSMService;
	}

	public Integer getAppResTypeAtVerified() {
		return appResTypeAtVerified;
	}

	public void setAppResTypeAtVerified(Integer appResTypeAtVerified) {
		this.appResTypeAtVerified = appResTypeAtVerified;
	}
	
	public String getAppInstantLoanAccountNumber() {
		return appInstantLoanAccountNumber;
	}

	public void setAppInstantLoanAccountNumber(String appInstantLoanAccountNumber) {
		this.appInstantLoanAccountNumber = appInstantLoanAccountNumber;
	}

	public Integer getAppIDSPMDumpId() {
		return appIDSPMDumpId;
	}

	public void setAppIDSPMDumpId(Integer appIDSPMDumpId) {
		this.appIDSPMDumpId = appIDSPMDumpId;
	}

	public Integer getAppInstantLoanStageId() {
		return appInstantLoanStageId;
	}

	public void setAppInstantLoanStageId(Integer appInstantLoanStageId) {
		this.appInstantLoanStageId = appInstantLoanStageId;
	}

	public String getAppInstantLoanRequest() {
		return appInstantLoanRequest;
	}

	public void setAppInstantLoanRequest(String appInstantLoanRequest) {
		this.appInstantLoanRequest = appInstantLoanRequest;
	}

	public String getAppInstantLoanResponse() {
		return appInstantLoanResponse;
	}

	public void setAppInstantLoanResponse(String appInstantLoanResponse) {
		this.appInstantLoanResponse = appInstantLoanResponse;
	}

	public String getAppCustomerUniqueId() {
		return appCustomerUniqueId;
	}

	public void setAppCustomerUniqueId(String appCustomerUniqueId) {
		this.appCustomerUniqueId = appCustomerUniqueId;
	}

	public String getAppDemandLoanAccontReference() {
		return appDemandLoanAccontReference;
	}

	public void setAppDemandLoanAccontReference(String appDemandLoanAccontReference) {
		this.appDemandLoanAccontReference = appDemandLoanAccontReference;
	}

	public String getAppDemandLoanAccontNumber() {
		return appDemandLoanAccontNumber;
	}

	public void setAppDemandLoanAccontNumber(String appDemandLoanAccontNumber) {
		this.appDemandLoanAccontNumber = appDemandLoanAccontNumber;
	}

	public Date getAppOTPLastAttemptTime() {
		return appOTPLastAttemptTime;
	}

	public void setAppOTPLastAttemptTime(Date appOTPLastAttemptTime) {
		this.appOTPLastAttemptTime = appOTPLastAttemptTime;
	}

	public Date getAppEntryDate() {
		return appEntryDate;
	}

	public void setAppEntryDate(Date appEntryDate) {
		this.appEntryDate = appEntryDate;
	}

	public Integer getAppInstantLoanOTPConscent() {
		return appInstantLoanOTPConscent;
	}

	public void setAppInstantLoanOTPConscent(Integer appInstantLoanOTPConscent) {
		this.appInstantLoanOTPConscent = appInstantLoanOTPConscent;
	}

	public Integer getAppInstantLoanEULAConscent() {
		return appInstantLoanEULAConscent;
	}

	public void setAppInstantLoanEULAConscent(Integer appInstantLoanEULAConscent) {
		this.appInstantLoanEULAConscent = appInstantLoanEULAConscent;
	}

	public Integer getAppInstantLoanApplyConscent() {
		return appInstantLoanApplyConscent;
	}

	public void setAppInstantLoanApplyConscent(Integer appInstantLoanApplyConscent) {
		this.appInstantLoanApplyConscent = appInstantLoanApplyConscent;
	}

	public Integer getAppEmailAttemptCount() {
		return appEmailAttemptCount;
	}

	public void setAppEmailAttemptCount(Integer appEmailAttemptCount) {
		this.appEmailAttemptCount = appEmailAttemptCount;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getAppEmployerLandmark() {
		return appEmployerLandmark;
	}

	public void setAppEmployerLandmark(String appEmployerLandmark) {
		this.appEmployerLandmark = appEmployerLandmark;
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

	public Integer getAppHaveCoOperativeBankAcc() {
		return appHaveCoOperativeBankAcc;
	}

	public void setAppHaveCoOperativeBankAcc(Integer appHaveCoOperativeBankAcc) {
		this.appHaveCoOperativeBankAcc = appHaveCoOperativeBankAcc;
	}

	public Integer getAppCbsRelationShipId() {
		return appCbsRelationShipId;
	}

	public void setAppCbsRelationShipId(Integer appCbsRelationShipId) {
		this.appCbsRelationShipId = appCbsRelationShipId;
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

	public Boolean getAppCallRSMService() {
		return appCallRSMService;
	}

	public Integer getCrmLeadSourceId() {
		return crmLeadSourceId;
	}

	public void setCrmLeadSourceId(Integer crmLeadSourceId) {
		this.crmLeadSourceId = crmLeadSourceId;
	}
	public String getCveProductCategory() {
    return this.cveProductCategory;
  }
  
  public void setCveProductCategory(String cveProductCategory) {
    this.cveProductCategory = cveProductCategory;
  }
  
  public String getCveSalutation() {
    return this.cveSalutation;
  }
  
  public void setCveSalutation(String cveSalutation) {
    this.cveSalutation = cveSalutation;
  }
  
  public String getCveAppFirstName() {
    return this.cveAppFirstName;
  }
  
  public void setCveAppFirstName(String cveAppFirstName) {
    this.cveAppFirstName = cveAppFirstName;
  }
  
  public String getCveAppMiddleName() {
    return this.cveAppMiddleName;
  }
  
  public void setCveAppMiddleName(String cveAppMiddleName) {
    this.cveAppMiddleName = cveAppMiddleName;
  }
  
  public String getCveAppLastName() {
    return this.cveAppLastName;
  }
  
  public void setCveAppLastName(String cveAppLastName) {
    this.cveAppLastName = cveAppLastName;
  }
  
  public String getCveAppPrevBranchId() {
    return this.cveAppPrevBranchId;
  }
  
  public void setCveAppPrevBranchId(String cveAppPrevBranchId) {
    this.cveAppPrevBranchId = cveAppPrevBranchId;
  }
  
  public String getCveAppEmail() {
    return this.cveAppEmail;
  }
  
  public void setCveAppEmail(String cveAppEmail) {
    this.cveAppEmail = cveAppEmail;
  }
  
  public String getCveAppConsentRevoke() {
    return this.cveAppConsentRevoke;
  }
  
  public void setCveAppConsentRevoke(String cveAppConsentRevoke) {
    this.cveAppConsentRevoke = cveAppConsentRevoke;
  }
  
  public String getCveAppConsentRevokeYes() {
    return this.cveAppConsentRevokeYes;
  }
  
  public void setCveAppConsentRevokeYes(String cveAppConsentRevokeYes) {
    this.cveAppConsentRevokeYes = cveAppConsentRevokeYes;
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
