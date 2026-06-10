Below are minimum changes on your existing working code.

1. Remove close button from HTML

Remove/comment this block:

<button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close">
	<span aria-hidden="true"> <img
		src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" /></span>
</button>

⸻

2. Make modal non-closable from outside click / ESC

Change this:

<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel">

To this:

<div class="modal fade otp-box"
	id="consentHomeLoan"
	tabindex="-1"
	role="dialog"
	aria-labelledby="myModalLabel"
	data-bs-backdrop="static"
	data-bs-keyboard="false">

⸻

3. Replace Accept button HTML

Replace:

<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="acceptPrivacyConsent();" style="opacity: 0.6; cursor: not-allowed;">Accept</button>

With:

<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity: 0.6; cursor: not-allowed;">
	Accept
</button>

⸻

4. Add this function before loadPrivacyByLocale()

function canOpenPrivacyPopup(){
	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();
	if(mobile == null || $.trim(mobile) == ""){
		alert("Please enter mobile number before viewing privacy notice.");
		if($("#infoprovide").length > 0){
			$("#infoprovide").prop("checked", false);
		}
		if($("#infoprovideCBS").length > 0){
			$("#infoprovideCBS").prop("checked", false);
		}
		return false;
	}
	if(dob == null || $.trim(dob) == ""){
		alert("Please enter date of birth before viewing privacy notice.");
		if($("#infoprovide").length > 0){
			$("#infoprovide").prop("checked", false);
		}
		if($("#infoprovideCBS").length > 0){
			$("#infoprovideCBS").prop("checked", false);
		}
		return false;
	}
	return true;
}

⸻

5. Replace modal open event

Replace your current:

$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	if($("#privacyLocaleDropdown option[value='eng']").length > 0){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	} else {
		var firstLocale = $("#privacyLocaleDropdown option:first").val();
		loadPrivacyByLocale(firstLocale);
	}
});

With this:

$(document).on("show.bs.modal", "#consentHomeLoan", function(e){
	if(!canOpenPrivacyPopup()){
		e.preventDefault();
		return false;
	}
});
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	$("#quotePrivacyConsentFlag").val("");
	$("#quoteNtbId").val("");
	if($("#infoprovide").length > 0){
		$("#infoprovide").prop("checked", false);
	}
	if($("#infoprovideCBS").length > 0){
		$("#infoprovideCBS").prop("checked", false);
	}
	if($("#privacyLocaleDropdown option[value='eng']").length > 0){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	} else {
		var firstLocale = $("#privacyLocaleDropdown option:first").val();
		loadPrivacyByLocale(firstLocale);
	}
});

⸻

6. Replace resetConsentScrollValidation()

function resetConsentScrollValidation(){
	$("#acceptConsentBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.6",
			"cursor":"not-allowed"
		});
	$("#consentHomeLoanDiv").off("scroll").on("scroll", function(){
		var div = $(this)[0];
		if(div.scrollTop + div.clientHeight >= div.scrollHeight - 5){
			$("#acceptConsentBtn")
				.prop("disabled", false)
				.css({
					"opacity":"1",
					"cursor":"pointer"
				});
		}
	});
	var div = $("#consentHomeLoanDiv")[0];
	if(div && div.scrollHeight <= div.clientHeight + 5){
		$("#acceptConsentBtn")
			.prop("disabled", false)
			.css({
				"opacity":"1",
				"cursor":"pointer"
			});
	}
}

⸻

7. Replace acceptPrivacyConsent()

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
	var div = $("#consentHomeLoanDiv")[0];
	if(div && div.scrollTop + div.clientHeight < div.scrollHeight - 5){
		alert("Please read the privacy notice till the end before accepting.");
		return false;
	}
	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = $.trim(dob).replace(/\D/g, "");
	var ntbId = cleanMobile + cleanDob + loanTypeId;
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

⸻

8. Remove duplicate click handler

Delete this block because onclick="acceptPrivacyConsent();" already handles everything:

$(document).on("click", "#acceptConsentBtn", function() {
	...
});

⸻

Final behavior:

Mobile + DOB empty → popup will not open.
Popup opened → close button removed, outside click disabled.
User must scroll till end → only then Accept enables.
Accept clicked → checkbox checked, hidden fields set, popup closes.
