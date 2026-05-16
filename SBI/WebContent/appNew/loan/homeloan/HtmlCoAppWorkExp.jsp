<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_workExperience1">
	<label class="">Work experience<b class="req">*</b></label>
	<div class="flat-field">
	<s:select name="quote.loanQuoteCoapplicantFirstWorkExperience" id="co_workExperience1" autocomplete="off" list="#{1:'Less than 6 months',2:'6 months or more with a job contract of min 2 years abroad',3:'2 years or more',4:'None of the above'}"
 		value="%{quote.loanQuoteCoapplicantFirstWorkExperience}" cssClass="form-select" headerKey="0" headerValue="Select work experience" onfocus="customOnFocus(this);"/>
	</div>
</li>
