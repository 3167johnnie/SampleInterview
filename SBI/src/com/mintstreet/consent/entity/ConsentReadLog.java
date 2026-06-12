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
@Table(name="RUPEEPOWER_OCAS_T_13702")
@org.hibernate.annotations.Entity(dynamicUpdate=true, dynamicInsert=true)
@SequenceGenerator(name="RUPEEPOWER_OCAS_T_13702_SEQ", sequenceName="RUPEEPOWER_OCAS_T_13702_SEQ" ,allocationSize=1)
public class ConsentReadLog extends Domain<Integer> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RUPEEPOWER_OCAS_T_13702_SEQ")
	@Column(name="CONSENT_READ_ID")
	private Integer consentReadId;
	
	@Column(name="CONSENT_READ_X_CORRELATION_ID", unique = true, nullable = false)
	private String consentReadxCorrelationId;
	
	@Column(name="CONSENT_READ_DP_CIF")
	private String consentReadDpCIF;

	@Column(name="CONSENT_READ_REQUEST")
	private String consentReadRequest;

	@Column(name="CONSENT_READ_REQUEST_ENTRY_TIME")
	private Date  RequestEntryTime;
	
	@Column(name="CONSENT_READ_RESPONSE_CODE")
	private String ResponseCode;

	@Column(name="CONSENT_READ_RESPONSE")
	private String ResponseMsg;
		
	@Column(name="CONSENT_READ_RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="CONSENT_READ_RESPONSE_ENTRY_TIME")
	private Date responseEntryTime;

	public Integer getConentReadId() {
		return consentReadId;
	}

	public void setConentReadId(Integer consentReadId) {
		this.consentReadId = consentReadId;
	}

	public String getConsentReadxCorrelationId() {
		return consentReadxCorrelationId;
	}

	public void setConsentReadxCorrelationId(String consentReadxCorrelationId) {
		this.consentReadxCorrelationId = consentReadxCorrelationId;
	}

	public String getConsentReadDpCIF() {
		return consentReadDpCIF;
	}

	public void setConsentReadDpCIF(String consentReadDpCIF) {
		this.consentReadDpCIF = consentReadDpCIF;
	}

	public String getConsentReadRequest() {
		return consentReadRequest;
	}

	public void setConsentReadRequest(String consentReadRequest) {
		this.consentReadRequest = consentReadRequest;
	}

	public Date getRequestEntryTime() {
		return RequestEntryTime;
	}

	public void setRequestEntryTime(Date requestEntryTime) {
		RequestEntryTime = requestEntryTime;
	}

	public String getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}

	public String getResponseMsg() {
		return ResponseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		ResponseMsg = responseMsg;
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

	@Override
	public String toString() {
		return "ConsentReadLog [conenstReadId=" + consentReadId + ", consentReadxCorrelationId="
				+ consentReadxCorrelationId + ", consentReadDpCIF=" + consentReadDpCIF + ", consentReadRequest="
				+ consentReadRequest + ", RequestEntryTime=" + RequestEntryTime + ", ResponseCode=" + ResponseCode
				+ ", ResponseMsg=" + ResponseMsg + ", responseStatus=" + responseStatus + ", responseEntryTime="
				+ responseEntryTime + "]";
	}
	
}
