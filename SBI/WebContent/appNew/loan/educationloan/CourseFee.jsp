<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#needToDisable==true}">
	<li style="display:<s:property value="%{quote.loanQuoteCourseTypeId!=3?'block':'none'}"/>;">
		<label>Admission fee</label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteAdmissionFee" id="AdmissionFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteAdmissionFee>0?quote.loanQuoteAdmissionFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<li id="447">
		<label>Tuition fee <b class="req">*</b></label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteTuitionFee" id="tuitionFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteTuitionFee>0?quote.loanQuoteTuitionFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" />
	</li>
	<li id="448" style="display:<s:property value="%{quote.loanQuoteCourseTypeId!=3?'block':'none'}"/>;">
		<label>Hostel fee/Living expenses</label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteHostelFee" id="hostelFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteHostelFee>0?quote.loanQuoteHostelFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<li id="449">
		<label>Examination/Lab/Library fee</label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteExaminationLabOrLibFee" id="examinationFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteExaminationLabOrLibFee>0?quote.loanQuoteExaminationLabOrLibFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<li id="450">
		<label>Purchase of books </label>
		<s:textfield cssClass="form-control" name="quote.loanQuotePurchaseOfBookAndEquipment" id="purchaseOfBook" autocomplete="off" aria-invalid="false" value="%{quote.loanQuotePurchaseOfBookAndEquipment>0?quote.loanQuotePurchaseOfBookAndEquipment:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<li id="451">
		<label>Caution fee</label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteCautionFee" id="cautionFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteCautionFee>0?quote.loanQuoteCautionFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value); "/>
	</li>
	<div id="TRAVELEXPENSESDIV">
		<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE}"> 
			<s:if test="%{quote!=null && quote.loanQuoteCountryOfStudyId == 2 && quote.loanQuoteCourseTypeId!=3}">
				<s:include value="/appNew/loan/educationloan/includes/TravelExpenses.jsp"></s:include>
			</s:if>
		</s:if>
	</div>
</s:if>
	<li id="454">
		<label>Purchase of computer</label>
		<s:textfield cssClass="form-control" name="quote.loanQuotePurchaseOfComputer" id="purchaseOfComputer" autocomplete="off" aria-invalid="false" value="%{quote.loanQuotePurchaseOfComputer>0?quote.loanQuotePurchaseOfComputer:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<li id="456">
		<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
			<label>Misc. expenses</label>
			<s:textfield cssClass="form-control" name="quote.loanQuoteOtherExpenses" id="miscExpense" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteOtherExpenses>0?quote.loanQuoteOtherExpenses:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
		</s:if>
	</li>
	<li id="721">
		<label>Fellowship/Scholarship</label>
		<s:textfield cssClass="form-control" name="quote.loanQuoteScholarship" id="loanQuoteScholarship" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteScholarship>0?quote.loanQuoteScholarship:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	</li>
	<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE || (isDsrPage=='true' && appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE)}">
		<li id="divoverseasHealthInsuranceFee">
			<label class="">Overseas Health Insurance fee</label>
			<s:textfield cssClass="form-control" name="quote.loanQuoteOverseasHealthInsuranceFee" id="overseasHealthInsuranceFee" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteOverseasHealthInsuranceFee>0?quote.loanQuoteOverseasHealthInsuranceFee:''}"  placeholder="Rs" maxlength="9" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
		</li>
	</s:if>
