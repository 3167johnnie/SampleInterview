package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.MasterLanguage;

public class MasterLanguageDao extends GenericDao<Integer, MasterLanguage> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(MasterLanguageDao.class.getName());

	@SuppressWarnings("unchecked")
	public List<MasterLanguage> getAllActiveLanguages() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<MasterLanguage> languageList = (List<MasterLanguage>) findByNamedQueryRaw(
					"MasterLanguage.getAllActiveLanguages", params);
			logger.info("Active language count : " + (languageList == null ? 0 : languageList.size()));
			return languageList;
		} catch (Exception e) {
			logger.info("Exception in getAllActiveLanguages", e);
		}
		return null;
	}
}
