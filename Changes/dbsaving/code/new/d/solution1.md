Yes, there are **2 errors** in this `ConsentPopup.jsp`.

## Main syntax error

This line is broken:

```html
<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="acceptPrivacyConsent(); style="opacity: 0.6; cursor: not-allowed;">Accept</button>
```

Problem:

```html
onclick="acceptPrivacyConsent(); style="
```

You forgot to close `onclick` before `style`.

## Replace with this

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity:0.6; cursor:not-allowed;">
	Accept
</button>
```

---

## Second issue

Remove this duplicate click handler:

```javascript
$(document).on("click", "#acceptConsentBtn", function() {
	...
});
```

Because your button already calls:

```javascript
onclick="acceptPrivacyConsent();"
```

That duplicate handler can close popup without properly checking hidden field flow.

---

## Also update inside `loadPrivacyByLocale()`

Change:

```javascript
$("#infoprovideCBS").prop("checked", false);
```

to:

```javascript
if ($("#infoprovideCBS").length > 0) {
	$("#infoprovideCBS").prop("checked", false);
}
```

---

After these fixes, the console syntax error should go.
