package com.mintstreet.consent.bo;

import com.google.gson.annotations.SerializedName;

public class ConsentReadResponse {
	
	@SerializedName("EIS_RESPONSE")
	private ConsentReadResponseReadEisResponse eisResponse;
	
	@SerializedName("ERROR_CODE")
	private String errorCode;
	
	@SerializedName("ERROR_DESCRIPTION")
	private String errorDescription;
	
	@SerializedName("RESPONSE_STATUS")
	private String responseStatus;

	public ConsentReadResponseReadEisResponse getEisResponse() {
		return eisResponse;
	}

	public void setEisResponse(ConsentReadResponseReadEisResponse eisResponse) {
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
	
	
	
	
}
