var ContentHeight=300,ContentHeightSmall=250,TimeToSlide=1050.0,openAccordion='',prevAccordion=null;
var avg_age_year = 30;
var current_date = new Date();
var current_year = current_date.getFullYear();
var current_month = current_date.getMonth();
var current_day = current_date.getDate();

//context path changes
var ctxPath = window.location.pathname;
if (ctxPath != '/personal-banking') {
	ctxPath = ctxPath.substring(0, window.location.pathname.indexOf("/",2));
}

if(min_age_year == undefined){
	if(loanTypeId == 4){
		min_age_year = 12;
	} else {
		min_age_year = 18;
	}
}
if( max_age_year == undefined){
	if(loanTypeId == 2){
		max_age_year = 65;
	} else {
		max_age_year = 70;
	}

}
var max_range = parseInt(current_year-min_age_year);
var min_range = parseInt(current_year-max_age_year);
var avg_range = parseInt(current_year-avg_age_year);

var change_value=0;//storing the onchange values of the select boxes
var change_value_loanPurpose=0;//
var months=["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"];

var elementId=[];
var elementType=[];
var elementValue=[];
var elementMandatory=[];
var elementName=[];
var labelName=[];
var headerLabel=[];

var cityDetails=[];
var districtDetails=[];
var branchDetails=[];
var subDistrictDetails=[];
var villageDetails=[];
var retirementAgeData=[{'key':0, 'value':'Select retirement age'},
                       {'key':45, 'value':45},{'key':46, 'value':46},{'key':47, 'value':47},
                       {'key':48, 'value':48},{'key':49, 'value':49},{'key':50, 'value':50},
                       {'key':51, 'value':51},{'key':52, 'value':52},{'key':53, 'value':53},
                       {'key':54, 'value':54},{'key':55, 'value':55},{'key':56, 'value':56},
                       {'key':57, 'value':57},{'key':58, 'value':58},{'key':59, 'value':59},
                       {'key':60, 'value':60},{'key':61, 'value':61},
                       {'key':62, 'value':62},{'key':63, 'value':63},{'key':64, 'value':64},
                       {'key':65, 'value':65},{'key':66, 'value':66},{'key':67, 'value':67},
                       {'key':68, 'value':68},{'key':69, 'value':69},{'key':70, 'value':70},
                       {'key':71, 'value':71},{'key':72, 'value':72},{'key':73, 'value':73},
                       {'key':74, 'value':74},{'key':75, 'value':75}
					];
var checkOffTypeData=[{'key':0, 'value':'Select check-off'},{'key':1, 'value':'Full check-off'},{'key':2, 'value':'Partial check-off'},{'key':3, 'value':'No check-off'}];
var genderData ='';
var residentData = [{'key':0, 'value':'Select Resident Type'},{'key':1, 'value':'Resident Indian'},{'key':2, 'value':'NRI/PIO Holder'}];
var residentIndianData = [{'key':0, 'value':'Select Resident Type'},{'key':1, 'value':'Resident Indian'}];
//var relationshiptDataHomeLoan = [{'key':1, 'value':'Spouse'},{'key':2, 'value':'Parent'},{'key':3, 'value':'Sibling'},{'key':4, 'value':'Child'},{'key':5, 'value':'Unrelated'}];
var twoWheelerTypeData = [{'key':0, 'value':'Select two wheeler type'},{'key':1, 'value':'Scooter'},{'key':2, 'value':'Bike'},{'key':3, 'value':'Moped'},{'key':4, 'value':'Battery operated'}];
var jsonData;
var dateformatCheckExp =/^\d{2}[\/\-]\d{2}[\/\-]\d{4}$/;
var stringCheckExp =/^[a-zA-Z0-9\s\.\-\,\/\(\)\&]*$/;
var stringCheckExpLength = /^[a-zA-Z0-9\s\.\-\,\/]{3,}$/;
var stringCheckExpForFnameAndMiddleName = /^[a-zA-Z\s]+$/;
var stringCheckExpForLastName = /^[a-zA-Z]+$/;
var stringCheckErrorMessage ="Please enter only [a-z,0-9,.,-,/,(,),&]";
var stringCheckErrorMessageLength = "Please enter at least 3 characters.";


var StringCheckErrorNameMesssage = "Please enter only [a-z]";
var StringCheckErrorLastNameMesssage = "Please enter [a-z] and do not include space";
var toAddress = new Array();
var applicantAddress = new Array();
var employmentAddress = new Array();
var officeAddress = new Array();
var permanentAddress = new Array();
var permanentAddress = new Array();
var coapplicant1Address = new Array();
var coapplicant2Address = new Array();
var coapplicant3Address = new Array();
var enableApplicantAddressByCheckBoxIds = new Array();
var addressLine1 = "";var addressLine_2 = "";var addressLandmark = "";var selectState = 0;var selectCity = 0;var selectBranch = 0;var selectDistrict = 0;var selectPincode = "";
var preserveStateClass="";
var isEligibleToSubmitPickupDate=false;
var isEligibleToSubmitDocs=false;
function disableHiddenElement(formId,elementName){
	$("input[type='hidden']").not('input[name=loanQuoteLoanType]').remove();
}
function removeDivId(currentId){
	if(currentId!=undefined && currentId!=''){
    	jQuery('#'+currentId).remove();
    	
    }
	currentId='';
}

function validateFirst(target,src,Max,Min){
    if(!src)return;
    if(src.options!=undefined){
    	if(src.options[src.selectedIndex].value==0){
    		return false;
    	}
        M.byId(target).value=src.options[src.selectedIndex].value;
    }else if(src.type!=undefined && src.type=='text'){
        if(isNaN(src.value)||src.value>Max) src.value=Max;
        if(Min==undefined)Min=0;
        if(src.value<Min) src.value=Min;

        M.byId(target).value=src.value;
    }
}


function dropOff() {
	if(loanTypeId==1){
		if (typeof dropOffPopupStatus === 'undefined') {
			return true;
		} else if(dropOffPopupStatus == 0) {
			var dropOffUrl = window.location.href = window.location.protocol + "//" + window.location.host +"/" +CONTEXT+ajaxPostUrl+"?requestIndex=24";
			M.popup(dropOffUrl,"",800,650);
		}
	}
}

function removeStyleVisibility(id) {
	jQuery('#'+id).attr('style','visibility:hidden');
}

function showDynamicLoader(buttonId, position){
	var htmlContent = '<span class="globalLoader"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"/></span>';
	if(position!=undefined && position!= null && position == "after"){
		jQuery('#'+buttonId).after(htmlContent);
	}else{
		jQuery('#'+buttonId).before(htmlContent);
	}
	jQuery('#'+buttonId).attr("disabled", "disabled");
}
function showDynamicLoader(buttonId){
	jQuery('#'+buttonId).attr("disabled", "disabled");
	var glbLoader = $('#'+buttonId).parent().find(".globalLoader");
	if(BANK_IMAGE_FOLDER_NEWUI == undefined) {
		BANK_IMAGE_FOLDER_NEWUI = $("#BANK_IMAGE_FOLDER_NEWUI").val();
	}
	if(glbLoader && glbLoader.length == 0  ){
		var htmlContent = '<span class="globalLoader"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"/></span>';
		jQuery('#'+buttonId).before(htmlContent);
	}
}
function removeDynamicLoader(buttonId){
	jQuery('#'+buttonId).removeAttr("disabled");
	jQuery('.globalLoader').remove();
}

function resendShowLoader(buttonId, position){
	var htmlContent = '<span class="resend-Loader"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"/></span>';
	if(position!=undefined && position!= null && position == "after"){
		jQuery('#'+buttonId).after(htmlContent);
	}else{
		jQuery('#'+buttonId).before(htmlContent);
	}
	jQuery('#'+buttonId).attr("disabled", "disabled");
}
function resendShowLoader(buttonId){
	jQuery('#'+buttonId).attr("disabled", "disabled");
	var glbLoader = $('#'+buttonId).parent().find(".resend-Loader");
	if(glbLoader && glbLoader.length == 0  ){
		var htmlContent = '<span class="resend-Loader"><img src="'+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif"/></span>';
		jQuery('#'+buttonId).before(htmlContent);
	}
}
function resendRemoveLoader(buttonId){
	jQuery('#'+buttonId).removeAttr("disabled");
	jQuery('.resend-Loader').remove();
}

function disableById(elementId,flag){
	if(M.byId(elementId)!=undefined){
		if(flag){
			$("#"+elementId).val(0);
			$("#"+elementId).prop("disabled","disabled"); 
		}
		else{
			 $("#"+elementId).removeAttr('disabled');
		}
	}
}

function disableAndRemoveErrorById(formId, elementId,flag){
	if(M.byId(elementId)!=undefined){
		if(flag){
			$("#"+formId+" #"+elementId).prop("disabled",true); 
		}
		else{
			$("#"+formId+" #"+elementId).prop("disabled",false); 
		}
	}
}

function disableByFormAndId(formId, elementId,flag){
	if(M.byId(elementId)!=undefined){
		if(flag){
			$("#"+formId+" #"+elementId).prop("disabled","disabled"); 
		}
		else{
			$("#"+formId+" #"+elementId).removeAttr('disabled');
		}
	}
}
function disableByFormAndName(formId, elementId,flag){
	if(flag){
		jQuery("input[name='"+elementId+"']").prop("disabled","disabled");
	}else{
		jQuery("input[name='"+elementId+"']").removeAttr('disabled');
	}
}
function disableByIdForHiddenEle(elementId,flag){
	if(M.byId(elementId)!=undefined){
		if(flag){
			$("#"+elementId).val(0);
			$("#"+elementId).attr("disabled","disabled"); 
		}
		else{
			 $("#"+elementId).removeAttr('disabled');
		}
	}
}

function ChangeCase(elem){
	 $(this).val(elem.value.toUpperCase());
}

function changeToReadonly(elementId,flag){
	if(M.byId(elementId)!=undefined){
		if(flag){
			$('#'+elementId).attr('readonly', true);
			$('#'+elementId).removeClass('error').next('span').remove();
		}else {
			$('#'+elementId).attr('readonly', false);
			
		}		
	}
}

function populateDropdownFromJSONArrayList(elementId, listAsArray, selectedIndex){
	var selected="";
	var option="";
	jQuery( '#'+elementId ).empty();
	var intitalSelection ="";
	if(elementId == "stateId" || elementId == "state" || elementId =="preferredState" || elementId =="stateOfStudy" || elementId =="residentStateId" || elementId =="workStateId" || elementId =="propertyStateId"){
		intitalSelection = "Select state";
	}else if(elementId == "property_city" || elementId == "residingCity" ||  elementId == "co_residingCity1" || elementId == "residentCityId"  ||  elementId == "workCityId" || elementId =="propertyCityId"){
		intitalSelection = "Select city";
	}else if(elementId == "localitiy"){
		intitalSelection = "Select locality";
	}else if(elementId == "branchId" || elementId == "workBranchId" || elementId == "propertyBranchId" || elementId=="preferredBranch"|| elementId=="appBranchId" || elementId == "appEdvantageBranchId"){
		intitalSelection = "Select branch";
	}else if(elementId == "loanPurpose"){
		intitalSelection = "Select loan purpose";
	}else if(elementId == "propertyCategory"){
		intitalSelection = "Select property category";
	}else if(elementId == "loanCategory"){
		intitalSelection = "Select loan category";
	}else if(elementId == "dealerName"){
		intitalSelection = "Select dealer name";
	}else if(elementId == "propertyType"){
		intitalSelection = "Select property type";
	}else if(elementId == "countryRegion" || elementId == "co_countryRegion1" ||elementId == "co_countryRegion2" || elementId == "residentCountryId" || elementId =="countryOfStudy"){
		intitalSelection = "Select country";
	}else if(elementId == "employmentType" ||elementId =="coEmploymentType" ||elementId =="coapplicantEmploymentTypeId" 
		||elementId =="co_employmentType1" || elementId =="co_employmentType2" ){
		intitalSelection = "Select employment type";
	}else if(elementId=="loanWithBankId" || elementId=="BankWhichHasExistingEducationLoan"){
		intitalSelection = "Select bank";
	}else if(elementId=="makeCar" || elementId=="makeBike"){
		intitalSelection = "Select make";
	}else if(elementId=="instituteName" || elementId=="instituteId"){
		intitalSelection = "Select institute";
	}else if(elementId=="graduation"){
		intitalSelection = "Select course";
	}else if(elementId=="certificateId"){
		intitalSelection = "Select certificate";
	}else if(elementId=="coapplicantFirstRelationshipId" || elementId=="co_applicant_relationship1" || elementId=="co_applicant_relationship2"){
		intitalSelection = "Select relationship";
	}else if(elementId=="industryType"){
		intitalSelection = "Select organisation type";
	}else if(elementId=="gender"){
		intitalSelection = "Select gender";
	}else if(elementId=="courseType"){
		intitalSelection = "Select course type";
	}else if(elementId=="professionId" || elementId=="CoapplicantProfessionId"){
		intitalSelection = "Select profession";
	}else if(elementId=="salaryPackage" || elementId=="loanQuoteSalaryPackage"){
		intitalSelection = "Select salary package";
	}else if(elementId=="appPermanentDistrictId" || elementId=="preferredDistrict" || elementId=="districtId" || elementId=="appDistrictId" 
		|| elementId=="workDistrictId" || elementId=="appCoapplicant1DistrictId"  || elementId=="appCoapplicant2DistrictId"||elementId =="appCoapplicantDistrictId" ){
		intitalSelection = "Select district";
	}else if(elementId=="projectId"){
		intitalSelection = "Select project";
	}else if(elementId == "coapplicantIndustryType"){
		intitalSelection = "Select organisation type";
	}else if(elementId=="loanQuoteOrgName"){
		intitalSelection = "Select organisation name";
	}else if(elementId == 'pensionPayingBranchId'){
		intitalSelection = "Select branch";
	}else if(elementId == 'preferredBranchId'){
		intitalSelection = "Select branch";
	}
	
	
	if(intitalSelection!=undefined && intitalSelection!=""){
		disableById(elementId, true);
		option='<option value="0">'+intitalSelection+'</option>';
		jQuery('#'+elementId).append(option);
	}
	if(listAsArray!=undefined && listAsArray!=''){
		disableById(elementId, false);
		jQuery('#'+elementId).removeAttr("disabled");
		$.each( listAsArray, function( index, dropdownValue ) {
			option = '<option value="'+dropdownValue.key+'" '+selected+' >'+dropdownValue.value+'</option>';
			jQuery('#'+elementId).append(option);
		});
	}else{
		disableById(elementId, true);
	}
}
function populateBanks(elementId, banks, selectedBank){
	jQuery( '#'+elementId ).empty();
	if(banks.length>=1){
		var option = "";
		option  = '<option value="0">Select bank</option>';
		jQuery('#'+elementId).append(option);
		var selected="";
		for(var index=0; index<banks.length;index++){
			var bank = banks[index];
			if( bank.displayOrder == 2 ) {
				option = '<option value="'+bank.key+'" '+selected+' >'+bank.value+'</option>';
				jQuery('#'+elementId).append(option);
			}
		}
		option = '<optgroup label="--------------">';
		jQuery('#'+elementId).append(option);
		for(var index=0; index<banks.length;index++){
			var bank = banks[index];
			if( bank.displayOrder == 1){
				option = '<option value="'+bank.key+'" '+selected+' >'+bank.value+'</option>';
				jQuery('#'+elementId).append(option);
			}
		}
	}else{
		option = '<option value="0" >No Records</option>';
		jQuery('#'+elementId).append(option);
		jQuery('#'+elementId).attr("disabled", true);
	}
}
/**
 * close mobile OTP
 */
function closeMobileOTPPopup(){
	if(M.byId('pageOverlay2') !=undefined){
		M.byId('pageOverlay2').innerHTML="";
		M.byId('pageOverlay2').style.display='none';
	}
	if(M.byId('mobileOTP2') !=undefined){
		M.byId('mobileOTP2').style.display='none';
	}
}

function show_hide_toggle(target_ele, bool){
	if(bool){
		$(target_ele).attr('style','display:block;');
	}else{
		$(target_ele).attr('style','display:none;');
	}
}

function show_hide_toggle_first(target_ele, bool){
	
	if(M.byId(target_ele)!=undefined){
		if(bool){
			$("#"+target_ele).attr('style','display:block;');
		}else{
			$("#"+target_ele).attr('style','display:none;');
		}	
	}
}
function fetch_validation_object(requestIndex,loanType){
	var datastring = 'requestIndex='+requestIndex+'&loanTypeId='+loanType;
	$.ajax({
		type: "POST",
 		url: ctxPath+"/getRequestConfigByLoanType",
 		data: datastring,
 		//headers : {"Accept-Encoding" : "gzip"},
 		beforeSend:function(){
			//do something
		},
		complete:function(){
			//do something
		},
 		success:function(response){
 			jsonObject = response;// set the jsonobject to new json as per request index
 		},
 		async: false
	});
	return jsonObject;
}

/**
 * Add validation rules based on json validation object
 * @param form object(javascript) e.g document.formname
 */

function removeElement(formData){
	if(formData!=null && formData!=undefined){
		for(var i=0; i<formData.elements.length; i++){
			//var fieldName = formData.elements[i].name;
			var fieldId = formData.elements[i].id;
			if(fieldId!=null && fieldId != undefined && fieldId!=''){
				if(M.byId(fieldId)!=undefined && (M.byId(fieldId).value==0 ||M.byId(fieldId).value=='' ||M.byId(fieldId).value==null)){
					disableById(fieldId, true);
				}
			}
		}
	}
}

function remove_comma(formData){
	if(formData!=null && formData!=undefined){
		if(formData.elements!=null && formData.elements!=undefined){
			for(var i=0; i<formData.elements.length; i++){
				//var fieldName = formData.elements[i].name;
				var fieldId = formData.elements[i].id;
				//var fieldType = formData.elements[i].type;
				if(fieldId!=null && fieldId != undefined && fieldId!=''){
					var placeholder = $("#"+fieldId).attr('placeholder');
					if(placeholder!=undefined && placeholder=="Rs"){
						var strip_commas = $("#"+fieldId).val().replace(/,/g, "");
						$("#"+fieldId).val(strip_commas);
					}
				}
			}
		}
	}
	
}

function apply_currency_form(formData){
	if(formData!=null && formData!=undefined){
		if(formData.elements!=null && formData.elements!=undefined){
			for(var i=0; i<formData.elements.length; i++){
				var fieldId = formData.elements[i].id;
				if(fieldId!=undefined && fieldId!=null && fieldId!=''){
					var placeholder = $("#"+fieldId).attr('placeholder');
					if(placeholder!=undefined && placeholder=="Rs"){
						var strip_commas = $("#"+fieldId).val().replace(/,/g, "");
						strip_commas = Number(strip_commas).toPrecision();
						strip_commas = strip_commas.replace(".0","");
						$("#"+fieldId).val(M.moneyFormat(strip_commas));
					}
				}
					
			}
		}
	}
	
}
function apply_currency_form_for_label(){
	if(formData!=null && formData!=undefined){
		if(formData.elements!=null && formData.elements!=undefined){
			for(var i=0; i<formData.elements.length; i++){
				var fieldId = formData.elements[i].id;
				if(fieldId!=undefined && fieldId!=null && fieldId!=''){
					var placeholder = $("#"+fieldId).attr('placeholder');
					if(placeholder!=undefined && placeholder=="Rs"){
						var strip_commas = $("#"+fieldId).val().replace(/,/g, "");
						strip_commas = Number(strip_commas).toPrecision();
						strip_commas = strip_commas.replace(".0","");
						$("#"+fieldId).val(M.moneyFormat(strip_commas));
					}
				}
			}
		}
	}
}
function addValidationsRules(formData,useObj){
	
	$('#errorCallback').remove();
	
	if(useObj==undefined){
		jsonObject = fetchJsonData();//initialize json data
	}else{
		jsonJSArray = useObj;
		jsonObject = $.parseJSON(jsonJSArray);
	}
	if(jsonObject!=null && jsonObject!=undefined){
		if($.isEmptyObject(jsonObject)){
			alert("Sorry for the inconvenience, Please refresh");
		}

		for(var j=0; j<jsonObject.length; j++){
			for(var i=0; i<formData.elements.length; i++){
					var fieldName = formData.elements[i].name;
					var fieldId = formData.elements[i].id;
					var fieldType = formData.elements[i].type;
				if (fieldName == 'quote.loanQuoteAppFirstName') {
					
					$('#' + fieldId).rules('add', {
						nameCheck: true
					});
				}
				if (fieldName == 'quote.loanQuoteFirstName') {
				
					$('#' + fieldId).rules('add', {
						nameCheck: true
					});
				}
				if (fieldName == 'quote.loanQuoteMiddleName') {
				
					if ($('#middleName').val() != '') {
						$('#' + fieldId).rules('add', {
							nameCheck: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							nameCheck: false
						});
					}
				}
				if (fieldName == 'quote.loanQuoteLastName') {
				
					$('#' + fieldId).rules('add', {
						lastNameCheck: true
					});
				}
				if (fieldName == 'appForm.appFirstName') {
					$('#' + fieldId).rules('add', {
						nameCheck: true
					});
				}
				if (fieldName == 'appForm.appMiddleName') {
					if ($('#appMiddleName').val() != '') {
						$('#' + fieldId).rules('add', {
							nameCheck: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							nameCheck: false
						});
					}
				}
				if (fieldName == 'appForm.appLastName') {
					$('#' + fieldId).rules('add', {
						lastNameCheck: true
					});
				}
				if (fieldName == 'quote.loanQuoteAddress1') {
					if (($('#address1').val() != '') && ($('#address1').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
				if (fieldName == 'quote.loanQuoteAddress2') {
					if (($('#address2').val() != '') && ($('#address2').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
				if (fieldName == 'quote.loanQuoteAddressLandmark') {
					$('#' + fieldId).rules('add', {
						stringCheck: true
					});
				}
				if(fieldName=='appForm.appAddress1'){
					if (($('#appAddress1').val() != '') && ($('#appAddress1').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
				if(fieldName=='appForm.appAddress2'){
					if (($('#appAddress2').val() != '') && ($('#appAddress2').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
				if(fieldName=='appForm.appPermanentAddress1'){
					if (($('#appPermanentAddress1').val() != '') && ($('#appPermanentAddress1').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
				if(fieldName=='appForm.appPermanentAddress2'){
					if (($('#appPermanentAddress2').val() != '') && ($('#appPermanentAddress2').val().length < 3)) {
						$('#' + fieldId).rules('add', {
							addressLength: true
						});
					} else {
						$('#' + fieldId).rules('add', {
							stringCheck: true
						});
					}
				}
					if(fieldName==jsonObject[j].validation_name){
						/**
						 * found the form element in the json validation object
						 */
						if(jsonObject[j].mandatory && fieldType=='select-one'){ //if its required
							if(fieldName=='appForm.appWorkExperienceMonth' ||fieldName=='appForm.appWorkExprerienceYear' || fieldName=='appForm.appWorkExprerienceMonth' || fieldName=='appForm.appCompanyJoiningMonth'){
								$('#'+fieldId).rules('add', {
							        required: true,
							        valueNotEquals: 13
							    });
							}else if(fieldName=="appForm.appPhotoId" || fieldName=="appForm.appIdentityProofId" || fieldName=="appForm.appResidenceProofId" || fieldName=="appForm.appIncomeProofId" || fieldName=="appForm.appEmploymentProofId" || fieldName=="appForm.appAutoDetailsProofId" || fieldName=="appForm.appPropertyProofId" || fieldName=="appForm.appAcademicProofId"){
								//do nothing
							}else if(fieldName=="quote.loanQuoteCourseDurationYears" || fieldName=="quote.loanQuoteCourseDurationMonth" || fieldName=="quote.loanQuoteCourseDurationMonth" ||  fieldName=="cbs.cbsTypeOfRelationship" || fieldName=="cbs.cbsNoOfExistingXpressCredit"){
								$('#'+fieldId).rules('add', {
							        required: true,
							        valueNotEquals: -1
							    });
							}
							else{
								$('#'+fieldId).rules('add', {
							        required: true,
							        valueNotEquals: 0
							    });
							}
						}
						else if(jsonObject[j].data_type_id=="8" 
							&& jsonObject[j].mandatory == 1
							&& jsonObject[j].conditional_mandatory == 0 ){ //for datatype email mandatory case
							$('#'+fieldId).rules('add', {
								email: true
						    });
						}
						else if(jsonObject[j].data_type_id=="8"
							&& jsonObject[j].mandatory == 0
							&& jsonObject[j].conditional_mandatory == 1 ){ //for datatype email non mandatory case
							$('#'+fieldId).rules('add', {
								emailConditional: true
						    });
						}
						else if(jsonObject[j].mandatory && fieldType=='text'){ //if its required
							if(fieldName=='quote.loanQuoteCostOfConstruction'){
								$('#'+fieldId).rules('add', {
									customConstructionCost: true
							    });
							}else if(fieldName=='quote.loanQuotePreEMIs' || fieldName=='quote.loanQuoteCoapplicantFirstPreEMIs'  || fieldName=='quote.loanQuoteCoapplicantSecondPreEMIs'){
								$('#'+fieldId).rules('add', {
									customEMI: true
								});
							}else{
								$('#'+fieldId).rules('add', {
							        required: true
							    });
							}
						}
						else if(jsonObject[j].mandatory && fieldType=='checkbox'){ //if its required
							$('#'+fieldId).rules('add', {
						        required: true
						    });
						}
						if((jsonObject[j].data_type_id=="1" || jsonObject[j].data_type_id=="3") && fieldType=='text'){ //incase of text field and integer datatype
							if(loanTypeId==15){
								$('#'+fieldId).rules('add', {
									maxlength: '14',
									number:true,
									min: jsonObject[j].min_value,
									max: jsonObject[j].max_value
								});
							}else{
								$('#'+fieldId).rules('add', {
									maxlength: '14',
									number:true
								});
							}
							$('#'+fieldId).keydown(function(event){
								return M.digit(event);
							});
						}
						if((jsonObject[j].data_type_id=="11") && fieldType=='text'){ //incase of text field and integer datatype
							$('#'+fieldId).rules('add', {
								maxlength: '10'
						    });
						}
						if((jsonObject[j].data_type_id=="2") && fieldType=='text'){ //incase of text field and integer datatype
							$('#'+fieldId).rules('add', {
								minlength: jsonObject[j].min_length,
								maxlength: jsonObject[j].max_length,
								stringCheck:true
						    });
						}
						if(jsonObject[j].validation_name=="quote.loanQuoteEmployerName"){ //autocomplete case for checks for space char, num and -
							$('#'+fieldId).keydown(function(event){
								return M.isString(event);
							});
						}
						if(jsonObject[j].mandatory && jsonObject[j].data_type_id=="7"){ //for datatype date
							if(fieldName=='quote.loanQuoteDateOfBirth'){
								$('#'+fieldId).rules('add', {
									dateformatCheck: true,
									ageCheck: true
							    });
							}else if(fieldName=='appForm.appDob' || fieldName=='appForm.appCoAppDob1' || fieldName=='appForm.appCoAppDob2' || fieldName=='appForm.appCoAppDob3' || fieldName=='appForm.appCoAppDob4'){
								$('#'+fieldId).rules('add', {
									dateformatCheck: true,
									ageCheck: true
							    });
							}else{
								$('#'+fieldId).rules('add', {
									dateformatCheck: true
								});
							}
							
						}
						//rule for mobile_check start -------
						if(jsonObject[j].data_type_id=="4" && (fieldName=='mobile' || fieldName=='trackMobile' || fieldName=='cbs.cbsMobileNumber' || fieldName=='mobileWantUsToCallYou' || fieldId=="cbsMobileNumber1" )){ //for datatype date
							$('#'+fieldId).rules('add', {
								mobile: true
						    });
						}
						//rule for mobile_check end
						//rule for pincode start 
						if(jsonObject[j].data_type_id=="12" && fieldType=='text'){ //for datatype date 
							if(jsonObject[j].mandatory == 1){
								$('#'+fieldId).rules('add', {
									required: true,
									min: jsonObject[j].min_value,
									max: jsonObject[j].max_value,
									minlength : 6,
									maxlength : 6,
									number:true
								});
							} else {
								$('#'+fieldId).rules('add', {
									required: false,
									min: jsonObject[j].min_value,
									max: jsonObject[j].max_value,
									minlength : 6,
									maxlength : 6,
									number:true
								});
							}
						}
						//rule for pincode end
						//rule for work exp year start 
						if(jsonObject[j].data_type_id=="14" && fieldType=='text'){ //for datatype date 
							$('#'+fieldId).rules('add', {
								required: true,
								min: jsonObject[j].min_value,
								max: jsonObject[j].max_value
						    });
						}
						//rule for work exp year end
						
						//rule for aadhaar number start 
						if(jsonObject[j].data_type_id=="15" && fieldType=='text'){ 
							$('#'+fieldId).rules('add', {
								aadhaarNumber: true
						    });
						}
						//rule for aadhaar number end
						if(fieldName=='cbs.cbsAccountNumber'){
							$('#'+fieldId).rules('add', {
								extraValidateAccountNo: true
						    });
						}
						
						//rule for Pan Card no start
						if(jsonObject[j].data_type_id=="11" && fieldType=='text'){
							if(jsonObject[j].loan_type_id!="15" && jsonObject[j].loan_type_id!="17"){
								$('#'+fieldId).rules('add', {
									isPanCard: true
							    });
							}
						}
						//rule for Pan Card no end
					continue;
				}
			}
		}
		for(var i=0; i<formData.elements.length; i++){
			
				var fieldName = formData.elements[i].name;
				var fieldId = formData.elements[i].id;
				var fieldType = formData.elements[i].type;
				
				var val = jQuery('#workStateId').val();		
				
				var infoprovideCBS = jQuery('#infoprovideCBS').val();
				console.log("infoprovideCBS::"+infoprovideCBS);
				
				var val2 = jQuery('#infoprovideCBSRevoke').val();
				var val3 = jQuery('#cbsMobileNumber').val();
				
				if(fieldName=="cbsTypeOfRelationship" || fieldName=="cbs.cveProductCategory" || fieldName=="cbs.cveSalutation" ||  fieldName=="quote.loanQuoteWorkStateId" || fieldName=="quote.loanQuoteWorkCityId" || fieldName=="quote.loanQuoteWorkBranchId" || fieldName=="loanQuoteWorkBranchId" || fieldId=="cbsTypeOfRelationshipCveRevoke" || fieldName=="cbsMobileNumber" || fieldName=="infoprovideCBSRevoke" || fieldName=="infoprovideCBScve"){	
					$('#'+fieldId).rules('add', {
						required: true,
					    valueNotEquals: 0
					});	
					jQuery('#'+fieldId).addClass('error');
				}			
		}					
		
 
		
		
		 if ($('#cbsTypeOfRelationshipCveRevoke').length)
		{	
		var cbsTypeOfRelationshipCveRevoke = $("#cbsTypeOfRelationshipCveRevoke option:selected").val();
		if (cbsTypeOfRelationshipCveRevoke=="0") {
			$('#cbsTypeOfRelationshipCveRevoke').rules('add', {
						required: true,
					    valueNotEquals: 0
					});
			jQuery('#cbsTypeOfRelationshipCveRevoke').addClass('error');
			
		   }
		}

		if ($('#cbsMobileNumber1').length > 0) {
		var cbsMobileNumberRevoke = $('#cbsMobileNumber1').val();
		var spanErrMsg = '<span id="requiredSpan" style="color: #e96e57;font-size: 11px;display: block!important;">Please enter valid mobile no.</span>';

		if ($('#requiredSpan').length >0) {
			$('span[id^="requiredSpan"]').remove();
		}

		if (cbsMobileNumberRevoke=="") {
			$('#cbsMobileNumber1').rules('add', {
						required: true,
					    valueNotEquals: 0
			});
		    $('#cbsMobileNumber1').after(spanErrMsg);
			
		} else  if (cbsMobileNumberRevoke.length < 10) {
			$('#cbsMobileNumber1').rules('add', {
						required: true,
					    valueNotEquals: 0
			});
		    $('#cbsMobileNumber1').after(spanErrMsg);
		} else  if (cbsMobileNumberRevoke.charAt(0) < 6) {
					$('#cbsMobileNumber1').rules('add', {
						required: true,
					    valueNotEquals: 0
			});
		    $('#cbsMobileNumber1').after(spanErrMsg);
		}
	}

		 var consentRevocation1 = jQuery('#consentRevocation1').val();		
		if (consentRevocation1=="N") {
			if (!$("#infoprovideCBScve").is(":checked")) {
				jQuery('#infoprovideCBScve').addClass('error');
				$('#infoprovideCBScve').css({ 'outline':'solid','outline-width': '2px', 'outline-color': '#e96e57'});			
				var consent_error_msg="";
				consent_error_msg="Please provide consent to authorize SBI representatives to contact via calls, mails and/or text messages to discuss to product offerings.";
				alert(consent_error_msg);
				return false;
			} else {
				console.log("IN ELSE");
			} 
		}

		var cbsIsdCodeCve='';
		 $('#cbsIsdCodeCve').on("blur", function(){
			cbsIsdCodeCve = $('#cbsIsdCodeCve').val();
		 });
				 
		var cbsIsdCode = $('#cbsIsdCode').val();
		
		if (cbsIsdCodeCve=="91"){
			$("#cbsIsdCodeCve").val($("#cbsIsdCodeCve").val());
		} else {
			$("input[name='cbsIsdCode']").val(cbsIsdCodeCve);
		}
	}
}

function formReset(formId){
	$('#'+formId).find('.error').each(function(){
		$(this).removeClass('error');
	});
}

function auto_suggestion_employer(elementId) {
	jQuery("#"+ elementId).typeahead('destroy');
	jQuery("#"+ elementId).typeahead({
		source : function(query, process) {
			$.ajax({
				type:'GET',
				url: 'getAllEmployer',
				data: 'query=' + query,
				beforeSend:function(){
					$('#autofillLoader').addClass('autofill-loader');
				},
				complete:function(){
					$('#autofillLoader').removeClass('autofill-loader');
				},
				success:function(data){
					return process($.parseJSON(data).suggestions);
				},
				error:function(data){
					return false;
				}
			});
		},
		updater : function(item) {
			$('#autofillLoader').removeClass('autofill-loader');
			return item;
		}
	});
}

function auto_suggestion_branch_name(elementId) {
	jQuery("#"+ elementId).typeahead('destroy');
	jQuery("#"+ elementId).typeahead({
		source : function(query, process) {
			$.ajax({
				type:'GET',
				url: 'getAllBranchName',
				data: 'query=' + query,
				beforeSend:function(){
					$('#autofillLoader').addClass('autofill-loader');
				},
				complete:function(){
					$('#autofillLoader').removeClass('autofill-loader');
				},
				success:function(data){
					return process($.parseJSON(data).suggestions);
				},
				error:function(data){
					return false;
				}
			});
		},
		updater : function(item) {
			$('#autofillLoader').removeClass('autofill-loader');
			return item;
		}
	});
}

function auto_suggestion(elementId){
	jQuery("#"+ elementId).typeahead('destroy');
	jQuery("#"+ elementId).typeahead({
		source : function(query, process) {
			$.ajax({
				type:'GET',
				url: 'getAllBuilder',
				data: 'query=' + query,
				beforeSend:function(){
					$('#autofillLoaderProject').addClass('autofill-loader');
				},
				complete:function(){
					$('#autofillLoaderProject').removeClass('autofill-loader');
				},
				success:function(data){
					return process($.parseJSON(data).suggestions);
				},
				error:function(data){
					return false;
				}
			});
		},
		updater : function(item) {
			$('#autofillLoaderProject').removeClass('autofill-loader');
			return item;
		}
	});
}


function auto_suggestion_courseName(elementId){
	jQuery("#"+ elementId).typeahead('destroy');
	jQuery("#"+ elementId).typeahead({
		source : function(query, process) {
			$.ajax({
				type:'GET',
				url: 'getAllCourseName?courseType='+jQuery("#courseType").val(),
				data: 'query=' + query,
				beforeSend:function(){
					$('#autofillLoaderProjectCour').addClass('autofill-loader');
				},
				complete:function(){
					$('#autofillLoaderProjectCour').removeClass('autofill-loader');
				},
				success:function(data){
					return process($.parseJSON(data).suggestions);
				},
				error:function(data){
					return false;
				}
			});
		},
		updater : function(item) {
			$('#autofillLoaderProjectCour').removeClass('autofill-loader');
			return item;
		}
	});
}

function auto_suggestion_university(elementId){
	jQuery("#"+ elementId).typeahead('destroy');
	jQuery("#"+ elementId).typeahead({
		source : function(query, process) {
			$.ajax({
				type:'GET',
				url: 'getAllUniversity?isInIndia='+jQuery("#CountryofStudyId").val(),
				data: 'query=' + query,
				beforeSend:function(){
					$('#autofillLoaderProjectuni').addClass('autofill-loader');
				},
				complete:function(){
					$('#autofillLoaderProjectuni').removeClass('autofill-loader');
				},
				success:function(data){
					return process($.parseJSON(data).suggestions);
				},
				error:function(data){
					return false;
				}
			});
		},
		updater : function(item) {
			$('#autofillLoaderProjectuni').removeClass('autofill-loader');
			return item;
		},
		items : 'all',
		scrollHeight:'10px'
	});
}

function goToByScroll(id){
	if(id!=undefined){
		$('html,body').animate({
	    	scrollTop: $("#"+id).offset().top
	    },'slow');	
	}
	$('#msg-panel').delay(5000).fadeOut('slow');
}

function showMsg(message, isSuccess){
	if(isSuccess!=null && isSuccess!= undefined && isSuccess){
		$('#msg-panel').attr('class','msg-war corner-all success-msg-global')
	}else{
		$('#msg-panel').attr('class','msg-war corner-all')
	}
	$('#msg-panel').html(message).show();    	
	$('html,body').animate({scrollTop: $('#msg-panel').offset().top},'slow');
	$('#msg-panel').delay(5000).fadeOut('slow');
}

function showMsg(message, isSuccess, fieldId){
	if(isSuccess!=null && isSuccess!= undefined && isSuccess){
		$('#msg-panel').attr('class','msg-war corner-all success-msg-global')
	}else{
		$('#msg-panel').attr('class','msg-war corner-all')
		jQuery('#'+fieldId).addClass('error');
	}
	$('#msg-panel').html(message).show();    	
	$('html,body').animate({scrollTop: $('#msg-panel').offset().top},'slow');
	$('#msg-panel').delay(5000).fadeOut('slow');
}

function hideMsg(){}
function showLoader(){$('#page-loader').show();}
function hideLoader(){$('#page-loader').hide();}
function showPOPMsg(emementId, message, isSuccess){
	if(isSuccess!=null && isSuccess!= undefined && isSuccess){
		$('#'+emementId).attr('class','error-msg-cbs success-msg-cbs')
	}else{
		$('#'+emementId).attr('class','error-msg-cbs')
	}
	$('#'+emementId).html(message).show();
}

function hidePOPMsg(emementId){
	$('#'+emementId).attr('class','error-msg-cbs')
	$('#'+emementId).html('').hide();
}

function changeLanguage(selectedLanguage){
	var selectedLanguage = jQuery('#selectedLanguage').val();
	if(selectedLanguage==null || selectedLanguage == undefined || selectedLanguage==""){
		return false;
	}
	jQuery.ajax({
	    type: "POST",
		url: "changelanguage",
		data: "selectedLanguage="+selectedLanguage,
		dataType: "json",
		//headers : {"Accept-Encoding" : "gzip"},
	    success:function(json){
	    	window.location.reload(true);
	    },
	    error:function(jqXHR, textStatus){
	       
	    }
	});
}


function killJustPopup(frm) {
    M.byId('itemOverlay').innerHTML="";
	M.byId('itemOverlay').style.display='none';
    	
    M.byId('pageOverlay').style.display='none';
}


function refreshCaptcha(id){
	if(PORT == undefined) {
		PORT = $("#PORT").val();
	}
	if(CONTEXT == undefined) {
		CONTEXT = $("#CONTEXT").val();
	}
	if(id!=undefined && id!=null && id!='' && document.images[id] != undefined && document.images[id]!=null && document.images[id]!=''){
		document.images[id].src=PORT+CONTEXT+'Captcha.jpg?catch='+new Date().getTime();	
	}
}

function loanQuoteOffer(){
	isOfferPage=true;
	hideMsg();
	$("#firstPageContent").hide();
	$("#secondPageContent").show();
	$("#thirdPageContent").hide();
	
	$("#sendQuote").removeClass('send-btn-failure');
	$("#sendQuote").removeClass('send-btn-success');
	$("#sendQuote").addClass('send-btn');
	$("#loaderEmail").html('');
}

function check_uploaded__doc_vals(){
	isValidationFail=false;
	var doc_1_upload_file = $("#docUrl_1").val();
	var appPhotoId_val = $("#appPhotoId").val();
	if(doc_1_upload_file!="" && appPhotoId_val==0 && appPhotoId_val!=undefined ){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appPhotoId").rules('add', {
	        required: true,
	        valueNotEquals: 0
	    });
		customOnFocusElement('appPhotoId');
	}else if(doc_1_upload_file=="" && appPhotoId_val==0 && appPhotoId_val!=undefined){
		$("#appPhotoId").rules('remove');
	}else if(doc_1_upload_file=="" && appPhotoId_val!=0 && appPhotoId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appPhotoId").rules('add', {
			uploadCheck:"docUrl_1"
	    });
		customOnFocusElement('appPhotoId');
	}
	
	var doc_2_upload_file = $("#docUrl_2").val();
	var appIdentityProofId_val = $("#appIdentityProofId").val();
	if(doc_2_upload_file!="" && appIdentityProofId_val==0 && appIdentityProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appIdentityProofId").rules('add', {
	        required: true,
	        valueNotEquals: 0
	    });
		customOnFocusElement('appIdentityProofId');
	}else if(doc_2_upload_file=="" && appIdentityProofId_val==0 && appIdentityProofId_val!=undefined){
		$("#appIdentityProofId").rules('remove');
	}else if(doc_2_upload_file=="" && appIdentityProofId_val!=0 && appIdentityProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appIdentityProofId").rules('add', {
			uploadCheck:"docUrl_2"
	    });
		customOnFocusElement('appIdentityProofId');
	}
	
	var doc_3_upload_file = $("#docUrl_3").val();
	var appResidenceProofId_val = $("#appResidenceProofId").val();
	if(doc_3_upload_file!="" && appResidenceProofId_val==0 && appResidenceProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appResidenceProofId").rules('add', {
	        required: true,
	        valueNotEquals: 0
	    });
		customOnFocusElement('appResidenceProofId');
	}else if(doc_3_upload_file=="" && appResidenceProofId_val==0 && appResidenceProofId_val!=undefined){
		$("#appResidenceProofId").rules('remove');
	}else if(doc_3_upload_file=="" && appResidenceProofId_val!=0 && appResidenceProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appResidenceProofId").rules('add', {
			uploadCheck:"docUrl_3"
	    });
		customOnFocusElement('appResidenceProofId');
	}

	var doc_4_upload_file = $("#docUrl_4").val();
	var appIncomeProofId_val = $("#appIncomeProofId").val();
	if(doc_4_upload_file!="" && appIncomeProofId_val==0 && appIncomeProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appIncomeProofId").rules('add', {
	        required: true,
	        valueNotEquals: 0
	    });
		customOnFocusElement('appIncomeProofId');
	}else if(doc_4_upload_file=="" && appIncomeProofId_val==0 && appIncomeProofId_val!=undefined){
		$("#appIncomeProofId").rules('remove');
	}else if(doc_4_upload_file=="" && appIncomeProofId_val!=0 && appIncomeProofId_val!=undefined){
		if (!isValidationFail) {isValidationFail=true;}
		$("#appIncomeProofId").rules('add', {
			uploadCheck:"docUrl_4"
	    });
		customOnFocusElement('appIncomeProofId');
	}
	if(loanTypeId!=undefined){
		if(loanTypeId==1){
			var doc_5_upload_file = $("#docUrl_5").val();
			var appEmploymentProofId_val = $("#appEmploymentProofId").val();
			if(doc_5_upload_file!="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appEmploymentProofId").rules('add', {
			        required: true,
			        valueNotEquals: 0
			    });
				customOnFocusElement('appEmploymentProofId');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				$("#appEmploymentProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val!=0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appEmploymentProofId").rules('add', {
					uploadCheck:"docUrl_5"
			    });
				customOnFocusElement('appEmploymentProofId');
			}
		}else if(loanTypeId==2){
			var doc_5_upload_file = $("#docUrl_5").val();
			var appEmploymentProofId_val = $("#appAutoDetailsProofId").val();
			if(doc_5_upload_file!="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appAutoDetailsProofId").rules('add', {
			        required: true,
			        valueNotEquals: 0
			    });
				customOnFocusElement('appAutoDetailsProofId');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				$("#appAutoDetailsProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val!=0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appAutoDetailsProofId").rules('add', {
					uploadCheck:"docUrl_5"
			    });
				customOnFocusElement('appAutoDetailsProofId');
			}
		}else if(loanTypeId==3){
			var doc_5_upload_file = $("#docUrl_5").val();
			var appEmploymentProofId_val = $("#appPropertyProofId").val();
			if(doc_5_upload_file!="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appPropertyProofId").rules('add', {
			        required: true,
			        valueNotEquals: 0
			    });
				customOnFocusElement('appPropertyProofId');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				$("#appPropertyProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val!=0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appPropertyProofId").rules('add', {
					uploadCheck:"docUrl_5"
			    });
				customOnFocusElement('appPropertyProofId');
			}
			
			var doc_5_upload_file = $("#docUrl_5").val();
			var appPensionProofId = $("#appPensionProofId").val();
			if(doc_5_upload_file!="" && appPensionProofId==0 && appPensionProofId!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appPensionProofId").rules('add', {
					required: true,
					valueNotEquals: 0
				});
				customOnFocusElement('appPensionProofId');
			}else if(doc_5_upload_file=="" && appPensionProofId==0 && appPensionProofId!=undefined){
				$("#appPensionProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appPensionProofId!=0 && appPensionProofId!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appPensionProofId").rules('add', {
					uploadCheck:"docUrl_5"
				});
				customOnFocusElement('appPensionProofId');
			}
			
		}else if(loanTypeId==4){
			var doc_5_upload_file = $("#docUrl_5").val();
			var appEmploymentProofId_val = $("#appAcademicProofId").val();
			if(doc_5_upload_file!="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appAcademicProofId").rules('add', {
			        required: true,
			        valueNotEquals: 0
			    });
				customOnFocusElement('appAcademicProofId');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				$("#appAcademicProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val!=0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appAcademicProofId").rules('add', {
					uploadCheck:"docUrl_5"
			    });
				customOnFocusElement('appAcademicProofId');
			}
		}else if(loanTypeId==15){
			var doc_5_upload_file = $("#docUrl_5").val();
			var appEmploymentProofId_val = $("#appLandOwnerProofId").val();
			if(doc_5_upload_file!="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appLandOwnerProofId").rules('add', {
			        required: true,
			        valueNotEquals: 0
			    });
				customOnFocusElement('appLandOwnerProofId');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val==0 && appEmploymentProofId_val!=undefined){
				$("#appLandOwnerProofId").rules('remove');
			}else if(doc_5_upload_file=="" && appEmploymentProofId_val!=0 && appEmploymentProofId_val!=undefined){
				if (!isValidationFail) {isValidationFail=true;}
				$("#appLandOwnerProofId").rules('add', {
					uploadCheck:"docUrl_5"
			    });
				customOnFocusElement('appLandOwnerProofId');
			}
		}
	}
}


function generateHTMLDiv(elementId,  labelName, isMandatory, labelClass, toolTip ){
	var mandatoryString = ((isMandatory==1)?'<b class="req">*</b>':'');
	var className="";
	if(isApplicationStatusPage){
		className='info-row2';
	}else if(isOfferPage){
		className='div-left3';
	}else{
		className='div-left';
	}
	var html='<div id="div'+elementId+'" class="'+className+'">'
					+'<label class='+(labelClass!=undefined && labelClass!=''?labelClass:'')+' >'+labelName +mandatoryString+'&nbsp;'+((toolTip!=undefined && toolTip!=null)?toolTip:'')+'</label>'
				+'</div>';
	return html;
}
function generateSelectHtml(elementId, elementName, functionNme){
	var html ='<select name="'+elementName+'" id="'+elementId+'" autocomplete="off" class="w175" style="border-color: rgb(197, 197, 197); onchange="'+functionNme+'">';
	return html;
}
function validateRulesCapturedValue(){
	$("#capturedValue").validate({
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
	          	showMsg(message, false);
	        }
	    },
		errorElement: "span",	
	    rules: {		
		    'manualEligVal': {
				valueNotEquals: 0
		    },
		    'manualTenureVal': {
				valueNotEquals: 0
		    }
	    },
		messages: {
			"manualEligVal":{
				required: "Eligibility cannot be blank"				
			},
			"manualTenureVal":{
				required: "Tenure cannot be blank"
			}
		}		    
	});
}
function showLoaderInsideDropdown(elementId){
	jQuery("#"+elementId).css('background','url('+BANK_IMAGE_FOLDER_NEWUI+'/sbi-loader-small.gif) no-repeat 90% 0px');
}
function removeLoaderInsideDropdown(elementId){
	jQuery("#"+elementId).css('background','');
}
function toolTipFromDB(toolTipJSArray){
	toolTipJSArray = toolTipJSArray.replace(/%27/g, "\'");
	toolTipJSArray = toolTipJSArray.replace(/&quot;/g, "\"");
	jsonToolTip = jQuery.parseJSON(toolTipJSArray);
	if(jQuery.isEmptyObject(jsonToolTip)){
		alert("Sorry for the inconvenience, Please refresh");
	}
	
	$.each(jsonToolTip, function(id, elements){
		if( jQuery("#"+elements.element_id).val()!=null && jQuery("#"+elements.element_id).val()!=undefined){
			if(elements.element_type==1){
				if($("#"+elements.element_id).attr('placeholder')=='Rs'){
					toolTipText="M.toolTip_TM('"+elements.element_tooltip+"', event, this, 150, 65, -30); this.value=M.unformatMoney(this.value);";	
				}else{
					toolTipText="M.toolTip_TM('"+elements.element_tooltip+"', event, this, 150, 65, -30);";
				}
				jQuery("#"+elements.element_id).attr("onfocus",toolTipText);
			}else if(elements.element_type==2){
				toolTipText='<a class="tooltip"><img alt="" src="'+BANK_IMAGE_FOLDER_NEWUI+'/question-icon.png"><span>'+elements.element_tooltip+'</span></a>';
				jQuery("#"+elements.element_id).prev().prev().append(toolTipText);
			}
			
		}
	});
}
function toolTipFromDB(toolTipJSArray, elem){
	toolTipJSArray = toolTipJSArray.replace(/%27/g, "\'");
	toolTipJSArray = toolTipJSArray.replace(/&quot;/g, "\"");
	jsonToolTip = jQuery.parseJSON(toolTipJSArray);
	if(jQuery.isEmptyObject(jsonToolTip)){
		alert("Sorry for the inconvenience, Please refresh");
	}
	
	$.each(jsonToolTip, function(id, elements){
		if( jQuery("#"+elements.element_id).val()!=null && jQuery("#"+elements.element_id).val()!=undefined){
			if(elements.element_type==1){
				if($("#"+elements.element_id).attr('placeholder')=='Rs'){
					toolTipText="M.toolTip_TM('"+elements.element_tooltip+"', event, this, 150, 65, -30); this.value=M.unformatMoney(this.value);";	
				}else{
					toolTipText="M.toolTip_TM('"+elements.element_tooltip+"', event, this, 150, 65, -30);";
				}
				jQuery("#"+elements.element_id).attr("onfocus",toolTipText);
			}else if(elements.element_type==2){
				toolTipText='<a class="tooltip"><img alt="" src="'+BANK_IMAGE_FOLDER_NEWUI+'/question-icon.png"><span>'+elements.element_tooltip+'</span></a>';
				jQuery("#"+elements.element_id).prev().prev().append(toolTipText);
			}
			
		}
	});
}
function getCallLogDetails(errorDisplayDiv, appSeqId){
	if(isDsrPage){
		if(appSeqId!=undefined && appSeqId!=null && appSeqId!='' && appSeqId!=0){
			formdata="requestIndex=23&requestType=2&appSeqId="+appSeqId;
		}else{
			formdata="requestIndex=23&requestType=2";
		}
		
		jQuery.ajax({
		    type: "POST",
			url: ajaxPostUrl,
			data: formdata,
			//headers : {"Accept-Encoding" : "gzip"},
		    success:function(htmlContent){
		    	if(htmlContent==null || htmlContent ==undefined){
		    		showMsg(SORRY_FOR_INCONVENIENCE, false);
	        		return false;
	        	}
		    	var stringMessage = [];
	        	stringMessage = htmlContent.split("|");
	        	if(stringMessage[0]=="error"){
	        		showMsg('<b>Information: </b><em>'+stringMessage[1]+'</em>', false);
	        	}else{
	        		jQuery("#callLogContent").html(htmlContent);
	        	}    	
		    },
		    error:function(jqXHR, textStatus){
		    	showMsg(SORRY_FOR_INCONVENIENCE, false);
		    	return false;
		    }
		});
	}
}

function setDateFormat(strDate, elementId){
	if(strDate!='' && strDate!='0' && strDate!=undefined && strDate!=null){
		jQuery("#"+elementId).val(strDate.substring(3,5)+"/"+strDate.substring(0,2)+"/"+strDate.substring(6,10));
	}
}
function unsetDateFormat(strDate, elementId){
	if(strDate!='' && strDate!='0' && strDate!=undefined && strDate!=null){
		jQuery("#"+elementId).val(strDate.substring(3,5)+"-"+strDate.substring(0,2)+"-"+strDate.substring(6,10));
	}
}
function loanFirstPage(){
	dropOffPopupStatus=1;
	closeOffStatus=1;
	if(uiType!=undefined && uiType!=""){
		window.location.href = window.location.protocol + "//" + window.location.host +"/" +CONTEXT+ajaxPostUrl+"?uiType="+uiType;
	}else{
		window.location.href = window.location.protocol + "//" + window.location.host +"/" +CONTEXT+ajaxPostUrl;
	}
}
function sbiHomePage(){
	dropOffPopupStatus=1;
	closeOffStatus=1;
	if(uiType!=undefined && uiType!=""){
		window.location.href = window.location.protocol + "//" + window.location.host +"/" +CONTEXT+"home?uiType="+uiType;
	}else{
		window.location.href = window.location.protocol + "//" + window.location.host + ctxPath;
		
	}
}

function stateCityDistrictValidation(selector) {
	var countryOfStudyId=jQuery("#CountryofStudyId").val();
	var selArr  = []; 
	var errorFlag = false;
	var errorType=0;//1=state, 2=city, 3=district, 4= district-branch, 5=branch
	if(loanTypeId == 1) {
		//homeloan
		selArr = ["preferredState","preferredCity","preferredDistrict", "preferredBranch" ];
	} else if(loanTypeId == 2){
		//autoloan
		selArr = ["stateId","cityId","districtId", "branchId" ];
	} else if(loanTypeId == 3){
		//perosnalloan
		if($('#loanPurpose').val()==23){
			if(selector == '#pensionPayingStateId'){
				selArr = ["pensionPayingStateId","pensionPayingCityId","pensionPayingDistrictId", "pensionPayingBranchId" ];
			} else if(selector == '#preferredStateId'){
				selArr = ["preferredStateId","preferredCityId","preferredDistrictId", "preferredBranchId" ];
			}
		}else{
			if(selector == '#workStateId'){
				selArr = ["workStateId","workCityId","workDistrictId", "workBranchId" ];
			} else if(selector == '#propertyStateId'){
				selArr = ["propertyStateId","propertyCityId","propertyDistrictId", "propertyBranchId" ];
			}
		}
	} else if(loanTypeId == 4){
		//educationloan
		selArr = ["residentStateId","residentCityId","districtId", "branchId" ];
	} 
	preLocVal = $(selector +" option:selected").val();
	if(!preLocVal){
		errorFlag = true;
	}
	if( preLocVal != "0" && !(loanTypeId == 2 && preLocVal == "2") ) {
		if(!$("#" +selArr[0]+ " option:selected").val()){
			errorFlag = true;
			errorType=1;
		} else {
			var cityVal = $("#" +selArr[1]+ " option:selected").val();
			if(!cityVal){
				errorFlag = true;
				errorType=2;
			} else {
				if(!(loanTypeId == 4 && countryOfStudyId == 2 && BANK_ID == BANK_ID_SBI)){
					if( cityVal == "9999999" && !$("#" +selArr[2]+ " option:selected").val()){
						errorFlag = true;
						errorType=3;
					} else if(cityVal == "9999999" && $("#" +selArr[2]+ " option:selected").val() && !$("#" +selArr[3]+ " option:selected").val()){
						errorFlag = true;
						errorType=4;
					} else if(cityVal != "9999999" && !$("#" +selArr[3]+ " option:selected").val()){
						errorFlag = true;
						errorType=5;
					}
				}  
				}
		}
	}
	if(errorFlag){
		var error_msg="";
		if(errorType==5){
			$("#"+selArr[1]).val(0).change();
			$("#"+selArr[1]).val(0).addClass('error').removeClass('fade');
			error_msg="Sorry, we did not find any branch for your current selection of city/district. Please select a branch from your nearby city/district or visit any of our branches to apply.";
		}else if (errorType == 4){
			$("#"+selArr[2]).val(0).change();
			$("#"+selArr[2]).val(0).addClass('error').removeClass('fade');
			error_msg="Sorry, we did not find any branch for your current selection of city/district. Please select a branch from your nearby city/district or visit any of our branches to apply.";
		} else{
			$(selector).val(0).change();
			$(selector).val(0).addClass('error');
			error_msg="Please reinitiate filling the  prefered location details.";
		}
		showMsg(error_msg,false);
		goToByScroll("msg-panel");
		return false;
	}
	return true;
}

function workAndJoinMonthValidation(){
	$('.error').removeClass('error');
	var joiningYear = $("#appCompanyJoiningYear option:selected").val() ;
	if(joiningYear != 0 ){
		$("#appCompanyJoiningYear").removeClass('error');
		var joiningMonth = $("#appCompanyJoiningMonth option:selected").val();
		if(joiningMonth == 0 && !$("#appCompanyJoiningMonth").attr("disabled")){
			$("#appCompanyJoiningMonth").addClass('error');
			showMsg('Please select the joining month.',false);
			$(window).scrollTop(0);
			return false;
		} 
	} 
	var workExp = $("#appWorkExperienceYear option:selected").val() ;
	var workExpMonth = $("#appWorkExperienceMonth option:selected").val();
	if(workExp > 0  && workExp < 13 && !$("#appWorkExperienceMonth").hasClass('fade')){
		$("#appWorkExperienceYear").removeClass('error');
		if(workExp!=12){
			if(workExpMonth == 13){
				$("#appWorkExperienceMonth").addClass('error');
				showMsg('Please select the work experience month.',false);
				$(window).scrollTop(0);
				return false;
			} 
		}
	} else if(workExpMonth == 0  ||  workExpMonth == 13 ){
		$("#appWorkExperienceYear").addClass('error');
		showMsg('Please select the work experience year.',false);
		$(window).scrollTop(0);
		return false;
	}
	return true;
}

function ValidateEmail(mail) {
	if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(mail)) {
		return (true);
	}
	return false;
}

function validationForMobile(mobile) {
	if (!isNaN(mobile)) {
		return false;
	} else if (!M.isMobile(mobile)) {
		return false;
	}
}
function isPassport(passportNumber){
	var i;
	var count=0;
	if(passportNumber.charAt(0).match(/^[a-zA-Z]/)){
		count=1;
		for(i=1;i<=passportNumber.length-1;i++){
			if(passportNumber.charAt(i).match(/^[0-9]/)){
				count++;
			}
		}
	}
	if(passportNumber.length==count){
		return true;
	}else{
		return false;
	}
}

function isVoterId(voterIdNumber){
	var i;
	var count=0;
	if(voterIdNumber.charAt(0).match(/^[a-zA-Z]/) && voterIdNumber.charAt(1).match(/^[a-zA-Z]/)){
		count=2;
		if(voterIdNumber.charAt(2).match(/^[a-zA-Z]/)){
			count=3;
		}
		for(i=count;i<=voterIdNumber.length-1;i++){
			if(voterIdNumber.charAt(i).match(/^[0-9\/]*$/)){
				count++;
			}
		}
	}
	if(voterIdNumber.length==count){
		return true;
	}else{
		return false;
	}
	
}

function openPopups(openForLoan, openForProduct){
	$("#"+openForLoan+"HTML").mCustomScrollbar({
		theme:"rounded",
		scrollInertia:5
	});
	
	//if (openForLoan == 'OTP1' || openForLoan == 'consentHomeLoan') { 
		//code to show cbs OTP popup (after bootstrap v5 upgrade)
		const openForLoanEl = document.getElementById(openForLoan);
		const openForLoanModal = new bootstrap.Modal(openForLoanEl, {
		  backdrop: 'static',
		  keyboard: false
		});
	
		openForLoanModal.show();
	//}

}
function customOnFocusElement(elementId){
}

function customOnFocus(src){
}
function customOnBlur(src){
}
function dob_date_picker(formId,fieldId){
	jQuery("#"+formId+" #"+fieldId).datepicker({
		debug: true,
		widgeParent: 'lidate_of_birth',   
		orientation : "top left",
		autoclose: true,			
		startDate: new Date(min_range, current_month, current_day),
		endDate: new Date(max_range, current_month, current_day)               
	}).on('changeDate', function(){
		$('#'+fieldId).removeClass('error');
		$('#'+fieldId).next().remove();
	});
}

$(document).on("keyup", ".dob-cal", function(){
	if(this.parentNode.id == 'divco_date_of_birth1AL'){
		$('.datepicker.datepicker-dropdown.dropdown-menu.datepicker-orient-left.datepicker-orient-top').addClass('cal-pop');
	}
	setCalendarPlacing(this.parentNode.id);
});

$(document).on("click", ".dob-cal", function(){
	if(this.parentNode.id == 'divco_date_of_birth1AL'){
		$('.datepicker.datepicker-dropdown.dropdown-menu.datepicker-orient-left.datepicker-orient-top').addClass('cal-pop');
	}
	setCalendarPlacing(this.parentNode.id);
});

$(document).on("focus", ".dob-cal", function(){
	if(this.parentNode.id == 'divco_date_of_birth1AL'){
		$('.datepicker.datepicker-dropdown.dropdown-menu.datepicker-orient-left.datepicker-orient-top').addClass('cal-pop');
	}
	var dobId = this.parentNode.id;
	setTimeout(function(){setCalendarPlacing(dobId);},1000);
});

function setCalendarPlacing(id){
	$('.datepicker.datepicker-dropdown.dropdown-menu').appendTo("#"+id);
}

$(document).ready(function(){
	if(DEPLOYMENT_MODE!='local'){
		$('body').on('contextmenu',function(e){
			return true;
		});	
	}
});

function setCurrencyFormat(formId){
	$("#"+formId+" :text").each(function(){
		if($(this).attr('placeholder')=='Rs'){
			var strip_commas = $(this).val().replace(/,/g, "");
			if(strip_commas>0){
				strip_commas = Number(strip_commas).toPrecision();
				strip_commas = strip_commas.replace(".0","");
				$(this).val(M.moneyFormat(strip_commas));
			}
		}
	});
}


function closeMobileNavigation()
 {
	if ($('#mainMenu').length) {
		$('#mainMenu').data('mmenu').close();
	}
 }
function cloaseOff() {
//	if(DEPLOYMENT_MODE=='local'){
		return false;
//	}
	if(typeof closeOffStatus !== 'undefined' && closeOffStatus == 0) {
		jQuery.ajax({
			type:'get',
			url: "logout",
			success:function(htmlContent){
			},
			error:function(data){
			}
		});
		return false;
	}
}

function isIdProofValid(idProof, validationFor){
	if (idProof!=undefined && idProof!=null && idProof!='' && validationFor!=undefined && validationFor!=null && validationFor!='') {
		var regEx;
		if (validationFor==165) {
			regEx = /^[a-zA-Z]{3}[0-9]{4,17}$/;
		}else if (validationFor==166) {
			regEx = /^([-\w]){6,16}$/;
		}else if (validationFor==181) {
			regEx = /^([A-Za-z]{3})+(P|C|F|p|c|f)+([A-Za-z]{1})+([0-9]{4})+([A-Za-z]{1})$/;
		}else if (validationFor==182) {
			regEx = /^[1-9]{1}[0-9]{11}$/;
		}else if (validationFor==183) {
			regEx = /^[1-9]{1}[0-9]+$/;
		}else{
			if(idProof.length>0 && idProof.length<=12){
				return true;
			}else{
				return false;
			}
		}
		return regEx.test(idProof);
	}else{
		return false;
	}
}
function getRequest(formId, objectType) {
	var generateSecretKeyStatus = getSecretKey();
	if(!generateSecretKeyStatus){
		return false;
	}
	var requestObj = {};
	var serializedString = $('#' + formId).serialize();
	if(objectType==0){
		
	}else if(objectType==1){
		serializedString = serializedString.replace(/lead\./g,"")
	}else if(objectType==2){
		serializedString = serializedString.replace(/quote\./g,"")
	}else if(objectType==3){
		serializedString = serializedString.replace(/appForm\./g,"")
	}else if(objectType==4){
		serializedString = serializedString.replace(/cbs\./g,"")
	}else if(objectType==5){
		serializedString = serializedString.replace(/quote\./g,"")
		serializedString = serializedString.replace(/appForm\./g,"")
	}
	serializedString = serializedString.replace(/\+/g, '%20');
	var formFieldArray = serializedString.split("&");
	$.each(formFieldArray, function(i, pair) {
		var nameValue = pair.split("=");
		if(nameValue[1]!=''){
			var name = decodeURIComponent(nameValue[0]);
			var value = decodeURIComponent(nameValue[1]);
			requestObj[name] = value;
		}
	});
	var txtPayload = JSON.stringify(requestObj);
	return getEncryptedRequest(txtPayload, secretKey);
}
function getEncryptedRequest(planeText, key){
	 var iv = CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
     var salt = CryptoJS.lib.WordArray.random(128/8).toString(CryptoJS.enc.Hex);
     var aesUtil = new AesUtil(128, 1000);
     var ciphertext = aesUtil.encrypt(salt, iv, key, planeText);
     var aesText = (iv + "::" + salt + "::" + ciphertext);
     var encryptedText = btoa(aesText);
     return encryptedText;
}
function getRequestWithoutForm(serializedString) {
	var generateSecretKeyStatus = getSecretKey();
	if(!generateSecretKeyStatus){
		return false;
	}
	var requestObj = {};
	serializedString = serializedString.replace(/\+/g, '%20');
	var formFieldArray = serializedString.split("&");
	$.each(formFieldArray, function(i, pair) {
		var nameValue = pair.split("=");
		if(nameValue[1]!=''){
			var name = decodeURIComponent(nameValue[0]);
			var value = decodeURIComponent(nameValue[1]);
			requestObj[name] = value;
		}
	});
	var txtPayload = JSON.stringify(requestObj);
	return getEncryptedRequest(txtPayload, secretKey);
}
function getSecretKey(){
	var reuturnVal = false;
	secretKey="";
	jQuery.ajax({
		type:'get',
		url: ctxPath+"/getString",
		async: false,
		cache: false,
		success:function(htmlContent){
			if((htmlContent!=undefined || htmlContent!=null || htmlContent!='') && htmlContent.indexOf("error") ==-1 ){
				secretKey=htmlContent;
				reuturnVal = true;
			}else{
				reuturnVal= false;
			}
		},
		error:function(xhr, errorType, exception){
			reuturnVal= false;
		}
	});
	return reuturnVal;
}

$(document).on("contextmenu",function(e){
	alert('Right click is disabled.');
	return false;
});
