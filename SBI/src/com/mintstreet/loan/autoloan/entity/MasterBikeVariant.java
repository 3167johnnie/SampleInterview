package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_02808")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterBikeVariant.getBikeVariantByBikeModel", query="SELECT m FROM MasterBikeVariant m where "
			+ " m.modelId = :modelId and m.variantActive='Y' AND m.variantDeleted='N' AND m.variantIsChecker=0 order by LOWER(m.variantName) asc "),
})
public class MasterBikeVariant extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	
	@Column(name="BIKE_VARIANT_ID")
	private Integer variantId;

	@Column(name="BIKE_VARIANT_MODEL_ID")
	private Integer modelId;
	
	@Column(name="BIKE_VARIANT_NAME")
	private String variantName;

	@Column(name="BIKE_VARIANT_ACTIVE")
	private String variantActive;

	@Column(name="BIKE_VARIANT_DELETED")
	private String variantDeleted;
	
	@Column(name="BIKE_VARIANT_IS_CHECKER")
	private Integer variantIsChecker;

	public MasterBikeVariant() {
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
}
