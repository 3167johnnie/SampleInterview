Yes. With your exact requirement, the minimal production-ready change is:

1. Checkbox starts unchecked.
2. User cannot manually select/unselect it.
3. Clicking SBI’s Privacy Notice opens openPopups('consentCveLoan','1').
4. Only after Privacy Notice is accepted, JavaScript automatically checks the checkbox.
5. Once checked, it cannot be unchecked.
6. Submit is blocked unless the checkbox is checked.
7. Your existing hidden appPrivacyConsentFlag should also be Y only after acceptance.

1. Change only the checkbox

Replace your current:

<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" readonly >

with:

<input type="checkbox"
       class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on"
       disabled="disabled">

Do not use readonly for the checkbox.

⸻

2. Remove this existing code

Remove:

$('#infoprovide').change(function() {
    if ($(this).is(':checked')) {
    } else {
        $('#infoprovide').prop('disabled', true);
    }
});

Also remove:

$('input[type="checkbox"][readonly]').on('click keydown', function(e) {
    e.preventDefault();
});

You don’t need either of these.

⸻

3. Add one function for successful Privacy Notice acceptance

Add this to your existing script:

function privacyConsentAccepted() {
    $('#infoprovide')
        .prop('disabled', false)
        .prop('checked', true);
    $('#appPrivacyConsentFlag').val('Y');
}

This function should be called only after the user clicks Accept inside ConsentPopupCve.jsp.

Do not call this when openPopups() is opened.

⸻

4. Modify the Accept logic inside ConsentPopupCve.jsp

Wherever your existing Privacy Notice popup has the successful Accept logic, after the consent has been successfully accepted/saved, call:

if (window.privacyConsentAccepted) {
    window.privacyConsentAccepted();
}

For example, if your existing code is something like:

function acceptConsent() {
    // existing consent processing
    // AJAX / save consent etc.
    if (success) {
        window.privacyConsentAccepted();
    }
}

This is the only place where the checkbox should become checked.

⸻

5. Make submit validate the checkbox

Replace your existing bottom script with this minimal version:

jQuery(document).ready(function () {
    // Checkbox must initially be unchecked
    $('#infoprovide')
        .prop('checked', false)
        .prop('disabled', true);
    $('#appPrivacyConsentFlag').val('N');
    // Called only after Privacy Notice is accepted
    window.privacyConsentAccepted = function () {
        $('#infoprovide')
            .prop('disabled', false)
            .prop('checked', true);
        $('#appPrivacyConsentFlag').val('Y');
    };
    // Prevent submission without consent
    $('#submitBtn').closest('form').on('submit', function (e) {
        if (!$('#infoprovide').is(':checked')) {
            e.preventDefault();
            alert('Please read and accept SBI’s Privacy Notice before submitting.');
            return false;
        }
        return true;
    });
});

6. Prevent unchecking after acceptance

Because the checkbox becomes enabled after acceptance, technically the user could click it and uncheck it.

So add this small protection:

$('#infoprovide').on('click', function (e) {
    if ($(this).is(':checked')) {
        e.preventDefault();
        return false;
    }
});

However, an even cleaner approach for your requirement is to keep the checkbox disabled even after automatically checking it:

window.privacyConsentAccepted = function () {
    $('#infoprovide')
        .prop('checked', true)
        .prop('disabled', true);
    $('#appPrivacyConsentFlag').val('Y');
};

This is what I recommend

Use:

window.privacyConsentAccepted = function () {
    $('#infoprovide')
        .prop('checked', true)
        .prop('disabled', true);
    $('#appPrivacyConsentFlag').val('Y');
};

Then the checkbox is always disabled from user interaction, but JavaScript can automatically check it after successful acceptance.

⸻

Final minimal code for your existing JSP

Your checkbox:

<div class="trms-section">
    <input type="checkbox"
           class="blue-css-checkbox"
           name="infoprovide"
           id="infoprovide"
           value="on"
           disabled="disabled">
    <label for="infoprovide" class="label-content" style="font-size:14px;">
        I hereby authorize State Bank of India and/or its representative
        to contact me with reference to my application.
        For more details please read
        <b>
            <a href="javascript:void(0);"
               onclick="openPopups('consentCveLoan','1');">
                SBI’s Privacy Notice
            </a>
        </b>
        <b class="req">*</b>
    </label>
</div>

Your JavaScript:

<script>
jQuery(document).ready(function () {
    // Initial state
    $('#infoprovide')
        .prop('checked', false)
        .prop('disabled', true);
    $('#appPrivacyConsentFlag').val('N');
    // Called ONLY after Privacy Notice is accepted
    window.privacyConsentAccepted = function () {
        $('#infoprovide')
            .prop('checked', true)
            .prop('disabled', true);
        $('#appPrivacyConsentFlag').val('Y');
    };
    // Submit validation
    $('#submitBtn').closest('form').on('submit', function (e) {
        if (!$('#infoprovide').is(':checked')) {
            e.preventDefault();
            alert('Please read and accept SBI’s Privacy Notice before submitting.');
            return false;
        }
        return true;
    });
});
</script>

Result

Action	Result
Page opens	☑️ Unchecked + cannot click
Click Privacy Notice	Popup opens
Close popup without accepting	❌ Checkbox remains unchecked
Accept Privacy Notice	✅ Checkbox automatically checked
Try clicking checkbox	❌ Cannot uncheck
Submit without acceptance	❌ Submission blocked
Submit after acceptance	✅ Submission proceeds

One important point: your existing openPopups('consentCveLoan','1') must remain exactly where it is. The only integration needed is to call privacyConsentAccepted() after the popup’s Accept operation has actually succeeded. This prevents a user from opening the popup and getting the checkbox checked without actually accepting the Privacy Notice.
