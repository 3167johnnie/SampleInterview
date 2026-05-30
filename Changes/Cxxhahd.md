Below are the complete file-wise changes from start to finish.

⸻

1. DB Change

ALTER TABLE RUPEEPOWER_OCAS_T_13703
ADD PRIVACY_IS_ACTIVE VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_13703
ADD PRIVACY_LANGUAGE_NAME VARCHAR2(100);

Update existing rows:

UPDATE RUPEEPOWER_OCAS_T_13703
SET PRIVACY_IS_ACTIVE = 'Y'
WHERE PRIVACY_IS_ACTIVE IS NULL;
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
COMMIT;

⸻

2. PrivacyRequestResponse.java

Remove this line

@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a")

Replace existing @NamedQueries

@NamedQueries({
	@NamedQuery(
		name="PrivacyRequestResponse.getPrivacyByLocale",
		query="SELECT p FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyLocale=:privacyLocale"
	),
})

With this

@NamedQueries({
	@NamedQuery(
		name="PrivacyRequestResponse.findAll",
		query="SELECT a FROM PrivacyRequestResponse a"
	),
	@NamedQuery(
		name="PrivacyRequestResponse.getPrivacyByLocale",
		query="SELECT p FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyLocale=:privacyLocale "
			+ "AND p.privacyIsActive=:privacyIsActive"
	),
	@NamedQuery(
		name="PrivacyRequestResponse.getAllPrivacyLanguages",
		query="SELECT DISTINCT p.privacyLocale, p.privacyLanguageName "
			+ "FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyLocale IS NOT NULL "
			+ "AND p.privacyIsActive=:privacyIsActive "
			+ "ORDER BY p.privacyLocale"
	)
})

Add these fields after privacyLocale

@Column(name="PRIVACY_IS_ACTIVE")
private String privacyIsActive;
@Column(name="PRIVACY_LANGUAGE_NAME")
private String privacyLanguageName;

Add getter/setter after locale getter/setter

public String getPrivacyIsActive() {
	return privacyIsActive;
}
public void setPrivacyIsActive(String privacyIsActive) {
	this.privacyIsActive = privacyIsActive;
}
public String getPrivacyLanguageName() {
	return privacyLanguageName;
}
public void setPrivacyLanguageName(String privacyLanguageName) {
	this.privacyLanguageName = privacyLanguageName;
}

⸻

3. PrivacyRequestResponseDao.java

Add import

import java.util.List;

Replace full DAO with this

package com.mintstreet.consent.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
public class PrivacyRequestResponseDao extends GenericDao<Integer, PrivacyRequestResponse> {
	private static final Logger logger =
			LogManager.getLogger(PrivacyRequestResponseDao.class.getName());
	private static final long serialVersionUID = 1L;
	public PrivacyRequestResponse getPrivacyByLocale(String locale) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("privacyLocale", locale);
		params.put("privacyIsActive", "Y");
		try {
			PrivacyRequestResponse result =
					(PrivacyRequestResponse) getSingleResult(
							"PrivacyRequestResponse.getPrivacyByLocale",
							params
					);
			logger.info(
					"Successfully retrieved PrivacyRequestResponse for locale: {}",
					locale
			);
			return result;
		} catch (Exception e) {
			logger.info("Exception in getPrivacyByLocale", e);
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Object[]> getAllPrivacyLanguages() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("privacyIsActive", "Y");
		try {
			return (List<Object[]>) getResultList(
					"PrivacyRequestResponse.getAllPrivacyLanguages",
					params
			);
		} catch (Exception e) {
			logger.info("Exception in getAllPrivacyLanguages", e);
		}
		return null;
	}
}

⸻

4. CommonService.java

Add this method near existing getPrivacyByLocale

Existing:

public PrivacyRequestResponse getPrivacyByLocale(String locale) {
	return privacyRequestResponseDao.getPrivacyByLocale(locale);
}

Add below it:

public List<Object[]> getAllPrivacyLanguages() {
	return privacyRequestResponseDao.getAllPrivacyLanguages();
}

No import needed because java.util.List already exists.

⸻

5. HomeLoanAction.java

Keep this field

private String privacyLocale;

Add getter/setter near bottom of class

public String getPrivacyLocale() {
	return privacyLocale;
}
public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

Add this method for notice by locale

public StreamResult getPrivacyNoticeByLocale() {
	JSONObject json = new JSONObject();
	try {
		PrivacyRequestResponse privacyObj =
				commonService.getPrivacyByLocale(privacyLocale);
		if (privacyObj != null
				&& privacyObj.getPrivacyNotice() != null) {
			json.put("status", "success");
			json.put("privacyNotice", privacyObj.getPrivacyNotice());
		} else {
			json.put("status", "fail");
			json.put("privacyNotice", "");
		}
	} catch (Exception e) {
		logger.info("Exception in getPrivacyNoticeByLocale", e);
		try {
			json.put("status", "fail");
			json.put("privacyNotice", "");
		} catch (JSONException je) {
			logger.info("JSONException in getPrivacyNoticeByLocale", je);
		}
	}
	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}

Add this method for dropdown

public StreamResult getPrivacyLanguageList() {
	JSONObject json = new JSONObject();
	JSONArray array = new JSONArray();
	try {
		List<Object[]> langList =
				commonService.getAllPrivacyLanguages();
		if (langList != null && !langList.isEmpty()) {
			for (Object[] obj : langList) {
				JSONObject langObj = new JSONObject();
				String locale = obj[0] != null ? obj[0].toString() : "";
				String languageName = obj[1] != null ? obj[1].toString() : locale;
				langObj.put("locale", locale);
				langObj.put("languageName", languageName);
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

6. struts.xml

Add these actions:

<action name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>
<action name="getPrivacyLanguageList"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyLanguageList">
	<result type="stream">
		<param name="contentType">application/json</param>
	</result>
</action>

⸻

7. JSP Dropdown

Replace static dropdown with:

<select id="privacyLocaleDropdown"
	class="form-control"
	onchange="loadPrivacyByLocale();">
</select>

⸻

8. JSP JavaScript

Add this:

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
				$("#consentHomeLoanDiv").html(json.privacyNotice);
				$("#consentHomeLoanDiv").scrollTop(0);
			}else{
				$("#consentHomeLoanDiv").html("Privacy Notice Not Found");
			}
		},
		error : function(){
			$("#consentHomeLoanDiv").html("Unable To Load Privacy Notice");
		}
	});
}

This final solution loads only:

PRIVACY_IS_ACTIVE = 'Y'

and dropdown values come distinct by:

PRIVACY_LOCALE, PRIVACY_LANGUAGE_NAME
