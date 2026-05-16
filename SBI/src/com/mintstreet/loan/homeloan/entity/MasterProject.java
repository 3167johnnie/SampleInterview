package com.mintstreet.loan.homeloan.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_10815")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterProject.getBuilderName", query="SELECT p FROM MasterProject p"
			+ " WHERE p.projectActive='Y' and p.projectDeleted='N' "
			+ " ORDER BY p.projectName asc"),
	@NamedQuery(name="MasterProject.findProjectByBuilderId", query="SELECT p FROM MasterProject p"
			+ " WHERE p.projectActive='Y' and p.projectDeleted='N' and p.projectBuilderId = :projectBuilderId "
			+ " AND p.projectIsChecker=0 "
			+ " ORDER BY p.projectName asc"),
			
	 @NamedQuery(name="MasterProject.getAllProject", query = "Select p from MasterProject p "
	 		+ " where p.projectActive='Y' and p.projectDeleted='N' "
			+ " AND p.projectIsChecker=0 "
	 		+ " AND LOWER(p.projectBuilderName) like :projectName  order by p.projectBuilderName asc "),
		   		
   	 @NamedQuery(name="MasterProject.findProjectByProjectName", query = "Select p from MasterProject p "
   	 		+ " where p.projectActive='Y' and p.projectDeleted='N' "
   	 		+ " AND p.projectIsChecker=0 "
   	 		+ " AND LOWER(p.projectBuilderName)=:projectName  order by p.projectBuilderName asc ")
})

public class MasterProject extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="PROJECT_ID")
	private Integer projectId;

	

	

	@Column(name="PROJECT_ACTIVE")
	private String projectActive;

	@Column(name="PROJECT_BUILDER_ID")
	private Integer projectBuilderId;

	

	@Column(name="PROJECT_DELETED")
	private String projectDeleted;

	@Column(name="PROJECT_MAKER_ID")
	private Integer projectMakerId;

	@Column(name="PROJECT_NAME")
	private String projectName;

	@Column(name="PROJECT_BUILDER_NAME")
	private String projectBuilderName;
	
	@Column(name="PROJECT_CODE")
	private String projectCode;
	
	@Column(name="PROJECT_IS_CHECKER")
	private Integer projectIsChecker;
	
	public MasterProject() {
	}

	public Integer getProjectId() {
		return this.projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}
	
	

	public String getProjectActive() {
		return this.projectActive;
	}

	public void setProjectActive(String projectActive) {
		this.projectActive = projectActive;
	}

	public Integer getProjectBuilderId() {
		return this.projectBuilderId;
	}

	public void setProjectBuilderId(Integer projectBuilderId) {
		this.projectBuilderId = projectBuilderId;
	}

	

	public String getProjectDeleted() {
		return this.projectDeleted;
	}

	public void setProjectDeleted(String projectDeleted) {
		this.projectDeleted = projectDeleted;
	}

	public Integer getProjectMakerId() {
		return this.projectMakerId;
	}

	public void setProjectMakerId(Integer projectMakerId) {
		this.projectMakerId = projectMakerId;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectBuilderName() {
		return projectBuilderName;
	}

	public void setProjectBuilderName(String projectBuilderName) {
		this.projectBuilderName = projectBuilderName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	
	public Integer getProjectIsChecker() {
		return projectIsChecker;
	}

	public void setProjectIsChecker(Integer projectIsChecker) {
		this.projectIsChecker = projectIsChecker;
	}	

}
