package com.mintstreet.loan.educationloan.bo.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
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

import com.mintstreet.callback.service.CallBackService;
import com.mintstreet.campaign.dao.MarTechDao;
import com.mintstreet.campaign.entity.MarTech;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.CBSCustomerInformation;
import com.mintstreet.common.bo.CBSLoanAccountInformation;
import com.mintstreet.common.bo.CRMRequest;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
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
import com.mintstreet.common.util.CRMServiceNew;
import com.mintstreet.common.util.CbsUtil;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.FileHelper;
import com.mintstreet.common.util.RefGenerateUtilEL;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.integration.edms.action.EdmsFinalServiceAction;
import com.mintstreet.integration.edms.bo.FstoreDoc;
import com.mintstreet.integration.pan.action.PanServiceAction;
import com.mintstreet.integration.pan.bo.PanApiInputParams;
import com.mintstreet.integration.pan.bo.PanApiReturnResponse;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.educationloan.util.EducationLoanHelper;
import com.mintstreet.loan.product.entity.MasterElProduct;

import freemarker.template.utility.StringUtil;

public class EducationProcessManagerImpl {
  private static final Logger logger = LogManager.getLogger(EducationProcessManagerImpl.class.getName());
  
  @Autowired
  private CommonService commonService;
  
  @Autowired
  private EducationLoanService educationLoanService;
  
  @Autowired
  private EducationLoanHelper educationLoanHelper;
  
  @Autowired
  private CommunicationManagerImpl communicationManagerImpl;
  
  @Autowired
  private TaskExecutorService taskExecutorService;
  
  @Autowired
  private SbiUtil SbiUtil;
  
  @Autowired
  private CbsUtil cbsUtil;
  
  public volatile String appRefKey;
  
  public volatile String lastReferenceNumber;
  
  @Autowired
  private CRMServiceNew crmServiceNew;
  
  @Autowired
  private RefGenerateUtilEL refGenerateUtil;
  
  @Autowired
  private CallBackService callBackService;
  
  @Autowired
  private PanServiceAction panServiceAction;
  
  @Autowired
  private MarTechDao marTechDao;
  
  @Autowired
  private EdmsFinalServiceAction edmsServiceAction;

  public double leadAmt;
  public double chosenAmt;
  public int loanTenure;
  
  public JSONObject processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId, String ajaxPostUrl, String imageName) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      ApplicationFormEducationLoan appFormData = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
      if (appFormData == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      if (ValidatorUtil.isValid(imageNo)) {
        String fullPath = "";
        if (imageNo.intValue() == 1) {
          if (appFormData.getAppPhotoIdName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + appFormData.getAppPhotoIdName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppPhotoId(null);
            appFormData.setAppPhotoIdName(null);
          } 
        } else if (imageNo.intValue() == 2) {
          if (appFormData.getAppIdentityProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + appFormData.getAppIdentityProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIdentityProofId(null);
            appFormData.setAppIdentityProofName(null);
          } 
        } else if (imageNo.intValue() == 3) {
          if (appFormData.getAppResidenceProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + appFormData.getAppResidenceProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppResidenceProofId(null);
            appFormData.setAppResidenceProofName(null);
          } 
        } else if (imageNo.intValue() == 4) {
          if (appFormData.getAppIncomeProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + appFormData.getAppIncomeProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppIncomeProofId(null);
            appFormData.setAppIncomeProofName(null);
          } 
        } else if (imageNo.intValue() == 5) {
          if (appFormData.getAppAcademicProofName() != null) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + appFormData.getAppAcademicProofName();
            FileHelper.deleteFile(fullPath);
            appFormData.setAppAcademicProofId(null);
            appFormData.setAppAcademicProofName(null);
          } else if (ValidatorUtil.isValid(imageName)) {
            fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL + Constants.EL_PDF_GENRATION_LOCATION + imageName;
            FileHelper.deleteFile(fullPath);
            appFormData.setAppAcademicProofId(null);
            appFormData.setAppAcademicProofName(null);
          } 
          appFormData.setAppEmploymentProofId(null);
          appFormData.setAppEmploymentProofName(null);
        } 
        appFormData = this.educationLoanService.save(appFormData);
        json.put("status", "success");
        json.put("message", "Document deleted.");
      } else {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      } 
    } catch (SQLException e) {
        logger.info("EducationProcessManagerImpl.java LNo: 199 :: processDeleteProductImage() :: ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
    }  
    return json;
  }
  
  public JSONObject processToDocumentPickupUploaded(Integer appSeqId, ApplicationFormEducationLoan appForm, HttpServletRequest request, Integer bankLMSUserId, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      if (appForm == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      ApplicationFormEducationLoan appFormData = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
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
      if ((ValidatorUtil.isValid(appForm.getAppAcademicProofId()) && !ValidatorUtil.isValid(appForm.getAppAcademicProofName())) || (
        !ValidatorUtil.isValid(appForm.getAppAcademicProofId()) && ValidatorUtil.isValid(appForm.getAppAcademicProofName()))) {
        json.put("status", "error");
        json.put("message", "Please select Academic proof type and upload corresponding document.|4");
        return json;
      } 
      if (ValidatorUtil.isValid(appForm.getAppAcademicProofId()))
        appFormData.setAppAcademicProofId(appForm.getAppAcademicProofId()); 
      if (ValidatorUtil.isValid(appForm.getAppAcademicProofName()))
        appFormData.setAppAcademicProofName(appForm.getAppAcademicProofName()); 
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
        
     		//logger.info("Alternate number from appForm setAppAlternateMobileNumber ::::: " + appFormData.getAppMobileNo());
     		appFormData.setAppAlternateMobileNumber(appForm.getAppMobileNo());
     		appFormData.setAppAlternateMobileNumber(appForm.getAppAlternateMobileNumber());
     		//logger.info("Alternate number from appForm getAppAlternateMobileNumber::: " + appFormData.getAppAlternateMobileNumber());  
        
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
        statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
          this.educationLoanHelper.insertCallLog(appSeqId, bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, callDocPickupTime, true);
        } 
        if (appFormData.getAppDocsEnteredTime() == null)
          appFormData.setAppDocsEnteredTime(new Date()); 
      } 
      appFormData = this.educationLoanService.save(appFormData);
      logger.info("EducationProcessImpl.java Line 360   *************after  save method  autoLoanService file" + appFormData.getAppReferenceId());
      
      FstoreDoc doc = new FstoreDoc();
      Integer branchCode = this.commonService.getBranchCodeByBranchId(appFormData.getAppBranchId());
      doc.setAppPhotoIdName(appFormData.getAppPhotoIdName());
      doc.setAppIdentityProofName(appFormData.getAppIdentityProofName());
      doc.setAppResidenceProofName(appFormData.getAppResidenceProofName());
      doc.setAppIncomeProofName(appFormData.getAppIncomeProofName());
      doc.setAppAcademicProofName(appFormData.getAppAcademicProofName());
      
      edmsServiceAction.uploadDocumentsToEDMS(doc, Constants.EDUCATION_LOAN_ID, appFormData.getAppReferenceId(), branchCode);
      
      json.put("status", "success");
      json.put("message", "Document uploaded successfully. Please proceed for the next steps.");
      if (!bankLMSUserId.equals(Constants.OTHER_ID_INTEGER) || appFormData.getAppAssignedLmsSalesUserId() != null) {
        if (bankLMSUserId.equals(Constants.OTHER_ID_INTEGER))
          bankLMSUserId = appFormData.getAppAssignedLmsSalesUserId(); 
        this.taskExecutorService.sendingSMSForEducationLoan(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE, Integer.valueOf(0), appFormData, bankLMSUserId, false);
      } 
    } catch (NullPointerException e) {
        logger.info("ELProcessImpl.java :: LNo :: 418 :: processToDocumentPickupUploaded() :: ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
     } catch (SQLException e) {
      logger.info("ELProcessImpl.java :: LNo :: 418 :: processToDocumentPickupUploaded() :: ", e);
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
    } 
    return json;
  }
  
  public JSONObject processAddCoapplicant(Integer moduleId, ApplicationFormEducationLoanQuote quote, String ajaxPostUrl) throws JSONException {
    JSONObject json = new JSONObject();
    try {
      Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } 
      Integer updatedQuoteId = this.educationLoanHelper.saveCoApplicantDetails(quote);
      if (updatedQuoteId == null) {
        json.put("status", "error");
        json.put("message", "Co-applicant not upadated try again.");
        return json;
      } 
      json.put("status", "success");
      json.put("message", "Co-applicant upadated.");
      return json;
    } catch (NullPointerException e) {
        logger.info("EducationProcessManagerImpl.java LNo: 446 :: processAddCoapplicant() :: ", e);
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        return json;
      } catch (SQLException e) {
      logger.info("EducationProcessManagerImpl.java LNo: 446 :: processAddCoapplicant() :: ", e);
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      return json;
    } 
  }
  
  public JSONObject OTP(Integer appSeqId, Integer stateId, String name, int appApplyingFrom, String isdCode, String mobile, String email, String inputOtp, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId, int appOTPVerifyType) {
    JSONObject json = new JSONObject(); 
    boolean isAlternate = false;

    try {
      if (appSeqId == null) {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
        json.put("alertCount", 0);
        return json;
      } 
      ApplicationFormEducationLoan application = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
      
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

      Integer alertCount = Integer.valueOf(0);
      if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
        if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (
          !ValidatorUtil.isValid(name) || !ValidatorUtil.isValid(mobile) || !ValidatorUtil.isValid(email))) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
        } 
        if (ValidatorUtil.isValid(name)) {
          SessionUtil.setApplicantName(name);
          application.setAppFirstName(name);
        } 
        if (ValidatorUtil.isValid(mobile)) {
			if (!isAlternate) {
				SessionUtil.setMobile(mobile);
			} else {
				SessionUtil.setalternateMobileNumber(mobile);
			}
		}
    
        if (ValidatorUtil.isValid(email)) {
          application.setAppWorkEmail(email);
          SessionUtil.setEmail(email);
        } 
        HttpServletRequest request = RequestUtil.getServletRequest();
        String resend = request.getParameter("changeM");
        
        logger.info("isAlternate  inside the education loan::: " + isAlternate);
        if (isAlternate) {
		//logger.info("isdcode chekig  inside the if (isAlternate) " + isdCode);
		application.setAppAltISDCode(isdCode);
		application.setAppAlternateMobileNumber(mobile);
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
				//logger.info("method OTP() >> is Alternate mobile number application.getAppAlternateMobileNumber()" +application.getAppAlternateMobileNumber()+ "application.getAppAltMobileVerificationCode()::" + application.getAppAltMobileVerificationCode());
				//logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 1111::" + application.getAppAlternateMobileNumber());
			} else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
				application.setAppEmailVerificationCode(
						this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
				logger.info("inside auto process impl lno  is Alternate mobile number771");
			}
			application = this.educationLoanService.save(application);
			//logger.info("after save >> is Alternate mobile number  application.getAppAlternateMobileNumber() 1111::" + application.getAppAlternateMobileNumber());

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
						logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode())+ application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
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
				application = this.educationLoanService.save(application);
				//logger.info("after save >> is Alternate mobile number  application.getAppAlternateMobileNumber() 2222::" + application.getAppAlternateMobileNumber());
			}
			json.put("status", "success");
			json.put("message", "OTP Code sent");
			json.put("alertCount", alertCount);
			return json;

		} else {
			logger.info("inside the else condition LNO 741 ");

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
				//logger.info("method OTP() >> is Alternate mobile number  application.getAppAltMobileVerificationCode()::" + application.getAppAltMobileVerificationCode());
				//logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 3333::" + application.getAppAlternateMobileNumber());
			} else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
				application.setAppEmailVerificationCode(
						this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
				logger.info("inside auto process impl lno  is Alternate mobile number771");
			}
			application.setAppAlternateMobileNumber(mobile);
			application = this.educationLoanService.save(application);

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
						logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode())+ application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
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
				//logger.info("method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 4444::" + application.getAppAlternateMobileNumber());
				application = this.educationLoanService.save(application);
			}
			json.put("status", "success");
			json.put("message", "OTP Code sent");
			json.put("alertCount", alertCount);
			return json;

		}

      }else {
        if (application.getAppMobileVerificationCode() == null || "N".equals(application.getAppMobileVerified())) {
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
              isAppFoundForDedupInApplicationStage = this.educationLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, (application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
              logger.info("ELProcessImpl.java :: LNo 553:: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
              if (isAppFoundForDedupInApplicationStage) {
                json.put("status", "error");
                json.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
                json.put("alertCount", alertCount);
                return json;
              } 
              isAppFoundForDedupInDropOffStage = this.educationLoanService.isAppFoundForDedupInDropOffStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
              logger.info("ELProcessImpl.java :: LNo 562:: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId ::" + appSeqId);
              isAppFoundForDedupInDropRejectStage = this.educationLoanService.isAppFoundForDedupInDropRejectStage((application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode(), application.getAppMobileNo(), null);
              logger.info("ELProcessImpl.java :: LNo 564:: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
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
          json.put("alertCount", alertCount);
          application.setAppOtpMobileAlertsCount(alertCount);
          application.setAppOTPVerifyType(Integer.valueOf(appOTPVerifyType));
          if (appOTPVerifyType == 0) {
            application.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(application.getAppMobileNo()));
          } else if (appOTPVerifyType == 1) {
            application.setAppEmailVerificationCode(this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
          } 
          application = this.educationLoanService.save(application);
          if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4 || stateId.intValue() == 15) {
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
					logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode())+ application.getAppMobileNo()).trim() + " is " + application.getAppMobileVerificationCode().toString());
				}
				
				
				
              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                json.put("status", "error");
                json.put("message", "OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            } else if (appOTPVerifyType == 1) {
              String msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
              msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
              msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
              msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
              msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
              msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", application.getAppEmailVerificationCode());
              msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.EDUCATION_LOAN_PRODUCT_NAME);
              boolean sendStatus = false;
              String[] emailId = { application.getAppWorkEmail() };
              sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
              logger.info("ELProcessImpl.java :: LNo 662:: msgBody " + msgBody + " with AppSeqId ::" + appSeqId);
              if (!sendStatus) {
                json.put("status", "error");
                json.put("message", "EMAIL OTP service is down");
                json.put("alertCount", alertCount);
                return json;
              } 
            } 
            application.setAppMobileVerificationCodeReceived("Y");
            application = this.educationLoanService.save(application);
          } 
          json.put("status", "success");
          json.put("message", "OTP Code sent");
          json.put("alertCount", alertCount);
          return json;
        } 
        
        json.put("status", "error");
        json.put("message", "Your mobile no. is already verified");
        json.put("alertCount", alertCount);
      }
      } else if (stateId.intValue() == 5 || stateId.intValue() == 16) {
    	  logger.info("inside else if condition with inputOtp :: "+inputOtp );
			
	        if(inputOtp !=null) {
	        	SbiUtil sbiutil=new SbiUtil();
	        	logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
	        	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
		//		logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
	        }
    	  
    		logger.info("inside else if condition with stateid :: " + stateId.intValue() + "  isAlternate flag::"
					+ isAlternate);
			if (isAlternate) {
				if (!ValidatorUtil.isValid(inputOtp)) {
					json.put("status", "error");
					json.put("message", "Please enter valid OTP.");
					json.put("alertCount", alertCount);
					return json;
				}

				logger.info("EducationProcessManagerImpl.java  LNO 764 ::  application.getAppOTPAttemptCount() : "
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
//				logger.info("application before save :: " + application);
				application = this.educationLoanService.save(application);
//				logger.info("application after save :: " + application);

				if (application == null) {
					logger.info("AutoProcessManagerImpl.java  LNO 764 :: error on saving::");
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
					json.put("alertCount", 1);
					return json;
				}
				boolean isOPTVerified = false;

				logger.info("method OTP() >> inside if condition 832 :: " + appOTPVerifyType);
			//	logger.info("application.getAppAltMobileVerificationCode()::: "
				//		+ application.getAppAltMobileVerificationCode());
				//logger.info("inputOtp::: " + inputOtp);

				if (application.getAppAltMobileVerificationCode() != null && String
						.valueOf(application.getAppAltMobileVerificationCode()).equalsIgnoreCase(inputOtp)) {
					isOPTVerified = true;
					application.setAppAltMobileVerified("Y");
					application.setAppMobileVerified("Y");

					logger.info("method OTP() >> setAppMobileVerified 3::" + application.getAppAltMobileVerified());
				}

				logger.info("isOPTVerified::: " + isOPTVerified);
				if (isOPTVerified) {
					logger.info("before OTP verification completion");
					StatusRequest statusRequest = new StatusRequest();
					statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
					statusRequest.setHaveLoanOffer(true);
					statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
					statusRequest.setState(stateId.intValue());
					statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
					statusRequest.setRsmDecision(0);
					statusRequest.setApplicationType(
							(SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue()
									: 0);
					statusRequest.setApplicationLeadType(application.getAppDataSourceId().intValue());
					StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
					if (statusManagerResponse.getStatus() != 0) {
						application.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
					} else if (application.getAppLoanStatusId().intValue() == 0) {
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					logger.info("ElProcessImpl.java Line 725 autoLoanService file OTP method"
							+ application.getAppReferenceId());
					application = this.educationLoanService.save(application);
					if (statusManagerResponse.isEligibleToInsertLog())
						this.educationLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(),
								statusManagerResponse.getStatusCallLogs(), null, null, true);
					json.put("status", "success");
					if (stateId.intValue() == 5) {
						json.put("message",
								"Thank you for your interest. Our representative will contact you shortly.");
					} else {
						//logger.info("EducationProcessManagerImpl.java  LNO 832 :: OTP verfied for mobileNo ::"
							//	+ application.getAppMobileNo() + " with AppSeqId ::" + appSeqId);
						json.put("message", "OTP authentication successful");
					}
					json.put("alertCount", alertCount);
					return json;
				}

//				logger.info("EduProcessManagerImpl.java  LNO 838 :: OTP is incorrect for mobileNo ::"
//						+ application.getAppMobileNo() + " with AppSeqId ::" + appSeqId);
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

			}
			else {
        if (!ValidatorUtil.isValid(inputOtp)) {
          json.put("status", "error");
          json.put("message", "Invalid Request OTP! send properly.");
          json.put("alertCount", alertCount);
          return json;
        } 
        logger.info("EducationProcessManagerImpl.java LNo : 686:  application.getAppOTPAttemptCount() : " + application.getAppOTPAttemptCount());
        if (!ValidatorUtil.isValid(application.getAppOTPAttemptCount()))
          application.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (application.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          json.put("alertCount", 1);
          return json;
        } 
        application.setAppOTPAttemptCount(Integer.valueOf(application.getAppOTPAttemptCount().intValue() + 1));
        application = this.educationLoanService.save(application);
        if (application == null) {
          logger.info("EducationProcessManager.java LNO 699 error on saving::");
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
          json.put("alertCount", 1);
          return json;
        } 
        application.setAppOTPAttemptCount(Integer.valueOf(application.getAppOTPAttemptCount().intValue() + 1));
        application = this.educationLoanService.save(application);
        if (application == null) {
          logger.info("EducationProcessManager.java  LNO 764 :: error on saving::");
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
        if (isOPTVerified) {
          ApplicationFormEducationLoanQuote quote = this.educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(application.getAppQuoteId());
          if (quote != null && quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
            application.setAppResTypeAtVerified(Integer.valueOf(2));
          } else {
            application.setAppResTypeAtVerified(Integer.valueOf(1));
          } 
          application.setAppOTPVerifyType(Integer.valueOf(appOTPVerifyType));
          application.setAppMobileVerified("Y");
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(application.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
          application = this.educationLoanService.save(application);
          if (statusManagerResponse.isEligibleToInsertLog())
            this.educationLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
           // logger.info("ELProcessImpl.java :: LNo :: 756 :: OTP verfied for mobileNo ::" + application.getAppMobileNo());
            json.put("message", "OTP authentication successful");
          } 
          json.put("alertCount", alertCount);
          return json;
        } 
        logger.info("ELProcessImpl.java :: LNo :: 763 ::OTP is incorrect for mobileNo ::" + application.getAppMobileNo());
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == application.getAppDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtp|2");
        } 
        json.put("alertCount", alertCount);
        return json;
      } 
      }
    } catch (NullPointerException e) {
        logger.info("EducationLoanProcessImpl.java LNo: 771 :: OTP() ", e);
        try {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("EducationLoanProcessImpl.java LNo: 777 :: OTP() ", (Throwable)e1);
        } 
      }  catch (JSONException e) {
      logger.info("EducationLoanProcessImpl.java LNo: 771 :: OTP() ", e);
      try {
        json.put("status", "error");
        json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
        json.put("alertCount", 0);
      } catch (JSONException e1) {
        logger.info("EducationLoanProcessImpl.java LNo: 777 :: OTP() ", (Throwable)e1);
      } 
    }  catch (SQLException e) {
        logger.info("EducationLoanProcessImpl.java LNo: 771 :: OTP() ", e);
        try {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("EducationLoanProcessImpl.java LNo: 777 :: OTP() ", (Throwable)e1);
        } 
      } 
    return json;
  }
  
  private JSONObject OTP(Integer appSeqId, Integer stateId, String name, int appApplyingFrom, String isdCode, String mobile, String email, String inputOtp, Integer trackVisitId, Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId, int appOTPVerifyType,Integer commonLoanType) {
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
          !ValidatorUtil.isValid(name) || !ValidatorUtil.isValid(mobile) || !ValidatorUtil.isValid(email))) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
        } 
        if ((stateId.intValue() == 14 || stateId.intValue() == 15) && 
          !ValidatorUtil.isValid(mobile)) {
          json.put("status", "error");
          json.put("message", "Invalid Request. Refresh browser to proceed ahead");
          return json;
        } 
        if (ValidatorUtil.isValid(name)) {
          SessionUtil.setApplicantName(name);
          lead.setLeadFirstName(name);
        } 
        if (appApplyingFrom == 2) {
          lead.setLeadApplyingFrom(appApplyingFrom);
          lead.setLeadIsdCode(isdCode);
          SessionUtil.setISDCode(isdCode);
          if (ValidatorUtil.isValid(mobile)) {
            lead.setLeadMobileNo(mobile);
            SessionUtil.setMobile(mobile);
          } 
        } else {
          lead.setLeadApplyingFrom(1);
          isdCode = Constants.COUNTRY_CODE_INDIA;
          lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA);
          SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
          if (ValidatorUtil.isValid(mobile)) {
            lead.setLeadMobileNo(mobile);
            SessionUtil.setMobile(mobile);
          } 
        } 
        if (!Constants.DUMMY_MOBILE_NO.contains(mobile) && !Constants.INQUIRY_DUPLICATION_CHECK.equals("0")) {
          boolean isLeadExists = false;
          isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.EDUCATION_LOAN_ID, lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo(), loanTypeId);
          if (isLeadExists) {
            json.put("status", "error");
            json.put("message", Constants.INQUIRY_DEDUPLICATION_MESSAGE);
            json.put("alertCount", alertCount);
            return json;
          } 
        } 
        if (ValidatorUtil.isValid(email)) {
          SessionUtil.setEmail(email);
          lead.setLeadWorkEmail(email);
        } 
        if (lead.getLeadMobileVerificationCode() == null || "N".equals(lead.getLeadMobileVerificationCodeVerified())) {
          if (appApplyingFrom == 2) {
            if (!lead.getLeadMobileNo().equalsIgnoreCase(mobile))
              lead.setLeadMobileAlertCount(Integer.valueOf(0)); 
          } else if (!lead.getLeadMobileNo().toString().equalsIgnoreCase(mobile)) {
            lead.setLeadMobileAlertCount(Integer.valueOf(0));
          } 
          if (stateId.intValue() == 3 || stateId.intValue() == 14) {
            alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0 : lead.getLeadMobileAlertCount().intValue());
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(lead.getLeadMobileNo()));
            lead.setLeadMobileVerificationCodeVerified("N");
            lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID));
            if (lead.getLeadSourceId() == null)
              lead.setLeadSourceId(Integer.valueOf(1)); 
            if (lead.getLeadDataSourceId() == null)
              lead.setLeadDataSourceId(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK); 
            lead.setLeadProductTypeId(Constants.EDUCATION_LOAN_ID);
            lead.setLeadLoanPurposeId(Integer.valueOf(8));
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
            lead.setLeadCreatedLmsUserId(bankLMSUserId);
            lead.setLeadVisitId(trackVisitId);
          } else if (stateId.intValue() == 4 || stateId.intValue() == 15) {
            alertCount = lead.getLeadMobileAlertCount();
            if (alertCount.intValue() >= 5) {
              json.put("status", "error");
              json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
              json.put("alertCount", alertCount);
              return json;
            } 
            lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(lead.getLeadMobileNo()));
          } 
          alertCount = Integer.valueOf(alertCount.intValue() + 1);
          lead.setLeadMobileAlertCount(alertCount);
          lead.setLeadLastUpdated(new Date());
          
      	   //customer consent NTB
     	 logger.info("commonLoanType 1343 "+commonLoanType);
         if(commonLoanType==4) {
			Integer consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "NTB").getConsentId();
			logger.info("consentTextNtb 1346 "+consentTextNtb);
		    lead.setLeadConsentId(consentTextNtb);
         }
		
          
          try {
            lead = this.commonService.save(lead);
            if (stateId.intValue() == 3)
              this.commonService.insertCallLog(lead.getLeadId(), bankLMSUserId, Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID), null); 
            json.put("status", "success");
            json.put("message", "OTP Code sent");
            json.put("alertCount", alertCount);
          } catch (SQLException e) {
            logger.info("EducationProcessManagerImpl.java LNo : 928 : Exception Caught", e);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
          } 
          SessionUtil.setLeadId(lead.getLeadId());
          if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14 || stateId.intValue() == 15) {
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
        logger.info("EducationProcessManagerImpl.java LNo : 872 lead.getLeadOTPAttempCount() : " + lead.getLeadOTPAttempCount() + " with AppSeqId ::" + appSeqId);
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
          logger.info("EducationProcessManager.java LNO 987 error on saving::");
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
          } catch (SQLException e) {
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            json.put("alertCount", alertCount);
            return json;
          } 
          logger.info("EducationProcessManagerImpl.java LNO ::919 , log for CALLBACK_SMS_CONSENT " + Constants.CALLBACK_SMS_CONSENT);
          if (Constants.CALLBACK_SMS_CONSENT) {
            logger.info("EducationProcessManagerImpl.java LNO ::922 , log for CALLBACK_SMS_CONSENT " + lead.getLeadMobileVerificationCodeVerified());
            if ("Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified())) {
              logger.info("EducationProcessManagerImpl.java LNO ::924 , log for CALLBACK_SMS_CONSENT " + Constants.MESSAGE_TYPE_SMS);
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
				logger.info("EducationProcessManagerImpl.java LNO ::1343 :: message has been sent to user " + EncryptDecryptUtil.encrypt(SMS_TEXT));
			} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
					| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
					| UnsupportedEncodingException e) {
				logger.info("EducationProcessManagerImpl.java LNO ::1291 , Exception caught ",e);
			}

              if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
                logger.info("EducationProcessManagerImpl.java LNO ::938 , OTP service is down:: msg not send");
                json.put("status", "error");
                json.put("message", "OTP service is down");
                return json;
              } 
            } 
          } 
          json.put("status", "success");
          if (stateId.intValue() == 5) {
            logger.info("ProcessManagerImpl LNO ::948 :: message has been sent to user ");
            json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
          } else {
            logger.info("OTP verfied for mobileNo ::" + lead.getLeadMobileNo() + " with AppSeqId ::" + appSeqId);
            json.put("message", "OTP authentication successful");
          } 
          json.put("alertCount", alertCount);
          logger.info("ELProcessImpl.java  LNO 1033 Before Calling Call Center Service ----" + lead);
          if (!Constants.CLICK_TO_CALL_BYPASS && 
            lead != null && lead.getLeadLoanPurposeId() != null && lead.getLeadLoanPurposeId().intValue() != 10) {
            logger.info("ELProcessImpl.java  LNO 1036----Service Response message----");
            this.callBackService.getcallBackService(lead);
            lead = this.commonService.save(lead);
          } 
          logger.info("ELProcessImpl.java  LNO 1041 after Calling Call Center Service ----" + lead);
          SessionUtil.setLeadId(null);
          json.put("alertCount", alertCount);
          return json;
        } 
        logger.info("ELProcessImpl.java :: LNo :: 1029 :: OTP is incorrect for mobileNo ::" + lead.getLeadMobileNo() + " with AppSeqId ::" + appSeqId);
        json.put("status", "error");
        if (Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP.intValue() == lead.getLeadDataSourceId().intValue()) {
          json.put("message", "OTP is incorrect! Try again.");
        } else {
          json.put("message", "OTP is incorrect! Try again.|inputOtpWantUs|2");
        } 
        json.put("alertCount", alertCount);
        return json;
      } 
    } catch (NullPointerException e) {
        logger.info("EeducationLoanProcessImpl.java LNo: 1037 :: OTP() ", e);
        try {
          json.put("status", "error");
          json.put("message", "OTP process is down");
          json.put("alertCount", 0);
          return json;
        } catch (JSONException e1) {
          logger.info("EducationLoanProcessImpl.java LNo: 1044 :: OTP() ", e);
        } 
      } catch (JSONException e) {
      logger.info("EeducationLoanProcessImpl.java LNo: 1037 :: OTP() ", e);
      try {
        json.put("status", "error");
        json.put("message", "OTP process is down");
        json.put("alertCount", 0);
        return json;
      } catch (JSONException e1) {
        logger.info("EducationLoanProcessImpl.java LNo: 1044 :: OTP() ", e);
      } 
    }  catch (SQLException e) {
        logger.info("EeducationLoanProcessImpl.java LNo: 1037 :: OTP() ", e);
        try {
          json.put("status", "error");
          json.put("message", "OTP process is down");
          json.put("alertCount", 0);
          return json;
        } catch (JSONException e1) {
          logger.info("EducationLoanProcessImpl.java LNo: 1044 :: OTP() ", e);
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
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType()); 
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

 	//logger.info(
 		//	"C>>otherRequest.getAlternateMobileNumber()::" + otherRequest.getAlternateMobileNumber());
 	//logger.info("before OTP call>>mobile::" + mobile);
    
	if (trackVisitId == null && otherRequest.getAlternateMobileNumber() == null) {
		json = OTP(appSeqId, stateId, null,  appApplyingFrom,  isdCode, mobile, email,
				inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId,appOTPVerifyType);
	}
	 if (otherRequest.getAlternateMobileNumber() != null) {
		if (!otherRequest.getAlternateMobileNumber().equals(mobile)) {
			json = OTP(appSeqId, stateId, null,  appApplyingFrom, otherRequest.getIsdCodeAlt(),
					"ALT_" + otherRequest.getAlternateMobileNumber(), email, otherRequest.getInputOtpAlt(), bankLMSUserId, ajaxPostUrl,
					loanTypeId,appOTPVerifyType);
			
		} else {
			json.put("status", "error");
			json.put("message", "Alternate Mobile cannot be same as Mobile number.");
			json.put("alertCount", 0);
		}
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
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().toString()); 
    if (otherRequest.getIsdCodeWantUsToCallYou() != null)
      isdCode = otherRequest.getIsdCodeWantUsToCallYou(); 
    if (appApplyingFrom == 2) {
      if (otherRequest.getNriMobileWantUsToCallYou() != null)
        mobile = otherRequest.getNriMobileWantUsToCallYou(); 
    } else if (otherRequest.getMobileWantUsToCallYou() != null) {
      mobile = otherRequest.getMobileWantUsToCallYou();
    } 
    int count = this.commonService.getCallBackLeadCount(Integer.valueOf(9), isdCode, mobile);
    logger.info("EducationProcessManagerImpl.java LNo : 1293 count : " + count + " with AppSeqId ::" + appSeqId);
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
      	//logger.info("DecryptedRequest inputOtp   1559  "+inputOtp);
      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
		//	logger.info("DecryptedRequest inputOtp   1563  "+inputOtp);
      }
     logger.info("(otherRequest.getCommmonloanType()   1456  "+otherRequest.getCommmonloanType());
   
    
    if (trackVisitId != null) {
      json = OTP(appSeqId, stateId, name, appApplyingFrom, isdCode, mobile, email, inputOtp, trackVisitId, bankLMSUserId, ajaxPostUrl, loanTypeId, appOTPVerifyType,otherRequest.getCommmonloanType());
    } else {
      json = OTP(appSeqId, stateId, name, appApplyingFrom, isdCode, mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId, appOTPVerifyType);
    } 
    return json;
  }
  
  public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormEducationLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) throws SQLException {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      ApplicationFormEducationLoan application = null;
      if (appSeqId != null) {
        application = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
        if (application == null) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
          return loanScenarioBean;
        } 
      } 
      if (application != null && 
        ajaxPostUrl.equalsIgnoreCase("education-loan"))
        if (application.getAppApplyingFrom() == 2) {
          if (application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo()) && application.getAppMobileVerified().equalsIgnoreCase("Y")) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            loanScenarioBean.setMessage("You have already verified OTP with NRI mobile number : " + application.getAppMobileNo());
            return loanScenarioBean;
          } 
        } else if (application.getAppMobileNo() != null && !application.getAppMobileNo().equalsIgnoreCase(quote.getAppMobile()) && application.getAppMobileVerified().equalsIgnoreCase("Y")) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("You have already verified OTP with mobile number : " + application.getAppMobileNo());
          return loanScenarioBean;
        }  
      int oldVisitId = 0;
      if (application != null && application.getAppQuoteId() != null) {
        oldVisitId = this.educationLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
      } else {
        oldVisitId = trackVisitId.intValue();
      } 
      quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      if (application != null) {
        if (application.getAppOTPVerifyType().intValue() == 0) {
          if (!"Y".equalsIgnoreCase(application.getAppMobileVerified()))
            if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
              quote.setAppApplyingFrom(1);
              quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
            } else if (quote.getAppApplyingFrom() == 2) {
              MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
              quote.setAppISDCode(country.getCountryCode().toString());
            }  
        } else if (!"Y".equalsIgnoreCase(application.getAppEmailVerified())) {
          if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
            quote.setAppApplyingFrom(1);
            quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
          } else if (quote.getAppApplyingFrom() == 2) {
            MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
            quote.setAppISDCode(country.getCountryCode().toString());
          } 
        } 
      } else if (quote.getAppApplyingFrom() == 0 || quote.getAppApplyingFrom() == 1) {
        quote.setAppApplyingFrom(1);
        quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
      } else if (quote.getAppApplyingFrom() == 2) {
        MasterCountry country = this.commonService.getCountryById(quote.getLoanQuoteCountryId());
        quote.setAppISDCode(country.getCountryCode().toString());
      } 
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && (
        appSeqId == null || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS.intValue() && "Y".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())))) {
        String oldMobile = quote.getAppMobile();
        String isdCode = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
        if (quote.getAppMobile() != null && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0"))
          if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
            boolean isLeadExists = false;
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.EDUCATION_LOAN_ID, Integer.valueOf(5), isdCode, oldMobile, loanTypeId);
            logger.info("ELProcessImpl.java:: LNo :: 1409 isLeadExists setting in session " + isLeadExists + " with AppSeqId ::" + appSeqId);
            if (isLeadExists) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
          } else {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.educationLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, null);
            logger.info("ELProcessImpl.java:: LNo :: 1418 isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.educationLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, null);
            logger.info("ELProcessImpl.java:: LNo :: 1426 isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId ::" + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.educationLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, null);
            logger.info("ELProcessImpl.java:: LNo :: 1427 isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
          }  
      } 
      Integer quoteApplyingFrom = Integer.valueOf(quote.getAppApplyingFrom());
      String quoteMobileNRI = quote.getAppNRIMobileNo();
      String quoteMobile = quote.getAppMobile();
      String quoteISD = quote.getAppISDCode();
      
      
 	 if (quote.getLoanQuoteTuitionFee() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteTuitionFee() )) {
   	  loanScenarioBean.setStatus(Integer.valueOf(0));
   	  loanScenarioBean.setMessage("Tuition Fee cannot be greater than 9 digits.|2");
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
      
      quote = this.educationLoanHelper.insertLoanQuote(quote, (bankLmsUser != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, trackVisitId, false);
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
      quote.setAppApplyingFrom(quoteApplyingFrom.intValue());
      quote.setAppMobile(quoteMobile);
      quote.setAppNRIMobileNo(quoteMobileNRI);
      quote.setAppISDCode(quoteISD);
      application = this.educationLoanHelper.insertAppLoan(quote, application, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null, Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null) ? bankLmsUser.getLmsUserIntermediaryId().intValue() : 0), false);
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
      if (quote.getLoanQuoteCountryOfStudyId() != null)
        if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID)) {
          SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_BIDYALAKHMI);
        } else if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID)) {
          SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_TAKE_OVER);
        } else if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId().intValue() == 2) {
          SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_EDVANTAGE);
        } else {
          SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_SCHOLAR);
        }  
      SessionUtil.setEducationLoanApplicationSequenceId(application.getAppSeqId());
      if (isAppFoundForDedupInDropRejectStage)
        application.setAppMobileDedup(Integer.valueOf(0)); 
      if (isAppFoundForDedupInDropOffStage)
        application.setAppMobileDedup(Integer.valueOf(1)); 
      if (SessionUtil.getApplicationCRMLeadId() != null)
        application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId()); 
      if (quote.getLoanQuoteId() != null) {
        loanScenarioBean = this.educationLoanHelper.callBRE(application, quote, bankLmsUser, Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
        if (loanScenarioBean.getStatus().intValue() != 1)
            return loanScenarioBean; 
          
        leadAmt = loanScenarioBean.getChosenEligibility();
        chosenAmt = educationLoanHelper.getChosenAmount();
        loanTenure = educationLoanHelper.getLoanTenure();
        
        logger.info("educationLoanHelper callBRE ( ) loanScenarioBean::::::"+loanScenarioBean);
        logger.info("educationLoanHelper callBRE ( ) loanScenarioBean::::::"+loanScenarioBean.getMaxEligibility());
        logger.info("educationLoanHelper callBRE ( ) loanScenarioBean::::::"+loanScenarioBean.getChosenTenure());
        logger.info("educationLoanHelper callBRE ( ) loanScenarioBean::::::"+loanScenarioBean.getChosenEligibility()+"..leadAmt.."+leadAmt+"..chosenAmt.."+chosenAmt + "..loanTenure.."+loanTenure);
        
        application = loanScenarioBean.getApplicationEL();
        if ((ajaxPostUrl.equalsIgnoreCase(Constants.SCHOLAR_LOAN_ACTION) || 
          ajaxPostUrl.equalsIgnoreCase(Constants.GLOBAL_EDVANTAGE_ACTION) || 
          ajaxPostUrl.equalsIgnoreCase(Constants.EDUCATION_LOAN_TAKEOVER_ACTION)) && 
          application.getAppMobileVerificationCodeReceived() == null) {
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
            msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.EDUCATION_LOAN_PRODUCT_NAME);
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
          application = this.educationLoanService.save(application);
        } 
          logger.info("ELProcessManagerImpl.java LNo: 2171  calling CRM ");
          if (Constants.EDUCATION_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl) && 
            application != null && 
            SessionUtil.getApplicationCRMLeadId() != null) {
        	  logger.info("LNO 1770 ");
            CRMRequest crmRequest = new CRMRequest();
            crmRequest.setCrmLeadId(SessionUtil.getApplicationCRMLeadId());
            crmRequest.setReferenceNumber(application.getAppSeqId());
            crmRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID);
          } 
        return loanScenarioBean;
      } 
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      return loanScenarioBean;
    } catch (NullPointerException e) {
        logger.info("ELProcessManagerImpl.java LNo: 2189 :: processGetQuotes()  :: ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
     } catch (RuntimeException e) {
      logger.info("ELProcessManagerImpl.java LNo: 2189 :: processGetQuotes()  :: ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
    }  catch (ParseException e) {
        logger.info("ELProcessManagerImpl.java LNo: 2189 :: processGetQuotes()  :: ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      }  
    return loanScenarioBean;
  }
  
  public ApplicationFormEducationLoan processSubmitQuote(Integer appSeqId, Integer requestIndex, ApplicationFormEducationLoan appForm, String isDsrPage, Integer bankLMSUserId) throws NoResultException, SQLException {
    ApplicationFormEducationLoan appFormData = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
    if (appFormData == null)
      return null; 
    
	logger.info("appFormData.getAppEmailVerified() 6" + appFormData.getAppEmailVerified());
	//logger.info(" EL appFormData.getAppAlternateMobileNumber()  :: " + appFormData.getAppAlternateMobileNumber());

    if ((appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK)) && 
      appFormData.getAppMobileVerified().equalsIgnoreCase("N")) {
      appFormData.setError("ELProcessImpl.java :: LNo :: 2205 :: Mobile OTP is not verified");
      return appFormData;
    } 
    ApplicationFormEducationLoanQuote quote = this.educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appFormData.getAppQuoteId());
    if (quote == null)
      return null; 
    logger.info("ELProcessImpl.java :: LNo :: 1464 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()) && 
      !Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && 
      appFormData.getAppMobileNo() != null && !Constants.DUMMY_MOBILE_NO.contains(appFormData.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")) {
      boolean isAppFoundForDedupInApplicationStage = false;
      isAppFoundForDedupInApplicationStage = this.educationLoanService.isAppFoundForDedupInApplicationStage(null, (appFormData.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : appFormData.getAppISDCode(), appFormData.getAppMobileNo(), null);
      logger.info("ELProcessImpl.java LNo 1435 :: thankyoupage isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
      if (isAppFoundForDedupInApplicationStage) {
        this.educationLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE6_ID, null, null, true);
        appFormData.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE6_ID));
        appFormData.setError(Constants.APP_DEDUPLICATION_MESSAGE);
        return appFormData;
      } 
    } 
    
	//validate name fields before saving
//	logger.info("first_name :: " + appForm.getAppFirstName());
//	logger.info("middle_name :: " + appForm.getAppMiddleName());
//	logger.info("last_name :: " + appForm.getAppLastName());
	
  /*  if (!validateLeadName(appForm.getAppFirstName(), appForm.getAppMiddleName(), appForm.getAppLastName())) {
    	appFormData.setError("Name is not in correct format.");
		return appFormData;
    }*/
	
	  if(!ValidatorUtil.validateFirstNameLength(appForm.getAppFirstName())) {
		appFormData.setError("Please enter between 2 to 20 characters in First name.");
     	return appFormData;
       }
       if(!ValidatorUtil.validateMiddleNameLength(appForm.getAppMiddleName())){
    	 appFormData.setError("Please enter between 2 to 20 characters in Middle name.");
      	 return appFormData;
       }
       if(!ValidatorUtil.validateLastNameLength(appForm.getAppLastName())) {
      	 appFormData.setError("Please enter between 2 to 20 characters in Last name.");
      	 return appFormData;
       }

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
	
 	  //added for same name validation
    if(appForm.getAppFirstName().trim().equalsIgnoreCase(appForm.getAppLastName().trim()) ||
    		(appForm.getAppMiddleName()!=null &&
    		(appForm.getAppFirstName().trim().equalsIgnoreCase(appForm.getAppMiddleName().trim()) || appForm.getAppMiddleName().trim().equalsIgnoreCase(appForm.getAppLastName().trim())))) {
		  appFormData.setError("For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
		  return appFormData;
	}

	if (!ValidatorUtil.isAddress(appForm.getAppAddress1().trim())) {
		appFormData.setError("Please enter between 3 to 40 characters in Address Line 1|appForm.appAddress1|1");
    	  return appFormData;
      }
      //if (!(appForm.getAppAddress1() != null && appForm.getAppAddress1().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
      if (!(appForm.getAppAddress1() != null && ValidatorUtil.isAddressChecker(appForm.getAppAddress1()))) {
    	  appFormData.setError("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 1|appForm.appAddress1|1");
          return appFormData;
      }
     
      if (!ValidatorUtil.isAddress(appForm.getAppAddress2().trim())) {
			appFormData.setError("Please enter between 3 to 40 characters in Address Line 2|appForm.appAddress2|1");
	    	  return appFormData;
	      }
	     // if (!(appForm.getAppAddress2() != null && appForm.getAppAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
	      if (!(appForm.getAppAddress2() != null && ValidatorUtil.isAddressChecker(appForm.getAppAddress2()))) {
	    	  appFormData.setError("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 2|appForm.appAddress2|1");
	          return appFormData;
	      }
	      
	      if(appForm.getAppAddressLandmark() != null) {
	      if (!ValidatorUtil.isAddressLandmarkALANDEL(appForm.getAppAddressLandmark().trim())) {
	    	  appFormData.setError("Please enter between 3 to 30 characters in  Landmark|appForm.appAddressLandmark|1");
	    	  return appFormData;
	        }
	      }
	      if(appForm.getAppAddressLandmark() != null) {
		      if (!appForm.getAppAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
		    	  appFormData.setError("Please enter only [a-z,0-9,(,),-,/] in  Landmark|appForm.appAddressLandmark|1");
		          return appFormData;
		      }
	      }
	      
    if(appForm.getAppPanCardNo() == null  && appForm.getAppOtherId().intValue() ==0 ) {
		appFormData.setError("Either enter PAN or select Other Identity Proof.|1");
        return appFormData;
	}
    if (appForm.getAppPanCardNo() != null && !ValidatorUtil.isValidPanNo(appForm.getAppPanCardNo() )) {
      	appFormData.setError("PAN is not in correct format.|2");
			return appFormData;
		}
	//validate name fields before saving end
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
    if (appForm.getAppDob() != null) {
      appFormData.setAppDobDT(DateUtil.changeDateFormatToDate(appForm.getAppDob().replaceAll("/", "-"), "MM-dd-yyyy"));
      appFormData.setAppDob(DateUtil.convertDateToFormattedType(appFormData.getAppDobDT(), "dd-MM-yyyy"));
    } 
    if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0)
      appFormData.setAppStateId(appForm.getAppStateId()); 
    if (appForm.getAppCityId() != null && appForm.getAppCityId().intValue() > 0)
      appFormData.setAppCityId(appForm.getAppCityId()); 
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
    appFormData.setAppNetAnnualIncome(appForm.getAppNetAnnualIncome());
    if (appForm.getAppBranchId() != null && appForm.getAppBranchId().intValue() > 0)
      appFormData.setAppBranchId(appForm.getAppBranchId()); 
    if (appForm.getAppDistrictId() != null)
      appFormData.setAppDistrictId(appForm.getAppDistrictId()); 
    if (appForm.getAppLocalityId() != null)
      appFormData.setAppLocalityId(appForm.getAppLocalityId()); 
    if (appForm.getAppEducationalQualificationId() != null)
      appFormData.setAppEducationalQualificationId(appForm.getAppEducationalQualificationId()); 
    if (appForm.getAppAdmissionQuota() != null)
      appFormData.setAppAdmissionQuota(appForm.getAppAdmissionQuota()); 
    if (appForm.getAppNetAnnualIncome() != null)
      appFormData.setAppNetAnnualIncome(appForm.getAppNetAnnualIncome()); 
    if (appForm.getAppEdvantageStateId() != null)
      appFormData.setAppEdvantageStateId(appForm.getAppEdvantageStateId()); 
    if (appForm.getAppEdvantageCityId() != null)
      appFormData.setAppEdvantageCityId(appForm.getAppEdvantageCityId()); 
    if (appForm.getAppEdvantageDistrictId() != null)
      appFormData.setAppEdvantageDistrictId(appForm.getAppEdvantageDistrictId()); 
    if (appForm.getAppEdvantageBranchId() != null) {
      appFormData.setAppEdvantageBranchId(appForm.getAppEdvantageBranchId());
      appFormData.setAppBranchId(appForm.getAppEdvantageBranchId());
    } 
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
    if (appForm.getAppPermanentDistrictId() != null)
      appFormData.setAppPermanentDistrictId(appForm.getAppPermanentDistrictId()); 
    if (appForm.getAppAbroadAddress1() != null)
      appFormData.setAppAbroadAddress1(appForm.getAppAbroadAddress1()); 
    if (appForm.getAppAbroadAddress2() != null)
      appFormData.setAppAbroadAddress2(appForm.getAppAbroadAddress2()); 
    if (appForm.getAppAbroadAddressLandMark() != null)
      appFormData.setAppAbroadAddressLandMark(appForm.getAppAbroadAddressLandMark()); 
    if (appForm.getAppAbroadCity() != null)
      appFormData.setAppAbroadCity(appForm.getAppAbroadCity()); 
    if (appForm.getAppAbroadState() != null)
      appFormData.setAppAbroadState(appForm.getAppAbroadState()); 
    if (appForm.getAppAbroadCountryId() != null)
      appFormData.setAppAbroadCountryId(appForm.getAppAbroadCountryId()); 
    if (appForm.getAppAbroadPincode() != null)
      appFormData.setAppAbroadPincode(appForm.getAppAbroadPincode()); 
    appFormData.setAppPanCardNo(appForm.getAppPanCardNo());
    
    appFormData.setAppPanCardLater(appForm.getAppPanCardLater());
    appFormData.setAppEverDefaultedCreditCard(appForm.getAppEverDefaultedCreditCard());
    appFormData.setCloneCoapplicantAddress1(appForm.isCloneCoapplicantAddress1());
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
    if (appForm.getAppCoapplicantCityId() != null)
      appFormData.setAppCoapplicantCityId(appForm.getAppCoapplicantCityId()); 
    if (appForm.getAppCoapplicantPincode() != null) {
      int pincodeInitial = appForm.getAppCoapplicantPincode().intValue() / 10000;
      String pinlastfix = appForm.getAppCoapplicantPincode().toString().substring(3, 6);
      MasterState state = this.commonService.getStateById((appForm.getAppCoapplicantStateId() != null) ? appForm.getAppCoapplicantStateId() : null);
      if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
        if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
          appFormData.setAppCoapplicantPincode(appForm.getAppCoapplicantPincode());
        } else {
          appFormData.setError("Entered co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode");
          return appFormData;
        } 
      } else {
        appFormData.setAppCoapplicantPincode(appForm.getAppCoapplicantPincode());
      } 
    } 
    appFormData.setAppCoapplicantPhone(appForm.getAppCoapplicantPhone());
    appFormData.setAppCoapplicantGender(appForm.getAppCoapplicantGender());
    appFormData.setAppCoapplicantEmploymentTypeId(appForm.getAppCoapplicantEmploymentTypeId());
    appFormData.setAppCoapplicantNmiAndNai(appForm.getAppCoapplicantNmiAndNai());
    appFormData.setAppCoapplicantPanCardNo(appForm.getAppCoapplicantPanCardNo());
    appFormData.setAppCoapplicantPanCardLater(appForm.isAppCoapplicantPanCardLater());
    appFormData.setAppCoapplicantDistrictId(appForm.getAppCoapplicantDistrictId());
    appFormData.setAppCoapplicantCollateral(appForm.getAppCoapplicantCollateral());
    appFormData.setAppCoapplicantMarketValueOfCollateral(appForm.getAppCoapplicantMarketValueOfCollateral());
    if (appForm.getAppCoapplicantRelationTypeId() != null)
      appFormData.setAppCoapplicantRelationTypeId(appForm.getAppCoapplicantRelationTypeId()); 
    appFormData.setAppOtherId(appForm.getAppOtherId());
    appFormData.setAppOtherIdNumber(appForm.getAppOtherIdNumber());
    if (appForm.getAppCoIdProof() != null)
      appFormData.setAppCoIdProof(appForm.getAppCoIdProof()); 
    if (appForm.getAppCoOtherIdNumber() != null)
      appFormData.setAppCoOtherIdNumber(appForm.getAppCoOtherIdNumber()); 
    if (appFormData.getAppSalesEnteredTime() != null)
      appFormData.setAppSalesEnteredTime(new Date()); 
    appFormData.setAppInterestedSbiLifeInsurance(appForm.getAppInterestedSbiLifeInsurance());
    appFormData.setAppPanCardVerified("N");
    appFormData.setAppCoappliantPanCardVerified("N");
    if (appFormData.getAppFilledAt() == null) {
      appFormData.setAppFilledAt(new Date());
      if (appFormData.getAppAlertStatusType() != null)
        appFormData.setAppAlertStatusType(null); 
    } 
    if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
      if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId().intValue() == 2 && appFormData.getAppLoanAmount().doubleValue() >= 20.0D)
        if (ValidatorUtil.isValid(appForm.getAppEdvantageBranchId())) {
          Integer[] circleIdNetworkModuleRegionId = this.commonService.getCircleIdNetworkModuleRegionByBranchId(appForm.getAppEdvantageBranchId());
          if (ValidatorUtil.isValid(circleIdNetworkModuleRegionId[0]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[1]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[2]) && 
            ValidatorUtil.isValid(circleIdNetworkModuleRegionId[3])) {
            appFormData.setAppCircleId(circleIdNetworkModuleRegionId[0]);
            appFormData.setAppNetworkId(circleIdNetworkModuleRegionId[1]);
            appFormData.setAppModuleId(circleIdNetworkModuleRegionId[2]);
            appFormData.setAppRegionId(circleIdNetworkModuleRegionId[3]);
          } else {
            appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
            return appFormData;
          } 
        } else {
          appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
          return appFormData;
        }  
    } else if (!ValidatorUtil.isValid(appFormData.getAppBranchId())) {
      appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
      return appFormData;
    } 
    if (appForm.getAppHaveAadhaarNumber() != null)
      appFormData.setAppHaveAadhaarNumber(appForm.getAppHaveAadhaarNumber()); 
    if (appForm.getAppCoAppHaveAadhaarNumber() != null)
      appFormData.setAppCoAppHaveAadhaarNumber(appForm.getAppCoAppHaveAadhaarNumber()); 
    appFormData = this.educationLoanService.save(appFormData);
    logger.info("ELProcessImpl.java :: LNo :: 2496 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    this.educationLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_DATA_SAVED, null, false);
    int nsdlPANStatus = 0;
    int coAppPanStatus = 0;
    if (!ValidatorUtil.isValid(appForm.getAppPanCardNo()) && !ValidatorUtil.isValid(appForm.getAppCoapplicantPanCardNo())) {
      appFormData.setAppRSMdecision(Integer.valueOf(3));
    } else {
      if (ValidatorUtil.isValid(appForm.getAppPanCardNo())) {
        PanApiInputParams apiInputParams = new PanApiInputParams();
        apiInputParams.setApiApplicantTypeId(Integer.valueOf(1));
        apiInputParams.setApiAppSeqId(appFormData.getAppSeqId());
        if (isDsrPage.equalsIgnoreCase("true")) {
          apiInputParams.setApiCallingSourceId(Integer.valueOf(2));
        } else {
          apiInputParams.setApiCallingSourceId(Integer.valueOf(1));
        } 
        apiInputParams.setApiMethodName("getPanDetails");
        apiInputParams.setApiProductId(Constants.EDUCATION_LOAN_ID);
        logger.info("ELProcessImpl.java :: LNo :: 1744 appSeqId===" + appFormData.getAppSeqId() + " calling pan service callPanServiceApi()");
        PanApiReturnResponse panApiResponseObj = this.panServiceAction.callPanServiceApi(apiInputParams);
        logger.info("ELProcessImpl.java :: LNo :: 1748 appSeqId===" + appFormData.getAppSeqId() + "===response pan service status id===" + panApiResponseObj.getApiStatusId() + "===Api Error===" + panApiResponseObj.getApiErrors());
        if (panApiResponseObj.getApiStatusId().intValue() == 1)
          nsdlPANStatus = 1; 
        if (nsdlPANStatus == 1) {
          appFormData.setAppPanCardVerified("Y");
          appFormData.setAppPanVerifiedDatetime(new Date());
          appFormData.setAppRSMdecision(Integer.valueOf(1));
        } else {
          appFormData.setAppPanCardVerified("N");
          appFormData.setAppRSMdecision(Integer.valueOf(3));
        } 
      } 
      if (ValidatorUtil.isValid(appForm.getAppCoapplicantPanCardNo())) {
        PanApiInputParams apiInputParams = new PanApiInputParams();
        apiInputParams.setApiApplicantTypeId(Integer.valueOf(7));
        apiInputParams.setApiAppSeqId(appFormData.getAppSeqId());
        if (isDsrPage.equalsIgnoreCase("true")) {
          apiInputParams.setApiCallingSourceId(Integer.valueOf(2));
        } else {
          apiInputParams.setApiCallingSourceId(Integer.valueOf(1));
        } 
        apiInputParams.setApiMethodName("getPanDetails");
        apiInputParams.setApiProductId(Constants.EDUCATION_LOAN_ID);
        logger.info("ELProcessImpl.java :: LNo :: 1784 appSeqId===" + appFormData.getAppSeqId() + " calling pan service callPanServiceApi()");
        PanApiReturnResponse panApiResponseObj = this.panServiceAction.callPanServiceApi(apiInputParams);
        logger.info("ELProcessImpl.java :: LNo :: 1788 appSeqId===" + appFormData.getAppSeqId() + " response pan service " + panApiResponseObj.getApiStatusId() + " Api Error===" + panApiResponseObj.getApiErrors());
        if (panApiResponseObj.getApiStatusId().intValue() == 1)
          coAppPanStatus = 1; 
        if (coAppPanStatus == 1) {
          appFormData.setAppCoappliantPanCardVerified("Y");
          appFormData.setAppRSMdecision(Integer.valueOf(1));
        } else {
          appFormData.setAppCoappliantPanCardVerified("N");
          appFormData.setAppRSMdecision(Integer.valueOf(3));
        } 
      } 
    } 
    logger.info("ELProcessImpl.java :: LNo :: 2664 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    this.educationLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (nsdlPANStatus == 1) ? Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS : Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL, null, false);
    this.educationLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), appFormData.getAppLoanStatusId().intValue(), (coAppPanStatus == 1) ? Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_SUCCESS : Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_COAPPLICANT_FAIL, null, false);
    boolean isBankLmsContactCenterId = this.commonService.getBankLmsUserRole(bankLMSUserId);
    logger.info(" EducationProcessManagerImpl.java Lno :: 2883 :: isBankLmsContactCenterId : " + isBankLmsContactCenterId + " bankLMSUserId : " + bankLMSUserId + " with AppSeqId ::" + appSeqId);
    if (isBankLmsContactCenterId) {
      if (appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue() || 
        appForm.getAppOtherId() == null || appForm.getAppOtherId().intValue() != 0 || 
        appForm.isAppCoapplicantPanCardLater() == null || !appForm.isAppCoapplicantPanCardLater().booleanValue() || 
        appForm.getAppCoIdProof() == null || !"0".equalsIgnoreCase(appForm.getAppCoIdProof())) {
        if ((appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue()) && (appForm.getAppPanCardNo() == null || "".equals(appForm.getAppPanCardNo())) && 
          appForm.getAppHaveAadhaarNumber() != null && appForm.getAppHaveAadhaarNumber().intValue() == 2 && 
          appForm.getAppOtherId() != null && appForm.getAppOtherId().intValue() == 0) {
          appFormData.setError("One of the Identity proof of applicant is mandatory.|1");
          return appFormData;
        } 
        if (appForm.getAppPanCardNo() != null && !"".equals(appForm.getAppPanCardNo()) && 
          !ValidatorUtil.validateIdProof(appForm.getAppPanCardNo(), Integer.valueOf(181))) {
          appFormData.setError("PAN is not in correct format.|2");
          return appFormData;
        } 
        if (ValidatorUtil.isValid(appForm.getAppOtherId())) {
          boolean validateStatus = ValidatorUtil.validateIdProof(appForm.getAppOtherIdNumber(), appForm.getAppOtherId());
          if (!validateStatus) {
            if (appForm.getAppOtherId().intValue() == 166) {
              appFormData.setError("Passport Number Not in Correct Format.|4");
            } else if (appForm.getAppOtherId().intValue() == 165) {
              appFormData.setError("Voter Id Not in Correct Format.|4");
            } else {
              appFormData.setError("Other Id proof is not in correct format.|4");
            } 
            return appFormData;
          } 
        } 
        if ((appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue()) && (appForm.getAppPanCardNo() == null || "".equals(appForm.getAppPanCardNo()))) {
          appFormData.setError("Either enter PAN or select PAN card later check box of applicant.|2");
          return appFormData;
        } 
        if ((appForm.isAppCoapplicantPanCardLater() == null || !appForm.isAppCoapplicantPanCardLater().booleanValue()) && (appForm.getAppCoapplicantPanCardNo() == null || "".equals(appForm.getAppCoapplicantPanCardNo())) && 
          appForm.getAppCoAppHaveAadhaarNumber() != null && appForm.getAppCoAppHaveAadhaarNumber().intValue() == 2 && 
          appForm.getAppCoOtherIdNumber() != null && "0".equalsIgnoreCase(appForm.getAppCoIdProof())) {
          appFormData.setError("One of the Identity proof of co-applicant is mandatory.|11");
          return appFormData;
        } 
        if (appForm.getAppCoapplicantPanCardNo() != null && !"".equals(appForm.getAppCoapplicantPanCardNo()) && 
          !ValidatorUtil.validateIdProof(appForm.getAppCoapplicantPanCardNo(), Integer.valueOf(181))) {
          appFormData.setError("Co-Applicant PAN is not in correct format.|8");
          return appFormData;
        } 
        if (ValidatorUtil.isValid(appForm.getAppCoIdProof())) {
          boolean validateStatus = ValidatorUtil.validateIdProof(appForm.getAppCoOtherIdNumber(), Integer.valueOf(Integer.parseInt(appForm.getAppCoIdProof())));
          if (!validateStatus) {
            if ("166".equalsIgnoreCase(appForm.getAppCoIdProof())) {
              appFormData.setError("Co-Applicant Passport Number Not in Correct Format.|10");
            } else if ("165".equalsIgnoreCase(appForm.getAppCoIdProof())) {
              appFormData.setError("Co-Applicant Voter Id Not in Correct Format.|10");
            } else {
              appFormData.setError("Co-Applicant Other Id proof is not in correct format.|10");
            } 
            return appFormData;
          } 
        } 
        if ((appForm.isAppCoapplicantPanCardLater() == null || !appForm.isAppCoapplicantPanCardLater().booleanValue()) && (appForm.getAppCoapplicantPanCardNo() == null || "".equals(appForm.getAppCoapplicantPanCardNo()))) {
          appFormData.setError("Either enter PAN or select PAN card later check box of co-applicant.|8");
          return appFormData;
        } 
      } 
    } else {
      if ((appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue()) && (appForm.getAppPanCardNo() == null || "".equals(appForm.getAppPanCardNo())) && 
        appForm.getAppHaveAadhaarNumber() != null && appForm.getAppHaveAadhaarNumber().intValue() == 2 && 
        appForm.getAppOtherId() != null && appForm.getAppOtherId().intValue() == 0) {
        appFormData.setError("One of the Identity proof of applicant is mandatory.|1");
        return appFormData;
      } 
      if (appForm.getAppPanCardLater() != null && appForm.getAppPanCardLater().booleanValue() && appForm.getAppHaveAadhaarNumber() != null && appForm.getAppHaveAadhaarNumber().intValue() == 2 && appForm.getAppOtherId().intValue() == 0) {
        appFormData.setError("Please select other identity proof of applicant.|5");
        return appFormData;
      } 
      if (appForm.getAppPanCardLater() != null && appForm.getAppPanCardLater().booleanValue() && appForm.getAppHaveAadhaarNumber() != null && appForm.getAppHaveAadhaarNumber().intValue() == 1 && appForm.getAppOtherId().intValue() == 0) {
        appFormData.setError("Either enter PAN or select Other Identity Proof.|1");
        return appFormData;
      } 
      if (appForm.getAppPanCardNo() != null && !"".equals(appForm.getAppPanCardNo()) && 
        !ValidatorUtil.validateIdProof(appForm.getAppPanCardNo(), Integer.valueOf(181))) {
        appFormData.setError("Applicant PAN is not in correct format.|2");
        return appFormData;
      } 
		 if (isDsrPage.equalsIgnoreCase("false") && appForm.getAppAlternateMobileNumber() != null && !"".equals(appForm.getAppAlternateMobileNumber())
    				&& appForm.getAppAltMobileVerified() == null ||  (appForm.getAppAltMobileVerified() != null && appForm.getAppAltMobileVerified().equalsIgnoreCase("N"))) {
    			appFormData.setError("Please verify alternate Mobile Number.|");
    			return appFormData;
    		}
   	
      if (ValidatorUtil.isValid(appForm.getAppOtherId())) {
        boolean validateStatus = ValidatorUtil.validateIdProof(appForm.getAppOtherIdNumber(), appForm.getAppOtherId());
        if (!validateStatus) {
          if (appForm.getAppOtherId().intValue() == 166) {
            appFormData.setError("Applicant Passport Number Not in Correct Format.|4");
          } else if (appForm.getAppOtherId().intValue() == 165) {
            appFormData.setError("Applicant Voter Id Not in Correct Format.|4");
          } else {
            appFormData.setError("Applicant Other Id proof is not in correct format.|4");
          } 
          return appFormData;
        } 
      } 
      if ((appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue()) && (appForm.getAppPanCardNo() == null || "".equals(appForm.getAppPanCardNo()))) {
        appFormData.setError("Either enter PAN or select PAN card later check box of applicant.|2");
        return appFormData;
      } 
      if ((appForm.isAppCoapplicantPanCardLater() == null || !appForm.isAppCoapplicantPanCardLater().booleanValue()) && (appForm.getAppCoapplicantPanCardNo() == null || "".equals(appForm.getAppCoapplicantPanCardNo())) && 
        appForm.getAppCoAppHaveAadhaarNumber() != null && appForm.getAppCoAppHaveAadhaarNumber().intValue() == 2 && 
        appForm.getAppCoIdProof() != null && "0".equalsIgnoreCase(appForm.getAppCoIdProof())) {
        appFormData.setError("One of the Identity proof of co-applicant is mandatory.|6");
        return appFormData;
      } 
      if (appForm.isAppCoapplicantPanCardLater() != null && appForm.isAppCoapplicantPanCardLater().booleanValue() && appForm.getAppCoAppHaveAadhaarNumber() != null && appForm.getAppCoAppHaveAadhaarNumber().intValue() == 2 && "0".equalsIgnoreCase(appForm.getAppCoIdProof())) {
        appFormData.setError("Please select one identity proof of co-applicant.|7");
        return appFormData;
      } 
      if (appForm.getAppCoapplicantPanCardNo() != null && !"".equals(appForm.getAppCoapplicantPanCardNo()) && 
        !ValidatorUtil.validateIdProof(appForm.getAppCoapplicantPanCardNo(), Integer.valueOf(181))) {
        appFormData.setError("Co-Applicant PAN is not in correct format.|8");
        return appFormData;
      } 
      if (ValidatorUtil.isValid(appForm.getAppCoIdProof())) {
        boolean validateStatus = ValidatorUtil.validateIdProof(appForm.getAppCoOtherIdNumber(), Integer.valueOf(Integer.parseInt(appForm.getAppCoIdProof())));
        if (!validateStatus) {
          if ("166".equalsIgnoreCase(appForm.getAppCoIdProof())) {
            appFormData.setError("Co-Applicant Passport Number Not in Correct Format.|10");
          } else if ("165".equalsIgnoreCase(appForm.getAppCoIdProof())) {
            appFormData.setError("Co-Applicant Voter Id Not in Correct Format.|10");
          } else {
            appFormData.setError("Co-Applicant Other Id proof is not in correct format.|10");
          } 
          return appFormData;
        } 
      } 
      if ((appForm.isAppCoapplicantPanCardLater() == null || !appForm.isAppCoapplicantPanCardLater().booleanValue()) && (appForm.getAppCoapplicantPanCardNo() == null || "".equals(appForm.getAppCoapplicantPanCardNo()))) {
        appFormData.setError("Either enter PAN or select PAN card later check box of co-applicant.|8");
        return appFormData;
      } 
    } 
    logger.info("disconnect cibil api ");
    logger.info("ELProcessImpl.java :: LNo :: 2704 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    if (SessionUtil.getApplicationType() != null || appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
      StatusRequest statusRequest = new StatusRequest();
      statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
      statusRequest.setHaveLoanOffer(true);
      statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
        this.educationLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
      if (statusManagerResponse.isInitiateCallAttempt())
        appFormData.setAppTotalCallAttempt(Integer.valueOf(0)); 
      logger.info("ELProcessImpl.java :: LNo :: 2766 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
      if (SessionUtil.getApplicationType() != null)
        if (SessionUtil.getApplicationType().intValue() != 0)
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
            logger.info("ELProcessImpl.java :: LNo :: 2785 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
          } else if (SessionUtil.getApplicationType().intValue() == 2) {
            logger.info("ELProcessImpl.java :: LNo :: 2787 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
          }   
    } 
    logger.info("ELProcessImpl.java :: LNo :: 2791 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    try {
      boolean isAbleToSendBMOrSalesTeamMessage = false;
      if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()))
        if (Constants.REFERENCE_NUMBER_BASED_ON_CLUSTER) {
          appFormData = this.refGenerateUtil.generateReferenceNumber(appFormData);
          if (appFormData != null && appFormData.getError() != null)
            return appFormData; 
        } else {
          this.lastReferenceNumber = this.educationLoanService.getLastGeneratedReferenceNumber(Constants.EDUCATION_LOAN_ID);
          this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_EL, Constants.SOURCE_STRING_EL, this.lastReferenceNumber);
       
          boolean isReferenceIdAvailable = false;
          isReferenceIdAvailable = this.educationLoanService.isReferenceIdAvailable(this.appRefKey);
          logger.info("EL isReferenceIdAvailable 2 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          if (!isReferenceIdAvailable) {
            appFormData.setAppReferenceId(this.appRefKey);
          } else {
            this.lastReferenceNumber = this.appRefKey;
            this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_EL, Constants.SOURCE_STRING_EL, this.lastReferenceNumber);
            isReferenceIdAvailable = this.educationLoanService.isReferenceIdAvailable(this.appRefKey);
            logger.info("EL isReferenceIdAvailable 3 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
            if (!isReferenceIdAvailable) {
              appFormData.setAppReferenceId(this.appRefKey);
            } else {
              this.lastReferenceNumber = this.appRefKey;
              this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_EL, Constants.SOURCE_STRING_EL, this.lastReferenceNumber);
              isReferenceIdAvailable = this.educationLoanService.isReferenceIdAvailable(this.appRefKey);
              logger.info("EL isReferenceIdAvailable 1 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
              appFormData.setAppReferenceId(this.appRefKey);
            } 
          } 
          logger.info("EL isReferenceIdAvailable 4 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
          appFormData = this.educationLoanService.save(appFormData);
        }  
      logger.info("ELProcessImpl.java :: LNo :: 1992 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
      if (ValidatorUtil.isValid(appFormData.getAppReferenceId())) {
        if (appFormData.getAppRSMdecision() != null && appFormData.getAppRSMdecision().intValue() != 2) {
            boolean sendMailStatus = false;
            if (isDsrPage.equalsIgnoreCase("true")) {
              sendMailStatus = true;
            } else if (appFormData.getAppAipMailSendStatus() == null) {
              sendMailStatus = true;
            } 
            if (sendMailStatus) {
              if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail()) && ValidatorUtil.isValid(appFormData.getAppReferenceId()))
                this.taskExecutorService.sendingEmailForEducationLoan(requestIndex, Integer.valueOf(1), appFormData); 
              if (ValidatorUtil.isValid(appFormData.getAppReferenceId()))
                this.taskExecutorService.sendingSMSForEducationLoan(requestIndex, Integer.valueOf(1), appFormData, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage); 
            } 
            this.taskExecutorService.generatePDFForEducationLoan(appFormData, quote);
        } else {
          logger.info("EdcuationProcessManagerImpl LNo :: 2841 reference number is not able to generate  with AppSeqId ::" + appSeqId);
          if (appFormData != null) {
            appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
            return appFormData;
          } 
        } 
          if (appFormData != null && quote != null && 
            !Constants.CRM_BYPASS)
            if (appFormData.getAppCRMLeadId() != null && isDsrPage.equalsIgnoreCase("true")) {
              CRMRequest crmRequest = new CRMRequest();
              crmRequest.setCrmLeadId(appFormData.getAppCRMLeadId());
              crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
              crmRequest.setReferenceNumber(appFormData.getAppSeqId());
              crmRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID);
            } else {
              callCrm(appFormData, quote);
            }  
      } 
    } catch (NullPointerException e) {
        logger.info("EducationLoanProcessImpl.java LNo: 2866 :: exception caught while generating ref number ", e);
        if (appFormData == null)
          appFormData = new ApplicationFormEducationLoan(); 
        appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
        return appFormData;
      }
    logger.info("ELProcessImpl.java :: LNo :: 2887 appFormData.getAppLoanStatusId() :: " + appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
    return appFormData;
  }

public CBSCallResponse processCbsCall(Integer appSeqId, Integer requestIndex, MasterCBSCall masterCbsCall, String isDsrPage, Integer bankLMSUserId, String receivedAction) throws SQLException {
    CBSCallResponse cbsCallResponse = new CBSCallResponse();
    masterCbsCall.setCbsLoanTypeId(Constants.EDUCATION_LOAN_ID);
    int count = this.commonService.getCBSApplicationCount(masterCbsCall.getCbsIsdCode(), masterCbsCall.getCbsMobileNumber(), Constants.EDUCATION_LOAN_ID).intValue();
    logger.info("EducationProcessManagerImpl.java LNo : 2922 count : " + count + " with AppSeqId ::" + appSeqId);
    if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
      cbsCallResponse.setResponseMsg(Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
      cbsCallResponse.setStatus(Integer.valueOf(0));
      return cbsCallResponse;
    } 
    cbsCallResponse.setStatus(Integer.valueOf(0));
    try {
      masterCbsCall.setCbsVisitId(SessionUtil.getVisitIdEL());
      if (SessionUtil.getEducationLoanCbsCallId() != null) {
        MasterCBSCall oldMasterCbsCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getEducationLoanCbsCallId());
        oldMasterCbsCall.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
        oldMasterCbsCall.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
        oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCode());
        oldMasterCbsCall.setCbsTypeOfRelationship(masterCbsCall.getCbsTypeOfRelationship());
        masterCbsCall = this.commonService.save(oldMasterCbsCall);
        if (masterCbsCall == null) {
          logger.info("EducationProcessManagerImpl LNO :: 2940 with AppSeqId ::" + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } else {
        masterCbsCall.setCbsOtpVerified(Integer.valueOf(0));
        masterCbsCall.setCbsRequiestedTime(new Date());
        masterCbsCall = this.commonService.save(masterCbsCall);
        if (masterCbsCall == null) {
          logger.info("EducationProcessManagerImpl LNO :: 2950 with AppSeqId ::" + appSeqId);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        SessionUtil.setEducationLoanCbsCallId(masterCbsCall.getCbsId());
      } 
      logger.info("EducationProcessManagerImpl LNO :: 2958=================" + SessionUtil.getEducationLoanCbsCallId() + " with AppSeqId ::" + appSeqId);
      MasterCBSResponse masterCBSResponse = null;
      if (masterCbsCall.getCbsResponseId() == null) {
        masterCBSResponse = new MasterCBSResponse();
      } else {
        masterCBSResponse = this.commonService.getMasterCBSResponseById(masterCbsCall.getCbsResponseId());
        if (masterCBSResponse == null) {
          logger.info("EducationProcessManagerImpl LNO :: 2965");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } 
      logger.info("Before Caaling callingCBSEngineForCIFLevelInformation");
      JSONObject cbsEngineResponseJson = this.cbsUtil.callingCBSEngineForCIFLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.EDUCATION_LOAN_ID.intValue());
      CBSCustomerInformation cbsCustomerInformation = new CBSCustomerInformation();
      this.cbsUtil.setCBSCustomerInformationBean(cbsCustomerInformation, cbsEngineResponseJson);
      if (cbsCustomerInformation.getStatus() != null && cbsCustomerInformation.getStatus().equals("0")) {
        logger.info("EducationProcessManagerImpl LNO :: 2978 with AppSeqId ::" + appSeqId);
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
        logger.info("EducationProcessManagerImpl LNO :: 2990 with AppSeqId ::" + appSeqId);
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (!masterCbsCall.getCbsMobileNumber().equals(cbsCustomerInformation.getMOBILENO())) {
        cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      JSONObject accountInfoResponseJson = this.cbsUtil.callingCBSEngineForAccountLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.EDUCATION_LOAN_ID.intValue());
      if (accountInfoResponseJson.has("ERROR_CODE") && accountInfoResponseJson.get("ERROR_CODE") != null && accountInfoResponseJson.get("ERROR_CODE").toString().trim().length() > 0) {
        logger.info("Education Loan  LNO :: 2423");
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
      String prodId = "";
      if (accountInfoResponseJson.has("AccType"))
        prodId = accountInfoResponseJson.getString("AccType"); 
      if (accountInfoResponseJson.has("Interest_Category"))
        prodId = String.valueOf(prodId) + accountInfoResponseJson.getString("Interest_Category"); 
      cbsCustomerInformation.setProductId(prodId);
//      logger.info("Response from Customer Enquiry and Account info Data Account no : " + cbsCustomerInformation.getACCOUNTNUMBER() + " Product Id : " + cbsCustomerInformation.getProductId());
      cbsCustomerInformation.setAccountDesc(accountInfoResponseJson.getString("AccountDescription"));
      boolean needToByPassLoanAccountInformation = true;
      if (masterCbsCall.getCbsTypeOfRelationship() != null) {
        Integer typeOfRelationship = masterCbsCall.getCbsTypeOfRelationship();
        if (typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3"))
          needToByPassLoanAccountInformation = false; 
      }
      
      if (ValidatorUtil.isValid(cbsCustomerInformation.getProductId())) {
        if (needToByPassLoanAccountInformation) {
          boolean productFound = this.commonService.isCbsMappingsExistByProductId(Constants.EDUCATION_LOAN_ID, cbsCustomerInformation.getProductId());
          if (!productFound) {
            logger.info("EducationProcessManagerImpl.java LNo: 3009 : Product id not match with mapping table with AppSeqId ::" + appSeqId);
            cbsCallResponse.setResponseMsg("Provided account No. does not pertain to selected type of relationship");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } 
      } else {
        logger.info("EducationProcessManagerImpl.java LNo: 3017 : Product id not received  in Customer information service with AppSeqId ::" + appSeqId);
        cbsCallResponse.setResponseMsg("Product id not received  in Customer information service");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      try {
        masterCbsCall.setCbsResponseTime(new Date());
        masterCBSResponse.setCbsLoanTypeId(Constants.EDUCATION_LOAN_ID);
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
          logger.info("EducationLoanProcessImpl.java LNo: 3124 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } catch (NullPointerException e) {
          logger.info("EducationLoanProcessImpl.java LNo: 3130 ::", e);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
       } 
      catch (ParseException e) {
          logger.info("EducationLoanProcessImpl.java LNo: 3130 ::", e);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
       }  
      CBSLoanAccountInformation cbsLoanAccountInformation = new CBSLoanAccountInformation();
      if (needToByPassLoanAccountInformation) {
        JSONObject loanAccountInformationcbsResponseJson = this.cbsUtil.callingCBSEngineForLoanAccountInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.CBS_LOAN_TYPE_EDUCATION_TOP_UP);
        this.cbsUtil.setCbsLoanAccountInformation(cbsLoanAccountInformation, loanAccountInformationcbsResponseJson);
        if (cbsLoanAccountInformation.getStatus() != null && cbsLoanAccountInformation.getStatus().equals("0")) {
          logger.info("Education Loan  :: 2571 with AppSeqId " + appSeqId);
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
      
	      JSONObject educationloanAccInforcbsResJson = this.cbsUtil.callingCBSEngineForEducationLoanAccInfo(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.CBS_LOAN_TYPE_EDUCATION_TOP_UP);
	      if (educationloanAccInforcbsResJson.has("ERROR_CODE") && educationloanAccInforcbsResJson.get("ERROR_CODE") != null && educationloanAccInforcbsResJson.get("ERROR_CODE").toString().trim().length() > 0) {
	        logger.info("HomeProcessManagerImpl LNO :: 2423");
	        if (educationloanAccInforcbsResJson.get("ERROR_DESCRIPTION") != null) {
	          if (educationloanAccInforcbsResJson.get("ERROR_DESCRIPTION").toString().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
	            cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
	          } else {
	            cbsCallResponse.setResponseMsg(educationloanAccInforcbsResJson.get("ERROR_DESCRIPTION").toString().trim());
	          } 
	        } else {
	          logger.info("ERROR_DESCRIPTION : " + accountInfoResponseJson.get("ERROR_DESCRIPTION").toString());
	          cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
	        } 
	        cbsCallResponse.setStatus(Integer.valueOf(0));
	        return cbsCallResponse;
	      } 
	      cbsLoanAccountInformation.setInstituteName(educationloanAccInforcbsResJson.getString("INSTITUTION_NAME"));
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
        logger.info("EducationLoanProcessImpl.java LNo: 3172 ::");
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
          logger.info("EducationProcessManagerImpl LNO :: 3218");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } catch (ParseException ex) {
        logger.info("EducationProcessManagerImpl LNO :: 3224 with AppSeqId ::" + appSeqId);
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (needToByPassLoanAccountInformation && 
        Integer.parseInt(masterCBSResponse.getCbsIracStatus()) >= Constants.CBS_IRAC_STATUS.intValue()) {
        logger.info("EducationProcessManagerImpl LNO :: 3236  CBS_IRAC_STATUS is greater than or equal to 4 with AppSeqId ::" + appSeqId);
        cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      boolean needToInsert = true;
      ApplicationFormEducationLoan app = null;
      ApplicationFormEducationLoanQuote quote = null;
      if (masterCBSResponse.getCbsAppSeqId() == null) {
        app = new ApplicationFormEducationLoan();
        quote = new ApplicationFormEducationLoanQuote();
      } else {
        app = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
        if (app == null) {
          logger.info("EducationProcessManagerImpl LNO :: 3252");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        needToInsert = false;
        if (app.getAppQuoteId() != null) {
          quote = this.educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(app.getAppQuoteId());
          if (quote == null) {
            logger.info("EducationProcessManagerImpl LNO :: 3261");
            cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
            cbsCallResponse.setStatus(Integer.valueOf(0));
            return cbsCallResponse;
          } 
        } else {
          quote = new ApplicationFormEducationLoanQuote();
        } 
      } 
      try {
        quote.setLoanQuoteActive("Y");
        quote.setLoanQuoteDeleted("N");
        quote.setLoanQuoteCreatedLmsUserId(Constants.OTHER_USER_ID);
        quote.setLoanQuoteEntryTime(new Date());
        quote.setLoanQuoteUpdatedTime(new Date());
        quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
        quote.setLoanQuoteVisitId(SessionUtil.getVisitIdEL());
        ValidatorUtil.isValid(masterCBSResponse.getCbsOutstanding());
        if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
          quote.setLoanQuoteDateOfBirth(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)); 
        if (ValidatorUtil.isValid(masterCbsCall.getCbsVisitId()))
          quote.setLoanQuoteVisitId(masterCbsCall.getCbsVisitId()); 
        if (ValidatorUtil.isValid(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
          quote.setLoanQuoteDateOfBirthDT(masterCBSResponse.getCbsDateOfBirth()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
          quote.setLoanQuoteGender(masterCBSResponse.getCbsGender()); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
          quote.setLoanQuotePreEmi(masterCBSResponse.getCbsEmi()); 
        
        //save consentId
        Consent consent = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "ETB");
		Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
		quote.setLoanQuoteConsentId(consentId); 
		
        quote = this.educationLoanService.save(quote);
        if (quote == null) {
          logger.info("EducationLoanProcessImpl.java LNo: 3306 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
      } catch (SQLException e) {
          logger.info("EducationLoanProcessImpl.java LNo: 3313 :: exception caught ", e);
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
        app.setAppContactCenterLocation(1);
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
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_DIRECT);
        } else if ("true".equalsIgnoreCase(masterCbsCall.getIsMobileRequest())) {
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP);
        } else {
          app.setAppDataSourceId(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
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
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsPhoneBusiness()) && 
          StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()))
          app.setAppOfficePhoneNumber(Long.valueOf(Long.parseLong(masterCBSResponse.getCbsPhoneBusiness()))); 
        if (ValidatorUtil.isValid(masterCBSResponse.getCbsPanNumber()))
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
        if (isDsrPage.equalsIgnoreCase("true"))
          if (bankLMSUserId != null) {
            if (this.commonService.isBankUser(bankLMSUserId))
              app.setAppAssignedLmsSalesUserId(bankLMSUserId); 
          } else if (SessionUtil.getBankLMSUser() != null && SessionUtil.getBankLMSUser().getLmsUserId() != null) {
            bankLMSUserId = SessionUtil.getBankLMSUser().getLmsUserId();
            if (this.commonService.isBankUser(bankLMSUserId))
              app.setAppAssignedLmsSalesUserId(bankLMSUserId); 
          }
        
	    //save consent id in main table
	    if (quote.getLoanQuoteConsentId() != null && app.getAppConsentId() == null) {
	    	app.setAppConsentId(quote.getLoanQuoteConsentId());
	    }
	    
        app = this.educationLoanService.save(app);
        if (app == null) {
          logger.info("EducationLoanProcessImpl.java LNo: 3437 ::");
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
        } 
        SessionUtil.setEducationLoanApplicationSequenceId(app.getAppSeqId());
      } catch (NullPointerException e) {
          logger.info("EducationLoanProcessImpl.java LNo: 3444 :: exception caught ", e);
          cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
          cbsCallResponse.setStatus(Integer.valueOf(0));
          return cbsCallResponse;
       }
      masterCBSResponse.setCbsAppSeqId(app.getAppSeqId());
      masterCBSResponse = this.commonService.save(masterCBSResponse);
      if (masterCBSResponse == null) {
        logger.info("EducationLoanProcessImpl.java LNo: 3453 ::");
        cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        return cbsCallResponse;
      } 
      if (needToInsert)
        try {
          this.educationLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), app.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE167, null, true);
        } catch (NullPointerException e) {
            logger.info("EducationLoanProcessImpl.java LNo: 3463 :: exception caught ", e);
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
      if (masterCbsCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA) && 
        ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
        msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
        if (msgBody != null) {
          msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET + Constants.BANK_IMAGE_FOLDER + "/");
          msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
          msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
          msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
          msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
          msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
          boolean sendStatus = false;
          String[] emailId = { masterCBSResponse.getCbsEmail() };
          sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
          if (!sendStatus) {
            logger.info("PersonalProcessManagerImpl LNO :: 3187 with AppSeqId ::" + appSeqId);
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
      return cbsCallResponse;
    } catch (NullPointerException e) {
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        logger.info("EducationLoanProcessImpl.java LNo: 3614 :: exception caught ", e);
        return cbsCallResponse;
     } catch (JSONException e) {
      cbsCallResponse.setResponseMsg("System error occured.");
      cbsCallResponse.setStatus(Integer.valueOf(0));
      logger.info("EducationLoanProcessImpl.java LNo: 3614 :: exception caught ", e);
      return cbsCallResponse;
    } catch (RuntimeException e) {
        cbsCallResponse.setResponseMsg("System error occured.");
        cbsCallResponse.setStatus(Integer.valueOf(0));
        logger.info("EducationLoanProcessImpl.java LNo: 3614 :: exception caught ", e);
        return cbsCallResponse;
      } 
  }
  
  public JSONObject processCBSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, OtherRequest otherRequest) throws JSONException {
    
	  logger.info("inside the processCBSOTP method otherRequest.getInputOtp()"+otherRequest.getInputOtp());
	  if(otherRequest.getInputOtp() !=null) {
      	SbiUtil sbiutil=new SbiUtil();
      	logger.info("DecryptedRequest inputOtp   1014  "+otherRequest.getInputOtp());
      	String inputOtp=sbiutil.getDecryptedRequest(otherRequest.getInputOtp());
		//	logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
			otherRequest.setInputOtp(inputOtp);
			//  logger.info("3014 otherRequest.getInputOtp()"+otherRequest.getInputOtp());
      }
     // logger.info("DecryptedRequest otherRequest.getInputOtp()   1023  "+otherRequest.getInputOtp());
	  JSONObject json = new JSONObject();
	  
    int appOTPVerifyType = 0;
    if (otherRequest != null && otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getEducationLoanCbsCallId() == null) {
      logger.info("EducationLoanProcessImpl.java LNo: 3636 ::");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getEducationLoanCbsCallId());
    if (masterCBSCall == null) {
      logger.info("EducationLoanProcessImpl.java LNo: 3646 ::");
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
        Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
        ApplicationFormEducationLoan app = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("EducationProcessManagerImpl.java LNO 4566:: after save error");
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
        if (alertCount.intValue() >= 5) {
          logger.info("EducationLoanProcessImpl.java LNo: 3695 :: with AppSeqId ::" + appSeqId);
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
          logger.info("EducationLoanProcessImpl.java LNo: 3718 :: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
        msgBody = this.SbiUtil.urlEncode(msgBody);
        String SMS_TEXT = null;
        if (masterCBSCall.getCbsIsdCode() == null || Constants.COUNTRY_CODE_INDIA.equals(String.valueOf(masterCBSCall.getCbsIsdCode()))) {
        	SMS_TEXT=Constants.SMS_STRING_INDIAN;
        } else {
        	SMS_TEXT=Constants.SMS_STRING_NRI;
        }
        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
        SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode());
        SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim());
        
        if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
      	  logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode());
        }
        
        if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (masterCBSCall.getCbsIsdCode() != null && masterCBSCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA)) {
          msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0))) + Constants.THIRD_EMAIL_PART;
          msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
          msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
          msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
          msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
          msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCBSCall.getCbsEmailOtpCode());
          msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.EDUCATION_LOAN_PRODUCT_NAME);
          boolean sendStatus = false;
          String[] emailId = { masterCBSCall.getCbsEmail() };
          sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
          if (!sendStatus) {
            logger.info("EducationProcessManagerImpl LNO :: 3874 with AppSeqId ::" + appSeqId);
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
          logger.info("EducationLoanProcessImpl.java LNo: 3778 ::");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (JSONException e) {
          logger.info("EducationProcessManager.java LNO 3802::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
      } catch (SQLException e) {
        logger.info("EducationProcessManager.java LNO 3802::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    if (stateId.intValue() == 29)
      try {
        if (!ValidatorUtil.isValid(otherRequest.getInputOtp())) {
          json.put("status", "error");
          json.put("message", "Invalid OTP code");
          return json;
        } 
        Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("EducationProcessManager.java LNO 3812::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
          return json;
        } 
        ApplicationFormEducationLoan app = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("EducationProcessManager.java LNO 3821::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
//        logger.info("EducationProcessManagerImpl.java LNo : 3826 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId ::" + appSeqId);
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        app = this.educationLoanService.save(app);
        if (app == null) {
          logger.info("EducationProcessManager.java LNO 38338 ::  error on saving::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        if (app.getAppDataSourceId() != null && !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP))
          if (ValidatorUtil.isValidEmail(otherRequest.getUserEmail())) {
            if (!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString()) && masterCBSCall.getCbsEmail() == null) {
              app.setAppWorkEmail(otherRequest.getUserEmail());
            } else if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())) {
              app.setAppWorkEmail(otherRequest.getUserEmail());
             // logger.info(" AutoProcessManager.java LNO 3949 ::  Capture Email From User is ::" + otherRequest.getUserEmail() + " with AppSeqId ::" + appSeqId);
            } 
            if (!app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP))
              SessionUtil.setEmail(otherRequest.getUserEmail()); 
          } else {
            logger.info("EducationProcessManager.java LNO 3853 with AppSeqId ::" + appSeqId);
            json.put("status", "error");
            json.put("message", "Please Enter valid email");
            return json;
          }  
        boolean isOPTVerified = false;
        if (appOTPVerifyType == 0) {
          if (masterCBSCall.getCbsOtpCode() != null)
            app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode()))); 
          if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(otherRequest.getInputOtp())) {
            masterCBSCall.setCbsMobileOtpVerified("Y");
            app.setAppMobileVerified("Y");
            app.setAppMobileVerificationCodeReceived("Y");
            int otpResType = getOTPResidantVerifiedType(app);
            app.setAppResTypeAtVerified(Integer.valueOf(otpResType));
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
//            logger.info("OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId ::" + appSeqId);
          } else {
            logger.info("EducationProcessManager.java LNO 3994 :: with AppSeqId ::" + appSeqId);
            app.setAppMobileVerified("N");
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else if (appOTPVerifyType == 1) {
          if (masterCBSCall.getCbsEmailOtpCode() != null)
            app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode()); 
          if (masterCBSCall.getCbsEmailOtpCode() != null && masterCBSCall.getCbsEmailOtpCode().equals(otherRequest.getInputOtp())) {
            masterCBSCall.setCbsEmailOtpVarified("Y");
            app.setAppEmailVerified("Y");
            app.setAppMobileVerificationCodeReceived("Y");
            isOPTVerified = true;
            json.put("status", "success");
            json.put("message", "OTP authentication successful");
//            logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail());
          } else {
            logger.info("EducationProcessManager.java LNO 4023:: with AppSeqId ::" + appSeqId);
            app.setAppEmailVerified("N");
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("EducationProcessManager.java LNO 3916:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          app.setAppEmailVerified("N");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
            logger.info("EducationProcessManager.java LNO 39339:: with AppSeqId ::" + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.educationLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.educationLoanService.save(app);
          if (app == null) {
            logger.info("EducationProcessManager.java LNO 3949::");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
        } else {
          logger.info("OTP is authentication failed:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("EducationProcessManager.java LNO 3962::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException e) {
          logger.info("EducationProcessManager.java LNO 3969::", e);
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
    if (SessionUtil.getEducationLoanCbsCallId() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 4517");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getEducationLoanCbsCallId());
    if (masterCBSCall == null) {
      logger.info("EducationProcessManagerImpl LNO :: 4527");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsOtpCount() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 4535");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsIsdCode() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 4546");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (masterCBSCall.getCbsMobileNumber() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 4555");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
    if (stateId.intValue() == 33)
      try {
        Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
        ApplicationFormEducationLoan app = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("EducationProcessManagerImpl.java LNO 4566:: after save error");
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
          logger.info("EducationProcessManagerImpl LNO :: 4577 with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          masterCBSCall.setCbsOtpCode(this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
//          logger.info("PersonalProcessManagerImpl LNO :: 5301 ::  mobile number ::" + masterCBSCall.getCbsMobileNumber() + " with AppSeqId ::" + appSeqId);
//          logger.info("PersonalProcessManagerImpl LNO :: 5302 :: mobile OTP ::" + masterCBSCall.getCbsOtpCode() + " with AppSeqId ::" + appSeqId);
        } else {
          logger.info("PersonalProcessManagerImpl LNO :: 5304 with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        boolean sendSMSConsent = false;
        if (isDsrPage.equalsIgnoreCase("true")) {
          boolean bankLmsUserRoleExceptContactCenter = this.commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
          if (bankLmsUserRoleExceptContactCenter) {
            logger.info("EducationProcessManagerImpl LNO :: 4599 :: Not contact Center user with AppSeqId ::" + appSeqId);
            sendSMSConsent = true;
          } else {
            logger.info("EducationProcessManagerImpl LNO :: 3803 :: contact Center user with AppSeqId ::" + appSeqId);
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
        	logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode());
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
          logger.info("EducationProcessManagerImpl LNO :: 4644 Sorry for the inconvenience with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        if (app != null) {
          app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode())));
          app.setAppMobileVerified("N");
        } 
        app = this.educationLoanService.save(app);
        if (app == null) {
          logger.info("EducationProcessManagerImpl LNO :: 4656 Sorry for the inconvenience with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (NullPointerException e) {
          logger.info("EducationProcessManagerImpl.java LNO 4690::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
      } catch (SQLException e) {
        logger.info("EducationProcessManagerImpl.java LNO 4690::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    if (stateId.intValue() == 34)
      try {
        Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("EducationProcessManagerImpl LNO :: 4700 Sorry for the inconvenience with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        ApplicationFormEducationLoan app = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
        if (app == null) {
          logger.info("EducationProcessManagerImpl LNO :: 4704 Sorry for the inconvenience with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        logger.info("EducationProcessManagerImpl.java LNo : 4709 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId ::" + appSeqId);
        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
          return json;
        } 
        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
        app = this.educationLoanService.save(app);
        if (app == null) {
          logger.info("EducationProcessManagerImpl java LNO 4721:: after save error Sorry for the inconvenience with AppSeqId ::" + appSeqId);
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
//            logger.info("EducationProcessManagerImpl.java LNO 4729  :: OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId ::" + appSeqId);
          } else {
            logger.info("EducationProcessManagerImpl.java LNO 4731:: with AppSeqId ::" + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("EducationProcessManagerImpl.java LNO 4738:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
            logger.info("EducationProcessManager.java LNO 4761 ::");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          if (statusManagerResponse.isEligibleToInsertLog())
            this.educationLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
          app = this.educationLoanService.save(app);
          if (app == null) {
            logger.info("EducationProcessManagerImpl.java LNO 4772 ::");
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
        } else {
          logger.info("EducationProcessManagerImpl.java LNO 4778 :: OTP is authentication failed:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        masterCBSCall = this.commonService.save(masterCBSCall);
        if (masterCBSCall == null) {
          logger.info("EducationProcessManagerImpl.java LNO 4786 ::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (SQLException e) {
          logger.info("EducationProcessManagerImpl.java LNO 4793 ::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       }
    return json;
  }
  
  private void callCrm(ApplicationFormEducationLoan appFormData, ApplicationFormEducationLoanQuote quote) {
    try {
      logger.info("ELProcessImpl.java :: LNo :: 4832 :: Before preparing CRM Request Object with AppSeqId ::" + appFormData.getAppSeqId());
      CRMRequest crmRequest = prepareCrmRequest(appFormData, quote);
      logger.info("ELProcessImpl.java :: LNo :: 4834 :: Before preparing CRM Request Object with AppSeqId ::" + appFormData.getAppSeqId());
     crmServiceNew.crmLeadCreation(crmRequest, appFormData);
    } catch (NullPointerException e) {
        logger.info("EducationProcessManagerImpl.java  LNO 4837 :: Exception occured", e);
    } catch (SQLException e) {
      logger.info("EducationProcessManagerImpl.java  LNO 4837 :: Exception occured", e);
    } 
  }
  
  private CRMRequest prepareCrmRequest(ApplicationFormEducationLoan appFormData, ApplicationFormEducationLoanQuote quote) throws NoResultException, SQLException  {
	    CRMRequest crmRequest = new CRMRequest();
	    if (appFormData == null) {
	      logger.info("ELProcessImpl.java :: LNo :: 4845 :: appFormData is null, returning back");
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
	    if (ValidatorUtil.isValidPanNo(appFormData.getAppPanCardNo())) {
	      crmRequest.setPanNumber(appFormData.getAppPanCardNo());
	    } else {
	      crmRequest.setPanNumber("");
	    } 
	    
	    if (appFormData.getAppMobileNo() != null) {
	    	crmRequest.setMobileNumber(appFormData.getAppMobileNo());
	    	//logger.info("ELProcessImpl.java :: LNo :: 12221 :: Mobile number in app form data:::: ::" + appFormData.getAppMobileNo());
	    }
	    if(quote.getAppMobile() != null) {
	    	logger.info("ELProcessImpl.java :: LNo :: 56789 :: Mobile number Indian:::: ::" + quote.getAppMobile());
	    }
	    if(quote.getAppNRIMobileNo() != null) {
	    	logger.info("ELProcessImpl.java :: LNo :: 345678 :: Mobile number NRI :::: ::" + quote.getAppNRIMobileNo());
	    }
	      
	    if (ValidatorUtil.isValid(appFormData.getAppOfficePhoneStdCode()) && appFormData.getAppOfficePhoneStdCode().length() == 4 && appFormData.getAppOfficePhoneNumber() != null && appFormData.getAppOfficePhoneNumber().longValue() == 7L) {
	      crmRequest.setPhoneNumber(String.valueOf(appFormData.getAppOfficePhoneStdCode()) + appFormData.getAppOfficePhoneNumber());
	    } else {
	      crmRequest.setPhoneNumber("");
	    } 
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
	    if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
	      crmRequest.setResidentType("NRI");
	    } else {
	      crmRequest.setResidentType("indian");
	    } 
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
	   // logger.info("ELProcessImpl.java getAppLoanAmount:::"+appFormData.getAppLoanAmount());
	   // logger.info("ELProcessImpl.java getLeadAmount:::"+crmRequest.getLeadAmount());
	    
	    MasterElProduct product = this.commonService.getEducationLoanProductById(appFormData.getAppEducationLoanId());
	    if (product != null && product.getElProductCrmCode() != null) {
	    	crmRequest.setProductCode(product.getElProductCrmCode());
	    }
	    
	    if (product != null && product.getElProductCategory() != null) {
	    	crmRequest.setProductCategory(product.getElProductCategory());
	    }
	    crmRequest.setCIFNumber("");
	    if(appFormData.getAppExistingRelationTypeId() != null) {
	    	MasterCBSResponse masterCBSResponse = this.educationLoanService.getMasterCBSResponseObjectByLoanTypeAppSeqId(appFormData.getAppSeqId());
	        if (masterCBSResponse != null) {
	          if (masterCBSResponse.getCbsCifNumber() != null && masterCBSResponse.getCbsCifNumber().length() > 11) {
	            crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber().substring(6));
	          } else if (masterCBSResponse.getCbsCifNumber() != null && masterCBSResponse.getCbsCifNumber().length() == 11) {
	            crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber());
	          }
	        }  	
	    }
	    
	    crmRequest.setDuplicate(true);
	    crmRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID);
	    
	    MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getEducationLoanCbsCallId());
    if (masterCBSCall != null && masterCBSCall.getCbsTypeOfRelationship() != null) {
     // logger.info("ELProcessImpl.java :: LNo :: 27102021 :: cbs type of relationship:::: ::" + masterCBSCall.getCbsTypeOfRelationship());
      crmRequest.setExistingRelationTypeId(masterCBSCall.getCbsTypeOfRelationship());
	}
    if (appFormData.getAppCoapplicantEmploymentTypeId() != null) {
     // logger.info("ELProcessImpl.java :: LNo :: 4447777 :: type of employment ID:::: ::" + this.commonService.getEmploymentTypeById(appFormData.getAppCoapplicantEmploymentTypeId())
      //    .getEmploymentTypeCrmId());
      crmRequest.setEmploymentTypeId((appFormData.getAppCoapplicantEmploymentTypeId() != null) ? this.commonService.getEmploymentTypeById(appFormData.getAppCoapplicantEmploymentTypeId())
          .getEmploymentTypeCrmId() : "");
    } 
	    
	    if(quote.getLoanQuoteWorkExperience() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4448888 :: work experience :::: ::" + quote.getLoanQuoteWorkExperience());
	    	crmRequest.setWorkExperienceId(quote.getLoanQuoteWorkExperience());
	    }
	    
	    if(quote.getLoanQuoteLastEducationalQualificationId() != null) { 
	    	//logger.info("ELProcessImpl.java :: LNo :: 4449999 :: highest qualification :::: ::" + quote.getLoanQuoteLastEducationalQualificationId());
	        crmRequest.setQualification(quote.getLoanQuoteLastEducationalQualificationId());
	    }
	    if (loanTenure!=0.0) {
	    	crmRequest.setLoanTenure(loanTenure);
	      //  logger.info("ELProcessImpl.java NOT NULL loanTenure:::"+crmRequest.getLeadAmount()+"..loanTenure.."+loanTenure);
	    } else {
	    	crmRequest.setLoanTenure(0);
	       // logger.info("ELProcessImpl.java NULL loanTenure:::"+crmRequest.getLeadAmount()+"..loanTenure.."+loanTenure);
	    }
	    
	    if(quote.getLoanQuoteCountryOfStudyId() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441001 :: Country of study type ID :::::" + quote.getLoanQuoteCountryOfStudyId());
	    	crmRequest.setCountryOfStudyType(quote.getLoanQuoteCountryOfStudyId());
	    }
	    
	    if(quote.getLoanQuoteCourseTypeId() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441002 :: course type id :::::" + quote.getLoanQuoteCourseTypeId());
	    	crmRequest.setCourseTypeId(quote.getLoanQuoteCourseTypeId());
	    }
	    if(quote.getLoanQuoteCourseName() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441002 :: course name :::::" + quote.getLoanQuoteCourseName());
	    	crmRequest.setCourseName(quote.getLoanQuoteCourseName());
	    }
	    
	    if(quote.getLoanQuoteInstituteName() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441002 :: InstituteName :::::" + quote.getLoanQuoteInstituteName());
	    	crmRequest.setInstitueName(quote.getLoanQuoteInstituteName());
	    }
	    
	    if(quote.getLoanQuoteUniversityName() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441002 :: UniversityName:::::" + quote.getLoanQuoteUniversityName());
	    	crmRequest.setUniversityName(quote.getLoanQuoteUniversityName());
	    }
	    
	    if(quote.getLoanQuoteNatureOfCourseId() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441003 :: NatureOfCourseId :::::" + quote.getLoanQuoteNatureOfCourseId());
	    	crmRequest.setNatureOfCourse(quote.getLoanQuoteNatureOfCourseId());
	    }
	    
	    if(quote.getLoanQuoteTuitionFee() != null ) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441005 :: QuoteTuitionFee :::::" + quote.getLoanQuoteTuitionFee());
	    	crmRequest.setTuitionFee(BigDecimal.valueOf(quote.getLoanQuoteTuitionFee().doubleValue()));
	    }
	    
	    if(appFormData.getAppInterestedSbiLifeInsurance() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441006 :: sbi life insurance :::::" + appFormData.getAppInterestedSbiLifeInsurance());
	    	crmRequest.setIsAvailSbiLifeInsurance(appFormData.getAppInterestedSbiLifeInsurance());
	    }
	    
	    if(quote.getLoanQuoteCourseDurationYears() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441007 :: course duration:::::" + quote.getLoanQuoteCourseDurationYears());
	    	crmRequest.setCourseDuration(String.valueOf(quote.getLoanQuoteCourseDurationYears())+" Years");
	    	if(quote.getLoanQuoteCourseDurationMonth() != null) {
	    		crmRequest.setCourseDuration(String.valueOf(quote.getLoanQuoteCourseDurationYears()) + " Years " + quote.getLoanQuoteCourseDurationMonth() + " Months");
	    	}
	    }
	    
	    if(quote.getLoanQuotePreferredLocation() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441008 :: preferred location :::::" + quote.getLoanQuotePreferredLocation());
	    	crmRequest.setPreferredLocationTypeId(quote.getLoanQuotePreferredLocation());
	    }
	    
	    if(appFormData.getAppPincode() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441009 :: pincode :::::" + appFormData.getAppPincode());
	    	crmRequest.setResPincode(appFormData.getAppPincode());
	    }
	    
	    if(appFormData.getAppDistrictId() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441010 :: appFormData.getAppDistrictId :::::" + appFormData.getAppDistrictId());
	    	crmRequest.setDistrict(String.valueOf(appFormData.getAppDistrictId()));
	    }
	    
	    if(quote.getResidentDistrictId() != null) {
	    	//logger.info("ELProcessImpl.java :: LNo :: 4441011 :: quote.getResidentDistrictId :::::" + quote.getResidentDistrictId());
	    	crmRequest.setDistrict(String.valueOf(quote.getResidentDistrictId()));
	    }
	    
    if (quote.getLoanQuoteCountryOfStudy() != null) {
      //logger.info("ELProcessImpl.java :: LNo :: 4441021 :: Country of study :::::" + quote.getLoanQuoteCountryOfStudy());
      crmRequest.setCountryOfStudy(this.commonService.getCountryById(quote.getLoanQuoteCountryOfStudy()).getCountryName());
      crmRequest.setNatureOfCourse(Integer.valueOf(1));
    } 
		
		if(appFormData.getAppLoanInterestRate() != null) {
	      //  logger.info("ELProcessImpl.java :: LNo :: 4441016 :: processing fee :::::" + appFormData.getAppLoanInterestRate());
	        crmRequest.setLoanInterestRate(appFormData.getAppLoanInterestRate());
	    }
    String alternateNo = "";
    if (appFormData.getAppAlternateMobileNumber() != null) {
      //logger.info("Alternate mobile number isd code " + appFormData.getAppAltISDCode());
      //logger.info("Alternate mobile number " + appFormData.getAppAlternateMobileNumber());
      alternateNo = "+" + appFormData.getAppAltISDCode() + "-" + appFormData.getAppAlternateMobileNumber();
    } 
	    crmRequest.setAlternateMobileNumber(alternateNo);
    if(appFormData.getAppLoanAmount() != null) { 	
        crmRequest.setLoanMaxAmount(SbiUtil.getFinalLoanAmount(appFormData.getAppLoanAmount()));
        //logger.info("ELProcessImpl.java :: LNo :: 3335 ::  loan amount :::::" + appFormData.getAppLoanAmount()+"..setCRMLoanMaxAmount.."+crmRequest.getLoanMaxAmount());
    }
    
    if(appFormData.getAppLoanEmi() != null) {
    	crmRequest.setLoanEmi(BigDecimal.valueOf(appFormData.getAppLoanEmi().doubleValue()));
    	//logger.info("ELProcessImpl.java :: NOT NULL app loan emi :::::" + appFormData.getAppLoanEmi());
    }
    if (chosenAmt!=0.0) {
    	crmRequest.setLeadAmount(SbiUtil.getFinalLoanAmount(chosenAmt));
       // logger.info("ELProcessImpl.java NOT NULL getLeadAmount:::"+crmRequest.getLeadAmount()+"..chosenAmt.."+chosenAmt);
    } else {
    	crmRequest.setLeadAmount(0);
       // logger.info("ELProcessImpl.java NULL getLeadAmount:::"+crmRequest.getLeadAmount()+"..chosenAmt.."+chosenAmt);
    }
    
    if (appFormData.getAppLoanProcessingFee() != null) {
    	crmRequest.setLoanProcessingFee(BigDecimal.valueOf(appFormData.getAppLoanProcessingFee()));
    	//logger.info("ELProcessImpl.java ::  NOT NULL :: processing fee :::::" + appFormData.getAppLoanProcessingFee());
    } else {
    	crmRequest.setLoanProcessingFee(BigDecimal.valueOf(appFormData.getAppLoanProcessingFee()));
    	//logger.info("ELProcessImpl.java ::  NULL :: processing fee :::::" + appFormData.getAppLoanProcessingFee());
    }
    if(appFormData.getAppLoanAmount() != null) { 	
        crmRequest.setLoanMaxAmount(SbiUtil.getFinalLoanAmount(appFormData.getAppLoanAmount()));
       // logger.info("ELProcessImpl.java :: LNo :: 3335 ::  loan amount :::::" + appFormData.getAppLoanAmount()+"..setCRMLoanMaxAmount.."+crmRequest.getLoanMaxAmount());
    }
	    Integer leadSourceKey = commonService.getLeadSourceKey(appFormData);
		crmRequest.setLeadSourceKey(leadSourceKey);
		
		Optional<MarTech> marTechDeatails = Optional.ofNullable(
				marTechDao.getDetailsByVisitId(educationLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));
		logger.info("ELProcessImpl.java :: LNo :: 4113 ::marTechDetails is:::" + marTechDeatails);
		if (marTechDeatails.isPresent()) {
			MarTech martech = marTechDeatails.get();
			crmRequest.setCampaignCode(martech.getCampaignCode());
			crmRequest.setOfferCode(martech.getOfferCode());
			crmRequest.setTrackingCode(martech.getTrackingCode());
			logger.info("ELProcessImpl.java :: LNo :: 4119 ::campaignCode is:::"
					+ crmRequest.getCampaignCode());
			logger.info("ELProcessImpl.java :: LNo :: 4121 ::offerCode is:::" + crmRequest.getOfferCode());
			logger.info("ELProcessImpl.java :: LNo :: 4122 ::trackingCode is:::"
					+ crmRequest.getTrackingCode());
		}
		logger.info(
				"ELProcessImpl.java :: LNo :: 4471 ::trackingCode is:::" + crmRequest.getTrackingCode());
	    return crmRequest;
	  }
  
  
  private static int getOTPResidantVerifiedType(ApplicationFormEducationLoan appForm) {
    int flag = 0;
    if (appForm.getAppISDCode() != null && "91".equals(appForm.getAppISDCode())) {
      flag = 1;
    } else {
      flag = 2;
    } 
    return flag;
  }
  
  public JSONObject verifyConcentOtp(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException  {
    JSONObject json = new JSONObject();
    int appOTPVerifyType = 0;
    if (otherRequest.getAppOTPVerifyType() != null)
      appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
    if (SessionUtil.getEducationLoanApplicationSequenceId() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 3803");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
      json.put("alertCount", 0);
      return json;
    } 
    ApplicationFormEducationLoan appForm = this.educationLoanService.getApplicationFormEducationLoanByAppSeqId(SessionUtil.getEducationLoanApplicationSequenceId());
    
    if (appForm == null) {
      logger.info("EducationProcessManagerImpl LNO :: 3981");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppOTPAttemptCount() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 3821");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    Integer alertCount = Integer.valueOf(0);
    alertCount = appForm.getAppOTPAttemptCount();
    if (appForm.getAppISDCode() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 3832");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (appForm.getAppMobileNo() == null) {
      logger.info("EducationProcessManagerImpl LNO :: 3841");
      json.put("status", "error");
      json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
      json.put("alertCount", 0);
      return json;
    } 
    if (stateId.intValue() == 41)
      try {
        if (alertCount.intValue() >= 5) {
          logger.info("EducationProcessManagerImpl LNO :: 3854 with AppSeqId ::" + appForm.getAppSeqId());
          json.put("status", "error");
          json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
          json.put("alertCount", alertCount);
          return json;
        } 
        if (appOTPVerifyType == 0) {
          appForm.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(appForm.getAppMobileNo()));
          logger.info("mobile number ::" + appForm.getAppMobileNo() + " with AppSeqId ::" + appForm.getAppSeqId());
          logger.info("mobile OTP ::" + appForm.getAppMobileVerificationCode() + " with AppSeqId ::" + appForm.getAppSeqId());
        } else {
          logger.info("EducationProcessManagerImpl LNO :: 3864 with AppSeqId ::" + appForm.getAppSeqId());
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          json.put("alertCount", alertCount);
          return json;
        } 
        boolean sendSMSConsent = false;
        if (isDsrPage.equalsIgnoreCase("true")) {
          boolean bankLmsUserRoleExceptContactCenter = this.commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
          if (bankLmsUserRoleExceptContactCenter) {
            logger.info("Not contact Center user with AppSeqId ::" + appForm.getAppSeqId());
            sendSMSConsent = true;
          } else {
            logger.info("contact Center user with AppSeqId ::" + appForm.getAppSeqId());
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
        appForm = this.educationLoanService.save(appForm);
        if (appForm == null) {
          logger.info("EducationProcessManagerImpl LNO :: 4523 Sorry for the inconvenience");
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
        } 
        json.put("status", "success");
        json.put("message", "OTP code sent");
        return json;
      } catch (JSONException e) {
          logger.info("EducationProcessManagerImpl.java LNO 3923::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
       } catch (SQLException e) {
        logger.info("EducationProcessManagerImpl.java LNO 3923::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    if (stateId.intValue() == 42)
      try {
        Integer appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
        if (appSeqId == null) {
          logger.info("EducationProcessManagerImpl.java LNO 3933::");
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
//            logger.info("OTP verfied for mobileNo ::" + (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim() + " with AppSeqId ::" + appSeqId);
          } else {
            logger.info("EducationProcessManagerImpl.java LNO 3964:: with AppSeqId ::" + appSeqId);
            json.put("status", "error");
            json.put("message", "OTP authentication failed");
            return json;
          } 
        } else {
          logger.info("EducationProcessManagerImpl.java LNO 3971:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP authentication failed");
          return json;
        } 
        if (isOPTVerified) {
          StatusRequest statusRequest = new StatusRequest();
          statusRequest.setCurrentStatus(appForm.getAppLoanStatusId().intValue());
          statusRequest.setHaveLoanOffer(true);
          statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID.intValue());
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
            logger.info("EducationProcessManagerImpl.java LNO 3993:: with AppSeqId ::" + appSeqId);
            json.put("status", "error");
            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
            return json;
          } 
          this.educationLoanHelper.insertCallLog(appForm.getAppSeqId(), bankLMSUserId.intValue(), Constants.CALL_LOGS_MESSAGE_STATE171_ID, null, null, true);
        } else {
          logger.info("OTP is authentication failed:: with AppSeqId ::" + appSeqId);
          json.put("status", "error");
          json.put("message", "OTP is incorrect! Try again.");
          return json;
        } 
        appForm = this.educationLoanService.save(appForm);
        if (appForm == null) {
          logger.info("EducationProcessManagerImpl.java LNO 4017::");
          json.put("status", "error");
          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
          return json;
        } 
        return json;
      } catch (NullPointerException e) {
          logger.info("EducationProcessManagerImpl.java LNO 4042::", e);
          json.put("status", "error");
          json.put("message", "Sorry for the inconvenience");
          return json;
      } catch (SQLException e) {
        logger.info("EducationProcessManagerImpl.java LNO 4042::", e);
        json.put("status", "error");
        json.put("message", "Sorry for the inconvenience");
        return json;
      }  
    return json;
  }
  
	
}
