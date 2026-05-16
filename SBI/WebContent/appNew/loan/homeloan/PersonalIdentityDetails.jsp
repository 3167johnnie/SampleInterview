<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<h3  >PERSONAL AND IDENTITY DETAILS</h3>
<ul class="form-section" style="float: right; ">
	<li>
	    
		<label>First name<b class="req">&nbsp;*</b></label>
		
		<s:textfield name="quote.loanQuoteAppFirstName"  id="firstName" value="%{quote.loanQuoteAppFirstName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);" onkeydown="return M.isChars(event);"  onfocus="customOnFocus(this);" />
	   
	</li>		
	<li>
	
		<label>Middle name</label>
		<s:textfield name="quote.loanQuoteMiddleName"  id="middleName" value="%{quote.loanQuoteMiddleName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);" onkeydown="return M.isChars(event);" />

	</li>
	<li>
		<label>Last name<b class="req">&nbsp;*</b></label>
		<s:textfield name="quote.loanQuoteLastName"  id="lastName" value="%{quote.loanQuoteLastName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);" onkeydown="return M.isChars(event);" />
	</li>
	<%-- <li>
		<label>Gender<b class="req">&nbsp;*</b></label>
        <div class="flat-field">
        	<s:select list="%{#genderList}" name="quote.loanQuoteGender" id="gender1" value="%{quote.loanQuoteGender}" cssClass="form-control" headerKey="0" headerValue="Select gender" onfocus="customOnFocus(this);"/>
		</div>
	</li> --%>
	<li>
		 <label>Current Address line 1<b class="req">&nbsp;*</b></label>
		 <s:textfield name="quote.loanQuoteAddress1"  id="address1" value="%{quote.loanQuoteAddress1}" maxlength="40"
	        cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
	</li>
	<li>
		 <label>Current Address line 2<b class="req">&nbsp;*</b></label>
		 <s:textfield name="quote.loanQuoteAddress2"  id="address2" value="%{quote.loanQuoteAddress2}" maxlength="40"
	           cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
	</li>
	<li>
		 <label>Landmark</label>
		 <s:textfield name="quote.loanQuoteAddressLandmark"  id="addressLandmark" value="%{quote.loanQuoteAddressLandmark}" maxlength="40"
	           cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
	</li>
	<div id="QUOTELOCATIONSTATE">
		<li>
           <label>State<b class="req">&nbsp;*</b></label>
			<div class="flat-field">
				<s:if test="%{appForm.appCityId!=null && appForm.appCityId>0}">
					<s:select name="quote.loanQuoteStateId" id="stateId" cssClass="form-select"
						autocomplete="off" list="%{#states}" value="%{appForm.appStateId}" headerKey="0"
						headerValue="Select state" onfocus="customOnFocus(this);"/>
				</s:if>
				<s:else>
						<s:select name="quote.loanQuoteStateId" id="stateId"
							autocomplete="off" list="%{#states}" cssClass="form-select" value=""
							headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
				</s:else>
			</div>
		</li>
    </div>
   	<div id="QUOTELOCATIONCITY">
		<li>
			<label>City<b class="req">&nbsp;*</b></label>
			<div class="flat-field">
				<s:if test="%{appForm.appCityId!=null && appForm.appCityId>0}">
					<s:select list="%{beanList.citiesoptgrp1==null?'':beanList.citiesoptgrp1}" name="quote.loanQuoteCityId" id="cityId" value="%{appForm.appCityId}" 
					cssClass="form-select" headerKey="0" headerValue="Select city" onfocus="customOnFocus(this);">
					</s:select>
				</s:if>
				<s:else>
						<s:select list="%{''}" name="quote.loanQuoteCityId" id="cityId" value="%{appForm.appCityId}" 
		                		headerKey="0" headerValue="Select city"  disabled="true" cssClass="disabledFields form-select" onfocus="customOnFocus(this);">
	                	</s:select>
				</s:else>
			</div>
		</li>
	</div>
	<div id="QUOTELOCATIONDISTRICT">
		<s:if test="%{appForm.appCityId!=null && appForm.appCityId == 9999999}">
				<s:if test="%{appForm.appDistrictId!=null}">
					<s:include value="/appNew/loan/homeloan/Districts.jsp"></s:include>
				</s:if>
		</s:if>
	</div>
	<li>
		<label>Pincode<b class="req">&nbsp;*</b></label>
		<s:textfield  value="%{quote.loanQuotePincode}" id="pincode" cssClass="disabledFields form-control" name="quote.loanQuotePincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"/>
	</li>
	
	
	
	<%--  <s:include value="/appNew/homeloan/IdentityProofsFirst.jsp"></s:include> --%>
	  <s:include value="/appNew/common/IdentityProofsFirst.jsp"></s:include> 
	 <s:include value="/appNew/loan/personal/RequestedLoanAmount.jsp"></s:include> 
	<li>
	<%-- <label>PAN<b class="req">&nbsp;*</b><font style="font-size:12px;"> (<a href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN" target="_blank">Verify Your PAN</a>)</font></label>
	<s:textfield placeholder="Enter PAN" name="quote.loanQuotePanCardNo"  id="appPanCardNo1" value="%{quote.loanQuotePanCardNo}" maxlength="10"
	cssClass="form-control" disabled="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'true':'false'}" />
	<s:if test="%{isDsrPage=='false'}">
		<div class="custom-tooltip">Enter valid PAN</div>
	</s:if> --%>
		<%-- <input type="checkbox" name="quote.loanQuotePanCardLater" id="appPanCardLater1" 
		value="<s:property value="%{(quote!=null && quote.loanQuotePanCardLater)?'true':'false'}"/>"  --%>
		
	</li> 
</ul>
