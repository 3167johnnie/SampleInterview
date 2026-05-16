package com.mintstreet.common.service;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.MasterQualificationDao;
import com.mintstreet.common.dao.ValidateRequestConfigDao;
import com.mintstreet.common.entity.MasterQualification;
import com.mintstreet.common.entity.ValidateRequestConfig;

public class ValidatorService {
	private static final Logger logger = LogManager.getLogger(ValidatorService.class.getName());
	private ValidateRequestConfigDao validateRequestConfigDao;
	private MasterQualificationDao masterQualificationDao;
	
	public List<ValidateRequestConfig> getValidateRequestConfig(Integer requestIndex, Integer loanTypeId) {
		return  validateRequestConfigDao.getAllValidateFormConfigByFormIndexId(requestIndex, loanTypeId);
	}
	public List<ValidateRequestConfig> getAllValidateRequestConfigByRequestIndexMobile(Integer requestIndex, Integer loanTypeId, Integer requestValidationType) {
		return  validateRequestConfigDao.getAllValidateRequestConfigByRequestIndexMobile(requestIndex, loanTypeId, requestValidationType);
	}
	public List<ValidateRequestConfig> getAllValidateRequestConfigByParentIdValue(Integer requestIndex,Integer elementParentId,String requestValidationExactValue) {
		return  validateRequestConfigDao.getAllValidateRequestConfigByParentIdValue(requestIndex, elementParentId, requestValidationExactValue);
	}
	
	public List<ValidateRequestConfig> getAllValidateRequestConfigByParentId(Integer requestIndex,Integer elementParentId,String requestValidationExactValue) {
		return  validateRequestConfigDao.getAllValidateRequestConfigByParentId(requestIndex, elementParentId, requestValidationExactValue);
	}
	
	public List<ValidateRequestConfig> getAllValidateRequestConfigByParentName(Integer requestIndex,String elementParentName,Integer OnchangeValue,Integer loanType) {
		 List <ValidateRequestConfig> validateConfigList = validateRequestConfigDao.getAllValidateRequestConfigByParentName(requestIndex, elementParentName, OnchangeValue, loanType);
		 return validateConfigList;
	}
	
	public List<ValidateRequestConfig> getAllValidateRequestConfigByNotFilter(Integer requestIndex,String elementString,Integer loanType) {
		 List <ValidateRequestConfig> validateConfigList = validateRequestConfigDao.getAllValidateRequestConfigByNotFilter(requestIndex, elementString, loanType);
		 return validateConfigList;
	}

	public List<MasterQualification> getAllQualification(){
		List<MasterQualification> masterQualificationsList = null;
		try{

			masterQualificationsList = masterQualificationDao.getAllQualification();
		} catch(SQLException e) {
			logger.info("ValidatorService.java LNo : 51 : Exception in getting MasterQualification", e);
		} catch(Exception e) {
			logger.info("ValidatorService.java LNo : 51 : Exception in getting MasterQualification", e);
		}
		return masterQualificationsList;
	}
	
	public MasterQualification getAllQualificationById(Integer qualificationId){
		MasterQualification masterQualification = null;
		try{

			masterQualification = masterQualificationDao.findById(qualificationId, MasterQualification.class);
		} catch(IllegalArgumentException e) {
			logger.info("ValidatorService.java LNo : 51 : Exception in getting MasterQualification", e);
		} catch(Exception e){
			logger.info("ValidatorService.java LNo : 63 : Exception in getting MasterQualification", e);
		}
		return masterQualification;
	}
	
	
	public List<ValidateRequestConfig> getValidateRequestConfigForAlternateMobile(Integer requestIndex, Integer loanTypeId) throws NoResultException {
		return  validateRequestConfigDao.getAllValidateFormConfigByFormIndexIdAlternateMobile(requestIndex, loanTypeId);
	}
	
	public ValidateRequestConfigDao getValidateRequestConfigDao() {
		return validateRequestConfigDao;
	}
	public void setValidateRequestConfigDao(
			ValidateRequestConfigDao validateRequestConfigDao) {
		this.validateRequestConfigDao = validateRequestConfigDao;
	}

	public MasterQualificationDao getMasterQualificationDao() {
		return masterQualificationDao;
	}

	public void setMasterQualificationDao(MasterQualificationDao masterQualificationDao) {
		this.masterQualificationDao = masterQualificationDao;
	}
	

	
}
