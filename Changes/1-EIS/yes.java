    "timestamp": "2026-06-24T15:03:16.484802351Z"
  },
  "ERROR_DESCRIPTION": "",
  "ERROR_CODE": ""
}




2026-06-25 20:18:22,463 INFO  (RequestHandler.java:50) - RequestHandler.java LNo : 45 ::: substring is:: Y"
2026-06-25 20:18:22,464 INFO  (RequestHandler.java:51) - RequestHandler.java LNo : 49 ::: SessionUtil.getHomeLoanApplicationSequenceId() is:: null
2026-06-25 20:18:22,464 INFO  (RequestHandler.java:54) - RequestHandler.java LNo : 51 ::: alternateNumber is:: true
2026-06-25 20:18:22,476 INFO  (StateManager.java:539) - quote HL :: com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote@4ed433c0
2026-06-25 20:18:22,494 INFO  (ValidatorManager.java:424) - validatorResponse isStatus : true
2026-06-25 20:18:22,495 INFO  (ValidatorManager.java:425) - validatorResponse getErrorMessage : null
2026-06-25 20:18:22,495 INFO  (SbiUtil.java:2952) - SbiUtil.java LNo : 2158 : getSessionId() 7075078D0EEE1102D74E9EC329F746AE
2026-06-25 20:18:22,495 INFO  (HomeLoanAction.java:427) - HomeLoanAction.java LN 435 get state getHomeLoan() 1
2026-06-25 20:18:22,496 INFO  (HomeLoanAction.java:432) - HomeLoanAction.java LN 401 session's visit id is 3915699
2026-06-25 20:18:22,496 INFO  (HomeLoanAction.java:520) - stateManagerBean value check :: 1
2026-06-25 20:18:22,496 INFO  (HomeLoanAction.java:1049) - stateManagerBean.getState():::1
2026-06-25 20:18:22,497 INFO  (HomeLoanAction.java:1050) - session Util check():::null
2026-06-25 20:18:22,497 INFO  (HomeLoanAction.java:1056) - 969 state is :::1
2026-06-25 20:18:22,498 INFO  (HomeLoanAction.java:2197) - cheking the altIsd
2026-06-25 20:18:22,516 INFO  (HomeLoanHelper.java:2020) - quote.getLoanQuoteMonthCompletionDate()::2
2026-06-25 20:18:22,517 INFO  (HomeLoanHelper.java:2021) - quote.getLoanQuoteYearCompletionDate()::2027
2026-06-25 20:18:22,517 INFO  (HomeLoanHelper.java:2024) - month::02
2026-06-25 20:18:22,517 INFO  (HomeLoanHelper.java:2026) - stdate::01-02-2027
2026-06-25 20:18:22,521 INFO  (HomeLoanHelper.java:2030) - endDay::2027-02-28
2026-06-25 20:18:22,522 INFO  (HomeLoanHelper.java:2032) - formattedDate::28-02-2027
2026-06-25 20:18:22,522 INFO  (HomeLoanHelper.java:2034) - newdate::Sun Feb 28 23:59:00 IST 2027
2026-06-25 20:18:22,523 INFO  (HomeLoanHelper.java:2036) - NewValue::2027-02-28T23:59:00
2026-06-25 20:18:22,523 INFO  (HomeLoanHelper.java:2040) - New Loan Quote Completion Date For Loan Category ID 2 & 4 :::::::::::::::Sun Feb 28 00:00:00 IST 2027
2026-06-25 20:18:22,532 INFO  (HomeLoanHelper.java:200) - bankLmsUserId:: 9999999::: and quote consentId is:: null
2026-06-25 20:18:22,804 INFO  (HomeLoanHelper.java:671) -  cheking the value alternate mobile number inside the helper class  null
2026-06-25 20:18:23,105 INFO  (CcmsUtil.java:28) - callingEISServiceForCcms start...
2026-06-25 20:18:23,108 INFO  (PanApiRSAEncryption.java:52) - PanApiRSAEncryption ( ) CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH : :  /srv/sbiroot/support_lms/documents/certificates/RenewEIS_certificate/EIS_ENC_UAT.cer
2026-06-25 20:18:23,149 INFO  (PanApiRSAEncryption.java:114) - RSA Encrypted Data SaUtjmWy0IlCa5/PJiAgza8CxAd20GCME6nay2+RF/+ORDF6R3S8kio742szn+5BZMbm5AoXMdHJLVJ9EZrrE7QJ2tkXFshRvUmNH8v7uOYdZ94D2uI8rhwLkKvAPQ9d0mZqEXoD+0UonaArhfOflFLkFCbt2TxBLcr1aU3NMkSQ6lzh78BdL2ffIvLIHjKqg768viYF6ceJTUDB4lwQ0fh1dqWetM0qSLjMQsu0ilzVFB0oozC3Vt12Px7Zv3BfASofowRzBmyfCS3nccai+cyUZYpXlVflnZSq0MKd5A+huiJX8pyT017Tiw1zTqeFRMzHDm4PnZvPhR8PCSsPKA==
2026-06-25 20:18:28,753 INFO  (CcmsUtil.java:80) - CCMS request: {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019eff40-e2c3-7938-9bd7-abbf52932a34","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"READ_PURPOSES"}
2026-06-25 20:18:28,753 INFO  (CcmsUtil.java:81) - Connection Response Code : 200
2026-06-25 20:18:28,757 INFO  (CcmsUtil.java:105) - eisEncryptedResponseStr : {"DIGI_SIGN":"eLXuVuqNvZWvQubgzLuxLlc3c9WleOcDit4v1BVhrHp68pQHFpGd+4Zu3pctEG3nSt3m6iDjYtMA5XKa+Hmj6RF5eQ2x0J/gyJCAigfKyJOY9hWIZd+tCE0UGVquBWpoO8iSzx/mYStgWPf9a5nnqrdlQth7VrmPex3r5IBeHK9kPItQ6csufahOfFe59L6IMyqSTH08ZP9KVG0P8oqiJDaZpMmo0b9t/ZqJ0wMfPYvEDZGTNBfbXBMx76JBwacOmq+k1Ym996c4wCixl2P2Rrkip/na2lV33tCeV7W+gzIigS6NqYNNlh8MP7JLefvAskg+KeYZPNGqGYQKbH1wPA==","RESPONSE":"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","REQUEST_REFERENCE_NUMBER":"SBIOC20261760000016004656","RESPONSE_DATE":"25-06-2026 20:18:28"}
2026-06-25 20:18:28,761 INFO  (CcmsUtil.java:118) - ERROR_DESCRIPTION: {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":true,"messages":null,"correlationId":"019eff40-e2c3-7938-9bd7-abbf52932a34","locale":"eng","body":[{"containerDescription":"Essential Purposes","purposes":[{"hasValidity":false,"code":"PR-FACILITA-000099","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To enable use of products or applications/requests for any products, process such applications/requests, perform any contract pursuant thereto, and undertake specified purposes related to these.\n- To personalize and improve platform/app experience and user engagement.\n- To enable product sales, cross-sales, distribution, or referrals, for data received from third parties.\n- To enable the use of websites, platforms, mobile applications, and online services.\n- To share your information with our partners and service providers for providing banking facilities and services opted by you.\n- To allow you to participate in surveys, contests and to administer these activities\n","dataPoints":[],"isEssential":true,"title":"Facilitate usage of our products, services and applications","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-REGULATO-000100","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- For credit scoring, credit analysis, risk analysis, preventing fraud and money laundering, identity and screening checks, due diligence, inspections, and related purposes, including obtaining reports and information from credit bureaus, or service providers to fulfil the product or service requirement chosen by you. \n- To process my data to comply with regulatory and legal requirements.\n- To share information with relevant authorities, including regulators, Income Tax and other government authorities of India and overseas.\n- To access/share my KYC records/information with the Central KYC Registry (CKYCR), run credit checks on my behalf, obtain a credit report for the purpose of performing customer due diligence/credit decisions in relation to opening an account with SBI.","dataPoints":[],"isEssential":true,"title":"Regulatory Compliance, Risk Management, and Customer Due Diligence","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-INCIDENT-000101","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"HOME_LOAN","label":"Home Loan"},{"code":"CAR_LOAN","label":"Car Loan"},{"code":"PERSONAL_LOAN","label":"Personal Loan"}],"description":"- Where necessary, to protect your interests in emergency situations when consent cannot be obtained.","dataPoints":[],"isEssential":true,"title":"Incidental or Emergency Use","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-MANAGEYO-000102","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To manage banking relationships with you.\n- For debt collection and related services.\n- To contact you or establish your whereabouts for legitimate use.\n- To inform you about changes to terms, conditions, and policies, and to communicate other administrative information.\n- To monitor or record communications, including phone calls, for improving services.\n- To reach out for account management and keeping you informed about your products or services.\n- To record CCTV images/video when you visit SBI premises/ branches or ATMs for operational, legal and regulatory purposes.\n- To resolve complaints and enquiries.\n- To send messages, notifications or alerts by post, telephone, text, email, social media, POS machines and other digital methods, including for example via our ATMs, mobile applications, push notifications, or online banking services (and new methods that may become available in the future). This may be for: a) managing your accounts b) meeting our regulatory obligations","dataPoints":[],"isEssential":true,"title":"Manage your relationship with us","version":1,"validityDays":null,"isNTB":true},{"hasValidity":true,"code":"PR-SERVICEI-000077","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"For service or product development, audit, business continuity, planning, developing efficient solutions, and improving product delivery","dataPoints":["Mobile"],"isEssential":true,"title":"Service Improvement and Development","version":1,"validityDays":3600,"isNTB":true}],"containerTitle":"Essential Purposes","isContainer":true},{"containerDescription":"Optional Purposes","purposes":[{"hasValidity":false,"code":"PR-ANALYTIC-000105","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- Understand customer needs better by carrying out data analytics that help us design products, services and provide offers that suit you. Such as pre-approved loan etc. ","dataPoints":[],"isEssential":false,"title":"Analytics","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000104","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Share updates and offers \u2013 by informing you about bank\u2019s products, promotions, and services that may be of interest to you","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000106","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"CREDIT_CARD","label":"Credit Card"},{"code":"WILL_TRUSTEESHIP_SERVICES","label":"Will Trusteeship Services"},{"code":"DEMAT_AND_SECURITIES","label":"Demat and securities"},{"code":"MUTUAL_FUND_AND_ASSET_MANAGEMENT","label":"Mutual Fund / Asset Management"},{"code":"GENERAL_INSURANCE","label":"General Insurance"},{"code":"LIFE_INSURANCE","label":"Life Insurance"}],"description":"-Share updates and offers of the companies with whom SBI has entered into agreements - for providing suitable \u201cservices/products\u201d opted by you. ","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing - Companies with whom SBI has entered into agreements","version":3,"validityDays":null,"isNTB":true}],"containerTitle":"Optional Purposes","isContainer":true},{"containerDescription":"","purposes":[{"hasValidity":true,"code":"PR-USERPROF-000118","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Browsing and transaction activity history will be used to provide tailored recommendations and customized content.","dataPoints":["Income Details","Pincode"],"isEssential":false,"title":"User Profile Personalization","version":2,"validityDays":90,"isNTB":false}],"containerTitle":"","isContainer":false}],"errors":null,"statusCode":200,"timestamp":"2026-06-25T14:48:28.687932235"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}
2026-06-25 20:18:28,762 INFO  (CcmsUtil.java:120) - ERROR_DESCRIPTION:
2026-06-25 20:18:29,144 INFO  (CcmsUtil.java:28) - callingEISServiceForCcms start...
2026-06-25 20:18:29,144 INFO  (PanApiRSAEncryption.java:52) - PanApiRSAEncryption ( ) CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH : :  /srv/sbiroot/support_lms/documents/certificates/RenewEIS_certificate/EIS_ENC_UAT.cer
2026-06-25 20:18:29,154 INFO  (PanApiRSAEncryption.java:114) - RSA Encrypted Data f3XjuicAzWWfagEV2m2OvxTxFnK7YrfXdHcwBBTm0uyT9ha6eZeNA1R/6phZIkVPtk7etBqh3AgEGkPJiTZxY5gwdMrpkbX3AMC7wrXmqFwDzC9eZhbiGVBny5GHoGyEYOuxwBRdUpnJn7vQqIs51n9dl6lF1CPzEiXT8jDt87r1r3SLVNoCnAdgAi3Bse+tyDogZDmrKVUQ91PhuZmFdd62Xe5lEMKMFk03eQldUWEhv0ezG8OpIrt8VCnkLnFB1WPKWfj29D0ECcwCYYVMqjut8GT8xs0Lgn5U+eQyuOhP2YG32bdj2MMU3BqrLlXY/SZ05ASwi2fkBu+DTQQ4YQ==
2026-06-25 20:18:29,366 INFO  (CcmsUtil.java:80) - CCMS request: {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019eff40-e2c1-7029-8821-708269cc8646","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","consents":[{"purposeVersion":"1","purposeCode":"PR-FACILITA-000099","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-REGULATO-000100","consented":"true"},{"consentedProducts":["HOME_LOAN"],"purposeVersion":"2","purposeCode":"PR-INCIDENT-000101","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-MANAGEYO-000102","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-SERVICEI-000077","consented":"true"},{"purposeVersion":"2","purposeCode":"PR-ANALYTIC-000105","consented":"true"},{"purposeVersion":"1","purposeCode":"PR-PROMOTIO-000104","consented":"true"},{"consentedProducts":["HOME_LOAN"],"purposeVersion":"3","purposeCode":"PR-PROMOTIO-000106","consented":"true"},{"purposeVersion":"2","purposeCode":"PR-USERPROF-000118","consented":"true"}],"dpData":{"dpMobile":"9619155147","dpCIF":"","ntbId":"9619155147270520081","dpIPAddress":"10.0.117.103","locale":"eng","dpEmail":"UIeerere@gmail.com","timestamp":"2026-06-25T20:18:22+05:30"},"touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"UPDATE_CONSENTS"}
2026-06-25 20:18:29,366 INFO  (CcmsUtil.java:81) - Connection Response Code : 200
2026-06-25 20:18:29,367 INFO  (CcmsUtil.java:105) - eisEncryptedResponseStr : {"DIGI_SIGN":"amFRsMMkH2XqJmAxL3RS+52O0g9MCxzv8NN0pwNOARrjW0+6Fp2ainF7gHbMPcDlBvOgqhFUh9ucXLdU7yTplknixfq4YAt5zjHH2nITiYffqUQxzmUYyxdMnXXps5WzaruMDH6HU0oxeYzRqHt01XoRKWnQN8Di26wXEt2Lk5g6B+ZU3yKUGtXg+tRCiPRvRNLZgvu9aXIoMzEplRJTVxJtL51ExY+liTlWCSCbQ0hWevOjlCEn2INCzc4AB3nNzcfuU6ZeHP8j1HznQQ+wEFu9hAfKGtOPRLG8WUWUXFKfqyKjwfnWvbxzJu4mTraX836Rtpk5nL7998mPk71eFA==","RESPONSE":"Ba39uo40J2ypr9mN+JGT/70efLlRUxrQJyF0qhaGM3lwl1xqSKtOWh2zqglZVmJnxoWiEHbNx+DIoNqBCMctcMOc64UXgn18kDDi0kUytEZSzXPFhLlSb6hMFcmYXtCbFisAPHPLkGgXkftJQzUBauDkG62kikfVUQ6CeCxxmBAKBrJl3wZmXeDcLteu80R2U2C42GaaglbxceDTdKoUrMHgDKbxF9vKNAc1JklDLGL5SqzK8J4pXRXIuWd7j3STvJGAb8Yk1wc2g46oJMFWuCvtyizC3Wc3yLyZcmHHfoTH7x954623oaBFYdEyVIKelT+UevRc8xIWzYRmj4I3mh+FwGuNkS3iBoy9bKyjK6l4e2+VUjxrpJiTti5vqFr3BQ19qEivwE3TGv93GXGKoVOGAZMHewopUTgXALCpTILTeaxPeHpL2PoC0lLDSJx4B2jUVbiRepW5goyvwLDck+YpUOuYxhH3LPelb3sldBoqlj2o29gUhBq9WLHha3qlzAIloQoIICveL2w90fnM8ad7l6u7Oy4/Rvob9USYmZ8TiDGFRQU543hGKYW0HYyDg7b8QJ852Mx7Yjn4BXO7v+shyT6YSCkO0IoQsy4KlMgSuq2VS4EXCuEgDnthuXWUo8wIpwuDKLzpn0l6GnWYOKYihN4QOkFHK2l0TPO1ZkDtDRODv2AOgvCj5vYtwRrSwfzE9eH3ZOHSVMcbNLvwQTLScjYBOpIF48DTpADPsn7ZwqZxQj9ScQ3zoo/D9wQiPQb9EYDpPywMroXh0kyw2eJdYzAn6DwXqojOabMUTfJdqxigQdq470s46VLnfJWFZ/o5mhXUmfYCbaInwrL0fh+I9s80kqeFIDJOeaaC9ZDcPCJnSqpiJjXK+CBQtnq31C2tvsQQ8xq+SEoXktW6a146aPnBIr1wcnNcVIJKZCLZSlMCYx6IHg==","REQUEST_REFERENCE_NUMBER":"SBIOC20261760000038770567","RESPONSE_DATE":"25-06-2026 20:18:29"}
2026-06-25 20:18:29,368 INFO  (CcmsUtil.java:118) - ERROR_DESCRIPTION: {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":false,"messages":[],"correlationId":"019eff40-e2c1-7029-8821-708269cc8646","locale":"eng","body":null,"errors":{"global":["Validation failed for consent list:\n - Consent[2] (purposeCode=PR-INCIDENT-000101): Missing product codes: CAR_LOAN, PERSONAL_LOAN\n - Consent[7] (purposeCode=PR-PROMOTIO-000106): Missing product codes: CREDIT_CARD, WILL_TRUSTEESHIP_SERVICES, DEMAT_AND_SECURITIES, MUTUAL_FUND_AND_ASSET_MANAGEMENT, GENERAL_INSURANCE, LIFE_INSURANCE\n - Consent[7] (purposeCode=PR-PROMOTIO-000106): Unexpected product codes: HOME_LOAN"]},"statusCode":400,"timestamp":"2026-06-25T14:48:29.329469425Z"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}



Purpose enquiry request 
	
	{
  "EIS_PAYLOAD": {
    "HEADERS": {
      "X-Correlation-Id": "019eff40-e2c3-7938-9bd7-abbf52932a34",
      "Accept-Language": "eng",
      "X-API-Version": "1"
    },
    "BODY": {
      "purposeSetId": "PS-CIFJOURN-000091",
      "touchPointId": "OCAS000001"
    }
  },
  "SOURCE_ID": "OC",
  "DESTINATION": "CRM",
  "TXN_TYPE": "CCMS",
  "TXN_SUB_TYPE": "READ_PURPOSES"
}
	
	
	Response 


		{
  "RESPONSE_STATUS": "0",
  "EIS_RESPONSE": {
    "success": true,
    "messages": null,
    "correlationId": "019eff40-e2c3-7938-9bd7-abbf52932a34",
    "locale": "eng",
    "body": [
      {
        "containerDescription": "Essential Purposes",
        "purposes": [
          {
            "hasValidity": false,
            "code": "PR-FACILITA-000099",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "- To enable use of products or applications/requests for any products, process such applications/requests, perform any contract pursuant thereto, and undertake specified purposes related to these.\n- To personalize and improve platform/app experience and user engagement.\n- To enable product sales, cross-sales, distribution, or referrals, for data received from third parties.\n- To enable the use of websites, platforms, mobile applications, and online services.\n- To share your information with our partners and service providers for providing banking facilities and services opted by you.\n- To allow you to participate in surveys, contests and to administer these activities\n",
            "dataPoints": [],
            "isEssential": true,
            "title": "Facilitate usage of our products, services and applications",
            "version": 1,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": false,
            "code": "PR-REGULATO-000100",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "- For credit scoring, credit analysis, risk analysis, preventing fraud and money laundering, identity and screening checks, due diligence, inspections, and related purposes, including obtaining reports and information from credit bureaus, or service providers to fulfil the product or service requirement chosen by you. \n- To process my data to comply with regulatory and legal requirements.\n- To share information with relevant authorities, including regulators, Income Tax and other government authorities of India and overseas.\n- To access/share my KYC records/information with the Central KYC Registry (CKYCR), run credit checks on my behalf, obtain a credit report for the purpose of performing customer due diligence/credit decisions in relation to opening an account with SBI.",
            "dataPoints": [],
            "isEssential": true,
            "title": "Regulatory Compliance, Risk Management, and Customer Due Diligence",
            "version": 1,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": false,
            "code": "PR-INCIDENT-000101",
            "isProductSpecific": true,
            "isETB": true,
            "bankProducts": [
              {
                "code": "HOME_LOAN",
                "label": "Home Loan"
              },
              {
                "code": "CAR_LOAN",
                "label": "Car Loan"
              },
              {
                "code": "PERSONAL_LOAN",
                "label": "Personal Loan"
              }
            ],
            "description": "- Where necessary, to protect your interests in emergency situations when consent cannot be obtained.",
            "dataPoints": [],
            "isEssential": true,
            "title": "Incidental or Emergency Use",
            "version": 2,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": false,
            "code": "PR-MANAGEYO-000102",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "- To manage banking relationships with you.\n- For debt collection and related services.\n- To contact you or establish your whereabouts for legitimate use.\n- To inform you about changes to terms, conditions, and policies, and to communicate other administrative information.\n- To monitor or record communications, including phone calls, for improving services.\n- To reach out for account management and keeping you informed about your products or services.\n- To record CCTV images/video when you visit SBI premises/ branches or ATMs for operational, legal and regulatory purposes.\n- To resolve complaints and enquiries.\n- To send messages, notifications or alerts by post, telephone, text, email, social media, POS machines and other digital methods, including for example via our ATMs, mobile applications, push notifications, or online banking services (and new methods that may become available in the future). This may be for: a) managing your accounts b) meeting our regulatory obligations",
            "dataPoints": [],
            "isEssential": true,
            "title": "Manage your relationship with us",
            "version": 1,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": true,
            "code": "PR-SERVICEI-000077",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "For service or product development, audit, business continuity, planning, developing efficient solutions, and improving product delivery",
            "dataPoints": [
              "Mobile"
            ],
            "isEssential": true,
            "title": "Service Improvement and Development",
            "version": 1,
            "validityDays": 3600,
            "isNTB": true
          }
        ],
        "containerTitle": "Essential Purposes",
        "isContainer": true
      },
      {
        "containerDescription": "Optional Purposes",
        "purposes": [
          {
            "hasValidity": false,
            "code": "PR-ANALYTIC-000105",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "- Understand customer needs better by carrying out data analytics that help us design products, services and provide offers that suit you. Such as pre-approved loan etc. ",
            "dataPoints": [],
            "isEssential": false,
            "title": "Analytics",
            "version": 2,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": false,
            "code": "PR-PROMOTIO-000104",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "Share updates and offers – by informing you about bank’s products, promotions, and services that may be of interest to you",
            "dataPoints": [],
            "isEssential": false,
            "title": "Promotional and Marketing",
            "version": 1,
            "validityDays": null,
            "isNTB": true
          },
          {
            "hasValidity": false,
            "code": "PR-PROMOTIO-000106",
            "isProductSpecific": true,
            "isETB": true,
            "bankProducts": [
              {
                "code": "CREDIT_CARD",
                "label": "Credit Card"
              },
              {
                "code": "WILL_TRUSTEESHIP_SERVICES",
                "label": "Will Trusteeship Services"
              },
              {
                "code": "DEMAT_AND_SECURITIES",
                "label": "Demat and securities"
              },
              {
                "code": "MUTUAL_FUND_AND_ASSET_MANAGEMENT",
                "label": "Mutual Fund / Asset Management"
              },
              {
                "code": "GENERAL_INSURANCE",
                "label": "General Insurance"
              },
              {
                "code": "LIFE_INSURANCE",
                "label": "Life Insurance"
              }
            ],
            "description": "-Share updates and offers of the companies with whom SBI has entered into agreements - for providing suitable “services/products” opted by you. ",
            "dataPoints": [],
            "isEssential": false,
            "title": "Promotional and Marketing - Companies with whom SBI has entered into agreements",
            "version": 3,
            "validityDays": null,
            "isNTB": true
          }
        ],
        "containerTitle": "Optional Purposes",
        "isContainer": true
      },
      {
        "containerDescription": "",
        "purposes": [
          {
            "hasValidity": true,
            "code": "PR-USERPROF-000118",
            "isProductSpecific": false,
            "isETB": true,
            "bankProducts": [],
            "description": "Browsing and transaction activity history will be used to provide tailored recommendations and customized content.",
            "dataPoints": [
              "Income Details",
              "Pincode"
            ],
            "isEssential": false,
            "title": "User Profile Personalization",
            "version": 2,
            "validityDays": 90,
            "isNTB": false
          }
        ],
        "containerTitle": "",
        "isContainer": false
      }
    ],
    "errors": null,
    "statusCode": 200,
    "timestamp": "2026-06-25T14:48:28.687932235"
  },
  "ERROR_DESCRIPTION": "",
  "ERROR_CODE": ""
}







