package com.mintstreet.loan.cveloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_00049")
@NamedQueries({
	@NamedQuery(name="MasterCveProduct.getCveProductByCode", query="SELECT m FROM MasterCveProduct m where "
			+ " m.cveProductCrmCode=:cveProductCrmCode and m.cveProductActive='Y' AND m.cveProductDeleted='N' "
			+ " order by m.cveProductName asc "),
	@NamedQuery(name="MasterCveProduct.getCveProducts", query="SELECT m FROM MasterCveProduct m where "
			+ " m.cveProductActive='Y' AND m.cveProductDeleted='N' order by m.cveProductName asc "),
})

public class MasterCveProduct extends com.mintstreet.common.entity.Domain<Integer>implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="RUPEEPOWER_OCAS_T_00049_CVEPRODUCTID_GENERATOR", allocationSize=1 )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUPEEPOWER_OCAS_T_00049_CVEPRODUCTID_GENERATOR")
	@Column(name="CVE_PRODUCT_ID")
	private Integer cveProductId;

	@Column(name="CVE_PRODUCT_NAME")
	private String cveProductName;

	@Column(name="CVE_PRODUCT_ACTIVE")
	private String cveProductActive;

	@Column(name="CVE_PRODUCT_DELETED")
	private String cveProductDeleted;

	@Column(name="CVE_PRODUCT_CRM_CODE")
	private String cveProductCrmCode;
	
	@Column(name="CVE_PRODUCT_CATEGORY")
	private String cveProductCategory;

	public Integer getCveProductId() {
		return cveProductId;
	}

	public void setCveProductId(Integer cveProductId) {
		this.cveProductId = cveProductId;
	}

	public String getCveProductName() {
		return cveProductName;
	}

	public void setCveProductName(String cveProductName) {
		this.cveProductName = cveProductName;
	}

	public String getCveProductActive() {
		return cveProductActive;
	}

	public void setCveProductActive(String cveProductActive) {
		this.cveProductActive = cveProductActive;
	}

	public String getCveProductDeleted() {
		return cveProductDeleted;
	}

	public void setCveProductDeleted(String cveProductDeleted) {
		this.cveProductDeleted = cveProductDeleted;
	}

	public String getCveProductCrmCode() {
		return cveProductCrmCode;
	}

	public void setCveProductCrmCode(String cveProductCrmCode) {
		this.cveProductCrmCode = cveProductCrmCode;
	}

	public String getCveProductCategory() {
		return cveProductCategory;
	}

	public void setCveProductCategory(String cveProductCategory) {
		this.cveProductCategory = cveProductCategory;
	}

}
