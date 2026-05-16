<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_profitAfterTax1">
	<label class="">PAT as per latest ITR(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstProfitAfterTax" id="co_profitAfterTax1" value="%{quote.loanQuoteCoapplicantFirstProfitAfterTax>0?quote.loanQuoteCoapplicantFirstProfitAfterTax:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_depreciation1">
	<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
	</s:if>
	<s:elseif test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBBJ}">
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
		<%-- <label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)/Depreciation last year (<span class="font-rupee">`</span>)</label> --%>
	</s:elseif>
	<s:else>
		<label class="">Depreciation last 3 years average (<span class="font-rupee">`</span>)</label>
	</s:else>
	<s:textfield name="quote.loanQuoteCoapplicantFirstDepreciatiation" id="co_depreciation1" value="%{quote.loanQuoteCoapplicantFirstDepreciatiation>0?quote.loanQuoteCoapplicantFirstDepreciatiation:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<li id="divco_preEMIs1">
	<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" maxlength="9" placeholder="Rs" cssClass="form-control" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO1">
<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident1CO2.jsp"></s:include>
</s:if>
</div>
