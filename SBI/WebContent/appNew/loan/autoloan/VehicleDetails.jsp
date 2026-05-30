<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<div id="carDetails">
	<s:if test="%{(quote==null || quote.loanQuoteLoanPurposeId != 0)}">
		<s:include value="/appNew/loan/autoloan/includes/CarDetail.jsp"></s:include>
	</s:if>
</div>

<%-- <div id="bikeDetails">
		<s:include value="/appNew/loan/autoloan/includes/BikeDetail.jsp"></s:include>
</div> --%>
