
}	



2026-06-24 20:33:10,645 INFO  (RequestHandler.java:51) - RequestHandler.java LNo : 49 ::: SessionUtil.getHomeLoanApplicationSequenceId() is:: null
2026-06-24 20:33:10,646 INFO  (RequestHandler.java:54) - RequestHandler.java LNo : 51 ::: alternateNumber is:: true
2026-06-24 20:33:10,658 INFO  (StateManager.java:539) - quote HL :: com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote@4c021642
2026-06-24 20:33:10,676 INFO  (ValidatorManager.java:424) - validatorResponse isStatus : true
2026-06-24 20:33:10,676 INFO  (ValidatorManager.java:425) - validatorResponse getErrorMessage : null
2026-06-24 20:33:10,677 INFO  (SbiUtil.java:2952) - SbiUtil.java LNo : 2158 : getSessionId() A0310D87629FBF68329C2AA093ADDE0B
2026-06-24 20:33:10,677 INFO  (HomeLoanAction.java:427) - HomeLoanAction.java LN 435 get state getHomeLoan() 1
2026-06-24 20:33:10,677 INFO  (HomeLoanAction.java:432) - HomeLoanAction.java LN 401 session's visit id is 3915678
2026-06-24 20:33:10,678 INFO  (HomeLoanAction.java:520) - stateManagerBean value check :: 1
2026-06-24 20:33:10,678 INFO  (HomeLoanAction.java:1049) - stateManagerBean.getState():::1
2026-06-24 20:33:10,678 INFO  (HomeLoanAction.java:1050) - session Util check():::null
2026-06-24 20:33:10,678 INFO  (HomeLoanAction.java:1056) - 969 state is :::1
2026-06-24 20:33:10,679 INFO  (HomeLoanAction.java:2197) - cheking the altIsd
2026-06-24 20:33:10,691 INFO  (HomeLoanHelper.java:2020) - quote.getLoanQuoteMonthCompletionDate()::12
2026-06-24 20:33:10,691 INFO  (HomeLoanHelper.java:2021) - quote.getLoanQuoteYearCompletionDate()::2026
2026-06-24 20:33:10,691 INFO  (HomeLoanHelper.java:2024) - month::12
2026-06-24 20:33:10,691 INFO  (HomeLoanHelper.java:2026) - stdate::01-12-2026
2026-06-24 20:33:10,695 INFO  (HomeLoanHelper.java:2030) - endDay::2026-12-31
2026-06-24 20:33:10,696 INFO  (HomeLoanHelper.java:2032) - formattedDate::31-12-2026
2026-06-24 20:33:10,697 INFO  (HomeLoanHelper.java:2034) - newdate::Thu Dec 31 23:59:00 IST 2026
2026-06-24 20:33:10,697 INFO  (HomeLoanHelper.java:2036) - NewValue::2026-12-31T23:59:00
2026-06-24 20:33:10,697 INFO  (HomeLoanHelper.java:2040) - New Loan Quote Completion Date For Loan Category ID 2 & 4 :::::::::::::::Thu Dec 31 00:00:00 IST 2026
2026-06-24 20:33:10,701 INFO  (HomeLoanHelper.java:200) - bankLmsUserId:: 9999999::: and quote consentId is:: null
2026-06-24 20:33:10,730 INFO  (HomeLoanHelper.java:671) -  cheking the value alternate mobile number inside the helper class  null
2026-06-24 20:33:10,762 INFO  (CcmsUtil.java:28) - callingEISServiceForCcms start...
2026-06-24 20:33:10,764 INFO  (PanApiRSAEncryption.java:52) - PanApiRSAEncryption ( ) CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH : :  /srv/sbiroot/support_lms/documents/certificates/RenewEIS_certificate/EIS_ENC_UAT.cer
2026-06-24 20:33:10,806 INFO  (PanApiRSAEncryption.java:114) - RSA Encrypted Data Xe5j87cPZ0ZsMYwixBgBdzUezzzj2JZuw5H2N88Ve4p1C+dj6aDlGPtRbN267dWJc9PJnqHMhpVkDEa46jAlvyRML85KROjib9LH/r5MA5hvuUZaA7VEutZG8LQGlHHILDvG4MyhQMKDNsrD8DohnOiTVbeL5xwpdzhgXYPHFyEovpf1u3x4N90PUYkDr71YKq1C3Y//fHuX8bMNdm3OHrii0dt7kmn2IvfEGkuHHrkT7z03UUwqKz5duAxU84tH5aEfdyLiYBuYDDe1NUzGNMItmKjHCEXHcO+NtzlLsXNkunt3UyQ9Vy1Larh/QkeIO3b9L86FCYYOvq1Mh4USaQ==
2026-06-24 20:33:16,539 INFO  (CcmsUtil.java:80) - CCMS request: {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019efa28-1297-778c-9478-600d4cdac8c3","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","consents":[{"purposeVersion":"1","purposeCode":"PR-FACILITA-000099","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-REGULATO-000100","consented":"true"},{"purposeVersion":"2","purposeCode":"PR-INCIDENT-000101","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-MANAGEYO-000102","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-SERVICEI-000077","consented":"true"},{"purposeVersion":"2","purposeCode":"PR-ANALYTIC-000105","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-PROMOTIO-000104","consented":"true"},{"purposeVersion":"3","purposeCode":"PR-PROMOTIO-000106","consented":"true"},{"purposeVersion":"2","purposeCode":"PR-USERPROF-000118","consented":"true"}],"dpData":{"dpMobile":"9619155147","dpCIF":"","ntbId":"9619155147030620081","dpIPAddress":"10.0.117.103","locale":"eng","dpEmail":"jhnjacob300@gmail.com","timestamp":"2026-06-24T20:33:10+05:30"},"touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"UPDATE_CONSENTS"}
2026-06-24 20:33:16,540 INFO  (CcmsUtil.java:81) - Connection Response Code : 200
2026-06-24 20:33:16,545 INFO  (CcmsUtil.java:105) - eisEncryptedResponseStr : {"DIGI_SIGN":"PNQc6Vaj1nl9jZ1EkxM3Jcer73g2RhCJIUVslDdtq35TpuMqLFddXj6BJc6TXdc02BYsQBf6WucXbw3l1ruFwCXqQHRlmJj60dp4dQfzob+UIvB5SXCJ8KRzhjy9QqewR/0ouT1RinQptepPulzVntXi/UjMjyt6M8mxF2RaIPSYZXdEsmA8BOHY82zoPWngGvmRu6vjtCdDRqxek1FTthusO3ZLWvkR8Vp00m9TqH+1Q7UU/LflzwceWxDZToyWgtT1Egn2ffsDn70Bqqkoy1JlzG0Y11l4+1ZT0rRJwzGSjlME9vEssEquokwD8aH3BD40YVeugPF41N1lIWxy/Q==","RESPONSE":"CfHfvBcckm5SRcp1NAU11uZG+EXa7wm00/v/op/jk4wsRi4WvcL5bL7Kb9ZJYUD4WdFWDiKvA29waH2LhVIq3LI7Rc+xeZbaUqazfQCoDeIj+hFJgb2gH4HjAnZTQlHV6LkCRoRP00K53BA1eeW7Mim1ImshIe0gDQhHr4NEE4MWMiYfkMghqocjn0X+0l9tcKHktxNdcRCVPH3AXVwrgSj7/YRi4uUNBLDalQduZTGwcLwDF/6n8ztoDpiVoWYoY56WeohaJI4Tyd+4p332qwuFW11DHVPITYPZnCiRuYdeey2ZXoikJcl5AXOjDtb4Dsxxb4rPRZUxH6e/zd06r1JtGnsiwnV7JgvKZhgflYQ/TIJDHUV1hbLUl5O3a8H+zmw1EswKuG/h0+LnLLx6ZSFz+MJlhvCA3odCYB+1NlzxmoyhoHaf8uLFJF78w7aWNUsypdgcQKtF4kfsE/ihWy/fZhROCtTSIervwRK4EBjLL3cSXlC8FOyUPozflbiMol9popnAOhLuMId8V6mJHQPFMn/Bztl0sODV7SH4mE2LuIFOxr1CBJ3kyhYDnOQfE2UCl7+P/DIgSd3UUDhhRN7vZb6ETSiANBcVl9Pi54ZwUC1vNT1pGi1Rr/WXppW/iCWxqUIQpFpIX/Bi7tmvi+UxnAjMkyfTl8yQgdoDVnxWooXW6Z8Zys9FBQFHljWMTF+fV9/DBgy9K67PQWm1CGS4W/1ydytfWQU8APIm+ZOPX+n1HD3rMl8cxpzDjQt7H/yFTulUT19rNxKXCm43wtQfe+p8C21EhlmwY3RH3RlMJC0TRNSU/NKgCq+J/jKhI/xj5/BNhT2Yb2SosJ8=","REQUEST_REFERENCE_NUMBER":"SBIOC20261750000010498513","RESPONSE_DATE":"24-06-2026 20:33:16"}
2026-06-24 20:33:16,546 INFO  (CcmsUtil.java:118) - ERROR_DESCRIPTION: {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":false,"messages":[],"correlationId":"019efa28-1297-778c-9478-600d4cdac8c3","locale":"eng","body":null,"errors":{"global":["Validation failed for consent list:\n - Consent[2] (purposeCode=PR-INCIDENT-000101): Missing product codes: HOME_LOAN, CAR_LOAN, PERSONAL_LOAN\n - Consent[7] (purposeCode=PR-PROMOTIO-000106): Missing product codes: CREDIT_CARD, WILL_TRUSTEESHIP_SERVICES, DEMAT_AND_SECURITIES, MUTUAL_FUND_AND_ASSET_MANAGEMENT, GENERAL_INSURANCE, LIFE_INSURANCE"]},"statusCode":400,"timestamp":"2026-06-24T15:03:16.484802351Z"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}
2026-06-24 20:33:16,546 INFO  (CcmsUtil.java:120) - ERROR_DESCRIPTION:

{
  "EIS_PAYLOAD": {
    "HEADERS": {
      "X-Correlation-Id": "019efa28-1297-778c-9478-600d4cdac8c3",
      "Accept-Language": "eng",
      "X-API-Version": "1"
    },
    "BODY": {
      "purposeSetId": "PS-CIFJOURN-000091",
      "consents": [
        {
          "purposeVersion": "1",
          "purposeCode": "PR-FACILITA-000099",
          "consented": "true"
        },
        {
          "purposeVersion": "1",
          "purposeCode": "PR-REGULATO-000100",
          "consented": "true"
        },
        {
          "purposeVersion": "2",
          "purposeCode": "PR-INCIDENT-000101",
          "consented": "true"
        },
        {
          "purposeVersion": "1",
          "purposeCode": "PR-MANAGEYO-000102",
          "consented": "true"
        },
        {
          "purposeVersion": "1",
          "purposeCode": "PR-SERVICEI-000077",
          "consented": "true"
        },
        {
          "purposeVersion": "2",
          "purposeCode": "PR-ANALYTIC-000105",
          "consented": "true"
        },
        {
          "purposeVersion": "1",
          "purposeCode": "PR-PROMOTIO-000104",
          "consented": "true"
        },
        {
          "purposeVersion": "3",
          "purposeCode": "PR-PROMOTIO-000106",
          "consented": "true"
        },
        {
          "purposeVersion": "2",
          "purposeCode": "PR-USERPROF-000118",
          "consented": "true"
        }
      ],
      "dpData": {
        "dpMobile": "9619155147",
        "dpCIF": "",
        "ntbId": "9619155147030620081",
        "dpIPAddress": "10.0.117.103",
        "locale": "eng",
        "dpEmail": "jhnjacob300@gmail.com",
        "timestamp": "2026-06-24T20:33:10+05:30"
      },
      "touchPointId": "OCAS000001"
    }
  },
  "SOURCE_ID": "OC",
  "DESTINATION": "CRM",
  "TXN_TYPE": "CCMS",
  "TXN_SUB_TYPE": "UPDATE_CONSENTS"
}





{
  "RESPONSE_STATUS": "0",
  "EIS_RESPONSE": {
    "success": false,
    "messages": [],
    "correlationId": "019efa28-1297-778c-9478-600d4cdac8c3",
    "locale": "eng",
    "body": null,
    "errors": {
      "global": [
        "Validation failed for consent list:\n - Consent[2] (purposeCode=PR-INCIDENT-000101): Missing product codes: HOME_LOAN, CAR_LOAN, PERSONAL_LOAN\n - 
Consent[7] (purposeCode=PR-PROMOTIO-000106): Missing product codes: CREDIT_CARD, WILL_TRUSTEESHIP_SERVICES, DEMAT_AND_SECURITIES, MUTUAL_FUND_AND_ASSET_MANAGEMENT, GENERAL_INSURANCE, LIFE_INSURANCE"
      ]
    },
    "statusCode": 400,
    "timestamp": "2026-06-24T15:03:16.484802351Z"
  },
  "ERROR_DESCRIPTION": "",
  "ERROR_CODE": ""
}












