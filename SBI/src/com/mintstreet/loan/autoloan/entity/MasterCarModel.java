package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_03363")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterCarModel.getCarModelByCompany", query="SELECT m FROM MasterCarModel m where "
			+ "m.companyId = :companyId  "
			+ " and m.modelActive='Y' AND m.modelDeleted='N' AND m.modelIsChecker=0 order by LOWER(m.modelName) asc  ")
})
public class MasterCarModel extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="CAR_MODEL_ID")
	private Integer modelId;

	@Column(name="CAR_MODEL_COMPANY_ID")
	private Integer companyId;

	@Column(name="CAR_MODEL_NAME")
	private String modelName;
	
	@Column(name="CAR_MODEL_ACTIVE")
	private String modelActive;

	@Column(name="CAR_MODEL_DELETED")
	private String modelDeleted;
	
	@Column(name="CAR_MODEL_IS_CHECKER")
	private Integer modelIsChecker;

	//added for car variant data
	@Column(name="CAR_MODEL_CODE")
	private Integer modelCode;

	
	public MasterCarModel() {
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
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

	

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	public Integer getModelIsChecker() {
		return modelIsChecker;
	}

	public void setModelIsChecker(Integer modelIsChecker) {
		this.modelIsChecker = modelIsChecker;
	}

	public Integer getModelCode() {
		return modelCode;
	}

	public void setModelCode(Integer modelCode) {
		this.modelCode = modelCode;
	}

}
