<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">District<span class="req">*</span></label>
	<div class="flat-field">
		<s:select name="appForm.appOfficeDistrictId" id="appOfficeDistrictId" autocomplete="off" list="%{beanList.districtsOffice!=null?beanList.districtsOffice:''}"
		value="%{appForm.appOfficeDistrictId}" headerKey="0" headerValue="Select district" cssClass="form-select" onfocus="customOnFocus(this);"/>
	</div>
</li>
