package com.mintstreet.loan.cveloan.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00291")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)

@NamedQueries({
	@NamedQuery(name="ApplicationFormCveLoan.findAll", query="SELECT a FROM ApplicationFormCveLoan a"),
	@NamedQuery(name="ApplicationFormCveLoan.getApplicationFormCveLoanByAppSeqId", 
	query = "Select a from ApplicationFormCveLoan a where a.appSeqId =:appSeqId"),
	@NamedQuery(name="ApplicationFormCveLoan.getApplicationFormCveLoanByAppReferenceId", 
		query = "Select a from ApplicationFormCveLoan a where a.appReferenceId =:appReferenceId "),
	@NamedQuery(name="ApplicationFormCveLoan.getApplicationFormCveLoanByAppReferenceIdAndMobileNo", 
		query = "Select a from ApplicationFormCveLoan a where a.appReferenceId =:appReferenceId and CONCAT(a.appISDCode,a.cbsMobileNumber)=:cbsMobileNumber "),				
	@NamedQuery(name="ApplicationFormCveLoan.getCveReferenceIdBySeqId", 
		query = "select a.appReferenceId from ApplicationFormCveLoan a where a.appSeqId =:appSeqId"),
})

	
	@SequenceGenerator(name="G8", sequenceName="RUPEEPOWER_OCAS_SEQ_00291" ,allocationSize=1)
	public class ApplicationFormCveLoan extends com.mintstreet.common.entity.Domain<java.lang.Integer> 
		{
			private static final long serialVersionUID = 1L;	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G8")
	
	@Column(name="APP_SEQ_ID")
	private Integer appSeqId;
	
	@Column(name="APP_REFERENCE_ID")
	private String appReferenceId;
	
	@Column(name="APP_DATA_SOURCE_ID")
	private String appDataSourceId;

	@Column(name="CVE_PRODUCT_CATEGORY")
	private String cveProductCategory;
	
	@Column(name="CVE_SALUTATION")
	private String cveSalutation;
	
	@Column(name="CVE_APP_FIRST_NAME")
	private String cveAppFirstName;
	
	@Column(name="CVE_APP_MIDDLE_NAME")
	private String cveAppMiddleName;
	
	@Column(name="CVE_APP_LAST_NAME")
	private String cveAppLastName;
	
    @Column(name="CVE_APP_PREV_BRANCH_ID")
    private Integer cveAppPrevBranchId;
	
    //Commented by Pratima for CVE
	//@Column(name="LOAN_QUOTE_WORK_BRANCH_ID")
	//private String loanQuoteWorkBranchId;
	
	@Column(name="CVE_APP_EMAIL")
	private String cveAppEmail;
	
	//Not in used so replace as CBS_CIF_NUMBER	
	//@Column(name="CVE_APP_CONSENT_REVOKE")	
	//private String cveAppConsentRevoke;
		
	@Column(name="CBS_CIF_NUMBER")
	private String cbsCifNumber;
	
	@Column(name="CVE_APP_CONSENT_REVOKE_YES")
	private String cveAppConsentRevokeYes;
	
	@Column(name="CBS_ACCOUNT_NUMBER")
	private String cbsAccountNumber;
	
	@Column(name="CBS_MOBILE_NUMBER")
	private String cbsMobileNumber;
	
	@Column(name="CREATED_DATE")
	private Date createdDate;
	
	@Column(name="APP_CRM_LEAD_ID")
	private String appCrmLeadId;
	
	@Column(name="APP_LEAD_UPDATE_TIME")
	private Date appLeadUpdateTime;
	
	@Column(name="APP_OTP_VERIFY_TYPE")
	private int appOTPVerifyType;
		
	@Column(name="APP_ISD_CODE")
	private String appISDCode;
	
	 @Column(name="APP_OTP_ATTEMPT_COUNT")
	private Integer appOTPAttemptCount;
	
	@Column(name="APP_MOB_VERIFYCODE_REC")
	private String appMobileVerificationCodeReceived;
	
	@Column(name="APP_MOBILE_VERIFIED")
	private String appMobileVerified;
	
	@Column(name="APP_EMAIL_VERIFY_CODE")
	private String appEmailVerificationCode;
	
	@Column(name="APP_EMAIL_VERIFIED")
	private String appEmailVerified; 
	
	@Column(name="APP_MOBILE_VERIFY_CODE")
	private Integer appMobileVerificationCode;
	
	@Column(name="APP_APPLYING_FROM")
	private int appApplyingFrom;
	
	@Column(name="APP_CONSENT_ID")
	private Integer loanQuoteConsentId;

	public Integer getAppOTPAttemptCount() {
		return appOTPAttemptCount;
	}

	public void setAppOTPAttemptCount(Integer appOTPAttemptCount) {
		this.appOTPAttemptCount = appOTPAttemptCount;
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

	public String getAppEmailVerificationCode() {
		return appEmailVerificationCode;
	}

	public void setAppEmailVerificationCode(String appEmailVerificationCode) {
		this.appEmailVerificationCode = appEmailVerificationCode;
	}

	public String getAppEmailVerified() {
		return appEmailVerified;
	}

	public void setAppEmailVerified(String appEmailVerified) {
		this.appEmailVerified = appEmailVerified;
	} 

	public int getAppOTPVerifyType() {
		return appOTPVerifyType;
	}

	public void setAppOTPVerifyType(int appOTPVerifyType) {
		this.appOTPVerifyType = appOTPVerifyType;
	}

	public String getAppISDCode() {
		return appISDCode;
	}

	public void setAppISDCode(String appISDCode) {
		this.appISDCode = appISDCode;
	}

	public Integer getAppSeqId() {
		return appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public String getAppReferenceId() {
		return appReferenceId;
	}

	public void setAppReferenceId(String appReferenceId) {
		this.appReferenceId = appReferenceId;
	}

	public String getAppDataSourceId() {
		return appDataSourceId;
	}

	public void setAppDataSourceId(String appDataSourceId) {
		this.appDataSourceId = appDataSourceId;
	}

	public String getCveProductCategory() {
		return cveProductCategory;
	}

	public void setCveProductCategory(String cveProductCategory) {
		this.cveProductCategory = cveProductCategory;
	}

	public String getCveSalutation() {
		return cveSalutation;
	}

	public void setCveSalutation(String cveSalutation) {
		this.cveSalutation = cveSalutation;
	}

	public String getCveAppFirstName() {
		return cveAppFirstName;
	}

	public void setCveAppFirstName(String cveAppFirstName) {
		this.cveAppFirstName = cveAppFirstName;
	}

	public String getCveAppMiddleName() {
		return cveAppMiddleName;
	}

	public void setCveAppMiddleName(String cveAppMiddleName) {
		this.cveAppMiddleName = cveAppMiddleName;
	}

	public String getCveAppLastName() {
		return cveAppLastName;
	}

	public void setCveAppLastName(String cveAppLastName) {
		this.cveAppLastName = cveAppLastName;
	} 

	public String getCveAppEmail() {
		return cveAppEmail;
	}

	public void setCveAppEmail(String cveAppEmail) {
		this.cveAppEmail = cveAppEmail;
	}

	public String getCveAppConsentRevokeYes() {
		return cveAppConsentRevokeYes;
	}

	public void setCveAppConsentRevokeYes(String cveAppConsentRevokeYes) {
		this.cveAppConsentRevokeYes = cveAppConsentRevokeYes;
	}
	
	public String getCbsAccountNumber() {
		return cbsAccountNumber;
	}

	public void setCbsAccountNumber(String cbsAccountNumber) {
		this.cbsAccountNumber = cbsAccountNumber;
	}

	public String getCbsMobileNumber() {
		return cbsMobileNumber;
	}

	public void setCbsMobileNumber(String cbsMobileNumber) {
		this.cbsMobileNumber = cbsMobileNumber;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}	

	public String getAppCrmLeadId() {
		return appCrmLeadId;
	}

	public void setAppCrmLeadId(String appCrmLeadId) {
		this.appCrmLeadId = appCrmLeadId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public Date getAppLeadUpdateTime() {
		return appLeadUpdateTime;
	}

	public void setAppLeadUpdateTime(Date appLeadUpdateTime) {
		this.appLeadUpdateTime = appLeadUpdateTime;
	}
	

	public Integer getAppMobileVerificationCode() {
		return appMobileVerificationCode;
	}

	public void setAppMobileVerificationCode(Integer appMobileVerificationCode) {
		this.appMobileVerificationCode = appMobileVerificationCode;
	}
	

	public int getAppApplyingFrom() {
		return appApplyingFrom;
	}

	public void setAppApplyingFrom(int appApplyingFrom) {
		this.appApplyingFrom = appApplyingFrom;
	}
	
	public Integer getCveAppPrevBranchId() {
		return cveAppPrevBranchId;
	}

	public void setCveAppPrevBranchId(Integer cveAppPrevBranchId) {
		this.cveAppPrevBranchId = cveAppPrevBranchId;
	}

	public String getCbsCifNumber() {
		return cbsCifNumber;
	}

	public void setCbsCifNumber(String cbsCifNumber) {
		this.cbsCifNumber = cbsCifNumber;
	}

	public Integer getLoanQuoteConsentId() {
		return loanQuoteConsentId;
	}

	public void setLoanQuoteConsentId(Integer loanQuoteConsentId) {
		this.loanQuoteConsentId = loanQuoteConsentId;
	}

	

	

	/* public String getLoanQuoteWorkBranchId() {
		return loanQuoteWorkBranchId;
	}

	public void setLoanQuoteWorkBranchId(String loanQuoteWorkBranchId) {
		this.loanQuoteWorkBranchId = loanQuoteWorkBranchId;
	} */


	
	
}
