<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>    
<li>
	<label>City<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b> </label>
	<s:if test="%{appForm.appPermanentCityId!=null && appForm.appPermanentCityId>0}">
		<div class="flat-field">
			<s:select list="%{beanList.citiesoptgrp1Permanent==null?'':beanList.citiesoptgrp1Permanent}" name="appForm.appPermanentCityId" id="appPermanentCityId" value="%{appForm.appPermanentCityId}" headerKey="0" headerValue="Select city" 
			cssClass="form-select %{appForm.clonePermanentAddress==true?'disabledFields':'disabledFields'}" disabled="%{appForm.clonePermanentAddress==true?'true':'false'}" onfocus="customOnFocus(this);">
		</s:select>
		</div>
	</s:if>
	<s:else>
		<div class="flat-field">
			<s:select cssClass="form-select" list="#{''}" name="appForm.appPermanentCityId" id="appPermanentCityId" value="%{appForm.appPermanentCityId}"  headerKey="0" headerValue="Select city" disabled="true"  onfocus="customOnFocus(this);"/>
		</div>
	</s:else>
</li>
