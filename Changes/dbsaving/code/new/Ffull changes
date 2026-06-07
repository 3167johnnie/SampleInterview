Below changes are for **DB-driven dropdown rendered directly in JSP using Struts iterator**, not AJAX dropdown population.

---

# 1. `MasterLanguage.java`

Your entity is mostly correct. Keep it like this.

```java
@NamedQueries({
	@NamedQuery(
		name = "MasterLanguage.getLanguageBylannguageCode",
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

No other change required in `MasterLanguage.java`.

---

# 2. `MasterLanguageDao.java`

Replace your full DAO with this.

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

			List<MasterLanguage> languageList =
					(List<MasterLanguage>) findByNamedQueryRaw(
							"MasterLanguage.getAllActiveLanguages",
							params
					);

			logger.info("Active language count : "
					+ (languageList == null ? 0 : languageList.size()));

			return languageList;

		} catch (Exception e) {
			logger.info("Exception in getAllActiveLanguages", e);
		}

		return null;
	}
}
```

Main change:

```java
findByNamedQueryRaw("MasterLanguage.getAllActiveLanguages", null);
```

changed to:

```java
Map<String, Object> params = new HashMap<String, Object>();

findByNamedQueryRaw("MasterLanguage.getAllActiveLanguages", params);
```

---

# 3. `CommonService.java`

You already have this. Keep it.

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

# 4. `HomeLoanAction.java`

Your file already imports `MasterLanguage` and has `privacyLocale`. 

## 4.1 Add field near `privacyLocale`

Find:

```java
private String privacyLocale;
```

Add below:

```java
private List<MasterLanguage> languages;
```

So it becomes:

```java
private String privacyLocale;
private List<MasterLanguage> languages;
```

---

## 4.2 Populate languages before JSP loads

Inside `generateUIBeanList(Integer moduleId)`, near your existing privacy notice code, find this part:

```java
PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
if (privacyObj != null) {
	beanList.setConsentHomeLoan(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentHomeLoan("Privacy Notice Not Available");
}
```

Replace with:

```java
languages = commonService.getAllActiveLanguages();

logger.info("Privacy dropdown language count : "
		+ (languages == null ? 0 : languages.size()));

PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
if (privacyObj != null) {
	beanList.setConsentHomeLoan(privacyObj.getPrivacyNotice());
} else {
	beanList.setConsentHomeLoan("Privacy Notice Not Available");
}
```

---

## 4.3 Add getter/setter near bottom of `HomeLoanAction.java`

Add before final closing `}`:

```java
public List<MasterLanguage> getLanguages() {
	return languages;
}

public void setLanguages(List<MasterLanguage> languages) {
	this.languages = languages;
}
```

You already have:

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}
```

Keep that also.

---

# 5. `ConsentPopup.jsp`

## 5.1 Replace dropdown only

Replace this:

```jsp
<select id="privacyLocaleDropdown" class ="privacy-consent-dropdown"
	onchange="loadPrivacyLanguageDropdown();">
	<!-- <option value="eng">English</option>
	<option value="hin">Hindi</option>
	<option value="mar">Marathi</option>
	<option value="mal">Malayalam</option> -->
</select>
```

With this:

```jsp
<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">

	<s:iterator value="languages" var="lang">
		<option value="<s:property value="#lang.lannguageCode" />"
			<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
			<s:property value="#lang.languageName" />
		</option>
	</s:iterator>

</select>
```

---

## 5.2 Keep modal open code like this

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	if($("#privacyLocaleDropdown option[value='eng']").length > 0){
		$("#privacyLocaleDropdown").val("eng");
		loadPrivacyByLocale("eng");
	} else {
		var firstLocale = $("#privacyLocaleDropdown option:first").val();
		loadPrivacyByLocale(firstLocale);
	}
});
```

---

## 5.3 Remove this function completely

Delete this full function because dropdown is now populated from JSP:

```javascript
function loadPrivacyLanguageDropdown(){
	...
}
```

---

# Final behavior

Dropdown options will be created in HTML like:

```html
<option value="asm">Assamese</option>
<option value="ben">Bengali</option>
<option value="eng" selected="selected">English</option>
<option value="hin">Hindi</option>
<option value="mar">Marathi</option>
```

On popup open, English notice loads by default. On selecting another language, this runs:

```javascript
loadPrivacyByLocale();
```

and loads consent notice for selected code like `hin`, `mar`, `mal`.
