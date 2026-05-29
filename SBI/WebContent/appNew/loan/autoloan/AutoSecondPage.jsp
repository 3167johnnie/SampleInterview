<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.ui.slider.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<div class="top-bar-bg-2"></div>
<s:if test="%{loanScenarioBean.showCoApplicant == 1}">
	<div id="AddCoapplicant">
		<s:include value="/appNew/loan/autoloan/AutoCoapplicantQuote.jsp"></s:include>
	</div>
</s:if>
<form name="capturedValue" id="capturedValue" method="post" enctype="application/x-www-form-urlencoded" action="javascript:void(0);">
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
							<s:property value="%{loanScenarioBean.minTenure}"  /> <s:property value="%{loanScenarioBean.minTenure>1?'months':'month'}"/>
						</s:if>
						<s:else>
							<s:property value="%{loanScenarioBean.minTenure}"  /> <s:property value="%{loanScenarioBean.minTenure>1?'years':'year'}"/>
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
		<s:hidden name="chosenTenure" id="chosenTenure" value="%{loanScenarioBean.chosenTenure}"/>
		<s:hidden name="chosenEligibility" id="chosenEligibility" value="%{loanScenarioBean.chosenEligibility}" />
		<s:hidden name="chosenLoanAccountType" id="chosenLoanAccountType" value="%{loanScenarioBean.chosenLoanAccountType}" />
		<s:hidden name="chosenProductId" id="chosenProductId" value="%{loanScenarioBean.chosenProductId}" />
		<s:hidden name="productSliderTenure" id="productSliderTenure" value="%{loanScenarioBean.productSliderTenure}" />
		<s:hidden name="productSaveEmail" id="productSaveEmail" value=""/>
		
		<div class="text-center loan-quote-loader" id="loan-quote-loader" style="display:none">
			<div class="loan-quote-loader-img <s:property value="%{loanScenarioBean.showCoApplicant == 1?'co-app-loader':''}"/> "><img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader-horizontal.gif" width="35"/> </div> 
		</div>
	</div>
	<div class="clearfix"></div>
	<!--loan offers-->
	<div class="loan-offers-container">
		<h3 class="flt">YOUR LOAN QUOTES</h3>
		 <s:if test="%{loanScenarioBean.showOffer==1}">
			<div class="offer-btn">
			 	<a class="product-offers" href="#" data-target="#productoffer" data-toggle="modal" >Click to view Offers</a>
			 </div>
		 </s:if>
		<s:iterator value="loanScenarioBean.loanQuotes" status="loanQuoteIndex" var="loanQuotesOuter">
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
					<div id="<s:property value="%{#commonQuote.productTypeId}"/>-<s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" style="display:<s:property value="%{#isEligibleToShow==1?'block':'none'}"/>;" class="loan-offer-div">
						<h2><s:property value="%{#commonQuote.productTypeName}"/> <span class="p-content">(Product type)</span></h2>
						<ul>
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
											<span class="p-content">(for <s:property value="%{loanScenarioBean.chosenTenure}"/> 
												<s:if test="%{loanScenarioBean.productSliderTenure ==1}">
													<s:property value="%{loanScenarioBean.chosenTenure>1?'months':'month'}"/>
												</s:if>
												<s:else>
													<s:property value="%{loanScenarioBean.chosenTenure>1?'years':'year'}"/>
												</s:else>
												)
											</span>
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
										<span class="p-content quote-heading">Best Rate</span>
										<span class="p-content">(<s:property value="%{#commonQuote.rateType==1?'Floating':'Fixed'}"/>)</span>
									</div>
								 </div>
							</li>
							<s:if test="%{#commonQuote.discountedEmi!=null && #commonQuote.discountedEmi>0}">
								<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
									<li class="flxi-emis">
										<div class="box">
											<div class="box-inner">
												<span class="emi-sec"> Only interest<span class="p-content"> (<s:property value="%{tenure1Duration}"/> mth)</span></span>
												<div class="clearfix"></div>
												<span class="emi-sec"> <span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/>:<span class="p-content"> (<s:property value="%{tenure2Duration}"/> mth)</span></span>
												<div class="clearfix"></div>
												<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/>:<span class="p-content"> (<s:property value="%{tenure3Duration}"/> mth)</span> </span>
												<div class="clearfix"></div>
												<span class="emi-sec"><span class="font-rupee">`</span> <s:property value="getText('{0,number,#,###}',{tenure4Emi})"/>:<span class="p-content"> (<s:property value="%{tenure4Duration}"/> mth)</span></span>
												<span class="p-content quote-heading">EMI</span>
												<span class="p-content">(Monthly Installment)</span>
											</div>
										 </div>
									</li>
								</s:if>
								<s:else>
									<li>
										<div class="box">
											<div class="box-inner">
													<s:if test="%{#commonQuote.discountedInterestRate!=null && #commonQuote.discountedInterestRate>0}">
													<span class="line-through"><span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span>
													<span class=""><span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedEmi)})"/></span>
												</s:if>
												<s:else>
													<span class="line-through"><span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/></span>
												</s:else>
												<span class="p-content quote-heading">EMI</span>
												<span class="p-content">(Monthly Installment)</span>
											</div>
										 </div>
									</li>
								</s:else>
							</s:if>
							<s:else>
								<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
									<s:if test="%{#commonQuote.productTypeId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
										<li class="flxi-emis">
											<div class="box">
												<div class="box-inner">
														<span class="emi-sec">Only interest<span class="p-content"> (<s:property value="%{tenure1Duration}"/> mth)</span> </span>
														<div class="clearfix"></div>
														<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/><span class="p-content"> (<s:property value="%{tenure2Duration}"/> mth)</span>  </span>
														<div class="clearfix"></div>
														<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/><span class="p-content"> (<s:property value="%{tenure3Duration}"/> mth)</span></span>
														<div class="clearfix"></div>
														<span class="emi-sec"><span class="font-rupee">`</span><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/><span class="p-content"> (<s:property value="%{tenure4Duration}"/> mth)</span></span>
														<span class="p-content quote-heading">EMI</span>
														<span class="p-content">(Monthly Installment)</span>
												</div>
											 </div>
										</li>
									</s:if>
									<s:else>
										<li>
											<div class="box">
												<div class="box-inner">
														<span class="font-rupee">`</span>
														<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/>
														<span class="p-content quote-heading">EMI</span>
														<span class="p-content">(Monthly Installment)</span>
												</div>
											 </div>
										</li>
									</s:else>
								</s:if>
								<s:else>
									<li>
										<div class="box">
											<div class="box-inner">
													<span class="font-rupee">`</span>
													<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.emi)})"/>
												<!--<span class="p-content">(EMI)</span>-->
											</div>
										 </div>
									</li>
								</s:else>
							</s:else>
							<li>
								<div class="box">
									<div class="box-inner">
										<span class="font-rupee">`</span>
										<s:if test="%{#commonQuote.discountedProcessingFee!=null && #commonQuote.discountedProcessingFee>=0 && #commonQuote.processingFee !=  #commonQuote.discountedProcessingFee}">
											<s:if test="%{#commonQuote.discountedProcessingFee==0}">
												<span class="line-through"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
												<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
											</s:if>
											<s:else>
												<span class="line-through"><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/></span>
												<span class=""><s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.discountedProcessingFee)})"/></span>
											</s:else>
										</s:if>
										<s:else>
											<s:if test="%{#commonQuote.processingFee!=null && #commonQuote.processingFee==0}">
												<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
											</s:if>
											<s:else>
												<s:property value="getText('{0,number,#,###}',{@java.lang.Math@round(#commonQuote.processingFee)})"/>
											</s:else>
										</s:else>
										<span class="p-content quote-heading">Processing Fee</span>
										<span class="p-content">(Including Service Tax)</span>
									</div>
								</div>
							</li>
							<li>
								<div class="box">
										<div class="box-inner">
											<div class="col-lg-12 txt-left" style="padding:0 0px">
												<div class="blue-radio blue-radio-danger">
													<input class='<s:property value="%{#commonQuote.productTypeId<7?'loanAccountType':''}"/>' <s:property value="%{#isEligibleToShow==1?'checked=\"checked\"':''}"/>
													value="1" id="accountTypeTerm_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
													name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" type="radio" name="btn"/>
											   <label for="accountTypeTerm_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Term Loan </label>
												</div>
											</div>
											<%-- <s:if test="%{#commonQuote.productTypeId<7}">
												<s:if test="%{loanScenarioBean.showFirstTermOverDraftCheck==1}">
													<div class="clearfix"></div>	
													<div class="col-lg-12 txt-left" style="padding:0 0px">
														<div class="blue-radio blue-radio-danger">
															<input class="loanAccountType" <s:property value="%{#isEligibleToShow==0?'checked=\"checked\"':''}"/>
																  value="2" id="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
																  name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" type="radio"/>
														    <label for="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Overdraft </label>
														</div>
											  	 		<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the Overdraft account (with an option to withdraw whenever required)</span></a>  
													</div>
												</s:if>
											</s:if>
											<s:elseif test="%{#commonQuote.productTypeId==7}">
												<div class="clearfix"></div>	
												<div class="col-lg-12 txt-left" style="padding:0 0px">
													<div class="blue-radio blue-radio-danger">
														<input class="" <s:property value="%{#isEligibleToShow==0?'checked=\"checked\"':''}"/>
															 value="2" id="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
															 name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
															 type="radio"/>
															 <label for="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Overdraft </label>
													</div>
										  	 		<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the Overdraft account (with an option to withdraw whenever required)</span></a>  
												</div>
											</s:elseif> --%>
											<%-- <s:elseif test="%{#commonQuote.productTypeId>7}">
												<s:if test="%{loanScenarioBean.showSecondTermOverDraftCheck==1}">
													<div class="clearfix"></div>
													<div class="col-lg-12 txt-left" style="padding:0 0px">	
														<div class="blue-radio blue-radio-danger">
															<input class="" <s:property value="%{#isEligibleToShow==0?'checked=\"checked\"':''}"/>
															  value="2" id="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
															  name ="accountType_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
															  type="radio"/>
															  <label for="accountTypeOverdraft_<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"> Overdraft </label>
												  	 	</div>
													  	<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>Enables customers to park their surplus funds/savings in the Overdraft account (with an option to withdraw whenever required)</span></a>  
													</div>
												</s:if>
											</s:elseif> --%>
											<span class="p-content quote-heading">A/c Type</span>
										</div>
									</div>
							</li>
							<li class="last-child">
								<span id="loader1" class="loader-small" style="display: none;"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif" name="loader" /></span>
								<input id="applyLoanOffer<s:property value="%{#commonQuote.productTypeId}"/><s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>" 
									onclick="applyLoanOffer(this, '<s:property value="%{#commonQuote.applyQuoteUrl}"/>');" 
									name="send-btn<s:property value="%{#commonQuote.productTypeId}"/> <s:property value="%{#assciateQuoteQuoteIndex.index+1}"/>"
									type="submit" class="apply-btn" value="Apply Now">
								    <a href="javascript:void(0);" onclick="javaScript:window.open('<s:property value="%{#commonQuote.repaymentScheduleUrl}"/>','','navigationtoolbar=no, scrollbars=yes,menubar=no,height=800,width=1000,resizable=yes,toolbar=no,location=no,status=no') " 
									class="emi-schedule-link">EMI schedule</a>&nbsp;
							</li>
						</ul>
					</div>
				</s:if>
			</s:iterator>
		</s:iterator>
	</div>
	
	<s:if test="%{loanScenarioBean.showOfferDiscount==1}">
		<div class="savings">
			<span>
				<s:if test="%{#commonQuote.isDiscountApplied==1}">
					<em class="wr">Rs&nbsp;</em> <s:property value="getText('{0,number,#,###}',{#commonQuote.totalCost - #commonQuote.discountedTotalCost})"/>
				</s:if>
				<s:else>
					--
				</s:else>
			</span>
		</div>
	</s:if>
	<!--discount email-->
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
				<s:if test="%{appForm.appEmailAttemptCount!=null && appForm.appEmailAttemptCount>4}">
					<s:set var="needToDisabledToEmail" value="%{'true'}"/>
				</s:if>
				<s:textfield id="emailSendQuote" name="email" maxlength="60" 
					value="%{appForm.appWorkEmail}" cssClass="form-control input-text" 
					placeholder="Enter your Email" autocomplete="off" disabled="%{needToDisabledToEmail}"/>
				<div class="email-heading">In case you find any difficulty in completing the loan application, a link has been sent to this email id to help you resume the quote.</div>
				<input type="submit" value="Send Quote" name="send-btn" class="send-btn" id="sendQuote" >
				<s:property value="%{needToDisabledToEmail=='true'?'disabled send-btn-failure':''}"/>
				<div class="clearfix"></div>
				<div id="loaderEmail"></div>
			</li>
		</ul>
	</div>
	<!-- OFFERS popup -->
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
							 <li><b>Yuva Loan:</b> Pay only the interest applied on home loan during the first 36 months.Regular EMIs start after completion of 36 months.</li>
						</ul>
					</div>
				</div>
			</div>
		</div>	
	</div>
</form>
<s:include value="/appNew/common/CommonOTPOther.jsp"></s:include>

<s:if test="%{isDsrPage=='false'}">
	<script type="text/javascript">
	      jQuery(document).ready(function(){
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
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/autoloan/js<s:property value="#minFolderPath"/>/jquery.autoloan_quote.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
	<%-- <script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081708&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script> --%>
	<s:if test="%{isDsrPage=='false'}">
		<!-- Facebook Pixel Code -->
			<script>
				fbq('trackCustom', 'AutoLoanQuote');
			</script>
			<!-- DO NOT MODIFY -->
			<!-- End Facebook Pixel Code -->
	</s:if>
</s:if>
