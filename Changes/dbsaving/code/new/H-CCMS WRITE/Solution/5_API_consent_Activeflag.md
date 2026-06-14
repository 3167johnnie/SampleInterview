Yes. In common code, derive:

```java
boolean consented = "Y".equalsIgnoreCase(input.getConsentFlag());
```

So:

```text
Y       → true
N/null/blank → false
```

## 1) `ConsentWriteInput.java`

Add field:

```java
private String consentFlag;
```

Getter/setter:

```java
public String getConsentFlag() {
	return consentFlag;
}

public void setConsentFlag(String consentFlag) {
	this.consentFlag = consentFlag;
}
```

---

## 2) `ConsentService.java`

Inside `generateConsentWriteRequest(ConsentWriteInput input)`, add:

```java
boolean consented =
	"Y".equalsIgnoreCase(input.getConsentFlag());
```

Then change both consents:

```java
consent1.setConsented(String.valueOf(consented));
```

```java
consent2.setConsented(String.valueOf(consented));
```

Final block:

```java
boolean consented =
	"Y".equalsIgnoreCase(input.getConsentFlag());

consent1.setPurposeCode("PURP-DEMOPURP-000068");
consent1.setPurposeVersion("1");
consent1.setConsented(String.valueOf(consented));

consentedProducts.add(input.getProductCode());

consent2.setPurposeCode("PURP-DEMOPURP-000071");
consent2.setPurposeVersion("1");
consent2.setConsented(String.valueOf(consented));
consent2.setConsentedProducts(consentedProducts);
```

If `ConsentRequestConsent.consented` is `Boolean`, use:

```java
consent1.setConsented(consented);
consent2.setConsented(consented);
```

But in your existing code you were using:

```java
consent1.setConsented("true");
```

so keep:

```java
String.valueOf(consented)
```

---

## 3) `HomeProcessManagerImpl.java`

When creating common input:

```java
ConsentWriteInput consentInput = new ConsentWriteInput();

consentInput.setNtbId(quote.getQuoteNtbId());
consentInput.setMobile(application.getAppMobileNo());
consentInput.setEmail(application.getAppWorkEmail());
consentInput.setIpAddress(this.SbiUtil.getIPAddress());
consentInput.setLocale(quote.getQuotePrivacyLocale());
consentInput.setProductCode("HOME_LOAN");
consentInput.setConsentFlag(quote.getQuotePrivacyConsentFlag());
```

For other loans:

```java
consentInput.setConsentFlag(autoQuote.getQuotePrivacyConsentFlag());
```

or

```java
consentInput.setConsentFlag(personalQuote.getQuotePrivacyConsentFlag());
```

That will generate:

```json
"consented": true
```

only when flag is `Y`.
