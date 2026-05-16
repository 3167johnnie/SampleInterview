<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{homeLoanPage <= 1}">
		<s:include value="/appNew/loan/homeloan/HomeCommonScript.jsp"></s:include>
	</s:if>
</s:if>
<s:include value="/appNew/loan/homeloan/HomeLeftSidebar.jsp"></s:include>
<s:include value="/appNew/loan/homeloan/HomeRightContent.jsp"></s:include>
<%-- <s:if test="%{homeLoanPage == 1}">
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081701&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
		<script language='JavaScript1.1' src='//pixel.mathtag.com/event/js?mt_id=1081707&mt_adid=174529&v1=&v2=&v3=&s1=&s2=&s3='></script>
	</s:if>
</s:if> --%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{homeLoanPage <= 1}">
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
	</s:if>
</s:if>
