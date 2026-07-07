package com.mintstreet.loan.cveloan.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.loan.cveloan.dao.ApplicationFormCveLoanDao;
import com.mintstreet.loan.cveloan.dao.MasterCveProductDao;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;

public class CveLoanService {
	
	private static final Logger logger = LogManager.getLogger(CveLoanService.class.getName());

	private ApplicationFormCveLoanDao applicationFormCveLoanDao;
	
	private MasterCveProductDao cveProductDao;
	
	public ApplicationFormCveLoan getApplicationFormCveLoanByAppSeqId(Integer appSeqId) throws SQLException, NoResultException {
		ApplicationFormCveLoan application = applicationFormCveLoanDao.findById(appSeqId, ApplicationFormCveLoan.class);
		if(application==null){
 			return null;
 		}
		return application;
	}
	
	public MasterCveProduct getCveProductByCode(String cveProductCode) {
		try {
			return (MasterCveProduct)this.cveProductDao.getCveProductByCode(cveProductCode);
		} catch (NoResultException | SQLException e) {
			logger.info("CveLoanService getCveProductByCategory LNo:36", e.getMessage());
		} catch (Exception e) {
			logger.info("CveLoanService getCveProductByCategory LNo:38", e.getMessage());
		}
		return null;
	}
	
	public List<MasterCveProduct> getCveProducts() {
		try {
			return cveProductDao.getCveProducts();
		} catch (NoResultException | SQLException e) {
			logger.info("CveLoanService getCveProductByCategory LNo:47", e.getMessage());
		} catch (Exception e) {
			logger.info("CveLoanService getCveProductByCategory LNo:49", e.getMessage());
		}
		return null;
	}

	public MasterCveProductDao getCveProductDao() {
		return cveProductDao;
	}

	public void setCveProductDao(MasterCveProductDao cveProductDao) {
		this.cveProductDao = cveProductDao;
	}
	  
}
