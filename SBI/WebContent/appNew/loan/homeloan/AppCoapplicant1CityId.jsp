<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">City<b class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></b></label>
	<s:if test="%{appForm.appCoapplicantCity_id_1!=null}">
	<div class="flat-field">
		<s:select list="%{beanList.citiesoptgrp1Coapplicant1==null?'':beanList.citiesoptgrp1Coapplicant1}" name="appForm.appCoapplicantCity_id_1" id="appCoapplicant1CityId"
		value="%{appForm.appCoapplicantCity_id_1}" headerKey="0" headerValue="Select city"
		cssClass="form-select %{appForm.cloneCoapplicantAddress1==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress1==true?'true':'false'}" onfocus="customOnFocus(this);">
		</s:select>
	</div>
	</s:if>
	<s:else>
		<div class="flat-field">
			<s:select list="#{''}" name="appForm.appCoapplicantCity_id_1" id="appCoapplicant1CityId" value="%{appForm.appCoapplicantCity_id_1}"
			headerKey="0" headerValue="Select city" cssClass="form-select" disabled="true" onfocus="customOnFocus(this);"/>
		</div>
	</s:else>
</li>
