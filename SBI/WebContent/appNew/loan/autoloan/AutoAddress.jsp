<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<s:if test="%{autoLoanPage==1}">
		<s:if test="%{quote.loanQuoteStateId !=null &&  quote.loanQuoteStateId >0}">
			<li id="divState">
				<label class="">State<b class="req">*</b></label>
				<s:select name="quote.loanQuoteStateId" id="stateId" autocomplete="off" list="%{#states}" cssClass=""
				 value="%{quote.loanQuoteStateId}" headerKey="0" headerValue="Select state "  onchange="javascript: getCities(this);" onfocus="customOnFocus(this);"/><!--  onchange="javascript: getCities(this);-->
			</li>
		</s:if>
		<div id="LOCATIONCITY">
		<s:if test="%{quote.loanQuoteCityId !=null && quote.loanQuoteCityId >0}">
			<div id="divCity">
				<label class="">City<b class="req">*</b></label>
				<s:select list="%{beanList.citiesoptgrp1==null?'':beanList.citiesoptgrp1}" name="quote.loanQuoteCityId" id="cityId" value="%{quote.loanQuoteCityId}"
				 headerKey="0" headerValue="Select city" onchange="javascript: getDistrictsOrBranches(this);" onfocus="customOnFocus(this);">
				<%-- <s:optgroup label="--------------" list="%{beanList.citiesoptgrp2}" /> --%>
				</s:select>
			</div>
			</s:if>
		</div>
		<s:if test="%{quote.loanQuoteCityId!=null && quote.loanQuoteCityId == 9999999}">
			<div id="LOCATIONDISTRICT">
				<s:if test="%{quote.loanQuoteDistrictId!=null && quote.loanQuoteDistrictId >0}">
					<div class="div-left" id="divLocality">
						<label class="">District<b class="req">*</b></label>
						<s:select name="quote.loanQuoteDistrictId" id="districtId" autocomplete="off" list="%{beanList.districts==null?'':beanList.districts}" cssClass=""
						 value="%{quote.loanQuoteDistrictId}" headerKey="0" headerValue="Select district" onchange="javascript: getBranches(this);" onfocus="customOnFocus(this);"/>
					</div>
				</s:if>
			</div>
			<div id="LOCATIONBRANCH">
				<s:if test="%{quote.loanQuoteBranchId!=null &&  quote.loanQuoteBranchId >0}">
					<div class="div-left" id="divDistrict">
						<label class="">Branch <font style="font-size:10px;"><b class="req">*</b> (<a target="_blank" class="mob-branch-link" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FINDER_URL}"/>">Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font></label>
						<s:select name="quote.loanQuoteBranchId" id="branchId" autocomplete="off" list="%{beanList.branches!=null?beanList.branches:''}" cssClass=""
						 value="%{quote.loanQuoteBranchId}" headerKey="0" headerValue="Select branch" onfocus="customOnFocus(this);"/>
					</div>
				</s:if>
			</div>
		</s:if>
		<s:else>
			<div id="LOCATIONBRANCH">
				<s:if test="%{quote.loanQuoteBranchId!=null && quote.loanQuoteBranchId >0}">
					<div class="div-left" id="divDistrict">
						<label class="">Branch <font style="font-size:10px;"><b class="req">*</b> (<a target="_blank" class="mob-branch-link" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FINDER_URL}"/>">Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font></label>
						<s:select name="quote.loanQuoteBranchId" id="branchId" autocomplete="off" list="%{beanList.branches!=null?beanList.branches:''}" cssClass=""
						 value="%{quote.loanQuoteBranchId}" headerKey="0" headerValue="Select branch" onfocus="customOnFocus(this);"/>
					</div>
				</s:if>
			</div>
		</s:else>
	</s:if>
	<s:if test="%{autoLoanPage==3}">
		<div id="divState">
			<label >State<b class="req">*</b></label>
			<div class="flat-field">
				<s:select name="appForm.appStateId" id="appStateId" autocomplete="off" list="%{#states}" cssClass="form-select"
			 	value="%{appForm.appStateId}" headerKey="0" headerValue="Select state" onfocus="customOnFocus(this);"/>
			</div>
		</div>
		<div id="APPLOCATIONCITY">
			<div id="divCity">
				<label >City<b class="req">*</b></label>
				<div class="flat-field">
					<s:if test="%{beanList.citiesoptgrp1!=null}">
						<s:select list="%{beanList.citiesoptgrp1==null?'':beanList.citiesoptgrp1}" name="appForm.appCityId" id="appCityId" value="%{appForm.appCityId}" headerKey="0" headerValue="Select city" cssClass="disabledFields form-select" disabled="true" onfocus="customOnFocus(this);">
						</s:select>
					</s:if>
					<s:else>
						<s:select list="#{''}" name="appForm.appCityId" id="appCityId" value="%{appForm.appCityId}" headerKey="0" headerValue="Select city" cssClass="disabledFields form-select" disabled="true" onfocus="customOnFocus(this);"></s:select>
					</s:else>
				</div>	
			</div>
		</div>
		<s:if test="%{appForm.appCityId!=null && appForm.appCityId == 9999999}">
			<div id="APPLOCATIONDISTRICT">
				<s:if test="%{appForm.appDistrictId!=null && appForm.appDistrictId >0}">
					<div id="divLocality">
						<label>District<b class="req">*</b></label>
						<div class="flat-field">
							<s:select name="appForm.appDistrictId" id="appDistrictId" autocomplete="off" list="%{beanList.districts==null?'':beanList.districts}" cssClass="form-select"
							 value="%{appForm.appDistrictId}" headerKey="0" headerValue="Select district" onfocus="customOnFocus(this);"/>
						</div>
					</div>
				</s:if>
			</div>
			<s:if test="%{quote.loanQuoteResidentTypeId==2}">
				<div id="APPLOCATIONBRANCH">
					<s:if test="%{appForm.appBranchId!=null && appForm.appBranchId >0}">
						<div id="divDistrict">
							<label>Branch <font style="font-size:10px;"><b class="req">*</b> (<a target="_blank" class="mob-branch-link" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FINDER_URL}"/>">Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font></label>
							<div class="flat-field">
								<s:select name="appForm.appBranchId" id="appBranchId" autocomplete="off" list="%{beanList.branches!=null?beanList.branches:''}" cssClass="form-select"
								 value="%{appForm.appBranchId}" headerKey="0" headerValue="Select branch" onfocus="customOnFocus(this);"/>
							 </div>
						</div>
					</s:if>
				</div>
			</s:if>
		</s:if>
		<s:else>
			<s:if test="%{quote.loanQuoteResidentTypeId==2}">
				<div class="" id="APPLOCATIONBRANCH">
					<s:if test="%{appForm.appBranchId!=null && appForm.appBranchId >0}">
						<div id="divDistrict">
							<label class="">Branch <font style="font-size:10px;"><b class="req">*</b> (<a target="_blank" class="mob-branch-link" href="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_FINDER_URL}"/>">Locate nearest <s:property value="%{@com.mintstreet.common.util.Constants@BANK_NAME}"/> branch</a>) </font></label>
							<div class="flat-field">
								<s:select name="appForm.appBranchId" id="appBranchId" autocomplete="off" list="%{beanList.branches!=null?beanList.branches:''}" cssClass="form-select"
								 value="%{appForm.appBranchId}" headerKey="0" headerValue="Select branch" onfocus="customOnFocus(this);"/>
							</div>
						</div>
					</s:if>
				</div>
			</s:if>
		</s:else>
	</s:if>
<script type="text/javascript">
<s:if test="%{autoLoanPage==3 && quote.loanQuoteResidentTypeId==1}">
disableAndRemoveErrorById('autoloancriteriaform','appStateId',true);
disableAndRemoveErrorById('autoloancriteriaform','appCityId',true);
disableAndRemoveErrorById('autoloancriteriaform','appDistrictId',true);
disableAndRemoveErrorById('autoloancriteriaform','appBranchId',true); 
</s:if>
<s:else>
	<s:if test="%{autoLoanPage==3}">
	disableAndRemoveErrorById('autoloancriteriaform','appCityId',true);
	</s:if>
	<s:else>
	</s:else>
</s:else>
</script>
