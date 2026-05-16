<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li id="divincomeFromRegularSource">
	<label class="">Monthly income from regular source (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteIncomeFromRegularSource" id="incomeFromRegularSource" value="%{quote.loanQuoteIncomeFromRegularSource>0?quote.loanQuoteIncomeFromRegularSource:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>

<s:if test="%{#needToDisplay==false}">
	<li id="divpreEMIsOther">
		<label class="">Existing EMIs, if any other selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:if>
<s:else>
	<li id="divpreEMIs">
		<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)<b class="req"><s:property escapeHtml="false" value="%{#isMandatory}"/></b></label>
		<s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" cssClass="form-control"  autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:else>
<%-- <s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4)}">
	<ul>
		<s:include value="/appNew/loan/homeloan/HomePmayItr.jsp"></s:include> 
	</ul>
</s:if> --%>
