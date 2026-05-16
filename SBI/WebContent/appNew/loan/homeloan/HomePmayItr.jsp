<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li>
	<label>ITR file available or not?<b class="req">*</b><a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>In case of No ITR/Form 16/Salary Slips/Audited balance Sheet is present with the applicant, Annexure A filling will be applicable.</span></a></label>
		<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
			<s:if test="%{quote.loanQuotePmayItr.equalsIgnoreCase('Y')}">
					<div class="blue-radio blue-radio-danger">
						<input type="radio" name="quote.loanQuotePmayItr" id="loanQuotePmayItrYes" checked="checked" value="Y">
						<label for="loanQuotePmayItrYes"> Yes</label>
					</div>	
					<div class="blue-radio blue-radio-danger">
						<input type="radio" name="quote.loanQuotePmayItr" id="loanQuotePmayItrNo" value="N">
						<label for="loanQuotePmayItrNo">No</label>
					</div>
			</s:if>
			 <s:else>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" id="loanQuotePmayItrYes" value="Y" name="quote.loanQuotePmayItr">
					<label for="loanQuotePmayItrYes"> Yes </label>
				</div>	
				<div class="blue-radio blue-radio-danger">
					<input type="radio"  id="loanQuotePmayItrNo" name="quote.loanQuotePmayItr" checked="checked"  value="N">
					<label for="loanQuotePmayItrNo">No </label>
				</div>
			</s:else> 
	 </div>
</li>
