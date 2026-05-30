<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:include value="/appNew/loan/autoloan/includes/AutoOccupationType.jsp"></s:include>

<li id="divgrossMonthlyIncome">
	 <label class="">Gross monthly salary (<span class="font-rupee">`</span>)<b class="req">*</b></label>
	 <s:textfield name="quote.loanQuoteGrossMonthlyIncome" id="grossMonthlyIncome" 
	 value="%{quote.loanQuoteGrossMonthlyIncome>0?quote.loanQuoteGrossMonthlyIncome:''}" autocomplete="off" 
	 maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" cssClass="form-control"
	 onfocus="this.value=M.unformatMoney(this.value);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" />
	<div class="custom-tooltip"> Gross income will be equivalent to total salary/income before any satutory deduction/repayment obligation. </div>
	
</li>
<li id="divnetMonthlySalary">
	 <label class="">Net monthly salary (<span class="font-rupee">`</span>)<b class="req">*</b></label>
	 <s:textfield name="quote.loanQuoteNetMonthlySalary" id="netMonthlySalary" cssClass="form-control" value="%{quote.loanQuoteNetMonthlySalary>0?quote.loanQuoteNetMonthlySalary:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onfocus="this.value=M.unformatMoney(this.value);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" />
	<div class="custom-tooltip"> Net monthly income will be equivalent to monthly gross salary net of all statutory deductions like income tax and compulsory deductions like EPF etc. </div>
</li>
<li id="divvariableMonthlyPay">
	<label class="">Variable monthly pay (<span class="font-rupee">`</span>)</label>
	 <s:set var="toolTip" value="%{''}"/>
	   <s:if test="%{quote.loanQuoteEmploymentTypeId==9}">
	   		<s:if test="%{isDsrPage=='false'}">
	   			<div class="custom-tooltip"> Last 3 months average of the approximate sum of all monthly incentives earned by you.</div>
	   		</s:if>
	   </s:if>
	<s:textfield name="quote.loanQuoteVariableMonthPay" id="variableMonthlyPay" cssClass="form-control" value="%{quote.loanQuoteVariableMonthPay>0?quote.loanQuoteVariableMonthPay:''}" autocomplete="off" maxlength="9" placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value); %{#toolTip}"/>
</li>
<li id="divexpectedRentalIncome">
	  <label class="">Rental income (<span class="font-rupee">`</span>)</label>
	  <s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" cssClass="form-control" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs"  onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divotherIncome">
	 <label class="">Other monthly income (<span class="font-rupee">`</span>)</label>
	 <s:textfield name="quote.loanQuoteOtherIncome" id="otherIncome" cssClass="form-control" value="%{quote.loanQuoteOtherIncome>0?quote.loanQuoteOtherIncome:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
</li>

<s:if test="%{#needToDisplay==false}">
	 <li id="divpreEMIsOther">
		 <label class="">Existing EMIs other than selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		 <s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" cssClass="form-control" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	 </li>
</s:if>
<s:else>
	<li id="divpreEMIs">
	  <label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	  <s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" cssClass="form-control" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:else>

<li id="divindustryType">
	<label class="">Organisation type <b class="req">*</b></label>
	  <div class="flat-field">
	  <s:select id="industryType" list="%{beanList.industryTypeData==null?'':beanList.industryTypeData}" name="quote.loanQuoteIndustryType"  value="%{quote.loanQuoteIndustryType}"
	   headerKey="0" headerValue="Select organisation type" cssClass="form-select" onfocus="customOnFocus(this);"/>
	   </div>
</li>
<li id="divemployerName">
	<label class="">Employer name<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteEmployerName" id="employerName" value="%{quote.loanQuoteEmployerName}" cssClass="form-control" autocomplete="off" maxlength="105" placeholder="Type slowly for auto fill"/>
	<span id="autofillLoader" class=""></span>
</li>

<div id="SALARYACCOUNTWITHSBI">
	<s:if test="%{(quote.loanQuoteLoanCategoryId==8 || quote.loanQuoteLoanCategoryId==9 || quote.loanQuoteLoanCategoryId==10) && quote.loanQuoteResidentTypeId==1}">
		<s:include value="/appNew/loan/autoloan/HtmlSalaryAccountWithSbi.jsp"></s:include>
	</s:if>
</div>

 <div id="TYPEOFSALARYPACKAGE">
	<s:if test="%{quote.loanQuoteHaveSalaryAccountWithSbi!=null && quote.loanQuoteHaveSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
		<s:include value="/appNew/loan/autoloan/HtmlTypeOfSalaryPackage.jsp"></s:include>
	</s:if>
</div>

