<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<input type="hidden" id="loanQuoteLoanType" name="quote.loanQuoteLoanType" value='<s:property value="%{quote.loanQuoteLoanType}"/>'/>
		<li id="LILOANPURPOSE">
			<label>Purpose of loan<b class="req">*</b></label>
			<div class="flat-field">
				<s:select list="%{beanList.loanPurposes?beanList.loanPurposes:''}" value="%{quote.loanQuoteLoanPurposeId}" 
					onchange="showPerspectiveRows4EductaionLoan(this);" id="loanPurpose" name="quote.loanQuoteLoanPurposeId" 
					headerKey="0" headerValue="Select Loan Purpose" cssClass="form-select %{isDsrPage=='true'?'':'disabledFields'}" disabled="%{isDsrPage=='true'?'false':'true'}" onfocus="customOnFocus(this);"/>
			</div>

	 <s:if test="%{isDsrPage=='false'}">
		<span id="plmsgId">
			<%-- <s:if test="%{quote!=null && quote.loanQuoteLoanPurposeId>0}"> --%>
				<s:if test="%{appElTypeId==1}">
					<a href='http://www.sbi.co.in/portal/web/student-platform/scholar-loan' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>
				</s:if>
				<s:elseif test="%{appElTypeId==2}">
						<a href='http://www.sbi.co.in/portal/web/student-platform/sbi-global-ed-vantage-scheme' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>
				</s:elseif >
				<s:elseif test="%{appElTypeId==3}">
						<a href='http://www.sbi.co.in/portal/web/student-platform/takeover-educational-loan' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>

				</s:elseif >
				
			<%-- </s:if> --%>
		</span>
	</s:if> 
		</li>
		<div id="LOANPURPOSE1">
			<s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId==24}"> 
				<s:include value="/appNew/loan/educationloan/includes/TakeoverloanPurpose.jsp"></s:include>
			</s:if>
		</div>	
		 
