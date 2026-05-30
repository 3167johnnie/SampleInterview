package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_02600")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterBikeModel.getBikeModelByCompany", query="SELECT m FROM MasterBikeModel m where "
			+ "m.modelCompanyId = :modelCompanyId  "
			+ " and m.modelActive='Y' AND m.modelDeleted='N' AND m.modelIsChecker=0 order by LOWER(m.modelName) asc "),
})
public class MasterBikeModel extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BIKE_MODEL_ID")
	private Integer modelId;
	
	@Column(name="BIKE_MODEL_COMPANY_ID")
	private Integer modelCompanyId;
	
	@Column(name="BIKE_MODEL_NAME")
	private String modelName;

	@Column(name="BIKE_MODEL_ACTIVE")
	private String modelActive;
	
	@Column(name="BIKE_MODEL_DELETED")
	private String modelDeleted;
	
	@Column(name="BIKE_MODEL_IS_CHECKER")
	private Integer modelIsChecker;
	
	public MasterBikeModel() {
	}


	public Integer getModelId() {
		return modelId;
	}


	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}


	public Integer getModelCompanyId() {
		return modelCompanyId;
	}


	public void setModelCompanyId(Integer modelCompanyId) {
		this.modelCompanyId = modelCompanyId;
	}


	public String getModelName() {
		return modelName;
	}


	public void setModelName(String modelName) {
		this.modelName = modelName;
	}


	public String getModelActive() {
		return modelActive;
	}


	public void setModelActive(String modelActive) {
		this.modelActive = modelActive;
	}


	public String getModelDeleted() {
		return modelDeleted;
	}


	public void setModelDeleted(String modelDeleted) {
		this.modelDeleted = modelDeleted;
	}
	public Integer getModelIsChecker() {
		return modelIsChecker;
	}


	public void setModelIsChecker(Integer modelIsChecker) {
		this.modelIsChecker = modelIsChecker;
	}

}
