<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

	<div class="right-side-menu">
			<div class="logo-div">
				<a href="javascript:sbiHomePage();">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/sbi-logo.png" />
				</a>
			</div>
			<div class="navigation">
				<div class="">
					<a id="hamburger" class="" href="#mainMenu"><span></span></a>
				</div>
				<div class="">
					<nav id="mainMenu">
						<ul>
							<li><a href="<s:property value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>">Home</a></li>
							<s:if test="%{homeLoanPage>0}">
								<li><a href="<s:property value="%{@com.mintstreet.common.util.Constants@SBI_HOMELOANS}"/>products.php?page_id=1" target="_blank">Our Products</a></li>
								<li><a href="javascript:void(0);" onclick="$('#approvedProject').modal('toggle');" title="Approved Projects">Approved Project</a></li>
							</s:if>
							<s:elseif test="%{autoLoanPage>0}">
								<li><a href="https://www.sbi.co.in/portal/web/personal-banking/car-loan" target="_blank">Our Products</a></li>
							</s:elseif>
							<s:elseif test="%{educationLoanPage>0}">
								<s:if test="%{appElTypeId==1}">
									<li><a href="https://www.sbi.co.in/portal/web/student-platform/scholar-loan" target="_blank">Our Products</a></li>
								</s:if>
								<s:elseif test="%{appElTypeId==2}">
									<li><a href="https://www.sbi.co.in/portal/web/student-platform/sbi-global-ed-vantage-scheme" target="_blank">Our Products</a></li>
								</s:elseif >
							</s:elseif>
							
							<li><a href="javascript:void(0);" onclick="closeMobileNavigation();showAppTrack();">Application Tracker</a></li>
						</ul>
					</nav>
				</div>
			</div>
	</div>
	<!-- <div class="navigation" style="display:block;"> -->
	<%-- <s:if test="%{homeLoanPage==1 || autoLoanPage==1 || personalLoanPage==1 || educationLoanPage==1}"> --%>
		<!-- <div class="nav-toggle">
			<div id="side-backdrop" style=""></div>
			<div class="menu">
				<div class="icon-close">
					<i class="fa fa-close"></i> <a>CLOSE</a>
				</div>
				<ul>
					<li><a href="<s:property value="%{@com.mintstreet.common.util.Constants@HOME_ACTION}"/>">Home</a></li>
					<li><a href="https://sbihomeloans.sbi.co.in/sbi-home-loan.php" target="_blank">Our Products</a></li>
					<li><a href="javascript:void(0);" onclick="$('#approvedProject').modal('toggle');" title="Approved Projects">Approved Project</a></li>
					<li><a href="javascript:void(0);" onclick="showAppTrack();">Application Tracker</a></li>
				</ul>
			</div>
			<div class="jumbotron">
				<div class="icon-menu"><i class="fa fa-bars "></i>Menu</div>
			</div>
		</div>-->
	<%-- </s:if> --%>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/jquery.mmenu.min.all.js"></script>
<script type="text/javascript" src="<s:property value="@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI"/>/main.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		jQuery("#productHomePageLogo").on("click", function(){
			sbiHomePage();
		});
	});
</script>
