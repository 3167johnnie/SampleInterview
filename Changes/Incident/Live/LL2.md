/SBI/src/com/mintstreet/common/action/CommonLoanAction.java
	
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
import com.mintstreet.common.util.AesUtil;
import com.mintstreet.common.util.CommonUtilites;
import com.mintstreet.common.util.ConstantUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.Security;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.uploadLead.entity.UploadBean;
import java.security.SecureRandom;
import java.util.UUID;
import org.apache.commons.codec.binary.Hex;   

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
	private String isXpressCreditIT;
	protected String sms_campaign;
	
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
			//UIBeanListStatic.setCitiesCard(commonService.getCititesSCC(Constants.CREDIT_CARD_ID));
			//UIBeanListStatic.setCardCityGroup1(commonService.getcitiesSCCMetro(Constants.CREDIT_CARD_ID));
			
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
			  // Ensure crypto keys exist in session before encrypting response
		    if (SessionUtil.getCryptoSalt() == null) {
		        byte[] saltBytes = new byte[16];
		        new SecureRandom().nextBytes(saltBytes);
		        byte[] ivBytes = new byte[16];
		        new SecureRandom().nextBytes(ivBytes);
		        String passPhrase = UUID.randomUUID().toString() + UUID.randomUUID().toString();

		        SessionUtil.setCryptoSalt(Hex.encodeHexString(saltBytes));
		        SessionUtil.setCryptoIv(Hex.encodeHexString(ivBytes));
		        SessionUtil.setCryptoPassPhrase(passPhrase);
		    }
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
//						inputOtp=stateManagerBean.getOtherRequest().getInputOtpWantUs();
						String encryptedOtp = stateManagerBean.getOtherRequest().getInputOtpWantUs();
						try {
						    String saltHex = SessionUtil.getCryptoSalt();
						    String ivHex = SessionUtil.getCryptoIv();
						    String passPhrase = SessionUtil.getCryptoPassPhrase();
						    AesUtil aesUtil = new AesUtil(256, 100000);
						    inputOtp = aesUtil.decrypt(saltHex, ivHex, passPhrase, encryptedOtp);
						} catch (Exception e) {
						    logger.error("OTP decryption failed", e);
						    responseMessage = "error|OTP verification failed. Please try again.";
						    return "jsonResponsePage";
						}   
						
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
			//UIBeanListStatic.setCitiesCard(commonService.getCititesSCC(Constants.CREDIT_CARD_ID));
			//UIBeanListStatic.setCardCityGroup1(commonService.getcitiesSCCMetro(Constants.CREDIT_CARD_ID));
			
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
	
//	public StreamResult getCryptoConfig() {
//		JSONObject json = new JSONObject();
//		try {
//			logger.info("inside getCryptoConfig method");
//			json.put("salt", "F3D46A54E1A2E81068AFCFE13A579711");
//			json.put("iv", "F3D46A54E1A2E81068AFCFE13A579711");
//			json.put("passPhrase", "The quick brown fox jumps over the lazy dog");
//			json.put("keySize", "256");
//			json.put("iterationCount", "100000");
//		} catch (JSONException e) {
//			logger.info("AutoLoanAction.java LNo.2220 :: carModelByCompanyId() ", e);
//		}
//		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
//	}
	
//	public StreamResult getCryptoConfig() {
//	    JSONObject json = new JSONObject();
//	    try {
//	        String salt = SessionUtil.getCryptoSalt();
//	        String iv = SessionUtil.getCryptoIv();
//	        String passPhrase = SessionUtil.getCryptoPassPhrase();
//
//	        // Generate new values only if not already set in this session
//	        if (salt == null || iv == null || passPhrase == null) {
//	            byte[] saltBytes = new byte[16];
//	            new java.security.SecureRandom().nextBytes(saltBytes);
//	            byte[] ivBytes = new byte[16];
//	            new java.security.SecureRandom().nextBytes(ivBytes);
//	            passPhrase = java.util.UUID.randomUUID().toString() + java.util.UUID.randomUUID().toString();
//
//	            salt = org.apache.commons.codec.binary.Hex.encodeHexString(saltBytes);
//	            iv = org.apache.commons.codec.binary.Hex.encodeHexString(ivBytes);
//
//	            SessionUtil.setCryptoSalt(salt);
//	            SessionUtil.setCryptoIv(iv);
//	            SessionUtil.setCryptoPassPhrase(passPhrase);
//	        }
//
//	        json.put("salt", salt);
//	        json.put("iv", iv);
//	        json.put("passPhrase", passPhrase);
//	        json.put("keySize", "256");
//	        json.put("iterationCount", "100000");
//	    } catch (JSONException e) {
//	        logger.error("getCryptoConfig error", e);
//	    }
//	    return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
//	}   
	
	public StreamResult getCryptoConfig() {
		logger.info("Start Inside getCryptoConfig()");
	    JSONObject json = new JSONObject();
	    try {
	        String salt = null;
	        String iv = null;
	        String passPhrase = null;

	        try {
	            salt = SessionUtil.getCryptoSalt();
	            iv = SessionUtil.getCryptoIv();
	            passPhrase = SessionUtil.getCryptoPassPhrase();
	        } catch (Exception e) {
	            logger.error("Failed to read crypto from session", e);
	        }

	        if (salt == null || iv == null || passPhrase == null) {
	            try {
	                byte[] saltBytes = new byte[16];
	                new SecureRandom().nextBytes(saltBytes);
	                byte[] ivBytes = new byte[16];
	                new SecureRandom().nextBytes(ivBytes);
	                passPhrase = UUID.randomUUID().toString() + UUID.randomUUID().toString();
	                salt = Hex.encodeHexString(saltBytes);
	                iv = Hex.encodeHexString(ivBytes);

	                try {
	                	logger.info(" Inside getCryptoConfig() stored in session start");
	                    SessionUtil.setCryptoSalt(salt);
	                    SessionUtil.setCryptoIv(iv);
	                    SessionUtil.setCryptoPassPhrase(passPhrase);
	                    logger.info(" Inside getCryptoConfig() stored in session end ");
	                } catch (Exception e) {
	                    logger.error("Failed to store crypto in session", e);
	                }
	            } catch (Exception e) {
	                logger.error("Failed to generate crypto keys", e);
	                // Fallback: return fixed values so JS doesn't break
	                salt = "F3D46A54E1A2E81068AFCFE13A579711";
	                iv = "F3D46A54E1A2E81068AFCFE13A579711";
	                passPhrase = "The quick brown fox jumps over the lazy dog";
	            }
	        }

	        json.put("salt", salt);
	        json.put("iv", iv);
	        json.put("passPhrase", passPhrase);
	        json.put("keySize", "256");
	        json.put("iterationCount", "100000");
	    } catch (Exception e) {
	        logger.error("getCryptoConfig fatal error", e);
	        // LAST RESORT: return valid JSON with fallback values
	        try {
	        json.put("salt", "F3D46A54E1A2E81068AFCFE13A579711");
	        json.put("iv", "F3D46A54E1A2E81068AFCFE13A579711");
	        json.put("passPhrase", "The quick brown fox jumps over the lazy dog");
	        json.put("keySize", "256");
				json.put("iterationCount", "100000");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }logger.info("End  Inside getCryptoConfig()");
	    return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
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
		SessionUtil.setApplicationType(null);
		SessionUtil.setVisitIdHomePage(null);
		SessionUtil.setVisitIdHL(null);
		SessionUtil.setVisitIdAL(null);
		SessionUtil.setVisitIdPL(null);
		SessionUtil.setVisitIdEL(null);
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

}



/JS/ocas/sbiNew/js/homesbi.js
	
	var allLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var activeLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var error_msg = "";
var globalMobile ="";
var isValidForHomePageOTP=true;

//context path changes
var ctxPath = window.location.pathname;
if (ctxPath != '/personal-banking') {
	ctxPath = ctxPath.substring(0, window.location.pathname.indexOf("/",2));
}

function  validateWantUstoCallyou(src, typeOfValidation){
	var flag = 0;
	var willPost=1;
	if(typeOfValidation ==1 || typeOfValidation == 2){
		if(M.byId('mobile') != undefined) {
			if(M.byId('mobile').value == 0 || M.byId('mobile').value == ''|| validationForMobile(M.byId('mobile').value)) {
				M.byId('mobile').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid mobile number", false);
	        }else{
	        	M.byId('mobile').style.borderColor=C[8];
	        }
		}
		if(M.byId('email') != undefined) {
			if(M.byId('email').value == 0 || M.byId('email').value == ''|| !ValidateEmail(M.byId('email').value)) {
				M.byId('email').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid email", false);
			}else{
				M.byId('email').style.borderColor=C[8];
			}
	    }
		if(M.byId('inputOtpWantUs') != undefined) {
			if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
				M.byId('inputOtpWantUs').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the correct otp", false);
	        }else{
	        	M.byId('inputOtpWantUs').style.borderColor=C[8];
	        }
		}
	}
	if(typeOfValidation == 3){
		if(M.byId('inputOtpWantUs') != undefined) {
			if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
				M.byId('inputOtpWantUs').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter OTP", false);
	        }else{
	        	 M.byId('inputOtpWantUs').style.borderColor=C[8];
	        }
	    }
		if(willPost==1 && M.byId('otpHomecaptcha') != undefined) {
			if(M.byId('otpHomecaptcha').value == 0 || M.byId('otpHomecaptcha').value == '' || M.byId('otpHomecaptcha').value.length!=6) {
				M.byId('otpHomecaptcha').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter Captcha", false);
	        }else{
	        	M.byId('otpHomecaptcha').style.borderColor=C[8];
	        }
	    }
	}
	if(willPost){
		return true;
	}else{
		return false;
	}
}

function submit_first_page_bind(){
	jsonObject = fetch_validation_object(3,0);
	addValidationsRules(document.homePageCallback, jsonObject);
    if(jQuery("form#homePageCallback").valid()){
    	if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
    		validateNriMobileNo('appNRIMobileNo');
		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
			validateIndianMobileNo('mobile');
		}else{
			isValidForHomePageOTP=false;
		}
    	/*if(!isValidForHomePageOTP){
    		return false;
    	}*/
    	hidePOPMsg('errorOTPMsg');
		jQuery("#resendOtpWantUs").show();
		/*var formdata = "params="+getRequest('homePageCallback', 1);
		formdata +="&requestIndex=3&requestType=2";*/
		var paramData= getRequest('homePageCallback', 1);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=3&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
	        type:'post',
	        url: ctxPath + "/" + ajaxPostUrl,
	        data: formdata,
	        //headers : {"Accept-Encoding" : "gzip"},
	        beforeSend:function(){
	        	removeDynamicLoader('callBankSubmit');
				showDynamicLoader('callBankSubmit');//show loader
			},
			complete:function(){
				removeDynamicLoader('callBankSubmit'); //remove loader
			},
	        success:function(htmlContent){
	        	if(htmlContent!=""){
	            	var stringMessage = [];
					
					isEncrypted(htmlContent).then(function(result) {
						
						if (result) {
							getDecryptedResponse(htmlContent).then(function(decryptedResponse) {
								stringMessage = decryptedResponse.split("|");
								
								if(stringMessage[0] == "error"){
				        			showPOPMsg('errorOTPMsg','<b>Error: </b><em>'+stringMessage[1]+'</em>', false);
				        	        return false;
				            	}else if(stringMessage[0] != "success"){
				            		showPOPMsg('errorOTPMsg','<b>Error: </b><em>Something went wrong!</em>', false);
				            	}else{
				            		//secretKey=getSecretKey();
				            		$("#beforeGetACallBack").hide();
				            		$("#afterGetACallBack").show();
				            		
				            		if(window.matchMedia("only screen and (max-width: 768px)").matches) {
				            			document.getElementsByClassName("otp-right-section")[0].style.height = "330px";
				            		}
				            		
				            		
				            		jQuery("#callbackBody").removeClass("form-tab");
				            		var resendHtml='';
				            		if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
				            			globalMobile = $("#appNRIMobileNo").val();
				            			resendHtml = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
				            			
				            		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
				            			globalMobile = $("#mobile").val().replace(/^(\d{6})(\d{4})$/, function() {
				            			  return arguments[1].replace(/./g, '*') + arguments[2];
				            			});
				            			resendHtml = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
				            		}else{
				            			return false;
				            		}
				            		jQuery("#resend_box").html(resendHtml);
				            		jQuery("#resend_box").show();
					            	jQuery("#inputOtpWantUs").val('');
					            	jQuery("#captcha").val('');
					            	jQuery("#otpHomecaptcha").val('');
					            	refreshCaptcha('HomePopcapImage');
					            	jQuery("#wantus_row_confirm").show();
					            	return false;
				            	}
								
							});
						} else {
							stringMessage[0] == "error"
							stringMessage[1] == "Something went wrong!"
							stringMessage[2] == "captcha"
						}
						
					});
					
	        	}
	        	return false;
	        },
	        error:function(data){
	        	showPOPMsg('errorOTPMsg','<b>Error: </b><em>Sorry for the inconvenience, Please click <a href='/'>here</a> to start again</em>', false);
		        return false;
	        }
	    });
	}
	return false;
}

jQuery(document).ready(function(){
	//secretKey=getSecretKey();
	jQuery("#call_back").validate({
		rules: {
			name: {
				required: true
		    },
		    mobileWantUsToCallYou: {
				required: true
		    },
		    email: {
				required: true,
				email:true
		    }
		}
	});

	jQuery("form#homePageCallback").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        jQuery(element).valid();
	    },
	    onblur:function (element) {
	        jQuery(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
	        if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          showPOPMsg('errorOTPMsg',message, false);
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if(element.attr("type")=="checkbox"){
				$('.colorAuto').css({ 'outline':'solid','outline-width': '2px', 'outline-color': '#e96e57'});
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});

	jQuery(document).on("click", "#resendOtpWantUs", function(){
		removeDynamicLoader('resendOtpWantUs');
		showDynamicLoader('resendOtpWantUs');
		$("#resendOtpWantUs").removeAttr('disabled');
	/*	var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";*/
		
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
				
				
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		refreshCaptcha('HomePopcapImage');
	        		if(stringMessage[2]>4){
	        			showPOPMsg('errorOTPMsg',"You have reached maximum number of OTP resend count", false);
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    }
		});
		return false;
	});
	jQuery(document).on("click", "#resendOtp", function(){
		removeDynamicLoader('resendOtp');
		showDynamicLoader('resendOtp');
		$("#resendOtp").removeAttr('disabled');
		var mobile = jQuery("#resendMobile").val();
		if(mobile != undefined && mobile != null && mobile!='') {
			if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
				if(!M.isMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
	        		return false;
				}
			}else{
				if(!M.isNRIMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
					return false;
				}
			}
	    }
		if(mobile!=undefined && mobile!=null && mobile!=''){
			globalMobile = mobile;
		}
		//var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
				
				var stringMessage = [];
	        	//stringMessage = htmlContent.split("|");
				if (isEncrypted(htmlContent)) {
					//decryptedResponse = window.atob(htmlContent);
					decryptedResponse = getDecryptedResponse(htmlContent);
					stringMessage = decryptedResponse.split("|");
					
				} else {
					
					stringMessage = htmlContent.split("|");
				}
			//	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		if(stringMessage[2]>4){
	        			jQuery("#resendOtpWantUs").remove();
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
		return false;
	});
	
	jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
        
		event.preventDefault();  
		//removeDynamicLoader('confirmOtpWantUs');
		showDynamicLoader('confirmOtpWantUs');
		$("#confirmOtpWantUs").removeAttr('disabled');
		hidePOPMsg('errorOTPMsg');
		if(!validateWantUstoCallyou(this, 3)){
			removeDynamicLoader('confirmOtpWantUs')
			return false;
		}
		//var formdata = "params="+getRequest('call_back', 1)+"&requestIndex=5&requestType=2";
		var inputOtp=document.getElementById('inputOtpWantUs').value;
		 if(inputOtp!="" || inputOtp !=''){
			 
			 getEncryptedString(inputOtp).then(function(encryptedOtp) {
				 document.getElementById('inputOtpWantUs1').value = encryptedOtp;
				 
			 });
				 
				 
				 var paramData= getRequest('call_back', 1);
					if(!paramData){
						var errorMsg = "Sorry for the inconvenience, please try again later.";
						showPOPMsg('errorOTPMsg',errorMsg, false);
						return false;
					}
					var formdata = "params="+paramData;
					formdata +="&requestIndex=5&requestType=2";
					if(uiType!=undefined && uiType!=""){
						formdata += "&uiType="+uiType;
					}
					jQuery.ajax({
					    type: "POST",
					    url: ctxPath + "/" + ajaxPostUrl,
						data: formdata,
						//headers : {"Accept-Encoding" : "gzip"},
					    success:function(htmlContent){
					    	removeDynamicLoader('confirmOtpWantUs');
					    	if(htmlContent==null || htmlContent ==undefined){
					    		removeDynamicLoader('confirmOtpWantUs');
					    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
				        		return false;
				        	}
					    	var stringMessage = [];
							var decryptedResponse = "";
							
							isEncrypted(htmlContent).then(function(result) {
								
								if (result) {
									
									
									getDecryptedResponse(htmlContent).then(function(decryptedResponse) {
										stringMessage = decryptedResponse.split("|");
										
										if(stringMessage[0]=="success"){
							        		//secretKey=getSecretKey();
							        		if(jQuery("#leadLoanPurposeId").val()==1){
							        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=HOME" width="1" height="1" />';
							        			}
							        		}else if(jQuery("#leadLoanPurposeId").val()==3){
							        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=AUTO" width="1" height="1" />';
							        			}
							        		}
							        		jQuery("#wantus_row_confirm").hide();
							        		//showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
							        		$("#afterGetACallBack").hide();
							        		var successMsg='<div class="thnk-msg">'
											+'<h2><span class="smile-icon"></span><br>Thank you </h2>'
											+'<p>Thank You for your interest. Our representative will contact you shortly.</p>'
											+'</div>';
							        		$("#errorOTPMsg").removeClass('error-msg-cbs')
							        		$("#errorOTPMsg").html(successMsg).show();
							        		jQuery("#resend_box").hide();
						   	        		setTimeout(function(){
						   	        			jQuery(".close").trigger("click");
							        			reinitiateCallback();
							        		}, 2000);
						   	        		if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        			fbq('track', 'Lead', { loan : 'Callback' });
							        		}
							        	}else if(stringMessage[0]=="error"){
							        		showPOPMsg('errorOTPMsg',stringMessage[1], false);
							        	}
										
									});
									
								} else {
									
									showPOPMsg('errorOTPMsg', 'Something went wrong', false);
								    removeDynamicLoader('confirmOtpWantUs');

									//stringMessage[0] = "error";
									//stringMessage[1] = "Something went wrong";
								}
							});
										
				        	return false;
					    },
					    error:function(jqXHR, textStatus){
					    	removeDynamicLoader('confirmOtpWantUs');
					    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
					    	return false;
					    }
					});
					return false;
		 }
		
	});
	
	jQuery(document).on("click","#leadApplyingFromIndiaWantUsNo", function() {
		var nrimobilediv='<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" class="w167 valid" id="appNRIMobileNo" value="" maxlength="12" name="lead.leadMobileNo" onfocus="M.toolTip_TM(\'Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.\', event, this, 130, -30, 5);">';
		jQuery("#mobileDetails").html(nrimobilediv);
		jQuery("#leadCountryId").val(0);
		jQuery("#country-div").show();
		jQuery("#city-div").hide();
	});

	jQuery(document).on("click","#leadApplyingFromIndiaWantUsYes", function(){
		var mobilediv='<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" class="w167 valid" id="mobile" value="" maxlength="10" name="lead.leadMobileNo">';
		jQuery("#mobileDetails").html(mobilediv);
		jQuery("#leadCityId").val(0);
		jQuery("#country-div").hide();
		jQuery("#city-div").show();
		jQuery("#cardCityGroup1").val(0);
		
	});
	
	jQuery(document).on("click","#cancelResend", function(){
		refreshCaptcha('HomePopcapImage');
		globalMobile = globalMobile.replace(/^(\d{6})(\d{4})$/, function() {
			return arguments[1].replace(/./g, '*') + arguments[2];
		});
		var html = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("click", "#changeMobileNumber", function(){
		refreshCaptcha('HomePopcapImage');
		$("#resendOtpWantUs").removeAttr('disabled');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="mobile" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">send</button>'+
				'<button class="resend-btn" id="cancelResend" name="cancelResend" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});


	//jQuery("#cancelResendNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#cancelResendNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});

	//jQuery("#changeMobileNumberNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#changeMobileNumberNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="nriMobileNo" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">Resend</button>'+
				'<button class="resend-btn" id="cancelResendNRI" name="cancelResendNRI" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("change","#leadCityId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	jQuery(document).on("change","#leadCountryId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#appNRIMobileNo").val("");
	});
	jQuery(document).on("change","#cardCityGroup1", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	
	jQuery(document).on("click","#retailLoanListing", function(){
		$(".mobile-landing").hide();
		$(".mobile-slider").hide();
		$(".retail-loan-listing").show();
		$('#footer').addClass('sticky-footer');
	});

	jQuery(document).on("click","#homeBack", function(){
		$(".mobile-landing").show();
		$(".mobile-slider").show();
		$(".retail-loan-listing").hide();
		$('#footer').removeClass('sticky-footer');
	});
});
jQuery(document).on("change","#leadLoanPurposeId", function(){
	if(this==undefined)return false;
	var selVal=this.value;
	
	if(selVal!=undefined){
		if(selVal>0){
			/*if(selVal==10){
				$("#citiesLeads").html($("#cardCities").html());
			}else{*/
				$("#citiesLeads").html($("#loanCities").html());
			/*}*/
		}
		return false;
	}
});

function getEncryptedString(str) {
	
	return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        var enc1 = aesUtil.encrypt(json.salt, json.iv, json.passPhrase, str);
		return enc1;
    
    }).catch(function() {
        return false;
    });
		
}

function isEncrypted(str) {
    
	if (str === '' || str.trim() === '') return Promise.resolve(false);
    return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        var dec1 = aesUtil.decrypt(json.salt, json.iv, json.passPhrase, str);
        var enc1 = aesUtil.encrypt(json.salt, json.iv, json.passPhrase, dec1);
        return enc1 == str;
    }).catch(function() {
        return false;
    });
}

function getDecryptedResponse(htmlContent) {

	return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        return aesUtil.decrypt(json.salt, json.iv, json.passPhrase, htmlContent);
    
    }).catch(function() {
        return false;
    });
	
}


function validateIndianMobileNo(indianMobileId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
		isValidForHomePageOTP=false;
		return false;
	}
	var mobile = jQuery('#'+indianMobileId).val();
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isMobile(mobile) && !jQuery('#'+indianMobileId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#mobile").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}
function validateNriMobileNo(nriMobieId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var mobile = jQuery('#'+nriMobieId).val();
	$(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
    	isValidForHomePageOTP=false;
		return false;
	}
	var countryRegion = $("#leadCountryId").val();
	if(countryRegion==null || countryRegion==undefined || countryRegion=="" || countryRegion==0){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select country code.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isNRIMobile(mobile) && !jQuery('#'+nriMobieId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isNRIMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#appNRIMobileNo").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}

$(document).on("change","#leadLoanPurposeId", function(){
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	
	var productTypeId=src.value;
	if(productTypeId==10){
		populateLanguageData(activeLanguages, 1, "leadLanguageId", "")
	}else{
		populateLanguageData(allLanguages, 1, "leadLanguageId", "")
	}
});

function populateLanguageData(data, selectedId, elementId){
	jQuery( '#'+elementId ).empty();
	var option= "";
	var selected="";
	for(var index=0; index<data.length;index++){
		var elementdata = data[index];
		option = '<option value="'+elementdata.key+'" '+selected+' >'+elementdata.value+'</option>';
		jQuery('#'+elementId).append(option);
	}
	disableById(elementId, false);
}
function openCallBackFormDiv(){
	jQuery("#tabSticker").addClass("hide-sticker");
	jQuery("#callbackBody").addClass("form-tab");
	jQuery(".home_middle_right_bottom").show();
	jQuery(".mobile-landing").hide();
	jQuery(".mobile-slider").hide();
}
function closeCallBackFormDiv(){
	jQuery("#tabSticker").removeClass("hide-sticker");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery(".home_middle_right_bottom").hide();
	jQuery(".mobile-landing").show();
	jQuery(".mobile-slider").show();
}



/SBI/WebContent/appNew/common/HomePopups.jsp
	
	<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
<div class="modal fade otp-box" id="getACallBack" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt="" /></span>
				</button>
				<div class="otp-left-section">
				<br>
					<h2><span>Get a</span> Callback</h2> 
					
					<p>(Please enter your contact details that we will revert back to you )</p>
				</div>
				<div class="otp-right-section" style="height: 825px;">
				<br>
					<div id="beforeGetACallBack" style="display: block;">
					
						<form name='homePageCallback' id='homePageCallback' method='post'  action='javascript:void(0);' enctype='application/x-www-form-urlencoded'  autocomplete="off">
							<ul class="otp-form" id="homeCallBack">
								<li>
									<label>Loan<b class="req">*</b></label>
									<div class="flat-field">
										<s:select id="leadLoanPurposeId" name="lead.leadLoanPurposeId" value="" 
										list="#{3:'Auto Loan',11:'EL Takeover',9:'Global Ed-vantage',1:'Home Loan',2:'Home Top Up Loan',7:'Loan against Immovable property',4:'Personal Loan: Salary account with SBI',12:'Phone Banking',8:'Scholar Loan'}" 
										headerKey="0" headerValue="Select loan purpose" cssClass="form-select modal-select " autocomplete="off" /> 
									</div>
								</li>
								<li>
									<label>Name<b class="req">*</b></label>
<%-- 									<s:textfield id="leadFirstName" name="lead.leadFirstName" value="" cssClass="form-control" placeholder="Name" maxlength="32" autocomplete="off" onkeydown="return M.isChars(event);"/>
 --%>									<s:textfield id="leadFirstName" name="lead.leadFirstName" value="" cssClass="form-control" placeholder="Name" maxlength="32" autocomplete="off" />
								</li>
								
								<div id="applying-from" style="display: none;">
									<%-- <li>
										<label>Applying from?<span class="mandatory">*</span></label>
										<div class="col-xs-12 mrgt-10-10">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" checked="checked" >
												<label for="leadApplyingFromIndiaWantUsYes">India</label>
											</div>	
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsNo" value="2" <s:property value="%{(lead!=null && lead.leadApplyingFrom == 1)?'checked=\"checked\"':''}"/> >
												<label for="leadApplyingFromIndiaWantUsNo">Abroad</label>
											</div>
										</div>
									</li> --%>
									<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" checked="checked" >
								</div>
								<%-- <div id="country-div" style="display:<s:property value="%{(lead!=null && lead.leadApplyingFrom == 2)?'block':'none'}"/>;" >
									<li>
										<label>Country<span class="mandatory">*</span></label>
										<div class="flat-field">
											<s:select list="%{#countries}" id="leadCountryId" name="lead.leadCountryId" value="%{lead.leadCountryId>0?lead.leadCountryId:91}" 
												headerKey="0" headerValue="Select country" cssClass="form-control"/>
										</div>
									</li>
								</div> --%>
								<div id="city-div" style="display:<s:property value="%{(lead==null || lead.leadApplyingFrom == 1)?'block':'none'}"/>;" >
									<li>
										<label>City<b class="req">*</b></label>
										<div class="flat-field">
											<s:select list="%{#citiesOtp1==null?'':#citiesOtp1}" id="leadCityId" name="lead.leadCityId" value="" headerKey="0" headerValue="Select city" cssClass="form-select modal-select"/>
										</div>
									</li>
								</div>
								<li>
									<label>Preferred Language<b class="req">*</b></label>
									<div class="flat-field">
										<s:select id="leadLanguageId" name="lead.leadLanguageId" value="" list="#{1:'Hindi', 2:'English'}" 
											headerKey="0" headerValue="Select Preferred Language" cssClass="form-select modal-select" autocomplete="off"/>
									</div>
								</li>
								<li>
									<label>Mobile<b class="req">*</b></label>
									<%-- <s:if test="%{lead!=null && lead.leadApplyingFrom == 2}">
										<s:textfield id="appNRIMobileNo" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="12" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);"/>
									 <div class="custom-tooltip">Enter mobile no. without ISD code or &#8216;0&#8217.</div>
									</s:if>
									<s:else>
										<s:textfield id="mobile" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="10" placeholder="Mobile No." onkeydown="return M.digit(event);"/>
									</s:else> --%>
									<s:textfield id="mobile" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="10" placeholder="Mobile No." />
								</li>
								<li>
									<label>Email</label>
									<s:textfield id="leadWorkEmail" name="lead.leadWorkEmail" value="" placeholder="Email" cssClass="form-control" autocomplete="off" maxlength="40" />
								</li>
									<li class="pp-captcha">
										<label>IDENTIFY YOURSELF<b class="req">*</b>
                                          <span>Copy text into input box</span></label>								
 											<img id="HOMEPocapImage" name="HOMEPocapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HOMEPocapImage');">
											<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
											<input type="text" class="form-control captcha-inpt" id="captcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" >												
									</li>
									</br>
								<li class="extra-margin-a">
									<div class="">
										<input type="checkbox" id="infoprovide" name="infoprovide" class="blue-css-checkbox colorAuto" style="left:auto!important;" required>
										
										<div id="consentCallForAllLoan" class="consentCallForAllLoan">
											<label class="label-content" for="infoprovide" style="margin-left:20px!important;">
												<s:property escapeHtml="false" value="%{@com.mintstreet.common.bo.UIBeanListStatic@consentCallback}" />
												&nbsp;<b class="req">*</b>
											</label>
										</div>


										<div id="consentCallForAutoLoan" class="consentCallForAutoLoan" style="display:none;">
										<label for="infoprovide" class="label-content consentScrollForAutoCallback" style="margin-left:20px!important;">
										I/We hereby agree and authorize the Bank to share, transmit, disclose, exchange, or use in any manner whatsoever, without any further specific consent or authorization from me/us, the information/data provided by/related to me/us including details of my/our account(s) to the Group Companies/Associates/Subsidiaries/Affiliates/Joint Ventures of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/> /any person or third party agencies/service providers whether located in India or overseas with whom the Bank has entered/propose to enter  into contracts/arrangements for provision of 'services/products' for the purpose of marketing/offering/selling any product/services and/or availing support services of any nature by the Bank.
										I/We hereby agree and consent to receive marketing communications through telephone calls/E-mail/SMS/WhatsApp	messages	on	various products / features / promotion offers provided by the Bank and its Group Companies/Associates/Subsidiaries/Affiliates/Joint Ventures. 
										I/We also acknowledge and agree that the consent hereby provided by me/us shall be legally binding on me/us irrespective of my/our registration with DNDINCPR registries and shall override such registrations.
										Notwithstanding the above, I/We acknowledge and agree that the Bank may at its absolute discretion disclose any of my/our information, if required or permitted by any law, rule or regulation or at the request/direction of any statutory or regulatory authority or court of law or if such disclosure is required for the purposes of preventing any fraud, without any further specific consent or authorization from me/us.
								&nbsp;<b class="req">*</b>
										</label>
										</div>
									</div>
								</li>
								</br>
                                <br> </br>
                                <br> 
								<li>
									<button type="submit" id="callBankSubmit" class="track-btn conf-track-btn" >SUBMIT</button>
								</li>
							</ul>
						</form>
					</div>
					<div id="afterGetACallBack" style="display: none;">
						<form  name="call_back" id="call_back" method="post" autocomplete="off">
							<ul class="otp-form" id ="bigGetAcallBcak">
			    				<div id="resend_box" style="display:none;">
									
								</div>
					    		<div id="wantus_row_confirm" style="display:block;">
					    			<li>
						    			<label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>
						    			<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp"  maxlength="6" "/>
						    			<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp" />
					    			</li>
					    			  <li class="pp-captcha" id="captchaDiv">
										<label>IDENTIFY YOURSELF<b class="req">*</b>
                                          <span>Copy text into input box</span></label>								
											<img id="HomePopcapImage" name="HomePopcapImage" src="Captcha.jpg">
											<a class="refresh-link" id="refresh_link_Captche" href="javascript:void(0);" title="Refresh" ">
											<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
											<input type="text" class="form-control captcha-inpt" id="otpHomecaptcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
									</li>
					    			<li>
						    			<button  class="track-btn conf-track-btn" id="confirmOtpWantUs" name="confirmOtpWantUs" type="submit">Confirm</button>
				    					<button  class="resend-btn" id="resendOtpWantUs" name="resendOtpWantUs" type="submit">Resend</button>
					    			</li>
					    		</div>
							</ul>
						</form>
					</div>
			    	<div class="clear"></div>
    				<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li>
				</div>
			</div>
		</div>
	</div>
</div>

<div id="loanCities" class="hide">
	<s:select list="%{#citiesOtp1==null?'':#citiesOtp1}" id="leadCityIdHidden" name="lead.leadCityId" value="%{lead.leadCityId}" headerKey="0" headerValue="Select city" cssClass="form-select"/>
</div>	
<%-- <div id="cardCities" class="hide">
	<s:select list="%{@com.mintstreet.common.bo.UIBeanListStatic@cardCityGroup1==null?'':@com.mintstreet.common.bo.UIBeanListStatic@cardCityGroup1}" 
	id="cardCityGroup1" name="lead.leadCityId" value="%{lead.leadCityId}" headerKey="0" headerValue="Select city" cssClass="form-select ">
		<s:optgroup list="%{@com.mintstreet.common.bo.UIBeanListStatic@citiesCard==null?'':@com.mintstreet.common.bo.UIBeanListStatic@citiesCard}" label="--------------"></s:optgroup> 
	</s:select>
</div> --%>	
<%-- <script type="text/javascript">
	function fetchJsonData(){
		jsonJSArray = '<s:property value="%{jsonJSArray}"/>';
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	min_age_year = 18;
	max_age_year = 70;
	
	$("#getACallBack").modal({
		show:false,
		backdrop:"static",
		keyboard:false
	});
	function getACallBackId(){
		$("#captcha").val('');
		refreshCaptcha('HOMEPocapImage');
		reinitiateCallback();
		$("#getACallBack").modal();
	}

	function reinitiateCallback(){
		$("#leadLoanPurposeId").val(0).removeClass('error').next('span').remove();
		$("#leadFirstName").val('').removeClass('error').next('span').remove();
		$("#leadApplyingFromIndiaWantUsYes").trigger("click");
		$("#leadCityId").val(0).removeClass('error').next('span').remove();
		$("#leadLanguageId").val(0).removeClass('error').next('span').remove();
		$("#mobile").val('').removeClass('error').next('span').remove();
		$("#mobile").removeAttr('readonly');
		$("#leadWorkEmail").val('').removeClass('error').next('span').remove();
		$("#infoprovide").removeClass('error');
		$("#infoprovide").attr('checked', false);
		$("#beforeGetACallBack").show();
		$("#afterGetACallBack").hide();
		hidePOPMsg('errorOTPMsg');
	}
</script> --%>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.HomePopups.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/homesbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

/SBI/ocas/sbiNew/js/jquery.HomePopups.js

$(document).ready(function(){
	
	jQuery("#refresh_link_Captche").on("click", function(){
		//	console('application status 2');
			
	    return refreshCaptcha('HomePopcapImage');
		});
	jQuery("#HOMEPocapImage").on("click", function(){
		//	console('application status 2');
		
		return refreshCaptcha('HomePopcapImage');
	});
	jQuery("#leadLoanPurposeId").on("change", function(){
		//	console('application status 2');
		
		onchange="consentCallForAutoLoan()"
	});
	jQuery("#homePageCallback").on("submit", function(){
		//	console('application status 2');
		
		return submit_first_page_bind();
	});
	jQuery("#leadFirstName").on("keydown", function(){
		//	console('application status 2');
		
	   return M.isChars(event);
	});
	jQuery("#mobile").on("keydown", function(){
		//	console('application status 2');
		
		return M.digit(event);
	});
	jQuery("#inputOtpWantUs").on("keydown", function(){
		//	console('application status 2');
		
		return M.digit(event);
	});
	
	

	function fetchJsonData(){
		jsonJSArray = '<s:property value="%{jsonJSArray}"/>';
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	min_age_year = 18;
	max_age_year = 70;
	
	$("#getACallBack").modal({
		show:false,
		backdrop:"static",
		keyboard:false
	});
	function getACallBackId(){
		$("#captcha").val('');
		refreshCaptcha('HOMEPocapImage');
		reinitiateCallback();
		$("#getACallBack").modal();
	}

	function reinitiateCallback(){
		$("#leadLoanPurposeId").val(0).removeClass('error').next('span').remove();
		$("#leadFirstName").val('').removeClass('error').next('span').remove();
		$("#leadApplyingFromIndiaWantUsYes").trigger("click");
		$("#leadCityId").val(0).removeClass('error').next('span').remove();
		$("#leadLanguageId").val(0).removeClass('error').next('span').remove();
		$("#mobile").val('').removeClass('error').next('span').remove();
		$("#mobile").removeAttr('readonly');
		$("#leadWorkEmail").val('').removeClass('error').next('span').remove();
		$("#infoprovide").removeClass('error');
		$("#infoprovide").attr('checked', false);
		$("#beforeGetACallBack").show();
		$("#afterGetACallBack").hide();
		hidePOPMsg('errorOTPMsg');
	}

	

	
	
	function consentCallForAutoLoan(){
		var autoLoan  = $("#leadLoanPurposeId").val();

		if (autoLoan == 3) {
			$('#consentCallForAllLoan').hide();
			$('#consentCallForAutoLoan').show();
	   }
	   else {
		  $('#consentCallForAllLoan').show();
		  $('#consentCallForAutoLoan').hide(); 
	   }
	}
	
});



/JS/ocas/sbiNew/js/homesbi.js
	
	var allLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var activeLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var error_msg = "";
var globalMobile ="";
var isValidForHomePageOTP=true;

//context path changes
var ctxPath = window.location.pathname;
if (ctxPath != '/personal-banking') {
	ctxPath = ctxPath.substring(0, window.location.pathname.indexOf("/",2));
}

function  validateWantUstoCallyou(src, typeOfValidation){
	var flag = 0;
	var willPost=1;
	if(typeOfValidation ==1 || typeOfValidation == 2){
		if(M.byId('mobile') != undefined) {
			if(M.byId('mobile').value == 0 || M.byId('mobile').value == ''|| validationForMobile(M.byId('mobile').value)) {
				M.byId('mobile').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid mobile number", false);
	        }else{
	        	M.byId('mobile').style.borderColor=C[8];
	        }
		}
		if(M.byId('email') != undefined) {
			if(M.byId('email').value == 0 || M.byId('email').value == ''|| !ValidateEmail(M.byId('email').value)) {
				M.byId('email').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid email", false);
			}else{
				M.byId('email').style.borderColor=C[8];
			}
	    }
		if(M.byId('inputOtpWantUs') != undefined) {
			if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
				M.byId('inputOtpWantUs').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the correct otp", false);
	        }else{
	        	M.byId('inputOtpWantUs').style.borderColor=C[8];
	        }
		}
	}
	if(typeOfValidation == 3){
		if(M.byId('inputOtpWantUs') != undefined) {
			if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
				M.byId('inputOtpWantUs').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter OTP", false);
	        }else{
	        	 M.byId('inputOtpWantUs').style.borderColor=C[8];
	        }
	    }
		if(willPost==1 && M.byId('otpHomecaptcha') != undefined) {
			if(M.byId('otpHomecaptcha').value == 0 || M.byId('otpHomecaptcha').value == '' || M.byId('otpHomecaptcha').value.length!=6) {
				M.byId('otpHomecaptcha').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter Captcha", false);
	        }else{
	        	M.byId('otpHomecaptcha').style.borderColor=C[8];
	        }
	    }
	}
	if(willPost){
		return true;
	}else{
		return false;
	}
}

function submit_first_page_bind(){
	jsonObject = fetch_validation_object(3,0);
	addValidationsRules(document.homePageCallback, jsonObject);
    if(jQuery("form#homePageCallback").valid()){
    	if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
    		validateNriMobileNo('appNRIMobileNo');
		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
			validateIndianMobileNo('mobile');
		}else{
			isValidForHomePageOTP=false;
		}
    	/*if(!isValidForHomePageOTP){
    		return false;
    	}*/
    	hidePOPMsg('errorOTPMsg');
		jQuery("#resendOtpWantUs").show();
		/*var formdata = "params="+getRequest('homePageCallback', 1);
		formdata +="&requestIndex=3&requestType=2";*/
		var paramData= getRequest('homePageCallback', 1);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=3&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
	        type:'post',
	        url: ctxPath + "/" + ajaxPostUrl,
	        data: formdata,
	        //headers : {"Accept-Encoding" : "gzip"},
	        beforeSend:function(){
	        	removeDynamicLoader('callBankSubmit');
				showDynamicLoader('callBankSubmit');//show loader
			},
			complete:function(){
				removeDynamicLoader('callBankSubmit'); //remove loader
			},
	        success:function(htmlContent){
	        	if(htmlContent!=""){
	            	var stringMessage = [];
					
					isEncrypted(htmlContent).then(function(result) {
						
						if (result) {
							getDecryptedResponse(htmlContent).then(function(decryptedResponse) {
								stringMessage = decryptedResponse.split("|");
								
								if(stringMessage[0] == "error"){
				        			showPOPMsg('errorOTPMsg','<b>Error: </b><em>'+stringMessage[1]+'</em>', false);
				        	        return false;
				            	}else if(stringMessage[0] != "success"){
				            		showPOPMsg('errorOTPMsg','<b>Error: </b><em>Something went wrong!</em>', false);
				            	}else{
				            		//secretKey=getSecretKey();
				            		$("#beforeGetACallBack").hide();
				            		$("#afterGetACallBack").show();
				            		
				            		if(window.matchMedia("only screen and (max-width: 768px)").matches) {
				            			document.getElementsByClassName("otp-right-section")[0].style.height = "330px";
				            		}
				            		
				            		
				            		jQuery("#callbackBody").removeClass("form-tab");
				            		var resendHtml='';
				            		if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
				            			globalMobile = $("#appNRIMobileNo").val();
				            			resendHtml = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
				            			
				            		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
				            			globalMobile = $("#mobile").val().replace(/^(\d{6})(\d{4})$/, function() {
				            			  return arguments[1].replace(/./g, '*') + arguments[2];
				            			});
				            			resendHtml = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
				            		}else{
				            			return false;
				            		}
				            		jQuery("#resend_box").html(resendHtml);
				            		jQuery("#resend_box").show();
					            	jQuery("#inputOtpWantUs").val('');
					            	jQuery("#captcha").val('');
					            	jQuery("#otpHomecaptcha").val('');
					            	refreshCaptcha('HomePopcapImage');
					            	jQuery("#wantus_row_confirm").show();
					            	return false;
				            	}
								
							});
						} else {
							stringMessage[0] == "error"
							stringMessage[1] == "Something went wrong!"
							stringMessage[2] == "captcha"
						}
						
					});
					
	        	}
	        	return false;
	        },
	        error:function(data){
	        	showPOPMsg('errorOTPMsg','<b>Error: </b><em>Sorry for the inconvenience, Please click <a href='/'>here</a> to start again</em>', false);
		        return false;
	        }
	    });
	}
	return false;
}

jQuery(document).ready(function(){
	//secretKey=getSecretKey();
	jQuery("#call_back").validate({
		rules: {
			name: {
				required: true
		    },
		    mobileWantUsToCallYou: {
				required: true
		    },
		    email: {
				required: true,
				email:true
		    }
		}
	});

	jQuery("form#homePageCallback").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        jQuery(element).valid();
	    },
	    onblur:function (element) {
	        jQuery(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
	        if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          showPOPMsg('errorOTPMsg',message, false);
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if(element.attr("type")=="checkbox"){
				$('.colorAuto').css({ 'outline':'solid','outline-width': '2px', 'outline-color': '#e96e57'});
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});

	jQuery(document).on("click", "#resendOtpWantUs", function(){
		removeDynamicLoader('resendOtpWantUs');
		showDynamicLoader('resendOtpWantUs');
		$("#resendOtpWantUs").removeAttr('disabled');
	/*	var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";*/
		
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
				
				
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		refreshCaptcha('HomePopcapImage');
	        		if(stringMessage[2]>4){
	        			showPOPMsg('errorOTPMsg',"You have reached maximum number of OTP resend count", false);
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    }
		});
		return false;
	});
	jQuery(document).on("click", "#resendOtp", function(){
		removeDynamicLoader('resendOtp');
		showDynamicLoader('resendOtp');
		$("#resendOtp").removeAttr('disabled');
		var mobile = jQuery("#resendMobile").val();
		if(mobile != undefined && mobile != null && mobile!='') {
			if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
				if(!M.isMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
	        		return false;
				}
			}else{
				if(!M.isNRIMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
					return false;
				}
			}
	    }
		if(mobile!=undefined && mobile!=null && mobile!=''){
			globalMobile = mobile;
		}
		//var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
				
				var stringMessage = [];
	        	//stringMessage = htmlContent.split("|");
				if (isEncrypted(htmlContent)) {
					//decryptedResponse = window.atob(htmlContent);
					decryptedResponse = getDecryptedResponse(htmlContent);
					stringMessage = decryptedResponse.split("|");
					
				} else {
					
					stringMessage = htmlContent.split("|");
				}
			//	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		if(stringMessage[2]>4){
	        			jQuery("#resendOtpWantUs").remove();
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
		return false;
	});
	
	jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
        
		event.preventDefault();  
		//removeDynamicLoader('confirmOtpWantUs');
		showDynamicLoader('confirmOtpWantUs');
		$("#confirmOtpWantUs").removeAttr('disabled');
		hidePOPMsg('errorOTPMsg');
		if(!validateWantUstoCallyou(this, 3)){
			removeDynamicLoader('confirmOtpWantUs')
			return false;
		}
		//var formdata = "params="+getRequest('call_back', 1)+"&requestIndex=5&requestType=2";
		var inputOtp=document.getElementById('inputOtpWantUs').value;
		 if(inputOtp!="" || inputOtp !=''){
			 
			 getEncryptedString(inputOtp).then(function(encryptedOtp) {
				 document.getElementById('inputOtpWantUs1').value = encryptedOtp;
				 
			 });
				 
				 
				 var paramData= getRequest('call_back', 1);
					if(!paramData){
						var errorMsg = "Sorry for the inconvenience, please try again later.";
						showPOPMsg('errorOTPMsg',errorMsg, false);
						return false;
					}
					var formdata = "params="+paramData;
					formdata +="&requestIndex=5&requestType=2";
					if(uiType!=undefined && uiType!=""){
						formdata += "&uiType="+uiType;
					}
					jQuery.ajax({
					    type: "POST",
					    url: ctxPath + "/" + ajaxPostUrl,
						data: formdata,
						//headers : {"Accept-Encoding" : "gzip"},
					    success:function(htmlContent){
					    	removeDynamicLoader('confirmOtpWantUs');
					    	if(htmlContent==null || htmlContent ==undefined){
					    		removeDynamicLoader('confirmOtpWantUs');
					    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
				        		return false;
				        	}
					    	var stringMessage = [];
							var decryptedResponse = "";
							
							isEncrypted(htmlContent).then(function(result) {
								
								if (result) {
									
									
									getDecryptedResponse(htmlContent).then(function(decryptedResponse) {
										stringMessage = decryptedResponse.split("|");
										
										if(stringMessage[0]=="success"){
							        		//secretKey=getSecretKey();
							        		if(jQuery("#leadLoanPurposeId").val()==1){
							        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=HOME" width="1" height="1" />';
							        			}
							        		}else if(jQuery("#leadLoanPurposeId").val()==3){
							        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=AUTO" width="1" height="1" />';
							        			}
							        		}
							        		jQuery("#wantus_row_confirm").hide();
							        		//showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
							        		$("#afterGetACallBack").hide();
							        		var successMsg='<div class="thnk-msg">'
											+'<h2><span class="smile-icon"></span><br>Thank you </h2>'
											+'<p>Thank You for your interest. Our representative will contact you shortly.</p>'
											+'</div>';
							        		$("#errorOTPMsg").removeClass('error-msg-cbs')
							        		$("#errorOTPMsg").html(successMsg).show();
							        		jQuery("#resend_box").hide();
						   	        		setTimeout(function(){
						   	        			jQuery(".close").trigger("click");
							        			reinitiateCallback();
							        		}, 2000);
						   	        		if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
							        			fbq('track', 'Lead', { loan : 'Callback' });
							        		}
							        	}else if(stringMessage[0]=="error"){
							        		showPOPMsg('errorOTPMsg',stringMessage[1], false);
							        	}
										
									});
									
								} else {
									
									showPOPMsg('errorOTPMsg', 'Something went wrong', false);
								    removeDynamicLoader('confirmOtpWantUs');

									//stringMessage[0] = "error";
									//stringMessage[1] = "Something went wrong";
								}
							});
										
				        	return false;
					    },
					    error:function(jqXHR, textStatus){
					    	removeDynamicLoader('confirmOtpWantUs');
					    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
					    	return false;
					    }
					});
					return false;
		 }
		
	});
	
	jQuery(document).on("click","#leadApplyingFromIndiaWantUsNo", function() {
		var nrimobilediv='<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" class="w167 valid" id="appNRIMobileNo" value="" maxlength="12" name="lead.leadMobileNo" onfocus="M.toolTip_TM(\'Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.\', event, this, 130, -30, 5);">';
		jQuery("#mobileDetails").html(nrimobilediv);
		jQuery("#leadCountryId").val(0);
		jQuery("#country-div").show();
		jQuery("#city-div").hide();
	});

	jQuery(document).on("click","#leadApplyingFromIndiaWantUsYes", function(){
		var mobilediv='<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" class="w167 valid" id="mobile" value="" maxlength="10" name="lead.leadMobileNo">';
		jQuery("#mobileDetails").html(mobilediv);
		jQuery("#leadCityId").val(0);
		jQuery("#country-div").hide();
		jQuery("#city-div").show();
		jQuery("#cardCityGroup1").val(0);
		
	});
	
	jQuery(document).on("click","#cancelResend", function(){
		refreshCaptcha('HomePopcapImage');
		globalMobile = globalMobile.replace(/^(\d{6})(\d{4})$/, function() {
			return arguments[1].replace(/./g, '*') + arguments[2];
		});
		var html = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("click", "#changeMobileNumber", function(){
		refreshCaptcha('HomePopcapImage');
		$("#resendOtpWantUs").removeAttr('disabled');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="mobile" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">send</button>'+
				'<button class="resend-btn" id="cancelResend" name="cancelResend" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});


	//jQuery("#cancelResendNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#cancelResendNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});

	//jQuery("#changeMobileNumberNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#changeMobileNumberNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="nriMobileNo" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">Resend</button>'+
				'<button class="resend-btn" id="cancelResendNRI" name="cancelResendNRI" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("change","#leadCityId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	jQuery(document).on("change","#leadCountryId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#appNRIMobileNo").val("");
	});
	jQuery(document).on("change","#cardCityGroup1", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	
	jQuery(document).on("click","#retailLoanListing", function(){
		$(".mobile-landing").hide();
		$(".mobile-slider").hide();
		$(".retail-loan-listing").show();
		$('#footer').addClass('sticky-footer');
	});

	jQuery(document).on("click","#homeBack", function(){
		$(".mobile-landing").show();
		$(".mobile-slider").show();
		$(".retail-loan-listing").hide();
		$('#footer').removeClass('sticky-footer');
	});
});
jQuery(document).on("change","#leadLoanPurposeId", function(){
	if(this==undefined)return false;
	var selVal=this.value;
	
	if(selVal!=undefined){
		if(selVal>0){
			/*if(selVal==10){
				$("#citiesLeads").html($("#cardCities").html());
			}else{*/
				$("#citiesLeads").html($("#loanCities").html());
			/*}*/
		}
		return false;
	}
});

function getEncryptedString(str) {
	
	return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        var enc1 = aesUtil.encrypt(json.salt, json.iv, json.passPhrase, str);
		return enc1;
    
    }).catch(function() {
        return false;
    });
		
}

function isEncrypted(str) {
    
	if (str === '' || str.trim() === '') return Promise.resolve(false);
    return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        var dec1 = aesUtil.decrypt(json.salt, json.iv, json.passPhrase, str);
        var enc1 = aesUtil.encrypt(json.salt, json.iv, json.passPhrase, dec1);
        return enc1 == str;
    }).catch(function() {
        return false;
    });
}

function getDecryptedResponse(htmlContent) {

	return jQuery.ajax({
        type: 'post',
        url: ctxPath + "/" + "getCryptoConfig",
        dataType: 'json'
    }).then(function(json) {
        var aesUtil = new AesUtil(json.keySize, json.iterationCount);
        return aesUtil.decrypt(json.salt, json.iv, json.passPhrase, htmlContent);
    
    }).catch(function() {
        return false;
    });
	
}


function validateIndianMobileNo(indianMobileId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
		isValidForHomePageOTP=false;
		return false;
	}
	var mobile = jQuery('#'+indianMobileId).val();
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isMobile(mobile) && !jQuery('#'+indianMobileId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#mobile").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}
function validateNriMobileNo(nriMobieId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var mobile = jQuery('#'+nriMobieId).val();
	$(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
    	isValidForHomePageOTP=false;
		return false;
	}
	var countryRegion = $("#leadCountryId").val();
	if(countryRegion==null || countryRegion==undefined || countryRegion=="" || countryRegion==0){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select country code.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isNRIMobile(mobile) && !jQuery('#'+nriMobieId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isNRIMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#appNRIMobileNo").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}

$(document).on("change","#leadLoanPurposeId", function(){
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	
	var productTypeId=src.value;
	if(productTypeId==10){
		populateLanguageData(activeLanguages, 1, "leadLanguageId", "")
	}else{
		populateLanguageData(allLanguages, 1, "leadLanguageId", "")
	}
});

function populateLanguageData(data, selectedId, elementId){
	jQuery( '#'+elementId ).empty();
	var option= "";
	var selected="";
	for(var index=0; index<data.length;index++){
		var elementdata = data[index];
		option = '<option value="'+elementdata.key+'" '+selected+' >'+elementdata.value+'</option>';
		jQuery('#'+elementId).append(option);
	}
	disableById(elementId, false);
}
function openCallBackFormDiv(){
	jQuery("#tabSticker").addClass("hide-sticker");
	jQuery("#callbackBody").addClass("form-tab");
	jQuery(".home_middle_right_bottom").show();
	jQuery(".mobile-landing").hide();
	jQuery(".mobile-slider").hide();
}
function closeCallBackFormDiv(){
	jQuery("#tabSticker").removeClass("hide-sticker");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery(".home_middle_right_bottom").hide();
	jQuery(".mobile-landing").show();
	jQuery(".mobile-slider").show();
}


/SBI/src/com/mintstreet/common/util/SbiUtil.java
	
	
	package com.mintstreet.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.persistence.NoResultException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.mintstreet.common.bo.DocumentTypeUIBeanList;
import com.mintstreet.common.bo.RepaymentSchedule;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.MasterBank;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterBuilder;
import com.mintstreet.common.entity.MasterCarDealer;
import com.mintstreet.common.entity.MasterCertificate;
import com.mintstreet.common.entity.MasterCoApplicant;
import com.mintstreet.common.entity.MasterCorpSalaryPackage;
import com.mintstreet.common.entity.MasterCountry;
import com.mintstreet.common.entity.MasterDealer;
import com.mintstreet.common.entity.MasterDocumentType;
import com.mintstreet.common.entity.MasterEmployeeOccupationType;
import com.mintstreet.common.entity.MasterEmployer;
import com.mintstreet.common.entity.MasterEmploymentType;
import com.mintstreet.common.entity.MasterGraduation;
import com.mintstreet.common.entity.MasterIndustryType;
import com.mintstreet.common.entity.MasterInstitute;
import com.mintstreet.common.entity.MasterLabelTooltip;
import com.mintstreet.common.entity.MasterLoanCategory;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterLocality;
import com.mintstreet.common.entity.MasterProfession;
import com.mintstreet.common.entity.MasterSalesTeam;
import com.mintstreet.common.entity.MasterState;
import com.mintstreet.common.entity.MasterUniversity;
import com.mintstreet.common.entity.ValidateRequestConfig;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.service.ValidatorService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.autoloan.entity.MasterCarModel;
import com.mintstreet.loan.autoloan.entity.MasterCarVariant;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.MasterProject;
import com.mintstreet.loan.homeloan.service.HomeLoanService;


public class SbiUtil {

	private static final Logger logger = LogManager.getLogger(SbiUtil.class.getName());

	@Autowired
	private ValidatorService validatorService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private AutoLoanService autoLoanService;

	@Autowired
	private EducationLoanService educationLoanService;

	@Autowired
	private HomeLoanService homeLoanService;

	@Autowired
	CommunicationManagerImpl communicationManagerImpl;
	
	char[] passphrase = Constants.SBIUTIL_PASSPHRASE.toCharArray();
	
	public String OcasID = null;

	public String getOcasID() {
		return OcasID;
	}

	public void setOcasID(String ocasID) {
		OcasID = ocasID;
	}

	public JSONArray getCities(Integer loanType, Integer cityStateId, Integer isRACPC, Integer isScholar, Integer isNRI,
			Integer isEdvantage, String isXpressCreditIT) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> citiesMap = null;
			logger.info("SbiUtil.java LNo : 97 : getCities() for state id val " + cityStateId);
			if (cityStateId == null) {
				citiesMap = commonService.getStateCityDistrictBranchWithoutLoanType(2, loanType, cityStateId, null,
						null, isRACPC, isScholar, isNRI, isEdvantage, isXpressCreditIT);
			} else {
				citiesMap = commonService.getStateCityDistrictBranch(2, loanType, cityStateId, null, null, isRACPC,
						isScholar, isNRI, isEdvantage, isXpressCreditIT);
			}
			Map<Integer, String> districtMap = commonService.getStateCityDistrictBranch(3, loanType, cityStateId, null,
					null, isRACPC, isScholar, isNRI, isEdvantage, isXpressCreditIT);
			if (citiesMap != null && !citiesMap.isEmpty()) {
				for (Map.Entry<Integer, String> entry : citiesMap.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityCategory", 2);
					jsonArray.put(json);
				}
			}

			if (isXpressCreditIT == null) {
				if (districtMap != null && !districtMap.isEmpty()) {
					JSONObject jsonTemp = new JSONObject();
					jsonTemp.put("key", Constants.OTHER_ID);
					jsonTemp.put("value", Constants.OTHER_VALUE);
					jsonTemp.put("cityCategory", "D");
					jsonArray.put(jsonTemp);
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 129 : getCities() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 130 : getCities() ", e);
		}
		return jsonArray;
	}

	public JSONArray getCitiesForHomeLoan(Integer cityStateId) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
			if (cityStateId != 0) {
				mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, cityStateId, null, null,
						null, null, null, null, null);
			} else {
				mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, null, null, null, null,
						null, null, null, null);
			}
			if (mapCity != null && !mapCity.isEmpty()) {
				for (Map.Entry<Integer, String> entry : mapCity.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityCategory", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 157 : getCitiesForHomeLoan() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 156 : getCitiesForHomeLoan() ", e);
		}
		return jsonArray;
	}

	public JSONArray getDistrictByStateId(Integer loanType, Integer districtStateId, Integer isRACPC, Integer isScholar,
			Integer isNRI, Integer isEdvantage) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> districtMap = commonService.getStateCityDistrictBranch(3, loanType, districtStateId,
					null, null, isRACPC, isScholar, isNRI, isEdvantage, null);
			if (districtMap != null && !districtMap.isEmpty()) {
				for (Map.Entry<Integer, String> entry : districtMap.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityDefaultDisplay", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 180 : getDistrictByStateId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 233 : getDistrictByStateId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getDistrictByStateIdBId(Integer loanType, Integer districtStateId, Integer isRACPC,
			Integer isScholar, Integer isNRI, Integer isEdvantage, Integer isBidyaLakhmi) {
		JSONArray jsonArray = new JSONArray();
		try {
			if (districtStateId == 4 && isBidyaLakhmi != null && isBidyaLakhmi > 0) {
				Map<Integer, String> districtMapBidya = commonService.getStateCityDistrictBranchForBidya(3, loanType,
						districtStateId, null, null, null, null, null, null, null, 1);
				if (districtMapBidya != null && !districtMapBidya.isEmpty()) {
					for (Map.Entry<Integer, String> entry : districtMapBidya.entrySet()) {
						JSONObject json = new JSONObject();
						json.put("key", entry.getKey());
						json.put("value", entry.getValue());
						json.put("cityDefaultDisplay", 2);
						jsonArray.put(json);
					}
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 206 : getDistrictByStateId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 233 : getDistrictByStateId() ", e);
		}
		return jsonArray;

	}

	public JSONArray getCities() {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> mapCity = new LinkedHashMap<Integer, String>();
			mapCity = commonService.getStateCityDistrictBranch(2, Constants.HOME_LOAN_ID, null, null, null, null, null,
					null, null, null);
			if (mapCity != null && !mapCity.isEmpty()) {
				for (Map.Entry<Integer, String> entry : mapCity.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityCategory", 2);
					jsonArray.put(json);
				}
				JSONObject jsonTemp = new JSONObject();
				jsonTemp.put("key", Constants.OTHER_ID);
				jsonTemp.put("value", Constants.OTHER_VALUE);
				jsonArray.put(jsonTemp);
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 234 : getCities() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 258 : getCities() ", e);
		}
		return jsonArray;
	}

	public JSONArray getCarModelByCompanyId(int carCompanyId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterCarModel> models;
		try {
			models = autoLoanService.getCarModelByCompany(carCompanyId);
			if (models != null) {
				
				JSONObject json1 = new JSONObject();
				JSONObject json2 = new JSONObject();
				JSONObject json3 = new JSONObject();
				JSONObject json4 = new JSONObject();
				
				json1.put("Id", models.get(0).getModelCode());
				json1.put("Name", "Small Size vehicle");
				jsonArray.put(json1);
				json2.put("Id", models.get(0).getModelCode());
				json2.put("Name", "Multi Utility Vehicle(MUV)");
				jsonArray.put(json2);
				json3.put("Id", models.get(0).getModelCode());
				json3.put("Name", "Sports Utility Vehicle(SUV)");
				jsonArray.put(json3);
				json4.put("Id", models.get(0).getModelCode());
				json4.put("Name", "Others");
				jsonArray.put(json4);
				
			}
		} catch (NullPointerException e) {
			logger.info("SbiUtil.java LNo : 277 : getCarModelByCompanyId() ", e);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 346 : getCarModelByCompanyId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getCarVariantByModelId(int carModleId, Integer carCompanyId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterCarVariant> models;
		try {
			models = autoLoanService.getCarVariantByCarModelId(carModleId, carCompanyId);
			if (models != null) {
				for (MasterCarVariant carVarint : models) {
					if (carVarint != null) {
						JSONObject json = new JSONObject();
						json.put("Id", carVarint.getVariantCode());	//changed for car variant data
						json.put("Name", carVarint.getVariantName());
						json.put("Category", "NA");
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 306 : getCarVariantByModelId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 375 : getCarVariantByModelId() ", e);
		}
		return jsonArray;
	}

	public JSONArray instituteByCourseType(int courseType) {
		JSONArray jsonArray = new JSONArray();
		List<MasterInstitute> institutes = null;
		try {
			institutes = educationLoanService.instituteByCourseType(courseType);
			if (institutes != null) {
				for (MasterInstitute institute : institutes) {
					if (institute != null) {
						JSONObject json = new JSONObject();
						json.put("key", institute.getInstituteId());
						json.put("value", institute.getInstituteName());
						jsonArray.put(json);
					}
				}

				JSONObject json = new JSONObject();
				json.put("key", Constants.OTHER_ID);
				json.put("value", Constants.OTHER_VALUE);
				jsonArray.put(json);

			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 335 : instituteByCourseType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 453 : instituteByCourseType() ", e);
		}
		return jsonArray;
	}

	public JSONObject universityByString(String query, Integer universityIsInIndia) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterUniversity> universities;
		try {
			universities = educationLoanService.getAllUniversityByQuerySting(query, universityIsInIndia);
			if (universities != null) {
				for (MasterUniversity university : universities) {
					if (university != null) {
						suggestions.put(university.getUniversityName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 358 : universityByString() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 474 : universityByString() ", e);
		}
		return json;
	}

	public String instituteCategoryType(Integer instituteId) throws NoResultException {
		MasterInstitute masterInstitute = educationLoanService.instituteCategoryById(instituteId);
		return masterInstitute != null ? masterInstitute.getInstituteCategeroy() : "";
	}

	public JSONArray certificateByCourseType(int courseType) {
		JSONArray jsonArray = new JSONArray();
		List<MasterCertificate> certificates = null;
		try {
			certificates = educationLoanService.getCertificateByCourseType(courseType);
			if (certificates != null) {
				for (MasterCertificate certificate : certificates) {
					if (certificates != null) {
						JSONObject json = new JSONObject();
						json.put("key", certificate.getCertificateId());
						json.put("value", certificate.getCertificateName());
						jsonArray.put(json);
					}
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 387 : certificateByCourseType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 501 : certificateByCourseType() ", e);
		}
		return jsonArray;
	}

	public JSONArray gradutionByCourseType(int courseType) {
		JSONArray jsonArray = new JSONArray();
		List<MasterGraduation> graduations = null;
		try {
			graduations = educationLoanService.gradutionByCourseType(courseType);
			if (graduations != null) {
				for (MasterGraduation graduation : graduations) {
					if (graduation != null) {
						JSONObject json = new JSONObject();
						json.put("key", graduation.getGraduationId());
						json.put("value", graduation.getGraduationName());
						jsonArray.put(json);
					}
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 411 : gradutionByCourseType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 523 : gradutionByCourseType() ", e);
		}
		return jsonArray;
	}

	public JSONArray getLocalites(int stateId, int cityId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterLocality> localities = null;
		try {
			localities = commonService.getlocalityByCityId(stateId, cityId);
			if (localities != null) {
				for (MasterLocality locality : localities) {
					if (localities != null) {
						JSONObject json = new JSONObject();
						json.put("cityId", locality.getLocalityId());
						json.put("cityName", locality.getLocalityName());
						json.put("cityDefaultDisplay", locality.getLocalityDefaultDisplay());
						jsonArray.put(json);
					}
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 436 : getLocalites() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 546 : getLocalites() ", e);
		}
		return jsonArray;

	}

	public JSONObject getLocalitesBasedOnSalesTeamIds(int stateId, int cityId, Integer isRACPC) {
		JSONObject json = new JSONObject();
		try {
			List<MasterSalesTeam> salesTeams = commonService.getAllHLSalesTeam(cityId);

			if (salesTeams != null && !salesTeams.isEmpty()) {
				List<Integer> salesTeamIds = new ArrayList<Integer>();
				for (MasterSalesTeam masterSalesTeam : salesTeams) {
					salesTeamIds.add(masterSalesTeam.getSalesTeamId());
				}

				List<MasterLocality> localities = commonService.getLocalityBySalesTeamIds(salesTeamIds);

				if (localities != null && !localities.isEmpty() && localities.size() > 1) {

					JSONArray jsonArray = new JSONArray();
					if (localities != null) {
						for (MasterLocality locality : localities) {
							if (localities != null) {
								JSONObject jsonInner = new JSONObject();
								jsonInner.put("cityId", locality.getLocalityId());
								jsonInner.put("cityName", locality.getLocalityName());
								jsonInner.put("cityDefaultDisplay", locality.getLocalityDefaultDisplay());
								jsonArray.put(jsonInner);
							}
						}
					}
					json.put("localities", jsonArray);
					json.put("populate", "locality");
				} else if (localities != null && localities.isEmpty()) {
					Integer isScholar = 0;
					json.put("districts", getDistrictByStateId(stateId, isRACPC, isScholar, null, null, null));
					json.put("populate", "district");
				} else {
					json.put("populate", "N/A");
				}
				return json;
			} else {
				json.put("populate", "N/A");
				return json;
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 489 : getLocalitesBasedOnSalesTeamIds() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 599 : getLocalitesBasedOnSalesTeamIds() ", e);
		}
		return json;
	}

	public JSONArray getBranchByDistrictId(Integer loanTypeId, Integer branchStateId, Integer branchCityId,
			Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI,
			Integer isEdvantage) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> mapBranch = commonService.getStateCityDistrictBranch(4, loanTypeId, branchStateId,
					branchCityId, branchDistrictId, branchIsRACPC, branchIsScholar, isNRI, isEdvantage, null);
			if (mapBranch != null && !mapBranch.isEmpty()) {
				for (Map.Entry<Integer, String> entry : mapBranch.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityDefaultDisplay", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 513 : getBranchByDistrictId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 621 : getBranchByDistrictId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getBranchByDistrictIdBidya(Integer loanTypeId, Integer branchStateId, Integer branchCityId,
			Integer branchDistrictId, Integer branchIsRACPC, Integer branchIsScholar, Integer isNRI,
			Integer isEdvantage, Integer isBidyaLakhmi) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> mapBranchBid = commonService.getStateCityDistrictBranchForBidya(4, loanTypeId,
					branchStateId, branchCityId, branchDistrictId, null, null, null, null, null, isBidyaLakhmi);
			if (mapBranchBid != null && !mapBranchBid.isEmpty()) {
				for (Map.Entry<Integer, String> entry : mapBranchBid.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityDefaultDisplay", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 537 : getBranchByDistrictId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 621 : getBranchByDistrictId() ", e);
		}
		return jsonArray;

	}

	public JSONArray getBranchByInstitute(String instituteName) {
		JSONArray jsonArray = new JSONArray();
		try {
			MasterInstitute masterInstitute = commonService.getInstituteByInstituteName(instituteName);
			if (masterInstitute != null) {
				JSONObject json = new JSONObject();

				if (masterInstitute.getInstituteDefaultBranch() != null) {
					MasterBranch branch = commonService.getBranchById(masterInstitute.getInstituteDefaultBranch());
					if (branch != null) {
						json.put("key", masterInstitute.getInstituteDefaultBranch());
						json.put("value", branch.getBranchName());
						json.put("cityDefaultDisplay", branch.getBranchDefaultDisplay());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 563 : getBranchByInstitute() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 644 : getBranchByInstitute() ", e);
		}
		return jsonArray;
	}

	public JSONArray getBranchByDistrictId(Integer loanTypeId, Integer branchDistrictId) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> mapBranch = commonService.getStateCityDistrictBranch(4, loanTypeId, null, null,
					branchDistrictId, null, null, null, null, null);
			if (mapBranch != null && !mapBranch.isEmpty()) {
				for (Map.Entry<Integer, String> entry : mapBranch.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("cityDefaultDisplay", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 585 : getBranchByDistrictId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 667 : getBranchByDistrictId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getBranchByCityId(Integer loanTypeId, Integer branchCityId) {
		JSONArray jsonArray = new JSONArray();
		try {
			if (branchCityId != null) {
				Map<Integer, String> mapBranch = commonService.getStateCityDistrictBranch(4, loanTypeId, null,
						branchCityId, null, null, null, null, null, null);
				if (mapBranch != null && !mapBranch.isEmpty()) {
					for (Map.Entry<Integer, String> entry : mapBranch.entrySet()) {
						JSONObject json = new JSONObject();
						json.put("key", entry.getKey());
						json.put("value", entry.getValue());
						json.put("cityDefaultDisplay", 2);
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 609 : getBranchByCityId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 692 : getBranchByCityId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getIndustryTypeByLoanId(Integer loanTypeId) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<MasterIndustryType> industries = commonService.getIndustryTypeByLoanId(loanTypeId);
			if (industries != null) {
				for (MasterIndustryType industry : industries) {
					if (industry != null) {
						JSONObject json = new JSONObject();
						json.put("key", industry.getIndustryTypeId());
						json.put("value", industry.getIndustryName());
						json.put("defaultDisplay", 0);
						jsonArray.put(json);
					}
				}
				if (loanTypeId != Constants.PERSONAL_LOAN_ID) {
					JSONObject jsonout = new JSONObject();
					jsonout.put("key", Constants.OTHER_ID);
					jsonout.put("value", Constants.OTHER_VALUE);
					jsonout.put("defaultDisplay", 0);
					jsonArray.put(jsonout);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 639 : getIndustryTypeByLoanId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 719 : getIndustryTypeByLoanId() ", e);
		}
		return jsonArray;

	}

	public JSONArray getAllSalaryPackageByLoanType(Integer loanTypeId, Integer industryTypeId) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<MasterCorpSalaryPackage> salaryPackages = null;

			if (industryTypeId == null) {
				salaryPackages = commonService.getAllSalaryPackageByLoanType(loanTypeId);
			} else {
				salaryPackages = commonService.getAllSalaryPackageByLoanTypeAndIndustryTypeId(loanTypeId,
						industryTypeId);
			}
			if (salaryPackages != null) {
				for (MasterCorpSalaryPackage salaryPackage : salaryPackages) {
					if (salaryPackage != null) {
						JSONObject json = new JSONObject();
						json.put("key", salaryPackage.getCorpSalPackId());
						json.put("value", salaryPackage.getCorpSalPackTitle());
						json.put("defaultDisplay", 0);
						jsonArray.put(json);
					}
				}
				
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 671 : getAllSalaryPackageByLoanType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 752 : getAllSalaryPackageByLoanType() ", e);
		}
		return jsonArray;
	}

	
	public JSONArray getAllLoanPurpose(Integer loanTypeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterLoanPurpose> loanPurposes;
		try {
			if (loanTypeId == null) {
				loanPurposes = commonService.getAllLoanPurpose();
			} else {
				loanPurposes = commonService.getAllLoanPurposeByLoanType(loanTypeId);
			}

			if (loanPurposes != null) {
				for (MasterLoanPurpose loanPurpose : loanPurposes) {
					if (loanPurpose != null) {
						JSONObject json = new JSONObject();
						json.put("loanPurposeId", loanPurpose.getLpId());
						json.put("lpDisplayOrder", loanPurpose.getLpDispalyOrder());
						json.put("loanPurposeType", loanPurpose.getLpTypeValue());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 702 : getAllLoanPurpose() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 780 : getAllLoanPurpose() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllLoanPurposeLinks() {
		JSONArray jsonArray = new JSONArray();
		List<MasterLoanPurpose> loanPurposes;
		try {
			loanPurposes = commonService.getAllLoanPurpose();
			if (loanPurposes != null) {
				for (MasterLoanPurpose loanPurpose : loanPurposes) {
					if (loanPurpose != null) {
						JSONObject json = new JSONObject();
						json.put("key", loanPurpose.getLpId());
						json.put("value", loanPurpose.getLpUrl());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 725 : getAllLoanPurpose() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 780 : getAllLoanPurposeLinks() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllCoApplicantByLoanId(Integer laonId, Integer isNRI) {
		List<MasterCoApplicant> coApplicants;
		JSONArray jsonArray = new JSONArray();
		try {
			coApplicants = commonService.getAllCoApplicantByLoanId(laonId, isNRI);
			if (coApplicants != null) {
				int i = 0;
				for (MasterCoApplicant coApplicant : coApplicants) {

					if (coApplicant != null) {
						i++;
						JSONObject json = new JSONObject();
						json.put("key", coApplicant.getCoapplicantid());
						json.put("value", coApplicant.getCoapplicantrelation().replaceAll("\\'", "%27"));
						json.put("displayOrder", i);
						jsonArray.put(json);
					}

				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 753 : getAllCoApplicantByLoanId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 827 : getAllCoApplicantByLoanId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllCountries() {
		JSONArray jsonArray = new JSONArray();
		List<MasterCountry> countries;
		try {
			countries = commonService.getAllCountries();
			if (countries != null) {
				for (MasterCountry country : countries) {
					if (country != null) {

						JSONObject json = new JSONObject();
						json.put("key", country.getCountryId());
						json.put("value", country.getCountryName());
						json.put("displayOrder", country.getCountryCode());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 778 : getAllCountries() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 850 : getAllCountries() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllBank(int loanTypeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterBank> banks = null;
		try {
			if (loanTypeId == Constants.HOME_LOAN_ID) {
				banks = commonService.getAllBankForHL();
			} else if (loanTypeId == Constants.AUTO_LOAN_ID) {
				banks = commonService.getAllBankForAL();
			} else if (loanTypeId == Constants.PERSONAL_LOAN_ID) {
				banks = commonService.getAllBankForPL();
			}
			if (banks != null) {
				for (MasterBank bank : banks) {
					if (bank != null) {
						JSONObject json = new JSONObject();
						json.put("key", bank.getBankId());
						json.put("value", bank.getBankName());
						json.put("displayOrder", bank.getBankDisplayOrder());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 808 : getAllBank() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 906 : getAllBank() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllBank(Integer loanTypeId, Integer productSubTypeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterBank> banks = null;
		try {
			if (loanTypeId == Constants.HOME_LOAN_ID) {
				banks = commonService.getAllBankForHL();
			} else if (loanTypeId == Constants.AUTO_LOAN_ID) {
				banks = commonService.getAllBankForAL();
			} else if (loanTypeId == Constants.PERSONAL_LOAN_ID) {
				banks = commonService.getAllBankForPL();
			} else if (loanTypeId == Constants.EDUCATION_LOAN_ID) {
				if (productSubTypeId == 3) {
					banks = commonService.getAllBankForELTakeOver();
				}
			}
			if (banks != null) {
				for (MasterBank bank : banks) {
					if (bank != null) {
						JSONObject json = new JSONObject();
						json.put("key", bank.getBankId());
						json.put("value", bank.getBankName());
						json.put("displayOrder", bank.getBankDisplayOrder());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 842 : getAllBank() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 906 : getAllBank() ", e);
		}
		return jsonArray;
	}

	public JSONArray getCarDealer() {
		JSONArray jsonArray = new JSONArray();
		List<MasterCarDealer> dealers;
		try {
			dealers = commonService.getAllCarDealer();
			if (dealers != null) {
				for (MasterCarDealer dealer : dealers) {
					if (dealer != null) {
						JSONObject json = new JSONObject();
						json.put("key", dealer.getDealerTypeId());
						json.put("value", dealer.getDealerName());
						jsonArray.put(json);
					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("key", Constants.OTHER_ID);
			json.put("value", Constants.OTHER_VALUE);
			jsonArray.put(json);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 869 : getCarDealer() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 931 : getCarDealer() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllProfession(Integer productTypeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterProfession> professions;
		try {
			professions = commonService.getAllProfession(productTypeId);
			if (professions != null) {
				for (MasterProfession profession : professions) {
					if (profession != null) {
						JSONObject json = new JSONObject();
						json.put("key", profession.getProfessionId());
						json.put("value", profession.getProfessionTitle());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 892 : getAllProfession() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 952 : getAllProfession() ", e);
		}
		return jsonArray;
	}

	public String getIPAddress() {
		String clientIPAddress = null;
		try {
			clientIPAddress = RequestUtil.getServletRequest().getHeader("X-Forwarded-For");
			if (clientIPAddress != null) {
				String[] clientIPAddressArray = clientIPAddress.split(" ");
				return clientIPAddressArray[0];
			} else {
				clientIPAddress = InetAddress.getLocalHost().getHostAddress();
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 911 : getclientIPAddress() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 972 : getclientIPAddress() ", e);
		}
		return clientIPAddress;
	}

	public JSONArray getAllLoanPurposeByParentId(Integer loanTypeId, Integer loanPurposeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterLoanCategory> loanCategories;
		try {
			loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(loanTypeId, loanPurposeId);
			if (loanCategories != null) {
				for (MasterLoanCategory category : loanCategories) {
					if (category != null) {
						JSONObject json = new JSONObject();
						json.put("key", category.getLoanCategoryId());
						json.put("value", category.getCategoryType());
						json.put("displayOrder", category.getDisplayOrder());


						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 937 : getAllLoanPurposeByParentId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 995 : getAllLoanPurposeByParentId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getAllStateId(Integer loanType, Integer isRACPC, Integer isScholar, Integer isNRI,
			Integer isEdvantage, String isXpressCreditIT) {
		JSONArray jsonArray = new JSONArray();
		try {
			Map<Integer, String> stateMap = commonService.getStateCityDistrictBranch(1, loanType, null, null, null,
					isRACPC, isScholar, isNRI, isEdvantage, isXpressCreditIT);
			if (stateMap != null && !stateMap.isEmpty()) {
				for (Map.Entry<Integer, String> entry : stateMap.entrySet()) {
					JSONObject json = new JSONObject();
					json.put("key", entry.getKey());
					json.put("value", entry.getValue());
					json.put("displayOrder", 2);
					jsonArray.put(json);
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 960 : getAllStateId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1070 : getAllStateId() ", e);
		}
		return jsonArray;
	}

	public JSONArray getLoanCatogory(Integer loanTypeId, Integer loanPurposeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterLoanCategory> loanCategories;
		try {
			if (SessionUtil.getLMSIntermediaryCatagories() != null) {
				
				loanCategories = commonService.getLoanCategoryByIds(SessionUtil.getLMSIntermediaryCatagories());
			} else {
				loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(loanTypeId, loanPurposeId);
			}
			if (loanCategories != null) {
				for (MasterLoanCategory category : loanCategories) {
					if (category != null) {
						JSONObject json = new JSONObject();
						json.put("key", category.getLoanCategoryId());
						json.put("value", category.getCategoryType());
						json.put("displayOrder", category.getDisplayOrder());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 989 : getLoanCatogory() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1118 : getLoanCatogory() ", e);
		}
		return jsonArray;
	}

	public JSONArray getPropertyType(Integer loanTypeId, Integer loanPurposeId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterLoanCategory> loanCategories;
		try {
			loanCategories = commonService.getAllLoanCategoryByLoanTypeAndPurposeId(loanTypeId, loanPurposeId);
			if (loanCategories != null) {
				MasterLoanCategory categoryp = commonService.getLoanCategoryById(11);
				JSONObject jsonp = new JSONObject();
				jsonp.put("key", categoryp.getLoanCategoryId());
				jsonp.put("value", categoryp.getCategoryType());
				jsonp.put("displayOrder", categoryp.getDisplayOrder());
				jsonArray.put(jsonp);

				for (MasterLoanCategory category : loanCategories) {
					if (category != null) {
						JSONObject json = new JSONObject();
						json.put("key", category.getLoanCategoryId());
						json.put("value", category.getCategoryType());
						json.put("displayOrder", category.getDisplayOrder());
						jsonArray.put(json);
					}
				}

			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1022 : getPropertyType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1150 : getPropertyType() ", e);
		}
		return jsonArray;
	}

	public JSONObject getAllEmployer(String query, boolean forPL, String isNasscom) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterEmployer> employers;
		try {
			
			if (query.matches("^[a-zA-Z0-9_-]*$")) {
				employers = commonService.getAllEmployerByQuerySting(query, forPL, isNasscom);
				if (employers != null) {
					for (MasterEmployer employer : employers) {
						if (employers != null) {
							suggestions.put(employer.getEmployerCompanyName());
						}
					}
				}
				json.put("query", query);
				json.put("suggestions", suggestions);
			} else {

				logger.info("Invalid Request.");
				json.put("error", "Invalid Request.");
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1045 : getAllEmployer() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1171 : getAllEmployer() ", e);
		}
		return json;
	}

	public JSONArray getAllProjectByBuilder(Integer projectBuilderId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterProject> projects = null;
		try {
			projects = homeLoanService.findProjectByBuilderId(projectBuilderId);
			if (projects != null) {
				for (MasterProject project : projects) {
					if (project != null) {
						JSONObject json = new JSONObject();
						json.put("key", project.getProjectId());
						json.put("value", project.getProjectName());
						json.put("defaultDisplay", 0);
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1069 : getAllProjectByBuilder() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1193 : getAllProjectByBuilder() ", e);
		}
		return jsonArray;
	}

	public JSONObject getAllBuilder(String query) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterBuilder> builders;
		try {
			builders = commonService.getAllBuilderByQuerySting(query);
			if (builders != null) {
				for (MasterBuilder builder : builders) {
					if (builder != null) {
						suggestions.put(builder.getBuilderName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1092 : getAllBuilder() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1214 : getAllBuilder() ", e);
		}
		return json;
	}

	public JSONObject getAllProject(String query) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterProject> projects;
		try {
			projects = homeLoanService.getAllProjectByQuerySting(query);
			if (projects != null) {
				for (MasterProject project : projects) {
					if (project != null) {
						suggestions.put(project.getProjectBuilderName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1115 : getAllProject() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1233 : getAllProject() ", e);
		}
		return json;
	}

	public JSONObject getPreownedCarDealer(String query) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterCarDealer> dealers;
		try {
			dealers = commonService.getPreownedCarDealer(query);
			if (dealers != null) {
				for (MasterCarDealer dealer : dealers) {
					if (dealer != null) {
						suggestions.put(dealer.getDealerName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);


		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1140 : getPreownedCarDealer() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1237 : getPreownedCarDealer() ", e);
		}
		return json;
	}

	public JSONObject getDealerByCompanyId(String query, Integer companyId) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterDealer> dealers;
		try {
			dealers = commonService.getDealerByCompanyId(query, companyId);
			if (dealers != null) {
				for (MasterDealer dealer : dealers) {
					if (dealer != null) {
						suggestions.put(dealer.getDealerName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1163 : getDealerByCompanyId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1259 : getDealerByCompanyId() ", e);
		}
		return json;
	}

	public JSONObject getAllCourseName(String query, Integer courseTypeId, Integer graduationIsInIndia) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterGraduation> courses;
		try {
			courses = educationLoanService.getAllCourseByQuerySting(query, courseTypeId, graduationIsInIndia);
			if (courses != null) {
				for (MasterGraduation course : courses) {
					if (course != null) {
						suggestions.put(course.getGraduationName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1186 : getAllCourseName() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1280 : getAllCourseName() ", e);
		}
		return json;
	}

	public JSONObject getAllInstituteName(String query, Integer courseTypeId, Integer instituteIsIndia,
			Integer loanTypeId) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterInstitute> institutes;
		try {
			institutes = educationLoanService.getAllInstituteByQuerySting(query, courseTypeId, instituteIsIndia,
					loanTypeId);
			if (institutes != null) {
				for (MasterInstitute institute : institutes) {
					if (institute != null) {
						suggestions.put(institute.getInstituteName());
					}
				}
			}
			json.put("query", query);
			json.put("suggestions", suggestions);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1211 : getAllInstituteName() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1301 : getAllInstituteName() ", e);
		}
		return json;
	}

	public Integer getVerificationCode(String mobile) {
		if (mobile == null) {
			return null;
		}
		if (Constants.DUMMY_MOBILE_NO.contains(mobile) || Constants.CBS_OTP_BYPASS) {
			return Integer.parseInt(Constants.DUMMY_MOBILE_OTP);
		} else {
			SecureRandom rand = new SecureRandom();
			return rand.nextInt(900000) + 100000;
		}
	}

	public static String getRandomAlphaNumericReferenceNumber() {

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		String yearInStringFormate = String.valueOf(year);


		GregorianCalendar gc = new GregorianCalendar();
		int julianDay = gc.get(GregorianCalendar.DAY_OF_YEAR);

		String jDay = "" + julianDay;
		if (julianDay < 10) {
			jDay = "00" + jDay;
		}
		if (julianDay >= 10 && julianDay < 100) {
			jDay = "0" + jDay;
		}

		String fixedString = "SBIOC" + yearInStringFormate + jDay + Constants.BRANCH_CODE;

		int random = new SecureRandom().nextInt(90000000) + 10000000;
		String referenceNumber = fixedString + random;
		return referenceNumber;
	}

	public String getVerificationCodeForEmail(String emailid) {
		if (emailid != null && (Constants.DUMMY_EMAIL_ID.contains(emailid) || Constants.CBS_OTP_BYPASS)) {
			return Constants.DUMMY_EMAIL_OTP;
		} else {
			return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
		}
	}


	public Map<Double, String> generateEligibiltyList(double minEligibility, double maxEligibility,
			int numberOfDecimalPlaces, double steps) {
		Map<Double, String> map = new LinkedHashMap<Double, String>();
		double tempLoanEligibility = minEligibility;
		double loanEligiale = minEligibility;
		java.text.DecimalFormat df = null;
		java.text.DecimalFormat tempdf = new java.text.DecimalFormat("#0.00");

		double tempSteps = steps;
		if (numberOfDecimalPlaces == 1) {
			df = new java.text.DecimalFormat("#0.00");


		}
		if (numberOfDecimalPlaces == 2) {
			df = new java.text.DecimalFormat("#0.00");


		}
		String suffix = " lakhs";
		do {
			if (tempLoanEligibility <= 1) {
				if (numberOfDecimalPlaces == 1) {
					map.put(tempLoanEligibility, df.format(tempLoanEligibility) + " lakh");
				} else if (numberOfDecimalPlaces == 2) {
					map.put(tempLoanEligibility, df.format(tempLoanEligibility) + " lakh");
				}
			} else {
				if (numberOfDecimalPlaces == 1) {
					map.put(tempLoanEligibility, df.format(tempLoanEligibility) + suffix);
				} else if (numberOfDecimalPlaces == 2) {
					map.put(tempLoanEligibility, df.format(tempLoanEligibility) + suffix);
				}
			}
			tempLoanEligibility = new Double(tempdf.format(tempLoanEligibility + tempSteps));
			loanEligiale = loanEligiale + steps;
		} while (loanEligiale <= maxEligibility);
		if (tempLoanEligibility > maxEligibility) {
			tempLoanEligibility = maxEligibility;
		}
		if (tempLoanEligibility <= 1) {
			if (numberOfDecimalPlaces == 1) {
				map.put(tempLoanEligibility, df.format(tempLoanEligibility) + " lakh");
			} else if (numberOfDecimalPlaces == 2) {
				map.put(tempLoanEligibility, df.format(tempLoanEligibility) + " lakh");
			}
		} else {
			if (numberOfDecimalPlaces == 1) {
				map.put(tempLoanEligibility, df.format(tempLoanEligibility) + suffix);
			} else if (numberOfDecimalPlaces == 2) {
				map.put(tempLoanEligibility, df.format(tempLoanEligibility) + suffix);
			}
		}
		Map<Double, String> manualEligVal = new TreeMap<Double, String>(map);
		return manualEligVal;
	}

	public Map<Integer, String> generateTenureList(Integer minTenure, Integer maxTenure, boolean isMonths) {
		Map<Integer, String> manualTenureVal = new LinkedHashMap<Integer, String>();
		for (int index = minTenure; index <= maxTenure; index++) {
			if (index == 1) {
				if (isMonths) {
					manualTenureVal.put(index, index + " month");
				} else {
					manualTenureVal.put(index, index + " year");
				}
			} else {
				if (isMonths) {
					manualTenureVal.put(index, index + " months");
				} else {
					manualTenureVal.put(index, index + " years");
				}
			}
		}
		return manualTenureVal;
	}

	public double getEMI(double loanAmount, int tenure, double interestRate, int isAdvanceEmiOption) {
		double emi = 0;
		double IRP = 0;
		double PW = 0;
		double PW1 = 0;
		double PW2 = 0;

		if (isAdvanceEmiOption == 1) {
			IRP = getIRP(interestRate);
			PW = Math.pow((1 + IRP), tenure); 


			PW1 = (PW - 1);
			emi = (loanAmount * IRP * PW) / PW1;
		} else {
			IRP = getIRP(interestRate);
			PW = Math.pow((1 + IRP), tenure); 


			PW1 = (PW - 1);
			PW2 = Math.pow((1 + IRP), (tenure - 1)); 


			emi = (loanAmount * IRP * PW2) / PW1;
		}

		return emi;
	}

	public double getReverseLoanAmount(double emi, double interestRate, int tenure, int isAdvanceEmiOption) {
		double IRP = 0;
		double PW = 0;
		double PW1 = 0;
		double PW2 = 0;
		double loanAmount = 0;
		if (isAdvanceEmiOption == 1) {
			IRP = getIRP(interestRate);
			PW = Math.pow((1 + IRP), tenure); 


			PW1 = (PW - 1);

			loanAmount = (emi * PW1) / (IRP * PW);
		} else {
			IRP = getIRP(interestRate);
			PW = Math.pow((1 + IRP), tenure); 


			PW1 = (PW - 1);
			PW2 = Math.pow((1 + IRP), (tenure - 1)); 



			loanAmount = (emi * PW1) / (IRP * PW2);
		}
		return loanAmount;
	}

	public double getIRP(double interestRate) {
		double IRP = (interestRate / 100) / 12; 

		return IRP;
	}


	public List<RepaymentSchedule> generateRepaymentList(ApplicationFormHomeLoan appForm, int tenure) {
		List<RepaymentSchedule> repaymentSchedule = new ArrayList<RepaymentSchedule>(tenure);
		List<String> monthTranslator = new ArrayList<String>();
		monthTranslator.add("Jan");
		monthTranslator.add("Feb");
		monthTranslator.add("Mar");
		monthTranslator.add("Apr");
		monthTranslator.add("May");
		monthTranslator.add("Jun");
		monthTranslator.add("Jul");
		monthTranslator.add("Aug");
		monthTranslator.add("Sep");
		monthTranslator.add("Oct");
		monthTranslator.add("Nov");
		monthTranslator.add("Dec");

		int currentMonth = DateUtil.getCurrentMonthInNumeric();


		int currentYear = 0;
		try {
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1432 : generateRepaymentList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1487 : generateRepaymentList() ", e);
		}

		String monthPart = "";
		String yearPart = "";
		for (int idx = 1; idx <= tenure; idx++) {
			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);


			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setBegninngLoanAmount(appForm.getAppLoanAmount() * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
			if (idx <= 12) {
				schecule.setInterest(appForm.getAppBhlInterestRate());
				schecule.setPrincipal(appForm.getAppBhlFirstInterestEmi());
			} else {
				schecule.setInterest(appForm.getAppLoanInterestRate());
				schecule.setPrincipal(appForm.getAppLoanEmi());
			}
			schecule.setRemainingLoanAmount(appForm.getAppLoanAmount() * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
			repaymentSchedule.add(schecule);
		}
		return repaymentSchedule;
	}


	public List<RepaymentSchedule> generateRepaymentList(double eligibiligy, int tenure, double interestRate,
			double emi, int rateType, int emiType, int moratoriumMonths, int moratoriumIRType,
			double loanEligibiltyMultiplierFactor, String hasPropertyRentedYes, Integer loanQuoteMonthStartDateOflease,
			Integer loanQuoteYearStartDateOfLease) {
		double beginningLoanBalance = 0;
		double principal = 0;
		double perMonthInterest = 0;
		double remainingLoanAmount = 0;
		double cumulativeInterest = 0;

		DecimalFormat df = new DecimalFormat("00.000");
		String interestRateString = df.format(interestRate);

		interestRate = new Double(interestRateString);

		double IRP = (interestRate / 100) / 12;


		List<RepaymentSchedule> repaymentSchedule = new ArrayList<RepaymentSchedule>(tenure);
		List<String> monthTranslator = new ArrayList<String>();
		monthTranslator.add("Jan");
		monthTranslator.add("Feb");
		monthTranslator.add("Mar");
		monthTranslator.add("Apr");
		monthTranslator.add("May");
		monthTranslator.add("Jun");
		monthTranslator.add("Jul");
		monthTranslator.add("Aug");
		monthTranslator.add("Sep");
		monthTranslator.add("Oct");
		monthTranslator.add("Nov");
		monthTranslator.add("Dec");

		int currentMonth = DateUtil.getCurrentMonthInNumeric();


		int currentYear = 0;
		try {
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1505 : generateRepaymentList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1487 : generateRepaymentList() ", e);
		}
		if (hasPropertyRentedYes != null && "Y".equals(hasPropertyRentedYes)) {
			currentMonth = loanQuoteMonthStartDateOflease;
			currentYear = loanQuoteYearStartDateOfLease;
		}
		String monthPart = "";
		String yearPart = "";
		for (int idx = 1; idx <= tenure; idx++) {
			if (idx == 1) {
				if (rateType == 1) {

					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				} else {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				}
			} else {
				beginningLoanBalance = remainingLoanAmount;
			}
			if (emiType == 0 && idx == 1) {

				perMonthInterest = 0;
			} else {
				perMonthInterest = beginningLoanBalance * IRP;
			}

			if (idx > moratoriumMonths) {
				principal = emi - perMonthInterest;
				remainingLoanAmount = beginningLoanBalance - principal;
				cumulativeInterest = cumulativeInterest + perMonthInterest;

			} else {
				if (moratoriumIRType == 0) {

					remainingLoanAmount = beginningLoanBalance + perMonthInterest;
				} else if (moratoriumIRType == 1) {

					remainingLoanAmount = beginningLoanBalance;
				} else if (moratoriumIRType == 2) {

					if (idx == moratoriumMonths) {
						double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
						remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
					} else {
						remainingLoanAmount = beginningLoanBalance;
					}
				}
			}

			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);


			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setPrincipal(principal);
			schecule.setBegninngLoanAmount(beginningLoanBalance);
			if (idx > moratoriumMonths) {
				schecule.setEMI(emi);
			} else {
				schecule.setEMI(0);
			}

			schecule.setInterest(perMonthInterest);
			schecule.setCumulativeInterest(cumulativeInterest);
			schecule.setRemainingLoanAmount(remainingLoanAmount);
			
			repaymentSchedule.add(schecule);

		}
		return correctingRepaymentScheculeList(repaymentSchedule);

	}

	public List<RepaymentSchedule> generateRepaymentListGLBullet(double eligibiligy, int tenure, double interestRate,
			double emi, int rateType, int emiType, int moratoriumMonths, int moratoriumIRType,
			double loanEligibiltyMultiplierFactor, String hasPropertyRentedYes, Integer loanQuoteMonthStartDateOflease,
			Integer loanQuoteYearStartDateOfLease) {
		double beginningLoanBalance = 0;
		//double principal = 0;
		double perMonthInterest = 0;
		double remainingLoanAmount = 0;
		//double cumulativeInterest = 0;

		DecimalFormat df = new DecimalFormat("00.000");
		String interestRateString = df.format(interestRate);

		interestRate = new Double(interestRateString);

		double IRP = (interestRate / 100) / 12;

		List<RepaymentSchedule> repaymentSchedule = new ArrayList<RepaymentSchedule>(tenure);
		List<String> monthTranslator = new ArrayList<String>();
		monthTranslator.add("Jan");
		monthTranslator.add("Feb");
		monthTranslator.add("Mar");
		monthTranslator.add("Apr");
		monthTranslator.add("May");
		monthTranslator.add("Jun");
		monthTranslator.add("Jul");
		monthTranslator.add("Aug");
		monthTranslator.add("Sep");
		monthTranslator.add("Oct");
		monthTranslator.add("Nov");
		monthTranslator.add("Dec");

		int currentMonth = DateUtil.getCurrentMonthInNumeric();


		int currentYear = 0;
		try {
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1624 : generateRepaymentList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1487 : generateRepaymentList() ", e);
		}
		if (hasPropertyRentedYes != null && "Y".equals(hasPropertyRentedYes)) {
			currentMonth = loanQuoteMonthStartDateOflease;
			currentYear = loanQuoteYearStartDateOfLease;
		}
		String monthPart = "";
		String yearPart = "";
		for (int idx = 1; idx <= tenure; idx++) {
			if (idx == 1) {
				beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
			} else {
				beginningLoanBalance = remainingLoanAmount;
			}

			perMonthInterest = beginningLoanBalance * IRP;
			remainingLoanAmount = beginningLoanBalance + perMonthInterest;

			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);

			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setBegninngLoanAmount(beginningLoanBalance);
			schecule.setInterest(perMonthInterest);
			schecule.setRemainingLoanAmount(remainingLoanAmount);
			repaymentSchedule.add(schecule);
		}
		return repaymentSchedule;
	}

	public List<RepaymentSchedule> correctingRepaymentScheculeList(List<RepaymentSchedule> repaymentScheculeList) {
		if (repaymentScheculeList != null && !CollectionUtils.isEmpty(repaymentScheculeList)) {
			int lastIndex = repaymentScheculeList.size() - 1;
			RepaymentSchedule lastSchedule = repaymentScheculeList.get(lastIndex);
			if (lastSchedule.getRemainingLoanAmount() < -1) {
				int adjustedAmount = (int) (lastSchedule.getRemainingLoanAmount() * -1);
				for (int i = lastIndex - 1; i > 0; i--) {
					if (adjustedAmount == 0) {
						break;
					}
					RepaymentSchedule element = repaymentScheculeList.get(i);
					element.setRemainingLoanAmount(element.getRemainingLoanAmount() + 1);
					RepaymentSchedule changeStartingAmount = repaymentScheculeList.get(i + 1);
					changeStartingAmount.setBegninngLoanAmount(element.getRemainingLoanAmount());
					adjustedAmount--;
				}
			} else if (lastSchedule.getRemainingLoanAmount() > 1) {
				int adjustedAmount = (int) (lastSchedule.getRemainingLoanAmount());
				for (int i = lastIndex - 1; i > 0; i--) {
					if (adjustedAmount == 0) {
						break;
					}
					RepaymentSchedule element = repaymentScheculeList.get(i);
					element.setRemainingLoanAmount(element.getRemainingLoanAmount() - 1);
					RepaymentSchedule changeStartingAmount = repaymentScheculeList.get(i + 1);
					changeStartingAmount.setBegninngLoanAmount(element.getRemainingLoanAmount());
					adjustedAmount--;
				}
			}
			lastSchedule.setRemainingLoanAmount(0);
		}
		return repaymentScheculeList;
	}

	public List<RepaymentSchedule> generateRepaymentList(double eligibiligy, int tenure, double interestRate,
			double emi, int rateType, int emiType, int moratoriumMonths) {
		double beginningLoanBalance = 0;
		double principal = 0;
		double perMonthInterest = 0;
		double remainingLoanAmount = 0;
		double cumulativeInterest = 0;
		DecimalFormat df = new DecimalFormat("00.000");
		String interestRateString = df.format(interestRate);

		double emi1 = getEMI(eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR, tenure, interestRate, emiType);
		interestRate = new Double(interestRateString);
		double IRP = (interestRate / 100) / 12;
		double preEmi = (eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR * IRP);
		double emi2 = (emi1 * 1.05);

		int monthSlab1 = 36;
		int monthSlab2 = 36;
		int monthSlab3 = tenure - moratoriumMonths - monthSlab1 - monthSlab2;

		int counter1 = moratoriumMonths + monthSlab1;
		int counter2 = counter1 + monthSlab2;
		int counter3 = counter2 + monthSlab3;
		String message = "";


		List<RepaymentSchedule> repaymentSchedule = new ArrayList<RepaymentSchedule>(tenure);
		List<String> monthTranslator = new ArrayList<String>();
		monthTranslator.add("Jan");
		monthTranslator.add("Feb");
		monthTranslator.add("Mar");
		monthTranslator.add("Apr");
		monthTranslator.add("May");
		monthTranslator.add("Jun");
		monthTranslator.add("Jul");
		monthTranslator.add("Aug");
		monthTranslator.add("Sep");
		monthTranslator.add("Oct");
		monthTranslator.add("Nov");
		monthTranslator.add("Dec");
		int currentMonth = DateUtil.getCurrentMonthInNumeric();

		int currentYear = 0;
		try {
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1741 : generateRepaymentList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1487 : generateRepaymentList() ", e);
		}
		String monthPart = "";
		String yearPart = "";
		for (int idx = 1; idx <= tenure; idx++) {
			if (idx == 1) {
				if (rateType == 1) {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				} else {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				}
			} else {
				beginningLoanBalance = remainingLoanAmount;
			}
			if (idx <= moratoriumMonths) {
				if (idx == 1) {
					emi = preEmi;
					message = idx + "-" + moratoriumMonths + " = " + emi;
				}
			} else if (idx <= counter1) {
				if (idx == moratoriumMonths + 1) {
					emi = emi1;
					message = message + "|" + idx + "-" + counter1 + " = " + emi;
					double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
					remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
				}
			} else if (idx <= counter2) {
				if (idx == counter1 + 1) {
					double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
					remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
					emi = emi2;
					message = message + "|" + idx + "-" + counter2 + " = " + emi;
				}
			} else if (idx <= counter3) {
				if (idx == counter2 + 1) {
					emi = getEMI(remainingLoanAmount, monthSlab3, interestRate, 1);
					message = message + "|" + idx + "-" + counter3 + " = " + emi;
				}
			}
			if (emiType == 0 && idx == 1) {
				perMonthInterest = 0;
			} else {
				perMonthInterest = beginningLoanBalance * IRP;
			}
			principal = emi - perMonthInterest;
			remainingLoanAmount = beginningLoanBalance - principal;
			cumulativeInterest = cumulativeInterest + perMonthInterest;

			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);
			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setPrincipal(principal);
			schecule.setBegninngLoanAmount(beginningLoanBalance);
			if (idx > moratoriumMonths) {
				schecule.setEMI(emi);
			} else {
				schecule.setEMI(0);
			}
			schecule.setInterest(perMonthInterest);
			schecule.setCumulativeInterest(cumulativeInterest);
			schecule.setRemainingLoanAmount(remainingLoanAmount);
			
			repaymentSchedule.add(schecule);
		}

		return correctingRepaymentScheculeList(repaymentSchedule);
	}

	public String getFlexiPayEmis(double eligibiligy, int tenure, double interestRate, double emi, int rateType,
			int emiType, int moratoriumMonths) {
		double beginningLoanBalance = 0;
		double principal = 0;
		double perMonthInterest = 0;
		double remainingLoanAmount = 0;
		double cumulativeInterest = 0;
		DecimalFormat df = new DecimalFormat("00.000");
		String interestRateString = df.format(interestRate);

		double emi1 = getEMI(eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR, tenure, interestRate, emiType);
		interestRate = new Double(interestRateString);
		double IRP = (interestRate / 100) / 12;
		double preEmi = (eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR * IRP);
		double emi2 = (emi1 * 1.05);
		int monthSlab1 = 36;
		int monthSlab2 = 36;
		int monthSlab3 = tenure - moratoriumMonths - monthSlab1 - monthSlab2;
		int counter1 = moratoriumMonths + monthSlab1;
		int counter2 = counter1 + monthSlab2;
		int counter3 = counter2 + monthSlab3;
		String message = "";
		for (int idx = 1; idx <= tenure; idx++) {
			if (idx == 1) {
				if (rateType == 1) {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				} else {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				}
			} else {
				beginningLoanBalance = remainingLoanAmount;
			}
			if (idx <= moratoriumMonths) {
				if (idx == 1) {
					emi = preEmi;
					message = idx + "-" + moratoriumMonths + "=" + emi;
				}
			} else if (idx <= counter1) {
				if (idx == moratoriumMonths + 1) {
					emi = emi1;
					message = message + "|" + idx + "-" + counter1 + "=" + emi;
					double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
					remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
				}
			} else if (idx <= counter2) {
				if (idx == counter1 + 1) {
					double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
					remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
					emi = emi2;
					message = message + "|" + idx + "-" + counter2 + "=" + emi;
				}
			} else if (idx <= counter3) {
				if (idx == counter2 + 1) {
					emi = getEMI(remainingLoanAmount, monthSlab3, interestRate, 1);
					message = message + "|" + idx + "-" + counter3 + "=" + emi;
				}
			}
			if (emiType == 0 && idx == 1) {
				perMonthInterest = 0;
			} else {
				perMonthInterest = beginningLoanBalance * IRP;
			}
			principal = emi - perMonthInterest;
			remainingLoanAmount = beginningLoanBalance - principal;
			cumulativeInterest = cumulativeInterest + perMonthInterest;
		}
		return message;
	}

	public double roundOffValue(double number) {
		return Math.round(number * 100000) / 100000.0d;
	}

	public BigDecimal convertDoubleToBigDecimalValue(Double amount) {
		BigDecimal b;
		if (amount != null) {
			b = new BigDecimal(amount.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} else {
			return null;
		}

		return b;
	}

	public String getApplicationReferenceId(String productInitialString, String sourceInitialString,
			String lastReferenceNumber) {
		String referenceId = null;
		try {
			String existingMonth = "";
			String existingYear = "";
			String lastSequenceNumber = "";
			int month = DateUtil.getCurrentMonth();
			String currentMonth = String.valueOf((month > 9 ? month : "0" + month));
			String currentYear = DateUtil.getCurrentYearTwoDigit();

			if (lastReferenceNumber == null) {
				existingMonth = currentMonth;
				existingYear = currentYear;
				lastSequenceNumber = "0";
			} else {
				existingMonth = lastReferenceNumber.substring(2, 4);
				existingYear = lastReferenceNumber.substring(4, 6);
				lastSequenceNumber = lastReferenceNumber.substring(6, (lastReferenceNumber.length()));
			}

			if (existingMonth.equalsIgnoreCase(currentMonth) && existingYear.equalsIgnoreCase(currentYear)) {
				lastSequenceNumber = String.valueOf(Integer.parseInt(lastSequenceNumber) + 1);
			} else {
				lastSequenceNumber = "1";
			}
			String sequentialNumber = null;
			String productIdLength = String.valueOf(lastSequenceNumber);
			if (productIdLength.length() == 1) {
				sequentialNumber = "00000" + lastSequenceNumber;
			} else if (productIdLength.length() == 2) {
				sequentialNumber = "0000" + lastSequenceNumber;
			} else if (productIdLength.length() == 3) {
				sequentialNumber = "000" + lastSequenceNumber;
			} else if (productIdLength.length() == 4) {
				sequentialNumber = "00" + lastSequenceNumber;
			} else if (productIdLength.length() == 5) {
				sequentialNumber = "0" + lastSequenceNumber;
			} else {
				sequentialNumber = "" + lastSequenceNumber;
			}

			referenceId = productInitialString + sourceInitialString + (month > 9 ? month : "0" + month) + currentYear
					+ sequentialNumber;
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1945 : getApplicationReferenceId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1666 : getApplicationReferenceId() ", e);
		}
		return referenceId;
	}

	public JSONArray populateJSValidation(Integer requestIndex, Integer loanTypeId) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<ValidateRequestConfig> validateRequestConfigList = validatorService
					.getValidateRequestConfig(requestIndex, loanTypeId);
			for (ValidateRequestConfig validateRequestConfig : validateRequestConfigList) {
				JSONObject json = new JSONObject();
				json.put("parent_id", validateRequestConfig.getElemParentId());
				json.put("exact_value", validateRequestConfig.getReqValidOnChangeVal());
				json.put("index_id", requestIndex);
				json.put("data_type_id", validateRequestConfig.getElementDateTypeId());
				json.put("data_type_name", validateRequestConfig.getReqValiName());
				json.put("loan_type_id", validateRequestConfig.getLoanTypeId());
				json.put("validation_name", validateRequestConfig.getReqValiName());
				json.put("min_value", validateRequestConfig.getReqValiMinValue());
				json.put("max_value", validateRequestConfig.getReqValiMaxValue());
				json.put("min_length", validateRequestConfig.getReqValiMinLength());
				json.put("max_length", validateRequestConfig.getReqValiMaxLength());
				json.put("conditional_mandatory", validateRequestConfig.getReqValiIsCondition());
				json.put("mandatory", validateRequestConfig.getReqValiIsManadat());
				json.put("display_order", validateRequestConfig.getReqValiDispOrd());
				json.put("message_name", validateRequestConfig.getReqValiMsgNm());

				jsonArray.put(json);
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1978 : populateJSValidation() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1710 : populateJSValidation() ", e);
		}
		return jsonArray;
	}

	public DocumentTypeUIBeanList generateUIBeanDocumentTypeList(Integer loanTypeId, Integer loanProductId) {
		DocumentTypeUIBeanList documentTypeList = new DocumentTypeUIBeanList();
		try {
			documentTypeList = new DocumentTypeUIBeanList();
			Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
			List<MasterDocumentType> documents = commonService.getDocumentTypeList(loanTypeId, loanProductId);
			if (documents != null && !documents.isEmpty()) {
				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				for (MasterDocumentType document : documents) {
					if (document.getDocumentTypeAddressProof() == 1) {
						maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
					}
				}
				documentTypeList.setResidanceProof(maps);

				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				for (MasterDocumentType document : documents) {
					if (document.getDocumentTypeBankStatement() == 1) {
						maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
					}
				}
				documentTypeList.setBankStementProof(maps);



				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				for (MasterDocumentType document : documents) {
					if (document.getDocumentTypeEmploymentProof() == 1) {
						maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
					}
				}
				documentTypeList.setEmploymentProof(maps);


				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				for (MasterDocumentType document : documents) {
					if (document.getDocumentTypeIdentityProof() == 1) {
						maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
					}
				}
				documentTypeList.setIdentityProof(maps);


				maps = null;
				maps = new LinkedHashMap<Integer, String>();
				for (MasterDocumentType document : documents) {
					if (document.getDocumentTypeIncomeProof() == 1) {
						maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
					}
				}
				documentTypeList.setIncomeProof(maps);

				if (loanTypeId != null && loanTypeId.equals(Constants.HOME_LOAN_ID)) {

				}

				if (loanTypeId != null && loanTypeId.equals(Constants.AUTO_LOAN_ID)) {

					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					for (MasterDocumentType document : documents) {
						if (document.getDocumentTypeAutoDetailsProof() == 1) {
							maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
						}
					}
					documentTypeList.setAutoDetailsProof(maps);
				}

				if (loanTypeId != null && loanTypeId.equals(Constants.PERSONAL_LOAN_ID)) {

					maps = new LinkedHashMap<Integer, String>();
					for (MasterDocumentType document : documents) {

						if (document.getDocumentTypePensionProof() != null
								&& document.getDocumentTypePensionProof() == 1) {
							maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
						}
					}
					documentTypeList.setPensionProof(maps);


					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					for (MasterDocumentType document : documents) {
						if (document.getDocumentTypePropertyProof() == 1) {
							maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
						}
					}
					documentTypeList.setPropertyProof(maps);

				}

				if (loanTypeId != null && loanTypeId.equals(Constants.EDUCATION_LOAN_ID)) {



					maps = null;
					maps = new LinkedHashMap<Integer, String>();
					for (MasterDocumentType document : documents) {
						if (document.getDocumentTypeAcademicProof() == 1) {
							maps.put(document.getDocumentTypeId(), document.getDocumentTypeName());
						}
					}
					documentTypeList.setAcademicProof(maps);
				}
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2121 : generateUIBeanDocumentTypeList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1797 : generateUIBeanDocumentTypeList() ", e);
		}
		return documentTypeList;
	}

	public JSONArray getEmploymentTypeByLoanType(Integer loanTypeId, String employmentDeleted) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<MasterEmploymentType> employmentTypes = null;
			if (employmentDeleted.equalsIgnoreCase("Y")) {
				employmentTypes = commonService.getAllEmploymentTypeByLoanType(loanTypeId);
			} else {
				employmentTypes = commonService.getAllEmploymentTypeByLoanTypeActive(loanTypeId);
			}
			if (employmentTypes != null) {
				for (MasterEmploymentType employmentType : employmentTypes) {
					if (employmentType != null) {
						JSONObject json = new JSONObject();
						json.put("key", employmentType.getEmploymentTypeId());
						json.put("value", employmentType.getEmploymentName());
						json.put("defaultDisplay", 0);
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2149 : getEmploymentTypeByLoanType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1824 : getEmploymentTypeByLoanType() ", e);
		}
		return jsonArray;
	}

	public String getSessionId(HttpServletRequest request, String sessionId) {
		try {
			if (request != null) {
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					if (sessionId == null) {
						for (Cookie locObj : cookies) {
							if ("PHPSESSID".equals(locObj.getName())) {
								sessionId = locObj.getValue();
							}
						}
					}
				}
			}
			
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2172 : getSessionId() ", ne);
			return null;
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1845 : getSessionId() ", e);
			return null;
		}
		return sessionId;
	}

	public BankLmsUser checkLmsUserLoginCookie(String sessionId) {
		BankLmsUser bankLmsUser = null;
		try {
			if (!ValidatorUtil.isValid(sessionId)) {
				return null;
			}
			bankLmsUser = commonService.getUserByPHPSessionId(sessionId);
			
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2189 : checkLmsUserLoginCookie() ", ne);
			return null;
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1858 : checkLmsUserLoginCookie() ", e);
			return null;
		}
		return bankLmsUser;
	}

	public boolean verifyCokkies(Cookie[] cookies, String sessionId) {
		for (Cookie locObj : cookies) {
			if ("PHPSESSID".equals(locObj.getName()) && sessionId.equals(locObj.getValue())) {
				return true;
			}
		}
		return true;
	}

	public static Map<String, String> generatePickupTimeList() {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("09:00", "09:00 am");
		map.put("09:30", "09:30 am");
		map.put("10:00", "10:00 am");
		map.put("10:30", "10:30 am");
		map.put("11:00", "11:00 am");
		map.put("11:30", "11:30 am");
		map.put("12:00", "12:00 pm");
		map.put("12:30", "12:30 pm");
		map.put("13:00", "01:00 pm");
		map.put("13:30", "01:30 pm");
		map.put("14:00", "02:00 pm");
		map.put("14:30", "02:30 pm");
		map.put("15:00", "03:00 pm");
		map.put("15:30", "03:30 pm");
		map.put("16:00", "04:00 pm");
		map.put("16:30", "04:30 pm");
		map.put("17:00", "05:00 pm");
		map.put("17:30", "05:30 pm");
		map.put("18:00", "06:00 pm");
		map.put("18:30", "06:30 pm");
		
		Map<String, String> pickupTime = new TreeMap<String, String>(map);
		return pickupTime;
	}

	public JSONArray populateTootTip(Integer loanTypeId, Integer requestIndex) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<MasterLabelTooltip> labelTooltips = commonService.getToolTipByLoanTypeAndReqestIndex(loanTypeId,
					requestIndex);
			for (MasterLabelTooltip labelTooltip : labelTooltips) {
				JSONObject jsonData = new JSONObject();
				JSONObject json = new JSONObject();
				jsonData.put("element_id", labelTooltip.getLabelTooltipElemId());
				jsonData.put("element_name", labelTooltip.getLabelTooltipInputName());
				jsonData.put("element_type", labelTooltip.getLabelTooltipInputType());
				jsonData.put("element_tooltip", labelTooltip.getLabelTooltipTooltip());
				json.put(labelTooltip.getLabelTooltipElemId(), jsonData);
				jsonArray.put(json);
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2249 : populateTootTip() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1913 : populateTootTip() ", e);
		}
		return jsonArray;
	}

	public String urlEncode(String msgBody) {
		msgBody = msgBody.replaceAll("%", "%25");
		msgBody = msgBody.replaceAll("@", "%40");
		msgBody = msgBody.replaceAll("&", "%26");
		msgBody = msgBody.replaceAll("'", "%27");
		msgBody = msgBody.replaceAll(" ", "%20");
		msgBody = msgBody.replaceAll(",", "%2C");
		msgBody = msgBody.replaceAll("/", "%2F");
		msgBody = msgBody.replaceAll("<", "%3C");
		msgBody = msgBody.replaceAll(">", "%3E");
		return msgBody;
	}

	public JSONObject getDBCredentialForHelper(JSONObject engineRequest) {
		try {
			if (Constants.DEPLOYMENT_MODE.equalsIgnoreCase("local")) {
				engineRequest.put("dIP", Constants.DIP_LOCAL);
				engineRequest.put("dPt", Constants.DPT_LOCAL);
			} else {
				engineRequest.put("dIP", Constants.RAC_SETTING_ENABLE ? Constants.RAC_DIP : Constants.DIP);
				engineRequest.put("dPt", Constants.RAC_SETTING_ENABLE ? Constants.RAC_DPT : Constants.DPT);
			}
			engineRequest.put("dN", Constants.RAC_SETTING_ENABLE ? Constants.RAC_DN : Constants.DN);
			engineRequest.put("dU", Constants.DU);
			engineRequest.put("dP", Constants.DP);
			engineRequest.put("app_bank_id", Constants.BANK_ID);
			if (Constants.RAC_SETTING_ENABLE) {
				engineRequest.put("dTy", Constants.RAC_DTY);
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2286 : getDBCredentialForHelper() ", ne);
		} catch (JSONException e) {
			logger.info("SbiUtil.java LNo : 1935 : getDBCredentialForHelper() ", e);
		}
		return engineRequest;
	}

	public String getTimeInterval(String pickupTime) {
		String timeInterval = null;
		List<String> keys = new ArrayList<String>(generatePickupTimeList().keySet());
		List<String> values = new ArrayList<String>(generatePickupTimeList().values());
		for (int index = 0; index < keys.size(); index++) {
			if (pickupTime.equalsIgnoreCase(keys.get(index))) {
				if (index == 0) {
					timeInterval = "08:30 am " + " - " + values.get(index + 1);
				} else if (index == (keys.size() - 1)) {
					timeInterval = values.get(index - 1) + " - " + "07:00 p.m";
				} else {
					timeInterval = values.get(index - 1) + " - " + values.get(index + 1);
				}
			}
		}
		return timeInterval;
	}

	public JSONArray getCourseType(Integer isAbroad) {
		JSONArray jsonArray = new JSONArray();
		try {
			List<BigDecimal> courseTypesList = educationLoanService.getCourseTypeList(isAbroad);
			if (courseTypesList != null && !courseTypesList.isEmpty()) {
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				Object[] courseTypeList = courseTypesList.toArray();
				if (courseTypeList != null && courseTypeList.length > 0) {
					for (int index = 0; index < courseTypeList.length; index++) {
						Object[] courseTypeObj = (Object[]) courseTypeList[index];
						maps.put(Integer.parseInt(courseTypeObj[0].toString()), courseTypeObj[1].toString());
						JSONObject json = new JSONObject();
						json.put("key", courseTypeObj[0].toString());
						json.put("value", courseTypeObj[1].toString());
						json.put("displayOrder", courseTypeObj[2].toString());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2331 : getCourseType() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1978 : getCourseType() ", e);
		}
		return jsonArray;
	}

	public Map<Integer, String> getScholarInistituteList() {
		try {
			List<MasterInstitute> instituteNameList = educationLoanService.getInstituteNameList();
			if (instituteNameList != null && !instituteNameList.isEmpty()) {
				Map<Integer, String> maps = new LinkedHashMap<Integer, String>();
				for (MasterInstitute institute : instituteNameList) {
					if (institute != null) {
						maps.put(institute.getInstituteId(), institute.getInstituteName());
					}
				}
				return maps;
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2351 : getScholarInistituteList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 2259 : getScholarInistituteList() ", e);
		}
		return null;
	}

	public JSONArray getScholarInistituteNameList() {
		try {
			List<MasterInstitute> instituteNameList = educationLoanService.getInstituteNameList();
			if (instituteNameList != null && !instituteNameList.isEmpty()) {
				JSONArray jsonArray = new JSONArray();
				for (MasterInstitute institute : instituteNameList) {
					if (institute != null) {
						JSONObject json = new JSONObject();
						json.put("key", institute.getInstituteId());
						json.put("value", institute.getInstituteName().replaceAll("&", "And"));
						json.put("displayOrder", 1);
						jsonArray.put(json);
					}
				}
				return jsonArray;
			}
		} catch (NullPointerException e) {
			logger.info("SbiUtil.java LNo : 2357 : getScholarInistituteNameList() ", e);
		} catch (Exception e1) {
			logger.info("SbiUtil.java LNo : 2279 : getScholarInistituteNameList() ", e1);
		}
		return null;
	}

	public String getLogDescription(Integer callStatusId) {
		if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE161_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE161;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE152_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE152;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE153_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE153;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE59_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE59;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE43_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE43;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE1_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE1;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE73_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE73;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE19_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE19;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE147_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE147;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE29_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE29;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE6_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE6;

		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE100_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE100;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE101_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE101;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE104_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE104;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE2_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE2;

		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE124_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE124;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE141_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE141;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE105_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE105;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE22_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE22;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE160_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE160;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE164_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE164;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE165_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE165;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE166_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE166;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE169_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE169;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE167_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE167;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE168_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE168;
		} else if (callStatusId == Constants.CALL_LOGS_MESSAGE_STATE170_ID) {
			return Constants.CALL_LOGS_MESSAGE_STATE170;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE171_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE171;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE172_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE172;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE173_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE173;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE174_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE174;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE175_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE175;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE176_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE176;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE177_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE177;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE178_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE178;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE347_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE347;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE32_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE32;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE87_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE87;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE94_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE94;
		} else if (callStatusId.equals(Constants.CALL_LOGS_MESSAGE_STATE122_ID)) {
			return Constants.CALL_LOGS_MESSAGE_STATE122;
		}
		return null;
	}


	public List<RepaymentSchedule> generateRepaymentList(double eligibiligy, int tenure, double interestRate,
			double emi1, int rateType, int emiType, int moratoriumMonths, int moratoriumIRType,
			double loanEligibiltyMultiplierFactor, String hasPropertyRentedYes, Integer loanQuoteMonthStartDateOflease,
			Integer loanQuoteYearStartDateOfLease, double emi2, int month1, int month2) {
		double beginningLoanBalance = 0;
		double principal = 0;
		double perMonthInterest = 0;
		double remainingLoanAmount = 0;
		double cumulativeInterest = 0;

		DecimalFormat df = new DecimalFormat("00.000");
		String interestRateString = df.format(interestRate);

		interestRate = new Double(interestRateString);

		double IRP = (interestRate / 100) / 12;


		List<RepaymentSchedule> repaymentSchedule = new ArrayList<RepaymentSchedule>(tenure);
		List<String> monthTranslator = new ArrayList<String>();
		monthTranslator.add("Jan");
		monthTranslator.add("Feb");
		monthTranslator.add("Mar");
		monthTranslator.add("Apr");
		monthTranslator.add("May");
		monthTranslator.add("Jun");
		monthTranslator.add("Jul");
		monthTranslator.add("Aug");
		monthTranslator.add("Sep");
		monthTranslator.add("Oct");
		monthTranslator.add("Nov");
		monthTranslator.add("Dec");

		int currentMonth = DateUtil.getCurrentMonthInNumeric();

		int currentYear = 0;
		try {
			currentYear = Integer.parseInt(DateUtil.getCurrentYear());
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2510 : generateRepaymentList() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1487 : generateRepaymentList() ", e);
		}
		if (hasPropertyRentedYes != null && "N".equals(hasPropertyRentedYes)) {
			currentMonth = loanQuoteMonthStartDateOflease;
			currentYear = loanQuoteYearStartDateOfLease;
		}
		String monthPart = "";
		String yearPart = "";
		for (int idx = 1; idx <= month1; idx++) {
			if (idx == 1) {
				if (rateType == 1) {

					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				} else {
					beginningLoanBalance = eligibiligy * Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR;
				}
			} else {
				beginningLoanBalance = remainingLoanAmount;
			}
			if (emiType == 0 && idx == 1) {

				perMonthInterest = 0;
			} else {
				perMonthInterest = beginningLoanBalance * IRP;
			}

			if (idx > moratoriumMonths) {
				principal = emi1 - perMonthInterest;
				remainingLoanAmount = beginningLoanBalance - principal;
				cumulativeInterest = cumulativeInterest + perMonthInterest;

			} else {
				if (moratoriumIRType == 0) {

					remainingLoanAmount = beginningLoanBalance + perMonthInterest;
				} else if (moratoriumIRType == 1) {

					remainingLoanAmount = beginningLoanBalance;
				} else if (moratoriumIRType == 2) {

					if (idx == moratoriumMonths) {
						double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
						remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
					} else {
						remainingLoanAmount = beginningLoanBalance;
					}
				}
			}

			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);


			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setPrincipal(principal);
			schecule.setBegninngLoanAmount(beginningLoanBalance);
			if (idx > moratoriumMonths) {
				schecule.setEMI(emi1);
			} else {
				schecule.setEMI(0);
			}

			schecule.setInterest(perMonthInterest);
			schecule.setCumulativeInterest(cumulativeInterest);
			schecule.setRemainingLoanAmount(remainingLoanAmount);
			
			repaymentSchedule.add(schecule);

		}
		for (int idx = month1 + 1; idx <= month2; idx++) {
			
			beginningLoanBalance = remainingLoanAmount;
			
			perMonthInterest = beginningLoanBalance * IRP;

			if (idx > moratoriumMonths) {
				principal = emi2 - perMonthInterest;
				remainingLoanAmount = beginningLoanBalance - principal;
				cumulativeInterest = cumulativeInterest + perMonthInterest;

			} else {
				if (moratoriumIRType == 0) {

					remainingLoanAmount = beginningLoanBalance + perMonthInterest;
				} else if (moratoriumIRType == 1) {

					remainingLoanAmount = beginningLoanBalance;
				} else if (moratoriumIRType == 2) {

					if (idx == moratoriumMonths) {
						double totalInterestEndOfMoratoriumPeriod = beginningLoanBalance * IRP * moratoriumMonths;
						remainingLoanAmount = beginningLoanBalance + totalInterestEndOfMoratoriumPeriod;
					} else {
						remainingLoanAmount = beginningLoanBalance;
					}
				}
			}

			monthPart = monthTranslator.get(currentMonth % 12);
			currentMonth++;
			if (currentMonth % 12 == 1) {
				currentYear++;
			}
			yearPart = String.valueOf(currentYear);


			RepaymentSchedule schecule = new RepaymentSchedule();
			schecule.setMonth(monthPart + "-" + yearPart);
			schecule.setPrincipal(principal);
			schecule.setBegninngLoanAmount(beginningLoanBalance);
			if (idx > moratoriumMonths) {
				schecule.setEMI(emi2);
			} else {
				schecule.setEMI(0);
			}

			schecule.setInterest(perMonthInterest);
			schecule.setCumulativeInterest(cumulativeInterest);
			schecule.setRemainingLoanAmount(remainingLoanAmount);
			
			repaymentSchedule.add(schecule);

		}
		return correctingRepaymentScheculeList(repaymentSchedule);

	}

	public boolean isPincodeValid(Integer pinCode, Integer stateId) {
		if (pinCode != null && stateId != null) {
			int pincodeInitial = (pinCode.intValue() / 10000);
			String pinlastfix = pinCode.toString().substring(3, 6);
			try {
				MasterState state = commonService.getStateById(stateId);
				if (state != null && state.getStatePinMinStartPrefix() != null
						&& state.getStatePinMaxStartPrefix() != null) {
					if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue()
							&& pincodeInitial <= state.getStatePinMaxStartPrefix().intValue()
							&& !pinlastfix.equals("000")) {
						return true;
					} else {
						return false;
					}
				}
			} catch (NullPointerException ne) {
				logger.info("SbiUtil.java LNo : 2663 : isPincodeValid() ", ne);
			} catch (Exception e) {
				logger.info("SbiUtil.java LNo : 2781 : isPincodeValid() ", e);
			}
		}
		return false;
	}

	public long getFinalLoanAmount(Double amount) {
		BigDecimal b = new BigDecimal(amount.doubleValue());
		long finalVal = Math.round(b.multiply(new BigDecimal(100000)).doubleValue());
		return finalVal;
	}
	
	public JSONArray getOccupationByEmployementTypeID(int employementId) {
		JSONArray jsonArray = new JSONArray();
		List<MasterEmployeeOccupationType> models;
		try {
			models = commonService.getAllOccupationTypesByEmploymentType(employementId);
			if (models != null) {
				for (MasterEmployeeOccupationType occupation : models) {
					if (occupation != null) {
						JSONObject json = new JSONObject();
						json.put("Id", occupation.getOccupationId());
						json.put("Name", occupation.getOccupation());
						json.put("category", occupation.getCategory());
						jsonArray.put(json);
					}
				}
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2726 : getCarModelByCompanyId() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 346 : getCarModelByCompanyId() ", e);
		}
		return jsonArray;
	}

	public String checkCaptcha(String captcha) {
		try {
			
			//new code for captcha validation
			String response = "";
			String encryptedResponse = "";
			
			if (!ValidatorUtil.isValid(SessionUtil.getCaptch())) {
				logger.info("SbiUtil.java @ 2863" + SessionUtil.getCaptch());
		        logger.info("SbiUtil.java @ 28633" + captcha);
		        logger.info("SbiUtil.java @ 2863| session captcha is not valid");
		        
		        response = "error|" + Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT;
		        encryptedResponse = getEncryptedResponse(response);
		        
				return encryptedResponse;
		        //return captcha.endsWith("encodedText") ? encryptedResponse : response;
			}
			if (!ValidatorUtil.isValid(captcha)) {
				logger.info("SbiUtil.java @ 2854 |Captcha in request is null or blank|2");
				
				response = "error|Invalid Captcha key|captcha|2";
				encryptedResponse = getEncryptedResponse(response);
				
				return encryptedResponse;
				//return captcha.endsWith("encodedText") ? encryptedResponse : response;
			}
			if (!SessionUtil.getCaptch().equals(captcha)) {
				logger.info("Captcha Session and Request : " + SessionUtil.getCaptch() + " :  " + captcha);
				logger.info("SbiUtil.java | Captch not matched @2859 | Session captcha not equal to request captcha");
				
				response = "error|Invalid Captcha key|captcha|2";
				encryptedResponse = getEncryptedResponse(response);
				
				return encryptedResponse;
				//return captcha.endsWith("encodedText") ? encryptedResponse : response;
			}

		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2752 : checkCaptcha() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LN 2880 | Exception in captcha handling ::", e);
			return "error|" + Constants.SORRY_FOR_INCONVENIENCE;
		}
		return null;
	}

	//new code for captcha validation
//	public String getEncryptedResponse(String response) throws UnsupportedEncodingException {
//
//		String encryptedResponse = null;
//		String plainResponse = null;
//		String key = null;
//		try {
//			key = EncryptDecryptUtil.decrypt(Constants.CRM_NEW_AES_KEY).substring(18, 50);
//			plainResponse = response + "|" + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
//			AesUtil util = new AesUtil(256, 100000);
//			encryptedResponse = util.encrypt(key, key, EncryptDecryptUtil.decrypt(String.valueOf(passphrase)), plainResponse);
//		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
//				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
//			logger.info("SbiUtil.java LNo : 2899 : getEncryptedResponse() ", e);
//		}
//
//		return encryptedResponse;
//	}
	
	public String getEncryptedResponse(String response) throws UnsupportedEncodingException {

	    String encryptedResponse = null;
	    String plainResponse = null;
	    String salt = null;
	    String iv = null;
	    String pass = null;
	    try {
	        salt = SessionUtil.getCryptoSalt();
	        iv = SessionUtil.getCryptoIv();
	        pass = SessionUtil.getCryptoPassPhrase();

	        // Fallback to existing fixed keys if session not yet initialized
	        if (salt == null || iv == null || pass == null) {
	            salt = EncryptDecryptUtil.decrypt(Constants.CRM_NEW_AES_KEY).substring(18, 50);
	            iv = salt;
	            pass = EncryptDecryptUtil.decrypt(String.valueOf(passphrase));
	        }

	        plainResponse = response + "|" + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	        AesUtil util = new AesUtil(256, 100000);
	        encryptedResponse = util.encrypt(salt, iv, pass, plainResponse);
	    } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
	            | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
	        logger.info("SbiUtil.java LNo : 2899 : getEncryptedResponse() ", e);
	    }

	    return encryptedResponse;
	}   
	
	public String getIdentityValidation(Boolean isAppPanCardLater, String appPanCardNo, Integer appAadhaarNumber,
			Integer appOtherId, String appOtherIdNumber, boolean isBankLmsContactCenterId) {
		String validationStatus = "pass";
		if (isBankLmsContactCenterId) {
			if (isAppPanCardLater != null && isAppPanCardLater && (appAadhaarNumber == null || appAadhaarNumber == 2)
					&& (appOtherId != null && appOtherId == 0)) {

			} else {
				if ((isAppPanCardLater == null || !isAppPanCardLater)
						&& (appPanCardNo == null || "".equals(appPanCardNo))
						&& (appAadhaarNumber == null || appAadhaarNumber == 2)
						&& (appOtherId != null && appOtherId == 0)) {
					validationStatus = "One of the Identity proof is mandatory.|1";
				} else {
					if (appPanCardNo != null && !"".equals(appPanCardNo)) {
						if (!ValidatorUtil.validateIdProof(appPanCardNo, 181)) {
							validationStatus = "PAN is not in correct format.|2";
						}
					}

					if (ValidatorUtil.isValid(appOtherId)) {
						boolean validateStatus = ValidatorUtil.validateIdProof(appOtherIdNumber, appOtherId);
						if (!validateStatus) {
							if (appOtherId == 166) {
								validationStatus = "Passport Number Not in Correct Format.|4";
							} else if (appOtherId == 165) {
								validationStatus = "Voter Id Not in Correct Format.|4";
							} else {
								validationStatus = "Other Id proof is not in correct format.|4";
							}
						}
					}
					
					if ((isAppPanCardLater == null || !isAppPanCardLater)
							&& (appPanCardNo == null || "".equals(appPanCardNo))) {
						validationStatus = "Enter PAN card.|2";
					}
				}
			}
		} else {
			if ((isAppPanCardLater == null || !isAppPanCardLater) && (appPanCardNo == null || "".equals(appPanCardNo))
					&& (appAadhaarNumber == null || appAadhaarNumber == 2) && (appOtherId != null && appOtherId == 0)) {
				validationStatus = "One of the Identity proof is mandatory.|1";
			} else {
				if (isAppPanCardLater != null && isAppPanCardLater
						&& (appAadhaarNumber == null || appAadhaarNumber == 2)
						&& (appOtherId != null && appOtherId == 0)) {
					validationStatus = "Please select other identity proof.|5";
				}

				if (isAppPanCardLater != null && isAppPanCardLater
						&& (appAadhaarNumber == null || appAadhaarNumber == 1)
						&& (appOtherId != null && appOtherId == 0)) {
					validationStatus = "Please enter either PAN No or select other identity proof.|11";
				}

				if (appPanCardNo != null && !"".equals(appPanCardNo)) {
					if (!ValidatorUtil.validateIdProof(appPanCardNo, 181)) {
						validationStatus = "PAN is not in correct format.|2";
					}
				}

				if (ValidatorUtil.isValid(appOtherId)) {
					boolean validateStatus = ValidatorUtil.validateIdProof(appOtherIdNumber, appOtherId);
					if (!validateStatus) {
						if (appOtherId == 166) {
							validationStatus = "Passport Number Not in Correct Format.|4";
						} else if (appOtherId == 165) {
							validationStatus = "Voter Id Not in Correct Format.|4";
						} else {
							validationStatus = "Other Id proof is not in correct format.|4";
						}
					}
				}

				if ((isAppPanCardLater == null || !isAppPanCardLater)
						&& (appPanCardNo == null || "".equals(appPanCardNo))) {
					validationStatus = "Enter PAN card.|2";
				}
			}
		}
		return validationStatus;
	}

	public boolean sendSMS(String isdCode, String mobile, String otpCode, boolean isResend, Integer smsType) {
		try {
			String msgBody = communicationManagerImpl.setEmailBody(0, 0, smsType, 0);
			msgBody = urlEncode(msgBody);
			String SMS_TEXT = null;
			if (Constants.COUNTRY_CODE_INDIA.equals(isdCode)) {
				SMS_TEXT=Constants.SMS_STRING_INDIAN;
			} else {
				SMS_TEXT=Constants.SMS_STRING_NRI;
			}
			SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT = SMS_TEXT.replaceAll("OTP_CODE", otpCode);
			SMS_TEXT = SMS_TEXT.replaceAll("MOBILE_CODE", (isdCode + mobile).trim());
			
			if (!Constants.DEPLOYMENT_MODE.equals("live")) {
				logger.info("OTP for Mobile Number: " + (isdCode + mobile).trim() + " is " + otpCode);
			}
			
			return communicationManagerImpl.sendSms(SMS_TEXT);
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 2881 : sentSMS() ", ne);
		} catch (Exception e) {
			logger.info("SBIUtil.java LNo : 2782 :: exception caught :: sentSMS() ", e);
		}
		return false;
	}


	public boolean sendEmail(String email, String otpCode, Integer emailType) {
		try {
			String msgBody = Constants.FIRST_EMAIL_PART
					+ StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(0, 0, emailType, 0))
					+ Constants.THIRD_EMAIL_PART;
			msgBody = msgBody.replaceAll("\\[BASE_URL\\]", Constants.BANK_IMAGE_FOLDER + "/");
			msgBody = msgBody.replaceAll("\\[BANK_URL\\]", Constants.BANK_URL);
			msgBody = msgBody.replaceAll("\\[BANK_NAME\\]", Constants.BANK_NAME);
			msgBody = msgBody.replaceAll("\\[BANK_FULL_NAME\\]", Constants.BANK_FULL_NAME);
			msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", otpCode);
			msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.AUTO_LOAN_PRODUCT_NAME);
			String[] emailId = { email };
			return communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
		} catch (NullPointerException e) {
			logger.info("SbiUtil.java LNo : 2902 : sentEmail() ", e);
		} catch (Exception e) {
			logger.info("SBIUtil.java LNo : 2782 :: exception caught :: sentEmail ", e);
		}
		return false;
	}

public JSONObject getAllBranchName(String query) {
		JSONObject json = new JSONObject();
		JSONArray suggestions = new JSONArray();
		List<MasterBranch> branches;
		try {
			if(query.matches("[a-zA-Z0-9_-]*$")) {
				branches = commonService.getAllBranchNameByQueryString(query);
				if (branches != null) {
					for (MasterBranch branch : branches) {
						if (branches != null) {
							suggestions.put(branch.getBranchName() + " [ " + branch.getBranchCode() + " ]");
						}
					}
				}
				json.put("query", query);
				json.put("suggestions", suggestions);
			} else {
				json.put("error", "Invalid Request");
			}
		} catch (NullPointerException ne) {
			logger.info("SbiUtil.java LNo : 1068 : getAllBranchName() ", ne);
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1070 : getAllBranchName() ", e);
		}
		return json;
	}
	
	public String getDecryptedRequest(String request) {
		// String decryptedRequest = null;
		String decryptedRequest1 = null;
		String plainRequest = null;
		String key = null;
		try {
			key = EncryptDecryptUtil.decrypt(Constants.CRM_NEW_AES_KEY).substring(18, 50);
			plainRequest = String.valueOf(request) + "|" + RandomStringUtils.randomAlphanumeric(6).toUpperCase();
			AesUtil util = new AesUtil(256, 100000);
			decryptedRequest1 = util.decrypt(key, key, EncryptDecryptUtil.decrypt(String.valueOf(passphrase)), plainRequest);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			logger.info("SbiUtil.java LNo : 1070 : getAllBranchName() ", e);
		} finally {
			Arrays.fill(passphrase, ' ');
		}
		return decryptedRequest1;
	}

	
	public String getOcasSessionId(HttpServletRequest request) {
		try {
			String sessionId="";
			
			if (request != null) {
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (Cookie locObj : cookies) {
						if ("OCASID".equals(locObj.getName()) || "JSESSIONID".equals(locObj.getName())) {
							sessionId = locObj.getValue();
							//OcasID = sessionId;
							setOcasID(sessionId);
						}
					}
					logger.info("SbiUtil.java LNo : 2158 : getSessionId() " + sessionId);
				}
			}
			return sessionId;
		} catch (NullPointerException e) {
			logger.info("SbiUtil.java LNo : 1259 : getSessionId() ", e);
			return null;
		} catch (Exception e) {
			logger.info("SbiUtil.java LNo : 1845 : getSessionId() ", e);
			return null;
		}
	}
}


/SBI/src/com/mintstreet/common/session/SessionUtil.java
	package com.mintstreet.common.session;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.validation.ValidatorUtil;
import com.opensymphony.xwork2.ActionContext;


public class SessionUtil {
	
	private static final Logger logger = LogManager.getLogger(SessionUtil.class.getName());
  
	public static final String SELECTED_LANGUAGE_SESSION_VAR = "selectedLanguage";
  
  public static final String CAPTCHA_SESSION_VAR = "captcha";
  
  public static final String EMAIL_SESSION_VAR = "email";
  
  public static final String MOBILE_SESSION_VAR = "mobile";
  
	public static final String ALTERNATE_MOBILE_NUMBER_SESSION_VAR = "alternateMobileNumber";
	public static final String ALTERNATE_CAPTCHA_SESSION_VAR= "altCaptcha";
	public static final String ALTERNATE_EMAIL_SESSION_VAR = "altEmail";
	
	public static final String SESSION_OTP_VERIFY = "N";
	
  public static final String SBI_TATA_FILE_NAME = "sbiTataPdfName";
  
  public static final String ISD_SESSION_VAR = "isdCode";
  
  public static final String APPLICANT_NAME_SESSION_VAR = "applicantName";
  
  public static final String LEAD_ID = "leadId";
  
  public static final String RQST_ID = "rqstId";
  
  public static final String HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdHomeLoan";
  
  public static final String HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdHomeLoanTopup";
  
  public static final String PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdPersonalLoan";
  
  public static final String PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdPensionLoan";
  
  public static final String AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdAutoLoan";
  
  public static final String EDUCATION_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdEducationLoan";
  
  public static final String AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdAutoLoan";
  
  public static final String HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdHomeTopUpLoan";
  
  public static final String HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdHomeLoan";
  
  public static final String EDUCATION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdEducationLoan";
  
  public static final String PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdPersonalLoan";
  public static final String PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdPensionLoan";
  
  public static final String EDUCATION_APP_TYPE_ID_SESSION_VAR = "appElIdTypeLoan";
  
  public static final String EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdScholarLoan";
  
  public static final String EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdGlobalEdvantageLoan";
  
  public static final String EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdEducationTakeoverLoan";
  
  public static final String EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR = "appSeqIdEducationNormal";
  
  public static final String EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdEducationBidyaLakshmiLoan";
  
  public static final String PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR = "appSeqIdPersonalGoldLoan";

  public static final String HOME_LOAN_APP_REFERENCE_ID_SESSION_VAR = "appRefIdHomeLoan";
  
  public static final String PERSONAL_APP_TYPE_ID_SESSION_VAR = "appPlIdTypeLoan";
  
  
  public static final String BANK_LMS_USER_ID_SESSION_VAR = "bankLMSUser";
  
  public static final String APPLICATION_TYPE = "applicationType";
	public static final String LMS_INTERMEDIARY_RELATION = "lmsIntermediaryRelation";
	public static final String LMS_INTERMEDIARY_CATEGORIES = "lmsIntermediaryCategories";
  
  
  
  public static final String VISIT_ID_HL = "visitIdHL";
  
  public static final String VISIT_ID_AL = "visitIdAL";
  
  public static final String VISIT_ID_PL = "visitIdPL";
  public static final String VISIT_ID_PNL = "visitIdPNL";
  
  public static final String VISIT_ID_EL = "visitIdEL";
  
  public static final String VISIT_ID_SCC = "visitIdSCC";
  
  public static final String VISIT_ID_HOME_PAGE = "visitIdHomePage";
  
  public static final String IS_EKYC_APPLICATIOMN = "isEKYCApplciation";
  
  public static final String CBS_EXISTING_RELATION_WITH_BANK = "existCbsRelationWithBank";
  
  public static final String API_TOKEN = "apiToken";
  
  public static final String APPLICATION_CRM_LEAD_SESSION_VAR = "appCRMLeadId";
  
  public static final String FLIPKART_LEAD_ID = "flipkartLeadId";
  
  public static final String SIGNZY_ACCESS_TOKEN = "signzy_access_token";
  
  public static final String SIGNZY_USER_ID = "signzy_user_id";
  
  public static final String ACCESS_TOKEN = "access_token";
  
  public static final String UI_TYPE = "uiType";
  
  public static final String LOAN_TYPE = "loantype";
  
  public static final String VISIT_ID_HOME_CAMPAIGN = "visitIdHLCampaign";
  
  public static final String VISIT_ID_AUTO_CAMPAIGN = "visitIdALCampaign";
  
  public static final String VISIT_ID_EDUCATION_CAMPAIGN = "visitIdELCampaign";
  
  public static final String SECRET_KEY = "secretKey";
  
  public static final String CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR = "appSeqIdCveLoan";
  
  public static final String CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR = "cbsCallIdCve";
  
  public static final String CVE_APP_TYPE_ID_SESSION_VAR = "appCveIdTypeLoan";
  
  public static final String HOME_LOAN_STATE = "homeLoanState";
  
  public static final String HOME_LOAN_ERROR = "homeLoanError";
  
  public static final String HOME_LOAN_STATE16_CHECK = "homeLoanState16Check";
  public static final String GOLD_LOAN_CHECKER = "gold_loan";
  
  public static final String RESEND_MOBILE_NUMBER = "resend_mobile_number";
  
  public static final String TRACKER_CHECK = "tracker";
  
  public static final String CRYPTO_SALT = "cryptoSalt";
  public static final String CRYPTO_IV = "cryptoIv";
  public static final String CRYPTO_PASSPHRASE = "cryptoPassPhrase";
  
	@SuppressWarnings("rawtypes")
  public static Map getSession() {
    return ActionContext.getContext().getSession();
  }
  
  public static ServletContext getServletContext() {
		return (ServletContext) ActionContext.getContext().get(	StrutsStatics.SERVLET_CONTEXT);
	}
	
	public static HttpSession getSessionAction() {
		ActionContext context = ActionContext.getContext();
		HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
		return request.getSession();
	}

	public static String getSelectedLanguage() {
		if (getSession().get(SELECTED_LANGUAGE_SESSION_VAR) != null) {
			return (String) getSession().get(SELECTED_LANGUAGE_SESSION_VAR);
		} else if (getSessionAction().getAttribute(SELECTED_LANGUAGE_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(SELECTED_LANGUAGE_SESSION_VAR);
		} else {
			return null;
		}
	}
	
  public static void setSelectedLanguage(String selectedLanguage) {
		getSessionAction().setAttribute(SELECTED_LANGUAGE_SESSION_VAR, selectedLanguage);
  }
  
 public static Integer getRqstId() {
	if (getSession().get(RQST_ID) != null) {
		return Integer.parseInt(getSession().get(RQST_ID).toString());
	} else if (getSessionAction().getAttribute(RQST_ID) != null) {
		return Integer.parseInt(getSessionAction().getAttribute(RQST_ID).toString());
	} else {
		return null;
	}
    }
  
  public static void setRqstId(Integer rqstId) {
		getSessionAction().setAttribute(RQST_ID, rqstId);
  }
  
  public static String getCaptch() {
		if (getSession().get(CAPTCHA_SESSION_VAR) != null) {
			return (String) getSession().get(CAPTCHA_SESSION_VAR);
		}else if (getSessionAction().getAttribute(CAPTCHA_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(CAPTCHA_SESSION_VAR);
		} else {
                     return null;
		}
  	    }	 
  public static void setCaptch(String captch) {
		getSessionAction().setAttribute(CAPTCHA_SESSION_VAR, captch);
  }
  
  public static void setLeadId(Integer leadId) {
		getSessionAction().setAttribute(LEAD_ID, leadId);
  }
  
  public static Integer getLeadId() {
		if (getSession().get(LEAD_ID) != null) {
			return Integer.parseInt(getSession().get(LEAD_ID).toString());
		} else if (getSessionAction().getAttribute(LEAD_ID) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(LEAD_ID).toString());
		} else {
			return null;
		}
	}
  
  public static void setApplicantName(String applicantName) {
		getSessionAction().setAttribute(APPLICANT_NAME_SESSION_VAR, applicantName);
  }
  
  public static String getApplicantName() {
		if (getSession().get(APPLICANT_NAME_SESSION_VAR) != null) {
			return (String) getSession().get(APPLICANT_NAME_SESSION_VAR);
		} else if (getSessionAction().getAttribute(APPLICANT_NAME_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(APPLICANT_NAME_SESSION_VAR);
		} else {
			return null;
		}
	}
  
 public static Integer getHomeLoanApplicationSequenceId() {
		if (getSession().get(HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setHomeLoanApplicationSequenceId(Integer appSeqIdHomeLoan) {
		getSessionAction().setAttribute(HOME_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdHomeLoan);
  }
  
  public static Integer getAutoLoanApplicationSequenceId() {
		if (getSession().get(AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if(getSessionAction().getAttribute(AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR)!=null){
			return Integer.parseInt(getSessionAction().getAttribute(AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		}else{
			return null;
		}
	}
  
  public static void setAutoLoanApplicationSequenceId(Integer appSeqIdAutoLoan) {
		getSessionAction().setAttribute(AUTO_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdAutoLoan);
  }
  
  public static Integer getEducationLoanApplicationSequenceId()  {
		Integer appElTypeId = SessionUtil.getEducationTypeId();
		if(appElTypeId == null){
			return null;
		}
		if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_EDVANTAGE)){
			return getEducationGlobalEdvantageSequenceId();
		} else if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_SCHOLAR)){
			return getEducationScholarLoanSequenceId();
		} else if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_TAKE_OVER)){
			return getEducationTakeOverLoanApplicationSequenceId();
		} else if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_NORMAL)){
			return getEducationApplicationNormalSequenceId();
		}else if(appElTypeId.equals(Constants.APP_EL_TYPE_ID_BIDYALAKHMI)){
			return getEducationBidyaLakshmiLoanSequenceId();
		}
		return null;
	}
  
  public static void setEducationLoanApplicationSequenceId(Integer appSeqIdEducationLoan) {
		Integer appElTypeId;
    try {
			appElTypeId = SessionUtil.getEducationTypeId();
			if(appElTypeId == null){
       				 return; 
			}
      if (appElTypeId.equals(Constants.APP_EL_TYPE_ID_EDVANTAGE)) {
        setEducationGlobalEdvantageSequenceId(appSeqIdEducationLoan);
      } else if (appElTypeId.equals(Constants.APP_EL_TYPE_ID_SCHOLAR)) {
        setEducationScholarLoanSequenceId(appSeqIdEducationLoan);
      } else if (appElTypeId.equals(Constants.APP_EL_TYPE_ID_TAKE_OVER)) {
        setEducationTakeOverLoanApplicationSequenceId(appSeqIdEducationLoan);
      } else if (appElTypeId.equals(Constants.APP_EL_TYPE_ID_NORMAL)) {
        setEducationApplicationNormalSequenceId(appSeqIdEducationLoan);
      } else if (appElTypeId.equals(Constants.APP_EL_TYPE_ID_BIDYALAKHMI)) {
        setEducationBidyaLakshmiLoanSequenceId(appSeqIdEducationLoan);
      } 
	} catch (NullPointerException e) {
		logger.info("SessionUtil.java LN 302 setEducationLoanApplicationSequenceId() ::", e);
    } catch (Exception e) {
    	logger.info("SessionUtil.java LN 302 setEducationLoanApplicationSequenceId() ::", e);
    }
  }
  
 	public static Integer getPersonalLoanApplicationSequenceId()  {
		if (getSession().get(PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setPersonalLoanApplicationSequenceId(Integer appSeqIdPersonalLoan) {
		getSessionAction().setAttribute(PERSONAL_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdPersonalLoan);
  }
  
  public static void setPensionLoanApplicationSequenceId(Integer appSeqIdPensionLoan) {
		getSessionAction().setAttribute(PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdPensionLoan);
  }
  
  public static Integer getPensionLoanApplicationSequenceId()  {
		if (getSession().get(PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PENSION_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  //Added for CVE
  public static void setCveLoanApplicationSequenceId(Integer appSeqIdCveLoan) { 
	    getSessionAction().setAttribute(CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdCveLoan);
	  }
	  
  	public static Integer getCveLoanApplicationSequenceId()  {
	    if (getSession().get(CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null)
	      return Integer.valueOf(Integer.parseInt(getSession().get(CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString())); 
	    if (getSessionAction().getAttribute(CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null)
	      return Integer.valueOf(Integer.parseInt(getSessionAction().getAttribute(CVE_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString())); 
	    return null;
	  }
	//Added for CVE  
	  
  public static String getEmail()  {
		if (getSession().get(EMAIL_SESSION_VAR) != null) {
			return (String) getSession().get(EMAIL_SESSION_VAR);
		} else if (getSessionAction().getAttribute(EMAIL_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(EMAIL_SESSION_VAR);
		} else {
			return null;
		}
	}
  
  public static void setEmail(String email) {
		getSessionAction().setAttribute(EMAIL_SESSION_VAR, email);
  }
  
  public static void setMobile(String mobile) {
		getSessionAction().setAttribute(MOBILE_SESSION_VAR, mobile);
  }
  
  public static String getMobile()  {
		if (getSession().get(MOBILE_SESSION_VAR) != null) {
			return (String) getSession().get(MOBILE_SESSION_VAR);
		} else if (getSessionAction().getAttribute(MOBILE_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(MOBILE_SESSION_VAR);
		} else {
			return null;
		}
	}
	
	//Added by Ruchita for alternate mobile number
	
	public static void setAltCaptcha(String altCaptcha) {
		getSessionAction().setAttribute(ALTERNATE_CAPTCHA_SESSION_VAR, altCaptcha);
	}
	
	public static String getAltCaptcha()  {
		if (getSession().get(ALTERNATE_CAPTCHA_SESSION_VAR) == null) {
			return (String) getSession().get(ALTERNATE_CAPTCHA_SESSION_VAR);
		} else if (getSessionAction().getAttribute(ALTERNATE_CAPTCHA_SESSION_VAR) == null) {
			return (String) getSessionAction().getAttribute(ALTERNATE_CAPTCHA_SESSION_VAR);
		} else {
			return null;
		}
	}
	
	public static void setalternateMobileNumber(String alternateMobileNumber) {
		getSessionAction().setAttribute(ALTERNATE_MOBILE_NUMBER_SESSION_VAR, alternateMobileNumber);
	}
	
	public static String getalternateMobileNumber()  {
		if (getSession().get(ALTERNATE_MOBILE_NUMBER_SESSION_VAR) == null) {
			return (String) getSession().get(ALTERNATE_MOBILE_NUMBER_SESSION_VAR);
		} else if (getSessionAction().getAttribute(ALTERNATE_MOBILE_NUMBER_SESSION_VAR) == null) {
			return (String) getSessionAction().getAttribute(ALTERNATE_MOBILE_NUMBER_SESSION_VAR);
		} else {
			return null;
		}
	}
	
	//Ruchita For session 
	public static void setSessionOtpVerify(String N) {
		getSessionAction().setAttribute(SESSION_OTP_VERIFY, "N");
	}
	public static String getSessionOtpVerify()  {
		if (getSession().get(SESSION_OTP_VERIFY) == null) {
			return (String) getSession().get(SESSION_OTP_VERIFY);
		} else if (getSessionAction().getAttribute(SESSION_OTP_VERIFY) == null) {
			return (String) getSessionAction().getAttribute(SESSION_OTP_VERIFY);
		} else {
			return null;
		}
	}
	//END
	
	/*public static void setaltEmail(String altEmail) {
		getSessionAction().setAttribute(ALTERNATE_EMAIL_SESSION_VAR, altEmail);
	}
	
	public static String getaltEmail()  {
		if (getSession().get(ALTERNATE_EMAIL_SESSION_VAR) == null) {
			return (String) getSession().get(ALTERNATE_EMAIL_SESSION_VAR);
		} else if (getSessionAction().getAttribute(ALTERNATE_EMAIL_SESSION_VAR) == null) {
			return (String) getSessionAction().getAttribute(ALTERNATE_EMAIL_SESSION_VAR);
		} else {
			return null;
		}
	}*/
	//end
	
	public static void setISDCode(String isdCode) {
		getSessionAction().setAttribute(ISD_SESSION_VAR, isdCode);
  }
  
  public static String getISDCode() {
		if (getSession().get(ISD_SESSION_VAR) != null) {
			return (String) getSession().get(ISD_SESSION_VAR);
		} else if (getSessionAction().getAttribute(ISD_SESSION_VAR) != null) {
			return (String) getSessionAction().getAttribute(ISD_SESSION_VAR);
		} else {
			return null;
		}
	}
  
  public static BankLmsUser getBankLMSUser()  {
		if (getSession().get(BANK_LMS_USER_ID_SESSION_VAR) != null) {
			return (BankLmsUser)getSession().get(BANK_LMS_USER_ID_SESSION_VAR);
		} else if (getSessionAction().getAttribute(BANK_LMS_USER_ID_SESSION_VAR) != null) {
			return (BankLmsUser)getSessionAction().getAttribute(BANK_LMS_USER_ID_SESSION_VAR);
		} else {
			return null;
		}
	}
  
  public static void setBankLMSUser(BankLmsUser bankLmsUser) {
		getSessionAction().setAttribute(BANK_LMS_USER_ID_SESSION_VAR, bankLmsUser);
  }
  
  public static void setApplicationType(Integer applicationType) {
		getSessionAction().setAttribute(APPLICATION_TYPE, applicationType);
  }
  
  public static Integer getApplicationType()  {
		if (getSession().get(APPLICATION_TYPE) != null) {
			return Integer.parseInt(getSession().get(APPLICATION_TYPE).toString());
		} else if (getSessionAction().getAttribute(APPLICATION_TYPE) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(APPLICATION_TYPE).toString());
		} else {
			return null;
		}
	}
  
  public static void setLMSIntermediaryRelation(String intermediaryRel) {
		getSessionAction().setAttribute(LMS_INTERMEDIARY_RELATION, intermediaryRel);
  }
  
  public static String getLMSIntermediaryRelation()  {
		if (getSession().get(LMS_INTERMEDIARY_RELATION) != null) {
			return (String)getSession().get(LMS_INTERMEDIARY_RELATION);
		} else if (getSessionAction().getAttribute(LMS_INTERMEDIARY_RELATION) != null) {
			return (String)getSessionAction().getAttribute(LMS_INTERMEDIARY_RELATION);
		} else {
			return null;
		}
	}
  
  public static void setLMSIntermediaryCatagories(String intermediaryRel) {
		getSessionAction().setAttribute(LMS_INTERMEDIARY_CATEGORIES, intermediaryRel);
  }
  
  public static String getLMSIntermediaryCatagories()  {
		if (getSession().get(LMS_INTERMEDIARY_CATEGORIES) != null) {
			return (String)getSession().get(LMS_INTERMEDIARY_CATEGORIES);
		} else if (getSessionAction().getAttribute(LMS_INTERMEDIARY_CATEGORIES) != null) {
			return (String)getSessionAction().getAttribute(LMS_INTERMEDIARY_CATEGORIES);
		} else {
			return null;
		}
	}
  
  public static void setVisitIdHL(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_HL, visitId);
  }
  
  public static Integer getVisitIdHL()  {
		if (getSession().get(VISIT_ID_HL) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_HL).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_HL) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_HL).toString());
		} else {
			return null;
		}
	}
  
  public static void setVisitIdAL(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_AL, visitId);
  }
  
  public static Integer getVisitIdAL()  {
		if (getSession().get(VISIT_ID_AL) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_AL).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_AL) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_AL).toString());
		} else {
			return null;
		}
	}
  
  public static void setVisitIdPL(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_PL, visitId);
  }
  
  public static void setVisitIdPnL(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_PNL, visitId);
  }
  
  public static Integer getVisitIdPnl() {
		if (getSession().get(VISIT_ID_PNL) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_PNL).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_PNL) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_PNL).toString());
		} else {
			return null;
		}
	}
  
  public static Integer getVisitIdPL()  {
		if (getSession().get(VISIT_ID_PL) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_PL).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_PL) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_PL).toString());
		} else {
			return null;
		}
	}
  
  public static void setVisitIdEL(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_EL, visitId);
  }
  
  public static Integer getVisitIdEL()  {
		if (getSession().get(VISIT_ID_EL) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_EL).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_EL) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_EL).toString());
		} else {
			return null;
		}
	}
  
  public static void setVisitIdHomePage(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_HOME_PAGE, visitId);
  }
  
  public static Integer getVisitIdHomePage()  {
		if (getSession().get(VISIT_ID_HOME_PAGE) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_HOME_PAGE).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_HOME_PAGE) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_HOME_PAGE).toString());
		} else {
			return null;
		}
	}
  
  public static Integer getHomeLoanTopupApplicationSequenceId()  {
		if (getSession().get(HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setHomeLoanTopupApplicationSequenceId(Integer appSeqIdHomeLoanTopup) {
		getSessionAction().setAttribute(HOME_TOP_UP_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdHomeLoanTopup);
  }
  
  public static Integer getAutoLoanCbsCallId()  {
		if (getSession().get(AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static Integer getEducationGlobalEdvantageSequenceId()  {
		if (getSession().get( EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get( EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute( EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute( EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setEducationGlobalEdvantageSequenceId(Integer appSeqIdGlobalEdvantageLoan) {
		getSessionAction().setAttribute( EDUCATION_EDVANTAGE_APP_SECQUENCE_ID_SESSION_VAR , appSeqIdGlobalEdvantageLoan);
  }
  
  public static Integer getEducationScholarLoanSequenceId()  {
		if (getSession().get(EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setEducationScholarLoanSequenceId(Integer appSeqIdScholarLoan) {
		getSessionAction().setAttribute(EDUCATION_SCHOLAR_APP_SECQUENCE_ID_SESSION_VAR,appSeqIdScholarLoan );
  }
  
  public static void setAutoLoanCbsCallId(Integer appSeqIdAutoLoanTopup) {
		getSessionAction().setAttribute(AUTO_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdAutoLoanTopup);
  }
  
  public static Integer getHomeTopUpLoanCbsCallId()  {
		if (getSession().get(HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setHomeTopUpLoanCbsCallId(Integer appSeqIdHomeLoanTopup) {
		getSessionAction().setAttribute(HOME_TOP_UP_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdHomeLoanTopup);
  }
  
 public static Integer getHomeLoanCbsCallId()  {
		if (getSession().get(HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setHomeLoanCbsCallId(Integer appSeqIdHomeLoan) {
		getSessionAction().setAttribute(HOME_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdHomeLoan);
  }
  
  public static Integer getEducationLoanCbsCallId()  {
		if (getSession().get(EDUCATION_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setEducationLoanCbsCallId(Integer appSeqIdEducationLoan) {
		getSessionAction().setAttribute(EDUCATION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdEducationLoan);
  }
  
  public static boolean isEkycApplication()  {
		if (getSession().get(IS_EKYC_APPLICATIOMN) != null) {
			return Boolean.parseBoolean(getSession().get(IS_EKYC_APPLICATIOMN).toString());
		} else if (getSessionAction().getAttribute(IS_EKYC_APPLICATIOMN) != null) {
			return Boolean.parseBoolean(getSessionAction().getAttribute(IS_EKYC_APPLICATIOMN).toString());
		} else {
			return false;
		}
	}
  
  public static void setEkycApplication(boolean isEKYCApplication) {
		getSessionAction().setAttribute(IS_EKYC_APPLICATIOMN, isEKYCApplication);
  }
  
  public static Integer getPersonalLoanCbsCallId()  {
		if (getSession().get(PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setPersonalLoanCbsCallId(Integer appSeqIdPersonalLoan) {
    getSessionAction().setAttribute(PERSONAL_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdPersonalLoan);
  }
  
  
  
  //Added by Pratima for CVE	
  
  public static Integer getCveCbsCallId()  {
		if (getSession().get(CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
	  
	  public static void setCveCbsCallId(Integer appSeqIdCveLoan) {
	    getSessionAction().setAttribute(CVE_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdCveLoan);
	  }
	//Ended by Pratima for CVE  
	  
  
  
  public static Integer getPensionLoanCbsCallId()  {
		if (getSession().get(PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
	
	public static void setPensionLoanCbsCallId(Integer appSeqIdPensionLoan) {
		getSessionAction().setAttribute(PENSION_LOAN_CBS_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdPensionLoan);
	}
	public static Integer getPersonalGoldLoanSequenceId()  {
		if (getSession().get(PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
	
	public static void setPersonalGoldLoanSequenceId(Integer appSeqIdPersonalGoldLoan ) {
		getSessionAction().setAttribute(PERSONAL_GOLD_LOAN_APP_SEQUENCE_ID_SEQUENCE_ID_SESSION_VAR,appSeqIdPersonalGoldLoan );
	}
	public static Integer getEducationTypeId()  {
		if (getSession().get(EDUCATION_APP_TYPE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_APP_TYPE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_APP_TYPE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_APP_TYPE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
	
	public static void setEducationTypeId(Integer appElIdTypeLoan ) {
		getSessionAction().setAttribute(EDUCATION_APP_TYPE_ID_SESSION_VAR, appElIdTypeLoan );	
	}
  
  public static void setCbsExistingRelationshipWithBank(String existCbsRelationWithBank) {
		getSessionAction().setAttribute(CBS_EXISTING_RELATION_WITH_BANK, existCbsRelationWithBank);
  }
  
  public static String getCbsExistingRelationshipWithBank()  {
		if (getSession().get(CBS_EXISTING_RELATION_WITH_BANK) != null) {
			return (String) getSession().get(CBS_EXISTING_RELATION_WITH_BANK);
		} else if (getSessionAction().getAttribute(CBS_EXISTING_RELATION_WITH_BANK) != null) {
			return (String) getSessionAction().getAttribute(CBS_EXISTING_RELATION_WITH_BANK);

		}
		return null;
	}
  
  public static void setSbiTataPdfName(String sbiTataPdfName) {
		getSessionAction().setAttribute(SBI_TATA_FILE_NAME, sbiTataPdfName);
  }
  
  public static String getSbiTataPdfName()  {
		if (getSession().get(SBI_TATA_FILE_NAME) != null) {
			return (String) getSession().get(SBI_TATA_FILE_NAME);
		} else if (getSessionAction().getAttribute(SBI_TATA_FILE_NAME) != null) {
			return (String) getSessionAction().getAttribute(SBI_TATA_FILE_NAME);

		} else {
			return null;
		}
	}
  
	public static void setVisitIDSCC(Integer visitId) {
		 getSessionAction().setAttribute(VISIT_ID_SCC, visitId);
	}
	public static Integer getVisitSCC() {
	if (getSession().get(VISIT_ID_SCC) != null) {
		return Integer.parseInt(getSession().get(VISIT_ID_SCC).toString());
	} else if (getSessionAction().getAttribute(VISIT_ID_SCC) != null) {
		return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_SCC).toString());
	} else {
	return null;
	}
	}
  
	public static Integer getApplicationCRMLeadId()  {
		if (getSession().get(APPLICATION_CRM_LEAD_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(APPLICATION_CRM_LEAD_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(APPLICATION_CRM_LEAD_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(APPLICATION_CRM_LEAD_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setApplicationCRMLeadId(Integer appCRMLeadId) {
		getSessionAction().setAttribute(APPLICATION_CRM_LEAD_SESSION_VAR, appCRMLeadId);
  }
  
  public static Integer getFlipkartLeadId() {
		if (getSession().get(FLIPKART_LEAD_ID) != null) {
			return Integer.parseInt(getSession().get(FLIPKART_LEAD_ID).toString());
		} else if (getSessionAction().getAttribute(FLIPKART_LEAD_ID) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(FLIPKART_LEAD_ID).toString());
		} else {
			return null;
		}
	}
  
  public static void setFlipkartLeadId(Integer flipkartLeadId) {
		getSessionAction().setAttribute(FLIPKART_LEAD_ID, flipkartLeadId);
  }
  
  public static Integer getEducationApplicationNormalSequenceId()  {
		if (getSession().get(EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setEducationApplicationNormalSequenceId(Integer appSeqIdEducationTakeOverLoan) {
		getSessionAction().setAttribute(EDUCATION_APP_SECQUENCE_ID_NORMAL_SESSION_VAR, appSeqIdEducationTakeOverLoan);
  }
  
  public static void setEducationTakeOverLoanApplicationSequenceId(Integer appSeqIdEducationTakeOverLoan) {
		getSessionAction().setAttribute(EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR, appSeqIdEducationTakeOverLoan);
  }
  
  public static Integer getEducationTakeOverLoanApplicationSequenceId()  {
		if (getSession().get(EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_TAKE_OVER_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
 
  
  public static Integer getPersonalTypeId()  {
		if (getSession().get(PERSONAL_APP_TYPE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(PERSONAL_APP_TYPE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(PERSONAL_APP_TYPE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(PERSONAL_APP_TYPE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
  
  public static void setPersonalTypeId(Integer appPlIdTypeLoan) {
		getSessionAction().setAttribute(PERSONAL_APP_TYPE_ID_SESSION_VAR, appPlIdTypeLoan );	
  }
  
  
  
  public static Integer getCveTypeId()  {
	    if (getSession().get(CVE_APP_TYPE_ID_SESSION_VAR) != null)
	      return Integer.valueOf(Integer.parseInt(getSession().get(CVE_APP_TYPE_ID_SESSION_VAR).toString())); 
	    if (getSessionAction().getAttribute(CVE_APP_TYPE_ID_SESSION_VAR) != null)
	      return Integer.valueOf(Integer.parseInt(getSessionAction().getAttribute(CVE_APP_TYPE_ID_SESSION_VAR).toString())); 
	    return null;
	  }
	  
  public static void setCveTypeId(Integer appCveIdTypeLoan) {
	    //getSessionAction().setAttribute("appCveIdTypeLoan", appCveIdTypeLoan);
	    getSessionAction().setAttribute(CVE_APP_TYPE_ID_SESSION_VAR, appCveIdTypeLoan);
	  }
	  
  
  
  
	  public static Integer getPersonalLoanTypeSequenceId()  {
	         Integer appPlTypeId = SessionUtil.getPersonalTypeId();
	         if(appPlTypeId == null) {
	             return null; 
		 }
			if(appPlTypeId.equals(Constants.APP_PL_TYPE_PERSONAL)){
	      return getPersonalLoanApplicationSequenceId(); 
			}else if(appPlTypeId.equals(Constants.APP_PL_TYPE_GOLD)){
	      return getPersonalGoldLoanSequenceId(); 
			}
			else if(appPlTypeId.equals(Constants.APP_PL_TYPE_PENSION)){
	      return getPensionLoanApplicationSequenceId(); 
			} else if (appPlTypeId.equals(Constants.APP_PL_TYPE_CVE)) {
			      return getCveLoanApplicationSequenceId(); 
			}
	       return null;
	  }
  
	  public static void setPersonalLoanTypeSequenceId(Integer appSeqIdPLoan) {
			Integer appPlTypeId;
	             try {
	            appPlTypeId = SessionUtil.getPersonalTypeId();
		    if(appPlTypeId == null){
	                     return; 
	        }
	      if (appPlTypeId.equals(Constants.APP_PL_TYPE_PERSONAL)) {
	        setPersonalLoanApplicationSequenceId(appSeqIdPLoan);
	      } else if (appPlTypeId.equals(Constants.APP_PL_TYPE_PENSION)) {
	        setPensionLoanApplicationSequenceId(appSeqIdPLoan);
	      } else if (appPlTypeId.equals(Constants.APP_PL_TYPE_GOLD)) {
	        setPersonalGoldLoanSequenceId(appSeqIdPLoan);
	      } else if (appPlTypeId.equals(Constants.APP_PL_TYPE_CVE)) {
	          setCveLoanApplicationSequenceId(appSeqIdPLoan);
	      }
		} catch (NullPointerException e) {
			logger.info("SessionUtil.java LN 302 setPersonalLoanTypeSequenceId() ::", e);
	    } catch (Exception e) {
	    	logger.info("SessionUtil.java LN 302 setPersonalLoanTypeSequenceId() ::", e);
	    }
	  }
	  
	  
	  public static Integer getCveLoanTypeSequenceId()  {
		    Integer appCveTypeId = getCveTypeId();
		    if (appCveTypeId == null)
		      return null; 
		    if (appCveTypeId.equals(Constants.APP_PL_TYPE_CVE))
		      return getCveLoanApplicationSequenceId(); 
		    return null;
		  }
		  
		  public static void setCveLoanTypeSequenceId(Integer appSeqId) {
		    try {
		      Integer appCveTypeId = getCveTypeId();
		      if (appCveTypeId == null)
		        return; 
		      if (appCveTypeId.equals(Constants.APP_PL_TYPE_CVE))
		        setCveLoanApplicationSequenceId(appSeqId); 
		    } catch (NullPointerException e) {
		    	logger.info("SessionUtil.java LN 302 setCveLoanTypeSequenceId() ::", e);
		    } catch (Exception e) {
		    	logger.info("SessionUtil.java LN 302 setCveLoanTypeSequenceId() ::", e);
		    }
		  }
	  
  
	  public static Integer getPlTypeCbsCallId()  {
			Integer appPlTypeId = SessionUtil.getPersonalTypeId();
			if(appPlTypeId == null){
	                      return null; 
			}
			if(appPlTypeId.equals(Constants.APP_PL_TYPE_PERSONAL)){
	                   return getPersonalLoanCbsCallId(); 
			} else{
	                   return getPensionLoanCbsCallId();
	    }
	}

	public static void setPlTypeCbsCallId(Integer appSeqIdPensionLoan) {
			Integer appPlTypeId;
	  try {
				appPlTypeId = SessionUtil.getPersonalTypeId();
				if(appPlTypeId == null){
	                      return; 
				}
	    if (appPlTypeId.equals(Constants.APP_PL_TYPE_PERSONAL)) {
	      setPersonalLoanCbsCallId(appSeqIdPensionLoan);
	    } else {
	      setPensionLoanCbsCallId(appSeqIdPensionLoan);
	    } 
	  } catch (NullPointerException e) {
		  logger.info("SessionUtil.java LN 302 setPlTypeCbsCallId() ::", e);
	  } catch (Exception e) {
		  logger.info("SessionUtil.java LN 302 setPlTypeCbsCallId() ::", e);
	  }
	}
	
	
	public static Integer getCveTypeCbsCallId()  {
	    Integer appCveTypeId = getCveTypeId();
	    if (appCveTypeId == null)
	      return null; 
	    if (appCveTypeId.equals(Constants.APP_PL_TYPE_CVE))
	      return getPersonalLoanCbsCallId(); 
	    return getPensionLoanCbsCallId();
	  }
	  
	  public static void setCveTypeCbsCallId(Integer appSeqIdPensionLoan) {
	    try {
	      Integer appCveTypeId = getCveTypeId();
	      if (appCveTypeId == null)
	        return; 
	      if (appCveTypeId.equals(Constants.APP_PL_TYPE_CVE)) {
	        setPersonalLoanCbsCallId(appSeqIdPensionLoan);
	      } else {
	        setPensionLoanCbsCallId(appSeqIdPensionLoan);
	      } 
	    } catch (NullPointerException e) {
	    	logger.info("SessionUtil.java LN 302 setCveTypeCbsCallId() ::", e);
	    } catch (Exception e) {
	    	logger.info("SessionUtil.java LN 302 setCveTypeCbsCallId() ::", e);
	    }
	  }

  public static void setAccessToken(String accessToken) {
		getSessionAction().setAttribute(ACCESS_TOKEN, accessToken);
  }
  
  public static String getAccessToken() {
		if(getSession().get(ACCESS_TOKEN)!=null){
			return getSession().get(ACCESS_TOKEN).toString();
		}else{
                  return null;
               }
	}
  
  public static String getSignzyAccessToken() {
		if(getSession().get(SIGNZY_ACCESS_TOKEN)!=null){
			return SIGNZY_ACCESS_TOKEN;
		}else{
                  return null;
                }
	}
  
  public static void setSignzyAccessToken(String signzyAccessToken) {
		getSessionAction().setAttribute(SIGNZY_ACCESS_TOKEN, signzyAccessToken);
  }
  
  public static String getSignzyUserId() {
		if(getSession().get(SIGNZY_USER_ID)!=null){
			return SIGNZY_USER_ID;
		}else{
                    return null;
                 }
	}
  
  public static void setSignzyUserId(String signzyUserId) {
		 getSessionAction().setAttribute(SIGNZY_USER_ID, signzyUserId);
  }
  
  public static String getUiType()  {
		if (getSession().get(UI_TYPE) != null) {
			return (String) getSession().get(UI_TYPE);
		} else if (getSessionAction().getAttribute(UI_TYPE) != null) {
			return (String) getSessionAction().getAttribute(UI_TYPE);
		} else {
			return "";
		}
	}
  
  public static void setUiType(String uiType) {
		if(ValidatorUtil.isValid(uiType)){
			getSessionAction().setAttribute(UI_TYPE, uiType);
		}
	}
	
	public static void setLoanType(String loantype) {
		if(ValidatorUtil.isValid(loantype)){
			getSessionAction().setAttribute(LOAN_TYPE, loantype);
		}
	}
	
	public static String getLoanType()  {
		if (getSession().get(LOAN_TYPE) != null) {
			return getSession().get(LOAN_TYPE).toString();
		} else if (getSessionAction().getAttribute(LOAN_TYPE) != null) {
			return getSessionAction().getAttribute(LOAN_TYPE).toString();
		} else {
			return null;
		}
	}
	public static Integer getEducationBidyaLakshmiLoanSequenceId()  {
		if (getSession().get(EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSession().get(EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else if (getSessionAction().getAttribute(EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR).toString());
		} else {
			return null;
		}
	}
	
	public static void setEducationBidyaLakshmiLoanSequenceId(Integer appSeqIdEducationBidyaLakshmiLoan ) {
		getSessionAction().setAttribute(EDUCATION_BIDYA_LAKSHMI_LOAN_APP_SECQUENCE_ID_SESSION_VAR,appSeqIdEducationBidyaLakshmiLoan );
	}
	
	public static void setVisitIdHomeCampaign(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_HOME_CAMPAIGN, visitId);
	}
  
  public static Integer getVisitIdHomeCampaign()  {
		if (getSession().get(VISIT_ID_HOME_CAMPAIGN) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_HOME_CAMPAIGN).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_HOME_CAMPAIGN) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_HOME_CAMPAIGN).toString());
		} else {
                      return null;
                }
	}
  public static void setVisitIdAutoCampaign(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_AUTO_CAMPAIGN, visitId);
  }
  
  public static Integer getVisitIdAutoCampaign()  {
		if (getSession().get(VISIT_ID_AUTO_CAMPAIGN) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_AUTO_CAMPAIGN).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_AUTO_CAMPAIGN) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_AUTO_CAMPAIGN).toString());
		} else {
                      return null;
                }
	}
  public static void setVisitIdEducationCampaign(Integer visitId) {
		getSessionAction().setAttribute(VISIT_ID_EDUCATION_CAMPAIGN, visitId);
  }
  
  public static Integer getVisitIdEducationCampaign()  {
		if (getSession().get(VISIT_ID_EDUCATION_CAMPAIGN) != null) {
			return Integer.parseInt(getSession().get(VISIT_ID_EDUCATION_CAMPAIGN).toString());
		} else if (getSessionAction().getAttribute(VISIT_ID_EDUCATION_CAMPAIGN) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(VISIT_ID_EDUCATION_CAMPAIGN).toString());
		} else {
                     return null;
                }
	}
  public static void setSecretKey(String secretKey) {
		getSessionAction().setAttribute(SECRET_KEY, secretKey);
  }
  
  public static String getSecretKey()  {
		if (getSession().get(SECRET_KEY) != null) {
			return getSession().get(SECRET_KEY).toString();
		} else if (getSessionAction().getAttribute(SECRET_KEY) != null) {
			return getSessionAction().getAttribute(SECRET_KEY).toString();
		} else {
    return null;
  }
	}
  
public static Integer getHomeLoanState() {
		if (getSession().get(HOME_LOAN_STATE) != null) {
			return Integer.parseInt(getSession().get(HOME_LOAN_STATE).toString());
		} else if (getSessionAction().getAttribute(HOME_LOAN_STATE) != null) {
			return Integer.parseInt(getSessionAction().getAttribute(HOME_LOAN_STATE).toString());
		} else {
			return null;
		}
	}
	
	public static void setHomeLoanState(int state) {
		getSessionAction().setAttribute(HOME_LOAN_STATE, state);
	}
	
	public static String getHomeLoanError() {
		if (getSession().get(HOME_LOAN_ERROR) != null) {
			return getSession().get(HOME_LOAN_ERROR).toString();
		} else if (getSessionAction().getAttribute(HOME_LOAN_ERROR) != null) {
			return getSessionAction().getAttribute(HOME_LOAN_ERROR).toString();
		} else {
			return null;
		}
	}
	
	public static void setHomeLoanError(String state) {
		getSessionAction().setAttribute(HOME_LOAN_ERROR, state);
	}
	
	public static String getHomeLoanState16() {
		if (getSession().get(HOME_LOAN_STATE16_CHECK) != null) {
			return getSession().get(HOME_LOAN_STATE16_CHECK).toString();
		} else if (getSessionAction().getAttribute(HOME_LOAN_STATE16_CHECK) != null) {
			return getSessionAction().getAttribute(HOME_LOAN_STATE16_CHECK).toString();
		} else {
			return null;
		}
	}
	
	public static void setHomeLoanState16(String state) {
		getSessionAction().setAttribute(HOME_LOAN_STATE16_CHECK, state);
	}

	public static String getGoldLoan() {
		if (getSession().get(GOLD_LOAN_CHECKER) != null) {
			return getSession().get(GOLD_LOAN_CHECKER).toString();
		} else if (getSessionAction().getAttribute(GOLD_LOAN_CHECKER) != null) {
			return getSessionAction().getAttribute(GOLD_LOAN_CHECKER).toString();
		} else {
			return null;
		}
	}
	
	public static void setGoldLoan(String state) {
		getSessionAction().setAttribute(GOLD_LOAN_CHECKER, state);
	}
	
	public static String getResendMobileNumber() {
		if (getSession().get(RESEND_MOBILE_NUMBER) != null) {
			return getSession().get(RESEND_MOBILE_NUMBER).toString();
		} else if (getSessionAction().getAttribute(RESEND_MOBILE_NUMBER) != null) {
			return getSessionAction().getAttribute(RESEND_MOBILE_NUMBER).toString();
		} else {
			return null;
		}
	}
	
	public static void setResendMobileNumber(String number) {
		getSessionAction().setAttribute(RESEND_MOBILE_NUMBER, number);
	}
	
	public static String getTrackerChecker() {
		if (getSession().get(TRACKER_CHECK) != null) {
			return getSession().get(TRACKER_CHECK).toString();
		} else if (getSessionAction().getAttribute(TRACKER_CHECK) != null) {
			return getSessionAction().getAttribute(TRACKER_CHECK).toString();
		} else {
			return null;
		}
	}
	
	public static void setTrackerChecker(String number) {
		getSessionAction().setAttribute(TRACKER_CHECK, number);
	}
	
	public static void setCryptoSalt(String cryptoSalt) {
		logger.info(" setCryptoSalt Session: {}", cryptoSalt);
		logger.info(" CRYPTO_SALT {}", CRYPTO_SALT );
	    getSessionAction().setAttribute(CRYPTO_SALT, cryptoSalt);
	}

	public static String getCryptoSalt() {
		 logger.info(" getSession: {}", getSession());
		 logger.info(" getCryptoSalt ", getSession().get(CRYPTO_SALT));
		    logger.info("getCryptoSalt Action Session : {}",getSessionAction().getAttribute(CRYPTO_SALT).toString());
	    if (getSession().get(CRYPTO_SALT) != null) {
	        return getSession().get(CRYPTO_SALT).toString();
	    } else if (getSessionAction().getAttribute(CRYPTO_SALT) != null) {
	        return getSessionAction().getAttribute(CRYPTO_SALT).toString();
	    } else {
	        return null;
	    }
	}

	public static void setCryptoIv(String cryptoIv) {
		logger.info(" setCryptoIv Session: {}", cryptoIv);
		logger.info(" CRYPTO_IV {}", CRYPTO_IV );
	    getSessionAction().setAttribute(CRYPTO_IV, cryptoIv);
	}

	public static String getCryptoIv() {
		 logger.info(" getCryptoIv Session: {}", getSession().get(CRYPTO_IV).toString());
		    logger.info("getCryptoIv Action Session : {}",getSessionAction().getAttribute(CRYPTO_IV).toString());
		
	    if (getSession().get(CRYPTO_IV) != null) {
	        return getSession().get(CRYPTO_IV).toString();
	    } else if (getSessionAction().getAttribute(CRYPTO_IV) != null) {
	        return getSessionAction().getAttribute(CRYPTO_IV).toString();
	    } else {
	        return null;
	    }
	}

	public static void setCryptoPassPhrase(String cryptoPassPhrase) {
		logger.info(" setCryptoPassPhrase Session: {}", cryptoPassPhrase);
		logger.info(" CRYPTO_PASSPHRASE {}", CRYPTO_PASSPHRASE );
	    getSessionAction().setAttribute(CRYPTO_PASSPHRASE, cryptoPassPhrase);
	}

	public static String getCryptoPassPhrase() {
		
		 logger.info(" getCryptoPassPhrase Session: {}", getSession().get(CRYPTO_PASSPHRASE).toString());
		    logger.info("getCryptoPassPhrase Action Session : {}",getSessionAction().getAttribute(CRYPTO_PASSPHRASE).toString());
	    if (getSession().get(CRYPTO_PASSPHRASE) != null) {
	        return getSession().get(CRYPTO_PASSPHRASE).toString();
	    } else if (getSessionAction().getAttribute(CRYPTO_PASSPHRASE) != null) {
	        return getSessionAction().getAttribute(CRYPTO_PASSPHRASE).toString();
	    } else {
	        return null;
	    }
	}
}


in this code encryption decryption working first time on submiting correct otp it give invalid otp  , same otp submited second time its submiting sucessufull why is it happwning like this 
