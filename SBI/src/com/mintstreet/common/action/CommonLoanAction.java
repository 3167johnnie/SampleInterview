package com.mintstreet.common.action;
import java.io.ByteArrayInputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.result.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.UIBeanListStatic;
import com.mintstreet.common.bo.impl.ProcessManagerImpl;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.state.StateManagerBean;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.ConstantUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.Security;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.uploadLead.entity.UploadBean;

public class CommonLoanAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(CommonLoanAction.class.getName());
	private static final long serialVersionUID = 1L;
	@Autowired
	private ProcessManagerImpl processManagerImpl;
	
	
	private Integer stateId;
	private Integer residentType;
	private Integer districtId;
	private Integer cityId;
	private Integer keyValue;
	private String keyId;
	private String elementId;
	private Integer lpParentId;
	private String name;
	private String mobile;
	private String token;
	
	private Integer loanPurposeId;
	private Integer isRACPC;
	private Integer isScholar;
	private String institutdeName;
	private Integer preferredLocation;
	private Integer preferredLoanLocation;
	private Integer pensionPayingLocation;
	private String instituteName;

	private Integer onChangeValue;
	private Integer isNRI;
	private Integer isEdvantage;
	private Integer isBidyaLakhmi;

	protected ArrayList<UploadBean> quickLead;
	
	//protected PrivacyRequest privacyRequest;
	
	
	
	//protected PrivacyRequest privacyLead;
	 
	//protected  MingleBean sbiMingleLead;
	
	private String isXpressCreditIT;

	protected String sms_campaign;
	private Integer isAgri;
	
	
	public String execute() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String homeAboutUs() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String homeFaq() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String contactUs() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	
	public String privacyStatement() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String disclosure() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String termAndCondition() {
		isForFaqAndContactUs=1;
		return SUCCESS+(uiType==null?"":uiType);
	}
	public String underMaintainance() throws SQLException {

		SessionUtil.setBankLMSUser(null);
		
		if(!UIBeanListStatic.isDataSet){
			setStaticData();
		}
		request=RequestUtil.getServletRequest();
		if(!ValidatorUtil.isValid(sourceId)){
			sourceId=1;
		}
		StateManagerBean stateManagerBean= stateManager.getState(request, Constants.HOME_ID);
		if(stateManagerBean.getState()==-1){
			jsonJSArray = SbiUtil.populateJSValidation(1, Constants.HOME_ID).toString();
			Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
			mapCity = commonService.getStateCityDistrictBranchWithoutLoanType(2, null, null, null, null, null, null, null, null, null);
			mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
			beanList.setCitiesoptgrp1(mapCity);
			UIBeanListStatic.setCitiesCard(commonService.getCititesSCC(Constants.CREDIT_CARD_ID));
			UIBeanListStatic.setCardCityGroup1(commonService.getcitiesSCCMetro(Constants.CREDIT_CARD_ID));
			
			campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.HOME_ID );
		}
		return SUCCESS+(uiType==null?"":uiType);
	}
	
	public String home() throws SQLException {
   
		if(ValidatorUtil.isValid(uiType)){SessionUtil.setUiType(uiType);}else{SessionUtil.setUiType(null);}
		request=RequestUtil.getServletRequest();
		if(iPAddressForAppAndDBServerPass!=1){
			isValidIpAddressForAppAndDBServer();
		}
		if(iPAddressForAppAndDBServerPass==0){
			return "under-maintainance"+(uiType==null?"":uiType);
		}
		SessionUtil.setBankLMSUser(null);
		
		if(!UIBeanListStatic.isDataSet){
			setStaticData();
		}
		JSONObject json = new JSONObject();
		
		if(!ValidatorUtil.isValid(sourceId)){
			sourceId=1;
		}
		
		ajaxPostUrl="home";
		stateManagerBean = stateManager.getState(request, Constants.HOME_ID);
		
		if(SessionUtil.getVisitIdHomePage()!=null){
			visitId = SessionUtil.getVisitIdHomePage();
		}else{
			if(stateManagerBean.getState()==-1){
				visitId = campaignManager.getCampaignId(null, source, se, cp, ag, sourceId, Constants.HOME_ID );
				if(ValidatorUtil.isValid(visitId)){
					SessionUtil.setVisitIdHomePage(visitId);
				}else{
					logger.info("CommonLoanAction.java LN 132 unable to insert into visit entity.");
					responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
					return "jsonResponsePage";
				}
			}
		}
		if(stateManagerBean.getState()==3 || stateManagerBean.getState()==4 || stateManagerBean.getState()==5){
			try{
				
				if(stateManagerBean.getValidatorResponse().isStatus()){
					String inputOtp = null;
					if(stateManagerBean.getState()==3){    
						lead = stateManagerBean.getLead();
						responseMessage = SbiUtil.checkCaptcha(lead.getCaptcha() != null ? lead.getCaptcha() : "");
						if (responseMessage!=null) {
							return "jsonResponsePage";
						}
					}else if(stateManagerBean.getState()==5){
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						responseMessage = SbiUtil.checkCaptcha(otherRequest.getCaptcha() != null ? otherRequest.getCaptcha() : "");
						if (responseMessage!=null) {
							return "jsonResponsePage";
						}
						inputOtp=stateManagerBean.getOtherRequest().getInputOtpWantUs();
						lead = commonService.getLeadById(SessionUtil.getLeadId());
					} else if(stateManagerBean.getState() == 4){
						lead = commonService.getLeadById(SessionUtil.getLeadId());
						OtherRequest otherRequest = stateManagerBean.getOtherRequest();
						if(otherRequest!=null && otherRequest.getMobileNo()==null){
							if(otherRequest!=null && otherRequest.getMobile()!=null){
								otherRequest.setMobileNo(otherRequest.getMobile());
							}
						}
						if(otherRequest!=null && otherRequest.getMobileNo()!=null){
							if(lead.getLeadApplyingFrom()==2){
								if(!ValidatorUtil.isValidNRIMobile(otherRequest.getMobileNo())){
									responseMessage = "error|Invalid mobile number|1";
									return "jsonResponsePage"; 
								}
							}else{
								lead.setLeadApplyingFrom(1);
								lead.setLeadCountryId(null);
								lead.setLeadIsdCode(Constants.COUNTRY_CODE_INDIA);
								if(!ValidatorUtil.isValidMobile(otherRequest.getMobileNo())){
									responseMessage = "error|Invalid mobile number|1";
									return "jsonResponsePage"; 
								}
							}
							lead.setLeadMobileNo(otherRequest.getMobileNo());
						}
					}
					json = processManagerImpl.processWantUsToCallYou(SessionUtil.getLeadId(), stateManagerBean.getState(), lead, visitId, Constants.OTHER_ID_INTEGER, inputOtp);
					if(json.get("status").toString().equalsIgnoreCase("success")){

//						String mobileNo = lead.getLeadMobileNo();
//						int leadProdId = lead.getLeadProductTypeId();    
//						
//						StringBuilder captcha = new StringBuilder(SessionUtil.getCaptch());
//						StringBuilder response = new StringBuilder(mobileNo);   
//						response = response.reverse() ; 
//						response = response.append(leadProdId);
//						response = response.append(captcha.reverse());        

						responseMessage = "success|"+json.getString("message");
						if(json.getString("alertCount")!=null){      
							responseMessage =responseMessage+"|"+json.getString("alertCount");
						}
//						else {
//							responseMessage =responseMessage+"|"+"0";
//						}   
//						responseMessage = responseMessage+"|"+Base64.getEncoder().encodeToString(response.toString().getBytes());
						logger.info("stateManagerBean.getState() 232"+stateManagerBean.getState());
						if(stateManagerBean.getState()!=4) {
							responseMessage = SbiUtil.getEncryptedResponse(responseMessage);
						}
						SessionUtil.setCaptch(null);         
					}else{
						logger.info("stateManagerBean.getState() 239"+stateManagerBean.getState());
						responseMessage = "error|"+json.getString("message");
						if(json.getString("alertCount")!=null){
							responseMessage =responseMessage+"|"+json.getString("alertCount");
						}
						if(stateManagerBean.getState()!=4) {
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
				logger.info("CommonLoanAction.java LN 135 stateManagerBean.getState()==3, 4, 5  ::"+stateManagerBean.getState()+" :: ", e);
				responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
				return "jsonResponsePage";
			} catch (Exception e) {
				logger.info("CommonLoanAction.java LN 135 stateManagerBean.getState()==3, 4, 5  ::"+stateManagerBean.getState()+" :: ", e);
				responseMessage = "error|"+Constants.SORRY_FOR_INCONVENIENCE;
				return "jsonResponsePage";
			}
		}
		if(stateManagerBean.getState()==-1){
			jsonJSArray = SbiUtil.populateJSValidation(3, Constants.HOME_ID).toString();
			if(uiType!=null){
				jsonJSArrayApplicationTrack = SbiUtil.populateJSValidation(1, Constants.APP_LOAN_TRACK_ID).toString();
			}
			if(SessionUtil.getLeadId()!=null){
				lead = commonService.getLeadById(SessionUtil.getLeadId());
			}
			Map<Integer, String> mapCity =new LinkedHashMap<Integer, String>();
			mapCity = commonService.getStateCityDistrictBranchWithoutLoanType(2, null, null, null, null, null, null, null, null, null);
			mapCity.put(Constants.OTHER_ID_INTEGER, Constants.OTHER_VALUE);
			beanList.setCitiesoptgrp1(mapCity);
			UIBeanListStatic.setCitiesCard(commonService.getCititesSCC(Constants.CREDIT_CARD_ID));
			UIBeanListStatic.setCardCityGroup1(commonService.getcitiesSCCMetro(Constants.CREDIT_CARD_ID));
			
		}
		return SUCCESS+(uiType==null?"":uiType);
	}
	
	public StreamResult changeLanguage() {
		JSONObject json = new JSONObject();
		if(ValidatorUtil.isValid(selectedLanguage)){
			SessionUtil.setSelectedLanguage(selectedLanguage);
		}
		selectedLanguage = SessionUtil.getSelectedLanguage();
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	
	

	
	public StreamResult cityByStateId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cities", SbiUtil.getCities(loanTypeId, stateId, isRACPC, isScholar, isNRI, isEdvantage, isXpressCreditIT));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult getCitiesForHomeLoan() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cities", SbiUtil.getCitiesForHomeLoan(stateId));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}

	public StreamResult cityByHomeStateId() throws JSONException {
		JSONObject json = new JSONObject();
		isScholar = 0;
		json.put("cities", SbiUtil.getCities(loanTypeId, stateId, isRACPC ,isScholar, isNRI, isEdvantage, isXpressCreditIT));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getDistrictByStateId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cities", SbiUtil.getDistrictByStateId(loanTypeId, stateId, isRACPC,isScholar, isNRI, isEdvantage ));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	
	
	
	public StreamResult localityByStateId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cities", SbiUtil.getLocalites( stateId, cityId));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	public StreamResult localityOrDistrictByStateandCityId() {
		JSONObject json = new JSONObject();
		try {
			if(ValidatorUtil.isValid(cityId)){
				json.put("status", "success");
				if(cityId==Constants.OTHER_ID_INTEGER.intValue()){
					if(ValidatorUtil.isValid(isEdvantage)){
						json.put("districts", SbiUtil.getDistrictByStateId(loanTypeId, stateId, isRACPC, isScholar, isNRI, isEdvantage));
					}else{
						json.put("districts", SbiUtil.getDistrictByStateId(loanTypeId, stateId, isRACPC, isScholar, isNRI, isEdvantage));
					}
					json.put("populate", "district");
					
				}else{




					Map<Integer, String> branchMap = new LinkedHashMap<Integer, String>();
					if(isRACPC!=null && isRACPC>0){
						branchMap = commonService.getStateCityDistrictBranch(4, loanTypeId, null, cityId, null, isRACPC, null, null, null, null);

					}else if(isScholar!=null && isScholar>0){
						if((ValidatorUtil.isValid(preferredLocation) && ValidatorUtil.isValid(instituteName))&& preferredLocation == 2 ){
							MasterInstitute masterInsti = commonService.getInstituteByInstituteName(instituteName);
							if(masterInsti !=null ){
								MasterBranch scholarBranch = commonService.getBranchById(masterInsti.getInstituteDefaultBranch());
								branchMap.put(scholarBranch.getBranchId(), scholarBranch.getBranchName());
							}
						}else{
							branchMap = commonService.getStateCityDistrictBranch(4, loanTypeId, null, cityId, null, null, isScholar, null, null, null);
						}
						
					}else if(isNRI!=null && isNRI>0){
						branchMap = commonService.getStateCityDistrictBranch(4, loanTypeId, null, cityId, null, null, null, isNRI, null, null);
					}else if(isEdvantage != null && isEdvantage == 1){
						branchMap = commonService.getStateCityDistrictBranch(4, loanTypeId, null, cityId, null, null, null, null, isEdvantage, null);
					}else{
						branchMap = commonService.getStateCityDistrictBranch(4, loanTypeId, stateId, cityId, null, null, null, null, null, null);
					}
					
					if(branchMap!=null && !branchMap.isEmpty()){
						JSONArray jsonArray = new JSONArray();
						for(Map.Entry<Integer, String> entry : branchMap.entrySet()){
							JSONObject jsonInner = new JSONObject();
							jsonInner.put("key", entry.getKey());
							jsonInner.put("value",  entry.getValue());
							jsonInner.put("cityDefaultDisplay", 2);
							jsonArray.put(jsonInner);
						}
						
						json.put("branches", jsonArray);
						json.put("populate", "branch");
					}
				}
			}else{
				json.put("status", "error");
			}
		} catch (JSONException e) {
			logger.info("CommonLoanAction.java LNo 325 :: localityOrDistrictByStateandCityId() ", e);
		} catch (Exception e) {
			logger.info("CommonLoanAction.java LNo 325 :: localityOrDistrictByStateandCityId() ", e);
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	public StreamResult districtByStateid() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("cities", SbiUtil.getDistrictByStateId(loanTypeId, stateId, isRACPC,isScholar, isNRI, isEdvantage));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult branchByStateId() throws JSONException {
		JSONObject json = new JSONObject();
			if(institutdeName != null && institutdeName != ""){
				json.put("cities", SbiUtil.getBranchByInstitute( institutdeName));
			}else{
				if(isBidyaLakhmi!=null && isBidyaLakhmi>0){
					json.put("cities", SbiUtil.getBranchByDistrictIdBidya(loanTypeId, stateId, cityId, districtId, isRACPC, isScholar,isNRI,isEdvantage, isBidyaLakhmi));
				}else if(ValidatorUtil.isValid(isEdvantage)){
					json.put("cities", SbiUtil.getBranchByDistrictId( loanTypeId, stateId, cityId, districtId, isRACPC, isScholar, isNRI,isEdvantage));
				}else{
					json.put("cities", SbiUtil.getBranchByDistrictId( loanTypeId, stateId, cityId, districtId, isRACPC, isScholar, isNRI,null));
				}
			}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult branchByDistrictId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("branches", SbiUtil.getBranchByDistrictId( loanTypeId, null, null, districtId, null, null , isNRI ,isEdvantage));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	public StreamResult branchByCityId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("branches", SbiUtil.getBranchByDistrictId( loanTypeId, null, cityId, null, null, null , isNRI ,null));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult loanPurposeByParentId() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("elementData", SbiUtil.getLoanCatogory( loanTypeId, lpParentId));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	public StreamResult employerlist() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("elementData", SbiUtil.getLoanCatogory( loanTypeId, 1));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		
	}
	
	
	public StreamResult getAllBank() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("banks", SbiUtil.getAllBank( Constants.HOME_LOAN_ID));
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getAllEmployer() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllEmployer( query, false,null);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getAllBranchName() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllBranchName( query);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}

	public StreamResult getAllEmployerForPL() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllEmployer( query, true,null);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getAllEmployerForXpressCreditIT() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllEmployer( query, false,isNasscom);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getAllBuilder() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getAllBuilder( query);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	public StreamResult getPreownedCarDealer() {
		JSONObject json = new JSONObject();
		json=SbiUtil.getPreownedCarDealer( query);
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	
	public StreamResult getRequestConfigByLoanType() {
		jsonJSArray= SbiUtil.populateJSValidation(requestIndex, loanTypeId).toString();
		return new StreamResult(new ByteArrayInputStream(jsonJSArray.getBytes()));
	}
	
	
	
	
	public StreamResult getTypeOfSalaryPackage() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", "success");
		if(!ValidatorUtil.isValid(loanTypeId) || !ValidatorUtil.isValid(industryTypeId)){
			json.put("status", "error");
		}else{
			json.put("salaryPackages",SbiUtil.getAllSalaryPackageByLoanType( loanTypeId, industryTypeId));
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	

	public void getSmsCallBackUrlResponse() throws JSONException{
		HttpServletRequest request = RequestUtil.getServletRequest();
		if(request.getParameter("mobileno")!=null && request.getParameter("msgtext")!=null){
			String appMobileNo = request.getParameter("mobileno").trim();

			String msgtext = request.getParameter("msgtext");
			String[] msg =  msgtext.split(" ");
			Integer otp = Integer.valueOf(msg[1]);

			
			
			String keyword = request.getParameter("keyword");
			if (keyword!=null && !keyword.equals("")){
				if(keyword.trim().equals(Constants.OCAS_KEYWORD))
				{
					String mobile = null;
					if (appMobileNo.length()>=12){
						mobile= appMobileNo.substring(2);
					}else{
						mobile = appMobileNo;
					}
					processManagerImpl.getSmsVerified(mobile, otp);
				}	
			}else{
				logger.info("Keyword is blank");
			}
		}
	}
	
	public StreamResult getIvrsOtpCallBackUrlResponse() throws JSONException {
		HttpServletRequest request = RequestUtil.getServletRequest();
		JSONObject json = new JSONObject();
		String loanType= request.getParameter("loanType");
		String mobile = request.getParameter("mobileNumber");
		String otp = request.getParameter("otp");
		String checkSum = request.getParameter("checkSum");
		String checkSumCalculated="";
		try {
			checkSumCalculated = Security.get_SHA_256_SecurePassword(loanType+"|"+mobile+"|"+otp, EncryptDecryptUtil.decrypt(Constants.IVRS_SALT));
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			logger.info("CommonLoanAction.java LNo 564 :: Exception in getIvrsOtpCallBackUrlResponse() ", e);
		}
		logger.info("CheckSum calculate mobileWithOtp============="+checkSumCalculated);
		
		boolean error = false;
		json.put("mobileNumber", mobile);
		if(!ValidatorUtil.isValid(checkSum) || !checkSumCalculated.equalsIgnoreCase(checkSum)){
			//logger.info("Inside validating checkSum");
			json.put("status", "FAIL");
			json.put("error", "Invalid checksum");
			error =true;
		}else if(!ValidatorUtil.isValid(loanType)){
			//logger.info("Inside validating loanType");
			json.put("status", "FAIL");
			json.put("error", "Invalid loanType");
			error =true;
		}else if(!ValidatorUtil.isValidMobile(mobile)){
			//logger.info("Inside validating mobile");
			json.put("status", "FAIL");
			json.put("error", "Invalid mobile number");
			error =true;
		}else if(!ValidatorUtil.isValid(otp)){
			//logger.info("Inside validating otp");
			json.put("status", "FAIL");
			json.put("error", "Invalid otp");
			error =true;
		}
		
		if( !error ){
			logger.info("no error found");
			json = processManagerImpl.getOTPVerified(loanType, mobile, Integer.parseInt(otp));
		}
		
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	
	
	
	public String fileProcess() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("status", "success");
		return SUCCESS+(uiType==null?"":uiType);

		
	}
	
	public String logout(){
		SessionUtil.setEmail(null);
		SessionUtil.setCaptch(null);
		SessionUtil.setMobile(null);
		SessionUtil.setISDCode(null);
		SessionUtil.setApplicantName(null);
		SessionUtil.setLeadId(null);
		SessionUtil.setBankLMSUser(null);
		SessionUtil.setEkycApplication(false);
		SessionUtil.setUiType(null);
		SessionUtil.setHomeLoanTopupApplicationSequenceId(null);
		SessionUtil.setHomeTopUpLoanCbsCallId(null);
		SessionUtil.setHomeLoanApplicationSequenceId(null);
		SessionUtil.setHomeLoanCbsCallId(null);
		SessionUtil.setAutoLoanApplicationSequenceId(null);
		SessionUtil.setAutoLoanCbsCallId(null);
		SessionUtil.setPersonalLoanTypeSequenceId(null);
		SessionUtil.setPersonalLoanApplicationSequenceId(null);
		SessionUtil.setPensionLoanApplicationSequenceId(null);
		SessionUtil.setPersonalTypeId(null);
		SessionUtil.setPersonalLoanCbsCallId(null);
		SessionUtil.setEducationLoanApplicationSequenceId(null);
		SessionUtil.setEducationScholarLoanSequenceId(null);
		SessionUtil.setEducationGlobalEdvantageSequenceId(null);
		SessionUtil.setEducationTakeOverLoanApplicationSequenceId(null);
		SessionUtil.setEducationBidyaLakshmiLoanSequenceId(null);
		SessionUtil.setEducationLoanCbsCallId(null);
		SessionUtil.setEducationTypeId(null);
		SessionUtil.setAgriLoanApplicationSequenceId(null);
		SessionUtil.setAgriLoanCbsCallId(null);
		SessionUtil.setCreditCardApplicationSequenceId(null);
		SessionUtil.setCreditCardCbsCallId(null);
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
		return null;
	}
	
	public String updateConstants(){
		responseMessage = ConstantUtil.updateConstants();
		return "jsonResponsePage";
	}
	
	public String getSaltKey(){
		responseMessage=requestHandler.getUniqueRandom();
		SessionUtil.setSecretKey(responseMessage);
		return "jsonResponsePage";
	}











	
	public Integer getResidentType() {
		return residentType;
	}

	public void setResidentType(Integer residentType) {
		this.residentType = residentType;
	}
	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getKeyId() {
		return keyId;
	}

	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}

	public Integer getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(Integer keyValue) {
		this.keyValue = keyValue;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getLpParentId() {
		return lpParentId;
	}

	public void setLpParentId(Integer lpParentId) {
		this.lpParentId = lpParentId;
	}

	public Integer getOnChangeValue() {
		return onChangeValue;
	}

	public void setOnChangeValue(Integer onChangeValue) {
		this.onChangeValue = onChangeValue;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
	public Integer getLoanPurposeId() {
		return loanPurposeId;
	}
	public void setLoanPurposeId(Integer loanPurposeId) {
		this.loanPurposeId = loanPurposeId;
	}
	public Integer getIsRACPC() {
		return isRACPC;
	}
	public void setIsRACPC(Integer isRACPC) {
		this.isRACPC = isRACPC;
	}
	public Integer getIsScholar() {
		return isScholar;
	}
	public void setIsScholar(Integer isScholar) {
		this.isScholar = isScholar;
	}
	public String getInstitutdeName() {
		return institutdeName;
	}
	public void setInstitutdeName(String institutdeName) {
		this.institutdeName = institutdeName;
	}
	public Integer getPreferredLocation() {
		return preferredLocation;
	}
	public void setPreferredLocation(Integer preferredLocation) {
		this.preferredLocation = preferredLocation;
	}
	public String getInstituteName() {
		return instituteName;
	}
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public Integer getIsNRI() {
		return isNRI;
	}

	public void setIsNRI(Integer isNRI) {
		this.isNRI = isNRI;
	}

	public ArrayList<UploadBean> getQuickLead() {
		return quickLead;
	}

	public void setQuickLead(ArrayList<UploadBean> quickLead) {
		this.quickLead = quickLead;
	}
    
	

	/*public PrivacyRequest getPrivacyRequest() {
		return privacyRequest;
	}
	public void setPrivacyRequest(PrivacyRequest privacyRequest) {
		this.privacyRequest = privacyRequest;
	}*/
	
	public String getIsXpressCreditIT() {
		return isXpressCreditIT;
	}

	public void setIsXpressCreditIT(String isXpressCreditIT) {
		this.isXpressCreditIT = isXpressCreditIT;
	}
	public String getSms_campaign() {
		return sms_campaign;
	}
	public void setSms_campaign(String sms_campaign) {
		this.sms_campaign = sms_campaign;
	}

	public Integer getIsAgri() {
		return isAgri;
	}

	public void setIsAgri(Integer isAgri) {
		this.isAgri = isAgri;
	}

	/*public MingleBean getSbiMingleLead() {
		return sbiMingleLead;
	}

	public void setSbiMingleLead(MingleBean sbiMingleLead) {
		this.sbiMingleLead = sbiMingleLead;
	}*/


	public Integer getPreferredLoanLocation() {
		return preferredLoanLocation;
	}

	public void setPreferredLoanLocation(Integer preferredLoanLocation) {
		this.preferredLoanLocation = preferredLoanLocation;
	}

	public Integer getPensionPayingLocation() {
		return pensionPayingLocation;
	}

	public void setPensionPayingLocation(Integer pensionPayingLocation) {
		this.pensionPayingLocation = pensionPayingLocation;
	}
	
	public Integer getIsEdvantage() {
		return isEdvantage;
	}

	public void setIsEdvantage(Integer isEdvantage) {
		this.isEdvantage = isEdvantage;
	}
	public Integer getIsBidyaLakhmi() {
		return isBidyaLakhmi;
	}
	
	public void setIsBidyaLakhmi(Integer isBidyaLakhmi) {
		this.isBidyaLakhmi = isBidyaLakhmi;
	}
//	public PrivacyRequest getPrivacyLead() {
//		return privacyLead;
//	}
//	public void setPrivacyLead(PrivacyRequest privacyLead) {
//		this.privacyLead = privacyLead;
//	}
	
}
