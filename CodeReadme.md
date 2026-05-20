Based on ALL your existing code structure (Struts2 + JSP + jQuery + Hibernate + CommonService), below is the COMPLETE implementation plan exactly according to your existing project coding style.

You already provided enough files.

No more files required right now.

⸻

WHAT YOU ARE IMPLEMENTING

Inside consent popup:

1. User clicks Read More
2. Popup opens
3. Dropdown of 28 Indian languages shown
4. On dropdown change:
    * consent content reloads from DB
5. User must scroll to bottom
6. Only then:
    * Close button enabled
    * Accept button enabled
7. On Accept:
    * popup closes
    * checkbox auto checked

⸻

FILES TO CHANGE

File	Change
PrivacyRequestResponse.java	Add named query
ConsentService.java	Add DB method
PrivacyNoticeAction.java	Add AJAX API
struts.xml	Add action mapping
ConsentPopup.jsp	Add dropdown + buttons
jquery.commonFunction.js	Add AJAX + scroll logic
HomeFirstPageSession.jsp	No major change

⸻

STEP 1

ENTITY CHANGES

FILE

PrivacyRequestResponse.java

⸻

REPLACE EXISTING QUERY BLOCK

REMOVE:

@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a")
@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a"),
})

⸻

ADD

@NamedQueries({
	@NamedQuery(
		name = "PrivacyRequestResponse.findAll",
		query = "SELECT a FROM PrivacyRequestResponse a"
	),
	@NamedQuery(
		name = "PrivacyRequestResponse.getConsentByLocale",
		query = "SELECT a FROM PrivacyRequestResponse a "
			  + "WHERE a.privacyLocale = :privacyLocale "
			  + "AND a.privacyTouchPointId = :privacyTouchPointId"
	)
})

⸻

STEP 2

CONSENT SERVICE CHANGES

FILE

ConsentService.java

⸻

ADD IMPORTS

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

⸻

ADD METHOD

public String getConsentByLocale(String locale, String touchPointId) {
	try {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("privacyLocale", locale);
		params.put("privacyTouchPointId", touchPointId);
		List<?> list = commonDao.getNamedQueryResult(
				"PrivacyRequestResponse.getConsentByLocale",
				params
		);
		if(list != null && list.size() > 0) {
			PrivacyRequestResponse response =
					(PrivacyRequestResponse) list.get(0);
			return response.getPrivacyNotice();
		}
	} catch(Exception e) {
		logger.info("Exception in getConsentByLocale", e);
	}
	return "";
}

⸻

STEP 3

PRIVACY NOTICE ACTION CHANGES

FILE

PrivacyNoticeAction.java

⸻

ADD VARIABLES

ADD BELOW:

private PrivacyRequest privacyRequest=new PrivacyRequest();

ADD:

@Autowired
private ConsentService consentService;
private String localeCode;
private String touchPointId;

⸻

ADD GETTERS SETTERS

public String getLocaleCode() {
	return localeCode;
}
public void setLocaleCode(String localeCode) {
	this.localeCode = localeCode;
}
public String getTouchPointId() {
	return touchPointId;
}
public void setTouchPointId(String touchPointId) {
	this.touchPointId = touchPointId;
}

⸻

ADD NEW AJAX METHOD

ADD BELOW storePrivacyNotice()

public StreamResult getConsentByLocale() {
	JSONObject json = new JSONObject();
	try {
		logger.info("localeCode :: " + localeCode);
		logger.info("touchPointId :: " + touchPointId);
		String consentText =
				consentService.getConsentByLocale(
						localeCode,
						touchPointId
				);
		json.put("status", "success");
		json.put("consentText", consentText);
	} catch(Exception e) {
		logger.info("Exception in getConsentByLocale", e);
		try {
			json.put("status", "error");
			json.put("message", "Unable to fetch consent");
		} catch(JSONException ex) {
			logger.info("JSON Exception", ex);
		}
	}
	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}

⸻

STEP 4

STRUTS.XML CHANGES

FILE

struts.xml

⸻

ADD ACTION

<action
	name="getConsentByLocale"
	class="com.mintstreet.consent.action.PrivacyNoticeAction"
	method="getConsentByLocale">
	<result type="stream"/>
</action>

⸻

STEP 5

CONSENT POPUP CHANGES

FILE

ConsentPopup.jsp

⸻

REPLACE EXISTING DIV

REMOVE:

<div id = "consentHomeLoanDiv" class="f-otp-pop-content ">
	<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
</div>

⸻

ADD THIS COMPLETE BLOCK

<div class="language-dropdown-section">
	<select id="consentLanguageDropdown"
			class="form-select">
		<option value="ENG">English</option>
		<option value="HIN">Hindi</option>
		<option value="MAR">Marathi</option>
		<option value="MAL">Malayalam</option>
		<option value="TAM">Tamil</option>
		<option value="TEL">Telugu</option>
		<option value="KAN">Kannada</option>
		<option value="GUJ">Gujarati</option>
		<option value="BEN">Bengali</option>
	</select>
</div>
<div id="consentHomeLoanDiv"
	 class="f-otp-pop-content"
	 style="
		max-height:500px;
		overflow-y:auto;
	 ">
	<s:property
		escapeHtml="false"
		value="%{beanList.consentHomeLoan}" />
</div>
<div style="margin-top:20px;text-align:center;">
	<button
		type="button"
		id="acceptConsentBtn"
		class="submit-btn"
		disabled="disabled">
		Accept
	</button>
</div>

⸻

STEP 6

DISABLE CLOSE BUTTON INITIALLY

FIND

<button type="button"
	class="close clo"
	data-bs-dismiss="modal"

⸻

REPLACE WITH

<button type="button"
	class="close clo"
	id="closeConsentBtn"
	data-bs-dismiss="modal"
	disabled="disabled"

⸻

STEP 7

JAVASCRIPT CHANGES

FILE

jquery.commonFunction.js

⸻

ADD COMPLETE CODE

$(document).ready(function() {
	$("#consentLanguageDropdown").change(function() {
		var selectedLang = $(this).val();
		loadConsentByLanguage(selectedLang);
	});
	$("#consentHomeLoan").on('shown.bs.modal', function () {
		disableConsentClose();
	});
	$("#consentHomeLoanDiv").scroll(function() {
		enableConsentAfterScroll();
	});
	$("#acceptConsentBtn").click(function() {
		$("#infoprovide").prop("checked", true);
		$("#infoprovideCBS").prop("checked", true);
		$("#consentHomeLoan").modal('hide');
	});
});

⸻

ADD AJAX METHOD

function loadConsentByLanguage(localeCode) {
	$.ajax({
		url : "getConsentByLocale",
		type : "POST",
		data : {
			localeCode : localeCode,
			touchPointId : "HOME_LOAN"
		},
		success : function(response) {
			var json = JSON.parse(response);
			if(json.status == "success") {
				$("#consentHomeLoanDiv")
					.html(json.consentText);
				$("#consentHomeLoanDiv")
					.scrollTop(0);
				disableConsentClose();
			}
		}
	});
}

⸻

ADD SCROLL VALIDATION

function enableConsentAfterScroll() {
	var div = $("#consentHomeLoanDiv")[0];
	if(div.scrollTop + div.clientHeight
		>= div.scrollHeight - 10) {
		$("#acceptConsentBtn")
			.prop("disabled", false);
		$("#closeConsentBtn")
			.prop("disabled", false);
	}
}

⸻

ADD DISABLE METHOD

function disableConsentClose() {
	$("#acceptConsentBtn")
		.prop("disabled", true);
	$("#closeConsentBtn")
		.prop("disabled", true);
}

⸻

STEP 8

DISABLE ESC + OUTSIDE CLICK

FIND MODAL DIV

<div class="modal fade otp-box"

⸻

REPLACE WITH

<div class="modal fade otp-box"
	 id="consentHomeLoan"
	 tabindex="-1"
	 role="dialog"
	 data-bs-backdrop="static"
	 data-bs-keyboard="false"

⸻

FINAL FLOW

User clicks:

Read More

↓

Popup opens

↓

Cannot close popup

↓

Must scroll fully

↓

Accept enabled

↓

User clicks Accept

↓

Checkbox auto selected

↓

Popup closes

↓

Banking compliance satisfied

⸻

HOW YOUR EXISTING READ MORE WORKS

This line:

onclick="javascript:openPopups('consentHomeLoan','1');"

calls JS function:

openPopups()

inside:

jquery.commonFunction.js

That function does:

$('#consentHomeLoan').modal('show');

This opens modal whose ID is:

id="consentHomeLoan"

inside:

ConsentPopup.jsp

So:

Click	Opens
Read More	consentHomeLoan modal

⸻

DB STRUCTURE YOU ARE USING

Column	Meaning
PRIVACY_ID	PK
PRIVACY_LOCALE	ENG/HIN/MAR
PRIVACY_NOTICE	HTML content
PRIVACY_TOUCH_POINT_ID	HOME_LOAN

Perfect structure already.
