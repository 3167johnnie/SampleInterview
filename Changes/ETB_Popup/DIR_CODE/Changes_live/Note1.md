Yes. You only need a small JavaScript validation change. Your current code disables the checkbox when unchecked, which is not what you want.

1. Remove/replace this existing block

You currently have:

$('#infoprovide').change(function() {
    if ($(this).is(':checked')) {
    } else {
        $('#infoprovide').prop('disabled', true);
    }
});

Remove it. Do not disable the checkbox.

2. Add this minimal submit validation

Keep your existing $(document).ready() and add:

$('#submitBtn').click(function(e) {
    if (!$('#infoprovide').is(':checked')) {
        e.preventDefault();
        alert('Please accept the consent before submitting.');
        return false;
    }
});

So your final script can simply be:

jQuery(document).ready(function(){
    $('input[type="checkbox"][readonly]').on('click keydown', function(e) {
        e.preventDefault();
    });
    $('#infoprovide').rules('add', {
        required: true,
        valueNotEquals: 0
    });
    jQuery('#infoprovide').addClass('error');
    $('#submitBtn').click(function(e) {
        if (!$('#infoprovide').is(':checked')) {
            e.preventDefault();
            alert('Please accept the consent before submitting.');
            return false;
        }
    });
});

Important: your checkbox has readonly

You currently have:

<input type="checkbox" class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on"
       readonly>

readonly doesn’t work properly for checkboxes. Your existing code:

$('input[type="checkbox"][readonly]').on('click keydown', function(e) {
    e.preventDefault();
});

actually prevents the user from checking it.

If the checkbox is supposed to be unchecked initially and user must click it, change:

readonly

to nothing:

<input type="checkbox" class="blue-css-checkbox"
       name="infoprovide"
       id="infoprovide"
       value="on">

Final behavior

Checkbox	Submit
❌ Unchecked	Blocked + alert
✅ Checked	Allowed
Initially	Unchecked
User clicks checkbox	Can check/uncheck normally

Also, since you have two elements with id="submitBtn" in the JSP (one for normal consent and one for revoke), it would be safer to eventually give them different IDs. But for the minimum change above, the normal submit button will be handled.
