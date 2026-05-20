package com.mintstreet.consent.bo;

import com.google.gson.annotations.SerializedName;

public class ConsentResponseConsentWrite {
	
	@SerializedName("EIS_RESPONSE")
	private ConsentResponseWriteEisResponse eisResponse;
	
	@SerializedName("ERROR_CODE")
	private String errorCode;
	
	@SerializedName("ERROR_DESCRIPTION")
	private String errorDescription;
	
	@SerializedName("RESPONSE_STATUS")
	private String responseStatus;

	public ConsentResponseWriteEisResponse getEisResponse() {
		return eisResponse;
	}

	public void setEisResponse(ConsentResponseWriteEisResponse eisResponse) {
		this.eisResponse = eisResponse;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	@Override
	public String toString() {
		return "ConsentResponseReadPurpose [eisResponse=" + eisResponse + ", errorCode=" + errorCode
				+ ", errorDescription=" + errorDescription + ", responseStatus=" + responseStatus + "]";
	}
	
}
