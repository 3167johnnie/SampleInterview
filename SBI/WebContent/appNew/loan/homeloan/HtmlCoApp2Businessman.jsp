<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_profitAfterTax2">
	<label class="">PAT as per latest ITR(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondProfitAfterTax" id="co_profitAfterTax2" value="%{quote.loanQuoteCoapplicantSecondProfitAfterTax>0?quote.loanQuoteCoapplicantSecondProfitAfterTax:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_depreciation2">
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
	<s:textfield name="quote.loanQuoteCoapplicantSecondDepreciatiation" id="co_depreciation2" value="%{quote.loanQuoteCoapplicantSecondDepreciatiation>0?quote.loanQuoteCoapplicantSecondDepreciatiation:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<li id="divco_preEMIs2">
	<label>Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" cssClass="form-control"  maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO2">
<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident2CO2.jsp"></s:include>
</s:if>
</div>
