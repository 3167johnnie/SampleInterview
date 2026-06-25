package com.mintstreet.consent.entity;

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
@Table(name="RUPEEPOWER_OCAS_T_13701")
@org.hibernate.annotations.Entity(dynamicUpdate=true, dynamicInsert=true)
@SequenceGenerator(name="RUPEEPOWER_OCAS_T_13701_SEQ", sequenceName="RUPEEPOWER_OCAS_T_13701_SEQ" ,allocationSize=1)
public class ConsentWriteLog extends Domain<Integer> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RUPEEPOWER_OCAS_T_13701_SEQ")
	@Column(name="CONSENT_WRITE_ID")
	private Integer consentWriteId;
	
	@Column(name="CONSENT_WRITE_X_CORRELATION_ID", unique = true, nullable = false)
	private String xCorrelationId;

	@Column(name="CONSENT_WRITE_REQUEST")
	private String consentWriteRequest;

	@Column(name="CONSENT_WRITE_REQUEST_ENTRY_TIME")
	private Date requestEntryTime;
	
	@Column(name="CONSENT_WRITE_RESPONSE_CODE")
	private String responseCode;

	@Column(name="CONSENT_WRITE_RESPONSE")
	private String responseMsg;
		
	@Column(name="CONSENT_WRITE_RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="CONSENT_WRITE_RESPONSE_ENTRY_TIME")
	private Date responseEntryTime;

	public Integer getConsentWriteId() {
		return consentWriteId;
	}

	public void setConsentWriteId(Integer consentWriteId) {
		this.consentWriteId = consentWriteId;
	}

	public String getxCorrelationId() {
		return xCorrelationId;
	}

	public void setxCorrelationId(String xCorrelationId) {
		this.xCorrelationId = xCorrelationId;
	}

	public String getConsentWriteRequest() {
		return consentWriteRequest;
	}

	public void setConsentWriteRequest(String consentWriteRequest) {
		this.consentWriteRequest = consentWriteRequest;
	}

	public Date getRequestEntryTime() {
		return requestEntryTime;
	}

	public void setRequestEntryTime(Date requestEntryTime) {
		this.requestEntryTime = requestEntryTime;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Date getResponseEntryTime() {
		return responseEntryTime;
	}

	public void setResponseEntryTime(Date responseEntryTime) {
		this.responseEntryTime = responseEntryTime;
	}

}
