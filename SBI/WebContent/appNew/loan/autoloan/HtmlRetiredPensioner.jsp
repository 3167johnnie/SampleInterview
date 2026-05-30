<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/appNew/loan/autoloan/includes/AutoOccupationType.jsp"></s:include>
<li id="divnetMontlhyPension">
	<label class="">Net monthly pension (<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteNetMonthlyPension" id="netMontlhyPension" cssClass="form-control" value="%{quote.loanQuoteNetMonthlyPension>0?quote.loanQuoteNetMonthlyPension:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
</li>
<li id="divexpectedRentalIncome">
	<label>Rental income 
	(<span class="font-rupee">`</span>)</label>
	 <s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" cssClass="form-control" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divotherIncome"><label class="">Other monthly income (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteOtherIncome" id="otherIncome"  cssClass="form-control" value="%{quote.loanQuoteOtherIncome>0?quote.loanQuoteOtherIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
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

<li id="divpensionAccountWithSbi">
<label class="">Is your pension account with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/>?<b class="req">*</b></label>
<div class="col-xs-12 mrgt-10-10">
		<s:if test="%{quote.loanQuotePensionAccountWithSbi.equalsIgnoreCase('Y')}">
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuotePensionAccountWithSbi" id="pensionAccountWithSbiYes" checked="checked" value="Y">
			<label for="pensionAccountWithSbiYes"> Yes </label>
		</div>	
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuotePensionAccountWithSbi" id="pensionAccountWithSbiNo" value="N">
			<label for="pensionAccountWithSbiNo">No </label>
		</div>
	</s:if>
	<s:else>
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuotePensionAccountWithSbi" id="pensionAccountWithSbiYes" value="Y">
			<label for="pensionAccountWithSbiYes"> Yes </label>
		</div>	
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuotePensionAccountWithSbi" id="pensionAccountWithSbiNo" checked="checked"  value="N">
			<label for="pensionAccountWithSbiNo">No </label>
		</div>
	</s:else>							
</div>
	    
	    
	    
	    
	
</li>
