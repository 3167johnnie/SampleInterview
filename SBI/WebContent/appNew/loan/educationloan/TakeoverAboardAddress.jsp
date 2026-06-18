<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{quote.loanQuoteResidentTypeId==2}">
			<li>
				<label>Address line 1<b class="req">*</b></label><br>
				<s:textfield name="appForm.appAbroadAddress1" id="appAbroadAddress1" value="%{appForm.appAbroadAddress1}" maxlength="100" cssClass="form-control" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
				<s:if test="%{isDsrPage=='false'}">
			       <div class="custom-tooltip">Enter your address.</div>
				</s:if>
			</li>
			 <li>
                <label>Address line 2</label><br>
                <s:textfield name="appForm.appAbroadAddress2"  id="appAbroadAddress2" value="%{appForm.appAbroadAddress2}"   maxlength="%{isDsrPage=='true'?100:'100'}" cssClass="form-control" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
            </li>
            <li>
				<label>Landmark</label>
				<s:textfield name="appForm.appAbroadAddressLandMark" id="appAbroadAddressLandMark" value="%{appForm.appAbroadAddressLandMark}" maxlength="40" cssClass="form-control" onkeydown="return M.isAddressLine(event);" onpaste="return false;" />
			</li> 
			<li >
				<label>Country<b class="req">*</b></label>
				<div class="flat-field">
					 <s:textfield name="appForm.appAbroadCountryId" id="appAbroadCountryId" value="%{appForm.appAbroadCountryId}" cssClass="disabledFields form-control" autocomplete="off" disabled="true" onfocus="customOnFocus(this);" onpaste="return false;"/>
				</div>
			</li>
			<li >
				<label >State<b class="req">*</b></label>
				<s:textfield name="appForm.appAbroadState"  id="appAbroadState" value="%{appForm.appAbroadState}" maxlength="40" cssClass="form-control" onkeydown="return M.isAddressLine(event);" onpaste="return false;" />
			</li>
			<li >
				<label >City<b class="req">*</b></label>
				<s:textfield name="appForm.appAbroadCity"  id="appAbroadCity" value="%{appForm.appAbroadCity}" maxlength="40" cssClass="form-control" onkeydown="return M.isAddressLine(event);" onpaste="return false;" />
			</li>
			<li >
				<label >Zipcode/Pincode<b class="req">*</b></label>
				<s:textfield name="appForm.appAbroadPincode"  id="appAbroadPincode" value="%{appForm.appAbroadPincode}" maxlength="6" placeholder="Pincode" cssClass="form-control"  onkeydown="return M.digit(event);" onpaste="return false;"/>
			</li> 
</s:if>
