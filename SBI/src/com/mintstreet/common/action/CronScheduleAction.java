package com.mintstreet.common.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.callback.service.CallBackService;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.service.CommonService;
import com.mintstreet.common.service.TaskExecutorService;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
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

public class CronScheduleAction extends BaseAction {
	private static final Logger logger = LogManager.getLogger(CronScheduleAction.class.getName());
	private static final long serialVersionUID = -7752185422262810447L;

	@Autowired
	private TaskExecutorService taskExecutorService;
	
	@Autowired
	private HomeLoanService homeLoanService;
	
	@Autowired
	private AutoLoanService autoLoanService;
	
	@Autowired
	private PersonalLoanService personalLoanService;
	
	@Autowired
	private EducationLoanService educationLoanService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CallBackService callBackService;
	
	public String startDate;
	public String endDate;
	
	public String execute(){
		return SUCCESS;
	}
	
	public String generateMissingPDF(){
		logger.info("CronScheduleAction.java :: LNo. 56 :: loanTypeId=>" + loanTypeId+" startDate=>" + startDate+" endDate=>" + endDate+" appSeqId=>"+appSeqId);
		try{
			List<Integer> appFormIDS = null;
			if(!ValidatorUtil.isValid(loanTypeId)){
				responseMessage="Error : Missing loanTypeId params ";
				return "jsonResponsePage";
			}else{
				if(ValidatorUtil.isValid(appSeqId)){
					appFormIDS = new ArrayList<Integer>(1);
					appFormIDS.add(appSeqId);
				}else if(ValidatorUtil.isValid(startDate) && ValidatorUtil.isValid(endDate)){
					appFormIDS = commonService.getApplicationForMissingPDF(loanTypeId, startDate, endDate);
				}else{
					responseMessage="Missing post params. Either loanTypeId, startDate or endDate missing ";
					return "jsonResponsePage";
				}
			}
			if(appFormIDS==null || appFormIDS.isEmpty()){
				responseMessage="No loan application exists by filtering post params.";
				return "jsonResponsePage";
			}
			if(loanTypeId == Constants.HOME_LOAN_ID){
				int noOfPdfGenerated=0;
				for(Integer appSeqId : appFormIDS){
					if(!ValidatorUtil.isValid(appSeqId)){
						continue;
					}
					ApplicationFormHomeLoan appForm = homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
					if(appForm==null){
						continue;
					}
					if(!ValidatorUtil.isValid(appForm.getAppQuoteId())){
						continue;
					}
					ApplicationFormHomeLoanQuote quote = homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote==null){
						continue;
					}
					taskExecutorService.generatePDFForHomeLoan(appForm, quote);
					noOfPdfGenerated+=1;
				}
				responseMessage = noOfPdfGenerated+" Home loan application pdf generated.";
				return "jsonResponsePage";
			}else if(loanTypeId == Constants.AUTO_LOAN_ID){
				int noOfPdfGenerated=0;
				for(Integer appSeqId : appFormIDS){
					if(!ValidatorUtil.isValid(appSeqId)){
						continue;
					}
					ApplicationFormAutoLoan appForm = autoLoanService.getApplicationFormAutoLoanByAppSeqId(appSeqId);
					if(appForm==null){
						continue;
					}
					if(!ValidatorUtil.isValid(appForm.getAppQuoteId())){
						continue;
					}
					ApplicationFormAutoLoanQuote quote = autoLoanService.getApplicationFormAutoLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote==null){
						continue;
					}
					taskExecutorService.generatePDFForAutoLoan(appForm, quote);
					noOfPdfGenerated+=1;
				}
				responseMessage = noOfPdfGenerated+" Auto loan application pdf generated.";
				return "jsonResponsePage";
			}else if(loanTypeId == Constants.PERSONAL_LOAN_ID){
				int noOfPdfGenerated=0;
				for(Integer appSeqId : appFormIDS){
					if(!ValidatorUtil.isValid(appSeqId)){
						continue;
					}
					ApplicationFormPersonalLoan appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
					if(appForm==null){
						continue;
					}
					if(!ValidatorUtil.isValid(appForm.getAppQuoteId())){
						continue;
					}
					ApplicationFormPersonalLoanQuote quote = personalLoanService.getApplicationFormPersonalLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote==null){
						continue;
					}
					taskExecutorService.generatePDFForPersonalLoan(appForm, quote);
					noOfPdfGenerated+=1;
				}
				responseMessage = noOfPdfGenerated+" Personal loan application pdf generated.";
				return "jsonResponsePage";
			}else if(loanTypeId == Constants.EDUCATION_LOAN_ID){
				int noOfPdfGenerated=0;
				for(Integer appSeqId : appFormIDS){
					if(!ValidatorUtil.isValid(appSeqId)){
						continue;
					}
					ApplicationFormEducationLoan appForm = educationLoanService.getApplicationFormEducationLoanByAppSeqId(appSeqId);
					if(appForm==null){
						continue;
					}
					if(!ValidatorUtil.isValid(appForm.getAppQuoteId())){
						continue;
					}
					ApplicationFormEducationLoanQuote quote = educationLoanService.getApplicationFormEducationLoanQuoteByQuoteId(appForm.getAppQuoteId());
					if(quote==null){
						continue;
					}
					taskExecutorService.generatePDFForEducationLoan(appForm, quote);
					noOfPdfGenerated+=1;
				}
				responseMessage = noOfPdfGenerated+" Education loan application pdf generated.";
				return "jsonResponsePage";
			}
		} catch(SQLException e){
			logger.info("CronScheduleAction.java LN 167 exception ::", e);
		} catch(Exception e){
			logger.info("CronScheduleAction.java LN 167 exception ::", e);
		}
		return "jsonResponsePage";
	}

	public String callMissingClickToCall(){
		logger.info("CronScheduleAction.java :: LNo. 178 :: startDate=>" + startDate+" endDate=>" + endDate+" :: appLeadId :: "+appLeadId);
		try{
			if(!ValidatorUtil.isValid(startDate) || !ValidatorUtil.isValid(endDate)){
				Date currentDate = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				endDate = formatter.format(currentDate);
				
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DATE, -1);
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Date previousDate = cal.getTime();
				startDate = formatter.format(previousDate);
			}
			List<Integer> leadIds = null;
			List<Integer> infronicsLeadIds = null;
			if(ValidatorUtil.isValid(appLeadId)){
				leadIds = new ArrayList<Integer>(1);
				leadIds.add(appLeadId);
			}else if(ValidatorUtil.isValid(startDate) && ValidatorUtil.isValid(endDate)){
				leadIds = commonService.getLeadForMissingClickToCall(startDate, endDate);
				infronicsLeadIds = commonService.getLeadForMissingClickToCallInfronics(startDate, endDate);
				if (infronicsLeadIds!=null && infronicsLeadIds.size()>0) {
					leadIds.addAll(infronicsLeadIds);
				}
			}else{
				responseMessage="Missing post params. Either startDate or endDate missing ";
				return "jsonResponsePage";
			}
			logger.info("CronScheduleAction.java LNo :: 190 leadIds ::"+leadIds);
			if(leadIds==null || leadIds.isEmpty()){
				responseMessage="No lead exists by filtering post params.";
				return "jsonResponsePage";
			}
			logger.info("CronScheduleAction.java LNo :: 195 leadIds.size() ::"+leadIds.size());
			for(Integer leadId : leadIds){
				ApplicationFormLead lead = commonService.getLeadById(leadId);
				if(lead==null){
					logger.info("CronScheduleAction.java LN 199 leadId = " +leadId);
				}
				logger.info("CronScheduleAction.java LN 201 pushLead start = " +leadId);
				callBackService.pushLead(lead);
				logger.info("CronScheduleAction.java LN 203 pushLead end = " +leadId);
			}
			responseMessage = "Click2Call service called for leadIds."+leadIds;
			return "jsonResponsePage";
		} catch(SQLException e){
			logger.info("CronScheduleAction.java LN 208 exception ::", e);
		} catch(Exception e){
			logger.info("CronScheduleAction.java LN 208 exception ::", e);
		}
		return "jsonResponsePage";
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	
}
