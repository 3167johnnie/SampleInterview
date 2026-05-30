package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import java.util.Date;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_03599")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({//changed by hakeem on 30 aug 2022
	@NamedQuery(name="MasterCarVariant.getCarVariantByCarmodel", query="SELECT m FROM MasterCarVariant m where "
			+ " m.modelId=:modelId and makeId=:makeId and  m.variantActive='Y' AND m.variantDeleted='N' AND m.variantIsChecker=0 "
			+ " order by LOWER(m.variantName) asc  ")
})

public class MasterCarVariant  extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CAR_VARIANT_ID")
	private Integer variantId;

	@Column(name="CAR_VARIANT_MODEL_ID")
	private Integer modelId;
	
	@Column(name="CAR_VARIANT_NAME")
	private String variantName;

	@Column(name="CAR_VARIANT_CREATED")
	private Date variantCreated;

	@Column(name="CAR_VARIANT_UPDATED")
	private Date variantUpdated;

	@Column(name="CAR_VARIANT_ACTIVE")
	private String variantActive;

	@Column(name="CAR_VARIANT_DELETED")
	private String variantDeleted;
	
	@Column(name="CAR_VARIANT_IS_CHECKER")
	private Integer variantIsChecker;
	
	//added by hakeem on 30 aug 2022
    @Column(name="CAR_COMPANY_ID")
	private Integer makeId;

    //added for car variant data
	@Column(name="CAR_VARIANT_CODE")
	private Integer variantCode;
    
	public Integer getMakeId() {
		return makeId;
	}

	public void setMakeId(Integer makeId) {
		this.makeId = makeId;
	}
	//end 


	public MasterCarVariant() {
	}


	public Integer getVariantId() {
		return variantId;
	}


	public void setVariantId(Integer variantId) {
		this.variantId = variantId;
	}


	public Integer getModelId() {
		return modelId;
	}


	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}


	public String getVariantName() {
		return variantName;
	}


	public void setVariantName(String variantName) {
		this.variantName = variantName;
	}


	public Date getVariantCreated() {
		return variantCreated;
	}


	public void setVariantCreated(Date variantCreated) {
		this.variantCreated = variantCreated;
	}


	public Date getVariantUpdated() {
		return variantUpdated;
	}


	public void setVariantUpdated(Date variantUpdated) {
		this.variantUpdated = variantUpdated;
	}


	public String getVariantActive() {
		return variantActive;
	}


	public void setVariantActive(String variantActive) {
		this.variantActive = variantActive;
	}


	public String getVariantDeleted() {
		return variantDeleted;
	}


	public void setVariantDeleted(String variantDeleted) {
		this.variantDeleted = variantDeleted;
	}




	public Integer getVariantIsChecker() {
		return variantIsChecker;
	}


	public void setVariantIsChecker(Integer variantIsChecker) {
		this.variantIsChecker = variantIsChecker;
	}

	public Integer getVariantCode() {
		return variantCode;
	}

	public void setVariantCode(Integer variantCode) {
		this.variantCode = variantCode;
	}
}
