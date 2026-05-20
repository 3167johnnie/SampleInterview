package com.mintstreet.consent.entity;

import javax.persistence.Column;

public class Body {
	
	//@Column(name="ACK")
	private boolean ack;
	//@Column(name="BODY_ID")
	private String locale;
	//@Column(name="BODY_ID")
	private Integer version;
	
	public boolean isAck() {
		return ack;
	}
	public void setAck(boolean ack) {
		this.ack = ack;
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
	
	

}


