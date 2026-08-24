Yes. In your current code, the problem is that the hidden checkbox:

<input type="checkbox" ... checked="checked" style="display:none">

is already checked when the page loads, so your submit validation sees it as checked even when the Privacy Notice was never opened.

For your exact requirement, make the minimum changes only around the checkbox and submit button.

1. Change the existing checkbox

You currently have:

<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
    <input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" checked="checked" style="display:none">
</s:if>

If this checkbox represents Privacy Notice consent, don’t make it checked="checked" merely because mobile verification is Y.

Change it to:

<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
    <input type="checkbox"
           class="blue-css-checkbox"
           name="infoprovide"
           id="infoprovide"
           value="on"
           style="display:none">
</s:if>

Why?

This is the main reason your form is currently submitting without opening the popup.

You have:

checked="checked"

which means:

$('#infoprovide').is(':checked')

returns true immediately.

⸻

2. Keep your normal checkbox unchecked initially

Your <s:else> currently has:

<input type="checkbox" class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on"
       readonly>

Change it to:

<input type="checkbox"
       class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on"
       readonly>

You can keep readonly because your existing JavaScript prevents the user from manually changing it.

Your existing:

$('input[type="checkbox"][readonly]').on('click keydown', function(e) {
    e.preventDefault();
});

will prevent manual selection.

So the checkbox can only become checked through your existing popup acceptance logic.

⸻

3. Most important change — validate on Submit

Replace your current bottom JavaScript:

jQuery(document).ready(function(){
    
    $('#infoprovide').change(function() {
        if ($(this).is(':checked')) {
        } else {
            $('#infoprovide').prop('disabled', true);
        }
    });
    
    $('input[type="checkbox"][readonly]').on('click keydown', function(e) {
        e.preventDefault();
    });
    
    $('#infoprovide').rules('add', {
        required: true,
        valueNotEquals: 0
    }); 
    jQuery('#infoprovide').addClass('error');
    
});

with this:

jQuery(document).ready(function(){
    // User cannot manually select/unselect the consent checkbox
    $('input[type="checkbox"][readonly]').on('click keydown', function(e) {
        e.preventDefault();
        return false;
    });
    // Submit validation
    $('#submitBtn').on('click', function(e) {
        if (!$('#infoprovide').is(':checked')) {
            e.preventDefault();
            // Highlight checkbox
            $('#infoprovide').addClass('error');
            // Show message
            alert('Please read and accept the SBI Privacy Notice before submitting.');
            return false;
        }
        // Checkbox is checked - allow submit
        return true;
    });
});

This is the minimum change you need.

⸻

4. Better highlighting for the hidden checkbox

Because your checkbox is:

style="display:none"

adding an error class to the checkbox itself won’t visibly highlight anything.

Instead, highlight the Privacy Notice link/label.

Change your label slightly.

Current:

<label for="infoprovide" class="label-content" style="font-size:14px; ">
    
    I hereby authorize State Bank of India and/or its representative to contact me with reference to my application”. For more details please read  
    <b><a href="javascript:void(0);" onclick="javascript:openPopups('consentCveLoan','1');"> SBI’s Privacy Notice </a></b><b class="req">*</b>
</label>

Change to:

<label for="infoprovide"
       id="consentLabel"
       class="label-content"
       style="font-size:14px;">
    I hereby authorize State Bank of India and/or its representative to contact me with reference to my application.
    For more details please read
    <b>
        <a href="javascript:void(0);"
           id="privacyNoticeLink"
           onclick="javascript:openPopups('consentCveLoan','1');">
            SBI’s Privacy Notice
        </a>
    </b>
    <b class="req">*</b>
</label>

Then use:

$('#consentLabel').addClass('error');

instead of:

$('#infoprovide').addClass('error');

⸻

5. Final JavaScript I recommend for your existing page

Use this exact block:

<script>
jQuery(document).ready(function(){
    /*
     * Prevent user from manually checking/unchecking
     * the consent checkbox.
     *
     * Checkbox will be checked only from the
     * Privacy Notice acceptance flow.
     */
    $('input[type="checkbox"][readonly]').on('click keydown', function(e) {
        e.preventDefault();
        return false;
    });
    /*
     * Submit validation
     */
    $('#submitBtn').on('click', function(e) {
        if (!$('#infoprovide').is(':checked')) {
            e.preventDefault();
            // Highlight consent section
            $('#consentLabel').addClass('error');
            // Show validation message
            alert('Please read and accept the SBI Privacy Notice before submitting.');
            return false;
        }
        // Consent checkbox is checked.
        // Allow normal form submission.
        return true;
    });
});
</script>

⸻

6. Your checkbox section should therefore be

<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
    <input type="checkbox"
           class="blue-css-checkbox"
           name="infoprovide"
           id="infoprovide"
           value="on"
           style="display:none">
</s:if>
<s:else>
    <div class="trms-section">
        <input type="checkbox"
               class="blue-css-checkbox"
               name="infoprovide"
               id="infoprovide"
               value="on"
               readonly>
        <label for="infoprovide"
               id="consentLabel"
               class="label-content"
               style="font-size:14px;">
            I hereby authorize State Bank of India and/or its representative
            to contact me with reference to my application.
            For more details please read
            <b>
                <a href="javascript:void(0);"
                   id="privacyNoticeLink"
                   onclick="javascript:openPopups('consentCveLoan','1');">
                    SBI’s Privacy Notice
                </a>
            </b>
            <b class="req">*</b>
        </label>
    </div>
</s:else>

⸻

7. Submit button does not need to change

Keep your existing button:

<div class="qt-btn-div trms-btn flr mrgt-10">
    <input type="submit"
           class="submit-btn"
           value="Submit"
           id="submitBtn">
</div>

The JavaScript attached to #submitBtn will stop the submission.

⸻

Final behavior

Scenario 1 — User directly clicks Submit

Checkbox = unchecked
       ↓
Submit clicked
       ↓
$('#infoprovide').is(':checked') = false
       ↓
e.preventDefault()
       ↓
"Please read and accept the SBI Privacy Notice"
       ↓
FORM NOT SUBMITTED

Scenario 2 — User opens popup but does NOT accept

Click Privacy Notice
       ↓
openPopups(...)
       ↓
User closes popup
       ↓
Checkbox remains unchecked
       ↓
Submit
       ↓
BLOCKED

Scenario 3 — User opens popup and accepts

Your existing popup acceptance code checks:

$('#infoprovide').prop('checked', true);

Then:

Checkbox = checked
       ↓
Submit
       ↓
$('#infoprovide').is(':checked') = true
       ↓
Validation passes
       ↓
FORM SUBMITTED

One critical correction

Do not keep this:

checked="checked"

in this checkbox:

<input type="checkbox"
       name="infoprovide"
       id="infoprovide"
       checked="checked"
       style="display:none">

That defeats the entire validation.

The checkbox must start unchecked, and your existing openPopups() acceptance code should be the thing that changes it to:

$('#infoprovide').prop('checked', true);

Also, because you have two id="submitBtn" elements in the supplied JSP (normal submit and revoke submit), I strongly recommend changing the revoke button ID to something like submitRevokeBtn. Otherwise the selector #submitBtn can behave unpredictably depending on which section is active.
