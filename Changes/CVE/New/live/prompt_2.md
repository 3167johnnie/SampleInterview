 public LoanScenarioBean processGetQuotes(Integer appSeqId, ApplicationFormHomeLoanQuote quote, Integer trackVisitId, String ajaxPostUrl, BankLmsUser bankLmsUser, Integer loanTypeId) {
    LoanScenarioBean loanScenarioBean = new LoanScenarioBean();
    try {
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      ApplicationFormHomeLoan application = null;
      if (appSeqId != null) {
        application = this.homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
        if (application == null) {
          loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
          return loanScenarioBean;
        } 
        if (application.getAppDataSourceId().equals(Constants.LEAD_DATA_SOURCE_ID_MOBILE_APP)) {
          if (quote.getLoanQuoteAppFirstName() != null) {
            application.setAppFirstName(quote.getLoanQuoteAppFirstName()); 
          }
          
          if (quote.getLoanQuotePanCardNo() != null) {
              application.setAppPanCardNo(quote.getLoanQuotePanCardNo()); 
            }
          
          if(quote.getLoanQuotePincode()!=null){
  			application.setAppPincode(quote.getLoanQuotePincode());
  		}
      	if(quote.getLoanQuoteAddress1()!=null){
			application.setAppAddress1(quote.getLoanQuoteAddress1());
		}
		if(quote.getLoanQuoteAddress2()!=null){
			application.setAppAddress2(quote.getLoanQuoteAddress2());
		}
		  if (quote.getLoanQuoteMiddleName() != null)
            application.setAppMiddleName(quote.getLoanQuoteMiddleName()); 
          if (quote.getLoanQuoteLastName() != null)
            application.setAppLastName(quote.getLoanQuoteLastName()); 
          if (quote.getLoanQuoteDateOfBirth() != null) {
            application.setAppDob(quote.getLoanQuoteDateOfBirth());
            application.setAppDobDT(DateUtil.changeDateFormatToDate(quote.getLoanQuoteDateOfBirth(), "MM/dd/yyyy"));
          } 
          if (quote.getLoanQuoteAppWorkEmail() != null)
            application.setAppWorkEmail(quote.getLoanQuoteAppWorkEmail()); 
          if (quote.getLoanQuoteAppMobileNo() != null) {
            application.setAppMobileNo(quote.getLoanQuoteAppMobileNo());
          } 
        } 
      } 
      int oldVisitId = 0;
      if (application != null && application.getAppQuoteId() != null) {
        oldVisitId = this.homeLoanService.getOldVisitId(application.getAppQuoteId()).intValue();
      } else {
        oldVisitId = trackVisitId.intValue();
      } 
      quote.setLoanQuoteIpAddress(this.SbiUtil.getIPAddress());
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      
      if (appSeqId == null) {
    	  
    	  if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
      		return loanScenarioBean;
      	}

      	if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
      		return loanScenarioBean;
      	}
      	if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
      		loanScenarioBean.setStatus(Integer.valueOf(0));
      		loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
      		return loanScenarioBean;
      	}
      }
      
      if (quote.getLoanQuoteProjectId() != null) {
        MasterProject masterProject = this.homeLoanService.findProjectById(quote.getLoanQuoteProjectId());
        if (masterProject != null)
          quote.setLoanQuoteBuilderName(masterProject.getProjectName()); 
      } 
      boolean isAppFoundForDedupInDropOffStage = false;
      boolean isAppFoundForDedupInDropRejectStage = false;
      if (!Constants.EKYC_BIOMETRIC_DEDUPE_BYPASS && (
        appSeqId == null || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_CBS.intValue() && "Y".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_NORMAL.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())) || (
        application != null && application.getAppSubTypeId().intValue() == Constants.APP_APP_SUB_TYPE_ID_EKYC.intValue() && "N".equalsIgnoreCase(application.getAppMobileVerified()) && !ValidatorUtil.isValid(application.getAppReferenceId())))) {
        String oldMobile = "";
        String isdCode = "";
        boolean isEligibleForBypass = false;
        if (appSeqId == null && quote.getLoanQuoteAppMobileNo() != null) {
          isdCode = (quote.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : quote.getAppISDCode();
          oldMobile = quote.getLoanQuoteAppMobileNo();
          isEligibleForBypass = true;
        } else if (application != null && application.getAppMobileNo() != null) {
          isdCode = (application.getAppISDCode() == null) ? Constants.COUNTRY_CODE_INDIA : application.getAppISDCode();
          oldMobile = application.getAppMobileNo();
          isEligibleForBypass = true;
        } 
        if (isEligibleForBypass && !Constants.DUMMY_MOBILE_NO.contains(oldMobile) && !Constants.APP_DUPLICATION_CHECK.equals("0"))
          if (SessionUtil.getApplicationType() != null && SessionUtil.getApplicationType().intValue() == 2) {
            boolean isLeadExists = false;
            int leadLoanPurposeId = 0;
            if (quote.getLoanQuoteLoanPurposeId().intValue() == 1 || quote.getLoanQuoteLoanPurposeId().intValue() == 2 || quote.getLoanQuoteLoanPurposeId().intValue() == 3 || quote.getLoanQuoteLoanPurposeId().intValue() == 4) {
              leadLoanPurposeId = 1;
            } else if (quote.getLoanQuoteLoanPurposeId().intValue() == 5) {
              leadLoanPurposeId = 2;
            } 
            isLeadExists = this.commonService.getLeadByProductTypeAndMobileNo(Constants.HOME_LOAN_ID, Integer.valueOf(leadLoanPurposeId), isdCode, oldMobile, loanTypeId);
            logger.info("HLProcessImpl.java LNo 934 :: isLeadExists setting in session " + isLeadExists + " with AppSeqId " + appSeqId);
            if (isLeadExists) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.INQUIRY_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 942:: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 949 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 951 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
          } else {
            boolean isAppFoundForDedupInApplicationStage = false;
            isAppFoundForDedupInApplicationStage = this.homeLoanService.isAppFoundForDedupInApplicationStage((application != null) ? application.getAppReferenceId() : null, isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 955 :: isAppFoundForDedupInApplicationStage " + isAppFoundForDedupInApplicationStage + " with AppSeqId " + appSeqId);
            if (isAppFoundForDedupInApplicationStage) {
              loanScenarioBean.setStatus(Integer.valueOf(0));
              loanScenarioBean.setMessage(Constants.APP_DEDUPLICATION_MESSAGE);
              return loanScenarioBean;
            } 
            isAppFoundForDedupInDropOffStage = this.homeLoanService.isAppFoundForDedupInDropOffStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 962 :: isAppFoundForDedupInDropOffStage " + isAppFoundForDedupInDropOffStage + " with AppSeqId " + appSeqId);
            isAppFoundForDedupInDropRejectStage = this.homeLoanService.isAppFoundForDedupInDropRejectStage(isdCode, oldMobile, quote.getLoanQuoteLoanPurposeId());
            logger.info("HLProcessImpl.java LNo 964 :: isAppFoundForDedupInDropRejectStage " + isAppFoundForDedupInDropRejectStage + " with AppSeqId " + appSeqId);
          }  
      } 
      


      //validations for address fields
      //loanQuoteAddress1
      
      
     if(!ValidatorUtil.validateFirstNameLength(quote.getLoanQuoteAppFirstName())) {
      loanScenarioBean.setStatus(Integer.valueOf(0));
   	  loanScenarioBean.setMessage("Please enter between 2 to 20 characters in First name.");
   	  return loanScenarioBean;
     }
     if(!ValidatorUtil.validateMiddleNameLength(quote.getLoanQuoteMiddleName())) {
    	 loanScenarioBean.setStatus(Integer.valueOf(0));
    	 loanScenarioBean.setMessage("Please enter between 2 to 20 characters in Middle name.");
    	 return loanScenarioBean;
     }
     if(!ValidatorUtil.validateLastNameLength(quote.getLoanQuoteLastName())) {
    	 loanScenarioBean.setStatus(Integer.valueOf(0));
    	 loanScenarioBean.setMessage("Please enter between 2 to 20 characters in Last name.");
    	 return loanScenarioBean;
     }
  
  	if (!ValidatorUtil.validateFirstName(quote.getLoanQuoteAppFirstName())) {
	      
      loanScenarioBean.setStatus(Integer.valueOf(0));
	  loanScenarioBean.setMessage("First name is not in correct format. Please enter only [a-z].");
	  return loanScenarioBean;
  }
	if (!ValidatorUtil.validateMiddleName(quote.getLoanQuoteMiddleName())) {
		
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Middle name is not in correct format. Please enter only [a-z]. ");
		return loanScenarioBean;
	}
	if (!ValidatorUtil.validateLastName(quote.getLoanQuoteLastName())) {
		
		loanScenarioBean.setStatus(Integer.valueOf(0));
		loanScenarioBean.setMessage("Last name is not in correct format. Please enter only [a-z] & do not include spaces.");
		return loanScenarioBean;
	}

	//added for same name validation
	if (quote.getLoanQuoteAppFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || 
			(quote.getLoanQuoteMiddleName()!=null && 
				(quote.getLoanQuoteMiddleName().trim().equalsIgnoreCase(quote.getLoanQuoteLastName().trim()) || quote.getLoanQuoteAppFirstName().trim().equalsIgnoreCase(quote.getLoanQuoteMiddleName().trim())))) {
		
		  loanScenarioBean.setStatus(Integer.valueOf(0));
		  loanScenarioBean.setMessage("For Single name, Please avoid repetation of the name. Instead write FirstName-Your Name, Middlename-Son/daughter/wife of, last name-Applicable name.");
		  return loanScenarioBean;
	}
	
	if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress1().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 1");
    	  return loanScenarioBean;
      }
      //if (!(quote.getLoanQuoteAddress1() != null && quote.getLoanQuoteAddress1().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
      if (!(quote.getLoanQuoteAddress1() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress1()))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 1");
          return loanScenarioBean;
      }
   
      
      if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddress2().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in Address Line 2");
    	  return loanScenarioBean;
      }
      
      //if (!(quote.getLoanQuoteAddress2() != null && quote.getLoanQuoteAddress2().matches("[a-zA-Z0-9/,\\-\\s]+"))) {
      if (!(quote.getLoanQuoteAddress2() != null && ValidatorUtil.isAddressChecker(quote.getLoanQuoteAddress2()))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in Current Address Line 2");
          return loanScenarioBean;
      }
      
      if(quote.getLoanQuoteAddressLandmark() != null) {
      if (!ValidatorUtil.isAddress(quote.getLoanQuoteAddressLandmark().trim())) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Please enter between 3 to 40 characters in  Landmark");
    	  return loanScenarioBean;
       }
      }
      
      if(quote.getLoanQuoteAddressLandmark() != null) {
	      if (!quote.getLoanQuoteAddressLandmark().matches("[a-zA-Z0-9/,\\-\\s]+")) {
	    	  loanScenarioBean.setStatus(Integer.valueOf(0));
	          loanScenarioBean.setMessage("Please enter only [a-z,0-9,(,),-,/] in  Landmark  ");
	          return loanScenarioBean;
	      }
      }
      if (quote.getLoanQuoteStateId() == null || (quote.getLoanQuoteStateId() != null && quote.getLoanQuoteStateId() == 0)) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please select State");
          return loanScenarioBean;
      }
      
      if ((quote.getLoanQuoteStateId() != null && quote.getLoanQuoteStateId() != 0) && 
    		  (quote.getLoanQuoteCityId() == null || (quote.getLoanQuoteCityId() != null && quote.getLoanQuoteCityId() == 0))) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Please select City");
          return loanScenarioBean;
      }
      
      boolean isPinCodeValidForState = false;
      if (quote.getLoanQuotePincode() != null) {
    	  int pincodeInitial = quote.getLoanQuotePincode().intValue() / 10000;
		  String pinlastfix = quote.getLoanQuotePincode().toString().substring(3, 6);
		  MasterState state = this.commonService.getStateById((quote.getLoanQuoteStateId() != null) ? quote.getLoanQuoteStateId() : null);
		  if (state != null && state.getStatePinMinStartPrefix() != null && state.getStatePinMaxStartPrefix() != null) {
			  if (pincodeInitial >= state.getStatePinMinStartPrefix().intValue() && pincodeInitial <= state.getStatePinMaxStartPrefix().intValue() && !pinlastfix.equals("000")) {
				  isPinCodeValidForState = true;
		      } else {
		    	  isPinCodeValidForState = false;
		      }
		  }
      }

      if (!isPinCodeValidForState) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
          loanScenarioBean.setMessage("Entered pincode does not belong to entered state.");
          return loanScenarioBean;
      }


      if (quote.getLoanQuotePanCardNo() != null && !ValidatorUtil.isValidPanNo(quote.getLoanQuotePanCardNo() )) {
      	loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("PAN is not in correct format.|2");
			return loanScenarioBean;
		}
      if (quote.getLoanQuoteLoanAmountTaken() != null && !ValidatorUtil.isRequestedAmountLength(quote.getLoanQuoteLoanAmountTaken() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Requested loan Amount cannot be greater than 9 digits.|2");
    	  return loanScenarioBean;
      }
      if (quote.getLoanQuoteGender() != null && !ValidatorUtil.isGender(quote.getLoanQuoteGender() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Gender is not in correct format.|2");
    	  return loanScenarioBean;
      }
    
      if (quote.getAppMobile() != null && !ValidatorUtil.isValidMobile(quote.getAppMobile() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Mobile number is not in correct format.|2");
    	  return loanScenarioBean;
      }
      
      if (quote.getAppEmail() != null && !ValidatorUtil.isValidEmail(quote.getAppEmail() )) {
        	loanScenarioBean.setStatus(Integer.valueOf(0));
  			loanScenarioBean.setMessage("Email is not in correct format.|2");
  			return loanScenarioBean;
  		}
      
      if (quote.getLoanQuoteAppWorkEmail() != null && !ValidatorUtil.isValidEmail(quote.getLoanQuoteAppWorkEmail() )) {
    	  loanScenarioBean.setStatus(Integer.valueOf(0));
    	  loanScenarioBean.setMessage("Email is not in correct format.|2");
    	  return loanScenarioBean;
      }
     
      quote = this.homeLoanHelper.insertLoanQuote(quote, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : Constants.OTHER_USER_ID, trackVisitId);
      if (quote == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (quote != null && quote.getError() != null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(quote.getError());
        return loanScenarioBean;
      } 
      if (quote.getLoanQuoteId() == null || quote.getLoanQuoteId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      int previousQuoteId = (application != null && application.getAppQuoteId() != null) ? application.getAppQuoteId().intValue() : 0;
      application = this.homeLoanHelper.insertAppLoan(quote, application, (bankLmsUser != null && bankLmsUser.getLmsUserId() != null) ? bankLmsUser.getLmsUserId() : null, Integer.valueOf((bankLmsUser != null && bankLmsUser.getLmsUserIntermediaryId() != null) ? bankLmsUser.getLmsUserIntermediaryId().intValue() : 0));
      if (application == null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (application != null && application.getError() != null) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(application.getError());
        return loanScenarioBean;
      } 
      if (application.getAppSeqId() == null || application.getAppSeqId().intValue() == 0) {
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      } 
      if (quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 5) {
        if (Constants.HOME_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl)) {
          SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
        } else if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI) {
          SessionUtil.setHomeLoanTopupApplicationSequenceId(application.getAppSeqId());
        } else {
          SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
        } 
      } else {
        SessionUtil.setHomeLoanApplicationSequenceId(application.getAppSeqId());
      } 
      if (SessionUtil.getApplicationCRMLeadId() != null)
        application.setAppCRMLeadId(SessionUtil.getApplicationCRMLeadId()); 
      if (isAppFoundForDedupInDropRejectStage)
        application.setAppMobileDedup(Integer.valueOf(0)); 
      if (isAppFoundForDedupInDropOffStage)
        application.setAppMobileDedup(Integer.valueOf(1)); 
      quote = this.homeLoanService.getApplicationFromHomeLoanQuoteByQuoteId(quote.getLoanQuoteId());
      if (application.getAppQuoteId() != null) {
        if (Constants.BANK_ID.intValue() == Constants.BANK_ID_SBI && 
          quote.getLoanQuoteLoanPurposeId() != null && quote.getLoanQuoteLoanPurposeId().intValue() == 4 && 
          quote.getLoanQuotePrferredBranchId() != null) {
          MasterBranch branch = this.commonService.getBranchById(quote.getLoanQuotePrferredBranchId());
          if (branch != null && branch.getBranchIsRACPC() != null && branch.getBranchIsRACPC().intValue() == 0) {
            loanScenarioBean.setStatus(Integer.valueOf(0));
            loanScenarioBean.setMessage("No loan offers available for the inputs provided. Please call toll-free number provided at the bottom of this page, or contact our nearest branch.");
            return loanScenarioBean;
          } 
        } 
        logger.info("application.getAppMobileVerified()....:: " + application.getAppMobileVerified()); 
        
        if (application.getAppPrivacyConsentFlag() != null && !application.getAppPrivacyConsentFlag().equals("Y") 
	    		  && !(application.getAppMobileVerified() != null && application.getAppMobileVerified().equalsIgnoreCase("Y"))) {
        	boolean ccmsWriteStatus = writePrivacyConsentToCCMS(application, quote, loanScenarioBean, loanTypeId);
			//ccmsWriteStatus=true;
			SessionUtil.setConsentSubmitNTBHome("true");
			if (!ccmsWriteStatus) {
				return loanScenarioBean;
			}
        }

        loanScenarioBean = this.homeLoanHelper.callBRE(application, quote, bankLmsUser, Integer.valueOf(previousQuoteId), trackVisitId, ajaxPostUrl, true);
        if (loanScenarioBean.getStatus().intValue() != 1)
          return loanScenarioBean; 
        application = loanScenarioBean.getApplicationHL();
        if (Constants.HOME_LOAN_ACTION_DSR.equalsIgnoreCase(ajaxPostUrl) && 
          application != null && 
          SessionUtil.getApplicationCRMLeadId() != null) {
          CRMRequest crmRequest = new CRMRequest();
          crmRequest.setCrmLeadId(SessionUtil.getApplicationCRMLeadId());
          crmRequest.setReferenceNumber(application.getAppSeqId());
          crmRequest.setApplicantReferenceId(application.getAppReferenceId());
          crmRequest.setLoanTypeId(Constants.HOME_LOAN_ID);
        } 
        return loanScenarioBean;
      } 
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      return loanScenarioBean;
    } catch (SQLException e) {
        logger.info("HomeLoanProcessImpl.java LNo: 1386 :: processGetQuotes() ", e);
        loanScenarioBean.setStatus(Integer.valueOf(0));
        loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
        return loanScenarioBean;
      }  catch (ParseException e) {
      logger.info("HomeLoanProcessImpl.java LNo: 1386 :: processGetQuotes() ", e);
      loanScenarioBean.setStatus(Integer.valueOf(0));
      loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
      return loanScenarioBean;
    } 
  }


	private boolean writePrivacyConsentToCCMS(ApplicationFormHomeLoan application, ApplicationFormHomeLoanQuote quote,LoanScenarioBean loanScenarioBean, Integer loanTypeId) {

		try {
			if (application == null || quote == null) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage(Constants.SORRY_FOR_INCONVENIENCE);
				return false;
			}

			if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
				return false;
			}
			if (loanTypeId == null) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Unable to identify the loan type for consent.");
				return false;
			}
			if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
				return false;
			}

			if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean
						.setMessage("Invalid privacy language details. Please accept SBI Privacy Notice again.");
				return false;
			}

			String mobile = application.getAppMobileNo();
			String email = application.getAppWorkEmail();

			if (!ValidatorUtil.isValid(mobile)) {
				mobile = quote.getAppMobile();
			}

			if (!ValidatorUtil.isValid(email)) {
				email = quote.getAppEmail();
			}

			if (!ValidatorUtil.isValid(mobile)) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Mobile number is required for consent write.");
				return false;
			}

			String ipAddresss = this.SbiUtil.getIPAddress();
			  String ipAddress = ipAddresss.replace(",", "");


			ConsentWriteLog consentWrite = consentUtil.callCCMSConsentWriteAPI(quote.getQuoteNtbId(), mobile, email,
					ipAddress, quote.getQuotePrivacyLocale(), loanTypeId);


			if (consentWrite == null || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
					|| !"200".equalsIgnoreCase(consentWrite.getResponseCode()) || consentWrite.getConsentId() == null
					|| consentWrite.getConsentId().trim().isEmpty()
					|| !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
				return false;
			}
			
			String ccmsConsentId = consentWrite.getConsentId().trim();
			quote.setQuoteCcmsConsentId(ccmsConsentId);
			application.setAppCcmsConsentId(ccmsConsentId);
			quote = homeLoanService.save(quote);
			application = homeLoanService.save(application);

			if (quote == null || application == null) {
				loanScenarioBean.setStatus(Integer.valueOf(0));
				loanScenarioBean.setMessage("Consent was recorded, but application details could not be updated.");
				return false;
			}
			logger.info("CCMS consent ID saved in Quote and Application. " + "consentId: {}, quoteId: {}, appSeqId: {}, loanTypeId: {}", ccmsConsentId, quote.getLoanQuoteId(), application.getAppSeqId(), loanTypeId);
			
			//send SMS to NTB customer after submit consent
			String msgBody=communicationManagerImpl.setEmailBody(25, 0, Constants.MESSAGE_TYPE_SMS, 0);
			logger.info("msgBody11::" + msgBody);
			msgBody = SbiUtil.urlEncode(msgBody);
			String SMS_TEXT = null;
			if(Constants.COUNTRY_CODE_INDIA.equals(application.getAppISDCode())){
				SMS_TEXT=Constants.SMS_STRING_INDIAN;
			}else{
				SMS_TEXT=Constants.SMS_STRING_NRI;
			}
			SMS_TEXT=SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);
			SMS_TEXT=SMS_TEXT.replaceAll("MOBILE_CODE", application.getAppISDCode()+mobile);
			SMS_TEXT=SMS_TEXT.replaceAll("LOAN_TYPE", "Home Loan");
			SMS_TEXT=SMS_TEXT.replaceAll("CONSENT_ID", quote.getQuoteNtbId());
			logger.info("SMS_TEXT5::" + SMS_TEXT);
			
			communicationManagerImpl.sendSms(SMS_TEXT);

			return true;

		} catch (Exception e) {
			logger.info("Exception while calling CCMS Write API", e);
			loanScenarioBean.setStatus(Integer.valueOf(0));
			loanScenarioBean.setMessage("Unable to write consent to CCMS. Please try again.");
			return false;
		}
	}
	 
	 
	 public ConsentReadResponse getConsentReadResponse(String cifNumber) {
	      //Call CCMS API for read consent
		ConsentReadResponse readResponse = consentUtil.callCCMSConsentReadAPI(cifNumber);
		return readResponse;
	 }
	
	 public ConsentWriteLog getConsentWriteLog(String cifNumber,String consentId) {
		logger.info("cif number "+cifNumber);
		return consentUtil.getConsentWriteLog(cifNumber,consentId);
	 }
	 public List<ConsentWriteLog> getConsentRevokeData(String cifNumber,String loanType) {
		 logger.info("cif number "+cifNumber);
		 return consentUtil.getConsentRevokeData(cifNumber,loanType);
	 }	 
	 
	 public ConsentWriteLog getConsentWriteLogByNtbIdAndConsentId(String ntbNumber,String consentId) {
		 logger.info("ntb number "+ntbNumber);
		 return consentUtil.getConsentWriteLogByNtbIdAndConsentId(ntbNumber,consentId);
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 for home loan we have call writePrivacyConsentToCCM() method inside processGetQuotes() , according for CVE we have do it this existing code for home loan 
		 I have give above method and calling methods as large class cannot be send .
		 accordingly need do but where the final submit is happening .
