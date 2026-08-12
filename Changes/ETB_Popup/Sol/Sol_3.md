Yes. With the code you shared, the popup problem and the new requirement can be handled without adding any consent-saving logic.

The important points are:

1. Your checkbox must be checked by default.
2. It must not be disabled, otherwise the user cannot click it and onclick will not fire.
3. Clicking the checked checkbox must open the popup.
4. The checkbox must remain checked after clicking.
5. The popup needs a unique ID, for example consentPopupETB.
6. Your openPopups() must receive exactly that ID.
7. The popup only needs:
    * Language dropdown
    * Privacy Notice content
    * Close button
8. No acceptPrivacyConsent()
9. No quotePrivacyConsentFlag
10. No quoteNtbId
11. No submitConsent()
12. No database save
13. Language list and privacy-content APIs can remain common across loans.

⸻

1. First understand your current error

Your current HTML has:

onclick="javascript:openPopups('consentPopupETB','1');"

So JavaScript receives:

openForLoan = "consentPopupETB"

Your function does:

const openForLoanEl = document.getElementById(openForLoan);

Therefore it searches for:

document.getElementById("consentPopupETB")

Your popup actually has:

<div class="modal fade otp-box" id="consentPopupETB">

So that part is correct.

But your other JavaScript contains:

$(document).on("show.bs.modal", "#consentHomeLoan", ...)

and:

$(document).on("shown.bs.modal", "#consentHomeLoan", ...)

and:

$("#consentHomeLoan").modal("hide");

Those are Home Loan IDs, while your popup is:

consentPopupETB

This is inconsistent.

Also, your popup is Bootstrap 5, because you’re doing:

new bootstrap.Modal(...)

Therefore we should keep everything consistently Bootstrap 5.

⸻

2. Most important change — checkbox

Your current checkbox is:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">

Don’t use this:

disabled="disabled"

Because a disabled checkbox cannot be clicked.

Instead use:

<input type="checkbox"
       class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on"
       checked="checked"
       onclick="openETBPrivacyPopup(this); return false;">

Why?

Initially:

checked = true

When the user clicks:

openETBPrivacyPopup(this)

runs.

Then:

return false

prevents the checkbox from changing from checked → unchecked.

So the behavior becomes:

Page loads
    ↓
Checkbox = CHECKED
    ↓
User clicks checkbox
    ↓
Popup opens
    ↓
Checkbox remains CHECKED
    ↓
User closes popup
    ↓
Checkbox still CHECKED

That exactly matches your requirement.

⸻

3. Change your existing Auto/ETB page

Replace your current consent section:

<div id="termsAndConditionFirst" class="sbi-trms-div">
...
</div>

with the following.

<div id="termsAndConditionETB" class="sbi-trms-div">
    <ul class="form-section">
        <li class="full-width">
            <div class="trms-section">
                <input type="checkbox"
                       class="blue-css-checkbox"
                       name="infoprovide"
                       id="infoprovideETB"
                       value="on"
                       checked="checked"
                       onclick="openETBPrivacyPopup(this); return false;">
                <label for="infoprovideETB"
                       class="label-content"
                       style="font-size:14px;">
                    I hereby authorize State Bank of India and/or its representative
                    to contact me with reference to my application.
                    For more details please read
                    <b>
                        <a href="javascript:void(0);"
                           onclick="openETBPrivacyPopup(document.getElementById('infoprovideETB')); return false;">
                            SBI Privacy Notice
                        </a>
                    </b>
                    <b class="req">*</b>
                </label>
            </div>
        </li>
    </ul>
</div>

⸻

4. Why I changed id="infoprovide" to id="infoprovideETB"

You already have Home Loan/Auto Loan consent code using IDs such as:

infoprovide
infoprovideCBS
consentHomeLoan
consentAutoLoanNTB

For this new ETB consent, use completely unique names:

infoprovideETB
consentPopupETB
consentETBHTML
consentETBContent
privacyETBDropdown

This is important because otherwise jQuery may find the wrong element.

⸻

5. Add the popup JSP

Your popup currently starts with:

<div id="termAndConditionHTML">

I recommend changing this to:

<div id="consentETBHTML">

Then use this popup.

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="consentETBHTML">
    <div class="modal fade otp-box"
         id="consentPopupETB"
         tabindex="-1"
         aria-labelledby="consentPopupETBLabel"
         aria-hidden="true"
         data-bs-backdrop="static"
         data-bs-keyboard="false">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="privacy-modal-body">
                    <!-- CLOSE BUTTON -->
                    <button type="button"
                            class="close clo"
                            data-bs-dismiss="modal"
                            aria-label="Close">
                        <span aria-hidden="true">
                            <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" />
                        </span>
                    </button>
                    <!-- LANGUAGE DROPDOWN -->
                    <div class="privacy-consent-dropdown">
                        <select id="privacyETBDropdown"
                                class="privacy-consent-dropdown"
                                onchange="loadETBPrivacyByLocale(this.value);">
                            <option value="eng">
                                English
                            </option>
                        </select>
                    </div>
                    <!-- PRIVACY NOTICE CONTENT -->
                    <div id="consentETBContent"
                         class="privacy-consent-pop-content">
                        Loading Privacy Notice...
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

⸻

6. Important — there is NO Accept button now

Your old popup contains:

<button type="button"
        id="acceptConsentBtn"
        ...
        onclick="acceptPrivacyConsent();">
    Accept
</button>

Remove it completely.

Your requirement is:

Popup should open, language dropdown and privacy content should display. No record or saving.

Therefore:

Checkbox click
      ↓
Popup
      ↓
Language dropdown
      ↓
Privacy content
      ↓
Close

There is no:

Accept
   ↓
AJAX
   ↓
Save
   ↓
Database

⸻

7. JavaScript for opening this popup

Add this to the popup JSP or common JS.

function openETBPrivacyPopup(checkbox) {
    // Always keep checkbox selected
    if (checkbox) {
        checkbox.checked = true;
    }
    var popupElement = document.getElementById("consentPopupETB");
    if (!popupElement) {
        console.error("consentPopupETB element not found.");
        return false;
    }
    // Bootstrap 5 modal
    var popupInstance = bootstrap.Modal.getOrCreateInstance(
        popupElement,
        {
            backdrop: "static",
            keyboard: false
        }
    );
    popupInstance.show();
    // Load language list and English privacy notice
    loadETBPrivacyLanguages();
    return false;
}

⸻

8. Why getOrCreateInstance() is better here

You currently have:

const openForLoanModal = new bootstrap.Modal(openForLoanEl, {
    backdrop: 'static',
    keyboard: false
});

That can work.

But for your new popup, use:

bootstrap.Modal.getOrCreateInstance(...)

This means:

If Bootstrap already has an instance of this modal, use it. Otherwise create one.

So:

var popupInstance = bootstrap.Modal.getOrCreateInstance(
    popupElement,
    {
        backdrop: "static",
        keyboard: false
    }
);

then:

popupInstance.show();

⸻

9. Your generic openPopups() also needs correction

You showed:

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

For your new ETB popup, I recommend making it safer:

function openPopups(openForLoan, openForProduct) {
    var openForLoanEl = document.getElementById(openForLoan);
    if (!openForLoanEl) {
        console.error("Popup element not found: " + openForLoan);
        return false;
    }
    var openForLoanModal =
        bootstrap.Modal.getOrCreateInstance(
            openForLoanEl,
            {
                backdrop: "static",
                keyboard: false
            }
        );
    openForLoanModal.show();
    return false;
}

Then this also works:

onclick="openPopups('consentPopupETB','1'); return false;"

⸻

10. But I recommend NOT using openPopups() for this new popup

Because your existing openPopups() is being used by several existing loan flows.

You don’t want to accidentally break Home Loan or Auto Loan.

Therefore:

Existing loans

Keep:

openPopups(...)

New ETB popup

Use:

openETBPrivacyPopup(...)

This isolates the new requirement.

⸻

11. Language dropdown

You said:

Language code list and dropdown will be same across the loan.

Correct.

So don’t create:

getETBLanguageList

if you already have:

getPrivacyLanguageList

Use the existing common API:

function loadETBPrivacyLanguages() {
    $.ajax({
        url: "getPrivacyLanguageList",
        type: "POST",
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                var optionHtml = "";
                $.each(json.languageList, function(index, item) {
                    optionHtml +=
                        "<option value='" +
                        item.locale +
                        "'>" +
                        item.languageName +
                        "</option>";
                });
                $("#privacyETBDropdown").html(optionHtml);
                // Default English
                if ($("#privacyETBDropdown option[value='eng']").length > 0) {
                    $("#privacyETBDropdown").val("eng");
                    loadETBPrivacyByLocale("eng");
                } else {
                    var firstLocale =
                        $("#privacyETBDropdown option:first").val();
                    if (firstLocale) {
                        loadETBPrivacyByLocale(firstLocale);
                    }
                }
            } else {
                $("#privacyETBDropdown").html(
                    "<option value='eng'>English</option>"
                );
                loadETBPrivacyByLocale("eng");
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyLanguageList failed:",
                xhr.status,
                xhr.responseText
            );
            $("#privacyETBDropdown").html(
                "<option value='eng'>English</option>"
            );
            loadETBPrivacyByLocale("eng");
        }
    });
}

⸻

12. Privacy content AJAX

Again, use the same common privacy API:

getPrivacyNoticeByLocale

Don’t create a new API merely because the popup is ETB.

Use:

function loadETBPrivacyByLocale(locale) {
    if (!locale) {
        locale = $("#privacyETBDropdown").val();
    }
    $("#consentETBContent").html(
        "Loading Privacy Notice..."
    );
    $.ajax({
        url: "getPrivacyNoticeByLocale",
        type: "POST",
        data: {
            privacyLocale: locale
        },
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                $("#consentETBContent").html(
                    json.privacyNotice
                );
                $("#consentETBContent").scrollTop(0);
            } else {
                $("#consentETBContent").html(
                    "Privacy Notice Not Found"
                );
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyNoticeByLocale failed:",
                xhr.status,
                xhr.responseText
            );
            $("#consentETBContent").html(
                "Unable To Load Privacy Notice"
            );
        }
    });
}

⸻

13. Final JavaScript for this new popup

So the new popup JavaScript can simply be:

function openETBPrivacyPopup(checkbox) {
    // Checkbox must always remain checked
    if (checkbox) {
        checkbox.checked = true;
    }
    var popupElement =
        document.getElementById("consentPopupETB");
    if (!popupElement) {
        console.error(
            "consentPopupETB element not found."
        );
        return false;
    }
    var popupInstance =
        bootstrap.Modal.getOrCreateInstance(
            popupElement,
            {
                backdrop: "static",
                keyboard: false
            }
        );
    popupInstance.show();
    loadETBPrivacyLanguages();
    return false;
}
function loadETBPrivacyLanguages() {
    $.ajax({
        url: "getPrivacyLanguageList",
        type: "POST",
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                var optionHtml = "";
                $.each(
                    json.languageList,
                    function(index, item) {
                        optionHtml +=
                            "<option value='" +
                            item.locale +
                            "'>" +
                            item.languageName +
                            "</option>";
                    }
                );
                $("#privacyETBDropdown")
                    .html(optionHtml);
                if (
                    $("#privacyETBDropdown option[value='eng']")
                    .length > 0
                ) {
                    $("#privacyETBDropdown")
                        .val("eng");
                    loadETBPrivacyByLocale("eng");
                } else {
                    var firstLocale =
                        $("#privacyETBDropdown option:first")
                        .val();
                    loadETBPrivacyByLocale(
                        firstLocale
                    );
                }
            } else {
                $("#privacyETBDropdown").html(
                    "<option value='eng'>English</option>"
                );
                loadETBPrivacyByLocale("eng");
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyLanguageList failed:",
                xhr.status,
                xhr.responseText
            );
            $("#privacyETBDropdown").html(
                "<option value='eng'>English</option>"
            );
            loadETBPrivacyByLocale("eng");
        }
    });
}
function loadETBPrivacyByLocale(locale) {
    if (!locale) {
        locale =
            $("#privacyETBDropdown").val();
    }
    $("#consentETBContent").html(
        "Loading Privacy Notice..."
    );
    $.ajax({
        url: "getPrivacyNoticeByLocale",
        type: "POST",
        data: {
            privacyLocale: locale
        },
        success: function(response) {
            var json =
                typeof response === "string"
                ? JSON.parse(response)
                : response;
            if (json.status === "success") {
                $("#consentETBContent").html(
                    json.privacyNotice
                );
                $("#consentETBContent")
                    .scrollTop(0);
            } else {
                $("#consentETBContent").html(
                    "Privacy Notice Not Found"
                );
            }
        },
        error: function(xhr) {
            console.error(
                "getPrivacyNoticeByLocale failed:",
                xhr.status,
                xhr.responseText
            );
            $("#consentETBContent").html(
                "Unable To Load Privacy Notice"
            );
        }
    });
}

⸻

14. Now look at your CommonLoanAction.java

This is very important.

You showed:

public StreamResult submitConsent() throws JSONException {

This method is not required for your new requirement.

It does:

List<ConsentRequestConsent> consents =
    processManagerImpl.buildConsents(
        request,
        essentialPurpose,
        selectedPurposes,
        allPurposes,
        otherPurpose
    );

Then:

processManagerImpl.submitConsent(consents)

That is the actual consent submission flow.

Therefore do not call this method for your new popup.

Your new flow is only:

JSP
 ↓
JavaScript
 ↓
getPrivacyLanguageList
 ↓
JavaScript
 ↓
getPrivacyNoticeByLocale
 ↓
Display

No:

submitConsent()

⸻

15. Your existing submitConsent() can remain

You don’t necessarily need to delete this:

public StreamResult submitConsent() throws JSONException {

because other ETB consent functionality may still use it.

Just make sure your new popup doesn’t call it.

This is an important distinction:

Existing ETB consent form

Could still use:

submitConsent()

Your new Privacy Notice popup

Uses:

getPrivacyLanguageList
getPrivacyNoticeByLocale

only.

⸻

16. Do you need changes in CommonLoanAction.java?

Based strictly on the code you’ve shown, not necessarily.

Your JavaScript calls:

url: "getPrivacyLanguageList"

and:

url: "getPrivacyNoticeByLocale"

Those actions must already exist somewhere in your Struts configuration/action classes.

Your CommonLoanAction.java snippet does not contain either:

getPrivacyLanguageList()

or:

getPrivacyNoticeByLocale()

So don’t add duplicate methods until we identify where those existing endpoints are implemented.

You should search your project for:

getPrivacyLanguageList

and:

getPrivacyNoticeByLocale

You will probably find the existing implementation that Home Loan is already using.

⸻

17. Very important: your current popup has a naming mismatch

You currently have:

$(document).on("show.bs.modal", "#consentHomeLoan", function(e) {

But your actual popup is:

id="consentPopupETB"

For this new popup, remove these handlers entirely:

$(document).on("show.bs.modal", "#consentHomeLoan", function(e) {
    ...
});

and:

$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
    ...
});

They belong to the old Home Loan implementation.

⸻

18. Remove this old function

You don’t need:

function canOpenPrivacyPopup() {

because it checks:

$("#mobile").val()

and:

$("#date_of_birth").val()

Your new requirement says:

Just open popup and show language + privacy content.

So there is no need to prevent opening based on mobile/DOB.

⸻

19. Remove this old function

Don’t use:

function acceptPrivacyConsent()

because this is specifically doing saving-related preparation:

var ntbId = cleanMobile + cleanDob + loanTypeId;

and:

$("#quotePrivacyConsentFlag").val("Y");

and:

$("#quoteNtbId").val(ntbId);

Those are not required.

⸻

20. Remove these fields from the new popup

Don’t have:

$("#quotePrivacyConsentFlag").val("");

or:

$("#quoteNtbId").val("");

or:

$("#quotePrivacyLocale").val(selectedLocale);

because those belong to the save flow.

⸻

21. Your new architecture

The complete architecture should now be:

                 ETB PAGE
                    |
                    |
             Checkbox checked
                    |
                    ↓
        User clicks checkbox
                    |
                    ↓
        openETBPrivacyPopup()
                    |
                    ↓
        consentPopupETB
                    |
          ┌─────────┴─────────┐
          ↓                   ↓
 Language Dropdown       Privacy Content
          |                   |
          ↓                   ↓
getPrivacyLanguageList   getPrivacyNoticeByLocale
          |                   |
          └─────────┬─────────┘
                    ↓
              Display Notice
                    |
                    ↓
                  Close

There is no database operation anywhere in this flow.

⸻

22. Checkbox behavior

This is the exact behavior you requested.

Initial page:

<input
    type="checkbox"
    id="infoprovideETB"
    checked
>

Browser displays:

☑

User clicks:

onclick="openETBPrivacyPopup(this); return false;"

The function does:

checkbox.checked = true;

Then:

popupInstance.show();

So:

☑ → Popup opens → ☑

It never becomes:

☐

⸻

23. Why disabled must NOT be used

Your original:

<input ... disabled="disabled">

means:

Browser
   ↓
Checkbox disabled
   ↓
User clicks
   ↓
Nothing happens
   ↓
onclick does not execute

So don’t do:

checked disabled

Use:

checked
onclick="openETBPrivacyPopup(this); return false;"

⸻

24. If you want the label/text also to open popup

I recommend allowing both:

Checkbox click

onclick="openETBPrivacyPopup(this); return false;"

SBI Privacy Notice click

onclick="openETBPrivacyPopup(document.getElementById('infoprovideETB')); return false;"

Therefore both work:

☑ checkbox      → popup
SBI Privacy Notice → popup

⸻

25. One more important issue in your original popup

You have:

aria-bs-label="Close"

This is incorrect.

Use:

aria-label="Close"

So:

<button type="button"
        class="close clo"
        data-bs-dismiss="modal"
        aria-label="Close">

⸻

26. Your final files/changes

I would structure it like this:

Existing ETB JSP

Add:

infoprovideETB

and:

openETBPrivacyPopup()

⸻

New/modified popup JSP

Use:

consentETBHTML
consentPopupETB
privacyETBDropdown
consentETBContent

⸻

JavaScript

Add:

openETBPrivacyPopup()
loadETBPrivacyLanguages()
loadETBPrivacyByLocale()

⸻

Existing common APIs

Reuse:

getPrivacyLanguageList
getPrivacyNoticeByLocale

⸻

CommonLoanAction

No submitConsent() call for this popup.

Your existing:

submitConsent()

can stay for whatever existing functionality uses it.

⸻

27. Final naming map

To avoid mixing Home Loan, Auto Loan and ETB IDs:

Purpose	New ETB name
Main checkbox	infoprovideETB
Consent section	termsAndConditionETB
Popup wrapper	consentETBHTML
Bootstrap modal	consentPopupETB
Language dropdown	privacyETBDropdown
Privacy content	consentETBContent
Open function	openETBPrivacyPopup()
Language function	loadETBPrivacyLanguages()
Content function	loadETBPrivacyByLocale()
Common language API	getPrivacyLanguageList
Common content API	getPrivacyNoticeByLocale
Save function	None
DB save	None
Accept button	None

This separation is especially important because your existing code has Home Loan IDs such as consentHomeLoan mixed with the new consentPopupETB, which is one of the reasons the JavaScript behavior becomes confusing.

One thing I would check before you deploy

Search the project for these two exact strings:

getPrivacyLanguageList
getPrivacyNoticeByLocale

and also search for:

consentPopupETB

If you send me the Struts XML/action mapping for these two APIs and the Java methods that implement getPrivacyLanguageList and getPrivacyNoticeByLocale, I can trace the complete request JSP → JavaScript → Struts → Action → Service/DAO → response → popup, line by line, and tell you exactly whether any database save is happening.
