<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_netAnnualIncome2">
	<label class="">Net annual income (<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondNetAnnualIncome" id="co_netAnnualIncome2" value="%{quote.loanQuoteCoapplicantSecondNetAnnualIncome>0?quote.loanQuoteCoapplicantSecondNetAnnualIncome:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_otherIncome2">
	<label class="">Other monthly income(<span class="font-rupee">`</span>) </label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondOtherIncome" id="co_otherIncome2" value="%{quote.loanQuoteCoapplicantSecondOtherIncome>0?quote.loanQuoteCoapplicantSecondOtherIncome:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_preEMIs2">
	<label class="">Existing EMIs, if any(<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO2">
<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident2CO2.jsp"></s:include>
</s:if>
</div>
