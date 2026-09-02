Yes. The new logs make the problem much clearer.

### What is happening

Your latest flow is:

1. OTP authentication succeeds.
2. `ApplicationFormCveLoan` is successfully fetched for `AppSeqId = 14044`.
3. CCMS API starts successfully.
4. CCMS internally calls `READ_PURPOSES` and gets HTTP `200`.
5. Your code then eventually needs to save the returned CCMS consent ID.
6. **Your current code is still using `personalLoanService` to save `ApplicationFormCveLoan`.**

That is the main thing I would change.

Your CVE entity is mapped to `RUPEEPOWER_OCAS_T_00291`, and it already contains the privacy fields `APP_PRIVACY_CONSENT_FLAG`, `APP_NTB_ID`, and `APP_PRIVACY_LOCALE`. 

Also, your latest log proves that fetching the CVE record is now working: `AppSeqId 14044` is returned successfully. 

So **do not change the OTP flow**. Make the CCMS portion use `CveLoanService` consistently.

---

# 1. Change the CVE application fetch

### Current code — REMOVE

Inside your successful OTP block you currently have:

```java
cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

### Change to

```java
cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

This is important because you specifically want CVE data handled through `CveLoanService`.

Your current log shows:

```text
PersonalLoanService.java:501 - INSIDE PersonalLoanService.java BEFORE getApplicationFormCveLoanByAppSeqId
PersonalLoanService.java:503 - INSIDE PersonalLoanService.java AFTER getApplicationFormCveLoanByAppSeqId 14044
```

So although it returns the object, it is going through the wrong service layer for the CVE implementation. 

---

# 2. Change the save operation — this is VERY important

Your current `writePrivacyConsentToCCMS()` contains:

```java
application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = personalLoanService.save(application);
```

### Change ONLY this line

```java
cveAppForm = cveLoanService.save(application);
```

So the complete section becomes:

```java
/*
 * 8. Save CCMS consent ID in CVE application
 */
application.setAppCcmsConsentId(ccmsConsentId);

cveAppForm = cveLoanService.save(application);

if (cveAppForm == null) {
    logger.info("CveLoanAction.java :: CCMS consent was successful but CVE application update failed. "
            + "AppSeqId : " + application.getAppSeqId());

    responseMessage = "error|Consent was recorded, but application details could not be updated.";
    return false;
}
```

### Why this change matters

You specifically want:

```text
ApplicationFormCveLoan
        ↓
CveLoanService
        ↓
CVE DAO
        ↓
RUPEEPOWER_OCAS_T_00291
```

not:

```text
ApplicationFormCveLoan
        ↓
PersonalLoanService
        ↓
Personal Loan DAO / persistence logic
```

Your entity itself is definitely a CVE entity and maps to:

```java
@Entity
@Table(name="RUPEEPOWER_OCAS_T_00291")
public class ApplicationFormCveLoan
```



---

# 3. Your successful OTP block should look like this

I recommend replacing only the current CCMS portion inside:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){
```

with the following.

```java
if(json.get("status").toString().equalsIgnoreCase("success")) {

    /*
     * =========================================================
     * State 29 = CVE Consent Revocation
     * Do NOT call CCMS WRITE for revocation flow
     * =========================================================
     */
    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";

        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: " + json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";
    }

    /*
     * =========================================================
     * State 28 = Successful CVE OTP
     * Call CCMS WRITE API only after OTP success
     * =========================================================
     */
    try {

        Integer consentAppSeqId = SessionUtil.getPersonalLoanTypeSequenceId();

        logger.info("CveLoanAction.java :: Successful OTP. AppSeqId for CCMS : "
                + consentAppSeqId);

        if (consentAppSeqId == null) {

            logger.info("CveLoanAction.java :: AppSeqId is null after successful OTP");

            responseMessage = "error|Unable to process privacy consent. Please try again.";

            return "jsonResponsePage";
        }

        /*
         * =====================================================
         * Fetch CVE application using CveLoanService
         * =====================================================
         */
        cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(
                consentAppSeqId);

        logger.info("CveLoanAction.java :: CVE Application fetched for AppSeqId : "
                + consentAppSeqId);

        if (cveAppForm == null) {

            logger.info("CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : "
                    + consentAppSeqId);

            responseMessage = "error|Unable to process privacy consent. Please try again.";

            return "jsonResponsePage";
        }

        /*
         * =====================================================
         * CCMS WRITE
         * =====================================================
         */
        boolean ccmsWriteStatus = writePrivacyConsentToCCMS(cveAppForm);

        if (!ccmsWriteStatus) {

            logger.info("CveLoanAction.java :: CCMS consent write failed for AppSeqId : "
                    + consentAppSeqId);

            return "jsonResponsePage";
        }

        logger.info("CveLoanAction.java :: CCMS consent write successful for AppSeqId : "
                + consentAppSeqId);

        /*
         * =====================================================
         * Existing successful response
         * =====================================================
         */
        responseMessage = "success|" + json.getString("message");

        return "jsonResponsePage";

    } catch (Exception e) {

        logger.info(
                "CveLoanAction.java :: Exception while writing CVE consent to CCMS",
                e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";

        return "jsonResponsePage";
    }
}
```

This also fixes an important flow issue in your current code: **state 29 should not execute the normal CVE consent-write flow**, because your current code calls CCMS before checking whether the state is 29. Your original code had the `state==29` handling after the CCMS call. The revocation branch should be handled first.

---

# 4. Change `writePrivacyConsentToCCMS()`

Your method is mostly fine.

The important change is the persistence layer.

### Current

```java
application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = personalLoanService.save(application);
```

### Correct

```java
application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = cveLoanService.save(application);
```

Your final method should therefore contain:

```java
private boolean writePrivacyConsentToCCMS(ApplicationFormCveLoan application) {

    try {

        if (application == null) {

            responseMessage =
                    "error|Unable to process privacy consent. Please try again.";

            return false;
        }

        /*
         * 1. Validate privacy consent
         */
        if (!"Y".equalsIgnoreCase(application.getAppPrivacyConsentFlag())) {

            logger.info("CveLoanAction.java :: Privacy consent flag is not Y. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Please read and accept SBI Privacy Notice before proceeding.";

            return false;
        }

        /*
         * 2. Validate NTB ID
         */
        if (!ValidatorUtil.isValid(application.getAppNtbId())) {

            logger.info("CveLoanAction.java :: Invalid NTB ID. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Invalid consent details. Please accept SBI Privacy Notice again.";

            return false;
        }

        /*
         * 3. Validate privacy locale
         */
        if (!ValidatorUtil.isValid(application.getAppPrivacyLocale())) {

            logger.info("CveLoanAction.java :: Invalid privacy locale. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";

            return false;
        }

        /*
         * 4. Get CVE mobile and email
         */
        String mobile = application.getCbsMobileNumber();
        String email = application.getCveAppEmail();

        if (!ValidatorUtil.isValid(mobile)) {

            logger.info("CveLoanAction.java :: Mobile number is empty. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Mobile number is required for consent write.";

            return false;
        }

        /*
         * 5. Get IP Address
         */
        String ipAddresss = this.SbiUtil.getIPAddress();
        String ipAddress = ipAddresss.replace(",", "");

        /*
         * 6. Call CCMS WRITE API
         */
        logger.info("CveLoanAction.java :: Calling CCMS consent WRITE API. "
                + "AppSeqId : " + application.getAppSeqId()
                + ", NTB ID : " + application.getAppNtbId()
                + ", Locale : " + application.getAppPrivacyLocale());

        ConsentWriteLog consentWrite =
                consentUtil.callCCMSConsentWriteAPI(
                        application.getAppNtbId(),
                        mobile,
                        email,
                        ipAddress,
                        application.getAppPrivacyLocale(),
                        Constants.CVE_ID);

        /*
         * 7. Validate CCMS response
         */
        if (consentWrite == null) {

            logger.info("CveLoanAction.java :: CCMS response is NULL. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Unable to write consent to CCMS. Please try again.";

            return false;
        }

        logger.info("CveLoanAction.java :: CCMS response received. "
                + "ResponseStatus : " + consentWrite.getResponseStatus()
                + ", ResponseCode : " + consentWrite.getResponseCode()
                + ", ConsentId : " + consentWrite.getConsentId()
                + ", IsActive : " + consentWrite.getIsActive());

        if (!"true".equalsIgnoreCase(consentWrite.getResponseStatus())
                || !"200".equalsIgnoreCase(consentWrite.getResponseCode())
                || consentWrite.getConsentId() == null
                || consentWrite.getConsentId().trim().isEmpty()
                || !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

            logger.info("CveLoanAction.java :: CCMS consent write failed. "
                    + "AppSeqId : " + application.getAppSeqId());

            responseMessage =
                    "error|Unable to write consent to CCMS. Please try again.";

            return false;
        }

        /*
         * 8. Get CCMS Consent ID
         */
        String ccmsConsentId = consentWrite.getConsentId().trim();

        logger.info("CveLoanAction.java :: CCMS Consent ID received : "
                + ccmsConsentId);

        /*
         * 9. Save CCMS Consent ID into CVE table
         *
         * IMPORTANT:
         * Use CveLoanService, NOT PersonalLoanService.
         */
        application.setAppCcmsConsentId(ccmsConsentId);

        cveAppForm = cveLoanService.save(application);

        if (cveAppForm == null) {

            logger.info(
                    "CveLoanAction.java :: CCMS consent was successful but "
                    + "CVE application update failed. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage =
                    "error|Consent was recorded, but application details could not be updated.";

            return false;
        }

        logger.info(
                "CveLoanAction.java :: CVE application updated successfully. "
                + "CCMS ConsentId : " + ccmsConsentId
                + ", AppSeqId : " + application.getAppSeqId()
                + ", LoanTypeId : " + Constants.CVE_ID);

        /*
         * 10. Send SMS
         */
        String msgBody =
                communicationManagerImpl.setEmailBody(
                        25,
                        0,
                        Constants.MESSAGE_TYPE_SMS,
                        0);

        logger.info("msgBody11::" + msgBody);

        msgBody = SbiUtil.urlEncode(msgBody);

        String SMS_TEXT = null;

        if (Constants.COUNTRY_CODE_INDIA.equals(application.getAppISDCode())) {

            SMS_TEXT = Constants.SMS_STRING_INDIAN;

        } else {

            SMS_TEXT = Constants.SMS_STRING_NRI;
        }

        SMS_TEXT = SMS_TEXT.replaceAll("MESSAGE_TEXT", msgBody);

        SMS_TEXT = SMS_TEXT.replaceAll(
                "MOBILE_CODE",
                application.getAppISDCode() + mobile);

        SMS_TEXT = SMS_TEXT.replaceAll(
                "LOAN_TYPE",
                "CVE Loans ");

        SMS_TEXT = SMS_TEXT.replaceAll(
                "CONSENT_ID",
                application.getAppNtbId());

        logger.info("SMS_TEXT5::" + SMS_TEXT);

        communicationManagerImpl.sendSms(SMS_TEXT);

        return true;

    } catch (Exception e) {

        logger.info(
                "CveLoanAction.java :: Exception while calling CCMS Write API for CVE",
                e);

        responseMessage =
                "error|Unable to write consent to CCMS. Please try again.";

        return false;
    }
}
```

---

# 5. One more important issue: your `savePrivacyConsentCve()` already uses `CveLoanService`

This part of your code is correct:

```java
ApplicationFormCveLoan appForm =
        cveLoanService.getApplicationFormCveLoanByAppSeqId(appSeqId);
```

and:

```java
cveLoanService.save(appForm);
```

So keep it as it is. Your current implementation already does this correctly. 

That means the architecture should consistently be:

```text
CVE JSP
   ↓
CveLoanAction
   ↓
CveLoanService
   ↓
ApplicationFormCveLoan
   ↓
RUPEEPOWER_OCAS_T_00291
```

---

# 6. Very important: your current `ApplicationFormCveLoan` has the privacy fields

The entity contains:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

and corresponding getters/setters. 

So you **do not need to add these fields again**.

However, I notice another field in your Action:

```java
application.setAppCcmsConsentId(ccmsConsentId);
```

You should verify that `ApplicationFormCveLoan` actually has:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

with:

```java
public String getAppCcmsConsentId()
```

and:

```java
public void setAppCcmsConsentId(String appCcmsConsentId)
```

If it does **not** exist in `ApplicationFormCveLoan`, then this is the next thing that needs to be added to the entity and corresponding DB column.

---

# 7. Why your latest logs are actually good

The important sequence from your latest logs is:

```text
OTP authentication successful
```

then:

```text
getApplicationFormCveLoanByAppSeqId
```

returns:

```text
14044
```

then:

```text
callingEISServiceForCcms start...
```

then CCMS returns HTTP:

```text
Connection Response Code : 200
```

and then:

```text
Prepared CCMS consent...
```

So the earlier `SQLGrammarException` while loading `ApplicationFormCveLoan` is **no longer the problem in the latest run**. The latest run successfully fetched the CVE application and entered CCMS.  

The CCMS logs also show the CCMS `READ_PURPOSES` request receiving HTTP 200, so the CCMS integration is progressing further than before. 

---

# 8. Final minimal changes I recommend

You don't need to rewrite the whole `CveLoanAction.java`.

Make these **3 changes only** first:

### Change 1 — Fetch

**Before**

```java
cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

**After**

```java
cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

### Change 2 — Save

**Before**

```java
cveAppForm = personalLoanService.save(application);
```

**After**

```java
cveAppForm = cveLoanService.save(application);
```

### Change 3 — Check state 29 before CCMS

Instead of:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    // CCMS

    if(stateManagerBean.getState()==29){
```

use:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    if(stateManagerBean.getState()==29){

        // existing revocation response
        ...
        return "jsonResponsePage";
    }

    // CCMS WRITE only here
    ...
}
```

This gives you the correct flow:

```text
                    OTP
                     │
                     ▼
             OTP authentication
                     │
                  SUCCESS
                     │
            ┌────────┴────────┐
            │                 │
         state 29          state 28
        Revocation        Normal CVE
            │                 │
            ▼                 ▼
       Existing flow     Get ApplicationFormCveLoan
                              │
                              ▼
                     writePrivacyConsentToCCMS()
                              │
                              ▼
                       CCMS WRITE API
                              │
                              ▼
                     Get CCMS Consent ID
                              │
                              ▼
                 cveLoanService.save()
                              │
                              ▼
                  CVE table 00291
                              │
                              ▼
                         SMS
                              │
                              ▼
                         SUCCESS
```

**Most importantly:** don't use `personalLoanService.save()` anywhere for the `ApplicationFormCveLoan` update. Use `cveLoanService` for both fetching and saving the CVE entity. The CVE entity and its database mapping confirm that this is the correct persistence path. 
