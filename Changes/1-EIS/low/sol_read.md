I analyzed the successful response carefully.

Your CCMS Write integration is now working correctly.

The response body is:

```json
{
  "EIS_RESPONSE": {
    "success": true,
    "statusCode": 200,
    "body": {
      "ntbId": "9619155147290620081",
      "consentId": "019f6b4f-399a-71be-85a3-46cebcb4f751"
    }
  }
}
```

This is exactly the point where you should update the database.

## I recommend updating **only after** all these conditions are true:

```java
statusCode == 200
&& success == true
&& body != null
&& body.getConsentId() != null
```

Then perform the DB updates.

---

# Minimal production changes

You only need to modify **3 files**.

```
1. ConsentResponseConsentWriteBody.java
2. ConsentWriteLog.java
3. ConsentService.java
```

After that we'll update Quote/Application.

---

# STEP 1

## ConsentResponseConsentWriteBody.java

I'm assuming currently it has only ntbId.

Add:

```java
private String consentId;

public String getConsentId() {
    return consentId;
}

public void setConsentId(String consentId) {
    this.consentId = consentId;
}
```

Nothing else.

---

# STEP 2

## ConsentWriteLog.java

You already added

```
CONSENT_WRITE_NTB_ID
```

Now add

```java
@Column(name="CONSENT_WRITE_CONSENT_ID")
private String consentWriteConsentId;

@Column(name="CONSENT_WRITE_IS_ACTIVE")
private String consentWriteIsActive;

@Column(name="CONSENT_WRITE_LOAN_TYPE")
private Integer consentWriteLoanType;
```

Generate getters/setters.

Nothing else.

---

# STEP 3

Now go to

```
ConsentService.java
```

Inside

```
writeConsentToCCMS(...)
```

You already have

```java
ConsentResponseConsentWrite response =
...
```

Later you already check

```java
responseCode=200

responseStatus=true
```

Immediately after this block

```java
boolean success =
        "200".equals(responseCode)
        &&
        "true".equalsIgnoreCase(responseStatus);
```

Replace

```java
if(success){

    logger.info(...);

}
```

with

```java
if(success){

    logger.info("CCMS Consent Write successful.");

    if(response.getEisResponse()!=null
            &&
       response.getEisResponse().getBody()!=null){

        ConsentResponseConsentWriteBody body =
                response.getEisResponse().getBody();

        consentWrite.setConsentWriteConsentId(
                body.getConsentId());

        consentWrite.setConsentWriteIsActive("Y");

        /*
         * Loan Type ID
         * (pass while creating request)
         */
        consentWrite.setConsentWriteLoanType(
                loanTypeId);

        consentWriteDao.save(
                consentWrite.getConsentWriteId(),
                consentWrite);

        logger.info(
                "ConsentWriteLog updated successfully. "
                + "consentId : {}",
                body.getConsentId());

    }

}
```

This is the only change required inside this method.

---

# But there is one issue

Inside

```
writeConsentToCCMS()
```

You **don't have**

```
loanTypeId
```

available.

So the best minimal change is:

Current

```java
public ConsentWriteLog writeConsentToCCMS(
        ConsentWriteLog consentWrite)
```

Change to

```java
public ConsentWriteLog writeConsentToCCMS(
        ConsentWriteLog consentWrite,
        Integer loanTypeId)
```

Only this extra parameter.

Then

```
consentWrite.setConsentWriteLoanType(
        loanTypeId);
```

works.

---

# STEP 4

Now Quote table.

Wherever you're calling

```java
writeConsentToCCMS(...)
```

Currently something like

```java
ConsentWriteLog consentWrite =
        consentService.writeConsentToCCMS(consentWriteLog);
```

becomes

```java
ConsentWriteLog consentWrite =
        consentService.writeConsentToCCMS(
                consentWriteLog,
                loanTypeId);
```

---

Immediately after

```java
responseCode==200

responseStatus==true
```

add

```java
ApplicationFormHomeLoanQuote quote =
        applicationFormHomeLoanQuoteDao.findById(
                quoteId);

quote.setConsentId(
        consentWrite.getConsentWriteConsentId());

applicationFormHomeLoanQuoteDao.save(
        quote.getQuoteId(),
        quote);
```

That's it.

---

# STEP 5

Applicant table.

Immediately after Quote update

```java
ApplicationForm application =
        applicationDao.findById(appSeqId);

application.setConsentId(
        consentWrite.getConsentWriteConsentId());

applicationDao.save(
        application.getAppSeqId(),
        application);
```

---

# Final Flow

```
Purpose Enquiry
        │
        ▼
Prepare Write Request
        │
        ▼
CCMS Write
        │
        ▼
200 + success=true ?
        │
       YES
        │
        ▼
Read consentId
        │
        ▼
Update ConsentWriteLog
        │
        ├── CONSENT_WRITE_CONSENT_ID
        ├── CONSENT_WRITE_IS_ACTIVE='Y'
        ├── CONSENT_WRITE_LOAN_TYPE
        │
        ▼
Update Quote Table
        │
        ├── CONSENT_ID
        │
        ▼
Update Applicant Table
        │
        ├── CONSENT_ID
```

## One additional recommendation

Looking at your project, I would **not** update the Quote table and Applicant table inside `ConsentService`.

A cleaner design is:

* `ConsentService` should only:

  * call CCMS
  * update `ConsentWriteLog`
  * return the populated `ConsentWriteLog` (with `consentWriteConsentId`)

* `HomeProcessManagerImpl` (or whichever class orchestrates the Get Quote flow) should:

  * receive the updated `ConsentWriteLog`
  * update the Quote entity
  * update the Application entity
  * commit everything in the same transaction

This keeps `ConsentService` focused on CCMS integration and avoids mixing it with loan persistence logic. It also ensures all three database updates participate in the same transaction if one fails. Based on your current architecture, this is the approach I'd recommend for production.
