<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li id="divloanQuoteSalaryPackage">
	<label class="">Type of salary package<b class="req">*</b></label>
	<div class="flat-field">
		<s:select id="loanQuoteSalaryPackage" list="%{beanList.salaryPackageData==null?'':beanList.salaryPackageData}" name="quote.loanQuoteSalaryPackage" value="%{quote.loanQuoteSalaryPackage}"
		headerKey="0" headerValue="Select salary package" cssClass="form-select" onfocus="customOnFocus(this);"/>
	</div>
</li>
