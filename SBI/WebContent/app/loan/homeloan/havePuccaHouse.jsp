<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="div-left">
	<label  class="" style="line-height: 1px!important;padding-top: 6px;">Do you or any of your family<br> members own a Pucca house?<b class="req">*</b><a class="tooltip new-tltip" 
	style="padding-top: 11px;background:none;"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/>
	<span class="hint-txt1">To avail subsidy under PMAY scheme, please confirm if you or any of your family members own a Pucca House.</span></a></label>
	<s:property escapeHtml="false" value="%{#br}"/>
	<s:select list ="#{'0':'Select Pucca houses','1':'Yes','2':'No'}"  id="loanQuotePuccaHouse" name="quote.loanQuotePuccaHouse" autocomplete="off" aria-invalid="false" value="%{quote.loanQuotePuccaHouse}" />
</div>
