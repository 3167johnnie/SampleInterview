Short answer: **No, you do NOT need a separate AJAX call** for saving consent DB data **if the consent fields are already inside your existing loan form submission flow**.

That is the cleaner SBI/Mintstreet approach.

You already submit using:

```javascript
getRequest('commonCBSForm',4)
```

or

```javascript
getRequest('homeloanform',2)
```

So simply send consent values in the same request.

---

## Recommended approach (No extra AJAX)

### Flow

```plaintext
Read More
   ↓
Popup Opens
   ↓
Scroll till bottom
   ↓
Accept Click
   ↓
Set hidden fields
   ↓
Existing Submit button click
   ↓
Normal form submit
   ↓
Action receives consent values
   ↓
Save consent log in DB
```

---

## Frontend

### ConsentPopup.jsp

Already doing:

```javascript
$("#privacyConsentAccepted").val("Y");

$("#privacyConsentLocale")
    .val($("#privacyLocaleDropdown").val());
```

---

### HomeFirstPageSession.jsp

Hidden fields:

```jsp
<input type="hidden"
       name="privacyConsentAccepted"
       id="privacyConsentAccepted"
       value="N"/>

<input type="hidden"
       name="privacyConsentLocale"
       id="privacyConsentLocale"
       value="eng"/>
```

These automatically become part of:

```javascript
$('#homeloanform').serialize()
```

because they are inside form.

---

## Existing submit flow

Suppose current code:

```javascript
var paramData =
    getRequest('homeloanform',2);
```

NO CHANGE needed.

Because `getRequest()` already does:

```javascript
$('#'+formId).serialize()
```

Hidden fields get serialized automatically.

---

## Backend Action

Example:

```java
private String privacyConsentAccepted;
private String privacyConsentLocale;
```

Getter/setter.

After submit:

```java
System.out.println(privacyConsentAccepted);

System.out.println(privacyConsentLocale);
```

You will receive:

```plaintext
Y
eng
```

Then save DB record.

---

## When would AJAX be needed?

Only if business says:

> "Save consent immediately on Accept click even before form submit."

Flow becomes:

```plaintext
Accept click
     ↓
AJAX saveConsent()
     ↓
DB insert immediately
```

Example:

```javascript
$(document).on(
   "click",
   "#acceptConsentBtn",
   function(){

      $.ajax({

         url:"saveConsent",

         type:"POST",

         data:{
            locale:
              $("#privacyLocaleDropdown").val()
         }

      });

});
```

But this is **NOT required** for your current requirement:

> "Capture consent and store in DB on Submit button for all loans."

Requirement explicitly says **on Submit button**.

So **reuse existing submit API**.

---

### Final recommendation

✅ **Use existing form submit**
✅ **Use hidden fields**
✅ **No new AJAX call**
✅ **Save in Action/Service during normal submit processing**

This is simpler, safer, and matches your current Struts + JSP architecture.
