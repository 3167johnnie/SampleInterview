<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:include value="/appNew/common/HeaderProduct.jsp"></s:include>
<div id="msg-panel" class="msg-war corner-all" style="display: <s:property value="%{responseMessage!=null?'block':'none'}" />;"><span>&nbsp;</span>
	<b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
</div>
<div id="page-loader" style="display:none;">	
    <div class="spinner-container">
        <div class="addmarginB20">Please Wait...</div>        
        <img class="loader-img" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/loader_sbi.gif" alt="loader-heart" title="loader-heart"/>
    </div>
</div>
<s:set var="closeOffStr" value="%{' onbeforeunload=cloaseOff();cloaseOff(); '}"/>
	<body class="blue-bg" <s:property value="%{closeOffStr}" />>
		<div class="container-fluid ">
			<div class="row">
				<div class="col-lg-12 col-md-12 col-xs-12 col-sm-12 no-padding">
					<s:include value="/appNew/common/MessagePageProduct.jsp"></s:include>
					<s:include value="/appNew/common/RightSideToggleMenuBar.jsp"></s:include>
					<div id="firstPageContent"></div>
					<div id="secondPageContent">
						<s:include value="/appNew/loan/educationloan/EducationFirstPage.jsp"></s:include>
						
					</div>
					<div id="thirdPageContent"></div>
					<div id="fourthPageContent"></div>
				</div>
			</div>
		</div>
		
		<s:include value="/appNew/loan/educationloan/CommonContent.jsp"></s:include>
		<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
		<s:include value="/appNew/common/CommonFooterScript.jsp"></s:include>
		<script type="text/javascript">
	    	$("#content-2").mCustomScrollbar({
				//autoHideScrollbar:true,
				theme:"rounded",
				scrollInertia:5
			});
		</script>
	</body>
</html>
