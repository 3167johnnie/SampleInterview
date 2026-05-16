package com.mintstreet.loan.homeloan.dao;

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
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;

public class ApplicationFormHomeLoanDao extends GenericDao<Integer, ApplicationFormHomeLoan> {
  private static final long serialVersionUID = 4043461233136936441L;
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByQuoteId(Integer appQuoteId) throws SQLException, NoResultException {
    Map<String, Object> params = new HashMap<>(1);
    params.put("appQuoteId", appQuoteId);
    List<ApplicationFormHomeLoan> list = findByNamedQuery("ApplicationFormHomeLoan.getApplicationFormHomeLoanByQuoteId", params);
    if (list.size() == 1)
      return list.get(0); 
    return null;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByAppReferenceId(String appReferenceId) throws SQLException, NoResultException {
    Map<String, Object> params = new HashMap<>(1);
    params.put("appReferenceId", appReferenceId);
    List<ApplicationFormHomeLoan> list = findByNamedQuery("ApplicationFormHomeLoan.getApplicationFormHomeLoanByAppReferenceId", params);
    if (list.size() == 1)
      return list.get(0); 
    return null;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByAppReferenceIdAndMobileNo(String appReferenceId, String appMobileNo) throws SQLException, NoResultException {
    Map<String, Object> params = new HashMap<>(2);
    params.put("appReferenceId", appReferenceId);
    params.put("appMobileNo", appMobileNo);
    List<ApplicationFormHomeLoan> list = findByNamedQuery("ApplicationFormHomeLoan.getApplicationFormHomeLoanByAppReferenceIdAndMobileNo", params);
    if (list.size() == 1)
      return list.get(0); 
    return null;
  }
  
  public String getLastGeneratedReferenceNumber(Integer loanTypeId) throws SQLException, NoResultException {
    int month = DateUtil.getCurrentMonth();
    String currentMonth = String.valueOf((month > 9) ? Integer.valueOf(month) : ("0" + month));
    String currentYear = DateUtil.getCurrentYearTwoDigit();
    String query = "SELECT MAX(APP_REFERENCE_ID) FROM RUPEEPOWER_OCAS_T_00195 WHERE APP_REFERENCE_ID like '" + Constants.INTIAL_STRING_HL + Constants.SOURCE_STRING_HL + currentMonth + currentYear + "%'";
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, null);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] appList = Objectlist.toArray();
      int i = 0;
      if (i < appList.length) {
        if (appList[i] == null)
          return null; 
        return appList[i].toString();
      } 
    } 
    return null;
  }
  
  public boolean isAppFoundForDedupInDropRejectStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException {
    List<Object> params = new LinkedList();
    String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00195 A, RUPEEPOWER_OCAS_T_00255 Q, RUPEEPOWER_OCAS_T_02024 S  WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID  AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID  AND S.LOAN_STATUS_TYPE IN (2)  AND S.LOAN_STATUS_IS_CHECKER=0  AND A.APP_REFERENCE_ID IS NULL ";
    if (!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())) {
      Integer leadDuplicationPeriod = Integer.valueOf(Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD));
      Date appEntryTime = DateUtil.getTodayDate((leadDuplicationPeriod.intValue() * Constants.DUPLICATION_MULTIFICATION_FACTOR.intValue()));
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(5, -leadDuplicationPeriod.intValue());
      appEntryTime.setTime(c.getTime().getTime());
      String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");
      if (APP_ENTRY_TIME != null)
        query = String.valueOf(query) + " AND A.APP_ENTRY_TIME > TO_DATE('" + APP_ENTRY_TIME + "','DD-MM-YYYY') "; 
    } 
    if (loanPurposeId != null) {
      query = String.valueOf(query) + " AND Q.quote_loan_purpose_id = ?";
      params.add(loanPurposeId);
    } 
    if (isdCode != null && !isdCode.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_ISD_CODE = ?";
      params.add(isdCode);
    } 
    if (appMobileNo != null && !appMobileNo.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_MOBILE_NO = ?";
      params.add(appMobileNo);
    } 
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] objects = Objectlist.toArray();
      int i = 0;
      if (i < objects.length) {
        int count = Integer.parseInt(objects[i].toString());
        if (count > 0)
          return true; 
        return false;
      } 
    } 
    return false;
  }
  
  public boolean isAppFoundForDedupInDropOffStage(String isdCode, String appMobileNo, Integer loanPurposeId) throws SQLException, NoResultException {
    List<Object> params = new LinkedList();
    String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00195 A, RUPEEPOWER_OCAS_T_00255 Q, RUPEEPOWER_OCAS_T_02024 S  WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID  AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID  AND S.LOAN_STATUS_TYPE IN (1)  AND S.LOAN_STATUS_IS_CHECKER=0  AND A.APP_REFERENCE_ID IS NULL ";
    if (!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())) {
      Integer leadDuplicationPeriod = Integer.valueOf(Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD));
      Date appEntryTime = DateUtil.getTodayDate((leadDuplicationPeriod.intValue() * Constants.DUPLICATION_MULTIFICATION_FACTOR.intValue()));
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(5, -leadDuplicationPeriod.intValue());
      appEntryTime.setTime(c.getTime().getTime());
      String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");
      if (APP_ENTRY_TIME != null)
        query = String.valueOf(query) + " AND A.APP_ENTRY_TIME > TO_DATE('" + APP_ENTRY_TIME + "','DD-MM-YYYY') "; 
    } 
    if (loanPurposeId != null) {
      query = String.valueOf(query) + " AND Q.quote_loan_purpose_id = ?";
      params.add(loanPurposeId);
    } 
    if (isdCode != null && !isdCode.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_ISD_CODE = ?";
      params.add(isdCode);
    } 
    if (appMobileNo != null && !appMobileNo.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_MOBILE_NO = ?";
      params.add(appMobileNo);
    } 
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] objects = Objectlist.toArray();
      int i = 0;
      if (i < objects.length) {
        int count = Integer.parseInt(objects[i].toString());
        if (count > 0)
          return true; 
        return false;
      } 
    } 
    return false;
  }
  
  public boolean isAppFoundForDedupInApplicationStage(String appReferenceId, String isdCode, String appMobileNo, Integer loanPurposeId) throws NoResultException {
    List<Object> params = new LinkedList();
    String query = "SELECT COUNT(APP_SEQ_ID) FROM RUPEEPOWER_OCAS_T_00195 A, RUPEEPOWER_OCAS_T_00255 Q, RUPEEPOWER_OCAS_T_02024 S  WHERE A.app_quote_id = Q.QUOTE_SEQUENCE_ID  AND A.APP_LOAN_STATUS_ID = S.LOAN_STATUS_ID  AND S.LOAN_STATUS_TYPE IN (3,5,9)  AND LOAN_STATUS_ID NOT IN (42,137)  AND S.LOAN_STATUS_IS_CHECKER=0  AND A.APP_REFERENCE_ID IS NOT NULL ";
    if (!"0".equalsIgnoreCase(Constants.APP_DUPLICATION_TIME_PERIOD.toString())) {
      Integer leadDuplicationPeriod = Integer.valueOf(Integer.parseInt(Constants.APP_DUPLICATION_TIME_PERIOD));
      Date appEntryTime = DateUtil.getTodayDate((leadDuplicationPeriod.intValue() * Constants.DUPLICATION_MULTIFICATION_FACTOR.intValue()));
      Calendar c = Calendar.getInstance();
      c.setTime(new Date());
      c.add(5, -leadDuplicationPeriod.intValue());
      appEntryTime.setTime(c.getTime().getTime());
      String APP_ENTRY_TIME = DateUtil.getStringDateFromDate(appEntryTime, "dd-MM-yyyy");
      if (APP_ENTRY_TIME != null)
        query = String.valueOf(query) + " AND A.APP_ENTRY_TIME > TO_DATE('" + APP_ENTRY_TIME + "','DD-MM-YYYY') "; 
    } 
    if (loanPurposeId != null) {
      query = String.valueOf(query) + " AND Q.quote_loan_purpose_id = ?";
      params.add(loanPurposeId);
    } 
    if (isdCode != null && !isdCode.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_ISD_CODE = ?";
      params.add(loanPurposeId);
    } 
    if (appMobileNo != null && !appMobileNo.isEmpty()) {
      query = String.valueOf(query) + " AND A.APP_MOBILE_NO = ?";
      params.add(appMobileNo);
    } 
    if (appReferenceId != null) {
      query = String.valueOf(query) + " AND A.APP_REFERENCE_ID <> ?";
      params.add(appReferenceId);
    } 
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(query, params);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] objects = Objectlist.toArray();
      int i = 0;
      if (i < objects.length) {
        int count = Integer.parseInt(objects[i].toString());
        if (count > 0)
          return true; 
        return false;
      } 
    } 
    return false;
  }
  
  public boolean isReferenceIdAvailable(String appRefererenceId) throws SQLException, NoResultException {
    List<Object> params = new LinkedList();
    params.add(appRefererenceId);
    String nativeQuery = "SELECT APP_REFERENCE_ID FROM RUPEEPOWER_OCAS_T_00195 WHERE APP_REFERENCE_ID = ?";
    this.logger.info("isReferenceIdAvailable " + nativeQuery + " appRefererenceId " + appRefererenceId);
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery, params);
    if (Objectlist != null && !Objectlist.isEmpty())
      return true; 
    return false;
  }
  
  public Integer getVisitByAppSeqId(Integer appSeqId) throws SQLException, NoResultException {
    Integer visitId = null;
    visitId = getVisitByAppSeqIdAndEntityName(appSeqId, "RUPEEPOWER_OCAS_T_15128");
    if (ValidatorUtil.isValid(visitId))
      return visitId; 
    return visitId;
  }
  
  public Integer getVisitByAppSeqIdAndEntityName(Integer appSeqId, String visitTableName) {
    List<Object> params = new LinkedList();
    String nativeQuery = "SELECT APP.APP_SEQ_ID, APP.APP_QUOTE_ID, QUOTE.QUOTE_SEQUENCE_ID, QUOTE.QUOTE_VISIT_ID, QUOTE.QUOTE_NEW_VISIT_ID, VISIT.VISIT_ID, VISIT.VISIT_CAMPAIGN_ID";
    nativeQuery = String.valueOf(nativeQuery) + " FROM RUPEEPOWER_OCAS_T_00195 APP, RUPEEPOWER_OCAS_T_00255 QUOTE, " + visitTableName + " VISIT ";
    nativeQuery = String.valueOf(nativeQuery) + " WHERE ";
    nativeQuery = String.valueOf(nativeQuery) + " APP.APP_QUOTE_ID = QUOTE.QUOTE_SEQUENCE_ID AND QUOTE.QUOTE_VISIT_ID = VISIT.VISIT_ID ";
    nativeQuery = String.valueOf(nativeQuery) + " AND APP_SEQ_ID=?";
    nativeQuery = String.valueOf(nativeQuery) + " ORDER BY APP_SEQ_ID DESC ";
    params.add(appSeqId);
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery, params);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] visitList = Objectlist.toArray();
      if (visitList != null && visitList.length > 0) {
        Object[] visitObj = (Object[])visitList[0];
        int visitId = Integer.parseInt(visitObj[3].toString());
        return Integer.valueOf(visitId);
      } 
    } 
    return null;
  }
  
  public ApplicationFormHomeLoan getApplicationFormHomeLoanByMobileAndSmsOtp(String appMobileNo, Integer appMobileVerificationCode) throws SQLException, NoResultException {
    Map<String, Object> params = new HashMap<>(2);
    params.put("appMobileVerificationCode", appMobileVerificationCode);
    params.put("appMobileNo", appMobileNo);
    List<ApplicationFormHomeLoan> list = findByNamedQuery("ApplicationFormHomeLoan.getApplicationFormHomeLoanByMobileAndSmsOtp", params);
    if (list.size() > 0)
      return list.get(0); 
    return null;
  }
  
  public Double getBaseRate() {
    String nativeQuery = "SELECT BASE_RATE_VALUE FROM RUPEEPOWER_OCAS_T_15873";
    List<BigDecimal> Objectlist = (List<BigDecimal>) findByNativeQuery(nativeQuery, null);
    if (Objectlist != null && !Objectlist.isEmpty()) {
      Object[] baseRate = Objectlist.toArray();
      if (baseRate != null && baseRate.length > 0) {
        Double baseRateVal = Double.valueOf(Double.parseDouble(baseRate[0].toString()));
        return baseRateVal;
      } 
    } 
    return null;
  }

}
