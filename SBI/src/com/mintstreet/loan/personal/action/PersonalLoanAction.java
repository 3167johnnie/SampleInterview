package com.mintstreet.loan.personal.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

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
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterEmployeeOccupationType;
import com.mintstreet.common.entity.MasterEmployer;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterProfession;
import com.mintstreet.common.entity.MasterRelationshipWithBank;
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
import com.mintstreet.loan.personal.bo.impl.PersonalProcessManagerImpl;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.loan.personal.util.PersonalLoanHelper;
import com.mintstreet.loan.product.entity.MasterPlProduct;

public class PersonalLoanAction extends BaseAction {
	
	private static final Logger logger = LogManager.getLogger(PersonalLoanAction.class.getName());
	  
	  private static final long serialVersionUID = 1L;
	  
	  @Autowired
	  private PersonalLoanService personalLoanService;
	  
	  @Autowired
	  private PersonalLoanHelper personalLoanHelper;
	  
	  @Autowired
	  private PersonalProcessManagerImpl processManagerPersonalImpl;
	  
	  @Autowired
	  private AESEncryption aesEncryption;
	  
	  @Autowired
	  private CommunicationManagerImpl communicationManagerImpl;
	  
	  private ApplicationFormPersonalLoan appForm;
	  
	  private ApplicationFormPersonalLoanQuote quote;
	  
	  public JSONArray initLoanJSONArrayPersonalLoan;
	  
	  public String jsonJSArray1PersonalLoan;
	  
	  public static String jsonJSArray3PersonalLoan;
	  
	  private String instituteName;
	  
	  private String employerName;
	  
	  public String execute() {
	    return "success" + (uiType == null ? "" : uiType);
	  }
	  
	  
	  public String personalLoanDSR() {
			try {
				uiType = Constants.UI_TYPE;
				ajaxPostUrl = Constants.PERSONAL_LOAN_ACTION_DSR;

				if (SessionUtil.getPersonalTypeId() == null) {
					if (cbs != null && cbs.getCbsLoanPurpose() == Constants.PENSION_LOAN_PURPOSE_ID) {
						SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PENSION);
						appPLTypeId = Constants.APP_PL_TYPE_PENSION;
					} else {
						SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PERSONAL);
						appPLTypeId = Constants.APP_PL_TYPE_PERSONAL;
					}
				} else {
					appPLTypeId = SessionUtil.getPersonalTypeId();
				}

				isDsrPage = "true";
				request = RequestUtil.getServletRequest();
				sessionId = SbiUtil.getSessionId(request, sessionId);
				if (!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
					if (sessionId == null) {
						return "home" + (uiType == null ? "" : uiType);
					}
				}
				if (!ValidatorUtil.isValid(lead_id)) {
					lead_id = null;
				}
				if (!ValidatorUtil.isValid(app_id)) {
					app_id = null;
				}
				bankLmsUser = SbiUtil.checkLmsUserLoginCookie(sessionId);
				if (bankLmsUser != null) {
					appBankLmsUserId = bankLmsUser.getLmsUserId();
					SessionUtil.setBankLMSUser(bankLmsUser);
					contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
					//logger.info("PersonalLoanAction.java LNo : 107 : isContactCenterLmsUser = " + contactCenterLmsUser);
					if (!ValidatorUtil.isValid(app_id) && !ValidatorUtil.isValid(lead_id)) {
						if (request.getParameter("generatePDF") != null) {

						} else if (request.getParameter("requestIndex") == null) {
							if (SessionUtil.getApplicationType() == null) {

							} else {

								releaseSession(Constants.PERSONAL_LOAN_ID);
							}
							applicationTypeId = 2;
							SessionUtil.setApplicationType(2);
							appSeqId = null;
							SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
							appLeadId = null;
							SessionUtil.setLeadId(appLeadId);
							firstName = null;
							mobileNo = null;
							email = null;
			                alternateMobileNo = null;
							SessionUtil.setEmail(email);
							SessionUtil.setMobile(mobileNo);
							SessionUtil.setApplicantName(firstName);
              				SessionUtil.setalternateMobileNumber(alternateMobileNo);
						}
					} else if (ValidatorUtil.isValid(app_id)) {
						appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
						SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
						visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
						SessionUtil.setVisitIdPL(visitId);
						firstName = null;
						SessionUtil.setApplicantName(firstName);
						appLeadId = null;
						SessionUtil.setLeadId(appLeadId);
						if (request.getParameter("requestIndex") == null) {
							applicationTypeId = 0;
							SessionUtil.setApplicationType(0);
						}
					} else if (ValidatorUtil.isValid(lead_id)) {
						appLeadId = Integer.parseInt(Security.decrypt(lead_id, bankLmsUser.getLmsHashKey()));
						lead = commonService.getLeadById(appLeadId);
						if (lead != null) {
							SessionUtil.setLeadId(appLeadId);
							email = lead.getLeadWorkEmail() != null ? lead.getLeadWorkEmail().toString() : "";
							SessionUtil.setEmail(email);
							if (lead.getLeadApplyingFrom() == 2) {
								mobileNo = lead.getLeadMobileNo() != null ? lead.getLeadMobileNo() : "";
								SessionUtil.setISDCode(lead.getLeadIsdCode() != null ? lead.getLeadIsdCode() : "");
								
								//Added for displaying alt no in DSR page for UPLOAD functionality
								alternateMobileNo = lead.getLeadAlternateNumber();			
							} else {
								mobileNo = lead.getLeadMobileNo() != null ? lead.getLeadMobileNo().toString() : "";
								SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
								
								//Added for displaying alt no in DSR page for UPLOAD functionality
								alternateMobileNo = lead.getLeadAlternateNumber();							
							}
							
							boolean isTeleCallerUser = false;
							isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
							
							if (isTeleCallerUser && mobileNo != null && mobileNo != "") {
								isMobileNoMask = "true";
								mobileNoMaskVal = mobileNo.replaceAll("\\d(?=\\d{4})", "*");
							}
							if (isTeleCallerUser && email != null  && email !="") {
								isEmailMask = "true";
								emailMaskVal=email.replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
							}
							if (isTeleCallerUser && alternateMobileNo != null) {
								isalternateMobileNoMask = "true";
								alternateMobileNoMaskVal = alternateMobileNo.replaceAll("\\d(?=\\d{4})", "*");
							}
					
							SessionUtil.setMobile(mobileNo);
            				SessionUtil.setalternateMobileNumber(alternateMobileNo);
							firstName = lead.getLeadFirstName() != null ? lead.getLeadFirstName() : "";
							SessionUtil.setApplicantName(firstName);
							if (lead.getLeadAppSeqId() != null) {
								appSeqId = lead.getLeadAppSeqId();
								visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
								SessionUtil.setVisitIdPL(visitId);
							} else {
								appSeqId = null;
							}
							SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
							
							logger.info("leadConsentId for get a call back lead: " + lead.getLeadConsentId() + "  :: lead id::" + appLeadId);
							leadConsentId = lead.getLeadConsentId()!=null ? lead.getLeadConsentId() : 0;
							
						}
						if (request.getParameter("requestIndex") == null) {
							SessionUtil.setApplicationType(1);
							applicationTypeId = 1;
						}
					} else if (SessionUtil.getPersonalLoanTypeSequenceId() != null) {
						if (request.getParameter("requestIndex") == null) {
							releaseSession(Constants.PERSONAL_LOAN_ID);
						}
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						bankLmsUser = SessionUtil.getBankLMSUser();
					}
					if (ValidatorUtil.isValid(applicationTypeId)) {
						SessionUtil.setApplicationType(applicationTypeId);
					}
					if (request.getParameter("requestIndex") == null) {
						requestIndex = 9;
					}
				} else {
					if (!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {	
					  //return "home" + (uiType == null ? "" : uiType);		//commented for CSR 2018 - Session management
						
						// Added for CSR 2018 - Session management
						if(appSeqId == null){
							String lmsURL = Constants.BANK_ONLINE_URL;
							sessionId = null;						
							responseMessage = "error|"+"Sorry for the inconvenience, your session has been timed out. Please click <a href='" + lmsURL + "'> here </a> to start again";	
							logger.info("responseMessage 240..." + responseMessage);      
					        
							return "jsonResponsePage";
						}
						
					} else {
						bankLmsUser = SessionUtil.getBankLMSUser();
						if (bankLmsUser != null) {
							appBankLmsUserId = bankLmsUser.getLmsUserId();
							contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
							if (SessionUtil.getLeadId() != null) {
								appLeadId = SessionUtil.getLeadId();
								lead = commonService.getLeadById(appLeadId);
								appSeqId = lead.getLeadAppSeqId();
							} else if (appSeqId == null) {
								appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							}
						} else {
							if (appBankLmsUserId == null) {
								appBankLmsUserId = 4826;
							}
							bankLmsUser = commonService.getBankLmsUserById(appBankLmsUserId);
							SessionUtil.setBankLMSUser(bankLmsUser);
							contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
							if (!ValidatorUtil.isValid(appSeqId) && !ValidatorUtil.isValid(appLeadId)) {
								applicationTypeId = 2;
								SessionUtil.setApplicationType(2);
							} else if (ValidatorUtil.isValid(appSeqId)) {
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
								if (request.getParameter("requestIndex") == null) {
									applicationTypeId = 0;
									SessionUtil.setApplicationType(0);
								}
							} else if (ValidatorUtil.isValid(appLeadId)) {
								SessionUtil.setLeadId(appLeadId);
								if (lead == null) {
									lead = commonService.getLeadById(appLeadId);
								}
								email = lead.getLeadWorkEmail() != null ? lead.getLeadWorkEmail().toString() : "";
								SessionUtil.setEmail(email);
								if (lead.getLeadApplyingFrom() == 2) {
									mobileNo = lead.getLeadMobileNo() != null ? lead.getLeadMobileNo() : "";
									SessionUtil.setISDCode(lead.getLeadIsdCode() != null ? lead.getLeadIsdCode() : "");
								} else {
									mobileNo = lead.getLeadMobileNo() != null ? lead.getLeadMobileNo().toString() : "";
									SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
								}
								SessionUtil.setMobile(mobileNo);
           						SessionUtil.setalternateMobileNumber(alternateMobileNo);
								firstName = lead.getLeadFirstName() != null ? lead.getLeadFirstName() : "";
								SessionUtil.setApplicantName(firstName);
								if (lead.getLeadAppSeqId() != null) {
									appSeqId = lead.getLeadAppSeqId();
								} else {
									appSeqId = null;
								}
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
								if (request.getParameter("requestIndex") == null) {
									SessionUtil.setApplicationType(1);
									applicationTypeId = 1;
								}
							} else if (SessionUtil.getPersonalLoanTypeSequenceId() != null) {
							}

							if (ValidatorUtil.isValid(applicationTypeId)) {
								SessionUtil.setApplicationType(applicationTypeId);
							}
							if (request.getParameter("requestIndex") == null) {
								requestIndex = -1;
							}
							visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
							SessionUtil.setVisitIdPL(visitId);
						}
					}
				}
				logger.info("before getPersonalLoan call from personalLoanDSR() :: Loan ID.." +Constants.PERSONAL_LOAN_ID);
				return getPersonalLoan(Constants.PERSONAL_LOAN_DSR_ID);
			} catch (NullPointerException e) {
				logger.info("PersonalLoanAction.java LN 252 personalLoanDSR() ::" + e.getMessage());
				return "home" + (uiType == null ? "" : uiType);
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java LN 252 personalLoanDSR() ::" + e.getMessage());
				return "home" + (uiType == null ? "" : uiType);
			}
		}
	  
	  public String personalLoan() {
	    try {
	      if (ValidatorUtil.isValid( uiType)) {
	        SessionUtil.setUiType( uiType);
	      } else {
	        SessionUtil.setUiType(null);
	      } 
	       isDsrPage = "false";
	       ajaxPostUrl = Constants.PERSONAL_LOAN_ACTION;
	       appPLTypeId = Constants.APP_PL_TYPE_PERSONAL;
	      SessionUtil.setPersonalTypeId( appPLTypeId);
	      SessionUtil.setApplicationType( (0));
	       request = RequestUtil.getServletRequest();
	      if (SessionUtil.getBankLMSUser() != null) {
	         isOnlineAndDsrActive = true;
	        releaseSession(Constants.PERSONAL_LOAN_ID);
	      } 
	      
	      if (!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
			} else {
				if (appSeqId != null) {
					SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
					visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdPL(visitId);
				}
			}
	      
	      logger.info("before getPersonalLoan call from personalLoan() :: Loan ID.." + Constants.PERSONAL_LOAN_ID);
	      return getPersonalLoan(Constants.PERSONAL_LOAN_ID);
	    } catch (NullPointerException e) {
		      logger.info("PersonalLoanAction.java LN 283 exception ::" + e.getMessage());
		      return "homePage" + (( uiType == null) ? "" :  uiType);
		} catch (SQLException e) {
	      logger.info("PersonalLoanAction.java LN 283 exception ::" + e.getMessage());
	      return "homePage" + (( uiType == null) ? "" :  uiType);
	    } 
	  }
	  
	  public String pensionLoan() {
	    try {
	      if (ValidatorUtil.isValid( uiType)) {
	        SessionUtil.setUiType( uiType);
	      } else {
	        SessionUtil.setUiType(null);
	      } 
	       appPLTypeId = Constants.APP_PL_TYPE_PENSION;
	      if ( appPLTypeId != null && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION)
	        SessionUtil.setPersonalTypeId(null); 
	      SessionUtil.setPersonalTypeId( appPLTypeId);
	       loanTypeId = Constants.PERSONAL_LOAN_ID;
	       isDsrPage = "false";
	       ajaxPostUrl = Constants.PENSION_LOAN_ACTION;
	      SessionUtil.setApplicationType( (0));
	       request = RequestUtil.getServletRequest();
	      if (SessionUtil.getBankLMSUser() != null) {
	         isOnlineAndDsrActive = true;
	        releaseSession(Constants.PERSONAL_LOAN_ID);
	      } 
	      
	      if (Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
			} else {
				if (appSeqId != null) {
					SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
					visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdPL(visitId);
				}
			}
	      
	      logger.info("before getPersonalLoan call from pensionLoan() :: Loan ID.." + Constants.PERSONAL_LOAN_ID);
	      return getPersonalLoan(Constants.PERSONAL_LOAN_ID);
	    } catch (NullPointerException e) {
		      logger.info("PensionLoanAction.java LNo : 206 : pensionLoan() " + e.getMessage());
		      return "homePage" + (( uiType == null) ? "" :  uiType);
		}  catch (SQLException e) {
	      logger.info("PensionLoanAction.java LNo : 206 : pensionLoan() " + e.getMessage());
	      return "homePage" + (( uiType == null) ? "" :  uiType);
	    } 
	  }
	  
	  public String goldLoan() {
	    try {
	      if (ValidatorUtil.isValid( uiType)) {
	        SessionUtil.setUiType( uiType);
	      } else {
	        SessionUtil.setUiType(null);
	      } 
	       appPLTypeId = Constants.APP_PL_TYPE_GOLD;
	      if ( appPLTypeId != null && SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD)
	        SessionUtil.setPersonalTypeId(null); 
	      SessionUtil.setPersonalTypeId( appPLTypeId);
	       goldType =  (27);
	       loanTypeId = Constants.PERSONAL_LOAN_ID;
	       isDsrPage = "false";
	       ajaxPostUrl = Constants.GOLD_LOAN_ACTION;
	      SessionUtil.setApplicationType( (0));
	       request = RequestUtil.getServletRequest();
	      if (SessionUtil.getBankLMSUser() != null) {
	         isOnlineAndDsrActive = true;
	        releaseSession(Constants.PERSONAL_LOAN_ID);
	      } 
	     
	      if (!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
			} else {
				if (appSeqId != null) {
					SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
					visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdPL(visitId);
				}
			}
	      
	      logger.info("before getPersonalLoan call from goldLoan() :: Loan ID.." + Constants.PERSONAL_LOAN_ID);
	      return getPersonalLoan(Constants.PERSONAL_LOAN_ID);
	    } catch (NullPointerException e) {
		      logger.info("goldLoanAction.java LNo : 206 : goldLoan() " + e.getMessage());
		      return "homePage" + (( uiType == null) ? "" :  uiType);
		} catch (SQLException e) {
	      logger.info("goldLoanAction.java LNo : 206 : goldLoan() " + e.getMessage());
	      return "homePage" + (( uiType == null) ? "" :  uiType);
	    } 
	  } 
	  
	  
	  public String getPersonalLoan(Integer moduleId) {
	    if (iPAddressForAppAndDBServerPass != 1)
	      isValidIpAddressForAppAndDBServer(); 
	    if (iPAddressForAppAndDBServerPass == 0)
	      return "under-maintainance" + (( uiType == null) ? "" :  uiType); 
	    if (!UIBeanListStatic.isDataSet)
	      setStaticData(); 
	     json = new JSONObject();
	    try {
	    	
	    	loanTypeId=Constants.PERSONAL_LOAN_ID;
			personalLoanPage = 1;
			stateManagerBean=stateManager.getState(request, moduleId);
			SbiUtil.getOcasSessionId(request);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			
			if(SessionUtil.getGoldLoan() != null && (stateManagerBean.getState() != 1 && stateManagerBean.getState() != -1 && stateManagerBean.getState() != 27) && isDsrPage == "true" && SessionUtil.getGoldLoan().trim().equals("27")) {
				stateManagerBean.setState(17);
			}
			
			if(SessionUtil.getVisitIdPL()!=null){
				visitId = SessionUtil.getVisitIdPL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId ==null ){
					if(SessionUtil.getPersonalLoanTypeSequenceId()==null || moduleId==Constants.PERSONAL_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.PERSONAL_LOAN_ID );
						if (!(campaignCode == null && offerCode == null && trackingCode == null) && appPLTypeId == Constants.APP_PL_TYPE_GOLD) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.PERSONAL_LOAN_ID, 0);
						}
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdPL(visitId);
						}else{
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}	
	    		
			 if(stateManagerBean.getState()==-1){
				 logger.info("state manager -1 calling PERSONAL LOAN::ajaxPostUrl.."+ajaxPostUrl);
				if(stateManagerBean.getValidatorResponse().isStatus()){
					metaInfo.setTitle(Constants.PERSONAL_LOAN_TITLE);
					metaInfo.setKeywords(Constants.PERSONAL_LOAN_KEYWORDS);
					metaInfo.setDescription(Constants.PERSONAL_LOAN_DESCRIPTION);
					
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
				logger.info("state manager 41  & 42 is called");
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


			if(stateManagerBean.getState()==33 || stateManagerBean.getState()==34){
				logger.info("state manager 33  & 34 is called");
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
      		
      		if (stateManagerBean.getState() == 28 || stateManagerBean.getState() == 29) {
	        logger.info("state manager 28 and 29 is called PERSONAL/PENSION/GOLD.......");
	        if ( stateManagerBean.getValidatorResponse().isStatus()) {
	          OtherRequest otherRequest =  stateManagerBean.getOtherRequest();
	          int appOTPVerifyType = 0;
	          if (otherRequest != null && otherRequest.getAppOTPVerifyType() != null)
	            appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim()); 
	          String inputOtp = null;
	          if (otherRequest != null && otherRequest.getInputOtp() != null)
	            inputOtp = otherRequest.getInputOtp(); 
	          String userEmail = null;
	          if (otherRequest != null && otherRequest.getUserEmail() != null)
	            userEmail = otherRequest.getUserEmail(); 
	           json =  processManagerPersonalImpl.processCBSOTP(moduleId,  ( stateManagerBean.getState()), 
	              ( bankLmsUser != null) ?  bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID,  ajaxPostUrl, 
	              appOTPVerifyType, inputOtp, userEmail, SessionUtil.getPersonalLoanTypeSequenceId(), 
	              SessionUtil.getPlTypeCbsCallId());
	          logger.info("PersoanlLoanAction.java :: processCBSOTP called..json:::: " +  json);
	          logger.info("PersoanlLoanAction.java :: processCBSOTP called..inputOtp:::: " + inputOtp);
	          logger.info("PersoanlLoanAction.java :: processCBSOTP called..appOTPVerifyType :::: " + appOTPVerifyType);
	          if ( json.get("status").toString().equalsIgnoreCase("success")) {
	            if ( stateManagerBean.getState() == 29) {
	              generateUIBeanList();
	               responseMessage = "success|" +  json.getString("message");
	              logger.info("PersoanlLoanAction.java :: processCBSOTP called..json:::: success1:: " +  json);
	              return "jsonResponsePage";
	            } 
	             responseMessage = "success|" +  json.getString("message");
	            logger.info("PersoanlLoanAction.java :: processCBSOTP called..json::::success2 " +  json);
	            return "jsonResponsePage";
	          } 
	           responseMessage = "error|" +  json.getString("message");
	          logger.info("PersoanlLoanAction.java :: processCBSOTP called..json::::error " +  json);
	          return "jsonResponsePage";
	        } 
	        String msg = CommonUtilites.serchingValuesFromMaps( stateManagerBean.getValidatorResponse().getErrorMessage());
	         responseMessage = "error|" + msg;
	        return "jsonResponsePage";
	      }  
	  
	     if (stateManagerBean.getState() == 27) {
			 logger.info("PersonalLoanAction.java LN 1264 PERSONAL/PENSION/GOLD for getState()==27"+stateManagerBean.getState());
	        
	          if ( stateManagerBean.getValidatorResponse().isStatus()) {
	            try {
	               cbs =  stateManagerBean.getCbs();
//	              logger.info("PersonalLoanAction.java LN 684 cbs ::::: " +  cbs);
	              if ( isDsrPage.equalsIgnoreCase("false") && ! cbs.getInfoprovideCBS().equals("on")) {
	                 responseMessage = "error|" + Constants.SORRY_FOR_INCONVENIENCE;
	                logger.info("PersonalLoanAction.java LN 688 responseMessage ::::: " +  responseMessage);
	                return "jsonResponsePage";
	              } 
	              CBSCallResponse cbsCallResponse =  processManagerPersonalImpl.processCbsCall( appSeqId,  requestIndex,  cbs, 
	                   isDsrPage, ( bankLmsUser != null) ?  bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID,  visitId, 
	                  SessionUtil.getPersonalLoanCbsCallId(),  (11), null, null, Constants.PERSONAL_LOAN_ACTION);
	              logger.info("PersonalLoanAction.java LN 695 cbsCallResponse::::: " + cbsCallResponse);
	              if (cbsCallResponse.getStatus() != null) {
	                if (cbsCallResponse.getStatus().intValue() == 0) {
	                   responseMessage = "error|" + cbsCallResponse.getResponseMsg();
	                  logger.info("PersonalLoanAction.java LN 700 Exception occured:::::" +  responseMessage);
	                  return "jsonResponsePage";
	                } 
	                if (cbsCallResponse.getStatus().intValue() == 1) {
	                  logger.info("PersonalLoanAction.java LN 703 Exception occured:::::" +  responseMessage);
	                  return "cbsOtpPage" + (( uiType == null) ? "" :  uiType);
	                } 
	                if (cbsCallResponse.getStatus().intValue() == 2) {
	                  logger.info("PersonalLoanAction.java LN 706 Exception occured:::::" +  responseMessage);
	                  return "cbsVerifyButtonPage" + (( uiType == null) ? "" :  uiType);
	                } 
	              } 
	            } catch (NullPointerException e) {
		              logger.info("PersonalLoanAction.java LN 370 Exception occured:::::" + e.getMessage());
		               responseMessage = "error|" + Constants.SORRY_FOR_INCONVENIENCE;
		              return "jsonResponsePage";
		        } catch (SQLException e) {
	              logger.info("PersonalLoanAction.java LN 370 Exception occured:::::" + e.getMessage());
	               responseMessage = "error|" + Constants.SORRY_FOR_INCONVENIENCE;
	              return "jsonResponsePage";
	            } 
	          } else {
	            String msg = CommonUtilites.serchingValuesFromMaps( stateManagerBean.getValidatorResponse().getErrorMessage());
	             responseMessage = "error|" + msg;
	            return "jsonResponsePage";
	          } 
	        }  
	           
	      if(stateManagerBean.getState()==26){
				try {
					if(stateManagerBean.getValidatorResponse().isStatus()){
						releaseSession(Constants.PERSONAL_LOAN_ID);
						appSeqId=null;
						appLeadId=null;
						firstName=null;
						mobileNo=null;
            			alternateMobileNo = null;
						email=null;
						stateManagerBean.setState(-1);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 360 stateManagerBean.getState()==26 ::" + e.getMessage());
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} 
			}

			

			if(stateManagerBean.getState()==25){
				try {
					/*if(stateManagerBean.getValidatorResponse().isStatus()){
						//String currentOcasId = SbiUtil.getOcasSessionId(request);
						Integer plTypeId = SessionUtil.getPersonalTypeId();
						releaseSession(Constants.PERSONAL_LOAN_ID);
						String appReferenceId = request.getParameter("generatePDF").substring(0, 12);
						if(ValidatorUtil.isValid(appReferenceId)){
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppReferenceId(appReferenceId);
							appSeqId = appForm.getAppSeqId();
							if(appForm!=null){
								if(ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									if (ValidatorUtil.isValid(appForm.getOcasID())) {
										String filePath = Constants.PDF_GENRATION_BASE_PATH + Constants.PL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
										inputStream = new FileInputStream(new File(filePath));
										SessionUtil.setPersonalTypeId(plTypeId);
										SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
										SessionUtil.setPersonalLoanApplicationSequenceId(appSeqId);
										SessionUtil.setPensionLoanApplicationSequenceId(appSeqId);
										return "downLoadPDF";
									} else {
										logger.info("PersonalLoanAction.java Ocas ID is not same as valid Ocas ID " + appSeqId);
										SessionUtil.setPersonalLoanApplicationSequenceId(appSeqId);
										SessionUtil.setPensionLoanApplicationSequenceId(appSeqId);
										SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
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
						appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
						
						
						if (SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_GOLD) && ajaxPostUrl.equals(Constants.GOLD_LOAN_ACTION)) {
							appSeqId = SessionUtil.getPersonalGoldLoanSequenceId();
							logger.info("gold loan before document dowmload appSeqId:::" + SessionUtil.getPersonalGoldLoanSequenceId());
						}
						
						if (ValidatorUtil.isValid(appSeqId)) {
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									String filePath = Constants.PDF_GENRATION_BASE_PATH
											+ Constants.PL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
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
					logger.info("PersonalLoanAction.java LN 385 stateManagerBean.getState()==25 ::" + e.getMessage());
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
			}

			

			if(stateManagerBean.getState()==23){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isDsrPage.equalsIgnoreCase("true")){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							getCallLogs(appSeqId, appLeadId);
						}
						return "callsLogDetails"+(uiType==null?"":uiType);
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
					
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 406 stateManagerBean.getState()==23 ::" + e.getMessage());
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
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId=SessionUtil.getPersonalLoanTypeSequenceId();
						}
						
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						logger.info("PersonalLoanAction.java LNo:329 :: appSeqId :: "+appSeqId+" imageNo :: "+imageNo+" ajaxPostUrl :"+ajaxPostUrl + "imageName :: "+imageName );
						json = processManagerPersonalImpl.processDeleteProductImage(appSeqId, imageNo, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, imageName);
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
					logger.info("PersonalLoanAction.java LN 440 stateManagerBean.getState()==22 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==21){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppPL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
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
						json = processManagerPersonalImpl.processToDocumentPickupUploaded(appSeqId, appForm, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("PersonalLoanAction.java LN 477 stateManagerBean.getState()==21 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			} 
	      
	      
			if(stateManagerBean.getState()==20){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}					
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						appForm=personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|N/A.pdf";
							return "jsonResponsePage";
						}
						if(appForm.getAppDownloadPdfFileName()!=null){
							SecureRandom rand = new SecureRandom();
							if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_PENSION) && ajaxPostUrl.equals(Constants.PENSION_LOAN_ACTION)){
								logger.info("PersonalLoanAction.java LN 806 stateManagerBean.getState()==20 ::"+SessionUtil.getPersonalTypeId());
								responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+(Constants.PENSION_LOAN_ACTION)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
							}else if(SessionUtil.getPersonalTypeId().equals(Constants.APP_PL_TYPE_GOLD) && ajaxPostUrl.equals(Constants.GOLD_LOAN_ACTION)){
								responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+(Constants.GOLD_LOAN_ACTION)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
							}else{
								responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+((moduleId == Constants.PERSONAL_LOAN_ID.intValue())?Constants.PERSONAL_LOAN_ACTION:Constants.PERSONAL_LOAN_ACTION_DSR)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
							}
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
					logger.info("PersonalLoanAction.java LN 504 stateManagerBean.getState()==20 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==18){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppPL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
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
						json = processManagerPersonalImpl.processToDocumentPickupUploaded(appSeqId, appForm, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl);
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
					logger.info("PersonalLoanAction.java LN 569 stateManagerBean.getState()==18 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(SessionUtil.getGoldLoan() != null && !SessionUtil.getGoldLoan().trim().equals("27") && stateManagerBean.getState()==17){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppPL();
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
						
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
										
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						
						appForm = processManagerPersonalImpl.processSubmitQuote(appSeqId, stateManagerBean.getState(), appForm, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appForm.getError()!=null){
							responseMessage = "error|"+appForm.getError();
							return "jsonResponsePage";
						}
						personalLoanPage = 4;
						generateUIBeanList(personalLoanPage);
						if(appForm.getAppPersonalLoanId()!=null && appForm.getAppPersonalLoanId()==6 || appForm.getAppPersonalLoanId()==7 || appForm.getAppPersonalLoanId()==8 ){
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID, 6);
						}else if(appForm.getAppPersonalLoanId()!=null && appForm.getAppPersonalLoanId()==9 || appForm.getAppPersonalLoanId()==10 ){
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID, 2);
						}else if(appForm.getAppPersonalLoanId()!=null && appForm.getAppPersonalLoanId()==13){
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID,13);
						}else {
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.PERSONAL_LOAN_ID, appForm.getAppPersonalLoanId() );
						}

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
					logger.info("PersonalLoanAction.java LN 613 stateManagerBean.getState()==17 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
	      
	      
			if(stateManagerBean.getState()==14 || stateManagerBean.getState()==15 || stateManagerBean.getState()==16){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = null;
              			if (stateManagerBean.getState() == 14 || stateManagerBean.getState() == 15 || stateManagerBean.getState() == 16) {
							otherRequest = stateManagerBean.getOtherRequest();
						}
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(appSeqId!=null && (ajaxPostUrl.equalsIgnoreCase("personal-loan") || ajaxPostUrl.equalsIgnoreCase("gold-loan"))){	
								if(stateManagerBean.getState()==16){
									if (!Constants.PL_CAPTCHA_BY_PASS && otherRequest.getAlternateMobileNumber() == null) {
									responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
									if (responseMessage!=null) {
										return "jsonResponsePage";
									}
								}
							}
						}
						if(stateManagerBean.getState() == 14 && otherRequest.getAlternateMobileNumber() == null) {
							otherRequest.setMobileNo(SessionUtil.getResendMobileNumber());
						}
						json = processManagerPersonalImpl.processMobileOTP(appSeqId,stateManagerBean.getState(), null, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId,otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							appForm=personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if(stateManagerBean.getState() == 14) {
								appForm.setAppMobileNo(SessionUtil.getResendMobileNumber());
							}
							quote=personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote.getLoanQuoteLoanPurposeId()==11 && quote.getLoanQuoteIndustryType()!=null && (quote.getLoanQuoteIndustryType()==1 ||quote.getLoanQuoteIndustryType()==15 || quote.getLoanQuoteIndustryType()==16
									|| quote.getLoanQuoteIndustryType()==17) && (appForm==null || (appForm.getAppMobileVerified()!=null && appForm.getAppMobileVerified().equalsIgnoreCase("Y")) || 
									(appForm.getAppEmailVerified()!=null && appForm.getAppEmailVerified().equalsIgnoreCase("Y"))) && otherRequest.getAlternateMobileNumber() == null){
								stateManagerBean.setState(1);

							}else{
								responseMessage = "success|"+json.getString("message");
								if(json.getString("alertCount")!=null){
									responseMessage =responseMessage+"|"+json.getString("alertCount");
								}
								
								if(otherRequest.getAlternateMobileNumber() == null && stateManagerBean.getState() != 15) {
									logger.info("PersonalLoanAction.java LNo : 1021 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
									responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
									logger.info("PersonalLoanAction.java LNo : 1023 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
								}
								if(stateManagerBean.getState() == 16 && quote.getLoanQuoteLoanPurposeId() == 27) {
									stateManagerBean.setState(17);
									SessionUtil.setCaptch(null);
								} else {
									SessionUtil.setCaptch(null);
									return "jsonResponsePage";									
								}
							}			
							
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							if(otherRequest.getAlternateMobileNumber() == null && stateManagerBean.getState() != 15) {
								logger.info("PersonalLoanAction.java LNo : 1035 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
								logger.info("PersonalLoanAction.java LNo : 1037 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
							}
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 650 stateManagerBean.getState()==14, 15, 16 ::"+stateManagerBean.getState()+" :: "+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
					
			if(stateManagerBean.getState()==13){
				try {
					//Integer accountType=null;
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest=stateManagerBean.getOtherRequest();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}
					   	
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
						//if(otherRequest.getChosenLoanAccountType()!=null && !otherRequest.getChosenLoanAccountType().isEmpty() && !("").equalsIgnoreCase(otherRequest.getChosenLoanAccountType())){
							//accountType=Integer.parseInt(otherRequest.getChosenLoanAccountType());
						//}
						appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
						if(appForm == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if((appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_WEB_LEAD) || appForm.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_CALL_BACK) ) 
								&&  appForm.getAppMobileVerified().equalsIgnoreCase("N") && appForm.getAppEmailVerified().equalsIgnoreCase("N") ){
							responseMessage = "error|Mobile OTP is not verified";
							return "jsonResponsePage";
						}else{
							quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							quote.setLoanQuoteLoanProductId(Integer.parseInt(applyUrlData[0]));
							quote.setLoanQuoteLoanTenure(chosenTenure);
							quote.setLoanQuoteLoanAmount(chosenEligibility);
							quote.setLoanQuoteAppliedCoupon(appForm.getAppLoanAppliedCoupon());
							if(quote.getLoanQuoteCheckOffType()!=null ){
								quote.setLoanQuoteLoanAccountType(Integer.parseInt(applyUrlData[1]));
							}
							
							
							if(quote !=null) {
								if(quote.getLoanQuoteLoanPurposeId() !=null && !quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)) {
									if (!ValidatorUtil.validateFirstName(quote.getLoanQuoteFirstName())) {
										responseMessage="error|First name is not in correct format. Please enter only [a-z].";
										return "jsonResponsePage";
									}
									if (!ValidatorUtil.validateMiddleName(quote.getLoanQuoteMiddleName())) {
										responseMessage="error|Middle name is not in correct format. Please enter only [a-z].";
										return "jsonResponsePage";
									}
									if (!ValidatorUtil.validateLastName(quote.getLoanQuoteLastName())) {
										responseMessage="error|Last name is not in correct format. Please enter only [a-z] & do not include spaces.";
										return "jsonResponsePage";
									}
								}
							}
							if(appForm !=null) {
								if (!ValidatorUtil.validateFirstName(appForm.getAppFirstName())) {
									responseMessage="error|First name is not in correct format. Please enter only [a-z].";
									return "jsonResponsePage";
								}
								if (!ValidatorUtil.validateMiddleName(appForm.getAppMiddleName())) {
									responseMessage="error|Middle name is not in correct format. Please enter only [a-z].";
									return "jsonResponsePage";
								}
								if (!ValidatorUtil.validateLastName(appForm.getAppLastName())) {
									responseMessage="error|Last name is not in correct format. Please enter only [a-z] & do not include spaces.";
									return "jsonResponsePage";
								}
							}
							
							loanScenarioBean = personalLoanHelper.callBRE(appForm, quote, bankLmsUser, quote.getLoanQuoteId(), quote.getLoanQuoteNewVisitId(), ajaxPostUrl, false);
							
							if(loanScenarioBean.getStatus()!=1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								return "jsonResponsePage";
							}
							appForm = loanScenarioBean.getApplicationPL();
							
							appForm.setAppPersonalLoanId(Integer.parseInt(applyUrlData[0]));
							if(quote.getLoanQuoteCheckOffType()!=null ){
								appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
							}
							appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
							appForm.setAppLoanProcessingFee((double)Double.parseDouble(applyUrlData[3]));
							appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));

							appForm.setAppEmiNmiRatio(Float.parseFloat(applyUrlData[6]));
							
							appForm.setAppLoanEmiDiscount(applyUrlData[7].toString().equalsIgnoreCase("-")?null:Double.parseDouble(applyUrlData[7]));
							appForm.setAppLoanProcessingFeeDiscount(applyUrlData[8].toString().equalsIgnoreCase("-")?null:(double) Double.parseDouble(applyUrlData[8]));
							appForm.setAppLoanInterestRateDiscount(applyUrlData[9].toString().equalsIgnoreCase("-")?null:Float.parseFloat(applyUrlData[9]));
							
							MasterPlProduct product =  commonService.getPersonalLoanProductById(appForm.getAppPersonalLoanId());
							appForm.setAppProductTenureFlag(product.getPLProductSliderTenure());
							
							if(SessionUtil.getApplicationType()!=null){
								StatusRequest statusRequest = new StatusRequest();
								statusRequest.setCurrentStatus(appForm.getAppLoanStatusId());
								statusRequest.setHaveLoanOffer(true);
								statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
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
									personalLoanHelper.insertCallLog(appForm.getAppSeqId(),(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), statusManagerResponse.getStatusCallLogs(), null, null, true);
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
								
								if (isTeleCallerUser) {
									if (appForm.getAppPanCardNo() != null) {
										isPanNoMask = "true";
										panNoMaskVal = appForm.getAppPanCardNo().replaceAll("^.{6}", "******");
									}
									if (appForm.getAppAlternateMobileNumber() != null) {
										isalternateMobileNoMask = "true";
										alternateMobileNoMaskVal = appForm.getAppAlternateMobileNumber().replaceAll("\\d(?=\\d{4})", "*");
									}
									if (email != null  && email !="") {
										isEmailMask = "true";
										emailMaskVal=email.replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
									}
									if (quote.getAppEmail() != null  && quote.getAppEmail()  !="") {
										isEmailMask = "true";
										emailMaskVal=email.replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
									}
									if (appForm.getAppSalaryAccNo()!= null) {
										isSalaryAccnum="true";
									    salaryAccnumVal = appForm.getAppSalaryAccNo().replaceAll("\\d(?=\\d{4})", "*");
									}
									if (appForm.getAppPensionAccountNumber()!= null) {
										isPensionAccnum="true";
										pensionAccnumVal = appForm.getAppPensionAccountNumber().replaceAll("\\d(?=\\d{4})", "*");
									}
									if (appForm.getAppOtherIdNumber()!= null) {
										isAppotherIdNum="true";
										appotherIdNumVal = appForm.getAppOtherIdNumber().replaceAll("[a-zA-Z0-9]", "*");
									}
								}
							}
							
							logger.info("PersonalLoanAction.java :: Call1 getLoanQuoteTermODType " + quote.getLoanQuoteTermODType());
							appForm = personalLoanService.save(appForm);
							quote= personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote!=null){

							}
							logger.info("PersonalLoanAction.java :: Call2 getLoanQuoteTermODType " + quote.getLoanQuoteTermODType());
							quote=personalLoanService.save(quote);
							if(quote==null){
								logger.info("PersonalLoanAction.java :950 :: error saving quote");
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
							personalLoanPage = 3;
							generateUIBeanList(personalLoanPage);
							jsonJSArray3PersonalLoan = SbiUtil.populateJSValidation(17, moduleId).toString();
							
							if(SessionUtil.getPersonalLoanCbsCallId()!=null){
								cbs = commonService.getMasterCBSCallObjById(SessionUtil.getPersonalLoanCbsCallId());
							}

							if(SessionUtil.getPersonalLoanCbsCallId()!=null){
								cbs = commonService.getMasterCBSCallObjById(SessionUtil.getPersonalLoanCbsCallId());
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
							        	logger.info("OTP for Mobile Number: " + (String.valueOf(appForm.getAppISDCode()) + appForm.getAppMobileNo()).trim() + " is " + appForm.getAppMobileVerificationCode().toString());
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
					logger.info("PersonalLoanAction.java LN 771 stateManagerBean.getState()==13 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
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
					logger.info("PersonalLoanAction.java LN 780 stateManagerBean.getState()==12 ::"+ e.getMessage());
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
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}

						if(quote.getLoanQuoteDateOfBirth()!=null){
							quote.setLoanQuoteDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantDateOfBirth(), "MM/dd/yyyy"));
						}
						json = processManagerPersonalImpl.processAddCoapplicant(moduleId, quote, ajaxPostUrl);
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
					logger.info("PersonalLoanAction.java LN 818 stateManagerBean.getState()==11 ::"+ e.getMessage());
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
					logger.info("PersonalLoanAction.java LN 613 stateManagerBean.getState()==17 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
	      
	      
	      
			if(stateManagerBean.getState()==9){
				try{
					stateManagerBean.setOriginalState(stateManagerBean.getState());
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(moduleId==Constants.PERSONAL_LOAN_ID){
							releaseSession(Constants.PERSONAL_LOAN_ID);
							appSeqId=null;
							lead=null;
						}
						if(request.getParameter("PLQuoteToken")!=null){

							String encyQuotId=aesEncryption.decrypt(request.getParameter("PLQuoteToken").toString());
							if(ValidatorUtil.isValid(encyQuotId)){
								appSeqId = Integer.parseInt(encyQuotId);
								appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
								quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
								if (Constants.PENSION_LOAN_PURPOSE_ID.equals(quote.getLoanQuoteLoanPurposeId())){
									appPLTypeId = Constants.APP_PL_TYPE_PENSION;
									SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PENSION);
									ajaxPostUrl=Constants.PENSION_LOAN_ACTION;
								}else if (quote.getLoanQuoteLoanPurposeId().intValue() == 11 || quote.getLoanQuoteLoanPurposeId().intValue() == 12){
									appPLTypeId = Constants.APP_PL_TYPE_PERSONAL;
									SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_PERSONAL);
									ajaxPostUrl=Constants.PERSONAL_LOAN_ACTION;
								}else if (quote.getLoanQuoteLoanPurposeId().intValue() == 27){
									appPLTypeId = Constants.APP_PL_TYPE_GOLD;
									SessionUtil.setPersonalTypeId(Constants.APP_PL_TYPE_GOLD);
									ajaxPostUrl=Constants.GOLD_LOAN_ACTION;
								}
								
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
								visitId = personalLoanService.getVisitByAppSeqId(appSeqId);
								SessionUtil.setVisitIdPL(visitId);
								stateManagerBean.setState(2);
							}else{
								stateManagerBean.setState(-1);
							}
						}else{
							logger.info("Inside Personal Loan State Id 9: outside landing state start if LMS caling appId : "+app_id + "--" +"tokenId : "+token_id + "--" +"leadId : "+lead_id);
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
											alternateMobileNo = lead.getLeadAlternateNumber();			
										}else{
											mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo().toString() : "";
											SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
											alternateMobileNo = lead.getLeadAlternateNumber();							
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
										SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
									}
									stateManagerBean.setState(-1);
								}else if(app_id!=null && app_id.length() >0){

									appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
									SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
									stateManagerBean.setState(2);	
								}else{
									stateManagerBean.setState(-1);
								}
							}else{
								releaseSession(Constants.PERSONAL_LOAN_ID);
								
								return "home"+(uiType==null?"":uiType);
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 927 stateManagerBean.getState()==9 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			

			if(stateManagerBean.getState()==8){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
						if(appSeqId == null){

							responseMessage = "error|Sorry for the inconvenience. Your session has been timed out, Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
							return "jsonResponsePage";
						}else{
							appForm=personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if(appForm == null){

								responseMessage = "error|Sorry for the inconvenience. Please click <a href='"+Constants.PORT+Constants.CONTEXT+ajaxPostUrl+(uiType==null?"":"?uiType=Constants.UI_TYPE")+"'>here</a> to start again.";
								return "jsonResponsePage";
							}else{
								float appLoanInterestRate=0f;
								double processingFee=0;
								double emi=0;
								int moratoriumMonths=0;
								double irEmi=0;
								if(request.getParameter("ir")!=null){
									appLoanInterestRate = Float.parseFloat(request.getParameter("ir"));
								}
								if(appLoanInterestRate<1){
									responseMessage = "error|Invalid parameter";
									return "jsonResponsePage";
								}
								
								if(request.getParameter("pId")!=null && Integer.parseInt(request.getParameter("pId"))==13){
									appForm.setProductId(Integer.parseInt(request.getParameter("pId")));
									if(request.getParameter("cTn")!=null && Integer.parseInt(request.getParameter("cTn"))<=12){
										if(request.getParameter("irEmi")!=null){
											irEmi = Double.parseDouble(request.getParameter("irEmi"));
										}
										if(irEmi<1){
											responseMessage = "error|Invalid parameter";
											return "jsonResponsePage";
										}
									}else {
										if(request.getParameter("emi")!=null){
											emi = Double.parseDouble(request.getParameter("emi"));
										}
										if(emi<1){
											responseMessage = "error|Invalid parameter";
											return "jsonResponsePage";
										}
									}
								}else{
									if(request.getParameter("emi")!=null){
										emi = Double.parseDouble(request.getParameter("emi"));
									}
									if(emi<1){
										responseMessage = "error|Invalid parameter";
										return "jsonResponsePage";
									}
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
								appForm.setAppLoanInterestRate(appLoanInterestRate);
								appForm.setAppLoanProcessingFee((double) Math.round(processingFee));
								if(request.getParameter("pId")!=null && Integer.parseInt(request.getParameter("pId"))==13){
									if(request.getParameter("cTn")!=null && Integer.parseInt(request.getParameter("cTn"))<=12){
										appForm.setAppLoanEmi(irEmi);
									}else{
										appForm.setAppLoanEmi(emi);
									}
								}else{
										appForm.setAppLoanEmi(emi);
								}
								if(appForm.getAppQuoteId() != null){
									quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
								}
								MasterPlProduct product =  commonService.getPersonalLoanProductById(appForm.getAppPersonalLoanId());
								appForm.setAppProductTenureFlag(product.getPLProductSliderTenure());
								
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
								if(request.getParameter("pId")!=null && Integer.parseInt(request.getParameter("pId"))==13 && request.getParameter("cTn")!=null && Integer.parseInt(request.getParameter("cTn"))<=12){
									repayments = SbiUtil.generateRepaymentListGLBullet(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), 
												irD, emiD,1,1,moratoriumMonths,1,product.getPLProductSliderAmtMul(),quote.getLoanQuoteHasPropertyRented(),quote.getLoanQuoteMonthStartDateOflease(),quote.getLoanQuoteYearStartDateOflease());
								}else {
									repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), 
											irD, emiD,1,1,moratoriumMonths,1,product.getPLProductSliderAmtMul(),quote.getLoanQuoteHasPropertyRented(),quote.getLoanQuoteMonthStartDateOflease(),quote.getLoanQuoteYearStartDateOflease());
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
					logger.info("PersonalLoanAction.java LN 1000 stateManagerBean.getState()==8 ::" + e.getMessage());
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
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						if(appSeqId == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						appForm=personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
						if(appForm == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						}
						if(otherRequest.getEmail()!=null){
							appForm.setAppWorkEmail(otherRequest.getEmail());
							
							SessionUtil.setEmail(otherRequest.getEmail());
						}
							
 					    if(!ValidatorUtil.isValid(appForm.getAppEmailAttemptCount())){
							appForm.setAppEmailAttemptCount(0);
						}
						if(appForm.getAppEmailAttemptCount()>Constants.APP_MAXIMUM_EMAIL_ATTEMPT_COUNT){
							responseMessage = "error|"+Constants.APP_MAXIMUM_EMAIL_ATTEMPT_MSG;
							return "jsonResponsePage";
						}
						appForm.setAppEmailAttemptCount(appForm.getAppEmailAttemptCount()+1);
						logger.info("PersonalLoanAction.java LNo : 1495 app.getAppOTPAttemptCount() : "+appForm.getAppEmailAttemptCount()+" with AppSeqId "+appSeqId);
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
						appForm.setAppPersonalLoanId(Integer.parseInt(applyUrlData[0]));
						quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote.getLoanQuoteCheckOffType()!=null){
								appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
							}
						appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
						appForm.setAppLoanProcessingFee(Double.parseDouble(applyUrlData[3]));
						appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
						
						appForm = personalLoanService.save(appForm);
						responseMessage = "success|Quotes sent to email";
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 1090 stateManagerBean.getState()==6 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if(stateManagerBean.getState()==3 || stateManagerBean.getState()==4 || stateManagerBean.getState()==5){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						jsonJSArray = SbiUtil.populateJSValidation(stateManagerBean.getState(), moduleId).toString();
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
						if(stateManagerBean.getState()==5 || stateManagerBean.getState()==3){
							if (!Constants.PL_CAPTCHA_BY_PASS) {
								responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
								if (responseMessage != null) {
									return "jsonResponsePage";
								}
							}
						}
						json = processManagerPersonalImpl.processWantUsToCallYou(appSeqId, stateManagerBean.getState(), visitId, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,loanTypeId, otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							responseMessage = "success|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							if(stateManagerBean.getState() != 4) {
								logger.info("PersonalLoanAction.java LNo : 1662 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
								logger.info("PersonalLoanAction.java LNo : 1664 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
							}
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
							}
							if(stateManagerBean.getState() != 4) {
								logger.info("PersonalLoanAction.java LNo : 1672 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
								responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
								logger.info("PersonalLoanAction.java LNo : 1674 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
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
					logger.info("PersonalLoanAction.java LN 1131 stateManagerBean.getState()==3, 4, 6 ::"+stateManagerBean.getState()+" :: " + e.getMessage());
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
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						}						
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
						appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
						if(appForm == null){
							if(stateManagerBean.getOriginalState()==9){
								stateManagerBean.setState(-1);
							}else{
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
								return "jsonResponsePage";
							}
						}
						if(request.getParameter("PLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
							if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
								stateManagerBean.setState(-1);
							}else{
								initPersonalLoan();
							}
							if(appForm!=null && !"Y".equalsIgnoreCase(appForm.getAppMobileVerified())){
								appForm.setAppMobileVerified("Y");									
							}
						}
						if(stateManagerBean.getState()==2){
							quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							if(quote == null){
								if(request.getParameter("PLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
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
				                  if (appForm.getAppAlternateMobileNumber() != null) {
				                    SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
				                    SessionUtil.setISDCode(appForm.getAppISDCode());
				                  }
								}else{
									if(appForm.getAppMobileNo()!=null){
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
										quote.setAppMobile(appForm.getAppMobileNo());
										quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
									}	
				                if (appForm.getAppAlternateMobileNumber() != null) {
				                    SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
				                    SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
				                  } 
				                } 
								quote.setLoanQuoteCityId(appForm.getAppCityId());
								quote.setLoanQuoteStateId(appForm.getAppStateId());
								quote.setLoanQuotePanCardLater(appForm.getAppPanCardLater()==null?true:appForm.getAppPanCardLater());
								quote.setLoanQuotePanCardNo(appForm.getAppPanCardNo());
								quote.setLoanQuoteOtherId(appForm.getAppOtherId());
								quote.setLoanQuoteOtherIdNumber(appForm.getAppOtherIdNumber());
								SessionUtil.setEmail(appForm.getAppWorkEmail());
								firstName=appForm.getAppFirstName() != null ?appForm.getAppFirstName() : "";
								SessionUtil.setApplicantName(firstName);
								quote.setAppEmail(appForm.getAppWorkEmail());
								Integer productSliderTenure = null;
								if(otherRequest!=null && otherRequest.getProductSliderTenure()!=null){
									productSliderTenure =Integer.parseInt(otherRequest.getProductSliderTenure().toString());
								}
								if(otherRequest!=null && otherRequest.getChosenTenure()!=null){
									chosenTenure = Integer.parseInt(otherRequest.getChosenTenure().toString());
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
								if(otherRequest!=null && otherRequest.getDiscountCouponName()!=null){
									quote.setLoanQuoteAppliedCoupon(otherRequest.getDiscountCouponName());
					    		}
								if(Constants.BANK_ID == Constants.BANK_ID_SBT || (Constants.BANK_ID == Constants.BANK_ID_SBI && quote.getLoanQuoteLoanAccountType()!=null)){
									if(otherRequest!=null && otherRequest.getChosenLoanAccountType()!=null && !("").equalsIgnoreCase(otherRequest.getChosenLoanAccountType())){

										if(request.getParameter("quote.loanQuoteTermODType")!=null){
											quote.setLoanQuoteTermODType(Integer.parseInt(request.getParameter("quote.loanQuoteTermODType")));
										}else if(otherRequest.getLoanQuoteTermODTypeMobile()!=null){
											quote.setLoanQuoteTermODType(Integer.parseInt(otherRequest.getLoanQuoteTermODTypeMobile()));
										}else{
											if(otherRequest.getChosenLoanAccountType() !=null && Integer.parseInt(otherRequest.getChosenLoanAccountType()) ==2){
												quote.setLoanQuoteTermODType(2);
											}else{
												quote.setLoanQuoteTermODType(1);
											}
										}
									}else{
										quote.setLoanQuoteTermODType(1);
									}
								}else{
									if(otherRequest!=null && otherRequest.getChosenLoanAccountType()!=null && !("").equals(otherRequest.getChosenLoanAccountType())){
										quote.setLoanQuoteLoanAccountType(Integer.parseInt(otherRequest.getChosenLoanAccountType()));
									}else{
										quote.setLoanQuoteLoanAccountType(1);
									}
								}
								
								if(otherRequest!=null && otherRequest.getChosenProductId()!=null && !("").equalsIgnoreCase(otherRequest.getChosenProductId())){
									quote.setLoanQuoteLoanProductId(Integer.parseInt(otherRequest.getChosenProductId()));
					    		}else{
					    			quote.setLoanQuoteLoanProductId(1);
					    		}

								loanScenarioBean=(LoanScenarioBean) processManagerPersonalImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

								if(loanScenarioBean.getApplicationPL()!=null){
									quote.setRequestStr(loanScenarioBean.getApplicationPL().getAppRequestData());
									quote.setResponseStr(loanScenarioBean.getApplicationPL().getAppOfferJsonData());
								}
								if(loanScenarioBean.getStatus() != 1){
									responseMessage = loanScenarioBean.getMessage();
									if(request.getParameter("PLQuoteToken")!=null){
										stateManagerBean.setState(-1);
									}else{
										if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
											responseMessage =  "error|"+loanScenarioBean.getMessage();
											return "jsonResponsePage";	
										}else{
											if(loanScenarioBean.getMessage()!=null){
												responseMessage =  "error|"+loanScenarioBean.getMessage();
												return "jsonResponsePage";
											}
											stateManagerBean.setState(0);
										}
									}
								}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(),  loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(), loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
								
									if(appSeqId==null){
										if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
											appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
										}										
									}
									if(appSeqId==null){
										responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
										return "jsonResponsePage";
									}
									appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
									quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
									if(quote.getLoanQuoteLoanPurposeId().intValue() != 27)
										personalLoanPage = 2;
									

									if(request.getParameter("PLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){

										if(appForm.getAppReferenceId()!=null){
											appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
											return "applicationTrack"+(uiType==null?"":uiType);
										}
										jsonJSArray1PersonalLoan = SbiUtil.populateJSValidation(1, moduleId).toString();

										populateForm(personalLoanPage, appSeqId);

										initPersonalLoan();
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
					logger.info("PersonalLoanAction.java LN 1277 stateManagerBean.getState()==2 ::" + e.getMessage());
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
						quote = stateManagerBean.getQuotePL();
						if (quote==null) {
							if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
								appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
								appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
								quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							}
						}
						
						if(quote!=null && quote.getInfoprovide()!=null && !quote.getInfoprovide().equals("on") 
								&& isDsrPage.equalsIgnoreCase("false") ){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(!Constants.PL_CAPTCHA_BY_PASS && (ajaxPostUrl.equalsIgnoreCase("personal-loan") || ajaxPostUrl.equalsIgnoreCase("gold-loan"))){	
							if(appSeqId==null){
								responseMessage = SbiUtil.checkCaptcha(quote.getCaptcha());
								if (responseMessage!=null) {
									return "jsonResponsePage";
								}
							}
						}
						responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
						logger.info("PersonalLoanAction.java LNo : 2073 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
						if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if(appForm!=null && appForm.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS)){
								if(appForm.getAppQuoteId() != null){
									ApplicationFormPersonalLoanQuote quoteOld = null;
									quoteOld = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
									quote.setLoanQuotePreEMIs(quoteOld.getLoanQuotePreEMIs());
									if(quoteOld.getLoanQuoteHaveSalaryAccountWithSbi() != null) {
										quote.setLoanQuoteHaveSalaryAccountWithSbi(quoteOld.getLoanQuoteHaveSalaryAccountWithSbi());
									} else if(quoteOld.getLoanQuoteHaveSalaryAccountWithSbi() == null) {
										quote.setLoanQuoteHaveSalaryAccountWithSbi("N");
									}
									if(quoteOld.getLoanQuoteLoanPurposeId()==11 || quoteOld.getLoanQuoteLoanPurposeId()==12){
										if(ValidatorUtil.isValid(quoteOld.getLoanQuoteExistingSanctionAmount())){
											quote.setLoanQuoteExistingSanctionAmount(quoteOld.getLoanQuoteExistingSanctionAmount());
										}
										if(quoteOld.getLoanQuoteExistingPLSanctionDT()!=null){
											quote.setLoanQuoteExistingPLSanctionDT((quoteOld.getLoanQuoteExistingPLSanctionDT()));
										}
										if(quoteOld.getLoanQuoteExistingXpressCreditNo()!=null && !quoteOld.getLoanQuoteExistingXpressCreditNo().toString().equals("-1")){
											quote.setLoanQuoteExistingXpressCreditNo(quoteOld.getLoanQuoteExistingXpressCreditNo());
										}
									}else{
										if(quote.getLoanQuoteLoanPurposeId()==Constants.PENSION_LOAN_PURPOSE_ID){
											if(ValidatorUtil.isValid(quoteOld.getLoanQuoteOutstandingLoanAmount())){
												quote.setLoanQuoteOutstandingLoanAmount(quoteOld.getLoanQuoteOutstandingLoanAmount());
											}
											if(quoteOld.getLoanQuoteExistingPLSanctionDT()!=null){
												quote.setLoanQuoteExistingPLSanctionDT((quoteOld.getLoanQuoteExistingPLSanctionDT()));
											}
										}
									}
									
									
									






								}
							}
						}
						
						if(quote.getLoanQuoteHaveSalaryAccountWithSbi() == null) {
							quote.setLoanQuoteHaveSalaryAccountWithSbi("N");
						}
						
						if(isDsrPage.equalsIgnoreCase("true")){
							if(quote.getLoanQuoteLoanPurposeId()==11){

							}
						}





						if(quote.getLoanQuoteDateOfBirth()!=null && quote.getLoanQuoteDateOfBirthDT() == null){
							quote.setLoanQuoteDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
						}
						if(quote.getLoanQuoteCoapplicantDateOfBirth()!=null){
							quote.setLoanQuoteCoapplicantDateOfBirthDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteCoapplicantDateOfBirth(), "MM/dd/yyyy"));
						}
						if(Constants.BANK_ID == Constants.BANK_ID_SBT || (Constants.BANK_ID == Constants.BANK_ID_SBI && quote.getLoanQuoteLoanAccountType()!=null)){
								quote.setLoanQuoteTermODType(1);
						}else if (quote.getLoanQuoteLoanPurposeId()==27) {
							quote.setLoanQuoteTermODType(1);
						}
						if(isDsrPage.equalsIgnoreCase("false") && quote.getLoanQuoteLoanPurposeId()==11 && (appForm ==null || "N".equals(appForm.getAppMobileVerified())) && quote.getLoanQuoteIndustryType()!=null 
								&& (quote.getLoanQuoteIndustryType()==1 ||quote.getLoanQuoteIndustryType()==15 
									|| quote.getLoanQuoteIndustryType()==16 || quote.getLoanQuoteIndustryType()==17)){
									JSONObject json=processManagerPersonalImpl.processGetQuotesElite(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);
										if(isDsrPage.equalsIgnoreCase("true") && json.get("status")=="success"){
											SessionUtil.setCaptch(null);
											return "secondPage"+(uiType==null?"":uiType);
										}
										if (json.get("status")=="success") {
											appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
											appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
											SessionUtil.setCaptch(null);
											return "otpPageElite"+(uiType==null?"":uiType);
										}else if (json.get("status")=="error") {
											responseMessage = "error|"+json.get("message");
											return "jsonResponsePage";
										}
								} else {
									
									if (quote.getLoanQuoteLoanPurposeId() != 27) {
										//added for alternate number on 14/11/2022
										String alternateMob = quote.getAlternateMobileNumber() != null ? quote.getAlternateMobileNumber() : "";
										String altIsd = quote.getAppAltISDCode() != null ? quote.getAppAltISDCode() : "";
										
										//added for server Side Validation of Apsec point 
										String firstNameData=quote.getLoanQuoteFirstName();
										String midldeNameData=quote.getLoanQuoteMiddleName();
										String lastNameData=quote.getLoanQuoteLastName();
										//String mobilenum=quote.getAppMobile();
										String mobilenum=quote.getAppMobile() != null ? quote.getAppMobile() : quote.getAppNRIMobileNo();
										
										if(isDsrPage.equalsIgnoreCase("true")) {
											String pattern = "^((?=[A-Za-z. ])(?![_\\\\-]).)*$";
											String patternMobile =  "^[6789][0-9]{4,9}$";
											
											if(firstNameData!= null && !firstNameData.matches(pattern)) {
												responseMessage = "error|First Name is not in correct format ";
												return "jsonResponsePage";
											}
											if(midldeNameData!= null && !midldeNameData.matches(pattern)) {
												responseMessage = "error|Middle Name is not in correct format ";
												return "jsonResponsePage";
											}
											if(lastNameData!= null && !lastNameData.matches(pattern)) {
												responseMessage = "error|Last Name is not in correct format ";
												return "jsonResponsePage";
											}
											
											if(!mobilenum.matches(patternMobile)) {
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
								}
									loanScenarioBean=(LoanScenarioBean) processManagerPersonalImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);
								}
						if(loanScenarioBean != null && loanScenarioBean.getApplicationPL()!=null) {
							quote.setRequestStr(loanScenarioBean.getApplicationPL().getAppRequestData());
							quote.setResponseStr(loanScenarioBean.getApplicationPL().getAppOfferJsonData());
						}
						
						
						if (quote.getLoanQuoteLoanPurposeId().intValue() != 27) {
							if(loanScenarioBean.getStatus() != 1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								if(loanScenarioBean.getApplicationPL()!=null){
									responseMessage += "|" + loanScenarioBean.getApplicationPL().getAppRequestData() + "|" + loanScenarioBean.getApplicationPL().getAppOfferJsonData();
								}
								return "jsonResponsePage";
							}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(), loanScenarioBean.getProductSliderDigitExact(), loanScenarioBean.getProductSliderAmtMul()/Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal = SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(), loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
								
								
								if(appSeqId==null){
									if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
										appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
									}
								}
								if(appSeqId==null){
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
									return "jsonResponsePage";
								}
								appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
								quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
								personalLoanPage=2;
								
								return "secondPage" + (uiType == null ? "" : uiType);
							}
						} else {
							
							
							if(loanScenarioBean.getStatus() != null && loanScenarioBean.getStatus() != 1){
								responseMessage = "error|"+loanScenarioBean.getMessage();
								if(loanScenarioBean.getApplicationPL()!=null){
									responseMessage += "|" + loanScenarioBean.getApplicationPL().getAppRequestData() + "|" + loanScenarioBean.getApplicationPL().getAppOfferJsonData();
								}
								return "jsonResponsePage";
							}
							if(appSeqId==null){
								if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
									appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
								}
							}
							if(appSeqId==null){
								responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
								return "jsonResponsePage";
							}
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
							personalLoanPage=2;
							logger.info("PersonalLoanAction.java LN 2143 isDsrPage value is : " + isDsrPage);
							if(quote.getLoanQuoteHaveSalaryAccountWithSbi().equals("Y") || (quote.getLoanQuoteHaveSalaryAccountWithSbi().equals("N") && isDsrPage.equals("true"))) {
								stateManagerBean.setState(17);
							}
							if(!quote.getLoanQuoteHaveSalaryAccountWithSbi().equals("Y") && isDsrPage.equals("false"))
								return "secondPage" + (uiType == null ? "" : uiType);
							}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (SQLException e) {
					logger.info("PersonalLoanAction.java LN 1331 stateManagerBean.getState()==1 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
	    
			
			logger.info("PersonalLoanAction.java LN 2283  ajaxPostUrl is " + ajaxPostUrl);
			if(SessionUtil.getGoldLoan() != null && (SessionUtil.getGoldLoan().trim().equals("27") || ajaxPostUrl.equalsIgnoreCase("gold-loan") || ajaxPostUrl.equalsIgnoreCase("personal-loan-dsr")) && stateManagerBean.getState()==17) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						logger.info("PersonalLoanAction.java LN 2288  stateManagerBean.getAppPL() is " + stateManagerBean.getAppPL());
						personalLoanPage = 4;
						//appForm = stateManagerBean.getAppPL();    commented for gold loan
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
						
						appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
						logger.info("PersonalLoanAction.java LN 2315 appSeqId is " + appSeqId);
						
						if (SessionUtil.getGoldLoan().trim().equals("27") || ajaxPostUrl.equalsIgnoreCase("gold-loan")) {
							SessionUtil.setPersonalGoldLoanSequenceId(appSeqId);
						}
						
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						
						appForm = processManagerPersonalImpl.processSubmitQuote(appSeqId, stateManagerBean.getState(), appForm, isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID));
						if(appForm==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(appForm.getError()!=null){
							responseMessage = "error|"+appForm.getError();
							return "jsonResponsePage";
						}
						
						//generateUIBeanList(personalLoanPage);
						if(jsonJSArrayThankyou==null){
							jsonJSArrayThankyou = SbiUtil.populateJSValidation(18, moduleId).toString();
						}
						SessionUtil.setGoldLoan("0");
						
						return "thankYouPage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("PersonalLoanAction.java LN 613 stateManagerBean.getState()==17 ::"+ e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if (stateManagerBean.getState()==0) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){

						personalLoanPage=1;
						if(SessionUtil.getApplicationType()==null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							if(SessionUtil.getPersonalLoanTypeSequenceId()!=null){
								appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							}						
							if(appSeqId != null){
								appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.PERSONAL_LOAN_ID){
										releaseSession(Constants.PERSONAL_LOAN_ID);
										appSeqId=null;
										lead=null;
									}
								}
							}
						}
						if(appSeqId==null){
							if(isDsrPage.equals("true") && SessionUtil.getPersonalLoanTypeSequenceId()!=null){
								appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							}else{
							appSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
							}
						}
						if(appSeqId !=null){
							appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
							if(appForm!=null){
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
					                if (appForm.getAppAlternateMobileNumber() != null) {
					                  alternateMobileNo = appForm.getAppAlternateMobileNumber();
					                  SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
					                  SessionUtil.setISDCode(appForm.getAppISDCode());
					                } else if (appForm.getAppAlternateMobileNumber() != null) {
					                  alternateMobileNo = appForm.getAppAlternateMobileNumber();
					                  SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
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
						}else{
							generateUIBeanList();
						}
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						
						personalLoanPage = 1;
						populateFirstPageContent(Constants.PERSONAL_LOAN_ID,1);	    
						
						jsonJSArray1PersonalLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						initPersonalLoan();
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
							getCallLogs(appSeqId, appLeadId);
						}
						if(SessionUtil.getApplicantName()!=null){
							firstName=SessionUtil.getApplicantName();
						}
						if(SessionUtil.getMobile()!=null){
							mobileNo=SessionUtil.getMobile();
						}
						if (SessionUtil.getalternateMobileNumber() != null) {
              				alternateMobileNo = SessionUtil.getalternateMobileNumber();
						}						
						if(SessionUtil.getEmail()!=null){
							email=SessionUtil.getEmail();
						}
						includeJs=true;
						initPersonalLoan();
						return "firstPage"+(uiType==null?"":uiType);
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
					
				} catch(NullPointerException e){
					stateManagerBean.setState(0);
					logger.info("PersonalLoanAction.java LN 1368 stateManagerBean.getState()==0 ::" + e.getMessage());
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
									if(moduleId==Constants.PERSONAL_LOAN_ID){
										releaseSession(Constants.PERSONAL_LOAN_ID);
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
											quote = null;
										}
									}else{
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
											releaseSession(Constants.PERSONAL_LOAN_ID);
											appSeqId=null;
											appForm = null;
											quote = null;
										}
									}
								}
							}
						}
						
						
						
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
									
								if (appForm.getAppAlternateMobileNumber() != null) {
				                    alternateMobileNo = appForm.getAppAlternateMobileNumber();
				                    SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
				                    SessionUtil.setISDCode(appForm.getAppISDCode());
				                  } 
									
								}else{
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									}
									
			                  
									if (appForm.getAppAlternateMobileNumber() != null) {
					                    alternateMobileNo = appForm.getAppAlternateMobileNumber();
					                    SessionUtil.setalternateMobileNumber(appForm.getAppAlternateMobileNumber());
					                    SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
									}
								}
								
								if(appForm.getAppWorkEmail()!=null){
									email = appForm.getAppWorkEmail();
									SessionUtil.setEmail(appForm.getAppWorkEmail());
								}
								
								if(appForm.getAppFirstName()!=null){
									firstName=appForm.getAppFirstName() != null?appForm.getAppFirstName() : "";
									SessionUtil.setApplicantName(firstName);
								}
								
								//mask PII data
								if(isDsrPage.equalsIgnoreCase("true")) {
									
									boolean isTeleCallerUser = false;
									isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
									
									if (isTeleCallerUser) {
										if (appForm.getAppMobileNo() != null) {
											isMobileNoMask = "true";
											mobileNoMaskVal = appForm.getAppMobileNo().replaceAll("\\d(?=\\d{4})", "*");
										}
										if (appForm.getAppWorkEmail() != null) {
											isEmailMask = "true";
											emailMaskVal = appForm.getAppWorkEmail().replaceAll("(?<=.{2}).(?=[^@]*?.@)", "*");
										}
									
										
										
										
										if (appForm.getAppSalaryAccNo()!= null) {
											isSalaryAccnum="true";
										    salaryAccnumVal = appForm.getAppSalaryAccNo().replaceAll("\\d(?=\\d{4})", "*");
										}
										
										if (appForm.getAppPensionAccountNumber()!= null) {
											isPensionAccnum="true";
											pensionAccnumVal = appForm.getAppPensionAccountNumber().replaceAll("\\d(?=\\d{4})", "*");
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
										if (appForm.getAppOtherIdNumber()!= null) {
											isAppotherIdNum="true";
											appotherIdNumVal = appForm.getAppOtherIdNumber().replaceAll("[a-zA-Z0-9]", "*");
										}
									
										
									}
								}
								
							}else{
								appSeqId = null;
								SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
							}
							populateForm(personalLoanPage, appSeqId);  
						}
						generateUIBeanList();
						generateUIBeanList(1);
						if(SessionUtil.getLeadId()!=null){
							lead=commonService.getLeadById(SessionUtil.getLeadId());
						}
						personalLoanPage = 1;
						populateFirstPageContent(Constants.PERSONAL_LOAN_ID,1);

						jsonJSArray1PersonalLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						jsonJSArray1CBS = SbiUtil.populateJSValidation(27, moduleId).toString();
						initPersonalLoan();
						if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
							getCallLogs(appSeqId, appLeadId);
						}
						if(SessionUtil.getApplicantName()!=null){
							firstName=SessionUtil.getApplicantName();
						}
						if(SessionUtil.getMobile()!=null){
							mobileNo=SessionUtil.getMobile();
						}
			            if (SessionUtil.getalternateMobileNumber() != null) {
    			          alternateMobileNo = SessionUtil.getalternateMobileNumber(); 
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
					logger.info("PersonalLoanAction.java LN 1448 stateManagerBean.getState()==0 ::" + e.getMessage());
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}    
	      
	    } catch (NullPointerException e) {
		      logger.info("PersonalLoanAction.java LN 1327 stateManagerBean.getState()==-1 ::" + e.getMessage());
		} catch (JSONException e) {
	      logger.info("PersonalLoanAction.java LN 1328 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } catch (SQLException e) {
		  logger.info("PersonalLoanAction.java LN 1329 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } catch (ParseException e) {
			  logger.info("PersonalLoanAction.java LN 1330 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } catch (FileNotFoundException e) {
			  logger.info("PersonalLoanAction.java LN 1331 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } catch (RuntimeException e) {
			  logger.info("PersonalLoanAction.java LN 1332 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } catch (UnsupportedEncodingException e) {
			  logger.info("PersonalLoanAction.java LN 1333 stateManagerBean.getState()==-1 ::" + e.getMessage());
	    } 
	    return "homePage" + (( uiType == null) ? "" :  uiType);
	  }
	  
	  private void populateForm(Integer pageNo, Integer appSeqId){
			try {
				if(appSeqId!=null){
					if(appForm!=null){
						quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
						logger.info("PersonalLoanAction populateForm .java LNo : 2348 " + appForm.getAppQuoteId());
						if(quote!=null){
							quote.setAppEmail(appForm.getAppWorkEmail());
							if(appForm.getAppApplyingFrom()==2){
								if(appForm.getAppMobileNo()!=null){
									quote.setAppNRIMobileNo(appForm.getAppMobileNo());
									quote.setAppISDCode(appForm.getAppISDCode());
								}
				              if (appForm.getAppAlternateMobileNumber() != null) {
				                quote.setAppNRIMobileNo(appForm.getAppMobileNo());
				                quote.setAppISDCode(appForm.getAppISDCode());
				              }
							}else{
								if(appForm.getAppMobileNo()!=null){
									quote.setAppMobile(appForm.getAppMobileNo());
									quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
								}
								
							if (appForm.getAppAlternateMobileNumber() != null) {
					        	quote.setAppMobile(appForm.getAppMobileNo());
					            quote.setAppISDCode(Constants.COUNTRY_CODE_INDIA);
					        }
								
								if(appForm.getAppFirstName() !=null){
									quote.setLoanQuoteFirstName(appForm.getAppFirstName());
								}
							
								if(appForm.getAppMiddleName()!=null){
									quote.setLoanQuoteMiddleName(appForm.getAppMiddleName());
								}
								if(appForm.getAppLastName()!=null){
									quote.setLoanQuoteLastName(appForm.getAppLastName());
								}
								
								if(appForm.getAppGender()!=null){
									quote.setLoanQuoteGender(appForm.getAppGender());
								}
								if(appForm.getAppAddress1()!=null){
									quote.setLoanQuoteAddress1(appForm.getAppAddress1());
								}
								
								if(appForm.getAppAddress2()!=null){
									quote.setLoanQuoteAddress2(appForm.getAppAddress2());
								}
								if(appForm.getAppAddressLandmark()!=null){
									quote.setLoanQuoteAddressLandmark(appForm.getAppAddressLandmark());
								}
								if(appForm.getAppPincode()!=null){
									quote.setLoanQuotePincode(appForm.getAppPincode());
								}
								if(appForm.getAppStateId()!=null){
									quote.setLoanQuoteStateId(appForm.getAppStateId());
								}
								if(appForm.getAppCityId()!=null){
									quote.setLoanQuoteCityId(appForm.getAppCityId());
								}
								if(appForm.getAppDistrictId()!=null){
									quote.setLoanQuoteCityId(appForm.getAppDistrictId());
								}
								if(appForm.getAppOfficeStateId()!=null){
									quote.setLoanQuoteWorkStateId(appForm.getAppOfficeStateId());
								}
								if(appForm.getAppIndustryTypeId()!=null){
									quote.setLoanQuoteIndustryType(appForm.getAppIndustryTypeId());
								}
								if(appForm.getAppPanCardLater()!=null){
									quote.setLoanQuotePanCardLater(appForm.getAppPanCardLater());
								}
							
								if(appForm.getAppPanCardNo()!=null){
									quote.setLoanQuotePanCardNo(appForm.getAppPanCardNo());
								}
								if(appForm.getAppOtherId()!=null){
									quote.setLoanQuoteOtherIdNumber(appForm.getAppOtherIdNumber());
								}
							}
							generateUIBeanList();
						}else{
							appSeqId=null;
							SessionUtil.setPersonalLoanTypeSequenceId(null);
						}
					}else{
						generateUIBeanList();
					}
				}else{
					generateUIBeanList();
				}
			} catch (NullPointerException e) {
				logger.info("PersonalLoanAction.java LN 1383 populateForm ::"+ e.getMessage());
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java LN 1383 populateForm ::"+ e.getMessage());
			}
		}

		public void initPersonalLoan() {
			try {

					initLoanJSONArrayPersonalLoan = new JSONArray();
					JSONObject json1 = new JSONObject();
					json1.put("preferredStateData",SbiUtil.getAllStateId(Constants.PERSONAL_LOAN_ID, null, null, null, null, null));
					initLoanJSONArrayPersonalLoan.put(json1);
					
					JSONObject json2 = new JSONObject();
					JSONArray industryTypes = SbiUtil.getIndustryTypeByLoanId(Constants.PERSONAL_LOAN_ID);
					json2.put("industryTypeid",industryTypes );
					initLoanJSONArrayPersonalLoan.put(json2);

					JSONArray industryTypeSNOTSARAL = new JSONArray(industryTypes.toString());
					JSONObject jsonOther = new JSONObject();
					jsonOther.put("key", Constants.OTHER_ID);
					jsonOther.put("value", Constants.OTHER_VALUE);
					jsonOther.put("defaultDisplay", 0);
					industryTypeSNOTSARAL.put(jsonOther);
					JSONObject json3 = new JSONObject();
					json3.put("industryTypeDataNOTSARAL", industryTypeSNOTSARAL);
					initLoanJSONArrayPersonalLoan.put(json3);
					
					JSONObject json4 = new JSONObject();
					json4.put("employmentTypeData1", SbiUtil.getEmploymentTypeByLoanType( Constants.PERSONAL_LOAN_ID, "N") );
					initLoanJSONArrayPersonalLoan.put(json4);
					
					JSONObject json5 = new JSONObject();
					json5.put("employmentTypeData2", SbiUtil.getEmploymentTypeByLoanType( Constants.PERSONAL_LOAN_ID, "Y") );
					initLoanJSONArrayPersonalLoan.put(json5);
					
					if(Constants.BANK_ID == Constants.BANK_ID_SBI){
						JSONObject json6 = new JSONObject();
						json6.put("residentCountries", SbiUtil.getAllCountries());
						initLoanJSONArrayPersonalLoan.put(json6);					
					}
					
					JSONObject json7 = new JSONObject();
					json7.put("profession", SbiUtil.getAllProfession( Constants.PERSONAL_LOAN_ID));
					initLoanJSONArrayPersonalLoan.put(json7);
					
					JSONObject json8 = new JSONObject();
					json8.put("loanPurposeLinks", SbiUtil.getAllLoanPurposeLinks());
					initLoanJSONArrayPersonalLoan.put(json8);
					JSONObject json10 = new JSONObject();
					json10.put("relationShipIndia", SbiUtil.getAllCoApplicantByLoanId(Constants.PERSONAL_LOAN_ID,0));
					initLoanJSONArrayPersonalLoan.put(json10);
					
					JSONObject json11 = new JSONObject();
					json1.put("xpressCreditITStateData",SbiUtil.getAllStateId(Constants.PERSONAL_LOAN_ID, null, null, null, null, "Y"));
					initLoanJSONArrayPersonalLoan.put(json11);
					
			} catch (JSONException e) {
				logger.info("PersonalLoanAction.java LN 1383 initPersonalLoan() ::"+ e.getMessage());
				initLoanJSONArrayPersonalLoan.put("error");
			}
		}

		private void getCallLogs(Integer appSeqId, Integer appLeadId) {
			try {
				if (ValidatorUtil.isValid(appSeqId) || ValidatorUtil.isValid(appLeadId)) {
					callLogDetails = commonService.getCallLogByLeadAppId(Constants.PERSONAL_LOAN_ID, appSeqId, appLeadId);
				}
			} catch (NullPointerException e) {
				logger.info("PersonalLoanAction.java :: getCallLogs() " + e.getMessage());
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java :: getCallLogs() " + e.getMessage());
			}
		}

		private void generateUIBeanList(int pageNo) {
			try {
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				if (pageNo == 3) {
					if (quote != null) {
						if(appForm!=null && appForm.getAppCoapplicantStateId()!=null && appForm.getAppCoapplicantCityId()!=null && appForm.getAppCoapplicantCityId() >0){
							Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppCoapplicantStateId(), null, null, null, null, null, null, null);
							Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppCoapplicantStateId(), null, null, null, null, null, null, null);
							if (mapDistrict != null && !mapDistrict.isEmpty()) {
								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1Coapplicant1(mapCity);
							if (appForm.getAppCoapplicantCityId().equals(Constants.OTHER_ID_INTEGER)) {
								if(appForm.getAppCoapplicantDistrictId()!=null && appForm.getAppCoapplicantDistrictId()>0){
									beanList.setDistrictsCoapplicant1(mapDistrict);
								}
							}
						}


						if(appForm!=null && appForm.getAppPermanentStateId()!=null && appForm.getAppPermanentCityId()!=null && appForm.getAppPermanentCityId() >0){
							Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
							Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppPermanentStateId(), null, null, null, null, null, null, null);
							if (mapDistrict != null && !mapDistrict.isEmpty()) {
								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1Permanent(mapCity);
							if (appForm.getAppPermanentCityId().equals(Constants.OTHER_ID_INTEGER)) {
								if(appForm.getAppPermanentDistrictId()!=null && appForm.getAppPermanentDistrictId()>0){
									beanList.setDistrictsPermanent(mapDistrict);
								}
							}
						}

						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						for (int index = 1; index <= 20; index++) {
							maps.put(index, String.valueOf(index));
						}
						beanList.setDependents(maps);

						List <MasterRelationshipWithBank>relationshipWithBanks = commonService.getAllRelationshipWithBank(Constants.PERSONAL_LOAN_ID);
						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						if (relationshipWithBanks != null) {
							for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) {
								maps.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
							}
						}
						beanList.setRelationshipWithBank(maps);

						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1){
							if (quote != null && quote.getLoanQuoteLoanPurposeId() == 13) {
								maps.put(15, "Salaried");
								maps.put(16, "Self Employed Professional");
								maps.put(17, "Self Employed Business");
								maps.put(18, "Agriculturist");
								maps.put(19, "Retired(Pensioner)");
							} else if (quote != null && quote.getLoanQuoteLoanPurposeId() == 12) {
								maps.put(15, "Salaried");
								maps.put(16, "Self Employed Professional");
								maps.put(17, "Self Employed Business");
							} else if (quote != null && quote.getLoanQuoteLoanPurposeId() == 11) {
								maps.put(15, "Salaried");

							}
						}else if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
							if (quote != null && quote.getLoanQuoteLoanPurposeId() == 13) {
								maps.put(15, "Salaried");
								maps.put(16, "Professional");
								maps.put(17, "Business");
								maps.put(18, "Agriculturist");
								maps.put(19, "Retired(Pensioner)");
							} else if (quote != null && quote.getLoanQuoteLoanPurposeId() == 12) {
								maps.put(15, "Salaried");
								maps.put(16, "Professional");
								maps.put(17, "Business");
							}
						}

						beanList.setEmployementTypesCoapplicants(maps);

						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 11) {
							List<MasterEmploymentType> employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.PERSONAL_LOAN_ID);
							for (MasterEmploymentType employmentType : employmentTypes) {
								maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
							}
						} else if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 12) {
							if (quote.getLoanQuoteResidentTypeId() == 1) {
								maps.put(15, "Salaried:Salary A/C with " + Constants.BANK_NAME);
								maps.put(20, "Salaried:Salary A/C with Other Bank");
								maps.put(16, "Self employed professional");
								maps.put(17, "Businessman");
								maps.put(18, "Agriculturist");
							} else if (quote.getLoanQuoteResidentTypeId() == 2) {
								maps.put(15, "Salaried");
								maps.put(16, "Professional");
								maps.put(17, "Businessman");
							}
						} else if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 13) {
							maps.put(15, "Salaried");
							maps.put(16, "Self Employed Professional");
							maps.put(17, "Self Employed Business");
							maps.put(18, "Agriculturist");
							maps.put(19, "Retired(Pensioner)");
						}
						beanList.setEmployementTypes(maps);

						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						List <MasterIndustryType> industryTypes= commonService.getIndustryTypeByLoanId(Constants.PERSONAL_LOAN_ID);
						for (MasterIndustryType industryType : industryTypes) {
							maps.put(industryType.getIndustryTypeId(), industryType.getIndustryName());
						}
						if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 11) {


						} else if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==12 || quote.getLoanQuoteLoanPurposeId()==13){
							maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setIndustryTypeData(maps);
						if (Constants.BANK_ID == Constants.BANK_ID_SBP) {
							if (quote != null && quote.getLoanQuoteCoapplicantIndustryType() != null) {
								beanList.setIndustryTypeDataCoapplicant1(maps);
							}
						}

						LinkedHashMap<Integer, String> yearsL = new LinkedHashMap<Integer, String>();
						int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						for (int index = 0; index < 65; currentYear--) {
							index++;
							yearsL.put(currentYear, String.valueOf(currentYear));
						}
						beanList.setBusinessYears(yearsL);

						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						int currentMonths = DateUtil.getCurrentMonth() - 1;
						currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						if (quote.getLoanQuoteYearCompanyJoining() != null) {
							if (currentYear != quote.getLoanQuoteYearCompanyJoining()) {
								currentMonths = 12;
							}
						}
						if (quote.getLoanQuoteYearStartDateOfCurrentProfession() != null) {
							if (currentYear != quote.getLoanQuoteYearStartDateOfCurrentProfession()) {
								currentMonths = 12;
							}
						}
						for (int index = 0; index < currentMonths; index++) {
							maps.put(index + 1, Constants.month[index]);
						}
						beanList.setBusinessMonths(maps);
					}
					if (appForm != null && appForm.getAppCompanyJoiningYear() != null) {
						int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						if ((currentYear - 7) >= appForm.getAppCompanyJoiningYear()) {
							appForm.setAppCompanyJoiningYear((currentYear - 7));
							appForm.setAppCompanyJoiningMonth(13);
						}
					}

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==13){
						maps.put(2, "PDC");
						maps.put(3, "ECS");
						maps.put(4, "Standing Instruction");
					}else if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==12){
						maps.put(1, "Check-off");

						maps.put(6, "SI linked to Salary Account");
						maps.put(7, "SI linked to Other Account");
						maps.put(8, "Other");
					}else if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==11 || quote.getLoanQuoteLoanPurposeId()==27)){
						if (quote.getLoanQuoteResidentTypeId() == 1) {
							maps.put(1, "Check-off");
							maps.put(2, "PDC");

							maps.put(4, "Standing Instruction");
						} else if (quote.getLoanQuoteResidentTypeId() == 2) {
							maps.put(1, "Check-off");
							maps.put(2, "PDC");
							maps.put(4, "Standing Instruction");
						}
					}else if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId().equals(Constants.PENSION_LOAN_PURPOSE_ID)){
						maps.put(4, "Standing Instruction");
					}
					if (Constants.BANK_ID == Constants.BANK_ID_SBBJ || Constants.BANK_ID == Constants.BANK_ID_SBP) {
						maps.put(5, "CASH");
					}
					beanList.setModeOfRepayment(maps);

				} else if (pageNo == 4) {
					if (appForm != null && appForm.getAppPickupStateId() != null && appForm.getAppPickupStateId() > 0) {
						Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
						Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
						if (mapDistrict != null && !mapDistrict.isEmpty()) {
							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setCitiesoptgrp1Pickup(mapCity);
						if(appForm!=null && appForm.getAppPickupCityId()!=null && appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictPickup(mapDistrict);
						}
					}
				}

				if (pageNo == 3 || pageNo == 4) {

					if(appForm!=null && appForm.getAppOfficeStateId()!=null && appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId() >0){
						Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
						Map<Integer, String> xpressCreditITmapCity = new LinkedHashMap<Integer, String>();
						xpressCreditITmapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, "Y");
						Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppOfficeStateId(), null, null, null, null, null, null, null);
						if (mapDistrict != null && !mapDistrict.isEmpty()) {
							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setCitiesoptgrp1Office(mapCity);
						beanList.setXpressCreditITcitiesgrp1(xpressCreditITmapCity);

						if (pageNo == 3) {
							if (appForm != null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)) {
								if (appForm.getAppOfficeDistrictId() != null && appForm.getAppOfficeDistrictId() > 0) {
									beanList.setDistrictsOffice(mapDistrict);
									if (appForm.getAppOfficeBranchId() != null && appForm.getAppOfficeBranchId() > 0) {
										beanList.setBranchesOffice(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, appForm.getAppOfficeStateId(), null, appForm.getAppOfficeDistrictId(), null, null, null, null, null));
									}
								}
							} else {
								if(appForm!=null && appForm.getAppOfficeBranchId()!=null && appForm.getAppOfficeBranchId()>0){
									beanList.setBranchesOffice(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, null, appForm.getAppOfficeCityId(), null, null, null, null, null, null));
								}
							}
						} else if (pageNo == 4) {
								if(appForm!=null && appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)){
								beanList.setDistrictsOffice(mapDistrict);
							}
						}
					}


					if(appForm!=null && appForm.getAppStateId()!=null && appForm.getAppCityId()!=null && appForm.getAppCityId() > 0){
						Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
						Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
						if (mapDistrict != null && !mapDistrict.isEmpty()) {
							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);

						}
						beanList.setCitiesoptgrp1(mapCity);

						if (pageNo == 3) {
							if (appForm != null && appForm.getAppCityId().equals(Constants.OTHER_ID_INTEGER)) {
								if (appForm.getAppDistrictId() != null && appForm.getAppDistrictId() > 0) {
									beanList.setDistricts(mapDistrict);
								}
								if (appForm.getAppBranchId() != null && appForm.getAppBranchId() > 0) {
									beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, appForm.getAppStateId(), null, appForm.getAppDistrictId(), null, null, null, null, null));
								}
							} else {
								if (appForm != null && appForm.getAppBranchId() != null && appForm.getAppBranchId() > 0) {
									beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, null, appForm.getAppCityId(), null, null, null, null, null, null));
								}
							}
						} else if (pageNo == 4) {
							if (appForm != null && appForm.getAppDistrictId() != null && appForm.getAppDistrictId() > 0) {
								beanList.setDistricts(mapDistrict);
							}
						}
					}
				}
				

				//customer consent				
				if (!appPLTypeId.equals(27)) {

					//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.PERSONAL_LOAN_ID, "ETB").getConsentText();
					Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.PERSONAL_LOAN_ID, "ETB").getConsentId();
					String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
					beanList.setConsentPersonalLoanEtb(consentTextEtb);
					
					//String consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.PERSONAL_LOAN_ID, "NTB").getConsentText();
					Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.PERSONAL_LOAN_ID, "NTB").getConsentId();
					String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
					beanList.setConsentPersonalLoanNtb(consentTextNtb);
				} else {
					//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.APP_PL_TYPE_GOLD, "ETB").getConsentText();
					Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.APP_PL_TYPE_GOLD, "ETB").getConsentId();
					String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
					beanList.setConsentGoldLoanEtb(consentTextEtb);
					
					//String consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.APP_PL_TYPE_GOLD, "NTB").getConsentText();
					Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.APP_PL_TYPE_GOLD, "NTB").getConsentId();
					String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
					beanList.setConsentGoldLoanNtb(consentTextNtb);
				}
				
								
			} catch (NullPointerException e) {
				logger.info("PersonalLoanAction.java LN 2682 generateUIBeanList() ::" + e.getMessage());
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java LN 2682 generateUIBeanList() ::" + e.getMessage());
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
					if (quote != null) {
						if(purpose.getLpId()!=null && quote.getLoanQuoteLoanPurposeId()!=null && purpose.getLpId().equals(quote.getLoanQuoteLoanPurposeId())){
							loanPurposeUrl = purpose.getLpUrl();
						}
					}
					if (isDsrPage == "false" && loanPurposeUrl == null) {
						MasterLoanPurpose loanPurpose = null;
						if (appPLTypeId == Constants.APP_PL_TYPE_GOLD) {
							loanPurpose = commonService.getLoanPurposeById(27);
						} else if (appPLTypeId == Constants.APP_PL_TYPE_PENSION) {
							loanPurpose = commonService.getLoanPurposeById(23);
						} else if (appPLTypeId == Constants.APP_PL_TYPE_PERSONAL) {
							loanPurpose = commonService.getLoanPurposeById(12);
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
						if (quote != null) {
							quote.setLoanQuoteLoanPurposeId(27);
						}
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
				int currentMonths2 = 0;
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
				currentMonths2 = DateUtil.getCurrentMonth();
				if(quote!=null && quote.getLoanQuoteCurrentContractStartYear()!=null && quote.getLoanQuoteCurrentContractStartYear().equals(currentYear2)){
					currentMonths2 = 12;
					for (int index = 0; index < currentMonths2; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
				} else {
					for (int index = 0; index < currentMonths2; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
				}
				beanList.setContractStartMonth(maps);
				yearsmap = new TreeMap<Integer, String>();
				currentYear2 = Integer.parseInt(DateUtil.getCurrentYear());
				for (int index = 0; index < 5; currentYear2++) {
					index++;
					yearsmap.put(currentYear2, String.valueOf(currentYear2));
				}
				beanList.setContractEndYear(yearsmap);
				if(quote!=null && quote.getLoanQuoteCurrentContractStartYear()!=null && quote.getLoanQuoteCurrentContractEndYear().equals(currentYear2)){
					currentMonths2 = 12;
					for (int index = currentMonths2; index < 12; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
				} else {
					for (int index = currentMonths2; index < 12; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
				}
				beanList.setContractEndMonth(maps);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				if(quote!=null  && quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==11 || quote.getLoanQuoteLoanPurposeId()==27)){
					maps.put(1, "Resident Indian");
				} else {
					maps.put(1, "Resident Indian");
					maps.put(2, "NRI");
				}
				beanList.setResidentTypes(maps);

				if (quote != null && quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 11) {
					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					if (appForm != null && appForm.getAppSubTypeId() != null && appForm.getAppSubTypeId().intValue() == 2) {
						List<MasterEmploymentType> employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.PERSONAL_LOAN_ID);
						for (MasterEmploymentType employmentType : employmentTypes) {
							maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
						}
					}
				} else {
					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					if (appForm != null && appForm.getAppSubTypeId() != null && appForm.getAppSubTypeId().intValue() == 2) {
						if (ValidatorUtil.isValid(appForm.getAppApplyingFrom()) && appForm.getAppApplyingFrom() == 1) {
							maps.put(15, "Salaried");

							maps.put(17, "Self employed businessman");
							maps.put(16, "Self employed professional");

						} else {
							maps.put(15, "Salaried");
							maps.put(17, "Self employed businessman");
							maps.put(16, "Self employed professional");
						}
					} else {
						if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==1){


							maps.put(15, "Salaried");

							maps.put(17, "Self employed businessman");
							maps.put(16, "Self employed professional");




						}else if(quote!=null && quote.getLoanQuoteResidentTypeId()!=null && quote.getLoanQuoteResidentTypeId()==2){
							maps.put(15, "Salaried");
							maps.put(16, "Professional");
							maps.put(17, "Businessman");
						} else {
							maps.put(15, "Salaried");
							maps.put(17, "Self employed businessman");
							maps.put(16, "Self employed professional");
						}
					}
					beanList.setEmployementTypes(maps);
				}
				Map<Integer, String> mapPensionVariants = new LinkedHashMap<Integer, String>();
				mapPensionVariants.put(6, "Regular Pension");
				mapPensionVariants.put(7, "Jai Jawan Pension");
				mapPensionVariants.put(8, "SBI Employees");
				mapPensionVariants.put(9, "Family Pension");
				beanList.setPensionLoanTypes(mapPensionVariants);

				Map<Integer, String> stateMap = new LinkedHashMap<Integer, String>();
				stateMap = commonService.getStateCityDistrictBranch(1, null, null, null, null, null, null, null, null, null);
				beanList.setPensionPayingState(stateMap);

				if(appForm!=null && quote!=null && appForm.getAppPensionPayingStateId()!=null && appForm.getAppCityId()!=null && appForm.getAppCityId() > 0){
					Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3,
							Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(),
							null, null, null, null, null, null, null);
					if (mapDistrict != null && !mapDistrict.isEmpty()) {
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setPensionPayingCity(mapCity);
				}

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

				if(quote!=null  && appForm.getAppPensionPayingCityId()!=null && appForm.getAppPensionPayingCityId().equals(Constants.OTHER_ID_INTEGER) && appForm.getAppPensionPayingStateId()!=null){
						Map<Integer, String> mapPensionDistrict = new LinkedHashMap<Integer, String>();
					mapPensionDistrict = commonService.getStateCityDistrictBranch(3,
							Constants.PERSONAL_LOAN_ID,
							appForm.getAppPensionPayingStateId(), null, null, null,
							null, null, null, null);
						beanList.setPensionDistricts(mapPensionDistrict);
						if (appForm.getAppPensionPayingBranchId() != null) {
							Map<Integer, String> mapPensionBranches = new LinkedHashMap<Integer, String>();
						mapPensionBranches = commonService.getStateCityDistrictBranch(4,
								Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(),
								null, appForm.getAppPensionPayingDistrId(), null, null, null, null, null);
							beanList.setPensionPayingBranches(mapPensionBranches);
						}
					} else {
					if(quote!=null  && appForm!=null && appForm.getAppPensionPayingStateId()!=null && appForm.getAppPensionPayingBranchId()!=null){
							Map<Integer, String> mapPensionBranches = new LinkedHashMap<Integer, String>();
						mapPensionBranches = commonService.getStateCityDistrictBranch(4,
								Constants.PERSONAL_LOAN_ID, appForm.getAppPensionPayingStateId(),
								appForm.getAppPensionPayingCityId(), null, null, null, null, null, null);
							beanList.setPensionPayingBranches(mapPensionBranches);
						}
					}

				if(quote!=null  && appForm!=null && appForm.getAppPreferredCityId()!=null && appForm.getAppPreferredStateId()!=null && appForm.getAppPreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
						Map<Integer, String> mapPreferredDistrict = new LinkedHashMap<Integer, String>();
					mapPreferredDistrict = commonService.getStateCityDistrictBranch(3,
							Constants.PERSONAL_LOAN_ID,
							appForm.getAppPreferredStateId(), null, null, null,
							null, null, null, null);
						if (mapPreferredDistrict != null && !mapPreferredDistrict.isEmpty()) {

						}
						beanList.setPreferredDistricts(mapPreferredDistrict);
						if (appForm.getAppPreferredLoanBranchId() != null) {
							Map<Integer, String> mapPreferredLoanBranches = new LinkedHashMap<Integer, String>();
							mapPreferredLoanBranches = commonService.getStateCityDistrictBranch(4,
								Constants.PERSONAL_LOAN_ID, appForm.getAppPreferredStateId(),
								null, appForm.getAppPreferredDistrictId(), null, null, null, null, null);
							beanList.setPreferredPayingBranches(mapPreferredLoanBranches);
						}
					} else {
					if(quote!=null  && appForm.getAppPreferredLoanBranchId()!=null && appForm.getAppPreferredStateId()!=null){
							Map<Integer, String> mapPreferredLoanBranches = new LinkedHashMap<Integer, String>();
							mapPreferredLoanBranches = commonService.getStateCityDistrictBranch(4,
									Constants.PERSONAL_LOAN_ID, appForm.getAppPreferredStateId(),
									appForm.getAppPreferredCityId(), null, null, null, null, null, null);
							beanList.setPreferredPayingBranches(mapPreferredLoanBranches);
						}
					}
				}
				int currentYear = 0;
				int currentMonths = 0;

				NavigableMap<Integer, String> nmap = null;
				TreeMap<Integer, String> years = new TreeMap<Integer, String>();
				currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					if(quote!=null && quote.getLoanQuoteHasPropertyRented()!=null && quote.getLoanQuoteHasPropertyRented().equalsIgnoreCase("Y")){
					for (int index = 0; index < 11; currentYear--) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}

					nmap = years.descendingMap();
					beanList.setYearsLeaseStart(nmap);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					currentMonths = DateUtil.getCurrentMonth();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						if(quote.getLoanQuoteYearStartDateOflease()!=null && currentYear != quote.getLoanQuoteYearStartDateOflease()){
						currentMonths = 12;
					}
					for (int index = 0; index < currentMonths; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setMonthsLeaseStart(maps);
						if(quote.getLoanQuoteYearStartDateOflease()!=null && quote.getLoanQuoteYearStartDateOflease().toString().equalsIgnoreCase(years.firstKey().toString()) ){
						quote.setLoanQuoteMonthStartDateOflease(0);
					}

					years = new TreeMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index = 0; index < 20; currentYear++) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}
					years.put(currentYear, String.valueOf(currentYear));
					beanList.setYearsLeaseEnd(years);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					currentMonths = DateUtil.getCurrentMonth();
						if(quote.getLoanQuoteYearEndDateOfLease()!=null && currentYear != quote.getLoanQuoteYearEndDateOfLease()){
						currentMonths = 12;
					}
					for (int index = 0; index < currentMonths; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setMonthsLeaseEnd(maps);
						if(quote.getLoanQuoteYearEndDateOfLease()!=null && quote.getLoanQuoteYearEndDateOfLease().toString().equalsIgnoreCase(years.lastKey().toString()) ){
						quote.setLoanQuoteMonthEndDateOfLease(0);
					}
				} else {
					for (int index = 0; index < 11; currentYear--) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}

					nmap = years.descendingMap();
					beanList.setYearsLeaseStart(nmap);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					currentMonths = DateUtil.getCurrentMonth();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
						if(quote!=null && quote.getLoanQuoteYearStartDateOflease()!=null && currentYear != quote.getLoanQuoteYearStartDateOflease()){
						currentMonths = 12;
					}
					for (int index = 0; index < 12; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setMonthsLeaseStart(maps);
						if(quote!=null && quote.getLoanQuoteYearStartDateOflease()!=null && quote.getLoanQuoteYearStartDateOflease().toString().equalsIgnoreCase(years.firstKey().toString()) ){
						quote.setLoanQuoteMonthStartDateOflease(0);
					}

					years = new TreeMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index = 0; index < 20; currentYear++) {
						index++;
						years.put(currentYear, String.valueOf(currentYear));
					}
					years.put(currentYear, String.valueOf(currentYear));
					beanList.setYearsLeaseEnd(years);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					currentMonths = DateUtil.getCurrentMonth();
						if(quote!=null && quote.getLoanQuoteYearEndDateOfLease()!=null && currentYear != quote.getLoanQuoteYearEndDateOfLease()){
						currentMonths = 12;
					}
					for (int index = currentMonths; index < Constants.month.length; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setMonthsLeaseEnd(maps);
						if(quote!=null && quote.getLoanQuoteYearEndDateOfLease()!=null && quote.getLoanQuoteYearEndDateOfLease().toString().equalsIgnoreCase(years.lastKey().toString()) ){
						quote.setLoanQuoteMonthEndDateOfLease(0);
					}
				}

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()==23 && quote.getLoanQuotePensionType()!=null && quote.getLoanQuotePensionType()!=9){
					maps.put(1, "Spouse");
					maps.put(2, "Sibling");
					maps.put(3, "Parents");
					maps.put(4, "Children");
					maps.put(5, "Other");
					}else if(quote!=null && quote.getLoanQuotePensionType()!=null && quote.getLoanQuotePensionType()==9){
					maps.put(2, "Sibling");
					maps.put(3, "Parents");
					maps.put(4, "Children");
					maps.put(5, "Other");
					}else if (quote!=null && quote.getLoanQuoteLoanPurposeId()==11 && quote.getLoanQuoteEmploymentNature()!=null && quote.getLoanQuoteEmploymentNature().equals(1)){
					maps.put(1, "Third Party Guarantor");
				} else {
					maps.put(1, "Spouse");
					maps.put(2, "Sibling");
					maps.put(3, "Parents");
					maps.put(4, "Children");
				}
				beanList.setRelationships(maps);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				maps.put(15, "Salaried");
				maps.put(16, "Self Employed Professional");
				maps.put(17, "Self Employed Business");
				maps.put(18, "Agriculturist");
				maps.put(19, "Pensioner (Retired)");
				maps.put(20, "Retired");
				maps.put(21, "Homemaker");
				beanList.setEmployementTypesCoapplicants(maps);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				maps.put(1, "Residential");
				maps.put(2, "Commercial");

				beanList.setPropertyTypes(maps);

				LinkedHashMap<Integer, String> bussyears = new LinkedHashMap<Integer, String>();
				int currYear = Integer.parseInt(DateUtil.getCurrentYear());
				for (int index = 0; index < 65; currYear--) {
					index++;
					bussyears.put(currYear, String.valueOf(currYear));
				}
				beanList.setBusinessYears(bussyears);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				int currMonths = DateUtil.getCurrentMonth() - 1;
				currentYear = Integer.parseInt(DateUtil.getCurrentYear());
				if (quote != null && quote.getLoanQuoteYearCompanyJoining() != null) {
					if (currentYear != quote.getLoanQuoteYearCompanyJoining()) {
						currMonths = 12;
					}
				}
				if (quote != null && quote.getLoanQuoteYearStartDateOfCurrentProfession() != null) {
					if (currentYear != quote.getLoanQuoteYearStartDateOfCurrentProfession()) {
						currMonths = 12;
					}
				}
				for (int index = 0; index < currMonths; index++) {
					maps.put(index + 1, Constants.month[index]);
				}
				beanList.setBusinessMonths(maps);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				List <MasterIndustryType> industryTypeList= commonService.getIndustryTypeByLoanId(Constants.PERSONAL_LOAN_ID);

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

				for (MasterIndustryType industryTypesList : industryTypeList) {
					maps.put(industryTypesList.getIndustryTypeId(), industryTypesList.getIndustryName());
				}
				if(cbs!=null && cbs.getCbsTypeOfRelationship()!=null && cbs.getCbsTypeOfRelationship().intValue()==3){
					if(quote!=null && quote.getLoanQuoteEmploymentNature()!=null && quote.getLoanQuoteEmploymentNature().intValue()==1){
						if (maps.containsKey(84)) {
							maps.remove(84);
						}
					} else {
						if (!maps.containsKey(84)) {
							maps.put(84, "Employees of IT company");
						}
					}
				} else {
					if (maps.containsKey(84)) {
						maps.remove(84);
					}
				}
				beanList.setIndustryTypeData(maps);

				if (quote != null) {

					maps = null;
					if (quote.getLoanQuoteEmploymentType() != null && quote.getLoanQuoteEmploymentType() > 0) {
						List<MasterEmployeeOccupationType> occTypes = commonService.getAllOccupationTypesByEmploymentType(quote.getLoanQuoteEmploymentType());
						maps = null;
						if (occTypes != null) {
							maps = new LinkedHashMap<Integer, String>();
							logger.info(" SessionUtil.getPersonalLoanCbsCallId()=="+SessionUtil.getPersonalLoanCbsCallId());
							for (MasterEmployeeOccupationType occType : occTypes) {
								if(SessionUtil.getPersonalLoanCbsCallId()==null && (occType!=null && occType.getOccupationId()==1)) 
								{
									continue;
								}
								else
									maps.put(occType.getOccupationId(), occType.getOccupation());
							}
							beanList.setOccupationTypes(maps);
						}
					} else {
						beanList.setOccupationTypes(maps);
					}


					
					
					if(quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==11 || quote.getLoanQuoteLoanPurposeId()==27)){

						
						if (quote != null && quote.getLoanQuoteWorkStateId() != null) {
							Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, quote.getLoanQuoteWorkStateId(), null, null, null, null, null, null, null);
							Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, quote.getLoanQuoteWorkStateId(), null, null, null, null, null, null, null);
							if (mapDistrict != null && !mapDistrict.isEmpty()) {

								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1WorkPlace(mapCity);

							if (quote != null && quote.getLoanQuoteWorkCityId() != null) {
								if (quote.getLoanQuoteWorkCityId().equals(Constants.OTHER_ID_INTEGER)) {
									if(quote.getLoanQuoteWorkDistrictId()!=null && quote.getLoanQuoteWorkDistrictId()>0){
										beanList.setDistrictsWorkPlace(mapDistrict);
										if(quote.getLoanQuoteWorkBranchId()!=null && quote.getLoanQuoteWorkBranchId()>0){
											beanList.setBranchesWorkPlace(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, quote.getLoanQuoteWorkStateId(), null, quote.getLoanQuoteWorkDistrictId(), null, null, null, null, null));
										}
									}
								} else {
									if(quote.getLoanQuoteWorkBranchId()!=null && quote.getLoanQuoteWorkBranchId()>0 && quote.getLoanQuoteWorkCityId()!=null){
										beanList.setBranchesWorkPlace(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuoteWorkCityId(), null, null, null, null, null, null));
									}
								}
							}
						}
					}

					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==12 || quote.getLoanQuoteLoanPurposeId()==13)){
						if (quote.getLoanQuotePropertyStateId() != null) {
							Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
							mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, quote.getLoanQuotePropertyStateId(), null, null, null, null, null, null, null);
							Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
							mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, quote.getLoanQuotePropertyStateId(), null, null, null, null, null, null, null);
							if (mapDistrict != null && !mapDistrict.isEmpty()) {

								mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
							}
							beanList.setCitiesoptgrp1PropertyPlace(mapCity);

							if (quote.getLoanQuotePropertyCityId() != null) {
								if (quote.getLoanQuotePropertyCityId().equals(Constants.OTHER_ID_INTEGER)) {
									if(quote.getLoanQuotePropertyDistrictId()!=null && quote.getLoanQuotePropertyDistrictId()>0){
										beanList.setDistrictsPropertyPlace(mapDistrict);
										if(quote.getLoanQuotePropertyBranchId()!=null && quote.getLoanQuotePropertyStateId()!=null && quote.getLoanQuotePropertyBranchId()>0){
											beanList.setBranchesPropertyPlace(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, quote.getLoanQuotePropertyStateId(), null, quote.getLoanQuotePropertyDistrictId(), null, null, null, null, null));
										}
									}
								} else {
									if(quote.getLoanQuotePropertyBranchId()!=null && quote.getLoanQuotePropertyBranchId()>0 && quote.getLoanQuotePropertyCityId()!=null){
										beanList.setBranchesPropertyPlace(commonService.getStateCityDistrictBranch(4, Constants.PERSONAL_LOAN_ID, null, quote.getLoanQuotePropertyCityId(), null, null, null, null, null, null));
									}
								}
							}
						}

					}

					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==11 || quote.getLoanQuoteLoanPurposeId()==27) && quote.getLoanQuoteIndustryType()!=null){
						List<MasterCorpSalaryPackage> salaryPackages=commonService.getAllSalaryPackageByLoanTypeAndIndustryTypeId(Constants.PERSONAL_LOAN_ID, quote.getLoanQuoteIndustryType());
						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						if (salaryPackages != null) {
							for (MasterCorpSalaryPackage salaryPackage : salaryPackages) {
								if (salaryPackage != null) {
									maps.put(salaryPackage.getCorpSalPackId(), salaryPackage.getCorpSalPackTitle());
								}
							}
						}
						beanList.setSalaryPackageData(maps);
					}

					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()!=13){
						if(quote.getLoanQuoteEmploymentType()!=null && (quote.getLoanQuoteEmploymentType()==15||quote.getLoanQuoteEmploymentType()==16 || quote.getLoanQuoteEmploymentType()==20) ){
							maps = null;
							maps = new LinkedHashMap<Integer, String>();
							List <MasterIndustryType> industryTypes= commonService.getIndustryTypeByLoanId(Constants.PERSONAL_LOAN_ID);

							for (MasterIndustryType industryType : industryTypes) {
								maps.put(industryType.getIndustryTypeId(), industryType.getIndustryName());
							}
							if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 11) {

								if(cbs!=null && cbs.getCbsTypeOfRelationship()!=null && cbs.getCbsTypeOfRelationship().intValue()==3){
									if(quote!=null && quote.getLoanQuoteEmploymentNature()!=null && quote.getLoanQuoteEmploymentNature().intValue()==1){
										if (maps.containsKey(84)) {
											maps.remove(84);
										}
									} else {
										if (!maps.containsKey(84)) {
											maps.put(84, "Employees of IT company");
										}
									}
								} else {
									if (maps.containsKey(84)) {
										maps.remove(84);
									}
								}


							} else if(quote.getLoanQuoteLoanPurposeId()!=null && (quote.getLoanQuoteLoanPurposeId()==12 || quote.getLoanQuoteLoanPurposeId()==13)){
								maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
								if (maps.containsKey(84)) {
									maps.remove(84);
								}
							}
							beanList.setIndustryTypeData(maps);
							if (Constants.BANK_ID == Constants.BANK_ID_SBP) {
								if (quote != null && quote.getLoanQuoteCoapplicantIndustryType() != null) {
									beanList.setIndustryTypeDataCoapplicant1(maps);
								}
							}

						}

						List<MasterProfession> professions = commonService.getAllProfession(Constants.PERSONAL_LOAN_ID);
						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						for (MasterProfession profession : professions) {
							maps.put(profession.getProfessionId(), profession.getProfessionTitle());
						}
						beanList.setProfessions(maps);
					}

					LinkedHashMap<Integer, String> yearsL = new LinkedHashMap<Integer, String>();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					for (int index = 0; index < 65; currentYear--) {
						index++;
						yearsL.put(currentYear, String.valueOf(currentYear));
					}
					beanList.setBusinessYears(yearsL);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					currentMonths = DateUtil.getCurrentMonth();
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					if (quote != null && quote.getLoanQuoteYearCompanyJoining() != null) {
						if (currentYear != quote.getLoanQuoteYearCompanyJoining()) {
							currentMonths = 12;
						}
					}
					if (quote != null && quote.getLoanQuoteYearStartDateOfCurrentProfession() != null) {
						if (currentYear != quote.getLoanQuoteYearStartDateOfCurrentProfession()) {
							currentMonths = 12;
						}
					}

					for (int index = 0; index < currentMonths; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setBusinessMonths(maps);
					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					if (quote != null && quote.getLoanQuoteYearStartDateOfCurrentProfession() != null
							&& quote.getLoanQuoteYearStartDateOfCurrentProfession() == (currentYear - 65 + 1)) {
						quote.setLoanQuoteMonthStartDateOflease(0);
					}
					if (quote != null && quote.getLoanQuoteYearCompanyJoining() != null
							&& quote.getLoanQuoteYearCompanyJoining() == (currentYear - 65 + 1)) {
						quote.setLoanQuoteMonthCompanyJoining(0);
					}

					currentYear = Integer.parseInt(DateUtil.getCurrentYear());
					currentMonths = DateUtil.getCurrentMonth();
					if (quote != null && quote.getLoanQuoteYearCoapplicantJoining() != null) {
						if (currentYear != quote.getLoanQuoteYearCoapplicantJoining()) {
							currentMonths = 12;
						}
					}
					if (quote != null && quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession() != null) {
						if (currentYear != quote.getLoanQuoteYearCoapplicantStartDateOfCurrentProfession()) {
							currentMonths = 12;
						}
					}

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					for (int index = 0; index < currentMonths; index++) {
						maps.put(index + 1, Constants.month[index]);
					}
					beanList.setBusinessMonthsCoapplicant(maps);

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==11){
						List<MasterEmploymentType> employmentTypes = commonService.getAllEmploymentTypeByLoanType(Constants.PERSONAL_LOAN_ID);
						for (MasterEmploymentType employmentType : employmentTypes) {
							maps.put(employmentType.getEmploymentTypeId(), employmentType.getEmploymentName());
						}
						beanList.setEmployementTypes(maps);
					}else if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==13){
						maps.put(15, "Salaried");
						maps.put(16, "Self Employed Professional");
						maps.put(17, "Self Employed Business");
						maps.put(18, "Agriculturist");
						maps.put(19, "Retired(Pensioner)");

						beanList.setEmployementTypes(maps);
					}

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==11){
						maps.put(15, "Salaried");


					}else if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==12){
						maps.put(15, "Salaried");
						maps.put(16, "Self Employed Professional");
						maps.put(17, "Self Employed Business");
						maps.put(18, "Agriculturist");
						maps.put(19, "Pensioner (Retired)");
						maps.put(20, "Retired");
						maps.put(21, "Homemaker");
					} else {
						if (quote != null && quote.getLoanQuoteCoapplicantResidentTypeId() != null) {
							if (quote.getLoanQuoteCoapplicantResidentTypeId() == 1) {
								maps.put(15, "Salaried");
								maps.put(16, "Self Employed Professional");
								maps.put(17, "Self Employed Business");
								maps.put(18, "Agriculturist");
								maps.put(19, "Pensioner (Retired)");
								maps.put(20, "Retired");
								maps.put(21, "Homemaker");

							} else if (quote.getLoanQuoteCoapplicantResidentTypeId() == 2) {
								maps.put(15, "Salaried");
								maps.put(16, "Self Employed Professional");
								maps.put(17, "Businessman");
							}
						}

					}

					beanList.setEmployementTypesCoapplicants(maps);

					if(quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==13){
						maps = null;
						maps = new LinkedHashMap<Integer, String>();
						List <MasterIndustryType> industryTypes= commonService.getIndustryTypeByLoanId(Constants.PERSONAL_LOAN_ID);
						for (MasterIndustryType industryType : industryTypes) {
							maps.put(industryType.getIndustryTypeId(), industryType.getIndustryName());
							if (maps.containsKey(84)) {
								maps.remove(84);
							}
						}
						maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						beanList.setIndustryTypeData(maps);
					}
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
				if (quote != null && quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId() == 23) {
					Map<Integer, String> map = new LinkedHashMap<>(4);
					if (quote.getLoanQuotePensionType() != null) {
						if (quote.getLoanQuotePensionType() == 6 || quote.getLoanQuotePensionType() == 9) {
							map.put(1, "Central Govt");
							map.put(2, "State Govt");
							map.put(3, "Public Sector Unit");
							map.put(4, "Other Organization");
						} else if (quote.getLoanQuotePensionType() == 7) {
							map.put(101, "Armed Forces");
							map.put(102, "Paramilitry Forces");
							map.put(103, "Other Armed Forces");
							map.put(104, "Other");
						} else if (quote.getLoanQuotePensionType() == 8) {
							map.put(201, "State Bank of India");
						}
						beanList.setIndustryType(map);
					}
					if (quote.getLoanQuoteIndustryType() != null) {
						Map<Integer, String> mapOrganisationName = new LinkedHashMap<>(4);
						if (quote.getLoanQuoteIndustryType() == 101) {
							mapOrganisationName.put(1, "Army");
							mapOrganisationName.put(2, "Navy");
							mapOrganisationName.put(3, "Air Force");
						} else if (quote.getLoanQuoteIndustryType() == 102) {
							mapOrganisationName.put(1, "Central Reserved Police Force (CRPF)");
							mapOrganisationName.put(2, "CISF");
							mapOrganisationName.put(3, "Border Sercurity Force (BSF)");
							mapOrganisationName.put(4, "ITBP");
						} else if (quote.getLoanQuoteIndustryType() == 103) {
							mapOrganisationName.put(1, "Coast Guards");
							mapOrganisationName.put(2, "Rashtriya Rifles");
							mapOrganisationName.put(3, "Assam Rifles");
						}
						beanList.setOrganisationName(mapOrganisationName);
					}

					Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
						if(quote!=null && appForm!=null && appForm.getAppPreferredStateId()!=null && quote.getLoanQuotePensionPayingStateId()!=null && quote.getLoanQuotePensionPayingDistrId()!=null){
						mapCity = commonService.getStateCityDistrictBranch(2, Constants.PERSONAL_LOAN_ID, appForm.getAppPreferredStateId(), null, null, null, null, null, null, null);
						Map<Integer, String> mapDistrict = new LinkedHashMap<Integer, String>();
						mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.PERSONAL_LOAN_ID, appForm.getAppPreferredStateId(), null, null, null, null, null, null, null);
						if (mapDistrict != null && !mapDistrict.isEmpty()) {
							mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
						}
						beanList.setCitiesoptgrp1(mapCity);
						beanList.setDistricts(mapDistrict);
						beanList.setBranches(commonService
								.getStateCityDistrictBranch(4,
										Constants.PERSONAL_LOAN_ID,
										quote.getLoanQuotePensionPayingStateId(),
										null,
										quote.getLoanQuotePensionPayingDistrId(),
								null, null, null, null, null));
					}
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
			} catch (NullPointerException e) {
				logger.info("PersonalLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java LN 2366 generateUIBeanList() ::" + e.getMessage());
			}
		}

		public StreamResult getInstituteCategoryByName() {
			JSONObject json = new JSONObject();
			try {
				MasterInstitute institute = personalLoanService.getInstituteByInstituteName(instituteName);
				JSONArray scholarStates = new JSONArray();
				Map<Integer, String> stateMap = null;
				if(Constants.BANK_ID == Constants.BANK_ID_SBI && institute!=null && 
						("AA".equals(institute.getInstituteCategeroy()) 
								|| "A".equals(institute.getInstituteCategeroy()) 
								|| "B".equals(institute.getInstituteCategeroy())
								|| "C".equals(institute.getInstituteCategeroy()))) {
					stateMap = commonService.getStateCityDistrictBranch(1, loanTypeId, null, null, null, null, 1, null, null, null);
				} else {
					stateMap = commonService.getStateCityDistrictBranch(1, loanTypeId, null, null, null, null, null, null, null, null);
				}
				for (Map.Entry<Integer, String> entry : stateMap.entrySet()) {
					JSONObject json2 = new JSONObject();
					json2.put("key", entry.getKey());
					json2.put("value", entry.getValue());
					json2.put("displayOrder", 2);
					scholarStates.put(json2);
				}

				if (institute == null) {
					json.put("status", "error");
					json.put("instituteCategory", "");
					json.put("scholarState", scholarStates);
				} else {
					json.put("status", "success");
					json.put("instituteCategory", institute.getInstituteCategeroy());
					json.put("scholarState", scholarStates);
				}
			} catch (JSONException e) {
				logger.info("PersonalLoanAction.java LN 2401 getInstituteCategoryByName() ::" + e.getMessage());
			} catch (SQLException e) {
				logger.info("PersonalLoanAction.java LN 2401 getInstituteCategoryByName() ::" + e.getMessage());
			}
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		public StreamResult getEmployerByName() throws JSONException {
			JSONObject json = new JSONObject();
				MasterEmployer employer = commonService.getEmployerIdByName(employerName);
				if (employer == null) {
					json.put("status", "error");
					json.put("employerId", "");
				} else {
					if(employer.getEmployerCategory()!=null &&  Constants.OTHER_ID_INTEGER.equals(employer.getEmployerCategory())){
						json.put("status", "error");
						json.put("employerId", "");
					} else {
						json.put("status", "success");
						json.put("employerId", employer.getEmployerCompanyId());
					}

				}
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		public ApplicationFormPersonalLoan getAppForm() {
			return appForm;
		}

		public void setAppForm(ApplicationFormPersonalLoan appForm) {
			this.appForm = appForm;
		}

		public ApplicationFormPersonalLoanQuote getQuote() {
			return quote;
		}

		public void setQuote(ApplicationFormPersonalLoanQuote quote) {
			this.quote = quote;
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

		public static String getJsonJSArray3PersonalLoan() {
			return jsonJSArray3PersonalLoan;
		}

		public static void setJsonJSArray3PersonalLoan(String jsonJSArray3PersonalLoan) {
			PersonalLoanAction.jsonJSArray3PersonalLoan = jsonJSArray3PersonalLoan;
		}

		public JSONArray getInitLoanJSONArrayPersonalLoan() {
			return initLoanJSONArrayPersonalLoan;
		}

		public void setInitLoanJSONArrayPersonalLoan(
				JSONArray initLoanJSONArrayPersonalLoan) {
			this.initLoanJSONArrayPersonalLoan = initLoanJSONArrayPersonalLoan;
		}

		public StreamResult occupationByEmployementTypeID() throws JSONException {
			JSONObject json = new JSONObject();
				HttpServletRequest req = RequestUtil.getServletRequest();
				if (req.getParameter("employementId") != null)
				json.put("cardata", SbiUtil.getOccupationByEmployementTypeID(Integer.parseInt(req.getParameter("employementId"))));
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}  
		
	
}
