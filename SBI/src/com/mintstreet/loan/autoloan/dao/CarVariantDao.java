package com.mintstreet.loan.autoloan.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterCarVariant;

public class CarVariantDao extends GenericDao<Integer,MasterCarVariant> {
	private static final long serialVersionUID = 7668291873594231832L;

	//changed by Hakeem on 01/09/2022
	public List<MasterCarVariant> getCarVariantByCarmodel(Integer modelId, Integer companyId) throws SQLException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("modelId", modelId);
        if (companyId != null) {
        	params.put("makeId", companyId);
        }
        List<MasterCarVariant> list =  findByNamedQuery("MasterCarVariant.getCarVariantByCarmodel", params);
        return list;
    }
}
