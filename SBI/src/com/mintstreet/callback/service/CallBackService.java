package com.mintstreet.callback.service;

import java.sql.SQLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.CallBackLeadPushLog;
import com.mintstreet.common.entity.Consent;
import com.mintstreet.common.entity.MasterCity;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.util.Constants;
import com.servion.sbiccwebservice.ContactCenterWebserviceProxy;



public class CallBackService   {
	private static final Logger logger = LogManager.getLogger(CallBackService.class.getName());


	@Autowired
	CommonService commonService;

	@Autowired
	private TaskExecutor taskExecutor;

	private int responseReturned;

	public void getcallBackService(ApplicationFormLead leads){
		try{
			logger.info("CallBackService.java :: LNO :: 33 getcallBackService :: Start "+leads);
			pushLeadToContactCenter(leads);
			logger.info("CallBackService.java :: LNO :: 35 getcallBackService :: End ");
		} catch(NullPointerException e){
			logger.info("CallBackService.java :: LNO :: 39 getcallBackService :: exception caught ",e);
		} catch(Exception e){
			logger.info("CallBackService.java :: LNO :: 39 getcallBackService :: exception caught ",e);
		}
	}

	public void pushLeadToContactCenter(ApplicationFormLead leads){
		try{
			logger.info("CallBackService.java :: LNO :: 45 pushLeadToContactCenter :: Start "+leads);
			taskExecutor.execute(new PushLead(leads));
			logger.info("CallBackService.java :: LNO :: 47 pushLeadToContactCenter :: End "+leads);
		} catch(NullPointerException e){
			logger.info("CallBackService.java :: LNO :: 49 pushLeadToContactCenter :: exception caught ",e);
		} catch(Exception e){
			logger.info("CallBackService.java :: LNO :: 49 pushLeadToContactCenter :: exception caught ",e);
		}
	}

	private class PushLead implements Runnable{
		ApplicationFormLead lead;
		public PushLead(ApplicationFormLead leads){
			this.lead=leads;
		}
		@Override
		public void run() {
			try {
				logger.info("CallBackService.java :: LNO :: 55 run :: Start ");
				pushLead(this.lead);
				logger.info("CallBackService.java :: LNO :: 57 run :: End ");
			} catch (JSONException e) {
				logger.info("CallBackService.java :: LNO :: 58 Error while pushing contact center lead: ", e);
			} catch (Exception e) {
				logger.info("CallBackService.java :: LNO :: 58 Error while pushing contact center lead: ", e);
			}
		}
	}

	public void pushLead(ApplicationFormLead lead) throws JSONException, SQLException{		
		String responseMessage="FAILED";
		String cityName = "Other";
		String loanPurpose="";
		String responseStatus="F";
		String language = "0";
		try {
			logger.info("CallBackService.java :: LNO :: 71 pushLead");
			MasterCity cityMaster = commonService.getCityById(lead.getLeadCityId());
			logger.info("CallBackService.java :: LNO :: 73 pushLead :: cityMaster "+cityMaster);
			if (cityMaster!=null){
				cityName = cityMaster.getCityName();
			}	
			logger.info("CallBackService.java :: LNO :: 77 pushLead :: cityName "+cityName);

			Map<Integer,String> loanPurposeMap= new HashMap<Integer,String>();
			loanPurposeMap.put(1, "Home Loan");
			loanPurposeMap.put(2, "Home Top Up Loan");
			loanPurposeMap.put(3, "Auto Loan");
			loanPurposeMap.put(4, "Personal Loan: Salary account with SBI");  
			loanPurposeMap.put(6, "Rent Plus (Loan against rent receivables)"); 
			loanPurposeMap.put(7, "Loan against Immovable property"); 
			loanPurposeMap.put(8, "Scholar Loan"); 
			loanPurposeMap.put(9, "Global Ed-vantage"); 
			loanPurposeMap.put(11, "EL Takeover");
			loanPurposeMap.put(12, "Phone Banking");
			loanPurposeMap.put(13, "Bidya Lakhmi education loan");
			loanPurposeMap.put(16, "SME LOAN");
			loanPurposeMap.put(15, "AGRI LOAN");
			
			language = "1"+(lead.getLeadLanguageId()==null?"1":lead.getLeadLanguageId().toString());
			logger.info("CallBackService.java :: LNO :: 96 pushLead :: cityName "+cityName);
			loanPurpose= loanPurposeMap.get(lead.getLeadLoanPurposeId());
			ContactCenterWebserviceProxy proxyObj = new ContactCenterWebserviceProxy();
			logger.info("CallBackService.java :: LNO :: 95 pushLead :: proxyObj "+proxyObj);
			//logger.info("CallBackService.java :: LNO :: 96 pushLead :: Call Back Servive --  Mobile No : "+lead.getLeadMobileNo() +", Loan Purpose : "+loanPurpose+", FirstName :"+lead.getLeadFirstName()+", CityName :"+cityName+", WorkEmail :"+lead.getLeadWorkEmail());
			logger.info("CallBackService.java :: LNO :: 97 pushLead :: Contact-Center,end point: "+proxyObj.getEndpoint());
			logger.info("CallBackService.java :: LNO :: 98 pushLead :: Loan Purpose "+loanPurpose);
			if (loanPurpose!=null){
				//responseReturned = proxyObj.register_OCAS_INSERT_CCDB(lead.getLeadMobileNo(), loanPurpose, lead.getLeadFirstName(), cityName,Integer.parseInt(language) , lead.getLeadWorkEmail());
				responseReturned = proxyObj.register_OCAS_INSERT_CCDB(lead,loanPurpose,cityName,Integer.parseInt(language) );
				logger.info("CallBackService.java Lno :: 101 ::::::::: Response returned :: "+responseReturned);	 		
				if (responseReturned==1){
					responseStatus="S";
					responseMessage="SUCCESS";
				}
			}else{
				responseMessage="OCAS Error - Loan Purpose is not mapped";
			}
			logger.info("CallBackService.java Lno :: 109 :: CallBack Contact Center Response  "+responseReturned +" , STATUS "+responseStatus+ ", Message : "+responseMessage );
		} catch(RemoteException e) {
			responseMessage=e.getMessage();
			logger.info("CallBackService.java Lno :: 112 :: Exception while connecting contact center", e);
		}catch (Exception e) {
			responseMessage=e.getMessage();
			logger.info("CallBackService.java Lno :: 115 :: Exception while connecting contact center", e);
		}finally{
			if (loanPurpose!=null){
				logger.info("CallBackService.java Lno :: 124 :: before getting data");
				CallBackLeadPushLog logTable = commonService.findByLeadId(lead.getLeadId());
//				logger.info("CallBackService.java Lno :: 126 :: logTable :: "+logTable);
				if(logTable==null){
					logTable =new CallBackLeadPushLog();
				}
				
				//save consentId
				Integer consentId = 0;
				if (lead.getLeadConsentId()== null) {
					Consent consent = commonService.getConsentByLoanType(Constants.CALLBACK_ID);
					consentId = (consent!= null && consent.getConsentId() != null) ? consent.getConsentId() : null;
				} else {
					consentId = lead.getLeadConsentId();
				}
				
				logTable.setLeadId(lead.getLeadId());
				logTable.setLeadMobileNumber(lead.getLeadMobileNo());
				logTable.setLeadLoanPurpose(loanPurpose);
				logTable.setLeadFirstName(lead.getLeadFirstName());
				logTable.setLeadCityName(cityName);
				logTable.setLeadLanguageId(Integer.parseInt(language));
				logTable.setLeadWorkEmailId(lead.getLeadWorkEmail());
				logTable.setProductId(lead.getLeadProductTypeId());
				logTable.setLeadLoanPurposeId(lead.getLeadLoanPurposeId());
				logTable.setResponseStatus(responseStatus);
				logTable.setResponseDescription(responseMessage);
				logTable.setLeadConsentId(consentId);
				logTable.setLeadCreatedAt(new Date());
				//logger.info("CallBackService.java :: LNO :: 135 pushLead :: logTable before save "+logTable);
				logTable=commonService.save(logTable);
				//logger.info("CallBackService.java :: LNO :: 137 pushLead :: logTable after save "+logTable);
			}else{
				logger.info(lead.getLeadLoanPurposeId()+" Loan Purpose Id for "+lead.getLeadId()+" is not mapped for dialer");
			}
		}
	}
}	

