<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
<form name="autoloancriteriaform" id="autoloancriteriaform"
	method="post" action="javascript:void(0);"
	enctype="application/x-www-form-urlencoded"
	onclick="<s:property value="%{(@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local')?'':'ga('send', {'hitType': 'event', 'eventCategory': 'CLAppSubmit', 'eventAction': 'CLAppSubmit', 'eventLabel': 'CLAppSubmit' });'}"/>">
	<h3>
		<s:property
			value="%{quote.loanQuoteResidentTypeId==1?'PERSONAL DETAILS':'ADDRESS IN INDIA'}" />
	</h3>
	<ul class="form-section">
		<li><label>First name<b class="req">*</b></label> <%-- <s:textfield
				name="appForm.appFirstName" id="appFirstName"
				value="%{appForm.appFirstName==null?#session.applicantName:appForm.appFirstName}"
				onfocus="%{#toolTip}" maxlength="20" onblur="ChangeCase(this);"
				cssClass="form-control %{(isDsrPage=='true' && #session.applicantName!=null ? ' disabledFields' : 'disabledFields')}"
				disabled="%{(isDsrPage=='true' && #session.applicantName!=null ? 'true' : 'false')}" /> --%>
				  <s:textfield name="appForm.appFirstName"  id="appFirstName" value="%{appForm.appFirstName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);"/>
			<s:if test="%{isDsrPage=='false'}">
				<div class="custom-tooltip">Enter first name</div>
			</s:if></li>
		<li><label>Middle name<span>&nbsp;</span></label> <s:textfield
				name="appForm.appMiddleName" id="appMiddleName"
				value="%{appForm.appMiddleName}" cssClass="form-control"
				maxlength="20" onblur="ChangeCase(this);" /></li>
		<li><label>Last name<b class="req">*</b></label> <s:textfield
				name="appForm.appLastName" id="appLastName"
				value="%{appForm.appLastName}" cssClass="form-control"
				maxlength="20" onfocus="%{#toolTip}" onblur="ChangeCase(this);" />
			<s:if test="%{isDsrPage=='false'}">
				<div class="custom-tooltip">Enter last name</div>
			</s:if></li>
		<li><label>Gender<b class="req">*</b></label>
			<div class="flat-field">
				<s:select list="%{#genderList}" name="appForm.appGender"
					cssClass="form-control" id="appGender" value="%{appForm.appGender}"
					headerKey="0" headerValue="Select gender" disabled="true"
					onfocus="customOnFocus(this);" />
			</div></li>
		<li><label>Date of birth<b class="req">*</b></label> <s:textfield
				name="appForm.appDob" id="third_date_of_birth"
				cssClass="form-control dob-cal" value="%{appForm.appDob}"
				maxlength="10" disabled="true" /></li>
		<li><label>Address line 1<b class="req">*</b></label> <s:textfield
				name="appForm.appAddress1" id="appAddress1"
				value="%{appForm.appAddress1}"
				maxlength="40" onfocus="%{#toolTip}"
				cssClass="form-control" onblur="ChangeCase(this);"
				onkeydown="return M.isAddressLine(event);" /> <s:if
				test="%{isDsrPage=='false'}">
				<div class="custom-tooltip">Enter your current address</div>
			</s:if></li>
		<li><label>Address line 2<b class="req">*</b></label> <s:textfield
				name="appForm.appAddress2" id="appAddress2"
				value="%{appForm.appAddress2}"
				maxlength="40" cssClass="form-control"
				onblur="ChangeCase(this);"
				onkeydown="return M.isAddressLine(event);" /></li>
		<li><label>Landmark<span>&nbsp;</span></label> <s:textfield
				name="appForm.appAddressLandmark" id="appAddressLandmark"
				value="%{appForm.appAddressLandmark}" maxlength="30"
				cssClass="form-control" onblur="ChangeCase(this);"
				onkeydown="return M.isAddressLine(event);" /></li>
		<s:include value="/appNew/loan/autoloan/includes/AutoAddress.jsp"></s:include>

		<s:hidden name="employmentTypeIdHidden" id="employmentTypeIdHidden"
			value="%{appForm.appEmploymentType}"></s:hidden>
		<li><label>Pincode<b class="req">*</b></label> <s:textfield
				value="%{appForm.appPincode}" id="appPincode"
				cssClass="disabledFields form-control" name="appForm.appPincode"
				placeholder="Pincode" autocomplete="off" maxlength="6"
				onkeydown="return M.digit(event);" /></li>
		<li><label>Residence type<b class="req">*</b></label>
			<div class="flat-field">
				<s:select
					list="%{beanList.residenceTypes==null?'':beanList.residenceTypes}"
					name="appForm.appResidenceType" cssClass="form-select"
					id="appResidenceType" value="%{appForm.appResidenceType}"
					headerKey="0" headerValue="Select residence type"
					onfocus="customOnFocus(this);" />
			</div></li>
		<li><label>Highest educational qualification<b
				class="req">*</b></label>
			<div class="flat-field">
				<s:select
					list="#{'0':'Select qualification',6:'Doctorate',5:'Post graduate',4:'Degree',3:'Diploma',2:'HSC',1:'Below HSC'}"
					cssClass="form-select" id="appHighestQualification"
					name="appForm.appHighestQualification"
					value="%{appForm.appHighestQualification}"
					onfocus="customOnFocus(this);" />
			</div></li>
		<li><label>Relationship with bank<b class="req">*</b></label>
			<div class="flat-field">
				<s:if
					test="%{beanList.relationshipWithBank!=null && beanList.relationshipWithBank.size() == 1}">
					<s:select
						list="%{beanList.relationshipWithBank!=null?beanList.relationshipWithBank:''}"
						name="appForm.appRelationshipWithBank"
						value="%{appForm.appRelationshipWithBank}"
						id="appRelationshipWithBank" headerKey="0"
						cssClass="form-select disabledFields" readonly='true'
						disabled="true" />
				</s:if>
				<%--  <s:if test="%{ beanList.relationshipWithBank!=null && beanList.relationshipWithBank.size() == 1 && appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS}" >
               		<s:select list="%{beanList.relationshipWithBank!=null?beanList.relationshipWithBank:''}" name="appForm.appRelationshipWithBank" value="%{appForm.appRelationshipWithBank}" 
              		id="appRelationshipWithBank" headerKey="0"  cssClass="form-control disabledFields" readonly='true' disabled="true" />
				</s:if>  --%>
				<s:else>
					<s:select
						list="%{beanList.relationshipWithBank!=null?beanList.relationshipWithBank:''}"
						name="appForm.appRelationshipWithBank"
						value="%{appForm.appRelationshipWithBank}"
						id="appRelationshipWithBank" headerKey="0"
						headerValue="Select Relationship" cssClass="form-select" />
				</s:else>
			</div></li>

		<s:if test="%{appForm.appEmploymentType==13}">
			<li><label>Pension account number<b class="req">*</b></label> <s:textfield
					name="appForm.appSalaryAccNo" cssClass="form-control"
					id="appSalaryAccNo" value="%{appForm.appSalaryAccNo}"
					maxlength="11" onkeydown="return M.digit(event);" /></li>
		</s:if>
		<s:if test="%{quote.loanQuoteLoanPurposeId!=7}">
			<li><label>Dealer name<span>&nbsp;</span></label> <s:if
					test="%{#session.lmsIntermediaryRelation!=null && !#session.lmsIntermediaryRelation.equalsIgnoreCase('null') && isDsrPage=='true'}">
					<s:textfield name="appForm.appLoanDealerName"
						id="appLoanDealerName" value="%{appForm.appLoanDealerName}"
						cssClass="disabledFields  form-select" disabled="true" />
				</s:if> <s:else>
					<s:textfield name="appForm.appLoanDealerName"
						cssClass="form-control" id="appLoanDealerName"
						value="%{appForm.appLoanDealerName}"
						placeholder="Type slowly for dealer name"
						onkeydown="return M.isChars(event);" />
				</s:else> <span id="autofillLoader" class=""></span></li>
		</s:if>
	</ul>

	<s:include value="/appNew/loan/homeloan/HomeNRIAddress.jsp"></s:include>
	<div class="clearfix"></div>
	<s:if test="%{quote.loanQuoteResidentTypeId==1}">
		<h3>
			<span class="flt">PERMANENT ADDRESS</span>
			<div class="prm-chk-div">
				<input type="checkbox" name="appForm.clonePermanentAddress"
					id="clonePermanentAddress"
					value="<s:property value="%{appForm.clonePermanentAddress==null?'false':'true'}"/>"
					<s:property value="%{appForm.clonePermanentAddress==true?'checked=\"checked\"':''}"/>
					class="blue-css-checkbox"> <label class="label-content"
					for="clonePermanentAddress">Same as current address</label>
			</div>
		</h3>

		<div class="clearfix"></div>
		<ul class="form-section">
			<li><label>Address line 1<b class="req">*</b></label> <s:textfield
					name="appForm.appPermanentAddress1" id="appPermanentAddress1"
					value="%{appForm.appPermanentAddress1}"
					maxlength="40"
					cssClass="form-control %{appForm.clonePermanentAddress==true?'disabledFields ':'disabledFields'}"
					disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>
			<li><label>Address line 2</label> <s:textfield
					name="appForm.appPermanentAddress2" id="appPermanentAddress2"
					value="%{appForm.appPermanentAddress2}"
					maxlength="40"
					cssClass="form-control %{appForm.clonePermanentAddress==true?'form-control':'form-control'}"
					disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>
			<li><label>Landmark</label> <s:textfield
					name="appForm.appPermanentAddressLandMark"
					id="appPermanentAddressLandMark"
					value="%{appForm.appPermanentAddressLandMark}" maxlength="20"
					cssClass="form-control disabledFields"
					disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>
			<li id="APPPERMANENTLOCATIONSTATE">
				<div id="permanentStates">
					<label>State<b class="req">*</b></label>
					<div class="flat-field">
						<s:select name="appForm.appPermanentStateId" list="%{#states}"
							id="appPermanentStateId" value="%{appForm.appPermanentStateId}"
							headerKey="0" headerValue="Select state"
							cssClass="form-control disabledFields"
							disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
							onfocus="customOnFocus(this);" />
					</div>
				</div>
			</li>
			<li id="APPPERMANENTLOCATIONCITY"><label>City<b
					class="req">*</b></label> <s:if test="%{appForm.appPermanentCityId!=null}">
					<div class="flat-field">
						<s:select
							list="%{beanList.citiesoptgrp1Permanent==null?'':beanList.citiesoptgrp1Permanent}"
							name="appForm.appPermanentCityId" id="appPermanentCityId"
							value="%{appForm.appPermanentCityId}" headerKey="0"
							headerValue="Select city" cssClass="form-select disabledFields"
							disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
							onfocus="customOnFocus(this);">
						</s:select>
					</div>
				</s:if> <s:else>
					<div class="flat-field">
						<s:select cssClass="disabledFields form-select" list="#{''}"
							name="appForm.appPermanentCityId" id="appPermanentCityId"
							disabled="true" value="%{appForm.appPermanentCityId}"
							headerKey="0" headerValue="Select city"
							onfocus="customOnFocus(this);">
						</s:select>
					</div>
				</s:else></li>
			<div id="APPPERMANENTLOCATIONDISTRICT">
				<s:if test="%{appForm.appPermanentDistrictId!=null}">
					<li><label>District<b class="req">&nbsp;*</b></label>
						<div class="flat-field">
							<s:select
								list="%{beanList.districtsPermanent==null?'':beanList.districtsPermanent}"
								name="appForm.appPermanentDistrictId"
								id="appPermanentDistrictId"
								value="%{appForm.appPermanentDistrictId}" headerKey="0"
								headerValue="Select district"
								cssClass="%{appForm.clonePermanentAddress==true?'disabledFields form-select':'disabledFields form-control'}"
								disabled="%{appForm.clonePermanentAddress==true?'true':'false'}">
							</s:select>
						</div></li>
				</s:if>
			</div>
			<li><label>Pincode<b class="req">*</b></label> <s:textfield
					name="appForm.appPermanentPincode" id="appPermanentPincode"
					value="%{appForm.appPermanentPincode}" placeholder="Pincode"
					autocomplete="off" maxlength="6" onkeydown="return M.digit(event);"
					cssClass="form-control disabledFields"
					disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" />
			</li>
		</ul>
	</s:if>

	<div class="clearfix"></div>
	<h3>NET WORTH DETAILS</h3>
	<ul class="form-section">
		<li><label>Immovable property (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appImmovableProperty"
				id="appImmovableProperty"
				value="%{appForm.appImmovableProperty>0?appForm.appImmovableProperty:''}"
				onkeydown="return M.digit(event);" maxlength="9"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"
				cssClass="form-control" placeholder="Rs"></s:textfield></li>
		<li><label>Bank deposits (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appBankDeposit" id="appBankDeposit"
				value="%{appForm.appBankDeposit>0?appForm.appBankDeposit:''}"
				cssClass="form-control" placeholder="Rs"
				onkeydown="return M.digit(event);" maxlength="9"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"></s:textfield></li>
		<li><label>NSCs (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appNsc" id="appNsc"
				value="%{appForm.appNsc>0?appForm.appNsc:''}"
				onkeydown="return M.digit(event);" maxlength="9"
				cssClass="form-control" placeholder="Rs"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"></s:textfield></li>
		<li><label>PF/PPF (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appPFOrPPF" id="appPFOrPPF"
				value="%{appForm.appPFOrPPF>0?appForm.appPFOrPPF:''}"
				onkeydown="return M.digit(event);" cssClass="form-control"
				maxlength="9" placeholder="Rs"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"></s:textfield></li>
		<li><label>Gold ornaments (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appGoldOrnaments" id="appGoldOrnaments"
				value="%{appForm.appGoldOrnaments>0?appForm.appGoldOrnaments:''}"
				cssClass="form-control" maxlength="9" placeholder="Rs"
				onkeydown="return M.digit(event);"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"></s:textfield></li>
		<li><label>Mutual funds/other assets (<span
				class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appMutualAsset" id="appMuttaulAsset"
				value="%{appForm.appMutualAsset>0?appForm.appMutualAsset:''}"
				cssClass="form-control" maxlength="9" placeholder="Rs"
				onkeydown="return M.digit(event);"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);"></s:textfield></li>
		<li><label>Existing loans (<span class="font-rupee">`</span>)
		</label> <s:textfield name="appForm.appExistingTotalLoanAmount"
				id="appExistingTotalLoanAmount"
				value="%{appForm.appExistingTotalLoanAmount>0?appForm.appExistingTotalLoanAmount:''}"
				cssClass="form-control" placeholder="Rs"
				onkeydown="return M.digit(event);"
				onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';"
				onfocus="this.value=M.unformatMoney(this.value);" maxlength="9" /></li>
	</ul>

	<div class="clearfix"></div>
	<s:if
		test="%{ appForm.appEmploymentType==9 || appForm.appEmploymentType==10 || appForm.appEmploymentType==11}">
		<h3>EMPLOYMENT DETAILS</h3>
		<ul class="form-section">
			<li><s:if test="%{appForm.appEmploymentType==9}">
					<label>Employer name<b class="req">*</b></label>
				</s:if> <s:elseif
					test="%{appForm.appEmploymentType==10 ||appForm.appEmploymentType==11}">
					<label>Business name<b class="req">*</b></label>
				</s:elseif> <s:textfield name="appForm.appLoanEmployerName"
					id="appLoanEmployerName" value="%{appForm.appLoanEmployerName}"
					placeholder="Type slowly for business name"
					cssClass="form-control  disabledFields" /></li>
			<li><s:if test="%{appForm.appEmploymentType==9}">
					<label>Joining date<b class="req">*</b></label>
				</s:if> <s:else>
					<label>Start date of business<b class="req">*</b></label>
				</s:else>
				<div class="flat-field1">
					<s:select
						list="%{beanList.joiningyears==null?'':beanList.joiningyears}"
						name="appForm.appCompanyJoiningYear" id="appCompanyJoiningYear"
						value="%{appForm.appCompanyJoiningYear}"
						cssClass="form-select date_dropdown_year" headerKey="0"
						headerValue="Select year" onfocus="customOnFocus(this);" />
				</div>
				<div class="flat-field1 mr-r9">
					<s:if
						test="%{appForm.appCompanyJoiningMonth>0 && appForm.appCompanyJoiningMonth<13}">
						<s:select list="%{#months}" name="appForm.appCompanyJoiningMonth"
							id="appCompanyJoiningMonth"
							value="%{appForm.appCompanyJoiningMonth}"
							cssClass="form-select disabledFields date_dropdown_month"
							headerKey="13" headerValue="Select Month"
							onfocus="customOnFocus(this);" />
					</s:if>
					<s:else>
						<s:select list="%{#months}" name="appForm.appCompanyJoiningMonth"
							id="appCompanyJoiningMonth"
							value="%{appForm.appCompanyJoiningMonth}"
							cssClass="form-select disabledFields date_dropdown_month "
							headerKey="13" headerValue="Select Month" disabled="true"
							onfocus="customOnFocus(this);" />
					</s:else>
				</div></li>
			<li><label>Total Work Experience<b class="req">*</b></label>
				<div class="flat-field1">
					<s:select
						list="#{1:'Less than 1', 2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'More than 10 years'}"
						cssClass="form-select gender" id="appWorkExperienceYear"
						name="appForm.appWorkExperienceYear"
						value="%{appForm.appWorkExperienceYear}" headerKey=""
						headerValue="Select year" onfocus="customOnFocus(this);" />
				</div>
				<div class="flat-field1 mr-r9">
					<s:if
						test="%{appForm.appWorkExperienceMonth>0 && appForm.appWorkExperienceMonth<13}">
						<s:select
							list="#{1:'0',2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'11'}"
							cssClass="form-select disabledFields gender"
							id="appWorkExperienceMonth" name="appForm.appWorkExperienceMonth"
							value="%{appForm.appWorkExperienceMonth}" headerKey="13"
							headerValue="Select month" onfocus="customOnFocus(this);" />
					</s:if>
					<s:else>
						<s:select
							list="#{1:'0',2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'11'}"
							cssClass="form-select disabledFields gender "
							id="appWorkExperienceMonth" name="appForm.appWorkExperienceMonth"
							value="%{appForm.appWorkExperienceMonth}" headerKey="13"
							headerValue="Select month" disabled="true"
							onfocus="customOnFocus(this);" />
					</s:else>
				</div></li>
			<s:if
				test="%{quote.loanQuoteResidentTypeId==null || quote.loanQuoteResidentTypeId==1}">
				<li><label>Address line 1<b class="req">*</b></label> <s:textfield
						name="appForm.appOfficeAddress1" id="appOfficeAddress1"
						value="%{appForm.appOfficeAddress1}" onfocus="%{#toolTip}"
						cssClass="form-control" maxlength="40"
						onkeydown="return M.isAddressLine(event);" /> <s:if
						test="%{isDsrPage=='false'}">
						<div class="custom-tooltip">Enter your current office
							address</div>
					</s:if></li>
				<li><label>Address line 2</label> <s:textfield
						name="appForm.appOfficeAddress2" id="appOfficeAddress2"
						value="%{appForm.appOfficeAddress2}" cssClass="form-control"
						maxlength="40" onkeydown="return M.isAddressLine(event);" /></li>
				<li id="APPOFFICELOCATIONSTATE"><label>State<b
						class="req">*</b></label>
					<div class="flat-field">
						<s:select name="appForm.appOfficeStateId" id="appOfficeStateId"
							cssClass="form-select" autocomplete="off"
							list="%{beanList.states==null?'':beanList.states}"
							value="%{appForm.appOfficeStateId}" headerKey="0"
							headerValue="Select state" onfocus="customOnFocus(this);" />
					</div></li>
				<li id="APPOFFICELOCATIONCITY"><label>City<b
						class="req">*</b></label> <s:if
						test="%{appForm.appOfficeCityId!=null && beanList.citiesoptgrp1Office!=null}">
						<div class="flat-field">
							<s:select
								list="%{beanList.citiesoptgrp1Office==null?'':beanList.citiesoptgrp1Office}"
								id="appOfficeCityId" name="appForm.appOfficeCityId"
								value="%{appForm.appOfficeCityId}" headerKey="0"
								headerValue="Select city"
								cssClass="form-select  disabledFields" disabled="true"
								onfocus="customOnFocus(this);">
							</s:select>
						</div>
					</s:if> <s:else>
						<div class="flat-field">
							<s:select list="#{''}" id="appOfficeCityId"
								name="appForm.appOfficeCityId"
								value="%{appForm.appOfficeCityId}" headerKey="0"
								headerValue="Select city"
								cssClass="form-select  disabledFields" disabled="true"
								onfocus="customOnFocus(this);">
							</s:select>
						</div>
					</s:else></li>
				<div id="APPOFFICELOCATIONDISTRICT">
					<s:if
						test="%{appForm.appOfficeCityId!=null && appForm.appOfficeCityId == 9999999}">
						<s:if
							test="%{appForm.appOfficeDistrictId!=null && appForm.appOfficeDistrictId >0}">
							<li id="divLocality"><label class="">District<b
									class="req">*</b></label>
								<div class="flat-field">
									<s:select name="appForm.appOfficeDistrictId"
										id="appOfficeDistrictId" autocomplete="off"
										list="%{beanList.districtsOffice==null?'':beanList.districtsOffice}"
										value="%{appForm.appOfficeDistrictId}" headerKey="0"
										headerValue="Select district"
										cssClass="form-select  disabledFields" disabled="true"
										onfocus="customOnFocus(this);" />
								</div></li>
						</s:if>
					</s:if>
				</div>
				<li><label>Pincode<b class="req">*</b></label> <s:textfield
						cssClass="form-control" name="appForm.appOfficePincode"
						id="appOfficePincode" value='%{appForm.appOfficePincode}'
						placeholder="Pincode" autocomplete="off" maxlength="6"
						onkeydown="return M.digit(event);" /></li>
			</s:if>
			<s:else>
				<s:include value="/appNew/loan/homeloan/HomeEMPNRIAddress.jsp"></s:include>
			</s:else>
			<li><label>Phone no. with STD code <b class="req"></b></label> <s:textfield
					name="appForm.appOfficePhoneStdCode" id="appOfficePhoneStdCode"
					value="%{appForm.appOfficePhoneStdCode}"
					cssClass="s-input flt form-control" autocomplete="off"
					minlength="1" maxlength="6" onkeydown="return M.digit(event);"
					placeholder="STD code" /> <s:textfield
					name="appForm.appOfficePhoneNumber" id="appOfficePhoneNumber"
					value="%{appForm.appOfficePhoneNumber}" autocomplete="off"
					maxlength="8" placeholder="Landline no."
					cssClass="b-input ml10 flt form-control"
					onkeydown="return M.digit(event);" onfocus="%{#toolTip}" /> <s:if
					test="%{isDsrPage=='false'}">
					<div class="custom-tooltip">Enter phone no with STD code</div>
				</s:if></li>

             
              
               <s:hidden id="first_mob"	value="%{#session.mobile}" /> 
               
			<div id="alternateMobile" class="alternateMobile"
				name="alternateMobile">
				<li><label>Alternate Mobile Number <b class="req"></b></label>


					<s:textfield name="appForm.appAltISDCode" id="appAltISDCode"
						value="%{appForm.appAltISDCode}"
						cssClass="s-input flt form-control" autocomplete="off"
						minlength="1" maxlength="3" onkeyup="return showlink();"
						onkeydown="return M.digit(event);" placeholder="ISD code" /> <s:textfield
						name="appForm.appAlternateMobileNumber" id="alternateMobileNumber"
						value="%{appForm.appAlternateMobileNumber}" autocomplete="off"
						maxlength="15" placeholder="Alternate Mobile Number"
						onkeyup="return showlink();"
						onkeydown="return M.digit(event);Noncbsvalidate();"
						
						
						cssClass="b-input ml10 flt form-control" disabled="" />
					<div class="custom-tooltip">Note:This additional mobile no
						will not be updated in CBS. Please input correct alternate mobile
						number as you are going to receive OTP to validate this mobile
						number</div> <br> <br> <br> <br> </br> <span
					id="inalterror" style="color: #e96e57; font-size: 11px"></span></li>
			</div>
			<li></li>


			<li><br>
				<div id="alternateUrl" class="alternateUrl" name="alternateUrl"
					style="display: none;">
					<%--    <b> <a href="javascript:void(0);" onclick="javascript:openPopups('OTP1','1');  return hideAlternateDiv(); ">Click here to verify Otp</a></b>--%>
					<b> <a href="javascript:void(0);" onclick="validatISD();">Click	here to verify OTP</a></b>
					<s:hidden id="alternateUrlVal" name="alternateUrlVal" value="0" />
				</div>
			</li>
 






		</ul>
	</s:if>

	<div class="clearfix"></div>
	<h3>IDENTITY DETAILS</h3>
	<ul class="form-section" id="5">
		<li><label>Do you have Aadhaar Number?</label>
			<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="appForm.appHaveAadhaarNumber"
						id="haveAadhaarYes" value="1" checked="checked"> <label
						for="haveAadhaarYes"> Yes </label>
				</div>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="appForm.appHaveAadhaarNumber"
						id="haveAadhaarNo" value="2"> <label for="haveAadhaarNo">
						No </label>
				</div>
			</div>
			<div id="aadhaarConfirmation" class="adh-msg"></div></li>
		<li><label>PAN<b class="req">*</b><font
				style="font-size: 12px;"> (<a
					href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN"
					target="_blank">Verify Your PAN</a>)
			</font></label> <s:textfield placeholder="Enter PAN" name="appForm.appPanCardNo"
				id="appPanCardNo" value="%{appForm.appPanCardNo}"
				cssClass="form-control" maxlength="10"
				disabled="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'true':'false'}" />
		</li>
		<li><label>Other identity proof<span>&nbsp;</span></label>
			<div class="flat-field">
				<s:select
					list="#{'0':'Select id proof','165':'Voter id','166':'Passport','167':'Driving license','160':'Ration card'}"
					name="appForm.appOtherId" id="appIdProof" cssClass="form-select"
					value="%{appForm.appOtherId}" onfocus="customOnFocus(this);" />
			</div></li>
		<div id="OTHERIDNUMBER">
			<s:if
				test="%{appForm.appOtherIdNumber!= null && appForm.appOtherIdNumber >0}">
				<li id="divAppOtherIdNumber"><label>Other identity no<b
						class="req">&nbsp;* </b></label>
						<s:textfield placeholder="Enter OtherId number" name="appForm.appOtherIdNumber"  id="appOtherIdNumber" cssClass="form-control"  value="%{appForm.appOtherIdNumber}"   maxlength="%{(appForm.appOtherId != null && appForm.appOtherId >0 && appForm.appOtherId == 166)?'10':((appForm.appOtherId != null && appForm.appOtherId >0 && (appForm.appOtherId == 165 || appForm.appOtherId == 167))?'20':'12' ) }" />
				</li>
			</s:if>
		</div>
	</ul>

	<div class="clearfix"></div>
	<s:if test="%{appForm.appCoapplicantTypeId_1>0}">
		<h3>
			<s:if test="%{quote.loanQuoteResidentTypeId==2}">
				<span class="flt"> GUARANTOR DETAILS</span>
			</s:if>
			<s:else>
				<span class="flt">CO-APPLICANT DETAILS</span>
			</s:else>
			<div class="prm-chk-div">
				<input type="checkbox" name="appForm.cloneCoapplicantAddress1"
					id="cloneCoapplicant1Address"
					value="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
					class="blue-css-checkbox"> <label class="label-content"
					for="cloneCoapplicant1Address">Same as current address</label>
			</div>
		</h3>

		<div class="clearfix"></div>
		<ul class="form-section">
			<li><label>First name<b class="req">*</b></label> <s:textfield
					name="appForm.appCoapplicantFirstName_1"
					id="appCoapplicantFirstName1"
					value="%{appForm.appCoapplicantFirstName_1}"
					cssClass="form-control" maxlength="20" /></li>
			<li><label>Middle name</label> <s:textfield
					name="appForm.appCoapplicantMiddleName1"
					id="appCoapplicantMiddleName1"
					value="%{appForm.appCoapplicantMiddleName1}"
					cssClass="form-control" maxlength="20" /></li>

			<li><label>Last name<b class="req">*</b></label> <s:textfield
					name="appForm.appCoapplicantLastName_1"
					id="appCoapplicantLastName1"
					value="%{appForm.appCoapplicantLastName_1}" cssClass="form-control"
					maxlength="40" /></li>

			<li><label>Address line 1<b class="req">*</b></label> <s:textfield
					name="appForm.appCoapplicantAddress_1_1"
					id="appCoapplicant1Address1"
					value="%{appForm.appCoapplicantAddress_1_1}" maxlength="40"
					cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields ':'disabledFields'}"
					disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>

			<li><label>Address line 2<span>&nbsp;</span></label> <s:textfield
					name="appForm.appCoapplicantAddress_2_1"
					id="appCoapplicant1Address2"
					value="%{appForm.appCoapplicantAddress_2_1}" maxlength="40"
					cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}"
					disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>

			<li><label>Landmark<span>&nbsp;</span></label> <s:textfield
					name="appForm.appCoapplicantLandmark_1"
					id="appCoapplicant1Landmark"
					value="%{appForm.appCoapplicantLandmark_1}" maxlength="20"
					cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}"
					disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
					onkeydown="return M.isAddressLine(event);" /></li>

			<li id="divCoapplicantState"><label>State<b class="req">*</b></label>
				<div class="flat-field">
					<s:select name="appForm.appCoapplicantState_id_1" list="%{#states}"
						id="appCoapplicant1StateId"
						value="%{appForm.appCoapplicantState_id_1}" headerKey="0"
						headerValue="Select state"
						cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':''}"
						disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
						onfocus="customOnFocus(this);" />
				</div></li>
			<li id="APPCOAPPLICANTLOCATIONCITY"><label>City<b
					class="req">*</b></label> <s:if
					test="%{appForm.appCoapplicantCity_id_1!=null}">
					<div class="flat-field">
						<s:select list="%{beanList.citiesoptgrp1Coapplicant1}"
							name="appForm.appCoapplicantCity_id_1" id="appCoapplicant1CityId"
							value="%{appForm.appCoapplicantCity_id_1}" headerKey="0"
							headerValue="Select city"
							cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':''}"
							disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
							onfocus="customOnFocus(this);">
						</s:select>
					</div>
				</s:if> <s:else>
					<div class="flat-field">
						<s:select cssClass="form-select disabledFields" list="#{''}"
							name="appForm.appCoapplicantCity_id_1" id="appCoapplicant1CityId"
							value="" headerKey="0" headerValue="Select city" disabled="true"
							onfocus="customOnFocus(this);" />
					</div>
				</s:else></li>
			<div id="APPCOAPPLICANTLOCATIONDISTRICT">
				<s:if
					test="%{appForm.appCoapplicantCity_id_1==@com.mintstreet.common.util.Constants@OTHER_USER_ID}">
					<li><label>District<b class="req">*</b></label>
						<div class="flat-field">
							<s:select
								list="%{beanList.districtsCoapplicant1==null?'':beanList.districtsCoapplicant1}"
								name="appForm.appCoapplicantDistrictId1"
								id="appCoapplicant1DistrictId"
								value="%{appForm.appCoapplicantDistrictId1}" headerKey="0"
								headerValue="Select district"
								cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}"
								disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
								onfocus="customOnFocus(this);">
							</s:select>
						</div></li>
				</s:if>
			</div>
			<li><label>Pincode<b class="req">*</b></label> <s:textfield
					cssClass="form-control disabledFields"
					name="appForm.appCoapplicantPincode_1" id="appCoapplicant1Pincode"
					value="%{appForm.appCoapplicantPincode_1}" placeholder="Pincode"
					autocomplete="off" maxlength="6" onkeydown="return M.digit(event);" />
			</li>
			<s:if
				test="%{appForm.appCoapplicantEmployerName1==9||appForm.appCoapplicantEmployerName1==10||appForm.appCoapplicantEmployerName1==11}">
				<li><label>Employer name<b class="req">*</b></label> <s:textfield
						name="appForm.appCoapplicantEmployerName1"
						id="appCoapplicantEmployerName1"
						value="%{appForm.appCoapplicantEmployerName1}"
						cssClass="form-control" maxlength="20" /></li>
			</s:if>
		</ul>
	</s:if>
	<div class="clearfix"></div>
	<s:if test="%{quote.loanQuoteResidentTypeId==1}">

	</s:if>

	<div class="clearfix"></div>
	<div id="termsAndConditionThird"
		class="sbi-trms-div sbi-trms-div-third">
		<ul class="form-section">
			<li class="full-width"><s:if test="%{isDsrPage=='false'}">
					<div class="trms-section">
						<input type="checkbox" class="blue-css-checkbox"
							name="terms_conditions_check" id="terms_conditions_check"
							value="on"> <label for="terms_conditions_check"
							class="label-content">I have read the Terms &amp;
							Conditions and agree to the terms therein, I also authorise the
							Bank and/or its representatives to verify any information
							contained in the application or otherwise from any source
							whatsoever at their sole discretion at your office/residence
							and/or contact you and/or your family members and/or your
							Employer/Banker/Credit Bureau/UIDAI/RBI and/or any third party as
							they deem necessary.<b class="req">*</b>
						</label>
					</div>
				</s:if>
				<div class="flr mrgt-10">
					<%--<input  type="submit" class="submit-btn" name="submitApplication" id="submitApplication" value="Submit Application" onclick="return validateAltNum();">--%>
					<input type="submit" class="submit-btn" name="submitApplication"
						id="submitApplication" value="Submit Application">
				</div></li>
		</ul>
	</div>
</form>


<div class="modal otp-box" id="OTP1" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				
				<div class="verify-otp-left-section verify-mobile-otp">
						</br>
						</br>
					<h2>
						<span>Verify</span> your mobile
					</h2>
					<p>(Please enter your contact details for OTP process)</p>
				</div>
				<form name="otpApplicationAlt" id="otpApplicationAlt" method="post"
					action="javascript:void(0);" autocomplete="off">
					<div class="otp-right-section">
						</br>
						</br>
						<ul class="otp-form">
							<div id="privacyStatementHTML" class="privacyStatementHTML"
								name="privacyStatementHTML">
								<div id="otpPopHome">
									<!-- <div id="otpPopHome" name="otpPopHome" id="otpPopHome"> -->







									<li><s:hidden id="appOTPVerifyType"
											name="appOTPVerifyType" value="0" /> <s:hidden
											id="appApplyingFrom" name="appApplyingFrom" value="1" /> <label>Enter
											your Alternate mobile no. <b class="req">*</b>
									</label> <!--start code for alternate mobile number by hakeem on 20 sep 22 -->
										<s:hidden name="isdCodeAlt" id="isdCodeAlt" value="91"
											cssClass="s-input flt form-control" autocomplete="off"
											maxlength="3" placeholder="91"
											onkeydown="return M.digit(event);" /> <s:textfield
											id="alternateMobileNumber2" name="alternateMobileNumber"
											maxlength="16" value="" placeholder="Alternate Mobile no."
											onkeydown="return M.digit(event);" cssClass="form-control"
											autocomplete="off" readonly='true' /></li>




								</div>
							</div>

							<div id="otp_verify_type" style="display: none;">
								<li><label>OTP verify by?<b class="req">*</b></label>
									<div class="col-xs-12 mrgt-9">
										<div class="blue-radio blue-radio-danger">
											<input type="radio" id="appOTPVerifyTypeYes" value="0"
												name="appOTPVerifyType" checked="checked"> <label
												for="appOTPVerifyTypeYes">Mobile</label>
										</div>
										<div class="blue-radio blue-radio-danger">
											<input type="radio" id="appOTPVerifyTypeNo" value="1"
												name="appOTPVerifyType"> <label
												for="appOTPVerifyTypeNo">Email</label>
										</div>
									</div></li>
							</div>


							<li><br>

								<div id="changeAlternateNum" class="alternateUrl"
									name="changeAlternateNum">
									<%--commented by hakeem on 23 sep 22--%>

								</div></li>

							<div id="overlay-row_otp_hl">
								<li>
									<button class="track-btn conf-track-btn" id="sendOptAlt"
										name="sendOptAlt" type="submit"
										onclick="setNewAlternateMobile();">Send OTP</button>
								</li>
							</div>
							<div id="OtpPopHl" class="OtpPopHl" style="display: none;">
								<div id="otp_row_confirm">
									<li id="otp_row_confirm_html"><label><span
											id="otpLabel">Enter code sent by SMS<b class="req">
													*</b></span></label> <input id="inputOtpAlt" name="inputOtpAlt"  maxlength="6"type="text"
										class="form-control secure-otp" 
										onkeydown="return M.digit(event);" autocomplete="off">
										<input id="inputOtpAlt1" name="inputOtpAlt" type="hidden"
										class="form-control secure-otp" 
										onkeydown="return M.digit(event);" autocomplete="off">

									</li>
									<li></li>


									<li>
										<button class="track-btn conf-track-btn" id="confirmOtpAlt"
											name="confirmOtpAlt" type="submit">Confirm</button>
										<button class="resend-btn" id="resendOtpAlt"
											name="resendOtpAlt" type="submit">Resend</button>
									</li>
								</div>
							</div>
							<!-- <div class="clear"></div>
								<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li> -->
						</ul>
						<div class="clear"></div>
						<li id="errorOTPMsg1" class="" style="display: none;"></li> <span
							class="otp-loader"><img id="opt-loader-application"
							style="display: none;"
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif" /></span>
						<!-- <div id="errorOTPMsg" style="display: none;"></div> -->
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mCustomScrollbar.concat.min.js"></script>


<%--END --%>
<div id="htmlappDistrictId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/AppDistrictId.jsp"></s:include>
</div>
<div id="htmlappBranchId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/AppBranchId.jsp"></s:include>
</div>
<div id="htmlappPermanentDistrictId" class="hide">
	<s:include
		value="/appNew/loan/autoloan/includes/AppPermanentDistrictId.jsp"></s:include>
</div>
<div id="htmlappOfficeDistrictId" class="hide">
	<s:include
		value="/appNew/loan/autoloan/includes/AppOfficeDistrictId.jsp"></s:include>
</div>
<div id="htmlappOtherIdNumber" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/AppOtherIdNumber.jsp"></s:include>
</div>
<div id="htmlappCoapplicant1DistrictId" class="hide">
	<s:include
		value="/appNew/loan/autoloan/includes/AppCoapplicant1DistrictId.jsp"></s:include>
</div>
<input type="hidden" id="residanceTypeId" name="residanceTypeId"
	value="" />
<script type="text/javascript">
	/* -------------------------------------------------
	 * apply auto suggest in employee name field for all
	-------------------------------------------------  */
	auto_suggestion_employer("appLoanEmployerName");
	auto_suggestion_employer("appCoapplicantEmployerName1"); 
	auto_suggestion_employer("appCoapplicantEmployerName2"); 
	var employmentTypeId =$('#employmentTypeIdHidden').val();
	if(employmentTypeId == 9){
		$("#appLoanEmployerName").attr('disabled','disabled');	
	}
		
	var isOfferPage= false;

	function fetchJsonData(){
		jsonJSArray = '<s:property value="%{@com.mintstreet.loan.autoloan.action.AutoLoanAction@jsonJSArray3AutoLoan}"/>';
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	apply_currency_form(document.autoloancriteriaform);

	jQuery("span.Rs").each(function(){
		var placeholder = jQuery(this).text();
		var strip_commas = placeholder.replace(/,/g, "");
		strip_commas = Number(strip_commas).toPrecision();
		strip_commas = strip_commas.replace(".0","");
		jQuery(this).text(M.moneyFormat(strip_commas));
	});
  	jQuery(document).unbind("change").on("change", "#appCityId", function() {
  		districtDetails[0]="APPLOCATIONDISTRICT";
		districtDetails[1]="appDistrictId";
		districtDetails[2]="appForm.appDistrictId";
		districtDetails[3]="District";
		
		branchDetails[0]="APPLOCATIONBRANCH1";
		branchDetails[1]="appBranchId";
		branchDetails[2]="appForm.appBranchId";
		branchDetails[3]="Branch";
		branchDetails[4]=<s:property value="%{quote.loanQuoteResidentTypeId==2?1:0}" />;
		branchDetails[5]="<font style='font-size:10px;'>(<a target='_blank' class='mob-branch-link' href='"+BANK_FINDER_URL+"'>Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font>";
		
		populateDistrictByStateId(this, jQuery("#appStateId").val(), districtDetails, branchDetails,'autoloancriteriaform');
	}); 
	
 	<s:if test="%{quote.loanQuoteResidentTypeId==2}">
 		$("#residanceTypeId").val('2');
		jQuery(document).on("change","#appDistrictId", function() {
			branchDetails[0]="APPLOCATIONBRANCH1";
			branchDetails[1]="appBranchId";
			branchDetails[2]="appForm.appBranchId";
			branchDetails[3]="Branch";
			branchDetails[4]=1;
			branchDetails[5]="<font style='font-size:10px;'>(<a target='_blank' class='mob-branch-link' href='"+BANK_FINDER_URL+"'>Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font>";
			populatebranchByDistrictId(this, jQuery("#appStateId").val(), jQuery("#appDistrictId").val(), branchDetails,'autoloancriteriaform');
		});
	</s:if> 
	<s:else>
	$("#residanceTypeId").val('1');
	</s:else>

</script>
<s:set var="minFolderPath" value="%{''}" />
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}" />
</s:if>
<script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/autoloan/js<s:property value="#minFolderPath"/>/jquery.auto_loan_third_page.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.copyAddress.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
	<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081708&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	<s:if test="%{isDsrPage=='false'}">
	</s:if>
</s:if> --%>

<script>


	function checkwithmobileno()
	{
		var fisrt_mob = $('#first_mob').val();
		var alter_mob = $('#alternateMobileNumber').val();
		
		console.log(fisrt_mob);
		console.log(alter_mob);
		if(fisrt_mob==alter_mob)
		{
			$('#inalterror').text('Alternate Mobile cannot be same as Mobile number.');
			document.getElementById("alternateUrl").style.display = "none";
			return false;
		}
		else{
			document.getElementById("alternateUrl").style.display = "block";
			return true;
		}
	}
	

	function validateAltNumVerify() {
	        if (document.getElementById("alternateUrl").style.display == "block") {          <!--created  code by hakeem 9 sep 22  -->
	        	$("#alternateUrlVal").val('1');
                $("#inalterror").html("Please verify Alternate Mobile Number");
                return false;
	        } else {
				$("#alternateUrlVal").val('0');
				$("#inalterror").html("");
				return true;
         	}
	}

    function showlink(){

 
		var mob_no_len = document.getElementById('alternateMobileNumber').value;
	     var alt_ISD = document.getElementById('appAltISDCode').value;
	     var fisrt_mob = $('#first_mob').val();
		 var pattern1 =  /^[1233456789][0-9]{7,15}$/;
		 var pattern =  /^[1233456789][0-9]{0,2}$/;
        if ((mob_no_len.length > 7 && mob_no_len.length <  17 )&& (pattern.test(alt_ISD) && pattern1.test(mob_no_len))) {
        	$("#inalterror").html("");	
        	if(fisrt_mob==mob_no_len)
    		{
    			$('#inalterror').text('Alternate Mobile cannot be same as Mobile number.');
    			document.getElementById("alternateUrl").style.display = "none";    			
    		}
    		else{
    			document.getElementById("alternateUrl").style.display = "block";
    		}  
			
		} else {
			 document.getElementById("alternateUrl").style.display = "none";						
						
				if (mob_no_len.startsWith("0")  || alt_ISD.startsWith("0")){
					  $("#inalterror").html(" Number should not start with 0"); 
					  //dfd
				 } else  if((mob_no_len.length == 0  &&  alt_ISD.length != 0 ) || (mob_no_len.length != 0  &&  alt_ISD.length == 0 )) {
  					 $("#inalterror").html("Provide  mobile and isd together ");
  				 } else if(mob_no_len.length < 8  && mob_no_len.length != 0) {
					   $("#inalterror").html("provide valid mobile number ");
				 } else {
					    $("#inalterror").html(""); 
				   }
			        
		}  
	}
   function validatISD(){
    	
    	var alt_ISD = document.getElementById('appAltISDCode').value;
		//var alt_mo = document.getElementById('alternateMobileNumber').value;
		
    	if(alt_ISD.length >0 && alt_ISD.length < 4 ) {
			 //var pattern1 =  /^[1233456789][0-9]{6,14}$/;
			 //var pattern =  /^[1233456789][0-9]{0,2}$/;
			
		     //if(pattern.test(alt_ISD) && pattern1.test(alt_mo) )
			$("#inalterror").html("");
			$("#alternateMobileNumber2").val($("#alternateMobileNumber").val());
	   	    $("#isdCodeAlt").val($("#appAltISDCode").val());	
			openPopups('OTP1','1');
			hideAlternateDiv();
	       
    	} else {
    		 $("#inalterror").html("Please valid ISD Code for alternate mobile number");
    	}
    }
    <!--  Start new code for alternate number on 24  Aug 22 -->
    
    <!-- end code for alternate mobile numnber  -->
  function hideAlternateDiv () {
	 
        document.getElementById('alternateMobileNumber').disabled = true;
        document.getElementById('appAltISDCode').disabled = true;
        document.getElementById("alternateUrl").style.display = "none";

        }
  function showAlternateDiv () {
	 
        document.getElementById('alternateMobileNumber').disabled = false;
        document.getElementById('appAltISDCode').disabled = false;
        document.getElementById("alternateUrl").style.display = "block";

        }
  
  function changeAlternateMobile () {
	  $('#alternateMobileNumber2').attr('readonly', false);
  }
  
  function setNewAlternateMobile () {
	 <!-- document.getElementById("changeAlternateNum").style.display = "none"; -->
	  $("#alternateMobileNumber").val($("#alternateMobileNumber2").val());
  }

</script>



<script type="text/javascript">
    function Noncbsvalidate() {
		
        var mobile = document.getElementById("alternateMobileNumber").value;
		alert("Noncbsvalidate"+mobile);
        if(mobile!=''){
	        //var pattern =  /^[1233456789][0-9]{9}$/;
	        var pattern =  /^[1233456789][0-9]{6,14}$/;
	        if (pattern.test(mobile)) {
	            $("#alterror").html("");
	            return true;
	        }
		}
		//$("#alterror").html("<img src='" + BANK_IMAGE_FOLDER + "/cross.png'> Invalid Mobile No.");
		return false;

	}
	
	function validateAlternateISDCode() {
        var isdCode = document.getElementById("appAltISDCode").value;
		console.log("validateAlternateISDCode"+isdCode);
        if(isdCode!=''){
	        var pattern =  /^[1233456789][0-9]{0,2}$/;
	        if (pattern.test(isdCode)) {
	            $("#alterror").html("");
	            return true;
	        }
		}
		//$("#alterror").html("<img src='" + BANK_IMAGE_FOLDER + "/cross.png'> Invalid Mobile No.");
		return false;

	}
</script>



<script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.otherLoanCommonOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloanOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script> --%>

