package com.mintstreet.loan.homeloan.bo.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Year;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.mintstreet.campaign.dao.MarTechDao;
import com.mintstreet.campaign.entity.MarTech;
import com.mintstreet.campaign.entity.TrackCampaign;
import com.mintstreet.campaign.entity.TrackCampaignPlacementSource;
import com.mintstreet.campaign.entity.TrackMainCampaign;
import com.mintstreet.campaign.entity.TrackVisit;
import com.mintstreet.common.bo.BureauLinkRequestNew;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.CBSCustomerInformation;
import com.mintstreet.common.bo.CBSLoanAccountInformation;
import com.mintstreet.common.bo.CRMRequest;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.RSMResponse;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.engine.CommonEngine;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.BureauLinkRequestResponse;
import com.mintstreet.common.entity.MasterBank;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterDistrict;
import com.mintstreet.common.entity.MasterDocumentType;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterLoanCategory;
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
import com.mintstreet.common.util.ConsentUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.FileHelper;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.MapperUtil;
import com.mintstreet.common.util.RefGenerateUtilHL;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.bo.ConsentReadResponse;
import com.mintstreet.integration.edms.action.EdmsFinalServiceAction;
import com.mintstreet.integration.edms.bo.FstoreDoc;
import com.mintstreet.integration.pan.action.PanServiceAction;
import com.mintstreet.integration.pan.bo.PanApiInputParams;
import com.mintstreet.integration.pan.bo.PanApiReturnResponse;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.entity.MasterProject;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.homeloan.util.HomeLoanHelper;
import com.mintstreet.loan.product.entity.HlProduct;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;

import freemarker.template.utility.StringUtil;

public class HomeProcessManagerImpl {
  private static final Logger logger = LogManager.getLogger(HomeProcessManagerImpl.class.getName());
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private HomeLoanService homeLoanService;
  
  @Autowired
  private HomeLoanHelper homeLoanHelper;
  
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
  
  public volatile String appRefKey;
  
  public volatile String lastReferenceNumber;
  
  @Autowired
  private BureauLinkUtil bureauLinkUtil;
  
  @Autowired
  private CRMServiceNew crmServiceNew;
  
  @Autowired
  private RefGenerateUtilHL refGenerateUtil;
  
  @Autowired
  private PanServiceAction panServiceAction;
  
  @Autowired
  private MarTechDao marTechDao;
  
  @Autowired
  private EdmsFinalServiceAction edmsServiceAction;
  
  @Autowired
  private ConsentUtil consentUtil;
  
  @Autowired
  private ConsentService consentService;
  
  public JSONObject processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId, String ajaxPostUrl, String imageName) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      ApplicationFormHomeLoan appFormData = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
      if (appFormData == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      if (ValidatorUtil.isValid(imageNo)) {
        String fullPath = "";
        if (imageNo.intValue() == 1) {
          if (appFormData.getAppPhotoIdName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + appFormData.getAppPhotoIdName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } 
        } else if (imageNo.intValue() == 2) {
          if (appFormData.getAppIdentityProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + appFormData.getAppIdentityProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } 
        } else if (imageNo.intValue() == 3) {
          if (appFormData.getAppResidenceProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + appFormData.getAppResidenceProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } 
        } else if (imageNo.intValue() == 4) {
          if (appFormData.getAppIncomeProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + appFormData.getAppIncomeProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } 
        } else if (imageNo.intValue() == 5) {
          if (appFormData.getAppEmploymentProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + appFormData.getAppEmploymentProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppEmploymentProofId(null);
            appFormData.setAppEmploymentProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.HL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppEmploymentProofId(null);
            appFormData.setAppEmploymentProofName(null);
          } 
        } 
        
        appFormData = this.homeLoanService.save(appFormData);
        json.put("status", "success");
        json.put("message", "Document deleted.");
      } else {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      } 
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 326 :: processDeleteProductImage() ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
    } 
    return json;
  }
  
  public JSONObject processToDocumentPickupUploaded(Integer appSeqId, ApplicationFormHomeLoan appForm, Integer bankLMSUserId, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      if (appForm == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      ApplicationFormHomeLoan appFormData = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
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
          appFormData.setAppPickupStateId(appForm.getAppPickupStateId()); 
        if (appForm.getAppPickupCityId() != null)
          appFormData.setAppPickupCityId(appForm.getAppPickupCityId()); 
        if (appFormData.getAppPickupCityId() != null && 
          appFormData.getAppPickupCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue() && 
          appForm.getAppPickupDistrictId() == null) {
          json.put("status", "error");
          json.put("message", "Invalid params");
          return json;
        } 
        if (appForm.getAppPickupDistrictId() != null)
          appFormData.setAppPickupDistrictId(appForm.getAppPickupDistrictId()); 
        if (appForm.getAppPickupPincode() != null)
          appFormData.setAppPickupPincode(appForm.getAppPickupPincode()); 
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
        statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
          Date callDocPickupTime = DateUtil.changeDateFormatToDate(String.valueOf(appForm.getAppDocPickupDate()) + " " + appForm.getAppDocPickupTimeString(), "MM/dd/yyyy HH:mm");
          this.homeLoanHelper.insertCallLog(appSeqId, bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, callDocPickupTime, true);
        } 
        if (appFormData.getAppDocsEnteredTime() == null)
          appFormData.setAppDocsEnteredTime(new Date()); 
        
      } 
      
      appFormData = this.homeLoanService.save(appFormData);
      logger.info("HomeLoanProcessImpl.java Line 403   *************after svae method  HomeLoanService file" + appFormData.getAppReferenceId());
      
      FstoreDoc doc = new FstoreDoc();
      Integer branchCode = this.commonService.getBranchCodeByBranchId(appFormData.getAppBranchId());
      doc.setAppPhotoIdName(appFormData.getAppPhotoIdName());
      doc.setAppIdentityProofName(appFormData.getAppIdentityProofName());
      doc.setAppResidenceProofName(appFormData.getAppResidenceProofName());
      doc.setAppIncomeProofName(appFormData.getAppIncomeProofName());
      doc.setAppEmploymentProofName(appFormData.getAppEmploymentProofName());
      
      edmsServiceAction.uploadDocumentsToEDMS(doc, Constants.HOME_LOAN_ID, appFormData.getAppReferenceId(), branchCode);
      
      json.put("status", "success");
      json.put("message", "Document uploaded successfully. Please proceed for the next steps.");
      if (!bankLMSUserId.equals(Constants.OTHER_ID_INTEGER) || appFormData.getAppAssignedLmsSalesUserId() != null) {
        if (bankLMSUserId.equals(Constants.OTHER_ID_INTEGER))
          bankLMSUserId = appFormData.getAppAssignedLmsSalesUserId(); 
        this.taskExecutorService.sendingSMSForHomeLoan(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE, Integer.valueOf(0), appFormData, bankLMSUserId, false);
      } 
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 470 :: processToDocumentPickupUploaded() ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
    } 
    return json;
  }
  
  public JSONObject processAddCoapplicant(Integer appSeqId, ApplicationFormHomeLoanQuote quote, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      ApplicationFormHomeLoan application = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
      if (application == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      } 
      Integer quoteId = application.getAppQuoteId();
      if (quoteId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      ApplicationFormHomeLoanQuote quotePrev = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(application.getAppQuoteId());
      if (quotePrev == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      } 
      quotePrev = this.homeLoanHelper.saveCoApplicantDetails(quotePrev, quote);
      if (quotePrev == null) {
        json.put("status", "error");
        json.put("message", "Co-applicant not upadated try again.");
      } 
      json.put("status", "success");
      json.put("message", "Co-applicant upadated.");
      return json;
    } catch (JSONException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 513 :: processAddCoapplicant() ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } catch (SQLException e) {
      logger.info("HomeLoanProcessImpl.java LNo: 513 :: processAddCoapplicant() ", e);
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      return json;
    } 
  }
  
  public JSONObject OTP(Integer appSeqId, Integer stateId, String name, int appApplyingFrom, int appOTPVerifyType, String isdCode, String mobile, String email, String inputOtp, Integer bankLMSUserId, String ajaxPostUrl) {
    JSONObject json = new JSONObject();
    boolean isAlternate=false;
    
    try {
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
        return json;
      } 
      ApplicationFormHomeLoan application = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
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
			application.setAppAltISDCode(isdCode);
		}

      Integer alertCount = Integer.valueOf(0);
      if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
        if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (
          !ValidatorUtil.isValid(name) || !ValidatorUtil.isValid(mobile) || !ValidatorUtil.isValid(email))) {
        	
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
        } 
        if (appApplyingFrom == 2) {
          application.setAppOTPVerifyType(appOTPVerifyType);
          application.setAppApplyingFrom(appApplyingFrom);
          ApplicationFormHomeLoanQuote quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(application.getAppQuoteId());
          if (quote != null && quote.getLoanQuoteCountryId() != null) {
            MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
            if (country != null)
              isdCode = country.getCountryCode().toString(); 
          } 
          if (!application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
            application.setAppISDCode(isdCode);
            SessionUtil.setISDCode(isdCode);
          } 
          if (ValidatorUtil.isValidNRIMobile(mobile)) {
        	  
        	  if(!isAlternate) {
        		    application.setAppMobileNo(mobile);
                    SessionUtil.setMobile(mobile);
                }else {
              	  
              	  if (ValidatorUtil.isValidAlternateMobile(mobile)) {
        				application.setAppMobileNo(mobile);
        				SessionUtil.setMobile(mobile);
        				application.setAppAlternateMobileNumber(mobile);
        			}
                }
        
          } 
        } else {
          application.setAppOTPVerifyType(0);
          application.setAppApplyingFrom(1);
          isdCode = Constants.COUNTRY_CODE_INDIA;
          
          //logger.info("isAlternate check1 while send OTP::" + isAlternate);
         // logger.info("mobile check1 while send OTP::" + mobile);
          if(!isAlternate) {
          	application.setAppMobileNo(mobile);
          } else {
        	  if (ValidatorUtil.isValidAlternateMobile(mobile)) {
  				
  				application.setAppAlternateMobileNumber(mobile);
  			}
          }
          application.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          
         // logger.info("mobile check2 while send OTP::" + mobile);
      	if (ValidatorUtil.isValid(mobile)) {
			if (!isAlternate) {
				
			//	logger.info("1st mobile if not alternate:: " + mobile);
				 application.setAppMobileNo(mobile);
					SessionUtil.setMobile(mobile);
				//	logger.info("get 1st mobile from session : " + SessionUtil.getMobile());
			} else {
				SessionUtil.setalternateMobileNumber(mobile);
			}
        } 
        if (ValidatorUtil.isValid(name)) {
          SessionUtil.setApplicantName(name);
          application.setAppFirstName(name);
        } 
        if (ValidatorUtil.isValid(email)) {
          SessionUtil.setEmail(email);
          application.setAppWorkEmail(email);
        } 
        
		HttpServletRequest request = RequestUtil.getServletRequest();
		String resend = request.getParameter("changeM");
		if(isAlternate) {

			if (application.getAppAltMobileVerificationCode() == null
					|| "N".equals(application.getAppAltMobileVerified())) {

				if (resend == null && application.getAppAlternateMobileNumber() != null
						&& !application.getAppAlternateMobileNumber().equalsIgnoreCase(mobile))
					application.setAppAltOtpMobAlertsCount(Integer.valueOf(0));

				if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
					alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0
							: application.getAppAltOtpMobAlertsCount().intValue());

					if (alertCount.intValue() >= 5) {
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
						json.put("alertCount", alertCount);
						return json;
					}

					alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0
							: application.getAppAltOtpMobAlertsCount().intValue());
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
					// code to generate OTP for mobile no.
					application.setAppAltMobileVerificationCode(
							this.SbiUtil.getVerificationCode(application.getAppAlternateMobileNumber()));
					logger.info(
							"method OTP() >> is Alternate mobile number  application.getAppAltMobileVerificationCode()::"
									+ application.getAppAltMobileVerificationCode());
				} else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
					application.setAppEmailVerificationCode(
							this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
					logger.info("inside auto process impl lno  is Alternate mobile number771");
				}
				application = this.homeLoanService.save(application);

				if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4
						|| stateId.intValue() == 15) {
					if (appOTPVerifyType == 0) {
						String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
								Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
						msgBody = this.SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;

						if (!application.getAppAltISDCode().equals("91")) {
							SMS_TEXT=Constants.SMS_STRING_NRI;
						} else {
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}
						
						SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE",
								application.getAppAltMobileVerificationCode().toString());
						SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
								(String.valueOf(application.getAppAltISDCode())
										+ application.getAppAlternateMobileNumber()).trim());
						
						 if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							 logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode()) + application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
						 }
						 
						if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
							json.put("status", "error");
							json.put("message", "OTP service is down");
							json.put("alertCount", alertCount);
							return json;
						}
					}
					application.setAppAltMobileVerificationCodeReceived("Y");
					application = this.homeLoanService.save(application);
				}
				json.put("status", "success");
				json.put("message", "OTP Code sent");
				json.put("alertCount", alertCount);
				return json;

			} else {

				if (resend == null && application.getAppAlternateMobileNumber() != null
						&& !application.getAppAlternateMobileNumber().equalsIgnoreCase(mobile))
					application.setAppAltOtpMobAlertsCount(Integer.valueOf(0));

				if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
					alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0
							: application.getAppAltOtpMobAlertsCount().intValue());

					if (alertCount.intValue() >= 5) {
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
						json.put("alertCount", alertCount);
						return json;
					}
					alertCount = Integer.valueOf((application.getAppAltOtpMobAlertsCount() == null) ? 0
							: application.getAppAltOtpMobAlertsCount().intValue());
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
					// code to generate OTP for mobile no.
					application.setAppAltMobileVerificationCode(
							this.SbiUtil.getVerificationCode(application.getAppAlternateMobileNumber()));
					logger.info(
							"method OTP() >> is Alternate mobile number  application.getAppAltMobileVerificationCode()::"
									+ application.getAppAltMobileVerificationCode());
				}
				application = this.homeLoanService.save(application);

				if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4
						|| stateId.intValue() == 15) {
					if (appOTPVerifyType == 0) {
						String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
								Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
						msgBody = this.SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;
						
						//changes to send success message start
						if (!application.getAppAltISDCode().equals("91")) {
							application.setAppApplyingFrom(2);
						} else {
							application.setAppApplyingFrom(1);
						}
						//changes to send success message end
						
						if (application.getAppApplyingFrom() == 2) {
							SMS_TEXT=Constants.SMS_STRING_NRI;
						} else {
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}
						
						SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE",
								application.getAppAltMobileVerificationCode().toString());
						SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
								(String.valueOf(application.getAppAltISDCode())
										+ application.getAppAlternateMobileNumber()).trim());
						
						if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode()) + application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
						}
						
						if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
							json.put("status", "error");
							json.put("message", "OTP service is down");
							json.put("alertCount", alertCount);
							return json;
						}
					}
					application.setAppAltMobileVerificationCodeReceived("Y");
					application.setAppMobileVerificationCodeReceived("Y");
					application = this.homeLoanService.save(application);
				}
				json.put("status", "success");
				json.put("message", "OTP Code sent");
				json.put("alertCount", alertCount);
				return json;

			}

		}else {
			
        boolean isAppFoundForDedupInDropOffStage = false;
        boolean isAppFoundForDedupInDropRejectStage = false;
        if (!Constants.DUMMY_MOBILE_NO.contains(application.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
          boolean isAppFoundForDedupInApplicationStage = false;
          if (application.getAppQuoteId() != null) {
            ApplicationFormHomeLoanQuote quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(application.getAppQuoteId());
            if (quote == null) {
              json.put("status", "error");
              json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
              json.put("alertCount", 0);
              return json;
            } 
            isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, (application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java :: LNo 605 :: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              json.put("status", "error");
              json.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
              json.put("alertCount", alertCount);
              return json;
            } 
            isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java :: LNo 613 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java :: LNo 615 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
          } 
        } 
        if (isAppFoundForDedupInDropRejectStage)
          application.setAppMobileDedup(Integer.valueOf(0)); 
        if (isAppFoundForDedupInDropOffStage)
          application.setAppMobileDedup(Integer.valueOf(1)); 
        if (application.getAppMobileVerificationCode() == null || "N".equals(application.getAppMobileVerified())) {
          if (application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(mobile))
            application.setAppOtpMobileAlertsCount(Integer.valueOf(0)); 
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {
            alertCount = Integer.valueOf((application.getAppOtpMobileAlertsCount() == null) ? 0 : application.getAppOtpMobileAlertsCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
          } else if (stateId.intValue() == 4 || stateId.intValue() == 15) {
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
          application.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(application.getAppMobileNo()));
          if (appApplyingFrom == 2)
            application.setAppEmailVerificationCode(this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail())); 
          
          application = this.homeLoanService.save(application);
          if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
            if (appOTPVerifyType == 0) {
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
              msgBody = this.SbiUtil.urlEncode(msgBody);
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
                json.put("status", "error");
                json.put("message", "OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            }
            
            if (appApplyingFrom == 2 && (
              appOTPVerifyType == 1 || stateId.intValue() == 14 || stateId.intValue() == 15)) {
              String msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
              msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
              msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
              msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
              msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
              msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", application.getAppEmailVerificationCode());
              msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.HOME_LOAN_PRODUCT_NAME);
              boolean sendStatus = false;
              String[] emailId = { application.getAppWorkEmail() };
              sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
              if (!sendStatus) {
                json.put("status", "error");
                json.put("message", "EMAIL OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
              logger.info("HomeProcessManagerImpl.java LNo : 818 MESSAGE_TEXT : " + msgBody);
            } 
            application.setAppMobileVerificationCodeReceived("Y");
            application = this.homeLoanService.save(application);
          } 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
          return json;
        } 
		}
	} 
	} else if (stateId.intValue() == 5 || stateId.intValue() == 16) {
		
		boolean isEmail=false;
	       
		if (inputOtp.startsWith("EML_")) {
			isEmail=true;
			inputOtp = inputOtp.substring(4);
		
		}
		
		if(!isEmail) {
	        
	        if(inputOtp !=null) {
	        	SbiUtil sbiutil=new SbiUtil();
	        	//logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
	        	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
				//logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
	        }
		}
	       // logger.info("DecryptedRequest inputOtp   1023  "+inputOtp);
			logger.info("inside else if condition with stateid :: " + stateId.intValue() + "  isAlternate flag::"
					+ isAlternate);
			if (isAlternate) {

				if (!ValidatorUtil.isValid(inputOtp)) {
					json.put("status", "error");
					json.put("message", "Please enter valid OTP.");
					json.put("alertCount", alertCount);
					return json;
				}

				logger.info("homeProcessManagerImpl.java  LNO 764 ::  application.getAppOTPAttemptCount() : "
						+ application.getAppOTPAttemptCount() + " with AppSeqId ::" + appSeqId);
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
				logger.info("application.getAppOTPAttemptCount() before save method::: "
						+ application.getAppOTPAttemptCount());
				//logger.info("application before save :: " + application);
				application = this.homeLoanService.save(application);
				//logger.info("application after save :: " + application);

				if (application == null) {
					logger.info("AutoProcessManagerImpl.java  LNO 764 :: error on saving::");
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
					json.put("alertCount", 1);
					return json;
				}
				boolean isOPTVerified = false;

				if (application.getAppAltMobileVerificationCode() != null && String
						.valueOf(application.getAppAltMobileVerificationCode()).equalsIgnoreCase(inputOtp)) {
					isOPTVerified = true;
					application.setAppAltMobileVerified("Y");
					application.setAppMobileVerified("Y");

					logger.info("method OTP() >> setAppMobileVerified 3::" + application.getAppAltMobileVerified());
				}

				logger.info("isOPTVerified::: " + isOPTVerified);
				if (isOPTVerified) {
			          StatusRequest statusRequest = new StatusRequest();
			          statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
			          statusRequest.setHaveLoanOffer(true);
			          statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
			          if (statusManagerResponse.isEligibleToInsertLog())
			            if (stateId.intValue() == 5) {
			              this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), Constants.CALL_LOGS_MESSAGE_STATE1CB, null, true);
			              application.setAppLeadAsCallBack(Integer.valueOf(1));
			            } else {
			              if (application.getAppDataSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue())
			                this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE1_ID, null, null, true); 
			              this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true);
			            }  

			          //logger.info("Home lOan data 6 " + application.toString());
			          application = this.homeLoanService.save(application);
			          json.put("status", "success");
			          if (stateId.intValue() == 5) {
			        	  //logger.info("cheking orginal moblie number io if  1120 from Session "+SessionUtil.getMobile());
			            logger.info("ProcessManagerImpl LNO ::926 :: message has been sent to user ");
			            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
			          } else {
			          //  logger.info("OTP verfied for mobileNo ::" + application.getAppAlternateMobileNumber());
			            //logger.info("cheking orginal moblie number from Session "+SessionUtil.getMobile());
			            json.put("message", "OTP authentication successful");
			          } 
			          json.put("alertCount", alertCount);
			          return json;
			        }

				//logger.info("AutoProcessManagerImpl.java  LNO 838 :: OTP is incorrect for mobileNo ::"
					//	+ application.getAppAlternateMobileNumber() + " with AppSeqId ::" + appSeqId);
				json.put("status", "error");
				logger.info("cheking LNumber 819 Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue()"
						+ Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue());
				logger.info("cheking LNumber 820 application.getAppDataSourceId().intValue()"
						+ application.getAppDataSourceId().intValue());

				if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == application.getAppDataSourceId()
						.intValue()) {
					json.put("message", "OTP is incorrect! Try again.");
				} else {
					json.put("message", "OTP is incorrect! Try again.|inputOtp|2");
				}
				json.put("alertCount", alertCount);
				return json;

			} else {
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Invalid Request OTP! send properly.");
          json.put("alertCount", alertCount);
          return json;
        } 
        logger.info("HomeProcessManagerImpl.java LNo : 879 application.getAppOTPAttemptCount() : " + application.getAppOTPAttemptCount());
        if (!ValidatorUtil.isValid(application.getAppOTPAttemptCount()))
          application.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (application.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          json.put("alertCount", 1);
          return json;
        } 
        application.setAppOTPAttemptCount(Integer.valueOf(application.getAppOTPAttemptCount().intValue() + 1));
        application = this.homeLoanService.save(application);
        if (application == null) {
          logger.info("HomeProcessManager.java LNO 3018 error on saving:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 1);
          return json;
        } 
        boolean isOPTVerified = false;
        //logger.info("AppMobileVerified is Not Alternate : " + application.getAppMobileVerified());
       // logger.info("AppMobileVerificationCode is Not Alternate : " + application.getAppMobileVerificationCode());
      //  logger.info("inputOtp is Not Alternate : " + inputOtp);
       // logger.info("appOTPVerifyType is Not Alternate : " + appOTPVerifyType);
        
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
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
          if (statusManagerResponse.isEligibleToInsertLog())
            if (stateId.intValue() == 5) {
              this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), Constants.CALL_LOGS_MESSAGE_STATE1CB, null, true);
              application.setAppLeadAsCallBack(Integer.valueOf(1));
            } else {
              if (application.getAppDataSourceId().intValue() != Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue())
                this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE1_ID, null, null, true); 
              this.homeLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true);
            }  
          if (Constants.CALLBACK_SMS_CONSENT) {
            if ("Y".equalsIgnoreCase(application.getAppMobileVerified())) {
              String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
              msgBody = this.SbiUtil.urlEncode(msgBody);
              String SMS_TEXT = null;
              if (application.getAppApplyingFrom() == 2) {
                SMS_TEXT=Constants.SMS_STRING_NRI;
              } else {
                SMS_TEXT=Constants.SMS_STRING_INDIAN;
              }
              SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
              SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(application.getAppISDCode()) + application.getAppMobileNo()).trim());
              //need to mask
              try {
				logger.info("HomeProcessManagerImpl.java LNO ::1289 :: message has been sent to user " + EncryptDecryptUtil.encrypt(SMS_TEXT));
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				logger.info("HomeProcessManagerImpl.java LNO ::1114 , Exception caught ",e);
				
			}
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                logger.info("HomeProcessManagerImpl.java LNO ::916 , OTP service is down:: msg not send");
                json.put("status", "error");
                json.put("message", "OTP service is down");
                return json;
              } 
            } 
          } 
          application = this.homeLoanService.save(application);
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
            json.put("message", "OTP authentication successful");
          } 
          json.put("alertCount", alertCount);
          return json;
        } 
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == application.getAppDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtp|2");
        } 
        json.put("alertCount", alertCount);
        return json;
			} } 
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 748 :: processAddCoapplicant() ", e);
        try {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("HomeLoanProcessImpl.java LNo: 754 :: processAddCoapplicant() ", (Throwable)e1);
        } 
      } catch (JSONException e) {
      logger.info("HomeLoanProcessImpl.java LNo: 748 :: processAddCoapplicant() ", e);
      try {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
      } catch (JSONException e1) {
        logger.info("HomeLoanProcessImpl.java LNo: 754 :: processAddCoapplicant() ", (Throwable)e1);
      } 
    } 
    return json;
  }
  
  public JSONObject processMobileOTP(Integer appSeqId, Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, OtherRequest otherRequest) throws JSONException {
    JSONObject json = new JSONObject();
    String mobile = null;
    String isdCode = null;
    int appApplyingFrom = 0;
    int appOTPVerifyType = 0;
    if (otherRequest != null && otherRequest.getAppApplyingFrom() != null)
      appApplyingFrom = Integer.parseInt(otherRequest.getAppApplyingFrom()); 
    if (otherRequest != null && otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType()); 

    if (appApplyingFrom == 2) {
      if (otherRequest != null && otherRequest.getIsdCode() != null)
        isdCode = otherRequest.getIsdCode(); 
      if (otherRequest != null && otherRequest.getNriMobileNo() != null)
        mobile = otherRequest.getNriMobileNo(); 
    } else {
      isdCode = Constants.COUNTRY_CODE_INDIA;
      if (otherRequest != null && otherRequest.getMobile() != null)
        mobile = otherRequest.getMobile(); 
    } 
    String email = null;
    if (otherRequest != null && otherRequest.getEmail() != null)
      email = otherRequest.getEmail(); 
    String inputOtp = null;
    boolean isEmail=false;
    if (appOTPVerifyType == 0) {
      if (otherRequest != null && otherRequest.getInputOtp() != null)
        inputOtp = otherRequest.getInputOtp(); 
    } else if (appOTPVerifyType == 1 && 
      otherRequest != null && otherRequest.getInputOtpEmail() != null) {
    	isEmail=true;
      inputOtp = otherRequest.getInputOtpEmail();
    } 
    //logger.info("cheking the otherRequest.getAlternateMobileNumber() "+otherRequest.getAlternateMobileNumber());
    //logger.info("otherRequest.getIsdCodeAlt() "+otherRequest.getIsdCodeAlt());
	if (otherRequest.getAlternateMobileNumber() != null) {
		
		if (!otherRequest.getAlternateMobileNumber().equals(mobile)) {
			//logger.info("cheking the otherRequest.getAlternateMobileNumber() !=null "+otherRequest.getAlternateMobileNumber());
			json = OTP(appSeqId, stateId, null, appApplyingFrom, appOTPVerifyType, otherRequest.getIsdCodeAlt(),
					"ALT_" + otherRequest.getAlternateMobileNumber(), email, otherRequest.getInputOtpAlt(), bankLMSUserId, ajaxPostUrl);
			
		} else {
			json.put("status", "error");
			json.put("message", "Alternate Mobile cannot be same as Mobile number.");
			json.put("alertCount", 0);
		}
	} else {
		 if(isEmail) {
		//logger.info("cheking the otherRequest.getAlternateMobileNumber() ==null "+otherRequest.getAlternateMobileNumber());
		json = OTP(appSeqId, stateId, null, appApplyingFrom, appOTPVerifyType, isdCode,  mobile, email, "EML_" +inputOtp, bankLMSUserId, ajaxPostUrl);
		 }else {
		  json = OTP(appSeqId, stateId, null, appApplyingFrom, appOTPVerifyType, isdCode,  mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl);
 
		 }
		}

    return json;
  }
  
  public JSONObject processWantUsToCallYou(Integer appSeqId, Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, OtherRequest otherRequest) throws SQLException, JSONException {
    JSONObject json = new JSONObject();
    String name = null;
    String mobile = null;
    String isdCode = Constants.COUNTRY_CODE_INDIA;
    int appApplyingFrom = 0;
    int appOTPVerifyType = 0;
    if (otherRequest.getName() != null)
      name = otherRequest.getName(); 
    if (otherRequest.getAppApplyingFrom() != null)
      appApplyingFrom = Integer.parseInt(otherRequest.getAppApplyingFrom()); 
    if (otherRequest.getIsdCodeWantUsToCallYou() != null)
      isdCode = otherRequest.getIsdCodeWantUsToCallYou(); 
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType()); 
    if (otherRequest.getMobileWantUsToCallYou() != null)
      mobile = otherRequest.getMobileWantUsToCallYou(); 
    int count = this.commonService.getCallBackLeadCount(Integer.valueOf(1), isdCode, mobile);
    logger.info("HomeProcessManagerImpl.java LNo : 1034 count : " + count + " with AppSeqId " + appSeqId);
    if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
      json.put("status", "error");
      json.put("message", Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
      json.put("alertCount", 1);
      return json;
    } 
    String email = null;
    if (otherRequest.getEmail() != null)
      email = otherRequest.getEmail(); 
    String inputOtp = null;
    if (otherRequest.getInputOtpWantUs() != null) {
      inputOtp = otherRequest.getInputOtpWantUs();
    } else if (appOTPVerifyType == 1 && 
      otherRequest.getInputOtpWantUsEmail() != null) {
      inputOtp = otherRequest.getInputOtpWantUsEmail();
    } 
    json = OTP(appSeqId, stateId, name, appApplyingFrom, appOTPVerifyType, isdCode, mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl);
    return json;
  }
  
  public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormHomeLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      ApplicationFormHomeLoan application = null;
      if (appSeqId != null) {
        application = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
        if (application == null) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
          return loanScenarioBean;
        } 
        if (application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
          if (quote.getLoanQuoteAppFirstName() != null) {
            application.setAppFirstName(quote.getLoanQuoteAppFirstName()); 
          }
          
          if (quote.getLoanQuotePanCardNo() != null) {
              application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
            }
          
          if(quote.getLoanQuotePincode()!=null){
  			application.setAppPincode(quote.getLoanQuotePincode());
  		}
      	if(quote.getLoanQuoteAddress1()!=null){
			application.setAppAddress1(quote.getLoanQuoteAddress1());
		}
		if(quote.getLoanQuoteAddress2()!=null){
			application.setAppAddress2(quote.getLoanQuoteAddress2());
		}
		  if (quote.getLoanQuoteMiddleName() != null)
            application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
          if (quote.getLoanQuoteLastName() != null)
            application.setAppLastName(quote.getLoanQuoteLastName()); 
          if (quote.getLoanQuoteDateOfBirth() != null) {
            application.setAppDob(quote.getLoanQuoteDateOfBirth());
            application.setAppDobDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
          } 
          if (quote.getLoanQuoteAppWorkEmail() != null)
            application.setAppWorkEmail(quote.getLoanQuoteAppWorkEmail()); 
          if (quote.getLoanQuoteAppMobileNo() != null) {
            application.setAppMobileNo(quote.getLoanQuoteAppMobileNo());
          } 
        } 
      } 
      int oldVisitId = 0;
      if (application != null && application.getAppQuoteId() != null) {
        oldVisitId = this.homeLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
      } else {
        oldVisitId = trackVisitId.intValue();
      } 
      quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      
      if (appSeqId == null) {
    	  
    	  if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
      		return loanScenarioBean;
      	}

      	if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
      		return loanScenarioBean;
      	}
      	if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
      		return loanScenarioBean;
      	}
      }
      
      if (quote.getLoanQuoteProjectId() != null) {
        MasterProject masterProject = this.homeLoanService.findProjectById(quote.getLoanQuoteProjectId());
        if (masterProject != null)
          quote.setLoanQuoteBuilderName(masterProject.getProjectName()); 
      } 
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && (
        appSeqId == null || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS.intValue() && "Y".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())))) {
        String oldMobile = "";
        String isdCode = "";
        boolean isEligibleForBypass = false;
        if (appSeqId == null && quote.getLoanQuoteAppMobileNo() != null) {
          isdCode = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
          oldMobile = quote.getLoanQuoteAppMobileNo();
          isEligibleForBypass = true;
        } else if (application != null && application.getAppMobileNo() != null) {
          isdCode = (application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode();
          oldMobile = application.getAppMobileNo();
          isEligibleForBypass = true;
        } 
        if (isEligibleForBypass && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0"))
          if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
            boolean isLeadExists = false;
            int leadLoanPurposeId = 0;
            if (quote.getLoanQuoteLoanPurposeId().intValue() == 1 || quote.getLoanQuoteLoanPurposeId().intValue() == 2 || quote.getLoanQuoteLoanPurposeId().intValue() == 3 || quote.getLoanQuoteLoanPurposeId().intValue() == 4) {
              leadLoanPurposeId = 1;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 5) {
              leadLoanPurposeId = 2;
            } 
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.HOME_LOAN_ID, Integer.valueOf(leadLoanPurposeId), isdCode, oldMobile, loanTypeId);
            logger.info("HLProcessImpl.java LNo 934 :: isLeadExists setting in session " + isLeadExists + " with AppSeqId " + appSeqId);
            if (isLeadExists) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 942:: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 949 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 951 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
          } else {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 955 :: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 962 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 964 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
          }  
      } 
      


      //validations for address fields
      //loanQuoteAddress1
      
      
     if(!ValidatorUtil.validateFirstNameLength(quote.getLoanQuoteAppFirstName())) {
      loanScenarioBean.setStatus(Integer.valueOf(0));
   	  loanScenarioBean.setMessage("Please enter between 2 to 20 characters in First name.");
   	  return loanScenarioBean;
     }
     if(!ValidatorUtil.validateMiddleNameLength(quote.getLoanQuoteMiddleName())) {
    	 loanScenarioBean.setStatus(Integer.valueOf(0));
    	 loanScenarioBean.setMessage("Please enter between 2 to 20 characters in Middle name.");
    	 return loanScenarioBean;
     }
     if(!ValidatorUtil.validateLastNameLength(quote.getLoanQuoteLastName())) {
    	 loanScenarioBean.setStatus(Integer.valueOf(0));
    	 loanScenarioBean.setMessage("Please enter between 2 to 20 characters in Last name.");
    	 return loanScenarioBean;
     }
  
  	if (!ValidatorUtil.validateFirstName(quote.getLoanQuoteAppFirstName())) {
	      
      loanScenarioBean.setStatus(Integer.valueOf(0));
	  loanScenarioBean.setMessage("First name is not in correct format. Please enter only [a-z].");
	  return loanScenarioBean;
  }
	if (!ValidatorUtil.validateMiddleName(quote.getLoanQuoteMiddleName())) {
		
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Middle name is not in correct format. Please enter only [a-z]. ");
		return loanScenarioBean;
	}
	if (!ValidatorUtil.validateLastName(quote.getLoanQuoteLastName())) {
		
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Last name is not in correct format. Please enter only [a-z] & do not include spaces.");
		return loanScenarioBean;
	}

	//added for same name validation
	if (quote.getLoanQuoteAppFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || 
			(quote.getLoanQuoteMiddleName()!=null && 
				(quote.getLoanQuoteMiddleName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || quote.getLoanQuoteAppFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteMiddleName().trim())))) {
		
		  loanScenarioBean.setStatus(Integer.valueOf(0));
		  loanScenarioBean.setMessage("For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
		  return loanScenarioBean;
	}
	
	if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress1().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 1");
    	  return loanScenarioBean;
      }
      //if (!(quote.getLoanQuoteAddress1() != null && quote.getLoanQuoteAddress1().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
      if (!(quote.getLoanQuoteAddress1() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress1()))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 1");
          return loanScenarioBean;
      }
   
      
      if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress2().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 2");
    	  return loanScenarioBean;
      }
      
      //if (!(quote.getLoanQuoteAddress2() != null && quote.getLoanQuoteAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
      if (!(quote.getLoanQuoteAddress2() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress2()))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 2");
          return loanScenarioBean;
      }
      
      if(quote.getLoanQuoteAddressLandmark() != null) {
      if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddressLandmark().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in  Landmark");
    	  return loanScenarioBean;
       }
      }
      
      if(quote.getLoanQuoteAddressLandmark() != null) {
	      if (!quote.getLoanQuoteAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
	    	  loanScenarioBean.setStatus(Integer.valueOf(0));
	          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in  Landmark  ");
	          return loanScenarioBean;
	      }
      }
      if (quote.getLoanQuoteStateId() == null || (quote.getLoanQuoteStateId() != null && quote.getLoanQuoteStateId() == 0)) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please select State");
          return loanScenarioBean;
      }
      
      if ((quote.getLoanQuoteStateId() != null && quote.getLoanQuoteStateId() != 0) && 
    		  (quote.getLoanQuoteCityId() == null || (quote.getLoanQuoteCityId() != null && quote.getLoanQuoteCityId() == 0))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please select City");
          return loanScenarioBean;
      }
      
      boolean isPinCodeValidForState = false;
      if (quote.getLoanQuotePincode() != null) {
    	  int pincodeInitial = quote.getLoanQuotePincode().intValue() / 10000;
		  String pinlastfix = quote.getLoanQuotePincode().toString().substring(3, 6);
		  MasterState state = this.commonService.getStateById((quote.getLoanQuoteStateId() != null) ? quote.getLoanQuoteStateId() : null);
		  if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
			  if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
				  isPinCodeValidForState = true;
		      } else {
		    	  isPinCodeValidForState = false;
		      }
		  }
      }

      if (!isPinCodeValidForState) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Entered pincode does not belong to entered state.");
          return loanScenarioBean;
      }


      if (quote.getLoanQuotePanCardNo() != null && !ValidatorUtil.isValidPanNo(quote.getLoanQuotePanCardNo() )) {
      	loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("PAN is not in correct format.|2");
			return loanScenarioBean;
		}
      if (quote.getLoanQuoteLoanAmountTaken() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteLoanAmountTaken() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Requested loan Amount cannot be greater than 9 digits.|2");
    	  return loanScenarioBean;
      }
      if (quote.getLoanQuoteGender() != null && !ValidatorUtil.isGender(quote.getLoanQuoteGender() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Gender is not in correct format.|2");
    	  return loanScenarioBean;
      }
    
      if (quote.getAppMobile() != null && !ValidatorUtil.isValidMobile(quote.getAppMobile() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Mobile number is not in correct format.|2");
    	  return loanScenarioBean;
      }
      
      if (quote.getAppEmail() != null && !ValidatorUtil.isValidEmail(quote.getAppEmail() )) {
        	loanScenarioBean.setStatus(Integer.valueOf(0));
  			loanScenarioBean.setMessage("Email is not in correct format.|2");
  			return loanScenarioBean;
  		}
      
      if (quote.getLoanQuoteAppWorkEmail() != null && !ValidatorUtil.isValidEmail(quote.getLoanQuoteAppWorkEmail() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Email is not in correct format.|2");
    	  return loanScenarioBean;
      }
     
      quote = this.homeLoanHelper.insertLoanQuote(quote, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, trackVisitId);
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
      int previousQuoteId = (application != null && application.getAppQuoteId() != null) ? application.getAppQuoteId().intValue() : 0;
      application = this.homeLoanHelper.insertAppLoan(quote, application, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null, Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null) ? bankLmsUser.getLmsUserIntermediaryId().intValue() : 0));
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
      if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 5) {
        if (Constants.HOME_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl)) {
          SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
        } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
          SessionUtil.setHomeLoanTopupApplicationSequenceId(application.getAppSeqId());
        } else {
          SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
        } 
      } else {
        SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
      } 
      if (SessionUtil.getApplicationCRMLeadId() != null)
        application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId()); 
      if (isAppFoundForDedupInDropRejectStage)
        application.setAppMobileDedup(Integer.valueOf(0)); 
      if (isAppFoundForDedupInDropOffStage)
        application.setAppMobileDedup(Integer.valueOf(1)); 
      quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(quote.getLoanQuoteId());
      if (application.getAppQuoteId() != null) {
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && 
          quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 4 && 
          quote.getLoanQuotePrferredBranchId() != null) {
          MasterBranch branch = this.commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
          if (branch != null && branch.getBranchIsRACPC() != null && branch.getBranchIsRACPC().intValue() == 0) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
            return loanScenarioBean;
          } 
        } 
        
        if (!(application.getAppMobileVerified() != null && application.getAppMobileVerified().equalsIgnoreCase("Y"))) {
        	boolean ccmsWriteStatus = writePrivacyConsentToCCMS(application, quote, loanScenarioBean);
			ccmsWriteStatus=true;
			SessionUtil.setConsentSubmitNTBHome("true");
			if (!ccmsWriteStatus) {
				return loanScenarioBean;
			}
        }

        loanScenarioBean = this.homeLoanHelper.callBRE(application, quote, bankLmsUser, Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
        if (loanScenarioBean.getStatus().intValue() != 1)
          return loanScenarioBean; 
        application = loanScenarioBean.getApplicationHL();
        if (Constants.HOME_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl) && 
          application != null && 
          SessionUtil.getApplicationCRMLeadId() != null) {
          CRMRequest crmRequest = new CRMRequest();
          crmRequest.setCrmLeadId(SessionUtil.getApplicationCRMLeadId());
          crmRequest.setReferenceNumber(application.getAppSeqId());
          crmRequest.setApplicantReferenceId(application.getAppReferenceId());
          crmRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
        } 
        return loanScenarioBean;
      } 
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      return loanScenarioBean;
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 1386 :: processGetQuotes() ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      }  catch (ParseException e) {
      logger.info("HomeLoanProcessImpl.java LNo: 1386 :: processGetQuotes() ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      return loanScenarioBean;
    } 
  }
  
  public ApplicationFormHomeLoan processSubmitQuote(Integer appSeqId, Integer requestIndex, ApplicationFormHomeLoan appForm, String isDsrPage, Integer bankLMSUserId) throws SQLException, NullPointerException, RuntimeException, ParseException {
    if (appSeqId == null)
      return null; 
    ApplicationFormHomeLoan appFormData = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
    if (appFormData == null)
      return null; 
    if (isDsrPage.equalsIgnoreCase("false") && (
      appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK)) && 
      appFormData.getAppMobileVerified().equalsIgnoreCase("N") && appFormData.getAppEmailVerified().equalsIgnoreCase("N")) {
      appFormData.setError("Mobile OTP is not verified");
      return appFormData;
    } 
    ApplicationFormHomeLoanQuote quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appFormData.getAppQuoteId());
    if (quote == null)
      return null; 
    logger.info("HLProcessImpl.java :: LNo :: 1394 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()) && 
      !Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && 
      appFormData.getAppMobileNo() != null && !Constants.DUMMY_MOBILE_NO.contains(appFormData.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
      boolean isAppFoundForDedupInApplicationStage = false;
      isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage(null, (appFormData.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : appFormData.getAppISDCode(), appFormData.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId());
      logger.info("HLProcessImpl.java :: LNo 1416 :: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
      if (isAppFoundForDedupInApplicationStage) {
        this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE6_ID, null, null, true);
        appFormData.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE6_ID));
        appFormData.setError(Constants.APP_DEDUPLICATION_MESSAGE);
        return appFormData;
      } 
    } 
    
	Year currentYear = Year.now();
	Year yearMinus7 = currentYear.minusYears(7);
	Integer yearMinus7Int = Integer.parseInt(yearMinus7.toString());
	
	if(!appForm.getAppCompanyJoiningYear().equals(yearMinus7Int)) {
		Integer employmentType = appForm.getAppEmploymentType();
		if (employmentType!= null && (appForm.getAppCompanyJoiningMonth() == 0 || appForm.getAppCompanyJoiningYear() == 0)) {
			if (employmentType==1) {
				 appFormData.setError("Applicant Joining Date not in correct format.|appForm.appOfficePincode");
		     
				
			} else if (employmentType==2 ||employmentType==3) {
				 appFormData.setError("Applicant Joining Date not in correct format.|appForm.appOfficePincode");
			}
			return appFormData;
	    }
	}
    
//	logger.info("first_name :: " + appForm.getAppFirstName());
//	logger.info("middle_name :: " + appForm.getAppMiddleName());
//	logger.info("last_name :: " + appForm.getAppLastName());
	
	 if (!validateLeadName(appForm.getAppFirstName(), appForm.getAppMiddleName(), appForm.getAppLastName())) {
	    	appFormData.setError("Name is not in correct format.|2");
			return appFormData;
	 }
	
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
    if (appForm.getAppFirstName() != null)
      appFormData.setAppFirstName(StringUtil.capitalize(appForm.getAppFirstName())); 
    if (appForm.getAppMiddleName() != null)
      appFormData.setAppMiddleName(StringUtil.capitalize(appForm.getAppMiddleName())); 
    if (appForm.getAppLastName() != null)
      appFormData.setAppLastName(StringUtil.capitalize(appForm.getAppLastName())); 
    if (appForm.getAppGender() != null)
      appFormData.setAppGender(appForm.getAppGender()); 
    if (appForm.getAppAddress1() != null)
      appFormData.setAppAddress1(StringUtil.capitalize(appForm.getAppAddress1())); 
    if (appForm.getAppAddress2() != null)
      appFormData.setAppAddress2(StringUtil.capitalize(appForm.getAppAddress2())); 
    if (appForm.getAppAddressLandmark() != null)
      appFormData.setAppAddressLandmark(StringUtil.capitalize(appForm.getAppAddressLandmark())); 
    if (appForm.getAppCustomerSignature() != null)
      appFormData.setAppCustomerSignature(appForm.getAppCustomerSignature()); 
    if (appForm.getAppStateId() != null)
      appFormData.setAppStateId(appForm.getAppStateId()); 
    if (appForm.getAppDistrictId() != null)
      appFormData.setAppDistrictId(appForm.getAppDistrictId()); 
    if (appForm.getAppCityId() != null)
      appFormData.setAppCityId(appForm.getAppCityId()); 
    if (appForm.getAppCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue() && 
      appForm.getAppDistrictId() == null) {
      appFormData.setError("Invalid params");
      return appFormData;
    } 
    if (appForm.getAppBranchId() != null && appForm.getAppBranchId().intValue() > 0) {
      appFormData.setAppBranchId(appForm.getAppBranchId());
    } else if (quote.getLoanQuotePrferredBranchId() != null && quote.getLoanQuotePrferredBranchId().intValue() > 0) {
      appFormData.setAppBranchId(quote.getLoanQuotePrferredBranchId());
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
    appFormData.setAppResidenceType(appForm.getAppResidenceType());
    if (appForm.getAppLoanEmployerName() != null)
      appFormData.setAppLoanEmployerName(appForm.getAppLoanEmployerName()); 
    String stdate = "";
    if (appForm.getAppCompanyJoiningMonth() != null && appForm.getAppCompanyJoiningYear() != null) {
      stdate = "01-" + ((appForm.getAppCompanyJoiningMonth().intValue() <= 12) ? appForm.getAppCompanyJoiningMonth().intValue() : 12) + "-" + appForm.getAppCompanyJoiningYear();
      appFormData.setAppCompanyJoiningDate(DateUtil.convertStringToDate(stdate));
    } 
    if (appForm.getAppCompanyJoiningMonth() == null && appForm.getAppCompanyJoiningYear() != null) {
      stdate = "01-01-" + appForm.getAppCompanyJoiningYear();
      appFormData.setAppCompanyJoiningDate(DateUtil.convertStringToDate(stdate));
    } 
    appFormData.setAppCompanyJoiningMonth(appForm.getAppCompanyJoiningMonth());
    if (appForm.getAppCompanyJoiningYear() != null)
      appFormData.setAppCompanyJoiningYear(appForm.getAppCompanyJoiningYear()); 
    if (appForm.getAppWorkExperienceYear() != null)
      appFormData.setAppWorkExperienceYear(appForm.getAppWorkExperienceYear()); 
    appFormData.setAppWorkExperienceMonth(appForm.getAppWorkExperienceMonth());
    if (appForm.getAppPropertyAddress1() != null)
      appFormData.setAppPropertyAddress1(StringUtil.capitalize(appForm.getAppPropertyAddress1())); 
    if (appForm.getAppPropertyAddress2() != null)
      appFormData.setAppPropertyAddress2(StringUtil.capitalize(appForm.getAppPropertyAddress2())); 
    appFormData.setAppPropertyAddressLandmark(appForm.getAppPropertyAddressLandmark());
    if (appForm.getAppPropertyStateId() != null)
      appFormData.setAppPropertyStateId(appForm.getAppPropertyStateId()); 
    if (appForm.getAppPropertyCityId() != null)
      appFormData.setAppPropertyCityId(appForm.getAppPropertyCityId()); 
    if (appForm.getAppPropertyDistrictId() != null)
      appFormData.setAppPropertyDistrictId(appForm.getAppPropertyDistrictId()); 
    if (appForm.getAppPropertyPincode() != null) {
      int pincodeInitial = appForm.getAppPropertyPincode().intValue() / 10000;
      String pinlastfix = appForm.getAppPropertyPincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppPropertyStateId() != null) ? appForm.getAppPropertyStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppPropertyPincode(appForm.getAppPropertyPincode());
        } else {
          appFormData.setError("Entered property pincode does not belong to entered state.|appForm.appPropertyPincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppPropertyPincode(appForm.getAppPropertyPincode());
      } 
    } 
    if (appForm.getAppOfficeAddress1() != null)
      appFormData.setAppOfficeAddress1(StringUtil.capitalize(appForm.getAppOfficeAddress1())); 
    if (appForm.getAppOfficeAddress2() != null)
      appFormData.setAppOfficeAddress2(StringUtil.capitalize(appForm.getAppOfficeAddress2())); 
    if (appForm.getAppOfficeStateId() != null)
      appFormData.setAppOfficeStateId(appForm.getAppOfficeStateId()); 
    if (appForm.getAppOfficeCityId() != null)
      appFormData.setAppOfficeCityId(appForm.getAppOfficeCityId()); 
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
          appFormData.setError("Entered Office pincode does not belong to entered state.|appForm.appOfficePincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppOfficePincode(appForm.getAppOfficePincode());
      } 
    } 
    appFormData.setAppOfficePhoneNumber(appForm.getAppOfficePhoneNumber());
    appFormData.setAppOfficePhoneStdCode(appForm.getAppOfficePhoneStdCode());
    appFormData.setAppPanCardNo(appForm.getAppPanCardNo());
    appFormData.setAppEverDefaultedCreditCard(appForm.getAppEverDefaultedCreditCard());
    if (appForm.getAppCoapplicantFirstName_1() != null)
      appFormData.setAppCoapplicantFirstName_1(StringUtil.capitalize(appForm.getAppCoapplicantFirstName_1())); 
    if (appForm.getAppCoapplicantMiddleName1() != null)
      appFormData.setAppCoapplicantMiddleName1(StringUtil.capitalize(appForm.getAppCoapplicantMiddleName1())); 
    if (appForm.getAppCoapplicantLastName_1() != null)
      appFormData.setAppCoapplicantLastName_1(StringUtil.capitalize(appForm.getAppCoapplicantLastName_1())); 
    if (appForm.getAppCoapplicantAddress_1_1() != null)
      appFormData.setAppCoapplicantAddress_1_1(StringUtil.capitalize(appForm.getAppCoapplicantAddress_1_1())); 
    if (appForm.getAppCoapplicantAddress_2_1() != null)
      appFormData.setAppCoapplicantAddress_2_1(StringUtil.capitalize(appForm.getAppCoapplicantAddress_2_1())); 
    if (appForm.getAppCoapplicantLandmark_1() != null)
      appFormData.setAppCoapplicantLandmark_1(StringUtil.capitalize(appForm.getAppCoapplicantLandmark_1())); 
    if (appForm.getAppCoapplicantState_id_1() != null)
      appFormData.setAppCoapplicantState_id_1(appForm.getAppCoapplicantState_id_1()); 
    if (appForm.getAppCoapplicantCity_id_1() != null)
      appFormData.setAppCoapplicantCity_id_1(appForm.getAppCoapplicantCity_id_1()); 
    if (appForm.getAppCoapplicantDistrictId1() != null)
      appFormData.setAppCoapplicantDistrictId1(appForm.getAppCoapplicantDistrictId1()); 
    if (appForm.getAppCoapplicantPincode_1() != null) {
      int pincodeInitial = appForm.getAppCoapplicantPincode_1().intValue() / 10000;
      String pinlastfix = appForm.getAppCoapplicantPincode_1().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppCoapplicantState_id_1() != null) ? appForm.getAppCoapplicantState_id_1() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppCoapplicantPincode_1(appForm.getAppCoapplicantPincode_1());
        } else {
          appFormData.setError("Entered first Co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode_1");
          return appFormData;
        } 
      } else {
        appFormData.setAppCoapplicantPincode_1(appForm.getAppCoapplicantPincode_1());
      } 
    } 
    appFormData.setCloneCoapplicantAddress1(appForm.isCloneCoapplicantAddress1());
    if (appForm.getAppCoapplicantFirstName_2() != null)
      appFormData.setAppCoapplicantFirstName_2(StringUtil.capitalize(appForm.getAppCoapplicantFirstName_2())); 
    if (appForm.getAppCoapplicantMiddleName2() != null)
      appFormData.setAppCoapplicantMiddleName2(StringUtil.capitalize(appForm.getAppCoapplicantMiddleName2())); 
    if (appForm.getAppCoapplicantLastName_2() != null)
      appFormData.setAppCoapplicantLastName_2(StringUtil.capitalize(appForm.getAppCoapplicantLastName_2())); 
    if (appForm.getAppCoapplicantAddress_1_2() != null)
      appFormData.setAppCoapplicantAddress_1_2(StringUtil.capitalize(appForm.getAppCoapplicantAddress_1_2())); 
    if (appForm.getAppCoapplicantAddress_2_2() != null)
      appFormData.setAppCoapplicantAddress_2_2(StringUtil.capitalize(appForm.getAppCoapplicantAddress_2_2())); 
    if (appForm.getAppCoapplicantLandmark_2() != null)
      appFormData.setAppCoapplicantLandmark_2(StringUtil.capitalize(appForm.getAppCoapplicantLandmark_2())); 
    if (appForm.getAppCoapplicantState_id_2() != null)
      appFormData.setAppCoapplicantState_id_2(appForm.getAppCoapplicantState_id_2()); 
    if (appForm.getAppCoapplicantCity_id_2() != null)
      appFormData.setAppCoapplicantCity_id_2(appForm.getAppCoapplicantCity_id_2()); 
    if (appForm.getAppCoapplicantDistrictId2() != null)
      appFormData.setAppCoapplicantDistrictId2(appForm.getAppCoapplicantDistrictId2()); 
    if (appForm.getAppCoapplicantPincode_2() != null) {
      int pincodeInitial = appForm.getAppCoapplicantPincode_2().intValue() / 10000;
      String pinlastfix = appForm.getAppCoapplicantPincode_2().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppCoapplicantState_id_2() != null) ? appForm.getAppCoapplicantState_id_2() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppCoapplicantPincode_2(appForm.getAppCoapplicantPincode_2());
        } else {
          appFormData.setError("Entered second Co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode_2");
          return appFormData;
        } 
      } else {
        appFormData.setAppCoapplicantPincode_2(appForm.getAppCoapplicantPincode_2());
      } 
    } 
    appFormData.setCloneCoapplicantAddress2(appForm.isCloneCoapplicantAddress2());
    appFormData.setClonePermanentAddress(appForm.isClonePermanentAddress());
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
          appFormData.setError("Entered permanent address pincode does not belong to entered State.|appForm.appPermanentPincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppPermanentPincode(appForm.getAppPermanentPincode());
      } 
    } 
    if (appFormData.getAppSalesEnteredTime() != null)
      appFormData.setAppSalesEnteredTime(new Date()); 
    if (appFormData.getAppFilledAt() == null) {
      appFormData.setAppFilledAt(new Date());
      if (appFormData.getAppAlertStatusType() != null)
        appFormData.setAppAlertStatusType(null); 
    } 
    appFormData.setAppPanCardLater(appForm.getAppPanCardLater());
    if (appForm.getAppHaveAadhaarNumber() != null)
      appFormData.setAppHaveAadhaarNumber(appForm.getAppHaveAadhaarNumber()); 
    appFormData.setAppOtherId(appForm.getAppOtherId());
    appFormData.setAppOtherIdNumber(appForm.getAppOtherIdNumber());
    appFormData.setAppInterestedSbiLifeInsurance(appForm.getAppInterestedSbiLifeInsurance());
    appFormData.setAppPanCardVerified("N");
    boolean isBankLmsContactCenterId = this.commonService.getBankLmsUserRole(bankLMSUserId);
    logger.info(" HomeProcessImpl.java Lno :: 2455 :: isBankLmsContactCenterId : " + isBankLmsContactCenterId + " bankLMSUserId : " + bankLMSUserId + " with AppSeqId " + appSeqId);
    String identityvalidation = this.SbiUtil.getIdentityValidation(appForm.getAppPanCardLater(), appForm.getAppPanCardNo(), appForm.getAppHaveAadhaarNumber(), appForm.getAppOtherId(), appForm.getAppOtherIdNumber(), isBankLmsContactCenterId);
    if (!"pass".equalsIgnoreCase(identityvalidation)) {
      appFormData.setError(identityvalidation);
      return appFormData;
    } 
    if (!ValidatorUtil.isValid(appFormData.getAppBranchId())) {
      appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
      return appFormData;
    } 
 		if (appForm.getAppAlternateMobileNumber() != null && !"".equals(appForm.getAppAlternateMobileNumber())
 				&& !ValidatorUtil.isValidAlternateMobile(appForm.getAppAlternateMobileNumber())) {
 			appFormData.setError("Please enter valid alternate Mobile Number.|");
 			return appFormData;
 		}
 		else if (isDsrPage.equalsIgnoreCase("false") && appForm.getAppAlternateMobileNumber() != null && !"".equals(appForm.getAppAlternateMobileNumber())
 				&& appForm.getAppAltMobileVerified() == null ||  (appForm.getAppAltMobileVerified() != null && appForm.getAppAltMobileVerified().equalsIgnoreCase("N"))) {
 			appFormData.setError("Please verify alternate Mobile Number.|");
 			return appFormData;
 		}
 		
// 		logger.info("alternate mobile number appForm.getAppAlternateMobileNumber() "+appForm.getAppAlternateMobileNumber());
		if (appForm.getAppAlternateMobileNumber() != null  && isDsrPage.equalsIgnoreCase("true"))
		{ 
			appFormData.setAppAltISDCode(appForm.getAppAltISDCode());
			appFormData.setAppAlternateMobileNumber(appForm.getAppAlternateMobileNumber());	
			//logger.info("alternate mobile number appForm.getAppAlternateMobileNumber() 1 "+appForm.getAppAlternateMobileNumber());
		}

    if (appForm.getAppCustomerSignature() != null)
      appFormData.setAppCustomerSignature(appForm.getAppCustomerSignature()); 
    if (appForm.getAppRelationshipWithBank() != null)
      appFormData.setAppRelationshipWithBank(appForm.getAppRelationshipWithBank()); 
    if (appForm.getAppModeOfRepayment() != null)
      appFormData.setAppModeOfRepayment(appForm.getAppModeOfRepayment()); 
    if (appForm.getAppNoOfYearsCurrentAdd() != null)
      appFormData.setAppNoOfYearsCurrentAdd(appForm.getAppNoOfYearsCurrentAdd()); 
    if (appForm.getAppSchemePmay() != null)
      appFormData.setAppSchemePmay(appForm.getAppSchemePmay()); 
    appFormData = this.homeLoanService.save(appFormData);
    logger.info("HLProcessImpl.java :: LNo :: 1665 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_DATA_SAVED, null, false);
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
      apiInputParams.setApiProductId(Constants.HOME_LOAN_ID);
      logger.info("HLProcessImpl.java :: LNo :: 1581 appSeqId===" + appFormData.getAppSeqId() + " calling pan service callPanServiceApi()");
      PanApiReturnResponse panApiResponseObj = this.panServiceAction.callPanServiceApi(apiInputParams);
      logger.info("HLProcessImpl.java :: LNo :: 1581 appSeqId===" + appFormData.getAppSeqId() + "===response pan service status id===" + panApiResponseObj.getApiStatusId() + "===Api Error===" + panApiResponseObj.getApiErrors());
      if (panApiResponseObj.getApiStatusId().intValue() == 1)
        nsdlPANStatus = 1; 
    } 
    logger.info("HLProcessImpl.java :: LNo :: 1684 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (nsdlPANStatus == 1) ? Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS : Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL, null, false);
    if (nsdlPANStatus == 1) {
      appFormData.setAppPanCardVerified("Y");
      appFormData.setAppPanVerifiedDatetime(new Date());
      appFormData.setAppRSMdecision(Integer.valueOf(1));
    } else {
      appFormData.setAppRSMdecision(Integer.valueOf(3));
    } 
    if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() != 4) {
      Integer count = null;
      if (appFormData.getAppSeqId() != null) {
        logger.info("Checkinfo if Cibil request already made for App SEQ Id :: " + appFormData.getAppSeqId());
        count = this.commonService.getCountByAppIdAndLoanType(appFormData.getAppSeqId().toString(), "A06", "SUCCESS");
      } 
      if (count != null && count <= 0)
          if (appFormData != null) {
            boolean isContactCenterUser = false;
            if (isDsrPage.equalsIgnoreCase("true"))
              isContactCenterUser = this.commonService.getBankLmsUserRole(bankLMSUserId); 
            logger.info("HomeProcessManagerImpl.java LNo: 1588 :: Calling :: AppCibilScore " +appForm.getAppCibilScore()+ " :: isCibilBypass :: " + isContactCenterUser + " with AppSeqId " + appSeqId);
            logger.info("HomeProcessManagerImpl.java LNo: 1773 :: Calling :: appFormData.getAppCibilScore() :: " + appFormData.getAppCibilScore() + " with AppSeqId " + appSeqId);
          } 
    } 
    String engineRequest = null;
    String engineResponse = null;
    if (!appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP) && 
      quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 4)
      try {
        quote = this.homeLoanHelper.getUpdateQuote(quote);
        JSONObject quoteData = JSONUtil.beanObjectToJSONObjct(quote);
        quoteData = this.SbiUtil.getDBCredentialForHelper(quoteData);
        boolean appNoOfCoApplicantsSet1 = false;
        boolean appNoOfCoApplicantsSet2 = false;
        boolean appNoOfCoApplicantsSet3 = false;
        if (quote.getLoanQuoteCoapplicantFirstRelationshipId() != null) {
          if ((quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 5) || 
            quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 6 || quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 7) {
            if (quote.getLoanQuoteCoapplicantFirstNetMonthlyPension() != null || quote.getLoanQuoteCoapplicantFirstOtherIncome() != null || 
              quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource() != null) {
              appNoOfCoApplicantsSet3 = true;
            } else {
              appNoOfCoApplicantsSet2 = true;
            } 
          } else if ((quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 1) || 
            quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 2 || quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 3 || 
            quote.getLoanQuoteCoapplicantFirstEmploymentTypeId().intValue() == 4) {
            appNoOfCoApplicantsSet3 = true;
          } else {
            appNoOfCoApplicantsSet1 = true;
          } 
          if (quote.getLoanQuoteCoapplicantSecondRelationshipId() != null)
            if ((quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 5) || 
              quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 6 || quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 7) {
              if (quote.getLoanQuoteCoapplicantSecondNetMonthlyPension() != null || quote.getLoanQuoteCoapplicantSecondOtherIncome() != null || 
                quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource() != null) {
                if (appNoOfCoApplicantsSet2) {
                  appNoOfCoApplicantsSet2 = false;
                  appNoOfCoApplicantsSet3 = true;
                } else if (appNoOfCoApplicantsSet3) {
                  appNoOfCoApplicantsSet3 = true;
                  appNoOfCoApplicantsSet2 = false;
                } else {
                  appNoOfCoApplicantsSet3 = true;
                } 
              } else if (appNoOfCoApplicantsSet3) {
                appNoOfCoApplicantsSet3 = true;
                appNoOfCoApplicantsSet2 = false;
                appNoOfCoApplicantsSet1 = false;
              } else {
                appNoOfCoApplicantsSet2 = true;
              } 
            } else if ((quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() != null && quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 1) || 
              quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 2 || quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 3 || 
              quote.getLoanQuoteCoapplicantSecondEmploymentTypeId().intValue() == 4) {
              if (appNoOfCoApplicantsSet2) {
                appNoOfCoApplicantsSet2 = false;
                appNoOfCoApplicantsSet3 = true;
              } else if (appNoOfCoApplicantsSet3) {
                appNoOfCoApplicantsSet3 = true;
                appNoOfCoApplicantsSet2 = false;
              } else {
                appNoOfCoApplicantsSet3 = true;
              } 
            } else {
              appNoOfCoApplicantsSet1 = true;
            }  
        } else {
          appNoOfCoApplicantsSet1 = true;
        } 
        if (appNoOfCoApplicantsSet1) {
          quoteData.put("appNoOfApplicant", 1);
        } else if (appNoOfCoApplicantsSet2) {
          quoteData.put("appNoOfApplicant", 2);
        } else if (appNoOfCoApplicantsSet3) {
          quoteData.put("appNoOfApplicant", 3);
        } 
        logger.info("HLProcessImpl.java :: LNo :: 2694 appNoOfApplicant1 :: " + appNoOfCoApplicantsSet1 + " with AppSeqId " + appSeqId);
        logger.info("HLProcessImpl.java :: LNo :: 2695 appNoOfApplicant2 :: " + appNoOfCoApplicantsSet2 + " with AppSeqId " + appSeqId);
        logger.info("HLProcessImpl.java :: LNo :: 2696 appNoOfApplicant3 :: " + appNoOfCoApplicantsSet3 + " with AppSeqId " + appSeqId);
        JSONObject appData = JSONUtil.beanObjectToJSONObjct(appFormData);
        appData.put("appOfferJsonData", "");
        appData.put("appRSMResponse", "");
        if (appForm.getAppWorkExperienceYear() != null) {
          appData.put("appWorkExprerienceYear", appForm.getAppWorkExperienceYear().intValue() - 1);
          if (appForm.getAppWorkExperienceYear().intValue() == 1) {
            appData.put("appWorkExprerienceMonth", 11);
          } else if (appForm.getAppWorkExperienceYear().intValue() == 12) {
            appData.put("appWorkExprerienceMonth", 0);
          } else if (appForm.getAppWorkExperienceMonth() != null) {
            appData.put("appWorkExprerienceMonth", appForm.getAppWorkExperienceMonth().intValue() - 1);
          } 
        } 
        if (Constants.IS_ENGINE_OBF) {
          JSONObject finalQuoteData = MapperUtil.convertHomeLoan(quoteData);
          JSONObject finalAppData = MapperUtil.convertHomeLoanApplication(appData);
          engineRequest = "rsmRequest={\"engineRequest\":" + finalQuoteData.toString() + ",\"applicationFormRequest\":" + finalAppData.toString() + "}";
        } else {
          engineRequest = "rsmRequest={\"engineRequest\":" + quoteData.toString() + ",\"applicationFormRequest\":" + appData.toString() + "}";
        } 
        JSONObject appRSMResponse = this.commonEngine.callingRSMEngine(engineRequest, Constants.HOME_LOAN_ID);
        engineResponse = appRSMResponse.toString();
        RSMResponse rsmBean = new RSMResponse();
        rsmBean = (RSMResponse)JSONUtil.getObjctFromJSON(rsmBean, appRSMResponse.toString());
        logger.info("HLProcessImpl.java :: LNo :: 2715 pre-rsmDecision :: " + rsmBean.getRsmDecision() + " with AppSeqId " + appSeqId);
        logger.info("HomeLoanProcessImpl.java LNo: 2716 :: RSM Calling rsmBean" + rsmBean + " with AppSeqId " + appSeqId);
        float rsmScoreFinal = Math.round(rsmBean.getRsmScore() * 100.0F / 90.0F);
        int rsmDecision = this.commonService.getRSMDecision((int)rsmScoreFinal, appFormData.getAppHomeLoanId().intValue(), Constants.HOME_LOAN_ID.intValue());
        logger.info("HLProcessImpl.java :: LNo :: 2719 final-rsmDecision :: " + rsmDecision + " with AppSeqId " + appSeqId);
        if (rsmBean != null)
          if (rsmBean.getRsmStatus() == 1 || rsmBean.getRsmStatus() == 3) {
            appFormData.setAppRSMResponse(appRSMResponse.toString());
            appFormData.setAppRSMStatus(Integer.valueOf(rsmBean.getRsmStatus()));
            appFormData.setAppRSMScore(Float.valueOf(rsmScoreFinal));
            logger.info("HLProcessImpl.java :: LNo :: 2726 pre-rsmScore :: " + rsmBean.getRsmScore() + " with AppSeqId " + appSeqId);
            logger.info("HLProcessImpl.java :: LNo :: 2727 final-rsmScore :: " + rsmScoreFinal + " with AppSeqId " + appSeqId);
            appFormData.setAppRSMdecision(Integer.valueOf(rsmDecision));
            appFormData.setAppRSMGrade(Integer.valueOf(rsmBean.getRsmGrade()));
          } else {
            appFormData.setAppRSMdecision(Integer.valueOf(rsmDecision));
            appFormData.setAppRSMStatus(Integer.valueOf(rsmBean.getRsmStatus()));
            appFormData.setAppRSMResponse(rsmBean.getMessage());
          }  
        this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (rsmBean == null) ? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON : ((rsmBean.getRsmDecision() == 0 || rsmBean.getRsmDecision() == 2) ? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_19 : Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_147), null, false);
      } catch (NullPointerException e) {
          logger.info("HomeLoanProcessImpl.java LNo: 1917 :: RSM Calling ", e);
          appFormData.setAppRSMdecision(Integer.valueOf(2));
          this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON, null, false);
      } catch (JSONException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 1917 :: RSM Calling ", e);
        appFormData.setAppRSMdecision(Integer.valueOf(2));
        this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON, null, false);
      }  
    logger.info("HLProcessImpl.java :: LNo :: 1684 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    if (SessionUtil.getApplicationType() != null || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
      statusRequest.setState(17);
      statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
      statusRequest.setRsmDecision(appFormData.getAppRSMdecision().intValue());
      statusRequest.setNsdlPANStatus(1);
      statusRequest.setAppPanCardNo(appFormData.getAppPanCardNo());
      statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
      statusRequest.setApplicationLeadType(appFormData.getAppDataSourceId().intValue());
      StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
      if (statusManagerResponse.getStatus() != 0) {
        appFormData.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
      } else if (appFormData.getAppLoanStatusId().intValue() == 0) {
        appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return appFormData;
      } 
      if (statusManagerResponse.isEligibleToInsertLog() && statusManagerResponse.getStatusCallLogs() > 0)
        this.homeLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
      if (statusManagerResponse.isInitiateCallAttempt())
        appFormData.setAppTotalCallAttempt(Integer.valueOf(0)); 
      logger.info("HLProcessImpl.java :: LNo :: 1755 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
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
              this.commonService.save(lead);
            } 
          } 
          logger.info("HLProcessImpl.java :: LNo :: 1795 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
        } else if (SessionUtil.getApplicationType().intValue() == 2) {
          logger.info("HLProcessImpl.java :: LNo :: 1816 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
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
          this.lastReferenceNumber = this.homeLoanService.getLastGeneratedReferenceNumber(Constants.HOME_LOAN_ID);
          logger.info("lastReferenceNumber" + this.lastReferenceNumber);
          this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_HL, Constants.SOURCE_STRING_HL, this.lastReferenceNumber);
          boolean isReferenceIdAvailable = false;
          isReferenceIdAvailable = this.homeLoanService.isReferenceIdAvailable(this.appRefKey);
          logger.info("isReferenceIdAvailable" + isReferenceIdAvailable);
          logger.info("HL isReferenceIdAvailable 1 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          if (!isReferenceIdAvailable) {
            appFormData.setAppReferenceId(this.appRefKey);
          } else {
            this.lastReferenceNumber = this.appRefKey;
            this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_HL, Constants.SOURCE_STRING_HL, this.lastReferenceNumber);
            isReferenceIdAvailable = this.homeLoanService.isReferenceIdAvailable(this.appRefKey);
            logger.info("HL isReferenceIdAvailable 2 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
            if (!isReferenceIdAvailable) {
              appFormData.setAppReferenceId(this.appRefKey);
            } else {
              this.lastReferenceNumber = this.appRefKey;
              this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_HL, Constants.SOURCE_STRING_HL, this.lastReferenceNumber);
              isReferenceIdAvailable = this.homeLoanService.isReferenceIdAvailable(this.appRefKey);
              logger.info("HL isReferenceIdAvailable 3 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
              appFormData.setAppReferenceId(this.appRefKey);
            } 
          } 
          logger.info("HL isReferenceIdAvailable 4 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          appFormData = this.homeLoanService.save(appFormData);
        }  
      logger.info("HLProcessImpl.java :: LNo :: 1847 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
      if (ValidatorUtil.isValid(appFormData.getAppReferenceId())) {
        if (appFormData.getAppRSMdecision().intValue() != 2 && appFormData.getAppRSMdecision() != null && appFormData.getAppRSMdecision().intValue() != 0)
          try {
            boolean sendMailStatus = false;
            if (isDsrPage.equalsIgnoreCase("true")) {
              sendMailStatus = true;
            } else if (appFormData.getAppAipMailSendStatus() == null) {
              sendMailStatus = true;
            } 
            if (sendMailStatus) {
              if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail()) && ValidatorUtil.isValid(appFormData.getAppReferenceId())) {
                Integer applicant = quote.getLoanQuoteEmploymentTypeId();
                Integer coApplicant1 = quote.getLoanQuoteCoapplicantFirstEmploymentTypeId();
                Integer coApplicant2 = quote.getLoanQuoteCoapplicantSecondEmploymentTypeId();
                if (quote.getLoanQuoteLoanPurposeId().intValue() == 3) {
                  if ((coApplicant2 == null && coApplicant1 == null && applicant != null && applicant.intValue() == 1) || (
                    coApplicant2 == null && coApplicant1 != null && coApplicant1.intValue() == 1 && applicant != null && applicant.intValue() == 1) || (
                    applicant != null && applicant.intValue() == 1 && coApplicant1 != null && coApplicant1.intValue() == 1 && coApplicant2 != null && coApplicant2.intValue() == 1)) {
                    File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_TO_Salaried_Applicant.pdf");
                    File[] hlSalFile = { hlSal };
                    logger.info("HomeProcessManagerImpl.java LNo :: 2444 :: Take Over HL_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                    this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                  } else if ((coApplicant2 == null && coApplicant1 == null && applicant != null && applicant.intValue() != 1) || (
                    coApplicant2 == null && coApplicant1 != null && coApplicant1.intValue() != 1 && applicant != null && applicant.intValue() != 1) || (
                    applicant != null && applicant.intValue() != 1 && coApplicant1 != null && coApplicant1.intValue() != 1 && coApplicant2 != null && coApplicant2.intValue() != 1)) {
                    File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_TO_Self_Employed_Business_Applicant.pdf");
                    File[] hlSalFile = { hlSal };
                    logger.info("HomeProcessManagerImpl.java LNo :: 2453 :: Take Over HL_NON_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                    this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                  } else if ((coApplicant2 == null && applicant != null && (applicant.intValue() != 1 || applicant.intValue() == 1) && coApplicant1 != null && (coApplicant1.intValue() != 1 || coApplicant1.intValue() == 1)) || (
                    coApplicant2 != null && (coApplicant2.intValue() != 1 || coApplicant2.intValue() == 1) && coApplicant1 != null && (coApplicant1.intValue() != 1 || coApplicant1.intValue() == 1) && 
                    applicant != null && (applicant.intValue() != 1 || applicant.intValue() == 1))) {
                    File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_TO_Salaried_Applicant.pdf");
                    File hlSal2 = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_TO_Self_Employed_Business_Applicant.pdf");
                    File[] hlSalFile = { hlSal, hlSal2 };
                    logger.info("HomeProcessManagerImpl.java LNo :: 2564 :: Take Over HL_NON_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                    this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                  } 
                } else if ((coApplicant2 == null && coApplicant1 == null && applicant != null && applicant.intValue() == 1) || (
                  coApplicant2 == null && applicant != null && applicant.intValue() == 1 && coApplicant1 != null && coApplicant1.intValue() == 1) || (
                  applicant != null && applicant.intValue() == 1 && coApplicant1 != null && coApplicant1.intValue() == 1 && coApplicant2 != null && coApplicant2.intValue() == 1)) {
                  File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "Checklist for HL_Salaried_Applicant.pdf");
                  File[] hlSalFile = { hlSal };
                  logger.info("HomeProcessManagerImpl.java LNo :: 2576 :: Non-Take Over HL_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                  this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                } else if ((coApplicant1 == null && coApplicant2 == null && applicant != null && applicant.intValue() != 1) || (
                  coApplicant2 == null && applicant != null && applicant.intValue() != 1 && coApplicant1 != null && coApplicant1.intValue() != 1) || (
                  applicant != null && applicant.intValue() != 1 && coApplicant1 != null && coApplicant1.intValue() != 1 && coApplicant2 != null && coApplicant2.intValue() != 1)) {
                  File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_Self_Employed_Business_Applicant.pdf");
                  File[] hlSalFile = { hlSal };
                  logger.info("HomeProcessManagerImpl.java LNo :: 2585 :: Non-Take Over HL_NON_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                  this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                } else if ((coApplicant2 == null && applicant != null && (applicant.intValue() != 1 || applicant.intValue() == 1) && coApplicant1 != null && (coApplicant1.intValue() != 1 || coApplicant1.intValue() == 1)) || (
                  coApplicant2 != null && (coApplicant2.intValue() != 1 || coApplicant2.intValue() == 1) && applicant != null && (applicant.intValue() != 1 || applicant.intValue() == 1) && 
                  coApplicant1 != null && (coApplicant1.intValue() != 1 || coApplicant1.intValue() == 1))) {
                  File hlSal = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "Checklist for HL_Salaried_Applicant.pdf");
                  File hlSal2 = new File(String.valueOf(Constants.PDF_TEMPLATE_BASE_PATH) + "HL_Self_Employed_Business_Applicant.pdf");
                  File[] hlSalFile = { hlSal, hlSal2 };
                  logger.info("HomeProcessManagerImpl.java LNo :: 2596 :: Non-Take Over HL_NON_SAL_CHECK_LIST_PATH : " + hlSal.getName() + " with AppSeqId " + appSeqId);
                  this.taskExecutorService.sendingEmailForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, hlSalFile);
                } 
              } 
              if (ValidatorUtil.isValid(appFormData.getAppReferenceId()))
                this.taskExecutorService.sendingSMSForHomeLoan(requestIndex, Integer.valueOf(1), appFormData, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage); 
            } 
            this.taskExecutorService.generatePDFForHomeLoan(appFormData, quote);
          } catch (NullPointerException e) {
              logger.info("HLProcessImpl.java LNo : 2014 : Exception caught in while generating PDF ", e);
          }  
          logger.info("HLProcessImpl.java LNo: 2961  calling CRM  with AppSeqId " + appSeqId + " CRM_BYPASS===" + Constants.CRM_BYPASS);
          if (appFormData != null && quote != null) {
            logger.info("HLProcessImpl.java LNo: 1943  calling CRM  with AppSeqId " + appSeqId);
            if (!Constants.CRM_BYPASS)
              if (appFormData.getAppCRMLeadId() != null && isDsrPage.equalsIgnoreCase("true")) {
                CRMRequest crmRequest = new CRMRequest();
                crmRequest.setCrmLeadId(appFormData.getAppCRMLeadId());
                crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
                crmRequest.setReferenceNumber(appFormData.getAppSeqId());
                crmRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
              } else {
                callCrm(appFormData, quote);
              }  
          } 
      } else {
        logger.info("HomeProcessManagerImpl reference number is not able to generate  with AppSeqId " + appSeqId);
        if (appFormData != null) {
          appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
          return appFormData;
        } 
      } 
      logger.info("HLProcessImpl.java :: LNo :: 1871 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 1886 ::  exception caught while generating ref number ", e);
        if (appFormData == null)
          appFormData = new ApplicationFormHomeLoan(); 
        appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return appFormData;
      } 
    logger.info("HLProcessImpl.java :: LNo :: 1877 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
    appFormData.setEngineRequest(engineRequest);
    appFormData.setEngineResponse(engineResponse);
    return appFormData;
  }
  
  public CBSCallResponse processCbsCall(Integer appSeqId, Integer requestIndex, MasterCBSCall masterCbsCall, String isDsrPage, Integer bankLMSUserId, Integer visitId, Integer cbsCallId, String receivedAction, Integer loanQuoteLoanPurposeId, Integer socialMediaId, Integer deviceId) throws SQLException {
    CBSCallResponse cbsCallResponse = new CBSCallResponse();
    masterCbsCall.setCbsLoanTypeId(Constants.HOME_LOAN_ID);
    int count = this.commonService.getCBSApplicationCount(masterCbsCall.getCbsIsdCode(), masterCbsCall.getCbsMobileNumber(), Constants.HOME_LOAN_ID).intValue();
    logger.info("HomeProcessManagerImpl.java LNo : 2844 count : " + count + " with AppSeqId " + appSeqId);
    if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
      cbsCallResponse.setResponseMsg(Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
      cbsCallResponse.setStatus(Integer.valueOf(0));
      return cbsCallResponse;
    } 
    cbsCallResponse.setStatus(Integer.valueOf(0));
    try {
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      logger.info("loanQuoteLoanPurposeId : " + loanQuoteLoanPurposeId);
      if (!Constants.CBS_DEDUPE_BYPASS && loanQuoteLoanPurposeId != null) {
        String oldMobile = "";
        String isdCode = "";
        boolean isEligibleForBypass = false;
        if (appSeqId == null && masterCbsCall.getCbsMobileNumber() != null) {
          isdCode = (masterCbsCall.getCbsIsdCode() == null) ? Constants.COUNTRY_CODE_INDIA : masterCbsCall.getCbsIsdCode().toString();
          oldMobile = masterCbsCall.getCbsMobileNumber();
          isEligibleForBypass = true;
        } 
        if (isEligibleForBypass && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
          boolean isAppFoundForDedupInApplicationStage = false;
          isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage(null, isdCode, oldMobile, loanQuoteLoanPurposeId);
          logger.info("HLProcessImpl.java LNo 2637:: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
          if (isAppFoundForDedupInApplicationStage) {
            cbsCallResponse.setResponseMsg(Constants.APP_DEDUPLICATION_MESSAGE);
            return cbsCallResponse;
          } 
          isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
          logger.info("HLProcessImpl.java LNo 2643 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
          isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
          logger.info("HLProcessImpl.java LNo 2645 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
        } 
      } 
      masterCbsCall.setCbsVisitId(visitId);
      if (cbsCallId != null) {
        MasterCBSCall oldMasterCbsCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
        oldMasterCbsCall.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
        oldMasterCbsCall.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
        oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCode());
        oldMasterCbsCall.setCbsTypeOfRelationship(masterCbsCall.getCbsTypeOfRelationship());
        masterCbsCall = this.commonService.save(oldMasterCbsCall);
        if (masterCbsCall == null) {
          logger.info("HomeProcessManagerImpl LNO :: 2378 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } else {
        masterCbsCall.setCbsOtpVerified(Integer.valueOf(0));
        masterCbsCall.setCbsRequiestedTime(new Date());
        masterCbsCall = this.commonService.save(masterCbsCall);
        if (masterCbsCall == null) {
          logger.info("HomeProcessManagerImpl LNO :: 2388 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        if (receivedAction.equalsIgnoreCase(Constants.HOME_LOAN_ACTION)) {
          SessionUtil.setHomeLoanCbsCallId(masterCbsCall.getCbsId());
          logger.info("SessionUtil=================" + SessionUtil.getHomeLoanCbsCallId() + " with AppSeqId " + appSeqId);
        } else if (receivedAction.equalsIgnoreCase(Constants.HOME_TOP_UP_LOAN_ACTION)) {
          SessionUtil.setHomeTopUpLoanCbsCallId(masterCbsCall.getCbsId());
          logger.info("SessionUtil=================" + SessionUtil.getHomeTopUpLoanCbsCallId() + " with AppSeqId " + appSeqId);
        } 
      } 
      cbsCallResponse.setCbsCallId(masterCbsCall.getCbsId());
      cbsCallResponse.setVisitId(visitId);
      MasterCBSResponse masterCBSResponse = null;
      if (masterCbsCall.getCbsResponseId() == null) {
        masterCBSResponse = new MasterCBSResponse();
      } else {
        masterCBSResponse = this.commonService.getMasterCBSResponseById(masterCbsCall.getCbsResponseId());
        if (masterCBSResponse == null) {
          logger.info("HomeProcessManagerImpl LNO :: 2410 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } 
      
      logger.info("Before Caaling callingCBSEngineForCIFLevelInformation");
      JSONObject cbsEngineResponseJson = this.cbsUtil.callingCBSEngineForCIFLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.HOME_LOAN_ID.intValue());
      CBSCustomerInformation cbsCustomerInformation = new CBSCustomerInformation();
      this.cbsUtil.setCBSCustomerInformationBean(cbsCustomerInformation, cbsEngineResponseJson);
      if (cbsCustomerInformation.getStatus() != null && cbsCustomerInformation.getStatus().equals("0")) {
        logger.info("HomeLoan LNO :: 2978 with AppSeqId ::" + appSeqId);
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
        logger.info("HomeProcessManagerImpl LNO :: 2430" + cbsCustomerInformation.getError() + " with AppSeqId " + appSeqId);
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (!masterCbsCall.getCbsMobileNumber().equals(cbsCustomerInformation.getMOBILENO())) {
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      JSONObject accountInfoResponseJson = this.cbsUtil.callingCBSEngineForAccountLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.HOME_LOAN_ID.intValue());
      if (accountInfoResponseJson.has("ERROR_CODE") && accountInfoResponseJson.get("ERROR_CODE") != null && accountInfoResponseJson.get("ERROR_CODE").toString().trim().length() > 0) {
        logger.info("Home Loan  LNO :: 2423");
        if (accountInfoResponseJson.get("ERROR_DESCRIPTION") != null) {
          if (accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
            cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
          } else {
            cbsCallResponse.setResponseMsg(accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim());
          } 
        } else {
          cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
        } 
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      String productId = "";
      if (accountInfoResponseJson.has("AccType"))
        productId = accountInfoResponseJson.getString("AccType"); 
      if (accountInfoResponseJson.has("Interest_Category"))
        productId = String.valueOf(productId) + accountInfoResponseJson.getString("Interest_Category"); 
      cbsCustomerInformation.setProductId(productId);
      //logger.info("Response from Customer Enquiry and Account info Data Account no : " + EncryptDecryptUtil.encrypt(cbsCustomerInformation.getACCOUNTNUMBER())  + " Product Id : " + cbsCustomerInformation.getProductId());
      cbsCustomerInformation.setAccountDesc(accountInfoResponseJson.getString("AccountDescription"));
      boolean needToByPassLoanAccountInformation = true;
      if (masterCbsCall.getCbsTypeOfRelationship() != null) {
        Integer typeOfRelationship = masterCbsCall.getCbsTypeOfRelationship();
        if (typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3"))
          needToByPassLoanAccountInformation = false; 
      } 
      if (ValidatorUtil.isValid(cbsCustomerInformation.getProductId())) {
        if (needToByPassLoanAccountInformation) {
          boolean productFound = this.commonService.isCbsMappingsExistByProductId(Constants.HOME_LOAN_ID, cbsCustomerInformation.getProductId());
          if (!productFound) {
            logger.info("HomeProcessManagerImpl.java LNo: 2434 : Product id not match with mapping table with AppSeqId " + appSeqId);
            cbsCallResponse.setResponseMsg("Provided account No. does not pertain to selected type of relationship");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } 
      } else {
        logger.info("HomeProcessManagerImpl.java LNo: 2440 : Product id not received  in Customer information service");
        cbsCallResponse.setResponseMsg("Product id not received  in Customer information service");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      try {
        masterCbsCall.setCbsResponseTime(new Date());
        masterCBSResponse.setCbsLoanTypeId(Constants.HOME_LOAN_ID);
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
        if (ValidatorUtil.isValidPanNo(cbsCustomerInformation.getPANNUMBER()))
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
          logger.info("HomeProcessManagerImpl LNO :: 2564 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        logger.info("masterCBSResponse :: " + masterCBSResponse.toString() + " with AppSeqId " + appSeqId);
      } catch (NullPointerException e) {
          logger.info("HomeLoanProcessImpl.java LNo: 2548 :: exception caught ", e);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
      } 
      CBSLoanAccountInformation cbsLoanAccountInformation = new CBSLoanAccountInformation();
      logger.info("needToByPassLoanAccountInformation : " + needToByPassLoanAccountInformation);
      if (needToByPassLoanAccountInformation) {
        JSONObject loanAccountInformationcbsResponseJson = this.cbsUtil.callingCBSEngineForLoanAccountInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.CBS_LOAN_TYPE_HOME_TOP_UP);
        this.cbsUtil.setCbsLoanAccountInformation(cbsLoanAccountInformation, loanAccountInformationcbsResponseJson);
        if (cbsLoanAccountInformation.getStatus() != null && cbsLoanAccountInformation.getStatus().equals("0")) {
          logger.info("Home Loan  :: 2571 with AppSeqId " + appSeqId);
          if (cbsLoanAccountInformation.getErrorReason() != null) {
            if (cbsLoanAccountInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
              cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
            } else {
              cbsCallResponse.setResponseMsg(cbsLoanAccountInformation.getErrorReason().trim());
            } 
          } else {
            cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes 3.");
          } 
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        cbsLoanAccountInformation.setBranchCode(cbsCustomerInformation.getBranchCode());
        cbsLoanAccountInformation.setReferenceNumber(cbsEngineResponseJson.getString("REQUEST_REFERENCE_NUMBER"));
      } 
      masterCbsCall.setCbsResponseId(masterCBSResponse.getCbsResponseId());
      logger.info("CBS_RES_ID :: " + masterCBSResponse.getCbsResponseId() + " with AppSeqId " + appSeqId);
      if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
        masterCbsCall.setCbsEmail(masterCBSResponse.getCbsEmail());
        masterCbsCall.setCbsEmailMask(masterCBSResponse.getCbsEmail());
        masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(1));
      } else {
        masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(0));
      }
      masterCbsCall.setConsentRevokeETB(masterCbsCall.getConsentRevokeETB());
      masterCbsCall = this.commonService.save(masterCbsCall);
  //    logger.info("save masterCbsCall :: " + masterCbsCall + " with AppSeqId " + appSeqId);
      if (masterCbsCall == null) {
        logger.info("HomeProcessManagerImpl LNO :: 2589 with AppSeqId " + appSeqId);
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
        if (ValidatorUtil.isValid(cbsLoanAccountInformation.getRepaymentstartdate())) {
          logger.info("HLProcessImpl.java LNo : 3069 cbsLoanAccountInformation.getRepaymentstartdate(): " + cbsLoanAccountInformation.getRepaymentstartdate() + " with AppSeqId " + appSeqId);
          masterCBSResponse.setCbsRepaymentStartDate(DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getRepaymentstartdate()));
          logger.info("HLProcessImpl.java LNo : 3071 masterCBSResponse.getCbsRepaymentStartDate(): " + masterCBSResponse.getCbsRepaymentStartDate() + " with AppSeqId " + appSeqId);
        } 
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
          logger.info("HomeLoanProcessImpl.java LNo: 2644 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        //logger.info("save loanType information in master CBS Response table :: " + masterCBSResponse.toString() + " with AppSeqId " + appSeqId);
      } catch (ParseException ex) {
        logger.info("HomeProcessManagerImpl LNO :: 2635", ex);
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      }  
      if (needToByPassLoanAccountInformation && 
        Integer.parseInt(masterCBSResponse.getCbsIracStatus()) >= Constants.CBS_IRAC_STATUS.intValue()) {
        logger.info("HomeLoanProcessImpl.java LNo: 2651 with AppSeqId " + appSeqId);
        cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      boolean needToInsert = true;
      ApplicationFormHomeLoan app = null;
      ApplicationFormHomeLoanQuote quote = null;
      if (masterCBSResponse.getCbsAppSeqId() == null) {
        app = new ApplicationFormHomeLoan();
        quote = new ApplicationFormHomeLoanQuote();
      } else {
        app = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
        if (app == null) {
          logger.info("HomeLoanProcessImpl.java LNo: 2666 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        needToInsert = false;
        if (app.getAppQuoteId() != null) {
          quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(app.getAppQuoteId());
          if (quote == null) {
            logger.info("HomeLoanProcessImpl.java LNo: 2675 with AppSeqId " + appSeqId);
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } else {
          quote = new ApplicationFormHomeLoanQuote();
        } 
      } 
      try {
        quote.setLoanQuoteActive("Y");
        quote.setLoanQuoteDeleted("N");
        quote.setLoanQuoteFirstOwnerOfProperty("N");
        quote.setLoanQuoteCreatedLmsUserId(Constants.OTHER_USER_ID);
        quote.setLoanQuoteEntryTime(new Date());
        quote.setLoanQuoteUpdatedTime(new Date());
        quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
        quote.setLoanQuoteVisitId(visitId);
        quote.setLoanQuoteIsEligibleForHarGhar(Integer.valueOf(0));
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsOutstanding()))
          quote.setLoanQuoteOutstandingLoanAmount(masterCBSResponse.getCbsOutstanding()); 
        if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
          quote.setLoanQuoteDateOfBirth(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)); 
        if (ValidatorUtil.isValid(masterCbsCall.getCbsVisitId()))
          quote.setLoanQuoteVisitId(masterCbsCall.getCbsVisitId()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsValueOfPrimarySecurity()))
          quote.setLoanQuotePresentValueOfProperty(Double.valueOf(masterCBSResponse.getCbsValueOfPrimarySecurity().intValue())); 
        logger.info("HLProcessManagerImpl.java : LNo: 3168 " + masterCBSResponse.getCbsRepaymentStartDate() + " with AppSeqId " + appSeqId);
        if (masterCBSResponse.getCbsRepaymentStartDate() != null) {
          String date = DateUtil.convertDateToString(masterCBSResponse.getCbsRepaymentStartDate());
          logger.info("HLProcessManagerImpl.java : LNo: 3171 date : " + date + " with AppSeqId " + appSeqId);
          String stdate = String.valueOf(date.substring(0, 2)) + "-" + date.substring(2, 4) + "-" + date.substring(4);
          logger.info("HLProcessManagerImpl.java : LNo: 3173 stdate : " + stdate + " with AppSeqId " + appSeqId);
          logger.info("HLProcessManagerImpl.java : LNo: 3174 stdate : " + DateUtil.convertStringToDate(stdate) + " with AppSeqId " + appSeqId);
          logger.info("HLProcessManagerImpl.java : LNo: 3175 stdate : " + new Integer(date.substring(2, 4)) + " with AppSeqId " + appSeqId);
          logger.info("HLProcessManagerImpl.java : LNo: 3176 stdate : " + new Integer(date.substring(4)) + " with AppSeqId " + appSeqId);
          quote.setLoanQuoteExistingHomeLoanStartDate(DateUtil.convertStringToDate(stdate));
          quote.setLoanQuoteMonthExistingHomeLoanStartDate(new Integer(date.substring(2, 4)));
          quote.setLoanQuoteYearExistingHomeLoanStartDate(new Integer(date.substring(4)));
        } 
        logger.info("HLProcessManagerImpl.java : LNo: 3181 " + masterCBSResponse.getCbsRepaymentStartDate() + " with AppSeqId " + appSeqId);
        if (masterCBSResponse.getCbsRepaymentStartDate() != null) {
          Calendar cal = Calendar.getInstance();
          cal.setTime(masterCBSResponse.getCbsRepaymentStartDate());
          Integer tenure = masterCBSResponse.getCbsLoanTenure();
          cal.add(1, tenure.intValue());
          logger.info("HLProcessManagerImpl.java : LNo: 3189 : cal.getTime().toString() : " + cal.getTime().toString() + " with AppSeqId " + appSeqId);
          quote.setLoanQuoteExistingHomeLoanEndDate(cal.getTime());
          logger.info("HLProcessManagerImpl.java : LNo: 3191 : DateUtil.getYearFromDate(cal.getTime()) : " + DateUtil.getYearFromDate(cal.getTime()) + " with AppSeqId " + appSeqId);
          quote.setLoanQuoteYearExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getYearFromDate(cal.getTime())));
          logger.info("HLProcessManagerImpl.java : LNo: 3193 : DateUtil.getMonthFromDate(cal.getTime()) : " + DateUtil.getMonthFromDate(cal.getTime()) + " with AppSeqId " + appSeqId);
          quote.setLoanQuoteMonthExistingHomeLoanEndDate(Integer.valueOf(DateUtil.getMonthFromDate(cal.getTime())));
        } 
        if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
          quote.setLoanQuoteDateOfBirthDT(masterCBSResponse.getCbsDateOfBirth()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
          quote.setLoanQuoteGender(masterCBSResponse.getCbsGender());
        //new code for cibil
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsFirstName()))
        	quote.setLoanQuoteAppFirstName(masterCBSResponse.getCbsFirstName());
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsMiddleName()))
        	quote.setLoanQuoteMiddleName(masterCBSResponse.getCbsMiddleName()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsLastName()))
        	quote.setLoanQuoteLastName(masterCBSResponse.getCbsLastName()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine1()))
        	quote.setLoanQuoteAddress1(masterCBSResponse.getCbsAddressLine1()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine2()))
        	quote.setLoanQuoteAddress2(masterCBSResponse.getCbsAddressLine2()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsAddressLine3()))
        	quote.setLoanQuoteAddressLandmark(masterCBSResponse.getCbsAddressLine3()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode()) && 
            masterCBSResponse.getCbsPinCode().length() == 6)
            quote.setLoanQuotePincode(Integer.valueOf(Integer.parseInt(masterCBSResponse.getCbsPinCode()))); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsPanNumber()))
            quote.setLoanQuotePanCardNo(masterCBSResponse.getCbsPanNumber());
        
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsCifNumber()))
            quote.setLoanQuoteCifNumber(masterCBSResponse.getCbsCifNumber()); 
        
        //end code for cibil
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
          quote.setLoanQuotePreEMIs(masterCBSResponse.getCbsEmi()); 
        if (receivedAction.equalsIgnoreCase(Constants.HOME_TOP_UP_LOAN_ACTION))
          quote.setLoanQuoteLoanPurposeId(Integer.valueOf(5));
        
        //Integer consentId = commonService.getConsentIdByLoanType(Constants.HOME_LOAN_ID);
		//quote.setLoanQuoteConsentId(consentId); 
        
        quote = this.homeLoanService.save(quote);
        
        if (quote == null) {
          logger.info("HomeLoanProcessImpl.java LNo: 2762 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        
      } catch (ParseException ex) {
        logger.info("HomeLoanProcessImpl.java LNo: 2768", ex);
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
          if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(String.valueOf(masterCbsCall.getCbsIsdCode()))) {
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
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
        } else if ("true".equalsIgnoreCase(masterCbsCall.getIsMobileRequest())) {
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP);
        } else {
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
        } 
        app.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE167_ID));
        app.setAppSubTypeId(Constants.APP_APP_SUB_TYPE_ID_CBS);
        app.setAppContactCenterLocation(1);
        app.setAppLeadUpdateTime(new Date());
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
          app.setAppLoanEmi(masterCBSResponse.getCbsEmi()); 
        if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
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
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsPhoneBusiness()) && 
          StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()))
          app.setAppOfficePhoneNumber(Long.valueOf(Long.parseLong(masterCBSResponse.getCbsPhoneBusiness()))); 
        if (ValidatorUtil.isValidPanNo(masterCBSResponse.getCbsPanNumber()))
          app.setAppPanCardNo(masterCBSResponse.getCbsPanNumber()); 
        if (ValidatorUtil.isValid(masterCbsCall.getCbsResponseId()))
          app.setAppExistingRelationTypeId(Integer.toString(masterCbsCall.getCbsResponseId().intValue())); 
        if (ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())) {
          app.setAppISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
          SessionUtil.setISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
        } 
        if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
          app.setAppMobileNo(masterCbsCall.getCbsMobileNumber());
          SessionUtil.setMobile(masterCbsCall.getCbsMobileNumber());
        } 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
          app.setAppGender(masterCBSResponse.getCbsGender()); 
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
            if (this.commonService.isBankUser(bankLMSUserId))
              app.setAppAssignedLmsSalesUserId(bankLMSUserId); 
          } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null) {
            bankLMSUserId = SessionUtil.getBankLMSUser().getLmsUserId();
            if (this.commonService.isBankUser(bankLMSUserId))
              app.setAppAssignedLmsSalesUserId(bankLMSUserId); 
          }  
        
        if (quote.getLoanQuoteCifNumber() != null) {
        	app.setAppCifNumber(quote.getLoanQuoteCifNumber());
        }
        
        //save consent id in main table
        //if (quote.getLoanQuoteConsentId() != null && app.getAppConsentId() == null) {
	    //	app.setAppConsentId(quote.getLoanQuoteConsentId());
	    //}
        
        
        app = this.homeLoanService.save(app);
        if (app == null) {
          logger.info("HomeLoanProcessImpl.java LNo: 2842 with AppSeqId " + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        if (receivedAction.equalsIgnoreCase(Constants.HOME_LOAN_ACTION)) {
          SessionUtil.setHomeLoanApplicationSequenceId(app.getAppSeqId());
        } else if (receivedAction.equalsIgnoreCase(Constants.HOME_TOP_UP_LOAN_ACTION)) {
          SessionUtil.setHomeLoanTopupApplicationSequenceId(app.getAppSeqId());
        } 
      } catch (SQLException e) {
          logger.info("HomeLoanProcessImpl.java LNo: 2737 :: exception caught ", e);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
       } 
      cbsCallResponse.setAppSeqId(app.getAppSeqId());
      masterCBSResponse.setCbsAppSeqId(app.getAppSeqId());
      masterCBSResponse = this.commonService.save(masterCBSResponse);
      if (masterCBSResponse == null) {
        logger.info("HomeLoanProcessImpl.java LNo: 2867 with AppSeqId " + appSeqId);
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (needToInsert)
        try {
          this.homeLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), app.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE167, null, true);
        } catch (NullPointerException e) {
            logger.info("HomeLoanProcessImpl.java LNo: 2901 :: exception caught ", e);
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          }  
      if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
        masterCbsCall.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSResponse.getCbsEmail()));
      } 
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
        cbsCallResponse.setStatus(Integer.valueOf(0));
        cbsCallResponse.setResponseMsg("Mobile OTP service is down");
        return cbsCallResponse;
      } 
      if (masterCbsCall.getCbsIsdCode() != null && masterCbsCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
        ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
        msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
        if (msgBody != null) {
          msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
          msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
          msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
          msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
          msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
          msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.HOME_LOAN_PRODUCT_NAME);
          boolean sendStatus = false;
          String[] emailId = { masterCBSResponse.getCbsEmail() };
          sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
          if (!sendStatus) {
            logger.info("HomeProcessManagerImpl LNO ::3710 with AppSeqId " + appSeqId);
            cbsCallResponse.setResponseMsg("Email OTP service is down");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } 
      } 
      masterCbsCall.setCbsOtpCount(String.valueOf(1));
      masterCbsCall = this.commonService.save(masterCbsCall);
      
      
      if (masterCbsCall == null) {
        logger.info("HomeProcessManagerImpl LNO :: 3207 with AppSeqId " + appSeqId);
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
      
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 2996 :: exception caught ", e);
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
     } catch (JSONException e) {
      logger.info("HomeLoanProcessImpl.java LNo: 2996 :: exception caught ", e);
      cbsCallResponse.setResponseMsg("System error occured.");
      cbsCallResponse.setStatus(Integer.valueOf(0));
    } catch (ParseException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 2996 :: exception caught ", e);
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
    } catch (RuntimeException e) {
          logger.info("HomeLoanProcessImpl.java LNo: 2996 :: exception caught ", e);
          cbsCallResponse.setResponseMsg("System error occured.");
          cbsCallResponse.setStatus(Integer.valueOf(0));
    }  
    return cbsCallResponse;
  }
  
  public JSONObject processCBSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, int appOTPVerifyType, String inputOtp, String userEmail, Integer appSeqId, Integer cbsCallId, String receivedAction) throws JSONException {
	logger.info("inside the processCBSOTP method inputOtp"+inputOtp);
	  if(inputOtp !=null) {
      	SbiUtil sbiutil=new SbiUtil();
      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
      }
	
    JSONObject json = new JSONObject();
    if (cbsCallId == null) {
      logger.info("HomeLoanProcessImpl.java LNo: 2979 with AppSeqId " + appSeqId);
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    }
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
    if (masterCBSCall == null) {
      logger.info("HomeLoanProcessImpl.java LNo: 2989 with AppSeqId " + appSeqId);
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
    if (stateId.intValue() == 28)
      try {
        if (appSeqId != null) {
          ApplicationFormHomeLoan app = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
          if (app == null) {
            logger.info("HomeProcessManagerImpl.java LNO 4722:: after save error with AppSeqId " + appSeqId);
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
          logger.info("HomeProcessManagerImpl LNO :: 3056 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
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
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", masterCBSCall.getCbsMobileNumber().trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
      	  logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode().toString());
        }
        
        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          logger.info("HomeLoanProcessImpl.java LNo: 3083 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (masterCBSCall.getCbsIsdCode() != null && masterCBSCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
          ValidatorUtil.isValidEmail(masterCBSCall.getCbsEmail())) {
          msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
          msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
          msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
          msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
          msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
          msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCBSCall.getCbsEmailOtpCode());
          msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.HOME_LOAN_PRODUCT_NAME);
          boolean sendStatus = false;
          String[] emailId = { masterCBSCall.getCbsEmail() };
          sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
          logger.info("CBS EMAIL msgbody of home : " + msgBody + " with AppSeqId ::" + appSeqId);
          if (!sendStatus) {
            logger.info("HomeProcessManagerImpl LNO :: 3874 with AppSeqId " + appSeqId);
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
          logger.info("HomeLoanProcessImpl.java LNo: 3154 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (JSONException e) {
          logger.info("HomeProcessManager.java LNO 3176::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } catch (SQLException e) {
        logger.info("HomeProcessManager.java LNO 3176::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    if (stateId.intValue() == 29)
      try {
        if (appSeqId == null) {
          logger.info("HomeProcessManager.java LNO 3368:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return json;
        } 
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Invalid OTP code");
          return json;
        } 
        ApplicationFormHomeLoan app = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("HomeLoanProcessImpl.java LNo: 3147 with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        logger.info("HomeProcessManagerImpl.java LNo : 3848 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
        
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        app = this.homeLoanService.save(app);
        if (app == null) {
          logger.info("HomeProcessManager.java LNO 3857 error on saving:: with AppSeqId " + appSeqId);
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
            //  logger.info("Capture Email From User is ::" + userEmail + " with AppSeqId " + appSeqId);
            } 
            SessionUtil.setEmail(userEmail);
          } else {
            logger.info("HomeProcessManager.java LNO 3141 with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "Please Enter valid email");
            return json;
          }  
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (masterCBSCall.getCbsOtpCode() != null)
            app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode()))); 
          if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(inputOtp)) {
            masterCBSCall.setCbsMobileOtpVerified("Y");
            app.setAppMobileVerified("Y");
            app.setAppMobileVerificationCodeReceived("Y");
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            //logger.info("OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("HomeLoanProcessImpl.java LNo: 3166 with AppSeqId " + appSeqId);
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
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
           // logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("HomeLoanProcessImpl.java LNo: 3191 with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            app.setAppEmailVerified("N");
            return json;
          } 
        } else {
          logger.info("HomeLoanProcessImpl.java LNo: 3199 with AppSeqId " + appSeqId);
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
          statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.homeLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.homeLoanService.save(app);
          if (app == null) {
            logger.info("HomeProcessManager.java LNO 3487:: with AppSeqId " + appSeqId);
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
          logger.info("HomeProcessManager.java LNO 3500:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (JSONException e) {
          logger.info("HomeProcessManager.java LNO 3238::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       } catch (SQLException e) {
        logger.info("HomeProcessManager.java LNO 3238::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    return json;
  }
  
  public JSONObject verifySMSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException {
    JSONObject json = new JSONObject();
    int appOTPVerifyType = 0;
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getHomeLoanCbsCallId() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3803");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getHomeLoanCbsCallId());
    if (masterCBSCall == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3981");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsOtpCount() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3821");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsIsdCode() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3832");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsMobileNumber() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3841");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
    if (stateId.intValue() == 33)
      try {
        Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
        ApplicationFormHomeLoan app = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("HomeProcessManagerImpl.java LNO 4722:: after save error with appSeqId" + appSeqId);
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
          logger.info("HomeProcessManagerImpl LNO :: 3854");
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          masterCBSCall.setCbsOtpCode(this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
          logger.info("HomeProcessManagerImpl LNO :: 3827 ::  mobile number ::" + masterCBSCall.getCbsMobileNumber() + " with AppSeqId " + appSeqId);
          logger.info("HomeProcessManagerImpl LNO :: 3829 :: mobile OTP ::" + masterCBSCall.getCbsOtpCode() + " with AppSeqId " + appSeqId);
        } else {
          logger.info("HomeProcessManagerImpl LNO :: 3831");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        boolean sendSMSConsent = false;
        if (isDsrPage.equalsIgnoreCase("true")) {
          boolean bankLmsUserRoleExceptContactCenter = this.commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
          if (bankLmsUserRoleExceptContactCenter) {
            logger.info("HomeProcessManagerImpl LNO :: 4728 :: Not contact Center user with AppSeqId " + appSeqId);
            sendSMSConsent = true;
          } else {
            logger.info("HomeProcessManagerImpl LNO :: 4728 :: contact Center user with AppSeqId " + appSeqId);
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
        alertCount = Integer.valueOf(alertCount.intValue() + 1);
        masterCBSCall.setCbsOtpCount(alertCount.toString());
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("HomeProcessManagerImpl LNO :: 4523 Sorry for the inconvenience with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        if (app != null) {
          app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode())));
          app.setAppMobileVerified("N");
        } 
        app = this.homeLoanService.save(app);
        if (app == null) {
          logger.info("HomeProcessManagerImpl LNO :: 4535 Sorry for the inconvenience with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (JSONException e) {
          logger.info("HomeProcessManagerImpl.java LNO 3923::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       } catch (SQLException e) {
        logger.info("HomeProcessManagerImpl.java LNO 3923::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    if (stateId.intValue() == 34)
      try {
        Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("HomeProcessManagerImpl.java LNO 3933:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        ApplicationFormHomeLoan app = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("HomeProcessManagerImpl.java LNO 4861:: after save error with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        logger.info("HomeProcessManagerImpl.java LNo : 4860 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        app = this.homeLoanService.save(app);
        if (app == null) {
          logger.info("HomeProcessManagerImpl.java LNO 4872:: after save error with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience, Please click <a hrdf='" + Constants.PORT + Constants.CONTEXT + ajaxPostUrl + "'>here</a> to start again.");
          return json;
        } 
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (app.getAppMobileVerified().equals("Y")) {
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
            //logger.info("HomeProcessManager.java LNO :: 4859 OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
          } else {
            logger.info("HomeProcessManager.java LNO :: 4861 :: OTP authentication failed with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("HomeProcessManager.java LNO 4868:: OTP authentication failed with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
            logger.info("HomeProcessManagerImpl.java LNO 3993:: with AppSeqId " + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.homeLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.homeLoanService.save(app);
          if (app == null) {
            logger.info("HomeProcessManagerImpl.java LNO 4003:: with AppSeqId " + appSeqId);
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
          logger.info("HomeProcessManagerImpl.java LNO 4017:: with AppSeqId " + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (JSONException e) {
          logger.info("HomeProcessManagerImpl.java LNO 4042::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        }  catch (SQLException e) {
        logger.info("HomeProcessManagerImpl.java LNO 4042::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    return json;
  }
  
  private void callCrm(ApplicationFormHomeLoan appFormData, ApplicationFormHomeLoanQuote quote) {
    try {
      logger.info("Before preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
      CRMRequest crmRequest = prepareCrmRequest(appFormData, quote);
      logger.info("Beofoe preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
      this.crmServiceNew.crmLeadCreation(crmRequest, appFormData);
    } catch (NullPointerException e) {
        logger.info("HomeProcessManagerImpl.java  LNO 4497:: Exception occured", e);
    } catch (ParseException e) {
      logger.info("HomeProcessManagerImpl.java  LNO 4497:: Exception occured", e);
    }  catch (RuntimeException e) {
        logger.info("HomeProcessManagerImpl.java  LNO 4497:: Exception occured", e);
     } catch (SQLException e) {
         logger.info("HomeProcessManagerImpl.java  LNO 4497:: Exception occured", e);
     }
  }
  
  private CRMRequest prepareCrmRequest(ApplicationFormHomeLoan appFormData, ApplicationFormHomeLoanQuote quote) throws SQLException, NullPointerException, RuntimeException, ParseException {
    CRMRequest crmRequest = new CRMRequest();
    if (appFormData == null) {
      logger.info("appFormData is null, returning back with AppSeqId is null");
      return null;
    } 
    if (quote == null) {
      logger.info("quote is null, returning back with AppSeqId is null");
      return null;
    } 
    if (appFormData.getAppFirstName() != null)
      crmRequest.setFirstName(appFormData.getAppFirstName()); 
    if (appFormData.getAppMiddleName() != null)
      crmRequest.setMiddleName(appFormData.getAppMiddleName()); 
    if (appFormData.getAppLastName() != null)
      crmRequest.setLastName(appFormData.getAppLastName()); 
    if (appFormData.getAppGender() != null)
      if (appFormData.getAppGender().equals("M")) {
        crmRequest.setGender("1");
      } else if (appFormData.getAppGender().equals("F")) {
        crmRequest.setGender("2");
      } else if (appFormData.getAppGender().equals("O")) {
        crmRequest.setGender("3");
      }
    
    if (appFormData.getAppDobDT() != null)
      crmRequest.setDateOfBirth(DateUtil.getDateTimeForCRMInString(appFormData.getAppDobDT())); 
    if (appFormData.getAppCityId() != null && appFormData.getAppCityId().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
      MasterCity city = this.commonService.getCityById(appFormData.getAppCityId());
      if (city != null)
        crmRequest.setCity(city.getCityName()); 
    } 
    if (appFormData.getAppDistrictId() != null) {
      MasterDistrict district = this.commonService.getDistrictById(appFormData.getAppDistrictId());
      if (district != null)
        crmRequest.setDistrict(district.getDistrictName()); 
    } 
    if (appFormData.getAppStateId() != null) {
      MasterState state = this.commonService.getStateById(appFormData.getAppStateId());
      if (state != null)
        crmRequest.setState(state.getStateName()); 
    } 
    if (ValidatorUtil.isValidPanNo(appFormData.getAppPanCardNo()))
      crmRequest.setPanNumber(appFormData.getAppPanCardNo()); 
    if (appFormData.getAppMobileNo() != null) {
      crmRequest.setMobileNumber(appFormData.getAppMobileNo());
    } 
    if (ValidatorUtil.isValid(appFormData.getAppOfficePhoneStdCode()) && 
      appFormData.getAppOfficePhoneStdCode().length() == 4 && 
      appFormData.getAppOfficePhoneNumber() != null && 
      appFormData.getAppOfficePhoneNumber().longValue() == 7L)
      crmRequest.setPhoneNumber(String.valueOf(appFormData.getAppOfficePhoneStdCode()) + appFormData.getAppOfficePhoneNumber()); 
    crmRequest.setPreferredChannelKey(Integer.valueOf(2));
    if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail())) {
      crmRequest.setEmailId(appFormData.getAppWorkEmail());
      crmRequest.setPreferredChannelKey(Integer.valueOf(1));
    } 
    crmRequest.setStatusCode(Integer.valueOf(100002));
    crmRequest.setLeadPriority(Integer.valueOf(1));
    crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
    if (quote.getLoanQuoteLoanPurposeId() != null) {
      MasterLoanPurpose loanPurpose = this.commonService.getLoanPurposeById(quote.getLoanQuoteLoanPurposeId());
      if (loanPurpose != null)
        crmRequest.setLoanPurpose(loanPurpose.getLpTypeValue()); 
    } 
    if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
      crmRequest.setResidentType("2");
    } else {
      crmRequest.setResidentType("1");
    } 
    if (quote.getLoanQuoteCountryId() != null) {
      MasterCountry masterCountry = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
      if (masterCountry != null)
        crmRequest.setResidentCountry(masterCountry.getCountryName()); 
    } else {
      crmRequest.setResidentCountry("India");
    } 
    if (appFormData.getAppISDCode() != null && !"".equals(appFormData.getAppISDCode()))
      crmRequest.setCountryCode(Integer.valueOf(Integer.parseInt(appFormData.getAppISDCode()))); 
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
    if (appFormData.getAppAddress1() != null)
      crmRequest.setAddressLine1(appFormData.getAppAddress1()); 
    if (appFormData.getAppAddress2() != null)
      crmRequest.setAddressLine2(appFormData.getAppAddress2()); 
    if (appFormData.getAppAddressLandmark() != null)
      crmRequest.setAddressLine3(appFormData.getAppAddressLandmark()); 
    if (appFormData.getAppOtherIdNumber() != null && appFormData.getAppOtherId().intValue() > 0) {
      MasterDocumentType masterDocumentType = this.commonService.getDocumentType(appFormData.getAppOtherId().intValue());
      if (masterDocumentType != null && masterDocumentType.getDocumentTypeCrmId() != null) {
        crmRequest.setIdentificationProof(masterDocumentType.getDocumentTypeCrmId());
        crmRequest.setIdentificationNumber(appFormData.getAppOtherIdNumber());
      } else {
    	  crmRequest.setIdentificationProof("");
          crmRequest.setIdentificationNumber("");
      }
    } 
    crmRequest.setReferenceNumber(appFormData.getAppSeqId());
    crmRequest.setLeadSource(appFormData.getAppDataSourceId());
    crmRequest.setLeadAmount(this.SbiUtil.getFinalLoanAmount(appFormData.getAppLoanAmount()));
    
    //product and category changes
    crmRequest.setProductCode(Integer.valueOf(216));
    crmRequest.setProductCategory("HL");
    if (appFormData.getAppHomeLoanId() != null) {
    	HlProduct product = this.commonService.getHomeLoanProductById(appFormData.getAppHomeLoanId());
    	if (product != null && product.getHlProductCrmCode() != null)
    		crmRequest.setProductCode(product.getHlProductCrmCode());
      
    	if (product != null && product.getHlProductCategory() != null)
    		crmRequest.setProductCategory(product.getHlProductCategory());
    }
    
    MasterCBSResponse masterCBSResponse = this.homeLoanService.getMasterCBSResponseObjectByLoanTypeAppSeqId(appFormData.getAppSeqId());
    if (masterCBSResponse != null)
      if (masterCBSResponse.getCbsCifNumber() != null && masterCBSResponse.getCbsCifNumber().length() > 11) {
        crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber().substring(6));
      } else if (masterCBSResponse.getCbsCifNumber() != null && 
        masterCBSResponse.getCbsCifNumber().length() == 11) {
        crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber());
      }  
    crmRequest.setDuplicate(true);
    crmRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
    if (masterCBSResponse != null && 
      masterCBSResponse.getCbsResponseId() != null) {
      MasterCBSCall masterCBSCall = this.homeLoanService.getMasterCBSCallObjectByCbsResponseId(masterCBSResponse.getCbsResponseId());
      if (masterCBSCall != null && masterCBSCall.getCbsTypeOfRelationship() != null)
        crmRequest.setExistingRelationTypeId(masterCBSCall.getCbsTypeOfRelationship()); 
    } 
    if (quote.getLoanQuoteBuilderName() != null)
      crmRequest.setBuilderName(quote.getLoanQuoteBuilderName()); 
    if (quote.getLoanQuoteCostHomeFlat() != null)
      crmRequest.setCostHomeFlat(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCostHomeFlat())); 
    if (appFormData.getAppDocPickupDateDT() != null)
      crmRequest.setAppointmentDateTime(DateUtil.getDateInISO8601Format(appFormData.getAppDocPickupDateDT())); 
    if (quote.getLoanQuoteLoanCategoryId() != null) {
      MasterLoanCategory masterLoanCategory = this.commonService.getLoanCategoryById(quote.getLoanQuoteLoanCategoryId());
      if (masterLoanCategory != null)
        crmRequest.setLoanCategoryTypeId(masterLoanCategory.getLoanCategoryCrmId()); 
    } 
    if (quote.getLoanQuoteReimburse() != null)
      crmRequest.setAvailForReimbursement(quote.getLoanQuoteReimburse());
    
    if (quote.getLoanQuoteCompletionDate() != null) {
      crmRequest.setHomeCompletionDate(DateUtil.getDateTimeForCRMInString(quote.getLoanQuoteCompletionDate()));
    }
    if (quote.getLoanQuoteLandCost() != null)
      crmRequest.setCostOfLand(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteLandCost())); 
    if (quote.getLoanQuoteLoanPurposeId() != null && Arrays.<Integer>asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2) }).contains(quote.getLoanQuoteLoanPurposeId()) && "Y".equals(quote.getLoanQuoteReimburse())) {
      if (quote.getLoanQuoteAmountInvested() != null)
        crmRequest.setPropertyMarketValue(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteAmountInvested())); 
      if (quote.getLoanQuotePropertyMarketValue() != null)
        crmRequest.setAmountInvested(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuotePropertyMarketValue())); 
    } else {
      if (quote.getLoanQuoteAmountInvested() != null)
        crmRequest.setAmountInvested(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteAmountInvested())); 
      if (quote.getLoanQuotePropertyMarketValue() != null)
        crmRequest.setPropertyMarketValue(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuotePropertyMarketValue())); 
    } 
    if (quote.getLoanQuotePropertyType() != null)
      crmRequest.setLoanPropertyType(quote.getLoanQuotePropertyType()); 
    if (quote.getLoanQuoteCurrentValueOfProperty() != null)
      crmRequest.setCurrentValueOfProperty(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCurrentValueOfProperty())); 
    if (quote.getLoanQuoteYearExistingHomeLoanStartDate() != null && quote.getLoanQuoteMonthExistingHomeLoanStartDate() != null) {
      String stdate = "";
      stdate = "01-" + quote.getLoanQuoteMonthExistingHomeLoanStartDate() + "-" + quote.getLoanQuoteYearExistingHomeLoanStartDate();
      crmRequest.setExistingHomeLoanStartDate(DateUtil.getDateInISO8601Format(DateUtil.convertStringToDate(stdate)));
    } 
    if (quote.getYearTenureOfExistingHomeLoan() != null && quote.getMonthTenureOfExistingHomeLoan() != null && quote.getLoanQuoteYearExistingHomeLoanStartDate() != null) {
      String etdate = "";
      etdate = "01-" + quote.getMonthTenureOfExistingHomeLoan() + "-" + (quote.getLoanQuoteYearExistingHomeLoanStartDate().intValue() + quote.getYearTenureOfExistingHomeLoan().intValue());
      crmRequest.setExistingHomeLoanEndDate(DateUtil.getDateInISO8601Format(DateUtil.convertStringToDate(etdate)));
    } 
    if (quote.getLoanQuoteLoanWithBankId() != null) {
      MasterBank masterBank = this.commonService.getBankByBankId(quote.getLoanQuoteLoanWithBankId());
      if (masterBank != null)
        crmRequest.setExistingHomeLoanBankName(masterBank.getBankName()); 
    } 
    if (quote.getLoanQuoteOutstandingLoanAmount() != null)
      crmRequest.setOutstandingLoanAmount(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteOutstandingLoanAmount())); 
    if (quote.getLoanQuoteMortgageCreated() != null)
      crmRequest.setHasMortgageCreated(quote.getLoanQuoteMortgageCreated()); 
    if (quote.getLoanQuotePossessionCompleted() != null)
      crmRequest.setIsPossessionCompleted(quote.getLoanQuotePossessionCompleted()); 
    if (quote.getLoanQuoteRenovationCost() != null)
      crmRequest.setRenovationCost(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteRenovationCost())); 
    if (quote.getLoanQuoteEmploymentTypeId() != null) {
      MasterEmploymentType masterEmpoymentType = this.commonService.getEmploymentTypeById(quote.getLoanQuoteEmploymentTypeId());
      if (masterEmpoymentType != null)
        crmRequest.setEmploymentTypeId(masterEmpoymentType.getEmploymentTypeCrmId()); 
    } 
    if (quote.getLoanQuoteNetMonthlySalary() != null)
      crmRequest.setNetMonthlySalary(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteNetMonthlySalary())); 
    if (quote.getLoanQuoteVariableMonthPay() != null)
      crmRequest.setVariableMonthPay(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteVariableMonthPay())); 
    if (quote.getLoanQuoteExpectedRentalIncome() != null)
      crmRequest.setExpectedRentalIncome(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteExpectedRentalIncome())); 
    if (quote.getLoanQuoteOtherIncome() != null)
      crmRequest.setOtherIncome(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteOtherIncome())); 
    if (quote.getLoanQuotePreEMIsOther() != null && quote.getLoanQuotePreEMIs() != null) {
      crmRequest.setCurrentEmiPaying(this.SbiUtil.convertDoubleToBigDecimalValue(Double.valueOf(quote.getLoanQuotePreEMIs().doubleValue() + quote.getLoanQuotePreEMIsOther().doubleValue())));
    } else if (quote.getLoanQuotePreEMIs() != null) {
      crmRequest.setCurrentEmiPaying(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuotePreEMIs()));
    } else if (quote.getLoanQuotePreEMIsOther() != null) {
      crmRequest.setCurrentEmiPaying(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuotePreEMIsOther()));
    } 
    if (quote.getLoanQuoteRetirementAgeApplicant() != null)
      crmRequest.setRetirementAgeApplicant(quote.getLoanQuoteRetirementAgeApplicant()); 
    if (quote.getLoanQuoteIndustryType() != null) {
      MasterIndustryType masterIndustryType = this.commonService.getIndustryTypeById(quote.getLoanQuoteIndustryType());
      if (masterIndustryType != null && masterIndustryType.getIndustryTypeCrmId() != null) {
        crmRequest.setIndustryType(masterIndustryType.getIndustryTypeCrmId());
      } else if (quote.getLoanQuoteIndustryType().intValue() == Constants.OTHER_ID_INTEGER.intValue()) {
        crmRequest.setIndustryType("O");
      } 
    } 
    if (quote.getLoanQuoteCheckOffType() != null)
      crmRequest.setCheckOffType(quote.getLoanQuoteCheckOffType()); 
    if (quote.getLoanQuoteEmployerName() != null) {
      crmRequest.setEmployerName(quote.getLoanQuoteEmployerName());
    } 

    if (quote.getLoanQuoteHaveSalaryAccountWithSbi() != null)
      if ("Y".equalsIgnoreCase(quote.getLoanQuoteHaveSalaryAccountWithSbi())) {
        crmRequest.setHaveSalaryAccountWithSbi("Yes");
      } else if ("N".equalsIgnoreCase(quote.getLoanQuoteHaveSalaryAccountWithSbi())) {
        crmRequest.setHaveSalaryAccountWithSbi("No");
      }  
    if (quote.getLoanQuoteProfitAfterTax() != null)
      crmRequest.setProfitAfterTax(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteProfitAfterTax())); 
    if (quote.getLoanQuoteDepreciation() != null)
      crmRequest.setDepreciation(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteDepreciation())); 
    if (quote.getLoanQuoteNetAnnualIncome() != null)
      crmRequest.setNetAnnualIncome(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteNetAnnualIncome())); 
    if (quote.getLoanQuoteNetMonthlyPension() != null)
      crmRequest.setNetMonthlyPension(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteNetMonthlyPension())); 
    if (quote.getLoanQuoteIncomeFromRegularSource() != null)
      crmRequest.setIncomeFromRegularSource(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteIncomeFromRegularSource())); 
    if (appFormData.getAppLoanMaxAmont() != null)
      crmRequest.setLoanMaxAmount(this.SbiUtil.getFinalLoanAmount(appFormData.getAppLoanMaxAmont())); 
    if (appFormData.getAppLoanTenure() != null)
      crmRequest.setLoanTenure(Integer.valueOf(appFormData.getAppLoanTenure().intValue() * 12)); 
    if (appFormData.getAppLoanInterestRateDiscount() != null && appFormData.getAppLoanInterestRateDiscount().intValue() > 0) {
      crmRequest.setLoanInterestRate(appFormData.getAppLoanInterestRateDiscount());
    } else if (appFormData.getAppLoanInterestRate() != null) {
      crmRequest.setLoanInterestRate(appFormData.getAppLoanInterestRate());
    } 
    if (appFormData.getAppLoanEmiDiscount() != null && appFormData.getAppLoanEmiDiscount().intValue() > 0) {
      crmRequest.setLoanEmi(new BigDecimal(Math.round(appFormData.getAppLoanEmiDiscount())));
    } else if (appFormData.getAppLoanEmi() != null) {
      crmRequest.setLoanEmi(new BigDecimal(Math.round(appFormData.getAppLoanEmi())));
    } 
    if (appFormData.getAppLoanProcessingFeeDiscount() != null) {
      crmRequest.setLoanProcessingFee(this.SbiUtil.convertDoubleToBigDecimalValue(appFormData.getAppLoanProcessingFeeDiscount()));
    } else if (appFormData.getAppLoanProcessingFee() != null) {
      crmRequest.setLoanProcessingFee(this.SbiUtil.convertDoubleToBigDecimalValue(appFormData.getAppLoanProcessingFee()));
    } 
    if (appFormData.getAppLoanAccountType() != null)
      crmRequest.setLoanAccountType(appFormData.getAppLoanAccountType()); 
    if (appFormData.getAppPincode() != null)
      crmRequest.setResPincode(appFormData.getAppPincode()); 
    if (appFormData.getAppResidenceType() != null)
      crmRequest.setResidenceType(appFormData.getAppResidenceType()); 
    if (appFormData.getAppPropertyAddress1() != null)
      crmRequest.setPropertyAddress1(appFormData.getAppPropertyAddress1()); 
    if (appFormData.getAppPropertyAddress2() != null)
      crmRequest.setPropertyAddress2(appFormData.getAppPropertyAddress2()); 
    if (appFormData.getAppPropertyAddressLandmark() != null)
      crmRequest.setPropertyAddressLandmark(appFormData.getAppPropertyAddressLandmark()); 
    if (appFormData.getAppPropertyCityId() != null && appFormData.getAppPropertyCityId().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
      MasterCity city = this.commonService.getCityById(appFormData.getAppPropertyCityId());
      if (city != null)
        crmRequest.setPropertyCity(city.getCityName()); 
    } 
    if (appFormData.getAppPropertyDistrictId() != null) {
      MasterDistrict district = this.commonService.getDistrictById(appFormData.getAppPropertyDistrictId());
      if (district != null)
        crmRequest.setPropertyDistrict(district.getDistrictName()); 
    } 
    if (appFormData.getAppPropertyStateId() != null) {
      MasterState state = this.commonService.getStateById(appFormData.getAppPropertyStateId());
      if (state != null)
        crmRequest.setPropertyState(state.getStateName()); 
    } 
    if (appFormData.getAppPropertyPincode() != null)
      crmRequest.setPropertyPincode(appFormData.getAppPropertyPincode()); 
    if (appFormData.getAppLoanEmployerName() != null) {
      crmRequest.setEmployerName(appFormData.getAppLoanEmployerName());
    } 
    if (appFormData.getAppCompanyJoiningDate() != null)
      crmRequest.setCompanyJoiningDate(DateUtil.getDateInISO8601Format(appFormData.getAppCompanyJoiningDate())); 
    if (appFormData.getAppWorkExperienceYear() != null && 
      appFormData.getAppWorkExperienceYear().intValue() < 13) {
      Integer totalWorkExpInMonths = Integer.valueOf((appFormData.getAppWorkExperienceYear().intValue() - 1) * 12);
      if (appFormData.getAppWorkExperienceMonth() != null && 
        appFormData.getAppWorkExperienceMonth().intValue() < 13)
        totalWorkExpInMonths = Integer.valueOf(totalWorkExpInMonths.intValue() + appFormData.getAppWorkExperienceMonth().intValue() - 1); 
      if (totalWorkExpInMonths.intValue() <= 12) {
        totalWorkExpInMonths = Integer.valueOf(11);
      } else if (totalWorkExpInMonths.intValue() >= 144) {
        totalWorkExpInMonths = Integer.valueOf(145);
      } 
      crmRequest.setTotalWorkExperience(totalWorkExpInMonths);
    } 
    if ("2".equals(crmRequest.getResidentType())) {
      if (appFormData.getAppEMPNRIAddress1() != null)
        crmRequest.setOfficeAddress1(appFormData.getAppEMPNRIAddress1()); 
      if (appFormData.getAppEMPNRIAddress2() != null)
        crmRequest.setOfficeAddress2(appFormData.getAppEMPNRIAddress2()); 
      if (appFormData.getAppEMPNRICity() != null)
        crmRequest.setOfficeCity(appFormData.getAppEMPNRICity()); 
      if (appFormData.getAppEMPNRIState() != null)
        crmRequest.setOfficeState(appFormData.getAppEMPNRIState()); 
      if (appFormData.getAppEMPCountryId() != null) {
        MasterCountry masterCountry = this.commonService.getCountryById(appFormData.getAppEMPCountryId());
        if (masterCountry != null)
          crmRequest.setEmploymentCountry(masterCountry.getCountryName()); 
      } 
      if (appFormData.getAppEMPNRIZipcode() != null)
        crmRequest.setOfficePincode(Integer.valueOf(Integer.parseInt(appFormData.getAppEMPNRIZipcode()))); 
    } else {
      crmRequest.setEmploymentCountry("India");
      if (appFormData.getAppOfficeAddress1() != null)
        crmRequest.setOfficeAddress1(appFormData.getAppOfficeAddress1()); 
      if (appFormData.getAppOfficeAddress2() != null)
        crmRequest.setOfficeAddress2(appFormData.getAppOfficeAddress2()); 
      if (appFormData.getAppOfficeCityId() != null && appFormData.getAppOfficeCityId().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
        MasterCity city = this.commonService.getCityById(appFormData.getAppOfficeCityId());
        if (city != null)
          crmRequest.setOfficeCity(city.getCityName()); 
      } 
      if (appFormData.getAppOfficeStateId() != null && 
        appFormData.getAppOfficeStateId() != null) {
        MasterState state = this.commonService.getStateById(appFormData.getAppOfficeStateId());
        if (state != null)
          crmRequest.setOfficeState(state.getStateName()); 
      } 
      if (appFormData.getAppEMPCountryId() != null) {
        MasterCountry masterCountry = this.commonService.getCountryById(appFormData.getAppEMPCountryId());
        if (masterCountry != null)
          crmRequest.setEmploymentCountry(masterCountry.getCountryName()); 
      } 
      if (appFormData.getAppOfficePincode() != null)
        crmRequest.setOfficePincode(appFormData.getAppOfficePincode()); 
    } 
    if (appFormData.getAppOfficePhoneNumber() != null) {
      String officePhoneNumber = "";
      if (appFormData.getAppOfficePhoneStdCode() != null)
        officePhoneNumber = appFormData.getAppOfficePhoneStdCode(); 
      officePhoneNumber = String.valueOf(officePhoneNumber) + appFormData.getAppOfficePhoneNumber();
      crmRequest.setOfficePhoneNumber(officePhoneNumber);
    } 
    if (appFormData.getAppNextCallTime() != null)
      crmRequest.setNextCallTime(DateUtil.getDateInISO8601Format(appFormData.getAppNextCallTime())); 
    if (quote.getLoanQuoteNewVisitId() != null) {
      logger.info("Visit Id====" + quote.getLoanQuoteNewVisitId() + " for appSeqId===" + appFormData.getAppSeqId());
      TrackVisit trackVisitData = this.commonService.getVisitDetails(quote.getLoanQuoteNewVisitId());
      if (trackVisitData != null) {
        logger.info("VisitCampaignId====" + trackVisitData.getVisitCampaignId() + " for appSeqId===" + appFormData.getAppSeqId());
        if (trackVisitData.getVisitCampaignId() != null) {
          TrackCampaign trackCampaignData = this.commonService.getCampaignDetails(trackVisitData.getVisitCampaignId());
          if (trackCampaignData != null) {
            logger.info("CampaignTitle====" + trackCampaignData.getCampaignTitle() + " for appSeqId===" + appFormData.getAppSeqId());
            if (trackCampaignData.getCampaignTitle() != null) {
              crmRequest.setCampaignName(trackCampaignData.getCampaignTitle());
              logger.info("CampaignMainCampaignId====" + trackCampaignData.getCampaignMainCampaignId() + " for appSeqId===" + appFormData.getAppSeqId());
              Integer campaignMainCampaignId = new Integer(trackCampaignData.getCampaignMainCampaignId());
              if (campaignMainCampaignId != null) {
                TrackMainCampaign trackMainCampaignData = this.commonService.getMainCampaignDetails(campaignMainCampaignId);
                logger.info("MainCampaignName====" + trackMainCampaignData.getMainCampaignName() + " for appSeqId===" + appFormData.getAppSeqId());
                crmRequest.setCampaignTitle(trackMainCampaignData.getMainCampaignName());
                logger.info("MainCampaignPlacementSrcId====" + trackMainCampaignData.getMainCampaignPlacementSrcId() + " for appSeqId===" + appFormData.getAppSeqId());
                TrackCampaignPlacementSource trackCampaignPlacementSource = this.commonService.getCampaignPlacementSourceDetails(Integer.valueOf(trackMainCampaignData.getMainCampaignPlacementSrcId()));
                logger.info("PlacementSourceName====" + trackCampaignPlacementSource.getPlacementSourceName() + " for appSeqId===" + appFormData.getAppSeqId());
                if (trackCampaignPlacementSource.getPlacementSourceName() != null)
                  crmRequest.setPlacementSource(trackCampaignPlacementSource.getPlacementSourceName()); 
              } 
            } 
          } 
        } 
      } 
    } 
    crmRequest.setCoAppExist1("N");
    crmRequest.setCoAppExist2("N");
    if (quote.getLoanQuoteCoapplicantFirstId() != null && quote.getLoanQuoteCoapplicantFirstId().intValue() == 1) {
      crmRequest.setCoAppExist1("Y");
      if (quote.getLoanQuoteCoapplicantFirstRelationshipId() != null) {
        MasterCoApplicant masterCoApplicant = this.commonService.getCoApplicantById(quote.getLoanQuoteCoapplicantFirstRelationshipId());
        if (masterCoApplicant != null && masterCoApplicant.getCoapplicantCrmId() != null)
          crmRequest.setCoAppRelationTypeId1(masterCoApplicant.getCoapplicantCrmId()); 
      } 
      if (appFormData.getAppCoapplicantFirstName_1() != null)
        crmRequest.setCoAppFirstName1(appFormData.getAppCoapplicantFirstName_1()); 
      if (appFormData.getAppCoapplicantMiddleName1() != null)
        crmRequest.setCoAppMiddleName1(appFormData.getAppCoapplicantMiddleName1()); 
      if (appFormData.getAppCoapplicantLastName_1() != null)
        crmRequest.setCoAppLastName1(appFormData.getAppCoapplicantLastName_1()); 
      if (quote.getLoanQuoteCoapplicantFirstDateOfBirthDT() != null)
        crmRequest.setCoAppDOB1(DateUtil.getDateInISO8601Format(quote.getLoanQuoteCoapplicantFirstDateOfBirthDT())); 
      if (quote.getLoanQuoteCoapplicantFirstGender() != null)
        if (quote.getLoanQuoteCoapplicantFirstGender().equals("M")) {
          crmRequest.setCoAppGender1("1");
        } else if (quote.getLoanQuoteCoapplicantFirstGender().equals("F")) {
          crmRequest.setCoAppGender1("2");
        } else if (quote.getLoanQuoteCoapplicantFirstGender().equals("O")) {
          crmRequest.setCoAppGender1("3");
        }
      if (quote.getLoanQuoteCoapplicantFirstResidentTypeId() != null)
        crmRequest.setCoAppResidentTypeId1(quote.getLoanQuoteCoapplicantFirstResidentTypeId()); 
      if (quote.getLoanQuoteCoapplicantFirstEmploymentTypeId() != null) {
        MasterEmploymentType masterEmpoymentType = this.commonService.getEmploymentTypeById(quote.getLoanQuoteCoapplicantFirstEmploymentTypeId());
        if (masterEmpoymentType != null)
          crmRequest.setCoAppEmploymentTypeId1(masterEmpoymentType.getEmploymentTypeCrmId()); 
      } 
      if (quote.getLoanQuoteCoapplicantFirstCountryId() != null) {
        MasterCountry masterCountry = this.commonService.getCountryById(quote.getLoanQuoteCoapplicantFirstCountryId());
        if (masterCountry != null)
          crmRequest.setCoAppCountry1(masterCountry.getCountryName()); 
      } else {
        crmRequest.setCoAppCountry1("India");
      } 
      if (quote.getLoanQuoteCoapplicantFirstMonthlySalary() != null)
        crmRequest.setCoAppNetMonthlySalary1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstMonthlySalary())); 
      if (quote.getLoanQuoteCoapplicantFirstVariableMonthPayon() != null)
        crmRequest.setCoAppVariableMonthPay1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstVariableMonthPayon())); 
      if (quote.getLoanQuoteCoapplicantFirstOtherIncome() != null)
        crmRequest.setCoAppOtherIncome1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstOtherIncome())); 
      if (quote.getLoanQuoteCoapplicantFirstPreEMIs() != null)
        crmRequest.setCoAppCurrentEmiPaying1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstPreEMIs())); 
      if (quote.getLoanQuoteCoapplicantFirstProfitAfterTax() != null)
        crmRequest.setCoAppProfitAfterTax1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstProfitAfterTax())); 
      if (quote.getLoanQuoteCoapplicantFirstDepreciatiation() != null)
        crmRequest.setCoAppDepreciation1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstDepreciatiation())); 
      if (quote.getLoanQuoteCoapplicantFirstNetAnnualIncome() != null)
        crmRequest.setCoAppNetAnnualIncome1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstNetAnnualIncome())); 
      if (quote.getLoanQuoteCoapplicantFirstNetMonthlyPension() != null)
        crmRequest.setCoAppNetMonthlyPension1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstNetMonthlyPension())); 
      if (quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource() != null)
        crmRequest.setCoAppIncomeFromRegularSource1(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantFirstIncomeFromOtherSource())); 
      if (quote.getLoanQuoteCoapplicantFirstRetirementAge() != null)
        crmRequest.setCoAppRetirementAge1(quote.getLoanQuoteCoapplicantFirstRetirementAge()); 
      if (appFormData.getAppCoapplicantAddress_1_1() != null)
        crmRequest.setCoAppAddress1_1(appFormData.getAppCoapplicantAddress_1_1()); 
      if (appFormData.getAppCoapplicantAddress_2_1() != null)
        crmRequest.setCoAppAddress2_1(appFormData.getAppCoapplicantAddress_2_1()); 
      if (appFormData.getAppCoapplicantLandmark_1() != null)
        crmRequest.setCoAppAddressLandmark1(appFormData.getAppCoapplicantLandmark_1()); 
      if (appFormData.getAppCoapplicantCity_id_1() != null && appFormData.getAppCoapplicantCity_id_1().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
        MasterCity city = this.commonService.getCityById(appFormData.getAppCoapplicantCity_id_1());
        if (city != null)
          crmRequest.setCoAppCity1(city.getCityName()); 
      } 
      if (appFormData.getAppCoapplicantDistrictId1() != null) {
        MasterDistrict district = this.commonService.getDistrictById(appFormData.getAppCoapplicantDistrictId1());
        if (district != null)
          crmRequest.setCoAppDistrict1(district.getDistrictName()); 
      } 
      if (appFormData.getAppCoapplicantState_id_1() != null) {
        MasterState state = this.commonService.getStateById(appFormData.getAppCoapplicantState_id_1());
        if (state != null)
          crmRequest.setCoAppState1(state.getStateName()); 
      } 
      if (appFormData.getAppCoapplicantPincode_1() != null)
        crmRequest.setCoAppPincode1(appFormData.getAppCoapplicantPincode_1()); 
      if (appFormData.getAppCoapplicantPanCardNo_1() != null)
        crmRequest.setCoAppPAN1(appFormData.getAppCoapplicantPanCardNo_1()); 
      if (quote.getLoanQuoteCoapplicantFirstWorkExperience() != null)
        crmRequest.setCoAppWorkExperienceId1(quote.getLoanQuoteCoapplicantFirstWorkExperience()); 
    } 
    if (quote.getLoanQuoteCoapplicantSecondId() != null && quote.getLoanQuoteCoapplicantSecondId().intValue() == 1) {
      crmRequest.setCoAppExist2("Y");
      if (quote.getLoanQuoteCoapplicantSecondRelationshipId() != null) {
        MasterCoApplicant masterCoApplicant = this.commonService.getCoApplicantById(quote.getLoanQuoteCoapplicantSecondRelationshipId());
        if (masterCoApplicant != null && masterCoApplicant.getCoapplicantCrmId() != null)
          crmRequest.setCoAppRelationTypeId2(masterCoApplicant.getCoapplicantCrmId()); 
      } 
      if (appFormData.getAppCoapplicantFirstName_2() != null)
        crmRequest.setCoAppFirstName2(appFormData.getAppCoapplicantFirstName_2()); 
      if (appFormData.getAppCoapplicantMiddleName2() != null)
        crmRequest.setCoAppMiddleName2(appFormData.getAppCoapplicantMiddleName2()); 
      if (appFormData.getAppCoapplicantLastName_2() != null)
        crmRequest.setCoAppLastName2(appFormData.getAppCoapplicantLastName_2()); 
      if (quote.getLoanQuoteCoapplicantSecondDateOfBirthDT() != null)
        crmRequest.setCoAppDOB2(DateUtil.getDateInISO8601Format(quote.getLoanQuoteCoapplicantSecondDateOfBirthDT())); 
      if (quote.getLoanQuoteCoapplicantSecondGender() != null)
        if (quote.getLoanQuoteCoapplicantSecondGender().equals("M")) {
          crmRequest.setCoAppGender2("1");
        } else if (quote.getLoanQuoteCoapplicantSecondGender().equals("F")) {
          crmRequest.setCoAppGender2("2");
        } else if (quote.getLoanQuoteCoapplicantSecondGender().equals("O")) {
          crmRequest.setCoAppGender2("3");
        }  
      if (quote.getLoanQuoteCoapplicantSecondResidentTypeId() != null)
        crmRequest.setCoAppResidentTypeId2(quote.getLoanQuoteCoapplicantSecondResidentTypeId()); 
      if (quote.getLoanQuoteCoapplicantSecondEmploymentTypeId() != null) {
        MasterEmploymentType masterEmpoymentType = this.commonService.getEmploymentTypeById(quote.getLoanQuoteCoapplicantSecondEmploymentTypeId());
        if (masterEmpoymentType != null)
          crmRequest.setCoAppEmploymentTypeId2(masterEmpoymentType.getEmploymentTypeCrmId()); 
      } 
      if (quote.getLoanQuoteCoapplicantSecondCountryId() != null) {
        MasterCountry masterCountry = this.commonService.getCountryById(quote.getLoanQuoteCoapplicantSecondCountryId());
        if (masterCountry != null)
          crmRequest.setCoAppCountry2(masterCountry.getCountryName()); 
      } else {
        crmRequest.setCoAppCountry2("India");
      } 
      if (quote.getLoanQuoteCoapplicantSecondMonthlySalary() != null)
        crmRequest.setCoAppNetMonthlySalary2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondMonthlySalary())); 
      if (quote.getLoanQuoteCoapplicantSecondVariableMonthPayon() != null)
        crmRequest.setCoAppVariableMonthPay2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondVariableMonthPayon())); 
      if (quote.getLoanQuoteCoapplicantSecondOtherIncome() != null)
        crmRequest.setCoAppOtherIncome2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondOtherIncome())); 
      if (quote.getLoanQuoteCoapplicantSecondPreEMIs() != null)
        crmRequest.setCoAppCurrentEmiPaying2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondPreEMIs())); 
      if (quote.getLoanQuoteCoapplicantSecondProfitAfterTax() != null)
        crmRequest.setCoAppProfitAfterTax2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondProfitAfterTax())); 
      if (quote.getLoanQuoteCoapplicantSecondDepreciatiation() != null)
        crmRequest.setCoAppDepreciation2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondDepreciatiation())); 
      if (quote.getLoanQuoteCoapplicantSecondNetAnnualIncome() != null)
        crmRequest.setCoAppNetAnnualIncome2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondNetAnnualIncome())); 
      if (quote.getLoanQuoteCoapplicantSecondNetMonthlyPension() != null)
        crmRequest.setCoAppNetMonthlyPension2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondNetMonthlyPension())); 
      if (quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource() != null)
        crmRequest.setCoAppIncomeFromRegularSource2(this.SbiUtil.convertDoubleToBigDecimalValue(quote.getLoanQuoteCoapplicantSecondIncomeFromOtherSource())); 
      if (quote.getLoanQuoteCoapplicantSecondRetirementAge() != null)
        crmRequest.setCoAppRetirementAge2(quote.getLoanQuoteCoapplicantSecondRetirementAge()); 
      if (appFormData.getAppCoapplicantAddress_1_2() != null)
        crmRequest.setCoAppAddress1_2(appFormData.getAppCoapplicantAddress_1_2()); 
      if (appFormData.getAppCoapplicantAddress_2_2() != null)
        crmRequest.setCoAppAddress2_2(appFormData.getAppCoapplicantAddress_2_2()); 
      if (appFormData.getAppCoapplicantLandmark_2() != null)
        crmRequest.setCoAppAddressLandmark2(appFormData.getAppCoapplicantLandmark_2()); 
      if (appFormData.getAppCoapplicantCity_id_2() != null && appFormData.getAppCoapplicantCity_id_2().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
        MasterCity city = this.commonService.getCityById(appFormData.getAppCoapplicantCity_id_2());
        if (city != null)
          crmRequest.setCoAppCity2(city.getCityName()); 
      } 
      if (appFormData.getAppCoapplicantDistrictId2() != null) {
        MasterDistrict district = this.commonService.getDistrictById(appFormData.getAppCoapplicantDistrictId2());
        if (district != null)
          crmRequest.setCoAppDistrict2(district.getDistrictName()); 
      } 
      if (appFormData.getAppCoapplicantState_id_2() != null) {
        MasterState state = this.commonService.getStateById(appFormData.getAppCoapplicantState_id_2());
        if (state != null)
          crmRequest.setCoAppState2(state.getStateName()); 
      } 
      if (appFormData.getAppCoapplicantPincode_2() != null)
        crmRequest.setCoAppPincode2(appFormData.getAppCoapplicantPincode_2()); 
      if (appFormData.getAppCoapplicantPanCardNo_2() != null)
        crmRequest.setCoAppPAN2(appFormData.getAppCoapplicantPanCardNo_2()); 
      if (quote.getLoanQuoteCoapplicantSecondWorkExperience() != null)
        crmRequest.setCoAppWorkExperienceId2(quote.getLoanQuoteCoapplicantSecondWorkExperience()); 
    }
    
    Integer leadSourceKey = commonService.getLeadSourceKey(appFormData);
    crmRequest.setLeadSourceKey(leadSourceKey);
    
    String alternateNo = "";
    if (appFormData.getAppAlternateMobileNumber() != null) {
    	alternateNo = "+" + appFormData.getAppAltISDCode() + "-" + appFormData.getAppAlternateMobileNumber();
    }
    crmRequest.setAlternateMobileNumber(alternateNo);
    
    Optional<MarTech> marTechDeatails = Optional.ofNullable(marTechDao.getDetailsByVisitId(homeLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));
	logger.info("HomeProcessManagerImpl.java :: LNo :: 4461 ::marTechDetails is:::" + marTechDeatails);
	if(marTechDeatails.isPresent()) {
		MarTech martech = marTechDeatails.get();
		crmRequest.setCampaignCode(martech.getCampaignCode());
		crmRequest.setOfferCode(martech.getOfferCode());
		crmRequest.setTrackingCode(martech.getTrackingCode());
		logger.info("HomeProcessManagerImpl.java :: LNo :: 4467 ::campaignCode is:::" + crmRequest.getCampaignCode());
		logger.info("HomeProcessManagerImpl.java :: LNo :: 4468 ::offerCode is:::" + crmRequest.getOfferCode());
		logger.info("HomeProcessManagerImpl.java :: LNo :: 4469 ::trackingCode is:::" + crmRequest.getTrackingCode());
	}
	logger.info("HomeProcessManagerImpl.java :: LNo :: 4471 ::trackingCode is:::" + crmRequest.getTrackingCode());
    	
    return crmRequest;
  }
  
  public JSONObject verifyConcentOtp(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException, NoResultException, SQLException {
    JSONObject json = new JSONObject();
    int appOTPVerifyType = 0;
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getHomeLoanApplicationSequenceId() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3803");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    ApplicationFormHomeLoan appForm = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(SessionUtil.getHomeLoanApplicationSequenceId());
    if (appForm == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3981");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppOTPAttemptCount() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3821");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppISDCode() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3832");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppMobileNo() == null) {
      logger.info("HomeProcessManagerImpl LNO :: 3841");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = appForm.getAppOTPAttemptCount();
    if (stateId.intValue() == 41)
      try {
        if (alertCount.intValue() >= 5) {
          logger.info("HomeProcessManagerImpl LNO :: 3854");
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          appForm.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(appForm.getAppMobileNo()));
         // logger.info("mobile number ::" + appForm.getAppMobileNo() + " with AppSeqId " + appForm.getAppSeqId());
         // logger.info("mobile OTP ::" + appForm.getAppMobileVerificationCode() + " with AppSeqId " + appForm.getAppSeqId());
        } else {
          logger.info("HomeProcessManagerImpl LNO :: 4529");
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
        String msgBody = null;
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
        appForm = this.homeLoanService.save(appForm);
        if (appForm == null) {
          logger.info("HomeProcessManagerImpl LNO :: 4523 Sorry for the inconvenience with AppSeqId is null");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (JSONException e) {
          logger.info("HomeProcessManagerImpl.java LNO 3923::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       }   
    if (stateId.intValue() == 42)
      try {
        Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("HomeProcessManagerImpl.java LNO 3933:: with AppSeqId " + appForm.getAppSeqId());
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
            //logger.info("OTP verfied for mobileNo ::" + (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim() + " with AppSeqId " + appForm.getAppSeqId());
          } else {
            logger.info("HomeProcessManagerImpl.java LNO 3964:: with AppSeqId " + appForm.getAppSeqId());
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("HomeProcessManagerImpl.java LNO 3971:: with AppSeqId " + appForm.getAppSeqId());
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(appForm.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID.intValue());
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
            logger.info("HomeProcessManagerImpl.java LNO 3993:: with AppSeqId " + appForm.getAppSeqId());
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          this.homeLoanHelper.insertCallLog(appForm.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE171_ID, null, null, true);
          appForm = this.homeLoanService.save(appForm);
          if (appForm == null) {
            logger.info("HomeProcessManagerImpl.java LNO 4003::");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
        } else {
          logger.info("OTP is authentication failed::");
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        appForm = this.homeLoanService.save(appForm);
        if (appForm == null) {
          logger.info("HomeProcessManagerImpl.java LNO 4017::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException e) {
          logger.info("HomeProcessManagerImpl.java LNO 4042::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
      } 
    return json;
  }
  
	private boolean validateLeadName(String firstName, String middleName, String lastName) {
		
		  boolean isValidFirstName = false;
		  boolean isValidMiddleName = false;
		  boolean isValidLastName = false;
		  
		  boolean isValidLeadName = true;
			
		    if (firstName != null && !firstName.trim().equals("") && ValidatorUtil.isValidLeadName(firstName)) {
				isValidFirstName = true;
			}
			if (middleName == null || middleName.trim().equals("")) {
				isValidMiddleName = true;
			} else if (middleName != null && !middleName.trim().equals("") && ValidatorUtil.isValidLeadName(middleName)) {
				isValidMiddleName = true;
			}
			if (lastName != null && !lastName.trim().equals("") && ValidatorUtil.isValidLeadName(lastName)) {
				isValidLastName = true;
			}

		    if (!isValidFirstName || !isValidMiddleName || !isValidLastName) {
		    	isValidLeadName = false;
			}
		    
		    return isValidLeadName;
	}
	
	//new code for cibil
	 public ApplicationFormHomeLoanQuote cibilCall(ApplicationFormHomeLoanQuote quote, ApplicationFormHomeLoan application) {
		   try {
	           
			   BureauLinkRequestNew bureauLinkRequestnew =bureauLinkUtil.prepareBureaulinkRequestForHomeLoan(quote, application); 
			   
			   BureauLinkRequestResponse requestResponseData = bureauLinkUtil.saveBureauLinkRequestNew(bureauLinkRequestnew, "A06");
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
					        
				       if (jsonResponse != null && cibilScoreMap.get("CIBILTUSC3") != null) {
				    	   quote.setLoanQuoteCibilScore(Integer.valueOf(cibilScoreMap.get("CIBILTUSC3")));
				    	   homeLoanService.save(quote);
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
		     } catch (SQLException e) {
		    	  logger.info("Exception in BureauLink Process :: " + e);
		     } catch (IOException e) {
			       logger.info("Exception in BureauLink Process :: " + e);
			 } catch (ParseException e) {
			       logger.info("Exception in BureauLink Process :: " + e);
			 } catch (NumberFormatException e) {
				   logger.info("Exception in BureauLink Process :: " + e);
			 }
		    return quote;
	 }
	 
	private boolean writePrivacyConsentToCCMS(ApplicationFormHomeLoan application, ApplicationFormHomeLoanQuote quote,LoanScenarioBean loanScenarioBean) {

		try {
			if (application == null || quote == null) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
				return false;
			}

			if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
				return false;
			}

			if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
				return false;
			}

			if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean
						.setMessage("Invalid privacy language details. Please accept SBI Privacy Notice again.");
				return false;
			}

			String mobile = application.getAppMobileNo();
			String email = application.getAppWorkEmail();

			if (!ValidatorUtil.isValid(mobile)) {
				mobile = quote.getAppMobile();
			}

			if (!ValidatorUtil.isValid(email)) {
				email = quote.getAppEmail();
			}

			if (!ValidatorUtil.isValid(mobile)) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Mobile number is required for consent write.");
				return false;
			}

			String ipAddresss = this.SbiUtil.getIPAddress();
			  String ipAddress = ipAddresss.replace(",", "");

			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest(quote.getQuoteNtbId(), mobile,
					email, ipAddress, quote.getQuotePrivacyLocale());

			consentWrite = consentService.writeConsentToCCMS(consentWrite);

			if (consentWrite == null || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
					|| !"200".equalsIgnoreCase(consentWrite.getResponseCode())) {

				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
//				return false;
				return false;
			}
			
			//send SMS to NTB customer after submit consent
			String msgBody=communicationManagerImpl.setEmailBody(25, 0, Constants.MESSAGE_TYPE_SMS, 0);
			logger.info("msgBody11::" + msgBody);
			msgBody = SbiUtil.urlEncode(msgBody);
			String SMS_TEXT = null;
			//if(Constants.COUNTRY_CODE_INDIA.equals("91")){
				SMS_TEXT=Constants.SMS_STRING_INDIAN;
			//}else{
			//	SMS_TEXT=Constants.SMS_STRING_NRI;
			//}
			SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", "91"+mobile);
			SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TYPE", "HL");
			SMS_TEXT=SMS_TEXT.replaceAll("CONSENT_ID", quote.getQuoteNtbId());
			logger.info("SMS_TEXT5::" + SMS_TEXT);
			
			communicationManagerImpl.sendSms(SMS_TEXT);

			return true;

		} catch (Exception e) {
			logger.info("Exception while calling CCMS Write API", e);
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
			return false;
		}
	}
	 
	 
	 public ConsentReadResponse getConsentReadResponse(String cifNumber) {
	      //Call CCMS API for read consent
		ConsentReadResponse readResponse = consentUtil.callCCMSConsentReadAPI(cifNumber);
		return readResponse;
	 }
	
	 public ConsentWriteLog getConsentWriteLog(String cifNumber,String consentId) {
		logger.info("cif number "+cifNumber);
		return consentUtil.getConsentWriteLog(cifNumber,consentId);
	 }
	 public List<ConsentWriteLog> getConsentRevokeData(String cifNumber,String loanType) {
		 logger.info("cif number "+cifNumber);
		 return consentUtil.getConsentRevokeData(cifNumber,loanType);
	 }	 
	 
}
