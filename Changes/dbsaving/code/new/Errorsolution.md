This error means one JavaScript block is broken / not closed properly.

Main issue is likely this old broken button still present somewhere:

onclick="savePrivacyConsent(); style="opacity: 0.6;

This breaks JS/HTML quotes and causes:

Uncaught SyntaxError: Unexpected end of input

Fix 1: ConsentPopup.jsp Accept button

Replace button fully with this:

<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity:0.6; cursor:not-allowed;">
	Accept
</button>

Do not keep savePrivacyConsent() in button.

⸻

Fix 2: Remove duplicate old click from jquery.commonFunction.js

At bottom you have:

$("#acceptConsentBtn").click(function() {
	$("#infoprovide").prop("checked", true);
	$("#infoprovideCBS").prop("checked", true);
	$("#consentHomeLoan").modal('hide');
});

Remove/comment this block because now ConsentPopup.jsp handles accept.

/*$("#acceptConsentBtn").click(function() {
	$("#infoprovide").prop("checked", true);
	$("#infoprovideCBS").prop("checked", true);
	$("#consentHomeLoan").modal('hide');
});*/

⸻

Fix 3: infoprovideCBS::undefined

This is not the main error. It only means this element does not exist on Home Loan page:

var infoprovideCBS = jQuery('#infoprovideCBS').val();

Safe change:

var infoprovideCBS = "";
if ($("#infoprovideCBS").length > 0) {
	infoprovideCBS = $("#infoprovideCBS").val();
}
console.log("infoprovideCBS::" + infoprovideCBS);

⸻

Fix 4: Check your new script closing

In HomeFirstPageSession.jsp, make sure this is complete:

$(document).on("click", "#subtn", function(){
	if($("#quotePrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}
	if($.trim($("#quoteNtbId").val()) == ""){
		alert("Invalid consent details. Please accept SBI Privacy Notice again.");
		return false;
	}
});

Most likely cause: broken Accept button quote, not backend.
