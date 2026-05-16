<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>

<form name="homeloancriteriaform" id="homeloancriteriaform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded"
	onclick="<s:property value="%{(@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local')?'':'ga('send', {'hitType': 'event', 'eventCategory': 'HLAppSubmit', 'eventAction': 'HLAppSubmit', 'eventLabel': 'HLAppSubmit' });'}"/>">
	<h3><s:property value="%{quote.loanQuoteResidentTypeId==1?'PERSONAL DETAILS':'ADDRESS IN INDIA'}"/></h3>
	
	<ul class="form-section">
		<li>
			<label>First name<span class="req">&nbsp;*</span></label>
			<s:textfield name="appForm.appFirstName"  id="appFirstName" value="%{appForm.appFirstName==null?#session.applicantName:appForm.appFirstName}"   maxlength="20"  onblur="ChangeCase(this);"
				cssClass="form-control %{#session.applicantName!=null?'disabledFields':''}" readonly="true" />
		     <s:if test="%{isDsrPage=='false'}">
			    <div class="custom-tooltip">Enter first name</div>
		     </s:if>
		    
		</li>
		<li>
			<label>Middle name<span>&nbsp;</span></label>
			<s:textfield name="appForm.appMiddleName"  id="appMiddleName" value="%{appForm.appMiddleName}"   readonly="true"   cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);"/>
		</li>
		<li>
			<label>Last name<span class="req">&nbsp;*</span></label>
			<s:textfield name="appForm.appLastName"  id="appLastName" value="%{appForm.appLastName}"   maxlength="20"  
			cssClass="form-control %{(quote.loanQuoteLoanPurposeId==25 && appForm.appLastName!=null ? 'disabledFields' : '')}" 
			onblur="ChangeCase(this);" readonly="true" /> 
		    <s:if test="%{isDsrPage=='false'}">
			     <div class="custom-tooltip">Enter last name</div>
		    </s:if>
		</li>
		<li>
			<label>Gender<span class="req">&nbsp;*</span></label>
			<div class="flat-field">
			<s:select disabled="true" list="%{#genderList}" name="appForm.appGender" id="appGender" value="%{appForm.appGender}" headerKey="0" headerValue="Select gender"  cssClass="form-select" onfocus="customOnFocus(this);"/>
			</div>
	   	</li>
	   	<li>
			<label>Date of birth<span class="req">&nbsp;*</span></label>
			<s:textfield  disabled="true" name="appForm.appDob"  id="third_date_of_birth"  cssClass="form-control dob-cal"  value="%{appForm.appDob}" maxlength="10"/>
	   	</li>
		<li>
			<label>Address line 1<span class="req">&nbsp;*</span></label>
			<s:textfield name="appForm.appAddress1"  id="appAddress1" value="%{appForm.appAddress1}" maxlength="%{isDsrPage=='true'?100:'100'}"
			 cssClass="form-control %{(quote.loanQuoteLoanPurposeId==25 && appForm.appAddress1!=null? 'disabledFields' : '')}" 
			 readonly="true" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
		    <s:if test="%{isDsrPage=='false'}">
			    <div class="custom-tooltip">Enter your address</div>
		    </s:if>
		
		</li>
		<li>
			<label>Address line 2<span class="req">&nbsp;*</span></label>
			<s:textfield name="appForm.appAddress2"  id="appAddress2" value="%{appForm.appAddress2}"   maxlength="%{isDsrPage=='true'?100:'100'}" 
			cssClass="form-control %{(quote.loanQuoteLoanPurposeId==25 && appForm.appAddress2!=null? 'disabledFields' : '')}" 
			 readonly="true"  onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
		</li>
		<li>
			<label>Landmark<span class="req">&nbsp;</span></label>
			<s:textfield name="appForm.appAddressLandmark"  id="appAddressLandmark" value="%{appForm.appAddressLandmark}"   maxlength="30" 
			cssClass="form-control %{(quote.loanQuoteLoanPurposeId==25 && appForm.appAddressLandmark!=null? 'disabledFields' : '')}" 
			 readonly="true"  onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
		</li>
	<li>
		<label class="">State<b class="req">*</b></label>
		<s:set var="isDisabledResidenceState" value="%{true}"/>
			<s:if test="%{quote.loanQuoteResidentTypeId!=null && quote.loanQuoteResidentTypeId==1}">
				<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
					<s:if test="%{appForm.appStateId!=null && appForm.appStateId>0 && quote.loanQuotePreferredLocationOfAvailingLoan == 2}"></s:if>
					<s:elseif test="%{appForm.appStateId!=null && appForm.appStateId>0}">
						<s:set var="isDisabledResidenceState" value="%{true}"/>
					</s:elseif>
					<s:else></s:else>
				</s:if>
				<s:else></s:else>
			</s:if>
		
		<div class="flat-field">
			<s:select name="appForm.appStateId" id="appStateId" autocomplete="off" list="%{#states}"  cssClass="form-select" 
			   disabled="true" value="%{appForm.appStateId}" headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<div id="APPLOCATIONCITY">
		<s:if test="%{appForm.appStateId>0}">
			<s:include value="/appNew/loan/homeloan/AppCityId.jsp"></s:include>
		</s:if>
	</div>
	<div id="APPLOCATIONDISTRICT">
		<s:if test="%{beanList.districts!=null && appForm.appCityId == 9999999}">
			<s:include value="/appNew/loan/homeloan/AppDistrictId.jsp"></s:include>
		</s:if>
	</div>
	<li>
		<label>Pincode<span class="req">&nbsp;*</span></label>
		<s:textfield  value="%{appForm.appPincode}" id="appPincode" cssClass="form-control %{(quote.loanQuoteLoanPurposeId==25 && appForm.appPincode!=null? 'disabledFields' : '')}" 
		 readonly="true"  name="appForm.appPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"/>
	</li>
	<s:if test="%{loanScenarioBean.chosenProductId!=@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
		<li>
			<label>Residence type<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
			<div class="flat-field"> 
				<s:select list="#{1:'Owned',2:'Rented - With family',3:'Rented - With friends',4:'Rented - Staying alone',5:'Paying Guest / Hostel',5:'Company'}" 
				name="appForm.appResidenceType" id="appResidenceType" value="%{appForm.appResidenceType}" cssClass="form-control" headerKey="0" headerValue="Select residence type" onfocus="customOnFocus(this);"/>
			</div>
		</li>
	</s:if>
	<s:if test="%{quote.loanQuoteLoanPurposeId==4}">
		<s:include value="/appNew/loan/homeloan/includes/PALPersonalDetail.jsp"></s:include>
	</s:if>
	</ul>
	<s:include value="/appNew/loan/homeloan/HomeNRIAddress.jsp"></s:include>
	<div class="clearfix"></div>
	<s:if test="%{quote.loanQuoteResidentTypeId==1}">
	
	<h3><span class="flt">PERMANENT ADDRESS</span>
	<div class="prm-chk-div">
		<input type="checkbox" name="appForm.clonePermanentAddress" id="clonePermanentAddress" value="<s:property value="%{appForm.clonePermanentAddress==null?'false':'true'}"/>" <s:property value="%{appForm.clonePermanentAddress==true?'checked=\"checked\"':''}"/> class="blue-css-checkbox">
		<label class="label-content" for="clonePermanentAddress">Same as current address</label>
		<%-- <s:checkbox name="appForm.clonePermanentAddress" id="clonePermanentAddress" value="%{appForm.clonePermanentAddress==true?'true':'false'}" cssStyle="vertical-align:middle" cssClass="clone_applicant_address" />
		<label style="line-height:17px; font-size:11px;">Same as current address</label> --%>
	</div>
	</h3>
	<div style="clear:both;"></div>
	<ul class="form-section">
		<li>
			<label>Address line 1<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
			<s:textfield name="appForm.appPermanentAddress1"  id="appPermanentAddress1" value="%{appForm.appPermanentAddress1}"  maxlength="%{isDsrPage=='true'?100:'100'}"  onkeydown="return M.isAddressLine(event);" 
			cssClass="form-control %{appForm.clonePermanentAddress==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onpaste="return false;"/>
		   <s:if test="%{isDsrPage=='false'}">
			  <div class="custom-tooltip">Enter permanent address</div>
		   </s:if>
		</li>
		<li>
			<label>Address line 2<span class="req">&nbsp;</span></label>
			<s:textfield name="appForm.appPermanentAddress2"  id="appPermanentAddress2" value="%{appForm.appPermanentAddress2}"   maxlength="%{isDsrPage=='true'?100:'100'}"  onkeydown="return M.isAddressLine(event);" 
			cssClass="form-control %{appForm.clonePermanentAddress==true?'form-control ':'form-control'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onpaste="return false;"/>
		</li>
		<li>
			<label>Landmark<span class="req">&nbsp;</span></label>
			<s:textfield name="appForm.appPermanentAddressLandMark"  id="appPermanentAddressLandMark" value="%{appForm.appPermanentAddressLandMark}"   maxlength="30"  onkeydown="return M.isAddressLine(event);" 
			cssClass="form-control %{appForm.clonePermanentAddress==true?'form-control ':'form-control'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onpaste="return false;"/>
		</li>
		<li id="APPPERMANENTLOCATIONSTATE">
			<label>State<!-- <b class="req">&nbsp;*</b> --></label>
			<div class="flat-field">
				<s:select name="appForm.appPermanentStateId"  list="%{beanList.states==null?'':beanList.states}" id="appPermanentStateId"
				value="%{appForm.appPermanentStateId}" headerKey="0" headerValue="Select state"
				cssClass="%{appForm.clonePermanentAddress==true?'form-select ':'form-control'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onfocus="customOnFocus(this);"/>  
			</div>
		</li>
		<div id="APPPERMANENTLOCATIONCITY">
				<s:include value="/appNew/loan/homeloan/AppPermanentCityId.jsp"></s:include>
		</div>
		<div id="APPPERMANENTLOCATIONDISTRICT">
			 <s:if test="%{appForm.appPermanentDistrictId!=null && beanList.districtsPermanent!=null}">
				<s:include value="/appNew/loan/homeloan/AppPermanentDistrictId.jsp"></s:include>
			 </s:if>
		</div>
		<li>
			<label>Pincode<b class="req"><s:property escapeHtml ="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
			<s:textfield name="appForm.appPermanentPincode" id="appPermanentPincode"  value="%{appForm.appPermanentPincode}" placeholder="Pincode"  autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"
			cssClass="form-control %{appForm.clonePermanentAddress==true?'disabledFields ':'disabledFields'} " disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" />  
		</li>
	</ul> 
	
	</s:if>
	
	<div class="clearfix"></div>
	<s:if test="%{quote.loanQuoteLoanPurposeId!=4}">
	<h3>PROPERTY ADDRESS</h3>
    <ul class="form-section">
		<li>
			<label>Address line 1<span class="req">&nbsp;*</span></label>
			<s:textfield name="appForm.appPropertyAddress1" cssClass="form-control" id="appPropertyAddress1" value="%{appForm.appPropertyAddress1}" maxlength="100" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" onpaste="return false;"/>
		</li>
		<li>
			<label>Address line 2</label>
			<s:textfield name="appForm.appPropertyAddress2" cssClass="form-control" maxlength="40" id="appPropertyAddress2" value="%{appForm.appPropertyAddress2}"  onblur="ChangeCase(this);"  onkeydown="return M.isAddressLine(event);" onpaste="return false;" />
		</li>
		<li>
			<label>Landmark</label>
			<s:textfield name="appForm.appPropertyAddressLandmark" cssClass="form-control" maxlength="40" id="appPropertyLandmark" value="%{appForm.appPropertyAddressLandmark}"  onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);"  onpaste="return false;" />
		</li>
		<li id="divState">
			<label class="">State<b class="req">*</b></label>
			<s:set var="isDisabledPropertyState" value="%{false}"/>
			<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
				<s:if test="%{appForm.appPropertyCityId!=null && appForm.appPropertyCityId>0 && quote.loanQuotePreferredLocationOfAvailingLoan==1}"></s:if>
				<s:elseif test="%{appForm.appPropertyCityId!=null && appForm.appPropertyCityId>0 && quote.loanQuotePreferredLocationOfAvailingLoan==2}">
					<s:set var="isDisabledPropertyState" value="%{true}"/>
				</s:elseif>
				<s:else></s:else>
			</s:if>
			<s:else></s:else>
			<div class="flat-field">  
				<s:select name="appForm.appPropertyStateId" id="appPropertyStateId" autocomplete="off" list="%{#states}" 
				cssClass="form-select %{#isDisabledPropertyState==true?'':''}" disabled="%{#isDisabledPropertyState}"
				value="%{appForm.appPropertyStateId}" headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
			</div>
		</li>
		<div id="APPPROPERTYLOCATIONCITY">
			<s:include value="/appNew/loan/homeloan/AppPropertyCityId.jsp"></s:include>
		</div>
		<div id="APPPROPERTYLOCATIONDISTRICT">
			<s:if test="%{appForm.appPropertyCityId!=null && appForm.appPropertyCityId == 9999999}">
				<s:if test="%{appForm.appPropertyDistrictId!=null && appForm.appPropertyDistrictId >0}">
					<s:include value="/appNew/loan/homeloan/AppPropertyDistrictId.jsp"></s:include>
				</s:if>
			</s:if>
		</div>
		<li>
			<label>Pincode<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
			<s:textfield cssClass="form-control " name="appForm.appPropertyPincode"  id="appPropertyPincode" value='%{appForm.appPropertyPincode}' placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"/>
		</li>
	</ul>
	</s:if>
	
	<div class="clearfix"></div>
	</br>
	<s:if test="%{ appForm.appEmploymentType==1 || appForm.appEmploymentType==2 || appForm.appEmploymentType==3}">
	<s:hidden id="appEmploymentType" name="appForm.appEmploymentType" value="%{appForm.appEmploymentType}" />
	<h3>EMPLOYMENT DETAILS</h3>
    <ul class="form-section">
		<li>
			<s:if test="%{appForm.appEmploymentType==1}">
				 <label>Employer name<span class="req">&nbsp;*</span></label>
			</s:if> 
		    <s:elseif test="%{appForm.appEmploymentType==2 ||appForm.appEmploymentType==3}">
				 <label>Company name<span class="req">&nbsp;*</span></label>
			</s:elseif>
			    <s:if test="%{isDsrPage=='false'}">
			         <div class="custom-tooltip">Enter company you are currently working with.</div>
		        </s:if>
			
			<s:if test="%{appForm.appLoanEmployerName!=null}">
				<s:textfield name="appForm.appLoanEmployerName"  id="appLoanEmployerName" value="%{appForm.appLoanEmployerName}"  cssClass="form-control" disabled="true"/>
			</s:if>
			<s:else>
				<s:textfield name="appForm.appLoanEmployerName"  id="appLoanEmployerName" value="%{appForm.appLoanEmployerName}"  cssClass="form-control"/>
			</s:else>
		</li>
		<li>
			<s:if test="%{appForm.appEmploymentType==1}">
			   <label>Joining date<span class="req">&nbsp;*</span></label>
			</s:if>
			<s:else>
				<label>Start date of business<b class="req">&nbsp;*</b></label>
			</s:else>
		  	<div class="flat-field1">
				<s:select list="%{beanList.joiningyears==null?'':beanList.joiningyears}" name="appForm.appCompanyJoiningYear"  id="appCompanyJoiningYear" value="%{appForm.appCompanyJoiningYear}"  cssClass="form-select date_dropdown_year"  headerKey="0" headerValue="Select year" onfocus="customOnFocus(this);"/>
			</div>
			<s:if test="%{appForm.appCompanyJoiningMonth!=null && appForm.appCompanyJoiningMonth>0 && appForm.appCompanyJoiningMonth<13}">
			 	<div class="flat-field1 mr-r9">	
					<s:select list="#{'1':'Jan ','2':'Feb','3':'Mar','4':'Apr','5':'May','6':'Jun','7':'July','8':'Aug','9':'Sep','10':'Oct','11':'Nov','12':'Dec'}" 
					name="appForm.appCompanyJoiningMonth" id="appCompanyJoiningMonth" value="%{appForm.appCompanyJoiningMonth}" cssClass="form-select %{appForm.appCompanyJoiningMonth>=0?'':'disabledFields '} date_dropdown_month" disabled="%{appForm.appCompanyJoiningMonth>=0?'false':'true'" headerKey="13" headerValue="Select month" onfocus="customOnFocus(this);"/>
				</div>
			</s:if>
			<s:else>
				<div class="flat-field1  mr-r9">		
					<s:select list="#{'1':'Jan ','2':'Feb','3':'Mar','4':'Apr','5':'May','6':'Jun','7':'July','8':'Aug','9':'Sep','10':'Oct','11':'Nov','12':'Dec'}" 
					name="appForm.appCompanyJoiningMonth" id="appCompanyJoiningMonth" value="%{appForm.appCompanyJoiningMonth}" cssClass="form-select disabledFields date_dropdown_month" disabled="true" headerKey="13" headerValue="Select month" onfocus="customOnFocus(this);"/>
				</div>
			</s:else>
		</li>
		<li>
			<label>Total work experience<b class="req">&nbsp;*</b></label>
			<div class="flat-field1"> 
				<s:select list="#{1:'Less than 1', 2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'More than 10 years'}" cssClass="form-select"
		  		id="appWorkExperienceYear" name="appForm.appWorkExperienceYear" value="%{appForm.appWorkExperienceYear}" headerKey="13" headerValue="Select year" onfocus="customOnFocus(this);"/>    <!-- headerKey="13" -->
			</div>
			<s:if test="%{appForm.appWorkExperienceMonth>0 && appForm.appWorkExperienceMonth<13}">
				 <div class="flat-field1 mr-r9"> 
					 <s:select list="#{1:'0',2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'11'}" cssClass="form-select disabledFields"
					 id="appWorkExperienceMonth" name="appForm.appWorkExperienceMonth" value="%{appForm.appWorkExperienceMonth}" 
					 headerKey="13" headerValue="Select month" onfocus="customOnFocus(this);"/>
				 </div>
			</s:if>
			<s:else>
				<div class="flat-field1 mr-r9"> 
					<s:select list="#{1:'0',2:'1', 3:'2', 4:'3', 5:'4', 6:'5', 7:'6', 8:'7', 9:'8', 10:'9', 11:'10', 12:'11'}" cssClass="form-select disabledFields "
					 id="appWorkExperienceMonth" name="appForm.appWorkExperienceMonth" value="%{appForm.appWorkExperienceMonth}" 
					 headerKey="13" headerValue="Select month" disabled="true" onfocus="customOnFocus(this);"/>
			    </div>
			</s:else>
		</li>
		<s:if test="%{quote.loanQuoteResidentTypeId==null || quote.loanQuoteResidentTypeId==1}">
			<li>
				<label>Address line 1<span class="req">&nbsp;*</span></label>
				<s:textfield name="appForm.appOfficeAddress1" cssClass="form-control"  id="appOfficeAddress1" value="%{appForm.appOfficeAddress1}" maxlength="40" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" onpaste="return false;" />
			    <s:if test="%{isDsrPage=='false'}">
			         <div class="custom-tooltip">Enter your current office address.</div>
		        </s:if>
			</li>
			<li>
				<label>Address line 2</label>
				<s:textfield name="appForm.appOfficeAddress2" maxlength="40" cssClass="form-control" id="appOfficeAddress2" value="%{appForm.appOfficeAddress2}"  onblur="ChangeCase(this);"  onkeydown="return M.isAddressLine(event);"  onpaste="return false;" />
			</li>
		  	<li id="APPOFFICELOCATIONSTATE">
			<label class="">State<span class="req">*</span></label>
				<div class="flat-field">
					<s:select name="appForm.appOfficeStateId" id="appOfficeStateId" cssClass="form-select " autocomplete="off" list="%{#states}"
					value="%{appForm.appOfficeStateId}" headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
				</div>
			</li>
			<div id="APPOFFICELOCATIONCITY">
				<s:include value="/appNew/loan/homeloan/AppOfficeCityId.jsp"></s:include>
			</div>
			<div id="APPOFFICELOCATIONDISTRICT">
				<s:if test="%{appForm.appOfficeCityId!=null && appForm.appOfficeCityId == 9999999}">
					<s:if test="%{appForm.appOfficeDistrictId!=null && appForm.appOfficeDistrictId >0}">
						<s:include value="/appNew/loan/homeloan/AppOfficeDistrictId.jsp"></s:include>
					</s:if>
				</s:if>
			</div>
			<li>
				<label>Pincode<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
				<s:textfield  name="appForm.appOfficePincode" cssClass="form-control"  id="appOfficePincode" value='%{appForm.appOfficePincode}' placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"/>
			</li>
		</s:if>
		<s:else>
		<s:include value="/appNew/loan/homeloan/HomeEMPNRIAddress.jsp"></s:include>
		</s:else>
	    <li>
			<label>Phone no.(with STD code)<b class="req"></b></label>
			<s:textfield name="appForm.appOfficePhoneStdCode" id="appOfficePhoneStdCode" value="%{appForm.appOfficePhoneStdCode}" autocomplete="off" maxlength="6" placeholder="STD code" onkeydown="return M.digit(event);" cssClass="s-input flt form-control"/>
			<s:textfield name="appForm.appOfficePhoneNumber" id="appOfficePhoneNumber" value="%{appForm.appOfficePhoneNumber}" maxlength="8" placeholder="Land line no." 
			 onkeydown="return M.digit(event);" cssClass="b-input ml10 flt form-control"/>
		</li>

                    <%-- start code for alternate mobile number  --%>
                     <s:hidden id="first_mob"	value="%{#session.mobile}" /> 
                     <s:hidden id="mobile" name="appForm.appMobileNo" value="%{appForm.appMobileNo}" /> 
                     <s:hidden id="appMobileNo" name="appForm.appMobileNo" value="%{appForm.appMobileNo}" /> 
               <div id="alternateMobile" class="alternateMobile" name ="alternateMobile">
                 <li>
               <%--  commneted by hakeem for alternate mobile number it will show before link 10 aug      --%>                        
               <%-- <label>Alternate Mobile Number (Note:This additional mobile no will not be updated in CBS)<b class="req"></b></label>
                       <label>Please input correct alternate mobile number as you are going to receive OTP to validate this mobile number</label>--%>
                        <%--  created  by hakeem for alternate mobile number it will show before link on 10 aug      --%>
                               <label>Alternate Mobile Number <b class="req"></b></label>
                              
                                                            
                      <%-- <s:textfield name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" />--%>
                            <s:textfield name="appForm.appAltISDCode"  id="appAltISDCode"  value="%{appForm.appAltISDCode}" cssClass="s-input flt form-control" autocomplete="off"  minlength="1" maxlength="3"  onkeyup= "return showlink();" onkeydown="return M.digit(event);" placeholder="ISD code"  />
                         

        <%--               <s:textfield name="appForm.appAlternateMobileNumber" onblur="Noncbsvalidate();" id="alternateMobileNumber" value="%{appForm.appAlternateMobileNumber}" autocomplete="off" maxlength="15" placeholder="Alternate Mobile Number" onkeyup= "return showlink();" onkeydown="return M.digit(event);" cssClass="b-input ml10 flt form-control" disabled=""/>  --%>

  <s:textfield name="appForm.appAlternateMobileNumber" id="alternateMobileNumber" value="%{appForm.appAlternateMobileNumber}" autocomplete="off" maxlength="15" placeholder="Alternate Mobile Number" onkeyup= "return showlink();" onkeydown="return M.digit(event);Noncbsvalidate();" cssClass="b-input ml10 flt form-control" disabled=""  />
<div class="custom-tooltip">Note:This additional mobile no will not be updated in CBS. Please input correct alternate mobile number as you are going to receive OTP to validate this mobile number</div>
           <%--created new code for alternate mobile number on 23 Aug 22 --%>     
		   
		   <br>
		   <br>
           <%-- <span id="inalterror" style="color:#e96e57; font-size:11px"></span>--%>
		   
		   <br>
		   <br>
		   <br>
		   <span id="inalterror" style="color:#e96e57; font-size:11px"></span>
          
           <%-- <span class="alterror" id="alterror"></span>--%>
                </li>
               </div>
              <li>
              </li>
              
               
             <li>
                <br>
                <div id="alternateUrl" class="alternateUrl" name ="alternateUrl" style="display:none;">
                      <!--  <b> <a href="javascript:void(0);" onclick="javascript:openPopups('OTP2','1');  return hideAlternateDiv(); ">Click here to verify Otp</a></b> -->
                       <b> <a href="javascript:void(0);" onclick="validatISD();">Click here to verify OTP</a></b>
                        <s:hidden id="alternateUrlVal" name="alternateUrlVal" value="0"/>
                </div>
              
             </li> 
            

        <%--end code --%>	                
                         
             	
		
	</ul>
	</s:if>
	
	<div class="clearfix"></div> 
	<h3>IDENTITY DETAILS</h3>
	<ul class="form-section form-section-li" id="5">
		<li>
			<label>Do you have Aadhaar Number?</label>
			<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="appForm.appHaveAadhaarNumber" id="haveAadhaarYes" value="1"  checked="checked">
					<label for="haveAadhaarYes"> Yes </label>
				</div>
				<div class="blue-radio blue-radio-danger">
					<input type="radio" name="appForm.appHaveAadhaarNumber" id="haveAadhaarNo" value="2">
					<label for="haveAadhaarNo"> No </label>
				</div>
			</div>
			<div id="aadhaarConfirmation" class="adh-msg"></div>
		</li>
		<li>
	        <label>PAN<span class="req">&nbsp;*</span> (<a href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN" target="_blank">Verify your PAN</a>)</label>
	        <s:textfield placeholder="Enter PAN" name="appForm.appPanCardNo"  id="appPanCardNo" value="%{appForm.appPanCardNo}" maxlength="10"
	        cssClass="form-control disabledFiedls" readonly="true" />
			<s:if test="%{isDsrPage=='false'}">
				<div class="custom-tooltip">Enter Valid PAN</div>
			</s:if>
     		<%--<div class="sm-chkbox">
     			<input type="checkbox" id="appPanCardLater" name="appForm.appPanCardLater" 
     			value="<s:property value="%{appForm.appPanCardLater==true?'true':'false'}"/>" 
     			<s:property value="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'checked=checked':''}"/> class="blue-css-checkbox">
     			<label class="label-content" for="appPanCardLater">I would like to submit my PAN later<span class="req">&nbsp;*</span></label>
			</div>--%>
        </li>
		<%-- <li>      
	        <label>Aadhaar number<span class="req"> </span></label>
	        <s:if test="%{appForm.appSubTypeId==@com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_EKYC}">
				<s:textfield placeholder="Enter Aadhaar number" name="appForm.appAadhaarNumber" id="appAadhaarNumber" value="%{appForm.appAadhaarNumber}"   maxlength="12" onkeydown="return M.digit(event);" cssClass="form-control" disabled="true" />
			</s:if>
			<s:else>
				<s:textfield placeholder="Enter Aadhaar number" name="appForm.appAadhaarNumber" cssClass="form-control" id="appAadhaarNumber" value="%{appForm.appAadhaarNumber}"   maxlength="12" onkeydown="return M.digit(event);" />
			</s:else>
		</li> --%>
		<li>
			<label>Other identity proof<b class="req">&nbsp;</b></label>
			<div class="flat-field">
					<s:select list="#{'0':'Select id proof','165':'Voter Id','166':'Passport','167':'Driving License','160':'Ration Card'}" name="appForm.appOtherId" id="appIdProof" value="%{appForm.appOtherId}" cssClass="form-select" onfocus="customOnFocus(this);"/>
			</div>
		</li>
		<div id="AppOtherIdNumber" >
			<s:if test="%{appForm.appOtherId!=0 && appForm.appOtherId!=null}">
				<li>
					<label>Other identity no<span class="req">&nbsp;* </span></label>
					<s:textfield placeholder="Enter OtherId number" cssClass="form-control" name="appForm.appOtherIdNumber"  id="appOtherIdNumber" value="%{appForm.appOtherIdNumber}"   maxlength="%{(appForm.appOtherId != null && appForm.appOtherId >0 && appForm.appOtherId == 166)?'10':((appForm.appOtherId != null && appForm.appOtherId >0 && (appForm.appOtherId == 165 || appForm.appOtherId == 167))?'20':'12' ) }" />
				</li>
			</s:if>
		</div>
		<s:if test="%{loanScenarioBean.chosenProductId!=@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
			<s:if test="appForm.appCoapplicantTypeId_1 <=0 && appForm.appCoapplicantTypeId_2 <= 0">
				<li>
					<label>Do you want to avail SBI life insurance? <span class="req">&nbsp;*</span></label>
					<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
						<s:if test="%{appForm.appInterestedSbiLifeInsurance!=null && appForm.appInterestedSbiLifeInsurance.equalsIgnoreCase('N')}">
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" value="Y">
								<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
							</div>	
							<div class="blue-radio blue-radio-danger">
								<input type="radio"  name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" value="N" checked="checked">
								<label for="appInterestedSbiLifeInsuranceNo">No </label>
							</div>
						</s:if>
						<s:else>
								<div class="blue-radio blue-radio-danger">
									<input type="radio" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" value="Y" checked="checked">
									<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
								</div>	
								<div class="blue-radio blue-radio-danger">
									<input type="radio"  name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" value="N">
									<label for="appInterestedSbiLifeInsuranceNo">No </label>
								</div>
						</s:else>
					</div>
			  	</li>
			  </s:if>
		</s:if>
	</ul>
		
<div class="clearfix"></div>
<s:if test="%{quote.loanQuoteResidentTypeId!=1}"></s:if>


<s:if test="appForm.appCoapplicantTypeId_1>0">
	<div class="clearfix"></div>
	<h3><span class="flt">Co-applicant 1 : details</span>
		<div class="prm-chk-div">
		 	<input type="checkbox" value="<s:property value="%{appForm.cloneCoapplicantAddress1==null?'false':'true'}"/>"  id="cloneCoapplicant1Address" name="appForm.cloneCoapplicantAddress1" <s:property value="%{appForm.cloneCoapplicantAddress1==true?'checked=\"checked\"':''}"/> class="blue-css-checkbox">
		 	<label class="label-content" for="cloneCoapplicant1Address" style="line-height:17px; font-size:11px;">Address same as for applicant</label>
		</div> 
	</h3>
	<div style="clear:both;"></div>
     <ul class="form-section">
 		<li>
			 <label>First name<b class="req">*</b></label>
			 <s:textfield name="appForm.appCoapplicantFirstName_1"  id="appCoapplicantFirstName1" value="%{appForm.appCoapplicantFirstName_1}"   maxlength="20"  onblur="ChangeCase(this);" 
			 cssClass="form-control"/>
		</li>
		<li>
			 <label>Middle name</label>
			 <s:textfield name="appForm.appCoapplicantMiddleName1"  id="appCoapplicantMiddleName1" value="%{appForm.appCoapplicantMiddleName1}"   maxlength="20"  onblur="ChangeCase(this);" 
			 cssClass="form-control"/>
		</li>
		<li>
			<label>Last name<b class="req">*</b></label>
			<s:textfield name="appForm.appCoapplicantLastName_1"  id="appCoapplicantLastName1" value="%{appForm.appCoapplicantLastName_1}"   maxlength="20"  onblur="ChangeCase(this);" 
			cssClass="form-control"/>
		</li>
	    <li>
		    <label>Address line 1<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
		    <s:textfield name="appForm.appCoapplicantAddress_1_1"  id="appCoapplicant1Address1" value="%{appForm.appCoapplicantAddress_1_1}"   maxlength="100" onblur="ChangeCase(this);"
		    cssClass=" form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onkeydown="return M.isAddressLine(event);"  />
	    </li>
		<li>
		    <label>Address line 2<b class="req">&nbsp;</b></label>
		    <s:textfield name="appForm.appCoapplicantAddress_2_1"  id="appCoapplicant1Address2" value="%{appForm.appCoapplicantAddress_2_1}"   maxlength="40" onblur="ChangeCase(this);"
		    cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onkeydown="return M.isAddressLine(event);" />
	    </li>
		<li>
		    <label>Landmark<b class="req">&nbsp;</b></label>
		    <s:textfield name="appForm.appCoapplicantLandmark_1"  id="appCoapplicant1Landmark" value="%{appForm.appCoapplicantLandmark_1}"   maxlength="40" onblur="ChangeCase(this);"
		    cssClass=" form-control %{appForm.cloneCoapplicantAddress1==true?'form-control ':'form-control'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onkeydown="return M.isAddressLine(event);" />
	    </li>
	    <div id="APPCOAPPLICANT1LOCATIONSTATE">
	    	<li>
				<label class="">State<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
				<div class="flat-field">
					<s:select name="appForm.appCoapplicantState_id_1" id="appCoapplicant1StateId" autocomplete="off" list="%{#states}"
					value="%{appForm.appCoapplicantState_id_1}" headerKey="0" headerValue="Select state" 
					cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'form-control ':'form-control'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onfocus="customOnFocus(this);"/>
				</div>
			</li>
	    </div>
	    <div id="APPCOAPPLICANT1LOCATIONCITY">
	    	<s:include value="/appNew/loan/homeloan/AppCoapplicant1CityId.jsp"></s:include>
	    </div>
		<div id="APPCOAPPLICANT1LOCATIONDISTRICT">
			<s:if test="%{appForm.appCoapplicantCity_id_1!=null && appForm.appCoapplicantCity_id_1 == 9999999}">
					<s:if test="%{appForm.appCoapplicantDistrictId1!=null && appForm.appCoapplicantDistrictId1 >0}">
						<s:include value="/appNew/loan/homeloan/AppCoapplicant1DistrictId.jsp"></s:include>
					</s:if>
	 		</s:if>
		</div>
      	<li>
	      <label>Pincode<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	      <s:textfield value="%{appForm.appCoapplicantPincode_1}" name="appForm.appCoapplicantPincode_1" id="appCoapplicant1Pincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"
	      cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields ':'disabledFields'} " disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"/>  
		</li>
	    <s:if test="%{appForm.appCoapplicantEmployerName1==1||appForm.appCoapplicantEmployerName1==2||appForm.appCoapplicantEmployerName1==3}">
			 <li>
				 <label>Employer name<span class="req">*</span></label>
				 <s:textfield name="appForm.appCoapplicantEmployerName1"  id="appCoapplicantEmployerName1" value="%{appForm.appCoapplicantEmployerName1}"   maxlength="20"
				 cssClass="form-control  %{appForm.cloneCoapplicantAddress1==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"/>
			 </li>
		 </s:if>
	</ul> 
</s:if>
		 
<s:if test="appForm.appCoapplicantTypeId_2>0">
<div class="clearfix"></div>
	<h3><span class="flt">Co-applicant 2 : details</span>
		<div class="prm-chk-div">
		 	<input type="checkbox" value="<s:property value="%{appForm.cloneCoapplicantAddress2==null?'false':'true'}"/>"  id="cloneCoapplicant2Address" name="appForm.cloneCoapplicantAddress2" <s:property value="%{appForm.cloneCoapplicantAddress2==true?'checked=\"checked\"':''}"/> class="blue-css-checkbox">
		 	<label class="label-content" for="cloneCoapplicant2Address" style="line-height:17px; font-size:11px;">Address same as for applicant</label>
		</div>
	</h3>
	<div style="clear:both;"></div>
	<ul class="form-section">
    <li>
	    <label>First name<b class="req">*</b></label>
	    <s:textfield name="appForm.appCoapplicantFirstName_2"  id="appCoapplicantFirstName2" value="%{appForm.appCoapplicantFirstName_2}"   maxlength="16"  onblur="ChangeCase(this);" 
	    cssClass="form-control" />
    </li>
 	<li>
	    <label>Middle name<b class="req">&nbsp;</b></label>
	    <s:textfield name="appForm.appCoapplicantMiddleName2"  id="appCoapplicantMiddleName2" value="%{appForm.appCoapplicantMiddleName2}"   maxlength="16"  onblur="ChangeCase(this);" 
	    cssClass="form-control" />
    </li>
	<li>
		<label>Last name<span class="req">*</span></label>
		<s:textfield name="appForm.appCoapplicantLastName_2"  id="appCoapplicantLastName2" value="%{appForm.appCoapplicantLastName_2}"   maxlength="20"   onblur="ChangeCase(this);"
		cssClass="form-control" />
	</li>
	<li>
		<label>Address line 1<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
		<s:textfield name="appForm.appCoapplicantAddress_1_2"  id="appCoapplicant2Address1" value="%{appForm.appCoapplicantAddress_1_2}"   maxlength="100" onblur="ChangeCase(this);"
		cssClass ="form-control %{appForm.cloneCoapplicantAddress2==true?'disabledFields ':'disabledFields'} " disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onkeydown="return M.isAddressLine(event);" />
	</li>
	<li>
	   <label>Address line 2<b class="req">&nbsp;</b></label>
	   <s:textfield name="appForm.appCoapplicantAddress_2_2"  id="appCoapplicant2Address2" value="%{appForm.appCoapplicantAddress_2_2}" maxlength="40" onblur="ChangeCase(this);"
	   cssClass=" form-control %{appForm.cloneCoapplicantAddress2==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onkeydown="return M.isAddressLine(event);" />
    </li>
	<li>
	   <label>Landmark<b class="req">&nbsp;</b></label>
	   <s:textfield name="appForm.appCoapplicantLandmark_2"  id="appCoapplicant2Landmark" value="%{appForm.appCoapplicantLandmark_2}" maxlength="40"  onblur="ChangeCase(this);"
	   cssClass="form-control %{appForm.cloneCoapplicantAddress2==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onkeydown="return M.isAddressLine(event);" />
   	</li>
	<li id="APPCOAPPLICANT2LOCATIONSTATE" class="">
		<label class="">State<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
		<div class="flat-field">
			<s:select name="appForm.appCoapplicantState_id_2" id="appCoapplicant2StateId" autocomplete="off" list="%{#states}" 
			value="%{appForm.appCoapplicantState_id_2}" headerKey="0" headerValue="Select state" 
			cssClass="form-control %{appForm.cloneCoapplicantAddress2==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onfocus="customOnFocus(this);"/>
	   </div>
	</li>
	<div id="APPCOAPPLICANT2LOCATIONCITY">
		<s:include value="/appNew/loan/homeloan/AppCoapplicant2CityId.jsp"></s:include>
	</div>
	<div id="APPCOAPPLICANT2LOCATIONDISTRICT">
		<s:if test="%{appForm.appCoapplicantCity_id_2!=null && appForm.appCoapplicantCity_id_2 == 9999999}">
			<s:if test="%{appForm.appCoapplicantDistrictId2!=null && appForm.appCoapplicantDistrictId2 >0}">
				<s:include value="/appNew/loan/homeloan/AppCoapplicant2DistrictId.jsp"></s:include>
			</s:if>
		</s:if>
	</div>
	<li>
        <label>Pincode<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
        <s:textfield value="%{appForm.appCoapplicantPincode_2}" name="appForm.appCoapplicantPincode_2" id="appCoapplicant2Pincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"
        cssClass="form-control %{appForm.cloneCoapplicantAddress2==true?'disabledFields ':'disabledFields'} " disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}"/>  
	</li>
    <s:if test="%{appForm.appCoapplicantEmployerName2==1||appForm.appCoapplicantEmployerName2==2||appForm.appCoapplicantEmployerName2==3}">
		 <li>
			<label>Employer name<b class="req">*</b></label>
			<s:textfield name="appForm.appCoapplicantEmployerName2"  id="appCoapplicantEmployerName2" value="%{appForm.appCoapplicantEmployerName2}" cssClass="form-control"  maxlength="20" />
		 </li>
   </s:if>
 </ul>
 </s:if>
	<ul class="form-section" id="5">
		<s:if test="appForm.appCoapplicantTypeId_1 >0 || appForm.appCoapplicantTypeId_2 > 0">
			<li>
				<label>Do you want to avail SBI life insurance?<span class="req">*</span></label>
					<div class="col-xs-12 mrgt-10-10">
					<s:if test="%{appForm.appInterestedSbiLifeInsurance!=null && appForm.appInterestedSbiLifeInsurance.equalsIgnoreCase('N')}">
						<div class="blue-radio blue-radio-danger">
							<input type="radio" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" value="Y">
							<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
						</div>	
						<div class="blue-radio blue-radio-danger">
							<input type="radio"  name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" value="N" checked="checked">
							<label for="appInterestedSbiLifeInsuranceNo">No </label>
						</div>
					</s:if>
					<s:else>
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" value="Y" checked="checked">
								<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
							</div>	
							<div class="blue-radio blue-radio-danger">
								<input type="radio"  name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" value="N">
								<label for="appInterestedSbiLifeInsuranceNo">No </label>
							</div>
					</s:else>
				</div>	
			</li>
		</s:if>
	</ul>
	<div class="clearfix"></div>
	<div id="termsAndConditionThird" class="sbi-trms-div sbi-trms-div-third">
		<ul class="form-section">
			<li class="full-width"><s:if test="%{isDsrPage=='false'}">
					<div class="trms-section">
						<input type="checkbox" class="blue-css-checkbox" name="terms_conditions_check" id="terms_conditions_check" value="on">
						<label for="terms_conditions_check" class="label-content">
							I have read the terms &amp;
							conditions and agree to the terms therein, I also authorise the
							bank and/or its representatives to verify any information
							contained in the application or otherwise from any source
							whatsoever at their sole discretion at your office/residence
							and/or contact you and/or your family members and/or your
							employer/banker/credit bureau/UIDAI/RBI and/or any third party as
							they deem necessary.
							<span><b class="req">*</b></span>
						</label>
					</div>
				</s:if> 
				<div class="qt-btn-div flr mrgt-10 m-100">
					<input type="submit" class="submit-btn" name="submitApplication" id="submitApplication" value="Submit Application">
				</div>
			</li>
		</ul>
	</div>
</form>






<%--END --%>

<div id="htmlappCityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppCityId.jsp"></s:include>
</div>
<div id="htmlappDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppDistrictId.jsp"></s:include>
</div>
<div id="htmlappPermanentCityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppPermanentCityId.jsp"></s:include>
</div>
<div id="htmlappPermanentDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppPermanentDistrictId.jsp"></s:include>
</div>

<div id="htmlappPropertyCityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppPropertyCityId.jsp"></s:include>
</div>
<div id="htmlappPropertyDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppPropertyDistrictId.jsp"></s:include>
</div>
<div id="htmlappOfficeCityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppOfficeCityId.jsp"></s:include>
</div>
<div id="htmlappOfficeDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppOfficeDistrictId.jsp"></s:include>
</div>
<div id="htmlappCoapplicant1CityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppCoapplicant1CityId.jsp"></s:include>
</div>
<div id="htmlappCoapplicant1DistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppCoapplicant1DistrictId.jsp"></s:include>
</div>
<div id="htmlappCoapplicant2CityId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppCoapplicant2CityId.jsp"></s:include>
</div>
<div id="htmlappCoapplicant2DistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppCoapplicant2DistrictId.jsp"></s:include>
</div>
<s:include value="/appNew/common/CommonOTP.jsp"></s:include>





<script type="text/javascript">
	var isOfferPage= false;
	function fetchJsonData(){
		if(ajaxPostUrl=='home-loan' || ajaxPostUrl=='home-loan-dsr'){
			jsonJSArray = '<s:property value="%{@com.mintstreet.loan.homeloan.action.HomeLoanAction@jsonJSArray3HomeLoan}"/>';
		}else{
			jsonJSArray = '<s:property value="%{@com.mintstreet.loan.homeloan.action.HomeTopupLoanAction@jsonJSArray3HomeLoanTopup}"/>';
		}
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	apply_currency_form(document.homeloancriteriaform);

	jQuery("p.Rs").each(function(){
		var placeholder = jQuery(this).text();
		var strip_commas = placeholder.replace(/,/g, "");
		strip_commas = Number(strip_commas).toPrecision();
		strip_commas = strip_commas.replace(".0","");
		jQuery(this).text(M.moneyFormat(strip_commas));
	});
	<s:if test="%{isDsrPage=='false'}">
		<s:if test="%{#session.bankLMSUser==null}">
			<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N')}">
				dropOffPopupStatus=1;
			</s:if>
		</s:if>
	</s:if>
	var fieldmaxlength;
	<s:if test="%{(appForm.appOtherId != null && appForm.appOtherId >0 && appForm.appOtherId==166)}">
		fieldmaxlength=10
	</s:if>
</script>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>		
</s:if>
<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local' && @com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='uat_rp'}">
	<%-- <script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081707&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script> --%>
	<s:if test="%{isDsrPage=='false'}">
	<!-- Facebook Pixel Code -->
	<script>
		fbq('trackCustom', 'HomeLoanDetails');
	</script>
	<!-- DO NOT MODIFY -->
	<!-- End Facebook Pixel Code -->
	</s:if>
</s:if>

<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.home_loan_third_page.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script> --%>
<script type="text/javascript">
	setTimeout(function(){
		$(".m-hm-prd").fadeOut('slow');
	},3000 );
	
<!-- Ruchita Start -->
	$( document ).ready(function() {
   		console.log('verified flag :: ' + $('#appMobileVerifiedCheck').val());
		var errorMessageText = $('#errorOTPMsg').text();
	        if (errorMessageText == 'Alternate Mobile cannot be same as Mobile number.') {
            	document.getElementById("changeAlternateNum").style.display = "block";
       		}
	});

    function validateAltNum() {
    	
    	return validateAltNumVerify();

    	/*var	checkval=document.getElementById('alternateMobileNumber').value;
		if(checkval.length > 6 && checkval.length < 16)
			{
			$("#inalterror").html(""); 
			  return true;
			}
		else if(checkval.length > 0 && checkval.length < 6 )
			{
			return false;
			 $("#inalterror").html("Invalid mobile number please provide valid mobile number  ");
			}
		else{
			
			$("#inalterror").html(""); 
			 return true;
			 }*/
            }

	function validateAltNumVerify() {
	<!-- 	alert('validateAltNumVerify called');-->
		<!--    if (document.getElementById("alternateUrl").style.display == "block" &&  mob_no_len.length >0 && mob_no_len.length <6) -->
	        if (document.getElementById("alternateUrl").style.display == "block") {          <!--created  code by hakeem 9 sep 22  -->
	        	$("#alternateUrlVal").val('1');
                $("#inalterror").html("Please verify Alternate Mobile Number");
                return false;
	        } else {
		        <!--		alert('validateAltNumVerify else called');-->
				$("#alternateUrlVal").val('0');
				$("#inalterror").html("");
				return true;
         	}
	}

	function showlink(){

	   var mainMobile = document.getElementById('mobile').value;
		
		
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

	   	    console.log('appMobileVerified:::' + $("#appMobileVerifiedCheck").val());
	   	    //if ($("#appMobileVerified").val() == 'Y') {
	   	    	console.log('open popup condition');
	   	    	openPopups('OTP1','1');
	   	    //}
			hideAlternateDiv();
    	} else {
    		 $("#inalterror").html(" Please valid ISD Code for alternate mobile number");
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
<!-- End -->

<!-- Ruchita Start -->
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
<!-- End -->


<!-- Ruchita Start -->
 <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloanOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<!-- End -->


