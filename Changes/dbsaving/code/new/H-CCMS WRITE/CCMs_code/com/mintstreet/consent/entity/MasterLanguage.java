package com.mintstreet.consent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "RUPEEPOWER_OCAS_T_13704")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterLanguage.getLanguageBylannguageCode", query = "select count(l) from MasterLanguage l where l.isActive ='Y' AND l.lannguageCode=:lannguageCode"), 
	@NamedQuery(
			name = "MasterLanguage.getAllActiveLanguages",
			query = "SELECT l FROM MasterLanguage l "
				+ "WHERE l.isActive = 'Y' "
				+ "AND l.lannguageCode IS NOT NULL "
				+ "AND l.languageName IS NOT NULL "
				+ "ORDER BY l.langID"
		)
})





public class MasterLanguage extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "LANG_ID")
	private Integer langID;
	
	@Column(name = "LANNGUAGE_CODE")
	private String lannguageCode;
	
	@Column(name = "LANGUAGE_NAME")
	private String languageName;
	
	@Column(name = "IS_ACTIVE")
	private String isActive;
	
   
	@Column(name = "CREATED_AT")
	private Date createdAt;


	public Integer getLangID() {
		return langID;
	}


	public void setLangID(Integer langID) {
		this.langID = langID;
	}


	public String getLannguageCode() {
		return lannguageCode;
	}


	public void setLannguageCode(String lannguageCode) {
		this.lannguageCode = lannguageCode;
	}


	public String getLanguageName() {
		return languageName;
	}


	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}


	public String getIsActive() {
		return isActive;
	}


	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
	
	
	
	
	

}
