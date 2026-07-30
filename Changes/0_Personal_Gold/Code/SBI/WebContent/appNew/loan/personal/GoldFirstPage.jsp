<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if  test="%{includeJs==true}">
	<s:if test="%{personalLoanPage <= 1}">
		<s:include value="/appNew/loan/personal/PersonalCommonScript.jsp"></s:include>
	</s:if>
</s:if>
<s:include value="/appNew/loan/personal/PersonalLeftSidebar.jsp"></s:include>
<s:include value="/appNew/loan/personal/GoldRightContent.jsp"></s:include>

<s:if  test="%{includeJs==true}">
	<s:if test="%{personalLoanPage <= 1}">
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
	</s:if>
</s:if>
