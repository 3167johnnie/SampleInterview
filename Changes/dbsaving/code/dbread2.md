Yes. Before `appSeqId` is created, you have **2 practical options**.

## Best option for your case

Store consent temporarily in **session**, then save it into DB when `appSeqId` is created.

Flow:

```text
Accept clicked
→ generate ntbId from frontend mobile + DOB + loanType
→ AJAX sends ntbId to Action
→ Action stores in SessionUtil
→ popup closes
→ later when application row is saved and appSeqId is created
→ take ntbId from session
→ set appPrivacyConsentFlag = Y
→ set appNtbId = session ntbId
→ save application
```

---

## Option 1: Store in session first

### In `SessionUtil.java`

Add methods:

```java
public static void setPrivacyConsentFlag(String flag) {
	getSession().setAttribute("PRIVACY_CONSENT_FLAG", flag);
}

public static String getPrivacyConsentFlag() {
	Object obj = getSession().getAttribute("PRIVACY_CONSENT_FLAG");
	return obj != null ? obj.toString() : null;
}

public static void setPrivacyConsentNtbId(String ntbId) {
	getSession().setAttribute("PRIVACY_CONSENT_NTB_ID", ntbId);
}

public static String getPrivacyConsentNtbId() {
	Object obj = getSession().getAttribute("PRIVACY_CONSENT_NTB_ID");
	return obj != null ? obj.toString() : null;
}
```

---

## In `HomeLoanAction.java`

Change `savePrivacyConsent()` to this:

```java
public StreamResult savePrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		if (ntbId == null || ntbId.trim().length() == 0) {
			json.put("status", "fail");
			json.put("message", "NTB ID not generated.");
		} else {
			Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();

			if (appSeqId == null) {
				SessionUtil.setPrivacyConsentFlag("Y");
				SessionUtil.setPrivacyConsentNtbId(ntbId);

				json.put("status", "success");
				json.put("message", "Consent captured in session.");
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

---

## Then where application is finally saved

Wherever you have:

```java
homeLoanService.save(appForm);
```

Before save, add:

```java
if ("Y".equalsIgnoreCase(SessionUtil.getPrivacyConsentFlag())) {
	appForm.setAppPrivacyConsentFlag("Y");
	appForm.setAppNtbId(SessionUtil.getPrivacyConsentNtbId());
	appForm.setAppPrivacyConsentTime(new Date());
}
```

So final save becomes:

```java
if ("Y".equalsIgnoreCase(SessionUtil.getPrivacyConsentFlag())) {
	appForm.setAppPrivacyConsentFlag("Y");
	appForm.setAppNtbId(SessionUtil.getPrivacyConsentNtbId());
	appForm.setAppPrivacyConsentTime(new Date());
}

homeLoanService.save(appForm);
```

---

## Why session is best here

Because before `appSeqId`, there is no row in:

```sql
RUPEEPOWER_OCAS_T_00195
```

So DB cannot update this:

```sql
WHERE APP_SEQ_ID = ?
```

until the application is created.

Session keeps consent temporarily and saves it when the application record exists.
