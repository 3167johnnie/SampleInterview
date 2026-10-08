Changing to $.ajax() is possible, but it will not fix a backend 404 by itself. We need to distinguish whether the request misses the Struts action or your Java method returns “Privacy Notice Not Found.”

The Discover CORS error belongs to a separate analytics request.

1. Replace only downloadPDF() in ConsentPopup.jsp

Keep the existing button and all consent functions unchanged.

Replace your current downloadPDF() with:

function downloadPDF() {
    var selectedLocale = $("#privacyLocaleDropdown").val() || "eng";
    var downloadBtn = $("#downloadPrivacyPdfBtn");
    if (downloadBtn.prop("disabled")) {
        return;
    }
    $.ajax({
        url: '<s:url value="/downloadPrivacyNoticePdf" includeContext="true" includeParams="none"/>',
        type: "POST",
        data: {
            privacyLocale: selectedLocale
        },
        /*
         * Receive PDF bytes through jQuery's text transport.
         * This avoids older jQuery versions accessing responseText
         * on an XMLHttpRequest configured with responseType="blob".
         */
        dataType: "text",
        mimeType: "text/plain; charset=x-user-defined",
        timeout: 60000,
        beforeSend: function() {
            downloadBtn.prop("disabled", true).text("Downloading...");
        },
        success: function(response, textStatus, xhr) {
            var contentType =
                    xhr.getResponseHeader("Content-Type") || "";
            if (contentType.toLowerCase().indexOf("application/pdf") === -1 ||
                    !response ||
                    response.substring(0, 5) !== "%PDF-") {
                console.error(
                    "Expected PDF but received:",
                    xhr.status,
                    contentType
                );
                alert("The server did not return a PDF. Check the server logs.");
                return;
            }
            try {
                // Restore the original binary bytes.
                var bytes = new Uint8Array(response.length);
                for (var i = 0; i < response.length; i++) {
                    bytes[i] = response.charCodeAt(i) & 255;
                }
                var blob = new Blob([bytes], {
                    type: "application/pdf"
                });
                var downloadUrl = window.URL.createObjectURL(blob);
                var link = document.createElement("a");
                link.href = downloadUrl;
                link.download = "SBI_Privacy_Notice.pdf";
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                window.setTimeout(function() {
                    window.URL.revokeObjectURL(downloadUrl);
                }, 10000);
            } catch (e) {
                console.error("Unable to save Privacy Notice PDF:", e);
                alert("Unable to save Privacy Notice PDF. Please try again.");
            }
        },
        error: function(xhr, textStatus, errorThrown) {
            console.error(
                "Privacy PDF request failed:",
                xhr.status,
                textStatus,
                errorThrown
            );
            if (xhr.status === 404) {
                alert(
                    "PDF request returned 404. Check the Network response " +
                    "and server entry log."
                );
            } else if (textStatus === "timeout") {
                alert("PDF download timed out. Please try again.");
            } else {
                alert("Unable to download Privacy Notice PDF. Please try again.");
            }
        },
        complete: function() {
            downloadBtn.prop("disabled", false).text("Download PDF");
        }
    });
}

This uses your existing jQuery AJAX pattern. The binary-text technique preserves PDF bytes without requiring a jQuery Blob transport or any new library. jQuery supports the mimeType option, and MDN documents receiving binary data using x-user-defined. 

Keep this code inside the JSP, where <s:url> is processed.

2. Check the deployed mappings

In the deployed web.xml, inside the mapping with:

<filter-name>struts2</filter-name>

Ensure this entry exists:

<url-pattern>/downloadPrivacyNoticePdf</url-pattern>

In struts-common-loan.xml, inside its existing <package>, ensure:

<action name="downloadPrivacyNoticePdf"
        method="downloadPrivacyNoticePdf"
        class="commonLoanAction">
</action>

Also ensure that the deployed struts.xml includes this configuration file:

<include file="struts-common-loan.xml"/>

Do not add duplicate entries if they already exist. Check the deployed WAR/configuration, because changing local files alone does not update Tomcat.

3. Identify exactly where the 404 comes from

At the beginning of your Java method, add:

public StreamResult downloadPrivacyNoticePdf() {
    logger.info(
        "Entered downloadPrivacyNoticePdf; locale={}",
        privacyLocale
    );
    Document document = null;

In the existing privacyPdfError() method, add this log before setting the status:

private StreamResult privacyPdfError(int status, String message) {
    logger.warn(
        "Privacy PDF failed; status={}, locale={}, reason={}",
        status,
        privacyLocale,
        message
    );
    ServletActionContext.getResponse().setStatus(status);
    // Keep the remaining existing code unchanged.

Redeploy, click Download PDF once, and inspect Network → downloadPrivacyNoticePdf → Response, together with the server logs:

Evidence	Required fix
No entry log; 404 error page	Check deployed filter mapping, action configuration/include, and any proxy routing
Entry log; Privacy Notice Not Found.	Check getPrivacyIdByLocale(selectedLocale) and the corresponding PRIVACY_NOTICE content
Entry log; HTTP 500	Use the logged exception to fix generation/dependency issues
HTTP 200; application/pdf	Backend succeeded; the AJAX function downloads the file

Your Java code explicitly returns 404 for missing notice data, so do not assume every 404 is a URL problem. No actual response body or server log has been supplied yet to distinguish these cases.

4. Resolve the Discover errors separately

Your browser is calling:

https://mardiscpr.dwhmartr.sbi.bank.in/DiscoverUIPost.php

There are two possible intended configurations.

If Discover must run on UAT: its collector or gateway must permit the UAT origin in its response:

Access-Control-Allow-Origin: https://testonlineapply.sbi.co.in

If a preflight occurs, it must also handle OPTIONS and allow the actual requested method/headers. The Discover/network team must separately resolve the connection resets.

CORS permission belongs on the collector response. Adding headers to your PDF action or changing AJAX options cannot supply that permission. 

If Discover should be disabled on UAT: keep the guard previously supplied in the final configuration block:

// Default configuration
(function () {
    "use strict";
    if (window.location.hostname === "testonlineapply.sbi.co.in") {
        return;
    }
    // Existing configuration continues here.

Because requests continue after your change, verify the browser actually receives it:

1. Open Developer Tools → Network → enable Disable cache.
2. Reload the page.
3. Open the loaded Discover_UIC_OCAS.js response.
4. Search for the guard above.
5. Check whether the script loads more than once or another script calls DCX.init().

If the guard is missing, deploy the modified script and update the existing script URL’s version parameter, for example from ?v= to ?v=20261008-2. If the guard is present but requests continue after a fresh reload, another initialization is starting Discover.

Apply the AJAX replacement, then use the two Java logs to resolve the remaining 404. The Discover request requires either a working collector configuration or verified UAT disabling.
