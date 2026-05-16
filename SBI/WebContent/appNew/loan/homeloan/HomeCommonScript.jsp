<%@ taglib prefix="s" uri="/struts-tags"%>
<script type='text/javascript'>
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
	$('.ui-slider.handle').draggable();
</script>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jsLibrary.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/bootstrap-datepicker.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>


<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/homeLoanSbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.address.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap3-typeahead.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.copyAddress.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

