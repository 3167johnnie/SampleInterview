Below is the READY-MADE implementation using your entity:

```java
PrivacyRequestResponse.java
```

based on your existing JSP + Struts + jQuery architecture.
Your entity structure reference: 
Frontend popup flow reference: 

---

# 1. PrivacyRequestResponse.java

ADD THIS NAMED QUERY

```java
@NamedQueries({

	@NamedQuery(
		name="PrivacyRequestResponse.findAll",
		query="SELECT a FROM PrivacyRequestResponse a"
	),

	@NamedQuery(
		name="PrivacyRequestResponse.getPrivacyByTouchPointAndLocale",
		query="SELECT p FROM PrivacyRequestResponse p "
			+ "WHERE p.privacyTouchPointId=:privacyTouchPointId "
			+ "AND p.privacyLocale=:privacyLocale"
	),
})
```

---

# 2. CREATE DAO

# PrivacyRequestResponseDao.java

```java
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
	getPrivacyByTouchPointAndLocale(
			String touchPointId,
			String locale){

		Map<String,Object> params =
			new HashMap<String,Object>();

		params.put("privacyTouchPointId", touchPointId);
		params.put("privacyLocale", locale);

		try{

			return (PrivacyRequestResponse)getSingleResult(
				"PrivacyRequestResponse.getPrivacyByTouchPointAndLocale",
				params
			);

		}catch(Exception e){

			logger.info(
				"Exception in getPrivacyByTouchPointAndLocale",
				e
			);
		}

		return null;
	}
}
```

---

# 3. CommonService.java

ADD DAO VARIABLE

```java
private PrivacyRequestResponseDao privacyRequestResponseDao;
```

ADD GETTER SETTER

```java
public PrivacyRequestResponseDao getPrivacyRequestResponseDao() {
	return privacyRequestResponseDao;
}

public void setPrivacyRequestResponseDao(
		PrivacyRequestResponseDao privacyRequestResponseDao) {

	this.privacyRequestResponseDao =
			privacyRequestResponseDao;
}
```

ADD SERVICE METHOD

```java
public PrivacyRequestResponse
getPrivacyByTouchPointAndLocale(
		String touchPointId,
		String locale){

	return privacyRequestResponseDao
		.getPrivacyByTouchPointAndLocale(
				touchPointId,
				locale
		);
}
```

---

# 4. ACTION CLASS

Example:

```java
HomeLoanAction.java
```

---

# ADD VARIABLE

```java
private String privacyLocale;
```

---

# GETTER SETTER

```java
public String getPrivacyLocale() {
	return privacyLocale;
}

public void setPrivacyLocale(String privacyLocale) {
	this.privacyLocale = privacyLocale;
}
```

---

# ADD AJAX METHOD

```java
public StreamResult getPrivacyNoticeByLocale(){

	JSONObject json = new JSONObject();

	try{

		PrivacyRequestResponse privacyObj =
			commonService
			.getPrivacyByTouchPointAndLocale(
					"HOME_LOAN",
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
```

---

# 5. STRUTS.XML

```xml
<action
	name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyNoticeByLocale">

	<result type="stream">
		<param name="contentType">
			application/json
		</param>
	</result>

</action>
```

---

# 6. ConsentPopup.jsp

COMPLETE READY-MADE FRONTEND

```jsp
<div id="termAndConditionHTML">

	<div class="modal fade otp-box"
		id="consentHomeLoan"
		tabindex="-1"
		role="dialog"
		data-bs-backdrop="static"
		data-bs-keyboard="false"
		aria-labelledby="myModalLabel">

		<div class="modal-dialog"
			role="document">

			<div class="modal-content">

				<div class="modal-body">

					<button type="button"
						class="close clo"
						id="consentCloseBtn"
						disabled="disabled"
						aria-bs-label="Close"
						style="
							opacity:0.5;
							cursor:not-allowed;
						">

						<span aria-hidden="true">

							<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/>

						</span>

					</button>

					<!-- LANGUAGE DROPDOWN -->

					<div style="margin-bottom:15px;">

						<select
							id="privacyLocaleDropdown"
							class="form-control"
							onchange="loadPrivacyByLocale();">

							<option value="eng">
								English
							</option>

							<option value="hin">
								Hindi
							</option>

							<option value="mar">
								Marathi
							</option>

							<option value="mal">
								Malayalam
							</option>

							<option value="guj">
								Gujarati
							</option>

						</select>

					</div>

					<!-- PRIVACY CONTENT -->

					<div id="privacyNoticeDiv"
						class="f-otp-pop-content"
						style="
							max-height:500px;
							overflow-y:auto;
							padding-right:10px;
						">

						<s:property
							escapeHtml="false"
							value="%{beanList.consentHomeLoan}" />

					</div>

					<!-- ACCEPT BUTTON -->

					<div style="
						text-align:center;
						margin-top:20px;">

						<button type="button"
							id="acceptConsentBtn"
							class="submit-btn"
							disabled="disabled"
							style="
								opacity:0.5;
								cursor:not-allowed;
							">

							I Agree

						</button>

					</div>

				</div>

			</div>

		</div>

	</div>

</div>
```

---

# 7. JAVASCRIPT

ADD AT BOTTOM OF JSP

```html
<script type="text/javascript">

var consentReadCompleted = false;


/* LOAD LANGUAGE */

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

				/* RESET SCROLL */

				$("#privacyNoticeDiv")
					.scrollTop(0);

				consentReadCompleted = false;

				$("#acceptConsentBtn")
					.prop("disabled", true)
					.css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});

				$("#consentCloseBtn")
					.prop("disabled", true)
					.css({
						"opacity":"0.5",
						"cursor":"not-allowed"
					});

			}else{

				alert("Privacy Notice Not Found");
			}
		},

		error : function(){

			alert("Unable To Load Data");
		}
	});
}


/* RESET WHEN POPUP OPENS */

$('#consentHomeLoan').on(
	'shown.bs.modal',
	function () {

	consentReadCompleted = false;

	$("#acceptConsentBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.5",
			"cursor":"not-allowed"
		});

	$("#consentCloseBtn")
		.prop("disabled", true)
		.css({
			"opacity":"0.5",
			"cursor":"not-allowed"
		});

	$("#privacyNoticeDiv")
		.scrollTop(0);
});


/* SCROLL DETECTION */

$("#privacyNoticeDiv").on(
	"scroll",
	function () {

	var div = $(this)[0];

	if (
		div.scrollTop + div.clientHeight
		>= div.scrollHeight - 5
	) {

		if(!consentReadCompleted){

			consentReadCompleted = true;

			$("#acceptConsentBtn")
				.prop("disabled", false)
				.css({
					"opacity":"1",
					"cursor":"pointer"
				});

			$("#consentCloseBtn")
				.prop("disabled", false)
				.css({
					"opacity":"1",
					"cursor":"pointer"
				});
		}
	}
});


/* ACCEPT BUTTON */

$("#acceptConsentBtn").on(
	"click",
	function () {

	if(consentReadCompleted){

		$("#infoprovide")
			.prop("checked", true);

		$("#infoprovideCBS")
			.prop("checked", true);

		$("#consentHomeLoan")
			.modal("hide");
	}
});


/* CLOSE BUTTON */

$("#consentCloseBtn").on(
	"click",
	function () {

	if(consentReadCompleted){

		$("#consentHomeLoan")
			.modal("hide");
	}
});

</script>
```

---

# 8. DB DATA FORMAT

| PRIVACY_ID | PRIVACY_TOUCH_POINT_ID | PRIVACY_LOCALE | PRIVACY_NOTICE    |
| ---------- | ---------------------- | -------------- | ----------------- |
| 1          | HOME_LOAN              | eng            | English content   |
| 2          | HOME_LOAN              | hin            | Hindi content     |
| 3          | HOME_LOAN              | mar            | Marathi content   |
| 4          | HOME_LOAN              | mal            | Malayalam content |

---

# 9. FLOW

```text
Read More
   ↓
Popup Opens
   ↓
Language Dropdown Change
   ↓
AJAX Call
   ↓
Struts Action
   ↓
CommonService
   ↓
PrivacyRequestResponseDao
   ↓
DB Fetch
   ↓
Update HTML
   ↓
User Scroll Till End
   ↓
Accept Enabled
   ↓
Checkbox Auto Checked
```
