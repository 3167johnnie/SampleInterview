CCMS NTB Consent frontend changes and storing in DB on consent accept
 parent 95d56c9f
 Branches 
 No related tags found
 No related merge requests found
Changes
17
Showing 
 with 741 additions and 25 deletions
  JS/ocas/sbiNew/css/global.css 
+
15
−
1
@@ -198,6 +198,20 @@ padding:4px 30px 4px 9px; font-size:15px;font-family:Roboto-Light; float:left;op
padding:4px 30px 4px 9px; font-size:15px;font-family:Roboto-Light; float:left;}
.input-text:focus{ box-shadow:none;}
.form-control[disabled], .form-control[readonly], fieldset[disabled] .form-control{background:#f8f8f8;color:#a9a9a9!important;}
.otp-box .modal-body,.app-box .modal-body,.login-box .modal-body{padding:0px;width: 623px;height:478px;}
.privacy-modal-body, .privacy-app-box .privacy-modal-body, .privacy-login-box .privacy-modal-body {
    padding: 20px;
    width: 623px;
    height: 478px;
}
.privacy-consent-box {padding:20px;width: 623px;height:478px;}
.privacy-consent-pop-content{ font-size:12px; color:#3a3a3a; padding:5px; text-align:justify; height:348px; overflow-y:scroll;}
.privacy-consent-dropdown {
            display: flex;
            justify-content: center;
            margin-bottom: 15px;
			background:#f8f8f8;color:#a9a9a9!important;
        }
select.form-control[disabled]{background:#f8f8f8!important;color:#a9a9a9!important;opacity: 0.7;}
input#discountCouponName {width: 59%;}
.form-section div.flat-field select.form-control[disabled]{background:#f8f8f8 /* url("../images/drop-down-arrow-disabled.png") no-repeat scroll 100% top !important; */}
@@ -344,7 +358,7 @@ transition:all 0.3s ease-in-out 0s, visibility 0s ease-in-out 0.3s, z-index 0s e
.ddn-nav,.ddn-nav-2{display:none}
.ddn-nav,.ddn-nav-2{display:none; position:absolute; top:-185px;background:#0648a0; width:600px; float:left; z-index:9999;}
.ddn-nav-2{top:-130px;}
.ddn-nav ul,.ddn-nav-2 ul, .ddn-nav-hover ul{width:100%; padding:20px 20px 40px 20px; float:left;}
.ddn-nav ul,.ddn-nav-2 ul, .ddn-nav-hover ul{width:100%; padding:16px 20px 40px 20px; float:left;}
.ddn-nav-hover ul li,.ddn-nav-hover ul li:first-child,.ddn-nav-2 ul li:first-child{background:url(../images/arrow.png) no-repeat left 11px!important;}
.ddn-nav ul li,.ddn-nav-2 ul li,.ddn-nav-hover ul li{ width:33.333%; padding:0 0 0 0px; background:url(../images/arrow.png) no-repeat left 11px; }
.ddn-nav ul li:hover a,.ddn-nav-2 ul li:hover a,.ddn-nav-hover ul li:hover a{background:url(../images/arrow.png) no-repeat left 11px; }
  SBI/WebContent/WEB-INF/applicationContext.xml 
+
11
−
1
@@ -337,7 +337,15 @@
	<bean id="consentWriteDao" class="com.mintstreet.consent.dao.ConsentWriteDao" >
	   	<property name="entityManagerFactory" ref="entityManagerFactory" />
    </bean>

    
    <bean id="privacyRequestResponseDao" class="com.mintstreet.consent.dao.PrivacyRequestResponseDao">
		<property name="entityManagerFactory" ref="entityManagerFactory" />
	</bean>
	
	<bean id="masterLanguageDao" class="com.mintstreet.consent.dao.MasterLanguageDao">
		<property name="entityManagerFactory" ref="entityManagerFactory" />
	</bean>
    
	<bean id="commonService" class="com.mintstreet.common.service.CommonService">
		<property name="masterGenderDao" ref="masterGenderDao" />
		<property name="stateDao" ref="stateDao" />
@@ -415,6 +423,8 @@
	    <property name="applicationFormCaseCveDao" ref="applicationFormCaseCveDao"/>
	    <property name="consentDao"  ref="consentDao"/>
	    <property name="ccmsConfigDao"  ref="ccmsConfigDao"/>
	    <property name="privacyRequestResponseDao"  ref="privacyRequestResponseDao"/>
	    <property name="masterLanguageDao" ref="masterLanguageDao" />
	      
	</bean>
	
  SBI/WebContent/WEB-INF/web.xml 
+
3
−
0
@@ -262,6 +262,9 @@
	<url-pattern>/quick-lead</url-pattern>
	<url-pattern>/cve</url-pattern>
	<url-pattern>/cve-loan-actions</url-pattern>
	<url-pattern>/getPrivacyNoticeByLocale</url-pattern>
	<url-pattern>/savePrivacyConsent</url-pattern>
	<url-pattern>/getPrivacyLanguageList</url-pattern>
    <dispatcher>FORWARD</dispatcher>
    <dispatcher>REQUEST</dispatcher>
  </filter-mapping>
  SBI/WebContent/appNew/common/ConsentPopup.jsp 
+
323
−
7
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="termAndConditionHTML">
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<!--  comment for strict click -->
	<!-- <div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"> -->
	<div class="modal fade otp-box" id="consentHomeLoan" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" data-bs-backdrop="static"
		data-bs-keyboard="false">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close"><span aria-hidden="true">
					<img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png"/></span></button> 
					<div id = "consentHomeLoanDiv" class="f-otp-pop-content ">
				<div class="privacy-modal-body">
					<%-- <button type="button" class="close clo" data-bs-dismiss="modal" aria-bs-label="Close">
						<span aria-hidden="true"> <img
							src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closedark.png" /></span>
					</button> --%>
					<%-- <div id="consentHomeLoanDiv" class="f-otp-pop-content ">
						<s:property escapeHtml="false" value="%{beanList.consentHomeLoan}" />
						
					</div> --%>
					<div class="privacy-consent-dropdown">

						<select id="privacyLocaleDropdown"
							class="privacy-consent-dropdown"
							onchange="loadPrivacyByLocale();">
							<s:iterator value="languages" var="lang">
								<option value="<s:property value="#lang.lannguageCode" />"
									<s:if test="#lang.lannguageCode == 'eng'">selected="selected"</s:if>>
									<s:property value="#lang.languageName" />
								</option>
							</s:iterator>
						</select>
					</div>
					<div id="consentHomeLoanDiv" class="privacy-consent-pop-content">Loading
						Privacy Notice...</div>
					<!-- ACCEPT BUTTON -->
					<div style="margin-top: 15px; text-align: center;">
						<button type="button" id="acceptConsentBtn"
							class="btn btn-primary" disabled="disabled"
							onclick="acceptPrivacyConsent();"
							style="opacity: 0.6; cursor: not-allowed;">Accept</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>


	function canOpenPrivacyPopup() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();
		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before viewing privacy notice.");
			if ($("#infoprovide").length > 0) {
				$("#infoprovide").prop("checked", false);
			}
			if ($("#infoprovideCBS").length > 0) {
				$("#infoprovideCBS").prop("checked", false);
			}
			return false;
		}
		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before viewing privacy notice.");
			if ($("#infoprovide").length > 0) {
				$("#infoprovide").prop("checked", false);
			}
			if ($("#infoprovideCBS").length > 0) {
				$("#infoprovideCBS").prop("checked", false);
			}
			return false;
		}
		return true;
	}

	function loadPrivacyByLocale(locale) {
		if (locale == null) {
			locale = $("#privacyLocaleDropdown").val();
		}
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		$("#infoprovide").prop("checked", false);
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("checked", false);
		}

		$
				.ajax({
					url : "getPrivacyNoticeByLocale",
					type : "POST",
					data : {
						privacyLocale : locale
					},
					success : function(response) {
						var json = JSON.parse(response);

						if (json.status == "success") {
							$("#consentHomeLoanDiv").html(json.privacyNotice);
							$("#consentHomeLoanDiv").scrollTop(0);
							resetConsentScrollValidation();
						} else {
							$("#consentHomeLoanDiv").html(
									"Privacy Notice Not Found");
						}
					},
					error : function() {
						$("#consentHomeLoanDiv").html0(
								"Unable To Load Privacy Notice");
					}
				});
	}

	

	$(document).on("show.bs.modal", "#consentHomeLoan", function(e) {
		if (!canOpenPrivacyPopup()) {
			e.preventDefault();
			return false;
		}
	});
	$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
		$("#quotePrivacyConsentFlag").val("");
		$("#quoteNtbId").val("");
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("checked", false);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("checked", false);
		}
		if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
			$("#privacyLocaleDropdown").val("eng");
			loadPrivacyByLocale("eng");
		} else {
			var firstLocale = $("#privacyLocaleDropdown option:first").val();
			loadPrivacyByLocale(firstLocale);
		}
	});

	/* 	$(document).on("shown.bs.modal", "#consentHomeLoan", function() {
	 if ($("#privacyLocaleDropdown option[value='eng']").length > 0) {
	 $("#privacyLocaleDropdown").val("eng");
	 loadPrivacyByLocale("eng");
	 } else {
	 var firstLocale = $("#privacyLocaleDropdown option:first").val();
	 loadPrivacyByLocale(firstLocale);
	 }
	 });
	 */
	/* 	$(document).on("shown.bs.modal", "#consentHomeLoan", function(){
	 loadPrivacyLanguageDropdown();
	 }); */

	$(document).on("scroll", "#consentHomeLoanDiv", function() {
		var div = $(this)[0];
		if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	});

/* 	// for checkbox 
	$(document).on("click", "#acceptConsentBtn", function() {
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("disabled", false).prop("checked", true);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
		}
		var consentModalEl = document.getElementById("consentHomeLoan");
		var consentModal = bootstrap.Modal.getInstance(consentModalEl);
		if (consentModal != null) {
			consentModal.hide();
		} else {
			$("#consentHomeLoan").modal("hide");
		}
	}); */

/* 	function resetConsentScrollValidation() {
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});

		$("#consentHomeLoanDiv").off("scroll").on("scroll", function() {
			var div = $(this);

			if (div.scrollTop() + div.innerHeight() >= this.scrollHeight - 5) {
				$("#acceptConsentBtn").prop("disabled", false).css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
			}
		});
	} */
	

	function resetConsentScrollValidation() {
		$("#acceptConsentBtn").prop("disabled", true).css({
			"opacity" : "0.6",
			"cursor" : "not-allowed"
		});
		$("#consentHomeLoanDiv").off("scroll").on("scroll", function() {
			var div = $(this)[0];
			if (div.scrollTop + div.clientHeight >= div.scrollHeight - 5) {
				$("#acceptConsentBtn").prop("disabled", false).css({
					"opacity" : "1",
					"cursor" : "pointer"
				});
			}
		});
		var div = $("#consentHomeLoanDiv")[0];
		if (div && div.scrollHeight <= div.clientHeight + 5) {
			$("#acceptConsentBtn").prop("disabled", false).css({
				"opacity" : "1",
				"cursor" : "pointer"
			});
		}
	}

	
/* 	function acceptPrivacyConsent() {

		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();

		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before accepting consent.");
			return false;
		}

		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before accepting consent.");
			return false;
		}

		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");

		console.log("cleanMobile GENERATED : ", cleanMobile);
		console.log("cleanDob GENERATED : ", cleanDob);
		console.log("loanTypeId  GENERATED : ", loanTypeId);
		var ntbId = cleanMobile + cleanDob + loanTypeId;

		console.log("NTBID GENERATED : ", ntbId);

		$("#quotePrivacyConsentFlag").val("Y");
		$("#quoteNtbId").val(ntbId);

		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("disabled", false).prop("checked", true);
		}

		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
		}

		$("#consentHomeLoan").modal("hide");
	}
 */
 

	function acceptPrivacyConsent() {
		var mobile = $("#mobile").val();
		var dob = $("#date_of_birth").val();
		if (mobile == null || $.trim(mobile) == "") {
			alert("Please enter mobile number before accepting consent.");
			return false;
		}
		if (dob == null || $.trim(dob) == "") {
			alert("Please enter date of birth before accepting consent.");
			return false;
		}
		var div = $("#consentHomeLoanDiv")[0];
		if (div && div.scrollTop + div.clientHeight < div.scrollHeight - 5) {
			alert("Please read the privacy notice till the end before accepting.");
			return false;
		}
		var cleanMobile = $.trim(mobile).replace(/\D/g, "");
		var cleanDob = $.trim(dob).replace(/\D/g, "");
		var ntbId = cleanMobile + cleanDob + loanTypeId;
		$("#quotePrivacyConsentFlag").val("Y");
		$("#quoteNtbId").val(ntbId);
		if ($("#infoprovide").length > 0) {
			$("#infoprovide").prop("disabled", false).prop("checked", true);
		}
		if ($("#infoprovideCBS").length > 0) {
			$("#infoprovideCBS").prop("disabled", false).prop("checked", true);
		}
		$("#consentHomeLoan").modal("hide");
	}
	function loadPrivacyLanguageDropdown() {
		$
				.ajax({
					url : "getPrivacyLanguageList",
					type : "POST",
					success : function(response) {
						var json = typeof response === "string" ? JSON
								.parse(response) : response;
						if (json.status == "success") {
							var optionHtml = "";
							$
									.each(
											json.languageList,
											function(index, item) {
												optionHtml += "<option value='" + item.locale+ "'>"+ item.languageName + "</option>";
											});
							$("#privacyLocaleDropdown").html(optionHtml);
							$("#privacyLocaleDropdown").val("eng");
							loadPrivacyByLocale("eng");
						} else {
							$("#privacyLocaleDropdown").html(
									"<option value='eng'>English</option>");
							loadPrivacyByLocale("eng");
						}
					},
					error : function(xhr) {
						console.log("getPrivacyLanguageList failed:",
								xhr.status, xhr.responseText);
						$("#privacyLocaleDropdown").html(
								"<option value='eng'>English</option>");
						loadPrivacyByLocale("eng");
					}
				});
	}
</script>

  SBI/WebContent/appNew/loan/homeloan/HomeFirstPageSession.jsp 
+
17
−
5
@@ -184,13 +184,13 @@
		<ul class="form-section">
			<li class="full-width">
				<div class="trms-section">
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on">
					<!-- <input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on"> -->
					<!-- // checkbox disabled for NTB HL -->
					<input type="checkbox" class="blue-css-checkbox" name="infoprovide" id="infoprovide" value="on" disabled="disabled">
					<label for="infoprovide" class="label-content" style="font-size:14px; ">
						
						I/We certify that the information and particulars provided by me/us in this application form (and all documents referred or provided herewith) are true, correct, 
						complete and up to date in all respects and I have not withheld any information. I/ We authorize State Bank of India to make inquiries related to or verify said 
						information directly or through any third party. 
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> Read more </a></b><b class="req">*</b>
						I hereby authroize State Bank of India and/or its representative to contact me with reference to my application”. For more details please read  
						<b><a href="javascript:void(0);" onclick="javascript:openPopups('consentHomeLoan','1');"> SBI’s Privacy Notice </a></b><b class="req">*</b>
					</label>
				</div>
				<div> </div>
@@ -203,6 +203,18 @@
		 </ul>
	</div>
 <script type="text/javascript">
/*  $(document).on("click", "#subtn", function(){

		if($("#quotePrivacyConsentFlag").val() != "Y"){
			alert("Please read and accept SBI Privacy Notice before proceeding.");
			return false;
		}

		if($.trim($("#quoteNtbId").val()) == ""){
			alert("Invalid consent details. Please accept SBI Privacy Notice again.");
			return false;
		}
	}); */
		function showCrossBtn() {
			  var cross = document.getElementById("cross_btn");
			  var plus = document.getElementById("cross_btn1");
  SBI/WebContent/appNew/loan/homeloan/HomeRightContent.jsp 
+
2
−
0
@@ -37,6 +37,8 @@
					<form name="homeloanform" id="homeloanform" method="post" action="javascript:void(0);" enctype="application/x-www-form-urlencoded" 
 						style="display :<s:property value="%{showForm?'block':'none'}"/>;">
 						<s:hidden name="csrfTokenVal" id="csrfTokenVal" value="%{#session.csrfTokenVal}" />
 						<s:hidden name="quote.quotePrivacyConsentFlag" id="quotePrivacyConsentFlag" />
						<s:hidden name="quote.quoteNtbId" id="quoteNtbId" />
						<s:include value="/appNew/loan/homeloan/HomeFirstPageSession.jsp"></s:include>
					</form>
				</div>
  SBI/src/com/mintstreet/common/service/CommonService.java 
+
31
−
0
@@ -138,7 +138,9 @@ import com.mintstreet.common.util.Constants;
import com.mintstreet.common.util.SbiUtil;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.dao.CcmsConfigDao;
import com.mintstreet.consent.dao.MasterLanguageDao;
import com.mintstreet.consent.dao.PrivacyNoticeDao;
import com.mintstreet.consent.dao.PrivacyRequestResponseDao;
import com.mintstreet.consent.entity.CCMSConfig;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
@@ -310,7 +312,9 @@ public class CommonService {

  private CcmsConfigDao ccmsConfigDao;
  
  private PrivacyRequestResponseDao privacyRequestResponseDao;
  
  private MasterLanguageDao masterLanguageDao;
  


@@ -1962,6 +1966,7 @@ public CRMNextLogDao getCrmNextLogDao() {
		 this.privacyNoticeDao = privacyNoticeDao;
	 }
	 

	 public boolean getLanguageBylannguageCode(String lannguageCode) throws NoResultException, SQLException {
		   
		   Integer count = (Integer) this.privacyNoticeDao.getLanguageBylannguageCode(lannguageCode);
@@ -1987,6 +1992,32 @@ public CRMNextLogDao getCrmNextLogDao() {
			return application;
	 }

	public PrivacyRequestResponseDao getPrivacyRequestResponseDao() {
		return privacyRequestResponseDao;
	}


	public void setPrivacyRequestResponseDao(PrivacyRequestResponseDao privacyRequestResponseDao) {
		this.privacyRequestResponseDao = privacyRequestResponseDao;
	}
	
	//
	public PrivacyRequestResponse getPrivacyByLocale(String locale) {
		return privacyRequestResponseDao.getPrivacyByLocale(locale);
	}

	public MasterLanguageDao getMasterLanguageDao() {
		return masterLanguageDao;
	}
	public void setMasterLanguageDao(MasterLanguageDao masterLanguageDao) {
		this.masterLanguageDao = masterLanguageDao;
	}



	public List<MasterLanguage> getAllActiveLanguages() {
		return masterLanguageDao.getAllActiveLanguages();
	}

	
}
  SBI/src/com/mintstreet/consent/dao/MasterLanguageDao.java  0 → 100644
+
28
−
0
package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.MasterLanguage;

public class MasterLanguageDao extends GenericDao<Integer, MasterLanguage> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(MasterLanguageDao.class.getName());

	@SuppressWarnings("unchecked")
	public List<MasterLanguage> getAllActiveLanguages() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<MasterLanguage> languageList = (List<MasterLanguage>) findByNamedQueryRaw(
					"MasterLanguage.getAllActiveLanguages", params);
			logger.info("Active language count : " + (languageList == null ? 0 : languageList.size()));
			return languageList;
		} catch (Exception e) {
			logger.info("Exception in getAllActiveLanguages", e);
		}
		return null;
	}
}
\ No newline at end of file
  SBI/src/com/mintstreet/consent/dao/PrivacyRequestResponseDao.java  0 → 100644
+
29
−
0
package com.mintstreet.consent.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mintstreet.common.dao.GenericDao;
import com.mintstreet.consent.entity.PrivacyRequestResponse;


public class PrivacyRequestResponseDao extends GenericDao<Integer, PrivacyRequestResponse> {
	private static final Logger logger = LogManager.getLogger(PrivacyRequestResponseDao.class.getName());
	private static final long serialVersionUID = 1L;

	public PrivacyRequestResponse getPrivacyByLocale(String locale) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("privacyLocale", locale);
		params.put("privacyIsActive", "Y");
		try {
			PrivacyRequestResponse result = (PrivacyRequestResponse) getSingleResult("PrivacyRequestResponse.getPrivacyByLocale", params);
			logger.info("Successfully retrieved PrivacyRequestResponse for locale: {}", locale);
			return result;
		} catch (Exception e) {
			logger.info("Exception in getPrivacyByLocale", e);
		}
		return null;
	}
}
\ No newline at end of file
  SBI/src/com/mintstreet/consent/entity/MasterLanguage.java 
+
11
−
1
@@ -14,7 +14,17 @@ import javax.persistence.Table;
@Table(name = "RUPEEPOWER_OCAS_T_13704")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@NamedQueries({
	@NamedQuery(name="MasterLanguage.getLanguageBylannguageCode", query = "select count(l) from MasterLanguage l where l.isActive ='Y' AND l.lannguageCode=:lannguageCode"), })
	@NamedQuery(name="MasterLanguage.getLanguageBylannguageCode", query = "select count(l) from MasterLanguage l where l.isActive ='Y' AND l.lannguageCode=:lannguageCode"), 
	@NamedQuery(
			name = "MasterLanguage.getAllActiveLanguages",
			query = "SELECT l FROM MasterLanguage l "
				+ "WHERE l.isActive = 'Y' "
				+ "AND l.lannguageCode IS NOT NULL "
				+ "AND l.languageName IS NOT NULL "
				+ "ORDER BY l.langID"
		)
})




  SBI/src/com/mintstreet/consent/entity/PrivacyRequestResponse.java 
+
7
−
5
@@ -21,7 +21,13 @@ import javax.persistence.Table;
@NamedQueries({
	@NamedQuery(name="PrivacyRequestResponse.findAll", query="SELECT a FROM PrivacyRequestResponse a"),
	@NamedQuery(name="PrivacyRequestResponse.getPrivacyRequestResponseByPrivacyLocale", 
	query = "Select a from PrivacyRequestResponse a where a.privacyIsActive='Y' and a.privacyLocale =:privacyLocale"),
				query = "Select a from PrivacyRequestResponse a where a.privacyIsActive='Y' and a.privacyLocale =:privacyLocale"),
	@NamedQuery(
			name="PrivacyRequestResponse.getPrivacyByLocale",
			query="SELECT p FROM PrivacyRequestResponse p "
				+ "WHERE p.privacyLocale=:privacyLocale "
				+ "AND p.privacyIsActive=:privacyIsActive"
		),
})

public class PrivacyRequestResponse extends com.mintstreet.common.entity.Domain<Integer> implements Serializable {
@@ -184,7 +190,6 @@ public class PrivacyRequestResponse extends com.mintstreet.common.entity.Domain<
	public void setPrivacyIsActive(String privacyIsActive) {
		this.privacyIsActive = privacyIsActive;
	}

	@Override
	public String toString() {
		return "PrivacyRequestResponse [privacyId=" + privacyId + ", privacyTouchPointId=" + privacyTouchPointId
@@ -195,7 +200,4 @@ public class PrivacyRequestResponse extends com.mintstreet.common.entity.Domain<
				+ privacyMessage + ", privacyErrorMsg=" + privacyErrorMsg + ", privacyIsActive=" + privacyIsActive
				+ "]";
	}

	

}
  SBI/src/com/mintstreet/loan/homeloan/action/HomeLoanAction.java 
+
148
−
3
@@ -9,6 +9,7 @@ import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@@ -54,6 +55,8 @@ import com.mintstreet.common.util.DateUtil;
import com.mintstreet.common.util.RequestUtil;
import com.mintstreet.common.util.Security;
import com.mintstreet.common.validation.ValidatorUtil;
import com.mintstreet.consent.entity.MasterLanguage;
import com.mintstreet.consent.entity.PrivacyRequestResponse;
import com.mintstreet.loan.homeloan.bo.impl.HomeProcessManagerImpl;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoan;
import com.mintstreet.loan.homeloan.entity.ApplicationFormHomeLoanQuote;
@@ -85,6 +88,8 @@ public class HomeLoanAction extends BaseAction {

	private ApplicationFormHomeLoan appForm;
	private ApplicationFormHomeLoanQuote quote;
	private String privacyLocale;
	private List<MasterLanguage> languages;

	public JSONArray initLoanJSONArrayHomeLoan;
	public String jsonJSArray1HomeLoan;
@@ -3255,9 +3260,17 @@ public class HomeLoanAction extends BaseAction {
			
				//customer consent
				//String consentText = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID).getConsentText();
				Integer consentId = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID).getConsentId();
				String consentText = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentId);
				beanList.setConsentHomeLoan(consentText);
//				Integer consentId = commonService.getConsentByLoanType(Constants.HOME_LOAN_ID).getConsentId();
//				String consentText = commonService.getClobdata("RUPEEPOWER_OCAS_T_13689", "CONSENT_TEXT", "CONSENT_ID", consentId);
//				beanList.setConsentHomeLoan(consentText);
			languages = commonService.getAllActiveLanguages();
			logger.info("Privacy dropdown language count : " + (languages == null ? 0 : languages.size()));
			PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale("eng");
			if (privacyObj != null) {
				beanList.setConsentHomeLoan(privacyObj.getPrivacyNotice());
			} else {
				beanList.setConsentHomeLoan("Privacy Notice Not Available");
			}
				
				//read Consent from CCMS
				//consentUtil.callCCMSConsentReadAPI();
@@ -3275,6 +3288,126 @@ public class HomeLoanAction extends BaseAction {
		}
	}
	
	public StreamResult getPrivacyNoticeByLocale() {
		JSONObject json = new JSONObject();
		try {
			/*
			 * Default English
			 */
			if (privacyLocale == null || "".equals(privacyLocale)) {
				privacyLocale = "eng";
			}
			PrivacyRequestResponse privacyObj = commonService.getPrivacyByLocale(privacyLocale);
			if (privacyObj != null) {
				json.put("status", "success");
				json.put("privacyNotice", privacyObj.getPrivacyNotice());
			} else {
				json.put("status", "fail");
				json.put("privacyNotice", "Privacy Notice Not Found");
			}
		} catch (Exception e) {
			try {
				json.put("status", "fail");
				json.put("privacyNotice", "Unable To Load Privacy Notice");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	public StreamResult getPrivacyLanguageList() {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			List<MasterLanguage> langList = commonService.getAllActiveLanguages();
			if (langList != null && !langList.isEmpty()) {
				for (MasterLanguage lang : langList) {
					JSONObject langObj = new JSONObject();
					langObj.put("locale", lang.getLannguageCode());
					langObj.put("languageName", lang.getLanguageName());
					array.put(langObj);
				}
				json.put("status", "success");
				json.put("languageList", array);
			} else {
				json.put("status", "fail");
				json.put("languageList", array);
			}
		} catch (Exception e) {
			logger.info("Exception in getPrivacyLanguageList", e);
			try {
				json.put("status", "fail");
				json.put("languageList", array);
			} catch (JSONException je) {
				logger.info("JSONException in getPrivacyLanguageList", je);
			}
		}
		return new StreamResult(new ByteArrayInputStream(json.toString().getBytes()));
	}
	public StreamResult savePrivacyConsent() {
		JSONObject json = new JSONObject();
		try {
			Integer appSeqId = SessionUtil.getHomeLoanApplicationSequenceId();
			if (appSeqId == null) {
				json.put("status", "fail");
				json.put("message", "Session expired. Application not found.");
			} else {
				ApplicationFormHomeLoan appForm =
						homeLoanService.getApplicationFormHomeLoanByAppSeqId(appSeqId);
				if (appForm == null) {
					json.put("status", "fail");
					json.put("message", "Application not found.");
				} else {
					String ntbId = generateHomeLoanNtbId(appForm);
					appForm.setAppPrivacyConsentFlag("Y");
					appForm.setAppNtbId(ntbId);
					//appForm.setAppPrivacyConsentTime(new Date());
					homeLoanService.save(appForm);
					json.put("status", "success");
					json.put("message", "Consent saved successfully.");
					json.put("ntbId", ntbId);
				}
			}
		} catch (Exception e) {
			logger.info("Exception in savePrivacyConsent", e);
			try {
				json.put("status", "fail");
				json.put("message", "Unable to save consent.");
			} catch (JSONException je) {
				logger.info("JSONException in savePrivacyConsent", je);
			}
		}
		return new StreamResult(
				new ByteArrayInputStream(json.toString().getBytes())
		);
	}
	private void loadPrivacyLanguages() {
		try {
			languages = commonService.getAllActiveLanguages();
			logger.info("Privacy language dropdown count : "
					+ (languages == null ? 0 : languages.size()));
		} catch (Exception e) {
			logger.info("Exception in loadPrivacyLanguages", e);
		}
	}
	private String generateHomeLoanNtbId(ApplicationFormHomeLoan appForm) {
		String mobile = appForm.getAppMobileNo() != null
				? appForm.getAppMobileNo()
				: "";
		String dob = "";
		try {
			if (appForm.getAppDobDT() != null) {
				dob = new SimpleDateFormat("ddMMyyyy")
						.format(appForm.getAppDobDT());
			}
		} catch (Exception e) {
			logger.info("Exception while formatting DOB for NTB ID", e);
		}
		String loanType = String.valueOf(Constants.HOME_LOAN_ID);
		logger.info("HomeLoanAction.java ::  NTB ID ----:  ", mobile + dob + loanType);
		return mobile + dob + loanType;
	}

	public StreamResult getAllProject() {
		JSONObject json = new JSONObject();
@@ -3386,4 +3519,16 @@ public class HomeLoanAction extends BaseAction {
		this.tenure4Emi = tenure4Emi;
	}
		
	public String getPrivacyLocale() {
		return privacyLocale;
	}
	public void setPrivacyLocale(String privacyLocale) {
		this.privacyLocale = privacyLocale;
	}
	public List<MasterLanguage> getLanguages() {
		return languages;
	}
	public void setLanguages(List<MasterLanguage> languages) {
		this.languages = languages;
	}
}
  SBI/src/com/mintstreet/loan/homeloan/bo/impl/HomeProcessManagerImpl.java 
+
11
−
0
@@ -1298,6 +1298,17 @@ public class HomeProcessManagerImpl {
      quote.setLoanQuoteVisitId(Integer.valueOf(oldVisitId));
      quote.setLoanQuoteNewVisitId(trackVisitId);
      quote.setLoanQuoteBrowserName(CommonUtilites.getBrowserName());
      if (!"Y".equalsIgnoreCase(quote.getQuotePrivacyConsentFlag())) {
    		loanScenarioBean.setStatus(Integer.valueOf(0));
    		loanScenarioBean.setMessage("Please read and accept SBI Privacy Notice before proceeding.");
    		return loanScenarioBean;
    	}

    	if (!ValidatorUtil.isValid(quote.getQuoteNtbId())) {
    		loanScenarioBean.setStatus(Integer.valueOf(0));
    		loanScenarioBean.setMessage("Invalid consent details. Please accept SBI Privacy Notice again.");
    		return loanScenarioBean;
    	}
      if (quote.getLoanQuoteProjectId() != null) {
        MasterProject masterProject = this.homeLoanService.findProjectById(quote.getLoanQuoteProjectId());
        if (masterProject != null)
  SBI/src/com/mintstreet/loan/homeloan/entity/ApplicationFormHomeLoan.java 
+
46
−
1
@@ -50,7 +50,7 @@ public class ApplicationFormHomeLoan extends com.mintstreet.common.entity.Domain
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "G2")
	@Column(name = "APP_SEQ_ID")
	private Integer appSeqId;

	
	@Column(name = "APP_QUOTE_ID")
	private Integer appQuoteId;

@@ -840,6 +840,18 @@ public class ApplicationFormHomeLoan extends com.mintstreet.common.entity.Domain
	@Column(name="APP_CONSENT_ID")
	private Integer appConsentId;
	
	@Column(name="APP_PRIVACY_CONSENT_FLAG")
	private String appPrivacyConsentFlag;

	@Column(name="APP_NTB_ID")
	private String appNtbId;
	
	@Column(name="APP_IP_ADDRESS")
	private String loanAppIpAddress;
	
	@Column(name = "APP_PRIVACY_LOCALE")
	private String appPrivacyLocale;
	
	public String getOcasID() {
		return ocasID;
	}
@@ -2804,5 +2816,38 @@ public class ApplicationFormHomeLoan extends com.mintstreet.common.entity.Domain
	public void setAppConsentId(Integer appConsentId) {
		this.appConsentId = appConsentId;
	}
	
	public String getAppPrivacyConsentFlag() {
		return appPrivacyConsentFlag;
	}

	public void setAppPrivacyConsentFlag(String appPrivacyConsentFlag) {
		this.appPrivacyConsentFlag = appPrivacyConsentFlag;
	}

	public String getAppNtbId() {
		return appNtbId;
	}

	public void setAppNtbId(String appNtbId) {
		this.appNtbId = appNtbId;
	}
	
	public String getLoanAppIpAddress() {
		return loanAppIpAddress;
	}

	public void setLoanAppIpAddress(String loanAppIpAddress) {
		this.loanAppIpAddress = loanAppIpAddress;
	}

	public String getAppPrivacyLocale() {
		return appPrivacyLocale;
	}

	public void setAppPrivacyLocale(String appPrivacyLocale) {
		this.appPrivacyLocale = appPrivacyLocale;
	}


}
  SBI/src/com/mintstreet/loan/homeloan/entity/ApplicationFormHomeLoanQuote.java 
+
33
−
1
@@ -587,6 +587,16 @@ public class ApplicationFormHomeLoanQuote extends Domain<Integer> implements Ser
	@Transient
	private Integer loanQuoteConsentId;
	
	//For Privacy Concent Ntb
	@Column(name = "QUOTE_PRIVACY_CONSENT_FLAG")
	private String quotePrivacyConsentFlag;

	@Column(name = "QUOTE_NTB_ID")
	private String quoteNtbId;
	
	@Column(name = "QUOTE_PRIVACY_LOCALE")
	private String quotePrivacyLocale;
	
	public Double getLoanQuoteMarketValueOfProperty() {
		return loanQuoteMarketValueOfProperty;
	}
@@ -2043,5 +2053,27 @@ public class ApplicationFormHomeLoanQuote extends Domain<Integer> implements Ser
		this.loanQuoteConsentId = loanQuoteConsentId;
	}
	
   
	public String getQuotePrivacyConsentFlag() {
		return quotePrivacyConsentFlag;
	}

	public void setQuotePrivacyConsentFlag(String quotePrivacyConsentFlag) {
		this.quotePrivacyConsentFlag = quotePrivacyConsentFlag;
	}

	public String getQuoteNtbId() {
		return quoteNtbId;
	}

	public void setQuoteNtbId(String quoteNtbId) {
		this.quoteNtbId = quoteNtbId;
	}
	
	public String getQuotePrivacyLocale() {
		return quotePrivacyLocale;
	}

	public void setQuotePrivacyLocale(String quotePrivacyLocale) {
		this.quotePrivacyLocale = quotePrivacyLocale;
	}
}
  SBI/src/com/mintstreet/loan/homeloan/util/HomeLoanHelper.java 
+
8
−
0
@@ -678,6 +678,14 @@ public class HomeLoanHelper  {
		    if (quote.getLoanQuoteConsentId() != null && application.getAppConsentId() == null) {
		    	application.setAppConsentId(quote.getLoanQuoteConsentId());
		    }
		    //set flag for consent - NTB 
		    if (quote.getQuotePrivacyConsentFlag() != null && application.getAppPrivacyConsentFlag() == null) {
		    	application.setAppPrivacyConsentFlag(quote.getQuotePrivacyConsentFlag());
		    }
		    
		    if (quote.getQuoteNtbId() != null && application.getAppNtbId() == null) {
		    	application.setAppNtbId(quote.getQuoteNtbId());
		    }
		    
		    application.setOcasID(SbiUtil.getOcasID());
			application = homeLoanService.save(application);
  SBI/src/struts-home-loan-actions.xml 
+
18
−
0
@@ -75,6 +75,24 @@
			</result>
			<result name="cbsVerifySmsOtpPageOld">/app/common/commonCbsVarifySmsOtp.jsp</result>
		</action>
		<action name="getPrivacyNoticeByLocale" class="homeLoanAction"
		method="getPrivacyNoticeByLocale">
		<result type="stream">
			<param name="contentType">application/json</param>
		</result>
	</action>
	<action name="getPrivacyLanguageList" class="homeLoanAction"
		method="getPrivacyLanguageList">
		<result type="stream">
			<param name="contentType">application/json</param>
		</result>
	</action>
	<action name="savePrivacyConsent" class="homeLoanAction"
		method="savePrivacyConsent">
		<result type="stream">
			<param name="contentType">application/json</param>
		</result>
	</action>
		<action name="getAllProject" method="getAllProject" class="homeLoanAction">
			<result name="success" type="json">
       	 		<param name="root">json</param>
Write a comment or drag your files here…
State Bank of India - OCAS Source Repository
