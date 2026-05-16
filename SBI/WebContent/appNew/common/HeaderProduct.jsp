<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<!--[if IE 7 ]>    <html class="ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]>    <html class="ie8" lang="en"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en"> <!--<![endif]-->
<head>
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
			<!-- Google Tag Manager -->
			<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
			new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
			j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
			'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
			})(window,document,'script','dataLayer','GTM-TRNVC76');</script>
			<!-- End Google Tag Manager -->
	</s:if>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE,chrome=1">
  	<title><s:property value="%{(metaInfo.title!=null && metaInfo.title!='') ? metaInfo.title : @com.mintstreet.common.util.Constants@HOME_TITLE}"/></title>
    <meta name="keywords" content="<s:property value="%{(metaInfo.keywords!=null && metaInfo.keywords!='') ? metaInfo.keywords : @com.mintstreet.common.util.Constants@HOME_KEYWORDS}"/>" />
    <meta name="description" content="<s:property value="%{(metaInfo.description!=null && metaInfo.description!='') ? metaInfo.description : @com.mintstreet.common.util.Constants@HOME_DESCRIPTION}"/>" />
	<!--Veiw Port -->
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	<link rel="icon" type="image/png" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/favicon.ico"/> 
	<link rel="shortcut icon" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/favicon.ico"/>
	<!--Bottstrap CSS -->
	<link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/datepicker3.css" media="all">
	<link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/bootstrap.css" media="all">
	<!--Core CSS -->
	<link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/global.css" media="all">
	<link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.css" media="all">
	<link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/jquery-ui.css" media="all">
	
	<!--  script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.min.js"></script -->
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery-3.7.1.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/mobile-detect.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/bootstrap.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery-ui.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/common-rule-validation.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.ui.touch-punch.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/newsbox.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/aes.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/AesUtil.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/pbkdf2.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		
	<script>
		var closeOffStatus = 1;
		<s:if test="%{appSeqId!=null}">
			closeOffStatus = 0;
		</s:if>
	</script>
	<s:include value="/app/common/version.jsp"></s:include>
	<s:include value="/app/common/commonHeaderJSObject.jsp"/>
	<s:if test="%{homeLoanPage>0}">
		<s:include value="/appNew/loan/homeloan/HomeCommonScript.jsp" />
	</s:if>
	<s:elseif test="%{autoLoanPage>0}">
		<s:include value="/appNew/loan/autoloan/AutoCommonScript.jsp"></s:include>
	</s:elseif>
	<s:elseif test="%{personalLoanPage>0}">
		<s:include value="/appNew/loan/personal/PersonalCommonScript.jsp"></s:include>
	</s:elseif>
	<s:elseif test="%{educationLoanPage>0}">
		<s:include value="/appNew/loan/educationloan/EducationCommonScript.jsp"></s:include>
	</s:elseif>
	<s:elseif test="%{agriLoanPage>0}">
		<s:include value="/appNew/loan/agriloan/AgriCommonScript.jsp"></s:include>
	</s:elseif>
	<s:elseif test="%{creditCardPage>0}">
		<s:include value="/appNew/loan/creditcard/CreditCardCommonScript.jsp"></s:include>
	</s:elseif>
</head>
