<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<div id="htmlLoanPurpose" class="hide">
		<s:include value="/appNew/loan/educationloan/LoanPurposeType.jsp"></s:include>
	</div>
	<div id="htmlCourseDetailsId" class="hide">
		<s:include value="/appNew/loan/educationloan/CourseDetails.jsp"></s:include>
	</div>
	<div id="htmlCourseInstituteUniversity" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/CourseTypeDetails.jsp"></s:include>
	</div>
	<div id="htmlTravelExpenses" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/TravelExpenses.jsp"></s:include>
	</div>
	<div id="htmlState" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentState.jsp"></s:include>
	</div>
	<div id="htmlresidentCityId" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentCity.jsp"></s:include>
	</div>
	 <div id="htmldistrictId" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentDistrict.jsp"></s:include>
	</div> 
	<div id="htmlbranchId" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentBranch.jsp"></s:include>
	</div>
	 <div id="htmlpreferredLocation" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/EducationPreferredLocation.jsp"></s:include>
	</div>
	 <div id="htmldivocollateral" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/EducationCollectralSecurity.jsp"></s:include>
	</div>
	<div id="htmlExistingEmployee" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ExistingEmployee.jsp"></s:include>
	</div>
	
	<div id="htmlIndianMobileNo" class="hide">
		 <s:include value="/appNew/loan/educationloan/EducationContactDetails.jsp"></s:include>
	</div>
	<div id="htmlOTHERIDNUMBER" class="hide">
		 <s:include value="/appNew/loan/educationloan/includes/EiOtherIdProof.jsp"></s:include>
	</div>
	 <div id="htmlLOANPURPOSE1" class="hide">
		 <s:include value="/appNew/loan/educationloan/includes/TakeoverloanPurpose.jsp"></s:include>
	</div>
	<%-- <div id="divColletralType">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverColletralType.jsp"></s:include>
	</div>  --%>
	
	<div id="htmlresidentCityId" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplicantResidentCity.jsp"></s:include>
	</div>
	
	<%-- <div id="htmlAPPLYINGFORM" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/ApplyingFormTakeOver.jsp"></s:include>
	</div> --%>
	 <div id="htmlRESIDENTTYPETAKECONTACT" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverResidanceApply.jsp"></s:include>
	</div>	
	 <%-- <div id="htmlRESIDANCETYPEVALUE">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverNRIMobileNo.jsp"></s:include>
	</div>	
	 --%>
	 <div id="htmlNRIMobileNo" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverNRIMobileNo.jsp"></s:include>
	</div>
	
	<div id="htmlTakeoverIndianMobileNo" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverIndianMobileNo.jsp"></s:include>
	</div>
	<div id="htmlresidentValue" class="hide">
		<s:include value="/appNew/loan/educationloan/includes/TakeoverCountry.jsp"></s:include>
	</div>
	
	<s:include value="/appNew/status/ApplicationStatus.jsp"></s:include>
	<s:include value="/appNew/common/ApproveProjects.jsp"></s:include>
	<s:include value="/appNew/common/CommonCallback.jsp"></s:include>
	
<script type="text/javascript">
/* jQuery(document).ready(function(){
 apply_currency_form(document.educationloanform);
	$("#content-1").mCustomScrollbar({
			//autoHideScrollbar:true,
				theme:"rounded",
			scrollInertia:5,
			mouseWheel:{scrollAmount:30},
			scrollButtons:{
				enable:true
			},
			 advanced:{
				updateOnBrowserResize:true,
				updateOnContentResize:true,
				autoScrollOnFocus:true,
				autoExpanHorizontalScroll:true 
			}
		});
		
	 }); */
	
function fetchJsonData(){
	jsonJSArray = '<s:property value="%{jsonJSArray1EducationLoan}"/>';
	jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
	jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
	jsonObject = jQuery.parseJSON(jsonJSArray);
	return jsonObject;
}

$("body").on("focus","input,select",function(){
	 $("#content-1").mCustomScrollbar('scrollTo',$(this).prev());
})
jQuery(document).ready(function(){
	apply_currency_form(document.educationloanform);
	$("#content-1").mCustomScrollbar({
		theme:"rounded",
		scrollInertia:5,
		callbacks:{
            onScroll:function(){ 
                $("#dateOfBirth").datepicker('place');
             }
		},
		mouseWheel:{scrollAmount:30},
		scrollButtons:{
			enable:true
		},
		 advanced:{
			updateOnBrowserResize:true,
			updateOnContentResize:true,
			autoScrollOnFocus:true,
			autoExpanHorizontalScroll:true 
		}
		
	});
	$("body").on("focus","input:not(#dateOfBirth,[type='checkbox']),select",function(){
		 $("#content-1").mCustomScrollbar('scrollTo',$(this).prev());
	})
	
	dob_date_picker('educationloanform','dateOfBirth');
	
	$('body').bind('cut copy paste',function(e){
		e.preventDefault();
	}); 
	autoSuggestionCourseName('courseName');
	autoSuggestionInstituteName('instituteName');
	auto_suggestion_university('universityName');
	
	<s:if test="%{educationLoanPage==1}">
		<s:if test="%{quote.loanQuoteSTDOfLoanRepayment>quote.loanQuoteSTDOfAppliedCourse}">
			autoSuggestionPreviousInstituteName('previousInstituteName');
		</s:if>
		//apply_currency_form(document.educationloanform);
	</s:if>
});
/* <s:if test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_SCHOLAR
	|| appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_EDVANTAGE ||
	|| appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_NORMAL}">
	$("#educationLoanPurposeDetails10").html($('#htmlloanQuoteSelectedLoanPurposeId10').html());
	$('#loanPurpose').attr('disabled',true);
</s:if>
<s:elseif test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_NORMAL}">
$("#educationLoanPurposeDetails24").html($('#htmlloanQuoteSelectedLoanPurposeId24').html());
</s:elseif> */
<s:if test="%{ajaxPostUrl==@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION
	|| ajaxPostUrl==@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION }">
	$('#loanPurpose').val(10);
</s:if>
<s:elseif test="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
	$("#LOANPURPOSE1").html($('#htmlLOANPURPOSE1').html());
	$('#loanPurpose').val(24);
	//$('#loanPurpose').attr('disabled',true);
	$('#loanPurpose').addClass('disabledFields');
</s:elseif>
</script>
