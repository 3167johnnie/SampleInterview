Below is the complete ETB Privacy Notice implementation based on your existing consent page, ConsentPopupETB.jsp, CommonLoanAction.java, Bootstrap 5 openPopups() function, and current submitConsent() flow.

This implementation does only the following:

* Essential-purpose checkbox remains checked.
* User cannot uncheck it.
* Clicking the checkbox area or “View Privacy Notice” opens the popup.
* Popup loads active languages.
* English is selected by default.
* Changing language loads the corresponding privacy notice.
* Popup can be closed.
* No Accept button.
* No mobile/DOB validation.
* No NTB ID generation.
* No privacy-consent database update.
* No changes to your existing submitConsent() processing.

⸻

Final request flow

Consent collection JSP
    ↓
Click checked Essential Purpose checkbox area
    ↓
openPopups("consentPopupETB", "1")
    ↓
ConsentPopupETB.jsp opens
    ↓
AJAX: getEtbPrivacyLanguageList
    ↓
CommonLoanAction.getEtbPrivacyLanguageList()
    ↓
Language dropdown populated
    ↓
AJAX: getEtbPrivacyNoticeByLocale
    ↓
CommonLoanAction.getEtbPrivacyNoticeByLocale()
    ↓
CommonService → DAO → privacy notice table
    ↓
Content displayed in ETB popup

⸻

Step 1: Enable the popup include

In your consent collection JSP, you currently have:

<%-- <s:include value="/appNew/common/ConsentPopupETB.jsp"></s:include> --%>

Replace it with:

<s:include value="/appNew/common/ConsentPopupETB.jsp"></s:include>

This include is mandatory.

When the server renders your consent page, it inserts the complete popup HTML into the same response page. Without this include, the browser cannot find:

id="consentPopupETB"

and Bootstrap throws an error while creating the modal.

⸻

Step 2: Change the Essential Purpose checkbox

In your existing consent page, find this block:

<s:if test="%{#stat.first}">
	<td style="border: 1px solid rgb(140 140 140)" rowspan="<s:property value='rowSpanCount' />">
		<a href="javascript:void(0);" onclick="javascript:openPopups('consentPopupETB','1');">
			<input type="checkbox" id="essentialPurpose" checked disabled>
		</a>
	</td>
</s:if>

Replace it completely with:

<s:if test="%{#stat.first}">
	<td style="border: 1px solid rgb(140 140 140)"
		rowspan="<s:property value='rowSpanCount' />">
		<span id="etbPrivacyPopupTrigger"
			class="etb-privacy-popup-trigger"
			role="button"
			tabindex="0"
			title="Click to view SBI Privacy Notice"
			onclick="openEtbPrivacyPopup();"
			onkeydown="openEtbPrivacyPopupByKeyboard(event);">
			<input type="checkbox"
				id="essentialPurposeCheckbox"
				checked="checked"
				disabled="disabled"
				aria-label="Essential purpose selected" />
			<span class="etb-privacy-popup-text">
				View Privacy Notice
			</span>
		</span>
	</td>
</s:if>

Why the existing code should be replaced

Your current code places a disabled checkbox inside an anchor:

<a onclick="...">
	<input disabled>
</a>

A disabled form control may prevent or inconsistently propagate click events. The corrected version makes the outer <span> clickable while keeping the checkbox permanently checked and disabled.

⸻

Step 3: Add popup-opening JavaScript to the consent page

Add this near the bottom of the consent collection JSP, before the final closing section or after the table:

<script type="text/javascript">
	function openEtbPrivacyPopup() {
		/*
		 * Essential Purpose must always remain selected.
		 */
		$("#essentialPurposeCheckbox")
			.prop("checked", true)
			.prop("disabled", true);
		/*
		 * consentPopupETB must exactly match the modal ID
		 * in ConsentPopupETB.jsp.
		 */
		openPopups("consentPopupETB", "1");
		return false;
	}
	function openEtbPrivacyPopupByKeyboard(event) {
		if (event.key === "Enter" || event.key === " ") {
			event.preventDefault();
			openEtbPrivacyPopup();
		}
	}
</script>

⸻

Step 4: Add optional CSS

Add this to your existing global CSS file:

.etb-privacy-popup-trigger {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	gap: 8px;
	cursor: pointer;
}
.etb-privacy-popup-trigger input[type="checkbox"] {
	margin: 0;
	pointer-events: none;
}
.etb-privacy-popup-text {
	color: #0648a0;
	font-size: 12px;
	font-weight: 600;
	text-decoration: underline;
	cursor: pointer;
}
.etb-privacy-popup-trigger:focus {
	outline: 2px solid #0648a0;
	outline-offset: 3px;
}

The checkbox uses:

pointer-events: none;

so the click always reaches the outer clickable element.

⸻

Step 5: Replace ConsentPopupETB.jsp

Your existing ETB popup still contains HomeLoan-specific IDs and methods:

consentHomeLoan
consentHomeLoanDiv
privacyLocaleDropdown
acceptConsentBtn
acceptPrivacyConsent()
quotePrivacyConsentFlag
quoteNtbId

Remove the complete existing content of:

WebContent/appNew/common/ConsentPopupETB.jsp

Replace it with the following code.

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
					<!-- Close button -->
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
					<!-- Language dropdown -->
					<div class="privacy-consent-dropdown">
						<select id="etbPrivacyLocaleDropdown"
							class="privacy-consent-dropdown"
							aria-label="Select Privacy Notice language"
							onchange="loadEtbPrivacyNotice();">
							<option value="">
								Loading languages...
							</option>
						</select>
					</div>
					<!-- Privacy Notice content -->
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
	 * Runs after the ETB modal has completely opened.
	 */
	$(document).on(
		"shown.bs.modal",
		"#consentPopupETB",
		function() {
			/*
			 * Keep Essential Purpose checked.
			 */
			$("#essentialPurposeCheckbox")
				.prop("checked", true)
				.prop("disabled", true);
			/*
			 * Load dropdown and default privacy notice.
			 */
			loadEtbPrivacyLanguageDropdown();
		}
	);
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
			$("#essentialPurposeCheckbox")
				.prop("checked", true)
				.prop("disabled", true);
		}
	);
	/*
	 * Loads all active languages from the server.
	 */
	function loadEtbPrivacyLanguageDropdown() {
		$("#etbPrivacyLocaleDropdown")
			.html("<option value=''>Loading languages...</option>")
			.prop("disabled", true);
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
					$.each(
						json.languageList,
						function(index, language) {
							var locale =
								language.locale == null
									? ""
									: language.locale;
							var languageName =
								language.languageName == null
									? ""
									: language.languageName;
							var selectedAttribute =
								locale === "eng"
									? " selected='selected'"
									: "";
							optionHtml +=
								"<option value='"
								+ escapeEtbDropdownValue(locale)
								+ "'"
								+ selectedAttribute
								+ ">"
								+ escapeEtbDropdownValue(languageName)
								+ "</option>";
						}
					);
					$("#etbPrivacyLocaleDropdown")
						.html(optionHtml)
						.prop("disabled", false);
					/*
					 * English is default when available.
					 */
					if ($("#etbPrivacyLocaleDropdown option[value='eng']").length > 0) {
						$("#etbPrivacyLocaleDropdown").val("eng");
					} else {
						var firstLocale =
							$("#etbPrivacyLocaleDropdown option:first").val();
						$("#etbPrivacyLocaleDropdown").val(firstLocale);
					}
					loadEtbPrivacyNotice();
				} else {
					setEtbEnglishFallback();
				}
			},
			error : function(xhr) {
				console.log(
					"getEtbPrivacyLanguageList failed:",
					xhr.status,
					xhr.responseText
				);
				setEtbEnglishFallback();
			}
		});
	}
	/*
	 * Provides English if the language-list request fails.
	 */
	function setEtbEnglishFallback() {
		$("#etbPrivacyLocaleDropdown")
			.html("<option value='eng'>English</option>")
			.val("eng")
			.prop("disabled", false);
		loadEtbPrivacyNotice();
	}
	/*
	 * Loads the privacy notice for the selected locale.
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
						&& json.privacyNotice != null
						&& $.trim(json.privacyNotice) !== "") {
					/*
					 * Privacy notice is stored as HTML,
					 * therefore .html() is intentionally used.
					 */
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
	 * Escapes server values used inside dropdown HTML.
	 */
	function escapeEtbDropdownValue(value) {
		if (value == null) {
			return "";
		}
		return $("<div>")
			.text(value)
			.html();
	}
</script>

⸻

Step 6: Important popup ID naming

Your openPopups() method contains:

$("#"+openForLoan+"HTML")

and:

document.getElementById(openForLoan)

Therefore, this call:

openPopups("consentPopupETB", "1");

requires both these elements:

<div id="consentPopupETBHTML">
	<div id="consentPopupETB"
		class="modal fade">

Do not use:

<div id="termAndConditionHTML">

for this ETB popup.

The correct outer ID is:

id="consentPopupETBHTML"

⸻

Step 7: Update openPopups()

Your current method can throw this error when the modal ID is missing:

Cannot read properties of undefined (reading 'backdrop')

Replace your current openPopups() method with:

function openPopups(openForLoan, openForProduct) {
	var popupElement =
		document.getElementById(openForLoan);
	/*
	 * Stop before calling Bootstrap when modal does not exist.
	 */
	if (popupElement == null) {
		console.error(
			"openPopups: popup element not found. ID:",
			openForLoan
		);
		alert("Privacy Notice popup is currently unavailable.");
		return false;
	}
	/*
	 * Apply custom scrollbar only when its wrapper exists
	 * and the plugin is available.
	 */
	var popupWrapperSelector =
		"#" + openForLoan + "HTML";
	if ($(popupWrapperSelector).length > 0
			&& $.fn.mCustomScrollbar != null) {
		$(popupWrapperSelector).mCustomScrollbar({
			theme : "rounded",
			scrollInertia : 5
		});
	}
	/*
	 * Reuse an existing Bootstrap modal instance.
	 */
	var popupModal =
		bootstrap.Modal.getInstance(popupElement);
	if (popupModal == null) {
		popupModal =
			new bootstrap.Modal(
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

⸻

Step 8: Update CommonLoanAction.java

You already have:

import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.struts2.result.StreamResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

Therefore, these do not need to be added again.

8.1 Add entity imports

Add after your existing consent import:

import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

For example:

import com.mintstreet.consent.bo.ConsentRequestConsent;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

⸻

8.2 Add privacyLocale field

Find this section:

private String name;
private String mobile;
private String token;

Add below it:

private String privacyLocale;

Result:

private String name;
private String mobile;
private String token;
private String privacyLocale;

This field receives the AJAX request parameter:

privacyLocale=eng

through its setter.

⸻

Step 9: Add the language-list method

In CommonLoanAction.java, add the following method immediately before:

public StreamResult submitConsent() throws JSONException {

Add:

public StreamResult getEtbPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray languageArray = new JSONArray();
	try {
		List<MasterLanguage> languageList =
				commonService.getAllActiveLanguages();
		if (languageList != null
				&& !languageList.isEmpty()) {
			for (MasterLanguage language : languageList) {
				if (language == null) {
					continue;
				}
				String locale =
					language.getLannguageCode();
				String languageName =
					language.getLanguageName();
				if (!ValidatorUtil.isValid(locale)
						|| !ValidatorUtil.isValid(languageName)) {
					continue;
				}
				JSONObject languageJson =
					new JSONObject();
				languageJson.put(
					"locale",
					locale
				);
				languageJson.put(
					"languageName",
					languageName
				);
				languageArray.put(languageJson);
			}
		}
		if (languageArray.length() > 0) {
			json.put("status", "success");
		} else {
			json.put("status", "fail");
		}
		json.put(
			"languageList",
			languageArray
		);
	} catch (Exception e) {
		logger.info(
			"Exception in CommonLoanAction.getEtbPrivacyLanguageList",
			e
		);
		try {
			json.put("status", "fail");
			json.put(
				"languageList",
				languageArray
			);
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

Step 10: Add the privacy-notice method

Immediately after getEtbPrivacyLanguageList(), add:

public StreamResult getEtbPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();
	try {
		/*
		 * Use English when locale is absent.
		 */
		if (!ValidatorUtil.isValid(privacyLocale)) {
			privacyLocale = "eng";
		}
		privacyLocale =
			privacyLocale.trim();
		logger.info(
			"Loading ETB Privacy Notice for locale : "
			+ privacyLocale
		);
		PrivacyRequestResponse privacyObject =
				commonService.getPrivacyByLocale(
					privacyLocale
				);
		if (privacyObject != null
				&& ValidatorUtil.isValid(
					privacyObject.getPrivacyNotice()
				)) {
			json.put(
				"status",
				"success"
			);
			json.put(
				"privacyNotice",
				privacyObject.getPrivacyNotice()
			);
		} else {
			json.put(
				"status",
				"fail"
			);
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
			json.put(
				"status",
				"fail"
			);
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

Step 11: Add getter and setter

At the bottom of CommonLoanAction.java, before the final class closing brace, add:

public String getPrivacyLocale() {
	return privacyLocale;
}
public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

Without the setter, Struts will not bind:

data : {
	privacyLocale : selectedLocale
}

to the Java field.

⸻

Step 12: Keep submitConsent() unchanged

Do not modify your existing:

public StreamResult submitConsent() throws JSONException

It continues to process:

essentialPurpose
selectedPurposes
allPurposes
otherPurpose

Your existing hidden Essential Purpose value remains:

<input type="hidden"
	name="essentialPurpose"
	value="<s:property value='code'/>|<s:property value='version'/>" />

The checked display checkbox is only visual. The hidden input is what sends the required purpose code/version.

⸻

Step 13: Add Struts mappings

Open the Struts XML file that already contains the mapping for:

submitConsent

Add the following mappings in the same package.

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

The exact bean name must match your existing Spring/Struts configuration.

If your existing submitConsent action is defined like this:

<action name="submitConsent"
	class="commonLoanAction"
	method="submitConsent">

then use:

class="commonLoanAction"

for the two new actions.

⸻

Step 14: Add filter mappings in web.xml

Find the filter mapping where you already added:

<url-pattern>/getPrivacyNoticeByLocale</url-pattern>
<url-pattern>/getPrivacyLanguageList</url-pattern>

Add:

<url-pattern>/getEtbPrivacyLanguageList</url-pattern>
<url-pattern>/getEtbPrivacyNoticeByLocale</url-pattern>

Example:

<filter-mapping>
	<filter-name>yourExistingFilter</filter-name>
	<!-- Existing URL mappings -->
	<url-pattern>/getEtbPrivacyLanguageList</url-pattern>
	<url-pattern>/getEtbPrivacyNoticeByLocale</url-pattern>
	<dispatcher>FORWARD</dispatcher>
	<dispatcher>REQUEST</dispatcher>
</filter-mapping>

Do not create a second identical filter mapping unless your project configuration requires it. Add the URL patterns to the existing relevant mapping.

⸻

Step 15: Verify CommonService.java

These methods should already exist from the working HomeLoan privacy implementation:

public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	return privacyRequestResponseDao.getPrivacyByLocale(locale);
}
public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}

If they already exist, make no change.

If they do not exist, add them to CommonService.java:

public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	return privacyRequestResponseDao.getPrivacyByLocale(locale);
}
public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}

⸻

Step 16: Verify applicationContext.xml

The HomeLoan implementation already requires these beans:

<bean id="privacyRequestResponseDao"
	class="com.mintstreet.consent.dao.PrivacyRequestResponseDao">
	<property name="entityManagerFactory"
		ref="entityManagerFactory" />
</bean>
<bean id="masterLanguageDao"
	class="com.mintstreet.consent.dao.MasterLanguageDao">
	<property name="entityManagerFactory"
		ref="entityManagerFactory" />
</bean>

Inside the commonService bean:

<property name="privacyRequestResponseDao"
	ref="privacyRequestResponseDao" />
<property name="masterLanguageDao"
	ref="masterLanguageDao" />

Because HomeLoan privacy loading already works, these probably exist. Do not duplicate them.

⸻

Step 17: Remove obsolete code from old ConsentPopupETB.jsp

The replacement popup must not contain any of the following:

canOpenPrivacyPopup()
acceptPrivacyConsent()
resetConsentScrollValidation()

Remove references to:

#consentHomeLoan
#consentHomeLoanDiv
#privacyLocaleDropdown
#acceptConsentBtn
#quotePrivacyConsentFlag
#quoteNtbId
#quotePrivacyLocale
#infoprovide
#infoprovideCBS

The final ETB popup should use only:

consentPopupETBHTML
consentPopupETB
etbPrivacyLocaleDropdown
etbPrivacyNoticeDiv
loadEtbPrivacyLanguageDropdown()
loadEtbPrivacyNotice()
setEtbEnglishFallback()

⸻

Step 18: Browser debugging checklist

Check 1: Confirm popup exists

Open browser console and run:

document.getElementById("consentPopupETB")

Expected result:

<div id="consentPopupETB" class="modal fade otp-box">...</div>

It must not return:

null

Check 2: Confirm outer wrapper exists

document.getElementById("consentPopupETBHTML")

It must not return null.

Check 3: Test modal directly

openPopups("consentPopupETB", "1")

The popup should open.

Check 4: Verify language endpoint

In browser Network tab, open the popup and look for:

getEtbPrivacyLanguageList

Expected status:

200

Expected response:

{
	"status": "success",
	"languageList": [
		{
			"locale": "eng",
			"languageName": "English"
		}
	]
}

Check 5: Verify privacy endpoint

After language list loads, check:

getEtbPrivacyNoticeByLocale

Request payload:

privacyLocale=eng

Expected response:

{
	"status": "success",
	"privacyNotice": "<p>...</p>"
}

Check 6: Verify checkbox

Run:

$("#essentialPurposeCheckbox").prop("checked")

Expected:

true

Run:

$("#essentialPurposeCheckbox").prop("disabled")

Expected:

true

⸻

Files changed

1. Existing consent collection JSP
   - Enable ConsentPopupETB.jsp include
   - Replace Essential Purpose checkbox block
   - Add openEtbPrivacyPopup() functions
2. WebContent/appNew/common/ConsentPopupETB.jsp
   - Replace complete file with ETB-only viewer popup
3. CommonLoanAction.java
   - Add two imports
   - Add privacyLocale field
   - Add getEtbPrivacyLanguageList()
   - Add getEtbPrivacyNoticeByLocale()
   - Add privacyLocale getter/setter
4. Struts common action XML
   - Add getEtbPrivacyLanguageList action
   - Add getEtbPrivacyNoticeByLocale action
5. web.xml
   - Add both URL patterns
6. jquery.commonFunction.js
   - Replace openPopups() with null-safe version

No entity, quote table, application table, DB consent column, NTB ID, privacy flag, or save method is required for this ETB privacy-view popup.
