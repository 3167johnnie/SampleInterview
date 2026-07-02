package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentResponsePurposes {
	
	private String code;
	private String version;
	private String title;
	private String description;
	private String validityDays;
	private List<String> dataPoints;
	private List<ConsentResponseBankProducts> bankProducts;
	private String isEssential;
	private String isProductSpecific;
	private String isNTB;
	private String isETB;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getValidityDays() {
		return validityDays;
	}
	
	public void setValidityDays(String validityDays) {
		this.validityDays = validityDays;
	}
	
	public List<String> getDataPoints() {
		return dataPoints;
	}
	
	public void setDataPoints(List<String> dataPoints) {
		this.dataPoints = dataPoints;
	}
	
	public List<ConsentResponseBankProducts> getBankProducts() {
		return bankProducts;
	}
	
	public void setBankProducts(List<ConsentResponseBankProducts> bankProducts) {
		this.bankProducts = bankProducts;
	}
	
	public String getIsEssential() {
		return isEssential;
	}
	
	public void setIsEssential(String isEssential) {
		this.isEssential = isEssential;
	}
	
	public String getIsProductSpecific() {
		return isProductSpecific;
	}
	
	public void setIsProductSpecific(String isProductSpecific) {
		this.isProductSpecific = isProductSpecific;
	}
	
	public String getIsNTB() {
		return isNTB;
	}
	
	public void setIsNTB(String isNTB) {
		this.isNTB = isNTB;
	}
	
	public String getIsETB() {
		return isETB;
	}
	
	public void setIsETB(String isETB) {
		this.isETB = isETB;
	}

	@Override
	public String toString() {
		return "ConsentResponsePurposes [code=" + code + ", version=" + version + ", title=" + title + ", description="
				+ description + ", validityDays=" + validityDays + ", dataPoints=" + dataPoints + ", bankProducts="
				+ bankProducts + ", isEssential=" + isEssential + ", isProductSpecific=" + isProductSpecific
				+ ", isNTB=" + isNTB + ", isETB=" + isETB + "]";
	}
	
}
