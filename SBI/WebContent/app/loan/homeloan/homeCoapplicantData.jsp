<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
<div id="coAppImageData" class="field-row3">
	<s:if test="%{quote.loanQuoteCoapplicantFirstId==1}">
					<div class="div-left3" id="divco_applicant_relationship1">
						<label class="">Select relationship  <b class="req">*</b></label><br>
						<s:select id="co_applicant_relationship1" name="quote.loanQuoteCoapplicantFirstRelationshipId" list="%{beanList.relationships}" 
							value="%{quote.loanQuoteCoapplicantFirstRelationshipId}"	headerKey="0" headerValue="Select Relationship" />
					</div>
					<div class="div-left3" id="divco_date_of_birth1">
						<label class="">Date of Birth  <b class="req">*</b></label><br>
						<s:textfield name="quote.loanQuoteCoapplicantFirstDateOfBirth" id="co_date_of_birth1" placeholder="dd-mm-yyyy"  cssClass="" maxlength="10"  
							value="%{quote.loanQuoteCoapplicantFirstDateOfBirth}" size="10"/> 
					</div>
					<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
						<div class="div-left3 " id="divco_co_gender1">
							<label>Gender<b class="req">*</b></label><br>
							<s:select name="quote.loanQuoteCoapplicantFirstGender" id="co_gender1" value="%{quote.loanQuoteCoapplicantFirstGender}" list="%{#genderList!=null?#genderList:''}" headerKey="0" headerValue="Select gender" cssClass="" />
						</div>
					</s:if>
					<div class="div-left3" id="divco_residentType1">
						<label class="">Resident type  <b class="req">*</b></label><br> 
						<s:select id="co_residentType1" name="quote.loanQuoteCoapplicantFirstResidentTypeId" list="%{beanList.residentTypesCoApplicant}" 
							value="%{quote.loanQuoteCoapplicantFirstResidentTypeId}" headerKey="0" headerValue="Select Resident Type" />
					</div>
					<s:if test="%{quote.loanQuoteCoapplicantFirstResidentTypeId == 2}">
						<div id="co_residentType1Data">
							<div class="div-left3" id="divco_countryRegion1">
								<label class="">Select your Country/Region<b class="req">*</b></label><br>
								<s:select name="quote.loanQuoteCoapplicantFirstCountryId" id="co_countryRegion1" autocomplete="off"  list="%{#countries}"
								 value="%{quote.loanQuoteCoapplicantFirstCountryId}" headerKey="0" headerValue="Select Country" />
							</div>
							<div class="div-left3" id="divco_workExperience1">
								<label class="">Work experience (years)<b class="req">*</b></label><br>
								<s:select name="quote.loanQuoteCoapplicantFirstWorkExperience" id="co_workExperience1" autocomplete="off" list="#{1:'<1',2:'1-2',3:'>2'}"
							 		value="%{quote.loanQuoteCoapplicantFirstWorkExperience}" headerKey="0" headerValue="Select Work experience"/>
								
							</div>
						</div>
					</s:if>
					<div class="div-left3" id="divco_employmentType1">
						<label class="">Type of employment  <b class="req">*</b></label><br>
						<s:select name="quote.loanQuoteCoapplicantFirstEmploymentTypeId" id="co_employmentType1" autocomplete="off" list="%{beanList.employementTypes}" 
							value="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId}" headerKey="0" headerValue="Select Employment type" />
					</div>
					<div id="EMPTYPE1">
						<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==1}">
							<div class="div-left3" id="divco_netMonthlySalary1">
								<label class="">Net monthly Salary(<span class="wr">Rs</span>)<b class="req">*</b></label><br> 
								<s:textfield name="quote.loanQuoteCoapplicantFirstMonthlySalary" id="co_netMonthlySalary1" value="%{quote.loanQuoteCoapplicantFirstMonthlySalary>0?quote.loanQuoteCoapplicantFirstMonthlySalary:''}" maxlength="9" placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<div class="div-left3" id="divco_variableMonthlyPay1">
								<label class="">Variable monthly pay(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstVariableMonthPayon" id="co_variableMonthlyPay1" value="%{quote.loanQuoteCoapplicantFirstVariableMonthPayon>0?quote.loanQuoteCoapplicantFirstVariableMonthPayon:''}" maxlength="9"  placeholder="Rs" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>	
							</div>
							<%-- <div class="div-left3" id="divco_expectedRentalIncome1">
								<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstExpectedRentalIncome" id="co_expectedRentalIncome1" value="%{quote.loanQuoteCoapplicantFirstExpectedRentalIncome>0?quote.loanQuoteCoapplicantFirstExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
							</div> --%>
							<div class="div-left3" id="divco_otherIncome1">
								<label class="">Other monthly income(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<div class="div-left3" id="divco_preEMIs1">
								<label class="">Existing EMIs if any(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<div class="div-left3" id="divco_retirementAgeApplicant1">
								<label class="">Retirement Age<b class="req">*</b></label><br>
								<s:select id="co_retirementAgeApplicant1" name="quote.loanQuoteCoapplicantFirstRetirementAge" list="%{beanList.retirementAge}" 
									value="%{quote.loanQuoteCoapplicantFirstRetirementAge}" headerKey="0" headerValue="Select Retirement Age" />
							</div>
							<%-- <div class="div-left3" id="">
								<label class="">Have a corporate salary package with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/>?<b class="req">*</b></label><br>
								<s:if test="%{quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi!=null &&  quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
									<input type="radio" name="quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi1Yes" checked="checked" value="Y"><span style="float:left;"> Yes&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<input type="radio" name="quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi1No" value="N"><span style="float:left;"> No</span>
								</s:if>
								<s:else>
									<input type="radio" name="quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi1Yes" value="Y"><span style="float:left;"> Yes&nbsp;&nbsp;&nbsp;&nbsp;</span>
									<input type="radio" name="quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi1No" checked="checked" value="N"><span style="float:left;"> No</span>
								</s:else>
							</div>
							<s:if test="%{quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi!=null && quote.loanQuoteCoapplicantFirstCoSalaryAccountWithSbi.equalsIgnoreCase('Y')}">
								<div class="div-left3" id="">
									<label class="">Employer Name<b class="req">*</b></label><br>
									<s:textfield name="quote.loanQuoteCoapplicantFirstCoEmplyerName" id="co_employerName1" value="%{quote.loanQuoteCoapplicantFirstCoEmplyerName}" autocomplete="off" maxlength="105"/>
									<script type="text/javascript">auto_suggestion_employer('co_employerName1');</script>
								</div>
							</s:if> --%>
						</s:if>
						<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==2 || quote.loanQuoteCoapplicantFirstEmploymentTypeId==3}">
							<div class="div-left3" id="divco_profitAfterTax1">
								<label class="">Profit After Tax(<span class="wr">Rs</span>)<b class="req">*</b></label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstProfitAfterTax" id="co_profitAfterTax1" value="%{quote.loanQuoteCoapplicantFirstProfitAfterTax>0?quote.loanQuoteCoapplicantFirstProfitAfterTax:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<div class="div-left3" id="divco_depreciation1">
								<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBBJ}">
									<label class="">Depreciation last 3 years average (<span class="wr">Rs</span>)/Depreciation last year (<span class="wr">Rs</span>)</label><br>
								</s:if>
								<s:else>
									<label class="">Depreciation last 3 years average (<span class="wr">Rs</span>)</label><br>
								</s:else>
								<s:textfield name="quote.loanQuoteCoapplicantFirstDepreciatiation" id="co_depreciation1" value="%{quote.loanQuoteCoapplicantFirstDepreciatiation>0?quote.loanQuoteCoapplicantFirstDepreciatiation:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<%-- <div class="div-left3" id="divco_expectedRentalIncome1">
								<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstExpectedRentalIncome" id="co_expectedRentalIncome1" value="%{quote.loanQuoteCoapplicantFirstExpectedRentalIncome>0?quote.loanQuoteCoapplicantFirstExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
							</div> --%>
							<div class="div-left3" id="divco_preEMIs1">
								<label class="">Existing EMIs if any (<span class="wr">Rs</span>) </label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
						</s:if>
						<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==4}">
							<div class="div-left3" id="divco_netAnnualIncome1">
								<label class="">Net Annual Income (<span class="wr">Rs</span>)<b class="req">*</b></label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstNetAnnualIncome" id="co_netAnnualIncome1" value="%{quote.loanQuoteCoapplicantFirstNetAnnualIncome>0?quote.loanQuoteCoapplicantFirstNetAnnualIncome:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<%-- <div class="div-left3" id="divco_expectedRentalIncome1">
								<label class="">Expected monthly rental Income (<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstExpectedRentalIncome" id="co_expectedRentalIncome1" value="%{quote.loanQuoteCoapplicantFirstExpectedRentalIncome>0?quote.loanQuoteCoapplicantFirstExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
							</div> --%>
							<div class="div-left3" id="divco_otherIncome1">
								<label class="">Other monthly income(<span class="wr">Rs</span>) </label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
						</s:if>
						<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==5}">
							<div class="div-left3" id="divco_netMonthlyPension1">
								<label class="">Net Monthly pension Income (<span class="wr">Rs</span>)<b class="req">*</b></label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstNetMonthlyPension" id="co_netMonthlyPension1" value="%{quote.loanQuoteCoapplicantFirstNetMonthlyPension>0?quote.loanQuoteCoapplicantFirstNetMonthlyPension:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<%-- <div class="div-left3" id="divco_expectedRentalIncome1">
								<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstExpectedRentalIncome" id="co_expectedRentalIncome1" value="%{quote.loanQuoteCoapplicantFirstExpectedRentalIncome>0?quote.loanQuoteCoapplicantFirstExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
							</div> --%>
							<div class="div-left3" id="divco_otherIncome1">
								<label class="">Other monthly income (<span class="wr">Rs</span>)</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstOtherIncome" id="co_otherIncome1" value="%{quote.loanQuoteCoapplicantFirstOtherIncome>0?quote.loanQuoteCoapplicantFirstOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
							<div class="div-left3" id="divco_preEMIs1">
								<label class="">Existing EMIs if any(<span class="wr">Rs</span>)
								</label><br>
								<s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_preEMIs1" value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
							</div>
						</s:if>
						
						<s:if test="%{quote.loanQuoteCoapplicantFirstEmploymentTypeId==6||quote.loanQuoteCoapplicantFirstEmploymentTypeId==7}">
							<div id="divco_incomeFromRegularSource1" class="div-left3">
							<label class="">Income From Regular Source (<span class="wr">Rs</span>)<b class="req">*</b></label>
							<br><s:textfield name="quote.loanQuoteCoapplicantFirstPreEMIs" id="co_incomeFromRegularSource1" autocomplete="off" class="w167" onkeydown="return M.digit(event);" maxlength="9"  placeholder="Rs"  value="%{quote.loanQuoteCoapplicantFirstIncomeFromOtherSource>0?quote.loanQuoteCoapplicantFirstIncomeFromOtherSource:''}" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);" /></div>
							<div id="divco_preEMIs1" class="div-left3"><label class="">Existing EMIs, if any (<span class="wr">Rs</span>)</label><br>
							<s:textfield name="co_preEMIs1" id="co_preEMIs1" autocomplete="off" class="w167" maxlength="9"  placeholder="Rs"   value="%{quote.loanQuoteCoapplicantFirstPreEMIs>0?quote.loanQuoteCoapplicantFirstPreEMIs:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/></div>
						</s:if>
					</div>
	</s:if>
	<s:else>
			<div class="div-left3" id="divco_applicant_relationship1">
				<label class="">Select relationship  <b class="req">*</b></label><br>
				<s:select id="co_applicant_relationship1" name="quote.loanQuoteCoapplicantFirstRelationshipId" list="%{beanList.relationships}" 
					value="%{quote.loanQuoteCoapplicantFirstRelationshipId}"	headerKey="0" headerValue="Select Relationship" />
			</div>
			<div class="div-left3" id="divco_date_of_birth1">
				<label class="">Date of Birth  <b class="req">*</b></label><br>
				<input type="text" name="quote.loanQuoteCoapplicantFirstDateOfBirth" id="co_date_of_birth1" placeholder="dd-mm-yyyy" class="" value="" maxlength="10"  placeholder="Rs"  size="10"  > 
			</div>
			<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
				<div class="div-left3 " id="divco_co_gender1">
					<label>Gender<b class="req">*</b></label><br>
					<s:select name="quote.loanQuoteCoapplicantFirstGender" id="co_gender1" value="%{quote.loanQuoteCoapplicantFirstGender}" list="%{#genderList!=null?#genderList:''}" headerKey="0" headerValue="Select gender" cssClass="" />
				</div>
			</s:if>
			<div class="div-left3" id="divco_residentType1">
				<label class="">Resident type  <b class="req">*</b></label><br> 
				<s:select id="co_residentType1" name="quote.loanQuoteCoapplicantFirstResidentTypeId" list="%{beanList.residentTypesCoApplicant}" 
					value="" headerKey="0" headerValue="Select Resident Type" />
			</div>
			<div class="div-left3" id="divco_employmentType1">
				<label class="">Type of employment  <b class="req">*</b></label><br>
				<s:select name="quote.loanQuoteCoapplicantFirstEmploymentTypeId" id="co_employmentType1" autocomplete="off" list="%{beanList.employementTypes}" 
					value="" headerKey="0" headerValue="Select Employment Type" />
			</div>
	</s:else>

</div>
	
<div class="clear"></div>
<div id="co_app_second" class="sub-titles-fade-quote">Add another Co-applicant
	<span style="float: right; margin-top:0px; display: block; width: 65%;">
	<a herf="#" id="coAppImageNextQuote" name="coAppImageNextQuote" style="text-decoration: none; cursor: pointer; font-size:20px!important; margin-top: 0px; float: right; margin-left: -1px; font-weight: bold; margin-right: 11px; color:#ffffff;">+</a>
	</span>
</div>
<div class="clear"></div>
<!-- <div id="coAppImageNextQuoteData" class="last-co-applicant field-row3" ></div> -->
<div class="clear"></div>
	<s:if test="%{quote.loanQuoteCoapplicantSecondId==1}">
		<div class="div-left3" id="divco_applicant_relationship2">
			<label class="">Select relationship  <b class="req">*</b></label><br>
			<s:select id="co_applicant_relationship2" name="quote.loanQuoteCoapplicantSecondRelationshipId" list="%{beanList.relationships}" 
				value="%{quote.loanQuoteCoapplicantSecondRelationshipId}"	headerKey="0" headerValue="Select Relationship" />
		</div>
		<div class="div-left3" id="divco_date_of_birth2">
			<label class="clear">Date of Birth  <b class="req">*</b></label>
			<s:textfield name="quote.loanQuoteCoapplicantSecondDateOfBirth" id="co_date_of_birth2" placeholder="dd-mm-yyyy"  cssClass=" " maxlength="10"   
				value="%{quote.loanQuoteCoapplicantSecondDateOfBirth}" size="10" /> 
		</div>
		<div class="div-left3 " id="divco_co_gender2">
			<label>Gender<b class="req">*</b></label><br>
			<s:select name="quote.loanQuoteCoapplicantSecondGender" id="co_gender2" value="%{quote.loanQuoteCoapplicantSecondGender}" list="%{#genderList!=null?#genderList:''}" headerKey="0" headerValue="Select gender" cssClass="" />
		</div>
		<div class="div-left3" id="divco_residentType2">
			<label class="">Resident type  <b class="req">*</b></label><br> 
			<s:select id="co_residentType2" name="quote.loanQuoteCoapplicantSecondResidentTypeId" list="%{beanList.residentTypesCoApplicant}" 
				value="%{quote.loanQuoteCoapplicantSecondResidentTypeId}" headerKey="0" headerValue="Select Resident Type" />
		</div>
		<s:if test="%{quote.loanQuoteCoapplicantSecondResidentTypeId == 2}">
			<div id="co_residentType1Data">
				<div class="div-left3" id="divco_countryRegion2">
					<label class="">Select your Country/Region<b class="req">*</b></label><br>
					<s:select name="quote.loanQuoteCoapplicantSecondCountryId" id="co_countryRegion2" autocomplete="off"  list="%{#countries}"
					 value="%{quote.loanQuoteCoapplicantSecondCountryId}" headerKey="0" headerValue="Select Country" />
				</div>
				<div class="div-left3" id="divco_workExperience2">
					<label class="">Work experience (years)<b class="req">*</b></label><br>
					<s:select name="quote.loanQuoteCoapplicantSecondWorkExperience" id="co_workExperience2" autocomplete="off" list="#{1:'>1',2:'1-2',3:'>2'}"
				 		value="%{quote.loanQuoteCoapplicantSecondWorkExperience}" headerKey="0" headerValue="Select Work experience"/>
					
				</div>
			</div>
		</s:if>
		<div class="div-left3" id="divco_employmentType2">
			<label class="">Type of employment  <b class="req">*</b></label><br>
			<s:select name="quote.loanQuoteCoapplicantSecondEmploymentTypeId" id="co_employmentType2" autocomplete="off" list="%{beanList.employementTypes}" 
				value="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId}" headerKey="0" headerValue="Select Employment type" />
		</div>
		<div id="EMPTYPE2">
			<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==1}">
				<div class="div-left3" id="divco_netMonthlySalary2">
					<label class="">Net monthly Salary(<span class="wr">Rs</span>)<b class="req">*</b></label><br> 
					<s:textfield name="quote.loanQuoteCoapplicantSecondMonthlySalary" id="co_netMonthlySalary2" value="%{quote.loanQuoteCoapplicantSecondMonthlySalary>0?quote.loanQuoteCoapplicantSecondMonthlySalary:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<div class="div-left3" id="divco_variableMonthlyPay2">
					<label class="">Variable monthly pay(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondVariableMonthPayon" id="co_variableMonthlyPay2" value="%{quote.loanQuoteCoapplicantSecondVariableMonthPayon>0?quote.loanQuoteCoapplicantSecondVariableMonthPayon:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>	
				</div>
				<%-- <div class="div-left3" id="divco_expectedRentalIncome2">
					<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondExpectedRentalIncome" id="co_expectedRentalIncome2" value="%{quote.loanQuoteCoapplicantSecondExpectedRentalIncome>0?quote.loanQuoteCoapplicantSecondExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
				</div> --%>
				<div class="div-left3" id="divco_otherIncome2">
					<label class="">Other monthly income(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondOtherIncome" id="co_otherIncome2" value="%{quote.loanQuoteCoapplicantSecondOtherIncome>0?quote.loanQuoteCoapplicantSecondOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<div class="div-left3" id="divco_emiPayPerMonth2">
					<label class="">Existing EMIs if any(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<div class="div-left3" id="divco_retirementAgeApplicant2">
					<label class="">Retirement Age<b class="req">*</b></label><br>
					<s:select id="co_retirementAgeApplicant2" name="quote.loanQuoteCoapplicantSecondRetirementAge" list="%{beanList.retirementAge}" 
						value="%{quote.loanQuoteCoapplicantSecondRetirementAge}" headerKey="0" headerValue="Select Retirement Age" />
				</div>
				<%-- <div class="div-left3" id="">
					<label class="">Have a corporate salary package with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/>?<b class="req">*</b></label><br>
					<s:if test="%{quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi!=null && quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi=='Y'}">
						<input type="radio" name="quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi2Yes" checked="checked" value="Y"><span style="float:left;"> Yes&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<input type="radio" name="quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi2No" value="N"><span style="float:left;"> No</span>
					</s:if>
					<s:else>
						<input type="radio" name="quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi2Yes" value="Y"><span style="float:left;"> Yes&nbsp;&nbsp;&nbsp;&nbsp;</span>
						<input type="radio" name="quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi" id="co_salaryAccountWithSbi2No" checked="checked" value="N"><span style="float:left;"> No</span>
					</s:else>
				</div>
				<s:if test="%{quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi!=null && quote.loanQuoteCoapplicantSecondCoSalaryAccountWithSbi=='Y'}">
					<div class="div-left3" id="">
						<label class="">Employer Name<b class="req">*</b></label><br>
						<s:textfield name="quote.loanQuoteCoapplicantSecondCoEmplyerName" id="co_employerName2" value="%{quote.loanQuoteCoapplicantSecondCoEmplyerName}" autocomplete="off" maxlength="105"/>
							<script type="text/javascript">auto_suggestion_employer('co_employerName1');</script>
						<script type="text/javascript">auto_suggestion_employer('co_employerName2');</script>
					</div>
				</s:if> --%>
			</s:if>
			<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==2 || quote.loanQuoteCoapplicantSecondEmploymentTypeId==3}">
				<div class="div-left3" id="divco_profitAfterTax2">
					<label class="">Profit After Tax(<span class="wr">Rs</span>)<b class="req">*</b></label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondProfitAfterTax" id="co_profitAfterTax2" value="%{quote.loanQuoteCoapplicantSecondProfitAfterTax>0?quote.loanQuoteCoapplicantSecondProfitAfterTax:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<div class="div-left3" id="divco_depreciation2">
					<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBBJ}">
						<label class="">Depreciation last 3 years average (<span class="wr">Rs</span>)/Depreciation last year (<span class="wr">Rs</span>)</label><br>
					</s:if>
					<s:else>
						<label class="">Depreciation last 3 years average (<span class="wr">Rs</span>)</label><br>
					</s:else>
					<s:textfield name="quote.loanQuoteCoapplicantSecondDepreciatiation" id="co_depreciation2" value="%{quote.loanQuoteCoapplicantSecondDepreciatiation>0?quote.loanQuoteCoapplicantSecondDepreciatiation:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<%-- <div class="div-left3" id="divco_expectedRentalIncome2">
					<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondExpectedRentalIncome" id="co_expectedRentalIncome2" value="%{quote.loanQuoteCoapplicantSecondExpectedRentalIncome>0?quote.loanQuoteCoapplicantSecondExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
				</div> --%>
				<div class="div-left3" id="divco_preEMIs2">
					<label class="">Existing EMIs if any (<span class="wr">Rs</span>) </label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
			</s:if>
			<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==4}">
				<div class="div-left3" id="divco_netAnnualIncome2">
					<label class="">Net Annual Income (<span class="wr">Rs</span>)<b class="req">*</b></label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondNetAnnualIncome" id="co_netAnnualIncome2" value="%{quote.loanQuoteCoapplicantSecondNetAnnualIncome>0?quote.loanQuoteCoapplicantSecondNetAnnualIncome:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<%-- <div class="div-left3" id="divco_expectedRentalIncome2">
					<label class="">Expected monthly rental Income (<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondExpectedRentalIncome" id="co_expectedRentalIncome2" value="%{quote.loanQuoteCoapplicantSecondExpectedRentalIncome>0?quote.loanQuoteCoapplicantSecondExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
				</div> --%>
				<div class="div-left3" id="divco_otherIncome2">
					<label class="">Other monthly income(<span class="wr">Rs</span>) </label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondOtherIncome" id="co_otherIncome2" value="%{quote.loanQuoteCoapplicantSecondOtherIncome>0?quote.loanQuoteCoapplicantSecondOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
			</s:if>
			<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==5}">
				<div class="div-left3" id="divco_netMonthlyPension2">
					<label class="">Net Monthly pension Income (<span class="wr">Rs</span>)<b class="req">*</b></label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondNetMonthlyPension" id="co_netMonthlyPension2" value="%{quote.loanQuoteCoapplicantSecondNetMonthlyPension>0?quote.loanQuoteCoapplicantSecondNetMonthlyPension:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<%-- <div class="div-left3" id="divco_expectedRentalIncome2">
					<label class="">Expected monthly rental Income(<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondExpectedRentalIncome" id="co_expectedRentalIncome2" value="%{quote.loanQuoteCoapplicantSecondExpectedRentalIncome>0?quote.loanQuoteCoapplicantSecondExpectedRentalIncome:''}"  maxlength="9"  placeholder="Rs" />
				</div> --%>
				<div class="div-left3" id="divco_otherIncome2">
					<label class="">Other monthly income (<span class="wr">Rs</span>)</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondOtherIncome" id="co_otherIncome2" value="%{quote.loanQuoteCoapplicantSecondOtherIncome>0?quote.loanQuoteCoapplicantSecondOtherIncome:''}"  maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
				<div class="div-left3" id="divco_preEMIs2">
					<label class="">Existing EMIs if any(<span class="wr">Rs</span>)
					</label><br>
					<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_preEMIs2" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" maxlength="9"  placeholder="Rs"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
				</div>
			</s:if>
			<s:if test="%{quote.loanQuoteCoapplicantSecondEmploymentTypeId==6 ||quote.loanQuoteCoapplicantSecondEmploymentTypeId==7}">
				<div id="divco_incomeFromRegularSource2" class="div-left3"><label class="">Income From Regular Source  (<span class="wr">Rs</span>)<b class="req">*</b></label><br>
				<s:textfield name="quote.loanQuoteCoapplicantSecondPreEMIs" id="co_incomeFromRegularSource2" autocomplete="off" class="w167"  maxlength="9"  placeholder="Rs"  value="%{quote.loanQuoteCoapplicantSecondIncomeFromOtherSource>0?quote.loanQuoteCoapplicantSecondIncomeFromOtherSource:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/></div>
				<div id="divco_emiPayPerMonth2" class="div-left3"><label class="">Existing EMIs, if any  (<span class="wr">Rs</span>)</label><br>
				<s:textfield name="co_preEMIs2" id="co_preEMIs2" autocomplete="off" class="w167" placeholder="Rs" maxlength="9" value="%{quote.loanQuoteCoapplicantSecondPreEMIs>0?quote.loanQuoteCoapplicantSecondPreEMIs:''}" onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/></div>
			</s:if>
		</div>
	</s:if>
	<s:else>
		<div id="coAppImageNextData" class="field-row3"></div>
	</s:else>
<script type="text/javascript">
jQuery(document).ready(function(){
	jQuery("#co_date_of_birth1, #co_date_of_birth2").datepick({
		dateFormat: 'dd-mm-yyyy',
		changeMonth: true, 
		changeYear: true,
		yearRange: max_range+':'+min_range, 
		minDate: new Date(min_range, current_month, current_day),
		maxDate: new Date(max_range, current_month, current_day),
		setDate: new Date(avg_range, 1, 1)
	});
	apply_currency_form(document.coApplicantQuoteForm);
});
</script>
