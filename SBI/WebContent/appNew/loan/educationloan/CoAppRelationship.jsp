<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<li id="466">
		<label class="">Relationship<b class="req">*</b></label>
			<div class="flat-field ">
				<s:select name="quote.loanQuoteCoapplicantFirstRelationshipId" id="coapplicantFirstRelationshipId" autocomplete="off" list="%{beanList.relationships}" cssClass="form-select"
			 	value="%{quote.loanQuoteCoapplicantFirstRelationshipId}" headerKey="0" headerValue="Select relationship" onfocus="customOnFocus(this);"/>
			</div>
	</li>
