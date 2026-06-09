<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

 <div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">
					<button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" /></span>
					</button>
					<%-- <div id="consentHomeLoanDiv" class="f-otp-pop-content ">
						<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
						
					</div> --%>
					<div class ="privacy-consent-dropdown">

						<select id="privacyLocaleDropdown" class="privacy-consent-dropdown" onchange="loadPrivacyByLocale();">
							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>
					</select>
						<%-- <select id="privacyLocaleDropdown" class="privacy-consent-dropdown" onchange="loadPrivacyByLocale();">
							<c:forEach var="lang" items="${languages}">
								<option value="${lang.locale}"
									${lang.locale == 'eng' ? 'selected="selected"' : ''}>
									${lang.languageName}</option>
							</c:forEach>
						</select> --%>

						<%--  <select id="privacyLocaleDropdown" class ="privacy-consent-dropdown"
							onchange="loadPrivacyByLocale();">
							<!-- <option value="eng">English</option>
							<option value="hin">Hindi</option>
							<option value="mar">Marathi</option>-
							<option value="mal">Malayalam</option> -->
						</select>  --%>
					</div>
					<div id="consentHomeLoanDiv" class="privacy-consent-pop-content">Loading Privacy Notice...</div>
					<!-- ACCEPT BUTTON -->
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="acceptPrivacyConsent();" style="opacity: 0.6; cursor: not-allowed;">Accept</button>
<!-- 						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary" disabled="disabled"
							onclick="savePrivacyConsent();">Accept</button> -->
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	 function loadPrivacyByLocale(locale) {
		if (locale == null) {
			locale = $("#privacyLocaleDropdown").val();
		}
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		
		
		$("#infoprovide").prop("checked", false);
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("checked", false);
		}
		
		$.ajax({
			url : "getPrivacyNoticeByLocale",
			type : "POST",
			data : {
				privacyLocale : locale
			},
			success : function(response) {
				var json = JSON.parse(response);

				if (json.status == "success") {
					$("#consentHomeLoanDiv").html(json.privacyNotice);
					$("#consentHomeLoanDiv").scrollTop(0);
					resetConsentScrollValidation();
				} else {
					$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
				}
			},
			error : function() {
				$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
			}
		});
	}
	
	
	/* $(document).on("shown.bs.modal", "#consentHomeLoan", function(){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	}); */
	


/* 	$(document).on("shown.bs.modal", "#consentHomeLoan", function(){

	    var defaultLocale = $("#privacyLocaleDropdown").val();

	    if(defaultLocale == null || defaultLocale == ""){
	        defaultLocale = "eng";
	    }

	    loadPrivacyByLocale(defaultLocale);
	}); */
	
	$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
		if($("#privacyLocaleDropdown option[value='eng']").length > 0){
			$("#privacyLocaleDropdown").val("eng");
			loadPrivacyByLocale("eng");
		} else {
			var firstLocale = $("#privacyLocaleDropdown option:first").val();
			loadPrivacyByLocale(firstLocale);
		}
	}); 
	
/* 	$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
		loadPrivacyLanguageDropdown();
	}); */
	
	$(document).on("scroll", "#consentHomeLoanDiv", function() {
		var div = $(this)[0];
		if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	});

	
	
	// for checkbox 
	$(document).on("click", "#acceptConsentBtn", function() {
		if ($("#infoprovide").length > 0) {
			$("#infoprovide")
				.prop("disabled", false)
				.prop("checked", true);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS")
				.prop("disabled", false)
				.prop("checked", true);
		}
		var consentModalEl = document.getElementById("consentHomeLoan");
		var consentModal = bootstrap.Modal.getInstance(consentModalEl);
		if (consentModal != null) {
			consentModal.hide();
		} else {
			$("#consentHomeLoan").modal("hide");
		}
	});
	
	function resetConsentScrollValidation(){
		$("#acceptConsentBtn")
			.prop("disabled", true)
			.css({
				"opacity":"0.6",
				"cursor":"not-allowed"
			});

		$("#consentHomeLoanDiv").off("scroll").on("scroll", function(){
			var div = $(this);

			if(div.scrollTop() + div.innerHeight() >= this.scrollHeight - 5){
				$("#acceptConsentBtn")
					.prop("disabled", false)
					.css({
						"opacity":"1",
						"cursor":"pointer"
					});
			}
		});
	} 
	
	
	function acceptPrivacyConsent(){

		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();

		if(mobile == null || $.trim(mobile) == ""){
			alert("Please enter mobile number before accepting consent.");
			return false;
		}

		if(dob == null || $.trim(dob) == ""){
			alert("Please enter date of birth before accepting consent.");
			return false;
		}

		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");

		 console.log("cleanMobile GENERATED : ", cleanMobile);
		 console.log("cleanDob GENERATED : ", cleanDob);
		 console.log("loanTypeId  GENERATED : ", loanTypeId);
		var ntbId = cleanMobile + cleanDob + loanTypeId;
		
		 console.log("NTBID GENERATED : ", ntbId);

		$("#quotePrivacyConsentFlag").val("Y");
		$("#quoteNtbId").val(ntbId);

		if($("#infoprovide").length > 0){
			$("#infoprovide")
				.prop("disabled", false)
				.prop("checked", true);
		}

		if($("#infoprovideCBS").length > 0){
			$("#infoprovideCBS")
				.prop("disabled", false)
				.prop("checked", true);
		}

		$("#consentHomeLoan").modal("hide");
	}
	
	function loadPrivacyLanguageDropdown(){
		$.ajax({
			url : "getPrivacyLanguageList",
			type : "POST",
			success : function(response){
				var json = typeof response === "string"
					? JSON.parse(response)
					: response;
				if(json.status == "success"){
					var optionHtml = "";
					$.each(json.languageList, function(index, item){
						optionHtml += "<option value='"
							+ item.locale
							+ "'>"
							+ item.languageName
							+ "</option>";
					});
					$("#privacyLocaleDropdown").html(optionHtml);
					$("#privacyLocaleDropdown").val("eng");
					loadPrivacyByLocale("eng");
				} else {
					$("#privacyLocaleDropdown").html(
						"<option value='eng'>English</option>"
					);
					loadPrivacyByLocale("eng");
				}
			},
			error : function(xhr){
				console.log("getPrivacyLanguageList failed:", xhr.status, xhr.responseText);
				$("#privacyLocaleDropdown").html(
					"<option value='eng'>English</option>"
				);
				loadPrivacyByLocale("eng");
			}
		});
	}
	
	
</script>


