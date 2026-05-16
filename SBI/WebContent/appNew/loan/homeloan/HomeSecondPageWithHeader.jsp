<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:include value="/appNew/common/HeaderProduct.jsp"></s:include>
	<div id="msg-panel" class="msg-war corner-all" style="display: <s:property value="%{responseMessage!=null?'block':'none'}" />;"><span>&nbsp;</span>
		<b><s:property value="%{infoMessage==1?'Message':'Error'}" />: </b><em><s:property escapeHtml="false" value="responseMessage" /></em>
	</div>
	<div id="page-loader">	
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
						<s:include value="/appNew/loan/homeloan/HomeFirstPage.jsp"></s:include>
					</div>
					<div id="thirdPageContent"></div>
					<div id="fourthPageContent"></div>
				</div>
			</div>
		</div>
		<!-- Facebook Pixel Code -->
		<s:if test="%{@com.mintstreet.common.util.Constants@DEPLOYMENT_MODE!='local'}">
			<script>
				!function(f, b, e, v, n, t, s) {
					if (f.fbq)
						return;
					n = f.fbq = function() {
						n.callMethod ? n.callMethod.apply(n, arguments) : n.queue
								.push(arguments)
					};
					if (!f._fbq)
						f._fbq = n;
					n.push = n;
					n.loaded = !0;
					n.version = '2.0';
					n.queue = [];
					t = b.createElement(e);
					t.async = !0;
					t.src = v;
					s = b.getElementsByTagName(e)[0];
					s.parentNode.insertBefore(t, s)
			}(window, document, 'script','https://connect.facebook.net/en_US/fbevents.js');
			fbq('init', '306199269752742');
			fbq('track', 'PageView');
			
				fbq('track', 'ViewContent');
			</script>
			<noscript>
				<img height="1" width="1" style="display: none"
					src="https://www.facebook.com/tr?id=306199269752742&ev=PageView&noscript=1" />
			</noscript>
		</s:if>
		<!-- DO NOT MODIFY -->
		<!-- End Facebook Pixel Code -->
		<s:include value="/appNew/loan/homeloan/CommonContent.jsp"></s:include>
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
