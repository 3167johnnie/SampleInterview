Correct. Your dropdown works because `onchange="loadPrivacyByLocale();"` is firing.

But on first popup open, no event is firing. So English is not loading.

Add this in `ConsentPopup.jsp` inside your `<script>` block, after `loadPrivacyByLocale()` function:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){

	$("#privacyLocaleDropdown").val("eng");

	loadPrivacyByLocale("eng");
});
```

Also change your `.read-more` click in `jquery.homeloan.js`.

Find:

```javascript
jQuery(document).on("click",".read-more", function(){
	$("#checkoff").modal('toggle');
});
```

Replace with:

```javascript
jQuery(document).on("click",".read-more", function(){

	$("#privacyLocaleDropdown").val("eng");

	loadPrivacyByLocale("eng");

	$("#consentHomeLoan").modal("show");
});
```

Use both. This makes English load in two cases:

```text
Read More click → load eng
Modal actually opens → load eng again safely
```

One correction: if you are using Bootstrap 5, `shown.bs.modal` still works, but `$("#consentHomeLoan").modal("show")` may not work unless Bootstrap jQuery bridge is available. Since your dropdown is working and old modal code exists, keep this first.

Also make sure your DB value is exactly:

```text
eng
```

not:

```text
Eng
ENGLISH
english
```

because your query is matching `PRIVACY_LOCALE = eng`.
