Yes. The new log gives us a much clearer picture. The failure is inside consentWriteDao.save(...), not in the CCMS API call itself.

There are also two definite bugs in your logging, which are making the diagnosis confusing.

1. First fix the incorrect log statement

You currently have:

logger.info("Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, correlationId: {}, loanTypeId: {}", correlationId, ntbId, loanTypeId);

There are 4 {} placeholders but only 3 arguments.

You intended:

logger.info(
    "Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, loanTypeId: {}",
    correlationId,
    ntbId,
    loanTypeId
);

That’s why your log is showing:

correlationId: 01a06729-fac8-75b8-be51-fe8c729766f2,
ntbId: 3,
correlationId: 24,
loanTypeId: {}

The values are shifted because the logger has 4 placeholders but 3 values.

Also, ntbId: 3 is important. It suggests your CVE NTB ID currently being passed to CCMS is simply 3, which we should investigate separately after fixing the DB save issue.

⸻

2. Your second log is in the wrong place

You have:

logger.info("CCMS Consent Write request log saved. writeId: {}", consentWrite.getConsentWriteId());
try {
    consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);

The log says:

CCMS Consent Write request log saved. writeId: null

before the database save happens.

So this log is misleading.

Move it after the save().

⸻

3. Replace your entire save section with this

Replace this:

ConsentWriteLog consentWrite = new ConsentWriteLog();
consentWrite.setxCorrelationId(correlationId);
consentWrite.setNtbNumber(ntbId);
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());
consentWrite.setLoanType(String.valueOf(loanTypeId));
logger.info("Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, correlationId: {}, loanTypeId: {}", correlationId, ntbId, loanTypeId);
logger.info("CCMS Consent Write request log saved. writeId: {}", consentWrite.getConsentWriteId());
try {
    consentWrite = consentWriteDao.save(consentWrite.getConsentWriteId(), consentWrite);
} catch (Exception e) {
      logger.error("Exception while saving CCMS Consent Write request log", e);
        throw new RuntimeException( "Unable to save CCMS Consent Write request log.", e);
}
if (consentWrite == null || consentWrite.getConsentWriteId() == null) {
    throw new RuntimeException ("Unable to save CCMS Consent Write request log.");
}
logger.info("CCMS Consent Write request log saved. writeId: {}, correlationId: {}", consentWrite.getConsentWriteId(), correlationId);
return consentWrite;

with:

ConsentWriteLog consentWrite = new ConsentWriteLog();
consentWrite.setxCorrelationId(correlationId);
consentWrite.setNtbNumber(ntbId);
consentWrite.setConsentWriteRequest(consentRequest.toString());
consentWrite.setRequestEntryTime(new Date());
consentWrite.setLoanType(String.valueOf(loanTypeId));
logger.info(
    "Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, loanTypeId: {}",
    correlationId,
    ntbId,
    loanTypeId
);
logger.info(
    "CCMS ConsentWriteLog before save. writeId: {}, correlationId: {}, ntbId: {}, loanType: {}",
    consentWrite.getConsentWriteId(),
    correlationId,
    ntbId,
    consentWrite.getLoanType()
);
try {
    consentWrite = consentWriteDao.save(
        consentWrite.getConsentWriteId(),
        consentWrite
    );
} catch (Exception e) {
    logger.error(
        "Exception while saving CCMS Consent Write request log. "
        + "correlationId: {}, ntbId: {}, loanTypeId: {}",
        correlationId,
        ntbId,
        loanTypeId,
        e
    );
    throw new RuntimeException(
        "Unable to save CCMS Consent Write request log.",
        e
    );
}
if (consentWrite == null) {
    logger.error(
        "CCMS ConsentWriteLog save returned NULL. correlationId: {}, ntbId: {}, loanTypeId: {}",
        correlationId,
        ntbId,
        loanTypeId
    );
    throw new RuntimeException(
        "Unable to save CCMS Consent Write request log."
    );
}
if (consentWrite.getConsentWriteId() == null) {
    logger.error(
        "CCMS ConsentWriteLog saved but generated ID is NULL. "
        + "correlationId: {}, ntbId: {}, loanTypeId: {}",
        correlationId,
        ntbId,
        loanTypeId
    );
    throw new RuntimeException(
        "Unable to save CCMS Consent Write request log."
    );
}
logger.info(
    "CCMS Consent Write request log saved successfully. "
    + "writeId: {}, correlationId: {}, ntbId: {}, loanTypeId: {}",
    consentWrite.getConsentWriteId(),
    correlationId,
    ntbId,
    loanTypeId
);
return consentWrite;

⸻

4. But this alone may NOT fix the actual problem

Your current stack trace says:

java.lang.RuntimeException: Unable to save CCMS Consent Write request log.
    at ConsentService.generateConsentWriteRequest(ConsentService.java:476)

The important part is that your new code is now catching the original exception:

catch (Exception e) {
    logger.error(
        "Exception while saving CCMS Consent Write request log",
        e);
    throw new RuntimeException(
        "Unable to save CCMS Consent Write request log.",
        e);
}

But the log you posted does not contain the nested/root exception.

That is unusual.

We need to determine what consentWriteDao.save() is actually throwing.

It could be something like:

ORA-01400: cannot insert NULL into ...

or:

ORA-00904: invalid identifier

or:

ORA-12899: value too large for column

or:

ConstraintViolationException

or:

SQLGrammarException

or:

PropertyValueException

Therefore the next most important file is:

ConsentWriteLog

and:

ConsentWriteLogDao

especially the implementation of:

consentWriteDao.save(...)

⸻

5. There is a very important clue in your log

Your log currently says:

Prepared CCMS consent...

for all the purposes.

Then:

Saving CCMS Consent Write request log...

Then immediately:

RuntimeException: Unable to save CCMS Consent Write request log.

This means:

Working

getPurposeResponseForWrite()
        ↓
prepareConsentsFromPurposeResponse()
        ↓
ConsentRequest creation
        ↓
JSON conversion
        ↓
ConsentWriteLog object creation

Failing

consentWriteDao.save(...)
        ↓
DATABASE INSERT

Not reached

CCMS Consent Write API

So do not modify prepareConsentsFromPurposeResponse() or the CCMS payload yet.

The current problem is most likely your local CCMS request-log database persistence.

⸻

6. Check ConsentWriteLog entity

We need to inspect something like:

@Entity
@Table(name = "...")
public class ConsentWriteLog {
    @Id
    @GeneratedValue(...)
    private Integer consentWriteId;
    private String xCorrelationId;
    private String ntbNumber;
    private String consentWriteRequest;
    private Date requestEntryTime;
    private String loanType;
}

I particularly want to verify these five fields:

consentWriteId
xCorrelationId
ntbNumber
consentWriteRequest
requestEntryTime
loanType

Because you recently added:

consentWrite.setLoanType(String.valueOf(loanTypeId));

If loanType was newly introduced, there are three things that must match:

Java entity field
        ↓
Hibernate mapping
        ↓
Oracle database column

For example:

@Column(name = "LOAN_TYPE")
private String loanType;

must correspond to an actual database column:

LOAN_TYPE

If the DB column doesn’t exist, you’ll get something like:

ORA-00904: "LOAN_TYPE": invalid identifier

⸻

7. Very important: verify the database column for loanType

Because your current code has:

consentWrite.setLoanType(String.valueOf(loanTypeId));

and your previous implementation appears to have had this commented-out code:

// consentWrite = consentWriteDao.save(
//     consentWrite.getConsentWriteId(),
//     consentWrite
// );

You need to verify whether loanType was actually added to the ConsentWriteLog table.

Run:

DESC <CCMS_CONSENT_WRITE_LOG_TABLE>;

or:

SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, NULLABLE
FROM USER_TAB_COLUMNS
WHERE TABLE_NAME = '<CCMS_CONSENT_WRITE_LOG_TABLE>'
ORDER BY COLUMN_ID;

Do not guess the table name. Get it from the @Table annotation in ConsentWriteLog.

⸻

8. Also check column sizes

Your code does:

consentWrite.setConsentWriteRequest(consentRequest.toString());

This can generate a large JSON string.

If the DB column is something like:

VARCHAR2(4000)

you could get:

ORA-12899: value too large for column

or a Hibernate/JDBC conversion error.

For a complete CCMS request JSON, the DB column may need to be:

CLOB

depending on your existing schema.

Again, don’t change this yet. First inspect the existing ConsentWriteLog entity and DB column.

⸻

9. Check loanTypeId

Your method correctly starts with:

if (loanTypeId == null) {
    throw new IllegalArgumentException(
        "Loan type ID is required for CCMS Consent Write."
    );
}

And your CVE caller is:

consentUtil.callCCMSConsentWriteAPI(
    application.getAppNtbId(),
    mobile,
    email,
    ipAddress,
    application.getAppPrivacyLocale(),
    Constants.CVE_ID
);

So if Constants.CVE_ID is, for example:

24

then:

String.valueOf(loanTypeId)

will become:

"24"

That’s perfectly valid if the database column is String/VARCHAR2.

Your broken log makes it look like:

loanTypeId: {}

but that is simply because of the placeholder mismatch.

⸻

10. Fix the logging first and rerun

After the change, you should see something similar to:

Saving CCMS Consent Write request log.
correlationId: 01a06729-fac8-75b8-be51-fe8c729766f2,
ntbId: 3,
loanTypeId: 24
CCMS ConsentWriteLog before save.
writeId: null,
correlationId: 01a06729-fac8-75b8-be51-fe8c729766f2,
ntbId: 3,
loanType: 24

Then, if DB insertion fails, you should get the actual root exception beneath:

Exception while saving CCMS Consent Write request log

For example:

Caused by: org.hibernate.exception.SQLGrammarException
Caused by: java.sql.SQLException
Caused by: oracle.jdbc...
ORA-00904...

That Caused by section is what we need.

⸻

11. One more issue: NTB ID = 3

This is separate and should be fixed after the DB problem.

Your current log says:

ntbId: 3

Earlier your intended CVE NTB generation was:

mobile + DOB + loanTypeId

But your current backend method was using:

if (appForm.getCbsAccountNumber() != null) {
    dob = new SimpleDateFormat("ddMMyyyy")
            .format(appForm.getCbsAccountNumber());
}

That is incorrect because:

getCbsAccountNumber()

returns an account number, not a DOB.

So do not keep that implementation.

We need to use the actual DOB field from:

ApplicationFormCveLoan

For example, if your entity contains:

private Date dateOfBirth;

then we would use:

String dob = new SimpleDateFormat("ddMMyyyy")
        .format(application.getDateOfBirth());

But I don’t want to invent the field name. We should use the actual CVE entity field.

⸻

12. There is also a small cleanup in your method

You currently have:

String correlationId = UuidV7Generator.generateV7().toString();

That’s fine.

Then:

payloadHeaders.setxCorelationId(correlationId);

Notice:

Corelation

rather than:

Correlation

If that spelling comes from the existing DTO/API contract, don’t change it. It is likely intentional because the CCMS API expects that JSON property.

⸻

13. Your complete method structure should be

The overall flow should remain:

public ConsentWriteLog generateConsentWriteRequest(
        String ntbId,
        String mobile,
        String email,
        String ipAddress,
        String locale,
        Integer loanTypeId) {
    // 1. Validate loan type
    if (loanTypeId == null) {
        throw new IllegalArgumentException(
            "Loan type ID is required for CCMS Consent Write."
        );
    }
    // 2. Create CCMS request objects
    // 3. Generate correlation ID
    // 4. Load CCMS configuration
    // 5. Prepare DP data
    // 6. Get purpose enquiry
    // 7. Prepare consent list
    // 8. Build CCMS request JSON
    // 9. Create ConsentWriteLog
    // 10. Save ConsentWriteLog
    try {
        consentWrite = consentWriteDao.save(
            consentWrite.getConsentWriteId(),
            consentWrite
        );
    } catch (Exception e) {
        logger.error(
            "Exception while saving CCMS Consent Write request log",
            e
        );
        throw new RuntimeException(
            "Unable to save CCMS Consent Write request log.",
            e
        );
    }
    // 11. Verify generated ID
    // 12. Return log
    return consentWrite;
}

That architecture is correct.

⸻

14. What I need from you next

Please share these two classes/methods exactly as they exist:

A. ConsentWriteLog.java

Especially:

@Entity
@Table(...)
public class ConsentWriteLog

and all fields/mappings.

B. consentWriteDao.save(...)

For example:

public ConsentWriteLog save(...)

from your DAO/base DAO implementation.

Also, if possible, share the Oracle table definition for the ConsentWriteLog table.

With those, I can identify the exact database column/mapping causing the exception and give you the precise line-by-line change rather than guessing.

Most important conclusion from the current logs

Your CVE flow has progressed this far:

CVE OTP verification
        ↓
SUCCESS
        ↓
CVE ApplicationForm fetched
        ↓
CCMS purpose enquiry
        ↓
All consent purposes prepared
        ↓
CCMS request JSON prepared
        ↓
ConsentWriteLog created
        ↓
❌ consentWriteDao.save()
        ↓
CCMS API NOT YET CALLED

So the immediate issue is not CVE OTP, not CveLoanService, and not the CCMS API. It is the persistence of ConsentWriteLog.

And the first correction is definitely the malformed logger:

logger.info(
    "Saving CCMS Consent Write request log. correlationId: {}, ntbId: {}, loanTypeId: {}",
    correlationId,
    ntbId,
    loanTypeId
);

Then send me ConsentWriteLog + consentWriteDao.save(). I can trace the DB insert field-by-field and give you the exact fix.
