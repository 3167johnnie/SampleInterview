<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_netMonthlySalary2">
	<label class="">Net monthly salary(<span class="font-rupee">`</span>)<b class="req">*</b></label> 
	<s:textfield name="quote.loanQuoteCoapplicantSecondMonthlySalary" id="co_netMonthlySalary2" value="%{quote.loanQuoteCoapplicantSecondMonthlySalary>0?quote.loanQuoteCoapplicantSecondMonthlySalary:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_variableMonthlyPay2">
	<label class="">Variable monthly pay(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondVariableMonthPayon" id="co_variableMonthlyPay2" value="%{quote.loanQuoteCoapplicantSecondVariableMonthPayon>0?quote.loanQuoteCoapplicantSecondVariableMonthPayon:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>	
</li>

<li id="divco_otherIncome2">
	<label class="">Other monthly income(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondOtherIncome" id="co_otherIncome2" value="%{quote.loanQuoteCoapplicantSecondOtherIncome>0?quote.loanQuoteCoapplicantSecondOtherIncome:''}" cssClass="form-control"  maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_emiPayPerMonth2">
	<label class="">Existing EMIs, if any(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_retirementAgeApplicant2">
	<label class="">Retirement age<b class="req">*</b></label>
	<div class="flat-field">
	<s:select id="co_retirementAgeApplicant2" name="quote.loanQuoteCoapplicantSecondRetirementAge" list="%{beanList.retirementAge==null?'':beanList.retirementAge}" 
		value="%{quote.loanQuoteCoapplicantSecondRetirementAge}" cssClass="form-select" headerKey="0" headerValue="Select retirement Age" onfocus="customOnFocus(this);"/>
	</div>
</li>
<div id="RESIDENTTYPEBCO2">
<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident2CO2.jsp"></s:include>
</s:if>
</div>
