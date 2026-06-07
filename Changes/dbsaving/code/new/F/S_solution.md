Below are the **final minimum code changes** to make dropdown dynamic from DB.

---

## 1. `MasterLanguage.java`

Keep your query like this:

```java
@NamedQueries({
	@NamedQuery(
		name="MasterLanguage.getLanguageBylannguageCode",
		query = "select count(l) from MasterLanguage l "
			+ "where l.isActive ='Y' "
			+ "AND l.lannguageCode=:lannguageCode"
	),
	@NamedQuery(
		name = "MasterLanguage.getAllActiveLanguages",
		query = "SELECT l FROM MasterLanguage l "
			+ "WHERE l.isActive = 'Y' "
			+ "AND l.lannguageCode IS NOT NULL "
			+ "AND l.languageName IS NOT NULL "
			+ "ORDER BY l.langID"
	)
})
```

---

## 2. `MasterLanguageDao.java`

Replace with this:

```java
package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.MasterLanguage;

public class MasterLanguageDao extends GenericDao<Integer, MasterLanguage> {

	private static final long serialVersionUID = 1L;

	private static final Logger logger =
			LogManager.getLogger(MasterLanguageDao.class.getName());

	@SuppressWarnings("unchecked")
	public List<MasterLanguage> getAllActiveLanguages() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			List<MasterLanguage> list =
					(List<MasterLanguage>) findByNamedQueryRaw(
							"MasterLanguage.getAllActiveLanguages",
							params
					);

			logger.info("Language dropdown count : "
					+ (list == null ? 0 : list.size()));

			return list;

		} catch (Exception e) {
			logger.info("Exception in getAllActiveLanguages", e);
		}

		return null;
	}
}
```

---

## 3. `CommonService.java`

You already have this. Keep it:

```java
private MasterLanguageDao masterLanguageDao;
```

```java
public MasterLanguageDao getMasterLanguageDao() {
	return masterLanguageDao;
}

public void setMasterLanguageDao(MasterLanguageDao masterLanguageDao) {
	this.masterLanguageDao = masterLanguageDao;
}

public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}
```

---

## 4. `HomeLoanAction.java`

Replace only `getPrivacyLanguageList()` with this:

```java
public StreamResult getPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray array = new JSONArray();

	try {
		List<MasterLanguage> langList =
				commonService.getAllActiveLanguages();

		logger.info("HomeLoanAction language dropdown count : "
				+ (langList == null ? 0 : langList.size()));

		if (langList != null && !langList.isEmpty()) {

			for (MasterLanguage lang : langList) {

				if (lang == null) {
					continue;
				}

				JSONObject langObj = new JSONObject();

				langObj.put(
						"locale",
						lang.getLannguageCode() != null
								? lang.getLannguageCode()
								: ""
				);

				langObj.put(
						"languageName",
						lang.getLanguageName() != null
								? lang.getLanguageName()
								: ""
				);

				array.put(langObj);
			}

			json.put("status", "success");
			json.put("languageList", array);

		} else {
			json.put("status", "fail");
			json.put("languageList", array);
		}

	} catch (Exception e) {
		logger.info("Exception in getPrivacyLanguageList", e);

		try {
			json.put("status", "fail");
			json.put("languageList", array);
		} catch (JSONException je) {
			logger.info("JSONException in getPrivacyLanguageList", je);
		}
	}

	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}
```

---

## 5. JSP HTML

No big HTML change. Keep:

```jsp
<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>
```

---

## 6. JSP JavaScript

Replace only `loadPrivacyLanguageDropdown()`:

```javascript
function loadPrivacyLanguageDropdown(){
	$.ajax({
		url : "getPrivacyLanguageList",
		type : "POST",
		success : function(response){

			var json = typeof response === "string"
				? JSON.parse(response)
				: response;

			console.log("Language dropdown response:", json);

			if(json.status == "success"
				&& json.languageList != null
				&& json.languageList.length > 0){

				var optionHtml = "";

				$.each(json.languageList, function(index, item){

					if(item.locale != null && item.locale != ""
						&& item.languageName != null && item.languageName != ""){

						optionHtml += "<option value='"
							+ item.locale
							+ "'>"
							+ item.languageName
							+ "</option>";
					}
				});

				$("#privacyLocaleDropdown").empty().append(optionHtml);

				if($("#privacyLocaleDropdown option[value='eng']").length > 0){
					$("#privacyLocaleDropdown").val("eng");
					loadPrivacyByLocale("eng");
				} else {
					var firstLocale = $("#privacyLocaleDropdown option:first").val();
					loadPrivacyByLocale(firstLocale);
				}

			} else {
				$("#privacyLocaleDropdown").html(
					"<option value='eng'>English</option>"
				);
				loadPrivacyByLocale("eng");
			}
		},
		error : function(xhr){
			console.log("getPrivacyLanguageList error:",
				xhr.status,
				xhr.responseText
			);

			$("#privacyLocaleDropdown").html(
				"<option value='eng'>English</option>"
			);
			loadPrivacyByLocale("eng");
		}
	});
}
```

---

## 7. Modal open event

Keep this:

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	loadPrivacyLanguageDropdown();
});
```

Now dropdown will show:

```text
Assamese
Bengali
Bodo
Dogri
English
Gujarati
Hindi
...
```

with values:

```text
asm, ben, brx, dgo, eng, guj, hin...
```
