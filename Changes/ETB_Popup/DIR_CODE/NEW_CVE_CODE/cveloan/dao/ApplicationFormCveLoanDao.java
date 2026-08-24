package com.mintstreet.loan.cveloan.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;

public class ApplicationFormCveLoanDao extends GenericDao<Integer, ApplicationFormCveLoan> {
	private static final long serialVersionUID = 1L;
	
	public ApplicationFormCveLoan getApplicationFormCveLoanBySeqId(Integer appSeqId) throws NoResultException {
		logger.info("CveDao.java getApplicationFormCveLoanBySeqId::: "+appSeqId);
       
        ApplicationFormCveLoan cveLoan = findById(appSeqId, ApplicationFormCveLoan.class);
        
        logger.info("CveDao.java getApplicationFormCveLoanBySeqId::list::"+cveLoan.toString());
            
        return cveLoan;
    }
	
	//Added for CVE
	public List<ApplicationFormCveLoan> getApplicationFormCveLoanByRefId(Integer appSeqId) throws SQLException
	{		   
		logger.info("getApplicationFormCveLoanByRefId CVE  appSeqId "+appSeqId);
        List<Object> params = new LinkedList<Object>();
	    params.add(appSeqId);
	    
	    String query="SELECT * FROM RUPEEPOWER_OCAS_T_00291 WHERE APP_SEQ_ID = ?";
	    logger.info("getApplicationFormCveLoanByRefId CVE:: "+query +" appSeqId "+appSeqId);
	    
	    @SuppressWarnings("unchecked")
		List<Object> Objectlist = (List<Object>) findByNativeQuery(query);
	    List<ApplicationFormCveLoan> cveList = new ArrayList<>();
	    logger.info("ApplicationFormPLLoanDao.JAVA Objectlist.size() :: "+Objectlist.size());
	    
	    for (int i = 0; i < Objectlist.size(); i++) 
	    {	
	      logger.info("ApplicationFormPLLoanDao.JAVA Objectlist.size() :: "+Objectlist.size());
	      ApplicationFormCveLoan cveData = new ApplicationFormCveLoan();
	      
	      Object[] cveObject = (Object[])Objectlist.get(i);  
	      logger.info("ApplicationFormPLLoanDao.JAVA Objectlist.get(i) :: "+Objectlist.get(i));
	      logger.info("ApplicationFormPLLoanDao.JAVA cveObject :: "+cveObject);
	      
	      cveData.setAppReferenceId((String)cveObject[1]);
	      logger.info("PlDao.JAVA getAppReferenceId:: "+cveData.getAppReferenceId());
	      
	      cveList.add(cveData);
	    	 logger.info("ApplicationFormPLLoanDao.JAVA cveList :: "+cveList);
		    }
		   
		    return cveList;  
        } 
	
	 public ApplicationFormCveLoan getApplicationFormCveLoanByAppReferenceId(String appReferenceId) throws NoResultException{
	        Map<String, Object> params = new HashMap<String, Object>(1);
	        params.put("appReferenceId", appReferenceId);        
	        List<ApplicationFormCveLoan> list = findByNamedQuery("ApplicationFormCveLoan.getApplicationFormCveLoanByAppReferenceId", params);
	        if(list.size()==1){
	        	return list.get(0);
	        }
	        return null;
	    }
	 
	public ApplicationFormCveLoan getApplicationFormCveLoanByAppReferenceIdAndMobileNo(String appReferenceId, String cbsMobileNumber) throws NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("appReferenceId", appReferenceId);
        params.put("cbsMobileNumber", cbsMobileNumber);
        List<ApplicationFormCveLoan> list = findByNamedQuery("ApplicationFormCveLoan.getApplicationFormCveLoanByAppReferenceIdAndMobileNo", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }

	public String getCveReferenceIdBySeqId(Integer appSeqId) throws SQLException {
		logger.info("getCveReferenceIdBySeqId>>appSeqId "+appSeqId);
	       
		Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appSeqId", appSeqId);
        
        String referenceId = (String) getSingleResult("ApplicationFormCveLoan.getCveReferenceIdBySeqId", params);
        
        logger.info("getCveReferenceIdBySeqId>>referenceId "+referenceId);
        
        return referenceId;
	}
	
	 public int updateCRMLeadId(String appCrmLeadId, String appReferenceId) throws SQLException
	 {
		 logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadId ( ) appCrmLeadId::"+appCrmLeadId+"--appReferenceId--"+appReferenceId);
		 
		 int appCrmLeadId1 = 0;
	    Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("APP_CRM_LEAD_ID", appCrmLeadId);
        params.put("APP_REFERENCE_ID", appReferenceId); 
        
	   String updateQuery = "UPDATE RUPEEPOWER_OCAS_T_00291 SET APP_CRM_LEAD_ID='"+appCrmLeadId+"' where APP_REFERENCE_ID = '" +appReferenceId+"'";
	   logger.info("ApplicationFormCveDao : LNo : 317 : update query response :: "+updateQuery);
  		 
  		//int updateResult = executeUpdate(updateQuery);
  		int updateResult = executeUpdateNative(updateQuery);
  		
		logger.info("ApplicationFormCveDao : LNo : 224 : update query response :: "+updateResult);
		
		if(updateResult==1)
		{		
			appCrmLeadId1 = updateResult;
			logger.info("ApplicationFormCveDao : LNo : 227 : update query response ::"+updateResult+"--appCrmLeadId1--"+appCrmLeadId1);
			return appCrmLeadId1;
		}
		return appCrmLeadId1;
		
	  } 
	
	 public boolean updateCRMLeadCVE(String appRefNumberCve, String crmLeadIdCve, String cveAppEmail) throws SQLException {
		 logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadCVE ( ) appCrmLeadId::"+crmLeadIdCve+"--appReferenceId--"+appRefNumberCve+"--cveAppEmail--"+cveAppEmail);
	     
	        List<Object> params = new LinkedList<Object>();
	        String appCrmLeadId = crmLeadIdCve;
	        logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadCVE ( ) appCrmLeadId :: "+appCrmLeadId+"******"+cveAppEmail);
	        
			String query = "SELECT APP_REFERENCE_ID,APP_CRM_LEAD_ID,CVE_APP_EMAIL FROM RUPEEPOWER_OCAS_T_00291 "
						+ " WHERE APP_REFERENCE_ID = ? ";
			params.add(appRefNumberCve);
			logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadCVE ( ) query :: "+query);
			
			@SuppressWarnings("unchecked")
			List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);
			if(Objectlist!=null && !Objectlist.isEmpty()){
				Boolean returnStatus = false;
				Object[] objects = Objectlist.toArray();
				for (Object object :objects) {
					 returnStatus =  (object!=null);
				}
				
				String updateQuery="UPDATE RUPEEPOWER_OCAS_T_00291 SET APP_CRM_LEAD_ID = '"+appCrmLeadId+"' ,CVE_APP_EMAIL ='"+cveAppEmail+"' WHERE APP_REFERENCE_ID= ?";
				logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadCVE ( ) updateQuery :: "+updateQuery);
				
				int updateResult = executeUpdateNative(updateQuery, params);
				logger.info("ApplicationFormCveDao.JAVA calling updateCRMLeadCVE ( ) update query response :: "+updateResult+" for appCrmLeadId : "+appCrmLeadId+" : returnStatus : "+returnStatus+"--cveAppEmail--"+cveAppEmail);
				if(updateResult==1){
					return returnStatus;
				}
			}
		    return false;
		}
	 
  }
