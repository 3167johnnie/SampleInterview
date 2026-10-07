Make **only these two changes** in your JSP. All existing language-loading, scroll validation, and consent logic stays unchanged.

### 1. Replace the Download PDF button

Find:

```jsp
<!-- ✅ Download button -->
<button onclick="downloadPDF()">Download PDF</button>
```

Replace with:

```jsp
<!-- Download PDF from the common backend action -->
<button type="button"
        id="downloadPrivacyPdfBtn"
        class="btn btn-primary"
        onclick="downloadPDF();">
    Download PDF
</button>
```

`type="button"` prevents the download button from submitting the loan form.

### 2. Replace only the existing `downloadPDF()` function

At the bottom of your `<script>`, remove this function:

```javascript
function downloadPDF() {
    var element = document.getElementById('consentHomeLoanDiv');

    var opt = {
        margin:      [10, 10, 10, 10],
        filename:    'SBI_Privacy_Consent.pdf',
        image:       { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2, useCORS: true },
        jsPDF:       { unit: 'mm', format: 'a4', orientation: 'portrait' },
        pagebreak:   { mode: ['avoid-all', 'css', 'legacy'] }
    };

    html2pdf().set(opt).from(element).save();
}
```

Replace it with:

```jsp
function downloadPDF() {

    // 1. Read the currently selected language.
    var selectedLocale = $("#privacyLocaleDropdown").val() || "eng";
    var downloadBtn = $("#downloadPrivacyPdfBtn");

    // 2. Prevent repeated clicks while downloading.
    if (downloadBtn.prop("disabled")) {
        return;
    }

    downloadBtn.prop("disabled", true).text("Downloading...");

    function resetDownloadButton() {
        downloadBtn.prop("disabled", false).text("Download PDF");
    }

    // 3. Call the common Java PDF action.
    var xhr = new XMLHttpRequest();

    xhr.open(
        "POST",
        '<s:url action="downloadPrivacyNoticePdf"/>',
        true
    );

    xhr.responseType = "blob";
    xhr.timeout = 60000;

    xhr.setRequestHeader(
        "Content-Type",
        "application/x-www-form-urlencoded; charset=UTF-8"
    );

    // 4. Download only a successful PDF response.
    xhr.onload = function() {

        try {
            var contentType = xhr.getResponseHeader("Content-Type") || "";

            if (xhr.status !== 200 ||
                    contentType.toLowerCase().indexOf("application/pdf") === -1 ||
                    !xhr.response ||
                    xhr.response.size === 0) {

                alert("Unable to download Privacy Notice PDF. Please try again.");
                return;
            }

            var downloadUrl = window.URL.createObjectURL(xhr.response);
            var link = document.createElement("a");

            link.href = downloadUrl;
            link.download = "SBI_Privacy_Notice.pdf";

            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);

            // Release the temporary browser URL after download starts.
            window.setTimeout(function() {
                window.URL.revokeObjectURL(downloadUrl);
            }, 10000);

        } catch (e) {
            console.error("Privacy PDF download failed:", e);
            alert("Unable to download Privacy Notice PDF. Please try again.");

        } finally {
            resetDownloadButton();
        }
    };

    // 5. Handle connection failures.
    xhr.onerror = function() {
        resetDownloadButton();
        alert("Unable to download Privacy Notice PDF. Please try again.");
    };

    // 6. Handle a request timeout.
    xhr.ontimeout = function() {
        resetDownloadButton();
        alert("Privacy Notice PDF download timed out. Please try again.");
    };

    xhr.onabort = function() {
        resetDownloadButton();
    };

    // 7. Send the selected locale to CommonLoanAction.
    try {
        xhr.send("privacyLocale=" + encodeURIComponent(selectedLocale));

    } catch (e) {
        resetDownloadButton();
        console.error("Unable to start Privacy PDF download:", e);
        alert("Unable to download Privacy Notice PDF. Please try again.");
    }
}
```

Keep this function **inside the JSP’s existing `<script>` block**, because `<s:url>` is processed by JSP.

The button now calls the previously added `downloadPrivacyNoticePdf` backend action, which generates the PDF using the selected language’s notice from the database. No `html2pdf`, `html2canvas`, or `jsPDF` library is needed for this function.
