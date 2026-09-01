Yes. The logs now show the actual root cause, and it is happening before your new CCMS call in CveLoanAction can execute.

What the log is telling us

The important sequence is:

stateManagerBean 29
        ↓
processCBSOTP()
        ↓
ApplicationFormPersonalLoan app = ...
        ↓
getApplicationFormCveLoanBySeqId(14040)
        ↓
SQLGrammarException: could not load an entity:
[com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan#14040]
        ↓
processCBSOTP returns status = error
        ↓
CveLoanAction never enters:
if(json.get("status").equalsIgnoreCase("success"))

Your source confirms that processCBSOTP() is doing this at the state-29 CVE block: it loads ApplicationFormPersonalLoan, then immediately calls getApplicationFormCveLoanBySeqId(appSeqId). Pasted text (2)(20260827-033139).txt

And the cveData variable is not actually used after that call in the shown code. Pasted text (2)(20260827-033139).txt

So do not put more CCMS code into this failing section yet.

⸻

1. First fix this error

Your current code has:

ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app.getAppSeqId());
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app);
String refId = commonService.getCveReferenceIdBySeqId(appSeqId);
ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);
cveForm.setAppReferenceId(refId);

The failing line is:

ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);

Your log proves it:

getApplicationFormCveLoanBySeqId:::::14040
SQLGrammarException: could not load an entity:
[com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan#14040]

Minimal change

Since cveData is not being used in this block, remove/comment only this line:

// ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);

So it becomes:

ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app.getAppSeqId());
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app);
String refId = commonService.getCveReferenceIdBySeqId(appSeqId);
// ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);
cveForm.setAppReferenceId(refId);

This is the first change I recommend.

Do not change anything else in this section initially.

⸻

2. Why this error appeared after your change

You specifically wanted:

use ApplicationFormCveLoan instead of ApplicationFormPersonalLoan and save in ApplicationFormCveLoan table.

That is correct for CVE data persistence, but there are two separate things here:

Existing application used for OTP

Your existing code currently gets:

ApplicationFormPersonalLoan app =
    personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);

Then it performs OTP processing against that object.

CVE table

Separately, you added:

ApplicationFormCveLoan cveData =
    commonService.getApplicationFormCveLoanBySeqId(appSeqId);

But the database/entity mapping for ApplicationFormCveLoan#14040 is failing.

The log specifically shows Hibernate cannot load that entity. Therefore, simply changing the Java variable type is not enough. The ApplicationFormCveLoan entity must correctly map to the CVE table and the row must exist for sequence 14040.

Your existing service method also directly calls the CVE DAO:

public ApplicationFormCveLoan getApplicationFormCveLoanBySeqId(Integer appSeqId)
        throws NoResultException {
    logger.info("PersonalLoanService.java LNo : 952 : getApplicationFormCveLoanBySeqId:::::"+appSeqId);
    ApplicationFormCveLoan application =
        applicationFormCveLoanDao.getApplicationFormCveLoanBySeqId(appSeqId);
    logger.info("PersonalLoanService.java LNo : 955 : application.toString()"+application.toString());
    return application;
}

Pasted text (2).txt

So there is an actual CVE DAO/entity/database issue if that lookup is required.

⸻

3. Important: your current log is state 29

Your log says:

stateManagerBean 29

And your CveLoanAction code does:

if(json.get("status").toString().equalsIgnoreCase("success")){
    if(stateManagerBean.getState()==29){
        responseMessage =
            "success|Dear Customer, Your revocation request has been successfully registered.";
        ...
        return "jsonResponsePage";
    }

This is clearly the CVE revocation flow.

Your source also shows that processCBSOTP() has a dedicated stateId == 29 block. Pasted text (2)(20260827-033139).txt

Therefore:

If your requirement is NEW CVE consent

You should not call CCMS Write Consent in state 29, because state 29 is the revocation flow.

The CCMS write should happen in the normal CVE submit/OTP-success flow, not the revoke flow.

⸻

4. Your desired flow should be this

For normal CVE:

CVE final submit
      ↓
OTP verification
      ↓
processCBSOTP()
      ↓
OTP authentication successful
      ↓
return JSON status = success
      ↓
CveLoanAction
      ↓
CCMS Write API
      ↓
CCMS success
      ↓
save CCMS Consent ID
      ↓
success response

Your current action already has the correct place conceptually:

json = cveProcessManagerPersonalImpl.processCBSOTP(...);

followed by:

if(json.get("status").toString().equalsIgnoreCase("success")){

The CCMS call should be placed inside this success block, before returning success.

⸻

5. But don’t call CCMS before fixing the current exception

Right now you have:

processCBSOTP()
       ↓
SQLGrammarException
       ↓
status = error

So this code:

if(json.get("status").toString().equalsIgnoreCase("success")){

is never entered.

That’s why adding:

writePrivacyConsentToCCMS(...)

there appears to “not work”.

The CCMS method isn’t the problem yet.

⸻

6. Fix processCBSOTP() first

File

CveProcessManagerImpl.java

Find this block

ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app.getAppSeqId());
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app);
String refId = commonService.getCveReferenceIdBySeqId(appSeqId);
ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);
cveForm.setAppReferenceId(refId);

Change only this

ApplicationFormPersonalLoan app = personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app.getAppSeqId());
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::"+app);
String refId = commonService.getCveReferenceIdBySeqId(appSeqId);
// ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);
cveForm.setAppReferenceId(refId);

This is minimal and is justified because cveData is not used in the subsequent code shown. Pasted text (2)(20260827-033139).txt

⸻

7. Do NOT change this yet

Do not immediately change:

ApplicationFormPersonalLoan app

to:

ApplicationFormCveLoan app

inside processCBSOTP().

Why?

Because the existing OTP logic accesses many ApplicationFormPersonalLoan properties:

app.getAppOTPAttemptCount()
app.setAppOTPAttemptCount(...)
app.getAppDataSourceId()
app.setAppWorkEmail(...)
app.setAppMobileVerified(...)
app.setAppResTypeAtVerified(...)
app.setAppMobileVerificationCodeReceived(...)
app.setAppEmailVerified(...)
app.setAppOTPVerifyType(...)

The source shows this existing OTP flow operating on ApplicationFormPersonalLoan. Pasted text (2)(20260827-033139).txt

Changing the entire object to ApplicationFormCveLoan would therefore be a large and risky change, not the minimal CCMS change you asked for.

⸻

8. CVE table should be saved separately

You already have the CVE service save method:

public ApplicationFormCveLoan save(ApplicationFormCveLoan cveForm)
        throws SQLException {
    return applicationFormCveLoanDao.save(cveForm.getAppSeqId(), cveForm);
}

Pasted text (2).txt

So the correct architecture is:

ApplicationFormPersonalLoan
        ↓
existing OTP processing
ApplicationFormCveLoan
        ↓
CVE-specific data / CCMS consent information

rather than replacing the entire OTP object.

⸻

9. Then add CCMS call in CveLoanAction

After the above exception is fixed, your action can do:

if(json.get("status").toString().equalsIgnoreCase("success")){
    if(stateManagerBean.getState()==29){
        // REVOCATION FLOW
        responseMessage =
            "success|Dear Customer, Your revocation request has been successfully registered.";
        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
        return "jsonResponsePage";
    }else{
        // NORMAL CVE OTP SUCCESS
        // CCMS WRITE SHOULD COME HERE
        responseMessage = "success|" + json.getString("message");
        return "jsonResponsePage";
    }
}

But before giving you the exact CCMS code here, we need to use the actual CVE consent fields from your existing CVE JSP/action. From your earlier code, those include things such as:

appPrivacyConsentFlag
appNtbId
appPrivacyLocale

and the CCMS method needs:

NTB ID
mobile
email
IP
privacy locale
Constants.CVE_ID

The Home Loan implementation you showed uses exactly this pattern.

⸻

10. Very important correction to your current CCMS condition

Your Home Loan code has:

if (application.getAppPrivacyConsentFlag() != null
        && !application.getAppPrivacyConsentFlag().equals("Y")
        && !(application.getAppMobileVerified() != null
        && application.getAppMobileVerified().equalsIgnoreCase("Y"))) {
    boolean ccmsWriteStatus =
        writePrivacyConsentToCCMS(application, quote,
                                  loanScenarioBean, loanTypeId);
    ...
}

For CVE, don’t blindly copy this condition.

Your requirement is:

after OTP call, if OTP is success, call CCMS API.

Therefore the sequence should be:

processCBSOTP()
        ↓
json.status == success
        ↓
normal CVE flow
        ↓
writePrivacyConsentToCCMS()

not:

processCBSOTP()
        ↓
CCMS
        ↓
OTP

⸻

11. Your immediate next test

Make only this one change first:

// ApplicationFormCveLoan cveData = commonService.getApplicationFormCveLoanBySeqId(appSeqId);

Deploy and test the same OTP.

You should then see:

CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::14040
...
OTP authentication successful

instead of:

SQLGrammarException: could not load an entity:
ApplicationFormCveLoan#14040

If the next error is another CVE-table-related error, we fix that specific point rather than changing the whole flow.

⸻

One more thing I noticed

Your log has:

stateManagerBean 29

and the response is:

Dear Customer, Your revocation request has been successfully registered.

So this particular test is a revocation request, not the normal CVE consent submission.

The normal CCMS Write Consent API should be tested with the state that represents the normal CVE final submission, otherwise you risk writing a new consent during a revoke operation.

Your current CveLoanAction explicitly treats state 29 as revocation. Pasted text(38).txt

In short

Do these in this order:

1. CveProcessManagerImpl.java — comment/remove the unused getApplicationFormCveLoanBySeqId(appSeqId) call.
2. Verify OTP succeeds without SQLGrammarException.
3. Test the normal CVE final submit, not state 29 revocation.
4. In CveLoanAction.java, inside json.status == success for the normal CVE flow, call writePrivacyConsentToCCMS().
5. Pass ApplicationFormCveLoan to the save operation for the CCMS consent ID.
6. Do not convert the existing OTP ApplicationFormPersonalLoan app object unless the CVE table/entity architecture requires it elsewhere.

The current error is therefore not a CCMS API error; it is the premature ApplicationFormCveLoan Hibernate load at appSeqId=14040.
