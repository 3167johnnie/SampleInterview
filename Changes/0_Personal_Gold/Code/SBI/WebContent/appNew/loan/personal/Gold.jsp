<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/appNew/common/HeaderProduct.jsp"></s:include>
	<div id="msg-panel" class="msg-war corner-all" style="display: <s:property value="%{responseMessage!=null?'block':'none'}" />;"><span>&nbsp;</span>
		<b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
	</div>
	<div id="page-loader">	
	    <div class="spinner-container">
	        <img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader_sbi.gif"  />
	        <div class="addmarginB20">Please Wait...</div>
	    </div>
	    <div class="loader-rpimg">
	    	<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/f-logo.png" alt=""  />
	    </div>
	</div>
	<s:set var="closeOffStr" value="%{' onbeforeunload=cloaseOff();cloaseOff(); '}"/>
	<body class="blue-bg" <s:property value="%{closeOffStr}" />>
	<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE=='live'}">
			<!-- Google Tag Manager (noscript) -->
			<noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-TRNVC76"
			height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
			<!-- End Google Tag Manager (noscript) -->
	</s:if>
		<div class="container-fluid ">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 no-padding">
					<s:include value="/appNew/common/MessagePageProduct.jsp"></s:include>
					<s:include value="/appNew/common/RightSideToggleMenuBar.jsp"></s:include>
					<div id="firstPageContent">
						<s:include value="/appNew/loan/personal/GoldFirstPage.jsp"></s:include>
					</div>
					<!-- <div id="secondPageOTPContent"></div> -->
					<div id="secondPageContent"></div>
					<div id="thirdPageContent"></div>
					<div id="fourthPageContent"></div>
				</div>
			</div>
		</div>
		<s:include value="/appNew/loan/personal/CommonContent.jsp"></s:include>
		<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
		<div id="pixelTracking"></div>
		<div id="pixelTrackingGoCloud"></div>
	</body>
</html>
