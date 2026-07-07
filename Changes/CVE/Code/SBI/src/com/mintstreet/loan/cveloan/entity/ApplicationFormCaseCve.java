package com.mintstreet.loan.cveloan.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.mintstreet.common.entity.Domain;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00292")
@org.hibernate.annotations.Entity(dynamicUpdate=true, dynamicInsert=true)
@SequenceGenerator(name="RUPEEPOWER_OCAS_SEQ_00292", sequenceName="RUPEEPOWER_OCAS_SEQ_00292" ,allocationSize=1)


public class ApplicationFormCaseCve extends Domain<Integer> implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RUPEEPOWER_OCAS_SEQ_00292")
	@Column(name="CRM_LOG_ID")
	private Integer crmLogId;
	
	@Column(name="REFERENCE_NUMBER")
	private String referenceNumber;
	
	@Column(name="CIF_NUMBER")
	//private String requestCrmLeadId;
	private String cifNumber;
	
	@Column(name="REQUEST_CASE_LEADID")
	private String requestCaseLeadId;
	
	@Column(name="UPDATE_DATE")
	private Date updateDate;
	
	@Column(name="RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="REQUEST_XML")
	private String requestXml;
	
	@Column(name="RESPONSE_DESCRIPTION")
    private String responseDescription;
	
	@Column(name="CONSENT_REVOKE")
	private String consentRevoke;
	
	@Column(name="REPRESENTATIVE_MODE")
	private String representativeMode;
	
	@Column(name="SCHEDULE_DATE")
	private String scheduleDate;
	
	@Column(name="SCHEDULE_TIME")
	private String scheduleTime;

	public Integer getCrmLogId() {
		return crmLogId;
	}

	public void setCrmLogId(Integer crmLogId) {
		this.crmLogId = crmLogId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getRequestCaseLeadId() {
		return requestCaseLeadId;
	}

	public void setRequestCaseLeadId(String requestCaseLeadId) {
		this.requestCaseLeadId = requestCaseLeadId;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getRequestXml() {
		return requestXml;
	}

	public void setRequestXml(String requestXml) {
		this.requestXml = requestXml;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCifNumber() {
		return cifNumber;
	}

	public void setCifNumber(String cifNumber) {
		this.cifNumber = cifNumber;
	}

	public String getConsentRevoke() {
		return consentRevoke;
	}

	public void setConsentRevoke(String consentRevoke) {
		this.consentRevoke = consentRevoke;
	}

	public String getRepresentativeMode() {
		return representativeMode;
	}

	public void setRepresentativeMode(String representativeMode) {
		this.representativeMode = representativeMode;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

}
