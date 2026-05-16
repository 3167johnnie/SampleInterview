<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="info-form-box-loan" <s:property value="%{showCBSForPL?'show':'hide'}"/> id="commonCbsForPL">
	<div class="clearfix"></div>
	<h3>LOAN PURPOSE</h3>
	<ul class="form-section">
		<li>
			<label>Purpose of loan<b class="req">*</b></label>
			<div class="flat-field">
				<s:if test="%{personalLoanPage == 1}">		
					<s:select list="%{beanList.loanPurposes==null?'':beanList.loanPurposes}" value="%{cbs.cbsLoanPurpose}" id="cbsLoanPurpose" name="cbs.cbsLoanPurpose" cssClass="form-select" onfocus="customOnFocus(this);"/>
				</s:if>
			</div>
			<s:if test="%{isDsrPage=='false'}">
				<div id="plmsgId">
					<s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
						<a href='https://<s:property value="%{loanPurposeUrl}"/>' target="_blank"><span class="note">Click to read the scheme details before applying</span></a>
					</s:if>
				</div>
			</s:if>
		</li>
	</ul>
	<div id="commonCbsDivId" class="form-div-cbs" style="display:<s:property value="%{showCBS?'block;':'none;'}" />">
	<s:if test="%{appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_PENSION}">
		<div id="CBSNONCBSDIV">
		  <div class="clearfix"></div>
	        </br>
			<h3>RELATIONSHIP WITH BANK</h3>
			<ul class="form-section form-section-option <s:property value="%{showCBS?'show':'hide'}" />">
				<li id="divcbsRelationship" class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
				<!-- 	<div class="col-xs-12 col-sm-6 mrgt-15">
						<label class="" style="color:#172154; font-size:13px;">Do you have an existing relationship with SBI?</label>
					</div>
					<div class="col-xs-12 col-sm-6">
						<div class="blue-radio blue-radio-danger m-left-20">
							<input type="radio" name="relationshipWithSBI" id="relationshipWithSBIYes" checked="checked" value="1">
							<label for="relationshipWithSBIYes"> Yes </label>
						</div>	
						<div class="blue-radio blue-radio-danger">
							<input type="radio"  name="relationshipWithSBI" id="relationshipWithSBINo" value="2" >
							<label for="relationshipWithSBINo">No </label>
						</div>
					</div> -->
					
					<div class="row">
						<div class="text-end col-sm-6 mrgt-15 cpt">
							<label class="" style="color:#172154; font-size:13px;">Do you have an existing relationship with SBI?</label>
						</div>
						<div class="col-sm-6">
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
	</s:if>
		<ul class="form-section">
			<li>
				<label>Type of relationship<b class="req">*</b>
				<a class="tool-tip"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/><span>If you maintain multiple accounts with us, please select the first option from the dropdown.</span></a></label>
				<!-- Please maintain the below Ids, because of Back end implementation we need these Ids as it. 3:'Salary A/c With SBI',2:'Other deposit or loan account'-->
				<div id="relationshipWithSBIBank" class="flat-field">
					<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_GOLD}">
							<s:select list="#{1:'Saving Bank Account',2:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
					</s:if>
					<s:else>
						<s:if test="homeLoanPage>=0">
							<s:select list="#{1:'Home loan',2:'Salary A/c With SBI',3:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
						</s:if>
						<s:elseif test="autoLoanPage>=0">
							<s:select list="#{1:'Home loan',2:'Salary a/c with SBI',3:'Pension a/c with SBI',4:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
						</s:elseif>
						<s:elseif test="personalLoanPage>=0">
							
						</s:elseif>
						<s:elseif test="educationLoanPage>=0">
							<s:select list="#{1:'Education Loan',2:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-selectl"/>
						</s:elseif>
						<s:elseif test="agriLoanPage>=0">
							<s:select list="#{1:'Savings account',2:'Kisan credit card',3:'Tractor loan',4:'Other deposits'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
						</s:elseif>
						<s:elseif test="creditCardPage>=0">
							<s:select list="#{1:'Home loan',2:'Salary A/c With SBI',3:'Other deposit or loan account'}" value="%{cbs.cbsTypeOfRelationship}"  id="cbsTypeOfRelationship" name="cbs.cbsTypeOfRelationship" headerKey="-1" headerValue="Select type of relationship" onfocus="customOnFocus(this);" cssClass="form-select"/>
						</s:elseif>
					</s:else>
				</div>
			</li>
			<li>				
				<label class="">Account number<b class="req">*</b></label>
				<s:textfield name="cbs.cbsAccountNumber" id="accountNumber" value="" cssClass="form-control" 
					autocomplete="off" maxlength="11" placeholder="Account number" onkeydown="return M.digit(event);" />
			</li>
			<li>
				<label>Enter your mobile number with ISD Code&nbsp;<b class="req">*</b></label>
				<!-- <label>Mobile<b class="req">*</b></label> -->
				<s:textfield name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" onkeydown="return M.digit(event);" />
				<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber" value="" autocomplete="off" maxlength="10"  placeholder="Mobile" cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>
			</li>
			<div id="xpressCreditLoan"></div>
		</ul>
	</div>
	<div id="submit-btn">
		<div class="clearfix"></div>
			<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display :<s:property value="%{showCBS?'block':'none'}"/>;">
				<ul class="form-section">
					<li class="full-width">
						<div class="trms-section">
							<input type="checkbox" class="blue-css-checkbox" id="infoprovideCBS" name="infoprovideCBS">

							<!-- personal and pension loan consent -->
							<s:if test="%{appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_GOLD}">
								<label for="infoprovideCBS" class="label-content" id="personalLoanConsentId">
									<s:property escapeHtml="false" value="%{beanList.consentPersonalLoanEtb}" />
										&nbsp;<b class="req">*</b>
								</label>
							</s:if>
							
							<!-- gold loan consent -->
							<s:if test="%{appPLTypeId==@com.mintstreet.common.util.Constants@APP_PL_TYPE_GOLD}">
								<label for="infoprovideCBS" class="label-content" id="personalLoanConsentId">
									<s:property escapeHtml="false" value="%{beanList.consentGoldLoanEtb}" />
										&nbsp;<b class="req">*</b>
								</label>
							</s:if>

						</div>
						<div class="qt-btn-div trms-btn flr mrgt-10">
							<input  type="submit" class="submit-btn" value="Submit" id="submitBtn">
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
