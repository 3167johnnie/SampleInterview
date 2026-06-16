<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--right panel-->
<div class="right-panel">
	<%-- <s:include value="/appNew/common/RightSideToggleMenuBar.jsp"></s:include> --%>
	<div class="loan-form-container">
		<div class="step-div">
			<ul> 
				<s:if test="%{homeLoanPage == 1}">
					<li class="current"><span class="count">1</span><span class="text">Get <br>Eligibility</span></li>
					<li class=""><span class="count">2</span><span class="text">Loan<br>Offer</span></li>
					<li class="step-count-3"><span class="count">3</span><span class="text">Complete <br>Application</span></li>
				</s:if>
				<s:elseif test="%{homeLoanPage == 2}">
					<li class="info-fill"><a id="productSecondPageLink" href="javascript:void(0);" ><span class="count">1</span> <span class="text">Get <br>Eligibility</span></a></li>
					<li class="current"><span class="count">2</span> <span class="text">Loan<br>Offer</span></li>
					<li class="step-count-3"><span class="count">3</span> <span class="text">Complete <br>Application </span></li>
				</s:elseif>
				<s:elseif test="%{homeLoanPage == 3}">
					<li class="info-fill"><a id="productSecondPageLink" href="javascript:void(0);" ><span class="count">1</span> <span class="text">Get <br>Eligibility</span></a></li>
					<li class="info-fill"><a href="javascript:loanQuoteOffer();"><span class="count">2</span> <span class="text">Loan<br>Offer</span></a></li>
					<li class="step-count-3 current"><span class="count">3</span> <span class="text">Complete <br>Application </span></li>
				</s:elseif>
				<s:elseif test="%{homeLoanPage == 4}">
					<li class="info-fill"><span class="count">1</span> <span class="text">Get <br>Eligibility</span></li>
					<li class="info-fill"><span class="count">2</span> <span class="text">Loan<br>Offer</span></li>
					<li class="step-count-3 info-fill"><span class="count">3</span> <span class="text">Complete <br>Application </span></li>
				</s:elseif>
			</ul>
		</div>
		<div class="form-container-section">
   			<s:if test="%{homeLoanPage == 1}">
				<div id="content-1" class="form-div">
	   				<s:if test="%{appSeqId==null}">
					    <s:include value="/appNew/common/CommonCbs.jsp"></s:include>
	   				</s:if>
					<form name="homeloanform" id="homeloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" 
 						style="display :<s:property value="%{showForm?'block':'none'}"/>;">
 						<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
 						<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
						<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
						<s:hidden name="quote.quotePrivacyLocale" id="quotePrivacyLocale" />
						<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>
					</form>
				</div>
   			</s:if>
   			<s:elseif test="%{homeLoanPage == 2}">
   				<div id="content-2" class="form-div">
					<s:include value="/appNew/loan/homeloan/HomeSecondPage.jsp"></s:include>
				</div>
   			</s:elseif>
   			<s:elseif test="%{homeLoanPage == 3}">
   				<div id="content-3" class="form-div">
					<s:include value="/appNew/loan/homeloan/HomeThirdPage.jsp"></s:include>
				</div>
   			</s:elseif>
   			<s:elseif test="%{homeLoanPage == 4}">
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
