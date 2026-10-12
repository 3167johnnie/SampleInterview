<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1"
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
							onchange="loadPrivacyByLocale();">
							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div id="consentHomeLoanDiv" class="privacy-consent-pop-content">Loading
						Privacy Notice...</div>
						
						
    					
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="downloadPrivacyPdfBtn"
							class="btn btn-primary" onclick="downloadPDF();"
							style="margin-right: 10px;">Download PDF</button>

						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary" disabled="disabled"
							onclick="acceptPrivacyConsent();"
							style="opacity: 0.6; cursor: not-allowed;">Accept</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>


	function canOpenPrivacyPopup() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();
		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before viewing privacy notice.");
			if ($("#infoprovide").length > 0) {
				$("#infoprovide").prop("checked", false);
			}
			if ($("#infoprovideCBS").length > 0) {
				$("#infoprovideCBS").prop("checked", false);
			}
			return false;
		}
		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before viewing privacy notice.");
			if ($("#infoprovide").length > 0) {
				$("#infoprovide").prop("checked", false);
			}
			if ($("#infoprovideCBS").length > 0) {
				$("#infoprovideCBS").prop("checked", false);
			}
			return false;
		}
		return true;
	}

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

		$
				.ajax({
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
							$("#consentHomeLoanDiv").html(
									"Privacy Notice Not Found");
						}
					},
					error : function() {
						$("#consentHomeLoanDiv").html(
								"Unable To Load Privacy Notice");
					}
				});
	}

	

	$(document).on("show.bs.modal", "#consentHomeLoan", function(e) {
		if (!canOpenPrivacyPopup()) {
			e.preventDefault();
			return false;
		}
	});
	$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("checked", false);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("checked", false);
		}
		if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
			$("#privacyLocaleDropdown").val("eng");
			loadPrivacyByLocale("eng");
		} else {
			var firstLocale = $("#privacyLocaleDropdown option:first").val();
			loadPrivacyByLocale(firstLocale);
		}
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


	

	function resetConsentScrollValidation() {
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});
		$("#consentHomeLoanDiv").off("scroll").on("scroll", function() {
			var div = $(this)[0];
			if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
				$("#acceptConsentBtn").prop("disabled", false).css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
			}
		});
		var div = $("#consentHomeLoanDiv")[0];
		if (div && div.scrollHeight <= div.clientHeight + 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	}


	function acceptPrivacyConsent() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();
		var selectedLocale = $("#privacyLocaleDropdown").val();
		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before accepting consent.");
			return false;
		}
		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before accepting consent.");
			return false;
		}
		var div = $("#consentHomeLoanDiv")[0];
		if (div && div.scrollTop + div.clientHeight < div.scrollHeight - 5) {
			alert("Please read the privacy notice till the end before accepting.");
			return false;
		}
		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");
		var selectedLoan ="HL";
		/* var ntbId = cleanMobile + cleanDob + loanTypeId; */
		var ntbId = generateNtbId(mobile, selectedLoan);
		$("#quotePrivacyConsentFlag").val("Y");
		$("#quoteNtbId").val(ntbId);
		$("#quotePrivacyLocale").val(selectedLocale);
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("disabled", false).prop("checked", true);
			$("#infoprovide").prop("disabled", true);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
			$("#infoprovideCBS").prop("disabled", true);
		}
		$("#consentHomeLoan").modal("hide");
	}
	function loadPrivacyLanguageDropdown() {
		$
				.ajax({
					url : "getPrivacyLanguageList",
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
							loadPrivacyByLocale("eng");
						} else {
							$("#privacyLocaleDropdown").html(
									"<option value='eng'>English</option>");
							loadPrivacyByLocale("eng");
						}
					},
					error : function(xhr) {
						console.log("getPrivacyLanguageList failed:",
								xhr.status, xhr.responseText);
						$("#privacyLocaleDropdown").html(
								"<option value='eng'>English</option>");
						loadPrivacyByLocale("eng");
					}
				});
	}
	

	function downloadPDF() {

		var selectedLocale = $("#privacyLocaleDropdown").val();
		var downloadBtn = $("#downloadPrivacyPdfBtn");

		if (downloadBtn.prop("disabled")) {
			return;
		}

		if (selectedLocale == null || $.trim(selectedLocale) === "") {
			alert("Please select a language before downloading.");
			return;
		}

		$
				.ajax({
					// Same relative URL pattern as your existing AJAX functions.
					// No ".action" suffix.
					url : "downloadPrivacyNoticePdf",
					type : "POST",
					dataType : "json",
					timeout : 60000,

					data : {
						privacyLocale : selectedLocale
					},

					beforeSend : function() {
						downloadBtn.prop("disabled", true).text(
								"Downloading...");
					},

					success : function(response) {

						try {
							var json = typeof response === "string" ? JSON
									.parse(response) : response;

							if (!json || json.status !== "success"
									|| json.contentType !== "application/pdf"
									|| !json.pdfBase64) {

								alert(json && json.message ? json.message
										: "Unable to generate Privacy Notice PDF.");
								return;
							}

							// Decode the PDF returned by the Java action.
							var binary = window.atob(json.pdfBase64);

							// Check the PDF signature before saving.
							if (binary.substring(0, 5) !== "%PDF-") {
								throw new Error("Invalid PDF response.");
							}

							var bytes = new Uint8Array(binary.length);

							for (var i = 0; i < binary.length; i++) {
								bytes[i] = binary.charCodeAt(i);
							}

							var blob = new Blob([ bytes ], {
								type : "application/pdf"
							});

							var downloadUrl = window.URL.createObjectURL(blob);

							try {
								var link = document.createElement("a");

								link.href = downloadUrl;
								link.download = "SBI_Privacy_Notice.pdf";
								link.style.display = "none";

								document.body.appendChild(link);

								try {
									link.click();
								} finally {
									document.body.removeChild(link);
								}

							} finally {
								window.setTimeout(function() {
									window.URL.revokeObjectURL(downloadUrl);
								}, 10000);
							}

						} catch (e) {
							// Do not log the Base64 PDF or notice content.
							console.error("Privacy PDF download failed:",
									e.message);
							alert("Unable to save Privacy Notice PDF. Please try again.");
						}
					},

					error : function(xhr, textStatus) {

						var json = xhr.responseJSON;

						// Compatibility with jQuery versions without responseJSON.
						if (!json && xhr.responseText) {
							try {
								json = JSON.parse(xhr.responseText);
							} catch (ignore) {
								// An HTML error page is not a JSON API response.
							}
						}

						console.error(
								"Privacy PDF request failed. HTTP status:",
								xhr.status, "Request status:", textStatus);

						if (json && json.message) {
							alert(json.message);

						} else if (textStatus === "timeout") {
							alert("PDF download timed out. Please try again.");

						} else if (xhr.status === 404) {
							alert("PDF download endpoint was not found. "
									+ "Please check the deployed Struts and web.xml mappings.");

						} else {
							alert("Unable to download Privacy Notice PDF. Please try again.");
						}
					},

					complete : function() {
						downloadBtn.prop("disabled", false)
								.text("Download PDF");
					}
				});
	}
</script>


