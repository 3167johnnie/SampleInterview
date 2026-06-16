package com.mintstreet.consent.bo;

import com.google.gson.annotations.SerializedName;

public class ConsentRequest {
	
	@SerializedName("SOURCE_ID")
	private String sourceId;
	
	@SerializedName("EIS_PAYLOAD")
	private ConsentRequestEisPayload eisPayload;
	
	@SerializedName("DESTINATION")
	private String destination;
	
	@SerializedName("TXN_TYPE")
	private String transactionType;
	
	@SerializedName("TXN_SUB_TYPE")
	private String transactionSubType;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public ConsentRequestEisPayload getEisPayload() {
		return eisPayload;
	}

	public void setEisPayload(ConsentRequestEisPayload eisPayload) {
		this.eisPayload = eisPayload;
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

	public String getTransactionSubType() {
		return transactionSubType;
	}

	public void setTransactionSubType(String transactionSubType) {
		this.transactionSubType = transactionSubType;
	}
	
}
