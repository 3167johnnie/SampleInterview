<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divisAvaiReimbursement">
	<label>Do you want to avail reimbursement?<b class="req">*</b>
	<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Please select Yes if investment is made from own resources or through borrowing from friends, relatives and employers only during the preceding 12 months.</span></a>
	</label>
			<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
			<s:property escapeHtml="false" value="%{#br}"/>
  	<s:if test="%{quote.loanQuoteReimburse.equalsIgnoreCase('Y')}">
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuoteReimburse" id="isAvaiReimbursementYes" checked="checked" value="Y">
			<label for="isAvaiReimbursementYes"> Yes </label>
		</div>	
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuoteReimburse" id="isAvaiReimbursementNo" value="N">
			<label for="isAvaiReimbursementNo">No </label>
		</div>
 	</s:if> 
	<s:else>
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuoteReimburse" id="isAvaiReimbursementYes" value="Y">
			<label for="isAvaiReimbursementYes"> Yes </label>
		</div>	
		<div class="blue-radio blue-radio-danger">
			<input type="radio" name="quote.loanQuoteReimburse" id="isAvaiReimbursementNo" checked="checked" value="N">
			<label for="isAvaiReimbursementNo">No </label>
		</div>
	</s:else>							
	</div>
</li>    
<div id="ACTUALINVESTMENTMADE"> 
	<s:if test="%{quote.loanQuoteReimburse.equalsIgnoreCase('Y')}"> 
		<s:include value="/appNew/loan/homeloan/AvaiReimbursement.jsp"></s:include>
</s:if>
</div>
