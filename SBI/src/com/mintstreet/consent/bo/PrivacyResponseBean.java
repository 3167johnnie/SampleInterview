package com.mintstreet.consent.bo;


import java.util.List;
import java.util.Map;

import com.mintstreet.consent.entity.Body;

public class PrivacyResponseBean {
	
	private boolean success;
	private Integer statusCode;
	private List<String> message;
	private Map<String,String> errors;
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
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(List<String> message) {
		this.message = message;
	}
	public Map<String, String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String, String> errors) {
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
	@Override
	public String toString() {
		return " [success=" + success + ", statusCode=" + statusCode + ", message=" + message
				+ ", errors=" + errors + ", body=" + body + ", timestamp=" + timestamp + ", correlationId="
				+ correlationId + "]";
	}
	
	

	
	
	
	
		
		
	

}
