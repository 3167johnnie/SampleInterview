<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li id="divLocality">
	<label class="">District<b class="req">*</b></label>
		<div class="flat-field">
			<s:select name="appForm.appDistrictId" id="appDistrictId" autocomplete="off" list="%{beanList.districts!=null?beanList.districts:''}" cssClass="form-select"
			 value="%{appForm.appDistrictId}"     headerKey="0" headerValue="Select district"/>
        </div>			
</li>
