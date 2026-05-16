<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<s:if test="%{appForm.appPropertyCityId!=null && appForm.appPropertyCityId>0}">
			<label>City<b class="req">*</b></label>
			<div class="flat-field">  
				<s:select list="%{beanList.citiesoptgrp1PropertyPlace==null?'':beanList.citiesoptgrp1PropertyPlace}" name="appForm.appPropertyCityId" 
					id="appPropertyCityId" value="%{appForm.appPropertyCityId}" headerKey="0" headerValue="Select city" disabled="%{#isDisabledPropertyState}" 
					cssClass="form-select %{#isDisabledPropertyState==true?'':''}" onfocus="customOnFocus(this);">
				</s:select>
			</div>
	</s:if>
	<s:else>
			<label>City<b class="req">*</b></label>
			<div class="flat-field">  
				<s:select list="#{''}" name="appForm.appPropertyCityId" id="appPropertyCityId" value="%{appForm.appPropertyCityId}" headerKey="0" headerValue="Select city" cssClass="form-select " disabled="true" onfocus="customOnFocus(this);"/>
			</div>
	</s:else>
</li>
