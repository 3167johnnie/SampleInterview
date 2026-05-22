Yes — this OLD code SHOULD be removed/changed for your new multilingual privacy flow.

Your current code:

```java
Integer consentId = commonService
	.getConsentByLoanType(Constants.HOME_LOAN_ID)
	.getConsentId();

String consentText = commonService.getClobdata(
	"RUPEEPOWER_OCAS_T_13689",
	"CONSENT_TEXT",
	"CONSENT_ID",
	consentId
);

beanList.setConsentHomeLoan(consentText);
```

was built for OLD architecture:

```text
CONSENT TABLE
CONSENT_TEXT (CLOB)
CONSENT_ID
LOAN_TYPE
```

But NOW your new table/entity is:

```text
PRIVACY_NOTICE
PRIVACY_LOCALE
```

and:

✅ NO CLOB
✅ NO CONSENT_ID
✅ NO LOAN TYPE
✅ NO TOUCHPOINT
✅ NO getClobdata()

---

# WHAT YOU SHOULD DO NOW

You should REPLACE that old code completely.

---

# FIND THIS CODE

Inside:

```java
HomeLoanAction.java
```

Search:

```java
beanList.setConsentHomeLoan(consentText);
```

OR search:

```java
getConsentByLoanType
```

---

# REMOVE THIS ENTIRE BLOCK

```java
Integer consentId = commonService
	.getConsentByLoanType(Constants.HOME_LOAN_ID)
	.getConsentId();

String consentText = commonService.getClobdata(
	"RUPEEPOWER_OCAS_T_13689",
	"CONSENT_TEXT",
	"CONSENT_ID",
	consentId
);

beanList.setConsentHomeLoan(consentText);
```

---

# REPLACE WITH THIS NEW CODE

```java
PrivacyRequestResponse privacyObj =
		commonService.getPrivacyByLocale("eng");

if(privacyObj != null){

	beanList.setConsentHomeLoan(
			privacyObj.getPrivacyNotice()
	);

}else{

	beanList.setConsentHomeLoan(
			"Privacy Notice Not Available"
	);
}
```

---

# WHY THIS CHANGE IS REQUIRED

Because your JSP:

```jsp
<s:property escapeHtml="false"
	value="%{beanList.consentHomeLoan}" />
```

still depends on:

```java
beanList.setConsentHomeLoan(...)
```

So:

## OLD FLOW

```text
DB CLOB
    ↓
getClobdata()
    ↓
consentText
    ↓
beanList.setConsentHomeLoan()
```

---

# NEW FLOW

```text
PRIVACY_NOTICE
    ↓
privacyObj.getPrivacyNotice()
    ↓
beanList.setConsentHomeLoan()
```

---

# VERY IMPORTANT

This line SHOULD REMAIN SAME in JSP:

```jsp
<s:property escapeHtml="false"
	value="%{beanList.consentHomeLoan}" />
```

DO NOT REMOVE IT.

Because:

* first popup load uses this
* English content loads initially from backend
* later AJAX replaces content dynamically

---

# FINAL FLOW NOW

# INITIAL PAGE LOAD

```text
HomeLoanAction.java
    ↓
getPrivacyByLocale("eng")
    ↓
beanList.setConsentHomeLoan()
    ↓
JSP popup shows English
```

---

# AFTER USER CHANGES LANGUAGE

```text
Dropdown onchange
    ↓
AJAX call
    ↓
getPrivacyNoticeByLocale()
    ↓
DB fetch by PRIVACY_LOCALE
    ↓
$("#consentHomeLoanDiv").html(...)
```

---

# SO YES

✅ This old consent code MUST change
✅ getClobdata() no longer needed
✅ consentId no longer needed
✅ loan type no longer needed
✅ CLOB handling no longer needed
✅ only PRIVACY_NOTICE needed now

