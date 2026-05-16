<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
	var loanPurposeLinks="";
	isInSession=false;

	<s:if test="%{appSeqId!=null}">
		isInSession=true;
	</s:if>
	$(document).on("keydown", function (e) {
	    if (e.which === 8 && !$(e.target).is("input:not([readonly]):not([type=radio]):not([type=checkbox]), textarea, [contentEditable], [contentEditable=true]")) {
	        e.preventDefault();
	    }
	});
	jQuery(document).ready(function(){
		jQuery("#loanPurpose").focus();
			selectedIndex=0;
			<s:if test="%{autoLoanPage>0}">
				initLoanJSONArray = '<s:property value="%{initLoanJSONArrayAutoLoan}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
				$.each(json, function(elementIds, elementValuess){
					$.each(elementValuess, function(elementId, elementValues){
						if(!isInSession){
							if(elementId=='makeCar'){
								populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
								disableById('makeCar', true);
							}else{
								populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
							}
						}
						if(elementId=='loanWithBankId'){
							bankWithIdElementData =elementValues;
						}else if(elementId=='preferredState'){
							preferredStateData=elementValues;
						}else if(elementId=='residentCountries'){
							residentCountries=elementValues;
						}else if(elementId=='dealerName'){
							dealerName=elementValues;
						}else if(elementId=='relationShipNRI'){
							relationshipsNRI=elementValues;
						}else if(elementId=='relationShipIndia'){
							relationshipsIndia=elementValues;
						}else if(elementId=='loanCategory'){
							loanCategoryAuto=elementValues;
						}else if(elementId=='gender'){
							genderData=elementValues;
						}else if(elementId=='industries'){
							industriesData=elementValues;
						}else if(elementId=='salaryPackages'){
							salaryPackageData=elementValues;
						}else if(elementId=='loanPurposeLinks'){
	    					loanPurposeLinks=elementValues;
	    				}
						
					});
				});
				if(isInSession){
					apply_currency_form(document.autoloanform);
				}
				totalAutoPrice();
				if(!isInSession){
					addRemoveCarDetails(true, false);
				}
				if(isDsrPage){
					jQuery("#423").children("label").html("E-mail");
				}else{
					jQuery("#726").remove();
				}
				if(isDsrPage){
					var lmsIntermediaryRelation='<s:property value="%{#session.lmsIntermediaryRelation}"/>';
					if(lmsIntermediaryRelation!="null"){
						isRegisteredDealer=true;
					}else{
						isRegisteredDealer=false;							
					}
				}else{
					isRegisteredDealer=false;
				}
			</s:if>
			<s:if test="%{personalLoanPage>0}">
				initLoanJSONArray = '<s:property value="%{initLoanJSONArrayPersonalLoan}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
				$.each(json, function(elementIds, elementValuess){
	    			$.each(elementValuess, function(elementId, elementValues){
	    				if(!isInSession){
	    					populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
	    				}
	    				if(elementId=='loanWithBankId'){
	    					bankWithIdElementData =elementValues;
	    				}else if(elementId=='preferredStateData'){
	    					preferredStateData=elementValues;
	    				}else if(elementId=='residentCountries'){
	    					residentCountries=elementValues;
	    				}else if(elementId=='residentCities'){
	    					residentCities=elementValues;
	    				}else if(elementId=='countryRegion'){
	    					countryRegionData=elementValues;
	    				}else if(elementId=='residingCity'){
	    					residingCityData=elementValues;
	    				}else if(elementId=='employmentType'){
	    					employmentTypeData=elementValues;
	    				}else if(elementId=='industryTypeid'){
	    					industryTypeData=elementValues;
	    				}else if(elementId=='industryTypeDataNOTSARAL'){
	    					industryTypeDataNOTSARAL=elementValues;
	    				}else if(elementId=='gender'){
	    					genderData=elementValues;
	    				}else if(elementId=='profession'){
	    					professionData=elementValues;
	    				}else if(elementId=='salaryPackages'){
							salaryPackageData=elementValues;
	    				}else if(elementId=='loanPurposeLinks'){
	    					loanPurposeLinks=elementValues;
	    				}else if(elementId=='relationShipIndia'){
							relationshipsIndia=elementValues;
						}
	    			});
				});
				if(isDsrPage){
					jQuery("#628").children("label").html("E-mail");
				}else{
					jQuery("#727").remove();
				}
			</s:if>
			
			 <s:if test="%{educationLoanPage>0}">
				initLoanJSONArray = '<s:property value="%{initLoanJSONArrayEducationLoan}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
				$.each(json, function(elementIds, elementValuess){
	    			$.each(elementValuess, function(elementId, elementValues){
	    				if(!isInSession){
	    					populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
	    				}
	    				if(elementId=='residentStateId'){
	    					preferredStateData=elementValues;
	    				}else if(elementId=='residentCountries'){
	    					residentCountries=elementValues;
	    				}else if(elementId=='residentCities'){
	    					residentCities=elementValues;
	    				}else if(elementId=='gender'){
	    					genderData=elementValues;
	    				}else if(elementId=='loanPurposeLinks'){
	    					loanPurposeLinks=elementValues;
	    				}else if(elementId=='courseTypeIndian'){
	    					courseTypeIndian=elementValues;
	    				}else if(elementId=='courseTypeAbroad'){
	    					courseTypeAbroad=elementValues;
	    				}else if(elementId=='instituteScholarList'){
	    					instituteScholarList=elementValues;
	    				}
	    			});
				});
				/* if(!isInSession && isDsrPage){
		    		disableById("courseType", true);
		    	} */ 
				<s:if test="%{toolTipJSArray!= null && toolTipJSArray!='' && toolTipJSArray!='[]'}">
					toolTipFromDB('<s:property value="%{toolTipJSArray}"/>');
				</s:if>
				/* if(isDsrPage){
					jQuery("#472").children("label").html('E-mail');
					jQuery("#728").children("label").html('First Name<b class="req">*</b>');
				}else{ */
					jQuery("#728").remove();
				//}
			</s:if> 
			<s:if test="%{homeLoanPage>0 || homeTopUpLoanPage>0}">
				initLoanJSONArray = '<s:property value="%{initLoanJSONArrayHomeLoan}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
				$.each(json, function(elementIds, elementValuess){
					$.each(elementValuess, function(elementId, elementValues){
						if(!isInSession){
							populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
						}
						if(elementId=='loanWithBankId'){
							bankWithIdElementData =elementValues;
						}else if(elementId=='propertyType'){
							propertyTypeData =elementValues;
						}else if(elementId=='preferredStateDataRACPC'){
							preferredStateDataRACPC=elementValues;
						}else if(elementId=='preferredState'){
							preferredStateData=elementValues;
						}else if(elementId=='residingCountry'){
							residentCountries=elementValues;
						}else if(elementId=='gender'){
							genderData=elementValues;
						}else if(elementId=='industries'){
							industriesData=elementValues;
						}else if(elementId=='salaryPackages'){
							salaryPackageData=elementValues;
						}else if(elementId=='relationShipIndia'){
							relationshiptData=elementValues;
						}else if(elementId=='projectData'){
							projectData=elementValues;
						}else if(elementId=='loanPurposeLinks'){
	    					loanPurposeLinks=elementValues;
						}
					});
				});
				if(!isInSession){
					jQuery("#employmentType").attr("disabled", true);
				}
				if(isDsrPage){
					var lmsIntermediaryRelation='<s:property value="%{#session.lmsIntermediaryRelation}"/>';
					if(lmsIntermediaryRelation!="null"){
						isRegisteredBuilder=true;
						jQuery("#309").show();
						jQuery("#310").show();
						jQuery("#311").show();
						jQuery("#312").show();
					}else{
						isRegisteredBuilder=false;
					}
					jQuery("#309").show();
					jQuery("#310").show();
					jQuery("#311").show();
					jQuery("#312").show();
				}else{
					isRegisteredBuilder=false;
				 	jQuery("#309").remove();
					jQuery("#310").remove();
					jQuery("#311").remove();
					jQuery("#312").remove();
				}
			</s:if>
			
			
			<s:if test="%{agriLoanPage>0}">
				initLoanJSONArray = '<s:property value="%{initLoanJSONArrayAgriLoan}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
				$.each(json, function(elementIds, elementValuess){
					$.each(elementValuess, function(elementId, elementValues){
						if(!isInSession){
							populateDropdownFromJSONArrayList(elementId, elementValues, selectedIndex);
						}
						if(elementId=='state'){
							stateElementData =elementValues;
						}
					});
				});
				
				if(isDsrPage){
					var lmsIntermediaryRelation='<s:property value="%{#session.lmsIntermediaryRelation}"/>';
					if(lmsIntermediaryRelation!="null"){
						isRegisteredBuilder=true;
					}else{
						isRegisteredBuilder=false;
					}
				}else{
					isRegisteredBuilder=false;
				}
			</s:if>
		
			<s:if test="%{creditCardPage>0}">
				initLoanJSONArray = '<s:property value="%{jsonJSArray1CreditCard}"/>';
				initLoanJSONArray = initLoanJSONArray.replace(/%27/g, "\'");
				initLoanJSONArray = initLoanJSONArray.replace(/&quot;/g, "\"");
				json = jQuery.parseJSON(initLoanJSONArray);
				if(jQuery.isEmptyObject(json)){
					alert("Sorry for the inconvenience, Please refresh");
				}
			</s:if>
			if(!isInSession){
				if($("#loanPurpose option").length==2){
			  		$("#loanPurpose").val($("#loanPurpose option:eq(1)").val()).trigger("change");
			  	}
				if($("#loanCategory option").length==2){
			  		$("#loanCategory").val($("#loanCategory option:eq(1)").val()).trigger("change");
			  	}
			}
	 });
</script>
