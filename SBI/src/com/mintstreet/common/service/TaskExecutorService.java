package com.mintstreet.common.service;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import com.mintstreet.common.bo.impl.CommunicationManagerImpl;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.BankLmsUser;
import com.mintstreet.common.entity.MasterLoanPurpose;
import com.mintstreet.common.entity.MasterSalesTeam;
import com.mintstreet.common.util.AESEncryption;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.CurrencyUtil;
import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.EncryptDecryptUtil;
import com.mintstreet.common.util.GeneratePDF;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoanQuote;
import com.mintstreet.loan.agriloan.service.AgriLoanService;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.autoloan.entity.MasterCarCompany;
import com.mintstreet.loan.autoloan.entity.MasterCarModel;
import com.mintstreet.loan.autoloan.service.AutoLoanService;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.educationloan.service.EducationLoanService;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.homeloan.service.HomeLoanService;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.loan.personal.service.PersonalLoanService;
import com.mintstreet.loan.product.entity.AlProduct;
import com.mintstreet.loan.product.entity.HlProduct;
import com.mintstreet.loan.product.entity.MasterElProduct;
import com.mintstreet.loan.product.entity.MasterPlProduct;

import freemarker.template.utility.StringUtil;

public class TaskExecutorService {
	private static final Logger logger = LogManager.getLogger(TaskExecutorService.class.getName());

	@Autowired
	private TaskExecutor taskExecutor;

	@Autowired
	private GeneratePDF generatePDF;

	@Autowired
	private HomeLoanService homeLoanService;
	
	@Autowired
	private AutoLoanService autoLoanService;
	
	@Autowired
	private AgriLoanService agriLoanService;
	
	@Autowired
	private PersonalLoanService personalLoanService;
	
	@Autowired
	private EducationLoanService educationLoanService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CommunicationManagerImpl communicationManagerImpl;
	
	@Autowired
	private SbiUtil SbiUtil;
		
	
	@Autowired
	private AESEncryption aesEncryption;
	
	private class GeneratePDFForHomeLoan implements Runnable {
  		private ApplicationFormHomeLoan appHL;
  		private ApplicationFormHomeLoanQuote quoteHL;
  		
	    public GeneratePDFForHomeLoan(ApplicationFormHomeLoan appHL, ApplicationFormHomeLoanQuote quoteHL) {
	    	this.appHL=appHL;
	    	this.quoteHL=quoteHL;
	    }

		public void run() {
			try {
				if(appHL!=null && quoteHL!=null){
					String urlParameters = generatePDF.getPdfGenerationData(appHL, quoteHL);
					if (urlParameters != null) {
						urlParameters = "data="+ urlParameters.replaceAll("\"", "\\\"");
						JSONObject json = generatePDF.generatePDF(urlParameters, Constants.HOME_LOAN_ID);
						if (json != null) {
							if (json.getString("status").equalsIgnoreCase("success")) {
								appHL.setAppDownloadPdfFileName(json.get("fileName").toString());
								appHL = homeLoanService.save(appHL);
							}
						}
					}
				}
			} catch (JSONException e) {
				logger.info("TaskExecutorService.java LNo : 116 : Exception caught in GeneratePDFForHomeLoan() ",e);
			} catch (Exception e) {
				logger.info("TaskExecutorService.java LNo : 116 : Exception caught in GeneratePDFForHomeLoan() ",e);
			}
		}
	}

  	private class SendingSMSForHomeLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormHomeLoan appHL;
  		private Integer bankLMSUserId;
  		private boolean isAbleToSendBMOrSalesTeamMessage;
	    public SendingSMSForHomeLoan(Integer requestIndex, Integer havePan, ApplicationFormHomeLoan appHL, 
	    		Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appHL=appHL;
	    	this.bankLMSUserId=bankLMSUserId;
	    	this.isAbleToSendBMOrSalesTeamMessage=isAbleToSendBMOrSalesTeamMessage;
	    }
	    public void run() {
	    	try{

	    		//logger.info("TaskExecutorService.java LNo : 138 : MESSAGE SENDING TO CUSTOMER HL.");
	    		if(appHL!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(Constants.HOME_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
	    				
	    				//ISD code condition added for international SMS seva issue
	    				if(appHL.getAppApplyingFrom()==2 && (appHL.getAppISDCode() != null && !appHL.getAppISDCode().equals(Constants.COUNTRY_CODE_INDIA))){
	    					SMS_TEXT=Constants.SMS_STRING_NRI;
	    				}else{
	    					SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    				}
	    				SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    				if(requestIndex.equals(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE)){
	    					//logger.info("TaskExecutorService.java LNo : 151 : MESSAGE SENDING TO CUSTOMER HL :: Doc scheduled.");
	    					if(appHL.getAppISDCode()!=null && appHL.getAppMobileNo()!=null && appHL.getAppFirstName()!=null && appHL.getAppLastName()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appHL.getAppISDCode() + appHL.getAppMobileNo()).trim());
    							SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appHL.getAppFirstName()+(appHL.getAppMiddleName()!=null?" "+appHL.getAppMiddleName():"")+" "+(appHL.getAppLastName()))));
    							if(appHL.getAppDocPickupDateDT()!=null && appHL.getAppDocPickupTimeString()!=null){
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", SbiUtil.urlEncode(DateUtil.convertDateToFormattedType(appHL.getAppDocPickupDateDT(),"dd-MM-yyyy")));
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", SbiUtil.urlEncode(SbiUtil.getTimeInterval(appHL.getAppDocPickupTimeString())));
    							
    								if(bankLMSUserId!=null){
    									BankLmsUser bankLmsUser = commonService.getBankLmsUserById(bankLMSUserId);
    									if(bankLmsUser!=null && bankLmsUser.getLmsFirstName()!=null && bankLmsUser.getLmsLastName()!=null){
    										SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_NAME", SbiUtil.urlEncode(String.valueOf(bankLmsUser.getLmsFirstName()+" "+(bankLmsUser.getLmsLastName()))));
    									}
    									//logger.info("SMS_TEXT_DOCUMENT_SCHEDULE==============================="+SMS_TEXT);
    								}
    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    									logger.info("TaskExecutorService.java LNo : 167 : OTP Service is down HL.");
    								}
    							}
	    					}
	    				}else{
	    					logger.info("TaskExecutorService.java LNo : 172 : MESSAGE SENDING TO CUSTOMER HL:: AIP Approval.");
	    					if(appHL.getAppISDCode()!=null && appHL.getAppMobileNo()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appHL.getAppISDCode() + appHL.getAppMobileNo()).trim());
	    						if(appHL.getAppLoanAmount()!=null){
	    							SMS_TEXT=SMS_TEXT.replaceAll("ELIGIBILITY_AMT", String.valueOf(new Double(appHL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR).intValue()));
	    							if(appHL.getAppHomeLoanId() != null && appHL.getAppLoanTenure() != null){
	    								HlProduct product =  commonService.getHomeLoanProductById(appHL.getAppHomeLoanId());
	    								if(product!=null && product.getHlProductSliderTenure()!=null ){
	    									if(product.getHlProductSliderTenure()==1){
	    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appHL.getAppLoanTenure()));
	    									}else if(product.getHlProductSliderTenure()==2){
	    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appHL.getAppLoanTenure()*12));
	    									}
	    									String ir ="";
	    									if(appHL.getAppLoanInterestRateDiscount() != null && appHL.getAppLoanInterestRateDiscount() != 0){
	    										if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
	    											ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppBhlInterestRate()))+"%25+%2F+"+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
	    										}else{
	    											ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRateDiscount()));
	    										}
	    									}else if(appHL.getAppLoanInterestRate() != null && appHL.getAppLoanInterestRate() != 0){
	    										if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
	    											ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppBhlInterestRate()))+"%25+%2F+"+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
	    										}else{
	    											ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
	    										}
	    									}
	    									
	    									SMS_TEXT=SMS_TEXT.replaceAll("IR_AMT", ir);
	    									String stremi="--";
											if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
												if(appHL.getAppFlexiPayDetails()!=null ){
													try{
														String[] emiMessage = appHL.getAppFlexiPayDetails().split("\\|");
														String tenure1Duration = emiMessage[0].split("=")[0];
														double tenure1Emi = Double.parseDouble(emiMessage[0].split("=")[1]);
														
														String tenure2Duration = emiMessage[1].split("=")[0];
														double tenure2Emi = Double.parseDouble(emiMessage[1].split("=")[1]);
														
														String tenure3Duration = emiMessage[2].split("=")[0];
														double tenure3Emi = Double.parseDouble(emiMessage[2].split("=")[1]);
														
														String tenure4Duration = emiMessage[3].split("=")[0];
														double tenure4Emi = Double.parseDouble(emiMessage[3].split("=")[1]);
														stremi = Math.round(tenure1Emi)+"+%28"+tenure1Duration+"+months%29"+"%2C+"
																+"Rs+"+Math.round(tenure2Emi)+"+%28"+tenure2Duration+"+months%29"+"%2C+"
																+"Rs+"+Math.round(tenure3Emi)+"+%28"+tenure3Duration+"+months%29"+"%2C+"
																+"Rs+"+Math.round(tenure4Emi)+"+%28"+tenure4Duration+"+months%29";
														logger.info("TaskExecutorService.java LNo : 217 : Sending SMS to customer for save quote");
														//logger.info("SMS_TEXT_FLEXI==============================="+SMS_TEXT);
														
													} catch (NullPointerException e){
														logger.info("TaskExecutorService.java LN 221 SendingSMSForHomeLoan :: appHL : appHL.getAppFlexiPayDetails() :: "+appHL.getAppFlexiPayDetails());
													} catch (Exception e){
														logger.info("TaskExecutorService.java LN 221 SendingSMSForHomeLoan :: appHL : appHL.getAppFlexiPayDetails() :: "+appHL.getAppFlexiPayDetails());
													}
												}else{
													logger.info("TaskExecutorService.java LN 224 SendingSMSForHomeLoan :: appHL : "+appHL.getAppFlexiPayDetails());
												}
    										}else if((appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0) 
    												&& (appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0)) {
	    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
	    											logger.info("TaskExecutorService.java PRIVILEGE SHAURYA...."+appHL.getAppHomeLoanId());
	    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmiDiscount1()))+"%281-"+appHL.getAppLoanEmi1Duration()+"+months%29"+"%2C+"+"Rs.+"+ String.valueOf(Math.round(appHL.getAppLoanEmiDiscount2()))+"%28"+(appHL.getAppLoanEmi1Duration()+1)+"-"+(appHL.getAppLoanEmi2Duration())+"+months%29"; 
	    											logger.info("TaskExecutorService.java PRIVILEGE SHAURYA......"+appHL.getAppLoanEmiDiscount1()+"..."+appHL.getAppLoanEmi1Duration()+"..stremi.."+stremi);
	    										}
	    										logger.info("TaskExecutorService.java LN 231 ::  appHL EmiDiscount 1 : "+appHL.getAppLoanEmiDiscount1() +" EmiDiscount 2 : "+appHL.getAppLoanEmiDiscount2());
	    										//logger.info("SMS_TEXT_==============================="+SMS_TEXT);
	    									}else if(appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0){
	    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
	    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmiDiscount2()));
	    										}
	    										logger.info("TaskExecutorService.java LN 237 ::  appHL EmiDiscount 2 : "+appHL.getAppLoanEmiDiscount2());
	    										//logger.info("SMS_TEXT==============================="+SMS_TEXT);
	    									}else if(appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0){
	    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
	    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmiDiscount1()));
	    										}
	    										logger.info("TaskExecutorService.java LN 243 ::  appHL EmiDiscount 1 : "+appHL.getAppLoanEmiDiscount1());
	    										//logger.info("SMS_TEXT==============================="+SMS_TEXT);
	    									}else if((appHL.getAppLoanEmi1()!=null && appHL.getAppLoanEmi1()>0) 
		    											&& (appHL.getAppLoanEmi2()!=null && appHL.getAppLoanEmi2()>0)){
		    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){

		    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmi1()))+"%281-"+appHL.getAppLoanEmi1Duration()+"+months%29"+"%2C+"+"Rs.+"+ String.valueOf(Math.round(appHL.getAppLoanEmi2()))+"%28"+(appHL.getAppLoanEmi1Duration()+1)+"-"+(appHL.getAppLoanEmi2Duration())+"+months%29"; 
		    											logger.info("TaskExecutorService.java LN 250 ::  appHL Emi 1 : "+appHL.getAppLoanEmi1()+" appHL Emi 2 : "+appHL.getAppLoanEmi2());
		    											//logger.info("SMS_TEXT==============================="+SMS_TEXT);
		    									}else if((appHL.getAppLoanEmi1()!=null && appHL.getAppLoanEmi1()>0)){
		    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
		    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmi1()));
		    										}
		    										logger.info("TaskExecutorService.java LN 256::  appHL EmiDiscount 1 : "+appHL.getAppLoanEmi1());
		    										//logger.info("SMS_TEXT==============================="+SMS_TEXT);
		    									}else if(appHL.getAppLoanEmi2()!=null && appHL.getAppLoanEmi2()>0){
		    										if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
		    											stremi = String.valueOf(Math.round(appHL.getAppLoanEmi2()));
		    										}
		    										logger.info("TaskExecutorService.java LN 262 ::  appHL EmiDiscount 1 : "+appHL.getAppLoanEmi2());
		    										//logger.info("SMS_TEXT==============================="+SMS_TEXT);
		    									}
	    									}else{
	    										if(appHL.getAppLoanEmiDiscount()!=null){
		    										if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
		    											stremi= String.valueOf(Math.round(appHL.getAppBhlFirstInterestEmi()))+"+%2F+"+String.valueOf(Math.round(appHL.getAppLoanEmi()));
		    										}else{
		    											stremi= String.valueOf(Math.round(appHL.getAppLoanEmiDiscount()));
		    										}
		    									}else if(appHL.getAppLoanEmi()!=null){
		    										if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
		    											stremi= String.valueOf(Math.round(appHL.getAppBhlFirstInterestEmi()))+"+%2F+"+String.valueOf(Math.round(appHL.getAppLoanEmi()));
		    										}else{
		    											stremi= String.valueOf(Math.round(appHL.getAppLoanEmi()));	
		    										}
		    										
		    									}
	    									}
											//logger.info("TaskExecutorService.java stremi.........."+stremi);
											
											SMS_TEXT=SMS_TEXT.replaceAll("EMI_AMT", stremi);
	    									SMS_TEXT=SMS_TEXT.replaceAll("APP_REF", appHL.getAppReferenceId());
	    									//logger.info("TaskExecutorService.java LNo : 286 : SMS_TEXT :: "+SMS_TEXT);
	    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    										logger.info("TaskExecutorService.java LNo : 281 : OTP Service is down HL .");
	    									}
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    			

	    			logger.info("TaskExecutorService.java LNo : 291 : MESSAGE SENDING TO BM SMS WILL SEND ONLY 8 AM TO 8 PM. HL");
	    			//logger.info("TaskExecutorService.java LNo : 292 : For Testing isAbleToSendBMOrSalesTeamMessage :: "+isAbleToSendBMOrSalesTeamMessage);
	    			if(isAbleToSendBMOrSalesTeamMessage){
	    				//logger.info("TaskExecutorService.java LNo : 294 : For Testing ISD Code: "+appHL.getAppISDCode()+" , Mobile : "+appHL.getAppMobileNo()+ " ,"
	    					//	+ " First Name : " + appHL.getAppFirstName() +" , appHL.getAppLastName() "+appHL.getAppLastName());
	    				if(appHL.getAppISDCode()!=null && appHL.getAppMobileNo()!=null && appHL.getAppFirstName()!=null && appHL.getAppLastName()!=null){
	    					SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    					int currentTime = Integer.parseInt(sdf.format(new Date()));
	    					if(currentTime>=8 && currentTime <=20){
    							String BMMobileNumber = null;
    							logger.info("TaskExecutorService.java LNo : 301 : For Testing Sales Team Id : "+appHL.getAppSalesTeamId() +", Branch Id : "+appHL.getAppBranchId());
    							if(appHL.getAppSalesTeamId()!=null && appHL.getAppBranchId()!=null){
    								MasterSalesTeam masterSalesTeam = commonService.getHLSalesTeamById(appHL.getAppSalesTeamId());
    								logger.info("TaskExecutorService.java LNo : 304 : For Testing Sales Team is CPC : "+masterSalesTeam.getSalesTeamIsCPC() );
    								if(masterSalesTeam!=null && masterSalesTeam.getSalesTeamIsCPC()!=null && masterSalesTeam.getSalesTeamIsCPC() == 1 ){
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appHL.getAppSalesTeamId(), appHL.getAppBranchId(), 1);	
    								}else{
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appHL.getAppSalesTeamId(), appHL.getAppBranchId(), 2);
    								}
    							}else{
    								BMMobileNumber = commonService.getBMOrSalesTeamMobile(null, appHL.getAppBranchId(), 3);
    							}
    							if(ValidatorUtil.isValid(BMMobileNumber)){
    								havePan = 2;
    								msgBody=communicationManagerImpl.setEmailBody(Constants.HOME_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
    								if(msgBody!=null){
    									msgBody = SbiUtil.urlEncode(msgBody);
    									String SMS_TEXT = null;


    									SMS_TEXT=Constants.SMS_STRING_INDIAN;
    									SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_MOBILE_CODE", (appHL.getAppISDCode() + appHL.getAppMobileNo()).trim());
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appHL.getAppFirstName()+(appHL.getAppMiddleName()!=null?" "+appHL.getAppMiddleName():"")+" "+(appHL.getAppLastName()))));
    									SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+BMMobileNumber));
    									//logger.info("TaskExecutorService.java LNo : 329 : Sending Message to BM : "+SMS_TEXT);
    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    										logger.info("TaskExecutorService.java LNo : 331 : OTP Service is down HL.");
    									}
    								}
    							}
	    					}
	    				}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("TaskExecutorService.java LNo : 340 : Caught Exception in Sending SMS : SendingSMSForHomeLoan()",e);
	    	} catch(Exception e){
	    		logger.info("TaskExecutorService.java LNo : 340 : Caught Exception in Sending SMS : SendingSMSForHomeLoan()",e);
	    	}
	    }
  	}
  	
  	private class SendingEmailForHomeLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormHomeLoan appHL;
  		private File[] attachment;
  		
	    public SendingEmailForHomeLoan(Integer requestIndex, Integer havePan, ApplicationFormHomeLoan appHL, File[] attachment) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appHL=appHL;
	    	this.attachment=attachment;
	    }
	    public void run() {
			try{
				//logger.info("TaskExecutorService.java LN 366 ::  Work Email :: " + appHL.getAppWorkEmail());
				if(appHL!=null && appHL.getAppWorkEmail()!=null){
					//String msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.HOME_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
					
					String msgBody = null;
					
				
					if ((appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0) && (appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0)) {
						msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.HOME_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,3)) + Constants.THIRD_EMAIL_PART;
						logger.info("TaskExecutorService.java CALLING LOAN EMI DISCOUNT TEMPLATE....");
					} else {
						msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.HOME_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
						logger.info("TaskExecutorService.java CALLING LOAN EMI TEMPLATE....");
				    }
					
					if(msgBody!=null ){
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						String [] email={appHL.getAppWorkEmail()};
						String stremi="";
						String stremiDisc="";
						
						if((appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0) && (appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0)){
							if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								//stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount1())+"/- <b><sup>1.</sup></b>"+" , "+" <img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount2())+" /- <b><sup>2.</sup></b>";
								stremiDisc="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount1())+"/- <b><sup>1.</sup></b>"+" , "+" <img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount2())+" /- <b><sup>2.</sup></b>";
								logger.info("TaskExecutorService.java LN 403 ::  appHL Discount1 :: " + appHL.getAppLoanEmiDiscount1() + " AND appHL Discount 2 :: "+ appHL.getAppLoanEmiDiscount2());
							}
						}else if(appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0){
							if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount1())+"/-";
								logger.info("TaskExecutorService.java LN 377 ::  appHL Discount1 :: " + appHL.getAppLoanEmiDiscount1());
							}
						}else if(appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0){
							if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmiDiscount2())+"/-";
								logger.info("TaskExecutorService.java LN 382 :: appHL Discount1 :: " + appHL.getAppLoanEmiDiscount2());
							}
						}else if((appHL.getAppLoanEmi1()!=null && appHL.getAppLoanEmi1()>0) && (appHL.getAppLoanEmi2()!=null && appHL.getAppLoanEmi2()>0)){
							if(Constants.SBI_PRIVILEGE_PRODUCT_ID.equals(appHL.getAppHomeLoanId()) || Constants.SBI_SHAURYA_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								//stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmi1())+"/- <b><sup>1.</sup></b>"+" , "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\"> "+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmi2())+" /- <b><sup>2.</sup></b>";
								
								stremiDisc="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmi1())+"/- <b><sup>1.</sup></b>"+" , "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\"> "+CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanEmi2())+" /- <b><sup>2.</sup></b>";
								logger.info("TaskExecutorService.java LN 420 :: stremiDisc...."+stremiDisc);
								logger.info("TaskExecutorService.java LN 421 ::  appHL Emi1 ::" + appHL.getAppLoanEmi1() + " AND appHL Emi1 2 :: "+ appHL.getAppLoanEmi2());
							}
						}else if(appHL.getAppLoanEmiDiscount()!=null){
							logger.info("TaskExecutorService.java LN 390 :: TEMPLATE DISCOUNT getAppLoanEmiDiscount::"+appHL.getAppLoanEmiDiscount());

								if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
									if(appHL.getAppFlexiPayDetails()!=null ){
											stremi = Constants.FLEXI_TEMPLATE;
											String[] emiMessage = appHL.getAppFlexiPayDetails().split("\\|");
											stremi=stremi.replaceAll("\\[EMI1_DURATION\\]", emiMessage[0].split("=")[0]+ " months");
											stremi=stremi.replaceAll("\\[EMI1_AMOUNT\\]", "Only Interest");
											stremi=stremi.replaceAll("\\[EMI2_DURATION\\]", emiMessage[1].split("=")[0]+ " months");
											stremi=stremi.replaceAll("\\[EMI2_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[1].split("=")[1])));
											stremi=stremi.replaceAll("\\[EMI3_DURATION\\]", emiMessage[2].split("=")[0]+ " months");
											stremi=stremi.replaceAll("\\[EMI3_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[2].split("=")[1])));
											stremi=stremi.replaceAll("\\[EMI4_DURATION\\]", emiMessage[3].split("=")[0]+ " months");
											stremi=stremi.replaceAll("\\[EMI4_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[3].split("=")[1])));
											stremi=stremi.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
									}
								}else{

										
										if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
											stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanEmi()));
											stremi="1 - 12 months: "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppBhlFirstInterestEmi()))+" / "+" 13 - 24 months: "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppLoanEmi()))+"/-";
											//logger.info("TaskExecutorService.java LN 419 FLEXI TEMPLATE DISCOUNT::" +stremi);
										}else{
											stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanEmiDiscount()));
											stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppLoanEmiDiscount()))+"/-";
											//logger.info("TaskExecutorService.java LN 423 FLEXI TEMPLATE DISCOUNT::" +stremi);
										}
										
								}

						}else if(appHL.getAppLoanEmi()!=null){
							if(Constants.FLEXI_PAY_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								if(appHL.getAppFlexiPayDetails()!=null ){
										stremi = Constants.FLEXI_TEMPLATE;
										String[] emiMessage = appHL.getAppFlexiPayDetails().split("\\|");
										stremi=stremi.replaceAll("\\[EMI1_DURATION\\]", emiMessage[0].split("=")[0]+ " months");
										stremi=stremi.replaceAll("\\[EMI1_AMOUNT\\]", "Only Interest");
										stremi=stremi.replaceAll("\\[EMI2_DURATION\\]", emiMessage[1].split("=")[0]+ " months");
										stremi=stremi.replaceAll("\\[EMI2_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[1].split("=")[1])));
										stremi=stremi.replaceAll("\\[EMI3_DURATION\\]", emiMessage[2].split("=")[0]+ " months");
										stremi=stremi.replaceAll("\\[EMI3_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[2].split("=")[1])));
										stremi=stremi.replaceAll("\\[EMI4_DURATION\\]", emiMessage[3].split("=")[0]+ " months");
										stremi=stremi.replaceAll("\\[EMI4_AMOUNT\\]", CurrencyUtil.moneyFormatInIndianStyle(Double.parseDouble(emiMessage[3].split("=")[1])));
										stremi=stremi.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
								}
							} else {
								
									if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
										stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanEmi()));
										stremi="1 - 12 months: "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppBhlFirstInterestEmi()))+" / "+" 13 - 24 months: "+"<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppLoanEmi()))+"/-";
										//logger.info("TaskExecutorService.java LN 460 FLEXI TEMPLATE DISCOUNT::" +stremi);
									}else{
										stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanEmi()));
										stremi="<img style=\"vertical-align:bottom\" src=\""+Constants.BANK_IMAGE_FOLDER+"/"+"rupee-icon.jpg\">"+CurrencyUtil.moneyFormatInIndianStyle(Math.round(appHL.getAppLoanEmi()))+"/-";
										//logger.info("TaskExecutorService.java LN 464 FLEXI TEMPLATE DISCOUNT::" +stremi);	
									}

							}
						}
						
						//logger.info("TaskExecutorService.java LN 560 msgBody Discount EMI TEMPLATE stremi::" +stremi);
						
						String lacOrLacs = "";
						String amtstr = "";
						String maxamtstr = "";
						if(appHL.getAppLoanAmount()!=null){
							Double amt=appHL.getAppLoanAmount();
							maxamtstr=CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
							if(amt>1){
								lacOrLacs=" lakhs";
							}else{
								lacOrLacs=" lakh";
							}
							amtstr=CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(amt));
						}
						HlProduct product=null;
						if(appHL.getAppHomeLoanId() != null ){
							product = commonService.getHomeLoanProductById(appHL.getAppHomeLoanId());
						}
						String irtype="Fixed";
						if(appHL.getAppLoanInterestRateType()!=null){
							irtype=appHL.getAppLoanInterestRateType()==1?"Floating":"Fixed";
						}
						String ir ="";
						if(appHL.getAppLoanInterestRateDiscount() != null && appHL.getAppLoanInterestRateDiscount() != 0){
							if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								ir = "1 - 12 months : "+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppBhlInterestRate()))+"% / 13 - 24 months : "+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
							}else{
								ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRateDiscount()));
							}
						}else if(appHL.getAppLoanInterestRate() != null && appHL.getAppLoanInterestRate() != 0){
							if(Constants.BHL_PRODUCT_ID.equals(appHL.getAppHomeLoanId())){
								ir = "1 - 12 months : "+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppBhlInterestRate()))+"% / 13 - 24 months : "+CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
							}else{
								ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appHL.getAppLoanInterestRate()));
							}
							
						}
						String  tenure=appHL.getAppLoanTenure()!=null? appHL.getAppLoanTenure().toString():"N/A";
						
						String encyQuotId=aesEncryption.encrypt(appHL.getAppSeqId().toString());
						String APP_NAME=appHL.getAppFirstName()!=null?StringUtil.capitalize(appHL.getAppFirstName()):"Applicant";
						
						String amtSep = "";
						if(appHL.getAppLoanAmount()!=null){
							amtSep = CurrencyUtil.moneyFormatInIndianStyle(appHL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						}
						String processingFee = "--";
						if(appHL.getAppLoanProcessingFeeDiscount()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanProcessingFeeDiscount()));
						}else if(appHL.getAppLoanProcessingFee()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appHL.getAppLoanProcessingFee()));
						}
						String basePrefixTenure="year";
						if(product != null){
							if(product.getHlProductSliderTenure()==1){
								basePrefixTenure="month";
							}
						}
						String APP_SALUTATION = "";
						if("M".equalsIgnoreCase(appHL.getAppGender())){
							APP_SALUTATION = "Mr.";
						}else if("F".equalsIgnoreCase(appHL.getAppGender())){
							APP_SALUTATION = "Ms.";
						}else{
							APP_SALUTATION = "";
						}
						String PRODUCT_NAME = "";
						if(product != null){
							PRODUCT_NAME = product.getHlProductName();
						}
						msgBody=msgBody.replaceAll("APP_SALUTATION",APP_SALUTATION);
						msgBody=msgBody.replaceAll("LOAN_TENURE_SEP",tenure+ (appHL.getAppLoanTenure()!=null && appHL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("LOAN_AMT_SEP",amtSep );
						msgBody=msgBody.replaceAll("APP_NAME",APP_NAME);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT_LAC",amtstr +lacOrLacs);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT",maxamtstr );
						msgBody=msgBody.replaceAll("LOAN_TENURE",tenure+ (appHL.getAppLoanTenure()!=null && appHL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("INTEREST_RATE",ir);
						//msgBody=msgBody.replaceAll("EMI_AMT",stremi ); 
						
						//if((appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0) && (appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0))
						if(((appHL.getAppLoanEmiDiscount1()!=null && appHL.getAppLoanEmiDiscount1()>0) && (appHL.getAppLoanEmiDiscount2()!=null && appHL.getAppLoanEmiDiscount2()>0)) ||
								((appHL.getAppLoanEmi1()!=null && appHL.getAppLoanEmi1()>0) && (appHL.getAppLoanEmi2()!=null && appHL.getAppLoanEmi2()>0)))
						{
							msgBody=msgBody.replaceAll("EMI_AMT",stremiDisc );
							logger.info("TaskExecutorService.java LN 566 msgBody Discount EMI TEMPLATE stremiDisc::" +stremiDisc+"....."+appHL.getAppLoanEmiDiscount1()+"...."+appHL.getAppLoanEmiDiscount2());
						} else {
							msgBody=msgBody.replaceAll("EMI_AMT",stremi );
							//logger.info("TaskExecutorService.java LN 569 msgBody Discount EMI TEMPLATE stremi::" +stremi);
						}
						
						msgBody=msgBody.replaceAll("IR_TYPE", irtype);
						msgBody=msgBody.replaceAll("PRO_FEE", processingFee);
						msgBody=msgBody.replaceAll("MARGIN", "--");
						msgBody=msgBody.replaceAll("PRODUCT NAME", PRODUCT_NAME);
						msgBody=msgBody.replaceAll("PRODUCT_NAME", PRODUCT_NAME);
						
						
						logger.info("TaskExecutorService.java LN 560 msgBody Discount EMI TEMPLATE processingFee::" +processingFee);
						
						Integer loanTenure = (appHL.getAppLoanTenure()!=null ?appHL.getAppLoanTenure():null);
						Double eligibilty = (appHL.getAppLoanAmount()!=null ? appHL.getAppLoanAmount(): null);
						String uiType="";
						if(ValidatorUtil.isValid(appHL.getExtra())){
							uiType="&uiType="+Constants.UI_TYPE;
						}
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							if(appHL.getAppHomeLoanId() == 5){

								msgBody=msgBody.replaceAll("GEN_LINK_IOS",Constants.SCHEMENAME+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.HOME_TOP_UP_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_HLTOPUP+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);

								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.HOME_TOP_UP_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_HLTOPUP+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}else{

								msgBody=msgBody.replaceAll("GEN_LINK_IOS",Constants.SCHEMENAME+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.HOME_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_HL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);

								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.HOME_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_HL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							msgBody=msgBody.replaceAll("APP_REF_ID",appHL.getAppReferenceId());
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"="+encyQuotId+uiType);
						}
						
						msgBody=msgBody.replaceAll("AIP_DATE", DateUtil.convertDateToFormattedType(appHL.getAppFilledAt(), "dd/MM/yyyy"));
						
						//logger.info("TaskExecutorService.java LN 560 msgBody Final HL Email ::" +msgBody);
						
						boolean sendStatus = false;
						//logger.info("TaskExecutorService.java LNo : 586 : Sending Email for Quote as well as AIP");
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							logger.info("TaskExecutorService.java LNo : 580 : Sending Email to customer for save quote");
							//logger.info("msgBody==============================="+msgBody);
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, "Your Home Loan Quote");
							logger.info("msgBody Constants.REQUEST_INDEX_APPLICATION_SUBMIT============"+Constants.REQUEST_INDEX_SAVE_QUOTE);
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							logger.info("TaskExecutorService.java LNo : 584 : Sending Email to customer for AIP Approval");
							//logger.info("msgBody==============================="+msgBody);
							logger.info("msgBody Constants.REQUEST_INDEX_APPLICATION_SUBMIT============"+Constants.REQUEST_INDEX_APPLICATION_SUBMIT);
							
							if(attachment.length!=0){
								sendStatus = communicationManagerImpl.sendEmailCsv(email, msgBody, PRODUCT_NAME+" In-principle Approval", attachment);
							}else{
								sendStatus = communicationManagerImpl.sendEmail(email, msgBody, PRODUCT_NAME+" In-principle Approval");
							}
						}
						logger.info("TaskExecutorService.java LNo : 600 : Finally the send status "+sendStatus);
						if(sendStatus){
							int count=appHL.getAppNonOtpEmailAlertsCount()==null?0:appHL.getAppNonOtpEmailAlertsCount();
							count=count+1;
							appHL.setAppNonOtpEmailAlertsCount(count);
							appHL.setAppNonOtpEmailAlertsLastTime(new Date());
							if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
								appHL.setAppAipMailSendStatus("Y");
							}
							homeLoanService.save(appHL);
						}else{
							logger.info("TaskExecutorService.java LNo : 600 : Currently mail server is down HL.");
						}
					}
				}
			} catch(NullPointerException e){
				logger.info("TaskExecutorService.java LNo : 605 : Caught Exception in Sending EMAIL :: SendingEmailForHomeLoan()",e);
			} catch(Exception e){
				logger.info("TaskExecutorService.java LNo : 605 : Caught Exception in Sending EMAIL :: SendingEmailForHomeLoan()",e);
			}
	    }
  	}
  	
	public void generatePDFForHomeLoan(ApplicationFormHomeLoan appHL, ApplicationFormHomeLoanQuote quoteHL)throws TaskRejectedException{
		taskExecutor.execute(new GeneratePDFForHomeLoan(appHL, quoteHL));
	}
	public void sendingEmailForHomeLoan(Integer requestIndex, Integer havePan, ApplicationFormHomeLoan appHL, File[] attachment)throws TaskRejectedException{
		taskExecutor.execute(new SendingEmailForHomeLoan(requestIndex, havePan, appHL, attachment));
	}
	public void sendingSMSForHomeLoan(Integer requestIndex, Integer havePan, ApplicationFormHomeLoan appHL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage)throws TaskRejectedException{
		taskExecutor.execute(new SendingSMSForHomeLoan(requestIndex, havePan, appHL, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage));
	}
  	
	private class GeneratePDFForAutoLoan implements Runnable {
  		private ApplicationFormAutoLoan appAL;
  		private ApplicationFormAutoLoanQuote quoteAL;
	    public GeneratePDFForAutoLoan(ApplicationFormAutoLoan appAL, ApplicationFormAutoLoanQuote quoteAL) {
	    	this.appAL=appAL;
	    	this.quoteAL=quoteAL;
	    }
		public void run() {
			try {
				if(appAL!=null && quoteAL!=null){
					String urlParameters = generatePDF.getPdfGenerationData(appAL, quoteAL);
					if (urlParameters != null) {
						urlParameters = "data=" + urlParameters.replaceAll("\"", "\\\"");
						JSONObject json = generatePDF.generatePDF(urlParameters, Constants.AUTO_LOAN_ID);
						if (json != null) {
							if (json.getString("status").equalsIgnoreCase("success")) {
								appAL.setAppDownloadPdfFileName(json.get("fileName").toString());
								appAL = autoLoanService.save(appAL);
							}
						}
					}
				}
			} catch (NullPointerException e) {
				logger.info("TaskExecutorService.java LNo : 643 : Exception caught in GeneratePDFForAutoLoan() ", e);
			} catch (Exception e) {
				logger.info("TaskExecutorService.java LNo : 643 : Exception caught in GeneratePDFForAutoLoan() ",e);
			}
		}
	}

  	private class SendingSMSForAutoLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormAutoLoan appAL;
  		private Integer bankLMSUserId;
  		private boolean isAbleToSendBMOrSalesTeamMessage;
	    public SendingSMSForAutoLoan(Integer requestIndex, Integer havePan, ApplicationFormAutoLoan appAL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appAL=appAL;
	    	this.bankLMSUserId=bankLMSUserId;
	    	this.isAbleToSendBMOrSalesTeamMessage=isAbleToSendBMOrSalesTeamMessage;
	    }
	    public void run() {
	    	try{

	    		//logger.info("TaskExecutorService.java LNo : 664 : MESSAGE SENDING TO CUSTOMER AL.");
	    		if(appAL!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(Constants.AUTO_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
	    				//if(appAL.getAppApplyingFrom()==2){
	    				//ISD code condition added for international SMS seva issue
		    			if(appAL.getAppApplyingFrom()==2 && (appAL.getAppISDCode() != null && !appAL.getAppISDCode().equals(Constants.COUNTRY_CODE_INDIA))){
	    					SMS_TEXT=Constants.SMS_STRING_NRI;
	    				}else{
	    					SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    				}
	    				SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    				if(requestIndex.equals(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE)){
	    					//logger.info("TaskExecutorService.java LNo : 677 : MESSAGE SENDING TO CUSTOMER AL :: Doc scheduled.");
	    					if(appAL.getAppISDCode()!=null && appAL.getAppMobileNo()!=null && appAL.getAppFirstName()!=null && appAL.getAppLastName()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appAL.getAppISDCode() + appAL.getAppMobileNo()).trim());
	    						SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appAL.getAppFirstName()+(appAL.getAppMiddleName()!=null?" "+appAL.getAppMiddleName():"")+" "+(appAL.getAppLastName()))));
	    						
	    						if(appAL.getAppDocPickupDateDT()!=null && appAL.getAppDocPickupTimeString()!=null){
	    							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", SbiUtil.urlEncode(DateUtil.convertDateToFormattedType(appAL.getAppDocPickupDateDT(),"dd-MM-yyyy")));
	    							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", SbiUtil.urlEncode(SbiUtil.getTimeInterval(appAL.getAppDocPickupTimeString())));
	    							
	    							if(bankLMSUserId!=null){
	    								BankLmsUser bankLmsUser = commonService.getBankLmsUserById(bankLMSUserId);
	    								if(bankLmsUser!=null && bankLmsUser.getLmsFirstName()!=null && bankLmsUser.getLmsLastName()!=null){
	    									SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_NAME", SbiUtil.urlEncode(String.valueOf(bankLmsUser.getLmsFirstName()+" "+(bankLmsUser.getLmsLastName()))));
	    								}
	    								//logger.info("SMS_TEXT_DOCUMENT_SCHEDULE==============================="+SMS_TEXT);
	    							}
	    							if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    								logger.info("TaskExecutorService.java LNo : 694 : OTP Service is down AL.");
	    							}
	    						}
	    					}
	    				}else{
	    					//logger.info("TaskExecutorService.java LNo : 699 : MESSAGE SENDING TO CUSTOMER AL:: AIP Approval.");
	    					if(appAL.getAppISDCode()!=null && appAL.getAppMobileNo()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appAL.getAppISDCode() + appAL.getAppMobileNo()).trim());
	    					}
	    					if(appAL.getAppLoanAmount()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("ELIGIBILITY_AMT", String.valueOf(new Double(appAL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR).intValue()));
	    						if(appAL.getAppAutoLoanId() != null && appAL.getAppLoanTenure() != null){
	    							AlProduct product = commonService.getAutoLoanProductById(appAL.getAppAutoLoanId());
	    							if(product!=null && product.getALProductSliderTenure()!=null ){
	    								if(product.getALProductSliderTenure()==1){
	    									SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appAL.getAppLoanTenure()));
	    								}else if(product.getALProductSliderTenure()==2){
	    									SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appAL.getAppLoanTenure()*12));
	    								}
	    								String ir ="";
	    								if(appAL.getAppLoanInterestRateDiscount() != null && appAL.getAppLoanInterestRateDiscount() != 0){
	    									ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appAL.getAppLoanInterestRateDiscount()));
	    								}else if(appAL.getAppLoanInterestRate() != null && appAL.getAppLoanInterestRate() != 0){
	    									ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appAL.getAppLoanInterestRate()));
	    								}
	    								SMS_TEXT=SMS_TEXT.replaceAll("IR_AMT", ir);
	    								String stremi="";
	    								if(appAL.getAppLoanEmiDiscount()!=null){
	    									stremi= String.valueOf(Math.round(appAL.getAppLoanEmiDiscount()));
	    								}else if(appAL.getAppLoanEmi()!=null){
	    									stremi= String.valueOf(Math.round(appAL.getAppLoanEmi()));
	    								}
	    								SMS_TEXT=SMS_TEXT.replaceAll("EMI_AMT", stremi);
	    								SMS_TEXT=SMS_TEXT.replaceAll("APP_REF", appAL.getAppReferenceId());
	    								logger.info("TaskExecutorService.java LNo : 745 : SMS_TEXT :: "+EncryptDecryptUtil.encrypt(SMS_TEXT));
	    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    									logger.info("TaskExecutorService.java LNo : 730 : OTP Service is down AL.");
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    			

	    			logger.info("TaskExecutorService.java LNo : 739 : MESSAGE SENDING TO BM SMS WILL SEND ONLY 8 AM TO 8 PM. AL");
	    			if(isAbleToSendBMOrSalesTeamMessage){
	    				if(appAL.getAppISDCode()!=null && appAL.getAppMobileNo()!=null && appAL.getAppFirstName()!=null && appAL.getAppLastName()!=null){
	    					SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    					int currentTime = Integer.parseInt(sdf.format(new Date()));
	    					if(currentTime>=8 && currentTime <=20){
	    						String BMMobileNumber = null;
	    						if(appAL.getAppSalesTeamId()!=null && appAL.getAppBranchId()!=null){
	    							MasterSalesTeam masterSalesTeam = commonService.getHLSalesTeamById(appAL.getAppSalesTeamId());
    								if(masterSalesTeam!=null && masterSalesTeam.getSalesTeamIsCPC()!=null && masterSalesTeam.getSalesTeamIsCPC() == 1 ){
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appAL.getAppSalesTeamId(), appAL.getAppBranchId(), 1);	
    								}else{
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appAL.getAppSalesTeamId(), appAL.getAppBranchId(), 2);
    								}
	    						}else{
	    							BMMobileNumber = commonService.getBMOrSalesTeamMobile(null, appAL.getAppBranchId(), 3);
	    						}
	    						if(ValidatorUtil.isValid(BMMobileNumber)){
	    							havePan = 2;
	    							msgBody=communicationManagerImpl.setEmailBody(Constants.AUTO_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    							if(msgBody!=null){
	    								msgBody = SbiUtil.urlEncode(msgBody);
	    								String SMS_TEXT = null;

	    								SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    								SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    								SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_MOBILE_CODE", (appAL.getAppISDCode() + appAL.getAppMobileNo()).trim());
	    								SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appAL.getAppFirstName()+(appAL.getAppMiddleName()!=null?" "+appAL.getAppMiddleName():"")+" "+(appAL.getAppLastName()))));
	    								SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+BMMobileNumber));
	    								logger.info("TaskExecutorService.java LNo : 772 : Sending Sms");
	    								//logger.info("msgBody==============================="+msgBody);
	    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    									logger.info("TaskExecutorService.java LNo : 775 : OTP Service is down AL.");
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("TaskExecutorService.java LNo : 784 : Caught Exception in Sending SMS : SendingSMSForAutoLoan()" ,e);
	    	} catch(Exception e){
	    		logger.info("TaskExecutorService.java LNo : 784 : Caught Exception in Sending SMS : SendingSMSForAutoLoan()" ,e);
	    	}
	    }
  	}
  	
  	private class SendingEmailForAutoLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormAutoLoan appAL;
  		
	    public SendingEmailForAutoLoan(Integer requestIndex, Integer havePan, ApplicationFormAutoLoan appAL) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appAL=appAL;
	    }
	    public void run() {
			try{
				if(appAL!=null && appAL.getAppWorkEmail()!=null){
					String msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.AUTO_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
					if(msgBody!=null){
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						String [] email={appAL.getAppWorkEmail()};
						ApplicationFormAutoLoanQuote autoLoanQuote = null;
						String bikeCompanyName = "";
						String bikeModelName = "";
						String carCompanyName = "";
						String carModelName = "";
						Integer carQuotePrice = 0;
						Integer bikeQuotePrice = 0;
						if(appAL.getAppQuoteId() != null){
							autoLoanQuote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appAL.getAppQuoteId());
							if(autoLoanQuote != null){
								//Integer bikeCompanyId = autoLoanQuote.getLoanQuoteBikeMakeId();
								//Integer bikeModelId = autoLoanQuote.getLoanQuoteBikeModelId();
								Integer carCompanyId = autoLoanQuote.getLoanQuoteCarMakeId();
								Integer carModelId = autoLoanQuote.getLoanQuoteCarModelId();
								
								/*if(bikeCompanyId != null){
									MasterBikeCompany bikeCompany = autoLoanService.getBikeCompanyById(bikeCompanyId);
									if(bikeCompany != null){
										bikeCompanyName = bikeCompany.getCompanyName();
									}
									if(bikeModelId != null){
										MasterBikeModel bikeModel = autoLoanService.getBikeModelById(bikeModelId);
										if(bikeModel != null){
											bikeModelName = bikeModel.getModelName();
										}
									}
									if(autoLoanQuote.getLoanQuoteLoanPurposeId()!=null && autoLoanQuote.getLoanQuoteLoanPurposeId().intValue()==8){
										bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteInsuredDeclaredValue() != null ? autoLoanQuote.getLoanQuoteInsuredDeclaredValue().intValue():0);
									}else{
										bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteOnRoadPrice() != null ? autoLoanQuote.getLoanQuoteOnRoadPrice().intValue():0);
									}
									bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteRoadTaxBike() != null ? autoLoanQuote.getLoanQuoteRoadTaxBike().intValue():0);
									bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteInsuranceChargeBike() != null ? autoLoanQuote.getLoanQuoteInsuranceChargeBike().intValue():0);
									bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteRegistrationChargeBike() != null ? autoLoanQuote.getLoanQuoteRegistrationChargeBike().intValue():0);
									bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteAccessoriesChargeBike() != null ? autoLoanQuote.getLoanQuoteAccessoriesChargeBike().intValue():0);
									bikeQuotePrice = bikeQuotePrice +(autoLoanQuote.getLoanQuoteOtherMiscChargeBike() != null ? autoLoanQuote.getLoanQuoteOtherMiscChargeBike().intValue():0);
								}*/
								
								if(carCompanyId != null){
									MasterCarCompany carCompany = autoLoanService.getCarCompanyById(carCompanyId);
									if(carCompany != null){
										carCompanyName = carCompany.getCompanyName();
									}
									if(carModelId != null){
										MasterCarModel carModel = autoLoanService.getCarModelById(carModelId);
										if(carModel != null){
											carModelName = carModel.getModelName();
										}
									}
									
									if(autoLoanQuote.getLoanQuoteLoanPurposeId()!=null && autoLoanQuote.getLoanQuoteLoanPurposeId().intValue()==8){
										carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteInsuredDeclaredValue()!= null ? autoLoanQuote.getLoanQuoteInsuredDeclaredValue().intValue():0);
									}else{
										carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteOnRoadPrice()!= null ? autoLoanQuote.getLoanQuoteOnRoadPrice().intValue():0);
									}
									carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteRoadTaxCar()!= null ? autoLoanQuote.getLoanQuoteRoadTaxCar().intValue():0);
									carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteInsuranceChargeCar()!= null ? autoLoanQuote.getLoanQuoteInsuranceChargeCar().intValue():0);
									carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteRegistrationChargeCar()!= null ? autoLoanQuote.getLoanQuoteRegistrationChargeCar().intValue():0);
									carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteAccessoriesChargeCar()!= null ? autoLoanQuote.getLoanQuoteAccessoriesChargeCar().intValue():0);
									carQuotePrice = carQuotePrice + (autoLoanQuote.getLoanQuoteOtherMiscChargeCar()!= null ? autoLoanQuote.getLoanQuoteOtherMiscChargeCar().intValue():0);
								}
							}
							
						}
						String stremi="";
						if(appAL.getAppLoanEmiDiscount()!=null){
							stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appAL.getAppLoanEmiDiscount()));
						}else if(appAL.getAppLoanEmi()!=null){
							stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appAL.getAppLoanEmi()));
						}
						
						Double amt=appAL.getAppLoanAmount();
						String maxamtstr=CurrencyUtil.moneyFormatInIndianStyle(appAL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						
						String amtstr = Double.toString(amt);
						String lacOrLacs = "";
						if(amt>1){
							lacOrLacs=" lakhs";
						}else{
							lacOrLacs=" lakh";
						}
						AlProduct product=null;
						if(appAL.getAppAutoLoanId() != null ){
							product = commonService.getAutoLoanProductById(appAL.getAppAutoLoanId());
						}
						String irtype=appAL.getAppLoanInterestRateType()==1?"Floating":"Fixed";
						String ir ="";
						if(appAL.getAppLoanInterestRateDiscount() != null && appAL.getAppLoanInterestRateDiscount() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appAL.getAppLoanInterestRateDiscount()));
						}else if(appAL.getAppLoanInterestRate() != null && appAL.getAppLoanInterestRate() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appAL.getAppLoanInterestRate()));
						}
						String  tenure=appAL.getAppLoanTenure()!=null? appAL.getAppLoanTenure().toString():"N/A";
						
						String encyQuotId=aesEncryption.encrypt(appAL.getAppSeqId().toString());
						String APP_NAME=appAL.getAppFirstName()!=null?StringUtil.capitalize(appAL.getAppFirstName()):"Applicant";
						
						String amtSep=CurrencyUtil.moneyFormatInIndianStyle(appAL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						String autoProductName = "" ;
						String autoProductPrice = "";
						String rupeeImageUrl ="<img style='vertical-align:bottom;' src='"+Constants.BANK_IMAGE_FOLDER+"/rupee-icon.jpg'>";
						if(bikeCompanyName !=  ""){
							autoProductName =  bikeCompanyName +"-" + bikeModelName ;
						}
						if(carCompanyName != ""){
							if(autoProductName.length()>2){
								autoProductName = autoProductName + ","+ carCompanyName + "-"+ carModelName ;
							}else{
								autoProductName = autoProductName + carCompanyName + "-"+ carModelName ;
							}
						}
						if(bikeQuotePrice.intValue() >0){
							autoProductPrice = "" + CurrencyUtil.moneyFormatInIndianStyle( new Double(bikeQuotePrice));
						}
						if(carQuotePrice.intValue() > 0){
							if(autoProductPrice.length() > 1){
								autoProductPrice = autoProductPrice +","+rupeeImageUrl + CurrencyUtil.moneyFormatInIndianStyle( new Double(carQuotePrice));
							}else{
								autoProductPrice = autoProductPrice + CurrencyUtil.moneyFormatInIndianStyle( new Double(carQuotePrice));
							}
						}
						String processingFee = "--";
						if(appAL.getAppLoanProcessingFeeDiscount()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appAL.getAppLoanProcessingFeeDiscount()));
						}else if(appAL.getAppLoanProcessingFee()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appAL.getAppLoanProcessingFee()));
						}
						String basePrefixTenure="year";
						if(product != null){
							if(product.getALProductSliderTenure()==1){
								basePrefixTenure="month";
							}
						}
						String APP_SALUTATION = "";
						if("M".equalsIgnoreCase(appAL.getAppGender())){
							APP_SALUTATION = "Mr.";
						}else if("F".equalsIgnoreCase(appAL.getAppGender())){
							APP_SALUTATION = "Ms.";
						}else{
							APP_SALUTATION = "";
						}
						String PRODUCT_NAME = "";
						if(product != null){
							PRODUCT_NAME = product.getAlProductName();
						}
						msgBody=msgBody.replaceAll("APP_SALUTATION",APP_SALUTATION);
						msgBody=msgBody.replaceAll("AUTO_PRODUCT_NAME", autoProductName);
						msgBody=msgBody.replaceAll("AUTO_PRODUCT_PRICE", autoProductPrice);
						msgBody=msgBody.replaceAll("LOAN_TENURE_SEP",tenure + (appAL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("LOAN_AMT_SEP",amtSep );
						msgBody=msgBody.replaceAll("APP_NAME",APP_NAME);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT_LAC",amtstr+ lacOrLacs);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT",maxamtstr );
						msgBody=msgBody.replaceAll("LOAN_TENURE",tenure+ (appAL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("INTEREST_RATE",ir);
						msgBody=msgBody.replaceAll("EMI_AMT",stremi );
						msgBody=msgBody.replaceAll("IR_TYPE", irtype);
						msgBody=msgBody.replaceAll("PRO_FEE", processingFee);
						msgBody=msgBody.replaceAll("PRODUCT NAME", PRODUCT_NAME);
						msgBody=msgBody.replaceAll("PRODUCT_NAME", PRODUCT_NAME);
						Integer loanTenure = (appAL.getAppLoanTenure()!=null ?appAL.getAppLoanTenure():null);
						Double eligibilty = (appAL.getAppLoanAmount()!=null ? appAL.getAppLoanAmount(): null);
						String uiType="";
						if(ValidatorUtil.isValid(appAL.getExtra())){
							uiType="&uiType="+Constants.UI_TYPE;
						}
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){

							msgBody=msgBody.replaceAll("GEN_LINK_IOS",Constants.SCHEMENAME+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.AUTO_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_AL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);

							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.AUTO_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_AL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							msgBody=msgBody.replaceAll("APP_REF_ID",appAL.getAppReferenceId());
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"="+encyQuotId+uiType+uiType);
						}
						
						boolean sendStatus = false;
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							//logger.info("TaskExecutorService.java LNo : 987 : Sending Email to customer for save quote");
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, "Your Auto Loan Quote");

						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							//logger.info("TaskExecutorService.java LNo : 991 : Sending Email to customer for AIP Approval");

							
							if(autoLoanQuote.getLoanQuoteLoanPurposeId()!=null && autoLoanQuote.getLoanQuoteLoanPurposeId().intValue()==8){
								msgBody=msgBody.replaceAll("On-road price of the Vehicle","Insured's Declared Value");
							}
							
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, PRODUCT_NAME+" In-principle Approval");
						}
						if(sendStatus){
							int count=appAL.getAppNonOtpEmailAlertsCount()==null?0:appAL.getAppNonOtpEmailAlertsCount();
							count=count+1;
							appAL.setAppNonOtpEmailAlertsCount(count);
							if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
								appAL.setAppAipMailSendStatus("Y");
							}
							appAL.setAppNonOtpEmailAlertsLastTime(new Date());
							autoLoanService.save(appAL);
						}else{
							logger.info("TaskExecutorService.java LNo : 1007 : Currently mail server is down");
						}
					}
				}
			} catch(NullPointerException e){
				logger.info("TaskExecutorService.java LNo : 1012 : Caught Exception in Sending EMAIL",e);
			} catch(Exception e){
				logger.info("TaskExecutorService.java LNo : 1012 : Caught Exception in Sending EMAIL",e);
			}
	    }
  	}
  	
  	public void generatePDFForAutoLoan(ApplicationFormAutoLoan appAL, ApplicationFormAutoLoanQuote quoteAL)throws TaskRejectedException{
		taskExecutor.execute(new GeneratePDFForAutoLoan(appAL, quoteAL));

  	}
  	public void sendingEmailForAutoLoan(Integer requestIndex, Integer havePan, ApplicationFormAutoLoan appAL)throws TaskRejectedException{
		taskExecutor.execute(new SendingEmailForAutoLoan(requestIndex, havePan, appAL));

  	}
  	public void sendingSMSForAutoLoan(Integer requestIndex, Integer havePan, ApplicationFormAutoLoan appAL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage)throws TaskRejectedException{
		taskExecutor.execute(new SendingSMSForAutoLoan(requestIndex, havePan, appAL, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage));

  	}
  	
	private class GeneratePDFForEducationLoan implements Runnable {
		private ApplicationFormEducationLoan appEL;
		private ApplicationFormEducationLoanQuote quoteEL;

		public GeneratePDFForEducationLoan(ApplicationFormEducationLoan appEL, ApplicationFormEducationLoanQuote quoteEL) {
			this.appEL = appEL;
			this.quoteEL = quoteEL;
		}

		public void run() {
			try {
				if (appEL != null && quoteEL != null) {
					String urlParameters = generatePDF.getPdfGenerationData(appEL, quoteEL);
					if (urlParameters != null) {
						urlParameters = "data=" + urlParameters.replaceAll("\"", "\\\"");
						JSONObject json = generatePDF.generatePDF(urlParameters, Constants.EDUCATION_LOAN_ID);
						if (json != null) {
							if (json.getString("status")!=null && json.getString("status").equalsIgnoreCase("success")) {
								if (json.get("fileName")!=null){
									appEL.setAppDownloadPdfFileName(json.get("fileName").toString());
								}
								appEL = educationLoanService.save(appEL);
							}
						}
					}
				}
			} catch (JSONException e) {
				logger.info("TaskExecutorService.java LNo : 1054 : Exception caught in GeneratePDFForEducationLoan() ", e);
			} catch (Exception e) {
				logger.info("TaskExecutorService.java LNo : 1054 : Exception caught in GeneratePDFForEducationLoan() ", e);
			}
		}
	}

  	private class SendingSMSForEducationLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormEducationLoan appEL;
  		private Integer bankLMSUserId;
  		private boolean isAbleToSendBMOrSalesTeamMessage;
	    public SendingSMSForEducationLoan(Integer requestIndex, Integer havePan, ApplicationFormEducationLoan appEL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appEL=appEL;
	    	this.bankLMSUserId=bankLMSUserId;
	    	this.isAbleToSendBMOrSalesTeamMessage=isAbleToSendBMOrSalesTeamMessage;
	    }
	    public void run() {
	    	try{

	    		//logger.info("TaskExecutorService.java LNo : 1075 : MESSAGE SENDING TO CUSTOMER EL.");
	    		if(appEL!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(Constants.EDUCATION_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
	    				//if(appEL.getAppApplyingFrom()==2){
	    				//ISD code condition added for international SMS seva issue
			    		if(appEL.getAppApplyingFrom()==2 && (appEL.getAppISDCode() != null && !appEL.getAppISDCode().equals(Constants.COUNTRY_CODE_INDIA))){
			    			SMS_TEXT=Constants.SMS_STRING_NRI;
	    				}else{
	    					SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    				}
	    				SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    				if(requestIndex.equals(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE)){
	    					//logger.info("TaskExecutorService.java LNo : 1088 : MESSAGE SENDING TO CUSTOMER EL :: Doc scheduled.");
	    					if(appEL.getAppISDCode()!=null && appEL.getAppMobileNo()!=null && appEL.getAppFirstName()!=null && appEL.getAppLastName()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appEL.getAppISDCode() + appEL.getAppMobileNo()).trim());
    							SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appEL.getAppFirstName()+(appEL.getAppMiddleName()!=null?" "+appEL.getAppMiddleName():"")+" "+(appEL.getAppLastName()))));
    							
    							if(appEL.getAppDocPickupDateDT()!=null && appEL.getAppDocPickupTimeString()!=null){
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", SbiUtil.urlEncode(DateUtil.convertDateToFormattedType(appEL.getAppDocPickupDateDT(),"dd-MM-yyyy")));
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", SbiUtil.urlEncode(SbiUtil.getTimeInterval(appEL.getAppDocPickupTimeString())));
    							
    								if(bankLMSUserId!=null){
    									BankLmsUser bankLmsUser = commonService.getBankLmsUserById(bankLMSUserId);
    									if(bankLmsUser!=null && bankLmsUser.getLmsFirstName()!=null && bankLmsUser.getLmsLastName()!=null){
    										SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_NAME", SbiUtil.urlEncode(String.valueOf(bankLmsUser.getLmsFirstName()+" "+(bankLmsUser.getLmsLastName()))));
    									}
    								}
    								//logger.info("SMS_TEXT_DOCUMENT_SCHEDULE==============================="+SMS_TEXT);
    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    									logger.info("TaskExecutorService.java LNo : 1105 : OTP Service is down EL.");
    								}
    							}
	    					}
	    				}else{
	    					//logger.info("TaskExecutorService.java LNo : 1110 : MESSAGE SENDING TO CUSTOMER EL:: AIP Approval.");
	    					if(appEL.getAppISDCode()!=null && appEL.getAppMobileNo()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appEL.getAppISDCode() + appEL.getAppMobileNo()).trim());
	    						if(appEL.getAppLoanAmount()!=null){
	    							SMS_TEXT=SMS_TEXT.replaceAll("ELIGIBILITY_AMT", String.valueOf(new Double(appEL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR).intValue()));
	    							if(appEL.getAppEducationLoanId() != null && appEL.getAppLoanTenure() != null){
	    								MasterElProduct product = commonService.getEducationLoanProductById(appEL.getAppEducationLoanId());
	    								if(product!=null && product.getELProductSliderTenure()!=null ){
	    									if(product.getELProductSliderTenure()==1){
	    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appEL.getAppLoanTenure()));
	    									}else if(product.getELProductSliderTenure()==2){
	    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appEL.getAppLoanTenure()*12));
	    									}
	    									String ir ="";
	    									if(appEL.getAppLoanInterestRateDiscount() != null && appEL.getAppLoanInterestRateDiscount() != 0){
	    										ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appEL.getAppLoanInterestRateDiscount()));
	    									}else if(appEL.getAppLoanInterestRate() != null && appEL.getAppLoanInterestRate() != 0){
	    										ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appEL.getAppLoanInterestRate()));
	    									}
	    									SMS_TEXT=SMS_TEXT.replaceAll("IR_AMT", ir);
	    									String stremi="";
	    									if(appEL.getAppLoanEmiDiscount()!=null){
	    										stremi= String.valueOf(Math.round(appEL.getAppLoanEmiDiscount()));
	    									}else if(appEL.getAppLoanEmi()!=null){
	    										stremi= String.valueOf(Math.round(appEL.getAppLoanEmi()));
	    									}
	    									SMS_TEXT=SMS_TEXT.replaceAll("EMI_AMT", stremi);
	    									
	    									SMS_TEXT=SMS_TEXT.replaceAll("APP_REF", appEL.getAppReferenceId());
	    									logger.info("TaskExecutorService.java LNo : 1174 : SMS_TEXT :: "+EncryptDecryptUtil.encrypt(SMS_TEXT));
	    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    										logger.info("TaskExecutorService.java LNo : 1140 : OTP Service is down EL.");
	    									}
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    			

	    			logger.info("TaskExecutorService.java LNo : 1150 : MESSAGE SENDING TO BM SMS WILL SEND ONLY 8 AM TO 8 PM. EL");
	    			if(isAbleToSendBMOrSalesTeamMessage){
	    				if(appEL.getAppISDCode()!=null && appEL.getAppMobileNo()!=null && appEL.getAppFirstName()!=null && appEL.getAppLastName()!=null){
	    					SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    					int currentTime = Integer.parseInt(sdf.format(new Date()));
	    					if(currentTime>=8 && currentTime <=20){
    							String BMMobileNumber = null;
    							if(appEL.getAppSalesTeamId()!=null && appEL.getAppBranchId()!=null){
    								MasterSalesTeam masterSalesTeam = commonService.getHLSalesTeamById(appEL.getAppSalesTeamId());
    								if(masterSalesTeam!=null && masterSalesTeam.getSalesTeamIsCPC()!=null && masterSalesTeam.getSalesTeamIsCPC() == 1 ){
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appEL.getAppSalesTeamId(), appEL.getAppBranchId(), 1);	
    								}else{
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appEL.getAppSalesTeamId(), appEL.getAppBranchId(), 2);
    								}
    							}else{
    								BMMobileNumber = commonService.getBMOrSalesTeamMobile(null, appEL.getAppBranchId(), 3);
    							}
    							if(ValidatorUtil.isValid(BMMobileNumber)){
    								havePan = 2;
    								msgBody=communicationManagerImpl.setEmailBody(Constants.EDUCATION_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
    								if(msgBody!=null){
    									msgBody = SbiUtil.urlEncode(msgBody);
    									String SMS_TEXT = null;

    									SMS_TEXT=Constants.SMS_STRING_INDIAN;
    									SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_MOBILE_CODE", (appEL.getAppISDCode() + appEL.getAppMobileNo()).trim());
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appEL.getAppFirstName()+(appEL.getAppMiddleName()!=null?" "+appEL.getAppMiddleName():"")+" "+(appEL.getAppLastName()))));
    									SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+BMMobileNumber));
    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    										logger.info("TaskExecutorService.java LNo : 1184 : OTP Service is down EL.");
    									}
    								}
    							}
	    					}
	    				}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("TaskExecutorService.java LNo : 1193 : Caught Exception in Sending SMS : SendingSMSForEducationLoan()",e);
	    	} catch(Exception e){
	    		logger.info("TaskExecutorService.java LNo : 1193 : Caught Exception in Sending SMS : SendingSMSForEducationLoan()",e);
	    	}
	    }
  	}
  	
  	private class SendingEmailForEducationLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormEducationLoan appEL;
  		
	    public SendingEmailForEducationLoan(Integer requestIndex, Integer havePan, ApplicationFormEducationLoan appEL) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appEL=appEL;
	    }
	    public void run() {
			try{
				//logger.info("requestIndex : "+requestIndex+" , appEL : "+appEL +" , work Email :"+appEL.getAppWorkEmail());
				if(appEL!=null && appEL.getAppWorkEmail()!=null){
					String msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.EDUCATION_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
					//logger.info("msgBody : "+msgBody);
					if(msgBody!=null){
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						String [] email={appEL.getAppWorkEmail()};
						String stremi="";
						if(appEL.getAppLoanEmiDiscount()!=null){
							stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appEL.getAppLoanEmiDiscount()));
						}else if(appEL.getAppLoanEmi()!=null){
							stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appEL.getAppLoanEmi()));
						}
						Double amt=appEL.getAppLoanAmount();


						String lacOrLacs = "";
						if(amt>1){
							lacOrLacs=" lakhs";
						}else{
							lacOrLacs=" lakh";
						}
						MasterElProduct product=null;
						if(appEL.getAppEducationLoanId() != null ){
							product = commonService.getEducationLoanProductById(appEL.getAppEducationLoanId());
						}
						String amtstr= CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(amt));
						String maxamtstr=CurrencyUtil.moneyFormatInIndianStyle(appEL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						
						String irtype=appEL.getAppLoanInterestRateType()==1?"Floating":"Fixed";
						String ir ="";
						if(appEL.getAppLoanInterestRateDiscount() != null && appEL.getAppLoanInterestRateDiscount() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appEL.getAppLoanInterestRateDiscount()));
						}else if(appEL.getAppLoanInterestRate() != null && appEL.getAppLoanInterestRate() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appEL.getAppLoanInterestRate()));
						}
						String  tenure=appEL.getAppLoanTenure()!=null? appEL.getAppLoanTenure().toString():"N/A";
						
						String encyQuotId=aesEncryption.encrypt(appEL.getAppSeqId().toString());
						String APP_NAME=appEL.getAppFirstName()!=null?StringUtil.capitalize(appEL.getAppFirstName()):"Applicant";
						

						String amtSep=CurrencyUtil.moneyFormatInIndianStyle(appEL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						String processingFee = "--";
						if(appEL.getAppLoanProcessingFeeDiscount()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appEL.getAppLoanProcessingFeeDiscount()));
						}else if(appEL.getAppLoanProcessingFee()!=null){
							processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appEL.getAppLoanProcessingFee()));
						}
						String basePrefixTenure="year";
						if(product!=null){
							if(product.getELProductSliderTenure()==1){
								basePrefixTenure="month";
							}
						}
						
						String APP_SALUTATION = "";
						if("M".equalsIgnoreCase(appEL.getAppGender())){
							APP_SALUTATION = "Mr.";
						}else if("F".equalsIgnoreCase(appEL.getAppGender())){
							APP_SALUTATION = "Ms.";
						}else{
							APP_SALUTATION = "";
						}
						String PRODUCT_NAME = "";
						if(product != null){
							PRODUCT_NAME = product.getElProductName();
						}
						msgBody=msgBody.replaceAll("APP_SALUTATION",APP_SALUTATION);
						msgBody=msgBody.replaceAll("LOAN_TENURE_SEP",tenure+ (appEL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("LOAN_AMT_SEP",amtSep );
						msgBody=msgBody.replaceAll("APP_NAME",APP_NAME);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT_LAC",amtstr+ lacOrLacs);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT",maxamtstr );
						msgBody=msgBody.replaceAll("LOAN_TENURE",tenure+ (appEL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("INTEREST_RATE",ir);
						msgBody=msgBody.replaceAll("EMI_AMT",stremi );
						msgBody=msgBody.replaceAll("IR_TYPE", irtype);
						msgBody=msgBody.replaceAll("PRO_FEE", processingFee);
						msgBody=msgBody.replaceAll("PRODUCT NAME", PRODUCT_NAME);
						msgBody=msgBody.replaceAll("PRODUCT_NAME", PRODUCT_NAME);
						Integer loanTenure = (appEL.getAppLoanTenure()!=null ?appEL.getAppLoanTenure():null);
						Double eligibilty = (appEL.getAppLoanAmount()!=null ? appEL.getAppLoanAmount(): null);
						String uiType="";
						if(ValidatorUtil.isValid(appEL.getExtra())){
							uiType="&uiType="+Constants.UI_TYPE;
						}
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							if(appEL.getAppEducationLoanId() !=null && appEL.getAppEducationLoanId()==8){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.EDUCATION_LOAN_TAKEOVER_ACTION+"?"+Constants.URL_QUTOE_TOKEN_EL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}else if(appEL.getAppEducationLoanId() !=null && appEL.getAppEducationLoanId()==7){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.GLOBAL_EDVANTAGE_ACTION+"?"+Constants.URL_QUTOE_TOKEN_EL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}/*else if(appEL.getAppEducationLoanId() !=null && appEL.getAppEducationLoanId()==9){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.EDUCATION_LOAN_BIDYALAKHMI_ACTION+"?"+Constants.URL_QUTOE_TOKEN_EL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}*/else{
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.SCHOLAR_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_EL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
								
							}
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							msgBody=msgBody.replaceAll("APP_REF_ID",appEL.getAppReferenceId());
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"="+encyQuotId+uiType);
						}
						
						boolean sendStatus = false;
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							//logger.info("TaskExecutorService.java LNo : 1318 : Sending Email to customer for save quote EL");
							//logger.info("msgBody==============================="+msgBody);
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, "Your Education Loan Quote");
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							//logger.info("msgBody==============================="+msgBody);
							//logger.info("TaskExecutorService.java LNo : 1323 : Sending Email to customer for AIP Approval EL");
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, PRODUCT_NAME+" In-principle Approval");
						}
						if(sendStatus){
							int count=appEL.getAppNonOtpEmailAlertsCount()==null?0:appEL.getAppNonOtpEmailAlertsCount();
							count=count+1;
							appEL.setAppNonOtpEmailAlertsCount(count);
							if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
								appEL.setAppAipMailSendStatus("Y");
							}
							appEL.setAppNonOtpEmailAlertsLastTime(new Date());
							educationLoanService.save(appEL);
						}else{
							//logger.info("TaskExecutorService.java LNo : 1333 : Currently mail server is down EL");
						}
					}
				}
			} catch(SQLException e){
				logger.info("TaskExecutorService.java LNo : 1338 : Caught Exception in Sending EMAIL : SendingEmailForEducationLoan()",e);
			} catch(Exception e){
				logger.info("TaskExecutorService.java LNo : 1338 : Caught Exception in Sending EMAIL : SendingEmailForEducationLoan()",e);
			}
	    }
  	}
  	
  	public void generatePDFForEducationLoan(ApplicationFormEducationLoan appEL, ApplicationFormEducationLoanQuote quoteEL)throws TaskRejectedException{
		taskExecutor.execute(new GeneratePDFForEducationLoan(appEL, quoteEL));

  	}
  	public void sendingEmailForEducationLoan(Integer requestIndex, Integer havePan, ApplicationFormEducationLoan appEL)throws TaskRejectedException{
		taskExecutor.execute(new SendingEmailForEducationLoan(requestIndex, havePan, appEL));
  	}
  	public void sendingSMSForEducationLoan(Integer requestIndex, Integer havePan, ApplicationFormEducationLoan appEL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage)throws TaskRejectedException{
		taskExecutor.execute(new SendingSMSForEducationLoan(requestIndex, havePan, appEL, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage));
  	}
  	 	
	private class GeneratePDFForPersonalLoan implements Runnable {
		private ApplicationFormPersonalLoan appPL;
		private ApplicationFormPersonalLoanQuote quotePL;

		public GeneratePDFForPersonalLoan(ApplicationFormPersonalLoan appPL, ApplicationFormPersonalLoanQuote quotePL) {
			this.appPL = appPL;
			this.quotePL = quotePL;
		}

		public void run() {
			try {
				if(appPL!=null && quotePL!=null){
					String urlParameters = generatePDF.getPdfGenerationData(appPL, quotePL);
					if (urlParameters != null) {
						urlParameters = "data=" + urlParameters.replaceAll("\"", "\\\"");
						JSONObject json = generatePDF.generatePDF(urlParameters, Constants.PERSONAL_LOAN_ID);
						if (json != null) {
							if (json.getString("status").equalsIgnoreCase("success")) {
								appPL.setAppDownloadPdfFileName(json.get("fileName").toString());
								appPL = personalLoanService.save(appPL);
							}
						}
					}
				}
			} catch (NullPointerException e) {
				logger.info("TaskExecutorService.java LNo : 1378 : Exception caught in GeneratePDFForPersonalLoan() ",e);
			} catch (Exception e) {
				logger.info("TaskExecutorService.java LNo : 1378 : Exception caught in GeneratePDFForPersonalLoan() ",e);
			}
		}
	}

  	private class SendingSMSForPersonalLoan implements Runnable { 
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormPersonalLoan appPL;
  		private Integer bankLMSUserId;
  		private boolean isAbleToSendBMOrSalesTeamMessage;
	    public SendingSMSForPersonalLoan(Integer requestIndex, Integer havePan, ApplicationFormPersonalLoan appPL,
	    		Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appPL=appPL;
	    	this.bankLMSUserId=bankLMSUserId;
	    	this.isAbleToSendBMOrSalesTeamMessage=isAbleToSendBMOrSalesTeamMessage;
	    }
	    public void run() {
	    	try{

	    	   logger.info("TaskExecutorService.java LNo : 1400 : MESSAGE SENDING TO CUSTOMER PL.");
	    		if(appPL!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(Constants.PERSONAL_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
			    		
	    				if(appPL.getAppApplyingFrom()==2 && (appPL.getAppISDCode() != null && !appPL.getAppISDCode().equals(Constants.COUNTRY_CODE_INDIA))){
	    					SMS_TEXT=Constants.SMS_STRING_NRI;
	    				}else{
	    					SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    				}
	    				
	    				SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    				if(requestIndex.equals(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE)){
	    					logger.info("TaskExecutorService.java LNo : 1413 : MESSAGE SENDING TO CUSTOMER PL :: Doc scheduled.");
	    					if(appPL.getAppISDCode()!=null && appPL.getAppMobileNo()!=null && appPL.getAppFirstName()!=null && appPL.getAppLastName()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appPL.getAppISDCode() + appPL.getAppMobileNo()).trim());
    							SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appPL.getAppFirstName()+(appPL.getAppMiddleName()!=null?" "+appPL.getAppMiddleName():"")+" "+(appPL.getAppLastName()))));

    							if(appPL.getAppDocPickupDateDT()!=null && appPL.getAppDocPickupTimeString()!=null){
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", SbiUtil.urlEncode(DateUtil.convertDateToFormattedType(appPL.getAppDocPickupDateDT(),"dd-MM-yyyy")));
    								SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", SbiUtil.urlEncode(SbiUtil.getTimeInterval(appPL.getAppDocPickupTimeString())));
    							
    								if(bankLMSUserId!=null){
    									BankLmsUser bankLmsUser = commonService.getBankLmsUserById(bankLMSUserId);
    									if(bankLmsUser!=null && bankLmsUser.getLmsFirstName()!=null && bankLmsUser.getLmsLastName()!=null){
    										SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_NAME", SbiUtil.urlEncode(String.valueOf(bankLmsUser.getLmsFirstName()+" "+(bankLmsUser.getLmsLastName()))));
    									}
    								}
    								//logger.info("SMS_TEXT_DOCUMENT_SCHEDULE==============================="+SMS_TEXT);
    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    									logger.info("TaskExecutorService.java LNo : 1430 : OTP Service is down PL.");
    								}
    							}
	    					}
	    				}else{
	    					logger.info("TaskExecutorService.java LNo : 1435 : MESSAGE SENDING TO CUSTOMER PL:: AIP Approval.");
	    					if(appPL.getAppISDCode()!=null && appPL.getAppMobileNo()!=null){
	    						
	    						//for gold loan
	    						if (appPL.getAppPersonalLoanId() != null && appPL.getAppPersonalLoanId().equals(13)) {
	    							SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appPL.getAppISDCode() + appPL.getAppMobileNo()).trim());
		    						if(appPL.getAppLoanAmount()!=null){
    									SMS_TEXT=SMS_TEXT.replaceAll("APP_REF", appPL.getAppReferenceId());
    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    										logger.info("TaskExecutorService.java LNo : 1465 : OTP Service is down PL .");
    									}
		    						}
	    						} else {
	    							SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appPL.getAppISDCode() + appPL.getAppMobileNo()).trim());
		    						if(appPL.getAppLoanAmount()!=null){
		    							SMS_TEXT=SMS_TEXT.replaceAll("ELIGIBILITY_AMT", String.valueOf(new Double(appPL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR).intValue()));
		    							if(appPL.getAppPersonalLoanId() != null && appPL.getAppLoanTenure() != null){
		    								MasterPlProduct product = commonService.getPersonalLoanProductById(appPL.getAppPersonalLoanId());
		    								if(product!=null && product.getPLProductSliderTenure()!=null ){
		    									if(product.getPLProductSliderTenure()==1){
		    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appPL.getAppLoanTenure()));
		    									}else if(product.getPLProductSliderTenure()==2){
		    										SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TENURE", String.valueOf(appPL.getAppLoanTenure()*12));
		    									}
		    									String ir ="";
		    									if(appPL.getAppLoanInterestRateDiscount() != null && appPL.getAppLoanInterestRateDiscount() != 0){
		    										ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appPL.getAppLoanInterestRateDiscount()));
		    									}else if(appPL.getAppLoanInterestRate() != null && appPL.getAppLoanInterestRate() != 0){
		    										ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appPL.getAppLoanInterestRate()));
		    									}
		    									SMS_TEXT=SMS_TEXT.replaceAll("IR_AMT", ir);
		    									String stremi="";
		    									if(appPL.getAppLoanEmiDiscount()!=null){
		    										stremi= String.valueOf(Math.round(appPL.getAppLoanEmiDiscount()));
		    									}else if(appPL.getAppLoanEmi()!=null){
		    										stremi= String.valueOf(Math.round(appPL.getAppLoanEmi()));
		    									}
		    									SMS_TEXT=SMS_TEXT.replaceAll("EMI_AMT", stremi);
		    									SMS_TEXT=SMS_TEXT.replaceAll("APP_REF", appPL.getAppReferenceId());
		    									//logger.info("msgBody==============================="+msgBody);
		    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
		    										logger.info("TaskExecutorService.java LNo : 1465 : OTP Service is down PL .");
		    									}
		    								}
		    							}
		    						}
	    						}
	    					}
	    				}
	    			}
	    			

	    			logger.info("TaskExecutorService.java LNo : 1475 : MESSAGE SENDING TO BM SMS WILL SEND ONLY 8 AM TO 8 PM. PL");
	    			if(isAbleToSendBMOrSalesTeamMessage){
	    				if(appPL.getAppISDCode()!=null && appPL.getAppMobileNo()!=null && appPL.getAppFirstName()!=null && appPL.getAppLastName()!=null){
	    					SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    					int currentTime = Integer.parseInt(sdf.format(new Date()));
	    					if(currentTime>=8 && currentTime <=20){
    							String BMMobileNumber = null;
    							if(appPL.getAppSalesTeamId()!=null && appPL.getAppBranchId()!=null){
    								MasterSalesTeam masterSalesTeam = commonService.getHLSalesTeamById(appPL.getAppSalesTeamId());
    								if(masterSalesTeam!=null && masterSalesTeam.getSalesTeamIsCPC()!=null && masterSalesTeam.getSalesTeamIsCPC() == 1 ){
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appPL.getAppSalesTeamId(), appPL.getAppBranchId(), 1);	
    								}else{
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appPL.getAppSalesTeamId(), appPL.getAppBranchId(), 2);
    								}
    							}else{
    								BMMobileNumber = commonService.getBMOrSalesTeamMobile(null, appPL.getAppBranchId(), 3);
    							}
    							if(ValidatorUtil.isValid(BMMobileNumber)){
    								havePan = 2;
    								msgBody=communicationManagerImpl.setEmailBody(Constants.PERSONAL_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
    								if(msgBody!=null){
    									msgBody = SbiUtil.urlEncode(msgBody);
    									String SMS_TEXT =null;

    									SMS_TEXT=Constants.SMS_STRING_INDIAN;
    									SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_MOBILE_CODE", (appPL.getAppISDCode() + appPL.getAppMobileNo()).trim());
    									SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appPL.getAppFirstName()+(appPL.getAppMiddleName()!=null?" "+appPL.getAppMiddleName():"")+" "+(appPL.getAppLastName()))));
    									SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+BMMobileNumber));
    									logger.info("TaskExecutorService.java LNo : 1467 : Sending Sms ");
    									//logger.info("msgBody==============================="+msgBody);
    									if(!communicationManagerImpl.sendSms(SMS_TEXT)){
    										logger.info("TaskExecutorService.java LNo : 1207 : OTP Service is down PL.");
    									}
    								}
    							}
	    					}
	    				}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("TaskExecutorService.java LNo : 1216 : Caught Exception in Sending SMS : SendingSMSForPersonalLoan()",e);
	    	} catch(Exception e){
	    		logger.info("TaskExecutorService.java LNo : 1216 : Caught Exception in Sending SMS : SendingSMSForPersonalLoan()",e);
	    	}
	    }
  	}
  	
  	private class SendingEmailForPersonalLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormPersonalLoan appPL;
  		
	    public SendingEmailForPersonalLoan(Integer requestIndex, Integer havePan, ApplicationFormPersonalLoan appPL) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appPL=appPL;
	    }
	    public void run() {
			try{
				if(appPL!=null && appPL.getAppWorkEmail()!=null){
					logger.info("In SendingEmailForPersonalLoan method lineNo: 1476" +requestIndex);
					String msgBody= null;
					if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
						msgBody=Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.PERSONAL_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
						logger.info("In SendingEmailForPersonalLoan method save quote block lineNo: 1480");
					}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
						ApplicationFormPersonalLoanQuote quotePL = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appPL.getAppQuoteId());
						if(quotePL!=null && quotePL.getLoanQuoteLoanPurposeId()!=null && quotePL.getLoanQuoteLoanPurposeId()==11){
							msgBody=Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.PERSONAL_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,0)) + Constants.THIRD_EMAIL_PART;
						}else{
							msgBody=Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.PERSONAL_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,1)) + Constants.THIRD_EMAIL_PART;
						}
					}
					if(msgBody!=null){
						String [] email={appPL.getAppWorkEmail()};
						String stremi="";
						if(appPL.getAppPersonalLoanId()==13 && appPL.getAppLoanTenure()<=12 ){
							stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanEmi()));
						}else{
							if(appPL.getAppLoanEmiDiscount()!=null){
								stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanEmiDiscount()));
							}else if(appPL.getAppLoanEmi()!=null){
								stremi=CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanEmi()));
							}
						}
						Double amt=appPL.getAppLoanAmount();
						String maxamtstr=CurrencyUtil.moneyFormatInIndianStyle(appPL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);

						String amtstr = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(amt));
						String lacOrLacs = "";
						if(amt>1){
							lacOrLacs=" lakhs";
						}else{
							lacOrLacs=" lakh";
						}
						MasterPlProduct product=null;
						if(appPL.getAppPersonalLoanId() != null ){
							product = commonService.getPersonalLoanProductById(appPL.getAppPersonalLoanId());
						}
						
						String irtype=appPL.getAppLoanInterestRateType()==1?"Floating":"Fixed";
						String ir ="";
						if(appPL.getAppLoanInterestRateDiscount() != null && appPL.getAppLoanInterestRateDiscount() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appPL.getAppLoanInterestRateDiscount()));
						}else if(appPL.getAppLoanInterestRate() != null && appPL.getAppLoanInterestRate() != 0){
							ir = CurrencyUtil.moneyForWithTwoPlaceDecimal(new Double(appPL.getAppLoanInterestRate()));
						}
						String  tenure=appPL.getAppLoanTenure()!=null? appPL.getAppLoanTenure().toString():"N/A";
						
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						
						String encyQuotId=aesEncryption.encrypt(appPL.getAppSeqId().toString());
						String APP_NAME=appPL.getAppFirstName()!=null?StringUtil.capitalize(appPL.getAppFirstName()):"Applicant";
						

						String amtSep=CurrencyUtil.moneyFormatInIndianStyle(appPL.getAppLoanAmount()*Constants.LOAN_ELIGIBILITY_MULTIPLER_FACTOR);
						String processingFee = "--";
						if(appPL.getAppLoanProcessingFeeDiscount()!=null){
							if(requestIndex==17){
								processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanProcessingFeeDiscount()))+"/-";
							}else{
								processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanProcessingFeeDiscount()));
							}
						}else if(appPL.getAppLoanProcessingFee()!=null){
							if(requestIndex==17){
								processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanProcessingFee()))+"/-";
							}else{
								processingFee = CurrencyUtil.moneyformatWithoutDecimal(Math.round(appPL.getAppLoanProcessingFee()));
							}
						}
						String basePrefixTenure="year";
						if(product != null){
							if(product.getPLProductSliderTenure()==1){
								basePrefixTenure="month";
							}
						}
						
						String APP_SALUTATION = "";
						if(appPL.getAppGender()!=null && "M".equalsIgnoreCase(appPL.getAppGender())){
							APP_SALUTATION = "Mr.";
						}else if(appPL.getAppGender()!=null && "F".equalsIgnoreCase(appPL.getAppGender())){
							APP_SALUTATION = "Ms.";
						}else{
							APP_SALUTATION = "";
						}
						String PRODUCT_NAME = "";
						if(product != null){
							PRODUCT_NAME = product.getPlProductName();
						}
						msgBody=msgBody.replaceAll("APP_SALUTATION",APP_SALUTATION);
						msgBody=msgBody.replaceAll("LOAN_TENURE_SEP",tenure+ (appPL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("LOAN_AMT_SEP",amtSep );
						msgBody=msgBody.replaceAll("APP_NAME",APP_NAME);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT_LAC",amtstr +lacOrLacs);
						msgBody=msgBody.replaceAll("MAX_LOAN_AMT",maxamtstr );
						msgBody=msgBody.replaceAll("LOAN_TENURE",tenure+  (appPL.getAppLoanTenure()>1?" "+basePrefixTenure+"s":" "+basePrefixTenure));
						msgBody=msgBody.replaceAll("INTEREST_RATE",ir);
						
						if(appPL.getAppLoanTenure()<=12 ){
							if(appPL.getAppPersonalLoanId()==13){
								msgBody=msgBody.replaceAll("EMI_AMT",stremi );
								msgBody=msgBody.replaceAll("EMI","Interest");
							}else{
								msgBody=msgBody.replaceAll("EMI_AMT",stremi );
							}
						}else{
							msgBody=msgBody.replaceAll("EMI_AMT",stremi );
						}
						msgBody=msgBody.replaceAll("IR_TYPE", irtype);
						msgBody=msgBody.replaceAll("PRO_FEE", processingFee);
						msgBody=msgBody.replaceAll("PRODUCT NAME", PRODUCT_NAME);
						msgBody=msgBody.replaceAll("PRODUCT_NAME", PRODUCT_NAME);
						Integer loanTenure = (appPL.getAppLoanTenure()!=null ?appPL.getAppLoanTenure():null);
						Double eligibilty = (appPL.getAppLoanAmount()!=null ? appPL.getAppLoanAmount(): null);
						String uiType="";
						if(ValidatorUtil.isValid(appPL.getExtra())){
							uiType="&uiType="+Constants.UI_TYPE;
						}
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){

							if(appPL.getAppPersonalLoanId() !=null && appPL.getAppPersonalLoanId()==13){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.GOLD_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_PL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}else if(appPL.getAppPersonalLoanId() !=null && appPL.getAppPersonalLoanId()==1){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.PERSONAL_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_PL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}else if(appPL.getAppPersonalLoanId() !=null && appPL.getAppPersonalLoanId()==2){
								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.PENSION_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_PNL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}else{

								msgBody=msgBody.replaceAll("GEN_LINK_IOS",Constants.SCHEMENAME+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.PERSONAL_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_PL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);

								msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.PERSONAL_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_PL+"="+encyQuotId+"&chosenTenure="+loanTenure.intValue()+"&chosenEligibility="+eligibilty.doubleValue()+uiType);
							}
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							msgBody=msgBody.replaceAll("APP_REF_ID",appPL.getAppReferenceId());
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"="+encyQuotId+uiType);
						}
						
						boolean sendStatus = false;
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							logger.info("TaskExecutorService.java LNo : 1335 : Sending Email to customer for save quote PL");
							//logger.info("msgBody==============================="+msgBody);
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, "Your Personal Loan Quote");
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							logger.info("TaskExecutorService.java LNo : 1335 : Sending Email to customer for AIP Approval PL");
							//logger.info("msgBody==============================="+msgBody);
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, PRODUCT_NAME+" In-principle Approval");
						}
						
						if(sendStatus){
							int count=appPL.getAppNonOtpEmailAlertsCount()==null?0:appPL.getAppNonOtpEmailAlertsCount();
							count=count+1;
							appPL.setAppNonOtpEmailAlertsCount(count);
							if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
								appPL.setAppAipMailSendStatus("Y");
							}
							appPL.setAppNonOtpEmailAlertsLastTime(new Date());
							personalLoanService.save(appPL);
						}else{
							logger.info("TaskExecutorService.java LNo : 1349 : Currently mail server is down PL");
						}
					}
				}
			} catch(NullPointerException e){
				logger.info("TaskExecutorService.java LNo : 1355 : Caught Exception in Sending EMAIL : SendingEmailForPersonalLoan()",e);
			} catch(Exception e){
				logger.info("TaskExecutorService.java LNo : 1355 : Caught Exception in Sending EMAIL : SendingEmailForPersonalLoan()",e);
			}
	    }
  	}
  	
  	public void generatePDFForPersonalLoan(ApplicationFormPersonalLoan appPL, ApplicationFormPersonalLoanQuote quotePL)throws TaskRejectedException{
		taskExecutor.execute(new GeneratePDFForPersonalLoan(appPL, quotePL));
  	}
  	public void sendingEmailForPersonalLoan(Integer requestIndex, Integer havePan, ApplicationFormPersonalLoan appPL)throws TaskRejectedException{
		taskExecutor.execute(new SendingEmailForPersonalLoan(requestIndex, havePan, appPL));
  	}
  	public void sendingSMSForPersonalLoan(Integer requestIndex, Integer havePan, ApplicationFormPersonalLoan appPL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage)throws TaskRejectedException{
		taskExecutor.execute(new SendingSMSForPersonalLoan(requestIndex, havePan, appPL, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage));
  	}
  	
	private class GeneratePDFForAgriLoan implements Runnable {
  		private ApplicationFormAgriLoan appAGL;
  		private ApplicationFormAgriLoanQuote quoteAGL;
	    public GeneratePDFForAgriLoan(ApplicationFormAgriLoan appAGL, ApplicationFormAgriLoanQuote quoteAGL) {
	    	this.appAGL=appAGL;
	    	this.quoteAGL=quoteAGL;
	    }
		public void run() {
			try {
				if(appAGL!=null && quoteAGL!=null){
					String urlParameters = generatePDF.getPdfGenerationData(appAGL, quoteAGL);
					if (urlParameters != null) {
						urlParameters = "data=" + urlParameters.replaceAll("\"", "\\\"");
						JSONObject json = generatePDF.generatePDF(urlParameters, Constants.AGRI_LOAN_ID);
						if (json != null) {
							if (json.getString("status").equalsIgnoreCase("success")) {
								appAGL.setAppDownloadPdfFileName(json.get("fileName").toString());
								appAGL = agriLoanService.save(appAGL);
							}
						}
					}
				}
			} catch (NullPointerException e) {
				logger.info("TaskExecutorService.java LNo : 1487 : Exception caught in GeneratePDFForAgriLoan() ",e);
			} catch (Exception e) {
				logger.info("TaskExecutorService.java LNo : 1487 : Exception caught in GeneratePDFForAgriLoan() ",e);
			}
		}
	}
	private class SendingSMSForAgriLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormAgriLoan appAGL;
  		private Integer bankLMSUserId;
  		private boolean isAbleToSendBMOrSalesTeamMessage;
	    public SendingSMSForAgriLoan(Integer requestIndex, Integer havePan, ApplicationFormAgriLoan appAGL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appAGL=appAGL;
	    	this.bankLMSUserId=bankLMSUserId;
	    	this.isAbleToSendBMOrSalesTeamMessage=isAbleToSendBMOrSalesTeamMessage;
	    }
	    public void run() {
	    	try{

	    		logger.info("TaskExecutorService.java LNo : 1507 : MESSAGE SENDING TO CUSTOMER AGL.");
	    		if(appAGL!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(Constants.AGRI_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, havePan);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
	    				
	    				//ISD code condition added for international SMS seva issue
			    		if(appAGL.getAppApplyingFrom()==2 && (appAGL.getAppISDCode() != null && !appAGL.getAppISDCode().equals(Constants.COUNTRY_CODE_INDIA))){
			    			SMS_TEXT=Constants.SMS_STRING_NRI;
	    				}else{
	    					SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    				}
	    				SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    				if(requestIndex.equals(Constants.REQUEST_INDEX_DOCUMENT_SCHEDULE)){
	    					logger.info("TaskExecutorService.java LNo : 1520 : MESSAGE SENDING TO CUSTOMER AGL :: Doc scheduled.");
	    					if(appAGL.getAppISDCode()!=null && appAGL.getAppMobileNo()!=null && appAGL.getAppFirstName()!=null && appAGL.getAppLastName()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appAGL.getAppISDCode() + appAGL.getAppMobileNo()).trim());
	    						SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appAGL.getAppFirstName()+(appAGL.getAppMiddleName()!=null?" "+appAGL.getAppMiddleName():"")+" "+(appAGL.getAppLastName()))));
	    						
	    						if(appAGL.getAppDocPickupDateDT()!=null && appAGL.getAppDocPickupTimeString()!=null){
	    							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_DATE", SbiUtil.urlEncode(DateUtil.convertDateToFormattedType(appAGL.getAppDocPickupDateDT(),"dd-MM-yyyy")));
	    							SMS_TEXT=SMS_TEXT.replaceAll("SCHEDULE_TIME", SbiUtil.urlEncode(SbiUtil.getTimeInterval(appAGL.getAppDocPickupTimeString())));
	    							
	    							if(bankLMSUserId!=null){
	    								BankLmsUser bankLmsUser = commonService.getBankLmsUserById(bankLMSUserId);
	    								if(bankLmsUser!=null && bankLmsUser.getLmsFirstName()!=null && bankLmsUser.getLmsLastName()!=null){
	    									SMS_TEXT=SMS_TEXT.replaceAll("REPRESENTATIVE_NAME", SbiUtil.urlEncode(String.valueOf(bankLmsUser.getLmsFirstName()+" "+(bankLmsUser.getLmsLastName()))));
	    								}
	    							}
	    							//logger.info("SMS_TEXT_DOCUMENT_SCHEDULE==============================="+SMS_TEXT);
	    							logger.info("TaskExecutorService.java LNo : 1535 AGL :"+SMS_TEXT);
	    							if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    								logger.info("TaskExecutorService.java LNo : 1536 : OTP Service is down AGL.");
	    							}
	    						}
	    					}
	    				}else{
	    					logger.info("TaskExecutorService.java LNo : 1541 : MESSAGE SENDING TO CUSTOMER AGL:: Application submitted.");
	    					if(appAGL.getAppISDCode()!=null && appAGL.getAppMobileNo()!=null){
	    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (appAGL.getAppISDCode() + appAGL.getAppMobileNo()).trim());
	    					}
	    					logger.info("TaskExecutorService.java LNo AGL : 1563 :"+EncryptDecryptUtil.encrypt(SMS_TEXT));
	    					if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    						logger.info("TaskExecutorService.java LNo : 1565 : OTP Service is down AGL.");
	    					}
	    				}
	    			}
	    			

	    			logger.info("TaskExecutorService.java LNo : 1573 AGL : MESSAGE SENDING TO BM SMS WILL SEND ONLY 8 AM TO 8 PM. AL");
	    			if(isAbleToSendBMOrSalesTeamMessage){
	    				if(appAGL.getAppISDCode()!=null && appAGL.getAppMobileNo()!=null && appAGL.getAppFirstName()!=null && appAGL.getAppLastName()!=null){
	    					SimpleDateFormat sdf = new SimpleDateFormat("HH");
	    					int currentTime = Integer.parseInt(sdf.format(new Date()));
	    					if(currentTime>=8 && currentTime <=20){
	    						String BMMobileNumber = null;
	    						if(appAGL.getAppSalesTeamId()!=null && appAGL.getAppBranchId()!=null){
	    							MasterSalesTeam masterSalesTeam = commonService.getHLSalesTeamById(appAGL.getAppSalesTeamId());
    								if(masterSalesTeam!=null && masterSalesTeam.getSalesTeamIsCPC()!=null && masterSalesTeam.getSalesTeamIsCPC() == 1 ){
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appAGL.getAppSalesTeamId(), appAGL.getAppBranchId(), 1);	
    								}else{
    									BMMobileNumber = commonService.getBMOrSalesTeamMobile(appAGL.getAppSalesTeamId(), appAGL.getAppBranchId(), 2);
    								}
	    						}else{
	    							BMMobileNumber = commonService.getBMOrSalesTeamMobile(null, appAGL.getAppBranchId(), 3);
	    						}
	    						if(ValidatorUtil.isValid(BMMobileNumber)){
	    							havePan = 2;
	    							msgBody=communicationManagerImpl.setEmailBody(Constants.AGRI_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_SMS, 2);
	    							if(msgBody!=null){
	    								msgBody = SbiUtil.urlEncode(msgBody);
	    								String SMS_TEXT = null;

	    								SMS_TEXT=Constants.SMS_STRING_INDIAN;
	    								SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
	    								SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_MOBILE_CODE", (appAGL.getAppISDCode() + appAGL.getAppMobileNo()).trim());
	    								SMS_TEXT=SMS_TEXT.replaceAll("CUSTOMER_NAME", SbiUtil.urlEncode(String.valueOf(appAGL.getAppFirstName()+(appAGL.getAppMiddleName()!=null?" "+appAGL.getAppMiddleName():"")+" "+(appAGL.getAppLastName()))));
	    								SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", (Constants.COUNTRY_CODE_INDIA+BMMobileNumber));
	    								//logger.info("TaskExecutorService.java LNo AGL : 1606 :"+SMS_TEXT);
	    								if(!communicationManagerImpl.sendSms(SMS_TEXT)){
	    									logger.info("TaskExecutorService.java LNo : 1606 : OTP Service is down AGL.");
	    								}
	    							}
	    						}
	    					}
	    				}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("TaskExecutorService.java LNo : 1615 AGL : Caught Exception in Sending SMS : SendingSMSForAgriLoan()" ,e);
	    	} catch(Exception e){
	    		logger.info("TaskExecutorService.java LNo : 1615 AGL : Caught Exception in Sending SMS : SendingSMSForAgriLoan()" ,e);
	    	}
	    }
  	}
	
	private class SendingEmailForAgriLoan implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormAgriLoan appAGL;
  		
  		private String loanPurposeNameAGL1;
  		private String loanPurposeNameAGL2;
  		private String loanPurposeNameAGL3;
  		private String loanPurposeNameAGL4;
  		
	    public SendingEmailForAgriLoan(Integer requestIndex, Integer havePan, ApplicationFormAgriLoan appAGL) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.appAGL=appAGL;
	    }
	    public void run() {
			try{
				if(appAGL!=null && appAGL.getAppWorkEmail()!=null){
					String msgBody = Constants.FIRST_EMAIL_PART +  StringEscapeUtils.unescapeHtml(communicationManagerImpl.setEmailBody(Constants.AGRI_LOAN_ID, requestIndex, Constants.MESSAGE_TYPE_EMAIL,havePan)) + Constants.THIRD_EMAIL_PART;
					if(msgBody!=null){
						msgBody=msgBody.replaceAll("\\[BASE_URL\\]",Constants.BANK_IMAGE_FOLDER+"/");
						msgBody=msgBody.replaceAll("\\[BANK_URL\\]",Constants.BANK_URL);
						msgBody=msgBody.replaceAll("\\[BANK_NAME\\]",Constants.BANK_NAME);
						msgBody=msgBody.replaceAll("\\[BANK_FULL_NAME\\]",Constants.BANK_FULL_NAME);
						String [] email={appAGL.getAppWorkEmail()};
						ApplicationFormAgriLoanQuote quoteAGL = null;
						if(appAGL.getAppQuoteId() != null){
							quoteAGL = agriLoanService.getApplicationFormAgriLoanQuoteByQuoteId(appAGL.getAppQuoteId());
							if(quoteAGL != null){
								if(quoteAGL.getLoanQuoteLoanPurposeId1()!=null){
									MasterLoanPurpose loanpurposeobj1=commonService.getLoanPurposeById(quoteAGL.getLoanQuoteLoanPurposeId1());
									if(loanpurposeobj1!=null){
										loanPurposeNameAGL1 = loanpurposeobj1.getLpTypeValue();
									}
								}
								if(quoteAGL.getLoanQuoteLoanPurposeId2()!=null){
									MasterLoanPurpose loanpurposeobj2=commonService.getLoanPurposeById(quoteAGL.getLoanQuoteLoanPurposeId2());
									if(loanpurposeobj2!=null){
										loanPurposeNameAGL2=(loanpurposeobj2.getLpTypeValue());
										
									}
								}
								if(quoteAGL.getLoanQuoteLoanPurposeId3()!=null){
									MasterLoanPurpose loanpurposeobj3=commonService.getLoanPurposeById(quoteAGL.getLoanQuoteLoanPurposeId3());
									if(loanpurposeobj3!=null){
										loanPurposeNameAGL3=(loanpurposeobj3.getLpTypeValue());
									}
								}
								if(quoteAGL.getLoanQuoteLoanPurposeId4()!=null){
									MasterLoanPurpose loanpurposeobj4=commonService.getLoanPurposeById(quoteAGL.getLoanQuoteLoanPurposeId4());
									if(loanpurposeobj4!=null){
										loanPurposeNameAGL4=(loanpurposeobj4.getLpTypeValue());
									}
								}
							}
							}
						String encyQuotId=aesEncryption.encrypt(appAGL.getAppSeqId().toString());
						String APP_NAME=appAGL.getAppFirstName()!=null?StringUtil.capitalize(appAGL.getAppFirstName()):"Applicant";
					
						String APP_SALUTATION = "";
						if(appAGL.getAppGender()!=null && "M".equalsIgnoreCase(appAGL.getAppGender())){
							APP_SALUTATION = "Mr.";
						}else if(appAGL.getAppGender()!=null && "F".equalsIgnoreCase(appAGL.getAppGender())){
							APP_SALUTATION = "Ms.";
						}else{
							APP_SALUTATION = "";
						}
						ArrayList<String> arrayList=new ArrayList<String>(4);
						String PRODUCT_NAME = "";
						if(loanPurposeNameAGL1!=null){
							 arrayList.add(loanPurposeNameAGL1);
						}
						if(loanPurposeNameAGL2!=null){
							arrayList.add(loanPurposeNameAGL2);
						}
						if(loanPurposeNameAGL3!=null){
							arrayList.add(loanPurposeNameAGL3);
						}
						if(loanPurposeNameAGL4!=null){
							arrayList.add(loanPurposeNameAGL4);
						}
						PRODUCT_NAME=arrayList.toString();
						PRODUCT_NAME =PRODUCT_NAME.substring(1, PRODUCT_NAME.length()-1);
						
						String REFERENCE_ID="";
						if(appAGL!=null && appAGL.getAppReferenceId()!=null){
							REFERENCE_ID=appAGL.getAppReferenceId();
						}
						msgBody=msgBody.replaceAll("APP_SALUTATION",APP_SALUTATION);
						msgBody=msgBody.replaceAll("APP_NAME",APP_NAME);
						msgBody=msgBody.replaceAll("\\[PRODUCT_NAME\\]", PRODUCT_NAME);
						msgBody=msgBody.replaceAll("APP_REF_ID", REFERENCE_ID);
						
						String uiType="";
						if(ValidatorUtil.isValid(appAGL.getExtra())){
							uiType="&uiType="+Constants.UI_TYPE;
						}
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.AGRI_LOAN_ACTION+"?"+Constants.URL_QUTOE_TOKEN_AGL+"="+encyQuotId+uiType+uiType);
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							msgBody=msgBody.replaceAll("APP_REF_ID",REFERENCE_ID);
							msgBody=msgBody.replaceAll("GEN_LINK",Constants.PROTOCOL+Constants.IP_URL_INTERNET+Constants.CONTEXT+Constants.APPLICATION_STATUS_ACTION+"="+encyQuotId+uiType);
						}
						
						boolean sendStatus = false;
						if(requestIndex==Constants.REQUEST_INDEX_SAVE_QUOTE){
							logger.info("TaskExecutorService.java LNo : 1722 AGL : Sending Email to customer for save quote");
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, "Your Agri Loan Quote");
						}else if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
							logger.info("TaskExecutorService.java LNo : 1724 AGL : Sending Email to customer for Application submitted");
							sendStatus = communicationManagerImpl.sendEmail(email, msgBody, PRODUCT_NAME+" In-principle Approval");
						}
						if(sendStatus){
							int count=appAGL.getAppNonOtpEmailAlertsCount()==null?0:appAGL.getAppNonOtpEmailAlertsCount();
							count=count+1;
							appAGL.setAppNonOtpEmailAlertsCount(count);
							if(requestIndex==Constants.REQUEST_INDEX_APPLICATION_SUBMIT){
								appAGL.setAppAipMailSendStatus("Y");
							}
							agriLoanService.save(appAGL);
						}else{
							logger.info("TaskExecutorService.java LNo : 1734 AGL : Currently mail server is down");
						}
					}
				}
			} catch(NullPointerException e){
				logger.info("TaskExecutorService.java LNo : 1739 AGL : Caught Exception in Sending EMAIL",e);
			} catch(Exception e){
				logger.info("TaskExecutorService.java LNo : 1739 AGL : Caught Exception in Sending EMAIL",e);
			}
	    }
  	}
	
  	public void generatePDFForAgriLoan(ApplicationFormAgriLoan appAGL, ApplicationFormAgriLoanQuote quoteAL)throws TaskRejectedException{
		taskExecutor.execute(new GeneratePDFForAgriLoan(appAGL, quoteAL));
  	}
  	public void sendingEmailForAgriLoan(Integer requestIndex, Integer havePan, ApplicationFormAgriLoan appAGL)throws TaskRejectedException{
		taskExecutor.execute(new SendingEmailForAgriLoan(requestIndex, havePan, appAGL));
  	}
  	public void sendingSMSForAgriLoan(Integer requestIndex, Integer havePan, ApplicationFormAgriLoan appAGL, Integer bankLMSUserId, boolean isAbleToSendBMOrSalesTeamMessage)throws TaskRejectedException{
		taskExecutor.execute(new SendingSMSForAgriLoan(requestIndex, havePan, appAGL, bankLMSUserId, isAbleToSendBMOrSalesTeamMessage));
  	}
  	
	private class SendingSMSForInfronics implements Runnable {
  		private Integer requestIndex;
  		private Integer havePan;
  		private ApplicationFormLead formLead;
	    public SendingSMSForInfronics(Integer requestIndex, Integer havePan, ApplicationFormLead formLead, Integer bankLMSUserId) {
	    	this.requestIndex=requestIndex;
	    	this.havePan=havePan;
	    	this.formLead=formLead;
	    }
	    public void run() {
	    	try{

	    		//logger.info("SendingSMSForInfronics() SBI LEAD MESSAGE SENDING TO CUSTOMER.");
	    		if(formLead!=null){
	    			String msgBody=communicationManagerImpl.setEmailBody(0, requestIndex, Constants.MESSAGE_TYPE_SMS, 1);
	    			//logger.info("SendingSMSForInfronics() Template for customer ::: "+msgBody);
	    			if(msgBody!=null){
	    				msgBody = SbiUtil.urlEncode(msgBody);
	    				String SMS_TEXT = null;
	    				
	    				SMS_TEXT=Constants.SMS_STRING_INDIAN;
    					SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
    					if(formLead.getLeadMobileNo()!=null && formLead.getLeadProductTypeId()!=null){
    						SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", formLead.getLeadMobileNo());
    						SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);

    					}
    					//logger.info("SendingSMSForInfronics() Final message to Customer "+SMS_TEXT);
    					if(!communicationManagerImpl.sendSms(SMS_TEXT)){
							logger.info("SendingSMSForInfronics() SMS Service is down.");
						}
	    			}
	    		}
	    	} catch(NullPointerException e){
	    		logger.info("SendingSMSForInfronics() Caught Exception in Sending SMS : SendingSMSForInfronics()",e);
	    	} catch(Exception e){
	    		logger.info("SendingSMSForInfronics() Caught Exception in Sending SMS : SendingSMSForInfronics()",e);
	    	}
	    }
  	}

  	public void SendingSMSForInfronics(Integer requestIndex, Integer havePan,  ApplicationFormLead formLead , Integer bankLMSUserId)throws TaskRejectedException{
  		taskExecutor.execute(new SendingSMSForInfronics(requestIndex, havePan, formLead, bankLMSUserId));	

  	}

  	private class SendMailTest implements Runnable {
  		private String displayData = null;
  		public SendMailTest(String displayData){
  			this.displayData = displayData;
  		}
  		 public void run() {
 		    logger.info("TaskExecutorService.java LNo : 1378 : displayData "+displayData);
 	    }
  	}
  	public void CallTaskExecuter(String displayData) {
		taskExecutor.execute(new SendMailTest(displayData));
	}
}
