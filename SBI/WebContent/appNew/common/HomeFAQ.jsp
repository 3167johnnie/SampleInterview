<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/appNew/common/Header.jsp"></s:include>

<section class="inner-banner">
	<div class="caro-inner" role="listbox">
		<div class="container">
			<div class="carousel-caption">
				<div class="intro-text">
					<div class="intro-lead-in">
						SBI APPLY ONLINE
						<p class="para-txt">THE Most Preferred Home Loan Provider</p>
					</div>

				</div>
			</div>
		</div>
		<img
			src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/banner-bg.jpg"
			alt="">
	</div>
</section>


<section class="faq-content">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h2>
					<span class="head-bg">Frequently Asked Questions<span>(FAQs)</span>
				</h2>
				<h3>
					1. Which are the loan products that I can apply for through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply?
				</h3>
				<p>
					Currently, Home Loan, Auto Loan, Education Loan, Personal
					Loan,Loans against Mortgage of Immovable Property and Rent Plus
					(Loan against rent receivables) products are available through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply.
				</p>
				<h3>2. I own a plot and I now wish to construct a house. Can I
					apply online for a Home Loan for constructing my house?</h3>
				<h3>Yes. It is possible to apply online for Home Loans for the
					following purposes:-</h3>
				<ul>
					<li>i. Purchase of a ready built House/Flat (new/old)</li>
					<li>ii. Purchase of under construction House/Flat</li>
					<li>iii. Construction of house on a plot owned by you</li>
					<li>iv. Repair/Renovation of a House/Flat</li>
					<li>v. Extension of a House</li>
					<li>vi. Purchasing a Plot for construction of a house</li>
					<li>vii. For transferring your existing Home Loan availed of
						from another Home Loan provider</li>
					<li>viii.Top-up loans for our existing Home Loan Borrowers
						(extending house, furnishings, etc.)</li>
				</ul>
				<h3>3. I am an NRI. Can I apply online for a Home Loan?</h3>
				<p>Yes. Both NRIs and PIOs can apply online and avail of Home
					Loans for any of the purposes specified above, subject to their
					meeting other requirements.</p>
				<h3>4. Are there any specific products/concessions available
					for women applicants?</h3>
				<p>
					Yes. "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Her Ghar" is a loan product designed for women and available
					online, where a woman needs to be the sole / first applicant and
					the property acquired/proposed to be acquired should be in the name
					of such sole / first woman applicant. There is a concessional rate
					of interest of 5 bps (i.e.0.05%) less for women borrowers under "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Her Ghar".For Car Loans, a Woman borrower gets a 5 bps (0.05%)
					concession in interest rate. There is also a concession of 50 bps
					(0.50%) in Rate of Interest for Girls in case of Education Loan.
				</p>
				<h3>5. I am 21 years old. Can I apply online for a Home Loan?
					Are any concessions available?</h3>
				<p>
					Yes. Salaried applicants between 21-45 years of age are eligible
					for "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Yuva Home Loan" subject to fulfilment of employment and monthly
					income criteria. Under this scheme, only the interest applied on
					your Home Loan is payable during the first 36 months. The regular
					EMIs start after completion of 36 months.
				</p>
				<h3>
					6. I availed of a Home Loan from another Home Loan provider. Can I
					apply online for having this loan transferred to
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					?
				</h3>
				<p>Yes. Home Loans from Scheduled Commercial Banks (SCBs),
					Private and Foreign Banks, Housing Finance Companies (HFCs)
					registered with National Housing Bank (NHB) or Borrowers' employers
					(if they are Central/State Govts. or PSUs) can be taken over,
					subject to the applicant complying with other terms &amp;
					conditions.</p>
				<h3>
					7. I would like to park surplus funds available in my Home Loan, so
					that I can save on interest costs. Does
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					offer such a product online?
				</h3>
				<p>
					Yes. "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Maxgain" is such an innovative and customer friendly product that
					enables our Home Loan customers to park their surplus funds in the
					MaxGain account. The customer will also have the choice to withdraw
					the surplus funds deposited. This will enable customers to save on
					their Home Loan interest. The MaxGain (Home Loan) account will be
					linked to the customer's SB/Current Account where the customer will
					also be provided a Cheque Book and Internet Banking facilities.
				</p>
				<h3>8. Can I apply online for purchasing a plot of land? I will
					be constructing a house on this plot at a later date.</h3>
				<p>
					Yes. "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Realty" enables the customer to purchase a plot for construction of
					a house later. Construction of house should commence within 2 years
					from the date of availment of the
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Realty Loan. Customers may even avail of another Home Loan for
					construction of house on the plot financed under
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Realty, with the added benefit of running both the loans
					concurrently.
				</p>
				<h3>
					9. I already have a Home Loan with
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					. Can I apply online for another loan for furnishing my house?
				</h3>
				<p>
					"
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Top Up Home Loan" caters to such requirements, and you may apply
					online for such a 'top-up' loan to furnish your house, etc.
				</p>
				<h3>10. I wish to buy a pre-owned car. Can I apply online for a
					loan?</h3>
				<p>Yes. You may apply online for purchasing a used/ pre-owned
					car direct from the owner or through a Pre-owned Certified Car
					Dealer.</p>
				<h3>11. I am a Person of Indian Origin. Can I apply for a car
					loan?</h3>
				<p>
					No. At the moment,
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					only offers the facility of applying online for car loans to NRI
					applicants. PIOs may get in touch with our Contact Centre or
					nearest
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Branch.
				</p>
				<h3>12. I have already purchased a car from my own funds. Can I
					now apply for a loan online?</h3>
				<p>Yes. It is possible to apply online for reimbursement of the
					cost of a car already purchased, subject to certain terms and
					conditions.</p>
				<h3>
					13. I have secured admission to an IIT. Can I apply online for an
					Education Loan from
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					?
				</h3>
				<p>
					Yes. "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Scholar Loans" are designed for applicants who have secured
					admission to designated premier institutions such as IITs, IIMs,
					etc.
				</p>
				<h3>14. Can I apply online for a loan for doing a course
					abroad?</h3>
				<p>
					Yes. You may apply online for an "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Student Loan" for prosecuting studies at educational institutions
					in India or abroad, subject to terms and conditions.
				</p>
				<h3>
					15. I propose to do a 6 month Diploma course being conducted by a
					Government organization. Can I apply online for an Education Loan
					from <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					?
				</h3>
				<p>
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					's Loan Scheme for Vocational Education and Training caters to the
					requirements of applicants who wish to do skill
					development/certification courses from specified Government/
					non-government institutions, leading to a certificate/Diploma,
					degree, etc. You may apply online for a loan for this purpose.
				</p>
				<h3>
					16. I require funds for my daughter's marriage. Can I apply online
					for a loan from
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					?
				</h3>
				<p>
					Yes. It is now possible to apply online for a Personal Loan from
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					for any non-speculative purpose including marriage, education,
					travel, medical expenses, etc.
				</p>
				<h3>17. I wish to raise a loan for my personal needs against my
					commercial property. Can I apply online?</h3>
				<p>Yes. You may apply online for a loan against the mortgage of
					immovable property or against assignment of the rentals received on
					your commercial/residential property, subject to terms and
					conditions.</p>
				<h3>18. I am a government employee. I need a personal loan for
					a vacation overseas. What do I do?</h3>
				<p>You may apply online for a Personal Loan whether you are a
					salaried government/ private sector employee or a businessman or a
					professional(certain categories), for any non-speculative purpose
					including marriage, education, travel, medical expenses, etc.</p>
				<h3>
					19. Is it possible to get detailed information related to loan
					products before applying for a loan, such as the Terms &amp;
					Conditions, documents <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					required, etc.?
				</h3>
				<p>
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply is user friendly. It takes you through a step-by-step
					application process, providing answers to your questions on
					eligibility, terms &amp; conditions, required documents, etc.
					Details relating to loan products are also available on
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
					's website
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}" />
					. For more information you may call on the toll-free numbers
					provided on the site:
					<s:property
						value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_2}" />
					/
					<s:property
						value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_1}" />
					.
				</p>
				<h3>
					20. What is the difference between enquiring / applying for a loan
					through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply and moving a loan application at a branch?
				</h3>
				<p>There is no material difference in applying for a loan online
					or at a branch, except for the convenience/speed offered by the
					online loan enquiry/application process. Schemes, eligibility,
					processes, pricing, etc., remain the same.</p>
				<h3>21. What are the benefits of applying for a loan online? Or
					Why should I prefer to apply online rather than at a branch?</h3>
				<h3>We detail below some of the benefits of applying online:</h3>
				<ul>
					<li>i. You can apply for a retail loan product from anywhere,
						anytime to any designated branch.</li>
					<li>ii. You needn't wait for your turn for interacting with a
						Bank executive/ representative in regard to your loan requirement.
						The website provides most of the <br>&nbsp;&nbsp;&nbsp;details.
					</li>
					<li>iii. You may know and understand details of <s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />'s
						various retail loan products through the links provided on the
						website.
					</li>
					<li>iv. You may know your eligibility under <s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />'s
						various retail loan schemes and accordingly evaluate all available
						options.
					</li>
					<li>v. You will be provided guidance for choosing the most
						suitable product variant with the best pricing.</li>
					<li>vi. You may customize the loan amount and tenor as per
						your requirement.</li>
					<li>vii. It is possible to complete and submit your loan
						application at a later date if the required information /
						documents are not readily available.</li>
					<li>viii. You can even download a pre-filled loan application
						for submitting a signed copy to the Bank's Sales Executive, who
						will be contacting you. This will <br>&nbsp;&nbsp;&nbsp;
						&nbsp; reduce your effort in completing the loan application.
					</li>
					<li>ix. You can upload your KYC, Income and other documents to
						expedite the process.</li>
					<li>x. You can schedule an appointment with the Bank for
						collection of documents.</li>
					<li>xi. You can track the status of your online loan
						application, end to end, at any time after submitting the
						application.</li>
				</ul>
				<h3>
					22. I don't know the values to be put in various fields. I am new
					to
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					and unfamiliar with the loan application process.
				</h3>
				<h3>
					The "
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply" website is user friendly. It will facilitate your
					filling up the loan application in the following manner:
				</h3>
				<ul>
					<li>i. Contextual help/ tool tips are provided, wherever
						required.</li>
					<li>ii. In most cases you will be able to select an
						appropriate value from a dropdown menu.</li>
					<li>iii. In case of need you may even call our Contact Centre
						at the toll-free numbers provided on the site, for help with the
						application process.</li>
				</ul>
				<h3>23. What is the necessary information/ required documents
					that I should keep ready while applying for a loan?</h3>
				<h3>We suggest, for your convenience, that he followings
					documents should be available with you at the time of applying
					online:</h3>
				<ul>
					<li>i. Salary Slip, previous year's I-Tax return</li>
					<li>ii. Details of Property (Home Loans), Car (Car Loans),
						Admission letter/ prospectus of Educational Institution (Education
						Loans), etc.</li>
					<li>iii. PAN, other Identity proof such as Voter ID, Aadhaar
						Card, Passport, etc.</li>
					<li>iv. Your salary/pension account No. with <s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						(Applicable for Personal Loans and Auto loan to pensioner), etc.
					</li>
				</ul>
				<h3>24. What happens if I am unable to complete the loan
					application in one-go.</h3>
				<p>Don't worry. If you do not have all information readily
					available, you may complete the application at a later date (within
					15 days). A quote will be emailed to you with a link. Clicking on
					this link will take you back to your last quote, and you may
					continue with the loan application process.</p>
				<h3>25. How do I know that my application has been submitted
					successfully?</h3>
				<p>
					If you meet the basic requirements of eligibility under the
					selected loan scheme,
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply will generate an "Approval-in-Principle" letter
					indicating your loan application reference number, your maximum
					eligibility for loan, loan tenor, rate of interest, processing fee,
					etc., which will be sent to the e-mail ID provided by you.
				</p>
				<h3>
					26. I applied for a loan, but the system shows an error message "We
					are unable to process your loan application, please call our
					Contact <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Centre". Why is
					this happening?
				</h3>
				<p>
					You may not have input the relevant information correctly or there
					may be a mismatch in the income/eligibility parameters required for
					generation of an "Approval-in-Principle" letter. In such an
					eventuality you will receive a call from our Contact Centre. You
					may also enquire about the status of your loan application by
					calling
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					's Contact Centre at the toll-free numbers provided on the site.
				</p>
				<h3>
					27. I am receiving the error message, "You have already applied for
					a loan. Please click here to view current status of your loan
					application". I <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; have
					forgotten/misplaced my application Reference ID.
				</h3>
				<p>
					Please open the email received in the inbox of your email account
					from
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply that contains the "Approval-in-Principle" letter. It
					contains your loan application reference ID. In case you are unable
					to trace the email, please call our Contact Centre at
					<s:property
						value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_2}" />
					(toll free) or
					<s:property
						value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_1}" />
					(toll free) or
					<s:property
						value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_3}" />
					(toll No.) for getting your loan application reference ID.
				</p>
				<h3>
					28. Why is PAN an optional field in
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online? What is the benefit of submitting my PAN? What
					happens if I do not / am unable to <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					submit my PAN at the time of applying online?
				</h3>
				<p>
					PAN is the most widely accepted identity proof and a requirement
					for most large value financial transactions. Although PAN is a
					requirement for processing any loan application,
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online provides an applicant the comfort of submitting PAN at
					a later date, if the same is not readily available. Submitting your
					PAN online will, however, expedite the loan process.
				</p>
				<h3>29. My mobile is registered under 'Do Not Disturb'. How
					will the Bank contact me?</h3>
				<p>
					Once you check the box against the text, "I agree to be contacted
					by Bank over Mobile/ email", in
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online Apply, the Bank will call you using other (non-DND
					restricted) numbers.
				</p>
				<h3>
					30. I apprehend that if I provide my mobile number on
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online, I will start receiving telemarketing calls.
				</h3>
				<p>
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					will only call you in connection with your online loan application.
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					does not call for marketing / promotion unless you have
					specifically registered your mobile number for this purpose.
				</p>
				<h3>31. What will be the next step after submission of my loan
					application?</h3>
				<ul>
					<li>Step1: The Bank's official or an authorized agent will
						call you for scheduling an appointment.</li>
					<li>Step2: They will visit / call on you at the scheduled time
						for collection of documents and to satisfy your queries, if any.</li>
					<li>Step3: The Bank will start processing your application as
						per Bank's policy.</li>
				</ul>
				<h3>32. Can I upload documentsat a later date?</h3>
				<p>Yes. After clicking on the 'Application Status' tab on the
					'Home' page, you are presented with a screen which provides you
					with the current status of your loan application, and also enables
					you to schedule an appointment and/or to upload the required
					documents at a later date, too.</p>
				<h3>
					33. Are the uploaded documents sufficient for loan processing, or
					will the Bank ask for additional documents or additional copies of
					<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; documents?
				</h3>
				<p>The Bank may ask for additional documents or additional
					copies of documents to verify the inputs given in the application,
					or to comply with regulatory requirements/ the Bank's loan policy.
				</p>
				<h3>34. What types of documents can be uploaded with the
					application?</h3>
				<p>Any one of the following types of documents may be uploaded
					under each loan group.</p>
				<p></p>
				<div class="table-responsive">
					<table cellspacing="0" cellpadding="0" class="faq-div-table">
						<tbody>
							<tr>
								<th>Document Type</th>
								<th>Home Loan</th>
								<th>Auto Loan</th>
								<th>Education Loan</th>
								<th>Personal Loan</th>
							</tr>
	
							<tr>
								<td>Photograph</td>
								<td>Photograph</td>
								<td>Photograph</td>
								<td>Photograph</td>
								<td>Photograph</td>
							</tr>
							<tr>
								<td>Identity Proof</td>
								<td>PAN Card Voter ID Driving License Passport Employer IDe</td>
								<td>PAN Card Voter ID Driving License Passport Employer ID</td>
								<td>PAN Card Voter ID Driving License Passport</td>
								<td>PAN Card Voter ID Driving License Passport</td>
							</tr>
							<tr>
								<td>Address Proof</td>
								<td>Telephone Bill ElectricityBill Lease/ Rent Agreement
									Passport Ration Card</td>
								<td>Telephone Bill Electricity Bill Property tax receipt
									Ration Card Passport Voter ID Card</td>
								<td>Telephone Bill Electricity Bill Property tax receipt
									Ration Card Passport Voter ID Card</td>
								<td>Telephone Bill Electricity Bill Property tax receipt
									Ration Card Passport Voter ID Card</td>
							</tr>
	
							<tr>
								<td>Income Proof</td>
								<td>Latest Form 16 + Pay slip Latest ITR + Pay slip Salary
									A/c statement for last six months</td>
								<td>Latest salary slip Latest form 16 Salary account
									statement for last six months Audited/ Certified ITR for last 2
									years for Professionals</td>
								<td>Latest salary slip Latest form 16 Salary account
									statement for last six months Audited/ Certified ITR for last 2
									years for Professionals</td>
								<td>Latest salary slip Latest form 16 Salary account
									statement for last six months Audited/ Certified ITR for last 2
									years for Professionals</td>
							</tr>
							<tr>
								<td>Others (Purpose related documents)</td>
								<td>Agreement for sale, Allotment letter etc.</td>
								<td>Quotation for car to be purchased</td>
								<td>Proof of admission / selection, etc.</td>
								<td>Check-off letter from employer</td>
							</tr>
						</tbody>
					</table>
				</div>	
				<p></p>
				<h3>35. Is it possible for me to apply for more than one loan
					product at a time, online?</h3>
				<p>Yes, an applicant can apply for more than one product at a
					time but can't apply for more than one variant of the same product
					i.e. an applicant may simultaneously apply for a Home Loan and a
					Car Loan, but not for a "Basic Home Loan" as well as "Takeover of
					Home Loan". Rest assured that the Bank will offer the product most
					appropriate to your needs/eligibility.</p>
				<h3>36. What happens if I apply for a loan and then change my
					mind, or if I am not in a position to avail of the loan I applied
					for?</h3>
				<p>A Bank executive / representative will call you before
					processing the loan. You may advise them accordingly when they
					visit or call on you.</p>
				<h3>
					37. What happens if I am unable to produce or hand over to the
					Bank's representative the required documents after scheduling an <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					appointment?
				</h3>
				<p>Our representative will reschedule the appointment for
					collecting the remaining documents, or you may deposit the same at
					the loan processing Branch.</p>
				<h3>38. Is it possible to negotiate / get a higher quantum of
					loan / lower interest rates by applying online?</h3>
				<p>
					In such cases, you should personally visit the Branch since
					deviations from the existing terms and conditions are not handled
					through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Online apply.
				</p>
				<h3>
					39. Can I also apply for a similar / same loan or another loan at a
					branch after applying online through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online?
				</h3>
				<p>
					Yes, you may, subject to your eligibility for the loan. However,
					you should advise the Branch regarding your earlier loan
					application applied through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online.
				</p>
				<h3>
					40. After applying for a loan through
					<s:property
						value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
					Apply Online can I follow up my loan application with a branch?
				</h3>
				<p>You may follow up the status of your application online by
					using the application reference number provided in the e-mail sent
					to you when you applied online.</p>
				<h3>41. Will the branch staff help me to apply online / to
					complete an application?</h3>
				<p>Yes. You may also seek assistance from our Contact Centre by
					calling on the toll free numbers.</p>
				<h3>42. What do I do if the system / application hangs while I
					am applying online for a loan?</h3>
				<p>Refresh your browser and try again.</p>
				<h3>
					43.Can I switch / change the product I have applied for, after
					submitting an application online? For eg; if I wish to change the
					make and model <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;of the car I
					am purchasing, or a different house, or if I wish to apply for a
					different type of loan what do I do?
				</h3>
				<p>Since this involves a credit decision, the response may vary
					from case to case. It would be advisable in such an eventuality for
					the applicant to contact the Bank using the toll free numbers or to
					call at the branch for clarifications/ support.</p>
			</div>
		</div>
	</div>
</section>
<div class="clear">&nbsp;</div>
<s:include value="/appNew/common/Footer.jsp"></s:include>
