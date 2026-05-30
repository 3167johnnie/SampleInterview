package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_03480")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MasterCarType extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CAR_TYPE_ID")
	private int typeId;

	@Column(name="CAR_type_active")
	private String typeActive;

	@Column(name="CAR_type_deleted")
	private String typeDeleted;

	@Column(name="CAR_type_description")
	private String typeDescription;

	@Column(name="CAR_TYPE_NAME")
	private String typeName;

	public MasterCarType() {
	}

	public int getTypeId() {
		return this.typeId;
	}

	public void setTypeId(int typeId) {
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
