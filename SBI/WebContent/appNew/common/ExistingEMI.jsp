<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="needToDisplay" value="true"/>
	<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
		<s:set var="needToDisplay" value="false"/>
	</s:if>
<div id="expRentalIncome"></div>
<li>
	<s:if test="%{#needToDisplay==false}">
		<label class="">Existing EMIs other than selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</s:if>
	<s:else>
		<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)</label>
		<s:textfield  value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}"  name="quote.loanQuotePreEMIs" id="preEMIs" autocomplete="off" cssClass="form-control" placeholder="Rs" maxlength="9" 
		onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
	</s:else>
</li>
