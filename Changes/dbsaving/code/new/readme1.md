Perfect. Now I can give **minimal code-flow changes** on your actual files.

I found the correct place.

Your **Get Loan Quote button = `#subtn`** and consent checkbox = `#infoprovide`. 

---

# 1) `HomeRightContent.jsp`

## FILE

`/sbi/WebContent/appNew/loan/homeloan/HomeRightContent.jsp`

Find this form:

```jsp
<form name="homeloanform"
	id="homeloanform"
	method="post"
	action="javascript:void(0);"
	enctype="application/x-www-form-urlencoded">
```

Find inside form:

```jsp
<s:hidden name="csrfTokenVal"
	id="csrfTokenVal"
	value="%{#session.csrfTokenVal}" />
```

### ADD BELOW IT

```jsp
<s:hidden
	name="appForm.appPrivacyConsentFlag"
	id="appPrivacyConsentFlag" />

<s:hidden
	name="appForm.appNtbId"
	id="appNtbId" />
```

Final:

```jsp
<form name="homeloanform"
	id="homeloanform"
	method="post"
	action="javascript:void(0);"
	enctype="application/x-www-form-urlencoded">

	<s:hidden
		name="csrfTokenVal"
		id="csrfTokenVal"
		value="%{#session.csrfTokenVal}" />

	<s:hidden
		name="appForm.appPrivacyConsentFlag"
		id="appPrivacyConsentFlag" />

	<s:hidden
		name="appForm.appNtbId"
		id="appNtbId" />

	<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>

</form>
```

---

# 2) `ConsentPopup.jsp`

## CHANGE ACCEPT BUTTON

Find:

```html
<button type="button"
id="acceptConsentBtn"
class="btn btn-primary"
disabled="disabled"
onclick="savePrivacyConsent();
style="opacity:0.6;cursor:not-allowed;">
Accept
</button>
```

### REPLACE WITH

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity:0.6;cursor:not-allowed;">
	Accept
</button>
```

---

## REMOVE ENTIRE OLD FUNCTION

Delete fully:

```javascript
function savePrivacyConsent(){

	$.ajax({
		url : "savePrivacyConsent",
		type : "POST",

		success : function(response){

			var json = JSON.parse(response);

			if(json.status=="success"){
				$("#consentHomeLoan").modal("hide");
			}else{
				alert(json.message);
			}
		},

		error : function(){
			alert("Unable to save consent.");
		}
	});
}
```

---

## ADD NEW FUNCTION

Add near bottom of script:

```javascript
function acceptPrivacyConsent(){

	var mobile =
		$("#mobile").val();

	var dob =
		$("#date_of_birth").val();

	if($.trim(mobile)==""){
		alert("Please enter mobile number.");
		return false;
	}

	if($.trim(dob)==""){
		alert("Please enter date of birth.");
		return false;
	}

	var cleanMobile =
		$.trim(mobile).replace(/\D/g,'');

	var cleanDob =
		$.trim(dob).replace(/\D/g,'');

	var ntbId =
		cleanMobile +
		cleanDob +
		loanTypeId;

	$("#appPrivacyConsentFlag")
		.val("Y");

	$("#appNtbId")
		.val(ntbId);

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

	var consentModalEl =
		document.getElementById(
			"consentHomeLoan"
		);

	var consentModal =
		bootstrap.Modal.getInstance(
			consentModalEl
		);

	if(consentModal!=null){
		consentModal.hide();
	}else{
		$("#consentHomeLoan")
			.modal("hide");
	}
}
```

---

# 3) `ConsentPopup.jsp`

## KEEP YOUR EXISTING CODE

KEEP:

* `loadPrivacyByLocale()`
* scroll validation
* `resetConsentScrollValidation()`

No changes needed.

---

# 4) `HomeFirstPageSession.jsp`

Find button block: 

Current:

```jsp
<input type="submit"
	class="submit-btn"
	name="subtn"
	id="subtn"
	value="Get Loan Quote">
```

ADD BELOW IT:

```javascript
<script>

$(document).on(
	"click",
	"#subtn",
	function(){

	if(
		$("#appPrivacyConsentFlag")
			.val()!="Y"
	){

		alert(
			"Please read and accept consent before proceeding."
		);

		return false;
	}

});

</script>
```

---

# 5) `ApplicationFormHomeLoan.java`

Add:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;
```

Add getters/setters.

---

# 6) REMOVE UNUSED ACTION

You no longer need:

```xml
savePrivacyConsent
```

or AJAX backend method.

---

## FINAL FLOW

```text
Open popup
↓
Scroll till end
↓
Accept
↓
appPrivacyConsentFlag=Y
appNtbId=mobile+dob+loanTypeId
stored in hidden fields
↓
Get Loan Quote click
↓
normal form submit
↓
Struts binds automatically
↓
ApplicationFormHomeLoan saved
↓
DB save happens in existing flow
```

Minimal changes. No new DAO/service/action needed.
