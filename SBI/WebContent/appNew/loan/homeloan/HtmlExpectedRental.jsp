<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li id="divexpectedRentalIncome">
	<label class="">Expected monthly rental Income (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
