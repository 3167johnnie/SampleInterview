<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label>Preferred location for availing loan<b class="req">*</b></label>
	<div class="flat-field">
		<s:select list="%{beanList.preferredLocation==null?'':beanList.preferredLocation}" id="preferredLocation" name="quote.loanQuotePreferredLocationOfAvailingLoan" 
			cssClass="form-select" value="%{quote.loanQuotePreferredLocationOfAvailingLoan}" 
			headerKey="0" headerValue="Select preferred location" onfocus="customOnFocus(this);"/>
	</div>
</li>
