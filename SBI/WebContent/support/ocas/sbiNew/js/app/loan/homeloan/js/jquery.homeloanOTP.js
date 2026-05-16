var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

$(document).ready(function(){
	//secretKey=getSecretKey();
  
  //ready
	$("form#otpApplication").validate({
		onkeyup: false,
		onfocusout: function (element) {
	        jQuery(element).valid();
	    },
	    onblur:function (element) {
	        jQuery(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
	        if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          showPOPMsg('errorOTPMsg',message, false);
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if($("#"+element.attr("id")).is("select")){
				error.insertAfter($(element).parent());
			}else if(element.attr("type")=="checkbox"){
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});
 
	//jQuery("#confirmOtp").on("click", function(){
	jQuery("#confirmOtp").unbind("click").bind("click",function(){
		console.log('Onclick event confirmOtp step1');
		//removeDynamicLoader('confirmOtp');
		showDynamicLoader('confirmOtp');
		hidePOPMsg('errorOTPMsg');
		var isEmailOtp = false;
				if($("#appOTPVerifyTypeNo").is(":checked")){
					isEmailOtp = true;
			if(M.byId('inputOtpEmail').value == "") {
				M.byId('inputOtpEmail').style.borderColor = "red";
				showPOPMsg('errorOTPMsg','Please enter email OTP', false);
				M.byId("confirmOtp").disabled = false;
				jQuery("#opt-loader-application").hide();
				removeDynamicLoader('confirmOtp');
				return false;
			}
		}
		if($("#appOTPVerifyTypeYes").is(":checked")){
			if(M.byId('inputOtp').value == "") {
				M.byId('inputOtp').style.borderColor = "red";
				showPOPMsg('errorOTPMsg','Please enter mobile OTP', false);
				M.byId("confirmOtp").disabled = false;
				jQuery("#opt-loader-application").hide();
				removeDynamicLoader('confirmOtp');
				return false;
			}else
			{				
			    jQuery("input[name='inputOtp']").css({"border-bottom-width":"1px","border-bottom-style":"solid","border-bottom-color":"#00adef"});
			}
		}
		var jsonObject = fetch_validation_object(16,1);
		addValidationsRules(document.otpApplication,jsonObject);
		if($("form#otpApplication").valid()){
			
			console.log('Onclick event confirmOtp step2');
			showDynamicLoader('confirmOtp');
			$("#appApplyingFromIndiaYes").removeAttr('disabled');
			$("#appApplyingFromIndiaNo").removeAttr('disabled');

			if (isEmailOtp == false) {
				
			var inputOtp=document.getElementById('inputOtp').value;
			//
			  if(inputOtp!="" || inputOtp !=''){
				 
			var encryptedOtp = getEncryptedStr(inputOtp);
		   
			document.getElementById('inputOtp1').value = encryptedOtp;
			  }
			}else if(isEmailOtp==true){
				var inputOtpEmail = document.getElementById('inputOtpEmail').value;
				//var encryptedOtp = getEncryptedStr(inputOtpEmail);
				document.getElementById('inputOtpEmail').value = inputOtpEmail;
			}
			
//			var formdata = "params="+getRequest('otpApplication', 0);
//	    	formdata +="&requestIndex=16&requestType=2";
				

			
			
	    	var paramData= getRequest('otpApplication', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg',errorMsg, false);
				return false;
			}
			var formdata = "params="+paramData;
			formdata +="&requestIndex=16&requestType=2";
			jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
			jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
			
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			jQuery.ajax({
				type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				success:function(htmlContent){
					removeDynamicLoader('confirmOtp');
					if(htmlContent==null || htmlContent ==undefined){
						showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
						return false;
					}
					var stringMessage = [];
					
					if (isEncrypted(htmlContent)) {
						console.log('content is ' + htmlContent);
						decryptedResponse = getDecryptedResponse(htmlContent);
						console.log('decryptedResponse is ' + decryptedResponse);
						stringMessage = decryptedResponse.split("|");
					} else {
						stringMessage = htmlContent.split("|");
					}
					
					//stringMessage = htmlContent.split("|");
					if(stringMessage[0]=="success"){
						//secretKey=getSecretKey();
						isMobileVerified="Y";
						jQuery("#overlay-row_otp_hl").hide();
						jQuery("#overlay-row_otp_hl").remove();
						jQuery("#otp_row_confirm").hide();
						jQuery("#otp_verify_type").hide();
						removeDynamicLoader('confirmOtp');
						showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png"> OTP verified successfully', true);
						jQuery("a.call_us").unbind("click");
						setTimeout(function(){
							$("#productOTP").modal('hide');
						}, 2000);
					}else if(stringMessage[0]=="error"){
						removeDynamicLoader('confirmOtp');
						showPOPMsg('errorOTPMsg',stringMessage[1], false);
						jQuery("input[name='"+stringMessage[2]+"']").addClass('error');
						return false;
					}
				},
				error:function(jqXHR, textStatus){
					showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
					return false;
				}
			});
		}else{
			$("#confirmOtp").removeAttr('disabled');
			removeDynamicLoader('confirmOtp');
		}
		return false;
	});
	
	function getDecryptedResponse(htmlContent) {
		var aesUtil = new AesUtil(keySize, iterationCount);
		var dec1 = aesUtil.decrypt(salt, iv, passPhrase, htmlContent);
		
		return dec1;
	}

	function isEncrypted(str) {
		
		if (str ==='' || str.trim() ===''){ return false; }
		try {
			
			var aesUtil = new AesUtil(keySize, iterationCount);
			
			var dec1 = aesUtil.decrypt(salt, iv, passPhrase, str);
			var enc1 = aesUtil.encrypt(salt, iv, passPhrase, dec1);
			
			return enc1 == str;
		} catch (err) {
			return false;
		}
	}
	
	jQuery("#resendOtp").unbind("click").bind("click",function(){
	//jQuery("#resendOtp").one("click", function(){
		//alert('homeloanOtp')
		removeDynamicLoader('resendOtp');
		showDynamicLoader('resendOtp');
		hidePOPMsg('errorOTPMsg');
		//
		//var jsonObject = fetch_validation_object(15,1);
		//addValidationsRules(document.otpApplication,jsonObject);
		/*if($("form#otpApplication").valid()){*/
			$("#appApplyingFromIndiaYes").removeAttr('disabled');
			$("#appApplyingFromIndiaNo").removeAttr('disabled');
			/*var formdata = "params="+getRequest('otpApplication', 0);
	    	formdata +="&requestIndex=15&requestType=2";*/
	    	var paramData= getRequest('otpApplication', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg',errorMsg, false);
				return false;
			}
			var formdata = "params="+paramData;
			formdata +="&requestIndex=15&requestType=2";
			jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
			jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			jQuery.ajax({
				type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				//headers : {"Accept-Encoding" : "gzip"},
				success:function(htmlContent){
					removeDynamicLoader('resendOtp');
					if(htmlContent==null || htmlContent ==undefined){
						showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
						return false;
					}
					var stringMessage = [];
					stringMessage = htmlContent.split("|");
					
					
					
					if(stringMessage[0]=="success"){
						//secretKey=getSecretKey();
						removeDynamicLoader('resendOtp');
						refreshCaptcha('CommonOtpcapImage');
						showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
						show_hide_toggle("#otp_row_confirm", 1);
						show_hide_toggle("#overlay-row_otp_hl", 0);
					}else if(stringMessage[0]=="error"){
						refreshCaptcha('CommonOtpcapImage');
						if(stringMessage[2]!=undefined && stringMessage[2]!=null){
							if(stringMessage[2]>4){
								jQuery("#resendOtp").remove();
							}
						}
						showPOPMsg('errorOTPMsg',stringMessage[1], false);
					}
					
				},
				error:function(jqXHR, textStatus){
					showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
					return false;
				}
			});
		/*}else{
			$("#resendOtp").removeAttr('disabled');
			removeDynamicLoader('resendOtp');
		}*/
		return false;
	});
	
	
	jQuery("#sendOpt").unbind("click").bind("click",function(){
		hidePOPMsg('errorOTPMsg');
		  if($('#appApplyingFromIndiaNo').is(':checked')){
			// if($('#appApplyingFromIndiaNo').is(':checked')){
				$('#mobileotp').remove();
				$('#emailidotp').remove();
				if(!M.isMobile($('#appNRIMobileNo').val()) && !M.isEmail($('#emailOTP').val())){
					//$("#appNRIMobileNo").after("<div class='clear error' id='mobileotp'> Invalid mobile no.</div>");
					M.byId('appNRIMobileNo').style.borderColor = "red";
					showPOPMsg('errorOTPMsg','Invalid mobile number', false);
					$('#appNRIMobileNo').focus();
					//showPOPMsg('errorOTPMsg','Invalid mobile no.', false);
					$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id1.</div>");
					M.byId('emailOTP').style.borderColor = "red";
					showPOPMsg('errorOTPMsg','Invalid email id', false);
					$('#emailOTP').focus();
					return false;
				}
			//}
			if($('#appOTPVerifyTypeNo').is(':checked')){
				$('#emailidotp').remove();
				if(!M.isEmail($('#emailOTP').val())){
					$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id2.</div>");
					M.byId('emailOTP').style.borderColor = "red";
					//showPOPMsg('errorOTPMsg','Invalid email id', false);
				//	$('#emailOTP').focus();
					return false;
				}
			}
		}else{
			var mobile=$('#mobile').val();
			$('#mobileotp').remove();
			$('#emailidotp').remove();
			/*if(!M.isMobile(mobile) && !M.isEmail($('#emailOTP').val())){
				$("#mobile").after("<div class='clear error' id='mobileotp'> Invalid mobile no.</div>");
				M.byId('mobile').style.borderColor = "red";
				//showPOPMsg('errorOTPMsg','Invalid mobile no.', false);
				$('#mobile').focus();
				$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id.</div>");
				M.byId('emailOTP').style.borderColor = "red";
				//showPOPMsg('errorOTPMsg','Invalid email id', false);
				$('#emailOTP').focus();
				return false;
			}*/
		}
		var jsonObject = fetch_validation_object(14,1);
		addValidationsRules(document.otpApplication,jsonObject);
		if($("form#otpApplication").valid()){
			showDynamicLoader('sendOpt');
			$(".disabledFields").each(function(){
				$(this).removeAttr('disabled');
			});
			/*var formdata = "params="+getRequest('otpApplication', 0);
	    	formdata +="&requestIndex=14&requestType=2";*/
	    	var paramData= getRequest('otpApplication', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg',errorMsg, false);
				return false;
			}
			var formdata = "params="+paramData;
			formdata +="&requestIndex=14&requestType=2";
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			$.ajax({
			    type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				//headers : {"Accept-Encoding" : "gzip"},
			    success:function(htmlContent){
			    	removeDynamicLoader('sendOpt');
			    	$("#opt-loader-application").hide();
			    	if(htmlContent==null || htmlContent ==undefined){
			    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		        		return false;
		        	}
			    	var stringMessage = [];
		        	//stringMessage = htmlContent.split("|");
					
			    	if (isEncrypted(htmlContent)) {
						console.log('content is ' + htmlContent);
						decryptedResponse = getDecryptedResponse(htmlContent);
						console.log('decryptedResponse is ' + decryptedResponse);
						stringMessage = decryptedResponse.split("|");
					} else {
						stringMessage = htmlContent.split("|");
					}
					
			    	if(stringMessage[0]=="success"){
			    		//secretKey=getSecretKey();
			    		showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
			    		show_hide_toggle("#otp_row_confirm", 1);
				    	show_hide_toggle("#overlay-row_otp_hl", 0);
				    	show_hide_toggle("#otpPopHome", 0);
				    	jQuery("#otpPopHome").hide();
				    	jQuery("#OtpPopHl1").show();
				    	jQuery("#OtpPopHl").show();
				    	if($("#appApplyingFromIndiaYes").is(':checked')){
				    		jQuery("#otp_verify_type").hide();
				    	}else if($("#appApplyingFromIndiaNo").is(':checked')){
				    		jQuery("#otp_verify_type").show();
				    	}
				    	jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
				    	jQuery('#appApplyingFromIndiaYes').addClass('disabledFields');
				    	jQuery('#appApplyingFromIndiaNo').addClass('disabledFields');
				    	jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
				    	$('#comncaptcha').attr('name','captcha');
				    	$('#homecaptcha').attr('name','captcha_1');
				    	refreshCaptcha('CommonOtpcapImage');
			    	}else if(stringMessage[0]=="error"){
			    		refreshCaptcha('CommonOtpcapImage');
			    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		        		return false;
			    	}
			    },
			    error:function(jqXHR, textStatus){
			    	removeDynamicLoader('sendOpt');
			    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
					return false;
			    }
			});			
		}		
		return false;
	});
	
	jQuery(document).on("click","#appApplyingFromIndiaNo", function() {
		var tooltipMessage="";
		if(!isDsrPage){
			tooltipMessage="M.toolTip_TM('Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.', event, this, 130, 25, 0);";
		}
		var nrimobilediv=''
			+'<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" onfocus="'+tooltipMessage+'" class="form-control" id="appNRIMobileNo" value="" maxlength="12" name="nriMobileNo">'
			+'</li>';
		jQuery("#mobileDetailsOTP").html(nrimobilediv);
		jQuery("#appOTPVerifyTypeYes").trigger("click");
		$('#isdCode').val(91);
	});

	jQuery(document).on("click","#appApplyingFromIndiaYes", function(){
		var mobilediv=''
			+'<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" id="mobile" value="" maxlength="10" name="mobile" class="form-control">'
			+'</li>';
		jQuery("#mobileDetailsOTP").html(mobilediv);
		jQuery("#appOTPVerifyTypeYes").trigger("click");
		$('#isdCode').val(91);
	});

	jQuery(document).on("click","#appOTPVerifyTypeYes", function() {
		var mobilediv=''
			+'<li><label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>'
			+'<input id="inputOtp" name="inputOtp" class="form-control" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control">'
			+'</li>';
		jQuery("#otp_row_confirm_html").html(mobilediv);
	});

	jQuery(document).on("click","#appOTPVerifyTypeNo", function(){
		var mobilediv=''
			+'<li><label><span id="otpLabel">Enter code sent by Email<b class="req"> *</b></span></label>'
			+'<input id="inputOtpEmail" name="inputOtpEmail" type="text" class="form-control" maxlength="6" onkeydown="return M.isAlphaNumeric(event);" class="form-control">'
			+'</li>';
		jQuery("#otp_row_confirm_html").html(mobilediv);
	});


});


function isMobileAlt(b) {

	var len = b.length;

	b = b.toString();
	if (!$.isNumeric(b)) {
		return null
	}
	if (b.length < 7) {
		return null
	} else {
		if (b < "1000000000" || b > "9999999999") {
			return null
		} else {
			return b
		}
	}
}
function getEncryptedStr(str) {
	
	try {
		
	
		var aesUtil = new AesUtil(keySize, iterationCount);
		var enc1 = aesUtil.encrypt(salt, iv, passPhrase, str);
		
		
		return enc1;
	} catch (err) {
		return false;
	}
}


$(document).ready(function(){
	$("form#otpApplicationAlt").validate({
		onkeyup: false,
		onfocusout: function (element) {

console.log('inside validate');
	        jQuery(element).valid();

	    },
	    onblur:function (element) {
console.log('inside onblur');
	        jQuery(element).valid();
	    },
	    focusInvalid: false,
	    invalidHandler: function(event, validator) {
	    	var errors = validator.numberOfInvalids();
	        if (errors) {
	          var message = errors == 1
	            ? 'You missed 1 field. It has been highlighted'
	            : 'You missed ' + errors + ' fields. They have been highlighted';
	          showPOPMsg('errorOTPMsg1',message, false);
	        }
	    },
	    errorElement: "span",
		errorPlacement: function(error, element) {
			if($("#"+element.attr("id")).is("select")){
				error.insertAfter($(element).parent());
			}else if(element.attr("type")=="checkbox"){
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});


	jQuery("#confirmOtpAlt").unbind("click").bind("click",function(){
	//jQuery("#confirmOtpAlt").on("click", function(){
		
		removeDynamicLoader('confirmOtpAlt');
		showDynamicLoader('confirmOtpAlt');
		hidePOPMsg('errorOTPMsg1');
		
		  //commented two if condition on 24 jan 2023 
			/*	if($("#appOTPVerifyTypeNo").is(":checked")){
			//if(M.byId('inputOtpEmail').value == "") {
			//	M.byId('inputOtpEmail').style.borderColor = "red";
			//	showPOPMsg('errorOTPMsg','Please enter email OTP', false);
				M.byId("confirmOtp").disabled = false;
				jQuery("#opt-loader-application").hide();
				removeDynamicLoader('confirmOtp');
				return false;
			//}
		}
		if($("#appOTPVerifyTypeYes").is(":checked")){
			if(M.byId('inputOtp').value == "") {
				M.byId('inputOtp').style.borderColor = "red";
				showPOPMsg('errorOTPMsg1','Please enter mobile OTP', false);
				M.byId("confirmOtpAlt").disabled = false;
				jQuery("#opt-loader-application").hide();
				removeDynamicLoader('confirmOtp');
				return false;
			}
		}*/
		var jsonObject = fetch_validation_object(16,1);
		addValidationsRules(document.otpApplicationAlt,jsonObject);
		if($("form#otpApplicationAlt").valid() || $("#inputOtpAlt").val()==''){
			$("#appApplyingFromIndiaYes").removeAttr('disabled');
			$("#appApplyingFromIndiaNo").removeAttr('disabled');
			var formdata = "params="+getRequest('otpApplication', 0);
	    	formdata +="&requestIndex=16&requestType=2";
			var inputOtpAlt=document.getElementById('inputOtpAlt').value;
				//alert('alternate otp inputOtpAlt'+ inputOtpAlt);
			var encryptedOtp = getEncryptedStr(inputOtpAlt);
		   // alert("encryptedOtp2"+encryptedOtp);
			document.getElementById('inputOtpAlt1').value = encryptedOtp;
	    	var paramData= getRequest('otpApplicationAlt', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg1',errorMsg, false);
				return false;
			}
			var formdata = "params="+paramData;
			formdata +="&requestIndex=16&requestType=2";
			jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
			jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
			
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			jQuery.ajax({
				type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				success:function(htmlContent){
					removeDynamicLoader('confirmOtpAlt');
					if(htmlContent==null || htmlContent ==undefined){
						showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
						return false;
					}
					var stringMessage = [];
					stringMessage = htmlContent.split("|");
					if(stringMessage[0]=="success"){
						//secretKey=getSecretKey();
						isMobileVerified="Y";
						jQuery("#overlay-row_otp_hl").hide();
						jQuery("#overlay-row_otp_hl").remove();
						jQuery("#otp_row_confirm").hide();
						jQuery("#otp_verify_type").hide();
						showPOPMsg('errorOTPMsg1','<img src="'+BANK_IMAGE_FOLDER+'/tick.png"> OTP verified successfully', true);
						jQuery("a.call_us").unbind("click");
						setTimeout(function(){
							$("#OTP1").modal('hide');
						}, 2000);
						
					}else if(stringMessage[0]=="error"){
						refreshCaptcha("homeCommonOtpcapImage");
						showPOPMsg('errorOTPMsg1',stringMessage[1], false);
						jQuery("input[name='"+stringMessage[2]+"']").addClass('error');
						jQuery("input[name='inputOtpAlt']").css({"border-bottom-width":"1px","border-bottom-style":"solid","border-bottom-color":"red"});
						return false;
					}
				},
				error:function(jqXHR, textStatus){
					showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
					return false;
				}
				
			});
		}else{
			$("#confirmOtpAlt").removeAttr('disabled');
			removeDynamicLoader('confirmOtpAlt');
		}
		return false;
	});
									
								
	//jQuery("#resendOtpAlt").on("click", function(){
		
   jQuery("#resendOtpAlt").unbind("click").bind("click",function(){
		removeDynamicLoader('resendOtpAlt');
		showDynamicLoader('resendOtpAlt');
		hidePOPMsg('errorOTPMsg1');
		//var jsonObject = fetch_validation_object(15,1);
		//addValidationsRules(document.otpApplication,jsonObject);
		//if($("form#otpApplicationAlt").valid()){
			//commmented 2 line on 7 feb
		$("#appApplyingFromIndiaYes").removeAttr('disabled');
	$("#appApplyingFromIndiaNo").removeAttr('disabled');
		//	var formdata = "params="+getRequest('otpApplication', 0);
	    //	formdata +="&requestIndex=15&requestType=2";
	    	var paramData= getRequest('otpApplicationAlt', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg1',errorMsg, false);
				return false;
			}
			var formdata = "params="+paramData;
			formdata +="&requestIndex=15&requestType=2";
			jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
			jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			jQuery.ajax({
				type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				//headers : {"Accept-Encoding" : "gzip"},
				success:function(htmlContent){
					removeDynamicLoader('resendOtpAlt');
					if(htmlContent==null || htmlContent ==undefined){
						showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
						return false;
					}
					var stringMessage = [];
					stringMessage = htmlContent.split("|");
					if(stringMessage[0]=="success"){
						//secretKey=getSecretKey();
						removeDynamicLoader('resendOtpAlt');
						refreshCaptcha('CommonOtpcapImage');
						showPOPMsg('errorOTPMsg1','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
						show_hide_toggle("#otp_row_confirm", 1);
						show_hide_toggle("#overlay-row_otp_hl", 0);
					}else if(stringMessage[0]=="error"){
						refreshCaptcha('CommonOtpcapImage');
						if(stringMessage[2]!=undefined && stringMessage[2]!=null){
							if(stringMessage[2]>4){
								jQuery("#resendOtpAlt").remove();
							}
						}
						showPOPMsg('errorOTPMsg1',stringMessage[1], false);
					}
					
				},
				error:function(jqXHR, textStatus){
					showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
					return false;
				}
			});
		//}else{
		//	$("#resendOtp").removeAttr('disabled');
		//	removeDynamicLoader('resendOtp');
		//}
		return false;
	});
	
	
	jQuery("#sendOptAlt").unbind("click").bind("click",function(){
		console.log("inside sendotpalt function");
		hidePOPMsg('errorOTPMsg1');
		  if($('#appApplyingFromIndiaNo').is(':checked')){
			//if($('#appApplyingFromIndiaNo').is(':checked')){
				$('#mobileotp').remove();
			//	$('#emailidotp').remove();
				//if(!M.isMobile($('#appNRIMobileNo').val()) && !M.isEmail($('#emailOTP').val())){
					if(!isMobileAlt($('#appNRIMobileNo').val()) ){
					//$("#appNRIMobileNo").after("<div class='clear error' id='mobileotp'> Invalid mobile no.</div>");
					M.byId('appNRIMobileNo').style.borderColor = "red";
					showPOPMsg('errorOTPMsg1','Invalid mobile number', false);
					$('#appNRIMobileNo').focus();
					//showPOPMsg('errorOTPMsg','Invalid mobile no.', false);
					//$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id1.</div>");
					//M.byId('emailOTP').style.borderColor = "red";
					//showPOPMsg('errorOTPMsg','Invalid email id', false);
					//$('#emailOTP').focus();
					return false;
				}
			//}
			/*if($('#appOTPVerifyTypeNo').is(':checked')){
				$('#emailidotp').remove();
				if(!M.isEmail($('#emailOTP').val())){
					$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id2.</div>");
					M.byId('emailOTP').style.borderColor = "red";
					//showPOPMsg('errorOTPMsg','Invalid email id', false);
				//	$('#emailOTP').focus();
					return false;
				}
			}*/
		}else{
			var mobile=$('#alternateMobileNumber').val();
			$('#mobileotp').remove();
	//		$('#emailidotp').remove();
			//if(!M.isMobile(mobile) && !M.isEmail($('#emailOTP').val())){
				if(!isMobileAlt(mobile) ){
				$("#alternateMobileNumber").after("<div class='clear error' id='mobileotp'> Invalid mobile no.</div>");
				M.byId('alternateMobileNumber').style.borderColor = "red";
				//showPOPMsg('errorOTPMsg','Invalid mobile no.', false);
				$('#alternateMobileNumber').focus();
				//$("#emailOTP").after("<div class='clear error' id='emailidotp'> Invalid email id.</div>");
				//M.byId('emailOTP').style.borderColor = "red";
				//showPOPMsg('errorOTPMsg','Invalid email id', false);
				//$('#emailOTP').focus();
				return false;
			}
		}
		var jsonObject = fetch_validation_object(14,1);
		addValidationsRules(document.otpApplicationAlt,jsonObject);
		
		var homeOtp =$('#homecaptcha').val();
		
		var commonOtp =$('#comncaptcha').val();
		
		if($("form#otpApplicationAlt").valid()){
			showDynamicLoader('sendOptAlt');
			$(".disabledFields").each(function(){
				$(this).removeAttr('disabled');
			});
			var formdata = "params="+getRequest('otpApplicationAlt', 0);
	    	formdata +="&requestIndex=14&requestType=2";
	    	var paramData= getRequest('otpApplicationAlt', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorOTPMsg1',errorMsg, false);
				return false;
			}
			
			var formdata = "params="+paramData;
			formdata +="&requestIndex=14&requestType=2";
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			$.ajax({
			    type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				//headers : {"Accept-Encoding" : "gzip"},
			 	
			    success:function(htmlContent){
			    	console.log (htmlContent);
			    	//var homeOtp1 =$('#homecaptcha').val();
					//alert("homeOtp $$$$$$$$qqqqqqq "+homeOtp1);   //5 comming
					
					//var commonOtp1 =$('#comncaptcha').val();
					//alert("commonOtp $$$$$$$$qqqqqqq "+commonOtp1);   //6 comming
			    	
			    	//refreshCaptcha("homeCommonOtpcapImage1");
		    		//alert("refreshCaptcha....... " + stringMessage);
		    		//alert("homecaptcha....... " + homecaptcha);
		    		
			    	removeDynamicLoader('sendOptAlt');
			    	$("#opt-loader-application").hide();
			    	if(htmlContent==null || htmlContent ==undefined){
			    		showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
		        		return false;
		        	}
			    	var stringMessage = [];
		        	stringMessage = htmlContent.split("|");
			    	if(stringMessage[0]=="success"){
			    		//secretKey=getSecretKey();
			    		//refreshCaptcha("homeCommonOtpcapImage");
			    	//	alert("refreshCaptcha....... " + stringMessage);
			    	//	alert("homecaptcha....... " + homecaptcha);
			    		
			    		//refreshCaptcha("homecaptcha");
			    	//	alert("refreshCaptcha homecaptcha ....... " + stringMessage);
			    	//	alert("homecaptcha homecaptcha ....... " + homecaptcha);
			    		
			    		showPOPMsg('errorOTPMsg1','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
			    		show_hide_toggle("#otp_row_confirm", 1);
			    		show_hide_toggle("#modal-content", 0);
				    	show_hide_toggle("#overlay-row_otp_hl", 0);
				    	show_hide_toggle("#privacyStatementHTML", 0);
				    	show_hide_toggle("#otpPopHome", 0);
				    
				    	//jQuery("#otpPopHome").hide();
				    	//jQuery("#privacyStatementHTML").hide();
				    	jQuery("#OtpPopHl1").show();
				    	jQuery("#OtpPopHl").show();
				    	//jQuery("#otpPopHome").hide();
				    	if($("#appApplyingFromIndiaYes").is(':checked')){
				    		jQuery("#otp_verify_type").hide();
				    	}else if($("#appApplyingFromIndiaNo").is(':checked')){
				    		jQuery("#otp_verify_type").show();
				    	}
				    	jQuery('#appApplyingFromIndiaYes').attr("disabled", "disabled");
				    	jQuery('#appApplyingFromIndiaYes').addClass('disabledFields');
				    	jQuery('#appApplyingFromIndiaNo').addClass('disabledFields');
				    	jQuery('#appApplyingFromIndiaNo').attr("disabled", "disabled");
				    	jQuery("#otpPopHome1").hide();
				    	
				    	//$("#appMobileNo").attr("disabled", "disabled");
				    	$("#appAlternateMobileNumber").attr("disabled", "disabled");
				    	
				    	///$('#comncaptcha').attr('name','captcha');
				    	//$('#homecaptcha').attr('name','captcha_1');
				    	
				    	jQuery("#otpPopHome").hide();				    	
				    	jQuery("#OtpPopHl2").show();
				    	//jQuery("#OtpPopHl").show();
				    	//alert('Harsha');
//				    	$("#OtpPopHl1").css('display','block');
				    	$("#OtpPopHl2").css('display','block');	
				    	//alert("homeCaptcha.. " + homecaptcha);
				    	//alert($('#homecaptcha').get(0));
				    	//refreshCaptcha('CommonOtpcapImage');
			    	}else if(stringMessage[0]=="error"){
			    		//refreshCaptcha('CommonOtpcapImage');
			    		showPOPMsg('errorOTPMsg1',stringMessage[1], false);
		        		return false;
			    	}
			    },
			    error:function(jqXHR, textStatus){
			    	removeDynamicLoader('sendOptAlt');
			    	showPOPMsg('errorOTPMsg1',SORRY_FOR_INCONVENIENCE, false);
					return false;
			    }
			});			
		}		
		return false;
	});

	//Start
								
	jQuery(document).on("click","#appApplyingFromIndiaNo", function() {
		var tooltipMessage="";
		if(!isDsrPage){
			tooltipMessage="M.toolTip_TM('Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.', event, this, 130, 25, 0);";
		}
		var nrimobilediv=''
			+'<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" onfocus="'+tooltipMessage+'" class="form-control" id="appNRIMobileNo" value="" maxlength="12" name="nriMobileNo">'
			+'</li>';
		jQuery("#mobileDetailsOTP").html(nrimobilediv);
		jQuery("#appOTPVerifyTypeYes").trigger("click");
		$('#isdCode').val(91);
	});

	jQuery(document).on("click","#appApplyingFromIndiaYes", function(){
		var mobilediv=''
			+'<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" id="mobile" value="" maxlength="10" name="mobile" class="form-control">'
			+'</li>';
		jQuery("#mobileDetailsOTP").html(mobilediv);
		jQuery("#appOTPVerifyTypeYes").trigger("click");
		$('#isdCode').val(91);
	});

	jQuery(document).on("click","#appOTPVerifyTypeYes", function() {
		var mobilediv=''
			+'<li><label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label>'
			+'<input id="inputOtp" name="inputOtp" class="form-control secure-otp" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control">'
			+'<input id="inputOtp1" name="inputOtp" type="hidden">'
			+'</li>';
		jQuery("#otp_row_confirm_html").html(mobilediv);
	});

	jQuery(document).on("click","#appOTPVerifyTypeNo", function(){
		var mobilediv=''
			+'<li><label><span id="otpLabel">Enter code sent by Email<b class="req"> *</b></span></label>'
			+'<input id="inputOtpEmail" name="inputOtpEmail" type="text" class="form-control" maxlength="6" onkeydown="return M.isAlphaNumeric(event);" class="form-control">'
			+'</li>';
		jQuery("#otp_row_confirm_html").html(mobilediv);
	});


});



/*end*/

