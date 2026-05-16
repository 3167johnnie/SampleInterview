<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<li>
	<label class="">City<span class="req"><s:property escapeHtml="false" value="%{@com.mintstreet.common.util.Constants@BANK_ID==@com.mintstreet.common.util.Constants@BANK_ID_SBI?'':'&nbsp;*'}"/></span></label>
	<s:if test="%{appForm.appCoapplicantCity_id_2!=null && appForm.appCoapplicantState_id_2!=null}">
		<div class="flat-field">
			<s:select list="%{beanList.citiesoptgrp1Coapplicant2==null?'':beanList.citiesoptgrp1Coapplicant2}" name="appForm.appCoapplicantCity_id_2" id="appCoapplicant2CityId" 
			value="%{appForm.appCoapplicantCity_id_2}" headerKey="0" headerValue="Select city" 
			cssClass="form-select %{appForm.cloneCoapplicantAddress2==true?'disabledFields':'disabledFields'}" disabled="%{appForm.cloneCoapplicantAddress2==true?'true':'false'}" onfocus="customOnFocus(this);">
		</s:select>
		</div>
	</s:if>
	<s:else>
		<div class="flat-field">
		<s:select list="#{''}" name="appForm.appCoapplicantCity_id_2" id="appCoapplicant2CityId" value="%{appForm.appCoapplicantCity_id_2}" 
			headerKey="0" headerValue="Select city" cssClass="form-select" disabled="true" onfocus="customOnFocus(this);">
			</s:select>
		</div>
	</s:else>
</li>
