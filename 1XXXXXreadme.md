Got it — now instead of using `Consent.java`, you want the SAME multilingual popup flow using your existing entity:

```java
PrivacyRequestResponse.java
```

with:

| Column                   | Purpose                                    |
| ------------------------ | ------------------------------------------ |
| `PRIVACY_ID`             | Primary key                                |
| `PRIVACY_TOUCH_POINT_ID` | Loan/Product Type                          |
| `PRIVACY_NOTICE`         | Consent/Privacy content                    |
| `PRIVACY_LOCALE`         | Language code (`eng`, `hin`, `mar`, `mal`) |

Your entity structure is here: 

Now I’ll give EXACT code changes according to this entity only.

---

# 1. ADD NEW NAMED QUERY

Inside:

```java
@NamedQueries({
```

ADD:

```java
@NamedQuery(
	name="PrivacyRequestResponse.getPrivacyByTouchPointAndLocale",
	query="SELECT p FROM PrivacyRequestResponse p "
	+ "WHERE p.privacyTouchPointId=:privacyTouchPointId "
	+ "AND p.privacyLocale=:privacyLocale"
),
```

FINAL:

```java
@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll",
			query="SELECT a FROM PrivacyRequestResponse a"),

	@NamedQuery(
		name="PrivacyRequestResponse.getPrivacyByTouchPointAndLocale",
		query="SELECT p FROM PrivacyRequestResponse p "
		+ "WHERE p.privacyTouchPointId=:privacyTouchPointId "
		+ "AND p.privacyLocale=:privacyLocale"
	),
})
```

---

# 2. CREATE DAO

Create:

```text
PrivacyRequestResponseDao.java
```

```java
package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

public class PrivacyRequestResponseDao
	extends GenericDao<Integer, PrivacyRequestResponse>{

	private static final Logger logger =
			LogManager.getLogger(
				PrivacyRequestResponseDao.class.getName());

	private static final long serialVersionUID = 1L;

	public PrivacyRequestResponse
	getPrivacyByTouchPointAndLocale(
			String touchPointId,
			String locale){

		Map<String,Object> params =
				new HashMap<String,Object>();

		params.put("privacyTouchPointId", touchPointId);
		params.put("privacyLocale", locale);

		try{

			return (PrivacyRequestResponse)getSingleResult(
				"PrivacyRequestResponse.getPrivacyByTouchPointAndLocale",
				params
			);

		}catch(Exception e){

			logger.info(
				"Exception in getPrivacyByTouchPointAndLocale",
				e
			);
		}

		return null;
	}
}
```

---

# 3. ADD IN CommonService.java

Inject DAO:

```java
private PrivacyRequestResponseDao privacyRequestResponseDao;
```

Getter Setter:

```java
public PrivacyRequestResponseDao getPrivacyRequestResponseDao() {
	return privacyRequestResponseDao;
}

public void setPrivacyRequestResponseDao(
		PrivacyRequestResponseDao privacyRequestResponseDao) {

	this.privacyRequestResponseDao =
			privacyRequestResponseDao;
}
```

---

# 4. ADD SERVICE METHOD

Inside `CommonService.java`

```java
public PrivacyRequestResponse
getPrivacyByTouchPointAndLocale(
		String touchPointId,
		String locale){

	return privacyRequestResponseDao
			.getPrivacyByTouchPointAndLocale(
					touchPointId,
					locale
			);
}
```

---

# 5. ACTION CLASS CHANGES

Example:

```text
HomeLoanAction.java
```

ADD VARIABLE:

```java
private String privacyLocale;
```

Getter Setter:

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}
```

---

# 6. ADD AJAX METHOD

```java
public StreamResult getPrivacyNoticeByLocale(){

	JSONObject json = new JSONObject();

	try{

		PrivacyRequestResponse privacyObj =
				commonService
				.getPrivacyByTouchPointAndLocale(
						"HOME_LOAN",
						privacyLocale
				);

		if(privacyObj != null){

			json.put("status","success");

			json.put(
				"privacyNotice",
				privacyObj.getPrivacyNotice()
			);

		}else{

			json.put("status","fail");

			json.put(
				"privacyNotice",
				"Privacy Notice Not Found"
			);
		}

	}catch(Exception e){

		json.put("status","fail");

		json.put(
			"privacyNotice",
			"Unable To Load Privacy Notice"
		);
	}

	return new StreamResult(
		new ByteArrayInputStream(
			json.toString().getBytes()
		)
	);
}
```

---

# 7. STRUTS.XML

```xml
<action name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyNoticeByLocale">

	<result type="stream">
		<param name="contentType">
			application/json
		</param>
	</result>

</action>
```

---

# 8. JSP DROPDOWN

Inside popup JSP:

```jsp
<div style="margin-bottom:15px;">

	<select id="privacyLocaleDropdown"
		class="form-control"
		onchange="loadPrivacyByLocale();">

		<option value="eng">English</option>
		<option value="hin">Hindi</option>
		<option value="mar">Marathi</option>
		<option value="mal">Malayalam</option>
		<option value="guj">Gujarati</option>

	</select>

</div>
```

---

# 9. DIV TO UPDATE

```jsp
<div id="privacyNoticeDiv">

	<s:property escapeHtml="false"
		value="%{beanList.consentHomeLoan}" />

</div>
```

---

# 10. JAVASCRIPT AJAX

```html
<script>

function loadPrivacyByLocale(){

	var locale =
		$("#privacyLocaleDropdown").val();

	$.ajax({

		url : "getPrivacyNoticeByLocale",

		type : "POST",

		data : {
			privacyLocale : locale
		},

		success : function(response){

			var json = JSON.parse(response);

			if(json.status=="success"){

				$("#privacyNoticeDiv")
					.html(json.privacyNotice);

			}else{

				alert("Privacy Notice Not Found");
			}
		},

		error : function(){

			alert("Unable To Load Data");
		}
	});
}

</script>
```

---

# 11. DB DATA FORMAT

Your table should contain:

| PRIVACY_ID | PRIVACY_TOUCH_POINT_ID | PRIVACY_LOCALE | PRIVACY_NOTICE    |
| ---------- | ---------------------- | -------------- | ----------------- |
| 1          | HOME_LOAN              | eng            | English content   |
| 2          | HOME_LOAN              | hin            | Hindi content     |
| 3          | HOME_LOAN              | mar            | Marathi content   |
| 4          | HOME_LOAN              | mal            | Malayalam content |

---

# 12. IMPORTANT

Your multilingual content is directly stored in:

```java
PRIVACY_NOTICE
```

So NO CLOB retrieval needed.

This line is enough:

```java
privacyObj.getPrivacyNotice()
```

---

# 13. MAIN DIFFERENCE FROM OLD CONSENT FLOW

OLD:

```java
Consent.java
consentText
consentLanguage
```

NEW:

```java
PrivacyRequestResponse.java
privacyNotice
privacyLocale
```

---

# 14. YOUR FLOW NOW

```text
Dropdown Changed
    ↓
AJAX Call
    ↓
Struts Action
    ↓
CommonService
    ↓
PrivacyRequestResponseDao
    ↓
DB Fetch Using:
    privacyTouchPointId
    privacyLocale
    ↓
Return privacyNotice
    ↓
Update Popup DIV
```

Your previous architecture reference is here: 
