package com.mintstreet.loan.cveloan.action;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.action.BaseAction;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterRelationshipWithBank;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.AESEncryption;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.cveloan.bo.impl.CveProcessManagerImpl;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;
import com.mintstreet.loan.cveloan.service.CveLoanService;
import com.mintstreet.loan.personal.bo.impl.PersonalProcessManagerImpl;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.service.PersonalLoanService;

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
	
	@Autowired
	private CveLoanService cveLoanService;
	
	public JSONArray initLoanJSONArrayPersonalLoan;
	public String jsonJSArray1PersonalLoan;
	private String instituteName;
	private String employerName;

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
			
			} catch (NullPointerException e) {
				logger.info("CveLoanAction.java LNo : 130 : cveLoan() " + e.getMessage());
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
								
							}else{
								responseMessage = "success|"+json.getString("message");
//								logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json::::success2 "+json);
								
								return "jsonResponsePage";
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
						} catch (NullPointerException e) {
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
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("CveLoanAction.java LN 780 stateManagerBean.getState()==12 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("CveLoanAction.java LN 780 stateManagerBean.getState()==12 ::"+ e.getMessage());
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
					logger.info("CveLoanAction.java LN 613 stateManagerBean.getState()==17 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("CveLoanAction.java LN 613 stateManagerBean.getState()==17 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		
			if(stateManagerBean.getState()==1){
				try {
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
				} catch (NullPointerException e) {
					logger.info("CveLoanAction.java LN 1331 stateManagerBean.getState()==1 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("CveLoanAction.java LN 1331 stateManagerBean.getState()==1 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
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
					
				} catch (NullPointerException e) {
					logger.info("CveLoanAction.java LN 1448 stateManagerBean.getState()==0 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} catch (Exception e) {
					logger.info("CveLoanAction.java LN 1448 stateManagerBean.getState()==0 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		} catch(NullPointerException e){
			logger.info("CveLoanAction.java LN 728 stateManagerBean.getState()==-1 ::" + e.getMessage());
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
		try {
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
		} catch (NullPointerException e) {
			logger.info("CveLoanAction.java LN 1383 populateForm ::"+ e.getMessage());
		} catch (Exception e) {
			logger.info("CveLoanAction.java LN 1383 populateForm ::"+ e.getMessage());
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
				
		} catch (NullPointerException e) {
			logger.info("CveLoanAction.java LN 771 initPersonalLoan() ::"+ e.getMessage());
			initLoanJSONArrayPersonalLoan.put("error");
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
			Integer consentIdCve = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "NA").getConsentId();
			String consentTextCve = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdCve);
			beanList.setConsentCveLoan(consentTextCve);
			
			//consent revoke
			//String consentTextCveRevoke = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "REV").getConsentText();
			Integer consentIdCveRevoke = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, "REV").getConsentId();
			String consentTextCveRevoke = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdCveRevoke);
			beanList.setConsentCveRevoke(consentTextCveRevoke);
			
		} catch (NullPointerException e) {
			logger.info("CveLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
		} catch (Exception e) {
			logger.info("CveLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
		}
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

}
