<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">State<b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="quote.loanQuoteWorkStateId" id="workStateId" autocomplete="off" list="%{#states}" cssClass="form-select"
	 	value="%{quote.loanQuoteWorkStateId}" headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
	</div>
</li>
