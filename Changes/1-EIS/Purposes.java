package com.mintstreet.consent.bo;

import java.util.List;

public class Purposes {
	
	private List<BankProducts> bankProducts;
	private String code;
	private String consentInfo;
	private List<String> dataPoints;
	private String description;
	private boolean isEssential;
	private boolean isETB;
	private boolean isNTB;
	private boolean isProductSpecific;
	private String title;
	private Integer validityDays;
	private String version;
	private boolean hasValidity;

	public List<BankProducts> getBankProducts() {
		return bankProducts;
	}
	public void setBankProducts(List<BankProducts> bankProducts) {
		this.bankProducts = bankProducts;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getConsentInfo() {
		return consentInfo;
	}
	public void setConsentInfo(String consentInfo) {
		this.consentInfo = consentInfo;
	}
	public List<String> getDataPoints() {
		return dataPoints;
	}
	public void setDataPoints(List<String> dataPoints) {
		this.dataPoints = dataPoints;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isEssential() {
		return isEssential;
	}
	public void setEssential(boolean isEssential) {
		this.isEssential = isEssential;
	}
	public boolean isETB() {
		return isETB;
	}
	public void setETB(boolean isETB) {
		this.isETB = isETB;
	}
	public boolean isNTB() {
		return isNTB;
	}
	public void setNTB(boolean isNTB) {
		this.isNTB = isNTB;
	}
	public boolean isProductSpecific() {
		return isProductSpecific;
	}
	public void setProductSpecific(boolean isProductSpecific) {
		this.isProductSpecific = isProductSpecific;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getValidityDays() {
		return validityDays;
	}
	public void setValidityDays(Integer validityDays) {
		this.validityDays = validityDays;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isHasValidity() {
		return hasValidity;
	}
	public void setHasValidity(boolean hasValidity) {
		this.hasValidity = hasValidity;
	}
	@Override
	public String toString() {
		return "Purposes [bankProducts=" + bankProducts + ", code=" + code + ", consentInfo=" + consentInfo
				+ ", dataPoints=" + dataPoints + ", description=" + description + ", isEssential=" + isEssential
				+ ", isETB=" + isETB + ", isNTB=" + isNTB + ", isProductSpecific=" + isProductSpecific + ", title="
				+ title + ", validityDays=" + validityDays + ", version=" + version + ", hasValidity=" + hasValidity
				+ "]";
	}
	
}
