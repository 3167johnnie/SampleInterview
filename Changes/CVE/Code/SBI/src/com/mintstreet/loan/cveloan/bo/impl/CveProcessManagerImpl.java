package com.mintstreet.loan.cveloan.bo.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.campaign.dao.MarTechDao;
import com.mintstreet.campaign.entity.MarTech;
import com.mintstreet.common.bo.CBSCallResponse;
import com.mintstreet.common.bo.CBSCustomerInformation;
import com.mintstreet.common.bo.CBSLoanAccountInformation;
import com.mintstreet.common.bo.CRMRequest;
import com.mintstreet.common.bo.CRMResponse;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.MasterBranch;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.common.entity.MasterCBSResponse;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.status.StatusManager;
import com.mintstreet.common.status.StatusManagerResponse;
import com.mintstreet.common.status.StatusRequest;
import com.mintstreet.common.util.CRMServiceNew;
import com.mintstreet.common.util.CbsUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCaseCve;
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;
import com.mintstreet.loan.cveloan.service.CveLoanService;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.loan.personal.util.PersonalLoanHelper;

public class CveProcessManagerImpl {

	private static final Logger logger = LogManager.getLogger(CveProcessManagerImpl.class.getName());
	@Autowired
	private CommonService commonService;

	@Autowired
	private PersonalLoanService personalLoanService;
	
	@Autowired
	private PersonalLoanHelper personalLoanHelper;
	
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	
	@Autowired
	private SbiUtil SbiUtil;
	
	@Autowired
	private CbsUtil cbsUtil;
	
	@Autowired
	private CRMServiceNew crmServiceNew;
	
	@Autowired
	private MarTechDao marTechDao;

	@Autowired
	private CveLoanService cveLoanService;
	
	public volatile String appRefKey;
	public volatile String lastReferenceNumber;
	
	StringBuffer formatJson;
	
	public static Integer branchCode;
	public static String refCve;
	public static String getcif;	
	public String caseMessage;
	public static Integer cbsIsdCodeCve;
	public String FIRSTNAME; 
	public String MIDDLENAME;
	public String LASTNAME;
	
	public CBSCallResponse  processCbsCall(Integer appSeqId, Integer requestIndex, 
			MasterCBSCall masterCbsCall,String isDsrPage, Integer bankLMSUserId,
			Integer visitId, Integer cbsCallId, Integer loanQuoteLoanPurposeId,
			 Integer socialMediaId, Integer deviceId,String receivedAction) throws SQLException {
		
		ApplicationFormCveLoan cveForm = new ApplicationFormCveLoan();
		CBSCallResponse cbsCallResponse = new CBSCallResponse();
		
		String cveFlag = Constants.CVE_ACTION;
		masterCbsCall.setCbsLoanTypeId(Constants.PERSONAL_LOAN_ID);
		int count = commonService.getCBSApplicationCount(masterCbsCall.getCbsIsdCode(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID);
//		logger.info("CveProcessManagerImpl.java LNo : 3384 count : "+count+" with AppSeqId "+appSeqId+"--getCbsIsdCode--"+masterCbsCall.getCbsIsdCode());
		if(count>Constants.APP_MAXIMUM_OTP_REQUEST_COUNT){
			cbsCallResponse.setResponseMsg(Constants.APP_MAXIMUM_OTP_REQUEST_MSG);
			cbsCallResponse.setStatus(0);
			logger.info("CveProcessManagerImpl.java LNo :2644:: INSIDE IF "+cbsCallResponse.getStatus());
			return cbsCallResponse;
		}
		cbsCallResponse.setStatus(0);
		logger.info("CveProcessManagerImpl.java LNo :2644:: ELSE "+cbsCallResponse.getStatus());
		try{
			
			 boolean isAppFoundForDedupInDropOffStage = false;
			 boolean isAppFoundForDedupInDropRejectStage = false;	
			
			if (receivedAction!=null && receivedAction.equalsIgnoreCase(cveFlag)) 
			{
				logger.info("CveProcessManagerImpl.java :: LNo 2737 :: CBS_DEDUPE_BYPASS for CVE ");
				if(Constants.CBS_DEDUPE_BYPASS && loanQuoteLoanPurposeId!=null){
					String oldMobile="";
					String isdCode="";
					if(appSeqId == null && masterCbsCall.getCbsMobileNumber()!=null){
						isdCode = ( masterCbsCall.getCbsIsdCode()==null?Constants.COUNTRY_CODE_INDIA:masterCbsCall.getCbsIsdCode().toString() );
						oldMobile =  masterCbsCall.getCbsMobileNumber();
					}
					if(!Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0")){

						boolean isAppFoundForDedupInApplicationStage = false;
						isAppFoundForDedupInApplicationStage = personalLoanService.isAppFoundForDedupInApplicationStage( null, isdCode, oldMobile, loanQuoteLoanPurposeId );
						logger.info("CveProcessManagerImpl.java :: LNo 3554 :: isAppFoundForDedupInApplicationStage "+isAppFoundForDedupInApplicationStage+" with AppSeqId "+appSeqId);
						if(isAppFoundForDedupInApplicationStage){
							cbsCallResponse.setResponseMsg(Constants.APP_DEDUPLICATION_MESSAGE);
							return cbsCallResponse;
						}
						isAppFoundForDedupInDropOffStage = personalLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
						logger.info("CveProcessManagerImpl.java :: LNo 3427 :: isAppFoundForDedupInDropOffStage "+isAppFoundForDedupInDropOffStage+" with AppSeqId "+appSeqId);
						isAppFoundForDedupInDropRejectStage = personalLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, loanQuoteLoanPurposeId);
						logger.info("CveProcessManagerImpl.java :: LNo 3429 :: isAppFoundForDedupInDropRejectStage "+isAppFoundForDedupInDropRejectStage+" with AppSeqId "+appSeqId);
					}
				}
			}  
						
//			logger.info("visitId::" + visitId);
			masterCbsCall.setCbsVisitId(visitId);
			
//			logger.info("cbsCallId::" + cbsCallId);		
//			logger.info("CveProcessManagerImpl LNO :: 2944--" + cveForm.getCveAppConsentRevokeYes() + "----" + masterCbsCall.getCbsIsdCodeCve() + "--cbsIsdCodeCve--" + cbsIsdCodeCve);
			Integer cbsIsdCode = 91;
			
			if(cbsCallId!=null){
				MasterCBSCall oldMasterCbsCall = commonService.getMasterCBSCallObjById(cbsCallId);
				oldMasterCbsCall.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
				oldMasterCbsCall.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
				
				if (masterCbsCall.getCbsIsdCodeCve() != null && cbsIsdCode != masterCbsCall.getCbsIsdCodeCve()) {
		          oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCodeCve());
//		          logger.info("CveProcessManagerImpl LNO :: IF:::::::" + masterCbsCall.getCbsIsdCodeCve() + "***" + oldMasterCbsCall.getCbsIsdCode());
		        } else {
		          oldMasterCbsCall.setCbsIsdCode(masterCbsCall.getCbsIsdCode());
//		          logger.info("CveProcessManagerImpl LNO :: ELSE::::::" + masterCbsCall.getCbsIsdCodeCve() + "***" + oldMasterCbsCall.getCbsIsdCode());
		        }
				
				oldMasterCbsCall.setCbsTypeOfRelationship(masterCbsCall.getCbsTypeOfRelationship());
				oldMasterCbsCall = commonService.save(oldMasterCbsCall);
				if(oldMasterCbsCall==null){
					logger.info("CveProcessManagerImpl LNO :: 3443");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
			}else{
				masterCbsCall.setCbsRequiestedTime(new Date());
				masterCbsCall.setCbsOtpVerified(0);
				masterCbsCall = commonService.save(masterCbsCall);
				if(masterCbsCall==null){
					logger.info("CveProcessManagerImpl LNO :: 3453");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
				SessionUtil.setPlTypeCbsCallId(masterCbsCall.getCbsId());
			}
						
			cbsCallResponse.setCbsCallId(masterCbsCall.getCbsId());
			cbsCallResponse.setVisitId(visitId);
			logger.info("SessionUtil================="+SessionUtil.getPlTypeCbsCallId()+" with AppSeqId "+appSeqId);
			MasterCBSResponse masterCBSResponse = null;
			if(masterCbsCall.getCbsResponseId() == null){
				masterCBSResponse = new MasterCBSResponse();
			}else{
				masterCBSResponse = commonService.getMasterCBSResponseById(masterCbsCall.getCbsResponseId());
				if(masterCBSResponse == null){
					logger.info("CveProcessManagerImpl LNO :: 3469");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
			}
			
			if(masterCbsCall.getCbsNoOfExistingXpressCredit() != null){
				Integer noOfExistingXpressCredit = masterCbsCall.getCbsNoOfExistingXpressCredit();
				if(noOfExistingXpressCredit.toString().equals("2")){
					logger.info("CveProcessManagerImpl.java LNo: 2769 ::CBS_NO_OF_EXIST_XPRESS_CREDIT is equal to 2");
					cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
			}
			
			if(masterCbsCall.getCbsNoOfExistingPensionLoan() != null){
				Integer noOfExistingAccount = masterCbsCall.getCbsNoOfExistingPensionLoan();
				if(noOfExistingAccount.toString().equals("2")){
					logger.info("CveProcessManagerImpl.java LNo: 3302 ::CBS_NO_OF_EXIST_ACCOUNT is equal to 2");
					cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
			}
			   
			logger.info("Before Caaling callingCBSEngineForCIFLevelInformation");    
			JSONObject cbsEngineResponseJson = cbsUtil.callingCBSEngineForCIFLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID);
			CBSCustomerInformation cbsCustomerInformation = new CBSCustomerInformation();   
			cbsUtil.setCBSCustomerInformationBean(cbsCustomerInformation,cbsEngineResponseJson);    

			if(cbsCustomerInformation.getStatus()!=null && cbsCustomerInformation.getStatus().equals("0")){
				logger.info("CveProcessManagerImpl 2978"+" with AppSeqId ::"+appSeqId);
				if(cbsCustomerInformation.getErrorReason()!=null ) {
					if(cbsCustomerInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")){
						cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
					}  else {   
						cbsCallResponse.setResponseMsg(cbsCustomerInformation.getErrorReason().trim());
					}            
				}else{
					cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
				}
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			
			if(cbsCustomerInformation.getError()!=null  && cbsCustomerInformation.getError().trim().length() > 0){       
				logger.info("CveProcessManagerImpl LNO :: 2698 Either Account No. or Mobile No. do not match with our data base.");
				cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			
			//logger.info("CBS Mobile No : "+cbsCustomerInformation.getMOBILENO()+" Input Mobile No :  "+masterCbsCall.getCbsMobileNumber() +" For appSeqId :  "+appSeqId);
			if(!masterCbsCall.getCbsMobileNumber().equals(cbsCustomerInformation.getMOBILENO())) {
				cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
				cbsCallResponse.setStatus(0);                    
				return cbsCallResponse;       
			} 
			
			JSONObject accountInfoResponseJson = cbsUtil.callingCBSEngineForAccountLevelInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(), Constants.PERSONAL_LOAN_ID);
			
			if(accountInfoResponseJson.has("ERROR_CODE")  && accountInfoResponseJson.get("ERROR_CODE") !=null && accountInfoResponseJson.get("ERROR_CODE").toString().trim().length() > 0){
				logger.info("Personal Loan  LNO :: 2423");
				if(accountInfoResponseJson.get("ERROR_DESCRIPTION") !=null) {
					if(accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim().equalsIgnoreCase("INVALID CHECK DIGIT")){
						cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
						logger.info("callingCBSEngineForAccountLevelInformation INVALID CHECK DIGIT:: "+cbsCallResponse.getResponseMsg());
					}
					else {    
						cbsCallResponse.setResponseMsg(accountInfoResponseJson.get("ERROR_DESCRIPTION").toString().trim());
						logger.info("callingCBSEngineForAccountLevelInformation ERROR_DESCRIPTION :: "+cbsCallResponse.getResponseMsg());
					}
				}else{    
					cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
					logger.info("callingCBSEngineForAccountLevelInformation ELSE system has encountered a technical error :: "+cbsCallResponse.getResponseMsg());
				}
				cbsCallResponse.setStatus(0);   
				return cbsCallResponse;   
			}			
      
			String prodId = "";   
			if(accountInfoResponseJson.has("AccType")) {      
				prodId = accountInfoResponseJson.getString("AccType");      
			} 
			if (accountInfoResponseJson.has("Interest_Category")) {          
				prodId+= accountInfoResponseJson.getString("Interest_Category");     
			}   
			cbsCustomerInformation.setProductId(prodId);       
//			logger.info("Response from Customer Enquiry and Account info Data Account no : "+cbsCustomerInformation.getACCOUNTNUMBER()+" Product Id : "+cbsCustomerInformation.getProductId());
			cbsCustomerInformation.setAccountDesc(accountInfoResponseJson.getString("AccountDescription"));
			 
		
			String cve = "cve";
//			logger.info("CveProcessManagerImpl.java LNO 3971 cveFlag:: "+cveFlag+"--PersonalId--"+SessionUtil.getPersonalTypeId());
			
			boolean needToByPassLoanAccountInformation = true;
			if(masterCbsCall.getCbsTypeOfRelationship()!=null){
				Integer typeOfRelationship = masterCbsCall.getCbsTypeOfRelationship();
				
				logger.info("needToByPassLoanAccountInformation FOR CVE typeOfRelationship:"+typeOfRelationship);
				if(masterCbsCall.getCbsLoanPurpose()!=null && masterCbsCall.getCbsLoanPurpose().intValue() == 23){
					logger.info("needToByPassLoanAccountInformation FOR 23 :: "+masterCbsCall.getCbsLoanPurpose()+" with TypeOfRelationship "+masterCbsCall.getCbsTypeOfRelationship());
					
				}else if(masterCbsCall.getCbsLoanPurpose()!=null && masterCbsCall.getCbsLoanPurpose().intValue() == 27){
					if(typeOfRelationship.toString().equals("1") || typeOfRelationship.toString().equals("2")){
						needToByPassLoanAccountInformation = false;
					//	logger.info("needToByPassLoanAccountInformation FOR 27 typeOfRelationship 1 or 2:: "+needToByPassLoanAccountInformation);
					}
				}else if(receivedAction!=null && (receivedAction.equalsIgnoreCase(cve)) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL)){
					//logger.info("needToByPassLoanAccountInformation INSIDE typeOfRelationship for CVE :: ");
					if(typeOfRelationship.toString().equals("1") || typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3") || typeOfRelationship.toString().equals("4") || typeOfRelationship.toString().equals("5")){
						needToByPassLoanAccountInformation = false;
						//logger.info("needToByPassLoanAccountInformation ELSE typeOfRelationship for CVE :: "+needToByPassLoanAccountInformation);
					}
				}else{		
					if(typeOfRelationship.toString().equals("2") || typeOfRelationship.toString().equals("3")){
						needToByPassLoanAccountInformation = false;
						logger.info("needToByPassLoanAccountInformation ELSE typeOfRelationship 2 or 3 :: "+needToByPassLoanAccountInformation);
					} 
				}
			}
			
			if(ValidatorUtil.isValid(cbsCustomerInformation.getProductId())){
				
				if(masterCbsCall.getCbsLoanPurpose()!=null && masterCbsCall.getCbsLoanPurpose().intValue() == 23){
					int productId=Integer.parseInt(cbsCustomerInformation.getProductId());
					if(productId == 62517901 || productId == 62517902 || productId == 64502021 || productId == 64504214 || productId == 62507901){
						logger.info("Product Id : "+productId+" prodID "+prodId+"  with AppSeqId "+appSeqId);    
						cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
						cbsCallResponse.setStatus(0);     
						return cbsCallResponse;
					}
					if(masterCbsCall.getCbsTypeOfRelationship().intValue()==1){
						if(productId == 63507901 || productId == 63507902 || productId == 63507903 || productId == 63507911 || productId == 63507912
								 || productId == 63517901 || productId == 63517902 || productId == 63517911 || productId == 63517912 || productId ==  63517913
								 || productId == 63517914 || productId == 63517915 || productId == 64514215 || productId == 62511138){
						}else{
							logger.info("product Id :: "+productId+" with AppSeqId "+appSeqId);
							logger.info("CveProcessManagerImpl.java LNo: 3455 : Product id is not eligible For Avialing loan");
							cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
							cbsCallResponse.setStatus(0);
							return cbsCallResponse;
						}

					}else if(masterCbsCall.getCbsTypeOfRelationship().intValue()==2){
						if(productId == 63507901 || productId == 63507902 || productId == 63507903 || productId == 63507911 || productId == 63507912
								 || productId == 63517901 || productId == 63517902 || productId == 63517911 || productId == 63517912 || productId ==  63517913
								 || productId == 63517914 || productId == 63517915 || productId == 64514215){
						}else{
							logger.info("product Id :: "+productId+" with AppSeqId "+appSeqId);
							logger.info("CveProcessManagerImpl.java LNo: 3465 : Product id is not eligible For Avialing loan");
							cbsCallResponse.setResponseMsg(Constants.COMMON_SORRY_MSG);
							cbsCallResponse.setStatus(0);
							return cbsCallResponse;   
						}
					}
				}else{
					if(needToByPassLoanAccountInformation){
						logger.info("CveProcessManagerImpl.java LNo: 2744 : Product existence check for Loan Id "+Constants.PERSONAL_LOAN_ID+" and product id "+cbsCustomerInformation.getProductId());
						boolean productFound = commonService.isCbsMappingsExistByProductId(Constants.PERSONAL_LOAN_ID, cbsCustomerInformation.getProductId());
						logger.info("CveProcessManagerImpl.java LNo: 2746 : Product found status "+productFound);
						if(!productFound){
							logger.info("CveProcessManagerImpl.java LNo: 2706 : Product id not match with mapping table");
							cbsCallResponse.setResponseMsg("Provided account No. does not pertain to selected type of relationship");
							cbsCallResponse.setStatus(0);
							return cbsCallResponse;
						}
					}
				}
			}else{
				logger.info("CveProcessManagerImpl.java LNo: 2712 : Product id not received  in Customer information service");
				cbsCallResponse.setResponseMsg("Product id not received  in Customer information service");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			try{
				masterCbsCall.setCbsResponseTime(new Date());
				masterCBSResponse.setCbsLoanTypeId(Constants.PERSONAL_LOAN_ID);
				logger.info("CveProcessManagerImpl.java LNo: 2893 :"+masterCBSResponse.getCbsLoanTypeId());
				if(ValidatorUtil.isValid(cbsCustomerInformation.getReferenceNumber())){
					masterCBSResponse.setCbsReferenceNumber(cbsCustomerInformation.getReferenceNumber());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getACCOUNTNUMBER())){
					masterCBSResponse.setCbsAccountNumber(cbsCustomerInformation.getACCOUNTNUMBER());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getCIFNUMBER())){
					masterCBSResponse.setCbsCifNumber(cbsCustomerInformation.getCIFNUMBER());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getSALUTATION())){
					masterCBSResponse.setCbsSalutation(cbsCustomerInformation.getSALUTATION());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getFIRSTNAME())){
					masterCBSResponse.setCbsFirstName(cbsCustomerInformation.getFIRSTNAME());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getMIDDLENAME())){
					masterCBSResponse.setCbsMiddleName(cbsCustomerInformation.getMIDDLENAME());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getLASTNAME())){
					masterCBSResponse.setCbsLastName(cbsCustomerInformation.getLASTNAME());
				}
				
				//logger.info("CveProcessManagerImpl.java LNo: 2916 :"+cbsCustomerInformation.getACCOUNTNUMBER());
				//logger.info("CveProcessManagerImpl.java LNo: 2917 :"+cbsCustomerInformation.getCIFNUMBER());
				
				FIRSTNAME = masterCBSResponse.getCbsFirstName();
				//logger.info("CveProcessManagerImpl getFIRSTNAME from CBS::"+masterCBSResponse.getCbsFirstName()+"**FIRSTNAME**"+FIRSTNAME);			
				
				MIDDLENAME = masterCBSResponse.getCbsMiddleName();
				//logger.info("CveProcessManagerImpl getMIDDLENAME from CBS::"+masterCBSResponse.getCbsMiddleName()+"**MIDDLENAME**"+MIDDLENAME);
				
				LASTNAME = masterCBSResponse.getCbsLastName();
				//logger.info("CveProcessManagerImpl getLASTNAME from CBS::"+masterCBSResponse.getCbsLastName()+"**LASTNAME**"+LASTNAME);
				
				masterCbsCall.setEMAIL(masterCBSResponse.getCbsEmail());
				//logger.info("CveProcessManagerImpl getEMAIL from CBS::"+masterCbsCall.getEMAIL());
				
				 getCIFNUMBER(cbsCustomerInformation.getCIFNUMBER());
				 
				 if(ValidatorUtil.isValid(cbsCustomerInformation.getCIFNUMBER())) {
					 masterCbsCall.setCveCifNumber(cbsCustomerInformation.getCIFNUMBER());
//					 logger.info("CveProcessManagerImpl LNo: 3232 getCbsCifNumber:::"+masterCbsCall.getCveCifNumber());
				 }
				  
				//logger.info("CveProcessManagerImpl LNo: 2918 :"+cbsCustomerInformation.getSALUTATION());
				//logger.info("CveProcessManagerImpl LNo: 2919 :"+cbsCustomerInformation.getFIRSTNAME());
				
				if(ValidatorUtil.isValid(cbsCustomerInformation.getDATEOFBIRTH())){
					masterCBSResponse.setCbsDateOfBirth(DateUtil.convertStringToDateWithOutDelimiter(cbsCustomerInformation.getDATEOFBIRTH()));
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE1())){
					masterCBSResponse.setCbsAddressLine1(cbsCustomerInformation.getADDRESSLINE1());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE2())){
					masterCBSResponse.setCbsAddressLine2(cbsCustomerInformation.getADDRESSLINE2());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getADDRESSLINE3())){
					masterCBSResponse.setCbsAddressLine3(cbsCustomerInformation.getADDRESSLINE3());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getDISTRICT())){
					masterCBSResponse.setCbsDistrict(cbsCustomerInformation.getDISTRICT());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getSUBDISTRICT())){
					masterCBSResponse.setCbsSubDistrict(cbsCustomerInformation.getSUBDISTRICT());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getCITY())){
					masterCBSResponse.setCbsCity(cbsCustomerInformation.getCITY());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getSTATE())){
					masterCBSResponse.setCbsState(cbsCustomerInformation.getSTATE());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getPINCODE())){
					masterCBSResponse.setCbsPinCode(cbsCustomerInformation.getPINCODE());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getMOBILENO())){
					masterCBSResponse.setCbsMobileNo(cbsCustomerInformation.getMOBILENO());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getLANDLINENO())){
					masterCBSResponse.setCbsLandlineNo(cbsCustomerInformation.getLANDLINENO());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getPANNUMBER())){
					masterCBSResponse.setCbsPanNumber(cbsCustomerInformation.getPANNUMBER());
				}

				if(ValidatorUtil.isValid(cbsCustomerInformation.getIDTYPE())){
					masterCBSResponse.setCbsIdType(cbsCustomerInformation.getIDTYPE());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getIDNUMBER())){
					masterCBSResponse.setCbsIdNumber(cbsCustomerInformation.getIDNUMBER());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getEMAIL())){
					masterCBSResponse.setCbsEmail(cbsCustomerInformation.getEMAIL());
				}
				logger.info("Phone Business ::"+StringUtils.isNumeric(cbsCustomerInformation.getPhoneBusiness())+" with AppSeqId "+appSeqId);
				
				if(ValidatorUtil.isValid(cbsCustomerInformation.getPhoneBusiness())){
					if(StringUtils.isNumeric(cbsCustomerInformation.getPhoneBusiness())){
						masterCBSResponse.setCbsPhoneBusiness(cbsCustomerInformation.getPhoneBusiness());
					}
				}
				
				if(ValidatorUtil.isValid(cbsCustomerInformation.getCountry())){
					masterCBSResponse.setCbsCountry(cbsCustomerInformation.getCountry());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getPFNo())){
					masterCBSResponse.setCbsPfNo(cbsCustomerInformation.getPFNo());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getError())){
					masterCBSResponse.setCbsError(cbsCustomerInformation.getError());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getAccountDesc())){
					masterCBSResponse.setCbsAccountDescription(cbsCustomerInformation.getAccountDesc());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getGender())){
					masterCBSResponse.setCbsGender(cbsCustomerInformation.getGender());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getBranchCode())){
					masterCBSResponse.setCbsBranchCode(cbsCustomerInformation.getBranchCode());
				}
				if(ValidatorUtil.isValid(cbsCustomerInformation.getProductId())){
					masterCBSResponse.setCbsProductId(cbsCustomerInformation.getProductId());
				}
				masterCBSResponse = commonService.save(masterCBSResponse);
				if(masterCBSResponse==null){
					logger.info("CveProcessManagerImpl LNo: 2815");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}

			} catch(SQLException e){
				logger.info("CveProcessManagerImpl LNo: 2822 :: exception caught " + e.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			} catch(Exception e){
				logger.info("CveProcessManagerImpl LNo: 2822 :: exception caught " + e.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
						
			CBSLoanAccountInformation cbsLoanAccountInformation = new CBSLoanAccountInformation();
			if(needToByPassLoanAccountInformation){
				JSONObject loanAccountInformationcbsResponseJson = cbsUtil.callingCBSEngineForLoanAccountInformation(masterCbsCall.getCbsAccountNumber(), masterCbsCall.getCbsMobileNumber(),Constants.CBS_LOAN_TYPE_XPRESS_CREDIT_TOP_UP);
				cbsUtil.setCbsLoanAccountInformation(cbsLoanAccountInformation,loanAccountInformationcbsResponseJson);
			
				if(cbsLoanAccountInformation.getStatus()!=null && cbsLoanAccountInformation.getStatus().equals("0")){
					logger.info("Personal Loan  :: 2571"+" with AppSeqId "+appSeqId);
					if(cbsLoanAccountInformation.getErrorReason()!=null ) {
						if( cbsLoanAccountInformation.getErrorReason().trim().equalsIgnoreCase("INVALID CHECK DIGIT")){
							cbsCallResponse.setResponseMsg("Either Account No. or Mobile No. do not match with our records.");
						}
						else {   
							cbsCallResponse.setResponseMsg(cbsLoanAccountInformation.getErrorReason().trim());
						}    
					}else{
						cbsCallResponse.setResponseMsg("Sorry, the system has encountered a technical error at the moment. Please try again after few minutes.");
					}
					cbsCallResponse.setStatus(0);       
					return cbsCallResponse;
				}				

				cbsLoanAccountInformation.setBranchCode(cbsCustomerInformation.getBranchCode());
				cbsLoanAccountInformation.setReferenceNumber(cbsEngineResponseJson.getString("REQUEST_REFERENCE_NUMBER"));
			}
			
			masterCbsCall.setCbsResponseId(masterCBSResponse.getCbsResponseId());
			logger.info("setCbsResponseId :: "+masterCBSResponse.getCbsResponseId()+" with AppSeqId "+appSeqId);
			if(ValidatorUtil.isValid(masterCBSResponse.getCbsEmail())){
				masterCbsCall.setCbsEmail(masterCBSResponse.getCbsEmail());
				masterCbsCall.setCbsEmailMask(masterCBSResponse.getCbsEmail());
				masterCbsCall.setCbsIsEligibleForEmailOtp(1);
			}else{
				masterCbsCall.setCbsIsEligibleForEmailOtp(0);
			}
			masterCbsCall = commonService.save(masterCbsCall);
			if(masterCbsCall==null){
				logger.info("CveProcessManagerImpl LNo: 2857 :: ");
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			
			try {
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getExistingLoanAcc())){
					masterCBSResponse.setCbsExistingLoanAcNo(cbsLoanAccountInformation.getExistingLoanAcc());
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getSchemeName())){
					masterCBSResponse.setCbsSchemeName(cbsLoanAccountInformation.getSchemeName());
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getSanctionedLimit())){
					masterCBSResponse.setCbsSanctionedLimit(Double.parseDouble(cbsLoanAccountInformation.getSanctionedLimit()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getSanctiondate())){
					masterCBSResponse.setCbsSanctionDate(DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getSanctiondate()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getLoanTenure())){
					masterCBSResponse.setCbsLoanTenure(Integer.parseInt(cbsLoanAccountInformation.getLoanTenure()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getRepaymentstartdate())){
					masterCBSResponse.setCbsRepaymentStartDate(DateUtil.convertStringToDateWithOutDelimiter(cbsLoanAccountInformation.getRepaymentstartdate()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getValueofPrimarySecurity())){
					masterCBSResponse.setCbsValueOfPrimarySecurity(Double.parseDouble(cbsLoanAccountInformation.getValueofPrimarySecurity()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getIRACStatus())){
					masterCBSResponse.setCbsIracStatus(cbsLoanAccountInformation.getIRACStatus());
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getEMI())){
					masterCBSResponse.setCbsEmi(Double.parseDouble(cbsLoanAccountInformation.getEMI()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getOutstanding())){
					masterCBSResponse.setCbsOutstanding(Double.parseDouble(cbsLoanAccountInformation.getOutstanding()));
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getInstituteName())){
					masterCBSResponse.setCbsInstituteName(cbsLoanAccountInformation.getInstituteName());
				}
				if(ValidatorUtil.isValid(cbsLoanAccountInformation.getBranchCode())){
					masterCBSResponse.setCbsBranchCode(cbsLoanAccountInformation.getBranchCode());
				}
				masterCBSResponse = commonService.save(masterCBSResponse);
				if(masterCBSResponse==null){
					logger.info("CveProcessManagerImpl LNo: 2902 ::");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
				
			} catch(ParseException ex){
				logger.info("CveProcessManagerImpl LNo: 2909 :: exception caught "+ ex.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			} catch (Exception e) {
				logger.info("CveProcessManagerImpl LNo: 2914 :: exception caught " + e.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			
			if(!Constants.CBS_IRAC_STATUS_BYPASS){
				if(needToByPassLoanAccountInformation){
					if(Integer.parseInt(masterCBSResponse.getCbsIracStatus())>= Constants.CBS_IRAC_STATUS){
						logger.info("CveProcessManagerImpl LNo: 2922 ::CBS_IRAC_STATUS is greater than or equal to 4");
						cbsCallResponse.setResponseMsg("Dear customer, we are unable to process your application right now. Kindly contact the nearest branch for further details.");
						cbsCallResponse.setStatus(0);
						return cbsCallResponse;
					}
				}
			}
			
			ApplicationFormCveLoan cveApp = null;
			
			logger.info("CveProcessManagerImpl LNo: 3435 receivedAction before if condition----"+receivedAction);		
			
			if(receivedAction.equalsIgnoreCase(Constants.CVE_ACTION)){	
				logger.info("CveProcessManagerImpl LNo: 3449 receivedAction for CVE----"+receivedAction);		
				
			if(masterCBSResponse.getCbsAppSeqId()==null){
				cveApp = new ApplicationFormCveLoan();
				logger.info("CveProcessManagerImpl LNo: 3453 ::IF CONDITION getCbsAppSeqId----"+masterCBSResponse.getCbsAppSeqId());
			} else {
				
				logger.info("CveProcessManagerImpl LNo: 3458 :: ELSE CONDITION getCbsAppSeqId----"+masterCBSResponse.getCbsAppSeqId());
				cveApp = personalLoanService.getApplicationFormCveLoanByAppSeqId(masterCBSResponse.getCbsAppSeqId());
				logger.info("CveProcessManagerImpl LNo: 3460 ::getCbsAppSeqId----"+masterCBSResponse.getCbsAppSeqId());
				
				if(cveApp == null){
					logger.info("CveProcessManagerImpl LNo: 2938 ::");
					cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
					cbsCallResponse.setStatus(0);
					return cbsCallResponse;
				}
			}
				
			try{
				cveApp.setAppEmailVerified("N");
				logger.info("getAppEmailVerified ::"+cveApp.getAppEmailVerified());
				
				cveApp.setAppMobileVerified("N");
				logger.info("getAppMobileVerified ::"+cveApp.getAppMobileVerified());
				
				if(cveApp.getAppLeadUpdateTime()==null){
					cveApp.setAppLeadUpdateTime(new Date());
				}
				if(cveApp.getCreatedDate()==null){
					cveApp.setCreatedDate(new Date());
				}		
				
				if(ValidatorUtil.isValid(masterCbsCall.getCbsIsdCode())){
					if(Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCbsCall.getCbsIsdCode().toString())){
						cveApp.setAppApplyingFrom(1);
						logger.info("IF CONDITION getAppApplyingFrom ::"+cveApp.getAppApplyingFrom());
					}else{
						cveApp.setAppApplyingFrom(2);
						logger.info("ELSE CONDITION getAppApplyingFrom ::"+cveApp.getAppApplyingFrom());
					}
				}else{
					cveApp.setAppApplyingFrom(1);
					logger.info("ELSE CONDITION getAppApplyingFrom(1) ::"+cveApp.getAppApplyingFrom());
				}
				
				cveApp.setAppLeadUpdateTime(new Date());
				
				 logger.info("getAppLeadUpdateTime ::" + cveApp.getAppLeadUpdateTime());
		         // logger.info("Phone Business ::" + StringUtils.isNumeric(masterCBSResponse.getCbsPhoneBusiness()));
		          //logger.info("setAppISDCodeeeeeee1::" + masterCbsCall.getCbsIsdCodeCve());
		          logger.info("getCveAppConsentRevokeYes......." + masterCbsCall.getCveAppConsentRevokeYes()); 
		          
		          if (masterCbsCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N")) {
		          //    logger.info("setAppISDCode getCbsIsdCodeCve:::" + masterCbsCall.getCbsIsdCodeCve() + "..RevokeNo.." + cveApp.getCveAppConsentRevokeYes());
		              cveApp.setAppISDCode(Integer.toString(masterCbsCall.getCbsIsdCodeCve().intValue()));
		            //  logger.info("setAppISDCode getAppISDCode::" + cveApp.getAppISDCode() + "****" + masterCbsCall.getCbsIsdCodeCve());
		              SessionUtil.setISDCode(Integer.toString(masterCbsCall.getCbsIsdCodeCve().intValue()));
		            } else {
		              //logger.info("setAppISDCode getCbsIsdCode:::" + masterCbsCall.getCbsIsdCode() + "..RevokeYes.." + cveApp.getCveAppConsentRevokeYes());
		              cveApp.setAppISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
		              //logger.info("setAppISDCode getAppISDCode::" + cveApp.getAppISDCode() + "****" + masterCbsCall.getCbsIsdCode());
		              SessionUtil.setISDCode(Integer.toString(masterCbsCall.getCbsIsdCode().intValue()));
		            } 
		          
		          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
		            cveApp.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
		            //logger.info("setAppMobileNo ::" + masterCbsCall.getCbsMobileNumber());
		            SessionUtil.setMobile(masterCbsCall.getCbsMobileNumber());
		            //logger.info("setMobile ::" + masterCbsCall.getCbsMobileNumber());
		          } 
		          if (ValidatorUtil.isValid(masterCbsCall.getCbsMobileNumber())) {
		            cveApp.setAppMobileVerificationCode(this.SbiUtil.getVerificationCode(masterCbsCall.getCbsMobileNumber()));
		            logger.info("getAppMobileVerified ::" + cveApp.getAppMobileVerified());
		          } 
						
				
				cveApp.setCbsAccountNumber(masterCbsCall.getCbsAccountNumber());
				//logger.info("getCbsAccountNumber ::"+cveApp.getCbsAccountNumber());
				
				cveApp.setCbsMobileNumber(masterCbsCall.getCbsMobileNumber());
				//logger.info("getCbsMobileNumber :: "+masterCbsCall.getCbsMobileNumber());
				
				cveApp.setCveProductCategory(masterCbsCall.getCveProductCategory());
				logger.info("getCveProductCategory :: "+cveApp.getCveProductCategory()); 
				
				cveApp.setCveSalutation(masterCbsCall.getCveSalutation());
				//logger.info("getCveSalutation :: "+cveApp.getCveSalutation()); 
				
				cveApp.setCveAppFirstName(masterCbsCall.getCveAppFirstName());
				//logger.info("getCveAppFirstName :: "+cveApp.getCveAppFirstName());
				
				cveApp.setCveAppMiddleName(masterCbsCall.getCveAppMiddleName());
				//logger.info("getCveAppMiddleName :: "+cveApp.getCveAppMiddleName());
				
				cveApp.setCveAppLastName(masterCbsCall.getCveAppLastName());
				//logger.info("getCveAppLastName :: "+cveApp.getCveAppLastName());
				
				//logger.info("getCbsEmail*********::"+masterCbsCall.getCbsEmail()+"******"+masterCBSResponse.getCbsEmail());
				
				if(masterCbsCall.getCbsEmail()!=null) {
					cveApp.setCveAppEmail(masterCbsCall.getCbsEmail());
					//logger.info("getCveAppEmail CBS :: "+cveApp.getCveAppEmail());
				}
				else {
					cveApp.setCveAppEmail(masterCbsCall.getEMAIL());
					//logger.info("getCveAppEmail :: "+cveApp.getCveAppEmail());
				}
									
				cveApp.setCveAppPrevBranchId(masterCbsCall.getLoanQuoteWorkBranchId());
				//logger.info("getCveAppPrevBranchId :: "+cveApp.getCveAppPrevBranchId());
				
				cveApp.setCbsCifNumber(masterCbsCall.getCveCifNumber());
				//logger.info("getCbsCifNumber :: "+cveApp.getCbsCifNumber());
				
				cveApp.setCveAppConsentRevokeYes(masterCbsCall.getCveAppConsentRevokeYes());
				logger.info("getCveAppConsentRevokeYes :: "+cveApp.getCveAppConsentRevokeYes()); 
				
				String cveCustomerType = cveApp.getCveAppConsentRevokeYes().equalsIgnoreCase("Y") ? "REV" : "NA";
		        Consent consent = commonService.getConsentByLoanAndCustomerType(Constants.CVE_ID, cveCustomerType);
		        Integer consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
		        cveApp.setLoanQuoteConsentId(consentId); 
		        
				if(!ValidatorUtil.isValid(cveApp.getAppReferenceId()))
			    {		
					lastReferenceNumber = personalLoanService.getLastGeneratedReferenceNumberCVE(Constants.CVE_ID);
					
					appRefKey = SbiUtil.getApplicationReferenceId("2", "4", lastReferenceNumber);
					
					boolean isReferenceIdAvailable=false;
					isReferenceIdAvailable = personalLoanService.isReferenceIdAvailableCVE(appRefKey);
					
					logger.info("CVE isReferenceIdAvailable 1 "+isReferenceIdAvailable+ " appRefKey "+appRefKey+ " appSeqId "+appSeqId +" lastReferenceNumber "+lastReferenceNumber);
					if(!isReferenceIdAvailable){
						cveApp.setAppReferenceId(appRefKey);
					}else{
						lastReferenceNumber = appRefKey;
						appRefKey= SbiUtil.getApplicationReferenceId("2", "4", lastReferenceNumber);
						
						isReferenceIdAvailable = personalLoanService.isReferenceIdAvailable(appRefKey);
						logger.info("CVE isReferenceIdAvailable 2 "+isReferenceIdAvailable+ " appRefKey "+appRefKey+ " appSeqId "+appSeqId +" lastReferenceNumber "+lastReferenceNumber);
						if(!isReferenceIdAvailable){
							cveApp.setAppReferenceId(appRefKey);
						}else{
							lastReferenceNumber = appRefKey;
							appRefKey= SbiUtil.getApplicationReferenceId("2", "4", lastReferenceNumber);
							
							isReferenceIdAvailable = personalLoanService.isReferenceIdAvailableCVE(appRefKey);
							
							logger.info("CVE isReferenceIdAvailable 3 "+isReferenceIdAvailable+ " appRefKey "+appRefKey+ " appSeqId "+appSeqId +" lastReferenceNumber "+lastReferenceNumber);
							cveApp.setAppReferenceId(appRefKey);
						}
					}
					logger.info("CVE isReferenceIdAvailable 4 "+isReferenceIdAvailable+ " appRefKey "+appRefKey+ " appSeqId "+appSeqId +" lastReferenceNumber "+lastReferenceNumber);
					logger.info("CveProcessManagerImpl LNo: 483 : BEFORE SAVE appRefKey--"+appRefKey);
			     }
		  		
//				logger.info("CveProcessManagerImpl LNO :: 3443 BEFORE SAVE1 cveApp ::"+cveApp);	//Saved in cve table
				cveApp = personalLoanService.save(cveApp); // INSERT into CVE TABLE.			
//				logger.info("CveProcessManagerImpl LNO :: 3443 AFTER SAVE2 cveApp ::"+cveApp); 
//				logger.info("CveProcessManagerImpl LNo: 3842 : SAVE PERSONAL9--"+"cve--"+cve+"--PLtype--"+SessionUtil.getPersonalTypeId());					
		    	
		    	logger.info("CveProcessManagerImpl LNo: 3856 :: Insert into Personal table----AFTER----");
				logger.info("CveProcessManagerImpl LNo: 4024 ::BEFORE----"+cveApp.getAppSeqId());
				
				SessionUtil.setPersonalLoanTypeSequenceId(cveApp.getAppSeqId());
				logger.info("CveProcessManagerImpl LNo: 4026 ::AFTER-----"+cveApp.getAppSeqId());
				logger.info("CveProcessManagerImpl LNo: 4026 ::getPersonalLoanTypeSequenceId	-----"+SessionUtil.getPersonalLoanTypeSequenceId());
				
			} catch(SQLException e){
				logger.info("CveProcessManagerImpl LNo: 3098 :: exception caught " + e.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			} catch(Exception e){
				logger.info("CveProcessManagerImpl LNo: 3098 :: exception caught " + e.getMessage());
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			}
			
			cbsCallResponse.setAppSeqId(cveApp.getAppSeqId());
			
			logger.info("CveProcessManagerImpl LNo: 3594 cveApp.getAppSeqId::"+cveApp.getAppSeqId())	;
			masterCBSResponse.setCbsAppSeqId(cveApp.getAppSeqId());		
//			logger.info("CveProcessManagerImpl LNo: 3596 cveApp.getAppSeqId::"+cveApp.getAppSeqId()+"--CBSresSeqId--"+masterCBSResponse.getCbsAppSeqId());
			
//			logger.info("CveProcessManagerImpl LNo: 3599 masterCBSResponse::"+masterCBSResponse);
//			logger.info("CveProcessManagerImpl LNo: 3599 masterCBSResponse::"+masterCBSResponse.toString());
			masterCBSResponse = commonService.save(masterCBSResponse);
						
			 if(masterCBSResponse==null){
				logger.info("CveProcessManagerImpl LNo: 3107 ::");
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				cbsCallResponse.setStatus(0);
				return cbsCallResponse;
			} 
				
			if(ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())){
				masterCbsCall.setCbsEmailOtpCode(SbiUtil.getVerificationCodeForEmail(masterCBSResponse.getCbsEmail()));
//				logger.info("CveProcessManagerImpl.java :: LNo :: 4143  :: email otp"+masterCbsCall.getCbsEmailOtpCode());
			}
//			logger.info("Mobile number "+masterCbsCall.getCbsMobileNumber());
			
//			logger.info("mobile otp "+cveApp.getAppMobileVerificationCode());
			masterCbsCall.setCbsOtpCode(String.valueOf(cveApp.getAppMobileVerificationCode()));

			String msgBody=null;
			if (isDsrPage.equalsIgnoreCase("true")){
				msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS_CONSENT, 0);
				logger.info("CveProcessManagerImpl.java :: msgBody ::"+msgBody);
			}else{
				msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
				logger.info("CveProcessManagerImpl.java :: msgBody ::"+msgBody);
			}
			msgBody = SbiUtil.urlEncode(msgBody);
			String SMS_TEXT = null;
			if(masterCbsCall.getCbsIsdCode()==null){
				cbsCallResponse.setStatus(0);
				cbsCallResponse.setResponseMsg("Invalid ISD code");
				return cbsCallResponse;
			}
				
			if (masterCbsCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N")) {
				if(masterCbsCall.getCbsIsdCodeCve().intValue()==Integer.parseInt(Constants.COUNTRY_CODE_INDIA) ){
					SMS_TEXT=Constants.SMS_STRING_INDIAN;
				}else{
					SMS_TEXT=Constants.SMS_STRING_NRI;
				}
			} else {
				if(masterCbsCall.getCbsIsdCode().intValue()==Integer.parseInt(Constants.COUNTRY_CODE_INDIA) ){
					SMS_TEXT=Constants.SMS_STRING_INDIAN;
				}else{
					SMS_TEXT=Constants.SMS_STRING_NRI;
				}
			}		
		    SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", masterCbsCall.getCbsOtpCode().toString());				  
			if (masterCbsCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N")) {
				SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCbsCall.getCbsIsdCodeCve()+masterCbsCall.getCbsMobileNumber()).trim());
				
				if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
					logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCodeCve()+masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode());
				}
				
				if(!communicationManagerImpl.sendSms(SMS_TEXT)){
					cbsCallResponse.setStatus(0);
					cbsCallResponse.setResponseMsg("Mobile OTP service is down");
					return cbsCallResponse;
				}
			} else {
				SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCbsCall.getCbsIsdCode()+masterCbsCall.getCbsMobileNumber()).trim());
				
				if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
					logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCode()+masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode());
				}
				if(!communicationManagerImpl.sendSms(SMS_TEXT)){
					cbsCallResponse.setStatus(0);
					cbsCallResponse.setResponseMsg("Mobile OTP service is down");
					return cbsCallResponse;
				}
			} 
				
			  	SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCbsCall.getCbsIsdCode()+masterCbsCall.getCbsMobileNumber()).trim());	
			  
			  	if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
					logger.info("OTP for Mobile Number: " + (masterCbsCall.getCbsIsdCode()+masterCbsCall.getCbsMobileNumber()).trim() + " is " + masterCbsCall.getCbsOtpCode());
				}
				if(!communicationManagerImpl.sendSms(SMS_TEXT)){
					cbsCallResponse.setStatus(0);
					cbsCallResponse.setResponseMsg("Mobile OTP service is down");
					return cbsCallResponse;
				}
			
			if(masterCbsCall.getCbsIsdCode().intValue()!=Integer.parseInt(Constants.COUNTRY_CODE_INDIA)){
				if(ValidatorUtil.isValidEmail(masterCBSResponse.getCbsEmail())){
					msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_EMAIL, 0)) + Constants.THIRD_EMAIL_PART;
					if(msgBody!=null){
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCbsCall.getCbsEmailOtpCode());
						msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
						boolean sendStatus = false;
						String [] emailId={masterCBSResponse.getCbsEmail()};
//						logger.info("CveProcessManagerImpl LNo: 4006 :sendEmail--> emailId::: "+emailId);
						sendStatus = communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
						logger.info("CveProcessManagerImpl LNo: 4009 :sendEmail--> sendStatus::: "+sendStatus);
						if(!sendStatus){
							logger.info("CveProcessManagerImpl LNO :: 3187");
							cbsCallResponse.setResponseMsg("Email OTP service is down");
							cbsCallResponse.setStatus(0);
							return cbsCallResponse;
						}
					}
				}
			}
		
			masterCbsCall.setCbsOtpCount(String.valueOf(1));
			masterCbsCall = commonService.save(masterCbsCall);
			
			if(masterCbsCall==null){
				logger.info("CveProcessManagerImpl LNO :: 3207");
				cbsCallResponse.setStatus(0);
				cbsCallResponse.setResponseMsg("Sorry for the inconvenience");
				return cbsCallResponse;
			}
			if(masterCbsCall.getCbsMobileNumber().length() <= 8)
		    	  masterCbsCall.setCbsMobileNumberMask(masterCbsCall.getCbsMobileNumber().replaceAll("\\d(?=\\d{2})", "*"));
		      else
		    	  masterCbsCall.setCbsMobileNumberMask(masterCbsCall.getCbsMobileNumber().replaceAll("\\d(?=\\d{4})", "*"));
			cbsCallResponse.setResponseMsg("");
			if(isDsrPage.equalsIgnoreCase("true")){
				cbsCallResponse.setStatus(2);
			}else{
				cbsCallResponse.setStatus(1);
			}			
		  } 							 	
		} catch (SQLException e) {
			cbsCallResponse.setResponseMsg("System error occured.");
			cbsCallResponse.setStatus(0);
			logger.info("CveProcessManagerImpl LNo: 4558 :: exception caught " + e.getMessage());
		} catch (Exception e) {
			cbsCallResponse.setResponseMsg("System error occured.");
			cbsCallResponse.setStatus(0);
			logger.info("CveProcessManagerImpl LNo: 4558 :: exception caught " + e.getMessage());
		}
		return cbsCallResponse;
	}
	
	public JSONObject processCBSOTP(Integer moduleId, Integer stateId, 
			Integer bankLMSUserId, String ajaxPostUrl, int appOTPVerifyType,
			String inputOtp, String userEmail, Integer appSeqId, Integer cbsCallId) throws JSONException {
		JSONObject json = new JSONObject();
		logger.info("CveProcessManagerImpl LNo: 1029 ::inputOtp::"+inputOtp);
		logger.info("CveProcessManagerImpl LNo: 4622 ::appSeqId::"+appSeqId+"..cbsCallId.."+cbsCallId+"..ajaxPostUrl.."+ajaxPostUrl);
		
		  if(inputOtp !=null) {
		      	SbiUtil sbiutil=new SbiUtil();
		      	logger.info("DecryptedRequest inputOtp   1014  "+inputOtp);
		      	inputOtp=sbiutil.getDecryptedRequest(inputOtp);
			//	logger.info("DecryptedRequest inputOtp   1018  "+inputOtp);
		      }   
		//  logger.info("DecryptedRequest inputOtp   1043  "+inputOtp);
			
		if(cbsCallId==null){
			logger.info("CveProcessManagerImpl LNo: 3239 :: ");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
			json.put("alertCount", 0);
			return json;
		}
		
		MasterCBSCall masterCBSCall= commonService.getMasterCBSCallObjById(cbsCallId);
		ApplicationFormCveLoan cveForm = new ApplicationFormCveLoan();
		
		Integer appPLTypeId = Constants.APP_PL_TYPE_CVE;
		String cve = "cve";
		ajaxPostUrl = Constants.CVE_ACTION;
		
		if(masterCBSCall==null){
			logger.info("CveProcessManagerImpl LNo: 3249 :: ");
			json.put("status", "error");
			json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			json.put("alertCount", 0);
			return json;
		}
		if(masterCBSCall.getCbsMobileOtpVerified()!=null && "Y".equalsIgnoreCase(masterCBSCall.getCbsMobileOtpVerified())){
			json.put("status", "error");
			json.put("message", "Your mobile no. is already verified");
			json.put("alertCount", 5);
			return json;
		}

		Integer alertCount = Integer.parseInt(masterCBSCall.getCbsOtpCount());
		if(stateId == 28){
			try {
				logger.info("CveProcessManagerImpl processCBSOTP : stateId == 28 :: ");
				if(appSeqId!=null){
					ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					if(app == null){
						logger.info("CveProcessManagerImpl LNO 5293:: after save error");
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					if(!ValidatorUtil.isValid(app.getAppOTPAttemptCount())){
						app.setAppOTPAttemptCount(0);
					}
					if(app.getAppOTPAttemptCount()>Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT){
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG);
						return json;
					}
				}
				if(alertCount>=5){
					logger.info("CveProcessManagerImpl LNo: 3294 :: ");
					json.put("status", "error");
					json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
					json.put("alertCount", alertCount);
					return json;
				}else{
					if(appOTPVerifyType == 0){
						logger.info("CveProcessManagerImpl LNo: 4124 ::appOTPVerifyType----"+appOTPVerifyType);
						masterCBSCall.setCbsOtpCode(SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
					}else if(appOTPVerifyType == 1){
						logger.info("CveProcessManagerImpl LNo: 4127 ::appOTPVerifyType----"+appOTPVerifyType);
						masterCBSCall.setCbsEmailOtpCode(SbiUtil.getVerificationCodeForEmail(masterCBSCall.getCbsEmail()));
					}else{
						logger.info("CveProcessManagerImpl LNo: 4130 ::appOTPVerifyType----"+appOTPVerifyType);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						json.put("alertCount", alertCount);
						return json;
					}
					if(appOTPVerifyType == 0){
						logger.info("CveProcessManagerImpl LNo: 4137 ::appOTPVerifyType----"+appOTPVerifyType);
						String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
						msgBody = SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;
						if(Constants.COUNTRY_CODE_INDIA.equals(masterCBSCall.getCbsIsdCode()+"")){
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}else{
							SMS_TEXT=Constants.SMS_STRING_NRI;
						}
						SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim());
						
					  	if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode());
						}
					  	
						if(!communicationManagerImpl.sendSms(SMS_TEXT)){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							json.put("alertCount", alertCount);
							return json;
						}
					}else if(appOTPVerifyType == 1){
						logger.info("CveProcessManagerImpl LNo: 4164 ::appOTPVerifyType----"+appOTPVerifyType);
						if(masterCBSCall.getCbsIsdCode().intValue()!=Integer.parseInt(Constants.COUNTRY_CODE_INDIA)){
							if(ValidatorUtil.isValidEmail(masterCBSCall.getCbsEmail())){
								String msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_EMAIL, 0)) + Constants.THIRD_EMAIL_PART;
								msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
								msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
								msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
								msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
								msgBody = msgBody.replaceAll("\\[OTP_CODE\\]", masterCBSCall.getCbsEmailOtpCode());
								msgBody = msgBody.replaceAll("\\[PRODUCT_NAME\\]", Constants.PERSONAL_LOAN_PRODUCT_NAME);
								boolean sendStatus = false;
								String [] emailId={masterCBSCall.getCbsEmail()};
//								logger.info("CveProcessManagerImpl LNo: 5377 :sendEmail--> emailId::: "+emailId);
								
								sendStatus = communicationManagerImpl.sendEmail(emailId, msgBody, Constants.OTP_SUBJECT_LINE);
								logger.info("CveProcessManagerImpl LNo: 5380 :sendEmail--> sendStatus::: "+sendStatus);
								
								logger.info("CBS EMAIL msgbody of pl: "+msgBody+" with AppSeqId ::"+appSeqId);
								if(!sendStatus){
									logger.info("CveProcessManagerImpl LNo: 3362 :: ");
									json.put("status", "error");
									json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
									json.put("alertCount", alertCount);
									return json;
								}
							}
						}
					}
					alertCount++;
					masterCBSCall.setCbsOtpCount(alertCount.toString());
					masterCBSCall = commonService.save(masterCBSCall);
					if(masterCBSCall==null){
						logger.info("CveProcessManagerImpl LNo: 3374 :: ");
						json.put("status", "error");
						json.put("message", "Sorry for the inconvenience");
						return json;
					}
					json.put("status", "success");
					json.put("message", "OTP code sent");
					return json;
				}		
			} catch (SQLException e) {
				logger.info("CveProcessManagerImpl.java LNO 3397::" + e.getMessage());
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			} catch (Exception e) {
				logger.info("CveProcessManagerImpl.java LNO 3397::" + e.getMessage());
				json.put("status", "error");
				json.put("message", "Sorry for the inconvenience");
				return json;
			}
			
		}else if(stateId == 29){			
				
				if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && ((SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD)))
				{
					logger.info("CveProcessManagerImpl processCBSOTP : stateId == 29 :: ");
					try {
					
					if(appSeqId == null){
						logger.info("CveProcessManagerImpl.java LNO 3407::"+" with AppSeqId "+appSeqId);
						logger.info("CveProcessManagerImpl processCBSOTP : appSeqId == null ");
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
						return json;
					}
					if(!ValidatorUtil.isValid(inputOtp)){
						logger.info("CveProcessManagerImpl processCBSOTP : inputOtp :: ");
						json.put("status", "error");
						json.put("message", "Invalid OTP code");
						return json;
					}
					
					ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app.getAppSeqId());
					logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app);
					
					String refId = commonService.getCveReferenceIdBySeqId(appSeqId);
					
					ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);
					cveForm.setAppReferenceId(refId);
					
//					logger.info("CveProcessManagerImpl LNo : 4899 @@@@@@@@@@@@@::cveApp.getAppSeqId:::"+cveData); 
					
					logger.info("CveProcessManagerImpl LNo : 4277 app.getAppOTPAttemptCount() : "+app.getAppOTPAttemptCount()+" with AppSeqId "+appSeqId);
					if(!ValidatorUtil.isValid(app.getAppOTPAttemptCount())){	
						app.setAppOTPAttemptCount(0);
					}
					if(app.getAppOTPAttemptCount()>Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT){
		
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
						return json;
					}
					
					app.setAppOTPAttemptCount(app.getAppOTPAttemptCount()+1);
			
					logger.info("CveProcessManagerImpl LNo: 483 : SAVE PERSONAL10");
					
					app = personalLoanService.save(app);
					
					if(app==null){
				
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					  
					if(app.getAppDataSourceId()!=null && !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)){
			
						if(ValidatorUtil.isValidEmail(userEmail)){
							if(!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString()) && masterCBSCall.getCbsEmail()==null){
								app.setAppWorkEmail(userEmail);
				
							}else if(Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())){
								app.setAppWorkEmail(userEmail);
						
//								logger.info("Capture Email From User is ::"+userEmail+" with AppSeqId "+appSeqId);
							}
							SessionUtil.setEmail(userEmail);
						}
					}
					boolean isOPTVerified=false;
					if(appOTPVerifyType == 0){
						
						logger.info("CveProcessManagerImpl LNo: 4137 ::appOTPVerifyType----"+appOTPVerifyType);
						if(masterCBSCall.getCbsOtpCode()!=null){
							app.setAppMobileVerificationCode(Integer.parseInt(masterCBSCall.getCbsOtpCode()));
						}
					//	logger.info("Generated OTP  ::" + masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber());
					//	logger.info("Generated OTP  masterCBSCall.getCbsOtpCode() ::" +masterCBSCall.getCbsOtpCode() );
							if(masterCBSCall.getCbsOtpCode()!=null && masterCBSCall.getCbsOtpCode().equals(inputOtp)){
								app.setAppMobileVerified("Y");
								int otpResType = getOTPResidantVerifiedType(app);
								app.setAppResTypeAtVerified(otpResType);
								app.setAppMobileVerificationCodeReceived("Y");
								isOPTVerified = true;
								json.put("status", "success");
								json.put("message", "OTP authentication successful");
							//	logger.info("PLProcessImpl.java :: LNo :: 1619  :: OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim()+" with AppSeqId "+appSeqId);
							}else{
								logger.info("PersoanlProcessManager.java LNO 3455"+" with AppSeqId "+appSeqId);
								json.put("status", "error");
								json.put("message", "OTP authentication failed");
								app.setAppMobileVerified("N");
								return json;
							}
							//logger.info("CveProcessManagerImpl processCBSOTP : 5400 :: "+inputOtp);
					}else if(appOTPVerifyType == 1){
						
						logger.info("CveProcessManagerImpl LNo: 4137 ::appOTPVerifyType----"+appOTPVerifyType);
						
						if(masterCBSCall.getCbsEmailOtpCode()!=null){
							app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode());
						}
						if(masterCBSCall.getCbsEmailOtpCode()!=null && masterCBSCall.getCbsEmailOtpCode().equals(inputOtp)){
							masterCBSCall.setCbsEmailOtpVarified("Y");
							app.setAppEmailVerified("Y");
							app.setAppMobileVerificationCodeReceived("Y");
							
							isOPTVerified = true;
							json.put("status", "success");
							json.put("message", "OTP authentication successful");
							logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail()+" with AppSeqId "+appSeqId);
						}else{
							logger.info("PersoanlProcessManager.java LNO 3447::"+" with AppSeqId "+appSeqId);
							app.setAppEmailVerified("N");
				
							json.put("status", "error");
							json.put("message", "OTP authentication failed");
							return json;
						}
						logger.info("CveProcessManagerImpl processCBSOTP : 5420 :: "+inputOtp);
					}else{
						logger.info("PersoanlProcessManager.java LNO 3492");
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						app.setAppEmailVerified("N");
				
						return json;
					}
					
					app.setAppOTPVerifyType(appOTPVerifyType);
				
					logger.info("CveProcessManagerImpl LNo: 4335 ::appOTPVerifyType----"+appOTPVerifyType+"--"+app.getAppOTPVerifyType());
					
					if(isOPTVerified){
						StatusRequest statusRequest = new StatusRequest();
						statusRequest.setCurrentStatus(app.getAppLoanStatusId());
						statusRequest.setHaveLoanOffer(true);
						if(statusRequest.getLoanTypeId()==Constants.PERSONAL_LOAN_ID){
							statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
						}else{
							statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
						}
						statusRequest.setState(stateId);
						statusRequest.setBankLMSUserId(bankLMSUserId);
						statusRequest.setRsmDecision(0);
						statusRequest.setApplicationType(SessionUtil.getApplicationType()!=null?SessionUtil.getApplicationType():0);
						statusRequest.setApplicationLeadType(app.getAppDataSourceId());
						statusRequest.setApplicationSubTypeId(app.getAppSubTypeId());
						
						logger.info("CveProcessManagerImpl LNo: 4984 statusRequest.getApplicationLeadType::"+statusRequest.getApplicationLeadType()+"---"+app.getAppDataSourceId());
						
						StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
						
						 if(statusManagerResponse.getStatus()!=0){
							app.setAppLoanStatusId(statusManagerResponse.getStatus());
					
						} else if(app.getAppLoanStatusId() == 0 ){
				
							logger.info("CveProcessManagerImpl.java LNO 3515"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							return json;
						} 
						
						if(statusManagerResponse.isEligibleToInsertLog()){
							personalLoanHelper.insertCallLog(app.getAppSeqId(),bankLMSUserId, statusManagerResponse.getStatusCallLogs(), null, null, true);
						
						}
						logger.info("CveProcessManagerImpl LNo: 483 : SAVE PERSONAL11");
						app = personalLoanService.save(app);
						
						if(app==null){
							logger.info("CveProcessManagerImpl.java LNO 3525");
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							return json;
						}
					}else{
						logger.info("OTP is authentication failed::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", "OTP is incorrect! Try again.");
						return json;
					}
					
					masterCBSCall = commonService.save(masterCBSCall);				 		
					String CbsMobileNumber = masterCBSCall.getCbsMobileNumber();
									
					logger.info("CveProcessManagerImpl.java CVE DATA sent to CRM LNO 5501 appPLTypeId:: "+appPLTypeId+"..ajaxPostUrl.."+ajaxPostUrl+"..CbsMobileNumber.."+CbsMobileNumber);
					logger.info("CveProcessManagerImpl.java CVE DATA sent to CRM LNO 5502 and stopping PERSONAL,GOLD,PENSION Ocas leads to CRM in this flow.....");	
					logger.info("CveProcessManagerImpl.java CVE DATA sent to CRM LNO 5503 PL:: "+Constants.APP_PL_TYPE_PERSONAL+"..PN.."+Constants.APP_PL_TYPE_PENSION+"..GL.."+Constants.APP_PL_TYPE_GOLD);
					
				if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N")) && ((SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD))) 
				{		
				logger.info("CveProcessManagerImpl.java CVE DATA sent to CRM LNO 5506 and stopping PERSONAL,GOLD,PENSION Ocas leads to CRM in this flow....."); 
						
					cveForm.setCbsAccountNumber(masterCBSCall.getCbsAccountNumber());
					cveForm.setCbsMobileNumber(masterCBSCall.getCbsMobileNumber());
					cveForm.setAppISDCode(masterCBSCall.getCbsIsdCode().toString());  
					cveForm.setCveProductCategory(masterCBSCall.getCveProductCategory());
					cveForm.setCveSalutation(masterCBSCall.getCveSalutation());
					cveForm.setCveAppFirstName(masterCBSCall.getCveAppFirstName());
					cveForm.setCveAppMiddleName(masterCBSCall.getCveAppMiddleName());
					cveForm.setCveAppLastName(masterCBSCall.getCveAppLastName());
					
					if(masterCBSCall.getCbsEmail()!=null) {
						cveForm.setCveAppEmail(masterCBSCall.getCbsEmail());
					}
					else {
						cveForm.setCveAppEmail(userEmail);
					}
						
					cveForm.setCveAppConsentRevokeYes(masterCBSCall.getCveAppConsentRevokeYes());							
					logger.info("CveProcessManagerImpl LNO :: 4629 CVETABLE SAVE getAppReferenceId::::"+cveForm.getAppReferenceId()+"--appRefKey--"+appRefKey);
					
					if(cveForm.getCreatedDate()==null){
						cveForm.setCreatedDate(new Date());
					}
					cveForm.setAppLeadUpdateTime(new Date());				
									
					cveForm.setCveAppPrevBranchId(masterCBSCall.getLoanQuoteWorkBranchId());
					logger.info("CveProcessManagerImpl  4644 @@@@@@ cveForm.getPreferredBranch:: "+cveForm.getCveAppPrevBranchId());			
					
					logger.info("CveProcessManagerImpl  4642 @@@@@@ branchCode---"+branchCode);
					
					cveForm.setCbsCifNumber(masterCBSCall.getCveCifNumber());
					
//					 logger.info("CveProcessManagerImpl  4632 @@@@@@ cveForm.getPreferredBranch:: "+cveForm.getCveAppPrevBranchId());						
//										
//					logger.info("CveProcessManagerImpl LNO :: 4636 CVETABLE SAVE getCbsAccountNumber :: "+cveForm.getCbsAccountNumber());
//					logger.info("CveProcessManagerImpl LNO :: 4637 CVETABLE SAVE getCbsMobileNumber :: "+cveForm.getCbsMobileNumber());
//					logger.info("CveProcessManagerImpl LNO :: 4637 CVETABLE SAVE getAppISDCode :: "+cveForm.getAppISDCode());
//					logger.info("CveProcessManagerImpl LNO :: 4638 CVETABLE SAVE getCveAppFirstName :: "+cveForm.getCveAppFirstName());
//					logger.info("CveProcessManagerImpl LNO :: 4639 CVETABLE SAVE getCveAppMiddleName :: "+cveForm.getCveAppMiddleName());
//					logger.info("CveProcessManagerImpl LNO :: 4640 CVETABLE SAVE getCveAppEmail :: "+cveForm.getCveAppEmail());
//					logger.info("CveProcessManagerImpl LNO :: 4641 CVETABLE SAVE getCveProductCategory :: "+cveForm.getCveProductCategory()); 
//					logger.info("CveProcessManagerImpl LNO :: 4642 CVETABLE SAVE getCveAppPrevBranchId :: "+cveForm.getCveAppPrevBranchId()); 
//					logger.info("CveProcessManagerImpl LNO :: 4643 CVETABLE SAVE getCbsCifNumber :: "+cveForm.getCbsCifNumber());
//					logger.info("CveProcessManagerImpl LNO :: 4644 CVETABLE SAVE getCveAppConsentRevokeYes :: "+cveForm.getCveAppConsentRevokeYes());
//					logger.info("CveProcessManagerImpl LNO :: 4645 CVETABLE SAVE getAppReferenceId :: "+cveForm.getAppReferenceId());   
//					logger.info("CveProcessManagerImpl LNO :: 4646 CVETABLE SAVE appRefKey :: "+appRefKey);
//					logger.info("CveProcessManagerImpl LNO :: 4647 CVETABLE SAVE appRefKey :: "+appRefKey);	
//					
//					logger.info("CveProcessManagerImpl LNo: 465555555 :: cveForm::::" + cveForm); 		
				
				if(app!=null) {
					if(!Constants.CRM_BYPASS) {
							logger.info("CveProcessManagerImpl LNo:3335 :: BEFORE callCrmCve ");							
							CRMRequest crmRequest = new CRMRequest();
							CRMResponse crmResponse = new CRMResponse();
							String message = "FAIL";
							
							crmRequest.setCrmLeadId(app.getAppCRMLeadId());						
							crmRequest.setApplicantReferenceId(cveForm.getAppReferenceId());	
							crmRequest.setReferenceNumber(app.getAppSeqId());
							crmRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
							
							logger.info("CveProcessManagerImpl LNo:4313 :: getAppCRMLeadId NOT-NULL getAppCRMLeadId "+crmRequest.getCrmLeadId());
							logger.info("CveProcessManagerImpl LNo:4314 :: getAppCRMLeadId NOT-NULL getAppReferenceId "+crmRequest.getApplicantReferenceId());
							logger.info("CveProcessManagerImpl LNo:4315 :: getAppCRMLeadId NOT-NULL getAppSeqId "+crmRequest.getReferenceNumber());			
//							
//							logger.info("CveProcessManagerImpl LNo:4441 :: "+masterCBSCall.getCbsAccountNumber());
//							logger.info("CveProcessManagerImpl LNo:4442 :: "+masterCBSCall.getCbsMobileNumber());
//							logger.info("CveProcessManagerImpl LNo:4443 :: "+masterCBSCall.getCveProductCategory());
//							logger.info("CveProcessManagerImpl LNo:4444 :: "+masterCBSCall.getCveSalutation());
//							logger.info("CveProcessManagerImpl LNo:4445 :: "+masterCBSCall.getCveAppFirstName());
//							logger.info("CveProcessManagerImpl LNo:4446 :: "+masterCBSCall.getCveAppMiddleName());					
//							logger.info("CveProcessManagerImpl LNo:4448 :: "+masterCBSCall.getCveAppLastName());
//							logger.info("CveProcessManagerImpl LNo:4450 :: "+masterCBSCall.getCbsEmail());	    //Fetch from OTP existing screen so that's why added here
//							logger.info("CveProcessManagerImpl LNo:4451 :: "+masterCBSCall.getLoanQuoteWorkBranchId());	
//							
//							logger.info("CveProcessManagerImpl Calling processCbsCall() "+masterCBSCall.toString());
//							logger.info("CveProcessManagerImpl LNo:5010 :: "+masterCBSCall.getFIRSTNAME()+"**FIRSTNAME**"+FIRSTNAME);
//							logger.info("CveProcessManagerImpl LNo:5010 :: "+masterCBSCall.getMIDDLENAME()+"**MIDDLENAME**"+MIDDLENAME);
//							logger.info("CveProcessManagerImpl LNo:5010 :: "+masterCBSCall.getLASTNAME()+"**LASTNAME**"+LASTNAME);
//							logger.info("CveProcessManagerImpl LNo:5010 :: "+masterCBSCall.getEMAIL());
//							
							ApplicationFormCaseCve applicationFormCaseCve = new ApplicationFormCaseCve();					
						if(masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("N"))	{
							logger.info("CveProcessManagerImpl LNo:5079 :: BEFORE AFTER getAppCRMLeadId "+app.getAppCRMLeadId());
							logger.info("CveProcessManagerImpl LNo:5080 :: BEFORE AFTER getAppReferenceId "+app.getAppReferenceId());
							logger.info("CveProcessManagerImpl LNo:5081 :: BEFORE AFTER getAppSeqId "+app.getAppSeqId());
							
							logger.info("CveProcessManagerImpl LNo:5083 :: getCveAppConsentRevoke NO FLAG and OCAS Case create in CRM Service ");
												
							callCrmCveCase(app,masterCBSCall,cveForm);
							
							logger.info("CveProcessManagerImpl LNo:5098 :: crmResponse for CASE creation is SUCCESS then calling getCaseResponse ( )");
							
							Date caseDate = cveForm.getAppLeadUpdateTime();
							logger.info("CveProcessManagerImpl LNo:5010 :: "+caseDate);
							logger.info("CveProcessManagerImpl LNo:5011 :: "+caseDate.toString());
							
						String caseResponseCve = crmServiceNew.getCaseResponse();
						logger.info("CveProcessManagerImpl LNo:5092 :: crmResponse for CASE creation::"+crmResponse.toString()+"==caseResponseCve=="+caseResponseCve);
						
						applicationFormCaseCve.setResponseStatus(caseResponseCve);
						crmResponse.getObjectKey();
						logger.info("CveProcessManagerImpl LNo:5112 :: crmResponse for CASE creation::"+applicationFormCaseCve.getResponseStatus());					
						
						if(message.equalsIgnoreCase(caseResponseCve)) {
							logger.info("CveProcessManagerImpl :: crmResponse for CASE creation is FALSE then skipping Lead CREATION API for CVE consent");
							 json.put("status", "error");
							 json.put("message", "Dear Customer, due to technical issue, we could not capture your interest for the selected product. Please click <a href='javascript:loanFirstPage();'>here</a> to start again");
							 json.put("caseResponseCve", "FAIL");
							 return json; 		
							
						} else if (!message.equalsIgnoreCase(caseResponseCve)) {
							callCrmCve(app,masterCBSCall,cveForm);     								
							
						   //Showing message on Thank You screen after verifying OTP and getting SUCCESS response from CRM
							json.put("status", "success");
							
							if (masterCBSCall.getCbsMobileNumber()!=null && (CbsMobileNumber.equalsIgnoreCase(masterCBSCall.getCbsMobileNumber())))
							  {
								String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 1);
								msgBody = SbiUtil.urlEncode(msgBody);
								String SMS_TEXT = null;
																	
								SMS_TEXT=Constants.SMS_STRING_INDIAN;
								
								SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
								SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
								SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsMobileNumber()).trim());
								
							  	if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
									logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode());
								}
							  	
								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
									logger.info("CveProcessManagerImpl LNO ::5539 , OTP service is down:: msg not send");
									json.put("status", "error");
									json.put("message", "sms service is down");
									return json;
								}
							} 
							return json;
						} 						
					  } 
					}										
			      }		
		     } 
				
			else if(ajaxPostUrl!=null && (ajaxPostUrl.equalsIgnoreCase(cve)) && (masterCBSCall.getCveAppConsentRevokeYes().equalsIgnoreCase("Y")) && ((SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PERSONAL) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_PENSION) && (SessionUtil.getPersonalTypeId() != Constants.APP_PL_TYPE_GOLD)))
			    {	
					logger.info("CveProcessManagerImpl.java SENDING CVE Consent REVOKE SMS to CUSTOMER after verifying OTP.............");	
					String message = "FAIL";
					ApplicationFormCaseCve applicationFormCaseCve = new ApplicationFormCaseCve();
					
					cveForm.setCbsAccountNumber(masterCBSCall.getCbsAccountNumber());
					cveForm.setCbsMobileNumber(masterCBSCall.getCbsMobileNumber());
					cveForm.setCbsCifNumber(masterCBSCall.getCveCifNumber());
					if(cveForm.getCreatedDate()==null){
						cveForm.setCreatedDate(new Date());
					}
					cveForm.setAppLeadUpdateTime(new Date());
					
//					logger.info("CveProcessManagerImpl getCbsAccountNumber:: "+cveForm.getCbsAccountNumber());
//					logger.info("CveProcessManagerImpl getCbsMobileNumber:: "+cveForm.getCbsMobileNumber());
//					logger.info("CveProcessManagerImpl getCbsCifNumber:: "+cveForm.getCbsCifNumber());
//					logger.info("CveProcessManagerImpl getCreatedDate:: "+cveForm.getCreatedDate());
//					logger.info("CveProcessManagerImpl getAppLeadUpdateTime:: "+cveForm.getAppLeadUpdateTime());
					
					callCrmCveCase(app,masterCBSCall,cveForm);
					
					CRMResponse crmResponse = new CRMResponse();
					String successRevoke = "SUCCESS";
					String duplicateMessageRevoke = "Duplicacy";
					
					Date caseDate = cveForm.getAppLeadUpdateTime();
					logger.info("CveProcessManagerImpl LNo:5076 :: "+caseDate);
					logger.info("CveProcessManagerImpl LNo:5077 :: "+caseDate.toString());	
					
					 String caseResponseCve = crmServiceNew.getCaseResponse();
					logger.info("CveProcessManagerImpl LNo:5092 :: crmResponse for CASE creation::"+crmResponse.getMessage()+"======"+crmResponse.toString()+"==caseResponseCve=="+caseResponseCve);
					
					applicationFormCaseCve.setResponseStatus(caseResponseCve);
					crmResponse.getObjectKey();
					logger.info("CveProcessManagerImpl LNo:5112 :: crmResponse for CASE creation::"+crmResponse.getObjectKey()+"------"+applicationFormCaseCve.getResponseStatus());											
					
					applicationFormCaseCve.setResponseStatus(caseResponseCve);
					crmResponse.getObjectKey();
					logger.info("CveProcessManagerImpl LNo:5112 :: crmResponse for CASE creation::"+crmResponse.getObjectKey()+"------"+applicationFormCaseCve.getResponseStatus());
					
					String representativeMode = crmServiceNew.getRepresentativeMode();
					logger.info("CveProcessManagerImpl LNo:5112 ::representativeMode::"+representativeMode);
					
					String schedeledDate = crmServiceNew.getSchedeledDate();
					logger.info("CveProcessManagerImpl LNo:5112 ::schedeledDate::"+schedeledDate);
					
					String schedeledTime = crmServiceNew.getSchedeledTime();
					logger.info("CveProcessManagerImpl LNo:5112 ::schedeledTime::"+schedeledTime);
					
					if(message.equalsIgnoreCase(caseResponseCve)) {
						logger.info("CveProcessManagerImpl :: crmResponse for CASE creation is FALSE then skipping Lead CREATION API for CVE consent REVOCATION");
						 json.put("status", "error");				
						 json.put("message", "Dear Customer, due to technical issue, we could not capture your interest for the selected product. Please click <a href='javascript:loanFirstPage();'>here</a> to start again");
						 json.put("caseResponseCve", "FAIL");
						 return json; 
					} else if(successRevoke.equalsIgnoreCase(caseResponseCve)) {
						logger.info("CveProcessManagerImpl :: crmResponse for CASE creation is SUCCESS then skipping Lead CREATION API for CVE consent REVOCATION");
						 json.put("status", "success");				
						 json.put("message", "Dear Customer, Your revocation request has been successfully registered.");
						
						 if (masterCBSCall.getCbsMobileNumber()!=null && (CbsMobileNumber.equalsIgnoreCase(masterCBSCall.getCbsMobileNumber())))
						  {
//							logger.info("CveProcessManagerImpl LNO ::5551 INSIDE MSG for SUCCESS CVE SMS_CONSENT "+CbsMobileNumber+"..getCbsMobileNumber.."+masterCBSCall.getCbsMobileNumber());
							logger.info("CveProcessManagerImpl LNO ::5552 INSIDE MSG for SUCCESS CVE SMS_CONSENT "+Constants.MESSAGE_TYPE_SMS);
							String msgBody=communicationManagerImpl.setEmailBody(24, 0, Constants.MESSAGE_TYPE_SMS, 0);	   	//Edit parameters for NEW SMS template
							msgBody = SbiUtil.urlEncode(msgBody);
							String SMS_TEXT = null;
																
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
							
							SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
							SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsMobileNumber()).trim());
							
							if(!communicationManagerImpl.sendSms(SMS_TEXT)){
								logger.info("CveProcessManagerImpl LNO ::5565 , OTP service is down:: msg not send");
								json.put("status", "error");
								json.put("message", "sms service is down");
								return json;
							}
						}
						 return json; 
					} else if(duplicateMessageRevoke.equalsIgnoreCase(caseResponseCve)) {
						logger.info("CveProcessManagerImpl :: crmResponse for CASE creation is Duplicacy then skipping Lead CREATION API for CVE consent REVOCATION");
						 json.put("status", "duplicacy");				
						 json.put("message", "Dear Customer, Consent Revocation already taken via "+representativeMode+" on "+schedeledDate);
						
						 if (masterCBSCall.getCbsMobileNumber()!=null && (CbsMobileNumber.equalsIgnoreCase(masterCBSCall.getCbsMobileNumber())))
						  {
							String msgBody=communicationManagerImpl.setEmailBody(24, 1, Constants.MESSAGE_TYPE_SMS, 0);	   	//Edit parameters for NEW SMS template
							msgBody = SbiUtil.urlEncode(msgBody);
							String SMS_TEXT = null;
																
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
							
							SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
							SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsMobileNumber()).trim());
							
							SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_MODE", representativeMode);
							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", schedeledDate);
							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", schedeledTime);
							
							logger.info("CveProcessManagerImpl LNO ::1762 :: Duplicacy message has been sent to user "+SMS_TEXT);
							if(!communicationManagerImpl.sendSms(SMS_TEXT)){
								logger.info("CveProcessManagerImpl LNO ::5565 , OTP service is down:: msg not send");
								json.put("status", "error");
								json.put("message", "sms service is down");
								return json;
							}
						}		 
						 return json; 
				 }	
			}				
				return json;			
			} catch (SQLException e) {
				logger.info("CveProcessManagerImpl.java LNO 3546::" + e.getMessage());
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				return json;
			} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 3546::" + e.getMessage());
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}						
			 } else {
					logger.info("CveProcessManagerImpl LNo : 4751 EXCEPT CVE BLOCK-------");
					try {
						logger.info("CveProcessManagerImpl processCBSOTP : stateId == 29 :: ");
						if(appSeqId == null){
							logger.info("CveProcessManagerImpl.java LNO 3407::"+" with AppSeqId "+appSeqId);
							logger.info("CveProcessManagerImpl processCBSOTP : appSeqId == null ");
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
							return json;
						}
						if(!ValidatorUtil.isValid(inputOtp)){
							logger.info("CveProcessManagerImpl processCBSOTP : inputOtp :: ");
							json.put("status", "success");
							json.put("message", "Invalid OTP code");
							return json;
						}
					
					ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					
			        if (app == null) {
			          logger.info("CveProcessManagerImpl.java LNO 3416:: with AppSeqId " + appSeqId);
			          json.put("status", "error");
			          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			          return json;
			        } 
			        
			        logger.info("CveProcessManagerImpl LNo : 4277 app.getAppOTPAttemptCount() : " + app.getAppOTPAttemptCount() + " with AppSeqId " + appSeqId);
			        if (!ValidatorUtil.isValid(app.getAppOTPAttemptCount()))
			          app.setAppOTPAttemptCount(Integer.valueOf(0)); 
			        if (app.getAppOTPAttemptCount().intValue() > Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT.intValue()) {
			          json.put("status", "error");
			          json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
			          return json;
			        } 
			        app.setAppOTPAttemptCount(Integer.valueOf(app.getAppOTPAttemptCount().intValue() + 1));
			        app = this.personalLoanService.save(app);
			        if (app == null) {
			          logger.info("CveProcessManagerImpl.java LNO 4286 error on saving:: with AppSeqId " + appSeqId);
			          json.put("status", "error");
			          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			          return json;
			        } 
			        if (app.getAppDataSourceId() != null && !app.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP) && 
			          ValidatorUtil.isValidEmail(userEmail)) {
			          if (!Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString()) && masterCBSCall.getCbsEmail() == null) {
			            app.setAppWorkEmail(userEmail);
			          } else if (Constants.COUNTRY_CODE_INDIA.equalsIgnoreCase(masterCBSCall.getCbsIsdCode().toString())) {
			            app.setAppWorkEmail(userEmail);
			            logger.info("Capture Email From User is ::" + userEmail + " with AppSeqId " + appSeqId);
			          } 
			          SessionUtil.setEmail(userEmail);
			        } 
			        boolean isOPTVerified = false;
			        if (appOTPVerifyType == 0) {
			          if (masterCBSCall.getCbsOtpCode() != null)
			            app.setAppMobileVerificationCode(Integer.valueOf(Integer.parseInt(masterCBSCall.getCbsOtpCode()))); 
			          logger.info("Generated OTP  ::" + masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber());
			          if (masterCBSCall.getCbsOtpCode() != null && masterCBSCall.getCbsOtpCode().equals(inputOtp)) {
			            app.setAppMobileVerified("Y");
			            int otpResType = getOTPResidantVerifiedType(app);
			            app.setAppResTypeAtVerified(Integer.valueOf(otpResType));
			            app.setAppMobileVerificationCodeReceived("Y");
			            isOPTVerified = true;
			            json.put("status", "success");
			            json.put("message", "OTP authentication successful");
			         //   logger.info("PLProcessImpl.java :: LNo :: 1619  :: OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode() + masterCBSCall.getCbsMobileNumber()).trim() + " with AppSeqId " + appSeqId);
			          } else {
			            logger.info("CveProcessManagerImpl.java LNO 3455 with AppSeqId " + appSeqId);
			            json.put("status", "error");
			            json.put("message", "OTP authentication failed");
			            app.setAppMobileVerified("N");
			            return json;
			          } 
			        } else if (appOTPVerifyType == 1) {
			          if (masterCBSCall.getCbsEmailOtpCode() != null)
			            app.setAppEmailVerificationCode(masterCBSCall.getCbsEmailOtpCode()); 
			          if (masterCBSCall.getCbsEmailOtpCode() != null && masterCBSCall.getCbsEmailOtpCode().equals(inputOtp)) {
			            masterCBSCall.setCbsEmailOtpVarified("Y");
			            app.setAppEmailVerified("Y");
			            app.setAppMobileVerificationCodeReceived("Y");
			            isOPTVerified = true;
			            json.put("status", "success");
			            json.put("message", "OTP authentication successful");
			         //   logger.info("OTP verfied for email ::" + masterCBSCall.getCbsEmail() + " with AppSeqId " + appSeqId);
			          } else {
			            logger.info("CveProcessManagerImpl.java LNO 3447:: with AppSeqId " + appSeqId);
			            app.setAppEmailVerified("N");
			            json.put("status", "error");
			            json.put("message", "OTP authentication failed");
			            return json;
			          } 
			        } else {
			         // logger.info("CveProcessManagerImpl.java LNO 3492");
			          json.put("status", "error");
			          json.put("message", "OTP authentication failed");
			          app.setAppEmailVerified("N");
			          return json;
			        } 
			        app.setAppOTPVerifyType(appOTPVerifyType);
			        if (isOPTVerified) {
			          StatusRequest statusRequest = new StatusRequest();
			          statusRequest.setCurrentStatus(app.getAppLoanStatusId().intValue());
			          statusRequest.setHaveLoanOffer(true);
			          if (statusRequest.getLoanTypeId() == Constants.PERSONAL_LOAN_ID.intValue()) {
			            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
			          } else {
			            statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID.intValue());
			          } 
			          statusRequest.setState(stateId.intValue());
			          statusRequest.setBankLMSUserId(bankLMSUserId.intValue());
			          statusRequest.setRsmDecision(0);
			          statusRequest.setApplicationType((SessionUtil.getApplicationType() != null) ? SessionUtil.getApplicationType().intValue() : 0);
			          statusRequest.setApplicationLeadType(app.getAppDataSourceId().intValue());
			          statusRequest.setApplicationSubTypeId(app.getAppSubTypeId().intValue());
			          StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
			          if (statusManagerResponse.getStatus() != 0) {
			            app.setAppLoanStatusId(Integer.valueOf(statusManagerResponse.getStatus()));
			          } else if (app.getAppLoanStatusId().intValue() == 0) {
			            //logger.info("CveProcessManagerImpl.java LNO 3515 with AppSeqId " + appSeqId);
			            json.put("status", "error");
			            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			            return json;
			          } 
			          if (statusManagerResponse.isEligibleToInsertLog())
			            this.personalLoanHelper.insertCallLog(app.getAppSeqId(), bankLMSUserId.intValue(), statusManagerResponse.getStatusCallLogs(), null, null, true); 
			          app = this.personalLoanService.save(app);
			          if (app == null) {
			            logger.info("CveProcessManagerImpl.java LNO 3525");
			            json.put("status", "error");
			            json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			            return json;
			          } 
			        } else {
			          logger.info("OTP is authentication failed:: with AppSeqId " + appSeqId);
			          json.put("status", "error");
			          json.put("message", "OTP is incorrect! Try again.");
			          return json;
			        } 
			        masterCBSCall = this.commonService.save(masterCBSCall);
			        if (masterCBSCall == null) {
			         // logger.info("CveProcessManagerImpl.java LNO 3539 with AppSeqId " + appSeqId);
			          json.put("status", "error");
			          json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
			          return json;
			        }
					return json;	
					
				} catch (SQLException e) {
					logger.info("CveProcessManagerImpl.java LNO 3546::" + e.getMessage());
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 3546::" + e.getMessage());
					json.put("status", "error");
					json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
					return json;
				}
				}	
		     }
		return json;
	}
	
	private void callCrmCve(ApplicationFormPersonalLoan appFormData, MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
	      CRMRequest crmRequest = prepareCrmRequestCve(appFormData, masterCBSCall, cveForm);
	      logger.info("CveProcessManagerImpl :: LNO 5573 :: Beofoe preparing CRM Request Object with AppSeqId " + appFormData.getAppSeqId());
	      this.crmServiceNew.pushLeadToCRMcve(crmRequest);
	    
	  }
		
	private CRMRequest prepareCrmRequestCve(ApplicationFormPersonalLoan appFormData, MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
	    CRMRequest crmRequest = new CRMRequest();
	   // logger.info("CveProcessManagerImpl :: LNO 5866 :: prepareCrmRequestCve::cveForm::::" + cveForm);
	    if (cveForm == null) {
	      logger.info("CveProcessManagerImpl :: LNO 5587 :: cveForm is null, returning back");
	      return null;
	    } 
	    masterCBSCall.getCbsAccountNumber();
	    //logger.info("CveProcessManagerImpl masterCBSCall.getCbsAccountNumber():: " + masterCBSCall.getCbsAccountNumber());
	    crmRequest.setMobileNumber(masterCBSCall.getCbsMobileNumber());
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCbsMobileNumber::" + masterCBSCall.getCbsMobileNumber());
	    crmRequest.setFirstName(masterCBSCall.getCveAppFirstName());
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCveAppFirstName()::" + masterCBSCall.getCveAppFirstName());
	    crmRequest.setMiddleName(masterCBSCall.getCveAppMiddleName());
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCveAppMiddleName::" + masterCBSCall.getCveAppMiddleName());
	    crmRequest.setLastName(masterCBSCall.getCveAppLastName());
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCveAppLastName::" + masterCBSCall.getCveAppLastName());
	    //logger.info("CveProcessManagerImpl crmRequest cveForm.getCveAppPrevBranchId::" + cveForm.getCveAppPrevBranchId());
	    MasterBranch masterBranch = this.commonService.getBranchById(cveForm.getCveAppPrevBranchId());
	    if (masterBranch != null && masterBranch.getBranchCode() != null) {
	      int branchCodeLength = masterBranch.getBranchCode().length();
	      String branchCode = "";
	      if (branchCodeLength > 0 && branchCodeLength == 1) {
	        branchCode = "0000" + masterBranch.getBranchCode();
	      } else if (branchCodeLength == 2) {
	        branchCode = "000" + masterBranch.getBranchCode();
	      } else if (branchCodeLength == 3) {
	        branchCode = "00" + masterBranch.getBranchCode();
	      } else if (branchCodeLength == 4) {
	        branchCode = "0" + masterBranch.getBranchCode();
	      } else if (branchCodeLength > 4) {
	        branchCode = masterBranch.getBranchCode();
	      } 
	      crmRequest.setPreferredBranch(branchCode);
	      logger.info(
	          "CveProcessManagerImpl LNO :: 6466 list branchCode " + branchCode);
	    } 
	    if (cveForm.getCveAppEmail() != null)
	      crmRequest.setEmailId(cveForm.getCveAppEmail()); 
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCbsEmail::" + cveForm.getCveAppEmail() + "--CRM-EMAIL--" + crmRequest.getEmailId());
	    
	   	MasterCveProduct cveProduct = cveLoanService.getCveProductByCode(masterCBSCall.getCveProductCategory());
	    crmRequest.setProductKey(cveProduct.getCveProductCrmCode());
	    crmRequest.setProductCategory(cveProduct.getCveProductCategory());
	    
	    crmRequest.setCveSalutation(masterCBSCall.getCveSalutation());
	    //logger.info("CveProcessManagerImpl masterCBSCall.getCveSalutation::" + masterCBSCall.getCveSalutation());
	    crmRequest.setCveAppConsentRevoke(masterCBSCall.getCveAppConsentRevokeYes());
	   // logger.info("CveProcessManagerImpl masterCBSCall.getCveAppConsentRevokeYes::" + masterCBSCall.getCveAppConsentRevokeYes());
	    crmRequest.setApplicantReferenceId(cveForm.getAppReferenceId());
	    //logger.info("CveProcessManagerImpl getAppReferenceId::crmRequest " + crmRequest.getApplicantReferenceId());
	    crmRequest.setReferenceNumber(appFormData.getAppSeqId());
	    //logger.info("CveProcessManagerImpl getAppSeqId::getAppSeqId::::" + appFormData.getAppSeqId());
	   // logger.info("CveProcessManagerImpl crmRequest.getCveCifNumber:: " + masterCBSCall.getCveCifNumber());
	    if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() > 11) {
	      crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber().substring(6));
	     // logger.info("CveProcessManagerImpl IF CASE getCIFNumber1:: " + masterCBSCall.getCveCifNumber());
	      //logger.info("CveProcessManagerImpl IF CASE getCIFNumber2:: " + crmRequest.getCIFNumber());
	    } else if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() == 11) {
	      crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber());
	     // logger.info("CveProcessManagerImpl ELSE CASE getCIFNumber:: " + crmRequest.getCIFNumber());
	      
				try {
					Optional<MarTech> marTechDeatails = Optional.ofNullable(marTechDao
							.getDetailsByVisitId(personalLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));
	
				//	logger.info("CveProcessManagerImpl.java :: LNo :: 2013 ::marTechDetails is:::" + marTechDeatails);
					if (marTechDeatails.isPresent()) {
						MarTech martech = marTechDeatails.get();
						crmRequest.setCampaignCode(martech.getCampaignCode());
						crmRequest.setOfferCode(martech.getOfferCode());
						crmRequest.setTrackingCode(martech.getTrackingCode());
						logger.info("CveProcessManagerImpl.java :: LNo :: 2019 ::campaignCode is:::"
								+ crmRequest.getCampaignCode());
						logger.info(
								"CveProcessManagerImpl.java :: LNo :: 2022 ::offerCode is:::" + crmRequest.getOfferCode());
						logger.info("CveProcessManagerImpl.java :: LNo :: 2023 ::trackingCode is:::"
								+ crmRequest.getTrackingCode());
					}
					logger.info("CveProcessManagerImpl.java :: LNo :: 2026 ::trackingCode is:::"
							+ crmRequest.getTrackingCode());
				} catch (SQLException e) {
					logger.info("CveProcessManagerImpl.java LNO 2029 ::" + e.getMessage());
				}
			}
	    return crmRequest;
	}	
		
		public JSONObject verifySMSOTP(Integer moduleId, Integer stateId, 
				Integer bankLMSUserId, String ajaxPostUrl, String isDsrPage, OtherRequest otherRequest) throws JSONException { 
			
			JSONObject json = new JSONObject();
			int appOTPVerifyType=0;
			if(otherRequest.getAppOTPVerifyType()!=null){
				appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim());
				logger.info("CveProcessManagerImpl LNO :: 5104 appOTPVerifyType----"+appOTPVerifyType);
			}
			
			if(SessionUtil.getPlTypeCbsCallId()==null){
				logger.info("CveProcessManagerImpl LNO :: 5239");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
				json.put("alertCount", 0);
				return json;
			}
			
			MasterCBSCall masterCBSCall= commonService.getMasterCBSCallObjById(SessionUtil.getPlTypeCbsCallId());
			
			if(masterCBSCall==null){
				logger.info("CveProcessManagerImpl LNO :: 5249");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			
			if(masterCBSCall.getCbsOtpCount()==null){
				logger.info("CveProcessManagerImpl LNO :: 5257");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			
			if(masterCBSCall.getCbsIsdCode()==null){
				logger.info("CveProcessManagerImpl LNO :: 5268");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			if(masterCBSCall.getCbsMobileNumber()==null){
				logger.info("CveProcessManagerImpl LNO :: 5277");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			Integer alertCount = Integer.parseInt(masterCBSCall.getCbsOtpCount());
			if(stateId == 33){
				try {
					Integer appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
					ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					if(app == null){
						logger.info("CveProcessManagerImpl.java LNO 5293:: after save error"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					if(app.getAppOTPAttemptCount()>Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT){
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_AFTER_ATTEMPT_MSG);
						return json;
					}
					if(alertCount>=5){
						logger.info("CveProcessManagerImpl LNO :: 5290"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
						json.put("alertCount", alertCount);
						return json;
					}else{
						if(appOTPVerifyType == 0){
							masterCBSCall.setCbsOtpCode(SbiUtil.getVerificationCode(masterCBSCall.getCbsMobileNumber()).toString());
							logger.info("CveProcessManagerImpl LNO :: 5301 ::  mobile number ::"+masterCBSCall.getCbsMobileNumber()+" with AppSeqId "+appSeqId);
							logger.info("CveProcessManagerImpl LNO :: 5302 :: mobile OTP ::"+masterCBSCall.getCbsOtpCode()+" with AppSeqId "+appSeqId);
						}else{
							logger.info("CveProcessManagerImpl LNO :: 5304");
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							json.put("alertCount", alertCount);
							return json;
						}

						boolean sendSMSConsent= false;
						if(isDsrPage.equalsIgnoreCase("true")){
							boolean bankLmsUserRoleExceptContactCenter = commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
							logger.info("CveProcessManagerImpl.java LNo: 5215 :: isDsrPage--bankLMSUserId--"+bankLmsUserRoleExceptContactCenter+"--bankLMSUserId--"+bankLMSUserId);
							if(bankLmsUserRoleExceptContactCenter){
								logger.info("CveProcessManagerImpl LNO :: 5321 :: Not contact Center user"+" with AppSeqId "+appSeqId);
								sendSMSConsent=true;
							}else{
								logger.info("CveProcessManagerImpl LNO :: 5327 ::  contact Center user"+" with AppSeqId "+appSeqId);
							}
						}

						String msgBody=null;
						if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent){
							msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS_CONSENT, 0);
						}else{
							msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
						}
						msgBody = SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;
						if(Constants.COUNTRY_CODE_INDIA.equals(masterCBSCall.getCbsIsdCode().toString())){
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}else{
							SMS_TEXT=Constants.SMS_STRING_NRI;
						}
						SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", masterCBSCall.getCbsOtpCode().toString());
						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim());
						
					  	if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							logger.info("OTP for Mobile Number: " + (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim() + " is " + masterCBSCall.getCbsOtpCode());
						}
					  	
						if(!communicationManagerImpl.sendSms(SMS_TEXT)){
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							json.put("alertCount", alertCount);
							return json;
						}
						
						alertCount++;
						masterCBSCall.setCbsOtpCount(alertCount.toString());
						masterCBSCall = commonService.save(masterCBSCall);
						if(masterCBSCall==null){
							logger.info("CveProcessManagerImpl LNO :: 5365 :: Sorry for the inconvenience"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", "Sorry for the inconvenience");
							return json;
						}
						if(app!=null){
							app.setAppMobileVerificationCode(Integer.parseInt(masterCBSCall.getCbsOtpCode()));
							app.setAppMobileVerified("N");
						}
						app = personalLoanService.save(app);
						if(app==null){
							logger.info("CveProcessManagerImpl LNO :: 5378 ::  Sorry for the inconvenience"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", "Sorry for the inconvenience");
							return json;
						}
						json.put("status", "success");
						json.put("message", "OTP code sent");
						return json;
					}		
				} catch (JSONException e) {
					logger.info("CveProcessManagerImpl.java LNO 5413 ::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 5413 ::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
				
			}else if(stateId == 34){
				try {
					Integer appSeqId=SessionUtil.getPersonalLoanTypeSequenceId();
					if(appSeqId == null){
						logger.info("CveProcessManagerImpl.java LNO 5429::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					
					ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					if(app == null){
						logger.info("CveProcessManagerImpl.java LNO 5438:: exception caught"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					logger.info("CveProcessManagerImpl.java LNo : 5443 app.getAppOTPAttemptCount() : "+app.getAppOTPAttemptCount()+" with AppSeqId "+appSeqId);
					if(!ValidatorUtil.isValid(app.getAppOTPAttemptCount())){
						app.setAppOTPAttemptCount(0);
					}
					if(app.getAppOTPAttemptCount()>Constants.APP_MAXIMUM_OTP_ATTEMPT_COUNT){
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_OTP_ATTEMPT_MSG);
						return json;
					}
					app.setAppOTPAttemptCount(app.getAppOTPAttemptCount()+1);
					logger.info("CveProcessManagerImpl.java LNo: 483 : SAVE PERSONAL12");
					app = personalLoanService.save(app);
					if(app == null){
						logger.info("CveProcessManagerImpl.java LNO 5455:: after save error"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					boolean isOPTVerified=false;
					
					if(appOTPVerifyType == 0){
						if(app.getAppMobileVerified().equals("Y")){
							isOPTVerified = true;
							json.put("status", "success");
							json.put("message", "OTP authentication successful");
							logger.info("OTP verfied for mobileNo ::" + (masterCBSCall.getCbsIsdCode()+masterCBSCall.getCbsMobileNumber()).trim()+" with AppSeqId "+appSeqId);
						}else{
							logger.info("CveProcessManagerImpl.java LNO 5460::");
							json.put("status", "error");
							json.put("message", "OTP authentication failed");
							return json;
						}
					}else{
						logger.info("CveProcessManagerImpl.java LNO 5467::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}
					
					if(isOPTVerified){
						StatusRequest statusRequest = new StatusRequest();
						statusRequest.setCurrentStatus(app.getAppLoanStatusId());
						statusRequest.setHaveLoanOffer(true);
						if(statusRequest.getLoanTypeId()==Constants.PERSONAL_LOAN_ID){
							statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
						} else {
							statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
						}
						statusRequest.setState(stateId);
						statusRequest.setBankLMSUserId(bankLMSUserId);
						statusRequest.setRsmDecision(0);
						statusRequest.setApplicationType(SessionUtil.getApplicationType());
						statusRequest.setApplicationLeadType(app.getAppDataSourceId());
						statusRequest.setApplicationSubTypeId(app.getAppSubTypeId());
						statusRequest.setAppMobileVerified(app.getAppMobileVerified());
						StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
						if(statusManagerResponse.getStatus()!=0){
							app.setAppLoanStatusId(statusManagerResponse.getStatus());
						} else if(app.getAppLoanStatusId() == 0 ){
							logger.info("CveProcessManagerImpl.java LNO 5493::"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							return json;
						}
						if(statusManagerResponse.isEligibleToInsertLog()){
							personalLoanHelper.insertCallLog(app.getAppSeqId(),bankLMSUserId, statusManagerResponse.getStatusCallLogs(), null, null, true);
						}
						logger.info("CveProcessManagerImpl.java LNo: 483 : SAVE PERSONAL13");
						app = personalLoanService.save(app);
						if(app==null){
							logger.info("CveProcessManagerImpl.java LNO 5503 ::"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							return json;
						}
					}else{
						logger.info("CveProcessManagerImpl.java LNO 5509  :: OTP is authentication failed::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", "OTP is incorrect! Try again.");
						return json;
					}
					
					masterCBSCall = commonService.save(masterCBSCall);
					if(masterCBSCall==null){
						logger.info("CveProcessManagerImpl.java LNO 5517::");
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					return json;
				} catch (SQLException e) {
					logger.info("CveProcessManagerImpl.java LNO 5524::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 5524::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
			}

			return json;
		}
			
		 private void callCrmCveCase(ApplicationFormPersonalLoan appFormData, MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
				CRMRequest crmRequest = prepareCrmRequestCveCase(appFormData, masterCBSCall,cveForm);		
				logger.info("CveProcessManagerImpl.java :: LNO 5573 :: Beofoe preparing CRM Request Object"+" with AppSeqId "+appFormData.getAppSeqId());
	
			    crmServiceNew.pushCaseLeadToCRMcve(crmRequest); 
		}
				
				
		private CRMRequest prepareCrmRequestCveCase (ApplicationFormPersonalLoan appFormData , MasterCBSCall masterCBSCall, ApplicationFormCveLoan cveForm) {
			CRMRequest crmRequest = new CRMRequest();
			//logger.info("CveProcessManagerImpl.java :: LNO 5866 :: prepareCrmRequestCve::cveForm::::"+cveForm);
					
			if(cveForm == null){
				logger.info("CveProcessManagerImpl.java :: LNO 5587 :: cveForm is null, returning back");
				return null;
			}
			
//			logger.info("CveProcessManagerImpl.java LNo:6134 :: "+masterCBSCall.getFIRSTNAME()+"**FIRSTNAME**"+FIRSTNAME);
//			logger.info("CveProcessManagerImpl.java LNo:6135 :: "+masterCBSCall.getMIDDLENAME()+"**MIDDLENAME**"+MIDDLENAME);
//			logger.info("CveProcessManagerImpl.java LNo:6136 :: "+masterCBSCall.getLASTNAME()+"**LASTNAME**"+LASTNAME);
			
			if (FIRSTNAME == null) {
				crmRequest.setFirstName(" ");
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getFirstName::"+crmRequest.getFirstName());
			} else {
				crmRequest.setFirstName(FIRSTNAME);
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getFirstName::"+crmRequest.getFirstName());
			}
	      	
			if (MIDDLENAME == null) {
				crmRequest.setMiddleName(" ");
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getMiddleName::"+crmRequest.getMiddleName());
			} else {
				crmRequest.setMiddleName(MIDDLENAME);
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getMiddleName::"+crmRequest.getMiddleName());
			}
	      //	logger.info("CveProcessManagerImpl.JAVA crmRequest.getMiddleName::"+crmRequest.getMiddleName()+"**MIDDLENAME**"+MIDDLENAME);

	      	if (LASTNAME == null) {
				crmRequest.setLastName(" ");
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getLastName::"+crmRequest.getLastName());
			} else {
				crmRequest.setLastName(LASTNAME);
				//logger.info("CveProcessManagerImpl.JAVA crmRequest.getLastName::"+crmRequest.getLastName());
			}
	      	//logger.info("CveProcessManagerImpl.JAVA crmRequest.getLastName::"+crmRequest.getLastName()+"**LASTNAME**"+LASTNAME);
	      	
			//logger.info("CveProcessManagerImpl.java crmRequest.getCveCifNumber:: "+ masterCBSCall.getCveCifNumber());		    	    
		    if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() > 11) {
				crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber().substring(6));
				//logger.info("CveProcessManagerImpl.java IF CASE getCIFNumber1:: "+ masterCBSCall.getCveCifNumber());
				//logger.info("CveProcessManagerImpl.java IF CASE getCIFNumber2:: "+ crmRequest.getCIFNumber());
			} else if (masterCBSCall.getCveCifNumber() != null && masterCBSCall.getCveCifNumber().length() == 11) {
				crmRequest.setCIFNumber(masterCBSCall.getCveCifNumber());
				//logger.info("CveProcessManagerImpl.java ELSE CASE getCIFNumber:: "+ crmRequest.getCIFNumber());
			}
				
		    crmRequest.setMobileNumber(masterCBSCall.getCbsMobileNumber());
	      	//logger.info("CveProcessManagerImpl.JAVA masterCBSCall.getCbsMobileNumber::"+masterCBSCall.getCbsMobileNumber());	
			
	      	if(cveForm.getCveAppEmail()!=null) {
				crmRequest.setEmailId(cveForm.getCveAppEmail());
			//	logger.info("CveProcessManagerImpl.JAVA masterCBSCall.getCbsEmail::"+cveForm.getCveAppEmail()+"--CRM-EMAIL--"+crmRequest.getEmailId());
			}
			
	      	crmRequest.setCveAppConsentRevoke(masterCBSCall.getCveAppConsentRevokeYes());
		//	logger.info("CveProcessManagerImpl.JAVA masterCBSCall.getCveAppConsentRevokeYes::"+masterCBSCall.getCveAppConsentRevokeYes());
			
			crmRequest.setCaseDate(DateUtil.convertDateToFormattedType(cveForm.getAppLeadUpdateTime(), "dd/MM/yyyy"));
			logger.info("CveProcessManagerImpl.JAVA cveForm.getAppLeadUpdateTime::"+crmRequest.getCaseDate()+"------"+cveForm.getAppLeadUpdateTime());		
			
			crmRequest.setAccountNo(masterCBSCall.getCbsAccountNumber());
	      	// logger.info("CveProcessManagerImpl.JAVA masterCBSCall.getCbsAccountNumber():: "+masterCBSCall.getCbsAccountNumber());
	      	 
	     	crmRequest.setApplicantReferenceId(cveForm.getAppReferenceId());
			logger.info("CveProcessManagerImpl.JAVA getAppReferenceId::crmRequest "+crmRequest.getApplicantReferenceId());
			
			crmRequest.setReferenceNumber(cveForm.getAppSeqId());         // Set Sequence ID for CVE     
	      	logger.info("CveProcessManagerImpl.JAVA getAppSeqId::"+cveForm.getAppSeqId());
			
	      	crmRequest.setDateTimeComplaint(DateUtil.convertDateToFormattedType(cveForm.getAppLeadUpdateTime(), "dd/MM/yyyy HH:mm:ss"));
			logger.info("CveProcessManagerImpl.JAVA cveForm.getDateTimeComplaint::"+crmRequest.getDateTimeComplaint()+"--getAppLeadUpdateTime--"+cveForm.getAppLeadUpdateTime());
			
			crmRequest.setConsentRevoke(cveForm.getCveAppConsentRevokeYes());
			logger.info("CveProcessManagerImpl.JAVA getConsentRevoke::"+crmRequest.getConsentRevoke());
			
			try {
				Optional<MarTech> marTechDeatails = Optional.ofNullable(marTechDao
						.getDetailsByVisitId(personalLoanService.getVisitByAppSeqId(appFormData.getAppSeqId())));

				logger.info("CveProcessManagerImpl.java :: LNo :: 2417 ::marTechDetails is:::" + marTechDeatails);
				if (marTechDeatails.isPresent()) {
					MarTech martech = marTechDeatails.get();
					crmRequest.setCampaignCode(martech.getCampaignCode());
					crmRequest.setOfferCode(martech.getOfferCode());
					crmRequest.setTrackingCode(martech.getTrackingCode());
					logger.info("CveProcessManagerImpl.java :: LNo :: 2423 ::campaignCode is:::"
							+ crmRequest.getCampaignCode());
					logger.info(
							"CveProcessManagerImpl.java :: LNo :: 2426 ::offerCode is:::" + crmRequest.getOfferCode());
					logger.info("CveProcessManagerImpl.java :: LNo :: 2427 ::trackingCode is:::"
							+ crmRequest.getTrackingCode());
				}
				logger.info("CveProcessManagerImpl.java :: LNo :: 2430 ::trackingCode is:::"
						+ crmRequest.getTrackingCode());
			} catch (SQLException e) {
				logger.info("CveProcessManagerImpl.java LNO 2433 ::" + e.getMessage());
			}
				    
			 return crmRequest;
		   } 
		
			
		private static int getOTPResidantVerifiedType(ApplicationFormPersonalLoan appForm)
		{
			//logger.info("CveProcessManagerImpl.java  LNO 6628 :: getOTPResidantVerifiedType ( ) ");
			int flag=0;
			if (appForm.getAppISDCode()!=null && "91".equals(appForm.getAppISDCode())) {
				flag =1;
				//logger.info("CveProcessManagerImpl.java  LNO 6628 :: getOTPResidantVerifiedType:::"+flag+"***"+appForm.getAppISDCode());
			}else{
				flag =2;
			//	logger.info("CveProcessManagerImpl.java  LNO 6628 :: getOTPResidantVerifiedType:::"+flag+"***"+appForm.getAppISDCode());
			}
			return flag;
		}


		public JSONObject verifyConcentOtp(Integer moduleId, Integer stateId, 
				Integer bankLMSUserId, String ajaxPostUrl,String isDsrPage, OtherRequest otherRequest) throws JSONException, SQLException { 

			JSONObject json = new JSONObject();
			int appOTPVerifyType=0;
			if(otherRequest.getAppOTPVerifyType()!=null){
				appOTPVerifyType = Integer.parseInt(otherRequest.getAppOTPVerifyType().trim());
			}
			if(SessionUtil.getPersonalLoanApplicationSequenceId()==null){
				logger.info("CveProcessManagerImpl LNO :: 3803");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE_SESSION_TIMEDOUT);
				json.put("alertCount", 0);
				return json;
			}
			ApplicationFormPersonalLoan	appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(SessionUtil.getPersonalLoanApplicationSequenceId());
			if(appForm==null){
				logger.info("CveProcessManagerImpl LNO :: 3981"+" with AppSeqId is null");
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			if(appForm.getAppOTPAttemptCount()==null){
				logger.info("CveProcessManagerImpl LNO :: 3821"+" with AppSeqId "+appForm.getAppSeqId());
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			if(appForm.getAppISDCode()==null){
				logger.info("CveProcessManagerImpl LNO :: 3832"+" with AppSeqId "+appForm.getAppSeqId());
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			if(appForm.getAppMobileNo()==null){
				logger.info("CveProcessManagerImpl LNO :: 3841"+" with AppSeqId "+appForm.getAppSeqId());
				json.put("status", "error");
				json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
				json.put("alertCount", 0);
				return json;
			}
			Integer alertCount = appForm.getAppOTPAttemptCount();
			if(stateId == 41){
				try {
					if(alertCount>=5){
						logger.info("CveProcessManagerImpl LNO :: 3854"+" with AppSeqId "+appForm.getAppSeqId());
						json.put("status", "error");
						json.put("message", Constants.APP_MAXIMUM_RESEND_OTP_REQUEST_MSG);
						json.put("alertCount", alertCount);
						return json;
					}else{
						if(appOTPVerifyType == 0){
							appForm.setAppMobileVerificationCode(SbiUtil.getVerificationCode(appForm.getAppMobileNo()));
							logger.info("mobile OTP ::"+appForm.getAppMobileVerificationCode()+" with AppSeqId "+appForm.getAppSeqId());
						}else{
							logger.info("CveProcessManagerImpl LNO :: 3864"+" with AppSeqId "+appForm.getAppSeqId());
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							json.put("alertCount", alertCount);
							return json;
						}
						
						boolean sendSMSConsent= false;
						if(isDsrPage.equalsIgnoreCase("true")){
							boolean bankLmsUserRoleExceptContactCenter = commonService.getBankLmsUserRoleExceptContactCenter(bankLMSUserId);
							if(bankLmsUserRoleExceptContactCenter){
								logger.info("Not contact Center user"+" with AppSeqId "+appForm.getAppSeqId());
								sendSMSConsent=true;
							}else{
								logger.info("contact Center user"+" with AppSeqId "+appForm.getAppSeqId());
							}
						}

						String msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
						if (isDsrPage.equalsIgnoreCase("true") && sendSMSConsent){
							msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS_CONSENT, 0);
						}else{
							msgBody=communicationManagerImpl.setEmailBody(0, 0, Constants.MESSAGE_TYPE_SMS, 0);
						}
						msgBody = SbiUtil.urlEncode(msgBody);
						String SMS_TEXT = null;
						if(Constants.COUNTRY_CODE_INDIA.equals(appForm.getAppISDCode().toString())){
							SMS_TEXT=Constants.SMS_STRING_INDIAN;
						}else{
							SMS_TEXT=Constants.SMS_STRING_NRI;
						}
						SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
						SMS_TEXT=SMS_TEXT.replaceAll("OTP_CODE", appForm.getAppMobileVerificationCode().toString());
						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim());
						
					  	if (Constants.DEPLOYMENT_MODE.equals("UAT")) {
							logger.info("OTP for Mobile Number: " + (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim() + " is " + appForm.getAppMobileVerificationCode());
						}
					  	
						if(!communicationManagerImpl.sendSms(SMS_TEXT)){
							json.put("status", "error");
							json.put("message", "OTP service is down");
							json.put("alertCount", alertCount);
							return json;
						}
							
						alertCount++;
						appForm.setAppOTPAttemptCount(alertCount);
						logger.info("CveProcessManagerImpl.java LNo: 483 : SAVE PERSONAL15");
						appForm = personalLoanService.save(appForm);
						if(appForm==null){
							logger.info("CveProcessManagerImpl LNO :: 4523 Sorry for the inconvenience");
							json.put("status", "error");
							json.put("message", "Sorry for the inconvenience");
							return json;
						}
						appForm = personalLoanService.save(appForm);
						if(appForm==null){
							logger.info("CveProcessManagerImpl LNO :: 4535 Sorry for the inconvenience");
							json.put("status", "error");
							json.put("message", "Sorry for the inconvenience");
							return json;
						}

						json.put("status", "success");
						json.put("message", "OTP code sent");
						return json;
					}
				} catch (JSONException e) {
					logger.info("CveProcessManagerImpl.java LNO 3923::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 3923::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}

			}else if(stateId == 42){
				try {
					Integer appSeqId = SessionUtil.getPersonalLoanApplicationSequenceId();
					if(appSeqId == null){
						logger.info("CveProcessManagerImpl.java LNO 3933::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}

					boolean isOPTVerified=true;

					if(appOTPVerifyType == 0){
						if(appForm.getAppMobileVerified().equals("Y")){
							ApplicationFormLead lead=commonService.getLeadById(SessionUtil.getLeadId());
							lead.setLeadMobileVerificationCodeVerified("Y");
							lead = commonService.save(lead);
							isOPTVerified = true;
							json.put("status", "success");
							json.put("message", "OTP authentication successful");
							logger.info("OTP verfied for mobileNo ::" + (appForm.getAppISDCode()+appForm.getAppMobileNo()).trim()+" with AppSeqId "+appSeqId);
						}else{
							logger.info("CveProcessManagerImpl.java LNO 3964::"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", "OTP authentication failed");
							return json;
						}
					}else{
						logger.info("CveProcessManagerImpl.java LNO 3971::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", "OTP authentication failed");
						return json;
					}

					if(isOPTVerified){
						StatusRequest statusRequest = new StatusRequest();
						statusRequest.setCurrentStatus(appForm.getAppLoanStatusId());
						statusRequest.setHaveLoanOffer(true);
						statusRequest.setLoanTypeId(Constants.PERSONAL_LOAN_ID);
						statusRequest.setState(stateId);
						statusRequest.setBankLMSUserId(bankLMSUserId);
						statusRequest.setRsmDecision(0);
						statusRequest.setApplicationType(SessionUtil.getApplicationType());
						statusRequest.setApplicationLeadType(appForm.getAppDataSourceId());
						statusRequest.setApplicationSubTypeId(appForm.getAppSubTypeId());
						statusRequest.setAppMobileVerified(appForm.getAppMobileVerified());
						StatusManagerResponse statusManagerResponse = StatusManager.getStatus(statusRequest);
						if(statusManagerResponse.getStatus()!=0){
							appForm.setAppLoanStatusId(Constants.CALL_LOGS_MESSAGE_STATE171_ID);
						} else if(appForm.getAppLoanStatusId() == 0 ){
							logger.info("CveProcessManagerImpl.java LNO 3993::"+" with AppSeqId "+appSeqId);
							json.put("status", "error");
							json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
							return json;
						}
						personalLoanHelper.insertCallLog(appForm.getAppSeqId(),bankLMSUserId, Constants.CALL_LOGS_MESSAGE_STATE171_ID, null, null, true);
					}else{
						logger.info("OTP is authentication failed::");
						json.put("status", "error");
						json.put("message", "OTP is incorrect! Try again.");
						return json;
					}

					appForm = personalLoanService.save(appForm);
					if(appForm==null){
						logger.info("CveProcessManagerImpl.java LNO 4017::"+" with AppSeqId "+appSeqId);
						json.put("status", "error");
						json.put("message", Constants.SORRY_FOR_INCONVENIENCE);
						return json;
					}
					return json;
				} catch (SQLException e) {
					logger.info("CveProcessManagerImpl.java LNO 4042::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				} catch (Exception e) {
					logger.info("CveProcessManagerImpl.java LNO 4042::" + e.getMessage());
					json.put("status", "error");
					json.put("message", "Sorry for the inconvenience");
					return json;
				}
			  }
			return json;
		  }	
		
		  public void getCIFNUMBER(String cifnumber) {
		//	logger.info("CveProcessManagerImpl.java LNO cifnumber::"+cifnumber);
		    getcif = cifnumber;
			logger.info("CveProcessManagerImpl.java LNO getcif::"+getcif);	
		  }  
		  
		  public String setUtmParam(String utm) {
				//logger.info("CveProcessManagerImpl.java LNO utm::"+utm);
				return utm;
			}	  
		  
		  public String getFailCaseResponse() {
				logger.info("CALLING getCaseResponse()");
				String msg = null;
				msg = caseMessage;
				return msg;
			}
      }
