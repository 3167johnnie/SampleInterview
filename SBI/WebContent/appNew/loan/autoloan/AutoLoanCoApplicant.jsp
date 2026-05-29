<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<ul class="form-section" >
	<li id="divco_applicant_relationship1">
		<label class="">Relationship  <b class="req">*</b></label>
		<div class="flat-field">
			<s:select id="co_applicant_relationship1" name="quote.loanQuoteCoapplicantFirstRelationshipId" list="%{beanList.relationships!=null?beanList.relationships:''}" 
				value="%{quote.loanQuoteCoapplicantFirstRelationshipId}" cssClass="form-select" headerKey="0" headerValue="Select relationship" onfocus="customOnFocus(this);"></s:select>
		</div>
	</li>
	<li id="divco_date_of_birth1">
		<label class="">Date of Birth  <b class="req">*</b></label>
		<input type="text" name="quote.loanQuoteCoapplicantFirstDateOfBirth" id="co_date_of_birth1" placeholder="dd-mm-yyyy"  class="form-control dob-cal" maxlength="10" value="<s:property value="%{quote.loanQuoteCoapplicantFirstDateOfBirth}"/>"  autocomplete="off"/>
	</li>
	<li id="divco_residentType1">
		<label class="">Resident type  <b class="req">*</b></label><br> 
		<div class="flat-field">
			<s:select id="co_residentType1" name="quote.loanQuoteCoapplicantFirstResidentTypeId" autocomplete="off" list="%{beanList.residentTypesCoApplicant!=null?beanList.residentTypesCoApplicant:''}" 
				value="%{quote.loanQuoteCoapplicantFirstResidentTypeId}" cssClass="form-select" headerKey="0" headerValue="Select Resident Type" onfocus="customOnFocus(this);"/>
			</div>
	</li>
	<div id="RESIDENTTYPE1">
		<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 1}">
			
		</s:if>
		<s:elseif test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
			<s:include value="/appNew/loan/autoloan/includes/HtmlCoAppOtherCountry.jsp"></s:include>
		</s:elseif>
		<%-- <s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
			<li class="" id="divco_workExperience1">
				<label class="">Work experience (years)<b class="req">*</b></label><br>
				<div class="flat-field">
					<s:select name="quote.loanQuoteCoapplicantFirstWorkExperience" id="co_workExperience1" autocomplete="off" list="#{1:'>1',2:'1-2',3:'>2'}"
				 		value="%{quote.loanQuoteCoapplicantFirstWorkExperience}" cssClass="form-control" headerKey="0" headerValue="Select work experience" onfocus="customOnFocus(this);"/>
				</div>
			</li>
		</s:if> --%>
	</div>
	<li id="divco_employmentType1">
		<label class="">Type of employment  <b class="req">*</b></label><br>
		<div class="flat-field">
			<s:select name="quote.loanQuoteCoapplicantFirstEmploymentTypeId" id="co_employmentType1" autocomplete="off" list="%{beanList.employementTypesCoapplicants!=null?beanList.employementTypesCoapplicants:''}" 
				value="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId}" cssClass="form-select" headerKey="0"  headerValue="Select employment type" onfocus="customOnFocus(this);" onchange="populateCoapplicants(this);"/>
		</div>
	</li>
	<div id="EMPTYPECOAPP">
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==9}">
			<s:include value="/appNew/loan/autoloan/HtmlCoAppSalaried.jsp"></s:include>
		</s:if>
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==10 || quote.loanQuoteCoapplicantFirstEmploymentTypeId==11}">
			<s:include value="/appNew/loan/autoloan/HtmlCoAppBusinessman.jsp"></s:include>
		</s:if>
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==12}">
			<s:include value="/appNew/loan/autoloan/HtmlCoAppAgriculture.jsp"></s:include>
		</s:if>
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==13}">
			<s:include value="/appNew/loan/autoloan/HtmlCoAppPensioner.jsp"></s:include>
		</s:if>
		
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==21}">
			<s:include value="/appNew/loan/autoloan/HtmlCoAppHomemaker.jsp"></s:include>
		</s:if>
		
		<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==6}">
			<s:include value="/appNew/loan/homeloan/HtmlCoAppRetired&Homemaker.jsp"></s:include>
		</s:if>
	</div>
</ul>
<script type="text/javascript">
	   jQuery(document).ready(function(){
	   dob_date_picker('autoloanform' ,'co_date_of_birth1');
	}); 
</script>

