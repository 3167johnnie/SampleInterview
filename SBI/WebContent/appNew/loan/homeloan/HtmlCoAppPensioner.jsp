<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_netMonthlyPension1">
	<label class="">Net monthly pension (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstNetMonthlyPension" id="co_netMonthlyPension1" value="%{quote.loanQuoteCoapplicantFirstNetMonthlyPension>0?quote.loanQuoteCoapplicantFirstNetMonthlyPension:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<li id="divco_otherIncome1">
	<label class="">Other monthly income (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"    />
</li>
<li id="divco_preEMIs1">
	<label class="">Existing EMIs, if any(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO1">
<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident1CO2.jsp"></s:include>
</s:if>
</div>
