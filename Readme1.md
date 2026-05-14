-----------------------------------------------------------------------
/SBI/WebContent/appNew/loan/homeloan/HomeFirstPageSession.jsp

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="selectedLangue" value="%{#session.selectedLanguage}" />
<div class="clearfix"></div>
	<div class="clearfix"></div>
	</br>
	<h3>PROPERTY DETAILS</h3>
	<ul class="form-section">
		<s:set var="needToDisable" value="false"/>
		<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
			<s:if test="%{quote.loanQuoteLoanPurposeId==5}">
				<s:set var="needToDisable" value="true"/>
			</s:if>
		</s:if>
		<li id="LILOANPURPOSE">
			<label>Purpose of loan<b class="req">*</b></label>
			<div class="flat-field">
				<s:select list="%{beanList.loanPurposes==null?'':beanList.loanPurposes}" value="%{quote.loanQuoteLoanPurposeId}" 
			 		id="loanPurpose" name="quote.loanQuoteLoanPurposeId" headerKey="0" headerValue="Select loan purpose"
			 		cssClass="form-select %{(#needToDisable==true && isDsrPage=='true')?'disabledFields':''} customSelector"
			 		disabled="%{(#needToDisable==true && isDsrPage=='true')?'true':'false'}" onfocus="customOnFocus(this);"/>
			</div>
			<s:if test="%{isDsrPage=='false'}">
				<span id="plmsgId">
					<s:if test="%{quote.loanQuoteLoanPurposeId>0}">
						<a href='https://<s:property value="%{loanPurposeUrl}"/>' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>
					</s:if>
				</span>
			</s:if>
		</li>
		<s:if test="%{quote.loanQuoteLoanPurposeId==1 || quote.loanQuoteLoanPurposeId==3}">
			<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId.jsp"></s:include>
		</s:if>
		<div id="loanCategoryDetails">
			<s:if test="%{quote.loanQuoteLoanPurposeId==1}">
				<s:if test="%{quote.loanQuoteLoanCategoryId==1}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId1.jsp"></s:include>
				</s:if>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==2}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId2.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==3}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId3.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==4}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId4.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==5}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId5.jsp"></s:include>
				</s:elseif>
			</s:if>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==2}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId2.jsp"></s:include>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==3}">
				<s:if test="%{quote.loanQuoteLoanCategoryId==6}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId6.jsp"></s:include>
				</s:if>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==5}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId5.jsp"></s:include>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==25}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId25.jsp"></s:include>
			</s:elseif>
		</div>
	</ul>
	
	<div class="clearfix"></div>
	</br>
	<h3>APPLICANT DETAILS</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/homeloan/HomeLoanApplicatnsDetail.jsp"></s:include>
	
		</ul>
	
	<ul class="form-section">
	</br>
	<s:include value="/appNew/loan/homeloan/PersonalIdentityDetails.jsp"></s:include>
	</ul>
<div class="clearfix"></div>   
</br>
<h3>INCOME DETAILS</h3>
<ul class="form-section">
	<s:include value="/appNew/loan/homeloan/HomeLoanIncomeDetails.jsp"></s:include>
</ul>
 <div class="clearfix"></div>
 </br>
<h3>CONTACT DETAILS</h3>
<ul class="form-section">
<s:include value="/appNew/loan/homeloan/ContactsDetails.jsp"></s:include>
</ul>

<div class="clearfix"></div>
      <div class="co-head flt">
				<h2>Click to add a co-applicant</h2>
	  </div>

<div class="co-app-btn flr">
<p>
		<a class="app-btn 11 collapsed" data-bs-toggle="collapse"
			href="#collapseExample" id="coAppImageDetails"
			data-bs-parent="#collapseOne" role="button" aria-expanded="false"
			aria-controls="collapseOne" onclick="showCrossBtn1();"> Co applicant details + </a>

</p>
</div>

<div class="collapse" id="collapseExample">
    <div class="card-header" id="headingOne">
      <h2 class="mb-0">
        <button class=" btn btn-link btn-block text-left collapsed"  type="button"  data-bs-toggle="collapse" href="#collapseExample"  id="coAppImageDetails" data-bs-parent="#collapseOne"  aria-expanded="false" aria-controls="collapseOne"
         onclick="showCrossBtn1();"> Co-applicant details 1  
          <span style="color:#76ABDF;font-size:11pt; float:right; display: none;width: 30px; " display="none" id="cross_btn2" > x  </span>
           <span style="color:#76ABDF;font-size:11pt; float:right; display: block;width: 30px;" id="cross_btn3" > +  </span>
    
      </h2>
    </div>
		<div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
	      <div class="card-body">
	        <s:include value="/appNew/loan/homeloan/HomeLoanCoApplicant1.jsp"></s:include>
	      </div>
	   </div>
	
	    <div class="card-header" id="headingTwo">
	      <h2 class="mb-0">
	        <button class="btn btn-link btn-block text-left collapsed in" id="coll_btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo" onclick="showCrossBtn();">
	          Co-applicant details 2
	           <span style="color:#76ABDF;font-size:11pt; float:right; display: none;width: 30px;" display="none" id="cross_btn" > x   </span>
	           <span style="color:#76ABDF;font-size:11pt; float:right; display: block;width: 30px;" id="cross_btn1" > +   </span>
	        </button>
	      </h2>
	    </div>
	    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
	      <div class="card-body">
	         <s:include value="/appNew/loan/homeloan/HomeLoanCoApplicant2.jsp"></s:include>
	      </div>
	   </div>   
</div>
<div id="aab" style="display:none;">
	<br>
	<br>
</div>
<div class="clearfix"></div>
	<h3>PREFERRED LOCATION</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/homeloan/HomeLoanPreferedLocation.jsp"></s:include>
		<div id="PREFERREDLOCATIONSTATE">
			<s:if test="%{quote.loanQuotePreferredLocationOfAvailingLoan==1 || quote.loanQuotePreferredLocationOfAvailingLoan==2}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredState.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONCITY">
			<s:if test="%{quote!=null && quote.loanQuotePreferredStateId!=null && quote.loanQuotePreferredStateId>0}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredCity.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONDISTRICT">
			<s:if test="%{quote!=null && quote.loanQuotePreferredCityId==9999999}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredDistrict.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONBRANCH">
			<s:if test="%{quote!=null && quote.loanQuotePreferredCityId!=null && quote.loanQuotePreferredCityId>0}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredBranch.jsp"></s:include>
			</s:if>
		</div>
	</ul>
	<s:if test="%{appSeqId==null}">
	<div class="clearfix"></div>
	</br>
	<h3>IDENTIFY YOURSELF</h3>
	<ul class="form-section captcha">
			<li>
			 <label>Copy text into input box<b class="req">*</b></label>
				<img id="HOMELoanFcapImage" name="HOMELoanFcapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HOMELoanFcapImage');">
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
				<input type="text" class="form-control" id="captcha" name="captcha" value="" maxlength="6" placeholder="captcha" autocomplete="off"/>
			</li>
		</ul>
	</s:if>
	<div class="clearfix"></div>
	<div id="termsAndConditionFirst" class="sbi-trms-div">
		<ul class="form-section">
			<li class="full-width">
				<div class="trms-section">
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">
					<label for="infoprovide" class="label-content" style="font-size:14px; ">
						
						I/We certify that the information and particulars provided by me/us in this application form (and all documents referred or provided herewith) are true, correct, 
						complete and up to date in all respects and I have not withheld any information. I/ We authorize State Bank of India to make inquiries related to or verify said 
						information directly or through any third party. 
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> Read more </a></b><b class="req">*</b>
					</label>
				</div>
				<div> </div>
				<div> </div>
				<div class="qt-btn-div flr mrgt-10 m-100 call-us">
					<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
					<span class=callUS>Call us at 1800 425 3800</span>		
				</div>
			 </li>
		 </ul>
	</div>
 <script type="text/javascript">
		function showCrossBtn() {
			  var cross = document.getElementById("cross_btn");
			  var plus = document.getElementById("cross_btn1");
			  if (plus.style.display == "block") {
				  cross.style.display = "block";
				  plus.style.display = "none";
				  const myDiv = document.getElementById('collapseTwo');
					// Hide the div
					myDiv.style.display = 'block';
					$("#COAPPLICANT2").show();
			  } else if (cross.style.display == "block"){
				  plus.style.display = "block";
				  cross.style.display = "none";
				  const myDiv = document.getElementById('collapseTwo');
				// Hide the div
				myDiv.style.display = 'none';
			  }
			}
		
		function showCrossBtn1() {
			  var cross = document.getElementById("cross_btn2");
			  var plus = document.getElementById("cross_btn3");
			  if (plus.style.display == "block") {
				  cross.style.display = "block";
				  plus.style.display = "none";
				  const myDiv = document.getElementById('collapseOne');
					// Hide the div
					myDiv.style.display = 'block';
					$("#headingOne").show();
					$("#headingTwo").show();
					$("#aab").hide();
			  } else if (cross.style.display == "block") {
				  plus.style.display = "block";
				  cross.style.display = "none";
				  const myDiv = document.getElementById('collapseOne');

				$("#headingOne").hide();
				$("#headingTwo").hide();
				$("#collapseOne").hide();
				$("#COAPPLICANT2").hide();
				$("#aab").show();
			  } 
		}
</script>  



-----------------------------------------------------------------------
workspace\ocas_repo_java\SBI\WebContent\appNew\common\HeaderTabs.jsp

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="prd-tab">
	<div class="container">
		<div class="row">
			<ul>
				<li id="ul_li_2" class="menu-li" rel="ddn-nav-2">
					<a href="javascript:;">
						<h2><span class="hl-icon"></span>Home Loan</h2>
						<p>One stop solution for all your Housing needs.</p>
						<span>&nbsp;</span>
					</a>
					<div class="ddn-nav-hover ddn-nav-hover-1">
						<ul>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>">Home Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>">Home Top Up Loan</a></li>
						</ul>
					</div>
				</li>
				<li id="ul_li_1" class="menu-li" rel="ddn-nav">
					<a href="javascript:;" >
					<h2><span class="rl-icon"></span>Other Retail Loan</h2>
					<p>Apply for Auto Loan, Personal Loan,Gold Loan, Pension Loan, Scholar Loan and Global ED- Vantage, EL Takeover</p></a>
					
					<div class="ddn-nav-hover ddn-nav-hover-2">
						<ul>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>">Auto Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>">Personal Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>">Gold Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>">Pension Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>">Scholar Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>">Global ED-Vantage</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>">EL Takeover</a></li>
						</ul>
					</div>
					
				</li>
				<div class="menu-div ddn-nav-2">
					<ul>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>">Home Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>">Home Top Up Loan</a></li>
					</ul>
				</div>
				<div class="menu-div ddn-nav">
					<ul>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>">Auto Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>">Personal Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>">Gold Loan</a></li>	
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>">Pension Loan</a></li>	
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>">Scholar Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>">Global ED-Vantage</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>">EL Takeover</a></li>
					</ul>
				</div>
			<%-- 	<li  id="ul_li_3">
					<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AGRI_LOAN_ACTION}"/>">
						<h2><span class="agl-icon"></span>Agri Loan</h2>
						<p>A help offered by SBI to improve agriculture statistics. </p>
					</a>	
				</li> --%>
				<li  id="ul_li_4">
					<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCC_LOAN_ACTION}"/>">
						<h2><span class="ccr-icon"></span>Credit Card</h2>
						<p>A complete range of Credit Cards that matches your lifestyle. </p>
					</a>	
				</li>
				<li id="ul_li_5" class="menu-li" rel="ddn-nav">
                    <a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@CVE_ACTION}"/>">
						<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/cve.jpg"> <h2>Insurance & Investment</h2>
                            <p>A Complete range of Insurance,Investment and Credit Card that matches your requirement.</p>
                    </a>
                </li>
			</ul>
		</div>
	</div>
</div>



--------------------------------------------------------
/SBI/WebContent/appNew/common/CommonCbs.jsp




<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<% response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
 response.setHeader("Pragma","no-cache"); //HTTP 1.0 
 response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>

<head>
	<meta charset="utf-8">
	<meta http-equiv="Cache-control" content="no-cache">
</head>

<form name="commonCBSForm" id="commonCBSForm" method="post" action="javascript:void(0);"   enctype="application/x-www-form-urlencoded" onsubmit="return getCBSCall();" >
<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
	<div id="content-cbs" class="form-div-cbs" style="display:<s:property value="%{!showCBS?'none;':'block;'}" />;position:relative; top:15px;">
		
		<!-- Added by Pratima for CVE -->
		<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
			<s:include value="/appNew/common/CommonCbsCveNonConsent.jsp"></s:include>
        </s:if>
		<!-- Ended by Pratima for CVE -->

		<!-- Added by Pratima for CVE -->
		<s:if test="personalLoanPage==1 && appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE">				
			<s:include value="/appNew/common/CommonCbsPersonalLoan.jsp"></s:include>
		</s:if>
		<!-- Ended by Pratima for CVE -->
		
		<s:if test="%{!personalLoanPage==1 && appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE}">
			<div id="container">
			
				<h3>RELATIONSHIP WITH BANK</h3>
				<s:if test="%{homeTopUpLoanPage!=1}">
					<s:if test="%{agriLoanPage == 1 && appForm!=null && isDsrPage=='true'}">
					</s:if>
					<s:else>
						<div id="commonCbsDivId" class="form-div-cbs" style="display:<s:property value="%{showCBS?'block;':'none;'}" />">
							<ul class="form-section form-section-option <s:property value="%{showCBS?'show':'hide'}" />">
								<li id="divcbsRelationship" class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
								<div class="row ">
									<!-- <div class="col-xs-12 col-sm-6   mt-6  "> -->
									 <div class="text-end col-sm-6 mrgt-15 cpt">
										<label class="" style="color:#172154; font-size:13px;">Do you have an existing relationship with SBI?<b class="req">*</b></label>
									</div>
									
									
									 <div class="col-sm-6">
										<!-- <div class="col-xs-12 col-sm-6"> -->
											<div class="blue-radio blue-radio-danger m-left-20">
												<input type="radio" name="relationshipWithSBI" id="relationshipWithSBIYes" checked="checked" value="1">
												<label for="relationshipWithSBIYes"> Yes </label>
											</div>	
											<div class="blue-radio blue-radio-danger">
												<input type="radio"  name="relationshipWithSBI" id="relationshipWithSBINo" value="2" >
												<label for="relationshipWithSBINo">No </label>
											</div>
										</div>
									</div>
								</li>
							</ul>
						</div>
					</s:else>
				</s:if>
				<ul class="form-section">
					<div id="relationshipWithSBIBank">
						<li> 
												
							<label >Type of relationship <b class="req">*</b> <a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>If you maintain multiple accounts with us, please select the first option from the dropdown.</span></a></label>
							<s:if test="%{homeTopUpLoanPage==1}">
								<div class="flat-field">
									<s:select list="#{1:'Home loan'}" value="1" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:if>
									<s:elseif test="%{homeLoanPage==1}">
								<div class="flat-field">
									<s:select list="#{1:'Home loan',2:'Salary a/c with SBI',3:'Other deposit or loan account'}" value="" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:elseif>
							<s:elseif test="%{autoLoanPage == 1}">
								<div class="flat-field">
									<s:select list="#{1:'Home loan',2:'Salary a/c with SBI',3:'Pension a/c with SBI',4:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:elseif>
							<s:elseif test="%{educationLoanPage == 1}">
								<div class="flat-field">
									<s:select list="#{1:'Education loan',2:'Other deposit or loan account of student'}" value="%{cbs.cbsTypeOfRelationship}" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:elseif>
							<s:elseif test="%{agriLoanPage == 1}">
								<div class="flat-field">
									<s:select list="#{1:'Savings account', 2:'Kisan credit card', 3:'Tractor loan', 4:'Other deposits'}" value="%{cbs.cbsTypeOfRelationship}" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:elseif>
							<s:elseif test="%{creditCardPage == 1}">
								<div class="flat-field">
									<s:select list="#{1:'Home loan',2:'Salary a/c with SBI',3:'Other deposit or loan account'}" value="%{cbs.cbsTypeofRelationship}" id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select Type of relationship"  cssClass="form-select" onfocus="customOnFocus(this);"/>
								</div>
							</s:elseif>
						</li>
						<li>
						<label>Account number<b class="req">*</b></label>
						<s:textfield name="cbs.cbsAccountNumber" id="accountNumber" value="" maxlength="11" autocomplete="off" placeholder="Account number" cssClass="form-control" onkeydown="return M.digit(event);"/>
							<s:if test="%{educationLoanPage == 1}">
								  <div class="custom-tooltip">Please put SBI account number of student</div>
							</s:if>
							
						</li>
						<li>
							<label>Enter your mobile number with ISD code<span class="req">*</span></label>
							<s:textfield name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" />
							<!-- <input type="number" name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" class="s-input flt form-control" autocomplete="off" maxlength="3" min="90" max="102" placeholder="91" onkeydown="return M.digit(event);" style="-moz-appearance:textfield;"/> -->
							<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber" value="" autocomplete="off" maxlength="10" placeholder="Mobile" 
							cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>
							<s:if test="%{educationLoanPage == 1}">
								<div class="custom-tooltip mobile-custom-tooltip">Please provide mobile number</div>
							</s:if>
							
						</li>
						
						
						
						<!-- terms and conditions start -->
						<div class="clearfix"></div>
						<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display :<s:property value="%{showCBS?'block':'none'}"/>;">
							<li class="full-width">
								<div class="trms-section">
								    <input type="checkbox" class="blue-css-checkbox" id="infoprovideCBS" name="infoprovideCBS" >
										
									<!-- home loan consent -->
									<s:if test="%{homeLoanPage==1}">
										
										<label for="infoprovideCBS" class="label-content" style="font-size:14px; ">

											I/We certify that the information and particulars provided by me/us in this application form (and all documents referred or provided herewith) are true, correct, 
											complete and up to date in all respects and I have not withheld any information. I/ We authorize State Bank of India to make inquiries related to or verify said 
											information directly or through any third party. 
											<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> Read more </a></b><b class="req">*</b>
											
											
										</label>
									</s:if>
									
									<!-- auto loan consent -->
									<s:if test="%{autoLoanPage == 1}">
										<label for="infoprovideCBS" class="label-content consentScrollForAuto scollerClass">
											<s:property escapeHtml="false" value="%{beanList.consentAutoLoanETB}" />
											&nbsp;<b class="req">*</b>
										</label>
									</s:if>
									
									<!-- education loan consent -->
							    <s:if test="%{educationLoanPage == 1}">
						        	<label for="infoprovideCBS" class="label-content">
										<s:property escapeHtml="false" value="%{beanList.consentEducationLoanEtb}" />
										&nbsp;<b class="req">*</b>
									</label>
								</s:if>
								
								<!-- agri loan consent -->
						        <s:if test="%{agriLoanPage == 1}">
						        	<label for="infoprovideCBS" class="label-content">
										<s:property escapeHtml="false" value="%{beanList.consentAgriLoanEtb}" />
										&nbsp;<b class="req">*</b>
									</label>
								</s:if>
								
								<!-- credit card consent -->
								<s:if test="%{creditCardPage == 1}">
									<label for="infoprovideCBS" class="label-content">
										<s:property escapeHtml="false" value="%{beanList.consentCreditCardEtb}" />
										&nbsp;<b class="req">*</b>
									</label>
								</s:if>
								</div>
								<div class="qt-btn-div trms-btn flr mrgt-10">
									<input  type="submit" class="submit-btn" value="Submit" id="submitBtn">
								</div>
							</li>
						</div>
					</div>
				</ul>
			</div>	
		</s:if>
	</div>
</form>


<s:if test="personalLoanPage==1 && appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_CVE">
<div id="htmlCBSPLTypeOfRelationship11" style="display:none;">
	<!-- PERSONAL LOAN -->
	<s:select list="#{1:'Xpress Credit Loan',3:'Salary A/c With SBI',2:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
</div>

<div id="htmlCBSPLTypeOfRelationship12" style="display:none;">
	<!-- LAP -->
	<s:select list="#{3:'Salary A/c With SBI',2:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
</div>
<div id="htmlCBSPLTypeOfRelationship23" style="display:none;">
	<!-- PENSION LOAN -->
	<s:select list="#{1:'Pension Loan A/c',2:'Pension A/c'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
</div>
<div id="htmlCBSPLTypeOfRelationship27" style="display:none;">
	<!-- GOLD LOAN -->
	<s:select list="#{1:'Saving Bank Account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
</div>
 </s:if>

<div id="otpConfirmBox" style="display:none;"></div>
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/><s:property value="#minFolderPath"/>/jquery.cbs.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		if(loanTypeId!=17){
			/* $("#content-cbs").mCustomScrollbar({
				theme:"rounded",
				scrollInertia:5
			}); */
		}
	});
	function fetchCBSJsonData(){
		jsonJSArray1CBS = '<s:property value="%{jsonJSArray1CBS}"/>';
		jsonJSArray1CBS = jsonJSArray1CBS.replace(/%27/g, "\'");
		jsonJSArray1CBS = jsonJSArray1CBS.replace(/&quot;/g, "\"");
		return jsonJSArray1CBS;
	}
</script>

<!-- Ruchita Start -->
<script type="text/javascript">
    function cbsvalidate() {
        var mobile = document.getElementById("cbsaltMobile").value;
        var pattern =  /^([56789]{1})([\d]{3})[(\D\s)]?[\d]{3}[(\D\s)]?[\d]{3}$/;
        if (pattern.test(mobile)) {
            $("#alrerrorcbs").html("");
            return true;
        }
  $("#alrerrorcbs").html("<img src='" + BANK_IMAGE_FOLDER + "/cross.png'> Invalid Mobile No.");

  return false;

    }
</script>
<!-- END -->

<script>
$(".consentScrollForAuto").mCustomScrollbar({
        theme:"rounded",
        scrollInertia:5,
        mouseWheel:{scrollAmount:30},
        scrollButtons:{
                enable:true
        }

});
</script>
-----------------------------------------
/SBI/WebContent/appNew/loan/homeloan/HomeLoan.jsp



<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/appNew/common/HeaderProduct.jsp"></s:include>
	<div id="msg-panel" class="msg-war corner-all" style="display: <s:property value="%{responseMessage!=null?'block':'none'}" />;"><span>&nbsp;</span>
		<b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
	</div>
	<div id="page-loader">	
	    <div class="spinner-container">
	        <img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader_sbi.gif"  />
	        <div class="addmarginB20">Please Wait...</div>
	    </div>
	    <div class="loader-rpimg">
	    	<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/f-logo.png" alt=""  />
	    </div>
	</div>
	<s:set var="closeOffStr" value="%{' onbeforeunload=cloaseOff();cloaseOff(); '}"/>
	<body class="blue-bg" <s:property value="%{closeOffStr}" />>
		<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
				<!-- Google Tag Manager (noscript) -->
				<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-TRNVC76"
				height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
				<!-- End Google Tag Manager (noscript) -->
		</s:if>
		<div class="container-fluid ">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 no-padding">
					<s:include value="/appNew/common/MessagePageProduct.jsp"></s:include>
					<s:include value="/appNew/common/RightSideToggleMenuBar.jsp"></s:include>
					<div id="firstPageContent">
						<s:include value="/appNew/loan/homeloan/HomeFirstPage.jsp"></s:include>
					</div>
					<div id="secondPageContent"></div>
					<div id="thirdPageContent"></div>
					<div id="fourthPageContent"></div>
				</div>
			</div>
		</div>
		<s:include value="/appNew/loan/homeloan/CommonContent.jsp"></s:include>
		<s:include value="/appNew/common/ConsentPopup.jsp"></s:include>
		<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
		<div id="pixelTracking"></div>
		<div id="pixelTrackingGoCloud"></div>
		<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
			<%-- <s:if test="%{isDsrPage=='false'}"> --%>
				<!-- Facebook Pixel Code -->
				<script>
					!function(f, b, e, v, n, t, s) {
						if (f.fbq)return;
						n = f.fbq = function() {n.callMethod ? n.callMethod.apply(n, arguments) : n.queue.push(arguments)};
						if (!f._fbq)f._fbq = n;
						n.push = n;n.loaded = !0;n.version = '2.0';n.queue = [];t = b.createElement(e);t.async = !0;t.src = v;s = b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t, s)
					}(window, document, 'script','https://connect.facebook.net/en_US/fbevents.js');
					fbq('init', '306199269752742');fbq('track', 'PageView');
				</script>
				<noscript><img height="1" width="1" style="display: none"	src="https://www.facebook.com/tr?id=306199269752742&ev=PageView&noscript=1" /></noscript>
				<!-- DO NOT MODIFY -->
				<!-- End Facebook Pixel Code -->
			<%-- </s:if> --%>
		</s:if>
	</body>
</html>



---------------------------------------------
/SBI/WebContent/appNew/loan/homeloan/HomeFirstPage.jsp 

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{homeLoanPage <= 1}">
		<s:include value="/appNew/loan/homeloan/HomeCommonScript.jsp"></s:include>
	</s:if>
</s:if>
<s:include value="/appNew/loan/homeloan/HomeLeftSidebar.jsp"></s:include>
<s:include value="/appNew/loan/homeloan/HomeRightContent.jsp"></s:include>
<%-- <s:if test="%{homeLoanPage == 1}">
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081701&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081707&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	</s:if>
</s:if> --%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{homeLoanPage <= 1}">
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
	</s:if>
</s:if>



-------------------------------------------

/SBI/WebContent/appNew/common/ConsentPopup.jsp


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close"><span aria-hidden="true">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/></span></button> 
					<div id = "consentHomeLoanDiv" class="f-otp-pop-content ">
						<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
--------------------------------------------------------
              
              /SBI/WebContent/appNew/loan/homeloan/HomeFirstPageSession.jsp
              
              
              
              -----------
   <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="selectedLangue" value="%{#session.selectedLanguage}" />
<div class="clearfix"></div>
	<div class="clearfix"></div>
	</br>
	<h3>PROPERTY DETAILS</h3>
	<ul class="form-section">
		<s:set var="needToDisable" value="false"/>
		<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
			<s:if test="%{quote.loanQuoteLoanPurposeId==5}">
				<s:set var="needToDisable" value="true"/>
			</s:if>
		</s:if>
		<li id="LILOANPURPOSE">
			<label>Purpose of loan<b class="req">*</b></label>
			<div class="flat-field">
				<s:select list="%{beanList.loanPurposes==null?'':beanList.loanPurposes}" value="%{quote.loanQuoteLoanPurposeId}" 
			 		id="loanPurpose" name="quote.loanQuoteLoanPurposeId" headerKey="0" headerValue="Select loan purpose"
			 		cssClass="form-select %{(#needToDisable==true && isDsrPage=='true')?'disabledFields':''} customSelector"
			 		disabled="%{(#needToDisable==true && isDsrPage=='true')?'true':'false'}" onfocus="customOnFocus(this);"/>
			</div>
			<s:if test="%{isDsrPage=='false'}">
				<span id="plmsgId">
					<s:if test="%{quote.loanQuoteLoanPurposeId>0}">
						<a href='https://<s:property value="%{loanPurposeUrl}"/>' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>
					</s:if>
				</span>
			</s:if>
		</li>
		<s:if test="%{quote.loanQuoteLoanPurposeId==1 || quote.loanQuoteLoanPurposeId==3}">
			<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId.jsp"></s:include>
		</s:if>
		<div id="loanCategoryDetails">
			<s:if test="%{quote.loanQuoteLoanPurposeId==1}">
				<s:if test="%{quote.loanQuoteLoanCategoryId==1}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId1.jsp"></s:include>
				</s:if>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==2}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId2.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==3}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId3.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==4}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId4.jsp"></s:include>
				</s:elseif>
				<s:elseif test="%{quote.loanQuoteLoanCategoryId==5}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId5.jsp"></s:include>
				</s:elseif>
			</s:if>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==2}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId2.jsp"></s:include>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==3}">
				<s:if test="%{quote.loanQuoteLoanCategoryId==6}">
					<s:include value="/appNew/loan/homeloan/includes/LoanCategoryId6.jsp"></s:include>
				</s:if>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==5}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId5.jsp"></s:include>
			</s:elseif>
			<s:elseif test="%{quote.loanQuoteLoanPurposeId==25}">
				<s:include value="/appNew/loan/homeloan/includes/LoanPurposeId25.jsp"></s:include>
			</s:elseif>
		</div>
	</ul>
	
	<div class="clearfix"></div>
	</br>
	<h3>APPLICANT DETAILS</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/homeloan/HomeLoanApplicatnsDetail.jsp"></s:include>
	
		</ul>
	
	<ul class="form-section">
	</br>
	<s:include value="/appNew/loan/homeloan/PersonalIdentityDetails.jsp"></s:include>
	</ul>
<div class="clearfix"></div>   
</br>
<h3>INCOME DETAILS</h3>
<ul class="form-section">
	<s:include value="/appNew/loan/homeloan/HomeLoanIncomeDetails.jsp"></s:include>
</ul>
 <div class="clearfix"></div>
 </br>
<h3>CONTACT DETAILS</h3>
<ul class="form-section">
<s:include value="/appNew/loan/homeloan/ContactsDetails.jsp"></s:include>
</ul>

<div class="clearfix"></div>
      <div class="co-head flt">
				<h2>Click to add a co-applicant</h2>
	  </div>

<div class="co-app-btn flr">
<p>
		<a class="app-btn 11 collapsed" data-bs-toggle="collapse"
			href="#collapseExample" id="coAppImageDetails"
			data-bs-parent="#collapseOne" role="button" aria-expanded="false"
			aria-controls="collapseOne" onclick="showCrossBtn1();"> Co applicant details + </a>

</p>
</div>

<div class="collapse" id="collapseExample">
    <div class="card-header" id="headingOne">
      <h2 class="mb-0">
        <button class=" btn btn-link btn-block text-left collapsed"  type="button"  data-bs-toggle="collapse" href="#collapseExample"  id="coAppImageDetails" data-bs-parent="#collapseOne"  aria-expanded="false" aria-controls="collapseOne"
         onclick="showCrossBtn1();"> Co-applicant details 1  
          <span style="color:#76ABDF;font-size:11pt; float:right; display: none;width: 30px; " display="none" id="cross_btn2" > x  </span>
           <span style="color:#76ABDF;font-size:11pt; float:right; display: block;width: 30px;" id="cross_btn3" > +  </span>
    
      </h2>
    </div>
		<div id="collapseOne" class="collapse" aria-labelledby="headingOne" data-bs-parent="#accordionExample">
	      <div class="card-body">
	        <s:include value="/appNew/loan/homeloan/HomeLoanCoApplicant1.jsp"></s:include>
	      </div>
	   </div>
	
	    <div class="card-header" id="headingTwo">
	      <h2 class="mb-0">
	        <button class="btn btn-link btn-block text-left collapsed in" id="coll_btn" type="button" data-bs-toggle="collapse" data-bs-target="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo" onclick="showCrossBtn();">
	          Co-applicant details 2
	           <span style="color:#76ABDF;font-size:11pt; float:right; display: none;width: 30px;" display="none" id="cross_btn" > x   </span>
	           <span style="color:#76ABDF;font-size:11pt; float:right; display: block;width: 30px;" id="cross_btn1" > +   </span>
	        </button>
	      </h2>
	    </div>
	    <div id="collapseTwo" class="collapse" aria-labelledby="headingTwo" data-bs-parent="#accordionExample">
	      <div class="card-body">
	         <s:include value="/appNew/loan/homeloan/HomeLoanCoApplicant2.jsp"></s:include>
	      </div>
	   </div>   
</div>
<div id="aab" style="display:none;">
	<br>
	<br>
</div>
<div class="clearfix"></div>
	<h3>PREFERRED LOCATION</h3>
	<ul class="form-section">
		<s:include value="/appNew/loan/homeloan/HomeLoanPreferedLocation.jsp"></s:include>
		<div id="PREFERREDLOCATIONSTATE">
			<s:if test="%{quote.loanQuotePreferredLocationOfAvailingLoan==1 || quote.loanQuotePreferredLocationOfAvailingLoan==2}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredState.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONCITY">
			<s:if test="%{quote!=null && quote.loanQuotePreferredStateId!=null && quote.loanQuotePreferredStateId>0}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredCity.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONDISTRICT">
			<s:if test="%{quote!=null && quote.loanQuotePreferredCityId==9999999}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredDistrict.jsp"></s:include>
			</s:if>
		</div>
		<div id="PREFERREDLOCATIONBRANCH">
			<s:if test="%{quote!=null && quote.loanQuotePreferredCityId!=null && quote.loanQuotePreferredCityId>0}">
				<s:include value="/appNew/loan/homeloan/includes/PreferredBranch.jsp"></s:include>
			</s:if>
		</div>
	</ul>
	<s:if test="%{appSeqId==null}">
	<div class="clearfix"></div>
	</br>
	<h3>IDENTIFY YOURSELF</h3>
	<ul class="form-section captcha">
			<li>
			 <label>Copy text into input box<b class="req">*</b></label>
				<img id="HOMELoanFcapImage" name="HOMELoanFcapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('HOMELoanFcapImage');">
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
				<input type="text" class="form-control" id="captcha" name="captcha" value="" maxlength="6" placeholder="captcha" autocomplete="off"/>
			</li>
		</ul>
	</s:if>
	<div class="clearfix"></div>
	<div id="termsAndConditionFirst" class="sbi-trms-div">
		<ul class="form-section">
			<li class="full-width">
				<div class="trms-section">
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">
					<label for="infoprovide" class="label-content" style="font-size:14px; ">
						
						I/We certify that the information and particulars provided by me/us in this application form (and all documents referred or provided herewith) are true, correct, 
						complete and up to date in all respects and I have not withheld any information. I/ We authorize State Bank of India to make inquiries related to or verify said 
						information directly or through any third party. 
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> Read more </a></b><b class="req">*</b>
					</label>
				</div>
				<div> </div>
				<div> </div>
				<div class="qt-btn-div flr mrgt-10 m-100 call-us">
					<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="Get Loan Quote">
					<span class=callUS>Call us at 1800 425 3800</span>		
				</div>
			 </li>
		 </ul>
	</div>
 <script type="text/javascript">
		function showCrossBtn() {
			  var cross = document.getElementById("cross_btn");
			  var plus = document.getElementById("cross_btn1");
			  if (plus.style.display == "block") {
				  cross.style.display = "block";
				  plus.style.display = "none";
				  const myDiv = document.getElementById('collapseTwo');
					// Hide the div
					myDiv.style.display = 'block';
					$("#COAPPLICANT2").show();
			  } else if (cross.style.display == "block"){
				  plus.style.display = "block";
				  cross.style.display = "none";
				  const myDiv = document.getElementById('collapseTwo');
				// Hide the div
				myDiv.style.display = 'none';
			  }
			}
		
		function showCrossBtn1() {
			  var cross = document.getElementById("cross_btn2");
			  var plus = document.getElementById("cross_btn3");
			  if (plus.style.display == "block") {
				  cross.style.display = "block";
				  plus.style.display = "none";
				  const myDiv = document.getElementById('collapseOne');
					// Hide the div
					myDiv.style.display = 'block';
					$("#headingOne").show();
					$("#headingTwo").show();
					$("#aab").hide();
			  } else if (cross.style.display == "block") {
				  plus.style.display = "block";
				  cross.style.display = "none";
				  const myDiv = document.getElementById('collapseOne');

				$("#headingOne").hide();
				$("#headingTwo").hide();
				$("#collapseOne").hide();
				$("#COAPPLICANT2").hide();
				$("#aab").show();
			  } 
		}
</script>  
           -
