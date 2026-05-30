package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_02703")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MasterBikeType extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	
	@Column(name="BIKE_TYPE_ID")
	private Integer typeId;

	@Column(name="BIKE_TYPE_ACTIVE")
	private String typeActive;

	@Column(name="BIKE_TYPE_DELETED")
	private String typeDeleted;

	@Column(name="BIKE_TYPE_DESCRIPTION")
	private String typeDescription;

	@Column(name="BIKE_TYPE_NAME")
	private String typeName;



	public Integer getTypeId() {
		return this.typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeActive() {
		return this.typeActive;
	}

	public void setTypeActive(String typeActive) {
		this.typeActive = typeActive;
	}

	public String getTypeDeleted() {
		return this.typeDeleted;
	}

	public void setTypeDeleted(String typeDeleted) {
		this.typeDeleted = typeDeleted;
	}

	public String getTypeDescription() {
		return this.typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
