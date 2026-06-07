If you are loading languages dynamically from DB and setting them into `${languages}` from Action/Controller, then modify your JSP like this.

### JSP

```jsp
<select id="privacyLocaleDropdown"
        class="privacy-consent-dropdown"
        onchange="loadPrivacyByLocale();">

    <c:forEach var="lang" items="${languages}">
        <option value="${lang.locale}"
            ${lang.locale == 'eng' ? 'selected="selected"' : ''}>
            ${lang.languageName}
        </option>
    </c:forEach>

</select>
```

### Or using Struts tags

```jsp
<s:select
    id="privacyLocaleDropdown"
    name="privacyLocale"
    cssClass="privacy-consent-dropdown"
    list="languages"
    listKey="locale"
    listValue="languageName"
    onchange="loadPrivacyByLocale();"/>
```

---

### On Popup Open

Load English notice automatically.

```javascript
$(document).on("shown.bs.modal", "#consentHomeLoan", function(){

    var defaultLocale = $("#privacyLocaleDropdown").val();

    if(defaultLocale == null || defaultLocale == ""){
        defaultLocale = "eng";
    }

    loadPrivacyByLocale(defaultLocale);
});
```

---

### loadPrivacyByLocale()

```javascript
function loadPrivacyByLocale(locale){

    if(locale == null){
        locale = $("#privacyLocaleDropdown").val();
    }

    $.ajax({
        url : "getPrivacyNoticeByLocale",
        type : "POST",
        data : {
            privacyLocale : locale
        },
        success : function(response){

            var json = typeof response === "string"
                ? JSON.parse(response)
                : response;

            if(json.status == "success"){

                $("#consentHomeLoanDiv").html(json.privacyNotice);

                $("#consentHomeLoanDiv").scrollTop(0);

                resetConsentScrollValidation();

            }else{

                $("#consentHomeLoanDiv").html(
                    "<p>Privacy Notice Not Available</p>"
                );
            }
        },
        error : function(){
            $("#consentHomeLoanDiv").html(
                "<p>Error loading Privacy Notice</p>"
            );
        }
    });
}
```

### Backend

Before opening the JSP, load languages from DB:

```java
List<PrivacyRequestResponse> languages =
        privacyRequestResponseService.getAllLanguages();

request.setAttribute("languages", languages);
```

Example DB records:

| locale | languageName |
| ------ | ------------ |
| eng    | English      |
| hin    | Hindi        |
| mar    | Marathi      |
| tam    | Tamil        |

Result:

* Dropdown populated from DB.
* English selected by default.
* On popup open, English HTML content loads automatically.
* On selecting Hindi/Marathi/Tamil, corresponding HTML content is fetched from DB and rendered inside `consentHomeLoanDiv`.
