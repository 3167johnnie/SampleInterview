<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!--
	The outer ID must follow this naming rule:
	openPopups("consentPopupETB", "1")
	looks for:
	1. consentPopupETB
	2. consentPopupETBHTML
-->
<div id="consentPopupETBHTML">
	<div class="modal fade otp-box" id="consentPopupETB" tabindex="-1"
		role="dialog" aria-labelledby="consentPopupETBTitle"
		aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">
					<!-- Close button -->
					<button type="button" class="close clo" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"
							alt="Close" />
						</span>
					</button>
					<h4 id="consentPopupETBTitle">SBI Privacy Notice</h4>
					<!-- Language dropdown -->
					<div class="privacy-consent-dropdown">
						<select id="etbPrivacyLocaleDropdown"
							class="privacy-consent-dropdown"
							aria-label="Select Privacy Notice language"
							onchange="loadEtbPrivacyNotice();">
							<option value="">Loading languages...</option>
						</select>
					</div>
					<!-- Privacy Notice content -->
					<div id="etbPrivacyNoticeDiv" class="privacy-consent-pop-content">
						Loading Privacy Notice...</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	/*
	 * Runs after the ETB modal has completely opened.
	 */
	$(document).on(
			"shown.bs.modal",
			"#consentPopupETB",
			function() {
				/*
				 * Keep Essential Purpose checked.
				 */
				$("#essentialPurposeCheckbox").prop("checked", true).prop(
						"disabled", true);
				/*
				 * Load dropdown and default privacy notice.
				 */
				loadEtbPrivacyLanguageDropdown();
			});
	/*
	 * Runs after the ETB modal closes.
	 */
	$(document).on(
			"hidden.bs.modal",
			"#consentPopupETB",
			function() {
				/*
				 * Ensure checkbox remains checked.
				 */
				$("#essentialPurposeCheckbox").prop("checked", true).prop(
						"disabled", true);
			});
	/*
	 * Loads all active languages from the server.
	 */
	function loadEtbPrivacyLanguageDropdown() {
		$("#etbPrivacyLocaleDropdown").html(
				"<option value=''>Loading languages...</option>").prop(
				"disabled", true);
		$("#etbPrivacyNoticeDiv").html("Loading Privacy Notice...");
		$
				.ajax({
					url : "getEtbPrivacyLanguageList",
					type : "POST",
					dataType : "json",
					success : function(json) {
						var optionHtml = "";
						if (json != null && json.status === "success"
								&& json.languageList != null
								&& json.languageList.length > 0) {
							$
									.each(
											json.languageList,
											function(index, language) {
												var locale = language.locale == null ? ""
														: language.locale;
												var languageName = language.languageName == null ? ""
														: language.languageName;
												var selectedAttribute = locale === "eng" ? " selected='selected'"
														: "";
												optionHtml += "<option value='"
														+ escapeEtbDropdownValue(locale)
														+ "'"
														+ selectedAttribute
														+ ">"
														+ escapeEtbDropdownValue(languageName)
														+ "</option>";
											});
							$("#etbPrivacyLocaleDropdown").html(optionHtml)
									.prop("disabled", false);
							/*
							 * English is default when available.
							 */
							if ($("#etbPrivacyLocaleDropdown option[value='eng']").length > 0) {
								$("#etbPrivacyLocaleDropdown").val("eng");
							} else {
								var firstLocale = $(
										"#etbPrivacyLocaleDropdown option:first")
										.val();
								$("#etbPrivacyLocaleDropdown").val(firstLocale);
							}
							loadEtbPrivacyNotice();
						} else {
							setEtbEnglishFallback();
						}
					},
					error : function(xhr) {
						console.log("getEtbPrivacyLanguageList failed:",
								xhr.status, xhr.responseText);
						setEtbEnglishFallback();
					}
				});
	}
	/*
	 * Provides English if the language-list request fails.
	 */
	function setEtbEnglishFallback() {
		$("#etbPrivacyLocaleDropdown").html(
				"<option value='eng'>English</option>").val("eng").prop(
				"disabled", false);
		loadEtbPrivacyNotice();
	}
	/*
	 * Loads the privacy notice for the selected locale.
	 */
	function loadEtbPrivacyNotice() {
		var selectedLocale = $("#etbPrivacyLocaleDropdown").val();
		if (selectedLocale == null || $.trim(selectedLocale) === "") {
			selectedLocale = "eng";
		}
		$("#etbPrivacyNoticeDiv").html("Loading Privacy Notice...");
		$
				.ajax({
					url : "getEtbPrivacyNoticeByLocale",
					type : "POST",
					dataType : "json",
					data : {
						privacyLocale : selectedLocale
					},
					success : function(json) {
						if (json != null && json.status === "success"
								&& json.privacyNotice != null
								&& $.trim(json.privacyNotice) !== "") {
							/*
							 * Privacy notice is stored as HTML,
							 * therefore .html() is intentionally used.
							 */
							$("#etbPrivacyNoticeDiv").html(json.privacyNotice)
									.scrollTop(0);
						} else {
							$("#etbPrivacyNoticeDiv").html(
									"Privacy Notice Not Found");
						}
					},
					error : function(xhr) {
						console.log("getEtbPrivacyNoticeByLocale failed:",
								xhr.status, xhr.responseText);
						$("#etbPrivacyNoticeDiv").html(
								"Unable To Load Privacy Notice");
					}
				});
	}
	/*
	 * Escapes server values used inside dropdown HTML.
	 */
	function escapeEtbDropdownValue(value) {
		if (value == null) {
			return "";
		}
		return $("<div>").text(value).html();
	}
</script>
