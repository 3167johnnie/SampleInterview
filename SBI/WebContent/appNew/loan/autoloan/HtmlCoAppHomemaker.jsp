<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_incomeFromRegularSource1" >
<label class="">Monthly income from regular source (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstIncomeFromOtherSource" id="co_incomeFromRegularSource1" autocomplete="off"  cssClass="form-control" placeholder="Rs" onkeydown="return M.digit(event);" maxlength="9" value="%{quote.loanQuoteCoapplicantFirstIncomeFromOtherSource>0?quote.loanQuoteCoapplicantFirstIncomeFromOtherSource:''}" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
</li>
<li id="divco_preEMIs1" ><label>Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" cssClass="form-control" autocomplete="off" placeholder="Rs" maxlength="9"  value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO1">
<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==9}">
	<%-- <s:include value="/appNew/loan/homeloan/includes/WorkExResident1CO2.jsp"></s:include> --%>
</s:if>
</div>
