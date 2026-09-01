Yes. The logs make the problem much clearer now.

The important point

Your CCMS code is not the reason for the current SQLGrammarException.

The failure happens before your writePrivacyConsentToCCMS() is reached.

From your log:

CveProcessManagerImpl LNo : 1164 ::app.getAppSeqId:::14040
CveProcessManagerImpl LNo : 1165 ::app.getAppSeqId:::com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan@21a89701
CommonService.java ... getCveReferenceIdBySeqId>>appSeqId::14040
PersonalLoanService.java ... getApplicationFormCveLoanBySeqId:::::14040
CveProcessManagerImpl.java ... SQLGrammarException:
could not load an entity:
[com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan#14040]

There are two separate issues in the code you posted.

⸻

1. First fix: do NOT use personalLoanService for CVE entity

You currently have:

cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());

This is wrong architecturally.

You already have:

@Autowired
private CveLoanService cveLoanService;

So for ApplicationFormCveLoan, use cveLoanService, not personalLoanService.

Change this

cveAppForm = personalLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());

To this

cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());

This is the first minimal change.

⸻

2. Your writePrivacyConsentToCCMS() also uses the wrong service

You currently have:

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = personalLoanService.save(application);

Again, this is a CVE entity:

ApplicationFormCveLoan

Therefore it should be saved through:

cveLoanService

Change

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = personalLoanService.save(application);

To

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = cveLoanService.save(application);

Do not use PersonalLoanService for ApplicationFormCveLoan.

⸻

3. But there is a more important problem in your logs

Even after making the above two changes, your current error can still occur.

Look carefully at this:

CveProcessManagerImpl LNo : 1165 ::
com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan@21a89701

That means inside:

CveProcessManagerImpl.processCBSOTP(...)

the variable app is still an:

ApplicationFormPersonalLoan

But later the code tries to load:

ApplicationFormCveLoan#14040

This is the actual location we need to correct.

⸻

4. Check CveProcessManagerImpl.processCBSOTP()

Your log says:

CveProcessManagerImpl LNo : 1164 ::app.getAppSeqId:::14040
CveProcessManagerImpl LNo : 1165 ::app.getAppSeqId:::com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan@21a89701

So somewhere around line 1146–1165 you have something similar to:

ApplicationFormPersonalLoan app = ...

or:

ApplicationFormPersonalLoan app =
        personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);

Then later you call:

getApplicationFormCveLoanBySeqId(appSeqId)

That is mixing Personal Loan and CVE objects.

For CVE, the object should be:

ApplicationFormCveLoan

⸻

5. Minimal change inside CveProcessManagerImpl

You need to find code around the log:

logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::" + app.getAppSeqId());
logger.info("CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::" + app);

Look immediately above those lines.

If you have:

ApplicationFormPersonalLoan app =
        personalLoanService.getApplicationFormPersonalLoanByAppSeqId(appSeqId);

then for CVE it should be changed to:

ApplicationFormCveLoan app =
        cveLoanService.getApplicationFormCveLoanByAppSeqId(appSeqId);

and make sure CveProcessManagerImpl has:

@Autowired
private CveLoanService cveLoanService;

and import:

import com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan;
import com.mintstreet.loan.cveloan.service.CveLoanService;

However

I don’t want you to blindly replace that line yet, because your CveProcessManagerImpl code is not included in this message. The log proves that this class is where the current SQL error starts.

⸻

6. Why the SQLGrammarException is occurring

This line is the strongest evidence:

SQLGrammarException: could not load an entity:
[com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan#14040]

Hibernate is attempting to load:

ApplicationFormCveLoan

with:

appSeqId = 14040

and the database query generated from the ApplicationFormCveLoan Hibernate mapping is failing.

So there are two possibilities:

Possibility A — wrong CVE DAO/service implementation

Your method:

getApplicationFormCveLoanByAppSeqId(14040)

may be implemented incorrectly.

Possibility B — ApplicationFormCveLoan entity mapping has a DB mismatch

For example, the entity may contain:

@Column(name="SOME_COLUMN")
private String someColumn;

but that column does not exist in the actual CVE table.

Or the table mapping may be incorrect:

@Table(name="...")

Or a Hibernate relationship mapping may be invalid.

⸻

7. Do this test first

Before changing the Hibernate entity, make the CCMS flow cleanly separated.

Your CveLoanAction successful OTP block should be:

if(json.get("status").toString().equalsIgnoreCase("success")){
    
    if(stateManagerBean.getState()==29){
        responseMessage = "success|Dear Customer, Your revocation request has been successfully registered.";
        logger.info("CveLoanAction.java :: processCBSOTP for CVE called..json:::: success1:: " + json);
        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(appSeqId);
        return "jsonResponsePage";
    } else {
        try {
            Integer cveAppSeqId = SessionUtil.getPersonalLoanTypeSequenceId();
            logger.info("CveLoanAction.java :: CVE AppSeqId for CCMS : " + cveAppSeqId);
            cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(cveAppSeqId);
            if (cveAppForm == null) {
                logger.info(
                    "CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : "
                    + cveAppSeqId);
                responseMessage =
                    "error|Unable to process privacy consent. Please try again.";
                return "jsonResponsePage";
            }
            boolean ccmsWriteStatus =
                    writePrivacyConsentToCCMS(cveAppForm);
            if (!ccmsWriteStatus) {
                return "jsonResponsePage";
            }
            responseMessage = "success|" + json.getString("message");
            return "jsonResponsePage";
        } catch (Exception e) {
            logger.info(
                "CveLoanAction.java :: Exception while writing CVE consent to CCMS",
                e);
            responseMessage =
                "error|Unable to write consent to CCMS. Please try again.";
            return "jsonResponsePage";
        }
    }
}

The important change is:

cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(cveAppSeqId);

not:

personalLoanService.getApplicationFormCveLoanByAppSeqId(...)

⸻

8. Change the save also

Inside:

writePrivacyConsentToCCMS()

you currently have:

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = personalLoanService.save(application);

Change it to:

application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = cveLoanService.save(application);

So the complete section becomes:

String ccmsConsentId = consentWrite.getConsentId().trim();
/*
 * 8. Save CCMS consent ID in CVE application
 */
application.setAppCcmsConsentId(ccmsConsentId);
cveAppForm = cveLoanService.save(application);
if (cveAppForm == null) {
    logger.info(
        "CveLoanAction.java :: CCMS consent was successful but CVE application update failed. "
        + "AppSeqId : " + application.getAppSeqId());
    responseMessage =
        "error|Consent was recorded, but application details could not be updated.";
    return false;
}

⸻

9. There is another bug in your NTB ID method

You currently have:

if (appForm.getCbsAccountNumber() != null) {
    dob = new SimpleDateFormat("ddMMyyyy")
            .format(appForm.getCbsAccountNumber());
}

This is almost certainly incorrect.

cbsAccountNumber sounds like an account number, not a DOB.

Your log/code should use the actual DOB field from ApplicationFormCveLoan.

For example, if your entity has:

getAppDob()

then:

dob = new SimpleDateFormat("ddMMyyyy")
        .format(appForm.getAppDob());

But do not change this until we see the actual ApplicationFormCveLoan fields. I don’t want to guess the CVE DOB field.

Also your current catch hides the problem:

catch (Exception e) {
    logger.info("Exception while formatting DOB for NTB ID", e);
}

Therefore the NTB ID can become:

mobile + "" + loanType

without you realizing it.

⸻

10. One more important issue: privacy data must already be in DB

Your writePrivacyConsentToCCMS() checks:

if (!"Y".equalsIgnoreCase(application.getAppPrivacyConsentFlag()))

and:

if (!ValidatorUtil.isValid(application.getAppNtbId()))

and:

if (!ValidatorUtil.isValid(application.getAppPrivacyLocale()))

Therefore, before OTP success, the CVE record must already contain:

APP_PRIVACY_CONSENT_FLAG = Y
APP_NTB_ID                = generated NTB ID
APP_PRIVACY_LOCALE        = selected locale

Your savePrivacyConsentCve() does this:

appForm.setAppPrivacyConsentFlag("Y");
appForm.setAppNtbId(ntbId);
cveLoanService.save(appForm);

but it does not set APP_PRIVACY_LOCALE.

You need to verify that the frontend is sending/storing the selected locale.

For example:

appForm.setAppPrivacyLocale(privacyLocale);

if privacyLocale is available in that request.

Otherwise this validation will fail:

if (!ValidatorUtil.isValid(application.getAppPrivacyLocale())) {
    responseMessage =
        "error|Invalid privacy language details. Please accept SBI Privacy Notice again.";
    return false;
}

⸻

11. Recommended flow

Your CVE flow should ultimately be:

CVE Privacy Popup
       |
       | Accept
       v
savePrivacyConsentCve()
       |
       +--> ApplicationFormCveLoan
       |
       +--> APP_PRIVACY_CONSENT_FLAG = Y
       +--> APP_NTB_ID = generated NTB ID
       +--> APP_PRIVACY_LOCALE = selected locale
       |
       v
CVE Application DB
       |
       v
CBS Call
       |
       v
OTP Page
       |
       v
OTP Submit
       |
       v
processCBSOTP()
       |
       | OTP SUCCESS
       v
ApplicationFormCveLoan
       |
       v
writePrivacyConsentToCCMS()
       |
       +--> Validate Y
       +--> Validate NTB ID
       +--> Validate Locale
       +--> Get Mobile
       +--> Get Email
       +--> Get IP
       |
       v
CCMS API
       |
       v
CCMS SUCCESS
       |
       +--> APP_CCMS_CONSENT_ID saved
       |
       v
SMS
       |
       v
success response

⸻

12. But your current log shows the flow is failing before this

This is the critical part:

processCBSOTP
      ↓
ApplicationFormPersonalLoan@21a89701
      ↓
getCveReferenceIdBySeqId(14040)
      ↓
getApplicationFormCveLoanBySeqId(14040)
      ↓
SQLGrammarException
      ↓
processCBSOTP returns:
status = error

Therefore your code never reaches:

writePrivacyConsentToCCMS(cveAppForm);

because this condition is never true:

if(json.get("status").toString().equalsIgnoreCase("success"))

Your log confirms:

{
  "message":"Sorry for the inconvenience, Please click here to start again",
  "status":"error"
}

So don’t troubleshoot CCMS API yet. First fix the CVE entity loading inside CveProcessManagerImpl.

⸻

What I need from you for the exact next change

Please share these 3 pieces of existing code:

1. CveProcessManagerImpl.processCBSOTP() — especially around line 1146–1165
2. CveLoanService interface/class — specifically:

getApplicationFormCveLoanByAppSeqId()
save()

3. ApplicationFormCveLoan.java entity

With those three, I can point out the exact lines to replace, including whether the SQLGrammarException is coming from the CVE DAO query or from the ApplicationFormCveLoan Hibernate mapping. That is the part causing your current error.
