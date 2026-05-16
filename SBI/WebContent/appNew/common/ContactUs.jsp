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
		<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/banner-bg.jpg" alt="">
	</div>
</section>

<section class="about-content ">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h2>
					<span class="head-bg">Contact <span>us</span></span>
				</h2>
				<s:if
					test="%{@com.mintstreet.common.util.Constants@BANK_ID == @com.mintstreet.common.util.Constants@BANK_ID_SBBJ}">
					<p>
						<br> At
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
						, we are committed to provide best apply online services to our
						customers. Please feel free to share your Apply Online experience
						with us over phone. You may track your application status by login
						in to Apply Online Home Page > Application Status.
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						also provides status of your loan application submitted through
						Online Apply over phone round the clock. Please contact us on:-
					</p>
				</s:if>
				<s:if
					test="%{@com.mintstreet.common.util.Constants@BANK_ID == @com.mintstreet.common.util.Constants@BANK_ID_SBT}">
					<p>
						<br> At
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
						, we are committed to provide best apply online services to our
						customers. Please feel free to share your Apply Online experience
						with us over phone. <br>You may track your application status
						by login in to Apply Online Home Page > Application Status.
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						also provides status of your loan application submitted through
						Online Apply over phone round the clock. Please contact us on:-
					</p>
				</s:if>
				<s:if
					test="%{@com.mintstreet.common.util.Constants@BANK_ID == @com.mintstreet.common.util.Constants@BANK_ID_SBH}">
					<p>
						At
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
						, we are committed towards providing the finest internet banking
						services to our customers. We invite you to share your Apply
						Online experience with us over phone. You may track your
						application status by logging into ---> Apply Online Home Page >
						Application Status.
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						also provides the status of your loan application submitted
						through Online Apply over phone, round the clock.
					</p>
					<p>Please call:</p>
				</s:if>
				<s:if
					test="%{@com.mintstreet.common.util.Constants@BANK_ID == @com.mintstreet.common.util.Constants@BANK_ID_SBM}">
					<p>
						At
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
						, we are committed towards providing the finest internet banking
						services to our customers. We invite you to share your Apply
						Online experience with us over phone. You may track your
						application status by logging into ---> Apply Online Home Page >
						Application Status.
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						also provides the status of your loan application submitted
						through Online Apply over phone, round the clock
					</p>
					<p>Please call:</p>
				</s:if>
				<s:else>
					<p>
						<br> At
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_FULL_NAME}" />
						, we are committed towards providing the finest internet banking
						services to our customers. We invite you to share your Apply
						Online experience with us over phone. You may track your
						application status by logging into ---> Apply Online Home Page >
						Application Status.
						<s:property
							value="%{@com.mintstreet.common.util.Constants@BANK_NAME}" />
						also provides the status of your loan application submitted
						through Online Apply over phone, round the clock. </br>
					</p>
					<p class="mb-5">Please call:</p>
				</s:else>
				<s:if
					test="%{@com.mintstreet.common.util.Constants@BANK_ID == @com.mintstreet.common.util.Constants@BANK_ID_SBT}">
					<p class="mb-5">
						<strong><s:property
								value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_1}" />
							(Toll free and accessible from all landlines and mobile phones)
					</p>
					<p>
						<br>
						<strong><s:property
								value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_3}" /></strong>
						(Accessible from all landlines and mobile phones)</br>
					</p>
					<a
						href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/web/customer-care/customer-care"
						target="_blank">Complaint / Feedback</a>
				</s:if>
				<s:else>
					<p class="mb-5">
						<strong><s:property
								value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_2}" />
							& <s:property
								value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_1}" /></strong>
						(Toll free and accessible from all landlines and mobile phones)
					</p>
					<p>
						<br>
						<strong><s:property
								value="%{@com.mintstreet.common.util.Constants@CONTACT_NO_3}" /></strong>
						(Accessible from all landlines and mobile phones)</br>
					</p>
					<a
						href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/web/customer-care/customer-care"
						target="_blank">Complaint / Feedback</a>
				</s:else>

				<div id="applicationDetails"></div>
			</div>

		</div>
	</div>
</section>
<div class="clear">&nbsp;</div>
<s:include value="/appNew/common/Footer.jsp"></s:include>
