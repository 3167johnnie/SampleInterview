<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_countryRegion1">
	<label class="">Country/Region<b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="quote.loanQuoteCoapplicantFirstCountryId" id="co_countryRegion1" autocomplete="off"  list="%{#countries}"
	 value="%{quote.loanQuoteCoapplicantFirstCountryId}" cssClass="form-select" headerKey="0" headerValue="Select country" onfocus="customOnFocus(this);"/>
	</div>
</li>
