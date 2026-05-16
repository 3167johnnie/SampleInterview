<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--[if IE 7 ]>    <html class="ie7" lang="en"> <![endif]-->
<!--[if IE 8 ]>    <html class="ie8" lang="en"> <![endif]-->
<!--[if IE 9 ]>    <html class="ie9" lang="en"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> 
<html lang="en"> <!--<![endif]-->
	<head>
		<s:hidden id="headerDeploymentMode" value="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE}"/>
		<%-- <s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
				<!-- Google Tag Manager -->
				<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
				new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
				j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
				'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
				})(window,document,'script','dataLayer','GTM-TRNVC76');</script>
				<!-- End Google Tag Manager -->
		</s:if> --%>
	    <meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=9; IE=8; IE=7; IE=EDGE,chrome=1">
	   	<title><s:property value="%{(metaInfo.title!=null && metaInfo.title!='') ? metaInfo.title : @com.mintstreet.common.util.Constants@HOME_TITLE}"/></title>
	    <meta name="keywords" content="<s:property value="%{(metaInfo.keywords!=null && metaInfo.keywords!='') ? metaInfo.keywords : @com.mintstreet.common.util.Constants@HOME_KEYWORDS}"/>" />
	    <meta name="description" content="<s:property value="%{(metaInfo.description!=null && metaInfo.description!='') ? metaInfo.description : @com.mintstreet.common.util.Constants@HOME_DESCRIPTION}"/>" />
	    <!-- <meta http-equiv="content-security-policy" content="script-src 'self'; script-src-attr 'self' 'unsafe-inline';"> -->
		<!--Veiw Port -->
	    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
	    <link rel="icon" type="image/png" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/favicon.ico"/>
	    <link rel="shortcut icon" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/favicon.ico"/>
	  	<!--Bottstrap CSS -->
	    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/bootstrap.css" media="all">
	    <!--Core CSS -->
	    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/global.css" media="all">
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery-3.7.1.min.js"></script>
		<s:include value="/app/common/commonHeaderJSObject.jsp"/>
        <!--<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.min.js"></script>-->
		
	    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/modernizr-latest.js"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/bootstrap.min.js"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/home.js"></script>
	    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jsLibrary.js"></script>
	    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.plugin.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/common-rule-validation.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.concat.min.js"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/aes.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/AesUtil.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/pbkdf2.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		
		<!-- Internet Explorer Conditional Comments -->
        <!--Bootstrap Fallback for IE7 Support-->
        <!--[if lt IE 8]>
                <link href="/support/css/bootstrap-ie7.css" rel="stylesheet">
        <![endif]-->    
        <!--HTML5 Shim and Respond.js IE6-8 support of HTML5 elements and media queries-->    
        <!--[if lt IE 9]>
		         <link rel="stylesheet" type="text/css" href="/support/css/ie7.css" media="all">
                <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/html5shiv.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
                <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/><s:property value="#minFolderPath"/>/respond.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
        <![endif]-->
		<!--[if lt IE 7]>
                <script src="http://ie7-js.googlecode.com/svn/version/2.1(beta4)/IE7.js"></script>
        <![endif]-->

		<s:hidden id="headerAjaxPostUrl" value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>
		<s:hidden id="headerAppSeqId" value="%{appSeqId}"/>
		
		<%-- <script type='text/javascript'>
		  	var ajaxPostUrl = '<s:property value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>';
		  	var min_age_year = 18;
			var max_age_year = 70;
		  	if(BANK_ID == BANK_ID_SBI){
				min_age_year = 18;
				max_age_year = 70;
			} else if(BANK_ID == BANK_ID_SBBJ){
				min_age_year = 21;
				max_age_year = 70;
			}
		  	var ageValidationMsg ="Applicant age should be more than "+min_age_year+" years.";
			var closeOffStatus = 1;
			<s:if test="%{appSeqId!=null}">
				closeOffStatus = 0;
			</s:if>
		</script> --%>
	</head>
	<s:set var="closeOffStr" value="%{' onbeforeunload=cloaseOff();cloaseOff(); '}"/>
		<body <s:property value="%{closeOffStr}" />> 
			<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
				<!-- Google Tag Manager (noscript) -->
				<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-TRNVC76"
				height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
				<!-- End Google Tag Manager (noscript) -->
			</s:if>
			<div>
				<header>
					<div id="msg-panel" class="msg-war corner-all" style="display: none;"><span>&nbsp;</span>
						<b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
					</div>
					<br>
					<div class="container ">
						<div class="row">
							<div class="col-sm-3 col-lg-6 ie-flt">
								<div class="logo-div no-padding">
									<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/logo.png" alt="logo" /></a>
								</div>	
							</div>
							<div class="col-sm-9 col-lg-6 call-div">
								<p class="call">
									<span><s:property value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_1}" /></span>
									<span><s:property value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_2}" /></span>
									<span><s:property value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_3}" /></span>
								</p>
								<s:if test="%{isForFaqAndContactUs==null || isForFaqAndContactUs==0}">
										<div class="navigation ">
										<div class="">
											<a id="hamburger" class="" href="#mainMenu"><span></span></a>
										</div>
										<div class="">
											<nav id="mainMenu">
												<p class="top-nav-link">
													<a href="javascript:void(0);" id="headerApplicationTracker" >Application Tracker</a>  
													<a href="javascript:void(0);" id="headerGetACallback" class="mr-0">Get a Callback</a> 
													
												</p>
											</nav>
										</div>
									 </div>
								</s:if>
								
							</div>
						</div>
						<s:include value="/appNew/common/MessagePage.jsp"></s:include>
					</div>
				</header>
