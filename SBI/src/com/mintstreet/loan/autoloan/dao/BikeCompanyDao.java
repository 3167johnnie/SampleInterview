package com.mintstreet.loan.autoloan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterBikeCompany;
public class BikeCompanyDao extends GenericDao<Integer,MasterBikeCompany> {
	private static final long serialVersionUID = 1956612625003012579L;
	
	public List<MasterBikeCompany> getAllBikeCompanyByTwoWheelerType(Integer companyTwoWheelerTypeId) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("companyTwoWheelerTypeId", companyTwoWheelerTypeId);        
        List<MasterBikeCompany> list = findByNamedQuery("MasterBikeCompany.getAllBikeCompanyByTwoWheelerType", params);
        return list;
    }
}
