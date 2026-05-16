<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{agriLoanPage==0}">
<li>
	<label>Photograph<span id="uploadStatus1" class="<s:property value="%{#appForm!=null && #appForm.appPhotoIdName!=null?'success-icon':''}"/>"></span></label>
	<s:hidden name="appForm.appPhotoIdName" id="docUrl_1" value="%{#appForm.appPhotoIdName}"/>
	<s:hidden name="thumbsSelectedId" id="thumbsSelectedId" value="1"/>
	<div class="flat-field">
		<s:select data-rel="docUrl_1" list="#{'1':'Photograph'}" name="appForm.appPhotoId" id="appPhotoId" 
	 		cssClass="form-select"	value="%{#appForm.appPhotoId}" headerKey="0" headerValue="Select photograph" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
	</div> 
	<div id="doc_1" class="doc-upload-div"></div>
	<div id="editDeleteButton_1"  style="display: <s:property value="%{#appForm!=null && #appForm.appPhotoIdName!=null?'block':'none'}"/>;">
		<a id="remove_1" class="delete-icon" href="javascript:removePhoto(1);">&nbsp;</a>	
	</div>
	<span></span>
</li>
<li>
	<label>Identity proof<span id="uploadStatus2" class="<s:property value="%{#appForm!=null && #appForm.appIdentityProofName!=null?'success-icon':''}"/>"></span></label>
	<s:hidden name="appForm.appIdentityProofName" id="docUrl_2" value="%{#appForm.appIdentityProofName}"/>
	<div class="flat-field">
		<s:select data-rel="docUrl_2" list="%{#documentTypeList.identityProof!=null?#documentTypeList.identityProof:''}" name="appForm.appIdentityProofId" id="appIdentityProofId" 
	 		cssClass="form-select"	value="%{#appForm.appIdentityProofId}" headerKey="0" headerValue="Select identity proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
	</div> 
	<!-- <a id="doc_2" href="javascript:void(0);">Upload</a> -->
	<div id="doc_2" class="doc-upload-div"></div>
	<div id="editDeleteButton_2"  style="display: <s:property value="%{#appForm!=null && #appForm.appIdentityProofName!=null?'block':'none'}"/>;">
		<a id="remove_2" class="delete-icon" href="javascript:removePhoto(2);">&nbsp;</a>	
	</div>
	<span></span>
</li>
<li>
	<label>Residence proof<span id="uploadStatus3" class="<s:property value="%{#appForm!=null && #appForm.appResidenceProofName!=null?'success-icon':''}"/>"></span></label>
	<s:hidden name="appForm.appResidenceProofName" id="docUrl_3" value="%{#appForm.appResidenceProofName}"/>
	<div class="flat-field">
		<s:select data-rel="docUrl_3" list="%{#documentTypeList.residanceProof!=null?#documentTypeList.residanceProof:''}" name="appForm.appResidenceProofId" id="appResidenceProofId" 
	 		cssClass="form-select" value="%{#appForm.appResidenceProofId}" headerKey="0" headerValue="Select residence proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
	</div> 
	<div id="doc_3" class="doc-upload-div"></div>
	<div id="editDeleteButton_3"  style="display: <s:property value="%{#appForm!=null && #appForm.appResidenceProofName!=null?'block':'none'}"/>;">
		<a id="remove_3" class="delete-icon" href="javascript:removePhoto(3);">&nbsp;</a>	
	</div>
	<span></span>
</li>
<li>
	<label>Income proof<span id="uploadStatus4" class="<s:property value="%{#appForm!=null && #appForm.appIncomeProofName!=null?'success-icon':''}"/>"></span></label>
	<s:hidden name="appForm.appIncomeProofName" id="docUrl_4" value="%{#appForm.appIncomeProofName}"/>
	<div class="flat-field">
		<s:select data-rel="docUrl_4" list="%{#documentTypeList.incomeProof!=null?#documentTypeList.incomeProof:''}" name="appForm.appIncomeProofId" id="appIncomeProofId" 
 			cssClass="form-select"	value="%{#appForm.appIncomeProofId}" headerKey="0" headerValue="Select income proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
	</div> 
	<div id="doc_4" class="doc-upload-div"></div>
	<div id="editDeleteButton_4"  style="display: <s:property value="%{#appForm!=null && #appForm.appIncomeProofName!=null?'block':'none'}"/>;">
		<a id="remove_4" class="delete-icon" href="javascript:removePhoto(4);">&nbsp;</a>	
	</div>
	<span></span>
</li>
<s:if test="%{educationLoanPage>0}">
	<li>
		<label>Academic proof<span id="uploadStatus5" class="<s:property value="%{#appForm!=null && #appForm.appAcademicProofName!=null?'success-icon':''}"/>"></span></label>
		<s:hidden name="appForm.appAcademicProofName" id="docUrl_5" value="%{#appForm.appAcademicProofName}"/>
		<div class="flat-field">
			<s:select data-rel="docUrl_5" list="%{#documentTypeList.academicProof!=null?#documentTypeList.academicProof:''}" name="appForm.appAcademicProofId" id="appAcademicProofId" 
	 			cssClass="form-select"	value="%{#appForm.appAcademicProofId}" headerKey="0" headerValue="Select academic proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
		</div> 
		<div id="doc_5" class="doc-upload-div"></div>
		<div id="editDeleteButton_5"  style="display: <s:property value="%{#appForm!=null && #appForm.appAcademicProofName!=null?'block':'none'}"/>;">
			<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);">&nbsp;</a>	
		</div>
		<span></span>
	</li>
</s:if>
<s:elseif test="%{autoLoanPage>0}">
	<li>
		<label>Car quote<span id="uploadStatus5" class="<s:property value="%{#appForm!=null && #appForm.appAutoDetailsProofName!=null?'success-icon':''}"/>"></span></label>
		<s:hidden name="appForm.appAutoDetailsProofName" id="docUrl_5" value="%{#appForm.appAutoDetailsProofName}"/>
		<div class="flat-field">
			<s:select data-rel="docUrl_5" list="%{#documentTypeList.autoDetailsProof!=null?#documentTypeList.autoDetailsProof:''}" name="appForm.appAutoDetailsProofId" id="appAutoDetailsProofId" 
		 		cssClass="form-select"	value="%{#appForm.appAutoDetailsProofId}" headerKey="0" headerValue="Select car quotation" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
		</div> 
		<div id="doc_5" class="doc-upload-div"></div>
		<div id="editDeleteButton_5"  style="display: <s:property value="%{#appForm!=null && #appForm.appAutoDetailsProofName!=null?'block':'none'}"/>;">
			<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);">&nbsp;</a>	
		</div>
		<span></span>
	</li>
</s:elseif>
<s:elseif test="%{personalLoanPage>0}">
 	<s:if test="%{(#appForm.appPersonalLoanId==3 || #appForm.appPersonalLoanId==4)}">
	 	<li>
			<label>Property proof<span id="uploadStatus5" class="<s:property value="%{#appForm!=null && #appForm.appPropertyProofName!=null?'success-icon':''}"/>"></span></label>
			<s:hidden name="appForm.appPropertyProofName" id="docUrl_5" value="%{#appForm.appPropertyProofName}"/>
			<div class="flat-field">
				<s:select data-rel="docUrl_5" list="%{#documentTypeList.propertyProof!=null?#documentTypeList.propertyProof:''}" name="appForm.appPropertyProofId" id="appPropertyProofId" 
		 			cssClass="form-select"	value="%{#appForm.appPropertyProofId}" headerKey="0" headerValue="Select property proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
			</div> 
			<div id="doc_5" class="doc-upload-div"></div>
			<div id="editDeleteButton_5"  style="display: <s:property value="%{#appForm!=null && #appForm.appPropertyProofName!=null?'block':'none'}"/>;">
				<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);">&nbsp;</a>	
			</div>
			<span></span>
		</li>
 	</s:if>
 	<s:elseif test="%{(#appForm.appPersonalLoanId==6 || #appForm.appPersonalLoanId==7 || #appForm.appPersonalLoanId==8)}">
			<li>
				<label>Pension proof<span id="uploadStatus5"class="<s:property value="%{#appForm!=null && #appForm.appPensionProofName!=null?'success-icon':''}"/>"></span></label>
				<s:hidden name="appForm.appPensionProofName" id="docUrl_5" value="%{#appForm.appPensionProofName}"/>
				<div class="flat-field">
					<s:select data-rel="docUrl_5" list="%{#documentTypeList.pensionProof!=null?#documentTypeList.pensionProof:''}" name="appForm.appPensionProofId" id="appPensionProofId" 
		 			cssClass="form-select" value="%{#appForm.appPensionProofId}" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();" headerKey="0" headerValue="Select pension proof"/>
				</div>
				<div id="doc_5" class="doc-upload-div"></div>
				<div id="editDeleteButton_5" style="display: <s:property value="%{#appForm!=null && #appForm.appPensionProofName!=null?'block':'none'}"/>;">
					<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);"> &nbsp;</a>
				</div>
			</li>
		</s:elseif>
</s:elseif>
<s:elseif test="%{homeLoanPage>0}">
	<li>
		<label>Employment proof<span id="uploadStatus5" class="<s:property value="%{#appForm!=null && #appForm.appEmploymentProofName!=null?'success-icon':''}"/>"></span></label>
		<s:hidden name="appForm.appEmploymentProofName" id="docUrl_5" value="%{#appForm.appEmploymentProofName}"/>
		<div class="flat-field">
				<s:select data-rel="docUrl_5" list="%{#documentTypeList.employmentProof!=null?#documentTypeList.employmentProof:''}" name="appForm.appEmploymentProofId" id="appEmploymentProofId" 
		 		cssClass="form-select"	value="%{#appForm.appEmploymentProofId==null?''#appForm.appEmploymentProofId}" headerKey="0" headerValue="Select employment proof" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
		</div> 
		<div id="doc_5" class="doc-upload-div"></div>
		<div id="editDeleteButton_5"  style="display: <s:property value="%{#appForm!=null && #appForm.appEmploymentProofName!=null?'block':'none'}"/>;">
			<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);">&nbsp;</a>	
		</div>
		<span></span>
	</li>
</s:elseif>
</s:if>
<%-- <s:elseif test="%{agriLoanPage>0}">
	<li>
		<label>Land ownership proof<span id="uploadStatus5" class="<s:property value="%{#appForm!=null && #appForm.appLandOwnerProofName!=null?'success-icon':''}"/>"></span></label>
		<s:hidden name="appForm.appLandOwnerProofName" id="docUrl_5" value="%{#appForm.appLandOwnerProofName}"/>
		<div class="flat-field">
			<s:select data-rel="docUrl_5" list="%{#documentTypeList.landDetailsProof!=null?#documentTypeList.landDetailsProof:''}" name="appForm.appLandOwnerProofId" id="appLandOwnerProofId" 
					cssClass="form-select"	value="%{#appForm.appLandOwnerProofId}" headerKey="0" headerValue="Select land ownership" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
		</div> 
		<div id="doc_5" class="doc-upload-div"></div>
		<div id="editDeleteButton_5"  style="display: <s:property value="%{#appForm!=null && #appForm.appLandOwnerProofName!=null?'block':'none'}"/>;">
			<a id="remove_5" class="delete-icon" href="javascript:removePhoto(5);">&nbsp;</a>	
		</div>
		<span></span>
	</li>
	<li>
		<label>Other Documents<span id="uploadStatus6" class="<s:property value="%{#appForm!=null && #appForm.appOtherProofName!=null?'success-icon':''}"/>"></span></label>
		<s:hidden name="appForm.appOtherProofName" id="docUrl_6" value="%{#appForm.appOtherProofName}"/>
		<div class="flat-field">
			<s:select data-rel="docUrl_6" list="%{#documentTypeList.otherDocumentsProof!=null?#documentTypeList.otherDocumentsProof:''}" name="appForm.appOtherProofId" id="appOtherProofId" 
					cssClass="form-select"	value="%{#appForm.appOtherProofId}" headerKey="0" headerValue="Select other document" onfocus="customOnFocus(this);" onchange="isEligibleToUploadDocument();"/>
		</div> 
		<div id="doc_6" class="doc-upload-div"></div>
		<div id="editDeleteButton_6"  style="display: <s:property value="%{#appForm!=null && #appForm.appOtherProofName!=null?'block':'none'}"/>;">
			<a id="remove_6" class="delete-icon" href="javascript:removePhoto(6);">&nbsp;</a>	
		</div>
		<span></span>
	</li>
</s:elseif> --%>
