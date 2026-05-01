Alright John—here’s a **clean, no-confusion, step-by-step guide** to create a **working Struts 2 + JPA Maven Web Project in Eclipse** for your Consent API.

I’ll assume **fresh start** and show exactly what to click, create, and paste.

---

# 🚀 STEP 1: Create Maven Web Project

### 👉 In Eclipse:

```text
File → New → Maven Project
```

✔ Tick:

```text
☑ Create a simple project (skip archetype selection)   ❌ (DON’T tick this)
```

Click **Next**

---

### 👉 Select Archetype:

Search:

```text
maven-archetype-webapp
```

Select it → **Next**

---

### 👉 Enter Details:

```text
Group Id     : com.sbi
Artifact Id  : consent-api
Version      : 1.0-SNAPSHOT
Packaging    : war
```

Click **Finish**

---

# 📁 STEP 2: Fix Project Structure (IMPORTANT)

After creation, convert to proper Maven structure:

---

## 👉 Right click project:

```text
Build Path → Configure Build Path → Source tab
```

Add:

```text
src/main/java
src/main/resources
```

---

## 👉 Create folders manually:

```text
src/main/java
src/main/resources
```

---

# 📦 STEP 3: Add Dependencies (pom.xml)

Open `pom.xml` → Replace with:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sbi</groupId>
    <artifactId>consent-api</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <dependencies>

        <!-- Struts 2 -->
        <dependency>
            <groupId>org.apache.struts</groupId>
            <artifactId>struts2-core</artifactId>
            <version>2.5.33</version>
        </dependency>

        <dependency>
            <groupId>org.apache.struts</groupId>
            <artifactId>struts2-json-plugin</artifactId>
            <version>2.5.33</version>
        </dependency>

        <!-- Servlet -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>

        <!-- JPA -->
        <dependency>
            <groupId>javax.persistence</groupId>
            <artifactId>javax.persistence-api</artifactId>
            <version>2.2</version>
        </dependency>

        <!-- Hibernate -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.6.15.Final</version>
        </dependency>

        <!-- MySQL -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>

        <!-- Jackson -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.15.2</version>
        </dependency>

    </dependencies>

    <build>
        <finalName>consent-api</finalName>
    </build>

</project>
```

---

# ⚙️ STEP 4: Configure web.xml

📍 Path:

```text
src/main/webapp/WEB-INF/web.xml
```

Replace with:

```xml
<web-app>

    <filter>
        <filter-name>struts2</filter-name>
        <filter-class>
            org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter
        </filter-class>
    </filter>

    <filter-mapping>
        <filter-name>struts2</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

</web-app>
```

---

# ⚙️ STEP 5: Create struts.xml

📍 Path:

```text
src/main/resources/struts.xml
```

```xml
<struts>

  <package name="default" namespace="/" extends="json-default">

    <action name="consentApi" class="com.sbi.action.ConsentAction">
        <result type="json"/>
    </action>

  </package>

</struts>
```

---

# 📁 STEP 6: Create Package Structure

Inside `src/main/java`:

```text
com.sbi
├── action
├── service
├── model
```

---

# 🧱 STEP 7: Add Model Classes

👉 Add all these:

* `ConsentRequest`
* `Headers`
* `Body`
* `DPData`
* `Consent`

(Use the exact code I gave earlier)

---

# 🔥 STEP 8: Add Action Class

📍 `com.sbi.action.ConsentAction`

👉 Paste earlier Action code

---

# 🌐 STEP 9: Add Service Class

📍 `com.sbi.service.ConsentService`

👉 Paste earlier Service code

---

# 🗄️ STEP 10: Add JPA Config (Optional but REQUIRED if DB)

📍 Create:

```text
src/main/resources/META-INF/persistence.xml
```

```xml
<persistence>
  <persistence-unit name="myPU">

    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <properties>
      <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/test"/>
      <property name="javax.persistence.jdbc.user" value="root"/>
      <property name="javax.persistence.jdbc.password" value="root"/>
      <property name="hibernate.hbm2ddl.auto" value="update"/>
    </properties>

  </persistence-unit>
</persistence>
```

---

# ▶️ STEP 11: Run Project

---

## 👉 Right click project:

```text
Run As → Run on Server
```

Choose:

```text
Apache Tomcat
```

---

# 🧪 STEP 12: Test API

---

### URL:

```text
http://localhost:8080/consent-api/consentApi.action
```

---

### Method:

```text
POST
```

---

### Body:

👉 `x-www-form-urlencoded`

Key:

```text
requestJson
```

Value:
👉 Paste your JSON

---

# 🚨 FINAL IMPORTANT NOTES

---

## ❗ 1. Struts does NOT accept raw JSON body

👉 Always use:

```text
requestJson
```

---

## ❗ 2. JSON Plugin is MUST

Without:

```xml
struts2-json-plugin
```

👉 It won’t work

---

## ❗ 3. URL pattern

Always:

```text
/actionName.action
```

---

# 🎯 DONE

You now have:

* ✅ Maven Web Project
* ✅ Struts 2 API
* ✅ JSON request handling
* ✅ Third-party integration
* ✅ JPA ready setup

---

# 🚀 If You Want Next

I can help you:

* Debug if it fails
* Add DB logging (JPA)
* Convert to Spring Boot (modern)

Just say 👍
