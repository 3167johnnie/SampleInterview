Make only these minimum changes in your existing ConsentPopup.jsp.

⸻

Change 1: reset checkbox when popup content loads

Inside your loadPrivacyByLocale(locale) function, find this:

$("#acceptConsentBtn").prop("disabled", true).css({
	"opacity" : "0.6",
	"cursor" : "not-allowed"
});

Immediately after it, add this:

$("#infoprovide").prop("checked", false);
$("#infoprovideCBS").prop("checked", false);

So it becomes:

$("#acceptConsentBtn").prop("disabled", true).css({
	"opacity" : "0.6",
	"cursor" : "not-allowed"
});
$("#infoprovide").prop("checked", false);
$("#infoprovideCBS").prop("checked", false);

⸻

Change 2: auto-check checkbox after Accept

Find this code at bottom:

$(document).on("click", "#acceptConsentBtn", function() {
	$("#consentHomeLoan").modal("hide");
});

Replace with this:

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

⸻

Change 3: make checkbox disabled in HomeFirstPageSession.jsp

Find:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">

Replace with:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">

⸻

Final updated script section

Your final script should look like this:

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
	$(document).on("scroll", "#consentHomeLoanDiv", function() {
		var div = $(this)[0];
		if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	});
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

Now checkbox will check only after:

Read More → popup opens → scroll bottom → Accept click
