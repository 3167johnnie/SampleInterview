<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="jsFolderPath" value="%{@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI}"/>
<s:include value="/appNew/common/FooterPopupContent.jsp"></s:include>
<footer class="footer-campaign">
	<div class="container">
			<div class="row">
				<div class="footer-top">
					<div class="col-lg-10 ie-col-lg-10 ie-flt">
						<p>Site is best viewed in screen resolution 1366 x 768 pixels and above and is compatible with <br>
						Internet Explorer 9.0 or above, Mozilla, Google Chrome, Safari and Opera  Downloads. </p>
					</div>
					<div class="col-lg-2 ie-col-lg-2 ie-flt">
					<!-- Commented by pratima for removing RupeePower logo in Production -->
					 <%-- <div style=" background:#fff; padding: 9px; float:left;border-radius: 5px; margin-bottom: 10px;">
						<span class="pwrd-div"></span>
					</div>  --%>	
					
					</div>
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
							<a href="privacy-statement" target="_blank">Privacy Statement</a>
							<a href="disclosure-statement" target="_blank">Disclosure</a>
							<a href="term-condition" target="_blank">Terms & Conditions</a>
							
							<!-- <a href="javascript:void(0);" onclick="javascript:openPopups('privacyStatement','1');">Privacy Statement</a>
							<a href="javascript:void(0);" onclick="javascript:openPopups('disclosure','1');">Disclosure</a>
							<a href="javascript:void(0);" onclick="javascript:openPopups('termAndCondition','1');">Terms & Conditions</a> -->
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
<script type="text/javascript" src="<s:property value="%{#jsFolderPath}"/>/FooterCampaign.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <script type="text/javascript">
	jQuery(document).ready(function(){
		$('body').bind('cut copy paste',function(e){
			e.preventDefault();
		}); 
	});
</script> --%>
</html>
