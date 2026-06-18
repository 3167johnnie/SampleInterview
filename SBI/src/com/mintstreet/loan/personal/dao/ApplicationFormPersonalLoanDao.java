package com.mintstreet.loan.personal.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;


public class ApplicationFormPersonalLoanDao  extends GenericDao<Integer, ApplicationFormPersonalLoan>{
	private static final long serialVersionUID = -2722472467545039131L;	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo) throws NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("appReferenceId", appReferenceId);
        params.put("appMobileNo", appMobileNo);
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }

	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByQuoteId(Integer  appQuoteId) throws SQLException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appQuoteId", appQuoteId);        
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByQuoteId", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByAppReferenceId(String appReferenceId) throws SQLException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appReferenceId", appReferenceId);        
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByAppReferenceId", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	
	@SuppressWarnings("unchecked")
	public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException{

		int month=DateUtil.getCurrentMonth();
		String currentMonth = String.valueOf((month>9?month:"0"+month));
		String currentYear = DateUtil.getCurrentYearTwoDigit();
		String query = "SELECT MAX(APP_REFERENCE_ID) FROM RUPEEPOWER_OCAS_T_00360 WHERE APP_REFERENCE_ID like '"+Constants.INTIAL_STRING_PL+Constants.SOURCE_STRING_PL+currentMonth+currentYear+"%'";
		
		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, null);
		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] appList = Objectlist.toArray();
			for (int i = 0; i < appList.length;) {
				if(appList[i]==null){
					return null;
				}
				return appList[i].toString();
			}
		}
		return null;
	}
	
	public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{


		List<Object> params = new LinkedList<Object>();
		String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00360 A, RUPEEPOWER_OCAS_T_00440 Q, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID "
				+ " AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
				+ " AND S.LOAN_STATUS_TYPE IN (2) "
				+ " AND S.LOAN_STATUS_IS_CHECKER=0 "
				+ " AND A.APP_REFERENCE_ID IS NULL ";
		
		if(!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())){
			Integer leadDuplicationPeriod = Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD);
			Date appEntryTime = DateUtil.getTodayDate(leadDuplicationPeriod*Constants.DUPLICATION_MULTIFICATION_FACTOR);
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        c.add(Calendar.DATE, -leadDuplicationPeriod);
	        appEntryTime.setTime(c.getTime().getTime());
	        String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");

			if(APP_ENTRY_TIME!=null){
				query += " AND A.APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		
		if(loanPurposeId!=null){
			query += " AND Q.quote_loan_purpose_id = ?";
			params.add(loanPurposeId);
		}
		if( isdCode !=null && !isdCode.isEmpty()){
			query += " AND A.APP_ISD_CODE = ?" ;
			params.add(isdCode);
		}
		if( appMobileNo !=null && !appMobileNo.isEmpty()){
			query += " AND A.APP_MOBILE_NO = ?" ;
			params.add(appMobileNo);
		}
		

		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);

		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] objects = Objectlist.toArray();
			for (int i = 0; i < objects.length;) {
				int count = Integer.parseInt(objects[i].toString());
				if(count>0){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{


		List<Object> params = new LinkedList<Object>();
		String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00360 A, RUPEEPOWER_OCAS_T_00440 Q, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID "
				+ " AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
				+ " AND S.LOAN_STATUS_TYPE IN (1) "
				+ " AND S.LOAN_STATUS_IS_CHECKER=0 "
				+ " AND A.APP_REFERENCE_ID IS NULL ";
		
		if(!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())){
			Integer leadDuplicationPeriod = Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD);
			Date appEntryTime = DateUtil.getTodayDate(leadDuplicationPeriod*Constants.DUPLICATION_MULTIFICATION_FACTOR);
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        c.add(Calendar.DATE, -leadDuplicationPeriod);
	        appEntryTime.setTime(c.getTime().getTime());
	        String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");

			if(APP_ENTRY_TIME!=null){
				query += " AND A.APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		if(loanPurposeId!=null){
			query += " AND Q.quote_loan_purpose_id = ?";
			params.add(loanPurposeId);
		}
		if( isdCode !=null && !isdCode.isEmpty()){
			query += " AND A.APP_ISD_CODE = ?";
			params.add(isdCode);
		}
		if( appMobileNo !=null && !appMobileNo.isEmpty()){
			query += " AND A.APP_MOBILE_NO = ?";
			params.add(appMobileNo);
		}

		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);

		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] objects = Objectlist.toArray();
			for (int i = 0; i < objects.length;) {
				int count = Integer.parseInt(objects[i].toString());
				if(count>0){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	
	@SuppressWarnings("unchecked")
	public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException{


		List<Object> params = new LinkedList<Object>();
		String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00360 A, RUPEEPOWER_OCAS_T_00440 Q, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID "
				+ " AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
				+ " AND S.LOAN_STATUS_TYPE IN (3,5,9)  AND LOAN_STATUS_ID NOT IN (42,137) "
				+ " AND S.LOAN_STATUS_IS_CHECKER=0 "
				+ " AND A.APP_REFERENCE_ID IS NOT NULL ";
		
		if(!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())){
			Integer leadDuplicationPeriod = Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD);
			Date appEntryTime = DateUtil.getTodayDate(leadDuplicationPeriod*Constants.DUPLICATION_MULTIFICATION_FACTOR);
	        Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        c.add(Calendar.DATE, -leadDuplicationPeriod);
	        appEntryTime.setTime(c.getTime().getTime());
	        String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");

			if(APP_ENTRY_TIME!=null){
				query += " AND A.APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		if(loanPurposeId!=null){
			query += " AND Q.quote_loan_purpose_id = ?";
			params.add(loanPurposeId);
		}
		if( isdCode !=null && !isdCode.isEmpty()){
			query += " AND A.APP_ISD_CODE = ?";
			params.add(loanPurposeId);
		}
		if( appMobileNo !=null && !appMobileNo.isEmpty()){
			query += " AND A.APP_MOBILE_NO = ?" ;
			params.add(appMobileNo);
		}
		if(appReferenceId!=null){
			query += " AND A.APP_REFERENCE_ID <> ?" ;
			params.add(appReferenceId);
		}
		
		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);

		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] objects = Objectlist.toArray();
			for (int i = 0; i < objects.length;) {
				int count = Integer.parseInt(objects[i].toString());
				if(count>0){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isReferenceIdAvailable(String appRefererenceId) throws SQLException{
		List<Object> params = new LinkedList<Object>();
	    params.add(appRefererenceId);
	    String nativeQuery="SELECT APP_REFERENCE_ID FROM RUPEEPOWER_OCAS_T_00360 WHERE APP_REFERENCE_ID = ?";
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);

	    if(Objectlist!=null && !Objectlist.isEmpty()){
	    	return true;
	    }
	    return false;
	}
	
	
	public Integer getVisitByAppSeqId(Integer appSeqId) {
		Integer visitId = null;
		
		visitId = getVisitByAppSeqIdAndEntityName(appSeqId, "RUPEEPOWER_OCAS_T_15128");
		if(ValidatorUtil.isValid(visitId)){
			return visitId;
		}
		return visitId;
	}
	
	
	@SuppressWarnings("unchecked")
	public Integer getVisitByAppSeqIdAndEntityName(Integer appSeqId, String visitTableName){
		List<Object> params = new LinkedList<Object>();
	    String nativeQuery="SELECT APP.APP_SEQ_ID, APP.APP_QUOTE_ID, QUOTE.QUOTE_SEQUENCE_ID, QUOTE.QUOTE_VISIT_ID, QUOTE.QUOTE_NEW_VISIT_ID, VISIT.VISIT_ID, VISIT.VISIT_CAMPAIGN_ID";
	    nativeQuery +=" FROM RUPEEPOWER_OCAS_T_00360 APP, RUPEEPOWER_OCAS_T_00440 QUOTE, "+visitTableName+" VISIT ";
	    nativeQuery +=" WHERE ";
	    nativeQuery +=" APP.APP_QUOTE_ID = QUOTE.QUOTE_SEQUENCE_ID AND QUOTE.QUOTE_VISIT_ID = VISIT.VISIT_ID ";
	    nativeQuery +=" AND APP_SEQ_ID= ?";
	    nativeQuery +=" ORDER BY APP_SEQ_ID DESC ";
	    params.add(appSeqId);
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);
	    if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] visitList = Objectlist.toArray();
			if(visitList!=null && visitList.length>0){
				Object[] visitObj = (Object[])visitList[0];

				int visitId = Integer.parseInt(visitObj[3].toString());
				return visitId;
			}
		}
	    return null;
	}
	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByMobileAndSmsOtp(String appMobileNo,Integer appMobileVerificationCode) throws SQLException{ 
		Map<String, Object> params = new HashMap<String, Object>(2);
		 params.put("appMobileVerificationCode", appMobileVerificationCode);
	     params.put("appMobileNo", appMobileNo);
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByMobileAndSmsOtp",params);
        if(list.size()>0){
        	return list.get(0);
        }
        return null;
    }
	
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByIdspmDumpId(Integer appIDSPMDumpId) throws SQLException{ 
		Map<String, Object> params = new HashMap<String, Object>(2);
	     params.put("appIDSPMDumpId", appIDSPMDumpId);
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByIdspmDumpId",params);
        if(list.size()>0){
        	return list.get(0);
        }
        return null;
    }
	public ApplicationFormPersonalLoan getApplicationFormPersonalLoanByUniqueRefNummber(String appCustomerUniqueId) throws NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appCustomerUniqueId", appCustomerUniqueId);        
        List<ApplicationFormPersonalLoan> list = findByNamedQuery("ApplicationFormPersonalLoan.getApplicationFormPersonalLoanByUniqueRefNummber", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	@SuppressWarnings("unchecked")
	public Integer getInstantLoanCount(Integer appPersonalLoanId) throws SQLException{
		List<Object> params = new LinkedList<Object>();
	    String nativeQuery="SELECT COUNT(APP.APP_SEQ_ID) ";
	    nativeQuery +=" FROM RUPEEPOWER_OCAS_T_00360 APP ";
	    nativeQuery +=" WHERE ";
	    nativeQuery +=" APP_PRODUCT_VARIANT_ID=?";
	    nativeQuery +=" AND APP_DEMAND_LOAN_ACC_NO IS NOT NULL ";
	    params.add(appPersonalLoanId);
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);
	    if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] visitList = Objectlist.toArray();
			if(visitList!=null && visitList.length>0){
				Integer count = Integer.parseInt(visitList[0].toString());
				return count;
			}
		}
	    return null;
	}
	
	
	//Added by Pratima for CVE
	@SuppressWarnings("unchecked")
	public String getLastGeneratedReferenceNumberCVE(Integer loanTypeId) throws SQLException{

		int month=DateUtil.getCurrentMonth();
		String currentMonth = String.valueOf((month>9?month:"0"+month));
		String currentYear = DateUtil.getCurrentYearTwoDigit();
		String query = "SELECT MAX(APP_REFERENCE_ID) FROM RUPEEPOWER_OCAS_T_00291 WHERE APP_REFERENCE_ID like '"+"2"+"4"+currentMonth+currentYear+"%'";
		
		List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, null);
		if(Objectlist!=null && !Objectlist.isEmpty()){
			Object[] appList = Objectlist.toArray();
			for (int i = 0; i < appList.length;) {
				if(appList[i]==null){
					return null;
				}
				return appList[i].toString();
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isReferenceIdAvailableCVE(String appRefererenceId) throws SQLException{
		List<Object> params = new LinkedList<Object>();
	    params.add(appRefererenceId);
	    String nativeQuery="SELECT APP_REFERENCE_ID FROM RUPEEPOWER_OCAS_T_00291 WHERE APP_REFERENCE_ID = ?";
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);

	    if(Objectlist!=null && !Objectlist.isEmpty()){
	    	return true;
	    }
	    return false;
	}
	
	//Added by Hakeem for CVE
	  public ApplicationFormCveLoan getApplicationFormCveLoanByRefId(String appReferenceId) throws SQLException
	{		   
        List<Object> params = new LinkedList<Object>();
	    params.add(appReferenceId);
	    
	    String nativeQuery="SELECT APP_REFERENCE_ID FROM RUPEEPOWER_OCAS_T_00291 WHERE APP_REFERENCE_ID = ?";
	    
	    @SuppressWarnings("unchecked")
		List<ApplicationFormCveLoan> list = (List<ApplicationFormCveLoan>) findByNativeQuery(nativeQuery,params);

	    if(list.size()==1){
        	return list.get(0);
        }
        return null;
         
    } 
	
	public ApplicationFormCveLoan getAppReferenceIdListFromCVEtable() throws SQLException
	  {		
		
	  	String query = "select * from RUPEEPOWER_OCAS_T_00291 where CVE_APP_CONSENT_REVOKE='CRMPASS'";
	      
	    @SuppressWarnings("unchecked")
		
	    List<ApplicationFormCveLoan> Objectlist = (List<ApplicationFormCveLoan>) findByNativeQuery(query);
	    
	  // for (int i = 0; i < Objectlist.size(); i++) 
	  // {	
	      ApplicationFormCveLoan cveData = new ApplicationFormCveLoan();
	      
	      if(Objectlist.size() == 1){
	        	return Objectlist.get(0);
	        }
	   //}
	      return cveData;     
	  }
	
	
	public List<ApplicationFormCveLoan> updateCRMFlagCVEtable() throws SQLException  
	{
		
		List<ApplicationFormCveLoan> returnStatus = new ArrayList<ApplicationFormCveLoan>();
		
		String query = "select APP_REFERENCE_ID,CVE_APP_CONSENT_REVOKE from RUPEEPOWER_OCAS_T_00291 where CVE_APP_CONSENT_REVOKE='CRMPASS'";
	    
		@SuppressWarnings("unchecked")
		List<Object> Objectlist = (List<Object>) findByNativeQuery(query);
	    List<ApplicationFormCveLoan> cveList = new ArrayList<>();
	    
	    for (int i = 0; i < Objectlist.size(); i++) 
	    {
	      
	      ApplicationFormCveLoan cveData1 = new ApplicationFormCveLoan();
	      
	      Object[] cveObject1 = (Object[])Objectlist.get(i);  
	      
	      cveData1.setAppReferenceId((String)cveObject1[0]);
	      
	      //cveData1.setCveAppConsentRevoke((String)cveObject1[1]);
	      
    	 cveList.add(cveData1);
    		 
		   List<Object> params = new LinkedList<Object>();
		   params.add(cveData1.getAppReferenceId());
		 
		   //String updateQuery = "UPDATE RUPEEPOWER_OCAS_T_00291 SET CVE_APP_CONSENT_REVOKE='CRMPASSED' where APP_REFERENCE_ID=?";
		   String updateQuery = "UPDATE RUPEEPOWER_OCAS_T_00291 SET CVE_APP_CONSENT_REVOKE='CRMPASSED' where CVE_APP_CONSENT_REVOKE='CRMPASS'";
    		 
    		 //int updateResult = executeUpdateNative(updateQuery, params);	
    		int updateResult = executeUpdateNative(updateQuery);	
 			if(updateResult==1)
 			{	
				return returnStatus;
 			} 
    		 
	   }
	  return cveList;  	
	    
	}
	//Ended by Pratima for CVE
 }
