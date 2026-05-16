<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="needToDisplay" value="%{true}"/>
<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
	<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
			<s:if test="%{isDsrPage=='false'}">
				<s:set var="needToDisplay" value="%{false}"/>
			</s:if>
	</s:if>
</s:if>
<li id="divprofitAfterTax">
	<label class="">PAT as per latest ITR(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteProfitAfterTax" id="profitAfterTax" value="%{quote.loanQuoteProfitAfterTax>0?quote.loanQuoteProfitAfterTax:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divdepreciation">
	<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
	</s:if>
	<s:elseif test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBBJ}">
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
	</s:elseif>
	<s:else>
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
	</s:else>
	<s:textfield name="quote.loanQuoteDepreciation" id="depreciation" value="%{quote.loanQuoteDepreciation>0?quote.loanQuoteDepreciation:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<div id="expectedRentalBusiness">
	<s:if test="%{!(quote.loanQuoteLoanPurposeId == 1 && quote.loanQuoteLoanCategoryId ==3)}">
		<s:include value="/appNew/loan/homeloan/HtmlExpectedRental.jsp"></s:include>
	</s:if>
</div>

<s:if test="%{#needToDisplay==false}">
<li id="divpreEMIsOther">
	<label class="">Existing EMIs, if any other selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
	<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
</s:if>
<s:else>
<li id="divpreEMIs">
	<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)<b class="req"><s:property escapeHtml="false" value="%{#isMandatory}"/></b></label>
	<s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
</s:else>
<%-- <s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4)}">
	<ul>
		<s:include value="/appNew/loan/homeloan/HomePmayItr.jsp"></s:include> 
	</ul>
</s:if> --%>
<div id="RESIDENTTYPEB">
	<s:if test="%{quote.loanQuoteResidentTypeId==2}">
		<s:include value="/appNew/loan/homeloan/includes/WorkExResident2.jsp"></s:include>
	</s:if>
</div>
			
