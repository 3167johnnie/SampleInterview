Yes. Based on the CVE code you provided, **the correct place to call CCMS Write API is inside `CveLoanAction.java`, immediately after `processCBSOTP(...)` returns `status = success`, and before your existing `return "jsonResponsePage";`.**

Your existing CVE flow already saves the privacy consent values into the CVE application during `processCBSOTP()`: `APP_PRIVACY_CONSENT_FLAG`, `APP_NTB_ID`, and `APP_PRIVACY_LOCALE` are populated from `MasterCBSCall` before the CVE application is saved. 

So you **do not need to modify `CveProcessManagerImpl`** for this requirement.

## 1. Existing flow

Currently your code is:

```java
json = cveProcessManagerPersonalImpl.processCBSOTP(moduleId, stateManagerBean.getState(), 
        (bankLmsUser!=null?bankLmsUser.getLmsUserId():Constants.OTHER_USER_ID), ajaxPostUrl, 
        appOTPVerifyType, inputOtp, userEmail, SessionUtil.getPersonalLoanTypeSequenceId(),
        SessionUtil.getPlTypeCbsCallId());

...

if(json.get("status").toString().equalsIgnoreCase("success")){
    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";                

    }else{
        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

The `processCBSOTP()` method itself sets `"status": "success"` only after OTP authentication succeeds and the application is saved.  

---

# 2. Minimal changes required

There are **3 changes** in `CveLoanAction.java`.

### Change 1 — Add CCMS imports

At the top of `CveLoanAction.java`, after your existing imports:

```java
import com.mintstreet.common.entity.MasterRelationshipWithBank;
```

add the imports for the CCMS response class and, if they are not already available through your project package:

```java
import com.mintstreet.common.entity.ConsentWriteLog;
```

Use the **same package/import used by your Home Loan class** for `ConsentWriteLog`.

Also, your `consentUtil` appears to be part of your existing common CCMS implementation. If it is not inherited from `BaseAction`, add the same `@Autowired` declaration that exists in your Home Loan implementation.

For example:

```java
@Autowired
private ConsentUtil consentUtil;
```

Use your project's actual package for `ConsentUtil`.

**Do not create another CCMS utility. Reuse the existing `consentUtil.callCCMSConsentWriteAPI(...)` implementation used by Home Loan.**

---

# 3. Add the CCMS call exactly after OTP success

This is the **most important change**.

Find this existing block:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){
    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
        return "jsonResponsePage";                

    }else{
        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

### Replace it with:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    /*
     * CCMS WRITE CONSENT
     * Call CCMS only after OTP authentication is successful.
     */
    try {

        if (SessionUtil.getPersonalLoanTypeSequenceId() != null) {

            appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(
                    SessionUtil.getPersonalLoanTypeSequenceId());

            if (appForm == null) {

                logger.info("CveLoanAction.java :: Unable to fetch CVE application for CCMS. AppSeqId : "
                        + SessionUtil.getPersonalLoanTypeSequenceId());

                responseMessage = "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }

            boolean ccmsWriteStatus = writePrivacyConsentToCCMS(appForm);

            if (!ccmsWriteStatus) {

                logger.info("CveLoanAction.java :: CCMS consent write failed for CVE. AppSeqId : "
                        + appForm.getAppSeqId());

                return "jsonResponsePage";
            }

            logger.info("CveLoanAction.java :: CCMS consent write successful for CVE. AppSeqId : "
                    + appForm.getAppSeqId());
        }

    } catch (Exception e) {

        logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";
        return "jsonResponsePage";
    }


    /*
     * EXISTING CVE SUCCESS FLOW
     */
    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";                

    }else{

        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

### Why retrieve `appForm` here?

Because `processCBSOTP()` receives the application sequence ID:

```java
SessionUtil.getPersonalLoanTypeSequenceId()
```

and internally loads/saves the application using that sequence ID. 

Therefore after OTP success:

```java
appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

gives you the final CVE application containing the consent information.

---

# 4. Add the CVE `writePrivacyConsentToCCMS()` method

Now add this method inside `CveLoanAction.java`, preferably near the bottom before:

```java
public ApplicationFormPersonalLoan getAppForm()
```

### Add:

```java
private boolean writePrivacyConsentToCCMS(ApplicationFormPersonalLoan application) {

    try {

        if (application == null) {

            responseMessage = "error|Unable to process privacy consent. Please try again.";
            return false;
        }

        /*
         * Validate privacy consent
         */
        if (!"Y".equalsIgnoreCase(application.getAppPrivacyConsentFlag())) {

            responseMessage = "error|Please read and accept SBI Privacy Notice before proceeding.";
            return false;
        }

        /*
         * Validate NTB ID
         */
        if (!ValidatorUtil.isValid(application.getAppNtbId())) {

            responseMessage = "error|Invalid consent details. Please accept SBI Privacy Notice again.";
            return false;
        }

        /*
         * Validate privacy language
         */
        if (!ValidatorUtil.isValid(application.getAppPrivacyLocale())) {

            responseMessage = "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";
            return false;
        }

        /*
         * Get mobile and email
         */
        String mobile = application.getCbsMobileNumber();
        String email = application.getCveAppEmail();

        if (!ValidatorUtil.isValid(mobile)) {

            responseMessage = "error|Mobile number is required for consent write.";
            return false;
        }

        /*
         * IP Address
         */
        String ipAddresss = this.SbiUtil.getIPAddress();
        String ipAddress = ipAddresss.replace(",", "");

        /*
         * CCMS WRITE API
         *
         * CVE loan type must be passed here.
         */
        ConsentWriteLog consentWrite =
                consentUtil.callCCMSConsentWriteAPI(
                        application.getAppNtbId(),
                        mobile,
                        email,
                        ipAddress,
                        application.getAppPrivacyLocale(),
                        Constants.CVE_ID);

        /*
         * Validate CCMS response
         */
        if (consentWrite == null
                || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
                || !"200".equalsIgnoreCase(consentWrite.getResponseCode())
                || consentWrite.getConsentId() == null
                || consentWrite.getConsentId().trim().isEmpty()
                || !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

            responseMessage = "error|Unable to write consent to CCMS. Please try again.";

            logger.info("CveLoanAction.java :: CCMS consent write failed for AppSeqId : "
                    + application.getAppSeqId());

            return false;
        }

        String ccmsConsentId = consentWrite.getConsentId().trim();

        logger.info("CveLoanAction.java :: CCMS consent write successful. "
                + "consentId: " + ccmsConsentId
                + ", appSeqId: " + application.getAppSeqId()
                + ", loanTypeId: " + Constants.CVE_ID);

        return true;

    } catch (Exception e) {

        logger.info("CveLoanAction.java :: Exception while calling CCMS Write API for CVE", e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";

        return false;
    }
}
```

---

# 5. One important point about your CVE entity

Your CVE application already contains these fields:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

and corresponding getters/setters. 

So these calls are valid:

```java
application.getAppPrivacyConsentFlag()
application.getAppNtbId()
application.getAppPrivacyLocale()
```

Also, your CVE process already sets these values from `MasterCBSCall`:

```java
if ("Y".equalsIgnoreCase(masterCBSCall.getAppPrivacyConsentFlag())) {
    cveApp.setAppPrivacyConsentFlag("Y");
} else {
    cveApp.setAppPrivacyConsentFlag("N");
}

if (ValidatorUtil.isValid(masterCBSCall.getAppNtbId())) {
    cveApp.setAppNtbId(masterCBSCall.getAppNtbId());
}

if (ValidatorUtil.isValid(masterCBSCall.getAppPrivacyLocale())) {
    cveApp.setAppPrivacyLocale(masterCBSCall.getAppPrivacyLocale());
}
```

So **you don't need to pass these values again from the Action**. 

---

# 6. Do NOT put CCMS call in the OTP failure branches

Your existing code has:

```java
} else if(json.get("message").toString().equalsIgnoreCase("OTP authentication failed")) {

    responseMessage = "error|"+json.getString("message");

    ...

    return "jsonResponsePage";
}
```

Leave this exactly as it is.

The CCMS API must **not** be called here.

Your flow should be:

```text
User enters OTP
       |
       v
processCBSOTP()
       |
       v
OTP verification
       |
   +---+---+
   |       |
 FAIL    SUCCESS
   |       |
   v       v
return   Fetch CVE Application
error        |
             v
      Check Privacy Consent = Y
             |
             v
      Check NTB ID
             |
             v
      Check Privacy Locale
             |
             v
      CCMS WRITE API
             |
       +-----+-----+
       |           |
     FAIL        SUCCESS
       |           |
       v           v
   return       Existing
    error       CVE success
```

This is consistent with your current OTP implementation, where `processCBSOTP()` only returns success after OTP verification and application save. 

---

# 7. Very important: State 29 / Revocation

You have this special condition:

```java
if(stateManagerBean.getState()==29){
    responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
    ...
    return "jsonResponsePage";
}
```

I **would not blindly call the normal CCMS Write API for state 29**.

State `29` is explicitly your **consent revocation flow**, and your existing message says:

> `"Your revocation request has been successfully registered."`

Your normal CCMS **write** API is for granting/storing consent. Revocation should normally follow your existing CCMS revoke flow, not write consent again.

Therefore, if your requirement is:

> **CVE normal OTP success → CCMS Write API**

then make the CCMS call only for the normal CVE flow:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    if(stateManagerBean.getState()==29){

        // EXISTING REVOCATION FLOW - DO NOT CALL WRITE API HERE

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";
    }

    // CCMS WRITE ONLY FOR NORMAL CVE CONSENT
    try {

        if (SessionUtil.getPersonalLoanTypeSequenceId() != null) {

            appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(
                    SessionUtil.getPersonalLoanTypeSequenceId());

            if (appForm == null) {
                responseMessage = "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }

            boolean ccmsWriteStatus = writePrivacyConsentToCCMS(appForm);

            if (!ccmsWriteStatus) {
                return "jsonResponsePage";
            }
        }

    } catch (Exception e) {

        logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";
        return "jsonResponsePage";
    }

    responseMessage = "success|"+json.getString("message");

    return "jsonResponsePage";
}
```

**I recommend this version.**

---

# 8. Even smaller change — recommended version

If you want **minimum possible changes**, your existing success block should become only this:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";                

    }else{

        // ================= CCMS WRITE START =================

        try {

            appForm = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(
                    SessionUtil.getPersonalLoanTypeSequenceId());

            if (appForm == null) {

                responseMessage = "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }

            boolean ccmsWriteStatus = writePrivacyConsentToCCMS(appForm);

            if (!ccmsWriteStatus) {
                return "jsonResponsePage";
            }

        } catch (Exception e) {

            logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

            responseMessage = "error|Unable to write consent to CCMS. Please try again.";
            return "jsonResponsePage";
        }

        // ================= CCMS WRITE END ==================

        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

This gives you exactly:

**OTP success → fetch CVE application → CCMS Write → if CCMS succeeds → existing success response.**

Your CVE application is already saved by `processCBSOTP()` before returning success, so this placement is appropriate. 

### One more consideration

Your Home Loan implementation saves the CCMS `consentId` back into the Quote/Application after a successful CCMS response. Your CVE `ApplicationFormPersonalLoan` shown in the supplied code does **not** show an `APP_CCMS_CONSENT_ID` field. So for the **minimal change requested**, I would **not add a new DB field/entity change** unless your CVE table already has such a column.

The CCMS Write API call itself can be made using:

```java
application.getAppNtbId()
application.getCbsMobileNumber()
application.getCveAppEmail()
application.getAppPrivacyLocale()
Constants.CVE_ID
```

because the CVE process already populates the consent flag, NTB ID and locale before saving the application. 
