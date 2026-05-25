Below is the start-to-finish exact change.

1. CommonCbs.jsp

Change 1: hide consent by default

Find:

<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display :<s:property value="%{showCBS?'block':'none'}"/>;">

Replace with:

<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display:none;">

Because default selected radio is Yes, so consent should not show initially.

⸻

Change 2: keep your existing Read more link

Keep this as it is:

<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> Read more </a></b><b class="req">*</b>

No change needed here because openPopups() will handle popup opening.

⸻

2. jquery.cbs.js

Change 1: inside document.ready

Find this block:

jQuery(document).ready(function(){
	$("#commonCBSForm").validate({

Inside the same ready function, before the final closing:

});

add this:

	/*
	 * Default handling for relationship consent
	 */
	handleCBSRelationshipConsent();

⸻

Change 2: update YES radio function

Find:

jQuery(document).on("change","#relationshipWithSBIYes", function() {
	$('#relationshipWithSBINo').removeAttr("checked");
	$('#relationshipWithSBIYes').attr("checked", "checked");
	$('#relationshipWithSBIBank').show();

Replace this part with:

jQuery(document).on("change","#relationshipWithSBIYes", function() {
	$('#relationshipWithSBINo').removeAttr("checked");
	$('#relationshipWithSBIYes').attr("checked", "checked");
	$('#relationshipWithSBIBank').show();
	handleCBSRelationshipConsent();

Rest of the function stays same.

⸻

Change 3: update NO radio function

Find:

jQuery(document).on("change","#relationshipWithSBINo", function() {
	
	$('#relationshipWithSBIYes').removeAttr("checked");
	$('#relationshipWithSBINo').attr("checked", "checked");
	$('#relationshipWithSBIBank').hide();

Replace this part with:

jQuery(document).on("change","#relationshipWithSBINo", function() {
	
	$('#relationshipWithSBIYes').removeAttr("checked");
	$('#relationshipWithSBINo').attr("checked", "checked");
	$('#relationshipWithSBIBank').hide();
	handleCBSRelationshipConsent();

Rest of the function stays same.

⸻

Change 4: add this new function at bottom of jquery.cbs.js

Paste this at the bottom of file:

function handleCBSRelationshipConsent(){
	var relationshipValue =
		jQuery("input[name='relationshipWithSBI']:checked").val();
	if(relationshipValue == "2"){
		/*
		 * Relationship with SBI = NO
		 * Show consent checkbox and Read More
		 */
		jQuery("#termsAndConditionCBS").show();
		jQuery("#infoprovideCBS")
			.prop("disabled", false)
			.prop("checked", false);
	}else{
		/*
		 * Relationship with SBI = YES
		 * Hide consent checkbox and Read More
		 */
		jQuery("#termsAndConditionCBS").hide();
		jQuery("#infoprovideCBS")
			.prop("checked", false)
			.prop("disabled", true);
	}
}

⸻

Change 5: add consent validation in getCBSCall()

Find:

function getCBSCall(){
	remove_comma(document.commonCBSForm);
	addValidationsRules(document.commonCBSForm,fetchCBSJsonData());

Replace with:

function getCBSCall(){
	remove_comma(document.commonCBSForm);
	addValidationsRules(document.commonCBSForm,fetchCBSJsonData());
	var relationshipValue =
		jQuery("input[name='relationshipWithSBI']:checked").val();
	if(relationshipValue == "2"){
		if(!jQuery("#infoprovideCBS").is(":checked")){
			showMsg(
				"<em>Please read and accept the consent before proceeding.</em>",
				false
			);
			return false;
		}
	}else{
		jQuery("#infoprovideCBS")
			.prop("checked", false)
			.prop("disabled", true);
	}

Rest of getCBSCall() stays same.

⸻

3. jquery.commonFunction.js

Change openPopups() function

Find:

function openPopups(openForLoan, openForProduct){
	$("#"+openForLoan+"HTML").mCustomScrollbar({
		theme:"rounded",
		scrollInertia:5
	});
	
	const openForLoanEl = document.getElementById(openForLoan);
	const openForLoanModal = new bootstrap.Modal(openForLoanEl, {
	  backdrop: 'static',
	  keyboard: false
	});
	openForLoanModal.show();
}

Replace with:

function openPopups(openForLoan, openForProduct){
	$("#"+openForLoan+"HTML").mCustomScrollbar({
		theme:"rounded",
		scrollInertia:5
	});
	
	const openForLoanEl =
		document.getElementById(openForLoan);
	const openForLoanModal =
		bootstrap.Modal.getOrCreateInstance(
			openForLoanEl,
			{
				backdrop: 'static',
				keyboard: false
			}
		);
	openForLoanModal.show();
	if(openForLoan == "consentHomeLoan"){
		if(typeof loadPrivacyLanguageDropdown == "function"){
			loadPrivacyLanguageDropdown();
		}else if(typeof loadPrivacyByLocale == "function"){
			jQuery("#privacyLocaleDropdown").val("eng");
			loadPrivacyByLocale("eng");
		}
	}
}

⸻

4. ConsentPopup.jsp

In your popup JS, add this Accept button click code:

jQuery(document).on("click", "#acceptConsentBtn", function(){
	jQuery("#infoprovideCBS")
		.prop("checked", true)
		.prop("disabled", false);
	var consentModalEl =
		document.getElementById("consentHomeLoan");
	var consentModal =
		bootstrap.Modal.getInstance(consentModalEl);
	if(consentModal != null){
		consentModal.hide();
	}
});

This will:

Scroll bottom
↓
Accept button enabled
↓
Click Accept
↓
infoprovideCBS checkbox checked
↓
Popup closes

⸻

Final behavior:

Default Yes selected
→ SBI account details shown
→ consent hidden
User selects No
→ SBI account details hidden
→ actual loan form shown
→ consent checkbox + Read More shown
User clicks Read More
→ popup opens
→ English consent loads
User scrolls bottom + clicks Accept
→ checkbox checked
→ popup closes
Submit
→ allowed only after consent accepted
