package com.mintstreet.loan.cveloan.action;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonParseException;
import com.mintstreet.common.action.BaseAction;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterRelationshipWithBank;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.AESEncryption;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.ConsentUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.loan.cveloan.bo.impl.CveProcessManagerImpl;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;
import com.mintstreet.loan.cveloan.service.CveLoanService;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.personal.bo.impl.PersonalProcessManagerImpl;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;

public class CveLoanAction extends BaseAction {
	

	private static final Logger logger = LogManager.getLogger(CveLoanAction.class.getName());
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private PersonalLoanService personalLoanService;

	@Autowired
	private PersonalProcessManagerImpl processManagerPersonalImpl;
	
	@Autowired
	private CveProcessManagerImpl cveProcessManagerPersonalImpl;

	@Autowired
	private AESEncryption aesEncryption;

	private ApplicationFormPersonalLoan appForm;
	
	private ApplicationFormCveLoan cveAppForm;
	
	@Autowired
	private CveLoanService cveLoanService;
	
	@Autowired
	private ConsentUtil consentUtil;
	
	 @Autowired
	 private CommunicationManagerImpl communicationManagerImpl;
	
	public JSONArray initLoanJSONArrayPersonalLoan;
	public String jsonJSArray1PersonalLoan;
	private String instituteName;
	private String employerName;
	private String privacyLocale;
	private List<MasterLanguage> languages;

	public String execute() {
		return "success" + (uiType == null ? "" : uiType);
	} 
	 
		public String cveLoan() {
			try {
				logger.info("cveLoanAction.java LNo : 524 : cveLoan()");
				logger.info("uiType : 525 : cveLoan()" + uiType);
				if(ValidatorUtil.isValid(uiType))
				{
						SessionUtil.setUiType(uiType);
					}else{
						SessionUtil.setUiType(null);
				}

				appPLTypeId = Constants.APP_PL_TYPE_CVE;
//				logger.info("cveLoanAction.java LNo : 462 : cveLoan()--appPLTypeId--"+appPLTypeId);
				
				if (appPLTypeId != null && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_CVE) {
					SessionUtil.setPersonalTypeId(null);
				} 
				SessionUtil.setPersonalTypeId(appPLTypeId);
			
				cveType = 130;
//				logger.info("cveLoanAction.java LNo : 480 : cveLoan()--cveType--"+cveType);
				
				loanTypeId = Constants.CVE_ID;
//				logger.info("cveLoanAction.java LNo : 484 : cveLoan()--Constants.CVE_ID--"+loanTypeId);

				isDsrPage = "false";
			
				ajaxPostUrl = Constants.CVE_ACTION;
//				logger.info("cveLoanAction.java LNo : 489 : cveLoan()--ajaxPostUrl--"+ajaxPostUrl);
				
				SessionUtil.setApplicationType(0);
//				logger.info("cveLoanAction.java LNo : 492 : cveLoan()--"+SessionUtil.getApplicationType());
				
				request = RequestUtil.getServletRequest();
				if (SessionUtil.getBankLMSUser() != null) {
					isOnlineAndDsrActive = true;
					releaseSession(Constants.PERSONAL_LOAN_ID);
				
//					logger.info("cveLoanAction.java LNo : 499 : cveLoan()--releaseSession--"+Constants.CVE_ID);
				}

				if (!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
				} else {
					if (appSeqId != null) {
//						logger.info("cveLoanAction.java LNo : 505 : cveLoan()--appSeqId--"+appSeqId);
						
						SessionUtil.setPersonalLoanTypeSequenceId(appSeqId); 		//---------------------------- > set seq id
//						logger.info("cveLoanAction.java LNo : 509 : cveLoan()--appSeqId--"+SessionUtil.getPersonalLoanTypeSequenceId());
						
						visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
//						logger.info("cveLoanAction.java LNo : 512 : cveLoan()--visitId--"+visitId);
						
						SessionUtil.setVisitIdPL(visitId);
					}
				}
							
				logger.info("before getPersonalLoan call from cveLoan() :: Loan ID.." +Constants.PERSONAL_LOAN_ID);
				return getPersonalLoan(Constants.PERSONAL_LOAN_ID);
			
			} catch (SQLException e) {
				logger.info("CveLoanAction.java LNo : 132 : cveLoan() " + e.getMessage());
			} catch (Exception e) {
				logger.info("CveLoanAction.java LNo : 134 : cveLoan() " + e.getMessage());
			}
			return "homePage" + (uiType == null ? "" : uiType);
		}
	
	

	public String getPersonalLoan(Integer moduleId){
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
			loanTypeId=Constants.PERSONAL_LOAN_ID;
			personalLoanPage = 1;
			stateManagerBean=stateManager.getState(request, moduleId);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			logger.info("CveLoanAction.java LN 407 stateManagerBean " + stateManagerBean.getState());

			
			if(SessionUtil.getVisitIdPL()!=null){
				visitId = SessionUtil.getVisitIdPL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId ==null ){
					logger.info("state manager ELSE -1 calling for CVE");
					if(SessionUtil.getPersonalLoanTypeSequenceId()==null || moduleId==Constants.PERSONAL_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.PERSONAL_LOAN_ID );
						/*if (!(campaignCode == null && offerCode == null && trackingCode == null)) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.PERSONAL_LOAN_ID, 0);
						}*/
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdPL(visitId);
						}else{
							logger.info("CveLoanAction.java LN 336 unable to insert into visit entity.");
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}
			
			String cve = "cve";
			if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)) 
			{
				logger.info("state manager -1 calling for CVE");
				if(stateManagerBean.getState()==-1){
					if(stateManagerBean.getValidatorResponse().isStatus()){
						metaInfo.setTitle(Constants.CVE_TITLE);
						metaInfo.setKeywords(Constants.CVE_KEYWORDS);
						metaInfo.setDescription(Constants.CVE_DESCRIPTION);
						
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_TITLE);
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_KEYWORDS);
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_DESCRIPTION);
						
						browserver = CommonUtilites.getBrowserUserAgent();
						browser = CommonUtilites.getBrowserName();
						if(!ValidatorUtil.isValid(SessionUtil.getSelectedLanguage())){
							SessionUtil.setSelectedLanguage("English");
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				}
			} else {
				logger.info("state manager -1 calling for EXCEPT CVE");
				if(stateManagerBean.getState()==-1){
					if(stateManagerBean.getValidatorResponse().isStatus()){
						metaInfo.setTitle(Constants.PERSONAL_LOAN_TITLE);
						metaInfo.setKeywords(Constants.PERSONAL_LOAN_KEYWORDS);
						metaInfo.setDescription(Constants.PERSONAL_LOAN_DESCRIPTION);
						
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_TITLE);
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_KEYWORDS);
//						logger.info("state manager -1 calling for CVE****"+Constants.CVE_DESCRIPTION);
						
						browserver = CommonUtilites.getBrowserUserAgent();
						browser = CommonUtilites.getBrowserName();
						if(!ValidatorUtil.isValid(SessionUtil.getSelectedLanguage())){
							SessionUtil.setSelectedLanguage("English");
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				}
			}
			
			
			
			
		if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)) 
		{
			 if(stateManagerBean.getState()==41 || stateManagerBean.getState()==42){
				logger.info("state manager 41  & 42 is called for CVE");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerPersonalImpl.verifyConcentOtp(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), 
							ajaxPostUrl, isDsrPage, otherRequest);
					
					if(json.get("status").toString().equalsIgnoreCase("success")){
						if(stateManagerBean.getState()==44){
							stateManagerBean.setState(0);
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
		}

		if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)) 
		{
			if(stateManagerBean.getState()==33 || stateManagerBean.getState()==34){
				logger.info("state manager 33  & 34 is called for CVE");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerPersonalImpl.verifySMSOTP(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), 
							ajaxPostUrl, isDsrPage, otherRequest);
					
					
					if(json.get("status").toString().equalsIgnoreCase("success")){
						if(stateManagerBean.getState()==34){
							stateManagerBean.setState(0);
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
			}
			
			if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)) {
				if(stateManagerBean.getState()==28 || stateManagerBean.getState()==29){
					logger.info("state manager 28 and 29 is called for CVE.......");
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					
						int appOTPVerifyType=0;
						if(otherRequest!=null && otherRequest.getAppOTPVerifyType()!=null){
							appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim());
						}
						String inputOtp = null;
						if(otherRequest!=null && otherRequest.getInputOtp()!=null){
							inputOtp = otherRequest.getInputOtp();
						}
						String userEmail = null;
						if(otherRequest!=null && otherRequest.getUserEmail()!=null){
							userEmail = otherRequest.getUserEmail();
						}
						
						json = cveProcessManagerPersonalImpl.processCBSOTP(moduleId, stateManagerBean.getState(), 
								(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, 
								appOTPVerifyType, inputOtp, userEmail, SessionUtil.getPersonalLoanTypeSequenceId(),
								SessionUtil.getPlTypeCbsCallId());
						
//						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: "+json);
						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..inputOtp:::: "+inputOtp);
//						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..appOTPVerifyType :::: "+appOTPVerifyType);
//						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..SessionUtil.getPersonalLoanTypeSequenceId() :::: "+SessionUtil.getPersonalLoanTypeSequenceId());
						
						String apiMessage = json.optString("message");
						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..apiMessage:::: "+apiMessage);
						
						String caseResponseCve = json.optString("caseResponseCve");
						logger.info("CveLoanAction.java LNo:390 :: crmResponse for CASE creation::"+caseResponseCve);
						
						if(json.get("status").toString().equalsIgnoreCase("success")){
							if(stateManagerBean.getState()==29){

								responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
								logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);
							
								appSeqId = null;
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//----- > set seq id null after release CVE session
								return "jsonResponsePage";

//							}else{
//								responseMessage = "success|"+json.getString("message");
////								logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::success2 "+json);
//								
//								return "jsonResponsePage";
//							}
							} else {
								// =========================================================
								// Fetch CVE ApplicationFormCveLoan after successful OTP
								// =========================================================
								try {
									cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
											SessionUtil.getPersonalLoanTypeSequenceId());

									if (cveAppForm == null) {

										logger.info(
												"CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : "
														+ SessionUtil.getPersonalLoanTypeSequenceId());

										responseMessage = "error|Unable to process privacy consent. Please try again.";
										return "jsonResponsePage";
									}

									// =====================================================
									// CCMS WRITE
									// =====================================================
									boolean ccmsWriteStatus = writePrivacyConsentToCCMS(cveAppForm);

									if (!ccmsWriteStatus) {
										return "jsonResponsePage";
									}

									// =====================================================
									// Existing success response
									// =====================================================
									responseMessage = "success|" + json.getString("message");

									return "jsonResponsePage";

								} catch (Exception e) {

									logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

									responseMessage = "error|Unable to write consent to CCMS. Please try again.";

									return "jsonResponsePage";
								}
							}
						} else if(json.get("status").toString().equalsIgnoreCase("duplicacy")) {
							
							responseMessage = "duplicacy|"+apiMessage;
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::duplicacy "+apiMessage);
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::responseMessage "+responseMessage);
							
							appSeqId = null;
							SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
							return "jsonResponsePage";
						} else if (json.get("message").toString().equalsIgnoreCase("OTP authentication failed")) {
							responseMessage = "error|"+json.getString("message");
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::OTP authentication failed "+apiMessage);
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::responseMessage "+responseMessage);
							
							//SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
							return "jsonResponsePage";
						} else if (json.get("message").toString().equalsIgnoreCase("You have reached maximum number of verify OTP request. You can't resend OTP request.")) {
							responseMessage = "error|"+json.getString("message");
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::error::"+apiMessage);
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::You can't resend OTP request::"+responseMessage);
							
							//SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
							return "jsonResponsePage";
						} else if (json.get("message").toString().equalsIgnoreCase("You have reached maximum number of verify OTP request.")) {
							responseMessage = "error|"+json.getString("message");
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::error:::"+apiMessage);
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::reached maximum number of verify OTP request:::"+responseMessage);
							
							appSeqId = null;
							SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
							return "jsonResponsePage";
						} else if (caseResponseCve.equalsIgnoreCase("FAIL")) {
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: API FAIL:::"+apiMessage);
							responseMessage = "error|"+json.getString("message");
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::error "+json);
							appSeqId = null;
							SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
							return "jsonResponsePage";
						} else {
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: API SUCCESS::ELSE CONDITION::"+caseResponseCve+"......."+apiMessage);
							responseMessage = "error|"+json.getString("message");
							logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::error "+json);
							return "jsonResponsePage";
						}					
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::error "+json);
						appSeqId = null;
						SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);			//---------------------------- > set seq id null after release CVE session
						return "jsonResponsePage";
					}
				}		
			}

			if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)) {
				logger.info("CveLoanAction.java LN 1218 INSIDE CVE CONDITION for getState()==27");
				
				if(stateManagerBean.getState()==27){
					logger.info("CveLoanAction.java LN 1221 INSIDE CVE CONDITION--stateManagerBean.getState()==27----"+stateManagerBean.getState());
					if(stateManagerBean.getValidatorResponse().isStatus()){
						try{
							cbs = stateManagerBean.getCbs();
							
							/*if(isDsrPage.equalsIgnoreCase("false") && !cbs.getInfoprovideCBS().equals("on")){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								logger.info("CveLoanAction.java LN 688 responseMessage ::::: "+responseMessage);
								return "jsonResponsePage";
							}*/
							
							CBSCallResponse cbsCallResponse = cveProcessManagerPersonalImpl.processCbsCall(appSeqId, requestIndex, cbs, 
									isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), visitId, 
									SessionUtil.getPersonalLoanCbsCallId(), 11, null, null,Constants.CVE_ACTION);
							
							logger.info("CveLoanAction.java LN 695 cbsCallResponse::::: "+cbsCallResponse+SessionUtil.getPersonalLoanCbsCallId()+"-----"+Constants.CVE_ACTION);
							
							if(cbsCallResponse.getStatus()!=null){
								if(cbsCallResponse.getStatus()==0){
									responseMessage = "error|"+cbsCallResponse.getResponseMsg();
									logger.info("CveLoanAction.java LN 700 Exception occured:::::"+responseMessage);
									return "jsonResponsePage";
								}else if(cbsCallResponse.getStatus()==1){
									logger.info("CveLoanAction.java LN 703 Exception occured:::::"+responseMessage);
									return "cbsOtpPage"+(uiType==null?"":uiType);
								}else if(cbsCallResponse.getStatus()==2){
									logger.info("CveLoanAction.java LN 706 Exception occured:::::"+responseMessage);
									return "cbsVerifyButtonPage"+(uiType==null?"":uiType);
								}
							}
						} catch (SQLException e) {
							logger.info("CveLoanAction.java LN 370 Exception occured:::::"+ e.getMessage());
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						} catch (Exception e) {
							logger.info("CveLoanAction.java LN 370 Exception occured:::::"+ e.getMessage());
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				}
				
			}
			
			if(stateManagerBean.getState()==12){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					
				}else{
					String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
					responseMessage = "error|"+msg;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==10){
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
			}
		
			if(stateManagerBean.getState()==1){
				if(SessionUtil.getApplicationType()==null){
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
					return "jsonResponsePage";
				}
				if(isOnlineAndDsrActive){
					responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
					return "jsonResponsePage";
				}
				
				if(stateManagerBean.getValidatorResponse().isStatus()){
					if(appSeqId==null){
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
					}
					
				}else{
					String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
					responseMessage = "error|"+msg;
					return "jsonResponsePage";
				}
			}

			if (stateManagerBean.getState()==-1) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){	
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							if(isOnlineAndDsrActive){
								appSeqId=null;
								responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
							}
							if(appSeqId != null){
								appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
								if(request.getParameter("PLQuoteToken")!=null){
									String encyQuotId=aesEncryption.decrypt(request.getParameter("PLQuoteToken").toString());
									logger.info("CveLoanAction.java LNo : 1166 : encyQuotId : "+encyQuotId);
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
													logger.info("CveLoanAction.java LNo : 1174 : appSeqId =" + appForm.getAppSeqId() + "from LMS state 2 PL outside landing checking refernece :: appReferencetIdEncrypted="+appReferencetIdEncrypted);
													responseMessage = Constants.SAVE_QUOTE_REDIRECTION.replaceAll("CLICK_HERE","<a href='"+Constants.PORT+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"?appReferencetIdEncrypted="+appReferencetIdEncrypted+(uiType==null?"":"&uiType="+Constants.UI_TYPE)+"'>here</a>");
												}
											}
										}
									}
								}
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.PERSONAL_LOAN_ID){
										releaseSession(Constants.PERSONAL_LOAN_ID);
										appSeqId=null;
										lead=null;
										appForm = null;
									}
								}
							}
							
							
						}
						
						logger.info("CveLoanAction.java LNo : 2213 " + appSeqId);
						
						if(ValidatorUtil.isValid(request.getParameter("crmLeadId"))){
							Integer crmLeadId = Integer.parseInt(request.getParameter("crmLeadId"));
							if(crmLeadId!=null){
								SessionUtil.setApplicationCRMLeadId(crmLeadId);
							}
						}
						
						if(appSeqId==null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}
						if(isOnlineAndDsrActive){
							appSeqId=null;
							responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
						}
						
						if(appSeqId !=null){
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())){
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
											releaseSession(Constants.PERSONAL_LOAN_ID);
											appSeqId=null;
											appForm = null;
										}
									}else{
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
											releaseSession(Constants.PERSONAL_LOAN_ID);
											appSeqId=null;
											appForm = null;
										}
									}
								}
							}
						}
						
						
						logger.info("getPersonalLoan():::appSeqId::"+appSeqId);
						if(appSeqId !=null){
							
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							
							if(appForm!=null){
								if(appForm.getAppSubTypeId() == Constants.APP_APP_SUB_TYPE_ID_CBS){
								if(isDsrPage.equalsIgnoreCase("true")){
									if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
										releaseSession(Constants.PERSONAL_LOAN_ID);
									}
								}else{
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
										releaseSession(Constants.PERSONAL_LOAN_ID);
									}
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
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
							}
							populateForm(personalLoanPage, appSeqId);  
						}
						generateUIBeanList();
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						personalLoanPage = 1;
						populateFirstPageContent(Constants.PERSONAL_LOAN_ID,1);

						jsonJSArray1PersonalLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();
						initPersonalLoan();
						/*if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
						}*/
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
						return "homePage"+(uiType==null?"":uiType);	
					}
					
					else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
					
				} catch (SQLException e) {
					logger.info("CveLoanAction.java LN 1448 stateManagerBean.getState()==0 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("CveLoanAction.java LN 1448 stateManagerBean.getState()==0 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		} catch (JSONException e) {
			logger.info("CveLoanAction.java LN 730 stateManagerBean.getState()==-1 ::" + e.getMessage());
		} catch (SQLException e) {
			logger.info("CveLoanAction.java LN 732 stateManagerBean.getState()==-1 ::" + e.getMessage());
		} catch(Exception e){
			logger.info("CveLoanAction.java LN 734 stateManagerBean.getState()==-1 ::" + e.getMessage());
		}
		return "homePage"+(uiType==null?"":uiType);
	}

	private void populateForm(Integer pageNo, Integer appSeqId){
		if(appSeqId!=null){
			if(appForm!=null){
				appSeqId=null;
				SessionUtil.setPersonalLoanTypeSequenceId(null);
				
			}else{
				generateUIBeanList();
			}
		}else{
			generateUIBeanList();
		}
	}

	public void initPersonalLoan()  {
		try {
				initLoanJSONArrayPersonalLoan = new JSONArray();
				JSONObject json1 = new JSONObject();
				json1.put("preferredStateData",SbiUtil.getAllStateId(Constants.PERSONAL_LOAN_ID, null, null, null, null, null));
				initLoanJSONArrayPersonalLoan.put(json1);
				
				JSONObject json8 = new JSONObject();
				json8.put("loanPurposeLinks", SbiUtil.getAllLoanPurposeLinks());
				initLoanJSONArrayPersonalLoan.put(json8);
				
		} catch (JSONException e) {
			logger.info("CveLoanAction.java LN 774 initPersonalLoan() ::"+ e.getMessage());
			initLoanJSONArrayPersonalLoan.put("error");
		} catch (Exception e) {
			logger.info("CveLoanAction.java LN 777 initPersonalLoan() ::"+ e.getMessage());
			initLoanJSONArrayPersonalLoan.put("error");
		}
	}


	private void generateUIBeanList() {
		try {
			Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
			List<MasterLoanPurpose> loanpurposes = commonService.getAllLoanPurposeByLoanType(Constants.PERSONAL_LOAN_ID);
			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			for (MasterLoanPurpose purpose : loanpurposes) {
				maps.put(purpose.getLpId().intValue(), purpose.getLpTypeValue());
	
				if (isDsrPage == "false" && loanPurposeUrl == null) {
					MasterLoanPurpose loanPurpose = null;
					if (appPLTypeId == Constants.APP_PL_TYPE_GOLD) {
						loanPurpose = commonService.getLoanPurposeById(27);
					} else if (appPLTypeId == Constants.APP_PL_TYPE_PENSION) {
						loanPurpose = commonService.getLoanPurposeById(23);
					} else if (appPLTypeId == Constants.APP_PL_TYPE_PERSONAL) {
						loanPurpose = commonService.getLoanPurposeById(12);
					}else if(appPLTypeId == Constants.APP_PL_TYPE_CVE){			//"APP_PL_TYPE_CVE" added for CVE
						loanPurpose = commonService.getLoanPurposeById(130);
						logger.info("CveLoanAction.java LN 2682 generateUIBeanList()loanPurpose ::"+loanPurpose.toString());
					}
					if (loanPurpose != null && loanPurpose.getLpUrl() != null) {
						loanPurposeUrl = loanPurpose.getLpUrl();
					}
				}

			}
			if (isDsrPage == "false") {
				if (appPLTypeId == Constants.APP_PL_TYPE_PENSION) {
					maps.remove(11);
					maps.remove(12);
					maps.remove(13);
					maps.remove(27);
				} else if (appPLTypeId == Constants.APP_PL_TYPE_GOLD) {
					maps.remove(11);
					maps.remove(12);
					maps.remove(13);
					maps.remove(Constants.PENSION_LOAN_PURPOSE_ID);
					
				} else if (appPLTypeId == Constants.APP_PL_TYPE_PERSONAL) {
					maps.remove(Constants.PENSION_LOAN_PURPOSE_ID);
					maps.remove(27);
				}
			} else {
				if (contactCenterLmsUser) {
					maps.remove(Constants.PENSION_LOAN_PURPOSE_ID);

				}
			}
			beanList.setLoanPurposes(maps);

			int currentYear2 = 0;
			NavigableMap<Integer, String> navmap = null;
			TreeMap<Integer, String> yearsmap = new TreeMap<Integer, String>();
			currentYear2 = Integer.parseInt(DateUtil.getCurrentYear());
			for (int index = 0; index < 5; currentYear2--) {
				index++;
				yearsmap.put(currentYear2, String.valueOf(currentYear2));
			}
			navmap = yearsmap.descendingMap();
			beanList.setContractStartYear(navmap);

			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			
			beanList.setContractStartMonth(maps);
			yearsmap = new TreeMap<Integer, String>();
			currentYear2 = Integer.parseInt(DateUtil.getCurrentYear());
			for (int index = 0; index < 5; currentYear2++) {
				index++;
				yearsmap.put(currentYear2, String.valueOf(currentYear2));
			}
			beanList.setContractEndYear(yearsmap);
			
			beanList.setContractEndMonth(maps);

			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			
			
			beanList.setResidentTypes(maps);

			Map<Integer, String> mapPensionVariants = new LinkedHashMap<Integer, String>();
			mapPensionVariants.put(6, "Regular Pension");
			mapPensionVariants.put(7, "Jai Jawan Pension");
			mapPensionVariants.put(8, "SBI Employees");
			mapPensionVariants.put(9, "Family Pension");
			beanList.setPensionLoanTypes(mapPensionVariants);

			Map<Integer, String> stateMap = new LinkedHashMap<Integer, String>();
			stateMap = commonService.getStateCityDistrictBranch(1, null, null, null, null, null, null, null, null, null);
			beanList.setPensionPayingState(stateMap);

			if (appForm != null && appForm.getAppPensionPayingStateId() != null) {
				Map<Integer, String> mapBranches = new LinkedHashMap<Integer, String>();
				mapBranches = commonService.getStateCityDistrictBranch(2,
						Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(),
						null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
				mapDistrict = commonService.getStateCityDistrictBranch(3,
						Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(),
						null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty()) {
					beanList.setDistricts(mapDistrict);
				}
				beanList.setBranches(mapBranches);
	
			}
			
			 if (appSeqId != null) {
				MasterCBSResponse masterCBSResponse = personalLoanService.getMasterCBSResponseObjectByCbsAppSeqId(appSeqId);
				if (masterCBSResponse != null) {
					cbs = personalLoanService.getMasterCBSCallObjectByCbsResponseId(masterCBSResponse.getCbsResponseId());
				} else {
					cbs = commonService.getMasterCBSCallObjById(SessionUtil.getPersonalLoanCbsCallId());
				}
			} else {
				cbs = commonService.getMasterCBSCallObjById(SessionUtil.getPersonalLoanCbsCallId()); 
			} 	
			 
			if(appForm!=null && appForm.getAppCityId()!=null && appForm.getAppCityId() > 0 && appForm.getAppStateId()!=null){
				Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
				mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
				mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
				if (mapDistrict != null && !mapDistrict.isEmpty()) {
					mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);

				}
				beanList.setCitiesoptgrp1(mapCity);
				beanList.setDistricts(mapDistrict);

			}
			
			List <MasterRelationshipWithBank>relationshipWithBanks = commonService.getAllRelationshipWithBank(Constants.PERSONAL_LOAN_ID);
			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			if (relationshipWithBanks != null) {
				for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) {
					maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
				}
			}
			beanList.setRelationshipWithBank(maps);
			
			
			List<MasterCveProduct> cveProducts = cveLoanService.getCveProducts();
			Map<String,String> cveProductsMap = new LinkedHashMap<String,String>();
			if (cveProducts != null) {
				for (MasterCveProduct cveProduct: cveProducts) {
					cveProductsMap.put(cveProduct.getCveProductCrmCode(),cveProduct.getCveProductName());
				}
			}
			beanList.setCveProductCategories(cveProductsMap);
			
			//blank maps added to resolve JSP error
			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			beanList.setYearsLeaseStart(maps);
			beanList.setMonthsLeaseStart(maps);
			beanList.setYearsLeaseEnd(maps);
			beanList.setMonthsLeaseEnd(maps);
			
			//consent
			//String consentTextCve = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "NA").getConsentText();
//			Integer consentIdCve = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "NA").getConsentId();
//			String consentTextCve = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdCve);
//			beanList.setConsentCveLoan(consentTextCve);
			
//			languages = commonService.getAllActiveLanguages();
//
//			PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
//			if (privacyObj != null) {
//				beanList.setConsentCveLoan(privacyObj.getPrivacyNotice());
//			} else {
//				beanList.setConsentCveLoan("Privacy Notice Not Available");
//			}
			languages = commonService.getAllActiveLanguages();
			logger.info("Privacy dropdown language count : " + (languages == null ? 0 : languages.size()));
			
			//PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
			Integer privacyId = commonService.getPrivacyIdByLocale("eng");
			String privacyText = commonService.getNClobdata("RUPEEPOWER_OCAS_T_13703", "PRIVACY_NOTICE", "PRIVACY_ID", privacyId);
			
			if (privacyText != null) {
				beanList.setConsentCveLoan(privacyText);
			} else {
				beanList.setConsentCveLoan("Privacy Notice Not Available");
			}
			
			//consent revoke
			//String consentTextCveRevoke = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "REV").getConsentText();
			Integer consentIdCveRevoke = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "REV").getConsentId();
			String consentTextCveRevoke = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdCveRevoke);
			beanList.setConsentCveRevoke(consentTextCveRevoke);
			
		} catch (SQLException e) {
			logger.info("CveLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
		} catch (Exception e) {
			logger.info("CveLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
		}
	}
		
	public StreamResult getPrivacyNoticeByLocaleCve() {
		JSONObject json = new JSONObject();
		try {
			/*
			 * Default English
			 */
			if (privacyLocale == null || "".equals(privacyLocale)) {
				privacyLocale = "eng";
			}
			//PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale(privacyLocale);
			Integer privacyId = commonService.getPrivacyIdByLocale(privacyLocale);
			String privacyText = commonService.getNClobdata("RUPEEPOWER_OCAS_T_13703", "PRIVACY_NOTICE", "PRIVACY_ID", privacyId);
			
			if (privacyText != null) {
				json.put("status", "success");
				json.put("privacyNotice", privacyText);
			} else {
				json.put("status", "fail");
				json.put("privacyNotice", "Privacy Notice Not Found");
			}
		} catch (JSONException | JsonParseException e) {
			try {
				json.put("status", "fail");
				json.put("privacyNotice", "Unable To Load Privacy Notice");
			} catch (JSONException e1) {
				logger.info("Exception caught during Loading Privacy Notice" + e1);
			}
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getPrivacyLanguageListCve() {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<MasterLanguage> langList = commonService.getAllActiveLanguages();
			if (langList != null && !langList.isEmpty()) {
				for (MasterLanguage lang : langList) {
					JSONObject langObj = new JSONObject();
					langObj.put("locale", lang.getLannguageCode());
					langObj.put("languageName", lang.getLanguageName());
					array.put(langObj);
				}
				json.put("status", "success");
				json.put("languageList", array);
			} else {
				json.put("status", "fail");
				json.put("languageList", array);
			}
		} catch (Exception e) {
			logger.info("Exception in getPrivacyLanguageList", e);
			try {
				json.put("status", "fail");
				json.put("languageList", array);
			} catch (JSONException je) {
				logger.info("JSONException in getPrivacyLanguageList", je);
			}
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	public StreamResult savePrivacyConsentCve() {
		JSONObject json = new JSONObject();
		try {
			Integer appSeqId = SessionUtil.getCveLoanApplicationSequenceId();
			if (appSeqId == null) {
				json.put("status", "fail");
				json.put("message", "Session expired. Application not found.");
			} else {
				ApplicationFormCveLoan appForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(appSeqId);
				if (appForm == null) {
					json.put("status", "fail");
					json.put("message", "Application not found.");
				} else {
					String ntbId = generateCveLoanNtbId(appForm);
					appForm.setAppPrivacyConsentFlag("Y");
					appForm.setAppNtbId(ntbId);
					//appForm.setAppPrivacyConsentTime(new Date());
					cveLoanService.save(appForm);
					json.put("status", "success");
					json.put("message", "Consent saved successfully.");
					json.put("ntbId", ntbId);
				}
			}
		} catch (Exception e) {
			logger.info("Exception in savePrivacyConsent", e);
			try {
				json.put("status", "fail");
				json.put("message", "Unable to save consent.");
			} catch (JSONException je) {
				logger.info("JSONException in savePrivacyConsent", je);
			}
		}
		return new StreamResult(
				new ByteArrayInputStream(json.toString().getBytes())
		);
	}
	private void loadPrivacyLanguages() {
		try {
			languages = commonService.getAllActiveLanguages();
			logger.info("Privacy language dropdown count : "
					+ (languages == null ? 0 : languages.size()));
		} catch (Exception e) {
			logger.info("Exception in loadPrivacyLanguages", e);
		}
	}
	private String generateCveLoanNtbId(ApplicationFormCveLoan appForm) {
		String mobile = appForm.getCbsMobileNumber() != null
				? appForm.getCbsMobileNumber()
				: "";
		String dob = "";
		try {
			if (appForm.getCbsAccountNumber() != null) {
				dob = new SimpleDateFormat("ddMMyyyy")
						.format(appForm.getCbsAccountNumber());
			}
		} catch (Exception e) {
			logger.info("Exception while formatting DOB for NTB ID", e);
		}
		String loanType = String.valueOf(Constants.PL_TYPE_CVE);
		logger.info("CveLoanAction.java ::  NTB ID ----:  ", mobile + dob + loanType);
		return mobile + dob + loanType;
	}
	
	public ApplicationFormPersonalLoan getAppForm() {
		return appForm;
	}

	public void setAppForm(ApplicationFormPersonalLoan appForm) {
		this.appForm = appForm;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getJsonJSArray1PersonalLoan() {
		return jsonJSArray1PersonalLoan;
	}

	public void setJsonJSArray1PersonalLoan(String jsonJSArray1PersonalLoan) {
		this.jsonJSArray1PersonalLoan = jsonJSArray1PersonalLoan;
	}


	public JSONArray getInitLoanJSONArrayPersonalLoan() {
		return initLoanJSONArrayPersonalLoan;
	}

	public void setInitLoanJSONArrayPersonalLoan(
			JSONArray initLoanJSONArrayPersonalLoan) {
		this.initLoanJSONArrayPersonalLoan = initLoanJSONArrayPersonalLoan;
	}
		
	public String getPrivacyLocale() {
		return privacyLocale;
	}
	public void setPrivacyLocale(String privacyLocale) {
		this.privacyLocale = privacyLocale;
	}
	public List<MasterLanguage> getLanguages() {
		return languages;
	}
	public void setLanguages(List<MasterLanguage> languages) {
		this.languages = languages;
	}

	private boolean writePrivacyConsentToCCMS(ApplicationFormCveLoan application) {
		try {
			if (application == null) {
				responseMessage = "error|Unable to process privacy consent. Please try again.";
				return false;
			}

			/*
			 * 1. Validate privacy consent
			 */
			if (!"Y".equalsIgnoreCase(application.getAppPrivacyConsentFlag())) {
				responseMessage = "error|Please read and accept SBI Privacy Notice before proceeding.";
				return false;
			}

			/*
			 * 2. Validate NTB ID
			 */
			if (!ValidatorUtil.isValid(application.getAppNtbId())) {
				responseMessage = "error|Invalid consent details. Please accept SBI Privacy Notice again.";
				return false;
			}

			/*
			 * 3. Validate privacy locale
			 */
			if (!ValidatorUtil.isValid(application.getAppPrivacyLocale())) {
				responseMessage = "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";
				return false;
			}

			/*
			 * 4. Get CVE mobile and email
			 */
			String mobile = application.getCbsMobileNumber();
			String email = application.getCveAppEmail();
			if (!ValidatorUtil.isValid(mobile)) {
				responseMessage = "error|Mobile number is required for consent write.";
				return false;
			}
			/*
			 * 5. Get IP Address
			 */
			String ipAddresss = this.SbiUtil.getIPAddress();
			String ipAddress = ipAddresss.replace(",", "");

			/*
			 * 6. Call CCMS WRITE API
			 */
			ConsentWriteLog consentWrite = consentUtil.callCCMSConsentWriteAPI(application.getAppNtbId(), mobile, email,
					ipAddress, application.getAppPrivacyLocale(), Constants.CVE_ID);

			/*
			 * 7. Validate CCMS response
			 */
			if (consentWrite == null || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
					|| !"200".equalsIgnoreCase(consentWrite.getResponseCode()) || consentWrite.getConsentId() == null
					|| consentWrite.getConsentId().trim().isEmpty()
					|| !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {
				logger.info("CveLoanAction.java :: CCMS consent write failed. AppSeqId : " + application.getAppSeqId());
				responseMessage = "error|Unable to write consent to CCMS. Please try again.";
				return false;
			}

			String ccmsConsentId = consentWrite.getConsentId().trim();

			/*
			 * 8. Save CCMS consent ID in CVE application
			 */
			application.setAppCcmsConsentId(ccmsConsentId);
			cveAppForm = personalLoanService.save(application);
			if (cveAppForm == null) {
				logger.info("CveLoanAction.java :: CCMS consent was successful but CVE application update failed. "
						+ "AppSeqId : " + application.getAppSeqId());
				responseMessage = "error|Consent was recorded, but application details could not be updated.";
				return false;
			}
			logger.info(
					"CveLoanAction.java :: CCMS consent successfully written for CVE. " + "ConsentId : " + ccmsConsentId
							+ ", AppSeqId : " + application.getAppSeqId() + ", LoanTypeId : " + Constants.CVE_ID);
			
			
			//send SMS to NTB customer after submit consent
			String msgBody=communicationManagerImpl.setEmailBody(25, 0, Constants.MESSAGE_TYPE_SMS, 0);
			logger.info("msgBody11::" + msgBody);
			msgBody = SbiUtil.urlEncode(msgBody);
			String SMS_TEXT = null;
			if(Constants.COUNTRY_CODE_INDIA.equals(application.getAppISDCode())){
				SMS_TEXT=Constants.SMS_STRING_INDIAN;
			}else{
				SMS_TEXT=Constants.SMS_STRING_NRI;
			}
			SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", application.getAppISDCode()+mobile);
			SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TYPE", "CVE Loans ");
			SMS_TEXT=SMS_TEXT.replaceAll("CONSENT_ID", application.getAppNtbId());
			logger.info("SMS_TEXT5::" + SMS_TEXT);
			
			communicationManagerImpl.sendSms(SMS_TEXT);
			
			return true;
			
		} catch (Exception e) {
			logger.info("CveLoanAction.java :: Exception while calling CCMS Write API for CVE", e);
			responseMessage = "error|Unable to write consent to CCMS. Please try again.";
			return false;
		}
}
	}
