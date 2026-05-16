<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{isDsrPage=='false'}">
	<s:if test="%{#session.bankLMSUser==null}">
		<s:if test="%{appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified.equalsIgnoreCase('N')}">
			<!-- Get a callback popup -->
			<div class="modal otp-box" id="productOTP" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			  <div class="modal-dialog" role="document">
				<div class="modal-content">
				  <div class="modal-body">
				  <%-- <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/closelight.png" alt=""/></span></button> --%> 
					<div class="verify-otp-left-section verify-mobile-otp">
					</br>
					</br>
						<h2><span>Verify</span> your mobile</h2>	
						<p>(Please enter your contact details for OTP process)</p>
					</div>
					<form name="otpApplication" id="otpApplication" method="post" action="javascript:void(0);" autocomplete="off"  >
					<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
						<div class="otp-right-section">
						</br>
						</br>
							<ul class="otp-form">
						 <%--	<div id="privacyStatementHTML" class="privacyStatementHTML" name="privacyStatementHTML">--%> 
							<div id="otpPopHome">
								<s:if test="%{quote.loanQuoteResidentTypeId==2}">
									<li>
										<label>Applying from?<b class="req">*</b></label>
										<s:if test="%{appForm != null && appForm.appApplyingFrom == 2}">
											<div class="col-xs-12 mrgt-9">
												<div class="blue-radio blue-radio-danger">
													<input id="appApplyingFromIndiaYes" value="1" name="appApplyingFrom" type="radio" class="disabledFields">
													<label for="appApplyingFromIndiaYes"> India </label>
												</div>	
												<div class="blue-radio blue-radio-danger">
													<input id="appApplyingFromIndiaNo" value="2" name="appApplyingFrom" class="disabledFields" checked="checked" type="radio">
													<label for="appApplyingFromIndiaNo">Abroad </label>
												</div>
											</div>	
										</s:if>
										<s:else>
											<div class="col-xs-12 mrgt-15">
												<div class="blue-radio blue-radio-danger">
													<input id="appApplyingFromIndiaYes" value="1" name="appApplyingFrom" checked="checked" type="radio">
													<label for="appApplyingFromIndiaYes"> India </label>
												</div>	
												<div class="blue-radio blue-radio-danger">
													<input id="appApplyingFromIndiaNo" value="2" name="appApplyingFrom" type="radio">
													<label for="appApplyingFromIndiaNo">Abroad </label>
												</div>
											</div>	
										</s:else>
									</li>
									<div id="mobileDetailsOTP">
										<li>
											<label>Enter your mobile no. <b class="req">*</b></label>
											<s:if test="%{appForm != null && appForm.appApplyingFrom == 2}">
						    					<s:textfield id="appNRIMobileNo" name="nriMobileNo" maxlength="12" value="%{#session.mobile}" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" cssClass="form-control" autocomplete="off"/>
						    					<div class="custom-tooltip">Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.</div>
						    				</s:if>
							    			<s:else>
							    				<s:textfield id="mobile" name="mobile" maxlength="10" value="%{#session.mobile}" placeholder="Mobile no ." onkeydown="return M.digit(event);" cssClass="form-control " autocomplete="off"/>
							    			</s:else>
										</li>
									</div>
								</s:if>
								<s:else>
									<li>
										<s:hidden id="appOTPVerifyType" name="appOTPVerifyType" value="0"/>
										<s:hidden id="appApplyingFrom" name="appApplyingFrom" value="1"/>
										<label>Enter your mobile no. <b class="req">*</b></label>
										<s:textfield id="mobile" name="mobile" maxlength="10" value="%{#session.mobile}" readonly="true"  placeholder="Mobile no 2 ." onkeydown="return M.digit(event);" cssClass="form-control disabled" autocomplete="off"/>
									</li>
								</s:else>
								<s:hidden id="isdCode" name="isdCode" value="%{appForm.appISDCode}"/>
								<li>
									<label>Email <b class="req">*</b></label>
									<s:textfield id="emailOTP" name="email" maxlength="60" value="%{#session.email}"  readonly="true" cssClass="form-control disabled" autocomplete="off" placeholder="Email  "/>
								</li>					
								 <li class="pp-captcha" id="captchaDiv">
						 			<label>IDENTIFY YOURSELF<b class="req">*</b>
                                       	<span>Copy text into input box</span></label>								
									<!-- <img id="homeCommonOtpcapImage1" name="homeCommonOtpcapImage1" src="Captcha.jpg" class="captcha-img">
									<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('homeCommonOtpcapImage1');"> -->
									<img id="homeCommonOtpcapImage" name="homeCommonOtpcapImage" src="Captcha.jpg" class="captcha-img">
									<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('homeCommonOtpcapImage');">
									<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
									<input type="text" class="form-control captcha-inpt" id="homecaptcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
								</li>
							</div>
							<%--</div>--%>
							
							<div id="otp_verify_type" style="display: none;">
							</br>
							</br>
									<li>
										<label>OTP verify by?<b class="req">*</b></label>
										<div class="col-xs-12 mrgt-9">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" id="appOTPVerifyTypeYes" value="0" name="appOTPVerifyType" checked="checked" >
												<label for="appOTPVerifyTypeYes">Mobile</label>
											</div>	
											<div class="blue-radio blue-radio-danger">
												<input type="radio" id="appOTPVerifyTypeNo" value="1" name="appOTPVerifyType">
												<label for="appOTPVerifyTypeNo">Email</label>
											</div>
										</div>	
									</li>
								</div>
							
								<%-- <s:if test="%{appForm.appMobileVerificationCode==null}"> --%>
									<div id="overlay-row_otp_hl">
										<li>
										<!-- </br> -->
											<button  class="track-btn conf-track-btn" id="sendOpt" name="sendOpt" type="submit">Send OTP</button>
										</li>
									</div>
									<div id="OtpPopHl1">
									<div id="OtpPopHl">
					    			<div id="otp_row_confirm" >
						    			<li id="otp_row_confirm_html">
							    			<label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>
							    			<input id="inputOtp" name="inputOtp" type="text" class="form-control secure-otp"  maxlength="6" onkeydown="return M.digit(event);" autocomplete="off">
							    			<input id="inputOtp1" name="inputOtp" type="hidden" class="form-control secure-otp"  onkeydown="return M.digit(event);" autocomplete="off">
							    	
						    			</li>
                              			 <li class="pp-captcha" id="captchaDiv">
												<label>IDENTIFY YOURSELF<b class="req">*</b>
	                                         	<span>Copy text into input box</span></label>								
												<img id="CommonOtpcapImage" name="CommonOtpcapImage" src="Captcha.jpg">
												<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('CommonOtpcapImage');">
												<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
												<input type="text" class="form-control captcha-inpt" id="comncaptcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
											</li>
						    			<li>
						    			    
							    			<button  class="track-btn conf-track-btn" id="confirmOtp" name="confirmOtp" type="submit">Confirm</button>
					    					<button  class="resend-btn" id="resendOtp" name="resendOtp" type="submit">Resend</button>
						    			</li>
						    		</div>
								<%-- </s:else> --%>
							</div>
							</div>
								<!-- <div class="clear"></div>
								<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li> -->
							</ul>
							<div class="clear"></div>
	    					<!-- <li id="errorOTPMsg" class="" style="display:none;"></li> -->
							<%-- <span class="otp-loader"><img id="opt-loader-application" style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span> --%>
			    			<div id="errorOTPMsg" style="display:none;"></div>  
						</div>
				  	</form>
				  </div>
				</div>
			  </div>
			</div>
			
	
			<!-- Get a callback popup -->
			<script type="text/javascript">
        	/* 	$("#productOTP").modal({
        			show:true,
        			backdrop:"static",
        			keyboard:false
        		}); */
        		
        		
        		const productOTPEl = document.getElementById('productOTP');
        		const productOTPModal = new bootstrap.Modal(productOTPEl, {
        		  backdrop: 'static',       
        		  keyboard: false
        		});
              
        		productOTPModal.show();
        		</script>
        		</s:if>
        		</s:if>
        	</s:if>	
        	
        	 <s:if test="%{isDsrPage=='false'}">
	<s:if test="%{#session.bankLMSUser==null}">
		
<div class="modal otp-box" id="OTP1" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			  <div class="modal-dialog" role="document">
			<div class="modal-content" >
				  <div class="modal-body">
                             <button type="button"  onclick="showAlternateDiv();" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>" alt=""/></span></button> 
	<div class="verify-otp-left-section verify-mobile-otp">
					</br>
					</br>
						<h2><span>Verify</span> your mobile</h2>	
						<p>(Please enter your contact details for OTP process)</p>
					</div>
					<form name="otpApplicationAlt" id="otpApplicationAlt" method="post" action="javascript:void(0);" autocomplete="off">
					<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
						<div class="otp-right-section">
						</br>
						</br>
							<ul class="otp-form">
							<div id="privacyStatementHTML" class="privacyStatementHTML" name="privacyStatementHTML"> 	
							<div id="otpPopHome1">
							<!-- <div id="otpPopHome" name="otpPopHome" id="otpPopHome"> -->
								
								
									<li>
										<s:hidden id="appOTPVerifyType" name="appOTPVerifyType" value="0"/>
										<s:hidden id="appApplyingFrom" name="appApplyingFrom" value="1"/>
										<label>Enter your Alternate mobile no. <b class="req">*</b></label>
											<s:hidden name="isdCodeAlt" id="isdCodeAlt" value= "91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" onkeydown="return M.digit(event);"/>
										<s:textfield id="alternateMobileNumber2" name="alternateMobileNumber" maxlength="15" value="" placeholder="Alternate Mobile no." onkeydown="return M.digit(event);" cssClass="form-control" autocomplete="off" readonly='true' />
                                     </li>
								
									<div id="overlay-row_otp_hl_alt">
										<li>
											<button class="track-btn conf-track-btn" id="sendOptAlt" name="sendOptAlt" type="submit" >Send OTP </button>
										</li>
									</div>
							</div>
							</div>
							
							<div id="otp_verify_type" style="display: none;">
									<li>
										<label>OTP verify by?<b class="req">*</b></label>
										<div class="col-xs-12 mrgt-9">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" id="appOTPVerifyTypeYes" value="0" name="appOTPVerifyType" checked="checked" >
												<label for="appOTPVerifyTypeYes">Mobile</label>
											</div>	
										</div>	
									</li>
							</div>
							
								
									
								
									<div id="OtpPopHl2" class="OtpPopHl" style="display: none;">
									
										<div id="otp_row_confirm" >
						    			     	<li id="otp_row_confirm_html">
								    			<label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>
								    			<input id="inputOtpAlt" name="inputOtpAlt" type="text" class="form-control secure-otp" maxlength="6" onkeydown="return M.digit(event);" autocomplete="off">
								    			<input id="inputOtpAlt1" name="inputOtpAlt" type="hidden" class="form-control secure-otp"  onkeydown="return M.digit(event);" autocomplete="off">
											 	
							    			    </li>
											       <li>
											       </li>
	                                                                          			
							    			<li>
								    			<button  class="track-btn conf-track-btn" id="confirmOtpAlt" name="confirmOtpAlt" type="submit">Confirm</button>
						    					<button  class="resend-btn" id="resendOtpAlt" name="resendOtpAlt" type="submit">Resend</button>
							    			</li>
							    		</div>
						    		</div>
							</ul>
							<div class="clear"></div>
	    					<li id="errorOTPMsg1" class="" style="display:none;"></li>
							<span class="otp-loader"><img id="opt-loader-application" style="display:none;" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
			    			<div id="errorOTPMsg1" style="display:none;"></div>
						</div>
				  	</form>
				  </div>
				</div>
			  </div>
			</div>
</div>

        		</s:if>
        	</s:if>
        	

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloanOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.concat.min.js"></script>
<%-- <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>		 --%>
		<script>
	
		
	
	
	
	
	
	
	
		
		
