Below are the exact changes.

## Part 1: Force user to scroll and Accept before Submit

### File 1: `ConsentPopup.jsp`

You already have scroll + accept working. Add one hidden flag.

Find:

```jsp
<div id="consentHomeLoanDiv" class="f-otp-pop-content" style="max-height: 400px; overflow-y: auto;">Loading Privacy Notice...</div>
```

Add this hidden field immediately after it:

```jsp
<input type="hidden" id="consentAcceptedFlag" name="consentAcceptedFlag" value="N">
<input type="hidden" id="consentAcceptedLocale" name="consentAcceptedLocale" value="">
```

---

Find inside `loadPrivacyByLocale(locale)`:

```javascript
$("#infoprovide").prop("checked", false);
$("#infoprovideCBS").prop("checked", false);
```

Add below it:

```javascript
$("#consentAcceptedFlag").val("N");
$("#consentAcceptedLocale").val("");
```

---

Find Accept button click:

```javascript
$(document).on("click", "#acceptConsentBtn", function() {
```

Inside it, after checkbox checked code:

```javascript
$("#infoprovide")
	.prop("disabled", false)
	.prop("checked", true);
```

Add:

```javascript
$("#consentAcceptedFlag").val("Y");
$("#consentAcceptedLocale").val($("#privacyLocaleDropdown").val());
```

Final Accept block should include:

```javascript
$(document).on("click", "#acceptConsentBtn", function() {

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

	$("#consentAcceptedFlag").val("Y");
	$("#consentAcceptedLocale").val($("#privacyLocaleDropdown").val());

	var consentModalEl = document.getElementById("consentHomeLoan");
	var consentModal = bootstrap.Modal.getInstance(consentModalEl);

	if (consentModal != null) {
		consentModal.hide();
	} else {
		$("#consentHomeLoan").modal("hide");
	}
});
```

---

### File 2: `HomeFirstPageSession.jsp`

Find:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">
```

Replace with:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">
```

Add hidden fields inside `homeloanform`, near checkbox area:

```jsp
<input type="hidden" name="privacyConsentAccepted" id="privacyConsentAccepted" value="N">
<input type="hidden" name="privacyConsentLocale" id="privacyConsentLocale" value="">
```

Then in Accept click from `ConsentPopup.jsp`, also add:

```javascript
$("#privacyConsentAccepted").val("Y");
$("#privacyConsentLocale").val($("#privacyLocaleDropdown").val());
```

So Accept click should set both popup and form hidden values.

---

### File 3: `homeLoanSbi.js`

Find inside `submit_first_page_bind()`:

```javascript
if($("form#homeloanform").valid()){
```

Immediately after it, add:

```javascript
	if(!$("#infoprovide").is(":checked")
			|| $("#privacyConsentAccepted").val() != "Y"){

		showMsg(
			"<em>Please click Read More, scroll till the end and accept the consent before proceeding.</em>",
			false
		);

		return false;
	}
```

---

## Part 2: Save consent in DB against lead/application

### Recommended table

Create a new table:

```sql
CREATE TABLE RUPEEPOWER_OCAS_T_CONSENT_LOG (
	CONSENT_LOG_ID NUMBER PRIMARY KEY,
	APP_SEQ_ID NUMBER,
	LEAD_ID NUMBER,
	LOAN_TYPE_ID NUMBER,
	PRIVACY_LOCALE VARCHAR2(20),
	CONSENT_ACCEPTED VARCHAR2(1),
	CONSENT_ACCEPTED_ON TIMESTAMP,
	CONSENT_TEXT CLOB
);
```

Sequence:

```sql
CREATE SEQUENCE RUPEEPOWER_OCAS_T_CONSENT_LOG_SEQ
START WITH 1
INCREMENT BY 1;
```

---

### Entity: `ConsentLog.java`

Create:

```java
package com.mintstreet.consent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_CONSENT_LOG")
@SequenceGenerator(
	name="RUPEEPOWER_OCAS_T_CONSENT_LOG_SEQ",
	sequenceName="RUPEEPOWER_OCAS_T_CONSENT_LOG_SEQ",
	allocationSize=1
)
public class ConsentLog implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(
		strategy=GenerationType.SEQUENCE,
		generator="RUPEEPOWER_OCAS_T_CONSENT_LOG_SEQ"
	)
	@Column(name="CONSENT_LOG_ID")
	private Integer consentLogId;

	@Column(name="APP_SEQ_ID")
	private Integer appSeqId;

	@Column(name="LEAD_ID")
	private Integer leadId;

	@Column(name="LOAN_TYPE_ID")
	private Integer loanTypeId;

	@Column(name="PRIVACY_LOCALE")
	private String privacyLocale;

	@Column(name="CONSENT_ACCEPTED")
	private String consentAccepted;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CONSENT_ACCEPTED_ON")
	private Date consentAcceptedOn;

	@Lob
	@Column(name="CONSENT_TEXT")
	private String consentText;

	// getters and setters
}
```

---

### DAO: `ConsentLogDao.java`

```java
package com.mintstreet.consent.dao;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.ConsentLog;

public class ConsentLogDao
	extends GenericDao<Integer, ConsentLog> {

	private static final long serialVersionUID = 1L;
}
```

---

### `CommonService.java`

Add DAO:

```java
private ConsentLogDao consentLogDao;

public ConsentLogDao getConsentLogDao() {
	return consentLogDao;
}

public void setConsentLogDao(ConsentLogDao consentLogDao) {
	this.consentLogDao = consentLogDao;
}
```

Add service method:

```java
public ConsentLog saveConsentLog(ConsentLog consentLog){
	return consentLogDao.save(consentLog);
}
```

---

### Spring XML

Add DAO bean:

```xml
<bean id="consentLogDao"
	class="com.mintstreet.consent.dao.ConsentLogDao"/>
```

Add property inside `commonService` bean:

```xml
<property name="consentLogDao" ref="consentLogDao"/>
```

---

### `HomeLoanAction.java`

Add variables:

```java
private String privacyConsentAccepted;
private String privacyConsentLocale;
```

Getters/setters:

```java
public String getPrivacyConsentAccepted() {
	return privacyConsentAccepted;
}

public void setPrivacyConsentAccepted(String privacyConsentAccepted) {
	this.privacyConsentAccepted = privacyConsentAccepted;
}

public String getPrivacyConsentLocale() {
	return privacyConsentLocale;
}

public void setPrivacyConsentLocale(String privacyConsentLocale) {
	this.privacyConsentLocale = privacyConsentLocale;
}
```

Before saving/submitting quote, validate:

```java
if(!"Y".equalsIgnoreCase(privacyConsentAccepted)){
	responseMessage = "error|Please read and accept the consent before proceeding.";
	return "jsonResponsePage";
}
```

After application/lead is created and `appSeqId` is available, save log:

```java
PrivacyRequestResponse privacyObj =
	commonService.getPrivacyByLocale(
		privacyConsentLocale != null ? privacyConsentLocale : "eng"
	);

ConsentLog consentLog = new ConsentLog();

consentLog.setAppSeqId(appSeqId);
consentLog.setLeadId(appLeadId);
consentLog.setLoanTypeId(Constants.HOME_LOAN_ID);
consentLog.setPrivacyLocale(
	privacyConsentLocale != null ? privacyConsentLocale : "eng"
);
consentLog.setConsentAccepted("Y");
consentLog.setConsentAcceptedOn(new Date());

if(privacyObj != null){
	consentLog.setConsentText(
		privacyObj.getPrivacyNotice()
	);
}

commonService.saveConsentLog(consentLog);
```

Add imports:

```java
import java.util.Date;
import com.mintstreet.consent.entity.ConsentLog;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
```

---

For **all loans**, repeat the same hidden fields and submit validation in each loan form, then save using respective `loanTypeId` and `appSeqId/leadId`.
