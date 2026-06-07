Yes. Use this function only.

## Keep HTML same

```jsp
<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>
```

## On modal open

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	loadPrivacyLanguageDropdown();
});
```

## Replace `loadPrivacyLanguageDropdown()`

```javascript
function loadPrivacyLanguageDropdown(){

	$("#privacyLocaleDropdown").html(
		"<option value='eng'>English</option>"
	);

	$.ajax({
		url : "getPrivacyLanguageList",
		type : "POST",
		success : function(response){

			var json = typeof response === "string"
				? JSON.parse(response)
				: response;

			var optionHtml = "";

			if(json.status == "success"
				&& json.languageList != null
				&& json.languageList.length > 0){

				$.each(json.languageList, function(index, item){
					optionHtml += "<option value='"
						+ item.locale
						+ "'>"
						+ item.languageName
						+ "</option>";
				});

				$("#privacyLocaleDropdown").html(optionHtml);

				$("#privacyLocaleDropdown").val("eng");

				loadPrivacyByLocale("eng");
			}
		},
		error : function(){
			$("#privacyLocaleDropdown").html(
				"<option value='eng'>English</option>"
			);

			loadPrivacyByLocale("eng");
		}
	});
}
```

## Existing `loadPrivacyByLocale()` should remain

```javascript
function loadPrivacyByLocale(locale) {
	if (locale == null) {
		locale = $("#privacyLocaleDropdown").val();
	}

	$.ajax({
		url : "getPrivacyNoticeByLocale",
		type : "POST",
		data : {
			privacyLocale : locale
		},
		success : function(response) {
			var json = typeof response === "string"
				? JSON.parse(response)
				: response;

			if (json.status == "success") {
				$("#consentHomeLoanDiv").html(json.privacyNotice);
				$("#consentHomeLoanDiv").scrollTop(0);
				resetConsentScrollValidation();
			} else {
				$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
			}
		}
	});
}
```

Flow:

```text
Popup opens
→ getPrivacyLanguageList calls DB
→ all language options added to select
→ English selected by default
→ English notice loaded
→ user selects Hindi/Marathi/etc
→ loadPrivacyByLocale() loads selected notice
```
