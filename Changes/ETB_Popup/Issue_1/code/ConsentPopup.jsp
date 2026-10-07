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
						 <!-- ✅ Download button -->
    					
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="downloadPrivacyPdfBtn"
							class="btn btn-primary" onclick="downloadPDF();">
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
						$("#consentHomeLoanDiv").html0(
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

		// 1. Read the currently selected language.
		var selectedLocale = $("#privacyLocaleDropdown").val() || "eng";
		var downloadBtn = $("#downloadPrivacyPdfBtn");

		// 2. Prevent repeated clicks while downloading.
		if (downloadBtn.prop("disabled")) {
			return;
		}

		downloadBtn.prop("disabled", true).text("Downloading...");

		function resetDownloadButton() {
			downloadBtn.prop("disabled", false).text("Download PDF");
		}

		// 3. Call the common Java PDF action.
		var xhr = new XMLHttpRequest();

		xhr.open("POST", '<s:url action="downloadPrivacyNoticePdf"/>', true);

		xhr.responseType = "blob";
		xhr.timeout = 60000;

		xhr.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");

		// 4. Download only a successful PDF response.
		xhr.onload = function() {

			try {
				var contentType = xhr.getResponseHeader("Content-Type") || "";

				if (xhr.status !== 200
						|| contentType.toLowerCase().indexOf("application/pdf") === -1
						|| !xhr.response || xhr.response.size === 0) {

					alert("Unable to download Privacy Notice PDF. Please try again.");
					return;
				}

				var downloadUrl = window.URL.createObjectURL(xhr.response);
				var link = document.createElement("a");

				link.href = downloadUrl;
				link.download = "SBI_Privacy_Notice.pdf";

				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);

				// Release the temporary browser URL after download starts.
				window.setTimeout(function() {
					window.URL.revokeObjectURL(downloadUrl);
				}, 10000);

			} catch (e) {
				console.error("Privacy PDF download failed:", e);
				alert("Unable to download Privacy Notice PDF. Please try again.");

			} finally {
				resetDownloadButton();
			}
		};

		// 5. Handle connection failures.
		xhr.onerror = function() {
			resetDownloadButton();
			alert("Unable to download Privacy Notice PDF. Please try again.");
		};

		// 6. Handle a request timeout.
		xhr.ontimeout = function() {
			resetDownloadButton();
			alert("Privacy Notice PDF download timed out. Please try again.");
		};

		xhr.onabort = function() {
			resetDownloadButton();
		};

		// 7. Send the selected locale to CommonLoanAction.
		try {
			xhr.send("privacyLocale=" + encodeURIComponent(selectedLocale));

		} catch (e) {
			resetDownloadButton();
			console.error("Unable to start Privacy PDF download:", e);
			alert("Unable to download Privacy Notice PDF. Please try again.");
		}
	}
</script>


