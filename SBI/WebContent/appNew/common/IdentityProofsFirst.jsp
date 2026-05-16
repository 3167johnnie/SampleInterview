<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li>
	<label>PAN<b class="req">&nbsp;*</b><font style="font-size:12px;"> (<a href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN" target="_blank">Verify Your PAN</a>)</font></label>
	<s:textfield placeholder="Enter PAN" name="quote.loanQuotePanCardNo"  id="appPanCardNo1" value="%{quote.loanQuotePanCardNo}" maxlength="10"
	cssClass="form-control" disabled="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'true':'false'}" />
	<s:if test="%{isDsrPage=='false'}">
		<div class="custom-tooltip">Enter valid PAN</div>
	</s:if>

	<%-- <div id="id-sm-chkbox" class="sm-chkbox" style="display: <s:property value="%{quote!=null && quote.loanQuoteLoanPurposeId!=27?'block':'block'}"/>;">
		<input type="checkbox" name="quote.loanQuotePanCardLater" id="appPanCardLater1" 
		value="<s:property value="%{(quote!=null && quote.loanQuotePanCardLater)?'true':'false'}"/>" 
	
		<s:property value="%{(quote!=null && quote.loanQuotePanCardLater)?'checked=checked':''}"/>
	     
 		<label class="label-content" for="appPanCardLater1">I would like to submit my PAN later<span class="req">&nbsp;*</span></label>
 		
	</div> --%>
</li>
<s:if test="%{quote.loanQuoteLoanPurposeId != 27}">
	 <li>
		<label>Do you have Aadhaar Number?</label>
		<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
			<s:if test="%{quote !=null && quote.loanQuoteHaveAadhaarNumber!=null && quote.loanQuoteHaveAadhaarNumber==1}">
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarYes" value="1" checked="checked">
					<label for="haveAadhaarYes"> Yes </label>
				</div>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarNo" value="2" >
					<label for="haveAadhaarNo"> No </label>
				</div>
			</s:if>
			<s:elseif test="%{quote !=null && quote.loanQuoteHaveAadhaarNumber!=null && quote.loanQuoteHaveAadhaarNumber==2}">
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarYes" value="1">
					<label for="haveAadhaarYes"> Yes </label>
				</div>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarNo" value="2" checked="checked">
					<label for="haveAadhaarNo"> No </label>
				</div>
			</s:elseif>
			<s:else>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarYes" value="1" checked="checked">
					<label for="haveAadhaarYes"> Yes </label>
				</div>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveAadhaarNumber" id="haveAadhaarNo" value="2" >
					<label for="haveAadhaarNo"> No </label>
				</div>
			</s:else>
		</div>
		<div id="aadhaarConfirmation" class="adh-msg"></div>
	</li>	
	<%-- <li>
		<label>Aadhaar number<b class="req">&nbsp; </b></label>
		<s:textfield placeholder="Enter Aadhaar number" name="quote.loanQuoteAadhaarNumber"  id="appAadhaarNumber" value="%{appForm.appAadhaarNumber}"  cssClass="form-control" minLength="12" maxlength="12" onkeydown="return M.digit(event);" />
	</li> --%>
	<li>
		<label>Other identity proof<b class="req">&nbsp;</b></label>
		<div class="flat-field">
			<s:select list="#{'0':'Select Id Proof','165':'Voter Id','166':'Passport','167':'Driving License','160':'Ration Card'}" name="quote.loanQuoteOtherId" id="appIdProof1" value="%{quote.loanQuoteOtherId}" cssClass="form-select" onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<div id="AppOtherIdNo">
		<s:if test="%{quote.loanQuoteOtherId!=null && quote.loanQuoteOtherId!=0}">
			<s:include value="/appNew/loan/personal/OtherIdentityNo.jsp"></s:include>
		</s:if>
		
	
	</div>
</s:if>
