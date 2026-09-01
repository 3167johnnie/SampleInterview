2026-09-01 13:24:46,904 INFO  (RequestHandler.java:76) - RequestHandler.java LNo : 42 :: getRandom :: a37c-b932407b7af
2026-09-01 13:24:46,927 INFO  (CveLoanAction.java:95) - cveLoanAction.java LNo : 524 : cveLoan()
2026-09-01 13:24:46,927 INFO  (CveLoanAction.java:96) - uiType : 525 : cveLoan()null
2026-09-01 13:24:46,927 INFO  (CveLoanAction.java:149) - before getPersonalLoan call from cveLoan() :: Loan ID..3
2026-09-01 13:24:46,928 INFO  (RequestHandler.java:20) - Request Value : OWQ1OGRkYWQxMWJlNWQzYWJkZmZmNDI3NDBlNmMzNjE6OjFmY2YzYTM5N2RiNjg1MWE0OWY0NTkwMGUxN2E2OWZlOjp4b1RqZExEY3M5bGpPNkw3Uk1XMGN3U0h5dzltdXUrR1BpVEkrOUR6YmxhNThHSE4yRTY5eVQxL1NaUStaRTBNdVVZK2RSN2o4c3VaelZUYXNic0FGaTNnN0xzQ0R5TlFLQ2Y5T0NxZXFNZ2FsVlBRcUQzYktKTUFQc1lxclVxYjlnbERHYWJKN0lGWWVJL2VQUkR1ZEhOQ2ZJYWltNkVMSFVyTU9kRmFHNFk1Rk8vNHRNakY4S2E3YTBUeTc5V1dpdXBlM1Zwa2JNS3FoNEw3WlBiQU12RWRQeHBReVZXUmM2TUdocDlvaWtrPQ==
2026-09-01 13:24:46,928 INFO  (RequestHandler.java:23) - Request Value decryptedRequest : 9d58ddad11be5d3abdfff42740e6c361::1fcf3a397db6851a49f45900e17a69fe::xoTjdLDcs9ljO6L7RMW0cwSHyw9muu+GPiTI+9Dzbla58GHN2E69yT1/SZQ+ZE0MuUY+dR7j8suZzVTasbsAFi3g7LsCDyNQKCf9OCqeqMgalVPQqD3bKJMAPsYqrUqb9glDGabJ7IFYeI/ePRDudHNCfIaim6ELHUrMOdFaG4Y5FO/4tMjF8Ka7a0Ty79WWiupe3VpkbMKqh4L7ZPbAMvEdPxpQyVWRc6MGhp9oikk=
2026-09-01 13:24:46,929 INFO  (RequestHandler.java:34) - RequestHandler.java LNo : 42 ::: calling fetchAppDataSourceId()::
2026-09-01 13:24:46,929 INFO  (RequestHandler.java:50) - RequestHandler.java LNo : 45 ::: substring is:: 0"
2026-09-01 13:24:46,930 INFO  (RequestHandler.java:51) - RequestHandler.java LNo : 49 ::: SessionUtil.getHomeLoanApplicationSequenceId() is:: null
2026-09-01 13:24:46,930 INFO  (RequestHandler.java:54) - RequestHandler.java LNo : 51 ::: alternateNumber is:: true
2026-09-01 13:24:47,224 INFO  (ValidatorManager.java:119) - ntbNumber null
2026-09-01 13:24:47,224 INFO  (ValidatorManager.java:128) - SessionUtil.getConsentRevokeNTB() null
2026-09-01 13:24:47,224 INFO  (ValidatorManager.java:442) - validatorResponse isStatus : true
2026-09-01 13:24:47,224 INFO  (ValidatorManager.java:443) - validatorResponse getErrorMessage : null
2026-09-01 13:24:47,225 INFO  (CveLoanAction.java:181) - CveLoanAction.java LN 407 stateManagerBean 29
2026-09-01 13:24:47,225 INFO  (CveLoanAction.java:208) - state manager -1 calling for CVE
2026-09-01 13:24:47,225 INFO  (CveLoanAction.java:319) - state manager 28 and 29 is called for CVE.......
2026-09-01 13:24:47,225 INFO  (CveProcessManagerImpl.java:983) - CveProcessManagerImpl LNo: 1029 ::inputOtp::hNgx2BJp1LVydvi6uQhS4g==
2026-09-01 13:24:47,225 INFO  (CveProcessManagerImpl.java:984) - CveProcessManagerImpl LNo: 4622 ::appSeqId::14040..cbsCallId..36899..ajaxPostUrl..cve
2026-09-01 13:24:47,225 INFO  (CveProcessManagerImpl.java:988) - DecryptedRequest inputOtp   1014  hNgx2BJp1LVydvi6uQhS4g==
2026-09-01 13:24:47,519 INFO  (CveProcessManagerImpl.java:1146) - CveProcessManagerImpl processCBSOTP : stateId == 29 ::
2026-09-01 13:24:47,523 INFO  (CveProcessManagerImpl.java:1164) - CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::14040
2026-09-01 13:24:47,523 INFO  (CveProcessManagerImpl.java:1165) - CveProcessManagerImpl LNo : 4259::app.getAppSeqId:::com.mintstreet.loan.personal.entity.ApplicationFormPersonalLoan@21a89701
2026-09-01 13:24:47,523 INFO  (CommonService.java:1724) - getCveReferenceIdBySeqId>>appSeqId::14040
2026-09-01 13:24:47,525 INFO  (CommonService.java:1729) - getCveReferenceIdBySeqId>>referenceId240926000001
2026-09-01 13:24:47,525 INFO  (CommonService.java:1687) - PersonalLoanService.java LNo : 952 : getApplicationFormCveLoanBySeqId:::::14040
2026-09-01 13:24:47,528 INFO  (CveProcessManagerImpl.java:1605) - CveProcessManagerImpl.java LNO 3546::org.hibernate.exception.SQLGrammarException: could not load an entity: [com.mintstreet.loan.cveloan.entity.ApplicationFormCveLoan#14040]
2026-09-01 13:24:47,528 INFO  (CveLoanAction.java:342) - CveLoanAction.java :: processCBSOTP for CVE called..inputOtp:::: hNgx2BJp1LVydvi6uQhS4g==
2026-09-01 13:24:47,528 INFO  (CveLoanAction.java:347) - CveLoanAction.java :: processCBSOTP for CVE called..apiMessage:::: Sorry for the inconvenience, Please click <a href='javascript:loanFirstPage();'>here</a> to start again
2026-09-01 13:24:47,528 INFO  (CveLoanAction.java:350) - CveLoanAction.java LNo:390 :: crmResponse for CASE creation::
2026-09-01 13:24:47,528 INFO  (CveLoanAction.java:450) - CveLoanAction.java :: processCBSOTP for CVE called..json:::: API SUCCESS::ELSE CONDITION::.......Sorry for the inconvenience, Please click <a href='javascript:loanFirstPage();'>here</a> to start again
2026-09-01 13:24:47,528 INFO  (CveLoanAction.java:452) - CveLoanAction.java :: processCBSOTP for CVE called..json::::error {"message":"Sorry for the inconvenience, Please click <a href='javascript:loanFirstPage();'>here<\/a> to start again","status":"error"}


this is logs getting error.
getting this error for above changes help me to make changes to give where changes need to do stepby step.
