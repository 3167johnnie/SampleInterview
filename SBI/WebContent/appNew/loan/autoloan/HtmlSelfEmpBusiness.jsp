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
<s:include value="/appNew/loan/autoloan/includes/AutoOccupationType.jsp"></s:include>
<li id="divprofitAfterTax">
	<label class="">PAT as per latest ITR(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteProfitAfterTax" id="profitAfterTax" value="%{quote.loanQuoteProfitAfterTax>0?quote.loanQuoteProfitAfterTax:''}" 
	cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI 
		|| @com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBP
		|| @com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBH}">
<li id="divdepreciation">
		<label class="">Latest year's depreciation (<span class="font-rupee">`</span>)</label>
		<s:textfield name="quote.loanQuoteDepreciation" id="depreciation" cssClass="form-control" value="%{quote.loanQuoteDepreciation>0?quote.loanQuoteDepreciation:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divexpectedRentalIncome">
		<label class="">Monthly rental income (<span class="font-rupee">`</span>)</label>
		<s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" cssClass="form-control" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
</s:if>
<s:if test="%{#needToDisplay==false}">
	<li id="divpreEMIsOther">
		<label class="">Existing EMIs, if any other selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther"  cssClass="form-control" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:if>
<s:else>
	<li id="divpreEMIs">
	   <label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	   <s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" cssClass="form-control" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:else>


			
