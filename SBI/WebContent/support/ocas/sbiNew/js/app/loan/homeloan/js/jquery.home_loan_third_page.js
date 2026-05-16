applicantAddress = ["appAddress1","appAddress2","appAddressLandmark","appStateId","appCityId","appBranchId","appDistrictId","appPincode"];
permanentAddress = ["appPermanentAddress1","appPermanentAddress2","appPermanentAddressLandMark","appPermanentStateId","appPermanentCityId","appPermanentId","appPermanentDistrictId","appPermanentPincode","APPPERMANENTLOCATIONCITY","APPPERMANENTLOCATIONDISTRICT","appForm.appPermanentDistrictId"];
propertyAddress = ["appPropertyAddress1","appPropertyAddress2","appPropertyLandmark","appPropertyStateId","appPropertyCityId","appPropertyId","appPropertyDistrictId","appPropertyPincode"];
officeAddress = ["appOfficeAddress1","appOfficeAddress2","appOfficeLandmark","appOfficeStateId","appOfficeCityId","appOfficeBranchId","appOfficeDistrictId","appOfficePincode"];
coapplicant1Address = ["appCoapplicant1Address1","appCoapplicant1Address2","appCoapplicant1Landmark","appCoapplicant1StateId","appCoapplicant1CityId","appCoapplicant1BranchId","appCoapplicant1DistrictId","appCoapplicant1Pincode","APPCOAPPLICANT1LOCATIONCITY","APPCOAPPLICANT1LOCATIONDISTRICT","appForm.appCoapplicantDistrictId1"];
coapplicant2Address = ["appCoapplicant2Address1","appCoapplicant2Address2","appCoapplicant2Landmark","appCoapplicant2StateId","appCoapplicant2CityId","appCoapplicant2BranchId","appCoapplicant2DistrictId","appCoapplicant2Pincode","APPCOAPPLICANT2LOCATIONCITY","APPCOAPPLICANT2LOCATIONDISTRICT","appForm.appCoapplicantDistrictId2"];
enableApplicantAddressByCheckBoxIds =["clonePermanentAddress","cloneOfficeAddress","clonePropertyAddress","cloneCoapplicant1Address","cloneCoapplicant2Address","clonePropertyAddress"];

/**
 * function for disabling and applying readonly to main applicant address lines
 * check if any checkbox is checked 
 */
function disable_readonly_main_applicant(){
	if(!$("#clone_applicant_address_2").is(':checked') && !$("#clone_applicant_address_1").is(':checked')){
		changeToReadonly("appAddress1",false);
		changeToReadonly("appAddress2",false);
		changeToReadonly("appAddressLandmark",false);
		$("select#appDistrictId").removeAttr('disabled');
		changeToReadonly("appPincode",false);
		$("select#appStateId").removeAttr('disabled');
	}
}
$(document).ready(function(){
	$("#homeloancriteriaform").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        $(element).valid();
	    },
	    onblur:function (element) {
	        $(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
	        if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          	showMsg(message);    	
	          	hideMsg();
		        }
	    },
		/*groups:{
			joiningDate:"appForm.appCompanyJoiningYear appForm.appCompanyJoiningMonth",
			workExp:"appForm.appWorkExprerienceYear appForm.appWorkExprerienceMonth",
			phoneExt: "appForm.appOfficePhoneNumberExt appForm.appOfficePhoneNumber"
		},*/
		errorElement: "span",
		errorPlacement: function(error, element) {
			if(element.attr("type")=="checkbox"){
				return true;
			}else if(element.attr("name")=="appForm.appCompanyJoiningYear"){
				$("#appCompanyJoiningYear").closest('li').append(error.addClass('left'));
			}else if(element.attr("name")=="appForm.appCompanyJoiningMonth"){
				$("#appCompanyJoiningMonth").closest('li').append(error.addClass('right'));
			}else if(element.attr("name")=="appForm.appWorkExprerienceYear"){
				$("#appWorkExperienceYear").closest('li').append(error.addClass('left'));
			}else if(element.attr("name")=="appForm.appWorkExprerienceMonth"){
				$("#appWorkExperienceMonth").closest('li').append(error.addClass('right'));
			}else if(element.attr("name")=="appForm.appPanCardNo"){
				error.insertAfter(element);
			}else if(element.attr("name")=="appForm.appOfficePhoneNumber" || element.attr("name")=="appForm.appOfficePhoneStdCode"){
				error.insertAfter("#appOfficePhoneNumber");
			}else if(element.attr("name")=="terms_conditions_check"){
				return true;
			}else if($("#"+element.attr("id")).is("select")){
				error.insertAfter($(element).parent());
			}else{ 
				error.insertAfter(element);
			}
			if($("#"+element.attr("id")).is("select")){
				customOnFocusElement(element.attr("id"));
			}
		}
	});

	$("#third_date_of_birth").attr('readonly','readonly');
	$("#appGender").attr('disabled','disabled');
	
	//new code cic 
	$("#appStateId").attr('disabled','disabled');
	
	
	$("#appPanCardLater").click(function(){
		if($(this).is(':checked')){
			$("#appPanCardLater").val('true');
			if($("#appPanCardNo").hasClass('error')){
				$("#appPanCardNo").removeClass('error');
				$("#appPanCardNo").parent().find("span.error").remove();
			}
			$("#appPanCardNo").val('');
			$("#appPanCardNo").attr('disabled','disabled');
		}else{
			$("#appPanCardLater").val('false');
			$("#appPanCardNo").removeClass('disabledFields');
			$("#appPanCardNo").removeAttr('disabled');
		}		
	});
	
	jQuery('#submitApplication').unbind('click').bind("click",function(){
		$("span.error").each(function(){
			$(this).remove();
		});
		if(!workAndJoinMonthValidation()){
			return false;
		}
		remove_comma(document.homeloancriteriaform);
		jQuery("#fourthPageContent").html('');
    	jQuery("#fourthPageContent").hide();
    	removeDynamicLoader('submitApplication');
		addValidationsRules(document.homeloancriteriaform);
		if($("#homeloancriteriaform").valid()){
			if(!extraValidationThirdPage()){
	    		window.scrollTo(0, 170);
	    		return false;
	    	}
			$(".disabledFields").each(function(){
				$(this).removeAttr('disabled');
			});
	    	var paramData= getRequest('homeloancriteriaform', 3);
	    	if(!paramData){
	    		var errorMsg = "Sorry for the inconvenience, please try again later.";
	    		showMsg(errorMsg);
	    		return false;
	    	}
	    	var formdata = "params="+paramData;
	    	formdata +="&requestIndex=17&requestType=2";
	    	if(uiType!=undefined && uiType!=""){
	    		formdata += "&uiType="+uiType;
	    	}
			jQuery.ajax({
		        type:'post',
		        url: ajaxPostUrl,
		        data: formdata,
		        beforeSend:function(){
		        	showLoader();
				},
				complete:function(){
					hideLoader();
				},
		        success:function(htmlContent){
		        	$(".disabledFields").each(function(){
		    			$(this).attr('disabled','disabled');
		    		});
		        	removeDynamicLoader('submitApplication');
		        	if(htmlContent!=""){
		            	var stringMessage = [];
		            	stringMessage = htmlContent.split("|");
		        		if(stringMessage[0] == "error"){
		        			var error_msg=stringMessage[1];
		        			showMsg(error_msg);
		        			disableByFormAndName('homeloancriteriaform', stringMessage[2], false);
		        	    	var errorPosition = stringMessage[2];
		        			$("#appPanCardNo").removeClass('error');
							$("#appIdProof").removeClass('error');
							$("#appOtherIdNumber").removeClass('error');
		        			if (errorPosition!=undefined && errorPosition!="") {
								if (errorPosition==1) {
									$("#appPanCardNo").addClass('error');
									$("#appIdProof").addClass('error');
								}else if (errorPosition==2) {
									$("#appPanCardNo").addClass('error');
								}else if (errorPosition==3) {
								}else if (errorPosition==4) {
									$("#appOtherIdNumber").addClass('error');
								}else if (errorPosition==5) {
									$("#appIdProof").addClass('error');
								}else{
									if(stringMessage[2]){
				            			jQuery("select[name='"+stringMessage[2]+"']").val(0).addClass('error');
				            			jQuery("input[name='"+stringMessage[2]+"']").addClass('error').removeClass('fade').removeAttr('disabled');
				            		}
								}
							}
		        			return false;
		            	}else{
			            	if(isDsrPage){
			            		jQuery("#fourthPageContent").html(htmlContent);
			            		jQuery("#fourthPageContent").show();
			            		//calling ajax form call logs
		                		getCallLogDetails("msg-panel");
		                		
			            		jQuery("#subtn").hide();
				            	jQuery("#subtnChange").hide();
				            	jQuery("#editLoanQuote").hide();
				            	jQuery("#changeLoanQuote").hide();
				            	jQuery("#submitApplication").hide();
				            	$("#homeloancriteriaform input, #homeloancriteriaform select").attr('disabled',true);
			            		jQuery(".apply-small-btn").remove();
			            		jQuery(".emi-schedule-link").remove();
			            	}else{
			            		jQuery("#secondPageContent").html('');
			            		jQuery("#secondPageContent").hide();
			            		jQuery("#thirdPageContent").html('');
				            	jQuery("#thirdPageContent").hide();
				            	goToByScroll('msg-panel');
			            		jQuery("#fourthPageContent").html(htmlContent);
			            		jQuery("#fourthPageContent").show();
				            	$("#content-4, #content-left").mCustomScrollbar({
				        			theme:"rounded",
				        			scrollInertia:5,
				        			callbacks:{
				                        onScroll:function(){ 
				                            $("#appDocPickupDate").datepicker('place');
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
				            	$("#accordion").accordion();
			            	}
			            	if(BANK_ID == BANK_ID_SBI){
			            		// Google Conversion Tracking Code
			            		var google_conversion_id = 946046381;
			            		var google_conversion_language = "en";
			            		var google_conversion_format = "3";
			            		var google_conversion_color = "ffffff";
			            		var google_conversion_label = "V8zUCKrMvGAQrYuOwwM";
			            		var google_remarketing_only = false;
			            		if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
			            			M.byId('pixelTracking').innerHTML='<img height="1" width="1" style="border-style:none;" alt="" src="https://www.googleadservices.com/pagead/conversion/946046381/?label=V8zUCKrMvGAQrYuOwwM&amp;guid=ON&amp;script=0"/> ';
			            		}
			            	}
		            	}
		        	}		            	
		        },
		        error:function(data){
		        	$(".disabledFields").each(function(){
		    			$(this).attr('disabled','disabled');
		    		});
		        	error_msg=SORRY_FOR_INCONVENIENCE;
		        	showMsg(error_msg);
		        	hideMsg();
			        return false;
		        }
		    });
		}
		return false;
	});
	
	
	/*-------------------------------------------------
	 * Keypress event validations
	 --------------------------------------------------*/
	
	$("#appFirstName, #appLastName, #appMiddleName, #appCoapplicantFirstName1, #appCoapplicantLastName1, #appCoapplicantMiddleName1,#appNRICity, #appNRIState, #appEMPNRIState, #appEMPNRICity").keydown(function(event){
		return M.isChars(event);
	});
	$("#appCoapplicantFirstName2, #appCoapplicantLastName2, #appCoapplicantMiddleName2").keydown(function(event){
		return M.isChars(event);
	});
	
	$("#appAddress1, #appAddress2, #appOfficeAddress1, #appOfficeAddress2, #appPropertyLandmark, #appPropertyAddress2, #appPropertyAddress1, #appAddressLandmark").keydown(function(event){
		return M.isAddressLine(event);
	});
	
	$("#appOfficePhoneNumber, #appOfficePhoneNumberExt").keydown(function(event){
		return M.digit(event);
	});
	/**-------------------------------------------------
	 * apply auto suggest in employee name field for all
	------------------------------------------------- */
	auto_suggestion_employer("appLoanEmployerName");
	auto_suggestion_employer("appCoapplicantEmployerName1");
	auto_suggestion_employer("appCoapplicantEmployerName2");
	
	$(document).on("change","#clonePermanentAddress", function(){
		if($(this).is(":checked")){
			initCopyAddress(this, applicantAddress);
		}
		if($(this).is(":checked") && addressLine1!="" && selectCity!=0 && selectPincode!=0 && selectState!=0){
			disabledAddress(applicantAddress, true);
			copyAddress(permanentAddress, applicantAddress);
		}else{
			emptyAddress(permanentAddress, applicantAddress, enableApplicantAddressByCheckBoxIds);
		}
	});
	
	$(document).on("change","#clonePropertyAddress", function(){
		if($(this).is(":checked")){
			initCopyAddress(this, applicantAddress);
		}
		if($(this).is(":checked") && addressLine1!="" && selectCity!=0 && selectPincode!=0 && selectState!=0){
			disabledAddress(applicantAddress, true);
			copyAddress(propertyAddress, applicantAddress);
		}else{
			emptyAddress(propertyAddress, applicantAddress, enableApplicantAddressByCheckBoxIds);
		}
	});
	
	$(document).on("change","#cloneOfficeAddress", function(){
		if($(this).is(":checked")){
			initCopyAddress(this, applicantAddress);
		}
		
		if($(this).is(":checked") && addressLine1!="" && selectCity!=0 && selectPincode!=0 && selectState!=0){
			disabledAddress(applicantAddress, true);
			copyAddress(officeAddress, applicantAddress);
		}else{
			emptyAddress(officeAddress, applicantAddress, enableApplicantAddressByCheckBoxIds);
		}
	});
	
	$(document).on("change","#cloneCoapplicant1Address", function(){
		if($(this).is(":checked")){
			initCopyAddress(this, applicantAddress);
		}
		if($(this).is(":checked") && addressLine1!="" && selectCity!=0 && selectPincode!=0 && selectState!=0){
			disabledAddress(applicantAddress, true);
			copyAddress(coapplicant1Address, applicantAddress);
		}else{
			emptyAddress(coapplicant1Address, applicantAddress, enableApplicantAddressByCheckBoxIds);
		}
	});
	$(document).on("change","#cloneCoapplicant2Address", function(){
		if($(this).is(":checked")){
			initCopyAddress(this, applicantAddress);
		}
		if($(this).is(":checked") && addressLine1!="" && selectCity!=0 && selectPincode!=0 && selectState!=0){
			disabledAddress(applicantAddress, true);
			copyAddress(coapplicant2Address, applicantAddress);
		}else{
			emptyAddress(coapplicant2Address, applicantAddress, enableApplicantAddressByCheckBoxIds);
		}
	});
	
	/**------------------------------------------------
	 * relation b/w joining date and work experience
	 ------------------------------------------------*/
	
	jQuery('#appCompanyJoiningYear').unbind('change').bind("change",function(){
		if($("#"+this.id+" option:first").is(":selected")){
			$("#appCompanyJoiningMonth").removeClass('error');
			$('#appCompanyJoiningMonth').val(13);
			$('#appCompanyJoiningMonth').attr('selected','selected');
			$("#appCompanyJoiningMonth").attr('disabled','disabled');			
		}else if($("#"+this.id+" option:last").is(":selected")){
			$("#appCompanyJoiningMonth").removeClass('error');
			$('#appCompanyJoiningMonth').val(13);
			$('#appCompanyJoiningMonth').attr('selected','selected');
			$("#appCompanyJoiningMonth").attr('disabled','disabled');			
		}else{
			$("#appCompanyJoiningMonth").removeAttr('disabled');
			$('#appCompanyJoiningMonth').val(13);
			$('#appCompanyJoiningMonth').attr('selected','selected');
		}
	});
	
	jQuery('#appWorkExperienceYear').unbind('change').bind("change",function(){
		if($("#"+this.id+" option:first").is(":selected")){
			$("#appWorkExperienceMonth").removeClass('error');
			$("#appWorkExperienceMonth").val('13');
			$("#appWorkExperienceMonth").attr('disabled','disabled');			
		}else if($("#"+this.id+" option[value='1']").is(":selected")){
			$("#appWorkExperienceMonth").removeClass('error');
			$("#appWorkExperienceMonth").val('13');
			$('#appWorkExperienceMonth').attr('selected','selected');
			$("#appWorkExperienceMonth").attr('disabled','disabled');
		}else if($("#"+this.id+" option:last").is(":selected")){
			$("#appWorkExperienceMonth").removeClass('error');
			$("#appWorkExperienceMonth").val('13');
			$('#appWorkExperienceMonth').attr('selected','selected');
			$("#appWorkExperienceMonth").attr('disabled','disabled');
		}else{
			$("#appWorkExperienceMonth").removeAttr('disabled');
			$("#appWorkExperienceMonth").val('13');
		}
	});
	if($('#haveAadhaarYes').is(':checked')){
		var error_msg = "Ensure to submit copy of Aadhaar with Application Form.";
		$("#aadhaarConfirmation").html('<em>'+error_msg+'</em>').show();
	}
});

jQuery(document).on("click","#haveAadhaarNo", function(){
	jQuery("#aadhaarConfirmation").hide();
});
jQuery(document).on("click","#haveAadhaarYes", function(){
	var error_msg = "Ensure to submit copy of Aadhaar with Application Form.";
	$("#aadhaarConfirmation").html(error_msg).show();
});
function extraValidationThirdPage(){
	var isValidProof=false;
	jQuery("#appPanCardNo").removeClass('error');
	jQuery("#appPanCardNo").next('span').hide();
	jQuery("#appOtherIdNumber").removeClass("error");
	jQuery("#appOtherIdNumber").next('span').hide();
	jQuery("#appOfficePhoneStdCode").removeClass('error');
	jQuery("#appOfficePhoneStdCode").next('span').hide();
	jQuery("#appOfficePhoneNumber").removeClass('error');
	jQuery("#appOfficePhoneNumber").next('span').hide();
	
	var PanNo = jQuery("#appPanCardNo").val();
	var appIdProof = jQuery("#appIdProof").val();
	var appOtherIdNumber = jQuery("#appOtherIdNumber").val();
	var error_msg;
	
	if(PanNo!=undefined && PanNo!=null && PanNo!=""){
		isValidProof=isIdProofValid(PanNo,181);
		if(!isValidProof){
			error_msg = "Applicant PAN Number is Invalid";
			showMsg(error_msg,false,'appPanCardNo');
			return false;
		}
	}
	
	if(appIdProof != undefined && appIdProof != 0){
		isValidProof=isIdProofValid(appOtherIdNumber,appIdProof);
		if (!isValidProof) {
			if(appIdProof == 166){
				 error_msg="Passport Number Not in Correct Format";
			}else if (appIdProof == 165) {
				error_msg="VoterId Number Not in Correct Format";
			}else{
				var error_msg="Other Id Number Not in Correct Format";
			}
			showMsg(error_msg,false,'appOtherIdNumber');
			return false;
		}
	}
	
	var year = document.getElementById("appCompanyJoiningYear").value;
	console.log("values is "+year);
	var currentDate = new Date();
	var currentYear = currentDate.getFullYear();
	var month = document.getElementById("appCompanyJoiningMonth").value;
	if(year == 0) {
		error_msg="Joining year is incorrect";
		$("#appCompanyJoiningYear").addClass('error');
		showMsg(error_msg,false,'appCompanyJoiningYear');
		return false;
	} 
	if (year != currentYear - 7) {
		if (month == 0) {
			error_msg="Please select month for Joining Date";
			$("#appCompanyJoiningMonth").addClass('error');
			showMsg(error_msg,false,'appCompanyJoiningMonth');
			return false;
		}
	}
	
	var isError=0;
	var stdCode = $("#appOfficePhoneStdCode").val();
	var phoneNumber = $("#appOfficePhoneNumber").val();
	if(stdCode==undefined && phoneNumber == undefined){
		return true;
	}
	if((stdCode!= undefined || stdCode != null || stdCode !="" ) && (phoneNumber == undefined || phoneNumber == null || phoneNumber =="")){
		isError=2;
	}
	if((phoneNumber!= undefined || phoneNumber != null || phoneNumber !="" ) && (stdCode == undefined || stdCode == null || stdCode =="")){
		isError=1;
	}
	if(stdCode =='' && phoneNumber == ''){
		 isError=0;
	}
	if(isError==0){
		if(stdCode==''){
			return true;
		}
		if(stdCode.length>=2 && stdCode.length<=6){
			if(stdCode.length==6){
				if(stdCode.indexOf("0")==0){
					isError=0;
				}else{
					isError=1;
				}
			}else{
				isError=0;
			}
		}else{
			isError=1;
		}
	}

	if(isError==0){
		return true;
	}else{
		if(isError == 2){
			var error_msg="Std code and phone should be given together.";
			showMsg(error_msg);
			hideMsg();
			jQuery("#appOfficePhoneNumber").addClass('error');
			return false;
		}else{
			var error_msg="Std code is invalid.";
			showMsg(error_msg);
			hideMsg();
			jQuery("#appOfficePhoneStdCode").addClass('error');
			return false;
		}
	}
}


jQuery(document).on("change","#appIdProof", function() {
	jQuery("#appOtherIdNumber").removeClass("error");
	var appIdProof = jQuery("#appIdProof").val();
	if(appIdProof == 0){
		jQuery("#AppOtherIdNumber").html('');
	}else {
		jQuery("#AppOtherIdNumber").html($('#htmlOtherIdentityNo').html());
		if(appIdProof == 166){
			jQuery("#appOtherIdNumber").attr('maxLength','16');
		}else if(appIdProof == 165){
			jQuery("#appOtherIdNumber").attr('maxLength','20');
		}else {
			jQuery("#appOtherIdNumber").attr('maxLength','12');
		}
	}
});	

document.addEventListener("wheel",e =>{e.stopPropagation();}, true);
