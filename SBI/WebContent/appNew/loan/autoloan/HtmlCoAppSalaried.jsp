<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_grossMonthlyIncome1">
	<label class="">Gross monthly salary(<span class="font-rupee">`</span>)<b class="req">*</b></label> 
	<s:textfield name="quote.loanQuoteCoapplicantFirstGrossMonthlyIncome" id="co_grossMonthlyIncome1" value="%{quote.loanQuoteCoapplicantFirstGrossMonthlyIncome>0?quote.loanQuoteCoapplicantFirstGrossMonthlyIncome:''}" cssClass="form-control" maxlength="10" placeholder="Rs" onkeydown="return M.digit(event);" onfocus="this.value=M.unformatMoney(this.value);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" />
	<div class="custom-tooltip"> Gross income will be equivalent to total salary/income before any satutory deduction/repayment obligation. </div>
	
</li>
<li id="divnetMonthlySalary">
	<label class="">Net monthly salary(<span class="font-rupee">`</span>)<b class="req">*</b></label> 
	<s:textfield name="quote.loanQuoteCoapplicantFirstMonthlySalary" id="co_netMonthlySalary1" value="%{quote.loanQuoteCoapplicantFirstMonthlySalary>0?quote.loanQuoteCoapplicantFirstMonthlySalary:''}" cssClass="form-control" maxlength="10" placeholder="Rs" onkeydown="return M.digit(event);" onfocus="this.value=M.unformatMoney(this.value);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" />
	<div class="custom-tooltip">Net Monthly Income will be equivalent to monthly Gross Salary net of all statutory deductions like Income Tax and compulsory deductions like EPF etc. </div>
	
</li>
<li id="divvariableMonthlyPay">
	<label class="">Variable monthly pay(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstVariableMonthPay" id="co_variableMonthlyPay1" value="%{quote.loanQuoteCoapplicantFirstVariableMonthPay>0?quote.loanQuoteCoapplicantFirstVariableMonthPay:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>	
</li>
<li id="divco_otherIncome1">
	<label class="">Other monthly income(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}"  maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" cssClass="form-control" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_emiPayPerMonth1">
	<label class="">Existing EMIs, if any(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" cssClass="form-control"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>			

