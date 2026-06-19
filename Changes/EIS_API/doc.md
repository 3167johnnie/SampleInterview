ENCRYPTION AND DIGITAL SIGNATURE 
Pre-requisites: 
Two key pairs (Public and Private) will be generated, one at EIS end and other at channel end. Public key for 
the respective Private Key will be shared between EIS and channel.   
Implementation and Process Flow: 
1) Channel has to generate a 32 character plain text dynamic key (do not use Key generator 
function for generation of secret key), which will be used for encrypting the plain payload. 
2) Encrypt the plain JSON request using AES algorithm with the help of secret key generated in step 1. 
3) Channel will sign the plain text request using “SHA256withRSA” algorithm along with the private 
key generated (the respective public key to be shared with EIS). This will provide the digital signature 
and need to be passed in DIGI_SIGN field in request body. 
4) Encrypt the secret key generated in step 1 with the help of shared EIS Public key using RSA algorithm. 
The generated value needs to be passed in request header parameter AccessToken. 
5) All the three values (obtained from Steps 2, 3, 4) must be in Base64 encoding format and shared 
with EIS at the time of request in the below mentioned format. 
{ 
} 
"REQUEST_REFERENCE_NUMBER": "SBISI25111900000000000006", 
"REQUEST": "[Outcome of Step (2)]", 
"DIGI_SIGN": "[Outcome of Step (3)]" 
Add the encrypted secret key in the Http Header request 
AccessToken - [Outcome of Step (4)] 
6) EIS will validate the request received and decrypt the AccessToken with RSA algorithm to get the    
secret key (AES Key). 
7) Decryption of REQUEST using the secret key obtained from decryption of AccessToken in step 7. 
8) EIS will verify DIGI_SIGN received in the request body with the shared Channel Public Key. 
9) Failure in Validation of request received will return ERROR_CODE SI011 with 401 HTTP code and the 
failure in DIGI_SIGN verification will return ERROR_CODE SI051 with 401 HTTP status code. 
{ 
} 
"REQUEST_REFERENCE_NUMBER": 
"ERROR_CODE": "SI051", 
"SBISI25111900000000000011", 
"ERROR_DESCRIPTION": "Unable to process due to technical error!!" 
Response HTTP Header 
X-Original-HTTP-Status-Code - 401 
10) 
On Successful decryption and verification, EIS will move on for valid request processing. 
Depending on success/failure EIS will forward the response to the Channel with 200 HTTP status code. 
{ 
"RESPONSE": "[Encrypted Response (Encrypted with the key obtained from the decryption of 
AccessToken)]", 
"REQUEST_REFERENCE_NUMBER": 
"RESPONSE_DATE": "26-11-2019 13:10:17", 
"SBISI25111900000000000010", 
"DIGI_SIGN": "[Signed Data (Singed on Plain Response)]" 
} 
11) 
Finally channel must decrypt the RESPONSE using the same AES 256 encryption key (secret key) 
which was used in for REQUEST and verify the DIGI_SIGN with SHA256 RSA algorithm with shared EIS 
Public Key. 
Algorithm Specification: 
Advanced Encryption Standard (AES) for Payload Encryption. 
• Cipher Mode Operation    : Galois/Counter Mode (GCM) with No Padding 
• Cryptographic Key 
• IVector 
• GCM Tag Length 
: 256 bits* 
: First 12 byte of cryptographic key (Secret Key) 
: 16 Bytes 
Rivest-Shamir-Adleman (RSA) for AES Key Encryption. 
• Cipher Mode Operation    : Electronic Codebook (ECB) with OAEPPadding 
• Cryptographic Key 
: 2048 bit X509 Certificate 
SHA256-Rivest-Shamir-Adleman (RSA) for Digital Signature. 
• Hashing Algorithm        
• Cryptographic Key        
: SHA 256 
: 2048 bit X509 Certificate 
REFERENCE SCREENSHOTS: 
➢ Prepare JSON request using encrypted payload and encrypted key as shown below 
NOTE: - Payload is passed in body as REQUEST field, Signature is passed in body as DIGI_SIGN field and 
encrypted key in header as AccessToken.  
➢ Encrypted response format after successful processing of the request. 
Authorization/Authentication Failure response format before processing the request. 
➢ Validation Failure response format before processing the request. 
* Note: Kindly do not use Key generator function for generation of cryptographic key, only use keyboard 
characters of the appropriate length.  
