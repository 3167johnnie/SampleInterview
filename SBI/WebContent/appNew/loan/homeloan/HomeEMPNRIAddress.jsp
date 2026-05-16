<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label>Address line 1<b class="req">&nbsp;*</b></label><br>
	<s:textfield name="appForm.appEMPNRIAddress1"  id="appEMPNRIAddress1" value="%{appForm.appEMPNRIAddress1}" maxlength="100"
	 cssClass="form-control" onblur=""/>
   <s:if test="%{isDsrPage=='false'}">
			<div class="custom-tooltip">Enter your address</div>
	</s:if>
</li>
<li>
	<label>Address line 2</label><br>
	<s:textfield name="appForm.appEMPNRIAddress2"  id="appEMPNRIAddress2" value="%{appForm.appEMPNRIAddress2}"   maxlength="40" cssClass="form-control" onblur=""/>
</li>
<li>
	<label>Landmark</label><br>
	<s:textfield name="appForm.appEMPNRIAddressLandmark"  id="appEMPNRIAddressLandmark" value="%{appForm.appEMPNRIAddressLandmark}" maxlength="40" cssClass="form-control" onblur=""/>
</li>
<li>
	<label class="">City<b class="req">*</b></label>
	<s:textfield name="appForm.appEMPNRICity"  id="appEMPNRICity" value="%{appForm.appEMPNRICity}" maxlength="40" cssClass="form-control" onblur=""/>
</li>
<li>
	<label class="">State<b class="req">*</b></label>
	<s:textfield name="appForm.appEMPNRIState"  id="appEMPNRIState" value="%{appForm.appEMPNRIState}" maxlength="40" cssClass="form-control" onblur=""/>
</li>
<li class="div-left" id="">
	<label class="">Country<b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="appForm.appEMPCountryId" id="appEMPCountryId"  value="%{appForm.appEMPCountryId}" list="%{#countries}" cssClass="form-select"  autocomplete="off" headerKey="0" headerValue="Select country" onfocus="customOnFocus(this);"/>
	</div>
</li>
<li>
	<label class="">Zipcode/Pincode<b class="req">*<s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	<s:textfield name="appForm.appEMPNRIZipcode"  id="appEMPNRIZipcode" value="%{appForm.appEMPNRIZipcode}" maxlength="15" cssClass="disabledFields form-control"  onkeydown="return M.isAlphaNumeric(event);"/>
</li>
