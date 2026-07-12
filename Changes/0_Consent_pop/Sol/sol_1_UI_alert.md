Yes. Instead of using `alert()`, SBI Internet Banking and YONO generally show **inline validation messages** near the form/button. It is a better UX because the user doesn't get disruptive popups.

## Step 1. Add a validation message div

In **HomeFirstPageSession.jsp**, just above the **Get Loan Quote** button, add:

```jsp
<div id="privacyConsentError"
	style="display:none;
	color:#d32f2f;
	font-size:13px;
	margin:8px 0;
	font-family:Roboto-Regular;">
</div>
```

Place it here:

```jsp
<div> </div>
<div> </div>

<div id="privacyConsentError"
	style="display:none;
	color:#d32f2f;
	font-size:13px;
	margin:8px 0;
	font-family:Roboto-Regular;">
</div>

<div class="qt-btn-div flr mrgt-10 m-100 call-us">
	<input type="submit" class="submit-btn"
		name="subtn"
		id="subtn"
		value="Get Loan Quote">
</div>
```

---

# Step 2. Create two helper methods

In **ConsentPopup.jsp** (or HomeFirstPageSession.jsp), add:

```javascript
function showPrivacyError(message) {

	$("#privacyConsentError")
		.html(message)
		.show();

	$('html, body').animate({
		scrollTop: $("#privacyConsentError").offset().top - 100
	}, 300);
}

function clearPrivacyError() {

	$("#privacyConsentError")
		.html("")
		.hide();
}
```

---

# Step 3. After Accept button

Replace

```javascript
$("#infoprovide")
	.prop("disabled", false)
	.prop("checked", true);
```

with

```javascript
$("#infoprovide")
	.prop("disabled", false)
	.prop("checked", true);

clearPrivacyError();
```

Similarly for CBS.

---

# Step 4. When user unchecks checkbox

Replace

```javascript
alert("Privacy consent has been removed. Please read and accept the Privacy Notice again.");
```

with

```javascript
showPrivacyError(
	"Privacy consent has been removed. Please read and accept SBI Privacy Notice again."
);
```

---

# Step 5. On form submit

Instead of

```javascript
alert("Please read and accept SBI Privacy Notice before proceeding.");
```

use

```javascript
showPrivacyError(
	"Please read and accept SBI Privacy Notice before proceeding."
);
```

Instead of

```javascript
alert("Invalid consent details. Please read and accept SBI Privacy Notice again.");
```

use

```javascript
showPrivacyError(
	"Invalid consent details. Please read and accept SBI Privacy Notice again."
);
```

---

# Step 6. When popup is opened

Inside

```javascript
$(document).on("show.bs.modal", "#consentHomeLoan", function(e) {
```

add

```javascript
clearPrivacyError();
```

---

# Step 7. When consent is accepted

At the end of

```javascript
acceptPrivacyConsent()
```

add

```javascript
clearPrivacyError();
```

---

## Final UI

Instead of browser alerts, the user will see a red validation message just above the **Get Loan Quote** button.

Example:

```
----------------------------------------------------
☒ Please read and accept SBI Privacy Notice before proceeding.

                    [ Get Loan Quote ]
----------------------------------------------------
```

or

```
----------------------------------------------------
☒ Privacy consent has been removed.
Please read and accept SBI Privacy Notice again.

                    [ Get Loan Quote ]
----------------------------------------------------
```

This is much closer to the validation style used in SBI Online Internet Banking and other banking portals: the message appears inline on the page, remains visible until corrected, and doesn't interrupt the user's workflow with browser alert dialogs.
