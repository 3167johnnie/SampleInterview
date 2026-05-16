<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
</s:if>
<s:else>
	<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0 || agriLoanPage>0}">
		<s:set var="minFolderPath" value="%{@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI+'/fineuploder/'}"/>
		<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
			<s:set var="minFolderPath" value="%{@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI+'/fineuploder/min/'}"/>
		</s:if>
		<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>jquery.commonThankYouPage.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>base.fineuploader.min.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		</s:if>
		<s:else>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>util.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>button.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>handler.base.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>handler.form.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>handler.xhr.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>uploader.basic.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>dnd.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>uploader.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
			<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>jquery-plugin.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
		</s:else>
		<script type="text/javascript" src="<s:property value="%{#minFolderPath}"/>jquery.fineuploader.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
	</s:if>
</s:else>

<%-- <s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
	<s:if test="%{homeLoanPage>0}">
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081702&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081707&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	</s:if>
	<s:elseif test="%{autoLoanPage>0}">
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081704&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081708&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	</s:elseif>
</s:if> --%>
<s:set var="documentTypeList" value="%{documentTypeList}"/>
<s:set var="productName" value="%{'Home Loan'}"/>
<s:if test="%{homeLoanPage>0}">
	<s:set var="productName" value="%{'Home Loan'}"/>
</s:if>
<s:elseif test="%{autoLoanPage>0}">
	<s:set var="productName" value="%{'Auto Loan'}"/>
</s:elseif>
<s:elseif test="%{personalLoanPage>0}">
	<s:set var="productName" value="%{'Personal Loan'}"/>
</s:elseif>
<s:elseif test="%{educationLoanPage>0}">
	<s:set var="productName" value="%{'Education Loan'}"/>
</s:elseif>
<s:elseif test="%{agriLoanPage>0}">
	<s:set var="productName" value="%{'Agri Loan'}"/>
</s:elseif>
<s:elseif test="%{creditCardPage>0}">
	<s:set var="productName" value="%{'Credit Card'}"/>
</s:elseif>
<s:set var="appForm" value="%{appForm}"/>
<div class="thank-you-container">
	<h2>
		<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
			<span class="sad-icon"></span>
		</s:if>
		<s:else>
			<span class="smile-icon"></span>
		</s:else>
		<br>Thank you
	</h2>
	<span class="blue-c">
		<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0}">
			&nbsp;<s:if test="%{#appForm.appGender.equalsIgnoreCase('M')}">Mr.</s:if><s:elseif test="%{#appForm.appGender.equalsIgnoreCase('F')}">Ms.</s:elseif><s:else></s:else>&nbsp;<s:property value="%{#appForm.appFirstName}"/>&nbsp;<s:property value="%{(appForm.appMiddleName==null||appForm.appMiddleName=='')?'':appForm.appMiddleName+'&nbsp;'}" escapeHtml="false"/><s:property value="%{#appForm.appLastName}"/>,
		</s:if>
	</span>
	
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE.equalsIgnoreCase('local') || @com.mintstreet.common.util.Constants@DEPLOYMENT_MODE.equalsIgnoreCase('uat')|| @com.mintstreet.common.util.Constants@DEPLOYMENT_MODE.equalsIgnoreCase('uat_rp')}">
		<s:if test="%{autoLoanPage>0 || personalLoanPage>0}">
			<div id="rsmValie" style="float: right; line-height: 20px; font-weight: bold; font-size: 13px; margin-right: 10px; color: red;">
				<span>* For UAT only: RSM score = <s:property value="%{#appForm.appRSMScore}"></s:property></span>
			</div>
		</s:if>
	</s:if>
	<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
		<div class="t-content <s:property value="%{creditCardPage>0?'thank-banner-div-383':''}"/> " style="<s:property value="%{creditCardPage>0?'':'height:250px;'}"/>">
		<s:if test="%{creditCardPage>0}">			                     
          	<span class="blue-c">Dear <s:property value="%{appForm.appFirstName}"/>,</span>
              	<p >Thank you for your interest. Our representative will get back to you soon.</p>
             	 <img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/sbi-banner.jpg" alt="">
       	</s:if>
        <s:else>
        	<p>We regret to inform you that we are currently unable to process your <s:property value="%{#productName}"/> request.</p>
            <p>Please contact our nearest Branch for further assistance.</p>
        </s:else>	
        <p>&nbsp;</p>
        <div class="clear"></div>
         </div>
	</s:if>	
	<s:else>
		<div class="t-content <s:property value="%{creditCardPage>0?'thank-banner-div-383':''}"/> " style="<s:property value="%{creditCardPage>0?'height:250px;':''}"/>">
			<p>Your <s:if test="homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0 || agriloanPage>0}"></s:if><s:property value="%{#appForm.appProductVariantName}"/>  <s:property value="%{#variantName}"/>
				application reference number is <b>#<s:if test="creditCardPage>0"><s:property value="%{#appForm.appCardCreditLeadRefNumber}"/></s:if><s:else><s:property value="%{#appForm.appReferenceId}"/></s:else></b>.
				Kindly use this reference number for all future correspondence or for tracking of your application status. Our <s:if test="%{!creditCardPage>0}">bank</s:if> representative will get in touch with you shortly to guide you on the next steps for processing your application.
			</p>
			
			<s:if test="personalLoanPage>0 && appForm.appPersonalLoanId==13">
			<p>
			Based on the details provided you are eligible for product(s):->
				<ul class="product-listing">
					<li>Gold Loan</li>
				</ul> 
			</p>
			</s:if>
			
			<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0 || agriLoanPage>0}">
			  <s:if test="%{homeLoanPage>0 || autoLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0  || (personalLoanPage>0 && appForm.appPersonalLoanId!=13)}">
				<s:if test="%{!#appForm.appWorkEmail.isEmpty() && #appForm.appWorkEmail!=null}">
					<p>A mail has been sent to <a href='mailto:<s:property value="%{#appForm.appWorkEmail}"/>'><s:property value="%{#appForm.appWorkEmail}"/></a> and SMS to contact number <s:property value="%{#appForm.appMobileNo}"/>.</p>
				</s:if>
				</s:if>
				<s:else>
				<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0  || (personalLoanPage>0 && appForm.appPersonalLoanId!=13)}">
					<p>A message has been sent to the contact number provided.</p>
					<p>&nbsp;</p>
				</s:if>
				</s:else>
				<p>Click the button below to download the <s:property value="%{#productName}"/> Application Form.</p>
				<p>&nbsp;</p>
				<div>
					<span class="download-btn-holder">
						<a id="downloadApplicationFormInPdf" onclick="javascript:callServiceForPDFName();"
						href="javascript:void(0);" class="download-btn">Download</a>
					</span>
					<span id="downloadMessage" class="loader-icon"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/>Please wait pdf generation in progress ...</span>
				</div>
			</s:if>
		 	<s:if test="%{creditCardPage>0}">
		 		<img src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/>/sbi-banner.jpg" alt="">
		 	</s:if>
		</div>
	</s:else>
	<div class="clearfix">&nbsp;</div>
	<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
	</s:if>
	<s:else>
		<div id="appointmentAndDocumentUpload">
			<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0  || (personalLoanPage>0 && appForm.appPersonalLoanId!=13)}">
				<form class="pickup-add" id="uploadDocumentDataAndPickupAddress" name="uploadDocumentDataAndPickupAddress" action="javascript:void(0);" enctype="multipart/form-data" >
					<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
					<s:hidden id="pageType" value="loanThankYou"></s:hidden>
					<div class="upload-pickup-box">
						<div id="accordion" class="thank-accord">
							<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0 || agriLoanPage>0}">
								<h3>UPLOAD DOCUMENT <br><small style="padding-right:15px;">(Document should be either images [jpg, jpeg, png, gif] or Adobe PDF Max File size limit 3 MB per document.)</small></h3>
								<div class="doc-upload-box">
									<ul class="upload-doc-form">
										<s:include value="/appNew/common/ThankYouCommonUploadContent.jsp"></s:include>
									</ul>
								</div>
							</s:if>
							<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0}">
								<h3>PICKUP ADDRESS</h3>
								<div class="doc-pickup-box">
									<ul class="form-section">
										<li class="full-width brd min-height lbl-fnt">
											<div class="col-xs-12 mrgt-10-10">
												<s:if test="%{personalLoanPage>0 && appForm.appPensionAccountNumber!=null}">
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check1" value="1" <s:property value="%{#appForm.appDocPickupCheck==null || (#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==1)?'checked=\"checked\"':''}"/> >
														<label for="check1"> Residence Address </label>
													</div>
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check4" value="4" <s:property value="%{#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==4?'checked=\"checked\"':''}"/>>
														<label for="check4">Submit at branch</label>
													</div>
												</s:if>
												<s:else>
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check1" value="1" <s:property value="%{#appForm.appDocPickupCheck==null || (#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==1)?'checked=\"checked\"':''}"/> >
														<label for="check1"> Residence Address </label>
													</div>
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check2" value="2" <s:property value="%{#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==2?'checked=\"checked\"':''}"/> >
														<label for="check2">Office Address </label>
													</div>
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check3" value="3" <s:property value="%{#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==3?'checked=\"checked\"':''}"/> >
														<label for="check3">Other Address </label>
													</div>
													<div class="blue-radio blue-radio-danger">
														<input type="radio" class="new-radio" name="appForm.appDocPickupCheck" id="check4" value="4" <s:property value="%{#appForm.appDocPickupCheck!=null && #appForm.appDocPickupCheck==4?'checked=\"checked\"':''}"/>>
														<label for="check4">Submit at branch</label>
													</div>
												</s:else>
											</div>
										</li>
										<div id="docPickupCheck1"></div>
										<div id="docPickupCheck2" style="display: none;"></div>
										<div id="docPickupCheck3" style="display: none;"></div>
										<div id="docPickupCheck4" style="display: none;"></div>
										<div id="collectionDateAndTime"  class="info-row3">
										
											<ul class="form-section">
												<li id="collectionDateAndTime">
													<label>Document collection date & time<span class="req">*</span></label>
													<div class="flat-field-date">
														<input type="text" name="appForm.appDocPickupDate" id="appDocPickupDate"  
														value="<s:property value="%{#appForm.appDocPickupDate}"/>" placeholder="dd-mm-yyyy" 
														class="form-select dob-cal" maxlength="10" onselect="onchangeOfdateOfPickUp();" onkeyup="checkDateVal();"/>
													</div>
													<div class="flat-field1 mr-r9">
														<s:select list="%{@com.mintstreet.common.util.Constants@PICKUP_TIME_LIST}" cssStyle="padding-right:23px;"
														id="appDocPickupTime" name="appForm.appDocPickupTimeString" value="appForm.appDocPickupTimeString" 
														cssClass="form-select" disabled="%{#appForm.appDocPickupTimeString!=null?'false':'true'}"
														headerKey="" headerValue="Select pickup time" onchange="javascript:updatePickupTimeRuile();" onfocus="customOnFocus(this);"/>
													</div>
												</li>
											</ul>
										</div>
									</ul>  
								</div>
							</s:if>
						</div>
						<div class="common-go-btn">
						  <input type="submit" class="submit-btn" value="Submit" name="btnUploadDocument" id="btnUploadDocument">
						</div>
					</div>
				</form>
			</s:if>
			<div class="clearfix">&nbsp;</div>
		</div>
		<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0 || (personalLoanPage>0 && appForm.appPersonalLoanId!=13)}">
			<s:include value="/appNew/common/WhatNext.jsp"></s:include>
		</s:if>
	</s:else>
</div>
<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
</s:if>
<s:else>
	<s:if test="%{homeLoanPage>0 || autoLoanPage>0 || personalLoanPage>0 || educationLoanPage>0 || homeTopUpLoanPage>0}">
		<s:include value="/appNew/common/ThankYouCommonContent.jsp"></s:include>
	</s:if>
</s:else>
<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local' && @com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='uat_rp'}">
	<s:if test="%{isDsrPage=='false'}">
		<s:if test="%{homeLoanPage>0 }">
			<!-- Facebook Pixel Code -->
			<script>
			fbq('track', 'CompleteRegistration',{loan :'Home'});
			</script>
			<!-- DO NOT MODIFY -->
			<!-- End Facebook Pixel Code -->
		</s:if>
		<s:elseif test="%{personalLoanPage>0}">
			<!-- Facebook Pixel Code -->
			<script>
			fbq('track', 'CompleteRegistration',{loan :'Personal'});
			</script>
			<!-- DO NOT MODIFY -->
			<!-- End Facebook Pixel Code -->
		</s:elseif>
		<s:elseif test="%{autoLoanPage>0}">
			<!-- Facebook Pixel Code -->
			<script>
			fbq('track', 'CompleteRegistration',{loan :'Auto'});
			</script>
			<!-- DO NOT MODIFY -->
			<!-- End Facebook Pixel Code -->
		</s:elseif>
	</s:if>
</s:if>
<s:if test="%{#appForm.appRSMdecision==null || appForm.appRSMdecision==0 || appForm.appRSMdecision==2}">
</s:if>
<s:else>
	<s:include value="/appNew/common/ThankYouCommonScript.jsp"></s:include>
</s:else>
<s:if test="%{agriLoanPage>0}">
	<style type="text/css">
		.agri-er span.error{text-align:center!important;}
		.prd-list{padding-bottom:10px;margin:0 0 0 15px;}
		.prd-list li{list-style-type:circle; color:#0199cd; padding:0 0 10px 0px;}
	</style>
</s:if>
