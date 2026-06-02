Yes, then use a **separate consent table** first.

Because before `appSeqId` exists, you cannot update `RUPEEPOWER_OCAS_T_00195`. So store consent immediately in a new table using **sessionId / OCAS ID + ntbId**, then later map it to `appSeqId`.

## Correct flow

```text
Accept clicked
→ get mobile + DOB from frontend
→ generate ntbId
→ AJAX to savePrivacyConsent
→ backend stores in PRIVACY_CONSENT_CAPTURE table using sessionId
→ popup closes
→ later when appSeqId is created
→ update same consent row with appSeqId
→ also update ApplicationFormHomeLoan
```

---

# 1. Create new DB table

```sql
CREATE TABLE SBI_TEST.RUPEEPOWER_OCAS_T_PRIVACY_CONSENT (
	CONSENT_CAPTURE_ID NUMBER(10) PRIMARY KEY,
	OCAS_SESSION_ID VARCHAR2(100),
	APP_SEQ_ID NUMBER(10),
	LOAN_TYPE_ID NUMBER(5),
	MOBILE_NO VARCHAR2(20),
	DATE_OF_BIRTH VARCHAR2(20),
	NTB_ID VARCHAR2(100),
	CONSENT_FLAG CHAR(1),
	CONSENT_LOCALE VARCHAR2(3),
	CONSENT_DATE DATE
);

CREATE SEQUENCE SBI_TEST.RUPEEPOWER_OCAS_T_PRIVACY_CONSENT_SEQ
START WITH 1
INCREMENT BY 1;
```

---

# 2. Create entity: `PrivacyConsentCapture.java`

```java
package com.mintstreet.consent.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="RUPEEPOWER_OCAS_T_PRIVACY_CONSENT")
@SequenceGenerator(
	name="RUPEEPOWER_OCAS_T_PRIVACY_CONSENT_SEQ",
	sequenceName="RUPEEPOWER_OCAS_T_PRIVACY_CONSENT_SEQ",
	allocationSize=1
)
@NamedQueries({
	@NamedQuery(
		name="PrivacyConsentCapture.getByOcasSessionId",
		query="SELECT p FROM PrivacyConsentCapture p "
			+ "WHERE p.ocasSessionId=:ocasSessionId "
			+ "ORDER BY p.consentCaptureId DESC"
	)
})
public class PrivacyConsentCapture extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RUPEEPOWER_OCAS_T_PRIVACY_CONSENT_SEQ")
	@Column(name="CONSENT_CAPTURE_ID")
	private Integer consentCaptureId;

	@Column(name="OCAS_SESSION_ID")
	private String ocasSessionId;

	@Column(name="APP_SEQ_ID")
	private Integer appSeqId;

	@Column(name="LOAN_TYPE_ID")
	private Integer loanTypeId;

	@Column(name="MOBILE_NO")
	private String mobileNo;

	@Column(name="DATE_OF_BIRTH")
	private String dateOfBirth;

	@Column(name="NTB_ID")
	private String ntbId;

	@Column(name="CONSENT_FLAG")
	private String consentFlag;

	@Column(name="CONSENT_LOCALE")
	private String consentLocale;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CONSENT_DATE")
	private Date consentDate;

	public Integer getConsentCaptureId() {
		return consentCaptureId;
	}

	public void setConsentCaptureId(Integer consentCaptureId) {
		this.consentCaptureId = consentCaptureId;
	}

	public String getOcasSessionId() {
		return ocasSessionId;
	}

	public void setOcasSessionId(String ocasSessionId) {
		this.ocasSessionId = ocasSessionId;
	}

	public Integer getAppSeqId() {
		return appSeqId;
	}

	public void setAppSeqId(Integer appSeqId) {
		this.appSeqId = appSeqId;
	}

	public Integer getLoanTypeId() {
		return loanTypeId;
	}

	public void setLoanTypeId(Integer loanTypeId) {
		this.loanTypeId = loanTypeId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getNtbId() {
		return ntbId;
	}

	public void setNtbId(String ntbId) {
		this.ntbId = ntbId;
	}

	public String getConsentFlag() {
		return consentFlag;
	}

	public void setConsentFlag(String consentFlag) {
		this.consentFlag = consentFlag;
	}

	public String getConsentLocale() {
		return consentLocale;
	}

	public void setConsentLocale(String consentLocale) {
		this.consentLocale = consentLocale;
	}

	public Date getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
}
```

---

# 3. Create DAO: `PrivacyConsentCaptureDao.java`

```java
package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyConsentCapture;

public class PrivacyConsentCaptureDao extends GenericDao<Integer, PrivacyConsentCapture> {

	private static final long serialVersionUID = 1L;

	public PrivacyConsentCapture savePrivacyConsentCapture(PrivacyConsentCapture obj) {
		return (PrivacyConsentCapture) save(obj.getConsentCaptureId(), obj);
	}

	public PrivacyConsentCapture getByOcasSessionId(String ocasSessionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ocasSessionId", ocasSessionId);

		List<?> list = getNamedQueryResult(
				"PrivacyConsentCapture.getByOcasSessionId",
				params
		);

		if (list != null && !list.isEmpty()) {
			return (PrivacyConsentCapture) list.get(0);
		}

		return null;
	}
}
```

---

# 4. `CommonService.java`

Add DAO field:

```java
private PrivacyConsentCaptureDao privacyConsentCaptureDao;
```

Add import:

```java
import com.mintstreet.consent.dao.PrivacyConsentCaptureDao;
import com.mintstreet.consent.entity.PrivacyConsentCapture;
```

Add methods:

```java
public PrivacyConsentCapture savePrivacyConsentCapture(PrivacyConsentCapture obj) {
	return privacyConsentCaptureDao.savePrivacyConsentCapture(obj);
}

public PrivacyConsentCapture getPrivacyConsentByOcasSessionId(String ocasSessionId) {
	return privacyConsentCaptureDao.getByOcasSessionId(ocasSessionId);
}

public PrivacyConsentCaptureDao getPrivacyConsentCaptureDao() {
	return privacyConsentCaptureDao;
}

public void setPrivacyConsentCaptureDao(PrivacyConsentCaptureDao privacyConsentCaptureDao) {
	this.privacyConsentCaptureDao = privacyConsentCaptureDao;
}
```

---

# 5. `HomeLoanAction.java`

Add fields:

```java
private String ntbId;
private String consentMobile;
private String consentDob;
private String consentLocale;
```

Add getter/setter:

```java
public String getNtbId() {
	return ntbId;
}

public void setNtbId(String ntbId) {
	this.ntbId = ntbId;
}

public String getConsentMobile() {
	return consentMobile;
}

public void setConsentMobile(String consentMobile) {
	this.consentMobile = consentMobile;
}

public String getConsentDob() {
	return consentDob;
}

public void setConsentDob(String consentDob) {
	this.consentDob = consentDob;
}

public String getConsentLocale() {
	return consentLocale;
}

public void setConsentLocale(String consentLocale) {
	this.consentLocale = consentLocale;
}
```

Add save method:

```java
public StreamResult savePrivacyConsent() {
	JSONObject json = new JSONObject();

	try {
		String ocasSessionId = SbiUtil.getOcasSessionId(request);

		if (ocasSessionId == null) {
			ocasSessionId = request.getSession().getId();
		}

		PrivacyConsentCapture consent = new PrivacyConsentCapture();

		consent.setOcasSessionId(ocasSessionId);
		consent.setLoanTypeId(Constants.HOME_LOAN_ID);
		consent.setMobileNo(consentMobile);
		consent.setDateOfBirth(consentDob);
		consent.setNtbId(ntbId);
		consent.setConsentFlag("Y");
		consent.setConsentLocale(consentLocale);
		consent.setConsentDate(new Date());

		commonService.savePrivacyConsentCapture(consent);

		json.put("status", "success");
		json.put("message", "Consent saved successfully.");

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

Add imports:

```java
import java.util.Date;
import com.mintstreet.consent.entity.PrivacyConsentCapture;
```

---

# 6. `ConsentPopup.jsp` AJAX

Inside `savePrivacyConsent()`, send mobile and DOB also:

```javascript
var mobile = $("#mobile").val();
var dob = $("#date_of_birth").val();
var locale = $("#privacyLocaleDropdown").val();

var cleanMobile = $.trim(mobile).replace(/\D/g, "");
var cleanDob = $.trim(dob).replace(/\D/g, "");

var ntbId = cleanMobile + cleanDob + loanTypeId;

$.ajax({
	url : "savePrivacyConsent",
	type : "POST",
	data : {
		ntbId : ntbId,
		consentMobile : cleanMobile,
		consentDob : dob,
		consentLocale : locale
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
```

---

# 7. `struts.xml`

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

# 8. Later when `appSeqId` is created

After application save and `appSeqId` is available, link temp consent to application:

```java
String ocasSessionId = SbiUtil.getOcasSessionId(request);

if (ocasSessionId == null) {
	ocasSessionId = request.getSession().getId();
}

PrivacyConsentCapture consent =
		commonService.getPrivacyConsentByOcasSessionId(ocasSessionId);

if (consent != null && "Y".equalsIgnoreCase(consent.getConsentFlag())) {
	appForm.setAppPrivacyConsentFlag("Y");
	appForm.setAppNtbId(consent.getNtbId());

	consent.setAppSeqId(appForm.getAppSeqId());
	commonService.savePrivacyConsentCapture(consent);
}
```

This is the only clean way to **store in DB immediately before `appSeqId` exists**.
