Your current issue is caused by these lines inside `acceptPrivacyConsent()`:

```javascript
$("#infoprovide").prop("disabled", true);
```

After consent is accepted, you are disabling the checkbox again. Therefore, the user cannot uncheck it.

You need only three small changes.

## 1. `ConsentPopup.jsp` — change `acceptPrivacyConsent()`

### Existing code

```javascript
if ($("#infoprovide").length > 0) {
	$("#infoprovide").prop("disabled", false).prop("checked", true);
	$("#infoprovide").prop("disabled", true);
}

if ($("#infoprovideCBS").length > 0) {
	$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
	$("#infoprovideCBS").prop("disabled", true);
}
```

### Replace with

```javascript
if ($("#infoprovide").length > 0) {
	$("#infoprovide")
		.prop("disabled", false)
		.prop("checked", true);
}

if ($("#infoprovideCBS").length > 0) {
	$("#infoprovideCBS")
		.prop("disabled", false)
		.prop("checked", true);
}
```

Do not disable the checkbox after checking it.

Your final `acceptPrivacyConsent()` should contain:

```javascript
function acceptPrivacyConsent() {
	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();
	var selectedLocale = $("#privacyLocaleDropdown").val();

	if (mobile == null || $.trim(mobile) == "") {
		alert("Please enter mobile number before accepting consent.");
		return false;
	}

	if (dob == null || $.trim(dob) == "") {
		alert("Please enter date of birth before accepting consent.");
		return false;
	}

	var div = $("#consentHomeLoanDiv")[0];

	if (div && div.scrollTop + div.clientHeight < div.scrollHeight - 5) {
		alert("Please read the privacy notice till the end before accepting.");
		return false;
	}

	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = $.trim(dob).replace(/\D/g, "");
	var ntbId = cleanMobile + cleanDob + loanTypeId;

	$("#quotePrivacyConsentFlag").val("Y");
	$("#quoteNtbId").val(ntbId);
	$("#quotePrivacyLocale").val(selectedLocale);

	if ($("#infoprovide").length > 0) {
		$("#infoprovide")
			.prop("disabled", false)
			.prop("checked", true);
	}

	if ($("#infoprovideCBS").length > 0) {
		$("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", true);
	}

	$("#consentHomeLoan").modal("hide");

	return true;
}
```

---

## 2. `HomeFirstPageSession.jsp` — update checkbox change event

Your existing code currently disables the checkbox when it is unchecked, but does not clear the accepted consent values. 

### Existing code

```javascript
$('#infoprovide').change(function() {
	if ($(this).is(':checked')) {
	} else {
		$('#infoprovide').prop('disabled', true);
	}
});
```

### Replace with

```javascript
$("#infoprovide").on("change", function() {

	if (!$(this).is(":checked")) {

		// Clear previously accepted consent details
		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		$("#quotePrivacyLocale").val("");

		/*
		 * Disable checkbox after unchecking.
		 * Customer must open Privacy Notice and accept again.
		 */
		$(this).prop("disabled", true);

		alert("Privacy consent has been removed. Please read and accept the Privacy Notice again.");
	}
});
```

The behavior will now be:

1. Checkbox starts disabled.
2. User opens privacy notice.
3. User reads and clicks **Accept**.
4. Checkbox becomes enabled and checked.
5. User can uncheck it.
6. After unchecking, hidden consent values are cleared.
7. Checkbox becomes disabled again.
8. User cannot manually re-check it.
9. User must open the popup and accept consent again.

Keep the initial checkbox as:

```jsp
<input type="checkbox"
	class="blue-css-checkbox"
	name="infoprovide"
	id="infoprovide"
	value="on"
	disabled="disabled">
```

This matches the existing checkbox location in your JSP. 

---

## 3. `HomeFirstPageSession.jsp` — validate before form submission

Add this event inside your existing `jQuery(document).ready(function() { ... });`.

```javascript
$("#homeloanform").on("submit", function(e) {

	if (!$("#infoprovide").is(":checked")
			|| $("#quotePrivacyConsentFlag").val() !== "Y") {

		e.preventDefault();

		alert("Please read and accept SBI Privacy Notice before proceeding.");

		return false;
	}

	if ($.trim($("#quoteNtbId").val()) === "") {

		e.preventDefault();

		alert("Invalid consent details. Please read and accept SBI Privacy Notice again.");

		return false;
	}

	return true;
});
```

Since your submit button has `type="submit"` and ID `subtn`, validating the form submission is safer than validating only the button click. The current submit button is at the end of the consent section. 

Your complete ready block should be:

```javascript
jQuery(document).ready(function() {

	$("#infoprovide").on("change", function() {

		if (!$(this).is(":checked")) {

			$("#quotePrivacyConsentFlag").val("");
			$("#quoteNtbId").val("");
			$("#quotePrivacyLocale").val("");

			$(this).prop("disabled", true);

			alert("Privacy consent has been removed. Please read and accept the Privacy Notice again.");
		}
	});

	$("#homeloanform").on("submit", function(e) {

		if (!$("#infoprovide").is(":checked")
				|| $("#quotePrivacyConsentFlag").val() !== "Y") {

			e.preventDefault();

			alert("Please read and accept SBI Privacy Notice before proceeding.");

			return false;
		}

		if ($.trim($("#quoteNtbId").val()) === "") {

			e.preventDefault();

			alert("Invalid consent details. Please read and accept SBI Privacy Notice again.");

			return false;
		}

		return true;
	});

	$("#infoprovide").rules("add", {
		required: true,
		valueNotEquals: 0
	});

	jQuery("#infoprovide").addClass("error");
});
```

## Important flow

Do not use this after accepting:

```javascript
$("#infoprovide").prop("disabled", true);
```

Use:

```javascript
$("#infoprovide").prop("disabled", false).prop("checked", true);
```

Then disable it only when the customer manually unchecks it:

```javascript
$(this).prop("disabled", true);
```

This gives exactly the required behavior: accept → checked → user may uncheck → consent invalidated → must read and accept again → submit allowed only when checked.
