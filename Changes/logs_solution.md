Issue is clear from log:

```text
could not locate named parameter [privacyIsActive]
```

Meaning: your DAO is passing this parameter:

```java
params.put("privacyIsActive", "Y");
```

but your **entity named query still does not have**:

```java
:privacyIsActive
```

Log confirms error is from `getPrivacyByLocale()` query. 

Fix this in `PrivacyRequestResponse.java`.

Replace:

```java
@NamedQuery(
	name="PrivacyRequestResponse.getPrivacyByLocale",
	query="SELECT p FROM PrivacyRequestResponse p "
		+ "WHERE p.privacyLocale=:privacyLocale"
),
```

With:

```java
@NamedQuery(
	name="PrivacyRequestResponse.getPrivacyByLocale",
	query="SELECT p FROM PrivacyRequestResponse p "
		+ "WHERE p.privacyLocale=:privacyLocale "
		+ "AND p.privacyIsActive=:privacyIsActive"
),
```

Also confirm this field exists in same entity:

```java
@Column(name="PRIVACY_IS_ACTIVE")
private String privacyIsActive;
```

with getter/setter:

```java
public String getPrivacyIsActive() {
	return privacyIsActive;
}

public void setPrivacyIsActive(String privacyIsActive) {
	this.privacyIsActive = privacyIsActive;
}
```

After change: **clean build + redeploy + restart Tomcat/JBoss**. Hibernate reads `@NamedQuery` at startup, so only changing file without restart may still use old query.
