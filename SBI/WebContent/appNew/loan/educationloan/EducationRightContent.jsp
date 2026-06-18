<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--right panel-->
<div class="right-panel">
	<%-- <s:include value="/appNew/common/RightSideToggleMenuBar.jsp"></s:include> --%>
	<div class="loan-form-container">
		<div class="step-div">
			<ul>
				<s:if test="%{educationLoanPage == 1}">
					<li class="current"><span class="count">1</span><span class="text">Get <br>instant<br> approval</span></li>
					<li class=""><span class="count">2</span><span class="text">Choose and apply</span></li>
					<li class="step-count-3"><span class="count">3</span><span class="text">Complete <br>for fast processing</span></li>
				</s:if>
				<s:elseif test="%{educationLoanPage == 2}">
					<li class="info-fill"><a id="productSecondPageLink" href="javascript:void(0);" ><span class="count">1</span> <span class="text">Get <br>instant<br> approval</span></a></li>
					<li class="current"><span class="count">2</span> <span class="text">Choose and apply</span></li>
					<li class="step-count-3"><span class="count">3</span> <span class="text">Complete <br>for fast processing </span></li>
				</s:elseif>
				<s:elseif test="%{educationLoanPage == 3}">
					<li class="info-fill"><a id="productSecondPageLink" href="javascript:void(0);" ><span class="count">1</span> <span class="text">Get <br>instant<br> approval</span></a></li>
					<li class="info-fill"><a href="javascript:loanQuoteOffer();"><span class="count">2</span> <span class="text">Choose and apply</span></a></li>
					<li class="step-count-3 current"><span class="count">3</span> <span class="text">Complete <br>for fast processing </span></li>
				</s:elseif>
				<s:elseif test="%{educationLoanPage == 4}">
					<li class="info-fill"><span class="count">1</span> <span class="text">Get <br>Eligibility</span></li>
					<li class="info-fill"><span class="count">2</span> <span class="text">Loan<br>Offer</span></li>
					<li class="step-count-3 info-fill"><span class="count">3</span> <span class="text">Complete <br>Application </span></li>
				</s:elseif>
			</ul>
		</div>
		<div class="form-container-section">
   			<s:if test="%{educationLoanPage == 1}">
   				<div id="content-1" class="form-div">
	   				<s:if test="%{appSeqId==null}">
					    <s:include value="/appNew/common/CommonCbs.jsp"></s:include>
	   				</s:if>
		   				<s:if test="%{isDsrPage=='false'}">
			   				<div id="m-hide" class="<s:property value="%{appSeqId!=null?'':'hide-div-position'}"/>">
								<s:if test="%{appElTypeId!=@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
									<marquee style="font-size: 15px; color: #6495ED;">
										Please login to <a href="https://www.vidyalakshmi.co.in/Students/"
											target=" ">https://www.vidyalakshmi.co.in</a> to apply for Education
										Loan upto Rs. 10.00 lakhs in India and upto Rs. 20.00 lakhs abroad.
									</marquee>
								</s:if>
			   				</div>
						</s:if>
					<form name="educationloanform" id="educationloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" 
			 			style="display :<s:property value="%{showForm?'block':'none'}"/>;">
			 				<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
							<s:include value="/appNew/loan/educationloan/EducationFirstPageSession.jsp"></s:include>
					</form>
				</div>
   			</s:if>
   			<s:elseif test="%{educationLoanPage == 2}">
   				<div id="content-2" class="form-div">
					<s:include value="/appNew/loan/educationloan/EducationSecondPage.jsp"></s:include>
				</div>
   			</s:elseif>
   			<s:elseif test="%{educationLoanPage == 3}">
   				<div id="content-3" class="form-div">
					<s:include value="/appNew/loan/educationloan/EducationThirdPage.jsp"></s:include>
				</div>
   			</s:elseif>
   			<s:elseif test="%{educationLoanPage == 4}">
   				<div id="content-4" class="form-div-tab">
   					<s:include value="/appNew/common/LoanThankYou.jsp"></s:include>
   				</div>
   			</s:elseif>
   			<div class="rp-f-logo">
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/f-logo.png" alt=""  />
			</div>
		</div>
	</div>
	<s:include value="/appNew/common/FooterProduct.jsp"></s:include>
	<!--right panel-->
	
</div>
