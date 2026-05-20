package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentRequestConsent {
	
	private String purposeCode;
	private String purposeVersion;
	private String consented;
	private List<String> consentedProducts;
	private List<String> nonConsentedProducts;
	
	public String getPurposeCode() {
		return purposeCode;
	}
	
	public void setPurposeCode(String purposeCode) {
		this.purposeCode = purposeCode;
	}
	
	public String getPurposeVersion() {
		return purposeVersion;
	}
	
	public void setPurposeVersion(String purposeVersion) {
		this.purposeVersion = purposeVersion;
	}
	
	public String getConsented() {
		return consented;
	}
	
	public void setConsented(String consented) {
		this.consented = consented;
	}
	
	public List<String> getConsentedProducts() {
		return consentedProducts;
	}
	
	public void setConsentedProducts(List<String> consentedProducts) {
		this.consentedProducts = consentedProducts;
	}
	
	public List<String> getNonConsentedProducts() {
		return nonConsentedProducts;
	}
	
	public void setNonConsentedProducts(List<String> nonConsentedProducts) {
		this.nonConsentedProducts = nonConsentedProducts;
	}
	
}
