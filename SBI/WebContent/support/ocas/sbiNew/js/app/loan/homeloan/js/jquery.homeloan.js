//employmentType

var workExpData = [{'key':0, 'value':'Select work experience'},{'key':1, 'value':'Less than 6 months'},
                   {'key':2, 'value':'6 months or more with a job contract of min 2 years abroad'},
                   {'key':3, 'value':'2 years or more'},
                   {'key':4, 'value':'None of the above'}];
var workExpForFlexiData = [{'key':0, 'value':'Select work experience'},{'key':1, 'value':'Less than 2 years'},
                   {'key':2, 'value':'2 years or more'}];

var genderData = [{'key':0, 'value':'Select Gender'},{'key':'M', 'value':'Male'},{'key':'F', 'value':'Female'},{'key':'O', 'value':'Other'}];

var industryTypeDataGov=[{'key':20, 'value':'Central Govt / State Govt'},,{'key':83, 'value':'Defence Personnel'},{'key':22, 'value':'Listed Pvt Ltd'},
							{'key':21, 'value':'MNC'},{'key':23, 'value':'Unlisted Pvt Ltd'},{'key':9999999, 'value':'Others'}];

var maritalStatusData=[{'key':0, 'value':'Select Marital Status'},{'key':1, 'value':'Married'},{'key':2, 'value':'Single'}];

jQuery(document).on("change","#loanPurpose", function(){
	showPerspectiveRows4HomeLoan(this);
	if( jQuery("#loanPurpose").val()==2){
		$('#CHECKOFFTYPE').html('');
	}
});

function showPerspectiveRows4HomeLoan(src){
	$("#PuccaHouse").html('');
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	$('#loanCategoryDetails').html('');
    jQuery("#preferredLocation").val(0).trigger("change");
 
    if(genderValue!=undefined && genderValue!=''){
    	if($("#gender").val()!='' || $("#gender").val() != undefined){
    		
    	}else{
    		jQuery("#gender").val(genderValue);
    	}
     }else{
    	 if($("#gender").val()!='' || $("#gender").val() != undefined){
    		
    	 }else{
    		 jQuery("#gender").val(0).trigger("change");
    	 }
     }
    divErase4addMaritalStatus = "divloanQuoteMaritalStatus";
    if(divErase4addMaritalStatus!=undefined && divErase4addMaritalStatus!=''){
    	jQuery('#'+divErase4addMaritalStatus).remove();
    }
    
    var currentRow=src.parentNode;
    var currentId = currentRow.id;
    var selVal=src.options[src.selectedIndex].value;
    var  loanLink = "";
	for(var i =0; i<loanPurposeLinks.length ;i++){
		if(loanPurposeLinks[i].key == selVal){
			loanLink = loanPurposeLinks[i].value;
		}
	}
	if(selVal>0){
		//flatFieldControl(propertyCategory);
		var	linkforLoanPurpose ="<a  target='_blank' href='http://"+loanLink+"'><span class='note'>Click to read the scheme details before applying</span></a>";
		jQuery("#plmsgId").html(linkforLoanPurpose);
	}else{
		jQuery("#plmsgId").html('');
	}
	jQuery("#employmentType").val(0);
	jQuery("#employmentType").trigger("change");
	jQuery("#residentType").val(0);
	jQuery("#residentType").trigger("change");
	jQuery("#employmentType").attr('disabled',true);
    if(selVal!=undefined){
    	$('#homeloanform #LOANCATEGORYLIST').remove();
    	$('#loanCategoryDetails').html('');
    	$('#INSTAMARITALSTATUS').html('');
    	changeAvailingLoan();
    	var innerHtml='';
    	if(selVal>0){
    		if(selVal == 4 ){
				$('#INSTAMARITALSTATUS').html($('#htmlMaritalStatus').html());
    			$("#PuccaHouse").html('');
    			/*$('#divPmayITR').html('');*/
    			return false;
    		}else if(selVal == 1 || selVal == 3){
    			disableById('propertyCategory', true);
    			disableById('preferredLocation', false);
    			jQuery.ajax({
    			    type: "POST",
    				url: "loanPurposeByParentId",
    				data: "lpParentId="+selVal+"&loanTypeId="+loanTypeId,
    				dataType: "json",
    				//headers : {"Accept-Encoding" : "gzip"},
    			    success:function(json){
    			    	disableById('preferredLocation', false);
    			    	disableById('propertyCategory', false);
    			    	$('#LILOANPURPOSE').after($('#htmlLoanCategoryId').html());
		        		populateDropdownFromJSONArrayList('propertyCategory', json.elementData, 0);
    			    },
    			    error:function(jqXHR, textStatus){
    			       //do something
    			    }
    			});
    			return false;
    		}else if(selVal == 5){
    			// Nothing happened

    			$("#PuccaHouse").html('');
    			/*$('#divPmayITR').html('');*/
    		}else{
    			$('#loanCategoryDetails').html($('#htmlLoanPurposeId'+selVal).html());
    			if(selVal == 25){
    				auto_suggestion('builderName');
    			}
    		}
    	}
    }
    return false;
}

jQuery(document).on("change","#propertyCategory", function() {
	showPerspectiveRows4PropertyCategory(this);
	if( jQuery("#loanPurpose").val()==2){
		$('#CHECKOFFTYPE').html('');
	}		
});

function showPerspectiveRows4PropertyCategory(src){
	jQuery("#residentType").val(0);
	jQuery("#residentType").trigger("change");
	$("#PuccaHouse").html('');
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	$('#loanCategoryDetails').html('');
    	$('#divReimbursement').html('');
    	$('#divConstructionCost').html('');
    	if(selVal>0){
    		$('#loanCategoryDetails').html($('#htmlLoanCategoryId'+selVal).html());
    		if(selVal==1 || selVal==2 || selVal==4 || selVal==5){
    			$('#divReimbursement').html($('#htmlHomeLoanReimbursement').html());
    			$('#divConstructionCost').html($('#htmlCostOfConstruction').html());
    		}
    		if(selVal==1 || selVal == 2){
    			if(isRegisteredBuilder){
    			}else{
    				auto_suggestion('builderName');
    			}
    		}
    		if(isRegisteredBuilder && jQuery("#builderName").val()!=""){
    			jQuery("#builderName").addClass("disabledFields");
    			jQuery("#builderName").attr("disabled",true);
    		}
    		if(selVal==5){
    			$("#PuccaHouse").html('');
    			/*$('#divPmayITR').html('');*/
    		}
    		var loanPurpose1=$('#loanPurpose').val();
    		var resident=$('#residentType').val();
    		if(loanPurpose1==1 && resident==1 &&  (selVal==1 || selVal==2 || selVal==4)){
    			$("#PuccaHouse").html($('#htmlhomePmayPuccaHouse').html());
    		}
    		
		}
	}
    return false;
}

function showPerspectiveRows4ResidentType(src){
    if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var selVal=src.options[src.selectedIndex].value;
    changeAvailingLoan();
    $('#RESIDENTCOUNTRY').html('');
    $('#employmentType').val('0');
    if(selVal!=undefined){
    	var innerHtml='';
    	if(selVal>0){
	    		changeEmploymentType('employmentType','residentType');
	    		if(selVal == 1){
	    			$("#RESIDENTTYPEVALUE").show();
	    			$("#251").removeClass("inactiveLink");
	    			var loanPurpose1=$('#loanPurpose').val();
	    			var propCat = jQuery("#propertyCategory").val();
	    			if(propCat!=undefined && propCat==5){
	    				$("#PuccaHouse").html('');
	    				/*$('#divPmayITR').html('');*/
	    			}
	    			if((propCat!=undefined && propCat!=null) && loanPurpose1==1 && (propCat==1 || propCat==2 || propCat==4)  ){
	    				$("#PuccaHouse").html($('#htmlhomePmayPuccaHouse').html());
	    			}
	    			return true;
	    		}
	    		if(selVal == 2 ){
	    			
	    			$("#RESIDENTTYPEVALUE").hide();
	    			
	    			$("#PuccaHouse").html('');
	    			/*$('#divPmayITR').html('');*/
	    			$('#RESIDENTCOUNTRY').html($('#htmlResidentCountryRegion').html());
	    			populateDropdownFromJSONArrayList('co_countryRegion1', residentCountries, 0);
	    		}
		}else{
			$("#PuccaHouse").html('');
			/*$('#divPmayITR').html('');*/
			jQuery("#employmentType").val(0);
    		jQuery("#employmentType").trigger("change");
    		jQuery("#employmentType").attr("disabled", true);
    	}
    }
    changeAvailingLoan();
    return false;
   }
   

function showPerspectiveRows4EmpTypeHomeLoan(src){
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var selVal=src.options[src.selectedIndex].value;
    $('#divPmayITR').html('');
    $('#SalariedEmp').html('');
    $('#expectedRentalBusiness').html('');
    $('#expectedRentalAgri').html('');
    $('#expectedRentalRetiredPensioner').html('');
    var residentType=$('#residentType').val();
    var propertyCategory=$('#propertyCategory').val();
    if(selVal!=undefined){
    	var innerHtml='';
    	var loanPurpose=$('#loanPurpose').val();
    	if(selVal>0){
    		if(selVal==1){
    			$('#SalariedEmp').html($('#htmlEmpSalariedTypes').html());
    			 $('#divPmayITR').html('');
    			
    			$('#homeloanform #divworkExperience').remove();
    			if(residentType==1){
    				var propertyCategory=$('#propertyCategory').val();
    				if(loanPurpose==1 && (propertyCategory==1 ||propertyCategory==2 || propertyCategory==4) && residentType==1){
        				if(applicationSubTypeId==2){
        					$("#divpreEMIsOther").after("<div id='divPmayITR'></div>");
            				$('#divPmayITR').html($('#htmlhomePmayItr').html());
        				}else{
        					$("#divpreEMIs").after("<div id='divPmayITR'></div>");
        					$('#divPmayITR').html($('#htmlhomePmayItr').html());
        				}
        			}
    				if(loanPurpose!=0 && loanPurpose==1 && propertyCategory!=3){
    					$('#divemployerName').after($('#htmlWorkExResIndian').html());
    					//$('#divPmayITR').html('');
    				}
    				
        		}else if(residentType==2){
        			$('#divemployerName').after($('#htmlWorkExNRICountry').html());
        			 $('#divPmayITR').html('');
        		}
    			auto_suggestion_employer('employerName');
    		}else if(selVal==2 || selVal==3){
    			$('#SalariedEmp').html($('#htmlSelfEmpBusiness').html());
    			 $('#divPmayITR').html('');
    			
    			$('#expectedRentalBusiness').html($('#htmlExpectedMonthRental').html());
    			$('#workExSelfEmpBusiness').html('');
    			if(residentType==1 && (selVal==2 || selVal==3)){
    				if(loanPurpose==1 && (propertyCategory==1 ||propertyCategory==2 || propertyCategory==4) && residentType==1){
	    				if(applicationSubTypeId==2){
	    					$("#divpreEMIsOther").after("<div id='divPmayITR'></div>");
	        				$('#divPmayITR').html($('#htmlhomePmayItr').html());
	    				}else{
	    					$("#divpreEMIs").after("<div id='divPmayITR'></div>");
	    					$('#divPmayITR').html($('#htmlhomePmayItr').html());
	    				}
    				}	
        		}else if(residentType==2){
        			$('#workExSelfEmpBusiness').html($('#htmlWorkExNRICountry').html());
        		}
    			
    		}else if(selVal==4){
    			$('#SalariedEmp').html($('#htmlAgriculturist').html());
    			$('#expectedRentalAgri').html($('#htmlExpectedMonthRental').html());
    			$('#workExSelfEmpAgri').html('');
    			if(residentType==1){
    				 $('#divPmayITR').html('');
    	    			if(loanPurpose==1 && (propertyCategory==1 ||propertyCategory==2 || propertyCategory==4) && residentType==1){
    	    				if(applicationSubTypeId==2){
            					$("#divpreEMIsOther").after("<div id='divPmayITR'></div>");
                				$('#divPmayITR').html($('#htmlhomePmayItr').html());
            				}else{
            					$("#divpreEMIs").after("<div id='divPmayITR'></div>");
            					$('#divPmayITR').html($('#htmlhomePmayItr').html());
            				}
    	    			}
        		}else if(residentType==2){
        			$('#workExSelfEmpAgri').html($('#htmlWorkExNRICountry').html());
        		}
    			
    		}else if(selVal==5){
    			$('#SalariedEmp').html($('#htmlRetiredPensinor').html());
    			$('#expectedRentalRetiredPensioner').html($('#htmlExpectedMonthRental').html());
    			$('#workExSelfEmpRetiredPensioner').html('');
    			if(residentType==1){
    				 $('#divPmayITR').html('');
    				if(loanPurpose==1 && (propertyCategory==1 ||propertyCategory==2 || propertyCategory==4) && residentType==1){
    					if(applicationSubTypeId==2){
        					$("#divpreEMIsOther").after("<div id='divPmayITR'></div>");
            				$('#divPmayITR').html($('#htmlhomePmayItr').html());
        				}else{
        					$("#divpreEMIs").after("<div id='divPmayITR'></div>");
        					$('#divPmayITR').html($('#htmlhomePmayItr').html());
        				}
        			}
        		}else if(residentType==2){
        			$('#workExSelfEmpRetiredPensioner').html($('#htmlWorkExNRICountry').html());
        		}
    			
    		}else if(selVal==6 || selVal==7){
    			$('#SalariedEmp').html($('#htmlRetired').html());
    			$('#workExSelfEmpRetired').html('');
    			if(residentType==1){
    				 $('#divPmayITR').html('');
    				 if(loanPurpose==1 && (propertyCategory==1 ||propertyCategory==2 || propertyCategory==4) && residentType==1){
    					 if(applicationSubTypeId==2){
         					$("#divpreEMIsOther").after("<div id='divPmayITR'></div>");
             				$('#divPmayITR').html($('#htmlhomePmayItr').html());
         				}else{
         					$("#divpreEMIs").after("<div id='divPmayITR'></div>");
         					$('#divPmayITR').html($('#htmlhomePmayItr').html());
         				}
         			}
        		}else if(residentType==2){
        			$('#workExSelfEmpRetired').html($('#htmlWorkExNRICountry').html());
        		}
    		}
    		apply_currency_form(document.homeloanform);
    		}
    }
    return false;
}
jQuery(document).on("click","#salaryAccountWithSbiYes", function() {
	src = this; 
	if(src==undefined)return false;
	$("#homeloanform #salaryAccountWithSbiYes").attr('checked','checked');
	$('#corpsalarypackage').html('');
	$('#corpsalarypackage').html($('#htmlCorporateSalarypackage').html());
});

jQuery(document).on("click","#salaryAccountWithSbiNo", function() {
	src = this; 
	if(src==undefined)return false;
	$("#homeloanform #salaryAccountWithSbiNo").attr('checked','checked');
	$('#corpsalarypackage').html('');
});

jQuery(document).on("click","#co_salaryAccountWithSbi1No", function() {
	src = this; 
	if(src==undefined)return false;
});

jQuery(document).on("click","#co_salaryAccountWithSbi2No", function() {
	src = this; 
	if(src==undefined)return false;
});

function generateElement(elementId, elementName, elementType, elementData, selectedElementValue,funName,check,yearupto, className, startFrom){
	var elementOuterHTML='';
	if(elementType == 2){
		elem=M.create("select");
		elem.name=elementName;
		elem.setAttribute("id",elementId);
		elem.setAttribute("autocomplete","off");
		if(className!=undefined){
			elem.className=className;
		}else{
			elem.className="w167";
		}
		
		if(funName!=null && funName!="")
		elem.onChange=funName;
	}else if(elementType == 1){
		elem=M.create("input");
		elem.name=elementName;
		elem.setAttribute("id",elementId);
		elem.setAttribute("autocomplete","off");
		if(elementData !=null && elementData != undefined && elementData != ''){
			elem.value = elementData;
		}
		
		elem.className="w167";
		if(elementId=="builderName" || elementId=="employerName" || elementId=="co_employerName2" || elementId=="co_employerName1"){
			elem.maxLength=105;
			elem.setAttribute("placeholder","Type slowly for auto fill");
			if(elementData!=undefined && elementData!=""){
				elem.setAttribute("value",elementData[0]);	
			}
		}else if(elementId=='retirementAgeApplicant'){
			elem.maxLength=2;
		}else if(elementId=='co_retirementAgeApplicant1'){
			elem.maxLength=2;
		}else if(elementId=='co_retirementAgeApplicant2'){
			elem.maxLength=2;
		}else if(elementId=='co_date_of_birth1'){
			elem.setAttribute("placeholder","dd-mm-yyyy");
			elem.maxLength=10;
		}else if(elementId=='co_date_of_birth2'){
			elem.setAttribute("placeholder","dd-mm-yyyy");
			elem.maxLength=10;
		}else{
			elem.setAttribute("placeholder","Rs");
			if(browserver =='MSIE6' || browserver =='MSIE') {
	            elem.onfocus = function(){this.value=M.unformatMoney(this.value);};  
	            elem.onblur = function(){var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';};  
	            elem.onkeydown = function(){return M.digit(event);};
	            $(elem).on("focus blur", function(e) {
	                this.value = $.trim(this.value);    
	                if ((e.type === "focus") && this.createTextRange) {
	                    var r = this.createTextRange();
	                    r.moveStart("character", this.value.length);
	                    r.select();
	                }
	            });
	        } else {
	            M.attachEvent(elem, "blur", M.formatAsMoney);
	            M.attachEvent(elem, "focus", M.unformatMoney);
	            if(!isDsrPage){
	            	if(elementId=='employerName'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Current Employer Name', event, this, 175, 0, -25);");
		    		}else if(elementId=='costHomeFlat'){
		    			if(BANK_ID == BANK_ID_SBM){
		    				elem.setAttribute("onfocus","M.toolTip_TM('Excluding stamp duty, registration charges and other charges', event, this, 130, 65, -30);");
		    			}else{
		    				elem.setAttribute("onfocus","M.toolTip_TM('This includes car park, society charges, statutory deposit - water/electrical meter installation charges, club charges', event, this, 130, 65, -30);");
		    			}
		    		}else if(elementId=='landCost'){
		    			if(BANK_ID == BANK_ID_SBM){
		    				elem.setAttribute("onfocus","M.toolTip_TM('Inclusive of stamp duty, registration and development cost', event, this, 130, 70, 00);");
		    			}else{
		    			}
		    		}else if(elementId=='constructionCost'){
		    			if(jQuery("#propertyCategory").val()==1 || jQuery("#propertyCategory").val()==5){
		    				elem.setAttribute("onfocus","M.toolTip_TM('This includes cost of extension of a ready build property', event, this, 130, 65, -30);");
		    			}else{
		    				elem.setAttribute("onfocus","M.toolTip_TM('This includes sum total of amount for building home on a plot', event, this, 130, 65, -30);");
		    			}
		    			
		    		}else if(elementId=='outStandingBalance'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Current outstanding balance on the existing home loan', event, this, 130, 65, -30);");
		    		}else if(elementId=='currentValueOfProperty'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Current market value of property', event, this, 130, 65, -30);");
		    		}else if(elementId=='persentValueOfProperty'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Current market value of property', event, this, 130, 65, -30);");
		    		}else if(elementId=='loanTopupAmountRequested'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Required loan amount', event, this, 130, 65, -30);");
		    		}else if(elementId=='netMonthlySalary'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('This is your monthly salary as per your payslip after deductions (such as PF and tax). This does not include cash payments and in-kind components. The bank will ask you to furnish details later', event, this, 130, 65, -30);");
		    		}else if(elementId=='otherIncome'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('This is your total other monthly income except rental income. It includes all variable  income, monthly bonus,etc', event, this, 130, 65, -30);");
		    		}else if(elementId=='preEMIs'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('This is the sum total of all monthly payments that you all making on all your current open loans. Please ignore those loans where remaining loan term is less than 12 months', event, this, 130, 65, -30);");
		    		}else if(elementId=='appLoanEmployerName'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Current Employer's Name', event, this, 130, 65, -30);");
		    		}else if(elementId=='expectedRentalIncome'){
		    			elem.setAttribute("onfocus","M.toolTip_TM('Rental income will be considered only where the applicant already owns a house', event, this, 130, 65, -30);");
		    		}
	            	
	            }
		            
	            
	        }
			elem.maxLength=9;
	        elem.onkeydown=M.digit;
		}
		
		
		
	}else if(elementType==3){
		 
		elem=M.create("input");
	    try{  
	        elem = M.create('<input type=" radio" name="" id="" />');//<input type="radio" name=" '+elementName+' " Id="' +elementId+' ''   />' ) ;  
	    } catch(err){  
	        elem = M.create('input');  
	    }  
	    elem.setAttribute('type','radio');
	    elem.name=elementName;
	    elem.setAttribute("id",elementId);
	}else if(elementType==4){
		elem=M.create("select");
        elem.name=elementName;
        elem.setAttribute("id","month"+elementId);
        elem.className="gender date_dropdown_month";
        elem.options[elem.options.length]=new Option("Select month","0",false,true);
        for(var nxt=0;nxt<months.length;nxt++)elem.options[elem.options.length]=new Option(months[nxt],(nxt+1),false,false);
        elem.disabled=true;	
	}else if(elementType==5){
		elem=M.create("select");
		elem.name=elementName;
	    elem.setAttribute("id","year"+elementId);
		elem.className="gender date_dropdown_year";
		elem.style.marginRight="5px";
		elem.options[elem.options.length]=new Option("Select year","0",false,true);
		var YEAR=new Date().getFullYear();
		if(yearupto!=undefined || yearupto==""){
			yearupto=30;
		}
		for(var yearindex=0; yearindex <yearupto;yearindex++){
			if(BANK_ID == BANK_ID_SBP){
				if(yearindex <2){
					YEAR--;
					continue;
				}
			}
			elem.options[elem.options.length]=new Option(YEAR,YEAR--,false,false);
		}
	}else if(elementType==6){
		elem=M.create("select");
		elem.name=elementName;
	    elem.setAttribute("id","year"+elementId);
		elem.className="gender date_dropdown_year";
		elem.style.marginRight="5px";
		elem.options[elem.options.length]=new Option("Select year","0",false,true);
		var YEAR=new Date().getFullYear();
		if(yearupto!=undefined || yearupto=="")
			{yearupto=30;}
		for(var yearindex=0; yearindex <yearupto;yearindex++){
			elem.options[elem.options.length]=new Option(YEAR,YEAR++,false,false);
		}
	}else if(elementType==7){
		elem=M.create("select");
		elem.name=elementName;
	    elem.setAttribute("id","year"+elementId);
		elem.className="gender date_dropdown_year";
		elem.style.marginRight="5px";
		elem.options[elem.options.length]=new Option("Select year","0",false,true);
		var YEAR=new Date().getFullYear()+2;
		if(yearupto!=undefined || yearupto=="")
			{yearupto=30;}
		for(var yearindex=0; yearindex <yearupto;yearindex++){
			elem.options[elem.options.length]=new Option(YEAR,YEAR--,false,false);
		}	
	}else if(elementType==8){
		elem=M.create("select");
		elem.name=elementName;
	    elem.setAttribute("id",elementId);
		elem.className="gender";
		elem.style.marginRight="5px";
		elem.options[elem.options.length]=new Option("Select year","0",false,true);
		for(var yearindex=startFrom; yearindex <= yearupto; yearindex++){
			elem.options[elem.options.length]=new Option(yearindex,yearindex,false,false);
		}	
	}else if(elementType==9){
		elem=M.create("select");
		elem.name=elementName;
	    elem.setAttribute("id",elementId);
		elem.className="gender";
		elem.style.marginRight="5px";
		elem.options[elem.options.length]=new Option("Select month","0",false,true);
		for(var yearindex=startFrom; yearindex <= yearupto; yearindex++){
			elem.options[elem.options.length]=new Option(yearindex,yearindex+1,false,false);
		}	
	}else if(elementType==10){
		elem=M.create("select");
		elem.name=elementName;
		elem.setAttribute("id","year"+elementId);
		elem.className="gender";
		elem.style.marginRight="5px";
		if(startFrom !== undefined || startFrom==""){
			startFrom = new Date().getFullYear();
			yearupto = parseInt(yearupto)+parseInt(startFrom);
		}
		elem.options[elem.options.length]=new Option("Select year","0",false,true);
		for(var yearindex=startFrom; yearindex <= yearupto; yearindex++){
			elem.options[elem.options.length]=new Option(yearindex,yearindex,false,false);
		}	
	}
	elementOuterHTML = elem;
	return elementOuterHTML;
}

jQuery(document).ready(function(){
	jQuery(document).on("click","#coAppImageDetails", function(){
		if(!$('#coAppImageDetails').hasClass('collapsed')){
			$('#coApplicant1').html($('#htmlHomeLoanCoApplicant1Data').html());
			
			dob_date_picker('homeloanform', 'co_date_of_birth1');
			
			$("#coAppImageNext").addClass("collapsed");
			$("#coAppImage").removeClass("collapsed");
			$("#coAppImageNext").addClass("collapsed");
			$('#co_gender1').val(0);
		}else if($('#coAppImageDetails').hasClass('collapsed')){/*
			$('#coApplicant1').html('');
			$('#coPrefill1').html('');
			$('#coApplicant2').html('');
		*/}
	});
	
	jQuery(document).on("click","#coAppImage", function(){
		if($('#coAppImage').hasClass('collapsed') && ($('#coAppImage').attr('aria-expanded')==="false")){
			$('#coPrefill1').html('');
			$('#coApplicant1').html('');
			$('#coApplicant2').html('');
			$("#collapseTwo").removeClass("in");
			$("#coAppImageDetails").addClass("collapsed");
		}else if(!$('#coAppImage').hasClass('collapsed')){
			$('#coApplicant1').html($('#htmlHomeLoanCoApplicant1Data').html());
			$("#coAppImage").removeClass("collapsed");
			$('#co_gender1').val(0);
		}
	});
	
	jQuery(document).on("click","#coAppImageNext", function(){
		if($('#coAppImageNext').hasClass('collapsed') && ($('#coAppImageNext').attr('aria-expanded')==="false")){
			$("#coAppImageNext").removeClass("collapsed");
			$('#coPrefill2').remove();
		 	 $('#coApplicant2').html($('#htmlHomeLoanCoApplicant2Data').html());
		 	
		 	 dob_date_picker('homeloanform', 'co_date_of_birth2');
		 	
		 	 /*$("#homeloanform #co_date_of_birth2").datepicker ({
				 format: 'dd-mm-yyyy',
				 debug: true,
				 widgeParent: 'lidate_of_birth',
				 orientation : "top left",
				 autoclose: true,
				 startDate: new Date(min_range, current_month, current_day),
				 endDate: new Date(max_range, current_month, current_day)
			 });*/
		 	$('#co_gender2').val(0);
		 }else if(!$('#coAppImageNext').hasClass('collapsed')){
			 $('#coApplicant2').html('');
			 $("#coAppImageNext").addClass("collapsed");
		 }
	});
	jQuery(document).on("click","#coAppImageNext2", function(){
		if($('#coAppImageNext2').hasClass('collapsed') && ($('#coAppImageNext2').attr('aria-expanded')==="false")){
			$("#coAppImageNext2").addClass("collapsed");
			$('#coApp2Data').html('');
			$('#COAPPLICANT2').html('');
		 }else if(!$('#coAppImageNext2').hasClass('collapsed')){
			 $("#coAppImageNext2").removeClass("collapsed");
			$('#coApplicant2').html($('#htmlHomeLoanCoApplicant2Data').html());
			$('#co_gender2').val(0);
		 }
	});
});


jQuery(document).on("change","#co_employmentType1", function() {
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var selVal=src.options[src.selectedIndex].value;
    $('#EMPTYPECOAPP').html('');
    if(selVal!=undefined){
    	if(selVal>0){
        	var innerHtml='';
        	var co_residentType1=$('#co_residentType1').val();
        	if(selVal>0){
        		if(selVal == 1){
        			$('#EMPTYPECOAPP').html($('#htmlCoApplicant1').html());
            		if(co_residentType1==2){
            			$('#divco_retirementAgeApplicant1').after($('#htmlCoAppWorkExp').html());
            		}
        		}else if(selVal == 2 ||selVal == 3){
        			$('#EMPTYPECOAPP').html($('#htmlCoAppBusinessman').html());
        			if(co_residentType1==2){
            			$('#divco_preEMIs1').after($('#htmlCoAppWorkExp').html());
            		}
        		}else if(selVal == 4){
        			$('#EMPTYPECOAPP').html($('#htmlCoAppAgriculture').html());
        		}else if(selVal == 5){
        			$('#EMPTYPECOAPP').html($('#htmlCoAppPensioner').html());
        		}else if(selVal == 6 ||selVal == 7){
        			$('#EMPTYPECOAPP').html($('#htmlCoAppRetiredHomemaker').html());
        		}
        		apply_currency_form(document.homeloanform);
        		}else{
            	jQuery("#co_employmentType1").val(0);
        		jQuery("#co_employmentType1").trigger("change");
        		jQuery("#co_employmentType1").attr("disabled", true);
        }
        }
    }
    return false;
});

jQuery(document).on("change","#co_employmentType2", function() {
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var selVal=src.options[src.selectedIndex].value;
    $('#COEMPTYPESECAND').html('');
    if(selVal!=undefined){
    	var co_residentType2=$('#co_residentType2').val();
    	if(selVal>0){
    		if(selVal == 1){
    			$('#COEMPTYPESECAND').html($('#htmlCoApp2Salaried').html());
    			if(co_residentType2==2){
        			$('#divco_retirementAgeApplicant2').after($('#htmlCoApp2WorkExp').html());
        		}
    		}else if(selVal == 2|| selVal == 3){
    			$('#COEMPTYPESECAND').html($('#htmlCoApp2Businessman').html());
    			if(co_residentType2==2){
        			$('#divco_preEMIs2').after($('#htmlCoApp2WorkExp').html());
        		}
    		}else if(selVal == 4){
    			$('#COEMPTYPESECAND').html($('#htmlCoApp2Agriculture').html());
    		}else if(selVal == 5){
    			$('#COEMPTYPESECAND').html($('#htmlCoApp2RetiredPensioner').html());
    		}else if(selVal == 6 ||selVal == 7){
    			$('#COEMPTYPESECAND').html($('#htmlCoApp2RetiredHomemaker').html());
    		}
    		apply_currency_form(document.homeloanform);
    		 }
    }
    return false;
});

jQuery(document).on("change","#co_residentType1", function() {
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var currentId = src.parentNode.id;
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	var innerHtml='';
    	$('#CORESIDENTTYPE').html('');
    	if(selVal>0){
    		changeEmploymentType('co_employmentType1', 'co_residentType1');
    		if(selVal == 1){
    			$('#co_employmentType1').attr('disabled',false);
    			return true;
    		}else if(selVal == 2){
    			$('#co_employmentType1').attr('disabled',false);
    			$('#CORESIDENTTYPE').html($('#htmlCoAppOtherCountry').html());
    		}
    		
    		}else{
	        	$("#co_employmentType1").val(0);
	    		$("#co_employmentType1").trigger("change");
	    		$("#co_employmentType1").attr("disabled", true);
        }
    }
    return false;
});

jQuery(document).on("change","#co_residentType2", function() {
	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
    
    var currentId = src.parentNode.id;
    var selVal=src.options[src.selectedIndex].value;
    $('#COAPPOTHERCOUNTRY').html('');
    $('#RESIDENTTYPE2').html('');
    if(selVal!=undefined){
    	if(selVal>0){
    		changeEmploymentType('co_employmentType2', 'co_residentType2');
    		$('#divco_employmentType2').attr('disabled',false);
    		if(selVal == 1){
    			return true;
    		}
        	if(selVal == 2 ){
        		$('#COAPPOTHERCOUNTRY').html($('#htmlCoApp2OtherCountry').html());
        	}
    	}else{
    		$("#co_employmentType2").val(0);
    		$("#co_employmentType2").trigger("change");
    		$("#co_employmentType2").attr("disabled", true);
    	}
    }
    return false;
});

jQuery(document).on("change","#preferredLocation", function(){
	src = this;
	if(src==undefined)return false;
	var selVal=src.value;
    
    if(selVal!=undefined){
    	$('#PREFERREDLOCATIONSTATE').html('');
    	$('#PREFERREDLOCATIONCITY').html('');
    	$('#PREFERREDLOCATIONBRANCH').html('');
    	$('#PREFERREDLOCATIONDISTRICT').html('');
    	//$('#htmlPreferedState').html('');
    	if(selVal>0){
    		if(selVal == 1 || selVal==2){
    			$('#PREFERREDLOCATIONSTATE').html($('#htmlPreferredState').html());
				if(jQuery("#loanPurpose").val()==4){
					elementValue = preferredStateDataRACPC;
				}else{
					var appresidenttype =jQuery("#residentType").val();
					var co_residentType1 =jQuery("#co_residentType1").val();
					var co_residentType2 = jQuery("#co_residentType2").val();
					var loanPurpose=jQuery("#loanPurpose").val();
					if(loanType == 1 && loanPurpose!=5){
						if(appresidenttype==2 || ( co_residentType1!=undefined &&  co_residentType1==2 )|| ( co_residentType2!=undefined &&  co_residentType2==2 )){
							elementValue = preferredStateData;//preferredStateDataNRI;
						}else{
							elementValue = preferredStateData;
						}
					}else{
						elementValue = preferredStateData;
					}
				}
				populateDropdownFromJSONArrayList('preferredState', elementValue, 0);
    		}
    	}
    }
    return false;
});


var rowsToErase4CoApplicant2=0;

jQuery(document).on("change","#homeloanform #yearcompletionDate,#yeartenorExistingHomeLoan", function() { 
	var selected_year = $(this).val();
	if(selected_year==0){
		$('#monthcompletionDate').attr("disabled",true);
		$('#monthcompletionDate').addClass("disabledFields");
	}else{
		$('#monthcompletionDate').removeAttr("disabled");
		$('#monthcompletionDate').removeClass("disabledFields");
	}
	var CURRENT_MONTH = new Date().getMonth();
	var option_string = "";		
	option_string += '<option value="0">Select month</option>';
	if(current_year==selected_year){
		for(var j=CURRENT_MONTH;j<months.length;j++){
			option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
		}
	}else{
		for(var j=0;j<months.length;j++){
			option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
		}
	}
	$('#monthcompletionDate').find("option").remove().end().append(option_string);
});

jQuery(document).on("change","#yearexistingHomeLoanStartDate", function() { 
	var selected_year = $(this).val();
	if(selected_year==0){
		$(this).next().attr("disabled",true);
	}else{
		$(this).next().removeAttr("disabled");
	}
	var CURRENT_MONTH = new Date().getMonth()-1;
	var option_string = "";		
	option_string += '<option value="0">Select month</option>';
	if(current_year==selected_year){
		for(var j=0;j<CURRENT_MONTH;j++){
			option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
		}
	}else{
		for(var j=0;j<months.length;j++){
			option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
		}
	}
	$(this).next().find("option").remove().end().append(option_string);
});

jQuery(document).on("change","#yearemiStartDateOfExistingLoan, #yearemiStartDateOfExistingTopupLoan", function() { 
	var selected_year = $(this).val();
	if(selected_year==0){
		$('#monthemiStartDateOfExistingLoan').attr("disabled",true);
	}else{
		$('#monthemiStartDateOfExistingLoan').removeAttr("disabled");
	}
	var option_string = "";		
	option_string += '<option value="0">Select month</option>';
	for(var j=0;j<months.length;j++){
		option_string += '<option value="'+parseInt(j+1)+'">'+months[j]+'</option>';
	}
	$('#monthemiStartDateOfExistingLoan').find("option").remove().end().append(option_string);
});

var cityDetails=new Array();
var districtDetails=new Array();
var branchDetails=new Array();
jQuery(document).ready(function(){
	//FIRST PAGE SCRIPT
	jQuery(document).on("change","#preferredState", function() {
		cityDetails[0]="PREFERREDLOCATIONCITY";
		cityDetails[1]="preferredCity";
		cityDetails[2]="quote.loanQuotePreferredCityId";
		cityDetails[3]="City";
		
		districtDetails[0]="PREFERREDLOCATIONDISTRICT";
		branchDetails[0]="PREFERREDLOCATIONBRANCH";
		
		populateCitiesByStateId(this, cityDetails,'homeloanform',districtDetails,branchDetails);
	});
	jQuery(document).on("change","#preferredCity", function() {
		districtDetails[0]="PREFERREDLOCATIONDISTRICT";
		districtDetails[1]="preferredDistrict";
		districtDetails[2]="quote.loanQuotePreferredDistrictId";
		districtDetails[3]="District";
		
		branchDetails[0]="PREFERREDLOCATIONBRANCH";
		branchDetails[1]="preferredBranch";
		branchDetails[2]="quote.loanQuotePrferredBranchId";
		branchDetails[3]="Branch";
		branchDetails[4]=1;
		branchDetails[5]="<font style='font-size:10px;'>(<a target='_blank' href='"+BANK_FINDER_URL+"'>Locate nearest "+BANK_NAME+" branch</a>) </font>";
		populateDistrictByStateId(this, jQuery("#preferredState").val(), districtDetails, branchDetails,'homeloanform');
	});
	
	jQuery(document).on("change","#preferredDistrict", function() {
		branchDetails[0]="PREFERREDLOCATIONBRANCH";
		branchDetails[1]="preferredBranch";
		branchDetails[2]="quote.loanQuotePrferredBranchId";
		branchDetails[3]="Branch";
		branchDetails[4]=1;
		populatebranchByDistrictId(this, jQuery("#preferredState").val(), jQuery("#preferredDistrict").val(), branchDetails,'homeloancriteriaform');
	});
	
	//THIRD PAGE SCRIPT
	jQuery(document).on("change","#appStateId", function() {
		cityDetails[0]="APPLOCATIONCITY";
		cityDetails[1]="appCityId";
		cityDetails[2]="appForm.appCityId";
		cityDetails[3]="City";
		
		districtDetails[0]="APPLOCATIONDISTRICT";
		branchDetails[0]="APPLOCATIONBRANCH";
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	});

	jQuery(document).on("change","#appCityId", function() {
		districtDetails[0]="APPLOCATIONDISTRICT";
		districtDetails[1]="appDistrictId";
		districtDetails[2]="appForm.appDistrictId";
		districtDetails[3]="District";
		
		branchDetails[0]="APPLOCATIONBRANCH";
		branchDetails[1]="appBranchId";
		branchDetails[2]="appForm.appBranchId";
		branchDetails[3]="Branch";
		branchDetails[4]=0;
		
		populateDistrictByStateId(this, jQuery("#appStateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
	
	//third page property
	
	jQuery(document).on("change","#appPermanentStateId", function() {
		cityDetails[0]="APPPERMANENTLOCATIONCITY";
		cityDetails[1]="appPermanentCityId";
		cityDetails[2]="appForm.appPermanentCityId";
		cityDetails[3]="City";
		cityDetails[4]=0;
		
		districtDetails[0]="APPCOAPPLICANTLOCATIONDISTRICT";
		branchDetails[0]="APPCOAPPLICANTLOCATIONBRANCH";
		
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	}); 
	jQuery(document).on("change","#appPermanentCityId", function() {
		districtDetails[0]="APPPERMANENTLOCATIONDISTRICT";
		districtDetails[1]="appPermanentDistrictId";
		districtDetails[2]="appForm.appPermanentDistrictId";
		districtDetails[3]="District";
		districtDetails[4]=0;
		
		branchDetails[0]="APPLOCATIONBRANCH";
		branchDetails[1]="appPermanentBranchId";
		branchDetails[2]="appForm.appPermanentBranchId";
		branchDetails[3]="Branch";
		branchDetails[4]=0;
		
		populateDistrictByStateId(this, jQuery("#appPermanentStateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
	
	jQuery(document).on("change","#appPropertyStateId", function() {
		cityDetails[0]="APPPROPERTYLOCATIONCITY";
		cityDetails[1]="appPropertyCityId";
		cityDetails[2]="appForm.appPropertyCityId";
		cityDetails[3]="City";
		cityDetails[4]="1";
		
		districtDetails[0]="APPPROPERTYLOCATIONDISTRICT";
		branchDetails[0]="APPPROPERTYLOCATIONBRANCH";
		
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	}); 
	jQuery(document).on("change","#appPropertyCityId", function() {
		districtDetails[0]="APPPROPERTYLOCATIONDISTRICT";
		districtDetails[1]="appPropertyDistrictId";
		districtDetails[2]="appForm.appPropertyDistrictId";
		districtDetails[3]="District";
		districtDetails[4]="1";
		
		branchDetails[0]="APPPROPERTYLOCATIONBRANCH";
		branchDetails[1]="appPropertyBranchId";
		branchDetails[2]="appForm.appPropertyBranchId";
		branchDetails[3]="Preferred Branch";
		branchDetails[4]="0";
		
		populateDistrictByStateId(this, jQuery("#appPropertyStateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
	
	jQuery(document).on("change","#appOfficeStateId", function() {
		cityDetails[0]="APPOFFICELOCATIONCITY";
		cityDetails[1]="appOfficeCityId";
		cityDetails[2]="appForm.appOfficeCityId";
		cityDetails[3]="City";
		cityDetails[4]="1";
		
		districtDetails[0]="APPOFFICELOCATIONDISTRICT";
		branchDetails[0]="APPOFFICELOCATIONBRANCH";
		
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	}); 
	
	jQuery(document).on("change","#appOfficeCityId", function() {
		districtDetails[0]="APPOFFICELOCATIONDISTRICT";
		districtDetails[1]="appOfficeDistrictId";
		districtDetails[2]="appForm.appOfficeDistrictId";
		districtDetails[3]="District";
		districtDetails[4]="1";
		
		branchDetails[0]="APPOFFICELOCATIONBRANCH";
		branchDetails[1]="appOfficeBranchId";
		branchDetails[2]="appForm.appOfficeBranchId";
		branchDetails[3]="Preferred Branch";
		branchDetails[4]=0;
		
		populateDistrictByStateId(this, jQuery("#appOfficeStateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
	
	jQuery(document).on("change","#appCoapplicant1StateId", function() {
		cityDetails[0]="APPCOAPPLICANT1LOCATIONCITY";
		cityDetails[1]="appCoapplicant1CityId";
		cityDetails[2]="appForm.appCoapplicantCity_id_1";
		cityDetails[3]="City";
		cityDetails[4]=0;
		districtDetails[0]="APPCOAPPLICANT1LOCATIONDISTRICT";
		branchDetails[0]="APPCOAPPLICANTLOCATIONBRANCH";
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	}); 
	
	jQuery(document).on("change","#appCoapplicant1CityId", function() {
		districtDetails[0]="APPCOAPPLICANT1LOCATIONDISTRICT";
		districtDetails[1]="appCoapplicant1DistrictId";
		districtDetails[2]="appForm.appCoapplicantDistrictId1";
		districtDetails[3]="District";
		districtDetails[4]=0;
		
		branchDetails[0]="APPCOAPPLICANT1BRANCH";
		branchDetails[1]="appCoapplicant1BranchId";
		branchDetails[2]="appForm.appCoapplicant1BranchId";
		branchDetails[3]="Preferred Branch";
		branchDetails[4]=0; // for NonMandatory
		
		populateDistrictByStateId(this, jQuery("#appCoapplicant1StateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
	
	jQuery(document).on("change","#appCoapplicant2StateId", function() {
		cityDetails[0]="APPCOAPPLICANT2LOCATIONCITY";
		cityDetails[1]="appCoapplicant2CityId";
		cityDetails[2]="appForm.appCoapplicantCity_id_2";
		cityDetails[3]="City";
		cityDetails[4]=0;
		
		districtDetails[0]="APPCOAPPLICANT2LOCATIONDISTRICT";
		branchDetails[0]="APPCOAPPLICANTLOCATIONBRANCH";
		populateCitiesByStateId(this, cityDetails,'homeloancriteriaform',districtDetails,branchDetails);
	}); 
	
	jQuery(document).on("change","#appCoapplicant2CityId", function() {
		districtDetails[0]="APPCOAPPLICANT2LOCATIONDISTRICT";
		districtDetails[1]="appCoapplicant2DistrictId";
		districtDetails[2]="appForm.appCoapplicantDistrictId2";
		districtDetails[3]="District";
		districtDetails[4]=0;
		
		branchDetails[0]="APPCOAPPLICANT2BRANCH";
		branchDetails[1]="appCoapplicant2BranchId";
		branchDetails[2]="appForm.appCoapplicant2BranchId";
		branchDetails[3]="Preferred Branch";
		branchDetails[4]=0;
		
		populateDistrictByStateId(this, jQuery("#appCoapplicant2StateId").val(), districtDetails, branchDetails,'homeloancriteriaform');
	});
});

function showHideSalaryPackage(empTypeId,residenceTypeId, salaryAccountWith, salaryPkg){
	var employmentType = jQuery("#"+empTypeId).val();
	var residentType = jQuery("#"+residenceTypeId).val();
	if(employmentType==1){
		if(residentType==undefined || residentType==0 || residentType==1){
			jQuery("#div"+salaryAccountWith).show();
		}else{
			jQuery("#div"+salaryAccountWith).hide();
			jQuery("#div"+salaryPkg).hide();
			jQuery("#div"+salaryAccountWith+"No").attr('checked',true);
			jQuery("#div"+salaryAccountWith+"Yes").attr('checked',false);
		}
	}
    if(residentType == 2 ){
    	generateWorkExpElement(0);
    }else{
    	if(BANK_ID == BANK_ID_SBI){
    		var loanPurpose = jQuery("#loanPurpose").val();
    		var propertyCategory = jQuery("#propertyCategory").val();
    		if((loanPurpose==1 || loanPurpose==3 || loanPurpose==4) && propertyCategory != 3 && employmentType==1){
    			generateWorkExpElement(1);
    		}
    	} else if(BANK_ID == BANK_ID_SBBJ){
    		
    	}
    }
}


jQuery(document).on("change","#employmentType", function(){
	showEmploymentType4HomeLoan(this);
});


function showEmploymentType4HomeLoan(src){
    if(src==undefined||src.options==undefined||!src.options.length)return false;
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	$('#homeloanform #divworkExperience').remove();
    	$('#RESIDENTTYPEB').html('');
    	
    	var loanPurposeId = jQuery("#loanPurpose").val();
    	var loanCategoryId = jQuery("#propertyCategory").val();
    	var residentTypeId = jQuery("#residentType").val();
    	
    	if (selVal>0) {
        	if(residentTypeId == 1){
        		if (loanPurposeId!=null && loanPurposeId!=0 && loanPurposeId==1 && loanCategoryId !=3 && selVal == 1) {
        			$('#RESIDENTTYPEB').html($('#htmlWorkExResident1').html());
				}
        	}else if(residentTypeId==2){
        		$('#RESIDENTTYPEB').html($('#htmlWorkExResident2').html());
        	}
		}
    }
    return false;
}

jQuery(document).on("change","#co_employmentType1", function(){
	showCoEmploymentType14HomeLoan(this);
});


function showCoEmploymentType14HomeLoan(src){
    if(src==undefined||src.options==undefined||!src.options.length)return false;
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	$('#homeloanform #divworkExperience1').remove();
    	$('#RESIDENTTYPEB1').html('');
    	
    	var loanPurposeId = jQuery("#loanPurpose").val();
    	var loanCategoryId = jQuery("#propertyCategory").val();
    	var residentTypeId = jQuery("#co_countryRegion1").val();
    	
    	if (selVal>0) {
        	 if(residentTypeId==2){
        		$('#RESIDENTTYPEB1').html($('#htmlWorkExResident12').html());
        	}
		}
    }
    return false;
}

jQuery(document).on("change","#co_employmentType2", function(){
	showCoEmploymentType24HomeLoan(this);
});


function showCoEmploymentType24HomeLoan(src){
    if(src==undefined||src.options==undefined||!src.options.length)return false;
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	$('#homeloanform #divworkExperience2').remove();
    	$('#RESIDENTTYPEB2').html('');
    	
    	var loanPurposeId = jQuery("#loanPurpose").val();
    	var loanCategoryId = jQuery("#propertyCategory").val();
    	var residentTypeId = jQuery("#co_countryRegion2").val();
    	
    	if (selVal>0) {
        	 if(residentTypeId==2){
        		$('#RESIDENTTYPEB2').html($('#htmlWorkExResident22').html());
        	}
		}
    }
    return false;
}

function generateWorkExpElement(isForFlexi){
	divErase4ResidentTypeIdB = "RESIDENTTYPEB";
	if(isInSession){
		jQuery("#EMPTYPE" ).append("<div  id='"+divErase4ResidentTypeIdB+"'></div>");
	}else{
		if(isDsrPage){
			jQuery("#251").before("<div  id='"+divErase4ResidentTypeIdB+"'></div>");
		}else{
			jQuery("#251").before("<div  id='"+divErase4ResidentTypeIdB+"'></div>");
		}
	}
	elementId=['workExperience'];
	elementType=[2];
	elementValue[0]=(isForFlexi == 1?workExpForFlexiData:workExpData);
	elementMandatory=[1];
	elementName=['quote.loanQuoteWorkExperience'];
	labelName=['Work experience'];
	labelNameHindi=['&#2310;&#2346; &#2357;&#2352;&#2381;&#2340;&#2350;&#2366;&#2344; &#2350;&#2375;&#2306; &#2352;&#2361;&#2340;&#2375; &#2361;&#2376;&#2306; &#2358;&#2361;&#2352;'];
	
	var label=[];
	for (var index = 0; index < elementId.length; index++) {
		if(currentLanguage == undefined || currentLanguage=='English'){
			label=labelName[index];
		}else if(currentLanguage=='Hindi'){
			label=labelNameHindi[index];
		}
		innerHtml = generateHTMLDiv(elementId[index], label, elementMandatory[index]);
		jQuery("#"+divErase4ResidentTypeIdB ).append(innerHtml);
		if(index == 1){
			jQuery("#div"+elementId[index]).append(generateElement(elementId[index], elementName[index], elementType[index],elementValue[index],0,'','','','small-select'));        			
		}else{
			jQuery("#div"+elementId[index]).append(generateElement(elementId[index], elementName[index], elementType[index],elementValue[index],0,'','','',''));
		}
		if(elementType[index]==2){
			populateDropdownFromJSONArrayList(elementId[index], elementValue[index], 0);
		}
		
		if(jQuery("#loanPurpose").val()==3 && jQuery("#residentType").val()==1 && jQuery("#employmentType").val()==1){
			jQuery("#RESIDENTTYPEB").remove();
		}
	}
}

/*
function changeEmploymentType(empType, residenceType){
	var resiType = jQuery("#"+residenceType).val();
	var coApplicantEmploymentData3="";
	if(resiType==1 || resiType==0){
		coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'},{'key':2, 'value':'Self employed businessman'},{'key':3, 'value':'Self employed professional'},{'key':4, 'value':'Agriculturist'},{'key':5, 'value':'Retired(Pensioner)'},{'key':6, 'value':'Retired'},{'key':7, 'value':'Homemaker'}];
	}else if(resiType==2){
		coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'},{'key':2, 'value':'Self employed businessman'},{'key':3, 'value':'Self employed professional'}];
	}
	populateDropdownFromJSONArrayList(empType, coApplicantEmploymentData3, 0);
	jQuery("#"+empType).val(0).trigger("change");
	jQuery("#"+empType).attr("disable",false);
}
*/

function changeEmploymentType(empType, residenceType){
	var resiType = jQuery("#"+residenceType).val();
	
	var loanPurpose = jQuery("#loanPurpose").val();
	var loanCategoryId = jQuery("#propertyCategory").val();
	var propertyType = jQuery("#propertyType").val();

	var coApplicantEmploymentData3="";
	if((loanPurpose == "1" && loanCategoryId == "3") || (loanPurpose == "3" && loanCategoryId == "6" && propertyType == "3")){
	if(resiType==1 || resiType==0){
		coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'}];
	}else if((loanCategoryId == "3" || loanCategoryId == "6") && resiType==2){
		coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'}];
	}
	}
	else
	{

		if(resiType==1 || resiType==0){
			coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'},{'key':2, 'value':'Self employed businessman'},{'key':3, 'value':'Self employed professional'},{'key':4, 'value':'Agriculturist'},{'key':5, 'value':'Retired(Pensioner)'},{'key':6, 'value':'Retired'},{'key':7, 'value':'Homemaker'}];
		}else if(resiType==2){
			coApplicantEmploymentData3=[{'key':1, 'value':'Salaried'},{'key':2, 'value':'Self employed businessman'},{'key':3, 'value':'Self employed professional'}];
		}
	}
	populateDropdownFromJSONArrayList(empType, coApplicantEmploymentData3, 0);
	jQuery("#"+empType).val(0).trigger("change");
	jQuery("#"+empType).attr("disable",false);
}

jQuery(document).on("change","#homeloanform #yeartenureOfExisingHomeLoan", function() {
	if(jQuery("#homeloanform #yeartenureOfExisingHomeLoan option[value='0']").is(":selected")){
		jQuery('#monthtenureOfExisingHomeLoan option:first').attr('selected','selected');
		jQuery('#monthtenureOfExisingHomeLoan').attr('disabled',true);
	}else if(jQuery("#homeloanform #yeartenureOfExisingHomeLoan option[value='0']").is(":selected")){
		jQuery('#monthtenureOfExisingHomeLoan option[value="0"]').attr('disabled',true);
		jQuery('#monthtenureOfExisingHomeLoan option:first').attr('selected','selected');
		jQuery('#monthtenureOfExisingHomeLoan').removeAttr('disabled');
	}else{
		jQuery('#homeloanform #monthtenureOfExisingHomeLoan option[value="0"]').removeAttr('disabled');
		jQuery('#monthtenureOfExisingHomeLoan option:first').attr('selected','selected');
		jQuery('#monthtenureOfExisingHomeLoan').removeAttr('disabled');
	}
	
});

jQuery(document).on("change","#industryType", function(){

	src=this;
	if(src==undefined||src.options==undefined||!src.options.length)return false;
	var selVal=src.value;
	
	var loanPurpose = jQuery("#loanPurpose").val();
	var loanCategoryId = jQuery("#propertyCategory").val();
	var loanPropertyType = jQuery("#propertyType").val();
	var residentType = jQuery("#residentType").val();
	
	if(selVal==9999999 || selVal==0 ){
		showCorporateSalaryPackage(false);
	}else{
		showCorporateSalaryPackage(true);
	}
	
	var relationship = $('input[name="relationshipWithSBI"]:checked').val()
    if(relationship!=undefined && relationship!=null && relationship==2){
    	 $("#salaryAccountWithSbiYes").prop("disabled", true);
    	 $('#relationshipWithSBINo').prop("disabled", true);
    }
	if(residentType != undefined && residentType == 2){
		showCorporateSalaryPackage(false);
	}
	$('#CHECKOFFTYPE').html('');
	
	if((loanPurpose!=0 && loanPurpose!=2 && loanPurpose!=5)){
		if (loanPurpose==1 && (loanCategoryId!=0 && loanCategoryId!=3) && residentType!=2 && (selVal==20 || selVal==83)) {
			$('#CHECKOFFTYPE').html($('#htmlSalaryCheckOff').html());
		}else if (loanPurpose==3 && (loanPropertyType!=0 && loanPropertyType!=3) && residentType!=2 && (selVal==20 || selVal==83)) {
			$('#CHECKOFFTYPE').html($('#htmlSalaryCheckOff').html());
		}else if (loanPurpose==4 && residentType!=2 && (selVal==20 || selVal==83)) {
			$('#CHECKOFFTYPE').html($('#htmlSalaryCheckOff').html());
		}
	}
});

function showCorporateSalaryPackage(flag){
	divErase4SalaryPackage='';
	
	divErase4SalaryAccountWithSBI = "SALARYACCOUNTWITHSBI";
	var loanPurpose = jQuery("#loanPurpose").val();
	if(loanPurpose!=undefined && loanPurpose ==5 ){
		return false;
	}
	if(flag){
		$('#homeloanform #divsalaryAccountWithSbi').remove();
		$('#divemployerName').after($('#htmlSalaryAccountWithSbi').html());
	}else{
		$('#corpsalarypackage').html('');
		$('#homeloanform #divsalaryAccountWithSbi').remove();
	}
}

function changeAvailingLoan(){
	var loanPurpose = jQuery("#loanPurpose").val();
	var residentType = jQuery("#residentType").val();
	if(loanPurpose==undefined && residentType==undefined){
		jQuery('.availing-loan').html('Preferred location for availing loan');
		if(residentType==1 || residentType==0){
			elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence'},{'key':2, 'value':'Place of property'}];
		}else if(residentType==2){
			elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence in India'},{'key':2, 'value':'Place of property'}];
		}
	}else{
		if(loanPurpose == 5){
			jQuery('.availing-loan').html('Location for availing loan');
			elementValue=[{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Existing home loan account branch'}];
		}else if(loanPurpose == 4){
			jQuery('.availing-loan').html('Preferred location for availing loan');
			if(residentType==1 || residentType==0){
				elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence'}];
			}else if(residentType==2){
				elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence in India'}];
			}
		}else{
			jQuery('.availing-loan').html('Preferred location for availing loan');
			if(residentType==1 || residentType==0){
				elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence'},{'key':2, 'value':'Place of property'}];
			}else if(residentType==2){
				elementValue = [{'key':0, 'value':'Select preferred location'},{'key':1, 'value':'Place of residence in India'},{'key':2, 'value':'Place of property'}];
			}
		}
	}
	populateDropdownFromJSONArrayList('preferredLocation', elementValue , 0);
	jQuery("#preferredLocation").val(0).trigger("change");
}
jQuery(document).on("change","#propertyType", function() {
	jQuery("#residentType").val(0);
	jQuery("#residentType").trigger("change");
	showPerspectiveRows4PropertyType(this);
});


function showPerspectiveRows4PropertyType(src){
	if(src==undefined||src.options==undefined||!src.options.length)return false;
 
    var selVal=src.options[src.selectedIndex].value;
    if(selVal!=undefined){
    	if(selVal==2 || selVal==4){
    		$('#PROPERTYTYPEID').html($('#htmlExtCompDateTime').html());
    		}else{
			$('#PROPERTYTYPEID').html('');
		}
	}
    return false;
}

jQuery(document).on("change","#isAvaiReimbursementNo", function(){
	if(jQuery('#loanPurpose').val()==2){
		src = this; 
		if(src==undefined)return false;
		$('#ACTUALINVESTMENTMADE').html('');
	}
	
	if(jQuery('#loanPurpose').val()==1){
		if(jQuery('#propertyCategory').val()==1 || jQuery('#propertyCategory').val()==2 || jQuery('#propertyCategory').val()==4 || jQuery('#propertyCategory').val()==5 ){
			src = this; 
			if(src==undefined)return false;
			$('#ACTUALINVESTMENTMADE').html('');
			$('#divConstructionCost').html($('#htmlCostOfConstruction').html());
		}
	}
});
jQuery(document).on("click","#isAvaiReimbursementYes", function(){
	$('#divConstructionCost').html('');
	if(jQuery('#loanPurpose').val()==2){
		src = this; 
		if(src==undefined)return false;
		$('#ACTUALINVESTMENTMADE').html($('#htmlAvaiReimbursement').html());
	}
	if(jQuery('#loanPurpose').val()==1){
		if(jQuery('#propertyCategory').val()==1 || jQuery('#propertyCategory').val()==2 || jQuery('#propertyCategory').val()==4 || jQuery('#propertyCategory').val()==5 ){
			
			src = this; 
			if(src==undefined)return false;
			$('#ACTUALINVESTMENTMADE').html($('#htmlAvaiReimbursement').html());
		}
	}
	
});
jQuery(document).on("click",".read-more", function(){
	$("#checkoff").modal('toggle');
});




//new code for cibil



jQuery(document).on(
		
		"change",
		"#stateId",
		function() {
		
			cityDetails[0] = "QUOTELOCATIONDISTRICT";
			cityDetails[1] = "cityId";
			cityDetails[2] = "quote.loanQuoteCityId";
			cityDetails[3] = "City";
			districtDetails[0] = "APPLOCATIONDISTRICT";
			branchDetails[0] = "APPLOCATIONBRANCH";
			populateCitiesByStateId(this, cityDetails, "homeloanform",
					districtDetails, branchDetails)
		});
jQuery(document).on(
		"change",
		"#cityId",
		function() {
		
			districtDetails[0] = "QUOTELOCATIONDISTRICT";
			districtDetails[1] = "districtId";
			districtDetails[2] = "quote.loanQuoteDistrictId";
			districtDetails[3] = "District";
			branchDetails[0] = "";
			branchDetails[1] = "";
			branchDetails[2] = "";
			branchDetails[3] = "";
			branchDetails[4] = 0;
			populateDistrictByStateId(this, jQuery("#stateId").val(),
					districtDetails, branchDetails, "homeloanform")
		});

//end code for cibil



