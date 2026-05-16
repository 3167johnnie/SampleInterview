<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label>Mobile no.<b class="req">*</b></label>
	<%-- <s:if test="%{isDsrPage.equalsIgnoreCase('true')}"> --%>
		<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
			<s:textfield name="quote.appMobile" id="mobile"    value="%{appForm.appMobileNo}" autocomplete="off" maxlength="10" placeholder="Mobile" onkeydown="return M.digit(event);" cssClass=" form-control"  readonly="true" />
		</s:if>
		<s:else>
			<s:textfield name="quote.appMobile" id="mobile" value="%{mobileNo}" autocomplete="off" maxlength="10" placeholder="Mobile" cssClass="form-control" onkeydown="return M.digit(event);" />
		</s:else>
<%-- 	</s:if> --%>
	<s:else>
		<%-- <s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
			<s:textfield name="quote.appMobile" id="disabledMobile" value="%{appForm.appMobileNo}" autocomplete="off" maxlength="10" placeholder="Mobile" cssClass="disabledFields form-control" onkeydown="return M.digit(event);" readonly="true" />
			<s:textfield name="quote.appMobile" id="mobile" value="%{appForm.appMobileNo}" autocomplete="off" maxlength="10" placeholder="Mobile" cssClass=" form-control" onkeydown="return M.digit(event);" readonly="true" />
		</s:if>
		<s:else> --%>
<%-- 			<s:textfield name="quote.appMobile" id="mobile" value="%{appForm.appMobileNo}" autocomplete="off" maxlength="10" placeholder="Mobile" cssClass="form-control" onkeydown="return M.digit(event);"/>
 --%>	<%-- 	</s:else> --%>
	</s:else>
</li>
