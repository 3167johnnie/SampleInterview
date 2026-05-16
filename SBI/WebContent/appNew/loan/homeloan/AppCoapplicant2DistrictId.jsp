<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">District<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	<div class="flat-field">
		<s:select name="appForm.appCoapplicantDistrictId2" id="appCoapplicant2DistrictId" autocomplete="off" 
		list="%{beanList.districtsCoapplicant2!=null?beanList.districtsCoapplicant2:''}" value="%{appForm.appCoapplicantDistrictId2}" 
		headerKey="0" headerValue="Select district" cssClass="form-select %{appForm.cloneCoapplicantAddress2==true?'disabledFields':'disabledFields'} " disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onfocus="customOnFocus(this);"/>
	</div>
</li>
