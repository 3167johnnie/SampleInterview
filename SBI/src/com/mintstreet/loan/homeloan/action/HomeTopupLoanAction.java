package com.mintstreet.loan.homeloan.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.action.BaseAction;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.IntermediaryRel;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterLmsIntermediary;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.status.StatusManager;
import com.mintstreet.common.status.StatusManagerResponse;
import com.mintstreet.common.status.StatusRequest;
import com.mintstreet.common.util.AESEncryption;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.Security;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.homeloan.bo.impl.HomeProcessManagerImpl;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.homeloan.util.HomeLoanHelper;
import com.mintstreet.loan.product.entity.HlProduct;


public class HomeTopupLoanAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(HomeTopupLoanAction.class.getName());
	private static final long serialVersionUID = 1L;
	@Autowired
	private HomeLoanService homeLoanService;
	
	@Autowired
	private HomeLoanHelper homeLoanHelper;
	
	@Autowired
	private HomeProcessManagerImpl processManagerHomeImpl;
	@Autowired
	private AESEncryption aesEncryption;
	private ApplicationFormHomeLoan appForm;
	private ApplicationFormHomeLoanQuote quote;

	public JSONArray initLoanJSONArrayHomeLoan;
	public String jsonJSArray1HomeLoan;
	public static String jsonJSArray3HomeLoan;
	public static String jsonJSArray3HomeLoanTopup;
	public String tenure1Duration;
	public double tenure1Emi;
	public String tenure2Duration;
	public double tenure2Emi;
	public String tenure3Duration;
	public double tenure3Emi;
	public String tenure4Duration;
	public double tenure4Emi;
	private IntermediaryRel intermediaryRel = null;
	
	public String execute(){
		return SUCCESS;
	}
	
	public String homeTopUpLoan(){
		try{
			if(ValidatorUtil.isValid(uiType)){SessionUtil.setUiType(uiType);}else{SessionUtil.setUiType(null);}
			loanTypeId=Constants.HOME_TOP_UP_LOAN_ID;
			isDsrPage="false";
			ajaxPostUrl=Constants.HOME_TOP_UP_LOAN_ACTION;
			SessionUtil.setApplicationType(0);
			request=RequestUtil.getServletRequest();
			if(SessionUtil.getBankLMSUser()!=null){
				isOnlineAndDsrActive=true;
				releaseSession(Constants.HOME_TOP_UP_LOAN_ID);

				SessionUtil.setHomeLoanApplicationSequenceId(null);
				SessionUtil.setAutoLoanApplicationSequenceId(null);
				SessionUtil.setEducationLoanApplicationSequenceId(null);
				SessionUtil.setPersonalLoanApplicationSequenceId(null);
			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				if(appSeqId!=null){
					SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
					visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdHL(visitId);
				}
			}
			return getHomeLoan(Constants.HOME_LOAN_ID);
		} catch(NullPointerException e){
			logger.info("HomeTopupLoanAction.java LNo : 206 : homeLoan() ", e);
		} catch(Exception e){
			logger.info("HomeTopupLoanAction.java LNo : 206 : homeLoan() ", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String getHomeLoan(Integer moduleId){
		loanTypeId=Constants.HOME_TOP_UP_LOAN_ID;
		if(iPAddressForAppAndDBServerPass!=1){
			isValidIpAddressForAppAndDBServer();
		}
		if(iPAddressForAppAndDBServerPass==0){
			return "under-maintainance"+(uiType==null?"":uiType);
		}

		if(!UIBeanListStatic.isDataSet){
			setStaticData();
		}
		json = new JSONObject();
		try{
			homeLoanPage = 1;
			stateManagerBean=stateManager.getState(request, moduleId);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			if(SessionUtil.getVisitIdHL()!=null){
				visitId = SessionUtil.getVisitIdHL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId ==null ){
					if(SessionUtil.getHomeLoanTopupApplicationSequenceId()==null || moduleId==Constants.HOME_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.HOME_LOAN_ID );
						/*if (!(campaignCode == null && offerCode == null && trackingCode == null)) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.HOME_LOAN_ID, 0);
						}*/
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdHL(visitId);
						}else{
							logger.info("HomeTopupLoanAction.java LN 334 unable to insert into visit entity.");
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}
			if(stateManagerBean.getState()==-1){
				metaInfo.setTitle(Constants.HOME_LOAN_TITLE);
				metaInfo.setKeywords(Constants.HOME_LOAN_KEYWORDS);
				metaInfo.setDescription(Constants.HOME_LOAN_DESCRIPTION);
				browserver = CommonUtilites.getBrowserUserAgent();
				browser = CommonUtilites.getBrowserName();
				if(!ValidatorUtil.isValid(SessionUtil.getSelectedLanguage())){
					SessionUtil.setSelectedLanguage("English");
				}
			}
			
						
			
			if(stateManagerBean.getState()==28 || stateManagerBean.getState()==29){
				logger.info("state manager 28 and 29 is called");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					int appOTPVerifyType=0;
					if(otherRequest.getAppOTPVerifyType()!=null){
						appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType());
					}
					String inputOtp = null;
					if(otherRequest.getInputOtp()!=null){
						inputOtp = otherRequest.getInputOtp();
					}
					String userEmail = null;
					if(otherRequest.getUserEmail()!=null){
						userEmail = otherRequest.getUserEmail();
					}
					json = processManagerHomeImpl.processCBSOTP(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, 
							appOTPVerifyType, inputOtp, userEmail, SessionUtil.getHomeLoanTopupApplicationSequenceId(),
							SessionUtil.getHomeTopUpLoanCbsCallId(), 
							Constants.HOME_TOP_UP_LOAN_ACTION);
					
					if(json.get("status").toString().equalsIgnoreCase("success")){
						if(stateManagerBean.getState()==29){

							responseMessage = "success|"+json.getString("message");
							return "jsonResponsePage";
						}else{
							responseMessage = "success|"+json.getString("message");
							return "jsonResponsePage";
						}
					}else{
						responseMessage = "error|"+json.getString("message");
						return "jsonResponsePage";
					}
				}else{
					String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
					responseMessage = "error|"+msg;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==27){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					try{
						cbs = stateManagerBean.getCbs();
						if(isDsrPage.equalsIgnoreCase("false") && !cbs.getInfoprovideCBS().equals("on")){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						CBSCallResponse cbsCallResponse = processManagerHomeImpl.processCbsCall(appSeqId, requestIndex, cbs, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),visitId, null, Constants.HOME_TOP_UP_LOAN_ACTION, 5, null, null);
						cbs = commonService.getMasterCBSCallObjById(SessionUtil.getHomeTopUpLoanCbsCallId());
						if(cbsCallResponse.getStatus()!=null){
							if(cbsCallResponse.getStatus()==0){
								responseMessage = "error|"+cbsCallResponse.getResponseMsg();
								return "jsonResponsePage";
							}else if(cbsCallResponse.getStatus()==1){

								return "cbsOtpPage"+(uiType==null?"":uiType);
							}
						}
						
					} catch (NullPointerException e) {
						logger.info("HomeTopupLoanAction.java LN 184 Exception occured:::::",e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					} catch (Exception e) {
						logger.info("HomeTopupLoanAction.java LN 184 Exception occured:::::",e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					}
				}else{
					String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
					responseMessage = "error|"+msg;
					return "jsonResponsePage";
				}
				
			}

			if(stateManagerBean.getState()==26){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						releaseSession(Constants.HOME_TOP_UP_LOAN_ID);
						appSeqId=null;
						appLeadId=null;
						firstName=null;
						mobileNo=null;
						email=null;
						stateManagerBean.setState(-1);
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 343 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 343 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
			}
			

			if(stateManagerBean.getState()==25){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						
						appSeqId =SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(ValidatorUtil.isValid(appSeqId)){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())){
									String filePath = Constants.PDF_GENRATION_BASE_PATH +Constants.HL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
									inputStream = new FileInputStream(new File(filePath));
									return "downLoadPDF";
								}
							}
						}
						infoMessage=1;
						responseMessage = Constants.PDF_SESSION_TIMEOUT_REDIRECTION.replaceAll("CLICK_HERE","<a href='"+Constants.PORT+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"'>here</a>");
						stateManagerBean.setState(-1);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LNo : 368 : stateManagerBean.getState()==25 ", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LNo : 368 : stateManagerBean.getState()==25 ", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
			}
			

			if(stateManagerBean.getState()==24){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(SessionUtil.getHomeLoanTopupApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						}
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						appForm =homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
						return "dropOff"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 404 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 404 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
				
			}

			if(stateManagerBean.getState()==23){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						
						if(isDsrPage.equalsIgnoreCase("true")){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							
							getCallLogs(appSeqId, appLeadId);
						}
						return "callsLogDetails"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 426 stateManagerBean.getState()==23 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 426 stateManagerBean.getState()==23 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}

			if(stateManagerBean.getState()==22){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						logger.info("HomeTopupLoanAction.java LNo:329 :: appSeqId :: "+appSeqId+" imageNo :: "+imageNo+" ajaxPostUrl :"+ajaxPostUrl + "imageName :: "+imageName );
						json = processManagerHomeImpl.processDeleteProductImage(appSeqId, imageNo, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, imageName);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							return "jsonResponsePage";
						}else{
							responseMessage = "error|"+json.getString("message");
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==22 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==22 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}

				if(stateManagerBean.getState()==21){
					try{
						if(stateManagerBean.getValidatorResponse().isStatus()){
							appForm = stateManagerBean.getAppHL();
							if(isOnlineAndDsrActive){
								responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
								return "jsonResponsePage";
							}
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							if(appSeqId == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
								return "jsonResponsePage";
							}

							if(ValidatorUtil.isValid(appForm.getAppDocPickupDate())){
								appForm.setAppDocPickupDateDT(DateUtil.changeDateFormatToDate(appForm.getAppDocPickupDate(), "MM/dd/yyyy"));
							}
							
							if (appForm.getAppDocPickupDateDT()!=null) {
								String dateStatus = DateUtil.checkDateInRange(appForm.getAppDocPickupDateDT(),appForm.getAppDocPickupTimeString(), 14);
								if (dateStatus.equals("1")) {
									responseMessage = "error|"+"Date is not in correct range.|"+"appForm.appDocPickupDate|1";
									return "jsonResponsePage";
								}else if (dateStatus.equals("2") || dateStatus.equals("3")) {
									responseMessage = "error|"+"Pickup time is not in correct range.|"+"appForm.appDocPickupTimeString|1";
									return "jsonResponsePage";
								}
							}else {
								if (ValidatorUtil.isValid(appForm.getAppDocPickupTimeString())) {
									responseMessage = "error|"+"Please select the document pickup date.|"+"appForm.appDocPickupDate|1";
									return "jsonResponsePage";
								}
							}
							json = processManagerHomeImpl.processToDocumentPickupUploaded(appSeqId, appForm,(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
							if(json.get("status").toString().equalsIgnoreCase("success")){
								responseMessage = "success|"+json.getString("message");
								return "jsonResponsePage";
							}else{
								responseMessage = "error|"+json.getString("message");
								return "jsonResponsePage";
							}
						}else{
							String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
							responseMessage = "error|"+msg;
							return "jsonResponsePage";
						}
					} catch (NullPointerException e) {
						logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==21 ::", e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					} catch (Exception e) {
						logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==21 ::", e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					}
				}


				if(stateManagerBean.getState()==20){
					try{
						if(stateManagerBean.getValidatorResponse().isStatus()){
							
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							if(appSeqId == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
								return "jsonResponsePage";
							}
							
							appForm=homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							if(appForm.getAppDownloadPdfFileName()!=null){
								SecureRandom rand = new SecureRandom();
								responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+((moduleId == Constants.HOME_LOAN_ID.intValue())?Constants.HOME_TOP_UP_LOAN_ACTION:Constants.HOME_TOP_UP_LOAN_ACTION_DSR)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
							}else{
								responseMessage = "error|Please wait pdf generation in progress ...";
							}
							return "jsonResponsePage";
						}else{
							String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
							responseMessage = "error|"+msg;
							return "jsonResponsePage";
						}
					} catch (NullPointerException e) {
						logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==20 ::", e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					} catch (Exception e) {
						logger.info("HomeTopupLoanAction.java LN 459 stateManagerBean.getState()==20 ::", e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					}
				}
				
			if(stateManagerBean.getState()==19){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 543 stateManagerBean.getState()==19 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 543 stateManagerBean.getState()==19 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==18){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppHL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}

						if(ValidatorUtil.isValid(appForm.getAppDocPickupDate())){
							appForm.setAppDocPickupDateDT(DateUtil.changeDateFormatToDate(appForm.getAppDocPickupDate(), "MM/dd/yyyy"));
						}
						
						if (appForm.getAppDocPickupDateDT()!=null) {
							String dateStatus = DateUtil.checkDateInRange(appForm.getAppDocPickupDateDT(),appForm.getAppDocPickupTimeString(), 14);
							if (dateStatus.equals("1")) {
								responseMessage = "error|"+"Date is not in correct range.|"+"appForm.appDocPickupDate|1";
								return "jsonResponsePage";
							}else if (dateStatus.equals("2") || dateStatus.equals("3")) {
								responseMessage = "error|"+"Pickup time is not in correct range.|"+"appForm.appDocPickupTimeString|1";
								return "jsonResponsePage";
							}
						}else {
							if (ValidatorUtil.isValid(appForm.getAppDocPickupTimeString())) {
								responseMessage = "error|"+"Please select the document pickup date.|"+"appForm.appDocPickupDate|1";
								return "jsonResponsePage";
							}
						}
						json = processManagerHomeImpl.processToDocumentPickupUploaded(appSeqId, appForm, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							return "jsonResponsePage";
						}else{
							responseMessage = "error|"+json.getString("message");
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 580 stateManagerBean.getState()==18 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 580 stateManagerBean.getState()==18 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==17){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppHL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						appForm = processManagerHomeImpl.processSubmitQuote(appSeqId, stateManagerBean.getState(), appForm, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appForm.getError()!=null){
							responseMessage = "error|"+appForm.getError();
							return "jsonResponsePage";
						}
						homeLoanPage=4;
						quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
						generateUIBeanList(homeLoanPage);
						if(jsonJSArrayThankyou==null){
							jsonJSArrayThankyou = SbiUtil.populateJSValidation(18, moduleId).toString();
						}
						documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.HOME_LOAN_ID, appForm.getAppHomeLoanId() );

						if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) ){
							if(appForm.getAppFlexiPayDetails()!=null){
								try{
									String[] emiMessage = appForm.getAppFlexiPayDetails().split("\\|");
									tenure1Duration = emiMessage[0].split("=")[0];
									tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
									
									tenure2Duration = emiMessage[1].split("=")[0];
									tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
									
									tenure3Duration = emiMessage[2].split("=")[0];
									tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
									
									tenure4Duration = emiMessage[3].split("=")[0];
									tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
								} catch(NullPointerException e){
									logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								} catch(Exception e){
									logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								}
							}
						}
						HlProduct product =  commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
						productName = product.getHlProductName();
						productURl = product.getProductUrl();
						return "thankYouPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
						
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 631 stateManagerBean.getState()==17 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 631 stateManagerBean.getState()==17 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==14 || stateManagerBean.getState()==15 || stateManagerBean.getState()==16){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = null;
						if(stateManagerBean.getState()==15 || stateManagerBean.getState()==16 || stateManagerBean.getState()==14){
							otherRequest = stateManagerBean.getOtherRequest();
						}
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						json = processManagerHomeImpl.processMobileOTP(appSeqId, moduleId,stateManagerBean.getState(), (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl, otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 668 stateManagerBean.getState()==14, 15, 16 :: "+stateManagerBean.getState()+" ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 668 stateManagerBean.getState()==14, 15, 16 :: "+stateManagerBean.getState()+" ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==13){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest=stateManagerBean.getOtherRequest();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						String applyUrlData[] = null;
						String applyUrl =null;
						if(otherRequest.getAppliedLoanId()!=null){
							applyUrl = aesEncryption.decrypt(otherRequest.getAppliedLoanId().toString());
							if(applyUrl==null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							applyUrlData = applyUrl.split("\\|");
							if(applyUrlData!=null && applyUrlData.length<10){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						
						appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						boolean isAppFoundForDedupInDropOffStage = false;
						boolean isAppFoundForDedupInDropRejectStage = false;
						if (appSeqId == null) {

							if(appForm.getAppMobileNo()!=null && !Constants.DUMMY_MOBILE_NO.contains(appForm.getAppMobileNo()) && !Constants.APP_DUPLICATION_CHECK.equals("0")){

								boolean isAppFoundForDedupInApplicationStage = false;
								quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
								isAppFoundForDedupInApplicationStage = homeLoanService.isAppFoundForDedupInApplicationStage((appForm!=null?appForm.getAppReferenceId():null), ( appForm.getAppISDCode()==null?Constants.COUNTRY_CODE_INDIA:appForm.getAppISDCode() ), appForm.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId() );
								logger.info("isAppFoundForDedupInApplicationStage "+isAppFoundForDedupInApplicationStage);
								if(isAppFoundForDedupInApplicationStage){
									responseMessage = "error|"+Constants.APP_DEDUPLICATION_MESSAGE;
									return "jsonResponsePage";
								}
								isAppFoundForDedupInDropOffStage = homeLoanService.isAppFoundForDedupInDropOffStage(appForm.getAppISDCode()==null?Constants.COUNTRY_CODE_INDIA:appForm.getAppISDCode(), appForm.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId() );
								logger.info("isAppFoundForDedupInDropOffStage "+isAppFoundForDedupInDropOffStage);
								isAppFoundForDedupInDropRejectStage = homeLoanService.isAppFoundForDedupInDropRejectStage(( appForm.getAppISDCode()==null?Constants.COUNTRY_CODE_INDIA:appForm.getAppISDCode() ), appForm.getAppMobileNo(), quote.getLoanQuoteLoanPurposeId() );
								logger.info("isAppFoundForDedupInDropRejectStage "+isAppFoundForDedupInDropRejectStage);
							}
						}
						if(isAppFoundForDedupInDropRejectStage){
							appForm.setAppMobileDedup(0);
						}
						if(isAppFoundForDedupInDropOffStage){
							appForm.setAppMobileDedup(1);
						}
						
						appForm.setAppHomeLoanId(Integer.parseInt(applyUrlData[0]));
						appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
						appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
						appForm.setAppLoanProcessingFee((double)Double.parseDouble(applyUrlData[3]));
						appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
						appForm.setAppLoanMoratoriumPeriod(Integer.parseInt(applyUrlData[5]));
						appForm.setAppEmiNmiRatio(Float.parseFloat(applyUrlData[6]));
						
						appForm.setAppLoanEmiDiscount(applyUrlData[7].toString().equalsIgnoreCase("-")?null:Double.parseDouble(applyUrlData[7]));
						appForm.setAppLoanProcessingFeeDiscount(applyUrlData[8].toString().equalsIgnoreCase("-")?null:(double)Double.parseDouble(applyUrlData[8]));
						appForm.setAppLoanInterestRateDiscount(applyUrlData[9].toString().equalsIgnoreCase("-")?null:Float.parseFloat(applyUrlData[9]));
						
						HlProduct product =  commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
						appForm.setAppProductTenureFlag(product.getHlProductSliderTenure());
						productName = product.getHlProductName();
						productURl = product.getProductUrl();
						if(SessionUtil.getApplicationType()!=null){
							StatusRequest statusRequest = new StatusRequest();
							statusRequest.setCurrentStatus(appForm.getAppLoanStatusId());
							statusRequest.setHaveLoanOffer(true);
							statusRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
							statusRequest.setState(stateManagerBean.getState());
							statusRequest.setBankLMSUserId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
							statusRequest.setRsmDecision(0);
							statusRequest.setNsdlPANStatus(0);
							statusRequest.setAppPanCardNo(null);
							statusRequest.setApplicationType(SessionUtil.getApplicationType()!=null?SessionUtil.getApplicationType():0);
							statusRequest.setApplicationLeadType(appForm.getAppDataSourceId());
							StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
							
							if(statusManagerResponse.isEligibleToInsertLog()){
								if(SessionUtil.getBankLMSUser()==null){
									if(statusRequest.getCurrentStatus() == Constants.CALL_LOGS_MESSAGE_STATE1_ID){
										if(statusManagerResponse.getStatus()!=0){
											appForm.setAppLoanStatusId(statusManagerResponse.getStatus());
										} else if(appForm.getAppLoanStatusId() == 0 ){
											responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
											return "jsonResponsePage";
										}
										homeLoanHelper.insertCallLog(appForm.getAppSeqId(),(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);;	
									}
								}else{
									if(statusManagerResponse.getStatus()!=0){
										appForm.setAppLoanStatusId(statusManagerResponse.getStatus());
									} else if(appForm.getAppLoanStatusId() == 0 ){
										responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
										return "jsonResponsePage";
									}
									if(statusRequest.getCurrentStatus() == 1 
											|| statusRequest.getCurrentStatus() == 4 
											|| statusRequest.getCurrentStatus() == 5 
											|| statusRequest.getCurrentStatus() == 154 
											|| statusRequest.getCurrentStatus() == 155 
											|| statusRequest.getCurrentStatus() == 156 
											|| statusRequest.getCurrentStatus() == 157 
											
											|| statusRequest.getCurrentStatus() == 141
											|| statusRequest.getCurrentStatus() == 161
											|| statusRequest.getCurrentStatus() == 152
											|| statusRequest.getCurrentStatus() == 106
											|| statusRequest.getCurrentStatus() == 107
											|| statusRequest.getCurrentStatus() == 108
											|| statusRequest.getCurrentStatus() == 109
											|| statusRequest.getCurrentStatus() == 110
											|| statusRequest.getCurrentStatus() == 111
											){
										homeLoanHelper.insertCallLog(appForm.getAppSeqId(),(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);
									}
								}
							}
							
							if(SessionUtil.getApplicationType()==0){

							}else if(SessionUtil.getApplicationType()==1){

								if(statusManagerResponse.getStatusLead()!=0){
									if(SessionUtil.getLeadId()!=null){
										ApplicationFormLead lead=commonService.getLeadById(SessionUtil.getLeadId());
										lead.setLeadLoanStatusId(statusManagerResponse.getStatusLead());
										lead = commonService.save(lead);

									}
								}
							}else if(SessionUtil.getApplicationType()==2){
							}
						}
						appForm = homeLoanService.save(appForm);
						quote= homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
						homeLoanPage=3;
						generateUIBeanList(homeLoanPage);
						
						if(jsonJSArray3HomeLoanTopup==null){
							jsonJSArray3HomeLoanTopup = SbiUtil.populateJSValidation(17, moduleId).toString();
						}
						if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) ){
							if(appForm.getAppFlexiPayDetails()!=null ){
								try{
									String[] emiMessage = appForm.getAppFlexiPayDetails().split("\\|");
									tenure1Duration = emiMessage[0].split("=")[0];
									tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
									
									tenure2Duration = emiMessage[1].split("=")[0];
									tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
									
									tenure3Duration = emiMessage[2].split("=")[0];
									tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
									
									tenure4Duration = emiMessage[3].split("=")[0];
									tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
								} catch(NullPointerException e){
									logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								} catch(Exception e){
									logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								}
							}
						}
						return "thirdPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 826 stateManagerBean.getState()==13 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 826 stateManagerBean.getState()==13 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==12){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(appSeqId != null){
							homeLoanPage = 2;
							populateForm(homeLoanPage, appSeqId, moduleId);
							return "coapplicantdata"+(uiType==null?"":uiType);
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 855 stateManagerBean.getState()==12 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 855 stateManagerBean.getState()==12 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==11){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}

						if(quote.getLoanQuoteDateOfBirth()!=null){
							quote.setLoanQuoteDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantFirstDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantFirstDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantFirstDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantSecondDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantSecondDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantSecondDateOfBirth(), "MM/dd/yyyy"));
						}
						json = processManagerHomeImpl.processAddCoapplicant(appSeqId, quote, ajaxPostUrl);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
						}else{
							responseMessage = "error|"+json.getString("message");
						}
						return "jsonResponsePage";
					}else{
						String msg = CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 896 stateManagerBean.getState()==11 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 896 stateManagerBean.getState()==11 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==10){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 915 stateManagerBean.getState()==10 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 915 stateManagerBean.getState()==10 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==9){
				try{
					stateManagerBean.setOriginalState(stateManagerBean.getState());
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(moduleId==Constants.HOME_LOAN_ID){
							releaseSession(Constants.HOME_TOP_UP_LOAN_ID);
							appSeqId=null;
							lead=null;
						}
						if(request.getParameter("HLTopupQuoteToken")!=null){
							String encyQuotId=aesEncryption.decrypt(request.getParameter("HLTopupQuoteToken").toString());
							if(ValidatorUtil.isValid(encyQuotId)){
								appSeqId = Integer.parseInt(encyQuotId);
								SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
								if(SessionUtil.getHomeLoanTopupApplicationSequenceId()==null){
									SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
								}
								visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
								SessionUtil.setVisitIdHL(visitId);
								stateManagerBean.setState(2);
							}else{
								stateManagerBean.setState(-1);
							}
						}
						if(request.getParameter("token_id")!=null){
							logger.info("HomeTopupLoanAction.java LNo : 646 : Inside Home Loan State Id 9: outside landing state start if LMS caling appId : "+app_id+" tokenId : "+token_id+" leadId : "+lead_id);
							request = RequestUtil.getServletRequest();
							sessionId = SbiUtil.getSessionId(request, sessionId);
							bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
							SessionUtil.setBankLMSUser(bankLmsUser);
							Cookie [] cookies= request.getCookies();
							bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
							if(bankLmsUser!=null){
								SessionUtil.setBankLMSUser(bankLmsUser);
								logger.info("bankLmsUser First Time : "+(bankLmsUser!=null?bankLmsUser.getLmsUserId():null) + "--" + "getLmsUserIntermediaryId First Time : "+bankLmsUser.getLmsUserIntermediaryId());
							}
							if(token_id!=null &&  SbiUtil.verifyCokkies(cookies,token_id) && commonService.checkLmsUserLogin(token_id) ){
								if(lead_id!=null && lead_id.length() >0){
									appLeadId = Integer.parseInt(Security.decrypt(lead_id, bankLmsUser.getLmsHashKey()));
									lead = commonService.getLeadById(appLeadId);
									if(lead!=null){
										SessionUtil.setLeadId(appLeadId);
										email = lead.getLeadWorkEmail()!=null ? lead.getLeadWorkEmail().toString() : "";
										SessionUtil.setEmail(email);
										if(lead.getLeadApplyingFrom()==2){
											mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo() : "";
											SessionUtil.setISDCode(lead.getLeadIsdCode()!=null?lead.getLeadIsdCode():"");
										}else{
											mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo().toString() : "";
											SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
										}
										SessionUtil.setMobile(mobileNo);
										firstName=lead.getLeadFirstName()!=null?lead.getLeadFirstName():"";
										SessionUtil.setApplicantName(firstName);
										if(lead.getLeadAppSeqId()!=null){
											appSeqId=lead.getLeadAppSeqId();
										}else{
											appSeqId=null;
										}
										SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
									}
									stateManagerBean.setState(-1);
								}else if(app_id!=null && app_id.length() >0){
									appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
									SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
									if(SessionUtil.getHomeLoanTopupApplicationSequenceId()==null){
										SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
									}
									stateManagerBean.setState(2);
								}else{
									stateManagerBean.setState(-1);
								}
							}else{
								logger.info("HomeTopupLoanAction.java LNo : 997 : 2 "+SessionUtil.getSession());
								releaseSession(Constants.HOME_TOP_UP_LOAN_ID);
								return "home"+(uiType==null?"":uiType);
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1000 stateManagerBean.getState()==9 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1000 stateManagerBean.getState()==9 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==8){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){

							responseMessage = "error|Sorry for the inconvenience. Your session has been timed out, Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
							return "jsonResponsePage";
						}else{
							appForm=homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm == null){

								responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
								return "jsonResponsePage";
							}else{
								float appLoanInterestRate=0f;
								double processingFee=0;
								double emi=0;
								int moratoriumMonths=0;
								if(request.getParameter("ir")!=null){
									appLoanInterestRate = Float.parseFloat(request.getParameter("ir"));
								}
								if(appLoanInterestRate<1){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								
								if(request.getParameter("emi")!=null){
									emi = Double.parseDouble(request.getParameter("emi"));
								}
								if(emi<1){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								
								if(request.getParameter("pf")!=null){
									processingFee = Double.parseDouble(request.getParameter("pf"));
								}
								if(processingFee<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								
								if(request.getParameter("moratoriumMonths")!=null){
									moratoriumMonths = Integer.parseInt(request.getParameter("moratoriumMonths"));
								}
								if(moratoriumMonths<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								int pId = 0;
								if(request.getParameter("pId")!=null){
									pId = Integer.parseInt(request.getParameter("pId"));
								}
								if(pId<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								
								appForm.setAppLoanInterestRate(appLoanInterestRate);
								appForm.setAppLoanProcessingFee((double) Math.round((processingFee)));
								appForm.setAppLoanEmi(emi);
								HlProduct product =  commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
								appForm.setAppHomeLoanId(pId);
								appForm.setAppProductTenureFlag(product.getHlProductSliderTenure());
								if(Constants.FLEXI_PAY_PRODUCT_ID.intValue() == pId){
									repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), appForm.getAppLoanInterestRate(), appForm.getAppLoanEmi(),1,1,moratoriumMonths);
									if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) ){
										if(appForm.getAppFlexiPayDetails()!=null ){
											try{
												String[] emiMessage = appForm.getAppFlexiPayDetails().split("\\|");
												tenure1Duration = emiMessage[0].split("=")[0];
												tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
												
												tenure2Duration = emiMessage[1].split("=")[0];
												tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
												
												tenure3Duration = emiMessage[2].split("=")[0];
												tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
												
												tenure4Duration = emiMessage[3].split("=")[0];
												tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
											} catch(NullPointerException e){
												logger.info("HomeTopupLoanAction.java LN 1149 stateManagerBean.getState()==8 ::", e);
											} catch(Exception e){
												logger.info("HomeTopupLoanAction.java LN 1149 stateManagerBean.getState()==8 ::", e);
											}
										}
									}
								} else {
									repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), appForm.getAppLoanInterestRate(), appForm.getAppLoanEmi(),1,1,moratoriumMonths,1,product.getHlProductSliderAmtMul(),null,null,null);
								}
							}
						}
						return "repaymentScheduled"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1080 stateManagerBean.getState()==8 ::", e);
					responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1080 stateManagerBean.getState()==8 ::", e);
					responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==6){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
						}
						appForm=homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
						if(appForm == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						if(request.getParameter("email")!=null){
							appForm.setAppWorkEmail(request.getParameter("email"));
							SessionUtil.setEmail(request.getParameter("email"));
						}
						appForm = homeLoanService.save(appForm);
						
						responseMessage = "success| ";
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1147 stateManagerBean.getState()==6 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1147 stateManagerBean.getState()==6 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			if(stateManagerBean.getState()==3 || stateManagerBean.getState()==4 || stateManagerBean.getState()==5){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						json = processManagerHomeImpl.processWantUsToCallYou(appSeqId, moduleId,stateManagerBean.getState(), (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl, otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1183 stateManagerBean.getState()==3, 4, 5 :: "+stateManagerBean.getState()+ " :: ", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1183 stateManagerBean.getState()==3, 4, 5 :: "+stateManagerBean.getState()+ " :: ", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			 
			if(stateManagerBean.getState()==2){
				try {
					if(isOnlineAndDsrActive){
						responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
						return "jsonResponsePage";
					}
					if(appSeqId == null){
						appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
					}
					if(appSeqId==null){
						if(stateManagerBean.getOriginalState()==9){
							stateManagerBean.setState(-1);
						}else{
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
					}
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
						if(appForm == null){
							if(stateManagerBean.getOriginalState()==9){
								stateManagerBean.setState(-1);
							}else{
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						if(request.getParameter("HLTopupQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
							if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
								stateManagerBean.setState(-1);
							}else{
								initHomeLoan(moduleId);
							}
						}
						if(stateManagerBean.getState()==2){
							if(appForm.getAppApplyingFrom()==2){
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(appForm.getAppISDCode());
								}
							}else{
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
								}
							}
							if(appForm.getAppWorkEmail()!=null){
								SessionUtil.setEmail(appForm.getAppWorkEmail());
							}
							if(appForm.getAppFirstName()!=null){
								firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
								SessionUtil.setApplicantName(firstName);
							}
							quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote==null){
								if(request.getParameter("HLTopupQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
									stateManagerBean.setState(-1);
								}else{
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
									return "jsonResponsePage";
								}
							
							}else{
								HlProduct hlProduct= null;
								if(otherRequest.getChosenProductId()!=null && !otherRequest.getChosenProductId().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanProductId(Integer.parseInt(otherRequest.getChosenProductId().toString()));
									hlProduct=commonService.getHomeLoanProductById(quote.getLoanQuoteLoanProductId());
								}else if(appForm.getAppHomeLoanId()!=null){
									hlProduct=commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
								}

								if(otherRequest.getChosenTenure()!=null){
									chosenTenure =Integer.parseInt(otherRequest.getChosenTenure().toString());
									if(hlProduct!= null && hlProduct.getHlProductSliderTenure().intValue()==2){
										chosenTenure = chosenTenure*Constants.LOAN_TENURE_MULTIPLER_FACTOR;
									}
									
									quote.setLoanQuoteLoanTenure(chosenTenure);
								}
							
								if(otherRequest.getChosenEligibility()!=null){
									BigDecimal amount = new BigDecimal(otherRequest.getChosenEligibility().toString());
									BigDecimal multiplier = new BigDecimal("100000");
									chosenEligibility=amount.multiply(multiplier).doubleValue();
									quote.setLoanQuoteLoanAmount(chosenEligibility);
								}
								if(otherRequest.getDiscountCouponName()!=null){
									quote.setLoanQuoteAppliedCoupon(otherRequest.getDiscountCouponName());
					    		}
								if(otherRequest.getChosenLoanAccountType()!=null && !otherRequest.getChosenLoanAccountType().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanAccountType(Integer.parseInt(otherRequest.getChosenLoanAccountType()));
					    		}else{
					    			quote.setLoanQuoteLoanAccountType(1);
					    		}

								loanScenarioBean=(LoanScenarioBean) processManagerHomeImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

								if(loanScenarioBean.getStatus() == 0 || loanScenarioBean.getStatus() == 2){
									responseMessage = loanScenarioBean.getMessage();
									if(request.getParameter("HLTopupQuoteToken")!=null){
										stateManagerBean.setState(-1);
									}else{
										stateManagerBean.setState(0);
									}
								}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(), loanScenarioBean.getProductSliderDigitExact(), 0.1f);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal= SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(), loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
									if(appSeqId == null){
										appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
									}
									if(appSeqId == null){
										responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
										return "jsonResponsePage";
									}
									appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
									homeLoanPage = 2;

									generateUIBeanListCommon();
									if(appForm.getAppFlexiPayDetails()!=null ){
										try{
											String[] emiMessage = appForm.getAppFlexiPayDetails().split("\\|");
											tenure1Duration = emiMessage[0].split("=")[0];
											tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
											
											tenure2Duration = emiMessage[1].split("=")[0];
											tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
											
											tenure3Duration = emiMessage[2].split("=")[0];
											tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
											
											tenure4Duration = emiMessage[3].split("=")[0];
											tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
										} catch(NullPointerException e){
											logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
										} catch(Exception e){
											logger.info("HomeTopupLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
										}
									}
									if(request.getParameter("HLTopupQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
										if(appForm.getAppReferenceId()!=null){
											appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
											return "applicationTrack"+(uiType==null?"":uiType);
										}
										initHomeLoan(moduleId);
										jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
										populateForm(homeLoanPage, appSeqId, moduleId);
										return  "secondPageWithHeader"+(uiType==null?"":uiType);
									}else{
										return "secondPageChange"+(uiType==null?"":uiType);
									}
								}
							}
						}	
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";	
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1316 stateManagerBean.getState()==2 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1316 stateManagerBean.getState()==2 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==1){
				try {
					if(SessionUtil.getApplicationType()==null){
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					}
					if(isOnlineAndDsrActive){
						responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
						return "jsonResponsePage";
					}
					if(stateManagerBean.getValidatorResponse().isStatus()){
						quote = stateManagerBean.getQuoteHL();
						if(isDsrPage.equalsIgnoreCase("false") && !quote.getInfoprovide().equals("on")){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appSeqId == null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						}
						if(appForm!=null){
							if(appForm.getAppApplyingFrom()==2){
								if(appForm.getAppMobileNo()!=null){
									quote.setAppNRIMobileNo(appForm.getAppMobileNo());
									SessionUtil.setMobile(appForm.getAppMobileNo());
									quote.setAppISDCode(appForm.getAppISDCode());
									SessionUtil.setISDCode(appForm.getAppISDCode());
								}
							}else{
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									quote.setLoanQuoteAppMobileNo(appForm.getAppMobileNo());

									quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
								}
							}
							if(appForm.getAppWorkEmail()!=null){
								SessionUtil.setEmail(appForm.getAppWorkEmail());
								quote.setLoanQuoteAppWorkEmail(appForm.getAppWorkEmail());
							}
							if(appForm.getAppFirstName()!=null){
								firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
								SessionUtil.setApplicantName(firstName);
								quote.setLoanQuoteAppFirstName(appForm.getAppFirstName());
							}
						}
						if(SessionUtil.getHomeLoanTopupApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null && appForm.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)){
								if(appForm.getAppQuoteId() != null){
									ApplicationFormHomeLoanQuote quoteOld = null;
									quoteOld = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());

									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteOutstandingLoanAmount())){
										quote.setLoanQuoteOutstandingLoanAmount(quoteOld.getLoanQuoteOutstandingLoanAmount());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteYearExistingHomeLoanStartDate())){
										quote.setLoanQuoteYearExistingHomeLoanStartDate(quoteOld.getLoanQuoteYearExistingHomeLoanStartDate());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteMonthExistingHomeLoanStartDate())){
										quote.setLoanQuoteMonthExistingHomeLoanStartDate(quoteOld.getLoanQuoteMonthExistingHomeLoanStartDate());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuotePreEMIs())){
										quote.setLoanQuotePreEMIs(quoteOld.getLoanQuotePreEMIs());
									}

									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteYearExistingHomeLoanEndDate())){
										quote.setLoanQuoteYearExistingHomeLoanEndDate(quoteOld.getLoanQuoteYearExistingHomeLoanEndDate());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteMonthExistingHomeLoanEndDate())){
										quote.setLoanQuoteMonthExistingHomeLoanEndDate(quoteOld.getLoanQuoteMonthExistingHomeLoanEndDate());
									}
								}
							}
						}

						if(quote.getLoanQuoteDateOfBirth()!=null){
							quote.setLoanQuoteDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantFirstDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantFirstDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantFirstDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantSecondDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantSecondDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantSecondDateOfBirth(), "MM/dd/yyyy"));
						}
						quote.setLoanQuoteLoanAccountType(1);
						loanScenarioBean=(LoanScenarioBean) processManagerHomeImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

						if(loanScenarioBean.getStatus() == 0 || loanScenarioBean.getStatus() == 2){
							responseMessage = "error|"+loanScenarioBean.getMessage();
							return "jsonResponsePage";
						}else{
							Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(), loanScenarioBean.getProductSliderDigitExact(), 0.1f);
							loanScenarioBean.setManualEligVal(manualEligVal);
							Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList( loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(), loanScenarioBean.getProductSliderTenure()==1?true:false);
							loanScenarioBean.setManualTenureVal(manualTenureVal);
							if(appSeqId == null){
								appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							}
							if(appSeqId == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
								return "jsonResponsePage";
							}
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							if(appForm.getAppApplyingFrom()==2){
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(appForm.getAppISDCode());
								}
							}else{
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);									
								}
							}
							if(appForm.getAppWorkEmail()!=null){
								SessionUtil.setEmail(appForm.getAppWorkEmail());
							}
							if(appForm.getAppFirstName()!=null){
								firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
								SessionUtil.setApplicantName(firstName);
							}
							homeLoanPage = 2;
							generateUIBeanListCommon();
							if(appForm.getAppFlexiPayDetails()!=null ){
								try{
									String[] emiMessage = appForm.getAppFlexiPayDetails().split("\\|");
									tenure1Duration = emiMessage[0].split("=")[0];
									tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
									
									tenure2Duration = emiMessage[1].split("=")[0];
									tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
									
									tenure3Duration = emiMessage[2].split("=")[0];
									tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
									
									tenure4Duration = emiMessage[3].split("=")[0];
									tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
								} catch (NullPointerException e){
									logger.info("HomeTopupLoanAction.java LN 1501 stateManagerBean.getState()==1 ::", e);
								} catch (Exception e){
									logger.info("HomeTopupLoanAction.java LN 1501 stateManagerBean.getState()==1 ::", e);
								}
							}
							if(request.getParameter("HLTopupQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
								if(ValidatorUtil.isValid(appForm.getAppReferenceId())){
									appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
									return "applicationTrack"+(uiType==null?"":uiType);
								}
								initHomeLoan(moduleId);
								jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
								return  "secondPageWithHeader"+(uiType==null?"":uiType);
							}else{
								return "secondPage"+(uiType==null?"":uiType);
							}
						}
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1458 stateManagerBean.getState()==1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1458 stateManagerBean.getState()==1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if (stateManagerBean.getState()==0) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						homeLoanPage=1;
						if(SessionUtil.getApplicationType()==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							if(isOnlineAndDsrActive){
								responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
								return "jsonResponsePage";
							}
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							if(appSeqId != null){
								appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.HOME_LOAN_ID){
										SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
										SessionUtil.setLeadId(null);
										appSeqId=null;
										lead=null;
									}
								}
							}
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						}
						homeLoanPage = 1;
						populateFirstPageContent(Constants.HOME_LOAN_ID,1);
						jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						populateForm(-1, appSeqId, moduleId);
						generateUIBeanListCommon();
						initHomeLoan(moduleId);
						includeJs=true;
						return "firstPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1497 stateManagerBean.getState()==0 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1497 stateManagerBean.getState()==0 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if (stateManagerBean.getState()==-1) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
							if(isOnlineAndDsrActive){
								appSeqId=null;
								responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
							}
							if(appSeqId != null){
								appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
								if(request.getParameter("HLTopupQuoteToken")!=null){
									String encyQuotId=aesEncryption.decrypt(request.getParameter("HLTopupQuoteToken").toString());
									infoMessage=1;
									if(ValidatorUtil.isValid(encyQuotId)){
										if(appForm == null){
											responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
										}
										if(ValidatorUtil.isValid(appForm.getAppReferenceId())){
											if(appForm.getAppLoanStatusId()!=null){
												LoanStatus loanStatus = commonService.getLoanStatusByLoanStatusId(appForm.getAppLoanStatusId());
												if(loanStatus.getLoanStatusType().intValue()==2 ||loanStatus.getLoanStatusType().intValue()==4 ||loanStatus.getLoanStatusType().intValue()==6 ||loanStatus.getLoanStatusType().intValue()==102){
													responseMessage = Constants.COMMON_SORRY_MSG;
												}else{

													appReferencetIdEncrypted="";
													responseMessage = Constants.SAVE_QUOTE_REDIRECTION.replaceAll("CLICK_HERE","<a href='"+Constants.PORT+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"?appReferencetIdEncrypted="+appReferencetIdEncrypted+(uiType==null?"":"&uiType="+Constants.UI_TYPE)+"'>here</a>");
												}
											}
										}
									}
								}
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.HOME_LOAN_ID){
										releaseSession(Constants.HOME_TOP_UP_LOAN_ID);
										appSeqId=null;
										lead=null;
										appForm=null;
										quote=null;
									}
								}
							}
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getHomeLoanTopupApplicationSequenceId();
						}
						if(isOnlineAndDsrActive){
							appSeqId=null;
							responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
						}
						homeLoanPage = 1;
						homeTopUpLoanPage=1;
						
						if(appSeqId !=null){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())){
									if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
										releaseSession(Constants.HOME_LOAN_ID);
										appSeqId=null;
										appForm = null;
										quote = null;
									}
								}
							}
						}
						
						if(appSeqId !=null){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(appForm.getAppSubTypeId() == Constants.APP_APP_SUB_TYPE_ID_CBS){
									if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
										releaseSession(Constants.HOME_LOAN_ID);
									}
								}
								if(appForm.getAppApplyingFrom()==2){
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(appForm.getAppISDCode());
									}
								}else{
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									}
								}
								
								if(appForm.getAppWorkEmail()!=null){
									email = appForm.getAppWorkEmail();
									SessionUtil.setEmail(appForm.getAppWorkEmail());
								}
								if(appForm.getAppFirstName()!=null){
									firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
									SessionUtil.setApplicantName(firstName);
								}
							}else{
								appSeqId = null;
								SessionUtil.setHomeLoanTopupApplicationSequenceId(appSeqId);
							}
						}
						populateForm(-1, appSeqId, moduleId);
						generateUIBeanListCommon();
						initHomeLoan(moduleId);
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
							getCallLogs(appSeqId, appLeadId);
						}
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("false")){
							populateFirstPageContent(loanTypeId,1);
						}

						jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();
						if(SessionUtil.getApplicantName()!=null){
							firstName=SessionUtil.getApplicantName();
						}
						if(SessionUtil.getMobile()!=null){
							mobileNo=SessionUtil.getMobile();
						}
						if(SessionUtil.getEmail()!=null){
							email=SessionUtil.getEmail();
						}
						getDisplayUpdate((appSeqId!=null), (appForm!=null && appForm.getAppSubTypeId()!=null)?appForm.getAppSubTypeId():0, applicationTypeId );
					//	logger.info("HomeTopupLoanAction.java :: LNo 1665  " +SessionUtil.getSession());
						return "homePage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeTopupLoanAction.java LN 1593 stateManagerBean.getState()==-1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("HomeTopupLoanAction.java LN 1593 stateManagerBean.getState()==-1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		} catch(NullPointerException e){
			logger.info("HomeTopupLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		} catch(Exception e){
			logger.info("HomeTopupLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
	
	private void populateForm(Integer pageNo, Integer appSeqId, Integer moduleId){
		try {
			if(appSeqId!=null){
				if(appForm==null){
					appForm= homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
				}
				if(quote==null){
					if(appForm!=null && appForm.getAppQuoteId()!=null){
						quote= homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
					}
				}
				if(quote!=null){
					generateUIBeanList(moduleId);
				}else{
					this.appSeqId=null;
					SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
				}
			} else {
				generateUIBeanList(moduleId);
			}
		} catch (NullPointerException e) {
			logger.info("HomeTopupLoanAction.java LNo :: populateForm(Integer pageNo, Integer appSeqId, Integer moduleId) ", e);
		} catch (Exception e) {
			logger.info("HomeTopupLoanAction.java LNo :: populateForm(Integer pageNo, Integer appSeqId, Integer moduleId) ", e);
		}
	}
	private void getIntermediary(Integer moduleId){
		try {
			if(moduleId==Constants.HOME_LOAN_DSR_ID){
				if(bankLmsUser==null){
					bankLmsUser = SessionUtil.getBankLMSUser();
				}
				if(bankLmsUser!=null && bankLmsUser.getLmsUserIntermediaryId()!=null){
					MasterLmsIntermediary masterLmsIntermediary = commonService.getLmsIntermediaryRelByIntermediatryId(bankLmsUser.getLmsUserIntermediaryId());
					if(masterLmsIntermediary!=null){
						SessionUtil.setLMSIntermediaryRelation(masterLmsIntermediary.getLmsIntermediaryName());
						SessionUtil.setBankLMSUser(bankLmsUser);
						intermediaryRel = commonService.findRelationByIntermediatryIdAndLoanType(masterLmsIntermediary.getLmsIntermediaryId(), Constants.HOME_LOAN_ID);
						if(intermediaryRel!=null){
							String ids=(intermediaryRel.getIntermediaryRelLoanCat1()!=null?intermediaryRel.getIntermediaryRelLoanCat1():"")
									+(intermediaryRel.getIntermediaryRelLoanCat2()!=null?","+intermediaryRel.getIntermediaryRelLoanCat2():"")
									+(intermediaryRel.getIntermediaryRelLoanCat3()!=null?","+intermediaryRel.getIntermediaryRelLoanCat3():"");
							SessionUtil.setLMSIntermediaryCatagories(ids);
						}
					}else{
						SessionUtil.setLMSIntermediaryCatagories(null);
						SessionUtil.setLMSIntermediaryRelation("null");
					}
				}else{
					SessionUtil.setLMSIntermediaryCatagories(null);
					SessionUtil.setLMSIntermediaryRelation("null");
				}
			}else{
				SessionUtil.setLMSIntermediaryCatagories(null);
				SessionUtil.setLMSIntermediaryRelation("null");
			}
		} catch (NullPointerException e) {
			logger.info("HomeTopupLoanAction.java LNo :: getIntermediary(Integer moduleId) ", e);
		} catch (Exception e) {
			logger.info("HomeTopupLoanAction.java LNo :: getIntermediary(Integer moduleId) ", e);
		}
	}
	public void initHomeLoan(Integer moduleId) {
		try {

				initLoanJSONArrayHomeLoan = new JSONArray();
				JSONObject json1 = new JSONObject();
				json1.put("preferredState",SbiUtil.getAllStateId(Constants.HOME_LOAN_ID, null, null, null, null, null));
				initLoanJSONArrayHomeLoan.put(json1);
				
				JSONObject json2 = new JSONObject();
				json2.put("loanWithBankId",SbiUtil.getAllBank( Constants.HOME_LOAN_ID));
				initLoanJSONArrayHomeLoan.put(json2);
				
				JSONObject json3 = new JSONObject();
				json3.put("propertyType", SbiUtil.getPropertyType( Constants.HOME_LOAN_ID,  1));
				initLoanJSONArrayHomeLoan.put(json3);
				
				JSONObject json4 = new JSONObject();
				json4.put("relationShipIndia", SbiUtil.getAllCoApplicantByLoanId(Constants.HOME_LOAN_ID,0));
				initLoanJSONArrayHomeLoan.put(json4);
				
				JSONObject json5 = new JSONObject();
				json5.put("salaryPackages", SbiUtil.getAllSalaryPackageByLoanType(Constants.HOME_LOAN_ID, null));
				initLoanJSONArrayHomeLoan.put(json5);
				
				JSONObject json6 = new JSONObject();
				json6.put("residingCountry", SbiUtil.getAllCountries());
				initLoanJSONArrayHomeLoan.put(json6);					
			
				JSONObject json7 = new JSONObject();
				json7.put("industries", SbiUtil.getIndustryTypeByLoanId(Constants.HOME_LOAN_ID));
				initLoanJSONArrayHomeLoan.put(json7);
				
				JSONObject json8 = new JSONObject();
				json8.put("preferredStateDataRACPC",SbiUtil.getAllStateId(Constants.HOME_LOAN_ID, 1, null, null, null, null));
				initLoanJSONArrayHomeLoan.put(json8);
				
				JSONObject json9 = new JSONObject();
				json9.put("loanPurposeLinks", SbiUtil.getAllLoanPurposeLinks());
				initLoanJSONArrayHomeLoan.put(json9);

			getIntermediary(moduleId);
			JSONObject json10 = new JSONObject();
			if(intermediaryRel!=null){

				JSONArray jsonArray = new JSONArray();
				json10.put("projectData", jsonArray.put(SessionUtil.getLMSIntermediaryRelation()));
			}else{
				json10.put("projectData", "");
			}
			initLoanJSONArrayHomeLoan.put(json10);
		} catch (JSONException e) {
			logger.info("HomeTopupLoanAction.java LNo :: initHomeLoan(Integer moduleId) ", e);
			initLoanJSONArrayHomeLoan.put("error");
		} catch (Exception e) {
			logger.info("HomeTopupLoanAction.java LNo :: initHomeLoan(Integer moduleId) ", e);
			initLoanJSONArrayHomeLoan.put("error");
		}
	}

	private void getCallLogs(Integer appSeqId, Integer appLeadId){
		try{
			if(ValidatorUtil.isValid(appSeqId) || ValidatorUtil.isValid(appLeadId)){
				callLogDetails = commonService.getCallLogByLeadAppId(Constants.HOME_LOAN_ID, appSeqId, appLeadId  );
			}
		} catch (NullPointerException e){
			logger.info("HomeTopupLoanAction.java :: getCallLogs(appSeqId) ", e);
		} catch (Exception e){
			logger.info("HomeTopupLoanAction.java :: getCallLogs(appSeqId) ", e);
		}
	}
	
	private void generateUIBeanList(int pageNo){
		try{
			
			if(appForm!=null && appForm.getAppStateId()!=null && appForm.getAppStateId() > 0){
				Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
				mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
				mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				if(mapDistrict!=null && !mapDistrict.isEmpty()){

					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				}
				beanList.setCitiesoptgrp1(mapCity);
				if(pageNo==3){
					if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
						beanList.setDistricts(mapDistrict);
					}
				}
				if(pageNo==4){
					if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
						beanList.setDistricts(mapDistrict);
					}
				}
			}

			if(pageNo==3){
				if(appForm!=null && appForm.getAppPropertyStateId()!=null){
					
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppPropertyStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppPropertyStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){

						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1PropertyPlace(mapCity);
					if(appForm.getAppPropertyCityId()!=null && appForm.getAppPropertyCityId().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppPropertyDistrictId()!=null && appForm.getAppPropertyDistrictId()>0){
							beanList.setDistrictsPropertyPlace(mapDistrict);
						}
					}
				}
				if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1){
					if(appForm!=null && appForm.getAppPermanentStateId()!=null){
						Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
						Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
						if(mapDistrict!=null && !mapDistrict.isEmpty()){

							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setCitiesoptgrp1Permanent(mapCity);
						if(pageNo==3){
							if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
								beanList.setDistrictsPermanent(mapDistrict);
							}
						}
					}
				}
				if(appForm!=null && appForm.getAppCompanyJoiningYear()!=null){
					int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					if((currentYear-7)>=appForm.getAppCompanyJoiningYear() ){
						appForm.setAppCompanyJoiningYear((currentYear-7));
						appForm.setAppCompanyJoiningMonth(13);
					}
				}
			}
			
			if(appForm!=null && appForm.getAppOfficeStateId()!=null && appForm.getAppOfficeStateId() >0){
				Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
				mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
				mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
				if(mapDistrict!=null && !mapDistrict.isEmpty()){

					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				}
				beanList.setCitiesoptgrp1Office(mapCity);
				if(pageNo==3){
					if(appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)){
						beanList.setDistrictsOffice(mapDistrict);
					}
				}
			}
			
			if(pageNo==4){
				if(appForm!=null && appForm.getAppPickupStateId()!=null && appForm.getAppPickupStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){

						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Pickup(mapCity);
					if(pageNo==4){
						if(appForm.getAppPickupCityId()!=null && appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictPickup(mapDistrict);
						}
					}
				}
				if(appForm!=null && appForm.getAppOfficeStateId()!=null && appForm.getAppOfficeStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){

						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Office(mapCity);
					if(pageNo==4){
						if(appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictsOffice(mapDistrict);
						}
					}
				}
			}
			if(pageNo==3){
				if(appForm!=null && appForm.getAppCoapplicantState_id_1()!=null){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){

						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Coapplicant1(mapCity);
					if(pageNo==3){
						if(appForm.getAppCoapplicantCity_id_1()!=null && appForm.getAppCoapplicantCity_id_1().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictsCoapplicant1(mapDistrict);
						}
					}
				}
				
				if(appForm!=null && appForm.getAppCoapplicantState_id_2()!=null){
					
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_2(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_2(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){

						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Coapplicant2(mapCity);
					if(appForm.getAppCoapplicantCity_id_2()!=null && appForm.getAppCoapplicantCity_id_2().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppCoapplicantDistrictId2()!=null){
							beanList.setDistrictsCoapplicant2(mapDistrict);
						}
					}
				}
			}
		} catch (NullPointerException e){
			logger.info("HomeTopupLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoan appForm, int pageNo) ", e);
		} catch(Exception e){
			logger.info("HomeTopupLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoan appForm, int pageNo) ", e);
		}
	}
	
	private void generateUIBeanListCommon(){

		try {
			Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
			LinkedHashMap <Integer,String> years=null;
			List<MasterEmploymentType> employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.HOME_LOAN_ID);
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for (MasterEmploymentType employmentType : employmentTypes ) {
				maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
			}
			if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
				maps.remove(4);
				maps.remove(5);
				maps.remove(6);
				maps.remove(7);
			}
			beanList.setEmployementTypes(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for (MasterEmploymentType employmentType : employmentTypes ) {
				maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
			}
			if(quote!=null && quote.getLoanQuoteCoapplicantFirstResidentTypeId()!=null && quote.getLoanQuoteCoapplicantFirstResidentTypeId()==2){
				maps.remove(4);
				maps.remove(5);
				maps.remove(6);
				maps.remove(7);
			}
			beanList.setEmployementTypesCoapplicants(maps);
				
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for (MasterEmploymentType employmentType : employmentTypes ) {
				maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
			}
			if(quote!=null && quote.getLoanQuoteCoapplicantSecondResidentTypeId()!=null && quote.getLoanQuoteCoapplicantSecondResidentTypeId()==2){
				maps.remove(4);
				maps.remove(5);
				maps.remove(6);
				maps.remove(7);
			}
			beanList.setEmployementTypesCoapplicants2(maps);
			
			
			years = null; 
			years = new LinkedHashMap<Integer, String>();
			int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
			for (int index =0; index<=29;currentYear--  ){
				if(Constants.BANK_ID == Constants.BANK_ID_SBP){
					index++;
					if(index <= 2){
						continue;
					}
				}else{
					index++;
				}
				years.put(currentYear, String.valueOf(currentYear));
			}
			years.put(currentYear, String.valueOf(currentYear));
			beanList.setYears(years);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			int currentMonths = DateUtil.getCurrentMonth()-2;
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
			if(Constants.BANK_ID == Constants.BANK_ID_SBP){
				currentYear = currentYear-2;
			}
			if(quote!=null && quote.getLoanQuoteYearExistingHomeLoanStartDate()!=null){
				if(currentYear != quote.getLoanQuoteYearExistingHomeLoanStartDate() ){
					currentMonths=0;
				}
			}
			if(currentMonths >0){
				for (int index = 0; index<=currentMonths+1; index++  ) {
					maps.put(index+1, Constants.month[index]);
				}
			}else{
				for (int index = currentMonths; index<Constants.month.length; index++  ) {
					maps.put(index+1, Constants.month[index]);
				}
			}
			beanList.setMonthsTillCurrentMonth(maps);
			
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			currentMonths = DateUtil.getCurrentMonth()-2;
			if(quote!=null && quote.getLoanQuoteYearExistingHomeLoanEndDate()!=null){
				if(currentYear != quote.getLoanQuoteYearExistingHomeLoanEndDate()){
					currentMonths=11;
				}
			}
			for (int index = 0; index <= currentMonths; index++  ) {
				maps.put(index+1, Constants.month[index]);
			}
			beanList.setMonthsFromCurrentMonthTillDecember(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
			for (int index =0; index<=30;currentYear++  ) {
				index++;
				maps.put(currentYear, String.valueOf(currentYear));
			}
			beanList.setYeartenorExistingHomeLoan(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			List<MasterCoApplicant>coapplicants=commonService.getAllCoApplicantByLoanId(Constants.HOME_LOAN_ID, 0);
			maps = new LinkedHashMap<Integer, String>();
			for (MasterCoApplicant coapplicant : coapplicants ) {
				maps.put(coapplicant.getCoapplicantid(), coapplicant.getCoapplicantrelation());
			}
			beanList.setRelationships(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			maps.put(1, "Resident Indian");
			if(Constants.BANK_ID == Constants.BANK_ID_SBI){				
				maps.put(2, "NRI/PIO Holder");
			}
			beanList.setResidentTypes(maps);
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			maps.put(1, "Resident Indian");
			maps.put(2, "NRI/PIO Holder");
			beanList.setResidentTypesCoApplicant(maps);
		} catch (NullPointerException e) {
			logger.info("HomeTopupLoanAction.java :: generateUIBeanListCommon() ", e);
		} catch (Exception e) {
			logger.info("HomeTopupLoanAction.java :: generateUIBeanListCommon() ", e);
		}
	}
	
	private void generateUIBeanList(Integer moduleId){
		try {
			Map<Integer, String> maps = null;
			getIntermediary(moduleId);
			
			List<MasterLoanPurpose> loanpurposes = null;
			List<MasterLoanCategory> loanCategories = null;
			if(intermediaryRel!=null){
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				loanpurposes = commonService.getLoanPurposeByIds(intermediaryRel.getIntermediaryRelLoanPurpose());
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (MasterLoanPurpose purpose : loanpurposes) {
					maps.put(purpose.getLpId(), purpose.getLpTypeValue());
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
						loanPurposeUrl=purpose.getLpUrl();
					}
				}
				beanList.setLoanPurposes(maps);
				String ids=intermediaryRel.getIntermediaryRelLoanCat1()
						+(intermediaryRel.getIntermediaryRelLoanCat2()!=null?","+intermediaryRel.getIntermediaryRelLoanCat2():"")
						+(intermediaryRel.getIntermediaryRelLoanCat3()!=null?","+intermediaryRel.getIntermediaryRelLoanCat3():"");
				loanCategories = commonService.getLoanCategoryByIds(ids);
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				if(loanCategories!=null){
					for (MasterLoanCategory category : loanCategories) {
						maps.put(category.getLoanCategoryId(), category.getCategoryType());
					}
					beanList.setPropertyCategories(maps);
				}else{
					beanList.setPropertyCategories(maps);
				}
				
				
			}else{
				loanpurposes = commonService.getAllLoanPurposeByLoanType(Constants.HOME_LOAN_ID);
				maps = new LinkedHashMap<Integer, String>();
				for (MasterLoanPurpose purpose : loanpurposes) {
					maps.put(purpose.getLpId(), purpose.getLpTypeValue());
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
						loanPurposeUrl=purpose.getLpUrl();
					}
				}
				beanList.setLoanPurposes(maps);
				
				if(quote!=null && quote.getLoanQuoteLoanPurposeId()!= null && (quote.getLoanQuoteLoanPurposeId()==1 || quote.getLoanQuoteLoanPurposeId()==3 )){
					loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(Constants.HOME_LOAN_ID, quote.getLoanQuoteLoanPurposeId());
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					if(loanCategories!=null){
						for (MasterLoanCategory category : loanCategories) {
							maps.put(category.getLoanCategoryId(), category.getCategoryType());
						}
						beanList.setPropertyCategories(maps);
					}
				}
			}
			loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(Constants.HOME_LOAN_ID, 1);
			if(loanCategories!=null){
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				MasterLoanCategory categoryp = commonService.getLoanCategoryById(11);
				maps.put(categoryp.getLoanCategoryId(), categoryp.getCategoryType());
				for (MasterLoanCategory category : loanCategories) {
					maps.put(category.getLoanCategoryId(), category.getCategoryType());
				}
				beanList.setPropertyTypes(maps);
			}
			

			if (quote==null) {
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				maps.put(1, "Place of residence in India");
				maps.put(2, "Place of property");
			}else{
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				if (quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && ("4").equals(quote.getLoanQuoteLoanPurposeId())) {
					if (quote.getLoanQuoteResidentTypeId()!=null && "2".equals(quote.getLoanQuoteResidentTypeId())) {
						maps.put(1, "Place of residence in India");
					}else{
						maps.put(1, "Place of residence");
					}
				}else if (quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && ("5").equals(quote.getLoanQuoteLoanPurposeId())) {
					maps.put(1, "Existing home loan account branch");
				}else{
					if (quote!=null && quote.getLoanQuoteResidentTypeId()!=null && "1".equals(quote.getLoanQuoteResidentTypeId())) {
						maps.put(1, "Place of residence");
						maps.put(2, "Place of property");
					}else{
						maps.put(1, "Place of residence in India");
						maps.put(2, "Place of property");
					}
				}
			}
			if (maps!=null && maps.size()>0) {
				beanList.setPreferredLocation(maps);
			}
			
			beanList.setStatesRACPC(commonService.getStateCityDistrictBranch(1, Constants.HOME_LOAN_ID, null, null, null, 1, null, null, null, null));
			if(quote!=null){
				Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
				Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
				if(Constants.BANK_ID == Constants.BANK_ID_SBI){
					if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId() ==4){
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, 1, null, null, null, null);
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, 1, null, null, null, null);
					}else{
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, null, null, null, null, null);
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, null, null, null, null, null);
					}
				} else {
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, null, null, null, null, null);
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, null, null, null, null, null);
				}
				if(mapDistrict!=null && !mapDistrict.isEmpty()){
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				}
				beanList.setCitiesoptgrp1(mapCity);
				
				if(quote!=null && quote.getLoanQuotePreferredCityId()!=null && quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
					if(quote.getLoanQuotePreferredDistrictId()!=null){
						if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId() ==4){
							beanList.setDistricts(commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, 1, null, null, null, null));
						} else {
							beanList.setDistricts(commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, null, null, null, null, null, null));
						}
						
						if(quote.getLoanQuotePrferredBranchId()!=null){
							if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId() ==4){
								beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, quote.getLoanQuotePreferredDistrictId(), 1, null, null, null, null));
							}else{
								beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), null, quote.getLoanQuotePreferredDistrictId(), null, null, null, null, null));
							}
						}
					}
				}else{
					if(quote!=null && quote.getLoanQuotePrferredBranchId()!=null){
						if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId() ==4){
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), quote.getLoanQuotePreferredCityId(), null, 1, null, null, null, null));
						}else{
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), quote.getLoanQuotePreferredCityId(), null, null, null, null, null, null));
						}
					}
				}

					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index=0; index<=4; index++) {
						maps.put(currentYear, String.valueOf(currentYear));
						currentYear++;
					}
					beanList.setCompletionYear(maps);
					
					
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					int currentMonths = DateUtil.getCurrentMonth()-1;
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					if(quote!=null){
						if(quote.getLoanQuoteYearCompletionDate()!=null && currentYear != quote.getLoanQuoteYearCompletionDate()){
							currentMonths = 0;
						}
					}
					for (int index = currentMonths; index<Constants.month.length; index++  ) {
						maps.put(index+1, Constants.month[index]);
					}
					beanList.setCompletionMonth(maps);
				}
			
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				List <MasterIndustryType> industryTypes= commonService.getIndustryTypeByLoanId(Constants.HOME_LOAN_ID);
				for (MasterIndustryType industryType : industryTypes) {
					maps.put(industryType.getIndustryTypeId(), industryType.getIndustryName());
				}
				maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				beanList.setIndustryTypeData(maps);
				
				List<MasterCorpSalaryPackage> salaryPackages=commonService.getAllSalaryPackageByLoanType(Constants.HOME_LOAN_ID);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				if(salaryPackages!=null){
					for (MasterCorpSalaryPackage salaryPackage : salaryPackages) {
						if(salaryPackage!=null){
							maps.put(salaryPackage.getCorpSalPackId(), salaryPackage.getCorpSalPackTitle());
						}
					}
				}
				beanList.setSalaryPackageData(maps);
			
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (int index =1; index<=30;index++  ) {
					maps.put(index, String.valueOf(index));
				}
				beanList.setTenureYear(maps);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (int index =0; index<=11;index++  ) {
					maps.put(index+1, String.valueOf(index));
				}
				beanList.setTenureMonth(maps);
				
				//customer consent
				//String consentText = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID).getConsentText();
				Integer consentId = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID).getConsentId();
				String consentText = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentId);
				beanList.setConsentHomeLoan(consentText);
				
			} catch (NullPointerException e) {
				logger.info("HomeTopupLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoanQuote quote, Integer moduleId) ", e);
			} catch (Exception e) {
				logger.info("HomeTopupLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoanQuote quote, Integer moduleId) ", e);
			}
		}

	
	public StreamResult getAllProject() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllProject(query);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}

	public ApplicationFormHomeLoan getAppForm() {
		return appForm;
	}

	public void setAppForm(ApplicationFormHomeLoan appForm) {
		this.appForm = appForm;
	}

	public ApplicationFormHomeLoanQuote getQuote() {
		return quote;
	}

	public void setQuote(ApplicationFormHomeLoanQuote quote) {
		this.quote = quote;
	}

	public String getJsonJSArray1HomeLoan() {
		return jsonJSArray1HomeLoan;
	}

	public void setJsonJSArray1HomeLoan(String jsonJSArray1HomeLoan) {
		this.jsonJSArray1HomeLoan = jsonJSArray1HomeLoan;
	}

	public static String getJsonJSArray3HomeLoanTopup() {
		return jsonJSArray3HomeLoanTopup;
	}

	public static void setJsonJSArray3HomeLoanTopup(String jsonJSArray3HomeLoanTopup) {
		HomeTopupLoanAction.jsonJSArray3HomeLoanTopup = jsonJSArray3HomeLoanTopup;
	}

	public JSONArray getInitLoanJSONArrayHomeLoan() {
		return initLoanJSONArrayHomeLoan;
	}

	public void setInitLoanJSONArrayHomeLoan(JSONArray initLoanJSONArrayHomeLoan) {
		this.initLoanJSONArrayHomeLoan = initLoanJSONArrayHomeLoan;
	}

	public String getTenure1Duration() {
		return tenure1Duration;
	}

	public void setTenure1Duration(String tenure1Duration) {
		this.tenure1Duration = tenure1Duration;
	}

	public double getTenure1Emi() {
		return tenure1Emi;
	}

	public void setTenure1Emi(double tenure1Emi) {
		this.tenure1Emi = tenure1Emi;
	}

	public String getTenure2Duration() {
		return tenure2Duration;
	}

	public void setTenure2Duration(String tenure2Duration) {
		this.tenure2Duration = tenure2Duration;
	}

	public double getTenure2Emi() {
		return tenure2Emi;
	}

	public void setTenure2Emi(double tenure2Emi) {
		this.tenure2Emi = tenure2Emi;
	}

	public String getTenure3Duration() {
		return tenure3Duration;
	}

	public void setTenure3Duration(String tenure3Duration) {
		this.tenure3Duration = tenure3Duration;
	}

	public double getTenure3Emi() {
		return tenure3Emi;
	}

	public void setTenure3Emi(double tenure3Emi) {
		this.tenure3Emi = tenure3Emi;
	}

	public String getTenure4Duration() {
		return tenure4Duration;
	}

	public void setTenure4Duration(String tenure4Duration) {
		this.tenure4Duration = tenure4Duration;
	}

	public double getTenure4Emi() {
		return tenure4Emi;
	}

	public void setTenure4Emi(double tenure4Emi) {
		this.tenure4Emi = tenure4Emi;
	}
}
