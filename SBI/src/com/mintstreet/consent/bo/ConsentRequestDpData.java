package com.mintstreet.consent.bo;

public class ConsentRequestDpData {

	private String dpCIF;
	private String dpMobile;
	private String dpEmail;
	private String dpIPAddress;
	private String locale;
	private String timestamp;
	
	public String getDpCIF() {
		return dpCIF;
	}
	
	public void setDpCIF(String dpCIF) {
		this.dpCIF = dpCIF;
	}
	
	public String getDpMobile() {
		return dpMobile;
	}
	
	public void setDpMobile(String dpMobile) {
		this.dpMobile = dpMobile;
	}
	
	public String getDpEmail() {
		return dpEmail;
	}
	
	public void setDpEmail(String dpEmail) {
		this.dpEmail = dpEmail;
	}
	
	public String getDpIPAddress() {
		return dpIPAddress;
	}
	
	public void setDpIPAddress(String dpIPAddress) {
		this.dpIPAddress = dpIPAddress;
	}
	
	public String getLocale() {
		return locale;
	}
	
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
}
