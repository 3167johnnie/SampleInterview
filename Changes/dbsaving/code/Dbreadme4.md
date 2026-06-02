Yes, this is the best and cleanest approach for your current flow.

Since customer clicks Get Loan Quote, application record is created/saved at that time. So do this:

Accept consent popup
→ generate ntbId from frontend mobile + DOB + loanTypeId
→ store values in hidden fields only
→ close popup
→ when Get Loan Quote is clicked
→ hidden fields go with form
→ backend saves APP_PRIVACY_CONSENT_FLAG = Y and APP_NTB_ID in RUPEEPOWER_OCAS_T_00195

No separate DB call on Accept needed.

⸻

1. DB change

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);
ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_NTB_ID VARCHAR2(100);

⸻

2. ApplicationFormHomeLoan.java

Add below existing:

@Column(name="APP_CONSENT_ID")
private Integer appConsentId;

Add:

@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;
@Column(name="APP_NTB_ID")
private String appNtbId;

Add getters/setters:

public String getAppPrivacyConsentFlag() {
	return appPrivacyConsentFlag;
}
public void setAppPrivacyConsentFlag(String appPrivacyConsentFlag) {
	this.appPrivacyConsentFlag = appPrivacyConsentFlag;
}
public String getAppNtbId() {
	return appNtbId;
}
public void setAppNtbId(String appNtbId) {
	this.appNtbId = appNtbId;
}

⸻

3. HomeFirstPage.jsp

Before this:

<input type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">

Add hidden fields:

<s:hidden name="appForm.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="appForm.appNtbId" id="appNtbId" />

Final:

<s:hidden name="appForm.appPrivacyConsentFlag" id="appPrivacyConsentFlag" />
<s:hidden name="appForm.appNtbId" id="appNtbId" />
<input type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">

⸻

4. ConsentPopup.jsp button correction

Replace your current broken button:

<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="savePrivacyConsent(); style="opacity: 0.6; cursor: not-allowed;">Accept</button>

With:

<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity: 0.6; cursor: not-allowed;">
	Accept
</button>

⸻

5. ConsentPopup.jsp JS changes

Remove this old function fully:

function savePrivacyConsent(){
	$.ajax({
		url : "savePrivacyConsent",
		type : "POST",
		success : function(response){
			var json = JSON.parse(response);
			if(json.status == "success"){
				$("#consentHomeLoan").modal("hide");
			}else{
				alert(json.message);
			}
		},
		error : function(){
			alert("Unable to save consent. Please try again.");
		}
	});
}

Add this instead:

function acceptPrivacyConsent(){
	var mobile = $("#mobile").val();
	var dob = $("#date_of_birth").val();
	if(mobile == null || $.trim(mobile) == ""){
		alert("Please enter mobile number before accepting consent.");
		return false;
	}
	if(dob == null || $.trim(dob) == ""){
		alert("Please enter date of birth before accepting consent.");
		return false;
	}
	var cleanMobile = $.trim(mobile).replace(/\D/g, "");
	var cleanDob = $.trim(dob).replace(/\D/g, "");
	var ntbId = cleanMobile + cleanDob + loanTypeId;
	$("#appPrivacyConsentFlag").val("Y");
	$("#appNtbId").val(ntbId);
	if ($("#infoprovide").length > 0) {
		$("#infoprovide")
			.prop("disabled", false)
			.prop("checked", true);
	}
	if ($("#infoprovideCBS").length > 0) {
		$("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", true);
	}
	$("#consentHomeLoan").modal("hide");
}

⸻

6. Keep scroll validation

Your existing resetConsentScrollValidation() is fine. Keep it.

⸻

7. Final submit validation

Before Get Quote submit, add validation:

$(document).on("click", "#subtn", function(){
	if($("#appPrivacyConsentFlag").val() != "Y"){
		alert("Please read and accept the consent before proceeding.");
		return false;
	}
});

⸻

8. Action class

No separate savePrivacyConsent() action is needed now.

Because hidden fields will bind automatically to:

appForm.appPrivacyConsentFlag
appForm.appNtbId

and when existing Get Quote flow saves appForm, DB will store:

APP_PRIVACY_CONSENT_FLAG = 'Y'
APP_NTB_ID = mobile + dob + loanTypeId

So remove/avoid this action:

<action name="savePrivacyConsent" ...>

This is the correct flow for “save only when Get Loan Quote is called.”
