<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
	
<li id="divcheckOffType">
	<label>Check-off type
	<s:if test="%{isDsrPage=='false'}"><b class="req">*</b><a href='javascript:void(0)' class='read-more' >read more</a></s:if></label>
	<div class="flat-field">
		<s:select list="#{1:'Full check-off', 3:'No check-off'}" id="checkOffType" name="quote.loanQuoteCheckOffType" value="%{quote.loanQuoteCheckOffType}" 
		cssClass="form-select" headerKey="0" headerValue="Select check-off" onfocus="customOnFocus(this);"/>
	</div>
</li>
