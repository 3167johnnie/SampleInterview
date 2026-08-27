Yes. Based on your existing Home Loan implementation and the CVE `processCBSOTP()` flow you shared, the **minimal-change approach** is:

> **OTP API succeeds → call `writePrivacyConsentToCCMS()` → only continue/return success if CCMS write succeeds.**

The important point is that the CCMS call should be placed **inside**:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){
```

and **before** your existing `stateManagerBean.getState()==29` / normal success response logic.

### 1. CVE `CveAction.java` — add CCMS call here

You currently have:

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

Change it to:

```java
if(json.get("status").toString().equalsIgnoreCase("success")) {

    /*
     * OTP verification successful.
     * Now write Privacy Consent to CCMS.
     */
    logger.info("CveLoanAction.java :: CVE OTP verification successful. Calling CCMS Consent Write API.");

    boolean ccmsWriteStatus = cveProcessManagerPersonalImpl.writePrivacyConsentToCCMS(
            SessionUtil.getPersonalLoanTypeSequenceId(),
            loanTypeId
    );

    logger.info("CveLoanAction.java :: CCMS Consent Write Status :: " + ccmsWriteStatus);

    if (!ccmsWriteStatus) {
        responseMessage = "error|Unable to write consent to CCMS. Please try again.";
        return "jsonResponsePage";
    }

    if(stateManagerBean.getState()==29){

        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "+json);

        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
        return "jsonResponsePage";

    } else {

        responseMessage = "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

However, **do not blindly use this exact signature yet**, because your existing `writePrivacyConsentToCCMS()` method signature needs to be checked.

From the Home Loan code you supplied, the method is:

```java
private boolean writePrivacyConsentToCCMS(
        ApplicationFormHomeLoan application,
        ApplicationFormHomeLoanQuote quote,
        LoanScenarioBean loanScenarioBean,
        Integer loanTypeId)
```

That method **cannot be directly called from `CveAction.java`** because:

1. It is `private`.
2. It belongs to the Home Loan process class.
3. It expects `ApplicationFormHomeLoan` and `ApplicationFormHomeLoanQuote`.
4. CVE has its own application/quote objects.

So for CVE, the correct minimal implementation is to put the **same CCMS API call logic in the CVE process manager/service**, then expose a small public method that the Action calls.

---

# 2. Recommended structure

Your flow should become:

```text
CveAction.java
      |
      | processCBSOTP()
      v
OTP verification
      |
      | status = success
      v
writePrivacyConsentToCCMS()
      |
      | CCMS success
      v
Existing CVE success flow
```

Specifically:

```java
if OTP success
       |
       +---- CCMS failed --> error response
       |
       +---- CCMS success --> existing code
```

This is important because otherwise the customer can get a successful OTP response while CCMS consent was never recorded.

---

# 3. Minimal method to add in CVE Process Manager

Because your existing CVE code already has `cveProcessManagerPersonalImpl`, I would add a method there.

For example:

```java
public boolean writePrivacyConsentToCCMS(Integer appSeqId, Integer loanTypeId) {

    try {

        ApplicationFormCveLoan application =
                cveLoanService.getApplicationFormCveLoanByAppSeqId(appSeqId);

        if (application == null) {
            logger.info("CVE Application not found for appSeqId :: " + appSeqId);
            return false;
        }

        /*
         * Get the CVE quote corresponding to application.
         * Use your existing CVE DAO/service method here.
         */
        ApplicationFormCveLoanQuote quote =
                cveLoanService.getApplicationFormCveLoanQuoteByAppSeqId(appSeqId);

        if (quote == null) {
            logger.info("CVE Quote not found for appSeqId :: " + appSeqId);
            return false;
        }

        return writePrivacyConsentToCCMS(
                application,
                quote,
                loanTypeId
        );

    } catch (Exception e) {

        logger.error(
                "Exception while writing CVE Privacy Consent to CCMS",
                e
        );

        return false;
    }
}
```

**But the exact DAO/service method names above depend on your CVE classes.** I don't want you to create methods that don't exist in your project.

---

# 4. CVE-specific `writePrivacyConsentToCCMS()`

You should then have a CVE version similar to your Home Loan method.

For example:

```java
private boolean writePrivacyConsentToCCMS(
        ApplicationFormCveLoan application,
        ApplicationFormCveLoanQuote quote,
        Integer loanTypeId) {

    try {

        if (application == null || quote == null) {
            logger.info("CVE Application/Quote is null.");
            return false;
        }

        if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
            logger.info("CVE Privacy consent flag is not Y.");
            return false;
        }

        if (loanTypeId == null) {
            logger.info("CVE loanTypeId is null.");
            return false;
        }

        if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
            logger.info("Invalid CVE NTB ID.");
            return false;
        }

        if (!ValidatorUtil.isValid(quote.getQuotePrivacyLocale())) {
            logger.info("Invalid CVE privacy locale.");
            return false;
        }

        String mobile = application.getAppMobileNo();
        String email = application.getAppWorkEmail();

        if (!ValidatorUtil.isValid(mobile)) {
            mobile = quote.getAppMobile();
        }

        if (!ValidatorUtil.isValid(email)) {
            email = quote.getAppEmail();
        }

        if (!ValidatorUtil.isValid(mobile)) {
            logger.info("Mobile number is required for CVE consent write.");
            return false;
        }

        String ipAddresss = this.SbiUtil.getIPAddress();
        String ipAddress = ipAddresss.replace(",", "");

        ConsentWriteLog consentWrite =
                consentUtil.callCCMSConsentWriteAPI(
                        quote.getQuoteNtbId(),
                        mobile,
                        email,
                        ipAddress,
                        quote.getQuotePrivacyLocale(),
                        loanTypeId
                );

        if (consentWrite == null
                || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
                || !"200".equalsIgnoreCase(consentWrite.getResponseCode())
                || consentWrite.getConsentId() == null
                || consentWrite.getConsentId().trim().isEmpty()
                || !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

            logger.info("CVE CCMS Consent Write failed.");
            return false;
        }

        String ccmsConsentId = consentWrite.getConsentId().trim();

        quote.setQuoteCcmsConsentId(ccmsConsentId);
        application.setAppCcmsConsentId(ccmsConsentId);

        quote = cveLoanService.save(quote);
        application = cveLoanService.save(application);

        if (quote == null || application == null) {
            logger.info(
                    "CVE CCMS consent successful but DB update failed."
            );
            return false;
        }

        logger.info(
                "CVE CCMS consent successfully written. consentId: "
                + ccmsConsentId
                + ", appSeqId: "
                + application.getAppSeqId()
                + ", loanTypeId: "
                + loanTypeId
        );

        return true;

    } catch (Exception e) {

        logger.error(
                "Exception while calling CVE CCMS Write API",
                e
        );

        return false;
    }
}
```

### But there is one important thing

Your actual CVE entity getter names may **not** be:

```java
getAppMobileNo()
getAppWorkEmail()
getAppMobile()
getAppEmail()
getQuotePrivacyConsentFlag()
getQuoteNtbId()
getQuotePrivacyLocale()
```

Those are based on your Home Loan implementation.

For CVE, use the actual fields from your CVE `ApplicationFormCveLoan` and CVE quote classes.

---

# 5. The most important change in `CveAction.java`

Once the CVE process manager exposes the CCMS method, your Action change can be extremely small.

Your existing code:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){
```

becomes:

```java
if(json.get("status").toString().equalsIgnoreCase("success")){

    logger.info("CveLoanAction.java :: CVE OTP successful. Calling CCMS Consent Write API.");

    boolean ccmsWriteStatus =
            cveProcessManagerPersonalImpl.writePrivacyConsentToCCMS(
                    SessionUtil.getPersonalLoanTypeSequenceId(),
                    loanTypeId
            );

    logger.info(
            "CveLoanAction.java :: CVE CCMS Consent Write Status :: "
            + ccmsWriteStatus
    );

    if (!ccmsWriteStatus) {

        responseMessage =
                "error|Unable to write consent to CCMS. Please try again.";

        return "jsonResponsePage";
    }

    if(stateManagerBean.getState()==29){

        responseMessage =
                "success|Dear Customer, Your revocation request has been successfully registered.";

        logger.info(
                "CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: "
                + json
        );

        appSeqId = null;

        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);

        return "jsonResponsePage";

    }else{

        responseMessage =
                "success|"+json.getString("message");

        return "jsonResponsePage";
    }
}
```

---

# 6. One correction regarding `loanTypeId`

Based on your earlier CVE implementation, **do not hard-code a Home Loan ID**.

You already have:

```java
loanTypeId
```

available in your Action/process flow.

Pass the CVE loan type that is already being used by your CVE flow:

```java
writePrivacyConsentToCCMS(
    SessionUtil.getPersonalLoanTypeSequenceId(),
    loanTypeId
);
```

That keeps the CCMS implementation reusable.

---

# 7. What happens after this change

### Successful case

```text
Customer enters OTP
        ↓
processCBSOTP()
        ↓
OTP SUCCESS
        ↓
writePrivacyConsentToCCMS()
        ↓
CCMS API
        ↓
responseStatus = true
responseCode = 200
isActive = Y
        ↓
Save consentId in CVE Application/Quote
        ↓
Existing CVE success response
```

### CCMS failure

```text
Customer enters OTP
        ↓
processCBSOTP()
        ↓
OTP SUCCESS
        ↓
writePrivacyConsentToCCMS()
        ↓
CCMS API FAILED
        ↓
return error
```

So the user **will not get the normal CVE success response if CCMS consent writing fails**.

---

## One thing I need from your CVE code to give you the exact line-by-line patch

The only part I don't want to guess is **how to retrieve the CVE application and quote from**:

```java
SessionUtil.getPersonalLoanTypeSequenceId()
```

and the exact CVE getter/setter names.

If you send me the **CVE `CveLoanAction.java` method containing this OTP code + the `cveProcessManagerPersonalImpl` class/methods around `processCBSOTP()`**, I can give you the **exact minimal changes (imports + method + exact insertion point), without changing your existing CVE flow**.
