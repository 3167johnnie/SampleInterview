<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="COAPPLICANT2">
	 <ul class="form-section" >
		<li id="divco_applicant_relationship2">
			<label class="">Relationship  <b class="req">*</b></label>
			<div class="flat-field">
			<s:select id="co_applicant_relationship2" name="quote.loanQuoteCoapplicantSecondRelationshipId" list="%{beanList.relationships==null?'':beanList.relationships}" 
				value="%{quote.loanQuoteCoapplicantSecondRelationshipId}" cssClass="form-select"	headerKey="0" headerValue="Select relationship" onfocus="customOnFocus(this);"/>
			</div>
		</li>
		<li id="divco_date_of_birth2">
			<label class="">Date of birth  <b class="req">*</b></label>
				<input type="text" name="quote.loanQuoteCoapplicantSecondDateOfBirth" id="co_date_of_birth2"  placeholder="dd-mm-yyyy" 
	 			autocomplete="off" maxlength="10" value="<s:property value="quote.loanQuoteCoapplicantSecondDateOfBirth"/>" class="form-control dob-cal"/>
		</li>
		<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
			<li id="divco_co_gender2">
				<label>Gender<b class="req">*</b></label>
				<div class="flat-field">
				<s:select name="quote.loanQuoteCoapplicantSecondGender" id="co_gender2" value="%{quote.loanQuoteCoapplicantSecondGender}" list="%{#genderList}" headerKey="0" headerValue="Select gender" cssClass="form-select" onfocus="customOnFocus(this);"/>
				</div>
			</li>
		</s:if>
		<li id="CO_residentType2">
			<label class="">Resident type  <b class="req">*</b></label> 
			<div class="flat-field">
			<s:select id="co_residentType2" name="quote.loanQuoteCoapplicantSecondResidentTypeId" list="%{beanList.residentTypesCoApplicant==null?'':beanList.residentTypesCoApplicant}" 
				value="%{quote.loanQuoteCoapplicantSecondResidentTypeId}" cssClass="form-select" headerKey="0" headerValue="Select resident type" onfocus="customOnFocus(this);"/>
			</div>
		</li>
		<div id="COAPPOTHERCOUNTRY"></div>
		<div id="RESIDENTTYPE2">
			<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId == 2}">
				<s:include value="/appNew/loan/homeloan/HtmlCoApp2OtherCountry.jsp"></s:include>
			</s:if>
		</div>
		<li id="divco_employmentType2">
			<label>Type of employment  <b class="req">*</b></label>
			<div class="flat-field">
			<s:select name="quote.loanQuoteCoapplicantSecondEmploymentTypeId" id="co_employmentType2" autocomplete="off" list="%{beanList.employementTypesCoapplicants2==null?'':beanList.employementTypesCoapplicants2}" 
				value="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId}" cssClass="%{quote==null?' disabledFields':''} form-select" disabled="%{quote==null?'true':'false'}" headerKey="0" headerValue="Select employment type" onfocus="customOnFocus(this);"/>
			</div>
		</li>
			<div id="COEMPTYPESECAND">
				<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==1}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2Salaried.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==2 || quote.loanQuoteCoapplicantSecondEmploymentTypeId==3}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2Businessman.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==4}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2Agriculture.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==5}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2RetiredPensioner.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==6 ||quote.loanQuoteCoapplicantSecondEmploymentTypeId==7}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2RetiredHomemaker.jsp"></s:include>
				</s:if>
				<%-- <s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId == 2}">
					<s:include value="/appNew/loan/homeloan/HtmlCoApp2WorkExp.jsp"></s:include>
				</s:if> --%>
			</div>
		</ul>
<%-- </s:if>	 --%>
</div>
					
