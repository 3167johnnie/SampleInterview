<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divsalaryAccountWithSbi">
	<label>Have a corporate salary package with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/>?<b class="req">*</b></label>
	<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
		<s:if test="%{quote.loanQuoteIndustryType >0 && quote.loanQuoteIndustryType!=9999999 && quote.loanQuoteLoanPurposeId !=5 && quote.loanQuoteResidentTypeId != 2}">
			<s:if test="%{quote.loanQuoteHaveSalaryAccountWithSbi!=null && quote.loanQuoteHaveSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveSalaryAccountWithSbi" id="salaryAccountWithSbiYes" checked="checked" value="Y">
					<label for="salaryAccountWithSbiYes"> Yes </label>
				</div>	
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveSalaryAccountWithSbi" id="salaryAccountWithSbiNo" value="N">
					<label for="salaryAccountWithSbiNo">No </label>
				</div>
			</s:if>
			<s:else>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveSalaryAccountWithSbi" id="salaryAccountWithSbiYes" value="Y">
					<label for="salaryAccountWithSbiYes"> Yes </label>
				</div>	
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="quote.loanQuoteHaveSalaryAccountWithSbi" id="salaryAccountWithSbiNo" checked="checked"  value="N">
					<label for="salaryAccountWithSbiNo">No </label>
				</div>
			</s:else>							
		</s:if>
		<s:else>
			<div class="blue-radio blue-radio-danger">
				<input type="radio" id="salaryAccountWithSbiYes" value="" name="quote.loanQuoteHaveSalaryAccountWithSbi">
				<label for="salaryAccountWithSbiYes"> Yes </label>
			</div>	
			<div class="blue-radio blue-radio-danger">
				<input type="radio"  id="salaryAccountWithSbiNo" value="" name="quote.loanQuoteHaveSalaryAccountWithSbi" checked="checked"  value="N">
				<label for="salaryAccountWithSbiNo">No </label>
			</div>
		</s:else>
	</div>
</li>    

