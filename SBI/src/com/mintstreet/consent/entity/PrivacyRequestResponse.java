package com.mintstreet.consent.entity;

import java.io.Serializable;

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
@Table(name="RUPEEPOWER_OCAS_T_13703")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="RUPEEPOWER_OCAS_T_13703_SEQ", sequenceName="RUPEEPOWER_OCAS_T_13703_SEQ" ,allocationSize=1)
@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a")
@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a"),
})

public class PrivacyRequestResponse extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="RUPEEPOWER_OCAS_T_13703_SEQ")
	@Column(name="PRIVACY_ID")
	private Integer privacyId; 
	
	@Column(name="PRIVACY_TOUCH_POINT_ID")
	private String privacyTouchPointId;
	
	@Column(name="PRIVACY_NOTICE")
	private String privacyNotice;
	
	@Column(name="PRIVACY_LOCALE")
	private String privacyLocale;
	
	@Column(name="PRIVACY_API_VERSION")
	private String privacyApiVersion;
	
	@Column(name="PRIVACY_TIME_STAMP_REQ")
	private String privacyTimeStampReq;
	
	@Column(name="PRIVACY_CORRELATION_ID")
	private String privacyCorrelationId;
	
	@Column(name="PRIVACY_ACK")
	private String privacyAck;
	
	@Column(name="PRIVACY_TIME_STAMP_RES")
	private String privacyTimeStampRes;
	
	@Column(name="PRIVACY_STATUS")
	private String privacyStatus;
	
	@Column(name="PRIVACY_STATUS_CODE")
	private Integer privacyStatusCode;
	
	@Column(name="PRIVACY_MESSAGE")
	private String privacyMessage;
	
	@Column(name="PRIVACY_ERROR_MSG")
	private String privacyErrorMsg;

	public Integer getPrivacyId() {
		return privacyId;
	}

	public void setPrivacyId(Integer privacyId) {
		this.privacyId = privacyId;
	}

	public String getPrivacyTouchPointId() {
		return privacyTouchPointId;
	}

	public void setPrivacyTouchPointId(String privacyTouchPointId) {
		this.privacyTouchPointId = privacyTouchPointId;
	}

	public String getPrivacyNotice() {
		return privacyNotice;
	}

	public void setPrivacyNotice(String privacyNotice) {
		this.privacyNotice = privacyNotice;
	}

	public String getPrivacyLocale() {
		return privacyLocale;
	}

	public void setPrivacyLocale(String privacyLocale) {
		this.privacyLocale = privacyLocale;
	}

	public String getPrivacyApiVersion() {
		return privacyApiVersion;
	}

	public void setPrivacyApiVersion(String privacyApiVersion) {
		this.privacyApiVersion = privacyApiVersion;
	}

	public String getPrivacyTimeStampReq() {
		return privacyTimeStampReq;
	}

	public void setPrivacyTimeStampReq(String privacyTimeStampReq) {
		this.privacyTimeStampReq = privacyTimeStampReq;
	}

	public String getPrivacyCorrelationId() {
		return privacyCorrelationId;
	}

	public void setPrivacyCorrelationId(String privacyCorrelationId) {
		this.privacyCorrelationId = privacyCorrelationId;
	}

	public String getPrivacyAck() {
		return privacyAck;
	}

	public void setPrivacyAck(String privacyAck) {
		this.privacyAck = privacyAck;
	}

	public String getPrivacyTimeStampRes() {
		return privacyTimeStampRes;
	}

	public void setPrivacyTimeStampRes(String privacyTimeStampRes) {
		this.privacyTimeStampRes = privacyTimeStampRes;
	}

	public String getPrivacyStatus() {
		return privacyStatus;
	}

	public void setPrivacyStatus(String privacyStatus) {
		this.privacyStatus = privacyStatus;
	}

	public Integer getPrivacyStatusCode() {
		return privacyStatusCode;
	}

	public void setPrivacyStatusCode(Integer privacyStatusCode) {
		this.privacyStatusCode = privacyStatusCode;
	}

	public String getPrivacyMessage() {
		return privacyMessage;
	}

	public void setPrivacyMessage(String privacyMessage) {
		this.privacyMessage = privacyMessage;
	}

	public String getPrivacyErrorMsg() {
		return privacyErrorMsg;
	}

	public void setPrivacyErrorMsg(String privacyErrorMsg) {
		this.privacyErrorMsg = privacyErrorMsg;
	}

	@Override
	public String toString() {
		return "PrivacyRequestResponse [privacyId=" + privacyId + ", privacyTouchPointId=" + privacyTouchPointId
				+ ", privacyNotice=" + privacyNotice + ", privacyLocale=" + privacyLocale + ", privacyApiVersion="
				+ privacyApiVersion + ", privacyTimeStampReq=" + privacyTimeStampReq + ", privacyCorrelationId="
				+ privacyCorrelationId + ", privacyAck=" + privacyAck + ", privacyTimeStampRes=" + privacyTimeStampRes
				+ ", privacyStatus=" + privacyStatus + ", privacyStatusCode=" + privacyStatusCode + ", privacyMessage="
				+ privacyMessage + ", privacyErrorMsg=" + privacyErrorMsg + "]";
	}

	
	
	
	
	
	
	

	
	
	
	
	
}
