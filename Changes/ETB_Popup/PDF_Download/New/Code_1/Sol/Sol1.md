Keep PDF generation in **`HomeLoanAction`**, since it is working there. Your current PDF AJAX function already sends `privacyLocale` correctly.

In the attached JSP, two issues affect language loading:

* **Line 107** always calls `JSON.parse(response)`, which fails if jQuery already parsed the response.
* When the dropdown is empty, the popup tries loading an undefined first locale instead of fetching the language list.

Apply these JSP changes. Line numbers refer to your original attached JSP.

### 1. Disable Download until the notice loads

Around **line 42**, add `disabled="disabled"` to the existing download button:

```jsp
<button type="button" id="downloadPrivacyPdfBtn"
    class="btn btn-primary"
    disabled="disabled"
    onclick="downloadPDF();"
    style="margin-right: 10px;">
    Download PDF
</button>
```

### 2. Add one variable after `<script>`

Before `canOpenPrivacyPopup()`, add:

```javascript
// Prevent an older language response replacing a newer selection.
var privacyNoticeRequestId = 0;
```

### 3. Replace `loadPrivacyByLocale()` — original lines 85–123

Replace only this function with:

```javascript
function loadPrivacyByLocale(locale) {

    if (locale == null) {
        locale = $("#privacyLocaleDropdown").val();
    }

    if (locale == null || $.trim(locale) === "") {
        $("#consentHomeLoanDiv")
            .removeData("loadedLocale")
            .text("Please select a privacy notice language.");

        $("#downloadPrivacyPdfBtn").prop("disabled", true);
        return;
    }

    var requestId = ++privacyNoticeRequestId;

    $("#acceptConsentBtn").prop("disabled", true).css({
        "opacity" : "0.6",
        "cursor" : "not-allowed"
    });

    $("#downloadPrivacyPdfBtn").prop("disabled", true);

    $("#consentHomeLoanDiv")
        .removeData("loadedLocale")
        .text("Loading Privacy Notice...");

    $("#infoprovide").prop("checked", false);

    if ($("#infoprovideCBS").length > 0) {
        $("#infoprovideCBS").prop("checked", false);
    }

    $
        .ajax({
            url : "getPrivacyNoticeByLocale",
            type : "POST",
            dataType : "json",

            data : {
                privacyLocale : locale
            },

            success : function(response) {

                // Ignore a response from an earlier selection.
                if (requestId !== privacyNoticeRequestId) {
                    return;
                }

                var json = typeof response === "string"
                        ? JSON.parse(response)
                        : response;

                if (json &&
                        json.status === "success" &&
                        typeof json.privacyNotice === "string" &&
                        $.trim(json.privacyNotice) !== "") {

                    $("#consentHomeLoanDiv")
                        .html(json.privacyNotice)
                        .data("loadedLocale", locale)
                        .scrollTop(0);

                    resetConsentScrollValidation();

                    $("#downloadPrivacyPdfBtn").prop("disabled", false);

                } else {
                    $("#consentHomeLoanDiv")
                        .removeData("loadedLocale")
                        .text("Privacy Notice Not Found");
                }
            },

            error : function(xhr) {

                if (requestId !== privacyNoticeRequestId) {
                    return;
                }

                $("#consentHomeLoanDiv")
                    .removeData("loadedLocale")
                    .text("Unable To Load Privacy Notice");

                console.error(
                    "getPrivacyNoticeByLocale failed:",
                    xhr.status
                );
            }
        });
}
```

This restores dynamic loading and enables Download only after the selected language’s notice successfully appears.

With `dataType : "json"`, jQuery parses the response before passing it to `success`. ([api.jquery.com][1])

### 4. Update English-default loading — original lines 142–148

Inside your existing `shown.bs.modal` handler, find:

```javascript
if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
    $("#privacyLocaleDropdown").val("eng");
    loadPrivacyByLocale("eng");
} else {
    var firstLocale = $("#privacyLocaleDropdown option:first").val();
    loadPrivacyByLocale(firstLocale);
}
```

Replace with:

```javascript
if ($("#privacyLocaleDropdown option").length === 0) {

    // Fetch languages when they were not rendered by the JSP.
    loadPrivacyLanguageDropdown();

} else {

    var defaultLocale =
            $("#privacyLocaleDropdown option[value='eng']").length > 0
            ? "eng"
            : $("#privacyLocaleDropdown option:first").val();

    $("#privacyLocaleDropdown").val(defaultLocale);
    loadPrivacyByLocale(defaultLocale);
}
```

Keep the rest of this modal handler unchanged.

### 5. Adjust `loadPrivacyLanguageDropdown()` — original lines 237–239

Find:

```javascript
$("#privacyLocaleDropdown").html(optionHtml);
$("#privacyLocaleDropdown").val("eng");
loadPrivacyByLocale("eng");
```

Replace with:

```javascript
$("#privacyLocaleDropdown").html(optionHtml);

if ($("#privacyLocaleDropdown option").length === 0) {
    $("#privacyLocaleDropdown").append(
        $("<option>").val("eng").text("English")
    );
}

var defaultLocale =
        $("#privacyLocaleDropdown option[value='eng']").length > 0
        ? "eng"
        : $("#privacyLocaleDropdown option:first").val();

$("#privacyLocaleDropdown").val(defaultLocale);
loadPrivacyByLocale(defaultLocale);
```

Keep its existing `else` and error handling.

### 6. Update `downloadPDF()` to use the displayed notice’s locale

At original **line 259**, replace:

```javascript
var selectedLocale = $("#privacyLocaleDropdown").val();
```

With:

```javascript
var selectedLocale = $("#consentHomeLoanDiv").data("loadedLocale");
```

Keep:

```javascript
var downloadBtn = $("#downloadPrivacyPdfBtn");
```

After the existing disabled-button guard:

```javascript
if (downloadBtn.prop("disabled")) {
    return;
}
```

Add:

```javascript
if (!selectedLocale ||
        selectedLocale !== $("#privacyLocaleDropdown").val()) {

    alert("Please wait until the selected language's notice loads.");
    return;
}
```

Keep the existing request unchanged:

```javascript
url : "downloadPrivacyNoticePdf",
type : "POST",
dataType : "json",

data : {
    privacyLocale : selectedLocale
},
```

The PDF request now uses the locale of the **successfully displayed notice**.

### 7. Lock language selection while downloading

Inside the existing PDF AJAX `beforeSend`, add the dropdown line:

```javascript
beforeSend : function() {
    downloadBtn.prop("disabled", true).text("Downloading...");

    $("#privacyLocaleDropdown").prop("disabled", true);
},
```

Replace its existing `complete` callback with:

```javascript
complete : function() {

    $("#privacyLocaleDropdown").prop("disabled", false);

    var loadedLocale =
            $("#consentHomeLoanDiv").data("loadedLocale");

    var currentLocale =
            $("#privacyLocaleDropdown").val();

    downloadBtn
        .prop("disabled",
              !loadedLocale || loadedLocale !== currentLocale)
        .text("Download PDF");
}
```

Keep the PDF success/error callbacks unchanged.

Your shared Struts mapping can remain:

```xml
<action name="downloadPrivacyNoticePdf"
        method="downloadPrivacyNoticePdf"
        class="homeLoanAction">
</action>
```

No backend move is needed.

After these changes, opening the popup loads English when available; selecting another language loads its notice; Download sends that displayed language to the working PDF action. Test a rapid language switch as well—the older response should no longer overwrite the latest selection.

This guarantees matching **locales**. The backend still fetches the current database notice, so exact content matching assumes the notice is not edited between display and download. Also verify the downloaded PDF renders each language correctly; this JSP fix does not change font support.

[1]: https://api.jquery.com/jQuery.ajax/?utm_source=chatgpt.com "jQuery.ajax()"
