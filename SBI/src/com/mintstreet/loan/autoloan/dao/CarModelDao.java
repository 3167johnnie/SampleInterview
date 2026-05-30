package com.mintstreet.loan.autoloan.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.autoloan.entity.MasterCarModel;

public class CarModelDao extends GenericDao<Integer,MasterCarModel> {
	private static final long serialVersionUID = -7041607333750481836L;

	public List<MasterCarModel> getCarModelByCompany(Integer companyId) throws SQLException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("companyId", companyId);        
        List<MasterCarModel> list = findByNamedQuery("MasterCarModel.getCarModelByCompany", params);
        return list;
    }
}
