var iv = "F3D46A54E1A2E81068AFCFE13A579711";
var salt = "F3D46A54E1A2E81068AFCFE13A579711";
var keySize = 256;
var iterations = iterationCount = 100000;
var passPhrase = "The quick brown fox jumps over the lazy dog";

function submit_first_page_bind(){
	addVlaidationForAllNewFiled();
	remove_comma(document.homeloanform);
	addValidationsRules(document.homeloanform);
    if($("form#homeloanform").valid()){
    	if(!extraValidationFirstPage()){
    		apply_currency_form(document.homeloanform);
    		window.scrollTo(0, 170);
    		return false;
    	}
    	$(".disabledFields").each(function(){
			$(this).removeAttr('disabled');
		});
    	var coll=jQuery("#co_applicant_relationship1").val();
    	var coll2=jQuery("#co_applicant_relationship2").val();
    	
    	if(coll==0 && coll2==0){
    		jQuery("#co_applicant_relationship1").val(null);
    		jQuery("#co_date_of_birth1").val(null);
    		jQuery("#co_gender1").val(null);
    		jQuery("#co_residentType1").val(null);
    		jQuery("#co_employmentType1").val(null);
    		jQuery("#co_applicant_relationship2").val(null);
    		jQuery("#co_date_of_birth2").val(null);
    		jQuery("#co_gender2").val(null);
    		jQuery("#co_residentType2").val(null);
    		jQuery("#co_employmentType2").val(null);
    	
    	}else if(coll!=0 && coll2==0){
    		jQuery("#co_applicant_relationship2").val(null);
    		jQuery("#co_date_of_birth2").val(null);
    		jQuery("#co_gender2").val(null);
    		jQuery("#co_residentType2").val(null);
    		jQuery("#co_employmentType2").val(null);
    	}
    	setDateFormat(jQuery("#date_of_birth").val(),'date_of_birth');
    	setDateFormat(jQuery("#co_date_of_birth1").val(),'co_date_of_birth1');
    	setDateFormat(jQuery("#co_date_of_birth2").val(),'co_date_of_birth2');
    	var paramData= getRequest('homeloanform', 2);
    	if(!paramData){
    		var errorMsg = "Sorry for the inconvenience, please try again later.";
    		showMsg(errorMsg);
    		return false;
    	}
    	var formdata = "params="+paramData;
    	formdata +="&requestIndex=1&requestType=1";
    	if(uiType!=undefined && uiType!=""){
    		formdata += "&uiType="+uiType;
    	}
    	unsetDateFormat(jQuery("#date_of_birth").val(),'date_of_birth');
    	unsetDateFormat(jQuery("#co_date_of_birth1").val(),'co_date_of_birth1');
    	unsetDateFormat(jQuery("#co_date_of_birth2").val(),'co_date_of_birth2');
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
            	hideLoader();
            	if(htmlContent!=""){
                	var stringMessage = [];
					var decryptedResponse = "";
					
					
					if (isEncrypted(htmlContent)) {
						
						decryptedResponse = getDecryptedResponse(htmlContent);
						
						stringMessage = decryptedResponse.split("|");
					} else {
					   stringMessage = htmlContent.split("|"); 
						//stringMessage[0] == "error"
						//stringMessage[1] == "Something went wrong!"
						//stringMessage[2] == "captcha"
					
					}
					
				
            		
					if(stringMessage[0] == "error"){
                		error_msg=stringMessage[1];
                
                		showMsg(error_msg);
                		if(stringMessage[2]){
                			jQuery("select[name='"+stringMessage[2]+"']").val(0).addClass('error');
	            		}
                		if(M.byId('captcha') != undefined && stringMessage[2]=="captcha" ){
                			M.byId("captcha").style.borderColor=C[5];
                		}
                		apply_currency_form(document.homeloanform);
                		hideMsg();
            	        return false;
                	}else{
                		closeOffStatus=0;
                		if($("#appOTPVerifiedByMobile").val()=='Y'){
                			$("#mobile").prop("disabled","disabled"); 
                			$('#mobile').addClass('disabledFields');
                			$("#disabledMobile").prop("disabled","disabled"); 
                			$('#disabledMobile').addClass('disabledFields');
                			$("#appNRIMobileNo").prop("disabled","disabled"); 
                			$('#appNRIMobileNo').addClass('disabledFields');
                			$("#appNRIMobileNoDisabled").prop("disabled","disabled"); 
                			$('#appNRIMobileNoDisabled').addClass('disabledFields');
                		}
    	            	jQuery("#secondPageContent").html(htmlContent);
    	            	jQuery("#secondPageContent").show();
    	            	if(isDsrPage){
    	            		getCallLogDetails("msg-panel");
    	            		jQuery("a#coAppImage").bind('click',false);
		            		jQuery("a#coAppImageNextData").bind('click',false);
    	            		$("#subtnChange").removeClass("hide");
                			$("#subtn").addClass("hide");
    	            		$("#homeloanform input, #homeloanform select").attr('disabled',true);
    	        			disableById("subtnChange", false);
    	            	}else{
    						jQuery("#firstPageContent").html('');
    						jQuery('#thirdPageContent').html('');
    		            	jQuery('#thirdPageContent').hide();
    		            	jQuery('#fourthPageContent').html('');
    		            	jQuery('#fourthPageContent').hide();
							$("#content-2").mCustomScrollbar({
								//autoHideScrollbar:true,
								theme:"rounded",
								scrollInertia:5
							});
    		            	window.scrollTo(0, 0);
    	            	}
    	            	return false;
                	}
            	}
            	return false;
            },
            error:function(data){
            	apply_currency_form(document.homeloanform);
            	error_msg=SORRY_FOR_INCONVENIENCE;
            	showMsg(error_msg);
            	hideMsg();
    	        return false;
            }
        });
    }	
	return false;
}


function showCrossBtn() {
	  var cross = document.getElementById("cross_btn");
	  var plus = document.getElementById("cross_btn1");
	  if (plus.style.display == "block") {
		  cross.style.display = "block";
		  plus.style.display = "none";
	  } else if (cross.style.display == "block"){
		  plus.style.display = "block";
		  cross.style.display = "none";
	  }
	}


function getDecryptedResponse(htmlContent) {
	var aesUtil = new AesUtil(keySize, iterationCount);
	var dec1 = aesUtil.decrypt(salt, iv, passPhrase, htmlContent);
	
	return dec1;
}
//new code for cibil
function addVlaidationForAllNewFiled(){
	jQuery("#firstName").removeClass('error');
	jQuery("#firstName").next('span').hide();
	if($("#firstName").val()!=undefined || $("#firstName").val()!=""){
			jQuery("#firstName").addClass('error');
			jQuery("#firstName").next('span').show().html('This field is required.');
			$('#firstName').rules('add', {
				required: true
			});
		
	}else{
		jQuery("#firstName").rules("remove","required");
	}
	jQuery("#lastName").removeClass('error');
	jQuery("#lastName").next('span').hide();
	if($("#lastName").val()!=undefined || $("#lastName").val()!=""){
		jQuery("#lastName").addClass('error');
		jQuery("#lastName").next('span').show().html('This field is required.');
		$('#lastName').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#lastName").rules("remove","required");
	}

	jQuery("#address1").removeClass('error');
	jQuery("#address1").next('span').hide();
	if($("#address1").val()!=undefined || $("#address1").val()!=""){
		jQuery("#address1").addClass('error');
		jQuery("#address1").next('span').show().html('This field is required.');
		$('#address1').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#address1").rules("remove","required");
	}
	jQuery("#address2").removeClass('error');
	jQuery("#address2").next('span').hide();
	if($("#address2").val()!=undefined || $("#address2").val()!=""){
		jQuery("#address2").addClass('error');
		jQuery("#address2").next('span').show().html('This field is required.');
		$('#address2').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#address2").rules("remove","required");
	}

	
	
	
	jQuery("#pincode").removeClass('error');
	jQuery("#pincode").next('span').hide();
	if($("#pincode").val()!=undefined || $("#pincode").val()!=""){
		jQuery("#pincode").addClass('error');
		jQuery("#pincode").next('span').show().html('This field is required.');
		$('#pincode').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#pincode").rules("remove","required");
	}
	jQuery("#appPanCardNo1").removeClass('error');
	jQuery("#appPanCardNo1").next('span').hide();
	if($("#appPanCardNo1").val()!=undefined || $("#appPanCardNo1").val()!=""){
		jQuery("#appPanCardNo1").addClass('error');
		jQuery("#appPanCardNo1").next('span').show().html('This field is required.');
		$('#appPanCardNo1').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#appPanCardNo1").rules("remove","required");
	}
	
	
	
	var disabledMobile = document.getElementById("disabledMobile");
	if(disabledMobile != null || disabledMobile != ' '  ){
		
	}else{
	jQuery("#mobile").removeClass('error');
	jQuery("#mobile").next('span').hide();
	if($("#mobile").val()!=undefined || $("#mobile").val()!=""){
		jQuery("#mobile").addClass('error');
		jQuery("#mobile").next('span').show().html('This field is required.');
		$('#mobile').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#mobile").rules("remove","required");
	}
	}
	jQuery("#emailid").removeClass('error');
	jQuery("#emailid").next('span').hide();
	if($("#emailid").val()!=undefined || $("#emailid").val()!=""){
		jQuery("#emailid").addClass('error');
		jQuery("#emailid").next('span').show().html('This field is required.');
		$('#emailid').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#emailid").rules("remove","required");
	}
	
	
	
	jQuery("#requestedAmount").removeClass('error');
	jQuery("#requestedAmount").next('span').hide();
	if($("#requestedAmount").val()!=undefined || $("#requestedAmount").val()!=""){
		jQuery("#requestedAmount").addClass('error');
		jQuery("#requestedAmount").next('span').show().html('This field is required.');
		$('#requestedAmount').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#requestedAmount").rules("remove","required");
	}
	
	jQuery("#mobile").removeClass('error');
	jQuery("#mobile").next('span').hide();
	if($("#mobile").val()!=undefined || $("#mobile").val()!=""){
		jQuery("#mobile").addClass('error');
		jQuery("#mobile").next('span').show().html('This field is required.');
		$('#mobile').rules('add', {
			required: true
		});
		
	}else{
		jQuery("#mobile").rules("remove","required");
	}
	

	
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

function extraValidationFirstPage(){
	if(!stateCityDistrictValidation("#preferredLocation")){
		return false;
	}
	var willPost=1;
	if(jQuery("#employmentType").val()==1){
		var curAge = M.countAgeNew(jQuery("#date_of_birth").val());
		var retAge = jQuery("#retirementAgeApplicant").val();
		if(parseFloat(curAge) > parseInt(retAge)){
			var error_msg="Applicant age can't be greater than retirement age.";
			showMsg(error_msg);
			willPost=0;
			hideMsg();
		}
	}
	if(willPost==1){
		if(jQuery("#co_employmentType1").val()!=undefined && jQuery("#co_employmentType1").val()==1){
			var curAge = M.countAge(jQuery("#co_date_of_birth1").val());
			var retAge = jQuery("#co_retirementAgeApplicant1").val();
			if(parseFloat(curAge) > parseInt(retAge)){
				var error_msg="Co-applicant 1 age can't be greater than retirement age.";
				showMsg(error_msg);
				willPost=0;
				hideMsg();
			}
		}
	}
	if(willPost==1){
		if(jQuery("#homeloanform #co_date_of_birth2").val()!=undefined && jQuery("#homeloanform #co_employmentType2").val()==1){
			var curAge = M.countAge(jQuery("#co_date_of_birth2").val());
			var retAge = jQuery("#co_retirementAgeApplicant2").val();
			if(parseFloat(curAge) > parseInt(retAge)){
				var error_msg="Co-applicant 2 age can't be greater than retirement age.";
				showMsg(error_msg);
				willPost=0;
				hideMsg();
			}
		}
	}
	if(willPost==1){
		var loanPurposeId =parseInt(jQuery('#loanPurpose').val());
		var propertyCategoryId =parseInt(jQuery('#propertyCategory').val());
		if(loanPurposeId == 3 && propertyCategoryId == 7 ){
			var loanYear =parseInt($("#yearemiStartDateOfExistingLoan").val());
			var loanMonth =parseInt($("#monthemiStartDateOfExistingLoan").val());
			var topupYear =parseInt($("#yearemiStartDateOfExistingTopupLoan").val());
			var topupMonth =parseInt($("#monthemiStartDateOfExistingTopupLoan").val());
			if(topupYear >=loanYear){
				if(topupYear ==loanYear && topupMonth<=loanMonth){
					var error_msg="Topup date should be less than loan date";
					showMsg(error_msg);
					willPost=0;
					hideMsg();
				}
			}else{
				var error_msg="Top up date should not be less than loan date";
				showMsg(error_msg);
				willPost=0;
				hideMsg();
			}
		}
	}
	if(willPost==1 && (jQuery("#loanPurpose").val()==3 )){//|| jQuery("#loanPurpose").val()==5
		var mnts="";
		var yrs="";
		if(jQuery("#loanPurpose").val()==3 ){
			yrs = $("#yearemiStartDateOfExistingLoan").val();
			mnts = ($("#monthemiStartDateOfExistingLoan").val()<10?"0"+$("#monthemiStartDateOfExistingLoan").val():$("#monthemiStartDateOfExistingLoan").val());
		}else if(jQuery("#loanPurpose").val()==5 ){
			yrs = $("#yearexistingHomeLoanStartDate").val();
			mnts = ($("#monthexistingHomeLoanStartDate").val()<10?"0"+$("#monthexistingHomeLoanStartDate").val():$("#monthexistingHomeLoanStartDate").val());
		}
		var dobAge = M.countAgeNew(jQuery("#date_of_birth").val());
		var loanAge = M.countAgeNew("01-"+mnts+"-"+yrs);
		var diffAge = parseFloat(dobAge-loanAge);
		if( diffAge<18 ){
			var error_msg="";
			if(jQuery("#loanPurpose").val()==3 ){
				error_msg="Either the Date of Birth or Loan start date of existing Home loan seem incorrect. Please check again.";
				willPost=0;
			}else if(jQuery("#loanPurpose").val()==5 ){
				error_msg="Either the Date of Birth or EMI start date of existing Home loan seem incorrect. Please check again.";
				willPost=0;
			}
			showMsg(error_msg);
			willPost=0;
			hideMsg();
		}
		if(willPost==1 && jQuery("#co_date_of_birth1").val()!=undefined){
			dobAge = M.countAge(jQuery("#co_date_of_birth1").val());
			var diffAge = parseFloat(dobAge-loanAge);
			if( diffAge<18 ){
				var error_msg="";
				if(jQuery("#loanPurpose").val()==3 ){
					error_msg="Either the co-applicant 1 Date of Birth or Loan start date of existing Home loan seem incorrect. Please check again.";
				}else if(jQuery("#loanPurpose").val()==5 ){
					error_msg="Either the co-applicant 1 Date of Birth or EMI start date of existing Home loan seem incorrect. Please check again.";
				}
				showMsg(error_msg);
				willPost=0;
				hideMsg();
			}
		}
		if(willPost==1 && jQuery("#co_date_of_birth2").val()!=undefined){
			dobAge = M.countAge(jQuery("#co_date_of_birth2").val());
			if( dobAge-loanAge<18 ){
				var error_msg="";
				if(jQuery("#loanPurpose").val()==3 ){
					error_msg="Either the co-applicant 2 Date of Birth or Loan start date of existing Home loan seem incorrect. Please check again.";
				}else if(jQuery("#loanPurpose").val()==5 ){
					error_msg="Either the co-applicant 2 Date of Birth or EMI start date of existing Home loan seem incorrect. Please check again.";
				}
				showMsg(error_msg);
				willPost=0;
				hideMsg();
			}
		} 
	}
	if(willPost==0){
		return false;
	}else{
		return true;
	}
}


$(document).ready(function(){
	$("#homeloanform").validate({
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
		errorElement: "span",
		errorPlacement: function(error, element) {
		   if(element.attr("type")=="checkbox"){
				return true;
			}else if(element.attr("name")=="quote.loanQuoteYearCompletionDate" ){
				$("#yearcompletionDate").closest('li').append(error.addClass('left'));
			}
			else if(element.attr("name")=="quote.loanQuoteMonthCompletionDate"){
				$("#monthcompletionDate").closest('li').append(error.addClass('right'));
			}
			else if(element.attr("name")=="quote.loanQuoteYearExistingHomeLoanEndDate" ){
				$("#monthtenorExistingHomeLoan").closest('li').append(error.addClass('left'));
			}
			else if(element.attr("name")=="quote.loanQuoteMonthExistingHomeLoanEndDate"){
				$("#monthtenorExistingHomeLoan").closest('li').append(error.addClass('right'));
			}
			else if(element.attr("name")=="quote.yearTenureOfExistingHomeLoan"){
				$("#yeartenureOfExisingHomeLoan").closest('li').append(error.addClass('left'));
			}
			else if(element.attr("name")=="quote.monthTenureOfExistingHomeLoan"){
				$("#monthtenureOfExisingHomeLoan").closest('li').append(error.addClass('right'));
			}
			else if(element.attr("name")=="quote.loanQuoteYearExistingHomeLoanStartDate"){
				$("#yearemiStartDateOfExistingLoan").closest('li').append(error.addClass('left'));
			}else if(element.attr("name")=="quote.loanQuoteMonthExistingHomeLoanStartDate"){
				$("#monthemiStartDateOfExistingLoan").closest('li').append(error.addClass('right'));
			}else if(element.attr("name")=="quote.loanQuoteEmiStartDateOfExistingTopupLoanYear"){
				$("#monthemiStartDateOfExistingTopupLoan").closest('li').append(error.addClass('left'))
			}else if(element.attr("name")=="quote.loanQuoteEmiStartDateOfExistingTopupLoanMonth"){
				error.insertAfter($("#monthemiStartDateOfExistingTopupLoan").parent().parent()).addClass('right');
			}else if(element.attr("name") == "date_of_birth" || element.attr("name") == "co_date_of_birth2" || element.attr("name") == "co_date_of_birth1"){
				$('<br />').insertAfter(element);error.insertAfter(element);
			}else if(element.attr("type")=="radio"){
				var id = element.attr("id");
				$("#"+id).parent('div').find('input:last').end().append('<br />').append(error);
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
	$(document).on("change","select#propertyCategory",function(){
		change_value = $("select#propertyCategory").val();
	});
	$(document).on("change","select#loanPurpose",function(){
		change_value_loanPurpose = $("select#loanPurpose").val();
	});

	$(document).on("change","select.date_dropdown_year", function(){
		var selected_year = $(this).val();
		if($("#"+this.id+" option:first").is(":selected") || $("#"+this.id+" option:last").is(":selected")){
			if($("#"+this.id+" option:last").is(":selected") 
					&& (this.id=='yearemiStartDateOfExistingLoan' || this.id=='yearemiStartDateOfExistingTopupLoan' || 
						this.id=='yearexistingHomeLoanStartDate' || this.id=='yeartenorExistingHomeLoan') ){
				
			}else{
				$(this).next().attr("disabled",true);
			}
		}else{
			$(this).next().removeAttr("disabled");
		}
		
		var CURRENT_MONTH = new Date().getMonth();
		
		var option_string = "";		
		option_string += '<option value="0">Select month</option>';
	
		if(current_year==selected_year){
			for(var j=0;j<=CURRENT_MONTH;j++){
				option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
			}
		}else{
			for(var j=0;j<months.length;j++){
				option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
			}
		}
		$("#appCompanyJoiningMonth").find("option").remove().end().append(option_string);
		$("#monthemiStartDateOfExistingLoan").find("option").remove().end().append(option_string);
		
	});
	

	$("#subtn").unbind("click").on("click",function(){
		submit_first_page_bind();
		return false;
	});
	
	$("#subtnChange").unbind("click").on("click",function(){
		$("#homeloanform input, #homeloanform select").attr('disabled',false);
		$(".disabledFields").each(function(){
			$(this).attr('disabled','disabled');
		});

		jQuery("a#coAppImage").unbind('click',false);
		jQuery("a#coAppImageNextData").unbind('click',false);
		
		jQuery("#secondPageContent").html('');
		jQuery('#secondPageContent').hide();
		jQuery('#thirdPageContent').html('');
		jQuery('#thirdPageContent').hide();
    	jQuery('#fourthPageContent').html('');
    	jQuery('#fourthPageContent').hide();
		$("#subtnChange").addClass("hide");
		$("#subtn").removeClass("hide");
		if(isRegisteredBuilder){
		}else{
			auto_suggestion('builderName');
		}
		auto_suggestion_employer("employerName");
	});
	
	if(applicationSubTypeId!=2){
		 $("#salaryAccountWithSbiYes").prop("disabled", true);
    	 $('#salaryAccountWithSbiNo').prop("disabled", true);
	}
});
