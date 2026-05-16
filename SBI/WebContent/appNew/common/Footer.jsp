<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="jsFolderPath" value="%{@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI}"/>
<s:hidden id="fotterDBIpaddress" value="%{@com.mintstreet.common.action.BaseAction@iPAddressForAppAndDBServerPass}"/>
<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
<footer>
	<div class="container">
			<div class="row">
				<div class="footer-top">
					<div class="col-lg-12 ie-col-lg-10 ie-flt">
						<p>Site is best viewed in screen resolution 1366 x 768 pixels and above and is compatible with <br>
						Internet Explorer 9.0 or above, Mozilla, Google Chrome, Safari and Opera  Downloads. </p>
					</div>
					
					<!-- Commented by pratima for removing RupeePower logo in Production -->
					 <%-- <div class="col-lg-2 col-xs-12 ie-col-lg-2 ie-flt">
						<span class="pwrd-div"></span>
					</div> --%> 
					
				</div>
			</div>
	</div>
	<div class="footer-bottom">
		<div class="container">
				<div class="row">
					<div class="col-lg-12">
						<p>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/home-faq" target="_blank">FAQs</a>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/contact-us" target="_blank">Contact Us</a>
							<a id="fprivacyStmt" href="javascript:void(0);">Privacy Statement</a>
							<a id="fdisclosuree" href="javascript:void(0);">Disclosure</a>
							<a id="ftandc" href="javascript:void(0);">Terms & Conditions</a>
						</p>
						<p>&copy; State Bank of India (APM ID: L&A_Info_344). All Right Reserved</p>
					</div>
				</div>
		</div>			
	</div>
</footer>
<s:include value="/appNew/status/ApplicationStatus.jsp"></s:include>
<div id="go2CloudforcallBack"></div>
</body>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mmenu.min.all.js"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/main.js"></script>
<script type="text/javascript" src="<s:property value="%{#jsFolderPath}"/>/Footer.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <script type="text/javascript">
	jQuery(document).ready(function(){
		$('body').bind('cut copy paste',function(e){
			e.preventDefault();
		}); 
	});
	<s:if test="%{@com.mintstreet.common.action.BaseAction@iPAddressForAppAndDBServerPass==0}">
		openIllegalDiv();
	</s:if>
	function closeIllegalDiv(){
		stickerOpenClose(true);
		jQuery("#illegal").removeClass("show").addClass("hide");
		jQuery("#illegalOverlay").removeClass("show").addClass("hide");
	}
	function openIllegalDiv(){
		stickerOpenClose(false);
		jQuery("#illegal").removeClass("hide").addClass("show");
		jQuery("#illegalOverlay").removeClass("hide").addClass("show");
	}
	function stickerOpenClose(flag){
		var documentWidth = document.documentElement.clientWidth;				
		if(documentWidth==800 || documentWidth == 980){
			if(flag){
				$('#tabSticker').show();
			}else{
				$('#tabSticker').hide();
			}
		}
	}
</script> --%>
</html>
