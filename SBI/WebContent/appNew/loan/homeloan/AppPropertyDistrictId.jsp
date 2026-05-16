<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">District<b class="req">*</b></label>
	<div class="flat-field">  		
		<s:select name="appForm.appPropertyDistrictId" id="appPropertyDistrictId" autocomplete="off" 
		list="%{beanList.districtsPropertyPlace!=null?beanList.districtsPropertyPlace:''}" cssClass="form-select %{#isDisabledPropertyState==true?'':''}" disabled="%{#isDisabledPropertyState}"
		value="%{appForm.appPropertyDistrictId}" headerKey="0" headerValue="Select district" onfocus="customOnFocus(this);"/>
	</div>
</li>    
