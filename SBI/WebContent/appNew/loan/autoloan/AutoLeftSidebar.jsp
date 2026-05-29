<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--left Section-->
<div class="left-panel">
	<div class="left-back-img">
		<img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/auto-bg.jpg" alt="" />
	</div>
	<div class="left-feature-panel">
		<h2>
			<span class="auto-icon"></span>
			<span><i>SBI</i><br>
			<span class="underline">AUTO</span> LOAN</span>
		</h2>
		<div id="content-left" class="loan-form-feature">
			<s:if test="%{autoLoanPage == 1}">
				<div class="viewport">
					<s:property escapeHtml="false" value="%{leftSideContent}"/>
				</div>
			</s:if>
			<s:elseif test="%{autoLoanPage == 2}">
				<h2>Your Loan Details</h2>
				<ul class="loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{loanScenarioBean.loanPurpose}" /></p>
							<span>Loan Purpose</span>
						</div>
					</li>
					<s:if test="%{loanScenarioBean.loanPurpose == 'Takeover of car loan'}">
						<li>
							<div class="loan-num-div"><span>2</span></div>
							<div class="loan-detail-div">
								<span class="field">Outstanding Balance</span>
							</div>
						</li>
					</s:if>
					<s:else>
						<s:if test="%{quote.loanQuoteLoanPurposeId == 8}">
							<li>
								<div class="loan-num-div"><span>2</span></div>
								<div class="loan-detail-div">
									<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,##.00}',{quote.loanQuoteDealerExshowroomPrice})"/></span></p>
									<span class="field">Dealer's Price </span>
								</div>
							</li>
							<li>
								<div class="loan-num-div"><span>3</span></div>
								<div class="loan-detail-div">
									<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,##.00}',{quote.loanQuoteInsuredDeclaredValue})"/></span></p>
									<span class="field">Insured's Declared Value</span>
								</div>
							</li>
						</s:if>
						<s:else>
							<li>
								<div class="loan-num-div"><span>2</span></div>
								<div class="loan-detail-div">
									<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,##.00}',{quote.loanQuoteExshowroomPriceCar})"/></span></p>
									<span class="field">Dealer's Ex-showroom Price </span>
								</div>
							</li>
							<li>
								<div class="loan-num-div"><span>3</span></div>
								<div class="loan-detail-div">
									<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,##.00}',{quote.loanQuoteOnRoadPrice})"/></span></p>				
									<span class="field">On-Road Price</span>
								</div>
							</li>
						</s:else>
					</s:else>
						<li>
							<div class="loan-num-div"><span>4</span></div>
							<div class="loan-detail-div">
								<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,##.00}',{quote.loanQuoteNetIncome})"/></span></p>
								<span>Net Annual Income</span>
							</div>
						</li>
				</ul>
			</s:elseif>
			<s:elseif test="%{autoLoanPage == 3 || autoLoanPage == 4}">
				<h3><span>Your Loan </span>Details</h3>
				<ul class="loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{appForm.appLoanAmount*100000})}" /></span></p>
							<span>Loan Amount</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>2</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{appForm.appLoanTenure}"/> <s:property value="%{appForm.appLoanTenure>1?'months':'month'}"/></p>
							<span>Loan Tenure</span>
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
							<span>Interest Rate</span>
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
						 			<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
										<s:if test="%{appForm.appHomeLoanId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
											<s:property value="%{tenure1Duration}"/> mth : <span>: Only Interest</span><br>
											<s:property value="%{tenure2Duration}"/> mth : <span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/></span><br>
											<s:property value="%{tenure3Duration}"/> mth : <span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/></span><br>
											<s:property value="%{tenure4Duration}"/> mth : <span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/></span>
										</s:if>
										<s:else>
											<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})}"/></span>
										</s:else>
									</s:if>
									<s:else>
										<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})}"/></span>
									</s:else>
						 		</s:else>
							</p>
							<span>EMI</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>5</span></div>
						<div class="loan-detail-div">
							<p>
								<s:if test="%{appForm.appLoanProcessingFeeDiscount!=null && appForm.appLoanProcessingFeeDiscount>=0}">
						 			<s:if test="%{appForm.appLoanProcessingFeeDiscount==0}">
						 				0
						 			</s:if>
						 			<s:else>
						 				<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFeeDiscount)})}"/></span>
						 			</s:else>
			 					</s:if>
						 		<s:else>
						 			<span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFee)})}"/></span>
						 		</s:else>
							</p>
							<span>Processing Fee</span>
						</div>
					</li>
					<s:if test="%{autoLoanPage == 4}">
						<li>
							<div class="loan-num-div"><span>6</span></div>
							<div class="loan-detail-div">
								<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/documents/25328/29531/1377606055343_HOME_LOAN_MITC.pdf/6d6e7d93-23c6-4e7a-9b0b-dffe2a95292f">Terms &amp; Conditions</a></p>
								<p><a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/documents">Doc Check List</a></p>
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
