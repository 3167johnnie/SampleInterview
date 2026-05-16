<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="docPickupCheck1Html" style="display: none;">
	<li>
		<label >Address line 1<span class="req">*</span></label>
		<s:textfield name="appForm.appPickupAddress1" value="%{#appForm.appAddress1}" id="appAddress1"  cssClass="form-control disabledFields " disabled="true" maxlength="16"/>
	</li>
	
	<li>
		<label >Address line 2<span class="req">&nbsp;</span></label>
		<s:textfield name="appForm.appPickupAddress2" value="%{#appForm.appAddress2}" id="appAddress2"  cssClass="form-control disabledFields " disabled="true" maxlength="16"/>
	</li>
	<li>
		<label >State<span class="req">*</span></label>
		<div class="flat-field">
			<s:select list="beanList.states" value="%{#appForm.appStateId}" id="appStateId" name="appForm.appPickupStateId" 
			 headerKey="0" headerValue="Select state" disabled="true"  cssClass="form-select disabledFields " onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<li>
		<label >City<span class="req">*</span></label>
		<div class="flat-field">
			<s:select list="%{beanList.citiesoptgrp1!=null?beanList.citiesoptgrp1:''}" value="%{#appForm.appCityId}"  id="appCityId" name="appForm.appPickupCityId" 
			headerKey="0" headerValue="Select city" disabled="true" cssClass="form-select disabledFields " onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<div id="APPLOCATIONDISTRICT">
		<s:if test="%{#appForm.appCityId!=null && #appForm.appCityId==9999999}">
			<li>
				<label>District<span class="req">*</span></label>
				<div class="flat-field">
					<s:select list="%{beanList.districts!=null?beanList.districts:''}" value="%{#appForm.appDistrictId}" id="appDistrictId" 
					name="appForm.appPickupDistrictId" headerKey="0" headerValue="Select district" disabled="true" 
					cssClass="disabledFields  form-control" onfocus="customOnFocus(this);"/>			
				</div>
			</li>
		</s:if>
	</div>
	<li>
		<label>Pincode<span class="req">*</span></label>
		<s:textfield  value="%{#appForm.appPincode}" id="appPincode" name="appForm.appPickupPincode" placeholder="Pincode" 
		autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);" disabled="true" cssClass="form-control disabledFields " />
	</li>
</div>
		
	<div id="docPickupCheck2Html" style="display: none;">		
		<li>
			<label >Address line 1<span class="req">*</span></label>
			<s:if test="%{#appForm.appOfficeAddress1 == null ||appForm.appOfficeAddress1 == ''}">
				<s:textfield   name="appForm.appPickupAddress1"  id="appOfficeAddress1"  value="%{#appForm.appOfficeAddress1}" cssClass="form-control" />
			</s:if>
			<s:else>
				<s:textfield   name="appForm.appPickupAddress1"  id="appOfficeAddress1"  value="%{#appForm.appOfficeAddress1}" disabled="true" cssClass="disabledFields form-control" />
			</s:else>
		</li>
		
		<li>
			<label >Address line 2<span class="req">&nbsp;</span></label>
			<s:if test="%{#appForm.appOfficeAddress1 == null}">
				<s:textfield   name="appForm.appPickupAddress2"  id="appOfficeAddress2" value="%{#appForm.appOfficeAddress2}"  cssClass="form-control" />
			</s:if>
			<s:else>
				<s:textfield   name="appForm.appPickupAddress2"  id="appOfficeAddress2" value="%{#appForm.appOfficeAddress2}"  disabled="true" cssClass="disabledFields  form-control"/>
			</s:else>
		</li>
		<li>
			<label >State<span class="req">*</span></label>
			<div class="flat-field">
				<s:if test="%{#appForm.appOfficeStateId != null}">
					<s:select list="beanList.states" name="appForm.appPickupStateId" id="appOfficeStateId"   value="%{#appForm.appOfficeStateId}" 
					 headerKey="0" headerValue="Select state"  disabled="true" cssClass="disabledFields  form-select" onfocus="customOnFocus(this);"></s:select>							
				</s:if>
				<s:else>
					<s:select list="beanList.states" name="appForm.appPickupStateId" id="appOfficeStateId" value="%{#appForm.appOfficeStateId}" 
					headerKey="0" headerValue="Select state" cssClass="form-select" onfocus="customOnFocus(this);"></s:select>
				</s:else>
			</div>
		</li>
		<!-- <div id="APPOFFICELOCATIONCITY"> -->
			<li id="divOfficeCityId">
				<label >City<span class="req">*</span></label>
				<div class="flat-field">
					<s:select list="%{beanList.citiesoptgrp1Office!=null?beanList.citiesoptgrp1Office:''}" 
					name="appForm.appPickupCityId"  id="appOfficeCityId"  value="%{#appForm.appOfficeCityId}" 
					headerKey="0" headerValue="Select city"  disabled="true" cssClass="disabledFields  form-select" onfocus="customOnFocus(this);"/>
				</div>
			</li>
		<!-- </div> -->
		<div id="APPOFFICEPICKUPLOCATIONDISTRICT">
			<s:if test="%{#appForm.appOfficeCityId!=null && #appForm.appOfficeCityId == 9999999}">
				<li>
					<label>District<span class="req">*</span></label>
					<div class="flat-field">
						<s:select list="%{beanList.districtsOffice!=null?beanList.districtsOffice:''}" name="appForm.appPickupDistrictId" 
						id="appOfficeDistrictId" value="%{#appForm.appOfficeDistrictId}" 
						headerKey="0" headerValue="Select district" disabled="true" cssClass="disabledFields  form-select" onfocus="customOnFocus(this);"/>
					</div>
				</li>
			</s:if>
		</div>
		<li>
			<label >Pincode<span class="req">*</span></label>
			<s:if test="%{#appForm.appOfficePincode != null}">
				<s:textfield  value="%{#appForm.appOfficePincode}" id="appOfficePincode" name="appForm.appPickupPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);" disabled="true" cssClass="disabledFields  form-control"/>
			</s:if>
			<s:else>
				<s:textfield  value="%{#appForm.appOfficePincode}" id="appOfficePincode" name="appForm.appPickupPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);" cssClass="form-control" />
			</s:else>
		</li>
	</div>
							
<div id="docPickupCheck3Html" style="display: none;">		
	<li>
		<label >Address line 1<span class="req">*</span></label>
		<s:textfield name="appForm.appPickupAddress1" value="%{#appForm.appPickupAddress1}" id="appPickupAddress1" cssClass="form-control"/>
	</li>
	
	<li>
		<label >Address line 2<span class="req">&nbsp;</span></label>
		<s:textfield name="appForm.appPickupAddress2" value="%{#appForm.appPickupAddress2}" id="appPickupAddress2" cssClass="form-control"/>
	</li>
	<li>
		<label >State<span class="req">*</span></label>
		<div class="flat-field">
			<s:select list="beanList.states" value="%{#appForm.appPickupStateId}" name="appForm.appPickupStateId" id="appPickupStateId" cssClass="form-select"
				headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<li id='APPOTHERPICKUPLOCATIONCITY'>
		<label >City<span class="req">*</span></label>
		<div class="flat-field">
			<s:select list="%{beanList.citiesoptgrp1Pickup!=null?beanList.citiesoptgrp1Pickup:''}" name="appForm.appPickupCityId"  id="appPickupCityId" 
			value="%{#appForm.appPickupCityId}" headerKey="0" headerValue="Select city" cssClass="form-select"  onfocus="customOnFocus(this);"/>
		</div>
	</li>
	<div id="APPOTHERPICKUPLOCATIONDISTRICT">
		<s:if test="%{#appForm.appPickupCityId!=null && #appForm.appPickupCityId == 9999999}">
			<li>
				<label >District<span class="req">*</span></label>
				<div class="flat-field">
					<s:select list="%{beanList.districtPickup==null?'':beanList.districtPickup}" name="appForm.appPickupDistrictId" 
					id="appPickupDistrictId" value="%{#appForm.appPickupDistrictId}" headerKey="0" headerValue="Select district"  
					cssClass="form-select" onfocus="customOnFocus(this);"/>
				</div>
			</li>
		</s:if>
	</div>
	<li>
		<label >Pincode<span class="req">*</span></label>
		 <s:textfield value="%{#appForm.appPickupPincode}" name="appForm.appPickupPincode" id="appPickupPincode" placeholder="Pincode" autocomplete="off"  maxlength="6" onkeydown="return M.digit(event);" cssClass="form-control"/> 
	</li>
</div>

<div id="htmlappPickupDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppPickupDistrictId.jsp"></s:include>
</div>
<div id="htmlappDistrictId" class="hide">
	<s:include value="/appNew/loan/homeloan/AppDistrictId.jsp"></s:include>
</div>
<div id="htmlappOfficeDistrictId" class="hide">
	<li>
		<label>District<span class="req">*</span></label>
		<div class="flat-field">
			<s:select list="%{beanList.districtsOffice!=null?beanList.districtsOffice:''}" name="appForm.appPickupDistrictId" id="appOfficeDistrictId" value="%{#appForm.appOfficeDistrictId}" 
			headerKey="0" headerValue="Select district" disabled="true" cssClass="disabledFields  form-select" onfocus="customOnFocus(this);"/>
		</div>
	</li>
</div>

<div id="docPickupCheck4Html" style="display: none;">		
</div>
