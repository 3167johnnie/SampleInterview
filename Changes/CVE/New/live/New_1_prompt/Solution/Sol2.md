Now that I can see the ApplicationFormCveLoan entity, the reason is much clearer.

The key point

Your entity has:

@Id
@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="G8")
@Column(name="APP_SEQ_ID")
private Integer appSeqId;

So this call:

applicationFormCveLoanDao.findById(appSeqId, ApplicationFormCveLoan.class);

is valid for finding the record by APP_SEQ_ID.

Therefore, if:

SessionUtil.getPersonalLoanTypeSequenceId()

contains 14040, but:

cveLoanService.getApplicationFormCveLoanByAppSeqId(14040)

returns null, the most likely problem is:

14040 is not actually present as APP_SEQ_ID in RUPEEPOWER_OCAS_T_00291.

First, prove it with DB query

Run:

SELECT APP_SEQ_ID,
       APP_REFERENCE_ID,
       CBS_MOBILE_NUMBER,
       CVE_APP_EMAIL
FROM RUPEEPOWER_OCAS_T_00291
WHERE APP_SEQ_ID = 14040;

If it returns a row

Then the problem is in your DAO/session/transaction handling.

If it returns no row

Then SessionUtil.getPersonalLoanTypeSequenceId() is holding the wrong application sequence ID for the CVE table.

⸻

Your immediate code change

Since you already have a CVE-specific session value:

SessionUtil.getCveLoanApplicationSequenceId()

you should use that for CVE.

Change:

cveAppForm = cveLoanService.getApplicationFormCveLoanByAppSeqId(
        SessionUtil.getPersonalLoanTypeSequenceId());

to:

Integer cveAppSeqId =
        SessionUtil.getCveLoanApplicationSequenceId();
logger.info("CveLoanAction.java :: CVE Application Sequence ID = "
        + cveAppSeqId);
if (cveAppSeqId == null) {
    logger.info("CveLoanAction.java :: CVE Application Sequence ID is NULL");
    responseMessage =
            "error|CVE application details not found. Please try again.";
    return "jsonResponsePage";
}
cveAppForm =
        cveLoanService.getApplicationFormCveLoanByAppSeqId(cveAppSeqId);

Then check your log.

⸻

But there is an important issue

Earlier your CVE flow was using:

SessionUtil.getPersonalLoanTypeSequenceId()

and your log showed:

appSeqId:::14040

If 14040 belongs to the Personal Loan table, then this is exactly why:

CveLoanService
       ↓
ApplicationFormCveLoan
       ↓
RUPEEPOWER_OCAS_T_00291
       ↓
APP_SEQ_ID = 14040
       ↓
NO RECORD
       ↓
null

So don’t solve this by changing the DAO.

You need to make sure that the CVE application’s own sequence ID is stored in the CVE session variable when the CVE application is created.

⸻

Best solution for your current flow

Before OTP verification, where the CVE application is created/saved, you should have something equivalent to:

ApplicationFormCveLoan cveApplication =
        cveLoanService.save(cveAppForm);
if (cveApplication != null) {
    Integer cveAppSeqId = cveApplication.getAppSeqId();
    logger.info("CveLoanAction.java :: CVE Application created. "
            + "AppSeqId = " + cveAppSeqId);
    SessionUtil.setCveLoanApplicationSequenceId(cveAppSeqId);
    // If existing CVE OTP process requires this:
    SessionUtil.setPersonalLoanTypeSequenceId(cveAppSeqId);
}

The important part is that the value comes from:

cveApplication.getAppSeqId()

after saving ApplicationFormCveLoan.

Do not manufacture the sequence ID and do not take a Personal Loan sequence ID.

⸻

Then your CCMS code becomes

Inside:

if(json.get("status").toString().equalsIgnoreCase("success")){

use:

if (json.get("status").toString().equalsIgnoreCase("success")) {
    if (stateManagerBean.getState() == 29) {
        responseMessage =
                "success|Dear Customer, Your revocation request has been successfully registered.";
        appSeqId = null;
        SessionUtil.setPersonalLoanTypeSequenceId(null);
        return "jsonResponsePage";
    }
    if (stateManagerBean.getState() == 28) {
        Integer cveAppSeqId =
                SessionUtil.getCveLoanApplicationSequenceId();
        logger.info(
                "CveLoanAction.java :: CVE AppSeqId after OTP success = "
                + cveAppSeqId);
        if (cveAppSeqId == null) {
            logger.info(
                    "CveLoanAction.java :: CVE AppSeqId is NULL");
            responseMessage =
                    "error|CVE application details not found. Please try again.";
            return "jsonResponsePage";
        }
        try {
            cveAppForm =
                    cveLoanService.getApplicationFormCveLoanByAppSeqId(
                            cveAppSeqId);
            if (cveAppForm == null) {
                logger.info(
                        "CveLoanAction.java :: ApplicationFormCveLoan "
                        + "not found for AppSeqId = "
                        + cveAppSeqId);
                responseMessage =
                        "error|CVE application details not found. Please try again.";
                return "jsonResponsePage";
            }
            logger.info(
                    "CveLoanAction.java :: ApplicationFormCveLoan found. "
                    + "AppSeqId = " + cveAppForm.getAppSeqId());
            // CCMS ONLY AFTER OTP SUCCESS
            boolean ccmsWriteStatus =
                    writePrivacyConsentToCCMS(cveAppForm);
            if (!ccmsWriteStatus) {
                return "jsonResponsePage";
            }
            responseMessage =
                    "success|" + json.getString("message");
            return "jsonResponsePage";
        } catch (Exception e) {
            logger.error(
                    "CveLoanAction.java :: Error while fetching CVE application "
                    + "and writing CCMS consent",
                    e);
            responseMessage =
                    "error|Unable to process privacy consent. Please try again.";
            return "jsonResponsePage";
        }
    }
}

In short

Your entity mapping is correct:

APP_SEQ_ID → appSeqId

Your service method is also basically correct.

The thing you need to fix is which sequence ID you’re passing.

For CVE, use the CVE application’s appSeqId, preferably from SessionUtil.getCveLoanApplicationSequenceId(), and make sure that value was populated from ApplicationFormCveLoan.getAppSeqId() when the CVE application was initially saved.

If your DB query shows APP_SEQ_ID = 14040 does exist in RUPEEPOWER_OCAS_T_00291, then send me your ApplicationFormCveLoanDao class—that will be the next place to fix.
