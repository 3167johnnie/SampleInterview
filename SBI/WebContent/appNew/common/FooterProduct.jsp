<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--FOOTER START--->
<div class="ocas-footer">
	<div class="f-nav">
		<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/home-faq" target="_blank">FAQs</a>
		<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/contact-us" target="_blank">Contact Us</a>
		<a href="javascript:void(0);" onclick="javascript:openPopups('privacyStatement','1');">Privacy Statement</a>
		<a href="javascript:void(0);" onclick="javascript:openPopups('disclosure','1');">Disclosure</a>
		<a href="javascript:void(0);" onclick="javascript:openPopups('termAndCondition','1');">Terms & Conditions</a>
	</div>
</div>
<%-- <script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER"/>/jquery.commonFunction.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script> --%>
<script>

	window.history.pushState(null, "",window.location.href);
	window.onpopstate = function(){
		window.history.pushState(null, "",window.location.href);
	};
</script>
