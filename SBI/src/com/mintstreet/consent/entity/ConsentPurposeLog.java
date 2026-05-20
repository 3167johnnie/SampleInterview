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
@Table(name="RUPEEPOWER_OCAS_T_13700")
@org.hibernate.annotations.Entity(dynamicUpdate=true, dynamicInsert=true)
@SequenceGenerator(name="RUPEEPOWER_OCAS_T_13700_SEQ", sequenceName="RUPEEPOWER_OCAS_T_13700_SEQ" ,allocationSize=1)
public class ConsentPurposeLog extends Domain<Integer> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RUPEEPOWER_OCAS_T_13700_SEQ")
	@Column(name="PURPOSE_ID")
	private Integer purposeId;
	
	@Column(name="PURPOSE_X_CORRELATION_ID", unique = true, nullable = false)
	private String xCorrelationId;

	@Column(name="PURPOSE_REQUEST")
	private String puposeRequest;

	@Column(name="PURPOSE_REQUEST_ENTRY_TIME")
	private Date requestEntryTime;
	
	@Column(name="PURPOSE_RESPONSE_CODE")
	private String responseCode;

	@Column(name="PURPOSE_RESPONSE")
	private String responseMsg;
		
	@Column(name="PURPOSE_RESPONSE_STATUS")
	private String responseStatus;
	
	@Column(name="PURPOSE_RESPONSE_ENTRY_TIME")
	private Date responseEntryTime;

	public Integer getPurposeId() {
		return purposeId;
	}

	public void setPurposeId(Integer purposeId) {
		this.purposeId = purposeId;
	}

	public String getxCorrelationId() {
		return xCorrelationId;
	}

	public void setxCorrelationId(String xCorrelationId) {
		this.xCorrelationId = xCorrelationId;
	}

	public String getPuposeRequest() {
		return puposeRequest;
	}

	public void setPuposeRequest(String puposeRequest) {
		this.puposeRequest = puposeRequest;
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

	@Override
	public String toString() {
		return "ConsentPurposeLog [purposeId=" + purposeId + ", xCorrelationId=" + xCorrelationId + ", puposeRequest="
				+ puposeRequest + ", requestEntryTime=" + requestEntryTime + ", responseCode=" + responseCode
				+ ", responseMsg=" + responseMsg + ", responseStatus=" + responseStatus + ", responseEntryTime="
				+ responseEntryTime + "]";
	}
	
}
