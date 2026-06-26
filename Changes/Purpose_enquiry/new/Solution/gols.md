{
  "EIS_PAYLOAD": {
    "HEADERS": {
      "X-Correlation-Id": "019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a",
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






{
  "RESPONSE_STATUS": "0",
  "EIS_RESPONSE": {
    "success": true,
    "messages": null,
    "correlationId": "019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a",
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
    "timestamp": "2026-06-26T06:42:45.092599150"
  },
  "ERROR_DESCRIPTION": "",
  "ERROR_CODE": ""
}

2026-06-26 12:12:39,432 INFO  (HomeLoanAction.java:3256) - ccms enQUIRY API <<<<<<<<<>>>>>>>>>>>>>>>>>>>>>START>>>>>>>>>>>>>>>>>>>> :
2026-06-26 12:12:39,488 INFO  (ConsentService.java:105) - ccms PurposeEnquiry consentRequest : {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"READ_PURPOSES"}
2026-06-26 12:12:39,488 INFO  (ConsentService.java:106) - ccms PurposeEnquiry consentRequest : {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"READ_PURPOSES"}
2026-06-26 12:12:39,554 INFO  (CcmsUtil.java:28) - callingEISServiceForCcms start...
2026-06-26 12:12:39,556 INFO  (PanApiRSAEncryption.java:52) - PanApiRSAEncryption ( ) CBS_SI_PAN_RSA_ENCRYPTION_CERTIFICATE_PATH : :  /srv/sbiroot/support_lms/documents/certificates/RenewEIS_certificate/EIS_ENC_UAT.cer
2026-06-26 12:12:39,615 INFO  (PanApiRSAEncryption.java:114) - RSA Encrypted Data h0vGHMlP1uuuzF14WJxyV78v23g4EIzo/Ghy4ZGS+01GzqcP+V66KFv+qb78MdexsSQG6VO4sdX4nz2P3AuaJcpjgxaM0EMb1PNYNyK8iM4venIye4OF4g2td6dIjmboq0hSPIOA80KtPYb/UMQrubPSizXCsqBrdHmm/POd4N5gAm6CtH6H2L1SVzXK8Jp9QF1lINeGsC0/QOXm3Q769ADwXwspxdbnZTFF8+HXT28tCApkhN3rAUJ8a5ctDpwiJJtKwb/ero0h+fbif78vdmAyct4wXToa+I1r6lwZ0t7amOD6E/wczj4kLmOgNFCfbje26wN/5KyK6IB5EWSAjg==
2026-06-26 12:12:45,165 INFO  (CcmsUtil.java:80) - CCMS request: {"EIS_PAYLOAD":{"HEADERS":{"X-Correlation-Id":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","Accept-Language":"eng","X-API-Version":"1"},"BODY":{"purposeSetId":"PS-CIFJOURN-000091","touchPointId":"OCAS000001"}},"SOURCE_ID":"OC","DESTINATION":"CRM","TXN_TYPE":"CCMS","TXN_SUB_TYPE":"READ_PURPOSES"}
2026-06-26 12:12:45,166 INFO  (CcmsUtil.java:81) - Connection Response Code : 200
2026-06-26 12:12:45,170 INFO  (CcmsUtil.java:105) - eisEncryptedResponseStr : {"DIGI_SIGN":"g49eRDvR4F7d7qJ/ATp7AIdmuPy23wZwqXgKaejWzkIf0aLKbiXRljFe5FMGi2pgBw2SgZwLxyvU3aipLo0KvBISquwk1AJhzTI72pie+Gr6ppdzp+ufZtBWLRuzFga3w1XIr8rNG1eevyq9dxg8NUfOS9cWEAB5MOloZR7bmml5eqMBUtU0Au2hNXYy+b5eFMO3XqcbEXVx1g4322SUW9Ouuh1g4kuSS0tgDAu4BKZ/o3oeAzKIOZ7SjLZ1+dWyb4nJDWr7FY4frkATgHaV1+PW8U8tWcKNg27FvFKQcuqnTl+JzB+ZjyIzuYbPwT+y0LhPyEQuXlWmPjkTW4tchA==","RESPONSE":"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","REQUEST_REFERENCE_NUMBER":"SBIOC20261770000061529254","RESPONSE_DATE":"26-06-2026 12:12:45"}
2026-06-26 12:12:45,176 INFO  (CcmsUtil.java:118) - ERROR_DESCRIPTION: {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":true,"messages":null,"correlationId":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","locale":"eng","body":[{"containerDescription":"Essential Purposes","purposes":[{"hasValidity":false,"code":"PR-FACILITA-000099","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To enable use of products or applications/requests for any products, process such applications/requests, perform any contract pursuant thereto, and undertake specified purposes related to these.\n- To personalize and improve platform/app experience and user engagement.\n- To enable product sales, cross-sales, distribution, or referrals, for data received from third parties.\n- To enable the use of websites, platforms, mobile applications, and online services.\n- To share your information with our partners and service providers for providing banking facilities and services opted by you.\n- To allow you to participate in surveys, contests and to administer these activities\n","dataPoints":[],"isEssential":true,"title":"Facilitate usage of our products, services and applications","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-REGULATO-000100","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- For credit scoring, credit analysis, risk analysis, preventing fraud and money laundering, identity and screening checks, due diligence, inspections, and related purposes, including obtaining reports and information from credit bureaus, or service providers to fulfil the product or service requirement chosen by you. \n- To process my data to comply with regulatory and legal requirements.\n- To share information with relevant authorities, including regulators, Income Tax and other government authorities of India and overseas.\n- To access/share my KYC records/information with the Central KYC Registry (CKYCR), run credit checks on my behalf, obtain a credit report for the purpose of performing customer due diligence/credit decisions in relation to opening an account with SBI.","dataPoints":[],"isEssential":true,"title":"Regulatory Compliance, Risk Management, and Customer Due Diligence","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-INCIDENT-000101","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"HOME_LOAN","label":"Home Loan"},{"code":"CAR_LOAN","label":"Car Loan"},{"code":"PERSONAL_LOAN","label":"Personal Loan"}],"description":"- Where necessary, to protect your interests in emergency situations when consent cannot be obtained.","dataPoints":[],"isEssential":true,"title":"Incidental or Emergency Use","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-MANAGEYO-000102","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To manage banking relationships with you.\n- For debt collection and related services.\n- To contact you or establish your whereabouts for legitimate use.\n- To inform you about changes to terms, conditions, and policies, and to communicate other administrative information.\n- To monitor or record communications, including phone calls, for improving services.\n- To reach out for account management and keeping you informed about your products or services.\n- To record CCTV images/video when you visit SBI premises/ branches or ATMs for operational, legal and regulatory purposes.\n- To resolve complaints and enquiries.\n- To send messages, notifications or alerts by post, telephone, text, email, social media, POS machines and other digital methods, including for example via our ATMs, mobile applications, push notifications, or online banking services (and new methods that may become available in the future). This may be for: a) managing your accounts b) meeting our regulatory obligations","dataPoints":[],"isEssential":true,"title":"Manage your relationship with us","version":1,"validityDays":null,"isNTB":true},{"hasValidity":true,"code":"PR-SERVICEI-000077","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"For service or product development, audit, business continuity, planning, developing efficient solutions, and improving product delivery","dataPoints":["Mobile"],"isEssential":true,"title":"Service Improvement and Development","version":1,"validityDays":3600,"isNTB":true}],"containerTitle":"Essential Purposes","isContainer":true},{"containerDescription":"Optional Purposes","purposes":[{"hasValidity":false,"code":"PR-ANALYTIC-000105","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- Understand customer needs better by carrying out data analytics that help us design products, services and provide offers that suit you. Such as pre-approved loan etc. ","dataPoints":[],"isEssential":false,"title":"Analytics","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000104","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Share updates and offers \u2013 by informing you about bank\u2019s products, promotions, and services that may be of interest to you","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000106","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"CREDIT_CARD","label":"Credit Card"},{"code":"WILL_TRUSTEESHIP_SERVICES","label":"Will Trusteeship Services"},{"code":"DEMAT_AND_SECURITIES","label":"Demat and securities"},{"code":"MUTUAL_FUND_AND_ASSET_MANAGEMENT","label":"Mutual Fund / Asset Management"},{"code":"GENERAL_INSURANCE","label":"General Insurance"},{"code":"LIFE_INSURANCE","label":"Life Insurance"}],"description":"-Share updates and offers of the companies with whom SBI has entered into agreements - for providing suitable \u201cservices/products\u201d opted by you. ","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing - Companies with whom SBI has entered into agreements","version":3,"validityDays":null,"isNTB":true}],"containerTitle":"Optional Purposes","isContainer":true},{"containerDescription":"","purposes":[{"hasValidity":true,"code":"PR-USERPROF-000118","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Browsing and transaction activity history will be used to provide tailored recommendations and customized content.","dataPoints":["Income Details","Pincode"],"isEssential":false,"title":"User Profile Personalization","version":2,"validityDays":90,"isNTB":false}],"containerTitle":"","isContainer":false}],"errors":null,"statusCode":200,"timestamp":"2026-06-26T06:42:45.092599150"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}
2026-06-26 12:12:45,176 INFO  (CcmsUtil.java:120) - ERROR_DESCRIPTION:
2026-06-26 12:12:45,177 INFO  (ConsentService.java:121) - ccms PurposeEnquiry responseJson : {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":true,"messages":null,"correlationId":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","locale":"eng","body":[{"containerDescription":"Essential Purposes","purposes":[{"hasValidity":false,"code":"PR-FACILITA-000099","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To enable use of products or applications/requests for any products, process such applications/requests, perform any contract pursuant thereto, and undertake specified purposes related to these.\n- To personalize and improve platform/app experience and user engagement.\n- To enable product sales, cross-sales, distribution, or referrals, for data received from third parties.\n- To enable the use of websites, platforms, mobile applications, and online services.\n- To share your information with our partners and service providers for providing banking facilities and services opted by you.\n- To allow you to participate in surveys, contests and to administer these activities\n","dataPoints":[],"isEssential":true,"title":"Facilitate usage of our products, services and applications","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-REGULATO-000100","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- For credit scoring, credit analysis, risk analysis, preventing fraud and money laundering, identity and screening checks, due diligence, inspections, and related purposes, including obtaining reports and information from credit bureaus, or service providers to fulfil the product or service requirement chosen by you. \n- To process my data to comply with regulatory and legal requirements.\n- To share information with relevant authorities, including regulators, Income Tax and other government authorities of India and overseas.\n- To access/share my KYC records/information with the Central KYC Registry (CKYCR), run credit checks on my behalf, obtain a credit report for the purpose of performing customer due diligence/credit decisions in relation to opening an account with SBI.","dataPoints":[],"isEssential":true,"title":"Regulatory Compliance, Risk Management, and Customer Due Diligence","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-INCIDENT-000101","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"HOME_LOAN","label":"Home Loan"},{"code":"CAR_LOAN","label":"Car Loan"},{"code":"PERSONAL_LOAN","label":"Personal Loan"}],"description":"- Where necessary, to protect your interests in emergency situations when consent cannot be obtained.","dataPoints":[],"isEssential":true,"title":"Incidental or Emergency Use","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-MANAGEYO-000102","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To manage banking relationships with you.\n- For debt collection and related services.\n- To contact you or establish your whereabouts for legitimate use.\n- To inform you about changes to terms, conditions, and policies, and to communicate other administrative information.\n- To monitor or record communications, including phone calls, for improving services.\n- To reach out for account management and keeping you informed about your products or services.\n- To record CCTV images/video when you visit SBI premises/ branches or ATMs for operational, legal and regulatory purposes.\n- To resolve complaints and enquiries.\n- To send messages, notifications or alerts by post, telephone, text, email, social media, POS machines and other digital methods, including for example via our ATMs, mobile applications, push notifications, or online banking services (and new methods that may become available in the future). This may be for: a) managing your accounts b) meeting our regulatory obligations","dataPoints":[],"isEssential":true,"title":"Manage your relationship with us","version":1,"validityDays":null,"isNTB":true},{"hasValidity":true,"code":"PR-SERVICEI-000077","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"For service or product development, audit, business continuity, planning, developing efficient solutions, and improving product delivery","dataPoints":["Mobile"],"isEssential":true,"title":"Service Improvement and Development","version":1,"validityDays":3600,"isNTB":true}],"containerTitle":"Essential Purposes","isContainer":true},{"containerDescription":"Optional Purposes","purposes":[{"hasValidity":false,"code":"PR-ANALYTIC-000105","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- Understand customer needs better by carrying out data analytics that help us design products, services and provide offers that suit you. Such as pre-approved loan etc. ","dataPoints":[],"isEssential":false,"title":"Analytics","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000104","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Share updates and offers \u2013 by informing you about bank\u2019s products, promotions, and services that may be of interest to you","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000106","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"CREDIT_CARD","label":"Credit Card"},{"code":"WILL_TRUSTEESHIP_SERVICES","label":"Will Trusteeship Services"},{"code":"DEMAT_AND_SECURITIES","label":"Demat and securities"},{"code":"MUTUAL_FUND_AND_ASSET_MANAGEMENT","label":"Mutual Fund / Asset Management"},{"code":"GENERAL_INSURANCE","label":"General Insurance"},{"code":"LIFE_INSURANCE","label":"Life Insurance"}],"description":"-Share updates and offers of the companies with whom SBI has entered into agreements - for providing suitable \u201cservices/products\u201d opted by you. ","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing - Companies with whom SBI has entered into agreements","version":3,"validityDays":null,"isNTB":true}],"containerTitle":"Optional Purposes","isContainer":true},{"containerDescription":"","purposes":[{"hasValidity":true,"code":"PR-USERPROF-000118","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Browsing and transaction activity history will be used to provide tailored recommendations and customized content.","dataPoints":["Income Details","Pincode"],"isEssential":false,"title":"User Profile Personalization","version":2,"validityDays":90,"isNTB":false}],"containerTitle":"","isContainer":false}],"errors":null,"statusCode":200,"timestamp":"2026-06-26T06:42:45.092599150"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}
2026-06-26 12:12:45,178 INFO  (ConsentService.java:124) - ccms PurposeEnquiry responseJson responseStr : {"RESPONSE_STATUS":"0","EIS_RESPONSE":{"success":true,"messages":null,"correlationId":"019f02aa-8ccb-7b47-b2b9-b3769eb6ed7a","locale":"eng","body":[{"containerDescription":"Essential Purposes","purposes":[{"hasValidity":false,"code":"PR-FACILITA-000099","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To enable use of products or applications/requests for any products, process such applications/requests, perform any contract pursuant thereto, and undertake specified purposes related to these.\n- To personalize and improve platform/app experience and user engagement.\n- To enable product sales, cross-sales, distribution, or referrals, for data received from third parties.\n- To enable the use of websites, platforms, mobile applications, and online services.\n- To share your information with our partners and service providers for providing banking facilities and services opted by you.\n- To allow you to participate in surveys, contests and to administer these activities\n","dataPoints":[],"isEssential":true,"title":"Facilitate usage of our products, services and applications","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-REGULATO-000100","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- For credit scoring, credit analysis, risk analysis, preventing fraud and money laundering, identity and screening checks, due diligence, inspections, and related purposes, including obtaining reports and information from credit bureaus, or service providers to fulfil the product or service requirement chosen by you. \n- To process my data to comply with regulatory and legal requirements.\n- To share information with relevant authorities, including regulators, Income Tax and other government authorities of India and overseas.\n- To access/share my KYC records/information with the Central KYC Registry (CKYCR), run credit checks on my behalf, obtain a credit report for the purpose of performing customer due diligence/credit decisions in relation to opening an account with SBI.","dataPoints":[],"isEssential":true,"title":"Regulatory Compliance, Risk Management, and Customer Due Diligence","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-INCIDENT-000101","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"HOME_LOAN","label":"Home Loan"},{"code":"CAR_LOAN","label":"Car Loan"},{"code":"PERSONAL_LOAN","label":"Personal Loan"}],"description":"- Where necessary, to protect your interests in emergency situations when consent cannot be obtained.","dataPoints":[],"isEssential":true,"title":"Incidental or Emergency Use","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-MANAGEYO-000102","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- To manage banking relationships with you.\n- For debt collection and related services.\n- To contact you or establish your whereabouts for legitimate use.\n- To inform you about changes to terms, conditions, and policies, and to communicate other administrative information.\n- To monitor or record communications, including phone calls, for improving services.\n- To reach out for account management and keeping you informed about your products or services.\n- To record CCTV images/video when you visit SBI premises/ branches or ATMs for operational, legal and regulatory purposes.\n- To resolve complaints and enquiries.\n- To send messages, notifications or alerts by post, telephone, text, email, social media, POS machines and other digital methods, including for example via our ATMs, mobile applications, push notifications, or online banking services (and new methods that may become available in the future). This may be for: a) managing your accounts b) meeting our regulatory obligations","dataPoints":[],"isEssential":true,"title":"Manage your relationship with us","version":1,"validityDays":null,"isNTB":true},{"hasValidity":true,"code":"PR-SERVICEI-000077","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"For service or product development, audit, business continuity, planning, developing efficient solutions, and improving product delivery","dataPoints":["Mobile"],"isEssential":true,"title":"Service Improvement and Development","version":1,"validityDays":3600,"isNTB":true}],"containerTitle":"Essential Purposes","isContainer":true},{"containerDescription":"Optional Purposes","purposes":[{"hasValidity":false,"code":"PR-ANALYTIC-000105","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"- Understand customer needs better by carrying out data analytics that help us design products, services and provide offers that suit you. Such as pre-approved loan etc. ","dataPoints":[],"isEssential":false,"title":"Analytics","version":2,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000104","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Share updates and offers \u2013 by informing you about bank\u2019s products, promotions, and services that may be of interest to you","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing","version":1,"validityDays":null,"isNTB":true},{"hasValidity":false,"code":"PR-PROMOTIO-000106","isProductSpecific":true,"isETB":true,"bankProducts":[{"code":"CREDIT_CARD","label":"Credit Card"},{"code":"WILL_TRUSTEESHIP_SERVICES","label":"Will Trusteeship Services"},{"code":"DEMAT_AND_SECURITIES","label":"Demat and securities"},{"code":"MUTUAL_FUND_AND_ASSET_MANAGEMENT","label":"Mutual Fund / Asset Management"},{"code":"GENERAL_INSURANCE","label":"General Insurance"},{"code":"LIFE_INSURANCE","label":"Life Insurance"}],"description":"-Share updates and offers of the companies with whom SBI has entered into agreements - for providing suitable \u201cservices/products\u201d opted by you. ","dataPoints":[],"isEssential":false,"title":"Promotional and Marketing - Companies with whom SBI has entered into agreements","version":3,"validityDays":null,"isNTB":true}],"containerTitle":"Optional Purposes","isContainer":true},{"containerDescription":"","purposes":[{"hasValidity":true,"code":"PR-USERPROF-000118","isProductSpecific":false,"isETB":true,"bankProducts":[],"description":"Browsing and transaction activity history will be used to provide tailored recommendations and customized content.","dataPoints":["Income Details","Pincode"],"isEssential":false,"title":"User Profile Personalization","version":2,"validityDays":90,"isNTB":false}],"containerTitle":"","isContainer":false}],"errors":null,"statusCode":200,"timestamp":"2026-06-26T06:42:45.092599150"},"ERROR_DESCRIPTION":"","ERROR_CODE":""}
2026-06-26 12:12:45,283 INFO  (HomeLoanAction.java:3258) - ccms enQUIRY API <<<<<<<<<>>>>>>>>>>>>>>>>>>>>>END >>>>>>>>>>>>>>>>>>>> :
2026-06-26 12:12:45,288 INFO  (MasterLanguageDao.java:21) - Active language count : 23
2026-06-26 12:12:45,288 INFO  (HomeLoanAction.java:3267) - Privacy dropdown language count : 23
2026-06-26 12:12:45,321 INFO  (PrivacyRequestResponseDao.java:22) - Successfully retrieved PrivacyRequestResponse for locale: eng
2026-06-26 12:12:45,321 INFO  (HomeLoanAction.java:3282) - Privacy callCCMSConsentWriteAPI  Start :
2026-06-26 12:12:45,322 INFO  (HomeLoanAction.java:3284) - Privacy callCCMSConsentWriteAPI  End :
2026-06-26 12:12:45,417 INFO  (BaseAction.java:532) - BaseAction.java : isInSession:false applicationSubTypeId:0 applicationSubTypeId:0 applicationType:0 isDsrPage:false
2026-06-26 12:12:45,417 INFO  (BaseAction.java:683) - BaseAction.java LNo: 660 : isInSession:false applicationSubTypeId:0 applicationSubTypeId:0 applicationType:0 isDsrPage:false
2026-06-26 12:12:56,358 INFO  (ImageCaptcha.java:94) - Image Captcha before setting : gmzf3z






