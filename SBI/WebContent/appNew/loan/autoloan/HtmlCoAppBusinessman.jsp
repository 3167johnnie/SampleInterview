<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_profitAfterTax1">
	<label class="">PAT as per latest ITR(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstProfitAfterTax" id="co_profitAfterTax1" value="%{quote.loanQuoteCoapplicantFirstProfitAfterTax>0?quote.loanQuoteCoapplicantFirstProfitAfterTax:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_depreciation1">
	<label class="">Latest year's depreciation(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstDepreciatiation" id="co_depreciation1" value="%{quote.loanQuoteCoapplicantFirstDepreciatiation>0?quote.loanQuoteCoapplicantFirstDepreciatiation:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<li id="divco_preEMIs1">
	<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>) </label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
