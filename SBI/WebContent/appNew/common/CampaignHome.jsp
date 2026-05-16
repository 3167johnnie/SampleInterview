<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="en"> 
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
    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/bootstrap.css" media="all">
    <!--Core CSS -->
    <link rel="stylesheet" type="text/css" href="<s:property value="@com.mintstreet.common.util.Constants@BANK_CSS_FOLDER_NEWUI"/>/global.css" media="all">
	<s:include value="/app/common/version.jsp"></s:include>
    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.min.js"></script>
    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/modernizr-latest.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/bootstrap.min.js"></script>
    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jsLibrary.js"></script>
    <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.plugin.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.validate_latest.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/common-rule-validation.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.concat.min.js"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/aes.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/AesUtil.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/pbkdf2.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	
	<s:include value="/app/common/commonHeaderJSObject.jsp"/>
	
</head>
<s:set var="closeOffStr" value="%{' onbeforeunload=cloaseOff();cloaseOff(); '}"/>
<body class="callbck-bdy-hl" <s:property value="%{closeOffStr}" />> 
<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
			<!-- Google Tag Manager (noscript) -->
			<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-TRNVC76"
			height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
			<!-- End Google Tag Manager (noscript) -->
</s:if>
<header class="campaign-header">
	<div class="container ">
		<div class="row">
			 <div class="col-sm-3 col-lg-12 ie-flt">
				<div class="logo-div ">
					<a href="<s:property value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sp_logo.png" alt="logo" width="100" /></a>
				</div>	
			</div> 
			
		</div>
	</div>
</header>
<div class="clear"></div>
	<div class="modal-dialog landing-pp" role="document">
		<div class="modal-content">
			<div class="modal-body">			
				<div class="otp-left-section">
					<h2><span>Get a</span> Callback</h2>
					<p id="leftPanelMsg">(Please enter your contact details that we will revert back to you )</p>
				</div>
				<div class="otp-right-section" >
					<div id="beforeGetACallBack" style="display: block;">
						<form name='campaignCallBackPage' id='campaignCallBackPage' method='post' action='javascript:void(0);' enctype='application/x-www-form-urlencoded' onsubmit="return submit_first_page_bind();" autocomplete="off">
							<ul class="otp-form">
								<li>
									<label>Loan<b class="req">*</b></label>
									<div class="flat-field">
										<s:select id="leadLoanPurposeId" name="lead.leadLoanPurposeId" value="%{homeLoanPage}" 
										 list="%{beanList.loanPurposes}"
										headerKey="0" headerValue="Select loan purpose" cssClass="form-select" autocomplete="off"/> 
									</div>
								</li>
								<li>
									<label>Name<b class="req">*</b></label>
									<s:textfield id="leadFirstName" name="lead.leadFirstName" value="" cssClass="form-control" placeholder="Name" maxlength="32" autocomplete="off" onkeydown="return M.isChars(event);"/>
								</li>
								
								<div id="city-div" style="display:<s:property value="%{(lead==null || lead.leadApplyingFrom == 1)?'block':'none'}"/>;" >
									<li>
										<label>City<b class="req">*</b></label>
										<div class="flat-field">
											<s:select list="%{#citiesOtp1==null?'':#citiesOtp1}" id="leadCityId" name="lead.leadCityId" value="" headerKey="0" headerValue="Select city" cssClass="form-select"/>
										</div>
									</li>
								</div>
								<li>
									<label>Preferred Language<b class="req">*</b></label>
									<div class="flat-field">
										<s:select id="leadLanguageId" name="lead.leadLanguageId" value="" list="#{1:'Hindi', 2:'English'}" 
											headerKey="0" headerValue="Select Preferred Language" cssClass="form-select" autocomplete="off"/>
									</div>
								</li>
								<li>
									<label>Mobile<b class="req">*</b></label>
									
									<s:textfield id="mobile" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="10" placeholder="Mobile No." onkeydown="return M.digit(event);"/>
								</li>
								<li class="hide">
									<label>Applying from?<span class="mandatory">*</span></label>
									<s:if test="%{lead!=null && lead.leadApplyingFrom == 2}">
					    				<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" style="width:auto;float:left;"><span style="float:left;margin: 3px 0 0 5px;"> India&nbsp;&nbsp;&nbsp;&nbsp;</span>
						    			<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsNo" value="2" checked="checked" style="width:auto;float:left;"><span style="float:left;margin: 3px 0 0 5px;"> Abroad</span>
					    			</s:if>
					    			<s:else>
					    				<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" checked="checked" style="width:auto;float:left;"><span style="float:left;margin: 3px 0 0 5px;"> India&nbsp;&nbsp;&nbsp;&nbsp;</span>
						    			<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsNo" value="2" style="width:auto;float:left;"><span style="float:left;margin: 3px 0 0 5px;"> Abroad</span>
			    					</s:else>
								</li>
								<li>
									<label>Email</label>
									<s:textfield id="leadWorkEmail" name="lead.leadWorkEmail" value="" placeholder="Email" cssClass="form-control" autocomplete="off" maxlength="40" />
								</li>
								<li class="captcha-hi">
									
										<label>IDENTIFY YOURSELF<b class="req">*</b> <small>Copy text into input box</small></label>
    									<div class="cmp-captcha">
    									   <img id="campaignHomeCapImg" name="campaignHomeCapImg" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('campaignHomeCapImg');">
    									  <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
    									</div>
    									 <input type="text" class="form-control" id="campaignHomeCap" name="captcha" value="" maxlength="6" placeholder="Captcha." autocomplete="off"/>
										
  								</li>
								<li>
									<div class="">
										<input type="checkbox" id="infoprovide" name="infoprovide" class="blue-css-checkbox">
										<label class="label-content" for="infoprovide">
											I agree to be contacted by <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> and/ or it's representatives over phone or e-mail.
										<b class="req">*</b></label>
									</div>
								</li>
								<li>
									<button type="submit" id="callBankSubmit" class="track-btn conf-track-btn" >SUBMIT</button>
								</li>
							</ul>
						</form>
					</div>
					<div id="afterGetACallBack" style="display: none;">
						<form  name="call_back" id="call_back" method="post" autocomplete="off">
							<ul class="otp-form">
					    		<div id="resend_box-mb" style="display:none;"></div>
					    		<div id="wantus_row_confirm" class="campaign-wantus_row_confirm" style="display:block;">
					    			<li>
						    			<label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>
						    			<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp" maxlength="6"  onkeydown="return M.digit(event);"/>
						    			<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp"   onkeydown="return M.digit(event);"/>
					    			</li>
					    			<li class="captcha-hi">
										<label>IDENTIFY YOURSELF<b class="req">*</b> <small>Copy text into input box</small></label>
    									<div class="cmp-captcha">
    									 	<img id="otpCampaignHomeCapImg" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('otpCampaignHomeCapImg');">
    									 	<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
    									</div>
    									<input type="text" class="form-control" id="otpCampaignHomeCapText" name="captcha" value="" maxlength="6" placeholder="Captcha." autocomplete="off"/>
  								    </li> 
  								    &nbsp
					    			<li>
						    			<button  class="track-btn conf-track-btn" id="confirmOtpWantUs" name="confirmOtpWantUs" type="submit">Confirm</button>
				    					<button  class="campaign-btn resend-btn" id="resendOtpWantUs" name="resendOtpWantUs" type="submit">Resend</button>
					    			</li>
					    		</div>
							</ul>
						</form>
					</div>
			    	<div class="clear"></div>
			    	<div id="errorOTPMsg" class="thankyou-msg" style="display:none;"></div>
				</div>
			</div>
		</div>
	</div>
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
		<div>
			<!-- Facebook Pixel Code -->
			<script>
				!function(f, b, e, v, n, t, s) {
					if (f.fbq)
						return;
					n = f.fbq = function() {
						n.callMethod ? n.callMethod.apply(n, arguments) : n.queue
								.push(arguments)
					};
					if (!f._fbq)
						f._fbq = n;
					n.push = n;
					n.loaded = !0;
					n.version = '2.0';
					n.queue = [];
					t = b.createElement(e);
					t.async = !0;
					t.src = v;
					s = b.getElementsByTagName(e)[0];
					s.parentNode.insertBefore(t, s)
				}(window, document, 'script',
						'https://connect.facebook.net/en_US/fbevents.js');
				fbq('init', '306199269752742');
				fbq('track', 'PageView');
			</script>
			<noscript>
				<img height="1" width="1" style="display: none"
					src="https://www.facebook.com/tr?id=306199269752742&ev=PageView&noscript=1" />
			</noscript>
		<!-- DO NOT MODIFY -->
		<!-- End Facebook Pixel Code -->
		</div>
	</s:if>
	<script type="text/javascript">
		function fetchJsonData(){
			jsonJSArray = '<s:property value="%{jsonJSArray}"/>';
			jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
			jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
			jsonObject = jQuery.parseJSON(jsonJSArray);
			return jsonObject;
		}
	</script>
	<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/commoncallback.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	<s:include value="/appNew/common/FooterCampaign.jsp"></s:include>
</body>
</html>
