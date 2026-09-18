<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="consentETBHTML">
	<div class="modal fade otp-box" id="consentPopupETB" tabindex="-1"
		aria-labelledby="consentPopupETBLabel" aria-hidden="true"
		data-bs-backdrop="static" data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">
					<!-- CLOSE BUTTON -->
					<button type="button" class="close clo" data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" />
						</span>
					</button>
					<!-- LANGUAGE DROPDOWN -->
					<div class="privacy-consent-dropdown">
						<select id="privacyETBDropdown" class="privacy-consent-dropdown"
							onchange="loadETBPrivacyByLocale(this.value);">
							<option value="eng">English</option>
						</select>
					</div>
					<!-- PRIVACY NOTICE CONTENT -->
					<div id="consentETBContent" class="privacy-consent-pop-content">
						Loading Privacy Notice...</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>

function openETBPrivacyPopup(checkbox) {
    // Checkbox must always remain checked
    if (checkbox) {
        checkbox.checked = true;
    }
    var popupElement =
        document.getElementById("consentPopupETB");
    if (!popupElement) {
        console.error(
            "consentPopupETB element not found."
        );
        return false;
    }
    var popupInstance =
        bootstrap.Modal.getOrCreateInstance(
            popupElement,
            {
                backdrop: "static",
                keyboard: false
            }
        );
    popupInstance.show();
    loadETBPrivacyLanguages();
    return false;
}
function loadETBPrivacyLanguages() {
    $.ajax({
        url: "getPrivacyLanguageList",
        type: "POST",
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                var optionHtml = "";
                $.each(
                    json.languageList,
                    function(index, item) {
                        optionHtml +=
                            "<option value='" +
                            item.locale +
                            "'>" +
                            item.languageName +
                            "</option>";
                    }
                );
                $("#privacyETBDropdown")
                    .html(optionHtml);
                if (
                    $("#privacyETBDropdown option[value='eng']")
                    .length > 0
                ) {
                    $("#privacyETBDropdown")
                        .val("eng");
                    loadETBPrivacyByLocale("eng");
                } else {
                    var firstLocale =
                        $("#privacyETBDropdown option:first")
                        .val();
                    loadETBPrivacyByLocale(
                        firstLocale
                    );
                }
            } else {
                $("#privacyETBDropdown").html(
                    "<option value='eng'>English</option>"
                );
                loadETBPrivacyByLocale("eng");
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyLanguageList failed:",
                xhr.status,
                xhr.responseText
            );
            $("#privacyETBDropdown").html(
                "<option value='eng'>English</option>"
            );
            loadETBPrivacyByLocale("eng");
        }
    });
}
function loadETBPrivacyByLocale(locale) {
    if (!locale) {
        locale =
            $("#privacyETBDropdown").val();
    }
    $("#consentETBContent").html(
        "Loading Privacy Notice..."
    );
    $.ajax({
        url: "getPrivacyNoticeByLocale",
        type: "POST",
        data: {
            privacyLocale: locale
        },
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                $("#consentETBContent").html(
                    json.privacyNotice
                );
                $("#consentETBContent")
                    .scrollTop(0);
            } else {
                $("#consentETBContent").html(
                    "Privacy Notice Not Found"
                );
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyNoticeByLocale failed:",
                xhr.status,
                xhr.responseText
            );
            $("#consentETBContent").html(
                "Unable To Load Privacy Notice"
            );
        }
    });
}
</script>
