<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="prd-tab">
	<div class="container">
		<div class="row">
			<ul>
				<li id="ul_li_2" class="menu-li" rel="ddn-nav-2">
					<a href="javascript:;">
						<h2><span class="hl-icon"></span>Home Loan</h2>
						<p>One stop solution for all your Housing needs.</p>
						<span>&nbsp;</span>
					</a>
					<div class="ddn-nav-hover ddn-nav-hover-1">
						<ul>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>">Home Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>">Home Top Up Loan</a></li>
						</ul>
					</div>
				</li>
				<li id="ul_li_1" class="menu-li" rel="ddn-nav">
					<a href="javascript:;" >
					<h2><span class="rl-icon"></span>Other Retail Loan</h2>
					<p>Apply for Auto Loan, Personal Loan,Gold Loan, Pension Loan, Scholar Loan and Global ED- Vantage, EL Takeover</p></a>
					
					<div class="ddn-nav-hover ddn-nav-hover-2">
						<ul>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>">Auto Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>">Personal Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>">Gold Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>">Pension Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>">Scholar Loan</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>">Global ED-Vantage</a></li>
							<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>">EL Takeover</a></li>
						</ul>
					</div>
					
				</li>
				<div class="menu-div ddn-nav-2">
					<ul>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_LOAN_ACTION}"/>">Home Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@HOME_TOP_UP_LOAN_ACTION}"/>">Home Top Up Loan</a></li>
					</ul>
				</div>
				<div class="menu-div ddn-nav">
					<ul>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AUTO_LOAN_ACTION}"/>">Auto Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PERSONAL_LOAN_ACTION}"/>">Personal Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GOLD_LOAN_ACTION}"/>">Gold Loan</a></li>	
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@PENSION_LOAN_ACTION}"/>">Pension Loan</a></li>	
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCHOLAR_LOAN_ACTION}"/>">Scholar Loan</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@GLOBAL_EDVANTAGE_ACTION}"/>">Global ED-Vantage</a></li>
						<li><a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@EDUCATION_LOAN_TAKEOVER_ACTION}"/>">EL Takeover</a></li>
					</ul>
				</div>
			<%-- 	<li  id="ul_li_3">
					<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@AGRI_LOAN_ACTION}"/>">
						<h2><span class="agl-icon"></span>Agri Loan</h2>
						<p>A help offered by SBI to improve agriculture statistics. </p>
					</a>	
				</li> --%>
				<li  id="ul_li_4">
					<a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@SCC_LOAN_ACTION}"/>">
						<h2><span class="ccr-icon"></span>Credit Card</h2>
						<p>A complete range of Credit Cards that matches your lifestyle. </p>
					</a>	
				</li>
				<li id="ul_li_5" class="menu-li" rel="ddn-nav">
                    <a href="<%= request.getContextPath().replace("/sbi", "/personal-banking") %>/<s:property value="%{@com.mintstreet.common.util.Constants@CVE_ACTION}"/>">
						<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/cve.jpg"> <h2>Insurance & Investment</h2>
                            <p>A Complete range of Insurance,Investment and Credit Card that matches your requirement.</p>
                    </a>
                </li>
			</ul>
		</div>
	</div>
</div>
