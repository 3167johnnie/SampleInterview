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

<script>

        jQuery(document).ready(function(){
                $('body').bind('cut copy paste',function(e){
                        e.preventDefault();
                });
        });
</script>	

	<div class="clearfix"></div>
	
	<ul class="form-section" style="display:none">
	
		<li>
			<label>Purpose of loan<b class="req">*</b></label>
			<div class="flat-field">
				<s:if test="%{personalLoanPage == 1}">		
					<s:select list="%{beanList.loanPurposes==null?'':beanList.loanPurposes}" value="%{cbs.cbsLoanPurpose}" id="cbsLoanPurpose" name="cbs.cbsLoanPurpose" cssClass="form-select" onfocus="customOnFocus(this);"/>
				</s:if>
			</div>
		</li>
		
	</ul>
	<%-- <div id="commonCbsDivId" class="form-div-cbs" style="display:<s:property value="%{showCBS?'block;':'none;'}" />"> --%>
	<s:if test="%{appPLTypeId!=@com.mintstreet.common.util.Constants@APP_PL_TYPE_PENSION}">
		<div id="CBSNONCBSDIV">
			<h3>RELATIONSHIP WITH BANK</h3>
			<ul class="form-section form-section-option <s:property value="%{showCBS?'show':'hide'}" />">
				<li id="divcbsRelationship" class="full-width brd lbl-fnt" style="min-height:50px; padding:0 0 0 0px; margin-bottom:20px;">
					<div class="row">
						<div class="text-end col-sm-6 mrgt-15 cpt">
							<label class="" style="color:#172154; font-size:13px;">Do you have an existing relationship with SBI?</label>
						</div>
						<div class="col-sm-6">
							<div class="blue-radio blue-radio-danger m-left-20">
									<input type="radio" name="relationshipWithSBI" id="relationshipWithSBIYes" checked="checked" value="1">
							<label for="relationshipWithSBIYes"> Yes </label>
							</div>	
							
						</div>
					</div>
				</li>
			</ul>
		</div>
	</s:if>

		<div id="test1">
			<ul class="form-section">
			<li>
                        <label>Type of relationship  <b class="req">*</b></label>
			 <div class="flat-field">
                        <s:select list="#{1:'Saving Bank account with SBI',2:'Salary Account with SBI',3:'Pension Account with SBI',4:'Home Loan Account with SBI',5:'Other deposit or loan account with SBI'}" headerKey="0" headerValue="Select type of Relationship" name="cbsTypeOfRelationship" id="cbsTypeOfRelationshipCve" onfocus="customOnFocus(this);" cssClass="form-select" value=""/>
                        </div>
			</li>

			<li>				
				<label class="">Account number<b class="req">*</b></label>
				<s:textfield name="cbs.cbsAccountNumber" id="accountNumber" value="" cssClass="form-control" 
					autocomplete="off" maxlength="11" placeholder="Account number" onkeydown="return M.digit(event);" />
			</li>
			<%-- <li>
				<label>Enter your mobile number with ISD Code&nbsp;<b class="req">*</b></label>
				<s:textfield name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" onkeydown="return M.digit(event);" />
				<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber" value="" autocomplete="off" maxlength="10"  placeholder="Mobile" cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>
			</li> --%>

			<%-- <li>
				<label>Enter your mobile number with ISD code<span class="req">*</span></label>
				<s:textfield name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91"  />
			        <input type="number" name="cbs.cbsIsdCode" id="cbsIsdCode" value="91" class="s-input flt form-control" autocomplete="off" maxlength="3" min="90" max="102" placeholder="91" onkeydown="return M.digit(event);" style="-moz-appearance:textfield;"/> 
				<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber" value="" autocomplete="off" maxlength="10" placeholder="Mobile" cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>
				<s:if test="%{educationLoanPage == 1}">
					<div class="custom-tooltip mobile-custom-tooltip">Please provide mobile number</div>
				</s:if>		
			</li> --%>

			<li>
			<label id="ll">Enter your mobile number with ISD Code &nbsp;<b class="req">*</b></label>
			<s:textfield name="cbsIsdCodeCve" id="cbsIsdCodeCve" value= "91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" onkeydown="return M.digit(event);"/>
			<s:hidden name="cbs.cbsIsdCode" id="cbsIsdCode" value="" />	
			
			<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber" value="" autocomplete="off" maxlength="10"  placeholder="Mobile" cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>	
			</li>
			
			<li>
			     <label>Product category desired &nbsp;<b class="req">*</b></label>
				<div class="flat-field">

				 <s:select list="%{beanList.cveProductCategories!=null?beanList.cveProductCategories:''}" name="cbs.cveProductCategory" id="cveProductCategory" 
				 	headerKey="0" headerValue="Select type of Product Category" cssClass="form-select" onfocus="customOnFocus(this);" value="%{cveForm.productCategory}"/>

			</div>
			</li>

			<li>
				<label>Salutation &nbsp; <b class="req">*</b> </label>
				<div class="flat-field">
				<s:select list="#{'01':'Mr.','02':'Mrs.','04':'Miss.','05':'Dr.','06':'Dr.(Mrs.)','07':'Master','09':'Shri'}" headerKey="0" headerValue="Select type of Salutation" name="cbs.cveSalutation" id="cveSalutation" onfocus="customOnFocus(this);" cssClass="form-select" value=""/>
			        </div>
			</li>
			<li>
				<label>First Name &nbsp;<b class="req">*</b></label>
				<s:textfield name="cbs.cveAppFirstName" id="cveAppFirstName" value="" autocomplete="off" maxlength="20"  placeholder="First Name" cssClass="form-control" required="required" onkeydown="return M.isChars(event);"/>
			</li>
			<li>
				<label>Middle Name &nbsp; </b></label>
				<s:textfield name="cbs.cveAppMiddleName" id="cveAppMiddleName" value="" autocomplete="off" maxlength="20"  placeholder="Middle Name" cssClass="form-control"  onkeydown="return M.isChars(event);"/>			
			</li>
			<li>
				<label>Last Name &nbsp;<b class="req">*</b></label>
				<s:textfield name="cbs.cveAppLastName" id="cveAppLastName" value="" autocomplete="off" maxlength="20"  placeholder="Last Name" cssClass="form-control" required="required" onkeydown="return M.isChars(event);"/>
			</li>
		    <li>
				<s:include value="/appNew/common/WorkPlaceDetailsCve.jsp"></s:include>
			</li>
			</ul>


	     </div>  
	<%-- </div> --%>
	<div id="test2"> 
		<div id="submit-btn">
			<div class="clearfix"></div>
				<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display :<s:property value="%{showCBS?'block':'none'}"/>;">
					<ul class="form-section">
						<li class="full-width">
							<%-- <div class="trms-section">
							 <div class="consentCheckbox">
								<input type="checkbox" class="blue-css-checkbox colorCve" id="infoprovideCBScve" name="infoprovideCBS">
								
								
								<label for="infoprovideCBScve" class="label-content" style="font-size: 10px;">
									<s:property escapeHtml="false" value="%{beanList.consentCveLoan}" />
									&nbsp;<b class="req">*</b>
								</label>
								
							</div>
							</div> --%>
							
							<div class="trms-section">
					<!-- <input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on"> -->
					<!-- // checkbox disabled for NTB HL -->
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">
					<label for="infoprovide" class="label-content" style="font-size:14px; ">
						
						I hereby authroize State Bank of India and/or its representative to contact me with reference to my application”. For more details please read  
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentCveLoan','1');"> SBI’s Privacy Notice </a></b><b class="req">*</b>
					</label>
				</div>
							
	
							<div class="qt-btn-div trms-btn flr mrgt-10">
								<input  type="submit" class="submit-btn" value="Submit" id="submitBtn">
							</div>
						</li>
					</ul>
				</div>
			</div>
	 </div>	 
	 <br/>
	 <br/>	 
	 

<div id="consentRevocation"><h4 style="color:#337ab7;cursor: pointer;font-size:18px"><u><b style="margin-left: 12px;"> Consent Revoke ? </b></u></h4></div> 

<input type="hidden" id="consentRevocation1" name="cbs.cveAppConsentRevokeYes" value="N">

		<div id="test3" style="display:none;" >
			<ul class="form-section">	   

			<li>
            	<label>Type of relationship  <b class="req">*</b></label>
			 	<div class="flat-field">
                	<s:select list="#{1:'Saving Bank account with SBI',2:'Salary Account with SBI',3:'Pension Account with SBI',4:'Home Loan Account with SBI',5:'Other deposit or loan account with SBI'}" headerKey="0" headerValue="Select type of Relationship" name="cbsTypeOfRelationshipRevoke" id="cbsTypeOfRelationshipCveRevoke" onfocus="customOnFocus(this);" cssClass="form-select" value=""/>
                 </div>
			</li>

			<li>
				<label class="">Account number<b class="req">*</b></label>
				<s:textfield name="cbs.cbsAccountNumber" id="accountNumber" value="" cssClass="form-control" 
					autocomplete="off" maxlength="11" placeholder="Account number" onkeydown="return M.digit(event);" />
			</li>
			
			<li>
				<label>Enter your mobile number with ISD Code&nbsp;<b class="req">*</b></label>
			<s:textfield name="cbs.cbsIsdCode" id="cbsIsdCodeRevoke" value= "91" cssClass="s-input flt form-control" autocomplete="off" maxlength="3" placeholder="91" onkeydown="return M.digit(event);" />
				<s:textfield name="cbs.cbsMobileNumber" id="cbsMobileNumber1" value="" autocomplete="off" maxlength="10"  placeholder="Mobile" cssClass="b-input ml10 flt form-control" onkeydown="return M.digit(event);"/>
			</li>
			
			
			</ul>
        </div>
	 <%-- </div> --%>
	 <br/>
	 <br/>
<div id="test4" style="display:none;">
	<div id="submit-btn">
		<div class="clearfix"></div>
<div id="termsAndConditionCBS" class="sbi-trms-div sbi-trms-div-cbs" style="display :<s:property value="%{showCBS?'block':'none'}"/>;">
				<ul class="form-section">
					<li class="full-width">
						<div class="trms-section">
							<input type="checkbox" class="blue-css-checkbox colorCveRevoke" id="infoprovideCBSRevoke" name="infoprovideCBS">
							<label for="infoprovideCBSRevoke" class="label-content" style="font-size: 10px;">
								<s:property escapeHtml="false" value="%{beanList.consentCveRevoke}" />
								&nbsp;<b class="req">*</b>
							</label>
						</div>		
						
						<div class="qt-btn-div trms-btn flr mrgt-10">
							<input  type="submit" class="submit-btn" value="Submit" id="submitBtn">
						</div>
					</li>
				</ul>
			</div>
		</div>
   </div>
   
