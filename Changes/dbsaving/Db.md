Make this change in ConsentPopup.jsp, inside acceptPrivacyConsent().

Replace this:

$("#infoprovide").prop("disabled", false).prop("checked", true);

with this:

$("#infoprovide").prop("checked", true).prop("disabled", true);

And replace this:

$("#infoprovideCBS").prop("disabled", false).prop("checked", true);

with this:

$("#infoprovideCBS").prop("checked", true).prop("disabled", true);

Final part should look like this:

if ($("#infoprovide").length > 0) {
	$("#infoprovide").prop("checked", true).prop("disabled", true);
}
if ($("#infoprovideCBS").length > 0) {
	$("#infoprovideCBS").prop("checked", true).prop("disabled", true);
}

Also keep this in HomeFirstPageSession.jsp as it is:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">

This way:

The user cannot directly check/uncheck the checkbox.

The checkbox gets checked only after clicking Accept in the privacy popup.

After it is checked, it again becomes disabled, so the user cannot uncheck it.
