<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{quote.loanQuoteLoanPurposeId==2}">
	<li>
		<label>Cost of renovation<b class="req">*</b></label>
		<s:textfield name="quote.loanQuoteRenovationCost" id="renovationCost" 
		value="%{quote.loanQuoteRenovationCost>0?quote.loanQuoteRenovationCost:''}" 
		autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" 
		onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" 
		onfocus="this.value=M.unformatMoney(this.value);" cssClass="form-control"/>
	</li>
</s:if>
<s:else>
	<li>
		<label>Cost of renovation<b class="req">*</b></label>
		<s:textfield name="quote.loanQuoteRenovationCost" id="renovationCost" 
		value="%{quote.loanQuoteRenovationCost>0?quote.loanQuoteRenovationCost:''}" 
		autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" 
		onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" 
		onfocus="this.value=M.unformatMoney(this.value);" cssClass="form-control"/>
	</li>
</s:else>
<s:include value="/appNew/loan/homeloan/HomeLoanReimbursement.jsp"></s:include>
