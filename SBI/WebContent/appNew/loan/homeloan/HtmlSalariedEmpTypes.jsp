<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:set var="needToDisplay" value="%{true}"/>
<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
	<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
			<s:if test="%{isDsrPage=='false'}">
				<s:set var="needToDisplay" value="%{false}"/>
			</s:if>
	</s:if>
</s:if>
<li id="divnetMonthlySalary">
	<label class="">Net monthly salary(<span class="font-rupee">`</span>)<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteNetMonthlySalary" id="netMonthlySalary" value="%{quote.loanQuoteNetMonthlySalary>0?quote.loanQuoteNetMonthlySalary:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value); "/>
	 <div class="custom-tooltip">This is your monthly salary as per your payslip after deductions (such as PF and tax). This does not include cash payments and in-kind components. The bank will ask you to furnish details later.</div>
</li>
<li id="divvariableMonthlyPay">
	<label class="">Variable monthly pay (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteVariableMonthPay" id="variableMonthlyPay" value="%{quote.loanQuoteVariableMonthPay>0?quote.loanQuoteVariableMonthPay:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
</li>
<li id="divexpectedRentalIncome">
	<label class="">Expected monthly rental income (<span class="font-rupee">`</span>)</label>
	<s:if test="%{quote.loanQuoteLoanCategoryId == 2 && quote.loanQuoteEmploymentTypeId == 1}">
		<s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	      <div class="custom-tooltip">Rental income will be considered only where the applicant already owns a house.</div>
	</s:if>
	<s:else>
		<s:textfield name="quote.loanQuoteExpectedRentalIncome" id="expectedRentalIncome" value="%{quote.loanQuoteExpectedRentalIncome>0?quote.loanQuoteExpectedRentalIncome:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</s:else>
</li>
<li id="divotherIncome">
	<label class="">Other monthly income (<span class="font-rupee">`</span>)</label>
	<s:textfield name="quote.loanQuoteOtherIncome" id="otherIncome" value="%{quote.loanQuoteOtherIncome>0?quote.loanQuoteOtherIncome:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
</li>
<s:if test="%{#needToDisplay==false}">
	<li id="divpreEMIsOther">
		<label>Existing EMIs other than selected loan (<span class="font-rupee">`</span>)<b class="req"></b></label>
		<s:textfield name="quote.loanQuotePreEMIsOther" id="preEMIsOther" value="%{quote.loanQuotePreEMIsOther>0?quote.loanQuotePreEMIsOther:''}" cssClass="form-control" autocomplete="off" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:if>
<s:else>
	<li id="divpreEMIs">
		<label class="">Existing EMIs, if any (<span class="font-rupee">`</span>)<b class="req"><s:property escapeHtml="false" value="%{#isMandatory}"/></b></label>
		<s:textfield name="quote.loanQuotePreEMIs" id="preEMIs" value="%{quote.loanQuotePreEMIs>0?quote.loanQuotePreEMIs:''}" autocomplete="off" cssClass="form-control" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);"  onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
</s:else>
<li id="divretirementAgeApplicant">
	<label class="">Retirement age <b class="req">*</b></label>
	<div class="flat-field">
		<s:select name="quote.loanQuoteRetirementAgeApplicant" id="retirementAgeApplicant" autocomplete="off" list="%{beanList.retirementAge==null?'':beanList.retirementAge}" 
		value="%{quote.loanQuoteRetirementAgeApplicant}" cssClass="form-select" headerKey="0" headerValue="Select retirement age" onfocus="customOnFocus(this);"/>
	</div>
</li>
<li id="divindustryType">
	<label class="">Organization type <b class="req">*</b></label>
	<div class="flat-field">
		<s:select id="industryType" list="%{beanList.industryTypeData==null?'':beanList.industryTypeData}" name="quote.loanQuoteIndustryType" value="%{quote.loanQuoteIndustryType}"
			cssClass="form-select" headerKey="0" headerValue="Select organisation type" onfocus="customOnFocus(this);"/>
	</div>
</li>
<li id="divemployerName">
	<label class="">Employer name<b class="req">*</b></label>
	<s:textfield name="quote.loanQuoteEmployerName" id="employerName" value="%{quote.loanQuoteEmployerName}" cssClass="form-control" autocomplete="off" maxlength="105" placeholder="Type slowly for auto fill"/>
	<span id="autofillLoader" class=""></span>
</li>
<div id="SALARYACCOUNTWITHSBI">
	<s:if test="%{quote.loanQuoteIndustryType >0 && quote.loanQuoteIndustryType!=9999999 && quote.loanQuoteLoanPurposeId !=5 && quote.loanQuoteResidentTypeId != 2}">
		<s:include value="/appNew/loan/homeloan/HtmlSalaryAccountWithSbi.jsp"></s:include>
	</s:if>
</div>
<div id="corpsalarypackage">
	<s:if test="%{quote.loanQuoteHaveSalaryAccountWithSbi!=null && quote.loanQuoteHaveSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
		<s:include value="/appNew/loan/homeloan/HtmlCorporateSalarypackage.jsp"></s:include>
	</s:if>
</div>
<div id="RESIDENTTYPEB">
<s:if test="%{quote.loanQuoteResidentTypeId==1}">
	<s:if test="%{quote.loanQuoteLoanPurposeId==1 && quote.loanQuoteLoanCategoryId !=3 && quote.loanQuoteEmploymentTypeId == 1}">
		<s:include value="/appNew/loan/homeloan/includes/WorkExResident1.jsp"></s:include>
	</s:if>
</s:if>
<s:elseif test="%{quote.loanQuoteResidentTypeId==2}">
	<s:include value="/appNew/loan/homeloan/includes/WorkExResident2.jsp"></s:include>
</s:elseif>
</div>
<div id="CHECKOFFTYPE">
	<s:if test="%{quote.loanQuoteLoanPurposeId!=2 && quote.loanQuoteLoanPurposeId!=5 && quote.loanQuoteResidentTypeId!=2 && (quote.loanQuoteIndustryType==20 || quote.loanQuoteIndustryType==83)}">
			<s:include value="/appNew/loan/homeloan/SalaryCheckOff.jsp"></s:include>
	</s:if>
</div>
