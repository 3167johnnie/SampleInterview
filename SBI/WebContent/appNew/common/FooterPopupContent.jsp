<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div id="privacyStatementHTML">
	<div class="modal fade otp-box" id="privacyStatement" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close"><span aria-hidden="true">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png"/></span></button> 
					<div class="f-otp-pop-content">
							<h2>Privacy Statement</h2>
	            			<div class="clear"></div>
				            <h3>1. DEFINITIONS</h3>
					            <p>In these Terms of Service (Terms & Conditions), unless the context indicates otherwise, the following words and phrases shall have the meanings indicated against them:</p>
					            <p><strong>'The Bank'</strong> refers to <s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/>, a body corporate established by means of an Act of  Parliament and having its Corporate Office at "State Bank Bhavan", Madame Cama Road, Nariman Point, Mumbai - 400 021 (which expression shall, unless it is repugnant to the subject or context thereof,  include its successors and assigns).</p>
					            <p><strong>'The Site'</strong> means  <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></p>
					            <p>Apply Online Services are the Bank's services which provide the facility of applying for retail loan product through the Apply Online website of the Bank  <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></p>
					            <p><strong>'User'</strong> refers to an online applicant and/or authorized sales partners of the Bank.</p>
					            <p><strong>'Applicant'</strong> refers to an applicant named in the Application Form and shall, where the context requires, include Co-applicant(s) with the Applicant. </p>
					            <p><strong>'Application'</strong> refers to an Applicant's Home Loan and/or Auto Loan and/or Education Loan and/or Personal Loan application to the Bank through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
					            <p><strong>'Information'</strong> refers to any information obtained by an applicant from the Bank for availing various services through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
					            <p><strong>'Personal Information'</strong> refers to any information about the applicant obtained by the Bank in connection with the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services.</p>
					            <p><strong>'Third party product'</strong> refers to any product and/or service of third party which is offered by or through the intermediary of the Bank.</p>
					            <p><strong>'One Time Password (OTP)'</strong> refers to one time 6 digit pass code sent to the mobile number provided by an applicant during a session, for verification of his/her mobile number. </p>
					            <p><strong>'Quote' / 'Eligibility'</strong> refers to an applicant's eligibility under the particular loan scheme. The quote shown to an applicant depends upon inputs provided by the online applicant. </p>
					            <p><strong>'Approval in Principle'</strong> letter refers to the letter sent to the email ID provided by an applicant, conveying an in-principle approval for the loan applied for.</p>
					            <p><strong>'Scheduling an appointment'</strong> means any request or instruction that is received from an applicant by the Bank through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online for scheduling an appointment with the Bank's official for document collection and/or KYC verification.</p>
					            
					            
					             <h3>2. APPLICABILITY OF TERMS</h3>
					            <p>By accessing the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service the User acknowledges and accepts these Terms of Service (Terms & Conditions). Any terms and conditions relating to the loan schemes of the Bank other than these Terms will continue to apply. </p>
					            
					            
					             <h3>3. SOFTWARE COMPATIBILITY</h3>
					            <p>The Bank may advise from time to time the internet software such as Browser, which are required for using <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online Services. There will be no obligation on the part of the Bank to support all the versions of such Internet software.</p>
					            
					            
					             <h3>4. CYBER CRIME</h3>
					            <p>The Internet per se is susceptible to cyber-crimes like phishing, vishing (Voice phishing), SMSing (phishing through SMS), compromising of User's system security, etc., that could affect instructions to the Bank. While the Bank shall endeavor to protect the interests of customers, there cannot be any guarantee against such cyber-crimes and other actions that might affect security of information. The User shall separately evaluate all such risks and the Bank shall not be held responsible for any losses arising out of such cyber-crimes. The User also understands that doing a transaction at a Cyber-cafe/shared computer terminal is risky, and shall avoid using the services of a Cybercafe/shared com-puter terminal to use the Bank's <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service.</p>
					            
					            
					             <h3>5. TECHNOLOGY RISKS</h3>
					            <p>The Site may require maintenance and during such periods it may not be possible to process the requests of customers. This could result in delays and/or failure in processing instructions. The User understands that the Bank disclaims any and all liability, whether direct or indirect, whether arising out of loss or otherwise arising out of any failure or inability by the Bank to honor any customer instruction for whatsoever reason.</p>
					            
					            
					             <h3>6. DOCUMENTS REQUIRED FOR TRANSACTION PROCESSING</h3>
					            <p>The User shall be responsible for submitting necessary documents and information as the Bank may require along with any request for any service under <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online. If any request for a service is such that it cannot be given effect to unless it is followed up by requisite documentation, the Bank shall not be required to act upon the request until it receives such documentation from the User.</p>
					            
					            
					             <h3>7. AUTHORITY TO THE BANK</h3>
					            <p>By giving an authority to the Bank agreeing to be contacted by the Bank or their representative, the applicant understands that he/ she has permitted the Bank or their representative to contact him/her over phone / email in connection with his/her loan application/ partial application/enquiry and the Bank shall not be liable for breach of the applicant's mandate for "Do not Disturb", if any.  </p>
					            
					            
					             <h3>8. ACCURACY OF INFORMATION</h3>
					            <p>A customer shall provide such information as the Bank may from time to time reasonably request for the purposes of providing the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services. The User is responsible for correctness of information supplied to the Bank through the use of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services. The Bank accepts no liability for consequences arising out of erroneous/incomplete/incorrect information provided by the User. If the User suspects that there is an error in the information supplied to the Bank by him/her, he/she shall advise the Bank at the earliest. The Bank will endeavor to correct such errors wherever possible on a 'best efforts' basis. If the User notices an error in the loan application provided to him/her through the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service, he shall advise the Bank at the earliest.</p>
					            
					            
					             <h3>9. LIABILITY OF THE USER</h3>
					            <p>The User shall be liable for</p>
					            <ul>
						            <li>Non-compliance of Terms of Service (Terms & Conditions) mentioned herein.</li>
						            <li>If he/she has breached the Terms of Service (Terms & Conditions) or contributed or caused any loss by his/her negligent actions.</li>
					            </ul>
					           
					            
					             <h3>10. NON-TRANSFERABILITY</h3>
					            <p>The grant of facility of Apply Online Services to an Applicant is non-transferable under any circumstances and application shall be used by the Applicant only.</p>
					            
					            
					             <h3>11. DISCLAIMER</h3>
					            <ul>
						            <li>The Bank shall not be liable for any unauthorized email sent to a User's email ID through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service, which can be attributed to fraudulent or negligent conduct of the User.</li>
						            <li>The Bank shall not be liable to the applicant(s) for any damages whatsoever whether such damages are direct, indirect, incidental, consequential and irrespective of whether any claim is based on investment or any other loss of any character or nature whatsoever and whether sustained by the user(s) or any other person, if <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online access is not available in the desired manner for reasons including but not limited to natural calamity, fire and other natural disasters, legal restraints,faults in the telecommunication network or Internet or network failure, software or hardware error or any other reasons beyond the control of the Bank.</li>
						            <li>The Bank shall endeavor to take all possible steps to maintain secrecy and confidentiality of information provided by Users, but shall not be liable to the Users for any damages whatsoever caused on account of breach of secrecy/confidentiality due to reasons beyond the control of the Bank.</li>
						            <li>The Bank, for valid reasons, may refuse to execute any application/instruction submitted by the User.</li>
						            <li>The quote/ in principle approval is tentative, and the Bank assumes no liability for such quote, which is based on the inputs provided by the User. Further, loan eligibility may vary depending upon changes in interest rate, margin, processing fee and/or terms and conditions of the respec-tive loan scheme.</li>
						            <li>The validity period of such "Approval in Principle" would be 45 days from the issuance of the letter. Final sanction of the loan application will depend on satisfactory KYC, Income, credit worthiness and documents verification. The applicant will also have to provide Collateral Security and Third Party Guarantee for sufficient value, wherever applicable.</li>
						            <li>Appointments scheduled by an online User through the system would be tentative only; the Bank is not liable to follow such appointments meticulously.</li>
						            <li>"Upload Document" functionality is provided for expediting the loan process. The Bank may ask for original and/or photo copy of such document for verification and/or at the time of processing of loan application. Bank will not use such document for any other purpose like updating KYC details, etc. </li>
						            <li>The Bank will in no way be held responsible for or liable for delay, failure and/or untimely delivery of SMS password and/or SMS Alerts due to but not limited to network congestions, network failure, systems failure or any others reasons beyond the reasonable control of the Bank or its service provider(s).</li>
					            </ul>
					            
					            
					            <h3>12. INDEMNITY</h3>
					            <p>The User agrees to indemnify, hold harmless and defend the Bank and its affiliates against any loss and damages that may be caused from or relating to </p>
					            <ul>
						            <li>Breach of Terms of Service (Terms & Conditions) mentioned herein.</li>
						            <li>Improper use of the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service by a User/applicant.</li>
						            <li>The use of products/ <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service in any manner which violates the Terms of Service (Terms & Conditions) or otherwise violates any law, rule, conditions or regulation.</li>
						            <li>Any claims made by third parties arising from issues related to any failure, delay or interruptions of the product and/or services as provided by Bank through <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></li>
					            </ul>
					            
					            
					             <h3>13. DISCLOSURE OF PERSONAL INFORMATION</h3>
					            <p>The User agrees that the Bank or its service providers may hold and process his/her Personal/Technical Information on Computer or otherwise in connection with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services as well as for statistical analysis.</p>
					            <p>The User agrees that the Bank may collect User system related information. The User also agrees that the Bank may disclose, in strict confidence, to other institutions, such Personal Information as may be reasonably necessary for reasons inclusive of, but not limited to, the following:</p>
					            <ul>
						            <li>In compliance with legal, regulatory and/or Reserve Bank of India's directives.</li>
						            <li>For fraud prevention purposes.</li>
					            </ul>
					            
					            
					             <h3>14. AMENDMENTS</h3>
					            <p>The Bank has the absolute discretion to amend or supplement any of the Terms of Service (Terms & Conditions) at any time, without prior notice. However the Bank will endeavor to notify any such change by posting it on the website or through advertisement or any other means as the Bank may deem fit which shall be binding on the User. The existence and availability of the new functionali-ties/facilities/features will be notified to the User as and when they are made available. By using these new services, the User agrees to be bound by the Terms of Service (Terms & Conditions) applicable therefore.</p>
					            
					            
					             <h3>15. PROPRIETARY RIGHTS</h3>
					            <p>The User acknowledges that the software underlying the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service as well as other Internet related software which are required for accessing <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service is the legal proper-ty of the Bank/the respective service providers. The permission given by the Bank to access <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service will not convey any proprietary or ownership rights in the above software. The User shall not attempt to modify, translate, disassemble, decompile or reverse engineer the software underlying the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services nor create any derivative product based on the software.</p>
					           
					            
					             <h3>16. PROPRIETARY AND INTELLECTUAL PROPERTY RIGHTS</h3>
					            <p>The copyright, trademarks, logos, slogans and service marks displayed on the website(s) are regis-tered and unregistered intellectual property rights of the Bank or of the respective intellectual property right owners.  Nothing contained on the website(s) should be construed as granting, by implication, estoppels, or otherwise, any license or right to use any intellectual property displayed on the website(s) without the written permission of the Bank or such third party that may own the intellectual property displayed on the website(s).</p>
					            <p>The Bank grants the right to access the website(s) to the User and to use the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online servic-es in accordance with the Terms of Service (Terms & Conditions) mentioned herein. The User ac-knowledges that the Services including, but not limited to, text, content, photographs, video, audio and/or graphics, are either the property of, or used with permission by, the Bank and/or by the content providers and may be protected by applicable copyrights, trademarks, service marks, international treaties and/or other proprietary rights and laws of India and other countries, and the applicable Terms of Service (Terms & Conditions).</p>
					            <p>The User should assume that everything he/she views or reads on the website (collectively referred to as "content") is copyrighted/ protected by intellectual property laws unless otherwise provided and may not be used, except as provided in these Terms of Service (Terms & Conditions), without the prior written permission of the Bank or the relevant copyright owner. </p>
					            <p>Any breach of the restrictions on use provided in these terms is expressly prohibited by law, and may result in severe civil and criminal penalties. The Bank shall be entitled to obtain equitable relief (includ-ing all damage, direct, indirect, consequential and exemplary) over and above all other remedies available to it, to protect its interests therein.</p>
					            
					            
					            <h3>17. TERMINATION OF INTERNET BANKING SERVICES</h3>
					            <p>The Bank may at its sole discretion, at any time without giving notice or reasons suspend or terminate all or any of the Apply Online Services offered by the Bank or their use by the applicant including for reasons such as</p>
					            <ul>
						            <li>Breach of these Terms of Service (Terms & Conditions) by the User</li>
						            <li>Knowledge or information about the death, bankruptcy or legal incapacity of the User.</li>
					            </ul>
					            
					            
					            <h3>18. THIRD PARTY LINKS</h3>
					            <p>The Site may provide hyperlinks to websites not controlled by <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online, and such hyperlinks do not imply any endorsement, agreement on, or support of the content, products and /or services of such websites. <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online does not editorially control the content, products and /or services on such websites and shall not be liable, in any manner whatsoever, for access to, or the inability of access to, or the use, or inability to use the content available on or through such websites./p>
					            
					            
					            <h3>19. NOTICES</h3>
					            <p>Notice under these Terms of Service (Terms & Conditions) to the user may be given through any medium of communication as may be deemed appropriate by the Bank i.e. Bank's website (<a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/></a>  or <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a>) or email, sms, public notification at Branches, or through newspapers, radio, TV, etc. Such notices will have the same effect as a notice served individually to each user.</p>
					            
					            
					            <h3>20. FORCE MAJEURE</h3>
					            <p>The Bank shall not be liable for delay in performing or failure to perform any of its obligations under these Terms of Service (Terms & Conditions) which is caused by circumstances beyond its reasonable control, including, but not limited to, the failure, malfunction or unavailability of telecommunications, data communications and/or computer systems and services, natural calamities, war, civil unrest, government action, strikes, lock-outs and/or other industrial action or trade disputes (whether involving the Bank's employees or those of a third party). Any delay or failure of this kind will not be deemed to be a breach of the Terms of Service (Terms & Conditions) and the time for performance of the af-fected obligation will be extended by a period which is reasonable in the circumstances.</p>
					            
					            
					            <h3>21. DISCLAIMER ON THE INFORMATION TECHNOLOGY ACT 2000</h3>
					            <p>The Bank has adopted the mode of authentication of the User by means of verification of the User ID and or through verification of password or through any other mode of verification as may be stipulated at the discretion of the Bank. The User hereby agrees/consents for the mode of verification adopted by the Bank. The User agrees that the transactions carried out or put through by the aforesaid mode shall be valid, binding and enforceable against the User and the User shall not be entitled to raise any dispute questioning such transactions.</p>
					            
					            
					            <h3>22. DISCLAIMER ON ANTI VIRUS UPDATE</h3>
					            <p>The User needs to get his PCs/laptops/tablets/mobiles scanned on a regular basis and have these updated with the latest antivirus software available. The Bank shall not be responsible in case of any data loss or theft due to any virus transmitted in the system through the usage of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
					            
					            
					            <h3>23. GOVERNING LAWS</h3>
					            <p>These Terms of Service (Terms & Conditions) and/or the use of services provided through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services are construed to be governed in accordance with the laws in India. The Bank accepts no liability whatsoever, direct or implied, for non-compliance with the laws of any country other than that of India. The mere fact that <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service can be accessed through Internet by a User from a country other than India shall not be interpreted to imply that the laws of the said countries go-vern these Terms of Service (Terms & Conditions) and/or the operations in the accounts of the User through Internet and/or the Use of Apply Online Services. The User agrees to abide by prevailing laws in respect of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services applicable in India. It is the responsibility of the User to comply with any regulations prevailing in the country from where he/she is accessing the Internet.</p>
					            <p>All disputes shall be subject to the jurisdiction of courts in Mumbai (India). The Bank however, may, in its absolute discretion commence any legal action or proceedings arising out of these terms in any other court, tribunal or other appropriate forum, and the User hereby consents to that jurisdiction.</p>
				            
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="disclosureHTML">
	<div class="modal fade otp-box" id="disclosure" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close"><span aria-hidden="true">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""></span></button>
					<div class="f-otp-pop-content">
							<h2>Disclosure</h2>
	            			<div class="clear"></div>
	            			<p><s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/> with its over 200 years of service to the nation embodies safety, trust and integrity. We have always woven these values into our relationship with customers. Apply Online is one more effort to add value to the relationship. Apply Online facility offers the convenience of applying for a banking product from <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> anywhere, anytime. In terms of the Code of Fair Banking Practices, we notify you on the characteristics of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
				            <h3>What is the Disclosure about?</h3>
				            <p>The debate about how secure the Internet is, has not settled down. We would therefore say that  Apply Online comes with the risks inherent to the Internet arena. </p>
				            <p>Smart users the world over have found ways to manage these risks. Banks worldwide have moved their customers to the Internet with enormous gains in efficiency and service quality. It is the customer who gains. This is exactly what we want for our customers. To come back to the question of risks, good practice suggests that the users should evaluate risks, appreciate and balance the criticalities and the convenience which Apply Online offers. <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> has put in place secure and effective systems to mitigate the risks from the Bank's end. We seek a little effort from your side, in maintaining this as a safe and secure channel. We want our customers to appreciate the risks realistically and mitigate them at their end. This includes proper handling of Username and passwords and the overall safety of the system at the user end.</p>
				            
				            
				            <h3>Security aspects</h3>
				            <p>The <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online channel is protected by advanced security features, both physical and logical. <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> has considered various risks inherent in transacting over a public network such as the Internet, and has deployed appropriate security measures to protect customers. Firewalls allow only valid web traffic to reach our server. Proven 256 bit Secure Socket Layer (SSL) encryption technology is deployed to ensure that the information exchanged between your computer and <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a>  over the Internet is secure and cannot be intruded upon. 'VeriSign' certifies that information exchanged during a valid session is protected during its transmission over the Internet. Additionally, the Bank has installed mechanisms such as Intrusion Detection Systems.</p>
				            
				            
				            <h3>Availability of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service</h3>
				            <ul>
					            <li class="disc">The <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service functions on a 24x7 basis.</li>
					            <li class="disc">Some of the services in Apply Online are extended in conjunction with Other Source Systems (OSS) within or outside the Bank, which may or may not function on a 24x7 basis. Such services will be available only when the OSS are functional. Availability of Apply Online is also dependent upon availability of the network. In addition, Apply Online will not be available during downtime.</li>
				            </ul>
				            
				            
				            <h3>Services offered</h3>
				            <p><s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online is an alternate channel for our customers, and provides the facility of applying for most of the retail loan products available at our branches. Retail loan products such as Home Loan, Auto Loan, Education Loan and Personal Loan can be applied for through this service. Other post application services such as viewing the current status of application, downloading a pre-filled loan application form, scheduling an appointment with the Bank's official and uploading documents, and pre-application services like Get back a call from an Expert, viewing the details of a product, terms and conditions, FAQs and the like are enabled through this channel.</p>
				            
				            
				            <h3>Control measures</h3>
				            <p>We have set up the following operational and control measures for Apply Online customers: </p>
				            <ul>
					            <li class="disc">Applicants can track the status of their application by using only the application reference ID and mobile number.</li>
					            <li class="disc">Customer logins and activities are tracked and archived for future reference.</li>
				            </ul>
				            <p>For viewing the quote and submission of application, an authorization, such as a One Time Password, is required.</p>
				            
				            
				            <h3>Precautions</h3>
				            <p>To enhance security in carrying out banking and other transactions, we recommend that customers adopt the following practices. These are not unique to the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> site. Banks the world over expect their customers to do this much in mutual interest. </p>
				            <ul>
					            <li class="disc">Do not leave your computer unattended while you are connected to <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a> </li>
					            <li class="disc">Please close the browser window after you have finished your session. </li>
					            <li class="disc">Do not access Apply Online if your computer device is not free of malware.</li>
				            </ul>
				            <p>For viewing the quote and submission of application, an authorization, such as a One Time Password, is required.</p>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div id="termAndConditionHTML">
<div class="modal fade otp-box" id="termAndCondition" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close"><span aria-hidden="true">
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""></span></button>
				<div class="f-otp-pop-content">
						<h2>Terms of Service (Terms & Conditions)</h2>
			            <div class="clear"></div>
			            <h3>1. DEFINITIONS</h3>
			            <p>In these Terms of Service (Terms & Conditions), unless the context indicates otherwise, the following words and phrases shall have the meanings indicated against them:</p>
			            <p><strong>'The Bank'</strong> refers to <s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}"/>, a body corporate established by means of an Act of  Parliament and having its Corporate Office at "State Bank Bhavan", Madame Cama Road, Nariman Point, Mumbai - 400 021 (which expression shall, unless it is repugnant to the subject or context thereof,  include its successors and assigns).</p>
			            <p><strong>'The Site'</strong> means  <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></p>
			            <p>Apply Online Services are the Bank's services which provide the facility of applying for retail loan product through the Apply Online website of the Bank  <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></p>
			            <p><strong>'User'</strong> refers to an online applicant and/or authorized sales partners of the Bank.</p>
			            <p><strong>'Applicant'</strong> refers to an applicant named in the Application Form and shall, where the context requires, include Co-applicant(s) with the Applicant. </p>
			            <p><strong>'Application'</strong> refers to an Applicant's Home Loan and/or Auto Loan and/or Education Loan and/or Personal Loan application to the Bank through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
			            <p><strong>'Information'</strong> refers to any information obtained by an applicant from the Bank for availing various services through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
			            <p><strong>'Personal Information'</strong> refers to any information about the applicant obtained by the Bank in connection with the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services.</p>
			            <p><strong>'Third party product'</strong> refers to any product and/or service of third party which is offered by or through the intermediary of the Bank.</p>
			            <p><strong>'One Time Password (OTP)'</strong> refers to one time 6 digit pass code sent to the mobile number provided by an applicant during a session, for verification of his/her mobile number. </p>
			            <p><strong>'Quote' / 'Eligibility'</strong> refers to an applicant's eligibility under the particular loan scheme. The quote shown to an applicant depends upon inputs provided by the online applicant. </p>
			            <p><strong>'Approval in Principle'</strong> letter refers to the letter sent to the email ID provided by an applicant, conveying an in-principle approval for the loan applied for.</p>
			            <p><strong>'Scheduling an appointment'</strong> means any request or instruction that is received from an applicant by the Bank through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online for scheduling an appointment with the Bank's official for document collection and/or KYC verification.</p>
			            
			            
			             <h3>2. APPLICABILITY OF TERMS</h3>
			            <p>By accessing the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service the User acknowledges and accepts these Terms of Service (Terms & Conditions). Any terms and conditions relating to the loan schemes of the Bank other than these Terms will continue to apply. </p>
			            
			            
			             <h3>3. SOFTWARE COMPATIBILITY</h3>
			            <p>The Bank may advise from time to time the internet software such as Browser, which are required for using <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online Services. There will be no obligation on the part of the Bank to support all the versions of such Internet software.</p>
			            
			            
			             <h3>4. CYBER CRIME</h3>
			            <p>The Internet per se is susceptible to cyber-crimes like phishing, vishing (Voice phishing), SMSing (phishing through SMS), compromising of User's system security, etc., that could affect instructions to the Bank. While the Bank shall endeavor to protect the interests of customers, there cannot be any guarantee against such cyber-crimes and other actions that might affect security of information. The User shall separately evaluate all such risks and the Bank shall not be held responsible for any losses arising out of such cyber-crimes. The User also understands that doing a transaction at a Cyber-cafe/shared computer terminal is risky, and shall avoid using the services of a Cybercafe/shared com-puter terminal to use the Bank's <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service.</p>
			            
			            
			             <h3>5. TECHNOLOGY RISKS</h3>
			            <p>The Site may require maintenance and during such periods it may not be possible to process the requests of customers. This could result in delays and/or failure in processing instructions. The User understands that the Bank disclaims any and all liability, whether direct or indirect, whether arising out of loss or otherwise arising out of any failure or inability by the Bank to honor any customer instruction for whatsoever reason.</p>
			            
			            
			             <h3>6. DOCUMENTS REQUIRED FOR TRANSACTION PROCESSING</h3>
			            <p>The User shall be responsible for submitting necessary documents and information as the Bank may require along with any request for any service under <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online. If any request for a service is such that it cannot be given effect to unless it is followed up by requisite documentation, the Bank shall not be required to act upon the request until it receives such documentation from the User.</p>
			            
			            
			             <h3>7. AUTHORITY TO THE BANK</h3>
			            <p>By giving an authority to the Bank agreeing to be contacted by the Bank or their representative, the applicant understands that he/ she has permitted the Bank or their representative to contact him/her over phone / email in connection with his/her loan application/ partial application/enquiry and the Bank shall not be liable for breach of the applicant's mandate for "Do not Disturb", if any.  </p>
			            
			            
			             <h3>8. ACCURACY OF INFORMATION</h3>
			            <p>A customer shall provide such information as the Bank may from time to time reasonably request for the purposes of providing the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services. The User is responsible for correctness of information supplied to the Bank through the use of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services. The Bank accepts no liability for consequences arising out of erroneous/incomplete/incorrect information provided by the User. If the User suspects that there is an error in the information supplied to the Bank by him/her, he/she shall advise the Bank at the earliest. The Bank will endeavor to correct such errors wherever possible on a 'best efforts' basis. If the User notices an error in the loan application provided to him/her through the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service, he shall advise the Bank at the earliest.</p>
			            
			            
			             <h3>9. LIABILITY OF THE USER</h3>
			            <p>The User shall be liable for</p>
			            <ul>
				            <li>Non-compliance of Terms of Service (Terms & Conditions) mentioned herein.</li>
				            <li>If he/she has breached the Terms of Service (Terms & Conditions) or contributed or caused any loss by his/her negligent actions.</li>
			            </ul>
			           
			            
			             <h3>10. NON-TRANSFERABILITY</h3>
			            <p>The grant of facility of Apply Online Services to an Applicant is non-transferable under any circumstances and application shall be used by the Applicant only.</p>
			            
			            
			             <h3>11. DISCLAIMER</h3>
			            <ul>
				            <li>The Bank shall not be liable for any unauthorized email sent to a User's email ID through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service, which can be attributed to fraudulent or negligent conduct of the User.</li>
				            <li>The Bank shall not be liable to the applicant(s) for any damages whatsoever whether such damages are direct, indirect, incidental, consequential and irrespective of whether any claim is based on investment or any other loss of any character or nature whatsoever and whether sustained by the user(s) or any other person, if <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online access is not available in the desired manner for reasons including but not limited to natural calamity, fire and other natural disasters, legal restraints,faults in the telecommunication network or Internet or network failure, software or hardware error or any other reasons beyond the control of the Bank.</li>
				            <li>The Bank shall endeavor to take all possible steps to maintain secrecy and confidentiality of information provided by Users, but shall not be liable to the Users for any damages whatsoever caused on account of breach of secrecy/confidentiality due to reasons beyond the control of the Bank.</li>
				            <li>The Bank, for valid reasons, may refuse to execute any application/instruction submitted by the User.</li>
				            <li>The quote/ in principle approval is tentative, and the Bank assumes no liability for such quote, which is based on the inputs provided by the User. Further, loan eligibility may vary depending upon changes in interest rate, margin, processing fee and/or terms and conditions of the respec-tive loan scheme.</li>
				            <li>The validity period of such "Approval in Principle" would be 45 days from the issuance of the letter. Final sanction of the loan application will depend on satisfactory KYC, Income, credit worthiness and documents verification. The applicant will also have to provide Collateral Security and Third Party Guarantee for sufficient value, wherever applicable.</li>
				            <li>Appointments scheduled by an online User through the system would be tentative only; the Bank is not liable to follow such appointments meticulously.</li>
				            <li>"Upload Document" functionality is provided for expediting the loan process. The Bank may ask for original and/or photo copy of such document for verification and/or at the time of processing of loan application. Bank will not use such document for any other purpose like updating KYC details, etc. </li>
				            <li>The Bank will in no way be held responsible for or liable for delay, failure and/or untimely delivery of SMS password and/or SMS Alerts due to but not limited to network congestions, network failure, systems failure or any others reasons beyond the reasonable control of the Bank or its service provider(s).</li>
			            </ul>
			            
			            
			            <h3>12. INDEMNITY</h3>
			            <p>The User agrees to indemnify, hold harmless and defend the Bank and its affiliates against any loss and damages that may be caused from or relating to </p>
			            <ul>
				            <li>Breach of Terms of Service (Terms & Conditions) mentioned herein.</li>
				            <li>Improper use of the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service by a User/applicant.</li>
				            <li>The use of products/ <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service in any manner which violates the Terms of Service (Terms & Conditions) or otherwise violates any law, rule, conditions or regulation.</li>
				            <li>Any claims made by third parties arising from issues related to any failure, delay or interruptions of the product and/or services as provided by Bank through <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a></li>
			            </ul>
			            
			            
			             <h3>13. DISCLOSURE OF PERSONAL INFORMATION</h3>
			            <p>The User agrees that the Bank or its service providers may hold and process his/her Personal/Technical Information on Computer or otherwise in connection with <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services as well as for statistical analysis.</p>
			            <p>The User agrees that the Bank may collect User system related information. The User also agrees that the Bank may disclose, in strict confidence, to other institutions, such Personal Information as may be reasonably necessary for reasons inclusive of, but not limited to, the following:</p>
			            <ul>
				            <li>In compliance with legal, regulatory and/or Reserve Bank of India's directives.</li>
				            <li>For fraud prevention purposes.</li>
			            </ul>
			            
			            
			             <h3>14. AMENDMENTS</h3>
			            <p>The Bank has the absolute discretion to amend or supplement any of the Terms of Service (Terms & Conditions) at any time, without prior notice. However the Bank will endeavor to notify any such change by posting it on the website or through advertisement or any other means as the Bank may deem fit which shall be binding on the User. The existence and availability of the new functionali-ties/facilities/features will be notified to the User as and when they are made available. By using these new services, the User agrees to be bound by the Terms of Service (Terms & Conditions) applicable therefore.</p>
			            
			            
			             <h3>15. PROPRIETARY RIGHTS</h3>
			            <p>The User acknowledges that the software underlying the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service as well as other Internet related software which are required for accessing <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service is the legal proper-ty of the Bank/the respective service providers. The permission given by the Bank to access <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service will not convey any proprietary or ownership rights in the above software. The User shall not attempt to modify, translate, disassemble, decompile or reverse engineer the software underlying the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services nor create any derivative product based on the software.</p>
			           
			            
			             <h3>16. PROPRIETARY AND INTELLECTUAL PROPERTY RIGHTS</h3>
			            <p>The copyright, trademarks, logos, slogans and service marks displayed on the website(s) are regis-tered and unregistered intellectual property rights of the Bank or of the respective intellectual property right owners.  Nothing contained on the website(s) should be construed as granting, by implication, estoppels, or otherwise, any license or right to use any intellectual property displayed on the website(s) without the written permission of the Bank or such third party that may own the intellectual property displayed on the website(s).</p>
			            <p>The Bank grants the right to access the website(s) to the User and to use the <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online servic-es in accordance with the Terms of Service (Terms & Conditions) mentioned herein. The User ac-knowledges that the Services including, but not limited to, text, content, photographs, video, audio and/or graphics, are either the property of, or used with permission by, the Bank and/or by the content providers and may be protected by applicable copyrights, trademarks, service marks, international treaties and/or other proprietary rights and laws of India and other countries, and the applicable Terms of Service (Terms & Conditions).</p>
			            <p>The User should assume that everything he/she views or reads on the website (collectively referred to as "content") is copyrighted/ protected by intellectual property laws unless otherwise provided and may not be used, except as provided in these Terms of Service (Terms & Conditions), without the prior written permission of the Bank or the relevant copyright owner. </p>
			            <p>Any breach of the restrictions on use provided in these terms is expressly prohibited by law, and may result in severe civil and criminal penalties. The Bank shall be entitled to obtain equitable relief (includ-ing all damage, direct, indirect, consequential and exemplary) over and above all other remedies available to it, to protect its interests therein.</p>
			            
			            
			            <h3>17. TERMINATION OF INTERNET BANKING SERVICES</h3>
			            <p>The Bank may at its sole discretion, at any time without giving notice or reasons suspend or terminate all or any of the Apply Online Services offered by the Bank or their use by the applicant including for reasons such as</p>
			            <ul>
				            <li>Breach of these Terms of Service (Terms & Conditions) by the User</li>
				            <li>Knowledge or information about the death, bankruptcy or legal incapacity of the User.</li>
			            </ul>
			            
			            
			            <h3>18. THIRD PARTY LINKS</h3>
			            <p>The Site may provide hyperlinks to websites not controlled by <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online, and such hyperlinks do not imply any endorsement, agreement on, or support of the content, products and /or services of such websites. <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online does not editorially control the content, products and /or services on such websites and shall not be liable, in any manner whatsoever, for access to, or the inability of access to, or the use, or inability to use the content available on or through such websites./p>
			            
			            
			            <h3>19. NOTICES</h3>
			            <p>Notice under these Terms of Service (Terms & Conditions) to the user may be given through any medium of communication as may be deemed appropriate by the Bank i.e. Bank's website (<a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/></a>  or <a href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/>" target="_blank"><s:property value="%{@com.mintstreet.common.util.Constants@BANK_ONLINE_URL}"/></a>) or email, sms, public notification at Branches, or through newspapers, radio, TV, etc. Such notices will have the same effect as a notice served individually to each user.</p>
			            
			            
			            <h3>20. FORCE MAJEURE</h3>
			            <p>The Bank shall not be liable for delay in performing or failure to perform any of its obligations under these Terms of Service (Terms & Conditions) which is caused by circumstances beyond its reasonable control, including, but not limited to, the failure, malfunction or unavailability of telecommunications, data communications and/or computer systems and services, natural calamities, war, civil unrest, government action, strikes, lock-outs and/or other industrial action or trade disputes (whether involving the Bank's employees or those of a third party). Any delay or failure of this kind will not be deemed to be a breach of the Terms of Service (Terms & Conditions) and the time for performance of the af-fected obligation will be extended by a period which is reasonable in the circumstances.</p>
			            
			            
			            <h3>21. DISCLAIMER ON THE INFORMATION TECHNOLOGY ACT 2000</h3>
			            <p>The Bank has adopted the mode of authentication of the User by means of verification of the User ID and or through verification of password or through any other mode of verification as may be stipulated at the discretion of the Bank. The User hereby agrees/consents for the mode of verification adopted by the Bank. The User agrees that the transactions carried out or put through by the aforesaid mode shall be valid, binding and enforceable against the User and the User shall not be entitled to raise any dispute questioning such transactions.</p>
			            
			            
			            <h3>22. DISCLAIMER ON ANTI VIRUS UPDATE</h3>
			            <p>The User needs to get his PCs/laptops/tablets/mobiles scanned on a regular basis and have these updated with the latest antivirus software available. The Bank shall not be responsible in case of any data loss or theft due to any virus transmitted in the system through the usage of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online.</p>
			            
			            
			            <h3>23. GOVERNING LAWS</h3>
			            <p>These Terms of Service (Terms & Conditions) and/or the use of services provided through <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services are construed to be governed in accordance with the laws in India. The Bank accepts no liability whatsoever, direct or implied, for non-compliance with the laws of any country other than that of India. The mere fact that <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online service can be accessed through Internet by a User from a country other than India shall not be interpreted to imply that the laws of the said countries go-vern these Terms of Service (Terms & Conditions) and/or the operations in the accounts of the User through Internet and/or the Use of Apply Online Services. The User agrees to abide by prevailing laws in respect of <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Apply Online services applicable in India. It is the responsibility of the User to comply with any regulations prevailing in the country from where he/she is accessing the Internet.</p>
			            <p>All disputes shall be subject to the jurisdiction of courts in Mumbai (India). The Bank however, may, in its absolute discretion commence any legal action or proceedings arising out of these terms in any other court, tribunal or other appropriate forum, and the User hereby consents to that jurisdiction.</p>
		            
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
