Create one common utility method and use it from every loan flow.

## 1. Add common utility class

Create:

```java
package com.mintstreet.consent.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.mintstreet.common.validation.ValidatorUtil;

public final class ConsentNtbIdUtil {

	private static final String NTB_TIMESTAMP_FORMAT =
			"yyyyMMddHHmmssSSS";

	private ConsentNtbIdUtil() {
		/*
		 * Utility class. Object creation is not required.
		 */
	}

	/**
	 * Generates NTB ID in this format:
	 *
	 * MobileNumber + Timestamp + LoanTypeId
	 *
	 * Example:
	 * 9619155147 + 20260723153045123 + 8
	 *
	 * Result:
	 * 9619155147202607231530451238
	 */
	public static String generateNtbId(
			String mobileNumber,
			Integer loanTypeId) {

		if (!ValidatorUtil.isValid(mobileNumber)) {
			throw new IllegalArgumentException(
					"Mobile number is mandatory for NTB ID generation"
			);
		}

		if (loanTypeId == null) {
			throw new IllegalArgumentException(
					"Loan type ID is mandatory for NTB ID generation"
			);
		}

		String cleanMobileNumber =
				mobileNumber.replaceAll("[^0-9]", "");

		if (!ValidatorUtil.isValid(cleanMobileNumber)) {
			throw new IllegalArgumentException(
					"Valid mobile number is mandatory for NTB ID generation"
			);
		}

		String timestamp =
				new SimpleDateFormat(
						NTB_TIMESTAMP_FORMAT
				).format(new Date());

		return cleanMobileNumber
				+ timestamp
				+ loanTypeId;
	}
}
```

---

## 2. Recommended thread-safe Java 8 version

Because this is production code, prefer `DateTimeFormatter`, which is immutable and thread-safe.

Replace the preceding implementation with:

```java
package com.mintstreet.consent.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mintstreet.common.validation.ValidatorUtil;

public final class ConsentNtbIdUtil {

	private static final DateTimeFormatter NTB_TIMESTAMP_FORMATTER =
			DateTimeFormatter.ofPattern(
					"yyyyMMddHHmmssSSS"
			);

	private ConsentNtbIdUtil() {
		/*
		 * Utility class. Object creation is not required.
		 */
	}

	/**
	 * Generates:
	 *
	 * MobileNumber + yyyyMMddHHmmssSSS + LoanTypeId
	 */
	public static String generateNtbId(
			String mobileNumber,
			Integer loanTypeId) {

		if (!ValidatorUtil.isValid(mobileNumber)) {
			throw new IllegalArgumentException(
					"Mobile number is mandatory for NTB ID generation"
			);
		}

		if (loanTypeId == null) {
			throw new IllegalArgumentException(
					"Loan type ID is mandatory for NTB ID generation"
			);
		}

		String cleanMobileNumber =
				mobileNumber.replaceAll("[^0-9]", "");

		if (!ValidatorUtil.isValid(cleanMobileNumber)) {
			throw new IllegalArgumentException(
					"Valid mobile number is mandatory for NTB ID generation"
			);
		}

		String timestamp =
				LocalDateTime.now().format(
						NTB_TIMESTAMP_FORMATTER
				);

		return cleanMobileNumber
				+ timestamp
				+ loanTypeId;
	}
}
```

Use this version.

---

# 3. Generate NTB ID only in backend

Do not rely on this JavaScript-generated value:

```javascript
var ntbId = cleanMobile + cleanDob + loanTypeValue;
```

Frontend values can be modified by the customer.

The frontend may continue sending the hidden NTB field for compatibility, but the backend must overwrite it with a newly generated value before the first application save.

---

# 4. Update `ConsentPopup.jsp`

Inside `acceptPrivacyConsent()`, replace:

```javascript
var cleanMobile = $.trim(mobile).replace(/\D/g, "");
var cleanDob = "";

if($("#date_of_birth").length > 0){
	cleanDob = $.trim($("#date_of_birth").val()).replace(/\D/g, "");
}

var loanTypeValue = "";

if(typeof loanTypeId !== "undefined" && loanTypeId != null){
	loanTypeValue = loanTypeId;
}

var ntbId = cleanMobile + cleanDob + loanTypeValue;
```

With:

```javascript
/*
 * Final NTB ID is generated securely in Java backend.
 * This temporary frontend value is only used to mark that
 * consent was accepted.
 */
var ntbId = "PENDING_BACKEND_GENERATION";
```

Keep:

```javascript
if($("#quoteNtbId").length > 0){
	$("#quoteNtbId").val(ntbId);
}

if($("#appNtbId").length > 0){
	$("#appNtbId").val(ntbId);
}
```

The backend will overwrite this value.

A cleaner alternative is to stop setting NTB ID entirely from JavaScript:

```javascript
if($("#quoteNtbId").length > 0){
	$("#quoteNtbId").val("");
}

if($("#appNtbId").length > 0){
	$("#appNtbId").val("");
}
```

Then remove frontend validation requiring nonblank NTB ID.

---

# 5. Update frontend submit validation

Remove this validation from CVE/Home/Auto/Personal JSP:

```javascript
if($.trim($("#appNtbId").val()) == ""){
	alert("Invalid consent details. Please accept SBI Privacy Notice again.");
	return false;
}
```

Also remove quote-side validation such as:

```javascript
if($.trim($("#quoteNtbId").val()) == ""){
	alert("Invalid consent details. Please accept SBI Privacy Notice again.");
	return false;
}
```

NTB ID is now generated only on the server.

Keep validation for:

```javascript
consentFlag == "Y"
locale != blank
checkbox checked
```

---

# 6. Home Loan backend change

Add import in `HomeProcessManagerImpl.java`:

```java
import com.mintstreet.consent.util.ConsentNtbIdUtil;
```

Before saving the quote or before calling CCMS, generate:

```java
String mobileNumber =
		ValidatorUtil.isValid(
				quote.getQuoteMobileNo())
				? quote.getQuoteMobileNo()
				: application.getAppMobileNo();

String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				mobileNumber,
				Constants.HOME_LOAN_ID
		);

quote.setQuoteNtbId(generatedNtbId);
application.setAppNtbId(generatedNtbId);
```

Then use:

```java
consentInput.setNtbId(generatedNtbId);
```

Do not use:

```java
consentInput.setNtbId(
		quote.getQuoteNtbId()
);
```

until after the backend has overwritten it.

---

# 7. CVE backend change

Add import in `CveProcessManagerImpl.java`:

```java
import com.mintstreet.consent.util.ConsentNtbIdUtil;
```

After the mobile number is available and before:

```java
cveApp = personalLoanService.save(cveApp);
```

add:

```java
String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				masterCbsCall.getCbsMobileNumber(),
				Constants.CVE_ID
		);

cveApp.setAppNtbId(generatedNtbId);
```

Then prepare consent request using the same value:

```java
consentInput.setNtbId(generatedNtbId);
```

Full CVE insertion block:

```java
cveApp.setAppPrivacyConsentFlag(
		"Y".equalsIgnoreCase(
				masterCbsCall
						.getAppPrivacyConsentFlag())
				? "Y"
				: "N"
);

if (ValidatorUtil.isValid(
		masterCbsCall.getAppPrivacyLocale())) {

	cveApp.setAppPrivacyLocale(
			masterCbsCall.getAppPrivacyLocale()
	);
} else {
	cveApp.setAppPrivacyLocale("eng");
}

String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				masterCbsCall.getCbsMobileNumber(),
				Constants.CVE_ID
		);

cveApp.setAppNtbId(generatedNtbId);

cveApp =
		personalLoanService.save(cveApp);

if (cveApp == null) {
	cbsCallResponse.setResponseMsg(
			Constants.SORRY_FOR_INCONVENIENCE
	);
	cbsCallResponse.setStatus(0);
	return cbsCallResponse;
}
```

Later:

```java
ConsentWriteInput consentInput =
		new ConsentWriteInput();

consentInput.setNtbId(
		generatedNtbId
);

consentInput.setMobile(
		cveApp.getCbsMobileNumber()
);

consentInput.setEmail(
		cveApp.getCveAppEmail()
);

consentInput.setIpAddress(
		this.SbiUtil.getIPAddress()
);

consentInput.setLocale(
		cveApp.getAppPrivacyLocale()
);

consentInput.setProductCode(
		cveApp.getCveProductCategory()
);

consentInput.setConsentFlag(
		cveApp.getAppPrivacyConsentFlag()
);

consentInput.setLoanTypeId(
		Constants.CVE_ID
);
```

---

# 8. Auto Loan backend change

Add import:

```java
import com.mintstreet.consent.util.ConsentNtbIdUtil;
```

Before quote/application save or before CCMS call:

```java
String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				autoQuote.getQuoteMobileNo(),
				Constants.AUTO_LOAN_ID
		);

autoQuote.setQuoteNtbId(
		generatedNtbId
);

autoApplication.setAppNtbId(
		generatedNtbId
);

consentInput.setNtbId(
		generatedNtbId
);
```

---

# 9. Personal Loan backend change

Add import:

```java
import com.mintstreet.consent.util.ConsentNtbIdUtil;
```

Generate:

```java
String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				personalQuote.getQuoteMobileNo(),
				Constants.PERSONAL_LOAN_ID
		);

personalQuote.setQuoteNtbId(
		generatedNtbId
);

personalApplication.setAppNtbId(
		generatedNtbId
);

consentInput.setNtbId(
		generatedNtbId
);
```

---

# 10. Other loan types

For every other process manager:

```java
String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				mobileNumber,
				loanTypeId
		);

quote.setQuoteNtbId(generatedNtbId);
application.setAppNtbId(generatedNtbId);

consentInput.setNtbId(generatedNtbId);
consentInput.setLoanTypeId(loanTypeId);
```

For one-table products:

```java
String generatedNtbId =
		ConsentNtbIdUtil.generateNtbId(
				mobileNumber,
				loanTypeId
		);

application.setAppNtbId(generatedNtbId);
consentInput.setNtbId(generatedNtbId);
```

---

# 11. Example output

Input:

```java
String mobile = "9619155147";
Integer loanTypeId = 8;
```

At:

```text
2026-07-23 15:30:45.123
```

Generated timestamp:

```text
20260723153045123
```

Final NTB ID:

```text
9619155147202607231530451238
```

Format:

```text
9619155147
+
20260723153045123
+
8
```

---

# 12. Add DB uniqueness protection

Application logic alone cannot guarantee uniqueness under simultaneous requests. Add a unique constraint on the Consent Write Log:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_13701
ADD CONSTRAINT UK_CCMS_WRITE_NTB_ID
UNIQUE (CONSENT_WRITE_NTB_ID);
```

Add similar constraints only where each application table must independently prevent duplicates:

```sql
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00291
ADD CONSTRAINT UK_CVE_APP_NTB_ID
UNIQUE (APP_NTB_ID);
```

For quote/application tables, the same NTB ID is intentionally stored in both tables, so each table may have its own unique constraint, but do not create a cross-table uniqueness assumption.

---

## Important uniqueness limitation

`mobile + millisecond timestamp + loanTypeId` can theoretically collide when two requests for the same mobile and loan type are processed within the same millisecond.

The database unique constraint is necessary. For automatic retry, use:

```java
public static String generateNtbId(
		String mobileNumber,
		Integer loanTypeId,
		int retryCounter) {

	String baseNtbId =
			generateNtbId(
					mobileNumber,
					loanTypeId
			);

	return retryCounter > 0
			? baseNtbId + retryCounter
			: baseNtbId;
}
```

However, since your required format is strictly:

```text
mobile + yyyyMMddHHmmssSSS + loanTypeId
```

keep the original method and enforce uniqueness using the database constraint.
