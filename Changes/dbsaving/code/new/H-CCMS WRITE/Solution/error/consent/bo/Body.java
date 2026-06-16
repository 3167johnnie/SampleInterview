package com.mintstreet.consent.bo;

import java.util.List;

public class Body {
	
	private String dpCIF;
	
	private List<DpConsent> dpConsent;
	
	public String getDpCIF() {
		return dpCIF;
	}
	public void setDpCIF(String dpCIF) {
		this.dpCIF = dpCIF;
	}
	public List<DpConsent> getDpConsent() {
		return dpConsent;
	}
	public void setDpConsent(List<DpConsent> dpConsent) {
		this.dpConsent = dpConsent;
	}
	
	

}
