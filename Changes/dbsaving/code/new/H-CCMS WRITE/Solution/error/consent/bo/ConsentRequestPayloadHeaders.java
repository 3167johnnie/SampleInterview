package com.mintstreet.consent.bo;

import com.google.gson.annotations.SerializedName;

public class ConsentRequestPayloadHeaders {

	@SerializedName("Accept-Language")
	private String acceptLanguage;
	
	@SerializedName("X-Correlation-Id")
	private String xCorelationId;
	
	@SerializedName("X-API-Version")
	private String xApiVersion;

	public String getAcceptLanguage() {
		return acceptLanguage;
	}

	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}

	public String getxCorelationId() {
		return xCorelationId;
	}

	public void setxCorelationId(String xCorelationId) {
		this.xCorelationId = xCorelationId;
	}

	public String getxApiVersion() {
		return xApiVersion;
	}

	public void setxApiVersion(String xApiVersion) {
		this.xApiVersion = xApiVersion;
	}
	
	
}
