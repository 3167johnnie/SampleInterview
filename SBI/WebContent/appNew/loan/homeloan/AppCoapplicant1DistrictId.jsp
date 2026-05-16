<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">District<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	<div class="flat-field">
		<s:select name="appForm.appCoapplicantDistrictId1" id="appCoapplicant1DistrictId" autocomplete="off"
		list="%{beanList.districtsCoapplicant1!=null?beanList.districtsCoapplicant1:''}" value="%{appForm.appCoapplicantDistrictId1}" 
		headerKey="0" headerValue="Select district"
		cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'} " disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onfocus="customOnFocus(this);"/>
	 </div>
</li>
