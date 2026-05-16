<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--left Section-->
<div class="left-panel">
	<div class="left-back-img">
		<img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/h-bg.jpg" alt="" />
	</div>
	<div class="left-feature-panel">
		<h2>
			<span class="home-icon"></span>
			<span><i>SBI</i><br>
			<span class="underline">HOME</span> LOAN</span>
		</h2>
		<s:if test="%{homeLoanPage == 1}">
			<div id="" class="loan-form-feature" style="height:150px; float:left; margin-bottom:50px;">
				<div class="viewport">
					<div class="pro-feature-box holder">  
						<h2>Unique Advantages</h2>  
							<ul class="feature-list" id="ticker01" >  
<!-- 								<li>Can be availed for any personal purpose.</li>   -->
<!-- 								<li>At rates much lower than usual personal loans.</li>   -->
<!-- 								<li>Home top up Loan also available as an Overdraft. Optimally utilize your surplus funds.</li> -->
								<li>Home Loan & Home top up Loan also available as an Overdraft. Optimally utilize your surplus funds.</li>  
								<li>Low processing charges. No hidden costs or administrative charges.</li>  
								<li>No prepayment penalties. Reduce your interest burden by prepaying the loan.</li>  
							</ul>
						  
					</div>
				</div>
			</div>
			<div id="" class="loan-form-feature" style="height:150px; float:left;margin-top:40px;">
				<div class="viewport">
					<div class="pro-feature-box holder">  
						<h2>Latest Offers</h2>  
							<ul class="feature-list" id="ticker02">  
						<!-- 	<li>No processing fee on Takeover of Home Loan</li>   -->
						<!--	<li>Upgrade to a new home with SBI Bridge Home Loan</li>  -->
                        <!-- 	<li>Check you Insta Home Top Up Loan eligibility through Internet Banking</li> -->
								<li>Check you Insta Home Top Up Loan eligibility through YONO App</li>
								<li>SBI launched Home Loans linked to Repo Linked Lending Rate.</li>  
						<!--	<li>Avail up to Rs. 2.67 lakhs interest subsidy under PMAY-CLSS. For more information <a target="_blank" class="marquee-link" href="https://nbh.org.in">Click Here</a></li> --> 
							</ul>
					</div>  
				</div>
			</div>
		</s:if>
		<s:elseif test="%{homeLoanPage == 2}">
			<div id="content-left" class="loan-form-feature">
				<h2>Your loan details</h2>
				<ul class="loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{loanScenarioBean.loanPurpose}" /></p>
							<span>Loan purpose</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>2</span></div>
						<div class="loan-detail-div">
							<s:if test="%{loanScenarioBean.projectCost>0}">		
								<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{loanScenarioBean.projectCost})}" /></span></p>
							</s:if>
							<s:else>
								<p>Not applicable</p>
							</s:else>
							<span>Project cost</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>3</span></div>
						<div class="loan-detail-div">
							<s:if test="%{quote.loanQuoteEmploymentTypeId==2 || quote.loanQuoteEmploymentTypeId==3 }">
									<p><span class="font-rupee">
									</span><s:property value="getText('{0,number,#,###}',
									{quote.loanQuoteNetAnnualIncomeOfApplicant})"/></p>
									<span>Net annual income</span>	
								</s:if> 
								<s:else>
									<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{loanScenarioBean.netAnnualIncome})}" /></span></p>
									<span>Net annual income</span>
								</s:else> 
						</div>
					</li>
				</ul>
			</div>
		</s:elseif>
		<s:elseif test="%{homeLoanPage == 3 || homeLoanPage == 4}">
			<div id="content-left" class="loan-form-feature">
				<h3>Your loan details</h3>
				<s:if test="%{appForm.appHomeLoanId==12}">
					<ul class="loan-detail bhl-loan-detail">
					<li>
						<div class="loan-num-div"><span>1</span></div>
						<div class="loan-detail-div">
							<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{appForm.appLoanAmount*100000})}" /></span></p>
							<span>Loan amount</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>2</span></div>
						<div class="loan-detail-div">
							<p><s:property value="%{appForm.appLoanTenure}"/> Years</p>
							<span>Loan tenure</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>3</span></div>
						<div class="loan-detail-div">
							<p>
						 		<em class="inter-sec-blk">1 - 12 months : <s:property value="getText('{0,number,##.00}',{appForm.appBhlInterestRate})"/>%</em>
						 		<s:if test="%{(appForm.appGender!=null && appForm.appGender.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_1!=null && appForm.appCoapplicantGender_1.equalsIgnoreCase('F')) || (appForm.appCoapplicantGender_2!=null && appForm.appCoapplicantGender_2.equalsIgnoreCase('F'))}">
								<em class="inter-sec-blk">	13 - 24 months : <s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRateDiscount})"/>%</em>
								</s:if>
							 	<s:else>
								<em class="inter-sec-blk">13 - 24 months : <s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRate})"/>%</em>
								</s:else>
							</p>
								<span>Interest rate</span>
						</div>
					</li>
					<li>
						<div class="loan-num-div"><span>4</span></div>
						<div class="loan-detail-div">
							<p>
								<em class="emi-sec-blk">
									1 - 12 months : <span class="font-rupee">`</span> <s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appBhlFirstInterestEmi)})}"/>
									</em>
							</p>
							<p>
								<em class="emi-sec-blk">
									13 - 24 months : <span class="font-rupee">`</span> <s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})}"/>
								</em>
							</p>
							<span>EMI</span>
						</div>
					</li>
					<li>	
						<div class="loan-num-div"><span>5</span></div>
						<div class="loan-detail-div">
							<p>
							<span class="font-rupee">`</span>
								<s:if test="%{appForm.appLoanProcessingFeeDiscount!=null && appForm.appLoanProcessingFeeDiscount>=0}">
						 			<s:if test="%{appForm.appLoanProcessingFeeDiscount==0}">
						 				0
						 			</s:if>
						 			<s:else>
						 				<span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFeeDiscount)})}"/></span>
						 			</s:else>
					 			</s:if>
						 		<s:else>
						 			<span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFee)})}"/></span>
						 		</s:else>
						 		</p>
							<span>Processing fee</span>
						</div>
					</li>
					<s:if test="%{homeLoanPage == 4}">
						<li>
							<div class="loan-num-div"><span>6</span></div>
							<div class="loan-detail-div">
								<p>
									<a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/documents/25328/29531/1377606055343_HOME_LOAN_MITC.pdf/6d6e7d93-23c6-4e7a-9b0b-dffe2a95292f">Terms &amp; conditions</a>
								</p>
								<p>
									<a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/documents">Doc check list</a>
								</p>
								<span>Downloads</span>
							</div>
						</li>
					</s:if>
				</ul>
				</s:if>
				<s:else>
					<ul class="loan-detail">
						<li>
							<div class="loan-num-div"><span>1</span></div>
							<div class="loan-detail-div">
								<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{appForm.appLoanAmount*100000})}" /></span></p>
								<span>Loan amount</span>
							</div>
						</li>
						<li>
							<div class="loan-num-div"><span>2</span></div>
							<div class="loan-detail-div">
								<p><s:property value="%{appForm.appLoanTenure}"/> Years</p>
								<span>Loan tenure</span>
							</div>
						</li>
						<li>
							<div class="loan-num-div"><span>3</span></div>
							<div class="loan-detail-div">
								<p>
									<s:if test="%{appForm.appLoanInterestRateDiscount!=null && appForm.appLoanInterestRateDiscount>0}">
							 			<s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRateDiscount})"/>
							 		</s:if>
							 		<s:else>
							 			<s:property value="getText('{0,number,##.00}',{appForm.appLoanInterestRate})"/>
							 		</s:else>
								%</p>
								<span>Interest rate</span>
							</div>
						</li>
						<li>
							<div class="loan-num-div"><span>4</span></div>
							<div class="loan-detail-div">
								<s:if test="%{appForm.appLoanEmiDiscount!=null && appForm.appLoanEmiDiscount>0}">
						 			<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmiDiscount)})}"/></span></p>
						 		</s:if>
						 		<s:else>
									<s:if test="%{appForm.appHomeLoanId==@com.mintstreet.common.util.Constants@FLEXI_PAY_PRODUCT_ID}">
										<p>Only Interest <span>(<s:property value="%{tenure1Duration}"/> mth )</span> </p>
										<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure2Emi})"/></span> <span>(<s:property value="%{tenure2Duration}"/> mth)</span></p>
										<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure3Emi})"/></span> <span>(<s:property value="%{tenure3Duration}"/> mth)</span></p>
										<p><span class="font-rupee">`</span><span class="Rs"><s:property value="getText('{0,number,#,###}',{tenure4Emi})"/></span> <span>(<s:property value="%{tenure4Duration}"/> mth)</span></p>
									</s:if>
									<s:else>
										<p><span class="font-rupee">`</span><span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanEmi)})}"/></span></p>
									</s:else>
						 		</s:else>
								<span>EMI</span>
							</div>
						</li>
						<li>	
							<div class="loan-num-div"><span>5</span></div>
							<div class="loan-detail-div">
								<p>
								<span class="font-rupee">`</span>
									<s:if test="%{appForm.appLoanProcessingFeeDiscount!=null && appForm.appLoanProcessingFeeDiscount>=0}">
							 			<s:if test="%{appForm.appLoanProcessingFeeDiscount==0}">
							 				0
							 			</s:if>
							 			<s:else>
							 				<span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFeeDiscount)})}"/></span>
							 			</s:else>
						 			</s:if>
							 		<s:else>
							 			<span class="Rs"><s:property value="%{getText('{0,number,##,##,###}',{@java.lang.Math@round(appForm.appLoanProcessingFee)})}"/></span>
							 		</s:else>
							 		</p>
								<span>Processing fee</span>
							</div>
						</li>
						<s:if test="%{homeLoanPage == 4}">
							<li>
								<div class="loan-num-div"><span>6</span></div>
								<div class="loan-detail-div">
									<p>
										<a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/documents/25328/29531/1377606055343_HOME_LOAN_MITC.pdf/6d6e7d93-23c6-4e7a-9b0b-dffe2a95292f">Terms &amp; conditions</a>
									</p>
									<p>
										<a class="req-doc-text" target="_blank" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FULL_URL}"/>/portal/web/personal-banking/documents">Doc check list</a>
									</p>
									<span>Downloads</span>
								</div>
							</li>
						</s:if>
					</ul>
				</s:else>
			</div>
		</s:elseif>
	</div>
</div>
<script type="text/javascript">
	jQuery("span.Rs").each(function(){
		var placeholder = jQuery(this).text();
		if(placeholder!=undefined){
			var strip_commas = placeholder.replace(/,/g, "");
			if(strip_commas>0){
				strip_commas = Number(strip_commas).toPrecision();
				strip_commas = strip_commas.replace(".0","");
				jQuery(this).text(M.moneyFormat(strip_commas));
			}
		}
	});
</script>

<script type="text/javascript">
	$(function(){
		$("#ticker01,#ticker02").bootstrapNews({
			newsPerPage:4,
			autoplay:true,
			pauseOnHover:true,
			navigation:false,
			direction:'up',
			newsTickerInterval:2500,
			onToDo:function(){
			}
	
			
		});
		
	});
	
</script>
<!--<script type="text/javascript">
jQuery.fn.liScroll = function(settings) {
	settings = jQuery.extend({
		travelocity: 0.10
		}, settings);		
		return this.each(function(){
				var $strip = jQuery(this);
				$strip.addClass("newsticker")
				var stripHeight = 1;
				$strip.find("li").each(function(i){
					stripHeight += jQuery(this, i).outerHeight(true); // thanks to Michael Haszprunar and Fabien Volpi
				});
				var $mask = $strip.wrap("<div class='mask'></div>");
				var $tickercontainer = $strip.parent().wrap("<div class='tickercontainer'></div>");								
				var containerHeight = $strip.parent().parent().height();	//a.k.a. 'mask' width 	
				$strip.height(stripHeight);			
				var totalTravel = stripHeight;
				var defTiming = totalTravel/settings.travelocity;	// thanks to Scott Waye		
				function scrollnews(spazio, tempo){
				$strip.animate({top: '-='+ spazio}, tempo, "linear", function(){$strip.css("top", containerHeight); scrollnews(totalTravel, defTiming);});
				}
				scrollnews(totalTravel, defTiming);				
				$strip.hover(function(){
				jQuery(this).stop();
				},
				function(){
				var offset = jQuery(this).offset();
				var residualSpace = offset.top + stripHeight;
				var residualTime = residualSpace/settings.travelocity;
				scrollnews(residualSpace, residualTime);
				});			
		});	
};

$(function(){
	$("ul#ticker01,ul#ticker02").liScroll();
});

</script>-->


<!--left Section End-->
