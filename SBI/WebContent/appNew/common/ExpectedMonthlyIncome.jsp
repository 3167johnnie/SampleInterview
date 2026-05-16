<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">Expected monthly rental income (<span class="font-rupee">`</span>) <b class="req"></b></label>
	<s:textfield name="quote.loanQuoteExpMonthlyRentalIncome" id="expMonthlyRentalIncome" value="%{quote.loanQuoteExpMonthlyRentalIncome>0?quote.loanQuoteExpMonthlyRentalIncome:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onfocus="this.value=M.unformatMoney(this.value);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" />
	<div class="custom-tooltip">This excludes maintainance charges, Other situatory charges.</div>
</li>
