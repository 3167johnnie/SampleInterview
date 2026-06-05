Use MasterLanguage table only for dropdown, and keep PrivacyRequestResponse table for notice content.

1. MasterLanguage.java

Replace @NamedQueries with:

@NamedQueries({
	@NamedQuery(
		name = "MasterLanguage.getLanguageBylannguageCode",
		query = "select count(l) from MasterLanguage l "
			+ "where l.isActive ='Y' "
			+ "and l.lannguageCode=:lannguageCode"
	),
	@NamedQuery(
		name = "MasterLanguage.getAllActiveLanguages",
		query = "select l from MasterLanguage l "
			+ "where l.isActive ='Y' "
			+ "order by l.languageName"
	)
})

⸻

2. Create MasterLanguageDao.java

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

Add DAO field:

private MasterLanguageDao masterLanguageDao;

Add getter/setter:

public MasterLanguageDao getMasterLanguageDao() {
	return masterLanguageDao;
}
public void setMasterLanguageDao(MasterLanguageDao masterLanguageDao) {
	this.masterLanguageDao = masterLanguageDao;
}

Add service method:

public List<MasterLanguage> getAllActiveLanguages() {
	return masterLanguageDao.getAllActiveLanguages();
}

Add imports:

import java.util.List;
import com.mintstreet.consent.entity.MasterLanguage;

⸻

4. Spring XML / Bean XML

Where your DAO beans are configured, add:

<bean id="masterLanguageDao"
	class="com.mintstreet.consent.dao.MasterLanguageDao">
</bean>

Inside commonService bean, add property:

<property name="masterLanguageDao" ref="masterLanguageDao" />

⸻

5. HomeLoanAction.java

Add import:

import com.mintstreet.consent.entity.MasterLanguage;

Replace your old dropdown method with this:

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

6. JavaScript remains almost same

No major change needed. This will now load all active languages from RUPEEPOWER_OCAS_T_13704.

function loadPrivacyLanguageDropdown(){
	$.ajax({
		url : "getPrivacyLanguageList",
		type : "POST",
		success : function(response){
			var json = typeof response === "string"
				? JSON.parse(response)
				: response;
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
			} else {
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

Final flow:

Dropdown:
RUPEEPOWER_OCAS_T_13704
LANNGUAGE_CODE + LANGUAGE_NAME
WHERE IS_ACTIVE = 'Y'
Notice:
RUPEEPOWER_OCAS_T_13703
PRIVACY_LOCALE = selected LANNGUAGE_CODE
AND PRIVACY_IS_ACTIVE = 'Y'
