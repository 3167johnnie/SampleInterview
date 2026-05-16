<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/app/common/messageTextPage.jsp"></s:include>
<s:if test="%{#needToShowMessage}">
	<div class="marquee-txt1">
		<marquee scrollamount="3">
			Dear Users, the Online Customer Acquisition System will be under maintenance from <s:property value="%{marqueeStartTime}"/> to <s:property value="%{marqueeEndTime}"/> on <s:property value="%{marqueeDate}"/>. Inconvenience is regretted.
		</marquee>
	</div>
</s:if>
