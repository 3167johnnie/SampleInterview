<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.ui.slider.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
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
							<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.maxEligibility})"/> <s:property value="%{loanScenarioBean.maxEligibility>1?'lakhs':'lakh'}"/>
						</s:if>
						<s:else>
							<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.maxEligibility})"/> <s:property value="%{loanScenarioBean.maxEligibility>1?'lakhs':'lakh'}"/>
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
				<!-- <input type="text" id="slider-tenure-amount" readonly class="slider-txt"> -->
				<div id="slider-tenure"></div>
				<div class="range-div">
					<div class="flt width-33">
						<s:if test="%{loanScenarioBean.productSliderTenure ==1}">
							<s:property value="%{loanScenarioBean.minTenure}"  /> months
						</s:if>
						<s:else>
							<s:property value="%{loanScenarioBean.minTenure}"  /> years
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
						<s:select headerKey="0" headerValue="Select Eligibility" 
							value="%{loanScenarioBean.chosenEligibility}" 
							name="manualEligVal" list="%{loanScenarioBean.manualEligVal}"
							cssClass="form-select"  
							onchange="return validateFirst('chosenEligibility', this, '%{loanScenarioBean.maxEligibility}');" onfocus="customOnFocus(this);" onblur="customOnBlur(this);"/>
					</div>
				</li>
				<li>
					<label>Select tenure</label>
					<div class="flat-field">
						 <s:select headerKey="0" headerValue="Select Tenure" 
							value="loanScenarioBean.chosenTenure" 
							name="manualTenureVal" list="%{loanScenarioBean.manualTenureVal}"
							cssClass="form-select" 
							onchange="return validateFirst('chosenTenure', this, '%{loanScenarioBean.maxTenure}' );" onfocus="customOnFocus(this);"  onblur="customOnBlur(this);"/>
					</div>
				</li>
				<li>
					<input id="changeLoanQuote" name="change-btn" class="apply-btn" value="Change" type="submit">
				</li>
			</ul>
		</div>
		<s:hidden name="chosenTenure" id="chosenTenure" value="%{loanScenarioBean.chosenTenure}"/>
		<s:hidden name="chosenEligibility" id="chosenEligibility" value="%{loanScenarioBean.chosenEligibility}" />
		<s:hidden name="chosenProductId" id="chosenProductId" value="%{loanScenarioBean.chosenProductId}" />
		<s:hidden name="chosenLoanAccountType" id="chosenLoanAccountType" value="%{loanScenarioBean.chosenLoanAccountType}" />
		<s:hidden name="productSaveEmail" id="productSaveEmail" value=""/>
		
		<div class="text-center loan-quote-loader" id="loan-quote-loader" style="display:none">
			<div class="loan-quote-loader-img"><img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader-horizontal.gif" width="35"/> </div> 
		</div>
	</div>
	<div class="clearfix"></div>
	<!--loan offers-->
	<div class="loan-offers-container">
		<h3 class="flt">YOUR LOAN QUOTES</h3>
		<div class="offer-btn">
		 	<a class="product-offers" href="#" data-target="#productoffer" data-toggle="modal" >Click to view Offers</a>
		 </div>
		 <s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4)}">
			<s:if test="%{quote.loanQuotePuccaHouse==2 && (quote.loanQuoteEligiblePmay==1 || quote.loanQuoteEligiblePmay==2)}">
				<s:if test ="%{loanScenarioBean.chosenTenure>=21 }">
					<div class="brps-offer">
						<ul> 
							 <li>
								<span>PMAY Offer:</span> Please change the tenure to 20 years to avail loan under PMAY SCHEME for availing eligible subsidy.
							 </li>
						</ul>
					</div>
			  	</s:if>
			  	<s:else>
		        	 <s:if test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME1 && loanScenarioBean.chosenTenure <= 20}">
					 	<div class="brps-offer">
					 		<ul>
						 		<li><span>PMAY Offer:</span> Maximum subsidy available under PMAY CLSS EWS is capped at Rs 2.68 lakhs.</li>
						 		<li> <span>PMAY Offer:</span> Offer is subject to maximum carpet area and other stipulations as applicable.</li>
					 		</ul>
					 	</div>
					 </s:if> 
					  <s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME2 && loanScenarioBean.getChosenTenure <= 20 }">
					 		<div class="brps-offer">
					 			<ul>
					 				<li><span>PMAY Offer:</span> Maximum subsidy available under PMAY CLSS LIG is capped at Rs 2.68 lakhs.<br></li>
					 				<li><span>PMAY Offer:</span> Offer is subject to maximum carpet area and other stipulations as applicable.</li>
						 		</ul>
					 		</div>
					  </s:elseif> 
					 <s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME3 && loanScenarioBean.getChosenTenure <= 20}">
					 	<div class="brps-offer">
						 	<ul>
							 	<li><span>PMAY Offer:</span> Maximum subsidy available under PMAY CLSS  MIG-I is capped at Rs 2.35 lakhs.</li>
							 	<li><span>PMAY Offer:</span> Offer is subject to maximum carpet area and other stipulations as applicable.</li>
						 </ul>
						 </div>
					 </s:elseif>
					<s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME4 && loanScenarioBean.getChosenTenure <= 20}">
			 			<div class="brps-offer">
				 			<ul>
					 			<li><span>PMAY Offer:</span> Maximum subsidy available under PMAY CLSS  MIG-II is capped at Rs 2.30 lakhs.</li>
					 			<li><span>PMAY Offer:</span> Offer is subject to maximum carpet area and other stipulations as applicable.</li>
			 				</ul>
			 			</div>
				 </s:elseif>
			 </s:else>
			  </s:if> 
			  
		 </s:if> 	
		<s:iterator value="loanScenarioBean.loanQuotes"  status="loanQuoteIndex" var="loanQuotesOuter">
			<s:set var="accountTermOD" value="value" />
			<s:set var="accountTypeTerm" value="#accountTermOD.accountTypeTerm" />
			<s:set var="accountTypeOverDraft" value="#accountTermOD.accountTypeOverDraft" />
			<s:set var="showTermOverDraft" value="%{1}"/>
			<s:if test="#accountTypeTerm==null && #accountTypeOverDraft==null ">
				<s:set var="showTermOverDraft" value="%{0}"/>
			</s:if>
			<s:set var="isEligibleToShow" value="%{1}"/>
			<s:iterator value="#{1:'1',2:'2'}" status="assciateQuoteQuoteIndex" var="assciateQuote">
				<s:if test="%{#assciateQuoteQuoteIndex.index==0}">
					<s:set var="commonQuote" value="%{#accountTypeTerm}" />
					<s:set var="isEligibleToShow" value="%{1}"/>
				</s:if>
				<s:else>
					<s:set var="commonQuote" value="%{#accountTypeOverDraft}" />
					<s:if test="%{#showTermOverDraft==0}">
						<s:set var="isEligibleToShow" value="%{1}"/>
					</s:if>
					<s:else>
						<s:set var="isEligibleToShow" value="%{0}"/>
					</s:else>
				</s:else>
				<s:if test="%{#commonQuote!=null}">
					<s:if test="%{#assciateQuoteQuoteIndex.index==0}">
						<s:set var="firstSaveEmailURL" value="%{#commonQuote.applyQuoteUrl}"/>
					</s:if>
					<div class="loan-offer-div">
						<h2><s:property value="%{#commonQuote.productTypeName}"/>
						<s:if test="%{loanScenarioBean.chosenTenure <=  20}">
							<s:if test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME1  }">
						 		<span >(PMAY CLSS EWS)</span></s:if>
						 	<s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME2}">
						 		<span >(PMAY CLSS LIG)</span>
						 	</s:elseif>
						 	<s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME3}">
						 		<span >(PMAY CLSS MIG-I)</span>
						 	</s:elseif>
						 	<s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME4}">
						 		<span >(PMAY CLSS MIG-II)</span>
						 	</s:elseif>
					 	</s:if>
					 	<span class="p-content">(Product type)</span></h2>
						<s:if test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
							<ul id="<s:property value="%{#loanQuoteIndex.index+1}"/>-<s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"  style="display:<s:property value="%{#commonQuote.chosenAccountTypeId==#assciateQuoteQuoteIndex.index+1?'block':'none'}"/>;" class="bhl-loan-offer">
								<li>
									<div class="box">
										<div class="box-inner">
											<span class="font-rupee">`</span> 
											<s:if test="%{loanScenarioBean.productSliderDigitExact==1}">
												<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>
												<s:property value="%{loanScenarioBean.chosenEligibility>1?'lakhs':'lakh'}"/>
											</s:if>
											<s:else>
												<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>
												<s:property value="%{loanScenarioBean.chosenEligibility>1?'lakhs':'lakh'}"/>
											</s:else>
											<span class="p-content quote-heading">Loan Amount</span>
										 </div>
									 </div>	
								</li>
								<li class="bhl-interest-rate">
									<div class="box">
										<div class="box-inner">
											
												 <span class="">1 - 12 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.appBhlInterestRate})"/>%</span>
												 <s:if test="%{(appForm.appGender!=null && appForm.appGender.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_1!=null && appForm.appCoapplicantGender_1.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_2!=null && appForm.appCoapplicantGender_2.equalsIgnoreCase('F'))}">
												 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.discountedInterestRate})"/>%</span>
												 </s:if>
												 <s:else>
									 			 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.interestRate})"/>%</span>
												 </s:else>
												 <span class="p-content quote-heading">Interest Rate</span>
											
										</div>
									</div>
								</li>
								
								<li class="bhl-interest-prd">
									<div class="box">
										<div class="box-inner">
											<span>1 - 12 months: <em class="font-rupee">`</em><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.appBhlFirstInterestEmi)})"/></span>
											<span>13 - 24 months: <em class="font-rupee">`</em><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span>
											<span class="p-content quote-heading">Interest</span>
										</div>
									</div>
								</li>
								
								<li>
									<div class="box">
										<div class="box-inner">
											<s:if test="%{#commonQuote.discountedProcessingFee!=null && #commonQuote.discountedProcessingFee>=0 && #commonQuote.processingFee !=  #commonQuote.discountedProcessingFee}">
												<s:if test="%{#commonQuote.discountedProcessingFee==0}">
													<span class="line-through"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
													<span class="font-rupee">`</span>
													<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
												</s:if>
												<s:else>
													<span class="line-through"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
													<span class="font-rupee">`</span>
													<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
												</s:else>
											</s:if>
											<s:else>
												<s:if test="%{#commonQuote.processingFee!=null && #commonQuote.processingFee==0}">
												<span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
												</s:if>
												<%-- <span class="font-rupee">`</span> --%>
												<s:else>
													<span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
												</s:else>
											</s:else>
											<span class="p-content quote-heading">Processing Fee</span>
										</div>
									</div>
								</li>
								<li>
								 <div class="box">
									<div class="box-inner">
										<s:if test="%{loanScenarioBean.chosenProductId!=@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
												<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" checked="checked" value="1" 
															id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															type="radio" name="btn"/>
														<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
														</div>
													</div>
												</s:if>
												<s:else>
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==1)?'checked=checked':''}"/>
															value="1" id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															type="radio" name="btn"/>
															<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
														</div>
													</div>
												</s:else>
												<s:if test="%{loanScenarioBean.showSecondTermOverDraftCheck==1}">
													<s:if test="%{appForm.appLoanAmount>=20}">
														<s:if test="%{#commonQuote.productTypeId!=@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
															<div class="clearfix"></div>	
															<div class="col-lg-12 txt-left" style="padding:0 0px" >
																<div class="blue-radio blue-radio-danger">
																	 <input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==2)?'checked=checked':''}"/>
																	 value="2" id="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																	  name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																	  type="radio" name="btn" />
																	<label for="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">Overdraft </label>
																</div>
																<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the overdraft account (with an option to withdraw whenever required)</span></a>
															</div>
														</s:if>
													</s:if>
												</s:if>
												<s:else>
													<s:if test="%{appForm.appLoanAmount>=20}">
														<s:if test="%{#commonQuote.productTypeId!=10}">
															<s:if test="%{#commonQuote.productTypeId!=11}">
																<s:if test="%{#commonQuote.productTypeId!=3}">
																	<s:if test="%{#commonQuote.productTypeId!=4}">
																		<div class="clearfix"></div>	
																		<div class="col-lg-12 txt-left" style="padding:0 0px" >
																			<div class="blue-radio blue-radio-danger">
																				<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==2)?'checked=checked':''}"/>
																				 value="2" id="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																				  name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																				  type="radio" name="btn" />
																				<label for="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">Overdraft </label>
																			</div>
																			<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the overdraft account (with an option to withdraw whenever required).EMI arrived at based on the drawing power and may get reduced relatively, depending on the amount actually drawn.</span></a>
																		</div>
																	</s:if>
																</s:if>
															</s:if>
														</s:if>
													</s:if>
												</s:else>
											</s:if>
											<s:else>
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==1)?'checked=checked':''}"/>
															value="1" id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															type="radio" name="btn"/>
															<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
														</div>
													</div>
												</s:else>
											<span class="p-content quote-heading">A/c Type</span>
										</div>
									</div>
								</li>
								<li class="last-child">
									<span id="loader1" class="loader-small" style="display: none;"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif" name="loader" /></span>
									<input id="applyLoanOffer<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
										onclick="applyLoanOffer(this, '<s:property value="%{#commonQuote.applyQuoteUrl}"/>');" 
										name="send-btn<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
										type="submit" class="apply-btn" value="Apply Now">
									<a href="javascript:void(0);" onclick="javaScript:window.open('<s:property value="%{#commonQuote.repaymentScheduleUrl}"/>','','navigationtoolbar=no, scrollbars=yes,menubar=no,height=800,width=1000,resizable=yes,toolbar=no,location=no,status=no') " 
										class="emi-schedule-link">EMI schedule</a>	
								</li>
							</ul>
						</s:if>
						<s:else>
							<ul id="<s:property value="%{#loanQuoteIndex.index+1}"/>-<s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"  style="display:<s:property value="%{#commonQuote.chosenAccountTypeId==#assciateQuoteQuoteIndex.index+1?'block':'none'}"/>;">
								<li>
									<div class="box">
										<div class="box-inner">
											<span class="font-rupee">`</span> 
											<s:if test="%{loanScenarioBean.productSliderDigitExact==1}">
												<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>
												<s:property value="%{loanScenarioBean.chosenEligibility>1?'lakhs':'lakh'}"/>
											</s:if>
											<s:else>
												<s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>
												<s:property value="%{loanScenarioBean.chosenEligibility>1?'lakhs':'lakh'}"/>
											</s:else>
											<span class="p-content quote-heading">Loan Amount</span>
										 </div>
									 </div>	
								</li>
								<%-- <li>
									<div class="box">
										<div class="box-inner">
											 <s:if test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
											 	<div class="rate-bhl">
													 <span class="">1 - 12 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.appBhlInterestRate})"/>%</span>
													 <s:if test="%{(appForm.appGender!=null && appForm.appGender.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_1!=null && appForm.appCoapplicantGender_1.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_2!=null && appForm.appCoapplicantGender_2.equalsIgnoreCase('F'))}">
													 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.discountedInterestRate})"/>%</span>
													 </s:if>
													 <s:else>
										 			 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.interestRate})"/>%</span>
													 </s:else>
												 </div>
											</s:if>
											<s:else>
												<s:if test="%{#commonQuote.discountedInterestRate!=null && #commonQuote.discountedInterestRate>0}">
													<span class="line-through"><s:property value="getText('{0,number,##.00}',{#commonQuote.interestRate})"/>%</span>
													<span class=""><s:property value="getText('{0,number,##.00}',{#commonQuote.discountedInterestRate})"/>%</span>
												</s:if>
												<s:else>
													<span class=""><s:property value="getText('{0,number,##.00}',{#commonQuote.interestRate})"/>%</span>
												</s:else>
											</s:else>
											 <s:if test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
												<span class="p-content quote-heading">Interest Rate</span>
											</s:if>
											<s:else>
												<span class="p-content quote-heading">Best Rate</span>
											</s:else>
											<span class="p-content">(<s:property value="%{#commonQuote.rateType==1?'Floating':'fixed'}"/>)</span>
										</div>
									</div>	
								</li> --%>
								
								<s:if test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
									<li>
									<div class="box">
										<div class="box-inner">
											<div class="rate-bhl">
												 <span class="">1 - 12 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.appBhlInterestRate})"/>%</span>
												 <s:if test="%{(appForm.appGender!=null && appForm.appGender.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_1!=null && appForm.appCoapplicantGender_1.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_2!=null && appForm.appCoapplicantGender_2.equalsIgnoreCase('F'))}">
												 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.discountedInterestRate})"/>%</span>
												 </s:if>
												 <s:else>
									 			 	<span class="">13 - 24 months: <s:property value="getText('{0,number,#,##.00}',{#commonQuote.interestRate})"/>%</span>
												 </s:else>
												 <span class="p-content quote-heading">Interest Rate</span>
											</div>
										</div>
									</div>
									</li>
								</s:if>
								<s:else>
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
												<span class="p-content quote-heading">Best Rate</span>
												<p>*The interest rate may be less depending on your CIBIL Score. <a href="<s:property value="%{@com.mintstreet.common.util.Constants@SBI_HOMELOANS}"/>downloads/HL_ROI.jpg" target="_blank">Click here</a>for  more details. CIBIL Score range 300-549 will not be considered for sanction.</p>
										</div>
									</div>
									</li>
								</s:else>
								
								<s:if test="%{#commonQuote.discountedEmi!=null && #commonQuote.discountedEmi>0}">
									<s:if test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
										<li>
											<div class="box">
												<div class="box-inner">
													1 - 12 months: <span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.appBhlFirstInterestEmi)})"/>
													13 - 24 months: <span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/>
													<span class="p-content quote-heading">Interest</span>
												</div>
											</div>
										</li>
			               			 </s:if>
									<s:elseif test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
										<li class="flxi-emis">
										<div class="box">
											<div class="box-inner">
													<span class="emi-sec"> Only interest<span class="p-content"> (<s:property value="%{tenure1Duration}"/> mth)</span></span>
													<div class="clearfix"></div>
													
													<span class="emi-sec"> <span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/>:<span class="p-content"> (<s:property value="%{tenure2Duration}"/> mth)</span></span>
													<div class="clearfix"></div>
													
													<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/>:<span class="p-content"> (<s:property value="%{tenure3Duration}"/> mth)</span> </span>
													<div class="clearfix"></div>
													
													<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/>:<span class="p-content"> (<s:property value="%{tenure4Duration}"/> mth)</span> </span>
													<span class="p-content quote-heading">EMI</span>
												</div>
										 </div>	
										</li>
									</s:elseif>
									<s:else>
										<li>
											<div class="box">
												<div class="box-inner">
													<s:if test="%{#commonQuote.discountedEmi!=null && #commonQuote.discountedEmi>0}">
														<span class="line-through"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span>
														<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedEmi)})"/>
													</s:if>
													<s:else>
														<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/>
													</s:else>
														<span class="p-content quote-heading">EMI</span>
												</div>
											  </div>
										</li>
									</s:else>
								</s:if>
								<s:else>
									<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
										<li class="flxi-emis">
											<div class="box">
												<div class="box-inner">
													<span class="emi-sec">Only interest<span class="p-content"> (<s:property value="%{tenure1Duration}"/> mth)</span> </span>
													<div class="clearfix"></div>
													<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/><span class="p-content"> (<s:property value="%{tenure2Duration}"/> mth)</span></span>
													<div class="clearfix"></div>
													<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/><span class="p-content"> (<s:property value="%{tenure3Duration}"/> mth)</span></span>
													<div class="clearfix"></div>
													<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/><span class="p-content"> (<s:property value="%{tenure4Duration}"/> mth)</span></span>
													<span class="p-content quote-heading">EMI</span>
												</div>
											  </div>
										</li>
									</s:if>	

									<s:elseif test="%{(#commonQuote.discountedEmi1>0 && #commonQuote.discountedEmi2>0)}">
									<li>
											<div class="box">
												<div class="box-inner">
												<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedEmi1)})"/> 
													</br>
												<span class="p-content quote-heading"> Upto Retirement </span>
												<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.repaymentMonth1)})"/>	
													</br>
												<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedEmi2)})"/>
													</br>
												<span class="p-content quote-heading"> Post Retirement </span>
													</br>
												<span class="p-content quote-heading">(EMI)</span>
												</div>
											</div>
									</li>
									</s:elseif>
									
									<s:elseif test="%{(#commonQuote.emi1>0 && #commonQuote.emi2>0)}">
									<li>
											<div class="box">
												<div class="box-inner">
												<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi1)})"/> 
													</br>
												<span class="p-content quote-heading"> Upto Retirement </span>
												<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.repaymentMonth1)})"/>	
													</br>
												<span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi2)})"/>
													</br>
												<span class="p-content quote-heading"> Post Retirement </span>
													</br>
												<span class="p-content quote-heading">(EMI2)</span>
												</div>
											</div>
									</li>
									</s:elseif>
									
									<s:else>
										<li>
											<div class="box">
												<div class="box-inner">
													<span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/>
													<span class="p-content quote-heading">EMI</span>
												</div>
											 </div>
										</li>
									</s:else>
								</s:else>
								<li id="pro_fee">
									<div class="box">
										<div class="box-inner">
											<s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4) && quote.loanQuotePuccaHouse==2}">
												<%-- <s:if test="%{quote.loanQuotePuccaHouse==2}"> --%>
													<s:if test ="%{loanScenarioBean.chosenTenure<=20 && (quote.loanQuoteEligiblePmay ==1 || quote.loanQuoteEligiblePmay ==2 || quote.loanQuoteEligiblePmay ==3 || quote.loanQuoteEligiblePmay ==4)  }">
														<span class="font-rupee">`</span>0
													</s:if>
													<s:else>
														<s:if test="%{#commonQuote.processingFee!=null && #commonQuote.processingFee==0}">
                                                            <span class="font-rupee">`</span>
															<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
                                                        </s:if>
                                                        <s:elseif test="%{#commonQuote.discountedProcessingFee!=null && #commonQuote.discountedProcessingFee>=0 && #commonQuote.processingFee !=  #commonQuote.discountedProcessingFee}">
															<span class="line-through"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
															<span class="font-rupee">`</span>
															<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
                                                        </s:elseif>
                                                        <s:else>
														    <span class="font-rupee">`</span>
															<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
                                                        </s:else>
													</s:else>
												<%-- </s:if> --%>
											</s:if>
											<s:else>
												<s:if test="%{#commonQuote.discountedProcessingFee!=null && #commonQuote.discountedProcessingFee>=0 && #commonQuote.processingFee !=  #commonQuote.discountedProcessingFee}">
													<s:if test="%{#commonQuote.discountedProcessingFee==0}">
														<span class="line-through"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
														<span class="font-rupee">`</span>
														<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
													</s:if>
													<s:else>
														<span class="line-through"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
														<span class="font-rupee">`</span>
														<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
													</s:else>
												</s:if>
												<s:else>
													<s:if test="%{#commonQuote.processingFee!=null && #commonQuote.processingFee==0}">
													<span class="font-rupee">`</span>
														<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
													</s:if>
													<%-- <span class="font-rupee">`</span> --%>
													<s:else>
														<span class="font-rupee">`</span>
														<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
													</s:else>
												</s:else>
											</s:else>
											<span class="p-content quote-heading">Processing Fee</span>
										</div>
									</div>
								</li>
								<li class="ac-type-cls">
									<div class="box">
										<div class="box-inner">
										<s:if test="%{loanScenarioBean.chosenProductId!=@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
												<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" checked="checked" value="1" 
															id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															type="radio" name="btn"/>
														<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
														</div>
													</div>
												</s:if>
												<s:else>
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==1)?'checked=checked':''}"/>
															value="1" id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
															type="radio" name="btn"/>
															<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
														</div>
													</div>
												</s:else>
												<s:if test="%{loanScenarioBean.showSecondTermOverDraftCheck==1}">
													<s:if test="%{appForm.appLoanAmount>=20}">
														<s:if test="%{#commonQuote.productTypeId!=@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
															<div class="clearfix"></div>	
															<div class="col-lg-12 txt-left" style="padding:0 0px" >
																<div class="blue-radio blue-radio-danger">
																	 <input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==2)?'checked=checked':''}"/>
																	 value="2" id="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																	  name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																	  type="radio" name="btn" />
																	<label for="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">Overdraft </label>
																</div>
																<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the overdraft account (with an option to withdraw whenever required)</span></a>
															</div>
														</s:if>
													</s:if>
												</s:if>
												<s:else>
													<s:if test="%{appForm.appLoanAmount>=20}">
														<s:if test="%{#commonQuote.productTypeId!=10}">
															<s:if test="%{#commonQuote.productTypeId!=11}">
																<s:if test="%{#commonQuote.productTypeId!=3}">
																	<s:if test="%{#commonQuote.productTypeId!=4}">
																		<div class="clearfix"></div>	
																		<div class="col-lg-12 txt-left" style="padding:0 0px" >
																			<div class="blue-radio blue-radio-danger">
																				<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==2)?'checked=checked':''}"/>
																				 value="2" id="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																				  name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																				  type="radio" name="btn" />
																				<label for="accountTypeOverdraft_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>">Overdraft </label>
																			</div>
																			<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the overdraft account (with an option to withdraw whenever required).EMI arrived at based on the drawing power and may get reduced relatively, depending on the amount actually drawn.</span></a>
																		</div>
																	</s:if>
																</s:if>
															</s:if>
														</s:if>
													</s:if>
												</s:else>
											</s:if>
											<s:else>
												<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
														<div class="col-lg-12 txt-left" style="padding:0 0px">
															<div class="blue-radio blue-radio-danger">
																<input class="loanAccountType" checked="checked" value="1" 
																id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																type="radio" name="btn"/>
															<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
															</div>
														</div>
													</s:if>
													<s:else>
														<div class="col-lg-12 txt-left" style="padding:0 0px">
															<div class="blue-radio blue-radio-danger">
																<input class="loanAccountType" <s:property value="%{(loanScenarioBean.quoteAccountType==1)?'checked=checked':''}"/>
																value="1" id="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																name ="accountType_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
																type="radio" name="btn"/>
																<label for="accountTypeTerm_<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
															</div>
														</div>
													</s:else>
											</s:else>
											<span class="p-content quote-heading">A/c Type</span>
										</div>
									</div>
								</li>
								<li class="last-child">
										<span id="loader1" class="loader-small" style="display: none;"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif" name="loader" /></span>
										<input id="applyLoanOffer<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
											onclick="applyLoanOffer(this, '<s:property value="%{#commonQuote.applyQuoteUrl}"/>');" 
											name="send-btn<s:property value="%{#loanQuoteIndex.index+1}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
											type="submit" class="apply-btn" value="Apply Now">
										<a href="javascript:void(0);" onclick="javaScript:window.open('<s:property value="%{#commonQuote.repaymentScheduleUrl}"/>','','navigationtoolbar=no, scrollbars=yes,menubar=no,height=800,width=1000,resizable=yes,toolbar=no,location=no,status=no') " 
											class="emi-schedule-link">EMI schedule</a>	
									
								</li>
							</ul>
						</s:else>
					</div>
				</s:if>
			</s:iterator>
		</s:iterator>
	</div>
	<!--discount email callback-->
	<div class="final-action-panel">
		<ul>
			<li>
				<s:textfield id="discountCouponName" name="discountCouponName" value="%{quote.loanQuoteAppliedCoupon}" cssClass="form-control discount-text" placeholder="Have a discount code?"  autocomplete="off" maxlength="6"  />
				<input id="applyDiscountCoupon" type="submit" value="Enter" class="brd-btn">
				<div class="clearfix"></div>
				<div id="loaderDiscount" class="discount-msg"><span></span></div>
				
			</li>
			<li style="margin-left: 20px;">
				<s:set var="needToDisabledToEmail" value="%{'false'}"/>
				<s:if test="%{(appForm.appEmailAttemptCount!=null && appForm.appEmailAttemptCount>4)}">
					<s:set var="needToDisabledToEmail" value="%{'true'}"/>
				</s:if>
				<s:textfield id="emailSendQuote" name="email" maxlength="60" value="%{appForm.appWorkEmail}" cssClass="form-control input-text"
				 placeholder="Enter your Email" autocomplete="off" disabled="%{needToDisabledToEmail}"/>
				<div class="email-heading">In case you find any difficulty in completing the loan application, a link has been sent to this email id to help you resume the quote.</div>
				<input type="submit" value="Send Quote" name="send-btn" class="send-btn" id="sendQuote" >
				<s:property value="%{needToDisabledToEmail=='true'?'disabled send-btn-failure':''}"/>
				<div class="clearfix"></div>
				
				<div id="loaderEmail"></div>
			</li>
			<li>
				<s:if test="%{appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified.equalsIgnoreCase('N')}">
			 		<a href="javascript:void(0);" class="call-btn call-icon call_us">Get a callback</a>
				</s:if>
				<s:else>
					<a href="javascript:void(0);" class="call-btn call-icon disabled" data-bs-toggle="modal" data-target="#productCallBack">Get a Callback</a>
				</s:else>
				
			</li>
		</ul>
	</div>
	<!--discount email callback-->
	<!-- OFFERS popup -->
	<div class="modal fade otp-box" id="productoffer" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content">
		  <div class="modal-body">
		  <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close"><span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""/></span></button> 
				<div class="offer-left-section">
					<div class="offer-tagline">
						<span class="ribbon-icon"></span>
						<h2 style="text-align:center;"><span>OFFERS & </span>INFORMATION</h2>
					</div>	
				</div>
				<div class="offer-right-section">
					<ul>
					<s:if test="%{loanScenarioBean.chosenProductId!=@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
							<s:if test="%{loanScenarioBean.showOffer==1 }">
								<li><strong>SBI FlexiPay Loan:</strong> Pay only the interest applied on Home Loan during the first <s:property value="%{appForm.appMoratorimMonths}"/> months. Flexi EMIs start after completion of <s:property value="%{appForm.appMoratorimMonths}"/> months.</li>
							</s:if>
							<s:if test="%{loanScenarioBean.productSliderTenure ==1}">
								<li><strong>Disclaimer:</strong> "the sanctioning of the loan amount is subject to availability of sufficient, regular and continuous source of income for servicing the loan repayment"</li>
							</s:if>
							<s:if test="%{(quote.loanQuoteCoapplicantFirstRelationshipId!=null && quote.loanQuoteCoapplicantFirstRelationshipId==5)
								|| (quote.loanQuoteCoapplicantSecondRelationshipId!=null && quote.loanQuoteCoapplicantSecondRelationshipId==5)}">
								<li><strong>Disclaimer:</strong> The above loan amount is subject to discretion of higher authorities.</li>
							</s:if>
							<%-- <s:if test="%{quote.loanQuoteLoanCategoryId==5 && quote.loanQuoteLoanCategoryId!=3}">
								<li><strong>Note:</strong> The minimum loan amount for availing a Home Loan as 'Overdraft' is Rs. 20 lakhs.</li>
							</s:if>
							<s:else>
								<s:if test="%{quote.loanQuoteLoanCategoryId==3}">
									<li><strong>Note:</strong> This loan offer is for purchase of Plot for construction of House within 5years from date of first disbursement failing which, the Bank will charge a higher rate of interest (1 Year MCLR + 2.60%).</li><br>
								</s:if>
								<s:else>
									<li><strong>Note:</strong> The 'Overdraft' facility is available for loan amount above Rs.20 lacs and upto Rs. 2 crores</li>
								</s:else>
				        	</s:else> --%>
				        	<s:if test="%{quote.loanQuoteLoanCategoryId==5 && quote.loanQuoteReimburse.equalsIgnoreCase('N')}">
					        		<li><strong>Note:</strong> The minimum loan amount for availing a Home Loan as 'Overdraft' is Rs. 20 lakhs.</li>
					        	</s:if>
					        	<s:elseif test="%{quote.loanQuoteLoanCategoryId==5 && quote.loanQuoteReimburse.equalsIgnoreCase('Y') }">
					        		 <li><strong>Disclaimer:</strong> Quantum of Reimbursement is based on lower of <br>a) The present market value of the property after considering the relevant LTV <br>b) Investment made for the house during the last 12 months only.</li>
					        	</s:elseif>
					        	<s:elseif test="%{quote.loanQuoteReimburse.equalsIgnoreCase('Y') }">
							        <li><strong>Disclaimer:</strong> Quantum of Reimbursement is based on lower of <br>a) The present market value of the property after considering the relevant LTV <br>b) Investment made for the house during the last 12 months only.</li>
					        	</s:elseif>
					        	<s:else>
					        		<s:if test="%{quote.loanQuoteLoanCategoryId==3}">
					        			<li><strong>Note:</strong> This loan offer is for purchase of Plot for construction of House within 5 years from date of first disbursement failing which, the Bank will charge a higher rate of interest (1 Year MCLR + 2.60%).</li><br>
					        		</s:if >

					        		<s:if test="%{appForm.appSchemePmay==null &&  quote.loanQuoteLoanCategoryId!=3}">
					        			<li><strong>Note:</strong> The 'Overdraft' facility is available for loan amount above Rs.20 lakhs and upto Rs. 2 crores</li>
					        		</s:if>
					        		<s:elseif test="%{appForm.appSchemePmay!=null &&  quote.loanQuoteLoanCategoryId!=3 && appForm.appLoanAmount>=20 }">
					        		 
					        		</s:elseif>
					        	</s:else>
			        	</s:if>
			        	<s:elseif test="%{loanScenarioBean.chosenProductId==@com.mintstreet.common.util.Constants@BHL_PRODUCT_ID}">
							 <li><strong>Note:</strong> The loan should be liquidated within 24 months and interest to be serviced at monthly intervals</li>
						 </s:elseif>
						<s:if test="%{quote.loanQuoteLoanPurposeId==1 && (quote.loanQuoteLoanCategoryId==1 || quote.loanQuoteLoanCategoryId==2 || quote.loanQuoteLoanCategoryId==4)}">
				        	<s:if test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME1 && loanScenarioBean.chosenTenure <=  20}">
							 		<li><strong>Note:</strong> You are eligible under PMAY CLSS EWS.</li><br>
							 		<li><strong>Note:</strong> Maximum eligible subsidy under the offer is capped at Rs 2.68 lakhs.</li><br>
							 </s:if>
							 <s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME2 && loanScenarioBean.getChosenTenure()<=20 }">
							 		<li><strong>Note:</strong> You are eligible under PMAY CLSS LIG.</li><br>
							 		<li><strong>Note:</strong> Maximum eligible subsidy under the offer is capped at Rs 2.68 lakhs.</li><br>
							 </s:elseif>
							 <s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME3 && loanScenarioBean.getChosenTenure()<=20}">
							 	<li><strong>Note:</strong> You are eligible under PMAY CLSS MIG-I.</li><br>
							 		<li><strong>Note:</strong> Maximum eligible subsidy under the offer is capped at Rs 2.35 lakhs.</li><br>
							 </s:elseif>
							 <s:elseif test="%{appForm.appSchemePmay==@com.mintstreet.common.util.Constants@PMAY_SCHEME4 && loanScenarioBean.getChosenTenure()<=20}">
							 		<li><strong>Note:</strong> You are eligible under PMAY CLSS MIG-II.</li><br>
							 		<li><strong>Note:</strong> Maximum eligible subsidy under the offer is capped at Rs 2.30 lakhs.</li><br>
						 </s:elseif>
					</s:if> 
					</ul>
				</div>
		  </div>
		</div>
	  </div>
	</div>
	<!-- OFFERS popup -->
</form>
<s:if test="%{appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified.equalsIgnoreCase('N')}">
	<!-- Get a callback popup -->
	<div class="modal fade otp-box" id="productCallBack" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
		<div class="modal-content">
		  <div class="modal-body">
		 <%--  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt=""/></span></button>  --%>
			<div class="verify-otp-left-section">
			<h2 style="text-align:center;"><span>Get a</span> callback</h2>	
			<p>Enter your contact details and we will revert back shortly.</p>
			</div>
			<div class="otp-right-section">
				<form  name="call_back" id="call_back" method="post">
					<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
					<s:hidden id="appApplyingFrom" name="appApplyingFrom" value="%{appForm.appApplyingFrom==2?appForm.appApplyingFrom:1}"/>
					<div id ="callBackInformation">
						<ul class="otp-form">
							<li>
								<label>Full name<b class="req">*</b></label>
								<s:textfield id="name" name="name" cssClass="form-control" maxlength="20" value="" onkeydown="return M.isChars(event);"/>
							</li>
							<li>
								<label>Mobile no.<b class="req">*</b></label>
								<s:textfield id="mobileWantUsToCallYou" name="mobileWantUsToCallYou" cssClass="form-control" maxlength="10"  value="" onkeydown="return M.digit(event);"/>
			    				<span class="privacy-note"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/lock-img.png" /> Your privacy is protected</span>
							</li>
							<li>
								<label>Email<b class="req">*</b></label>
								<s:textfield id="email" name="email" cssClass="form-control" maxlength="40" value="" />
							</li>
							<li class="pp-captcha" id="captchaDiv">
								<label>IDENTIFY YOURSELF<b class="req">*</b>
                                   <span>Copy text into input box</span></label>								
								<img id="Homeinformation" name="Homeinformation" src="Captcha.jpg">
								<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('Homeinformation');">
								<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
								<input type="text" class="form-control captcha-inpt" id="captchaInformation" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
							</li>
							<div id="overlay-row_otp">
								<li>
									<button  class="track-btn conf-track-btn" id="callBankSubmit" type="submit">Submit</button>
								</li>
							</div>
						</ul>
					</div>
					<div id="wantus_row_confirm" >
						<ul class="otp-form">
							<li>
								<label>Enter code sent by SMS<b class="req">*</b></label>
								<input type="text" id="inputOtpWantUs" name="inputOtpWantUs" class="form-control secure-otp" maxlength="6"  onkeydown="return M.digit(event);"/>
								<input type="hidden" id="inputOtpWantUs1" name="inputOtpWantUs" class="form-control secure-otp" maxlength="6"  onkeydown="return M.digit(event);"/>
							</li>
                          	<li class="pp-captcha" id="captchaDiv">
								<label>IDENTIFY YOURSELF<b class="req">*</b>
                                   <span>Copy text into input box</span></label>								
								<img id="HomeLoancapImage" name="HomeLoancapImage" src="Captcha.jpg">
								<a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HomeLoancapImage');">
								<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
								<input type="text" class="form-control captcha-inpt" id="captcha" name="captcha" value="" maxlength="6" placeholder="enter captcha" aria-required="true" aria-invalid="true">																							
							</li>
							<li>
				    			<button  class="track-btn conf-track-btn" id="confirmOtpWantUs" type="submit">Confirm</button>
				    			<button  class="resend-btn" id="resendOtpWantUs" type="submit">Resend</button>
							</li>
						</ul>
	    			</div>
						<li id="errorWantUsToCall" class="error-msg-cbs" style="display:none;"></li>
				</form>
			</div>
		  </div>
		</div>
	  </div>
	</div>
	<!-- Get a callback popup -->
</s:if>
<s:if test="%{isDsrPage=='false'}">
	<script type="text/javascript">
	      jQuery(document).ready(function() {
		      	jQuery( "#slider-elig" ).slider({
		              range: 'min',
		              animate: true,
		              value: <s:property value="getText('{0,number,#0.00}',{loanScenarioBean.chosenEligibility})"/>,
		              min: <s:property value="%{loanScenarioBean.minEligibility}"/>,
		              max: <s:property value="%{loanScenarioBean.maxEligibility}"/>,
		              step: 0.10,
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
		              slide: function(event, ui) { jQuery( "#slider-tenure-amount" ).html( ui.value+ " <span>Years</span>" );jQuery( "#chosenTenure" ).val( ui.value );},
		              stop: function(event, ui) {
		            	  changeQutotes();  
					 }
		          });
	            $( "#slider-tenure-amount" ).html($( "#slider-tenure" ).slider( "value" ) + " <span>Years</span>" );
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
<s:set var="minFolderPath" value="%{''}" />
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}" />
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.homeloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
	<%-- <script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081707&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script> --%>
	<s:if test="%{isDsrPage=='false'}">
		<!-- Facebook Pixel Code -->
		<script>
			fbq('track', 'HomeLoanQuote');
		</script>
	</s:if>
	<noscript>
		<img height="1" width="1" style="display:none" src="https://www.facebook.com/tr?id=306199269752742&ev=PageView&noscript=1"/>
	</noscript>
	<!-- DO NOT MODIFY -->
	<!-- End Facebook Pixel Code -->
</s:if>
