<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="htmlLoanCategoryId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/LoanCategoryId.jsp"></s:include>
</div>
<div id="htmlPurchaseOfVehicle" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/PurchaseOfVehicle.jsp"></s:include>
</div>
<div id="htmlLoanPurposeId7" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/LoanPurposeId7.jsp"></s:include>
</div>
<div id="htmlLoanPurposeId8" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/LoanPurposeId8.jsp"></s:include>
</div>
<div id="htmlVehiclePurchasedYes" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/VehiclePurchasedYes.jsp"></s:include>
</div>

<div id="htmlVarientDealerPrice" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/VarientDealerPrice.jsp"></s:include>
</div>

<div id="htmlVarientExshowroomPrice" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/VarientExshowroomPrice.jsp"></s:include>
</div>

<div id="htmlLoanCategoryId10" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/LoanCategoryId10.jsp"></s:include>
</div>

<div id="htmlState" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantState.jsp"></s:include>
</div>

<div id="htmlCountryRegion" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantCountryRegion.jsp"></s:include>
</div>

  <div id="htmlCoAppOtherCountry" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/HtmlCoAppOtherCountry.jsp"></s:include>
</div>  

<div id="htmlcityId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantCity.jsp"></s:include>
</div>

<div id="htmldistrictId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantDistrict.jsp"></s:include>
</div>

<div id="htmlbranchId" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantBranch.jsp"></s:include>
</div>

<div id="htmlResidanceType" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/ApplicantResidanceType.jsp"></s:include>
</div>

<div id="htmlNRIMobileNo" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/NRIMobileNo.jsp"></s:include>
</div>

<div id="htmlIndianMobileNo" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/IndianMobileNo.jsp"></s:include>
</div>

<div id="htmlEmpSalariedTypes" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlSalariedEmpTypes.jsp"></s:include>
</div>

<div id="htmlSalaryAccountWithSbi" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlSalaryAccountWithSbi.jsp"></s:include>
</div>

<div id="htmltypeOfSalaryPackage" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlTypeOfSalaryPackage.jsp"></s:include>
</div>

<div id="htmlSelfBusiness" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlSelfEmpBusiness.jsp"></s:include>
</div>

<div id="htmlAgriculturist" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlAgriculturist.jsp"></s:include>
</div>

<div id="htmlRetiredPensioner" class=hide>
	<s:include value="/appNew/loan/autoloan/HtmlRetiredPensioner.jsp"></s:include>
</div>
<div id="htmlAutoLoanCoApplicant1Data" class="hide">
	<s:include value="/appNew/loan/autoloan/AutoLoanCoApplicant.jsp"></s:include>
</div>
<div id="htmlCoApplicant1" class="hide">
	<s:include value="/appNew/loan/autoloan/HtmlCoAppSalaried.jsp"></s:include>
</div>
<div id="htmlCoAppBusinessman" class="hide">
	<s:include value="/appNew/loan/autoloan/HtmlCoAppBusinessman.jsp"></s:include>
</div>
<div id="htmlCoAppAgriculture" class="hide">
	<s:include value="/appNew/loan/autoloan/HtmlCoAppAgriculture.jsp"></s:include>
</div>
<div id="htmlCoAppPensioner" class="hide">
	<s:include value="/appNew/loan/autoloan/HtmlCoAppPensioner.jsp"></s:include>
</div>
<div id="htmlCoAppHomemaker" class="hide">
	<s:include value="/appNew/loan/autoloan/HtmlCoAppHomemaker.jsp"></s:include>
</div>
<div id="htmlCarType" class="hide">
	<s:include value="/appNew/loan/autoloan/includes/SuvCarType.jsp"></s:include>
</div>
<s:include value="/appNew/status/ApplicationStatus.jsp"></s:include>
<s:include value="/appNew/common/ApproveProjects.jsp"></s:include>
<s:include value="/appNew/common/CommonCallback.jsp"></s:include>
<script type="text/javascript">
function fetchJsonData(){
	jsonJSArray = '<s:property value="%{jsonJSArray1AutoLoan}"/>';
	jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
	jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
	jsonObject = jQuery.parseJSON(jsonJSArray);
	return jsonObject;
}

jQuery(document).ready(function(){
	apply_currency_form(document.autoloanform);
	$("#content-1").mCustomScrollbar({
		//autoHideScrollbar:true,
		theme:"rounded",
		scrollInertia:5,
		callbacks:{
            onScroll:function(){ 
                $("#date_of_birth, #co_date_of_birth1").datepicker('place');
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
	
	$("body").on("focus","input:not(#date_of_birth, #co_date_of_birth1),select",function(){
		 $("#content-1").mCustomScrollbar('scrollTo',$(this).prev());
	})
	
	dob_date_picker('autoloanform','date_of_birth');
	dob_date_picker('autoloanform','co_date_of_birth1');
});
</script>
