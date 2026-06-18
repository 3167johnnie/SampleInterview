                             
SBI Confidential  1 of 19 
Centralized Consent Management System (CCMS) 
API Specification (unencrypted request-response) 
 
 
 
 
 
Document Control  
Sr. No. Information Type Value 
1 Document Title API specification of Centralized Consent Management System 
(CCMS) 
2 Document Code CCMS-API 
3 Version V1.2 
4 Release Date 24.04.2026 
5 Document Owner IT-CRM, Software Factory 
6 Document Access Confidential 
7 Document Audience Integrating applications of the bank (Data Fiduciary applications 
/Channels/Touchpoints) 
 
 
Revision History 
Version 
No. 
Revised By 
(Name / Designation / 
Department) 
Revision 
Date 
Description 
1.0 
[draft 
RC] 
Software Factory, 
GITC 
11.11.2025 Initial draft describing the APIs exposed by CCMS for 
consumption by channels/consent touchpoints (Data 
Fiduciary and Data Processing applications) through 
EIS layer. 
1.0 Software Factory, 
GITC 
31.12.2025 As per meeting dated 30.12.2025, headed by DMD (IT) 
sir, 1) Consent validation/consumption APIs are 
removed, 2) Privacy notice API endpoint is removed – 
privacy notice, whenever changes, will be pushed to 
channels/touchpoints which should be stored by 
channels locally, 3) For NTB, DOB field is added. 
1.1 Software Factory, 
GITC 
06.03.2026 Mobile number field’s maximum allowed length 
increased to 17. Miscellaneous improvements. 
1.2 Software Factory, 
GITC 
24.04.2026 NTB consent retrieval facility. 
 
                             
SBI Confidential  2 of 19 
Contents 
Document Control ......................................................................................................................................... 1 
Revision History ............................................................................................................................................. 1 
1. Executive Summary ................................................................................................................................... 3 
2. Pre-Requisites for Integrating Applications ............................................................................................... 3 
3. Standard API Request and Response ......................................................................................................... 4 
3.1. Request ................................................................................................................................................... 4 
3.1.1. Headers ........................................................................................................................................... 4 
3.1.2. Request Parameters ........................................................................................................................ 4 
3.2. Response ................................................................................................................................................. 4 
3.2.1. Response for Accepted Request ..................................................................................................... 4 
3.2.1. Response for Unprocessed Request ............................................................................................... 5 
4. API Endpoints ............................................................................................................................................. 6 
4.1. Purpose Enquiry ...................................................................................................................................... 6 
4.1.1. API Details ....................................................................................................................................... 6 
4.1.1. API Inputs ........................................................................................................................................ 7 
4.1.2. API Outputs ..................................................................................................................................... 8 
4.2. Consent Write (Capture, Modification, Revocation) .............................................................................. 9 
4.2.1. API Details ..................................................................................................................................... 10 
4.2.2. API Inputs ...................................................................................................................................... 11 
4.2.3. API Outputs ................................................................................................................................... 13 
4.3. Consent Read with Purpose Metadata ................................................................................................. 13 
4.3.1. API Details ..................................................................................................................................... 13 
4.3.2. API Inputs ...................................................................................................................................... 15 
4.3.3. API Outputs ................................................................................................................................... 15 
5. API Response Codes ................................................................................................................................. 17 
6. Appendix .................................................................................................................................................. 17 
 
  
1. Executive Summary 
This document defines the technical and functional specifications for the CCMS APIs, which provide a 
standardized interface for data principal’s consent capturing, modification, revocation, and validation. 
The API enables authorized internal applications and services of the bank to manage data principals’ 
consents in a centralized solution through consistent and predictable endpoints. 
The APIs are designed according to REST architectural principle. They use JSON as the primary data 
interchange format and adheres to the compliance policies.  
This specification details the API’s endpoint definitions, data models, versioning strategy, error 
handling, and performance considerations. It serves as the authoritative reference for developers, 
architects, and system administrators responsible for building, integrating, and maintaining data 
fiduciary applications of the bank. 
2. Pre-Requisites for Integrating Applications 
The integrating applications are required to complete below pre-requisites before they can start use of 
CCMS APIs for consent management. 
1) Channel/Touchpoint ID: Get the application onboarded by contacting the CCMS team. The CCMS 
team will provide a channel/touchpoint ID. 
2) Purposes: A ‘purpose’ refers to the granular explanation of intended use of personal data of data 
principals. The purposes should be created in CCMS against which the consent is to be captured. 
The CCMS team will assess and advise if new purpose(s) is/are required to be created or existing 
purpose(s) can be utilized. 
3) Purpose Set: A ‘purpose set’ is a logical categorization of the purposes which may or may not 
include all the purposes available in CCMS. The integrating application may need to show a limited 
number of purposes as applicable to a specific journey in the system’s workflow. The integrating 
application team should get a purpose set created as applicable to each of their journey if purposes 
are varying across journeys. The CCMS team shall provide a random string as purpose set code/Id 
which the integrating application will include in payload to fetch the purposes configured under 
the purpose set. 
Outcome: The integrating application has 1) all desired purposes created in CCMS, 2) purposes are 
categorized under one or more purpose sets with desired sort order, 3) Obtained ‘Channel/Touchpoint 
ID’ and ‘Purpose Set Id’ from CCMS Team. 
The next step is to get the API endpoints configured in EIS layer and integrate as per EIS standards and 
recommendations. 
SBI Confidential  
3 of 19 
3. Standard API Request and Response 
3.1. Request 
3.1.1. Headers 
Header 
Description 
Accept-Language To indicate the expected language by 
client. Supports 22 Indian scheduled 
languages plus English (code: eng) as 
default language. For full list, please 
refer Language Master in Appendix. 
Mandatory Type Length / Range / 
Format / Enum 
No 
string 
3  
lowercase ISO 639
3 code 
X-Correlation-Id 
An Id to track a request. The client 
must generate UUID (Universally 
Unique Identifier) for correlation Id.  
Yes 
string 
36  
RFC 
compliant 
4122 
UUID 
(mandatory v7) 
X-API-Version 
For accessing the APIs with specific 
version. The version number shall be 
advised by the CCMS team as and 
when changes. 
No 
int 
Natural numbers 
1,2, etc. 
Default: 1 
3.1.2. Request Parameters 
Please refer to the respective API endpoints in respective section. 
3.2.  Response 
3.2.1. Response for Accepted Request 
Response Payload (Plain) 
{ 
success: boolean,  indicates whether request was successfully 
processed or not 
statusCode: integer,  3 digits HTTP Status Code 
messages?: string[] | null,  Messages for client 
locale: string,  3 letter lowercase ISO 639-3 code  
errors?: {  Syntactic validation errors 
fieldName: "ErrorMessage", 
fieldName2: "ErrorMessage" 
} | null, 
body?: object | object[] | null,  Response payload which may vary 
as per endpoint 
timestamp: string,  format YYYY-MM-DDTHH:MM:SSZ 
correlationId: string  same as received in header from API client 
or autogenerated by CCMS 
} 
SBI Confidential  
4 of 19 
                             
SBI Confidential  5 of 19 
 
Field Description Mandatory Type Length / Range / 
Format / Enum 
success To indicate whether the request 
was processed successfully. 
Yes Boolean true, false 
statusCode HTTP response status code. Yes Integer 3 
 
messages Messages from the API server – 
mostly validation errors. 
No string 
array 
Array of strings. 
locale The locale in which the API has 
provided the response. It will 
generally as per the client’s 
provided language preference in 
Accept-Language header. 
Yes string 3  
lowercase ISO 639
3 code 
errors Validation errors against request 
parameters. 
No Object JSON object or null 
body Response payload of API. No Object JSON object or null 
timestamp Request processing timestamp. Yes string YYYY-MM
DDTHH:MM:SSZ 
correlationId The correlation Id which was 
produced by the client and sent in 
request header X-Correlation-Id. 
Yes string 36  
RFC 4122 
compliant UUID 
(mandatory v7) 
 
3.2.1. Response for Unprocessed Request 
This response may be expected in encryption failure, invalid X-Correlation-Id, or any error at CCMS end. 
Response Payload (Plain) 
{  
  success: false, 
  statusCode: integer, 
  messages: string[], 
  locale: string, 
  errors: null, 
  body: null, 
  timestamp: string,  
  correlationId: string 
} 
 
Field Description Mandatory Type Length / Range / 
Format / Enum 
success To indicate whether the request 
was processed successfully. 
Yes Boolean false 
statusCode HTTP response status code. Yes Integer 3 
 
                             
SBI Confidential  6 of 19 
messages Messages from the API server – 
mostly validation errors. 
No string 
array 
Array of strings or 
null 
locale The locale in which the API has 
provided the response. It will 
generally as per the client’s 
provided language preference in 
Accept-Language header. 
Yes string 3  
lowercase ISO 639
3 code 
errors Validation errors against request 
parameters. 
No Object null 
body Response payload of API. No Object Null 
timestamp Request processing timestamp. Yes string YYYY-MM
DDTHH:MM:SSZ 
correlationId The correlation Id which was 
produced by the client and sent in 
request header X-Correlation-Id. 
In case of error in the provided 
correlation Id, the API server may 
generate and provide an Id. 
No string 36  
RFC 4122 
compliant UUID 
(mandatory v7) 
 
 
4. API Endpoints 
4.1. Purpose Enquiry 
The API provides the purpose master from CCMS as per the group and order of purposes configured in 
the CCMS under a purpose set. 
4.1.1. API Details 
HTTP Method POST 
Content Type application/json 
API Request Payload  { 
    "touchPointId": "value", 
    "purposeSetId": "value" 
} 
API Response Payload  {  
    … 
    … 
    "body":  
    [ 
        { 
            "isContainer": value, 
            "containerTitle": "value", 
            "containerDescription": "value", 
            "purposes":  
            [ 
                { 
                             
SBI Confidential  7 of 19 
                    "code": "value", 
                    "version": value, 
                    "title": "value", 
                    "description": "value", 
                    "isEssential": value, 
                    "isProductSpecific": value, 
                    "isNTB": value, 
                    "isETB": value, 
                    "hasValidity": value, 
                    "validityDays": value, 
                    "dataPoints":  
                     [ 
                        "value", 
                        "value", 
                        … 
                    ], 
                    "bankProducts":  
                     [ 
                        {"code": "value", "label": "value"},  
                        {"code": "value", "label": "value"},  
                        … 
                    ] 
                }, 
                … 
            ] 
        }, 
        { 
            <NEXT PURPOSE CONTAINER> 
        }, 
        … 
    ], 
    … 
    … 
} 
Error Payload Please refer the standard responses above. 
 
4.1.1. API Inputs 
Field Description Mandatory Type Length / Range / 
Format / Enum 
touchPointId 
The Id/Code provided by CCMS 
team during application 
integration onboarding. 
Yes string 1-10 
                             
SBI Confidential  8 of 19 
purposeSetId 
The Id/Code provided by CCMS 
team for referring a logical group 
of purposes. 
Yes string 1-100 
 
4.1.2. API Outputs 
In the body of the response below parameters shall be provided. 
Field Description Mandatory 
in Output Type 
Length / 
Range / 
Format / Enum 
isContainer 
Indicates whether the current 
item is a purpose container. More 
about purpose container available 
in appendix. 
Yes boolean true, false 
containerTitle Concise title to display to end 
user. No string 0-255 
containerDescription Description to show to end user. No string 0-4000 
purposes Array of purposes  Yes Object JSON 
 
Individual parameter inside ‘purposes’ field above 
Field Description Mandatory 
in Output Type  Length / Range / 
Format / Enum 
code Internal code/identifier of the 
purpose. Yes string 1-100 
version Latest version number of the 
purpose. Yes int 1-99999 
title  Title of the purpose Yes string 1-255 
description 
Description of the purpose and may 
show privacy notice with respect to 
the purpose 
Yes string 1-4000 
isEssential 
Boolean flag to indicate if the 
purpose is essential and the data 
principal must select/provide 
consent for it. The validation should 
essentially force the user to choose 
this purpose instead of showing it 
pre-selected. 
Yes boolean true, false 
                             
SBI Confidential  9 of 19 
isProductSpecific Whether consent is to be captured 
against individual bank products. Yes boolean true, false 
isNTB Future scope. May indicate if the 
purpose is applicable to NTB users. Yes boolean true, false 
isETB Future scope. May indicate if the 
purpose is applicable to ETB users. Yes boolean true, false 
hasValidity Indicates if the consent against the 
respective purpose can have expiry. Yes boolean true, false 
validityDays 
Base number of days for calculating 
the consent expiry date. If 
hasValidity is false, this field has no 
significance. 
No int Null or 1-36500 
dataPoints 
The data points of the user which 
are supposed to be used if the user 
provides consent for this purpose. 
Yes string[] 
Array of strings 
String size: 1-50 
bankProducts 
If consent is to be captured against 
individual products of the bank, the 
value of this field may be used to 
show as selectable items. 
Yes Object[] Array of JSON 
 
Individual parameter inside ‘bankProducts’ field above 
Field Description Mandatory 
in Output Type  Length / Range / 
Format / Enum 
code Internal code/Id of the bank 
product. Conditional string 1-50 
label Label/text to show to end user/data 
principal while capturing consent. Conditional string 1-50 
 
4.2. Consent Write (Capture, Modification, Revocation) 
This API endpoint should be used for consent capture, modification, or revocation.  In the request payload, 
the integrating applications have to ensure that it sends the status against each purpose. 
On success, a message may be shown to the end user which should explain the time required for the bank 
to consider any consent revocation. 
Consent captured/modified/revoked against an NTB (using ntbId) will remain confined to the touchpoint. 
If two channels/touchpoints register consent(s) against the same NTB ID, the consent(s) will still be 
treated separate while enquiring. 
 
                             
SBI Confidential  10 of 19 
4.2.1. API Details 
HTTP Method POST 
Content Type application/json 
API Request Payload  { 
    "touchPointId": "value", 
    "purposeSetId": "value", 
    "dpData":  
    { 
        "dpCIF": "value", 
        "ntbId": "value", 
        "dpMobile": "value", 
        "dpEmail": "value", 
        "dpDOB": "value", 
        "dpIPAddress": "value", 
        "locale": "value", 
        "timestamp": "value" 
    }, 
    "consents":  
     [ 
        { 
            "purposeCode": "value", 
            "purposeVersion": value, 
            "consented": value, 
            "consentedProducts": ["value", "value", …], 
            "notConsentedProducts": ["value", "value", …] 
        }, 
        { 
            <NEXT CONSENT> 
        }, 
        … 
    ] 
} 
API Response Payload  {  
    … 
    … 
    "body":  
    { 
        "dpCIF": "value", 
        "ntbId": "value", 
        "consentId": "value" 
    }, 
    … 
    … 
} 
Error Payload Please refer the standard responses above. 
 
                             
SBI Confidential  11 of 19 
4.2.2. API Inputs 
 
Field Description Mandatory Type Length / Range / 
Format / Enum 
touchPointId 
The Id/Code provided by CCMS 
team during application 
integration onboarding. 
Yes string 1-10 
purposeSetId 
The purpose set Id/Code used by 
the touchpoint/channel to fetch 
purposes from CCMS and display 
to data principal/end user. 
Yes string 1-100 
dpData 
Data of data principal/end user to 
uniquely identify the DP and store 
metadata for audit trail. 
Yes Object JSON 
consents 
Action taken by DP/end user 
against the requested consents 
along with purpose information 
which was displayed to DP/end 
user. 
Yes Object[] JSON 
 
Individual parameter inside ‘dpData’ and ‘consents’ fields above 
dpCIF field must be present for all ETB journeys, or wherever available.  
For ‘NTB’ cases, ntbId is mandatory. ntbId should be generated by the source application and preferably 
the same which the source application uses to uniquely identify its users. 
dpCIF and ntbId are mutually exclusive fields. 
dpMobile, dpEmail, and dpDOB fields should be included whenever available for the NTB cases. 
Field Description Mandatory Type  Length / Range / 
Format / Enum 
dpCIF For bank’s customers, CIF 
must be sent. Conditional string 13-17 
ntbId 
ID generated by Data 
Fiduciary to identify the 
consent when CIF is not 
available. 
Conditional string 1-255 
dpMobile 
Mobile number of data 
principal. Valid characterset 
for dpMobile is +(only at 
No string 10-17 
                             
SBI Confidential  12 of 19 
beginning,optional) and 0-9 
numbers. 
dpEmail Email address of data 
principal. No string 5-255 
dpDOB Date of birth of the data 
principal. No string YYYY-MM-DD 
dpIPAddress IP address of the data 
principal’s device. Yes string 7-45 
locale 
The language preference 
selected by data principal 
while submitting consent. The 
language preference selected 
by data principal while 
submitting consent. Values 
will be from Language Master 
provided. 
Yes string 3 
timestamp 
The timestamp at which the 
consent was submitted by 
data principal. 
Yes string YYYY-MM
DDTHH:MM:SSZ 
purposeCode 
The code/Id of purpose 
received from CCMS when 
purposes/consents of user 
were fetched.  
Yes string 1-100 
purposeVersion 
The version number of the 
purpose as received from 
CCMS when 
purposes/consents of user 
were fetched. 
Yes int 1-99999 
consented 
This field determines the 
status of consent against a 
purpose. 
Yes boolean true, false 
consentedProducts 
If bank products were 
presented to data principal 
for individual product level 
consent capture, the code of 
the bank products as received 
from CCMS should be sent. 
Conditional string[] 
Individual string 
element length: 
1-100 
                             
SBI Confidential  13 of 19 
notConsentedProducts 
If bank products were 
presented to data principal 
for individual product level 
consent capture, the code of 
the bank products should be 
sent which were not selected 
by the data principal. 
Conditional string[] 
Individual string 
element length: 
1-100 
 
4.2.3. API Outputs 
In the body of the response below parameters shall be provided. 
Field Description Mandatory 
in Output Type 
Length / 
Range / 
Format / Enum 
dpCIF CIF field as advised in request payload. Conditional string 13-17 
ntbId ntbId field as advised in request payload. Conditional string 1-255 
consentId A reference ID generated by CCMS. Yes string 
36  
RFC 4122 
compliant 
UUID 
(mandatory 
v7) 
 
4.3. Consent Read with Purpose Metadata 
The API endpoint provides the latest consent status against the identifier of the data principal – CIF or 
NTB ID. The API provides the respective purpose information along with present consent status of the 
enquired data principal. This API may be used by the touchpoints which need to present the consent 
status to data principals or validate consent for a single data principle through API. 
When enquired by -  
• dpCIF: API response will provide present consent status regardless the source channel which had 
captured/modified/revoked the consent(s). Consent shared across touchpoints/channels. 
• ntbId: API response will provide present consent status of the ntbId as provided by the 
touchPointId. The consent status of the NTB will not be shared across touchpoints/channels. 
dpCIF and ntbId are conditionally optional fields. 
4.3.1. API Details 
HTTP Method POST 
Content Type application/json 
API Request Payload  { 
                             
SBI Confidential  14 of 19 
    "touchPointId": "value", 
    "purposeSetId": "value", 
    "dpCIF": "value", 
    "ntbId": "value" 
} 
API Response Payload  {  
    … 
    … 
    "body":  
    { 
        "dpCIF": "value", 
        "ntbId": "value", 
        "dpConsent":     
        [ 
        { 
            "isContainer": value, 
            "containerTitle": "value", 
            "containerDescription": "value", 
            "purposes":  
             [ 
                { 
                    "code": "value", 
                    "version": value, 
                    "title": "value", 
                    "description": "value", 
                    "isEssential": value, 
                    "isProductSpecific": value, 
                    "isNTB": value, 
                    "isETB": value, 
                    "hasValidity": value, 
                    "validityDays": value, 
                    "dataPoints": [ 
                        "value", 
                        "value", 
                        … 
                    ], 
                    "bankProducts":  
                     [ 
                        {"code": "value", "label": "value"},  
                        {"code": "value", "label": "value"},  
                        … 
                    ], 
                    "consentInfo":  
                    { 
                        "status": "value", 
                        "consentId": "value", 
                        "dpIPAddress": "value", 
                             
SBI Confidential  15 of 19 
                        "locale": "value", 
                        "timestamp": "value", 
                        "validTill": "value", 
                        "touchpointInfo":  
                        { 
                            "orgName": "value", 
                            "appName": "value", 
                            "deptName": "value" 
                        }, 
                        "consentedProducts": ["value", "value", …] 
                    } 
                }, 
                … 
            ] 
        }, 
        { 
            <NEXT PURPOSE CONTAINER> 
        }, 
        … 
    ] 
    }, 
    … 
    … 
} 
Error Payload Please refer the standard responses above.  
4.3.2. API Inputs 
Field Description Mandatory Type Length / Range / 
Format / Enum 
touchPointId 
The Id/Code provided by CCMS 
team during application 
integration onboarding. 
Yes string 1-10 
purposeSetId 
The purpose set Id/Code under 
which the desired set of purposes 
configured. 
Yes string 1-100 
dpCIF 
For bank’s customers, CIF must 
be sent to CCMS for enquiring the 
present consent status. 
Conditional string 13-17 
ntbId 
ID of NTB generated by Data 
Fiduciary while consent capture / 
modification / revocation. 
Conditional string 1-255 
 
4.3.3. API Outputs 
                             
SBI Confidential  16 of 19 
Please refer the ‘Purposes’ API for the common fields inside ‘dpConsent’ field. Along with each 
‘purpose’ array element in dpConsent, respective consent status, if available, is provided in 
‘consentInfo’ field. 
Field Description Mandatory 
in Output Type 
Length / 
Range / 
Format / Enum 
consentInfo Object representing the consent status of 
the enquired data principal ID in request. No Object JSON 
status Consent status. No string 
Active, 
Inactive, 
Expired 
consentId A reference ID generated by CCMS while 
capturing/modifying/revoking consent. No string 
36  
RFC 4122 
compliant 
UUID 
(mandatory 
v7) 
dpIP IP address of the data principal’s device 
from which consent was updated. No string 7-45 
locale 
The language preference selected by data 
principal while submitting consent. 
Values will be from Language Master 
provided. 
No string 3 
timestamp The timestamp at which the consent was 
submitted by data principal. No string 
YYYY-MM
DDTHH:MM:SS
Z 
validTill 
Validity of the consent / consent expiry 
time. If consent is perpetual 
(hasValidity=false for respective 
purpose), validTill would be null. 
No string 
null / 
YYYY-MM
DDTHH:MM:SS
Z 
touchpointInfo 
The source/data fiduciary application 
through which consent was 
captured/modified/revoked.  
No Object JSON 
orgName Organization name of the 
touchpoint/channel. No string 0-100 
appName Application name of the 
touchpoint/channel. No string 0-100 
                             
SBI Confidential  17 of 19 
deptName Department name of the 
touchpoint/channel. No string 0-100 
 
5. API Response Codes 
HTTP Status Code  Response 
200 
Success 
Successful response as per the API details of the 
respective API 
200 
Response payload statusCode 400 
Input errors 
{ 
 success: false, 
 statusCode: 400, 
 messages: string[], 
 locale: string, 
 errors: null, 
 body: null, 
 timestamp: string,  
 correlationId: string 
} 
200 
Response payload statusCode 422 
Unprocessable entity 
{ 
 success: false, 
 statusCode: 422, 
 messages: string[], 
 locale: string, 
 errors: null, 
 body: null, 
 timestamp: string,  
 correlationId: string 
} 
500 
Internal server error 
{ 
 success: false, 
 statusCode: 500, 
 messages: string[], 
 locale: string, 
 errors: null, 
 body: null, 
 timestamp: string,  
 correlationId: string 
} 
 
6. Appendix 
Label Description 
Data Principal An individual to whom the personal data relates. They have the right to give, manage 
and withdraw consent for data processing. 
                             
SBI Confidential  18 of 19 
Data Fiduciary Any person or entity that determines the purpose and means of processing personal 
data and is responsible for obtaining and managing consent in compliance with the 
DPDP Act. 
Data Processor A person or entity that processes personal data on behalf of a Data Fiduciary, 
following their instructions. 
Channel An application/interface through which data principal manages consents. Channel 
and touchpoint words are used interchangeably.  
Touchpoint An application/interface through which data principal manages consents. Channel 
and touchpoint words are used interchangeably. 
X-Correlation-Id X-Correlation-Id is used for uniquely identifying each request for 
tracking/debugging. Although, it is not forced to be unique and not necessarily to be 
as per the specified compliance, it is the responsibility of the source party to 
generate it as suggested.  
The validation check will ensure a string of 36 characters total (8-4-4-4-12 groups). 
Language 
Master 
Code Language Name 
asm Assamese 
ben Bengali 
brx Bodo 
dgo Dogri 
eng English 
guj Gujarati 
hin Hindi 
kan Kannada 
kas Kashmiri 
kok Konkani 
mai Maithili 
mal Malayalam 
mni Manipuri 
mar Marathi 
nep Nepali 
ory Odia 
pan Punjabi 
san Sanskrit 
sat Santali 
snd Sindhi 
tam Tamil 
tel Telugu 
urd Urdu 
 
Purpose Description of use of data obtained from a person. Purpose verbiage against which 
a consent is requested to the user, e.g., Email Marketing. 
Consent Data principal’s concurrence to process/use the personal data as mentioned in 
purpose statement. 
Purpose 
Container 
A purpose container groups similar kind of purposes.  
Example: Purpose Container: Marketing, Member Purpose 1: Email Marketing, 
Member Purpose 2: SMS Marketing. 
Illustration of Mix of Granular Purposes (1,2,5) and Grouped Purposes (3,4) 
| Purpose 1 
| Purpose 2 
| Purpose Container 1 
| Purpose 3 
| Purpose 4 
| Purpose 5 
Container Nesting Level: 0, i.e. there won’t be any containers inside a container. 
Purpose Set 
A virtual categorization of purposes and purpose containers. A purpose set will 
enable the API consumers to fetch a specific set of purposes as per the context in 
their user journey. 
Purpose sets will be maintained by the CCMS admin team, and they will provide a 
purpose set ID after mapping the required purposes and purpose containers to the 
purpose set. 
ETB 
Existing to Bank 
New to Bank 
NTB --  End of Document  -- 
SBI Confidential  
19 of 19 
