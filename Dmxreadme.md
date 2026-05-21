Below are the EXACT file-wise changes you need for your existing JSP + Struts + Spring + Hibernate architecture.

⸻

FILE 1:

PrivacyRequestResponse.java

Location:

com.mintstreet.consent.entity

⸻

FIND THIS

@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll",
			query="SELECT a FROM PrivacyRequestResponse a"),
})

⸻

REPLACE WITH

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
})

⸻

FILE 2:

CREATE NEW FILE

PrivacyRequestResponseDao.java

Location:

com.mintstreet.consent.dao

⸻

COMPLETE FILE

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
	getPrivacyByLocale(String locale){
		Map<String,Object> params =
				new HashMap<String,Object>();
		params.put("privacyLocale", locale);
		try{
			return (PrivacyRequestResponse)getSingleResult(
				"PrivacyRequestResponse.getPrivacyByLocale",
				params
			);
		}catch(Exception e){
			logger.info(
				"Exception in getPrivacyByLocale",
				e
			);
		}
		return null;
	}
}

⸻

FILE 3:

applicationContext.xml

You MUST add DAO bean.

Location usually:

WEB-INF/applicationContext.xml

OR

resources/applicationContext.xml

⸻

ADD THIS BEAN

Search similar DAO beans.

ADD BELOW THEM:

<bean id="privacyRequestResponseDao"
	class="com.mintstreet.consent.dao.PrivacyRequestResponseDao"/>

⸻

FILE 4:

CommonService.java

Location:

com.mintstreet.common.service

⸻

STEP 1

ADD IMPORT

import com.mintstreet.consent.dao.PrivacyRequestResponseDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

⸻

STEP 2

ADD VARIABLE

Search existing DAO variables.

ADD:

private PrivacyRequestResponseDao
		privacyRequestResponseDao;

⸻

STEP 3

ADD GETTER SETTER

public PrivacyRequestResponseDao
getPrivacyRequestResponseDao() {
	return privacyRequestResponseDao;
}
public void setPrivacyRequestResponseDao(
		PrivacyRequestResponseDao privacyRequestResponseDao) {
	this.privacyRequestResponseDao =
			privacyRequestResponseDao;
}

⸻

STEP 4

ADD SERVICE METHOD

public PrivacyRequestResponse
getPrivacyByLocale(String locale){
	return privacyRequestResponseDao
			.getPrivacyByLocale(locale);
}

⸻

FILE 5:

applicationContext-service.xml

OR wherever CommonService bean is configured

Find:

<bean id="commonService"

⸻

ADD PROPERTY

<property name="privacyRequestResponseDao"
	ref="privacyRequestResponseDao"/>

⸻

FINAL RESULT

Example:

<bean id="commonService"
	class="com.mintstreet.common.service.CommonService">
	<property name="privacyRequestResponseDao"
		ref="privacyRequestResponseDao"/>
</bean>

⸻

FILE 6:

HomeLoanAction.java

Location:

com.mintstreet.loan.homeloan.action

⸻

STEP 1

ADD IMPORTS

import java.io.ByteArrayInputStream;
import org.apache.struts2.dispatcher.StreamResult;
import org.json.JSONObject;
import com.mintstreet.consent.entity
		.PrivacyRequestResponse;

⸻

STEP 2

ADD VARIABLE

private String privacyLocale;

⸻

STEP 3

ADD GETTER SETTER

public String getPrivacyLocale() {
	return privacyLocale;
}
public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

⸻

STEP 4

ADD METHOD

public StreamResult getPrivacyNoticeByLocale(){
	JSONObject json = new JSONObject();
	try{
		/*
		 * Default English
		 */
		if(privacyLocale == null
				|| "".equals(privacyLocale)){
			privacyLocale = "eng";
		}
		PrivacyRequestResponse privacyObj =
				commonService
				.getPrivacyByLocale(
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

⸻

FILE 7:

struts.xml

Find HomeLoanAction mappings.

ADD:

<action name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyNoticeByLocale">
	<result type="stream">
		<param name="contentType">
			application/json
		</param>
	</result>
</action>

⸻

FILE 8:

JSP FILE

Example:

homeLoan.jsp

OR popup JSP.

⸻

STEP 1

ADD DROPDOWN

<div style="margin-bottom:15px;">
	<select id="privacyLocaleDropdown"
		class="form-control"
		onchange="loadPrivacyByLocale();">
		<option value="eng">English</option>
		<option value="hin">Hindi</option>
		<option value="mar">Marathi</option>
		<option value="mal">Malayalam</option>
	</select>
</div>

⸻

STEP 2

ADD CONTENT DIV

<div id="privacyNoticeDiv">
	<s:property
		escapeHtml="false"
		value="%{privacyNotice}" />
</div>

⸻

STEP 3

ADD AJAX SCRIPT

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

⸻

FILE 9:

DATABASE

Insert records:

INSERT INTO RUPEEPOWER_OCAS_T_13703
(PRIVACY_ID, PRIVACY_LOCALE, PRIVACY_NOTICE)
VALUES
(1,'eng','English Privacy Content');
INSERT INTO RUPEEPOWER_OCAS_T_13703
(PRIVACY_ID, PRIVACY_LOCALE, PRIVACY_NOTICE)
VALUES
(2,'hin','Hindi Privacy Content');
INSERT INTO RUPEEPOWER_OCAS_T_13703
(PRIVACY_ID, PRIVACY_LOCALE, PRIVACY_NOTICE)
VALUES
(3,'mar','Marathi Privacy Content');
INSERT INTO RUPEEPOWER_OCAS_T_13703
(PRIVACY_ID, PRIVACY_LOCALE, PRIVACY_NOTICE)
VALUES
(4,'mal','Malayalam Privacy Content');

⸻

FINAL WORKING FLOW

Popup Opens
    ↓
English content loaded from DB
    ↓
Dropdown Changed
    ↓
AJAX Call
    ↓
Action
    ↓
Service
    ↓
DAO
    ↓
DB Query by PRIVACY_LOCALE
    ↓
PRIVACY_NOTICE returned
    ↓
Popup Updated
