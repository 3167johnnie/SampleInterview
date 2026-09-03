package com.mintstreet.consent.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.ConsentWriteLog;

public class ConsentWriteDao extends GenericDao<Integer, ConsentWriteLog>{
	private static final long serialVersionUID = 404346123313078900L;
	
	 public List<ConsentWriteLog> getConsentRevokeData(String cifNumber,String loanType) throws SQLException, NoResultException {
		    Map<String, Object> params = new HashMap<>(2);
		    params.put("cifNumber", cifNumber);
		    params.put("responseCode", "200");
		    params.put("loanType", loanType);
		    return   findByNamedQuery("ConsentWriteLog.getConsentRevokeDataByCifNumber", params);
		    
		  
		  }
	 public ConsentWriteLog getConsentWriteLog(String cifNumber,String consentId) throws NoResultException{
	        Map<String, Object> params = new HashMap<String, Object>(1);
	        params.put("cifNumber", cifNumber);
			params.put("responseCode", "200");
			params.put("consentId", consentId);        
	        List<ConsentWriteLog> list = findByNamedQuery("ConsentWriteLog.getConsentWriteLog", params);
	        if(list.size()==1){
	        	return list.get(0);
	        }
	        return null;
	    }
	 public ConsentWriteLog getConsentWriteLogByNtbIdAndConsentId(String ntbNumber,String consentId) throws NoResultException{
	        Map<String, Object> params = new HashMap<String, Object>(1);
	        params.put("ntbNumber", ntbNumber);
			params.put("responseCode", "200");
			params.put("consentId", consentId);        
	        List<ConsentWriteLog> list = findByNamedQuery("ConsentWriteLog.getConsentWriteLogByNtbIdAndConsentId", params);
	        if(list.size()==1){
	        	return list.get(0);
	        }
	        return null;
	    }


}
