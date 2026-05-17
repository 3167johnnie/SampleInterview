Perfect — now we have the exact architecture.

You already have:

* `ConsentPopup.jsp`
* `CommonService`
* `ConsentDao`
* `Consent Entity`
* popup flow
* existing DB consent loading

So now I can give the EXACT existing-code-based implementation.

Your uploaded files confirm that `CommonService` already uses `ConsentDao`. 

And callback service confirms existing consent retrieval pattern:

```java
commonService.getConsentByLoanType(...)
```

already exists. 

---

# FINAL IMPLEMENTATION FLOW

Current Flow:

```text
Read More
   ↓
openPopups('consentHomeLoan','1')
   ↓
ConsentPopup.jsp opens
   ↓
beanList.consentHomeLoan displayed
```

New Flow:

```text
Dropdown change
   ↓
AJAX call
   ↓
Action class
   ↓
CommonService
   ↓
ConsentDao
   ↓
DB
   ↓
return translated consent
   ↓
replace #consentHomeLoanDiv html
```

---

# STEP 1 — DB CHANGE

Run:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13689
ADD CONSENT_LANGUAGE VARCHAR2(20);
```

---

# STEP 2 — ENTITY CHANGE

File:

```text
Consent.java
```

ADD BELOW:

```java
@Column(name="CONSENT_LANGUAGE")
private String consentLanguage;
```

ADD getter setter:

```java
public String getConsentLanguage() {
	return consentLanguage;
}

public void setConsentLanguage(String consentLanguage) {
	this.consentLanguage = consentLanguage;
}
```

---

# STEP 3 — ADD NEW NAMED QUERY

Inside:

```java
@NamedQueries({
```

ADD:

```java
@NamedQuery(
	name="Consent.getConsentByLoanTypeAndLanguage",
	query="Select c from Consent c where "
	+ "c.consentLoanType=:consentLoanType "
	+ "and c.consentLanguage=:consentLanguage "
	+ "and c.consentActive='Y' "
	+ "and c.consentDeleted='N'"
),
```

---

# STEP 4 — MODIFY ConsentDao.java

Now search:

```java
getConsentByLoanType
```

You WILL find existing DAO method.

ADD BELOW IT:

```java
public Consent getConsentByLoanTypeAndLanguage(Integer loanType,String language){

	Map<String,Object> params = new HashMap<String,Object>();

	params.put("consentLoanType", loanType);
	params.put("consentLanguage", language);

	try{
		return (Consent)getSingleResult(
			"Consent.getConsentByLoanTypeAndLanguage",
			params
		);
	}catch(Exception e){
		logger.info("Exception in getConsentByLoanTypeAndLanguage",e);
	}

	return null;
}
```

---

# STEP 5 — MODIFY CommonService.java

Search:

```java
getConsentByLoanType
```

You WILL find existing method.

ADD BELOW IT:

```java
public Consent getConsentByLoanTypeAndLanguage(Integer loanType,String language){

	return consentDao.getConsentByLoanTypeAndLanguage(
			loanType,
			language
	);
}
```

---

# STEP 6 — MODIFY ACTION CLASS

Most likely:

```text
HomeLoanAction.java
```

ADD VARIABLE:

```java
private String consentLanguage;
```

Getter Setter:

```java
public String getConsentLanguage() {
	return consentLanguage;
}

public void setConsentLanguage(String consentLanguage) {
	this.consentLanguage = consentLanguage;
}
```

---

# STEP 7 — ADD AJAX METHOD IN ACTION

ADD:

```java
public StreamResult getConsentByLanguage() {

	JSONObject json = new JSONObject();

	try {

		Consent consent =
				commonService.getConsentByLoanTypeAndLanguage(
						Constants.HOME_LOAN_ID,
						consentLanguage
				);

		if(consent != null){

			String consentText =
					commonService.getClobdata(
						"RUPEEPOWER_OCAS_T_13689",
						"CONSENT_TEXT",
						"CONSENT_ID",
						consent.getConsentId()
					);

			json.put("status","success");
			json.put("consentText", consentText);

		}else{

			json.put("status","fail");
			json.put("consentText","Consent not found");
		}

	}catch(Exception e){

		json.put("status","fail");
		json.put("consentText","Error occurred");
	}

	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}
```

---

# STEP 8 — STRUTS.XML ENTRY

ADD:

```xml
<action name="getConsentByLanguage"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getConsentByLanguage">

	<result type="stream">
		<param name="contentType">application/json</param>
	</result>

</action>
```

---

# STEP 9 — MODIFY ConsentPopup.jsp

VERY IMPORTANT.

Inside:

```jsp
<div id = "consentHomeLoanDiv"
```

ADD DROPDOWN ABOVE IT.

FINAL:

```jsp
<div class="languageDiv" style="margin-bottom:15px;">

	<select id="consentLanguageDropdown"
		class="form-control"
		onchange="loadConsentByLanguage();">

		<option value="ENGLISH">English</option>
		<option value="HINDI">Hindi</option>
		<option value="MARATHI">Marathi</option>
		<option value="TAMIL">Tamil</option>
		<option value="TELUGU">Telugu</option>
		<option value="KANNADA">Kannada</option>
		<option value="MALAYALAM">Malayalam</option>

	</select>

</div>

<div id="consentHomeLoanDiv"
	class="f-otp-pop-content ">

	<s:property escapeHtml="false"
		value="%{beanList.consentHomeLoan}" />

</div>
```

---

# STEP 10 — JAVASCRIPT

Inside:

```jsp
ConsentPopup.jsp
```

BOTTOM ADD:

```html
<script>

function loadConsentByLanguage(){

	var language =
		$("#consentLanguageDropdown").val();

	$.ajax({

		url : "getConsentByLanguage",

		type : "POST",

		data : {
			consentLanguage : language
		},

		success : function(response){

			var json = JSON.parse(response);

			if(json.status=="success"){

				$("#consentHomeLoanDiv")
					.html(json.consentText);

			}else{

				alert("Consent not found");
			}
		},

		error : function(){

			alert("Unable to load consent");
		}
	});
}

</script>
```

---

# STEP 11 — HOW POPUP CURRENTLY WORKS

This:

```jsp
onclick="javascript:openPopups('consentHomeLoan','1');"
```

means:

---

## Step A

Calls JS function:

```javascript
openPopups()
```

---

## Step B

Passes popup id:

```javascript
consentHomeLoan
```

---

## Step C

Finds modal:

```html
<div class="modal fade otp-box"
	id="consentHomeLoan">
```

inside:

```text
ConsentPopup.jsp
```

---

## Step D

Bootstrap shows popup.

---

# EXACT PLACE WHERE MODAL HTML EXISTS

File:

```text
/appNew/common/ConsentPopup.jsp
```

This is THE ONLY popup file.

---

# MOST IMPORTANT

DO NOT CHANGE:

```jsp
HomeFirstPageSession.jsp
```

except maybe dropdown trigger if required.

All actual popup work happens inside:

```text
ConsentPopup.jsp
```

---

# RECOMMENDED DB DATA

| CONSENT_ID | CONSENT_LANGUAGE | CONSENT_TEXT    |
| ---------- | ---------------- | --------------- |
| 1          | ENGLISH          | English consent |
| 2          | HINDI            | Hindi consent   |
| 3          | MARATHI          | Marathi consent |

---

# YOU ARE USING OLD STRUTS + JSP + JQUERY ARCHITECTURE

So:

* NO REST controller
* NO Spring Boot controller
* NO React
* NO fetch API

Use:

* Struts Action
* StreamResult
* jQuery AJAX

ONLY.
