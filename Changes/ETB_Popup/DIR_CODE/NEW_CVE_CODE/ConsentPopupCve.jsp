<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentCveLoan" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-bs-backdrop="static"
		data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">
					<%-- <button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" /></span>
					</button> --%>
					<%-- <div id="consentHomeLoanDiv" class="f-otp-pop-content ">
						<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
						
					</div> --%>
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close" style="margin-right: -60px !important; margin-top: -20px !important;">
                    	<span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" alt="" /></span>
                    </button>
					<div class="privacy-consent-dropdown">

						<select id="privacyLocaleDropdown"
							class="privacy-consent-dropdown"
							onchange="loadPrivacyByLocaleCve();">
							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div id="consentCveLoanDiv" class="privacy-consent-pop-content">Loading
						Privacy Notice...</div>
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary" disabled="disabled"
							onclick="acceptPrivacyConsentCve();"
							style="opacity: 0.6; cursor: not-allowed;">Accept</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>


	function canOpenPrivacyPopupCve() {
		var mobile = $("#cbsMobileNumber").val();
		
		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before viewing privacy notice.");
			if ($("#infoprovideCve").length > 0) {
				$("#infoprovideCve").prop("checked", false);
			}
			if ($("#infoprovideCBS").length > 0) {
				$("#infoprovideCBS").prop("checked", false);
			}
			return false;
		}
		return true;
	}

	function loadPrivacyByLocaleCve(locale) {
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

		$
				.ajax({
					url : "getPrivacyNoticeByLocaleCve",
					type : "POST",
					data : {
						privacyLocale : locale
					},
					success : function(response) {
						var json = JSON.parse(response);

						if (json.status == "success") {
							$("#consentCveLoanDiv").html(json.privacyNotice);
							$("#consentCveLoanDiv").scrollTop(0);
							resetConsentScrollValidationCve();
						} else {
							$("#consentCveLoanDiv").html(
									"Privacy Notice Not Found");
						}
					},
					error : function() {
						$("#consentCveLoanDiv").html0(
								"Unable To Load Privacy Notice");
					}
				});
	}

	

	$(document).on("show.bs.modal", "#consentCveLoan", function(e) {
		if (!canOpenPrivacyPopupCve()) {
			e.preventDefault();
			return false;
		}
	});
	$(document).on("shown.bs.modal", "#consentCveLoan", function() {
		$("#appPrivacyConsentFlag").val("");
		$("#appNtbId").val("");
		if ($("#infoprovideCve").length > 0) {
			$("#infoprovideCve").prop("checked", false);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("checked", false);
		}
		if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
			$("#privacyLocaleDropdown").val("eng");
			loadPrivacyByLocaleCve("eng");
		} else {
			var firstLocale = $("#privacyLocaleDropdown option:first").val();
			loadPrivacyByLocaleCve(firstLocale);
		}
	});


	$(document).on("scroll", "#consentCveLoanDiv", function() {
		var div = $(this)[0];
		if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	});


	

	function resetConsentScrollValidationCve() {
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});
		$("#consentCveLoanDiv").off("scroll").on("scroll", function() {
			var div = $(this)[0];
			if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
				$("#acceptConsentBtn").prop("disabled", false).css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
			}
		});
		var div = $("#consentCveLoanDiv")[0];
		if (div && div.scrollHeight <= div.clientHeight + 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	}


	function acceptPrivacyConsentCve() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();
		var selectedLocale = $("#privacyLocaleDropdown").val();
		/* if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before accepting consent.");
			return false;
		}
		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before accepting consent.");
			return false;
		} */
		var div = $("#consentCveLoanDiv")[0];
		if (div && div.scrollTop + div.clientHeight < div.scrollHeight - 5) {
			alert("Please read the privacy notice till the end before accepting.");
			return false;
		}
		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");
		var ntbId = cleanMobile + cleanDob + loanTypeId;
		$("#appPrivacyConsentFlag").val("Y");
		$("#appNtbId").val(ntbId);
		$("#appPrivacyLocale").val(selectedLocale);
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("disabled", false).prop("checked", true);
			$("#infoprovide").prop("disabled", true);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
			$("#infoprovideCBS").prop("disabled", true);
		}
		$("#consentCveLoan").modal("hide");
	}
	function loadPrivacyLanguageDropdown() {
		$
				.ajax({
					url : "getPrivacyLanguageListCve",
					type : "POST",
					success : function(response) {
						var json = typeof response === "string" ? JSON.parse(response) : response;
						if (json.status == "success") {
							var optionHtml = "";
							$.each(json.languageList,
											function(index, item) {
												optionHtml += "<option value='" + item.locale+ "'>"+ item.languageName + "</option>";
											});
							$("#privacyLocaleDropdown").html(optionHtml);
							$("#privacyLocaleDropdown").val("eng");
							loadPrivacyByLocaleCve("eng");
						} else {
							$("#privacyLocaleDropdown").html(
									"<option value='eng'>English</option>");
							loadPrivacyByLocaleCve("eng");
						}
					},
					error : function(xhr) {
						console.log("getPrivacyLanguageList failed:",
								xhr.status, xhr.responseText);
						$("#privacyLocaleDropdown").html(
								"<option value='eng'>English</option>");
						loadPrivacyByLocaleCve("eng");
					}
				});
	}
</script>
