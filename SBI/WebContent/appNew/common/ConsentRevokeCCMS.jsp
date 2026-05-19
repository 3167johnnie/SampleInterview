
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
