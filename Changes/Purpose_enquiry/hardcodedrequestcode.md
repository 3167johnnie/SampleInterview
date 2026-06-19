For debugging, you can completely bypass the DTO creation and directly hardcode the Purpose Enquiry request JSON.

### Option 1 (Recommended) - Hardcode `JSONObject`

Inside your `ConsentService.java`:

```java
JSONObject requestJson = new JSONObject();

requestJson.put("SOURCE_ID", "OC");

JSONObject eisPayload = new JSONObject();

JSONObject headers = new JSONObject();
headers.put("Accept-Language", "eng");
headers.put("X-Correlation-Id", "f2bcebd5-9058-4d04-95ad-b9f9f76b5d58");
headers.put("X-API-Version", "1");

JSONObject body = new JSONObject();
body.put("touchPointId", "SBIOCAS");
body.put("purposeSetId", "TESTEISPS1");

eisPayload.put("HEADERS", headers);
eisPayload.put("BODY", body);

requestJson.put("EIS_PAYLOAD", eisPayload);
requestJson.put("DESTINATION", "CRM");
requestJson.put("TXN_TYPE", "CCMS");
requestJson.put("TXN_SUB_TYPE", "PURPOSE_ENQUIRY");

logger.info("Purpose Enquiry Request : " + requestJson.toString());

JSONObject response =
        ccmsUtil.callingEISServiceForPurposeEnquiry(
                requestJson.toString());

logger.info("Purpose Enquiry Response : " + response.toString());
```

---

## Option 2 - Hardcode String (Fastest)

If you only want to test the API call, simply replace the request generation with:

```java
String request = "{"
        + "\"SOURCE_ID\":\"OC\","
        + "\"EIS_PAYLOAD\":{"
        + "\"HEADERS\":{"
        + "\"Accept-Language\":\"eng\","
        + "\"X-Correlation-Id\":\"f2bcebd5-9058-4d04-95ad-b9f9f76b5d58\","
        + "\"X-API-Version\":\"1\""
        + "},"
        + "\"BODY\":{"
        + "\"touchPointId\":\"SBIOCAS\","
        + "\"purposeSetId\":\"TESTEISPS1\""
        + "}"
        + "},"
        + "\"DESTINATION\":\"CRM\","
        + "\"TXN_TYPE\":\"CCMS\","
        + "\"TXN_SUB_TYPE\":\"PURPOSE_ENQUIRY\""
        + "}";

logger.info("Purpose Enquiry Request : " + request);

JSONObject response =
        ccmsUtil.callingEISServiceForPurposeEnquiry(request);

logger.info("Purpose Enquiry Response : " + response.toString());
```

---

## The actual JSON being sent

```json
{
  "SOURCE_ID": "OC",
  "EIS_PAYLOAD": {
    "HEADERS": {
      "Accept-Language": "eng",
      "X-Correlation-Id": "f2bcebd5-9058-4d04-95ad-b9f9f76b5d58",
      "X-API-Version": "1"
    },
    "BODY": {
      "touchPointId": "SBIOCAS",
      "purposeSetId": "TESTEISPS1"
    }
  },
  "DESTINATION": "CRM",
  "TXN_TYPE": "CCMS",
  "TXN_SUB_TYPE": "PURPOSE_ENQUIRY"
}
```

### Even simpler for debugging

If your `callingEISServiceForPurposeEnquiry()` only accepts a `String`, you can directly do:

```java
JSONObject response = ccmsUtil.callingEISServiceForPurposeEnquiry(
"{\"SOURCE_ID\":\"OC\","
+ "\"EIS_PAYLOAD\":{"
+ "\"HEADERS\":{"
+ "\"Accept-Language\":\"eng\","
+ "\"X-Correlation-Id\":\"123456789\","
+ "\"X-API-Version\":\"1\""
+ "},"
+ "\"BODY\":{"
+ "\"touchPointId\":\"SBIOCAS\","
+ "\"purposeSetId\":\"TESTEISPS1\""
+ "}"
+ "},"
+ "\"DESTINATION\":\"CRM\","
+ "\"TXN_TYPE\":\"CCMS\","
+ "\"TXN_SUB_TYPE\":\"PURPOSE_ENQUIRY\""
+ "}"
);
```

This completely avoids DTOs and lets you verify that the Purpose Enquiry API itself is working before implementing the dynamic request generation.
