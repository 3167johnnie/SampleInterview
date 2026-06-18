<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--left Section-->
<div class="left-panel">
	<div class="left-back-img">
		<img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/edu-bg.jpg" alt="" />
	</div>
	<div class="left-feature-panel">
		<h2>
			<span class="edu-icon"></span>
			<span><i>SBI</i><br>
			<s:if test="%{appElTypeId==2}">
				<span class="underline">GLOBAL</span>
				<br>
				<span>ED-VANTAGE</span>
			</s:if>
			<s:elseif test="%{appElTypeId==1}">
				<span class="underline">SCHOLAR </span><span> LOAN</span>
			</s:elseif >
			<s:elseif test="%{appElTypeId==3}">
				<span class="underline">EL TAKEOVER LOAN</span>
			</s:elseif>
			</span>
			<%-- <span class="underline">SCHOLAR</span> LOAN</span> --%>
		</h2>
		<div id="content-left" class="loan-form-feature">
			<s:if test="%{educationLoanPage == 1}">
				<div class="viewport">
					<s:property escapeHtml="false" value="%{leftSideContent}"/>
				</div>
			</s:if>
			<s:elseif test="%{educationLoanPage == 2}">
				<h2>Your Loan Details</h2>
				<ul class="loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{quote.loanQuoteInstituteName!=null?quote.loanQuoteInstituteName:'--'}" /></p>
							<span>Institute name</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>2</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{quote.loanQuoteCourseName!=null?quote.loanQuoteCourseName:'--'}"/></p>
							<span>Course name</span>
						</div>
					</li>
					<s:if test="%{quote.loanQuoteLoanPurposeId==10}">
					<li>
						<div class="loan-num-div"><span>3</span></div>
						<div class="loan-detail-div">
							<p>
							<s:if test="%{quote.loanQuoteCourseDurationYears>0}">
								<s:property value="%{quote.loanQuoteCourseDurationYears}"/>&nbsp;<s:property value="%{quote.loanQuoteCourseDurationYears>1?'years':'year'}"/>
							<s:if test="%{quote.loanQuoteCourseDurationMonth>0}">
								<s:property value="%{quote.loanQuoteCourseDurationMonth}"/> <s:property value="%{quote.loanQuoteCourseDurationMonth>1?'months':'month'}"/>
							</s:if>
							</s:if>
						<s:else>
							<s:if test="%{quote.loanQuoteCourseDurationMonth>0}">
								<s:property value="%{quote.loanQuoteCourseDurationMonth}"/> <s:property value="%{quote.loanQuoteCourseDurationMonth>1?'months':'month'}"/>
						</s:if>
						</s:else>
						</p>
						<span>Course duration</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>4</span></div>
						<div class="loan-detail-div">
							<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##.00}',{quote.loanQuoteLoanRequestedAmount})}"/></span></p>
							<span>Total course fee</span>
						</div>
					</li>
				</s:if>
				<s:elseif test="%{quote.loanQuoteLoanPurposeId==24}">
					<li>
						<div class="loan-num-div"><span>3</span></div>
						<div class="loan-detail-div">
							<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{quote.loanQuoteOutstandingLoanAmount}"/></span></p>
							<span>Outstanding balance</span>
						</div>
					</li>
				</s:elseif>	
				
				</ul>
				<div class="clear"></div>
				<h2>You are eligible for the below product(s)</h2>
				<ul class="loan-detail">
					<div class="loan-detail-div">
						<s:if test="%{loanScenarioBean.firstProductName!=null}">
							<p><a target="_blank" href="<s:property value="%{loanScenarioBean.firstProductUrl!=null?loanScenarioBean.firstProductUrl:'javascript:void(0);'}"/>"><s:property value="%{loanScenarioBean.firstProductName}"/></a></p>
						</s:if>
						<s:elseif test="%{loanScenarioBean.secondProductName!=null}">
							<p><a target="_blank" href="<s:property value="%{loanScenarioBean.secondProductUrl!=null?loanScenarioBean.secondProductUrl:'javascript:void(0);'}"/>"><s:property value="%{loanScenarioBean.secondProductName}"/></a></p>
						</s:elseif>	
					</div>
				</ul>
			</s:elseif>
			<s:elseif test="%{educationLoanPage == 3 || educationLoanPage == 4}">
				<h3><span>Your Loan </span>Details</h3>
				<ul class="loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##.00}',{appForm.appLoanAmount*100000})}" /></span></p>
							<span>Loan amount</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>2</span></div>
						<div class="loan-detail-div">
						 <p>
							<s:property value="%{appForm.appLoanTenure}"/> <s:property value="%{appForm.appLoanTenure>1?'months':'month'}"/>
						</p>
						   <span>Loan tenure</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>3</span></div>
						<div class="loan-detail-div">
							<p>
								<s:if test="%{appForm.appLoanInterestRateDiscount!=null && appForm.appLoanInterestRateDiscount>0}">
			 						<s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRateDiscount})"/>%
			 					</s:if>
			 					<s:else>
			 						<s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRate})"/>%
			 					</s:else>
							</p>
							<span>Interest rate</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>4</span></div>
						<div class="loan-detail-div">
							<p>
								<s:if test="%{appForm.appLoanEmiDiscount!=null && appForm.appLoanEmiDiscount>0}">
			 						<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmiDiscount)})}"/></span>
			 					</s:if>
			 					<s:else>
			 						<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})}"/></span>
			 					</s:else>
							</p>
							<span>EMI</span>
						</div>
					</li>
					<s:if test="%{educationLoanPage == 4}">
						<li>
							<div class="loan-num-div"><span>5</span></div>
							<div class="loan-detail-div">
								<s:if test="appForm.appEducationLoanId==2">
									<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/documents/25328/7410868/MITC-Scholar+Loans-+MAR+2015+%284+lists%29.pdf/dd28b52b-d290-4b1b-9cb2-38b2cdca58bf">Terms &amp; Conditions</a></p>
								</s:if>
								<s:else>
									<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/documents/25328/29531/1371638112056_SBI_STUDENT_LOAN_MITC.pdf/8d7cc6db-fe99-4ac6-896d-6ec5bab07821">Terms &amp; Conditions</a></p>
								</s:else>
								<br>
								<s:if test="appForm.appEducationLoanId==3">
									<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/loan-scheme-for-vocational-education">Doc Check List</a></p>
								</s:if>
					 			<s:elseif test="appForm.appEducationLoanId==2">
					 				<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/scholar-loans">Doc Check List</a></p>
					 			</s:elseif>
					 			<s:else>
					 				<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/education-loanssbi-student-loan-scheme">Doc Check List</a></p>
					 			</s:else>
								<span>Downloads</span>
							</div>
						</li>
					</s:if>
				</ul>
			</s:elseif>
		</div>
	</div>
</div>
<script type="text/javascript">
	jQuery("span.Rs").each(function(){
		var placeholder = jQuery(this).text();
		var strip_commas = placeholder.replace(/,/g, "");
		if(strip_commas>0){
			strip_commas = Number(strip_commas).toPrecision();
			strip_commas = strip_commas.replace(".0","");
			jQuery(this).text(M.moneyFormat(strip_commas));
		}
	});
</script>
<!--left Section End-->
