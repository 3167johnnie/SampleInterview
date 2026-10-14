Yes—the PDF must use the **currently selected dropdown language**, after that language’s notice finishes loading.

In `downloadPDF()`, replace its opening section, up to the AJAX call, with:

```javascript
function downloadPDF() {

    var selectedLocale = $("#privacyLocaleDropdown").val();
    var loadedLocale = $("#consentHomeLoanDiv").data("loadedLocale");
    var downloadBtn = $("#downloadPrivacyPdfBtn");

    if (downloadBtn.prop("disabled")) {
        return;
    }

    if (!selectedLocale || selectedLocale !== loadedLocale) {
        alert("Please wait until the selected language's notice loads.");
        return;
    }

    // Keep your existing $.ajax call below.
```

Inside that existing AJAX call, keep:

```javascript
url : "downloadPrivacyNoticePdf",
type : "POST",
dataType : "json",

data : {
    privacyLocale : selectedLocale
},
```

Also keep the earlier changes that:

- Disable Download while the selected notice loads.
- Set `.data("loadedLocale", locale)` after successful notice loading.
- Disable the dropdown during PDF generation and enable it in `complete`.

Your working `HomeLoanAction.downloadPrivacyNoticePdf()` already retrieves the notice using the submitted `privacyLocale`.

The flow is therefore:

**Select language → load that language’s notice → enable Download → send the selected locale → generate and download that language’s PDF.**

No additional backend change is needed for language selection.
