function application_mobileOtp_validate_rules(){
	$("#otpApplication").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        $(element).valid();
	    },
	    onblur:function (element) {
	        $(element).valid();
	    },
		rules: {
			'appApplyingFrom': {
				required: true
		    },
		    'mobile': {
				required: true,
				mobile:true
		    },
		    'email': {
				required: true,
				email: true
		    }
		},
		errorPlacement: function(error, element) {
			$("#errorOTPMsg").html(error);
		},
		messages: {
			
		}
	});
}

$(document).ready(function(){
	jQuery("#confirmOtp").on("click", function(){
		removeDynamicLoader('confirmOtp');
		showDynamicLoader('confirmOtp');
		jQuery("#errorOTPMsg").hide();
		if(jQuery('#inputOtp').val() == "") {
	        //M.byId('inputOtp').style.borderColor = "red";
	        jQuery("#errorOTPMsg").html('Please enter OTP');
	        disableById("confirmOtp", false);
	        jQuery("#opt-loader-application").hide();
	        removeDynamicLoader('confirmOtp');
	        return false;
	    }
		var formdata = "";
		formdata += "appApplyingFrom="+jQuery("#appApplyingFrom").val();
		if(jQuery("#appApplyingFrom").val()==1){
			formdata += "&mobile="+jQuery("#mobile").val(); 
		}else{
			formdata += "&mobile="+jQuery("#appNRIMobileNo").val()+"&isdCode="+jQuery("#isdCode").val();
		}
		formdata += "&inputOtp="+jQuery("#inputOtp").val();
		formdata += "&email="+jQuery("#emailOTP").val()+"&requestIndex=16&requestType=2";
		jQuery.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
		    success:function(htmlContent){
		    	removeDynamicLoader('confirmOtp');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
	        		jQuery("#errorOTPMsg").show();
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		jQuery("#emailSendQuote").val(jQuery("#emailOTP").val());
	        		jQuery("#overlay-row_otp").hide();
	        		jQuery("#overlay-row_otp").remove();
	        		jQuery("#otp_row_confirm").hide();
	        		jQuery("#errorOTPMsg").html('<img src="'+BANK_IMAGE_FOLDER_NEWUI+'/tick.png">'+stringMessage[1]);
	        		jQuery("#errorOTPMsg").show();
	        		jQuery("a.call_us").unbind("click");
	        		setTimeout(function(){
	        			//closeMobileOTPPopup();
	        			dropOffPopupStatus=1;
	        		}, 2000);
	        	}else if(stringMessage[0]=="error"){
	        		jQuery("#errorOTPMsg").html(stringMessage[1]);
	        		jQuery("#errorOTPMsg").show();
	        	}		    	
		    },
		    error:function(jqXHR, textStatus){
		    	jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
        		jQuery("#errorOTPMsg").show();
		    }
		});
		return false;
	});
	
	jQuery("#resendOtp").on("click", function(){
		removeDynamicLoader('resendOtp');
		showDynamicLoader('resendOtp');
		jQuery("#errorOTPMsg").hide();
		var formdata = "";
		formdata += "appApplyingFrom="+jQuery("#appApplyingFrom").val();
		if(jQuery("#appApplyingFrom").val()==1){
			formdata += "&mobile="+jQuery("#mobile").val(); 
		}else{
			formdata += "&mobile="+jQuery("#appNRIMobileNo").val()+"&isdCode="+jQuery("#isdCode").val();
		}
		formdata += "&email="+jQuery("#emailOTP").val()+"&requestIndex=15&requestType=2";
		jQuery.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtp');
		    	if(htmlContent==null || htmlContent ==undefined){
	        		jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
	        		jQuery("#errorOTPMsg").show();
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		removeDynamicLoader('resendOtp');
	        		$("#errorOTPMsg").html('<img src="'+BANK_IMAGE_FOLDER_NEWUI+'/tick.png">'+stringMessage[1]);
	        		$("#errorOTPMsg").show();
		    		show_hide_toggle("#otp_row_confirm", 1);
			    	show_hide_toggle("#overlay-row_otp", 0);
		    	}else if(stringMessage[0]=="error"){
		    		if(stringMessage[2]!=undefined && stringMessage[2]!=null){
		        		if(stringMessage[2]>4){
		        			jQuery("#resendOtp").remove();
		        		}
		        	}
		    		jQuery("#errorOTPMsg").html(stringMessage[1]);
		    		jQuery("#errorOTPMsg").show();
		    	}
	        	
		    },
		    error:function(jqXHR, textStatus){
		    	jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
        		jQuery("#errorOTPMsg").show();
		    }
		});
		return false;
	});
	$("#otpApplication").submit(function(){
		var validationResult = $("#otpApplication").valid();
		removeDynamicLoader('sendOpt');
		if(validationResult){
			showDynamicLoader('sendOpt');
			$("#errorOTPMsg").hide();
			var formdata = "";
			formdata += "appApplyingFrom="+jQuery("#appApplyingFrom").val();
			if(jQuery("#appApplyingFrom").val()==1){
				formdata += "&mobile="+jQuery("#mobile").val(); 
			}else{
				formdata += "&mobile="+jQuery("#appNRIMobileNo").val()+"&isdCode="+jQuery("#isdCode").val();
			}
			formdata += "&email="+jQuery("#emailOTP").val()+"&requestIndex=14&requestType=2";
			$.ajax({
			    type: "POST",
				url: ajaxPostUrl,
				data: formdata,
			    success:function(htmlContent){
			    	removeDynamicLoader('sendOpt');
			    	$("#opt-loader-application").hide();
			    	if(htmlContent==null || htmlContent ==undefined){
		        		jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
		        		jQuery("#errorOTPMsg").show();
		        		return false;
		        	}
			    	var stringMessage = [];
		        	stringMessage = htmlContent.split("|");
			    	if(stringMessage[0]=="success"){
			    		$("#overlay-row_otp").removeClass('field-row show');
		        		$("#overlay-row_otp").addClass('hide');
			    		$("#sendOpt").remove();
		        		$("#overlay-row_otp").remove();
			    		$("#overlay-row_otp").attr('class','overlay-row2 hide');
			    		jQuery("#errorOTPMsg").html('<img src="'+BANK_IMAGE_FOLDER_NEWUI+'/tick.png">'+stringMessage[1]);
			    		show_hide_toggle("#otp_row_confirm", 1);
				    	show_hide_toggle("#overlay-row_otp", 0);
			    	}else if(stringMessage[0]=="error"){
			    		removeDynamicLoader('sendOpt');
			    		jQuery("#errorOTPMsg").html(stringMessage[1]);
		        		jQuery("#errorOTPMsg").show();
		        		return false;
			    	}
			    },
			    error:function(jqXHR, textStatus){
			    	removeDynamicLoader('sendOpt');
			    	jQuery("#errorOTPMsg").html(SORRY_FOR_INCONVENIENCE);
	        		jQuery("#errorOTPMsg").show();
			    }
			});			
		}
		else{
			$("#errorOTPMsg").show();
		}
		return false;
	});
	
	$("#sendOpt").click(function(){
		$("#errorOTPMsg").hide();
		$("#otpApplication").submit();
		return false;
	});
});

jQuery(document).on("click","#appApplyingFromIndiaNo", function() {
	var tooltipMessage="";
	if(!isDsrPage){
		tooltipMessage="M.toolTip_TM('Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.', event, this, 130, 25, 50);";
	}
	var nrimobilediv=''
		+'<label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
		+'<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" onfocus="'+tooltipMessage+'" class="" id="appNRIMobileNo" value="" maxlength="12" name="nriMobileNo">'
		+'';
	jQuery("#mobileDetailsOTP").html(nrimobilediv);
});

jQuery(document).on("click","#appApplyingFromIndiaYes", function(){
	var mobilediv=''
		+'<label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
		+'<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" id="mobile" value="" maxlength="10" name="mobile">'
		+'';
	jQuery("#mobileDetailsOTP").html(mobilediv);
	
});
