package com.mintstreet.consent.bo;

public class PrivacyRequestBeanEnc {

	
	private String touchPointId;
	private String privacyNotice;
	private String locale;
	private Integer version;
	private String timestamp;
	private String correlationId;
	
	public String getTouchPointId() {
		return touchPointId;
	}
	public void setTouchPointId(String touchPointId) {
		this.touchPointId = touchPointId;
	}
	public String getPrivacyNotice() {
		return privacyNotice;
	}
	public void setPrivacyNotice(String privacyNotice) {
		this.privacyNotice = privacyNotice;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
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
		return "PrivacyRequestBeanEnc [touchPointId=" + touchPointId + ", privacyNotice=" + privacyNotice + ", locale="
				+ locale + ", version=" + version + ", timestamp=" + timestamp + ", correlationId=" + correlationId
				+ "]";
	}
	
}
