package com.mintstreet.common.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_13689")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="Consent.getConsentByLoanType", query = "Select c from Consent c where "
			+ "c.consentLoanType=:consentLoanType and c.consentActive = 'Y' and c.consentDeleted ='N' "),
	@NamedQuery(name="Consent.getConsentByLoanAndCustomerType", query = "Select c from Consent c where "
			+ "c.consentLoanType=:consentLoanType and c.consentCustomerType=:consentCustomerType and c.consentActive = 'Y' and c.consentDeleted ='N' "),
	@NamedQuery(name="Consent.getConsentIdByLoanType", query = "Select c.consentId from Consent c where "
			+ "c.consentLoanType=:consentLoanType and c.consentActive = 'Y' and c.consentDeleted ='N' ")	
})

public class Consent extends Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id	
	@Column(name="CONSENT_ID")
	private Integer consentId;

	@Column(name="CONSENT_LOAN_TYPE_ID")
	private Integer consentLoanType;
	
	@Column(name="CONSENT_CUSTOMER_TYPE")
	private String consentCustomerType;
	
	@Column(name="CONSENT_CREATED_AT")
	private Date consentCreatedAt;
	
	@Column(name="CONSENT_ACTIVE")
	private String consentActive;
	
	@Column(name="CONSENT_DELETED")
	private String consentDeleted;
	
	@Transient
	private String consentText;

	public Consent() {
	}

	public Integer getConsentId() {
		return consentId;
	}

	public void setConsentId(Integer consentId) {
		this.consentId = consentId;
	}

	public Integer getConsentLoanType() {
		return consentLoanType;
	}

	public void setConsentLoanType(Integer consentLoanType) {
		this.consentLoanType = consentLoanType;
	}

	public Date getConsentCreatedAt() {
		return consentCreatedAt;
	}

	public void setConsentCreatedAt(Date consentCreatedAt) {
		this.consentCreatedAt = consentCreatedAt;
	}

	public String getConsentActive() {
		return consentActive;
	}

	public void setConsentActive(String consentActive) {
		this.consentActive = consentActive;
	}

	public String getConsentDeleted() {
		return consentDeleted;
	}

	public void setConsentDeleted(String consentDeleted) {
		this.consentDeleted = consentDeleted;
	}

	public String getConsentText() {
		return consentText;
	}

	public void setConsentText(String consentText) {
		this.consentText = consentText;
	}

	public String getConsentCustomerType() {
		return consentCustomerType;
	}

	public void setConsentCustomerType(String consentCustomerType) {
		this.consentCustomerType = consentCustomerType;
	}



}
