<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
    <label>District<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	<div class="flat-field"> 
		<s:select name="appForm.appPermanentDistrictId"  list="%{beanList.districtsPermanent!=null?beanList.districtsPermanent:''}" id="appPermanentDistrictId" value="%{appForm.appPermanentDistrictId}"
		headerKey="0" headerValue="Select district" cssClass=" form-select %{appForm.clonePermanentAddress==true?'diasabledFields':''}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onfocus="customOnFocus(this);"/>  
	</div>
</li>
