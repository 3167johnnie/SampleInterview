<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

 <div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close clo" data-bs-dismiss="modal"
						aria-bs-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" /></span>
					</button>
					<%-- <div id="consentHomeLoanDiv" class="f-otp-pop-content ">
						<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
						
					</div> --%>
					<div style="margin-bottom: 15px;">

						<select id="privacyLocaleDropdown" class="form-control"
							onchange="loadPrivacyByLocale();">
							<option value="eng">English</option>
							<option value="hin">Hindi</option>
							<option value="mar">Marathi</option>
							<option value="mal">Malayalam</option>
						</select>
					</div>
					<div id="consentHomeLoanDiv" class="f-otp-pop-content" style="max-height: 400px; overflow-y: auto;">Loading Privacy Notice...</div>
					<!-- ACCEPT BUTTON -->
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary" disabled="disabled"
							style="opacity: 0.6; cursor: not-allowed;">Accept</button>
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
		$("#consentHomeLoan").modal("hide");
	});
</script>


