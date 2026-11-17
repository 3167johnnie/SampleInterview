var allLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var activeLanguages = [{'key':0, 'value':'Select Preferred Language'},{'key':1, 'value':'Hindi'},{'key':2, 'value':'English'}];

var error_msg = "";
var globalMobile ="";
var isValidForHomePageOTP=true;

//context path changes
var ctxPath = window.location.pathname;
if (ctxPath != '/personal-banking') {
	ctxPath = ctxPath.substring(0, window.location.pathname.indexOf("/",2));
}

var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

function  validateWantUstoCallyou(src, typeOfValidation){
	var flag = 0;
	var willPost=1;
	if(typeOfValidation ==1 || typeOfValidation == 2){
		if(M.byId('mobile') != undefined) {
			if(M.byId('mobile').value == 0 || M.byId('mobile').value == ''|| validationForMobile(M.byId('mobile').value)) {
				M.byId('mobile').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid mobile number", false);
	        }else{
	        	M.byId('mobile').style.borderColor=C[8];
	        }
		}
		if(M.byId('email') != undefined) {
			if(M.byId('email').value == 0 || M.byId('email').value == ''|| !ValidateEmail(M.byId('email').value)) {
				M.byId('email').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the valid email", false);
			}else{
				M.byId('email').style.borderColor=C[8];
			}
	    }
		if(M.byId('inputOtpWantUs') != undefined) {
			if(M.byId('inputOtpWantUs').value == 0 || M.byId('inputOtpWantUs').value == '' || M.byId('inputOtpWantUs').value.length!=6) {
				M.byId('inputOtpWantUs').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter the correct otp", false);
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
				showPOPMsg('errorOTPMsg',"Please enter OTP", false);
	        }else{
	        	 M.byId('inputOtpWantUs').style.borderColor=C[8];
	        }
	    }
		if(willPost==1 && M.byId('otpHomecaptcha') != undefined) {
			if(M.byId('otpHomecaptcha').value == 0 || M.byId('otpHomecaptcha').value == '' || M.byId('otpHomecaptcha').value.length!=6) {
				M.byId('otpHomecaptcha').style.borderColor=C[5];
				willPost=0;;
				showPOPMsg('errorOTPMsg',"Please enter Captcha", false);
	        }else{
	        	M.byId('otpHomecaptcha').style.borderColor=C[8];
	        }
	    }
	}
	if(willPost){
		return true;
	}else{
		return false;
	}
}

function submit_first_page_bind(){
	jsonObject = fetch_validation_object(3,0);
	addValidationsRules(document.homePageCallback, jsonObject);
    if(jQuery("form#homePageCallback").valid()){
    	if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
    		validateNriMobileNo('appNRIMobileNo');
		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
			validateIndianMobileNo('mobile');
		}else{
			isValidForHomePageOTP=false;
		}
    	/*if(!isValidForHomePageOTP){
    		return false;
    	}*/
    	hidePOPMsg('errorOTPMsg');
		jQuery("#resendOtpWantUs").show();
		/*var formdata = "params="+getRequest('homePageCallback', 1);
		formdata +="&requestIndex=3&requestType=2";*/
		var paramData= getRequest('homePageCallback', 1);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=3&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
	        type:'post',
	        url: ctxPath + "/" + ajaxPostUrl,
	        data: formdata,
	        //headers : {"Accept-Encoding" : "gzip"},
	        beforeSend:function(){
	        	removeDynamicLoader('callBankSubmit');
				showDynamicLoader('callBankSubmit');//show loader
			},
			complete:function(){
				removeDynamicLoader('callBankSubmit'); //remove loader
			},
	        success:function(htmlContent){
	        	if(htmlContent!=""){
	            	var stringMessage = [];
					var decryptedResponse = "";
					
					if (isEncrypted(htmlContent)) {
						decryptedResponse = getDecryptedResponse(htmlContent);
						stringMessage = decryptedResponse.split("|");
						
					} else {
						stringMessage[0] == "error"
						stringMessage[1] == "Something went wrong!"
						stringMessage[2] == "captcha"
						
					}
						//stringMessage = htmlContent.split("|");

	        		if(stringMessage[0] == "error"){
	        			showPOPMsg('errorOTPMsg','<b>Error: </b><em>'+stringMessage[1]+'</em>', false);
	        	        return false;
	            	}else if(stringMessage[0] != "success"){
	            		showPOPMsg('errorOTPMsg','<b>Error: </b><em>Something went wrong!</em>', false);
	            	}else{
	            		//secretKey=getSecretKey();
	            		$("#beforeGetACallBack").hide();
	            		$("#afterGetACallBack").show();
	            		
	            		if(window.matchMedia("only screen and (max-width: 768px)").matches) {
	            			console.log('asdsa');
	            			document.getElementsByClassName("otp-right-section")[0].style.height = "330px";
	            		}
	            		
	            		
	            		jQuery("#callbackBody").removeClass("form-tab");
	            		var resendHtml='';
	            		if(jQuery("#leadApplyingFromIndiaWantUsNo").is(":checked")){
	            			globalMobile = $("#appNRIMobileNo").val();
	            			resendHtml = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
	            			
	            		}else if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
	            			globalMobile = $("#mobile").val().replace(/^(\d{6})(\d{4})$/, function() {
	            			  return arguments[1].replace(/./g, '*') + arguments[2];
	            			});
	            			resendHtml = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
	            		}else{
	            			return false;
	            		}
	            		jQuery("#resend_box").html(resendHtml);
	            		jQuery("#resend_box").show();
		            	jQuery("#inputOtpWantUs").val('');
		            	jQuery("#captcha").val('');
		            	jQuery("#otpHomecaptcha").val('');
		            	refreshCaptcha('HomePopcapImage');
		            	jQuery("#wantus_row_confirm").show();
		            	return false;
	            	}
	        	}
	        	return false;
	        },
	        error:function(data){
	        	showPOPMsg('errorOTPMsg','<b>Error: </b><em>Sorry for the inconvenience, Please click <a href='/'>here</a> to start again</em>', false);
		        return false;
	        }
	    });
	}
	return false;
}

jQuery(document).ready(function(){
	//secretKey=getSecretKey();
	jQuery("#call_back").validate({
		rules: {
			name: {
				required: true
		    },
		    mobileWantUsToCallYou: {
				required: true
		    },
		    email: {
				required: true,
				email:true
		    }
		}
	});

	jQuery("form#homePageCallback").validate({
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
			if(element.attr("type")=="checkbox"){
				$('.colorAuto').css({ 'outline':'solid','outline-width': '2px', 'outline-color': '#e96e57'});
				return true;
			}else {
				error.insertAfter(element);
			}
		}
	});

	jQuery(document).on("click", "#resendOtpWantUs", function(){
		removeDynamicLoader('resendOtpWantUs');
		showDynamicLoader('resendOtpWantUs');
		$("#resendOtpWantUs").removeAttr('disabled');
	/*	var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";*/
		
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
				
				
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		refreshCaptcha('HomePopcapImage');
	        		if(stringMessage[2]>4){
	        			showPOPMsg('errorOTPMsg',"You have reached maximum number of OTP resend count", false);
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    }
		});
		return false;
	});
	jQuery(document).on("click", "#resendOtp", function(){
		removeDynamicLoader('resendOtp');
		showDynamicLoader('resendOtp');
		$("#resendOtp").removeAttr('disabled');
		var mobile = jQuery("#resendMobile").val();
		if(mobile != undefined && mobile != null && mobile!='') {
			if(jQuery("#leadApplyingFromIndiaWantUsYes").is(":checked")){
				if(!M.isMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
	        		return false;
				}
			}else{
				if(!M.isNRIMobile(mobile)){
					M.byId('resendMobile').style.borderColor=C[5];
					removeDynamicLoader('resendOtp');
					showPOPMsg('errorOTPMsg','Please enter the valid mobile number', false);
					return false;
				}
			}
	    }
		if(mobile!=undefined && mobile!=null && mobile!=''){
			globalMobile = mobile;
		}
		//var formdata = "params="+getRequest('call_back', 0)+"&requestIndex=4&requestType=2";
		var paramData= getRequest('call_back', 0);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=4&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('resendOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('resendOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
				
				var stringMessage = [];
	        	//stringMessage = htmlContent.split("|");
				if (isEncrypted(htmlContent)) {
					//decryptedResponse = window.atob(htmlContent);
					decryptedResponse = getDecryptedResponse(htmlContent);
					stringMessage = decryptedResponse.split("|");
					
				} else {
					
					stringMessage = htmlContent.split("|");
				}
			//	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		showPOPMsg('errorOTPMsg',stringMessage[1], true);
		    		show_hide_toggle("#wantus_row_confirm", 1);
			    	show_hide_toggle("#wantus_overlay-row_otp", 0);
			    	refreshCaptcha('HomePopcapImage');
		    	}else if(stringMessage[0]=="error"){
		    		refreshCaptcha('HomePopcapImage');
		    		showPOPMsg('errorOTPMsg',stringMessage[1], false);
		    	}
	        	if(stringMessage[2]!=undefined && stringMessage[2]!=null){
	        		if(stringMessage[2]>4){
	        			jQuery("#resendOtpWantUs").remove();
	        		}
	        	}
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('resendOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
		return false;
	});
	//jQuery(document).on("click", "#confirmOtpWantUs", function(){
	jQuery("#confirmOtpWantUs").unbind("click").bind("click",function(){
        //removeDynamicLoader('confirmOtpWantUs');
		showDynamicLoader('confirmOtpWantUs');
		$("#confirmOtpWantUs").removeAttr('disabled');
		hidePOPMsg('errorOTPMsg');
		if(!validateWantUstoCallyou(this, 3)){
			removeDynamicLoader('confirmOtpWantUs')
			return false;
		}
		//var formdata = "params="+getRequest('call_back', 1)+"&requestIndex=5&requestType=2";
		var inputOtp=document.getElementById('inputOtpWantUs').value;
		 if(inputOtp!="" || inputOtp !=''){
		var encryptedOtp = getEncryptedStr(inputOtp);
		document.getElementById('inputOtpWantUs1').value = encryptedOtp;
		 }
		var paramData= getRequest('call_back', 1);
		if(!paramData){
			var errorMsg = "Sorry for the inconvenience, please try again later.";
			showPOPMsg('errorOTPMsg',errorMsg, false);
			return false;
		}
		var formdata = "params="+paramData;
		formdata +="&requestIndex=5&requestType=2";
		if(uiType!=undefined && uiType!=""){
			formdata += "&uiType="+uiType;
		}
		jQuery.ajax({
		    type: "POST",
		    url: ctxPath + "/" + ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	removeDynamicLoader('confirmOtpWantUs');
		    	if(htmlContent==null || htmlContent ==undefined){
		    		removeDynamicLoader('confirmOtpWantUs');
		    		showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
		    	var stringMessage = [];
				var decryptedResponse = "";
				
				if (isEncrypted(htmlContent)) {
					decryptedResponse = getDecryptedResponse(htmlContent);
					stringMessage = decryptedResponse.split("|");
					
				} else {
					stringMessage[0] = "error";
					stringMessage[1] = "Something went wrong";
				}
        
        //	stringMessage = htmlContent.split("|");
					
	        	if(stringMessage[0]=="success"){
	        		//secretKey=getSecretKey();
	        		if(jQuery("#leadLoanPurposeId").val()==1){
	        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
	        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=HOME" width="1" height="1" />';
	        			}
	        		}else if(jQuery("#leadLoanPurposeId").val()==3){
	        			if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
	        				M.byId('go2CloudforcallBack').innerHTML='<img src="https://poly.go2cloud.org/aff_l?offer_id=442&adv_sub=AUTO" width="1" height="1" />';
	        			}
	        		}
	        		jQuery("#wantus_row_confirm").hide();
	        		//showPOPMsg('errorOTPMsg','<img src="'+BANK_IMAGE_FOLDER+'/tick.png">'+stringMessage[1], true);
	        		console.log('sadsa');
	        		$("#afterGetACallBack").hide();
	        		var successMsg='<div class="thnk-msg">'
					+'<h2><span class="smile-icon"></span><br>Thank you </h2>'
					+'<p>Thank You for your interest. Our representative will contact you shortly.</p>'
					+'</div>';
	        		$("#errorOTPMsg").removeClass('error-msg-cbs')
	        		$("#errorOTPMsg").html(successMsg).show();
	        		jQuery("#resend_box").hide();
   	        		setTimeout(function(){
	        			jQuery(".close").trigger("click");
	        			reinitiateCallback();
	        		}, 2000);
   	        		if(DEPLOYMENT_MODE!='local' && DEPLOYMENT_MODE!='uat_rp'){
	        			fbq('track', 'Lead', { loan : 'Callback' });
	        		}
	        	}else if(stringMessage[0]=="error"){
	        		showPOPMsg('errorOTPMsg',stringMessage[1], false);
	        	}
	        	return false;
		    },
		    error:function(jqXHR, textStatus){
		    	removeDynamicLoader('confirmOtpWantUs');
		    	showPOPMsg('errorOTPMsg',SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
		return false;
	});
	
	jQuery(document).on("click","#leadApplyingFromIndiaWantUsNo", function() {
		var nrimobilediv='<input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" class="w167 valid" id="appNRIMobileNo" value="" maxlength="12" name="lead.leadMobileNo" onfocus="M.toolTip_TM(\'Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.\', event, this, 130, -30, 5);">';
		jQuery("#mobileDetails").html(nrimobilediv);
		jQuery("#leadCountryId").val(0);
		jQuery("#country-div").show();
		jQuery("#city-div").hide();
	});

	jQuery(document).on("click","#leadApplyingFromIndiaWantUsYes", function(){
		var mobilediv='<input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" class="w167 valid" id="mobile" value="" maxlength="10" name="lead.leadMobileNo">';
		jQuery("#mobileDetails").html(mobilediv);
		jQuery("#leadCityId").val(0);
		jQuery("#country-div").hide();
		jQuery("#city-div").show();
		jQuery("#cardCityGroup1").val(0);
		
	});
	
	jQuery(document).on("click","#cancelResend", function(){
		refreshCaptcha('HomePopcapImage');
		globalMobile = globalMobile.replace(/^(\d{6})(\d{4})$/, function() {
			return arguments[1].replace(/./g, '*') + arguments[2];
		});
		var html = globalMobile + ' not your number? <a id="changeMobileNumber" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("click", "#changeMobileNumber", function(){
		refreshCaptcha('HomePopcapImage');
		$("#resendOtpWantUs").removeAttr('disabled');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="mobile" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">send</button>'+
				'<button class="resend-btn" id="cancelResend" name="cancelResend" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});


	//jQuery("#cancelResendNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#cancelResendNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html = globalMobile+' not your number? <a id="changeMobileNumberNRI" href="javascript:void(0);">Click here</a> to change.';
		jQuery("#resend_box").html(html);
	});

	//jQuery("#changeMobileNumberNRI").unbind("click").bind("click",function(){
	jQuery(document).on("click","#changeMobileNumberNRI", function(){
		refreshCaptcha('HomePopcapImage');
		var html =
			'<li>'+
				'<label>Enter your Mobile No.<b class="req">*</b></label>'+
				'<input id="resendMobile" name="nriMobileNo" class="form-control" maxlength="10" onkeydown="return M.digit(event);" type="text">'+
			'</li>'+
			'<li>'+
				'<button class="track-btn conf-track-btn" id="resendOtp" name="resendOtp" type="submit">Resend</button>'+
				'<button class="resend-btn" id="cancelResendNRI" name="cancelResendNRI" type="submit">Cancel</button>'+
			'</li>';
		jQuery("#resend_box").html(html);
	});
	jQuery(document).on("change","#leadCityId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	jQuery(document).on("change","#leadCountryId", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#appNRIMobileNo").val("");
	});
	jQuery(document).on("change","#cardCityGroup1", function(){
		//jQuery(".otpCodeSend").remove();
		jQuery("#mobile").val("");
	});
	
	jQuery(document).on("click","#retailLoanListing", function(){
		$(".mobile-landing").hide();
		$(".mobile-slider").hide();
		$(".retail-loan-listing").show();
		$('#footer').addClass('sticky-footer');
	});

	jQuery(document).on("click","#homeBack", function(){
		$(".mobile-landing").show();
		$(".mobile-slider").show();
		$(".retail-loan-listing").hide();
		$('#footer').removeClass('sticky-footer');
	});
});
jQuery(document).on("change","#leadLoanPurposeId", function(){
	if(this==undefined)return false;
	var selVal=this.value;
	
	if(selVal!=undefined){
		if(selVal>0){
			/*if(selVal==10){
				$("#citiesLeads").html($("#cardCities").html());
			}else{*/
				$("#citiesLeads").html($("#loanCities").html());
			/*}*/
		}
		return false;
	}
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

function getEncryptedStr(str) {
	
	try {
		var aesUtil = new AesUtil(keySize, iterationCount);
		var enc1 = aesUtil.encrypt(salt, iv, passPhrase, str);
		
		return enc1;
	} catch (err) {
		return false;
	}
}
function validateIndianMobileNo(indianMobileId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
		isValidForHomePageOTP=false;
		return false;
	}
	var mobile = jQuery('#'+indianMobileId).val();
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#mobile").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isMobile(mobile) && !jQuery('#'+indianMobileId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#mobile").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}
function validateNriMobileNo(nriMobieId){
	isValidForHomePageOTP=true;
	jQuery(".otpCodeSend").remove();
	var mobile = jQuery('#'+nriMobieId).val();
	$(".otpCodeSend").remove();
	var leadLoanPurposeId="";
	if(BANK_ID==BANK_ID_SBI){
		leadLoanPurposeId = $("#leadLoanPurposeId").val();
	}else{
		leadLoanPurposeId = $("#leadProductTypeId").val();
	}
	if(leadLoanPurposeId==null || leadLoanPurposeId==undefined || leadLoanPurposeId=="" || leadLoanPurposeId==0){
		if(BANK_ID==BANK_ID_SBI){
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select loan purpose.</span>");
		}
		else{
			jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select product type.</span>");
		}
    	isValidForHomePageOTP=false;
		return false;
	}
	var countryRegion = $("#leadCountryId").val();
	if(countryRegion==null || countryRegion==undefined || countryRegion=="" || countryRegion==0){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Select country code.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	globalMobile = mobile;
	if(mobile == '' || mobile == null || mobile == undefined ){
		jQuery("#appNRIMobileNo").after("<div class=''><span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'>Mobile no should not blank.</span>");
    	isValidForHomePageOTP=false;
		return false;
	}
	if(M.isNRIMobile(mobile) && !jQuery('#'+nriMobieId).hasClass('fade')) {
		isMobileValidate=true;
		return true;
	}else{
		isMobileValidate=false;
		if(!M.isNRIMobile(mobile)){
			jQuery(".otpCodeSend").remove();
			jQuery("#appNRIMobileNo").after("<span class='otpCodeSend'><img src='"+BANK_IMAGE_FOLDER+"/cross.png'> Invalid Mobile No.</span>");
			isValidForHomePageOTP=false;
			return false;
		}
		isMobileValidate=true;
		return true;
	}
}

$(document).on("change","#leadLoanPurposeId", function(){
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	
	var productTypeId=src.value;
	if(productTypeId==10){
		populateLanguageData(activeLanguages, 1, "leadLanguageId", "")
	}else{
		populateLanguageData(allLanguages, 1, "leadLanguageId", "")
	}
});

function populateLanguageData(data, selectedId, elementId){
	jQuery( '#'+elementId ).empty();
	var option= "";
	var selected="";
	for(var index=0; index<data.length;index++){
		var elementdata = data[index];
		option = '<option value="'+elementdata.key+'" '+selected+' >'+elementdata.value+'</option>';
		jQuery('#'+elementId).append(option);
	}
	disableById(elementId, false);
}
function openCallBackFormDiv(){
	jQuery("#tabSticker").addClass("hide-sticker");
	jQuery("#callbackBody").addClass("form-tab");
	jQuery(".home_middle_right_bottom").show();
	jQuery(".mobile-landing").hide();
	jQuery(".mobile-slider").hide();
}
function closeCallBackFormDiv(){
	jQuery("#tabSticker").removeClass("hide-sticker");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery("#callbackBody").removeClass("form-tab");
	jQuery(".home_middle_right_bottom").hide();
	jQuery(".mobile-landing").show();
	jQuery(".mobile-slider").show();
}
