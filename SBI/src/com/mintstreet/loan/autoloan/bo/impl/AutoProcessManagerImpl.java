package com.mintstreet.loan.autoloan.bo.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskRejectedException;

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
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.engine.CommonEngine;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.BureauLinkRequestResponse;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCarDealer;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterDistrict;
import com.mintstreet.common.entity.MasterDocumentType;
import com.mintstreet.common.entity.MasterEmployeeOccupationType;
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
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.FileHelper;
import com.mintstreet.common.util.JSONUtil;
import com.mintstreet.common.util.MapperUtil;
import com.mintstreet.common.util.RefGenerateUtilAL;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.integration.edms.action.EdmsFinalServiceAction;
import com.mintstreet.integration.edms.bo.FstoreDoc;
import com.mintstreet.integration.pan.action.PanServiceAction;
import com.mintstreet.integration.pan.bo.PanApiInputParams;
import com.mintstreet.integration.pan.bo.PanApiReturnResponse;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.autoloan.util.AutoLoanHelper;
import com.mintstreet.loan.product.entity.AlProduct;

import freemarker.template.utility.StringUtil;

public class AutoProcessManagerImpl {
	 private static final Logger logger = LogManager.getLogger(AutoProcessManagerImpl.class.getName());

	@Autowired
	private CommonService commonService;

	@Autowired
	private AutoLoanService autoLoanService;

	@Autowired
	private AutoLoanHelper autoLoanHelper;

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
	private CallBackService callBackService;

	public volatile String appRefKey;

	public volatile String lastReferenceNumber;

	@Autowired
	private RefGenerateUtilAL refGenerateUtil;

	@Autowired
	private PanServiceAction panServiceAction;
	
	@Autowired
	private MarTechDao marTechDao;

	@Autowired
	private EdmsFinalServiceAction edmsServiceAction;	

	public JSONObject processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId,
			String ajaxPostUrl, String imageName) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			ApplicationFormAutoLoan appFormData = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
			if (appFormData == null) {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				return json;
			}
			if (ValidatorUtil.isValid(imageNo)) {
				String fullPath = "";
				if (imageNo.intValue() == 1) {
					if (appFormData.getAppPhotoIdName() != null) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppPhotoIdName();
						FileHelper.deleteFile(fullPath);
						appFormData.setAppPhotoId(null);
						appFormData.setAppPhotoIdName(null);
					} else if (ValidatorUtil.isValid(imageName)) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + imageName;
						FileHelper.deleteFile(fullPath);
						appFormData.setAppPhotoId(null);
						appFormData.setAppPhotoIdName(null);
					}
				} else if (imageNo.intValue() == 2) {
					if (appFormData.getAppIdentityProofName() != null) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppIdentityProofName();
						FileHelper.deleteFile(fullPath);
						appFormData.setAppIdentityProofId(null);
						appFormData.setAppIdentityProofName(null);
					} else if (ValidatorUtil.isValid(imageName)) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + imageName;
						FileHelper.deleteFile(fullPath);
						appFormData.setAppIdentityProofId(null);
						appFormData.setAppIdentityProofName(null);
					}
				} else if (imageNo.intValue() == 3) {
					if (appFormData.getAppResidenceProofName() != null) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppResidenceProofName();
						FileHelper.deleteFile(fullPath);
						appFormData.setAppResidenceProofId(null);
						appFormData.setAppResidenceProofName(null);
					} else if (ValidatorUtil.isValid(imageName)) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + imageName;
						FileHelper.deleteFile(fullPath);
						appFormData.setAppResidenceProofId(null);
						appFormData.setAppResidenceProofName(null);
					}
				} else if (imageNo.intValue() == 4) {
					if (appFormData.getAppIncomeProofName() != null) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppIncomeProofName();
						FileHelper.deleteFile(fullPath);
						appFormData.setAppIncomeProofId(null);
						appFormData.setAppIncomeProofName(null);
					} else if (ValidatorUtil.isValid(imageName)) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + imageName;
						FileHelper.deleteFile(fullPath);
						appFormData.setAppIncomeProofId(null);
						appFormData.setAppIncomeProofName(null);
					}
				} else if (imageNo.intValue() == 5) {
					if (appFormData.getAppAutoDetailsProofName() != null) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + appFormData.getAppAutoDetailsProofName();
						FileHelper.deleteFile(fullPath);
						appFormData.setAppAutoDetailsProofId(null);
						appFormData.setAppAutoDetailsProofName(null);
					} else if (ValidatorUtil.isValid(imageName)) {
						fullPath = String.valueOf(Constants.UPLOAD_PATH_LIVE) + Constants.UPLOAD_INITIAL
								+ Constants.AL_PDF_GENRATION_LOCATION + imageName;
						FileHelper.deleteFile(fullPath);
						appFormData.setAppAutoDetailsProofId(null);
						appFormData.setAppAutoDetailsProofName(null);
					}
					appFormData.setAppEmploymentProofId(null);
					appFormData.setAppEmploymentProofName(null);
				}
				appFormData = this.autoLoanService.save(appFormData);
				json.put("status", "success");
				json.put("message", "Document deleted.");
			} else {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			}
		} catch (NullPointerException e) {
			logger.info(
					"AutoLoanProcessImpl.java LNo: 276 :: processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId,String ajaxPostUrl, String imageName) ",
					e);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
		} catch (SQLException e) {
			logger.info(
					"AutoLoanProcessImpl.java LNo: 276 :: processDeleteProductImage(Integer appSeqId, Integer imageNo, Integer bankLMSUserId,String ajaxPostUrl, String imageName) ",
					e);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
		}
		return json;
	}

	public JSONObject processToDocumentPickupUploaded(Integer appSeqId, ApplicationFormAutoLoan appForm,
			Integer bankLMSUserId, String ajaxPostUrl) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			if (appForm == null) {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				return json;
			}
			ApplicationFormAutoLoan appFormData = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
			if (appFormData == null) {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				return json;
			}
			if ((ValidatorUtil.isValid(appForm.getAppPhotoId()) && !ValidatorUtil.isValid(appForm.getAppPhotoIdName()))
					|| (!ValidatorUtil.isValid(appForm.getAppPhotoId())
							&& ValidatorUtil.isValid(appForm.getAppPhotoIdName()))) {
				json.put("status", "error");
				json.put("message", "Please select Photo Id and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppPhotoId()))
				appFormData.setAppPhotoId(appForm.getAppPhotoId());
			if (ValidatorUtil.isValid(appForm.getAppPhotoIdName()))
				appFormData.setAppPhotoIdName(appForm.getAppPhotoIdName());
			if ((ValidatorUtil.isValid(appForm.getAppIdentityProofId())
					&& !ValidatorUtil.isValid(appForm.getAppIdentityProofName()))
					|| (!ValidatorUtil.isValid(appForm.getAppIdentityProofId())
							&& ValidatorUtil.isValid(appForm.getAppIdentityProofName()))) {
				json.put("status", "error");
				json.put("message", "Please select Identitiy proof type and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppIdentityProofId()))
				appFormData.setAppIdentityProofId(appForm.getAppIdentityProofId());
			if (ValidatorUtil.isValid(appForm.getAppIdentityProofName()))
				appFormData.setAppIdentityProofName(appForm.getAppIdentityProofName());
			if ((ValidatorUtil.isValid(appForm.getAppResidenceProofId())
					&& !ValidatorUtil.isValid(appForm.getAppResidenceProofName()))
					|| (!ValidatorUtil.isValid(appForm.getAppResidenceProofId())
							&& ValidatorUtil.isValid(appForm.getAppResidenceProofName()))) {
				json.put("status", "error");
				json.put("message", "Please select Residence proof type and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppResidenceProofId()))
				appFormData.setAppResidenceProofId(appForm.getAppResidenceProofId());
			if (ValidatorUtil.isValid(appForm.getAppResidenceProofName()))
				appFormData.setAppResidenceProofName(appForm.getAppResidenceProofName());
			if ((ValidatorUtil.isValid(appForm.getAppIncomeProofId())
					&& !ValidatorUtil.isValid(appForm.getAppIncomeProofName()))
					|| (!ValidatorUtil.isValid(appForm.getAppIncomeProofId())
							&& ValidatorUtil.isValid(appForm.getAppIncomeProofName()))) {
				json.put("status", "error");
				json.put("message", "Please select Income proof type and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppIncomeProofId()))
				appFormData.setAppIncomeProofId(appForm.getAppIncomeProofId());
			if (ValidatorUtil.isValid(appForm.getAppIncomeProofName()))
				appFormData.setAppIncomeProofName(appForm.getAppIncomeProofName());
			if ((ValidatorUtil.isValid(appForm.getAppEmploymentProofId())
					&& !ValidatorUtil.isValid(appForm.getAppEmploymentProofName()))
					|| (!ValidatorUtil.isValid(appForm.getAppEmploymentProofId())
							&& ValidatorUtil.isValid(appForm.getAppEmploymentProofName()))) {
				json.put("status", "error");
				json.put("message", "Please select Employment proof type and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppEmploymentProofId()))
				appFormData.setAppEmploymentProofId(appForm.getAppEmploymentProofId());
			if (ValidatorUtil.isValid(appForm.getAppEmploymentProofName()))
				appFormData.setAppEmploymentProofName(appForm.getAppEmploymentProofName());
			if ((ValidatorUtil.isValid(appForm.getAppAutoDetailsProofId())
					&& !ValidatorUtil.isValid(appForm.getAppAutoDetailsProofName()))
					|| (!ValidatorUtil.isValid(appForm.getAppAutoDetailsProofId())
							&& ValidatorUtil.isValid(appForm.getAppAutoDetailsProofName()))) {
				json.put("status", "error");
				json.put("message", "Please select Auto details proof type and upload corresponding document.|4");
				return json;
			}
			if (ValidatorUtil.isValid(appForm.getAppAutoDetailsProofId()))
				appFormData.setAppAutoDetailsProofId(appForm.getAppAutoDetailsProofId());
			if (ValidatorUtil.isValid(appForm.getAppAutoDetailsProofName()))
				appFormData.setAppAutoDetailsProofName(appForm.getAppAutoDetailsProofName());
			if (appForm.getAppDocPickupCheck() == null)
				appForm.setAppDocPickupCheck(Integer.valueOf(1));
			appFormData.setAppDocPickupCheck(appForm.getAppDocPickupCheck());
			if (appFormData.getAppDocPickupCheck().intValue() != 4 && appForm.getAppDocPickupDateDT() != null
					&& ValidatorUtil.isValid(appForm.getAppDocPickupTimeString())) {
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
				if (appFormData.getAppPickupCityId() != null
						&& appFormData.getAppPickupCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue()
						&& appForm.getAppPickupDistrictId() == null) {
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
						json.put("message",
								"Entered pincode does not belong to entered state|appForm.appPickupPincode|");
					}
					return json;
				}
				StatusRequest statusRequest = new StatusRequest();
				statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
				statusRequest.setHaveLoanOffer(false);
				statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
				statusRequest.setState(18);
				statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
				statusRequest.setRsmDecision(0);
				statusRequest.setApplicationType(
						(SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
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
					Date callDocPickupTime = DateUtil.changeDateFormatToDate(
							String.valueOf(appForm.getAppDocPickupDate()) + " " + appForm.getAppDocPickupTimeString(),
							"MM/dd/yyyy HH:mm");
					this.autoLoanHelper.insertCallLog(appSeqId, bankLMSUserId.intValue(),
							statusManagerResponse.getStatusCallLogs(), null, callDocPickupTime, true);
				}
				if (appFormData.getAppDocsEnteredTime() == null)
					appFormData.setAppDocsEnteredTime(new Date());
			}
			appFormData = this.autoLoanService.save(appFormData);

		      FstoreDoc doc = new FstoreDoc();
		      Integer branchCode = this.commonService.getBranchCodeByBranchId(appFormData.getAppBranchId());
		      doc.setAppPhotoIdName(appFormData.getAppPhotoIdName());
		      doc.setAppIdentityProofName(appFormData.getAppIdentityProofName());
		      doc.setAppResidenceProofName(appFormData.getAppResidenceProofName());
		      doc.setAppIncomeProofName(appFormData.getAppIncomeProofName());
		      doc.setAppAutoDetailsProofName(appFormData.getAppAutoDetailsProofName());
		      
		      edmsServiceAction.uploadDocumentsToEDMS(doc, Constants.AUTO_LOAN_ID, appFormData.getAppReferenceId(), branchCode);
			
			json.put("status", "success");
			json.put("message", "Document uploaded successfully. Please proceed for the next steps.");
			if (!bankLMSUserId.equals(Constants.OTHER_ID_INTEGER)
					|| appFormData.getAppAssignedLmsSalesUserId() != null) {
				if (bankLMSUserId.equals(Constants.OTHER_ID_INTEGER))
					bankLMSUserId = appFormData.getAppAssignedLmsSalesUserId();
				this.taskExecutorService.sendingSMSForAutoLoan(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE,
						Integer.valueOf(0), appFormData, bankLMSUserId, false);
			}
		} catch (NullPointerException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 493 :: processToDocumentPickupUploaded() ", e);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
		} catch (SQLException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 493 :: processToDocumentPickupUploaded() ", e);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
		}
		return json;
	}

	public JSONObject processAddCoapplicant(Integer appSeqId, ApplicationFormAutoLoanQuote quote, String ajaxPostUrl) throws JSONException {
		JSONObject json = new JSONObject();
		try {
			if (appSeqId == null) {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				return json;
			}
			quote = this.autoLoanHelper.saveCoApplicantDetails(quote);
			if (quote == null) {
				json.put("status", "error");
				json.put("message", "Co-applicant not upadated try again.");
				return json;
			}
			if (!ValidatorUtil.isValid(quote.getLoanQuoteId())) {
				json.put("status", "error");
				json.put("message", "Co-applicant not upadated try again.");
				return json;
			}
			json.put("status", "success");
			json.put("message", "Co-applicant upadated.");
			return json;
		} catch (NullPointerException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 517 :: processAddCoapplicant() ", e);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			return json;
		} 
	}

	public JSONObject OTP(Integer appSeqId, Integer stateId, String name, Integer leadLanguageId, int appApplyingFrom,
			int appOTPVerifyType, String isdCode, String mobile, String email, String inputOtp, Integer bankLMSUserId,
			String ajaxPostUrl, Integer loanTypeId) {
		JSONObject json = new JSONObject();
		boolean isAlternate = false;

		try {
			if (appSeqId == null) {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
				json.put("alertCount", 0);
				return json;
			}

			ApplicationFormAutoLoan application = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
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
				//logger.info("alternate number after setter 541 : "  + application.getAppAlternateMobileNumber());
			}

			if (!isAlternate && application.getAppMobileVerificationCode() != null && "Y".equals(application.getAppMobileVerified())) {
				json.put("status", "error");
				json.put("message", "Your mobile no. is already verified");
				json.put("alertCount", 5);
				return json;
			}
			Integer alertCount = Integer.valueOf(0);

			if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14
					|| stateId.intValue() == 15) {

				if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (!ValidatorUtil.isValid(name)
						|| !ValidatorUtil.isValid(mobile) || !ValidatorUtil.isValid(email))) {
					json.put("status", "error");
					json.put("message", "Invalid Request. Refresh browser to proceed ahead");
					json.put("alertCount", alertCount);
				}

				if (ValidatorUtil.isValid(name)) {
					SessionUtil.setApplicantName(name);
					application.setAppFirstName(name);
				}

				if (ValidatorUtil.isValid(mobile)) {
					if (!isAlternate) {
				        application.setAppMobileNo(mobile);
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

				logger.info("isAlternate ::: " + isAlternate);
				
				if (isAlternate) {
					//logger.info("isdcode chekig  inside the if (isAlternate) " + isdCode);
					application.setAppAltISDCode(isdCode);

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
						application = this.autoLoanService.save(application);

						if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4
								|| stateId.intValue() == 15) {
							if (appOTPVerifyType == 0) {
								String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
										Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
								msgBody = this.SbiUtil.urlEncode(msgBody);
								String SMS_TEXT = null;
								
								//logger.info("application.getAppAltISDCode() --- " + application.getAppAltISDCode());
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
									logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode())
											+ application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
								}
								
								if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
									json.put("status", "error");
									json.put("message", "OTP service is down");
									json.put("alertCount", alertCount);
									return json;
								}
							}
							logger.info("before save alternate mobile number into db");
							application.setAppAltMobileVerificationCodeReceived("Y");
							application.setAppAlternateMobileNumber(mobile);
							application = this.autoLoanService.save(application);
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
							boolean isAppFoundForDedupInDropOffStage = false;
							boolean isAppFoundForDedupInDropRejectStage = false;
							if (!Constants.DUMMY_MOBILE_NO.contains(application.getAppAlternateMobileNumber())
									&& !Constants.APP_DUPLICATION_CHECK.equals("0")) {
								boolean isAppFoundForDedupInApplicationStage = false;
								isAppFoundForDedupInApplicationStage = this.autoLoanService
										.isAppFoundForDedupInApplicationStage(
												(application != null) ? application.getAppReferenceId() : null,
												(application.getAppAltISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppAltISDCode(),
												application.getAppAlternateMobileNumber(), null);
								logger.info(
										"AutoProcessImpl.java :: LNo 729  is Alternate mobile number  :: isAppFoundForDedupInApplicationStage "
												+ isAppFoundForDedupInApplicationStage + " with AppSeqId ::"
												+ appSeqId);
								if (isAppFoundForDedupInApplicationStage) {
									json.put("status", "error");
									json.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
									json.put("alertCount", alertCount);
									return json;
								}
								isAppFoundForDedupInDropOffStage = this.autoLoanService
										.isAppFoundForDedupInDropOffStage(
												(application.getAppAltISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppAltISDCode(),
												application.getAppAlternateMobileNumber(), null);
								logger.info(
										"AutoProcessImpl.java :: LNo 737  isALternateMobile number  :: isAppFoundForDedupInDropOffStage "
												+ isAppFoundForDedupInDropOffStage + " with AppSeqId ::" + appSeqId);
								isAppFoundForDedupInDropRejectStage = this.autoLoanService
										.isAppFoundForDedupInDropRejectStage(
												(application.getAppAltISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppAltISDCode(),
												application.getAppAlternateMobileNumber(), null);
								logger.info("AutoProcessImpl.java :: LNo 739:: isAppFoundForDedupInDropRejectStage "
										+ isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
							}
							if (isAppFoundForDedupInDropRejectStage)
								application.setAppMobileDedup(Integer.valueOf(0));
							if (isAppFoundForDedupInDropOffStage)
								application.setAppMobileDedup(Integer.valueOf(1));
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
						
						application.setAppAlternateMobileNumber(mobile);
						application = this.autoLoanService.save(application);

						if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4
								|| stateId.intValue() == 15) {
							if (appOTPVerifyType == 0) {
								String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
										Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
								msgBody = this.SbiUtil.urlEncode(msgBody);
								String SMS_TEXT = null;
								
								//changes to send success message start
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
									logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppAltISDCode())
											+ application.getAppAlternateMobileNumber()).trim() + " is " + application.getAppAltMobileVerificationCode().toString());
								}
								
								if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
									json.put("status", "error");
									json.put("message", "OTP service is down");
									json.put("alertCount", alertCount);
									return json;
								}
							}
							application.setAppAltMobileVerificationCodeReceived("Y");
						
							//logger.info("11method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 4444::" + mobile);
							application.setAppAlternateMobileNumber(mobile);
							//logger.info("22method OTP() >> is Alternate mobile number  application.getAppAlternateMobileNumber() 4444::" + application.getAppAlternateMobileNumber());
							application = this.autoLoanService.save(application);
						}
						json.put("status", "success");
						json.put("message", "OTP Code sent");
						json.put("alertCount", alertCount);
						return json;

					}

				} else {
					if (application.getAppMobileVerificationCode() == null
							|| "N".equals(application.getAppMobileVerified())) {
						if (resend == null && application.getAppMobileNo() != null
								&& !application.getAppMobileNo().equalsIgnoreCase(mobile))
							application.setAppOtpMobileAlertsCount(Integer.valueOf(0));

						if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 15) {

							alertCount = Integer.valueOf((application.getAppOtpMobileAlertsCount() == null) ? 0
									: application.getAppOtpMobileAlertsCount().intValue());
							if (alertCount.intValue() >= 5) {
								json.put("status", "error");
								json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
								json.put("alertCount", alertCount);
								return json;
							}
							boolean isAppFoundForDedupInDropOffStage = false;
							boolean isAppFoundForDedupInDropRejectStage = false;
							if (!Constants.DUMMY_MOBILE_NO.contains(application.getAppMobileNo())
									&& !Constants.APP_DUPLICATION_CHECK.equals("0")) {
								boolean isAppFoundForDedupInApplicationStage = false;
								isAppFoundForDedupInApplicationStage = this.autoLoanService
										.isAppFoundForDedupInApplicationStage(
												(application != null) ? application.getAppReferenceId() : null,
												(application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppISDCode(),
												application.getAppMobileNo(), null);
								logger.info("AutoProcessImpl.java :: LNo 639 :: isAppFoundForDedupInApplicationStage "
										+ isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
								if (isAppFoundForDedupInApplicationStage) {
									json.put("status", "error");
									json.put("message", Constants.APP_DEDUPLICATION_MESSAGE);
									json.put("alertCount", alertCount);
									return json;
								}
								isAppFoundForDedupInDropOffStage = this.autoLoanService
										.isAppFoundForDedupInDropOffStage(
												(application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppISDCode(),
												application.getAppMobileNo(), null);
								logger.info("AutoProcessImpl.java :: LNo 647 :: isAppFoundForDedupInDropOffStage "
										+ isAppFoundForDedupInDropOffStage + " with AppSeqId ::" + appSeqId);
								isAppFoundForDedupInDropRejectStage = this.autoLoanService
										.isAppFoundForDedupInDropRejectStage(
												(application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
														: application.getAppISDCode(),
												application.getAppMobileNo(), null);
								logger.info("AutoProcessImpl.java :: LNo 649:: isAppFoundForDedupInDropRejectStage "
										+ isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
							}
							if (isAppFoundForDedupInDropRejectStage)
								application.setAppMobileDedup(Integer.valueOf(0));
							if (isAppFoundForDedupInDropOffStage)
								application.setAppMobileDedup(Integer.valueOf(1));
							alertCount = Integer.valueOf((application.getAppOtpMobileAlertsCount() == null) ? 0
									: application.getAppOtpMobileAlertsCount().intValue());
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
						application.setAppOTPVerifyType(appOTPVerifyType);

						logger.info("method OTP() >> appOTPVerifyType::" + appOTPVerifyType);
						if (appOTPVerifyType == 0) {
							logger.info("inside auto process impl lno 592");

							// code to generate OTP for mobile no.
							application.setAppMobileVerificationCode(
									this.SbiUtil.getVerificationCode(application.getAppMobileNo()));
							application.setAppAltMobileVerificationCode(
									this.SbiUtil.getVerificationCode(application.getAppAlternateMobileNumber()));
							logger.info("method OTP() >> application.getAppMobileVerificationCode()::"
									+ application.getAppMobileVerificationCode());
						} else if (appOTPVerifyType == 1 && appApplyingFrom == 2) {
							application.setAppEmailVerificationCode(
									this.SbiUtil.getVerificationCodeForEmail(application.getAppWorkEmail()));
							logger.info("inside auto process impl lno 595");
						}
						//logger.info("ALProcessImpl.java Line 574 autoLoanService file OTP method "
							//	+ application.getAppReferenceId());
						application = this.autoLoanService.save(application);

						//logger.info("method OTP() after save method>> AppMobileNo::" + application.getAppMobileNo());
						//logger.info("method OTP() after save method>> AppMobileVerificationCode::"
							//	+ application.getAppMobileVerificationCode());

						//logger.info("ALProcessImpl.java Line 576 autoLoanService file OTP method"
							//	+ application.getAppReferenceId());

						//logger.info("method OTP() >> stateId.intValue()::" + stateId.intValue());
						if (stateId.intValue() == 3 || stateId.intValue() == 14 || stateId.intValue() == 4
								|| stateId.intValue() == 15) {
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
										application.getAppMobileVerificationCode().toString());
								SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
										(String.valueOf(application.getAppISDCode()) + application.getAppMobileNo())
												.trim());
								
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
							if (appApplyingFrom == 2) {
								String msgBody = String.valueOf(Constants.FIRST_EMAIL_PART) + StringEscapeUtils
										.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
												Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0)))
										+ Constants.THIRD_EMAIL_PART;
								msgBody = msgBody.replaceAll("\\[BASE_URL\\]",
										String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
								msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
								msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
								msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
								msgBody = msgBody.replaceAll("\\[OTP_CODE\\]",
										application.getAppEmailVerificationCode());
								msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.AUTO_LOAN_PRODUCT_NAME);
								boolean sendStatus = false;
								String[] emailId = { application.getAppWorkEmail() };
								sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody,
										Constants.OTP_SUBJECT_LINE);
								if (!sendStatus) {
									json.put("status", "error");
									json.put("message", "EMAIL OTP service is down");
									json.put("alertCount", alertCount);
									return json;
								}
							}
							application.setAppMobileVerificationCodeReceived("Y");

							//logger.info("ALProcessImpl.java Line 637 autoLoanService file OTP method"
								//	+ application.getAppReferenceId());
							application = this.autoLoanService.save(application);
							//logger.info("ALProcessImpl.java Line 640 autoLoanService file OTP method"
								//	+ application.getAppReferenceId());
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
				
				boolean isEmail=false;
			       
				if (inputOtp.startsWith("EML_")) {
					isEmail=true;
					inputOtp = inputOtp.substring(4);
				}
				if(!isEmail) {
					 if(inputOtp !=null  ) {
						 SbiUtil sbiutil=new SbiUtil();
						// logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
						 inputOtp=sbiutil.getDecryptedRequest(inputOtp);
					 }
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

					logger.info("AutoProcessManagerImpl.java  LNO 764 ::  application.getAppOTPAttemptCount() : "
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
					
					application.setAppAlternateMobileNumber(mobile);
					application = this.autoLoanService.save(application);
					//logger.info("application after save :: " + application);

					if (application == null) {
						logger.info("AutoProcessManagerImpl.java  LNO 764 :: error on saving::");
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
						json.put("alertCount", 1);
						return json;
					}
					boolean isOPTVerified = false;

					logger.info("method OTP() >> inside if condition 832 :: " + appOTPVerifyType);
					logger.info("application.getAppAltMobileVerificationCode()::: "
							+ application.getAppAltMobileVerificationCode());
					logger.info("inputOtp::: " + inputOtp);

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
					//	logger.info("ALProcessImpl.java Line 725 autoLoanService file OTP method"
						//		+ application.getAppReferenceId());
						application = this.autoLoanService.save(application);
						if (statusManagerResponse.isEligibleToInsertLog())
							this.autoLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(),
									statusManagerResponse.getStatusCallLogs(), null, null, true);
						json.put("status", "success");
						if (stateId.intValue() == 5) {
							json.put("message",
									"Thank you for your interest. Our representative will contact you shortly.");
						} else {
							//logger.info("AutoProcessManagerImpl.java  LNO 832 :: OTP verfied for mobileNo ::"
								//	+ application.getAppAlternateMobileNumber() + " with AppSeqId ::" + appSeqId);
							json.put("message", "OTP authentication successful");
						}
						json.put("alertCount", alertCount);
						return json;
					}

					//logger.info("AutoProcessManagerImpl.java  LNO 838 :: OTP is incorrect for mobileNo ::"
					//		+ application.getAppMobileNo() + " with AppSeqId ::" + appSeqId);
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
                    if(!isEmail) {
					if (!ValidatorUtil.isValid(inputOtp)) {
						json.put("status", "error");
						json.put("message", "Please enter valid OTP.");
						json.put("alertCount", alertCount);
						return json;
					}
                    }
					logger.info("AutoProcessManagerImpl.java  LNO 764 ::  application.getAppOTPAttemptCount() : "
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
					application = this.autoLoanService.save(application);
					if (application == null) {
						logger.info("AutoProcessManagerImpl.java  LNO 764 :: error on saving::");
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT);
						json.put("alertCount", 1);
						return json;
					}
					boolean isOPTVerified = false;

					if (appOTPVerifyType == 0) {
						logger.info("method OTP() >> inside if condition 912 :: " + appOTPVerifyType);
						if (application.getAppMobileVerificationCode() != null && String
								.valueOf(application.getAppMobileVerificationCode()).equalsIgnoreCase(inputOtp)) {
							isOPTVerified = true;
							logger.info("method OTP() >> isOPTVerified 2::" + isOPTVerified);
							logger.info(
									"method OTP() >> setAppMobileVerified 3::" + application.getAppMobileVerified());
							application.setAppMobileVerified("Y");
						}
					} else if (appOTPVerifyType == 1 && application.getAppEmailVerificationCode() != null
							&& application.getAppEmailVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
						isOPTVerified = true;
						application.setAppEmailVerified("Y");
					}
					application.setAppOTPVerifyType(appOTPVerifyType);
					if (isOPTVerified) {
						ApplicationFormAutoLoanQuote quote = this.autoLoanService
								.getApplicationFormAutoLoanQuoteByQuoteId(application.getAppQuoteId());
						if (quote != null && quote.getLoanQuoteResidentTypeId() != null
								&& quote.getLoanQuoteResidentTypeId().intValue() == 2) {
							application.setAppResTypeAtVerified(Integer.valueOf(2));
						} else {
							application.setAppResTypeAtVerified(Integer.valueOf(1));
						}
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
						logger.info("ALProcessImpl.java Line 725 autoLoanService file OTP method"
								+ application.getAppReferenceId());
						application = this.autoLoanService.save(application);
						if (statusManagerResponse.isEligibleToInsertLog())
							this.autoLoanHelper.insertCallLog(application.getAppSeqId(), bankLMSUserId.intValue(),
									statusManagerResponse.getStatusCallLogs(), null, null, true);
						json.put("status", "success");
						if (stateId.intValue() == 5) {
							json.put("message",
									"Thank you for your interest. Our representative will contact you shortly.");
						} else {
							//logger.info("AutoProcessManagerImpl.java  LNO 832 :: OTP verfied for mobileNo ::"
							//		+ application.getAppMobileNo() + " with AppSeqId ::" + appSeqId);
							json.put("message", "OTP authentication successful");
						}
						json.put("alertCount", alertCount);
						return json;
					}

				//	logger.info("AutoProcessManagerImpl.java  LNO 838 :: OTP is incorrect for mobileNo ::"
					//		+ application.getAppMobileNo() + " with AppSeqId ::" + appSeqId);
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

			}
		} catch (NullPointerException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 846 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 611 :: second OTP() ", (Throwable) e1);
			}
		} catch (JSONException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 846 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 611 :: second OTP() ", (Throwable) e1);
			}
		}catch (SQLException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 846 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 611 :: second OTP() ", (Throwable) e1);
			}
		}
		return json;
	}

	private JSONObject OTP(Integer appSeqId, Integer stateId, String name, Integer leadLanguageId, int appApplyingFrom,
			String isdCode, String mobile, String email, String inputOtp, Integer trackVisitId, Integer bankLMSUserId,
			String ajaxPostUrl, Integer loanTypeId,Integer commonLoanType) {
		JSONObject json = new JSONObject();
		try {
			ApplicationFormLead lead = null;
			if (SessionUtil.getLeadId() != null) {
				lead = this.commonService.getLeadById(SessionUtil.getLeadId());
			} else {
				lead = new ApplicationFormLead();
			}
			Integer alertCount = Integer.valueOf(0);
			if (stateId.intValue() == 3 || stateId.intValue() == 4 || stateId.intValue() == 14
					|| stateId.intValue() == 15) {
				if ((stateId.intValue() == 3 || stateId.intValue() == 4) && (!ValidatorUtil.isValid(name)
						|| !ValidatorUtil.isValid(mobile) || !ValidatorUtil.isValid(email))) {
					json.put("status", "error");
					json.put("message", "Invalid Request. Refresh browser to proceed ahead");
				}
				if ((stateId.intValue() == 14 || stateId.intValue() == 15) && !ValidatorUtil.isValid(mobile)) {
					json.put("status", "error");
					json.put("message", "Invalid Request. Refresh browser to proceed ahead");
					return json;
				}
				if (ValidatorUtil.isValid(leadLanguageId))
					lead.setLeadLanguageId(leadLanguageId);
				if (ValidatorUtil.isValid(name)) {
					SessionUtil.setApplicantName(name);
					lead.setLeadFirstName(name);
				}
				if (appApplyingFrom == 2) {
					lead.setLeadApplyingFrom(appApplyingFrom);
					isdCode = Constants.COUNTRY_CODE_INDIA;
					lead.setLeadIsdCode(isdCode);
					SessionUtil.setISDCode(isdCode);
					if (ValidatorUtil.isValid(mobile)) {
						lead.setLeadMobileNo(mobile);
						SessionUtil.setMobile(mobile);
					}
				} else {
					lead.setLeadApplyingFrom(1);
					lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA);
					SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
					if (ValidatorUtil.isValid(mobile)) {
						lead.setLeadMobileNo(mobile);
						SessionUtil.setMobile(mobile);
					}
				}
				if (!Constants.DUMMY_MOBILE_NO.contains(mobile) && !Constants.INQUIRY_DUPLICATION_CHECK.equals("0")) {
					boolean isLeadExists = false;
					isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.AUTO_LOAN_ID,
							lead.getLeadLoanPurposeId(), lead.getLeadIsdCode(), lead.getLeadMobileNo(), loanTypeId);
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
				if (lead.getLeadMobileVerificationCode() == null
						|| "N".equals(lead.getLeadMobileVerificationCodeVerified())) {
					if (!lead.getLeadMobileNo().equalsIgnoreCase(mobile))
						lead.setLeadMobileAlertCount(Integer.valueOf(0));
					if (stateId.intValue() == 3 || stateId.intValue() == 14) {
						alertCount = Integer.valueOf((lead.getLeadMobileAlertCount() == null) ? 0
								: lead.getLeadMobileAlertCount().intValue());
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
						lead.setLeadProductTypeId(Constants.AUTO_LOAN_ID);
						lead.setLeadLoanPurposeId(Integer.valueOf(3));
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
						lead.setLeadMobileVerificationCode(this.SbiUtil.getVerificationCode(mobile));
					}
					alertCount = Integer.valueOf(alertCount.intValue() + 1);
					lead.setLeadMobileAlertCount(alertCount);
					lead.setLeadLastUpdated(new Date());
					 logger.info("commonLoanType 1343 "+commonLoanType);
			            if(commonLoanType==2) {
						Integer consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentId();
						logger.info("consentTextNtb 1346 "+consentTextNtb);
					    lead.setLeadConsentId(consentTextNtb);
			            }
					
					  
					try {
						lead = this.commonService.save(lead);
						logger.info("consentTextNtb  after save to db 1353 "+lead.getLeadConsentId());
						if (stateId.intValue() == 3)
							this.commonService.insertCallLog(lead.getLeadId(), bankLMSUserId,
									Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE124_ID), null);
						json.put("status", "success");
						json.put("message", "OTP Code sent");
						json.put("alertCount", alertCount);
					} catch (JSONException e) {
						logger.info("AutoProcessManagerImpl.java LNo : 995 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCount", alertCount);
						return json;
					}
					SessionUtil.setLeadId(lead.getLeadId());
					if (stateId.intValue() == 3 || stateId.intValue() == 4) {
						String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
								Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
						msgBody = this.SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;
						if (lead.getLeadApplyingFrom() == 2) {
							SMS_TEXT=Constants.SMS_STRING_NRI;
						} else {
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}
						SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", lead.getLeadMobileVerificationCode().toString());
						SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (String.valueOf(isdCode) + mobile).trim());
						
						if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							logger.info("OTP for Mobile Number: " + (String.valueOf(isdCode) + mobile).trim() + " is " + lead.getLeadMobileVerificationCode().toString());
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
					json.put("message", "Please enter valid OTP.");
					json.put("alertCount", alertCount);
					return json;
				}
				logger.info("AutoProcessManagerImpl.java LNo : 1041  lead.getLeadOTPAttempCount() : "
						+ lead.getLeadOTPAttempCount() + " with AppSeqId ::" + appSeqId);
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
					logger.info("AutoProcessManager.java LNO 1054 error on saving::");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCount", 1);
					return json;
				}
				if (lead.getLeadMobileVerificationCode() != null
						&& lead.getLeadMobileVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
					if (SessionUtil.getLeadId() != null)
						lead = this.commonService.getLeadById(SessionUtil.getLeadId());
					lead.setLeadMobileVerificationCodeVerified("Y");
					lead.setLeadLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID));
					try {
						lead = this.commonService.save(lead);
						this.commonService.insertCallLog(lead.getLeadId(), Constants.OTHER_ID_INTEGER,
								Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE105_ID), null);
					} catch (NullPointerException e) {
						logger.info("AutoProcessManagerImpl.java LNo : 1081 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCount", alertCount);
						return json;
					}
					logger.info("AutoProcessManagerImpl LNO ::995 , log for CALLBACK_SMS_CONSENT "
							+ Constants.CALLBACK_SMS_CONSENT);
					if (Constants.CALLBACK_SMS_CONSENT) {
						logger.info("AutoProcessManagerImpl LNO ::997 , log for CALLBACK_SMS_CONSENT "
								+ lead.getLeadMobileVerificationCodeVerified());
						if ("Y".equalsIgnoreCase(lead.getLeadMobileVerificationCodeVerified())) {
							logger.info("AutoProcessManagerImpl LNO ::999 , log for CALLBACK_SMS_CONSENT "
									+ Constants.MESSAGE_TYPE_SMS);
							String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
									Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
							msgBody = this.SbiUtil.urlEncode(msgBody);
							String SMS_TEXT = null;
							
							if (lead.getLeadApplyingFrom() == 2) {
								SMS_TEXT=Constants.SMS_STRING_NRI;
							} else {
								SMS_TEXT=Constants.SMS_STRING_INDIAN;
							}
							SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
							SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
									(String.valueOf(lead.getLeadIsdCode()) + lead.getLeadMobileNo()).trim());
							
							if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
								logger.info("AutoProcessManagerImpl LNO ::1015 , OTP service is down:: msg not send");
								json.put("status", "error");
								json.put("message", "sms service is down");
								return json;
							}
						}
					}
					json.put("status", "success");
					if (stateId.intValue() == 5) {
						logger.info("AutoProcessManagerImpl LNO ::1025 :: message has been sent to user ");
						json.put("message",
								"Thank you for your interest. Our representative will contact you shortly.");
					} else {
						//logger.info("AutoProcessManagerImpl.java  LNO 1092 :: OTP verfied for mobileNo ::"
						//		+ lead.getLeadMobileNo() + " with AppSeqId ::" + appSeqId);
						json.put("message", "OTP authentication successful");
					}
					json.put("alertCount", alertCount);
					//logger.info("AutoProcessManagerImpl.java  LNO 1110 Before Calling Call Center Service ----" + lead);
					if (!Constants.CLICK_TO_CALL_BYPASS && lead != null && lead.getLeadLoanPurposeId() != null
							&& lead.getLeadLoanPurposeId().intValue() != 10) {
						logger.info("AutoProcessManagerImpl.java  LNO 1117----Service Response message----");
						this.callBackService.getcallBackService(lead);
						lead = this.commonService.save(lead);
					}
					logger.info("AutoProcessManagerImpl.java  LNO 1122 after Calling Call Center Service ----" + lead);
					SessionUtil.setLeadId(null);
					return json;
				}
				//logger.info(" AutoProcessManagerImpl.java  LNO 1099 :: OTP is incorrect for mobileNo ::"
				//		+ lead.getLeadMobileNo() + " with AppSeqId ::" + appSeqId);
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
			logger.info("AutoLoanProcessImpl.java LNo: 1107 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", "OTP process is down");
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 1113 :: OTP() ", e);
			}
		} catch (JSONException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 1107 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", "OTP process is down");
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 1113 :: OTP() ", e);
			}
		}catch (SQLException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 1107 :: OTP() ", e);
			try {
				json.put("status", "error");
				json.put("message", "OTP process is down");
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("AutoLoanProcessImpl.java LNo: 1113 :: OTP() ", e);
			}
		}
		return json;
	}

	public JSONObject processMobileOTP(Integer appSeqId, Integer stateId, Integer trackVisitId, Integer bankLMSUserId,
			String ajaxPostUrl, Integer loanTypeId, OtherRequest otherRequest) throws JSONException {
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
			if (otherRequest != null && otherRequest.getMobile() != null) {
				mobile = otherRequest.getMobile();
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
		boolean isEmail=false;
		
		String inputOtp = null;
		if (appOTPVerifyType == 0) {
			if (otherRequest != null && otherRequest.getInputOtp() != null)
				inputOtp = otherRequest.getInputOtp();
		} else if (appOTPVerifyType == 1 && otherRequest != null && otherRequest.getInputOtpEmail() != null) {
			isEmail=true;
			inputOtp = otherRequest.getInputOtpEmail();
		}
        if(isEmail) {
        	if (trackVisitId == null && otherRequest.getAlternateMobileNumber() == null) {
    			json = OTP(appSeqId, stateId, null, null, appApplyingFrom, appOTPVerifyType, isdCode, mobile, email,
    					"EML_" +inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId);
    		}
        }else {
		if (trackVisitId == null && otherRequest.getAlternateMobileNumber() == null) {
			json = OTP(appSeqId, stateId, null, null, appApplyingFrom, appOTPVerifyType, isdCode, mobile, email,
					inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId);
		}
        }
	//	logger.info("before OTP call>>otherRequest.getAlternateMobileNumber()::" + otherRequest.getAlternateMobileNumber());
//		logger.info("before OTP call>>mobile::" + mobile);
		if (otherRequest.getAlternateMobileNumber() != null) {
			if (!otherRequest.getAlternateMobileNumber().equals(mobile)) {
				json = OTP(appSeqId, stateId, null, null, appApplyingFrom, appOTPVerifyType, otherRequest.getIsdCodeAlt(),
						"ALT_" + otherRequest.getAlternateMobileNumber(), email, otherRequest.getInputOtpAlt(), bankLMSUserId, ajaxPostUrl,
						loanTypeId);
				
			} else {
				json.put("status", "error");
				json.put("message", "Alternate Mobile cannot be same as Mobile number.");
				json.put("alertCount", 0);
			}
		}
		return json;
	}

	public JSONObject processWantUsToCallYou(Integer appSeqId, Integer stateId, Integer trackVisitId,
			Integer bankLMSUserId, String ajaxPostUrl, Integer loanTypeId, OtherRequest otherRequest) throws SQLException, JSONException {
		JSONObject json = new JSONObject();
		String name = null;
		String mobile = null;
		String isdCode = null;
		int appApplyingFrom = 0;
		int appOTPVerifyType = 0;
		int leadLanguageId = 0;
		if (otherRequest.getName() != null)
			name = otherRequest.getName().toString();
		if (otherRequest.getLeadLanguageId() != null)
			leadLanguageId = Integer.parseInt(otherRequest.getLeadLanguageId().toString());
		if (otherRequest.getAppApplyingFrom() != null)
			appApplyingFrom = Integer.parseInt(otherRequest.getAppApplyingFrom().toString());
		if (otherRequest.getAppOTPVerifyType() != null)
			appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType());
		if (otherRequest.getIsdCodeWantUsToCallYou() != null)
			isdCode = otherRequest.getIsdCodeWantUsToCallYou().toString();
		if (appApplyingFrom == 2) {
			if (otherRequest.getNriMobileWantUsToCallYou() != null)
				mobile = otherRequest.getNriMobileWantUsToCallYou().toString();
		} else if (otherRequest.getMobileWantUsToCallYou() != null) {
			mobile = otherRequest.getMobileWantUsToCallYou().toString();
		}
		int count = this.commonService.getCallBackLeadCount(Integer.valueOf(3), isdCode, mobile);
		logger.info("AutoProcessManagerImpl.java LNo : 1359 count : " + count + " with AppSeqId ::" + appSeqId);
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
	      	logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
	      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
//			logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
	      }
		  
		   logger.info("(otherRequest.getCommmonloanType() AL   1647  "+otherRequest.getCommmonloanType());
		
		logger.info("inside the processWantUsToCallYou method in autoProcessimpl Lno 1581 ");
		if (trackVisitId != null) {
			json = OTP(appSeqId, stateId, name, Integer.valueOf(leadLanguageId), appApplyingFrom, isdCode, mobile,
					email, inputOtp, trackVisitId, bankLMSUserId, ajaxPostUrl, loanTypeId,otherRequest.getCommmonloanType());
		} else {
			json = OTP(appSeqId, stateId, name, Integer.valueOf(leadLanguageId), appApplyingFrom, appOTPVerifyType,
					isdCode, mobile, email, inputOtp, bankLMSUserId, ajaxPostUrl, loanTypeId);
		}
		return json;
	}

	public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormAutoLoanQuote quote, Integer trackVisitId,
			String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) {
		LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
		try {
			
			if (quote == null) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
				return loanScenarioBean;
			}
			ApplicationFormAutoLoan application = null;
			if (appSeqId != null) {
				application = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
				if (application == null) {
					loanScenarioBean.setStatus(Integer.valueOf(0));
					loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
					return loanScenarioBean;
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
						application.setAppDobDT(
								DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
					}
					if (quote.getAppEmail() != null)
						application.setAppWorkEmail(quote.getAppEmail());
					if (quote.getAppMobile() != null)
						application.setAppMobileNo(quote.getAppMobile());
				}
				//logger.info(" cheking the value alternate mobile number " +quote.getAlternateMobileNumber());
				if(quote.getAlternateMobileNumber() !=null) {
					application.setAppAlternateMobileNumber(quote.getAlternateMobileNumber());
					application.setAppAltISDCode(quote.getAppAltISDCode());
					SessionUtil.setalternateMobileNumber(quote.getAppMobile());
			
				}
			}
			if (application != null && application.getAppMobileVerified() != null
					&& application.getAppMobileVerified().equalsIgnoreCase("N")
					&& ajaxPostUrl.equalsIgnoreCase("auto-loan"))
				if (application.getAppApplyingFrom() == 2) {
					if (application.getAppMobileNo() != null
							&& !application.getAppMobileNo().equalsIgnoreCase(quote.getAppNRIMobileNo())
							&& application.getAppMobileVerified().equalsIgnoreCase("Y")) {
						loanScenarioBean.setStatus(Integer.valueOf(0));
						loanScenarioBean.setMessage("You have already verified OTP with NRI mobile number : "
								+ application.getAppMobileNo());
						return loanScenarioBean;
					}
				} else if (application.getAppMobileNo() != null
						&& !application.getAppMobileNo().equalsIgnoreCase(quote.getAppMobile())
						&& application.getAppMobileVerified().equalsIgnoreCase("Y")) {
					loanScenarioBean.setStatus(Integer.valueOf(0));
					loanScenarioBean.setMessage(
							"You have already verified OTP with mobile number : " + application.getAppMobileNo());
					return loanScenarioBean;
				}
			int oldVisitId = 0;
			if (application != null && application.getAppQuoteId() != null) {
				oldVisitId = this.autoLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
			} else {
				oldVisitId = trackVisitId.intValue();
			}
			quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
			quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
			quote.setLoanQuoteNewVisitId(trackVisitId);
			quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
			if (quote.getLoanQuoteEmploymentTypeId() != null && quote.getLoanQuoteEmploymentTypeId().intValue() == 9)
				quote.setLoanQuoteEmployerCompanyId(
						this.commonService.getAllEmployerIdByName(quote.getLoanQuoteEmployerName()));
			if (application != null) {
				if (application.getAppOTPVerifyType() == 0) {
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
			if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS
					&& (appSeqId == null
							|| (application != null
									&& application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS
											.intValue()
									&& "Y".equalsIgnoreCase(application.getAppMobileVerified())
									&& !ValidatorUtil.isValid(application.getAppReferenceId()))
							|| (application != null
									&& application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL
											.intValue()
									&& "N".equalsIgnoreCase(application.getAppMobileVerified())
									&& !ValidatorUtil.isValid(application.getAppReferenceId()))
							|| (application != null
									&& application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC
											.intValue()
									&& "N".equalsIgnoreCase(application.getAppMobileVerified())
									&& !ValidatorUtil.isValid(application.getAppReferenceId())))) {
				String oldMobile = quote.getAppMobile();
				String isdCode = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
				if (quote.getAppMobile() != null && !Constants.DUMMY_MOBILE_NO.contains(oldMobile)
						&& !Constants.APP_DUPLICATION_CHECK.equals("0"))
					if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
						boolean isLeadExists = false;
						isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.AUTO_LOAN_ID,
								Integer.valueOf(3), isdCode, oldMobile, loanTypeId);
						logger.info("AutoProcessImpl.java :: LNo  1499 :: isLeadExists setting in session "
								+ isLeadExists + " with AppSeqId ::" + appSeqId);
						if (isLeadExists) {
							loanScenarioBean.setStatus(Integer.valueOf(0));
							loanScenarioBean.setMessage(Constants.INQUIRY_DEDUPLICATION_MESSAGE);
							return loanScenarioBean;
						}
					} else {
						boolean isAppFoundForDedupInApplicationStage = false;
						isAppFoundForDedupInApplicationStage = this.autoLoanService
								.isAppFoundForDedupInApplicationStage(
										(application != null) ? application.getAppReferenceId() : null, isdCode,
										oldMobile, null);
						logger.info("AutoProcessImpl.java :: LNo 1508 :: isAppFoundForDedupInApplicationStage "
								+ isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
						if (isAppFoundForDedupInApplicationStage) {
							loanScenarioBean.setStatus(Integer.valueOf(0));
							loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
							return loanScenarioBean;
						}
						isAppFoundForDedupInDropOffStage = this.autoLoanService
								.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, null);
						logger.info("AutoProcessImpl.java :: LNo 1515 :: isAppFoundForDedupInDropOffStage "
								+ isAppFoundForDedupInDropOffStage);
						isAppFoundForDedupInDropRejectStage = this.autoLoanService
								.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, null);
						logger.info("AutoProcessImpl.java :: LNo 1517 :: isAppFoundForDedupInDropRejectStage "
								+ isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
					}
			}
			Integer quoteApplyingFrom = Integer.valueOf(quote.getAppApplyingFrom());
			String quoteMobileNRI = quote.getAppNRIMobileNo();
			String quoteMobile = quote.getAppMobile();
			String quoteISD = quote.getAppISDCode();
			
			 if (quote.getLoanQuoteExshowroomPriceCar() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteExshowroomPriceCar() )) {
		    	  loanScenarioBean.setStatus(Integer.valueOf(0));
		    	  loanScenarioBean.setMessage("Ex- showroom price cannot be greater than 9 digits.|2");
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
			
			
			
			
			quote = this.autoLoanHelper.insertLoanQuote(quote,
					(bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId()
							: Constants.OTHER_USER_ID,
					trackVisitId);
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
			int previousQuoteId = (application != null && application.getAppQuoteId() != null)
					? application.getAppQuoteId().intValue()
					: 0;
			quote.setAppApplyingFrom(quoteApplyingFrom.intValue());
			quote.setAppMobile(quoteMobile);
			quote.setAppNRIMobileNo(quoteMobileNRI);
			quote.setAppISDCode(quoteISD);
		    logger.info("before calling the insertAppLoan chaking the value of ");
			application = this.autoLoanHelper.insertAppLoan(quote, application,
					(bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null,
					Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null)
							? bankLmsUser.getLmsUserIntermediaryId().intValue()
							: 0));
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
			SessionUtil.setAutoLoanApplicationSequenceId(application.getAppSeqId());
			if (isAppFoundForDedupInDropRejectStage)
				application.setAppMobileDedup(Integer.valueOf(0));
			if (isAppFoundForDedupInDropOffStage)
				application.setAppMobileDedup(Integer.valueOf(1));
			if (SessionUtil.getApplicationCRMLeadId() != null)
				application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId());
			logger.info(
					"AutoLoanProcessImpl.java LNo: 1331 :: processGetQuotes() :: quote.getLoanQuoteOccupationCategory():: "
							+ quote.getLoanQuoteOccupationCategory() + " " + quote.toString());
			loanScenarioBean = this.autoLoanHelper.callBRE(application, quote, bankLmsUser,
					Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
			if (loanScenarioBean.getStatus().intValue() != 1)
				return loanScenarioBean;
			application = loanScenarioBean.getApplicationAL();
			if (ajaxPostUrl.equalsIgnoreCase(Constants.AUTO_LOAN_ACTION)
					&& application.getAppMobileVerificationCodeReceived() == null) {
				String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
						Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
				msgBody = this.SbiUtil.urlEncode(msgBody);
				String SMS_TEXT = null;
				
				if (application.getAppApplyingFrom() == 2) {
					SMS_TEXT=Constants.SMS_STRING_NRI;
				} else {
					SMS_TEXT=Constants.SMS_STRING_INDIAN;
				}
				SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
				SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", application.getAppMobileVerificationCode().toString());
				SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
						(String.valueOf(application.getAppISDCode()) + application.getAppMobileNo()).trim());
				
				if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
					logger.info("OTP for Mobile Number: " + (String.valueOf(application.getAppISDCode()) + application.getAppMobileNo()).trim() + " is " + application.getAppMobileVerificationCode().toString());
				}
				
				
				
				if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
					loanScenarioBean.setStatus(Integer.valueOf(0));
					loanScenarioBean.setMessage("OTP service is down");
					return loanScenarioBean;
				}
				if (application.getAppApplyingFrom() == 2) {
					msgBody = String.valueOf(Constants.FIRST_EMAIL_PART)
							+ StringEscapeUtils
									.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
											Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0)))
							+ Constants.THIRD_EMAIL_PART;
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
				//logger.info("ALProcessImpl.java Line 1404 autoLoanService file OTP method"
					//	+ application.getAppReferenceId());
				application = this.autoLoanService.save(application);
			}
			if (Constants.AUTO_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl) && application != null
					&& SessionUtil.getApplicationCRMLeadId() != null) {
				CRMRequest crmRequest = new CRMRequest();
				crmRequest.setCrmLeadId(SessionUtil.getApplicationCRMLeadId());
				crmRequest.setReferenceNumber(application.getAppSeqId());
				crmRequest.setLoanTypeId(Constants.AUTO_LOAN_ID);
			}
			return loanScenarioBean;
		} catch (NullPointerException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 2190 :: processGetQuotes() ", e);
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
			return loanScenarioBean;
		} catch (SQLException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 2190 :: processGetQuotes() ", e);
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
			return loanScenarioBean;
		}
	}

	public ApplicationFormAutoLoan processSubmitQuote(Integer appSeqId, Integer requestIndex,
			ApplicationFormAutoLoan appForm, String isDsrPage, Integer bankLMSUserId) throws SQLException, NullPointerException, RuntimeException, ParseException {
		ApplicationFormAutoLoan appFormData = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
//		logger.info("cheking in appFormData 4 "+appFormData.getAppAlternateMobileNumber());
		logger.info("appFormData.getAppDataSourceId() 1 " + appFormData.getAppDataSourceId());
		logger.info("Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD 2 " + Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD);
		logger.info("appFormData.getAppDataSourceId() 3 " + appFormData.getAppDataSourceId());

		logger.info("Constants.LEAD_DATA_SOURCE_ID_CALL_BACK 4" + Constants.LEAD_DATA_SOURCE_ID_CALL_BACK);
		logger.info("appFormData.getAppMobileVerified() 5 " + appFormData.getAppMobileVerified());
		logger.info("appFormData.getAppEmailVerified() 6" + appFormData.getAppEmailVerified());

		//logger.info("appFormData.getAppAlternateMobileNumber() :: " + appFormData.getAppAlternateMobileNumber());

		if ((appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD)
				|| appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK))
				&& appFormData.getAppMobileVerified().equalsIgnoreCase("N")
				&& appFormData.getAppEmailVerified().equalsIgnoreCase("N")) {
			logger.info("inside the id condition in 7 ");
			appFormData.setError(" AutoLoanProcessImpl.java LNo: 2205 :: Mobile OTP is not verified");
			return appFormData;
		}
		ApplicationFormAutoLoanQuote quote = this.autoLoanService
				.getApplicationFormAutoLoanQuoteByQuoteId(appFormData.getAppQuoteId());
		if (quote == null)
			return null;
		//logger.info("ALProcessImpl.java :: LNo :: 1408 quote.getLoanQuoteId() :: " + quote.getLoanQuoteId()
			//	+ " with AppSeqId ::" + appSeqId + "quote In String Form" + quote.toString());
		logger.info("ALProcessImpl.java :: LNo :: 2212 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
		if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
			if (ValidatorUtil.isValid(appForm.getAppStateId())) {
				appFormData.setAppStateId(appForm.getAppStateId());
			} else {
				logger.info("ALProcessIMPL.java :: LNo 2217 :: appForm.getAppStateId() is null ");
				appFormData.setError("Please select the state.|appForm.appStateId|1");
				return appFormData;
			}
			if (ValidatorUtil.isValid(appForm.getAppCityId())) {
				appFormData.setAppCityId(appForm.getAppCityId());
			} else {
				logger.info("ALProcessIMPL.java :: LNo 2224 :: appForm.getAppStateId() is null ");
				appFormData.setError("Please select the state.|appForm.appStateId|1");
				return appFormData;
			}
			if (appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER))
				if (ValidatorUtil.isValid(appForm.getAppDistrictId())) {
					appFormData.setAppDistrictId(appForm.getAppDistrictId());
				} else {
					logger.info("ALProcessIMPL.java :: LNo 2232 :: appForm.getAppStateId() is null ");
					appFormData.setError("Please select the city.|appForm.appCityId|1");
					return appFormData;
				}
			if (ValidatorUtil.isValid(appForm.getAppBranchId())) {
				appFormData.setAppBranchId(appForm.getAppBranchId());
			} else {
				logger.info("ALProcessIMPL.java :: LNo 2240 :: appForm.getAppBranchId() is null ");
				if (appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER)) {
					appFormData.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|appForm.appDistrictId|1");
				} else {
					appFormData.setError(String.valueOf(Constants.BRANCH_NOT_FOUND_MSG) + "|appForm.appCityId|1");
				}
				return appFormData;
			}
		}
		if (!ValidatorUtil.isValid(appFormData.getAppReferenceId()))
			if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && appFormData.getAppMobileNo() != null
					&& !Constants.DUMMY_MOBILE_NO.contains(appFormData.getAppMobileNo())
					&& !Constants.APP_DUPLICATION_CHECK.equals("0")) {
				boolean isAppFoundForDedupInApplicationStage = false;
				isAppFoundForDedupInApplicationStage = this.autoLoanService.isAppFoundForDedupInApplicationStage(null,
						(appFormData.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA
								: appFormData.getAppISDCode(),
						appFormData.getAppMobileNo(), null);
				logger.info("AutoProcessImpl.java :: LNo 1535 :: thankyoupage isAppFoundForDedupInApplicationStage "
						+ isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
				if (isAppFoundForDedupInApplicationStage) {
					this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
							Constants.CALL_LOGS_MESSAGE_STATE6_ID, null, null, true);
					appFormData.setAppLoanStatusId(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE6_ID));
					appFormData.setError(Constants.APP_DEDUPLICATION_MESSAGE);
					return appFormData;
				}
			}
		logger.info("ALProcessImpl.java :: LNo :: 2267 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
		
		
		//validate name fields before saving
//		logger.info("first_name :: " + appForm.getAppFirstName());
//		logger.info("middle_name :: " + appForm.getAppMiddleName());
//		logger.info("last_name :: " + appForm.getAppLastName());
		
		/* if (!validateLeadName(appForm.getAppFirstName(), appForm.getAppMiddleName(), appForm.getAppLastName())) {
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
		 
	    if(appForm.getAppFirstName().trim().equalsIgnoreCase(appForm.getAppLastName().trim()) ||
	    		(appForm.getAppMiddleName()!=null &&
	    		(appForm.getAppFirstName().trim().equalsIgnoreCase(appForm.getAppMiddleName().trim()) || appForm.getAppMiddleName().trim().equalsIgnoreCase(appForm.getAppLastName().trim())))) {
			  appFormData.setError("For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
			  return appFormData;
		}

	    if (appForm.getAppPanCardNo() != null && !ValidatorUtil.isValidPanNo(appForm.getAppPanCardNo() )) {
	      	appFormData.setError("PAN is not in correct format.|2");
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
			      //if (!(appForm.getAppAddress2() != null && appForm.getAppAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
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
		if (appForm.getAppCustomerSignature() != null)
			appFormData.setAppCustomerSignature(appForm.getAppCustomerSignature());
		if (quote.getLoanQuoteResidentTypeId() != null && quote.getLoanQuoteResidentTypeId().intValue() == 2) {
			if (appForm.getAppCityId() != null) {
				if (!appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER)) {
					List<MasterBranch> branches = this.commonService.getBranchByLoanTypeStateDistrictRACPCScholar(
							Constants.AUTO_LOAN_ID, null, appForm.getAppCityId(), null, null, null, null, null);
					if (branches != null && !branches.isEmpty()) {
						if (branches.size() == 1) {
							MasterBranch masterBranch = branches.get(0);
							if (masterBranch != null) {
								Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(
										Constants.AUTO_LOAN_ID, masterBranch.getBranchId(),
										quote.getLoanQuoteLoanPurposeId());
								appFormData.setAppSalesTeamId(salesTeamId);
								appFormData.setAppDistrictId(masterBranch.getBranchDistrictId());
							}
						}
					} else if (appForm.getAppBranchId() != null) {
						MasterBranch masterBranch = this.commonService.getBranchById(appForm.getAppBranchId());
						if (masterBranch != null) {
							Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(
									Constants.AUTO_LOAN_ID, masterBranch.getBranchId(),
									quote.getLoanQuoteLoanPurposeId());
							appFormData.setAppSalesTeamId(salesTeamId);
							appFormData.setAppDistrictId(masterBranch.getBranchDistrictId());
						}
					}
				}
				if (appForm.getAppBranchId() != null) {
					MasterBranch masterBranch = this.commonService.getBranchById(appForm.getAppBranchId());
					if (masterBranch != null) {
						Integer salesTeamId = this.commonService.getHLSTMPSTByLoanTypeAndBranchId(
								Constants.AUTO_LOAN_ID, masterBranch.getBranchId(), quote.getLoanQuoteLoanPurposeId());
						appFormData.setAppSalesTeamId(salesTeamId);
						appFormData.setAppDistrictId(masterBranch.getBranchDistrictId());
					}
				}
			}
			if (ValidatorUtil.isValid(appForm.getAppBranchId())) {
				Integer[] circleIdNetworkModuleRegionId = this.commonService
						.getCircleIdNetworkModuleRegionByBranchId(appForm.getAppBranchId());
				if (ValidatorUtil.isValid(circleIdNetworkModuleRegionId[0])
						&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[1])
						&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[2])
						&& ValidatorUtil.isValid(circleIdNetworkModuleRegionId[3])) {
					appFormData.setAppCircleId(circleIdNetworkModuleRegionId[0]);
					appFormData.setAppNetworkId(circleIdNetworkModuleRegionId[1]);
					appFormData.setAppModuleId(circleIdNetworkModuleRegionId[2]);
					appFormData.setAppRegionId(circleIdNetworkModuleRegionId[3]);
				} else {
					appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
					return appFormData;
				}
			} else {
				logger.info("ALProcessIMPL.java :: LNo 2342 :: appForm.getAppBranchId() is null ");
				appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return appFormData;
			}
		}
		if (appForm.getAppCityId().intValue() == Constants.OTHER_ID_INTEGER.intValue()
				&& appForm.getAppDistrictId() == null) {
			appFormData.setError("Invalid params");
			return appFormData;
		}
		if (appForm.getAppPincode() != null) {
			int pincodeInitial = appForm.getAppPincode().intValue() / 10000;
			String pinlastfix = appForm.getAppPincode().toString().substring(3, 6);
			MasterState state = this.commonService
					.getStateById((appForm.getAppStateId() != null) ? appForm.getAppStateId() : null);
			if (state != null && state.getStatePinMinStartPrefix() != null
					&& state.getStatePinMaxStartPrefix() != null) {
				if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue()
						&& pincodeInitial <= state.getStatePinMaxStartPrefix().intValue()
						&& !pinlastfix.equals("000")) {
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
		appFormData.setAppResidenceType(appForm.getAppResidenceType());
		appFormData.setAppHighestQualification(appForm.getAppHighestQualification());
		appFormData.setAppRelationshipWithBank(appForm.getAppRelationshipWithBank());
		appFormData.setAppLoanDealerName(appForm.getAppLoanDealerName());
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
			MasterState state = this.commonService
					.getStateById((appForm.getAppPermanentStateId() != null) ? appForm.getAppPermanentStateId() : null);
			if (state != null && state.getStatePinMinStartPrefix() != null
					&& state.getStatePinMaxStartPrefix() != null) {
				if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue()
						&& pincodeInitial <= state.getStatePinMaxStartPrefix().intValue()
						&& !pinlastfix.equals("000")) {
					appFormData.setAppPermanentPincode(appForm.getAppPermanentPincode());
				} else {
					appFormData.setError(
							"Entered permanent address pincode does not belong to entered state.|appForm.appPermanentPincode");
					return appFormData;
				}
			} else {
				appFormData.setAppPermanentPincode(appForm.getAppPermanentPincode());
			}
		}
		appFormData.setAppImmovableProperty(appForm.getAppImmovableProperty());
		appFormData.setAppBankDeposit(appForm.getAppBankDeposit());
		appFormData.setAppNsc(appForm.getAppNsc());
		appFormData.setAppPFOrPPF(appForm.getAppPFOrPPF());
		appFormData.setAppGoldOrnaments(appForm.getAppGoldOrnaments());
		appFormData.setAppMutualAsset(appForm.getAppMutualAsset());
		appFormData.setAppExistingTotalLoanAmount(appForm.getAppExistingTotalLoanAmount());
		appFormData.setAppSalaryAccNo(appForm.getAppSalaryAccNo());
		if (appForm.getAppLoanEmployerName() != null)
			appFormData.setAppLoanEmployerName(appForm.getAppLoanEmployerName());
		if (appForm.getAppCompanyJoiningMonth() != null && appForm.getAppCompanyJoiningYear() != null) {
			String stdate = "";
			stdate = "01-" + ((appForm.getAppCompanyJoiningMonth().intValue() <= 12)
					? appForm.getAppCompanyJoiningMonth().intValue()
					: 12) + "-" + appForm.getAppCompanyJoiningYear();
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
		if (appForm.getAppOfficeDistrictId() != null)
			appFormData.setAppOfficeDistrictId(appForm.getAppOfficeDistrictId());
		if (appForm.getAppOfficePincode() != null) {
			int pincodeInitial = appForm.getAppOfficePincode().intValue() / 10000;
			String pinlastfix = appForm.getAppOfficePincode().toString().substring(3, 6);
			MasterState state = this.commonService
					.getStateById((appForm.getAppOfficeStateId() != null) ? appForm.getAppOfficeStateId() : null);
			if (state != null && state.getStatePinMinStartPrefix() != null
					&& state.getStatePinMaxStartPrefix() != null) {
				if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue()
						&& pincodeInitial <= state.getStatePinMaxStartPrefix().intValue()
						&& !pinlastfix.equals("000")) {
					appFormData.setAppOfficePincode(appForm.getAppOfficePincode());
				} else {
					appFormData.setError(
							"Entered office pincode does not belong to entered state.|appForm.appOfficePincode");
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
		appFormData.setCloneCoapplicantAddress1(appForm.isCloneCoapplicantAddress1());
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
			MasterState state = this.commonService.getStateById(
					(appForm.getAppCoapplicantState_id_1() != null) ? appForm.getAppCoapplicantState_id_1() : null);
			if (state != null && state.getStatePinMinStartPrefix() != null
					&& state.getStatePinMaxStartPrefix() != null) {
				if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue()
						&& pincodeInitial <= state.getStatePinMaxStartPrefix().intValue()
						&& !pinlastfix.equals("000")) {
					appFormData.setAppCoapplicantPincode_1(appForm.getAppCoapplicantPincode_1());
				} else {
					appFormData.setError(
							"Entered co-applicant pincode does not belong to entered state.|appForm.appCoapplicantPincode_1");
					return appFormData;
				}
			} else {
				appFormData.setAppCoapplicantPincode_1(appForm.getAppCoapplicantPincode_1());
			}
		}
		appFormData.setAppCoapplicantHighestQualification(appForm.getAppCoapplicantHighestQualification());
		appFormData.setAppCoapplicantRelationShipWithBank(appForm.getAppCoapplicantRelationShipWithBank());
		appFormData.setAppCoapplicantImmovableProperty(appForm.getAppCoapplicantImmovableProperty());
		appFormData.setAppCoapplicantBankDeposit(appForm.getAppCoapplicantBankDeposit());
		appFormData.setAppCoapplicantNsc(appForm.getAppCoapplicantNsc());
		appFormData.setAppCoapplicantPFOrPPF(appForm.getAppCoapplicantPFOrPPF());
		appFormData.setAppCoapplicantGoldOrnaments(appForm.getAppCoapplicantGoldOrnaments());
		appFormData.setAppCoapplicantMutualAsset(appForm.getAppCoapplicantMutualAsset());
		appFormData.setAppCoapplicantExistingTotalLoanAmount(appForm.getAppCoapplicantExistingTotalLoanAmount());
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
		if (appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue()) {
			appFormData.setAppPanCardLater(appForm.getAppPanCardLater());
			appFormData.setAppPanCardVerified("N");
		}
		appFormData.setAppOtherId(appForm.getAppOtherId());
		appFormData.setAppOtherIdNumber(appForm.getAppOtherIdNumber());
		if (appFormData.getAppFilledAt() == null) {
			appFormData.setAppFilledAt(new Date());
			if (appFormData.getAppAlertStatusType() != null)
				appFormData.setAppAlertStatusType(null);
		}
		if (!ValidatorUtil.isValid(appFormData.getAppBranchId())) {
			appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
			return appFormData;
		}

			if (isDsrPage.equalsIgnoreCase("false") && appForm.getAppAlternateMobileNumber() != null && !"".equals(appForm.getAppAlternateMobileNumber())
 				&& appForm.getAppAltMobileVerified() == null ||  (appForm.getAppAltMobileVerified() != null && appForm.getAppAltMobileVerified().equalsIgnoreCase("N"))) {
 			appFormData.setError("Please verify alternate Mobile Number.|");
 			return appFormData;
 		}
	
		if ((appForm.getAppPanCardLater() == null || !appForm.getAppPanCardLater().booleanValue())
				&& !ValidatorUtil.isValidPanNo(appForm.getAppPanCardNo())) {
			appFormData.setError("PAN is not in correct format.|2");
			return appFormData;
		}

//		logger.info("AutoProcessImpl.java Line 1859 autoLoanService file OTP method" + appFormData.getAppReferenceId());
		appFormData = this.autoLoanService.save(appFormData);
		logger.info("ALProcessImpl.java :: LNo :: 1884 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
		this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
				appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_DATA_SAVED, null, false);
		if (appForm.getAppPanCardNo() != null && !"".equals(appForm.getAppPanCardNo())) {
			if (!ValidatorUtil.validateIdProof(appForm.getAppPanCardNo(), Integer.valueOf(181))) {
				appFormData.setError("PAN is not in correct format.|2");
				return appFormData;
			}
		} else {
			appFormData.setError("Please enter valid PAN no.|2");
			return appFormData;
		}
		if (ValidatorUtil.isValid(appForm.getAppOtherId())) {
			boolean validateStatus = ValidatorUtil.validateIdProof(appForm.getAppOtherIdNumber(),
					appForm.getAppOtherId());
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
		int nsdlPANStatus = 0;
		if (ValidatorUtil.isValid(appFormData.getAppPanCardNo())) {
			PanApiInputParams apiInputParams = new PanApiInputParams();
			apiInputParams.setApiApplicantTypeId(Integer.valueOf(1));
			apiInputParams.setApiAppSeqId(appFormData.getAppSeqId());
			if (isDsrPage.equalsIgnoreCase("true")) {
				apiInputParams.setApiCallingSourceId(Integer.valueOf(2));
			} else {
				apiInputParams.setApiCallingSourceId(Integer.valueOf(1));
			}
			apiInputParams.setApiMethodName("getPanDetails");
			apiInputParams.setApiProductId(Constants.AUTO_LOAN_ID);
			logger.info("ALProcessImpl.java :: LNo :: 1920 appSeqId===" + appFormData.getAppSeqId()
					+ " calling pan service callPanServiceApi()");
			PanApiReturnResponse panApiResponseObj = this.panServiceAction.callPanServiceApi(apiInputParams);
			logger.info("ALProcessImpl.java :: LNo :: 1924 appSeqId===" + appFormData.getAppSeqId()
					+ "===response pan service status id===" + panApiResponseObj.getApiStatusId() + "===Api Error==="
					+ panApiResponseObj.getApiErrors());
			if (panApiResponseObj.getApiStatusId().intValue() == 1)
				nsdlPANStatus = 1;
		}
		this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
				appFormData.getAppLoanStatusId().intValue(),
				(nsdlPANStatus == 1) ? Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_SUCCESS
						: Constants.CALL_LOGS_MESSAGE_STATE_PAN_SERVICE_FAIL,
				null, false);
		logger.info("ALProcessImpl.java :: LNo :: 2694 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
		if (nsdlPANStatus == 1) {
			logger.info("AutoProcessManagerImpl.java :: LNo 2696:: pan is verified :: status  :: " + nsdlPANStatus
					+ " with AppSeqId ::" + appSeqId);
			appFormData.setAppPanCardVerified("Y");
			appFormData.setAppPanVerifiedDatetime(new Date());
		} else {
			logger.info("AutoProcessManagerImpl.java :: LNo 2702 pan is not  verified  :: status  :: " + nsdlPANStatus
					+ " with AppSeqId " + appSeqId);
		}
		Integer count = this.commonService
				.getCountByAppIdAndLoanType(appFormData.getAppSeqId().toString(), "A01", "SUCCESS");
		if (count != null && count <= 0)
			try {
				if (appForm != null) {
					boolean isContactCenterUser = false;
					if (isDsrPage.equalsIgnoreCase("true"))
						isContactCenterUser = this.commonService.getBankLmsUserRole(bankLMSUserId);
					logger.info("AutoProcessManagerImpl.java LNo: 1896 :: Calling :: isCibilBypass :: "
							+ isContactCenterUser + " with AppSeqId " + appSeqId);
					Integer cibil = cibilCall(appFormData);
			        appFormData.setAppCibilScore(cibil);
					logger.info("AutoProcessManagerImpl.java LNo: 1971 :: Calling :: appFormData.getAppCibilScore() :: "
							+ appFormData.getAppCibilScore() + " with AppSeqId ::" + appSeqId);
				}
			} catch (NullPointerException eob) {
				logger.info("AutoLoanProcessImpl.java LNo: 2475 :: cIBIL Calling ", eob);
			} catch (SQLException eob) {
				logger.info("AutoLoanProcessImpl.java LNo: 2477 :: cIBIL Calling ", eob);
			}
		if ((!appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)
				&& appForm.getAppCallRSMService() == null) || isDsrPage.equalsIgnoreCase("true"))
			appForm.setAppCallRSMService(Boolean.valueOf(true));
		logger.info("AutoProcessManagerImpl.java LNo: 1948 :: Calling getLoanQuoteOccupationCategory :: "
				+ quote.getLoanQuoteOccupationCategory());
		if (quote.getLoanQuoteOccupationCategory() == null) {
			if (quote.getLoanQuoteOccupationTypeId() != null) {
				List<MasterEmployeeOccupationType> occupationObjList = this.commonService
						.getOccupationCategoryByOccupationType(quote.getLoanQuoteOccupationTypeId());
				if (occupationObjList != null && occupationObjList.size() > 0) {
					MasterEmployeeOccupationType occupationObj = occupationObjList.get(0);
					if (occupationObj != null && occupationObj.getCategory() != null) {
						quote.setLoanQuoteOccupationCategory(occupationObj.getCategory());
						logger.info("ALProcessImpl.java :: LNo :: 1960 appFormData.getAppLoanStatusId() :: "
								+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
						this.autoLoanService.save(quote);
						logger.info("ALProcessImpl.java :: LNo :: 1962 appFormData.getAppLoanStatusId() :: "
								+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
					}
				}
			}
			logger.info(
					"AutoProcessManagerImpl.java LNo: 1960 :: Calling  getLoanQuoteOccupationCategory after calculation:: "
							+ quote.getLoanQuoteOccupationCategory());
		}
		logger.info(
				"AutoProcessManagerImpl.java LNo: 1962 :: Calling  getLoanQuoteOccupationCategory without calculation:: "
						+ quote.getLoanQuoteOccupationCategory());
		if (appForm.getAppCallRSMService() != null && appForm.getAppCallRSMService().booleanValue()) {
			try {
				JSONObject quoteData = JSONUtil.beanObjectToJSONObjct(quote);
				quoteData = this.SbiUtil.getDBCredentialForHelper(quoteData);
				JSONObject appData = JSONUtil.beanObjectToJSONObjct(appFormData);
				appData.put("appOfferJsonData", "");
				appData.put("appRSMResponse", "");
				if (appForm.getAppHaveAadhaarNumber() != null && appForm.getAppHaveAadhaarNumber().intValue() == 1)
					appData.put("appAadhaarNumber", appForm.getAppHaveAadhaarNumber());
				String engineRequest = null;
				if (Constants.IS_ENGINE_OBF) {
					JSONObject finalQuoteData = MapperUtil.convertAutoLoan(quoteData);
					JSONObject finalAppData = MapperUtil.convertAutoLoanApplication(appData);
					engineRequest = "rsmRequest={\"engineRequest\":" + finalQuoteData.toString()
							+ ",\"applicationFormRequest\":" + finalAppData.toString() + "}";
				} else {
					engineRequest = "rsmRequest={\"engineRequest\":" + quoteData.toString()
							+ ",\"applicationFormRequest\":" + appData.toString() + "}";
				}
				JSONObject appRSMResponse = this.commonEngine.callingRSMEngine(engineRequest, Constants.AUTO_LOAN_ID);
				RSMResponse rsmBean = new RSMResponse();
				rsmBean = (RSMResponse) JSONUtil.getObjctFromJSON(rsmBean, appRSMResponse.toString());
				if (rsmBean != null) {
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

					}
				} else {
					appFormData.setAppRSMdecision(Integer.valueOf(2));
				}
				this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
						appFormData.getAppLoanStatusId().intValue(),
						(rsmBean == null) ? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON
								: ((rsmBean.getRsmDecision() == 0 || rsmBean.getRsmDecision() == 2)
										? Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_19
										: Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_147),
						null, false);
			} catch (NullPointerException e) {
				appFormData.setAppRSMdecision(Integer.valueOf(2));
				this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
						appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON,
						null, false);
			} catch (JSONException e) {
				appFormData.setAppRSMdecision(Integer.valueOf(2));
				this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
						appFormData.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE_RSM_SERVICE_NON,
						null, false);
			}
		} else {
			appFormData.setAppRSMdecision(Integer.valueOf(2));
		}
		logger.info("ALProcessImpl.java :: LNo :: 1958 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
		if (SessionUtil.getApplicationType() != null
				|| appFormData.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
			StatusRequest statusRequest = new StatusRequest();
			statusRequest.setCurrentStatus(appFormData.getAppLoanStatusId().intValue());
			statusRequest.setHaveLoanOffer(true);
			statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
			statusRequest.setState(17);
			statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
			statusRequest.setRsmDecision(appFormData.getAppRSMdecision().intValue());
			statusRequest.setNsdlPANStatus(1);
			statusRequest.setAppPanCardNo(appFormData.getAppPanCardNo());
			statusRequest.setApplicationType(
					(SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
			statusRequest.setApplicationLeadType(appFormData.getAppDataSourceId().intValue());
			StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
			if (statusManagerResponse.getStatus() != 0) {
				appFormData.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
			} else if (appFormData.getAppLoanStatusId().intValue() == 0) {
				appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
				return appFormData;
			}
			if (statusManagerResponse.isEligibleToInsertLog() && statusManagerResponse.getStatusCallLogs() > 0)
				this.autoLoanHelper.insertCallLog(appFormData.getAppSeqId(), bankLMSUserId.intValue(),
						statusManagerResponse.getStatusCallLogs(), null, null, true);
			if (statusManagerResponse.isInitiateCallAttempt())
				appFormData.setAppTotalCallAttempt(Integer.valueOf(0));
			logger.info("ALProcessImpl.java :: LNo :: 2847  appFormData.getAppLoanStatusId() :: "
					+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
			if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() != 0)
				if (SessionUtil.getApplicationType().intValue() == 1) {
					if (statusManagerResponse.getStatusLead() != 0 && SessionUtil.getLeadId() != null) {
						ApplicationFormLead lead = this.commonService.getLeadById(SessionUtil.getLeadId());
						if (lead != null) {
							if (lead.getLeadAlertStatusType() != null)
								lead.setLeadAlertStatusType(null);
							lead.setLeadLoanStatusId(Integer.valueOf(statusManagerResponse.getStatusLead()));
							this.commonService.save(lead);
						}
					}
					logger.info("ALProcessImpl.java :: LNo :: 2866 appFormData.getAppLoanStatusId() :: "
							+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
				} else if (SessionUtil.getApplicationType().intValue() == 2) {
					logger.info("ALProcessImpl.java :: LNo :: 2868 appFormData.getAppLoanStatusId() :: "
							+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
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
					this.lastReferenceNumber = this.autoLoanService
							.getLastGeneratedReferenceNumber(Constants.AUTO_LOAN_ID);
					this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_AL,
							Constants.SOURCE_STRING_AL, this.lastReferenceNumber);
					boolean isReferenceIdAvailable = false;
					isReferenceIdAvailable = this.autoLoanService.isReferenceIdAvailable(this.appRefKey);
					logger.info("AL isReferenceIdAvailable 1 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey
							+ " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
					if (!isReferenceIdAvailable) {
						appFormData.setAppReferenceId(this.appRefKey);
					} else {
						this.lastReferenceNumber = this.appRefKey;
						this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_AL,
								Constants.SOURCE_STRING_AL, this.lastReferenceNumber);
						isReferenceIdAvailable = this.autoLoanService.isReferenceIdAvailable(this.appRefKey);
						logger.info(
								"AL isReferenceIdAvailable 2 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey
										+ " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
						if (!isReferenceIdAvailable) {
							appFormData.setAppReferenceId(this.appRefKey);
						} else {
							this.lastReferenceNumber = this.appRefKey;
							this.appRefKey = this.SbiUtil.getApplicationReferenceId(Constants.INTIAL_STRING_AL,
									Constants.SOURCE_STRING_AL, this.lastReferenceNumber);
							isReferenceIdAvailable = this.autoLoanService.isReferenceIdAvailable(this.appRefKey);
							logger.info("AL isReferenceIdAvailable 3 " + isReferenceIdAvailable + " appRefKey "
									+ this.appRefKey + " appSeqId " + appSeqId + " lastReferenceNumber "
									+ this.lastReferenceNumber);
							appFormData.setAppReferenceId(this.appRefKey);
						}
					}
					logger.info("AL isReferenceIdAvailable 4 " + isReferenceIdAvailable + " appRefKey " + this.appRefKey
							+ " appSeqId " + appSeqId + " lastReferenceNumber " + this.lastReferenceNumber);
					logger.info("ALProcessImpl.java :: LNo :: 2107 appFormData.getAppLoanStatusId() :: "
							+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
					appFormData = this.autoLoanService.save(appFormData);
					logger.info("ALProcessImpl.java :: LNo ::  2110 appFormData.getAppLoanStatusId() :: "
							+ appFormData.getAppLoanStatusId() + " with AppSeqId " + appSeqId);
				}
			logger.info("ALProcessImpl.java :: LNo :: 2080 appFormData.getAppLoanStatusId() :: "
					+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
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
							if (ValidatorUtil.isValidEmail(appFormData.getAppWorkEmail())
									&& ValidatorUtil.isValid(appFormData.getAppReferenceId()))
								this.taskExecutorService.sendingEmailForAutoLoan(requestIndex, Integer.valueOf(1),
										appFormData);
							if (ValidatorUtil.isValid(appFormData.getAppReferenceId()))
								this.taskExecutorService.sendingSMSForAutoLoan(requestIndex, Integer.valueOf(1),
										appFormData, requestIndex, isAbleToSendBMOrSalesTeamMessage);
						}
						this.taskExecutorService.generatePDFForAutoLoan(appFormData, quote);
					} catch (TaskRejectedException e) {
						logger.info("ALProcessImpl.java LNo : 2273 : Exception caught in while generating PDF ", e);
					}
				try {
					logger.info("AutoLoanProcessImpl.java LNo: 2928  calling CRM  with AppSeqId ::" + appSeqId);
					if (appFormData != null && quote != null && !Constants.CRM_BYPASS)
						if (appFormData.getAppCRMLeadId() != null && isDsrPage.equalsIgnoreCase("true")) {
							CRMRequest crmRequest = new CRMRequest();
							crmRequest.setCrmLeadId(appFormData.getAppCRMLeadId());
							crmRequest.setApplicantReferenceId(appFormData.getAppReferenceId());
							crmRequest.setReferenceNumber(appFormData.getAppSeqId());
							crmRequest.setLoanTypeId(Constants.AUTO_LOAN_ID);
						} else {
							callCrm(appFormData, quote);
						}
				} catch (NullPointerException e) {
					logger.info("ALProcessImpl.java LNo : 2945 error while calling CRM ", e);
				} 
			} else {
				logger.info(
						"AutoLoanProcessImpl.java LNo: 2939 reference number is not able to generate  with AppSeqId ::"
								+ appSeqId);
				if (appFormData != null) {
					appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
					return appFormData;
				}
			}
			logger.info("ALProcessImpl.java :: LNo :: 29445 appFormData.getAppLoanStatusId() :: "
					+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
		} catch (NullPointerException e) {
			logger.info("AutoLoanProcessImpl.java LNo: 2947 :: exception caught ", e);
			if (appFormData == null)
				appFormData = new ApplicationFormAutoLoan();
			appFormData.setError(Constants.SORRY_FOR_INCONVENIENCE);
			return appFormData;
		} 
		logger.info("ALProcessImpl.java :: LNo :: 2967 appFormData.getAppLoanStatusId() :: "
				+ appFormData.getAppLoanStatusId() + " with AppSeqId ::" + appSeqId);
		return appFormData;
	}

	public CBSCallResponse processCbsCall(Integer appSeqId, Integer requestIndex, MasterCBSCall masterCbsCall,
			String isDsrPage, Integer bankLMSUserId, Integer visitId, Integer cbsCallId, Integer socialMediaId,
			Integer deviceId) throws SQLException, JSONException, RuntimeException, ParseException {
		CBSCallResponse cbsCallResponse = new CBSCallResponse();
		masterCbsCall.setCbsLoanTypeId(Constants.AUTO_LOAN_ID);
		int count = this.commonService.getCBSApplicationCount(masterCbsCall.getCbsIsdCode(),
				masterCbsCall.getCbsMobileNumber(), Constants.AUTO_LOAN_ID).intValue();
		logger.info("AutoProcessManagerImpl.java LNo : 3005 count : " + count + " with AppSeqId ::" + appSeqId);
		if (count > Constants.APP_MAXIMUM_OTP_REQUEST_COUNT.intValue()) {
			cbsCallResponse.setResponseMsg(Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
			cbsCallResponse.setStatus(Integer.valueOf(0));
			return cbsCallResponse;
		}
		cbsCallResponse.setStatus(Integer.valueOf(0));
		try {
			boolean isAppFoundForDedupInDropOffStage = false;
			boolean isAppFoundForDedupInDropRejectStage = false;
			if (!Constants.CBS_DEDUPE_BYPASS) {
				String oldMobile = "";
				String isdCode = "";
				boolean isEligibleForBypass = false;
				if (appSeqId == null && masterCbsCall.getCbsMobileNumber() != null) {
					isdCode = (masterCbsCall.getCbsIsdCode() == null) ? Constants.COUNTRY_CODE_INDIA
							: masterCbsCall.getCbsIsdCode().toString();
					oldMobile = masterCbsCall.getCbsMobileNumber();
					isEligibleForBypass = true;
				}
				if (isEligibleForBypass && !Constants.DUMMY_MOBILE_NO.contains(oldMobile)
						&& !Constants.APP_DUPLICATION_CHECK.equals("0")) {
					boolean isAppFoundForDedupInApplicationStage = false;
					isAppFoundForDedupInApplicationStage = this.autoLoanService
							.isAppFoundForDedupInApplicationStage(null, isdCode, oldMobile, null);
					logger.info("AutoProcessImpl.java :: LNo 3036:: isAppFoundForDedupInApplicationStage "
							+ isAppFoundForDedupInApplicationStage + " with AppSeqId ::" + appSeqId);
					if (isAppFoundForDedupInApplicationStage) {
						cbsCallResponse.setResponseMsg(Constants.APP_DEDUPLICATION_MESSAGE);
						return cbsCallResponse;
					}
					isAppFoundForDedupInDropOffStage = this.autoLoanService.isAppFoundForDedupInDropOffStage(isdCode,
							oldMobile, null);
					logger.info("AutoProcessImpl.java :: LNo 3042:: isAppFoundForDedupInDropOffStage "
							+ isAppFoundForDedupInDropOffStage + " with AppSeqId ::" + appSeqId);
					isAppFoundForDedupInDropRejectStage = this.autoLoanService
							.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, null);
					logger.info("AutoProcessImpl.java :: LNo 3044:: isAppFoundForDedupInDropRejectStage "
							+ isAppFoundForDedupInDropRejectStage + " with AppSeqId ::" + appSeqId);
				}
			}
			masterCbsCall.setCbsVisitId(visitId);
			if (cbsCallId != null) {
				MasterCBSCall oldMasterCbsCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
				oldMasterCbsCall.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
				oldMasterCbsCall.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
				oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCode());
				oldMasterCbsCall.setCbsTypeOfRelationship(masterCbsCall.getCbsTypeOfRelationship());
				oldMasterCbsCall = this.commonService.save(oldMasterCbsCall);
				if (oldMasterCbsCall == null) {
					logger.info("AutoProccessManagerImpl LNO:: 3059");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			} else {
				masterCbsCall.setCbsOtpVerified(Integer.valueOf(0));
				masterCbsCall.setCbsRequiestedTime(new Date());
				masterCbsCall = this.commonService.save(masterCbsCall);
				if (masterCbsCall == null) {
					logger.info("AutoProccessManagerImpl LNO:: 3069");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
				SessionUtil.setAutoLoanCbsCallId(masterCbsCall.getCbsId());
			}
			cbsCallResponse.setCbsCallId(masterCbsCall.getCbsId());
			cbsCallResponse.setVisitId(visitId);
			logger.info("autoProcessManagerImpl LNO :: 3078=================" + SessionUtil.getAutoLoanCbsCallId()
					+ " with AppSeqId ::" + appSeqId);
			MasterCBSResponse masterCBSResponse = null;
			if (masterCbsCall.getCbsResponseId() == null) {
				masterCBSResponse = new MasterCBSResponse();
			} else {
				masterCBSResponse = this.commonService.getMasterCBSResponseById(masterCbsCall.getCbsResponseId());
				if (masterCBSResponse == null) {
					logger.info("autoProcessManagerImpl LNO :: 3048");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			}
			logger.info("Before Caaling callingCBSEngineForCIFLevelInformation");
			JSONObject cbsEngineResponseJson = this.cbsUtil.callingCBSEngineForCIFLevelInformation(
					masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(),
					Constants.AUTO_LOAN_ID.intValue());
			CBSCustomerInformation cbsCustomerInformation = new CBSCustomerInformation();
			this.cbsUtil.setCBSCustomerInformationBean(cbsCustomerInformation, cbsEngineResponseJson);
			if (cbsCustomerInformation.getStatus() != null && cbsCustomerInformation.getStatus().equals("0")) {
				logger.info("Auto Loan LNO :: 2978 with AppSeqId ::" + appSeqId);
				if (cbsCustomerInformation.getErrorReason() != null) {
					if (cbsCustomerInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
						cbsCallResponse
								.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
					} else {
						cbsCallResponse.setResponseMsg(cbsCustomerInformation.getErrorReason().trim());
					}
				} else {
					cbsCallResponse.setResponseMsg(
							"Sorry, the system has encountered a technical error at the moment. Please try again after few minutes  1.");
				}
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			//logger.info("CBS Mobile No : " + cbsCustomerInformation.getMOBILENO() + " Input Mobile No :  "
			//		+ masterCbsCall.getCbsMobileNumber() + " For appSeqId :  " + appSeqId);
			if (!masterCbsCall.getCbsMobileNumber().equals(cbsCustomerInformation.getMOBILENO())) {
				cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			JSONObject accountInfoResponseJson = this.cbsUtil.callingCBSEngineForAccountLevelInformation(
					masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(),
					Constants.AUTO_LOAN_ID.intValue());
			if (accountInfoResponseJson.has("ERROR_CODE") && accountInfoResponseJson.get("ERROR_CODE") != null
					&& accountInfoResponseJson.get("ERROR_CODE").toString().trim().length() > 0) {
				logger.info("Auto Loan  LNO :: 2423");
				if (accountInfoResponseJson.get("ERROR_DESCRIPTION") != null) {
					if (accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim()
							.equalsIgnoreCase("INVALID CHECK DIGIT")) {
						cbsCallResponse
								.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
					} else {
						cbsCallResponse
								.setResponseMsg(accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim());
					}
				} else {
					cbsCallResponse.setResponseMsg(
							"Sorry, the system has encountered a technical error at the moment. Please try again after few minutes 2 .");
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
			//logger.info("Response from Customer Enquiry and Account info Data Account no : "
			//		+ cbsCustomerInformation.getACCOUNTNUMBER() + " Product Id : "
			//		+ cbsCustomerInformation.getProductId());
			cbsCustomerInformation.setAccountDesc(accountInfoResponseJson.getString("AccountDescription"));
			boolean needToByPassLoanAccountInformation = true;
			if (masterCbsCall.getCbsTypeOfRelationship() != null) {
				Integer typeOfRelationship = masterCbsCall.getCbsTypeOfRelationship();
				if (typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3")
						|| typeOfRelationship.toString().equals("4"))
					needToByPassLoanAccountInformation = false;
			}
			if (ValidatorUtil.isValid(cbsCustomerInformation.getProductId())) {
				if (needToByPassLoanAccountInformation) {
					boolean productFound = this.commonService.isCbsMappingsExistByProductId(Constants.HOME_LOAN_ID,
							cbsCustomerInformation.getProductId());
					if (!productFound) {
						logger.info(
								"AutoProcessManagerImpl.java LNo: 3131 : Product id not match with mapping table with AppSeqId ::"
										+ appSeqId);
						cbsCallResponse.setResponseMsg(
								"Provided account No. does not pertain to selected type of relationship");
						cbsCallResponse.setStatus(Integer.valueOf(0));
						return cbsCallResponse;
					}
				}
			} else {
				logger.info(
						"AutoProcessManagerImpl.java LNo: 3139 : Product id not received  in Customer information service with AppSeqId ::"
								+ appSeqId);
				cbsCallResponse.setResponseMsg("Product id not received  in Customer information service");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			try {
				masterCbsCall.setCbsResponseTime(new Date());
				masterCBSResponse.setCbsLoanTypeId(Constants.AUTO_LOAN_ID);
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
					masterCBSResponse.setCbsDateOfBirth(
							DateUtil.convertStringToDateWithOutDelimiter(cbsCustomerInformation.getDATEOFBIRTH()));
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
				if (ValidatorUtil.isValid(cbsCustomerInformation.getPhoneBusiness())
						&& StringUtils.isNumeric(cbsCustomerInformation.getPhoneBusiness()))
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
					logger.info("AutoLoanProcessImpl.java LNo: 3247 ::");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			} catch (NullPointerException e) {
				logger.info("AutoLoanProcessImpl.java LNo: 3253 :: exception caught ", e);
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			CBSLoanAccountInformation cbsLoanAccountInformation = new CBSLoanAccountInformation();
			if (needToByPassLoanAccountInformation) {
				JSONObject loanAccountInformationcbsResponseJson = this.cbsUtil
						.callingCBSEngineForLoanAccountInformation(masterCbsCall.getCbsAccountNumber(),
								masterCbsCall.getCbsMobileNumber(), Constants.CBS_LOAN_TYPE_AUTO_LOAN);
				this.cbsUtil.setCbsLoanAccountInformation(cbsLoanAccountInformation,
						loanAccountInformationcbsResponseJson);
				if (cbsLoanAccountInformation.getStatus() != null
						&& cbsLoanAccountInformation.getStatus().equals("0")) {
					logger.info("Auto Loan  :: 2571 with AppSeqId " + appSeqId);
					if (cbsLoanAccountInformation.getErrorReason() != null) {
						if (cbsLoanAccountInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")) {
							cbsCallResponse
									.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
						} else {
							cbsCallResponse.setResponseMsg(cbsLoanAccountInformation.getErrorReason().trim());
						}
					} else {
						cbsCallResponse.setResponseMsg(
								"Sorry, the system has encountered a technical error at the moment. Please try again after few minutes 3.");
					}
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
				cbsLoanAccountInformation.setBranchCode(cbsCustomerInformation.getBranchCode());
				cbsLoanAccountInformation
						.setReferenceNumber(cbsEngineResponseJson.getString("REQUEST_REFERENCE_NUMBER"));
			}
			masterCbsCall.setCbsResponseId(masterCBSResponse.getCbsResponseId());
			logger.info("CBS_RES_ID :: " + masterCBSResponse.getCbsResponseId() + " with AppSeqId ::" + appSeqId);
			if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmail())) {

				masterCbsCall.setCbsEmailMask(masterCBSResponse.getCbsEmail());
				//logger.info(" :"+masterCbsCall.getCbsEmailMask());
				masterCbsCall.setCbsEmail(masterCBSResponse.getCbsEmail());

				masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(1));
			} else {
				masterCbsCall.setCbsIsEligibleForEmailOtp(Integer.valueOf(0));
			}
			masterCbsCall = this.commonService.save(masterCbsCall);
			if (masterCbsCall == null) {

				logger.info("AutoLoanProcessImpl.java LNo: 3295 ::");
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
					masterCBSResponse.setCbsSanctionedLimit(
							Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getSanctionedLimit())));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getSanctiondate()))
					masterCBSResponse.setCbsSanctionDate(
							DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getSanctiondate()));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getLoanTenure()))
					masterCBSResponse.setCbsLoanTenure(
							Integer.valueOf(Integer.parseInt(cbsLoanAccountInformation.getLoanTenure())));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getRepaymentstartdate()))
					masterCBSResponse.setCbsRepaymentStartDate(DateUtil
							.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getRepaymentstartdate()));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getValueofPrimarySecurity()))
					masterCBSResponse.setCbsValueOfPrimarySecurity(
							Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getValueofPrimarySecurity())));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getIRACStatus()))
					masterCBSResponse.setCbsIracStatus(cbsLoanAccountInformation.getIRACStatus());
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getEMI()))
					masterCBSResponse.setCbsEmi(Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getEMI())));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getOutstanding()))
					masterCBSResponse.setCbsOutstanding(
							Double.valueOf(Double.parseDouble(cbsLoanAccountInformation.getOutstanding())));
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getInstituteName()))
					masterCBSResponse.setCbsInstituteName(cbsLoanAccountInformation.getInstituteName());
				if (ValidatorUtil.isValid(cbsLoanAccountInformation.getBranchCode()))
					masterCBSResponse.setCbsBranchCode(cbsLoanAccountInformation.getBranchCode());
				masterCBSResponse = this.commonService.save(masterCBSResponse);
				if (masterCBSResponse == null) {
					logger.info("AutoLoanProcessImpl.java LNo: 3340");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			} catch (ParseException ex) {
				logger.info("AutoLoanProcessImpl.java LNo: 3346", ex);
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			} 
			if (needToByPassLoanAccountInformation
					&& Integer.parseInt(masterCBSResponse.getCbsIracStatus()) >= Constants.CBS_IRAC_STATUS.intValue()) {
				logger.info(
						"AutoLoanProcessImpl.java LNo: 3358 :: CBS_IRAC_STATUS greter than equal to 4 with AppSeqId ::"
								+ appSeqId);
				cbsCallResponse.setResponseMsg(
						"Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			boolean needToInsert = true;
			ApplicationFormAutoLoan app = null;
			ApplicationFormAutoLoanQuote quote = null;
			if (masterCBSResponse.getCbsAppSeqId() == null) {
				app = new ApplicationFormAutoLoan();
				quote = new ApplicationFormAutoLoanQuote();
			} else {
				app = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
				if (app == null) {
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
				needToInsert = false;
				if (app.getAppQuoteId() != null) {
					quote = this.autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(app.getAppQuoteId());
					if (quote == null) {
						cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
						cbsCallResponse.setStatus(Integer.valueOf(0));
						return cbsCallResponse;
					}
				} else {
					quote = new ApplicationFormAutoLoanQuote();
				}
			}
			try {
				quote.setLoanQuoteActive("Y");
				quote.setLoanQuoteDeleted("N");
				quote.setLoanQuoteCreatedLmsUserId(Constants.OTHER_USER_ID);
				quote.setLoanQuoteEntryTime(new Date());
				quote.setLoanQuoteUpdatedTime(new Date());
				quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
				quote.setLoanQuoteVisitId(visitId);
				if (ValidatorUtil.isValid(masterCbsCall.getCbsTypeOfRelationship()))
					quote.setCbsRelationShipId(masterCbsCall.getCbsTypeOfRelationship());
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsOutstanding()))
					quote.setLoanQuoteOutstandingLoanAmount(masterCBSResponse.getCbsOutstanding());
				if (ValidatorUtil.isValid(
						DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
					quote.setLoanQuoteDateOfBirth(DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(),
							Constants.DATE_FORMAT));
				if (ValidatorUtil.isValid(masterCbsCall.getCbsVisitId()))
					quote.setLoanQuoteVisitId(masterCbsCall.getCbsVisitId());
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsGender()))
					quote.setLoanQuoteGender(masterCBSResponse.getCbsGender());
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsEmi()))
					quote.setLoanQuotePreEMIs(masterCBSResponse.getCbsEmi());
				if (ValidatorUtil.isValid(
						DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
					quote.setLoanQuoteDateOfBirthDT(masterCBSResponse.getCbsDateOfBirth());
				if (masterCBSResponse.getCbsRepaymentStartDate() != null) {
					String date = DateUtil.convertDateToString(masterCBSResponse.getCbsRepaymentStartDate());
					String stdate = String.valueOf(date.substring(0, 2)) + "-" + date.substring(2, 4) + "-"
							+ date.substring(4);
					quote.setLoanQuoteRepaymentStartDate(DateUtil.convertStringToDate(stdate));
				}
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsOutstanding()))
					quote.setLoanQuoteoutstandingHomeLoanAmount(masterCBSResponse.getCbsOutstanding());
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsValueOfPrimarySecurity()))
					quote.setLoanQuotePresentValueOfProperty(masterCBSResponse.getCbsValueOfPrimarySecurity());
				quote.setLoanQuoteLoanPurposeId(Integer.valueOf(6));
				
		        //save consentId
		        Consent consent = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB");
				Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
				quote.setLoanQuoteConsentId(consentId); 
				
				logger.info("ALProcessImpl.java :: LNo :: 2650 appFormData.getAppLoanStatusId() :: ");
				quote = this.autoLoanService.save(quote);
				logger.info("ALProcessImpl.java :: LNo :: 2652 appFormData.getAppLoanStatusId() :: ");
				if (quote == null) {
					logger.info("AutoLoanProcessImpl.java LNo: 3436 ::");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			} catch (ParseException ex) {
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
				if (ValidatorUtil.isValid(
						DateUtil.getStringDateFromDate(masterCBSResponse.getCbsDateOfBirth(), Constants.DATE_FORMAT)))
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
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsPinCode())
						&& masterCBSResponse.getCbsPinCode().length() == 6)
					app.setAppPincode(Integer.valueOf(Integer.parseInt(masterCBSResponse.getCbsPinCode())));
				if (ValidatorUtil.isValid(masterCBSResponse.getCbsPhoneBusiness())
						&& StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()))
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
					} else if (SessionUtil.getBankLMSUser() != null
							&& SessionUtil.getBankLMSUser().getLmsUserId() != null) {
						bankLMSUserId = SessionUtil.getBankLMSUser().getLmsUserId();
						if (this.commonService.isBankUser(bankLMSUserId))
							app.setAppAssignedLmsSalesUserId(bankLMSUserId);
					}
				if (ValidatorUtil.isValid(masterCbsCall.getCbsTypeOfRelationship()))
					app.setAppCbsRelationShipId(masterCbsCall.getCbsTypeOfRelationship());
				logger.info("ALProcessImpl.java :: LNo :: 2806 appFormData.getAppLoanStatusId() :: ");
				
		        //save consent id in main table
		        if (quote.getLoanQuoteConsentId() != null && app.getAppConsentId() == null) {
			    	app.setAppConsentId(quote.getLoanQuoteConsentId());
			    }
		        
				app = this.autoLoanService.save(app);
				logger.info("ALProcessImpl.java :: LNo :: 2809 appFormData.getAppLoanStatusId() :: ");
				if (app == null) {
					logger.info("AutoLoanProcessImpl.java LNo: 3586 ::");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
				SessionUtil.setAutoLoanApplicationSequenceId(app.getAppSeqId());
			} catch (NullPointerException e) {
				logger.info("AutoLoanProcessImpl.java LNo: 3593 :: exception caught ", e);
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			} 
			cbsCallResponse.setAppSeqId(app.getAppSeqId());
			masterCBSResponse.setCbsAppSeqId(app.getAppSeqId());
			masterCBSResponse = this.commonService.save(masterCBSResponse);
			if (masterCBSResponse == null) {
				logger.info("AutoLoanProcessImpl.java LNo: 3602 :: ");
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(Integer.valueOf(0));
				return cbsCallResponse;
			}
			if (needToInsert)
				try {
					this.autoLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(),
							app.getAppLoanStatusId().intValue(), Constants.CALL_LOGS_MESSAGE_STATE167, null, true);
				} catch (NullPointerException e) {
					logger.info("AutoLoanProcessImpl.java LNo: 3612 :: exception caught ", e);
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(Integer.valueOf(0));
					return cbsCallResponse;
				}
			if (ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
				masterCbsCall
						.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSResponse.getCbsEmail()));
			}
//			logger.info("AutoLoanProcessImpl.java LNo: 3633 ::  Mobile number " + masterCbsCall.getCbsMobileNumber()
	//				+ "  mobile otp " + app.getAppMobileVerificationCode() + " with AppSeqId ::" + appSeqId);
			masterCbsCall.setCbsOtpCode(app.getAppMobileVerificationCode().toString());
			String msgBody = null;
			if (isDsrPage.equalsIgnoreCase("true")) {
				msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
						Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
			} else {
				msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
						Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
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
			SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
					(masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim());
			
			if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
				logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCode() + masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode().toString());
			}
			
			if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
				cbsCallResponse.setStatus(Integer.valueOf(0));
				cbsCallResponse.setResponseMsg("Mobile OTP service is down");
				return cbsCallResponse;
			}
			if (masterCbsCall.getCbsIsdCode() != null
					&& masterCbsCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA)
					&& ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())) {
				msgBody = String.valueOf(Constants.FIRST_EMAIL_PART)
						+ StringEscapeUtils.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
								Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0)))
						+ Constants.THIRD_EMAIL_PART;
				if (msgBody != null) {
					msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.PROTOCOL)
							+ Constants.IP_URL_INTERNET + Constants.BANK_IMAGE_FOLDER + "/");
					msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
					msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
					msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
					msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
					msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.AUTO_LOAN_PRODUCT_NAME);
					boolean sendStatus = false;
					String[] emailId = { masterCBSResponse.getCbsEmail() };
					sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
					logger.info("CBS EMAIL msgbody auto: " + msgBody + " with AppSeqId ::" + appSeqId);
					if (!sendStatus) {
						logger.info("AutoProcessManagerImpl LNO ::3710 with AppSeqId ::" + appSeqId);
						cbsCallResponse.setResponseMsg("Email OTP service is down");
						cbsCallResponse.setStatus(Integer.valueOf(0));
						return cbsCallResponse;
					}
				}
			}
			masterCbsCall.setCbsOtpCount(String.valueOf(1));
			masterCbsCall = this.commonService.save(masterCbsCall);
			if (masterCbsCall == null) {
				logger.info("AutoProcessManagerImpl LNO ::3720");
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(Integer.valueOf(0));
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
		} catch (NullPointerException e) {
			cbsCallResponse.setResponseMsg("System error occured.");
			cbsCallResponse.setStatus(Integer.valueOf(0));
			logger.info("AutoLoanProcessImpl.java LNo: 3734 :: exception caught ", e);
		} 
		return cbsCallResponse;
	}

	public JSONObject processCBSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl,
			int appOTPVerifyType, String inputOtp, String userEmail, Integer appSeqId, Integer cbsCallId) throws JSONException, ParseException {
		
		  if(inputOtp !=null) {
	      	SbiUtil sbiutil=new SbiUtil();
	      	logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
	      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
			//logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
	      }
	    
	    //  logger.info("DecryptedRequest inputOtp   1023  "+inputOtp);
		JSONObject json = new JSONObject();
		if (cbsCallId == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3744 ");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
			json.put("alertCount", 0);
			return json;
		}
		MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(cbsCallId);
		if (masterCBSCall == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3754");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (masterCBSCall.getCbsMobileOtpVerified() != null
				&& "Y".equalsIgnoreCase(masterCBSCall.getCbsMobileOtpVerified())) {
			json.put("status", "error");
			json.put("message", "Your mobile no. is already verified");
			json.put("alertCount", 5);
			return json;
		}
		Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
		if (stateId.intValue() == 28)
			try {
				if (appSeqId != null) {
					ApplicationFormAutoLoan app = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
					if (app == null) {
						logger.info("AutoProcessManagerImpl.java LNO 4702:: after save error");
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
					logger.info("AutoProcessManagerImpl LNO :: 3804 with AppSeqId ::" + appSeqId);
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
					json.put("alertCount", alertCount);
					return json;
				}
				if (appOTPVerifyType == 0) {
					masterCBSCall.setCbsOtpCode(
							this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
				} else if (appOTPVerifyType == 1) {
					masterCBSCall
							.setCbsEmailOtpCode(this.SbiUtil.getVerificationCodeForEmail(masterCBSCall.getCbsEmail()));
				} else {
					logger.info("AutoProcessManagerImpl LNO :: 3827 with AppSeqId ::" + appSeqId);
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCount", alertCount);
					return json;
				}
				String msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
						Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
				msgBody = this.SbiUtil.urlEncode(msgBody);
				String SMS_TEXT = null;
				if (masterCBSCall.getCbsIsdCode() == null
						|| Constants.COUNTRY_CODE_INDIA.equals(String.valueOf(masterCBSCall.getCbsIsdCode()))) {
					SMS_TEXT=Constants.SMS_STRING_INDIAN;
				} else {
					SMS_TEXT=Constants.SMS_STRING_NRI;
				}
				SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
				SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode());
				SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
						(masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim());
				
				if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
					logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode().toString());
				}
				
				if (!this.communicationManagerImpl.sendSms(SMS_TEXT)) {
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCount", alertCount);
					return json;
				}
				if (masterCBSCall.getCbsIsdCode() != null
						&& masterCBSCall.getCbsIsdCode().intValue() != Integer.parseInt(Constants.COUNTRY_CODE_INDIA)
						&& ValidatorUtil.isValidEmail(masterCBSCall.getCbsEmail())) {
					msgBody = String.valueOf(Constants.FIRST_EMAIL_PART)
							+ StringEscapeUtils
									.unescapeHtml(this.communicationManagerImpl.setEmailBody(Integer.valueOf(0),
											Integer.valueOf(0), Constants.MESSAGE_TYPE_EMAIL, Integer.valueOf(0)))
							+ Constants.THIRD_EMAIL_PART;
					msgBody = msgBody.replaceAll("\\[BASE_URL\\]", String.valueOf(Constants.BANK_IMAGE_FOLDER) + "/");
					msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
					msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
					msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
					msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCBSCall.getCbsEmailOtpCode());
					msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.AUTO_LOAN_PRODUCT_NAME);
					boolean sendStatus = false;
					String[] emailId = { masterCBSCall.getCbsEmail() };
					sendStatus = this.communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
					if (!sendStatus) {
						logger.info("AutoProcessManagerImpl LNO :: 3874 with AppSeqId ::" + appSeqId);
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
					logger.info("AutoProcessManagerImpl LNO :: 3886");
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
				json.put("status", "success");
				json.put("message", "OTP code sent");
				return json;
			} catch (NullPointerException e) {
				logger.info("AutoProcessManager.java LNO 3910::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			} catch (RuntimeException e) {
				logger.info("AutoProcessManager.java LNO 3910::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}catch (SQLException e) {
				logger.info("AutoProcessManager.java LNO 3910::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		if (stateId.intValue() == 29)
			try {
				if (appSeqId == null) {
					logger.info("AutoProcessManager.java LNO 3919::");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
					return json;
				}
				if (!ValidatorUtil.isValid(inputOtp)) {
					json.put("status", "error");
					json.put("message", "Invalid OTP code");
					return json;
				}
				ApplicationFormAutoLoan app = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
				if (app == null) {
					logger.info("AutoProcessManager.java LNO 3932::");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				logger.info("AutoProcessManagerImpl.java LNo : 3937  app.getAppOTPAttemptCount() : "
						+ app.getAppOTPAttemptCount() + " with AppSeqId ::" + appSeqId);
				if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
					app.setAppOTPAttemptCount(Integer.valueOf(0));
				if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
					return json;
				}
				app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
				logger.info("ALProcessImpl.java :: LNo :: 3131 appFormData.getAppLoanStatusId() :: ");
				app = this.autoLoanService.save(app);
				logger.info("ALProcessImpl.java :: LNo :: 3134 appFormData.getAppLoanStatusId() :: ");
				if (app == null) {
					logger.info("AutoProcessManager.java LNO 3949 error on saving::");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				if (app.getAppDataSourceId() != null
						&& !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)
						&& ValidatorUtil.isValidEmail(userEmail)) {
					if (!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())
							&& masterCBSCall.getCbsEmail() == null) {
						app.setAppWorkEmail(userEmail);
					} else if (Constants.COUNTRY_CODE_INDIA
							.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())) {
						app.setAppWorkEmail(userEmail);
						//logger.info(" AutoProcessManager.java LNO 3949 ::  Capture Email From User is ::" + userEmail
							//	+ " with AppSeqId ::" + appSeqId);
					}
					if (!app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP))
						SessionUtil.setEmail(userEmail);
				}
				boolean isOPTVerified = false;
				if (appOTPVerifyType == 0) {
					if (masterCBSCall.getCbsOtpCode() != null)
					
					app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode())));
					if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(inputOtp)) {
						masterCBSCall.setCbsMobileOtpVerified("Y");
						app.setAppMobileVerified("Y");
						app.setAppMobileVerificationCodeReceived("Y");
						int otpResType = getOTPResidantVerifiedType(app);
						app.setAppResTypeAtVerified(Integer.valueOf(otpResType));
						isOPTVerified = true;
						json.put("status", "success");
						json.put("message", "OTP authentication successful");
						//logger.info("OTP verfied for mobileNo ::"
							//	+ (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim()
							//	+ " with AppSeqId ::" + appSeqId);
					} else {
						logger.info("AutoProcessManager.java LNO 3994 :: with AppSeqId ::" + appSeqId);
						app.setAppMobileVerified("N");
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}
				} else if (appOTPVerifyType == 1) {
					if (masterCBSCall.getCbsEmailOtpCode() != null)
						app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode());
					if (masterCBSCall.getCbsEmailOtpCode() != null
							&& masterCBSCall.getCbsEmailOtpCode().equals(inputOtp)) {
						masterCBSCall.setCbsEmailOtpVarified("Y");
						app.setAppEmailVerified("Y");
						app.setAppMobileVerificationCodeReceived("Y");
						isOPTVerified = true;
						json.put("status", "success");
						json.put("message", "OTP authentication successful");
						//logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail() + " with AppSeqId ::"
						//		+ appSeqId);
					} else {
						logger.info("AutoProcessManager.java LNO 4023:: with AppSeqId ::" + appSeqId);
						app.setAppEmailVerified("N");
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}
				} else {
					logger.info("AutoProcessManager.java LNO 4032:: with AppSeqId ::" + appSeqId);
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
					statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
					statusRequest.setState(stateId.intValue());
					statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
					statusRequest.setRsmDecision(0);
					statusRequest.setApplicationType(
							(SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue()
									: 0);
					statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
					statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
					StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
					if (statusManagerResponse.getStatus() != 0) {
						app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
					} else if (app.getAppLoanStatusId().intValue() == 0) {
						logger.info("AutoProcessManager.java LNO 4054:: with AppSeqId ::" + appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					if (statusManagerResponse.isEligibleToInsertLog())
						this.autoLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(),
								statusManagerResponse.getStatusCallLogs(), null, null, true);
					logger.info("ALProcessImpl.java :: LNo :: 3229 appFormData.getAppLoanStatusId() :: ");
					app = this.autoLoanService.save(app);
					logger.info("ALProcessImpl.java :: LNo ::  3232 appFormData.getAppLoanStatusId() :: ");
					if (app == null) {
						logger.info("AutoProcessManager.java LNO 4064::");
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
					logger.info("AutoProcessManager.java LNO 4077::");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				return json;
			} catch (NullPointerException e) {
					logger.info("AutoProcessManager.java LNO 4084::", e);
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
			} catch (SQLException e) {
				logger.info("AutoProcessManager.java LNO 4084::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		return json;
	}
	
	public JSONObject varifySMSOTP(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl,
			String isDsrPage, OtherRequest otherRequest) throws JSONException {
		JSONObject json = new JSONObject();
		int appOTPVerifyType = 0;
		if (otherRequest.getAppOTPVerifyType() != null)
			appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim());
		if (SessionUtil.getAutoLoanCbsCallId() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 4646 :: SessionUtil.getAutoLoanCbsCallId() :"
					+ SessionUtil.getAutoLoanCbsCallId());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
			json.put("alertCount", 0);
			return json;
		}
		MasterCBSCall masterCBSCall = this.commonService.getMasterCBSCallObjById(SessionUtil.getAutoLoanCbsCallId());
		if (masterCBSCall == null) {
			//logger.info("AutoProcessManagerImpl LNO :: 4656 :: masterCBSCall:" + masterCBSCall);
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (masterCBSCall.getCbsOtpCount() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 4664 :: masterCBSCall.getCbsOtpCount()"
					+ masterCBSCall.getCbsOtpCount());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (masterCBSCall.getCbsIsdCode() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 4675 :: masterCBSCall.getCbsIsdCode()"
					+ masterCBSCall.getCbsIsdCode());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (masterCBSCall.getCbsMobileNumber() == null) {
			//logger.info("AutoProcessManagerImpl LNO :: 4684 :: masterCBSCall.getCbsMobileNumber()"
			//		+ masterCBSCall.getCbsMobileNumber());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		Integer alertCount = Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCount()));
		if (stateId.intValue() == 33)
			try {
				Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
				ApplicationFormAutoLoan app = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
				if (app == null) {
					logger.info("AutoProcessManagerImpl.java LNO 4702:: after save error");
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
					logger.info("AutoProcessManagerImpl LNO :: 4697 :: alertCount" + alertCount + " with AppSeqId ::"
							+ appSeqId);
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
					json.put("alertCount", alertCount);
					return json;
				}
				if (appOTPVerifyType == 0) {
					masterCBSCall.setCbsOtpCode(
							this.SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
					//logger.info("AutoProcessManagerImpl LNO :: 5301 ::  mobile number ::"
					//		+ masterCBSCall.getCbsMobileNumber() + " with AppSeqId ::" + appSeqId);
				} else {
					logger.info("AutoProcessManagerImpl LNO :: 5304 with AppSeqId ::" + appSeqId);
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCount", alertCount);
					return json;
				}
				boolean sendSMSConsent = false;
				if (isDsrPage.equalsIgnoreCase("true")) {
					boolean bankLmsUserRoleExceptContactCenter = this.commonService
							.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
					if (bankLmsUserRoleExceptContactCenter) {
						logger.info("AutoProcessManagerImpl LNO :: 4728 :: Not contact Center user with AppSeqId ::"
								+ appSeqId);
						sendSMSConsent = true;
					} else {
						logger.info("AutoProcessManagerImpl LNO :: 4728 :: contact Center user with AppSeqId ::"
								+ appSeqId);
					}
				}
				String msgBody = null;
				if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent) {
					msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
							Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
				} else {
					msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
							Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
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
				SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
						(masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim());
				
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
					logger.info("AutoProcessManagerImpl LNO :: 4772 Sorry for the inconvenience");
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
				if (app != null) {
					app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode())));
					app.setAppMobileVerified("N");
				}
				logger.info("ALProcessImpl.java :: LNo :: 3542 appFormData.getAppLoanStatusId() :: ");
				app = this.autoLoanService.save(app);
				logger.info("ALProcessImpl.java :: LNo :: 3545 appFormData.getAppLoanStatusId() :: ");
				if (app == null) {
					logger.info("AutoProcessManagerImpl LNO :: 4784 Sorry for the inconvenience");
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
				json.put("status", "success");
				json.put("message", "OTP code sent");
				return json;
			} catch (NullPointerException e) {
				logger.info("AutoProcessManager.java LNO 4820 :: ", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			} catch (SQLException e) {
				logger.info("AutoProcessManager.java LNO 4820 :: ", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		if (stateId.intValue() == 34)
			try {
				Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
				if (appSeqId == null) {
					logger.info("AutoProcessManager.java LNO :: 4830  Sorry for the inconvenience");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				ApplicationFormAutoLoan app = this.autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
				if (app == null) {
					logger.info("AutoProcessManager.java LNO 4841:: after save error");
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience, Please click <a hrdf='" + Constants.PORT
							+ Constants.CONTEXT + ajaxPostUrl + "'>here</a> to start again.");
					return json;
				}
				logger.info("AutoProcessManager.java LNo : 4840 app.getAppOTPAttemptCount() : "
						+ app.getAppOTPAttemptCount() + " with AppSeqId ::" + appSeqId);
				if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
					app.setAppOTPAttemptCount(Integer.valueOf(0));
				if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
					return json;
				}
				app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
				logger.info("ALProcessImpl.java :: LNo ::  3591 appFormData.getAppLoanStatusId() :: ");
				app = this.autoLoanService.save(app);
				logger.info("ALProcessImpl.java :: LNo :: 3594 appFormData.getAppLoanStatusId() :: ");
				if (app == null) {
					logger.info("AutoProcessManager.java LNO :: 4850 Sorry for the inconvenience");
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience, Please click <a hrdf='" + Constants.PORT
							+ Constants.CONTEXT + ajaxPostUrl + "'>here</a> to start again.");
					return json;
				}
				boolean isOPTVerified = false;
				if (appOTPVerifyType == 0) {
					if (app.getAppMobileVerified().equals("Y")) {
						isOPTVerified = true;
						json.put("status", "success");
						json.put("message", "OTP authentication successful");
						//logger.info("AutoProcessManager.java LNO :: 4859 OTP verfied for mobileNo ::"
							//	+ (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim()
							//	+ " with AppSeqId ::" + appSeqId);
					} else {
						logger.info("AutoProcessManager.java LNO :: 4861 :: OTP authentication failed");
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}
				} else {
					logger.info("AutoProcessManager.java LNO 4868:: OTP authentication failed");
					json.put("status", "error");
					json.put("message", "OTP authentication failed");
					return json;
				}
				if (isOPTVerified) {
					StatusRequest statusRequest = new StatusRequest();
					statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
					statusRequest.setHaveLoanOffer(true);
					statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
					statusRequest.setState(stateId.intValue());
					statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
					statusRequest.setRsmDecision(0);
					statusRequest.setApplicationType(
							(SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue()
									: 0);
					statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
					statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
					statusRequest.setAppMobileVerified(app.getAppMobileVerified());
					StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
					if (statusManagerResponse.getStatus() != 0) {
						app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
					} else if (app.getAppLoanStatusId().intValue() == 0) {
						logger.info("AutoProcessManager.java LNO 4890  :: Sorry for the inconvenience");
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					if (statusManagerResponse.isEligibleToInsertLog())
						this.autoLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(),
								statusManagerResponse.getStatusCallLogs(), null, null, true);
					logger.info("ALProcessImpl.java :: LNo :: 3648 appFormData.getAppLoanStatusId() :: ");
					app = this.autoLoanService.save(app);
					logger.info("ALProcessImpl.java :: LNo ::  3651 appFormData.getAppLoanStatusId() :: ");
					if (app == null) {
						logger.info("AutoProcessManager.java LNO 4900 :: Sorry for the inconvenience");
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
				} else {
					logger.info("AutoProcessManager.java LNO 4906  :: OTP is incorrect! Try again.");
					json.put("status", "error");
					json.put("message", "OTP is incorrect! Try again. with AppSeqId ::" + appSeqId);
					return json;
				}
				masterCBSCall = this.commonService.save(masterCBSCall);
				if (masterCBSCall == null) {
					logger.info("AutoProcessManager.java LNO 4914 :: Sorry for the inconvenience ");
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				return json;
			} catch (NullPointerException e) {
		        logger.info("EducationProcessManagerImpl.java  LNO 4837 :: Exception occured", e);
			} catch (SQLException e) {
				logger.info("AutoProcessManager.java LNO 4921 ::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		return json;
	}

	private void callCrm(ApplicationFormAutoLoan appFormData, ApplicationFormAutoLoanQuote quote) {
		try {
			logger.info(" AutoProcessManagerImpl.java  LNO 4934 :: Before preparing CRM Request Object with AppSeqId ::"+ appFormData.getAppSeqId());
			logger.info("before calling crm prepareCrmRequest method  ");
			CRMRequest crmRequest = prepareCrmRequest(appFormData, quote);
			logger.info("AutoProcessManagerImpl.java  LNO 4936 ::  Before preparing CRM Request Object with AppSeqId ::"+ appFormData.getAppSeqId());

			crmServiceNew.crmLeadCreation(crmRequest, appFormData);
		} catch (NullPointerException e) {
	        logger.info("EducationProcessManagerImpl.java  LNO 4837 :: Exception occured", e);
		} 
	}

	private CRMRequest prepareCrmRequest(ApplicationFormAutoLoan appFormData, ApplicationFormAutoLoanQuote quote) {
		CRMRequest crmRequest = new CRMRequest();
		try {
			if (appFormData == null) {
				logger.info(" AutoProcessManagerImpl.java  LNO 4945 :: appFormData is null, returning back");
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
			
			if (appFormData.getAppCityId() != null
					&& appFormData.getAppCityId().intValue() != Constants.OTHER_ID_INTEGER.intValue()) {
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
			
			if (appFormData.getAppMobileNo() != null)
				crmRequest.setMobileNumber(appFormData.getAppMobileNo());
			if (ValidatorUtil.isValid(appFormData.getAppOfficePhoneStdCode())
					&& appFormData.getAppOfficePhoneStdCode().length() == 4 && appFormData.getAppOfficePhoneNumber() != null
					&& appFormData.getAppOfficePhoneNumber().longValue() == 7L) {
				crmRequest.setPhoneNumber(
						String.valueOf(appFormData.getAppOfficePhoneStdCode()) + appFormData.getAppOfficePhoneNumber());
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
				crmRequest.setResidentType("2");
			} else {
				crmRequest.setResidentType("1");
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
				MasterDocumentType masterDocumentType = this.commonService
						.getDocumentType(appFormData.getAppOtherId().intValue());
				if (masterDocumentType != null && masterDocumentType.getDocumentTypeCrmId() != null) {
					crmRequest.setIdentificationProof(masterDocumentType.getDocumentTypeCrmId());
					crmRequest.setIdentificationNumber(appFormData.getAppOtherIdNumber());
				} else {
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
			crmRequest.setLeadAmount(this.SbiUtil.getFinalLoanAmount(appFormData.getAppLoanAmount()));
			
			AlProduct product = this.commonService.getAutoLoanProductById(appFormData.getAppAutoLoanId());
			if (product != null && product.getAlProductId() != null) {
				if (product.getAlProductId().intValue() == 2) {
					crmRequest.setResidentType("2");
				} else {
					crmRequest.setResidentType("1");
				}
			}
			/* else {
			 	crmRequest.setProductCode(Integer.valueOf(0)); 
			 } */
			
			MasterCBSResponse masterCBSResponse = this.autoLoanService
					.getMasterCBSResponseObjectByLoanTypeAppSeqId(appFormData.getAppSeqId());
			if (masterCBSResponse != null) {
				if (masterCBSResponse.getCbsCifNumber() != null && masterCBSResponse.getCbsCifNumber().length() > 11) {
					crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber().substring(6));
				} else if (masterCBSResponse.getCbsCifNumber() != null
						&& masterCBSResponse.getCbsCifNumber().length() == 11) {
					crmRequest.setCIFNumber(masterCBSResponse.getCbsCifNumber());
				}
			} else {
				crmRequest.setCIFNumber("");
			}
			crmRequest.setDuplicate(true);
			crmRequest.setLoanTypeId(Constants.AUTO_LOAN_ID);
	    if (masterCBSResponse != null && masterCBSResponse.getCbsResponseId() != null) {
	    	MasterCBSCall masterCBSCall = autoLoanService.getMasterCBSCallObjectByCbsResponseId(masterCBSResponse.getCbsResponseId());
	    	crmRequest.setExistingRelationTypeId(masterCBSCall != null && masterCBSCall.getCbsTypeOfRelationship() != null ? masterCBSCall.getCbsTypeOfRelationship() : 0);
	    }
	    
	    if (quote.getLoanQuoteLoanCategoryId() != null) {
	    	MasterLoanCategory masterLoanCategory = this.commonService.getLoanCategoryById(quote.getLoanQuoteLoanCategoryId());
	    	if (masterLoanCategory != null)
	    		crmRequest.setLoanCategoryTypeId(masterLoanCategory.getLoanCategoryCrmId()); 
	    }
	    
	    crmRequest.setIsVehiclePurchasedCertifiedDealer(quote.getLoanQuoteIsDealerOrIndividual() != null ? (quote.getLoanQuoteIsDealerOrIndividual().equalsIgnoreCase("Y") ? "01" : "02" ) : "");
	    crmRequest.setVehicleOwnershipTransferred(quote.getLoanQuoteMaxNoOfOwnership() != null ? quote.getLoanQuoteMaxNoOfOwnership() : 0);
	    
	    if (quote.getLoanQuoteExistingDealerName() != null) {
	    	MasterCarDealer carDealer = this.commonService.findCarDealerByCarDealerId(Integer.parseInt(quote.getLoanQuoteExistingDealerName()));
	    	crmRequest.setCertifiedDealerCode(carDealer.getDealerCrmId() != null ? carDealer.getDealerCrmId() : "12");
	    }
	    crmRequest.setCarMake(quote.getLoanQuoteCarMakeId() != null ? String.valueOf(autoLoanService.getCarCompanyById(quote.getLoanQuoteCarMakeId()).getCompanyId()) : "");
	    crmRequest.setCarModel(quote.getLoanQuoteCarModelId() != null ? String.valueOf(autoLoanService.getCarModelById(quote.getLoanQuoteCarModelId()).getModelCode()) : "");
	   crmRequest.setCarVariant(quote.getLoanQuoteCarVariantId() != null ? String.valueOf(autoLoanService.getCarVariantById(quote.getLoanQuoteCarVariantId()).getVariantCode()) : "");
	    crmRequest.setCarType(quote.getLoanQuoteCarTypeId() != null ? (quote.getLoanQuoteCarTypeId().equalsIgnoreCase("Yes") ? "Y" : "N") : "");
	    crmRequest.setCarExShowRoomPrice(quote.getLoanQuoteExshowroomPriceCar() != null ? BigDecimal.valueOf(quote.getLoanQuoteExshowroomPriceCar()) : null);
	    
	    crmRequest.setCarOnRoadPrice(quote.getLoanQuoteOnRoadPrice() != null ? BigDecimal.valueOf(quote.getLoanQuoteOnRoadPrice()) : null);
	    crmRequest.setCarValuerPrice(quote.getLoanQuoteDealerExshowroomPrice() != null ? BigDecimal.valueOf(quote.getLoanQuoteDealerExshowroomPrice()) : null);
	    crmRequest.setCarInsuredDeclaredPrice(quote.getLoanQuoteInsuredDeclaredValue() != null ? BigDecimal.valueOf(quote.getLoanQuoteInsuredDeclaredValue()) : null);
	    crmRequest.setEmployerName(quote.getLoanQuoteEmployerName() != null ? quote.getLoanQuoteEmployerName() : "");
	    crmRequest.setResidentCountry(quote.getLoanQuoteCountryId() != null ? commonService.getCountryById(quote.getLoanQuoteCountryId()).getCountryName() : "");
	    crmRequest.setEmploymentTypeId(quote.getLoanQuoteEmploymentTypeId() != null ? commonService.getEmploymentTypeById(quote.getLoanQuoteEmploymentTypeId()).getEmploymentTypeCrmId() : "");
	    crmRequest.setGrossMonthlySalary(quote.getLoanQuoteGrossMonthlyIncome() != null ? BigDecimal.valueOf(quote.getLoanQuoteGrossMonthlyIncome()) : null);
	    
	    crmRequest.setNetMonthlySalary(quote.getLoanQuoteNetMonthlySalary() != null ? BigDecimal.valueOf(quote.getLoanQuoteNetMonthlySalary()) : null);
	    crmRequest.setVariableMonthPay(quote.getLoanQuoteVariableMonthPay() != null ? BigDecimal.valueOf(quote.getLoanQuoteVariableMonthPay()) : null);
	    crmRequest.setExpectedRentalIncome(quote.getLoanQuoteExpectedRentalIncome() != null ? BigDecimal.valueOf(quote.getLoanQuoteExpectedRentalIncome()) : null);
	    logger.info("inside the prepareCrmRequest method  ");
	    
	    crmRequest.setOtherIncome(quote.getLoanQuoteOtherIncome() != null ? BigDecimal.valueOf(quote.getLoanQuoteOtherIncome()) : null);
	    crmRequest.setCurrentEmiPaying(quote.getLoanQuotePreEMIs() != null ? BigDecimal.valueOf(quote.getLoanQuotePreEMIs()) : null);
	    logger.info("cheking quote.getLoanQuoteIndustryType() " +quote.getLoanQuoteIndustryType());
	    String industryTypeCrmId = "O";
	    if (quote.getLoanQuoteIndustryType() != null) {
	    	if (!quote.getLoanQuoteIndustryType().equals(Integer.valueOf(9999999))) {
	    		industryTypeCrmId = commonService.getIndustryTypeById(quote.getLoanQuoteIndustryType()).getIndustryTypeCrmId();
	    		if (industryTypeCrmId == null) {
	    			industryTypeCrmId = "O";
	    		}
	    	}
	    }
	    crmRequest.setIndustryType(industryTypeCrmId);
	    crmRequest.setHaveSalaryAccountWithSbi(quote.getLoanQuoteHaveSalaryAccountWithSbi() != null ? (quote.getLoanQuoteHaveSalaryAccountWithSbi().equalsIgnoreCase("Y") ? "Yes" : "No") : "");
	    crmRequest.setProfitAfterTax(quote.getLoanQuoteProfitAfterTax() != null ? BigDecimal.valueOf(quote.getLoanQuoteProfitAfterTax()) : null);
	    crmRequest.setDepreciation(quote.getLoanQuoteDepreciation() != null ? BigDecimal.valueOf(quote.getLoanQuoteDepreciation()) : null);
	    crmRequest.setNetAnnualIncome(quote.getLoanQuoteNetIncome() != null ? BigDecimal.valueOf(quote.getLoanQuoteNetIncome()) : null);
	    crmRequest.setLoanTenure(appFormData.getAppLoanTenure() != null ? appFormData.getAppLoanTenure() : 0);
	    crmRequest.setLoanInterestRate(appFormData.getAppLoanInterestRateDiscount() != null ? appFormData.getAppLoanInterestRateDiscount() : (appFormData.getAppLoanInterestRate() != null ? appFormData.getAppLoanInterestRate() : 0));
	    crmRequest.setLoanEmi(appFormData.getAppLoanEmiDiscount() != null ? BigDecimal.valueOf(appFormData.getAppLoanEmiDiscount()) : (appFormData.getAppLoanEmi() != null ? BigDecimal.valueOf(appFormData.getAppLoanEmi()) : null));
	    crmRequest.setLoanAccountType(appFormData.getAppLoanAccountType() != null ? appFormData.getAppLoanAccountType() : 0);
	    crmRequest.setLoanMaxAmount(SbiUtil.getFinalLoanAmount(Double.valueOf(appFormData.getAppLoanMaxAmont()))); 		//Added by Pratima Max Loan Amount
	    logger.info("getLoanMaxAmount:::"+crmRequest.getLoanMaxAmount()+"..getAppLoanMaxAmont.."+appFormData.getAppLoanMaxAmont());
	    logger.info("getAppLoanEmi::"+appFormData.getAppLoanEmi());
	    logger.info("getAppLoanEmiDiscount::"+appFormData.getAppLoanEmiDiscount());
	    logger.info("getLoanEmi:::"+crmRequest.getLoanEmi());

	      if (appFormData.getAppLoanProcessingFee()!=null) {
	    	  crmRequest.setLoanProcessingFee(BigDecimal.valueOf(appFormData.getAppLoanProcessingFee()));
	    	  logger.info("getLoanProcessingFee:: NOT NULL::"+appFormData.getAppLoanProcessingFee()+"...."+crmRequest.getLoanProcessingFee());
	      } else {
	    	  crmRequest.setLoanProcessingFee(null);
	    	  logger.info("getLoanProcessingFee::NULL::"+appFormData.getAppLoanProcessingFee()+"...."+crmRequest.getLoanProcessingFee());
	      }
	      
	    crmRequest.setCoAppPincode1(appFormData.getAppPincode() != null ? appFormData.getAppPincode() : 0);
	    crmRequest.setResidenceType(appFormData.getAppResidenceType() != null ? appFormData.getAppResidenceType() : 0);
	    crmRequest.setQualification(appFormData.getAppHighestQualification() != null ? appFormData.getAppHighestQualification() : 0);
	    crmRequest.setRelationshipWithBank((appFormData.getAppRelationshipWithBank() != null && appFormData.getAppRelationshipWithBank().equals(19)) ? "5" : "");

	    String alternateNo = "";
	    if (appFormData.getAppAlternateMobileNumber() != null) {
	    	alternateNo = "+" + appFormData.getAppAltISDCode() + "-" + appFormData.getAppAlternateMobileNumber();
	    }
	    crmRequest.setAlternateMobileNumber(alternateNo);	
	    crmRequest.setResPincode(appFormData.getAppPincode());
	    
	    if (appFormData.getAppOfficePhoneNumber() != null) {
	    	String officePhoneNumber = "";
	    	if (appFormData.getAppOfficePhoneStdCode() != null)
	    		officePhoneNumber = appFormData.getAppOfficePhoneStdCode(); 
	    	officePhoneNumber = String.valueOf(officePhoneNumber) + appFormData.getAppOfficePhoneNumber();
	        crmRequest.setOfficePhoneNumber(officePhoneNumber);
	    }
	    
	    if (appFormData.getAppCompanyJoiningDate() != null)
	        crmRequest.setCompanyJoiningDate(DateUtil.getDateInISO8601Format(appFormData.getAppCompanyJoiningDate()));
	    
	    if (appFormData.getAppWorkExperienceYear() != null && appFormData.getAppWorkExperienceYear().intValue() < 13) {
	    	
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
	    
	    crmRequest.setCoAppExist1("N");
	    if (quote.getLoanQuoteCoapplicantFirstId() != null && quote.getLoanQuoteCoapplicantFirstId().intValue() == 1) {
	      crmRequest.setCoAppExist1("Y");
	    }
	    
	    Integer leadSourceKey = commonService.getLeadSourceKey(appFormData);
	    crmRequest.setLeadSourceKey(leadSourceKey);
	    
	    if (appFormData.getAppAutoLoanId() != null) {
	    	if (product != null && product.getAlProductCrmCode() != null)
	    		crmRequest.setProductCode(product.getAlProductCrmCode());
	        
	    	if (product != null && product.getAlProductCategory() != null)
	    		crmRequest.setProductCategory(product.getAlProductCategory());
	    }
	    
	   /* if (quote.getLoanQuoteLoanPurposeId() != null) {
	    	if (quote.getLoanQuoteLoanPurposeId().equals(8)) {
	    		crmRequest.setProductKey("306");
	    	} else if (quote.getLoanQuoteLoanPurposeId().equals(6) && quote.getLoanQuoteResidentTypeId().equals(1)) {
	    		crmRequest.setProductKey("435");
	    	} else if (quote.getLoanQuoteLoanPurposeId().equals(6) && !quote.getLoanQuoteResidentTypeId().equals(1)) {
	    		crmRequest.setProductKey("403");
	    	}
	    }*/
	    
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
	        crmRequest.setEmploymentCountry("");
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
	          MasterState state1 = this.commonService.getStateById(appFormData.getAppOfficeStateId());
	          if (state1 != null)
	            crmRequest.setOfficeState(state1.getStateName()); 
	        } 
	        if (appFormData.getAppEMPCountryId() != null) {
	          MasterCountry masterCountry = this.commonService.getCountryById(appFormData.getAppEMPCountryId());
	          if (masterCountry != null)
	            crmRequest.setEmploymentCountry(masterCountry.getCountryName()); 
	        } 
	        if (appFormData.getAppOfficePincode() != null)
	          crmRequest.setOfficePincode(appFormData.getAppOfficePincode()); 
	      }
	    Optional<MarTech> marTechDeatails = Optional.ofNullable(
				marTechDao.getDetailsByVisitId(autoLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));
		logger.info("AutoProcessManagerImpl.java :: LNo :: 4741 ::marTechDetails is:::" + marTechDeatails);
		if (marTechDeatails.isPresent()) {
			MarTech martech = marTechDeatails.get();
			crmRequest.setCampaignCode(martech.getCampaignCode());
			crmRequest.setOfferCode(martech.getOfferCode());
			crmRequest.setTrackingCode(martech.getTrackingCode());
			logger.info("AutoProcessManagerImpl.java :: LNo :: 4747 ::campaignCode is:::"
					+ crmRequest.getCampaignCode());
			logger.info("AutoProcessManagerImpl.java :: LNo :: 4749 ::offerCode is:::" + crmRequest.getOfferCode());
			logger.info("AutoProcessManagerImpl.java :: LNo :: 4750 ::trackingCode is:::"
					+ crmRequest.getTrackingCode());
		}
		logger.info(
				"AutoProcessManagerImpl.java :: LNo :: 4454 ::trackingCode is:::" + crmRequest.getTrackingCode());
		} catch (NullPointerException e) {
	        logger.info("EducationProcessManagerImpl.java  LNO 4837 :: Exception occured", e);
		} catch(SQLException e) {
			logger.info("exception " + e.getMessage());
		}
		
    	return crmRequest;
	}

	private static int getOTPResidantVerifiedType(ApplicationFormAutoLoan appForm) {
		int flag = 0;
		if (appForm.getAppISDCode() != null && "91".equals(appForm.getAppISDCode())) {
			flag = 1;
		} else {
			flag = 2;
		}
		return flag;
	}

	public JSONObject verifyConcentOtp(Integer moduleId, Integer stateId, Integer bankLMSUserId, String ajaxPostUrl,
			String isDsrPage, OtherRequest otherRequest) throws JSONException, SQLException {
		JSONObject json = new JSONObject();
		int appOTPVerifyType = 0;
		if (otherRequest.getAppOTPVerifyType() != null)
			appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim());
		if (SessionUtil.getAutoLoanApplicationSequenceId() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3803");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
			json.put("alertCount", 0);
			return json;
		}
		ApplicationFormAutoLoan appForm = this.autoLoanService
				.getApplicationFormAutoLoanByAppSeqId(SessionUtil.getAutoLoanApplicationSequenceId());
		if (appForm == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3981 with AppSeqId is null");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (appForm.getAppOTPAttemptCount() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3821 with AppSeqId " + appForm.getAppSeqId());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (appForm.getAppISDCode() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3832 with AppSeqId " + appForm.getAppSeqId());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if (appForm.getAppMobileNo() == null) {
			logger.info("AutoProcessManagerImpl LNO :: 3841 with AppSeqId " + appForm.getAppSeqId());
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		Integer alertCount = appForm.getAppOTPAttemptCount();
		if (stateId.intValue() == 41)
			try {
				Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
				if (alertCount.intValue() >= 5) {
					logger.info("AutoProcessManagerImpl LNO :: 3854");
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
					json.put("alertCount", alertCount);
					return json;
				}
				if (appOTPVerifyType == 0) {
					logger.info("inside apimpl line mnumber 3521 ");
					appForm.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(appForm.getAppMobileNo()));
					//logger.info("mobile number ::" + appForm.getAppMobileNo() + "with AppSeqId " + appSeqId);
					//logger.info("mobile OTP ::" + appForm.getAppMobileVerificationCode());
				} else {
					logger.info("AutoProcessManagerImpl LNO :: 3864 with AppSeqId " + appSeqId);
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCount", alertCount);
					return json;
				}
				boolean sendSMSConsent = false;
				if (isDsrPage.equalsIgnoreCase("true")) {
					boolean bankLmsUserRoleExceptContactCenter = this.commonService
							.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
					if (bankLmsUserRoleExceptContactCenter) {
						logger.info("Not contact Center user");
						sendSMSConsent = true;
					} else {
						logger.info("contact Center user");
					}
				}
				String msgBody = null;
				if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent) {
					msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
							Constants.MESSAGE_TYPE_SMS_CONSENT, Integer.valueOf(0));
				} else {
					msgBody = this.communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
							Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
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
				appForm = this.autoLoanService.save(appForm);
				if (appForm == null) {
					logger.info(
							"AutoProcessManagerImpl LNO :: 4523 Sorry for the inconvenience with AppSeqId " + appSeqId);
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
				json.put("status", "success");
				json.put("message", "OTP code sent");
				return json;
			} catch (NullPointerException e) {
				logger.info("AutoProcessManagerImpl.java LNO 3923::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			} catch (SQLException e) {
				logger.info("AutoProcessManagerImpl.java LNO 3923::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		if (stateId.intValue() == 42)
			try {
				Integer appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
				if (appSeqId == null) {
					logger.info("AutoProcessManagerImpl.java LNO 3933::with AppSeqId " + appSeqId);
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
						//logger.info("OTP verfied for mobileNo ::"
						//		+ (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim()
						//		+ " with AppSeqId " + appSeqId);
					} else {
						logger.info("AutoProcessManagerImpl.java LNO 3964:: with AppSeqId " + appSeqId);
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}
				} else {
					logger.info("AutoProcessManagerImpl.java LNO 3971::");
					json.put("status", "error");
					json.put("message", "OTP authentication failed");
					return json;
				}
				if (isOPTVerified) {
					StatusRequest statusRequest = new StatusRequest();
					statusRequest.setCurrentStatus(appForm.getAppLoanStatusId().intValue());
					statusRequest.setHaveLoanOffer(true);
					statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID.intValue());
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
						logger.info("AutoProcessManagerImpl.java LNO 3993:: with AppSeqId " + appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					this.autoLoanHelper.insertCallLog(appForm.getAppSeqId(), bankLMSUserId.intValue(),
							Constants.CALL_LOGS_MESSAGE_STATE171_ID, null, null, true);
				} else {
					logger.info("OTP is authentication failed:: with AppSeqId " + appSeqId);
					json.put("status", "error");
					json.put("message", "OTP is incorrect! Try again.");
					return json;
				}
				logger.info("ALProcessImpl.java :: LNo :: 4138 appFormData.getAppLoanStatusId() :: ");
				appForm = this.autoLoanService.save(appForm);
				logger.info("ALProcessImpl.java :: LNo :: 4141 appFormData.getAppLoanStatusId() :: ");
				if (appForm == null) {
					logger.info("AutoProcessManagerImpl.java LNO 4017::with AppSeqId " + appSeqId);
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				return json;
			} catch (NullPointerException e) {
				logger.info("AutoProcessManagerImpl.java LNO 4042::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			} catch (SQLException e) {
				logger.info("AutoProcessManagerImpl.java LNO 4042::", e);
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
		return json;
	}
	
	public Integer cibilCall(ApplicationFormAutoLoan application) throws ParseException {
		Integer appCibilScore = -1;
		try {
			BureauLinkRequestNew bureauLinkRequestnew =bureauLinkUtil.prepareBureaulinkRequestForAutoLoan(application); 
			BureauLinkRequestResponse requestResponseData = bureauLinkUtil.saveBureauLinkRequestNew(bureauLinkRequestnew, "A01");
			String jsonInString = (new Gson()).toJson(bureauLinkRequestnew);
			//logger.info("jsonRequest :: " + jsonInString);

			//new code for getting response data
			String jsonResponse= bureauLinkUtil.sendReceiveJson(jsonInString);
			//logger.info("Response Received from Cibil API :: " +jsonResponse+ " At "+new Date() + "  for MemberRefNo :: " + application.getAppSeqId().toString());
			
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
				        
					if (requestResponseData != null && requestResponseData.getBlCibilScore() != null) {
						appCibilScore = Integer.parseInt(requestResponseData.getBlCibilScore());
						application.setAppCibilScore(appCibilScore);
					}
				} else {
					requestResponseData.setBlResponseErrorCode("00000");
					requestResponseData.setBlFetchResponseStatus("FAIL");
					application.setAppCibilScore(appCibilScore);
				}
			} else {
				requestResponseData.setBlResponseErrorCode("00000");
				requestResponseData.setBlFetchResponseStatus("FAIL");
				application.setAppCibilScore(appCibilScore);
			}
	       
			return application.getAppCibilScore();
			
		} catch (NullPointerException e) {
			logger.info("Exception in BureauLink Process :: " + e);
		} catch (SQLException e) {
			logger.info("Exception in BureauLink Process :: " + e);
		} catch (IOException e) {
			logger.info("Exception in BureauLink Process :: " + e);
		} catch (NumberFormatException e) {
			logger.info("Exception in BureauLink Process :: " + e);
		}
		application.setAppCibilScore(appCibilScore != null ? appCibilScore : -1);
	    return application.getAppCibilScore();
	}
	
}
