package com.mintstreet.loan.educationloan.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
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
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;

public class ApplicationFormEducationLoanDao  extends GenericDao<Integer, ApplicationFormEducationLoan>{
	private static final long serialVersionUID = -2722472467545039131L;
	
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo) throws SQLException, NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(2);
        params.put("appReferenceId", appReferenceId);
        params.put("appMobileNo", appMobileNo);
        List<ApplicationFormEducationLoan> list = findByNamedQuery("ApplicationFormEducationLoan.getApplicationFormEducationLoanByAppReferenceIdAndMobileNo", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }

	public ApplicationFormEducationLoan getApplicationFormEducationLoanByQuoteId(Integer  appQuoteId) throws SQLException, NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appQuoteId", appQuoteId);        
        List<ApplicationFormEducationLoan> list = findByNamedQuery("ApplicationFormEducationLoan.getApplicationFormEducationLoanByQuoteId", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByAppReferenceId(String appReferenceId) throws SQLException, NoResultException{
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("appReferenceId", appReferenceId);        
        List<ApplicationFormEducationLoan> list = findByNamedQuery("ApplicationFormEducationLoan.getApplicationFormEducationLoanByAppReferenceId", params);
        if(list.size()==1){
        	return list.get(0);
        }
        return null;
    }
	
	@SuppressWarnings("unchecked")
	public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException, NoResultException{

		int month=DateUtil.getCurrentMonth();
		String currentMonth = String.valueOf((month>9?month:"0"+month));
		String currentYear = DateUtil.getCurrentYearTwoDigit();
		String query = "SELECT MAX(APP_REFERENCE_ID) FROM RUPEEPOWER_OCAS_T_00120 WHERE APP_REFERENCE_ID like '"+Constants.INTIAL_STRING_EL+Constants.SOURCE_STRING_EL+currentMonth+currentYear+"%'";
		
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
	public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
        List<Object> params = new LinkedList<Object>();


		String query = "SELECT  COUNT(APP_SEQ_ID)  FROM RUPEEPOWER_OCAS_T_00120 A, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
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
				query += " AND APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		
		if(loanPurposeId!=null){
			query += " AND APP_PRODUCT_VARIANT_ID =?";
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
	public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
        List<Object> params = new LinkedList<Object>();


		String query = "SELECT  COUNT(APP_SEQ_ID)  FROM RUPEEPOWER_OCAS_T_00120 A, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE  A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
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
				query += " AND APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		
		if(loanPurposeId!=null){
			query += " AND APP_PRODUCT_VARIANT_ID = ?" ;
			params.add(loanPurposeId);
		}
		if( isdCode !=null && !isdCode.isEmpty()){
			query += " AND A.APP_ISD_CODE =?" ;
			params.add(isdCode);
		}
		if( appMobileNo !=null && !appMobileNo.isEmpty()){
			query += " AND A.APP_MOBILE_NO =?" ;
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
	public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException{
        List<Object> params = new LinkedList<Object>();


		
		String query = "SELECT  COUNT(APP_SEQ_ID)  FROM RUPEEPOWER_OCAS_T_00120 A, RUPEEPOWER_OCAS_T_02024 S "
				+ " WHERE A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID "
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
				query += " AND APP_ENTRY_TIME > TO_DATE('"+APP_ENTRY_TIME+"','DD-MM-YYYY') ";
			}
		}
		if(loanPurposeId!=null){
			query += " AND APP_PRODUCT_VARIANT_ID = ?";
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
		if(appReferenceId!=null){
			query += " AND APP_REFERENCE_ID <> ?";
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
	    String nativeQuery="SELECT APP_REFERENCE_ID FROM RUPEEPOWER_OCAS_T_00120 WHERE APP_REFERENCE_ID = ?";
	    logger.info("isReferenceIdAvailable "+nativeQuery +" appRefererenceId "+appRefererenceId);
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);

	    if(Objectlist!=null && !Objectlist.isEmpty()){
	    	return true;
	    }
	    return false;
	}
	public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException, NoResultException{
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
	    nativeQuery +=" FROM RUPEEPOWER_OCAS_T_00120 APP, RUPEEPOWER_OCAS_T_00168 QUOTE, "+visitTableName+" VISIT ";
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
	/*@SuppressWarnings("unchecked")
	public boolean isVLPStudentIdExists(int appVLPStudentAppId) throws SQLException, NoResultException{
        List<Object> params = new LinkedList<Object>();
	    String nativeQuery="SELECT APP_VLP_STUDENT_APP_ID FROM RUPEEPOWER_OCAS_T_00120 WHERE APP_VLP_STUDENT_APP_ID = ?";
		params.add(appVLPStudentAppId);
	    logger.info("isVLPStudentIdExists "+nativeQuery );
	    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery,params);
	    if(Objectlist!=null && !Objectlist.isEmpty()){
	    	return true;
	    }
	    return false;
	}*/
	public ApplicationFormEducationLoan getApplicationFormEducationLoanByMobileAndSmsOtp(String appMobileNo,Integer appMobileVerificationCode) throws NoResultException{ 
		Map<String, Object> params = new HashMap<String, Object>(2);
		 params.put("appMobileVerificationCode", appMobileVerificationCode);
	     params.put("appMobileNo", appMobileNo);
        List<ApplicationFormEducationLoan> list = findByNamedQuery("ApplicationFormEducationLoan.getApplicationFormEducationLoanByMobileAndSmsOtp",params);
        if(list.size()>0){
        	return list.get(0);
        }
        return null;
    }
	
}
