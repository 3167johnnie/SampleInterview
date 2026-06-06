Use this prod-safe code.

1. MasterLanguage.java

Add this named query:

@NamedQuery(
	name = "MasterLanguage.getAllActiveLanguages",
	query = "SELECT l FROM MasterLanguage l "
		+ "WHERE l.isActive = 'Y' "
		+ "AND l.lannguageCode IS NOT NULL "
		+ "AND l.languageName IS NOT NULL "
		+ "ORDER BY l.langID"
)

Final:

@NamedQueries({
	@NamedQuery(
		name="MasterLanguage.getLanguageBylannguageCode",
		query = "SELECT COUNT(l) FROM MasterLanguage l "
			+ "WHERE l.isActive ='Y' "
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

⸻

2. MasterLanguageDao.java

package com.mintstreet.consent.dao;
import java.util.List;
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
			return (List<MasterLanguage>) getResultList(
					"MasterLanguage.getAllActiveLanguages",
					null
			);
		} catch (Exception e) {
			logger.info("Exception in getAllActiveLanguages", e);
		}
		return null;
	}
}

⸻

3. CommonService.java

Add import:

import com.mintstreet.consent.entity.MasterLanguage;

Add DAO:

private MasterLanguageDao masterLanguageDao;

Add setter:

public void setMasterLanguageDao(MasterLanguageDao masterLanguageDao) {
	this.masterLanguageDao = masterLanguageDao;
}

Add method:

public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}

⸻

4. Spring XML

Add DAO bean:

<bean id="masterLanguageDao"
	class="com.mintstreet.consent.dao.MasterLanguageDao">
</bean>

Inside commonService bean:

<property name="masterLanguageDao" ref="masterLanguageDao" />

⸻

5. HomeLoanAction.java

Use this method:

public StreamResult getPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray array = new JSONArray();
	try {
		List<MasterLanguage> langList =
				commonService.getAllActiveLanguages();
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

⸻

6. JSP dropdown

<select id="privacyLocaleDropdown"
	class="privacy-consent-dropdown"
	onchange="loadPrivacyByLocale();">
</select>

⸻

7. JSP JavaScript

Add:

var contextPath = "<%=request.getContextPath()%>";

Replace loadPrivacyLanguageDropdown():

function loadPrivacyLanguageDropdown(){
	$.ajax({
		url : contextPath + "/getPrivacyLanguageList.action",
		type : "POST",
		success : function(response){
			var json = typeof response === "string"
				? JSON.parse(response)
				: response;
			if(json.status == "success" && json.languageList.length > 0){
				var optionHtml = "";
				$.each(json.languageList, function(index, item){
					optionHtml += "<option value='" + item.locale + "'>"
						+ item.languageName
						+ "</option>";
				});
				$("#privacyLocaleDropdown").html(optionHtml);
				$("#privacyLocaleDropdown").val("eng");
				loadPrivacyByLocale("eng");
			} else {
				$("#privacyLocaleDropdown").html(
					"<option value='eng'>English</option>"
				);
				loadPrivacyByLocale("eng");
			}
		},
		error : function(xhr){
			console.log("getPrivacyLanguageList failed:", xhr.status, xhr.responseText);
			$("#privacyLocaleDropdown").html(
				"<option value='eng'>English</option>"
			);
			loadPrivacyByLocale("eng");
		}
	});
}

This will populate:

<option value="asm">Assamese</option>
<option value="ben">Bengali</option>
<option value="eng">English</option>
<option value="hin">Hindi</option>
...

English will be selected by default, and selected value like hin, mar, mal will load respective consent notice.
