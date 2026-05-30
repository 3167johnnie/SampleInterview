package com.mintstreet.loan.educationloan.action;

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
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterBank;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterInstitute;
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
import com.mintstreet.loan.educationloan.bo.impl.EducationProcessManagerImpl;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.educationloan.util.EducationLoanHelper;
import com.mintstreet.loan.product.entity.MasterElProduct;



public class EducationLoanAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(EducationLoanAction.class.getName());
	private static final long serialVersionUID = 1L;
	@Autowired
	private EducationLoanService educationLoanService;
	
	@Autowired
	private EducationLoanHelper educationLoanHelper;
	
	@Autowired
	private EducationProcessManagerImpl processManagerEducationImpl;
	
	@Autowired
	private AESEncryption aesEncryption;
	
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	
	
	
	
	private ApplicationFormEducationLoan appForm;
	private ApplicationFormEducationLoanQuote quote;
	
	private Integer courseType;
	
	 private Integer instituteId;
	private Integer isInIndia;
	
	public JSONArray initLoanJSONArrayEducationLoan;
	public String jsonJSArray1EducationLoan;
	public static String jsonJSArray3EducationLoan;

	public String execute(){
		return SUCCESS;
	}
	
	public String educationLoanDSR(){
		try{
			uiType=Constants.UI_TYPE;
			if (SessionUtil.getEducationTypeId() != null) {
				appElTypeId = SessionUtil.getEducationTypeId();
			} else {
				appElTypeId = Constants.APP_EL_TYPE_ID_NORMAL;
				SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_NORMAL);
			}
			loanTypeId=Constants.EDUCATION_LOAN_DSR_ID;
			ajaxPostUrl=Constants.EDUCATION_LOAN_ACTION_DSR;
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
			if (bankLmsUser != null) {
				appBankLmsUserId = bankLmsUser.getLmsUserId();
				SessionUtil.setBankLMSUser(bankLmsUser);
				contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
//				logger.info("EducationLoanAction.java LNo : 100 : isContactCenterLmsUser = "+contactCenterLmsUser);
//				logger.info("EducationLoanAction.java LNo : 105 : "+SessionUtil.getSession());
				if ( !ValidatorUtil.isValid(app_id) && !ValidatorUtil.isValid(lead_id) ) {
					if(request.getParameter("generatePDF")!=null){

					}else if(request.getParameter("requestIndex")==null){
						if(SessionUtil.getApplicationType()==null){

						}else{

							releaseSession(Constants.EDUCATION_LOAN_ID);
						}
						applicationTypeId=2;
						SessionUtil.setApplicationType(2);
						appSeqId=null;
						SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
						appLeadId=null;
						SessionUtil.setLeadId(appLeadId);
						firstName=null;
						mobileNo=null;
						alternateMobileNo = null; 
						email=null;
						SessionUtil.setEmail(email);
						SessionUtil.setMobile(mobileNo);
						SessionUtil.setalternateMobileNumber(alternateMobileNo);
						SessionUtil.setApplicantName(firstName);
					}
				}else if (ValidatorUtil.isValid(app_id)) {
					appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
					appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
					quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID)){
						appElTypeId = Constants.APP_EL_TYPE_ID_BIDYALAKHMI;
						SessionUtil.setEducationTypeId(appElTypeId);
					}else if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID)){
						appElTypeId = Constants.APP_EL_TYPE_ID_TAKE_OVER;
						SessionUtil.setEducationTypeId(appElTypeId);
					}else if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId()==1) {
						appElTypeId = Constants.APP_EL_TYPE_ID_SCHOLAR;
						SessionUtil.setEducationTypeId(appElTypeId);
					}else if(quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId()==2){
						appElTypeId = Constants.APP_EL_TYPE_ID_EDVANTAGE;
						SessionUtil.setEducationTypeId(appElTypeId);
					}
					SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
					visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdEL(visitId);
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
						SessionUtil.setVisitIdEL(visitId);
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
						SessionUtil.setalternateMobileNumber(alternateMobileNo);
						firstName=lead.getLeadFirstName()!=null?lead.getLeadFirstName():"";
						SessionUtil.setApplicantName(firstName);
						if(lead.getLeadAppSeqId()!=null){
							appSeqId=lead.getLeadAppSeqId();
							
						}else{
							appSeqId=null;
						}
						SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
						
						logger.info("leadConsentId for get a call back lead: " + lead.getLeadConsentId() + "  :: lead id::" + appLeadId);
						leadConsentId = lead.getLeadConsentId()!=null ? lead.getLeadConsentId() : 0;
						
					}
					if(request.getParameter("requestIndex")==null){
						SessionUtil.setApplicationType(1);
						applicationTypeId=1;
					}
				} else if (SessionUtil.getEducationLoanApplicationSequenceId() != null) {
					if(request.getParameter("requestIndex")==null){
						releaseSession(Constants.EDUCATION_LOAN_ID);
					}
					appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
					bankLmsUser = SessionUtil.getBankLMSUser();
				}
				if(ValidatorUtil.isValid(applicationTypeId)){
					SessionUtil.setApplicationType(applicationTypeId);
				}
				if(request.getParameter("requestIndex")==null){
					requestIndex=9;
				}
			}else{
				if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
					//return "home"+(uiType==null?"":uiType);		//commented for CSR 2018 - Session management
					
					// Added for CSR 2018 - Session management
					if(appSeqId == null){
						String lmsURL = Constants.BANK_ONLINE_URL;
						sessionId = null;						
						responseMessage = "error|"+"Sorry for the inconvenience, your session has been timed out. Please click <a href='" + lmsURL + "'> here </a> to start again";	
						logger.info("responseMessage 242..." + responseMessage);      
				        
						return "jsonResponsePage";
					}
					
				}else{
					bankLmsUser = SessionUtil.getBankLMSUser();
					if (bankLmsUser != null) {

						appBankLmsUserId = bankLmsUser.getLmsUserId();
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
						//logger.info("EducationLoanAction.java LNo : 189 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if(SessionUtil.getLeadId()!=null){
							appLeadId = SessionUtil.getLeadId();
							lead = commonService.getLeadById(appLeadId);
							appSeqId=lead.getLeadAppSeqId();
						} else if(appSeqId==null){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						}
					} else {
						if(appBankLmsUserId==null){
							appBankLmsUserId=4826;
						}
						bankLmsUser = commonService.getBankLmsUserById(appBankLmsUserId);
						SessionUtil.setBankLMSUser(bankLmsUser);
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
						//logger.info("EducationLoanAction.java LNo : 202 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if ( !ValidatorUtil.isValid(appSeqId) && !ValidatorUtil.isValid(appLeadId) ) {
							applicationTypeId=2;
							SessionUtil.setApplicationType(2);
						}else if (ValidatorUtil.isValid(appSeqId)) {
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID)){
								appElTypeId = Constants.APP_EL_TYPE_ID_BIDYALAKHMI;
								SessionUtil.setEducationTypeId(appElTypeId);
							}else if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID)){
								appElTypeId = Constants.APP_EL_TYPE_ID_TAKE_OVER;
								SessionUtil.setEducationTypeId(appElTypeId);
							}else if (quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId()==1) {
								appElTypeId = Constants.APP_EL_TYPE_ID_SCHOLAR;
								SessionUtil.setEducationTypeId(appElTypeId);
							}else if(quote.getLoanQuoteCountryOfStudyId() != null && quote.getLoanQuoteCountryOfStudyId()==2){
								appElTypeId = Constants.APP_EL_TYPE_ID_EDVANTAGE;
								SessionUtil.setEducationTypeId(appElTypeId);
							}
							SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
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
							if(lead.getLeadApplyingFrom()==2){
								mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo() : "";
								SessionUtil.setISDCode(lead.getLeadIsdCode()!=null?lead.getLeadIsdCode():"");
							}else{
								mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo().toString() : "";
								SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
							}
							SessionUtil.setMobile(mobileNo);
							visitId=lead.getLeadVisitId();
							SessionUtil.setVisitIdEL(visitId);
							firstName=lead.getLeadFirstName()!=null?lead.getLeadFirstName():"";
							SessionUtil.setApplicantName(firstName);
							if(lead.getLeadAppSeqId()!=null){
								appSeqId=lead.getLeadAppSeqId();
							}else{
								appSeqId=null;
							}
							SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
							if(request.getParameter("requestIndex")==null){
								SessionUtil.setApplicationType(1);
								applicationTypeId=1;
							}
						} else if (SessionUtil.getEducationLoanApplicationSequenceId() != null) {
						}
						
						if(ValidatorUtil.isValid(applicationTypeId)){
							SessionUtil.setApplicationType(applicationTypeId);
						}
						if(request.getParameter("requestIndex")==null){
							requestIndex=-1;
						}
						if (appSeqId!=null) {
							visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
							SessionUtil.setVisitIdEL(visitId);
						}
					}
				}
			}
			
//			logger.info("EducationLoanAction.java LNo : 243 : "+SessionUtil.getSession());
			return getEducationLoan(Constants.EDUCATION_LOAN_DSR_ID);
		} catch(NoResultException | SQLException e){
			logger.info("EducationLoanAction.java LN 347 exception ::", e);
			return "home"+(uiType==null?"":uiType);
		} 
	}

	public String educationScholarLoan() {
		try {
			isDsrPage = "false";
			loanTypeId=Constants.EDUCATION_LOAN_ID;
			appElTypeId = Constants.APP_EL_TYPE_ID_SCHOLAR;
			SessionUtil.setEducationTypeId(appElTypeId);
			ajaxPostUrl = Constants.SCHOLAR_LOAN_ACTION;
			SessionUtil.setApplicationType(0);
			request = RequestUtil.getServletRequest();
			if (SessionUtil.getBankLMSUser() != null) {
				isOnlineAndDsrActive = true;
				releaseSession(Constants.EDUCATION_LOAN_ID);
                 Integer     appSeqIdHomeLoan =null;
				SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);

				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);

				SessionUtil.setAutoLoanApplicationSequenceId(null);

				SessionUtil.setEducationLoanApplicationSequenceId(null);

				SessionUtil.setPersonalLoanApplicationSequenceId(null);

			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				if(appSeqId!=null){
					SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
					visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdEL(visitId);
				}
			}
			//logger.info("EducationLoanAction.java LNo : 288 : "+SessionUtil.getSession());
			return getEducationLoan(Constants.EDUCATION_LOAN_ID);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 261 educationLoan() ::", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}

	public String educationGlobalEdvantage() {
		try {
			isDsrPage = "false";
			loanTypeId=Constants.EDUCATION_LOAN_ID;
			appElTypeId = Constants.APP_EL_TYPE_ID_EDVANTAGE;
			SessionUtil.setEducationTypeId(appElTypeId);
			ajaxPostUrl = Constants.GLOBAL_EDVANTAGE_ACTION;
			SessionUtil.setApplicationType(0);
			request = RequestUtil.getServletRequest();
			if (SessionUtil.getBankLMSUser() != null) {
				isOnlineAndDsrActive = true;
				releaseSession(Constants.EDUCATION_LOAN_ID);
				Integer appSeqIdHomeLoan=null;
				SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);
				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
				SessionUtil.setAutoLoanApplicationSequenceId(null);
				SessionUtil.setEducationLoanApplicationSequenceId(null);
				SessionUtil.setPersonalLoanApplicationSequenceId(null);
			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				if(appSeqId!=null){
					SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
					visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdEL(visitId);
				}
			}
			//logger.info("EducationLoanAction.java LNo : 321 : "+SessionUtil.getSession());
			return getEducationLoan(Constants.EDUCATION_LOAN_ID);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 261 educationLoan() ::", e);
		} 
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String educationTakeOver() {
		try {
			isDsrPage = "false";
			appElTypeId = Constants.APP_EL_TYPE_ID_TAKE_OVER;
			SessionUtil.setEducationTypeId(appElTypeId);
			ajaxPostUrl = Constants.EDUCATION_LOAN_TAKEOVER_ACTION;
			SessionUtil.setApplicationType(0);
			request = RequestUtil.getServletRequest();
			if (SessionUtil.getBankLMSUser() != null) {
				isOnlineAndDsrActive = true;
				releaseSession(Constants.EDUCATION_LOAN_ID);
				Integer appSeqIdHomeLoan=null;
				SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);

				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);

				SessionUtil.setAutoLoanApplicationSequenceId(null);

				SessionUtil.setEducationLoanApplicationSequenceId(null);

				SessionUtil.setPersonalLoanApplicationSequenceId(null);

			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				if(appSeqId!=null){
					SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
					visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdEL(visitId);
				}
			}
		//	logger.info("Education TakeOver LoanAction.java LNo : 109 : "+SessionUtil.getSession());
			return getEducationLoan(Constants.EDUCATION_LOAN_ID);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 261 educationLoan() ::", e);
		} 
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String educationLoan(){
		try{
			isDsrPage="false";
			ajaxPostUrl=Constants.EDUCATION_LOAN_ACTION;
			SessionUtil.setApplicationType(0);
			request=RequestUtil.getServletRequest();
			if(SessionUtil.getBankLMSUser()!=null){
				isOnlineAndDsrActive=true;
				releaseSession(Constants.EDUCATION_LOAN_ID);
			     Integer	appSeqIdHomeLoan=null;
				SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);
				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
				SessionUtil.setAutoLoanApplicationSequenceId(null);

				SessionUtil.setPersonalLoanApplicationSequenceId(null);
			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				
				if(appSeqId!=null){
					SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
					visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdEL(visitId);
				}
			}
			return getEducationLoan(Constants.EDUCATION_LOAN_ID);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 261 educationLoan() ::", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String getEducationLoan(int moduleId) {
		if(ValidatorUtil.isValid(uiType)){SessionUtil.setUiType(uiType);}else{SessionUtil.setUiType(null);}

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
			loanTypeId=Constants.EDUCATION_LOAN_ID;

			educationLoanPage = 1;
			stateManagerBean=stateManager.getState(request, moduleId);
			
			
			SbiUtil.getOcasSessionId(request);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			if(SessionUtil.getVisitIdEL()!=null){
				visitId = SessionUtil.getVisitIdEL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId ==null ){
					if(SessionUtil.getEducationLoanApplicationSequenceId()==null || moduleId==Constants.EDUCATION_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.EDUCATION_LOAN_ID );
						if (!(campaignCode == null && offerCode == null && trackingCode == null)) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.EDUCATION_LOAN_ID, 0);
						}
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdEL(visitId);
						}else{
							logger.info("EducationLoanAction.java LN 334 unable to insert into visit entity.");
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}
			if(stateManagerBean.getState()==-1){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					metaInfo.setTitle(Constants.EDUCATION_LOAN_TITLE);
					metaInfo.setKeywords(Constants.EDUCATION_LOAN_KEYWORDS);
					metaInfo.setDescription(Constants.EDUCATION_LOAN_DESCRIPTION);
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
				logger.info("state manager 33  & 34 is called");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerEducationImpl.verifyConcentOtp(moduleId, stateManagerBean.getState(), 
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
				logger.info("state manager 33  & 34 is called");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerEducationImpl.verifySMSOTP(moduleId, stateManagerBean.getState(), 
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
				logger.info("state manager 28 and 29 is called");
				if(stateManagerBean.getValidatorResponse().isStatus()){
					OtherRequest otherRequest = stateManagerBean.getOtherRequest();
					json = processManagerEducationImpl.processCBSOTP(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, otherRequest);
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
						CBSCallResponse cbsCallResponse = processManagerEducationImpl.processCbsCall(appSeqId, requestIndex, cbs, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl);
						cbs = commonService.getMasterCBSCallObjById(SessionUtil.getEducationLoanCbsCallId());
						if(cbsCallResponse.getStatus()!=null){
							if(cbsCallResponse.getStatus()==0){
								responseMessage = "error|"+cbsCallResponse.getResponseMsg();
								return "jsonResponsePage";
							}else if(cbsCallResponse.getStatus()==1){

								return "cbsOtpPage"+(uiType==null?"":uiType);
							}else if(cbsCallResponse.getStatus()==2){
								return "cbsVerifyButtonPage"+(uiType==null?"":uiType);
							}
						}
						
					} catch (SQLException e) {
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
						releaseSession(Constants.EDUCATION_LOAN_ID);
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
					logger.info("EducationLoanAction.java LN 360 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} 
			}


			if(stateManagerBean.getState()==25){
				try {
					/*if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						//String currentOcasId = SbiUtil.getOcasSessionId(request);
						releaseSession(Constants.EDUCATION_LOAN_ID);
						String appReferenceId = request.getParameter("generatePDF").substring(0, 12);
						if(ValidatorUtil.isValid(appReferenceId)) {
							appForm = educationLoanService.getApplicationFormEducationLoanByAppReferenceId(appReferenceId);
							appSeqId = appForm.getAppSeqId();
							if(appForm!=null){
								if(ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())){
									
									if (ValidatorUtil.isValid(appForm.getOcasID())) {
										String filePath = Constants.PDF_GENRATION_BASE_PATH + Constants.EL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
										inputStream = new FileInputStream(new File(filePath));
										SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
										SessionUtil.setEducationScholarLoanSequenceId(appSeqId);
										SessionUtil.setEducationGlobalEdvantageSequenceId(appSeqId);
										SessionUtil.setEducationTakeOverLoanApplicationSequenceId(appSeqId);
										SessionUtil.setEducationBidyaLakshmiLoanSequenceId(appSeqId);
										return "downLoadPDF";
									} else {
										logger.info("EducationLoanAction.java Ocas ID is not same as valid Ocas ID");
										SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
										SessionUtil.setEducationScholarLoanSequenceId(appSeqId);
										SessionUtil.setEducationGlobalEdvantageSequenceId(appSeqId);
										SessionUtil.setEducationTakeOverLoanApplicationSequenceId(appSeqId);
										SessionUtil.setEducationBidyaLakshmiLoanSequenceId(appSeqId);
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
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if (ValidatorUtil.isValid(appSeqId)) {
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									String filePath = Constants.PDF_GENRATION_BASE_PATH
											+ Constants.EL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
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
					logger.info("EducationLoanAction.java LN 349 stateManagerBean.getState()==25 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} 
			}


			if(stateManagerBean.getState()==23){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isDsrPage.equalsIgnoreCase("true")){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
							
							getCallLogs(appSeqId, appLeadId);
						}
						return "callsLogDetails"+(uiType==null?"":uiType);
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
					
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 376 stateManagerBean.getState()==23 ::", e);
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
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						logger.info("EdcuationLoanAction.java LNo:321 :: appSeqId :: "+appSeqId+" imageNo :: "+imageNo+" ajaxPostUrl :"+ajaxPostUrl + "imageName :: "+imageName );
						json = processManagerEducationImpl.processDeleteProductImage(appSeqId, imageNo, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, imageName);
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
				} catch (JSONException e) {
					logger.info("EducationLoanAction.java LN 418 stateManagerBean.getState()==22 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}

			if(stateManagerBean.getState()==21){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppEL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
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
						json = processManagerEducationImpl.processToDocumentPickupUploaded(appSeqId, appForm, request, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("EducationLoanAction.java LN 376 stateManagerBean.getState()==21 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==20){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){

						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						appForm=educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|N/A.pdf";
							return "jsonResponsePage";
						}
						appElTypeId = SessionUtil.getEducationTypeId();
						SecureRandom rand = new SecureRandom();
						/*if(appForm.getAppDownloadPdfFileName() != null && appElTypeId.intValue() ==  Constants.APP_EL_TYPE_ID_BIDYALAKHMI.intValue()){
							responseMessage = "success|" + Constants.PORT + Constants.CONTEXT + ((moduleId == Constants.EDUCATION_LOAN_ID.intValue()) ? Constants.EDUCATION_LOAN_BIDYALAKHMI_ACTION	: Constants.EDUCATION_LOAN_ACTION_DSR) + "?generatePDF=" + appForm.getAppReferenceId() + rand.nextInt(1000);
						}else*/ if (appForm.getAppDownloadPdfFileName() != null && appElTypeId.intValue() ==  Constants.APP_EL_TYPE_ID_SCHOLAR.intValue()) {
							responseMessage = "success|" + Constants.PORT + Constants.CONTEXT + ((moduleId == Constants.EDUCATION_LOAN_ID.intValue()) ? Constants.SCHOLAR_LOAN_ACTION	: Constants.EDUCATION_LOAN_ACTION_DSR) + "?generatePDF=" + appForm.getAppReferenceId() + rand.nextInt(1000);
						}else if(appForm.getAppDownloadPdfFileName() != null && appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_EDVANTAGE.intValue()){
							responseMessage = "success|" + Constants.PORT + Constants.CONTEXT + ((moduleId == Constants.EDUCATION_LOAN_ID.intValue()) ? Constants.GLOBAL_EDVANTAGE_ACTION	: Constants.EDUCATION_LOAN_ACTION_DSR) + "?generatePDF=" + appForm.getAppReferenceId() + rand.nextInt(1000);
						}else if(appForm.getAppDownloadPdfFileName() != null && appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_TAKE_OVER.intValue()){
							responseMessage = "success|" + Constants.PORT + Constants.CONTEXT + ((moduleId == Constants.EDUCATION_LOAN_ID.intValue()) ? Constants.EDUCATION_LOAN_TAKEOVER_ACTION	: Constants.EDUCATION_LOAN_ACTION_DSR) + "?generatePDF=" + appForm.getAppReferenceId() + rand.nextInt(1000);
						}
						else if(ajaxPostUrl.equals(Constants.EDUCATION_LOAN_ACTION_DSR)){
							responseMessage = "success|" + Constants.PORT + Constants.CONTEXT + Constants.EDUCATION_LOAN_ACTION_DSR + "?generatePDF=" + appForm.getAppReferenceId() + rand.nextInt(1000);
						}
						else{
							responseMessage = "error|Please wait pdf generation in progress ...";
						}

						/*this.json = this.processManagerEducationImpl.processToDocumentPickupUploaded(this.appSeqId, this.appForm, this.request, (this.bankLmsUser != null) ? this.bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, this.ajaxPostUrl);
						if (this.json.get("status").toString().equalsIgnoreCase("success")) {
							this.responseMessage = "success|" + this.json.getString("message");
							return "jsonResponsePage";
						}
						this.responseMessage = "error|" + this.json.getString("message");*/
						
						return "jsonResponsePage";
					}
					
		          String msg = CommonUtilites.serchingValuesFromMaps(this.stateManagerBean.getValidatorResponse().getErrorMessage());
		          this.responseMessage = "error|" + msg;
		          return "jsonResponsePage";
		          
		          
				} catch (IllegalArgumentException e) {
					logger.info("EducationLoanAction.java LN 481 stateManagerBean.getState()==20 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==18){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppEL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
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
						json = processManagerEducationImpl.processToDocumentPickupUploaded(appSeqId, appForm, request, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("EducationLoanAction.java LN 546 stateManagerBean.getState()==18 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
				
			}
			

			if(stateManagerBean.getState()==17){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppEL();
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
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						appForm = processManagerEducationImpl.processSubmitQuote(appSeqId, stateManagerBean.getState(), appForm, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appForm.getError()!=null){
							responseMessage = "error|"+appForm.getError();
							return "jsonResponsePage";
						}
						educationLoanPage = 4;
						generateUIBeanListApp(educationLoanPage);



						logger.info("appForm.getAppEducationLoanId() --- 1017 --"+appForm.getAppEducationLoanId());
						//documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.EDUCATION_LOAN_ID, appForm.getAppEducationLoanId()==7 || appForm.getAppEducationLoanId()==8  || appForm.getAppEducationLoanId()==9?1:appForm.getAppEducationLoanId() );
						documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.EDUCATION_LOAN_ID,appForm.getAppEducationLoanId());


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
					logger.info("EducationLoanAction.java LN 590 stateManagerBean.getState()==17 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
				
			}
			
			logger.info("stateManagerBean.getState()::::" + stateManagerBean.getState());
			if(stateManagerBean.getState()==14 || stateManagerBean.getState()==15 || stateManagerBean.getState()==16){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						//added code by hakeem on 1 
						//OtherRequest otherRequest =  stateManagerBean.getOtherRequest();
						OtherRequest otherRequest =null;
						if( stateManagerBean.getState()==14 ||stateManagerBean.getState()==15 || stateManagerBean.getState()==16){
							otherRequest = stateManagerBean.getOtherRequest();
						}
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(stateManagerBean.getState()==16){
							if(appSeqId!=null){
								if(!Constants.EL_CAPTCHA_BY_PASS && otherRequest.getAlternateMobileNumber() == null){
									responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
									if (responseMessage!=null) {
										return "jsonResponsePage";
									}
								}
							}
						}
						
						json = processManagerEducationImpl.processMobileOTP(appSeqId,stateManagerBean.getState(), 
								null, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId,
								otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							SessionUtil.setCaptch(null);
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}
						if(otherRequest.getAlternateMobileNumber() == null && stateManagerBean.getState() != 15) {
							logger.info("EducationLoanAction.java LNo : 1108 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
							responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							logger.info("EducationLoanAction.java LNo : 1110 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 650 stateManagerBean.getState()==14, 15, 16 ::"+stateManagerBean.getState()+" :: ", e);
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
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
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
						appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if((appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK) ) 
								&&  appForm.getAppMobileVerified().equalsIgnoreCase("N") ){
							responseMessage = "error|Mobile OTP is not verified";
							return "jsonResponsePage";
						}else{
							quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							quote.setLoanQuoteLoanProductId(Integer.parseInt(applyUrlData[0]));
							quote.setLoanQuoteLoanTenure(chosenTenure);
							quote.setLoanQuoteLoanAmount(chosenEligibility);
							quote.setLoanQuoteAppliedCoupon(appForm.getAppLoanAppliedCoupon());
							quote.setLoanQuoteLoanAccountType(Integer.parseInt(applyUrlData[1]));
							loanScenarioBean = educationLoanHelper.callBRE(appForm, quote, bankLmsUser, quote.getLoanQuoteId(), quote.getLoanQuoteNewVisitId(), ajaxPostUrl, false);
							logger.info("inside action after call BRE");
							if(loanScenarioBean.getStatus()!=1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								return "jsonResponsePage";
							}
							appForm = loanScenarioBean.getApplicationEL();
							logger.info("inside action Line 1082");
							appForm.setAppEducationLoanId(Integer.parseInt(applyUrlData[0]));
							appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
							appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
							appForm.setAppLoanProcessingFee((double) Double.parseDouble(applyUrlData[3]));
							appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
							appForm.setAppMoratoriumPeriod(Integer.parseInt(applyUrlData[5]));
							appForm.setAppEmiNmiRatio(Float.parseFloat(applyUrlData[6]));
							logger.info("inside action Line 1090");
							appForm.setAppLoanEmiDiscount(applyUrlData[7].toString().equalsIgnoreCase("-")?null:Double.parseDouble(applyUrlData[7]));
							appForm.setAppLoanProcessingFeeDiscount(applyUrlData[8].toString().equalsIgnoreCase("-")?null:(double) Double.parseDouble(applyUrlData[8]));
							appForm.setAppLoanInterestRateDiscount(applyUrlData[9].toString().equalsIgnoreCase("-")?null:Float.parseFloat(applyUrlData[9]));
							
							MasterElProduct product=commonService.getEducationLoanProductById(appForm.getAppEducationLoanId());
							appForm.setAppProductTenureFlag(product.getELProductSliderTenure());

							if(SessionUtil.getApplicationType()!=null){
								StatusRequest statusRequest = new StatusRequest();
								statusRequest.setCurrentStatus(appForm.getAppLoanStatusId());
								statusRequest.setHaveLoanOffer(true);
								statusRequest.setLoanTypeId(Constants.EDUCATION_LOAN_ID);
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
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
									return "jsonResponsePage";
								}
								if(statusManagerResponse.isEligibleToInsertLog()){
									educationLoanHelper.insertCallLog(appForm.getAppSeqId(),(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);
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
								
								if (isTeleCallerUser && appForm.getAppPanCardNo() != null) {
									isPanNoMask = "true";
									panNoMaskVal = appForm.getAppPanCardNo().replaceAll("^.{6}", "******");
								}
						
								if (isTeleCallerUser && appForm.getAppAlternateMobileNumber() != null) {
									isalternateMobileNoMask = "true";
									alternateMobileNoMaskVal = appForm.getAppAlternateMobileNumber().replaceAll("\\d(?=\\d{4})", "*");
								}
								
							}
							appForm = educationLoanService.save(appForm);
							educationLoanPage = 3;
							quote= educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
							generateUIBeanListApp(educationLoanPage);
							jsonJSArray3EducationLoan = SbiUtil.populateJSValidation(17, moduleId).toString();
							toolTipJSArray = SbiUtil.populateTootTip( Constants.EDUCATION_LOAN_ID, 17).toString();
							
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
										logger.info("OTP for Mobile Number: " + (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim() + " is " + appForm.getAppMobileVerificationCode());
									}
									
									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
										responseMessage = "error|Mobile OTP service is down";
										return "jsonResponsePage";
									}
								}
							}
							logger.info("inside action end:: before return");
							return "thirdPage"+(uiType==null?"":uiType);
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 349 stateManagerBean.getState()==13 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
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
			
			if(stateManagerBean.getState()==11){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						json = processManagerEducationImpl.processAddCoapplicant(moduleId, quote, ajaxPostUrl);
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
					logger.info("EducationLoanAction.java LN 778 stateManagerBean.getState()==11 ::", e);
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
					logger.info("EducationLoanAction.java LN 797 stateManagerBean.getState()==10 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}

			}
			

			if(stateManagerBean.getState()==9){
				try{
					stateManagerBean.setOriginalState(stateManagerBean.getState());
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(moduleId==Constants.EDUCATION_LOAN_ID){
							releaseSession(Constants.EDUCATION_LOAN_ID);
							appSeqId=null;
							lead=null;
						}
						if(request.getParameter("ELQuoteToken")!=null){

							String encyQuotId=aesEncryption.decrypt(request.getParameter("ELQuoteToken").toString());
							if(ValidatorUtil.isValid(encyQuotId)){
								appSeqId = Integer.parseInt(encyQuotId);
								appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
								quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
								if (quote.getLoanQuoteCountryOfStudyId()!=null){
									if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID)){
										SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_BIDYALAKHMI);
									}else if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID)){
										SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_TAKE_OVER);
									}else if (quote.getLoanQuoteCountryOfStudyId()!=null && quote.getLoanQuoteCountryOfStudyId()==2){

										SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_EDVANTAGE);
									}else {

										SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_SCHOLAR);
									}
								}
								SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
								visitId = educationLoanService.getVisitByAppSeqId(appSeqId);
								SessionUtil.setVisitIdEL(visitId);
								stateManagerBean.setState(2);
							}else{
								stateManagerBean.setState(-1);
							}
							
						}else{
							logger.info("EducationLoanAction.java LNo : 766 : State Id 9: outside landing state start if LMS caling appId : "+app_id + "--" + "tokenId : "+token_id + "--" +"leadId : "+lead_id);
							request = RequestUtil.getServletRequest();
							sessionId = SbiUtil.getSessionId(request, sessionId);
							bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
							SessionUtil.setBankLMSUser(bankLmsUser);
							Cookie [] cookies= request.getCookies();
							bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
							if(bankLmsUser!=null){
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
									SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
									
									if(SessionUtil.getEducationLoanApplicationSequenceId()==null){
										SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
									}
									
									stateManagerBean.setState(2);
								}else{
									stateManagerBean.setState(-1);
								}
								
							}else{
								releaseSession(Constants.EDUCATION_LOAN_ID);
								
								return "home"+(uiType==null?"":uiType);
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 888 stateManagerBean.getState()==9 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}

			}
			

			if(stateManagerBean.getState()==8){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){

							responseMessage = "error|Sorry for the inconvenience. Your session has been timed out, Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
							return "jsonResponsePage";
						}else{
							appForm=educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							if(appForm == null){

								responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
								return "jsonResponsePage";
							}else{
								float appLoanInterestRate=0f;
								double processingFee=0;
								double emi=0;
								int moratoriumMonths=0;
								int moratoriumType=2;
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
									processingFee =(double) Math.round(new Double((request.getParameter("pf"))));
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
								int moratoriumT = 0;
								if(request.getParameter("moratoriumType")!=null){
									moratoriumType = Integer.parseInt(request.getParameter("moratoriumType"));
									moratoriumT =moratoriumType;
								}
								if(moratoriumType<0){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								appForm.setAppLoanInterestRate(appLoanInterestRate);
								appForm.setAppLoanProcessingFee(processingFee);
								appForm.setAppLoanEmi(emi);
								MasterElProduct product=commonService.getEducationLoanProductById(appForm.getAppEducationLoanId());
								appForm.setAppProductTenureFlag(product.getELProductSliderTenure());
								appForm.setAppLoanAccountType(moratoriumT);
								
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
								repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==1?(appForm.getAppLoanTenure()+moratoriumMonths):(appForm.getAppLoanTenure()*12+moratoriumMonths)), irD, emiD,1,1,moratoriumMonths,moratoriumType,product.getELProductSliderAmtMul(),null,null,null);
							}
						}
						return "repaymentScheduled"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 970 stateManagerBean.getState()==8 ::", e);
					responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
					return "jsonResponsePage";
				}
			}
	
			if(stateManagerBean.getState()==6){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						if(appSeqId == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						appForm=educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
						if(appForm == null){
							json.put("status", "error");
							json.put("message", ""+Constants.SORRY_FOR_INCONVENIENCE);
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
						logger.info("EducationLoanAction.java LNo : 1693 app.getAppOTPAttemptCount() : "+appForm.getAppEmailAttemptCount()+" with AppSeqId "+appSeqId);
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
						appForm.setAppEducationLoanId(Integer.parseInt(applyUrlData[0]));
						appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
						appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
						appForm.setAppLoanProcessingFee(Double.parseDouble(applyUrlData[3]));
						appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
						appForm = educationLoanService.save(appForm);
						
						responseMessage = "success|Quotes sent to email";
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 1499 stateManagerBean.getState()==6 ::", e);
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
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						
						if(stateManagerBean.getState()==5 || stateManagerBean.getState()==3){
							responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
							if (responseMessage!=null) {
								return "jsonResponsePage";
							}
						}
						json = processManagerEducationImpl.processWantUsToCallYou(appSeqId, stateManagerBean.getState(), visitId, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId , otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							SessionUtil.setCaptch(null);
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
						}
						if(stateManagerBean.getState() != 4) {
							logger.info("EducationLoanAction.java LNo : 1679 before encryption value is : " + responseMessage + " for state " +stateManagerBean.getState());
							responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							logger.info("EducationLoanAction.java LNo : 1681 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
						}
							
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						jsonJSArray = SbiUtil.populateJSValidation(stateManagerBean.getState(), moduleId).toString();
						return "jsonResponsePage";
						
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 1102 stateManagerBean.getState()==3, 4, 5 ::"+stateManagerBean.getState()+" :: ", e);
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
					if(SessionUtil.getEducationTakeOverLoanApplicationSequenceId()!=null){
						appSeqId = SessionUtil.getEducationTakeOverLoanApplicationSequenceId();
					}
					if(SessionUtil.getEducationLoanApplicationSequenceId()!=null){
						appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
					}
					if(appSeqId==null){
						if(stateManagerBean.getOriginalState()==9){
							stateManagerBean.setState(-1);
						}else{
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
						if(appForm == null){
							if(stateManagerBean.getOriginalState()==9){
								stateManagerBean.setState(-1);
							}else{
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						if(request.getParameter("ELQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
							if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
								stateManagerBean.setState(-1);
							}else{
								initEducationLoan();
							}
						}
						if(stateManagerBean.getState()==2){
							if(appForm.getAppApplyingFrom()==2){
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(appForm.getAppISDCode());
								}
								//start code for alternate mobile number by hakeem 
								if (appForm.getAppAlternateMobileNumber() != null) {
									SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
									
									
									appForm.setAppAltISDCode(appForm.getAppAltISDCode());
									
								//end code 
								}
							
								
								
								
							}else{
								if(appForm.getAppMobileNo()!=null){
									SessionUtil.setMobile(appForm.getAppMobileNo());
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
								}
								// start code for alternate mobile by hakeem 29-sep-22
								if (appForm.getAppAlternateMobileNumber() != null) {
									SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
									
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									//quote.setAppAlternateMobileNumber(appForm.getAppAlternateMobileNumber());
								}
								
							
							}
							SessionUtil.setEmail(appForm.getAppWorkEmail());
							firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
							SessionUtil.setApplicantName(firstName);
							quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								if(request.getParameter("ELQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
									stateManagerBean.setState(-1);
								}else{
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
									return "jsonResponsePage";
								}
							}else{
								quote.setAppEmail(appForm.getAppWorkEmail());
								if(appForm.getAppApplyingFrom()==2){
									quote.setAppNRIMobileNo(appForm.getAppMobileNo());
									quote.setAppISDCode(appForm.getAppISDCode());
								}else{
									quote.setAppMobile(appForm.getAppMobileNo());
									quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
								}
								Integer productSliderTenure = null;
								if(otherRequest!=null && otherRequest.getProductSliderTenure()!=null){
									productSliderTenure =Integer.parseInt(otherRequest.getProductSliderTenure().toString());
								}
								if(otherRequest!=null && otherRequest.getChosenTenure()!=null){
									chosenTenure =Integer.parseInt(otherRequest.getChosenTenure().toString());
								
									if(productSliderTenure!= null && productSliderTenure.intValue()==2){
										chosenTenure = chosenTenure*Constants.LOAN_TENURE_MULTIPLER_FACTOR;
									}
									
									quote.setLoanQuoteLoanTenure(chosenTenure);
								}
								if(otherRequest!=null && otherRequest.getChosenEligibility()!=null){
									BigDecimal amount = new BigDecimal(otherRequest.getChosenEligibility().toString());
									BigDecimal multiplier = new BigDecimal("100000");
									chosenEligibility=amount.multiply(multiplier).doubleValue();
									quote.setLoanQuoteLoanAmount(chosenEligibility);
								}
								if(otherRequest!=null && otherRequest.getSbiLifeInsuranceCheck()!=null){
									quote.setLoanQuoteSbiLifeInsuranceCheck(otherRequest.getSbiLifeInsuranceCheck().toString());
								}
								if(otherRequest!=null && otherRequest.getMoratoriumCheck()!=null){
									quote.setLoanQuoteMoratoriumCheck(otherRequest.getMoratoriumCheck().toString());
								}
								if(otherRequest!=null && otherRequest.getDiscountCouponName()!=null){
									quote.setLoanQuoteAppliedCoupon(otherRequest.getDiscountCouponName());
					    		}
								if(otherRequest!=null && otherRequest.getChosenLoanAccountType()!=null && !otherRequest.getChosenLoanAccountType().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanAccountType(Integer.parseInt(otherRequest.getChosenLoanAccountType()));
					    		}else{
					    			quote.setLoanQuoteLoanAccountType(1);
					    		}
								if(otherRequest!=null && otherRequest.getChosenProductId()!=null && !otherRequest.getChosenProductId().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanProductId(Integer.parseInt(otherRequest.getChosenProductId()));
					    		}else{
					    			quote.setLoanQuoteLoanProductId(1);
					    		}

								loanScenarioBean=(LoanScenarioBean) processManagerEducationImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

								if(loanScenarioBean.getStatus() != 1){
									responseMessage = loanScenarioBean.getMessage();
									if(request.getParameter("ELQuoteToken")!=null){
										stateManagerBean.setState(-1);
									}else{
										if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
											responseMessage =  "error|"+loanScenarioBean.getMessage();
											/*if(loanScenarioBean.getApplicationEL()!=null){
												responseMessage += "|" + loanScenarioBean.getApplicationEL().getAppRequestData() + "|" + loanScenarioBean.getApplicationEL().getAppOfferJsonData();
											}*/
											return "jsonResponsePage";	
										}else{
											stateManagerBean.setState(0);
										}
									}
								}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(),  loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(),  loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
									if(appSeqId==null){
										appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
									}
									appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);

									quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
									educationLoanPage=2;
									populateForm(educationLoanPage, appSeqId);
									toolTipJSArray = SbiUtil.populateTootTip( Constants.EDUCATION_LOAN_ID, 2).toString();
									if(request.getParameter("ELQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
										if(appForm.getAppReferenceId()!=null){
											appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
											logger.info("EducationLoanAction.java LNo : 1015 : appSeqId =" + appForm.getAppSeqId() + "from LMS state 2 EL outside landing checking refernece :: appReferencetIdEncrypted="+appReferencetIdEncrypted);
											return "applicationTrack"+(uiType==null?"":uiType);
										}
										jsonJSArray1EducationLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
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
					logger.info("EducationLoanAction.java LN 1243 stateManagerBean.getState()==2 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(0);
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
						quote = stateManagerBean.getQuoteEL();
						if(isDsrPage.equalsIgnoreCase("false") && !quote.getInfoprovide().equals("on")){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						}
						if(!Constants.EL_CAPTCHA_BY_PASS && (ajaxPostUrl.equalsIgnoreCase("scholar-loan")||ajaxPostUrl.equalsIgnoreCase("bidyalakhmi-loan")||ajaxPostUrl.equalsIgnoreCase("edvantage-loan") ||ajaxPostUrl.equalsIgnoreCase("education-takeover-loan"))){
							if(appSeqId==null){
								responseMessage = SbiUtil.checkCaptcha(quote.getCaptcha());
								if (responseMessage!=null) {
									return "jsonResponsePage";
								}
							}
						}
						
						
						if(SessionUtil.getEducationLoanApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							if(quote!=null && appForm!=null && appForm.getAppSubTypeId()!=null && appForm.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)){
								if(appForm.getAppQuoteId() != null){
									ApplicationFormEducationLoanQuote quoteOld = null;
									quoteOld = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());

									if(quoteOld!=null && quoteOld.getLoanQuoteOutstandingLoanAmount()!=null){
										quote.setLoanQuoteOutstandingLoanAmount(quoteOld.getLoanQuoteOutstandingLoanAmount());
									}



								}
							} 
						}
						
						//quote.setLoanQuoteFirstName(loanQuoteFirstName);

						if(quote!=null && quote.getLoanQuoteDateOfBirth()!=null){
							quote.setLoanQuoteDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
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
						    	responseMessage = "error|Alternate Number should not start with 0";
						    	return "jsonResponsePage";
						    } else  if ((alternateMob.length() < 8  || alternateMob.length() > 16) &&  alternateMob.length() != 0 ) {
						    	responseMessage = "error|Provide valid alternate mobile number";
						    	return "jsonResponsePage";
						    } 
							
						}
						loanScenarioBean=(LoanScenarioBean) processManagerEducationImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

						if(loanScenarioBean.getStatus() != 1){
							responseMessage = "error|"+loanScenarioBean.getMessage();
							/*if(loanScenarioBean.getApplicationEL()!=null){
								responseMessage += "|" + loanScenarioBean.getApplicationEL().getAppRequestData() + "|" + loanScenarioBean.getApplicationEL().getAppOfferJsonData();
							}*/
							return "jsonResponsePage";
						}else{
							Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(), loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
							loanScenarioBean.setManualEligVal(manualEligVal);
							Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(),  loanScenarioBean.getProductSliderTenure()==1?true:false);
							loanScenarioBean.setManualTenureVal(manualTenureVal);
							if(appSeqId==null){
								appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
							}
							if(appSeqId == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
							educationLoanPage=2;
							populateForm(educationLoanPage, appSeqId);
							toolTipJSArray = SbiUtil.populateTootTip( Constants.EDUCATION_LOAN_ID, 2).toString();
							SessionUtil.setCaptch(null);
							return "secondPage"+(uiType==null?"":uiType);
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}

				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 1243 stateManagerBean.getState()==1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
					return "jsonResponsePage";
				}
			}
			
			if (stateManagerBean.getState()==0) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){

						educationLoanPage=1;
						if(SessionUtil.getApplicationType()==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
							if(appSeqId != null){
								appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.EDUCATION_LOAN_ID){
										releaseSession(Constants.EDUCATION_LOAN_ID);
										appSeqId=null;
										lead=null;
									}
								}
							}
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
						}
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						if(appSeqId!=null){
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
						}
						if(appForm.getAppFirstName()!=null){
							firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
							SessionUtil.setApplicantName(firstName);
						}
						if(appForm.getAppMobileNo()!=null){
							mobileNo=appForm.getAppMobileNo() != null ?appForm.getAppMobileNo() : "";
							SessionUtil.setMobile(mobileNo);
						}
						educationLoanPage = 1;
						if(isDsrPage.equalsIgnoreCase("false")){
							populateFirstPageContent(Constants.EDUCATION_LOAN_ID,1);
						}
						educationLoanPage = 1;					
						populateFirstPageContent(Constants.EDUCATION_LOAN_ID, 1);
						populateForm(educationLoanPage, appSeqId);
						jsonJSArray1EducationLoan = SbiUtil.populateJSValidation(1,moduleId).toString();
						toolTipJSArray = SbiUtil.populateTootTip(Constants.EDUCATION_LOAN_ID, 1).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();
						initEducationLoan();
						includeJs=true;
						return "firstPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					stateManagerBean.setState(-1);
					logger.info("EducationLoanAction.java LN 1862 stateManagerBean.getState()==0 ::",e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if (stateManagerBean.getState()==-1) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){

						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
							if(isOnlineAndDsrActive){
								appSeqId=null;
								responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
							}
							if(appSeqId != null){
								appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
								if(request.getParameter("ELQuoteToken")!=null){
									String encyQuotId=aesEncryption.decrypt(request.getParameter("ELQuoteToken").toString());
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
													logger.info("EducationLoanAction.java LNo : 1158 : appSeqId =" + appForm.getAppSeqId() + "from LMS state 2 EL outside landing checking refernece :: appReferencetIdEncrypted="+appReferencetIdEncrypted);
													responseMessage = Constants.SAVE_QUOTE_REDIRECTION.replaceAll("CLICK_HERE","<a href='"+Constants.PORT+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"?appReferencetIdEncrypted="+appReferencetIdEncrypted+(uiType==null?"":"&uiType="+Constants.UI_TYPE)+"'>here</a>");
												}
											}
										}
									}
								}
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.EDUCATION_LOAN_ID){
										releaseSession(Constants.EDUCATION_LOAN_ID);
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
						
						if(appSeqId==null){
							appSeqId = SessionUtil.getEducationLoanApplicationSequenceId();
								}
						if(isOnlineAndDsrActive){
							appSeqId=null;
							responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
						}
						
						if(appSeqId !=null){
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())) {
									
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
												appSeqId = null;
												appForm = null;
												quote = null;
										}
											}else{
										if ("N".equalsIgnoreCase(appForm.getAppMobileVerified())&& "N".equalsIgnoreCase(appForm.getAppEmailVerified())) {
											releaseSession(Constants.EDUCATION_LOAN_ID);
											appSeqId = null;
											appForm = null;
											quote = null;
										}
									}
								}
							}
						}
						
						if(appSeqId !=null){
							appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (appForm.getAppSubTypeId() == Constants.APP_APP_SUB_TYPE_ID_CBS) {
								
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
											releaseSession(Constants.EDUCATION_LOAN_ID);
										}
									}else{
										if ("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())) {
											releaseSession(Constants.EDUCATION_LOAN_ID);
										}
									}
								}
								if(appForm.getAppApplyingFrom()==2){
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(appForm.getAppISDCode());
									}
									
									//Added for alt mob no
									if(appForm.getAppAlternateMobileNumber()!=null){
										alternateMobileNo = appForm.getAppAlternateMobileNumber();
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
									}
									
								}else{
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									}
									//start code for alternate mobile number
									if(appForm.getAppAlternateMobileNumber()!=null){
										alternateMobileNo = appForm.getAppAlternateMobileNumber();
										SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
								
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
								SessionUtil.setEducationLoanApplicationSequenceId(appSeqId);
							}
						}
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						educationLoanPage = 1;
						populateForm(educationLoanPage, appSeqId);
						populateFirstPageContent(Constants.EDUCATION_LOAN_ID,1);
						jsonJSArray1EducationLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						toolTipJSArray = SbiUtil.populateTootTip( Constants.EDUCATION_LOAN_ID, 1).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();

						initEducationLoan();
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
							getCallLogs(appSeqId, appLeadId);
						}
						if(SessionUtil.getApplicantName()!=null){
							firstName=SessionUtil.getApplicantName();
						}

						if(SessionUtil.getMobile()!=null){
							mobileNo=SessionUtil.getMobile();
						}
						//strt code for alternate mobile number by hakeem 
						if(SessionUtil.getalternateMobileNumber()!=null){
							alternateMobileNo=SessionUtil.getalternateMobileNumber();
						}
						//end
						if(SessionUtil.getEmail()!=null){
							email=SessionUtil.getEmail();
						}
						getDisplayUpdate((appSeqId!=null), (appForm!=null && appForm.getAppSubTypeId()!=null)?appForm.getAppSubTypeId():0, applicationTypeId );
						if(quote!=null){
							if(isDsrPage.equalsIgnoreCase("true")){
								if (quote.getLoanQuoteCountryOfStudyId()!=null){
								if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_BIDYA_LAKSHMI_LOAN_PURPOSE_ID)){
									SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_BIDYALAKHMI);
								}else if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.EDUCATION_TAKEOVER_LOAN_PURPOSE_ID)){
									SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_TAKE_OVER);
								}else if (quote.getLoanQuoteCountryOfStudyId()!=null && quote.getLoanQuoteCountryOfStudyId()==2){

									SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_EDVANTAGE);
								}else {

									SessionUtil.setEducationTypeId(Constants.APP_EL_TYPE_ID_SCHOLAR);
								}
							}
						   }
						}
						return "homePage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("EducationLoanAction.java LN 1446 stateManagerBean.getState()==-1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		} catch(JSONException e){
			logger.info("EducationLoanAction.java LN 1364 exception ::" ,e);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 1365 exception ::" ,e);
		}catch(FileNotFoundException e){
			logger.info("EducationLoanAction.java LN 1366 exception ::" ,e);
		}catch(UnsupportedEncodingException e){
			logger.info("EducationLoanAction.java LN 1367 exception ::" ,e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
					
	private void populateForm(Integer pageNo, Integer appSeqId){
		try {
			if(appSeqId!=null){
				if(appForm==null){
					appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
				}
				if(appForm !=null ){
					quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote!=null){
						quote.setAppEmail(appForm.getAppWorkEmail());
						if(appForm.getAppApplyingFrom()==2){
							quote.setAppNRIMobileNo(appForm.getAppMobileNo());
							quote.setAppISDCode(appForm.getAppISDCode());
						}else{
							quote.setAppMobile(appForm.getAppMobileNo());
							quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
						}
						generateUIBeanList(pageNo);	
					}else{
						this.appSeqId=null;
						SessionUtil.setEducationLoanApplicationSequenceId(null);
					}
				}
			}else{
				generateUIBeanList(pageNo);
			}
		} catch (NullPointerException e) {
			logger.info("EducationLoanAction.java LN 1378 populateForm() ::", e);
		} 
		
	}
	
	public void  initEducationLoan() {

			initLoanJSONArrayEducationLoan = new JSONArray();
			try {
				
				
				JSONObject json3 = new JSONObject();
				json3.put("residentCountries", SbiUtil.getAllCountries());
				initLoanJSONArrayEducationLoan.put(json3);
				
				JSONObject json4 = new JSONObject();
				json4.put("loanPurposeLinks", SbiUtil.getAllLoanPurposeLinks());
				initLoanJSONArrayEducationLoan.put(json4);
				
				JSONObject json5 = new JSONObject();
				json5.put("courseTypeIndian", SbiUtil.getCourseType(0));
				initLoanJSONArrayEducationLoan.put(json5);
				
				JSONObject json6 = new JSONObject();
				json6.put("courseTypeAbroad", SbiUtil.getCourseType(2));
				initLoanJSONArrayEducationLoan.put(json6);
				if (appElTypeId != Constants.APP_EL_TYPE_ID_EDVANTAGE) {
					JSONObject json7 = new JSONObject();
					json7.put("instituteScholarList",SbiUtil.getScholarInistituteNameList());
					initLoanJSONArrayEducationLoan.put(json7);
				}
				
				JSONObject json8 = new JSONObject();
				json8.put("loanWithBankId",SbiUtil.getAllBank(Constants.EDUCATION_LOAN_ID, appElTypeId));
				initLoanJSONArrayEducationLoan.put(json8);
				
			} catch (JSONException e) {
				logger.info("EducationLoanAction.java LN 1416 initEducationLoan() ::", e);
				initLoanJSONArrayEducationLoan.put("error");
			}

	}

	private void getCallLogs(Integer appSeqId, Integer appLeadId){
		try{
			if(ValidatorUtil.isValid(appSeqId) || ValidatorUtil.isValid(appLeadId)){
				callLogDetails = commonService.getCallLogByLeadAppId(Constants.EDUCATION_LOAN_ID, appSeqId, appLeadId  );
			}
		} catch(SQLException e){
			logger.info("EducationLoanAction.java :: getCallLogs(appSeqId) ", e);
		} 
	}
	
	private void generateUIBeanListApp(int pageNo){
		try{
			if(pageNo==3){
				List<MasterEmploymentType> employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.EDUCATION_LOAN_ID);
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				for (MasterEmploymentType employmentType : employmentTypes ) {
					maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
				}
				beanList.setEmployementTypesCoapplicants(maps);
				
				if(appForm!=null && appForm.getAppCoapplicantStateId()!=null){
					Map<Integer, String> mapCity1 =new LinkedHashMap<Integer, String>();
					mapCity1 = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppCoapplicantStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict1 =new LinkedHashMap<Integer, String>();
					mapDistrict1 = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppCoapplicantStateId(), null, null, null, null, null, null, null);
					if(mapDistrict1!=null && !mapDistrict1.isEmpty()){
						mapCity1.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Coapplicant1(mapCity1);
					
					if(appForm.getAppCoapplicantCityId()!=null && appForm.getAppCoapplicantCityId().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppCoapplicantDistrictId()!=null){
			    			beanList.setDistrictsCoapplicant1(mapDistrict1);
						}
					}
				}
				
				if(appForm!=null && appForm.getAppPermanentStateId()!=null){
					Map<Integer, String> mapCity1 =new LinkedHashMap<Integer, String>();
					mapCity1 = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict1 =new LinkedHashMap<Integer, String>();
					mapDistrict1 = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
					if(mapDistrict1!=null && !mapDistrict1.isEmpty()){
						mapCity1.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Permanent(mapCity1);
					
					if(appForm.getAppCoapplicantCityId()!=null && appForm.getAppCoapplicantCityId().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppCoapplicantDistrictId()!=null){
			    			beanList.setDistrictsPermanent(mapDistrict1);
						}
					}
				}
				if(Constants.BANK_ID==Constants.BANK_ID_SBI){
					int isEdvantage = 0;
					if(quote!=null && quote.getLoanQuoteCountryOfStudyId()!=null && quote.getLoanQuoteCountryOfStudyId()==2){
						if(appForm!=null && appForm.getAppLoanAmount()!=null && appForm.getAppLoanAmount()>=20.0){
							isEdvantage =1;
						}else{
							
						}
					}

					if(isEdvantage == 1){
						beanList.setEdvantageState(commonService.getStateCityDistrictBranch(1, Constants.EDUCATION_LOAN_ID, null, null, null, null, null, null, 1, null));
					}else{
						beanList.setEdvantageState(commonService.getStateCityDistrictBranch(1, Constants.EDUCATION_LOAN_ID, null, null, null, null, null, null, null, null));
					}
					if(appForm!=null && appForm.getAppEdvantageStateId()!=null){
						Map<Integer, String> mapCity1 =new LinkedHashMap<Integer, String>();
						mapCity1 = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppEdvantageStateId(), null, null, null, null, null, 1, null);
						
						Map<Integer, String> mapDistrict1=new LinkedHashMap<Integer, String>();
						mapDistrict1 = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppEdvantageStateId(), null, null, null, null, null, 1, null);
						if(mapDistrict1!=null && !mapDistrict1.isEmpty()){
							mapCity1.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							beanList.setEdvantageCity(mapCity1);
						}
						beanList.setEdvantageDistrict(mapDistrict1);
						if(appForm.getAppEdvantageCityId()!= null && appForm.getAppEdvantageCityId().equals(Constants.OTHER_ID_INTEGER) && appForm.getAppEdvantageCityId() > 0){
							beanList.setEdvantegeBranch(commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, null, null, appForm.getAppEdvantageDistrictId(), null, null, null, 1, null));
						}else{
							beanList.setEdvantegeBranch(commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, null, appForm.getAppEdvantageCityId(), null, null, null, null, 1, null));
						}
						
					}
				}
				
				if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().intValue()==24){
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					List<MasterCoApplicant>coapplicants=commonService.getAllCoApplicantByLoanId(Constants.EDUCATION_LOAN_ID, 0);
					maps = new LinkedHashMap<Integer, String>();
					for (MasterCoApplicant coapplicant : coapplicants ) {
						maps.put(coapplicant.getCoapplicantid(), coapplicant.getCoapplicantrelation());
					}
					beanList.setRelationships(maps);
				}
			}else if(pageNo==4){
				if(appForm!=null && appForm.getAppPickupStateId()!=null && appForm.getAppPickupStateId() > 0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Pickup(mapCity);
					if(appForm!=null && appForm.getAppPickupCityId()!=null && appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)){
						beanList.setDistrictPickup(mapDistrict);
					}
				}
				
				if(appForm!=null && appForm.getAppOfficeStateId()!=null && appForm.getAppOfficeStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
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
			
			if(pageNo==3 || pageNo==4){
				Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
				if(appForm!=null && appForm.getAppStateId()!=null && appForm.getAppStateId() > 0){
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1(mapCity);
					
					if(appForm!=null && appForm.getAppCityId()!=null && appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER)){
						if(appForm.getAppDistrictId()!=null && appForm.getAppDistrictId()>0){
							beanList.setDistricts(mapDistrict);
						}
						if(appForm.getAppBranchId()!=null && appForm.getAppBranchId()>0){
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, appForm.getAppStateId(), null, appForm.getAppDistrictId(), null, null, null, null, null));
		    			}
					}else{
						if(appForm!=null && appForm.getAppBranchId()!=null && appForm.getAppBranchId()>0){
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, null, appForm.getAppCityId(), null, null, null, null, null, null));
		    			}
					 }
				}
			}
		} catch(NullPointerException e){
			logger.info("EducationLoanAction.java LN 1658 generateUIBeanList() ::", e);
		} catch(SQLException e){
			logger.info("EducationLoanAction.java LN 1658 generateUIBeanList() ::", e);
		}
	}
	
	private void generateUIBeanList(int pageNo){
		try {
			
			if(pageNo==1){
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				LinkedHashMap <Integer,String> years=null;
				List<MasterLoanPurpose> loanpurposes = commonService.getAllLoanPurposeByLoanType(Constants.EDUCATION_LOAN_ID);
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				for (MasterLoanPurpose purpose : loanpurposes) {
					maps.put(purpose.getLpId(), purpose.getLpTypeValue());
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
						loanPurposeUrl=purpose.getLpUrl();
					}
				}
				if(isDsrPage=="false"){
					if(appElTypeId == Constants.APP_EL_TYPE_ID_NORMAL){
						
					}else if(appElTypeId == Constants.APP_EL_TYPE_ID_SCHOLAR){
						maps.remove(24);
						maps.remove(26);
					}else if(appElTypeId == Constants.APP_EL_TYPE_ID_EDVANTAGE){
						maps.remove(24);
						maps.remove(26);
					}else if(appElTypeId == Constants.APP_EL_TYPE_ID_TAKE_OVER){
						maps.remove(10);
						maps.remove(26);
					}else if(appElTypeId == Constants.APP_EL_TYPE_ID_BIDYALAKHMI){
						maps.remove(10);
						maps.remove(24);
					}
				}
				beanList.setLoanPurposes(maps);
				if(appElTypeId == Constants.APP_EL_TYPE_ID_SCHOLAR || appElTypeId == Constants.APP_EL_TYPE_ID_BIDYALAKHMI){
					beanList.setCourseType(educationLoanService.getCourseType(1));
					beanList.setInstituteNameList(SbiUtil.getScholarInistituteList());
				}else{
					beanList.setCourseType(educationLoanService.getCourseType(2));
				}
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				List<MasterCoApplicant>coapplicants=commonService.getAllCoApplicantByLoanId(Constants.EDUCATION_LOAN_ID, 0);
				maps = new LinkedHashMap<Integer, String>();
				for (MasterCoApplicant coapplicant : coapplicants ) {
					maps.put(coapplicant.getCoapplicantid(), coapplicant.getCoapplicantrelation());
				}
				beanList.setRelationships(maps);
				Map<Integer, String> stateMap = commonService.getStateCityDistrictBranch(1, Constants.EDUCATION_LOAN_ID, null, null, null, null, null, null, null, null);
				beanList.setState(stateMap);
				if(quote!=null){
					if(quote.getLoanQuoteLoanOptedLoan()!=null && "Y".equalsIgnoreCase(quote.getLoanQuoteLoanOptedLoan()) &&
							quote.getLoanQuoteLoanWithInMoratoriumPeriod()!=null && "Y".equalsIgnoreCase(quote.getLoanQuoteLoanWithInMoratoriumPeriod())){
						years = null; 
						years = new LinkedHashMap<Integer, String>();
						int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						for (int index =0; index<4;currentYear++) {
							index++;
							years.put(currentYear, String.valueOf(currentYear));
						}
						beanList.setYears(years);
						
						years = null; 
						years = new LinkedHashMap<Integer, String>();
						currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						for (int index =0; index<4;currentYear++) {
							index++;
							years.put(currentYear, String.valueOf(currentYear));
						}
						beanList.setAppliendYears(years);
						
						
						years = null; 
						years = new LinkedHashMap<Integer, String>();
						currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						for (int index =0; index<15;currentYear++  ) {
							index++;
							years.put(currentYear, String.valueOf(currentYear));
						}
						years.put(currentYear, String.valueOf("After"+currentYear));
						beanList.setFutureYears(years);
					}
					
					
					maps = null; 
					maps = new LinkedHashMap<Integer, String>();
					maps.put(1, "Resident Indian");
					if(Constants.BANK_ID == Constants.BANK_ID_SBI){
						maps.put(2, "NRI/PIO Holder");
					}
					beanList.setResidentTypes(maps);
					
					Integer isScholar=0;
					if(Constants.BANK_ID == Constants.BANK_ID_SBI){
						if(quote.getLoanQuoteCountryOfStudyId()!=null && quote.getLoanQuoteCountryOfStudyId()==1 && quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==1 && 
								(quote.getLoanQuoteInstituteCat()!=null 
								&& quote.getLoanQuoteInstituteCat().equalsIgnoreCase("AA") 
								|| quote.getLoanQuoteInstituteCat().equalsIgnoreCase("A") 
								|| quote.getLoanQuoteInstituteCat().equalsIgnoreCase("B") 
								|| quote.getLoanQuoteInstituteCat().equalsIgnoreCase("C") )){
							isScholar=1;
						}
					} else if(Constants.BANK_ID == Constants.BANK_ID_SBBJ){
						isScholar=0;
					}
					
					if(ValidatorUtil.isValid(quote.getLoanQuoteResidentStateId())){
						Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
						Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
						if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_BIDYALAKHMI) && quote.getLoanQuoteResidentStateId() == 4){
							mapCity = commonService.getStateCityDistrictBranchForBidya(2, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null,1);
							mapDistrict = commonService.getStateCityDistrictBranchForBidya(3, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, null, null, null, null, null, null, 1);
							if(mapDistrict!=null && !mapDistrict.isEmpty()){
								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1Residence(mapCity);
						}else {
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, null, null, null, null, null, null);
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, null,null, null, null, null, null);
							if(mapDistrict!=null && !mapDistrict.isEmpty()){
								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1Residence(mapCity);
						}
						

						if(ValidatorUtil.isValid(quote.getLoanQuoteResidentCityId())){
							if(quote.getLoanQuoteResidentCityId().equals(Constants.OTHER_ID_INTEGER)){
								if(quote.getLoanQuoteDistrictId()!=null){
									beanList.setDistricts(mapDistrict);
								}
								if(quote.getLoanQuoteBranchId()!=null){
									Map<Integer, String> mapBranch=new LinkedHashMap<Integer, String>();
									if(quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==2){
										if(quote.getLoanQuoteInstituteName()!=null ){
											MasterInstitute masterInsti = commonService.getInstituteByInstituteName(quote.getLoanQuoteInstituteName());
											if(masterInsti !=null ){
												MasterBranch scholarBranch = commonService.getBranchById(masterInsti.getInstituteDefaultBranch());
												mapBranch.put(scholarBranch.getBranchId(), scholarBranch.getBranchName());
											}
										}
									}else{
										if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_BIDYALAKHMI) && quote.getLoanQuoteResidentStateId() == 4){
											mapBranch = commonService.getStateCityDistrictBranchForBidya(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, quote.getLoanQuoteDistrictId(), null, null, null, null, null, 1);
										}else{
											if(quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==1){

												mapBranch = commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, quote.getLoanQuoteDistrictId(), null, null, null, null, null);
											}
										}
									}
									if(appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_TAKE_OVER.intValue()){
										mapBranch = commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, quote.getLoanQuoteDistrictId(), null, null, null, null, null);
									}
									beanList.setBranches(mapBranch);
								}
							}else{
								if(quote.getLoanQuoteBranchId()!=null){
									Map<Integer, String> mapBranch=new LinkedHashMap<Integer, String>();
									if(isScholar!=null && isScholar>0){
										if(quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==2){
											if(quote.getLoanQuoteInstituteName()!=null ){
												MasterInstitute masterInsti = commonService.getInstituteByInstituteName(quote.getLoanQuoteInstituteName());
												if(masterInsti !=null ){
													MasterBranch scholarBranch = commonService.getBranchById(masterInsti.getInstituteDefaultBranch());
													mapBranch.put(scholarBranch.getBranchId(), scholarBranch.getBranchName());
												}
											}
										}else{
											mapBranch = commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, null, quote.getLoanQuoteResidentCityId(), null, null, isScholar, null, null, null);
										}
									}else if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_BIDYALAKHMI) && quote.getLoanQuoteResidentStateId() == 4 ){
										mapBranch = commonService.getStateCityDistrictBranchForBidya(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), quote.getLoanQuoteResidentCityId(), null, null, null, null, null, null, 1);
									}else{
										if(quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==1){

											mapBranch = commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, quote.getLoanQuoteDistrictId(), null, isScholar, null, null, null);
										}
									}
									if(appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_TAKE_OVER.intValue()){
										mapBranch = commonService.getStateCityDistrictBranch(4, Constants.EDUCATION_LOAN_ID, quote.getLoanQuoteResidentStateId(), null, quote.getLoanQuoteDistrictId(), null, null, null, null, null);
									}
									beanList.setBranches(mapBranch);
									if(quote.getLoanQuotePreferredLocation()!=null && quote.getLoanQuotePreferredLocation()==2){
										if(quote.getLoanQuoteInstituteName()!=null ){
											MasterInstitute masterInsti = commonService.getInstituteByInstituteName(quote.getLoanQuoteInstituteName());
											if(masterInsti !=null ){
												MasterBranch scholarBranch = commonService.getBranchById(masterInsti.getInstituteDefaultBranch());
												mapBranch.put(scholarBranch.getBranchId(), scholarBranch.getBranchName());
											}
										}
									}
									
								}
							}
						}
					}else{
						Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
						if(appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_BIDYALAKHMI.intValue()){
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null);
						}
						Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null);
						if(mapDistrict!=null && !mapDistrict.isEmpty()){
							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setCitiesoptgrp1Residence(mapCity);
					}
				}else{
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					if(appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_BIDYALAKHMI.intValue()){
						mapCity = commonService.getStateCityDistrictBranchForBidya(2, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null,1);
					}else{
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null);
					}
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					if(appElTypeId.intValue() == Constants.APP_EL_TYPE_ID_BIDYALAKHMI.intValue()){
						mapDistrict = commonService.getStateCityDistrictBranchForBidya(3, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null,1);
					}else{
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.EDUCATION_LOAN_ID, 4, null, null, null, null, null, null, null);
					}
					
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Residence(mapCity);
				}
				
				years = null; 
				years = new LinkedHashMap<Integer, String>();
				int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
				for (int index =0; index<=29;currentYear--  ){
					index++;
					years.put(currentYear, String.valueOf(currentYear));
				}
				years.put(currentYear, String.valueOf(currentYear));
				beanList.setYears(years);
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				int currentMonths = DateUtil.getCurrentMonth()-2;
				currentYear = Integer.parseInt(DateUtil.getCurrentYear());
				
				if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==24){
					if(quote.getLoanQuoteYearExistingEducationLoanStartDate()!=null && quote.getLoanQuoteYearExistingEducationLoanStartDate()==currentYear){
						if(currentMonths >0){
							for (int index = 0; index<=currentMonths+1; index++  ) {
								maps.put(index+1, Constants.month[index]);
							}
						}else{
							for (int index = currentMonths; index<Constants.month.length; index++  ) {
								maps.put(index+1, Constants.month[index]);
							}
						}
					}else{
						currentMonths=11;
						for (int index = 0; index <= currentMonths; index++  ) {
							maps.put(index+1, Constants.month[index]);
						}
					}
				}
				beanList.setMonthsTillCurrentMonth(maps);
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				maps.put(1, "Resident Indian");
				maps.put(2, "NRI/PIO Holder");
				beanList.setResidentTypes(maps);
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				List<MasterBank> masterBankList = commonService.getAllBankForELTakeOver();
				for (MasterBank bank : masterBankList) {
					maps.put(bank.getBankId(), bank.getBankName());
				}
				beanList.setEducationTakeOverGroupBank(maps);
				

				
				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				currentMonths = DateUtil.getCurrentMonth()-2;
				if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==24){
					if(quote.getLoanQuoteYearExistingEducationLoanEndDate()!=null && quote.getLoanQuoteYearExistingEducationLoanEndDate()==currentYear){
						if(currentMonths >0){
							for (int index = 0; index<=currentMonths+1; index++  ) {
								maps.put(index+1, Constants.month[index]);
							}
						}else{
							for (int index = currentMonths; index<Constants.month.length; index++  ) {
								maps.put(index+1, Constants.month[index]);
							}
						}
					}else{
						currentMonths=11;
						for (int index = 0; index <= currentMonths; index++  ) {
							maps.put(index+1, Constants.month[index]);
						}
					}
				}	
				beanList.setMonthsFromCurrentMonthTillDecember(maps);
				
			
				
				maps = null; 
				maps = new LinkedHashMap<Integer, String>();
				currentYear = Integer.parseInt(DateUtil.getCurrentYear());
				for (int index =0; index<=19;currentYear++  ) {
					index++;
					maps.put(currentYear, String.valueOf(currentYear));
				}
				beanList.setYearTenorExistingEducationTakeOverLoan(maps);
				
			}
			if(isDsrPage=="false" && loanPurposeUrl==null){
				MasterLoanPurpose loanPurpose = null;
				if(appElTypeId == Constants.APP_EL_TYPE_ID_NORMAL){
					loanPurpose = commonService.getLoanPurposeById(10);
				}else if(appElTypeId == Constants.APP_EL_TYPE_ID_SCHOLAR){
					loanPurpose = commonService.getLoanPurposeById(10);
				}else if(appElTypeId == Constants.APP_EL_TYPE_ID_EDVANTAGE){
					loanPurpose = commonService.getLoanPurposeById(10);
				}else if(appElTypeId == Constants.APP_EL_TYPE_ID_TAKE_OVER){
					loanPurpose = commonService.getLoanPurposeById(24);
				}else if(appElTypeId == Constants.APP_EL_TYPE_ID_BIDYALAKHMI){
					loanPurpose = commonService.getLoanPurposeById(26);
				}
				if(loanPurpose!=null && loanPurpose.getLpUrl()!=null){
					loanPurposeUrl=loanPurpose.getLpUrl();
				}
			}
			
			//customer consent ETB
			//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "ETB").getConsentText();
			Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "ETB").getConsentId();
			String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
			beanList.setConsentEducationLoanEtb(consentTextEtb);
			
			//customer consent NTB
			//String consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "NTB").getConsentText();
			Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.EDUCATION_LOAN_ID, "NTB").getConsentId();
			String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
			beanList.setConsentEducationLoanNtb(consentTextNtb);
			
		} catch (NullPointerException e) {
			logger.info("EducationLoanAction.java LN 1852 generateUIBeanList() ::", e);
		} catch (SQLException e) {
			logger.info("EducationLoanAction.java LN 1852 generateUIBeanList() ::", e);
		}
	}
	
	
	public StreamResult instituteByCourseType() throws JSONException {
		JSONObject json = new JSONObject();
		try {
			json.put("instituteData", SbiUtil.instituteByCourseType(courseType));
			if(courseType==1||courseType==2 ){
				json.put("graduationData", SbiUtil.gradutionByCourseType(courseType));
			}else if(courseType==4){
				json.put("graduationData", SbiUtil.certificateByCourseType(courseType));
			}
		} catch (JSONException e) {
			logger.info("EducationLoanAction.java LN 1867 instituteByCourseType() ::", e);
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult instituteCategoryType() throws NoResultException, JSONException  {
		JSONObject json = new JSONObject();
		json.put("instituteCat", SbiUtil.instituteCategoryType(instituteId));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult getAllCourseName() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllCourseName(query, courseType, isInIndia);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	public StreamResult getAllInstituteName() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllInstituteName(query, courseType, isInIndia, loanTypeId);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	public StreamResult getAllUniversity() {
		JSONObject json = new JSONObject();
		json=SbiUtil.universityByString(query, isInIndia);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}

	public ApplicationFormEducationLoan getAppForm() {
		return appForm;
	}

	public void setAppForm(ApplicationFormEducationLoan appForm) {
		this.appForm = appForm;
	}

	public ApplicationFormEducationLoanQuote getQuote() {
		return quote;
	}

	public void setQuote(ApplicationFormEducationLoanQuote quote) {
		this.quote = quote;
	}

	public Integer getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Integer instituteId) {
		this.instituteId = instituteId;
	}

	public Integer getIsInIndia() {
		return isInIndia;
	}

	public void setIsInIndia(Integer isInIndia) {
		this.isInIndia = isInIndia;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}


	public String getJsonJSArray1EducationLoan() {
		return jsonJSArray1EducationLoan;
	}

	public void setJsonJSArray1EducationLoan(String jsonJSArray1EducationLoan) {
		this.jsonJSArray1EducationLoan = jsonJSArray1EducationLoan;
	}

	public static String getJsonJSArray3EducationLoan() {
		return jsonJSArray3EducationLoan;
	}

	public static void setJsonJSArray3EducationLoan(String jsonJSArray3EducationLoan) {
		EducationLoanAction.jsonJSArray3EducationLoan = jsonJSArray3EducationLoan;
	}

	public JSONArray getInitLoanJSONArrayEducationLoan() {
		return initLoanJSONArrayEducationLoan;
	}

	public void setInitLoanJSONArrayEducationLoan(
			JSONArray initLoanJSONArrayEducationLoan) {
		this.initLoanJSONArrayEducationLoan = initLoanJSONArrayEducationLoan;
	}	
		
}
