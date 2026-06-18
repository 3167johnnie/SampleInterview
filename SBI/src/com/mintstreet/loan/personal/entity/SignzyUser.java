package com.mintstreet.loan.personal.entity;

import java.io.Serializable;

public class SignzyUser implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String ttl;
	private String created;
	private String userId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTtl() {
		return ttl;
	}
	public void setTtl(String ttl) {
		this.ttl = ttl;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}		
}
