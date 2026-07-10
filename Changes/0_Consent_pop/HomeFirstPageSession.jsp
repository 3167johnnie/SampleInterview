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
			<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
				<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" checked="checked" style="display:none">
			</s:if>
			<s:else>
				<div class="trms-section">
					<!-- <input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on"> -->
					<!-- // checkbox disabled for NTB HL -->
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">
					<label for="infoprovide" class="label-content" style="font-size:14px; ">
						
						I hereby authorize State Bank of India and/or its representative to contact me with reference to my application”. For more details please read  
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> SBI’s Privacy Notice </a></b><b class="req">*</b>
					</label>
				</div>
			</s:else>
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
 jQuery(document).ready(function(){
		
	 $('#infoprovide').change(function() {
	    if ($(this).is(':checked')) {
	    } else {
	    	$('#infoprovide').prop('disabled', true);
	    }
	}); 
	  //$('#infoprovide').removeAttr('disabled').prop('disabled', false); 
	  
	  $('input[type="checkbox"][readonly]').on('click keydown', function(e) {
	        e.preventDefault();
	    });
	 
	  	
	$('#infoprovide').rules('add', {
			required: true,
		    valueNotEquals: 0
	});	
	jQuery('#infoprovide').addClass('error');
	  
});
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
			} else if (cross.style.display == "block") {
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
