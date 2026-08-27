package com.mintstreet.common.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.mintstreet.common.entity.MasterQualification;
import com.mintstreet.common.entity.ValidateRequestConfig;
import com.mintstreet.common.service.ValidatorService;
import com.mintstreet.common.session.SessionUtil;
import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.SbiUtil;

public class ValidatorManager {
  private static final Logger logger = LogManager.getLogger(ValidatorManager.class.getName());
  
  @Autowired
  private ValidatorService validatorService;
  
  private boolean checkInput(Set<String> set, String str) {
    for (String val : set) {
      if (val.toUpperCase().contains(str.trim().toUpperCase()))
        return true; 
    }
    return false;
  }
  
  public ValidatorResponse isValidRquestList(String json, Integer requestIndex, Integer loanTypeId) {
    Map<String, String> errorMap = new HashMap<>();
    Map<String, String> requestMap = getMapObjectFromJSON(json);
    ValidatorResponse validatorResponse = new ValidatorResponse();
    validatorResponse.setStatus(true);
    validatorResponse.setErrorMessage(null);
    Integer appElTypeId = SessionUtil.getEducationTypeId();
    
    Boolean isAlternateMobile = false;
    if (requestMap.containsKey("alternateMobileNumber")) {
    	isAlternateMobile = true;
    }
	
    List<ValidateRequestConfig> validateRequestConfigList = null;
    try {
      if (requestIndex == null || requestIndex.intValue() == 0)
        requestIndex = Integer.valueOf(0); 
    
      //changed for alternate home loan 
      //validateRequestConfigList = this.validatorService.getValidateRequestConfig(requestIndex, loanTypeId);
      if (!isAlternateMobile) {
    	  validateRequestConfigList=validatorService.getValidateRequestConfig(requestIndex, loanTypeId);	
      } else {
    	  validateRequestConfigList=validatorService.getValidateRequestConfigForAlternateMobile(requestIndex, loanTypeId);
      }
      
      if (validateRequestConfigList == null) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else if (validateRequestConfigList.size() == 0) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else {
        int inputTagDataType = 0;
        int condmandatory = 0;
        int mandatory = 0;
        ArrayList<String> elementsName = new ArrayList<>();
       

        //new code for decrypting otp
//        logger.info("ValidatorManager.java LNo : 79:: requestMap " + requestMap);
        String inputotpenc = requestMap.get("inputOtpWantUs");
//        logger.info("ValidatorManager.java LNo : 79:: inputotpenc " + inputotpenc);
        
        if(inputotpenc !=null) {
        	SbiUtil sbiu=new SbiUtil();
			
			//for otp decrypt  in sep 2023
			String otp=sbiu.getDecryptedRequest(inputotpenc);
			
			//logger.info("DecryptedRequest OTP   6  "+otp);
			 //requestMap.get("inputOtpWantUs").replaceAll(otp, otp);
			requestMap.put("inputOtpWantUs", otp);
		//	 String in = requestMap.get("inputOtpWantUs");
        //    logger.info("ValidatorManager.java LNo ::: in 7 " + in);
        //    logger.info("ValidatorManager.java LNo ::: in 7 " + requestMap);
			
			// inputOtpWantUs=otp;
        }
        
        String inputotpenc2 = requestMap.get("inputOtp");
        //logger.info("ValidatorManager.java LNo : 79:: requestMap " + inputotpenc2);
        if(inputotpenc2 !=null) {
        	SbiUtil sbiu=new SbiUtil();
			
			//for otp decrypt  in sep 2023
			String otp=sbiu.getDecryptedRequest(inputotpenc2);
			
			//logger.info("DecryptedRequest OTP   105  "+otp);
			 //requestMap.get("inputOtpWantUs").replaceAll(otp, otp);
			requestMap.put("inputOtp", otp);
			 String in = requestMap.get("inputOtp");
           // logger.info("ValidatorManager.java LNo ::: in 109 " + in);
           // logger.info("ValidatorManager.java LNo ::: in 109 " + requestMap);
			
			// inputOtpWantUs=otp;
        }
        String ntbNumber = requestMap.get("ntbNumber");
        
        logger.info("ntbNumber "+ntbNumber);
        if(ntbNumber !=null) {
        	SessionUtil.setCheckRevokeNTB("1");
        	logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }else {
        	SessionUtil.setCheckRevokeNTB(null);
           //logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }
        
        logger.info("SessionUtil.getConsentRevokeNTB() "+SessionUtil.getConsentRevokeNTB());
        if(SessionUtil.getConsentRevokeNTB()!=null) {
        	SessionUtil.setCheckRevokeNTB("1");
        	logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }
        
        
        for (ValidateRequestConfig validateRequestConfigObject : validateRequestConfigList) {
          String inputTagid = validateRequestConfigObject.getReqValiName().replaceAll("cbs\\.", "").replaceAll("lead\\.", "").replaceAll("quote\\.", "").replaceAll("appForm\\.", "");
          inputTagid = inputTagid.trim();
          elementsName.add(inputTagid);
          condmandatory = validateRequestConfigObject.getReqValiIsCondition().intValue();
          mandatory = validateRequestConfigObject.getReqValiIsManadat().intValue();
          Set<String> keySet = requestMap.keySet();
          
          if (ValidatorUtil.isValid(inputTagid) && checkInput(keySet, inputTagid)) {
            String inputTagidValueArray = requestMap.get(inputTagid);
            if (ValidatorUtil.isValid(inputTagidValueArray)) {
            } else {
            }
            try {
              if (validateRequestConfigObject.getLoanTypeId() != null && validateRequestConfigObject.getLoanTypeId().intValue() == 0) {
                if (requestMap.containsKey("leadApplyingFrom") && requestMap.get("leadApplyingFrom") != null) {
                  String leadApplyingFrom = requestMap.get("leadApplyingFrom");
                  int applyingFrom = 0;
                  if (ValidatorUtil.isValid(leadApplyingFrom)) {
                    applyingFrom = Integer.parseInt(leadApplyingFrom);
                  } 
                  if (applyingFrom == 2) {
                    String inputTagidValue = requestMap.get("leadMobileNo");
                    if (inputTagDataType == Constants.DATA_TYPE_NRIMOBILE && !ValidatorUtil.isValidNRIMobile(inputTagidValue) && 
                      mandatory == 1) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      logger.info("ValidatorManager.java LNo : 75 : Server Side Validation failed for NRI-Mobile:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                      break;
                    } 
                  } 
                } 
          //      logger.info("ValidatorManager.java LNo : 97:: requestMap " + requestMap);
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            }  catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if ("loanQuoteCourseTypeId".equalsIgnoreCase(inputTagid) && 
                requestMap.containsKey("loanQuoteLastEducationalQualificationId")) {
                String lastQualificationId = requestMap.get("loanQuoteLastEducationalQualificationId");
                String LQID = "";
                String courseTypeId = "";
                if (ValidatorUtil.isValid(lastQualificationId))
                  LQID = lastQualificationId; 
                if (ValidatorUtil.isValid(inputTagidValueArray))
                  courseTypeId = inputTagidValueArray; 
                if (!validateApplicantLastQualification(courseTypeId, LQID)) {
                  errorMap.put("loanQuoteLastEducationalQualificationId", "Last qualification is not eligible for this course type");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if (appElTypeId == Constants.APP_EL_TYPE_ID_SCHOLAR && 
                "loanQuoteInstituteNameId".equalsIgnoreCase(inputTagid)) {
                String scholarInstitute = requestMap.get("loanQuoteInstituteNameId");
                String scholarInstituteName = "";
                if (ValidatorUtil.isValid(scholarInstitute))
                  scholarInstituteName = scholarInstitute; 
                if (ValidatorUtil.isValid(inputTagidValueArray))
                  scholarInstituteName = inputTagidValueArray; 
                logger.info("ValidatorManager.java LNo : 114:: Institute Name is " + scholarInstituteName);
                if (!ValidatorUtil.isValid(scholarInstituteName)) {
                  errorMap.put("loanQuoteInstituteNameId", "Scholar Institute Name not in Correct format ");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            }  catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if ("appPanCardNo".equalsIgnoreCase(inputTagid) && 
                requestMap.containsKey("appCoapplicantPanCardNo")) {
                String coApplicantPancardArray = requestMap.get("appCoapplicantPanCardNo");
                String coApplicantPancardNo = "";
                String applicantPancardNo = "";
                if (ValidatorUtil.isValid(coApplicantPancardArray))
                  coApplicantPancardNo = coApplicantPancardArray; 
                if (ValidatorUtil.isValid(inputTagidValueArray))
                  applicantPancardNo = inputTagidValueArray; 
                if (!validateApplicantAndCoApplicantPanCard(applicantPancardNo, coApplicantPancardNo)) {
                  errorMap.put("appPanCardNo", "applicant pancard cannot be same as coApplicant pancard ");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 107 : Erron in validating pancard of Applicant and CoApplicant in education Loan " + e);
            } catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 107 : Erron in validating pancard of Applicant and CoApplicant in education Loan " + e);
            } 
            if (ValidatorUtil.isValid(inputTagidValueArray)) {
              String inputTagidValue = inputTagidValueArray;
              inputTagDataType = validateRequestConfigObject.getElementDateTypeId().intValue();
              if (inputTagDataType == Constants.DATA_TYPE_INTEGER) {
                if (inputTagidValue != null) {
                  if (ValidatorUtil.isValid(inputTagidValue)) {
                    Long inputTagidInteger = null;
                    inputTagidValue = inputTagidValue.replaceAll(",", "");
                    if (RegexFunctions.checkForNumber(inputTagidValue) == 0)
                        inputTagidInteger = Long.valueOf(Long.parseLong(inputTagidValue)); 
                    if (RegexFunctions.checkForNumber(inputTagidValue) != 0) {
                      if (mandatory == 1) {
                        logger.info("ValidatorManager.java LNo : 127 : Server Side Validation failed for mandatory one :::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                        errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                        validatorResponse.setErrorMessage(errorMap);
                        validatorResponse.setStatus(false);
                        break;
                      } 
                      continue;
                    } 
                    if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidInteger, requestIndex)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      logger.info("ValidatorManager.java LNo : 137 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                      break;
                    }
                  } 
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_DATE) {
                boolean error = false;
                if (mandatory == 1) {
                  if (!ValidatorUtil.dateValidation(inputTagidValue))
                    error = true; 
                } else if (inputTagidValue != null && inputTagidValue.trim().length() > 0 && !ValidatorUtil.dateValidation(inputTagidValue)) {
                  error = true;
                } 
                if (error) {
                  logger.info("ValidatorManager.java LNo : 142 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_STRING) {
                if (mandatory == 1) {
                  if (RegexFunctions.checkForString(inputTagidValue) != 0) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 153 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                  if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidValue, requestIndex)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 159 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                  continue;
                } 
                if (ValidatorUtil.isValid(inputTagidValue)) {
                  if (RegexFunctions.checkForString(inputTagidValue) != 0) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 168 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                  if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidValue, requestIndex)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 174 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_DOUBLE) {
                if (ValidatorUtil.isValid(inputTagidValue)) {
                  inputTagidValue = inputTagidValue.replaceAll(",", "");
                  Double inputTagidDouble = null;
                  if (RegexFunctions.checkForNumberDouble(inputTagidValue) == 0)
                      inputTagidDouble = Double.valueOf(Double.parseDouble(inputTagidValue)); 
                  if (RegexFunctions.checkForNumberDouble(inputTagidValue) != 0) {
                    if (mandatory == 1) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      logger.info("ValidatorManager.java LNo : 198 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                      break;
                    } 
                    continue;
                  } 
                  if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidDouble, requestIndex)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 206 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_MOBILE && !ValidatorUtil.isValidMobile(inputTagidValue)) {
                if (mandatory == 1) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  logger.info("ValidatorManager.java LNo : 215 : Server Side Validation failed for Mobile:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_NRIMOBILE && !ValidatorUtil.isValidNRIMobile(inputTagidValue)) {
                if (mandatory == 1) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  logger.info("ValidatorManager.java LNo : 223 : Server Side Validation failed for NRI-Mobile:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_AADHAAR_NUMBER) {
                if (ValidatorUtil.isValid(inputTagidValue) && 
                  !ValidatorUtil.isValidAadhaarNo(inputTagidValue)) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  logger.info("ValidatorManager.java LNo : 223 : Server Side Validation failed for NRI-Mobile:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_EMAIL) {
                if (mandatory == 1) {
                  if (!ValidatorUtil.isValidEmail(inputTagidValue)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    logger.info("ValidatorManager.java LNo : 231 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                    break;
                  } 
                  continue;
                } 
                if (inputTagidValue != null && inputTagidValue != "" && !ValidatorUtil.isValidEmail(inputTagidValue)) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  logger.info("ValidatorManager.java LNo : 231 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_PAN && !ValidatorUtil.isValidPanNo(inputTagidValue)) {
                if (condmandatory == 0 && mandatory == 1) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  logger.info("ValidatorManager.java LNo : 240 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                  break;
                } 
                continue;
              } 
              if (inputTagDataType == Constants.DATA_TYPE_PIN && !ValidatorUtil.isValidPin(inputTagidValue) && 
                mandatory == 1) {
                errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                validatorResponse.setErrorMessage(errorMap);
                validatorResponse.setStatus(false);
                logger.info("ValidatorManager.java LNo : 249 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
                break;
              } 
            } 
            continue;
          } 
         //if(ntbNumber == null && SessionUtil.getCheckRevokeNTB()==null){
         // logger.info("ValidatorManager.java LNo :  for  SessionUtil.getConsentRevokeNTB() " +SessionUtil.getConsentRevokeNTB() );
         if(SessionUtil.getCheckRevokeNTB()==null ){
          if (condmandatory == 0 && mandatory == 1) {
            logger.info("ValidatorManager.java LNo : 257 : Server Side Validation failed for  " + validateRequestConfigObject.getReqValiName() + "::::::: as mentioned field  should be in request");
            errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
            validatorResponse.setErrorMessage(errorMap);
            validatorResponse.setStatus(false);
            break;
          	} 
          }
        } 
      } 
    } catch (NullPointerException e) {
        logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
    } catch (Exception e) {
      logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
    } 
    
    logger.info("validatorResponse isStatus : " + validatorResponse.isStatus());
    logger.info("validatorResponse getErrorMessage : " + validatorResponse.getErrorMessage());
    return validatorResponse;
  }
  
  public ValidatorResponse isValidRquestList(HttpServletRequest request, Integer requestIndex, Integer loanTypeId) {
    Map<String, String> errorMap = new HashMap<>();
    Map<String, String[]> requestMap = request.getParameterMap();
    ValidatorResponse validatorResponse = new ValidatorResponse();
    validatorResponse.setStatus(true);
    validatorResponse.setErrorMessage(null);
    Integer appElTypeId = SessionUtil.getEducationTypeId();
    List<ValidateRequestConfig> validateRequestConfigList = null;
    try {
      if (requestIndex == null || requestIndex.intValue() == 0)
        requestIndex = Integer.valueOf(0); 
      validateRequestConfigList = this.validatorService.getValidateRequestConfig(requestIndex, loanTypeId);
      if (validateRequestConfigList == null) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else if (validateRequestConfigList.size() == 0) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else {
        int inputTagDataType = 0;
        int condmandatory = 0;
        int mandatory = 0;
        ArrayList<String> elementsName = new ArrayList<>();
		outerloop:
        for (ValidateRequestConfig validateRequestConfigObject : validateRequestConfigList) {
          String inputTagid = validateRequestConfigObject.getReqValiName();
          inputTagid = inputTagid.trim();
          elementsName.add(inputTagid);
          condmandatory = validateRequestConfigObject.getReqValiIsCondition().intValue();
          mandatory = validateRequestConfigObject.getReqValiIsManadat().intValue();
          if (ValidatorUtil.isValid(inputTagid) && requestMap.containsKey(inputTagid)) {
            String[] inputTagidValueArray = requestMap.get(inputTagid);
           // if (inputTagidValueArray != null && inputTagidValueArray.length != 0)
              // inputTagidValueArray[0] ; 
            if(inputTagidValueArray != null && inputTagidValueArray.length !=0 && inputTagidValueArray[0] != ""){

			}else{

			}
            
            try {
              if (validateRequestConfigObject.getLoanTypeId() != null && validateRequestConfigObject.getLoanTypeId().intValue() == 0 && 
                requestMap.containsKey("leadApplyingFrom") && requestMap.get("leadApplyingFrom") != null) {
                String[] leadApplyingFrom = requestMap.get("leadApplyingFrom");
                int applyingFrom = 0;
                if (leadApplyingFrom != null && leadApplyingFrom.length != 0 && leadApplyingFrom[0] != "")
                  applyingFrom = Integer.parseInt(leadApplyingFrom[0]); 
                if (applyingFrom == 2) {
                  String inputTagidValue = request.getParameter("leadMobileNo");
                  if (inputTagDataType == Constants.DATA_TYPE_NRIMOBILE && !ValidatorUtil.isValidNRIMobile(inputTagidValue) && 
                    mandatory == 1) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break;
                  } 
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
             }  catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if ("loanQuoteCourseTypeId".equalsIgnoreCase(inputTagid) && 
                requestMap.containsKey("loanQuoteLastEducationalQualificationId")) {
                String[] lastQualificationId = requestMap.get("loanQuoteLastEducationalQualificationId");
                String LQID = "";
                String courseTypeId = "";
                if (lastQualificationId != null && lastQualificationId.length != 0 && lastQualificationId[0] != "")
                  LQID = lastQualificationId[0]; 
                if (inputTagidValueArray != null && inputTagidValueArray.length != 0 && inputTagidValueArray[0] != "")
                  courseTypeId = inputTagidValueArray[0]; 
                if (!validateApplicantLastQualification(courseTypeId, LQID)) {
                  errorMap.put("loanQuoteLastEducationalQualificationId", "Last qualification is not eligible for this course type");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
             } catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if (appElTypeId == Constants.APP_EL_TYPE_ID_SCHOLAR && 
                "loanQuoteInstituteNameId".equalsIgnoreCase(inputTagid)) {
                String[] scholarInstitute = requestMap.get("loanQuoteInstituteNameId");
                String scholarInstituteName = "";
                if (scholarInstitute != null && scholarInstitute.length != 0 && scholarInstitute[0] != "")
                  scholarInstituteName = scholarInstitute[0]; 
                if (inputTagidValueArray != null && inputTagidValueArray.length != 0 && inputTagidValueArray[0] != "")
                  scholarInstituteName = inputTagidValueArray[0]; 
                if (!ValidatorUtil.isValid(scholarInstituteName)) {
                  errorMap.put("loanQuoteInstituteNameId", "Scolar Institute Name not in Correct format ");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
             } catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 81:: Erron in validating course Type Id " + e);
            } 
            try {
              if ("appPanCardNo".equalsIgnoreCase(inputTagid) && 
                requestMap.containsKey("appCoapplicantPanCardNo")) {
                String[] coApplicantPancardArray = requestMap.get("appCoapplicantPanCardNo");
                String coApplicantPancardNo = "";
                String applicantPancardNo = "";
                if (coApplicantPancardArray != null && coApplicantPancardArray.length != 0 && coApplicantPancardArray[0] != "")
                  coApplicantPancardNo = coApplicantPancardArray[0]; 
                if (inputTagidValueArray != null && inputTagidValueArray.length != 0 && inputTagidValueArray[0] != "")
                  applicantPancardNo = inputTagidValueArray[0]; 
                if (!validateApplicantAndCoApplicantPanCard(applicantPancardNo, coApplicantPancardNo)) {
                  errorMap.put("appPanCardNo", "applicant pancard cannot be same as coApplicant pancard ");
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break;
                } 
              } 
            } catch (NullPointerException e) {
                logger.info("ValidatorManager.java LNo : 107 : Erron in validating pancard of Applicant and CoApplicant in education Loan " + e);
            } catch (Exception e) {
              logger.info("ValidatorManager.java LNo : 107 : Erron in validating pancard of Applicant and CoApplicant in education Loan " + e);
            } 
            if (inputTagidValueArray != null && inputTagidValueArray.length > 0) {
              byte b;
              int i;
              String[] arrayOfString;
              for (i = (arrayOfString = inputTagidValueArray).length, b = 0; b < i; ) {
                String inputTagidValue = arrayOfString[b];
                inputTagDataType = validateRequestConfigObject.getElementDateTypeId().intValue();
                if (inputTagDataType == Constants.DATA_TYPE_INTEGER) {
                  if (ValidatorUtil.isValid(inputTagidValue)) {
                    Long inputTagidInteger = null;
                    inputTagidValue = inputTagidValue.replaceAll(",", "");
                    if (RegexFunctions.checkForNumber(inputTagidValue) == 0)
                        inputTagidInteger = Long.valueOf(Long.parseLong(inputTagidValue));
                    if (RegexFunctions.checkForNumber(inputTagidValue) != 0) {
                      if (mandatory == 1) {
                        errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                        validatorResponse.setErrorMessage(errorMap);
                        validatorResponse.setStatus(false);
                        break outerloop;
                      } 
                    } else if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidInteger, requestIndex)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_DATE) {
                  boolean error = false;
                  if (mandatory == 1) {
                    if (!ValidatorUtil.dateValidation(inputTagidValue))
                      error = true; 
                  } else if (inputTagidValue != null && inputTagidValue.trim().length() > 0 && !ValidatorUtil.dateValidation(inputTagidValue)) {
                    error = true;
                  } 
                  if (error) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_STRING) {
                  if (mandatory == 1) {
                    if (RegexFunctions.checkForString(inputTagidValue) != 0) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                    if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidValue, requestIndex)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                  } else if (ValidatorUtil.isValid(inputTagidValue)) {
                    if (RegexFunctions.checkForString(inputTagidValue) != 0) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                    if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidValue, requestIndex)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_DOUBLE) {
                  if (ValidatorUtil.isValid(inputTagidValue)) {
                    inputTagidValue = inputTagidValue.replaceAll(",", "");
                    Double inputTagidDouble = null;
                    if (RegexFunctions.checkForNumberDouble(inputTagidValue) == 0)                  
                        inputTagidDouble = Double.valueOf(Double.parseDouble(inputTagidValue));  
                    if (RegexFunctions.checkForNumberDouble(inputTagidValue) != 0) {
                      if (mandatory == 1) {
                        errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                        validatorResponse.setErrorMessage(errorMap);
                        validatorResponse.setStatus(false);
                        break outerloop;
                      } 
                    } else if (!ValidateInputTagVale(validateRequestConfigObject, inputTagidDouble, requestIndex)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_MOBILE && !ValidatorUtil.isValidMobile(inputTagidValue)) {
                  if (mandatory == 1) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_NRIMOBILE && !ValidatorUtil.isValidNRIMobile(inputTagidValue)) {
                  if (mandatory == 1) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_AADHAAR_NUMBER) {
                  if (ValidatorUtil.isValid(inputTagidValue) && 
                    !ValidatorUtil.isValidAadhaarNo(inputTagidValue)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_EMAIL) {
                  if (mandatory == 1) {
                    if (!ValidatorUtil.isValidEmail(inputTagidValue)) {
                      errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                      validatorResponse.setErrorMessage(errorMap);
                      validatorResponse.setStatus(false);
                      break outerloop;
                    } 
                  } else if (inputTagidValue != null && inputTagidValue != "" && !ValidatorUtil.isValidEmail(inputTagidValue)) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_PAN && !ValidatorUtil.isValidPanNo(inputTagidValue)) {
                  if (condmandatory == 0 && mandatory == 1) {
                    errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                    validatorResponse.setErrorMessage(errorMap);
                    validatorResponse.setStatus(false);
                    break outerloop;
                  } 
                } else if (inputTagDataType == Constants.DATA_TYPE_PIN && !ValidatorUtil.isValidPin(inputTagidValue) && 
                  mandatory == 1) {
                  errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
                  validatorResponse.setErrorMessage(errorMap);
                  validatorResponse.setStatus(false);
                  break outerloop;
                } 
                b++;
              } 
            } 
            continue;
          } 
          if (condmandatory == 0 && mandatory == 1) {
            logger.info("ValidatorManager.java LNo : 257 : Server Side Validation failed for  " + validateRequestConfigObject.getReqValiName() + "::::::: as mentioned field  should be in request");
            errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
            validatorResponse.setErrorMessage(errorMap);
            validatorResponse.setStatus(false);
            break;
          } 
        } 
      } 
    } catch (NullPointerException e) {
        logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
    }  catch (Exception e) {
      logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
    } 
    return validatorResponse;
  }
  
  public Map<String, String> getMapObjectFromJSON(String json) {
    try {
      JSONObject jsonObject = new JSONObject(json);
      Map<String, String> map = jsonToMap(jsonObject);
      return map;
    } catch (JSONException e) {
        logger.info("ValidatorManager.java LNo : 387 : Inside the exception " + e);
        return null;
      } catch (Exception e) {
      logger.info("ValidatorManager.java LNo : 387 : Inside the exception " + e);
      return null;
    } 
  }
  
  public static Map<String, String> jsonToMap(JSONObject json) throws JSONException {
    Map<String, String> retMap = new HashMap<>();
    if (json != JSONObject.NULL)
      retMap = toMap(json); 
    return retMap;
  }
  
  public static Map<String, String> toMap(JSONObject object) throws JSONException {
    Map<String, String> map = new HashMap<>();
    Iterator<String> keysItr = object.keys();
    while (keysItr.hasNext()) {
      String key = keysItr.next();
      Object value = (Object)object.get(key);
      if (value instanceof JSONArray) {
        value = (Object)toList((JSONArray)value);
      } else if (value instanceof JSONObject) {
        value = (Object)toMap((JSONObject)value);
      } 
      map.put(key, value.toString());
    } 
    return map;
  }
  
  public static List<Object> toList(JSONArray array) throws JSONException {
    List<Object> list = new ArrayList();
    for (int i = 0; i < array.length(); i++) {
      Object value = (Object)array.get(i);
      if (value instanceof JSONArray) {
        value = (Object)toList((JSONArray)value);
      } else if (value instanceof JSONObject) {
        value = (Object)toMap((JSONObject)value);
      } 
      list.add(value);
    } 
    return list;
  }
  
  public ValidatorResponse isValidRquestListMobile(String json, Integer requestIndex, Integer loanTypeId, Integer requestValidationType) {
    Map<String, String> errorMap = new HashMap<>();
    Map<String, String> requestMap = getMapObjectFromJSON(json);
    ValidatorResponse validatorResponse = new ValidatorResponse();
    validatorResponse.setStatus(true);
    validatorResponse.setErrorMessage(null);
    List<ValidateRequestConfig> validateRequestConfigList = null;
    try {
      if (requestIndex == null || requestIndex.intValue() == 0)
        requestIndex = Integer.valueOf(0); 
      validateRequestConfigList = this.validatorService.getAllValidateRequestConfigByRequestIndexMobile(requestIndex, loanTypeId, requestValidationType);
      if (validateRequestConfigList == null) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else if (validateRequestConfigList.size() == 0) {
        validatorResponse.setStatus(true);
        validatorResponse.setErrorMessage(null);
      } else {
        for (ValidateRequestConfig valiReq : validateRequestConfigList) {
          String inputTagid = valiReq.getReqValiName();
          inputTagid = inputTagid.trim();
          String inputTagidValue = null;
          boolean needToCheckElement = false;
          boolean isValidElementValue = true;
          if (ValidatorUtil.isValid(inputTagid) && requestMap.containsKey(inputTagid))
            inputTagidValue = String.valueOf(requestMap.get(inputTagid)); 
          if (valiReq.getReqValiIsManadat() != null)
            if (valiReq.getReqValiIsManadat().intValue() == 1) {
              if (inputTagidValue != null && inputTagidValue != "") {
                needToCheckElement = true;
              } else {
                needToCheckElement = false;
                isValidElementValue = false;
              } 
            } else if (valiReq.getReqValiIsManadat().intValue() == 0 && 
              ValidatorUtil.isValid(inputTagidValue)) {
              needToCheckElement = true;
            }  
          logger.info("ValidatorManager.java LNo : 415 : KEY :: " + inputTagid + " VALUE :: " + inputTagidValue);
          if (needToCheckElement)
            if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_AADHAAR_NUMBER) {
              if (!ValidatorUtil.isValidAadhaarNo(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_DATE) {
              if (!ValidatorUtil.isValidDate(inputTagidValue, "MM/dd/yyyy"))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_DOUBLE) {
              boolean isValidDouble = true;
              inputTagidValue = inputTagidValue.replaceAll(",", "");
              Long inputTagidDouble = null;
              RegexFunctions.checkForNumberDouble(inputTagidValue);
                inputTagidDouble = Long.valueOf((long)Double.parseDouble(inputTagidValue));
              if (isValidDouble)
                if (RegexFunctions.checkForNumberDouble(inputTagidDouble.toString()) != 0) {
                  isValidDouble = false;
                } else if (!ValidateInputTagVale(valiReq, inputTagidDouble, requestIndex)) {
                  isValidDouble = false;
                }  
              if (!isValidDouble)
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_EMAIL) {
              if (!ValidatorUtil.isValidEmail(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_INTEGER) {
              boolean isValidInteger = true;
              Long inputTagidInteger = null;
              inputTagidValue = inputTagidValue.replaceAll(",", "");
              try {
                inputTagidInteger = Long.valueOf((long)Double.parseDouble(inputTagidValue));
              } catch (NullPointerException e) {
                  logger.info("ValidatorManager.java LNo : 121 : Number Format Exception in Validation Manager", e);
                  isValidInteger = false;
                } catch (Exception e) {
                logger.info("ValidatorManager.java LNo : 121 : Number Format Exception in Validation Manager", e);
                isValidInteger = false;
              } 
              if (isValidInteger)
                if (RegexFunctions.checkForNumber(inputTagidInteger.toString()) != 0) {
                  isValidInteger = false;
                } else if (!ValidateInputTagVale(valiReq, inputTagidInteger, requestIndex)) {
                  isValidInteger = false;
                }  
              if (!isValidInteger)
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_MOBILE) {
              if (!ValidatorUtil.isValidMobile(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_NRIMOBILE) {
              if (!ValidatorUtil.isValidNRIMobile(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_PAN) {
              if (!ValidatorUtil.isValidPanNo(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_PIN) {
              if (!ValidatorUtil.isValidPin(inputTagidValue))
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_STRING) {
              boolean isValidString = true;
              if (RegexFunctions.checkForString(inputTagidValue) != 0) {
                isValidString = false;
              } else if (!ValidateInputTagVale(valiReq, inputTagidValue, requestIndex)) {
                isValidString = false;
              } 
              if (!isValidString)
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_ACCOUNT_NUMBER) {
              boolean isValidAccountNumber = true;
              inputTagidValue = inputTagidValue.replaceAll(",", "");
              if (inputTagidValue.length() < 11 && inputTagidValue.length() > 23)
                isValidAccountNumber = false; 
              if (isValidAccountNumber && 
                RegexFunctions.checkForNumber(inputTagidValue) == 0)
                try {
                  Long long_ = Long.valueOf(Long.parseLong(inputTagidValue));
                } catch (NullPointerException e) {
                    logger.info("ValidatorManager.java LNo : 495 : Number Format Exception in Validation Manager", e);
                    isValidAccountNumber = false;
                } catch (Exception e) {
                  logger.info("ValidatorManager.java LNo : 495 : Number Format Exception in Validation Manager", e);
                  isValidAccountNumber = false;
                }  
              if (!isValidAccountNumber)
                isValidElementValue = false; 
            } else if (valiReq.getElementDateTypeId().intValue() == Constants.DATA_TYPE_BOOLEAN && 
              !inputTagidValue.equalsIgnoreCase("true") && !inputTagidValue.equalsIgnoreCase("false")) {
              isValidElementValue = false;
            }  
          if (!isValidElementValue) {
            errorMap.put(inputTagid, valiReq.getReqValiMsgNm());
            validatorResponse.setErrorMessage(errorMap);
            validatorResponse.setStatus(false);
            logger.info("ValidatorManager.java LNo : 592 : Server Side Validation failed for:::::::::::::" + inputTagid + ":::::::inputTagidValue:::::" + inputTagidValue);
            break;
          } 
        } 
      } 
    } catch (NullPointerException e) {
        logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
      }  catch (Exception e) {
      logger.info("ValidatorManager.java LNo : 267 : Inside the exception " + e);
    } 
    return validatorResponse;
  }
  
  public boolean validateApplicantLastQualification(String courseTypeId, String applicantLastQualificationId) {
    Integer intCourseTypeId = new Integer(Integer.parseInt(courseTypeId));
    MasterQualification masterQualification = this.validatorService.getAllQualificationById(intCourseTypeId);
    boolean valid = false;
    if (masterQualification != null) {
      String rules = masterQualification.getQualificationRule();
      String[] validLastQualificationIds = rules.split(",");
      byte b;
      int i;
      String[] arrayOfString1;
      for (i = (arrayOfString1 = validLastQualificationIds).length, b = 0; b < i; ) {
        String id = arrayOfString1[b];
        if (applicantLastQualificationId.equals(id))
          valid = true; 
        b++;
      } 
    } 
    return valid;
  }
  
  public boolean validateApplicantAndCoApplicantPanCard(String applicantPanCard, String coApplicantPanCard) {
    if (applicantPanCard != null && applicantPanCard != "" && coApplicantPanCard != null && coApplicantPanCard != "") {
      if (applicantPanCard.equals(coApplicantPanCard))
        return false; 
      return true;
    } 
    return true;
  }
  
  public boolean checkforConditionalManadatory(List<ValidateRequestConfig> conditional, String inputTagid) {
    boolean status = false;
    if (conditional != null && conditional.size() > 0) {
      for (ValidateRequestConfig validateRequestConfiglocal : conditional) {
        if (validateRequestConfiglocal.getReqValiName().equals(inputTagid)) {
          status = true;
          break;
        } 
      } 
    } else {
      status = true;
    } 
    return status;
  }
  
  private boolean ValidateInputTagVale(ValidateRequestConfig validateRequestConfigObject, Long inputTagidValue, Integer requestIndex) {
    boolean status = true;
    if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMaxValue()) && validateRequestConfigObject.getReqValiMaxValue().doubleValue() < inputTagidValue.longValue()) {
      status = false;
    } else if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMinValue()) && validateRequestConfigObject.getReqValiMinValue().intValue() > inputTagidValue.longValue()) {
      status = false;
    } else if (inputTagidValue.toString().equalsIgnoreCase(Constants.OTHER_VALUE)) {
      status = true;
    } 
    return status;
  }
  
  private boolean ValidateInputTagVale(ValidateRequestConfig validateRequestConfigObject, String inputTagidValue, Integer requestId) {
    boolean status = true;
    if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMaxLength()) && ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMinLength()) && 
      validateRequestConfigObject.getReqValiMaxLength().intValue() == validateRequestConfigObject.getReqValiMinLength().intValue() && 
      inputTagidValue.length() != validateRequestConfigObject.getReqValiMaxLength().intValue())
      return false; 
    if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMaxLength()) && validateRequestConfigObject.getReqValiMaxLength().intValue() < inputTagidValue.length()) {
      status = false;
    } else if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMinLength()) && validateRequestConfigObject.getReqValiMinLength().intValue() > inputTagidValue.length()) {
      status = false;
    } else if (!ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMaxValue()) || 
      validateRequestConfigObject.getReqValiMaxValue().doubleValue() >= inputTagidValue.length()) {
      if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMinValue()) && validateRequestConfigObject.getReqValiMinValue().intValue() > inputTagidValue.length())
        status = false; 
    } 
    return status;
  }
  
  private boolean ValidateInputTagVale(ValidateRequestConfig validateRequestConfigObject, Double inputTagidValue, Integer requestId) {
    boolean status = true;
    if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMaxValue()) && validateRequestConfigObject.getReqValiMaxValue().doubleValue() < inputTagidValue.doubleValue()) {
      status = false;
    } else if (ValidatorUtil.isValid(validateRequestConfigObject.getReqValiMinValue()) && validateRequestConfigObject.getReqValiMinValue().intValue() > inputTagidValue.doubleValue()) {
      status = false;
    } 
    return status;
  }
}




in above class a section of code is give a problem which was added recently by a developer  ,


        String ntbNumber = requestMap.get("ntbNumber");
        
        logger.info("ntbNumber "+ntbNumber);
        if(ntbNumber !=null) {
        	SessionUtil.setCheckRevokeNTB("1");
        	logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }else {
        	SessionUtil.setCheckRevokeNTB(null);
           //logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }
        
        logger.info("SessionUtil.getConsentRevokeNTB() "+SessionUtil.getConsentRevokeNTB());
        if(SessionUtil.getConsentRevokeNTB()!=null) {
        	SessionUtil.setCheckRevokeNTB("1");
        	logger.info("SessionUtil.getCheckRevokeNTB "+SessionUtil.getCheckRevokeNTB());
        }



        and bellow changes is making problem in existing flow 

                 //if(ntbNumber == null && SessionUtil.getCheckRevokeNTB()==null){
         // logger.info("ValidatorManager.java LNo :  for  SessionUtil.getConsentRevokeNTB() " +SessionUtil.getConsentRevokeNTB() );
         if(SessionUtil.getCheckRevokeNTB()==null ){
          if (condmandatory == 0 && mandatory == 1) {
            logger.info("ValidatorManager.java LNo : 257 : Server Side Validation failed for  " + validateRequestConfigObject.getReqValiName() + "::::::: as mentioned field  should be in request");
            errorMap.put(inputTagid, validateRequestConfigObject.getReqValiMsgNm());
            validatorResponse.setErrorMessage(errorMap);
            validatorResponse.setStatus(false);
            break;
          	} 
          }





          can you identify what the reason it making problem toexiting flow alsogive changes such that above code changes do interfear with the existing code where 
          give changes so that it works give code changes required help it step by step in details ,
          

        
