<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li>
	<label class="">Do you or any of your family members own a Pucca house?<b class="req">*</b>
	<a class="tool-tip" style="
    position: absolute;
"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/question-icon.png" alt=""/>
<span style="left: -190px;">To avail subsidy under PMAY scheme, please confirm if you or any of your family members own a Pucca House.</span></a></label>
	<div class="flat-field">
		<s:select list ="#{'0':'Select Pucca houses','1':'Yes','2':'No'}"  cssClass="form-select" id="loanQuotePuccaHouse" name="quote.loanQuotePuccaHouse" autocomplete="off" aria-invalid="false" value="%{quote.loanQuotePuccaHouse}" />
	</div>
</li>
