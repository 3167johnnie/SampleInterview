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
