I checked the **newly attached files**. The JSP still contains the old line:

```javascript
var json = JSON.parse(response);
```

It also does not contain the `loadedLocale` tracking from the previous fix. These changes need to be applied to this version.

Keep PDF generation in **`HomeLoanAction`**. Apply the following edits; line numbers refer to your newly attached files.

### 1. JSP line 27 — pass the selected value

Replace:

```jsp
onchange="loadPrivacyByLocale();"
```

With:

```jsp
onchange="loadPrivacyByLocale(this.value);"
```

Keep the option values and iterator unchanged.

### 2. JSP line 42 — initially disable Download

Replace the existing download button with:

```jsp
<button type="button" id="downloadPrivacyPdfBtn"
    class="btn btn-primary"
    disabled="disabled"
    onclick="downloadPDF();"
    style="margin-right: 10px;">
    Download PDF
</button>
```

It will become enabled when the selected notice finishes loading.

### 3. Immediately after `<script>` — add one variable

```javascript
var privacyNoticeRequestId = 0;
```

Define it only once.

### 4. Replace `loadPrivacyByLocale()` — original lines 85–123

```javascript
function loadPrivacyByLocale(locale) {

    var popup = $("#consentHomeLoan");
    var dropdown = popup.find("[id='privacyLocaleDropdown']");
    var noticeDiv = popup.find("[id='consentHomeLoanDiv']");
    var downloadBtn = popup.find("[id='downloadPrivacyPdfBtn']");

    if (locale == null) {
        locale = dropdown.val();
    }

    var requestId = ++privacyNoticeRequestId;

    noticeDiv.removeData("loadedLocale");
    downloadBtn.prop("disabled", true);

    $("#acceptConsentBtn").prop("disabled", true).css({
        "opacity" : "0.6",
        "cursor" : "not-allowed"
    });

    $("#infoprovide").prop("checked", false);

    if ($("#infoprovideCBS").length > 0) {
        $("#infoprovideCBS").prop("checked", false);
    }

    if (!locale) {
        noticeDiv.text("Please select a privacy notice language.");
        return;
    }

    dropdown.val(locale);
    noticeDiv.text("Loading Privacy Notice...");

    $
        .ajax({
            url : "getPrivacyNoticeByLocale",
            type : "POST",
            dataType : "json",

            data : {
                privacyLocale : locale
            },

            success : function(response) {

                // An older request must not overwrite the latest selection.
                if (requestId !== privacyNoticeRequestId ||
                        dropdown.val() !== locale) {
                    return;
                }

                // Handles either JSON text or an already parsed object.
                var json = typeof response === "string"
                        ? JSON.parse(response)
                        : response;

                if (json &&
                        json.status === "success" &&
                        typeof json.privacyNotice === "string" &&
                        $.trim(json.privacyNotice) !== "") {

                    noticeDiv
                        .html(json.privacyNotice)
                        .data("loadedLocale", locale)
                        .scrollTop(0);

                    resetConsentScrollValidation();
                    downloadBtn.prop("disabled", false);

                } else {
                    noticeDiv.text("Privacy Notice Not Found");
                }
            },

            error : function(xhr) {

                if (requestId !== privacyNoticeRequestId) {
                    return;
                }

                noticeDiv.text("Unable To Load Privacy Notice");

                console.error(
                    "Privacy notice request failed:",
                    xhr.status,
                    "Locale:",
                    locale
                );
            }
        });
}
```

This clears the previous English content when another language is selected. It then displays the returned notice and records which locale successfully loaded.

### 5. JSP lines 142–148 — retain English on popup opening

Replace only the language-selection block inside `shown.bs.modal` with:

```javascript
var dropdown = $("#consentHomeLoan")
        .find("[id='privacyLocaleDropdown']");

if (dropdown.find("option").length === 0) {

    loadPrivacyLanguageDropdown();

} else {

    var defaultLocale =
            dropdown.find("option[value='eng']").length > 0
            ? "eng"
            : dropdown.find("option:first").val();

    dropdown.val(defaultLocale);
    loadPrivacyByLocale(defaultLocale);
}
```

Keep all other lines in that modal handler unchanged.

This defaults to English **when the popup opens**. Changing the dropdown subsequently calls the selected locale through `this.value`.

### 6. JSP lines 237–239 — update language-list initialization

Inside `loadPrivacyLanguageDropdown()`, replace:

```javascript
$("#privacyLocaleDropdown").html(optionHtml);
$("#privacyLocaleDropdown").val("eng");
loadPrivacyByLocale("eng");
```

With:

```javascript
var dropdown = $("#consentHomeLoan")
        .find("[id='privacyLocaleDropdown']");

dropdown.html(optionHtml);

if (dropdown.find("option").length === 0) {
    dropdown.append(
        $("<option>").val("eng").text("English")
    );
}

var defaultLocale =
        dropdown.find("option[value='eng']").length > 0
        ? "eng"
        : dropdown.find("option:first").val();

dropdown.val(defaultLocale);
loadPrivacyByLocale(defaultLocale);
```

Keep the existing fallback and error handling.

Do not call `loadPrivacyLanguageDropdown()` on every selection—it rebuilds the dropdown and resets the default.

### 7. Update the beginning of `downloadPDF()` — original line 257

Replace the opening section **before its existing AJAX call** with:

```javascript
function downloadPDF() {

    var popup = $("#consentHomeLoan");
    var dropdown = popup.find("[id='privacyLocaleDropdown']");
    var noticeDiv = popup.find("[id='consentHomeLoanDiv']");
    var downloadBtn = popup.find("[id='downloadPrivacyPdfBtn']");

    var selectedLocale = dropdown.val();
    var loadedLocale = noticeDiv.data("loadedLocale");

    if (downloadBtn.prop("disabled")) {
        return;
    }

    if (!selectedLocale || selectedLocale !== loadedLocale) {
        alert("Please wait until the selected language's notice loads.");
        return;
    }

    // Keep your existing AJAX call below.
```

Keep this part of the existing AJAX call unchanged:

```javascript
url : "downloadPrivacyNoticePdf",
type : "POST",
dataType : "json",

data : {
    privacyLocale : selectedLocale
},
```

Replace its `beforeSend` with:

```javascript
beforeSend : function() {
    downloadBtn.prop("disabled", true).text("Downloading...");
    dropdown.prop("disabled", true);
},
```

Replace its `complete` with:

```javascript
complete : function() {

    dropdown.prop("disabled", false);

    var loadedLocale = noticeDiv.data("loadedLocale");

    downloadBtn
        .prop("disabled",
              !loadedLocale || loadedLocale !== dropdown.val())
        .text("Download PDF");
}
```

**Keep the existing PDF success callback, Base64 conversion, and error callback unchanged.**

### 8. Backend — verify the locale lookup without replacing the working method

Your attached Java already uses the supplied locale in both endpoints.

At Java **line 3478**, immediately after:

```java
Integer privacyId =
        commonService.getPrivacyIdByLocale(privacyLocale);
```

Add:

```java
logger.info(
        "Notice lookup: locale={}, privacyId={}",
        privacyLocale,
        privacyId);
```

At Java **line 4181**, immediately after:

```java
Integer privacyId =
        commonService.getPrivacyIdByLocale(selectedLocale);
```

Add:

```java
logger.info(
        "PDF lookup: locale={}, privacyId={}",
        selectedLocale,
        privacyId);
```

Keep the current Struts mapping to `homeLoanAction`. No XML change is needed for this fix.

After deployment, test:

| Action | Expected behavior |
|---|---|
| Open popup | English notice loads |
| Select another language | Old content clears; selected notice loads |
| Click Download | PDF request sends that selected locale |
| Change languages quickly | Earlier response cannot overwrite the latest notice |

**If English still appears:** inspect Network → `getPrivacyNoticeByLocale` → Payload and Response. If the payload contains the correct selected code but the response contains English, the service/DAO lookup or stored content must be corrected. The attached files do not include `getPrivacyIdByLocale()`’s implementation, so a JSP change cannot guarantee that lookup returns the correct database record.
