After seeing your actual table structure, my recommendation is **do NOT store all these fields in `Master_App_Form (RUPEEPOWER_OCAS_T_00195)`**.

You should only store the values that are part of the application lifecycle.

From your schema:

* `RUPEEPOWER_OCAS_T_00195` → Master Application (`APP_FORM_HOME_LOAN`)
* `RUPEEPOWER_OCAS_T_00255` → Quote (`APP_FORM_HOME_LOAN_QUOTE`)

And I can already see that you have:

```sql
APP_PRIVACY_CONSENT_FLAG CHAR(1)
APP_NTB_ID VARCHAR2(255)
```

in the Master table. 

And in Quote table you already have:

```sql
QUOTE_PRIVACY_CONSENT_FLAG CHAR(1)
QUOTE_NTB_ID VARCHAR2(255)
```

also present. 

---

# Should all 7 fields be stored in Master App Form?

You asked about:

```java
private String ntbId;
private String mobile;
private String email;
private String ipAddress;
private String locale;
private String productCode;
private String consentFlag;
```

The answer is **No**.

Only some of these belong in `RUPEEPOWER_OCAS_T_00195`.

| Field       | Store in APP_FORM? | Reason                                              |
| ----------- | ------------------ | --------------------------------------------------- |
| ntbId       | ✅ YES              | Needed later for CCMS Read/Write                    |
| mobile      | ❌ NO               | Already exists as `APP_MOBILE_NO`                   |
| email       | ❌ NO               | Already exists as `APP_EMAIL_ID` / `APP_WORK_EMAIL` |
| ipAddress   | ❌ NO               | Transient API value                                 |
| locale      | ✅ YES              | Useful audit information                            |
| productCode | ❌ NO               | Can derive from Product Variant                     |
| consentFlag | ✅ YES              | Already exists                                      |

---

# What should exist in Master App Form?

I would recommend:

```sql
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
```

So alter:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_LOCALE VARCHAR2(10);
```

Then Master App becomes:

```java
private String appPrivacyConsentFlag;

private String appNtbId;

private String appPrivacyLocale;
```

---

# What should exist in Quote table?

Quote is first page.

Quote should capture:

```java
private String quotePrivacyConsentFlag;

private String quoteNtbId;

private String quotePrivacyLocale;
```

Because user accepts consent **before Get Quote**.

Then after application is created:

```text
QUOTE
↓ copy values
MASTER APP
↓ call CCMS
CONSENT_WRITE_LOG
```

---

# During CCMS call

You DO NOT read everything from new columns.

Instead construct dynamically.

Example:

```java
ConsentWriteInput input = new ConsentWriteInput();

input.setNtbId(application.getAppNtbId());

input.setMobile(application.getAppMobileNo());

input.setEmail(
    ValidatorUtil.isValid(application.getAppWorkEmail())
        ? application.getAppWorkEmail()
        : application.getAppEmailId()
);

input.setIpAddress(
    SbiUtil.getIPAddress()
);

input.setLocale(
    application.getAppPrivacyLocale()
);

input.setProductCode(
    productCode
);

input.setConsentFlag(
    application.getAppPrivacyConsentFlag()
);
```

---

# Where each value should come from

| CCMS Request Field | Source                        |
| ------------------ | ----------------------------- |
| ntbId              | APP_NTB_ID                    |
| dpMobile           | APP_MOBILE_NO                 |
| dpEmail            | APP_EMAIL_ID / APP_WORK_EMAIL |
| dpIPAddress        | Runtime request               |
| locale             | APP_PRIVACY_LOCALE            |
| purpose products   | Derived from Loan Type        |
| consented          | APP_PRIVACY_CONSENT_FLAG      |

---

# Product Code should NOT be stored

For example:

```java
HOME_LOAN
AUTO_LOAN
PERSONAL_LOAN
EDUCATION_LOAN
```

can come from:

```java
application.getAppProductVariantId()
```

or

```java
loanTypeId
```

through mapping.

Example:

```java
private String getProductCode(Integer loanTypeId){

    switch(loanTypeId){

        case 1:
            return "HOME_LOAN";

        case 2:
            return "AUTO_LOAN";

        case 3:
            return "PERSONAL_LOAN";

        default:
            return "UNKNOWN";
    }
}
```

No DB column needed.

---

# Mobile and Email should NOT be duplicated

Because you already have:

Master App:

```sql
APP_MOBILE_NO
APP_EMAIL_ID
APP_WORK_EMAIL
```

Quote:

```sql
QUOTE_APP_MOBILE
QUOTE_APP_EMAIL
```

So adding:

```sql
APP_CONSENT_MOBILE
APP_CONSENT_EMAIL
```

would be bad database design.

---

# Final recommendation

### Quote Table (`00255`)

Keep:

```sql
QUOTE_PRIVACY_CONSENT_FLAG
QUOTE_NTB_ID
QUOTE_PRIVACY_LOCALE
```

---

### Master App (`00195`)

Keep:

```sql
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
```

---

### Consent Write Log (`13701`)

Keep:

```sql
CONSENT_WRITE_X_CORRELATION_ID
CONSENT_WRITE_REQUEST
CONSENT_WRITE_RESPONSE_CODE
CONSENT_WRITE_RESPONSE
CONSENT_WRITE_RESPONSE_STATUS
```

This becomes your audit trail.

---

So the answer is:

> **No, all seven fields should not be added to Master_App_Form.**
>
> Only these three should persist:
>
> ```java
> private String appNtbId;
> private String appPrivacyConsentFlag;
> private String appPrivacyLocale;
> ```
>
> Everything else (`mobile`, `email`, `ipAddress`, `productCode`) should be derived at runtime when the CCMS Write API is invoked. This avoids duplication and keeps the design common across Home, Auto, Personal, and future loan products.
--------------------------------------------------------------------------






Yes. For both tables, keep/add only these consent columns.

Do **not** add:

```text
mobile
email
ipAddress
productCode
```

because those already exist or can be derived.

## 1) Quote table

`RUPEEPOWER_OCAS_T_00255`

Already present:

```sql
QUOTE_PRIVACY_CONSENT_FLAG
QUOTE_NTB_ID
```

Add only if not present:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);
```

## 2) Master app table

`RUPEEPOWER_OCAS_T_00195`

Already present:

```sql
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
```

Add only if not present:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_LOCALE VARCHAR2(20);
```

## Final columns

Quote table:

```text
QUOTE_PRIVACY_CONSENT_FLAG
QUOTE_NTB_ID
QUOTE_PRIVACY_LOCALE
```

Master app table:

```text
APP_PRIVACY_CONSENT_FLAG
APP_NTB_ID
APP_PRIVACY_LOCALE
```

For CCMS request:

```text
mobile      → APP_MOBILE_NO / QUOTE_APP_MOBILE
email       → APP_WORK_EMAIL / APP_EMAIL_ID / QUOTE_APP_EMAIL
ipAddress   → runtime from request
productCode → derived from loanTypeId
```

No extra columns needed for those.
