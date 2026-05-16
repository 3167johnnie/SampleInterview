<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html dir="ltr" lang="en" version="-//W3C//DTD XHTML 1.1//EN" xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/1999/xhtml http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd">
    
<head>
<meta content="cache" http-equiv="Cache-Control" />
<meta content="cache" http-equiv="Pragma" />
<meta content="access plus 6 years" http-equiv="ExpiresDefault" />
</head> 

<body lang="en">
	<div class="modal fade otp-box" id="cbsOTP" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content">  
		  <div class="modal-body">
			<div class="verify-otp-left-section verify-mobile-otp">			
				</br>
				<h2><span>Verify your OTP</span></h2>	
				<p id="otpStaticContent" >OTP has been sent to <s:property value="%{cbs.cbsMobileNumberMask}"/></p>
			</div>
			<div class="otp-right-section">
				<form name="otpCBSForm" id="otpCBSForm" method="post" action="javascript:void(0);"   enctype="application/x-www-form-urlencoded" autocomplete="off">
					</br>
					</br>
					<ul class="otp-form">
						<s:if test="%{cbs.cbsIsdCode!=@com.mintstreet.common.util.Constants@COUNTRY_CODE_INDIA && cbs.cbsEmail!=null}">
							<li class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
								<label class="" style="color:#172154; font-size:13px;">OTP verify by?<b class="req">*</b></label>
								<div class="col-xs-12 mrgt-15">
										<div class="width-50 flt">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeYes" value="0" checked="checked" style="width:auto;">
												<label for="appOTPVerifyTypeYes"> Mobile </label>
											</div>
										</div>
										<s:if test="%{cbs.cbsEmail!=null}">
											<div class="width-50  flt">
												<div class="blue-radio blue-radio-danger">
													<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeNo" value="1" style="width:auto;">
													<label for="appOTPVerifyTypeNo"> Email </label>
												</div>
											</div>	
										</s:if>
								</div>
							</li>
						</s:if>
						<s:else>
			    			<s:hidden id="appOTPVerifyType" name="appOTPVerifyType" value="0"/>
							<s:hidden id="cveAppConsentRevokeYes" name="cveAppConsentRevokeYes" value="%{cbs.cveAppConsentRevokeYes}"/>
							
							<s:hidden id="responseStatus" name="responseStatus" value="%{applicationFormCaseCve.responseStatus}"/> 			
			    		</s:else>
						
						<s:hidden id="appISDCode" name="appISDCode" value="%{cbs.cbsIsdCode}"/>
						<s:hidden id="cbs.cbsIsdCode" name="appISDCode" value="%{cbs.cbsIsdCode}"/>
						<s:hidden name="cbsIsdCodeCve" id="cbsIsdCodeCve" value="%{cbs.cbsIsdCodeCve}"/>
						<s:hidden id="cveAppConsentRevokeYes" name="cveAppConsentRevokeYes" value="%{cbs.cveAppConsentRevokeYes}"/>
					
						<s:if test="%{cbs.cveAppConsentRevokeYes.equalsIgnoreCase('N') && cbs.cbsIsdCodeCve!=@com.mintstreet.common.util.Constants@COUNTRY_CODE_INDIA && cbs.cbsEmail!=null}">
							<li class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
								<label class="" style="color:#172154; font-size:13px;">OTP verify by?<b class="req">*</b></label>
								<div class="col-xs-12 mrgt-15">
										<div class="width-50 flt">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeYes" value="0" checked="checked" style="width:auto;">
												<label for="appOTPVerifyTypeYes"> Mobile </label>
											</div>
										</div>
										<s:if test="%{cbs.cbsEmail!=null}">
											<div class="width-50  flt">
												<div class="blue-radio blue-radio-danger">
													<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeNo" value="1" style="width:auto;">
													<label for="appOTPVerifyTypeNo"> Email </label>
												</div>
											</div>	
										</s:if>
								</div>
							</li>
						</s:if>
						<s:else>
			    			<s:hidden id="appOTPVerifyType" name="appOTPVerifyType" value="0"/>
							<s:hidden id="cveAppConsentRevokeYes" name="cveAppConsentRevokeYes" value="%{cbs.cveAppConsentRevokeYes}"/>
							<s:hidden id="responseStatus" name="responseStatus" value="%{applicationFormCaseCve.responseStatus}"/> 			
			    		</s:else>
						<li>
							<label>Enter your email<span class="req"><s:property value="%{agriLoanPage>0?'':' *'}" /></span></label>
							 <s:if test="%{cbs.cbsEmail!=null}">
								<s:hidden name="userEmail" id="userEmail" value="%{cbs.cbsEmail}" autocomplete="off" maxlength="40" placeholder="Email" cssClass="form-control disabledFields" disabled="true" />
								<s:textfield value="%{cbs.cbsEmailMask}" autocomplete="off" maxlength="40" placeholder="Email" cssClass="form-control disabledFields" disabled="true" />
							</s:if>
							<s:else>
								<s:textfield name="userEmail" id="userEmail" value="%{cbs.cbsEmail}" autocomplete="off" maxlength="40" placeholder="Email" cssClass="form-control"/>
							</s:else>
						</li>
						<div id="otp_row_confirm_html">
							<li>
								<label>Enter code sent by SMS<span class="req"> *</span></label>
								<input id="inputOtp" name="inputOtp"  type="text" onkeydown="return M.digit(event);" maxlength=6   placeholder="OTP CODE" class="form-control secure-otp">
								<input id="inputOtp1" name="inputOtp"  type="hidden" onkeydown="return M.digit(event);"  placeholder="OTP CODE" class="form-control secure-otp">
							</li>
						</div>
							<s:if test="%{appSeqId!=null}">
								<div id="captchaDiv" class="div-spread">
                                  	<span class="captcha-box" style="margin-left:0px;"> <span class="security-check">IDENTIFY YOURSELF - </span><span style="margin-right:14px;">Copy text into input box<span class="req"> *</span></span>
	                                	<img id="CommonCBScapImage" name="CommonCBScapImage" src="Captcha.jpg">
	                                	 <a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('CommonCBScapImage');"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/refresh.png"></a>
	                                	 <input type="text" id="captcha" name="captcha" value="" maxlength="6" placeholder="captcha" />
	                                </span>
                                 </div>
							</s:if>
						<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
						<li>
							<button id="confirmCbsOtp" class="track-btn conf-track-btn" name="confirmCbsOtp" type="submit" value="Confirm" onclick="confirmCBSOTPCveTimer()">Confirm</button>
							<span class="otp-loader cnrfm-otp-loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
						</li>
						</s:if>
						<s:else>
						<li>
							<button id="confirmCbsOtp" class="track-btn conf-track-btn" name="confirmCbsOtp" type="submit" value="Confirm" onclick="confirmCBSOTP()">Confirm</button>
							<span class="otp-loader cnrfm-otp-loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
						</li>
						</s:else>
						
						<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
						<li>
							<button id="resendCbsOtp" class="resend-btn" name="resendCbsOtp" type="submit" value="Resend" onclick="resendCBSOTPcve();">Resend code</button>
							<span class="resend-Loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
						</li>
						</s:if>
						<s:else>
						<li>
							<button id="resendCbsOtp" class="resend-btn" name="resendCbsOtp" type="submit" value="Resend" onclick="resendCBSOTP();">Resend code</button>
							<span class="resend-Loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
						</li>
						</s:else>
						
	    				<div class="clear"></div>
						 <s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
							<li id="errorOTPMsgCBS" class="error-msg-cbs-cve cveErrorMsg" style="display:none;"></li>	
						</s:if>
						<s:else>
							<li id="errorOTPMsgCBS" class="error-msg-cbs" style="display:none;"></li>
						</s:else> 
						<s:hidden id="cveAppConsentRevokeYes" name="cveAppConsentRevokeYes" value="%{cbs.cveAppConsentRevokeYes}"/>
					</ul>
				</form>
			</div>
		  </div>
		</div>
	  </div>
	</div>

 <s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">	

	<div class="modal otp-box" id="showThankYou" style="display:none;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content">
		    <div class="modal-body">
	  
		  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"> 
			  <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""></span></button> 
			<div class="verify-otp-left-section verify-mobile-otp">
				<h2><span>Get a</span> Callback</h2>	
				<p>(Please enter your contact details that we will revert back to you)</p>
			</div>
			
			<div class="otp-right-section" align="middle">
				<div class="clearfix"></div>
                                <br/>			
				     <span class="smile-icon"></span>
					 <br/>
					 <br/>
					 <p> Thank you </p>
					     <p> Thank you for your interest. Our representative will contact you shortly-SBI.  </p>
					<br/>
					
					<br/>
					<br/>
				      <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>/personal-banking">RETURN TO HOME</a>
			  </div>
		    </div>
		  </div>
	     </div>
       </div>
  
  </s:if> 
  <s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">	
	<div class="modal otp-box" id="showThankYouRevoke" style="display:none;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content">
		  <div class="modal-body">
		  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"> 
			  <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""></span></button> 
			<div class="verify-otp-left-section verify-mobile-otp">
				<h2><span>Get a</span> Callback</h2>	
				<p>(Please enter your contact details that we will revert back to you)</p>
			</div>
			
			<div class="otp-right-section" align="middle">
				<div class="clearfix"></div>
                                <br/>			
				     <span class="smile-icon"></span>
					 <br/>
					 <br/>
					 <p> Thank you </p>
				     <p> Your revocation request has been successfully registered-SBI  </p>
					<br/>
					<br/>
				      <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>/personal-banking">RETURN TO HOME</a>
			  </div>
		      </div>
		  </div>
	     </div>
       </div>
  </s:if>   
  
  <s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">	
	<div class="modal otp-box" id="showThankYouRevokeDuplicacy" style="display:none;" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content modal-content-cve">
		  <div class="modal-body">
		  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"> 
			  <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""></span></button> 
			<div class="verify-otp-left-section-cve verify-mobile-otp">
				</br>
				<h2><span>Get a</span> Callback</h2>	
				<p>(Please enter your contact details that we will revert back to you)</p>
			</div>
			
			<div class="otp-right-section" align="middle">
				<div class="clearfix"></div>
                                <br/>			
				     <span class="smile-icon"></span>
					 <br/>
					 <br/>
					 <p> Thank you </p>
				   
					 <p id="revocationMODE" >Dear Customer, Consent Revocation already taken via </span></p>
					<br/>
					<br/>
				      <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>/personal-banking">RETURN TO HOME</a>
			 </div>
		    </div>
		  </div>
	     </div>
       </div>
  </s:if>   

<script>

var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

var isEmailEncoded = false;
$(document).ready(function(){
	//secretKey=getSecretKey();
	$("form#otpCBSForm").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        $(element).valid();
	    },
	    onblur:function (element) {
	        $(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
			if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          showPOPMsg('errorOTPMsgCBS',message, false);
			  $("button[name=confirmCbsOtp]").removeAttr("disabled");
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if(element.attr("type")=="checkbox"){
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});
	
	var emailToBeEncoded = $('#userEmail').val();
	if(emailToBeEncoded.length > 0){
		var encoded = window.btoa(emailToBeEncoded);
	    $('#userEmail').val(encoded);
		isEmailEncoded = true;
	}
});


function getEncryptedStr(str) {
	
	try {
		var aesUtil = new AesUtil(keySize, iterationCount);
		var enc1 = aesUtil.encrypt(salt, iv, passPhrase, str);
		return enc1;
	} catch (err) {
		return false;
	}
}


function confirmCBSOTP(){
	jsonObject = fetch_validation_object(29,loanTypeId);
	
	if(isEmailEncoded){
	     var decodedEmail = window.atob($('#userEmail').val());
	     $('#userEmail').val(decodedEmail);
		 isEmailEncoded = false;
	}

	addValidationsRules(document.otpCBSForm,jsonObject);
	if($("#otpCBSForm").valid()){
		 $(".disabledFields").each(function(){
			$(this).removeAttr('disabled');
		});	
			var inputOtp=document.getElementById('inputOtp').value;
				 if(inputOtp!="" || inputOtp !=''){
			var encryptedOtp = getEncryptedStr(inputOtp);
			document.getElementById('inputOtp1').value = encryptedOtp;
				 }
		
		var formdata = "params="+getRequest('otpCBSForm', 4);
		formdata +="&requestIndex=29&requestType=1";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		$.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
			beforeSend:function(){
				$("#opt-loader-application").show();
			},
			complete:function(){
				$("#opt-loader-application").hide();
			},
		    success:function(htmlContent){
		    	if(htmlContent==null || htmlContent ==undefined){
		    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
					return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	$(".disabledFields").each(function(){
    				$(this).attr('disabled',true);
    			});
	        	if(stringMessage[0]=="error"){
	        		
	        		showPOPMsg('errorOTPMsgCBS',stringMessage[1], false);
	        	} else {
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsgCBS',stringMessage[1], true);
	        		$("#mobileOTP2").hide();
	        		$('#commonCbsDivId').hide();
	        		if(loanTypeId==1){
	        			M.byId('pixelTrackingGoCloud').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=440" width="1" height="1" />';
	        			if(<s:property value="%{homeTopUpLoanPage}"/>==1){
		 		            $("#homeloanform").show();
	
			            } else if(<s:property value="%{homeLoanPage}"/>==1){
	 		            	$("#homeloanform").show();
		            	}
	        		} else if(loanTypeId==2){
		            	$("#autoloanform").show();
		            	if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
		            		M.byId('pixelTrackingGoCloud').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442" width="1" height="1" />';
		            	}
	        		} else if(loanTypeId==3){
	        			$("#personalloanform").show();
	        		} else if(loanTypeId==8){
	        			$("#educationloanform").show();
	        			$("#m-hide").show();
	        		}else if(loanTypeId==17){
	        			$("#creditCardForm").show(); 
	        			$('#haveCreditCard').hide();
	        		}
	    			location.reload();
	        	}
		    },
		    error:function(jqXHR, textStatus){
	    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
				return false;
		    }
		});

	}
}

function resendCBSOTP(){
	removeDynamicLoader('resendOtp');
	showDynamicLoader('resendOtp');
	hidePOPMsg('errorOTPMsgCBS');
	var formdata = "params="+getRequest('otpCBSForm', 4);
	formdata +="&requestIndex=28&requestType=1";
	if(uiType!=undefined && uiType!=""){
		formdata += "&uiType="+uiType;
	}
	$.ajax({
	    type: "POST",
		url: ajaxPostUrl,
		data: formdata,
		//headers : {"Accept-Encoding" : "gzip"},
	    success:function(htmlContent){
	    	removeDynamicLoader('resendOtp');
	    	if(htmlContent==null || htmlContent ==undefined){
	    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
        		return false;
        	}
	    	var stringMessage = [];
        	stringMessage = htmlContent.split("|");
        	if(stringMessage[0]=="success"){
        		//secretKey=getSecretKey();
        		removeDynamicLoader('resendOtp');
        		showPOPMsg('errorOTPMsgCBS','<img src="'+BANK_IMAGE_FOLDER_NEWUI+'/tick.png">'+stringMessage[1], true);
	    		show_hide_toggle("#otp_row_confirm", 1);
		    	show_hide_toggle("#overlay-row_otp", 0);
	    	}else if(stringMessage[0]=="error"){
	    		if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		/* if(stringMessage[2]>4){
	        			$("#resendOtp").remove();
	        		}; */
	        	}
	    		showPOPMsg('errorOTPMsgCBS',stringMessage[1], false);
	    	};
	    },
	    error:function(jqXHR, textStatus){
	    	showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
			return false;
	    }
	});
	return false;
}

$("#appOTPVerifyTypeYes").unbind("click").on("click",function(){
	var mobilediv=''
		+'<li><label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>'
		+'<input id="inputOtp" name="inputOtp" class="form-control" maxlength="6" type="text" onkeydown="return M.digit(event);">'
		+'</li>';
	
	$("#otp_row_confirm_html").html(mobilediv);
	globalMobile = '<s:property value="%{#session.mobile}"/>';	
	$("#otpStaticContent").html("OTP has been sent to "+globalMobile);		
});

$("#appOTPVerifyTypeNo").unbind("click").on("click",function(){
	var mobilediv=''
		+'<li><label><span id="otpLabel">Enter code sent by Email<b class="req">*</b></span></label>'
		+'<input id="inputOtpEmail" name="inputOtp" type="text" class="form-control" maxlength="6" onkeydown="return M.isAlphaNumeric(event);">'
		+'</li>';
	
	$("#otp_row_confirm_html").html(mobilediv);
	 globalEmail = '<s:property value="%{#session.email}"/>';	
	/* $("#otpStaticContent").html("OTP has been sent to "+globalEmail); */
	/* Above commented condition and below added condition by Pratima in CVE demand*/
	$("#otpStaticContent").html("OTP has been sent to your Email Id.");	
});

$("#userEmail").unbind("blur").on("blur",function(){
	$('#otpCodeSendEmail').remove();
	M.byId('userEmail').style.borderColor = "";
	var emailid = $(this).val();
	if(loanTypeId==15 && emailid==''){
		return false;
	}
	if(!M.isEmail(emailid)){
		$("#userEmail").after("<div class='clear otpCodeSendEmail' id='otpCodeSendEmail>Invalid email id.</div>");
		M.byId('userEmail').style.borderColor = "red";
		return false;
	}
});

function confirmCBSOTPCveTimer ( )
{
  var confirmCbsOtp  = $("#confirmCbsOtp").val();
  	if (confirmCbsOtp == "Confirm") {		
		$("button[name=confirmCbsOtp]").attr("disabled", "disabled");
		
		
	}
  		setTimeout ("confirmCBSOTPCve()", 1);
}

function confirmCBSOTPCve(){
	var appOTPVerifyType  = $("#appOTPVerifyType").val();
	var confirmCbsOtp  = $("#confirmCbsOtp").val();
	
	if(isEmailEncoded){
	     var decodedEmail = window.atob($('#userEmail').val());
	     $('#userEmail').val(decodedEmail);
		 isEmailEncoded = false;
	}

	var cveAppConsentRevokeYes = document.getElementById("cveAppConsentRevokeYes").value;
	
	jsonObject = fetch_validation_object(29,loanTypeId);
	
	addValidationsRules(document.otpCBSForm,jsonObject);
	if($("#otpCBSForm").valid()){
		 $(".disabledFields").each(function(){
			$(this).removeAttr('disabled');
		});	
		
			var inputOtp=document.getElementById('inputOtp').value;
				 
			var encryptedOtp = getEncryptedStr(inputOtp);
			document.getElementById('inputOtp1').value = encryptedOtp;
				
		var formdata = "params="+getRequest('otpCBSForm', 4);
		formdata +="&requestIndex=29&requestType=1";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		$.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
			beforeSend:function(){
				$("#opt-loader-application").show();
			},
			complete:function(){
				$("#opt-loader-application").hide();
			},
		    success:function(htmlContent){
		    	if(htmlContent==null || htmlContent ==undefined){
		    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
					return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	$(".disabledFields").each(function(){
    				$(this).attr('disabled',true);
    			});
	        	if(stringMessage[0]=="error"){
	        		showPOPMsg('errorOTPMsgCBS',stringMessage[1], false);
					$("button[name=confirmCbsOtp]").removeAttr("disabled");
                 } else {
							showPOPMsg('errorOTPMsgCBS',stringMessage[1], true);
							$("#mobileOTP2").hide();
							$('#commonCbsDivId').hide();
							
							if(loanTypeId==3) {
							   $("#personalloanform").show();
							  }
                
							if (cveAppConsentRevokeYes == "N") {
								$('#cbsOTP').hide();
								$('#showThankYou').show();
								console.log("cveAppConsent Showing Thank You Screen::"+stringMessage[1]); 	
							} else  {
								console.log("Consent Revocation Confirm::"+stringMessage[1]);
								if(stringMessage[0]=="duplicacy"){
									console.log("Revoke duplicacy CASE::"+stringMessage[1]);				
									
									$("#confirmCbsOtp").attr("disabled", true);   						
									console.log("Disabled confirmCbsOtp::"+confirmCbsOtp);
									
									$("#resendCbsOtp").attr("disabled", true);   						
									console.log("Resend Disabled confirmCbsOtp::"+resendCbsOtp);
									
									$('#cbsOTP').hide();
											
									const fetchRevokeMode = stringMessage[1].split("Dear Customer, Consent Revocation already taken via").slice(-1);
									console.log("fetchRevokeMode:::"+fetchRevokeMode);
									
									$('#revocationMODE').append(fetchRevokeMode);
									$('#showThankYouRevokeDuplicacy').show(fetchRevokeMode);
								} else {
									console.log("Revoke SUCCESS CASE....");
									$('#cbsOTP').hide();
									$('#showThankYouRevoke').show();
								}		
							}
	        	        }
		    },
			focusInvalid: false,
			invalidHandler: function(event, validator)
			{
				var errors = validator.numberOfInvalids();
				if (errors) {
				var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
				showPOPMsg('errorOTPMsgCBS',message, false);
						}
			},
		    error:function(jqXHR, textStatus){
	    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
				return false;
		    }
		});
	}
}

function resendCBSOTPcve(){
	console.log("Calling resendCBSOTPcve");
	$("button[name=confirmCbsOtp]").removeAttr("disabled");
	removeDynamicLoader('resendOtp');
	showDynamicLoader('resendOtp');
	hidePOPMsg('errorOTPMsgCBS');

	var formdata = "params="+getRequest('otpCBSForm', 4);
	formdata +="&requestIndex=28&requestType=1";
	if(uiType!=undefined && uiType!=""){
		formdata += "&uiType="+uiType;
	}
	$.ajax({
	    type: "POST",
		url: ajaxPostUrl,
		data: formdata,
		//headers : {"Accept-Encoding" : "gzip"},
	    success:function(htmlContent){
	    	removeDynamicLoader('resendOtp');
	    	if(htmlContent==null || htmlContent ==undefined){
	    		showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
        		return false;
        	}
	    	var stringMessage = [];
        	stringMessage = htmlContent.split("|");
			
        	if(stringMessage[0]=="success"){
        		//secretKey=getSecretKey();
				console.log("stringMessage::success::"+stringMessage);
        		removeDynamicLoader('resendOtp');
        		showPOPMsg('errorOTPMsgCBS','<img src="'+BANK_IMAGE_FOLDER_NEWUI+'/tick.png">'+stringMessage[1], true);
	    		show_hide_toggle("#otp_row_confirm", 1);
		    	show_hide_toggle("#overlay-row_otp", 0);
	    	}else if(stringMessage[0]=="error"){
				console.log("stringMessage::error::"+stringMessage);
	    		 if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		/* if(stringMessage[2]>4){
	        			$("#resendOtp").remove();
	        		}; */
	        	}
				
	    		showPOPMsg('errorOTPMsgCBS',stringMessage[1], false);
	    	};
	    },
	    error:function(jqXHR, textStatus){
			console.log("stringMessage::error:function::"+stringMessage);
	    	showPOPMsg('errorOTPMsgCBS',SORRY_FOR_INCONVENIENCE, false);
			return false;
	    }
	});
	return false;
}

</script>
</body>
</html>
