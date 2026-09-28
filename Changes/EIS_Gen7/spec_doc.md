As per Bank's data classification, this document is a confidential document, please do not
share it with unauthorised users.
This note is downloaded by AMIT DEOKAR(amit.deokar1@sbi.co.in) on 27/07/2026 10:43:41
IST
NOTE DETAILS
NOTE ID : NT/NFIN/GITC/EIS/20260721/AMDE-3942 STATUS: APPROVED
SUBJECT : Standard Operating Procedure(SOP) on Encryption & Digital Signature
COMMENT LOG
Page# Doc Reference Comment Name
AUDIT LOG
APPROVED by Sudeep Philips (DGM IT- EIS) (dgmit.eis@sbi.co.in) on 23/07/2026, 6:56 PM (IST)
RECOMMENDED by Yogesh Kumar Asst Gen. Manager(S) (agmit.eis@sbi.co.in) and submitted to Sudeep Philips (DGM 
IT- EIS) (dgmit.eis@sbi.co.in) on 23/07/2026, 5:24 PM (IST)
RECOMMENDED by Laxmi Kant (laxmi.kant2@sbi.co.in) and submitted to Yogesh Kumar Asst Gen. Manager(S) (agmit.
eis@sbi.co.in) on 23/07/2026, 11:57 AM (IST)
RECOMMENDED by Viren Wake (viren.wake@sbi.co.in) and submitted to Laxmi Kant (laxmi.kant2@sbi.co.in) on 22/07
/2026, 11:10 AM (IST)
SUBMITTED to Viren Wake (viren.wake@sbi.co.in) by AMIT DEOKAR (amit.deokar1@sbi.co.in) on 21/07/2026, 11:54 
AM (IST)
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 1 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
 
 
Standard Operating Procedure (SOP) 
on Encryption & Digital Signature 
 
Enterprise Integration Services 
 
State Bank Global IT Centre 
Issue date: 21-07-2026 
 
 
 
 
 
 
 
(Strictly for internal circulation only) 
 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 2 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
   
CONFIDENTIALITY STATEMENT 
This document is intended for SBI-GITC personnel use only. Any distribution or reproduction without the 
expressed written consent of the Owner of the document is prohibited.  The document is not to be shared 
with any individuals other than those in the Master Distribution List and those authorized by Owner of 
the document. Personnel no longer having an authorized business need to retain this document are to 
return the document in its entirety to the Owner of the document. 
 
 
 
Document History: 
Issue Date Version 
No. 
Changed requested 
by Remarks (brief details of changes) 
21.07.2026 1.4 EIS Dept Added Gen7 Logic 
27.02.2026 1.3 Recommendations 
from IT-RMD Observations provided by IT-RMD 
22.09.2025 1.2 Yearly Review Annual Review of the Document 
Document Name Encryption and Digital Signature SOP 
Document ID SBI/CTO/EIS/EDS/1.3 
Document Version 1.3 
Issue Date 21.07.2026 
Prepared & Maintained By EIS Department 
Reviewer 1 Manager (Systems) 
Reviewer 2 Chief Manager (Systems) 
Reviewer 3 AGM - EIS 
Approved By DGM (IT- EIS) 
Owner DGM (IT - EIS) 
Classification Internal 
Review Frequency Annually or as and when required 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 3 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
22.07.2024 1.1 Version 1.1 Name of the field in process flow 
changed from ‘Access token’ to 
‘Access Key’. 
26.06.2023 1.0 Original Document Encryption and Digital Signature for 
EIS. 
  
Distribution List 
Version No Date Distributed to  Purpose 
1.4 21.07.2026 GITC Internal Compliance and Audit requirements 
1.3 27.02.2026 GITC Internal Compliance and Audit requirements 
1.2 22-09-2025 GITC Internal Compliance and Audit requirements 
1.1 22.07.2024 GITC Internal Compliance and Audit requirements 
1.0 26.06.2023 GITC Internal Compliance and Audit requirements 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 4 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
Contents 
1 Purpose ................................................................................................................................................. 5 
2 Scope: .................................................................................................................................................... 5 
3 Procedure: ............................................................................................................................................. 5 
4 Pre-requisites ........................................................................................................................................ 5 
5 Implementation Process ....................................................................................................................... 5 
5.1 ENCRYPTION AND DIGITAL SIGNATURE (Gen7) ............................................................................ 5 
 Implementation and Process Flow: ...................................................................................... 5 
5.2 ENCRYPTION AND DIGITAL SIGNATURE (GEN 5 and GEN 6) ....................................................... 10 
6 Logging and Monitoring ...................................................................................................................... 13 
7 FAQs: ................................................................................................................................................... 13 
8 Dos and Don’ts .................................................................................................................................... 13 
9 Contact Details: ................................................................................................................................... 13 
10 Acronyms: ....................................................................................................................................... 13 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 5 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
1 Purpose 
The purpose of this SOP is to define standardized procedures for the implementation, management, and 
governance of encryption mechanisms and digital signature controls to ensure confidentiality, integrity, 
non-repudiation, regulatory compliance, and secure operation of information systems 
 
2 Scope: 
The scope covered under this procedure includes the API Communication to ensure safeguarding the 
data from being accessed by unauthorized users including; 
• Implementation of API governance 
• Secure API communication to ensure safeguarding the data. 
 
3 Procedure:  
1. Client ID and Secret to be included in header while invoking API calls for API governance. 
2. Gen6 Standard Security mechanism will be available for API security. 
 
4 Pre-requisites 
1. Application to be registered with EIS. 
2. Client-ID and Secret to be obtained for the application from EIS Dev Portal. 
3. Two key pairs (Public and Private) will be generated, one at EIS end and other at channel end. Public 
key for the respective Private Key will be shared between EIS and channel.    
 
5 Implementation Process  
 
5.1 ENCRYPTION AND DIGITAL SIGNATURE (Gen7) 
 Implementation and Process Flow: 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 6 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
 
 
 Note:  
EIS: - Destination Channel where API consumed for External/Internal SBI departments.  
API Consumer/Channel means the requester who is going to consume destination (i.e., EIS) API. EIS can 
also be a consumer channel in some cases where Destination will be External/Internal Departments. 
 
1) Channel has to obtain Client-ID and Secret obtained from the EIS Developer portal. 
2) Send Client ID and Secret obtained from the EIS Developer portal in the HTTP header section. 
3) Channel has to generate a 32-character plain text dynamic key (do not use Key generator function 
for generation of secret key), which will be used for encrypting the plain payload.  
4) Encrypt the plain JSON request using AES algorithm with the help of secret key generated in step 3.  
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 7 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
5) Channel will sign the plain text request using “SHA256withRSA” algorithm along with the private 
key generated (the respective public key to be shared with EIS). This will provide the digital 
signature and need to be passed in DIGI_SIGN field in request body.  
6) Encrypt the access key generated in step 3 (depicted in above diagram) with the help of shared EIS 
Public key using RSA algorithm. This encrypted access key is required to be passed in request 
header parameter named as Access Token. 
7) All the three values (obtained from Steps 4, 5, 6) must be in Base64 encoding format and shared  
with EIS at the time of request in the below mentioned format. 
Body Content:  
{   
"REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000006",   
"REQUEST": "[Outcome of Step (4)]",   
"DIGI_SIGN": "[Outcome of Step (5)]"   
 }   
   
Add the encrypted access key in the Http Header request shown below:  
Header Content:  
AccessToken - [Outcome of Step (6)]   
Client-Id: [ Obtained for application from EIS Dev Portal Step 1] 
Client-Secret: [ Obtained for application from EIS Dev Portal Step 1] 
 
8) EIS receives all above-mentioned parameters/fields in the Request.  
9) EIS will validate the client-ID and secret for the application and its entitlement/plan. 
10) If the channel is not registered or client-ID secret is not correct, channel will receive below 
response 
{ 
    "httpCode": "401", 
    "httpMessage": "Unauthorized", 
    "moreInformation": "Invalid client id or secret." 
} 
 
 
 
11) If the channel exceeds the entitlement, it will receive the below error; 
 
{ 
    "httpCode": "429", 
    "httpMessage": "Too Many Requests", 
    "moreInformation": "Assembly Rate Limit exceeded" 
} 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 8 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
12) If the access and entitlement are available/granted for the channel for that API, the system will 
move forwards to check the payload as per Gen6 standards mentioned below; 
13) EIS will decrypt the encrypted Access Key (received in header) with RSA algorithm to get the plain 
access key. 
14) EIS will decrypt the payload body using the access key obtained in step 6.  
15) EIS will verify DIGI_SIGN received in the request body with the shared Channel Public Key.  
16) Failure in Validation of request received will return ERROR_CODE SI011 with 401 HTTP code and 
the failure in DIGI_SIGN verification will return ERROR_CODE SI051 with 401 HTTP status code.  
  
{ 
"REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000011", 
   "ERROR_CODE": "SI051",  
"ERROR_DESCRIPTION": "Unable to process due to technical error!!"  
}  
Response HTTP Header  
X-Original-HTTP-Status-Code - 401  
17) On Successful decryption and verification, EIS will move on for valid request processing. Depending 
on success/failure EIS will forward the response to the Channel with 200 HTTP status code.  
 
{  
"RESPONSE": "[Encrypted Response (Encrypted with the access key received in request]",   
            "REQUEST_REFERENCE_NUMBER":  "SBISI25111900000000000010",   
"RESPONSE_DATE": "26-11-2019 13:10:17",   
"DIGI_SIGN": "[Signed Data (Singed on Plain Response)]" 
} 
  
18) Finally, channel must decrypt the RESPONSE using the same encrypted AES 256 access key which 
was used in for REQUEST and verify the DIGI_SIGN with SHA256 RSA algorithm with shared EIS 
Public Key.  
  
  
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 9 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
Algorithm Specification:  
 
AES for Payload Encryption.  
• Cipher Mode Operation: Galois/Counter Mode (GCM) with No Padding  
• Cryptographic Key: 256 bits*  
• I Vector: First 12 byte of cryptographic key (Secret Key)  
• GCM Tag Length: 16 Bytes  
 
   RSA for AES Key Encryption.  
• Cipher Mode Operation: Electronic Codebook (ECB) with OAEPPadding  
• Cryptographic Key: 2048-bit X509 Certificate  
   SHA256-RSA for Digital Signature.  
• Hashing Algorithm: SHA256  
• Cryptographic Key: 2048-bit X509 Certificate  
 
Note: ‘*’ - Kindly do not use Key generator function for generation of cryptographic key, only use 
keyboard characters of the appropriate length.  
 
* Note – RRN:  RRN refers to Request Reference Number, which is a 25-digit reference number 
generated and ensured by the channel. 
 
REFERENCE SCREENSHOTS:  
  
➢ Prepare JSON request using encrypted payload and encrypted key as shown below  
 
 
 
 
 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 10 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
NOTE: - Payload is passed in body as REQUEST field, Signature is passed in body as DIGI_SIGN field and 
encrypted access key in header as Access Token.   
(” Access Token” is only field name in which key is passed) 
➢ Encrypted response format after successful processing of the request.  
  
 
Authorization/Authentication Failure response format before processing the request.  
  
 
 
Validation Failure response format before processing the request.  
  
 
  
5.2 ENCRYPTION AND DIGITAL SIGNATURE (GEN 5 and GEN 6) 
Pre-requisites:  
Channel has to generate a pair of keys (Private and Public) and Public Key need to be shared with EIS.  
 
Algorithm Specification:  
 AES for Payload Encryption.   
• Cipher Mode Operation:  
o Gen5- Cipher Block Chaining (CBC) with PKCS5 Padding and  
o Gen6- Galois/Counter Mode (GCM) with No Padding for GCM Tag Length: 16 Bytes  
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 11 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
• Cryptographic Key             : 256 bits    
• IVector                                 : First 12 byte of cryptographic key (Secret Key)    
 
RSA for AES Key Encryption.  
• Cipher Mode Operation   : Electronic Codebook (ECB) with OAEPPadding  
• Cryptographic Key             : 2048-bit X509 Certificate  
    
SHA256-Rivest-Shamir-Adleman (RSA) for Digital Signature.   
• Hashing Algorithm      :  SHA 256  
• Cryptographic Key      : 2048-bit X509 Certificate 
  
Implementation and Process Flow:  
1) Channel has to generate a 32-character plain text dynamic key (AES 256 encryption key) for the 
payload encryption.  
2) Encrypt the plain JSON request with above AES 256 encryption key.  
3) SHA256-RSA algorithm has to be used to sign the plain request payload with the help of Channel 
Private Key.  
4) RSA algorithm has to be used to encrypt the above AES 256 encryption key with the help of 
shared EIS Public Key.  
5) All the three values (obtained from Steps 2, 3, 4) must be in Base64 encoding format and shared 
with EIS at the time of request in the below mentioned format.   
{  
"REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000006",  
"REQUEST": "[Outcome of Step (2)]",  
"DIGI_SIGN": "[Outcome of Step (3)]"  
}   
Add the secret key in the Http Header request  
AccessToken - [Outcome of Step (4)]  
 
(” Access Token” is only field name in which key is passed) 
  
6) EIS will validate the request received and decrypt the Access Key with RSA algorithm and then 
proceed with decryption of REQUEST using the AES encryption key obtained from the decryption 
of Access Key and verification of DIGI_SIGN with the shared Channel Public Key.  
7) Failure in Validation of request received will return ERROR_CODE SI011 with 401 HTTP code and 
the failure in decryption/verification will return ERROR_CODE SI051 with 401 HTTP code.   
{  
    "REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000011",  
    "ERROR_CODE": "SI051",  
    "ERROR_DESCRIPTION": "Unable to process due to technical error!!"  
}  
Response HTTP Header  
     X-Original-HTTP-Status-Code - 401  
8) On Successful decryption/verification EIS will move on for valid request processing. Depending on 
success/failure EIS will forward the response to the Channel with 200 HTTP code.    
{ 
    "REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000010",  
    "RESPONSE_DATE": "26-11-2019 13:10:17",  
    "DIGI_SIGN": "[Signed Data (Singed on Plain Response)]"  
}  
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 12 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
  
9) Finally, channel must decrypt the RESPONSE using the same AES 256 encryption key which was 
used in for REQUEST and verify the DIGI_SIGN with SHA256 RSA algorithm with shared EIS Public 
Key.   
 
REFERENCE SCREENSHOTS:  
➢ Prepare JSON request using encrypted payload and encrypted key as shown below  
 
NOTE: - Payload is passed in body as REQUEST field, Signature is passed in body as DIGI_SIGN field 
and encrypted key in header as AccessToken.  
➢ Encrypted response format after successful processing of the request.  
  
➢ Authorization/Authentication Failure response format before processing the request.  
  
 
 
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
Encryption and Digital Signature                                                                                      EIS Department  
 
                                                                        
Encryption & Digital Signature                                          Internal                                                     Page 13 of 13       
Version – 1.3                                                                                                                                            21-07-2026 
 
➢ Validation Failure response format before processing the request.  
   
 References:  
• Policy & Standards Ver11.0 
• SBI Cyber Security Policy & Standards Ver 7.0 
 
6 Logging and Monitoring 
Logging and Monitoring ensure that all important security-related activities are recorded and 
continuously monitored to detect unauthorized access or misuse. 
➢ Transaction logs are integrated with the Security Operations Center (SOC). 
➢ If there is a failure in client credential authentication (such as an invalid Client ID or Client 
Secret) or Digital Signature verification (DigiSign) fails, the event is logged. 
➢ These logs are forwarded to the SOC team, where the Security Information and Event 
Management (SIEM) system analyzes them. 
➢ If suspicious activity is detected, the SOC generates an alert so that the security team can 
investigate and take appropriate action. 
 
7 FAQs: 
NA 
8 Dos and Don’ts 
NA 
9 Contact Details: 
   Email - sbisi@sbi.co.in 
10 Acronyms: 
 
1. AES - Advanced Encryption Standard 
2. RSA - Rivest-Shamir-Adleman 
3. SHA - Secure Hash Algorithm 
4. RRN - Request Reference Number 
 
****************** End Of Document ******************   
EasyApproval - Approved
Note Id: NT/NFIN/GITC/EIS/20260721/AMDE-3942
