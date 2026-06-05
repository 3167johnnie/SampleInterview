Please send these files also:

1. `struts.xml` action mappings
2. `HomeLoanAction.java` methods added
3. `CommonService.java` DAO injection part
4. Spring bean XML where `commonService` / DAO beans are configured

One clear issue in your JSP now:

```html
onchange="loadPrivacyLanguageDropdown();"
```

This is wrong. On change, it should load notice for selected language, not reload dropdown.

Change to:

```html
<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>
```

Also this part is wrong:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	$("#privacyLocaleDropdown").val("eng");
	loadPrivacyByLocale("eng");
});
```

Replace with:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	loadPrivacyLanguageDropdown();
});
```

Your 404 is most likely from one of these URLs:

```javascript
url : "getPrivacyLanguageList"
url : "getPrivacyNoticeByLocale"
```

After you send `struts.xml`, I’ll tell exact URL/action correction.
