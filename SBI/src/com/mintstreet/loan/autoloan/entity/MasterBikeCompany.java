package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_02499")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterBikeCompany.getAllBikeCompanyByTwoWheelerType", query = "Select c from MasterBikeCompany "
			+ " c where c.companyTwoWheelerTypeId = :companyTwoWheelerTypeId "
			+ "and c.companyActive = 'Y' and c.companyDeleted ='N' AND c.companyIsChecker=0 order by LOWER(c.companyName) asc ")
})

public class MasterBikeCompany extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="BIKE_COMPANY_ID")
	private Integer companyId;

	@Column(name="BIKE_COMPANY_TYPE_ID")
	private Integer companyTwoWheelerTypeId;
	
	@Column(name="BIKE_COMPANY_NAME")
	private String companyName;

	@Column(name="BIKE_COMPANY_SHORT_NAME")
	private String companyShortName;

	@Column(name="BIKE_COMPANY_ACTIVE")
	private String companyActive;

	@Column(name="BIKE_COMPANY_DELETED")
	private String companyDeleted;
	
	@Column(name="BIKE_COMPANY_IS_CHECKER")
	private Integer companyIsChecker;

	public MasterBikeCompany() {
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getCompanyTwoWheelerTypeId() {
		return companyTwoWheelerTypeId;
	}

	public void setCompanyTwoWheelerTypeId(Integer companyTwoWheelerTypeId) {
		this.companyTwoWheelerTypeId = companyTwoWheelerTypeId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getCompanyActive() {
		return companyActive;
	}

	public void setCompanyActive(String companyActive) {
		this.companyActive = companyActive;
	}

	public String getCompanyDeleted() {
		return companyDeleted;
	}

	public void setCompanyDeleted(String companyDeleted) {
		this.companyDeleted = companyDeleted;
	}
	
	public Integer getCompanyIsChecker() {
		return companyIsChecker;
	}

	public void setCompanyIsChecker(Integer companyIsChecker) {
		this.companyIsChecker = companyIsChecker;
	}
}
