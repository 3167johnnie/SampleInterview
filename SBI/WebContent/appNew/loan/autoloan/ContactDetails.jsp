<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if test="%{isDsrPage.equalsIgnoreCase('true')}">
	<li id="426">
		<label>First name<b class="req">*</b></label>
		<s:textfield name="quote.loanQuoteFirstName" placeholder="First name" id="loanQuoteFirstName" cssClass="form-control" value="%{(firstName!=null ? firstName : appForm.appFirstName)}" maxlength="16"/>
	</li>
</s:if>
<input type="hidden" class="form-control" id="isdCodeType" name="isdCodeType" value="<s:property value="%{appForm.appISDCode}" />" />
<input type="hidden" class="form-control" id="appResTypeAtVerified" name="appResTypeAtVerified" value="<s:property value="%{appForm.appResTypeAtVerified}" />" />
<input type="hidden" class="form-control" id="appOTPVerified" name="appOTPVerified" value="<s:property value="%{(appForm==null || (appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />
<input type="hidden" class="form-control" id="appOTPVerifiedByEmail" name="appOTPVerifiedByEmail" value="<s:property value="%{(appForm==null || (appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />
<input type="hidden" class="form-control" id="appOTPVerifiedByMobile" name="appOTPVerifiedByMobile" value="<s:property value="%{(appForm==null || (appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />

<div id="RESIDANCETYPE">
	<s:if test="%{(appForm!=null && appForm.appResTypeAtVerified!=null && appForm.appResTypeAtVerified==2)}">
		<s:include value="/appNew/loan/autoloan/includes/ApplicantResidanceType.jsp"></s:include>
	</s:if>
</div>

<div id="RESIDANCETYPEVALUE">
	<s:if test="%{appForm.appApplyingFrom!=null && appForm.appApplyingFrom==2}">
		<s:include value="/appNew/loan/autoloan/includes/NRIMobileNo.jsp"></s:include>
	</s:if>
	<s:else>
		<s:include value="/appNew/loan/autoloan/includes/IndianMobileNo.jsp"></s:include>
	</s:else>
</div>
<li id="423">
	<label>E-mail<s:if test="%{!isDsrPage.equalsIgnoreCase('true')}"><b class="req">*</b></s:if></label>
	<s:if test="%{appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('Y')}">
		<s:textfield name="quote.appEmail" id="emailid" value="%{appForm.appWorkEmail}" autocomplete="off" maxlength="40" placeholder="Email Id" cssClass="form-control" readonly="true"/>
	</s:if>
	<s:else>
		<s:textfield name="quote.appEmail" id="emailid" value="%{email}" autocomplete="off" maxlength="40" placeholder="Email Id" cssClass="form-control" />
	</s:else>
</li>
