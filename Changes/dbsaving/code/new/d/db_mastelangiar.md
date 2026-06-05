select * from SBI_TEST.RUPEEPOWER_OCAS_T_13703 where PRIVACY_IS_ACTIVE='Y' and PRIVACY_LOCALE='eng';

---Home Loan form  ----
select * from RUPEEPOWER_OCAS_T_00195;

desc RUPEEPOWER_OCAS_T_00195;



ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_NTB_ID VARCHAR2(255);

commit;

select APP_NTB_ID,APP_PRIVACY_CONSENT_FLAG from SBI_TEST.RUPEEPOWER_OCAS_T_00195


ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00195
ADD APP_PRIVACY_CONSENT_TIME DATE;


--Privacy Notices  request response 

select * from SBI_TEST.RUPEEPOWER_OCAS_T_13703;

--Language master consent privacy 
select * from SBI_TEST.RUPEEPOWER_OCAS_T_13704;

select * from SBI_TEST.RUPEEPOWER_OCAS_T_10815

MASTER project
select * from SBI_TEST.RUPEEPOWER_OCAS_T_20118
desc SBI_TEST.RUPEEPOWER_OCAS_T_20118

desc SBI_TEST.RUPEEPOWER_OCAS_T_13704;
------------------------------------------------------------------
App Form 
APP_FORM_HOME_LOAN_QUOTE' => 'RUPEEPOWER_OCAS_T_00255',
'APP_FORM_HOME_LOAN' => 'RUPEEPOWER_OCAS_T_00195',
'APP_FORM_HOME_LOAN_CALLS' => 'RUPEEPOWER_OCAS_T_00224',

select * from SBI_TEST.RUPEEPOWER_OCAS_T_00255;
desc SBI_TEST.RUPEEPOWER_OCAS_T_00255;
cx

--------HOME LOAN 
select * from SBI_TEST.RUPEEPOWER_OCAS_T_00195;
desc SBI_TEST.RUPEEPOWER_OCAS_T_00195;

select * from SBI_TEST.RUPEEPOWER_OCAS_T_00195 where APP_REFERENCE_ID='110626000001';


ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_PRIVACY_CONSENT_FLAG CHAR(1);

ALTER TABLE SBI_TEST.RUPEEPOWER_OCAS_T_00255
ADD QUOTE_NTB_ID VARCHAR2(255);
commit;
------------------------------------------------------------------

Name                   Null?    Type           
---------------------- -------- -------------- 
PRIVACY_ID             NOT NULL NUMBER(10)     
PRIVACY_TOUCH_POINT_ID          VARCHAR2(10)   
PRIVACY_LOCALE                  VARCHAR2(3)    
PRIVACY_API_VERSION             VARCHAR2(20)   
PRIVACY_TIME_STAMP_REQ          VARCHAR2(20)   
PRIVACY_CORRELATION_ID          VARCHAR2(40)   
PRIVACY_ACK                     VARCHAR2(10)   
PRIVACY_TIME_STAMP_RES          VARCHAR2(20)   
PRIVACY_STATUS                  VARCHAR2(10)   
PRIVACY_STATUS_CODE             NUMBER(3)      
PRIVACY_MESSAGE                 VARCHAR2(2000) 
PRIVACY_ERROR_MSG               VARCHAR2(2000) 
PRIVACY_NOTICE                  NCLOB          
PRIVACY_IS_ACTIVE               CHAR(1)

Name           Null?    Type         
-------------- -------- ------------ 
LANG_ID        NOT NULL NUMBER       
LANNGUAGE_CODE          VARCHAR2(10) 
LANGUAGE_NAME           VARCHAR2(50) 
IS_ACTIVE               CHAR(1)      
CREATED_AT     NOT NULL DATE         
   






INSERT INTO your_table_name (
    PRIVACY_ID,
    PRIVACY_TOUCH_POINT_ID,
    PRIVACY_LOCALE,
    PRIVACY_API_VERSION,
    PRIVACY_TIME_STAMP_REQ,
    PRIVACY_CORRELATION_ID,
    PRIVACY_ACK,
    PRIVACY_TIME_STAMP_RES,
    PRIVACY_STATUS,
    PRIVACY_STATUS_CODE,
    PRIVACY_MESSAGE,
    PRIVACY_ERROR_MSG,
    PRIVACY_NOTICE,
    PRIVACY_IS_ACTIVE
) VALUES (
    1000000001,              -- PRIVACY_ID: NUMBER(10) [Cannot be NULL]
    'OCASjohn_01',                 -- PRIVACY_TOUCH_POINT_ID: VARCHAR2(10)
    'mar',                    -- PRIVACY_LOCALE: VARCHAR2(3)
    'v1.2.0',                -- PRIVACY_API_VERSION: VARCHAR2(20)
    '2026-05-30T13:00:00Z',  -- PRIVACY_TIME_STAMP_REQ: VARCHAR2(20)
    'c83b7b82-965a-464a',    -- PRIVACY_CORRELATION_ID: VARCHAR2(40)
    'true',                   -- PRIVACY_ACK: VARCHAR2(10)
    '2026-05-30T13:00:02Z',  -- PRIVACY_TIME_STAMP_RES: VARCHAR2(20)
    'true',               -- PRIVACY_STATUS: VARCHAR2(10)
    200,                     -- PRIVACY_STATUS_CODE: NUMBER(3)
    '[Success]',-- PRIVACY_MESSAGE: VARCHAR2(2000)
    NULL,                    -- PRIVACY_ERROR_MSG: VARCHAR2(2000)
    'Notice text content',   -- PRIVACY_NOTICE: NCLOB
    'Y'                      -- PRIVACY_IS_ACTIVE: CHAR(1)
);


Name           Null?    Type         
-------------- -------- ------------ 
LANG_ID        NOT NULL NUMBER       
LANNGUAGE_CODE          VARCHAR2(10) 
LANGUAGE_NAME           VARCHAR2(50) 
IS_ACTIVE               CHAR(1)      
CREATED_AT     NOT NULL DATE         
   

LANG_ID  LANNGUAGE_CODE  LANGUAGE_NAME  IS_ACTIVE CREATED_AT
1	asm	Assamese	Y	11-05-26
2	ben	Bengali	Y	11-05-26
3	brx	Bodo	Y	11-05-26
4	dgo	Dogri	Y	11-05-26
5	eng	English	Y	11-05-26
6	guj	Gujarati	Y	11-05-26
7	hin	Hindi	Y	11-05-26
8	kan	Kannada	Y	11-05-26
9	kas	Kashmiri	Y	11-05-26
10	kok	Konkani	Y	11-05-26
11	mai	Maithili	Y	11-05-26
12	mal	Malayalam	Y	11-05-26
13	mni	Manipuri	Y	11-05-26
14	mar	Marathi	Y	11-05-26
15	nep	Nepali	Y	11-05-26
16	ory	Odia	Y	11-05-26
17	pan	Punjabi	Y	11-05-26
18	san	Sanskrit	Y	11-05-26
19	sat	Santali	Y	11-05-26
20	snd	Sindhi	Y	11-05-26
21	tam	Tamil	Y	11-05-26
22	tel	Telugu	Y	11-05-26
23	urd	Urdu	Y	11-05-26
