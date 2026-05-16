<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!-- OTP popup -->
<s:if test="%{#session.bankLMSUser==null}">
	<s:if test="%{(appForm.appMobileVerified==null || appForm.appMobileVerified.equalsIgnoreCase('N')) && (appForm.appEmailVerified==null || appForm.appEmailVerified.equalsIgnoreCase('N'))}">
		<div class="modal fade otp-box" id="commonOTP" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document">
				<div class="modal-content">
			  		<div class="modal-body">
						<div class="verify-otp-left-section verify-mobile-otp">
							<h2><span>Verify your OTP</span></h2>	
							<p id="otpStaticContent">OTP has been sent to <s:property value="%{appForm.appMobileNumberMask}"/></p>
						</div>
						<div class="otp-right-section">
							<form name="otpApplication" id="otpApplication" method="post" action="javascript:void(0);" autocomplete="off">
								<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
								<s:hidden id="isExistingCard" name="isExistingCard" value="%{appForm.appCardHolder}" />
								<ul class="otp-form">
									<s:hidden id="appApplyingFrom" name="appApplyingFrom" value="%{appForm.appApplyingFrom==0?1:appForm.appApplyingFrom}"/>
									<div id="resend_box">
										<li>
					    					<s:if test="%{appForm.appApplyingFrom==2}">
												<s:property value="%{appForm.appMobileNumberMask}"/> not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);" onclick="changeMobileNumberNRI();">Click here</a> to change.
					    					</s:if>
					    					<s:else>
					    						<s:property value="%{appForm.appMobileNumberMask}"/> not your number? <a id="changeMobileNumber" href="javascript:void(0);" onclick="changeMobileNumber();">Click here</a> to change.
					    					</s:else>
										</li>
						    		</div>
						    		<s:if test="%{quote.loanQuoteResidentTypeId==2 && appForm.appApplyingFrom==2}">
							    		<div id="otp_verify_type">
							    			<li class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
												<label class="" style="color:#172154; font-size:13px;">OTP verify by?<b class="req">*</b></label>
												<div class="col-xs-12 mrgt-15">
													<div class="width-50 flt">
														<div class="blue-radio blue-radio-danger">
															<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeYes" value="0" checked="checked" style="width:auto;" class="disabledFields" onclick="otpVerifyBy('Y');">
															<label for="appOTPVerifyTypeYes">Mobile</label>
														</div>
													</div>
													<div class="width-50  flt">
														<div class="blue-radio blue-radio-danger">
															<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeNo" value="1" style="width:auto;" class="disabledFields" onclick="otpVerifyBy('N');">
															<label for="appOTPVerifyTypeNo">Email</label>
														</div>
													</div>	
												</div>
											</li>
							    		</div>
						    		</s:if>
						    		<s:else>
							    		<input type="radio" name="appOTPVerifyType" id="appOTPVerifyTypeYes" value="0" checked="checked" style="width:auto;" class="disabledFields hide">
						    		</s:else> 
						    		<div id="otp_row_confirm">
						    			<li id="otp_row_confirm_html">
											<label>Enter code sent by SMS<span class="req"> *</span></label>
											<input id="inputOtp" name="inputOtp"  type="text" onkeydown="return M.digit(event);" maxlength="6" placeholder="OTP CODE " class="form-control secure-otp">
											<input id="inputOtp1" name="inputOtp"  type="hidden" >
										</li>				
	                               		<li class="pp-captcha" id="captchaDiv">
											<label>IDENTIFY YOURSELF<b class="req">*</b>
                                         	<span>Copy text into input box</span></label>								
											<img id="CommonOtpOthercapImage" name="CommonOtpOthercapImage" src="Captcha.jpg">
											<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('CommonOtpOthercapImage');">
											<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
											<input type="text" class="form-control captcha-inpt" id="captchaBox" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
										</li>
										</br>
										<li>
										
											<button id="confirmOtp" class="track-btn conf-track-btn" name="confirmOtp" type="submit" value="Confirm" onclick="confirmOTP();">Confirm</button>
											
											<span class="otp-loader cnrfm-otp-loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
										</li>
										<li style="height:42px;'">
											<button id="resendOtp" class="resend-btn" name="resendOtp" type="submit" value="Resend" onclick="resendOTP();">Resend code</button>
											
											<span class="otp-loader"><img id="opt-loader-application"  style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
										</li>
						    		</div>
				    				<div class="clear"></div>
				    				<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li>
								</ul>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- OTP popup -->
	</s:if>
</s:if>

<script type="text/javascript">
	productTypeId='<s:property value="%{#commonQuote.productTypeId}"/>'
	loanQuoteLoanPurposeId='<s:property value="%{quote.loanQuoteLoanPurposeId}"/>';
	appOTPVerifiedByMobile='<s:property value="%{appForm.appMobileVerified}"/>';
	appOTPVerifiedByEmail='<s:property value="%{appForm.appEmailVerified}"/>';
	NRICase='<s:property value="%{quote.loanQuoteResidentTypeId}"/>';
	
	if(NRICase==2){
		if(loanQuoteLoanPurposeId!=23 && appOTPVerifiedByEmail !='Y'){
			if(appOTPVerifiedByMobile =='Y'){}
			else
			refreshCaptcha('CommonOtpOthercapImage');
		}
	}else{
		if(loanQuoteLoanPurposeId!=23 && appOTPVerifiedByMobile !='Y'){
			refreshCaptcha('CommonOtpOthercapImage');
		}
	}
	
	
/* 	$("#commonOTP").modal({
		show:true,
		backdrop:"static",
		keyboard:false
	}); */
	
	//code to show cbs OTP popup (after bootstrap v5 upgrade)	
	const commonOTPEl = document.getElementById('commonOTP');
	const commonOTPModal = new bootstrap.Modal(commonOTPEl, {
	  backdrop: 'static',       
	  keyboard: false
	});
  
	commonOTPModal.show();
	
	//$("#commonOTP").modal("toggle");
	
	globalMobile='<s:property value="%{appForm.appMobileNo}"/>';
	globalEmail='<s:property value="%{appForm.appWorkEmail}"/>';
</script>
<!-- OTP Box -->
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>		
</s:if> 
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.commonOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
