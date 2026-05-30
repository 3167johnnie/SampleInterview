package com.mintstreet.loan.educationloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_05040")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class MasterCourseType extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="COURSE_TYPE_ID")
	private Integer courseTypeId;

	@Column(name="COURSE_FOR_ABROAD")
	private Integer courseForAbroad;

	@Column(name="COURSE_TYPE_ACTIVE")
	private String courseTypeActive;

	

	@Column(name="COURSE_TYPE_DELETED")
	private String courseTypeDeleted;

	

	

	@Column(name="COURSE_TYPE_NAME")
	private String courseTypeName;

	

	

	public MasterCourseType() {
	}

	public Integer getCourseTypeId() {
		return this.courseTypeId;
	}

	public void setCourseTypeId(Integer courseTypeId) {
		this.courseTypeId = courseTypeId;
	}

	public Integer getCourseForAbroad() {
		return this.courseForAbroad;
	}

	public void setCourseForAbroad(Integer courseForAbroad) {
		this.courseForAbroad = courseForAbroad;
	}

	public String getCourseTypeActive() {
		return this.courseTypeActive;
	}

	public void setCourseTypeActive(String courseTypeActive) {
		this.courseTypeActive = courseTypeActive;
	}

	

	public String getCourseTypeDeleted() {
		return this.courseTypeDeleted;
	}

	public void setCourseTypeDeleted(String courseTypeDeleted) {
		this.courseTypeDeleted = courseTypeDeleted;
	}

	

	public String getCourseTypeName() {
		return this.courseTypeName;
	}

	public void setCourseTypeName(String courseTypeName) {
		this.courseTypeName = courseTypeName;
	}

	
}
