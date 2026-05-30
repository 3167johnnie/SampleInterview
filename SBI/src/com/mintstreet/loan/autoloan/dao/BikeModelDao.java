package com.mintstreet.loan.autoloan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterBikeModel;

public class BikeModelDao extends GenericDao<Integer,MasterBikeModel> {
	private static final long serialVersionUID = 1285028204495468507L;
	
	public List<MasterBikeModel> getBikeModelByCompany(Integer modelCompanyId) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("modelCompanyId", modelCompanyId);        
        List<MasterBikeModel> list = findByNamedQuery("MasterBikeModel.getBikeModelByCompany", params);
        return list;
    }
}
