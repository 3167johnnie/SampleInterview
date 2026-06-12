package com.mintstreet.consent.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;

public class PrivacyNoticeDao extends GenericDao<Integer, PrivacyRequestResponse> { 
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LogManager.getLogger(PrivacyNoticeDao.class.getName());
	
	 public Integer getLanguageBylannguageCode(String lannguageCode) throws NoResultException, SQLException {
			if(lannguageCode == null){
				return null;
			}
			Map<String, Object> params = new HashMap<String, Object>(1);
			
			params.put("lannguageCode", lannguageCode);
			
			Long branchObj = null;
			try {
				branchObj = (Long) getSingleResult("MasterLanguage.getLanguageBylannguageCode", params);
			} catch (NoResultException nre){
				logger.info("MasterLanguage LN no. 30 Exception caught in getLanguageBylannguageCode :: ");
			}
			if(branchObj == null)
				return null;
			return branchObj.intValue();
		}
	 
	 public PrivacyRequestResponse getPrivacyRequestResponseByPrivacyLocale(String privacyLocale) throws NoResultException{
	        Map<String, Object> params = new HashMap<String, Object>(1);
	        params.put("privacyLocale", privacyLocale);        
	        List<PrivacyRequestResponse> list = findByNamedQuery("PrivacyRequestResponse.getPrivacyRequestResponseByPrivacyLocale", params);
	        if(list.size()==1){
	        	return list.get(0);
	        }
	        return null;
	    }
	  

}
