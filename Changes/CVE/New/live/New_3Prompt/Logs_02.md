	public ConsentWriteLog generateConsentWriteRequest(String ntbId, String mobile, String email, String ipAddress, String locale, Integer loanTypeId) {
		if (loanTypeId == null) {
			throw new IllegalArgumentException(
					"Loan type ID is required for CCMS Consent Write."
			);
		}
		ConsentRequest request = new ConsentRequest();
		ConsentRequestEisPayload eisPayload = new ConsentRequestEisPayload();
		ConsentRequestPayloadHeaders payloadHeaders = new ConsentRequestPayloadHeaders();
		ConsentRequestPayloadBody payloadBody = new ConsentRequestPayloadBody();
		ConsentRequestDpData dpData = new ConsentRequestDpData();

		// ConsentRequestConsent consent1 = new ConsentRequestConsent();
		// ConsentRequestConsent consent2 = new ConsentRequestConsent();

		// List<ConsentRequestConsent> consents = new ArrayList<>();
		// List<String> consentedProducts = new ArrayList<>();

		String correlationId = UuidV7Generator.generateV7().toString();
		CCMSConfig config = commonService.getCcmsConfigById(1);

		if (locale == null || locale.trim().equals("")) {
			locale = "eng";
		}

		payloadHeaders.setAcceptLanguage(config.getAcceptLanguage());
		payloadHeaders.setxCorelationId(correlationId);
		payloadHeaders.setxApiVersion(config.getApiVersion());

		dpData.setDpCIF("");
		dpData.setNtbId(ntbId);
		dpData.setDpMobile(mobile);
		dpData.setDpEmail(email);
		dpData.setDpIPAddress(ipAddress);
		dpData.setLocale(locale);
		dpData.setTimestamp(getCurrentIsoTimestamp());

//		consent1.setPurposeCode("PR-FACILITA-000099");
//		consent1.setPurposeVersion("1");
//		consent1.setConsented("true");

//		PR-FACILITA-000099  1	(PR-REGULATO-000100, 1); (PR-INCIDENT-000101, 2); (PR-MANAGEYO-000102, 1);
//		(PR-SERVICEI-000077, 1); (PR-ANALYTIC-000105, 2);
//		(PR-PROMOTIO-000104, 1); (PR-PROMOTIO-000106, 3); (PR-USERPROF-000118, 2)"

//		consentedProducts.add("HOME_LOAN");

//		consent2.setPurposeCode("PURP-DEMOPURP-000071");

//		consent2.setPurposeCode("PR-PROMOTIO-000106");
//		consent2.setPurposeVersion("1");
//		consent2.setConsented("true");
//		consent2.setConsentedProducts(consentedProducts);

//		consents.add(consent1);
//		consents.add(consent2);

		ConsentResponsePurposeEnquiry purposeResponse;

		try {
			purposeResponse = getPurposeResponseForWrite();
		} catch (JSONException e) {
			throw new RuntimeException("Unable to get Purpose Enquiry response from CCMS.", e);
		}

		List<ConsentRequestConsent> consents = prepareConsentsFromPurposeResponse(purposeResponse);

		if (consents == null || consents.isEmpty()) {
			throw new RuntimeException("No purpose found from CCMS Purpose Enquiry response.");
		}

		payloadBody.setTouchPointId(config.getTouchPointId());
		payloadBody.setPurposeSetId(config.getPurposeSetId());
		payloadBody.setDpData(dpData);
		payloadBody.setConsents(consents);

		eisPayload.setHeaders(payloadHeaders);
		eisPayload.setBody(payloadBody);

		request.setSourceId(config.getSourceId());
		request.setEisPayload(eisPayload);
		request.setDestination(config.getDestination());
		request.setTransactionType(config.getTransactionType());
		request.setTransactionSubType("UPDATE_CONSENTS");

		JSONObject consentRequest = JSONUtil.beanObjectToJSONObjct(request);

//		ConsentWriteLog consentWrite = new ConsentWriteLog();
//		consentWrite.setxCorrelationId(correlationId);
//		consentWrite.setConsentWriteRequest(consentRequest.toString());
//		consentWrite.setNtbNumber(ntbId);
//		consentWrite.setRequestEntryTime(new Date());
//
//		consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
//
//		return consentWrite;
		
		ConsentWriteLog consentWrite = new ConsentWriteLog();

		consentWrite.setxCorrelationId(correlationId);
		consentWrite.setNtbNumber(ntbId);
		consentWrite.setConsentWriteRequest(consentRequest.toString());
		consentWrite.setRequestEntryTime(new Date());
		consentWrite.setLoanType(String.valueOf(loanTypeId));

		logger.info("Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, correlationId: {}, loanTypeId: {}", correlationId, ntbId, loanTypeId);
		logger.info("CCMS Consent Write request log saved. writeId: {}", consentWrite.getConsentWriteId());
		try {
			consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
		} catch (Exception e) {
			  logger.error("Exception while saving CCMS Consent Write request log", e);
			    throw new RuntimeException( "Unable to save CCMS Consent Write request log.", e);

		}

		if (consentWrite == null || consentWrite.getConsentWriteId() == null) {

			throw new RuntimeException ("Unable to save CCMS Consent Write request log.");
		}

		logger.info("CCMS Consent Write request log saved. writeId: {}, correlationId: {}", consentWrite.getConsentWriteId(), correlationId);

		return consentWrite;
	}
	
	
	
	
	
	
	
	
	2026-09-03 17:36:40,066 INFO  (CcmsUtil.java:138) - ERROR_DESCRIPTION:
2026-09-03 17:36:40,079 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-FACILITA-000099, purposeVersion: 4, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-REGULATO-000100, purposeVersion: 2, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-INCIDENT-000101, purposeVersion: 3, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-MANAGEYO-000102, purposeVersion: 2, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-SERVICEI-000077, purposeVersion: 1, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-ANALYTIC-000105, purposeVersion: 2, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-PROMOTIO-000104, purposeVersion: 1, consented: true, productSpecific: false, consentedProducts: null, notConsentedProducts: null
2026-09-03 17:36:40,080 INFO  (ConsentService.java:225) - Prepared CCMS consent. purposeCode: PR-PROMOTIO-000106, purposeVersion: 3, consented: true, productSpecific: true, consentedProducts: [CREDIT_CARD, WILL_TRUSTEESHIP_SERVICES, DEMAT_AND_SECURITIES, MUTUAL_FUND_AND_ASSET_MANAGEMENT, GENERAL_INSURANCE, LIFE_INSURANCE], notConsentedProducts: []
2026-09-03 17:36:40,081 INFO  (ConsentService.java:464) - Saving CCMS Consent Write request log. correlationId: 01a06729-fac8-75b8-be51-fe8c729766f2, ntbId: 3, correlationId: 24, loanTypeId: {}
2026-09-03 17:36:40,081 INFO  (ConsentService.java:465) - CCMS Consent Write request log saved. writeId: null
2026-09-03 17:36:40,089 INFO  (CveLoanAction.java:1318) - CveLoanAction.java :: Exception while calling CCMS Write API for CVE
java.lang.RuntimeException: Unable to save CCMS Consent Write request log.
        at com.mintstreet.consent.service.ConsentService.generateConsentWriteRequest(ConsentService.java:476) ~[classes/:?]
        at com.mintstreet.common.util.ConsentUtil.callCCMSConsentWriteAPI(ConsentUtil.java:86) ~[classes/:?]
        at com.mintstreet.loan.cveloan.action.CveLoanAction.writePrivacyConsentToCCMS(CveLoanAction.java:1264) ~[classes/:?]
        at com.mintstreet.loan.cveloan.action.CveLoanAction.getPersonalLoan(CveLoanAction.java:392) ~[classes/:?]
        at com.mintstreet.loan.cveloan.action.CveLoanAction.cveLoan(CveLoanAction.java:148) ~[classes/:?]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_502]
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_502]
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_502]
        at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_502]
        at ognl.OgnlRuntime.invokeMethod(OgnlRuntime.java:894) ~[ognl-3.1.12.jar:?]
        at ognl.OgnlRuntime.callAppropriateMethod(OgnlRuntime.java:1539) ~[ognl-3.1.12.jar:?]
        at ognl.ObjectMethodAccessor.callMethod(ObjectMethodAccessor.java:68) ~[ognl-3.1.12.jar:?]
        at com.opensymphony.xwork2.ognl.accessor.XWorkMethodAccessor.callMethodWithDebugInfo(XWorkMethodAccessor.java:96) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.ognl.accessor.XWorkMethodAccessor.callMethod(XWorkMethodAccessor.java:88) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at ognl.OgnlRuntime.callMethod(OgnlRuntime.java:1615) ~[ognl-3.1.12.jar:?]
        at ognl.ASTMethod.getValueBody(ASTMethod.java:91) ~[ognl-3.1.12.jar:?]
        at ognl.SimpleNode.evaluateGetValueBody(SimpleNode.java:212) ~[ognl-3.1.12.jar:?]
        at ognl.SimpleNode.getValue(SimpleNode.java:258) ~[ognl-3.1.12.jar:?]
        at ognl.Ognl.getValue(Ognl.java:467) ~[ognl-3.1.12.jar:?]
        at ognl.Ognl.getValue(Ognl.java:431) ~[ognl-3.1.12.jar:?]
        at com.opensymphony.xwork2.ognl.OgnlUtil$3.execute(OgnlUtil.java:351) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.ognl.OgnlUtil.compileAndExecuteMethod(OgnlUtil.java:403) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.ognl.OgnlUtil.callMethod(OgnlUtil.java:349) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.DefaultActionInvocation.invokeAction(DefaultActionInvocation.java:436) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.DefaultActionInvocation.invokeActionO
		
		
		
		
		
		above is the method give code changes for them this logs after for exception what the problem, give solution for it what changes step by step 
