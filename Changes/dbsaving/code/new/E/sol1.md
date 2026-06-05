Below are the **complete required changes from start to finish**.

Your `HomeLoanAction` already has `getPrivacyNoticeByLocale()` and `getPrivacyLanguageList()` methods added, and it imports `MasterLanguage` also. So backend action class is mostly ready. 

---

## 1. Fix JSP dropdown

Replace this:

```jsp
<select id="privacyLocaleDropdown" class ="privacy-consent-dropdown"
	onchange="loadPrivacyLanguageDropdown();">
</select>
```

With this:

```jsp
<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>
```

---

## 2. Add context path in JSP script

Add this at the start of your `<script>`:

```jsp
var contextPath = "<%=request.getContextPath()%>";
```

---

## 3. Fix modal open event

Replace this:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	$("#privacyLocaleDropdown").val("eng");
	loadPrivacyByLocale("eng");
});
```

With this:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	loadPrivacyLanguageDropdown();
});
```

---

## 4. Replace `loadPrivacyLanguageDropdown()`

```javascript
function loadPrivacyLanguageDropdown(){
	$.ajax({
		url : contextPath + "/getPrivacyLanguageList.action",
		type : "POST",
		success : function(response){
			var json = typeof response === "string"
				? JSON.parse(response)
				: response;

			if(json.status == "success"){
				var optionHtml = "";

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
```

---

## 5. Replace AJAX URL inside `loadPrivacyByLocale()`

Change this:

```javascript
url : "getPrivacyNoticeByLocale",
```

To this:

```javascript
url : contextPath + "/getPrivacyNoticeByLocale.action",
```

Also replace:

```javascript
var json = JSON.parse(response);
```

With:

```javascript
var json = typeof response === "string"
	? JSON.parse(response)
	: response;
```

---

## 6. Final corrected `loadPrivacyByLocale()`

```javascript
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

	$.ajax({
		url : contextPath + "/getPrivacyNoticeByLocale.action",
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
		},
		error : function(xhr) {
			console.log("getPrivacyNoticeByLocale failed:", xhr.status, xhr.responseText);
			$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
		}
	});
}
```

---

## 7. Keep your Struts actions as below

Your mapping is okay:

```xml
<action name="getPrivacyNoticeByLocale" class="homeLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

<action name="getPrivacyLanguageList" class="homeLoanAction"
	method="getPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

---

## 8. Make sure DAO/service exists

Dropdown should call:

```java
List<MasterLanguage> langList = commonService.getAllActiveLanguages();
```

Your `HomeLoanAction` is already doing this. 

So `CommonService` must have:

```java
public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}
```

---

## Main reason for 404

Your JSP was calling:

```javascript
url : "getPrivacyLanguageList"
```

Browser may resolve it relative to the current page URL, so Struts action is not found.

Use:

```javascript
contextPath + "/getPrivacyLanguageList.action"
```

and

```javascript
contextPath + "/getPrivacyNoticeByLocale.action"
```
