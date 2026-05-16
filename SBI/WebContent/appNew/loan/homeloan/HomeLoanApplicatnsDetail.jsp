<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="appDobLiId">
	<label>Date of birth<b class="req">*</b></label>
	<input type="text" name="quote.loanQuoteDateOfBirth" id="date_of_birth"  placeholder="dd-mm-yyyy" class="form-control dob-cal"  maxlength="10" value="<s:property value="%{quote.loanQuoteDateOfBirth}"/>" autocomplete="off"/>
</li>
		
<li id="DIVGENDER">
	<label>Gender<b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="quote.loanQuoteGender" id="gender" value="%{quote.loanQuoteGender}" 
		list="%{#genderList}" headerKey="0" headerValue="Select gender" cssClass="form-select" onfocus="customOnFocus(this);"/>
	</div>
</li>

<li id="44">
	<label>Resident type<b class="req">*</b></label>
	<div class="flat-field">
		<s:select list="%{beanList.residentTypes==null?'':beanList.residentTypes}" id="residentType" 
		name="quote.loanQuoteResidentTypeId" onchange="showPerspectiveRows4ResidentType(this)" 
		value="%{quote.loanQuoteResidentTypeId}" headerKey="0" headerValue="Select resident type" cssClass="form-select" onfocus="customOnFocus(this);"/>
	</div>		
</li>
<div id="PuccaHouse">
 <s:if test="%{quote.loanQuoteLoanPurposeId==1  && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4) && (quote.loanQuoteResidentTypeId!=null && quote.loanQuoteResidentTypeId==1)}">
		<s:if test="%{quote.loanQuoteResidentTypeId!=null && quote.loanQuoteResidentTypeId==1}">
				<s:include value="/appNew/loan/homeloan/HavePuccaHouse.jsp"></s:include>
		</s:if>
 </s:if> 
</div>
<div id="RESIDENTCOUNTRY">
<s:if test="%{quote.loanQuoteResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/ResidentCountryRegion.jsp"></s:include>
</s:if>
</div>

<div id="INSTAMARITALSTATUS">
	<s:if test="%{quote.loanQuoteLoanPurposeId==4}"> 
	<!-- Used only for Insta PAL -->
		<s:include value="/appNew/loan/homeloan/includes/MaritalStatus.jsp"></s:include>
	</s:if>
</div>
