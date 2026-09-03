Yes. The previous DAO/NPE issue is resolved. Your new logs show that the CVE application is now reaching the CCMS layer successfully:

Prepared CCMS consent...
Saving CCMS Consent Write request log...

The new failure is inside ConsentService.generateConsentWriteRequest(), specifically while saving the CCMS request log.

The most important line is:

java.lang.RuntimeException: Unable to save CCMS Consent Write request log.
    at com.mintstreet.consent.service.ConsentService.generateConsentWriteRequest(ConsentService.java:470)

So do not change the CVE DAO or CveLoanService again for this issue.

⸻

1. What your current log proves

Your flow is now:

CVE OTP successful
       ↓
ApplicationFormCveLoan fetched successfully
       ↓
writePrivacyConsentToCCMS()
       ↓
ConsentUtil.callCCMSConsentWriteAPI()
       ↓
ConsentService.generateConsentWriteRequest()
       ↓
Prepare CCMS consent SUCCESS
       ↓
Save CCMS Consent Write request log
       ↓
FAILURE

This means the earlier problem:

CveLoanService.getApplicationFormCveLoanByAppSeqId()
NullPointerException

has been fixed.

The new problem is here:

ConsentService.java:470

⸻

2. There is an important clue in your log

This line is suspicious:

Saving CCMS Consent Write request log. correlationId: 01a0666d-812d-773a-bf11-8f0f67a64ee2,
ntbId: 3,
correlationId: 24,
loanTypeId: {}

Notice that the log says:

ntbId: 3
correlationId: 24
loanTypeId: {}

There appears to be a logging/parameter mismatch.

You are calling:

consentUtil.callCCMSConsentWriteAPI(
        application.getAppNtbId(),
        mobile,
        email,
        ipAddress,
        application.getAppPrivacyLocale(),
        Constants.CVE_ID);

That is logically correct if the method signature is:

callCCMSConsentWriteAPI(
    String ntbId,
    String mobile,
    String email,
    String ipAddress,
    String locale,
    Integer loanTypeId)

But we need to inspect the actual implementation of:

ConsentUtil.callCCMSConsentWriteAPI()

and then:

ConsentService.generateConsentWriteRequest()

because the exception is occurring there.

⸻

3. Do NOT change writePrivacyConsentToCCMS() yet

Your current code:

ConsentWriteLog consentWrite = consentUtil.callCCMSConsentWriteAPI(
        application.getAppNtbId(),
        mobile,
        email,
        ipAddress,
        application.getAppPrivacyLocale(),
        Constants.CVE_ID);

should not be changed blindly.

The next thing to verify is the actual method signature.

For example, if ConsentUtil has:

public ConsentWriteLog callCCMSConsentWriteAPI(
        String ntbId,
        String mobile,
        String email,
        String ipAddress,
        String privacyLocale,
        Integer loanTypeId)

then your call is correct.

If instead it has something like:

callCCMSConsentWriteAPI(
        String ntbId,
        String mobile,
        String email,
        String ipAddress,
        String privacyLocale,
        String loanTypeId)

or a different parameter order, then we need to correct the call.

⸻

4. Most important: inspect ConsentService.java around line 464–470

Your stack trace tells us exactly where to look:

ConsentService.java:464
Saving CCMS Consent Write request log

and:

ConsentService.java:470
Unable to save CCMS Consent Write request log

I expect code similar to:

logger.info("Saving CCMS Consent Write request log...");
try {
    consentWriteLogDao.save(...);
} catch (Exception e) {
    throw new RuntimeException("Unable to save CCMS Consent Write request log.");
}

The problem is that your code is apparently hiding the real database exception.

For example, if the existing code is:

catch (Exception e) {
    throw new RuntimeException("Unable to save CCMS Consent Write request log.");
}

change it to:

catch (Exception e) {
    logger.error("Exception while saving CCMS Consent Write request log", e);
    throw new RuntimeException(
            "Unable to save CCMS Consent Write request log.", e);
}

This is extremely important.

It will expose the actual reason, such as:

ORA-00942 table or view does not exist

or:

ORA-01400 cannot insert NULL

or:

ORA-12899 value too large for column

or:

SQLGrammarException

or:

ConstraintViolationException

⸻

5. Make this change first

In:

ConsentService.java

go to approximately line 464–470.

Find the catch block around saving the request log.

Current likely code

Something similar to:

catch (Exception e) {
    throw new RuntimeException("Unable to save CCMS Consent Write request log.");
}

Change to

catch (Exception e) {
    logger.error(
            "Exception while saving CCMS Consent Write request log. "
            + "ntbId=" + ntbId
            + ", loanTypeId=" + loanTypeId,
            e);
    throw new RuntimeException(
            "Unable to save CCMS Consent Write request log.", e);
}

If ntbId or loanTypeId are not local variables in that method, do not add variables that don’t exist. In that case simply use:

catch (Exception e) {
    logger.error(
            "Exception while saving CCMS Consent Write request log",
            e);
    throw new RuntimeException(
            "Unable to save CCMS Consent Write request log.", e);
}

Why?

Currently you only see:

Unable to save CCMS Consent Write request log.

That is the application’s generic exception.

We need the original Oracle/Hibernate exception underneath it.

⸻

6. There is another important issue in your current CVE code

Your log says:

ntbId: 3

But your CVE NTB generation method is:

private String generateCveLoanNtbId(ApplicationFormCveLoan appForm) {
    String mobile = appForm.getCbsMobileNumber() != null
            ? appForm.getCbsMobileNumber()
            : "";
    String dob = "";
    try {
        if (appForm.getCbsAccountNumber() != null) {
            dob = new SimpleDateFormat("ddMMyyyy")
                    .format(appForm.getCbsAccountNumber());
        }
    } catch (Exception e) {
        logger.info("Exception while formatting DOB for NTB ID", e);
    }
    String loanType = String.valueOf(Constants.PL_TYPE_CVE);
    logger.info("CveLoanAction.java ::  NTB ID ----:  ",
            mobile + dob + loanType);
    return mobile + dob + loanType;
}

This code is wrong.

You are doing:

new SimpleDateFormat("ddMMyyyy")
        .format(appForm.getCbsAccountNumber());

But:

getCbsAccountNumber()

is a String.

It is not the customer’s DOB.

So this must be corrected.

⸻

7. Why this matters

Your earlier requirement was:

NTB ID = mobile + DOB + loanTypeId

For example:

919845458547
+
01021996
+
<CVETYPE>

But your current code attempts to obtain DOB from:

getCbsAccountNumber()

That is clearly an account number, not DOB.

Therefore the generated NTB ID can be incorrect.

⸻

8. Find the actual DOB field in ApplicationFormCveLoan

Do not guess the getter.

Search your ApplicationFormCveLoan entity for fields/getters containing things such as:

dob
dateOfBirth
birth
DOB
appDob
cbsDob
appDateOfBirth

For example, if the entity actually contains:

private Date appDateOfBirth;

with:

public Date getAppDateOfBirth()

then the correct code becomes:

private String generateCveLoanNtbId(ApplicationFormCveLoan appForm) {
    String mobile = appForm.getCbsMobileNumber() != null
            ? appForm.getCbsMobileNumber()
            : "";
    String dob = "";
    if (appForm.getAppDateOfBirth() != null) {
        dob = new SimpleDateFormat("ddMMyyyy")
                .format(appForm.getAppDateOfBirth());
    }
    String loanType = String.valueOf(Constants.PL_TYPE_CVE);
    String ntbId = mobile + dob + loanType;
    logger.info("CveLoanAction.java :: CVE NTB ID = " + ntbId);
    return ntbId;
}

But use the actual DOB getter from your entity. Don’t copy getAppDateOfBirth() unless that is really your field.

⸻

9. Another issue: your logger statement is incorrect

You currently have:

logger.info("CveLoanAction.java ::  NTB ID ----:  ", mobile + dob + loanType);

Log4j2 will not necessarily print the value the way you’re expecting because there is no {} placeholder.

Change it to:

logger.info("CveLoanAction.java :: CVE NTB ID = " + mobile + dob + loanType);

or:

logger.info("CveLoanAction.java :: CVE NTB ID = {}", mobile + dob + loanType);

The second is preferable.

⸻

10. Your state == 29 flow has a business-flow problem

You currently have:

if(json.get("status").toString().equalsIgnoreCase("success")){

and immediately inside it:

boolean ccmsWriteStatus = writePrivacyConsentToCCMS(cveAppForm);

Then later:

if(stateManagerBean.getState()==29){
    responseMessage =
        "success|Dear Customer, Your revocation request has been successfully registered.";

This means state 29 also calls CCMS WRITE.

That is not correct based on the flow you described earlier.

State 29 is revocation.

You should not execute:

writePrivacyConsentToCCMS(cveAppForm);

for state 29.

⸻

11. Correct structure for state 28 and 29

Your current block should be reorganized.

Instead of:

if(json.get("status").toString().equalsIgnoreCase("success")){
    // Fetch application
    // CCMS WRITE
    if(stateManagerBean.getState()==29){
        ...
    }else{
        ...
    }
}

use:

if (json.get("status").toString().equalsIgnoreCase("success")) {
    /*
     * STATE 29 = CONSENT REVOCATION
     * Do NOT call CCMS WRITE here.
     */
    if (stateManagerBean.getState() == 29) {
        responseMessage =
                "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info(
                "CveLoanAction.java :: CVE consent revocation successful. "
                + "AppSeqId : "
                + SessionUtil.getCveLoanApplicationSequenceId());
        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(null);
        return "jsonResponsePage";
    }
    /*
     * STATE 28 = OTP verification / consent WRITE
     */
    if (stateManagerBean.getState() == 28) {
        try {
            Integer cveAppSeqId =
                    SessionUtil.getCveLoanApplicationSequenceId();
            logger.info(
                    "CveLoanAction.java :: CVE Application Sequence ID = "
                    + cveAppSeqId);
            if (cveAppSeqId == null) {
                logger.info(
                        "CveLoanAction.java :: CVE Application Sequence ID is NULL");
                responseMessage =
                        "error|CVE application details not found. Please try again.";
                return "jsonResponsePage";
            }
            cveAppForm =
                    cveLoanService.getApplicationFormCveLoanByAppSeqId(
                            cveAppSeqId);
            if (cveAppForm == null) {
                logger.info(
                        "CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : "
                        + cveAppSeqId);
                responseMessage =
                        "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }
            /*
             * CCMS WRITE ONLY FOR STATE 28
             */
            boolean ccmsWriteStatus =
                    writePrivacyConsentToCCMS(cveAppForm);
            if (!ccmsWriteStatus) {
                logger.info(
                        "CveLoanAction.java :: CVE CCMS consent WRITE failed. "
                        + "AppSeqId : " + cveAppSeqId);
                return "jsonResponsePage";
            }
            responseMessage =
                    "success|" + json.getString("message");
            logger.info(
                    "CveLoanAction.java :: CVE OTP verification and "
                    + "CCMS consent WRITE successful. AppSeqId : "
                    + cveAppSeqId);
            return "jsonResponsePage";
        } catch (Exception e) {
            logger.error(
                    "CveLoanAction.java :: Exception while writing CVE consent to CCMS",
                    e);
            responseMessage =
                    "error|Unable to write consent to CCMS. Please try again.";
            return "jsonResponsePage";
        }
    }
}

This is cleaner and prevents revocation from accidentally executing the WRITE API.

⸻

12. Your current writePrivacyConsentToCCMS() flow itself is mostly correct

This part:

ConsentWriteLog consentWrite =
        consentUtil.callCCMSConsentWriteAPI(
                application.getAppNtbId(),
                mobile,
                email,
                ipAddress,
                application.getAppPrivacyLocale(),
                Constants.CVE_ID);

is the correct place to make the CVE-specific CCMS call provided the method signature matches.

Then:

if (consentWrite == null
        || !"true".equalsIgnoreCase(consentWrite.getResponseStatus())
        || !"200".equalsIgnoreCase(consentWrite.getResponseCode())
        || consentWrite.getConsentId() == null
        || consentWrite.getConsentId().trim().isEmpty()
        || !"Y".equalsIgnoreCase(consentWrite.getIsActive())) {

is also reasonable.

Then:

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = cveLoanService.save(application);

is correct for your CVE requirement.

Do not change this to personalLoanService.save().

⸻

13. But there is a transaction-order consideration

Your current sequence is:

CCMS WRITE
   ↓
save CCMS consent ID into CVE table
   ↓
send SMS

That’s okay.

However, the current failure happens before the actual CCMS API call, because:

ConsentService.generateConsentWriteRequest()

is failing while saving the request log.

So the actual CCMS request is not being sent yet.

The current flow is:

CveLoanAction
     ↓
ConsentUtil
     ↓
ConsentService
     ↓
generateConsentWriteRequest()
     ↓
SAVE REQUEST LOG  ← FAILING HERE
     ↓
CCMS API

Therefore don’t debug the CCMS response validation yet.

⸻

14. Most likely database problem

The next exception will tell us exactly which one.

Typical possibilities are:

A. NULL column

For example:

ORA-01400: cannot insert NULL into ...

Then one of the fields being passed to the request-log table is null.

⸻

B. Value too large

ORA-12899: value too large for column

Potential candidates include:

NTB_ID
EMAIL
IP_ADDRESS
LOCALE
REQUEST_JSON

⸻

C. Invalid column/table

ORA-00942
ORA-00904

Then the CCMS request-log entity/table mapping is wrong in the environment.

⸻

D. Constraint violation

ORA-00001

Then the request log is attempting to insert a duplicate primary/unique key.

⸻

E. Hibernate mapping problem

Something like:

SQLGrammarException

Then we need to inspect the actual SQL/mapping.

⸻

15. One thing I want you to check immediately

Your log:

Saving CCMS Consent Write request log.
correlationId: 01a0666d-812d-773a-bf11-8f0f67a64ee2,
ntbId: 3,
correlationId: 24,
loanTypeId: {}

has suspicious output.

I would not ignore this.

It may indicate that the parameters are not what the CCMS service expects.

Please check the exact method declaration in:

ConsentUtil.java

for:

callCCMSConsentWriteAPI(...)

and the call from it into:

ConsentService.java

particularly:

generateConsentWriteRequest(...)

⸻

16. What I recommend you do now

Change 1 — expose the real exception

In ConsentService.java, around line 470:

catch (Exception e) {
    logger.error(
            "Exception while saving CCMS Consent Write request log",
            e);
    throw new RuntimeException(
            "Unable to save CCMS Consent Write request log.", e);
}

This is the first and most important change.

⸻

Change 2 — fix CVE NTB generation

Remove this:

if (appForm.getCbsAccountNumber() != null) {
    dob = new SimpleDateFormat("ddMMyyyy")
            .format(appForm.getCbsAccountNumber());
}

Replace it with the actual DOB field from ApplicationFormCveLoan.

Do not use account number as DOB.

⸻

Change 3 — fix NTB logging

Replace:

logger.info("CveLoanAction.java ::  NTB ID ----:  ",
        mobile + dob + loanType);

with:

logger.info(
        "CveLoanAction.java :: CVE NTB ID = {}",
        mobile + dob + loanType);

⸻

Change 4 — don’t CCMS WRITE state 29

Move the state 29 handling before:

writePrivacyConsentToCCMS(cveAppForm);

so:

STATE 28
    ↓
OTP success
    ↓
fetch CVE application
    ↓
CCMS WRITE
    ↓
save CCMS ID
    ↓
SMS
    ↓
success
STATE 29
    ↓
OTP/revocation success
    ↓
revocation response
    ↓
success

⸻

17. One more important point about your current logs

This:

Prepared CCMS consent. purposeCode: PR-REGULATO-000100
...
Prepared CCMS consent. purposeCode: PR-PROMOTIO-000106

is actually a good sign.

It means the CCMS consent payload construction is working.

The failure occurs after preparation:

Prepared CCMS consent
        ↓
Saving CCMS Consent Write request log
        ↓
ERROR

So I would not modify the purpose-code preparation logic.

⸻

What I need next for the exact final fix

Please share these two existing classes/methods exactly as they are:

1. ConsentUtil.java — specifically:

callCCMSConsentWriteAPI(...)

2. ConsentService.java — approximately lines 430–480, especially:

generateConsentWriteRequest(...)

With those two pieces, I can identify the exact field/parameter/database operation causing the failure and give you the line-by-line production change, rather than guessing at the CCMS layer.
