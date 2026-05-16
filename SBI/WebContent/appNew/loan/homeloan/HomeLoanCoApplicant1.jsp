<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="panel panel-default">
  <%--  <div class="co-app-heading panel-heading">
	  <h4 class="panel-title">
		 <s:if test="%{quote.loanQuoteCoapplicantFirstDateOfBirth!=null}">
		 	<a id="coAppImage" data-toggle="collapse" class="" data-parent="#nested" href="#nested-collapseOne" aria-expanded="true">Co-applicant details 1</a>
		 </s:if>
		 <s:else>
		 	<a id="coAppImage" data-toggle="collapse" data-parent="#nested" href="#nested-collapseOne">Co-applicant details 1</a>
		 </s:else>
	  </h4>
   </div> --%>
   <div id="nested-collapseOne" 
   		class="panel-collapse collapse show" <s:property value="%{quote.loanQuoteCoapplicantFirstDateOfBirth!=null?'aria-expanded=\"true\"':''}"/>>
	  <div class="co-app-body panel-body">
		 <ul class="form-section" >
			<li id="divco_applicant_relationship1">
				<label class="">Relationship  <b class="req">*</b></label>
				<div class="flat-field">
					<s:select id="co_applicant_relationship1" name="quote.loanQuoteCoapplicantFirstRelationshipId" list="%{beanList.relationships==null?'':beanList.relationships}" 
					value="%{quote.loanQuoteCoapplicantFirstRelationshipId}" cssClass="form-select"	headerKey="0" headerValue="Select relationship" onfocus="customOnFocus(this);"/>
				</div>
			</li>
			<li id="divco_date_of_birth1">
				<label>Date of birth  <b class="req">*</b></label>
				<input type="text" name="quote.loanQuoteCoapplicantFirstDateOfBirth" id="co_date_of_birth1"  placeholder="dd-mm-yyyy" 
				autocomplete="off" maxlength="10" value="<s:property value="%{quote.loanQuoteCoapplicantFirstDateOfBirth}"/>" class="form-control dob-cal"/>
			</li>
			<li id="divco_co_gender1">
				<label>Gender<b class="req">*</b></label>
				<div class="flat-field">
					<s:select name="quote.loanQuoteCoapplicantFirstGender" id="co_gender1" value="%{quote.loanQuoteCoapplicantFirstGender}" list="%{#genderList}" headerKey="0" headerValue="Select gender" cssClass="form-select" onfocus="customOnFocus(this);"/>
				</div>
			</li>
			<li id="divco_residentType1">
				<label class="">Resident type  <b class="req">*</b></label> 
				<div class="flat-field">
					<s:select id="co_residentType1" name="quote.loanQuoteCoapplicantFirstResidentTypeId" list="%{beanList.residentTypesCoApplicant==null?'':beanList.residentTypesCoApplicant}" 
						value="%{quote.loanQuoteCoapplicantFirstResidentTypeId}" cssClass="form-select" headerKey="0" headerValue="Select resident type" onfocus="customOnFocus(this);"/>
					</div>
			</li>
			<div id="CORESIDENTTYPE">
			<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
				<li id="divco_countryRegion1">
					<label class="">Country/Region<b class="req">*</b></label>
					<div class="flat-field">
						<s:select name="quote.loanQuoteCoapplicantFirstCountryId" id="co_countryRegion1" autocomplete="off"  list="%{#countries}"
						 value="%{quote.loanQuoteCoapplicantFirstCountryId}" cssClass="form-select" headerKey="0" headerValue="Select country" onfocus="customOnFocus(this);"/>
					</div>
				</li>
			</s:if>
			</div>
			<li id="divco_employmentType1">
				<label class="">Type of employment  <b class="req">*</b></label>
				<div class="flat-field">
					<s:select name="quote.loanQuoteCoapplicantFirstEmploymentTypeId" id="co_employmentType1" autocomplete="off" list="%{beanList.employementTypesCoapplicants==null?'':beanList.employementTypesCoapplicants}" 
						value="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId}" cssClass="%{quote==null?' disabledFields':''} form-select" disabled="%{quote==null?'true':'false'}"  headerKey="0" headerValue="Select employment type" onfocus="customOnFocus(this);"/>
				</div>
			</li>
			<div id="EMPTYPECOAPP">
				<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==1}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppSalaried.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==2 || quote.loanQuoteCoapplicantFirstEmploymentTypeId==3}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppBusinessman.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==4}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppAgriculture.jsp"></s:include>
				</s:if>
				<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==5}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppPensioner.jsp"></s:include>
				</s:if>
				
				<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==6||quote.loanQuoteCoapplicantFirstEmploymentTypeId==7}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppRetired&Homemaker.jsp"></s:include>
				</s:if>
				<%-- <s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
					<s:include value="/appNew/loan/homeloan/HtmlCoAppWorkExp.jsp"></s:include>
				</s:if> --%>
			</div>
		</ul>
	  </div>
   </div> 
</div>
		
<%-- <div class="panel panel-default">
   <div class="co-app-heading panel-heading">
	  <h4 class="panel-title">
		 <s:if test="%{quote.loanQuoteCoapplicantSecondDateOfBirth!=null}">
		 	<a id="coAppImageNext2" class="" data-toggle="collapse" data-parent="#nested" href="#nested-collapseTwo" aria-expanded="true">
				Co-applicant details 2
			</a>
		 </s:if>
		 <s:else>
		 	<a id="coAppImageNext" class="collapsed" data-toggle="collapse" data-parent="#nested" href="#nested-collapseTwo" aria-expanded="false">
		 		Co-applicant details 2
			 </a>
		 </s:else>
		 
	  </h4>
   </div>
		<div id="nested-collapseTwo" class="panel-collapse collapse in 22" <s:property value="%{quote.loanQuoteCoapplicantSecondDateOfBirth!=null?'aria-expanded=\"true\"':''}"/>>
	   		<div class="co-app-body panel-body"> 
				<div id="coApp2Data">
					  <s:if test="%{quote.loanQuoteCoapplicantSecondDateOfBirth!=null}">
					  		<s:include value="/appNew/loan/homeloan/HomeLoanCoApplicant2.jsp"></s:include>
					  </s:if>
				</div>
	  		</div>
		</div>
</div> --%>
	<%--  <div class="card">
    <div class="card-header" id="headingThree">
      <h2 class="mb-0">
       <!--  <button class="btn btn-link btn-block text-left collapsed" type="button" data-toggle="collapse" data-target="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
          Collapsible Group Item #3
        </button> -->
        <s:if test="%{quote.loanQuoteCoapplicantSecondDateOfBirth!=null}">
		 	<a id="coAppImageNext2" class="btn btn-link btn-block text-left collapsed" data-toggle="collapse" data-parent="#nested" href="#nested-collapseTwo" aria-expanded="true">
				Co-applicant details 2
			</a>
		 </s:if>
		 <s:else>
		 	<a id="coAppImageNext" class="collapsed" data-toggle="collapse" data-parent="#nested" href="#nested-collapseTwo" aria-expanded="false">
		 		Co-applicant details 2
			 </a>
		 </s:else>
      </h2>
    </div>
    <div id="collapseThree" class="collapse" aria-labelledby="headingThree" data-parent="#accordionExample">
      <div class="card-body">
        And lastly, the placeholder content for the third and final accordion panel. This panel is hidden by default.
      </div>
    </div>
  </div> --%>
