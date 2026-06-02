Below is the complete change set based on your **current code**.

Main flow:

```text
Read More clicked
→ popup opens
→ privacy notice loads
→ Accept disabled
→ customer scrolls till bottom
→ Accept enabled
→ Accept clicked
→ mobile fetched from #mobile
→ DOB fetched from #date_of_birth
→ ntbId = mobile + dob + loanTypeId
→ AJAX savePrivacyConsent
→ Action updates APP_PRIVACY_CONSENT_FLAG = Y and APP_NTB_ID
→ success closes popup and checks checkbox
```

---

# 1. DB change

Because Home Loan entity maps to:

```java
@Table(name = "RUPEEPOWER_OCAS_T_00195")
```

Run this:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_NTB_ID VARCHAR2(100);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_TIME DATE;
```

---

# 2. `ApplicationFormHomeLoan.java`

## 2.1 Add fields

Find this existing field:

```java
@Column(name="APP_CONSENT_ID")
private Integer appConsentId;
```

Add below it:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Temporal(TemporalType.TIMESTAMP)
@Column(name="APP_PRIVACY_CONSENT_TIME")
private Date appPrivacyConsentTime;
```

`Date`, `Temporal`, and `TemporalType` are already imported in your entity.

---

## 2.2 Add getter/setter

Add near other getter/setter methods:

```java
public String getAppPrivacyConsentFlag() {
	return appPrivacyConsentFlag;
}

public void setAppPrivacyConsentFlag(String appPrivacyConsentFlag) {
	this.appPrivacyConsentFlag = appPrivacyConsentFlag;
}

public String getAppNtbId() {
	return appNtbId;
}

public void setAppNtbId(String appNtbId) {
	this.appNtbId = appNtbId;
}

public Date getAppPrivacyConsentTime() {
	return appPrivacyConsentTime;
}

public void setAppPrivacyConsentTime(Date appPrivacyConsentTime) {
	this.appPrivacyConsentTime = appPrivacyConsentTime;
}
```

---

# 3. `HomeFirstPage.jsp`

You do **not** need to change DOB/mobile fields.

Your actual IDs are:

```javascript
$("#date_of_birth").val()
$("#mobile").val()
```

These come from:

```html
id="date_of_birth"
id="mobile"
```

Add hidden fields inside same form area, preferably before submit button:

```jsp
<s:hidden name="appForm.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="appForm.appNtbId" id="appNtbId" />
```

Example location before:

```jsp
<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
```

Add:

```jsp
<s:hidden name="appForm.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="appForm.appNtbId" id="appNtbId" />

<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
```

---

# 4. `ConsentPopup.jsp`

Your current button has syntax issue:

```html
onclick="savePrivacyConsent(); style="opacity: 0.6; cursor: not-allowed;"
```

This is wrong because `style` went inside `onclick`.

Replace your full button with this:

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="savePrivacyConsent();"
	style="opacity: 0.6; cursor: not-allowed;">
	Accept
</button>
```

---

# 5. Replace full script in `ConsentPopup.jsp`

Replace your current `<script>...</script>` with this cleaned version:

```html
<script>
	function loadPrivacyByLocale(locale) {
		if (locale == null) {
			locale = $("#privacyLocaleDropdown").val();
		}

		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		$("#infoprovide").prop("checked", false);
		$("#infoprovideCBS").prop("checked", false);

		$.ajax({
			url : "getPrivacyNoticeByLocale",
			type : "POST",
			data : {
				privacyLocale : locale
			},
			success : function(response) {
				var json = JSON.parse(response);

				if (json.status == "success") {
					$("#consentHomeLoanDiv").html(json.privacyNotice);
					$("#consentHomeLoanDiv").scrollTop(0);
					resetConsentScrollValidation();
				} else {
					$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
				}
			},
			error : function() {
				$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
			}
		});
	}

	$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	});

	function resetConsentScrollValidation(){
		$("#acceptConsentBtn")
			.prop("disabled", true)
			.css({
				"opacity":"0.6",
				"cursor":"not-allowed"
			});

		$("#consentHomeLoanDiv").off("scroll").on("scroll", function(){
			var div = $(this);

			if(div.scrollTop() + div.innerHeight() >= this.scrollHeight - 5){
				$("#acceptConsentBtn")
					.prop("disabled", false)
					.css({
						"opacity":"1",
						"cursor":"pointer"
					});
			}
		});
	}

	function savePrivacyConsent(){

		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();

		if(mobile == null || $.trim(mobile) == ""){
			alert("Please enter mobile number before accepting consent.");
			return false;
		}

		if(dob == null || $.trim(dob) == ""){
			alert("Please enter date of birth before accepting consent.");
			return false;
		}

		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");

		var ntbId = cleanMobile + cleanDob + loanTypeId;

		$.ajax({
			url : "savePrivacyConsent",
			type : "POST",
			data : {
				ntbId : ntbId
			},
			success : function(response){
				var json = JSON.parse(response);

				if(json.status == "success"){

					$("#appPrivacyConsentFlag").val("Y");
					$("#appNtbId").val(ntbId);

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

					var consentModalEl = document.getElementById("consentHomeLoan");
					var consentModal = bootstrap.Modal.getInstance(consentModalEl);

					if (consentModal != null) {
						consentModal.hide();
					} else {
						$("#consentHomeLoan").modal("hide");
					}

				}else{
					alert(json.message);
				}
			},
			error : function(){
				alert("Unable to save consent. Please try again.");
			}
		});
	}
</script>
```

Important: remove this old block completely because it closes popup without DB save:

```javascript
$(document).on("click", "#acceptConsentBtn", function() {
	...
});
```

Now popup closes only after AJAX success.

---

# 6. `HomeLoanAction.java`

## 6.1 Add import

At top add:

```java
import java.util.Date;
```

You already have:

```java
import java.io.ByteArrayInputStream;
import org.apache.struts2.result.StreamResult;
import org.json.JSONObject;
import org.json.JSONException;
```

---

## 6.2 Add field

Near:

```java
private String privacyLocale;
```

Add:

```java
private String ntbId;
```

---

## 6.3 Add getter/setter

Near bottom of class:

```java
public String getNtbId() {
	return ntbId;
}

public void setNtbId(String ntbId) {
	this.ntbId = ntbId;
}
```

---

## 6.4 Add save method

Add this method near your privacy methods:

```java
public StreamResult savePrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();

		if (appSeqId == null) {
			json.put("status", "fail");
			json.put("message", "Application session not found. Please proceed with application first.");
		} else if (ntbId == null || ntbId.trim().length() == 0) {
			json.put("status", "fail");
			json.put("message", "NTB ID not generated.");
		} else {
			ApplicationFormHomeLoan applicationForm =
					homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);

			if (applicationForm == null) {
				json.put("status", "fail");
				json.put("message", "Application details not found.");
			} else {
				applicationForm.setAppPrivacyConsentFlag("Y");
				applicationForm.setAppNtbId(ntbId);
				applicationForm.setAppPrivacyConsentTime(new Date());

				homeLoanService.save(applicationForm);

				json.put("status", "success");
				json.put("message", "Consent saved successfully.");
			}
		}
	} catch (Exception e) {
		logger.info("Exception in savePrivacyConsent", e);

		try {
			json.put("status", "fail");
			json.put("message", "Unable to save consent.");
		} catch (JSONException je) {
			logger.info("JSONException in savePrivacyConsent", je);
		}
	}

	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}
```

---

# 7. `struts.xml`

Add this action:

```xml
<action name="savePrivacyConsent"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="savePrivacyConsent">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

---

# 8. Important condition

This code will save in DB only if this exists:

```java
SessionUtil.getHomeLoanApplicationSequenceId()
```

If customer opens popup before application row is created, backend will return:

```text
Application session not found. Please proceed with application first.
```

So in your existing flow, make sure consent popup is opened after app sequence is created. If popup is on first page before quote is saved, then you cannot update DB immediately because no `APP_SEQ_ID` row exists yet.
