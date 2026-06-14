Yes. Add CCMS Write API call **before Get Quote submit continues** using AJAX.

Your existing `ConsentService.generateConsentWriteRequest()` has hardcoded values like mobile/email/locale/ntbId. Change it to accept dynamic values from screen. 

---

# 1) `ConsentService.java`

## Replace this method

```java
public ConsentWriteLog generateConsentWriteRequest() {
```

## With this

```java
public ConsentWriteLog generateConsentWriteRequest(
		String mobile,
		String email,
		String ipAddress,
		String locale,
		String ntbId) {
```

Inside method replace hardcoded values:

```java
payloadHeaders.setAcceptLanguage("eng");
payloadHeaders.setxApiVersion("1.0");

dpData.setDpCIF("123456");
dpData.setDpMobile("998877554466");
dpData.setDpEmail("test@gmail.com");
dpData.setDpIPAddress("10.0.0.1");
dpData.setLocale("eng");
```

With:

```java
payloadHeaders.setAcceptLanguage(locale);
payloadHeaders.setxApiVersion(config.getApiVersion());

dpData.setDpCIF("");
dpData.setNtbId(ntbId);
dpData.setDpMobile(mobile);
dpData.setDpEmail(email);
dpData.setDpIPAddress(ipAddress);
dpData.setLocale(locale);
dpData.setTimestamp(DateUtil.getCurrentDateInISO8601Format());
```

Add import if needed:

```java
import com.mintstreet.common.util.DateUtil;
```

Also replace hardcoded config values:

```java
request.setSourceId("OC");
request.setDestination("CRM");
request.setTransactionType("CCMS");
```

With:

```java
request.setSourceId(config.getSourceId());
request.setDestination(config.getDestination());
request.setTransactionType(config.getTransactionType());
```

---

# 2) `HomeLoanAction.java`

Add import:

```java
import com.mintstreet.consent.entity.ConsentWriteLog;
import com.mintstreet.consent.service.ConsentService;
```

Add autowired service:

```java
@Autowired
private ConsentService consentService;
```

Add this method in `HomeLoanAction.java`:

```java
public StreamResult writePrivacyConsentToCCMS() {

	JSONObject json = new JSONObject();

	try {
		request = RequestUtil.getServletRequest();

		String mobile = request.getParameter("mobile");
		String email = request.getParameter("email");
		String ntbId = request.getParameter("ntbId");
		String locale = request.getParameter("locale");

		if (!ValidatorUtil.isValid(mobile)) {
			json.put("status", "fail");
			json.put("message", "Mobile number is required for consent.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		if (!ValidatorUtil.isValid(ntbId)) {
			json.put("status", "fail");
			json.put("message", "NTB ID is required for consent.");
			return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
		}

		if (!ValidatorUtil.isValid(locale)) {
			locale = "eng";
		}

		String ipAddress = request.getRemoteAddr();

		ConsentWriteLog consentWrite =
			consentService.generateConsentWriteRequest(
				mobile,
				email,
				ipAddress,
				locale,
				ntbId
			);

		consentService.writeConsentToCCMS(consentWrite);

		json.put("status", "success");
		json.put("message", "Consent written to CCMS successfully.");

	} catch (Exception e) {
		logger.info("Exception in writePrivacyConsentToCCMS", e);
		try {
			json.put("status", "fail");
			json.put("message", "Unable to write consent to CCMS.");
		} catch (JSONException je) {
			logger.info("JSONException in writePrivacyConsentToCCMS", je);
		}
	}

	return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
}
```

---

# 3) `struts-home-loan-actions.xml`

Add action mapping:

```xml
<action name="writePrivacyConsentToCCMS"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="writePrivacyConsentToCCMS">
	<result name="success" type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
```

---

# 4) `HomeRightContent.jsp`

Inside form, keep/add all hidden fields:

```jsp
<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
<s:hidden name="quote.quoteCcmsWriteFlag" id="quoteCcmsWriteFlag" />
```

For `quoteCcmsWriteFlag`, add DB/entity only if you want to store it. If only frontend use, plain hidden is okay:

```html
<input type="hidden" id="quoteCcmsWriteFlag" value="" />
```

---

# 5) `ConsentPopup.jsp`

Inside `acceptPrivacyConsent()`, add locale:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();

$("#quotePrivacyConsentFlag").val("Y");
$("#quoteNtbId").val(ntbId);
$("#quotePrivacyLocale").val(selectedLocale);
$("#quoteCcmsWriteFlag").val("");
```

---

# 6) `HomeFirstPageSession.jsp`

Replace your current `#subtn` click validation with this:

```javascript
$(document).on("click", "#subtn", function(){

	if($("#quotePrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept SBI Privacy Notice before proceeding.");
		return false;
	}

	if($.trim($("#quoteNtbId").val()) == ""){
		alert("Invalid consent details. Please accept SBI Privacy Notice again.");
		return false;
	}

	if($.trim($("#quotePrivacyLocale").val()) == ""){
		alert("Invalid privacy language details. Please accept SBI Privacy Notice again.");
		return false;
	}

	if($("#quoteCcmsWriteFlag").val() != "Y"){

		var ccmsStatus = false;

		$.ajax({
			url : "writePrivacyConsentToCCMS",
			type : "POST",
			async : false,
			data : {
				mobile : $("#mobile").val(),
				email : $("#email").val(),
				ntbId : $("#quoteNtbId").val(),
				locale : $("#quotePrivacyLocale").val()
			},
			success : function(response){

				var json = typeof response === "string"
					? JSON.parse(response)
					: response;

				if(json.status == "success"){
					$("#quoteCcmsWriteFlag").val("Y");
					ccmsStatus = true;
				}else{
					alert(json.message);
					ccmsStatus = false;
				}
			},
			error : function(){
				alert("Unable to write consent to CCMS. Please try again.");
				ccmsStatus = false;
			}
		});

		if(!ccmsStatus){
			return false;
		}
	}

	return true;
});
```

---

# 7) Remove wrong early CCMS call

In `HomeLoanAction.generateUIBeanList(...)`, remove/comment this:

```java
consentUtil.callCCMSConsentWriteAPI();
```

It should **not** run while loading page. Your file currently shows this call inside UI generation. 

---

Final flow:

```text
Accept popup
→ hidden values set: consent=Y, ntbId, locale
→ Get Quote click
→ frontend validates
→ AJAX calls writePrivacyConsentToCCMS
→ backend calls ConsentService → CCMS Write API
→ if success, normal Get Quote flow continues
```
