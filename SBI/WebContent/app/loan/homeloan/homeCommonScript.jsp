<%@ taglib prefix="s" uri="/struts-tags"%>
<script type='text/javascript'>
	divErase4addCities="PREFERREDLOCATIONCITY";
	divErase4addDistrict="PREFERREDLOCATIONDISTRICT";
	divErase4addBranch="PREFERREDLOCATIONBRANCH";
	divErase4ResidentIsOwner = "PROPERTYOWNER";
	
	divErase4SalaryAccountWithSBI = "SALARYACCOUNTWITHSBI";
	bankWithIdElementData='';
	propertyTypeData='';
	preferredStateData='';
	var residentCountries='';
  	divErase4LoanPurposeId = 'LOANPURPOSE';
  	divErase4LoanCategoryId = 'LOANPURPOSELOANCATEGORY';
  	divErase4PropertyTypeId = 'PROPERTYTYPEID';
  	divErase4ResidentTypeIdA='RESIDENTTYPEA';
  	divErase4ResidentTypeIdB='RESIDENTTYPEB';
  	divErase4EmploymentTypeId='EMPTYPE';
  	
  	divErase4addPreferredLocation='PREFERREDLOCATIONSTATE';
  	divErase4addPreferredCities='PREFERREDLOCATIONCITY';
  	divErase4addPreferredDistrict='PREFERREDLOCATIONDISTRICT';
  	divErase4addPreffereBranch='PREFERREDLOCATIONBRANCH';
  	divErase4ResidentType1='RESIDENTTYPE1';
  	divErase4EmploymentTypeId1='EMPTYPE1';
  	divErase4ResidentType2='RESIDENTTYPE2';
  	divErase4EmploymentTypeId2='EMPTYPE2';
	
  	divErase4CheckOffType ='CHECKOFFTYPE';
  	
  	divErase4addCoapplicantDetails1='coAppImageData';
  	divErase4addCoapplicantDetails2='coAppImageNextData';
  	
  	divErase4ExShowRoomprice='ExShowRoomprice';
  	
  	divErase4addCoapplicantDetails='';
  	divErase4EmployerList='EMPLOYERLIST';
  	divErase4EmployerList1='EMPLOYERLIST1';
  	divErase4EmployerList2='EMPLOYERLIST2';
  	divErase4NRIMobile='NRIMOBILE';
  	divErase4ActualInvestmentMade='ACTUALINVESTMENTMADE';
  	divErase4IsAvaiReimbursement='ISAVAIREIMBURSEMENT';
  	divisAvaiReimbursement='divisAvaiReimbursement';
  	divErase4EPensioner='';
  	
  	divCreate4addCities="APPPICKUPLOCATIONCITY";
	divCreate4addDistrict="APPPICKUPLOCATIONDISTRICT";
	divCreate4addBranch="APPPICKUPLOCATIONBRANCH";
	divErase4CheckOffType ='CHECKOFFTYPE';
	var dropOffPopupStatus = 1;
  	var ajaxPostUrl='<s:property value="%{ajaxPostUrl}"/>';
  	var isDsrPage=<s:property value="%{isDsrPage}"/>;
  	var pageIdentifier =<s:property value="%{homeLoanPage}"/>;
  	var loanType = 1;
  	var loanTypeId = 1;
  	var globalMobile='<s:property value="%{#session.mobile}"/>';
	var globalIsdCode='<s:property value="%{#session.isdCode}"/>';
	var globalEmail='<s:property value="%{#session.email}"/>';
  	var callInitHomeLoan= false;
  	var isOfferPage = false;
 	<s:if test="%{requestIndex==-1 || requestIndex==0}">
 		callInitHomeLoan = true;
 	</s:if>
 	<s:if test="%{requestIndex==1}">
		isOfferPage = true;
	</s:if>
	var isInSession=false;
	isOfferPage=false;
	isInSession=false;
	isRegisteredBuilder=false;
	projectData="";
	<s:if test="%{appSeqId!=null}">
		isInSession=true;
	</s:if>
	var min_age_year = 18;
	var max_age_year = 70;
	var min_age_limit = 17.927510876870876;
	var max_age_limit = 69.9684;
	if(BANK_ID == BANK_ID_SBI){
		min_age_year = 18;
		max_age_year = 70;
	} else if(BANK_ID == BANK_ID_SBBJ){
		min_age_year = 18;
		max_age_year = 65;
		min_age_limit = 17.927510876870876;
		max_age_limit = 64.9655;
	} else if(BANK_ID == BANK_ID_SBH){
	} else if(BANK_ID == BANK_ID_SBM){
	} else if(BANK_ID == BANK_ID_SBP){
		min_age_year = 21;
		max_age_year = 70;
		min_age_limit = 20.932750103912987;
		max_age_limit = 69.9684;
	} else if(BANK_ID == BANK_ID_SBT){
	}
	var ageValidationMsg ="Applicant age should be between "+min_age_year+" - "+max_age_year+" years";
	var isMobileVerified="N";
	var isAgri=0;
	var isEdvantage = 0;
	<s:if test="%{appForm!=null?}">
		isMobileVerified='<s:property value="%{appForm.appMobileVerified}"/>';
	</s:if>
	noNeedToShowPopup=0;
	preEMIs='';
	preEMIsOther='';
	genderValue='0';
	var divisAvaiReimbursement='';
</script>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/jquery-1.11.0.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/jsLibrary.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/jquery.autocomplete.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/jquery.plugin.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/jquery.datepick_latest.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/homeLoanSbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/jquery.address.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
