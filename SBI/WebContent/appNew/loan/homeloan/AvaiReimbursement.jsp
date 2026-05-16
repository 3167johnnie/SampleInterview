<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
  <%-- <s:if test="%{quote.loanQuoteReimburse.equalsIgnoreCase('Y')}">???1<s:property value="%{quote.loanQuoteReimburse}"/>  --%>
		<li id="divactualInvestmentMade1">
			<label>Market value of property<b class="req">*</b></label>
			<s:textfield name="quote.loanQuoteAmountInvested" id="actualInvestmentMade" value="%{quote.loanQuoteAmountInvested>0?quote.loanQuoteAmountInvested:''}" autocomplete="off" maxlength="9"  placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" cssClass="form-control" onfocus="this.value=M.unformatMoney(this.value);"/>
		</li>
		<li id="divmarketValueOfProperty">
			<label>Amount Invested during the last 12 Months<b class="req">*</b></label>
			<s:textfield name="quote.loanQuotePropertyMarketValue" id="marketValueOfProperty" value="%{quote.loanQuotePropertyMarketValue>0?quote.loanQuotePropertyMarketValue:''}" autocomplete="off" maxlength="9"  placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" cssClass="form-control" onfocus="this.value=M.unformatMoney(this.value);"/>
		</li> 
  <%-- </s:if>   --%>
    	
