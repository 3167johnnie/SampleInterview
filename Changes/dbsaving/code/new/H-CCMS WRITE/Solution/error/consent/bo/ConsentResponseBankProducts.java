package com.mintstreet.consent.bo;

public class ConsentResponseBankProducts {

	private String code;
	private String label;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "ConsentResponseBankProducts [code=" + code + ", label=" + label + "]";
	}
	
}
