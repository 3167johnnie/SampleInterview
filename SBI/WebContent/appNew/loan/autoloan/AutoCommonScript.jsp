<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- <s:include value="/app/common/version.jsp"></s:include> --%>
<script type='text/javascript'>
  	makeBikeData="";
  	modelBikeData="";
  	VariantBike="";
  	relationships='';
  	loanCategoryAuto='';
  	
  	var ajaxPostUrl='<s:property value="%{ajaxPostUrl}"/>';
  	var isDsrPage=<s:property value="%{isDsrPage}"/>;
  	var pageIdentifier =<s:property value="%{autoLoanPage}"/>;
  	var loanType = 2;
  	var loanTypeId = 2;
  	var jsonJSArray = new Array();
	var jsonObject = {};
	var globalMobile='<s:property value="%{appForm.appMobileNo}"/>';
	var globalIsdCode='<s:property value="%{#session.isdCode}"/>';
	var globalEmail='<s:property value="%{#session.email}"/>';
	var isAgri=0;
	var isEdvantage = 0;
	<s:if test="%{quote.loanQuoteLoanCategoryId>=0}">
  		change_value = '<s:property value="%{quote.loanQuoteLoanCategoryId}"/>';
  	</s:if>
 	
  	var callInitAutoLoan= false;
 	var isOfferPage = false;
 	<s:if test="%{requestIndex==-1 || requestIndex==0}">
 		callInitAutoLoan = true;
 	</s:if>
 	<s:if test="%{requestIndex==1}">
		isOfferPage = true;
	</s:if>
	isOfferPage=false;
	isInSession=false;
	isRegisteredDealer=false;
	<s:if test="%{#session.appSeqIdAutoLoan!=null}">
		isInSession=true;
	</s:if>
	var min_age_year = 21;
	var max_age_year = 65;
	var min_age_limit = 20.932750103912987;
	var max_age_limit = 64.9655;
	if(BANK_ID == BANK_ID_SBI){
		min_age_year = 21;
		max_age_year = 65;
		min_age_limit = 20.932750103912987;
		max_age_limit = 64.9655;
	} else if(BANK_ID == BANK_ID_SBBJ){
		min_age_year = 21;
		max_age_year = 65;
		min_age_limit = 20.932750103912987;
		max_age_limit = 64.9655;
	} else if(BANK_ID == BANK_ID_SBH){
	} else if(BANK_ID == BANK_ID_SBM){
		min_age_year = 21;
		max_age_year = 65;
		min_age_limit = 20.932750103912987;
		max_age_limit = 64.9655;
	} else if(BANK_ID == BANK_ID_SBP){
		min_age_year = 21;
		max_age_year = 65;
		min_age_limit = 20.932750103912987;
		max_age_limit = 64.9655;
	} else if(BANK_ID == BANK_ID_SBT){
	}
	var ageValidationMsg ="Applicant age should be between "+min_age_year+" - "+max_age_year+" years";
	var isMobileVerified="N";
	var isLoanThankYouPage = false;
	<s:if test="%{appForm!=null?}">
		isMobileVerified='<s:property value="%{appForm.appMobileVerified}"/>';
	</s:if>
	noNeedToShowPopup=0;
	$('.ui-slider.handle').draggable();
</script>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jsLibrary.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap-datepicker.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.address.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/bootstrap3-typeahead.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.copyAddress.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/app/loan/autoloan/js<s:property value="#minFolderPath"/>/jquery.autoloan.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/app/loan/autoloan/js<s:property value="#minFolderPath"/>/autoLoanSbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
