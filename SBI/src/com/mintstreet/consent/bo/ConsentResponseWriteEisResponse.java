package com.mintstreet.consent.bo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ConsentResponseWriteEisResponse {

	private String success;
	private String statusCode;
	private List<String> messages;
	private String locale;
	private ConsentResponseEisError errors;
	
	@SerializedName("body")
	private ConsentResponseWriteEisBody eisBody;
	
	private String timestamp;
	private String correlationId;
	
	public String getSuccess() {
		return success;
	}
	
	public void setSuccess(String success) {
		this.success = success;
	}
	
	public String getStatusCode() {
		return statusCode;
	}
	
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public ConsentResponseEisError getErrors() {
		return errors;
	}
	
	public void setErrors(ConsentResponseEisError errors) {
		this.errors = errors;
	}
	
	public ConsentResponseWriteEisBody getEisBody() {
		return eisBody;
	}

	public void setEisBody(ConsentResponseWriteEisBody eisBody) {
		this.eisBody = eisBody;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getCorrelationId() {
		return correlationId;
	}
	
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	
}
