Your ETB requirement is simpler than HomeLoan/AutoLoan:

* Checkbox remains checked.
* User cannot uncheck it.
* Clicking the checkbox area opens the popup.
* Popup loads the language dropdown.
* Selecting a language loads the privacy notice.
* No Accept button.
* No scroll validation.
* No NTB ID.
* No privacy flag.
* No database save.
* Existing submitConsent() remains unchanged.

Use ETB-specific IDs and methods so there is no conflict with HomeLoan or AutoLoan.

Final flow

ETB consent page
→ user clicks checked checkbox/label
→ openPopups("consentPopupETB", "1")
→ ConsentPopupETB.jsp modal opens
→ getEtbPrivacyLanguageList
→ CommonLoanAction.getEtbPrivacyLanguageList()
→ language dropdown populated
→ getEtbPrivacyNoticeByLocale
→ CommonLoanAction.getEtbPrivacyNoticeByLocale()
→ notice loaded from RUPEEPOWER_OCAS_T_13703

⸻

1. Update the ETB consent page

At the top of your page, currently this include is commented:

<%-- <s:include value="/appNew/common/ConsentPopupETB.jsp"></s:include> --%>

Replace it with the active include:

<s:include value="/appNew/common/ConsentPopupETB.jsp"></s:include>

This is essential. Without this include, the browser does not have:

id="consentPopupETB"

and openPopups() will fail.

⸻

1.1 Replace the Essential Purpose checkbox block

Find:

<a href="javascript:void(0);" onclick="javascript:openPopups('consentPopupETB','1');">
	<input type="checkbox" id="essentialPurpose" checked disabled>
</a>

Replace with:

<span class="etb-essential-purpose-trigger"
	onclick="openEtbPrivacyPopup();"
	onkeydown="handleEtbPrivacyKeydown(event);"
	role="button"
	tabindex="0"
	title="Click to view SBI Privacy Notice">
	<input type="checkbox"
		id="essentialPurposeCheckbox"
		checked="checked"
		disabled="disabled"
		aria-label="Essential purpose selected">
	<span class="etb-privacy-view-text">
		View Privacy Notice
	</span>
</span>

Why this is needed:

A disabled checkbox generally does not receive click events. Therefore, the outer <span> receives the click while the checkbox stays permanently checked and disabled.

⸻

1.2 Add this JavaScript to the ETB consent page

Add near the bottom of the JSP:

<script type="text/javascript">
	function openEtbPrivacyPopup() {
		// Always keep the Essential Purpose checkbox selected.
		$("#essentialPurposeCheckbox")
			.prop("checked", true)
			.prop("disabled", true);
		openPopups("consentPopupETB", "1");
	}
	function handleEtbPrivacyKeydown(event) {
		if (event.key === "Enter" || event.key === " ") {
			event.preventDefault();
			openEtbPrivacyPopup();
		}
	}
</script>

⸻

1.3 Optional CSS for clickable checkbox area

Add to your CSS:

.etb-essential-purpose-trigger {
	display: inline-flex;
	align-items: center;
	gap: 8px;
	cursor: pointer;
}
.etb-essential-purpose-trigger input[type="checkbox"] {
	cursor: pointer;
}
.etb-privacy-view-text {
	color: #0648a0;
	text-decoration: underline;
	font-size: 12px;
}
.etb-essential-purpose-trigger:focus {
	outline: 2px solid #0648a0;
	outline-offset: 3px;
}

⸻

2. Replace ConsentPopupETB.jsp

Your current ETB popup contains HomeLoan IDs such as:

consentHomeLoanDiv
privacyLocaleDropdown
acceptConsentBtn
loadPrivacyByLocale()
acceptPrivacyConsent()

Remove that entire file content and use this ETB-specific version.

File:

WebContent/appNew/common/ConsentPopupETB.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="consentPopupETBHTML">
	<div class="modal fade otp-box"
		id="consentPopupETB"
		tabindex="-1"
		role="dialog"
		aria-labelledby="consentPopupETBTitle"
		aria-hidden="true"
		data-bs-backdrop="static"
		data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="privacy-modal-body">
					<button type="button"
						class="close clo"
						data-bs-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">
							<img
								src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"
								alt="Close" />
						</span>
					</button>
					<h4 id="consentPopupETBTitle">
						SBI Privacy Notice
					</h4>
					<div class="privacy-consent-dropdown">
						<select id="etbPrivacyLocaleDropdown"
							class="privacy-consent-dropdown"
							onchange="loadEtbPrivacyNotice();"
							aria-label="Select privacy notice language">
							<option value="">
								Loading languages...
							</option>
						</select>
					</div>
					<div id="etbPrivacyNoticeDiv"
						class="privacy-consent-pop-content">
						Loading Privacy Notice...
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	/*
	 * Load language list whenever the ETB modal is fully opened.
	 */
	$(document).on("shown.bs.modal", "#consentPopupETB", function() {
		$("#essentialPurposeCheckbox")
			.prop("checked", true)
			.prop("disabled", true);
		loadEtbPrivacyLanguageDropdown();
	});
	/*
	 * Also ensure the checkbox cannot be changed after modal closes.
	 */
	$(document).on("hidden.bs.modal", "#consentPopupETB", function() {
		$("#essentialPurposeCheckbox")
			.prop("checked", true)
			.prop("disabled", true);
	});
	/*
	 * Fetch active language list.
	 */
	function loadEtbPrivacyLanguageDropdown() {
		$("#etbPrivacyLocaleDropdown").html(
			"<option value=''>Loading languages...</option>"
		);
		$("#etbPrivacyLocaleDropdown").prop("disabled", true);
		$("#etbPrivacyNoticeDiv").html(
			"Loading Privacy Notice..."
		);
		$.ajax({
			url : "getEtbPrivacyLanguageList",
			type : "POST",
			dataType : "json",
			success : function(json) {
				var optionHtml = "";
				if (json != null
						&& json.status === "success"
						&& json.languageList != null
						&& json.languageList.length > 0) {
					$.each(json.languageList, function(index, item) {
						var selected = item.locale === "eng"
							? " selected='selected'"
							: "";
						optionHtml +=
							"<option value='" + escapeEtbHtml(item.locale) + "'" +
							selected + ">" +
							escapeEtbHtml(item.languageName) +
							"</option>";
					});
					$("#etbPrivacyLocaleDropdown")
						.html(optionHtml)
						.prop("disabled", false);
					if ($("#etbPrivacyLocaleDropdown option[value='eng']").length > 0) {
						$("#etbPrivacyLocaleDropdown").val("eng");
					} else {
						$("#etbPrivacyLocaleDropdown")
							.val($("#etbPrivacyLocaleDropdown option:first").val());
					}
					loadEtbPrivacyNotice();
				} else {
					$("#etbPrivacyLocaleDropdown")
						.html("<option value='eng'>English</option>")
						.val("eng")
						.prop("disabled", false);
					loadEtbPrivacyNotice();
				}
			},
			error : function(xhr) {
				console.log(
					"getEtbPrivacyLanguageList failed:",
					xhr.status,
					xhr.responseText
				);
				$("#etbPrivacyLocaleDropdown")
					.html("<option value='eng'>English</option>")
					.val("eng")
					.prop("disabled", false);
				loadEtbPrivacyNotice();
			}
		});
	}
	/*
	 * Fetch privacy notice for selected language.
	 */
	function loadEtbPrivacyNotice() {
		var selectedLocale =
			$("#etbPrivacyLocaleDropdown").val();
		if (selectedLocale == null
				|| $.trim(selectedLocale) === "") {
			selectedLocale = "eng";
		}
		$("#etbPrivacyNoticeDiv").html(
			"Loading Privacy Notice..."
		);
		$.ajax({
			url : "getEtbPrivacyNoticeByLocale",
			type : "POST",
			dataType : "json",
			data : {
				privacyLocale : selectedLocale
			},
			success : function(json) {
				if (json != null
						&& json.status === "success"
						&& json.privacyNotice != null) {
					$("#etbPrivacyNoticeDiv")
						.html(json.privacyNotice)
						.scrollTop(0);
				} else {
					$("#etbPrivacyNoticeDiv").html(
						"Privacy Notice Not Found"
					);
				}
			},
			error : function(xhr) {
				console.log(
					"getEtbPrivacyNoticeByLocale failed:",
					xhr.status,
					xhr.responseText
				);
				$("#etbPrivacyNoticeDiv").html(
					"Unable To Load Privacy Notice"
				);
			}
		});
	}
	/*
	 * Escape dropdown text returned from server.
	 * Privacy notice itself is HTML and is intentionally inserted using .html().
	 */
	function escapeEtbHtml(value) {
		if (value == null) {
			return "";
		}
		return $("<div>")
			.text(value)
			.html();
	}
</script>

There is deliberately no:

Accept button
mobile validation
DOB validation
NTB ID generation
quotePrivacyConsentFlag
quoteNtbId
quotePrivacyLocale
save AJAX

⸻

3. Update CommonLoanAction.java

Your ETB consent page already submits through CommonLoanAction.submitConsent(). For viewing the notice, add two separate read-only methods to the same action class.

3.1 Add imports

Add:

import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

Your class already has these imports:

import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.struts2.result.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

Therefore, no additional Java imports are required beyond the two entity imports.

⸻

3.2 Add field

Near your other private fields, add:

private String privacyLocale;

No languages field is required because the dropdown is loaded through AJAX.

⸻

3.3 Add getEtbPrivacyLanguageList()

Add this method before submitConsent():

public StreamResult getEtbPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray languageArray = new JSONArray();
	try {
		List<MasterLanguage> languageList =
				commonService.getAllActiveLanguages();
		if (languageList != null && !languageList.isEmpty()) {
			for (MasterLanguage language : languageList) {
				if (language == null) {
					continue;
				}
				if (!ValidatorUtil.isValid(language.getLannguageCode())
						|| !ValidatorUtil.isValid(language.getLanguageName())) {
					continue;
				}
				JSONObject languageJson = new JSONObject();
				languageJson.put(
					"locale",
					language.getLannguageCode()
				);
				languageJson.put(
					"languageName",
					language.getLanguageName()
				);
				languageArray.put(languageJson);
			}
			if (languageArray.length() > 0) {
				json.put("status", "success");
			} else {
				json.put("status", "fail");
			}
		} else {
			json.put("status", "fail");
		}
		json.put("languageList", languageArray);
	} catch (Exception e) {
		logger.info(
			"Exception in CommonLoanAction.getEtbPrivacyLanguageList",
			e
		);
		try {
			json.put("status", "fail");
			json.put("languageList", languageArray);
		} catch (JSONException jsonException) {
			logger.info(
				"JSONException in getEtbPrivacyLanguageList",
				jsonException
			);
		}
	}
	return new StreamResult(
		new ByteArrayInputStream(
			json.toString().getBytes()
		)
	);
}

⸻

3.4 Add getEtbPrivacyNoticeByLocale()

Add immediately after the language-list method:

public StreamResult getEtbPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();
	try {
		if (!ValidatorUtil.isValid(privacyLocale)) {
			privacyLocale = "eng";
		}
		privacyLocale = privacyLocale.trim();
		PrivacyRequestResponse privacyRequestResponse =
				commonService.getPrivacyByLocale(privacyLocale);
		if (privacyRequestResponse != null
				&& ValidatorUtil.isValid(
					privacyRequestResponse.getPrivacyNotice()
				)) {
			json.put("status", "success");
			json.put(
				"privacyNotice",
				privacyRequestResponse.getPrivacyNotice()
			);
		} else {
			json.put("status", "fail");
			json.put(
				"privacyNotice",
				"Privacy Notice Not Found"
			);
		}
	} catch (Exception e) {
		logger.info(
			"Exception in CommonLoanAction.getEtbPrivacyNoticeByLocale",
			e
		);
		try {
			json.put("status", "fail");
			json.put(
				"privacyNotice",
				"Unable To Load Privacy Notice"
			);
		} catch (JSONException jsonException) {
			logger.info(
				"JSONException in getEtbPrivacyNoticeByLocale",
				jsonException
			);
		}
	}
	return new StreamResult(
		new ByteArrayInputStream(
			json.toString().getBytes()
		)
	);
}

⸻

3.5 Add getter/setter

Near the bottom of CommonLoanAction.java, add:

public String getPrivacyLocale() {
	return privacyLocale;
}
public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

Struts uses this setter to bind:

privacyLocale=eng

from the AJAX request to the Java field.

⸻

4. Add Struts mappings

Add these mappings to the Struts XML file where commonLoanAction is configured.

<action name="getEtbPrivacyLanguageList"
	class="commonLoanAction"
	method="getEtbPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
		<param name="inputName">inputStream</param>
	</result>
</action>
<action name="getEtbPrivacyNoticeByLocale"
	class="commonLoanAction"
	method="getEtbPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
		<param name="inputName">inputStream</param>
	</result>
</action>

Since your existing methods directly return:

new StreamResult(new ByteArrayInputStream(...))

your project may not require:

<param name="inputName">inputStream</param>

To match your existing HomeLoan mapping style, this is sufficient:

<action name="getEtbPrivacyLanguageList"
	class="commonLoanAction"
	method="getEtbPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
<action name="getEtbPrivacyNoticeByLocale"
	class="commonLoanAction"
	method="getEtbPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

Use the style already working in your application.

⸻

5. Update web.xml

Your authentication/security filter must allow the two new URLs.

Find the filter mapping that already contains URLs such as:

<url-pattern>/getPrivacyNoticeByLocale</url-pattern>
<url-pattern>/getPrivacyLanguageList</url-pattern>

Add:

<url-pattern>/getEtbPrivacyLanguageList</url-pattern>
<url-pattern>/getEtbPrivacyNoticeByLocale</url-pattern>

Example:

<filter-mapping>
	<filter-name>yourExistingFilterName</filter-name>
	<!-- Existing mappings -->
	<url-pattern>/getEtbPrivacyLanguageList</url-pattern>
	<url-pattern>/getEtbPrivacyNoticeByLocale</url-pattern>
	<dispatcher>FORWARD</dispatcher>
	<dispatcher>REQUEST</dispatcher>
</filter-mapping>

⸻

6. CommonService.java

The following methods should already exist from your HomeLoan privacy changes:

public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	return privacyRequestResponseDao.getPrivacyByLocale(locale);
}
public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}

If they already exist, do not add them again.

⸻

7. applicationContext.xml

These beans should already exist because HomeLoan privacy is working:

<bean id="privacyRequestResponseDao"
	class="com.mintstreet.consent.dao.PrivacyRequestResponseDao">
	<property name="entityManagerFactory" ref="entityManagerFactory" />
</bean>
<bean id="masterLanguageDao"
	class="com.mintstreet.consent.dao.MasterLanguageDao">
	<property name="entityManagerFactory" ref="entityManagerFactory" />
</bean>

Inside commonService:

<property name="privacyRequestResponseDao"
	ref="privacyRequestResponseDao"/>
<property name="masterLanguageDao"
	ref="masterLanguageDao"/>

No new DAO or table is required.

⸻

8. Make openPopups() safe

Your common function currently throws a Bootstrap error when an ID is missing. Replace it with:

function openPopups(openForLoan, openForProduct) {
	var popupElement = document.getElementById(openForLoan);
	if (popupElement == null) {
		console.error(
			"openPopups: modal element not found for id:",
			openForLoan
		);
		alert("Privacy Notice popup is currently unavailable.");
		return false;
	}
	var popupHtmlId = "#" + openForLoan + "HTML";
	if ($(popupHtmlId).length > 0
			&& typeof $.fn.mCustomScrollbar === "function") {
		$(popupHtmlId).mCustomScrollbar({
			theme : "rounded",
			scrollInertia : 5
		});
	}
	var existingModal =
		bootstrap.Modal.getInstance(popupElement);
	var popupModal = existingModal;
	if (popupModal == null) {
		popupModal = new bootstrap.Modal(
			popupElement,
			{
				backdrop : "static",
				keyboard : false
			}
		);
	}
	popupModal.show();
	return false;
}

For this to work, the two IDs must match exactly:

<div id="consentPopupETBHTML">
	<div id="consentPopupETB" class="modal fade">

and the caller must be:

openPopups("consentPopupETB", "1");

⸻

9. Remove old ETB popup code

Remove all these functions from the old ConsentPopupETB.jsp:

canOpenPrivacyPopup()
loadPrivacyByLocale()
resetConsentScrollValidation()
acceptPrivacyConsent()
loadPrivacyLanguageDropdown()

Also remove references to:

#consentHomeLoan
#consentHomeLoanDiv
#privacyLocaleDropdown
#acceptConsentBtn
#quotePrivacyConsentFlag
#quoteNtbId
#quotePrivacyLocale
#infoprovide
#infoprovideCBS

The ETB popup must use only:

#consentPopupETB
#consentPopupETBHTML
#etbPrivacyLocaleDropdown
#etbPrivacyNoticeDiv
loadEtbPrivacyLanguageDropdown()
loadEtbPrivacyNotice()

⸻

10. No changes required to submitConsent()

Your existing:

public StreamResult submitConsent()

continues to submit the purposes selected from the consent form.

The privacy popup is only informational. It does not call:

submitConsent()

and does not change:

essentialPurpose
selectedPurposes
allPurposes
otherPurpose

The existing hidden field remains:

<input type="hidden"
	name="essentialPurpose"
	value="<s:property value='code'/>|<s:property value='version'/>" />

Therefore, your essential-purpose data is still submitted normally while the checkbox stays permanently checked.
