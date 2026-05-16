<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<h3>WORKPLACE DETAILS</h3>
<ul class="form-section">
<div id="WORKSTATES">
	<s:if test="%{quote!=null && quote.loanQuoteLoanPurposeId==11 && cbs.cbsTypeOfRelationship==1}">
		<s:include value="/appNew/loan/personal/includes/WorkStatesXpressCredit.jsp"></s:include>
	</s:if>
	<s:else>
		<s:include value="/appNew/common/WorkStates.jsp"></s:include>
	</s:else>
</div>

<li id="LOCATIONCITY" class="hide">
	<label class="">City<b class="req">*</b></label>
	<div class="flat-field">
		<s:select list="%{beanList.citiesoptgrp1WorkPlace==null?'':beanList.citiesoptgrp1WorkPlace}" name="quote.loanQuoteWorkCityId" id="workCityId" value="%{quote.loanQuoteWorkCityId}" 
			headerKey="0" headerValue="Select city" cssClass="form-select" disabled="%{quote.loanQuoteWorkCityId==null?'true':'false'}" onfocus="customOnFocus(this);">
		</s:select>
	</div>
</li>

<div id="LOCATIONDISTRICT">
	<s:if test="%{quote.loanQuoteWorkDistrictId!=null &&  quote.loanQuoteWorkDistrictId >0}">
		<s:include value="/appNew/loan/personal/WorkDistrict.jsp"></s:include>
	</s:if>
</div>


<div id="LOCATIONBRANCH">
	<s:if test="%{quote.loanQuoteWorkBranchId!=null && quote.loanQuoteWorkBranchId >0}">
		CVE : <s:include value="/appNew/loan/personal/WorkBranchCve.jsp"></s:include>
	</s:if>
</div>

	<!-- <s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
	<div id="LOCATIONBRANCH">
		<s:if test="%{quote.loanQuoteWorkBranchId!=null && quote.loanQuoteWorkBranchId >0}">
			<s:include value="/appNew/loan/personal/WorkBranchCve.jsp"></s:include>
		</s:if>
	</div>
	</s:if>
	<s:else>
	<div id="LOCATIONBRANCH">
		<s:if test="%{quote.loanQuoteWorkBranchId!=null && quote.loanQuoteWorkBranchId >0}">
			<s:include value="/appNew/loan/personal/WorkBranch.jsp"></s:include>
		</s:if>
	</div>
	</s:else> -->

<!-- <div id="LOCATIONBRANCH">
	<s:if test="%{quote.loanQuoteWorkBranchId!=null && quote.loanQuoteWorkBranchId >0}">
		COMMON : <s:include value="/appNew/loan/personal/WorkBranch.jsp"></s:include>
	</s:if>
</div> -->
</ul>
