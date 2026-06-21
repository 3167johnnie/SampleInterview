You are right. Use the **same HomeLoan `ConsentPopup.jsp` flow**, not a new popup. For AutoLoan, the data will load like this:

`AutoLoanAction.getAutoLoan()` → `populateFirstPageContent()` / `populateForm()` → `generateUIBeanList(moduleId, pageNo)` → loads `languages` and default English privacy notice → JSP renders dropdown → popup AJAX calls `getPrivacyNoticeByLocale` → `AutoLoanAction.getPrivacyNoticeByLocale()` returns notice.

AutoLoan already calls `populateFirstPageContent(Constants.AUTO_LOAN_ID,1)` before returning first page , and AutoLoan existing consent text is currently loaded inside `generateUIBeanList(Integer moduleId, int pageNo)` using `beanList.setConsentAutoLoanNtb(consentTextNtb)` . Change **that exact place**.

---

## 1. `AutoLoanAction.java`

### 1.1 Add imports

Add with other imports:

```java
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
```

`ByteArrayInputStream`, `StreamResult`, `JSONObject`, `JSONArray`, `List` are already present in your AutoLoanAction.

---

### 1.2 Add variables

Near existing:

```java
private ApplicationFormAutoLoanQuote quote;
private ApplicationFormAutoLoan appForm;
```

add:

```java
private String privacyLocale;
private List<MasterLanguage> languages;
```

---

### 1.3 Change existing `generateUIBeanList(Integer moduleId, int pageNo)`

Find this existing block near bottom of `generateUIBeanList(Integer moduleId, int pageNo)`:

```java
//customer consent ETB
//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentText();
Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentId();
String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
beanList.setConsentAutoLoanEtb(consentTextEtb);

//customer consent NTB
//String consentTextNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentText();
Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentId();
String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
beanList.setConsentAutoLoanNtb(consentTextNtb);
```

Replace only NTB part like this. Keep ETB if used elsewhere.

```java
//customer consent ETB - keep existing
//String consentTextEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentText();
Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentId();
String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
beanList.setConsentAutoLoanEtb(consentTextEtb);

// Privacy Notice NTB - same as HomeLoan
languages = commonService.getAllActiveLanguages();
logger.info("AutoLoan privacy dropdown language count : " + (languages == null ? 0 : languages.size()));

PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");

if (privacyObj != null) {
	beanList.setConsentAutoLoanNtb(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentAutoLoanNtb("Privacy Notice Not Available");
}
```

This is the **main content-loading change**.

---

### 1.4 Add AJAX method in `AutoLoanAction.java`

Add this before `getAllProject()` or near other stream methods:

```java
public StreamResult getPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();

	try {
		if (privacyLocale == null || "".equals(privacyLocale)) {
			privacyLocale = "eng";
		}

		PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale(privacyLocale);

		if (privacyObj != null) {
			json.put("status", "success");
			json.put("privacyNotice", privacyObj.getPrivacyNotice());
		} else {
			json.put("status", "fail");
			json.put("privacyNotice", "Privacy Notice Not Found");
		}

	} catch (Exception e) {
		logger.info("Exception in AutoLoan getPrivacyNoticeByLocale", e);
		try {
			json.put("status", "fail");
			json.put("privacyNotice", "Unable To Load Privacy Notice");
		} catch (JSONException e1) {
			logger.info("JSONException in AutoLoan getPrivacyNoticeByLocale", e1);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

---

### 1.5 Add language list method

```java
public StreamResult getPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray array = new JSONArray();

	try {
		List<MasterLanguage> langList = commonService.getAllActiveLanguages();

		if (langList != null && !langList.isEmpty()) {
			for (MasterLanguage lang : langList) {
				JSONObject langObj = new JSONObject();
				langObj.put("locale", lang.getLannguageCode());
				langObj.put("languageName", lang.getLanguageName());
				array.put(langObj);
			}

			json.put("status", "success");
			json.put("languageList", array);
		} else {
			json.put("status", "fail");
			json.put("languageList", array);
		}

	} catch (Exception e) {
		logger.info("Exception in AutoLoan getPrivacyLanguageList", e);
		try {
			json.put("status", "fail");
			json.put("languageList", array);
		} catch (JSONException je) {
			logger.info("JSONException in AutoLoan getPrivacyLanguageList", je);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

---

### 1.6 Add getters/setters at bottom

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

public List<MasterLanguage> getLanguages() {
	return languages;
}

public void setLanguages(List<MasterLanguage> languages) {
	this.languages = languages;
}
```

---

## 2. `AutoRightContent.jsp`

Existing AutoLoan form only has `csrfTokenVal` and includes `AutoFirstPageSession.jsp` .

Find:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
<s:include value="/appNew/loan/autoloan/AutoFirstPageSession.jsp"></s:include>
```

Replace with:

```jsp
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />

<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />

<s:include value="/appNew/loan/autoloan/AutoFirstPageSession.jsp"></s:include>
```

Use `quote.quotePrivacyConsentFlag`, not `quote.privacyConsentFlag`, because HomeLoan entity uses same field naming style.

---

## 3. `AutoFirstPageSession.jsp`

Existing AutoLoan first page uses old scrollable consent text and `beanList.consentAutoLoanNTB` .

Find:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide"  value="on">

<label for="infoprovide" class="label-content consentScrollForAuto scollerClass" id="autoID">
	<s:property escapeHtml="false" value="%{beanList.consentAutoLoanNTB}" />
	&nbsp;<b class="req">*</b>
</label>
```

Replace with:

```jsp
<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">

<label for="infoprovide" class="label-content" id="autoID">
	I hereby authorize State Bank of India and/or its representative to contact me with reference to my application. 
	For more details please read
	<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> SBI’s Privacy Notice </a></b>
	<b class="req">*</b>
</label>
```

Remove this old AutoLoan script:

```jsp
<script>
$(".consentScrollForAuto").mCustomScrollbar({
	theme:"rounded",
	scrollInertia:5,
	mouseWheel:{scrollAmount:30},
	scrollButtons:{
		enable:true
	}
	
});
</script>
```

At bottom of `AutoFirstPageSession.jsp`, add:

```jsp
<s:include value="/appNew/common/ConsentPopup.jsp"></s:include>
```

Do **not** create a new popup. Use existing HomeLoan working `ConsentPopup.jsp`.

---

## 4. `ConsentPopup.jsp`

Use existing file:

```text
WebContent/appNew/common/ConsentPopup.jsp
```

Only make these 2 small corrections.

### 4.1 Fix typo

Find:

```javascript
$("#consentHomeLoanDiv").html0(
	"Unable To Load Privacy Notice");
```

Replace:

```javascript
$("#consentHomeLoanDiv").html(
	"Unable To Load Privacy Notice");
```

### 4.2 Save selected locale also

Find inside `acceptPrivacyConsent()`:

```javascript
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
```

Replace:

```javascript
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val($("#privacyLocaleDropdown").val());
```

Important: this popup uses global `loanTypeId`. AutoLoanAction already sets:

```java
loanTypeId=Constants.AUTO_LOAN_ID;
```

inside `getAutoLoan(int moduleId)` .

---

## 5. `ApplicationFormAutoLoanQuote.java`

Add DB columns same as HomeLoan quote.

### Add near:

```java
@Transient
private Integer loanQuoteConsentId;
```

add:

```java
//For Privacy Consent Ntb
@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;

@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;

@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;
```

Add getters/setters:

```java
public String getQuotePrivacyConsentFlag() {
	return quotePrivacyConsentFlag;
}

public void setQuotePrivacyConsentFlag(String quotePrivacyConsentFlag) {
	this.quotePrivacyConsentFlag = quotePrivacyConsentFlag;
}

public String getQuoteNtbId() {
	return quoteNtbId;
}

public void setQuoteNtbId(String quoteNtbId) {
	this.quoteNtbId = quoteNtbId;
}

public String getQuotePrivacyLocale() {
	return quotePrivacyLocale;
}

public void setQuotePrivacyLocale(String quotePrivacyLocale) {
	this.quotePrivacyLocale = quotePrivacyLocale;
}
```

---

## 6. `ApplicationFormAutoLoan.java`

Add fields near existing:

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

@Column(name = "APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

Add getters/setters:

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

public String getAppPrivacyLocale() {
	return appPrivacyLocale;
}

public void setAppPrivacyLocale(String appPrivacyLocale) {
	this.appPrivacyLocale = appPrivacyLocale;
}
```

---

## 7. DB changes

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_NTB_ID VARCHAR2(100);
ALTER TABLE RUPEEPOWER_OCAS_T_00099 ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);

ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_NTB_ID VARCHAR2(100);
ALTER TABLE RUPEEPOWER_OCAS_T_00063 ADD APP_PRIVACY_LOCALE VARCHAR2(20);
```

---

## 8. `AutoProcessManagerImpl.java`

Add same backend validation as HomeLoan.

Find in `processGetQuotes(...)`, after quote is received and before main processing/save.

Add:

```java
if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
	return loanScenarioBean;
}

if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
	loanScenarioBean.setStatus(Integer.valueOf(0));
	loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
	return loanScenarioBean;
}
```

AutoLoan first-page submit already calls `processManagerAutoImpl.processGetQuotes(...)` after preparing quote data .

---

## 9. `AutoLoanHelper.java`

Find in `insertAppLoan()`:

```java
//save consent id in main table
if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
	application.setAppConsentId(quote.getLoanQuoteConsentId());
}

application = this.autoLoanService.save(application);
```

Replace with:

```java
//save consent id in main table
if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
	application.setAppConsentId(quote.getLoanQuoteConsentId());
}

//set flag for privacy consent - NTB
if (quote.getQuotePrivacyConsentFlag() != null && application.getAppPrivacyConsentFlag() == null) {
	application.setAppPrivacyConsentFlag(quote.getQuotePrivacyConsentFlag());
}

if (quote.getQuoteNtbId() != null && application.getAppNtbId() == null) {
	application.setAppNtbId(quote.getQuoteNtbId());
}

if (quote.getQuotePrivacyLocale() != null && application.getAppPrivacyLocale() == null) {
	application.setAppPrivacyLocale(quote.getQuotePrivacyLocale());
}

application = this.autoLoanService.save(application);
```

---

## 10. `struts-auto-loan-actions.xml`

Add mappings in AutoLoan struts file.

```xml
<action name="getPrivacyNoticeByLocale" class="autoLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

<action name="getPrivacyLanguageList" class="autoLoanAction"
	method="getPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

Because HomeLoan has the same action names, this works only if AutoLoan and HomeLoan action configs are in their proper Struts package/namespace. If they are in the same global namespace, then duplicate names can hit the wrong action. In that case use AutoLoan-specific names:

```xml
<action name="autoGetPrivacyNoticeByLocale" class="autoLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

<action name="autoGetPrivacyLanguageList" class="autoLoanAction"
	method="getPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

Then in `ConsentPopup.jsp`, change:

```javascript
url : "getPrivacyNoticeByLocale",
```

to dynamic product-wise URL:

```javascript
url : (typeof privacyNoticeUrl !== "undefined" ? privacyNoticeUrl : "getPrivacyNoticeByLocale"),
```

And in `AutoFirstPageSession.jsp`, before popup include:

```jsp
<script>
	var privacyNoticeUrl = "autoGetPrivacyNoticeByLocale";
</script>
```

For HomeLoan, no change needed because default remains `getPrivacyNoticeByLocale`.

---

## 11. Why popup opened but data did not load

Popup opened from JSP, so UI was fine. Data did not load because one of these was missing:

```text
AutoLoanAction.getPrivacyNoticeByLocale()
struts-auto-loan-actions.xml mapping
languages = commonService.getAllActiveLanguages()
commonService.getPrivacyByLocale("eng")
```

HomeLoan works because all these were added in HomeLoan git changes. AutoLoan must get the same action-level method and mapping, but inside existing AutoLoan flow.

Final AutoLoan loading point:

```text
getAutoLoan()
→ populateForm() / populateFirstPageContent()
→ generateUIBeanList(moduleId, pageNo)
→ languages = commonService.getAllActiveLanguages()
→ beanList.setConsentAutoLoanNtb(privacyObj.getPrivacyNotice())
→ AutoFirstPageSession.jsp includes ConsentPopup.jsp
→ popup calls AutoLoanAction.getPrivacyNoticeByLocale()
```



#################################################################################################################################################3





Yes, **HomeLoanAction.java is used**, but not by the label directly.

That label only opens popup. Actual privacy notice content loads from `ConsentPopup.jsp` JavaScript, which calls `HomeLoanAction.getPrivacyNoticeByLocale()`.

## Flow

```jsp
<input type="checkbox" ... id="infoprovide" ... disabled="disabled">
```

Checkbox is disabled, so user cannot tick it manually. It will be checked only after user reads popup and clicks **Accept**.

```jsp
<label for="infoprovide" ...>
```

This is the visible consent text beside checkbox.

```jsp
<a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');">
	SBI’s Privacy Notice
</a>
```

When user clicks this link, it calls:

```js
openPopups('consentHomeLoan','1')
```

This opens modal having id:

```jsp
<div class="modal fade otp-box" id="consentHomeLoan" ...>
```

This modal is inside `/appNew/common/ConsentPopup.jsp` .

After popup opens, this JS runs:

```js
$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
	$("#privacyLocaleDropdown").val("eng");
	loadPrivacyByLocale("eng");
});
```

Meaning: once popup is visible, default language is set to English, then `loadPrivacyByLocale("eng")` is called .

Then this function calls backend:

```js
$.ajax({
	url : "getPrivacyNoticeByLocale",
	type : "POST",
	data : {
		privacyLocale : locale
	}
});
```

So browser sends request to:

```text
getPrivacyNoticeByLocale
```

That Struts action maps to:

```java
HomeLoanAction.getPrivacyNoticeByLocale()
```

In `HomeLoanAction`, this method gets privacy notice from DB:

```java
PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale(privacyLocale);
```

Then returns JSON:

```java
json.put("status", "success");
json.put("privacyNotice", privacyObj.getPrivacyNotice());
```

HomeLoanAction has `privacyLocale` and `languages` fields added for this flow .

Back in popup JS, success response does:

```js
$("#consentHomeLoanDiv").html(json.privacyNotice);
```

So DB privacy content appears inside:

```jsp
<div id="consentHomeLoanDiv" class="privacy-consent-pop-content">
	Loading Privacy Notice...
</div>
```

After user scrolls till end, Accept button enables. On Accept:

```js
$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#infoprovide").prop("disabled", false).prop("checked", true);
$("#consentHomeLoan").modal("hide");
```

So checkbox becomes checked only after accepting.

## Simple answer

This JSP line:

```jsp
openPopups('consentHomeLoan','1')
```

only opens popup.

The actual loading happens from:

```text
ConsentPopup.jsp JavaScript
→ AJAX getPrivacyNoticeByLocale
→ HomeLoanAction.getPrivacyNoticeByLocale()
→ commonService.getPrivacyByLocale()
→ DB table RUPEEPOWER_OCAS_T_13703
→ response loaded into consentHomeLoanDiv
```

For AutoLoan, same will work only if AutoLoan has its own mapped `getPrivacyNoticeByLocale` action or the shared URL correctly points to AutoLoanAction.





## NEXT ##################################################################################################################

This is a very important concept in JSP + Bootstrap + Struts. Once you understand this, you'll know exactly how to implement it for Auto Loan as well.

---

# Step 1: User clicks this

```jsp
<a href="javascript:void(0);"
   onclick="javascript:openPopups('consentHomeLoan','1');">
   SBI’s Privacy Notice
</a>
```

Nothing is loaded from server here.

It simply calls JavaScript:

```javascript
openPopups('consentHomeLoan','1');
```

Here

```
consentHomeLoan
```

is **NOT a JSP name.**

It is simply the **id of a Bootstrap Modal.**

---

# Step 2: Where is consentHomeLoan?

Somewhere on the SAME page there is

```jsp
<div class="modal fade"
     id="consentHomeLoan">
```

Example

```jsp
<div id="termAndConditionHTML">

    <div class="modal fade"
         id="consentHomeLoan">

        ...

    </div>

</div>
```

This entire modal comes from

```
ConsentPopup.jsp
```

which is included into HomeLoan page. 

---

# Step 3: How does HomeLoan page get ConsentPopup.jsp?

Somewhere in HomeLoan.jsp or HomeFirstPage.jsp you'll find something similar to

```jsp
<s:include value="/appNew/common/ConsentPopup.jsp"/>
```

or

```jsp
<jsp:include page="/appNew/common/ConsentPopup.jsp"/>
```

or

```jsp
<%@ include file="/appNew/common/ConsentPopup.jsp"%>
```

One of these three.

This is the MOST IMPORTANT STEP.

When server renders HomeLoan page,

it becomes

```
HomeLoan.jsp
     +
ConsentPopup.jsp
-----------------------
One Single HTML Page
```

There are NOT two pages.

Only ONE page.

---

# Step 4: Browser receives

Browser actually receives

```html
<html>

...

<label>

<a onclick="openPopups('consentHomeLoan','1')">

</a>

...

<div class="modal fade"
     id="consentHomeLoan">

....

</div>

</html>
```

Notice

Both exist in SAME HTML.

---

# Step 5: User clicks

User clicks

```
Privacy Notice
```

Browser executes

```javascript
openPopups('consentHomeLoan','1');
```

Now JavaScript searches

```
id="consentHomeLoan"
```

inside current HTML.

It finds

```html
<div id="consentHomeLoan">
```

and Bootstrap displays it.

No server call.

No HomeLoanAction.

Nothing.

Only Bootstrap.

---

# Visual Flow

```
User Clicks

        |

        V

openPopups("consentHomeLoan")

        |

        V

Search HTML

id="consentHomeLoan"

        |

        V

Found

<div id="consentHomeLoan">

        |

        V

Bootstrap Opens Modal
```

---

# Step 6: Then how does content load?

Popup opens FIRST.

Immediately after popup opens,

this JavaScript runs:

```javascript
$(document).on("shown.bs.modal",
"#consentHomeLoan",
function(){

    loadPrivacyByLocale("eng");

});
```

This event means

```
Modal opened successfully
```

Now JavaScript calls

```javascript
loadPrivacyByLocale()
```

This is where backend starts.

---

# Step 7: AJAX call

```javascript
$.ajax({

    url:"getPrivacyNoticeByLocale",

    data:{
        privacyLocale:locale
    }

});
```

Now request goes to

```
HomeLoanAction.java
```

NOT before this.

---

# Step 8: HomeLoanAction

```
Browser

↓

getPrivacyNoticeByLocale

↓

Struts.xml

↓

HomeLoanAction.getPrivacyNoticeByLocale()

↓

CommonService

↓

DAO

↓

Database

↓

Privacy Notice

↓

JSON Response
```

---

# Step 9: Browser receives response

Suppose backend returns

```json
{
   "status":"success",
   "privacyNotice":"<h2>Hello</h2>"
}
```

JavaScript executes

```javascript
$("#consentHomeLoanDiv").html(json.privacyNotice);
```

Now

```html
<div id="consentHomeLoanDiv">
```

changes from

```
Loading Privacy Notice...
```

to

```
Actual DB Content
```

---

# Complete Flow Diagram

```
HomeLoan.jsp

        |

        | includes

        V

ConsentPopup.jsp

        |

        |

Browser receives ONE HTML

        |

        |

Click Privacy Notice

        |

        |

openPopups()

        |

        |

Find

id="consentHomeLoan"

        |

        |

Popup Opens

        |

        |

shown.bs.modal Event

        |

        |

loadPrivacyByLocale()

        |

        |

AJAX

        |

        |

HomeLoanAction

        |

        |

Database

        |

        |

JSON

        |

        |

$("#consentHomeLoanDiv").html(...)
```

---

## Why this matters for Auto Loan

For Auto Loan, you **do not need another popup page** if you want identical behavior. You can include the same `ConsentPopup.jsp` in the Auto Loan page, and as long as:

* the modal ID (`consentHomeLoan`) remains the same,
* `openPopups('consentHomeLoan','1')` is called,
* and the AJAX URL resolves to the correct Auto Loan (or shared) action,

the popup will open and behave exactly like Home Loan. The popup itself is just HTML included into the page; it isn't a separate page that's opened dynamically.
