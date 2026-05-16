<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:set var="jsFolderPath" value="%{@com.mintstreet.common.util.Constants@BANK_JS_FOLDER_NEWUI}"/>
<s:include value="/appNew/common/Header.jsp" />
<s:hidden id="ipAddressDB" value="%{@com.mintstreet.common.action.BaseAction@iPAddressForAppAndDBServerPass}"/>

<div class="side-button">

	<%-- <a id="appTracker" href="javascript:void(0);" onclick="closeMobileNavigation();showAppTrack();" class="app-track-btn"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/app-tracker.png" alt=""/><br>Application<br> Tracker</a>
	<a id="GetCallBack" href="javascript:void(0);" onclick="closeMobileNavigation();getACallBackId();" class="call-back-btn"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/call-back.png" alt=""/><br>Get a <br>Callback</a> --%>
	<a id="appTracker" href="javascript:void(0);" class="app-track-btn"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/app-tracker.png" alt=""/><br>Application<br> Tracker</a>


	<a id="GetCallBack" href="javascript:void(0);" class="call-back-btn"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/call-back.png" alt=""/><br>Get a <br>Callback</a>
</div>

<section class="home-banner">
 <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">

  <ol class="carousel-indicators">
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="5"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="6"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="7"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="8"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="9"></li>
    <li data-bs-target="#carouselExampleIndicators" data-bs-slide-to="10"></li>

  </ol>
  <div class="carousel-inner"  id="myCarousel" role="listbox"   >
    <div class="carousel-item active">
    
    <div class="container">
   
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME LOAN
								<p class="para-txt">Sapna Ho Jaisa, Home Loan Vaisa !</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_banner.jpg"  class="d-block w-100" alt="">
    
      
    </div>
    <div class="carousel-item">
    <div class="container">
      <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME TOP UP LOAN
								<p class="para-txt">Now avail a Top Up Loan at Home Loan Rate!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_TOPUP_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
      <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CAR LOAN
								<p class="para-txt">Upgrade your life!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AUTO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
       <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PERSONAL LOANS
								<p class="para-txt">Xpress Credit takes care of all your needs, instantly.</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
					
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PL_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
     <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI GOLD LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
   <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PENSION LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100"alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI SCHOLAR LOANS
								<p class="para-txt">Loan for pursuing higher education in select Premier Institutions in India</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCH_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI OVERSEAS EDUCATION LOAN
								<p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/GLO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
        <div class="container">              
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI EL TAKEOVER LOAN
								<p class="para-txt">EL TAKEOVER LOAN</p>
								<!-- <p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p> -->
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/OVER_ED_banner.jpg"  class="d-block w-100" alt="">
    </div>
   <div class="carousel-item">
        <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI AGRICULTURE LOAN
								<p class="para-txt">Agriculturists & Landless Agricultural Labourers</p>
							</div>
							<%-- <a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AGRI_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a> --%>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AGL_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CARD
								<p class="para-txt">WELCOME TO A WORLD OF BENEFITS WITH SBI CREDIT CARDS. </p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCC_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
		 		</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCC_banner.jpg"  class="d-block w-100"  alt="">
    </div>
  </div>
  
</div>  

</section>

<%-- <div id="carouselExampleAutoplaying" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-inner">
    <div class="carousel-item active">
     <!--  <img src="..." class="d-block w-100" alt="..."> -->
     <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AUTO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
      <img src="..." class="d-block w-100" alt="...">
    </div>
    <div class="carousel-item">
      <img src="..." class="d-block w-100" alt="...">
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleAutoplaying" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div> --%>






<%--  <!--start new code   -->
<div id="carouselExampleIndicators" class="carousel slide">
  <div class="carousel-indicators">
       <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="0" class="active" aria-current="true" aria-label="Slide 1"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="1" aria-label="Slide 2"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="2" aria-label="Slide 3"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="3" aria-label="Slide 4"></button>
    <button type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide-to="4" aria-label="Slide 5"></button>
  </div>
  <div class="carousel-inner">
    <div class="carousel-item active">
     <!--  <img src="..." class="d-block w-100" alt="..."> -->
     <img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PL_banner.jpg" class="d-block w-100" alt="">
        <div class="container">
   
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME LOAN
								<p class="para-txt">Sapna Ho Jaisa, Home Loan Vaisa !</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_banner.jpg"  class="d-block w-100" alt="">
   
    </div>
    <div class="carousel-item">
         <div class="container">
      <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME TOP UP LOAN
								<p class="para-txt">Now avail a Top Up Loan at Home Loan Rate!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_TOPUP_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
        <div class="container">
        <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CAR LOAN
								<p class="para-txt">Upgrade your life!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AUTO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
       <div class="container">
       <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PERSONAL LOANS
								<p class="para-txt">Xpress Credit takes care of all your needs, instantly.</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
					
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PL_banner.jpg" class="d-block w-100" alt="">
       </div>
    
      <div class="carousel-item">
      <div class="container">
     <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI GOLD LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
    <div class="carousel-caption">
       <div class="intro-text">
           <div calss="intro-lead-in">
             <span>Welcome</span>SBI PENSION LOAN
             <p class="para-txt"></p>
           </div>
       </div>
    </div>
    
    </div>
    
    
    </div>
    
     <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PENSION LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100"alt="">
    </div>
    
    
  </div>
   <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleIndicators" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="visually-hidden">Next</span>
  </button>
</div> 
<!--end new code  --> --%> 


 <%-- <div id="carouselExampleIndicators" class="carousel slide" data-ride="carousel">

  <ol class="carousel-indicators">
    <li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="1"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="2"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="3"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="4"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="5"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="6"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="7"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="8"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="9"></li>
    <li data-target="#carouselExampleIndicators" data-slide-to="10"></li>

  </ol>
  <div class="carousel-inner" role="listbox">
    <div class="carousel-item active">
    
    <div class="container">
   
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME LOAN
								<p class="para-txt">Sapna Ho Jaisa, Home Loan Vaisa !</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_banner.jpg"  class="d-block w-100" alt="">
    
      
    </div>
    <div class="carousel-item">
    <div class="container">
      <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME TOP UP LOAN
								<p class="para-txt">Now avail a Top Up Loan at Home Loan Rate!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_TOPUP_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
      <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CAR LOAN
								<p class="para-txt">Upgrade your life!</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AUTO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
       <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PERSONAL LOANS
								<p class="para-txt">Xpress Credit takes care of all your needs, instantly.</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
					
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PL_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
    <div class="container">
     <div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI GOLD LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
					</div>
				
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
   <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PENSION LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg"  class="d-block w-100"alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI SCHOLAR LOANS
								<p class="para-txt">Loan for pursuing higher education in select Premier Institutions in India</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCH_banner.jpg" class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI OVERSEAS EDUCATION LOAN
								<p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/GLO_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
        <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI EL TAKEOVER LOAN
								<p class="para-txt">EL TAKEOVER LOAN</p>
								<!-- <p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p> -->
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/OVER_ED_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
        <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI AGRICULTURE LOAN
								<p class="para-txt">Agriculturists & Landless Agricultural Labourers</p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AGRI_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AGL_banner.jpg"  class="d-block w-100" alt="">
    </div>
    <div class="carousel-item">
      <div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CARD
								<p class="para-txt">WELCOME TO A WORLD OF BENEFITS WITH SBI CREDIT CARDS. </p>
							</div>
							<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCC_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
		 		</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCC_banner.jpg"  class="d-block w-100"  alt="">
    </div>
  </div>
  
  --%>

  
  
<%--    <button class="carousel-control-prev" type="button" data-target="#carouselExampleIndicators" data-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-target="#carouselExampleIndicators" data-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </button> --%>
  
<%--   <button class="carousel-control-prev" type="button" data-target="#carouselExampleIndicators" data-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
    <span class="sr-only">Previous</span>
  </button>
  <button class="carousel-control-next" type="button" data-target="#carouselExampleIndicators" data-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
    <span class="sr-only">Next</span>
  </button> --%>
<!-- </div> -->
  <%--  </section> --%>




	<%-- <div id="carousel-example-generic" class="carousel slide"
		data-ride="carousel">
		<!-- Indicators -->
		<ol class="carousel-indicators">
			<li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
			<li data-target="#carousel-example-generic" data-slide-to="1"></li>
			<li data-target="#carousel-example-generic" data-slide-to="2"></li>
			<li data-target="#carousel-example-generic" data-slide-to="3"></li>
			<li data-target="#carousel-example-generic" data-slide-to="4"></li>
			<li data-target="#carousel-example-generic" data-slide-to="5"></li>
			<li data-target="#carousel-example-generic" data-slide-to="6"></li>
			<li data-target="#carousel-example-generic" data-slide-to="7"></li>
			<li data-target="#carousel-example-generic" data-slide-to="8"></li>
			<li data-target="#carousel-example-generic" data-slide-to="9"></li>
		</ol>

		<!-- Wrapper for slides -->
		<div class="carousel-inner" role="listbox">
			<div class="slider1 item active">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME LOAN
								<p class="para-txt">Sapna Ho Jaisa, Home Loan Vaisa !</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_banner.jpg" alt="">
			</div>
			<div class="slider2 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI HOME TOP UP LOAN
								<p class="para-txt">Now avail a Top Up Loan at Home Loan Rate!</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/HL_TOPUP_banner.jpg" alt="">
			</div>
			<div class="slider3 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CAR LOAN
								<p class="para-txt">Upgrade your life!</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AUTO_banner.jpg" alt="">
			</div>
			<div class="slider4 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PERSONAL LOANS
								<p class="para-txt">Xpress Credit takes care of all your needs, instantly.</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PL_banner.jpg" alt="">
			</div>
			<div class="slider5 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI GOLD LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg" alt="">
			</div>
			<div class="slider6 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI PENSION LOAN
								<p class="para-txt">Play the opening batsman in your second innings</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/PENSION_banner.jpg" alt="">
			</div>
			<div class="slider7 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI SCHOLAR LOANS
								<p class="para-txt">Loan for pursuing higher education in select Premier Institutions in India</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCH_banner.jpg" alt="">
			</div>
			<div class="slider8 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI OVERSEAS EDUCATION LOAN
								<p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/GLO_banner.jpg" alt="">
			</div>
			<div class="slider9 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI OVERSEAS EDUCATION LOAN
								<p class="para-txt">Challenge the world with GLOBAL ED-VANTAGE</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/OVER_ED_banner.jpg" alt="">
			</div>
			<div class="slider10 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI AGRICULTURE LOAN
								<p class="para-txt">Agriculturists & Landless Agricultural Labourers</p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@AGRI_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/AGL_banner.jpg" alt="">
			</div>
			<div class="slider11 item">
				<div class="container">
					<div class="carousel-caption">
						<div class="intro-text">
							<div class="intro-lead-in">
								<span>Welcome to</span>SBI CARD
								<p class="para-txt">WELCOME TO A WORLD OF BENEFITS WITH SBI CREDIT CARDS. </p>
							</div>
							<a href="<s:property value="%{@com.mintstreet.common.util.Constants@SCC_LOAN_ACTION}"/>" class="page-scroll btn btn-xl blue-btn" target="_blank">APPLY NOW</a>
						</div>
					</div>
				</div>
				<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/SCC_banner.jpg" alt="">
			</div>
		</div>
	</div> --%>
<%-- </section>

</div> --%>
<s:include value="/appNew/common/HeaderTabs.jsp" />
<!-- Why Section -->
<div class="why-con-div">
	<div class="container">
		<div class="row">
			<div class="col-lg-12">
				<h2>
					<span class="head-bg">Why Choose <span>SBI</span></span>
				</h2>
				<p class="txt-center">SBI welcomes you to explore the world of Premier Banking in India. SBI comes to you on the solid foundation of trust and transparency built in the tradition of SBI. Best practices followed in SBI mentioned below, will tell you why it makes sense to do business with SBI.</p>
				<ul>
					<li>
						<div class="numr">01</div>
						<div class="numr-txt">
							<h3>Our Values</h3>
							<p>SBI works on the values of Trust, Transparency, Integrity and Excellence.</p>
						</div>
					</li>
					<li>
						<div class="numr">02</div>
						<div class="numr-txt">
							<h3>Values to customer</h3>
							<p>SBI offers a wide range of products, low Interest Rates, Interest calculation on daily reducing balance with low processing fee, no hidden charges and no pre-payment penalty.</p>
						</div>
					</li>
					<li>
						<div class="numr">03</div>
						<div class="numr-txt">
							<h3>Our presence</h3>
							<p>SBI serves you with over 25,000+ branches with strong dedicated Sales team and Sunday Banking at selected branches. We have specialized work force at CPCs for loan processing. </p>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
</div>

<div class="knw-div">
	<div class="container">
		<div class="row">
			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 abt-div">
				<h3>
					Know More About<span>SBI Bank</span>
				</h3>
				<p>
					The origin of State Bank of India dates back to 1806 when the Bank
					of Calcutta (later called the Bank of Bengal) was established. <br>
					<br>
					<a target="_blank" href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/home-about-us">Read More</a>
				</p>
			</div>

			<div class="col-lg-6 col-md-6 col-sm-6 col-xs-12 dwn-app-div">
				<div class="app-txt">
					<h3>Download<span>Mobile App</span></h3>					
					<p>Download our top-rated app it’s free,easy and smart</p>														
					<a target="_blank" href="https://apps.apple.com/in/app/sbi-quick/id1015862980">
						<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/applestoresb.svg" width="91" alt=""></a>	
											
					<a target="_blank" href="https://play.google.com/store/apps/details?id=com.sbi.sbiquick&amp;hl=en_IN&amp;gl=US">
					
						<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/gplaystoresb.png" width="120" alt=""></a>							
				</div>
				<div class="app-img">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/mobile-app-img.jpg" alt="" />
				</div>
			</div>
		</div>
	</div>
</div>


	
<div class="page_overlay hide" id="illegalOverlay"></div>
<div class="modal otp-box" id="illegal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<div class="offer-left-section">
						 <p class="img-cntr">
						 	<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/warning-icon.png">
						 </p>	
				</div>
				<div class="offer-right-section">
					<h6>UNDER MAINTANANCE!</h6>
						<p>The product is under maintainance. Please contact your system administrator for assistance.</p>
      			</div>
			</div>
		</div>
	</div>	
</div>	
<s:include value="/appNew/common/HomePopups.jsp"></s:include>
<s:include value="/appNew/common/Footer.jsp"></s:include>
<script type="text/javascript" src="<s:property value="%{#jsFolderPath}"/>/BitSightHome.js?v=<s:property value="%{#SCRIPT_VERSION}"/>"></script>
<%-- <script type='text/javascript'>
	<s:if test="%{@com.mintstreet.common.action.BaseAction@iPAddressForAppAndDBServerPass==0}">
		openIllegalDiv();
	</s:if>
	function closeIllegalDiv(){
		jQuery("#illegal").removeClass("show").addClass("hide");
		jQuery("#illegalOverlay").removeClass("show").addClass("hide");
	}
	function openIllegalDiv(){
		jQuery("#illegal").removeClass("hide").addClass("show");
		jQuery("#illegalOverlay").removeClass("hide").addClass("show");
	}
</script> --%>

<%-- <script type="text/javascript">
$('.carousel').bind('slide.bs.carousel', function (e) {
    var index = $(e.relatedTarget).index();

    $('[data-target="#' + $(this).prop('id') + '"]').each(function (i) {
        if (i === index) {
            $(this).addClass('active');
        } else {
            $(this).removeClass('active');
            $(this).addClass('inactive');
        }
    });
});




let myCarousel = document.querySelector('#myCarousel');
myCarousel.addEventListener('slide.bs.carousel', (event) => {
    let elementChildrens = document.querySelector("#carouselExampleIndicators").children;
    elementChildrens.item(event.from).classList.remove("active");
    elementChildrens.item(event.to).classList.add("active");
});


</script> --%>


