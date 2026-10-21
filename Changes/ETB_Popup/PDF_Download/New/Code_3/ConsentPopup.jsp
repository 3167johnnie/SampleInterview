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
							onchange="loadPrivacyByLocale(this.value);">
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
							class="btn btn-primary" disabled="disabled"
							onclick="downloadPDF();" style="margin-right: 10px;">
							Download PDF</button>
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
var privacyNoticeRequestId = 0;

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

	    var popup = $("#consentHomeLoan");
	    var dropdown = popup.find("[id='privacyLocaleDropdown']");
	    var noticeDiv = popup.find("[id='consentHomeLoanDiv']");
	    var downloadBtn = popup.find("[id='downloadPrivacyPdfBtn']");
	    if (locale == null) {
	        locale = dropdown.val();
	    }
	    var requestId = ++privacyNoticeRequestId;
	    noticeDiv.removeData("loadedLocale");
	    downloadBtn.prop("disabled", true);
	    $("#acceptConsentBtn").prop("disabled", true).css({
	        "opacity" : "0.6",
	        "cursor" : "not-allowed"
	    });
	    $("#infoprovide").prop("checked", false);

	    if ($("#infoprovideCBS").length > 0) {
	        $("#infoprovideCBS").prop("checked", false);
	    }

	    if (!locale) {
	        noticeDiv.text("Please select a privacy notice language.");
	        return;
	    }

	    dropdown.val(locale);
	    noticeDiv.text("Loading Privacy Notice...");

	    $
	        .ajax({
	            url : "getPrivacyNoticeByLocale",
	            type : "POST",
	            dataType : "json",

	            data : {
	                privacyLocale : locale
	            },

	            success : function(response) {
	                // An older request must not overwrite the latest selection.
	                if (requestId !== privacyNoticeRequestId ||
	                        dropdown.val() !== locale) {
	                    return;
	                }
	                // Handles either JSON text or an already parsed object.
	                var json = typeof response === "string"
	                        ? JSON.parse(response)
	                        : response;

	                if (json &&
	                        json.status === "success" &&
	                        typeof json.privacyNotice === "string" &&
	                        $.trim(json.privacyNotice) !== "") {
	                    noticeDiv
	                        .html(json.privacyNotice)
	                        .data("loadedLocale", locale)
	                        .scrollTop(0);
	                    resetConsentScrollValidation();
	                    downloadBtn.prop("disabled", false);
	                } else {
	                    noticeDiv.text("Privacy Notice Not Found");
	                }
	            },

	            error : function(xhr) {
	                if (requestId !== privacyNoticeRequestId) {
	                    return;
	                }
	                noticeDiv.text("Unable To Load Privacy Notice");
	                console.error(
	                    "Privacy notice request failed:",
	                    xhr.status,
	                    "Locale:",
	                    locale
	                );
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
		var dropdown = $("#consentHomeLoan").find("[id='privacyLocaleDropdown']");
		if (dropdown.find("option").length === 0) {
    	loadPrivacyLanguageDropdown();
		} else {
		 var defaultLocale = dropdown.find("option[value='eng']").length > 0
            ? "eng"
            : dropdown.find("option:first").val();
	    dropdown.val(defaultLocale);
 	   loadPrivacyByLocale(defaultLocale);
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
							var dropdown = $("#consentHomeLoan")
					        .find("[id='privacyLocaleDropdown']");

					dropdown.html(optionHtml);
					if (dropdown.find("option").length === 0) {
					    dropdown.append(
					        $("<option>").val("eng").text("English")
					    );
					}

					var defaultLocale = dropdown.find("option[value='eng']").length > 0  ? "eng" : dropdown.find("option:first").val();
					dropdown.val(defaultLocale);
					loadPrivacyByLocale(defaultLocale);
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


	    var popup = $("#consentHomeLoan");
	    var dropdown = popup.find("[id='privacyLocaleDropdown']");
	    var noticeDiv = popup.find("[id='consentHomeLoanDiv']");
	    var downloadBtn = popup.find("[id='downloadPrivacyPdfBtn']");

	    var selectedLocale = dropdown.val();
	    var loadedLocale = noticeDiv.data("loadedLocale");

	    if (downloadBtn.prop("disabled")) {
	        return;
	    }

	    if (!selectedLocale || selectedLocale !== loadedLocale) {
	        alert("Please wait until the selected language's notice loads.");
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
					    downloadBtn.prop("disabled", true).text("Downloading...");
					    dropdown.prop("disabled", true);
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
					    dropdown.prop("disabled", false);
					    var loadedLocale = noticeDiv.data("loadedLocale");
					    downloadBtn
					        .prop("disabled",
					              !loadedLocale || loadedLocale !== dropdown.val())
					        .text("Download PDF");
					}
				});
	}
</script>
