<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<s:if test="%{#needToDisable==true}">
		<li id="462"><label> Last educational qualification<b class="req">*</b></label>
			<div class="flat-field ">
					<s:if test="%{quote.loanQuoteCountryOfStudyId ==null || quote.loanQuoteCountryOfStudyId==2}">
						<s:select list="#{'0':'Select educational qualification','5':'Doctrate','1':'Post Graduate or Above','2':'Graduate','3':'HSC(10+2)'}"
							cssClass="form-select" id="lastEducationalQualificationId"
							name="quote.loanQuoteLastEducationalQualificationId"
							autocomplete="off" aria-invalid="false"
							value="%{quote.loanQuoteLastEducationalQualificationId}" onfocus="customOnFocus(this);"/>
					</s:if>
					<s:else>
						<s:select list="#{'0':'Select educational qualification','5':'Doctrate','1':'Post Graduate or Above','2':'Graduate','3':'HSC(10+2)','4':'Below Higher Secondary'}"
							cssClass="form-select" id="lastEducationalQualificationId"
							name="quote.loanQuoteLastEducationalQualificationId"
							autocomplete="off" aria-invalid="false"
							value="%{quote.loanQuoteLastEducationalQualificationId}" onfocus="customOnFocus(this);"/>
					</s:else>
			</div>
		</li>
	</s:if>
	<div id="COLLECTRAL-SECURITY">
		<s:if test="%{quote.loanQuoteCountryOfStudyId!=null && quote.loanQuoteCountryOfStudyId==1 && quote.loanQuoteInstituteCat!=null && quote.loanQuoteInstituteCat.equalsIgnoreCase('B')}">
				<s:include value="/appNew/loan/educationloan/includes/EducationCollectralSecurity.jsp"></s:include>
		</s:if>
	</div>
		<li id="RESIDENTTYPE"><label>Gender<b class="req">*</b></label>
			<div class="flat-field">
				<s:select name="quote.loanQuoteGender" id="loanQuoteGender"
					list="#{'M':'Male', 'F':'Female','O':'Other'}"
					value="%{quote.loanQuoteGender}" autocomplete="off"
					cssClass="form-select" headerKey="0" headerValue="Select gender" onfocus="customOnFocus(this);"/>
			</div>
		</li>
	<s:if test="%{#needToDisable==false}">
		<li id="tkdob">
			<label>Date of birth<b class="req">*</b></label>
				<input type="text" name="quote.loanQuoteDateOfBirth" id="dateOfBirth"  placeholder="dd-mm-yyyy" class="form-control dob-cal"
				 maxlength="10" value="<s:property value="%{quote.loanQuoteDateOfBirth}"/>" >
		</li>
		<li id="RESIDENTTYPE1"><label>Resident type<b class="req">*</b></label>
			<div class="flat-field">
				<s:select list="%{beanList.residentTypes==null?'':beanList.residentTypes}"
					id="residentType" name="quote.loanQuoteResidentTypeId"
					onchange="showPerspectiveRows4ResidentType4ELtakeOver(this)"
					value="%{quote.loanQuoteResidentTypeId}" headerKey="0"
					headerValue="Select resident type" cssClass="form-select" onfocus="customOnFocus(this);"/>
			</div>
		</li>
	</s:if>
	<div id="RESIDENTTYPENRI">
		<s:if test="%{quote.loanQuoteResidentTypeId==2}">
				<s:include value="/appNew/loan/educationloan/includes/TakeoverCountry.jsp"></s:include>
		</s:if>
	</div>
 <s:if test="%{#needToDisable==true}">
	<li id="divworkexperience"><label class="">Work experience<b class="req">*</b></label>
		<div class="flat-field">
			<s:select name="quote.loanQuoteWorkExperience" id="workExperience"
				list="#{2:'Less than 2 year or none',3:'2 years and above'}"
				value="%{quote.loanQuoteWorkExperience}" autocomplete="off"
				cssClass="form-select" headerKey="0"
				headerValue="Select experience"  onfocus="customOnFocus(this);"/>
		</div>
	</li>
</s:if> 
