<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
			<s:include value="/app/common/version.jsp"></s:include>
				<div id="msg-panel" class="msg-war corner-all" style="display: <s:property value="%{responseMessage!=null?'block':'none'}" />;"><span>&nbsp;</span>
				  <b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
				</div>
				<form name="educationloancriteriaform" id="educationloancriteriaform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded">
					<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
					 <h3>PERSONAL DETAILS (Student)</h3>	
						<ul class="form-section">
	                        <li>
					            <label>First name<b class="req">*</b></label>
					             <%-- <s:textfield name="appForm.appFirstName"  id="appFirstName" value="%{appForm.appFirstName==null?#session.applicantName:appForm.appFirstName}"  maxlength="20"  onblur="ChangeCase(this);"
					             cssClass="form-control %{(isDsrPage=='true' && #session.applicantName!=null ? ' disabledFields' : 'disabledFields')}" disabled="%{(isDsrPage=='true' && #session.applicantName!=null ? 'true' : 'false')}" /> --%>
					             <s:textfield name="appForm.appFirstName"  id="appFirstName" value="%{appForm.appFirstName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);"/>
							 </li>
							<li>
	      						<label>Middle name<span>&nbsp;</span></label>
	     						<s:textfield name="appForm.appMiddleName"  id="appMiddleName" value="%{appForm.appMiddleName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);"/>
	     					</li>
							<li>
	                        	<label>Last name<b class="req">&nbsp;*</b></label>
	                            <s:textfield name="appForm.appLastName"  id="appLastName" value="%{appForm.appLastName}" cssClass="form-control"  maxlength="20" onblur="ChangeCase(this);"/>
							</li>
							<s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId!=24}">
								<li id="eduthirdpagedob1">
									<label> Date of birth<b class="req">&nbsp;*</b></label>
										<s:textfield name="appForm.appDob" id="third_date_of_birth"
										cssClass="form-control dob-cal" value="%{appForm.appDob!=null?appForm.appDob:''}"
										maxlength="10" placeholder="dd-mm-yyyy"/>
								</li>
						   </s:if>
						 <li>
                            <label>Current address line 1<b class="req">&nbsp;*</b></label>
                            <s:textfield name="appForm.appAddress1"  id="appAddress1" value="%{appForm.appAddress1}" maxlength="40" cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
						</li>
						<li>
                           <label>Current address line 2<b class="req">&nbsp;*</b></label>
                           <s:textfield name="appForm.appAddress2"  id="appAddress2" value="%{appForm.appAddress2}" maxlength="30" cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
						</li>
                        <li>
                            <label>Landmark<b class="req">&nbsp;</b></label>
                            <s:textfield name="appForm.appAddressLandmark"  id="appAddressLandmark" value="%{appForm.appAddressLandmark}" maxlength="30" cssClass="form-control" onblur="ChangeCase(this);" onkeydown="return M.isAddressLine(event);" />
						</li>
							 <s:include value="/appNew/loan/educationloan/includes/EducationAllocation.jsp"></s:include> 
						 <li>
							<label>Pincode<span class="req">&nbsp;*</span></label>
							<s:textfield  value="%{appForm.appPincode}" id="appPincode" cssClass="form-control" name="appForm.appPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"/>
						 </li>
                           <s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId==24}"> 
                           <li>
                              <label>Net annual income, If any</label>
                              <s:textfield name="appForm.appNetAnnualIncome"  id="appNetAnnualIncome" cssClass="form-control"  value="%{appForm.appNetAnnualIncome}" placeholder="Rs"  maxlength="10"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
                           </li>
                          </s:if>
                          <s:if test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
                          <s:if test="%{quote.loanQuoteCountryOfStudyId!=null && quote.loanQuoteCountryOfStudyId==1}">  
                            <li>
                             	<label> Last educational qualification<b class="req">&nbsp;*</b></label>
                             	 <div class="flat-field">
								<s:select  list ="#{0:'Select educational qualification','5':'Doctrate',1:'Post Graduate or Above',2:'Graduate',3:'HSC(10+2)'}" 
								id="loanQuoteLastEducationalQualificationId"  name="quote.loanQuoteLastEducationalQualificationId" 
								cssClass="form-select" value="%{quote.loanQuoteLastEducationalQualificationId}" disabled="true" onfocus="customOnFocus(this);"/>
								</div>
                            </li>
                          </s:if>
                          </s:if>
						 <s:elseif test="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI}">
							<s:if test="%{quote.loanQuoteCountryOfStudyId!=null && quote.loanQuoteCountryOfStudyId==2}">
							 <li>
								<label> Last educational qualification<b class="req">*</b></label>
								<s:select list ="#{'0':'Select educational qualification','5':'Doctrate','1':'Post Graduate or Above','2':'Graduate'}" cssClass="form-select" id="lastEducationalQualificationId" name="quote.loanQuoteLastEducationalQualificationId" cssStyle="border-color: rgb(197, 197, 197);" autocomplete="off" aria-invalid="false" value="%{quote.loanQuoteLastEducationalQualificationId}" onfocus="customOnFocus(this);" />
							</li>
							</s:if>
						</s:elseif>
                        <s:else>
	                       	 <li>
	                            <label> Last educational qualification<b class="req">&nbsp;*</b></label>
	                             <div class="flat-field">
									<s:select  list ="#{0:'Select educational qualification',1:'Post Graduate or Above',2:'Graduate',3:'HSC(10+2)',4:'Below Higher Secondary'}" 
									id="loanQuoteLastEducationalQualificationId"  name="quote.loanQuoteLastEducationalQualificationId" 
									cssClass="form-select" value="%{quote.loanQuoteLastEducationalQualificationId}" disabled="true" onfocus="customOnFocus(this);"/>
	                      		</div>
	                       </li>
                      </s:else>
	                  <s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId!=24}">
                         <li> 
                          	<label>Under what quota did you secure admission?<b class="req">&nbsp;*</b></label><br>
	                           <div class="flat-field">
		                          	<s:select list="#{0:'Select quota',1:'Regular',2:'Management'}" name="appForm.appAdmissionQuota" 
		                          	value="%{appForm.appAdmissionQuota}" id="appAdmissionQuota" cssClass="form-select"  onfocus="customOnFocus(this);"/>
	                          	</div>
                    	</li>
                    </s:if>
                    </ul>
				<%--start --%>
				<s:if test="%{quote.loanQuoteCountryOfStudyId == 2}">
					<div class="clearfix"></div>
					<h3><span class="flt">PREFERRED LOCATION FOR AVAILING LOAN</span></h3>
					<ul class="form-section">
						<!-- <div id="LOCATIONEDVANTAGESTATE"> -->
							 <li>
								 <div id="LOCATIONEDVANTAGESTATE">
									<label >State<b class="req">&nbsp;*</b></label>
									<div class="flat-field">
										<s:select list="%{beanList.edvantageState}" name="appForm.appEdvantageStateId" cssClass="form-select" id="appEdvantageStateId" value="%{appForm.appEdvantageStateId}"  headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
									</div>
								 </div>
							</li>
						<!-- </div> --> 
								<div id="LOCATIONEDVANTAGECITY"  >
									<s:if test="%{appForm.appEdvantageStateId!=null && appForm.appEdvantageStateId>0}"> 
										<s:include value="/appNew/loan/educationloan/includes/edvantageCity.jsp"></s:include>
											<%-- <s:if test="%{appForm.appEdvantageCityId!=null && appForm.appEdvantageCityId>0 && appForm.appEdvantageCityId.equals(@com.mintstreet.common.util.Constants@OTHER_ID_INTEGER)}">
												<s:if test="%{quote.loanQuoteCountryOfStudyId == 2 && appForm.appEducationLoanId == 7}"> --%>
													<%-- <div  id="LOCATIONEDVANTAGEDISTRICT">
														<s:if test="%{(appForm.appEdvantageDistrictId!=null || appForm.appEdvantageDistrictId >0) && beanList.edvantageDistrict!=null}">
															<s:include value="/appNew/loan/educationloan/includes/edvantageDistrict.jsp"></s:include>
														</s:if>
													</div> --%>
												</s:if>
												<%-- <div id="LOCATIOEDVANTAGENBRANCH">
													<s:include value="/appNew/loan/educationloan/includes/edvantageBranch.jsp"></s:include>
												</div> --%>
										<%-- </s:if>
								</s:if> --%>
							</div>
							
							<div  id="LOCATIONEDVANTAGEDISTRICT">
								<s:if test="%{(appForm.appEdvantageDistrictId!=null || appForm.appEdvantageDistrictId >0) && beanList.edvantageDistrict!=null}">
									<s:include value="/appNew/loan/educationloan/includes/edvantageDistrict.jsp"></s:include>
								</s:if>
							</div>
													
													
						    <div id="LOCATIOEDVANTAGENBRANCH">
							    <s:if test="%{appForm.appEdvantageBranchId!=null && appForm.appEdvantageBranchId >0}">
									<s:include value="/appNew/loan/educationloan/includes/edvantageBranch.jsp"></s:include>
								</s:if>
							</div>
						<!-- </div> -->
					</ul>
				</s:if>
			<s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId==24 && quote.loanQuoteResidentTypeId==2}">
			<div class="clearfix"></div>
			<h3><span class="flt">ABROAD ADDRESS</span></h3>
      		<ul class="form-section">
      			<s:include value="/appNew/loan/educationloan/TakeoverAboardAddress.jsp"></s:include>
      		</ul>
      		</s:if>
			<div class="clearfix"></div>
			<h3><span class="flt">PERMANENT ADDRESS</span></h3>
      		<div class="prm-chk-div">
	      		<input type="checkbox" name="appForm.clonePermanentAddress" id="clonePermanentAddress" value="<s:property value="%{appForm.clonePermanentAddress==null?'false':'true'}"/>" <s:property value="%{appForm.clonePermanentAddress==true?'checked=\"checked\"':''}"/> class="blue-css-checkbox">
				<label class="label-content" for="clonePermanentAddress">Same as current address</label>
     		</div>
			 	<!--  <div class="clearfix"></div> -->
   			<ul class="form-section">
				<li>
		            <label>Address line 1<b class="req">*</b></label>
		            <s:textfield name="appForm.appPermanentAddress1"  id="appPermanentAddress1" value="%{appForm.appPermanentAddress1}"   maxlength="%{isDsrPage=='true'?100:'100'}"
		             cssClass="form-control %{appForm.clonePermanentAddress==true?'disabledFields ':'disabledFields'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
		             onkeydown="return M.isAddressLine(event);" />
		        </li>
		        <li>
		           <label>Address line 2</label>
		           <s:textfield name="appForm.appPermanentAddress2"  id="appPermanentAddress2" value="%{appForm.appPermanentAddress2}"   maxlength="%{isDsrPage=='true'?100:'100'}" 
		            cssClass="form-control %{appForm.clonePermanentAddress==true?'form-control':'form-control'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
		            onkeydown="return M.isAddressLine(event);" />
				</li>
				<li>
		           <label>Landmark</label>
		           <s:textfield name="appForm.appPermanentAddressLandMark"  id="appPermanentAddressLandMark" value="%{appForm.appPermanentAddressLandMark}"   maxlength="20" 
		           cssClass="form-control disabledFields" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"
		           onkeydown="return M.isAddressLine(event);" /> 
		      	</li>
				<li id="LOCATIONPERMANENTSTATE" >
				    <div id="permanentStates">
				    <label>State<b class="req">*</b></label>
				   <div class="flat-field">
				   <s:select name="appForm.appPermanentStateId"  list="%{#states}" id="appPermanentStateId"
				 	value="%{appForm.appPermanentStateId}" headerKey="0" headerValue="Select state" 
				    cssClass="form-select disabledFields" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onfocus="customOnFocus(this);"/>
				    </div> 
				    </div>
				</li>
				<div id="LOCATIONPERMANENTCITY">
					<s:include value="/appNew/loan/educationloan/includes/ScholarCity.jsp"></s:include>
				</div> 
				<div id="LOCATIONPERMANENTDISTRICT">
					<s:if test="%{appForm.appPermanentDistrictId!=null && appForm.appPermanentDistrictId >0 && appForm.appPermanentCityId == 9999999}">
						<li>
						<label>District<b class="req">&nbsp;*</b></label>
							<div class="flat-field">
								 <s:select list="%{beanList.districtsPermanent==null?'':beanList.districtsPermanent}" name="appForm.appPermanentDistrictId" 
						              id="appPermanentDistrictId" value="%{appForm.appPermanentDistrictId}"  headerKey="0" headerValue="Select district"
						              onfocus="customOnFocus(this);" cssClass="form-select">
								 </s:select>
							</div>
						</li>
					</s:if>
				</div>
				<li>
				    <label>Pincode<b class="req">*</b></label>
				       <s:textfield name="appForm.appPermanentPincode" id="appPermanentPincode" value="%{appForm.appPermanentPincode}" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"
				       cssClass="form-control"  disabled="%{appForm.clonePermanentAddress==true?'true':'false'}"/> 
				</li>
			</ul>
				<%--end --%>
		 	<div class="clearfix"></div> 
			<h3>IDENTITY DETAILS</h3>
			<ul class="form-section form-section-li" id="5">
				 <s:if test="%{quote.loanQuoteCountryOfStudyId!=null && quote.loanQuoteCountryOfStudyId==1 }">
						<li>
							<label>Do you want to avail SBI life insurance?<b class="req"></b></label>
								<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
										<div class="blue-radio blue-radio-danger">
											<input type="radio" value="Y" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" checked="checked" value="Y">
											<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
										</div>
										<div class="blue-radio blue-radio-danger">
											<input type="radio" value="N" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" >
											<label for="appInterestedSbiLifeInsuranceNo"> No </label>
										</div>
								</div>
							</li>
					</s:if>
					<s:elseif test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId==24}">
						<li>
							<label>Do you want to avail SBI life insurance?<b class="req"></b></label>
								<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
										<div class="blue-radio blue-radio-danger">
											<input type="radio" value="Y" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceYes" checked="checked" value="Y">
											<label for="appInterestedSbiLifeInsuranceYes"> Yes </label>
										</div>
										<div class="blue-radio blue-radio-danger">
											<input type="radio" value="N" name="appForm.appInterestedSbiLifeInsurance" id="appInterestedSbiLifeInsuranceNo" >
											<label for="appInterestedSbiLifeInsuranceNo"> No </label>
										</div>
								</div>
							</li>
					</s:elseif>
					<li>
				        <label>PAN<span class="req">&nbsp;*</span> (<a href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN" target="_blank">Verify your PAN</a>)</label>
				        <s:textfield placeholder="Enter PAN" name="appForm.appPanCardNo"  id="appPanCardNo" value="%{appForm.appPanCardNo}" maxlength="10"
				        cssClass="form-control disabledFiedls" disabled="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'true':'false'}" />
						<s:if test="%{isDsrPage=='false'}">
							<div class="custom-tooltip">Enter valid PAN</div>
						</s:if>
			     		<div class="sm-chkbox">
			     			<input type="checkbox" id="appPanCardLater" name="appForm.appPanCardLater" 
			     			value="<s:property value="%{appForm.appPanCardLater==true?'true':'false'}"/>" 
			     			<s:property value="%{appForm.appPanCardLater == true && (appForm.appPanCardNo==null || appForm.appPanCardNo=='')?'checked=checked':''}"/> class="blue-css-checkbox">
			     			<label class="label-content" for="appPanCardLater">I would like to submit my PAN later<span class="req">&nbsp;</span></label>
						</div>
			        </li>
			        <li>
						<label>Do you have Aadhaar Number?</label>
						<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appHaveAadhaarNumber" id="haveAadhaarYes" value="1" checked="checked">
								<label for="haveAadhaarYes"> Yes </label>
							</div>
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appHaveAadhaarNumber" id="haveAadhaarNo" value="2" >
								<label for="haveAadhaarNo"> No </label>
							</div>
						</div>
						<div id="aadhaarConfirmation" class="adh-msg"></div>
					</li>
					<%-- <li>      
				        <label>Aadhaar number<span class="req"> </span></label>
				        <s:if test="%{appForm.appSubTypeId==@com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_EKYC}">
							<s:textfield placeholder="Enter aadhaar number" name="appForm.appAadhaarNumber" id="appAadhaarNumber" value="%{appForm.appAadhaarNumber}"   maxlength="12" onkeydown="return M.digit(event);" cssClass="form-control disabledFields" disabled="true" />
						</s:if>
						<s:else>
							<s:textfield placeholder="Enter aadhaar number" name="appForm.appAadhaarNumber" cssClass="form-control" id="appAadhaarNumber" value="%{appForm.appAadhaarNumber}"   maxlength="12" onkeydown="return M.digit(event);" />
						</s:else>
					</li> --%>
					<li>
						<label>Other identity proof<b class="req">&nbsp;</b></label>
						<div class="flat-field">
								<s:select list="#{'0':'Select id proof','165':'Voter Id','166':'Passport','167':'Driving License','160':'Ration Card'}" name="appForm.appOtherId" id="appIdProof" value="%{appForm.appOtherId}" cssClass="form-select" onfocus="customOnFocus(this);"/>
						</div>
					</li>
				<div id="OTHERIDNUMBER" > 
					<s:if test="%{appForm.appOtherId != null && appForm.appOtherId >0)?'':'hide' }">
			            <s:include value="/appNew/loan/educationloan/includes/EiOtherIdProof.jsp"></s:include>
        			</s:if>
				 </div>   
			</ul>
				<div class="clearfix"></div>
				<h3><span class="flt">Co-applicant Details</span></h3>
				<div class="prm-chk-div">
		 			<input type="checkbox" name="appForm.cloneCoapplicantAddress1" id="cloneCoapplicant1Address" value="<s:property value="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"/>"  class="blue-css-checkbox">
					<label class="label-content" for="cloneCoapplicant1Address">Address same as for applicant</label>
			  	</div>
							
			     <ul class="form-section">
			 		<li>
						<label>First name<b class="req">&nbsp;*</b></label>
                        	<s:textfield name="appForm.appCoapplicantFirstName"  id="appCoapplicantFirstName1" value="%{appForm.appCoapplicantFirstName}"   maxlength="20" 
                             cssClass="form-control"/>
					</li>
					<li>
						 <label>Middle name</label>
						  <s:textfield name="appForm.appCoapplicantMiddleName"  id="appCoapplicantMiddleName1" value="%{appForm.appCoapplicantMiddleName}"   maxlength="20" 
                           cssClass="form-control"/>
					</li>
					<li>
						<label>Last name<b class="req">&nbsp;*</b></label>
	                        <s:textfield name="appForm.appCoapplicantLastName"  id="appCoapplicantLastName" value="%{appForm.appCoapplicantLastName}"   maxlength="20" 
	                        cssClass="form-control"/>
					</li>
					<s:if test="%{quote.loanQuoteLoanPurposeId!=null && quote.loanQuoteLoanPurposeId==24}">
	                     <li>
	               			<label>Gender<b class="req">&nbsp;*</b></label>
	               				<div class="flat-field">
	                				<s:select list="%{#genderList}"  name="appForm.appGender" id="appGender" value="%{appForm.appGender}" headerKey="0" cssClass="form-select" headerValue="Select gender" onfocus="customOnFocus(this);" />
	            				</div>
	            		</li>
                        <li>
                        	<label>Relationship with applicant<b class="req">&nbsp;*</b></label>
                           	<div class="flat-field">
                            	<s:select list="%{beanList.relationships}"  name="appForm.appCoapplicantRelationTypeId"  value="%{appForm.appCoapplicantRelationTypeId}"
								id="appCoapplicantRelationshipId" headerKey="0" headerValue="Select relationship" autocomplete="off" cssClass="form-select" onfocus="customOnFocus(this);" /> 
                        	</div>
                        </li>
                    </s:if>
					
				    <li>
	                  <label>Address line 1<b class="req">&nbsp;*</b></label>
	                  	<s:textfield name="appForm.appCoapplicantAddress1"  id="appCoapplicant1Address1" value="%{appForm.appCoapplicantAddress1}"   maxlength="40" 
	                  		cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
	                   		onkeydown="return M.isAddressLine(event);" />
                     </li>
					
					<li>
	                    <label>Address line 2<b class="req">&nbsp;</b></label>
	                    	<s:textfield name="appForm.appCoapplicantAddress2"  id="appCoapplicant1Address2" value="%{appForm.appCoapplicantAddress2}"   maxlength="40"
	                    	cssClass=" form-control  %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
	                    	 onkeydown="return M.isAddressLine(event);" />
                    </li>
                    
					<li>
                   	 <label>Landmark<b class="req">&nbsp;</b></label>
                    	<s:textfield name="appForm.appCoapplicantLandmark"  id="appCoapplicant1Landmark" value="%{appForm.appCoapplicantLandmark}"   maxlength="30"
                    	cssClass="form-control %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"
                    	 onkeydown="return M.isAddressLine(event);" />
                     </li>
                      <li id="LOCATIONCOAPPLICANTSTATE" >
                       	<label>State<b class="req">&nbsp;*</b></label>
                        	<div class="flat-field">
                               <s:select name="appForm.appCoapplicantStateId"  list="%{beanList.states}" id="appCoapplicant1StateId"
                                value="%{appForm.appCoapplicantStateId}" headerKey="0" headerValue="Select state"  
                                cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onfocus="customOnFocus(this);"/>  
							</div>
					 </li>
					 <div id="LOCATIONCOAPPLICANTCITY"  >
                      	<s:include value="/appNew/loan/educationloan/includes/ScholarCoApplicantCity.jsp"></s:include>
					</div>
					<div id="LOCATIONCOAPPLICANTDISTRICT" >
						<s:if test="%{appForm.appCoapplicantDistrictId != null && appForm.appCoapplicantDistrictId >0 && appForm.appCoapplicantCityId == 9999999}">
                        	<li>
	                        	<label>District<b class="req">&nbsp;*</b></label>
		                           	<s:if test="%{beanList.districtsCoapplicant1!=null}">
		                               	<div class="flat-field">
	                                		<s:select list="%{beanList.districtsCoapplicant1}" name="appForm.appCoapplicantDistrictId" id="appCoapplicant1DistrictId"
	                                  			value="%{appForm.appCoapplicantDistrictId}"  headerKey="0" headerValue="Select district" onfocus="customOnFocus(this);"
	                                  			cssClass="form-select  %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}">
	                        				</s:select>
		                           		</div>
		                              </s:if>
		                              <s:else>
	                            		<div class="flat-field">
	                            			<s:select cssClass="form-select " list="#{''}" name="appForm.appCoapplicantDistrictId" id="appCoapplicant1DistrictId" onfocus="customOnFocus(this);"
	                              				value="%{appForm.appCoapplicantDistrictId}"  headerKey="0" headerValue="Select district" disabled="true">
	                    					</s:select>
	                    				</div>
		                              </s:else>
                        	</li>
						</s:if>
					</div>
					 <li>
                       <label>Pincode<b class="req">&nbsp;*</b></label>
                       <s:textfield  value="%{appForm.appCoapplicantPincode}" id="appCoapplicant1Pincode" name="appForm.appCoapplicantPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);"
                        cssClass="form-control  %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}"/>

					</li>
					<li>
                       <label>Mobile no<b class="req">&nbsp;*</b></label>
                       <s:textfield name="appForm.appCoapplicantPhone" id="appCoapplicantPhone" cssClass="form-control" value="%{appForm.appCoapplicantPhone}" maxlength="10"  onkeydown="return M.digit(event);" />
					</li>
					
		<%--start code  for alternate mobile by hakeem on 29-sep - 22--%>
		<li>
            <s:hidden id="first_mob"	value="%{#session.mobile}" />
            <div>
            <div id="alternateMobile" class="alternateMobile" name ="alternateMobile">
               <!--  <li> -->
                    <label>Alternate Mobile Number <b class="req"></b></label>                         
                    <s:textfield name="appForm.appAltISDCode"  id="appAltISDCode"  value="%{appForm.appAltISDCode}" cssClass="s-input flt form-control" autocomplete="off"  minlength="1" maxlength="3"  onkeyup= "return showlink();" onkeydown="return M.digit(event);" placeholder="ISD code"  />

               <s:textfield name="appForm.appAlternateMobileNumber" id="alternateMobileNumber" value="%{appForm.appAlternateMobileNumber}" autocomplete="off" maxlength="15" placeholder="Alternate Mobile Number" onkeyup= "return showlink();" onkeydown="return M.digit(event);" cssClass="b-input ml10 flt form-control" disabled=""  />
               <div class="custom-tooltip">Note:This additional mobile no will not be updated in CBS. Please input correct alternate mobile number as you are going to receive OTP to validate this mobile number</div>
           		
					<br>
					<br>
					<br>
					<br>
					<br>
					<br>
					
					<span id="inalterror" style="color:#e96e57; font-size:11px"></span>
          
           
               <!--  </li> -->
            </div> 
            <div id="alternateUrl" class="alternateUrl" name ="alternateUrl" style="display:none;">
                    <%--    <b> <a href="javascript:void(0);" onclick="javascript:openPopups('OTP1','1');  return hideAlternateDiv(); ">Click here to verify Otp</a></b>--%>
                        <b> <a href="javascript:void(0);" onclick="validatISD();">Click here to verify OTP</a></b>
                        <s:hidden id="alternateUrlVal" name="alternateUrlVal" value="0"/>
             </div>
            </div>
            </li>
			<!-- Added by Pratima --> 
			</br>
			<li>
			</li>
			<!-- Added by Pratima --> 
			
            <!--   <li>
                <br>
                <div id="alternateUrl" class="alternateUrl" name ="alternateUrl" style="display:none;">
                    <%--    <b> <a href="javascript:void(0);" onclick="javascript:openPopups('OTP1','1');  return hideAlternateDiv(); ">Click here to verify Otp</a></b>--%>
                        <b> <a href="javascript:void(0);" onclick="validatISD();">Click here to verify OTP</a></b>
                        <s:hidden id="alternateUrlVal" name="alternateUrlVal" value="0"/>
                </div>
              
             </li> -->
			 

        <%--end code for alternate mobile number --%>				
					
					<li>
                      <label>Employment type<b class="req">&nbsp;*</b></label>
	                      <s:if test="%{(appForm.appCoapplicantEmploymentTypeId==22 && quote.loanQuoteisSbiEmployee.equalsIgnoreCase('Y'))}">
	                      	<div class="flat-field">
		                      	<s:select cssClass="form-select disabledFields " name="appForm.appCoapplicantEmploymentTypeId" 
		                        list="%{beanList.employementTypesCoapplicants}"
		                        id="appCoapplicantEmploymentTypeId" value="%{appForm.appCoapplicantEmploymentTypeId}"
		                        headerKey="0" headerValue="Select employment " disabled="true"  onfocus="customOnFocus(this);"/>
	                        </div>
	                      </s:if>
                      <s:else>
                      	<div class="flat-field">
	                      	<s:select cssClass="form-select disabledFields " name="appForm.appCoapplicantEmploymentTypeId" 
	                        list="%{beanList.employementTypesCoapplicants}"
	                        id="appCoapplicantEmploymentTypeId" value="%{appForm.appCoapplicantEmploymentTypeId}"
	                        headerKey="0" headerValue="Select Employment " onfocus="customOnFocus(this);"  />
                        </div>
                      </s:else>
					</li>
					<li>
	                   <label>Net annual income<b class="req">&nbsp;*</b></label>
	                   <s:textfield name="appForm.appCoapplicantNmiAndNai"  id="appCoapplicantNmiAndNai" cssClass="form-control" value="%{appForm.appCoapplicantNmiAndNai}" placeholder="Rs"  maxlength="10"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" onfocus="this.value=M.unformatMoney(this.value);"/>
	    	        </li>
	    	        
           	        <s:if test="%{quote.loanQuoteLoanType==3 && quote.loanQuoteLoanAmount >4 && quote.loanQuoteLoanAmount <= 7.5 }">
            	    	<li>
                           <label>Type Of collateral offered for security<b class="req">&nbsp;*</b></label>
                           <div class="flat-field">
                           		<s:select  name="appForm.appCoapplicantCollateral"  id="appCoapplicantCollateral" value="%{appForm.appCoapplicantCollateral}" 
                            		list="#{1:'Flat/House',2:'Non-Agri land',3:'Fixed Deposits'}"
                            		headerKey="0" headerValue="Select collateral" onfocus="customOnFocus(this);"/> 
                           </div>
            	        </li>
            	        <li>
                           <label>Market value of collateral <b class="req">&nbsp;*</b></label>
                           		<s:textfield name="appForm.appCoapplicantMarketValueOfCollateral"  id="appCoapplicantMarketValueOfCollateral"
									value="%{appForm.appCoapplicantMarketValueOfCollateral>0?appForm.appCoapplicantMarketValueOfCollateral:''}" cssClass="form-control"
	 								placeholder="Rs"  maxlength="10"  onkeydown="return M.digit(event);" onblur="var tm=M.formatAsMoneyStr(this.value);this.value=tm?tm:'';" 
	 								onfocus="this.value=M.unformatMoney(this.value);"/>
            	        </li>
           	         </s:if>
		             <li>
		                 <label>PAN<span class="req">&nbsp;*</span> (<a href="https://eportal.incometax.gov.in/iec/foservices/#/pre-login/verifyYourPAN" target="_blank">Verify your PAN</a>)</label>
			        		<s:textfield placeholder="Enter PAN" name="appForm.appCoapplicantPanCardNo"  id="appCoapplicantPanCardNo" value="%{appForm.appCoapplicantPanCardNo}" maxlength="10"
			                     class="form-control"  disabled="%{appForm.appCoapplicantPanCardLater == true && (appForm.appCoapplicantPanCardNo==null || appForm.appCoapplicantPanCardNo=='')?'true':'false'}" />
						
						 <div class="sm-chkbox">
							<input type="checkbox" id="appCoapplicantPanCardLater" name="appForm.appCoapplicantPanCardLater" value="<s:property value="%{appForm.appCoapplicantPanCardLater!=null?'true':'false'}"/>" 
							<s:property value="%{appForm.appCoapplicantPanCardLater == true && (appForm.appCoapplicantPanCardLater==null || appForm.appCoapplicantPanCardLater=='')?'checked=checked':''}"/> class="blue-css-checkbox">
							<label class="label-content" for="appCoapplicantPanCardLater">I would like to submit my PAN later<span class="req">&nbsp;</span></label>
						</div>
	            	</li>
	            	 <li>
						<label>Do you have Aadhaar Number?</label>
						<div class="col-xs-12 mrgt-10-10 mrgt-left-7">
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appCoAppHaveAadhaarNumber" id="coAppHaveAadhaarYes" value="1"  checked="checked">
								<label for="coAppHaveAadhaarYes"> Yes </label>
							</div>
							<div class="blue-radio blue-radio-danger">
								<input type="radio" name="appForm.appCoAppHaveAadhaarNumber" id="coAppHaveAadhaarNo" value="2" >
								<label for="coAppHaveAadhaarNo"> No </label>
							</div>
						</div>
						<div id="coAadhaarConfirmation" class="adh-msg"></div>
					</li>
					<%-- <li>
	                    <label>Aadhaar number<b class="req">&nbsp; </b></label>
	                    <s:textfield placeholder="Enter aadhaar number" name="appForm.appCoapplicantAadhaarNumber"  id="appCoapplicantAadhaarNumber" value="%{appForm.appCoapplicantAadhaarNumber}"  cssClass="form-control" maxlength="12" onkeydown="return M.digit(event);" />
					</li> --%>
					<li>
						<label>Other identity proof<b class="req">&nbsp;</b></label>
						<div class="flat-field">
							<s:select list="#{'0':'Select Id Proof','165':'Voter Id','166':'Passport','167':'Driving License','160':'Ration Card'}" name="appForm.appCoIdProof" id="appCoIdProof" value="%{appForm.appCoIdProof}" onfocus="customOnFocus(this);" cssClass="form-select"/>
						</div>
					</li>
						<div id="divAppCoOtherIdNumber" > 
							<s:if test="%{(appForm.appCoIdProof!= null && appForm.appCoIdProof >0)?'':'hide' }">
					            <s:include value="/appNew/loan/educationloan/includes/CoAppEiOtherIdProof.jsp"></s:include>
		        			</s:if>
						</div>    
				</ul> 	
           		<div class="clearfix"></div>
				<div id="termsAndConditionThird" class="sbi-trms-div sbi-trms-div-third">
	   			<ul class="form-section">
					<li class="full-width"> 
						<s:if test="%{isDsrPage=='false'}">
							<div class="trms-section">
								<input type="checkbox" class="blue-css-checkbox" name="terms_conditions_check" id="terms_conditions_check"  value="on">
					 			<label for="terms_conditions_check" class="label-content">I have read the Terms &amp; Conditions and agree to the terms therein, I also authorise the Bank and/or its representatives to verify any information contained in the application or otherwise from any source whatsoever at their sole discretion at your office/residence and/or contact you and/or your family members and/or your Employer/Banker/Credit Bureau/UIDAI/RBI and/or any third party as they deem necessary.<b class="req">*</b></label>
			      			</div>
						</s:if>
						<s:if test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_CBS }">
							<div class="flr mrgt-10">
								<input  type="submit" class="submit-btn" name="submitApplication" id="submitApplication" value="Submit Application">
							</div>
						</s:if>
						<s:elseif test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_EKYC }">
							<div class="flr mrgt-10">
								<input  type="submit" class="submit-btn" name="submitApplication" id="submitApplication" value="Submit Application">
							</div>
                      </s:elseif>
                    <s:elseif test="%{appForm.appSubTypeId!=null && appForm.appSubTypeId == @com.mintstreet.common.util.Constants@APP_APP_SUB_TYPE_ID_NORMAL }"> 
                     	<div class="flr mrgt-10">
							<input  type="submit" class="submit-btn" name="submitApplication" id="submitApplication" value="Submit Application">
						</div>
                   </s:elseif>
				</li>    
			</ul>	
		</div>
	</form>

<!-- start code for pop up by hakeem on 29-sep-22-->
<div class="modal otp-box" id="OTP1" tabindex="-1" role="dialog" 
  aria-labelledby="myModalLabel">
			  <div class="modal-dialog" role="document">
			<div class="modal-content" >
				
				  <div class="modal-body">
				  <button type="button"  onclick="showAlternateDiv();" class="close" 
          data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true"><%-- <img 
            src="<s:property value="@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI"/> alt=""/> --%></span>
            </button> 
				<div class="verify-otp-left-section verify-mobile-otp">
					</br>
					</br>
					<h2>
						<span>Verify</span> your mobile
					</h2>
						<p>(Please enter your contact details for OTP process)</p>
				</div>
					<form name="otpApplicationAlt" id="otpApplicationAlt" method="post" action="javascript:void(0);" autocomplete="off">
						<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
						<div class="otp-right-section">
						</br>
						</br>
							<ul class="otp-form">
							<div id="privacyStatementHTML" class="privacyStatementHTML"  name="privacyStatementHTML"> 	
							<div id="otpPopHome">
							<!-- <div id="otpPopHome" name="otpPopHome" id="otpPopHome"> -->
								

									<li><s:hidden id="appOTPVerifyType"
											name="appOTPVerifyType" value="0" /> <s:hidden
											id="appApplyingFrom" name="appApplyingFrom" value="1" /> <label>Enter
											your Alternate mobile no. <b class="req">*</b>
									</label> <!--start code for alternate mobile number by hakeem on 20 sep 22 -->
										<s:hidden name="isdCodeAlt" id="isdCodeAlt" value="91"
											cssClass="s-input flt form-control" autocomplete="off"
											maxlength="3" placeholder="91"
											onkeydown="return M.digit(event);" /> <s:textfield
											id="alternateMobileNumber2" name="alternateMobileNumber"
											maxlength="16" value="" placeholder="Alternate Mobile no."
											onkeydown="return M.digit(event);" cssClass="form-control"
											autocomplete="off" readonly='true' /></li>
                                 
								
								
						
								
							</div>
							</div>
							
							<div id="otp_verify_type" style="display: none;">
									<li><label>OTP verify by?<b class="req">*</b></label>
										<div class="col-xs-12 mrgt-9">
											<div class="blue-radio blue-radio-danger">
												<input type="radio" id="appOTPVerifyTypeYes" value="0" 
                          name="appOTPVerifyType" checked="checked" ><label 
                          for="appOTPVerifyTypeYes">Mobile</label>
											</div>	
											<div class="blue-radio blue-radio-danger">
												<input type="radio" id="appOTPVerifyTypeNo" value="1" 
                        name="appOTPVerifyType"><label 
                        for="appOTPVerifyTypeNo">Email</label>
											</div>
										</div></li>
								</div>
                
                <li><br>
							
								<div id="changeAlternateNum" class="alternateUrl"
									name="changeAlternateNum">
									<%--commented by hakeem on 23 sep 22--%>

								</div></li>
									<div id="overlay-row_otp_hl">
										<li>
											<button  class="track-btn conf-track-btn" id="sendOptAlt" 
                      name="sendOptAlt" type="submit" 
                      onclick="setNewAlternateMobile();">Send OTP </button>
										</li>
									</div>
									<div id="OtpPopHl" class="OtpPopHl" style="display: none;">
					    			<div id="otp_row_confirm" >
						    			<li id="otp_row_confirm_html"><label><span 
                      id="otpLabel">Enter code sent by SMS<b class="req">
                       *</b></span></label><input id="inputOtpAlt" name="inputOtpAlt" maxlength="6" type="text" 
                       class="form-control secure-otp"  
                       onkeydown="return M.digit(event);" autocomplete="off">
                       <input id="inputOtpAlt1" name="inputOtpAlt" type="hidden"
										class="form-control secure-otp" 
										onkeydown="return M.digit(event);" autocomplete="off">
                       
						    			</li>
									<li></li>
										
						    			<li>
							    			<button  class="track-btn conf-track-btn" id="confirmOtpAlt" 
                        name="confirmOtpAlt" type="submit">Confirm</button>
                        <button  class="resend-btn" id="resendOtpAlt" 
                        name="resendOtpAlt" type="submit">Resend</button>
						    			</li>
						    		</div>
						    	</div>
								<!-- <div class="clear"></div>
								<li id="errorOTPMsg" class="error-msg-cbs" style="display:none;"></li> -->
							</ul>
							<div class="clear"></div>
              <li id="errorOTPMsg1" class="" style="display:none;"></li> <span
                class="otp-loader"><img id="opt-loader-application" 
                style="display:none;"
                src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-loader-small.gif"/></span>
						<!-- <div id="errorOTPMsg" style="display: none;"></div> -->
						</div>
				  	</form>
				  </div>
				</div>
			  </div>
			</div>
			<!-- </div> -->
			
	<!-- end	 -->																			



<div id="htmlappPermanentCityId" class="hide">
	 <s:include value="/appNew/loan/educationloan/includes/ScholarCity.jsp"></s:include>
</div>

 <div id="htmlappPermanentDistrictId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/ApplicantPermanentDistrict.jsp"></s:include>
</div>

<div id="htmlappCoapplicant1CityId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/ScholarCoApplicantCity.jsp"></s:include>
</div>
 <div id="htmlappCoapplicant1DistrictId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/CoApplicantPermanentDistrict.jsp"></s:include>
</div>

 <div id="htmlappEdvantageCityId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/edvantageCity.jsp"></s:include>
</div>
 <div id="htmlappEdvantageDistrictId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/edvantageDistrict.jsp"></s:include>
</div>
<div id="htmlappEdvantageBranchId" class="hide">
	<s:include value="/appNew/loan/educationloan/includes/edvantageBranch.jsp"></s:include>
</div>
<div id="htmlappOtherIdNumber" class="hide" >
    <s:include value="/appNew/loan/educationloan/includes/EiOtherIdProof.jsp"></s:include>
</div>
<div id="htmlAppCoOtherIdNumber" class="hide" >
    <s:include value="/appNew/loan/educationloan/includes/CoAppEiOtherIdProof.jsp"></s:include>
</div>   
<s:set var="minFolderPath" value="%{''}"/>
<s:if test="%{#SCRIPT_COMPRESSION.equalsIgnoreCase('true')}">
	<s:set var="minFolderPath" value="%{'/min'}"/>		
</s:if>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/educationloan/js<s:property value="#minFolderPath"/>/jquery.education_loan_third_page.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<script type="text/javascript"
	src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/app/loan/homeloan/js<s:property value="#minFolderPath"/>/jquery.otherLoanCommonOTP.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>

<script type="text/javascript">
function fetchJsonData(){
	 
	jsonJSArray = '<s:property value="%{@com.mintstreet.loan.educationloan.action.EducationLoanAction@jsonJSArray3EducationLoan}"/>';
	jsonJSArray = jsonJSArray.replace(/%27/g, "\'");
	jsonJSArray = jsonJSArray.replace(/&quot;/g, "\"");
	jsonObject = jQuery.parseJSON(jsonJSArray);
	return jsonObject;
}

$(document).ready(function(){
	$("#content-3").mCustomScrollbar({
		theme:"rounded",
		scrollInertia:5,
		callbacks:{
            onScroll:function(){ 
                $("#third_date_of_birth").datepicker('place');
             }
		},
		mouseWheel:{scrollAmount:30},
		scrollButtons:{
			enable:true
		},
		 advanced:{
			updateOnBrowserResize:true,
			updateOnContentResize:true,
			autoScrollOnFocus:true,
			autoExpanHorizontalScroll:true 
		}
		
	});

dob_date_picker('educationloancriteriaform', 'third_date_of_birth');
});
	<!-- start code alternate mobile number for validation by hakeeem 29-sep- 22 -->

    function showlink(){

 
      		console.log('showlink called'); 
		var mob_no_len = document.getElementById('alternateMobileNumber').value;
	     var alt_ISD = document.getElementById('appAltISDCode').value;
     var fisrt_mob = $('#first_mob').val();
	 var pattern1 =  /^[1233456789][0-9]{7,15}$/;
		 var pattern =  /^[1233456789][0-9]{0,2}$/;
    if ((mob_no_len.length > 7 && mob_no_len.length <  16 )&& (pattern.test(alt_ISD) && pattern1.test(mob_no_len))) {
        	$("#inalterror").html("");
    	if(fisrt_mob==mob_no_len)
		{
			$('#inalterror').text('Alternate Mobile cannot be same as Mobile number.');
			document.getElementById("alternateUrl").style.display = "none";    			
		}
		else{
			document.getElementById("alternateUrl").style.display = "block";
		}  
			
		} else {
			 document.getElementById("alternateUrl").style.display = "none";
                    
						
      if (mob_no_len.startsWith("0")  || alt_ISD.startsWith("0")){
				  $("#inalterror").html(" Number should not start with 0"); 
				  //dfd
			 } else  if((mob_no_len.length == 0  &&  alt_ISD.length != 0 ) || (mob_no_len.length != 0  &&  alt_ISD.length == 0 )) {
					 $("#inalterror").html("Provide  mobile and isd together ");
				 } else if(mob_no_len.length < 8  && mob_no_len.length != 0) {
				   $("#inalterror").html("provide valid mobile number ");
			 } else {
				    $("#inalterror").html(""); 
			   }
			        
		}  
           
	}
   function validatISD(){
    	
    	var alt_ISD = document.getElementById('appAltISDCode').value;
		
		
    	if(alt_ISD.length >0 && alt_ISD.length < 4 ) {
			$("#inalterror").html("");
			$("#alternateMobileNumber2").val($("#alternateMobileNumber").val());
	   	    $("#isdCodeAlt").val($("#appAltISDCode").val());	
			openPopups('OTP1','1');
			hideAlternateDiv();
			
		
	       
    	} else {
	    	$("#inalterror").html("Please enter valid ISD Code for alternate mobile number");
    	}
    }

  function hideAlternateDiv () {
	 
        document.getElementById('alternateMobileNumber').disabled = true;
        document.getElementById('appAltISDCode').disabled = true;
        document.getElementById("alternateUrl").style.display = "none";

        }
  function showAlternateDiv () {
	 
        document.getElementById('alternateMobileNumber').disabled = false;
        document.getElementById('appAltISDCode').disabled = false;
        document.getElementById("alternateUrl").style.display = "block";

        }
  
  function changeAlternateMobile () {
	  $('#alternateMobileNumber2').attr('readonly', false);
  }
  
  function setNewAlternateMobile () {
	
	  $("#alternateMobileNumber").val($("#alternateMobileNumber2").val());
  }


</script>
<!-- End -->










