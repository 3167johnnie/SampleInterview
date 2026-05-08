package com.mintstreet.common.action;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.campaign.impl.CampaignManager;
import com.mintstreet.common.bo.CallLogsDetails;
import com.mintstreet.common.bo.DocumentTypeUIBeanList;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.RepaymentSchedule;
import com.mintstreet.common.bo.UIBeanList;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.bo.WebRequest;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.MasterBank;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCarDealer;
import com.mintstreet.common.entity.MasterContent;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterGender;
import com.mintstreet.common.entity.MasterLoanType;
import com.mintstreet.common.entity.RequestACallBack;
import com.mintstreet.common.entity.SbiCampaignMaster;
import com.mintstreet.common.entity.SecurityChecks;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.state.StateManager;
import com.mintstreet.common.state.StateManagerBean;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.MetaTagsInfo;
import com.mintstreet.common.util.RequestHandler;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.loan.card.bo.CardScenario;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport implements SessionAware {
	private static final Logger logger = LogManager.getLogger(BaseAction.class.getName());
	private static final long serialVersionUID = 3037030470793812837L;

	@Autowired
	protected CampaignManager campaignManager;
	
	@Autowired
	protected CommonService commonService;
	
	@Autowired
	protected SbiUtil SbiUtil;
	
	@Autowired
	protected StateManager stateManager;
	
	@Autowired
	protected RequestHandler requestHandler;
	
	protected LoanScenarioBean loanScenarioBean;
	protected CardScenario cardScenario;
	protected MetaTagsInfo metaInfo = new MetaTagsInfo();
	
	protected int autoLoanPage;
	protected int homeLoanPage;
	protected int personalLoanPage;
	protected int pensionLoanPage;
	protected int educationLoanPage;
	protected int homeTopUpLoanPage;
	protected int agriLoanPage;
	protected int applicationStatusPage;
	protected int currentHomeTabActive;
	protected int homeLoanStates;
	protected Integer requestIndex;
	protected Integer requestType;
	protected String browserver;
	protected String browser;
	protected String selectedLanguage;
	protected Integer loanTypeId;
	protected ApplicationFormLead lead=null;
	protected SbiCampaignMaster sbiCamp=null;
	protected String responseMessage;
	protected Integer appElTypeId;
	protected Integer appPLTypeId;
	protected String isDsrPage;
	protected String ajaxPostUrl;
	protected UIBeanList beanList = new UIBeanList();
	protected JSONObject json=new JSONObject();
	protected JSONObject jsonAlt=new JSONObject();
	public DocumentTypeUIBeanList documentTypeList;
	public static int iPAddressForAppAndDBServerPass = 0;
//	public static int iPAddressForAppAndDBServerPass = 1;

	protected String jsonJSArray;
	public static String jsonJSArrayThankyou;
	protected String toolTipJSArray;
	protected String appReferencetIdEncrypted;
	protected String query;
	
	protected Integer sourceId;
	protected String mobileNo;
	
	protected String alternateMobileNo;
	protected String altEmail;
	public String getaltEmail() {
		return altEmail;
	}

	public void setAltEmail(String altEmail) {
		this.altEmail = altEmail;
	}

	protected String email;
	protected String firstName;
	protected String sessionId;
	protected BankLmsUser bankLmsUser;
	protected Integer industryTypeId;
	protected Integer salaryPackageTypeId;
	
	protected String lead_id;
	protected String app_id;
	protected String token_id;

	protected Integer imageNo;
	protected String imageName;
	protected String loanPurposeUrl;
	protected List<CallLogsDetails> callLogDetails;
	protected StateManagerBean stateManagerBean= null;
	protected InputStream inputStream;
	protected Integer appSeqId;
	protected Integer appLeadId;
	protected double chosenEligibility =0;
	protected Integer chosenTenure=0;
	protected List<RepaymentSchedule>  repayments;
	protected HttpServletRequest request =null;
	protected String leftSideContent;
	protected String rightSideContent;
	protected int infoMessage=0;
	protected boolean isOnlineAndDsrActive;
	protected String source;
	protected String se;
	protected String cp;
	protected String ag;
	protected String campaignCode;
	protected String offerCode;
	protected String trackingCode;
	public Integer visitId;
	protected Integer applicationTypeId;
	protected Integer appBankLmsUserId;
	protected MasterCBSCall cbs;
	protected boolean includeJs=false;
	protected String jsonJSArray1CBS;

	protected boolean contactCenterLmsUser;
	protected String isNasscom;
	protected int creditCardPage;
	
	protected boolean showCBS;
	protected boolean showEKYC;
	protected boolean showCreditCard;
	protected boolean showForm;
	protected boolean showFormCalllogs;

	protected boolean showCBSForPL;
	protected Integer baseApplicationSubTypeId;
	protected Integer crmLeadId;
	protected Integer cbsCallId;
	protected String cbsAccountNumber;
	protected boolean isMingleLead;
	protected int educationTakeOverLoanPage;

	protected String uiType;
	protected String productName;
	protected String productURl;
	
	protected String appBtn;
	protected String iosUrl;
	
	protected String jsonJSArrayApplicationTrack;
	protected Integer isForFaqAndContactUs;
	protected boolean ser;
	protected WebRequest webRequest;
	protected String params;

	protected String fingurePrint;
	protected String fingurePrint2;
	protected String deviceInfo;
	protected String consentFileName;
	protected Integer goldType;
	protected Integer cveType;
  
	protected int cvePage;
	protected int leadConsentId;
	
	protected String mobileNoMaskVal;
	protected String alternateMobileNoMaskVal;
	protected String emailMaskVal;
	protected String panNoMaskVal;
	protected String altIsdVal;
	protected String salaryAccnumVal;
	protected String pensionAccnumVal;
	protected String appotherIdNumVal;
	
	protected String isMobileNoMask;
	protected String isalternateMobileNoMask;
	protected String isEmailMask;
	protected String isPanNoMask;
    protected String isSalaryAccnum;
    protected String isPensionAccnum;
    protected String isAppotherIdNum;
	
	
	
	protected RequestACallBack reqst=new RequestACallBack();
	public void isValidIpAddressForAppAndDBServer(){
		try{
			if(Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
				Constants.SERVER_ID="1";
				iPAddressForAppAndDBServerPass=1;
				}else {
					logger.info("isValidIpAddressForAppAndDBServer() : Deployment Mode value is :: "+Constants.DEPLOYMENT_MODE);
					logger.info("isValidIpAddressForAppAndDBServer() : RAC Server Status :: "+Constants.RAC_SETTING_ENABLE);
					String dataBaseIPAddress = "";
					if(Constants.RAC_SETTING_ENABLE){
						dataBaseIPAddress = Constants.RAC_DIP;
					}else{
						dataBaseIPAddress = Constants.DIP;
					}
					logger.info("isValidIpAddressForAppAndDBServer() : DataBaseIPAddress :: "+dataBaseIPAddress);
					
					InetAddress address = InetAddress.getLocalHost();      
					String appServerIPAddress = address.getHostAddress();   
					logger.info("isValidIpAddressForAppAndDBServer() : AppServerIPAddress :: "+appServerIPAddress);
					
					String 	SERVER_SEQ = null;
					
					
					if(appServerIPAddress.equalsIgnoreCase(Constants.IP_PR)) {
					     SERVER_SEQ="1";
					   }else if(appServerIPAddress.equalsIgnoreCase(Constants.IP_DR) ) {
						 SERVER_SEQ="2";
					   }
					
				    logger.info("SERVER_SEQ() : SERVER_SEQ dd :: "+SERVER_SEQ);                                                                                                      
						List<String> ipList = null;
						//SecurityChecks securityChecks = commonService.getSecurityChecksById(Integer.parseInt(Constants.SERVER_SEQ));
						SecurityChecks securityChecks = commonService.getSecurityChecksById(Integer.parseInt(SERVER_SEQ));
						if(securityChecks!=null){
							if (securityChecks.getSecurityAppServerIds()!=null) {
								ipList = new ArrayList<String>();
								if (securityChecks.getSecurityAppServerIds().contains(",")) {
									String [] ipAddress = securityChecks.getSecurityAppServerIds().split(",");
									for (int i = 0; i < ipAddress.length; i++) {
										if(ipAddress[i]!=null && !("").equals(ipAddress[i])){
											ipList.add(ipAddress[i].trim());
										}
									}
								}else {
									if(securityChecks.getSecurityAppServerIds()!=null && !("").equals(securityChecks.getSecurityAppServerIds())){
										ipList.add(securityChecks.getSecurityAppServerIds().trim());
									}
								}
							//	ipList="http://10.189.41.175/";
								logger.info("isValidIpAddressForAppAndDBServer() : App Id's fetched from DB :: "+ipList.toString()+""+ipList.size());
								if (ipList!=null) {
									if (securityChecks!=null && securityChecks.getSecurityDbServerIp().equalsIgnoreCase(dataBaseIPAddress) && ipList.toString().contains(appServerIPAddress)) {
										int index = ipList.indexOf(appServerIPAddress);
										Constants.SERVER_ID=Integer.toString(index+1);
										iPAddressForAppAndDBServerPass=1;
									}else {
										iPAddressForAppAndDBServerPass=0;
									}
									logger.info("isValidIpAddressForAppAndDBServer() : calculated iPAddressForAppAndDBServerPass :: "+iPAddressForAppAndDBServerPass);
								}
							}
						}
					}
			logger.info("isValidIpAddressForAppAndDBServer() : Generated Server Id :: "+Constants.SERVER_ID);
		} catch(NullPointerException ne) {
			logger.info("isValidIpAddressForAppAndDBServer() : Exception caught :: ", ne);		
		} catch(UnknownHostException e) {
			logger.info("isValidIpAddressForAppAndDBServer() : Exception caught :: ", e);
		}
	}
	
	protected void populateFirstPageContent(Integer contentLoanTypeId, Integer contentRequestIndex){
		List<MasterContent> list = null;
		try {
			list = commonService.getContentForProductId(contentLoanTypeId,contentRequestIndex);
		} catch (NoResultException e) {
			logger.info("BaseAction.java LNo 138 :: populateFirstPageContent() ", e);
		} 
		if(list != null && !list.isEmpty()){
			for( MasterContent content : list){
				if(content.getContentPanelPos().equals("L")){
					leftSideContent = StringEscapeUtils.unescapeHtml(content.getContentPlaceholderContent());
				}else{
					rightSideContent =StringEscapeUtils.unescapeHtml(content.getContentPlaceholderContent());
				}

			}
		}
	}
	
	public void setStaticData() {
		try{
			UIBeanListStatic.isDataSet=true;
			Map<Integer, String>maps = null; 
			int ciyCat= 2;
			maps = new LinkedHashMap<Integer, String>();
			UIBeanListStatic.banksOptionGroup1 = new LinkedHashMap<Integer, String>();
			UIBeanListStatic.banksOptionGroup2 = new LinkedHashMap<Integer, String>();
			UIBeanListStatic.banksOptionGroup1AL = new LinkedHashMap<Integer, String>();
			UIBeanListStatic.banksOptionGroup2AL = new LinkedHashMap<Integer, String>();
			
			List<MasterBank> banks = commonService.getAllBank();
			for (MasterBank bank : banks) {
				maps.put(bank.getBankId(), bank.getBankName());
				if(bank.getBankForHL()==1){
					if(bank.getBankDisplayOrder() == ciyCat || bank.getBankDisplayOrder() == 2){
						UIBeanListStatic.banksOptionGroup1.put(bank.getBankId(), bank.getBankName());
					}else {
						UIBeanListStatic.banksOptionGroup2.put(bank.getBankId(), bank.getBankName());
					}
				}
				if(bank.getBankForAL()==1){
					if(bank.getBankDisplayOrder() == ciyCat || bank.getBankDisplayOrder() == 2){
						UIBeanListStatic.banksOptionGroup1AL.put(bank.getBankId(), bank.getBankName());
					}else {
						UIBeanListStatic.banksOptionGroup2AL.put(bank.getBankId(), bank.getBankName());
					}
				}
			}
			
			UIBeanListStatic.setBanksOptionGroup1(UIBeanListStatic.banksOptionGroup1);
			UIBeanListStatic.setBanksOptionGroup2(UIBeanListStatic.banksOptionGroup2);

			UIBeanListStatic.setBanksOptionGroup1AL(UIBeanListStatic.banksOptionGroup1AL);
			UIBeanListStatic.setBanksOptionGroup2AL(UIBeanListStatic.banksOptionGroup2AL);

			
			Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
			mapCity = commonService.getStateCityDistrictBranchWithoutLoanType(2, null, null, null, null, null, null, null, null, null);
			mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
			UIBeanListStatic.setCitiesOtp1(mapCity);



				
			Map<Integer, String> mapCityBidhyaLakhmi =new LinkedHashMap<Integer, String>();
			mapCityBidhyaLakhmi = commonService.getStateCityDistrictBranch(2, 4, 4, null, null, null, null, null, null, null);
			UIBeanListStatic.setBidyaLakhmiCities(mapCityBidhyaLakhmi);
			
			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			List<MasterCountry> countries = commonService.getAllCountries();
			for (MasterCountry country : countries) {
				maps.put(country.getCountryId(), country.getCountryName());
			}
			UIBeanListStatic.setCountries(maps);
			
			
			List<MasterCountry> countriesIncludingIndia = commonService.getAllCountriesIncludingIndia();
			maps = null;
			maps = new LinkedHashMap<Integer, String>();
			for (MasterCountry country : countriesIncludingIndia) {
				maps.put(country.getCountryId(), country.getCountryName());
			}
			UIBeanListStatic.setCountriesListIncludingIndia(maps);
			
			
			List<MasterLoanType> loanTypes = commonService.getAllLoanType();
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			for (MasterLoanType loanType : loanTypes) {
				if(loanType!=null){
					maps.put(loanType.getLoanTypeId(), loanType.getLoanTypeName());
				}
			}
			UIBeanListStatic.setLoanTypes(maps);
			Map<Integer, String> stateMap = commonService.getStateCityDistrictBranch(1, null, null, null, null, null, null, null, null, null);
			UIBeanListStatic.setStates(stateMap);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			maps.put(1, "Jan");maps.put(2, "Feb");maps.put(3, "Mar");maps.put(4, "Apr");maps.put(5, "May");maps.put(6, "Jun");maps.put(7, "Jul");maps.put(8, "Aug");
			maps.put(9, "Sep");maps.put(10, "Oct");maps.put(11, "Nov");maps.put(12, "Dec");
			UIBeanListStatic.setMonths(maps);
			
			maps = null;
			Map<Integer, Integer> mapsRetire = new LinkedHashMap<Integer, Integer>();
			for(Integer i=45 ;i<=75;i++){
				mapsRetire.put(i, i);	
			}
			UIBeanListStatic.setRetirementAge(mapsRetire);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			int currentYear = Integer.parseInt(DateUtil.getCurrentYear());
			for (int index =0; index<=5;currentYear--  ) {
				index++;
				maps.put(currentYear, String.valueOf(currentYear));
			}
			maps.put(currentYear, String.valueOf(currentYear));
			maps.put(--currentYear, "Before "+String.valueOf(++currentYear));
			UIBeanListStatic.setJoiningyears(maps);
			
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			List<MasterCarDealer> dealers = null;
			dealers = commonService.getAllCarDealer();
			if(dealers!=null){
				for (MasterCarDealer dealer : dealers) {
					maps.put(dealer.getDealerTypeId(), dealer.getDealerName());
				}
				maps.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
				UIBeanListStatic.setDealerList(maps);
			}else{
				UIBeanListStatic.setDealerList(null);
			}
			
			maps = null; 
			LinkedHashMap	<String, String> mapGender = new LinkedHashMap<String, String>();
			List<MasterGender> listGender = null;
			listGender=commonService.getAllGender();
			for (MasterGender gender : listGender) {
				mapGender.put(gender.getGenderName(), gender.getGenderDesc());
			}
			UIBeanListStatic.setMapGender(mapGender);
			
			Map<Integer, String> xpressCreditITstateMap = commonService.getStateCityDistrictBranch(1, null, null, null, null, null, null, null, null, "Y");
			UIBeanListStatic.setXpressCreditITstates(xpressCreditITstateMap);
			
			Map<Integer, String> agriStates = commonService.getStateQuery(Constants.AGRI_LOAN_ID);
			UIBeanListStatic.setAgriStates(agriStates);
			
			//String consentText = commonService.getConsentByLoanType(Constants.CALLBACK_ID).getConsentText();
			Integer consentIdNtb = commonService.getConsentByLoanType(Constants.CALLBACK_ID).getConsentId();
			String consentText = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
			UIBeanListStatic.setConsentCallback(consentText);
			
		} catch(SQLException e){
			UIBeanListStatic.isDataSet=false;
			logger.info("BaseAction.java LNo 180 :: setStaticData() ", e);
		} 
	}
	
	public String releaseSession(Integer loanTypeId) {
		SessionUtil.setEmail(null);
		SessionUtil.setCaptch(null);
		
		SessionUtil.setMobile(null);
		SessionUtil.setAltCaptcha(null);
		SessionUtil.setalternateMobileNumber(null);
		SessionUtil.setISDCode(null);
		//SessionUtil.setISDCodeAlt(null);
		SessionUtil.setApplicantName(null);
		SessionUtil.setLeadId(null);
		SessionUtil.setBankLMSUser(null);
		SessionUtil.setEkycApplication(false);
		SessionUtil.setUiType(null);
		if(loanTypeId==Constants.HOME_TOP_UP_LOAN_ID ){
			SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
			SessionUtil.setHomeTopUpLoanCbsCallId(null);
		}
		if(loanTypeId==Constants.HOME_LOAN_DSR_ID || loanTypeId==Constants.HOME_LOAN_ID){
			//for session release by hakim 
			Integer  appSeqIdHomeLoan=null;
	       	SessionUtil.setHomeLoanApplicationSequenceId(appSeqIdHomeLoan);
			SessionUtil.setHomeLoanCbsCallId(null);
		}
		if(loanTypeId==Constants.AUTO_LOAN_DSR_ID || loanTypeId==Constants.AUTO_LOAN_ID){
			SessionUtil.setAutoLoanApplicationSequenceId(null);
			SessionUtil.setAutoLoanCbsCallId(null);
		}
		if(loanTypeId==Constants.PERSONAL_LOAN_DSR_ID || loanTypeId==Constants.PERSONAL_LOAN_ID){
			SessionUtil.setPersonalLoanTypeSequenceId(null);
			SessionUtil.setPersonalLoanApplicationSequenceId(null);
			SessionUtil.setPensionLoanApplicationSequenceId(null);
			SessionUtil.setPersonalTypeId(null);
			SessionUtil.setPersonalLoanCbsCallId(null);
		}
		if(loanTypeId==Constants.EDUCATION_LOAN_DSR_ID || loanTypeId==Constants.EDUCATION_LOAN_ID){
			SessionUtil.setEducationLoanApplicationSequenceId(null);
			SessionUtil.setEducationScholarLoanSequenceId(null);
			SessionUtil.setEducationGlobalEdvantageSequenceId(null);
			SessionUtil.setEducationTakeOverLoanApplicationSequenceId(null);
			SessionUtil.setEducationBidyaLakshmiLoanSequenceId(null);
			SessionUtil.setEducationLoanCbsCallId(null);
			SessionUtil.setEducationTypeId(null);
		}
		if(loanTypeId==Constants.AGRI_LOAN_DSR_ID || loanTypeId==Constants.AGRI_LOAN_ID){
			SessionUtil.setAgriLoanApplicationSequenceId(null);
			SessionUtil.setAgriLoanCbsCallId(null);
		}
		if(loanTypeId==Constants.CREDIT_CARD_DSR_ID || loanTypeId==Constants.CREDIT_CARD_ID){
			SessionUtil.setCreditCardApplicationSequenceId(null);
			SessionUtil.setCreditCardCbsCallId(null);
		}

		SessionUtil.setApplicationType(null);
		SessionUtil.setVisitIdHomePage(null);
		SessionUtil.setVisitIdHL(null);
		SessionUtil.setVisitIdAL(null);
		SessionUtil.setVisitIdPL(null);
		SessionUtil.setVisitIdEL(null);
		SessionUtil.setVisitIdAGL(null);
		SessionUtil.setLMSIntermediaryRelation(null);
		SessionUtil.setLMSIntermediaryCatagories(null);
		SessionUtil.setCbsExistingRelationshipWithBank(null);
		SessionUtil.setApplicationCRMLeadId(null);
		SessionUtil.setFlipkartLeadId(null);
		return SUCCESS;
	}
		
	public void getDisplayUpdate(boolean isInSession, Integer applicationSubTypeId, Integer applicationType) {
		applicationType = SessionUtil.getApplicationType();
		baseApplicationSubTypeId=applicationSubTypeId;
		
		logger.info("BaseAction.java : isInSession:"+isInSession+" applicationSubTypeId:"+applicationSubTypeId+" applicationSubTypeId:"
		+applicationSubTypeId+" applicationType:"+applicationType+" isDsrPage:"+isDsrPage);
		if(isDsrPage.equalsIgnoreCase("true")){
			if(applicationType==null){
				applicationType=2;
				isInSession=false;
			}
			showFormCalllogs = true;
			if(contactCenterLmsUser){
				if(applicationType==0){
					showForm=true;
					showFormCalllogs = true;
				}else if(applicationType==1){
					if(isMingleLead){
						showCBS=true;
					}else{
						showForm = true;
					}
					showFormCalllogs = true;
				}else if(applicationType==2){
					if(applicationSubTypeId==0){
						if(isInSession){
							showForm=true;
							showFormCalllogs = true;
							showCBS=false;
							showEKYC=false;
						}else{
							if(loanTypeId == 2 || loanTypeId == 4 || loanTypeId == 1 || loanTypeId == 3 || loanTypeId==8){
								showCBS=true;
								showCBSForPL=false;
								showForm=false;
							}else{
								showEKYC=true;
							}
							
						}
					}else if(applicationSubTypeId==1){
						showForm=true;
						showFormCalllogs = true;
					}else if(applicationSubTypeId==2){
						showForm=true;
						showFormCalllogs = true;
					}
				}
			}else{
				if(applicationType==0){
					if(applicationSubTypeId==0){
						showForm = true;
					}else if(applicationSubTypeId==1){
						showForm = true;
					}else if(applicationSubTypeId==2){
						showForm = true;
					}
				}else if(applicationType==1){
					if(applicationSubTypeId==0){
						if(isMingleLead){
							showCBS=true;
						}else{
							showForm = true;
						}
					}else if(applicationSubTypeId==1){
						showForm = true;
					}else if(applicationSubTypeId==2){
						showForm = true;
					}
				}else if(applicationType==2){
					if(applicationSubTypeId==0){
						if(isInSession){
							showCBS=false;
							showEKYC=false;
							showForm=true;
							showFormCalllogs = true;
						}else{
							if(Constants.CREDIT_CARD_ID.equals(loanTypeId) ){
								showEKYC=false;
								showCBS=true;
								showForm=false;
								showCreditCard=true;
							}else if(Constants.AGRI_LOAN_ID.equals(loanTypeId)){
								showEKYC=false;
								showCBS=true;
								showForm=false;
							}else{
								showEKYC=false;
								showCBS=true;
								showForm=false;
							}
						}
					}else if(applicationSubTypeId==1){
						showForm = true;
					}else if(applicationSubTypeId==2){
						showForm = true;
					}
				}
			}
		} else {
			if(applicationType==null){
				applicationType=0;
				isInSession=false;
			}
			if(applicationType==0 || applicationType==2){
				if(applicationSubTypeId==0){
					if(isInSession){
						showForm = true;
						showCBS = false;
						showCreditCard=false;
					}else{
						if(Constants.CREDIT_CARD_ID.equals(loanTypeId)){
							showCBS = true;
							showForm = false;
							showCreditCard=true;
						}else {
							showCBS = true;
							showForm = false;
						}
					}
				}else if(applicationSubTypeId==1){
					if(isInSession){
						showForm = true;
					}else{
						showCBS = true;
					}
				}else if(applicationSubTypeId==2){
					if(isInSession){
						showForm = true;
						showCBS = false;
						showCreditCard=false;
					}else{
						showCBS = true;
					}
				}
			}else if(applicationType==1){
				if(applicationSubTypeId==0){
				}else if(applicationSubTypeId==1){
				}else if(applicationSubTypeId==2){
				}
			}else if(applicationType==2){	
				if(applicationSubTypeId==0){
					
				}else if(applicationSubTypeId==1){
					
				}else if(applicationSubTypeId==2){
					
				}
			}
		}
		
//		originalOcasId = SbiUtil.getOcasSessionId(request);
//		logger.info("BaseAction.java :: LNo 658 Ocas ID " + originalOcasId);
		
		logger.info("BaseAction.java LNo: 660 : isInSession:"+isInSession+" applicationSubTypeId:"+applicationSubTypeId+" applicationSubTypeId:"
				+applicationSubTypeId+" applicationType:"+applicationType+" isDsrPage:"+isDsrPage);
		
		logger.info("BaseAction.java LNo: 663 : showCBS:"+showCBS+" showEKYC:"+showEKYC+" showCreditCard:"
				+showCreditCard+" showForm:"+showForm+" showFormCalllogs:"+showFormCalllogs);
	}
	
	public MetaTagsInfo getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(MetaTagsInfo metaInfo) {
		this.metaInfo = metaInfo;
	}

	public int getHomeLoanPage() {
		return homeLoanPage;
	}

	public void setHomeLoanPage(int homeLoanPage) {
		this.homeLoanPage = homeLoanPage;
	}

	public int getPersonalLoanPage() {
		return personalLoanPage;
	}

	public void setPersonalLoanPage(int personalLoanPage) {
		this.personalLoanPage = personalLoanPage;
	}
	public int getPensionLoanPage() {
		return pensionLoanPage;
	}
	public void setPensionLoanPage(int pensionLoanPage) {
		this.pensionLoanPage = pensionLoanPage;
	}
	public int getEducationLoanPage() {
		return educationLoanPage;
	}

	public void setEducationLoanPage(int educationLoanPage) {
		this.educationLoanPage = educationLoanPage;
	}

	public String getBrowserver() {
		return browserver;
	}

	public void setBrowserver(String browserver) {
		this.browserver = browserver;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public Integer getRequestIndex() {
		return requestIndex;
	}

	public void setRequestIndex(Integer requestIndex) {
		this.requestIndex = requestIndex;
	}

	public int getHomeLoanStates() {
		return homeLoanStates;
	}

	public void setHomeLoanStates(int homeLoanStates) {
		this.homeLoanStates = homeLoanStates;
	}

	public Integer getRequestType() {
		return requestType;
	}

	public void setRequestType(Integer requestType) {
		this.requestType = requestType;
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public int getAutoLoanPage() {
		return autoLoanPage;
	}


	public void setAutoLoanPage(int autoLoanPage) {
		this.autoLoanPage = autoLoanPage;
	}

	public Integer getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(Integer loanTypeId) {
		this.loanTypeId = loanTypeId;
	}

	public ApplicationFormLead getLead() {
		return lead;
	}

	public void setLead(ApplicationFormLead lead) {
		this.lead = lead;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public String getIsDsrPage() {
		return isDsrPage;
	}

	public void setIsDsrPage(String isDsrPage) {
		this.isDsrPage = isDsrPage;
	}

	public String getAjaxPostUrl() {
		return ajaxPostUrl;
	}

	public void setAjaxPostUrl(String ajaxPostUrl) {
		this.ajaxPostUrl = ajaxPostUrl;
	}

	public String getJsonJSArray() {
		return jsonJSArray;
	}

	public void setJsonJSArray(String jsonJSArray) {
		this.jsonJSArray = jsonJSArray;
	}

	public String getAppReferencetIdEncrypted() {
		return appReferencetIdEncrypted;
	}

	public void setAppReferencetIdEncrypted(String appReferencetIdEncrypted) {
		this.appReferencetIdEncrypted = appReferencetIdEncrypted;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	
	//Added by Ruchita for alt mob no
	public String getAlternateMobileNo() {
		return alternateMobileNo;
	}

	public void setAlternateMobileNo(String alternateMobileNo) {
		this.alternateMobileNo = alternateMobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	

	

	public int getApplicationStatusPage() {
		return applicationStatusPage;
	}

	public void setApplicationStatusPage(int applicationStatusPage) {
		this.applicationStatusPage = applicationStatusPage;
	}

	public int getCurrentHomeTabActive() {
		return currentHomeTabActive;
	}

	public void setCurrentHomeTabActive(int currentHomeTabActive) {
		this.currentHomeTabActive = currentHomeTabActive;
	}

	public String getToolTipJSArray() {
		return toolTipJSArray;
	}

	public void setToolTipJSArray(String toolTipJSArray) {
		this.toolTipJSArray = toolTipJSArray;
	}

	public Integer getIndustryTypeId() {
		return industryTypeId;
	}

	public void setIndustryTypeId(Integer industryTypeId) {
		this.industryTypeId = industryTypeId;
	}

	public String getLead_id() {
		return lead_id;
	}

	public void setLead_id(String lead_id) {
		this.lead_id = lead_id;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	

	public Integer getImageNo() {
		return imageNo;
	}

	public void setImageNo(Integer imageNo) {
		this.imageNo = imageNo;
	}

	public List<CallLogsDetails> getCallLogDetails() {
		return callLogDetails;
	}

	public void setCallLogDetails(List<CallLogsDetails> callLogDetails) {
		this.callLogDetails = callLogDetails;
	}

	public String getLoanPurposeUrl() {
		return loanPurposeUrl;
	}

	public void setLoanPurposeUrl(String loanPurposeUrl) {
		this.loanPurposeUrl = loanPurposeUrl;
	}

	public static String getJsonJSArrayThankyou() {
		return jsonJSArrayThankyou;
	}

	public static void setJsonJSArrayThankyou(String jsonJSArrayThankyou) {
		BaseAction.jsonJSArrayThankyou = jsonJSArrayThankyou;
	}

	public String getLeftSideContent() {
		return leftSideContent;
	}

	public void setLeftSideContent(String leftSideContent) {
		this.leftSideContent = leftSideContent;
	}

	public String getRightSideContent() {
		return rightSideContent;
	}

	public void setRightSideContent(String rightSideContent) {
		this.rightSideContent = rightSideContent;
	}


	public static int getiPAddressForAppAndDBServerPass() {
		return iPAddressForAppAndDBServerPass;
	}

	public static void setiPAddressForAppAndDBServerPass(
			int iPAddressForAppAndDBServerPass) {
		BaseAction.iPAddressForAppAndDBServerPass = iPAddressForAppAndDBServerPass;
	}

	public DocumentTypeUIBeanList getDocumentTypeList() {
		return documentTypeList;
	}

	public void setDocumentTypeList(DocumentTypeUIBeanList documentTypeList) {
		this.documentTypeList = documentTypeList;
	}

	public LoanScenarioBean getLoanScenarioBean() {
		return loanScenarioBean;
	}

	public void setLoanScenarioBean(LoanScenarioBean loanScenarioBean) {
		this.loanScenarioBean = loanScenarioBean;
	}

	public Integer getAppSeqId() {
		return appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public Integer getAppLeadId() {
		return appLeadId;
	}

	public void setAppLeadId(Integer appLeadId) {
		this.appLeadId = appLeadId;
	}

	public double getChosenEligibility() {
		return chosenEligibility;
	}

	public void setChosenEligibility(double chosenEligibility) {
		this.chosenEligibility = chosenEligibility;
	}

	public Integer getChosenTenure() {
		return chosenTenure;
	}

	public void setChosenTenure(Integer chosenTenure) {
		this.chosenTenure = chosenTenure;
	}

	public UIBeanList getBeanList() {
		return beanList;
	}

	public void setBeanList(UIBeanList beanList) {
		this.beanList = beanList;
	}

	public List<RepaymentSchedule> getRepayments() {
		return repayments;
	}

	public void setRepayments(List<RepaymentSchedule> repayments) {
		this.repayments = repayments;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public int getInfoMessage() {
		return infoMessage;
	}

	public void setInfoMessage(int infoMessage) {
		this.infoMessage = infoMessage;
	}

	public boolean isOnlineAndDsrActive() {
		return isOnlineAndDsrActive;
	}

	public void setOnlineAndDsrActive(boolean isOnlineAndDsrActive) {
		this.isOnlineAndDsrActive = isOnlineAndDsrActive;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSe() {
		return se;
	}

	public void setSe(String se) {
		this.se = se;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}

	public String getAg() {
		return ag;
	}

	public void setAg(String ag) {
		this.ag = ag;
	}

	public Integer getApplicationTypeId() {
		return applicationTypeId;
	}

	public void setApplicationTypeId(Integer applicationTypeId) {
		this.applicationTypeId = applicationTypeId;
	}
	
	public Integer getAppBankLmsUserId() {
		return appBankLmsUserId;
	}
	public void setAppBankLmsUserId(Integer appBankLmsUserId) {
		this.appBankLmsUserId = appBankLmsUserId;
	}
	
	public int getHomeTopUpLoanPage() {
		return homeTopUpLoanPage;
	}
	public void setHomeTopUpLoanPage(int homeTopUpLoanPage) {
		this.homeTopUpLoanPage = homeTopUpLoanPage;
	}
	
	public MasterCBSCall getCbs() {
		return cbs;
	}
	public void setCbs(MasterCBSCall cbs) {
		this.cbs = cbs;
	}

	public void setSession(Map<String, Object> arg0) {
	}
	public boolean isIncludeJs() {
		return includeJs;
	}
	public void setIncludeJs(boolean includeJs) {
		this.includeJs = includeJs;
	}
	public String getJsonJSArray1CBS() {
		return jsonJSArray1CBS;
	}
	public void setJsonJSArray1CBS(String jsonJSArray1CBS) {
		this.jsonJSArray1CBS = jsonJSArray1CBS;
	}
	public boolean isContactCenterLmsUser() {
		return contactCenterLmsUser;
	}
	public void setContactCenterLmsUser(boolean contactCenterLmsUser) {
		this.contactCenterLmsUser = contactCenterLmsUser;
	}
	public Integer getAppElTypeId() {
		return appElTypeId;
	}
	public void setAppElTypeId(Integer appElTypeId) {
		this.appElTypeId = appElTypeId;
	}
	public SbiCampaignMaster getSbiCamp() {
		return sbiCamp;
	}
	public void setSbiCamp(SbiCampaignMaster sbiCamp) {
		this.sbiCamp = sbiCamp;
	}
	public String getIsNasscom() {
		return isNasscom;
	}
	public void setIsNasscom(String isNasscom) {
		this.isNasscom = isNasscom;
	}
	public int getAgriLoanPage() {
		return agriLoanPage;
	}
	public void setAgriLoanPage(int agriLoanPage) {
		this.agriLoanPage = agriLoanPage;
	}
	public boolean isShowCBS() {
		return showCBS;
	}
	public void setShowCBS(boolean showCBS) {
		this.showCBS = showCBS;
	}
	public boolean isShowForm() {
		return showForm;
	}
	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}
	public boolean isShowEKYC() {
		return showEKYC;
	}
	public void setShowEKYC(boolean showEKYC) {
		this.showEKYC = showEKYC;
	}
	public boolean isShowCreditCard() {
		return showCreditCard;
	}
	public void setShowCreditCard(boolean showCreditCard) {
		this.showCreditCard = showCreditCard;
	}
	public boolean isShowFormCalllogs() {
		return showFormCalllogs;
	}
	public void setShowFormCalllogs(boolean showFormCalllogs) {
		this.showFormCalllogs = showFormCalllogs;
	}
	public String getCbsAccountNumber() {
		return cbsAccountNumber;
	}
	public void setCbsAccountNumber(String cbsAccountNumber) {
		this.cbsAccountNumber = cbsAccountNumber;
	}
	
	public Integer getBaseApplicationSubTypeId() {
		return baseApplicationSubTypeId;
	}
	public boolean isMingleLead() {
		return isMingleLead;
	}
	public void setMingleLead(boolean isMingleLead) {
		this.isMingleLead = isMingleLead;
	}

	public String getUiType() {
		return uiType;
	}
	public void setUiType(String uiType) {
		this.uiType = uiType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductURl() {
		return productURl;
	}
	public void setProductURl(String productURl) {
		this.productURl = productURl;
	}
	public String getIosUrl(){
		return iosUrl;
	}
	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}
	public String getJsonJSArrayApplicationTrack() {
		return jsonJSArrayApplicationTrack;
	}
	public void setJsonJSArrayApplicationTrack(String jsonJSArrayApplicationTrack) {
		this.jsonJSArrayApplicationTrack = jsonJSArrayApplicationTrack;
	}
	public Integer getIsForFaqAndContactUs() {
		return isForFaqAndContactUs;
	}
	public void setIsForFaqAndContactUs(Integer isForFaqAndContactUs) {
		this.isForFaqAndContactUs = isForFaqAndContactUs;
	}
	public boolean isSer() {
		return ser;
	}
	public void setSer(boolean ser) {
		this.ser = ser;
	}
	public Integer getSalaryPackageTypeId() {
		return salaryPackageTypeId;
	}
	public void setSalaryPackageTypeId(Integer salaryPackageTypeId) {
		this.salaryPackageTypeId = salaryPackageTypeId;
	}	
	
	public WebRequest getWebRequest() {
		return webRequest;
	}

	public void setWebRequest(WebRequest webRequest) {
		this.webRequest = webRequest;
	}
	
	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getAppBtn() {
		return appBtn;
	}

	public void setAppBtn(String appBtn) {
		this.appBtn = appBtn;
	}

	public RequestACallBack getReqst() {
		return reqst;
	}

	public void setReqst(RequestACallBack reqst) {
		this.reqst = reqst;
	}
	
	public String getFingurePrint() {
		return fingurePrint;
	}
	public void setFingurePrint(String fingurePrint) {
		this.fingurePrint = fingurePrint;
	}
	public String getFingurePrint2() {
		return fingurePrint2;
	}
	public void setFingurePrint2(String fingurePrint2) {
		this.fingurePrint2 = fingurePrint2;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getConsentFileName() {
		return consentFileName;
	}
	public void setConsentFileName(String consentFileName) {
		this.consentFileName = consentFileName;
	}
	public int getCreditCardPage() {
		return creditCardPage;
	}
	public void setCreditCardPage(int creditCardPage) {
		this.creditCardPage = creditCardPage;
	}
	public Integer getAppPLTypeId() {
		return appPLTypeId;
	}
	public void setAppPLTypeId(Integer appPLTypeId) {
		this.appPLTypeId = appPLTypeId;
	}
	public int getEducationTakeOverLoanPage() {
		return educationTakeOverLoanPage;
	}
	public void setEducationTakeOverLoanPage(int educationTakeOverLoanPage) {
		this.educationTakeOverLoanPage = educationTakeOverLoanPage;
	}

	public Integer getGoldType() {
		return goldType;
	}

	public void setGoldType(Integer goldType) {
		this.goldType = goldType;
	}
  public Integer getCveType() {
    return this.cveType;
  }
  
  public void setCveType(Integer cveType) {
    this.cveType = cveType;
  }
  
  public int getCvePage() {
    return this.cvePage;
  }
  
  public void setCvePage(int cvePage) {
    this.cvePage = cvePage;
  }


	public String getCampaignCode() {
		return campaignCode;
	}
	
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}
	
	public String getOfferCode() {
		return offerCode;
	}
	
	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}
	
	public String getTrackingCode() {
		return trackingCode;
	}
	
	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}
	

	public int getLeadConsentId() {
		return leadConsentId;
	}
	
	public void setLeadConsentId(int leadConsentId) {
		this.leadConsentId = leadConsentId;
	}


	public String getMobileNoMaskVal() {
		return mobileNoMaskVal;
	}

	public void setMobileNoMaskVal(String mobileNoMaskVal) {
		this.mobileNoMaskVal = mobileNoMaskVal;
	}

	public String getIsMobileNoMask() {
		return isMobileNoMask;
	}

	public void setIsMobileNoMask(String isMobileNoMask) {
		this.isMobileNoMask = isMobileNoMask;
	}

	public String getEmailMaskVal() {
		return emailMaskVal;
	}

	public void setEmailMaskVal(String emailMaskVal) {
		this.emailMaskVal = emailMaskVal;
	}

	public String getIsEmailMask() {
		return isEmailMask;
	}

	public void setIsEmailMask(String isEmailNoMask) {
		this.isEmailMask = isEmailNoMask;
	}

	public String getIsalternateMobileNoMask() {
		return isalternateMobileNoMask;
	}

	public void setIsalternateMobileNoMask(String isalternateMobileNoMask) {
		this.isalternateMobileNoMask = isalternateMobileNoMask;
	}

	public String getAlternateMobileNoMaskVal() {
		return alternateMobileNoMaskVal;
	}

	public void setAlternateMobileNoMaskVal(String alternateMobileNoMaskVal) {
		this.alternateMobileNoMaskVal = alternateMobileNoMaskVal;
	}

	public String getIsPanNoMask() {
		return isPanNoMask;
	}

	public void setIsPanNoMask(String isPanNoMask) {
		this.isPanNoMask = isPanNoMask;
	}

	public String getPanNoMaskVal() {
		return panNoMaskVal;
	}

	public void setPanNoMaskVal(String panNoMaskVal) {
		this.panNoMaskVal = panNoMaskVal;
	}

	public String getAltIsdVal() {
		return altIsdVal;
	}

	public void setAltIsdVal(String altIsdVal) {
		this.altIsdVal = altIsdVal;
	}

	public String getSalaryAccnumVal() {
		return salaryAccnumVal;
	}

	public void setSalaryAccnumVal(String salaryAccnumVal) {
		this.salaryAccnumVal = salaryAccnumVal;
	}

	public String getPensionAccnumVal() {
		return pensionAccnumVal;
	}

	public void setPensionAccnumVal(String pensionAccnumVal) {
		this.pensionAccnumVal = pensionAccnumVal;
	}

	public String getAppotherIdNumVal() {
		return appotherIdNumVal;
	}

	public void setAppotherIdNumVal(String appotherIdNumVal) {
		this.appotherIdNumVal = appotherIdNumVal;
	}

	public String getIsSalaryAccnum() {
		return isSalaryAccnum;
	}

	public void setIsSalaryAccnum(String isSalaryAccnum) {
		this.isSalaryAccnum = isSalaryAccnum;
	}

	public String getIsPensionAccnum() {
		return isPensionAccnum;
	}

	public void setIsPensionAccnum(String isPensionAccnum) {
		this.isPensionAccnum = isPensionAccnum;
	}

	public String getIsAppotherIdNum() {
		return isAppotherIdNum;
	}

	public void setIsAppotherIdNum(String isAppotherIdNum) {
		this.isAppotherIdNum = isAppotherIdNum;
	}


package com.mintstreet.loan.homeloan.action;

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
import com.mintstreet.common.entity.IntermediaryRel;
import com.mintstreet.common.entity.LoanStatus;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterLmsIntermediary;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
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
import com.mintstreet.loan.homeloan.bo.impl.HomeProcessManagerImpl;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.homeloan.util.HomeLoanHelper;
import com.mintstreet.loan.product.entity.HlProduct;

public class HomeLoanAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(HomeLoanAction.class.getName());
	private static final long serialVersionUID = 1L;
	@Autowired
	private HomeLoanService homeLoanService;
	
	@Autowired
	private HomeLoanHelper homeLoanHelper;
	
	@Autowired
	private HomeProcessManagerImpl processManagerHomeImpl;
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	@Autowired
	private AESEncryption aesEncryption;
	private ApplicationFormHomeLoan appForm;
	private ApplicationFormHomeLoanQuote quote;

	public JSONArray initLoanJSONArrayHomeLoan;
	public String jsonJSArray1HomeLoan;
	public static String jsonJSArray3HomeLoan;
	public String tenure1Duration;
	public double tenure1Emi;
	public String tenure2Duration;
	public double tenure2Emi;
	public String tenure3Duration;
	public double tenure3Emi;
	public String tenure4Duration;
	public double tenure4Emi;
	public int duration1;
	public int duration2;
	public int repaymentMonth1;
	public int repaymentMonth2;
	
	private IntermediaryRel intermediaryRel = null;
	
	public String execute(){
		return SUCCESS;
	}
	
	public String homeLoanDSR(){
		try{
			uiType=Constants.UI_TYPE;
			loanTypeId=Constants.HOME_LOAN_DSR_ID;
			ajaxPostUrl=Constants.HOME_LOAN_ACTION_DSR;
			isDsrPage="true";
			request = RequestUtil.getServletRequest();
			
			if(SessionUtil.getBankLMSUser()==null){
				sessionId = SbiUtil.getSessionId(request, sessionId);
				if (sessionId==null) {
					if (token_id!=null) {
						sessionId = SbiUtil.getSessionId(request, token_id);
					}
				}
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
			}else{
				bankLmsUser = SessionUtil.getBankLMSUser();
			}
			
			if (bankLmsUser!=null) {				
				logger.info("Inside if when bank lms user is not null");
				appBankLmsUserId = bankLmsUser.getLmsUserId();
				SessionUtil.setBankLMSUser(bankLmsUser);
				contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
				//logger.info("HomeLoanAction.java LNo : 111 : isContactCenterLmsUser = "+contactCenterLmsUser);
				if ( !ValidatorUtil.isValid(app_id) && !ValidatorUtil.isValid(lead_id)) {
					if(request.getParameter("generatePDF")!=null){

					}else if(request.getParameter("requestIndex")==null){
						if(SessionUtil.getApplicationType()==null){

						}else{

							releaseSession(Constants.HOME_LOAN_ID);
						}
						applicationTypeId=2;
						SessionUtil.setApplicationType(2);
						appSeqId=null;
						SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
						appLeadId=null;
						SessionUtil.setLeadId(appLeadId);
						firstName=null;
						mobileNo=null;
						email=null;
						SessionUtil.setEmail(email);
						SessionUtil.setMobile(mobileNo);
						SessionUtil.setalternateMobileNumber(alternateMobileNo);			
						SessionUtil.setApplicantName(firstName);			
					}
				}else if (ValidatorUtil.isValid(app_id)) {
					appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
					SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
					visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdHL(visitId);
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
						visitId=lead.getLeadVisitId();
						SessionUtil.setVisitIdHL(visitId);
						email = lead.getLeadWorkEmail()!=null ? lead.getLeadWorkEmail().toString() : "";
						SessionUtil.setEmail(email);
						if(lead.getLeadApplyingFrom()==2){
							mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo() : "";
							SessionUtil.setISDCode(lead.getLeadIsdCode()!=null?lead.getLeadIsdCode():"");
							
							//Added for displaying alt no in DSR page for UPLOAD functionality
							alternateMobileNo = lead.getLeadAlternateNumber();				
						}else{			
							mobileNo = lead.getLeadMobileNo()!=null ? lead.getLeadMobileNo().toString() : "";
							SessionUtil.setISDCode(Constants.COUNTRY_CODE_INDIA);
							
							//Added for displaying alt no in DSR page for UPLOAD functionality
							alternateMobileNo = lead.getLeadAlternateNumber();						
						}
						
						boolean isTeleCallerUser = false;
						isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
						logger.info("isTeleCallerUser::::" + isTeleCallerUser);
						
						if (isTeleCallerUser && mobileNo != null  && mobileNo !="") {
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
						cbsAccountNumber=lead.getLeadCbsAccountNo();
						
						leadConsentId = lead.getLeadConsentId()!=null ? lead.getLeadConsentId() : 0;
						
						if(cbsAccountNumber!=null){
							isMingleLead=true;
						}
						logger.info("HomeLoanAction.java LNo : 111 : isMingleLead = " +isMingleLead );
						if(lead.getLeadAppSeqId()!=null){
							appSeqId=lead.getLeadAppSeqId();
							
						}else{
							appSeqId=null;
						}
						SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
					}
					if(request.getParameter("requestIndex")==null){
						SessionUtil.setApplicationType(1);
						applicationTypeId=1;
					}
				} else if (SessionUtil.getHomeLoanApplicationSequenceId() != null) {
					if(request.getParameter("requestIndex")==null){
						releaseSession(Constants.HOME_LOAN_ID);
					}
					appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
					bankLmsUser = SessionUtil.getBankLMSUser();
				}
				if(ValidatorUtil.isValid(applicationTypeId)){
					SessionUtil.setApplicationType(applicationTypeId);
				}
				
				if(request.getParameter("requestIndex")==null){
					requestIndex=9;
				}
			}else{
				logger.info("HomeLoanAction.java LNo : 204 bankLmsUser is null : ");
				if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
					//return "home"+(uiType==null?"":uiType);		//commented for CSR 2018 - Session management
					
					// Added for CSR 2018 - Session management
					if(appSeqId == null){
						String lmsURL = Constants.BANK_ONLINE_URL;
						sessionId = null;						
						responseMessage = "error|"+"Sorry for the inconvenience, your session has been timed out. Please click <a href='" + lmsURL + "'> here </a> to start again";	
						logger.info("responseMessage 247..." + responseMessage);      
				        
						return "jsonResponsePage";
					}
					
				}else{
					bankLmsUser = SessionUtil.getBankLMSUser();
					if (bankLmsUser!=null) {						
						appBankLmsUserId = bankLmsUser.getLmsUserId();
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
						//logger.info("HomeLoanAction.java LNo : 199 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if(SessionUtil.getLeadId()!=null){
							appLeadId = SessionUtil.getLeadId();
							lead = commonService.getLeadById(appLeadId);
							appSeqId=lead.getLeadAppSeqId();
						} else if(appSeqId==null){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						}
					}else{
						if(appBankLmsUserId==null){
							appBankLmsUserId = 9514;
						}
						bankLmsUser= commonService.getBankLmsUserById(appBankLmsUserId);
						SessionUtil.setBankLMSUser(bankLmsUser);
						contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
					//	logger.info("HomeLoanAction.java LNo : 213 : isContactCenterLmsUser = "+contactCenterLmsUser);
						if ( !ValidatorUtil.isValid(appSeqId) && !ValidatorUtil.isValid(appLeadId) ) {
							applicationTypeId=2;
							SessionUtil.setApplicationType(2);
						}else if (ValidatorUtil.isValid(appSeqId)) {
							SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
							if(request.getParameter("requestIndex")==null){
								applicationTypeId=0;
								SessionUtil.setApplicationType(0);
							}
						} else if (ValidatorUtil.isValid(appLeadId)) {
							SessionUtil.setLeadId(appLeadId);
							if(lead==null){
								lead = commonService.getLeadById(appLeadId);
							}
							visitId=lead.getLeadVisitId();
							SessionUtil.setVisitIdHL(visitId);
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
							SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
							if(request.getParameter("requestIndex")==null){
								SessionUtil.setApplicationType(1);
								applicationTypeId=1;
							}
						} else if (SessionUtil.getHomeLoanApplicationSequenceId() != null) {
						}
						
						if(ValidatorUtil.isValid(applicationTypeId)){
							SessionUtil.setApplicationType(applicationTypeId);
						}
						if(request.getParameter("requestIndex")==null){
							requestIndex=-1;
						}
						if (appSeqId!=null) {
							visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
							SessionUtil.setVisitIdHL(visitId);
						}
					}
				}
			}
			//logger.info("HomeLoanAction.java LNo : 248 : "+SessionUtil.getSession());
			return getHomeLoan(Constants.HOME_LOAN_DSR_ID);
		} catch(NullPointerException e){
			logger.info("HomeLoanAction.java LNo : 251 : homeLoanDSR() ", e);
			return "home"+(uiType==null?"":uiType);
		} catch(SQLException e){
			logger.info("HomeLoanAction.java LNo : 251 : homeLoanDSR() ", e);
			return "home"+(uiType==null?"":uiType);
		}
	}
	
	public String homeLoan(){
		try{
			if(ValidatorUtil.isValid(uiType)){SessionUtil.setUiType(uiType);}else{SessionUtil.setUiType(null);}
			loanTypeId=Constants.HOME_LOAN_ID;
			isDsrPage="false";
			ajaxPostUrl=Constants.HOME_LOAN_ACTION;
			SessionUtil.setApplicationType(0);
			request=RequestUtil.getServletRequest();
			if(SessionUtil.getBankLMSUser()!=null){
				isOnlineAndDsrActive=true;
				releaseSession(Constants.HOME_LOAN_ID);

				SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
				SessionUtil.setAutoLoanApplicationSequenceId(null);
				SessionUtil.setEducationLoanApplicationSequenceId(null);
				SessionUtil.setEducationLoanApplicationSequenceId(null);
				SessionUtil.setPersonalLoanApplicationSequenceId(null);
			}
			if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
			}else{
				if(appSeqId!=null){
					SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
					visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
					SessionUtil.setVisitIdHL(visitId);
				}
			}
			return getHomeLoan(Constants.HOME_LOAN_ID);
		} catch (NullPointerException e) {
			logger.info("HomeLoanAction.java LNo : 206 : homeLoan() ", e);
		} catch (SQLException e) {
			logger.info("HomeLoanAction.java LNo : 206 : homeLoan() ", e);
		}
		return "homePage"+(uiType==null?"":uiType);
	}
	
	public String getHomeLoan(Integer moduleId){
		
		logger.info("inside getHomeLoan start...");
		loanTypeId=Constants.HOME_LOAN_ID;
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
			SbiUtil.getOcasSessionId(request);
			if(!ValidatorUtil.isValid(sourceId)){
				sourceId=1;
			}
			logger.info("HomeLoanAction.java LN 435 get state getHomeLoan() " + stateManagerBean.getState());
			SessionUtil.setHomeLoanState(stateManagerBean.getState());
			if(stateManagerBean.getState() != 16)
				SessionUtil.setHomeLoanError("false");
			
			logger.info("HomeLoanAction.java LN 401 session's visit id is " + SessionUtil.getVisitIdHL());
			if(SessionUtil.getVisitIdHL()!=null){
				visitId = SessionUtil.getVisitIdHL(); 
			}else{
				if(stateManagerBean.getState()==-1 || visitId ==null ){
					logger.info("HomeLoanAction.java LN 406 sequenceId is " + SessionUtil.getHomeLoanApplicationSequenceId());
					if(SessionUtil.getHomeLoanApplicationSequenceId() == null || moduleId == Constants.HOME_LOAN_DSR_ID || visitId == null ){
						visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.HOME_LOAN_ID );
						/*if (!(campaignCode == null && offerCode == null && trackingCode == null)) {
							campaignManager.martech(visitId, campaignCode, offerCode, trackingCode, Constants.HOME_LOAN_ID, 0);
						}*/
						if(ValidatorUtil.isValid(visitId)){
							SessionUtil.setVisitIdHL(visitId);
						}else{
							logger.info("HomeLoanAction.java LN 334 unable to insert into visit entity.");
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
					}
				}
			}
			if(stateManagerBean.getState()==-1){
				if(stateManagerBean.getValidatorResponse().isStatus()){
					metaInfo.setTitle(Constants.HOME_LOAN_TITLE);
					metaInfo.setKeywords(Constants.HOME_LOAN_KEYWORDS);
					metaInfo.setDescription(Constants.HOME_LOAN_DESCRIPTION);
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
					json = processManagerHomeImpl.verifyConcentOtp(moduleId, stateManagerBean.getState(), 
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
					json = processManagerHomeImpl.verifySMSOTP(moduleId, stateManagerBean.getState(), 
							(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), 
							ajaxPostUrl,isDsrPage, otherRequest);
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
			
			logger.info("stateManagerBean value check :: " + stateManagerBean.getState());
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
							appOTPVerifyType, inputOtp, userEmail, SessionUtil.getHomeLoanApplicationSequenceId(),
							SessionUtil.getHomeLoanCbsCallId(), 
							Constants.HOME_LOAN_ACTION);
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
						CBSCallResponse cbsCallResponse = processManagerHomeImpl.processCbsCall(appSeqId, requestIndex, cbs, 
								isDsrPage, (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), visitId, 
								SessionUtil.getHomeLoanCbsCallId(), Constants.HOME_LOAN_ACTION, null, null, null);
						cbs = commonService.getMasterCBSCallObjById(SessionUtil.getHomeLoanCbsCallId());
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
						
					} catch (NullPointerException e) {
						logger.info("HomeLoanAction.java LN 184 Exception occured:::::",e);
						responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
						return "jsonResponsePage";
					} catch (SQLException e) {
						logger.info("HomeLoanAction.java LN 184 Exception occured:::::",e);
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
					if(stateManagerBean.getValidatorResponse().isStatus()) {
						releaseSession(Constants.HOME_LOAN_ID);
						appSeqId=null;
						appLeadId=null;
						firstName=null;
						mobileNo=null;
						alternateMobileNo=null;			
						email=null;
						stateManagerBean.setState(-1);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 343 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} 
			}
			

			if(stateManagerBean.getState()==25){
				try {
					/*String currentOcasId = SbiUtil.getOcasSessionId(request);
					logger.info("Current Ocas Id $$ " + currentOcasId);
					releaseSession(Constants.HOME_LOAN_ID);
					String appReferenceId = request.getParameter("generatePDF").substring(0, 12);
					if(stateManagerBean.getValidatorResponse().isStatus()) {
						if(ValidatorUtil.isValid(appReferenceId)) {
							appForm = homeLoanService.getApplicationFormHomeLoanByAppReferenceId(appReferenceId);
							appSeqId = appForm.getAppSeqId();
							logger.info("HomeLoanAction.java LN 593 appSeqId is :: " + appSeqId);
							if(appForm!=null) {
								if(ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									logger.info("Original Ocas ID from app form " + appForm.getOcasID());
									logger.info("Current Ocas Id $$ " + currentOcasId);
									if (ValidatorUtil.isValid(appForm.getOcasID())) {
										logger.info("both ocas ID are same ");
										String filePath = Constants.PDF_GENRATION_BASE_PATH + Constants.HL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
										inputStream = new FileInputStream(new File(filePath));
										SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
										return "downLoadPDF";
									} else {
										logger.info("HomeLoanAction.java Ocas ID is not same as valid Ocas ID");
										SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
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
					}
					*/
					
					if (stateManagerBean.getValidatorResponse().isStatus()) {
						logger.info("Current Ocas Id $$ f ");
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if (ValidatorUtil.isValid(appSeqId)) {
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if (appForm != null) {
								if (ValidatorUtil.isValid(appForm.getAppDownloadPdfFileName())) {
									String filePath = Constants.PDF_GENRATION_BASE_PATH
											+ Constants.HL_PDF_GENRATION_LOCATION + appForm.getAppDownloadPdfFileName();
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
					logger.info("HomeLoanAction.java LNo : 368 : stateManagerBean.getState()==25 ", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} catch (SQLException e) {
					logger.info("HomeLoanAction.java LNo : 368 : stateManagerBean.getState()==25 ", e);
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
						if(SessionUtil.getHomeLoanApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
					logger.info("HomeLoanAction.java LN 404 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				} catch (SQLException e) {
					logger.info("HomeLoanAction.java LN 404 stateManagerBean.getState()==26 ::", e);
					responseMessage = Constants.SORRY_FOR_INCONVENIENCE;
					stateManagerBean.setState(-1);
				}
				
			}

			if(stateManagerBean.getState()==23){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						if(isDsrPage.equalsIgnoreCase("true")){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
							
							getCallLogs(appSeqId, appLeadId);
						}
						return "callsLogDetails"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 426 stateManagerBean.getState()==23 ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						logger.info("HomeLoanAction.java LNo:329 :: appSeqId :: "+appSeqId+" imageNo :: "+imageNo+" ajaxPostUrl :"+ajaxPostUrl + "imageName :: "+imageName );
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
					logger.info("HomeLoanAction.java LN 459 stateManagerBean.getState()==22 ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
					logger.info("HomeLoanAction.java LN 459 stateManagerBean.getState()==21 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
				

			if(stateManagerBean.getState()==20){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
							responseMessage = "success|"+Constants.PORT+Constants.CONTEXT+((moduleId == Constants.HOME_LOAN_ID.intValue())?Constants.HOME_LOAN_ACTION:Constants.HOME_LOAN_ACTION_DSR)+"?generatePDF="+appForm.getAppReferenceId()+rand.nextInt(1000);
						}else{
							responseMessage = "error|Please wait pdf generation in progress ...";
						}
						
						logger.info("HL session release called for 20 stateID");
						return "jsonResponsePage";
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 459 stateManagerBean.getState()==20 ::", e);
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
					logger.info("HomeLoanAction.java LN 543 stateManagerBean.getState()==19 ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
					logger.info("HomeLoanAction.java LN 580 stateManagerBean.getState()==18 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==17){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appForm = stateManagerBean.getAppHL();
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
						if(appForm.getAppHomeLoanId()==12){
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.HOME_LOAN_ID, 1);
						}else{
							documentTypeList = SbiUtil.generateUIBeanDocumentTypeList(Constants.HOME_LOAN_ID, appForm.getAppHomeLoanId());
						}

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
									logger.info("HomeLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								} 
							}
							
						}else if(Constants.SBI_SHAURYA_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) || Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) ){
							if(appForm.getAppLoanEmi1Duration()!=null && appForm.getAppLoanEmi1Duration()>0){
								duration1 = appForm.getAppLoanEmi1Duration();
							}
							if(appForm.getAppLoanEmi2Duration()!=null && appForm.getAppLoanEmi2Duration()>0){
								duration2 = appForm.getAppLoanEmi2Duration();
							}
						}
						logger.info("HomeLoanAction.java LN 1015 Before Returning Thank You Page");
						logger.info("HL session release called for 17 stateID");
						return "thankYouPage"+(uiType==null?"":uiType);
					}else{
						logger.info("HomeLoanAction.java LN 1018 Before Returning Error Page : "+stateManagerBean.getValidatorResponse().isStatus());
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
						
					}
						
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 631 stateManagerBean.getState()==17 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			logger.info("stateManagerBean.getState():::" + stateManagerBean.getState());
			logger.info("session Util check():::" + SessionUtil.getHomeLoanState16());
			OtherRequest otherRequest1 = stateManagerBean.getOtherRequest();
			//logger.info("otherRequest:::" + otherRequest1);
			if(otherRequest1 != null && otherRequest1.getAlternateMobileNumber() != null) {
				SessionUtil.setHomeLoanState16("false");
			}
			logger.info("969 state is :::" + stateManagerBean.getState());
			if((stateManagerBean.getState()==14 || stateManagerBean.getState()==15 || stateManagerBean.getState()==16) && SessionUtil.getHomeLoanState16() == "false") {
				try{
					logger.info("972 Inside state :::" + stateManagerBean.getState());
					if(stateManagerBean.getState() == 16)
						SessionUtil.setHomeLoanState16("true");
					if(stateManagerBean.getValidatorResponse().isStatus()){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						//logger.info("otherRequest:::" + otherRequest);
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(stateManagerBean.getState()==16 || stateManagerBean.getState()==14) {
							if(!Constants.HL_CAPTCHA_BY_PASS && ajaxPostUrl.equalsIgnoreCase("home-loan") && otherRequest.getAlternateMobileNumber() == null) {
								logger.info("state is " + stateManagerBean.getState());
									logger.info("Inside state " + stateManagerBean.getState());
									SessionUtil.setHomeLoanState(stateManagerBean.getState());
									logger.info("now session captcha is " + SessionUtil.getCaptch());
									responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
								
								if (responseMessage!=null) {
									logger.info("@@@ setting session error to true ");
									SessionUtil.setHomeLoanState16("false");
									SessionUtil.setHomeLoanError("true");
									return "jsonResponsePage";
								}
							}
						}
						
						json = processManagerHomeImpl.processMobileOTP(appSeqId, moduleId,stateManagerBean.getState(), 
								(bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl,otherRequest);
						if(json.get("status").toString().equalsIgnoreCase("success")){
							appForm =homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if (appForm!=null) {
								quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
								if (quote!=null) {
									if (stateManagerBean.getState()==16) {
											responseMessage = "success|"+json.getString("message");
											if(json.getString("alertCount")!=null){
												responseMessage =responseMessage+"|"+json.getString("alertCount");
												
											}
											logger.info("for json error value " + SessionUtil.getHomeLoanError());
											
											if(otherRequest.getAlternateMobileNumber() == null) {
												responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
												logger.info("HomeLoanAction.java LNo : 1116 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
											}
											return "jsonResponsePage";
									} else {
										responseMessage = "success|"+json.getString("message");
										if(json.getString("alertCount")!=null){
											responseMessage =responseMessage+"|"+json.getString("alertCount");
											SessionUtil.setCaptch(null);
											
											if (stateManagerBean.getState() == 14 && otherRequest.getAlternateMobileNumber() == null) {
												logger.info("HomeLoanAction.java LNo : 1035 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
												responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
												logger.info("HomeLoanAction.java LNo : 1037 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
											}
											return "jsonResponsePage";
										}
									}
								}
							}
						}else{
							responseMessage = "error|"+json.getString("message");
							if(json.getString("alertCount")!=null){
								responseMessage =responseMessage+"|"+json.getString("alertCount");
								logger.info("Inside state " + stateManagerBean.getState() + " for error");
								SessionUtil.setHomeLoanState16("false");
								if (stateManagerBean.getState() == 14 && otherRequest.getAlternateMobileNumber() == null) {
									logger.info("HomeLoanAction.java LNo : 1144 before encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
									responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
									logger.info("HomeLoanAction.java LNo : 1152 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
								}
								return "jsonResponsePage";
							}
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 668 stateManagerBean.getState()==14, 15, 16 :: "+stateManagerBean.getState()+" ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						String applyUrlData[] = null;
						String applyUrl =null;
						if(otherRequest!=null && otherRequest.getAppliedLoanId()!=null){
							applyUrl = aesEncryption.decrypt(otherRequest.getAppliedLoanId().toString());
							logger.info("HomeLoanAction.java LN 1134 : decrypted appliedLoanId===" + applyUrl);
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
						quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
						if(quote == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						logger.info("HomeLoanAction.java LN 1156 : QuoteLoanProductId===" + applyUrlData[0]);
						quote.setLoanQuoteLoanProductId(Integer.parseInt(applyUrlData[0]));
						quote.setLoanQuoteLoanTenure(chosenTenure);
						quote.setLoanQuoteLoanAmount(chosenEligibility);
						quote.setLoanQuoteAppliedCoupon(appForm.getApploanappliedCoupon());
						quote.setLoanQuoteLoanAccountType(Integer.parseInt(applyUrlData[1]));
						/*loanScenarioBean = homeLoanHelper.callBRE(appForm, quote, bankLmsUser, quote.getLoanQuoteId(), quote.getLoanQuoteNewVisitId(), ajaxPostUrl, false);
						logger.info("HomeLoanAction.java LN 1111" + loanScenarioBean.getMessage());
						if(loanScenarioBean.getStatus()!=1){
							logger.info("HomeLoanAction.java LN 1113" + loanScenarioBean.getMessage());
							responseMessage = "error|"+loanScenarioBean.getMessage();
							return "jsonResponsePage";
						}
						appForm = loanScenarioBean.getApplicationHL();*/
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
						

						
						if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appForm.getAppHomeLoanId()) ){
							appForm.setAppLoanAccountType(1);
						}else{
							appForm.setAppLoanAccountType(Integer.parseInt(applyUrlData[1]));
						}
						
						appForm.setAppLoanEmi(Double.parseDouble(applyUrlData[2]));
						appForm.setAppLoanProcessingFee((double)Double.parseDouble(applyUrlData[3]));
						appForm.setAppLoanInterestRate(Float.parseFloat(applyUrlData[4]));
						appForm.setAppLoanMoratoriumPeriod(Integer.parseInt(applyUrlData[5]));
						appForm.setAppEmiNmiRatio(Float.parseFloat(applyUrlData[6]));
						appForm.setAppLoanEmiDiscount(applyUrlData[7].toString().equalsIgnoreCase("-")?null:Double.parseDouble(applyUrlData[7]));
						appForm.setAppLoanProcessingFeeDiscount(applyUrlData[8].toString().equalsIgnoreCase("-")?null:(double) Double.parseDouble(applyUrlData[8]));
						appForm.setAppLoanInterestRateDiscount(applyUrlData[9].toString().equalsIgnoreCase("-")?null:Float.parseFloat(applyUrlData[9]));
						appForm.setAppLoanEmi1(Double.parseDouble("0.0"));
						appForm.setAppLoanEmi2(Double.parseDouble("0.0"));
						appForm.setAppLoanEmiDiscount1(Double.parseDouble("0.0"));
						appForm.setAppLoanEmiDiscount2(Double.parseDouble("0.0"));
						if (applyUrlData.length>10){
							if (applyUrlData[10]!=null){
								appForm.setAppLoanEmi1(Double.parseDouble(applyUrlData[10]));
							}
							if (applyUrlData[11]!=null && !applyUrlData[11].equalsIgnoreCase("-")){
								appForm.setAppLoanEmi2(Double.parseDouble(applyUrlData[11]));
							}
							if (applyUrlData[12]!=null && !applyUrlData[12].equalsIgnoreCase("-")){
								appForm.setAppLoanEmiDiscount1(Double.parseDouble(applyUrlData[12]));
							}
							if (applyUrlData[13]!=null && !applyUrlData[13].equalsIgnoreCase("-")){
								appForm.setAppLoanEmiDiscount2(Double.parseDouble(applyUrlData[13]));
							}
							if (applyUrlData[14]!=null && !applyUrlData[14].equalsIgnoreCase("-")){
								duration1 = Integer.parseInt(applyUrlData[14]);
							}
							if (applyUrlData[15]!=null && !applyUrlData[15].equalsIgnoreCase("-")){
								duration2 =Integer.parseInt(applyUrlData[15]);;
							}
						}else{
							appForm.setAppLoanEmi1(null);
							appForm.setAppLoanEmi2(null);
							appForm.setAppLoanEmiDiscount1(null);
							appForm.setAppLoanEmiDiscount2(null);
						}
						
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
						
						appForm = homeLoanService.save(appForm);
						quote= homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
						homeLoanPage=3;
						generateUIBeanList(homeLoanPage);
						if(jsonJSArray3HomeLoan==null){
							jsonJSArray3HomeLoan = SbiUtil.populateJSValidation(17, moduleId).toString();
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
									logger.info("HomeLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
								}
							}
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
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 826 stateManagerBean.getState()==13 ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
					logger.info("HomeLoanAction.java LN 855 stateManagerBean.getState()==12 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if(stateManagerBean.getState()==11){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						quote = stateManagerBean.getQuoteHL();
						if(isOnlineAndDsrActive){
							responseMessage = "error|"+Constants.ALREADY_LOGGEDIN_REDIRECTION;
							return "jsonResponsePage";
						}
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
					logger.info("HomeLoanAction.java LN 896 stateManagerBean.getState()==11 ::", e);
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
					logger.info("HomeLoanAction.java LN 915 stateManagerBean.getState()==10 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==9){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						stateManagerBean.setOriginalState(stateManagerBean.getState());
						if(stateManagerBean.getValidatorResponse().isStatus()){
							if(moduleId==Constants.HOME_LOAN_ID){
								releaseSession(Constants.HOME_LOAN_ID);
								appSeqId=null;
								lead=null;
							}
							if(request.getParameter("HLQuoteToken")!=null){
								String encyQuotId=aesEncryption.decrypt(request.getParameter("HLQuoteToken").toString());
								if(ValidatorUtil.isValid(encyQuotId)){
									appSeqId = Integer.parseInt(encyQuotId);
									SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
									if(SessionUtil.getHomeLoanApplicationSequenceId()==null){
										SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
									}
									visitId = homeLoanService.getVisitByAppSeqId(appSeqId);
									SessionUtil.setVisitIdHL(visitId);
									stateManagerBean.setState(2);
								}else{
									stateManagerBean.setState(-1);
								}
							}
							if(request.getParameter("token_id")!=null){
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
											SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
										}
										stateManagerBean.setState(-1);
									}else if(app_id!=null && app_id.length() >0){
										appSeqId = Integer.parseInt(Security.decrypt(app_id, bankLmsUser.getLmsHashKey()));
										SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
										if(SessionUtil.getHomeLoanApplicationSequenceId()==null){
											SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
										}
										stateManagerBean.setState(2);
									}else{
										stateManagerBean.setState(-1);
									}
								}else{
							//		logger.info("HomeLoanAction.java LNo : 997 : 2 "+SessionUtil.getSession());
									releaseSession(Constants.HOME_LOAN_ID);
									return "home"+(uiType==null?"":uiType);
								}
							}
						}else{
							String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
							responseMessage = "error|"+msg;
							return "jsonResponsePage";
						}
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1000 stateManagerBean.getState()==9 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			

			if(stateManagerBean.getState()==8){
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
								double emi=0,emi1=0,emi2=0;
								int moratoriumMonths=0;
								if(request.getParameter("ir")!=null){
									appLoanInterestRate = Float.parseFloat(request.getParameter("ir"));
								}
								if(appLoanInterestRate<1){
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
								
								 if(Constants.SBI_SHAURYA_PRODUCT_ID==pId || Constants.SBI_PRIVILEGE_PRODUCT_ID==pId){
									if(request.getParameter("emi1")!=null && request.getParameter("emi2")!=null){
										emi1 = Double.parseDouble(request.getParameter("emi1"));
										emi2 = Double.parseDouble(request.getParameter("emi2"));
									}else if(request.getParameter("emi1")==null && request.getParameter("emi2")==null){
										if(request.getParameter("emi")!=null){
											emi = Double.parseDouble(request.getParameter("emi"));
										}
										if(emi<1){
											responseMessage = "error|Invalid parameter";
											return "jsonResponsePage";
										}
									}
									else{
										if((emi1<1 || emi2<1) && request.getParameter("emi")==null){
											responseMessage = "error|Invalid parameter";
											return "jsonResponsePage";
										}
									}
								}else{
									if(appForm.getAppHomeLoanId()==12){
										emi = appForm.getAppLoanEmi();
									}else {
										if(request.getParameter("emi")!=null){
											emi = Double.parseDouble(request.getParameter("emi"));
										}
									}
									
									if(emi<1){
										responseMessage = "error|Invalid parameter";
										return "jsonResponsePage";
									}
								}
								
								if(request.getParameter("pf")!=null){
									processingFee = (double) Math.round(new Double((request.getParameter("pf"))));
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
								appForm.setAppLoanProcessingFee(processingFee);
								appForm.setAppLoanEmi(emi);
								HlProduct product =  commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
								appForm.setAppHomeLoanId(pId);
								appForm.setAppProductTenureFlag(product.getHlProductSliderTenure());

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
									if(emi1>0.0 && emi2>0.0){
										appForm.setAppLoanInterestRateDiscount(appForm.getAppLoanInterestRate());
										irD = appForm.getAppLoanInterestRateDiscount();
									}else{
										irD = appForm.getAppLoanInterestRate();
									}
								}
								if(Constants.FLEXI_PAY_PRODUCT_ID.intValue() == pId){
										repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), irD, emiD,1,1,moratoriumMonths);
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
													logger.info("HomeLoanAction.java LN 1149 stateManagerBean.getState()==8 ::", e);
												} 
											}
										}
										}else{
											if(Constants.SBI_PRIVILEGE_PRODUCT_ID.intValue() == pId || Constants.SBI_SHAURYA_PRODUCT_ID.intValue() == pId){
											
												repaymentMonth1 = 0;
												repaymentMonth2 = 0;
												if(request.getParameter("emi1")!=null){
													emi1 = Double.parseDouble(request.getParameter("emi1"));
													appForm.setAppLoanEmi1(emi1);
													logger.info("HomeLoanAction.java LN 1417 stateManagerBean.getState()==8 emi1::"+emi1);
												}
												if(request.getParameter("emi2")!=null){
													emi2 = Double.parseDouble(request.getParameter("emi2"));
													appForm.setAppLoanEmi2(emi2);
													logger.info("HomeLoanAction.java LN 1417 stateManagerBean.getState()==8 emi2::"+emi2);
												}
												if(request.getParameter("repaymentMonth1")!=null){
													repaymentMonth1 = Integer.parseInt(request.getParameter("repaymentMonth1"));
													logger.info("HomeLoanAction.java LN 1417 stateManagerBean.getState()==8 repaymentMonth1::"+repaymentMonth1);
												}
												if(request.getParameter("repaymentMonth2")!=null){
													repaymentMonth2 = Integer.parseInt(request.getParameter("repaymentMonth2"));
													logger.info("HomeLoanAction.java LN 1417 stateManagerBean.getState()==8 repaymentMonth2::"+repaymentMonth2);
												}
												if(emi1>0 && emi2>0){
													repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), irD, emi1,1,1,moratoriumMonths,1,product.getHlProductSliderAmtMul(),null,null,null,emi2,repaymentMonth1,repaymentMonth2);
												}else{
													repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), irD, emiD,1,1,moratoriumMonths,1,product.getHlProductSliderAmtMul(),null,null,null);
												}
											}
										else{
											if(appForm.getAppHomeLoanId()==12){
												repayments = SbiUtil.generateRepaymentList(appForm, (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()));
											}
											else{
												repayments = SbiUtil.generateRepaymentList(appForm.getAppLoanAmount(), (appForm.getAppProductTenureFlag()==2?appForm.getAppLoanTenure()*12:appForm.getAppLoanTenure()), irD, emiD,1,1,moratoriumMonths,1,product.getHlProductSliderAmtMul(),null,null,null);
											}
										}
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
					logger.info("HomeLoanAction.java LN 1080 stateManagerBean.getState()==8 ::", e);

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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId == null){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
						}
						appForm=homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
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
						logger.info("HomeLoanAction.java LNo : 1802 app.getAppOTPAttemptCount() : "+appForm.getAppEmailAttemptCount()+" with AppSeqId "+appSeqId);
						appForm = homeLoanService.save(appForm);
						
						responseMessage = "success|Quotes sent to email";
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1147 stateManagerBean.getState()==6 ::", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						if(appSeqId == null){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
							return "jsonResponsePage";
						}
						if(stateManagerBean.getState()==5 || stateManagerBean.getState()==3){
							if (!Constants.HL_CAPTCHA_BY_PASS) {
								responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha());
								if (responseMessage != null) {
									return "jsonResponsePage";
								}
							}
						}
						json = processManagerHomeImpl.processWantUsToCallYou(appSeqId, moduleId,stateManagerBean.getState(), (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),ajaxPostUrl, otherRequest);
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
							responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
							logger.info("HomeLoanAction.java LNo : 1923 encrypted value is : " + responseMessage + " for state " +stateManagerBean.getState());
						}
						return "jsonResponsePage";
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1183 stateManagerBean.getState()==3, 4, 5 :: "+stateManagerBean.getState()+ " :: ", e);
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
						appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
						if(request.getParameter("HLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
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
								if(request.getParameter("HLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
									stateManagerBean.setState(-1);
								}else{
									responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
									return "jsonResponsePage";
								}
							
							}else{
								HlProduct hlProduct= null;
								if(otherRequest!=null && otherRequest.getChosenProductId() !=null && !otherRequest.getChosenProductId().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanProductId(Integer.parseInt(otherRequest.getChosenProductId().toString()));
									hlProduct=commonService.getHomeLoanProductById(quote.getLoanQuoteLoanProductId());
								}else if(appForm.getAppHomeLoanId()!=null){
									hlProduct=commonService.getHomeLoanProductById(appForm.getAppHomeLoanId());
								}

								if(otherRequest!=null && otherRequest.getChosenTenure()!=null){
									chosenTenure =Integer.parseInt(otherRequest.getChosenTenure().toString());
									if(hlProduct!= null && hlProduct.getHlProductSliderTenure().intValue()==2){
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
								if(otherRequest!=null && otherRequest.getChosenLoanAccountType()!=null && !otherRequest.getChosenLoanAccountType().equalsIgnoreCase("")){
									quote.setLoanQuoteLoanAccountType(Integer.parseInt(otherRequest.getChosenLoanAccountType()));
					    		}else{
					    			quote.setLoanQuoteLoanAccountType(1);
					    		}

								
								loanScenarioBean=(LoanScenarioBean) processManagerHomeImpl.processGetQuotes(appSeqId, quote, visitId, ajaxPostUrl, bankLmsUser,loanTypeId);

								if(loanScenarioBean.getStatus() == 0 || loanScenarioBean.getStatus() == 2){
									responseMessage = loanScenarioBean.getMessage();
									if(request.getParameter("HLQuoteToken")!=null){
										stateManagerBean.setState(-1);
									}else{
										if(isDsrPage!=null && isDsrPage.equalsIgnoreCase("true")){
											responseMessage =  "error|"+loanScenarioBean.getMessage();
											return "jsonResponsePage";	
										}else{
											stateManagerBean.setState(0);
										}
									}
								}else{
									Map<Double, String> manualEligVal=SbiUtil.generateEligibiltyList(loanScenarioBean.getMinEligibility(), loanScenarioBean.getMaxEligibility(), loanScenarioBean.getProductSliderDigitExact(), 0.1f);
									loanScenarioBean.setManualEligVal(manualEligVal);
									Map<Integer, String> manualTenureVal= SbiUtil.generateTenureList(loanScenarioBean.getMinTenure(), loanScenarioBean.getMaxTenure(), loanScenarioBean.getProductSliderTenure()==1?true:false);
									loanScenarioBean.setManualTenureVal(manualTenureVal);
									if(appSeqId == null){
										appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
											logger.info("HomeLoanAction.java LN 1359 stateManagerBean.getState()==2 ::", e);
										}
									}
									if(request.getParameter("HLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
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
					logger.info("HomeLoanAction.java LN 1316 stateManagerBean.getState()==2 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
			
			if(stateManagerBean.getState()==1){
				try {
					SessionUtil.setHomeLoanState16("false");
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
						if(appSeqId == null){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						}
						if(isDsrPage.equals("false") && !quote.getInfoprovide().equals("on")){
							responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
							return "jsonResponsePage";
						}
						if(!Constants.HL_CAPTCHA_BY_PASS && ajaxPostUrl.equalsIgnoreCase("home-loan")){
							if(appSeqId==null){
								responseMessage = SbiUtil.checkCaptcha(quote.getCaptcha());
								if (responseMessage!=null) {
									return "jsonResponsePage";
								}
							}
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
						
						if(SessionUtil.getHomeLoanApplicationSequenceId()!=null){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null && appForm.getAppSubTypeId().equals(Constants.APP_APP_SUB_TYPE_ID_CBS) ){
								if(appForm.getAppQuoteId() != null){
									ApplicationFormHomeLoanQuote quoteOld = null;
									quoteOld = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());

									if(ValidatorUtil.isValid(quoteOld.getLoanQuotePreEMIs())){
										quote.setLoanQuotePreEMIs(quoteOld.getLoanQuotePreEMIs());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteOutstandingLoanAmount())){
										quote.setLoanQuoteOutstandingLoanAmount(quoteOld.getLoanQuoteOutstandingLoanAmount());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteYearExistingHomeLoanStartDate())){
										quote.setLoanQuoteYearExistingHomeLoanStartDate(quoteOld.getLoanQuoteYearExistingHomeLoanStartDate());
									}
									if(ValidatorUtil.isValid(quoteOld.getLoanQuoteMonthExistingHomeLoanStartDate())){
										quote.setLoanQuoteMonthExistingHomeLoanStartDate(quoteOld.getLoanQuoteMonthExistingHomeLoanStartDate());
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
						String alternateMob = quote.getAlternateMobileNumber() != null ? quote.getAlternateMobileNumber() : "";
						String altIsd = quote.getAppAltISDCode() != null ? quote.getAppAltISDCode() : "";
						
						//added for server Side Validation of Apsec point 
						String firstNameData=quote.getLoanQuoteAppFirstName();
						String mobilenum=quote.getLoanQuoteAppMobileNo() != null ? quote.getLoanQuoteAppMobileNo() : quote.getAppNRIMobileNo();
						logger.info("cheking the altIsd   "+altIsd);
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
								appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
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
								} catch(NullPointerException e){
									logger.info("HomeLoanAction.java LN 1501 stateManagerBean.getState()==1 ::", e);
								} 
							}
							if(request.getParameter("HLQuoteToken")!=null || request.getParameter("lead_id")!=null  || request.getParameter("app_id")!=null){
								if(ValidatorUtil.isValid(appForm.getAppReferenceId())){
									appReferencetIdEncrypted=aesEncryption.encrypt(appForm.getAppReferenceId());
									return "applicationTrack"+(uiType==null?"":uiType);
								}
								initHomeLoan(moduleId);
								jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
								SessionUtil.setCaptch(null);
								return  "secondPageWithHeader"+(uiType==null?"":uiType);
							}else{
								SessionUtil.setCaptch(null);
								return "secondPage"+(uiType==null?"":uiType);
							}
						}
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1458 stateManagerBean.getState()==1 ::", e);
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
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
							if(appSeqId != null){
								appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
								if(appForm!=null && ValidatorUtil.isValid(appForm.getAppReferenceId())){
									if(moduleId==Constants.HOME_LOAN_ID){
										Integer appRefIdHomeLoan = null;
										SessionUtil.setHomeLoanApplicationSequenceId(appRefIdHomeLoan);
										SessionUtil.setLeadId(null);
										appSeqId=null;
										lead=null;
									}
								}
							}
						}
						if(appSeqId==null){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
							
						}
						if(appSeqId!=null){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
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
						homeLoanPage = 1;
						populateFirstPageContent(Constants.HOME_LOAN_ID,1);
						jsonJSArray1HomeLoan = SbiUtil.populateJSValidation(1, moduleId).toString();
						populateForm(-1, appSeqId, moduleId);
						generateUIBeanListCommon();
						initHomeLoan(moduleId);
						includeJs=true;
						getDisplayUpdate((appSeqId!=null), (appForm!=null && appForm.getAppSubTypeId()!=null)?appForm.getAppSubTypeId():0, applicationTypeId );
						return "firstPage"+(uiType==null?"":uiType);
					
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
					
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1497 stateManagerBean.getState()==0 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
			
			if (stateManagerBean.getState()==-1) {
				try{
					if(stateManagerBean.getValidatorResponse().isStatus()){

						if(!Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")){
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
							if(isOnlineAndDsrActive){
								appSeqId=null;
								responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
							}
							if(appSeqId != null){
								appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
								if(request.getParameter("HLQuoteToken")!=null){
									String encyQuotId=aesEncryption.decrypt(request.getParameter("HLQuoteToken").toString());
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
										releaseSession(Constants.HOME_LOAN_ID);
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
							appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
						}
						if(isOnlineAndDsrActive){
							appSeqId=null;
							responseMessage = Constants.ALREADY_LOGGEDIN_REDIRECTION;
						}
						homeLoanPage = 1;
						if(appSeqId !=null){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())){
									
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
											releaseSession(Constants.HOME_LOAN_ID);
											appSeqId=null;
											appForm = null;
											quote = null;
										}
									}else{
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
											releaseSession(Constants.HOME_LOAN_ID);
											appSeqId=null;
											appForm = null;
											quote = null;
										}
									}
								}
							}
						}
						
						if(appSeqId !=null){
							appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
							if(appForm!=null){
								if(appForm.getAppSubTypeId() == Constants.APP_APP_SUB_TYPE_ID_CBS){
									if(isDsrPage.equalsIgnoreCase("true")){
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified())){
											releaseSession(Constants.HOME_LOAN_ID);
										}
									}else{
										if("N".equalsIgnoreCase(appForm.getAppMobileVerified()) && "N".equalsIgnoreCase(appForm.getAppEmailVerified())){
											releaseSession(Constants.HOME_LOAN_ID);
										}
									}
								}
								if(appForm.getAppApplyingFrom()==2){
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										SessionUtil.setISDCode(appForm.getAppISDCode());
//										logger.info("HomeLoanAction.java 2358..."+SessionUtil.getMobile());		
										
										//Added for displaying alt no in DSR page for UPLOAD functionality
										SessionUtil.setalternateMobileNumber(alternateMobileNo); 	
//										logger.info("HomeLoanAction.java 2360..."+SessionUtil.getalternateMobileNumber());
									}
								}else{
									if(appForm.getAppMobileNo()!=null){
										mobileNo = appForm.getAppMobileNo();
										SessionUtil.setMobile(appForm.getAppMobileNo());
										//logger.info("HomeLoanAction.java 2354..."+SessionUtil.getMobile());		
										
										//Added for displaying alt no in DSR page for UPLOAD functionality
										SessionUtil.setISDCode(appForm.getAppISDCode());
										alternateMobileNo = appForm.getAppAlternateMobileNumber(); 				
										//logger.info("HomeLoanAction.java 2356..."+alternateMobileNo+"....."+appForm.getAppAlternateMobileNumber()+"..ISD.."+appForm.getAppISDCode());	
										//logger.info("HomeLoanAction.java 2357..."+appForm.getAppAltISDCode());				
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
							//	logger.info("bankLmsUser.getLmsUserId())::::" + bankLmsUser.getLmsUserId());
								//mask PII data
								if(isDsrPage.equalsIgnoreCase("true")) {
									
									boolean isTeleCallerUser = false;
									isTeleCallerUser = commonService.isTeleCallerUserRole(bankLmsUser.getLmsUserId());
									logger.info("isTeleCallerUser::::" + isTeleCallerUser);
									
									if (isTeleCallerUser) {
										if (appForm.getAppMobileNo() != null)  {
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
								SessionUtil.setHomeLoanApplicationSequenceId(appSeqId);
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
						
						if (SessionUtil.getalternateMobileNumber()!=null) {
							alternateMobileNo=SessionUtil.getalternateMobileNumber();
//							logger.info("HomeLoanAction.java getalternateMobileNumber........"+SessionUtil.getalternateMobileNumber());
						}
						//logger.info("HomeLoanAction.java :: LNo 1665  " +SessionUtil.getSession());
						return "homePage"+(uiType==null?"":uiType);
					}else{
						String msg=CommonUtilites.serchingValuesFromMaps(stateManagerBean.getValidatorResponse().getErrorMessage());
						responseMessage = "error|"+msg;
						return "jsonResponsePage";
					}
				} catch (NullPointerException e) {
					logger.info("HomeLoanAction.java LN 1593 stateManagerBean.getState()==-1 ::", e);
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				} 
			}
		} catch(SQLException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		} catch(UnsupportedEncodingException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		}catch(FileNotFoundException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		}catch(ParseException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		}catch(RuntimeException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
		}catch(JSONException e){
			logger.info("HomeLoanAction.java :: getHomeLoan(Integer moduleId) ", e);
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
					if(appForm!=null){
						quote= homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
					}
				}
				if(quote!=null){
					generateUIBeanList(moduleId);
				}else{
					this.appSeqId=null;
					Integer appRefIdHomeLoan = null;
					//String appSefIdHomeLoan = null;
					SessionUtil.setHomeLoanApplicationSequenceId(appRefIdHomeLoan);
					//SessionUtil.setHomeLoanApplicationSequenceId(appSefIdHomeLoan);
				}
			} else {
				generateUIBeanList(moduleId);
			}
		} catch (NullPointerException e) {
			logger.info("HomeLoanAction.java LNo :: populateForm(Integer pageNo, Integer appSeqId, Integer moduleId) ", e);
		} catch (SQLException e) {
			logger.info("HomeLoanAction.java LNo :: populateForm(Integer pageNo, Integer appSeqId, Integer moduleId) ", e);
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
			logger.info("HomeLoanAction.java LNo :: getIntermediary(Integer moduleId) ", e);
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
			logger.info("HomeLoanAction.java LNo :: initHomeLoan(Integer moduleId) ", e);
			initLoanJSONArrayHomeLoan.put("error");
		} 
	}

	private void getCallLogs(Integer appSeqId, Integer appLeadId){
		try{
			if(ValidatorUtil.isValid(appSeqId) || ValidatorUtil.isValid(appLeadId)){
				callLogDetails = commonService.getCallLogByLeadAppId(Constants.HOME_LOAN_ID, appSeqId, appLeadId  );
			}
		} catch(NullPointerException e){
			logger.info("HomeLoanAction.java :: getCallLogs(appSeqId) ", e);
		} catch(SQLException e){
			logger.info("HomeLoanAction.java :: getCallLogs(appSeqId) ", e);
		}
	}
	
	private void generateUIBeanList(int pageNo){
		try{
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
						if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
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
				
				if(appForm!=null && appForm.getAppCoapplicantState_id_1()!=null){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppCoapplicantState_id_1(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Coapplicant1(mapCity);
						if(appForm.getAppCoapplicantCity_id_1()!=null && appForm.getAppCoapplicantCity_id_1().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictsCoapplicant1(mapDistrict);
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
				
				if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==4){
					Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
					maps.put(2, "PDC");
					maps.put(1, "Check-off");
					maps.put(6, "SI linked to Salary Account");
					maps.put(7, "SI linked to Other Account");
					maps.put(8, "Other");
					beanList.setModeOfRepayment(maps);
					
					List <MasterRelationshipWithBank>relationshipWithBanks = commonService.getAllRelationshipWithBank(Constants.HOME_LOAN_ID);
					Map<Integer, String> mapRelation = new LinkedHashMap<Integer, String>();
					if(relationshipWithBanks!=null){
						for (MasterRelationshipWithBank masterRelationshipWithBank : relationshipWithBanks) {
							mapRelation.put(masterRelationshipWithBank.getRelationshipId(), masterRelationshipWithBank.getRelationshipName());
						}
					}
					beanList.setRelationshipWithBank(mapRelation);
					logger.info("HLAction.java LNo:: 2566 :: generateUIBeanList() relationshipWithBanks done");
				}
			}else if(pageNo==4){
				if(appForm!=null && appForm.getAppPickupStateId()!=null && appForm.getAppPickupStateId() >0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppPickupStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1Pickup(mapCity);
						if(appForm.getAppPickupCityId()!=null && appForm.getAppPickupCityId().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictPickup(mapDistrict);
						}
				}
			}
			
			if(pageNo==3 || pageNo==4){
				if(appForm!=null && appForm.getAppStateId()!=null && appForm.getAppStateId() > 0){
					Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
					mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					Map<Integer, String> mapDistrict=new LinkedHashMap<Integer, String>();
					mapDistrict = commonService.getStateCityDistrictBranch(3, Constants.HOME_LOAN_ID, appForm.getAppStateId(), null, null, null, null, null, null, null);
					if(mapDistrict!=null && !mapDistrict.isEmpty()){
						mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
					}
					beanList.setCitiesoptgrp1(mapCity);
						if(appForm.getAppDistrictId()!=null  && appForm.getAppDistrictId() >0){
							beanList.setDistricts(mapDistrict);
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
						if(appForm.getAppOfficeCityId()!=null && appForm.getAppOfficeCityId().equals(Constants.OTHER_ID_INTEGER)){
							beanList.setDistrictsOffice(mapDistrict);
						}
				}
			}
		} catch(NullPointerException e){
			logger.info("HomeLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoan appForm, int pageNo) ", e);
		} catch(SQLException e){
			logger.info("HomeLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoan appForm, int pageNo) ", e);
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
			if (quote!=null && quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId()==4) {
				maps.remove(4);
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
				for (int index = currentMonths; index<(Constants.month.length-1); index++  ) {
					maps.put(index+1, Constants.month[(index+1)]);
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
			if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH || Constants.BANK_ID == Constants.BANK_ID_SBT){			
				maps.put(2, "NRI/PIO Holder");
			}
			beanList.setResidentTypes(maps);
			maps = null; 
			maps = new LinkedHashMap<Integer, String>();
			maps.put(1, "Resident Indian");
			if(Constants.BANK_ID == Constants.BANK_ID_SBI || Constants.BANK_ID == Constants.BANK_ID_SBH || Constants.BANK_ID == Constants.BANK_ID_SBT){
				maps.put(2, "NRI/PIO Holder");
			}
			beanList.setResidentTypesCoApplicant(maps);
		} catch (NullPointerException e) {
			logger.info("HomeLoanAction.java :: generateUIBeanListCommon() ", e);
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
				boolean needToShowHLTopup=false;
				if(bankLmsUser!=null){
					contactCenterLmsUser = commonService.getBankLmsUserRole(bankLmsUser.getLmsUserId());
					if(contactCenterLmsUser){
						if(appForm!=null && appForm.getAppSeqId()!=null 
								&& appForm.getAppSubTypeId()!=null && Constants.APP_APP_SUB_TYPE_ID_CBS.equals(appForm.getAppSubTypeId())){
							MasterCBSResponse masterCBSResponse =  homeLoanService.getMasterCBSResponseObjectByLoanTypeAppSeqId(appForm.getAppSeqId());
							if(masterCBSResponse!=null && masterCBSResponse.getCbsResponseId()!=null){
								MasterCBSCall masterCBSCall = homeLoanService.getMasterCBSCallObjectByCbsResponseId(masterCBSResponse.getCbsResponseId());
								if(masterCBSCall!=null && masterCBSCall.getCbsTypeOfRelationship()!=null && masterCBSCall.getCbsTypeOfRelationship().intValue() == 1){
									needToShowHLTopup = true;
								}
							}
						}
					}
				}
				for (MasterLoanPurpose purpose : loanpurposes) {
					if(purpose.getLpId().intValue()==5 && !needToShowHLTopup){
						
					}else{
						maps.put(purpose.getLpId(), purpose.getLpTypeValue());
					}
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
				
				if(quote.getLoanQuotePreferredCityId()!=null && quote.getLoanQuotePreferredCityId().equals(Constants.OTHER_ID_INTEGER)){
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
					if(quote.getLoanQuotePrferredBranchId()!=null){
						if(quote.getLoanQuoteLoanPurposeId()!=null && quote.getLoanQuoteLoanPurposeId() ==4){
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), quote.getLoanQuotePreferredCityId(), null, 1, null, null, null, null));
						}else{
							beanList.setBranches(commonService.getStateCityDistrictBranch(4, Constants.HOME_LOAN_ID, quote.getLoanQuotePreferredStateId(), quote.getLoanQuotePreferredCityId(), null, null, null, null, null, null));
						}
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
			logger.info("HomeLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoanQuote quote, Integer moduleId) ", e);
		} catch (SQLException e) {
			logger.info("HomeLoanAction.java :: generateUIBeanList(ApplicationFormHomeLoanQuote quote, Integer moduleId) ", e);
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

	public static String getJsonJSArray3HomeLoan() {
		return jsonJSArray3HomeLoan;
	}

	public static void setJsonJSArray3HomeLoan(String jsonJSArray3HomeLoan) {
		HomeLoanAction.jsonJSArray3HomeLoan = jsonJSArray3HomeLoan;
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

  

}



<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
 "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
 "http://struts.apache.org/dtds/struts-2.0.dtd">

<!-- Home Loan action -->

<struts>
	<package name="home-loan-actions" extends="sbi">
		<action name="inithomeloan" method="initHomeLoan" class="homeLoanAction">
			<result name="success" type="json">
				<param name="root">json</param>
			</result>
		</action>
		
		<action name="home-loan" method="homeLoan" class="homeLoanAction">
			<!-- <interceptor-ref name="token"></interceptor-ref> --> 
			<interceptor-ref name="defaultStack"></interceptor-ref>
			<interceptor-ref name="defaultFileUploadStack"></interceptor-ref>
			<result name="homePageOld">/app/loan/homeloan/homePage.jsp</result>
			<result name="firstPageOld">/app/loan/homeloan/homeFirstPage.jsp</result>
			<result name="secondPageOld">/app/loan/homeloan/homeSecondPageScript.jsp</result>
			<result name="secondPageChangeOld">/app/loan/homeloan/homeSecondPage.jsp</result>
			<result name="thirdPageOld">/app/loan/homeloan/homeThirdPage.jsp</result>
			<result name="thankYouPageOld">/app/common/loanThankYou.jsp</result>
			<result name="jsonResponsePage">/app/common/jsonResponsePage.jsp</result>
			<result name="repaymentScheduledOld">/app/common/repaymentScheduled.jsp</result>
			<result name="coapplicantdataOld">/app/loan/homeloan/homeCoapplicantData.jsp</result>
			<result name="secondPageWithHeaderOld">/app/loan/homeloan/homeSecondPageWithHeader.jsp</result>
			<result name="dropOffOld">/app/loan/homeloan/homeDropOff.jsp</result>
			<result name="downLoadPDF" type="stream">
			  <param name="contentType">application/pdf</param>
			  <param name="allowCaching">false</param>
				<param name="inputName">inputStream</param>
				<param name="contentDisposition">filename=Home-Loan.pdf</param> 
				<param name="bufferSize">1024</param> 
			</result>
			<result name="cbsOtpPageOld">/app/common/commonCbsOtp.jsp</result>
			
			<!-- UI NEW  -->
			<result name="homePage">/appNew/loan/homeloan/HomeLoan.jsp</result>
			<result name="firstPage">/appNew/loan/homeloan/HomeFirstPage.jsp</result>
			<result name="secondPage">/appNew/loan/homeloan/HomeFirstPage.jsp</result>
			<result name="secondPageChange">/appNew/loan/homeloan/HomeFirstPage.jsp</result>
			<result name="thirdPage">/appNew/loan/homeloan/HomeFirstPage.jsp</result>
			<result name="thankYouPage">/appNew/loan/homeloan/HomeFirstPage.jsp</result>
			<result name="repaymentScheduled">/appNew/common/RepaymentScheduled.jsp</result>
			<result name="secondPageWithHeader">/appNew/loan/homeloan/HomeSecondPageWithHeader.jsp</result>
			<result name="dropOff">/appNew/loan/homeloan/HomeDropOff.jsp</result>
			<result name="cbsOtpPage">/appNew/common/CommonCbsOtp.jsp</result>
			<result name="repaymentScheduled">/appNew/common/RepaymentScheduled.jsp</result>
		</action>

		<action name="home-loan-dsr" method="homeLoanDSR" class="homeLoanAction">
			<!-- <interceptor-ref name="token"></interceptor-ref> --> 
			<interceptor-ref name="defaultStack"></interceptor-ref>
			<interceptor-ref name="defaultFileUploadStack"></interceptor-ref> 
			<result name="homePageOld">/app/dsr/homeloan/dsrHomeLoan.jsp</result>
			<result name="firstPageOld">/app/dsr/homeloan/dsrFirstPage.jsp</result>
			<result name="secondPageOld">/app/dsr/homeloan/dsrSecondPageScript.jsp</result>
			<result name="secondPageChangeOld">/app/dsr/homeloan/dsrSecondPage.jsp</result>
			<!-- <result name="thirdPageOld">/app/dsr/homeloan/dsrThirdPage.jsp</result> -->
			<result name="thirdPageOld">/app/loan/homeloan/homeThirdPage.jsp</result>
			<result name="thankYouPageOld">/app/common/dsrThankYou.jsp</result>
			<result name="jsonResponsePage">/app/common/jsonResponsePage.jsp</result>
			<result name="secondPageWithHeaderOld">/app/dsr/homeloan/dsrSecondPageWithHeader.jsp</result>
			<result name="callsLogDetailsOld">/app/common/callLogDetails.jsp</result>
			<result name="repaymentScheduledOld">/app/common/repaymentScheduled.jsp</result>
			<result name="downLoadPDF" type="stream">
			  <param name="contentType">application/pdf</param>
			  	<param name="allowCaching">false</param>
				<param name="inputName">inputStream</param>
				<param name="contentDisposition">filename=Home-Loan.pdf</param> 
				<param name="bufferSize">1024</param> 
			</result>
			<result name="cbsVerifySmsOtpPageOld">/app/common/commonCbsVarifySmsOtp.jsp</result>
		</action>
		<action name="getAllProject" method="getAllProject" class="homeLoanAction">
			<result name="success" type="json">
       	 		<param name="root">json</param>
       		</result>
		</action>
	</package>
</struts>	



<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" version="2.5">
  <display-name>www.sbi.co.in</display-name>
  <listener>
        <listener-class>org.apache.logging.log4j.web.Log4jServletContextListener</listener-class>
    </listener>
  <listener>
    <listener-class>
			org.springframework.web.context.ContextLoaderListener
	</listener-class>
  </listener>
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/applicationContext.xml</param-value>
  </context-param>
   <filter>
        <filter-name>log4jServletFilter</filter-name>
        <filter-class>org.apache.logging.log4j.web.Log4jServletFilter</filter-class>
    </filter>
   <filter>
    <filter-name>xssFilter</filter-name>
	<filter-class>
			com.mintstreet.common.security.XSSFilter</filter-class>
  </filter>
  <filter>
    <filter-name>cacheControlFilter</filter-name>
	<filter-class>
			com.mintstreet.common.security.CacheControlFilter</filter-class>
  </filter>
   <filter-mapping>
        <filter-name>log4jServletFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
        <dispatcher>FORWARD</dispatcher>
        <dispatcher>INCLUDE</dispatcher>
        <dispatcher>ERROR</dispatcher>
        <!-- <dispatcher>ASYNC</dispatcher> --><!-- Servlet 3.0 w/ disabled auto-initialization only; not supported in 2.5 -->
    </filter-mapping>
  <filter-mapping>
    <filter-name>xssFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <filter-mapping>
    <filter-name>cacheControlFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <filter>
    <filter-name>jpaFilter</filter-name>
    <filter-class>
			org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>jpaFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <filter>
    <filter-name>struts2</filter-name>
    <filter-class>
			org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter
	</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>struts2</filter-name>
    <url-pattern>/</url-pattern>
	<url-pattern>/changelanguage</url-pattern>
	<url-pattern>/home-faq</url-pattern>
	<url-pattern>/contact-us</url-pattern>
	<url-pattern>/privacy-statement</url-pattern>
	<url-pattern>/disclosure-statement</url-pattern>
	<url-pattern>/term-condition</url-pattern>
	<url-pattern>/under-maintainance</url-pattern>
	<url-pattern>/home</url-pattern>
	<url-pattern>/residancetypeonchange</url-pattern>
	<url-pattern>/citybystateid</url-pattern>
	<url-pattern>/getCitiesForHomeLoan</url-pattern>
	<url-pattern>/getDistrictByStateId</url-pattern>
	<url-pattern>/pinCodeByCityId</url-pattern>
	<url-pattern>/pinCodeByDistrictId</url-pattern>
	<url-pattern>/disticByStateid</url-pattern>
	<url-pattern>/branchByStateId</url-pattern>
	<url-pattern>/branchByDistrictId</url-pattern>
	<url-pattern>/branchByCityId</url-pattern>
	<url-pattern>/localityordistrictbystateandcityid</url-pattern>
	<url-pattern>/getallbank</url-pattern>
	<url-pattern>/loanPurposeByParentId</url-pattern>
	<url-pattern>/getAllEmployer</url-pattern>
	<url-pattern>/getAllBranchName</url-pattern>
	<url-pattern>/getAllEmployerForPL</url-pattern>
	<url-pattern>/getAllEmployerForXpressCreditIT</url-pattern>
	<url-pattern>/getAllBuilder</url-pattern>
	<url-pattern>/getPreownedCarDealer</url-pattern>
	<url-pattern>/getRequestConfigByLoanType</url-pattern>
	<url-pattern>/getTypeOfSalaryPackage</url-pattern>
	<url-pattern>/getTypeOfSalaryPackageRank</url-pattern>
	<url-pattern>/flexi-pay-emis</url-pattern>
	<url-pattern>/campaign-lead</url-pattern>
	<url-pattern>/sms-consent</url-pattern>
	<url-pattern>/infronics-Lead</url-pattern>
	<url-pattern>/ivrs-otp</url-pattern>
	<url-pattern>/mingle-lead</url-pattern>
	<url-pattern>/ios-url</url-pattern>
	<url-pattern>/home-about-us</url-pattern>
	<url-pattern>/agri-loan-actions</url-pattern>
	<!-- <url-pattern>/agri-loan</url-pattern> -->
	<url-pattern>/agri-loan-dsr</url-pattern>
	<url-pattern>/agriMake</url-pattern>
	<url-pattern>/agriModel</url-pattern>
	<url-pattern>/application-status</url-pattern>
	<url-pattern>/getapplicationtrack</url-pattern>
	<url-pattern>/auto-loan</url-pattern>
	<url-pattern>/auto-loan-dsr</url-pattern>
	<url-pattern>/bikeCompanyByBikeTypeId</url-pattern>
	<url-pattern>/bikeModelByCompanyId</url-pattern>
	<url-pattern>/bikeVariantByBikeModuleId</url-pattern>
	<url-pattern>/carVariantByCarModuleId</url-pattern>
	<url-pattern>/carModelByCompanyId</url-pattern>
	<url-pattern>/occupationByEmployementTypeID</url-pattern>
	<url-pattern>/exshowRoomPriceByCarVariantId</url-pattern>
	<url-pattern>/getDealerByCompanyId</url-pattern>
	<url-pattern>/initiateProcess</url-pattern>
	<url-pattern>/sendOTP</url-pattern>
	<url-pattern>/reSendOTP</url-pattern>
	<url-pattern>/verifyOTP</url-pattern>
	<url-pattern>/getLoanQuote</url-pattern>
	<url-pattern>/getSliderQuote</url-pattern>
	<url-pattern>/applyApplication</url-pattern>
	<url-pattern>/finalFormSubmit</url-pattern>
	<url-pattern>/downloadPdf</url-pattern>
	<url-pattern>/submitAppTrack</url-pattern>
	<url-pattern>/call-rule-engine</url-pattern>
	<url-pattern>/call-pdf-engine</url-pattern>
	<url-pattern>/call-nsdl-engine</url-pattern>
	<url-pattern>/getCallbackMasters</url-pattern>
	<url-pattern>/call-intial-token</url-pattern>
	<url-pattern>/getMasters</url-pattern>
	<url-pattern>/getModel</url-pattern>
	<url-pattern>/getVariant</url-pattern>
	<url-pattern>/getCity</url-pattern>
	<url-pattern>/getDistrict</url-pattern>
	<url-pattern>/getBranches</url-pattern>
	<url-pattern>/getAllStates</url-pattern>
	<url-pattern>/getEmployerList</url-pattern>
	<url-pattern>/finalAppointment</url-pattern>
	<url-pattern>/getCallBack</url-pattern>
	<url-pattern>/reSendOTPCallBack</url-pattern>
	<url-pattern>/callBackVerifyOTP</url-pattern>
	<url-pattern>/getAppRefStatus</url-pattern>
	<url-pattern>/getDocuments</url-pattern>
	<url-pattern>/getCBSCall</url-pattern>
	<url-pattern>/resendOTPCBS</url-pattern>
	<url-pattern>/verifyOTPCBS</url-pattern>
	<url-pattern>/setDeviceInfo</url-pattern>
	<url-pattern>/encrypt</url-pattern>
	<url-pattern>/credit-card</url-pattern>
	<url-pattern>/credit-card-dsr</url-pattern>
	<url-pattern>/generate-missing-pdf</url-pattern>
	<url-pattern>/edvantage-loan</url-pattern>
	<url-pattern>/scholar-loan</url-pattern>
	<url-pattern>/education-loan-dsr</url-pattern>
	<url-pattern>/instituteByCourseType</url-pattern>
	<url-pattern>/instituteCategoryType</url-pattern>
	<url-pattern>/getAllCourseName</url-pattern>
	<url-pattern>/getAllInstituteName</url-pattern>
	<url-pattern>/getAllUniversity</url-pattern>
	<url-pattern>/initeducationloan</url-pattern>
	<url-pattern>/education-takeover-loan</url-pattern>
	<url-pattern>/instituteByCourseType</url-pattern>
	<url-pattern>/instituteCategoryType</url-pattern>
	<url-pattern>/getAllCourseName</url-pattern>
	<url-pattern>/getAllInstituteName</url-pattern>
	<url-pattern>/getAllUniversity</url-pattern>
	<url-pattern>/sbi-flipkart</url-pattern>
	<url-pattern>/check-eligibility</url-pattern>
	<url-pattern>/verify-Otp</url-pattern>
	<url-pattern>/resend-Otp</url-pattern>
	<url-pattern>/inithomeloan</url-pattern>
	<url-pattern>/home-loan</url-pattern>
	<url-pattern>/home-loan-dsr</url-pattern>
	<url-pattern>/getAllProject</url-pattern>
	<url-pattern>/inithomeloan</url-pattern>
	<url-pattern>/home-top-up-loan</url-pattern>
	<url-pattern>/getAllProject</url-pattern>
	<url-pattern>/initpensionloan</url-pattern>
	<url-pattern>/pension-loan</url-pattern>
	<url-pattern>/getInstituteCategoryByName</url-pattern>
	<url-pattern>/getEmployerByName</url-pattern>
	<url-pattern>/personal-loan-actions</url-pattern>
	<url-pattern>/personal-loan</url-pattern>
	<url-pattern>/personal-loan-dsr</url-pattern>
	<url-pattern>/pension-loan</url-pattern>
	<url-pattern>/getInstituteCategoryByName</url-pattern>
	<url-pattern>/getInstituteCategoryByName</url-pattern>
	<url-pattern>/getEmployerByName</url-pattern>
	<url-pattern>/campaign-home</url-pattern>
	<url-pattern>/sbi-tata</url-pattern>
	<url-pattern>/confirm-Otp</url-pattern>
	<url-pattern>/resend-Otp</url-pattern>
	<url-pattern>/downloadSbiTataPdf</url-pattern>
	<url-pattern>/upload-vidya-lakshmi-loan</url-pattern>
	<url-pattern>/download-vidya-lakshmi-loan</url-pattern>
	<!-- <url-pattern>/vlp-file-list</url-pattern>
	<url-pattern>/vlp-get-file</url-pattern>
	<url-pattern>/vlp-status-report</url-pattern>
	<url-pattern>/vlp-status-upload</url-pattern>
	<url-pattern>/vlp-process-file</url-pattern> -->
	<url-pattern>/subDistrictByDistrictId</url-pattern>
	<url-pattern>/villageBysubdistrictId</url-pattern>
	<url-pattern>/uploadimage.action</url-pattern>
	<url-pattern>/Captcha/*</url-pattern>
	<url-pattern>/Captcha.jpg</url-pattern>
	<url-pattern>/SBI_LOS/rest</url-pattern>
	<url-pattern>/encrypt</url-pattern>
	<url-pattern>/decrypt</url-pattern>
	<url-pattern>/docUpload</url-pattern>
	<url-pattern>/getInstituteList</url-pattern>
	<url-pattern>/getAllProjects</url-pattern>
	<url-pattern>/quoteEmailData</url-pattern>
	<url-pattern>/getAgriState</url-pattern>
	<url-pattern>/getAgriDistrict</url-pattern>
	<url-pattern>/customer-feedback</url-pattern>
	<url-pattern>/customer-feedback-page</url-pattern>
	<url-pattern>/resend-Otp</url-pattern>
	<url-pattern>/customer-feedback-submit</url-pattern>
	<url-pattern>/getCardEmployerName</url-pattern>
	<url-pattern>/getAgriCityList</url-pattern>
	<url-pattern>/bulkUpload</url-pattern>
	<!-- url mappings for instant loans start -->
	<url-pattern>/pushNotifications</url-pattern>
	<url-pattern>/sendEmailAlerts</url-pattern>
	<url-pattern>/readIdspmCsv</url-pattern>
	<url-pattern>/readIdspmCsvOnDate</url-pattern>
	<url-pattern>/sendSmsAlerts</url-pattern>
	<url-pattern>/downloadIDSPMDump</url-pattern>
	<url-pattern>/sendEmailISDPMDump</url-pattern>
	
	<url-pattern>/callCreditVidyaService</url-pattern>
	<url-pattern>/callCreditVidyaBREService</url-pattern>
	
	<url-pattern>/getKeyAndServiceId</url-pattern>
	<url-pattern>/validateUserINB</url-pattern>
	<url-pattern>/validateUserATM</url-pattern>
	<url-pattern>/createLoanAccountInCBS</url-pattern>
	
	<url-pattern>/unsubscribeForNotifications</url-pattern>
	<url-pattern>/update-constants</url-pattern>
    <!-- url mappings for instant loans end -->
	<!-- Work for Campaign Loan start -->
	<url-pattern>/home-loan-callback</url-pattern>
	<url-pattern>/car-loan-callback</url-pattern>
	<url-pattern>/global-ed-callback</url-pattern>
	<url-pattern>/agri-loan-callback</url-pattern>
	<!-- Work for Campaign Loan end -->
	<url-pattern>/bidyalakhmi-loan</url-pattern>
	<url-pattern>/logout</url-pattern>
	<url-pattern>/get-feedback-url</url-pattern>
	<url-pattern>/getString</url-pattern>
	<url-pattern>/call-click2call-service</url-pattern>
	<url-pattern>/gold-loan</url-pattern>
	<url-pattern>/sendSmsAlertsByPk</url-pattern>
	<url-pattern>/quick-lead</url-pattern>
	<url-pattern>/cve</url-pattern>
	<url-pattern>/cve-loan-actions</url-pattern>
    <dispatcher>FORWARD</dispatcher>
    <dispatcher>REQUEST</dispatcher>
  </filter-mapping>
  <listener>
    <listener-class>
			com.mintstreet.common.session.SessionSendMailListener
	</listener-class>
  </listener>
  <servlet>
    <servlet-name>Captcha</servlet-name>
    <servlet-class>com.mintstreet.common.util.ImageCaptcha</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Captcha</servlet-name>
    <url-pattern>/Captcha.jpg</url-pattern>
  </servlet-mapping>
  <error-page>
    <error-code>404</error-code>
    <location>/app/common/error404.jsp</location>
  </error-page>
  <error-page>
    <error-code>500</error-code>
    <location>/app/common/error404.jsp</location>
  </error-page>
  <error-page>
    <error-code>502</error-code>
    <location>/app/common/error404.jsp</location>
  </error-page>
  <error-page>
    <error-code>503</error-code>
    <location>/app/common/error404.jsp</location>
  </error-page>
  <session-config>
    <session-timeout>10</session-timeout>
  </session-config>
  <welcome-file-list>
    <welcome-file>home</welcome-file>
  </welcome-file-list>
</web-app>




<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE struts PUBLIC
 "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN"
 "http://struts.apache.org/dtds/struts-2.0.dtd">
<struts>
	<constant name="struts.objectFactory" value="spring" />
	<constant name="struts.devMode" value="false" />
	<constant name="struts.ognl.allowStaticMethodAccess" value="true"/>
	<constant name="struts.multipart.maxSize" value="16777216"/>
	<include file="struts-interceptor.xml" />
	<package name="sbi" extends="sbi-interceptor">
		<default-action-ref name="home" />

		<global-results>
			<result name="home" type="redirect">/</result>
			<result name="originator" type="redirect">home</result>
			<result name="under-maintainance" type="redirect">under-maintainance</result>
			<result name="exception">/app/common/exception.jsp?mode=debug</result>
			<result name="404">/app/common/error404.jsp</result>
			<!-- <result name="error-action">/app/common/error404.jsp</result> -->
			<result name="applicationTrack" type="redirect">
				<param name="location">application-status</param>
				<param name="appReferencetIdEncrypted">${appReferencetIdEncrypted}</param>
				<param name="responseMessage">${responseMessage}</param>
			</result>
			<result name="applicationTrackNew" type="redirect">
				<param name="location">application-status</param>
				<param name="appReferencetIdEncrypted">${appReferencetIdEncrypted}</param>
				<param name="responseMessage">${responseMessage}</param>
			</result>
		</global-results>

		<global-exception-mappings>
			<exception-mapping exception="java.lang.Exception" result="exception" />
		</global-exception-mappings>
		<action name="uploadimage" method="upload" class="fileUploadAction">
			<interceptor-ref name="fileUpload">
				<param name="maximumSize">3145728</param>
				<param name="allowedTypes">
					image/pjpeg,image/jpeg,image/jpg,image/png,image/gif,application/pdf
				</param>
			</interceptor-ref>
			<interceptor-ref name="defaultFileUploadStack"></interceptor-ref>
		</action>
	</package>
	<include file="struts-generic-actions.xml"/>
	<include file="struts-common-loan.xml"/>
	<include file="struts-application-track-actions.xml"/>
	<include file="struts-home-loan-actions.xml"/>
	<include file="struts-home-topup-loan-actions.xml"/>
	<include file="struts-auto-loan-actions.xml"/>
	<include file="struts-education-loan-actions.xml"/>
	<include file="struts-personal-loan-actions.xml"/>	
	<include file="struts-vidya-lakshmi-loan-actions.xml"/>
	<include file="struts-cron-action.xml"/>
	<include file="struts-sbi-campaign.xml"/>
	<include file="struts-common-loan-web-service.xml"/>
	<include file="struts-agri-loan-actions.xml"/>
	<include file="struts-credit-card-actions.xml"></include>
	<include file="struts-flipkart-microsite.xml"/>
	<include file="struts-customer-feedback-action.xml"/>
	<include file="struts-instant-loan-action.xml"/>
	<include file="struts-campaign-callback.xml"/>
	<include file="struts-cve.xml"/>
</struts>
