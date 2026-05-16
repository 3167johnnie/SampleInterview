<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="isMandatory" value="%{''}"/>
	<s:if test="%{quote.loanQuoteLoanPurposeId==5 || (quote.loanQuoteLoanPurposeId==3 && quote.loanQuoteLoanCategoryId==7)}">
		<s:set var="isMandatory" value="%{'*'}"/>
	</s:if>
	<s:set var="needToDisplay" value="true"/>
		<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
			<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
					<s:if test="%{isDsrPage=='false'}">
						<s:set var="needToDisplay" value="false"/>
					</s:if>
			</s:if>
		</s:if>
	<li id="EMPLOYEMENTTYPES">
		<label>Type of employment<b class="req">*</b></label>
		<%-- <s:if test="%{homeLoanCriteria.gender.equalsIgnoreCase('M')}">
			<div class="flat-field">
				<s:select list="%{beanList.employementTypes==null?'':beanList.employementTypes}" id="employmentType" name="quote.loanQuoteEmploymentTypeId" onchange="showPerspectiveRows4EmpTypeHomeLoan(this)"
					value="%{quote.loanQuoteEmploymentTypeId}" cssClass="form-control" headerKey="0" headerValue="Select employment type" onfocus="customOnFocus(this);"/>
			</div>
		</s:if>
		<s:else>
		</s:else> --%>
		<div class="flat-field">
			<s:select list="%{beanList.employementTypes!=null?beanList.employementTypes:''}" id="employmentType" name="quote.loanQuoteEmploymentTypeId" onchange="showPerspectiveRows4EmpTypeHomeLoan(this)"
				value="%{quote.loanQuoteEmploymentTypeId}" cssClass="form-select" headerKey="0" headerValue="Select employment type" onfocus="customOnFocus(this);"
				 disabled="quote.loanQuoteEmploymentTypeId==null?'true':'false'"/>
		</div>
	</li>
		
<div id="SalariedEmp">
		<s:if test="%{quote.loanQuoteEmploymentTypeId==1}">
			<s:include value="/appNew/loan/homeloan/HtmlSalariedEmpTypes.jsp"></s:include>
		</s:if>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==2 || quote.loanQuoteEmploymentTypeId==3}">
			<s:include value="/appNew/loan/homeloan/HtmlSelfEmpBusiness.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==4}">
			<s:include value="/appNew/loan/homeloan/HtmlAgriculturist.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==5}">
			<s:include value="/appNew/loan/homeloan/HtmlRetiredPensinor.jsp"></s:include>
		</s:elseif>
		<s:elseif test="%{quote.loanQuoteEmploymentTypeId==6 || quote.loanQuoteEmploymentTypeId==7}">
			<s:include value="/appNew/loan/homeloan/HtmlRetiredEmp.jsp"></s:include>
		</s:elseif>
		 <s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4) && (quote.loanQuoteResidentTypeId!=2)}">
			<ul id="divPmayITR1">
				 <s:include value="/appNew/loan/homeloan/HomePmayItr.jsp"></s:include>
			</ul>
	   </s:if> 
</div>
