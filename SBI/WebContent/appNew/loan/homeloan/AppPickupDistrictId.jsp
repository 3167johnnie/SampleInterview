<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label >District<span class="req">*</span></label>
	<div class="flat-field">
		<s:select list="%{beanList.districtPickup==null?'':beanList.districtPickup}" name="appForm.appPickupDistrictId" id="appPickupDistrictId" value="%{appForm.appPickupDistrictId}" headerKey="0" headerValue="Select district" disabled="true" cssClass="disabledFields form-select" onfocus="customOnFocus(this);"/>
	</div>
</li>
