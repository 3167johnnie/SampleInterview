<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label> Resident type<b class="req">*</b></label>
	<div class="flat-field">
		<%-- <s:select list="#{1:'Resident Indian'}" value="%{quote.loanQuoteResidentTypeId}"  onchange="showPerspectiveRows4ResidentType(this)" cssClass="form-control" id="residentType" name="quote.loanQuoteResidentTypeId" headerKey="0" headerValue="Select resident type" onfocus="customOnFocus(this);"/> --%> 
		<s:select list="#{1:'Resident Indian'}" value="%{quote.loanQuoteResidentTypeId}" onchange="showPerspectiveRows4ResidentType(this)" cssClass="form-select" id="residentType" name="quote.loanQuoteResidentTypeId" onfocus="customOnFocus(this);"/>
	</div>
</li>
