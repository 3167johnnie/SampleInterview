<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{autoLoanPage <= 1}">
		<s:include value="/appNew/loan/autoloan/AutoCommonScript.jsp"></s:include>
	</s:if>
</s:if>

<s:include value="/appNew/loan/autoloan/AutoLeftSidebar.jsp"></s:include>
<s:include value="/appNew/loan/autoloan/AutoRightContent.jsp"></s:include>
<%-- <s:if test="%{autoLoanPage == 1}">
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081703&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081708&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	</s:if>
</s:if> --%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{autoLoanPage <= 1}">
		<s:include value="/app/common/CommonFooterScript.jsp"></s:include>
	</s:if>
</s:if>
