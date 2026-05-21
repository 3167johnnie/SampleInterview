You should NOT replace your existing popup structure.

You only need to ENHANCE the existing JSP popup.

Below are the EXACT frontend changes on your existing code.

⸻

EXISTING JSP

You currently have:

<div id = "consentHomeLoanDiv" class="f-otp-pop-content ">
	<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
</div>

This is the area where multilingual DB content should load dynamically.

⸻

FINAL UPDATED JSP

Replace your FULL popup with this updated version.

<%@ page language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="termAndConditionHTML">
	<div class="modal fade otp-box"
		id="consentHomeLoan"
		tabindex="-1"
		role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog"
			role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button"
						class="close clo"
						data-bs-dismiss="modal"
						aria-bs-label="Close">
						<span aria-hidden="true">
							<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/>
						</span>
					</button>
					<!-- LANGUAGE DROPDOWN -->
					<div style="margin-bottom:15px;">
						<select id="privacyLocaleDropdown"
							class="form-control"
							onchange="loadPrivacyByLocale();">
							<option value="eng">
								English
							</option>
							<option value="hin">
								Hindi
							</option>
							<option value="mar">
								Marathi
							</option>
							<option value="mal">
								Malayalam
							</option>
						</select>
					</div>
					<!-- CONSENT CONTENT -->
					<div id="consentHomeLoanDiv"
						class="f-otp-pop-content"
						style="
							max-height:400px;
							overflow-y:auto;
						">
						<s:property
							escapeHtml="false"
							value="%{beanList.consentHomeLoan}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

⸻

NOW ADD JAVASCRIPT

Add BELOW popup JSP.

⸻

COMPLETE SCRIPT

<script>
$(document).ready(function(){
	/*
	 * Default English Load
	 */
	loadPrivacyByLocale("eng");
	/*
	 * Scroll Detection
	 */
	$("#consentHomeLoanDiv").on("scroll", function(){
		var div = $(this)[0];
		/*
		 * Detect Bottom Scroll
		 */
		if(
			div.scrollTop + div.clientHeight
			>= div.scrollHeight
		){
			/*
			 * Auto Close Popup
			 */
			$("#consentHomeLoan").modal("hide");
		}
	});
});
/*
 * Load Consent By Language
 */
function loadPrivacyByLocale(locale){
	/*
	 * Dropdown Selection
	 */
	if(locale == null){
		locale =
			$("#privacyLocaleDropdown").val();
	}
	$.ajax({
		url : "getPrivacyNoticeByLocale",
		type : "POST",
		data : {
			privacyLocale : locale
		},
		success : function(response){
			var json = JSON.parse(response);
			if(json.status == "success"){
				$("#consentHomeLoanDiv")
					.html(json.privacyNotice);
				/*
				 * Reset Scroll Top
				 */
				$("#consentHomeLoanDiv")
					.scrollTop(0);
			}else{
				$("#consentHomeLoanDiv")
					.html(
						"Privacy Notice Not Found"
					);
			}
		},
		error : function(){
			$("#consentHomeLoanDiv")
				.html(
					"Unable To Load Privacy Notice"
				);
		}
	});
}
</script>

⸻

IMPORTANT BEHAVIOUR NOW

1. POPUP OPEN

Automatically loads:

eng

from DB.

⸻

2. USER SELECTS HINDI

Dropdown changes:

hin

AJAX calls:

getPrivacyNoticeByLocale

Backend fetches:

WHERE PRIVACY_LOCALE='hin'

Returns Hindi HTML content.

Popup updates instantly.

⸻

3. USER SCROLLS TO BOTTOM

This automatically closes popup:

$("#consentHomeLoan").modal("hide");

⸻

IMPORTANT BACKEND REQUIREMENT

Your backend action MUST return:

{
	"status":"success",
	"privacyNotice":"<html content>"
}

Otherwise frontend won’t work.

⸻

IMPORTANT CSS EFFECT

This:

max-height:400px;
overflow-y:auto;

is REQUIRED.

Otherwise scroll detection won’t work.

⸻

YOUR FINAL FLOW

Popup Opens
    ↓
English Consent Loaded From DB
    ↓
User Changes Dropdown
    ↓
Language Content Reloaded
    ↓
User Scrolls Till Bottom
    ↓
Popup Auto Closes








xxxxxxxxxxxxxxxxxxxxxxxxxxxx







Yes — exactly.

Your requirement is:

When popup opens:
	Load English consent automatically from DB
When user changes dropdown:
	Load selected language consent from DB

So you should NOT use:

beanList.consentHomeLoan

at all.

Everything should come from:

PRIVACY_NOTICE

using:

PRIVACY_LOCALE

⸻

FINAL CORRECT JSP FLOW

STEP 1

KEEP EMPTY DIV

Replace:

<s:property escapeHtml="false"
	value="%{beanList.consentHomeLoan}" />

WITH:

Loading Privacy Notice...

FINAL:

<div id="consentHomeLoanDiv"
	class="f-otp-pop-content"
	style="
		max-height:400px;
		overflow-y:auto;
	">
	Loading Privacy Notice...
</div>

⸻

STEP 2

AUTO LOAD ENGLISH

This is the MOST IMPORTANT PART.

Add:

$(document).ready(function(){
	loadPrivacyByLocale("eng");
});

This means:

Popup/Page loads
	↓
AJAX automatically sends:
	privacyLocale=eng
	↓
Backend fetches English content
	↓
English consent shown

⸻

FINAL JAVASCRIPT

Use THIS COMPLETE SCRIPT:

<script>
$(document).ready(function(){
	/*
	 * Default English Load
	 */
	loadPrivacyByLocale("eng");
	/*
	 * Scroll Bottom Detection
	 */
	$("#consentHomeLoanDiv").on("scroll", function(){
		var div = $(this)[0];
		if(
			div.scrollTop + div.clientHeight
			>= div.scrollHeight
		){
			/*
			 * Auto Close Popup
			 */
			$("#consentHomeLoan").modal("hide");
		}
	});
});
function loadPrivacyByLocale(locale){
	/*
	 * Dropdown Selected Locale
	 */
	if(locale == null){
		locale =
			$("#privacyLocaleDropdown").val();
	}
	$.ajax({
		url : "getPrivacyNoticeByLocale",
		type : "POST",
		data : {
			privacyLocale : locale
		},
		success : function(response){
			var json = JSON.parse(response);
			if(json.status == "success"){
				$("#consentHomeLoanDiv")
					.html(json.privacyNotice);
				/*
				 * Reset Scroll
				 */
				$("#consentHomeLoanDiv")
					.scrollTop(0);
			}else{
				$("#consentHomeLoanDiv")
					.html(
						"Privacy Notice Not Found"
					);
			}
		},
		error : function(){
			$("#consentHomeLoanDiv")
				.html(
					"Unable To Load Privacy Notice"
				);
		}
	});
}
</script>

⸻

FINAL BACKEND FLOW

Popup Open

Automatically sends:

privacyLocale=eng

Backend:

SELECT PRIVACY_NOTICE
FROM RUPEEPOWER_OCAS_T_13703
WHERE PRIVACY_LOCALE='eng'

Returns:

English Consent HTML

Frontend loads inside:

#consentHomeLoanDiv

⸻

USER SELECTS HINDI

Dropdown sends:

privacyLocale=hin

Backend fetches Hindi consent.

Popup updates dynamically.

⸻

FINAL RESULT

Popup Opens
    ↓
English consent loaded from DB
    ↓
Dropdown changed
    ↓
Selected language loaded from DB
    ↓
Scroll to bottom
    ↓
Popup closes



Replace your existing JSP with this COMPLETE updated version.

This version supports:

* Default English consent from DB
* Language dropdown
* AJAX-based multilingual loading
* Scroll till bottom validation
* Accept button enabled only after scroll completion
* Popup closes only after Accept button click
* SBI banking consent popup style

<%@ page language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="termAndConditionHTML">
	<div class="modal fade otp-box"
		id="consentHomeLoan"
		tabindex="-1"
		role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog"
			role="document">
			<div class="modal-content">
				<div class="modal-body">
					<!-- CLOSE BUTTON -->
					<button type="button"
						class="close clo"
						data-bs-dismiss="modal"
						aria-bs-label="Close">
						<span aria-hidden="true">
							<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/>
						</span>
					</button>
					<!-- TITLE -->
					<div style="
						font-size:20px;
						font-weight:600;
						color:#1d3b72;
						margin-bottom:15px;
						text-align:center;
					">
						Privacy Notice & Consent
					</div>
					<!-- LANGUAGE DROPDOWN -->
					<div style="margin-bottom:15px;">
						<select id="privacyLocaleDropdown"
							class="form-control"
							onchange="loadPrivacyByLocale();">
							<option value="eng">
								English
							</option>
							<option value="hin">
								Hindi
							</option>
							<option value="mar">
								Marathi
							</option>
							<option value="mal">
								Malayalam
							</option>
						</select>
					</div>
					<!-- CONSENT CONTENT -->
					<div id="consentHomeLoanDiv"
						class="f-otp-pop-content"
						style="
							max-height:400px;
							overflow-y:auto;
							border:1px solid #dcdcdc;
							padding:15px;
							border-radius:6px;
							background:#fafafa;
						">
						Loading Privacy Notice...
					</div>
					<!-- FOOTER MESSAGE -->
					<div id="scrollMessage"
						style="
							margin-top:10px;
							font-size:12px;
							color:#777;
							text-align:center;
						">
						Please scroll till bottom to enable Accept button
					</div>
					<!-- ACCEPT BUTTON -->
					<div style="
						margin-top:15px;
						text-align:center;
					">
						<button type="button"
							id="acceptConsentBtn"
							class="btn btn-primary"
							disabled="disabled"
							style="
								min-width:150px;
								opacity:0.6;
								cursor:not-allowed;
							">
							Accept
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	/*
	 * Default English Load
	 */
	loadPrivacyByLocale("eng");
	/*
	 * Scroll Detection
	 */
	$("#consentHomeLoanDiv").on("scroll", function(){
		var div = $(this)[0];
		/*
		 * Detect Bottom Scroll
		 */
		if(
			div.scrollTop + div.clientHeight
			>= div.scrollHeight - 5
		){
			/*
			 * Enable Accept Button
			 */
			$("#acceptConsentBtn")
				.prop("disabled", false)
				.css({
					"opacity":"1",
					"cursor":"pointer"
				});
			$("#scrollMessage").html(
				"You can now accept the consent"
			);
		}
	});
	/*
	 * Accept Button Click
	 */
	$("#acceptConsentBtn").click(function(){
		/*
		 * Close Popup
		 */
		$("#consentHomeLoan").modal("hide");
	});
});
/*
 * Load Privacy Notice By Locale
 */
function loadPrivacyByLocale(locale){
	/*
	 * Dropdown Locale
	 */
	if(locale == null){
		locale =
			$("#privacyLocaleDropdown").val();
	}
	/*
	 * Sync Dropdown
	 */
	$("#privacyLocaleDropdown")
		.val(locale);
	/*
	 * Disable Button On Language Change
	 */
	$("#acceptConsentBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.6",
			"cursor":"not-allowed"
		});
	$("#scrollMessage").html(
		"Please scroll till bottom to enable Accept button"
	);
	$.ajax({
		url : "getPrivacyNoticeByLocale",
		type : "POST",
		data : {
			privacyLocale : locale
		},
		beforeSend : function(){
			$("#consentHomeLoanDiv")
				.html(
					"<div style='text-align:center;padding:20px;'>Loading...</div>"
				);
		},
		success : function(response){
			var json = JSON.parse(response);
			if(json.status == "success"){
				/*
				 * Update Consent HTML
				 */
				$("#consentHomeLoanDiv")
					.html(json.privacyNotice);
				/*
				 * Reset Scroll
				 */
				$("#consentHomeLoanDiv")
					.scrollTop(0);
			}else{
				$("#consentHomeLoanDiv")
					.html(
						"<div style='color:red;text-align:center;'>Privacy Notice Not Found</div>"
					);
			}
		},
		error : function(){
			$("#consentHomeLoanDiv")
				.html(
					"<div style='color:red;text-align:center;'>Unable To Load Privacy Notice</div>"
				);
		}
	});
}
</script>












