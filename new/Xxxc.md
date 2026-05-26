Make only these changes in your code.

1. REMOVE this block from inside loadPrivacyByLocale()

Delete this full block:

$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	$("#privacyLocaleDropdown").val("eng");
	loadPrivacyByLocale("eng");
});

It is wrongly placed inside the function.

⸻

2. ADD this after loadPrivacyByLocale() function ends

Add this after this closing bracket:

}

of loadPrivacyByLocale().

$(document).ready(function(){
	$("#privacyLocaleDropdown").val("eng");
	loadPrivacyByLocale("eng");
});

⸻

Final corrected <script> code

Replace your full <script> block with this:

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
		$("#infoprovideCBS").prop("checked", false);
		
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
				} else {
					$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
				}
			},
			error : function() {
				$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
			}
		});
	}
	/*
	 * Load default English when ConsentPopup.jsp loads
	 */
	$(document).ready(function(){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	});
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
</script>

Now English loads as soon as ConsentPopup.jsp loads.
