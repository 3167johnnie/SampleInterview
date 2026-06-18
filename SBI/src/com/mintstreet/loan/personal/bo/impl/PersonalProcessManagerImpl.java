package com.mintstreet.loan.personal.bo.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mintstreet.callback.service.CallBackService;
import com.mintstreet.campaign.dao.MarTechDao;
import com.mintstreet.campaign.entity.MarTech;
import com.mintstreet.common.bo.BureauLinkRequestNew;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.CBSCustomerInformation;
import com.mintstreet.common.bo.CBSLoanAccountInformation;
import com.mintstreet.common.bo.CRMRequest;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.RSMResponse;
import com.mintstreet.common.bo.RsmData;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.engine.CommonEngine;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.BankLmsUserRole;
import com.mintstreet.common.entity.BureauLinkRequestResponse;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterDistrict;
import com.mintstreet.common.entity.MasterDocumentType;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterState;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.service.TaskExecutorService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.status.StatusManager;
import com.mintstreet.common.status.StatusManagerResponse;
import com.mintstreet.common.status.StatusRequest;
import com.mintstreet.common.util.BureauLinkUtil;
import com.mintstreet.common.util.CRMServiceNew;
import com.mintstreet.common.util.CbsUtil;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.FileHelper;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.MapperUtil;
import com.mintstreet.common.util.RefGenerateUtilPL;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.integration.edms.action.EdmsFinalServiceAction;
import com.mintstreet.integration.edms.bo.FstoreDoc;
import com.mintstreet.integration.pan.action.PanServiceAction;
import com.mintstreet.integration.pan.bo.PanApiInputParams;
import com.mintstreet.integration.pan.bo.PanApiReturnResponse;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.loan.personal.util.PersonalLoanHelper;
import com.mintstreet.loan.product.entity.MasterPlProduct;

import freemarker.template.utility.StringUtil;

public class PersonalProcessManagerImpl {
  private static final Logger logger = LogManager.getLogger(PersonalProcessManagerImpl.class.getName());
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private PersonalLoanService personalLoanService;
  
  @Autowired
  private PersonalLoanHelper personalLoanHelper;
  
  @Autowired
  private CommunicationManagerImpl communicationManagerImpl;
  
  @Autowired
  private TaskExecutorService taskExecutorService;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  @Autowired
  private CommonEngine commonEngine;
  
  @Autowired
  private CbsUtil cbsUtil;
  
  @Autowired
  private BureauLinkUtil bureauLinkUtil;
  
  @Autowired
  private CRMServiceNew crmServiceNew;
  
  @Autowired
  private RefGenerateUtilPL refGenerateUtil;
  
  @Autowired
  private CallBackService callBackService;
  
  @Autowired
  private PanServiceAction panServiceAction;
  
  @Autowired
  private MarTechDao marTechDao;

  @Autowired
  private EdmsFinalServiceAction edmsServiceAction;
  
  public volatile String appRefKey;
  
  public volatile String lastReferenceNumber;
  
  public double maxLoanAmount;
  
  public double maxAmt;
  
  public static Integer branchCode;
  
  public static String refCve;
  
  public static String getcif;
  
  StringBuffer formatJson;
  
  public JSONObject processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId, String ajaxPostUrl, String imageName) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      ApplicationFormPersonalLoan appFormData = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
      if (appFormData == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      if (ValidatorUtil.isValid(imageNo)) {
        String fullPath = "";
        if (imageNo.intValue() == 1) {
          if (appFormData.getAppPhotoIdName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppPhotoIdName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.AL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } 
        } else if (imageNo.intValue() == 2) {
          if (appFormData.getAppIdentityProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + appFormData.getAppIdentityProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } 
        } else if (imageNo.intValue() == 3) {
          if (appFormData.getAppResidenceProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + appFormData.getAppResidenceProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } 
        } else if (imageNo.intValue() == 4) {
          if (appFormData.getAppIncomeProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + appFormData.getAppIncomeProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } 
        } else if (imageNo.intValue() == 5) {
          if (appFormData.getAppPensionProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + appFormData.getAppPensionProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPensionProofId(null);
            appFormData.setAppPensionProofName(null);
          } else if (appFormData.getAppPropertyProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + appFormData.getAppPropertyProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPropertyProofId(null);
            appFormData.setAppPropertyProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.PL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPropertyProofId(null);
            appFormData.setAppPropertyProofName(null);
            appFormData.setAppPensionProofId(null);
            appFormData.setAppPensionProofName(null);
          } 
          appFormData.setAppEmploymentProofId(null);
          appFormData.setAppEmploymentProofName(null);
        } 
        appFormData = this.personalLoanService.save(appFormData);
        json.put("status", "success");
        json.put("message", "Document deleted.");
      } else {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      } 
    } catch(SQLException ne){
		logger.info("PersonalProcessManagerImpl.java LNo : 227 : Exception Caught", ne);
		json.put("status", "error");
	    json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
	}  
    return json;
  }
  
  public JSONObject processToDocumentPickupUploaded(Integer appSeqId, ApplicationFormPersonalLoan appForm, Integer bankLMSUserId, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      if (appForm == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      ApplicationFormPersonalLoan appFormData = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
      if (appFormData == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      if ((ValidatorUtil.isValid(appForm.getAppPhotoId()) && !ValidatorUtil.isValid(appForm.getAppPhotoIdName())) || (
        !ValidatorUtil.isValid(appForm.getAppPhotoId()) && ValidatorUtil.isValid(appForm.getAppPhotoIdName()))) {
        json.put("status", "error");
        json.put("message", "Please select Photo Id and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppPhotoId()))
        appFormData.setAppPhotoId(appForm.getAppPhotoId()); 
      if (ValidatorUtil.isValid(appForm.getAppPhotoIdName()))
        appFormData.setAppPhotoIdName(appForm.getAppPhotoIdName()); 
      if ((ValidatorUtil.isValid(appForm.getAppIdentityProofId()) && !ValidatorUtil.isValid(appForm.getAppIdentityProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppIdentityProofId()) && ValidatorUtil.isValid(appForm.getAppIdentityProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Identitiy proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppIdentityProofId()))
        appFormData.setAppIdentityProofId(appForm.getAppIdentityProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppIdentityProofName()))
        appFormData.setAppIdentityProofName(appForm.getAppIdentityProofName()); 
      if ((ValidatorUtil.isValid(appForm.getAppResidenceProofId()) && !ValidatorUtil.isValid(appForm.getAppResidenceProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppResidenceProofId()) && ValidatorUtil.isValid(appForm.getAppResidenceProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Residence proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppResidenceProofId()))
        appFormData.setAppResidenceProofId(appForm.getAppResidenceProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppResidenceProofName()))
        appFormData.setAppResidenceProofName(appForm.getAppResidenceProofName()); 
      if ((ValidatorUtil.isValid(appForm.getAppIncomeProofId()) && !ValidatorUtil.isValid(appForm.getAppIncomeProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppIncomeProofId()) && ValidatorUtil.isValid(appForm.getAppIncomeProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Income proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppIncomeProofId()))
        appFormData.setAppIncomeProofId(appForm.getAppIncomeProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppIncomeProofName()))
        appFormData.setAppIncomeProofName(appForm.getAppIncomeProofName()); 
      if ((ValidatorUtil.isValid(appForm.getAppPensionProofId()) && !ValidatorUtil.isValid(appForm.getAppPensionProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppPensionProofId()) && ValidatorUtil.isValid(appForm.getAppPensionProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select and upload corresponding pension document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppPensionProofId()))
        appFormData.setAppPensionProofId(appForm.getAppPensionProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppPensionProofName()))
        appFormData.setAppPensionProofName(appForm.getAppPensionProofName()); 
      if ((ValidatorUtil.isValid(appForm.getAppEmploymentProofId()) && !ValidatorUtil.isValid(appForm.getAppEmploymentProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppEmploymentProofId()) && ValidatorUtil.isValid(appForm.getAppEmploymentProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Employment proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppEmploymentProofId()))
        appFormData.setAppEmploymentProofId(appForm.getAppEmploymentProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppEmploymentProofName()))
        appFormData.setAppEmploymentProofName(appForm.getAppEmploymentProofName()); 
      if ((ValidatorUtil.isValid(appForm.getAppPropertyProofId()) && !ValidatorUtil.isValid(appForm.getAppPropertyProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppPropertyProofId()) && ValidatorUtil.isValid(appForm.getAppPropertyProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Property proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppPropertyProofId()))
        appFormData.setAppPropertyProofId(appForm.getAppPropertyProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppPropertyProofName()))
        appFormData.setAppPropertyProofName(appForm.getAppPropertyProofName()); 
      if (appForm.getAppDocPickupCheck() == null)
        appForm.setAppDocPickupCheck(Integer.valueOf(1)); 
      appFormData.setAppDocPickupCheck(appForm.getAppDocPickupCheck());
      if (appFormData.getAppDocPickupCheck().intValue() != 4 && 
        appForm.getAppDocPickupDateDT() != null && ValidatorUtil.isValid(appForm.getAppDocPickupTimeString())) {
        appFormData.setAppDocPickupDateDT(appForm.getAppDocPickupDateDT());
        appFormData.setAppDocPickupTimeString(appForm.getAppDocPickupTimeString());
        Date date = DateUtil.getDateByPassingHHMM(appForm.getAppDocPickupTimeString());
        appFormData.setAppDocPickupTime(date);
        if (appForm.getAppDocPickupCheck().intValue() == 2) {
          if (appForm.getAppPickupAddress1() != null)
            appFormData.setAppOfficeAddress1(appForm.getAppPickupAddress1()); 
          if (appForm.getAppPickupAddress2() != null)
            appFormData.setAppOfficeAddress2(appForm.getAppPickupAddress2()); 
          if (appForm.getAppPickupStateId() != null)
            appFormData.setAppOfficeStateId(appForm.getAppPickupStateId()); 
          if (appForm.getAppPickupCityId() != null)
            appFormData.setAppOfficeCityId(appForm.getAppPickupCityId()); 
          if (appForm.getAppPickupDistrictId() != null)
            appFormData.setAppOfficeDistrictId(appForm.getAppPickupDistrictId()); 
          if (appForm.getAppPickupPincode() != null)
            appFormData.setAppOfficePincode(appForm.getAppPickupPincode()); 
        } 
        if (appForm.getAppPickupAddress1() != null)
          appFormData.setAppPickupAddress1(appForm.getAppPickupAddress1()); 
        if (appForm.getAppPickupAddress2() != null)
          appFormData.setAppPickupAddress2(appForm.getAppPickupAddress2()); 
        if (appForm.getAppPickupStateId() != null)
          if (SessionUtil.getPensionLoanApplicationSequenceId() != null && appSeqId.intValue() == SessionUtil.getPensionLoanApplicationSequenceId().intValue()) {
            if (appForm.getAppPermanentStateId() != null) {
              appFormData.setAppPickupStateId(appForm.getAppPermanentStateId());
            } else if (appForm.getAppPickupStateId() != null) {
              appFormData.setAppPickupStateId(appForm.getAppPickupStateId());
            } 
          } else {
            appFormData.setAppPickupStateId(appForm.getAppPickupStateId());
          }  
        if (appForm.getAppPickupCityId() != null)
          if (SessionUtil.getPensionLoanApplicationSequenceId() != null && appSeqId.intValue() == SessionUtil.getPensionLoanApplicationSequenceId().intValue()) {
            if (appForm.getAppPermanentCityId() != null) {
              appFormData.setAppPickupCityId(appForm.getAppPermanentCityId());
              appFormData.setAppPickupDistrictId(appForm.getAppPermanentDistrictId());
            } else if (appForm.getAppPickupCityId() != null) {
              appFormData.setAppPickupCityId(appForm.getAppPickupCityId());
              appFormData.setAppPickupDistrictId(appForm.getAppPickupDistrictId());
            } 
          } else {
            appFormData.setAppPickupCityId(appForm.getAppPickupCityId());
            appFormData.setAppPickupDistrictId(appForm.getAppPickupDistrictId());
          }  
        if (appForm.getAppPickupDistrictId() != null && appForm.getAppPickupCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue())
          appFormData.setAppPickupDistrictId(appForm.getAppPickupDistrictId()); 
        if (appFormData.getAppPickupCityId() != null && 
          appFormData.getAppPickupCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue() && 
          appFormData.getAppPickupDistrictId() == null) {
          json.put("status", "error");
          json.put("message", "Invalid params");
          return json;
        } 
        if (appForm.getAppPickupPincode() != null)
          if (SessionUtil.getPensionLoanApplicationSequenceId() != null && appSeqId.intValue() == SessionUtil.getPensionLoanApplicationSequenceId().intValue()) {
            if (appForm.getAppPermanentPincode() != null) {
              appFormData.setAppPickupPincode(appForm.getAppPermanentPincode());
            } else if (appForm.getAppPickupPincode() != null) {
              appFormData.setAppPickupPincode(appForm.getAppPickupPincode());
            } 
          } else {
            appFormData.setAppPickupPincode(appForm.getAppPickupPincode());
          }  
        if (this.SbiUtil.isPincodeValid(appForm.getAppPickupPincode(), appForm.getAppPickupStateId())) {
          appFormData.setAppPickupPincode(appForm.getAppPickupPincode());
        } else {
          json.put("status", "error");
          if (appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
            json.put("message", "Entered pincode does not belong to entered state");
          } else {
            json.put("message", "Entered pincode does not belong to entered state|appForm.appPickupPincode|");
          } 
          return json;
        } 
        StatusRequest statusRequest = new StatusRequest();
        statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
        statusRequest.setHaveLoanOffer(false);
        if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
          statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
        } else {
          statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
        } 
        statusRequest.setState(18);
        statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
        statusRequest.setRsmDecision(0);
        statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
        statusRequest.setApplicationLeadType(appFormData.getAppDataSourceId().intValue());
        StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
        if (statusManagerResponse.isInitiateCallAttempt())
          appFormData.setAppTotalCallAttempt(Integer.valueOf(0)); 
        if (statusManagerResponse.getStatus() != 0) {
          appFormData.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
        } else if (appFormData.getAppLoanStatusId().intValue() == 0) {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        if (statusManagerResponse.isEligibleToInsertLog() && statusManagerResponse.getStatusCallLogs() > 0) {
          Date callDocPickupTime = DateUtil.changeDateFormatToDate(String.valueOf(String.valueOf(String.valueOf(appForm.getAppDocPickupDate()))) + " " + appForm.getAppDocPickupTimeString(), "MM/dd/yyyy HH:mm");
          this.personalLoanHelper.insertCallLog(appSeqId, bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, callDocPickupTime, true);
        } 
        if (appFormData.getAppDocsEnteredTime() == null)
          appFormData.setAppDocsEnteredTime(new Date()); 
        if (!Constants.CRM_BYPASS && appFormData.getAppCRMLeadId() != null)
          logger.info("PersonalProcessManagerImpl.java LNo: 485 : DO NOT CALLING callCrmUpdate Service..............."); 
      } 
      logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL1");
      appFormData = this.personalLoanService.save(appFormData);
      logger.info("PLProcessImpl.java Line 423   *************before svae method  autoLoanService file" + appFormData.getAppReferenceId());
      
      FstoreDoc doc = new FstoreDoc();
      Integer branchCode = this.commonService.getBranchCodeByBranchId(appFormData.getAppBranchId());
      doc.setAppPhotoIdName(appFormData.getAppPhotoIdName());
      doc.setAppIdentityProofName(appFormData.getAppIdentityProofName());
      doc.setAppResidenceProofName(appFormData.getAppResidenceProofName());
      doc.setAppIncomeProofName(appFormData.getAppIncomeProofName());
      doc.setAppPensionProofName(appFormData.getAppPensionProofName());
      
      edmsServiceAction.uploadDocumentsToEDMS(doc, Constants.PERSONAL_LOAN_ID, appFormData.getAppReferenceId(), branchCode);
      
      json.put("status", "success");
      json.put("message", "Document uploaded successfully. Please proceed for the next steps.");
      if (!bankLMSUserId.equals(Constants.OTHER_ID_INTEGER) || appFormData.getAppAssignedLmsSalesUserId() != null) {
        if (bankLMSUserId.equals(Constants.OTHER_ID_INTEGER))
          bankLMSUserId = appFormData.getAppAssignedLmsSalesUserId(); 
        this.taskExecutorService.sendingSMSForPersonalLoan(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE, Integer.valueOf(0), appFormData, bankLMSUserId, false);
      } 
    } catch(SQLException ne){
		logger.info("PersonalProcessManagerImpl.java LNo : 476 : Exception Caught", ne);
		json.put("status", "error");
	    json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
	}
    return json;
  }
  
  public JSONObject processAddCoapplicant(Integer moduleId, ApplicationFormPersonalLoanQuote quote, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      Integer appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      quote = this.personalLoanHelper.saveCoApplicantDetails(quote);
      if (quote == null) {
        json.put("status", "error");
        json.put("message", "Co-applicant not upadated try again.");
        return json;
      } 
      json.put("status", "success");
      json.put("message", "Co-applicant upadated.");
    } catch(SQLException ne){
		logger.info("PersonalProcessManagerImpl.java LNo : 504 : Exception Caught", ne);
		json.put("status", "error");
	    json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
	} 
    return json;
  }
  
  public JSONObject OTP(Integer appSeqId, Integer stateId, String name, int appApplyingFrom, int appOTPVerifyType, String isdCode, String mobile, String email, String inputOtp, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId) {
    JSONObject json = new JSONObject();
    boolean isAlternate = false;
    try {
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
        return json;
      } 
      ApplicationFormPersonalLoan application = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
      if (application == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
        return json;
      } 
      if (mobile.startsWith("ALT_")) {
          isAlternate = true;
          mobile = mobile.substring(4);
          application.setAppAlternateMobileNumber(mobile);
      }
      if( mobile != null && mobile.length() > 0 && !isAlternate) {
    	  //logger.info("PersonalProcessManagerImpl LNO::541 " + mobile);
    	  application.setAppMobileNo(mobile);
      }
      Integer alertCount = Integer.valueOf(0);
      if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
        if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (
          !ValidatorUtil.isValid(name) || !ValidatorUtil.isValidMobile(mobile) || !ValidatorUtil.isValidEmail(email))) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
          json.put("alertCount", alertCount);
        } 
        if (ValidatorUtil.isValid(name)) {
          SessionUtil.setApplicantName(name);
          SessionUtil.setApplicantName(name);
          application.setAppFirstName(name);
        } 
        if (ValidatorUtil.isValid(mobile))
          if (!isAlternate) {
            SessionUtil.setMobile(mobile);
          } else {
            SessionUtil.setalternateMobileNumber(mobile);
          }  
        if (ValidatorUtil.isValid(email)) {
          application.setAppWorkEmail(email);
          SessionUtil.setEmail(email);
        } 
        HttpServletRequest request = RequestUtil.getServletRequest();
        String resend = request.getParameter("changeM");
        if (isAlternate) {
          //logger.info("isdcode chekig  inside the if (isAlternate) " + isdCode);
          application.setAppAltISDCode(isdCode);
          application.setAppAlternateMobileNumber(mobile);
          if (application.getAppAltMobileVerificationCode() == null || 
            "N".equals(application.getAppAltMobileVerified())) {
            if (resend == null && application.getAppAlternateMobileNumber() != null && 
              !application.getAppAlternateMobileNumber().equalsIgnoreCase(mobile))
              application.setAppAltOtpMobAlertsCount(Integer.valueOf(0)); 
            if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
              alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0 : 
                  application.getAppAltOtpMobAlertsCount().intValue());
              if (alertCount.intValue() >= 5) {
                json.put("status", "error");
                json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
                json.put("alertCount", alertCount);
                return json;
              } 
              alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0 : 
                  application.getAppAltOtpMobAlertsCount().intValue());
              if (alertCount.intValue() >= 5) {
                json.put("status", "error");
                json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
                json.put("alertCount", alertCount);
                return json;
              } 
            } 
            alertCount = Integer.valueOf(alertCount.intValue() + 1);
            json.put("alertCount", alertCount);
            application.setAppAltOtpMobAlertsCount(alertCount);
            application.setAppOTPVerifyType(appOTPVerifyType);
            if (appOTPVerifyType == 0) {
              application.setAppAltMobileVerificationCode(
                  this.SbiUtil.getVerificationCode(application.getAppAlternateMobileNumber()));
           //   logger.info("method OTP() >> is Alternate mobile number  application.getAppAltMobileVerificationCode()::" + application.getAppAltMobileVerificationCode());
              //logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 1111::" + application.getAppAlternateMobileNumber());
            } else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
              application.setAppEmailVerificationCode(
                  this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
              logger.info("inside auto process impl lno  is Alternate mobile number771");
            } 
            application = this.personalLoanService.save(application);
            //logger.info("after save >> is Alternate mobile number  application.getAppAlternateMobileNumber() 1111::" + application.getAppAlternateMobileNumber());
            if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4 || 
              stateId.intValue() == 15) {
              if (appOTPVerifyType == 0) {
                String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), 
                    Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
                msgBody = this.SbiUtil.urlEncode(msgBody);
                String SMS_TEXT = null;
                if (application.getAppApplyingFrom() == 2) {
                	SMS_TEXT=Constants.SMS_STRING_NRI;
                } else {
                	SMS_TEXT=Constants.SMS_STRING_INDIAN;
                }
                
                SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
                SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", application.getAppAltMobileVerificationCode().toString());
                SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(String.valueOf(application.getAppAltISDCode())) + application.getAppAlternateMobileNumber()).trim());
                
		        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
		        	logger.info("OTP for Mobile Number: " + (String.valueOf(String.valueOf(application.getAppAltISDCode())) + application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
		        }

                if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                  json.put("status", "error");
                  json.put("message", "OTP service is down");
                  json.put("alertCount", alertCount);
                  return json;
                } 
              } 
              application.setAppAltMobileVerificationCodeReceived("Y");
              //logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 2222::" + application.getAppAlternateMobileNumber());
              application = this.personalLoanService.save(application);
              //logger.info("after save >> is Alternate mobile number  application.getAppAlternateMobileNumber() 2222::" + application.getAppAlternateMobileNumber());
            } 
            json.put("status", "success");
            json.put("message", "OTP Code sent");
            json.put("alertCount", alertCount);
            return json;
          } 
          logger.info("inside the else condition LNO 741 ");
          if (resend == null && application.getAppAlternateMobileNumber() != null && 
            !application.getAppAlternateMobileNumber().equalsIgnoreCase(mobile))
            application.setAppAltOtpMobAlertsCount(Integer.valueOf(0)); 
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
            alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0 : 
                application.getAppAltOtpMobAlertsCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0 : 
                application.getAppAltOtpMobAlertsCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
          } 
          alertCount = Integer.valueOf(alertCount.intValue() + 1);
          json.put("alertCount", alertCount);
          application.setAppAltOtpMobAlertsCount(alertCount);
          application.setAppOTPVerifyType(appOTPVerifyType);
          if (appOTPVerifyType == 0) {
            application.setAppAltMobileVerificationCode(
                this.SbiUtil.getVerificationCode(application.getAppAlternateMobileNumber()));
           // logger.info("method OTP() >> is Alternate mobile number  application.getAppAltMobileVerificationCode()::" + application.getAppAltMobileVerificationCode());
          /// logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 3333::" + application.getAppAlternateMobileNumber());
          } else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
            application.setAppEmailVerificationCode(
                this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
            logger.info("inside auto process impl lno  is Alternate mobile number771");
          } 
          application.setAppAlternateMobileNumber(mobile);
          application = this.personalLoanService.save(application);
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4 || 
            stateId.intValue() == 15) {
            if (appOTPVerifyType == 0) {
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), 
                  Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              String SMS_TEXT = null;
              if (application.getAppApplyingFrom() == 2) {
            	  SMS_TEXT=Constants.SMS_STRING_NRI;
              } else {
            	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
              }
              
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", 
                  application.getAppAltMobileVerificationCode().toString());
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(String.valueOf(application.getAppAltISDCode())) + 
                  application.getAppAlternateMobileNumber()).trim());
              
              if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
            	  logger.info("OTP for Mobile Number: " + (String.valueOf(String.valueOf(application.getAppAltISDCode())) + 
                          application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
              }
		        
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                json.put("status", "error");
                json.put("message", "OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            } 
            application.setAppAltMobileVerificationCodeReceived("Y");
            application.setAppAlternateMobileNumber(mobile);
           // logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 4444::" + application.getAppAlternateMobileNumber());
            application = this.personalLoanService.save(application);
          } 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
          return json;
        } 
        if (!isAlternate && application.getAppMobileVerificationCode() == null || "N".equals(application.getAppMobileVerified())) {
          if (resend == null && 
            application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(mobile))
            application.setAppOtpMobileAlertsCount(Integer.valueOf(0)); 
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
            alertCount = Integer.valueOf((application.getAppOtpMobileAlertsCount() == null) ? 0 : application.getAppOtpMobileAlertsCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            boolean isAppFoundForDedupInDropOffStage = false;
            boolean isAppFoundForDedupInDropRejectStage = false;
            if (!Constants.DUMMY_MOBILE_NO.contains(application.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
              boolean isAppFoundForDedupInApplicationStage = false;
              ApplicationFormPersonalLoanQuote quote = this.personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(application.getAppQuoteId());
              if (quote == null) {
                json.put("status", "error");
                json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
                json.put("alertCount", 0);
                return json;
              } 
              isAppFoundForDedupInApplicationStage = this.personalLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, (application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
              logger.info("PLProcessImpl.java :: LNo 553:: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
              if (isAppFoundForDedupInApplicationStage) {
                json.put("status", "error");
                json.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
                json.put("alertCount", alertCount);
                return json;
              } 
              isAppFoundForDedupInDropOffStage = this.personalLoanService.isAppFoundForDedupInDropOffStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
              isAppFoundForDedupInDropRejectStage = this.personalLoanService.isAppFoundForDedupInDropRejectStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
            } 
            if (isAppFoundForDedupInDropRejectStage)
              application.setAppMobileDedup(Integer.valueOf(0)); 
            if (isAppFoundForDedupInDropOffStage)
              application.setAppMobileDedup(Integer.valueOf(1)); 
            alertCount = Integer.valueOf((application.getAppOtpMobileAlertsCount() == null) ? 0 : application.getAppOtpMobileAlertsCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
          } 
          alertCount = Integer.valueOf(alertCount.intValue() + 1);
          application.setAppOtpMobileAlertsCount(alertCount);
          application.setAppOTPVerifyType(Integer.valueOf(appOTPVerifyType).intValue());
          if (appOTPVerifyType == 0) {
            application.setAppMobileVerificationCode(SbiUtil.getVerificationCode(application.getAppMobileNo()));
          } else if (appOTPVerifyType == 1) {
            application.setAppEmailVerificationCode(SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
          } 
          application = this.personalLoanService.save(application);
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4 || stateId.intValue() == 15) {
            if (appOTPVerifyType == 0) {
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              String SMS_TEXT = null;
              if (Constants.COUNTRY_CODE_INDIA.equals(application.getAppISDCode().toString())) {
            	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
              } else {
            	  SMS_TEXT=Constants.SMS_STRING_NRI;
              }
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", application.getAppMobileVerificationCode().toString());
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(String.valueOf(application.getAppISDCode())) + application.getAppMobileNo()).trim());
              
              if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
            	  logger.info("OTP for Mobile Number: " + (String.valueOf(String.valueOf(application.getAppISDCode())) + application.getAppMobileNo()).trim() + " is " + application.getAppMobileVerificationCode().toString());
              }

              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                json.put("status", "error");
                json.put("message", "OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            } else if (appOTPVerifyType == 1 && 
              appApplyingFrom == 2) {
              String msgBody = String.valueOf(String.valueOf(Constants.FIRST_EMAIL_PART)) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
              msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(String.valueOf(Constants.BANK_IMAGE_FOLDER)) + "/");
              msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
              msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
              msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
              msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", application.getAppEmailVerificationCode());
              msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
              boolean sendStatus = false;
              String[] emailId = { application.getAppWorkEmail() };
              //logger.info("PersonalProcessManagerImpl.java LNo: 693 :sendEmail--> emailId::: " + emailId);
              sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
              logger.info("PersonalProcessManagerImpl.java LNo: 696 :sendEmail--> sendStatus::: " + sendStatus);
              if (!sendStatus) {
                json.put("status", "error");
                json.put("message", "EMAIL OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            } 
            application.setAppMobileVerificationCodeReceived("Y");
            logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL3");
            application = this.personalLoanService.save(application);
          } 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
          return json;
        } 
        json.put("status", "error");
        json.put("message", "Your mobile no. is already verified");
        json.put("alertCount", alertCount);
      } else if (stateId.intValue() == 5 || stateId.intValue() == 16) {
    	  
    	  //logger.info("inside else if condition with inputOtp :: "+inputOtp );
    		if(inputOtp !=null) {
	        	SbiUtil sbiutil=new SbiUtil();
	        	//logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
				//for otp decrypt  in sep 2023
	        	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
			//	logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
	        }

    		logger.info("inside else if condition with stateid :: " + stateId.intValue() + "  isAlternate flag::" + isAlternate);
        if (isAlternate) {
          if (!ValidatorUtil.isValid(inputOtp)) {
            json.put("status", "error");
            json.put("message", "Please enter valid OTP.");
            json.put("alertCount", alertCount);
            return json;
          } 
          if (!ValidatorUtil.isValid(application.getAppOTPAttemptCount()))
            application.setAppOTPAttemptCount(Integer.valueOf(0)); 
          if (application.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT
            .intValue()) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
            json.put("alertCount", 1);
            return json;
          } 
          application
            .setAppOTPAttemptCount(Integer.valueOf(application.getAppOTPAttemptCount().intValue() + 1));
          logger.info("application.getAppOTPAttemptCount() before save method::: " + 
              application.getAppOTPAttemptCount());
          //logger.info("application before save :: " + application);
          application = this.personalLoanService.save(application);
         // logger.info("application after save :: " + application);
          if (application == null) {
            logger.info("AutoProcessManagerImpl.java  LNO 764 :: error on saving::");
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
            json.put("alertCount", 1);
            return json;
          } 
          boolean bool = false;
          logger.info("method OTP() >> inside if condition 832 :: " + appOTPVerifyType);
          //logger.info("application.getAppAltMobileVerificationCode()::: " + 
         //     application.getAppAltMobileVerificationCode());
          //logger.info("inputOtp::: " + inputOtp);
          if (application.getAppAltMobileVerificationCode() != null && 
            String.valueOf(application.getAppAltMobileVerificationCode()).equalsIgnoreCase(inputOtp)) {
            bool = true;
            application.setAppAltMobileVerified("Y");
            application.setAppMobileVerified("Y");
           // logger.info("method OTP() >> setAppMobileVerified 3::" + application.getAppAltMobileVerified());
          } 
          logger.info("isOPTVerified::: " + bool);
          if (bool) {
            logger.info("before OTP verification completion");
            StatusRequest statusRequest = new StatusRequest();
            statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
            statusRequest.setHaveLoanOffer(true);
            statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
            statusRequest.setState(stateId.intValue());
            statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
            statusRequest.setRsmDecision(0);
            statusRequest.setApplicationType(
                (SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 
                0);
            statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
            StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
            if (statusManagerResponse.getStatus() != 0) {
              application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
            } else if (application.getAppLoanStatusId().intValue() == 0) {
              json.put("status", "error");
              json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
              return json;
            } 
            logger.info("ElProcessImpl.java Line 725 autoLoanService file OTP method" + 
                application.getAppReferenceId());
            application = this.personalLoanService.save(application);
            if (statusManagerResponse.isEligibleToInsertLog())
              this.personalLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), 
                  statusManagerResponse.getStatusCallLogs(), null, null, true); 
            json.put("status", "success");
            if (stateId.intValue() == 5) {
              json.put("message", 
                  "Thank you for your interest. Our representative will contact you shortly.");
            } else {
              json.put("message", "OTP authentication successful");
            } 
            json.put("alertCount", alertCount);
            return json;
          } 
          json.put("status", "error");
          logger.info("cheking LNumber 819 Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue()" + 
              Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue());
          logger.info("cheking LNumber 820 application.getAppDataSourceId().intValue()" + 
              application.getAppDataSourceId().intValue());
          if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == application.getAppDataSourceId()
            .intValue()) {
            json.put("message", "OTP is incorrect! Try again.");
          } else {
            json.put("message", "OTP is incorrect! Try again.|inputOtp|2");
          } 
          json.put("alertCount", alertCount);
          return json;
        } 
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Invalid Request OTP! send properly.");
          json.put("alertCount", alertCount);
          return json;
        } 
        if (!ValidatorUtil.isValid(application.getAppOTPAttemptCount()))
          application.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (application.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          json.put("alertCount", 1);
          return json;
        } 
        application.setAppOTPAttemptCount(Integer.valueOf(application.getAppOTPAttemptCount().intValue() + 1));
        application = this.personalLoanService.save(application);
        if (application == null) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
          json.put("alertCount", 1);
          return json;
        } 
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (application.getAppMobileVerificationCode() != null && application.getAppMobileVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
            isOPTVerified = true;
            application.setAppMobileVerified("Y");
          } 
        } else if (appOTPVerifyType == 1 && 
          application.getAppEmailVerificationCode() != null && application.getAppEmailVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
          isOPTVerified = true;
          application.setAppEmailVerified("Y");
        } 
        application.setAppOTPVerifyType(appOTPVerifyType);
        if (isOPTVerified) {
          ApplicationFormPersonalLoanQuote quote = this.personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(application.getAppQuoteId());
          if (quote != null && quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
            application.setAppResTypeAtVerified(Integer.valueOf(2));
          } else {
            application.setAppResTypeAtVerified(Integer.valueOf(1));
          } 
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } else {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } 
          statusRequest.setState(stateId.intValue());
          statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
          statusRequest.setRsmDecision(0);
          statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
          statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
          StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
          if (statusManagerResponse.getStatus() != 0) {
            application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
          } else if (application.getAppLoanStatusId().intValue() == 0) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          application = this.personalLoanService.save(application);
          if (statusManagerResponse.isEligibleToInsertLog())
            this.personalLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
            json.put("message", "OTP authentication successful");
          } 
          json.put("alertCount", alertCount);
          return json;
        } 
        //logger.info("PersonalProcessManagerImpl.java  LNO 812 :: OTP is incorrect for mobileNo ::" + application.getAppMobileNo());
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == application.getAppDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtp|2");
        } 
        json.put("alertCount", alertCount);
        return json;
      } 
    } catch (SQLException ne) {
		logger.info("PersonalProcessManagerImpl.java LNo : 1067 : Exception Caught", ne);
		try {
	        json.put("status", "error");
	        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
	        json.put("alertCount", 0);
	      } catch (JSONException e1) {
	        logger.info("PersonalProcessManagerImpl.java LNo: 1073 :: processAddCoapplicant() :: " + e1.getMessage());
	      } 
	} catch (JSONException e) {
      logger.info("PersonalProcessManagerImpl.java LNo: 1076 :: processAddCoapplicant() :: " + e.getMessage());
      try {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
      } catch (JSONException e1) {
        logger.info("PersonalProcessManagerImpl.java LNo: 1082 :: processAddCoapplicant() :: " + e1.getMessage());
      } 
    } 
    return json;
  }
  
  private JSONObject OTP(Integer appSeqId, Integer stateId, String name, int appApplyingFrom, String isdCode, String mobile, String email, String inputOtp, Integer trackVisitId, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId,Integer commonLoanType) {
    JSONObject json = new JSONObject();
    try {
      ApplicationFormLead lead = null;
      if (SessionUtil.getLeadId() != null) {
        lead = this.commonService.getLeadById(SessionUtil.getLeadId());
      } else {
        lead = new ApplicationFormLead();
      } 
      Integer alertCount = Integer.valueOf(0);
      if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
        if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (
          !ValidatorUtil.isValid(name) || !ValidatorUtil.isValidMobile(mobile) || !ValidatorUtil.isValidEmail(email))) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
        } 
        if ((stateId.intValue() == 14 || stateId.intValue() == 15) && 
          !ValidatorUtil.isValid(mobile)) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
          return json;
        } 
        SessionUtil.setApplicantName(name);
        lead.setLeadFirstName(name);
        SessionUtil.setEmail(email);
        lead.setLeadWorkEmail(email);
        if (appApplyingFrom == 2) {
          lead.setLeadApplyingFrom(appApplyingFrom);
          lead.setLeadIsdCode(isdCode);
          SessionUtil.setISDCode(isdCode);
          lead.setLeadMobileNo(mobile);
          SessionUtil.setMobile(mobile);
        } else {
          lead.setLeadApplyingFrom(1);
          isdCode = Constants.COUNTRY_CODE_INDIA;
          lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          lead.setLeadMobileNo(mobile);
          SessionUtil.setMobile(mobile);
        } 
        if (!Constants.DUMMY_MOBILE_NO.contains(mobile) && !Constants.INQUIRY_DUPLICATION_CHECK.equals("0")) {
          boolean isLeadExists = false;
          isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.PERSONAL_LOAN_ID, lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo(), loanTypeId);
          if (isLeadExists) {
            json.put("status", "error");
            json.put("message", Constants.INQUIRY_DEDUPLICATION_MESSAGE);
            json.put("alertCount", alertCount);
            return json;
          } 
        } 
        if (lead.getLeadMobileVerificationCode() == null || "N".equals(lead.getLeadMobileVerificationCodeVerified())) {
          if (!lead.getLeadMobileNo().equalsIgnoreCase(mobile))
            lead.setLeadMobileAlertCount(Integer.valueOf(0)); 
          if (stateId.intValue() == 3 || stateId.intValue() == 14) {
            alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(mobile));
            lead.setLeadMobileVerificationCodeVerified("N");
            lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID));
            if (lead.getLeadSourceId() == null)
              lead.setLeadSourceId(Integer.valueOf(1)); 
            if (lead.getLeadDataSourceId() == null)
              lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK); 
            lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
            lead.setLeadLoanPurposeId(Integer.valueOf(4));
            lead.setLeadReceiveDatetime(new Date());
            lead.setLeadEntryTime(new Date());
            lead.setLeadEntryDate(new Date());
            lead.setLeadLastUpdated(new Date());
            lead.setLeadAppContactCenterLocation(1);
            lead.setLeadActive("Y");
            lead.setLeadDeleted("N");
            if (bankLMSUserId != null && bankLMSUserId.intValue() != 9999999) {
              BankLmsUser bankUser = this.commonService.getBankLmsUserById(bankLMSUserId);
              lead.setLeadIntermediaryId(bankUser.getLmsUserIntermediaryId());
            } 
            lead.setLeadVisitId(trackVisitId);
          } else if (stateId.intValue() == 4 || stateId.intValue() == 15) {
            alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(mobile));
          } 
          alertCount = Integer.valueOf(alertCount.intValue() + 1);
          lead.setLeadMobileAlertCount(alertCount);
          lead.setLeadLastUpdated(new Date());
          logger.info("commonLoanType 1143 "+commonLoanType);
          if(commonLoanType==3) {
			Integer consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.PERSONAL_LOAN_ID, "NTB").getConsentId();
			logger.info("consentTextNtb 1145 "+consentTextNtb);
		    lead.setLeadConsentId(consentTextNtb);
          }
          if(commonLoanType==27) {
  			Integer consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.APP_PL_TYPE_GOLD, "NTB").getConsentId();
  			logger.info("consentTextNtb 1151 "+consentTextNtb);
  		    lead.setLeadConsentId(consentTextNtb);
            }
          try {
            lead = this.commonService.save(lead);
            if (stateId.intValue() == 3)
              this.commonService.insertCallLog(lead.getLeadId(), bankLMSUserId, Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID), null); 
            json.put("status", "success");
            json.put("message", "OTP Code sent");
            json.put("alertCount", alertCount);
          } catch (SQLException ne){
      		logger.info("PersonalProcessManagerImpl.java LNo : 1191 : Exception Caught", ne);
      		json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
      	} 
          SessionUtil.setLeadId(lead.getLeadId());
          String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
          msgBody = this.SbiUtil.urlEncode(msgBody);
          String SMS_TEXT = null;
          if (lead.getLeadApplyingFrom() == 2) {
        	  SMS_TEXT=Constants.SMS_STRING_NRI;
          } else {
        	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
          }
          SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
          SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", lead.getLeadMobileVerificationCode().toString());
          SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
          
          if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
        	  logger.info("OTP for Mobile Number: " + (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim() + " is " + lead.getLeadMobileVerificationCode().toString());
          }

          if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
            json.put("status", "error");
            json.put("message", "OTP service is down");
            json.put("alertCount", alertCount);
            return json;
          } 
          return json;
        } 
        json.put("status", "error");
        json.put("message", "Your mobile no. is already verified");
        json.put("alertCount", alertCount);
      } else if (stateId.intValue() == 5 || stateId.intValue() == 16) {
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Invalid Request OTP! send properly.");
          json.put("alertCount", alertCount);
          return json;
        } 
        if (!ValidatorUtil.isValid(lead.getLeadOTPAttempCount()))
          lead.setLeadOTPAttempCount(Integer.valueOf(0)); 
        if (lead.getLeadOTPAttempCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          json.put("alertCount", 1);
          return json;
        } 
        lead.setLeadOTPAttempCount(Integer.valueOf(lead.getLeadOTPAttempCount().intValue() + 1));
        lead = this.commonService.save(lead);
        if (lead == null) {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 1);
          return json;
        } 
        if (lead.getLeadMobileVerificationCode() != null && lead.getLeadMobileVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
          if (SessionUtil.getLeadId() != null)
            lead = this.commonService.getLeadById(SessionUtil.getLeadId()); 
          lead.setLeadMobileVerificationCodeVerified("Y");
          lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID));
          try {
            lead = this.commonService.save(lead);
            this.commonService.insertCallLog(lead.getLeadId(), Constants.OTHER_ID_INTEGER, Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID), null);
          } catch (SQLException ne){
      		logger.info("PersonalProcessManagerImpl.java LNo : 1256 : Exception Caught", ne);
      		json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
      	} 
          logger.info("PersonalProcessManagerImpl.java LNO ::900 , log for CALLBACK_SMS_CONSENT " + Constants.CALLBACK_SMS_CONSENT);
          if (Constants.CALLBACK_SMS_CONSENT) {
            logger.info("PersonalProcessManagerImpl.java LNO ::973 , log for CALLBACK_SMS_CONSENT " + lead.getLeadMobileVerificationCodeVerified());
            if ("Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified())) {
              logger.info("PersonalProcessManagerImpl.java LNO ::975 , log for CALLBACK_SMS_CONSENT " + Constants.MESSAGE_TYPE_SMS);
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              String SMS_TEXT = null;
              if (lead.getLeadApplyingFrom() == 2) {
            	  SMS_TEXT=Constants.SMS_STRING_NRI;
              } else {
            	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
              } 
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
              try {
				logger.info("PersonalProcessManagerImpl.java LNO ::1333 :: message has been sent to user " + EncryptDecryptUtil.encrypt(SMS_TEXT));
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				
				
				logger.info("PersonalProcessManagerImpl.java LNO ::1264 , Exception caught ",e);
			}
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                logger.info("PersonalProcessManagerImpl.java LNO ::988 , OTP service is down:: msg not send");
                json.put("status", "error");
                json.put("message", "sms service is down");
                return json;
              } 
            } 
          } 
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            logger.info("PersonalProcessManagerImpl.java LNO ::998 :: message has been sent to user ");
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
            json.put("message", "OTP authentication successful");
          } 
          json.put("alertCount", alertCount);
          logger.info("PersonalProcessManagerImpl.java  LNO 1102 Before Calling Call Center Service ----" + lead);
          if (!Constants.CLICK_TO_CALL_BYPASS && 
            lead != null && lead.getLeadLoanPurposeId() != null && lead.getLeadLoanPurposeId().intValue() != 10) {
            logger.info("PersonalProcessManagerImpl.java  LNO 1105----Service Response message----");
            this.callBackService.getcallBackService(lead);
            lead = this.commonService.save(lead);
          } 
          logger.info("PersonalProcessManagerImpl.java  LNO 1110 after Calling Call Center Service ----" + lead);
          SessionUtil.setLeadId(null);
          json.put("alertCount", alertCount);
          return json;
        } 
        logger.info("PersonalProcessManagerImpl.java  LNO 1075 :: OTP is incorrect for mobileNo ::" + lead.getLeadMobileNo());
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == lead.getLeadDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtpWantUs|2");
        } 
        json.put("alertCount", alertCount);
        return json;
      } 
    } catch (SQLException e) {
        logger.info("PersonalProcessManagerImpl.java LNo: 1342 :: OTP() :: " + e.getMessage());
        try {
          json.put("status", "error");
          json.put("message", "OTP process is down");
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("PersonalProcessManagerImpl.java LNo: 1348 :: OTP() :: " + e.getMessage());
        } 
      } catch (JSONException e) {
      logger.info("PersonalProcessManagerImpl.java LNo: 1351 :: OTP() :: " + e.getMessage());
      try {
        json.put("status", "error");
        json.put("message", "OTP process is down");
        json.put("alertCount", 0);
      } catch (JSONException e1) {
        logger.info("PersonalProcessManagerImpl.java LNo: 1357 :: OTP() :: " + e.getMessage());
      } 
    }
    return json;
  }
  
  public JSONObject processMobileOTP(Integer appSeqId, Integer stateId, Integer trackVisitId, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId, OtherRequest otherRequest) throws JSONException {
    JSONObject json = new JSONObject();
    String mobile = null;
    String isdCode = null;
    int appApplyingFrom = 0;
    int appOTPVerifyType = 0;
    if (otherRequest != null && otherRequest.getAppApplyingFrom() != null)
      appApplyingFrom = Integer.parseInt(otherRequest.getAppApplyingFrom()); 
    if (otherRequest != null && otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (appApplyingFrom == 2) {
      if (otherRequest != null && otherRequest.getIsdCode() != null) {
        isdCode = otherRequest.getIsdCode();
      } else if (SessionUtil.getISDCode() != null) {
        isdCode = SessionUtil.getISDCode();
      } 
      if (otherRequest != null && otherRequest.getNriMobileNo() != null) {
        mobile = otherRequest.getNriMobileNo();
      } else if (SessionUtil.getMobile() != null) {
        mobile = SessionUtil.getMobile();
      } 
    } else {
      isdCode = Constants.COUNTRY_CODE_INDIA;
      if (otherRequest != null && otherRequest.getMobileNo() != null) {
        mobile = otherRequest.getMobileNo();
      } else if (SessionUtil.getMobile() != null) {
        mobile = SessionUtil.getMobile();
      } 
    } 
    String email = null;
    if (otherRequest != null && otherRequest.getEmail() != null) {
      email = otherRequest.getEmail();
    } else if (SessionUtil.getEmail() != null) {
      email = SessionUtil.getEmail();
    } 
    String inputOtp = null;
    if (appOTPVerifyType == 0) {
      if (otherRequest != null && otherRequest.getInputOtp() != null)
        inputOtp = otherRequest.getInputOtp(); 
    } else if (appOTPVerifyType == 1 && 
      otherRequest != null && otherRequest.getInputOtpEmail() != null) {
      inputOtp = otherRequest.getInputOtpEmail();
    } 
    
    if (trackVisitId == null && otherRequest.getAlternateMobileNumber() == null)
      json = OTP(appSeqId, stateId, null, appApplyingFrom, appOTPVerifyType, isdCode, mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId); 
   // logger.info("before OTP call>>otherRequest.getAlternateMobileNumber()::" + otherRequest.getAlternateMobileNumber());
 //   logger.info("before OTP call>>mobile::" + mobile);
    if (otherRequest.getAlternateMobileNumber() != null)
      if (!otherRequest.getAlternateMobileNumber().equals(mobile)) {
    	  
    	  inputOtp = otherRequest.getInputOtpAlt() != null ? inputOtp = otherRequest.getInputOtpAlt() : "";
        json = OTP(appSeqId, stateId, null, appApplyingFrom, appOTPVerifyType, otherRequest.getIsdCodeAlt(), "ALT_" + otherRequest.getAlternateMobileNumber(), email, inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId);
      } else {
        json.put("status", "error");
        json.put("message", "Alternate Mobile cannot be same as Mobile number.");
        json.put("alertCount", 0);
      }  
    return json;
  }
  
  public JSONObject processWantUsToCallYou(Integer appSeqId, Integer stateId, Integer trackVisitId, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId, OtherRequest otherRequest) throws SQLException, JSONException {
    JSONObject json = new JSONObject();
    String name = null;
    String mobile = null;
    String isdCode = null;
    int appApplyingFrom = 0;
    int appOTPVerifyType = 0;
    if (otherRequest.getName() != null)
      name = otherRequest.getName().toString(); 
    if (otherRequest.getAppApplyingFrom() != null)
      appApplyingFrom = Integer.parseInt(otherRequest.getAppApplyingFrom().toString()); 
    if (otherRequest.getIsdCodeWantUsToCallYou() != null)
      isdCode = otherRequest.getIsdCodeWantUsToCallYou().toString(); 
    if (appApplyingFrom == 2) {
      if (otherRequest.getNriMobileWantUsToCallYou() != null)
        mobile = otherRequest.getNriMobileWantUsToCallYou().toString(); 
    } else if (otherRequest.getMobileWantUsToCallYou() != null) {
      mobile = otherRequest.getMobileWantUsToCallYou().toString();
    } 
    int count = this.commonService.getCallBackLeadCount(Integer.valueOf(4), isdCode, mobile);
    if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
      json.put("status", "error");
      json.put("message", Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
      json.put("alertCount", 1);
      return json;
    } 
    String email = null;
    if (otherRequest.getEmail() != null)
      email = otherRequest.getEmail().toString(); 
    String inputOtp = null;
    if (otherRequest.getInputOtpWantUs() != null)
      inputOtp = otherRequest.getInputOtpWantUs(); 
    if(inputOtp !=null) {
      	SbiUtil sbiutil=new SbiUtil();
      	//logger.info("DecryptedRequest inputOtp   1444  "+inputOtp);
      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
			//logger.info("DecryptedRequest inputOtp   1448  "+inputOtp);
      }
    logger.info("(otherRequest.getCommmonloanType()   1456  "+otherRequest.getCommmonloanType());
    if (trackVisitId != null) {
      json = OTP(appSeqId, stateId, name, appApplyingFrom, isdCode, mobile, email, inputOtp, trackVisitId, bankLMSUserId, ajaxPostUrl, loanTypeId,otherRequest.getCommmonloanType());
    } else {
      json = OTP(appSeqId, stateId, name, appApplyingFrom, appOTPVerifyType, isdCode, mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId);
    } 
    return json;
  }
  
  public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormPersonalLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      Integer bankLMSUserId = (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID;
      boolean isContactCenterUser = this.commonService.getBankLmsUserRole(bankLMSUserId);
      if (!isContactCenterUser) {
        boolean isEligibleToCallCIBIL = false;
        if (quote.getLoanQuoteLoanPurposeId() != null)
          if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 && 
            quote.getLoanQuoteIndustryType() != null && (quote.getLoanQuoteIndustryType().intValue() == 84 || 
            quote.getLoanQuoteIndustryType().intValue() == 1 || quote.getLoanQuoteIndustryType().intValue() == 15 || 
            quote.getLoanQuoteIndustryType().intValue() == 16 || quote.getLoanQuoteIndustryType().intValue() == 17 || 
            quote.getLoanQuoteIndustryType().intValue() == 19 || quote.getLoanQuoteIndustryType().intValue() == 25)) {
            if (quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2)
              isEligibleToCallCIBIL = true; 
          }
        if (isEligibleToCallCIBIL && quote.getLoanQuoteSourceId() != null && !quote.getLoanQuoteSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP) && 
          quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1 && (
          quote.getLoanQuotePanCardNo() == null || "".equals(quote.getLoanQuotePanCardNo())) && quote.getLoanQuoteOtherId() != null && quote.getLoanQuoteOtherId().intValue() == 0) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please enter either PAN No or select other identity proof1.|1");
          return loanScenarioBean;
        } 
      } 
      ApplicationFormPersonalLoan application = null;
      if (appSeqId != null) {
        application = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
        if (application == null) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
          return loanScenarioBean;
        } 
        if (quote != null && quote.getLoanQuotePanCardLater() == null) {
          quote.setLoanQuotePanCardLater(Boolean.valueOf(false));
        } else if (quote != null && quote.getLoanQuotePanCardLater().booleanValue()) {
          quote.setLoanQuotePanCardLater(quote.getLoanQuotePanCardLater());
        } 
        if (application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
          if (quote.getLoanQuoteFirstName() != null)
            application.setAppFirstName(quote.getLoanQuoteFirstName()); 
          if (quote.getLoanQuoteMiddleName() != null)
            application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
          if (quote.getLoanQuoteLastName() != null)
            application.setAppLastName(quote.getLoanQuoteLastName()); 
          if (quote.getLoanQuoteGender() != null)
              application.setAppGender(quote.getLoanQuoteGender());
          if (quote.getLoanQuoteDateOfBirth() != null) {
            application.setAppDob(quote.getLoanQuoteDateOfBirth());
            application.setAppDobDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
          } 
          if (quote.getAppEmail() != null)
            application.setAppWorkEmail(quote.getAppEmail()); 
          if (quote.getAppMobile() != null)
            application.setAppMobileNo(quote.getAppMobile()); 
        } 
      } 
      if (application != null && (
        ajaxPostUrl.equalsIgnoreCase("personal-loan") || ajaxPostUrl.equalsIgnoreCase("pension-loan")))
        if (application.getAppApplyingFrom() == 2) {
          if (application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo()) && application.getAppMobileVerified().equalsIgnoreCase("Y")) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            loanScenarioBean.setMessage("You have already verified OTP with NRI mobile number : " + application.getAppMobileNo());
            return loanScenarioBean;
          } 
        } else if ((quote.getLoanQuoteLoanPurposeId().intValue() != 11 || quote.getLoanQuoteIndustryType() == null || (
          quote.getLoanQuoteIndustryType().intValue() != 1 && quote.getLoanQuoteIndustryType().intValue() != 15 && quote.getLoanQuoteIndustryType().intValue() != 16 && 
          quote.getLoanQuoteIndustryType().intValue() != 17 && quote.getLoanQuoteIndustryType().intValue() != 19 && quote.getLoanQuoteIndustryType().intValue() != 25 && quote.getLoanQuoteIndustryType().intValue() != 87)) && 
          application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppMobile()) && application.getAppMobileVerified().equalsIgnoreCase("Y")) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("You have already verified OTP with mobile number : " + application.getAppMobileNo());
          return loanScenarioBean;
        }  
      int oldVisitId = 0;
      if (application != null && application.getAppQuoteId() != null) {
        oldVisitId = personalLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
      } else {
        oldVisitId = trackVisitId.intValue();
      } 
      quote.setLoanQuoteIpAddress(SbiUtil.getIPAddress());
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      if (application != null) {
        if (application.getAppOTPVerifyType() == 0) {
          if (!"Y".equalsIgnoreCase(application.getAppMobileVerified()))
            if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
              quote.setAppApplyingFrom(1);
              quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
            } else if (quote.getAppApplyingFrom() == 2) {
              MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
              quote.setAppISDCode(country.getCountryCode().toString());
            }  
        } else if (!"Y".equalsIgnoreCase(application.getAppEmailVerified())) {
          if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
            quote.setAppApplyingFrom(1);
            quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          } else if (quote.getAppApplyingFrom() == 2) {
            MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
            quote.setAppISDCode(country.getCountryCode().toString());
          } 
        } 
      } else if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
        quote.setAppApplyingFrom(1);
        quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
      } else if (quote.getAppApplyingFrom() == 2) {
        MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
        quote.setAppISDCode(country.getCountryCode().toString());
      } 
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && (
        appSeqId == null || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS.intValue() && "Y".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())))) {
        String oldMobile = "";
        if (quote.getAppApplyingFrom() == 2) {
          oldMobile = quote.getAppNRIMobileNo();
        } else if (quote.getAppApplyingFrom() == 2) {
          oldMobile = quote.getAppMobile();
        } 
        String isdCode = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
        if (quote.getAppMobile() != null && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0"))
          if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
            boolean isLeadExists = false;
            int leadLoanPurposeId = 0;
            if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
              leadLoanPurposeId = 4;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
              leadLoanPurposeId = 7;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
              leadLoanPurposeId = 6;
            } 
            isLeadExists = commonService.getLeadByProductTypeAndMobileNo(Constants.PERSONAL_LOAN_ID, Integer.valueOf(leadLoanPurposeId), isdCode, oldMobile, loanTypeId);
            if (isLeadExists) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
          } else {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = personalLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("PLProcessImpl.java :: LNo 1497 :: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.personalLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            isAppFoundForDedupInDropRejectStage = this.personalLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
          }  
      } 
      Integer quoteApplyingFrom = Integer.valueOf(quote.getAppApplyingFrom());
      String quoteMobileNRI = quote.getAppNRIMobileNo();
      String quoteMobile = quote.getAppMobile();
      String quoteISD = quote.getAppISDCode();
      Integer preferredLoanBranch = null;
      Integer pensionPayingStateId = null;
      Integer pensionPayingCityId = null;
      Integer pensionPayingDistrId = null;
      Integer pensionPayingBranchId = null;
      Integer preferredStateId = null;
      Integer preferredCityId = null;
      Integer preferredDistrictId = null;
      Integer preferredLoanBranchId = null;
      String panCard = quote.getLoanQuotePanCardNo();
      Integer otherId = quote.getLoanQuoteOtherId();
      String otherIdNu = quote.getLoanQuoteOtherIdNumber();
      if (quote.getLoanQuoteLoanPurposeId().intValue() == Constants.PENSION_LOAN_PURPOSE_ID.intValue())
        if (quote != null && quote.getLoanQuotePensionPayingStateId() != null) {
          preferredLoanBranch = quote.getLoanQuotePreferredLoanBranch();
          pensionPayingStateId = quote.getLoanQuotePensionPayingStateId();
          pensionPayingCityId = quote.getLoanQuotePensionPayingCityId();
          pensionPayingDistrId = quote.getLoanQuotePensionPayingDistrId();
          pensionPayingBranchId = quote.getLoanQuotePensionPayingBranchId();
          preferredStateId = quote.getLoanQuotePreferredStateId();
          preferredCityId = quote.getLoanQuotePreferredCityId();
          preferredDistrictId = quote.getLoanQuotePreferredDistrictId();
          preferredLoanBranchId = quote.getLoanQuotePreferredLoanBranchId();
        } else if (application != null && application.getAppPensionPayingStateId() != null) {
          preferredLoanBranch = application.getAppPreferredLoanBranch();
          pensionPayingStateId = application.getAppPensionPayingStateId();
          pensionPayingCityId = application.getAppPensionPayingCityId();
          pensionPayingDistrId = application.getAppPensionPayingDistrId();
          pensionPayingBranchId = application.getAppPensionPayingBranchId();
          preferredStateId = application.getAppPreferredStateId();
          preferredCityId = application.getAppPreferredCityId();
          preferredDistrictId = application.getAppPreferredDistrictId();
          preferredLoanBranchId = application.getAppPreferredLoanBranchId();
        }  
      if (quote.getLoanQuoteSourceId() == null)
        if (SessionUtil.getApplicationType().intValue() == 0) {
          quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
        } else if (SessionUtil.getApplicationType().intValue() == 1) {
          if (SessionUtil.getLeadId() != null) {
            ApplicationFormLead lead = commonService.getLeadById(SessionUtil.getLeadId());
            if (lead != null && lead.getLeadDataSourceId() != null) {
              quote.setLoanQuoteSourceId(lead.getLeadDataSourceId());
            } else {
              quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
            } 
          } else {
            quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
          } 
        } else if (SessionUtil.getApplicationType().intValue() == 2) {
          quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
        }  
      String landMark = null;
      Integer stateId = null;
      Integer cityId = null;
      if (quote.getLoanQuoteFirstName() != null) {
        landMark = quote.getLoanQuoteAddressLandmark();
        stateId = quote.getLoanQuoteStateId();
        cityId = quote.getLoanQuoteCityId();
      } 
      if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteIndustryType() != null && quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2)
        quote.setLoanQuoteEmploymentType(Integer.valueOf(15)); 
      if (quote.getLoanQuotePincode() != null) {
        int pincodeInitial = quote.getLoanQuotePincode().intValue() / 10000;
        String pinlastfix = quote.getLoanQuotePincode().toString().substring(3, 6);
        MasterState state = commonService.getStateById((quote.getLoanQuoteStateId() != null) ? quote.getLoanQuoteStateId() : null);
        if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
          if (pincodeInitial < state.getStatePinMinStartPrefix().intValue() || pincodeInitial > state.getStatePinMaxStartPrefix().intValue() || pinlastfix.equals("000")) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == quote.getLoanQuoteSourceId().intValue()) {
              loanScenarioBean.setMessage("Entered pincode does not belong to entered state.");
            } else {
              loanScenarioBean.setMessage("Entered pincode does not belong to entered state.|quote.loanQuotePincode|2");
            } 
            return loanScenarioBean;
          } 
        } else {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == quote.getLoanQuoteSourceId().intValue()) {
            loanScenarioBean.setMessage("Entered pincode does not belong to entered state.");
          } else {
            loanScenarioBean.setMessage("Entered pincode does not belong to entered state.|quote.loanQuotePincode|2");
          } 
          return loanScenarioBean;
        } 
      }
      logger.info("PLProcessImpl.java LN 1678 ");
      if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
    	  if(quote.getLoanQuotePreferredBranchName() != null) {
    		  	quote.setLoanQuotePreferredBranchName(quote.getLoanQuotePreferredBranchName().split("\\[")[0].trim());
    		  	logger.info("PLProcessImpl.java LN 1692 " + quote.getLoanQuotePreferredBranchName());
				String result = commonService.getBranchNameByQueryString(quote.getLoanQuotePreferredBranchName());
				logger.info("PLProcessImpl.java LNo : 1716 : quote.getLoanQuotePreferredBranchName() is :: "
						+ quote.getLoanQuotePreferredBranchName() + " with result is : " + result);
				if(result == null) {
					loanScenarioBean.setStatus(Integer.valueOf(0));
					loanScenarioBean.setMessage(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|quote.loanQuotePreferredBranchName|1");
					return loanScenarioBean;
				}
			}
    	  
    	  if (quote.getLoanQuoteLoanAmountTaken() < Double.valueOf(20000.0) || quote.getLoanQuoteLoanAmountTaken() > Double.valueOf(5000000.0)) {
				logger.info("PLProcessImpl.java LNo : 137 : quote.getLoanQuoteLoanAmountTaken() is :: "
						+ quote.getLoanQuoteLoanAmountTaken());
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Please enter valid requested loan amount between 20 Thousand to 50 Lakhs.|quote.loanQuoteLoanAmount|2");
				return loanScenarioBean;
			}
    	  
    	  if (!ValidatorUtil.isValid(quote.getLoanQuotePreferredBranchName())) {
				logger.info("PLProcessImpl.java LNo : 143 : quote.getLoanQuotePreferredBranchName() is :: "
						+ quote.getLoanQuotePreferredBranchName());
				
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Please enter valid branch name.|quote.loanQuotePreferredBranchName");
				return loanScenarioBean;
			}

			if (!ValidatorUtil.isValid(quote.getLoanQuoteLoanAmountTaken())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage(
						"Please enter valid requested loan amount between 20 Thousand to 50 Lakhs.|quote.loanQuoteLoanAmount");
				return loanScenarioBean;
			}

			if (!ValidatorUtil.isValid(quote.getLoanQuoteGramOfGold())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Please enter valid gram of gold.|quote.loanQuoteGramOfGold");
				return loanScenarioBean;
			}

			if (quote.getLoanQuoteGramOfGold() < Integer.valueOf(4)
					|| quote.getLoanQuoteGramOfGold() > Integer.valueOf(999)) {
				logger.info("PLProcessImpl.java LNo : 149 : quote.getLoanQuoteGramOfGold() is :: "
						+ quote.getLoanQuoteGramOfGold());
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Gram of Gold value should be in between 4 gram to 999 grams.|quote.loanQuoteGramOfGold");
				return loanScenarioBean;
			}
      }
            
  /*  if  (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
    
      //land mark 
      if(quote.getLoanQuoteAddressLandmark() != null) {
	      if (!quote.getLoanQuoteAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
	    	  loanScenarioBean.setStatus(Integer.valueOf(0));
	          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in  Landmark  ");
	          return loanScenarioBean;
	      }
      }

    }*/
      
      if (!quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
    	  
    	  if(!ValidatorUtil.validateFirstNameLength(quote.getLoanQuoteFirstName())) {
	    	  loanScenarioBean.setStatus(Integer.valueOf(0));
	     	  loanScenarioBean.setMessage("Please enter between 2 to 20 characters in First name.");
	     	  return loanScenarioBean;
	      }
    	  
    	  if(!ValidatorUtil.validateMiddleNameLength(quote.getLoanQuoteMiddleName())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Please enter between 2 to 20 characters Middle name.");
    		  return loanScenarioBean;
    	  }
       
    	  if(!ValidatorUtil.validateLastNameLength(quote.getLoanQuoteLastName())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Please enter between 2 to 20 characters in Last name.");
    		  return loanScenarioBean;
    	  }
    
    	  if (!ValidatorUtil.validateFirstName(quote.getLoanQuoteFirstName())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("First name is not in correct format. Please enter only [a-z].");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (!ValidatorUtil.validateMiddleName(quote.getLoanQuoteMiddleName())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Middle name is not in correct format. Please enter only [a-z].");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (!ValidatorUtil.validateLastName(quote.getLoanQuoteLastName())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Last name is not in correct format. Please enter only [a-z] & do not include spaces.");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (quote.getLoanQuoteGender() != null && !ValidatorUtil.isGender(quote.getLoanQuoteGender() )) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Gender is not in correct format.");
    		  return loanScenarioBean;
    	  }
  
    	  if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress1().trim())) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 1");
    		  return loanScenarioBean;
    	  }

    	  //if (!(quote.getLoanQuoteAddress1() != null && quote.getLoanQuoteAddress1().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
    	  if (!(quote.getLoanQuoteAddress1() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress1()) )) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 1");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress2().trim())) {
		  	  loanScenarioBean.setStatus(Integer.valueOf(0));
		  	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 2");
		  	  return loanScenarioBean;
    	  }
    
    	  //if (!(quote.getLoanQuoteAddress2() != null &&     quote.getLoanQuoteAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
    	  if (!(quote.getLoanQuoteAddress2() != null &&  ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress2()))) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));  
    		  loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 2");
    		  return loanScenarioBean;
    	  }
    	  
    	  if(quote.getLoanQuoteAddressLandmark() != null) {
    		  if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddressLandmark().trim())) {
    			  loanScenarioBean.setStatus(Integer.valueOf(0));
    			  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Landmark");
    			  return loanScenarioBean;
    		  }
    	  }
    	  
    	  if(quote.getLoanQuoteAddressLandmark() != null) {
    		  if (!quote.getLoanQuoteAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
    			  loanScenarioBean.setStatus(Integer.valueOf(0));
    			  loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Landmark  ");
    			  return loanScenarioBean;
    		  }
    	  }
    
    	  if (quote.getLoanQuotePanCardNo() != null && !ValidatorUtil.isValidPanNo(quote.getLoanQuotePanCardNo() )) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("PAN is not in correct format.|2");
    		  return loanScenarioBean;
    	  }

    	  //added for same name validation
    	  if(quote.getLoanQuoteFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || 
    			  (quote.getLoanQuoteMiddleName()!=null && 
    				( quote.getLoanQuoteMiddleName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim())|| quote.getLoanQuoteFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteMiddleName().trim())))) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (quote.getLoanQuoteLoanAmountTaken() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteLoanAmountTaken() )) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Requested loan Amount cannot be greater than 9 digits.|2");
    		  return loanScenarioBean;
    	  }
    	  
    	  if (quote.getAppMobile() != null && !ValidatorUtil.isValidMobile(quote.getAppMobile() )) {
    		  loanScenarioBean.setStatus(Integer.valueOf(0));
    		  loanScenarioBean.setMessage("Mobile number is not in correct format.|2");
    		  return loanScenarioBean;
    	  }
      }
      if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
          boolean isBankLmsContactCenterId = this.commonService.getBankLmsUserRole(bankLMSUserId);
          logger.info(" PLProcessImpl.java :: LNo :: 1858 ::  isBankLmsContactCenterId : " + isBankLmsContactCenterId + " bankLMSUserId : " + bankLMSUserId + " with AppSeqId " + appSeqId);
          String identityvalidation = this.SbiUtil.getIdentityValidation(false, quote.getLoanQuotePanCardNo(), quote.getLoanQuoteHaveAadhaarNumber(), quote.getLoanQuoteOtherId(), quote.getLoanQuoteOtherIdNumber(), isBankLmsContactCenterId);
          if (!"pass".equalsIgnoreCase(identityvalidation)) {
        	  loanScenarioBean.setStatus(Integer.valueOf(0));
        	  loanScenarioBean.setMessage(identityvalidation);
            return loanScenarioBean;
          } 
        }
      quote = personalLoanHelper.insertLoanQuote(quote, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, trackVisitId);
      
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (quote != null && quote.getError() != null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(quote.getError());
        return loanScenarioBean;
      } 
      if (quote.getLoanQuoteId() == null || quote.getLoanQuoteId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      quote.setLoanQuoteEmployerNameId(commonService.getAllEmployerIdByName(quote.getLoanQuoteEmployerName()));
      int previousQuoteId = (application != null && application.getAppQuoteId() != null) ? application.getAppQuoteId().intValue() : 0;
      quote.setAppApplyingFrom(quoteApplyingFrom.intValue());
      quote.setAppMobile(quoteMobile);
      quote.setAppNRIMobileNo(quoteMobileNRI);
      quote.setAppISDCode(quoteISD);
      quote.setLoanQuotePanCardNo(panCard);
      quote.setLoanQuoteOtherId(otherId);
      quote.setLoanQuoteOtherIdNumber(otherIdNu);
      if (quote.getLoanQuoteLoanPurposeId().intValue() == Constants.PENSION_LOAN_PURPOSE_ID.intValue()) {
        quote.setLoanQuotePreferredLoanBranch(preferredLoanBranch);
        quote.setLoanQuotePensionPayingStateId(pensionPayingStateId);
        quote.setLoanQuotePensionPayingCityId(pensionPayingCityId);
        quote.setLoanQuotePensionPayingDistrId(pensionPayingDistrId);
        quote.setLoanQuotePensionPayingBranchId(pensionPayingBranchId);
        quote.setLoanQuotePreferredStateId(preferredStateId);
        quote.setLoanQuotePreferredCityId(preferredCityId);
        quote.setLoanQuotePreferredDistrictId(preferredDistrictId);
        quote.setLoanQuotePreferredLoanBranchId(preferredLoanBranchId);
      } 
      if (quote.getLoanQuoteFirstName() != null) {
        quote.setLoanQuoteAddressLandmark(landMark);
        quote.setLoanQuoteStateId(stateId);
        quote.setLoanQuoteCityId(cityId);
      } 
      application = personalLoanHelper.insertAppLoan(quote, application, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null, Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null) ? bankLmsUser.getLmsUserIntermediaryId().intValue() : 0));
      if (application == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (application != null && application.getError() != null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(application.getError());
        return loanScenarioBean;
      } 
      if (application.getAppSeqId() == null || application.getAppSeqId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      SessionUtil.setPersonalLoanTypeSequenceId(application.getAppSeqId());
      if (isAppFoundForDedupInDropRejectStage)
        application.setAppMobileDedup(Integer.valueOf(0)); 
      if (isAppFoundForDedupInDropOffStage)
        application.setAppMobileDedup(Integer.valueOf(1)); 
      if (SessionUtil.getApplicationCRMLeadId() != null)
        application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId()); 
      quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(application.getAppQuoteId());
      if (quote.getLoanQuoteFirstName() != null) {
        quote.setLoanQuoteAddressLandmark(landMark);
        quote.setLoanQuoteStateId(stateId);
        quote.setLoanQuoteCityId(cityId);
      } 
      if(quote.getLoanQuoteLoanPurposeId().intValue() == 27 && quote.getLoanQuotePreferredBranchName() != null) {
    	  Integer branchId = commonService.getBranchIdByBranchName(quote.getLoanQuotePreferredBranchName());
    	  application.setAppBranchId(branchId);
      }
      if (quote != null && quote.getLoanQuoteId() != null) {
        loanScenarioBean = personalLoanHelper.callBRE(application, quote, bankLmsUser, Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
        if (quote.getLoanQuoteLoanPurposeId().intValue() != 27 && loanScenarioBean.getStatus().intValue() != 1)
          return loanScenarioBean; 
        application = loanScenarioBean.getApplicationPL();
        if (Constants.PERSONAL_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl) && 
          application != null && 
          SessionUtil.getApplicationCRMLeadId() != null) {
          CRMRequest crmRequest = new CRMRequest();
          crmRequest.setCrmLeadId(SessionUtil.getApplicationCRMLeadId());
          crmRequest.setReferenceNumber(application.getAppSeqId());
          crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
        } 
        if ((ajaxPostUrl.equalsIgnoreCase(Constants.PERSONAL_LOAN_ACTION) || 
          ajaxPostUrl.equalsIgnoreCase(Constants.PENSION_LOAN_ACTION) || 
          ajaxPostUrl.equalsIgnoreCase(Constants.GOLD_LOAN_ACTION) || 
          ajaxPostUrl.equalsIgnoreCase(Constants.CVE_ACTION)) && 
          application.getAppMobileVerificationCodeReceived() == null) {
          logger.info("PLProcessImpl.java :: LNo :: 1592 ::ajaxPostUrl:: " + ajaxPostUrl + "//mobile verificationCode//" + application.getAppMobileVerificationCodeReceived());
          String msgBody = communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
          msgBody = SbiUtil.urlEncode(msgBody);
          String SMS_TEXT = null;
          if (application.getAppApplyingFrom() == 2) {
        	  SMS_TEXT=Constants.SMS_STRING_NRI;
          } else {
        	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
          } 
          SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
          SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", application.getAppMobileVerificationCode().toString());
          SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(application.getAppISDCode()) + application.getAppMobileNo()).trim());
          
          if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
             logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppISDCode()) + application.getAppMobileNo()).trim() + " is " + application.getAppMobileVerificationCode().toString());
          }
          
          
          
          if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            loanScenarioBean.setMessage("OTP service is down");
            return loanScenarioBean;
          } 
          if (application.getAppApplyingFrom() == 2) {
            msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
            msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
            msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
            msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
            msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
            msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", application.getAppEmailVerificationCode());
            msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.AUTO_LOAN_PRODUCT_NAME);
            boolean sendStatus = false;
            String[] emailId = { application.getAppWorkEmail() };
            sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
            if (!sendStatus) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage("Email OTP service is down");
              return loanScenarioBean;
            } 
          } 
          application.setAppMobileVerificationCodeReceived("Y");
          application = this.personalLoanService.save(application);
        } 
        return loanScenarioBean;
      } 
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      
	} catch (SQLException ne) {
			logger.info("PersonalProcessManagerImpl.java LNo: 1843 :: processGetQuotes() :: " + ne.getMessage());
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
	}
		return loanScenarioBean;
  }
  
  public ApplicationFormPersonalLoan processSubmitQuote(Integer appSeqId, Integer requestIndex, ApplicationFormPersonalLoan appForm, String isDsrPage, Integer bankLMSUserId) throws SQLException, NullPointerException, RuntimeException, ParseException {
    ApplicationFormPersonalLoan appFormData = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
    if (appFormData == null)
      return null; 
    if ((appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK)) && 
      appFormData.getAppMobileVerified().equalsIgnoreCase("N") && appFormData.getAppEmailVerified().equalsIgnoreCase("N")) {
      appFormData.setError("Mobile OTP is not verified");
      return appFormData;
    } 
    ApplicationFormPersonalLoanQuote quote = this.personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appFormData.getAppQuoteId());
    if(quote != null && quote.getLoanQuoteLoanPurposeId().intValue() == 27)
    	appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
    if (quote == null)
      return null; 
    logger.info("PLProcessImpl.java :: LNo :: 2406 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()) && 
      !Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && 
      appFormData.getAppMobileNo() != null && !Constants.DUMMY_MOBILE_NO.contains(appFormData.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
      boolean isAppFoundForDedupInApplicationStage = false;
      isAppFoundForDedupInApplicationStage = this.personalLoanService.isAppFoundForDedupInApplicationStage(null, (appFormData.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : appFormData.getAppISDCode(), appFormData.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId());
      logger.info("PLProcessImpl.java :: LNo :: 2412 ::  thankyoupage isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
      if (isAppFoundForDedupInApplicationStage) {
        this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE6_ID, null, null, true);
        appFormData.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE6_ID));
        appFormData.setError(Constants.APP_DEDUPLICATION_MESSAGE);
        return appFormData;
      } 
    } 
    
	//validate name fields before saving
	//logger.info("first_name :: " + appForm.getAppFirstName());
	//logger.info("middle_name :: " + appForm.getAppMiddleName());
	//logger.info("last_name :: " + appForm.getAppLastName());
	

	 if (!ValidatorUtil.validateFirstName(appForm.getAppFirstName())) {
		 appFormData.setError("First name is not in correct format. Please enter only [a-z].");
		 return appFormData;
	 }
 	if (!ValidatorUtil.validateMiddleName(appForm.getAppMiddleName())) {
 		appFormData.setError("Middle name is not in correct format. Please enter only [a-z].");
 		return appFormData;
 	}
 	if (!ValidatorUtil.validateLastName(appForm.getAppLastName())) {
 		appFormData.setError("Last name is not in correct format. Please enter only [a-z] & do not include spaces.");
 		return appFormData;
 	}
	
    if (appForm.getAppFirstName() != null)
      appFormData.setAppFirstName(StringUtil.capitalize(appForm.getAppFirstName())); 
    if (appForm.getAppMiddleName() != null)
      appFormData.setAppMiddleName(StringUtil.capitalize(appForm.getAppMiddleName())); 
    if (appForm.getAppLastName() != null)
      appFormData.setAppLastName(StringUtil.capitalize(appForm.getAppLastName())); 
    if (appForm.getAppGender() != null)
      appFormData.setAppGender(appForm.getAppGender()); 
    if (appForm.getAppMaritalStatus() != null)
      appFormData.setAppMaritalStatus(appForm.getAppMaritalStatus()); 
    if (appForm.getAppAddress1() != null)
      appFormData.setAppAddress1(StringUtil.capitalize(appForm.getAppAddress1())); 
    if (appForm.getAppAddress2() != null)
      appFormData.setAppAddress2(StringUtil.capitalize(appForm.getAppAddress2())); 
    if (appForm.getAppAddressLandmark() != null)
      appFormData.setAppAddressLandmark(StringUtil.capitalize(appForm.getAppAddressLandmark())); 
    if (appForm.getAppCustomerSignature() != null)
      appFormData.setAppCustomerSignature(appForm.getAppCustomerSignature()); 
    if (appForm.getAppPermanentStateId() != null)
      appFormData.setAppPermanentStateId(appForm.getAppPermanentStateId()); 
    if (appForm.getAppPermanentCityId() != null)
      appFormData.setAppPermanentCityId(appForm.getAppPermanentCityId()); 
    if (appForm.getAppPermanentDistrictId() != null)
      appFormData.setAppPermanentDistrictId(appForm.getAppPermanentDistrictId()); 
    if (quote != null && (quote.getLoanQuoteLoanPurposeId().intValue() == 13 || quote.getLoanQuoteLoanPurposeId().intValue() == 12)) {
      if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0) {
        appFormData.setAppStateId(appForm.getAppStateId());
      } else {
        logger.info("PLProcessImpl.java :: LNo :: 2463 :: appForm.getAppStateId() is null  with AppSeqId " + appSeqId);
        appFormData.setError("Please select the  state.|appForm.appStateId|1");
        return appFormData;
      } 
      if (appForm.getAppCityId() != null && appForm.getAppCityId().intValue() > 0) {
        appFormData.setAppCityId(appForm.getAppCityId());
      } else {
        logger.info("PLProcessImpl.java :: LNo :: 2470 ::  appForm.getAppCityId() is null ");
        appFormData.setError("Please select the  city.|appForm.appCityId|1");
        return appFormData;
      } 
      if (appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER))
        if (appForm.getAppDistrictId() != null && appForm.getAppDistrictId().intValue() > 0) {
          appFormData.setAppDistrictId(appForm.getAppDistrictId());
        } else {
          logger.info("PLProcessImpl.java :: LNo :: 2478  :: appForm.getAppDistrictId() is null  with AppSeqId " + appSeqId);
          appFormData.setError("Please select the district.|appForm.appDistrictId|1");
          return appFormData;
        }  
    } else if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
      if (appForm.getAppStateId() != null) {
        appFormData.setAppStateId(appForm.getAppStateId());
      } else {
        logger.info("PLProcessIMPL.java :: LNo 2494 :: appForm.getAppStateId() is null  with AppSeqId " + appSeqId);
        appFormData.setError("Please select the  state.|appForm.appStateId|1");
        return appFormData;
      } 
      if (appForm.getAppCityId() != null) {
        appFormData.setAppCityId(appForm.getAppCityId());
      } else {
        logger.info("PLProcessIMPL.java :: LNo 2501 :: appForm.getAppCityId() is null  with AppSeqId " + appSeqId);
        appFormData.setError("Please select the  city.|appForm.appCityId|1");
        return appFormData;
      } 
      if (appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER))
        if (appForm.getAppDistrictId() != null) {
          appFormData.setAppDistrictId(appForm.getAppDistrictId());
        } else {
          logger.info("PLProcessIMPL.java :: LNo 2509 :: appForm.getAppDistrictId() is null  with AppSeqId " + appSeqId);
          appFormData.setError("Please select the district.|appForm.appDistrictId|1");
          return appFormData;
        }  
      if (appFormData.getAppSubTypeId() != null && appFormData.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS))
        if (appForm.getAppResidenceTypeId() != null) {
          appFormData.setAppResidenceTypeId(appForm.getAppResidenceTypeId());
        } else {
          logger.info("PersonalProcessIMPL.java :: LNo 2519 :: appForm.getAppResidenceTypeId() is null  with AppSeqId " + appSeqId);
          appFormData.setError("Please select the residence type.|appForm.appResidenceTypeId|1");
          return appFormData;
        }  
    } else if (quote != null && quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
      if (appForm.getAppStateId() != null) {
        appForm.setAppStateId(appForm.getAppStateId());
        appFormData.setAppStateId(appForm.getAppStateId());
      } 
      if (appForm.getAppCityId() != null) {
        appForm.setAppCityId(appForm.getAppCityId());
        appFormData.setAppCityId(appForm.getAppCityId());
      } 
      if (appForm.getAppDistrictId() != null)
        appFormData.setAppDistrictId(appForm.getAppDistrictId()); 
      if (appForm.getAppRelationshipWithBank() != null)
        appForm.setAppRelationshipWithBank(appForm.getAppRelationshipWithBank()); 
      if (appForm.getAppPensionType() != null)
        appFormData.setAppPensionType(appForm.getAppPensionType()); 
      if (appForm.getAppPensionPayOrdNumber() != null)
        appFormData.setAppPensionPayOrdNumber(appForm.getAppPensionPayOrdNumber()); 
      if (appForm.getAppEmploymentNature() != null)
        appFormData.setAppEmploymentNature(appForm.getAppEmploymentNature()); 
      if (appForm.getAppEmploymentType() != null)
        appFormData.setAppEmploymentType(appForm.getAppEmploymentType()); 
      if (appForm.getAppOrgName() != null)
        appFormData.setAppOrgName(appForm.getAppOrgName()); 
      if (appForm.getAppMonthlyPensionAmt() != null)
        appFormData.setAppMonthlyPensionAmt(appForm.getAppMonthlyPensionAmt()); 
      if (appForm.getAppPreferredLoanBranch() != null) {
        appFormData.setAppPreferredLoanBranch(appForm.getAppPreferredLoanBranch());
        logger.info("PLProcessIMPL.java :: getAppPreferredLoanBranch :: " + appFormData.getAppPreferredLoanBranch());
      } 
      if (appForm.getAppPensionPayingStateId() != null)
        appFormData.setAppPensionPayingStateId(appForm.getAppPensionPayingStateId()); 
      if (appForm.getAppPensionPayingCityId() != null)
        appFormData.setAppPensionPayingCityId(appForm.getAppPensionPayingCityId()); 
      if (appForm.getAppPensionPayingDistrId() != null)
        appFormData.setAppPensionPayingDistrId(appForm.getAppPensionPayingDistrId()); 
      if (appForm.getAppPensionPayingBranchId() != null)
        appFormData.setAppPensionPayingBranchId(appForm.getAppPensionPayingBranchId()); 
      if (appForm.getAppPreferredLoanBranch() != null) {
        appFormData.setAppPreferredLoanBranch(appForm.getAppPreferredLoanBranch());
        logger.info("PLProcessIMPL.java :: getAppPreferredLoanBranch :: " + appFormData.getAppPreferredLoanBranch());
      } 
      if (appForm.getAppPreferredStateId() != null)
        appFormData.setAppPreferredStateId(appForm.getAppPreferredStateId()); 
      if (appForm.getAppPreferredCityId() != null)
        appFormData.setAppPreferredStateId(appForm.getAppPreferredCityId()); 
      if (appForm.getAppPreferredDistrictId() != null)
        appFormData.setAppPreferredDistrictId(appForm.getAppPreferredDistrictId()); 
      if (appForm.getAppPreviousBranchId() != null)
        appFormData.setAppPreviousBranchId(appForm.getAppPreviousBranchId()); 
      if (appForm.getAppPensionPayOrdNumber() != null)
        appFormData.setAppPensionPayOrdNumber(appForm.getAppPensionPayOrdNumber()); 
      if (appForm.getAppPensionAccountNumber() != null)
        appFormData.setAppPensionAccountNumber(appForm.getAppPensionAccountNumber()); 
      if (appForm.getAppPreEmis() != null)
        appFormData.setAppPreEmis(appForm.getAppPreEmis()); 
      if (appForm.getAppPermanentStateId() != null)
        appFormData.setAppPermanentStateId(appForm.getAppPermanentStateId()); 
      if (appForm.getAppPermanentCityId() != null)
        appFormData.setAppPermanentCityId(appForm.getAppPermanentCityId()); 
      if (appFormData.getAppPickupCityId() != null)
        appForm.setAppPickupCityId(appFormData.getAppPickupCityId()); 
    } 
    if (appForm.getAppPincode() != null) {
      int pincodeInitial = appForm.getAppPincode().intValue() / 10000;
      String pinlastfix = appForm.getAppPincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppStateId() != null) ? appForm.getAppStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppPincode(appForm.getAppPincode());
        } else {
          if (appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
            appFormData.setError("Entered pincode does not belong to entered state.");
          } else {
            appFormData.setError("Entered pincode does not belong to entered state.|appForm.appPincode|2");
          } 
          return appFormData;
        } 
      } else {
        appFormData.setAppPincode(appForm.getAppPincode());
      } 
    } 
    if (appForm.getAppLocalityId() != null)
      appFormData.setAppLocalityId(appForm.getAppLocalityId()); 
    if (appForm.getAppEmploymentTypeId() != null)
      appFormData.setAppEmploymentTypeId(appForm.getAppEmploymentTypeId()); 
    if (appForm.getAppIndustryTypeId() != null)
      appFormData.setAppIndustryTypeId(appForm.getAppIndustryTypeId()); 
    if (appForm.getAppPanCardNo() != null)
      appFormData.setAppPanCardNo(appForm.getAppPanCardNo()); 
    appFormData.setAppNetAnnualIncome(appForm.getAppNetAnnualIncome());
    appFormData.setAppPreEmis(appForm.getAppPreEmis());
    if (appForm.getAppRelationshipWithBank() != null)
      appFormData.setAppRelationshipWithBank(appForm.getAppRelationshipWithBank()); 
    if (ValidatorUtil.isValid(appForm.getAppModeOfRepayment()))
      appFormData.setAppModeOfRepayment(appForm.getAppModeOfRepayment()); 
    if (appForm.getAppEducationalQualificationId() != null)
      appFormData.setAppEducationalQualificationId(appForm.getAppEducationalQualificationId()); 
    if (appForm.getAppNoOfDependent() != null && appForm.getAppNoOfDependent().intValue() > 0)
      appFormData.setAppNoOfDependent(appForm.getAppNoOfDependent()); 
    if (appForm.getAppSalaryAccNo() != null)
      appFormData.setAppSalaryAccNo(appForm.getAppSalaryAccNo()); 
    appFormData.setAppImmovableProperty(appForm.getAppImmovableProperty());
    appFormData.setAppBankDeposit(appForm.getAppBankDeposit());
    appFormData.setAppNscs(appForm.getAppNscs());
    appFormData.setAppPfOrPpf(appForm.getAppPfOrPpf());
    appFormData.setAppMutualFundUnits(appForm.getAppMutualFundUnits());
    appFormData.setAppExistingTotalLoanAmount(appForm.getAppExistingTotalLoanAmount());
    if (appForm.getAppPermanentAddress1() != null)
      appFormData.setAppPermanentAddress1(StringUtil.capitalize(appForm.getAppPermanentAddress1())); 
    if (appForm.getAppPermanentAddress2() != null)
      appFormData.setAppPermanentAddress2(StringUtil.capitalize(appForm.getAppPermanentAddress2())); 
    if (appForm.getAppPermanentAddressLandMark() != null)
      appFormData.setAppPermanentAddressLandMark(StringUtil.capitalize(appForm.getAppPermanentAddressLandMark())); 
    if (appForm.getAppPermanentStateId() != null)
      appFormData.setAppPermanentStateId(appForm.getAppPermanentStateId()); 
    if (appForm.getAppPermanentCityId() != null)
      appFormData.setAppPermanentCityId(appForm.getAppPermanentCityId()); 
    if (appForm.getAppPermanentDistrictId() != null)
      appFormData.setAppPermanentDistrictId(appForm.getAppPermanentDistrictId()); 
    if (appForm.getAppPermanentPincode() != null) {
      int pincodeInitial = appForm.getAppPermanentPincode().intValue() / 10000;
      String pinlastfix = appForm.getAppPermanentPincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppPermanentStateId() != null) ? appForm.getAppPermanentStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppPermanentPincode(appForm.getAppPermanentPincode());
        } else {
          appFormData.setError("Entered permanent address pincode does not belong to entered state.|appForm.appPermanentPincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppPermanentPincode(appForm.getAppPermanentPincode());
      } 
    } 
    if (appForm.getAppLoanEmployerName() != null)
      appFormData.setAppLoanEmployerName(appForm.getAppLoanEmployerName()); 
    String stdate = "";
    if (appForm.getAppCompanyJoiningMonth() != null && appForm.getAppCompanyJoiningYear() != null) {
      stdate = "01-" + ((appForm.getAppCompanyJoiningMonth().intValue() <= 12) ? appForm.getAppCompanyJoiningMonth().intValue() : 12) + "-" + appForm.getAppCompanyJoiningYear();
      appFormData.setAppCompanyJoiningDate(DateUtil.convertStringToDate(stdate));
    } 
    appFormData.setAppCompanyJoiningMonth(appForm.getAppCompanyJoiningMonth());
    if (appForm.getAppCompanyJoiningYear() != null)
      appFormData.setAppCompanyJoiningYear(appForm.getAppCompanyJoiningYear()); 
    if (appForm.getAppWorkExperienceYear() != null)
      appFormData.setAppWorkExperienceYear(appForm.getAppWorkExperienceYear()); 
    appFormData.setAppWorkExperienceMonth(appForm.getAppWorkExperienceMonth());
    if (appForm.getAppOfficeAddress1() != null)
      appFormData.setAppOfficeAddress1(StringUtil.capitalize(appForm.getAppOfficeAddress1())); 
    if (appForm.getAppOfficeAddress2() != null)
      appFormData.setAppOfficeAddress2(StringUtil.capitalize(appForm.getAppOfficeAddress2())); 
    if (appForm.getAppOfficeStateId() != null)
      appFormData.setAppOfficeStateId(appForm.getAppOfficeStateId()); 
    if (appForm.getAppOfficeCityId() != null)
      appFormData.setAppOfficeCityId(appForm.getAppOfficeCityId()); 
    if (appForm.getAppOfficeBranchId() != null)
      appFormData.setAppOfficeBranchId(appForm.getAppOfficeBranchId()); 
    if (appForm.getAppOfficeDistrictId() != null)
      appFormData.setAppOfficeDistrictId(appForm.getAppOfficeDistrictId()); 
    if (appForm.getAppOfficePincode() != null) {
      int pincodeInitial = appForm.getAppOfficePincode().intValue() / 10000;
      String pinlastfix = appForm.getAppOfficePincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppOfficeStateId() != null) ? appForm.getAppOfficeStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppOfficePincode(appForm.getAppOfficePincode());
        } else {
          appFormData.setError("Entered office pincode does not belong to entered state.|appForm.appOfficePincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppOfficePincode(appForm.getAppOfficePincode());
      } 
    } 
    if (appForm.getAppOfficePhone() != null)
      appFormData.setAppOfficePhone(Long.parseLong(appForm.getAppOfficePhone()+"")); 
    appFormData.setAppOfficePhoneStdCode(appForm.getAppOfficePhoneStdCode());
    if (appForm.getAppCoapplicantFirstName() != null)
      appFormData.setAppCoapplicantFirstName(StringUtil.capitalize(appForm.getAppCoapplicantFirstName())); 
    if (appForm.getAppCoapplicantMiddleName() != null)
      appFormData.setAppCoapplicantMiddleName(StringUtil.capitalize(appForm.getAppCoapplicantMiddleName())); 
    if (appForm.getAppCoapplicantLastName() != null)
      appFormData.setAppCoapplicantLastName(StringUtil.capitalize(appForm.getAppCoapplicantLastName())); 
    if (appForm.getAppCoapplicantAddress1() != null)
      appFormData.setAppCoapplicantAddress1(StringUtil.capitalize(appForm.getAppCoapplicantAddress1())); 
    if (appForm.getAppCoapplicantAddress2() != null)
      appFormData.setAppCoapplicantAddress2(StringUtil.capitalize(appForm.getAppCoapplicantAddress2())); 
    if (appForm.getAppCoapplicantLandmark() != null)
      appFormData.setAppCoapplicantLandmark(StringUtil.capitalize(appForm.getAppCoapplicantLandmark())); 
    if (appForm.getAppCoapplicantStateId() != null)
      appFormData.setAppCoapplicantStateId(appForm.getAppCoapplicantStateId()); 
    if (appForm.getAppCoapplicantPincode() != null) {
      int pincodeInitial = appForm.getAppCoapplicantPincode().intValue() / 10000;
      String pinlastfix = appForm.getAppCoapplicantPincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppCoapplicantStateId() != null) ? appForm.getAppCoapplicantStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppCoapplicantPincode(appForm.getAppCoapplicantPincode());
        } else {
          if (quote != null && quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
            appFormData.setError("Entered guarantor pincode does not belong to entered state.|appForm.appCoapplicantPincode");
          } else if (quote != null && quote.getLoanQuotePropertyOwnedBySpouse().equalsIgnoreCase("Y")) {
            appFormData.setError("Entered co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode");
          } else if (quote != null && quote.getLoanQuotePropertyOwnedBySpouse().equalsIgnoreCase("N")) {
            appFormData.setError("Entered guarantor pincode does not belong to entered state.|appForm.appCoapplicantPincode");
          } else {
            appFormData.setError("Entered co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode");
          } 
          return appFormData;
        } 
      } else {
        appFormData.setAppCoapplicantPincode(appForm.getAppCoapplicantPincode());
      } 
    } 
    if (appForm.getAppCoapplicantCityId() != null)
      appFormData.setAppCoapplicantCityId(appForm.getAppCoapplicantCityId()); 
    if (appForm.getAppCoapplicantDistrictId() != null)
      appFormData.setAppCoapplicantDistrictId(appForm.getAppCoapplicantDistrictId()); 
    appFormData.setAppCoapplicantEmploymentTypeId(appForm.getAppCoapplicantEmploymentTypeId());
    appFormData.setAppCoapplicantIndustryTypeId(appForm.getAppCoapplicantIndustryTypeId());
    appFormData.setAppCoapplicantNetAnnualIncome(appForm.getAppCoapplicantNetAnnualIncome());
    if (appForm.getAppCoapplicantNoOfDependent() != null && appForm.getAppCoapplicantNoOfDependent().intValue() > 0)
      appFormData.setAppCoapplicantNoOfDependent(appForm.getAppCoapplicantNoOfDependent()); 
    appFormData.setAppCoapplicantPreEmis(appForm.getAppCoapplicantPreEmis());
    appFormData.setAppCoapplicantImmovableProperty(appForm.getAppCoapplicantImmovableProperty());
    appFormData.setAppCoapplicantBankDeposit(appForm.getAppCoapplicantBankDeposit());
    appFormData.setAppCoapplicantNscs(appForm.getAppCoapplicantNscs());
    appFormData.setAppCoapplicantPfOrPpf(appForm.getAppCoapplicantPfOrPpf());
    appFormData.setAppCoapplicantGoldOrnaments(appForm.getAppCoapplicantGoldOrnaments());
    appFormData.setAppCoapplicantMutualFundUnits(appForm.getAppCoapplicantMutualFundUnits());
    appFormData.setAppCoapplicantExistingTotalLoanAmount(appForm.getAppCoapplicantExistingTotalLoanAmount());
    appFormData.setAppCoapplicant2FirstName(appForm.getAppCoapplicant2FirstName());
    appFormData.setAppCoapplicant2MiddleName(appForm.getAppCoapplicant2MiddleName());
    appFormData.setAppCoapplicant2LastName(appForm.getAppCoapplicant2LastName());
    appFormData.setAppCoapplicant2EmploymentTypeId(appForm.getAppCoapplicant2EmploymentTypeId());
    appFormData.setAppCoapplicant2IndustryTypeId(appForm.getAppCoapplicant2IndustryTypeId());
    appFormData.setAppCoapplicant2NetAnnualIncome(appForm.getAppCoapplicant2NetAnnualIncome());
    if (appForm.getAppCoapplicant2NoOfDependent() != null && appForm.getAppCoapplicant2NoOfDependent().intValue() > 0)
      appFormData.setAppCoapplicant2NoOfDependent(appForm.getAppCoapplicant2NoOfDependent()); 
    appFormData.setAppCoapplicant2PreEmis(appForm.getAppCoapplicant2PreEmis());
    appFormData.setAppCoapplicant2ImmovableProperty(appForm.getAppCoapplicant2ImmovableProperty());
    appFormData.setAppCoapplicant2BankDeposit(appForm.getAppCoapplicant2BankDeposit());
    appFormData.setAppCoapplicant2Nscs(appForm.getAppCoapplicant2Nscs());
    appFormData.setAppCoapplicant2PfOrPpf(appForm.getAppCoapplicant2PfOrPpf());
    appFormData.setAppCoapplicant2GoldOrnaments(appForm.getAppCoapplicant2GoldOrnaments());
    appFormData.setAppCoapplicant2MutualFundUnits(appForm.getAppCoapplicant2MutualFundUnits());
    appFormData.setAppCoapplicant2ExistingTotalLoanAmount(appForm.getAppCoapplicant2ExistingTotalLoanAmount());
    appFormData.setAppCoapplicant3FirstName(appForm.getAppCoapplicant3FirstName());
    appFormData.setAppCoapplicant3MiddleName(appForm.getAppCoapplicant3MiddleName());
    appFormData.setAppCoapplicant3LastName(appForm.getAppCoapplicant3LastName());
    appFormData.setAppCoapplicant3EmploymentTypeId(appForm.getAppCoapplicant3EmploymentTypeId());
    appFormData.setAppCoapplicant3IndustryTypeId(appForm.getAppCoapplicant3IndustryTypeId());
    appFormData.setAppCoapplicant3NetAnnualIncome(appForm.getAppCoapplicant3NetAnnualIncome());
    if (appForm.getAppCoapplicant3NoOfDependent() != null && appForm.getAppCoapplicant3NoOfDependent().intValue() > 0)
      appFormData.setAppCoapplicant3NoOfDependent(appForm.getAppCoapplicant3NoOfDependent()); 
    appFormData.setAppCoapplicant3PreEmis(appForm.getAppCoapplicant3PreEmis());
    appFormData.setAppCoapplicant3ImmovableProperty(appForm.getAppCoapplicant3ImmovableProperty());
    appFormData.setAppCoapplicant3BankDeposit(appForm.getAppCoapplicant3BankDeposit());
    appFormData.setAppCoapplicant3Nscs(appForm.getAppCoapplicant3Nscs());
    appFormData.setAppCoapplicant3PfOrPpf(appForm.getAppCoapplicant3PfOrPpf());
    appFormData.setAppCoapplicant3GoldOrnaments(appForm.getAppCoapplicant3GoldOrnaments());
    appFormData.setAppCoapplicant3MutualFundUnits(appForm.getAppCoapplicant3MutualFundUnits());
    appFormData.setAppCoapplicant3ExistingTotalLoanAmount(appForm.getAppCoapplicant3ExistingTotalLoanAmount());
    appFormData.setAppPanCardNo(appForm.getAppPanCardNo());
    appFormData.setAppEverDefaultedCreditCard(appForm.getAppEverDefaultedCreditCard());
    appFormData.setAppOtherId(appForm.getAppOtherId());
    appFormData.setAppOtherIdNumber(appForm.getAppOtherIdNumber());
    appFormData.setClonePermanentAddress(appForm.isClonePermanentAddress());
    appFormData.setCloneCoapplicantAddress1(appForm.isCloneCoapplicantAddress1());
    appFormData.setAppWorkExperience(appForm.getAppWorkExperience());
    if (appFormData.getAppSalesEnteredTime() != null)
      appFormData.setAppSalesEnteredTime(new Date()); 
    if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
      if (appForm.getAppNRIAddress1() != null)
        appFormData.setAppNRIAddress1(StringUtil.capitalize(appForm.getAppNRIAddress1())); 
      if (appForm.getAppNRIAddress2() != null)
        appFormData.setAppNRIAddress2(StringUtil.capitalize(appForm.getAppNRIAddress2())); 
      if (appForm.getAppNRIAddressLandmark() != null)
        appFormData.setAppNRIAddressLandmark(StringUtil.capitalize(appForm.getAppNRIAddressLandmark())); 
      if (appForm.getAppCountryId() != null)
        appFormData.setAppCountryId(appForm.getAppCountryId()); 
      if (appForm.getAppNRIState() != null)
        appFormData.setAppNRIState(StringUtil.capitalize(appForm.getAppNRIState())); 
      if (appForm.getAppNRICity() != null)
        appFormData.setAppNRICity(StringUtil.capitalize(appForm.getAppNRICity())); 
      if (appForm.getAppNRIZipcode() != null)
        appFormData.setAppNRIZipcode(StringUtil.capitalize(appForm.getAppNRIZipcode())); 
      if (appForm.getAppEMPNRIAddress1() != null)
        appFormData.setAppEMPNRIAddress1(StringUtil.capitalize(appForm.getAppEMPNRIAddress1())); 
      if (appForm.getAppEMPNRIAddress2() != null)
        appFormData.setAppEMPNRIAddress2(StringUtil.capitalize(appForm.getAppEMPNRIAddress2())); 
      if (appForm.getAppEMPNRIAddressLandmark() != null)
        appFormData.setAppEMPNRIAddressLandmark(StringUtil.capitalize(appForm.getAppEMPNRIAddressLandmark())); 
      if (appForm.getAppEMPCountryId() != null)
        appFormData.setAppEMPCountryId(appForm.getAppEMPCountryId()); 
      if (appForm.getAppEMPNRIState() != null)
        appFormData.setAppEMPNRIState(StringUtil.capitalize(appForm.getAppEMPNRIState())); 
      if (appForm.getAppEMPNRICity() != null)
        appFormData.setAppEMPNRICity(StringUtil.capitalize(appForm.getAppEMPNRICity())); 
      if (appForm.getAppEMPNRIZipcode() != null)
        appFormData.setAppEMPNRIZipcode(StringUtil.capitalize(appForm.getAppEMPNRIZipcode())); 
    } 
    if (appForm.getAppResidenceTypeId() != null)
      appFormData.setAppResidenceTypeId(appForm.getAppResidenceTypeId()); 
    appFormData.setAppPanCardLater(appForm.getAppPanCardLater());
    appFormData.setAppPanCardVerified("N");
    if (appFormData.getAppFilledAt() == null) {
      appFormData.setAppFilledAt(new Date());
      if (appFormData.getAppAlertStatusType() != null)
        appFormData.setAppAlertStatusType(null); 
    } 
    if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() != 27 && 
      !ValidatorUtil.isValid(appFormData.getAppBranchId())) {
      appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
      return appFormData;
    } 
    if ((appForm.getAppHaveAadhaarNumber() == null || appForm.getAppHaveAadhaarNumber().intValue() == 0) && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() > 0) {
      appForm.setAppHaveAadhaarNumber(quote.getLoanQuoteHaveAadhaarNumber());
      appFormData.setAppHaveAadhaarNumber(quote.getLoanQuoteHaveAadhaarNumber());
    } 
    if (appForm.getAppPanCardLater() == null && quote.getLoanQuotePanCardLater() != null && (quote.getLoanQuotePanCardLater().booleanValue() || !quote.getLoanQuotePanCardLater().booleanValue())) {
      appForm.setAppPanCardLater(quote.getLoanQuotePanCardLater());
      appFormData.setAppPanCardLater(quote.getLoanQuotePanCardLater());
    } 
    if ((appForm.getAppPanCardNo() == null || appForm.getAppPanCardNo() == "") && quote.getLoanQuotePanCardNo() != null && quote.getLoanQuotePanCardNo() != "") {
      appForm.setAppPanCardNo(quote.getLoanQuotePanCardNo());
      appFormData.setAppPanCardNo(quote.getLoanQuotePanCardNo());
    } 
    if ((appForm.getAppOtherId() == null || appForm.getAppOtherId().intValue() == 0) && quote.getLoanQuoteOtherId() != null && quote.getLoanQuoteOtherId().intValue() > 0) {
      appForm.setAppOtherId(quote.getLoanQuoteOtherId());
      appFormData.setAppOtherId(quote.getLoanQuoteOtherId());
    } 
    if ((appForm.getAppOtherIdNumber() == null || appForm.getAppOtherIdNumber() == "") && quote.getLoanQuoteOtherIdNumber() != null && quote.getLoanQuoteOtherIdNumber() != "") {
      appForm.setAppOtherIdNumber(quote.getLoanQuoteOtherIdNumber());
      appFormData.setAppOtherIdNumber(quote.getLoanQuoteOtherIdNumber());
    } 
    if ((isDsrPage.equalsIgnoreCase("false") && appForm.getAppAlternateMobileNumber() != null && !"".equals(appForm.getAppAlternateMobileNumber()) && 
      appForm.getAppAltMobileVerified() == null) || (appForm.getAppAltMobileVerified() != null && appForm.getAppAltMobileVerified().equalsIgnoreCase("N"))) {
      appFormData.setError("Please verify alternate Mobile Number.|");
      return appFormData;
    } 
    if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
      boolean isBankLmsContactCenterId = this.commonService.getBankLmsUserRole(bankLMSUserId);
      logger.info(" PLProcessImpl.java :: LNo :: 3006 ::  isBankLmsContactCenterId : " + isBankLmsContactCenterId + " bankLMSUserId : " + bankLMSUserId + " with AppSeqId " + appSeqId);
      String identityvalidation = this.SbiUtil.getIdentityValidation(false, appForm.getAppPanCardNo(), appForm.getAppHaveAadhaarNumber(), appForm.getAppOtherId(), appForm.getAppOtherIdNumber(), isBankLmsContactCenterId);
      if (!"pass".equalsIgnoreCase(identityvalidation)) {
        appFormData.setError(identityvalidation);
        return appFormData;
      } 
    } 
    if (appForm.getAppCustomerSignature() != null)
      appFormData.setAppCustomerSignature(appForm.getAppCustomerSignature()); 
    if (appForm.getAppEmployerLandmark() != null)
      appFormData.setAppEmployerLandmark(appForm.getAppEmployerLandmark()); 
    if (quote.getLoanQuoteHaveAadhaarNumber() != null)
      appFormData.setAppHaveAadhaarNumber(quote.getLoanQuoteHaveAadhaarNumber()); 
    if (appForm.getAppHaveAadhaarNumber() != null)
      appFormData.setAppHaveAadhaarNumber(appForm.getAppHaveAadhaarNumber());
    
    if(quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
    	appFormData.setAppPersonalLoanId(Integer.valueOf(13));
    	appFormData.setAppLoanAmount(quote.getLoanQuoteLoanAmountTaken()/100000);
    }
    
    appFormData = this.personalLoanService.save(appFormData);
    logger.info("PLProcessImpl.java :: LNo :: 3034 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_DATA_SAVED, null, false);
    int nsdlPANStatus = 0;
    if (appForm.getAppPanCardLater() != null && appForm.getAppPanCardLater().booleanValue()) {
      nsdlPANStatus = 1;
    } else if (ValidatorUtil.isValid(appFormData.getAppPanCardNo())) {
      PanApiInputParams apiInputParams = new PanApiInputParams();
      apiInputParams.setApiApplicantTypeId(Integer.valueOf(1));
      apiInputParams.setApiAppSeqId(appFormData.getAppSeqId());
      if (isDsrPage.equalsIgnoreCase("true")) {
        apiInputParams.setApiCallingSourceId(Integer.valueOf(2));
      } else {
        apiInputParams.setApiCallingSourceId(Integer.valueOf(1));
      } 
      apiInputParams.setApiMethodName("getPanDetails");
      apiInputParams.setApiProductId(Constants.PERSONAL_LOAN_ID);
      logger.info("PLProcessImpl.java :: LNo :: 2345 appSeqId===" + appFormData.getAppSeqId() + " calling pan service callPanServiceApi()");
      PanApiReturnResponse panApiResponseObj = this.panServiceAction.callPanServiceApi(apiInputParams);
      logger.info("PLProcessImpl.java :: LNo :: 2349 appSeqId===" + appFormData.getAppSeqId() + "===response pan service status id===" + panApiResponseObj.getApiStatusId() + "===Api Error===" + panApiResponseObj.getApiErrors());
      if (panApiResponseObj.getApiStatusId().intValue() == 1)
        nsdlPANStatus = 1; 
    } 
   // logger.info("PLProcessImpl.java :: LNo :: 3050 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (nsdlPANStatus == 1) ? Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS : Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL, null, false);
    if (nsdlPANStatus == 1) {
      appFormData.setAppPanCardVerified("Y");
      appFormData.setAppPanVerifiedDatetime(new Date());
    } 
    boolean isCibilCall = true;
    if (appFormData.getAppDataSourceId() != null && appFormData.getAppDataSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() && 
      quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 11 && quote.getLoanQuoteIndustryType() != null && (
      quote.getLoanQuoteIndustryType().intValue() == 84 || quote.getLoanQuoteIndustryType().intValue() == 1 || 
      quote.getLoanQuoteIndustryType().intValue() == 15 || quote.getLoanQuoteIndustryType().intValue() == 16 || 
      quote.getLoanQuoteIndustryType().intValue() == 17 || quote.getLoanQuoteIndustryType().intValue() == 19 || quote.getLoanQuoteIndustryType().intValue() == 25 || 
      quote.getLoanQuoteIndustryType().intValue() == 87 || 
      quote.getLoanQuoteIndustryType().intValue() == 5 || quote.getLoanQuoteIndustryType().intValue() == 18) && 
      quote.getLoanQuoteEmploymentNature() != null && quote.getLoanQuoteEmploymentNature().intValue() == 2)
      isCibilCall = false;
    
    if(quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
    	isCibilCall = false;
    }
  
    if (isCibilCall) {
      Integer count = this.commonService.getCountByAppIdAndLoanType(appFormData.getAppSeqId().toString(), "A12", "SUCCESS");
      logger.info("Checkinfo if Cibil request already made for App SEQ Id :: " + appFormData.getAppSeqId());
      if (count != null && count <= 0)
        try {
          if (appForm != null) {
            boolean isContactCenterUser = false;
            if (isDsrPage.equalsIgnoreCase("true"))
              isContactCenterUser = this.commonService.getBankLmsUserRole(bankLMSUserId); 
            logger.info("PersonalProcessManagerImpl.java LNo: 2289 :: Calling :: isCibilBypass :: " + isContactCenterUser + " with AppSeqId " + appSeqId);
            cibilCallForPersonalLoan(quote, appFormData);
            appFormData.setAppCibilScore(appFormData.getAppCibilScore());
            logger.info("PersonalProcessManagerImpl.java LNo: 1773 :: Calling :: appFormData.getAppCibilScore() :: " + appFormData.getAppCibilScore() + " with AppSeqId " + appSeqId);
          } 
        } catch (NullPointerException ne) {
            logger.info("PersonalProcessManagerImpl.java :: LNO :: 2384 Exception caught while cibil :: " + appFormData.getAppSeqId() + " :: ", ne);
          }
    }
    MasterPlProduct product = null;
    if (quote.getLoanQuoteLoanPurposeId().intValue() != 27)
    	product = this.commonService.getPersonalLoanProductById(appFormData.getAppPersonalLoanId());
    logger.info("PLProcessImpl.java :: LNo :: 3100 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    if ((!appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP) && 
      appForm.isAppCallRSMService() == null) || isDsrPage.equalsIgnoreCase("true")) {
      appForm.setAppCallRSMService(Boolean.valueOf(true));
     // logger.info("PLProcessImpl.java :: LNo :: 2447 ::isAppCallRSMService::NULL-TRUE::::" + appForm.isAppCallRSMService());
    } 
    if (appForm.isAppCallRSMService() != null && appForm.isAppCallRSMService().booleanValue()) {
    //  logger.info("PLProcessImpl.java :: LNo :: 2450 ::isAppCallRSMService::FETCHING FROM RSM::::" + appForm.isAppCallRSMService());
      try {
        if (quote.getLoanQuoteExistingPLSanctionDT() != null) {
          quote.setLoanQuoteExistingPLRepaymentYear(DateUtil.getAge(quote.getLoanQuoteExistingPLSanctionDT()));
          logger.info("PLProcessImpl.java :: LNo :: 2454 ::ExistingPLRepaymentYear::::::" + quote.getLoanQuoteExistingPLRepaymentYear() + "--quote.getLoanQuoteExistingPLSanctionDT()--" + quote.getLoanQuoteExistingPLSanctionDT());
        } 
        if (quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
          quote.setLoanQuoteNetMonthlySalary(new Double(quote.getLoanQuoteMonthPensionAmt().intValue()));
          logger.info("PLProcessImpl.java :: LNo :: 2458 ::QuoteNetMonthlySalary::::::" + quote.getLoanQuoteNetMonthlySalary() + "--quote.getLoanQuoteMonthPensionAmt()--" + quote.getLoanQuoteMonthPensionAmt() + "--quote.getLoanQuoteLoanPurposeId()--" + quote.getLoanQuoteLoanPurposeId());
        } 
        JSONObject quoteData = JSONUtil.beanObjectToJSONObjct(quote);
        quoteData = this.SbiUtil.getDBCredentialForHelper(quoteData);
        JSONObject appData = JSONUtil.beanObjectToJSONObjct(appFormData);
        appData.put("appOfferJsonData", "");
        appData.put("appRSMResponse", "");
        //logger.info("PLProcessImpl.java :: LNo :: 2467 ::appData::::::" + appData.toString());
		if (quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
			if ((product != null && product.getPlProductId() != null && (product.getPlProductId().intValue() == Constants.XPRESS_CREDIT_IT_PRODUCT_ID.intValue()
							|| product.getPlProductId().intValue() == Constants.XPRESS_CREDIT_ELITE_PRODUCT_ID
									.intValue()
							|| product.getPlProductId().intValue() == Constants.XPRESS_CREDIT_NPE_PRODUCT_ID
									.intValue()
							|| product.getPlProductId().intValue() == Constants.REGULAR_PENSION.intValue()
							|| product.getPlProductId().intValue() == Constants.JAI_JAWAN_PENSION_LOAN
									.intValue()
							|| product.getPlProductId().intValue() == Constants.FAMILY_PENSION.intValue()))
							|| product.getPlProductId().intValue() == 14) {
						appData.put("appPersonalLoanId", 2);
						//logger.info("PLProcessImpl.java :: LNo :: 2473 ::PRODUCTCODE::::::" + product.getPlProductId()
							//	+ "--product--" + product + "--appData--" + appData.toString());
			}
		}
         
        if (quote.getLoanQuoteLoanPurposeId().intValue() == 27) {
          if (quote.getLoanQuotePanCardNo() != null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1) {
            quoteData.put("loanQuoteAadhaarPanStatus", 1);
          } else if ((quote.getLoanQuotePanCardNo() == null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1) || (quote.getLoanQuotePanCardNo() != null && quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 2)) {
            quoteData.put("loanQuoteAadhaarPanStatus", 2);
          } else {
            quoteData.put("loanQuoteAadhaarPanStatus", 3);
          } 
          quoteData.put("loanQuoteLoanRequestedAmount", quote.getLoanQuoteLoanAmountTaken());
          appData.put("appRelationshipWithBank", appFormData.getAppRelationshipWithBank());
          appFormData.setAppRSMdecision(Integer.valueOf(1));
        } 
        String engineRequest = null;
        RSMResponse rsmBean = new RSMResponse();
        if (quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
        	if(Constants.IS_ENGINE_OBF) {
        
          JSONObject finalQuoteData = MapperUtil.convertPersonalLoan(quoteData);
          JSONObject finalAppData = MapperUtil.convertPersonalLoanApplication(appData);
          engineRequest = "rsmRequest={\"engineRequest\":" + finalQuoteData.toString() + ",\"applicationFormRequest\":" + finalAppData.toString() + "}";
        } else {
          engineRequest = "rsmRequest={\"engineRequest\":" + quoteData.toString() + ",\"applicationFormRequest\":" + appData.toString() + "}";
        } 
        logger.info("PLProcessImpl.java :: LNo :: 2496 ::  urlParameters :: " + engineRequest + " with AppSeqId " + appSeqId);
        JSONObject appRSMResponse = this.commonEngine.callingRSMEngine(engineRequest, Constants.PERSONAL_LOAN_ID);
        rsmBean = (RSMResponse)JSONUtil.getObjctFromJSON(rsmBean, appRSMResponse.toString());
        logger.info("PLProcessImpl.java :: LNo :: 2500 ::rsmBean::::::" + rsmBean);
        if (ValidatorUtil.isValid(appFormData.getAppPersonalLoanId()) && (
          appFormData.getAppPersonalLoanId().intValue() == 2 || appFormData.getAppPersonalLoanId().intValue() == 5 || appFormData.getAppPersonalLoanId().intValue() == 14 || appFormData.getAppPersonalLoanId().intValue() == 6 || appFormData.getAppPersonalLoanId().intValue() == 7 || appFormData.getAppPersonalLoanId().intValue() == 8 || appFormData.getAppPersonalLoanId().intValue() == 9 || appFormData.getAppPersonalLoanId().intValue() == 10)) {
          rsmBean = getFinalRSMScore(rsmBean, appFormData, quote);
          logger.info("PLProcessImpl.java :: LNo :: 2506 ::rsmBean::::::" + rsmBean + "--LoanPurposeId--" + appFormData.getAppPersonalLoanId().intValue());
        } 
        appFormData.setAppOfferJsonData("reqData = " + engineRequest);
        appFormData.setAppRSMResponse("responseData = " + rsmBean.toString());
        if (rsmBean != null)
          if (rsmBean.getRsmStatus() == 1 || rsmBean.getRsmStatus() == 3) {
            appFormData.setAppRSMResponse(appRSMResponse.toString());
            appFormData.setAppRSMStatus(Integer.valueOf(rsmBean.getRsmStatus()));
            appFormData.setAppRSMScore(rsmBean.getRsmScore());
            appFormData.setAppRSMdecision(Integer.valueOf(rsmBean.getRsmDecision()));
            appFormData.setAppRSMGrade(Integer.valueOf(rsmBean.getRsmGrade()));
          } else {
            appFormData.setAppRSMdecision(Integer.valueOf(2));
            appFormData.setAppRSMStatus(Integer.valueOf(rsmBean.getRsmStatus()));
            appFormData.setAppRSMResponse(rsmBean.getMessage());
          }  }
        this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (rsmBean == null) ? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON : ((rsmBean.getRsmDecision() == 0 || rsmBean.getRsmDecision() == 2) ? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_19 : Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_147), null, false);
      } catch (NullPointerException ne) {
          logger.info("PersonalProcessManagerImpl.java LNo: 2468 :: Calling RSM Engine :: " + ne.getMessage());
          appFormData.setAppRSMdecision(Integer.valueOf(2));
          this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON, null, false);
        } catch (JSONException e) {
        logger.info("PersonalProcessManagerImpl.java LNo: 2472 :: Calling RSM Engine :: " + e.getMessage());
        appFormData.setAppRSMdecision(Integer.valueOf(2));
        this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON, null, false);
      } 
    } else {
      appFormData.setAppRSMdecision(Integer.valueOf(2));
    } 
   // logger.info("PLProcessImpl.java :: LNo :: 2541 :: appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    if (SessionUtil.getApplicationType() != null || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } else {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } 
      statusRequest.setState(17);
      statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
      statusRequest.setRsmDecision(appFormData.getAppRSMdecision().intValue());
      statusRequest.setNsdlPANStatus(1);
      statusRequest.setAppPanCardNo(appFormData.getAppPanCardNo());
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
      statusRequest.setApplicationLeadType(appFormData.getAppDataSourceId().intValue());
      StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
     // logger.info("PLProcessImpl.java :: LNo :: 2560 ::statusManagerResponse::::::" + statusManagerResponse);
      if (statusManagerResponse.getStatus() != 0) {
        appFormData.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
      } else if (appFormData.getAppLoanStatusId().intValue() == 0) {
        appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return appFormData;
      } 
      if (statusManagerResponse.isEligibleToInsertLog() && statusManagerResponse.getStatusCallLogs() > 0)
        this.personalLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
      if (statusManagerResponse.isInitiateCallAttempt())
        appFormData.setAppTotalCallAttempt(Integer.valueOf(0)); 
//      logger.info("PLProcessImpl.java :: LNo :: 2574 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
      if (SessionUtil.getApplicationType() != null && 
        SessionUtil.getApplicationType().intValue() != 0)
        if (SessionUtil.getApplicationType().intValue() == 1) {
          if (statusManagerResponse.getStatusLead() != 0 && 
            SessionUtil.getLeadId() != null) {
            ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            if (lead != null) {
              if (lead.getLeadAlertStatusType() != null)
                lead.setLeadAlertStatusType(null); 
              lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
              lead = this.commonService.save(lead);
            } 
          } 
          logger.info("PLProcessImpl.java :: LNo :: 3254 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
        } else if (SessionUtil.getApplicationType().intValue() == 2) {
          logger.info("PLProcessImpl.java :: LNo :: 3256 ::  appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
        }  
    } 
    try {
      boolean isAbleToSendBMOrSalesTeamMessage = false;
      if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()))
        if (Constants.REFERENCE_NUMBER_BASED_ON_CLUSTER) {
          appFormData = this.refGenerateUtil.generateReferenceNumber(appFormData);
          if (appFormData != null && appFormData.getError() != null)
            return appFormData; 
        } else {
          this.lastReferenceNumber = this.personalLoanService.getLastGeneratedReferenceNumber(Constants.PERSONAL_LOAN_ID);
          this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_PL, Constants.SOURCE_STRING_PL, this.lastReferenceNumber);
          boolean isReferenceIdAvailable = false;
          isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailable(this.appRefKey);
         // logger.info("PL isReferenceIdAvailable 1 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          if (!isReferenceIdAvailable) {
            appFormData.setAppReferenceId(this.appRefKey);
          } else {
            this.lastReferenceNumber = this.appRefKey;
            this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_PL, Constants.SOURCE_STRING_PL, this.lastReferenceNumber);
            isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailable(this.appRefKey);
            logger.info("PL isReferenceIdAvailable 2 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
            if (!isReferenceIdAvailable) {
              appFormData.setAppReferenceId(this.appRefKey);
            } else {
              this.lastReferenceNumber = this.appRefKey;
              this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_PL, Constants.SOURCE_STRING_PL, this.lastReferenceNumber);
              isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailable(this.appRefKey);
              logger.info("PL isReferenceIdAvailable 3 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
              appFormData.setAppReferenceId(this.appRefKey);
            } 
          } 
          logger.info("PL isReferenceIdAvailable 4 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL7");
          appFormData = this.personalLoanService.save(appFormData);
        }  
      logger.info("PLProcessImpl.java :: LNo :: 2098 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
      if (ValidatorUtil.isValid(appFormData.getAppReferenceId())) {
        if (appFormData.getAppRSMdecision().intValue() == 1 || appFormData.getAppRSMdecision().intValue() == 3)
          try {
            boolean sendMailStatus = false;
            if (isDsrPage.equalsIgnoreCase("true")) {
              sendMailStatus = true;
            } else if (appFormData.getAppAipMailSendStatus() == null) {
              sendMailStatus = true;
            } 
            if (sendMailStatus) {
              if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail()) && ValidatorUtil.isValid(appFormData.getAppReferenceId()) && quote.getLoanQuoteLoanPurposeId().intValue() != 27)
                this.taskExecutorService.sendingEmailForPersonalLoan(requestIndex, Integer.valueOf(1), appFormData); 
              if (ValidatorUtil.isValid(appFormData.getAppReferenceId()))
                this.taskExecutorService.sendingSMSForPersonalLoan(requestIndex, Integer.valueOf(1), appFormData, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage); 
            } 
            this.taskExecutorService.generatePDFForPersonalLoan(appFormData, quote);
          } catch (NullPointerException ne) {
              logger.info("PLProcessImpl.java LNo : 2580 : Exception caught in while generating PDF " + ne.getMessage());
            }   
        String cveFlag = Constants.CVE_ACTION;
        String ajaxPostUrl = Constants.CVE_ACTION;
        String PL = Constants.PERSONAL_LOAN_ACTION;
        String PN = Constants.PENSION_LOAN_ACTION;
        String GL = Constants.GOLD_LOAN_ACTION;
        logger.info("PLProcessImpl.java LNo : 2599 ::cveFlag:: " + cveFlag + "..ajaxPostUrl.." + ajaxPostUrl);
        logger.info("PLProcessImpl.java LNo : 2600 ::APP_PL_TYPE_PERSONAL.." + Constants.APP_PL_TYPE_PERSONAL + "..APP_PL_TYPE_PENSION.." + Constants.APP_PL_TYPE_PENSION + "..APP_PL_TYPE_GOLD.." + Constants.APP_PL_TYPE_GOLD);
//        logger.info("PLProcessImpl.java LNo : 2601 ::PERSONAL_LOAN_ACTION.." + Constants.PERSONAL_LOAN_ACTION + "..PENSION_LOAN_ACTION.." + Constants.PENSION_LOAN_ACTION + "..GOLD_LOAN_ACTION.." + Constants.GOLD_LOAN_ACTION);
        if (Constants.PERSONAL_LOAN_ACTION.equalsIgnoreCase(PL) || Constants.PENSION_LOAN_ACTION.equalsIgnoreCase(PN) || Constants.GOLD_LOAN_ACTION.equalsIgnoreCase(GL)) {
          logger.info("PLProcessImpl.java LNo : 2620 :: Passing OCAS Personal Leads to CRM Service ");
          try {
            if (appFormData != null && quote != null && 
              !Constants.CRM_BYPASS)
              if (appFormData.getAppCRMLeadId() != null && isDsrPage.equalsIgnoreCase("true")) {
                CRMRequest crmRequest = new CRMRequest();
                crmRequest.setCrmLeadId(appFormData.getAppCRMLeadId());
                crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
                crmRequest.setReferenceNumber(appFormData.getAppSeqId());
                crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
                logger.info("PersonalProcessManagerImpl.java LNo: 485 : DO NOT CALLING callCrmUpdate Service...............");
              } else {
                logger.info("PLProcessImpl.java LNo : 2617 :: BEFORE calling callCrm Passing OCAS Personal Leads to CRM Service ");
                callCrm(appFormData, quote);
                logger.info("PLProcessImpl.java LNo : 2619 :: AFTer calling callCrm Passing OCAS Personal Leads to CRM Service ");
              }  
          } catch (NullPointerException ne) {
              logger.info("PersonalLoanProcessImpl.java LNo:2610 :: exceptionwhile crm " + ne.getMessage());
           } 
        } 
      } else {
        logger.info("PersonalProcessManagerImpl reference number is not able to generate  with AppSeqId " + appSeqId);
        if (appFormData != null) {
          appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
          return appFormData;
        } 
      } 
      logger.info("PLProcessImpl.java :: LNo :: 3333 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    } catch (NullPointerException ne) {
        logger.info("PersonalLoanProcessImpl.java LNo: 2624 :: exception caught while generating ref number " + ne.getMessage());
        if (appFormData == null)
          appFormData = new ApplicationFormPersonalLoan(); 
        appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return appFormData;
      }  
    logger.info("PLProcessImpl.java :: LNo :: 3345 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    return appFormData;
  }
  
  public CBSCallResponse processCbsCall(Integer appSeqId, Integer requestIndex, MasterCBSCall masterCbsCall, String isDsrPage, Integer bankLMSUserId, Integer visitId, Integer cbsCallId, Integer loanQuoteLoanPurposeId, Integer socialMediaId, Integer deviceId, String receivedAction) throws SQLException {
    CBSCallResponse cbsCallResponse = new CBSCallResponse();
    String cveFlag = Constants.CVE_ACTION;
    //logger.info("PersonalProcessManagerImpl LNO :: 2791 --cveFlag--" + cveFlag + "--receivedAction--" + receivedAction);
    masterCbsCall.setCbsLoanTypeId(Constants.PERSONAL_LOAN_ID);
    int count = this.commonService.getCBSApplicationCount(masterCbsCall.getCbsIsdCode(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID).intValue();
    if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
      cbsCallResponse.setResponseMsg(Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
      cbsCallResponse.setStatus(Integer.valueOf(0));
      logger.info("PersonalProcessManagerImpl.java LNo :2644:: INSIDE IF " + cbsCallResponse.getStatus());
      return cbsCallResponse;
    } 
    cbsCallResponse.setStatus(Integer.valueOf(0));
    logger.info("PersonalProcessManagerImpl.java LNo :2644:: ELSE " + cbsCallResponse.getStatus());
    try {
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (receivedAction != null && !receivedAction.equalsIgnoreCase(cveFlag) && 
        !Constants.CBS_DEDUPE_BYPASS && loanQuoteLoanPurposeId != null) {
        logger.info("PLProcessImpl.java :: LNo 2737 :: CBS_DEDUPE_BYPASS EXCEPT CVE ");
        String oldMobile = "";
        String isdCode = "";
        if (appSeqId == null && masterCbsCall.getCbsMobileNumber() != null) {
          isdCode = (masterCbsCall.getCbsIsdCode() == null) ? Constants.COUNTRY_CODE_INDIA : masterCbsCall.getCbsIsdCode().toString();
          oldMobile = masterCbsCall.getCbsMobileNumber();
        } 
        if (!Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
          boolean isAppFoundForDedupInApplicationStage = false;
          isAppFoundForDedupInApplicationStage = this.personalLoanService.isAppFoundForDedupInApplicationStage(null, isdCode, oldMobile, loanQuoteLoanPurposeId);
          if (isAppFoundForDedupInApplicationStage) {
            cbsCallResponse.setResponseMsg(Constants.APP_DEDUPLICATION_MESSAGE);
            return cbsCallResponse;
          } 
          isAppFoundForDedupInDropOffStage = this.personalLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
          isAppFoundForDedupInDropRejectStage = this.personalLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
        } 
      } 
      if (receivedAction != null && receivedAction.equalsIgnoreCase(cveFlag)) {
        logger.info("PLProcessImpl.java :: LNo 2737 :: CBS_DEDUPE_BYPASS for CVE ");
        if (Constants.CBS_DEDUPE_BYPASS && loanQuoteLoanPurposeId != null) {
          String oldMobile = "";
          String isdCode = "";
          if (appSeqId == null && masterCbsCall.getCbsMobileNumber() != null) {
            isdCode = (masterCbsCall.getCbsIsdCode() == null) ? Constants.COUNTRY_CODE_INDIA : masterCbsCall.getCbsIsdCode().toString();
            oldMobile = masterCbsCall.getCbsMobileNumber();
          } 
          if (!Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.personalLoanService.isAppFoundForDedupInApplicationStage(null, isdCode, oldMobile, loanQuoteLoanPurposeId);
            if (isAppFoundForDedupInApplicationStage) {
              cbsCallResponse.setResponseMsg(Constants.APP_DEDUPLICATION_MESSAGE);
              return cbsCallResponse;
            } 
            isAppFoundForDedupInDropOffStage = this.personalLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
            isAppFoundForDedupInDropRejectStage = this.personalLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
          } 
        } 
      } 
      masterCbsCall.setCbsVisitId(visitId);
      if (cbsCallId != null) {
        MasterCBSCall oldMasterCbsCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
        oldMasterCbsCall.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
        oldMasterCbsCall.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
        oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCode());
        logger.info("PersonalProcessManagerImpl LNO :: 2912 BEFOR SAVE getCbsIsdCode ::" + oldMasterCbsCall.getCbsIsdCode());
        oldMasterCbsCall.setCbsTypeOfRelationship(masterCbsCall.getCbsTypeOfRelationship());
        logger.info("PersonalProcessManagerImpl LNO :: 3443 AFTER SAVE oldMasterCbsCall :: " + oldMasterCbsCall);
      } else {
        masterCbsCall.setCbsRequiestedTime(new Date());
        masterCbsCall.setCbsOtpVerified(Integer.valueOf(0));
        masterCbsCall = this.commonService.save(masterCbsCall);
        logger.info("PersonalProcessManagerImpl LNO :: 3443");
        if (masterCbsCall == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 3453");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        SessionUtil.setPlTypeCbsCallId(masterCbsCall.getCbsId());
      } 
      cbsCallResponse.setCbsCallId(masterCbsCall.getCbsId());
      cbsCallResponse.setVisitId(visitId);
      MasterCBSResponse masterCBSResponse = null;
      if (masterCbsCall.getCbsResponseId() == null) {
        masterCBSResponse = new MasterCBSResponse();
      } else {
        masterCBSResponse = this.commonService.getMasterCBSResponseById(masterCbsCall.getCbsResponseId());
        if (masterCBSResponse == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 3469");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } 
      if (masterCbsCall.getCbsNoOfExistingXpressCredit() != null) {
        Integer noOfExistingXpressCredit = masterCbsCall.getCbsNoOfExistingXpressCredit();
        if (noOfExistingXpressCredit.toString().equals("2")) {
          logger.info("PersonalLoanProcessImpl.java LNo: 2769 ::CBS_NO_OF_EXIST_XPRESS_CREDIT is equal to 2");
          cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } 
      if (masterCbsCall.getCbsNoOfExistingPensionLoan() != null) {
        Integer noOfExistingAccount = masterCbsCall.getCbsNoOfExistingPensionLoan();
        if (noOfExistingAccount.toString().equals("2")) {
          logger.info("PersonalLoanProcessImpl.java LNo: 3302 ::CBS_NO_OF_EXIST_ACCOUNT is equal to 2");
          cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } 
      logger.info("Before Caaling callingCBSEngineForCIFLevelInformation");
      JSONObject cbsEngineResponseJson = this.cbsUtil.callingCBSEngineForCIFLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID.intValue());
      CBSCustomerInformation cbsCustomerInformation = new CBSCustomerInformation();
      this.cbsUtil.setCBSCustomerInformationBean(cbsCustomerInformation, cbsEngineResponseJson);
      if (cbsCustomerInformation.getStatus() != null && cbsCustomerInformation.getStatus().equals("0")) {
        if (cbsCustomerInformation.getErrorReason() != null) {
          if (cbsCustomerInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
            cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
          } else {
            cbsCallResponse.setResponseMsg(cbsCustomerInformation.getErrorReason().trim());
          } 
        } else {
          cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
        } 
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (cbsCustomerInformation.getError() != null && cbsCustomerInformation.getError().trim().length() > 0) {
        logger.info("PersonalProcessManagerImpl LNO :: 2698 Either Account No. or Mobile No. do not match with our data base.");
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (!masterCbsCall.getCbsMobileNumber().equals(cbsCustomerInformation.getMOBILENO())) {
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      JSONObject accountInfoResponseJson = this.cbsUtil.callingCBSEngineForAccountLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID.intValue());
      if (accountInfoResponseJson.has("ERROR_CODE") && accountInfoResponseJson.get("ERROR_CODE") != null && accountInfoResponseJson.get("ERROR_CODE").toString().trim().length() > 0) {
        logger.info("Personal Loan  LNO :: 2423");
        if (accountInfoResponseJson.get("ERROR_DESCRIPTION") != null) {
          if (accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
            cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
            logger.info("callingCBSEngineForAccountLevelInformation INVALID CHECK DIGIT:: " + cbsCallResponse.getResponseMsg());
          } else {
            cbsCallResponse.setResponseMsg(accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim());
            logger.info("callingCBSEngineForAccountLevelInformation ERROR_DESCRIPTION :: " + cbsCallResponse.getResponseMsg());
          } 
        } else {
          cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
          logger.info("callingCBSEngineForAccountLevelInformation ELSE system has encountered a technical error :: " + cbsCallResponse.getResponseMsg());
        } 
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      String prodId = "";
      if (accountInfoResponseJson.has("AccType"))
        prodId = accountInfoResponseJson.getString("AccType"); 
      if (accountInfoResponseJson.has("Interest_Category"))
        prodId = String.valueOf(String.valueOf(String.valueOf(prodId))) + accountInfoResponseJson.getString("Interest_Category"); 
      cbsCustomerInformation.setProductId(prodId);
      //logger.info("Response from Customer Enquiry and Account info Data Account no : " + cbsCustomerInformation.getACCOUNTNUMBER() + " Product Id : " + cbsCustomerInformation.getProductId());
      cbsCustomerInformation.setAccountDesc(accountInfoResponseJson.getString("AccountDescription"));
      String cve = "cve";
      //logger.info("PersoanlProcessManager.java LNO 3971 cveFlag:: " + cveFlag + "..receivedAction.." + receivedAction + "--PersonalId--" + SessionUtil.getPersonalTypeId());
      boolean needToByPassLoanAccountInformation = true;
      if (masterCbsCall.getCbsTypeOfRelationship() != null) {
        Integer typeOfRelationship = masterCbsCall.getCbsTypeOfRelationship();
        //logger.info("needToByPassLoanAccountInformation FOR CVE typeOfRelationship:" + typeOfRelationship);
        if (masterCbsCall.getCbsLoanPurpose() != null && masterCbsCall.getCbsLoanPurpose().intValue() == 23) {
          //logger.info("needToByPassLoanAccountInformation FOR 23 :: " + masterCbsCall.getCbsLoanPurpose() + " with TypeOfRelationship " + masterCbsCall.getCbsTypeOfRelationship());
        } else if (masterCbsCall.getCbsLoanPurpose() != null && masterCbsCall.getCbsLoanPurpose().intValue() == 27) {
          if (typeOfRelationship.toString().equals("1") || typeOfRelationship.toString().equals("2")) {
            needToByPassLoanAccountInformation = false;
            //logger.info("needToByPassLoanAccountInformation FOR 27 typeOfRelationship 1 or 2:: " + needToByPassLoanAccountInformation);
          } 
        } else if (receivedAction != null && receivedAction.equalsIgnoreCase(cve) && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL) {
          //logger.info("needToByPassLoanAccountInformation INSIDE typeOfRelationship for CVE :: ");
          if (typeOfRelationship.toString().equals("1") || typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3") || typeOfRelationship.toString().equals("4") || typeOfRelationship.toString().equals("5")) {
            needToByPassLoanAccountInformation = false;
            //logger.info("needToByPassLoanAccountInformation ELSE typeOfRelationship for CVE :: " + needToByPassLoanAccountInformation);
          } 
        } else if (typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3")) {
          needToByPassLoanAccountInformation = false;
          //logger.info("needToByPassLoanAccountInformation ELSE typeOfRelationship 2 or 3 :: " + needToByPassLoanAccountInformation);
        } 
      } 
      if (ValidatorUtil.isValid(cbsCustomerInformation.getProductId())) {
        if (masterCbsCall.getCbsLoanPurpose() != null && masterCbsCall.getCbsLoanPurpose().intValue() == 23) {
          int productId = Integer.parseInt(cbsCustomerInformation.getProductId());
          if (productId == 62517901 || productId == 62517902 || productId == 64502021 || productId == 64504214 || productId == 62507901) {
            cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
          if (masterCbsCall.getCbsTypeOfRelationship().intValue() == 1) {
            if (productId != 63507901 && productId != 63507902 && productId != 63507903 && productId != 63507911 && productId != 63507912 && 
              productId != 63517901 && productId != 63517902 && productId != 63517911 && productId != 63517912 && productId != 63517913 && 
              productId != 63517914 && productId != 63517915 && productId != 64514215 && productId != 62511138 
              //&& productId != 10111101 && productId != 62512220	// changes to bypass Pension Loan account
              ) {
              logger.info("PersonalProcessManagerImpl.java LNo: 3455 : Product id is not eligible For Avialing loan");
              cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
              cbsCallResponse.setStatus(Integer.valueOf(0));
              return cbsCallResponse;
            } 
          } else if (masterCbsCall.getCbsTypeOfRelationship().intValue() == 2 && 
            productId != 63507901 && productId != 63507902 && productId != 63507903 && productId != 63507911 && productId != 63507912 && 
            productId != 63517901 && productId != 63517902 && productId != 63517911 && productId != 63517912 && productId != 63517913 && 
            productId != 63517914 && productId != 63517915 && productId != 64514215 
            //&& productId != 10111101 && productId != 62512220	// changes to bypass Pension Loan account
            ) {
            logger.info("PersonalProcessManagerImpl.java LNo: 3465 : Product id is not eligible For Avialing loan");
            cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } else if (needToByPassLoanAccountInformation) {
          logger.info("PersonalProcessManagerImpl.java LNo: 2744 : Product existence check for Loan Id " + Constants.PERSONAL_LOAN_ID + " and product id " + cbsCustomerInformation.getProductId());
          boolean productFound = this.commonService.isCbsMappingsExistByProductId(Constants.PERSONAL_LOAN_ID, cbsCustomerInformation.getProductId());
          logger.info("PersonalProcessManagerImpl.java LNo: 2746 : Product found status " + productFound);
          if (!productFound) {
            logger.info("PersonalProcessManagerImpl.java LNo: 2706 : Product id not match with mapping table");
            cbsCallResponse.setResponseMsg("Provided account No. does not pertain to selected type of relationship");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } 
      } else {
        logger.info("PersonalProcessManagerImpl.java LNo: 2712 : Product id not received  in Customer information service");
        cbsCallResponse.setResponseMsg("Product id not received  in Customer information service");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      try {
        masterCbsCall.setCbsResponseTime(new Date());
        masterCBSResponse.setCbsLoanTypeId(Constants.PERSONAL_LOAN_ID);
        logger.info("PersonalProcessManagerImpl.java LNo: 2893 :" + masterCBSResponse.getCbsLoanTypeId());
        if (ValidatorUtil.isValid(cbsCustomerInformation.getReferenceNumber()))
          masterCBSResponse.setCbsReferenceNumber(cbsCustomerInformation.getReferenceNumber()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getACCOUNTNUMBER()))
          masterCBSResponse.setCbsAccountNumber(cbsCustomerInformation.getACCOUNTNUMBER()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getCIFNUMBER()))
          masterCBSResponse.setCbsCifNumber(cbsCustomerInformation.getCIFNUMBER()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getSALUTATION()))
          masterCBSResponse.setCbsSalutation(cbsCustomerInformation.getSALUTATION()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getFIRSTNAME()))
          masterCBSResponse.setCbsFirstName(cbsCustomerInformation.getFIRSTNAME()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getMIDDLENAME()))
          masterCBSResponse.setCbsMiddleName(cbsCustomerInformation.getMIDDLENAME()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getLASTNAME()))
          masterCBSResponse.setCbsLastName(cbsCustomerInformation.getLASTNAME()); 
        //logger.info("PersonalProcessManagerImpl.java LNo: 2916 :" + cbsCustomerInformation.getACCOUNTNUMBER());
        //logger.info("PersonalProcessManagerImpl.java LNo: 2917 :" + cbsCustomerInformation.getCIFNUMBER());
        getCIFNUMBER(cbsCustomerInformation.getCIFNUMBER());
        if (ValidatorUtil.isValid(cbsCustomerInformation.getCIFNUMBER())) {
          masterCbsCall.setCveCifNumber(cbsCustomerInformation.getCIFNUMBER());
          //logger.info("PersonalProcessManagerImpl.java LNo: 3232 getCbsCifNumber:::" + masterCbsCall.getCveCifNumber());
        } 
        //logger.info("PersonalProcessManagerImpl.java LNo: 2918 :" + cbsCustomerInformation.getSALUTATION());
        //logger.info("PersonalProcessManagerImpl.java LNo: 2919 :" + cbsCustomerInformation.getFIRSTNAME());
        if (ValidatorUtil.isValid(cbsCustomerInformation.getDATEOFBIRTH()))
          masterCBSResponse.setCbsDateOfBirth(DateUtil.convertStringToDateWithOutDelimiter(cbsCustomerInformation.getDATEOFBIRTH())); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE1()))
          masterCBSResponse.setCbsAddressLine1(cbsCustomerInformation.getADDRESSLINE1()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE2()))
          masterCBSResponse.setCbsAddressLine2(cbsCustomerInformation.getADDRESSLINE2()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE3()))
          masterCBSResponse.setCbsAddressLine3(cbsCustomerInformation.getADDRESSLINE3()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getDISTRICT()))
          masterCBSResponse.setCbsDistrict(cbsCustomerInformation.getDISTRICT()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getSUBDISTRICT()))
          masterCBSResponse.setCbsSubDistrict(cbsCustomerInformation.getSUBDISTRICT()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getCITY()))
          masterCBSResponse.setCbsCity(cbsCustomerInformation.getCITY()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getSTATE()))
          masterCBSResponse.setCbsState(cbsCustomerInformation.getSTATE()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getPINCODE()))
          masterCBSResponse.setCbsPinCode(cbsCustomerInformation.getPINCODE()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getMOBILENO()))
          masterCBSResponse.setCbsMobileNo(cbsCustomerInformation.getMOBILENO()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getLANDLINENO()))
          masterCBSResponse.setCbsLandlineNo(cbsCustomerInformation.getLANDLINENO()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getPANNUMBER()))
          masterCBSResponse.setCbsPanNumber(cbsCustomerInformation.getPANNUMBER()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getIDTYPE()))
          masterCBSResponse.setCbsIdType(cbsCustomerInformation.getIDTYPE()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getIDNUMBER()))
          masterCBSResponse.setCbsIdNumber(cbsCustomerInformation.getIDNUMBER()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getEMAIL()))
          masterCBSResponse.setCbsEmail(cbsCustomerInformation.getEMAIL()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getPhoneBusiness()) && 
          StringUtils.isNumeric(cbsCustomerInformation.getPhoneBusiness()))
          masterCBSResponse.setCbsPhoneBusiness(cbsCustomerInformation.getPhoneBusiness()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getCountry()))
          masterCBSResponse.setCbsCountry(cbsCustomerInformation.getCountry()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getPFNo()))
          masterCBSResponse.setCbsPfNo(cbsCustomerInformation.getPFNo()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getError()))
          masterCBSResponse.setCbsError(cbsCustomerInformation.getError()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getAccountDesc()))
          masterCBSResponse.setCbsAccountDescription(cbsCustomerInformation.getAccountDesc()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getGender()))
          masterCBSResponse.setCbsGender(cbsCustomerInformation.getGender()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getBranchCode()))
          masterCBSResponse.setCbsBranchCode(cbsCustomerInformation.getBranchCode()); 
        if (ValidatorUtil.isValid(cbsCustomerInformation.getProductId()))
          masterCBSResponse.setCbsProductId(cbsCustomerInformation.getProductId()); 
        masterCBSResponse = this.commonService.save(masterCBSResponse);
        if (masterCBSResponse == null) {
          logger.info("PersonalLoanProcessImpl.java LNo: 2815");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } catch (NullPointerException ne) {
          logger.info("PersonalLoanProcessImpl.java LNo: 2978 :: exception caught " + ne.getMessage());
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      CBSLoanAccountInformation cbsLoanAccountInformation = new CBSLoanAccountInformation();
      if (needToByPassLoanAccountInformation) {
        JSONObject loanAccountInformationcbsResponseJson = this.cbsUtil.callingCBSEngineForLoanAccountInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.CBS_LOAN_TYPE_XPRESS_CREDIT_TOP_UP);
        this.cbsUtil.setCbsLoanAccountInformation(cbsLoanAccountInformation, loanAccountInformationcbsResponseJson);
        if (cbsLoanAccountInformation.getStatus() != null && cbsLoanAccountInformation.getStatus().equals("0")) {
          if (cbsLoanAccountInformation.getErrorReason() != null) {
            if (cbsLoanAccountInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
              cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
            } else {
              cbsCallResponse.setResponseMsg(cbsLoanAccountInformation.getErrorReason().trim());
            } 
          } else {
            cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
          } 
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        cbsLoanAccountInformation.setBranchCode(cbsCustomerInformation.getBranchCode());
        cbsLoanAccountInformation.setReferenceNumber(cbsEngineResponseJson.getString("REQUEST_REFERENCE_NUMBER"));
      } 
      masterCbsCall.setCbsResponseId(masterCBSResponse.getCbsResponseId());
      logger.info("CBS_RES_ID :: " + masterCBSResponse.getCbsResponseId() + " with AppSeqId ::" + appSeqId);
      if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmail())) {
        masterCbsCall.setCbsEmail(masterCBSResponse.getCbsEmail());
        masterCbsCall.setCbsEmailMask(masterCBSResponse.getCbsEmail());
        masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(1));
      } else {
        masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(0));
      } 
      masterCbsCall = this.commonService.save(masterCbsCall);
      if (masterCbsCall == null) {
        logger.info("PersonalLoanProcessImpl.java LNo: 2857 :: ");
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      try {
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getExistingLoanAcc()))
          masterCBSResponse.setCbsExistingLoanAcNo(cbsLoanAccountInformation.getExistingLoanAcc()); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getSchemeName()))
          masterCBSResponse.setCbsSchemeName(cbsLoanAccountInformation.getSchemeName()); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getSanctionedLimit()))
          masterCBSResponse.setCbsSanctionedLimit(Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getSanctionedLimit()))); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getSanctiondate()))
          masterCBSResponse.setCbsSanctionDate(DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getSanctiondate())); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getLoanTenure()))
          masterCBSResponse.setCbsLoanTenure(Integer.valueOf(Integer.parseInt(cbsLoanAccountInformation.getLoanTenure()))); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getRepaymentstartdate()))
          masterCBSResponse.setCbsRepaymentStartDate(DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getRepaymentstartdate())); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getValueofPrimarySecurity()))
          masterCBSResponse.setCbsValueOfPrimarySecurity(Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getValueofPrimarySecurity()))); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getIRACStatus()))
          masterCBSResponse.setCbsIracStatus(cbsLoanAccountInformation.getIRACStatus()); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getEMI()))
          masterCBSResponse.setCbsEmi(Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getEMI()))); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getOutstanding()))
          masterCBSResponse.setCbsOutstanding(Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getOutstanding()))); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getInstituteName()))
          masterCBSResponse.setCbsInstituteName(cbsLoanAccountInformation.getInstituteName()); 
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getBranchCode()))
          masterCBSResponse.setCbsBranchCode(cbsLoanAccountInformation.getBranchCode()); 
        masterCBSResponse = this.commonService.save(masterCBSResponse);
        if (masterCBSResponse == null) {
          logger.info("PersonalLoanProcessImpl.java LNo: 2902 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } catch (ParseException ex) {
        logger.info("PersonalLoanProcessImpl.java LNo: 2909 :: exception caught " + ex.getMessage());
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      }  
      if (!Constants.CBS_IRAC_STATUS_BYPASS && 
        needToByPassLoanAccountInformation && 
        Integer.parseInt(masterCBSResponse.getCbsIracStatus()) >= Constants.CBS_IRAC_STATUS.intValue()) {
        logger.info("PersonalLoanProcessImpl.java LNo: 2922 ::CBS_IRAC_STATUS is greater than or equal to 4");
        cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      boolean needToInsert = true;
      ApplicationFormPersonalLoan app = null;
      ApplicationFormPersonalLoanQuote quote = null;
      ApplicationFormCveLoan cveApp = null;
      logger.info("PersonalLoanProcessImpl.java LNo: 3435 receivedAction before if condition----" + receivedAction);
      if (receivedAction.equalsIgnoreCase("cve")) {
      //  logger.info("PersonalLoanProcessImpl.java LNo: 3449 receivedAction for CVE----" + receivedAction);
        if (masterCBSResponse.getCbsAppSeqId() == null) {
          cveApp = new ApplicationFormCveLoan();
          logger.info("PersonalLoanProcessImpl.java LNo: 3453 ::IF CONDITION getCbsAppSeqId----" + masterCBSResponse.getCbsAppSeqId());
        } else {
          logger.info("PersonalLoanProcessImpl.java LNo: 3458 :: ELSE CONDITION getCbsAppSeqId----" + masterCBSResponse.getCbsAppSeqId());
          cveApp = this.personalLoanService.getApplicationFormCveLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
          logger.info("PersonalLoanProcessImpl.java LNo: 3460 ::getCbsAppSeqId----" + masterCBSResponse.getCbsAppSeqId());
          if (cveApp == null) {
            logger.info("PersonalLoanProcessImpl.java LNo: 2938 ::");
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
          needToInsert = false;
        } 
        try {
          cveApp.setAppEmailVerified("N");
          logger.info("getAppEmailVerified ::" + cveApp.getAppEmailVerified());
          cveApp.setAppMobileVerified("N");
          logger.info("getAppMobileVerified ::" + cveApp.getAppMobileVerified());
          if (cveApp.getAppLeadUpdateTime() == null)
            cveApp.setAppLeadUpdateTime(new Date()); 
          if (cveApp.getCreatedDate() == null)
            cveApp.setCreatedDate(new Date()); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())) {
            if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCbsCall.getCbsIsdCode().toString())) {
              cveApp.setAppApplyingFrom(1);
              logger.info("IF CONDITION getAppApplyingFrom ::" + cveApp.getAppApplyingFrom());
            } else {
              cveApp.setAppApplyingFrom(2);
              logger.info("ELSE CONDITION getAppApplyingFrom ::" + cveApp.getAppApplyingFrom());
            } 
          } else {
            cveApp.setAppApplyingFrom(1);
            logger.info("ELSE CONDITION getAppApplyingFrom(1) ::" + cveApp.getAppApplyingFrom());
          } 
          cveApp.setAppLeadUpdateTime(new Date());
         // logger.info("Phone Business ::" + StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()));
          if (ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())) {
            cveApp.setAppISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
           // logger.info("setAppISDCode ::" + masterCbsCall.getCbsIsdCode());
            SessionUtil.setISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
          //  logger.info("setISDCode ::" + masterCbsCall.getCbsIsdCode());
          } 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
            cveApp.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
            //logger.info("setAppMobileNo ::" + masterCbsCall.getCbsMobileNumber());
            SessionUtil.setMobile(masterCbsCall.getCbsMobileNumber());
            //logger.info("setMobile ::" + masterCbsCall.getCbsMobileNumber());
          } 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
            cveApp.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(masterCbsCall.getCbsMobileNumber()));
           // logger.info("getAppMobileVerified ::" + cveApp.getAppMobileVerified());
          } 
          cveApp.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
          //logger.info("getCbsAccountNumber ::" + cveApp.getCbsAccountNumber());
          cveApp.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
          //logger.info("getCbsMobileNumber :: " + masterCbsCall.getCbsMobileNumber());
          cveApp.setCveProductCategory(masterCbsCall.getCveProductCategory());
          //logger.info("getCveProductCategory :: " + cveApp.getCveProductCategory());
          cveApp.setCveSalutation(masterCbsCall.getCveSalutation());
          //logger.info("getCveSalutation :: " + cveApp.getCveSalutation());
          cveApp.setCveAppFirstName(masterCbsCall.getCveAppFirstName());
          //logger.info("getCveAppFirstName :: " + cveApp.getCveAppFirstName());
          cveApp.setCveAppMiddleName(masterCbsCall.getCveAppMiddleName());
          //logger.info("getCveAppMiddleName :: " + cveApp.getCveAppMiddleName());
          cveApp.setCveAppLastName(masterCbsCall.getCveAppLastName());
          //logger.info("getCveAppLastName :: " + cveApp.getCveAppLastName());
          if (masterCbsCall.getCbsEmail() != null) {
            cveApp.setCveAppEmail(masterCbsCall.getCbsEmail());
            //logger.info("getCveAppEmail CBS :: " + cveApp.getCveAppEmail());
          } else {
            cveApp.setCveAppEmail(masterCbsCall.getCbsEmail());
            //logger.info("getCveAppEmail :: " + cveApp.getCveAppEmail());
          } 
          cveApp.setCveAppPrevBranchId(masterCbsCall.getLoanQuoteWorkBranchId());
          //logger.info("getCveAppPrevBranchId :: " + cveApp.getCveAppPrevBranchId());
          cveApp.setCveAppConsentRevokeYes(masterCbsCall.getCveAppConsentRevokeYes());
          //logger.info("getCveAppConsentRevokeYes :: " + cveApp.getCveAppConsentRevokeYes());
          if (!ValidatorUtil.isValid(cveApp.getAppReferenceId())) {
            this.lastReferenceNumber = this.personalLoanService.getLastGeneratedReferenceNumberCVE(Constants.CVE_ID);
            this.appRefKey = this.SbiUtil.getApplicationReferenceId("2", "4", this.lastReferenceNumber);
            boolean isReferenceIdAvailable = false;
            isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailableCVE(this.appRefKey);
            if (!isReferenceIdAvailable) {
              cveApp.setAppReferenceId(this.appRefKey);
            } else {
              this.lastReferenceNumber = this.appRefKey;
              this.appRefKey = this.SbiUtil.getApplicationReferenceId("2", "4", this.lastReferenceNumber);
              isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailable(this.appRefKey);
              if (!isReferenceIdAvailable) {
                cveApp.setAppReferenceId(this.appRefKey);
              } else {
                this.lastReferenceNumber = this.appRefKey;
                this.appRefKey = this.SbiUtil.getApplicationReferenceId("2", "4", this.lastReferenceNumber);
                isReferenceIdAvailable = this.personalLoanService.isReferenceIdAvailableCVE(this.appRefKey);
                cveApp.setAppReferenceId(this.appRefKey);
              } 
            } 
            logger.info("PersonalProcessManagerImpl.java LNo: 483 : BEFORE SAVE appRefKey--" + this.appRefKey);
            logger.info("PersonalProcessManagerImpl.java LNo: 483 : BEFORE SAVE appRefKey--" + this.appRefKey);
          } 
          //logger.info("PersonalProcessManagerImpl LNO :: 3443 BEFORE SAVE1 cveApp ::" + cveApp);
          cveApp = this.personalLoanService.save(cveApp);
          //logger.info("PersonalProcessManagerImpl LNO :: 3443 AFTER SAVE2 cveApp ::" + cveApp);
          //logger.info("PersonalLoanProcessImpl.java LNo: 3856 :: Insert into Personal table----AFTER----");
          logger.info("PersonalLoanProcessImpl.java LNo: 4024 ::BEFORE----" + cveApp.getAppSeqId());
          SessionUtil.setPersonalLoanTypeSequenceId(cveApp.getAppSeqId());
          logger.info("PersonalLoanProcessImpl.java LNo: 4026 ::AFTER-----" + cveApp.getAppSeqId());
          logger.info("PersonalLoanProcessImpl.java LNo: 4026 ::getPersonalLoanTypeSequenceId\t-----" + SessionUtil.getPersonalLoanTypeSequenceId());
        } catch (SQLException se) {
            logger.info("PersonalLoanProcessImpl.java LNo: 3201 :: exception caught " + se.getMessage());
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          }  
        cbsCallResponse.setAppSeqId(cveApp.getAppSeqId());
        logger.info("PersonalLoanProcessImpl.java LNo: 3594 cveApp.getAppSeqId::" + cveApp.getAppSeqId());
        masterCBSResponse.setCbsAppSeqId(cveApp.getAppSeqId());
        logger.info("PersonalLoanProcessImpl.java LNo: 3596 cveApp.getAppSeqId::" + cveApp.getAppSeqId() + "--CBSresSeqId--" + masterCBSResponse.getCbsAppSeqId());
        //logger.info("PersonalLoanProcessImpl.java LNo: 3599 masterCBSResponse::" + masterCBSResponse);
        //logger.info("PersonalLoanProcessImpl.java LNo: 3599 masterCBSResponse::" + masterCBSResponse.toString());
        masterCBSResponse = this.commonService.save(masterCBSResponse);
        if (masterCBSResponse == null) {
          logger.info("PersonalLoanProcessImpl.java LNo: 3107 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
          masterCbsCall.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSResponse.getCbsEmail()));
          logger.info("PLProcessImpl.java :: LNo :: 4143  :: email otp" + masterCbsCall.getCbsEmailOtpCode());
        } 
        //logger.info("Mobile number " + masterCbsCall.getCbsMobileNumber());
        logger.info("mobile otp " + cveApp.getAppMobileVerificationCode());
        masterCbsCall.setCbsOtpCode(String.valueOf(cveApp.getAppMobileVerificationCode()));
        String msgBody = null;
        if (isDsrPage.equalsIgnoreCase("true")) {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
        } else {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        } 
        msgBody = this.SbiUtil.urlEncode(msgBody);
        String SMS_TEXT = null;
        if (masterCbsCall.getCbsIsdCode() == null) {
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Invalid ISD code");
          return cbsCallResponse;
        } 
        if (masterCbsCall.getCbsIsdCode().intValue() == Integer.parseInt(Constants.COUNTRY_CODE_INDIA)) {
        	SMS_TEXT=Constants.SMS_STRING_INDIAN;
        } else {
        	SMS_TEXT=Constants.SMS_STRING_NRI;
        }
        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
        SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCbsCall.getCbsOtpCode().toString());
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
      	  logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode().toString());
        }
        
        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          logger.info("PersonalProcessManagerImpl LNO :: 3161");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Mobile OTP service is down");
          return cbsCallResponse;
        } 
        if (masterCbsCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
          ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
          msgBody = String.valueOf(String.valueOf(String.valueOf(Constants.FIRST_EMAIL_PART))) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
          if (msgBody != null) {
            msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(String.valueOf(String.valueOf(Constants.PROTOCOL))) + Constants.IP_URL_INTERNET + Constants.BANK_IMAGE_FOLDER + "/");
            msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
            msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
            msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
            msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
            msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
            boolean sendStatus = false;
            String[] emailId = { masterCBSResponse.getCbsEmail() };
            logger.info("PersonalProcessManagerImpl.java LNo: 4006 :sendEmail--> emailId::: " + emailId);
            sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
            logger.info("PersonalProcessManagerImpl.java LNo: 4009 :sendEmail--> sendStatus::: " + sendStatus);
            if (!sendStatus) {
              logger.info("PersonalProcessManagerImpl LNO :: 3187");
              cbsCallResponse.setResponseMsg("Email OTP service is down");
              cbsCallResponse.setStatus(Integer.valueOf(0));
              return cbsCallResponse;
            } 
          } 
        } 
        masterCbsCall.setCbsOtpCount(String.valueOf(1));
        masterCbsCall = this.commonService.save(masterCbsCall);
        if (masterCbsCall == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 3207");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          return cbsCallResponse;
        } 
        cbsCallResponse.setResponseMsg("");
        if (isDsrPage.equalsIgnoreCase("true")) {
          cbsCallResponse.setStatus(Integer.valueOf(2));
        } else {
          cbsCallResponse.setStatus(Integer.valueOf(1));
        } 
      } else {
        if (masterCBSResponse.getCbsAppSeqId() == null) {
          app = new ApplicationFormPersonalLoan();
          quote = new ApplicationFormPersonalLoanQuote();
        } else {
          app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
          logger.info("PersonalLoanProcessImpl.java LNo: 3450 ::getCbsAppSeqId----" + masterCBSResponse.getCbsAppSeqId());
          if (app == null) {
            logger.info("PersonalLoanProcessImpl.java LNo: 2938 ::");
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
          needToInsert = false;
          if (app.getAppQuoteId() != null) {
            quote = this.personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(app.getAppQuoteId());
            if (quote == null) {
              logger.info("PersonalLoanProcessImpl.java LNo: 2947 ::");
              cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
              cbsCallResponse.setStatus(Integer.valueOf(0));
              return cbsCallResponse;
            } 
          } else {
            quote = new ApplicationFormPersonalLoanQuote();
          } 
        } 
          if (receivedAction.equalsIgnoreCase(Constants.PENSION_LOAN_ACTION))
            quote.setLoanQuoteLoanPurposeId(Integer.valueOf(23)); 
          quote.setLoanQuoteHaveSalaryAccountWithSbi("Y");
          quote.setLoanQuoteActive("Y");
          quote.setLoanQuoteDeleted("N");
          quote.setLoanQuoteCreatedLmsUserId(Constants.OTHER_USER_ID);
          quote.setLoanQuoteEntryTime(new Date());
          quote.setLoanQuoteUpdatedTime(new Date());
          quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
          quote.setLoanQuoteVisitId(visitId);
          app.setAppContactCenterLocation(1);
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsOutstanding()))
            quote.setLoanQuoteOutstandingLoanAmount(masterCBSResponse.getCbsOutstanding()); 
          if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
            quote.setLoanQuoteDateOfBirth(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsVisitId()))
            quote.setLoanQuoteVisitId(masterCbsCall.getCbsVisitId()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
            quote.setLoanQuoteGender(masterCBSResponse.getCbsGender()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
            quote.setLoanQuotePreEMIs(masterCBSResponse.getCbsEmi()); 
          if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
            quote.setLoanQuoteDateOfBirthDT(masterCBSResponse.getCbsDateOfBirth()); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsLoanPurpose()))
            quote.setLoanQuoteLoanPurposeId(masterCbsCall.getCbsLoanPurpose()); 
          quote.setLoanQuoteExistingSanctionAmount(Double.valueOf((masterCBSResponse.getCbsSanctionedLimit() != null) ? masterCBSResponse.getCbsSanctionedLimit().doubleValue() : 0.0D));
          if (masterCBSResponse.getCbsSanctionDate() != null)
            quote.setLoanQuoteExistingPLSanctionDT(masterCBSResponse.getCbsSanctionDate()); 
          quote.setLoanQuoteExistingXpressCreditNo(Integer.valueOf((masterCbsCall.getCbsNoOfExistingXpressCredit() != null) ? masterCbsCall.getCbsNoOfExistingXpressCredit().intValue() : -1));
          if (ValidatorUtil.isValid(masterCbsCall.getCbsLoanPurpose()) && masterCbsCall.getCbsLoanPurpose().intValue() == 11) {
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsFirstName()))
              quote.setLoanQuoteFirstName(masterCBSResponse.getCbsFirstName()); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsMiddleName()))
              quote.setLoanQuoteMiddleName(masterCBSResponse.getCbsMiddleName()); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsLastName()))
              quote.setLoanQuoteLastName(masterCBSResponse.getCbsLastName()); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine1())) {
              String address1 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine1());
              quote.setLoanQuoteAddress1(address1);
            } 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine2())) {
              String address2 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine2());
              quote.setLoanQuoteAddress2(address2);
            } 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine3())) {
              String address3 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine3());
              quote.setLoanQuoteAddressLandmark(address3);
            } 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode()) && 
              masterCBSResponse.getCbsPinCode().length() == 6)
              quote.setLoanQuotePincode(Integer.valueOf(Integer.parseInt(masterCBSResponse.getCbsPinCode()))); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsPanNumber()))
              quote.setLoanQuotePanCardNo(masterCBSResponse.getCbsPanNumber()); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
              quote.setLoanQuoteGender(masterCBSResponse.getCbsGender()); 
          } else if (ValidatorUtil.isValid(masterCbsCall.getCbsLoanPurpose()) && masterCbsCall.getCbsLoanPurpose().intValue() == 23) {
            if (masterCbsCall.getCbsTypeOfRelationship() != null && 
              masterCbsCall.getCbsTypeOfRelationship().toString().equals("1") && 
              app.getAppPensionAccountNumber() != null)
              app.setAppPensionAccountNumber(app.getAppPensionAccountNumber()); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode()) && 
              masterCBSResponse.getCbsPinCode().length() == 6)
              quote.setLoanQuotePincode(Integer.valueOf(Integer.parseInt(masterCBSResponse.getCbsPinCode()))); 
            if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode()) && 
              masterCBSResponse.getCbsMobileNo() != null && masterCBSResponse.getCbsMobileNo().length() == 10)
              quote.setAppMobile(masterCBSResponse.getCbsMobileNo()); 
          } else if (ValidatorUtil.isValid(masterCbsCall.getCbsLoanPurpose()) && masterCbsCall.getCbsLoanPurpose().intValue() == 27 && 
            masterCbsCall.getCbsTypeOfRelationship() != null && 
            masterCbsCall.getCbsTypeOfRelationship().toString().equals("1") && 
            app.getAppSalaryAccNo() != null) {
            app.setAppSalaryAccNo(app.getAppSalaryAccNo());
           // logger.info("PersonalLoanProcessImpl.java:: 27 :: " + app.getAppSalaryAccNo());
          } 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsTypeOfRelationship())) {
            quote.setCbsRelationShipId(masterCbsCall.getCbsTypeOfRelationship());
            logger.info("PersonalLoanProcessImpl.java:: 3254:: " + quote.getCbsRelationShipId());
          } 
          
          //save consentId
          Integer personalTypeId = 0;
          if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_PERSONAL)) {
        	  personalTypeId = Constants.PERSONAL_LOAN_ID;
          } else if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_PENSION)) {
        	  personalTypeId = Constants.PERSONAL_LOAN_ID;
          } else if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_GOLD)) {
        	  personalTypeId = Constants.APP_PL_TYPE_GOLD;
          }

          Consent consent = commonService.getConsentByLoanAndCustomerType(personalTypeId, "ETB");
          Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
          quote.setLoanQuoteConsentId(consentId); 
  		
          quote = this.personalLoanService.save(quote);
          if (quote == null) {
            logger.info("PersonalLoanProcessImpl.java LNo: 2989 :: ");
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          }
        try {
          app.setAppQuoteId(quote.getLoanQuoteId());
          app.setAppEmailVerified("N");
          app.setAppMobileVerified("N");
          app.setAppActive("Y");
          app.setAppDeleted("N");
          if (app.getAppEntryTime() == null)
            app.setAppEntryTime(new Date()); 
          if (app.getAppEntryDate() == null)
            app.setAppEntryDate(new Date()); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())) {
            if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCbsCall.getCbsIsdCode().toString())) {
              app.setAppApplyingFrom(1);
            } else {
              app.setAppApplyingFrom(2);
            } 
          } else {
            app.setAppApplyingFrom(1);
          } 
          app.setAppOtpMobileAlertsCount(Integer.valueOf(0));
          app.setAppBankId(Constants.LEAD_BANK_ID);
          app.setAppFulfillmentGroupId(Constants.LEAD_FULFILLMENT_GROUP_ID);
          if (isDsrPage.equalsIgnoreCase("true")) {
           // logger.info("PersonalLoanProcessImpl.java LNo: 3611 :: isDsrPage-LEAD_DATA_SOURCE_ID_DIRECT--" + isDsrPage);
            app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
            logger.info("PersonalLoanProcessImpl.java LNo: 3613 :: isDsrPage---" + app.getAppDataSourceId());
          } else if ("true".equalsIgnoreCase(masterCbsCall.getIsMobileRequest())) {
            //logger.info("PersonalLoanProcessImpl.java LNo: 3616 :: LEAD_DATA_SOURCE_ID_MOBILE_APP---" + masterCbsCall.getIsMobileRequest());
            app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP);
            logger.info("PersonalLoanProcessImpl.java LNo: 3618 :: isDsrPage---" + app.getAppDataSourceId());
          } else {
            logger.info("PersonalLoanProcessImpl.java LNo: 3620 :: isDsrPage--LEAD_DATA_SOURCE_ID_WEB_LEAD-" + isDsrPage);
            app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
            logger.info("PersonalLoanProcessImpl.java LNo: 3622 :: isDsrPage---" + app.getAppDataSourceId());
          } 
          app.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE167_ID));
          app.setAppSubTypeId(Constants.APP_APP_SUB_TYPE_ID_CBS);
          app.setAppLeadUpdateTime(new Date());
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
            app.setAppLoanEmi(masterCBSResponse.getCbsEmi()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmail())) {
            app.setAppWorkEmail(masterCBSResponse.getCbsEmail());
            SessionUtil.setEmail(masterCBSResponse.getCbsEmail());
          } 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsFirstName()))
            app.setAppFirstName(masterCBSResponse.getCbsFirstName()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsMiddleName()))
            app.setAppMiddleName(masterCBSResponse.getCbsMiddleName()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsLastName()))
            app.setAppLastName(masterCBSResponse.getCbsLastName()); 
          if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
            app.setAppDobDT(masterCBSResponse.getCbsDateOfBirth()); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine1())) {
            String address1 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine1());
            app.setAppAddress1(address1);
          } 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine2())) {
            String address2 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine2());
            app.setAppAddress2(address2);
          } 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine3())) {
            String address3 = CbsUtil.removeSomeSpecialCharacter(masterCBSResponse.getCbsAddressLine3());
            app.setAppAddressLandmark(address3);
          } 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode()) && 
            masterCBSResponse.getCbsPinCode().length() == 6)
            app.setAppPincode(Integer.valueOf(Integer.parseInt(masterCBSResponse.getCbsPinCode()))); 
          logger.info("Phone Business ::" + StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()));
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsPhoneBusiness()) && 
            StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()))
            app.setAppOfficePhone(Long.valueOf(Long.parseLong(masterCBSResponse.getCbsPhoneBusiness()))); 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsPanNumber()))
            app.setAppPanCardNo(masterCBSResponse.getCbsPanNumber()); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsResponseId()))
            app.setAppExistingRelationTypeId(Integer.toString(masterCbsCall.getCbsResponseId().intValue())); 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())) {
            app.setAppISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
            //logger.info("setAppISDCode ::" + masterCbsCall.getCbsIsdCode());
            SessionUtil.setISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
            //logger.info("setISDCode ::" + masterCbsCall.getCbsIsdCode());
          } 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
            app.setAppMobileNo(masterCbsCall.getCbsMobileNumber());
            //logger.info("setAppMobileNo ::" + masterCbsCall.getCbsMobileNumber());
            SessionUtil.setMobile(masterCbsCall.getCbsMobileNumber());
            //logger.info("setMobile ::" + masterCbsCall.getCbsMobileNumber());
          } 
          if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
            app.setAppGender(masterCBSResponse.getCbsGender()); 
          if (masterCbsCall.getCbsTypeOfRelationship() != null) {
            if (masterCbsCall.getCbsTypeOfRelationship().toString().equals("3") || Constants.PENSION_LOAN_PURPOSE_ID.equals(masterCbsCall.getCbsLoanPurpose()))
              app.setAppSalaryAccNo(masterCbsCall.getCbsAccountNumber()); 
            if (masterCbsCall.getCbsTypeOfRelationship().toString().equals("1") || Constants.APP_PL_TYPE_GOLD.equals(masterCbsCall.getCbsLoanPurpose()))
              app.setAppSalaryAccNo(masterCbsCall.getCbsAccountNumber()); 
          } 
          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber()))
            app.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(masterCbsCall.getCbsMobileNumber())); 
          if (isAppFoundForDedupInDropRejectStage)
            app.setAppMobileDedup(Integer.valueOf(0)); 
          if (isAppFoundForDedupInDropOffStage)
            app.setAppMobileDedup(Integer.valueOf(1)); 
          if (deviceId != null)
            app.setAppMobileDeviceId(deviceId); 
          if (socialMediaId != null)
            app.setAppSocialMediaId(socialMediaId); 
          app.setAppPanCardLater(Boolean.valueOf(false));
          if (isDsrPage.equalsIgnoreCase("true"))
            if (bankLMSUserId != null) {
              if (this.commonService.isBankUser(bankLMSUserId)) {
               // logger.info("PersonalLoanProcessImpl.java LNo: 3805 :: isDsrPage--bankLMSUserId--" + bankLMSUserId);
                app.setAppAssignedLmsSalesUserId(bankLMSUserId);
               // logger.info("PersonalLoanProcessImpl.java LNo: 3807 :: isDsrPage-getAppAssignedLmsSalesUserId--" + app.getAppAssignedLmsSalesUserId());
              } 
            } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null) {
              bankLMSUserId = SessionUtil.getBankLMSUser().getLmsUserId();
              //logger.info("PersonalLoanProcessImpl.java LNo: 3811 :: isDsrPage--bankLMSUserId--" + bankLMSUserId);
              if (this.commonService.isBankUser(bankLMSUserId)) {
                //logger.info("PersonalLoanProcessImpl.java LNo: 3812 :: isDsrPage--bankLMSUserId--" + bankLMSUserId);
                app.setAppAssignedLmsSalesUserId(bankLMSUserId);
                logger.info("PersonalLoanProcessImpl.java LNo: 3814 :: isDsrPage--getAppAssignedLmsSalesUserId--" + app.getAppAssignedLmsSalesUserId());
              } 
            }  
          if (masterCbsCall.getCbsTypeOfRelationship() != null) {
            app.setAppCbsRelationShipId(masterCbsCall.getCbsTypeOfRelationship());
            logger.info("PersonalLoanProcessImpl.java LNo: 3416 ::" + app.getAppCbsRelationShipId());
          } 
          
          //save consent id in main table
          if (quote.getLoanQuoteConsentId() != null && app.getAppConsentId() == null) {
        	  app.setAppConsentId(quote.getLoanQuoteConsentId());
          }
          
          //logger.info("PersonalProcessManagerImpl.java LNo: 3845 : SAVE PERSONAL9----" + app.toString());
          app = this.personalLoanService.save(app);
          if (app == null) {
            logger.info("PersonalLoanProcessImpl.java LNo: 3091 ::");
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
          logger.info("PersonalLoanProcessImpl.java LNo: 4024 ::BEFORE----" + app.getAppSeqId());
          SessionUtil.setPersonalLoanTypeSequenceId(app.getAppSeqId());
          logger.info("PersonalLoanProcessImpl.java LNo: 4026 ::AFTER-----" + app.getAppSeqId());
        } catch (NullPointerException ne) {
            logger.info("PersonalLoanProcessImpl.java LNo: 3559 :: exception caught " + ne.getMessage());
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          }
        cbsCallResponse.setAppSeqId(app.getAppSeqId());
        masterCBSResponse.setCbsAppSeqId(app.getAppSeqId());
        masterCBSResponse = this.commonService.save(masterCBSResponse);
        if (masterCBSResponse == null) {
          logger.info("PersonalLoanProcessImpl.java LNo: 3107 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        if (needToInsert)
          try {
            this.personalLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), app.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE167, null, true);
          } catch (NullPointerException ne) {
              logger.info("PersonalLoanProcessImpl.java LNo: 3584 :: exception caught " + ne.getMessage());
              cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
              cbsCallResponse.setStatus(Integer.valueOf(0));
              return cbsCallResponse;
            } 
        if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
          masterCbsCall.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSResponse.getCbsEmail()));
          logger.info("PLProcessImpl.java :: LNo :: 4143  :: email otp" + masterCbsCall.getCbsEmailOtpCode());
        } 
        //logger.info("Mobile number " + masterCbsCall.getCbsMobileNumber() + "  mobile otp " + app.getAppMobileVerificationCode());
        masterCbsCall.setCbsOtpCode(app.getAppMobileVerificationCode().toString());
        String msgBody = null;
        if (isDsrPage.equalsIgnoreCase("true")) {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
        } else {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        } 
        msgBody = this.SbiUtil.urlEncode(msgBody);
        String SMS_TEXT = null;
        if (masterCbsCall.getCbsIsdCode() == null) {
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Invalid ISD code");
          return cbsCallResponse;
        } 
        if (masterCbsCall.getCbsIsdCode().intValue() == Integer.parseInt(Constants.COUNTRY_CODE_INDIA)) {
        	SMS_TEXT=Constants.SMS_STRING_INDIAN;
        } else {
        	SMS_TEXT=Constants.SMS_STRING_NRI;
        } 
        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
        SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCbsCall.getCbsOtpCode().toString());
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
        	logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode().toString());
        }
        
        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          logger.info("PersonalProcessManagerImpl LNO :: 3161");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Mobile OTP service is down");
          return cbsCallResponse;
        } 
        if (masterCbsCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
          ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
          msgBody = String.valueOf(String.valueOf(String.valueOf(Constants.FIRST_EMAIL_PART))) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
          if (msgBody != null) {
            msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(String.valueOf(String.valueOf(Constants.PROTOCOL))) + Constants.IP_URL_INTERNET + Constants.BANK_IMAGE_FOLDER + "/");
            msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
            msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
            msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
            msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
            msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
            boolean sendStatus = false;
            String[] emailId = { masterCBSResponse.getCbsEmail() };
            logger.info("PersonalProcessManagerImpl.java LNo: 4006 :sendEmail--> emailId::: " + emailId);
            sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
            logger.info("PersonalProcessManagerImpl.java LNo: 4009 :sendEmail--> sendStatus::: " + sendStatus);
            if (!sendStatus) {
              logger.info("PersonalProcessManagerImpl LNO :: 3187");
              cbsCallResponse.setResponseMsg("Email OTP service is down");
              cbsCallResponse.setStatus(Integer.valueOf(0));
              return cbsCallResponse;
            } 
          } 
        } 
        masterCbsCall.setCbsOtpCount(String.valueOf(1));
        masterCbsCall = this.commonService.save(masterCbsCall);
        if (masterCbsCall == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 3207");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          return cbsCallResponse;
        } 
        if(masterCbsCall.getCbsMobileNumber().length() <= 8)
      	  masterCbsCall.setCbsMobileNumberMask(masterCbsCall.getCbsMobileNumber().replaceAll("\\d(?=\\d{2})", "*"));
        else
      	  masterCbsCall.setCbsMobileNumberMask(masterCbsCall.getCbsMobileNumber().replaceAll("\\d(?=\\d{4})", "*"));
        cbsCallResponse.setResponseMsg("");
        if (isDsrPage.equalsIgnoreCase("true")) {
          cbsCallResponse.setStatus(Integer.valueOf(2));
        } else {
          cbsCallResponse.setStatus(Integer.valueOf(1));
        } 
      } 
    } catch (SQLException ne) {
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        logger.info("PersonalProcessManagerImpl.java LNo: 3670 :: exception caught " + ne.getMessage());
      } catch (JSONException e) {
      cbsCallResponse.setResponseMsg("System error occured.");
      cbsCallResponse.setStatus(Integer.valueOf(0));
      logger.info("PersonalProcessManagerImpl.java LNo: 3675 :: exception caught " + e.getMessage());
      
    }  catch (ParseException e) {
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        logger.info("PersonalProcessManagerImpl.java LNo: 3795 :: exception caught " + e.getMessage());
        
      } 
    return cbsCallResponse;
  }
  
  public JSONObject processCBSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, int appOTPVerifyType, String inputOtp, String userEmail, Integer appSeqId, Integer cbsCallId) throws JSONException {

	  if(inputOtp !=null) {
      	SbiUtil sbiutil=new SbiUtil();
      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
      }
      
	  JSONObject json = new JSONObject();
    
  //  logger.info("PersonalProcessManagerImpl.java CALLING processCBSOTP() moduleId.."+moduleId+"..stateId.."+stateId+"..appSeqId.."+appSeqId+"..inputOtp.."+inputOtp+"..userEmail.."+userEmail+"..ajaxPostUrl.."+ajaxPostUrl);
    
    if (cbsCallId == null) {
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
    ApplicationFormCveLoan cveForm = new ApplicationFormCveLoan();
    Integer appPLTypeId = Constants.APP_PL_TYPE_CVE;
    String cve = "cve";
    
    if (masterCBSCall == null) {
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsMobileOtpVerified() != null && "Y".equalsIgnoreCase(masterCBSCall.getCbsMobileOtpVerified())) {
      json.put("status", "error");
      json.put("message", "Your mobile no. is already verified");
      json.put("alertCount", 5);
      return json;
    } 
    Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
    if (stateId.intValue() == 28) {
      try {
        if (appSeqId != null) {
          ApplicationFormPersonalLoan app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
          if (app == null) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
            app.setAppOTPAttemptCount(Integer.valueOf(0)); 
          if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG);
            return json;
          } 
        } 
        if (alertCount.intValue() >= 5) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          masterCBSCall.setCbsOtpCode(this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
        } else if (appOTPVerifyType == 1) {
          masterCBSCall.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSCall.getCbsEmail()));
        } else {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
          msgBody = this.SbiUtil.urlEncode(msgBody);
          String SMS_TEXT = null;
          if (Constants.COUNTRY_CODE_INDIA.equals(String.valueOf(masterCBSCall.getCbsIsdCode()))) {
        	  SMS_TEXT=Constants.SMS_STRING_INDIAN;
          } else {
        	  SMS_TEXT=Constants.SMS_STRING_NRI;
          } 
          SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
          SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
          SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim());
          
          if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
        	  logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode().toString());
          }
          
          if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
          } 
        } else if (appOTPVerifyType == 1 && 
          masterCBSCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
          ValidatorUtil.isValidEmail(masterCBSCall.getCbsEmail())) {
          String msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
          msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
          msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
          msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
          msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
          msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCBSCall.getCbsEmailOtpCode());
          msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
          boolean sendStatus = false;
          String[] emailId = { masterCBSCall.getCbsEmail() };
          sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
          logger.info("CBS EMAIL msgbody of pl: " + msgBody + " with AppSeqId ::" + appSeqId);
          if (!sendStatus) {
            logger.info("PersonalProcessManagerImpl.java LNo: 3362 :: ");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
          } 
        } 
        alertCount = Integer.valueOf(alertCount.intValue() + 1);
        masterCBSCall.setCbsOtpCount(alertCount.toString());
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("PersonalProcessManagerImpl.java LNo: 3374 :: ");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (NullPointerException ne) {
          logger.info("PersonalProcessManager.java LNO 3811::" + ne.getMessage());
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } catch (SQLException e) {
        logger.info("PersonalProcessManager.java LNO 3816::" + e.getMessage());
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    } else if (stateId.intValue() == 29) {
    	logger.info("PersonalProcessManagerImpl.java INSIDE 29 state--"+stateId.intValue() );
      if (ajaxPostUrl != null && ajaxPostUrl.equalsIgnoreCase(cve) && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD) {
        logger.info("PersonalProcessManagerImpl.java LNo : 4751 INSIDE CVE BLOCK-------");
        logger.info("PersonalProcessManagerImpl.java processCBSOTP : stateId == 29 :: ");
        try {
          if (appSeqId == null) {
            logger.info("PersonalProcessManager.java LNO 3407:: with AppSeqId " + appSeqId);
            logger.info("PersonalProcessManagerImpl.java processCBSOTP : appSeqId == null ");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
            return json;
          } 
          if (!ValidatorUtil.isValid(inputOtp)) {
            logger.info("PersonalProcessManagerImpl.java processCBSOTP : inputOtp :: ");
            json.put("status", "error");
            json.put("message", "Invalid OTP code");
            return json;
          } 
          ApplicationFormPersonalLoan app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
          logger.info("PersonalProcessManagerImpl.java LNo : 4259::app.getAppSeqId:::" + app.getAppSeqId());
          //logger.info("PersonalProcessManagerImpl.java LNo : 4259::app.getAppSeqId:::" + app);
          logger.info("PersonalProcessManagerImpl.java LNo : 4277 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
          if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
            app.setAppOTPAttemptCount(Integer.valueOf(0)); 
          if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
            json.put("status", "error");
            json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
            return json;
          } 
          app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
          logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL10");
          app = this.personalLoanService.save(app);
          if (app == null) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (app.getAppDataSourceId() != null && !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP))
            if (ValidatorUtil.isValidEmail(userEmail)) {
              if (!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString()) && masterCBSCall.getCbsEmail() == null) {
                app.setAppWorkEmail(userEmail);
              } else if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())) {
                app.setAppWorkEmail(userEmail);
                //logger.info("Capture Email From User is ::" + userEmail + " with AppSeqId " + appSeqId);
              } 
              SessionUtil.setEmail(userEmail);
            }  
          boolean isOPTVerified = false;
          if (appOTPVerifyType == 0) {
            logger.info("PersonalProcessManagerImpl.java LNo: 4137 ::appOTPVerifyType----" + appOTPVerifyType);
            if (masterCBSCall.getCbsOtpCode() != null)
              app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode()))); 
            logger.info("Generated OTP  ::" + masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber());
            if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(inputOtp)) {
              app.setAppMobileVerified("Y");
              int otpResType = getOTPResidantVerifiedType(app);
              app.setAppResTypeAtVerified(Integer.valueOf(otpResType));
              app.setAppMobileVerificationCodeReceived("Y");
              isOPTVerified = true;
              json.put("status", "success");
              json.put("message", "OTP authentication successful");
           //   logger.info("PLProcessImpl.java :: LNo :: 1619  :: OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
            } else {
              logger.info("PersoanlProcessManager.java LNO 3455 with AppSeqId " + appSeqId);
              json.put("status", "error");
              json.put("message", "OTP authentication failed");
              app.setAppMobileVerified("N");
              return json;
            } 
            logger.info("PersonalProcessManagerImpl.java processCBSOTP : 5400 :: " + inputOtp);
          } else if (appOTPVerifyType == 1) {
            logger.info("PersonalProcessManagerImpl.java LNo: 4137 ::appOTPVerifyType----" + appOTPVerifyType);
            if (masterCBSCall.getCbsEmailOtpCode() != null)
              app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode()); 
            if (masterCBSCall.getCbsEmailOtpCode() != null && masterCBSCall.getCbsEmailOtpCode().equals(inputOtp)) {
              masterCBSCall.setCbsEmailOtpVarified("Y");
              app.setAppEmailVerified("Y");
              app.setAppMobileVerificationCodeReceived("Y");
              isOPTVerified = true;
              json.put("status", "success");
              json.put("message", "OTP authentication successful");
              logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail() + " with AppSeqId " + appSeqId);
            } else {
              logger.info("PersoanlProcessManager.java LNO 3447:: with AppSeqId " + appSeqId);
              app.setAppEmailVerified("N");
              json.put("status", "error");
              json.put("message", "OTP authentication failed");
              return json;
            } 
            logger.info("PersonalProcessManagerImpl.java processCBSOTP : 5420 :: " + inputOtp);
          } else {
            logger.info("PersoanlProcessManager.java LNO 3492");
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            app.setAppEmailVerified("N");
            return json;
          } 
          app.setAppOTPVerifyType(appOTPVerifyType);
          logger.info("PersonalProcessManagerImpl.java LNo: 4335 ::appOTPVerifyType----" + appOTPVerifyType + "--" + app.getAppOTPVerifyType());
          if (isOPTVerified) {
            StatusRequest statusRequest = new StatusRequest();
            statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
            statusRequest.setHaveLoanOffer(true);
            if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
              statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
            } else {
              statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
            } 
            statusRequest.setState(stateId.intValue());
            statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
            statusRequest.setRsmDecision(0);
            statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
            statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
            statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
            logger.info("PersonalProcessManagerImpl.java LNo: 4984 statusRequest.getApplicationLeadType::" + statusRequest.getApplicationLeadType() + "---" + app.getAppDataSourceId());
            StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
            if (statusManagerResponse.getStatus() != 0) {
              app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
            } else if (app.getAppLoanStatusId().intValue() == 0) {
              logger.info("PersoanlProcessManager.java LNO 3515 with AppSeqId " + appSeqId);
              json.put("status", "error");
              json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
              return json;
            } 
            if (statusManagerResponse.isEligibleToInsertLog())
              this.personalLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
            logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL11");
            app = this.personalLoanService.save(app);
            if (app == null) {
              logger.info("PersoanlProcessManager.java LNO 3525");
              json.put("status", "error");
              json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
              return json;
            } 
          } else {
            logger.info("OTP is authentication failed:: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP is incorrect! Try again.");
            return json;
          } 
          masterCBSCall = this.commonService.save(masterCBSCall);
          String CbsMobileNumber = masterCBSCall.getCbsMobileNumber();
          //logger.info("PersoanlProcessManager.java CVE DATA sent to CRM LNO 5501 appPLTypeId:: " + appPLTypeId + "..ajaxPostUrl.." + ajaxPostUrl + "..CbsMobileNumber.." + CbsMobileNumber);
          logger.info("PersoanlProcessManager.java CVE DATA sent to CRM LNO 5502 and stopping PERSONAL,GOLD,PENSION Ocas leads to CRM in this flow.....");
          logger.info("PersoanlProcessManager.java CVE DATA sent to CRM LNO 5503 PL:: " + Constants.APP_PL_TYPE_PERSONAL + "..PN.." + Constants.APP_PL_TYPE_PENSION + "..GL.." + Constants.APP_PL_TYPE_GOLD);
          if (ajaxPostUrl != null && ajaxPostUrl.equalsIgnoreCase(cve) && masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N") && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD) {
            logger.info("PersoanlProcessManager.java CVE DATA sent to CRM LNO 5506 and stopping PERSONAL,GOLD,PENSION Ocas leads to CRM in this flow.....");
            try {
              cveForm.setCbsAccountNumber(masterCBSCall.getCbsAccountNumber());
              cveForm.setCbsMobileNumber(masterCBSCall.getCbsMobileNumber());
              cveForm.setAppISDCode(masterCBSCall.getCbsIsdCode().toString());
              cveForm.setCveProductCategory(masterCBSCall.getCveProductCategory());
              cveForm.setCveSalutation(masterCBSCall.getCveSalutation());
              cveForm.setCveAppFirstName(masterCBSCall.getCveAppFirstName());
              cveForm.setCveAppMiddleName(masterCBSCall.getCveAppMiddleName());
              cveForm.setCveAppLastName(masterCBSCall.getCveAppLastName());
              if (masterCBSCall.getCbsEmail() != null) {
                cveForm.setCveAppEmail(masterCBSCall.getCbsEmail());
              } else {
                cveForm.setCveAppEmail(userEmail);
              } 
              cveForm.setCveAppConsentRevokeYes(masterCBSCall.getCveAppConsentRevokeYes());
              cveForm.setAppReferenceId(appRefKey);
              logger.info("PersonalProcessManagerImpl LNO :: 4629 CVETABLE SAVE getAppReferenceId::::" + cveForm.getAppReferenceId() + "--appRefKey--" + this.appRefKey);
              if (cveForm.getCreatedDate() == null)
                cveForm.setCreatedDate(new Date()); 
              cveForm.setAppLeadUpdateTime(new Date());
              cveForm.setCveAppPrevBranchId(masterCBSCall.getLoanQuoteWorkBranchId());
//              logger.info("PersonalProcessManagerImpl.java  4644 @@@@@@ cveForm.getPreferredBranch:: " + cveForm.getCveAppPrevBranchId());
//              logger.info("PersonalProcessManagerImpl.java  4632 @@@@@@ cveForm.getPreferredBranch:: " + cveForm.getCveAppPrevBranchId());
//              logger.info("PersonalProcessManagerImpl LNO :: 4636 CVETABLE SAVE getCbsAccountNumber :: " + cveForm.getCbsAccountNumber());
//              logger.info("PersonalProcessManagerImpl LNO :: 4637 CVETABLE SAVE getCbsMobileNumber :: " + cveForm.getCbsMobileNumber());
//              logger.info("PersonalProcessManagerImpl LNO :: 4637 CVETABLE SAVE getAppISDCode :: " + cveForm.getAppISDCode());
//              logger.info("PersonalProcessManagerImpl LNO :: 4638 CVETABLE SAVE getCveAppFirstName :: " + cveForm.getCveAppFirstName());
//              logger.info("PersonalProcessManagerImpl LNO :: 4639 CVETABLE SAVE getCveAppMiddleName :: " + cveForm.getCveAppMiddleName());
//              logger.info("PersonalProcessManagerImpl LNO :: 4640 CVETABLE SAVE getCveAppEmail :: " + cveForm.getCveAppEmail());
//              logger.info("PersonalProcessManagerImpl LNO :: 4641 CVETABLE SAVE getCveProductCategory :: " + cveForm.getCveProductCategory());
//              logger.info("PersonalProcessManagerImpl LNO :: 4642 CVETABLE SAVE getCveAppPrevBranchId :: " + cveForm.getCveAppPrevBranchId());
//              logger.info("PersonalProcessManagerImpl LNO :: 4644 CVETABLE SAVE getCveAppConsentRevokeYes :: " + cveForm.getCveAppConsentRevokeYes());
//              logger.info("PersonalProcessManagerImpl LNO :: 4645 CVETABLE SAVE getAppReferenceId :: " + cveForm.getAppReferenceId());
//              logger.info("PersonalProcessManagerImpl LNO :: 4646 CVETABLE SAVE appRefKey :: " + appRefKey);
              //logger.info("PersonalProcessManagerImpl LNO :: 4647 CVETABLE SAVE appRefKey :: " + appRefKey);
              //logger.info("PersonalLoanProcessImpl.java LNo: 465555555 :: cveForm::::" + cveForm);
              if (app != null && 
                !Constants.CRM_BYPASS)
                if (app.getAppCRMLeadId() != null) {
                  CRMRequest crmRequest = new CRMRequest();
                  crmRequest.setCrmLeadId(app.getAppCRMLeadId());
                  crmRequest.setApplicantReferenceId(cveForm.getAppReferenceId());
                  logger.info("PersonalLoanProcessImpl.java LNo:4419 ::" + crmRequest.getApplicantReferenceId());
                  crmRequest.setReferenceNumber(app.getAppSeqId());
                  crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
                  logger.info("PersonalProcessManagerImpl.java LNo: 485 : DO NOT CALLING callCrmUpdate Service...............");
                  logger.info("PersonalLoanProcessImpl.java LNo:4301 :: getAppCRMLeadId NULL getAppCRMLeadId " + crmRequest.getCrmLeadId());
                  logger.info("PersonalLoanProcessImpl.java LNo:4302 :: getAppCRMLeadId NULL getAppReferenceId " + crmRequest.getApplicantReferenceId());
                  logger.info("PersonalLoanProcessImpl.java LNo:4303 :: getAppCRMLeadId NULL getAppSeqId " + crmRequest.getReferenceNumber());
                } else {
                  logger.info("PersonalLoanProcessImpl.java LNo:3335 :: BEFORE callCrmCve ");
                  CRMRequest crmRequest = new CRMRequest();
                  crmRequest.setCrmLeadId(app.getAppCRMLeadId());
                  crmRequest.setApplicantReferenceId(appRefKey);
                  crmRequest.setReferenceNumber(app.getAppSeqId());
                  crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
                  logger.info("PersonalLoanProcessImpl.java LNo:4313 :: getAppCRMLeadId NOT-NULL getAppCRMLeadId " + crmRequest.getCrmLeadId());
                  logger.info("PersonalLoanProcessImpl.java LNo:4314 :: getAppCRMLeadId NOT-NULL getAppReferenceId " + appRefKey);
                  logger.info("PersonalLoanProcessImpl.java LNo:4315 :: getAppCRMLeadId NOT-NULL getAppSeqId " + crmRequest.getReferenceNumber());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4441 :: " + masterCBSCall.getCbsAccountNumber());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4442 :: " + masterCBSCall.getCbsMobileNumber());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4443 :: " + masterCBSCall.getCveProductCategory());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4444 :: " + masterCBSCall.getCveSalutation());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4445 :: " + masterCBSCall.getCveAppFirstName());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4446 :: " + masterCBSCall.getCveAppMiddleName());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4448 :: " + masterCBSCall.getCveAppLastName());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4450 :: " + masterCBSCall.getCbsEmail());
//                  logger.info("PersonalLoanProcessImpl.java LNo:4451 :: " + masterCBSCall.getLoanQuoteWorkBranchId());
//                  logger.info("PersonalLoanProcessImpl.java Calling processCbsCall() " + masterCBSCall.toString());
                    if (masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N")) {
                    logger.info("PersonalLoanProcessImpl.java LNo:5079 :: BEFORE AFTER getAppCRMLeadId " + app.getAppCRMLeadId());
                    logger.info("PersonalLoanProcessImpl.java LNo:5080 :: BEFORE AFTER getAppReferenceId " + app.getAppReferenceId());
                    logger.info("PersonalLoanProcessImpl.java LNo:5081 :: BEFORE AFTER getAppSeqId " + app.getAppSeqId());
                    logger.info("PersonalLoanProcessImpl.java LNo:5083 :: getCveAppConsentRevoke NO FLAG and OCAS Case create in CRM Service ");
                    logger.info("PersonalLoanProcessImpl.java :: BEFORE crmResponse for CASE creation is SUCCESS then calling Lead CREATION API for CVE consent");
                    callCrmCve(app, masterCBSCall, cveForm);
                    logger.info("PersonalLoanProcessImpl.java :: AFTER crmResponse for CASE creation is SUCCESS then calling Lead CREATION API for CVE consent");
                    if (masterCBSCall.getCbsMobileNumber() != null && CbsMobileNumber.equalsIgnoreCase(masterCBSCall.getCbsMobileNumber())) {
                  //    logger.info("PersonalProcessManagerImpl.java LNO ::5525 INSIDE MSG for CVE SMS_CONSENT " + CbsMobileNumber + "..getCbsMobileNumber.." + masterCBSCall.getCbsMobileNumber());
                      logger.info("PersonalProcessManagerImpl.java LNO ::5526 INSIDE MSG for CVE SMS_CONSENT " + Constants.MESSAGE_TYPE_SMS);
                      String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
                      msgBody = this.SbiUtil.urlEncode(msgBody);
                      String SMS_TEXT = null;
                      SMS_TEXT=Constants.SMS_STRING_INDIAN;
                      SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
                      SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
                      SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", masterCBSCall.getCbsMobileNumber().trim());

                      if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
                    	  logger.info("OTP for Mobile Number: " + masterCBSCall.getCbsMobileNumber().trim() + " is " + masterCBSCall.getCbsOtpCode().toString());
                      }
                        
                      if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                        logger.info("PersonalProcessManagerImpl.java LNO ::5539 , OTP service is down:: msg not send");
                        json.put("status", "error");
                        json.put("message", "sms service is down");
                        return json;
                      } 
                    } 
                  } 
                }  
            } catch (NullPointerException ne) {
                logger.info("PersonalLoanProcessImpl.java LNo:4070 :: exceptionwhile crm " + ne.getMessage());
              } 
          } else if (ajaxPostUrl != null && ajaxPostUrl.equalsIgnoreCase(cve) && masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("Y") && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD) {
            if (masterCBSCall.getCbsMobileNumber() != null && CbsMobileNumber.equalsIgnoreCase(masterCBSCall.getCbsMobileNumber())) {
              //logger.info("PersonalProcessManagerImpl.java LNO ::5551 INSIDE MSG for Duplicacy CVE SMS_CONSENT " + CbsMobileNumber + "..getCbsMobileNumber.." + masterCBSCall.getCbsMobileNumber());
              //logger.info("PersonalProcessManagerImpl.java LNO ::5552 INSIDE MSG for Duplicacy CVE SMS_CONSENT " + Constants.MESSAGE_TYPE_SMS);
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(24), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              String SMS_TEXT = null;
              SMS_TEXT = Constants.SMS_STRING_INDIAN;
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", masterCBSCall.getCbsMobileNumber().trim());
              logger.info("PersonalProcessManagerImpl.java LNO ::4297 :: Duplicacy message has been sent to user " + SMS_TEXT);
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                logger.info("PersonalProcessManagerImpl.java LNO ::5565 , OTP service is down:: msg not send");
                json.put("status", "error");
                json.put("message", "sms service is down");
                return json;
              } 
            } 
          } 
          return json;
        } catch (NullPointerException ne) {
            logger.info("PersonalProcessManager.java LNO 4096::" + ne.getMessage());
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
         } catch (JSONException e) {
          logger.info("PersonalProcessManager.java LNO 4101::" + e.getMessage());
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } catch (SQLException e) {
          logger.info("PersonalProcessManager.java LNO 4220::" + e.getMessage());
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
          } 
      } 
      else {
      logger.info("PersonalProcessManagerImpl.java LNo : 4751 EXCEPT CVE BLOCK-------");
      try {
        logger.info("PersonalProcessManagerImpl.java processCBSOTP : stateId == 29 :: ");
        if (appSeqId == null) {
          logger.info("PersonalProcessManager.java LNO 3407:: with AppSeqId " + appSeqId);
          logger.info("PersonalProcessManagerImpl.java processCBSOTP : appSeqId == null ");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return json;
        } 
        if (!ValidatorUtil.isValid(inputOtp)) {
          logger.info("PersonalProcessManagerImpl.java processCBSOTP : inputOtp :: ");
          json.put("status", "success");
          json.put("message", "Invalid OTP code");
          return json;
        } 
        ApplicationFormPersonalLoan app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("PersonalProcessManager.java LNO 3416:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        logger.info("PersonalProcessManagerImpl.java LNo : 4277 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        app = this.personalLoanService.save(app);
        if (app == null) {
          logger.info("PersonalProcessManager.java LNO 4286 error on saving:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        if (app.getAppDataSourceId() != null && !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP) && 
          ValidatorUtil.isValidEmail(userEmail)) {
          if (!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString()) && masterCBSCall.getCbsEmail() == null) {
            app.setAppWorkEmail(userEmail);
          } else if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())) {
            app.setAppWorkEmail(userEmail);
            //logger.info("Capture Email From User is ::" + userEmail + " with AppSeqId " + appSeqId);
          } 
          SessionUtil.setEmail(userEmail);
        } 
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (masterCBSCall.getCbsOtpCode() != null)
            app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode()))); 
        //  logger.info("Generated OTP  ::" + masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber());
          if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(inputOtp)) {
            app.setAppMobileVerified("Y");
            int otpResType = getOTPResidantVerifiedType(app);
            app.setAppResTypeAtVerified(Integer.valueOf(otpResType));
            app.setAppMobileVerificationCodeReceived("Y");
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            //logger.info("PLProcessImpl.java :: LNo :: 1619  :: OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("PersoanlProcessManager.java LNO 3455 with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            app.setAppMobileVerified("N");
            return json;
          } 
        } else if (appOTPVerifyType == 1) {
          if (masterCBSCall.getCbsEmailOtpCode() != null)
            app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode()); 
          if (masterCBSCall.getCbsEmailOtpCode() != null && masterCBSCall.getCbsEmailOtpCode().equals(inputOtp)) {
            masterCBSCall.setCbsEmailOtpVarified("Y");
            app.setAppEmailVerified("Y");
            app.setAppMobileVerificationCodeReceived("Y");
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("PersoanlProcessManager.java LNO 3447:: with AppSeqId " + appSeqId);
            app.setAppEmailVerified("N");
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("PersoanlProcessManager.java LNO 3492");
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          app.setAppEmailVerified("N");
          return json;
        } 
        app.setAppOTPVerifyType(appOTPVerifyType);
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } else {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } 
          statusRequest.setState(stateId.intValue());
          statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
          statusRequest.setRsmDecision(0);
          statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
          statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
          statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
          StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
          if (statusManagerResponse.getStatus() != 0) {
            app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
          } else if (app.getAppLoanStatusId().intValue() == 0) {
            logger.info("PersoanlProcessManager.java LNO 3515 with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.personalLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.personalLoanService.save(app);
          if (app == null) {
            logger.info("PersoanlProcessManager.java LNO 3525");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
        } else {
          logger.info("OTP is authentication failed:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("PersoanlProcessManager.java LNO 3539 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException se) {
          logger.info("PersonalProcessManager.java LNO 4253::" + se.getMessage());
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
       }  
    } 
  }
    return json;
  }
  
  public RSMResponse getFinalRSMScore(RSMResponse rsmResponse, ApplicationFormPersonalLoan app, ApplicationFormPersonalLoanQuote quote) {
    float rsmScoreFinal;
    if (rsmResponse == null || app == null || quote == null)
      return rsmResponse; 
    logger.info("PLProcessImpl.java :: LNo :: 5148 rsmResponse form rule engine :: " + rsmResponse.toString());
    float rsmScoreRuleEngine = 0.0F;
    RsmData rsmData = new RsmData();
    if (quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
      rsmData.setEmploymentScoreVal(6);
      rsmData.setEmploymentNatureScoreVal(10);
    } else if (quote.getLoanQuoteEmploymentType() != null) {
      rsmData.setEmploymentId(quote.getLoanQuoteEmploymentType().intValue());
    } 
    if (quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID) && app.getAppResidenceTypeId() != null) {
      rsmData.setEmploymentNatureId(10);
    } else if (quote.getLoanQuoteEmploymentNature() != null) {
      rsmData.setEmploymentNatureId(quote.getLoanQuoteEmploymentNature().intValue());
    } 
    if (app.getAppResidenceTypeId() != null)
      rsmData.setResidentTypeId(app.getAppResidenceTypeId().intValue()); 
    boolean flag = false;
    if (app.getAppCibilScore() != null && 
      app.getAppCibilScore().intValue() != 0 && app.getAppCibilScore().intValue() != -1) {
      rsmScoreRuleEngine = (float)(rsmScoreRuleEngine * 0.8D);
      flag = true;
      rsmData.setCibilScore(app.getAppCibilScore().intValue());
    } 
    //logger.info("PLProcessImpl.java :: LNo :: 5203 rsmResponse before calling getRSMData :: " + rsmData.toString());
    rsmData = this.commonService.getRSMData(rsmData, app.getAppPersonalLoanId());
    logger.info("PLProcessImpl.java :: LNo :: 5206 rsmResponse after calling getRSMData  :: " + rsmData.toString());
    if (flag) {
      rsmScoreFinal = (float)((rsmResponse.getRsmScore() + rsmData.getEmploymentScoreVal() + rsmData.getEmploymentNatureScoreVal() + 
        rsmData.getResidentTypeScoreVal()) * 0.8D + rsmData.getCibilScoreVal());
    } else {
      rsmScoreFinal = rsmResponse.getRsmScore() + rsmData.getEmploymentScoreVal() + rsmData.getEmploymentNatureScoreVal() + 
        rsmData.getResidentTypeScoreVal();
    } 
    rsmResponse.setRsmScore(rsmScoreFinal);
    logger.info("PLProcessImpl.java :: LNo :: 5216 rsmResponse before calling getRSMDecision :: rsmScoreFinal " + rsmScoreFinal);
    int rsmDecision = this.commonService.getRSMDecision((int)rsmScoreFinal, app.getAppPersonalLoanId().intValue(), Constants.PERSONAL_LOAN_ID.intValue());
    logger.info("PLProcessImpl.java :: LNo :: 5218 rsmResponse after calling getRSMDecision :: rsmDecision " + rsmDecision);
    rsmResponse.setRsmDecision(rsmDecision);
    logger.info("PLProcessImpl.java :: LNo :: 5220 rsmResponse after implementation some rule :: " + rsmResponse.toString());
    return rsmResponse;
  }
  
  public JSONObject verifySMSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException {
    JSONObject json = new JSONObject();
    int appOTPVerifyType = 0;
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getPlTypeCbsCallId() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 5239");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getPlTypeCbsCallId());
    if (masterCBSCall == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 5249");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsOtpCount() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 5257");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsIsdCode() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 5268");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsMobileNumber() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 5277");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
    if (stateId.intValue() == 33)
      try {
        Integer appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
        ApplicationFormPersonalLoan app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 5293:: after save error with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG);
          return json;
        } 
        if (alertCount.intValue() >= 5) {
          logger.info("PersonalProcessManagerImpl LNO :: 5290 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          masterCBSCall.setCbsOtpCode(this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
          logger.info("PersonalProcessManagerImpl LNO :: 5301 ::  mobile number ::" + masterCBSCall.getCbsMobileNumber() + " with AppSeqId " + appSeqId);
          logger.info("PersonalProcessManagerImpl LNO :: 5302 :: mobile OTP ::" + masterCBSCall.getCbsOtpCode() + " with AppSeqId " + appSeqId);
        } else {
          logger.info("PersonalProcessManagerImpl LNO :: 5304");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        boolean sendSMSConsent = false;
        if (isDsrPage.equalsIgnoreCase("true")) {
          boolean bankLmsUserRoleExceptContactCenter = this.commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
          logger.info("PersonalLoanProcessImpl.java LNo: 5215 :: isDsrPage--bankLMSUserId--" + bankLmsUserRoleExceptContactCenter + "--bankLMSUserId--" + bankLMSUserId);
          if (bankLmsUserRoleExceptContactCenter) {
            logger.info("PersonalProcessManagerImpl LNO :: 5321 :: Not contact Center user with AppSeqId " + appSeqId);
            sendSMSConsent = true;
          } else {
            logger.info("PersonalProcessManagerImpl LNO :: 5327 ::  contact Center user with AppSeqId " + appSeqId);
          } 
        } 
        String msgBody = null;
        if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent) {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
        } else {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        } 
        msgBody = this.SbiUtil.urlEncode(msgBody);
        String SMS_TEXT = null;
        if (Constants.COUNTRY_CODE_INDIA.equals(masterCBSCall.getCbsIsdCode().toString())) {
        	SMS_TEXT=Constants.SMS_STRING_INDIAN;
        } else {
        	SMS_TEXT=Constants.SMS_STRING_NRI;
        } 
        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
        SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
        	logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode().toString());
        }

        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        alertCount = Integer.valueOf(alertCount.intValue() + 1);
        masterCBSCall.setCbsOtpCount(alertCount.toString());
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 5365 :: Sorry for the inconvenience with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        if (app != null) {
          app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode())));
          app.setAppMobileVerified("N");
        } 
        app = this.personalLoanService.save(app);
        if (app == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 5378 ::  Sorry for the inconvenience with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (SQLException ne) {
          logger.info("PersonalProcessManagerImpl.java LNO 4577 ::" + ne.getMessage());
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       }   
    if (stateId.intValue() == 34)
      try {
        Integer appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
        if (appSeqId == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 5429:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        ApplicationFormPersonalLoan app = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 5438:: exception caught with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        logger.info("PersonalProcessManager.java LNo : 5443 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        logger.info("PersonalProcessManagerImpl.java LNo: 483 : SAVE PERSONAL12");
        app = this.personalLoanService.save(app);
        if (app == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 5455:: after save error with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (app.getAppMobileVerified().equals("Y")) {
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            logger.info("OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("PersonalProcessManagerImpl.java LNO 5460::");
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("PersonalProcessManagerImpl.java LNO 5467:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } else {
            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          } 
          statusRequest.setState(stateId.intValue());
          statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
          statusRequest.setRsmDecision(0);
          statusRequest.setApplicationType(SessionUtil.getApplicationType().intValue());
          statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
          statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
          statusRequest.setAppMobileVerified(app.getAppMobileVerified());
          StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
          if (statusManagerResponse.getStatus() != 0) {
            app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
          } else if (app.getAppLoanStatusId().intValue() == 0) {
            logger.info("PersonalProcessManagerImpl.java LNO 5493:: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.personalLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.personalLoanService.save(app);
          if (app == null) {
            logger.info("PersonalProcessManagerImpl.java LNO 5503 :: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
        } else {
          logger.info("PersonalProcessManagerImpl.java LNO 5509  :: OTP is authentication failed:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 5517::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException ne) {
          logger.info("PersonalProcessManagerImpl.java LNO 4688::" + ne.getMessage());
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       } 
    return json;
  }
  
  private void callCrmCve(ApplicationFormPersonalLoan appFormData, MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
    try {
      CRMRequest crmRequest = prepareCrmRequestCve(appFormData, masterCBSCall, cveForm);
      logger.info("PersonalProcessManagerImpl.java :: LNO 5573 :: Beofoe preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
      this.crmServiceNew.pushLeadToCRMcve(crmRequest);
    } catch (NullPointerException ne) {
        logger.info("PersonalProcessManagerImpl.java  LNO 4829:: Exception occured" + ne.getMessage());
     }
  }
  
  private CRMRequest prepareCrmRequestCve(ApplicationFormPersonalLoan appFormData, MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
    CRMRequest crmRequest = new CRMRequest();
    //logger.info("PersonalProcessManagerImpl.java :: LNO 5866 :: prepareCrmRequestCve::cveForm::::" + cveForm);
    if (cveForm == null) {
      logger.info("PersonalProcessManagerImpl.java :: LNO 5587 :: cveForm is null, returning back");
      return null;
    } 
    masterCBSCall.getCbsAccountNumber();
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCbsAccountNumber():: " + masterCBSCall.getCbsAccountNumber());
    crmRequest.setMobileNumber(masterCBSCall.getCbsMobileNumber());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCbsMobileNumber::" + masterCBSCall.getCbsMobileNumber());
    crmRequest.setFirstName(masterCBSCall.getCveAppFirstName());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveAppFirstName()::" + masterCBSCall.getCveAppFirstName());
    crmRequest.setMiddleName(masterCBSCall.getCveAppMiddleName());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveAppMiddleName::" + masterCBSCall.getCveAppMiddleName());
    crmRequest.setLastName(masterCBSCall.getCveAppLastName());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveAppLastName::" + masterCBSCall.getCveAppLastName());
    //logger.info("PersonalLoanProcessImpl.JAVA crmRequest cveForm.getCveAppPrevBranchId::" + cveForm.getCveAppPrevBranchId());
    MasterBranch masterBranch = this.commonService.getBranchById(cveForm.getCveAppPrevBranchId());
    if (masterBranch != null && masterBranch.getBranchCode() != null) {
      int branchCodeLength = masterBranch.getBranchCode().length();
      String branchCode = "";
      if (branchCodeLength > 0 && branchCodeLength == 1) {
        branchCode = "0000" + masterBranch.getBranchCode();
      } else if (branchCodeLength == 2) {
        branchCode = "000" + masterBranch.getBranchCode();
      } else if (branchCodeLength == 3) {
        branchCode = "00" + masterBranch.getBranchCode();
      } else if (branchCodeLength == 4) {
        branchCode = "0" + masterBranch.getBranchCode();
      } else if (branchCodeLength > 4) {
        branchCode = masterBranch.getBranchCode();
      } 
      crmRequest.setPreferredBranch(branchCode);
      logger.info(
          "PersonalLoanProcessImpl.java LNO :: 6466 list branchCode " + branchCode);
    } 
    if (cveForm.getCveAppEmail() != null)
      crmRequest.setEmailId(cveForm.getCveAppEmail()); 
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCbsEmail::" + cveForm.getCveAppEmail() + "--CRM-EMAIL--" + crmRequest.getEmailId());
    crmRequest.setProductCategory(masterCBSCall.getCveProductCategory());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveProductCategory::" + masterCBSCall.getCveProductCategory());
    crmRequest.setCveSalutation(masterCBSCall.getCveSalutation());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveSalutation::" + masterCBSCall.getCveSalutation());
    crmRequest.setCveAppConsentRevoke(masterCBSCall.getCveAppConsentRevokeYes());
    //logger.info("PersonalLoanProcessImpl.JAVA masterCBSCall.getCveAppConsentRevokeYes::" + masterCBSCall.getCveAppConsentRevokeYes());
    crmRequest.setApplicantReferenceId(cveForm.getAppReferenceId());
    //logger.info("PersonalLoanProcessImpl.JAVA getAppReferenceId::crmRequest " + crmRequest.getApplicantReferenceId());
    crmRequest.setReferenceNumber(appFormData.getAppSeqId());
    //logger.info("PersonalLoanProcessImpl.JAVA getAppSeqId::getAppSeqId::::" + appFormData.getAppSeqId());
    //logger.info("PersonalProcessManagerImpl.java crmRequest.getCveCifNumber:: " + masterCBSCall.getCveCifNumber());
    if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() > 11) {
      crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber().substring(6));
      //logger.info("PersonalProcessManagerImpl.java IF CASE getCIFNumber1:: " + masterCBSCall.getCveCifNumber());
      //logger.info("PersonalProcessManagerImpl.java IF CASE getCIFNumber2:: " + crmRequest.getCIFNumber());
    } else if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() == 11) {
      crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber());
      //logger.info("PersonalProcessManagerImpl.java ELSE CASE getCIFNumber:: " + crmRequest.getCIFNumber());
    } 
    return crmRequest;
  }
  
  private void callCrm(ApplicationFormPersonalLoan appFormData, ApplicationFormPersonalLoanQuote quote) {
    try {
      logger.info("PersonalProcessManagerImpl.java :: LNO 5677 :: Before preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
      CRMRequest crmRequest = prepareCrmRequest(appFormData, quote);
      logger.info("PersonalProcessManagerImpl.java :: LNO 5679 :: Beofore preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
      this.crmServiceNew.crmLeadCreation(crmRequest, appFormData);
    } catch (NullPointerException ne) {
        logger.info("PersonalProcessManagerImpl.java  LNO 4904:: Exception occured" + ne.getMessage());
     }
  }
  
  private CRMRequest prepareCrmRequest(ApplicationFormPersonalLoan appFormData, ApplicationFormPersonalLoanQuote quote) {
    this.maxAmt = this.personalLoanHelper.getMaxAmount();
    logger.info("PersonalProcessManagerImpl prepareCrmRequest ( ) maxAmt::::::" + this.maxAmt);
    CRMRequest crmRequest = new CRMRequest();
    try {
      if (appFormData == null) {
        logger.info("PersonalProcessManagerImpl.java :: LNO 5690 :: appFormData is null, returning back");
        return null;
      } 
      if (appFormData.getAppFirstName() != null) {
        crmRequest.setFirstName(appFormData.getAppFirstName());
      } else {
        crmRequest.setFirstName("");
      } 
      if (appFormData.getAppMiddleName() != null) {
        crmRequest.setMiddleName(appFormData.getAppMiddleName());
      } else {
        crmRequest.setMiddleName("");
      } 
      if (appFormData.getAppLastName() != null) {
        crmRequest.setLastName(appFormData.getAppLastName());
      } else {
        crmRequest.setLastName("");
      } 
      String gender = "";
      if (appFormData.getAppGender() != null)
        if (appFormData.getAppGender().equals("M")) {
          gender = "1";
        } else if (appFormData.getAppGender().equals("F")) {
          gender = "2";
        } else if (appFormData.getAppGender().equals("O")) {
          gender = "3";
        }  
      crmRequest.setGender(gender);
      if (appFormData.getAppDobDT() != null) {
        crmRequest.setDateOfBirth(DateUtil.getDateTimeForCRMInString(appFormData.getAppDobDT()));
      } else {
        crmRequest.setDateOfBirth("");
      } 
      if (appFormData.getAppCityId() != null && appFormData.getAppCityId().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
        MasterCity city = this.commonService.getCityById(appFormData.getAppCityId());
        if (city != null) {
          crmRequest.setCity(city.getCityName());
        } else {
          crmRequest.setCity("");
        } 
        crmRequest.setDistrict("");
      } else {
        crmRequest.setCity("");
        if (appFormData.getAppDistrictId() != null) {
          MasterDistrict district = this.commonService.getDistrictById(appFormData.getAppDistrictId());
          if (district != null)
            crmRequest.setDistrict(district.getDistrictName()); 
        } else {
          crmRequest.setDistrict("");
        } 
      } 
      MasterState state = this.commonService.getStateById(appFormData.getAppStateId());
      if (state != null && state.getStateName() != null) {
        crmRequest.setState(state.getStateName());
      } else {
        crmRequest.setState("");
      } 
      crmRequest.setAadharNumber("");
      if (appFormData.getAppPanCardNo() != null && ValidatorUtil.isValidPanNo(appFormData.getAppPanCardNo())) {
        crmRequest.setPanNumber(appFormData.getAppPanCardNo());
      } else {
        crmRequest.setPanNumber("");
      } 
      if (appFormData.getAppMobileNo() != null)
        crmRequest.setMobileNumber(appFormData.getAppMobileNo()); 
      crmRequest.setPhoneNumber("");
      if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail())) {
        crmRequest.setEmailId(appFormData.getAppWorkEmail());
      } else {
        crmRequest.setEmailId("");
      } 
      crmRequest.setStatusCode(Integer.valueOf(100002));
      crmRequest.setLeadPriority(Integer.valueOf(2));
      crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
      MasterLoanPurpose loanPurpose = this.commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
      crmRequest.setLoanPurpose(loanPurpose.getLpTypeValue());
      crmRequest.setResidentType("1");
      if (appFormData.getAppBranchId() != null) {
        MasterBranch masterBranch = this.commonService.getBranchById(appFormData.getAppBranchId());
        if (masterBranch != null && masterBranch.getBranchCode() != null) {
          int branchCodeLength = masterBranch.getBranchCode().length();
          String branchCode = "";
          if (branchCodeLength > 0 && branchCodeLength == 1) {
            branchCode = "0000" + masterBranch.getBranchCode();
          } else if (branchCodeLength == 2) {
            branchCode = "000" + masterBranch.getBranchCode();
          } else if (branchCodeLength == 3) {
            branchCode = "00" + masterBranch.getBranchCode();
          } else if (branchCodeLength == 4) {
            branchCode = "0" + masterBranch.getBranchCode();
          } else if (branchCodeLength > 4) {
            branchCode = masterBranch.getBranchCode();
          } 
          crmRequest.setPreferredBranch(branchCode);
        } 
      } 
      if (appFormData.getAppAddress1() != null) {
        crmRequest.setAddressLine1(appFormData.getAppAddress1());
      } else {
        crmRequest.setAddressLine1("");
      } 
      if (appFormData.getAppAddress2() != null) {
        crmRequest.setAddressLine2(appFormData.getAppAddress2());
      } else {
        crmRequest.setAddressLine2("");
      } 
      if (appFormData.getAppAddressLandmark() != null) {
        crmRequest.setAddressLine3(appFormData.getAppAddressLandmark());
      } else {
        crmRequest.setAddressLine3("");
      } 
      if (appFormData.getAppOtherIdNumber() != null && appFormData.getAppOtherId().intValue() > 0) {
        MasterDocumentType masterDocumentType = this.commonService.getDocumentType(appFormData.getAppOtherId().intValue());
        if (masterDocumentType != null && masterDocumentType.getDocumentTypeCrmId() != null) {
          crmRequest.setIdentificationProof(masterDocumentType.getDocumentTypeCrmId());
          crmRequest.setIdentificationNumber(appFormData.getAppOtherIdNumber());
        } else if (masterDocumentType != null && masterDocumentType.getDocumentTypeId() != null && masterDocumentType.getDocumentTypeId().intValue() == 160) {
          crmRequest.setIdentificationProof("");
          crmRequest.setIdentificationNumber("");
        } 
      } else {
        crmRequest.setIdentificationProof("");
        crmRequest.setIdentificationNumber("");
      } 
      crmRequest.setReferenceNumber(appFormData.getAppSeqId());
      crmRequest.setLeadSource(appFormData.getAppDataSourceId());
      crmRequest.setDescription("");
      if(appFormData.getAppLoanAmount() != null)
    	  crmRequest.setLeadAmount(this.SbiUtil.getFinalLoanAmount(appFormData.getAppLoanAmount()));
      else
    	  crmRequest.setLeadAmount(0L);
      
      MasterPlProduct product = null;
      if(appFormData.getAppPersonalLoanId() != null)
    	product = this.commonService.getPersonalLoanProductById(appFormData.getAppPersonalLoanId());
      
      if (product != null && product.getPlProductId() != null) {
    	  crmRequest.setProductCode(product.getPlProductCrmCode());
    	  crmRequest.setProductKey(String.valueOf(product.getPlProductId()));
    	  crmRequest.setProductCategory(product.getPlProductCategory());
      }
      
      
      logger.info("PLProcessImpl.java :: LNo :: 6846 ::after product code::::::");
//      logger.info("PLProcessImpl.java :: LNo :: 6848 ::@@@@@@@@::::::");
      crmRequest.setCIFNumber("");
      if (appFormData.getAppExistingRelationTypeId() != null) {
        MasterCBSResponse masterCBSResponse = personalLoanService.getMasterCBSResponseObjectByCbsAppSeqId(appFormData.getAppSeqId());
        if (masterCBSResponse != null)
          if (masterCBSResponse.getCbsCifNumber() != null && 
            masterCBSResponse.getCbsCifNumber().length() > 11) {
            crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber().substring(6));
          } else if (masterCBSResponse.getCbsCifNumber() != null && 
            masterCBSResponse.getCbsCifNumber().length() == 11) {
            crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber());
          }  
      } 
//      logger.info("PLProcessImpl.java :: LNo :: 6859 ::#########::::::");
      crmRequest.setDuplicate(true);
      crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
      //logger.info("PLProcessImpl.java :: LNo :: 6863 ::$$$$$$$$::::::");
      if (quote.getLoanQuoteResidentTypeId() != null)
        crmRequest.setResidenceType(quote.getLoanQuoteResidentTypeId()); 
     // logger.info("PLProcessImpl.java :: LNo :: 6869* ::^^^^^^^^^^^^^^::::::");
      if (quote.getLoanQuoteEmploymentNature() != null)
        crmRequest.setNatureOfEmployment(quote.getLoanQuoteEmploymentNature()); 
    //  logger.info("PLProcessImpl.java :: LNo :: 6875 ::&&&&&&&&&&&&&::::::");
      
      if (quote.getLoanQuoteIndustryType() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 5864 ::industry id::::::" + quote.getLoanQuoteIndustryType());
        crmRequest.setIndustryType(String.valueOf(quote.getLoanQuoteIndustryType()));
      }
      
      String industryTypeCrmId = "O";
      if (quote.getLoanQuoteIndustryType() != null && !quote.getLoanQuoteIndustryType().equals(Integer.valueOf(9999999)) ) {
      		industryTypeCrmId = commonService.getIndustryTypeById(quote.getLoanQuoteIndustryType()).getIndustryTypeCrmId();
      		if (industryTypeCrmId == null) {
      			industryTypeCrmId = "O";
      		}
      }
      crmRequest.setIndustryType(industryTypeCrmId);
      
      if (quote.getLoanQuoteYearCompanyJoining() != null)
        crmRequest.setCompanyJoiningDate(quote.getLoanQuoteYearCompanyJoining() + " Years" + quote.getLoanQuoteMonthCompanyJoining() + " Months"); 
      if (quote.getLoanQuoteEmployerName() != null)
        crmRequest.setEmployerName(quote.getLoanQuoteEmployerName()); 
      if (quote.getLoanQuoteGrossMonthlySalary() != null)
        crmRequest.setGrossMonthlySalary(BigDecimal.valueOf(quote.getLoanQuoteGrossMonthlySalary().doubleValue())); 
      if (quote.getLoanQuotePreEMIs() != null)
        crmRequest.setCurrentEmiPaying(BigDecimal.valueOf(quote.getLoanQuotePreEMIs().doubleValue())); 
      if (quote.getLoanQuoteRetirementAgeApplicant() != null)
        crmRequest.setRetirementAgeApplicant(quote.getLoanQuoteRetirementAgeApplicant()); 
      if (quote.getLoanQuoteLoanAccountType() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 6517 ::check off id::::::" + quote.getLoanQuoteLoanAccountType());
        crmRequest.setCheckOffType(quote.getLoanQuoteLoanAccountType());
      } 
      if (quote.getLoanQuoteNetMonthlySalary() != null)
        crmRequest.setNetMonthlySalary(BigDecimal.valueOf(quote.getLoanQuoteNetMonthlySalary().doubleValue())); 
      if (quote.getLoanQuoteAverageOfLastSalary() != null)
        crmRequest.setAverageOfLast6MonthSalary(BigDecimal.valueOf(quote.getLoanQuoteAverageOfLastSalary().doubleValue())); 
      if (quote.getLoanQuotePincode() != null)
        crmRequest.setResPincode(quote.getLoanQuotePincode()); 
      if (quote.getLoanQuoteWorkCityId() != null)
        crmRequest.setOfficeCity(this.commonService.getCityById(quote.getLoanQuoteWorkCityId()).getCityName()); 
      if (quote.getLoanQuoteWorkStateId() != null)
        crmRequest.setOfficeState(this.commonService.getStateById(quote.getLoanQuoteWorkStateId()).getStateName()); 
      if (quote.getLoanQuoteHaveSalaryAccountWithSbi() != null)
        crmRequest.setHaveSalaryAccountWithSbi(quote.getLoanQuoteHaveSalaryAccountWithSbi()); 
      if (appFormData.getAppResidenceTypeId() != null) {
        logger.info("PLProcessImpl.java :: LNo :: 6929 ::resedence type id in personal::::" + appFormData.getAppResidenceTypeId());
        crmRequest.setResidenceType(appFormData.getAppResidenceTypeId());
      } 
      if (quote.getLoanQuoteResidenceTypeId() != null) {
        logger.info("PLProcessImpl.java :: LNo :: 6934 ::resedence type id in gold::::" + quote.getLoanQuoteResidenceTypeId());
        if (quote.getLoanQuoteResidenceTypeId().intValue() == 1) {
          crmRequest.setResidenceType(Integer.valueOf(2));
        } else if (quote.getLoanQuoteResidenceTypeId().intValue() == 2) {
          crmRequest.setResidenceType(Integer.valueOf(1));
        } else {
          crmRequest.setResidenceType(quote.getLoanQuoteResidenceTypeId());
        } 
      } 
      if (quote.getLoanQuoteResidentTypeId() != null) {
      //  logger.info("PLProcessImpl.java :: LNo :: 6939 ::resedent type id::::" + quote.getLoanQuoteResidentTypeId());
        crmRequest.setResidentType(String.valueOf(quote.getLoanQuoteResidentTypeId()));
      } 
      if (appFormData.getAppModeOfRepayment() != null)
       // logger.info("PLProcessImpl.java :: LNo :: 6948 ::Mode of repayment::::" + appFormData.getAppModeOfRepayment()); 
      if (appFormData.getAppEducationalQualificationId() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 6952 ::education qualification::::" + appFormData.getAppEducationalQualificationId());
        crmRequest.setQualification(appFormData.getAppEducationalQualificationId());
      } 
      if (appFormData.getAppOfficeAddress1() != null) {
       // logger.info("PLProcessImpl.java :: LNo :: 5957 ::getAppOfficeAddress::::" + appFormData.getAppOfficeAddress1());
        crmRequest.setOfficeAddress1(appFormData.getAppOfficeAddress1());
      } 
      if (appFormData.getAppOfficeAddress2() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 6962 ::AppOfficeAddress2::::" + appFormData.getAppOfficeAddress2());
        crmRequest.setOfficeAddress2(appFormData.getAppOfficeAddress2());
      } 
      if (appFormData.getAppOfficeStateId() != null && appFormData.getAppOfficeStateId().intValue() != 0) {
        //logger.info("PLProcessImpl.java :: LNo :: 6967 ::office state::::" + appFormData.getAppOfficeStateId());
        //logger.info("PLProcessImpl.java :: LNo :: 6968 ::office state name::::" + this.commonService.getStateById(appFormData.getAppOfficeStateId()).getStateName());
        crmRequest.setOfficeState(this.commonService.getStateById(appFormData.getAppOfficeStateId()).getStateName());
      } 
      if (appFormData.getAppOfficeCityId() != null && appFormData.getAppOfficeStateId().intValue() != 0) {
        //logger.info("PLProcessImpl.java :: LNo :: 6973 ::education qualification::::" + appFormData.getAppOfficeCityId());
        //logger.info("PLProcessImpl.java :: LNo :: 6974 ::education qualification::::" + this.commonService.getCityById(appFormData.getAppOfficeCityId()).getCityName());
        crmRequest.setOfficeCity(this.commonService.getCityById(appFormData.getAppOfficeCityId()).getCityName());
      } 
      if (appFormData.getAppOfficePincode() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 6977 ::education qualification::::" + appFormData.getAppOfficePincode());
        crmRequest.setOfficePincode(appFormData.getAppOfficePincode());
      } 
      if (appFormData.getAppOfficePhoneStdCode() != null && appFormData.getAppOfficePhone() != null)
        crmRequest.setOfficePhoneNumber(appFormData.getAppOfficePhoneStdCode() + String.valueOf(appFormData.getAppOfficePhone())); 
      if (appFormData.getAppModeOfRepayment() != null) {
        //logger.info("PLProcessImpl.java :: LNo :: 6987 ::education qualification::::" + appFormData.getAppModeOfRepayment());
        crmRequest.setModeOfRepayment(appFormData.getAppModeOfRepayment());
      } 
      if (appFormData.getAppLoanEmi() != null) {
   //     logger.info("PLProcessImpl.java :: LNo :: 6992 ::appForm getLoanEmi::::" + appFormData.getAppLoanEmi());
        crmRequest.setLoanEmi(BigDecimal.valueOf(appFormData.getAppLoanEmi().doubleValue()));
//        logger.info("PLProcessImpl.java :: LNo :: 6992 ::CRM getLoanEmi::::" + crmRequest.getLoanEmi());
      } 
      if (appFormData.getAppLoanProcessingFee() != null) {
//        logger.info("ELProcessImpl.java :: LNo :: 4441016 :: processing fee :::::" + appFormData.getAppLoanProcessingFee());
        crmRequest.setLoanProcessingFee(BigDecimal.valueOf(appFormData.getAppLoanProcessingFee().doubleValue()));
      } 
      if (appFormData.getAppLoanAccountType() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7002 ::education qualification::::" + appFormData.getAppLoanAccountType());
        crmRequest.setLoanAccountType(appFormData.getAppLoanAccountType());
      } 
      if (appFormData.getAppLoanTenure() != null) {
//        logger.info("ELProcessImpl.java :: LNo :: 4441000 :: loan Tenure :::: ::" + appFormData.getAppLoanTenure());
        crmRequest.setLoanTenure(appFormData.getAppLoanTenure());
      } 
      if (quote.getLoanQuotePropertyType() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 5864 ::education qualification::::" + quote.getLoanQuotePropertyType());
        crmRequest.setLoanPropertyType(quote.getLoanQuotePropertyType());
      } 
      if (quote.getLoanQuotePropertyCost() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 5864 ::education qualification::::" + quote.getLoanQuotePropertyCost());
        crmRequest.setCurrentValueOfProperty(BigDecimal.valueOf(quote.getLoanQuotePropertyCost().doubleValue()));
      } 
      if (quote.getLoanQuotePropertyStateId() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 5864 ::education qualification::::" + quote.getLoanQuotePropertyStateId());
        crmRequest.setPropertyState(this.commonService.getStateById(quote.getLoanQuotePropertyStateId()).getStateName());
      } 
      if (quote.getLoanQuotePropertyCityId() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 5864 ::education qualification::::" + quote.getLoanQuotePropertyCityId());
        crmRequest.setPropertyCity(this.commonService.getCityById(quote.getLoanQuotePropertyCityId()).getCityName());
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark !!!!!!!!!::::");
      if (quote.getLoanQuotePropertyOwnedBySpouse() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7034 ::owned by spouse::::" + quote.getLoanQuotePropertyOwnedBySpouse());
        crmRequest.setIsLoanPropertyJointlyOwned(quote.getLoanQuotePropertyOwnedBySpouse().substring(0, 1));
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark @@@@@@@::::");
      if (quote.getLoanQuoteHasPropertyRented() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7039 ::property rented::::" + appFormData.getAppEducationalQualificationId());
        crmRequest.setHasLoanPropertyAlreadyRented(quote.getLoanQuoteHasPropertyRented().substring(0, 1));
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark #######::::");
      if (quote.getLoanQuoteYearStartDateOflease() != null && quote.getLoanQuoteMonthStartDateOflease() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7044 ::start year of lease::::" + quote.getLoanQuoteYearStartDateOflease());
//        logger.info("PLProcessImpl.java :: LNo :: 7044 ::start month of lease::::" + quote.getLoanQuoteMonthStartDateOflease());
        crmRequest.setLoanPropertyStartDateOfLease(quote.getLoanQuoteYearStartDateOflease() + " " + quote.getLoanQuoteMonthStartDateOflease());
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark $$$$$$$$::::");
      if (quote.getLoanQuoteYearEndDateOfLease() != null && quote.getLoanQuoteMonthEndDateOfLease() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7050 ::::" + quote.getLoanQuoteYearEndDateOfLease());
//        logger.info("PLProcessImpl.java :: LNo :: 7050 ::::" + quote.getLoanQuoteMonthEndDateOfLease());
        crmRequest.setLoanPropertyEndDateOfLease(quote.getLoanQuoteYearEndDateOfLease() + " " + quote.getLoanQuoteMonthEndDateOfLease());
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark %%%%%%%::::");
      if (quote.getLoanQuoteResidentCountryId() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7055 ::education qualification::::" + quote.getLoanQuoteResidentCountryId());
        crmRequest.setResidentCountry(this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId()).getCountryName());
      } 
//      logger.info("PLProcessImpl.java :: LNo :: Mark ^^^^^^^^::::");
      if (quote.getLoanQuoteProfitAfterTax() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7060 ::education qualification::::" + quote.getLoanQuoteProfitAfterTax());
        crmRequest.setProfitAfterTax(BigDecimal.valueOf(quote.getLoanQuoteProfitAfterTax().doubleValue()));
      } 
      if (quote.getLoanQuoteCoapplicantRelationshipId() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 7067 ::co applicant ::::" + quote.getLoanQuoteCoapplicantRelationshipId());
        crmRequest.setCoAppExist1("Y");
      } 
      if (appFormData.getAppLoanInterestRate() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 6664 :::Interest rate::::" + appFormData.getAppLoanInterestRate());
        crmRequest.setLoanInterestRate(appFormData.getAppLoanInterestRate());
      } 
      if (appFormData.getAppLoanAmount() != null) {
//        logger.info("PLProcessImpl.java :: LNo :: 6669 ::  max loan amount :::::" + appFormData.getAppLoanAmount() + "..maxLoanAmount.." + this.maxLoanAmount);
        crmRequest.setLoanMaxAmount((long)this.maxAmt);
        logger.info("PersonalProcessManagerImpl.java maxAmt::::" + this.maxAmt + "..getLoanMaxAmount.." + crmRequest.getLoanMaxAmount());
      } 
      String alternateNo = "";
      if (appFormData.getAppAlternateMobileNumber() != null) {
        alternateNo = "+" + appFormData.getAppAltISDCode() + "-" + appFormData.getAppAlternateMobileNumber();
      }
      crmRequest.setAlternateMobileNumber(alternateNo);
//      logger.info("PLProcessImpl.java :: LNo :: 7060 ::End of if block::::");
      Integer leadSourceKey = this.commonService.getLeadSourceKey(appFormData);
      logger.info("leadSourceKey :: " + leadSourceKey);
      crmRequest.setLeadSourceKey(leadSourceKey);
      
			Optional<MarTech> marTechDeatails = Optional.ofNullable(
					marTechDao.getDetailsByVisitId(personalLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));
			logger.info("PersonalProcessManagerImpl.java :: LNo :: 4461 ::marTechDetails is:::" + marTechDeatails);
			if (marTechDeatails.isPresent()) {
				MarTech martech = marTechDeatails.get();
				crmRequest.setCampaignCode(martech.getCampaignCode());
				crmRequest.setOfferCode(martech.getOfferCode());
				crmRequest.setTrackingCode(martech.getTrackingCode());
				logger.info("PersonalProcessManagerImpl.java :: LNo :: 4467 ::campaignCode is:::"
						+ crmRequest.getCampaignCode());
				logger.info("PersonalProcessManagerImpl.java :: LNo :: 4468 ::offerCode is:::" + crmRequest.getOfferCode());
				logger.info("PersonalProcessManagerImpl.java :: LNo :: 4469 ::trackingCode is:::"
						+ crmRequest.getTrackingCode());
			}
			logger.info(
					"PersonalProcessManagerImpl.java :: LNo :: 4471 ::trackingCode is:::" + crmRequest.getTrackingCode());
    } catch (NullPointerException ne) {
        logger.info("PLProcessImpl.java :: LNo :: 5257 :: " + ne.getMessage());
        logger.info("PLProcessImpl.java :: LNo :: 5258 :: " + ne.getCause());
    } catch (SQLException e) {
      logger.info("PLProcessImpl.java :: LNo :: 5261 :: " + e.getMessage());
      logger.info("PLProcessImpl.java :: LNo :: 5262 :: " + e.getCause());
      
    } 
    return crmRequest;
  }
  
  private static int getOTPResidantVerifiedType(ApplicationFormPersonalLoan appForm) {
    int flag = 0;
    if (appForm.getAppISDCode() != null && "91".equals(appForm.getAppISDCode())) {
      flag = 1;
    } else {
      flag = 2;
    } 
    return flag;
  }
  
  public JSONObject processGetQuotesElite(Integer appSeqId, ApplicationFormPersonalLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) throws JSONException {
    JSONObject jsonObject = new JSONObject();
    try {
      Integer bankLMSUserId = (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID;
      boolean isContactCenterUser = this.commonService.getBankLmsUserRole(bankLMSUserId);
      if (!isContactCenterUser && 
        quote.getLoanQuoteHaveAadhaarNumber() != null && quote.getLoanQuoteHaveAadhaarNumber().intValue() == 1 && (
        quote.getLoanQuotePanCardNo() == null || "".equals(quote.getLoanQuotePanCardNo())) && quote.getLoanQuoteOtherId() != null && quote.getLoanQuoteOtherId().intValue() == 0) {
        jsonObject.put("status", "error");
        jsonObject.put("message", "Please enter either PAN No or select other identity proof.|1");
        return jsonObject;
      } 
      if (quote == null) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      ApplicationFormPersonalLoan application = null;
      if (appSeqId != null) {
        application = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
        if (application == null) {
          jsonObject.put("status", "error");
          jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return jsonObject;
        } 
        if (application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
          if (quote.getLoanQuoteFirstName() != null)
            application.setAppFirstName(quote.getLoanQuoteFirstName()); 
          if (quote.getLoanQuoteMiddleName() != null)
            application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
          if (quote.getLoanQuoteLastName() != null)
            application.setAppLastName(quote.getLoanQuoteLastName()); 
          if (quote.getLoanQuoteDateOfBirth() != null) {
            application.setAppDob(quote.getLoanQuoteDateOfBirth());
            application.setAppDobDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
          } 
          if (quote.getAppEmail() != null)
            application.setAppWorkEmail(quote.getAppEmail()); 
          if (quote.getAppMobile() != null)
            application.setAppMobileNo(quote.getAppMobile()); 
        } 
      } 
      if (application != null && (
        ajaxPostUrl.equalsIgnoreCase("personal-loan") || ajaxPostUrl.equalsIgnoreCase("pension-loan")) && 
        application.getAppApplyingFrom() == 2 && 
        application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo()) && application.getAppMobileVerified().equalsIgnoreCase("Y")) {
        jsonObject.put("status", "error");
        jsonObject.put("message", "You have already verified OTP with NRI mobile number : " + application.getAppMobileNo());
        return jsonObject;
      } 
      int oldVisitId = 0;
      if (application != null && application.getAppQuoteId() != null) {
        oldVisitId = this.personalLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
      } else {
        oldVisitId = trackVisitId.intValue();
      } 
      quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      if (application != null) {
        if (application.getAppOTPVerifyType() == 0) {
          if (!"Y".equalsIgnoreCase(application.getAppMobileVerified()))
            if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
              quote.setAppApplyingFrom(1);
              quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
            } else if (quote.getAppApplyingFrom() == 2) {
              MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
              quote.setAppISDCode(country.getCountryCode().toString());
            }  
        } else if (!"Y".equalsIgnoreCase(application.getAppEmailVerified())) {
          if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
            quote.setAppApplyingFrom(1);
            quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          } else if (quote.getAppApplyingFrom() == 2) {
            MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
            quote.setAppISDCode(country.getCountryCode().toString());
          } 
        } 
      } else if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
        quote.setAppApplyingFrom(1);
        quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
      } else if (quote.getAppApplyingFrom() == 2) {
        MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteResidentCountryId());
        quote.setAppISDCode(country.getCountryCode().toString());
      } 
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && (
        appSeqId == null || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS.intValue() && "Y".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())))) {
        String oldMobile = "";
        if (quote.getAppApplyingFrom() == 2) {
          oldMobile = quote.getAppNRIMobileNo();
        } else if (quote.getAppApplyingFrom() == 2) {
          oldMobile = quote.getAppMobile();
        } 
        String str1 = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
        if (quote.getAppMobile() != null && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0"))
          if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
            boolean isLeadExists = false;
            int leadLoanPurposeId = 0;
            if (quote.getLoanQuoteLoanPurposeId().intValue() == 11) {
              leadLoanPurposeId = 4;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 12) {
              leadLoanPurposeId = 7;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 13) {
              leadLoanPurposeId = 6;
            } 
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.PERSONAL_LOAN_ID, Integer.valueOf(leadLoanPurposeId), str1, oldMobile, loanTypeId);
            if (isLeadExists) {
              jsonObject.put("status", "error");
              jsonObject.put("message", Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              return jsonObject;
            } 
          } else {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.personalLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, str1, oldMobile, quote.getLoanQuoteLoanPurposeId());
            if (isAppFoundForDedupInApplicationStage) {
              jsonObject.put("status", "error");
              jsonObject.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
              return jsonObject;
            } 
            isAppFoundForDedupInDropOffStage = this.personalLoanService.isAppFoundForDedupInDropOffStage(str1, oldMobile, quote.getLoanQuoteLoanPurposeId());
            isAppFoundForDedupInDropRejectStage = this.personalLoanService.isAppFoundForDedupInDropRejectStage(str1, oldMobile, quote.getLoanQuoteLoanPurposeId());
          }  
      } 
      Integer quoteApplyingFrom = Integer.valueOf(quote.getAppApplyingFrom());
      String quoteMobileNRI = quote.getAppNRIMobileNo();
      String quoteMobile = quote.getAppMobile();
      String quoteISD = quote.getAppISDCode();
      Integer preferredLoanBranch = null;
      Integer pensionPayingStateId = null;
      Integer pensionPayingCityId = null;
      Integer pensionPayingDistrId = null;
      Integer pensionPayingBranchId = null;
      Integer preferredStateId = null;
      Integer preferredCityId = null;
      Integer preferredDistrictId = null;
      Integer preferredLoanBranchId = null;
      String panCard = quote.getLoanQuotePanCardNo();
      Integer otherId = quote.getLoanQuoteOtherId();
      String otherIdNu = quote.getLoanQuoteOtherIdNumber();
      if (quote.getLoanQuoteLoanPurposeId().intValue() == Constants.PENSION_LOAN_PURPOSE_ID.intValue())
        if (quote != null && quote.getLoanQuotePensionPayingStateId() != null) {
          preferredLoanBranch = quote.getLoanQuotePreferredLoanBranch();
          pensionPayingStateId = quote.getLoanQuotePensionPayingStateId();
          pensionPayingCityId = quote.getLoanQuotePensionPayingCityId();
          pensionPayingDistrId = quote.getLoanQuotePensionPayingDistrId();
          pensionPayingBranchId = quote.getLoanQuotePensionPayingBranchId();
          preferredStateId = quote.getLoanQuotePreferredStateId();
          preferredCityId = quote.getLoanQuotePreferredCityId();
          preferredDistrictId = quote.getLoanQuotePreferredDistrictId();
          preferredLoanBranchId = quote.getLoanQuotePreferredLoanBranchId();
        } else if (application != null && application.getAppPensionPayingStateId() != null) {
          preferredLoanBranch = application.getAppPreferredLoanBranch();
          pensionPayingStateId = application.getAppPensionPayingStateId();
          pensionPayingCityId = application.getAppPensionPayingCityId();
          pensionPayingDistrId = application.getAppPensionPayingDistrId();
          pensionPayingBranchId = application.getAppPensionPayingBranchId();
          preferredStateId = application.getAppPreferredStateId();
          preferredCityId = application.getAppPreferredCityId();
          preferredDistrictId = application.getAppPreferredDistrictId();
          preferredLoanBranchId = application.getAppPreferredLoanBranchId();
        }  
      if (quote.getLoanQuoteSourceId() == null)
        if (SessionUtil.getApplicationType().intValue() == 0) {
          quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
        } else if (SessionUtil.getApplicationType().intValue() == 1) {
          if (SessionUtil.getLeadId() != null) {
            ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            if (lead != null && lead.getLeadDataSourceId() != null) {
              quote.setLoanQuoteSourceId(lead.getLeadDataSourceId());
            } else {
              quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
            } 
          } else {
            quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
          } 
        } else if (SessionUtil.getApplicationType().intValue() == 2) {
          quote.setLoanQuoteSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
        }  
      
      if(!ValidatorUtil.validateFirstNameLength(quote.getLoanQuoteFirstName())) {
    	  jsonObject.put("status", "error");  
    	  jsonObject.put("message","Please enter between 2 to 20 characters in First name.");
       	return jsonObject;
         }
         if(!ValidatorUtil.validateMiddleNameLength(quote.getLoanQuoteMiddleName())){
        	 jsonObject.put("status", "error");
        	 jsonObject.put("message","Please enter between 2 to 20 characters in Middle name.");
        	 return jsonObject;
         }
         if(!ValidatorUtil.validateLastNameLength(quote.getLoanQuoteLastName())) {
        	 jsonObject.put("status", "error");
        	 jsonObject.put("message","Please enter between 2 to 20 characters in Last name.");
        	 return jsonObject;
         }
  	
    	if (!ValidatorUtil.validateFirstName(quote.getLoanQuoteFirstName())) {
    	  jsonObject.put("status", "error");
          jsonObject.put("message", "First name is not in correct format. Please enter only [a-z].");
          return jsonObject;
    
      }
    	if (!ValidatorUtil.validateMiddleName(quote.getLoanQuoteMiddleName())) {
    		
    		jsonObject.put("status", "error");
    		jsonObject.put("message","Middle name is not in correct format. Please enter only [a-z]. ");
    		return jsonObject;
    	}
    	if (!ValidatorUtil.validateLastName(quote.getLoanQuoteLastName())) {
    		
    		jsonObject.put("status", "error");
    		jsonObject.put("message","Last name is not in correct format. Please enter only [a-z] & do not include spaces.");
    		return jsonObject;
    	}

    	//added for same name validation
    	if (quote.getLoanQuoteFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || 
    			(quote.getLoanQuoteMiddleName()!=null && 
    				(quote.getLoanQuoteMiddleName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || quote.getLoanQuoteFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteMiddleName().trim())))) {
    		
    		  jsonObject.put("status", "error");
    		  jsonObject.put("message","For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
    		  return jsonObject;
    	}
    	
    	//if ((quote.getLoanQuoteAddress1() != null && quote.getLoanQuoteAddress1().length()<3 && quote.getLoanQuoteAddress1().length() > 41 )) {
    	if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress1().trim())) {
    		  jsonObject.put("status", "error");
    		  jsonObject.put("message","Please enter between 3 to 40 characters in Address Line 1");
        	  return jsonObject;
          }
    	
    	// if (!(quote.getLoanQuoteAddress1() != null && quote.getLoanQuoteAddress1().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
    	if (!(quote.getLoanQuoteAddress1() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress1()))) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Please enter only [a-z,0-9,(,),-,/] in Current Address Line 1");
              return jsonObject;
    	}
         /* if ((quote.getLoanQuoteAddress2() != null && ValidatorUtil.isAddress(quote.getLoanQuoteAddress2()))) {
        	  loanScenarioBean.setStatus(Integer.valueOf(0));
        	  loanScenarioBean.setMessage("Please enter more than two characters in  Address Line 2");
        	  return loanScenarioBean;
          }*/
          
          if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress2().trim())) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Please enter between 3 to 40 characters in Address Line 2");
        	  return jsonObject;
          }
          
          //loanQuoteAddress2
          // if (!(quote.getLoanQuoteAddress2() != null && quote.getLoanQuoteAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
          if (!(quote.getLoanQuoteAddress2() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress2()))) {
        	  jsonObject.put("status", "error");  
        	  jsonObject.put("message","Please enter only [a-z,0-9,(,),-,/] in Current Address Line 2");
              return jsonObject;
          }
          if(quote.getLoanQuoteAddressLandmark() != null) {
          if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddressLandmark().trim())) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Please enter between 3 to 40 characters in Landmark");
        	  return jsonObject;
          }
          }
          
          if(quote.getLoanQuoteAddressLandmark() != null) {
    	      if (!quote.getLoanQuoteAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
    	    	  jsonObject.put("status", "error");
    	    	  jsonObject.put("message","Please enter only [a-z,0-9,(,),-,/] in  Landmark  ");
    	          return jsonObject;
    	      }
          }
      
      

          if (quote.getLoanQuotePanCardNo() != null && !ValidatorUtil.isValidPanNo(quote.getLoanQuotePanCardNo() )) {
          	//quote.setError("PAN is not in correct format.|2");
        	    jsonObject.put("status", "error");;
        	    jsonObject.put("message","PAN is not in correct format.");
    			return jsonObject;
    		}
          if (quote.getLoanQuoteLoanAmountTaken() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteLoanAmountTaken() )) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Requested loan Amount cannot be greater than 9 digits.");
        	  return jsonObject;
          }
          if (quote.getLoanQuoteGender() != null && !ValidatorUtil.isGender(quote.getLoanQuoteGender() )) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Gender is not in correct format.");
        	  return jsonObject;
          }
        
          if (quote.getAppMobile() != null && !ValidatorUtil.isValidMobile(quote.getAppMobile() )) {
        	  jsonObject.put("status", "error");
        	  jsonObject.put("message","Mobile number is not in correct format.");
        	  return jsonObject;
          }
      
          if (quote != null && quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
              boolean isBankLmsContactCenterId = this.commonService.getBankLmsUserRole(bankLMSUserId);
              logger.info(" PLProcessImpl.java :: LNo :: 5533 ::  isBankLmsContactCenterId : " + isBankLmsContactCenterId + " bankLMSUserId : " + bankLMSUserId + " with AppSeqId " + appSeqId);
              String identityvalidation = this.SbiUtil.getIdentityValidation(false, quote.getLoanQuotePanCardNo(), quote.getLoanQuoteHaveAadhaarNumber(), quote.getLoanQuoteOtherId(), quote.getLoanQuoteOtherIdNumber(), isBankLmsContactCenterId);
              if (!"pass".equalsIgnoreCase(identityvalidation)) {
            	  jsonObject.put("status", "error");
            	  jsonObject.put("message",identityvalidation);
                return jsonObject;
              } 
            }
      
      
      
      
      quote = this.personalLoanHelper.insertLoanQuote(quote, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, trackVisitId);
      if (quote == null) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      if (quote != null && quote.getError() != null) {
        jsonObject.put("status", "error");
        jsonObject.put("message", quote.getError());
        return jsonObject;
      } 
      if (quote.getLoanQuoteId() == null || quote.getLoanQuoteId().intValue() == 0) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      quote.setLoanQuoteEmployerNameId(this.commonService.getAllEmployerIdByName(quote.getLoanQuoteEmployerName()));
      int previousQuoteId = (application != null && application.getAppQuoteId() != null) ? application.getAppQuoteId().intValue() : 0;
      quote.setAppApplyingFrom(quoteApplyingFrom.intValue());
      quote.setAppMobile(quoteMobile);
      quote.setAppNRIMobileNo(quoteMobileNRI);
      quote.setAppISDCode(quoteISD);
      quote.setLoanQuotePanCardNo(panCard);
      quote.setLoanQuoteOtherId(otherId);
      quote.setLoanQuoteOtherIdNumber(otherIdNu);
      if (quote.getLoanQuoteLoanPurposeId().intValue() == Constants.PENSION_LOAN_PURPOSE_ID.intValue()) {
        quote.setLoanQuotePreferredLoanBranch(preferredLoanBranch);
        quote.setLoanQuotePensionPayingStateId(pensionPayingStateId);
        quote.setLoanQuotePensionPayingCityId(pensionPayingCityId);
        quote.setLoanQuotePensionPayingDistrId(pensionPayingDistrId);
        quote.setLoanQuotePensionPayingBranchId(pensionPayingBranchId);
        quote.setLoanQuotePreferredStateId(preferredStateId);
        quote.setLoanQuotePreferredCityId(preferredCityId);
        quote.setLoanQuotePreferredDistrictId(preferredDistrictId);
        quote.setLoanQuotePreferredLoanBranchId(preferredLoanBranchId);
      } 
      application = this.personalLoanHelper.insertAppLoan(quote, application, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null, Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null) ? bankLmsUser.getLmsUserIntermediaryId().intValue() : 0));
      if (application == null) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      if (application != null && application.getError() != null) {
        jsonObject.put("status", "error");
        jsonObject.put("message", application.getError());
        return jsonObject;
      } 
      if (application.getAppSeqId() == null || application.getAppSeqId().intValue() == 0) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      SessionUtil.setPersonalLoanTypeSequenceId(application.getAppSeqId());
      if (isAppFoundForDedupInDropRejectStage)
        application.setAppMobileDedup(Integer.valueOf(0)); 
      if (isAppFoundForDedupInDropOffStage)
        application.setAppMobileDedup(Integer.valueOf(1)); 
      if (SessionUtil.getApplicationCRMLeadId() != null)
        application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId()); 
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(false);
      if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } else {
        statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
      } 
      statusRequest.setState(1);
      statusRequest.setBankLMSUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0);
      statusRequest.setRsmDecision(0);
      statusRequest.setAppMobileVerified(application.getAppMobileVerified());
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
      statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
      statusRequest.setApplicationSubTypeId(application.getAppSubTypeId().intValue());
      StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
      if (statusManagerResponse.getStatus() != 0) {
        application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
        if (!statusManagerResponse.isPreserveData()) {
          application.setAppLoanAmount(null);
          application.setAppLoanAccountType(null);
          application.setAppLoanInterestRate(null);
          application.setAppLoanTenure(null);
          application.setAppLoanProcessingFee(null);
          application.setAppPersonalLoanId(null);
          application = this.personalLoanService.save(application);
        } 
      } else if (application.getAppLoanStatusId().intValue() == 0) {
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
      } 
      if (statusManagerResponse.isPreserveData() && previousQuoteId > 0) {
        application.setAppQuoteId(Integer.valueOf(previousQuoteId));
        application = this.personalLoanService.save(application);
      } 
      if (statusManagerResponse.isEligibleToInsertLog())
        this.personalLoanHelper.insertCallLog(application.getAppSeqId(), (bankLmsUser != null) ? bankLmsUser.getLmsUserId().intValue() : 0, statusManagerResponse.getStatusCallLogs(), null, null, true); 
      if (SessionUtil.getApplicationType() != null)
        if (SessionUtil.getApplicationType().intValue() != 0)
          if (SessionUtil.getApplicationType().intValue() == 1) {
            if (SessionUtil.getLeadId() != null) {
              ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
              if (statusManagerResponse.getStatusLead() != 0) {
                lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
                lead.setLeadAppSeqId(application.getAppSeqId());
                lead = this.commonService.save(lead);
              } 
              application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
              application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
              if (lead.getLeadCreatedLmsUserId() != null) {
                if (application.getAppCreatedLmsUserId() == null)
                  application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
              } else if (application.getAppCreatedLmsUserId() == null && 
                bankLmsUser != null) {
                application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
              } 
            } 
          } else if (SessionUtil.getApplicationType().intValue() == 2) {
            SessionUtil.setApplicationType(Integer.valueOf(1));
            ApplicationFormLead lead = null;
            if (SessionUtil.getLeadId() != null) {
              lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            } else {
              lead = new ApplicationFormLead();
            } 
            lead.setLeadFirstName((application.getAppFirstName() != null) ? application.getAppFirstName() : null);
            lead.setLeadApplyingFrom(application.getAppApplyingFrom());
            lead.setLeadIsdCode((application.getAppISDCode() != null) ? application.getAppISDCode() : Constants.COUNTRY_CODE_INDIA);
            lead.setLeadMobileNo((application.getAppMobileNo() != null) ? application.getAppMobileNo() : null);
            lead.setLeadCityId((quote.getLoanQuoteResidentCityId() != null) ? quote.getLoanQuoteResidentCityId() : Constants.OTHER_ID_INTEGER);
            lead.setLeadWorkEmail((application.getAppWorkEmail() != null) ? application.getAppWorkEmail() : "");
            if (lead.getLeadProductTypeId() == Constants.PERSONAL_LOAN_ID) {
              lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
            } else {
              lead.setLeadProductTypeId(Constants.PERSONAL_LOAN_ID);
            } 
            if (statusManagerResponse.getStatus() != 0)
              lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead())); 
            lead.setLeadMobileAlertCount(Integer.valueOf(1));
            lead.setLeadMobileVerificationCode(Integer.valueOf(Integer.parseInt(Constants.DUMMY_MOBILE_OTP)));
            lead.setLeadVisitId(trackVisitId);
            lead.setLeadActive("Y");
            lead.setLeadDeleted("N");
            lead.setLeadIntermediaryId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
            lead.setLeadCreatedLmsUserId((bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID);
            lead.setLeadMobileVerificationCodeVerified("N");
            lead.setLeadReceiveDatetime(new Date());
            lead.setLeadEntryTime(new Date());
            lead.setLeadEntryDate(new Date());
            lead.setLeadLastUpdated(new Date());
            if (lead.getLeadSourceId() == null)
              lead.setLeadSourceId(Constants.LEAD_SOURCE_ID); 
            if (lead.getLeadDataSourceId() == null)
              lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT); 
            if (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) {
              List<BankLmsUserRole> lmsUserRole = this.commonService.getBankLmsUserRoleByid(bankLmsUser.getLmsUserId());
              if (lmsUserRole != null)
                for (BankLmsUserRole bankLmsUserRole : lmsUserRole) {
                  if (lmsUserRole != null && bankLmsUserRole.getLmsRoleTypeId() != null && (3 == bankLmsUserRole.getLmsRoleTypeId().intValue() || 4 == bankLmsUserRole.getLmsRoleTypeId().intValue()))
                    lead.setLeadAssignedLmsUserId(bankLmsUser.getLmsUserId()); 
                }  
            } 
            lead.setLeadAppSeqId(application.getAppSeqId());
            if (bankLmsUser != null)
              if (bankLmsUser.getLmsUserType().intValue() == 1 || bankLmsUser.getLmsUserType().intValue() == 3) {
                lead.setLeadAppContactCenterLocation(0);
              } else {
                Integer lmsUserLocationId = this.commonService.getLmsUserLocationId(bankLmsUser.getLmsUserId());
                if (lmsUserLocationId != null) {
                  lead.setLeadAppContactCenterLocation(lmsUserLocationId.intValue());
                } else {
                  lead.setLeadAppContactCenterLocation(1);
                } 
              }  
            lead = this.commonService.save(lead);
            SessionUtil.setLeadId(lead.getLeadId());
            application.setAppMobileVerified(lead.getLeadMobileVerificationCodeVerified());
            application.setAppContactCenterLocation(lead.getLeadAppContactCenterLocation());
            if (lead.getLeadCreatedLmsUserId() != null) {
              if (application.getAppCreatedLmsUserId() == null)
                application.setAppCreatedLmsUserId(lead.getLeadCreatedLmsUserId()); 
            } else if (application.getAppCreatedLmsUserId() == null && 
              bankLmsUser != null) {
              application.setAppCreatedLmsUserId(bankLmsUser.getLmsUserId());
            } 
          }   
      SessionUtil.setPersonalLoanTypeSequenceId(application.getAppSeqId());
      int appApplyingFrom = 1, appOTPVerifyType = 0;
      String isdCode = null;
      if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2)
        appApplyingFrom = quote.getAppApplyingFrom(); 
      if (appApplyingFrom == 2) {
        if (application.getAppISDCode() != null && application.getAppCountryId() != null)
          isdCode = Integer.toString(application.getAppCountryId().intValue()); 
      } else {
        isdCode = Constants.COUNTRY_CODE_INDIA;
      } 
      jsonObject = OTP(application.getAppSeqId(), Integer.valueOf(14), application.getAppFirstName(), appApplyingFrom, appOTPVerifyType, isdCode, application.getAppMobileNo(), application.getAppWorkEmail(), null, (bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, ajaxPostUrl, Integer.valueOf(appOTPVerifyType));
      return jsonObject;
    } catch (SQLException ne) {
        logger.info("PersonalProcessManagerImpl.java LNo: 5696 :: processGetQuotes() :: " + ne.getMessage());
        jsonObject.put("status", "error");
        jsonObject.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return jsonObject;
     } 
  }
  
  public JSONObject verifyConcentOtp(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException, SQLException {
    JSONObject json = new JSONObject();
    int appOTPVerifyType = 0;
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getPersonalLoanApplicationSequenceId() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 3803");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    ApplicationFormPersonalLoan appForm = this.personalLoanService.getApplicationFormPersonalLoanByAppSeqId(SessionUtil.getPersonalLoanApplicationSequenceId());
    if (appForm == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 3981 with AppSeqId is null");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppOTPAttemptCount() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 3821 with AppSeqId " + appForm.getAppSeqId());
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppISDCode() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 3832 with AppSeqId " + appForm.getAppSeqId());
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppMobileNo() == null) {
      logger.info("PersonalProcessManagerImpl LNO :: 3841 with AppSeqId " + appForm.getAppSeqId());
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = appForm.getAppOTPAttemptCount();
    if (stateId.intValue() == 41)
      try {
        if (alertCount.intValue() >= 5) {
          logger.info("PersonalProcessManagerImpl LNO :: 3854 with AppSeqId " + appForm.getAppSeqId());
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          appForm.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(appForm.getAppMobileNo()));
          logger.info("mobile OTP ::" + appForm.getAppMobileVerificationCode() + " with AppSeqId " + appForm.getAppSeqId());
        } else {
          logger.info("PersonalProcessManagerImpl LNO :: 3864 with AppSeqId " + appForm.getAppSeqId());
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        boolean sendSMSConsent = false;
        if (isDsrPage.equalsIgnoreCase("true")) {
          boolean bankLmsUserRoleExceptContactCenter = this.commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
          if (bankLmsUserRoleExceptContactCenter) {
            logger.info("Not contact Center user with AppSeqId " + appForm.getAppSeqId());
            sendSMSConsent = true;
          } else {
            logger.info("contact Center user with AppSeqId " + appForm.getAppSeqId());
          } 
        } 
        String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent) {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
        } else {
          msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        } 
        msgBody = this.SbiUtil.urlEncode(msgBody);
        String SMS_TEXT = null;
        if (Constants.COUNTRY_CODE_INDIA.equals(appForm.getAppISDCode().toString())) {
        	SMS_TEXT=Constants.SMS_STRING_INDIAN;
        } else {
        	SMS_TEXT=Constants.SMS_STRING_NRI;
        } 
        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
        SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", appForm.getAppMobileVerificationCode().toString());
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
        	logger.info("OTP for Mobile Number: " + (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim() + " is " + appForm.getAppMobileVerificationCode().toString());
        }

        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          json.put("status", "error");
          json.put("message", "OTP service is down");
          json.put("alertCount", alertCount);
          return json;
        } 
        alertCount = Integer.valueOf(alertCount.intValue() + 1);
        appForm.setAppOTPAttemptCount(alertCount);
        appForm = this.personalLoanService.save(appForm);
        if (appForm == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 4523 Sorry for the inconvenience");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        appForm = this.personalLoanService.save(appForm);
        if (appForm == null) {
          logger.info("PersonalProcessManagerImpl LNO :: 4535 Sorry for the inconvenience");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (NullPointerException se) {
          logger.info("PersonalProcessManagerImpl.java LNO 5827::" + se.getMessage());
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       }  
    if (stateId.intValue() == 42)
      try {
        Integer appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 3933:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        boolean isOPTVerified = true;
        if (appOTPVerifyType == 0) {
          if (appForm.getAppMobileVerified().equals("Y")) {
            ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
            lead.setLeadMobileVerificationCodeVerified("Y");
            lead = this.commonService.save(lead);
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            logger.info("OTP verfied for mobileNo ::" + (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("PersonalProcessManagerImpl.java LNO 3964:: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("PersonalProcessManagerImpl.java LNO 3971:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(appForm.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
          statusRequest.setState(stateId.intValue());
          statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
          statusRequest.setRsmDecision(0);
          statusRequest.setApplicationType(SessionUtil.getApplicationType().intValue());
          statusRequest.setApplicationLeadType(appForm.getAppDataSourceId().intValue());
          statusRequest.setApplicationSubTypeId(appForm.getAppSubTypeId().intValue());
          statusRequest.setAppMobileVerified(appForm.getAppMobileVerified());
          StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
          if (statusManagerResponse.getStatus() != 0) {
            appForm.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE171_ID));
          } else if (appForm.getAppLoanStatusId().intValue() == 0) {
            logger.info("PersonalProcessManagerImpl.java LNO 3993:: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          this.personalLoanHelper.insertCallLog(appForm.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE171_ID, null, null, true);
        } else {
          logger.info("OTP is authentication failed::");
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        appForm = this.personalLoanService.save(appForm);
        if (appForm == null) {
          logger.info("PersonalProcessManagerImpl.java LNO 4017:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException se) {
          logger.info("PersonalProcessManagerImpl.java LNO 5905::" + se.getMessage());
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       } 
    return json;
  }
  
  public void getCIFNUMBER(String cifnumber) {
//    logger.info("PersonalProcessManagerImpl.java LNO cifnumber::" + cifnumber);
    getcif = cifnumber;
  //  logger.info("PersonalProcessManagerImpl.java LNO getcif::" + getcif);
  }
  
  public String setUtmParam(String utm) {
    logger.info("PersonalProcessManagerImpl.java LNO utm::" + utm);
    return utm;
  }
  
	//new code for cibil call
	 public ApplicationFormPersonalLoanQuote cibilCallForPersonalLoan(ApplicationFormPersonalLoanQuote quote, ApplicationFormPersonalLoan application) {
		   try {
	           
			   BureauLinkRequestNew bureauLinkRequestnew =bureauLinkUtil.prepareBureaulinkRequestForPersonalLoan(quote, application); 
			   
			   BureauLinkRequestResponse requestResponseData = bureauLinkUtil.saveBureauLinkRequestNew(bureauLinkRequestnew, "A12");
				String jsonInString = (new Gson()).toJson(bureauLinkRequestnew);
				//logger.info("jsonRequest :: " + jsonInString);

				//new code for getting response data
				String jsonResponse = bureauLinkUtil.sendReceiveJson(jsonInString);
				//logger.info("Response Received from Cibil API :: " +jsonResponse+ " At "+new Date() + "  for MemberRefNo :: " + quote.getLoanQuoteId().toString());
				
				requestResponseData.setBlRequestXml(jsonInString);
				
				if (jsonResponse != null && !jsonResponse.equals("")) {
					requestResponseData.setBlXmlResponse(jsonResponse);

					if (JSONUtil.isJsonValid(jsonResponse)) {
						HashMap<String, String> cibilScoreMap = bureauLinkUtil.fetchCibilDataFromResponse(jsonResponse);
						requestResponseData.setBlApplicationId(String.valueOf(application.getAppSeqId()));
						
						if (jsonResponse != null && cibilScoreMap.get("CIBILTUSC3") != null || cibilScoreMap.get("PLSCORE") != null) {
							requestResponseData.setBlCibilScore(cibilScoreMap.get("CIBILTUSC3"));
							requestResponseData.setBlCibilPlScore(cibilScoreMap.get("PLSCORE"));
							requestResponseData.setBlFetchResponseStatus("SUCCESS");
						} else {
							String errorCode = cibilScoreMap.get("errorCode");
							requestResponseData.setBlResponseErrorCode(errorCode);
							requestResponseData.setBlFetchResponseStatus("FAIL");
						}
						commonService.save(requestResponseData);
					        
				       if (jsonResponse != null && cibilScoreMap.get("PLSCORE") != null) {
				    	   quote.setLoanQuoteCibilScore(Integer.valueOf(cibilScoreMap.get("PLSCORE")));
				    	   personalLoanService.save(quote);  
				       }
					} else {
						requestResponseData.setBlResponseErrorCode("00000");
						requestResponseData.setBlFetchResponseStatus("FAIL");
					}
				} else {
					requestResponseData.setBlResponseErrorCode("00000");
					requestResponseData.setBlFetchResponseStatus("FAIL");
				}
		       
		       return quote;
		     } catch (ParseException e) {
		       logger.info("Exception in BureauLink Process :: " + e);
		     } catch (SQLException e) {
			   logger.info("Exception in BureauLink Process :: " + e);
			 } catch (IOException e) {
		       logger.info("Exception in BureauLink Process :: " + e);
		     } catch (NumberFormatException e) {
		       logger.info("Exception in BureauLink Process :: " + e);
		     }
		    return quote;
	}
}
