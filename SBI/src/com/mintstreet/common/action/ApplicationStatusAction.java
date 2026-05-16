package com.mintstreet.common.action;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.CRMFetchLeadStatus;
import com.mintstreet.common.entity.CRMNextLog;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.RequestACallBack;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.AESEncryption;
import com.mintstreet.common.util.CRMFetchLeadStatusApi;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.integration.edms.action.EdmsFinalServiceAction;
import com.mintstreet.integration.edms.bo.FstoreDoc;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoanQuote;
import com.mintstreet.loan.agriloan.entity.MasterAgriProduct;
import com.mintstreet.loan.agriloan.service.AgriLoanService;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.card.entity.ApplicationFormCreditCard;
import com.mintstreet.loan.card.entity.MasterCardType;
import com.mintstreet.loan.card.service.CreditCardService;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.personal.service.PersonalLoanService;

public class ApplicationStatusAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(ApplicationStatusAction.class.getName());

	private static final long serialVersionUID = 1L;

	@Autowired
	private HomeLoanService homeLoanService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private AutoLoanService autoLoanService;

	@Autowired
	private PersonalLoanService personalLoanService;

	@Autowired
	
	private EducationLoanService educationLoanService;

	@Autowired
	private AgriLoanService agriLoanService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private AESEncryption aesEncryption;

	@Autowired
	private CRMFetchLeadStatusApi crmFetchLeadStatusApi;
	
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	
	@Autowired
	private EdmsFinalServiceAction edmsServiceAction;

	private ApplicationFormHomeLoan applicationHL;

	private ApplicationFormHomeLoanQuote quoteHL;

	private ApplicationFormAutoLoan applicationAL;

	private ApplicationFormAutoLoanQuote quoteAL;

	private ApplicationFormEducationLoan applicationEL;

	private ApplicationFormEducationLoanQuote quoteEL;

	private ApplicationFormAgriLoan applicationAGL;

	private ApplicationFormAgriLoanQuote quoteAGL;

	private ApplicationFormPersonalLoan applicationPL;

	private ApplicationFormPersonalLoanQuote quotePL;

	private ApplicationFormCreditCard applicationSCC;

	//private RequestACallBack requestACallBack;

	private String loanPurposeNameAGL1;

	private String loanPurposeNameAGL2;

	private String loanPurposeNameAGL3;

	private String loanPurposeNameAGL4;

	private MasterCardType cardType;

	private String appReferenceId;

	private String trackMobile;

	private String trackIsdCode;

	private String pdfDownloadUrl;

	private String statusDetails;

	private String appCardReferenceNumber;

	private double totalLoanAmount;

	private String captcha;

	private String trackInputOtpWantUs;

  public String execute() {
		return "success" + ((uiType == null) ? "" : uiType);
	}

  public String applicationStatus() throws JSONException, SQLException, NoSuchAlgorithmException, UnsupportedEncodingException {
		boolean isRecored = false;
		applicationStatusPage = 1;
		metaInfo.setTitle(Constants.APPLICATION_STATUS_TITLE);
		metaInfo.setKeywords(Constants.APPLICATION_STATUS_KEYWORDS);
		metaInfo.setDescription(Constants.APPLICATION_STATUS_DESCRIPTION);
		browserver = CommonUtilites.getBrowserUserAgent();
		browser = CommonUtilites.getBrowserName();
		if (!ValidatorUtil.isValid(SessionUtil.getSelectedLanguage()))
			SessionUtil.setSelectedLanguage("English");
		if (iPAddressForAppAndDBServerPass != 1)
			isValidIpAddressForAppAndDBServer();
		if (iPAddressForAppAndDBServerPass == 0)
			return "under-maintainance" + ((uiType == null) ? "" : uiType);
		currentHomeTabActive = 5;
		request = RequestUtil.getServletRequest();
		stateManagerBean = stateManager.getState(request, Integer.valueOf(10));
		OtherRequest otherRequest = stateManagerBean.getOtherRequest();
		
		if (otherRequest != null) {
			if (otherRequest.getAppReferenceId() != null)
				appReferenceId = otherRequest.getAppReferenceId();
			if (otherRequest.getAppCardReferenceNumber() != null)
				appCardReferenceNumber = otherRequest.getAppCardReferenceNumber();
			if (otherRequest.getTrackIsdCode() != null)
				trackIsdCode = otherRequest.getTrackIsdCode();
			if (otherRequest.getTrackMobile() != null)
				trackMobile = otherRequest.getTrackMobile();
			if (otherRequest.getAppBtn() != null)
				appBtn = otherRequest.getAppBtn();
			if (otherRequest.getCaptcha() != null)
				captcha = otherRequest.getCaptcha();
			if (otherRequest.getTrackInputOtpWantUs() != null)
				trackInputOtpWantUs = otherRequest.getTrackInputOtpWantUs();
			if (requestIndex.intValue() != 5 && requestIndex.intValue() != 4) {
				if (appReferenceId != null && appReferenceId.startsWith(Constants.INTIAL_STRING_HL)) {
					loanTypeId = Integer.valueOf(1);
				} else if (appReferenceId != null && appReferenceId.startsWith(Constants.INTIAL_STRING_AL)) {
					loanTypeId = Integer.valueOf(2);
				} else if (appReferenceId != null && appReferenceId.startsWith(Constants.INTIAL_STRING_PL)) {
					loanTypeId = Integer.valueOf(3);
				} else if (appReferenceId != null && appReferenceId.startsWith(Constants.INTIAL_STRING_EL)) {
					loanTypeId = Integer.valueOf(4);
				} else if (appReferenceId != null && appReferenceId.startsWith(Constants.INTIAL_STRING_AGL)) {
					loanTypeId = Integer.valueOf(15);
			   } else if (appCardReferenceNumber != null ) {
				//} else if (appCardReferenceNumber != null && appCardReferenceNumber.startsWith("2")) {
				   logger.info("inside the 201 appCardReferenceNumber"+appCardReferenceNumber);
					loanTypeId = Integer.valueOf(17);
				}
			}
		}
		if (!ValidatorUtil.isValid(sourceId))
			sourceId = Integer.valueOf(1);
		if (!ValidatorUtil.isValid(SessionUtil.getSelectedLanguage()))
			SessionUtil.setSelectedLanguage("English");

		if (requestIndex != null && (requestIndex.intValue() == 5 || requestIndex.intValue() == 4)) {
			
			String inputOtp = null;
			if (requestIndex.intValue() == 5) {
				
				responseMessage = SbiUtil.checkCaptcha(captcha);
				
				if (responseMessage != null)
					return "jsonResponsePage";
				if (trackInputOtpWantUs != null)
					inputOtp = trackInputOtpWantUs;
			} else {
				requestIndex.intValue();
			}
			reqst = commonService.getRqstIdById(SessionUtil.getRqstId());

			if (reqst == null) {
				logger.error("ApplicationStatusAction.java LNo : 149 : Exception Caught" + reqst + " Is NULL");
				return "jsonResponsePage";
			}
			json = requestWantUsToCallYou(SessionUtil.getRqstId(), reqst, null, null, null, requestIndex,
					inputOtp, null);
			if (json.get("status").toString().equalsIgnoreCase("success")) {
				logger.info("StateId on click of 222 : " + stateManagerBean.getState());
				responseMessage = "success|" + json.getString("message");
				if (requestIndex.intValue() != 4) {
				responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
				}
				SessionUtil.setCaptch(null);
				return "jsonResponsePage";
			}
			logger.info("StateId on click of 229 : " + stateManagerBean.getState()+"requestIndex.intValue()");
			responseMessage = "error|" + json.getString("message");
			if (requestIndex.intValue() != 4) {
			responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
			}
			return "jsonResponsePage";
		}
		if (stateManagerBean.getState() == 1) {
			logger.info("StateId on click of Confirm 1 : " + stateManagerBean.getState());

			jsonJSArray = SbiUtil.populateJSValidation(Integer.valueOf(3), Constants.HOME_ID).toString();
			logger.info("StateId on click of Confirm 2 : " + jsonJSArray);
			if (uiType != null)
				jsonJSArrayApplicationTrack = SbiUtil
						.populateJSValidation(Integer.valueOf(1), Constants.APP_LOAN_TRACK_ID).toString();
			if (SessionUtil.getLeadId() != null)
				lead = commonService.getLeadById(SessionUtil.getLeadId());

			logger.info("StateId on click of Confirm 3 : " + lead);

			Map<Integer, String> mapCity = new LinkedHashMap<>();
			mapCity = commonService.getStateCityDistrictBranchWithoutLoanType(Integer.valueOf(2), null, null, null,
					null, null, null, null, null, null);
			mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
			beanList.setCitiesoptgrp1(mapCity);
			UIBeanListStatic.setCitiesCard(commonService.getCititesSCC(Constants.CREDIT_CARD_ID));
			UIBeanListStatic.setCardCityGroup1(commonService.getcitiesSCCMetro(Constants.CREDIT_CARD_ID));

			logger.info("StateId on click of Confirm 4 : " + requestIndex);
		}
		if (requestIndex == null) {
			logger.info(
					"ApplicationStatusAction.java LNo : 101  : appReferencetIdEncrypted : in applicationTrackAction "
							+ appReferencetIdEncrypted);
			if (ValidatorUtil.isValid(appReferencetIdEncrypted)) {
				appReferenceId = aesEncryption.decrypt(appReferencetIdEncrypted);
				requestIndex = Integer.valueOf(2);
			} else {
				if (!ValidatorUtil.isValid(uiType)) {
					responseMessage = "displayPage";
					return "applicationTrackError" + ((uiType == null) ? "" : uiType);
				}
				jsonJSArray = SbiUtil.populateJSValidation(Integer.valueOf(1), Constants.APP_LOAN_TRACK_ID)
						.toString();
				return "success" + ((uiType == null) ? "" : uiType);
			}
		}
		if (requestIndex.intValue() == 1)
			logger.info("");
			campaignManager.getCampaignId(null, source, se, cp, ag, sourceId.intValue(),
					Constants.APP_LOAN_TRACK_ID.intValue());
			logger.info("after calling getCampaignId method ");
	
		if (requestIndex.intValue() == 18) {
			logger.info("283");
			json = new JSONObject();
			if (json.get("status").toString().equalsIgnoreCase("success")) {
				responseMessage = "success|" + json.getString("message");
				return "jsonResponsePage";
			}
			responseMessage = "error|" + json.getString("message");
			return "jsonResponsePage";
		}
		if (requestIndex.intValue() == 1 || requestIndex.intValue() == 2) {
			if (loanTypeId != null && loanTypeId.equals(Constants.CREDIT_CARD_ID)) {
				logger.info("293");
				if (!ValidatorUtil.isValid(appCardReferenceNumber)) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid application reference no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no.";
					return "jsonResponsePage";
				}
			} else if (appReferenceId != null && !ValidatorUtil.isValid(appReferenceId)) {
				if (!ValidatorUtil.isValid(uiType)) {
					responseMessage = "Invalid application reference no.";
					return "applicationTrackError" + ((uiType == null) ? "" : uiType);
				}
				responseMessage = "error|Invalid application reference no.";
				return "jsonResponsePage";
			}
			if (requestIndex.intValue() == 1) {
				logger.info("inside the requestIndex.intValue() method 310");
				if (!ValidatorUtil.isValid(trackIsdCode)) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid ISD Code";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid ISD Code";
					return "jsonResponsePage";
				}
				if (!ValidatorUtil.isValid(trackMobile)) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid Mobile No";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid Mobile No";
					return "jsonResponsePage";
				}
			}
			// logger.info("otherRequest data before calling SCC_LOAN_ACTION " + otherRequest); 
			//logger.info("ApplicationStatusAction.java LNo : 145  : appReferenceId " + appReferenceId
				//	+ " :: trackIsdCode " + trackIsdCode + " :: trackMobile " + trackMobile
				//	+ " :: appCardReferenceNumber " + appCardReferenceNumber + " :: loanTypeId "
				//	+ loanTypeId);

			if(appCardReferenceNumber != null) {
				loanTypeId = 17;
			}
			if (loanTypeId != null && Constants.CREDIT_CARD_ID.equals(loanTypeId)) {
				loanTypeId = Integer.valueOf(17);
				ajaxPostUrl = Constants.SCC_LOAN_ACTION;
				if (trackMobile != null && trackIsdCode != null) {
					applicationSCC = creditCardService
							.getApplicationFormCreditCardByAppReferenceIdAndMobileNo(appCardReferenceNumber,
									String.valueOf(trackIsdCode) + trackMobile);
					logger.info("reqst 2222 " + appBtn);
					SessionUtil.setTrackerChecker(appBtn);
					if (!appBtn.equals("CALLBACK")) {
						if (otherRequest != null) {
							if (applicationSCC != null) {
	
								logger.info("inside the trackmobile and trackIsdCode condition   applicationSCC!=null");
								json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst,
										appCardReferenceNumber, trackMobile, trackIsdCode, requestIndex,
										null, loanTypeId);
	
								responseMessage = "success|" + json.getString("message");
								return "jsonResponsePage";
	
							} else {
								logger.info("inside else condition applicationSCC is null");
								responseMessage = "error|Invalid application reference no or mobile no.";
								return "jsonResponsePage";
							}
						}
					}

				} else if (appCardReferenceNumber != null) {
					applicationSCC = creditCardService
							.getApplicationFormCreditCardByAppCardCreditLeadRefNumber(appCardReferenceNumber);
				}
				if (applicationSCC == null) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid application reference no or mobile no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no or mobile no.";
					return "jsonResponsePage";
				}
				if (applicationSCC.getAppProductVariantId() != null)
					cardType = creditCardService
							.getCardTypeById(applicationSCC.getAppProductVariantId());
				appSeqId = applicationSCC.getAppSeqId();
				SessionUtil.setCreditCardApplicationSequenceId(appSeqId);
				creditCardPage = 1;
				if (applicationSCC.getAppDownloadPdfFileName() != null) {
					SecureRandom rand = new SecureRandom();
					pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
							+ Constants.CONTEXT + Constants.SCC_LOAN_ACTION + "?generatePDF="
							+ applicationSCC.getAppCardCreditLeadRefNumber() + rand.nextInt(1000)
							+ ((uiType == null) ? "" : ("&uiType=" + uiType));
				} else {
					pdfDownloadUrl = null;
				}
				LoanStatus status = commonService
						.getLoanStatusByLoanStatusId(applicationSCC.getAppLoanStatusId());
				if (status != null)
					statusDetails = status.getLoanStatusCustomerTitle();
			} 
			 else if (appReferenceId.startsWith(Constants.INTIAL_STRING_HL)
					|| (appBtn != null && appBtn.equals("CALLBACK"))) {
				logger.info("inside the trackmobile and trackIsdCode condition   CALLBACK appReferenceId  "
						+ appReferenceId);

				ajaxPostUrl = Constants.HOME_LOAN_ACTION;
				loanTypeId = Integer.valueOf(1);
				if (trackMobile != null && trackIsdCode != null) {

					applicationHL = homeLoanService.getApplicationFormHomeLoanByAppReferenceIdAndMobileNo(
							appReferenceId, trackIsdCode + trackMobile);
					logger.info("inside the trackmobile and trackIsdCode condition  CALLBACK 11 applicationHL    "
							+ applicationHL);
					SessionUtil.setTrackerChecker(appBtn);
					if (!appBtn.equals("CALLBACK")) {

						//logger.info("otherRequest HL : " + otherRequest);
						if (otherRequest != null) {
							if (applicationHL != null) {

								logger.info(
										"inside the trackmobile and trackIsdCode condition   applicationHL!=null && appReferenceId  ");
								json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst,
										appReferenceId, trackMobile, trackIsdCode, requestIndex,
										null, loanTypeId);

								responseMessage = "success|" + json.getString("message");
								return "jsonResponsePage";

							} else {
								logger.info("inside else condition applicationHL is null");
								responseMessage = "error|Invalid application reference no or mobile no.";
								return "jsonResponsePage";
							}
						}
					}

				} else if (appReferenceId != null) {
					applicationHL = homeLoanService.getApplicationFormHomeLoanByAppReferenceId(appReferenceId);
					logger.info("HomeLoanProcessImpl.java Line 403   *************after svae method  HomeLoanService file"+ applicationHL.getAppReferenceId());


					FstoreDoc doc = new FstoreDoc();
					Integer branchCode = commonService.getBranchCodeByBranchId(applicationHL.getAppBranchId());
					doc.setAppPhotoIdName(applicationHL.getAppPhotoIdName());
					doc.setAppIdentityProofName(applicationHL.getAppIdentityProofName());
					doc.setAppResidenceProofName(applicationHL.getAppResidenceProofName());
					doc.setAppIncomeProofName(applicationHL.getAppIncomeProofName());
					doc.setAppEmploymentProofName(applicationHL.getAppEmploymentProofName());

					logger.info("HomeLoanProcessImpl.java line HomeLoanProcessImpl 446  getAppReferenceId..... "
							+ applicationHL.getAppReferenceId());
					edmsServiceAction.uploadDocumentsToEDMS(doc, 1, appReferenceId, branchCode);
					// end code for edms

				}

				logger.info("HomeLoanProcessImpl.java Line 412  applicationHL   " + applicationHL + "appBtn " + appBtn);

				if (applicationHL == null) {
					if (!ValidatorUtil.isValid(uiType)) {
						if (appBtn != null && appBtn.equals("CALLBACK")) {
							responseMessage = "error|Reference no or mobile no you have entered does not belong to home loan. ";
							return "jsonResponsePage";
						}
						responseMessage = "Invalid application reference no or mobile no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no or mobile no.";
					return "jsonResponsePage";
				}
				if (appBtn != null && appBtn.equals("CALLBACK") && applicationHL != null) {
					if (appReferenceId != null && trackIsdCode != null)
						isRecored = commonService
								.getRequestACallBackByAppReferenceIdAndMobileNo(appReferenceId, trackMobile);
					if (!isRecored) {
						responseMessage = "error|You have already made a callback request for this reference number. | trackMobil";
						return "jsonResponsePage";
					}
					json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst, appReferenceId,
							trackMobile, trackIsdCode, requestIndex, null, loanTypeId);
					if (json.get("status").toString().equalsIgnoreCase("success")) {
						responseMessage = "success|" + json.getString("message");
						SessionUtil.setCaptch(null);
						return "jsonResponsePage";
					}
					responseMessage = "error|" + json.getString("message");
					return "jsonResponsePage";
				}


				appSeqId = applicationHL.getAppSeqId();
				ApplicationFormHomeLoanQuote quoteHL = homeLoanService
						.getApplicationFromHomeLoanQuoteByQuoteId(applicationHL.getAppQuoteId());
				if (quoteHL.getLoanQuoteLoanPurposeId().intValue() == 5) {
					SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
				} else {
					SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
				}
				generateUIBeanList(applicationHL);
				homeLoanPage = 1;
				if (applicationHL.getAppDownloadPdfFileName() != null) {
					SecureRandom rand = new SecureRandom();
					if (quoteHL.getLoanQuoteLoanPurposeId().equals(Integer.valueOf(5))) {
						pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
								+ Constants.CONTEXT + Constants.HOME_TOP_UP_LOAN_ACTION
								+ "?generatePDF=" + applicationHL.getAppReferenceId() + rand.nextInt(1000)
								+ ((uiType == null) ? "" : ("&uiType=" + uiType));
					} else {
						pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
								+ Constants.CONTEXT + Constants.HOME_LOAN_ACTION + "?generatePDF="
								+ applicationHL.getAppReferenceId() + rand.nextInt(1000)
								+ ((uiType == null) ? "" : ("&uiType=" + uiType));
					}
				} else {
					pdfDownloadUrl = null;
				}

				CRMNextLog crmResponse = commonService.getCRMLeadIdByReferenceID(appReferenceId);
				if (crmResponse.getCrmLeadId() != null) {
					Integer leadSourceKey = commonService.getLeadSourceKey(applicationHL);
					logger.info("leadSourceKey " + leadSourceKey);
					String sourceId = getSourceIdOnLeadSourceKey(leadSourceKey);
					crmFetchLeadStatusApi.fetchLeadStatusRequest(appReferenceId, Constants.HOME_LOAN_ID, crmResponse.getCrmLeadId(), sourceId);
					CRMFetchLeadStatus crmFetchStatus = commonService.getLeadByReferenceID(appReferenceId);
					logger.info("crmFetchStatus is " + crmFetchStatus);
					if(crmFetchStatus == null || crmFetchStatus.getResponseStatus().equals("F")) {
						LoanStatus status = commonService
								.getLoanStatusByLoanStatusId(applicationHL.getAppLoanStatusId());
						if (status != null)
							statusDetails = status.getLoanStatusCustomerTitle();
					} else {
						statusDetails = crmFetchStatus.getResponseDescription();
					}
				} else {
					LoanStatus status = commonService
							.getLoanStatusByLoanStatusId(applicationHL.getAppLoanStatusId());
					if (status != null)
						statusDetails = status.getLoanStatusCustomerTitle();
				}
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_AL)) {
				ajaxPostUrl = Constants.AUTO_LOAN_ACTION;
				loanTypeId = Integer.valueOf(2);
				if (trackMobile != null && trackIsdCode != null) {
					applicationAL = autoLoanService.getApplicationFormAutoLoanByAppReferenceIdAndMobileNo(
							appReferenceId, String.valueOf(trackIsdCode) + trackMobile);

					//logger.info("otherRequest AL : " + otherRequest);
					SessionUtil.setTrackerChecker(appBtn);
					if (otherRequest != null) {
						if (applicationAL != null) {

							logger.info("inside the trackmobile and trackIsdCode condition   applicationAL!=null");
							json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst, appReferenceId,
									trackMobile, trackIsdCode, requestIndex, null, loanTypeId);

							responseMessage = "success|" + json.getString("message");
							return "jsonResponsePage";

						} else {
							logger.info("inside else condition applicationAL is null");
							responseMessage = "error|Invalid application reference no or mobile no.";
							return "jsonResponsePage";
						}
					}

				} else if (appReferenceId != null) {
					applicationAL = autoLoanService.getApplicationFormAutoLoanByAppReferenceId(appReferenceId);
					
					logger.info("ALProcessImpl.java Line 423   *************before svae method  autoLoanService file" + applicationAL.getAppReferenceId());

					FstoreDoc doc = new FstoreDoc();
					Integer branchCode = commonService.getBranchCodeByBranchId(applicationAL.getAppBranchId());
					doc.setAppPhotoIdName(applicationAL.getAppPhotoIdName());
					doc.setAppIdentityProofName(applicationAL.getAppIdentityProofName());
					doc.setAppResidenceProofName(applicationAL.getAppResidenceProofName());
					doc.setAppIncomeProofName(applicationAL.getAppIncomeProofName());
					doc.setAppAutoDetailsProofName(applicationAL.getAppAutoDetailsProofName());

					edmsServiceAction.uploadDocumentsToEDMS(doc, 2, appReferenceId, branchCode);
					// edms

				}
				if (applicationAL == null) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid application reference no or mobile no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no or mobile no.";
					return "jsonResponsePage";
				}
				appSeqId = applicationAL.getAppSeqId();
				SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
				generateUIBeanList(applicationAL);
				autoLoanPage = 1;
				if (applicationAL.getAppDownloadPdfFileName() != null) {
					SecureRandom rand = new SecureRandom();
					pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
							+ Constants.CONTEXT + Constants.AUTO_LOAN_ACTION + "?generatePDF="
							+ applicationAL.getAppReferenceId() + rand.nextInt(1000)
							+ ((uiType == null) ? "" : ("&uiType=" + uiType));
				} else {
					pdfDownloadUrl = null;
				}
				
				CRMNextLog crmResponse = commonService.getCRMLeadIdByReferenceID(appReferenceId);
				if (crmResponse.getCrmLeadId() != null) {
					Integer leadSourceKey = commonService.getLeadSourceKey(applicationAL);
					logger.info("leadSourceKey " + leadSourceKey);
					String sourceId = getSourceIdOnLeadSourceKey(leadSourceKey);
					crmFetchLeadStatusApi.fetchLeadStatusRequest(appReferenceId, Constants.AUTO_LOAN_ID, crmResponse.getCrmLeadId(), sourceId);
					CRMFetchLeadStatus crmFetchStatus = commonService.getLeadByReferenceID(appReferenceId);
					if(crmFetchStatus == null || crmFetchStatus.getResponseStatus().equals("F")) {
						LoanStatus status = commonService
								.getLoanStatusByLoanStatusId(applicationAL.getAppLoanStatusId());
						if (status != null)
							statusDetails = status.getLoanStatusCustomerTitle();
					} else {
						statusDetails = crmFetchStatus.getResponseDescription();
					}
				} else {
					LoanStatus status = commonService
							.getLoanStatusByLoanStatusId(applicationAL.getAppLoanStatusId());
					if (status != null)
						statusDetails = status.getLoanStatusCustomerTitle();
				}
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_PL)) {
				ajaxPostUrl = Constants.PERSONAL_LOAN_ACTION;
				loanTypeId = Integer.valueOf(3);
				if (trackMobile != null && trackIsdCode != null) {
					applicationPL = personalLoanService
							.getApplicationFormPersonalLoanByAppReferenceIdAndMobileNo(appReferenceId,
									String.valueOf(trackIsdCode) + trackMobile);
					logger.info("ApplicationStatusAction.java applicationHL..... " + applicationHL
							+ "..appReferenceId.." + appReferenceId);

					//logger.info("otherRequest PL : " + otherRequest);
					SessionUtil.setTrackerChecker(appBtn);
					if (otherRequest != null) {
						if (applicationPL != null) {

							logger.info("inside the trackmobile and trackIsdCode condition   applicationPL!=null");
							json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst, appReferenceId,
									trackMobile, trackIsdCode, requestIndex, null, loanTypeId);

							responseMessage = "success|" + json.getString("message");
							return "jsonResponsePage";

						} else {
							logger.info("inside else condition applicationPL is null");
							responseMessage = "error|Invalid application reference no or mobile no.";
							return "jsonResponsePage";
						}
					}

				} else if (appReferenceId != null) {
					applicationPL = personalLoanService.getApplicationFormPersonalLoanByAppReferenceId(appReferenceId);

					logger.info("PLProcessImpl.java Line 423   *************before svae method  autoLoanService file" + applicationPL.getAppReferenceId());

					FstoreDoc doc = new FstoreDoc();
					Integer branchCode = commonService.getBranchCodeByBranchId(applicationPL.getAppBranchId());
					doc.setAppPhotoIdName(applicationPL.getAppPhotoIdName());
					doc.setAppIdentityProofName(applicationPL.getAppIdentityProofName());
					doc.setAppResidenceProofName(applicationPL.getAppResidenceProofName());
					doc.setAppIncomeProofName(applicationPL.getAppIncomeProofName());
					doc.setAppPensionProofName(applicationPL.getAppPensionProofName());
					logger.info("AutoProcessImpl.java line ApplicationFormAutoLoan  460  FstoreDoc @@@@@@@@@@@@@@@@@ @@@  FstoreDoc..... " + doc.toString());
					
					edmsServiceAction.uploadDocumentsToEDMS(doc, 3, appReferenceId, branchCode);
					// end code for edms

				}
				if (applicationPL == null) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid application reference no or mobile no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no or mobile no.";
					return "jsonResponsePage";
				}
				appSeqId = applicationPL.getAppSeqId();
				SessionUtil.setPersonalLoanApplicationSequenceId(appSeqId);
				generateUIBeanList(applicationPL);
				personalLoanPage = 1;
				String loanActionType = "";
				quotePL = personalLoanService
						.getApplicationFormPersonalLoanQuoteByQuoteId(applicationPL.getAppQuoteId());
				if (Constants.PENSION_LOAN_PURPOSE_ID.equals(quotePL.getLoanQuoteLoanPurposeId())) {
					SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PENSION);
					ajaxPostUrl = Constants.PENSION_LOAN_ACTION;
					loanActionType = Constants.PENSION_LOAN_ACTION;
					appPLTypeId = Constants.APP_PL_TYPE_PENSION;
				} else if (quotePL.getLoanQuoteLoanPurposeId().intValue() == 11
						|| quotePL.getLoanQuoteLoanPurposeId().intValue() == 12) {
					SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PERSONAL);
					ajaxPostUrl = Constants.PERSONAL_LOAN_ACTION;
					loanActionType = Constants.PERSONAL_LOAN_ACTION;
					appPLTypeId = Constants.APP_PL_TYPE_PERSONAL;
				} else if (quotePL.getLoanQuoteLoanPurposeId().intValue() == 27) {
					SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_GOLD);
					ajaxPostUrl = Constants.GOLD_LOAN_ACTION;
					loanActionType = Constants.GOLD_LOAN_ACTION;
					appPLTypeId = Constants.APP_PL_TYPE_GOLD;
				}
				SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
				if (applicationPL.getAppDownloadPdfFileName() != null) {
					SecureRandom rand = new SecureRandom();
					pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
							+ Constants.CONTEXT + loanActionType + "?generatePDF="
							+ applicationPL.getAppReferenceId() + rand.nextInt(1000)
							+ ((uiType == null) ? "" : ("&uiType=" + uiType));
				} else {
					pdfDownloadUrl = null;
				}

				CRMNextLog crmResponse = commonService.getCRMLeadIdByReferenceID(appReferenceId);
				if (crmResponse.getCrmLeadId() != null) {
					Integer leadSourceKey = commonService.getLeadSourceKey(applicationPL);
					logger.info("leadSourceKey " + leadSourceKey);
					String sourceId = getSourceIdOnLeadSourceKey(leadSourceKey);
					crmFetchLeadStatusApi.fetchLeadStatusRequest(appReferenceId, Constants.PERSONAL_LOAN_ID, crmResponse.getCrmLeadId(), sourceId);
					CRMFetchLeadStatus crmFetchStatus = commonService.getLeadByReferenceID(appReferenceId);
					if(crmFetchStatus == null || crmFetchStatus.getResponseStatus().equals("F")) {
						LoanStatus status = commonService
								.getLoanStatusByLoanStatusId(applicationPL.getAppLoanStatusId());
						if (status != null)
							statusDetails = status.getLoanStatusCustomerTitle();
					} else {
						statusDetails = crmFetchStatus.getResponseDescription();
					}					
				} else {
					LoanStatus status = commonService
							.getLoanStatusByLoanStatusId(applicationPL.getAppLoanStatusId());
					if (status != null)
						statusDetails = status.getLoanStatusCustomerTitle();
				}
				
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_EL)) {
				loanTypeId = Integer.valueOf(4);
				if (trackMobile != null && trackIsdCode != null) {
					applicationEL = educationLoanService
							.getApplicationFormEducationLoanByAppReferenceIdAndMobileNo(appReferenceId,
									String.valueOf(trackIsdCode) + trackMobile);
					//logger.info("ApplicationStatusAction.java applicationHL..... " + applicationHL
					//		+ "..appReferenceId.." + appReferenceId);

					//logger.info("otherRequest EL : " + otherRequest);
					SessionUtil.setTrackerChecker(appBtn);
					if (otherRequest != null) {
						if (applicationEL != null) {

							logger.info("inside the trackmobile and trackIsdCode condition   applicationEL!=null");
							json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst, appReferenceId,
									trackMobile, trackIsdCode, requestIndex, null, loanTypeId);

							responseMessage = "success|" + json.getString("message");
							return "jsonResponsePage";

						} else {
							logger.info("inside else condition applicationEL is null");
							responseMessage = "error|Invalid application reference no or mobile no.";
							return "jsonResponsePage";
						}
					}

				} else if (appReferenceId != null) {
					applicationEL = educationLoanService.getApplicationFormEducationLoanByAppReferenceId(appReferenceId);
					
					logger.info("EducationProcessImpl.java Line 360   *************after  save method  autoLoanService file" + applicationEL.getAppReferenceId());

					FstoreDoc doc = new FstoreDoc();

					Integer bi = applicationEL.getAppBranchId();
					Integer branchCode1 = commonService.getBranchCodeByBranchId(bi);
					doc.setAppPhotoIdName(applicationEL.getAppPhotoIdName());
					doc.setAppIdentityProofName(applicationEL.getAppIdentityProofName());
					doc.setAppResidenceProofName(applicationEL.getAppResidenceProofName());
					doc.setAppIncomeProofName(applicationEL.getAppIncomeProofName());
					doc.setAppAcademicProofName(applicationEL.getAppAcademicProofName());
					logger.info("AutoProcessImpl.java line ApplicationFormAutoLoan  460  FstoreDoc @@@@@@@@@@@@@@@@@ @@@  FstoreDoc..... " + doc.toString());
					
					edmsServiceAction.uploadDocumentsToEDMS(doc, 4, appReferenceId, branchCode1);
					// end code for edms
					
				}
				if (applicationEL == null) {
					if (!ValidatorUtil.isValid(uiType)) {
						responseMessage = "Invalid application reference no or mobile no.";
						return "applicationTrackError" + ((uiType == null) ? "" : uiType);
					}
					responseMessage = "error|Invalid application reference no or mobile no.";
					return "jsonResponsePage";
				}
				appSeqId = applicationEL.getAppSeqId();
				quoteEL = educationLoanService
						.getApplicationFormEducationLoanQuoteByQuoteId(applicationEL.getAppQuoteId());

				String loanActionType = "";
				/*if (Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID
						.equals(quoteEL.getLoanQuoteLoanPurposeId())) {
					SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_BIDYALAKHMI);
					ajaxPostUrl = Constants.EDUCATION_LOAN_BIDYALAKHMI_ACTION;
					loanActionType = Constants.EDUCATION_LOAN_BIDYALAKHMI_ACTION;
					appElTypeId = Constants.APP_EL_TYPE_ID_BIDYALAKHMI;
				} else*/ if (Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID
						.equals(quoteEL.getLoanQuoteLoanPurposeId())) {
					SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_TAKE_OVER);
					ajaxPostUrl = Constants.EDUCATION_LOAN_TAKEOVER_ACTION;
					loanActionType = Constants.EDUCATION_LOAN_TAKEOVER_ACTION;
					appElTypeId = Constants.APP_EL_TYPE_ID_TAKE_OVER;
				} else if (Constants.APP_EL_TYPE_ID_SCHOLAR.equals(quoteEL.getLoanQuoteCountryOfStudyId())) {
					SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_SCHOLAR);
					ajaxPostUrl = Constants.SCHOLAR_LOAN_ACTION;
					loanActionType = Constants.SCHOLAR_LOAN_ACTION;
					appElTypeId = Constants.APP_EL_TYPE_ID_SCHOLAR;
				} else if (Constants.APP_EL_TYPE_ID_EDVANTAGE.equals(quoteEL.getLoanQuoteCountryOfStudyId())) {
					ajaxPostUrl = Constants.GLOBAL_EDVANTAGE_ACTION;
					SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_EDVANTAGE);
					loanActionType = Constants.GLOBAL_EDVANTAGE_ACTION;
					appElTypeId = Constants.APP_EL_TYPE_ID_EDVANTAGE;
				}
				SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
				generateUIBeanList(applicationEL);
				educationLoanPage = 1;
				if (applicationEL.getAppDownloadPdfFileName() != null) {
					SecureRandom rand = new SecureRandom();
					pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
							+ Constants.CONTEXT + loanActionType + "?generatePDF="
							+ applicationEL.getAppReferenceId() + rand.nextInt(1000)
							+ ((uiType == null) ? "" : ("&uiType=" + uiType));
				} else {
					pdfDownloadUrl = null;
				}
				
				CRMNextLog crmResponse = commonService.getCRMLeadIdByReferenceID(appReferenceId);
				if (crmResponse.getCrmLeadId() != null) {
					Integer leadSourceKey = commonService.getLeadSourceKey(applicationEL);
					logger.info("leadSourceKey " + leadSourceKey);
					String sourceId = getSourceIdOnLeadSourceKey(leadSourceKey);
					crmFetchLeadStatusApi.fetchLeadStatusRequest(appReferenceId, Constants.EDUCATION_LOAN_ID, crmResponse.getCrmLeadId(), sourceId);
					CRMFetchLeadStatus crmFetchStatus = commonService.getLeadByReferenceID(appReferenceId);
					if(crmFetchStatus == null || crmFetchStatus.getResponseStatus().equals("F")) {
						LoanStatus status = commonService
								.getLoanStatusByLoanStatusId(applicationEL.getAppLoanStatusId());
						if (status != null)
							statusDetails = status.getLoanStatusCustomerTitle();
					} else {
						statusDetails = crmFetchStatus.getResponseDescription();
					}
				} else {
					LoanStatus status = commonService
							.getLoanStatusByLoanStatusId(applicationEL.getAppLoanStatusId());
					if (status != null)
						statusDetails = status.getLoanStatusCustomerTitle();
				}				
				
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_AGL)) {

				//int maxKeySize = javax.crypto.Cipher.getMaxAllowedKeyLength("AES");

				try {
					loanTypeId = Integer.valueOf(15);
					ajaxPostUrl = Constants.AGRI_LOAN_ACTION;
					if (trackMobile != null && trackIsdCode != null) {
						applicationAGL = agriLoanService
								.getApplicationFormAgriLoanByAppReferenceIdAndMobileNo(appReferenceId,
										String.valueOf(trackIsdCode) + trackMobile);

						logger.info("ApplicationStatusAction.java applicationHL..... " + applicationAGL
								+ "..appReferenceId.." + appReferenceId);

						
						//logger.info("otherRequest : " + otherRequest);
						SessionUtil.setTrackerChecker(appBtn);
						if (otherRequest != null) {
							if (applicationAGL != null) {

								logger.info("inside the trackmobile and trackIsdCode condition   applicationAGL!=null");
								json = requestWantUsToCallYou(SessionUtil.getLeadId(), reqst,
										appReferenceId, trackMobile, trackIsdCode, requestIndex,
										null, loanTypeId);

								responseMessage = "success|" + json.getString("message");
								return "jsonResponsePage";

							} else {
								logger.info("inside else condition applicationPL is null");
								responseMessage = "error|Invalid application reference no or mobile no.";
								return "jsonResponsePage";
							}
						}

					} else if (appReferenceId != null) {
						applicationAGL = agriLoanService
								.getApplicationFormAgriLoanByAppReferenceId(appReferenceId);
					}
					if (applicationAGL == null) {
						if (!ValidatorUtil.isValid(uiType)) {
							responseMessage = "Invalid application reference no or mobile no.";
							return "applicationTrackError" + ((uiType == null) ? "" : uiType);
						}
						responseMessage = "error|Invalid application reference no or mobile no.";
						return "jsonResponsePage";
					}
					appSeqId = applicationAGL.getAppSeqId();
					SessionUtil.setAgriLoanApplicationSequenceId(appSeqId);
					agriLoanPage = 1;
					if (applicationAGL.getAppDownloadPdfFileName() != null) {
						SecureRandom rand = new SecureRandom();
						pdfDownloadUrl = String.valueOf(Constants.PROTOCOL) + Constants.IP_URL_INTERNET
								+ Constants.CONTEXT + Constants.AGRI_LOAN_ACTION + "?generatePDF="
								+ applicationAGL.getAppReferenceId() + rand.nextInt(1000)
								+ ((uiType == null) ? "" : ("&uiType=" + uiType));
					} else {
						pdfDownloadUrl = null;
					}

					CRMNextLog crmResponse = commonService.getCRMLeadIdByReferenceID(appReferenceId);
					if (crmResponse.getCrmLeadId() != null) {
						Integer leadSourceKey = commonService.getLeadSourceKey(applicationAGL);
						logger.info("leadSourceKey " + leadSourceKey);
						String sourceId = getSourceIdOnLeadSourceKey(leadSourceKey);
						crmFetchLeadStatusApi.fetchLeadStatusRequest(appReferenceId, Constants.AGRI_LOAN_ID, crmResponse.getCrmLeadId(), sourceId);
						CRMFetchLeadStatus crmFetchStatus = commonService.getLeadByReferenceID(appReferenceId);
						logger.info("crmFetchStatus is " + crmFetchStatus);
						if(crmFetchStatus == null || crmFetchStatus.getResponseStatus().equals("F")) {
							LoanStatus status = commonService.getLoanStatusByLoanStatusId(applicationAGL.getAppLoanStatusId());
							if (status != null)
								statusDetails = status.getLoanStatusCustomerTitle();
						} else {
							statusDetails = crmFetchStatus.getResponseDescription();
						}
					} else {
						LoanStatus status = commonService.getLoanStatusByLoanStatusId(applicationAGL.getAppLoanStatusId());
						if (status != null)
							statusDetails = status.getLoanStatusCustomerTitle();
					}
					
					ApplicationFormAgriLoanQuote quoteAGL = agriLoanService
							.getApplicationFormAgriLoanQuoteByQuoteId(applicationAGL.getAppQuoteId());
					Set<Integer> masterAgriProducts = new HashSet<>(5, 1.0F);
					if (applicationAGL.getAppLoanStatusId1() != null
							&& applicationAGL.getAppLoanStatusId1()
									.equals(Integer.valueOf(Constants.CALL_LOGS_MESSAGE_STATE1_ID))
							&& applicationAGL.getAppProductVariantId1() != null) {
						MasterAgriProduct masterAgriProduct = agriLoanService
								.getAgriProductVariantById(applicationAGL.getAppProductVariantId1());
						if (masterAgriProduct != null) {
							masterAgriProducts.add(applicationAGL.getAppProductVariantId1());
							loanPurposeNameAGL1 = masterAgriProduct.getAglProductName();
						}
					}
					/*
					 * if (applicationAGL.getAppLoanStatusId2() != null &&
					 * applicationAGL.getAppLoanStatusId2().equals(Integer.valueOf(Constants.
					 * CALL_LOGS_MESSAGE_STATE1_ID)) &&
					 * applicationAGL.getAppProductVariantId2() != null) { MasterAgriProduct
					 * masterAgriProduct =
					 * agriLoanService.getAgriProductVariantById(applicationAGL.
					 * getAppProductVariantId2()); if (masterAgriProduct != null) {
					 * masterAgriProducts.add(applicationAGL.getAppProductVariantId2());
					 * loanPurposeNameAGL2 = masterAgriProduct.getAglProductName(); } } if
					 * (applicationAGL.getAppLoanStatusId3() != null &&
					 * applicationAGL.getAppLoanStatusId3().equals(Integer.valueOf(Constants.
					 * CALL_LOGS_MESSAGE_STATE1_ID)) &&
					 * applicationAGL.getAppProductVariantId3() != null) { MasterAgriProduct
					 * masterAgriProduct =
					 * agriLoanService.getAgriProductVariantById(applicationAGL.
					 * getAppProductVariantId3()); if (masterAgriProduct != null) {
					 * masterAgriProducts.add(applicationAGL.getAppProductVariantId3());
					 * loanPurposeNameAGL3 = masterAgriProduct.getAglProductName(); } } if
					 * (applicationAGL.getAppLoanStatusId4() != null &&
					 * applicationAGL.getAppLoanStatusId4().equals(Integer.valueOf(Constants.
					 * CALL_LOGS_MESSAGE_STATE1_ID)) &&
					 * applicationAGL.getAppProductVariantId4() != null) { MasterAgriProduct
					 * masterAgriProduct =
					 * agriLoanService.getAgriProductVariantById(applicationAGL.
					 * getAppProductVariantId4()); if (masterAgriProduct != null) {
					 * masterAgriProducts.add(applicationAGL.getAppProductVariantId4());
					 * loanPurposeNameAGL4 = masterAgriProduct.getAglProductName(); } }
					 */
					totalLoanAmount = 0.0D;
					Iterator<Integer> it = masterAgriProducts.iterator();
					while (it.hasNext()) {
						Integer lpId = it.next();
						/*
						 * if ((lpId != null && lpId.intValue() == 1) || lpId.intValue() == 10 ||
						 * lpId.intValue() == 11) { totalLoanAmount +=
						 * quoteAGL.getLoanQuoteTractorLoanAmount().doubleValue(); continue; }
						 */
						if (lpId != null && lpId.intValue() == 2) {
							totalLoanAmount += quoteAGL.getLoanQuoteHarvesterLoanAmount().doubleValue();
							continue;
						}
						/*
						 * if (lpId != null && lpId.intValue() == 3) { totalLoanAmount +=
						 * quoteAGL.getLoanQuotePowrTlrLoanAmount().intValue(); continue; } if (lpId !=
						 * null && lpId.intValue() == 4) { totalLoanAmount +=
						 * quoteAGL.getLoanQuotePumpsetLoanAmountReq().intValue(); continue; } if (lpId
						 * != null && lpId.intValue() == 5) { totalLoanAmount +=
						 * quoteAGL.getLoanQuoteProduceMktLoanAmount().intValue(); continue; } if (lpId
						 * != null && lpId.intValue() == 6) { totalLoanAmount +=
						 * quoteAGL.getLoanQuoteKisanCreditcardLoanAmount().doubleValue(); continue; }
						 * if (lpId != null && lpId.intValue() == 7) { totalLoanAmount +=
						 * quoteAGL.getLoanQuoteAssetBckdAmountReq().intValue(); continue; } if (lpId !=
						 * null && lpId.intValue() == 8) { totalLoanAmount +=
						 * quoteAGL.getLoanQuoteDairyAmountRequired().intValue(); continue; } if (lpId
						 * != null && lpId.intValue() == 9) totalLoanAmount +=
						 * quoteAGL.getLoanQuoteContrFarmLoanAmount().intValue();
						 */
					}

				} catch (Exception e) {
      				logger.info("inside catch block LNo : 879 ", e);
				}
			} else {
				if (!ValidatorUtil.isValid(uiType)) {
					if (appBtn != null && appBtn.equals("CALLBACK")) {
						responseMessage = "error|Invalid application reference no or mobile no.";
						return "jsonResponsePage";
					}
					responseMessage = "Invalid application reference no or mobile no.";
					return "applicationTrackError" + ((uiType == null) ? "" : uiType);
				}
				responseMessage = "error|Either mobile no or application reference no not valid";
				return "jsonResponsePage";
			}
			if (jsonJSArrayThankyou == null)
				jsonJSArrayThankyou = SbiUtil.populateJSValidation(Integer.valueOf(18), Constants.HOME_LOAN_ID)
						.toString();
			if (loanTypeId != null && Constants.CREDIT_CARD_ID.equals(loanTypeId)) {
				documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.CREDIT_CARD_ID,
						Integer.valueOf(1));
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_HL)) {
				if (applicationHL.getAppHomeLoanId() != null
						&& (applicationHL.getAppHomeLoanId().intValue() == 9
								|| applicationHL.getAppHomeLoanId().intValue() == 10
								|| applicationHL.getAppHomeLoanId().intValue() == 11
								|| applicationHL.getAppHomeLoanId().intValue() == 12)) {
					documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.HOME_LOAN_ID,
							Integer.valueOf(1));
				} else {
					documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.HOME_LOAN_ID,
							applicationHL.getAppHomeLoanId());
				}
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_AL)) {
				documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.AUTO_LOAN_ID,
						applicationAL.getAppAutoLoanId());
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_PL)) {
				if ((applicationPL.getAppPersonalLoanId() != null
						&& applicationPL.getAppPersonalLoanId().intValue() == 6)
						|| applicationPL.getAppPersonalLoanId().intValue() == 7
						|| applicationPL.getAppPersonalLoanId().intValue() == 8) {
					documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID,
							Integer.valueOf(6));
				} else if ((applicationPL.getAppPersonalLoanId() != null
						&& applicationPL.getAppPersonalLoanId().intValue() == 9)
						|| applicationPL.getAppPersonalLoanId().intValue() == 10) {
					documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID,
							Integer.valueOf(2));
				} else {
					documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID,
							applicationPL.getAppPersonalLoanId());
				}
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_EL)) {
				logger.info("applicationEL.getAppEducationLoanId() application tracker --- 1260 --"+applicationEL.getAppEducationLoanId());


				documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.EDUCATION_LOAN_ID,
						Integer.valueOf(applicationEL.getAppEducationLoanId().intValue()));
			} else if (appReferenceId.startsWith(Constants.INTIAL_STRING_AGL)) {
				documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.AGRI_LOAN_ID,
						Integer.valueOf(1));                
			}
			if (documentTypeList == null) {
				if (!ValidatorUtil.isValid(uiType)) {
					responseMessage = "Invalid document list.";
					return "applicationTrackError" + ((uiType == null) ? "" : uiType);
				}
				responseMessage = "error|Invalid document list.";
				return "jsonResponsePage";
			}
			if (requestIndex.intValue() == 2)
				return "applicationDetailsWithHeader" + ((uiType == null) ? "" : uiType);
			if (requestIndex.intValue() == 1)
				return "applicationDetails" + ((uiType == null) ? "" : uiType);
		}
		return "success" + ((uiType == null) ? "" : uiType);
	}

	private String getSourceIdOnLeadSourceKey(Integer leadSourceKey) {
		String sourceId = new String();
		switch(leadSourceKey) {
			case 7 : sourceId = "OCAS";
				break;
			case 91 : sourceId = "OCS";
					break;
			case 92 : sourceId = "OCM";
					break;
			case 126 : sourceId = "OCU";     
					break;
			case 127 : sourceId = "OCCB";
					break;
			case 128 : sourceId = "OCW";
					break;
			case 129 : sourceId = "OCWD";
					break;
			case 10 : sourceId = "OCBW";
					break;
			default : sourceId = "OCAS";
		}
		return sourceId;
	}

	private void generateUIBeanList(ApplicationFormHomeLoan appForm) {
		try {
			if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.HOME_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.HOME_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppDistrictId() != null
						&& appForm.getAppDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistricts(mapDistrict);
				}
				beanList.setCitiesoptgrp1(mapCity);
			}
			if (appForm.getAppOfficeStateId() != null && appForm.getAppOfficeStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.HOME_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.HOME_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppOfficeDistrictId() != null
						&& appForm.getAppOfficeDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictsOffice(mapDistrict);
				}
				beanList.setCitiesoptgrp1Office(mapCity);
			}
			if (appForm.getAppPickupStateId() != null && appForm.getAppPickupStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.HOME_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.HOME_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppPickupCityId() != null
						&& appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictPickup(mapDistrict);
				}
				beanList.setCitiesoptgrp1Pickup(mapCity);
			}
    } catch (NullPointerException e) {
        logger.info("ApplicationStatusAction.java LNo : 389  : generateUIBeanList()", e);
     } catch (Exception e) {
			logger.info("ApplicationStatusAction.java LNo : 389  : generateUIBeanList()", e);
		}
	}

	private void generateUIBeanList(ApplicationFormAutoLoan appForm) {
		try {
			if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.AUTO_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.AUTO_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppDistrictId() != null
						&& appForm.getAppDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistricts(mapDistrict);
				}
				beanList.setCitiesoptgrp1(mapCity);
			}
			if (appForm.getAppOfficeStateId() != null && appForm.getAppOfficeStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.AUTO_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.AUTO_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppOfficeDistrictId() != null
						&& appForm.getAppOfficeDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictsOffice(mapDistrict);
				}
				beanList.setCitiesoptgrp1Office(mapCity);
			}
			if (appForm.getAppPickupStateId() != null && appForm.getAppPickupStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.AUTO_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3), Constants.AUTO_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppPickupCityId() != null
						&& appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictPickup(mapDistrict);
				}
				beanList.setCitiesoptgrp1Pickup(mapCity);
			}
    } catch (NullPointerException e) {
        logger.info("ApplicationStatusAction.java LNo : 458  : generateUIBeanList()", e);
		} catch (Exception e) {
			logger.info("ApplicationStatusAction.java LNo : 458  : generateUIBeanList()", e);
		}
	}

	private void generateUIBeanList(ApplicationFormPersonalLoan appForm) {
		try {
			if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.PERSONAL_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppDistrictId() != null
						&& appForm.getAppDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistricts(mapDistrict);
				}
				beanList.setCitiesoptgrp1(mapCity);
			}
			if (appForm.getAppOfficeStateId() != null && appForm.getAppOfficeStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.PERSONAL_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.PERSONAL_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null,
						null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppOfficeDistrictId() != null
						&& appForm.getAppOfficeDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictsOffice(mapDistrict);
				}
				beanList.setCitiesoptgrp1Office(mapCity);
			}
			if (appForm.getAppPickupStateId() != null && appForm.getAppPickupStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.PERSONAL_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.PERSONAL_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null,
						null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppPickupCityId() != null
						&& appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictPickup(mapDistrict);
				}
				beanList.setCitiesoptgrp1Pickup(mapCity);
			}
    } catch (NullPointerException e) {
        logger.info("ApplicationStatusAction.java LNo : 529  : generateUIBeanList()", e);
		} catch (Exception e) {
			logger.info("ApplicationStatusAction.java LNo : 529  : generateUIBeanList()", e);
		}
	}

	private void generateUIBeanList(ApplicationFormEducationLoan appForm) {
		try {
			if (appForm.getAppStateId() != null && appForm.getAppStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.EDUCATION_LOAN_ID,
						appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.EDUCATION_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppDistrictId() != null
						&& appForm.getAppDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistricts(mapDistrict);
				}
				beanList.setCitiesoptgrp1(mapCity);
			}
			if (appForm.getAppOfficeStateId() != null && appForm.getAppOfficeStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.EDUCATION_LOAN_ID,
						appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.EDUCATION_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null,
						null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppOfficeDistrictId() != null
						&& appForm.getAppOfficeDistrictId().intValue() > 0) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictsOffice(mapDistrict);
				}
				beanList.setCitiesoptgrp1Office(mapCity);
			}
			if (appForm.getAppPickupStateId() != null && appForm.getAppPickupStateId().intValue() > 0) {
				Map<Integer, String> mapCity = new LinkedHashMap<>();
				mapCity = commonService.getStateCityDistrictBranch(Integer.valueOf(2), Constants.EDUCATION_LOAN_ID,
						appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<>();
				mapDistrict = commonService.getStateCityDistrictBranch(Integer.valueOf(3),
						Constants.EDUCATION_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null,
						null);
				if (mapDistrict != null && !mapDistrict.isEmpty() && appForm.getAppPickupCityId() != null
						&& appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					beanList.setDistrictPickup(mapDistrict);
				}
				beanList.setCitiesoptgrp1Pickup(mapCity);
			}
    } catch (NullPointerException e) {
        logger.info("ApplicationStatusAction.java LNo : 600  : generateUIBeanList()", e);
		} catch (Exception e) {
			logger.info("ApplicationStatusAction.java LNo : 600  : generateUIBeanList()", e);
		}
	}

  private JSONObject requestWantUsToCallYou(Integer reqstId, RequestACallBack reqst, String appReferenceId, String trackMobile, String trackIsdCode, Integer requestIndex, String inputOtp, Integer loanTypeId) {
		JSONObject json = new JSONObject();
		 if(inputOtp !=null) {
	        	SbiUtil sbiutil=new SbiUtil();
	        	//logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
				//for otp decrypt  in sep 2023
	        	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
			}
	       // logger.info("DecryptedRequest inputOtp   1023  "+inputOtp);
		
		try {
			Integer alertCount = Integer.valueOf(0);
			if (requestIndex != null && (requestIndex.intValue() == 1 || requestIndex.intValue() == 4)) {
				if (requestIndex.intValue() == 1) {
					if (reqst.getRqstStatusId() != null && reqst.getRqstStatusId().intValue() == 403
							&& reqst.getTrackMobile() != null
							&& "Y".equalsIgnoreCase(reqst.getRqstMobileVerificationCodeVerified())) {
						json.put("status", "error");
						json.put("message",
								"You have already made request to call you with number " + reqst.getTrackMobile());
						json.put("alertCount", alertCount);
						return json;
					}
					int count = 0;
				//	logger.info("ApplicationStatusAction.java LNo : 932 count : " + count);
					reqst.setAppReferenceId(appReferenceId);
					reqst.setRqstActive("Y");
					reqst.setRqstDeleted("N");
					reqst.setRqstISDCode(trackIsdCode);
					reqst.setTrackMobile(trackMobile);
					reqst.setRqstEntryDate(new Date());
					reqst.setRqstEntryTime(new Date());
					reqst.setRqstMobileVerificationCode(SbiUtil.getVerificationCode(reqst.getTrackMobile()));
					reqst.setRqstMobileVerificationCodeSendTime(new Date());
					reqst.setRqstMobileVerificationCodeVerified("N");
					reqst.setRqstStatusId(Integer.valueOf(405));
					reqst.setRqstLoanTypeId(loanTypeId);
				//	logger.info("mobile SEND OTP ::" + reqst.getRqstMobileVerificationCode());
					try {
						reqst = commonService.save(reqst);
      			    } catch (SQLException e) {
						logger.error("ApplicationStatusAction.java LNo : 950 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					SessionUtil.setRqstId(reqst.getRqstCallId());
				}
				if (requestIndex.intValue() == 4) {
					reqst = commonService.getRqstIdById(SessionUtil.getRqstId());
					if (reqst == null) {
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCount", 1);
						return json;
					}
					alertCount = Integer.valueOf(
							(reqst.getRqstOtpResendCount() == null) ? 0 : reqst.getRqstOtpResendCount().intValue());
					if (alertCount.intValue() >= 5) {
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
						json.put("alertCount", alertCount);
						return json;
					}
					json.put("status", "success");
					json.put("message", "OTP Code sent");
					json.put("alertCount", alertCount);
					reqst.setRqstMobileVerificationCode(SbiUtil.getVerificationCode(reqst.getTrackMobile()));
					logger.info("mobile RESEND OTP ::" + reqst.getRqstMobileVerificationCode());
					alertCount = Integer.valueOf(alertCount.intValue() + 1);
					reqst.setRqstOtpResendCount(alertCount);
					try {
						reqst = commonService.save(reqst);
						json.put("status", "success");
						json.put("message", "OTP Code sent");
						json.put("alertCount", alertCount);
        		  } catch (SQLException e) {
						logger.error("ApplicationStatusAction.java LNo : 987 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCount", 1);
						return json;
					}
				}
				if (reqst.getRqstMobileVerificationCodeReceived() == null || requestIndex.intValue() == 4) {
					
					String msgBody = communicationManagerImpl.setEmailBody(Integer.valueOf(0), Integer.valueOf(0),
							Constants.MESSAGE_TYPE_SMS, Integer.valueOf(0));
					msgBody = SbiUtil.urlEncode(msgBody);
					
					String SMS_TEXT = null;
					SMS_TEXT = Constants.SMS_STRING_INDIAN;
					SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
					SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", reqst.getRqstMobileVerificationCode().toString());
					SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
							(String.valueOf(reqst.getRqstISDCode()) + reqst.getTrackMobile()).trim());
					
					if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
						logger.info("OTP for Mobile Number: " + (String.valueOf(reqst.getRqstISDCode()) + reqst.getTrackMobile()).trim() + " is " + reqst.getRqstMobileVerificationCode().toString());
					}
					boolean smsSendStatus = communicationManagerImpl.sendSms(SMS_TEXT);
					logger.info("ApplicationStatusAction.java LNo : 1017 : " + smsSendStatus);
					
					if (!smsSendStatus) {
						json.put("status", "error");
						json.put("message", "OTP service is down");
						return json;
					}
					
					reqst.setRqstMobileVerificationCodeReceived("Y");
					try {
						reqst = commonService.save(reqst);
    		      } catch (SQLException e) {
						logger.error("ApplicationStatusAction.java LNo : 1013 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCountConfrm", alertCount);
						return json;
					}
				}
				json.put("status", "success");
				json.put("message", "OTP Code sent");
				json.put("alertCount", alertCount);
				return json;
			}
			if (requestIndex != null && requestIndex.intValue() == 5) {
				if (!ValidatorUtil.isValid(inputOtp)) {
					json.put("status", "error");
					json.put("message", "Please enter valid OTP.");
					json.put("alertCountConfrm", 0);
					return json;
				}
				alertCount = Integer.valueOf((reqst.getRqstOtpMobileAlertsCount() == null) ? 0
						: reqst.getRqstOtpMobileAlertsCount().intValue());
				if (alertCount.intValue() == 5) {
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
					json.put("alertCountConfrm", alertCount);
					return json;
				}
				alertCount = Integer.valueOf(alertCount.intValue() + 1);
				reqst.setRqstOtpMobileAlertsCount(alertCount);
				try {
					reqst = commonService.save(reqst);
 		       } catch (SQLException e) {
					logger.error("ApplicationStatusAction.java LNo : 1041 : Exception Caught", e);
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					json.put("alertCountConfrm", alertCount);
					return json;
				}
				if (reqst.getRqstMobileVerificationCode() != null
						&& reqst.getRqstMobileVerificationCode().toString().equalsIgnoreCase(inputOtp)) {
					reqst.setRqstMobileVerificationCodeVerified("Y");
					reqst.setRqstStatusId(Integer.valueOf(403));

					SessionUtil.setLeadId(null);
					try {
						reqst = commonService.save(reqst);
 			         } catch (SQLException e) {
						logger.error("ApplicationStatusAction.java LNo : 1053 : Exception Caught", e);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCountConfrm", alertCount);
						return json;
					}
					logger.info("ApplicationStatusAction LNO ::1059 , log for CALLBACK_SMS_CONSENT "
							+ Constants.CALLBACK_SMS_CONSENT);
					logger.info("ApplicationStatusAction LNO ::16789 , SessionUtil.getTrackerChecker() is "
							+ SessionUtil.getTrackerChecker());
					if (SessionUtil.getTrackerChecker().equals("CALLBACK")) {
						logger.info("ApplicationStatusAction LNO ::1061 , log for CALLBACK_SMS_CONSENT "
								+ reqst.getRqstMobileVerificationCodeVerified());
						if ("Y".equalsIgnoreCase(reqst.getRqstMobileVerificationCodeVerified())) {
							
							String msgBody = communicationManagerImpl.setEmailBody(Integer.valueOf(0),
									Integer.valueOf(0), Constants.MESSAGE_TYPE_SMS, Integer.valueOf(1));
							msgBody = SbiUtil.urlEncode(msgBody);
							
							String SMS_TEXT = null;
							SMS_TEXT = Constants.SMS_STRING_INDIAN;
							SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
							SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE",
									(String.valueOf(reqst.getRqstISDCode()) + reqst.getTrackMobile()).trim());
							
							if (!Constants.DEPLOYMENT_MODE.equals("live")) {
								logger.info("OTP for Mobile Number: " + (String.valueOf(reqst.getRqstISDCode()) + reqst.getTrackMobile()).trim() + " is " + reqst.getRqstMobileVerificationCode().toString());
							}
							
							if (!communicationManagerImpl.sendSms(SMS_TEXT)) {
								logger.info("ApplicationStatusAction LNO ::1072 , OTP service is down:: msg not send");
								json.put("status", "error");
								json.put("message", "OTP service is down");
								return json;
							}
						}
					}
					json.put("status", "success");
					json.put("message", "Thank you for your interest. Our representative will contact you shortly.");
					return json;
				}
				logger.info("OTP is incorrect for mobileNo ::" + reqst.getTrackMobile());
				json.put("status", "error");
				json.put("message", "OTP is incorrect! Try again.|trackInputOtpWantUs|2");
				json.put("alertCountConfrm", alertCount);
				return json;
			}
    } catch (JSONException e) {
        logger.info("ApplicationStatusAction.java LNo : 1092 : Exception Caught", e);
        try {
          json.put("status", "error");
          json.put("message", "OTP process is down");
          json.put("alertCount", 0);
        } catch (JSONException e1) {
          logger.info("ApplicationStatusAction.java LNo : 1098 : Exception Caught", (Throwable)e1);
        } 
		} catch (Exception e) {
			logger.info("ApplicationStatusAction.java LNo : 1092 : Exception Caught", e);
			try {
				json.put("status", "error");
				json.put("message", "OTP process is down");
				json.put("alertCount", 0);
			} catch (JSONException e1) {
				logger.info("ApplicationStatusAction.java LNo : 1098 : Exception Caught", (Throwable) e1);
			}
		}
		return json;
	}

	public String getAppReferenceId() {
		return this.appReferenceId;
	}

	public void setAppReferenceId(String appReferenceId) {
		this.appReferenceId = appReferenceId;
	}

	public ApplicationFormHomeLoan getApplicationHL() {
		return this.applicationHL;
	}

	public void setApplicationHL(ApplicationFormHomeLoan applicationHL) {
		this.applicationHL = applicationHL;
	}

	public ApplicationFormHomeLoanQuote getQuoteHL() {
		return this.quoteHL;
	}

	public void setQuoteHL(ApplicationFormHomeLoanQuote quoteHL) {
		this.quoteHL = quoteHL;
	}

	public ApplicationFormAutoLoan getApplicationAL() {
		return this.applicationAL;
	}

	public void setApplicationAL(ApplicationFormAutoLoan applicationAL) {
		this.applicationAL = applicationAL;
	}

	public ApplicationFormAutoLoanQuote getQuoteAL() {
		return this.quoteAL;
	}

	public void setQuoteAL(ApplicationFormAutoLoanQuote quoteAL) {
		this.quoteAL = quoteAL;
	}

	public ApplicationFormEducationLoan getApplicationEL() {
		return this.applicationEL;
	}

	public void setApplicationEL(ApplicationFormEducationLoan applicationEL) {
		this.applicationEL = applicationEL;
	}

	public ApplicationFormEducationLoanQuote getQuoteEL() {
		return this.quoteEL;
	}

	public void setQuoteEL(ApplicationFormEducationLoanQuote quoteEL) {
		this.quoteEL = quoteEL;
	}

	public ApplicationFormPersonalLoan getApplicationPL() {
		return this.applicationPL;
	}

	public void setApplicationPL(ApplicationFormPersonalLoan applicationPL) {
		this.applicationPL = applicationPL;
	}

	public ApplicationFormPersonalLoanQuote getQuotePL() {
		return this.quotePL;
	}

	public void setQuotePL(ApplicationFormPersonalLoanQuote quotePL) {
		this.quotePL = quotePL;
	}

	public String getTrackMobile() {
		return this.trackMobile;
	}

	public void setTrackMobile(String trackMobile) {
		this.trackMobile = trackMobile;
	}

	public String getTrackIsdCode() {
		return this.trackIsdCode;
	}

	public void setTrackIsdCode(String trackIsdCode) {
		this.trackIsdCode = trackIsdCode;
	}

	public String getPdfDownloadUrl() {
		return this.pdfDownloadUrl;
	}

	public void setPdfDownloadUrl(String pdfDownloadUrl) {
		this.pdfDownloadUrl = pdfDownloadUrl;
	}

	public String getStatusDetails() {
		return this.statusDetails;
	}

	public void setStatusDetails(String statusDetails) {
		this.statusDetails = statusDetails;
	}

	public ApplicationFormAgriLoan getApplicationAGL() {
		return this.applicationAGL;
	}

	public void setApplicationAGL(ApplicationFormAgriLoan applicationAGL) {
		this.applicationAGL = applicationAGL;
	}

	public ApplicationFormAgriLoanQuote getQuoteAGL() {
		return this.quoteAGL;
	}

	public void setQuoteAGL(ApplicationFormAgriLoanQuote quoteAGL) {
		this.quoteAGL = quoteAGL;
	}

	public ApplicationFormCreditCard getApplicationSCC() {
		return this.applicationSCC;
	}

	public void setApplicationSCC(ApplicationFormCreditCard applicationSCC) {
		this.applicationSCC = applicationSCC;
	}

	public String getLoanPurposeNameAGL1() {
		return this.loanPurposeNameAGL1;
	}

	public void setLoanPurposeNameAGL1(String loanPurposeNameAGL1) {
		this.loanPurposeNameAGL1 = loanPurposeNameAGL1;
	}

	public String getLoanPurposeNameAGL2() {
		return this.loanPurposeNameAGL2;
	}

	public void setLoanPurposeNameAGL2(String loanPurposeNameAGL2) {
		this.loanPurposeNameAGL2 = loanPurposeNameAGL2;
	}

	public String getLoanPurposeNameAGL3() {
		return this.loanPurposeNameAGL3;
	}

	public void setLoanPurposeNameAGL3(String loanPurposeNameAGL3) {
		this.loanPurposeNameAGL3 = loanPurposeNameAGL3;
	}

	public String getLoanPurposeNameAGL4() {
		return this.loanPurposeNameAGL4;
	}

	public void setLoanPurposeNameAGL4(String loanPurposeNameAGL4) {
		this.loanPurposeNameAGL4 = loanPurposeNameAGL4;
	}

	public MasterCardType getCardType() {
		return this.cardType;
	}

	public void setCardType(MasterCardType cardType) {
		this.cardType = cardType;
	}

	public String getAppCardReferenceNumber() {
		return this.appCardReferenceNumber;
	}

	public void setAppCardReferenceNumber(String appCardReferenceNumber) {
		this.appCardReferenceNumber = appCardReferenceNumber;
	}

	public double getTotalLoanAmount() {
		return this.totalLoanAmount;
	}

	public void setTotalLoanAmount(double totalLoanAmount) {
		this.totalLoanAmount = totalLoanAmount;
	}

	public String getCaptcha() {
		return this.captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getTrackInputOtpWantUs() {
		return this.trackInputOtpWantUs;
	}

	public void setTrackInputOtpWantUs(String trackInputOtpWantUs) {
		this.trackInputOtpWantUs = trackInputOtpWantUs;
	}

}
