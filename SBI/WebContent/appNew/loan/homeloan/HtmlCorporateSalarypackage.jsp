<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{quote.loanQuoteHaveSalaryAccountWithSbi!=null && quote.loanQuoteHaveSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
	<li id="divsalaryPackage">
		<label>Corporate salary package<b class="req">*</b></label>
		<div class="flat-field">
			<s:select id="salaryPackage" list="%{beanList.salaryPackageData==null?'':beanList.salaryPackageData}" name="quote.loanQuoteSalaryPackage" value="%{quote.loanQuoteSalaryPackage}"
			cssClass="form-select" headerKey="0" headerValue="Select salary package" onfocus="customOnFocus(this);"/>
		</div>
	</li>
</s:if>
<s:else>
	<li>
		<label class="">Corporate salary package<b class="req">*</b></label>
		<div class="flat-field">
			<s:select id="salaryPackage" list="%{beanList.salaryPackageData==null?'':beanList.salaryPackageData}" name="quote.loanQuoteSalaryPackage" value="%{quote.loanQuoteSalaryPackage}"
			cssClass="form-select" headerKey="0" headerValue="Select salary package" onfocus="customOnFocus(this);"/>
		</div>
	</li>
</s:else>
