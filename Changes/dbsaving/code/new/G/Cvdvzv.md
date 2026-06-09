Your dropdown is blank because:

<s:iterator value="languages" var="lang">

means languages must already be loaded in HomeLoanAction before JSP opens. Right now it is probably null.

Use this minimum working fix: populate dropdown by AJAX again.

1. Replace dropdown with empty select

<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>

Remove this block:

<s:iterator value="languages" var="lang">
	<option value="<s:property value="#lang.lannguageCode" />"
		<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
		<s:property value="#lang.languageName" />
	</option>
</s:iterator>

2. Change modal open event

Use this:

$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	loadPrivacyLanguageDropdown();
});

Remove/comment this:

$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	if($("#privacyLocaleDropdown option[value='eng']").length > 0){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	} else {
		var firstLocale = $("#privacyLocaleDropdown option:first").val();
		loadPrivacyByLocale(firstLocale);
	}
});

3. Keep loadPrivacyLanguageDropdown(), but replace with this

function loadPrivacyLanguageDropdown(){
	$.ajax({
		url : "getPrivacyLanguageList",
		type : "POST",
		success : function(response){
			var json = typeof response === "string"
				? JSON.parse(response)
				: response;
			console.log("Language Response:", json);
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
			} else {
				$("#privacyLocaleDropdown").html(
					"<option value='eng'>English</option>"
				);
				loadPrivacyByLocale("eng");
			}
		},
		error : function(xhr){
			console.log("getPrivacyLanguageList failed:", xhr.status, xhr.responseText);
			$("#privacyLocaleDropdown").html(
				"<option value='eng'>English</option>"
			);
			loadPrivacyByLocale("eng");
		}
	});
}

4. Keep loadPrivacyByLocale() same, but safer JSON parse

Replace:

var json = JSON.parse(response);

With:

var json = typeof response === "string"
	? JSON.parse(response)
	: response;

Final flow:

Popup open
→ AJAX calls getPrivacyLanguageList
→ DB languages populate dropdown
→ English selected by default
→ English privacy notice loads
→ User selects Hindi/Marathi/etc
→ onchange calls loadPrivacyByLocale()
