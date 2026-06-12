package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;


public class PrivacyRequestResponseDao extends GenericDao<Integer, PrivacyRequestResponse> {
	private static final Logger logger = LogManager.getLogger(PrivacyRequestResponseDao.class.getName());
	private static final long serialVersionUID = 1L;

	public PrivacyRequestResponse getPrivacyByLocale(String locale) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("privacyLocale", locale);
		params.put("privacyIsActive", "Y");
		try {
			PrivacyRequestResponse result = (PrivacyRequestResponse) getSingleResult("PrivacyRequestResponse.getPrivacyByLocale", params);
			logger.info("Successfully retrieved PrivacyRequestResponse for locale: {}", locale);
			return result;
		} catch (Exception e) {
			logger.info("Exception in getPrivacyByLocale", e);
		}
		return null;
	}
}
