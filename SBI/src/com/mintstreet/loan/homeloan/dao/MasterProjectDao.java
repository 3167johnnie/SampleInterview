package com.mintstreet.loan.homeloan.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.homeloan.entity.MasterProject;

public class MasterProjectDao extends GenericDao<Integer, MasterProject> {
	private static final long serialVersionUID = 1L;
	
	public List<MasterProject> findProjectByBuilderId(Integer projectBuilderId) throws SQLException, NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("projectBuilderId", projectBuilderId);
        List<MasterProject> list = findByNamedQuery("MasterProject.findProjectByBuilderId", params);
        return list;
    }
	
	public List<MasterProject> getAllProject(String query) throws SQLException, NoResultException {
		if(!ValidatorUtil.isValid(query)){
			return null;
		}
		 Map<String, Object> params = new HashMap<String, Object>(1);
		 query="%" + query.toLowerCase() + "%";
		 params.put("projectName", query); 
		 return findByNamedQuery("MasterProject.getAllProject",params);
	}
	
	public List<MasterProject> getProjectByProjectName(String projectName) throws SQLException, NoResultException {
		if(!ValidatorUtil.isValid(projectName)){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("projectName", projectName.toLowerCase()); 
		return findByNamedQuery("MasterProject.findProjectByProjectName",params);
	}
	public List<MasterProject> getBuilderName() throws NoResultException {
		return findByNamedQuery("MasterProject.getBuilderName");
	}
}
