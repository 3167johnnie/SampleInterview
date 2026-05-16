<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li id="divcountryRegion">
	<label class="">Country/Region<b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="quote.loanQuoteCountryId" id="countryRegion" autocomplete="off" list="%{#countries}" 
		 value="%{quote.loanQuoteCountryId}" headerKey="0" headerValue="Select country" cssClass="form-select" onfocus="customOnFocus(this);"/>
	</div> 
</li>
