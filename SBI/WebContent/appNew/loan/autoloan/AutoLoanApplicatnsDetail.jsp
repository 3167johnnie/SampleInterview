<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="350">
	   <label>Date of Birth<b class="req">*</b></label>
	   <input type="text" name="quote.loanQuoteDateOfBirth" id="date_of_birth"  placeholder="dd-mm-yyyy" 
	   class="form-control dob-cal" maxlength="10" value="<s:property value="%{quote.loanQuoteDateOfBirth}"/>" autocomplete="off"/>
</li>
<li id="720">
	   <label> Gender<b class="req">*</b></label>
	   <div class="flat-field">
         <s:select name="quote.loanQuoteGender" id="gender" value="%{quote.loanQuoteGender}" list="%{#genderList}"  
         headerKey="0" headerValue="Select gender" cssClass="form-select" onfocus="customOnFocus(this);"/>
       </div>
</li>
<li id="352">
	   <label>Resident type<b class="req">*</b></label>
	   <div class="flat-field">
	     <s:select list="%{beanList.residentTypes==null?'':beanList.residentTypes}" id="residentType" name="quote.loanQuoteResidentTypeId"
	     value="%{quote.loanQuoteResidentTypeId}" headerKey="0" headerValue="Select resident type" cssClass="form-select" onfocus="customOnFocus(this);"/>
	   </div>
</li>

