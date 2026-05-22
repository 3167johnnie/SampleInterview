Now we found the EXACT issue.

This line in your existing JS is controlling the “Read More” popup:

```javascript id="c6p7ow"
jQuery(document).on("click",".read-more", function(){
	$("#checkoff").modal('toggle');
});
```

BUT your popup ID is:

```html id="70m4sq"
id="consentHomeLoan"
```

NOT:

```html id="6mgg0e"
#checkoff
```

That is why after your popup changes:

```text id="q3vy8m"
Read More → Unknown Error
```

because JS is trying to open a modal that does not exist.

---

# EXACT FIX

Inside:

```text id="8b8wuj"
jquery.homeloan.js
```

FIND THIS:

```javascript id="c7tw8o"
jQuery(document).on("click",".read-more", function(){
	$("#checkoff").modal('toggle');
});
```

---

# REPLACE WITH THIS

```javascript id="fjlwm2"
jQuery(document).on("click",".read-more", function(){

	/*
	 * Open Consent Popup
	 */
	$("#consentHomeLoan").modal('show');

	/*
	 * Default English Load
	 */
	loadPrivacyByLocale("eng");

});
```

---

# NOW UPDATE ConsentPopup.jsp

REMOVE THIS OLD CODE:

```jsp id="6vrjlwm"
<div id = "consentHomeLoanDiv"
	class="f-otp-pop-content ">

	<s:property escapeHtml="false"
		value="%{beanList.consentHomeLoan}" />

</div>
```

---

# REPLACE WITH THIS

```jsp id="6j27xt"
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

	Loading Privacy Notice...

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
			opacity:0.6;
			cursor:not-allowed;
		">

		Accept

	</button>

</div>
```

---

# NOW ADD THIS JS

Add BELOW `ConsentPopup.jsp`

```html id="aehbfr"
<script>

function loadPrivacyByLocale(locale){

	if(locale == null){

		locale =
			$("#privacyLocaleDropdown").val();
	}

	/*
	 * Disable Accept Button
	 */
	$("#acceptConsentBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.6",
			"cursor":"not-allowed"
		});

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


/*
 * Scroll Detection
 */
$(document).on(
	"scroll",
	"#consentHomeLoanDiv",
	function(){

		var div = $(this)[0];

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
		}
});


/*
 * Accept Button
 */
$(document).on(
	"click",
	"#acceptConsentBtn",
	function(){

		$("#consentHomeLoan")
			.modal("hide");
});

</script>
```

---

# WHY YOUR POPUP BROKE

Because old system expected:

```javascript id="5jjlwm"
$("#checkoff")
```

But your new popup became:

```javascript id="2p7oh4"
#consentHomeLoan
```

So modal open failed.

---

# FINAL FLOW NOW

```text id="mkd4pk"
Read More Click
    ↓
consentHomeLoan Popup Opens
    ↓
English Consent Loaded From DB
    ↓
User Selects Language
    ↓
Consent Reloaded From DB
    ↓
User Scrolls Till Bottom
    ↓
Accept Button Enabled
    ↓
Accept Click
    ↓
Popup Closes
```

Your JS file content confirms the popup issue was caused by:

```javascript id="xjlwmc"
$("#checkoff").modal('toggle');
```

from the existing banking flow. 
