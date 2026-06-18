
Apply Auto Loan same as Home Loan, but use common Privacy Notice popup.

1) AutoFirstPageSession.jsp

Replace old Auto consent block

Remove this:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide"  value="on">
<label for="infoprovide" class="label-content consentScrollForAuto scollerClass" id="autoID">
	<s:property escapeHtml="false" value="%{beanList.consentAutoLoanNTB}" />
	&nbsp;<b class="req">*</b>
</label>

Add this:

<input type="checkbox"
	class="blue-css-checkbox"
	name="infoprovide"
	id="infoprovide"
	value="on"
	disabled="disabled">
<label for="infoprovide" class="label-content" style="font-size:14px;">
	I hereby authorize State Bank of India and/or its representative to contact me with reference to my application.
	For more details please read
	<b>
		<a href="javascript:void(0);" onclick="openPrivacyConsentPopup();">
			SBI’s Privacy Notice
		</a>
	</b>
	<b class="req">*</b>
</label>

Remove old scrollbar script

Remove:

<script>
$(".consentScrollForAuto").mCustomScrollbar({
	theme:"rounded",
	scrollInertia:5,
	mouseWheel:{scrollAmount:30},
	scrollButtons:{
		enable:true
	}
	
});
</script>

Add submit validation at bottom:

<script>
$(document).on("click", "#subtn", function() {
	return validatePrivacyConsentBeforeSubmit();
});
</script>

⸻

2) Add hidden fields in Auto Loan main form JSP

Inside Auto Loan <form>, add:

<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />

This should be inside same form where quote.loanQuoteLoanPurposeId etc. are submitted.

⸻

3) Include common popup + JS in Auto Loan page

Where Auto first page is rendered, add:

<s:include value="/appNew/common/ConsentPopup.jsp" />

Add common JS:

<script src="<%=request.getContextPath()%>/appNew/common/js/privacyConsentCommon.js"></script>

Add Auto Loan config:

<script>
configurePrivacyConsent({
	modalId: "consentPrivacyNotice",
	contentDivId: "consentPrivacyNoticeDiv",
	acceptBtnId: "acceptConsentBtn",
	localeDropdownId: "privacyLocaleDropdown",
	mobileFieldId: "mobile",
	dobFieldId: "date_of_birth",
	loanTypeId: "<s:property value='%{loanTypeId}'/>",
	consentFlagFieldId: "quotePrivacyConsentFlag",
	ntbIdFieldId: "quoteNtbId",
	localeFieldId: "quotePrivacyLocale",
	checkboxIds: ["infoprovide"],
	privacyNoticeUrl: "getPrivacyNoticeByLocale"
});
</script>

⸻

4) AutoLoanAction.java

In generateUIBeanList(Integer moduleId, int pageNo), remove old NTB consent loading:

Integer consentIdNtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "NTB").getConsentId();
String consentTextNtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdNtb);
beanList.setConsentAutoLoanNtb(consentTextNtb);

Replace with only language dropdown load:

languages = commonService.getAllActiveLanguages();
logger.info("Privacy dropdown language count : "
		+ (languages == null ? 0 : languages.size()));

Keep ETB consent if still used:

Integer consentIdEtb = commonService.getConsentByLoanAndCustomerType(Constants.AUTO_LOAN_ID, "ETB").getConsentId();
String consentTextEtb = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentIdEtb);
beanList.setConsentAutoLoanEtb(consentTextEtb);

⸻

5) Add fields in AutoLoanAction.java

Add imports:

import java.util.List;
import com.mintstreet.consent.entity.MasterLanguage;

Add class variable:

private List<MasterLanguage> languages;

Add getter/setter:

public List<MasterLanguage> getLanguages() {
	return languages;
}
public void setLanguages(List<MasterLanguage> languages) {
	this.languages = languages;
}

⸻

6) ApplicationFormAutoLoanQuote.java

Add fields:

@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
private String quotePrivacyConsentFlag;
@Column(name = "QUOTE_NTB_ID")
private String quoteNtbId;
@Column(name = "QUOTE_PRIVACY_LOCALE")
private String quotePrivacyLocale;

Add getters/setters:

public String getQuotePrivacyConsentFlag() {
	return quotePrivacyConsentFlag;
}
public void setQuotePrivacyConsentFlag(String quotePrivacyConsentFlag) {
	this.quotePrivacyConsentFlag = quotePrivacyConsentFlag;
}
public String getQuoteNtbId() {
	return quoteNtbId;
}
public void setQuoteNtbId(String quoteNtbId) {
	this.quoteNtbId = quoteNtbId;
}
public String getQuotePrivacyLocale() {
	return quotePrivacyLocale;
}
public void setQuotePrivacyLocale(String quotePrivacyLocale) {
	this.quotePrivacyLocale = quotePrivacyLocale;
}

⸻

7) Auto backend validation

Where Auto Loan checks:

!quote.getInfoprovide().equals("on")

add strict privacy check before processGetQuotes/BRE:

if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
	responseMessage = "error|Please read and accept SBI Privacy Notice before proceeding.";
	return "jsonResponsePage";
}
if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
	responseMessage = "error|Invalid consent details. Please accept SBI Privacy Notice again.";
	return "jsonResponsePage";
}
if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
	responseMessage = "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";
	return "jsonResponsePage";
}

⸻

8) DB columns for Auto quote table

Add to Auto quote table:

ALTER TABLE RUPEEPOWER_OCAS_T_AUTO_QUOTE
ADD QUOTE_PRIVACY_CONSENT_FLAG VARCHAR2(1);
ALTER TABLE RUPEEPOWER_OCAS_T_AUTO_QUOTE
ADD QUOTE_NTB_ID VARCHAR2(50);
ALTER TABLE RUPEEPOWER_OCAS_T_AUTO_QUOTE
ADD QUOTE_PRIVACY_LOCALE VARCHAR2(20);

Use actual Auto Loan quote table name from ApplicationFormAutoLoanQuote.java.

⸻

Final Auto flow:

Auto Loan page opens
→ common Privacy Notice popup used
→ user scrolls till bottom
→ Accept sets infoprovide checked + quotePrivacyConsentFlag=Y + quoteNtbId + locale
→ Get Loan Quote submits
→ Auto quote saves these values
→ backend validates Y
→ then CCMS Write
→ then BRE
