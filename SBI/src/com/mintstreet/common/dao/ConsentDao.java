package com.mintstreet.common.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.entity.Consent;

public class ConsentDao extends GenericDao<Integer, Consent>{
	private static final long serialVersionUID = -9091372691031889149L;
	
	public Consent getConsentByLoanType(Integer loanTypeId) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("consentLoanType", loanTypeId);        
        
        List<Consent> list = findByNamedQuery("Consent.getConsentByLoanType", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	
	public Integer getConsentIdByLoanType(Integer loanTypeId) throws NoResultException, SQLException {
		
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("consentLoanType", loanTypeId);        
        
        Integer consentId = (Integer) getSingleResult("Consent.getConsentIdByLoanType", params);
        
        if (consentId == null)
        	return null;
        
        return consentId;
    }
	
	public Consent getConsentByLoanAndCustomerType(Integer loanTypeId, String customerType) {
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("consentLoanType", loanTypeId);
        params.put("consentCustomerType", customerType);
        
        List<Consent> list = findByNamedQuery("Consent.getConsentByLoanAndCustomerType", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }

}
