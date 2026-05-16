<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<li>
    <label> Residence type<b class="req">&nbsp;*</b></label><br>
    <div class="flat-field">
      	<s:select list="#{4:'Rented/Others',3:'Parental',2:'Company provided',1:'Owned house'}" id="residenceTypeId" name="quote.loanQuoteResidenceTypeId" cssClass="form-select" autocomplete="off" value="%{quote.loanQuoteResidenceTypeId}"  headerKey="0" headerValue="Select residence type" onfocus="customOnFocus(this);"/>
	</div>
</li>

<li>
	<label>Relationship with bank<b class="req">&nbsp;*</b></label><br>
	<s:if test="%{quote != null && quote.loanQuoteRelationshipWithBank !=14}">
		<div class="flat-field">
			<s:select list="#{11:'Deposit relationship of atleast 3 years',12:'Satisfactory credit relationship for atleast 1 year'}" name="quote.loanQuoteRelationshipWithBank" value="%{quote.loanQuoteRelationshipWithBank}" 
			id="relationshipWithBank" cssClass="form-select" headerKey="0" headerValue="Select Relationship" onfocus="customOnFocus(this);"/>
		</div>
	</s:if>
	<s:else>
		<div class="flat-field">
			<s:select list="#{14:'New Customer'}" name="quote.loanQuoteRelationshipWithBank" value="%{quote.loanQuoteRelationshipWithBank}" 
			id="relationshipWithBank" cssClass="form-select" headerKey="0"  onfocus="customOnFocus(this);"/>
		</div>
	</s:else>
	
</li>
