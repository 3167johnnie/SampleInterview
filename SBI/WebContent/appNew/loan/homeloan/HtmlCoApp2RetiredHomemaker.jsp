<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divco_incomeFromRegularSource2" ><label class="">Monthly income from regular source  (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondIncomeFromOtherSource" id="co_incomeFromRegularSource2" cssClass="form-control" autocomplete="off" placeholder="Rs" maxlength="9" value="%{quote.loanQuoteCoapplicantSecondIncomeFromOtherSource>0?quote.loanQuoteCoapplicantSecondIncomeFromOtherSource:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divco_emiPayPerMonth2" ><label>Existing EMIs, if any  (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" autocomplete="off" cssClass="form-control" placeholder="Rs" maxlength="10"  value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<div id="RESIDENTTYPEBCO2">
<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident2CO2.jsp"></s:include>
</s:if>
</div>
