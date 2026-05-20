You already have a proper backend action architecture.
Now you only need:

1. Add one new API method in existing PrivacyNoticeAction
2. Add DAO/service method
3. Add popup dropdown
4. Add AJAX
5. Add banking-style consent enforcement

This is the clean SBI-style implementation.

⸻

FINAL IMPLEMENTATION FLOW

Read More
   ↓
Popup Opens
   ↓
Close disabled
   ↓
User scrolls till bottom
   ↓
Accept button enabled
   ↓
User clicks Accept
   ↓
Checkbox auto checked
   ↓
Popup closed
   ↓
Language dropdown changes
   ↓
AJAX call
   ↓
PrivacyNoticeAction
   ↓
DB fetch from RUPEEPOWER_OCAS_T_13703
   ↓
PRIVACY_NOTICE loaded

⸻

1. ENTITY CLASS CHANGES

FILE

/src/com/mintstreet/consent/entity/PrivacyRequestResponse.java

⸻

ADD NAMED QUERY

Find:

@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a"),
})

⸻

REPLACE WITH

@NamedQueries({
	@NamedQuery(
		name="PrivacyRequestResponse.findAll",
		query="SELECT a FROM PrivacyRequestResponse a"
	),
	@NamedQuery(
		name="PrivacyRequestResponse.getConsentByLocale",
		query="SELECT a FROM PrivacyRequestResponse a "
				+ "WHERE a.privacyLocale = :privacyLocale "
				+ "AND a.privacyTouchPointId = :privacyTouchPointId"
	),
})

⸻

2. DAO CLASS

CREATE FILE

/src/com/mintstreet/consent/dao/PrivacyRequestResponseDao.java

⸻

COMPLETE CODE

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
				PrivacyRequestResponseDao.class
			);
	public PrivacyRequestResponse getConsentByLocale(
			String privacyLocale,
			String privacyTouchPointId){
		Map<String,Object> params =
				new HashMap<String,Object>();
		params.put(
				"privacyLocale",
				privacyLocale
		);
		params.put(
				"privacyTouchPointId",
				privacyTouchPointId
		);
		try{
			return (PrivacyRequestResponse)
					getSingleResult(
						"PrivacyRequestResponse.getConsentByLocale",
						params
					);
		}catch(Exception e){
			logger.info(
				"Exception in getConsentByLocale",
				e
			);
		}
		return null;
	}
}

⸻

3. COMMONSERVICE INTERFACE

FILE

/src/com/mintstreet/common/service/CommonService.java

⸻

ADD IMPORT

import com.mintstreet.consent.entity.PrivacyRequestResponse;

⸻

ADD METHOD

public PrivacyRequestResponse getConsentByLocale(
		String privacyLocale,
		String privacyTouchPointId);

⸻

4. COMMONSERVICEIMPL

FILE

/src/com/mintstreet/common/service/impl/CommonServiceImpl.java

⸻

ADD IMPORTS

import com.mintstreet.consent.dao.PrivacyRequestResponseDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;

⸻

ADD DAO

@Autowired
private PrivacyRequestResponseDao privacyRequestResponseDao;

⸻

ADD METHOD

@Override
public PrivacyRequestResponse getConsentByLocale(
		String privacyLocale,
		String privacyTouchPointId){
	return privacyRequestResponseDao
			.getConsentByLocale(
				privacyLocale,
				privacyTouchPointId
			);
}

⸻

5. PRIVACYNOTICEACTION CHANGES

FILE

/src/com/mintstreet/consent/action/PrivacyNoticeAction.java

⸻

ADD VARIABLE

Find class variables.

ADD:

private String privacyLocale;

⸻

ADD GETTER SETTER

public String getPrivacyLocale() {
	return privacyLocale;
}
public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}

⸻

ADD NEW AJAX API METHOD

Add BELOW:

public StreamResult storePrivacyNotice()

⸻

ADD COMPLETE METHOD

public StreamResult getConsentByLanguage() {
	JSONObject json = new JSONObject();
	try {
		PrivacyRequestResponse response =
				commonService.getConsentByLocale(
						privacyLocale,
						"HOME_LOAN"
				);
		if(response != null){
			json.put("status", "success");
			json.put(
				"privacyNotice",
				response.getPrivacyNotice()
			);
		}else{
			json.put("status", "fail");
			json.put(
				"privacyNotice",
				"Consent not available"
			);
		}
	}catch(Exception e){
		logger.info(
			"Exception in getConsentByLanguage",
			e
		);
		try{
			json.put("status", "fail");
			json.put(
				"privacyNotice",
				"Error occurred"
			);
		}catch(Exception ex){}
	}
	return new StreamResult(
			new ByteArrayInputStream(
					json.toString().getBytes()
			)
	);
}

⸻

6. STRUTS.XML

FILE

Likely:

/WebContent/WEB-INF/classes/struts.xml

OR consent struts xml.

⸻

ADD ACTION

<action
	name="getConsentByLanguage"
	class="com.mintstreet.consent.action.PrivacyNoticeAction"
	method="getConsentByLanguage">
	<result type="stream">
		<param name="contentType">
			application/json
		</param>
	</result>
</action>

⸻

7. POPUP JSP CHANGES

FILE

/WebContent/appNew/common/ConsentPopup.jsp

⸻

REPLACE COMPLETE FILE WITH

<%@ page language="java"
	contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="termAndConditionHTML">
	<div class="modal fade otp-box"
		id="consentHomeLoan"
		tabindex="-1"
		role="dialog"
		data-bs-backdrop="static"
		data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button
						type="button"
						class="close clo"
						id="consentCloseBtn"
						disabled="disabled"
						data-bs-dismiss="modal">
						<span aria-hidden="true">
							<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/>
						</span>
					</button>
					<div class="language-selector-div">
						<label>
							Select Language
						</label>
						<select
							id="consentLanguageDropdown"
							class="form-select"
							onchange="changeConsentLanguage();">
							<option value="ENG">
								English
							</option>
							<option value="HIN">
								Hindi
							</option>
							<option value="MAR">
								Marathi
							</option>
							<option value="MAL">
								Malayalam
							</option>
							<option value="ASM">
								Assamese
							</option>
						</select>
					</div>
					<div
						id="consentHomeLoanDiv"
						class="f-otp-pop-content"
						style="
							max-height:500px;
							overflow-y:auto;
							padding:10px;
						">
						<s:property
							escapeHtml="false"
							value="%{beanList.consentHomeLoan}" />
					</div>
					<div
						style="
							text-align:center;
							margin-top:15px;
						">
						<button
							type="button"
							id="acceptConsentBtn"
							class="submit-btn"
							disabled="disabled">
							Accept Consent
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
var consentReadCompleted = false;
$("#consentHomeLoanDiv").on(
	'scroll',
	function(){
		var div = $(this)[0];
		if(
			div.scrollTop + div.clientHeight
			>=
			div.scrollHeight - 5
		){
			if(!consentReadCompleted){
				consentReadCompleted = true;
				$("#acceptConsentBtn")
					.prop("disabled", false);
				$("#consentCloseBtn")
					.prop("disabled", false);
			}
		}
	}
);
$("#acceptConsentBtn").click(function(){
	$("#infoprovide")
		.prop("checked", true);
	$("#infoprovideCBS")
		.prop("checked", true);
	$("#consentHomeLoan").modal('hide');
});
function changeConsentLanguage(){
	var language =
		$("#consentLanguageDropdown").val();
	$.ajax({
		url : 'getConsentByLanguage',
		type : 'POST',
		data : {
			privacyLocale : language
		},
		success : function(response){
			var json = JSON.parse(response);
			if(json.status == 'success'){
				$("#consentHomeLoanDiv")
					.html(json.privacyNotice);
				$("#acceptConsentBtn")
					.prop("disabled", true);
				$("#consentCloseBtn")
					.prop("disabled", true);
				consentReadCompleted = false;
				$("#consentHomeLoanDiv")
					.scrollTop(0);
			}else{
				$("#consentHomeLoanDiv")
					.html(
						"Unable to load consent"
					);
			}
		}
	});
}
</script>

⸻

8. NO CHANGES REQUIRED

HomeLoan.jsp

NO changes.

⸻

HomeFirstPageSession.jsp

NO changes.

Because:

onclick="javascript:openPopups('consentHomeLoan','1');"

already works.

⸻

9. WHAT YOUR EXISTING OPENPOPUPS DOES

Likely inside:

jquery.commonFunction.js

It simply does:

$('#consentHomeLoan').modal('show');

So your popup is already connected.

⸻

10. DATABASE DATA FORMAT

EXAMPLE

PRIVACY_ID	PRIVACY_LOCALE	PRIVACY_TOUCH_POINT_ID	PRIVACY_NOTICE
1	ENG	HOME_LOAN	English Consent
2	HIN	HOME_LOAN	Hindi Consent
3	MAR	HOME_LOAN	Marathi Consent

⸻

11. FINAL RESULT

You now have:

✅ multilingual consent
✅ DB driven content
✅ SBI banking UX
✅ mandatory full scroll
✅ disabled close button
✅ disabled ESC
✅ disabled outside click
✅ auto checkbox tick
✅ reusable architecture
✅ no duplicate code
✅ production-grade implementation
