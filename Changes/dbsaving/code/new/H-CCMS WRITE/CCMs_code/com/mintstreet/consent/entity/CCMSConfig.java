package com.mintstreet.consent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.mintstreet.common.entity.Domain;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_13699")
@NamedQueries({
	@NamedQuery(name="CCMSConfig.getConfigDetails", query = "Select c from CCMSConfig c where c.ccmsId = :ccmsId")
})
public class CCMSConfig extends Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CCMS_ID")
	private Integer ccmsId;
	
	@Column(name="CCMS_TOUCHPOINT_ID")
	private String touchPointId;
	
	@Column(name="CCMS_PURPOSE_SET_ID")
	private String purposeSetId;
	
	@Column(name="CCMS_REQUEST_TYPE")
	private String requestType;
	
	@Column(name="CCMS_ACCEPT_LANGUAGE")
	private String acceptLanguage;
	
	@Column(name="CCMS_API_VERSION")
	private String apiVersion;
	
	@Column(name="CCMS_SOURCE_ID")
	private String sourceId;
	
	@Column(name="CCMS_DESTINATION")
	private String destination;
	
	@Column(name="CCMS_TRANSACTION_TYPE")
	private String transactionType;
	
	@Column(name="CCMS_AUTH_Key")
	private String authKey;
	

	public Integer getCcmsId() {
		return ccmsId;
	}

	public void setCcmsId(Integer ccmsId) {
		this.ccmsId = ccmsId;
	}

	public String getTouchPointId() {
		return touchPointId;
	}

	public void setTouchPointId(String touchPointId) {
		this.touchPointId = touchPointId;
	}

	public String getPurposeSetId() {
		return purposeSetId;
	}

	public void setPurposeSetId(String purposeSetId) {
		this.purposeSetId = purposeSetId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	@Override
	public String toString() {
		return "CCMSConfig [ccmsId=" + ccmsId + ", touchPointId=" + touchPointId + ", purposeSetId=" + purposeSetId
				+ ", requestType=" + requestType + ", acceptLanguage=" + acceptLanguage + ", apiVersion=" + apiVersion
				+ ", sourceId=" + sourceId + ", destination=" + destination + ", transactionType=" + transactionType
				+ "]";
	}
	
}
