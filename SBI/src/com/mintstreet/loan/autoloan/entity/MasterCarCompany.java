package com.mintstreet.loan.autoloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_03135")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterCarCompany.getAllCarcompany", 
			query = "Select c from MasterCarCompany c where c.companyActive = 'Y' and c.companyDeleted ='N'"
					+ " AND c.companyIsChecker=0 order by LOWER(c.companyName) asc")
})

public class MasterCarCompany extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="car_company_id")
	private Integer companyId;

	@Column(name="car_company_name")
	private String companyName;

	@Column(name="car_company_short_name")
	private String companyShortName;

	

	@Column(name="car_company_active")
	private String companyActive;

	@Column(name="car_company_deleted")
	private String companyDeleted;
	
	@Column(name="CAR_COMPANY_IS_CHECKER")
	private Integer companyIsChecker;
	
	public MasterCarCompany() {
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
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
