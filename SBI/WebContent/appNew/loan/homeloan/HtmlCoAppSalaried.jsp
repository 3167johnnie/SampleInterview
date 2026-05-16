<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_netMonthlySalary1">
	<label class="">Net monthly salary(<span class="font-rupee">`</span>)<b class="req">*</b></label> 
	<s:textfield name="quote.loanQuoteCoapplicantFirstMonthlySalary" id="co_netMonthlySalary1" value="%{quote.loanQuoteCoapplicantFirstMonthlySalary>0?quote.loanQuoteCoapplicantFirstMonthlySalary:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_variableMonthlyPay1">
	<label class="">Variable monthly pay(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstVariableMonthPayon" id="co_variableMonthlyPay1" value="%{quote.loanQuoteCoapplicantFirstVariableMonthPayon>0?quote.loanQuoteCoapplicantFirstVariableMonthPayon:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>	
</li>

<li id="divco_otherIncome1">
	<label class="">Other monthly income(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}" cssClass="form-control"  maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_emiPayPerMonth1">
	<label class="">Existing EMIs, if any(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_retirementAgeApplicant1">
	<label class="">Retirement age<b class="req">*</b></label>
	<div class="flat-field">
		<s:select id="co_retirementAgeApplicant1" name="quote.loanQuoteCoapplicantFirstRetirementAge" list="%{beanList.retirementAge==null?'':beanList.retirementAge}" 
			value="%{quote.loanQuoteCoapplicantFirstRetirementAge}" cssClass="form-select" headerKey="0" headerValue="Select retirement Age" onfocus="customOnFocus(this);"/>
	</div>
</li>

<div id="RESIDENTTYPEBCO1">
<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident1CO2.jsp"></s:include>
</s:if>
</div>
