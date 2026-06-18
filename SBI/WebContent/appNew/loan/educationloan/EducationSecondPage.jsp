<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.ui.slider.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<s:set var="suffix" value="%{loanScenarioBean.maxEligibility>1000?' lakhs':' lakhs'}"/>
		<form name="capturedValue" id="capturedValue" method="post" enctype="application/x-www-form-urlencoded" action="javascript:void(0);">
			<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
			<div class="custom-tab">
			<h3>CUSTOMIZE YOUR LOAN QUOTE</h3>
			
			<div class="custom-slider">
			<div class="slider-div">
			<div class="slide-text">
				<span class="flt m-t5"> Loan Amount</span>
				<span id="slider-elig-amount"></span>
			</div>	
				<div class="clearfix"></div>
				<div id="slider-elig"></div>
				<div class="range-div">
					<div class="flt width-33">
						<s:if test="%{loanScenarioBean.productSliderDigitExact==1}">
							<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.minEligibility})"/> <s:property value="%{loanScenarioBean.minEligibility>1?'lakhs':'lakh'}"/>
						</s:if>
						<s:else>
							<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.minEligibility})"/> <s:property value="%{loanScenarioBean.minEligibility>1?'lakhs':'lakh'}"/>
						</s:else>
					</div>
					<div class="width-33 flt">&nbsp;</div>
					<div class="width-33 flr txt-right">
						<s:if test="%{loanScenarioBean.productSliderDigitExact==1}">
							<s:property value="getText('{0,number,##.00}',{loanScenarioBean.maxEligibility})"/> <s:property value="%{loanScenarioBean.maxEligibility>1?'lakhs':'lakh'}"/>
						</s:if>
						<s:else>
							<s:property value="getText('{0,number,##.00}',{loanScenarioBean.maxEligibility})"/> <s:property value="%{loanScenarioBean.maxEligibility>1?'lakhs':'lakh'}"/>
						</s:else>
					</div>
				</div>
			</div>
			<div class="slider-div">
			<div class="slide-text">
				<span class="flt m-t5">Loan Tenure</span>
				<span id="slider-tenure-amount"></span>
			</div>	
				<div class="clearfix"></div>
				<div id="slider-tenure"></div>
				<div class="range-div">
					<div class="flt width-33">
						<s:if test="%{loanScenarioBean.productSliderTenure ==1}">
							<s:property value="%{loanScenarioBean.minTenure}" />
						</s:if>
						<s:else>
							<s:property value="%{loanScenarioBean.minTenure}"  />
						</s:else>
						<s:if test="%{loanScenarioBean.productSliderTenure==1}"> 
							<s:property value="%{loanScenarioBean.chosenTenure>1?'months':'month'}"/>
						</s:if>
						<s:else>
							<s:property value="%{loanScenarioBean.chosenTenure>1?'years':'year'}"/>
						</s:else>
					</div>
					<div class="width-33 flt">&nbsp;</div>
					<div class="width-33 flr txt-right">
						<s:if test="%{loanScenarioBean.productSliderTenure ==1}">
							<s:property value="%{loanScenarioBean.maxTenure}" /> months
						</s:if>
						<s:else>
							<s:property value="%{loanScenarioBean.maxTenure}" /> years
						</s:else>
					</div>
				</div>
			</div>
			</div>
			<div class="manualy-slider">
				<ul class="form-section">
					<li>
						<label>Select eligiblity</label>
						<div class="flat-field">
							<s:select headerKey="" headerValue="Select Eligibility" 
								value="%{loanScenarioBean.chosenEligibility}" 
								name="manualEligVal" list="%{loanScenarioBean.manualEligVal}"
	                			cssClass="form-select"  
	                			onchange="return validateFirst('chosenEligibility', this, '%{loanScenarioBean.maxEligibility}');" onfocus="customOnFocus(this);" onblur="customOnBlur(this);">
	               		</s:select>
					</div>
				</li>
				<li>
					<label>Select tenure</label>
					<div class="flat-field">
						<s:select headerKey="" headerValue="Select Tenure" 
							value="loanScenarioBean.chosenTenure" 
							name="manualTenureVal" list="%{loanScenarioBean.manualTenureVal}" 
	            			cssClass="form-select" 
	 						onchange="return validateFirst('chosenTenure', this, '%{loanScenarioBean.maxTenure}' );" onfocus="customOnFocus(this);" onblur="customOnBlur(this);">
	           			</s:select>
					</div>
				</li>
				<li>
					<input id="changeLoanQuote" name="change-btn" class="apply-btn" value="Change" type="submit">
				</li>
			</ul>
		</div>
		<s:set var="cAccountType" value="%{quote.loanQuoteLoanAccountType>0?quote.loanQuoteLoanAccountType:1}" />
			<s:hidden name="chosenTenure" id="chosenTenure" value="%{loanScenarioBean.chosenTenure}"/>
			<s:hidden name="chosenEligibility" id="chosenEligibility" value="%{loanScenarioBean.chosenEligibility}" />
			<s:hidden name="chosenLoanAccountType" id="chosenLoanAccountType" value="%{#cAccountType}" />
			<s:hidden name="chosenProductId" id="chosenProductId" value="%{loanScenarioBean.chosenProductId}" />
			<s:hidden name="productSliderTenure" id="productSliderTenure" value="%{loanScenarioBean.productSliderTenure}" />
			<s:hidden name="productSaveEmail" id="productSaveEmail" value=""/>
		
		<div class="text-center loan-quote-loader" id="loan-quote-loader" style="display:none">
			<div class="loan-quote-loader-img"><img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader-horizontal.gif" width="35"/> </div> 
		</div>
	</div>
	
	<div class="clearfix"></div>
	<div class="loan-offers-container">
	<h3 class="flt">YOUR LOAN QUOTES</h3>
	<%-- <s:if test="%{loanScenarioBean.showOffer==1}"> --%>
		<div class="offer-btn">
		 	<a class="product-offers" href="#" data-target="#productoffer" data-toggle="modal" >Click to view Offers</a>
		</div>
 <%-- 	</s:if> --%>
	<div class="loan-offer-div edu-loan-offer-div <s:property value="%{appElTypeId==@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER?'edutakeroverloan-box':''}"/>" >		
		<s:iterator value="%{loanScenarioBean.allQuotes}" status="assciateQuoteQuoteIndex" var="commonQuote">
			<s:if test="%{#commonQuote!=null}">
							<s:if test="%{#assciateQuoteQuoteIndex.index==0}">
								<s:set var="firstSaveEmailURL" value="%{#commonQuote.applyQuoteUrl}"/>
							</s:if>
							<ul id="<s:property value="%{#commonQuote.productTypeId}"/>-<s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"style="display:<s:property value="%{#assciateQuoteQuoteIndex.index+1 == #cAccountType?'block':'none'}"/>;">
								<h2><s:property value="%{#commonQuote.productTypeName}"/><span class="p-content"> (Product type)</span></h2> 
								<li>
									<div class="box">
										<div class="box-inner">
											<span class="font-rupee">`</span>
											<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/> <s:property value="%{loanScenarioBean.chosenEligibility>1?'lakhs':'lakh'}"/>
											<span class="p-content">(for <s:property value="%{loanScenarioBean.chosenTenure}"/><s:if test="%{loanScenarioBean.productSliderTenure ==1}"> <s:property value="%{loanScenarioBean.chosenTenure>1?'months':'month'}"/></s:if><s:else><s:property value="%{loanScenarioBean.chosenTenure>1?'years':'year'}"/></s:else>)</span>
											<span class="p-content quote-heading">Loan amount</span>
										</div>
									</div>
								</li>
								<li>
									<div class="box">
										<div class="box-inner">
											<s:if test="%{#commonQuote.discountedInterestRate!=null && #commonQuote.discountedInterestRate>0}">
												<span class="line-through"><s:property value="getText('{0,number,##.00}',{#commonQuote.interestRate})"/>%</span>
												<span class=""><s:property value="getText('{0,number,##.00}',{#commonQuote.discountedInterestRate})"/>%</span>
											</s:if>
											<s:else>
												<span class=""><s:property value="getText('{0,number,##.00}',{#commonQuote.interestRate})"/>%</span>
											</s:else>
											<span class="p-content">(<s:property value="%{#commonQuote.rateType==1?'Floating':'fixed'}"/>)</span>
											<span class="p-content quote-heading">Best rate</span>
										</div>
									</div>
								</li>
								<li>
									<div class="box">
											<div class="box-inner">
												<s:if test="%{loanScenarioBean.isDiscountApplied==1}">
													<span class="line-through"><span class="font-rupee"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span></span>
													<span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedEmi)})"/></span>
												</s:if>
												<s:else>
													<span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span>
												</s:else>
												<span class="p-content quote-heading">EMI</span>
											</div>
									</div>
								</li>
								<li>
									<div class="box">
										<div class="box-inner">	
											<s:if test="%{#commonQuote.discountedProcessingFee!=null && #commonQuote.discountedProcessingFee>0}">
												<span class="line-through"><span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span></span>
												<span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
											</s:if>
											<s:else>
												<s:if test="%{#commonQuote.processingFee!=null && #commonQuote.processingFee==0}">
								                         <span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{#commonQuote.processingFee})"/></span>
							                     </s:if>
							                     <s:else>
								   		  				<span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{#commonQuote.processingFee})"/></span>
							                      </s:else>
											</s:else>
											<span class="p-content quote-heading">Processing Fee</span>
										</div>
									</div>
								</li>
								<s:if test="%{appElTypeId!=@com.mintstreet.common.util.Constants@APP_EL_TYPE_ID_TAKE_OVER}">
									<li class="edu-li">		
										<div class="box" >
											<div class="box-inner">	
													<s:if test="%{loanScenarioBean.allQuotes.size()>1}">
														<div class="col-lg-12 txt-left" style="padding:0 0px">
															<div class="blue-radio blue-radio-danger">
																<input class="loanAccountType" <s:property value="%{(#assciateQuoteQuoteIndex.index+1==1 )?'checked=checked':''}"/>
																 value="1" id="checkOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
													  name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
													  	 type="radio" name="btn" />
														   		<s:if test="%{appForm.appEducationLoanId == 7 && quote.loanQuoteCountryOfStudyId == 2}">
																	<label for="checkOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">Yes</label>
														   		</s:if>
														   		<s:else>
														   			<label for="checkOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Interest paid during moratorium </label>
														   		</s:else>
															</div>
														</div>
													  	<div class="col-lg-12 txt-left" style="padding:0 0px">
															<div class="blue-radio blue-radio-danger">
																<input class="loanAccountType" <s:property value="%{(quote.loanQuoteLoanAccountType)==2?'checked=checked':''}"/>
																value="2" id="partialCheckOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																type="radio" name="btn"/>
														   		<s:if test="%{appForm.appEducationLoanId == 7 && quote.loanQuoteCountryOfStudyId == 2}">
															  		<label for="partialCheckOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">No</label>
															  	</s:if>
															  	<s:else>
															  		<label for="partialCheckOff_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">No payment during moratorium</label>
															  	</s:else>
														   		
															</div>
													</div>
													<s:if test="%{quote.loanQuoteCountryOfStudyId == 2}">
														<span class="p-content quote-heading">Want to avail coverage under<br>SBI Life Rinn Raksha?</span>
														<span class="p-content quote-heading">Moratorium Type <a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Moratorium period refers the period available for repayment holiday i.e. repayment of instalment (principal and interest) will start after the moratorium period.</span></a></span>
													</s:if>
													<s:else>
														<span class="p-content quote-heading">Moratorium Type <a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Moratorium period refers the period available for repayment holiday i.e. repayment of instalment (principal and interest) will start after the moratorium period.</span></a></span>
													</s:else>
													<%-- </div>
													</div> --%>
											  	 		  
													
											 </s:if>
											<s:elseif test ="%{appForm.appEducationLoanId != 8}">
												<span class="term-loan">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="%{#commonQuote.displayName==null?'Term Loan':#commonQuote.displayName}"/></span>
											</s:elseif>
									</div>
								</div>
							</li>
						</s:if>
						<s:if test="%{loanScenarioBean.showOfferDiscount==1}">
							<li>
								<div class="box">
									<div class="box-inner">	
										<span>
											<s:if test="%{#commonQuote.isDiscountApplied==1}">
												 <s:property value="getText('{0,number,#,###}',{#commonQuote.totalCost - #commonQuote.discountedTotalCost})"/>
											</s:if>
											<s:else>
											--
											</s:else>
										</span>
									</div>
								</div>
							</li>
						</s:if>
							 <%-- <span class="" id="plusminus_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"></span>  --%>
							
					  <li class="last-child">
						<span id="loader1" class="loader-small" style="display: none;"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif" name="loader" /></span>
							<input id="applyLoanOffer<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
								onclick="applyLoanOffer(this, '<s:property value="%{#commonQuote.applyQuoteUrl}"/>');" 
								name="send-btn <s:property value="%{#loanQuoteIndex.index+1}"/> <s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
								type="submit" class="apply-btn" value="Apply Now">
								<a href="javascript:void(0);" onclick="javaScript:window.open('<s:property value="%{#commonQuote.repaymentScheduleUrl}"/>','','navigationtoolbar=no, scrollbars=yes,menubar=no,height=800,width=1000,resizable=yes,toolbar=no,location=no,status=no') " 
								class="emi-schedule-link">EMI schedule</a>&nbsp;
					 </li>
				</ul>
			</s:if>	
		</s:iterator>			
	</div>	
</div>
	
	<%-- <s:include value="/appNew/common/CommonOTPOther.jsp"></s:include> --%>
	<!--discount email-->
	<div class="final-action-panel single-final-action-panel">
		<ul>
			<li>
				<s:set var="needToDisabledToEmail" value="%{'false'}"/>
				<s:if test="%{appForm.appEmailAttemptCount!=null && appForm.appEmailAttemptCount>4}">
					<s:set var="needToDisabledToEmail" value="%{'true'}"/>
				</s:if>
				<s:textfield id="emailSendQuote" name="email" maxlength="60" value="%{appForm.appWorkEmail}" 
				cssClass="form-control input-text " placeholder="Enter your Email" autocomplete="off" disabled="%{needToDisabledToEmail}"/>
				<div class="email-heading">In case you find any difficulty in completing the loan application, a link has been sent to this email id to help you resume the quote.</div>
					<input type="submit" value="Send Quote" name="send-btn" class="send-btn" id="sendQuote">
					<s:property value="%{needToDisabledToEmail=='true'?'disabled send-btn-failure':''}"/>
					<div class="clearfix"></div>
					<div id="loaderEmail"></div>
			</li>
		</ul>
	</div>
	
	<div class="modal fade otp-box" id="productoffer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close"><span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""/></span></button> 
					<div class="offer-left-section">
						<div class="offer-tagline">
							<span class="ribbon-icon"></span>
							<h2><span>OFFERS & </span>INFORMATION</h2>
						</div>	
					</div>
					<div class="offer-right-section">
						<ul>
							 <s:if test="%{appForm.appEducationLoanId==1}">
					        		<li>
							        	<span>Bank may also provide loan for payment of premium on <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Life insurance.</span>
						        	</li>
						        	<s:if test="%{quote.loanQuoteisSbiEmployee==null || quote.loanQuoteisSbiEmployee.equalsIgnoreCase('N')}">
							        	<li>
								        	<span>An additional concession of 1% in interest rate is available, if interest serviced during the moratorium period.</span>
							        	</li>
						        	</s:if>
						        	<s:if test="%{(appForm.appLoanAmount+quote.loanQuoteOutstandingLoanAmount/100000.0)>7.5}">
						        		<li>
								        	<span> <strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security for full value of loan amount.</span>
							        	</li>
						        	</s:if>
					        	</s:if>
					        	<s:elseif test="%{appForm.appEducationLoanId==2}">
					        		<s:if test="%{quote.loanQuoteInstituteCat!=null && (quote.loanQuoteInstituteCat.equalsIgnoreCase('A') || quote.loanQuoteInstituteCat.equalsIgnoreCase('AA'))}">
					        			<li>
								        	<span>Bank may also provide loan for payment of premium on <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Life insurance.</span>
							        	</li>
							        	<%-- <li>
								        	<span>If required, Bank may ask for furnishing of appropriate amount of collateral security against the availed loan amount.</span>
							        	</li> --%>
					        		</s:if>
					        		<s:elseif test="%{quote.loanQuoteInstituteCat!=null && quote.loanQuoteInstituteCat.equalsIgnoreCase('B')}">
					        			<li>
								        	<span>Bank may also provide loan for payment of premium on <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Life insurance.</span>
							        	</li>
							        	<s:if test="quote.loanQuoteWorkExperience == 2 && quote.loanQuoteCollateral <= appForm.appLoanAmount*100000.0">
								        	<li>
									        	<span>Bank may provide additional concession of 25 bps in interest rate on provision of collateral security greater than the availed loan amount.</span>
								        	</li>
							        	</s:if>
					        		</s:elseif>
					        		<s:elseif test="%{quote.loanQuoteInstituteCat!=null && quote.loanQuoteInstituteCat.equalsIgnoreCase('C')}">
					        			<li>
								        	<span>Bank may also provide loan for payment of premium on <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> Life insurance.</span>
							        	</li>
							        	<s:if test="%{quote.loanQuoteisSbiEmployee==null || quote.loanQuoteisSbiEmployee.equalsIgnoreCase('N')}">
								        	<li>
									        	<span>An additional concession of 1% in interest rate is available, if interest serviced during the moratorium period.</span>
								        	</li>
							        	</s:if>
					        		</s:elseif>
					        	</s:elseif>
					        		<s:elseif test="%{appForm.appEducationLoanId==3}">
					        		<li>
							        	<span>An additional concession of 1% in interest rate is available, if interest serviced during the moratorium period.</span>
						        	</li>
					        	</s:elseif>
					        	<s:if test="%{quote.loanQuoteInstituteCat!=null && (quote.loanQuoteInstituteCat.equalsIgnoreCase('A') || quote.loanQuoteInstituteCat.equalsIgnoreCase('B'))}">
					        		<s:if test="%{quote.loanQuoteOutstandingLoanAmount/100000.0>0}">
						        		<s:if test="%{(appForm.appLoanAmount+quote.loanQuoteOutstandingLoanAmount/100000.0)>20 && (appForm.appLoanAmount+quote.loanQuoteOutstandingLoanAmount/100000.0)<=30}">
							        		<li>
									        	<span><strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security for full value of loan amount.</span>
								        	</li>
							        	</s:if>
						        	</s:if>
						        	<s:elseif test="%{appForm.appLoanAmount>20 && appForm.appLoanAmount<=30}">
						        		<li>
								        	<span><strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security for full value of loan amount.</span>
							        	</li>
						        	</s:elseif>
					        	</s:if>
				        		<s:elseif test="%{quote.loanQuoteInstituteCat!=null && quote.loanQuoteInstituteCat.equalsIgnoreCase('C')}">
				        		<s:if test="%{(appForm.appLoanAmount+quote.loanQuoteOutstandingLoanAmount/100000.0)>7.5}">
					        		<li>
							        	<span><strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security for full value of loan amount.</span>
						        	</li>
					        	</s:if>
			        		</s:elseif>
		        			<s:elseif test="%{appForm.appEducationLoanId == 7}">
						       	<li>
				        			<span>Bank may also provide loan for payment of premium of SBI life 'Rinn Raksha'</span>
						       	</li>
				        		<li>
						       		<span>An additional concession of 0.50% in interest rate is available. If loan is covered under SBI Life 'Rinn Raksha'.</span>
						       	</li>
						       	<li>
						       		<span><strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security as applicable under the scheme.</span>
						       	</li>
			        		</s:elseif>
			        		<s:elseif test="%{appForm.appEducationLoanId == 8}">
			        			<li>
							        <span><strong>Disclaimer:</strong> The above loan eligibility is valid only subject to provision of collateral security for full value of loan amount.</span>
						        </li>
			        		</s:elseif>
			        	
						</ul>
					</div>
				</div>
			</div>
		</div>	
	</div>
</form>
<s:include value="/appNew/common/CommonOTPOther.jsp"></s:include>
   	<!-- OTP Box -->
   	<%-- <div id="displayEngineResponse" style="display:none;">
		Request ::  <s:property value="%{quote.requestStr}"/>
		  <br></br>
		Response :: <s:property value="%{quote.responseStr}"/>
    </div> --%>
<s:if test="%{isDsrPage=='false'}">
<script type="text/javascript">
	jQuery(document).ready(function() {
		jQuery( "#slider-elig" ).slider({
	            range: 'min',
	            animate: true,
	            value: <s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>,
	            min: <s:property value="%{loanScenarioBean.minEligibility}"/>,
	            max: <s:property value="%{loanScenarioBean.maxEligibility}"/>,
	            step: <s:property value="%{loanScenarioBean.productSliderDigit}"/>,
	            slide: function(event, ui) { jQuery( "#slider-elig-amount" ).html( ui.value.toFixed(2)+ " <span>lakhs</span>");jQuery( "#chosenEligibility" ).val( ui.value.toFixed(2) );},
	            stop: function(event, ui) {
	          	  		changeQutotes();
	            	  }
	    	 	});
		
	            $( "#slider-elig-amount" ).html(parseFloat(<s:property value="%{loanScenarioBean.chosenEligibility}"/>).toFixed(2) + " <span>lakhs</span>" );
		      	jQuery( "#slider-tenure" ).slider({
		              range: 'min',
		              animate: true,
		              value: <s:property value="%{loanScenarioBean.chosenTenure}"/>,
		              min: <s:property value="%{loanScenarioBean.minTenure}"/>,
		              max: <s:property value="%{loanScenarioBean.maxTenure}"/>,
		              step: <s:property value="%{loanScenarioBean.productSliderTenureChn}"/>,
		              slide: function(event, ui) { jQuery( "#slider-tenure-amount" ).html( ui.value+ " <span>months</span>" );jQuery( "#chosenTenure" ).val( ui.value );},
		              stop: function(event, ui) {
		            	  changeQutotes();  
					  }
		          });
		      	
	              $( "#slider-tenure-amount" ).html($( "#slider-tenure" ).slider( "value" ) + " <span>months</span>" );
			      	jQuery("#productSaveEmail").val('<s:property value="%{#firstSaveEmailURL}"/>');
			      	//jQuery("#chosenLoanAccountType").val('<s:property value="%{quote.loanQuoteLoanAccountType}"/>');
			      	//jQuery("#chosenProductId").val('<s:property value="%{quote.loanQuoteLoanProductId}"/>');
				});
	        
			var isOfferPage= true;
			<s:if test="%{isDsrPage=='false'}">
				<s:if test="%{#session.bankLMSUser==null}">
					<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('N')}">
						dropOffPopupStatus=0;
					</s:if>
				</s:if>
		  	</s:if> 
		  	
	  	 jQuery(document).unbind("click").on("click", "#productSecondPageLink, #productLeftSideBarLink", function(){
			loanFirstPage();
		}); 
	</script>
</s:if>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>		
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/educationloan/js<s:property value="#minFolderPath"/>/jquery.educationloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
