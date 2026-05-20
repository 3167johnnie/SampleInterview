Yes — after analyzing your `applicationContext.xml`, you DEFINITELY need changes there. 

Your project is using:

* XML bean configuration
* manual DAO wiring
* setter injection

So for your new:

```java
PrivacyRequestResponseDao
```

you MUST add bean entries.

---

# REQUIRED CHANGES IN applicationContext.xml

You need ONLY 2 changes.

---

# 1. ADD DAO BEAN

Search this section:

```xml
<bean id="privacyNoticeDao"
	class="com.mintstreet.consent.dao.PrivacyNoticeDao">
```

ADD BELOW IT:

```xml
<bean id="privacyRequestResponseDao"
	class="com.mintstreet.consent.dao.PrivacyRequestResponseDao">

	<property name="entityManagerFactory"
		ref="entityManagerFactory" />

</bean>
```

---

# FINAL LOOK

```xml
<bean id="privacyNoticeDao"
	class="com.mintstreet.consent.dao.PrivacyNoticeDao">

	<property name="entityManagerFactory"
		ref="entityManagerFactory" />

</bean>

<bean id="privacyRequestResponseDao"
	class="com.mintstreet.consent.dao.PrivacyRequestResponseDao">

	<property name="entityManagerFactory"
		ref="entityManagerFactory" />

</bean>
```

---

# 2. ADD PROPERTY IN commonService

Search:

```xml
<bean id="commonService"
	class="com.mintstreet.common.service.CommonService">
```

Inside this bean you already have:

```xml
<property name="privacyNoticeDao"
	ref="privacyNoticeDao"/>
```

ADD BELOW IT:

```xml
<property name="privacyRequestResponseDao"
	ref="privacyRequestResponseDao"/>
```

---

# FINAL LOOK

```xml
<property name="privacyNoticeDao"
	ref="privacyNoticeDao"/>

<property name="privacyRequestResponseDao"
	ref="privacyRequestResponseDao"/>
```

---

# IMPORTANT

You ALSO must add inside:

```java
CommonService.java
```

---

# VARIABLE

```java
private PrivacyRequestResponseDao privacyRequestResponseDao;
```

---

# GETTER SETTER

```java
public PrivacyRequestResponseDao
getPrivacyRequestResponseDao() {

	return privacyRequestResponseDao;
}

public void setPrivacyRequestResponseDao(
	PrivacyRequestResponseDao privacyRequestResponseDao) {

	this.privacyRequestResponseDao =
			privacyRequestResponseDao;
}
```

Otherwise Spring injection will fail.

---

# NO CHANGES REQUIRED IN

| File                | Required? |
| ------------------- | --------- |
| `web.xml`           | ❌ No      |
| `persistence.xml`   | ❌ No      |
| `hibernate.cfg.xml` | ❌ No      |
| `jdbc.properties`   | ❌ No      |

---

# STRUTS.XML

YES — still required.

You need action mapping:

```xml
<action
	name="getPrivacyNoticeByLocale"
	class="com.mintstreet.loan.homeloan.action.HomeLoanAction"
	method="getPrivacyNoticeByLocale">

	<result type="stream">
		<param name="contentType">
			application/json
		</param>
	</result>

</action>
```

---

# FINAL FILES YOU MUST CHANGE

| File                             | Change                     |
| -------------------------------- | -------------------------- |
| `PrivacyRequestResponse.java`    | NamedQuery                 |
| `PrivacyRequestResponseDao.java` | New file                   |
| `CommonService.java`             | Add DAO injection + method |
| `applicationContext.xml`         | Add bean + property        |
| `HomeLoanAction.java`            | AJAX method                |
| `struts.xml`                     | Action mapping             |
| `ConsentPopup.jsp`               | Frontend dropdown + AJAX   |

---

# MOST IMPORTANT

Because your project already uses:

```xml
<context:component-scan base-package="com.mintstreet" />
```

you may THINK bean config unnecessary.

BUT your DAOs are explicitly wired manually:

```xml
<bean id="consentDao" ... />
```

So FOLLOW SAME PATTERN.

That is safest for production consistency.
