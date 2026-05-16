package com.mintstreet.common.service;

import java.sql.SQLException;

import com.mintstreet.common.dao.TemplateDao;
import com.mintstreet.common.entity.Template;

public class EmailService {
	
	private TemplateDao templateDao;
	
	
	
	public TemplateDao getTemplateDao() {
		return templateDao;
	}

	public void setTemplateDao(TemplateDao templateDao) {
		this.templateDao = templateDao;
	}

	public void saveTemplate(Template template) throws SQLException{
		templateDao.save(template.getTemplateId(), template);
	}
	
	public Template findDefaultEmailTemplate(Integer id) throws SQLException{
		return templateDao.findById(id, Template.class);
	}
	
}
