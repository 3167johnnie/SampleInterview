



<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#session.bankLMSUser==null}">
	<s:if test="%{appForm==null || appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified.equalsIgnoreCase('N')}">
		<!-- Get a callback popup -->
		<div class="modal fade otp-box" id="productCallBack" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" >
			<div class="modal-dialog" role="document">
				<div class="modal-content">
					<div class="modal-body common-blue-bg">
						<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close" onClick="refreshCaptcha('captchaFirstPage')">
							<span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" /></span>
						</button>
						<div class="verify-otp-left-section">
							<h2>
								<span>Get a</span> callback
							</h2>
							<p>Enter your contact details and we will revert back shortly.</p>
						</div>
						<div class="otp-right-section">
							<form name="call_back" id="call_back" method="post" autocomplete="off">
							<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
							<div id="callInformation">
									<ul class="otp-form" id="callBackData">
										<li>
											<label>Full name<b class="req">*</b></label>
											<s:textfield id="name" name="name" cssClass="form-control" maxlength="20" value="" onkeydown="return M.isChars(event);" />
										</li>
										<div style="display: none;">
											<li>
												<label>Applying from?<b class="req">*</b></label>
												<div class="col-xs-12 col-sm-6">
													<div class="blue-radio blue-radio-danger m-left-20">
														<input type="radio" name="appApplyingFrom" id="appApplyingFromIndiaWantUsYes" checked="checked" value="1">
														<label for="appApplyingFromIndiaWantUsYes"> Yes </label>
													</div>	
													<div class="blue-radio blue-radio-danger">
														<input type="radio"  name="appApplyingFrom" id="appApplyingFromIndiaWantUsNo" value="2" >
														<label for="appApplyingFromIndiaWantUsNo">No </label>
													</div>
												</div>
											</li>
										</div>
										<li>
											<label>Preferred language<b class="req">*</b></label>
											<div class="flat-field">
												<s:select id="leadLanguageId" name="leadLanguageId" value="0" list="#{1:'Hindi', 2:'English'}" headerKey="0" headerValue="Select preferred language" cssClass="form-select modal-select" autocomplete="off" />
											</div>
										</li>
										</br>
										
										<li>
											<label class="block">Mobile no.<b class="req">*</b></label>
											<s:textfield id="isdCodeWantUsToCallYou" cssClass="cs-input flt form-control" name="isdCodeWantUsToCallYou" maxlength="4" value="%{91}" placeholder="ISD code" readonly="true" />
											<s:textfield id="mobileWantUsToCallYou" cssClass="cb-input ml10 flt form-control" name="mobileWantUsToCallYou" maxlength="10" value="" placeholder="Mobile no." onkeydown="return M.digit(event);" />
											<span class="privacy-note"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/lock-img.png" />Your privacy is protected</span>
										</li>
										<li>
											<label>Email<b class="req">*</b></label>
											<s:textfield id="email" name="email" cssClass="form-control" maxlength="40" value="" />
											<s:hidden name="CommmonloanType" id="CommmonloanType" />
										</li>
										<li class="pp-captcha">
											<label>IDENTIFY YOURSELF<b class="req">*</b>
                                         	 <span>Copy text into input box</span></label>								
												<img id="CommonClcapImageInform" name="CommonClcapImageInform" src="Captcha.jpg">
												<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('CommonClcapImageInform');">
												<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
												<input type="text" class="form-control captcha-inpt" id="popcaptchaInform" name="captcha" value="" maxlength="6" placeholder="enter captcha" >												
										</li>
										<div id="overlay-row_otp">
										    </br>
											<li>
												<button class="track-btn conf-track-btn" id="callBankSubmit" type="submit">Submit</button>
											</li>
										</div>
									</ul>
								</div>
									<div id="wantus_row_confirm" >
									<ul class="otp-form">
											<li>
												<label>Enter code sent by SMS<b class="req">*</b></label>
												<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp"  maxlength=6 onkeydown="return M.digit(event);" />
												<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp"  onkeydown="return M.digit(event);" />
												
											</li>
											<li class="pp-captcha">
												<label>IDENTIFY YOURSELF<b class="req">*</b>
	                                         	 <span>Copy text into input box</span></label>								
													<img id="CommonClcapImage" name="CommonClcapImage" src="Captcha.jpg">
													<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('CommonClcapImage');">
													<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
													<input type="text" class="form-control captcha-inpt" id="popcaptcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" >												
											</li>
											
											<li>
										
												<button class="track-btn conf-track-btn" id="confirmOtpWantUs" type="submit">Confirm</button>
												<button class="resend-btn" id="resendOtpWantUs" type="submit">Resend</button>
											</li>
										</ul>
									</div>
									<li id="errorOTPMsgCallback" class="error-msg-cbs" style="display: none;"></li>
								</ul>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- Get a callback popup -->
	</s:if>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.commonCallback.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
