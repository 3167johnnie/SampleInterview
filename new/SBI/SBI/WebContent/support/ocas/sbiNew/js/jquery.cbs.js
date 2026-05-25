jQuery(document).ready(function(){

	$("#commonCBSForm").validate({
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
	          message = '<em>'+message+'</em>'
	          showMsg(message);
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if($("#"+element.attr("id")).is("select")){
				error.insertAfter($(element).parent());
			}else if(element.attr("type")=="checkbox"){
				return true;
			}else if(element.attr("name")=="cbs.cbsIsdCode" || element.attr("name")=="cbs.cbsMobileNumber"){
				$("#cbsMobileNumber").closest('li').append(error.addClass('adjust'));
			}else if(element.attr("name")=="cbs.cveProductCategory"){ 
				$("#cveProductCategory").closest('li').append(error.addClass('adjust'));
			}else{
				error.insertAfter(element);
			}
			if($("#"+element.attr("id")).is("select")){
				customOnFocusElement(element.attr("id"));
			}
		}
	});
	jQuery(document).on("change","#cbsTypeOfRelationship", function() {
		src=this;
		if(src==undefined||src.options==undefined||!src.options.length)return false;
		$('#noOfExistingAccount').html('');
		if($('#cbsTypeOfRelationship').val()==2 && $('#cbsLoanPurpose').val()==23){
			$('#noOfExistingAccount').html($('#htmlNoOfExistingAccount').html());
		}else{
			$('#noOfExistingAccount').html('');
		}
	});
	
	jQuery(document).on("change","#cbsTypeOfRelationshipCve", function() {
		src=this;
		if(src==undefined||src.options==undefined||!src.options.length)return false;
		var selValCve=src.options[src.selectedIndex].value;
	});
	
	if(loanTypeId == 3){
		$('#relationshipWithSBIBank').show();
		$('#pageOverlay2').hide();
		$('#mobileOTP2').hide();
		var cbsLoanPurpose=$('#cbsLoanPurpose').val();
		if(cbsLoanPurpose==23){
			jQuery("#cbsLoanPurpose").val(23).trigger("change");
		}else if(cbsLoanPurpose==11){
			jQuery("#cbsLoanPurpose").val(11).trigger("change");
		}
		$('input:radio[name="relationshipWithSBI"]').change(
		   function() {
			   if(this.checked && this.value=='1'){
				   if(isDsrPage){
					   $('#divCommonBiometric').hide();
					   $('#relationshipWithSBIBank').show();
					   $('#personalloanform').hide();
				   }else{
					   $('#relationshipWithSBIBank').show();
					   $('#personalloanform').hide();
				   }
			   }else{
				   if(isDsrPage){
					   $('#commonCbsDivId').hide();
					   $('#relationshipWithSBIBank').hide();
					   $('#divCommonBiometric').show();
					   $('#personalloanform').hide();
				   }else{
					   $('#relationshipWithSBIBank').hide();
					   $('#personalloanform').show();
				   }
			   }
		 });
	}
});


function getCBSCall(){
	remove_comma(document.commonCBSForm);
	addValidationsRules(document.commonCBSForm,fetchCBSJsonData());
	if($("form#commonCBSForm").valid()){
		if($("#accountNumber").val()[0]=="0"){
			showMsg('Invalid account number',false);
			return false;
		}
    	$(".disabledFields").each(function(){
			$(this).removeAttr('disabled');
		});
    	if($('#cbsTypeOfRelationship').val()==3){
			$('#cbsNoOfExistingXpressCredit').attr('disabled',true);
		}

		var paramData= getRequest('commonCBSForm', 4);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showMsg(errorMsg);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=27&requestType=1";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
	        type:'post',
	        url: ajaxPostUrl,
	        data: formdata,
	        beforeSend:function(){
	        	$('#page-loader').show();
			},
			complete:function(){
				$('#page-loader').hide();
			},
	        success:function(htmlContent){
	        	if(htmlContent!=""){
	            	var stringMessage = [];
	            	stringMessage = htmlContent.split("|");
	        		if(stringMessage[0] == "error"){
	            		error_msg=stringMessage[1];
	            		message = '<b>Error: </b><em>'+error_msg+'</em>'
	            		showMsg(message);
	            		hideMsg();
	        	        return false;
	            	}else{
	            		jQuery("#otpConfirmBox").html(htmlContent);
	            		jQuery("#otpConfirmBox").show();
	            		jQuery("#commonCbsDivId").hide();
	            		
	            		//code to show cbs OTP popup (after bootstrap v5 upgrade)
	            		const cbsOTPEl = document.getElementById('cbsOTP');
	            		const cbsOTPModal = new bootstrap.Modal(cbsOTPEl, {
	            		  backdrop: 'static',
	            		  keyboard: false
	            		});

	            		cbsOTPModal.show();
		            	
						removeDynamicLoader('submitBtn');
		            	return false;
	            	}
	        	}
	        	return false;
	        },
	        error:function(data){
	        	window.scrollTo(0, 175);
	        	message=SORRY_FOR_INCONVENIENCE;
        		showMsg(message);
        		hideMsg();
		        return false;
	        }
	    }); 
	}
	return false;
}
jQuery(document).on("change","#cbsLoanPurpose", function() {
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	$('#xpressCreditLoan').html('');
	var selVal=src.options[src.selectedIndex].value;
	$("#relationshipWithSBIBank").html($("#htmlCBSPLTypeOfRelationship"+selVal).html());
	linkUrlForLoanPurpose();

	$("#relationshipWithSBIBank").html($("#htmlCBSPLTypeOfRelationship"+selVal).html());
	if($('#cbsLoanPurpose').val()==11){
		$('#commonCbsDivId').show();
	}
	
	linkUrlForLoanPurpose();	
});

jQuery(document).on("change","#relationshipWithSBIYes", function() {
	$('#relationshipWithSBINo').removeAttr("checked");
	$('#relationshipWithSBIYes').attr("checked", "checked");
	$('#relationshipWithSBIBank').show();
	$('#test1').show();
	$('#test2').show();
	
	if(loanTypeId == 1){
		$('#homeloanform').hide();
	}else if(loanTypeId == 2){
		$('#autoloanform').hide();
	}else if(loanTypeId == 3){
		$('#personalloanform').hide();
			if(isDsrPage){
			$('#callLogContent').hide();
		}
	}else if(loanTypeId == 4){
		$('#m-hide').addClass('hide-div-position');
		$('#educationloanform').hide();
	}else if(loanTypeId == 15){
		$('#agriloanform').hide();
	}else if(loanTypeId == 17){
		$('#creditCardForm').hide();
		$('#cardId383').hide();
		$('#cardId421').hide();
		$('#cardId470').hide();
		$('#cardId484').hide();
	}
});

jQuery(document).on("change","#relationshipWithSBINo", function() {
	
	$('#relationshipWithSBIYes').removeAttr("checked");
	$('#relationshipWithSBINo').attr("checked", "checked");
	$('#relationshipWithSBIBank').hide();
	if(loanTypeId == 1){
		if(isDsrPage){
			$('#divCommonBiometric').show();
		}else{
			$('#homeloanform').show();
		}
	}else if(loanTypeId == 2){
		if(isDsrPage){
			$('#divCommonBiometric').show();
		}else{
			$('#autoloanform').show();
		}
	}else if(loanTypeId == 3){
		
		var loanPurposeGold = $("#cbsLoanPurpose").val();
		$("#loanPurpose").val($('#cbsLoanPurpose').val()).trigger("change");
		if(isDsrPage){
			$('#divCommonBiometric').show();
		}else{
			$("#commonCbsForPL").hide();
			$('#personalloanform').show();
			showHideFieldsByLoanPurpose($("#cbsLoanPurpose").val());
			
			$("#commonCbsForPLCveRevoke").hide();
			$('#personalloanform').show();
			showHideFieldsByLoanPurpose($("#cbsLoanPurpose").val());
		}
		$("#loanPurpose").prop("disabled", true);
		if(loanPurposeGold == 27) {
			var consent = "I hereby authorize State Bank of India and/or its representatives to call me, email me, or SMS me with reference to my loan application. This consent will supersede any registration for any Do Not Call (DNC) / National Do Not Call (NDNC). The information provided by me here is correct. <b class='req'>*</b>";
			$("#personalLoanConsentIdNo").empty();
			$("#personalLoanConsentIdNo").append(consent);	
		}
		
		linkUrlForLoanPurpose();
	}else if(loanTypeId == 4){
		$('#m-hide').removeClass('hide-div-position');
		$('#educationloanform').show();
	}else if(loanTypeId == 15){
		
		if(isDsrPage){
			$('#divCommonBiometric').show();
		}else{
			$('#agriloanform').show();
		}
	}else if(loanTypeId == 17){
		$('#creditCardForm').show();
		showCardBanner('383');
		$('#appProductVariantId1').attr("checked","checked");
		$('#divAppOccupationId').hide();
	}

	if(loanTypeId == 3){
		if(isDsrPage){
	        $("#commonCbsForPL").hide();
			$('#personalloanform').hide();
			$('#divCommonBiometric').show();
			$('#callLogContent').hide();
		}
	}
	if(loanTypeId == 4 && !isDsrPage){
		if( appElTypeId == 1 || appElTypeId == 2){
			 $("#CountryofStudyId").prop("disabled", true);
			 $('#educationloanform').show();
		}
		if(appElTypeId == 2){
			 $("#natureOfCourse").prop("disabled", true);
		}
	}
});

function showHideFieldsByLoanPurpose(loanPurpose){
	if (loanPurpose!=undefined && loanPurpose!=null && loanPurpose!='') {
		$('#APPLICANTRESIDENTTYPE').html('');
		$('#APPLICANTRESREL').html('');
		$('#EmploymentTypePAL').html('');
		$('#EmploymentTypeLAP').html('');
		$('#EmploymentTypeGL').html('');
		$('#PROPERTYDETAILS').html('');
		$('#LEASEDETAILS').html('');
		$('#PALWORKDETAIL').html('');
		$('#APPLICANTGURANTORDETAILS').html('');
		$('#PERSONALIDENTITYDETAILS').html('');
		$('#WORKSTATES').html('');
		$('#OCCUPATIONTYPE').html('');
		if (loanPurpose==11) {
			$('#APPLICANTRESIDENTTYPE').html($('#htmlResidentTypeIndian').html());
			$('#EmploymentTypePAL').html($('#htmlEmploymentTypePAL').html());
			$('#PALWORKDETAIL').html($('#htmlWorkPlaceDetails').html());
			$('#WORKSTATES').html($('#htmlWorkStates').html());
		}else if (loanPurpose==12) {
			$('#APPLICANTRESIDENTTYPE').html($('#htmlResidentTypeIndian').html());
			$('#employmentType').removeAttr('disabled');
			
			$('#EmploymentTypeLAP').html($('#htmlEmploymentTypeLAP').html());
			$('#PROPERTYDETAILS').html($('#htmlPropertyDetails').html());
			$('#LEASEDETAILS').html($('#htmlLeaseDetails').html());
			$('#APPLICANTGURANTORDETAILS').html($('#htmlPersonalLoanCoApplicant').html());
			$('#LAPCOAPPLICANT').html($('#htmlLapCoApplicant').html());
		}else if (loanPurpose==27) {
			$('#residentType').html($('#htmlResidentTypeIndian').html());
			$('#APPLICANTRESIDENTTYPE').html($('#htmlResidentTypeIndian').html());
			$('#APPLICANTRESREL').html($('#htmlApplicantGLResRel').html());
			$('#EmploymentTypeGL').html($('#htmlEmploymentTypeGL').html());
			$('#PERSONALIDENTITYDETAILS').html($('#htmlPersonalIdentityDetails').html());
			$('#PALWORKDETAIL').html($('#htmlWorkPlaceDetails').html());
			$('#WORKSTATES').html($('#htmlWorkStates').html());
			$('#OCCUPATIONTYPE').html($('#htmlOccupationTypeGL').html());
		}
	}
}

function linkUrlForLoanPurpose(){
	var setVal = src.value;

	var  loanLink ='www.sbi.co.in/portal/web/personal-banking/xpress-credit-personal-loan';
	for(var i =0; i<loanPurposeLinks.length ;i++){
		if(loanPurposeLinks[i].key == setVal){
			loanLink = loanPurposeLinks[i].value;
		}
	}
	if(setVal>0){
		var linkforLoanPurpose = "";
		linkforLoanPurpose ="<a  target='_blank' href='http://"+loanLink+"'><span class='note'>Click to read the scheme details before applying</span></a>";
		if(loanLink!=''){
			$("#plmsgId.note").html(linkforLoanPurpose);
		}
	}else{
		$("#plmsgId").html('');
	}
}

jQuery(document).on("click","#consentRevocation", function() {
	$('#test1').hide();
	$('#test2').hide();
	
	$('#test3').show();
	$('#test4').show();
	
	$('#consentRevocation').hide();
	$('#cbsTypeOfRelationshipCveRevoke').attr("name", "cbs.cbsTypeOfRelationship");
	
	$("#consentRevocation1").val("Y");
	
});
