<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type='text/javascript'>
	var isDsrPage=<s:property value="%{isDsrPage}"/>;
	var isEligibleForScholar= false;
	var isEligibleForEdvantage = false;
	var isOfferPage = false;
	var isEdvantage = 0;
	isOfferPage=false;
	
	var min_age_year = 12;
	var max_age_year = 70;
	var min_age_limit = 11.92476751671106;
	var max_age_limit = 69.9684;
	if(BANK_ID == BANK_ID_SBI){
		min_age_year = 12;
		max_age_year = 70;
		min_age_limit = 11.92476751671106;
		max_age_limit = 69.9684;
	} else if(BANK_ID == BANK_ID_SBBJ){
		min_age_year = 12;
		max_age_year = 70;
		min_age_limit = 11.92476751671106;
		max_age_limit = 69.9684;
	} else if(BANK_ID == BANK_ID_SBH){
	} else if(BANK_ID == BANK_ID_SBM){
	} else if(BANK_ID == BANK_ID_SBP){
	} else if(BANK_ID == BANK_ID_SBT){
	}
	var ageValidationMsg ="Applicant age should be between "+min_age_year+" - "+max_age_year+" years";
	<s:if test="%{requestIndex==1}">
		isOfferPage = true;
	</s:if> 
	var globalMobile='<s:property value="%{#session.mobile}"/>';
	var globalIsdCode='<s:property value="%{#session.isdCode}"/>';
	var globalEmail='<s:property value="%{#session.email}"/>';
	
	var isMobileVerified="N";
	<s:if test="%{appForm!=null?}">
		isMobileVerified='<s:property value="%{appForm.appMobileVerified}"/>';
	</s:if>
	$('.ui-slider.handle').draggable();
</script>

<%-- <script type='text/javascript'>
	bankWithIdElementData='';
	propertyTypeData='';
	residentCountries='';
	residentCities='';
	preferredStateData='';
	postgradutionData="";
	graduationData="";
	instituteData="";
	residingCityData="";
	countryRegionData="";
	certificateData="";
  	divErase4LoanPurposeId = 'LOANPURPOSE';
  	divErase4LoanCategoryId = 'LOANCATEGORY';
  	divErase4LoanInMoratoriumPeriod='MORATORIUMPERIOD';
  	divErase4CourseInstituteAndUniveristy='COURSEINSTITUTEUNIVERSITY';
  	divErase4TwoWheelerId = 'TWOWHEELERTYPE';
  	divErase4ResidentForEducation="EDUCATIONRESIDENTTYPE";
  	divErase4ResidentTypeId='RESIDENTTYPE';
  	divErase4EmploymentTypeId='EMPTYPE';
  	divErase4Repayment='REPAYMENT';
  	divErase4workExList="WORKLIST";
  	divErase4Collateral="COLLECTRAL-SECURITY";
  	divErase4addRelationship1 ="RELATIONSHIP";
  	
  	divErase4addLocation='LOCATIONSTATE';
  	divErase4addCities='LOCATIONCITY';
  	divErase4addDistrict='LOCATIONDISTRICT';
  	divErase4addBranch='LOCATIONBRANCH';
  	divErase4addEdvantageCities='EDVANTAGECITY';
  	
  	divErase4ExShowRoomprice ='ExShowRoomprice';
  	
  	divErase4InstituteList="INSTITUTELIST";
  	divErase4GenderList="GENDERLIST";
  	
  	divErase4ResidentType1='RESIDENTTYPE1';
  	divErase4EmploymentTypeId1='EMPTYPE1';
  	
  	divErase4addCoapplicantDetails2='';
	
  	divErase4addCoapplicantDetails1='coAppImageData';
  	
  	divErase4ExShowRoompriceBike='ExShowRoompriceBike';
  	divErase4ExShowRoompriceCar='ExShowRoompriceCar';
  	
  	divErase4DealerList="DEALERNAMELIST";
  	
  	divErase4addCoapplicantDetails='';
  	divErase4PerviousLoanList = "PERVIOUSLOANLIST";
  	divErase4EmployerList1='EMPLOYERLIST1';
  	divErase4EmployerList2='EMPLOYERLIST2';
  	divErase4EPensioner='';
  	divErase4CourseList="COURSETYPELIST";
  	divErase4CourseFeeList="COURSEFEELIST";
  	
  	divErase4addPreferredLocation = "PREFERREDLOCATIONBRANCH";
  	divErase4addRelationship = "469";
	var ajaxPostUrl='<s:property value="%{ajaxPostUrl}"/>';
  	var isDsrPage=<s:property value="%{isDsrPage}"/>;
  	var pageIdentifier =<s:property value="%{educationLoanPage}"/>;
  	var loanType = 4;
  	var loanTypeId = 4;
  	var jsonJSArray = new Array();
	var jsonObject = {};
	var globalMobile='<s:property value="%{#session.mobile}"/>';
	var globalIsdCode='<s:property value="%{#session.isdCode}"/>';
	var globalEmail='<s:property value="%{#session.email}"/>';
	
	<s:if test="%{quote.loanQuoteLoanCategoryId>=0}">
  		change_value = '<s:property value="%{quote.loanQuoteLoanCategoryId}"/>';
  	</s:if>
 	
  	var callInitHomeLoan= false;
 	var isOfferPage = false;
 	<s:if test="%{requestIndex==-1 || requestIndex==0}">
 		callInitEducationLoan = true;
 	</s:if>
 	<s:if test="%{requestIndex==1}">
		isOfferPage = true;
	</s:if>
	isOfferPage=false;
	isInSession=false;
	
	<s:if test="%{appSeqId!=null}">
		isInSession=true;
	</s:if>
	<s:else>
	
	</s:else>
	
	var isMobileVerified="N";
	<s:if test="%{appForm!=null?}">
		isMobileVerified='<s:property value="%{appForm.appMobileVerified}"/>';
	</s:if>
	noNeedToShowPopup=0;
	var isEligibleForScholar= false;
	var isEligibleForEdvantage = false;
	var isAgri=0;
	var isEdvantage = 0;
	var isScholar= 0;
	var isLoanThankYouPage = false;
	var instituteScholarList='';
</script>

<%-- <script type='text/javascript'>
	bankWithIdElementData='';
	propertyTypeData='';
	residentCountries='';
	residentCities='';
	preferredStateData='';
	postgradutionData="";
	graduationData="";
	instituteData="";
	residingCityData="";
	countryRegionData="";
	certificateData="";
  	divErase4LoanPurposeId = 'LOANPURPOSE';
  	divErase4LoanCategoryId = 'LOANCATEGORY';
  	divErase4LoanInMoratoriumPeriod='MORATORIUMPERIOD';
  	divErase4CourseInstituteAndUniveristy='COURSEINSTITUTEUNIVERSITY';
  	divErase4TwoWheelerId = 'TWOWHEELERTYPE';
  	divErase4ResidentForEducation="EDUCATIONRESIDENTTYPE";
  	divErase4ResidentTypeId='RESIDENTTYPE';
  	divErase4EmploymentTypeId='EMPTYPE';
  	divErase4Repayment='REPAYMENT';
  	divErase4workExList="WORKLIST";
  	divErase4Collateral="COLLECTRAL-SECURITY";
  	divErase4addRelationship1 ="RELATIONSHIP";
  	
  	divErase4addLocation='LOCATIONSTATE';
  	divErase4addCities='LOCATIONCITY';
  	divErase4addDistrict='LOCATIONDISTRICT';
  	divErase4addBranch='LOCATIONBRANCH';
  	divErase4addEdvantageCities='EDVANTAGECITY';
  	
  	divErase4ExShowRoomprice ='ExShowRoomprice';
  	
  	divErase4InstituteList="INSTITUTELIST";
  	divErase4GenderList="GENDERLIST";
  	
  	divErase4ResidentType1='RESIDENTTYPE1';
  	divErase4EmploymentTypeId1='EMPTYPE1';
  	
  	divErase4addCoapplicantDetails2='';
	
  	divErase4addCoapplicantDetails1='coAppImageData';
  	
  	divErase4ExShowRoompriceBike='ExShowRoompriceBike';
  	divErase4ExShowRoompriceCar='ExShowRoompriceCar';
  	
  	divErase4DealerList="DEALERNAMELIST";
  	
  	divErase4addCoapplicantDetails='';
  	divErase4PerviousLoanList = "PERVIOUSLOANLIST";
  	divErase4EmployerList1='EMPLOYERLIST1';
  	divErase4EmployerList2='EMPLOYERLIST2';
  	divErase4EPensioner='';
  	divErase4CourseList="COURSETYPELIST";
  	divErase4CourseFeeList="COURSEFEELIST";
  	
  	divErase4addPreferredLocation = "PREFERREDLOCATIONBRANCH";
  	divErase4addRelationship = "469";
	
</script> --%>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jsLibrary.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap-datepicker.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/app/loan/educationloan/js<s:property value="#minFolderPath"/>/jquery.educationLoan.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/app/loan/educationloan/js<s:property value="#minFolderPath"/>/educationLoanSbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.address.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap3-typeahead.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.copyAddress.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

