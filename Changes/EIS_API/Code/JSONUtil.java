package com.mintstreet.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.mintstreet.common.bo.CBSCustomerInformation;
import com.mintstreet.common.bo.CBSLoanAccountInformation;
import com.mintstreet.common.bo.CIFNumberResponse;
import com.mintstreet.common.bo.LoanScenarioBean;
import com.mintstreet.common.bo.OtherRequest;
import com.mintstreet.common.bo.RSMResponse;
import com.mintstreet.common.bo.RsmValuePairs;
import com.mintstreet.common.bo.WebRequest;
import com.mintstreet.common.entity.ApplicationFormLead;
import com.mintstreet.common.entity.MasterCBSCall;
import com.mintstreet.consent.bo.ConsentReadResponse;
import com.mintstreet.consent.bo.ConsentRequest;
import com.mintstreet.consent.bo.ConsentResponseConsentWrite;
import com.mintstreet.consent.bo.ConsentResponsePurposeEnquiry;
import com.mintstreet.consent.bo.PrivacyRequest;
import com.mintstreet.consent.bo.PrivacyRequestBean;
import com.mintstreet.consent.bo.PrivacyRequestBeanEnc;
import com.mintstreet.consent.bo.PrivacyResponseBean;
import com.mintstreet.consent.bo.PrivacyResponseEIS;
import com.mintstreet.integration.edms.bo.EdmsResponse;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoan;
import com.mintstreet.loan.agriloan.entity.ApplicationFormAgriLoanQuote;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoan;
import com.mintstreet.loan.autoloan.entity.ApplicationFormAutoLoanQuote;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoan;
import com.mintstreet.loan.educationloan.entity.ApplicationFormEducationLoanQuote;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoanQuote;
import com.mintstreet.uploadLead.entity.UploadBean;
import com.mintstreet.webservice.bo.Authentication;
import com.mintstreet.webservice.bo.CustomerData;
import com.mintstreet.webservice.bo.WebServiceResponse;

public class JSONUtil {
	
	private static final Logger logger = LogManager.getLogger(JSONUtil.class.getName());
	
	private static final Gson gson = new Gson();

	public static String object2json(Object obj) {
	    StringBuilder json = new StringBuilder();
	    if (obj == null) {
	      json.append("\"\"");
	    } else if (obj instanceof String || obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
	        || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
	        || obj instanceof BigInteger || obj instanceof Byte) {
	      json.append("\"").append(string2json(obj.toString())).append("\"");
	    } else if (obj instanceof Object[]) {
	      json.append(array2json((Object[]) obj));
	    } else if (obj instanceof List) {
	      json.append(list2json((List<?>) obj));
	    } else if (obj instanceof Map) {
	      json.append(map2json((Map<?, ?>) obj));
	    } else if (obj instanceof Set) {
	      json.append(set2json((Set<?>) obj));
	    } 
	    return json.toString();
	  }

	  public static String list2json(List<?> list) {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if (list != null && list.size() > 0) {
	      for (Object obj : list) {
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String array2json(Object[] array) {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if (array != null && array.length > 0) {
	      for (Object obj : array) {
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String map2json(Map<?, ?> map) {
	    StringBuilder json = new StringBuilder();
	    json.append("{");
	    if (map != null && map.size() > 0) {
	      for (Object key : map.keySet()) {
	        json.append(object2json(key));
	        json.append(":");
	        json.append(object2json(map.get(key)));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, '}');
	    } else {
	      json.append("}");
	    }
	    return json.toString();
	  }

	  public static String set2json(Set<?> set) {
	    StringBuilder json = new StringBuilder();
	    json.append("[");
	    if (set != null && set.size() > 0) {
	      for (Object obj : set) {
	        json.append(object2json(obj));
	        json.append(",");
	      }
	      json.setCharAt(json.length() - 1, ']');
	    } else {
	      json.append("]");
	    }
	    return json.toString();
	  }

	  public static String string2json(String s) {
	    if (s == null) return "";
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < s.length(); i++) {
	      char ch = s.charAt(i);
	      switch (ch) {
	        case '"':
	          sb.append("\\\"");
	          break;
	        case '\\':
	          sb.append("\\\\");
	          break;
	        case '\b':
	          sb.append("\\b");
	          break;
	        case '\f':
	          sb.append("\\f");
	          break;
	        case '\n':
	          sb.append("\\n");
	          break;
	        case '\r':
	          sb.append("\\r");
	          break;
	        case '\t':
	          sb.append("\\t");
	          break;
	        case '/':
	          sb.append("\\/");
	          break;
	        default:
          if (ch >= '\000' && ch <= '\037') {
	            String ss = Integer.toHexString(ch);
	            sb.append("\\u");
	            for (int k = 0; k < 4 - ss.length(); k++) {
	              sb.append('0');
	            }
	            sb.append(ss.toUpperCase());
	          } else {
	            sb.append(ch);
	          }
	      }
	    }
	    return sb.toString();
	  }
	  public static JSONObject beanObjectToJSONObjct(Object object){
			JSONObject json = new JSONObject();
			
			if(object instanceof ApplicationFormHomeLoan ){
				ApplicationFormHomeLoan application = (ApplicationFormHomeLoan)object;
				String jsonString = new Gson().toJson(application);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 170 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormHomeLoanQuote ){
				ApplicationFormHomeLoanQuote quote = (ApplicationFormHomeLoanQuote)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 178 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormAutoLoan ){
				ApplicationFormAutoLoan application = (ApplicationFormAutoLoan)object;
				String jsonString = new Gson().toJson(application);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 186 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormAutoLoanQuote ){
				ApplicationFormAutoLoanQuote quote = (ApplicationFormAutoLoanQuote)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 194 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormEducationLoan ){
				ApplicationFormEducationLoan application = (ApplicationFormEducationLoan)object;
				String jsonString = new Gson().toJson(application);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 202 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormEducationLoanQuote ){
				ApplicationFormEducationLoanQuote quote = (ApplicationFormEducationLoanQuote)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 210 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormPersonalLoan ){
				ApplicationFormPersonalLoan application = (ApplicationFormPersonalLoan)object;
				String jsonString = new Gson().toJson(application);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 218 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormPersonalLoanQuote ){
				ApplicationFormPersonalLoanQuote quote = (ApplicationFormPersonalLoanQuote)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 226 : Exception Caught",e);
				}
			}else if(object instanceof LoanScenarioBean ){
				LoanScenarioBean quote = (LoanScenarioBean)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 234 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormAgriLoan ){
				ApplicationFormAgriLoan application = (ApplicationFormAgriLoan)object;
				String jsonString = new Gson().toJson(application);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 250 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormAgriLoanQuote ){
				ApplicationFormAgriLoanQuote quote = (ApplicationFormAgriLoanQuote)object;
				String jsonString = new Gson().toJson(quote);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 259 : Exception Caught",e);
				}
			}/*else if(object instanceof ApplicationFormCreditCard ){
					ApplicationFormCreditCard app = (ApplicationFormCreditCard)object;
					String jsonString = new Gson().toJson(app);
					try {
						json = new JSONObject(jsonString);
					} catch (JSONException e) {
						logger.info("JSONUtil.java LNo : 259 : Exception Caught",e);
					}
			}
			else if(object instanceof MobileDeviceInformation ){
				MobileDeviceInformation mobileDeviceInformation = (MobileDeviceInformation)object;
				String jsonString = new Gson().toJson(mobileDeviceInformation);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 250 : Exception Caught",e);
				}
			}
			else if(object instanceof CardScenario ){
				CardScenario app = (CardScenario)object;
				String jsonString = new Gson().toJson(app);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 286 : Exception Caught",e);
				}
			}*/
			else if(object instanceof MasterCBSCall ){
				MasterCBSCall app = (MasterCBSCall)object;
				String jsonString = new Gson().toJson(app);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 305 : Exception Caught",e);
				}
			}else if(object instanceof ApplicationFormLead ){
				ApplicationFormLead lead = (ApplicationFormLead)object;
				String jsonString = new Gson().toJson(lead);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 305 : Exception Caught",e);
				}
			}else if(object instanceof CustomerData ){
				CustomerData customerData = (CustomerData)object;
				String jsonString = new Gson().toJson(customerData);
				try {	
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 322 : Exception Caught",e);
				}
			}else if(object instanceof WebServiceResponse ){
				WebServiceResponse webServiceResponse = (WebServiceResponse)object;
				String jsonString = new Gson().toJson(webServiceResponse);
				try {	
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 334 : Exception Caught",e);
				}
			}
			else if(object instanceof ConsentRequest){
				ConsentRequest request = (ConsentRequest)object;
				String jsonString = new Gson().toJson(request);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			else if(object instanceof PrivacyResponseBean){
				PrivacyResponseBean request = (PrivacyResponseBean)object;
				String jsonString = new Gson().toJson(request);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			else if(object instanceof PrivacyRequestBeanEnc){
				PrivacyRequestBeanEnc request = (PrivacyRequestBeanEnc)object;
				String jsonString = new Gson().toJson(request);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			else if(object instanceof PrivacyResponseEIS){
				PrivacyRequestBeanEnc request = (PrivacyRequestBeanEnc)object;
				String jsonString = new Gson().toJson(request);
				try {
					json = new JSONObject(jsonString);
				} catch (JSONException e) {
					logger.info("JSONUtil.java LNo : 340 : Exception Caught",e);
				}
			}
			
			return json;
		}
	  

	public static JSONObject getJONObjctFromJSONString(String jsonString){
		JSONObject jsonObj=null;
		try {
			jsonObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			logger.info("JSONUtil.java LNo : 358 : Exception Caught",e);
		}
		return jsonObj; 
	}
	  public static Object getObjctFromJSON(Object object, String jsonInString) {
		  Gson gson = new Gson();
		  Object objectFromJSONToBean = new Object();
		  if(object instanceof OtherRequest){
			  objectFromJSONToBean= gson.fromJson(jsonInString, OtherRequest.class);  
		  }else if(object instanceof ApplicationFormHomeLoan){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormHomeLoan.class);  
		  }else if(object instanceof ApplicationFormHomeLoanQuote){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormHomeLoanQuote.class);  
		  }else if(object instanceof ApplicationFormAutoLoan){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormAutoLoan.class);  
		  }else if(object instanceof ApplicationFormAutoLoanQuote){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormAutoLoanQuote.class);  
		  }else if(object instanceof ApplicationFormPersonalLoan){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormPersonalLoan.class);  
		  }else if(object instanceof ApplicationFormPersonalLoanQuote){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormPersonalLoanQuote.class);  
		  }else if(object instanceof ApplicationFormEducationLoan){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormEducationLoan.class);  
		  }else if(object instanceof ApplicationFormEducationLoanQuote){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormEducationLoanQuote.class);  
		  }else if(object instanceof ApplicationFormAgriLoan){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormAgriLoan.class);  
		  }else if(object instanceof ApplicationFormAgriLoanQuote){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormAgriLoanQuote.class);  
		  }else if(object instanceof ApplicationFormLead ){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ApplicationFormLead.class);  
		  }else if(object instanceof LoanScenarioBean ){
			  objectFromJSONToBean= gson.fromJson(jsonInString, LoanScenarioBean.class);  
		  }else if(object instanceof RSMResponse ){
			  objectFromJSONToBean= gson.fromJson(jsonInString, RSMResponse.class);
		  }else if(object instanceof CBSCustomerInformation ){
			  objectFromJSONToBean= gson.fromJson(jsonInString, CBSCustomerInformation.class);  
		  }else if(object instanceof CBSLoanAccountInformation){
			  objectFromJSONToBean= gson.fromJson(jsonInString, CBSLoanAccountInformation.class);   
		  }else if(object instanceof com.mintstreet.webservice.bo.RPRequest){
			  objectFromJSONToBean= gson.fromJson(jsonInString, com.mintstreet.webservice.bo.RPRequest.class);
		  }else if(object instanceof Authentication){
			  objectFromJSONToBean= gson.fromJson(jsonInString, Authentication.class);
		  }
		  /*else if(object instanceof MobileDeviceInformation){
			  objectFromJSONToBean= gson.fromJson(jsonInString, MobileDeviceInformation.class);
		  }*/
		  else if(object instanceof UploadBean){
			  objectFromJSONToBean= gson.fromJson(jsonInString, UploadBean.class);
		  }
		  else if(object instanceof PrivacyRequest){
			  objectFromJSONToBean= gson.fromJson(jsonInString, PrivacyRequest.class);
		  }

		  else if(object instanceof CIFNumberResponse){
			  objectFromJSONToBean= gson.fromJson(jsonInString, CIFNumberResponse.class);
		  }else if(object instanceof RsmValuePairs){
			  objectFromJSONToBean= gson.fromJson(jsonInString, RsmValuePairs.class);
		  }else if(object instanceof CIFNumberResponse){
			  objectFromJSONToBean= gson.fromJson(jsonInString, CIFNumberResponse.class);
		  }else if(object instanceof WebRequest){
			  objectFromJSONToBean= gson.fromJson(jsonInString, WebRequest.class);
		  }else if(object instanceof MasterCBSCall){
			  objectFromJSONToBean= gson.fromJson(jsonInString, MasterCBSCall.class);
		  }else if(object instanceof EdmsResponse){
			  objectFromJSONToBean= gson.fromJson(jsonInString, EdmsResponse.class);
		  }else if(object instanceof ConsentResponsePurposeEnquiry){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ConsentResponsePurposeEnquiry.class);
		  }else if(object instanceof ConsentResponseConsentWrite){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ConsentResponseConsentWrite.class);
		  }else if(object instanceof ConsentReadResponse){
			  objectFromJSONToBean= gson.fromJson(jsonInString, ConsentReadResponse.class);
		  }else if(object instanceof PrivacyRequestBean){
			  objectFromJSONToBean= gson.fromJson(jsonInString, PrivacyRequestBean.class);
		  }
		  
		  return objectFromJSONToBean; 
	  }
	  
	  public static boolean isJsonValid(String jsonInString) {
		  try {
	          gson.fromJson(jsonInString, Object.class);
	          return true;
	      } catch(com.google.gson.JsonSyntaxException ex) { 
	          return false;
	      }
	  }

}
