<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="isMandatory" value="%{''}"/>
<s:if test="%{quote.loanQuoteLoanPurposeId==5 || (quote.loanQuoteLoanPurposeId==3 && quote.loanQuoteLoanCategoryId==7)}">
	<s:set var="isMandatory" value="%{'*'}"/>
</s:if>

<li id="EMPLOYEMENTTYPES">
	<label>Type of employment<b class="req">*</b></label>
	 <div class="flat-field">
	     <s:select list="%{beanList.employementTypes==null?'':beanList.employementTypes}" id="employmentType" name="quote.loanQuoteEmploymentTypeId" onchange="showPerspectiveRows4EmpTypeAutoLoan(this)"
	     value="%{quote.loanQuoteEmploymentTypeId}" headerKey="0" headerValue="Select employment type" cssClass="form-select" onfocus="customOnFocus(this);"/>
	 </div>
</li>

<div id="EMPTYPE">
		
		<s:if test="%{quote.loanQuoteEmploymentTypeId==9}">
			<s:include value="/appNew/loan/autoloan/HtmlSalariedEmpTypes.jsp"></s:include>
		</s:if>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==10 || quote.loanQuoteEmploymentTypeId==11}">
			<s:include value="/appNew/loan/autoloan/HtmlSelfEmpBusiness.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==12}">
			<s:include value="/appNew/loan/autoloan/HtmlAgriculturist.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==13}">
			<s:include value="/appNew/loan/autoloan/HtmlRetiredPensioner.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==6}">
			<s:include value="/appNew/loan/autoloan/includes/AutoOccupationType.jsp"></s:include>
			<li id="divincomeFromRegularSource">
				<label class="">Income From Regular Source (<span class="font-rupee">`</span>)<b class="req">*</b></label>
				<s:textfield name="quote.loanQuoteIncomeFromRegularSource" id="incomeFromRegularSource" value="%{quote.loanQuoteIncomeFromRegularSource>0?quote.loanQuoteIncomeFromRegularSource:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
			</li>
			<li id="divpreEMIs">
				<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
				 <s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
			</li>
		</s:elseif>
</div>	

