<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
<!-- Get a callback popup -->
<div class="modal fade otp-box" id="getACallBack" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
					<span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt="" /></span>
				</button>
				<div class="otp-left-section">
				<!-- <br> -->
				<br>
					<h2><span>Get a</span> Callback</h2> 
					
					<p>(Please enter your contact details that we will revert back to you )</p>
				</div>
				<div class="otp-right-section" style="height: 825px;">
				<br>
				<!-- <br> -->
					<div id="beforeGetACallBack" style="display: block;">
					
<!-- 						<form name='homePageCallback' id='homePageCallback' method='post' action='javascript:void(0);' enctype='application/x-www-form-urlencoded' onsubmit="return submit_first_page_bind();" autocomplete="off">
 -->						<form name='homePageCallback' id='homePageCallback' method='post'  action='javascript:void(0);' enctype='application/x-www-form-urlencoded'  autocomplete="off">
							<ul class="otp-form" id="homeCallBack">
								<li>
									<label>Loan<b class="req">*</b></label>
									<div class="flat-field">
									
<%-- 										<s:select id="leadLoanPurposeId" name="lead.leadLoanPurposeId" value="" 
										list="#{3:'Auto Loan',11:'EL Takeover',9:'Global Ed-vantage',1:'Home Loan',2:'Home Top Up Loan',7:'Loan against Immovable property',4:'Personal Loan: Salary account with SBI',12:'Phone Banking',8:'Scholar Loan',15:'AGRI Loan'}" 
										headerKey="0" headerValue="Select loan purpose" cssClass="form-control" autocomplete="off" onchange="consentCallForAutoLoan()"/>  --%>
											<s:select id="leadLoanPurposeId" name="lead.leadLoanPurposeId" value="" 
										list="#{3:'Auto Loan',11:'EL Takeover',9:'Global Ed-vantage',1:'Home Loan',2:'Home Top Up Loan',7:'Loan against Immovable property',4:'Personal Loan: Salary account with SBI',12:'Phone Banking',8:'Scholar Loan'}" 
										headerKey="0" headerValue="Select loan purpose" cssClass="form-select modal-select " autocomplete="off" /> 
									</div>
								</li>
								<li>
									<label>Name<b class="req">*</b></label>
<%-- 									<s:textfield id="leadFirstName" name="lead.leadFirstName" value="" cssClass="form-control" placeholder="Name" maxlength="32" autocomplete="off" onkeydown="return M.isChars(event);"/>
 --%>									<s:textfield id="leadFirstName" name="lead.leadFirstName" value="" cssClass="form-control" placeholder="Name" maxlength="32" autocomplete="off" />
								</li>
								
								<div id="applying-from" style="display: none;">
									<%-- <li>
										<label>Applying from?<span class="mandatory">*</span></label>
										<div class="col-xs-12 mrgt-10-10">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" checked="checked" >
												<label for="leadApplyingFromIndiaWantUsYes">India</label>
											</div>	
											<div class="blue-radio blue-radio-danger">
												<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsNo" value="2" <s:property value="%{(lead!=null && lead.leadApplyingFrom == 1)?'checked=\"checked\"':''}"/> >
												<label for="leadApplyingFromIndiaWantUsNo">Abroad</label>
											</div>
										</div>
									</li> --%>
									<input type="radio" name="lead.leadApplyingFrom" id="leadApplyingFromIndiaWantUsYes" value="1" checked="checked" >
								</div>
								<%-- <div id="country-div" style="display:<s:property value="%{(lead!=null && lead.leadApplyingFrom == 2)?'block':'none'}"/>;" >
									<li>
										<label>Country<span class="mandatory">*</span></label>
										<div class="flat-field">
											<s:select list="%{#countries}" id="leadCountryId" name="lead.leadCountryId" value="%{lead.leadCountryId>0?lead.leadCountryId:91}" 
												headerKey="0" headerValue="Select country" cssClass="form-control"/>
										</div>
									</li>
								</div> --%>
								<div id="city-div" style="display:<s:property value="%{(lead==null || lead.leadApplyingFrom == 1)?'block':'none'}"/>;" >
									<li>
										<label>City<b class="req">*</b></label>
										<div class="flat-field">
											<s:select list="%{#citiesOtp1==null?'':#citiesOtp1}" id="leadCityId" name="lead.leadCityId" value="" headerKey="0" headerValue="Select city" cssClass="form-select modal-select"/>
										</div>
									</li>
								</div>
								<li>
									<label>Preferred Language<b class="req">*</b></label>
									<div class="flat-field">
										<s:select id="leadLanguageId" name="lead.leadLanguageId" value="" list="#{1:'Hindi', 2:'English'}" 
											headerKey="0" headerValue="Select Preferred Language" cssClass="form-select modal-select" autocomplete="off"/>
									</div>
								</li>
								<li>
									<label>Mobile<b class="req">*</b></label>
									<%-- <s:if test="%{lead!=null && lead.leadApplyingFrom == 2}">
										<s:textfield id="appNRIMobileNo" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="12" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);"/>
									 <div class="custom-tooltip">Enter mobile no. without ISD code or &#8216;0&#8217.</div>
									</s:if>
									<s:else>
										<s:textfield id="mobile" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="10" placeholder="Mobile No." onkeydown="return M.digit(event);"/>
									</s:else> --%>
									<s:textfield id="mobile" name="lead.leadMobileNo" value="" cssClass="form-control" autocomplete="off" maxlength="10" placeholder="Mobile No." />
									<!-- <div class="custom-tooltip">Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.</div> -->
								</li>
								<li>
									<label>Email</label>
									<s:textfield id="leadWorkEmail" name="lead.leadWorkEmail" value="" placeholder="Email" cssClass="form-control" autocomplete="off" maxlength="40" />
								</li>
									<li class="pp-captcha">
										<label>IDENTIFY YOURSELF<b class="req">*</b>
                                          <span>Copy text into input box</span></label>								
 											<img id="HOMEPocapImage" name="HOMEPocapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HOMEPocapImage');">
											<!-- <img id="HOMEPocapImage" name="HOMEPocapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" > -->
											<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
											<input type="text" class="form-control captcha-inpt" id="captcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" >												
									</li>
									</br>
								<li class="extra-margin-a">
									<div class="">
										<input type="checkbox" id="infoprovide" name="infoprovide" class="blue-css-checkbox colorAuto" style="left:auto!important;" required>
										<!-- <label class="label-content" for="infoprovide">
											I agree to be contacted by <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> over phone or e-mail.
										<b class="req">*</b></label> -->
										
										<!-- Commented by Pratima for Auto Loan Consent Changes -->
										<%-- <label for="infoprovide" class="label-content">  I hereby authorize <s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/> / its group companies and/or their representatives to contact me via calls, mails and/or text messages on the contact details so provided to offer me the product offerings of SBI/Its group companies. This consent will override any registration for DND/NDNC/NCPR. &nbsp;<b class="req">*</b></label> --%>
										<!-- Commented by Pratima for Auto Loan Consent Changes -->
										
										<!-- Added by Pratima for Auto Loan Consent Changes -->
										<div id="consentCallForAllLoan" class="consentCallForAllLoan">
											<label class="label-content" for="infoprovide" style="margin-left:20px!important;">
												<s:property escapeHtml="false" value="%{@com.mintstreet.common.bo.UIBeanListStatic@consentCallback}" />
												&nbsp;<b class="req">*</b>
											</label>
										</div>


										<div id="consentCallForAutoLoan" class="consentCallForAutoLoan" style="display:none;">
										<label for="infoprovide" class="label-content consentScrollForAutoCallback" style="margin-left:20px!important;">
										I/We hereby agree and authorize the Bank to share, transmit, disclose, exchange, or use in any manner whatsoever, without any further specific consent or authorization from me/us, the information/data provided by/related to me/us including details of my/our account(s) to the Group Companies/Associates/Subsidiaries/Affiliates/Joint Ventures of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/> /any person or third party agencies/service providers whether located in India or overseas with whom the Bank has entered/propose to enter  into contracts/arrangements for provision of 'services/products' for the purpose of marketing/offering/selling any product/services and/or availing support services of any nature by the Bank.
										I/We hereby agree and consent to receive marketing communications through telephone calls/E-mail/SMS/WhatsApp	messages	on	various products / features / promotion offers provided by the Bank and its Group Companies/Associates/Subsidiaries/Affiliates/Joint Ventures. 
										I/We also acknowledge and agree that the consent hereby provided by me/us shall be legally binding on me/us irrespective of my/our registration with DNDINCPR registries and shall override such registrations.
										Notwithstanding the above, I/We acknowledge and agree that the Bank may at its absolute discretion disclose any of my/our information, if required or permitted by any law, rule or regulation or at the request/direction of any statutory or regulatory authority or court of law or if such disclosure is required for the purposes of preventing any fraud, without any further specific consent or authorization from me/us.
								&nbsp;<b class="req">*</b>
										</label>
										</div>
										<!-- Added by Pratima for Auto Loan Consent Changes -->
										
										
									</div>
								</li>
							<!-- 	<br>--> </br>
                                <br> </br>
                                <br> 
								<li>
									<button type="submit" id="callBankSubmit" class="track-btn conf-track-btn" >SUBMIT</button>
								</li>
							</ul>
						</form>
					</div>
					<div id="afterGetACallBack" style="display: none;">
						<form  name="call_back" id="call_back" method="post" autocomplete="off">
							<ul class="otp-form" id ="bigGetAcallBcak">
			    				<div id="resend_box" style="display:none;">
									<!-- <li>
										<label>Enter your Mobile No.<b class="req">*</b></label>
										<input id="resendMobile" name="mobile" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">
									</li>
									<li>
										<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">Resend</button>
										<button class="resend-btn" id="cancelResend" name="cancelResend" type="submit">Cancel</button>
									</li> -->
								</div>
					    		<div id="wantus_row_confirm" style="display:block;">
					    			<li>
						    			<label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>
<!-- 						    			<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp"  maxlength="6" onkeydown="return M.digit(event);"/>
 -->						    			<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp"  maxlength="6" "/>
<!-- 						    			<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp" onkeydown="return M.digit(event);"/>
 -->						    			<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp" />
					    			</li>
					    			  <li class="pp-captcha" id="captchaDiv">
										<label>IDENTIFY YOURSELF<b class="req">*</b>
                                          <span>Copy text into input box</span></label>								
											<img id="HomePopcapImage" name="HomePopcapImage" src="Captcha.jpg">
											<!-- <a class="refresh-link"  href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HomePopcapImage');"> -->
											<a class="refresh-link" id="refresh_link_Captche" href="javascript:void(0);" title="Refresh" ">
											<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
											<input type="text" class="form-control captcha-inpt" id="otpHomecaptcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
									</li>
					    			<li>
						    			<button  class="track-btn conf-track-btn" id="confirmOtpWantUs" name="confirmOtpWantUs" type="submit">Confirm</button>
				    					<button  class="resend-btn" id="resendOtpWantUs" name="resendOtpWantUs" type="submit">Resend</button>
					    			</li>
					    		</div>
							</ul>
						</form>
					</div>
			    	<div class="clear"></div>
    				<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Get a callback popup -->

<div id="loanCities" class="hide">
	<s:select list="%{#citiesOtp1==null?'':#citiesOtp1}" id="leadCityIdHidden" name="lead.leadCityId" value="%{lead.leadCityId}" headerKey="0" headerValue="Select city" cssClass="form-select"/>
</div>	
<div id="cardCities" class="hide">
	<s:select list="%{@com.mintstreet.common.bo.UIBeanListStatic@cardCityGroup1==null?'':@com.mintstreet.common.bo.UIBeanListStatic@cardCityGroup1}" 
	id="cardCityGroup1" name="lead.leadCityId" value="%{lead.leadCityId}" headerKey="0" headerValue="Select city" cssClass="form-select ">
		<s:optgroup list="%{@com.mintstreet.common.bo.UIBeanListStatic@citiesCard==null?'':@com.mintstreet.common.bo.UIBeanListStatic@citiesCard}" label="--------------"></s:optgroup> 
	</s:select>
</div>	
<%-- <script type="text/javascript">
	function fetchJsonData(){
		jsonJSArray = '<s:property value="%{jsonJSArray}"/>';
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	min_age_year = 18;
	max_age_year = 70;
	
	$("#getACallBack").modal({
		show:false,
		backdrop:"static",
		keyboard:false
	});
	function getACallBackId(){
		$("#captcha").val('');
		refreshCaptcha('HOMEPocapImage');
		reinitiateCallback();
		$("#getACallBack").modal();
	}

	function reinitiateCallback(){
		$("#leadLoanPurposeId").val(0).removeClass('error').next('span').remove();
		$("#leadFirstName").val('').removeClass('error').next('span').remove();
		$("#leadApplyingFromIndiaWantUsYes").trigger("click");
		$("#leadCityId").val(0).removeClass('error').next('span').remove();
		$("#leadLanguageId").val(0).removeClass('error').next('span').remove();
		$("#mobile").val('').removeClass('error').next('span').remove();
		$("#mobile").removeAttr('readonly');
		$("#leadWorkEmail").val('').removeClass('error').next('span').remove();
		$("#infoprovide").removeClass('error');
		$("#infoprovide").attr('checked', false);
		$("#beforeGetACallBack").show();
		$("#afterGetACallBack").hide();
		hidePOPMsg('errorOTPMsg');
	}
</script> --%>
		<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.HomePopups.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/homesbi.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<!-- Added by Pratima for Auto Loan Consent Changes -->
<%-- <script>
function consentCallForAutoLoan(){
	var autoLoan  = $("#leadLoanPurposeId").val();

	if (autoLoan == 3) {
		$('#consentCallForAllLoan').hide();
		$('#consentCallForAutoLoan').show();
   }
   else {
	  $('#consentCallForAllLoan').show();
	  $('#consentCallForAutoLoan').hide(); 
   }
}
</script> --%>
