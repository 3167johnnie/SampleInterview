var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";


function changeQutotes(){
	$("#capturedValue input, #capturedValue select").prop('disabled',false);
	var paramData= getRequest('capturedValue', 0);
	if(!paramData){
		var errorMsg = "Sorry for the inconvenience, please try again later.";
		showMsg(errorMsg,false);
		return false;
	}
	var formdata = "params="+paramData;
	formdata +="&requestType=2&requestIndex=2";
	if(uiType!=undefined && uiType!=""){
		formdata += "&uiType="+uiType;
	}
	validateRulesCapturedValue();
	if(!$("#capturedValue").valid()){
		hideMsg();
		jQuery('#loan-quote-loader').hide();
		return false;
	}
	$("#capturedValue input, #capturedValue select, #capturedValue radio").attr('disabled',true);
	jQuery.ajax({
        type:'post',
        url: ajaxPostUrl,
        data: formdata,
        beforeSend:function(){
        	$('#loan-quote-loader').show();
		},
		complete:function(){
			$('#loan-quote-loader').hide();
		},
        success:function(htmlContent){
        	$("#capturedValue input, #capturedValue select, #capturedValue radio").attr('disabled',false);
        	if(htmlContent==null || htmlContent ==undefined || htmlContent == ""){
        		showMsg(SORRY_FOR_INCONVENIENCE,false);
        		jQuery('#loan-quote-loader').hide();
        		jQuery("#applyDiscountCoupon").removeAttr("disabled");
    	        return false;
        	}
        	var stringMessage = [];
        	stringMessage = htmlContent.split("|");
    		if(stringMessage[0] == "error"){
    			showMsg(stringMessage[1],false);
        		jQuery('#loan-quote-loader').hide();
        		jQuery("#applyDiscountCoupon").removeAttr("disabled");
        		goToByScroll("msg-panel");
    	        return false;
        	}else{
            	jQuery("#secondPageContent").html(htmlContent);
            	$("#content-2").mCustomScrollbar({
        			//autoHideScrollbar:true,
        			theme:"rounded",
        			scrollInertia:5
        		});
        	}
    		jQuery("#applyDiscountCoupon").removeAttr("disabled");
    		jQuery('#loan-quote-loader').hide();
        },
        error:function(data){
        	$("#capturedValue input, #capturedValue select, #capturedValue radio").attr('disabled',false);
        	jQuery('#loan-quote-loader').hide();
        	showMsg(SORRY_FOR_INCONVENIENCE,false);
    		goToByScroll('msg-panel');
    		return false;
        }
    });
	return false;
}

function applyLoanOffer(src, appliedLoanId, chosenLoanAccountType){
	noNeedToShowPopup=1;
	jQuery("#thirdPageContent").hide();
	jQuery("#thirdPageContent").html('');
	jQuery("#fourthPageContent").hide();
	jQuery("#fourthPageContent").html('');
	var elementId=src.id;
	var mp=M.byId('msg-panel');
	mp.style.display="none";
	var formData="";
	if(isDsrPage){
		formData="appliedLoanId="+appliedLoanId+"&chosenLoanAccountType="+chosenLoanAccountType;
	}else{
		formData="email="+$('#emailSendQuote').val()+"&appliedLoanId="+appliedLoanId
		+"&chosenLoanAccountType="+chosenLoanAccountType;
	}
	var paramData= getRequestWithoutForm(formData);
	if(!paramData){
		var errorMsg = "Sorry for the inconvenience, please try again later.";
		showMsg(errorMsg, false);
		return false;
	}
	var formdata = "params="+paramData;
	formdata +="&requestIndex=13&requestType=2";
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
        	hideLoader();
        	removeDynamicLoader(elementId);
        	$("#capturedValue input, #capturedValue select, #capturedValue radio").attr('disabled',false);
        	if(htmlContent==null || htmlContent ==undefined || htmlContent ==""){
        		showMsg(SORRY_FOR_INCONVENIENCE,false);
        		goToByScroll('msg-panel');
        		return false;
        	}
        	var stringMessage = [];
        	stringMessage = htmlContent.split("|");
    		if(stringMessage[0] == "error"){
    	    	removeDynamicLoader(elementId);
    	    	showMsg(stringMessage[1],false);
    	        return false;
        	}else{
        		jQuery("#thirdPageContent").html(htmlContent);
        		if(isDsrPage){
        			$("#changeLoanQuote").addClass("hide");
        			$("#editLoanQuote").removeClass("hide");
        			jQuery("#thirdPageContent").show();
        			$("#capturedValue input, #capturedValue select").attr('disabled',true);
            		$("#loanScenarioBean_manualEligVal").prop('disabled',false);
            		$("#loanScenarioBean_manualTenureVal").prop('disabled',false);
        			disableById("editLoanQuote", false);
            		getCallLogDetails("msg-panel");
            		
        		}else{
        			jQuery("#secondPageContent").hide();
					jQuery("#thirdPageContent").show();
					window.scrollTo(0, 0);
					$("#content-3").mCustomScrollbar({
						theme:"rounded",
						scrollInertia:5
					});
					if(isMobileVerified!='Y'){
						show_hide_toggle("#otpPopHome", 0);
				    	jQuery("#otpPopHome").show();
				    	jQuery("#OtpPopHl").hide();
						refreshCaptcha('homeCommonOtpcapImage');
		        		$('#comncaptcha').attr('name','captcha_1');
		        		$('#homecaptcha').attr('name','captcha');
					}
        		}
        	}
        },
        error:function(data){
        	removeDynamicLoader(elementId);
        	showMsg(stringMessage[1],false);
    		return false;
        }
    });
	return false;
}

function validatorRule(){
	var elementArrayList = [];
	var elementValidationRule = [];
	elementId = "loanPurpose";
	elementValidationRule.push(elementId);
	isMandatory = 1;
	elementValidationRule.push(isMandatory);	
	isMandatory = 1;
	elementValidationRule.push(isMandatory);
	elementArrayList.push(elementValidationRule);	
	return false;
}

$(document).ready(function(){
	$("#call_back").validate({
		rules: {
			name: {
				required: true
		    },
		    mobileWantUsToCallYou: {
				required: true,
				mobile : true
		    },
		    nriMobile: {
				required: true,
				nriMobile : true
		    },
		    email: {
				required: true,
				email:true
		    },
		    captcha: {
				required: true
		    }
		}
	});

	jQuery("#editLoanQuote").unbind("click").bind("click",function(){
		$("#capturedValue input, #capturedValue select").attr('disabled',false);
		jQuery('#thirdPageContent').html('');
		jQuery('#thirdPageContent').hide();
		jQuery('#fourthPageContent').html('');
		jQuery('#fourthPageContent').hide();
		$("#editLoanQuote").addClass("hide");
		$("#changeLoanQuote").removeClass("hide");
	});
	
	jQuery(".loanAccountType").unbind("click").bind("click",function(){
		var parentRow=this.parentNode.parentNode.parentNode.parentNode;
		var chosenProductId = jQuery(parentRow).prop('id');
		var splittedProductId = chosenProductId.split("-");
		if(BANK_ID == BANK_ID_SBI){
			
		}else{
			if(this.value==1){
				jQuery("#accountTypeTerm_"+splittedProductId[0]+this.value).prop("checked",true);
			}else if(this.value==2){
				jQuery("#accountTypeOverdraft_"+splittedProductId[0]+this.value).prop("checked",true);
			}else{
				jQuery("msg-panel").html(SORRY_FOR_INCONVENIENCE);
				jQuery("msg-panel").show();
			}
		}
		jQuery(this).prop("checked", true);
		jQuery("#chosenProductId").val(splittedProductId[0]);
		jQuery("#chosenLoanAccountType").val(this.value);
		changeQutotes();
		return false;
	});

	jQuery("#sendQuote").unbind("click").bind("click",function(){
		$('#sendQuote').attr('class','send-btn');
		$("#sendQuote").prop("disabled","disabled");
		$('#sendQuote').attr('class','send-btn-process');
		M.byId("loaderEmail").innerHTML = '';
		M.byId("loaderEmail").disabled = true;
	    if(M.byId('emailSendQuote').value == "") {
	        M.byId('emailSendQuote').style.borderColor = "red";
	        M.byId("loaderEmail").innerHTML = '';
	        M.byId("loaderEmail").disabled = false;
	        $("#sendQuote").removeAttr('disabled');
	        $('#sendQuote').attr('class','send-btn');
	        return false;
	    }
	    if(!M.isEmail(M.byId('emailSendQuote').value.toString())) {
	        M.byId('emailSendQuote').style.borderColor = "red";
	        M.byId("loaderEmail").innerHTML = '';
	        M.byId("loaderEmail").disabled = false;
	        $("#sendQuote").removeAttr('disabled');
	        $('#sendQuote').attr('class','send-btn');
	        return false;
	    }
	    var formData="email="+M.byId('emailSendQuote').value.toString()+"&productSaveEmail="+M.byId('productSaveEmail').value.toString();
	    var paramData= getRequestWithoutForm(formData);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showMsg(errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=6&requestType=2";
	    if(uiType!=undefined && uiType!=""){
	    	formdata += "&uiType="+uiType;
		}
	    jQuery.ajax({
	        type:'post',
	        url: ajaxPostUrl,
	        data: formdata,
	        //headers : {"Accept-Encoding" : "gzip"},
	        success:function(htmlContent){
	        	$("#sendQuote").removeAttr('disabled');
	        	if(htmlContent==null || htmlContent ==undefined){
	        		M.byId("loaderEmail").innerHTML = '';
	        		showMsg(stringMessage[1],false);
	        		$('#sendQuote').attr('class','send-btn');
	        		return false;
	        	}
	        	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
		    	if(stringMessage[0]=="success"){
		    		M.byId("loaderEmail").innerHTML = '<div style="width:120px; color:green;">'+stringMessage[1]+'</div>';
		    		$("#sendQuote").removeAttr('disabled');
	        		$('#sendQuote').attr('class','send-btn-success');
	        	}else{
	        		M.byId('loaderEmail').innerHTML='<div style="width:120px; color:red;">'+stringMessage[1]+'</div>';
	        		$("#sendQuote").prop("disabled","disabled");
	        		$("#emailSendQuote").prop("disabled","disabled");
	        		$('#sendQuote').attr('class','send-btn-failure');
	        	}
	        },
	        error:function(data){
	        	M.byId("loaderEmail").innerHTML = '';
	        	showMsg(stringMessage[1],false);
	    		$('#sendQuote').attr('class','send-btn-failure');
	        }
	    });
		return false;
	});

	jQuery("#applyDiscountCoupon").unbind("click").bind("click",function(){	
		M.byId("loaderDiscount").innerHTML='';
		M.byId("loaderDiscount").innerHTML = '<div style="width:150px;"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"></div>';
		jQuery("#applyDiscountCoupon").attr("disabled","disabled");
	    if(M.byId('discountCouponName').value == "") {
	        M.byId('discountCouponName').style.borderColor = "red";
	        M.byId("loaderDiscount").innerHTML = '<div style="width:150px; color:red;">No discount code entered</div>';
	        jQuery("#applyDiscountCoupon").removeAttr("disabled");
	        return false;
	    }else{
	    	 M.byId("loaderDiscount").innerHTML = '<div style="width:150px;"></div>';
		}
		changeQutotes();
		M.byId("loaderDiscount").innerHTML = '<div style="width:150px;"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"></div>';
		return false;
	});
	
	jQuery("#sendOptWantUs").unbind("click").bind("click",function(){
		$("#call_back").submit();
		return false;
	});

	jQuery('#changeLoanQuote').unbind('click').bind("click",function(){
		changeQutotes();
		return false;
	});

	jQuery('#call_back').unbind('submit').bind("submit",function(){
		if(noNeedToShowPopup){
			return false;
		}
		hidePOPMsg('errorWantUsToCall');
		showDynamicLoader('confirmOtpWantUs','after');
		if($("#call_back").valid()){
			var mobile = jQuery('#mobileWantUsToCallYou').val();
			if(!M.isMobile(mobile)){
				showPOPMsg('errorWantUsToCall','Invalid mobile number.', false);
	    		show_hide_toggle("#wantus_row_confirm", 0);
		    	show_hide_toggle("#wantus_overlay-row_otp", 0);
		    	return false;
			}
			var paramData= getRequest('call_back', 0);
			if(!paramData){
				var errorMsg = "Sorry for the inconvenience, please try again later.";
				showPOPMsg('errorWantUsToCall',errorMsg, false);
				return false;
			}
			showDynamicLoader('callBankSubmit');
			var formdata = "params="+paramData;
			formdata +="&requestIndex=3&requestType=2";
      
      
			if(uiType!=undefined && uiType!=""){
				formdata += "&uiType="+uiType;
			}
			jQuery.ajax({
			    type: "POST",
				url: ajaxPostUrl,
				data: formdata,
				//headers : {"Accept-Encoding" : "gzip"},
			    success:function(htmlContent){
			    	removeDynamicLoader('callBankSubmit');
			    	removeDynamicLoader('confirmOtpWantUs');
			    	if(htmlContent==null || htmlContent ==undefined){
			    		removeDynamicLoader('errorWantUsToCall');
			    		showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
		        		return false;
		        	}
			    	var stringMessage = [];
			    	
					if (isEncrypted(htmlContent)) {
						decryptedResponse = getDecryptedResponse(htmlContent);
						stringMessage = decryptedResponse.split("|");
					} else {
						stringMessage = htmlContent.split("|");
					}
					
			    	if(stringMessage[0]=="success"){
			    		jQuery("#emailSendQuote").val(jQuery("#email").val());
			    		$("#overlay-row_otp").hide();
			    		$("#callBackInformation").hide();
			    		$('#captcha').attr('name','captcha');
			    		$("#wantus_row_confirm").show();
			    		refreshCaptcha('HomeLoancapImage');
			    	}else if(stringMessage[0]=="error"){
			    		showPOPMsg('errorWantUsToCall',stringMessage[1], false);
			    		show_hide_toggle("#wantus_row_confirm", 0);
				    	show_hide_toggle("#wantus_overlay-row_otp", 0);				    	
			    	}
			    },
			    error:function(jqXHR, textStatus){
			    	removeDynamicLoader('errorWantUsToCall');
		    		showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
			    }
			});
			return false;
		}
		return false;
	});
});
	jQuery("a.product-offers").unbind("click").bind("click",function(e){
		e.preventDefault();
		$("#productoffer").modal('toggle');
	});
	jQuery("a.call_us").unbind("click").bind("click",function(e){
		e.preventDefault();
		$("#callBackInformation").show();
		$("#overlay-row_otp").show();
		refreshCaptcha('Homeinformation');
		$("#wantus_row_confirm").hide();
		$("#productCallBack").modal('toggle');
		$('#captcha').attr('name','captcha_1');
		
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

	function  validateWantUstoCallyou(src, typeOfValidation){
		var flag = 0;
		  var willPost=1;
		  if(typeOfValidation ==1 || typeOfValidation == 2){
			  if(M.byId('mobileWantUsToCallYou') != undefined) {
			        if(M.byId('mobileWantUsToCallYou').value == 0 || M.byId('mobileWantUsToCallYou').value == ''|| validationFormobileWantUsToCallYou(M.byId('mobileWantUsToCallYou').value)) {
			            M.byId('mobileWantUsToCallYou').style.borderColor=C[5];
			            willPost=0;;
			            jQuery("#errorWantUsToCall").html("Please enter the valid mobile number");
			        }else{
			        	M.byId('mobileWantUsToCallYou').style.borderColor=C[8];
			        }
			    }
			  if(M.byId('email') != undefined) {
			        if(M.byId('email').value == 0 || M.byId('email').value == ''|| !ValidateEmail(M.byId('email').value)) {
			            M.byId('email').style.borderColor=C[5];
			            willPost=0;;
			            jQuery("#errorWantUsToCall").html("Please enter the valid email");
			        }else{
			        	M.byId('email').style.borderColor=C[8];
			        }
			    }
			  if(M.byId('inputOtpWantUs') != undefined) {
			        if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
			            M.byId('inputOtpWantUs').style.borderColor=C[5];
			            willPost=0;;
			            jQuery("#errorWantUsToCall").html("Please enter otp");
			        }else{
			        	M.byId('inputOtpWantUs').style.borderColor=C[8];
			        }
			    }
		  }
		  	if(typeOfValidation == 3){
			  if(M.byId('inputOtpWantUs') != undefined) {
			        if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
			            M.byId('inputOtpWantUs').style.borderColor=C[5];
			            willPost=0;;
			            jQuery("#errorWantUsToCall").html("Please enter OTP");
			        }else{
			        	M.byId('inputOtpWantUs').style.borderColor=C[8];
			        }
			    }
			  if(willPost==1 && M.byId('captcha') != undefined) {
			        if(M.byId('captcha').value == 0 || M.byId('captcha').value == '' || M.byId('captcha').value.length!=6) {
			            M.byId('captcha').style.borderColor=C[5];
			            willPost=0;;
			            jQuery("#errorWantUsToCall").html("Please enter Captcha");
			        }else{
			        	M.byId('captcha').style.borderColor=C[8];
			        }
			    }
		  }
		  if(willPost){
			  return true;
		  }else{
			  return false;
		  }
		
	}

	//jQuery(document).on("click","#confirmOtpWantUs", function(){
	jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
		hidePOPMsg('errorWantUsToCall');
		showDynamicLoader('confirmOtpWantUs');
		if(!validateWantUstoCallyou(this, 3)){
			  jQuery("#errorWantUsToCall").show();
			  removeDynamicLoader('confirmOtpWantUs');
			  return false;
		}
		
		var inputOtpWantUs=document.getElementById('inputOtpWantUs').value;
		 if(inputOtpWantUs!="" || inputOtpWantUs !=''){
	var encryptedOtp = getEncryptedStr(inputOtpWantUs);
    
	document.getElementById('inputOtpWantUs1').value = encryptedOtp;
		 }
		var paramData= getRequest('call_back', 1);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorWantUsToCall',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=5&requestType=2";
    
    		
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
			 beforeSend:function(){
	        	//$('#page-loader').show();
			},
			complete:function(){
				//$('#page-loader').hide();
			},
		    success:function(htmlContent){
		    	removeDynamicLoader('confirmOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
        	        return false;
	        	}
		    	var stringMessage = [];
	        	//stringMessage = htmlContent.split("|");
	        	
	        	///var stringMessage = [];
		    	
				if (isEncrypted(htmlContent)) {
					decryptedResponse = getDecryptedResponse(htmlContent);
					stringMessage = decryptedResponse.split("|");
				} else {
					stringMessage = htmlContent.split("|");
				}
	        	
	        	
	        	if(stringMessage[0]=="success"){
	        		isMobileVerified="Y";
	        		jQuery("#wantus_overlay-row_otp").hide();
	        		jQuery("#wantus_row_confirm").hide();
	        		noNeedToShowPopup=1;
	        		var successMsg='<div class="thnk-msg">'
						+'<h2><span class="smile-icon"></span><br>Thank you </h2>'
						+'<p>Thank You for your interest. Our representative will contact you shortly.</p>'
						+'</div>';
		        		$("#errorWantUsToCall").removeClass('error-msg-cbs')
		        		$("#errorWantUsToCall").removeClass('success-msg-cbs')
		        		$("#errorWantUsToCall").html(successMsg).show();
	        		setTimeout(function(){
	        			$("#productCallBack").modal('hide');
	        			dropOffPopupStatus=1;
	        		}, 2000);
	        	}else if(stringMessage[0]=="error"){
	        		showPOPMsg('errorWantUsToCall',stringMessage[1], false);
	        		jQuery("input[name='"+stringMessage[2]+"']").addClass('error');
	        	}
	        	return false;
		    },
		    error:function(jqXHR, textStatus){
		    	showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
    	        return false;
		    }
		});
		
		return false;
	});
	jQuery(document).on("click","#resendOtpWantUs", function(){
		removeDynamicLoader('resendOtpWantUs');
		showDynamicLoader('resendOtpWantUs');
		hidePOPMsg('errorWantUsToCall');
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorWantUsToCall',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
        	        return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		refreshCaptcha('HomeLoancapImage');
	        		showPOPMsg('errorWantUsToCall','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
			    	if(isDsrPage){
	            		getCallLogDetails("errorWantUsToCall");
	        		}
			    	return false;
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomeLoancapImage');
		    		showPOPMsg('errorWantUsToCall',stringMessage[1], false);
		    	}
		    },
		    error:function(jqXHR, textStatus){
		    	showPOPMsg('errorWantUsToCall',SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
		return false;
	});
	
  
  function getEncryptedStr(str) {
	
	try {
		
		
		var aesUtil = new AesUtil(keySize, iterationCount);
		var enc1 = aesUtil.encrypt(salt, iv, passPhrase, str);
		
		
		return enc1;
	} catch (err) {
		return false;
	}
}
	jQuery(document).on("click","#appApplyingFromIndiaWantUsNo", function() {
		var tooltipMessage="";
		if(!isDsrPage){
			tooltipMessage="M.toolTip_TM('Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.', event, this, 130, -10, 0);";
		}
		var nrimobilediv='<div class="div-left" id="nri-mobile-div">'
			+'<label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" style="width:170px;" class="nriMobile" id="nriMobileWantUsToCallYou" value="" maxlength="12" name="nriMobileWantUsToCallYou" onfocus="'+tooltipMessage+'">'
			+'</div><span class="privacy-note"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/lock-img.jpg" /> Your privacy is protected</span>';
		jQuery("#mobileDetailsWantUs").html(nrimobilediv);
	});

	jQuery(document).on("click","#appApplyingFromIndiaWantUsYes", function(){
		var nrimobilediv='<div class="div-left" id="nri-mobile-div">'
			+'<label>Enter your Mobile No.&nbsp;<b class="req">*</b></label>'
			+'<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" style="width:147px;" class="mobile" id="mobileWantUsToCallYou" value="" maxlength="10" name="mobileWantUsToCallYou">'
			+'</div><span class="privacy-note"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/lock-img.jpg" /> Your privacy is protected</span>';
		jQuery("#mobileDetailsWantUs").html(nrimobilediv);
	});

	/*jQuery(document).on("click","#callBankSubmit", function(){
		refreshCaptcha('HomeLoancapImage');
	});*/
	
