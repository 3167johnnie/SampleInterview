package com.mintstreet.loan.educationloan.entity;

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
@Table(name="RUPEEPOWER_OCAS_T_00143")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@SequenceGenerator(name="G15", sequenceName="RUPEEPOWER_OCAS_SEQ_00035" ,allocationSize=1)

public class ApplicationFormEducationLoanCalls extends Domain<Integer> implements Serializable {
	private static final long serialVersionUID = -3431561045158949121L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G15")
	@Column(name="CALL_ID")
	private Integer callId;

	@Column(name="call_active")
	private String callActive;

	@Column(name="call_app_bank_id")
	private Integer callApplicationBankId;

	@Column(name="call_app_id")
	private Integer callApplicationId;

	@Column(name="call_deleted")
	private String callDeleted;

	@Column(name="call_description")
	private String callDescription;

	@Column(name="call_doc_pickup_time")
	private Date callDocPickupTime;

	@Column(name="call_end_time")
	private Date callEndTime;

	@Column(name="call_is_generic_alert")
	private String callIsGenericAlert;

	@Column(name="call_lms_user_id")
	private Integer callLmsUserId;

	

	

	@Column(name="call_status_id")
	private Integer callStatusId;

	public ApplicationFormEducationLoanCalls() {
	}
	
	public Integer getCallId() {
		return callId;
	}

	public void setCallId(Integer callId) {
		this.callId = callId;
	}

	public String getCallActive() {
		return callActive;
	}

	public void setCallActive(String callActive) {
		this.callActive = callActive;
	}

	public Integer getCallApplicationBankId() {
		return callApplicationBankId;
	}

	public void setCallApplicationBankId(Integer callApplicationBankId) {
		this.callApplicationBankId = callApplicationBankId;
	}

	public Integer getCallApplicationId() {
		return callApplicationId;
	}

	public void setCallApplicationId(Integer callApplicationId) {
		this.callApplicationId = callApplicationId;
	}

	public String getCallDeleted() {
		return callDeleted;
	}

	public void setCallDeleted(String callDeleted) {
		this.callDeleted = callDeleted;
	}

	public String getCallDescription() {
		return callDescription;
	}

	public void setCallDescription(String callDescription) {
		this.callDescription = callDescription;
	}

	public Date getCallDocPickupTime() {
		return callDocPickupTime;
	}

	public void setCallDocPickupTime(Date callDocPickupTime) {
		this.callDocPickupTime = callDocPickupTime;
	}

	public Date getCallEndTime() {
		return callEndTime;
	}

	public void setCallEndTime(Date callEndTime) {
		this.callEndTime = callEndTime;
	}

	public String getCallIsGenericAlert() {
		return callIsGenericAlert;
	}

	public void setCallIsGenericAlert(String callIsGenericAlert) {
		this.callIsGenericAlert = callIsGenericAlert;
	}

	public Integer getCallLmsUserId() {
		return callLmsUserId;
	}

	public void setCallLmsUserId(Integer callLmsUserId) {
		this.callLmsUserId = callLmsUserId;
	}

	

	

	public Integer getCallStatusId() {
		return callStatusId;
	}

	public void setCallStatusId(Integer callStatusId) {
		this.callStatusId = callStatusId;
	}

}
