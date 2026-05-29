<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="add-co-applicant-box">
	<input id="coAppImageQuote" class="blue-css-checkbox" name="coAppImageQuote" type="checkbox" value="" onclick="showCoapplicant(1);" />
	<label class="label-content" for="coAppImageQuote">DO YOU WANT TO ADD CO-APPLICANT ?</label>
	<span class="note">(This may help in enhancing your loan eligibility)</span>
</div>
<!-- Get AL coapplicant -->
<div class="modal otp-box" id="coapplicantPopup" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">
						<img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/closelight.png" alt="" /></span>
				</button>
				<form method="post" id="coApplicantQuoteForm" name="coApplicantQuoteForm" action="javascript:void(0);return false;">
				<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
					<input type="hidden" name="noOfCoapplicant" id="noOfCoapplicant" value="0" /> <input type="hidden" value="2" id="" name="requestType">
					<div class="co-form-section">
						<h2>Add a Co-Applicant<small>(This may help in enhancing your loan eligibility )</small></h2>
						<ul class="form-section">
							<li id="divco_applicant_relationship1"><label class="">Relationship<b class="req">*</b>
							</label>
								<div class="flat-field">
									<s:select id="co_applicant_relationship1"
										name="quote.loanQuoteCoapplicantFirstRelationshipId"
										list="%{beanList.relationships!=null?beanList.relationships:''}"
										value="%{quote.loanQuoteCoapplicantFirstRelationshipId}"
										cssClass="form-select" headerKey="0"
										headerValue="Select relationship"
										onfocus="customOnFocus(this);"></s:select>
								</div>
							</li>
							<li id="divco_date_of_birth1AL"><label>Date of birth<b class="req">*</b></label>
							<input type="text" name="quote.loanQuoteCoapplicantFirstDateOfBirth"
								id="co_date_of_birth1" placeholder="dd-mm-yyyy"
								class="form-control dob-cal" maxlength="10"
								value="<s:property value="%{quote.loanQuoteCoapplicantFirstDateOfBirth}"/>"
								autocomplete="off" />
							</li>
							<li id="divco_residentType1">
								<label class="">Resident type<b class="req">*</b></label>
								<div class="flat-field">
									<s:select id="co_residentType1"
										name="quote.loanQuoteCoapplicantFirstResidentTypeId"
										autocomplete="off"
										list="%{beanList.residentTypesCoApplicant!=null?beanList.residentTypesCoApplicant:''}"
										value="%{quote.loanQuoteCoapplicantFirstResidentTypeId}"
										cssClass="form-select" headerKey="0"
										headerValue="Select Resident Type"
										onfocus="customOnFocus(this);" />
								</div>
							</li>
							<div id="RESIDENTTYPE1">
								<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 1}">
									<%-- <div class="div-left" id="divco_countryRegion1">
										<label class="">City<b class="req">*</b></label>
										<s:select name="quote.loanQuoteCoapplicantFirstCityId" id="co_countryRegion1" autocomplete="off"  
										list="%{#cities}" cssClass="form-control" value="%{quote.loanQuoteCoapplicantFirstCityId}" headerKey="0" headerValue="Select city" onfocus="customOnFocus(this);"/>
									</div> --%>
								</s:if>
								<s:elseif test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
									<s:include value="/appNew/loan/autoloan/includes/HtmlCoAppOtherCountry.jsp"></s:include>
								</s:elseif>
							</div>
							<li id="divco_employmentType1">
								<label class="">Type of employment<b class="req">*</b></label>
								<div class="flat-field">
									<s:select
										name="quote.loanQuoteCoapplicantFirstEmploymentTypeId"
										id="co_employmentType1" autocomplete="off"
										list="%{beanList.employementTypesCoapplicants!=null?beanList.employementTypesCoapplicants:''}"
										value="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId}"
										cssClass="form-select" headerKey="0"
										headerValue="Select employment type"
										onfocus="customOnFocus(this);" onchange="populateCoapplicants(this);"/>
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
							<li class="full-width">
								<div class="txt-center">
									<input type="submit" name="saveCoApplicantQuote" id="saveCoApplicantQuote" class="submit-btn" value="Get Loan Quote" onclick="saveCoapplicant();">
								</div>
								<!-- <input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote"> -->
							</li>
							<li class="full-width">
								<div id="errorOTPMsgCoapplicant" style="display: none;"></div>
							</li>
						</ul>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/autoloan/js<s:property value="#minFolderPath"/>/jquery.autoloan_coapplicant.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
