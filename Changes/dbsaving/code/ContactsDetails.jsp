<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{isDsrPage.equalsIgnoreCase('true')}">
	<div class="div-left " id="727">
		<label>First name<b class="req">*</b></label>
		<s:textfield name="quote.loanQuoteFirstName" placeholder="First name" id="loanQuoteFirstName" value="%{(firstName!=null ? firstName : appForm.appFirstName)}" maxlength="16"/>
	</div>
</s:if>
<input type="hidden" class="form-control" id="isdCodeType" name="isdCodeType" value="<s:property value="%{appForm.appISDCode}" />" />
<input type="hidden" class="form-control" id="appResTypeAtVerified" name="appResTypeAtVerified" value="<s:property value="%{appForm.appResTypeAtVerified}" />" />
<input type="hidden" id="appOTPVerified" name="appOTPVerified" value="<s:property value="%{(appForm==null || (appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />
<input type="hidden" id="appOTPVerifiedByEmail" name="appOTPVerifiedByEmail" value="<s:property value="%{(appForm==null || (appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />
<input type="hidden" id="appOTPVerifiedByMobile" name="appOTPVerifiedByMobile" value="<s:property value="%{(appForm==null || (appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N')))?'N':'Y'}" />" />
<div id="ContactDetailsNRI">
	<s:if test="%{appForm.appApplyingFrom!=null && appForm.appResTypeAtVerified!=null && appForm.appResTypeAtVerified==2}">
		<s:include value="/appNew/loan/personal/ContactsDetailsNRI.jsp"></s:include>
	</s:if>
</div>
<div id="RESIDENTTYPEVALUE">
	<%-- <s:if test="%{appForm.appApplyingFrom!=null && appForm.appApplyingFrom==2}">
		<s:include value="/appNew/loan/personal/NRIMobileNo.jsp"></s:include>
	</s:if>
	<s:else>  --%>
	
	    
		<s:include value="/appNew/loan/homeloan/MobileNo.jsp"></s:include>
<%-- 	</s:else> --%>
</div>
<li>
	<label>E-mail
	<s:if test="%{!isDsrPage.equalsIgnoreCase('true')}"><b class="req">*</b> </s:if> 
	</label>
	<s:if test="%{appForm.appEmailVerified!=null && appForm.appEmailVerified.equalsIgnoreCase('Y')} ">
		<s:textfield name="quote.appEmail" id="emailid" value="%{appForm.appWorkEmail}" autocomplete="off" maxlength="40" placeholder="Email Id" cssClass=" form-control" readonly="true"/>
	</s:if>
	<s:else>
		<s:if test="%{appForm.appWorkEmail!=null}">
			<s:textfield name="quote.appEmail" id="emailid" value="%{email}" autocomplete="off" maxlength="40" placeholder="Email Id" cssClass="form-control"  readonly="true"/>
		</s:if>
		<s:else>
			<s:textfield name="quote.appEmail" id="emailid" value="%{email}" autocomplete="off" maxlength="40" placeholder="Email Id" cssClass="form-control" />
		</s:else>
		
		
	</s:else>
</li>
