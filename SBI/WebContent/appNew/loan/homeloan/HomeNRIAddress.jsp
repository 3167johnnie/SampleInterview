<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{quote.loanQuoteResidentTypeId==2}">
	<div class="clearfix"></div>
		<h3>CURRENT ADDRESS <span class="small-txt">(ABROAD)</span></h3>
		<ul class="form-section">
			<li>
				<label>Address line 1<b class="req">*</b></label><br>
				<s:textfield name="appForm.appNRIAddress1"  id="appNRIAddress1" value="%{appForm.appNRIAddress1}" maxlength="100"
				 cssClass="form-control" onblur=""/>
				<s:if test="%{isDsrPage=='false'}">
			       <div class="custom-tooltip">Enter your address.</div>
				</s:if>
			</li>
			<li>
				<label>Address line 2</label>
				<s:textfield name="appForm.appNRIAddress2"  id="appNRIAddress2" value="%{appForm.appNRIAddress2}"   maxlength="40" cssClass="form-control" onblur=""/>
			</li>
			<li>
				<label>Landmark</label>
				<s:textfield name="appForm.appNRIAddressLandmark"  id="appNRIAddressLandmark" value="%{appForm.appNRIAddressLandmark}" maxlength="40" cssClass="form-control" onblur=""/>
			</li>
			<li >
				<label class="">City<b class="req">*</b></label>
				<s:textfield name="appForm.appNRICity"  id="appNRICity" value="%{appForm.appNRICity}" maxlength="40" cssClass="form-control" onblur=""/>
			</li>
			<li >
				<label class="">State<b class="req">*</b></label>
				<s:textfield name="appForm.appNRIState"  id="appNRIState" value="%{appForm.appNRIState}" maxlength="40" cssClass="form-control" onblur=""/>
			</li>
			<li >
				<label class="">Country<b class="req">*</b></label>
				<div class="flat-field">
					<s:select name="appForm.appCountryId" id="appCountryId"  value="%{appForm.appCountryId}" list="%{#countries}" cssClass="disabledFields form-select"  autocomplete="off" headerKey="0" headerValue="Select country" disabled="true" onfocus="customOnFocus(this);"/>
				</div>
			</li>
			<li >
				<label class="">Zipcode/Pincode<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
				<s:textfield name="appForm.appNRIZipcode"  id="appNRIZipcode" value="%{appForm.appNRIZipcode}" maxlength="15" cssClass="disabledFields form-control"  onkeydown="return M.isAlphaNumeric(event);"/>
			</li>
		</ul>
</s:if>
