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

	

}

