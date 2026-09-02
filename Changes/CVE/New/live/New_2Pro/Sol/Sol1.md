Now the new logs identify the **exact root cause**. This is not a `14050`/session problem and not a CCMS problem.

Your log says:

```text
CveLoanService.java:26
java.lang.NullPointerException
```

And line 26 in the `CveLoanService` you sent is effectively:

```java
ApplicationFormCveLoan application =
    applicationFormCveLoanDao.findById(appSeqId, ApplicationFormCveLoan.class);
```

Therefore:

> **`applicationFormCveLoanDao` itself is `null`.**

Your `appSeqId = 14050` is reaching the service correctly.

---

# 1. What is happening

Your log proves this:

```text
OTP authentication successful
        ↓
AppSeqId = 14050
        ↓
CveLoanService.getApplicationFormCveLoanByAppSeqId(14050)
        ↓
applicationFormCveLoanDao.findById(...)
        ↓
applicationFormCveLoanDao == NULL
        ↓
NullPointerException
```

So **do not change**:

```java
SessionUtil.getPersonalLoanTypeSequenceId()
```

just because of this exception.

The sequence ID is successfully reaching the service:

```text
CVE Application Sequence ID = 14050
```

The failure occurs before Hibernate even executes the query.

---

# 2. Exact problem in your `CveLoanService`

You have:

```java
private ApplicationFormCveLoanDao applicationFormCveLoanDao;
```

but you only showed a setter for:

```java
public void setCveProductDao(...)
```

There is **no setter for `applicationFormCveLoanDao`** in the class you sent.

You have:

```java
private ApplicationFormCveLoanDao applicationFormCveLoanDao;
```

but it is never initialized.

Therefore:

```java
applicationFormCveLoanDao
```

is `null`.

---

# 3. Fix `CveLoanService`

Add this getter/setter to `CveLoanService`.

### Add:

```java
public ApplicationFormCveLoanDao getApplicationFormCveLoanDao() {
    return applicationFormCveLoanDao;
}

public void setApplicationFormCveLoanDao(
        ApplicationFormCveLoanDao applicationFormCveLoanDao) {

    this.applicationFormCveLoanDao = applicationFormCveLoanDao;
}
```

So your service should contain:

```java
public class CveLoanService {

    private static final Logger logger =
            LogManager.getLogger(CveLoanService.class.getName());

    private ApplicationFormCveLoanDao applicationFormCveLoanDao;

    private MasterCveProductDao cveProductDao;


    public ApplicationFormCveLoan getApplicationFormCveLoanByAppSeqId(
            Integer appSeqId)
            throws SQLException, NoResultException {

        if (appSeqId == null) {

            logger.info(
                    "CveLoanService :: AppSeqId is NULL");

            return null;
        }

        logger.info(
                "CveLoanService :: Fetching ApplicationFormCveLoan "
                + "for AppSeqId = " + appSeqId);

        if (applicationFormCveLoanDao == null) {

            logger.error(
                    "CveLoanService :: applicationFormCveLoanDao is NULL");

            throw new IllegalStateException(
                    "ApplicationFormCveLoanDao is not configured in CveLoanService");
        }

        ApplicationFormCveLoan application =
                applicationFormCveLoanDao.findById(
                        appSeqId,
                        ApplicationFormCveLoan.class);

        if (application == null) {

            logger.info(
                    "CveLoanService :: ApplicationFormCveLoan "
                    + "not found for AppSeqId = " + appSeqId);

            return null;
        }

        logger.info(
                "CveLoanService :: ApplicationFormCveLoan found "
                + "for AppSeqId = " + application.getAppSeqId());

        return application;
    }


    public ApplicationFormCveLoanDao getApplicationFormCveLoanDao() {
        return applicationFormCveLoanDao;
    }

    public void setApplicationFormCveLoanDao(
            ApplicationFormCveLoanDao applicationFormCveLoanDao) {

        this.applicationFormCveLoanDao = applicationFormCveLoanDao;
    }


    public MasterCveProductDao getCveProductDao() {
        return cveProductDao;
    }

    public void setCveProductDao(MasterCveProductDao cveProductDao) {
        this.cveProductDao = cveProductDao;
    }


    public ApplicationFormCveLoan save(
            ApplicationFormCveLoan applicationFormCveLoan)
            throws SQLException {

        applicationFormCveLoan.setAppLeadUpdateTime(new Date());

        return applicationFormCveLoanDao.save(
                applicationFormCveLoan.getAppSeqId(),
                applicationFormCveLoan);
    }
}
```

---

# 4. But adding the setter alone is NOT enough

This is the most important part.

Your project appears to use **Spring XML bean configuration**, because your `CveLoanService` doesn't have:

```java
@Service
```

or:

```java
@Autowired
```

on `applicationFormCveLoanDao`.

So you need to check your Spring configuration where `CveLoanService` is declared.

You probably have something similar to:

```xml
<bean id="cveLoanService"
      class="com.mintstreet.loan.cveloan.service.CveLoanService">
    
    <property name="cveProductDao"
              ref="masterCveProductDao"/>

</bean>
```

If so, it is missing:

```xml
<property name="applicationFormCveLoanDao"
          ref="applicationFormCveLoanDao"/>
```

---

# 5. Add DAO injection in Spring XML

Find the bean:

```xml
<bean id="cveLoanService"
      class="com.mintstreet.loan.cveloan.service.CveLoanService">
```

Then add:

```xml
<property name="applicationFormCveLoanDao"
          ref="applicationFormCveLoanDao"/>
```

For example:

```xml
<bean id="cveLoanService"
      class="com.mintstreet.loan.cveloan.service.CveLoanService">

    <property name="applicationFormCveLoanDao"
              ref="applicationFormCveLoanDao"/>

    <property name="cveProductDao"
              ref="cveProductDao"/>

</bean>
```

**The `ref` must exactly match the bean ID of your `ApplicationFormCveLoanDao`.**

---

# 6. Check the DAO bean name

Your DAO class is:

```java
ApplicationFormCveLoanDao
```

You need to find how it is configured.

It might be:

```xml
<bean id="applicationFormCveLoanDao"
      class="com.mintstreet.loan.cveloan.dao.ApplicationFormCveLoanDao">
```

If that is the actual bean ID, then use:

```xml
<property name="applicationFormCveLoanDao"
          ref="applicationFormCveLoanDao"/>
```

If your project has another bean ID, for example:

```xml
<bean id="applicationFormCveLoanDaoImpl"
```

then use:

```xml
<property name="applicationFormCveLoanDao"
          ref="applicationFormCveLoanDaoImpl"/>
```

**Do not invent the ref name. Use the actual bean ID from your Spring configuration.**

---

# 7. Why your previous code appeared to work with the service

This:

```java
@Autowired
private CveLoanService cveLoanService;
```

in `CveLoanAction` successfully creates/injects the `CveLoanService`.

That's why this works:

```java
cveLoanService.getApplicationFormCveLoanByAppSeqId(14050);
```

But inside that service:

```java
applicationFormCveLoanDao
```

was never injected.

So you have:

```text
CveLoanAction
      |
      | @Autowired
      ↓
CveLoanService        ← injected correctly
      |
      ↓
ApplicationFormCveLoanDao ← NOT injected
      |
      ↓
NULL
```

That's exactly what your stack trace is showing.

---

# 8. Your CVE Action code is now reaching the correct location

Your log:

```text
OTP authentication successful
```

then:

```text
CVE Application Sequence ID = 14050
```

then:

```text
Exception while writing CVE consent to CCMS
```

This means your placement of CCMS processing **inside successful OTP processing is correct**.

The failure is happening here:

```java
cveAppForm =
        cveLoanService.getApplicationFormCveLoanByAppSeqId(
                cveAppSeqId);
```

before:

```java
writePrivacyConsentToCCMS(cveAppForm);
```

So **CCMS has not even been called yet**.

---

# 9. Do NOT change your CCMS code to solve this exception

Don't change:

```java
writePrivacyConsentToCCMS()
```

yet.

Don't change:

```java
callCCMSConsentWriteAPI()
```

yet.

Don't change:

```java
SessionUtil.getPersonalLoanTypeSequenceId()
```

yet.

First fix:

```text
ApplicationFormCveLoanDao injection
```

---

# 10. Also verify `ApplicationFormCveLoanDao`

Your DAO needs to be a valid Spring bean.

It should be something along the lines of your project's existing DAO pattern:

```java
public class ApplicationFormCveLoanDao extends BaseDao {

    // existing methods
}
```

or whatever base DAO your project uses.

The important thing is that Spring creates it.

If it is XML configured, make sure there is a bean for it.

If it uses component scanning, it needs the appropriate Spring annotation, for example:

```java
@Repository
public class ApplicationFormCveLoanDao ...
```

But **don't add `@Repository` blindly** if your existing project uses XML DAO configuration. Follow the same pattern used by your other working DAOs.

---

# 11. Your `save()` will have the same problem

This is important.

After you fix the fetch, your CCMS code eventually executes:

```java
cveLoanService.save(application);
```

Your save method does:

```java
return applicationFormCveLoanDao.save(
        applicationFormCveLoan.getAppSeqId(),
        applicationFormCveLoan);
```

If `applicationFormCveLoanDao` remains null, you'll get another NPE there.

So fixing the DAO injection fixes **both**:

```java
getApplicationFormCveLoanByAppSeqId()
```

and:

```java
save()
```

---

# 12. Expected logs after the fix

After adding the DAO injection, you should see something like:

```text
OTP authentication successful

CveLoanAction.java :: CVE Application Sequence ID = 14050

CveLoanService :: Fetching ApplicationFormCveLoan for AppSeqId = 14050

CveLoanService :: ApplicationFormCveLoan found for AppSeqId = 14050

CveLoanAction.java :: ApplicationFormCveLoan found. AppSeqId = 14050

CveLoanAction.java :: Calling CCMS consent write API

...

CveLoanAction.java :: CCMS consent successfully written

CveLoanService :: saving ApplicationFormCveLoan

...

CveLoanAction.java :: CVE consent successfully written
```

If instead you get:

```text
CveLoanService :: ApplicationFormCveLoan NOT FOUND for AppSeqId = 14050
```

then **only after fixing the DAO injection** do we investigate whether `14050` exists in:

```sql
RUPEEPOWER_OCAS_T_00291
```

---

## Final exact changes

### Change 1 — `CveLoanService.java`

Add:

```java
public ApplicationFormCveLoanDao getApplicationFormCveLoanDao() {
    return applicationFormCveLoanDao;
}

public void setApplicationFormCveLoanDao(
        ApplicationFormCveLoanDao applicationFormCveLoanDao) {
    this.applicationFormCveLoanDao = applicationFormCveLoanDao;
}
```

### Change 2 — Spring XML

Find the `cveLoanService` bean and add:

```xml
<property name="applicationFormCveLoanDao"
          ref="YOUR_ACTUAL_APPLICATION_FORM_CVE_LOAN_DAO_BEAN_ID"/>
```

For example, if the DAO bean is actually named `applicationFormCveLoanDao`:

```xml
<bean id="cveLoanService"
      class="com.mintstreet.loan.cveloan.service.CveLoanService">

    <property name="applicationFormCveLoanDao"
              ref="applicationFormCveLoanDao"/>

    <property name="cveProductDao"
              ref="cveProductDao"/>

</bean>
```

### Change 3 — Keep your Action flow

Keep:

```java
if (json.get("status").toString().equalsIgnoreCase("success")) {
```

and inside state `28`:

```java
Integer cveAppSeqId =
        SessionUtil.getPersonalLoanTypeSequenceId();

cveAppForm =
        cveLoanService.getApplicationFormCveLoanByAppSeqId(
                cveAppSeqId);

boolean ccmsWriteStatus =
        writePrivacyConsentToCCMS(cveAppForm);
```

### Root cause

**`14050` is not the cause of this particular exception. `applicationFormCveLoanDao` is null.**

If you send me your **`ApplicationFormCveLoanDao.java` and the Spring XML where `cveLoanService` is configured**, I can give you the exact `ref`/bean configuration to paste in, without guessing the bean name.
