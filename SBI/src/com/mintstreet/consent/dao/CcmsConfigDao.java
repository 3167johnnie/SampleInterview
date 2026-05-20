package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.CCMSConfig;

public class CcmsConfigDao extends GenericDao<Integer, CCMSConfig> {
	private static final Logger logger = LogManager.getLogger(CcmsConfigDao.class.getName());
	
	private static final long serialVersionUID = 8914051008484037488L;

	public CCMSConfig getCcmsConfigById(Integer id) {
		if(id == null){
			return null;
		}
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("ccmsId", id);
		
		CCMSConfig configObj = null;
		try {
			configObj = (CCMSConfig) getSingleResult("CCMSConfig.getConfigDetails", params);
		} catch (NoResultException nre){
			logger.info("CcmsConfigDao LN no. 29 Exception caught in getCcmsConfigById :: ", nre);
		} catch (IllegalArgumentException e){
			logger.info("CcmsConfigDao LN no. 31 Exception caught in getCcmsConfigById :: ", e);
		} catch (Exception e){
			logger.info("CcmsConfigDao LN no. 33 Exception caught in getCcmsConfigById :: ", e);
		}

		if(configObj == null)
			return null;

		return configObj;
	}

}
