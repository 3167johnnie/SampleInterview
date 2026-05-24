Yes. For dropdown values to come from DB, your DB must store language display name also.

Right now table has only:

```text
PRIVACY_LOCALE
PRIVACY_NOTICE
```

For dropdown you also need:

```text
PRIVACY_LANGUAGE_NAME
```

Example:

| PRIVACY_LOCALE | PRIVACY_LANGUAGE_NAME | PRIVACY_NOTICE    |
| -------------- | --------------------- | ----------------- |
| eng            | English               | English content   |
| hin            | Hindi                 | Hindi content     |
| mar            | Marathi               | Marathi content   |
| mal            | Malayalam             | Malayalam content |

---

## 1. DB change

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_13703
ADD PRIVACY_LANGUAGE_NAME VARCHAR2(100);
```

Update data:

```sql
UPDATE RUPEEPOWER_OCAS_T_13703
SET PRIVACY_LANGUAGE_NAME = 'English'
WHERE PRIVACY_LOCALE = 'eng';

UPDATE RUPEEPOWER_OCAS_T_13703
SET PRIVACY_LANGUAGE_NAME = 'Hindi'
WHERE PRIVACY_LOCALE = 'hin';

UPDATE RUPEEPOWER_OCAS_T_13703
SET PRIVACY_LANGUAGE_NAME = 'Marathi'
WHERE PRIVACY_LOCALE = 'mar';

UPDATE RUPEEPOWER_OCAS_T_13703
SET PRIVACY_LANGUAGE_NAME = 'Malayalam'
WHERE PRIVACY_LOCALE = 'mal';
```

---

## 2. Entity change: `PrivacyRequestResponse.java`

Add column:

```java
@Column(name="PRIVACY_LANGUAGE_NAME")
private String privacyLanguageName;

public String getPrivacyLanguageName() {
	return privacyLanguageName;
}

public void setPrivacyLanguageName(String privacyLanguageName) {
	this.privacyLanguageName = privacyLanguageName;
}
```

Add named query:

```java
@NamedQuery(
	name="PrivacyRequestResponse.getAllPrivacyLanguages",
	query="SELECT p FROM PrivacyRequestResponse p "
		+ "WHERE p.privacyLocale IS NOT NULL "
		+ "ORDER BY p.privacyId"
)
```

Final named queries:

```java
@NamedQueries({

	@NamedQuery(
		name="PrivacyRequestResponse.findAll",
		query="SELECT a FROM PrivacyRequestResponse a"
	),

	@NamedQuery(
		name="PrivacyRequestResponse.getPrivacyByLocale",
		query="SELECT p FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyLocale=:privacyLocale"
	),

	@NamedQuery(
		name="PrivacyRequestResponse.getAllPrivacyLanguages",
		query="SELECT p FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyLocale IS NOT NULL "
			+ "ORDER BY p.privacyId"
	)
})
```

---

## 3. DAO change: `PrivacyRequestResponseDao.java`

Add method:

```java
@SuppressWarnings("unchecked")
public List<PrivacyRequestResponse> getAllPrivacyLanguages(){

	try{

		return (List<PrivacyRequestResponse>) getResultList(
			"PrivacyRequestResponse.getAllPrivacyLanguages",
			null
		);

	}catch(Exception e){

		logger.info(
			"Exception in getAllPrivacyLanguages",
			e
		);
	}

	return null;
}
```

Make sure import exists:

```java
import java.util.List;
```

---

## 4. CommonService.java

Add method:

```java
public List<PrivacyRequestResponse> getAllPrivacyLanguages(){

	return privacyRequestResponseDao
			.getAllPrivacyLanguages();
}
```

Import:

```java
import java.util.List;
```

---

## 5. HomeLoanAction.java

Add new AJAX method:

```java
public StreamResult getPrivacyLanguageList(){

	JSONObject json = new JSONObject();
	JSONArray array = new JSONArray();

	try{

		List<PrivacyRequestResponse> langList =
				commonService.getAllPrivacyLanguages();

		if(langList != null && !langList.isEmpty()){

			for(PrivacyRequestResponse obj : langList){

				JSONObject langObj = new JSONObject();

				langObj.put(
					"locale",
					obj.getPrivacyLocale()
				);

				langObj.put(
					"languageName",
					obj.getPrivacyLanguageName()
				);

				array.put(langObj);
			}

			json.put("status", "success");
			json.put("languageList", array);

		}else{

			json.put("status", "fail");
			json.put("languageList", array);
		}

	}catch(Exception e){

		json.put("status", "fail");
		json.put("languageList", array);
	}

	return new StreamResult(
		new ByteArrayInputStream(
			json.toString().getBytes()
		)
	);
}
```

Required imports:

```java
import java.util.List;
import org.json.JSONArray;
```

---

## 6. struts.xml

Add action:

```xml
<action name="getPrivacyLanguageList"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyLanguageList">

	<result type="stream">
		<param name="contentType">application/json</param>
	</result>

</action>
```

---

## 7. ConsentPopup.jsp dropdown change

Replace static dropdown:

```jsp
<select id="privacyLocaleDropdown"
	class="form-control"
	onchange="loadPrivacyByLocale();">
</select>
```

No static options needed.

---

## 8. JavaScript final code

Use this in `ConsentPopup.jsp`:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){

	loadPrivacyLanguageDropdown();

});

function loadPrivacyLanguageDropdown(){

	$.ajax({

		url : "getPrivacyLanguageList",

		type : "POST",

		success : function(response){

			var json = JSON.parse(response);

			if(json.status == "success"){

				var optionHtml = "";

				$.each(json.languageList, function(index, item){

					optionHtml += "<option value='"
						+ item.locale
						+ "'>"
						+ item.languageName
						+ "</option>";
				});

				$("#privacyLocaleDropdown").html(optionHtml);

				$("#privacyLocaleDropdown").val("eng");

				loadPrivacyByLocale("eng");

			}else{

				$("#privacyLocaleDropdown").html(
					"<option value='eng'>English</option>"
				);

				loadPrivacyByLocale("eng");
			}
		},

		error : function(){

			$("#privacyLocaleDropdown").html(
				"<option value='eng'>English</option>"
			);

			loadPrivacyByLocale("eng");
		}
	});
}
```

Your existing content loading function remains same:

```javascript
function loadPrivacyByLocale(locale){

	if(locale == null){
		locale = $("#privacyLocaleDropdown").val();
	}

	$("#acceptConsentBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.6",
			"cursor":"not-allowed"
		});

	$.ajax({

		url : "getPrivacyNoticeByLocale",

		type : "POST",

		data : {
			privacyLocale : locale
		},

		success : function(response){

			var json = JSON.parse(response);

			if(json.status == "success"){

				$("#consentHomeLoanDiv")
					.html(json.privacyNotice);

				$("#consentHomeLoanDiv")
					.scrollTop(0);

			}else{

				$("#consentHomeLoanDiv")
					.html("Privacy Notice Not Found");
			}
		},

		error : function(){

			$("#consentHomeLoanDiv")
				.html("Unable To Load Privacy Notice");
		}
	});
}
```

---

Now future language addition only needs DB insert/update. No JSP code change.
