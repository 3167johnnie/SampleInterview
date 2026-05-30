package com.mintstreet.loan.autoloan.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.IntermediaryRel;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterDealer;
import com.mintstreet.common.entity.MasterEmployeeOccupationType;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterLmsIntermediary;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterRelationshipWithBank;
import com.mintstreet.common.entity.MasterResidenceType;
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
import com.mintstreet.loan.autoloan.bo.impl.AutoProcessManagerImpl;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.entity.MasterCarCompany;
import com.mintstreet.loan.autoloan.entity.MasterCarModel;
import com.mintstreet.loan.autoloan.entity.MasterCarVariant;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.autoloan.util.AutoLoanHelper;
import com.mintstreet.loan.product.entity.AlProduct;

public class AutoLoanAction extends BaseAction {
  private static final Logger logger = LogManager.getLogger(AutoLoanAction.class.getName());
  
	private static final long serialVersionUID = 1L;
	@Autowired
	private AutoLoanService autoLoanService;
	
	@Autowired
	private AutoLoanHelper autoLoanHelper;
	
	@Autowired
	private AutoProcessManagerImpl processManagerAutoImpl;
	
	@Autowired
	private AESEncryption aesEncryption;
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	
	private ApplicationFormAutoLoanQuote quote;
	private ApplicationFormAutoLoan appForm;

	private Integer carCompanyId;
	private Integer  carModuleId;
	private Integer carVariantId;
	
	private Integer twoWheelerTypeId;
	private Integer  bikeCompanyId;
	private Integer bikeModelId;
	private Integer bikeVariantId;
	
	public JSONArray initLoanJSONArrayAutoLoan;
	public String jsonJSArray1AutoLoan;
	public static String jsonJSArray3AutoLoan;
	
	private IntermediaryRel intermediaryRel = null;
	
	public String execute(){
		return SUCCESS;
	}

	public String autoLoanDSR() {
		try {
			uiType=Constants.UI_TYPE;
			if(!ValidatorUtil.isValid(uiType)){uiType="Old";SessionUtil.setUiType(uiType);}
			ajaxPostUrl=Constants.AUTO_LOAN_ACTION_DSR;
			isDsrPage="true";
			request = RequestUtil.getServletRequest();
			sessionId = SbiUtil.getSessionId(request, sessionId);
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
				if(sessionId == null ){
					return "home"+(uiType==null?"":uiType);
				}
			}
			if(!ValidatorUtil.isValid(lead_id)){
				lead_id=null;
			}
			if(!ValidatorUtil.isValid(app_id)){
				app_id=null;
			}
			bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
			if (bankLmsUser!=null) {
				appBankLmsUserId = bankLmsUser.getLmsUserId();
				SessionUtil.setBankLMSUser(bankLmsUser);
				contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
				//logger.info("AutoLoanAction.java LNo : 119 : contactCenterLmsUser = "+contactCenterLmsUser);
				//logger.info("AutoLoanAction.java LNo : 123 : "+SessionUtil.getSession());
				if ( !ValidatorUtil.isValid(app_id) && !ValidatorUtil.isValid(lead_id) ) {
					if(request.getParameter("generatePDF")!=null){

					}else if(request.getParameter("requestIndex")==null){
						if(SessionUtil.getApplicationType()==null){

						}else{

							releaseSession(Constants.AUTO_LOAN_ID);
						}
						applicationTypeId=2;
						SessionUtil.setApplicationType(2);
						appSeqId=null;
						SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
						appLeadId=null;
						SessionUtil.setLeadId(appLeadId);
						firstName=null;
						mobileNo=null;
						email=null;
						alternateMobileNo=null;
						SessionUtil.setEmail(email);
						SessionUtil.setMobile(mobileNo);
						SessionUtil.setApplicantName(firstName);
						SessionUtil.setalternateMobileNumber(alternateMobileNo);
					}
				}else if (ValidatorUtil.isValid(app_id)) {
					appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
					SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
					visitId = autoLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdAL(visitId);
					firstName=null;
					SessionUtil.setApplicantName(firstName);
					appLeadId=null;
					SessionUtil.setLeadId(appLeadId);
					if(request.getParameter("requestIndex")==null){
						applicationTypeId=0;
						SessionUtil.setApplicationType(0);
					}
				} else if (ValidatorUtil.isValid(lead_id)) {
					appLeadId = Integer.parseInt(Security.decrypt(lead_id, bankLmsUser.getLmsHashKey()));
					lead = commonService.getLeadById(appLeadId);
					if(lead!=null){
						SessionUtil.setLeadId(appLeadId);
						visitId = lead.getLeadVisitId();
						SessionUtil.setVisitIdAL(visitId);
						email = lead.getLeadWorkEmail()!=null ? lead.getLeadWorkEmail().toString() : "";
						SessionUtil.setEmail(email);
						if(lead.getLeadApplyingFrom()==2){
							mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo() : "";
							SessionUtil.setISDCode(lead.getLeadIsdCode()!=null?lead.getLeadIsdCode():"");
							alternateMobileNo = lead.getLeadAlternateNumber();			
						}else{
							mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo().toString() : "";
							SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);		
							alternateMobileNo = lead.getLeadAlternateNumber();							
						}
						
						logger.info("bankLmsUser.getLmsUserId::::" + bankLmsUser.getLmsUserId());
						boolean isTeleCallerUser = false;
						isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
						logger.info("isTeleCallerUser::::" + isTeleCallerUser);
						
						if (isTeleCallerUser && mobileNo != null && mobileNo !="") {
							isMobileNoMask = "true";
							mobileNoMaskVal = mobileNo.replaceAll("\\d(?=\\d{4})", "*");
						}
						if (isTeleCallerUser && email != null && email !="") {
							isEmailMask = "true";
							emailMaskVal=email.replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
						}
						if (isTeleCallerUser && alternateMobileNo != null) {
							isalternateMobileNoMask = "true";
							alternateMobileNoMaskVal = alternateMobileNo.replaceAll("\\d(?=\\d{4})", "*");
						}
						
						SessionUtil.setMobile(mobileNo);
						firstName=lead.getLeadFirstName()!=null?lead.getLeadFirstName():"";
						SessionUtil.setApplicantName(firstName);
						if(lead.getLeadAppSeqId()!=null){
							appSeqId=lead.getLeadAppSeqId();
							
						}else{
							appSeqId=null;
						}
						SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
						
						logger.info("leadConsentId for get a call back lead: " + lead.getLeadConsentId() + "  :: lead id::" + appLeadId);
						leadConsentId = lead.getLeadConsentId()!=null ? lead.getLeadConsentId() : 0;
						
					}
					if(request.getParameter("requestIndex")==null){
						SessionUtil.setApplicationType(1);
						applicationTypeId=1;
					}
				} else if (SessionUtil.getAutoLoanApplicationSequenceId() != null) {
					if(request.getParameter("requestIndex")==null){
						releaseSession(Constants.AUTO_LOAN_ID);
					}
					appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
					bankLmsUser = SessionUtil.getBankLMSUser();
				}
				if(ValidatorUtil.isValid(applicationTypeId)){
					SessionUtil.setApplicationType(applicationTypeId);
				}
				if(request.getParameter("requestIndex")==null){
					requestIndex=9;
				}
			} else {
				if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
					//return "home"+(uiType==null?"":uiType);		//commented for CSR 2018 - Session management
					
					// Added for CSR 2018 - Session management
					if(appSeqId == null){
						String lmsURL = Constants.BANK_ONLINE_URL;
						sessionId = null;						
						responseMessage = "error|"+"Sorry for the inconvenience, your session has been timed out. Please click <a href='" + lmsURL + "'> here </a> to start again";	
					//	logger.info("responseMessage 233..." + responseMessage);      
				        
						return "jsonResponsePage";
					}

				}else{
					bankLmsUser = SessionUtil.getBankLMSUser();
					if (bankLmsUser!=null) {
						appBankLmsUserId = bankLmsUser.getLmsUserId();
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
						//logger.info("AutoLoanAction.java LNo : 208 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if(SessionUtil.getLeadId()!=null){
							appLeadId = SessionUtil.getLeadId();
							lead = commonService.getLeadById(appLeadId);
							appSeqId=lead.getLeadAppSeqId();
						} else if(appSeqId==null){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						}
					}else{
						if(appBankLmsUserId==null){
							appBankLmsUserId = 4826;
						}
						bankLmsUser= commonService.getBankLmsUserById(appBankLmsUserId);
						SessionUtil.setBankLMSUser(bankLmsUser);
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
						//logger.info("AutoLoanAction.java LNo : 219 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if ( !ValidatorUtil.isValid(appSeqId) && !ValidatorUtil.isValid(appLeadId) ) {
							applicationTypeId=2;
							SessionUtil.setApplicationType(2);
						}else if (ValidatorUtil.isValid(appSeqId)) {
							SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
							if(request.getParameter("requestIndex")==null){
								applicationTypeId=0;
								SessionUtil.setApplicationType(0);
							}
						} else if (ValidatorUtil.isValid(appLeadId)) {
							SessionUtil.setLeadId(appLeadId);
							if(lead==null){
								lead = commonService.getLeadById(appLeadId);
							}
							email = lead.getLeadWorkEmail()!=null ? lead.getLeadWorkEmail().toString() : "";
							SessionUtil.setEmail(email);
							visitId=lead.getLeadVisitId();
							SessionUtil.setVisitIdAL(visitId);
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
							SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
							if(request.getParameter("requestIndex")==null){
								SessionUtil.setApplicationType(2);
								applicationTypeId=2;
							}
						} else if (SessionUtil.getAutoLoanApplicationSequenceId() != null) {
						}
						
						if(ValidatorUtil.isValid(applicationTypeId)){
							SessionUtil.setApplicationType(applicationTypeId);
						}
						if(request.getParameter("requestIndex")==null){
							requestIndex=-1;
						}
						if (appSeqId!=null) {
							visitId = autoLoanService.getVisitByAppSeqId(appSeqId);
							SessionUtil.setVisitIdAL(visitId);
						}
					}
				}
			}
			return getAutoLoan(Constants.AUTO_LOAN_DSR_ID);
		} catch (NullPointerException e) {
			logger.info("AutoLoanAction.java  LNo : 268 :: autoLoanDSR() ", e);
			return "home"+(uiType==null?"":uiType);
		} catch (SQLException e) {
			logger.info("AutoLoanAction.java  LNo : 268 :: autoLoanDSR() ", e);
			return "home"+(uiType==null?"":uiType);
		}

	}
	
	public String autoLoan(){
		try{
			SessionUtil.setUiType(uiType);
			isDsrPage="false";
			ajaxPostUrl=Constants.AUTO_LOAN_ACTION;
			SessionUtil.setApplicationType(0);
			request=RequestUtil.getServletRequest();
			if(SessionUtil.getBankLMSUser()!=null){
				isOnlineAndDsrActive=true;
				responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
				releaseSession(Constants.AUTO_LOAN_ID);
				Integer appSeqIdHomeLoan = null;
				SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);
				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);

				SessionUtil.setEducationLoanApplicationSequenceId(null);
				SessionUtil.setPersonalLoanApplicationSequenceId(null);
				
			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				

				if(appSeqId!=null){
					SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
					visitId = autoLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdAL(visitId);
				}
			}
			return getAutoLoan(Constants.AUTO_LOAN_ID);
		} catch(NullPointerException e){
			logger.info("AutoLoanAction.java LNo :: autoLoan() ", e);
		} catch(SQLException e){
			logger.info("AutoLoanAction.java LNo :: autoLoan() ", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String getAutoLoan(int moduleId){
		loanTypeId=Constants.AUTO_LOAN_ID;
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
			autoLoanPage = 1;
			stateManagerBean=stateManager.getState(request, moduleId);
			SbiUtil.getOcasSessionId(request);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			if(SessionUtil.getVisitIdAL()!=null){
				visitId = SessionUtil.getVisitIdAL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId == null ){
					if(SessionUtil.getAutoLoanApplicationSequenceId()==null || moduleId==Constants.AUTO_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.AUTO_LOAN_ID );
						/*if (!(campaignCode == null && offerCode == null && trackingCode == null)) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.AUTO_LOAN_ID, 0);
						}*/
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdAL(visitId);
						}else{
          logger.info("AutoLoanAction.java LN 334 unable to insert into visit entity.");
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}
			if(stateManagerBean.getState()==-1){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					metaInfo.setTitle(Constants.AUTO_LOAN_TITLE);
					metaInfo.setKeywords(Constants.AUTO_LOAN_KEYWORDS);
					metaInfo.setDescription(Constants.AUTO_LOAN_DESCRIPTION);
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
			

			if(stateManagerBean.getState()==41 || stateManagerBean.getState()==42){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerAutoImpl.verifyConcentOtp(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), 
							ajaxPostUrl, isDsrPage, otherRequest);
					if(json.get("status").toString().equalsIgnoreCase("success")){
						if(stateManagerBean.getState()==42){
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
			 
				if(stateManagerBean.getState()==33 || stateManagerBean.getState()==34){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerAutoImpl.varifySMSOTP(moduleId, stateManagerBean.getState(), 
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
			if(stateManagerBean.getState()==28 || stateManagerBean.getState()==29){
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
					json = processManagerAutoImpl.processCBSOTP(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, 
							appOTPVerifyType, inputOtp, userEmail, SessionUtil.getAutoLoanApplicationSequenceId(),
							SessionUtil.getAutoLoanCbsCallId());
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
				try{
					cbs = stateManagerBean.getCbs();
					if(isDsrPage.equalsIgnoreCase("false") && !cbs.getInfoprovideCBS().equals("on")){
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					}
					if(stateManagerBean.getValidatorResponse().isStatus()){
						CBSCallResponse cbsCallResponse = processManagerAutoImpl.processCbsCall(appSeqId, requestIndex, cbs, 
								isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), visitId, 
								SessionUtil.getAutoLoanCbsCallId(), null, null);
						if(cbsCallResponse.getStatus()!=null){
							if(cbsCallResponse.getStatus()==0){
								responseMessage = "error|"+cbsCallResponse.getResponseMsg();
								return "jsonResponsePage";
							}else if(cbsCallResponse.getStatus()==1){
								return "cbsOtpPage"+(uiType==null?"":uiType);
							}else if(cbsCallResponse.getStatus()==2){
								return "cbsVerifySmsOtpPage"+(uiType==null?"":uiType);
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 386 Exception occured:::::",e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==26){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						releaseSession(Constants.AUTO_LOAN_ID);
						appSeqId=null;
						appLeadId=null;
						firstName=null;
						mobileNo=null;
						email=null;
						alternateMobileNo= null;
						stateManagerBean.setState(-1);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 360 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
			}

			

			if(stateManagerBean.getState()==25){
				try {
					/*if(stateManagerBean.getValidatorResponse().isStatus()){
						String currentOcasId = SbiUtil.getOcasSessionId(request);
						releaseSession(Constants.AUTO_LOAN_ID);
						String appReferenceId = request.getParameter("generatePDF").substring(0, 12);
						logger.info("referece id from request is :: " + appReferenceId);
						if(ValidatorUtil.isValid(appReferenceId)){
							appForm = autoLoanService.getApplicationFormAutoLoanByAppReferenceId(appReferenceId) ;
							appSeqId = appForm.getAppSeqId();
							if(appForm!=null){
								if(ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())){
									logger.info("Original Ocas ID from app form " + appForm.getOcasID());
									logger.info("Current Ocas Id $$ " + currentOcasId);
									if (ValidatorUtil.isValid(appForm.getOcasID())) {
										logger.info("both ocas ID are same " + appSeqId);
										String filePath = Constants.PDF_GENRATION_BASE_PATH + Constants.AL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
										inputStream = new FileInputStream(new File(filePath));
										SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
										return "downLoadPDF";
									} else {
										logger.info("AutoLoanAction.java Ocas ID is not same as valid Ocas ID");
										SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
										responseMessage = Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
										return "jsonResponsePage";
									}
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
					}*/
					if (stateManagerBean.getValidatorResponse().isStatus()) {
						logger.info("Current Ocas Id $$ f ");
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if (ValidatorUtil.isValid(appSeqId)) {
							appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									String filePath = Constants.PDF_GENRATION_BASE_PATH
											+ Constants.AL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
									inputStream = new FileInputStream(new File(filePath));
									return "downLoadPDF";
								}
							}
						}
						infoMessage = 1;
						responseMessage = Constants.PDF_SESSION_TIMEOUT_REDIRECTION.replaceAll("CLICK_HERE",
								"<a href='" + Constants.PORT + Constants.CONTEXT + Constants.APPLICATION_STATUS_ACTION
										+ "'>here</a>");
						stateManagerBean.setState(-1);
					} else {
						String msg = CommonUtilites
								.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|" + msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo 393 :: stateManagerBean.getState()==25 ", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
			}

			if(stateManagerBean.getState()==24){

				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 402 Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			if(stateManagerBean.getState()==23){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isDsrPage.equalsIgnoreCase("true")){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
							
							getCallLogs(appSeqId, appLeadId);
						}
						return "callsLogDetails"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 420 Exception occured:::::", e);
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
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						logger.info("AutoLoanAction.java LNo:348 :: appSeqId :: "+appSeqId+" imageNo :: "+imageNo+" ajaxPostUrl :"+ajaxPostUrl + "imageName :: "+imageName);
						json = processManagerAutoImpl.processDeleteProductImage(appSeqId, imageNo, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, imageName);
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
					logger.info("AutoLoanAction.java LN 453 Exception occured:::::",e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==21){
				try{
					
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppAL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}

						if(ValidatorUtil.isValid(appForm.getAppDocPickupDate())){
							appForm.setAppDocPickupDateDT(DateUtil.changeDateFormatToDate(appForm.getAppDocPickupDate(), "MM/dd/yyyy"));
						}
						
						if (appForm.getAppDocPickupDateDT()!=null) {
							String dateStatus = DateUtil.checkDateInRange(appForm.getAppDocPickupDateDT(),appForm.getAppDocPickupTimeString(), 14);
							if (dateStatus.equals("1")) {
								responseMessage = "error|"+"Date range should be within 15 days from current date.|"+"appForm.appDocPickupDate|1";
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
						json = processManagerAutoImpl.processToDocumentPickupUploaded(appSeqId, appForm, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("AutoLoanAction.java LN 490 Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
						
			if(stateManagerBean.getState()==20){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						
						appForm=autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|N/A.pdf";
							return "jsonResponsePage";
						}
						if(appForm.getAppDownloadPdfFileName()!=null){
							SecureRandom rand = new SecureRandom();
							responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+((moduleId == Constants.AUTO_LOAN_ID.intValue())?Constants.AUTO_LOAN_ACTION:Constants.AUTO_LOAN_ACTION_DSR)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
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
					logger.info("AutoLoanAction.java LN 517 Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==18){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppAL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
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
								responseMessage = "error|"+"Date range should be within 15 days from current date.|"+"appForm.appDocPickupDate|1";
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
						
						json = processManagerAutoImpl.processToDocumentPickupUploaded(appSeqId, appForm, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("AutoLoanAction.java LN 544 Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
				
			}
			

			if(stateManagerBean.getState()==17){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppAL();
						if(isDsrPage.equalsIgnoreCase("false") 
								&& appForm.getTerms_conditions_check()!=null  
								&& !("on").equals(appForm.getTerms_conditions_check())){
							responseMessage = "error|"+"Terms conditions is invalid";
							return "jsonResponsePage";
						}
						
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						autoLoanPage=4;
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						appForm = processManagerAutoImpl.processSubmitQuote(appSeqId, stateManagerBean.getState(), appForm, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appForm.getError()!=null){
							responseMessage = "error|"+appForm.getError();
							return "jsonResponsePage";
						}
						
						
						generateUIBeanList(autoLoanPage);
						documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.AUTO_LOAN_ID, appForm.getAppAutoLoanId() );
						if(jsonJSArrayThankyou==null){
							jsonJSArrayThankyou = SbiUtil.populateJSValidation(18, moduleId).toString();
						}

						return "thankYouPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 581 Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}

			if(stateManagerBean.getState()==14 || stateManagerBean.getState()==15 || stateManagerBean.getState()==16){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(stateManagerBean.getState()==16){
							if (!Constants.AL_CAPTCHA_BY_PASS && otherRequest.getAlternateMobileNumber() == null) {
								if (appSeqId != null && ajaxPostUrl.equalsIgnoreCase("auto-loan")) {
									responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
									if (responseMessage != null) {
										return "jsonResponsePage";
									}
								}
							}
						}
						json = processManagerAutoImpl.processMobileOTP(appSeqId,stateManagerBean.getState(), null, 
								(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId,otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							//------------------------------------------------------------------
							if(otherRequest.getAlternateMobileNumber() == null && stateManagerBean.getState() != 15) {
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							}
							SessionUtil.setCaptch(null);
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							if(otherRequest.getAlternateMobileNumber() == null && stateManagerBean.getState() != 15) {
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							}
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LN 1146 stateManagerBean.getState()==14, 15, 16 ::"+stateManagerBean.getState()+" :: ", e);
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
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						String applyUrlData[] = null;
						String applyUrl =null;
						if(otherRequest!=null && otherRequest.getAppliedLoanId()!=null){
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
						
						appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if((appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK) ) 
								&&  appForm.getAppMobileVerified().equalsIgnoreCase("N") && appForm.getAppEmailVerified().equalsIgnoreCase("N") ){
							responseMessage = "error|Mobile OTP is not verified";
							return "jsonResponsePage";
						}else{
							quote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							quote.setLoanQuoteLoanProductId(Integer.parseInt(applyUrlData[0]));
							quote.setLoanQuoteLoanTenure(chosenTenure);
							quote.setLoanQuoteLoanAmount(chosenEligibility);
							quote.setLoanQuoteAppliedCoupon(appForm.getApploanappliedCoupon());
							quote.setLoanQuoteLoanAccountType(Integer.parseInt(applyUrlData[1]));
							loanScenarioBean = autoLoanHelper.callBRE(appForm, quote, bankLmsUser, quote.getLoanQuoteId(), quote.getLoanQuoteNewVisitId(), ajaxPostUrl, false);
							
							if(loanScenarioBean.getStatus()!=1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								return "jsonResponsePage";
							}
							appForm = loanScenarioBean.getApplicationAL();
							
							quote.setLoanQuoteLoanProductId(Integer.parseInt(applyUrlData[0]));
							quote.setLoanQuoteLoanTenure(chosenTenure);
							quote.setLoanQuoteLoanAmount(chosenEligibility);
							quote.setLoanQuoteAppliedCoupon(appForm.getApploanappliedCoupon());
							quote.setLoanQuoteLoanAccountType(Integer.parseInt(applyUrlData[1]));
							loanScenarioBean = autoLoanHelper.callBRE(appForm, quote, bankLmsUser, quote.getLoanQuoteId(), quote.getLoanQuoteNewVisitId(), ajaxPostUrl, false);
							
							if(loanScenarioBean.getStatus()!=1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								return "jsonResponsePage";
							}
							appForm = loanScenarioBean.getApplicationAL();
							
							appForm.setAppAutoLoanId(Integer.parseInt(applyUrlData[0]));
							appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
							appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
							appForm.setAppLoanProcessingFee((double) Double.parseDouble(applyUrlData[3]));
							appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));

							appForm.setAppEmiNmiRatio(Float.parseFloat(applyUrlData[6]));
							
							appForm.setAppLoanEmiDiscount(applyUrlData[7].toString().equalsIgnoreCase("-")?null:Double.parseDouble(applyUrlData[7]));
							appForm.setAppLoanProcessingFeeDiscount(applyUrlData[8].toString().equalsIgnoreCase("-")?null:(double) Double.parseDouble(applyUrlData[8]));
							appForm.setAppLoanInterestRateDiscount(applyUrlData[9].toString().equalsIgnoreCase("-")?null:Float.parseFloat(applyUrlData[9]));
							
							AlProduct product =  commonService.getAutoLoanProductById(appForm.getAppAutoLoanId());
							appForm.setAppProductTenureFlag(product.getALProductSliderTenure());
							boolean bankLmsUserRoleExceptContactCenter = false;
							if(Constants.NONCBS_SMS_CONSENT && isDsrPage.equalsIgnoreCase("true")){
								bankLmsUser = SessionUtil.getBankLMSUser();
								bankLmsUserRoleExceptContactCenter = commonService.getBankLmsUserRoleExceptContactCenter(bankLmsUser.getLmsUserId());
								if(bankLmsUserRoleExceptContactCenter){
									
									
									appForm.setAppMobileVerificationCode(SbiUtil.getVerificationCode(appForm.getAppMobileNo()));

								} 
							}
							
							//mask PII data - drop off leads
							if(isDsrPage.equalsIgnoreCase("true")) {
								
								boolean isTeleCallerUser = false;
								isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
								logger.info("isTeleCallerUser::::" + isTeleCallerUser);
								
								if (isTeleCallerUser) {
									if (appForm.getAppMobileNo() != null) {
										isMobileNoMask = "true";
										mobileNoMaskVal = appForm.getAppMobileNo().replaceAll("\\d(?=\\d{4})", "*");
									}
									if (appForm.getAppWorkEmail() != null) {
										isEmailMask = "true";
										emailMaskVal = appForm.getAppWorkEmail().replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
									}
									if (appForm.getAppAlternateMobileNumber() != null) {
										isalternateMobileNoMask = "true";
										alternateMobileNoMaskVal = appForm.getAppAlternateMobileNumber().replaceAll("\\d(?=\\d{4})", "*");
									}
									if (appForm.getAppPanCardNo() != null) {
										isPanNoMask = "true";
										panNoMaskVal = appForm.getAppPanCardNo().replaceAll("^.{6}", "******");
									}
								}
							}
							
							logger.info("AutoLoanAction.java LNo : 504 : Auto Loan appForm.getAppSeqId() "+appForm.getAppSeqId()+" appForm.getAppReferenceId() "+appForm.getAppReferenceId()+" SessionUtil.getApplicationType() "+SessionUtil.getApplicationType()+" appForm.getAppLoanStatusId() "+appForm.getAppLoanStatusId());
							appForm = autoLoanService.save(appForm);

							if(SessionUtil.getApplicationType()!=null){
								StatusRequest statusRequest = new StatusRequest();
								statusRequest.setCurrentStatus(appForm.getAppLoanStatusId());
								statusRequest.setHaveLoanOffer(true);
								statusRequest.setLoanTypeId(Constants.AUTO_LOAN_ID);
								statusRequest.setState(stateManagerBean.getState());
								statusRequest.setBankLMSUserId((bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
								statusRequest.setRsmDecision(0);
								statusRequest.setNsdlPANStatus(0);
								statusRequest.setAppPanCardNo(null);
								statusRequest.setApplicationType(SessionUtil.getApplicationType()!=null?SessionUtil.getApplicationType():0);
								statusRequest.setApplicationLeadType(appForm.getAppDataSourceId());
								StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
								if(statusManagerResponse.getStatus()!=0){
									appForm.setAppLoanStatusId(statusManagerResponse.getStatus());
								} else if(appForm.getAppLoanStatusId() == 0 ){
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
									return "jsonResponsePage";
								}
								if(statusManagerResponse.isEligibleToInsertLog()){
									autoLoanHelper.insertCallLog(appForm.getAppSeqId(),(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);
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
							autoLoanPage=3;
							quote= autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
							generateUIBeanList(autoLoanPage);
							if(jsonJSArray3AutoLoan==null){
								jsonJSArray3AutoLoan = SbiUtil.populateJSValidation(17, moduleId).toString();
							}
							if(Constants.NONCBS_SMS_CONSENT){
								if(bankLmsUserRoleExceptContactCenter){
									String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS_CONSENT, 0);
									msgBody = SbiUtil.urlEncode(msgBody);
									String SMS_TEXT = null;
									
									if(Integer.parseInt(appForm.getAppISDCode())==Integer.parseInt(Constants.COUNTRY_CODE_INDIA) ){
										SMS_TEXT=Constants.SMS_STRING_INDIAN;
									}else{
										SMS_TEXT=Constants.SMS_STRING_NRI;
									}
									SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
									SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", appForm.getAppMobileVerificationCode().toString());
									SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim());

									if (!Constants.DEPLOYMENT_MODE.equals("live")) {
										logger.info("OTP for Mobile Number: " + (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim() + " is " +appForm.getAppMobileVerificationCode().toString());
									}
									
									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
										responseMessage = "error|Mobile OTP service is down";
										return "jsonResponsePage";
									}
								}
							}
							return "thirdPage"+(uiType==null?"":uiType);
						}
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo :: 1186 stateManagerBean.getState()==11 ", e);
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
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(appSeqId != null){
							autoLoanPage = 2;
							populateForm(autoLoanPage, appSeqId, moduleId);
							return "coapplicantData"+(uiType==null?"":uiType);
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo :1214 :: Exception occured:::::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==11){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						quote = stateManagerBean.getQuoteAL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
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
						json = processManagerAutoImpl.processAddCoapplicant(appSeqId, quote, ajaxPostUrl);
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
					logger.info("AutoLoanAction.java LNo : 1252 ::Exception occured:::::", e);
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
					logger.info("AutoLoanAction.java LNo : 1271 :: Exception occured:::::",e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==9){
				try{
					stateManagerBean.setOriginalState(stateManagerBean.getState());
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(moduleId==Constants.AUTO_LOAN_ID){
							releaseSession(Constants.AUTO_LOAN_ID);
							appSeqId=null;
							lead=null;
						}
						if(request.getParameter("ALQuoteToken")!=null){
							String encyQuotId=aesEncryption.decrypt(request.getParameter("ALQuoteToken").toString());
							logger.info("AutoLoanAction.java LNo : 1289 :: encyQuotId : "+encyQuotId);
							if(ValidatorUtil.isValid(encyQuotId)){
								appSeqId = Integer.parseInt(encyQuotId);
								SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
								visitId = autoLoanService.getVisitByAppSeqId(appSeqId);
								SessionUtil.setVisitIdAL(visitId);
								stateManagerBean.setState(2);
							}else{
								stateManagerBean.setState(-1);
							}
						}else{
							logger.info("AutoLoanAction.java LNo : 1300 : Inside Auto Loan State Id 9: outside landing state start if LMS caling appId : "+app_id+" tokenId : "+token_id+" leadId : "+lead_id);
							request = RequestUtil.getServletRequest();
							sessionId = SbiUtil.getSessionId(request, sessionId);
							Cookie [] cookies= request.getCookies();
							bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
							if(bankLmsUser!=null){
								SessionUtil.setBankLMSUser(bankLmsUser);
								logger.info("bankLmsUser First Time : "+(bankLmsUser!=null?bankLmsUser.getLmsUserId():null) + "--" + "getLmsUserIntermediaryId First Time : "+bankLmsUser.getLmsUserIntermediaryId());
							}
							if(token_id!=null && SbiUtil.verifyCokkies(cookies,token_id) && commonService.checkLmsUserLogin(token_id) ){
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
										SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
									}
									stateManagerBean.setState(-1);
								}else if(app_id!=null && app_id.length() >0){

									appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
									if(!ValidatorUtil.isValid(appSeqId)){
										appSeqId=null;
									}
									
									SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
									stateManagerBean.setState(2);
								}else{
									stateManagerBean.setState(-1);
								}
							}else{
								releaseSession(Constants.AUTO_LOAN_ID);
								return "home"+(uiType==null?"":uiType);
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo. 1359 :: Exception occured:::::",e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==8){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){

							responseMessage = "error|Sorry for the inconvenience. Your session has been timed out, Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
							return "jsonResponsePage";
						}else{
							appForm=autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
							if(appForm == null){

								responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
								return "jsonResponsePage";
							}else{
								float appLoanInterestRate=0f;
								double processingFee=0;
								double emi=0;
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
									processingFee = (double) Math.round(new Double ((request.getParameter("pf"))));
								}
								if(processingFee<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								int moratoriumMonths=0;
								if(moratoriumMonths<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								appForm.setAppLoanInterestRate(appLoanInterestRate);
								appForm.setAppLoanProcessingFee(processingFee);
								appForm.setAppLoanEmi(emi);
								AlProduct product =  commonService.getAutoLoanProductById(appForm.getAppAutoLoanId());
								appForm.setAppProductTenureFlag(product.getALProductSliderTenure());
								
								double emiD = 0;
								if(ValidatorUtil.isValid(appForm.getAppLoanEmiDiscount())){
									emiD = appForm.getAppLoanEmiDiscount();
								}else{
									emiD = appForm.getAppLoanEmi();
								}
								float irD = 0f;
								if(appForm.getAppLoanInterestRateDiscount()!=null && appForm.getAppLoanInterestRateDiscount()>0){
									irD = appForm.getAppLoanInterestRateDiscount();
								}else{
									irD = appForm.getAppLoanInterestRate();
								}
								repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), irD, emiD,1,1,moratoriumMonths,1,product.getALProductSliderAmtMul(),null,null,null);
							}
						}
						return "repaymentScheduled"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo : 1440 :: Exception occured:::::",e);
					responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
					return "jsonResponsePage";
				} 
			}
			
			if(stateManagerBean.getState()==6){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						if(appSeqId == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						appForm=autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
						if(appForm == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						if(otherRequest.getEmail()!=null){
							appForm.setAppWorkEmail(otherRequest.getEmail());
						}
						if(!ValidatorUtil.isValid(appForm.getAppEmailAttemptCount())){
							appForm.setAppEmailAttemptCount(0);
						}
						if(appForm.getAppEmailAttemptCount()>Constants.APP_MAXIMUM_EMAIL_ATTEMPT_COUNT){
							responseMessage = "error|"+Constants.APP_MAXIMUM_EMAIL_ATTEMPT_MSG;
							return "jsonResponsePage";
						}
						appForm.setAppEmailAttemptCount(appForm.getAppEmailAttemptCount()+1);
						logger.info("AutoLoanAction.java LNo : 1495 app.getAppOTPAttemptCount() : "+appForm.getAppEmailAttemptCount()+" with AppSeqId "+appSeqId);
						String applyUrlData[] = null;
						String applyUrl =null;
						if(otherRequest.getProductSaveEmail()!=null){
							applyUrl = aesEncryption.decrypt(otherRequest.getProductSaveEmail());
							if(applyUrl==null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							applyUrlData = applyUrl.split("\\|");
							if(applyUrlData!=null && applyUrlData.length<5){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						appForm.setAppAutoLoanId(Integer.parseInt(applyUrlData[0]));
						appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
						appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
						appForm.setAppLoanProcessingFee(Double.parseDouble(applyUrlData[3]));
						appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
						appForm = autoLoanService.save(appForm);
						
						responseMessage = "success|Quotes sent to email";
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo. 1494 :: stateManagerBean.getState()==6 :::::",e);
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
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						
						if(stateManagerBean.getState()==5 || stateManagerBean.getState()==3){
							responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
							if (responseMessage!=null) {
								return "jsonResponsePage";
							}
						}
						json = processManagerAutoImpl.processWantUsToCallYou(appSeqId,stateManagerBean.getState(), visitId, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId ,otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							if(stateManagerBean.getState() != 4) {
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							}
							SessionUtil.setCaptch(null);
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("AutoLoanAction.java LNo. : 1540 :: stateManagerBean.getState()==3, 4, 5 ::"+stateManagerBean.getState()+" :: ", e);
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
						appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
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
						appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
						if(appForm == null){
							if(stateManagerBean.getOriginalState()==9){
								stateManagerBean.setState(-1);
							}else{
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						if(request.getParameter("ALQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
							if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
								stateManagerBean.setState(-1);
							}else{
								initAutoLoan(moduleId);
							}
						}
						if(stateManagerBean.getState()==2){
							quote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								if(request.getParameter("ALQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
									stateManagerBean.setState(-1);
								}else{
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
									return "jsonResponsePage";
								}
							}else{
								quote.setAppApplyingFrom(appForm.getAppApplyingFrom());
								if(appForm.getAppApplyingFrom()==2){
									if(appForm.getAppMobileNo()!=null){
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(appForm.getAppISDCode());
										quote.setAppNRIMobileNo(appForm.getAppMobileNo());
										quote.setAppISDCode(appForm.getAppISDCode());
									}
								
									if(appForm.getAppAlternateMobileNumber()!=null){
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());

									
										appForm.setAppAltISDCode(appForm.getAppAltISDCode());
										
									}
								
								}else{
									if(appForm.getAppMobileNo()!=null){
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
										quote.setAppMobile(appForm.getAppMobileNo());
										quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
									}
									
									if(appForm.getAppAlternateMobileNumber()!=null){
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
								
									
									}
								
								}
								
							
								
								SessionUtil.setEmail(appForm.getAppWorkEmail());
								firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
								SessionUtil.setApplicantName(firstName);
								quote.setAppEmail(appForm.getAppWorkEmail());
								Integer productSliderTenure = null;
								if(otherRequest!=null && otherRequest.getProductSliderTenure() !=null && !otherRequest.getProductSliderTenure().equalsIgnoreCase("")){
									productSliderTenure =Integer.parseInt(otherRequest.getProductSliderTenure());
								}
								if(otherRequest!=null && otherRequest.getChosenTenure()!=null){
									chosenTenure =Integer.parseInt(otherRequest.getChosenTenure());
									if(productSliderTenure!= null && productSliderTenure.intValue()==2){
										chosenTenure = chosenTenure*Constants.LOAN_TENURE_MULTIPLER_FACTOR;
									}
									
									quote.setLoanQuoteLoanTenure(chosenTenure);
								}
								if(otherRequest!=null && otherRequest.getChosenEligibility()!=null){
									BigDecimal amount = new BigDecimal(otherRequest.getChosenEligibility());
									BigDecimal multiplier = new BigDecimal("100000");
									chosenEligibility=amount.multiply(multiplier).doubleValue();
									quote.setLoanQuoteLoanAmount(chosenEligibility);
								
								}
								if(otherRequest!=null && otherRequest.getDiscountCouponName()!=null){
									quote.setLoanQuoteAppliedCoupon(otherRequest.getDiscountCouponName());
								}
								if(otherRequest!=null && otherRequest.getChosenLoanAccountType()!=null && !otherRequest.getChosenLoanAccountType().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanAccountType(Integer.parseInt(otherRequest.getChosenLoanAccountType()));
					    		}else{
					    			quote.setLoanQuoteLoanAccountType(1);
					    		}
								if(otherRequest!=null && otherRequest.getChosenProductId() !=null && !otherRequest.getChosenProductId().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanProductId(Integer.parseInt(otherRequest.getChosenProductId().toString()));
								}else{
									quote.setLoanQuoteLoanProductId(1);
								}

								loanScenarioBean=(LoanScenarioBean) processManagerAutoImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

								if(loanScenarioBean.getStatus() != 1){
									responseMessage = "error|"+loanScenarioBean.getMessage();
									if(request.getParameter("ALQuoteToken")!=null){
										stateManagerBean.setState(-1);
										return "jsonResponsePage";
									}else{
										if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
											responseMessage =  "error|"+loanScenarioBean.getMessage();
											return "jsonResponsePage";	
										}else{
											stateManagerBean.setState(0);
											return "jsonResponsePage";
										}
									}
								}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(),  loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal= SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(),  loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
									if(appSeqId==null){
										appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
									}
									autoLoanPage=2;
									populateForm(1, appSeqId, moduleId);
									if(request.getParameter("ALQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
										if(ValidatorUtil.isValid(appForm.getAppReferenceId())){
											appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
											return "applicationTrack"+(uiType==null?"":uiType);
										}
										initAutoLoan(moduleId);
										jsonJSArray1AutoLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
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
					stateManagerBean.setState(0);
					logger.info("AutoLoanAction.java LNo. 1691 stateManagerBean.getState()==2 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if(stateManagerBean.getState()==1){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						quote = stateManagerBean.getQuoteAL();
						if(SessionUtil.getApplicationType()==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(isDsrPage.equalsIgnoreCase("false") && !quote.getInfoprovide().equals("on")){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						}
						
						if(!Constants.AL_CAPTCHA_BY_PASS && ajaxPostUrl.equalsIgnoreCase("auto-loan")){
							if(appSeqId==null){
								responseMessage = SbiUtil.checkCaptcha(quote.getCaptcha());
								if (responseMessage!=null) {
									return "jsonResponsePage";
								}
							}
						}
						responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
						
						if(SessionUtil.getAutoLoanApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
							appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
							if(appForm!=null && appForm.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)){
								if(appForm.getAppQuoteId() != null){
									ApplicationFormAutoLoanQuote quoteOld = null;
									quoteOld = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());




									if(ValidatorUtil.isValid(quoteOld.getLoanQuotePreEMIs())){
										quote.setLoanQuotePreEMIs(quoteOld.getLoanQuotePreEMIs());
									}
									if(quoteOld.getLoanQuoteRepaymentStartDate()!=null){
										quote.setLoanQuoteRepaymentStartDate(quoteOld.getLoanQuoteRepaymentStartDate());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteoutstandingHomeLoanAmount())){
										quote.setLoanQuoteoutstandingHomeLoanAmount(quoteOld.getLoanQuoteoutstandingHomeLoanAmount());
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
						if(quote.getLoanQuoteDop()!=null){
							quote.setLoanQuoteDopDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDop(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteStartDateOfCurrentLoan()!=null){
							quote.setLoanQuoteStartDateOfCurrentLoanDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteStartDateOfCurrentLoan(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuotePresentValueOfProperty()!=null){
							quote.setLoanQuotePresentValueOfProperty(quote.getLoanQuotePresentValueOfProperty());
						}
						if(quote.getLoanQuoteoutstandingTopupLoanAmount()!=null){
							quote.setLoanQuoteoutstandingTopupLoanAmount(quote.getLoanQuoteoutstandingTopupLoanAmount());
						}
						if(quote.getLoanQuoteIsMortagageCreated()!=null){
							quote.setLoanQuoteIsMortagageCreated(quote.getLoanQuoteIsMortagageCreated());
						}
						if(quote.getLoanQuoteIsPossessionComplete()!=null){
							quote.setLoanQuoteIsPossessionComplete(quote.getLoanQuoteIsPossessionComplete());
						}

						
						//added for alternate number on 14/11/2022
						String alternateMob = quote.getAlternateMobileNumber() != null ? quote.getAlternateMobileNumber() : "";
						String altIsd = quote.getAppAltISDCode() != null ? quote.getAppAltISDCode() : "";
						
						//added for server Side Validation of Apsec point 
						String firstNameData=quote.getLoanQuoteFirstName();
						//String mobilenum=quote.getAppMobile();
						String mobilenum=quote.getAppMobile() != null ? quote.getAppMobile() : quote.getAppNRIMobileNo();
						
						if(isDsrPage.equalsIgnoreCase("true")) {
							String pattern = "^((?=[A-Za-z. ])(?![_\\\\-]).)*$";
							String patternMobile =  "^[6789][0-9]{4,9}$";
							
							
								if(!firstNameData.matches(pattern))
								{
									responseMessage = "error|Name is not in correct format ";
									return "jsonResponsePage";
								}
								if(!mobilenum.matches(patternMobile))
								{
									responseMessage = "error|Mobile number is not in correct format ";
									return "jsonResponsePage";
								}
							 
							 
							if(((!alternateMob.trim().equals("") && alternateMob.length() != 0) && (altIsd.trim().equals("") || altIsd.length() == 0)) 
									|| ((!altIsd.trim().equals("") && altIsd.length() != 0) && (alternateMob.trim().equals("") || alternateMob.length() == 0))) {
								responseMessage = "error|Provide alternate mobile and ISD together";
								return "jsonResponsePage";
							 } else if (alternateMob.startsWith("0") || altIsd.startsWith("0") ) {
						    	responseMessage = "error|Alternate number should not start with 0";
						    	return "jsonResponsePage";
						    } else  if ((alternateMob.length() < 8  || alternateMob.length() > 16) &&  alternateMob.length() != 0 ) {
						    	responseMessage = "error|Provide alternate valid mobile number";
						    	return "jsonResponsePage";
						    } 
						}
						
						
						
						
						loanScenarioBean=(LoanScenarioBean) processManagerAutoImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser, loanTypeId);

						if(loanScenarioBean.getStatus() != 1){
							responseMessage = "error|"+loanScenarioBean.getMessage();
							return "jsonResponsePage";
						}else{
							Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(),  loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
							loanScenarioBean.setManualEligVal(manualEligVal);
							Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(),  loanScenarioBean.getProductSliderTenure()==1?true:false);
							loanScenarioBean.setManualTenureVal(manualTenureVal);
							if(appSeqId==null){
								appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
							}
							if(appSeqId == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
								return "jsonResponsePage";
							}
							autoLoanPage=2;
							populateForm(1, appSeqId, moduleId);
							SessionUtil.setCaptch(null);
							return "secondPage"+(uiType==null?"":uiType);
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					stateManagerBean.setState(-1);
					logger.info("AutoLoanAction.java LNo. 1823 :: stateManagerBean.getState()==1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if (stateManagerBean.getState()==0) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						autoLoanPage=1;
						if(SessionUtil.getApplicationType()==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
							if(appSeqId != null){
								appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.AUTO_LOAN_ID){
										releaseSession(Constants.AUTO_LOAN_ID);
										appSeqId=null;
										lead=null;
									}
								}
							}
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						}
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						populateForm(-1, appSeqId, moduleId);
						populateFirstPageContent(Constants.AUTO_LOAN_ID,1);
						jsonJSArray1AutoLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						initAutoLoan(moduleId);
						includeJs=true;
						getDisplayUpdate((appSeqId!=null), (appForm!=null && appForm.getAppSubTypeId()!=null)?appForm.getAppSubTypeId():0, applicationTypeId );
						return "firstPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch(NullPointerException e){
					stateManagerBean.setState(-1);
					logger.info("AutoLoanAction.java LNo. 1870 :: stateManagerBean.getState()==0 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if (stateManagerBean.getState()==-1) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						autoLoanPage=1;
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							if(appSeqId==null){
								appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
							}
							if(isOnlineAndDsrActive){
								appSeqId=null;
								responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
							}
							if(appSeqId != null){
								appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
								if(request.getParameter("ALQuoteToken")!=null){
									String encyQuotId=aesEncryption.decrypt(request.getParameter("ALQuoteToken").toString());
									infoMessage=1;
									if(ValidatorUtil.isValid(encyQuotId)){
										if(appForm == null){
											responseMessage = ""+Constants.SORRY_FOR_INCONVENIENCE;
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
									if(moduleId==Constants.AUTO_LOAN_ID){
										releaseSession(Constants.AUTO_LOAN_ID);
										appSeqId=null;
										lead=null;
										appForm = null;
										quote = null;
									}
								}
							}
						}
						if(ValidatorUtil.isValid(request.getParameter("crmLeadId"))){
							Integer crmLeadId = Integer.parseInt(request.getParameter("crmLeadId"));
							if(crmLeadId!=null){
								SessionUtil.setApplicationCRMLeadId(crmLeadId);
							}
						}
						if(SessionUtil.getLeadId()!=null){
						lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getAutoLoanApplicationSequenceId();
						}
						if(isOnlineAndDsrActive){
							appSeqId=null;
							responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
						}
                     	if(appSeqId !=null){
							appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())){
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
											releaseSession(Constants.AUTO_LOAN_ID);
											appSeqId=null;
											appForm = null;
											quote = null;
										}else{
											if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
												releaseSession(Constants.AUTO_LOAN_ID);
												appSeqId=null;
												appForm = null;
												quote = null;
											}
										}
									}
								}
							}
						}
                     	if(appSeqId !=null){
                     		appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
                     		if(appForm!=null){
                     			if(appForm.getAppSubTypeId() == Constants.APP_APP_SUB_TYPE_ID_CBS){
                     				if(isDsrPage.equalsIgnoreCase("true")){
                     					if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
                     						releaseSession(Constants.AUTO_LOAN_ID);
                     					}
                     				}else{
                     					if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
                     						releaseSession(Constants.AUTO_LOAN_ID);
										}
									}
								}
								if(appForm.getAppApplyingFrom()==2){
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
											SessionUtil.setMobile(appForm.getAppMobileNo());
									   SessionUtil.setISDCode(appForm.getAppISDCode());
									}
									if(appForm.getAppAlternateMobileNumber()!=null){
										alternateMobileNo = appForm.getAppAlternateMobileNumber();
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
									}
								} else {
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									}

									if(appForm.getAppAlternateMobileNumber()!=null){
										alternateMobileNo = appForm.getAppAlternateMobileNumber();
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
										//Added for displaying alt no in DSR page for UPLOAD functionality
										SessionUtil.setISDCode(appForm.getAppAltISDCode());
									}
								}
								
								if(appForm.getAppWorkEmail()!=null){
									email=appForm.getAppWorkEmail();
									SessionUtil.setEmail(appForm.getAppWorkEmail());
								}
								if(appForm.getAppFirstName()!=null){
									firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
									SessionUtil.setApplicantName(firstName);
								}
								
								logger.info(" altIsdVal=appForm.getAppAltISDCode(); 2131 " +appForm.getAppAltISDCode());
								
								//mask PII data
								if(isDsrPage.equalsIgnoreCase("true")) {
									
									boolean isTeleCallerUser = false;
									isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
									logger.info("isTeleCallerUser::::" + isTeleCallerUser);
									
									if (isTeleCallerUser) {
										if (appForm.getAppMobileNo() != null) {
											isMobileNoMask = "true";
											mobileNoMaskVal = appForm.getAppMobileNo().replaceAll("\\d(?=\\d{4})", "*");
										}
										if (appForm.getAppWorkEmail() != null) {
											isEmailMask = "true";
											emailMaskVal = appForm.getAppWorkEmail().replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
										}
										if (appForm.getAppAlternateMobileNumber() != null) {
											isalternateMobileNoMask = "true";
											altIsdVal=appForm.getAppAltISDCode();
											alternateMobileNoMaskVal = appForm.getAppAlternateMobileNumber().replaceAll("\\d(?=\\d{4})", "*");
										}
										if (appForm.getAppPanCardNo() != null) {
											isPanNoMask = "true";
											panNoMaskVal = appForm.getAppPanCardNo().replaceAll("^.{6}", "******");
										}
									}
								}
								
							}else{
								appSeqId = null;
								SessionUtil.setAutoLoanApplicationSequenceId(appSeqId);
							}
							populateForm(-1, appSeqId, moduleId);
						}else{
							generateUIBeanList(moduleId, -1);
						}
						initAutoLoan(moduleId);
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
							getCallLogs(appSeqId, appLeadId);
						}
						populateFirstPageContent(Constants.AUTO_LOAN_ID,1);

						jsonJSArray1AutoLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();
						if(SessionUtil.getApplicantName()!=null){
							firstName=SessionUtil.getApplicantName();
						}
						if(SessionUtil.getMobile()!=null){
							mobileNo=SessionUtil.getMobile();
						}
						if (SessionUtil.getalternateMobileNumber()!= null) {
							alternateMobileNo = SessionUtil.getalternateMobileNumber();
						}
						
						if(SessionUtil.getEmail()!=null){
							email=SessionUtil.getEmail();
						}
						getDisplayUpdate((appSeqId!=null), (appForm!=null && appForm.getAppSubTypeId()!=null)?appForm.getAppSubTypeId():0, applicationTypeId );
						return "homePage"+(uiType==null?"":uiType);
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch(NullPointerException e){
					logger.info("AutoLoanAction.java LNo. 2037:: stateManagerBean.getState()==-1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
		} catch(NullPointerException e){
			logger.info("AutoLoanAction.java LNo. 2043 :: getAutoLoan(int moduleId) ", e);
		} catch(UnsupportedEncodingException e){
			logger.info("AutoLoanAction.java LNo. 2044 :: getAutoLoan(int moduleId) ", e);
		} catch(JSONException e){
		   logger.info("AutoLoanAction.java LNo. 2045 :: getAutoLoan(int moduleId) ", e);
	    } catch(SQLException e){
	       logger.info("AutoLoanAction.java LNo. 2046 :: getAutoLoan(int moduleId) ", e);
        }catch(FileNotFoundException e){
 	       logger.info("AutoLoanAction.java LNo. 2046 :: getAutoLoan(int moduleId) ", e);
        }catch(java.text.ParseException e){
		logger.info("AutoLoanAction.java LNo. 2047 :: getAutoLoan(int moduleId) ", e);
	    }
		return "homePage"+(uiType==null?"":uiType);
	}
	
	
	
	private void populateForm(int pageNo, Integer appSeqId, Integer moduleId){
		try {
			if(appSeqId!=null){
				if(appForm==null){
					appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
				}
				if(appForm!=null){
					quote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote!=null){
						appSeqId=null;
						quote.setAppEmail(appForm.getAppWorkEmail());
						if(appForm.getAppApplyingFrom()==2){
							quote.setAppNRIMobileNo(appForm.getAppMobileNo());
							quote.setAppISDCode(appForm.getAppISDCode());

						}else{
							quote.setAppMobile(appForm.getAppMobileNo());
							quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
						}
						if(quote!=null && (pageNo==1 || pageNo==-1)){
							generateUIBeanList(moduleId, pageNo);
						}
					}else{
						this.appSeqId=null;
						SessionUtil.setAutoLoanApplicationSequenceId(null);
					}
				}else{
					generateUIBeanList(moduleId, pageNo);
				}
			}else{
				generateUIBeanList(moduleId, pageNo);
			}
		} catch (NullPointerException e) {
			logger.info("AutoLoanAction.java LNo. 2082 :: populateForm(int pageNo, Integer appSeqId, Integer moduleId) ", e);
		} catch (NoResultException e) {
			logger.info("AutoLoanAction.java LNo. 2083 :: populateForm(int pageNo, Integer appSeqId, Integer moduleId) ", e);
		} catch (SQLException e) {
		    logger.info("AutoLoanAction.java LNo. 2084 :: populateForm(int pageNo, Integer appSeqId, Integer moduleId) ", e);
	    }
		
	}
	private void getIntermediary(Integer moduleId){
		try {
			if(moduleId==Constants.AUTO_LOAN_DSR_ID){
				if(bankLmsUser==null){
					bankLmsUser = SessionUtil.getBankLMSUser();
				}
				if(bankLmsUser!=null && bankLmsUser.getLmsUserIntermediaryId()!=null){
					MasterLmsIntermediary masterLmsIntermediary = commonService.getLmsIntermediaryRelByIntermediatryId(bankLmsUser.getLmsUserIntermediaryId());
					if(masterLmsIntermediary!=null){
						SessionUtil.setLMSIntermediaryRelation(masterLmsIntermediary.getLmsIntermediaryName());
						SessionUtil.setBankLMSUser(bankLmsUser);
						intermediaryRel = commonService.findRelationByIntermediatryIdAndLoanType(masterLmsIntermediary.getLmsIntermediaryId(), Constants.AUTO_LOAN_ID);
						String ids=(intermediaryRel.getIntermediaryRelLoanCat1()!=null?intermediaryRel.getIntermediaryRelLoanCat1():"")
								+(intermediaryRel.getIntermediaryRelLoanCat2()!=null?","+intermediaryRel.getIntermediaryRelLoanCat2():"")
								+(intermediaryRel.getIntermediaryRelLoanCat3()!=null?","+intermediaryRel.getIntermediaryRelLoanCat3():"");
						SessionUtil.setLMSIntermediaryCatagories(ids);
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
			logger.info("AutoLoanAction.java LNo. 2115 :: getIntermediary(Integer moduleId) ", e);
		}
	}
	
	public void initAutoLoan(Integer moduleId) {
		try {

				initLoanJSONArrayAutoLoan = new JSONArray();
				JSONObject json1 = new JSONObject();
				json1.put("preferredState",SbiUtil.getAllStateId(Constants.AUTO_LOAN_ID, null, null, null, null, null));
				initLoanJSONArrayAutoLoan.put(json1);
				
				JSONObject json2 = new JSONObject();
				json2.put("loanWithBankId",SbiUtil.getAllBank( Constants.AUTO_LOAN_ID));
				initLoanJSONArrayAutoLoan.put(json2);
				
				
				
				JSONObject json4 = new JSONObject();
				json4.put("dealerName",SbiUtil.getCarDealer());
				initLoanJSONArrayAutoLoan.put(json4);
				
				JSONObject json5 = new JSONObject();
				json5.put("residentCountries", SbiUtil.getAllCountries());
				initLoanJSONArrayAutoLoan.put(json5);
				
				JSONObject json6 = new JSONObject();
				json6.put("relationShipNRI", SbiUtil.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,1));
				initLoanJSONArrayAutoLoan.put(json6);
				
				JSONObject json7 = new JSONObject();
				json7.put("relationShipIndia", SbiUtil.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,0));
				initLoanJSONArrayAutoLoan.put(json7);
				
				JSONObject json8 = new JSONObject();
				json8.put("industries", SbiUtil.getIndustryTypeByLoanId(Constants.AUTO_LOAN_ID));
				initLoanJSONArrayAutoLoan.put(json8);
				
				JSONObject json9 = new JSONObject();
				json9.put("salaryPackages", SbiUtil.getAllSalaryPackageByLoanType(Constants.AUTO_LOAN_ID, null));
				initLoanJSONArrayAutoLoan.put(json9);
				
				JSONObject json10 = new JSONObject();
				json10.put("loanPurposeLinks", SbiUtil.getAllLoanPurposeLinks());
				initLoanJSONArrayAutoLoan.put(json10);
				
				JSONObject json11 = new JSONObject();
				json11.put("loanCategory", SbiUtil.getLoanCatogory( Constants.AUTO_LOAN_ID, 6));
				initLoanJSONArrayAutoLoan.put(json11);
				

			getIntermediary(moduleId);
		} catch (NullPointerException e) {
			logger.info("AutoLoanAction.java LNo. 2170 :: initAutoLoan(Integer moduleId) ",e);
			initLoanJSONArrayAutoLoan.put("error");
		} catch (JSONException e) {
			logger.info("AutoLoanAction.java LNo. 2170 :: initAutoLoan(Integer moduleId) ",e);
			initLoanJSONArrayAutoLoan.put("error");
		}
	}

	private void getCallLogs(Integer appSeqId, Integer appLeadId){
		try{
			if(ValidatorUtil.isValid(appSeqId) || ValidatorUtil.isValid(appLeadId)){
				callLogDetails = commonService.getCallLogByLeadAppId(Constants.AUTO_LOAN_ID, appSeqId, appLeadId  );
			}
		} catch(NullPointerException e){
			logger.info("AutoLoanAction.java LNo. 2181 :: getCallLogs(appSeqId) ", e);
		} catch(SQLException e){
			logger.info("AutoLoanAction.java LNo. 2181 :: getCallLogs(appSeqId) ", e);
		}
	}
	
	public StreamResult carModelByCompanyId() {
		JSONObject json = new JSONObject();
		try {
			logger.info("inside the carModelByCompanyId carCompanyId value cheking  = " +carCompanyId);
			json.put("cardata", SbiUtil.getCarModelByCompanyId( carCompanyId));
		} catch (JSONException e) {
			logger.info("AutoLoanAction.java LNo.2220 :: carModelByCompanyId() ", e);
		} 
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}

	
	public StreamResult carVariantByCarModuleId() throws SQLException {
		JSONObject json = new JSONObject();
		try {
			logger.info("carCompanyId value cheking  = " +carCompanyId);
			json.put("cardata", SbiUtil.getCarVariantByModelId( carModuleId, carCompanyId));
		} catch (JSONException e) {
			logger.info("AutoLoanAction.java LNo.2230 :: carVariantByCarModuleId() ", e);
		} 
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	private void generateUIBeanList(int pageNo){
		try{
			if(pageNo==3){
				if(appForm!=null && appForm.getAppPermanentStateId()!=null && appForm.getAppPermanentCityId()!=null){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Permanent(mapCity);
					if(appForm.getAppPermanentCityId()!=null && appForm.getAppPermanentCityId().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppPermanentDistrictId()!=null){
							beanList.setDistrictsPermanent(mapDistrict);
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
				
				List <MasterRelationshipWithBank>relationshipWithBanks = commonService.getAllRelationshipWithBank(Constants.AUTO_LOAN_ID);
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				logger.info("AutoLoanAction.java LNo:: 2238 relationshipWithBanks="+relationshipWithBanks);
				if(relationshipWithBanks!=null)
				{
					logger.info("AutoLoanAction.java LNo:: 2270 checking cbs="+cbs +" : SessionUtil.getAutoLoanCbsCallId()="+SessionUtil.getAutoLoanCbsCallId());
					
					if(SessionUtil.getAutoLoanCbsCallId()!=null && SessionUtil.getAutoLoanCbsCallId()!=0) 
					{
						if(quote.getLoanQuoteEmploymentTypeId()!=null && quote.getLoanQuoteEmploymentTypeId()==9  && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1) 
						{
							if(quote.getLoanQuoteHaveSalaryAccountWithSbi()!=null && quote.getLoanQuoteHaveSalaryAccountWithSbi().equalsIgnoreCase("Y"))   
							{
								for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) 
								{
										if(masterRelationshipWithBank.getRelationshipId()!=null && (masterRelationshipWithBank.getRelationshipId()==20) )
										{
											maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
											break;
										}
								}
							} 
							else
							{
								for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) 
								{
										if(masterRelationshipWithBank.getRelationshipId()!=null && (masterRelationshipWithBank.getRelationshipId()==15 || masterRelationshipWithBank.getRelationshipId()==16  || masterRelationshipWithBank.getRelationshipId()==17  || masterRelationshipWithBank.getRelationshipId()==18 || masterRelationshipWithBank.getRelationshipId()==20) )
										{
											maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
										}
								}
							}
						}
						else  
						{
							for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) 
							{
									if(masterRelationshipWithBank.getRelationshipId()!=null && (masterRelationshipWithBank.getRelationshipId()==15 || masterRelationshipWithBank.getRelationshipId()==16  || masterRelationshipWithBank.getRelationshipId()==17  || masterRelationshipWithBank.getRelationshipId()==18) )
									{
										maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
									}
							}
						}
					}
					else    
					{
						for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) 
						{
								if(masterRelationshipWithBank.getRelationshipId()!=null && masterRelationshipWithBank.getRelationshipId()==19)
								{
									maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
									break;
								}
						}

					}
				}   
				beanList.setRelationshipWithBank(maps);

				
				List <MasterResidenceType>residenceTypes = commonService.getAllResidenceType(Constants.AUTO_LOAN_ID);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				if(residenceTypes!=null){
					for (MasterResidenceType masterResidenceType : residenceTypes) {
						maps.put(masterResidenceType.getResidenceTypeId(), masterResidenceType.getResidenceTypeName());
					}
				}
				beanList.setResidenceTypes(maps);
				
				List <MasterDealer> dealerNames = commonService.getAllDealer();
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				if(dealerNames!=null){
					for (MasterDealer masterDealer : dealerNames) {
						maps.put(masterDealer.getDealerId(), masterDealer.getDealerName());
					}
				}
				beanList.setAutodealers(maps);
				
				if(appForm!=null && appForm.getAppCoapplicantState_id_1()!=null && appForm.getAppCoapplicantCity_id_1()!=null){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Coapplicant1(mapCity);
					
					if(appForm.getAppCoapplicantCity_id_1()!=null && appForm.getAppCoapplicantCity_id_1().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppCoapplicantDistrictId1()!=null){
							beanList.setDistrictsCoapplicant1(mapDistrict);
						}
					}
				}	
			}else if(pageNo==4){
				if(appForm!=null && appForm.getAppPickupStateId()!=null && appForm.getAppPickupStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Pickup(mapCity);
					
					if(appForm!=null && appForm.getAppPickupCityId()!=null && appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)){
						beanList.setDistrictPickup(mapDistrict);
					}
				}
			}
			
			
			if(pageNo==3 || pageNo==4){
				if(appForm!=null && appForm.getAppStateId()!=null && appForm.getAppStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1(mapCity);
					if(appForm.getAppCityId()!=null && appForm.getAppCityId() >0){
						if(appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER)){
							if(appForm.getAppDistrictId()!=null){
								beanList.setDistricts(mapDistrict);
									if(appForm.getAppDistrictId()!=null && appForm.getAppDistrictId() >0){
										beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.AUTO_LOAN_ID, null, null, appForm.getAppDistrictId(), null, null, null, null, null));
					    			}
							}
						}else{
								if(appForm.getAppBranchId()!=null && appForm.getAppBranchId() >0 && appForm.getAppCityId()!=null && appForm.getAppCityId() >0){
									beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.AUTO_LOAN_ID, null, appForm.getAppCityId(), null, null, null, null, null, null));
								}
						}
					}
				}
				
				if(appForm!=null && appForm.getAppOfficeStateId()!=null && appForm.getAppOfficeStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Office(mapCity);
					if(pageNo==3 || pageNo==4){
						if(appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)){
							if(appForm.getAppOfficeDistrictId()!=null){
								beanList.setDistrictsOffice(mapDistrict);
							}
						}
					}
				}
			}
		} catch(NullPointerException e){
			logger.info("AutoLoanAction.java :: LNo :: 2437 : generateUIBeanList(int pageNo) ", e);
		} catch(SQLException e){
			logger.info("AutoLoanAction.java :: LNo :: 2437 : generateUIBeanList(int pageNo) ", e);
		}
	}


	private void generateUIBeanList(Integer moduleId, int pageNo){
		try {
			Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
			List<MasterEmploymentType> employmentTypes = null;
			if(pageNo==-1){
				getIntermediary(moduleId);
				List<MasterLoanPurpose> loanpurposes = null;
				List<MasterLoanCategory> loanCategories = null;
				if(intermediaryRel!=null){
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					if(intermediaryRel.getIntermediaryRelLoanPurpose()!=null){
					loanpurposes = commonService.getLoanPurposeByIds(intermediaryRel.getIntermediaryRelLoanPurpose());
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					for (MasterLoanPurpose purpose : loanpurposes) {
						maps.put(purpose.getLpId(), purpose.getLpTypeValue());
						if(quote!=null){
							if(purpose!=null && purpose.getLpId()!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
								if(purpose.getLpUrl()!=null){
									loanPurposeUrl=purpose.getLpUrl();	
								}								
							}
						}
					 }
					beanList.setLoanPurposes(maps);
					}
					
					
					String ids=intermediaryRel.getIntermediaryRelLoanCat1()!=null?","+intermediaryRel.getIntermediaryRelLoanCat1():""
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
					
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					if(intermediaryRel.getIntermediaryRelRelatedId()!=null){
					MasterDealer dealer = commonService.findCarDealerById(intermediaryRel.getIntermediaryRelRelatedId());
						if (dealer != null) {
							if (dealer.getDealerCompanyMakeId() != null) {
								MasterCarCompany carCompany = autoLoanService.getCarCompanyById(dealer.getDealerCompanyMakeId());
								if (carCompany != null) {							
										maps.put(carCompany.getCompanyId(),carCompany.getCompanyName());									 
								}
							}
						}
					beanList.setCarMake(maps);
					}					
					
				}else{
					loanpurposes = commonService.getAllLoanPurposeByLoanType(Constants.AUTO_LOAN_ID);
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					for (MasterLoanPurpose purpose : loanpurposes) {
						maps.put(purpose.getLpId(), purpose.getLpTypeValue());
						if(quote!=null){
							if(purpose.getLpId()!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
								if(purpose.getLpUrl()!=null){
								loanPurposeUrl=purpose.getLpUrl();
								}
							}
						}
					}
					beanList.setLoanPurposes(maps);
					if(quote!=null){
						loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(Constants.AUTO_LOAN_ID, 6);
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
					}
						
					List<MasterCarCompany> carCompanies = autoLoanService.getCarCompany();
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					if(carCompanies!=null){
						for (MasterCarCompany carCompany : carCompanies) {
							maps.put(carCompany.getCompanyId(), carCompany.getCompanyName());
						}
						beanList.setCarMake(maps);
					}
				}

				employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.AUTO_LOAN_ID);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (MasterEmploymentType employmentType : employmentTypes ) {
					maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
				}
				beanList.setEmployementTypes(maps);
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				maps.put(1, "Resident Indian");
				if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH
						 || Constants.BANK_ID == Constants.BANK_ID_SBT){					
					if(quote!=null && quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteLoanCategoryId()==8){
						maps.put(2, "NRI");
					}
				}
				beanList.setResidentTypes(maps);
				
				if(quote!=null){
					
					maps = null; 
					if(quote.getLoanQuoteEmploymentTypeId()!=null && quote.getLoanQuoteEmploymentTypeId()>0){
						List<MasterEmployeeOccupationType> occTypes = commonService.getAllOccupationTypesByEmploymentType(quote.getLoanQuoteEmploymentTypeId());
						maps = null; 
						if(occTypes!=null){
							maps = new LinkedHashMap<Integer, String>();
							logger.info(" SessionUtil.getAutoLoanCbsCallId()=="+SessionUtil.getAutoLoanCbsCallId());
							for (MasterEmployeeOccupationType occType : occTypes) {
								logger.info(" occType.getOccupationId()=="+occType.getOccupationId()+" So occupation not added ");
								if(SessionUtil.getAutoLoanCbsCallId()==null && (occType!=null && occType.getOccupationId()==1)) 
								{
									continue;
								}
								else
									maps.put(occType.getOccupationId(), occType.getOccupation());
							}
							beanList.setOccupationTypes(maps);
						}
					}else{
						beanList.setOccupationTypes(maps);
					}
					
					
					if(quote.getLoanQuoteOccupationTypeId()!=null && quote.getLoanQuoteOccupationCategory()!=null)
					{
						java.util.HashMap<Integer,Integer> hm = new java.util.HashMap<Integer,Integer>();
						hm.put(quote.getLoanQuoteOccupationCategory(),quote.getLoanQuoteOccupationTypeId());
						beanList.setOccupationCategories(hm);
					}
					
					
					if(quote.getLoanQuoteCarVariantId()!=null && quote.getLoanQuoteCarVariantId()>0)
					{
						java.util.HashMap<String,String> hm = new java.util.HashMap<String,String>();
						hm.put("Yes","Yes");
						hm.put("No","No");
						beanList.setCarType(hm);
						
					}else{
						beanList.setCarType(null);
					}
					
					if(quote.getLoanQuoteCarMakeId()!=null){
						List<MasterCarModel> carModels = autoLoanService.getCarModelByCompany(quote.getLoanQuoteCarMakeId());
						maps = null; 
						if(carModels!=null && carModels.size() > 0){
							
							maps = new LinkedHashMap<Integer, String>();
							/*for (MasterCarModel carModel : carModels) {
								maps.put(carModel.getModelCode(), carModel.getModelName());	//changed for car variant data
							}*/
							
							maps.put(carModels.get(0).getModelCode(), "Small Size vehicle");
							maps.put(carModels.get(0).getModelCode(), "Multi Utility Vehicle(MUV)");
							maps.put(carModels.get(0).getModelCode(), "Sports Utility Vehicle(SUV)");
							maps.put(carModels.get(0).getModelCode(), "Others");
							
							beanList.setCarModel(maps);
						}
					}else{
						beanList.setCarModel(maps);	
					}
					maps = null; 
					if(quote.getLoanQuoteCarModelId()!=null && quote.getLoanQuoteCarModelId()>0){
						List<MasterCarVariant> carVariants = autoLoanService.getCarVariantByCarModelId(quote.getLoanQuoteCarModelId(), quote.getLoanQuoteCarMakeId());
						maps = null; 
						if(carVariants!=null){
							maps = new LinkedHashMap<Integer, String>();
							for (MasterCarVariant carVariant : carVariants) {
								maps.put(carVariant.getVariantCode(), carVariant.getVariantName());	//changed for car variant data
							}
							beanList.setCarVariant(maps);
						}
					}else{
						beanList.setCarVariant(maps);
					}
					
					LinkedHashMap <Integer,String> years=null;
					years = null; 
					years = new LinkedHashMap<Integer, String>();
					int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index =0; index<=10;currentYear--  ) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}
					years.put(currentYear, String.valueOf("Before "+currentYear));
					beanList.setYears(years);
					
					
					years = null; 
					years = new LinkedHashMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index =0; index<=10;currentYear--  ) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}
					years.put(currentYear, String.valueOf("Before "+currentYear));
					beanList.setYeartenorExistingAutoLoan(years);

					if(quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1){
						
						if(quote.getLoanQuoteStateId()!=null && appForm!=null && appForm.getAppStateId()!=null){
							Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.AUTO_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
							Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.AUTO_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
							if(mapDistrict!=null && !mapDistrict.isEmpty()){

								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1(mapCity);
							if(pageNo==3){
								if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
									beanList.setDistricts(mapDistrict);
								}
							}
							
							if(quote.getLoanQuoteCityId()!=null){
								if(quote.getLoanQuoteCityId().equals(Constants.OTHER_ID_INTEGER)){
									if(quote.getLoanQuoteDistrictId()!=null){
						    			beanList.setDistricts(mapDistrict);
						    			if(quote.getLoanQuoteBranchId()!=null){
						    				beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.AUTO_LOAN_ID, quote.getLoanQuoteStateId(), null, quote.getLoanQuoteDistrictId(), null, null, null, null, null));
						    			}
									}
								}else{
									if(quote.getLoanQuoteBranchId()!=null){
										beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.AUTO_LOAN_ID, null, quote.getLoanQuoteCityId(), null, null, null, null, null, null));
									}
								}
							}
						}
					}
					
					if(quote.getLoanQuoteCoapplicantFirstId()!=null && quote.getLoanQuoteCoapplicantFirstId()>0){
						employmentTypes = commonService.getAllEmploymentTypeByLoanTypeActive(Constants.AUTO_LOAN_ID);
						maps = null; 
						maps = new LinkedHashMap<Integer, String>();
						for (MasterEmploymentType employmentType : employmentTypes ) {
							maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
						}
						beanList.setEmployementTypesCoapplicants(maps);
					}

					List<MasterCoApplicant> coapplicants= null;
					if(quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
						coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,1);
					}else{
						coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,0);
					}			
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					for(MasterCoApplicant coapplicant:coapplicants ){
						maps.put(coapplicant.getCoapplicantid(),coapplicant.getCoapplicantrelation());
					}
					beanList.setRelationships(maps);

					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					maps.put(1, "Resident Indian");
					if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH
							 || Constants.BANK_ID == Constants.BANK_ID_SBT){						
						if(quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteLoanCategoryId()!=8){
							maps.put(2, "NRI");
						}
						if(quote!=null){
							if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null &&  quote.getLoanQuoteResidentTypeId()==1){
								maps.put(2, "NRI");
							}
						}
						
					}
					beanList.setResidentTypesCoApplicant(maps);
					
					

				}
				

				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				List <MasterIndustryType> industryTypes= commonService.getIndustryTypeByLoanId(Constants.AUTO_LOAN_ID);
				for (MasterIndustryType industryType : industryTypes) {
					maps.put(industryType.getIndustryTypeId(), industryType.getIndustryName());
				}
				maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				beanList.setIndustryTypeData(maps);
				
				List<MasterCorpSalaryPackage> salaryPackages=commonService.getAllSalaryPackageByLoanType(Constants.AUTO_LOAN_ID);
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
			
				List<MasterCoApplicant> coapplicants= null;
				if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
					coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,1);
				}else{
					coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,0);
				}			
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for(MasterCoApplicant coapplicant:coapplicants ){
					maps.put(coapplicant.getCoapplicantid(),coapplicant.getCoapplicantrelation());
				}
				beanList.setRelationships(maps);
				
				employmentTypes = commonService.getAllEmploymentTypeByLoanTypeActive(Constants.AUTO_LOAN_ID);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (MasterEmploymentType employmentType : employmentTypes ) {
					maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
				}
				beanList.setEmployementTypesCoapplicants(maps);


				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				maps.put(1, "Resident Indian");
				if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH
						 || Constants.BANK_ID == Constants.BANK_ID_SBT){					
					if(quote!=null && quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteLoanCategoryId()!=8){
						maps.put(2, "NRI");
					}
					if(quote!=null && quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteResidentTypeId()==1){
						maps.put(2, "NRI");
					}
				}
				beanList.setResidentTypesCoApplicant(maps);
				
				if(Constants.BANK_ID == Constants.BANK_ID_SBT){
					LinkedHashMap <Integer,String> yearsL = new LinkedHashMap <Integer,String>();
					int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index =0; index<65;currentYear--  ) {
						index++;
						yearsL.put(currentYear, String.valueOf(currentYear));
					}
					beanList.setBusinessYears(yearsL);
					
					if(quote!=null){
						if(quote.getLoanQuoteEmploymentTypeId()!=null && (quote.getLoanQuoteEmploymentTypeId()>=9 && quote.getLoanQuoteEmploymentTypeId()<13 )){
							maps = null; 
							maps = new LinkedHashMap<Integer, String>();
							int currentMonths = DateUtil.getCurrentMonth()-1;
							currentYear = Integer.parseInt(DateUtil.getCurrentYear());
							if(quote.getLoanQuoteYearCompanyJoining()!=null){
								if(currentYear != quote.getLoanQuoteYearCompanyJoining()){
									currentMonths=12;
								}
							}
							if(quote.getLoanQuoteMonthCompanyJoining()!=null){
								if(currentYear != quote.getLoanQuoteMonthCompanyJoining()){
									currentMonths=12;
								}
							}
							for (int index = 0; index < currentMonths; index++  ) {
								maps.put(index+1, Constants.month[index]);
							}
							beanList.setBusinessMonths(maps);
						}
					
					}
					
				}
				
			}
			employmentTypes = commonService.getAllEmploymentTypeByLoanTypeActive(Constants.AUTO_LOAN_ID);
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for (MasterEmploymentType employmentType : employmentTypes ) {
				maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
			}
			beanList.setEmployementTypesCoapplicants(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			maps.put(1, "Resident Indian");
			if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH
					 || Constants.BANK_ID == Constants.BANK_ID_SBT){					
				if(quote!=null && quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteLoanCategoryId()!=8){
					maps.put(2, "NRI");
				}
				if(quote!=null && quote.getLoanQuoteLoanCategoryId()!=null && quote.getLoanQuoteResidentTypeId()==1){
					maps.put(2, "NRI");
				}
			}
			beanList.setResidentTypesCoApplicant(maps);
			
			List<MasterCoApplicant> coapplicants= null;
			if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
				coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,1);
			}else{
				coapplicants= commonService.getAllCoApplicantByLoanId(Constants.AUTO_LOAN_ID,0);
			}			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for(MasterCoApplicant coapplicant:coapplicants ){
				maps.put(coapplicant.getCoapplicantid(),coapplicant.getCoapplicantrelation());
			}
			beanList.setRelationships(maps);
			
			//customer consent ETB
			//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentText();
			Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentId();
			String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
			beanList.setConsentAutoLoanEtb(consentTextEtb);
			
			//customer consent NTB
			//String consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentText();
			Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentId();
			String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
			beanList.setConsentAutoLoanNtb(consentTextNtb);
			
		} catch (NullPointerException e) {
			logger.info("AutoLoanAction.java :: 2912 ::generateUIBeanList(Integer moduleId, int pageNo) ", e);
		} catch (SQLException e) {
			logger.info("AutoLoanAction.java :: 2912 ::generateUIBeanList(Integer moduleId, int pageNo) ", e);
		}
	}
	
	public StreamResult getDealerByCompanyId() throws SQLException {
		JSONObject json = new JSONObject();
		Integer appseqId=SessionUtil.getAutoLoanApplicationSequenceId();
		appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appseqId);
		quote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
		if(quote!=null){
			json=SbiUtil.getDealerByCompanyId( query,quote.getLoanQuoteCarMakeId());
		}else{
			json=SbiUtil.getDealerByCompanyId( query,0);
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}

	public ApplicationFormAutoLoan getAppForm() {
		return appForm;
	}

	public void setAppForm(ApplicationFormAutoLoan appForm) {
		this.appForm = appForm;
	}

	public ApplicationFormAutoLoanQuote getQuote() {
		return quote;
	}

	public void setQuote(ApplicationFormAutoLoanQuote quote) {
		this.quote = quote;
	}

	public Integer getCarCompanyId() {
		return carCompanyId;
	}
	public void setCarCompanyId(Integer carCompanyId) {
		this.carCompanyId = carCompanyId;
	}
	
	public Integer getCarModuleId() {
		return carModuleId;
	}
	public void setCarModuleId(Integer carModuleId) {
		this.carModuleId = carModuleId;
	}
	public Integer getCarVariantId() {
		return carVariantId;
	}
	public void setCarVariantId(Integer carVariantId) {
		this.carVariantId = carVariantId;
	}

	public Integer getTwoWheelerTypeId() {
		return twoWheelerTypeId;
	}
	public void setTwoWheelerTypeId(Integer twoWheelerTypeId) {
		this.twoWheelerTypeId = twoWheelerTypeId;
	}
	public Integer getBikeCompanyId() {
		return bikeCompanyId;
	}
	public void setBikeCompanyId(Integer bikeCompanyId) {
		this.bikeCompanyId = bikeCompanyId;
	}
	public Integer getBikeModelId() {
		return bikeModelId;
	}
	public void setBikeModelId(Integer bikeModelId) {
		this.bikeModelId = bikeModelId;
	}
	public Integer getBikeVariantId() {
		return bikeVariantId;
	}
	public void setBikeVariantId(Integer bikeVariantId) {
		this.bikeVariantId = bikeVariantId;
	}

	public String getJsonJSArray1AutoLoan() {
		return jsonJSArray1AutoLoan;
	}

	public void setJsonJSArray1AutoLoan(String jsonJSArray1AutoLoan) {
		this.jsonJSArray1AutoLoan = jsonJSArray1AutoLoan;
	}
	
	public static String getJsonJSArray3AutoLoan() {
		return jsonJSArray3AutoLoan;
	}

	public static void setJsonJSArray3AutoLoan(String jsonJSArray3AutoLoan) {
		AutoLoanAction.jsonJSArray3AutoLoan = jsonJSArray3AutoLoan;
	}

	public JSONArray getInitLoanJSONArrayAutoLoan() {
		return initLoanJSONArrayAutoLoan;
	}

	public void setInitLoanJSONArrayAutoLoan(JSONArray initLoanJSONArrayAutoLoan) {
		this.initLoanJSONArrayAutoLoan = initLoanJSONArrayAutoLoan;
	}	
	

	public StreamResult occupationByEmployementTypeID() {
		JSONObject json = new JSONObject();
		try {
			HttpServletRequest req=RequestUtil.getServletRequest();
			logger.info("SessionUtil.getAutoLoanCbsCallId() ==== "+SessionUtil.getAutoLoanCbsCallId());
			if(req.getParameter("employementId")!=null)
			json.put("cardata", SbiUtil.getOccupationByEmployementTypeID(Integer.parseInt(req.getParameter("employementId"))));
		} catch (JSONException e) {
			logger.info("AutoLoanAction.java LNo :: 3023 :: carModelByCompanyId() ", e);
		} 
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}	


}
