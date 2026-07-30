<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/version.jsp"></s:include>
	</br>
<h3>PURPOSE OF LOAN</h3>
<ul class="form-section">
	<s:include value="/appNew/loan/personal/includes/HtmlLoanPurpose.jsp"></s:include>
</ul>
<div class="clearfix"></div>
	</br>
<h3>APPLICANT DETAILS</h3>
<ul class="form-section">
	<%-- <s:include value="/appNew/loan/personal/ApplicantDetails.jsp"></s:include> --%>
	<li id="33">
	<label>Date of birth<b class="req">*</b></label>
	<s:if test="%{quote.loanQuoteHaveSalaryAccountWithSbi.equalsIgnoreCase('Y') && quote.loanQuoteDateOfBirth!=null}">
		<input type="text" name="quote.loanQuoteDateOfBirth" id="dateOfBirth"  placeholder="dd-mm-yyyy" class="disabledFields form-control"  disabled="true"  maxlength="10" value="<s:property value="%{quote.loanQuoteDateOfBirth}"/>" >
	</s:if>
	<s:else>
		<input type="text" name="quote.loanQuoteDateOfBirth" id="dateOfBirth"  placeholder="dd-mm-yyyy" class="form-control dob-cal" maxlength="10" value="<s:property value="%{quote.loanQuoteDateOfBirth}"/>" >
	</s:else>
	</li>
	<li><label> Resident type<b class="req">*</b></label>
		<div class="flat-field">
			<s:select list="%{beanList.residentTypes}" value="%{quote.loanQuoteResidentTypeId}"  onchange="showPerspectiveRows4ResidentType(this)" cssClass="form-select" id="residentType" name="quote.loanQuoteResidentTypeId" headerKey="0" headerValue="Select resident type" onfocus="customOnFocus(this);"/>
		</div>
	</li>
</ul>
<div class="clearfix"></div>
	<li>
		</br>
		<s:include value="/appNew/loan/personal/PersonalIdentityDetailsForGold.jsp"></s:include>
	</li>
<div class="clearfix"></div>
</br>
<h3>CONTACT DETAILS</h3>
	
<ul class="form-section">
<s:include value="/appNew/loan/personal/ContactsDetails.jsp"></s:include>
</ul>
<s:if test="%{appSeqId==null}">
	<li>
	<div class="clearfix"></div>
	</br>
		<h3>IDENTIFY YOURSELF</h3>
			<ul class="form-section captcha">
				<li>
					<label>Copy text into input box<span class="req"> *</span></label>
    				 <img id="captchaFirstPage" name="personalFcapImage" src="Captcha.jpg"><a class="refresh-link" href="javascript:void(0);" title="Refresh" onclick="return refreshCaptcha('captchaFirstPage');">
    				 <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/refresh.jpg"></a>
    				 <input type="text" class="form-control" id="captcha" name="captcha" value="" maxlength="6" placeholder="Captcha" autocomplete="off"/>
				</li>
		 </ul>
   </li>
</s:if>
<div class="clearfix"></div>
<div id="termsAndConditionFirst" class="sbi-trms-div">
	<ul class="form-section">
		<li class="full-width">
		
			<s:if test="%{appForm.appMobileVerified!=null && appForm.appMobileVerified.equalsIgnoreCase('Y')}">
				<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" checked="checked" style="display:none">
			</s:if>
			<s:else>
				<div class="trms-section gold-conscent">
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">
	
					<label for="infoprovide" class="label-content">
						<s:property escapeHtml="false" value="%{beanList.consentGoldLoanNTB}" />
						&nbsp;<b class="req">*</b>
					</label>

				</div>
			</s:else>
			
			
			<div class="flr mrgt-10">
				<input  type="submit" class="submit-btn" name="subtn" id="subtn" value="SUBMIT APPLICATION"></div>
			<br>
			<s:if test="%{appForm==null || (appForm.appMobileVerified.equalsIgnoreCase('N') && appForm.appEmailVerified.equalsIgnoreCase('N') )}">
				<div class="txt-center"><a class="callBackProduct" href="javascript:void(0);" onclick="javascript:showProductCallback(27);">Get a call back</a></div>
			</s:if>
		 </li>
	 </ul>
</div>
