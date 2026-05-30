<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li id="divnetAnnualIncome">
	<label class="">Net annual income (<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteNetAnnualIncome" id="netAnnualIncome" cssClass="form-control" value="%{quote.loanQuoteNetAnnualIncome>0?quote.loanQuoteNetAnnualIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<li id="divexpectedRentalIncome">
	 <label class="">Monthly rental income (<span class="font-rupee">`</span>)</label>
	 <s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" cssClass="form-control" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divotherIncome">
	<label class="">Other monthly income (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteOtherIncome" id="otherIncome" cssClass="form-control" value="%{quote.loanQuoteOtherIncome>0?quote.loanQuoteOtherIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
</li>
<s:if test="%{#needToDisplay==false}">
	<li id="divpreEMIsOther">
		<label class="">Existing EMIs, if any other selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" cssClass="form-control" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:if>
<s:else>
	<li id="divpreEMIs">
	   <label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	   <s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" cssClass="form-control" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:else>
