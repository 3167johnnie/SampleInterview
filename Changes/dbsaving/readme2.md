Below is the complete implementation.

## 1. DB change — Home Loan table

Table from your entity is:

```java
@Table(name = "RUPEEPOWER_OCAS_T_00195")
```

Your entity already has `APP_SEQ_ID`, `APP_MOBILE_NO`, `APP_DATE_OF_BIRTH`, and `APP_CONSENT_ID`. 

Run:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_NTB_ID VARCHAR2(100);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_TIME DATE;
```

---

## 2. `ApplicationFormHomeLoan.java`

Add these fields below:

```java
@Column(name="APP_CONSENT_ID")
private Integer appConsentId;
```

Add:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Temporal(TemporalType.TIMESTAMP)
@Column(name="APP_PRIVACY_CONSENT_TIME")
private Date appPrivacyConsentTime;
```

Add getters/setters near bottom:

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

## 3. `HomeFirstPage.jsp`

Inside your main form, add hidden fields:

```jsp
<s:hidden name="appForm.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="appForm.appNtbId" id="appNtbId" />
```

---

## 4. `ConsentPopup.jsp`

Accept button should be:

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();">
	Accept
</button>
```

Your content div should have scrollable height:

```html
<div id="consentHomeLoanDiv"
	style="max-height:300px; overflow-y:auto;">
</div>
```

---

## 5. `ConsentPopup.jsp` JavaScript

Add this full JS:

```javascript
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
```

Inside `loadPrivacyByLocale()` success, after:

```javascript
$("#consentHomeLoanDiv").scrollTop(0);
```

Add:

```javascript
resetConsentScrollValidation();
```

Now add Accept click logic:

```javascript
function acceptPrivacyConsent(){
	var mobile = $("#appMobileNo").val();
	var dob = $("#appDob").val();
	var loanType = "1"; // replace with actual HOME_LOAN_ID if different

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

	var ntbId = cleanMobile + cleanDob + loanType;

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
				$("#consentHomeLoan").modal("hide");
			}else{
				alert(json.message);
			}
		},
		error : function(){
			alert("Unable to save consent. Please try again.");
		}
	});
}
```

---

## 6. `HomeLoanAction.java`

Add import:

```java
import java.util.Date;
```

Add field:

```java
private String ntbId;
```

Add getter/setter:

```java
public String getNtbId() {
	return ntbId;
}

public void setNtbId(String ntbId) {
	this.ntbId = ntbId;
}
```

Add method:

```java
public StreamResult savePrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();

		if (appSeqId == null) {
			json.put("status", "fail");
			json.put("message", "Application session not found. Please save application first.");
		} else {
			ApplicationFormHomeLoan appForm =
					homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);

			if (appForm == null) {
				json.put("status", "fail");
				json.put("message", "Application not found.");
			} else {
				appForm.setAppPrivacyConsentFlag("Y");
				appForm.setAppNtbId(ntbId);
				appForm.setAppPrivacyConsentTime(new Date());

				homeLoanService.save(appForm);

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

## 7. `struts.xml`

Add:

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

## Important point

This works only when this value exists:

```java
SessionUtil.getHomeLoanApplicationSequenceId()
```

If customer accepts popup **before first page is saved**, DB update cannot happen because row does not exist yet.

Correct standard flow:

```text
First save/create application row
Set appSeqId in session
Open consent popup
Accept click
AJAX updates consent flag + ntbId
Close popup
```
