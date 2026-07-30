<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="right-panel">
	<div class="loan-form-container">
		<div class="step-div">
			<ul>
				<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
	     		        	<style> .step-div ul li:after{background:none} </style>
					 <li class="current"><span class="count">1</span><span class="text">CVE <br>APPLICATION</span></li>
	            </s:if> 
				<s:else>
				<s:if test="%{personalLoanPage == 1}">
					<li class="current"><span class="count">1</span><span class="text">Get <br>Eligibility</span></li>
					<%-- <li class=""><span class="count">2</span><span class="text">Loan<br>Offer</span></li> --%>
					<li class="step-count-3"><span class="count">2</span><span class="text">Complete <br>Application</span></li>
				</s:if>
				<s:elseif test="%{personalLoanPage == 2}">
					<li class="current"><span class="count">1</span><span class="text">Get <br>Eligibility</span></li>
					<%-- <li class=""><span class="count">2</span><span class="text">Loan<br>Offer</span></li> --%>
					<li class="step-count-3"><span class="count">2</span><span class="text">Complete <br>Application</span></li>
				</s:elseif>
				<s:elseif test="%{personalLoanPage == 4}">
					<li class="info-fill"><span class="count">1</span> <span class="text">Get <br>Eligibility</span></li>
					<%-- <li class="info-fill"><span class="count">2</span> <span class="text">Loan<br>Offer</span></li> --%>
					<li class="step-count-3 info-fill"><span class="count">2</span> <span class="text">Complete <br>Application </span></li>
				</s:elseif>
				</s:else>
				
			</ul>
		</div>
		<div class="form-container-section">
				<s:if test="%{personalLoanPage == 1}">
				<div id="content-1" class="form-div">
	   				<s:if test="%{appSeqId==null}">
					    <s:include value="/appNew/common/CommonCbs.jsp"></s:include>
	   				</s:if>
	   				
	   				<s:if test="%{showConsentPage == 1}">
		   				<form name="plConsentform" id="plConsentform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" 
	 						style="display :<s:property value="%{showForm?'block':'none'}"/>;">
							<s:include value="/appNew/common/CommonConsentPageETB.jsp"></s:include>
						</form>
					</s:if>
					
					<s:if test="%{isRekoveETB == 1}">
						<form name="revokeETBform" id="revokeETBform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" 
							style="display :<s:property value="%{showForm?'block':''}"/>;" onsubmit="return revokeConsent();" autocomplete="off">
							<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
							<s:include value="/appNew/loan/personal/revokeETB.jsp"></s:include>
						</form>
					</s:if>
					
					<form name="personalloanform" id="personalloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" onsubmit="return submit_first_page_bind();" 
						style="display :<s:property value="%{showPlForm?'block':'none'}"/>;">
		    		<s:include value="/appNew/loan/personal/GoldFirstPageSession.jsp"></s:include>
					</form>
				</div>
				</s:if>
				<s:elseif test="%{personalLoanPage == 2}">
	   				<div id="content-2" class="form-div-tab">
	   					<s:include value="/appNew/common/CommonOTPOther.jsp"></s:include>
	   				</div>
				</s:elseif>
				<s:elseif test="%{personalLoanPage == 4}">
   				<div id="content-4" class="form-div-tab">
   					<s:include value="/appNew/common/LoanThankYou.jsp"></s:include>
   				</div>
				</s:elseif>
   			<div class="rp-f-logo">
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/f-logo.png" alt=""  />
			</div>
		</div>
	</div>
	<%-- <s:include value="/appNew/common/HomePopups.jsp"></s:include> --%>
	<s:include value="/appNew/common/FooterProduct.jsp"></s:include>
</div>
