package com.mintstreet.loan.cveloan.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;

public class MasterCveProductDao extends GenericDao<Integer,MasterCveProduct> {
	
	private static final long serialVersionUID = -2411049336797583206L;
	
	public MasterCveProduct getCveProductByCode(String cveProductCode) throws SQLException, NoResultException {
        
		Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("cveProductCrmCode", cveProductCode);
        List<MasterCveProduct> list = findByNamedQuery("MasterCveProduct.getCveProductByCode", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }

	public List<MasterCveProduct> getCveProducts() throws NoResultException, SQLException {
		
        @SuppressWarnings("unchecked")
		List<MasterCveProduct> list = (List<MasterCveProduct>)findByNamedQueryRaw("MasterCveProduct.getCveProducts");
        if(list!= null && list.size()>=1){
        	return list;
        }
        return null;
	}

}
