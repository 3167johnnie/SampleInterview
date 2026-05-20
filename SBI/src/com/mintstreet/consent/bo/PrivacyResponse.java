package com.mintstreet.consent.bo;

import com.mintstreet.consent.entity.Body;

public class PrivacyResponse {
	
	private boolean success;
	private Integer statusCode;
	private String message;
	private String errors;
	private Body body;
	private String timestamp;
	private String correlationId;
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrors() {
		return errors;
	}
	public void setErrors(String errors) {
		this.errors = errors;
	}
	public Body getBody() {
		return body;
	}
	public void setBody(Body body) {
		this.body = body;
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





