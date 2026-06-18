<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:hidden id="instituteCat" name="quote.loanQuoteInstituteCat" value="%{quote.loanQuoteInstituteCat}"/>
<s:set var="needToDisable" value="true" />
<s:if test="%{appElTypeId==20}">
	<s:set var="needToDisable" value="false" />
</s:if>
	
	<s:if test="%{isDsrPage=='false'}">
	<s:set var="br" value="%{'<br>'}"/>
	<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
		<s:set var="Elcss" value="%{'fade disabledFields'}"/>
		<s:set var="disabledElcss" value="%{'true'}"/>
	</s:if>
	<s:else>
		<s:set var="disabledElcss" value="%{'false'}"/>
	</s:else>
</s:if>
<%-- <s:set var="needToDisable" value="true"/>
<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
	<s:set var="needToDisable" value="false"/>
</s:if> --%>
	<div class="clearfix"></div>
	</br>
	<h3>PURPOSE OF LOAN</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/educationloan/LoanPurposeType.jsp"></s:include>
	</ul>
	
	<div class="clearfix"></div>
	</br>
	<h3>COURSE DETAILS</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/educationloan/CourseDetails.jsp"></s:include>
	</ul>
	
	<s:if test="%{#needToDisable==true}">
	<div class="clearfix"></div>
	</br>
	<h3>COURSE FEE</h3> 
    <ul class="form-section">
		<div id="COURSEFEECONTAINER">
			<s:include value="/appNew/loan/educationloan/CourseFee.jsp"></s:include>
		</div>	
	</ul>
	</s:if>
	
<div class="clearfix"></div>
</br>
<h3>APPLICANT DETAILS </h3>
 <ul class="form-section">   
		<s:include value="/appNew/loan/educationloan/ApplicantDetails.jsp"></s:include>
			<div id="APPLICANTSTATE">
				<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentState.jsp"></s:include>
			</div>
			<div id="LOCATIONCITY">
				<s:if test="%{quote!=null && quote.loanQuoteResidentStateId!=null && quote.loanQuoteResidentStateId>0}">
					<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentCity.jsp"></s:include>
				</s:if>
			</div>
			<div id="LOCATIONDISTRICT">
				<s:if test="%{quote!=null &&  quote.loanQuoteResidentCityId!=null && quote.loanQuoteResidentCityId == 9999999 }">
					<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentDistrict.jsp"></s:include>
				</s:if>
			</div>
			<div id="PREFERREDLOCATION">
				<s:if test="%{quote!=null &&  quote.loanQuoteResidentCityId!=null && quote.loanQuoteResidentCityId>0 && appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_SCHOLAR}">
					<s:include value="/appNew/loan/educationloan/includes/EducationPreferredLocation.jsp"></s:include>
				</s:if>
			</div>
			<div id="LOCATIONBRANCH">
				<s:if test="%{quote!=null &&  quote.loanQuotePreferredLocation!=null && quote.loanQuotePreferredLocation>0 && appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_SCHOLAR}">
					<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentBranch.jsp"></s:include>
				</s:if>
				<s:elseif test="%{quote!=null &&  quote.loanQuoteResidentCityId!=null && quote.loanQuoteResidentCityId>0 && appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
					<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentBranch.jsp"></s:include>
				</s:elseif>
			</div>
			<s:if test="%{#needToDisable==false}">
				<div id="divColletralType">
					<s:include value="/appNew/loan/educationloan/includes/TakeoverColletralType.jsp"></s:include>
				</div>
			</s:if> 
		</ul> 
		
		<s:if test="%{#needToDisable==true}">
			<div class="clearfix"></div>
			</br>
			<h3>Co-APPLICANT DETAILS</h3>
			 <ul class="form-section">
		 		<s:include value="/appNew/loan/educationloan/CoAppRelationship.jsp"></s:include>
		 			<div id ="EXISTINGEMPLOYEESBI">
			 			<s:if test="%{quote.loanQuoteCoapplicantFirstRelationshipId!=null && quote.loanQuoteCoapplicantFirstRelationshipId == 11}">
			 				<s:include value="/appNew/loan/educationloan/includes/ExistingEmployee.jsp"></s:include>
			 			</s:if>
		 		 	</div>
	 		</ul> 
		</s:if>
	
	<div class="clearfix"></div>
	</br>
	<h3>CONTACT DETAILS</h3>
	<ul class="form-section">
	 	<s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
			<div id ="divApplyingFrom">
				<s:include value="/appNew/loan/educationloan/includes/ApplyingFormTakeOver.jsp"></s:include>
			</div>
		</s:if>
		<s:else>
			<s:include value="/appNew/loan/educationloan/EducationContactDetails.jsp"></s:include>
		</s:else>
	</ul>
		
	<s:if test="%{appSeqId==null}">
	<li>
	  	<div class="clearfix"></div>
	</br>
		
			<h3>IDENTIFY YOURSELF</h3>
	
			<ul class="form-section captcha">
				<li>
					<label>Copy text into input box<span class="req">*</span></label>
    				 <img id="captchaFirstPage" name="EducationcapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('captchaFirstPage');">
    				 <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
    				  <input type="text" class="form-control" id="captcha" name="captcha" value="" maxlength="6" placeholder="Captcha" autocomplete="off"/>
				</li>
		 </ul>
   </li>
   </s:if>
	<div class="clearfix"></div>
	<div id="termsAndConditionFirst" class="sbi-trms-div">
		<ul class="form-section">
			<li class="full-width">
				<div class="trms-section">
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide"  value="on">
					
					<label for="infoprovide" class="label-content">
						<s:property escapeHtml="false" value="%{beanList.consentEducationLoanNTB}" />
						&nbsp;<b class="req">*</b>
					</label>	
				</div>
				<div class="qt-btn-div flr mrgt-10 m-100">
					<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
					<s:if test="%{appForm==null || appForm.appMobileVerified.equalsIgnoreCase('N')}">
						<div class="txt-center"><a class="callBackProduct" href="javascript:void(0);" onclick="javascript:showProductCallback(4);">Get a call back</a></div>
					</s:if>
				</div>
			 </li>
		 </ul>
	</div>
