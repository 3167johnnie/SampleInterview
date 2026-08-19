<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

 <s:include value="/appNew/common/ConsentPopupETB.jsp"></s:include> 

<div class="clearfix"></div>
	<div class="clearfix"></div>
	
	<h3 style="text-align:centre">Consent Collection/ Modification/ Withdrawal Form</h3>
	<h3>Account Holder Details</h3>
	
	<ul class="form-section">
		<li><label>Full Name</label><s:textfield name="dpfullName" id="dpfullName" value="%{quote.loanQuoteAppFirstName} %{quote.loanQuoteFirstName} %{quote.loanQuoteMiddleName} %{quote.loanQuoteLastName}" 
				cssClass="form-control" readonly="true"/></li>
		<li><label>CIF Number</label><s:textfield name="dpCifNumber" id="dpCifNumber" value="%{quote.loanQuoteCifNumber}" cssClass="form-control" readonly="true"/></li>
	</ul>
	<br/>
	<div class="clearfix"></div>	
	<ul>
		
		<li>
			1. By signing this Consent Form, I confirm that I, as the Data Principal, have read and understood the Privacy Notice provided to me through a physical copy/digital copy and/or available at 
			https://www.dummylink.com and that I consent to SBI’s collecting, holding and Processing the Digital Personal Data for the relevant purposes as indicated therein.
		</li>
		<br/>
		<li>
			2. I confirm that the Officially Valid Document (OVD) details such as (Aadhaar, Driving License, etc.) and other information provided to the bank, belongs to me. 
			I authorize SBI to make any inquiries with regulatory, statutory and/or other agencies for OVD and other information verification.
		</li>
		<br/>
		<li>
			3. The following activities are essential for providing banking facilities and services; without which we will be unable to offer you the products or services you opted for.
		</li>
		
		<s:set var="eisResponse" value="%{consentRead.eisResponse}" />
		<table border="2px" width="100%" cellspacing="0" cellpadding="5">
			<thead>
				<tr>
					<th scope="col" style="border: 1px solid rgb(140 140 140)"  width="70%">Purpose Details</th>
					<th scope="col" style="border: 1px solid rgb(140 140 140)"  width="15%">Select</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="#eisResponse.body.dpConsent">
					
					<s:set var="containerTitle" value="containerTitle"/>
					<s:set var="rowSpanCount" value="purposes.size() * 2"/>
					
					<s:iterator value="purposes" status="stat">
					
						<s:set var="bankProductsSize" value="bankProducts.size()"/>
						<s:set var="purposeCode" value="code"/>
						<s:if test="%{#containerTitle == 'Essential Purposes' || #containerTitle == 'Optional Purposes'}">
							<tr>
								<th scope="col" style="border: 1px solid rgb(140 140 140)">
									<s:property value="title"/>
								</th>
							<s:if test="%{#containerTitle == 'Essential Purposes'}">
								<input type="hidden" name="essentialPurpose"
									value="<s:property value='code'/>|<s:property value='version'/>" />
								<%-- <s:if test="%{#stat.first}">
										<td style="border: 1px solid rgb(140 140 140)" rowspan="<s:property value='rowSpanCount' />" onclick="javascript:openPopups('consentPopupETB','1');">
											<!-- <a href="javascript:void(0);" onclick="javascript:openPopups('consentPopupETB','1');"> -->
												<input type="checkbox" id="essentialPurpose" checked disabled>
											</a>
										</td>
									</s:if> --%>
									
																		<s:if test="%{#stat.first}">
										<td style="border: 1px solid rgb(140 140 140)" rowspan="<s:property value='rowSpanCount' />">
												<input type="checkbox"  onclick="javascript:openPopups('consentPopupETB','1');" id="essentialPurpose" checked disabled>
										</td>
									</s:if>

									
<%-- 								<s:if test="%{#stat.first}">
									<td style="border: 1px solid rgb(140, 140, 140)"
										rowspan="<s:property v.alue='rowSpanCount' />"><span
										id="etbPrivacyPopupTrigger" class="etb-privacy-popup-trigger"
										role="button" tabindex="0"
										title="Click to view SBI Privacy Notice"
										onclick="openEtbPrivacyPopup();"
										onkeydown="openEtbPrivacyPopupByKeyboard(event);"> <input
											type="checkbox" id="essentialPurposeCheckbox"
											checked="checked" disabled="disabled"
											aria-label="Essential purpose selected" /> <span
											class="etb-privacy-popup-text"> View Privacy Notice </span>
									</span></td>
								</s:if> --%>

								<input type="hidden"
									name="purposeVersion_<s:property value='code'/>"
									value="<s:property value='version'/>" />
								<s:iterator value="bankProducts">
									<input type="hidden"
										name="product_<s:property value='#purposeCode'/>"
										value="<s:property value='code'/>" />
								</s:iterator>

							</s:if>
							<s:elseif test="%{#containerTitle == 'Optional Purposes' && #bankProductsSize == 0}">
									<td style="border: 1px solid rgb(140 140 140)" rowspan="2">
										<input type="hidden" name="allPurposes" value="<s:property value='code'/>" />
										<input type="hidden" name="purposeVersion_<s:property value='code'/>" value="<s:property value='version'/>" />
										<input type="checkbox" name="selectedPurposes" value="<s:property value='code'/>" />
									</td>
								</s:elseif>
								<s:else>
									<td style="border: 1px solid rgb(140 140 140)" rowspan="2">
										<input type="hidden" name="purposeVersion_<s:property value='code'/>" value="<s:property value='version'/>" />
										<s:iterator value="bankProducts" status="stat">
											
											<input type="hidden" name="allProducts_<s:property value='#purposeCode'/>" value="<s:property value='code'/>" />
											<input type="checkbox" name="product_<s:property  value='#purposeCode'/>" value="<s:property value='code'/>" />
											<s:property value="label"/>
											<br/>
										</s:iterator>
									</td>
								</s:else>
							</tr>
							<tr>
								<td style="border: 1px solid rgb(140 140 140)"><s:property value="description"/></td>
							</tr>
						</s:if>
						<s:else>
							<input type="hidden" name="otherPurpose" value="<s:property value='code'/>|<s:property value='version'/>" />
						</s:else>
						
					</s:iterator>
				</s:iterator>
			</tbody>
		</table>
		
		<li>
			4. If I do grant consent, I understand that I can also withdraw consent. I understand that SBI will stop Processing all or some of my Digital Personal Data if I withdraw consent for such Processing, 
			unless otherwise required to be Processed/retained under any law for the time being in force. 
			However, this will not affect any Digital Personal Data that has already been processed prior to the withdrawal of my consent.
		</li>
		<br/>
		<h4>Applicable only in the case of Processing Digital Personal Data of Children</h4>
		<br/>
		<li>
			5. I, the natural / lawful guardian(s) of <s:label value="%{quote.loanQuoteAppFirstName} %{quote.loanQuoteFirstName} %{quote.loanQuoteMiddleName} %{quote.loanQuoteLastName}" /> , a child who is under 18 years of age, hereby consent for Processing of my/our child’s/ward’s Digital Personal Data 
			as required under the Digital Personal Data Protection Act, 2023.
		</li>
		<br/>
		<li>
			6. I understand that SBI may require a photocopy of my/our Government ID card (or equivalent identification or any other proof) as evidence to verify my/our age/prove my/our relationship with the ward.
		</li>
		<br/>
		<li>
			7. I further understand that SBI shall not undertake Processing of Digital Personal Data that is likely to cause any detrimental effect on the well-being of my child. 
			SBI shall not undertake tracking or behavioral monitoring of children or targeted advertising directed at children, unless permitted by law.
		</li>
		<br/>
		<li>
			8. The above section is subject to regulations issued by RBI for providing banking and ancillary services in India. In the event of any conflict between a provision of the DPDPA, 
			2023 and a provision of any other law for the time being in force, the provision of the DPDPA, 2023 shall prevail to the extent of such conflict.
		</li>
		<br/>
		
		<h4>Applicable only in case of Processing Digital Personal Data of Persons with Disabilities (PwD)</h4>
		<br/>
		<li>
			9. I, the lawful guardian of <s:label value="%{quote.loanQuoteAppFirstName} %{quote.loanQuoteFirstName} %{quote.loanQuoteMiddleName} %{quote.loanQuoteLastName}" /> , a person with disability, hereby consent for Processing Digital Personal Data of such person under my guardianship, 
			as required under the Digital Personal Data Protection Act, 2023.
		</li>
		<br/>
		<li>
			10. I understand that SBI will be required to verify that I am the guardian appointed by a court of law, a designated authority or a local level committee, under the law applicable to guardianship.
		</li>
		<br/>
		<li>
			11. The above section is subject to regulations issued by RBI for providing banking and ancillary services in India. In the event of any conflict between a provision of the DPDPA, 
			2023 and a provision of any other law for the time being in force, the provision of the DPDPA, 2023, shall prevail to the extent of such conflict.
		</li>
		<br/>
	</ul>
		
	
	<div class="clearfix"></div>
	<div id="termsAndConditionFirst" class="sbi-trms-div">
		<ul class="form-section">
			<li class="full-width">
				<div class="qt-btn-div flr mrgt-10 m-100 call-us">
					<input  type="submit" class="submit-btn" name="submitConsent" id="submitConsent" value="Submit Consent">
				</div>
			 </li>
		 </ul>
	</div>
 
 <script type="text/javascript">
	function openEtbPrivacyPopup() {
		/*
		 * Essential Purpose must always remain selected.
		 */
		$("#essentialPurposeCheckbox")
			.prop("checked", true)
			.prop("disabled", true);
		/*
		 * consentPopupETB must exactly match the modal ID
		 * in ConsentPopupETB.jsp.
		 */
		openPopups("consentPopupETB", "1");
		return false;
	}
	function openEtbPrivacyPopupByKeyboard(event) {
		if (event.key === "Enter" || event.key === " ") {
			event.preventDefault();
			openEtbPrivacyPopup();
		}
	}
</script>
