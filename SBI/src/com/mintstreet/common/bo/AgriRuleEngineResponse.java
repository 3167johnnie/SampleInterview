package com.mintstreet.common.bo;

import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoanQuote;

public class AgriRuleEngineResponse {
	private Integer status;
	private String message;
	private ApplicationFormAgriLoan application;
	private ApplicationFormAgriLoanQuote quote;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ApplicationFormAgriLoan getApplication() {
		return application;
	}
	public void setApplication(ApplicationFormAgriLoan application) {
		this.application = application;
	}
	public ApplicationFormAgriLoanQuote getQuote() {
		return quote;
	}
	public void setQuote(ApplicationFormAgriLoanQuote quote) {
		this.quote = quote;
	}
}
