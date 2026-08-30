Yes. If the requirement is specifically:

> **CVE OTP success → call CCMS Write API → save the returned CCMS consent ID in `ApplicationFormCveLoan` / `RUPEEPOWER_OCAS_T_00291`**

then we should **not use `ApplicationFormPersonalLoan` at all** for the CCMS part.

Your `ApplicationFormCveLoan` is mapped to `RUPEEPOWER_OCAS_T_00291`.  It already has the required privacy fields: `APP_PRIVACY_CONSENT_FLAG`, `APP_NTB_ID`, and `APP_PRIVACY_LOCALE`.  

However, your current `ApplicationFormCveLoan` does **not appear to have a CCMS consent-ID field**; the existing `APP_CONSENT_ID` is mapped to `loanQuoteConsentId` and is an `Integer`, whereas the CCMS `consentId` returned by your Home Loan implementation is a `String`. 

So there are two parts.

---

# 1. Change `CveLoanAction.java` imports

Currently you have:

```java
import com.mintstreet.loan.cveloan.bo.impl.CveProcessManagerImpl;
import com.mintstreet.loan.cveloan.entity.MasterCveProduct;
import com.mintstreet.loan.cveloan.service.CveLoanService;
import com.mintstreet.loan.personal.bo.impl.PersonalProcessManagerImpl;
import com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan;
import com.mintstreet.loan.personal.service.PersonalLoanService;
```

Your CVE Action currently has `ApplicationFormPersonalLoan appForm`. 

Add:

```java
import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
```

You can keep the existing Personal Loan imports because the Action is already using them elsewhere.

---

# 2. Add `ApplicationFormCveLoan` variable

Currently:

```java
private ApplicationFormPersonalLoan appForm;
```

**Do not change this blindly**, because your existing Action may use `appForm` for Personal Loan functionality elsewhere.

Instead add a separate CVE variable immediately below it:

```java
private ApplicationFormPersonalLoan appForm;

private ApplicationFormCveLoan cveAppForm;
```

So:

```java
@Autowired
private AESEncryption aesEncryption;

private ApplicationFormPersonalLoan appForm;

private ApplicationFormCveLoan cveAppForm;

@Autowired
private CveLoanService cveLoanService;
```

This keeps the changes minimal and avoids breaking existing Personal Loan code.

---

# 3. Fetch `ApplicationFormCveLoan` after OTP success

Your current code is:

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

Your OTP process already works with `ApplicationFormCveLoan`. In the CVE process, it retrieves the CVE application using `getApplicationFormCveLoanByAppSeqId(...)`. 

Therefore, in the Action, after successful OTP, retrieve the CVE application using the same sequence ID.

## Replace only the `else` part

Change:

```java
}else{
    responseMessage = "success|"+json.getString("message");

    return "jsonResponsePage";
}
```

to:

```java
}else{

    // =========================================================
    // Fetch CVE ApplicationFormCveLoan after successful OTP
    // =========================================================
    try {

        cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
                SessionUtil.getPersonalLoanTypeSequenceId());

        if (cveAppForm == null) {

            logger.info("CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : "
                    + SessionUtil.getPersonalLoanTypeSequenceId());

            responseMessage = "error|Unable to process privacy consent. Please try again.";
            return "jsonResponsePage";
        }

        // =====================================================
        // CCMS WRITE
        // =====================================================
        boolean ccmsWriteStatus = writePrivacyConsentToCCMS(cveAppForm);

        if (!ccmsWriteStatus) {
            return "jsonResponsePage";
        }

        // =====================================================
        // Existing success response
        // =====================================================
        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";

    } catch (Exception e) {

        logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";

        return "jsonResponsePage";
    }
}
```

### Important

Your existing CVE service already exposes the method:

```java
personalLoanService.getApplicationFormCveLoanByAppSeqId(...)
```

and your CVE process itself uses this method. 

So you don't need to add another database retrieval method.

---

# 4. Do NOT call CCMS for State 29

Keep this section exactly as it is:

```java
if(stateManagerBean.getState()==29){

    responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
    logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

    appSeqId = null;
    SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

    return "jsonResponsePage";                
}
```

That is your **revocation flow**.

The CCMS Write API should happen only here:

```java
}else{
    // CCMS WRITE
}
```

So the flow becomes:

```text
OTP
 |
 v
processCBSOTP()
 |
 v
OTP SUCCESS
 |
 +---- State 29 --> Existing revoke flow
 |
 +---- Normal CVE
          |
          v
ApplicationFormCveLoan
          |
          v
Validate privacy consent
          |
          v
CCMS WRITE API
          |
          v
Save CCMS Consent ID
          |
          v
Existing success response
```

---

# 5. Add `writePrivacyConsentToCCMS()` using `ApplicationFormCveLoan`

Now add this method to `CveLoanAction.java`:

```java
private boolean writePrivacyConsentToCCMS(ApplicationFormCveLoan application) {

    try {

        if (application == null) {

            responseMessage = "error|Unable to process privacy consent. Please try again.";
            return false;
        }

        /*
         * 1. Validate privacy consent
         */
        if (!"Y".equalsIgnoreCase(application.getAppPrivacyConsentFlag())) {

            responseMessage = "error|Please read and accept SBI Privacy Notice before proceeding.";
            return false;
        }

        /*
         * 2. Validate NTB ID
         */
        if (!ValidatorUtil.isValid(application.getAppNtbId())) {

            responseMessage = "error|Invalid consent details. Please accept SBI Privacy Notice again.";
            return false;
        }

        /*
         * 3. Validate privacy locale
         */
        if (!ValidatorUtil.isValid(application.getAppPrivacyLocale())) {

            responseMessage = "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";
            return false;
        }

        /*
         * 4. Get CVE mobile and email
         */
        String mobile = application.getCbsMobileNumber();
        String email = application.getCveAppEmail();

        if (!ValidatorUtil.isValid(mobile)) {

            responseMessage = "error|Mobile number is required for consent write.";
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
        if (consentWrite == null
                || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
                || !"200".equalsIgnoreCase(consentWrite.getResponseCode())
                || consentWrite.getConsentId() == null
                || consentWrite.getConsentId().trim().isEmpty()
                || !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

            logger.info("CveLoanAction.java :: CCMS consent write failed. AppSeqId : "
                    + application.getAppSeqId());

            responseMessage = "error|Unable to write consent to CCMS. Please try again.";

            return false;
        }

        String ccmsConsentId = consentWrite.getConsentId().trim();

        /*
         * 8. Save CCMS consent ID in CVE application
         */
        application.setAppCcmsConsentId(ccmsConsentId);

        cveAppForm = personalLoanService.save(application);

        if (cveAppForm == null) {

            logger.info("CveLoanAction.java :: CCMS consent was successful but CVE application update failed. "
                    + "AppSeqId : " + application.getAppSeqId());

            responseMessage = "error|Consent was recorded, but application details could not be updated.";

            return false;
        }

        logger.info("CveLoanAction.java :: CCMS consent successfully written for CVE. "
                + "ConsentId : " + ccmsConsentId
                + ", AppSeqId : " + application.getAppSeqId()
                + ", LoanTypeId : " + Constants.CVE_ID);

        return true;

    } catch (Exception e) {

        logger.info("CveLoanAction.java :: Exception while calling CCMS Write API for CVE", e);

        responseMessage = "error|Unable to write consent to CCMS. Please try again.";

        return false;
    }
}
```

---

# 6. But `setAppCcmsConsentId()` does not currently exist

This is the **one important entity change**.

From the `ApplicationFormCveLoan` entity you provided, I can confirm these fields exist:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;
```

 

But there is no `APP_CCMS_CONSENT_ID` shown in the entity. The only existing consent ID field shown is:

```java
@Column(name="APP_CONSENT_ID")
private Integer loanQuoteConsentId;
```



Therefore **do not use**:

```java
application.setLoanQuoteConsentId(...)
```

because the CCMS consent ID is a String in your existing Home Loan code.

---

# 7. Add CCMS consent ID field to `ApplicationFormCveLoan`

If your DB table `RUPEEPOWER_OCAS_T_00291` does **not already contain** a CCMS consent ID column, add one.

For example:

```java
@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Then add getter:

```java
public String getAppCcmsConsentId() {
    return appCcmsConsentId;
}
```

And setter:

```java
public void setAppCcmsConsentId(String appCcmsConsentId) {
    this.appCcmsConsentId = appCcmsConsentId;
}
```

So the entity section becomes:

```java
@Column(name="APP_PRIVACY_CONSENT_FLAG")
private String appPrivacyConsentFlag;

@Column(name="APP_NTB_ID")
private String appNtbId;

@Column(name="APP_PRIVACY_LOCALE")
private String appPrivacyLocale;

@Column(name="APP_CCMS_CONSENT_ID")
private String appCcmsConsentId;
```

Then:

```java
public String getAppCcmsConsentId() {
    return appCcmsConsentId;
}

public void setAppCcmsConsentId(String appCcmsConsentId) {
    this.appCcmsConsentId = appCcmsConsentId;
}
```

And DB:

```sql
ALTER TABLE RUPEEPOWER_OCAS_T_00291
ADD APP_CCMS_CONSENT_ID VARCHAR2(100);
```

**Only execute this DB change if `APP_CCMS_CONSENT_ID` does not already exist.**

---

# 8. One correction to my previous answer

I previously suggested:

```java
personalLoanService.getApplicationFormPersonalLoanByAppSeqId(...)
```

That is **not what you want for this CVE implementation**.

For your requirement, it must be:

```java
personalLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());
```

The CVE codebase already uses `ApplicationFormCveLoan` and retrieves it through `getApplicationFormCveLoanByAppSeqId(...)`. 

Also, `ApplicationFormCveLoan` contains the correct CVE-specific mobile and email fields:

```java
getCbsMobileNumber()
getCveAppEmail()
```

 

---

# 9. Final minimal-change version

Your actual `CveLoanAction` success block should ultimately look like this:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";                

    }else{

        // =====================================================
        // FETCH CVE APPLICATION
        // =====================================================
        try {

            cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
                    SessionUtil.getPersonalLoanTypeSequenceId());

            if(cveAppForm == null){

                logger.info("CveLoanAction.java :: ApplicationFormCveLoan not found. AppSeqId : "
                        + SessionUtil.getPersonalLoanTypeSequenceId());

                responseMessage = "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }

            // =================================================
            // CALL CCMS WRITE API
            // =================================================
            boolean ccmsWriteStatus = writePrivacyConsentToCCMS(cveAppForm);

            if(!ccmsWriteStatus){
                return "jsonResponsePage";
            }

            // =================================================
            // EXISTING SUCCESS RESPONSE
            // =================================================
            responseMessage = "success|"+json.getString("message");

            return "jsonResponsePage";

        } catch(Exception e){

            logger.info("CveLoanAction.java :: Exception while writing CVE consent to CCMS", e);

            responseMessage = "error|Unable to write consent to CCMS. Please try again.";

            return "jsonResponsePage";
        }
    }
}
```

And the CCMS method receives:

```java
private boolean writePrivacyConsentToCCMS(
        ApplicationFormCveLoan application)
```

not:

```java
ApplicationFormPersonalLoan
```

### One thing I would change from the earlier proposal

Because your CVE `ApplicationFormCveLoan` is already persisted by `processCBSOTP()` before it returns success—the existing process calls `personalLoanService.save(app)` after successful OTP—there is no need to create a new CVE record. 

You simply:

**retrieve existing CVE record → CCMS write → set returned CCMS ID → save same CVE record.**

That is the cleanest and smallest change for your existing architecture.
