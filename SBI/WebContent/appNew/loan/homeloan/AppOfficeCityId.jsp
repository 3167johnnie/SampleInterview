<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">City<span class="req">*</span></label>
	<s:if test="%{appForm.appOfficeCityId!=null && appForm.appOfficeCityId>0}">
		<div class="flat-field">
			<s:select list="%{beanList.citiesoptgrp1Office == null?'':beanList.citiesoptgrp1Office}" name="appForm.appOfficeCityId" id="appOfficeCityId" cssClass="form-select" value="%{appForm.appOfficeCityId}" headerKey="0" headerValue="Select city" onfocus="customOnFocus(this);"></s:select>
		</div>
	</s:if>
	<s:else>
		<div class="flat-field">
			<s:select list="#{''}" name="appForm.appOfficeCityId" id="appOfficeCityId" value="%{appForm.appOfficeCityId}" headerKey="0" headerValue="Select city" cssClass="form-select" disabled="true" onfocus="customOnFocus(this);"/>
		</div>
	</s:else>
</li>
