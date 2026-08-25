Yes. For the **exact CVE changes and analysis**, I need a few more files/classes. You don't need to send the entire project.

### Please send these files

**1. CVE Action / Controller — required**

* The CVE `Action` class that handles the final **Submit**.
* Especially the method that receives the CVE application submission.

This is the most important missing piece because I need to identify exactly where to insert the CCMS call.

**2. `ApplicationFormCveLoan.java` — required**
You already sent the DAO and service, but I need the entity to verify whether these fields already exist:

```java
appPrivacyConsentFlag
appNtbId
appPrivacyLocale
```

and their column mappings.

**3. Existing CCMS implementation — required**
Send the actual existing code used by your **Gold/Personal/Home Loan** flow for:

```java
writePrivacyConsentToCCMS(...)
```

and/or:

```java
ConsentUtil
CcmsUtil
PrivacyRequestResponse
```

I need the actual method signature and request/response handling so I don't create a different CCMS implementation unnecessarily.

**4. Gold/Personal/Home Action submit method — highly useful**
If you have the Action method where the existing working CCMS call is made, send that method/class.

That lets me produce the CVE change as a direct adaptation:

```text
Existing Gold/Personal flow
          ↓
identify CCMS call
          ↓
map CVE fields
          ↓
insert same call into CVE
```

### Already received — no need to resend

I have enough from what you've provided for:

* `ConsentPopupCve.jsp`
* `CommonCbsCveNonConsent.jsp` reference
* `CveLoanService`
* `ApplicationFormCveLoanDao`
* `ApplicationFormCaseCveDao`
* `ApplicationFormCaseCve`
* `MasterCveProductDao`
* `MasterCveProduct`

### One thing I already noticed

There are a couple of CVE JSP/JS details I will verify rather than blindly changing, for example:

```javascript
var mobile = $("#mobile").val();
var dob = $("#date_of_birth").val();
```

while elsewhere you're using:

```javascript
$("#cbsMobileNumber")
```

Also:

```javascript
$("#infoprovide")
```

versus:

```javascript
$("#infoprovideCve")
```

These IDs need to match the actual CVE JSP. I will correct them only if the actual CVE page confirms they are inconsistent.

And I noticed this apparent typo:

```javascript
$("#consentCveLoanDiv").html0(
```

which should almost certainly be:

```javascript
$("#consentCveLoanDiv").html(
```

I'll include such fixes in the final change list, separated from the CCMS changes.

### Send next

You can simply paste/upload:

```text
1. ApplicationFormCveLoan.java

2. CVE Action.java / Controller.java

3. Existing Gold/Personal/Home CCMS implementation
   - writePrivacyConsentToCCMS()
   - ConsentUtil/CcmsUtil
   - PrivacyRequestResponse
   - related request/response classes

4. Existing Gold/Personal/Home Action method
   where CCMS is successfully called
```

**No need to send database scripts yet** unless `ApplicationFormCveLoan.java` shows that the consent fields are missing. Once I see those classes, I can tell you exactly whether a DB change is actually required.

Then I'll give you the complete **class → method → exact location → current code → replacement/addition → final code** solution with the minimum CVE-only changes.








#####
Yes — this is the **CVE Action**, and it gives us a much clearer picture of the flow.

I can now trace the CVE flow substantially, but **I still need 3 specific pieces before I give you the final code changes**. I don't want to guess the CCMS API or put the call in the wrong state.

## What I can confirm from this CVE Action

Your CVE flow is not a simple single-submit Action. It is driven by `stateManagerBean.getState()`.

The important CVE processing sequence appears to be:

```text
CVE page
   ↓
CVE Action
   ↓
State validation
   ↓
State 27
   ↓
processCbsCall(...)
   ↓
CBS OTP page
   ↓
State 28 / 29
   ↓
processCBSOTP(...)
   ↓
CVE CASE / revocation processing
```

The most important section for the **new consent requirement** is this one:

```java
if(stateManagerBean.getState()==27){
```

and specifically:

```java
CBSCallResponse cbsCallResponse = cveProcessManagerPersonalImpl.processCbsCall(
        appSeqId,
        requestIndex,
        cbs,
        isDsrPage,
        (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID),
        visitId,
        SessionUtil.getPersonalLoanCbsCallId(),
        11,
        null,
        null,
        Constants.CVE_ACTION);
```

### But I would NOT immediately put CCMS here

Because we first need to establish **where `appPrivacyConsentFlag`, `appNtbId`, and `appPrivacyLocale` actually reach the backend**.

Your Action currently has:

```java
private ApplicationFormPersonalLoan appForm;
```

and **does not have**:

```java
ApplicationFormCveLoan
```

nor:

```java
appPrivacyConsentFlag
appNtbId
appPrivacyLocale
```

as Action properties.

That is important.

---

# What I need from you now

### 1. `ApplicationFormCveLoan.java` — REQUIRED

Please send the complete class.

I specifically need to check whether it already contains:

```java
private String appPrivacyConsentFlag;
private String appNtbId;
private String appPrivacyLocale;
```

and the corresponding:

```java
getAppPrivacyConsentFlag()
setAppPrivacyConsentFlag()

getAppNtbId()
setAppNtbId()

getAppPrivacyLocale()
setAppPrivacyLocale()
```

If they don't exist, we'll determine the **minimum DB/entity change**.

---

### 2. `CveProcessManagerImpl.java` — VERY IMPORTANT

Please send the complete relevant methods, particularly:

```java
processCbsCall(...)
```

and:

```java
processCBSOTP(...)
```

This is actually more important now than sending another DAO.

I need to trace:

```text
CveLoanAction
      ↓
CveProcessManagerImpl
      ↓
processCbsCall()
      ↓
ApplicationFormCveLoan
      ↓
save/update
```

because **that is probably where the CVE application data is persisted**.

---

### 3. Existing working CCMS code — REQUIRED

Please send the existing implementation from your Gold/Personal/Home flow:

```java
writePrivacyConsentToCCMS(...)
```

and whatever classes it calls, such as:

```java
ConsentUtil
CcmsUtil
PrivacyRequestResponse
```

I want to reuse your existing implementation rather than create another CCMS client.

---

# One important discovery in your CVE Action

Your popup currently sets:

```javascript
$("#appPrivacyConsentFlag").val("Y");
$("#appNtbId").val(ntbId);
$("#appPrivacyLocale").val(selectedLocale);
```

But in the Action you sent, I don't see corresponding CVE Action properties.

Therefore, one of two things is happening:

### Possibility A — fields are already part of `OtherRequest`

For example:

```java
OtherRequest otherRequest = stateManagerBean.getOtherRequest();
```

and the consent fields are being carried there.

If so, we can probably make a **very small change**.

### Possibility B — fields are part of `ApplicationFormCveLoan`

Then the processing manager needs to populate/save them.

### Possibility C — fields are currently only HTML hidden fields

Then they are **not reaching the backend at all**, and we need to connect them to the CVE request.

I don't want to assume which one.

---

# I also noticed an important distinction

Your CVE Action is currently using:

```java
ApplicationFormPersonalLoan appForm;
```

and:

```java
personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
```

even though this is the CVE flow.

That appears intentional because CVE is being routed through the Personal Loan infrastructure.

So I **do not recommend replacing this with `ApplicationFormCveLoan` yet**.

Your existing architecture appears to be:

```text
CVE
 ↓
CveLoanAction
 ↓
Personal Loan infrastructure
 ↓
CveProcessManagerImpl
 ↓
CVE-specific processing
```

We should preserve that.

---

# Where I currently expect the final change

After seeing the manager code, I expect the final implementation to look approximately like:

```text
CVE Privacy Popup
        ↓
appPrivacyConsentFlag = Y
appNtbId = mobile + DOB + loanTypeId
appPrivacyLocale = selected locale
        ↓
CVE Submit
        ↓
backend receives consent information
        ↓
SERVER validates:
    flag == Y
    ntbId not blank
    locale not blank
        ↓
existing CVE processing
        ↓
CCMS consent API
        ↓
CCMS SUCCESS
        ↓
continue CVE flow
```

And if:

```java
appPrivacyConsentFlag != "Y"
```

then:

```text
STOP
↓
Do NOT call CBS
↓
Do NOT continue CVE processing
↓
return privacy-notice validation error
```

That server-side validation is important because JavaScript checkbox validation alone is not sufficient.

---

## One more thing I will fix

From your previous JSP, I already see these inconsistencies:

```javascript
var mobile = $("#mobile").val();
var dob = $("#date_of_birth").val();
```

while your validation uses:

```javascript
$("#cbsMobileNumber")
```

and your popup uses both:

```javascript
$("#infoprovide")
```

and:

```javascript
$("#infoprovideCve")
```

I will **not blindly change these yet**. Once you send the actual CVE first-page JSP containing those fields, I can tell you exactly which IDs are correct.

There is also this definite JavaScript typo:

```javascript
$("#consentCveLoanDiv").html0(
```

which should be:

```javascript
$("#consentCveLoanDiv").html(
```

That can be fixed independently.

---

### So, please send these next:

**Priority 1:**

```text
ApplicationFormCveLoan.java
```

**Priority 2:**

```text
CveProcessManagerImpl.java
```

especially:

```text
processCbsCall()
processCBSOTP()
```

**Priority 3:**

```text
Existing Gold/Personal/Home CCMS implementation
```

especially:

```text
writePrivacyConsentToCCMS()
ConsentUtil / CcmsUtil
PrivacyRequestResponse
```

**If available, also send the CVE first-page JSP** containing `infoprovideCve`, `appPrivacyConsentFlag`, `appNtbId`, and `appPrivacyLocale`.

After those, I should have enough to give you the **complete minimal-change CVE implementation**, including exactly **which existing files need changes and which files do not need changes**.
