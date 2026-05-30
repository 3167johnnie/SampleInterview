package com.mintstreet.loan.autoloan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterBikeVariant;

public class BikeVariantDao extends GenericDao<Integer,MasterBikeVariant> {
	private static final long serialVersionUID = -7595196332027248080L;

	public List<MasterBikeVariant> getBikeVariantByBikeModel(Integer modelId) throws Exception{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("modelId", modelId);        
        List<MasterBikeVariant> list =  findByNamedQuery("MasterBikeVariant.getBikeVariantByBikeModel", params);
        return list;
    }
}
