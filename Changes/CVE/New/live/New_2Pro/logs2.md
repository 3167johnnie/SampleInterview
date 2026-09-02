2026-09-02 19:26:28,720 INFO  (CRMServiceNew.java:1434) -  :: crmResponse in REST :: com.mintstreet.common.bo.CRMResponse@14079981
2026-09-02 19:26:28,794 INFO  (CRMServiceNew.java:1215) - CRMServiceNewJSON Convert XML to JSON format leadJson : : YQxlvIk91u7wTmegbT/ZWzIWlw6Wq46S3+BDvKFQDCgIZqhD4lMSkfV3Q8PHsdpFu40AkT78ysXRUNVWonkNiG+1hsnCiqPZNN7h8E6rNwzI2PvQuCZ/f/RuFDf/2QqDFg9rcVk1kyDudWcT0dk2qsO9aiqeHJHgiftdgZF6fzL2Ie3UQmcySy6DRU9AGdRBzn5X4UllTevUzRp8P8YihEJN6bBu9r5kF4nsLWo1mnYGiz7Pn54D48R+ji1BBIy4DO1FLgyoxKbAFZrArOV/BN5VLWQnNaasubNBsVq2h0nzJFFNS4NhhtKVKjP5jaqQMxKjAcmUVa5S5Z5dmdL0gGkOpRdpZTKxUFzPFAjqxlIx4oY1p7supLRGDVc26tu2GVEiqzQlEKgmR8xRp9csacxzR5NX6Tm9CrxInFo2ODO1r+tDYR3ZSKmG48vr1hcF8F5D5mkhAvlzjZ9G8cNiyJK0VW/OCYRw9tz8ontrRRo=
2026-09-02 19:26:28,795 INFO  (CRMServiceNew.java:1216) - finalLeadResponse : com.mintstreet.common.bo.CRMResponse@14079981
2026-09-02 19:26:28,797 INFO  (CveProcessManagerImpl.java:1468) - OTP for Mobile Number: 919845458547 is 123456
2026-09-02 19:26:28,797 INFO  (CommunicationManagerImpl.java:41) - SMS Send Bypassed
2026-09-02 19:26:28,797 INFO  (CveLoanAction.java:340) - CveLoanAction.java :: processCBSOTP for CVE called..inputOtp:::: hNgx2BJp1LVydvi6uQhS4g==
2026-09-02 19:26:28,797 INFO  (CveLoanAction.java:345) - CveLoanAction.java :: processCBSOTP for CVE called..apiMessage:::: OTP authentication successful
2026-09-02 19:26:28,797 INFO  (CveLoanAction.java:348) - CveLoanAction.java LNo:390 :: crmResponse for CASE creation::
2026-09-02 19:26:28,798 INFO  (CveLoanAction.java:355) - CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : 14050
2026-09-02 19:26:28,798 INFO  (CveLoanAction.java:358) - CveLoanAction.java :: ApplicationFormCveLoan not found for AppSeqId : 14050
2026-09-02 19:26:28,798 INFO  (CveLoanAction.java:368) - CveLoanAction.java :: CVE Application Sequence ID = 14050
2026-09-02 19:26:28,798 INFO  (CveLoanAction.java:399) - CveLoanAction.java :: Exception while writing CVE consent to CCMS
java.lang.NullPointerException: null
        at com.mintstreet.loan.cveloan.service.CveLoanService.getApplicationFormCveLoanByAppSeqId(CveLoanService.java:26) ~[classes/:?]
        at com.mintstreet.loan.cveloan.action.CveLoanAction.getPersonalLoan(CveLoanAction.java:377) ~[classes/:?]
        at com.mintstreet.loan.cveloan.action.CveLoanAction.cveLoan(CveLoanAction.java:148) ~[classes/:?]
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[?:1.8.0_502]
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[?:1.8.0_502]
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[?:1.8.0_502]
        at java.lang.reflect.Method.invoke(Method.java:498) ~[?:1.8.0_502]
        at ognl.OgnlRuntime.invokeMethod(OgnlRuntime.java:894) ~[ognl-3.1.12.jar:?]
        at ognl.OgnlRuntime.callAppropriateMethod(OgnlRuntime.java:1539) ~[ognl-3.1.12.jar:?]
        at ognl.ObjectMethodAccessor.callMethod(ObjectMethodAccessor.java:68) ~[ognl-3.1.12.jar:?]
        at com.opensymphony.xwork2.ognl.accessor.XWorkMethodAccessor.callMethodWithDebugInfo(XWorkMethodAccessor.java:96) ~[struts2-core-2.5.10.1.jar:2.5.10.1]
        at com.opensymphony.xwork2.
