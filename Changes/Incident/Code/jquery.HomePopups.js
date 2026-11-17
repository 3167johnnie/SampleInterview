$(document).ready(function(){
	
	jQuery("#refresh_link_Captche").on("click", function(){
		//	console('application status 2');
			
	    return refreshCaptcha('HomePopcapImage');
		});
	jQuery("#HOMEPocapImage").on("click", function(){
		//	console('application status 2');
		
		return refreshCaptcha('HomePopcapImage');
	});
	jQuery("#leadLoanPurposeId").on("change", function(){
		//	console('application status 2');
		
		onchange="consentCallForAutoLoan()"
	});
	jQuery("#homePageCallback").on("submit", function(){
		//	console('application status 2');
		
		return submit_first_page_bind();
	});
	jQuery("#leadFirstName").on("keydown", function(){
		//	console('application status 2');
		
	   return M.isChars(event);
	});
	jQuery("#mobile").on("keydown", function(){
		//	console('application status 2');
		
		return M.digit(event);
	});
	jQuery("#inputOtpWantUs").on("keydown", function(){
		//	console('application status 2');
		
		return M.digit(event);
	});
	
	

	function fetchJsonData(){
		jsonJSArray = '<s:property value="%{jsonJSArray}"/>';
		jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
		jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
		jsonObject = jQuery.parseJSON(jsonJSArray);
		return jsonObject;
	}
	min_age_year = 18;
	max_age_year = 70;
	
	$("#getACallBack").modal({
		show:false,
		backdrop:"static",
		keyboard:false
	});
	function getACallBackId(){
		$("#captcha").val('');
		refreshCaptcha('HOMEPocapImage');
		reinitiateCallback();
		$("#getACallBack").modal();
	}

	function reinitiateCallback(){
		$("#leadLoanPurposeId").val(0).removeClass('error').next('span').remove();
		$("#leadFirstName").val('').removeClass('error').next('span').remove();
		$("#leadApplyingFromIndiaWantUsYes").trigger("click");
		$("#leadCityId").val(0).removeClass('error').next('span').remove();
		$("#leadLanguageId").val(0).removeClass('error').next('span').remove();
		$("#mobile").val('').removeClass('error').next('span').remove();
		$("#mobile").removeAttr('readonly');
		$("#leadWorkEmail").val('').removeClass('error').next('span').remove();
		$("#infoprovide").removeClass('error');
		$("#infoprovide").attr('checked', false);
		$("#beforeGetACallBack").show();
		$("#afterGetACallBack").hide();
		hidePOPMsg('errorOTPMsg');
	}

	

	
	
	function consentCallForAutoLoan(){
		var autoLoan  = $("#leadLoanPurposeId").val();

		if (autoLoan == 3) {
			$('#consentCallForAllLoan').hide();
			$('#consentCallForAutoLoan').show();
	   }
	   else {
		  $('#consentCallForAllLoan').show();
		  $('#consentCallForAutoLoan').hide(); 
	   }
	}
	
});
