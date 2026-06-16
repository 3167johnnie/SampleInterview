gettimg this eroorwhile building  give code changes accordingly forthe above promt changes 



[INFO] --- maven-compiler-plugin:3.13.0:compile (default-compile) @ sbi ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 536 source files with javac [debug target 1.8] to target\classes
[INFO] /E:/Workspace New/ocas_repo_java/SBI/src/com/mintstreet/common/dao/MasterBranchDao.java: Some input files use unchecked or unsafe operations.
[INFO] /E:/Workspace New/ocas_repo_java/SBI/src/com/mintstreet/common/dao/MasterBranchDao.java: Recompile with -Xlint:unchecked for details.
[INFO] -------------------------------------------------------------
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /E:/Workspace New/ocas_repo_java/SBI/src/com/mintstreet/common/util/ConsentUtil.java:[49,70] method generateConsentWriteRequest in class com.mintstreet.consent.service.ConsentService cannot be applied to given types;
  required: java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String
  found: no arguments
  reason: actual and formal argument lists differ in length
[INFO] 1 error
[INFO] -------------------------------------------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  24.677 s





package com.mintstreet.common.util;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.consent.entity.ConsentPurposeLog;
import com.mintstreet.consent.entity.ConsentReadLog;
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;

public class ConsentUtil {

	@Autowired
	private ConsentService consentService;


	public void callCCMSPurposeEnquiryAPI() {
		try {
			// generate plain request
			ConsentPurposeLog purpose = consentService.generatePurposeRequest();

			// send request and read purpose from CCMS
			consentService.readPurposeFromCCMS(purpose);
		} catch (JSONException e) {
			e.printStackTrace();//TODO add loggers
		}

	}

	public void callCCMSConsentReadAPI() {
        
		try {
			// generate plain request
			ConsentReadLog consentRead = consentService.generateConsentReadRequest();
	
			// send request and consent read from CCMS
			consentService.consentReadFromCCMS(consentRead);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void callCCMSConsentWriteAPI() {
		
		try {
			// generate plain request
			ConsentWriteLog consentWrite = consentService.generateConsentWriteRequest();
			
			// send request and read purpose from CCMS
			consentService.writeConsentToCCMS(consentWrite);
		} catch (JSONException e) {
			e.printStackTrace();// TODO add loggers
		}

	}

}
