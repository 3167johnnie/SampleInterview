Your `ApplicationFormHomeLoan` entity maps to **RUPEEPOWER_OCAS_T_00195**, not `RUPEEPOWER_OCAS_T_13703`. So consent flag + NTB ID should be stored in **RUPEEPOWER_OCAS_T_00195** for Home Loan. Your entity already has `APP_MOBILE_NO`, `APP_DATE_OF_BIRTH`, `APP_CONSENT_ID`, and `APP_SEQ_ID`. 

## 1. DB changes — Home Loan table

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

Add below existing:

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

Add getter/setter near bottom:

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

## 3. `HomeLoanAction.java`

Add import:

```java
import java.text.SimpleDateFormat;
import java.util.Date;
```

Add this AJAX method near your other privacy methods:

```java
public StreamResult savePrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();

		if (appSeqId == null) {
			json.put("status", "fail");
			json.put("message", "Session expired. Application not found.");
		} else {
			ApplicationFormHomeLoan appForm =
					homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);

			if (appForm == null) {
				json.put("status", "fail");
				json.put("message", "Application not found.");
			} else {
				String ntbId = generateHomeLoanNtbId(appForm);

				appForm.setAppPrivacyConsentFlag("Y");
				appForm.setAppNtbId(ntbId);
				appForm.setAppPrivacyConsentTime(new Date());

				homeLoanService.save(appForm);

				json.put("status", "success");
				json.put("message", "Consent saved successfully.");
				json.put("ntbId", ntbId);
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
			new ByteArrayInputStream(json.toString().getBytes())
	);
}
```

Add helper method in same action:

```java
private String generateHomeLoanNtbId(ApplicationFormHomeLoan appForm) {
	String mobile = appForm.getAppMobileNo() != null
			? appForm.getAppMobileNo()
			: "";

	String dob = "";

	try {
		if (appForm.getAppDobDT() != null) {
			dob = new SimpleDateFormat("ddMMyyyy")
					.format(appForm.getAppDobDT());
		}
	} catch (Exception e) {
		logger.info("Exception while formatting DOB for NTB ID", e);
	}

	String loanType = String.valueOf(Constants.HOME_LOAN_ID);

	return mobile + dob + loanType;
}
```

---

## 4. `struts.xml`

Add action:

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

## 5. JSP button

Your accept button should be disabled first:

```html
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="savePrivacyConsent();">
	Accept
</button>
```

---

## 6. JSP JavaScript — force scroll till end

Add this after privacy notice loads:

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

Inside your `loadPrivacyByLocale()` success, after:

```javascript
$("#consentHomeLoanDiv").scrollTop(0);
```

Add:

```javascript
resetConsentScrollValidation();
```

---

## 7. JSP JavaScript — save consent on Accept

Add:

```javascript
function savePrivacyConsent(){
	$.ajax({
		url : "savePrivacyConsent",
		type : "POST",
		success : function(response){
			var json = JSON.parse(response);

			if(json.status == "success"){
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

For **all loans**, same logic must be repeated in each loan entity/table:

```text
ApplicationFormHomeLoan      → RUPEEPOWER_OCAS_T_00195
ApplicationFormAutoLoan      → Auto loan table
ApplicationFormPersonalLoan  → Personal loan table
ApplicationFormEducationLoan → Education loan table
ApplicationFormAgriLoan      → Agri loan table
```

For each loan, NTB ID logic remains:

```java
mobile + DOB + loanType
```
