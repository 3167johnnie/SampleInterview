<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
$(document).on("keydown", function (e) {
    if (e.which === 8 && !$(e.target).is("input:not([readonly]):not([type=radio]):not([type=checkbox]), textarea, [contentEditable], [contentEditable=true]")) {
        e.preventDefault();
    }
});
jQuery(document).ready(function(){
	application_form_validate_rules();
	setTimeout(function() {
		callServiceForPDFName();
	}, 10000);
    $("#check1, #check2, #check3, #check4").click(function() {
        var currentlySelected = $(this).val();
        $("#docPickupCheck1").hide();
        $("#docPickupCheck2").hide();
        $("#docPickupCheck3").hide();
        $("#docPickupCheck4").hide();
        $("#docPickupCheck" + currentlySelected).show();
        if(currentlySelected == 4){
        	$("#collectionDateAndTime").hide();
        }else{
        	$("#collectionDateAndTime").show();
        }
       /*  
        $("#content-4").mCustomScrollbar({
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
		$("body").on("focus","input:not(#appDocPickupDate),select",function(){
			 $("#content-4").mCustomScrollbar('scrollTo',$(this).prev());
		}) */
    });
	var currentHr = getCurrentHours();
	var currentDay = current_day;
	if(currentHr >= 17){
		currentDay = current_day + 1;
	}
	isApplicationStatusPage=true;
	
	$("#appDocPickupDate").datepicker({
		debug: true,
		widgeParent: 'lidate_of_birth',
		orientation : "top left",
		autoclose: true,			
		startDate: new Date(current_year, current_month, currentDay),
		endDate: new Date(current_year, current_month, current_day+14),
	}).on('changeDate', function(ev){
		$('#appDocPickupDate').removeClass('error');
		$('#appDocPickupDate').next().remove();
		onchangeOfdateOfPickUp();
	});
	
	$('#check<s:property value="%{#appForm.appDocPickupCheck==null || #appForm.appDocPickupCheck==4?1:#appForm.appDocPickupCheck}"/>').trigger("click");
});
	var placeholder = $('#appStatusLoanAmount').html();
	if(placeholder!=undefined){
		var strip_commas = placeholder.replace(/,/g, "");
		if(strip_commas>0){
		  strip_commas = Number(strip_commas).toPrecision();
		  strip_commas = strip_commas.replace(".0","");
		  $('#appStatusLoanAmount').text(M.moneyFormat(strip_commas));
		}
	}

	function onchangeOfdateOfPickUp(){
		$('.datepicker.datepicker-dropdown.dropdown-menu.datepicker-orient-left.datepicker-orient-top').appendTo("#appDocPickupDate");
		addPickupTimeRule();
		var pickupDate = $("#appDocPickupDate").val();
		var currentSelectedTime = $("#appDocPickupTime").val();
		var today = getCurrenDate();
		var currentHours = getCurrentHours();
		var parsHoursInInt = parseInt(currentHours);
		if (today == pickupDate) {
			if (parsHoursInInt < 17) {
				for (var i = 9; i <= parsHoursInInt; i++) {
					var value = "";
					if (i < 10) {
						value = "0" + i + ":00";
					} else {
						value = i + ":00";
					}
					$("#appDocPickupTime option[value='" + value + "']").remove();
					if (i < 10) {
						value = "0" + i + ":30";
					} else {
						value = i + ":30";
					}
					$("#appDocPickupTime option[value='" + value + "']").remove();
				}
			}
		} else {
			for (var i = 9; i <= 19; i++) {
				var value = "";
				if (i < 10) {
					value = "0" + i + ":00";
				} else {
					value = i + ":00";
				}
				$("#appDocPickupTime option[value='" + value + "']").remove();
				if (i < 10) {
					value = "0" + i + ":30";
				} else {
					value = i + ":30";
				}
				$("#appDocPickupTime option[value='" + value + "']").remove();
			}
			for (var i = 9; i <= 11; i++) {
				var value = "";
				if (i < 10) {
					value = "0" + i + ":00";
				} else {
					value = i + ":00";
				}
				$("#appDocPickupTime").append("<option value="+value+">" + value + " am</option>");
				if (i < 10) {
					value = "0" + i + ":30";
				} else {
					value = i + ":30";
				}
				$("#appDocPickupTime").append("<option value="+value+">" + value + " am</option>");
			}
			$("#appDocPickupTime").append("<option value='12:00'>12:00 pm</option>");
			$("#appDocPickupTime").append("<option value='12:30'>12:30 pm</option>");
			for (var i = 13; i <= 18; i++) {
				var value = "";
				if (i < 10) {
					value = "0" + i + ":00";
				} else {
					value = i + ":00";
				}
				var key = i-12;	
				var shownValue ="0" +key + ":00 pm";
				$("#appDocPickupTime").append("<option value="+value+">" + shownValue + "</option>");
				if (i < 10) {
					value = "0" + i + ":30";
				} else {
					value = i + ":30";
				}
					 shownValue ="0" +key + ":30 pm";
				$("#appDocPickupTime").append("<option value="+value+">" + shownValue + "</option>");
			}
			$("#appDocPickupTime").val(currentSelectedTime);
		}
	}
	

	function getCurrentHours() {
		var currentDate = new Date();
		var currentHour = currentDate.getHours();
		return currentHour;
	}
	function getCurrentMinutes() {
		var currentDate = new Date();
		var currentMinutes = currentDate.getMinutes();
		return currentMinutes;
	}
	function getCurrenDate() {
		var currentDate = new Date();
		var currentDay = currentDate.getDate();
		var currentMonth = currentDate.getMonth() + 1;
		var currentYear = currentDate.getFullYear();
		if (currentDay < 10) {
			currentDay = "0" + currentDay;
		}
		if (currentMonth < 10) {
			currentMonth = "0" + currentMonth;
		}
		var today = currentDay + "-" + currentMonth + "-" + currentYear;
		return today;
	}
	if(isDsrPage==null || isDsrPage==undefined){
		isDsrPage = false;
	}
	var uploadProdctName = "";
	 if(loanTypeId==1){
	 	uploadProdctName = "HL/";
	 } else if(loanTypeId==2) {
	 	uploadProdctName = "AL/";
	 } else if(loanTypeId==3) {
	 	uploadProdctName = "PL/";
	 } else if(loanTypeId==4) {
	 	uploadProdctName = "EL/";
	 } else if(loanTypeId==15) {
		 uploadProdctName = "AGL/"; 
	 } else if(loanTypeId==17) {
		 uploadProdctName = "SCC/";
	 }

	application_form_schedulePickupAddress_validate();
	var isOfferPage = false;
 	<s:if test="%{requestIndex==-1 || requestIndex==0}">
 		callInitHomeLoan = true;
 	</s:if>
	var isEligibleForEdvantage = false;
	<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
		<s:if test="%{quote.loanQuoteCountryOfStudyId == 2 && appForm.appEducationLoanId == 7}">
			isEligibleForEdvantage = true;
		</s:if>
		<s:else>
			isEligibleForEdvantage = false;
		</s:else>
	</s:if>
	var statuspage=false;
	<s:if test="%{applicationStatusPage>0}">
		statuspage=true;	
	</s:if>
	initUpload('doc_1','1', "1","thumb1","docUrl_1", uploadProdctName, isDsrPage);
	initUpload('doc_2','2', "2","thumb2","docUrl_2", uploadProdctName, isDsrPage);
	initUpload('doc_3','3', "3","thumb3","docUrl_3", uploadProdctName, isDsrPage);
	initUpload('doc_4','4', "4","thumb4","docUrl_4", uploadProdctName, isDsrPage);
	initUpload('doc_5','5', "5","thumb5","docUrl_5", uploadProdctName, isDsrPage);
</script>
<script type="text/javascript">
	setTimeout(function(){
		$(".m-hm-prd").fadeOut('slow');
	},3000 );
</script>
