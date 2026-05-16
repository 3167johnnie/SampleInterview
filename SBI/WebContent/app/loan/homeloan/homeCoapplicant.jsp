<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="add-co-applicant-box">
	<span>DO YOU WANT TO ADD CO-APPLICANT ?	<input id="coAppImageQuote" class="quoteCoAppAdd" name="coAppImageQuote" type="checkbox" value="" /></span> 
	<span class="note">( This may help in enhancing your loan eligibility )&nbsp; &nbsp; &nbsp;All joint owners should be co-applicant to the loan</span>
</div>
<div id="pageOverlayCoapplicantQuote" class="page_overlay3 show3 hide" style="display: none;"></div>
<div id="coApplicantQuoteContainer" class="item_wrapper3 show3" style="display: none;" >
	<form method="post" id="coApplicantQuoteForm" name="coApplicantQuoteForm"> 
		<input type="hidden" name="noOfCoapplicant" id="noOfCoapplicant" value="0"/>
		<input type="hidden" value="2" id="" name="requestType"> 
    	<div id="" class="item_overlay3">
    		<h6>Add a Co-applicant<span>
    			<a href="javascript:void(0);" class="hidePopup2"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/cancel-img_grey.png"></a></span>
    			<span class="send-quote-popup"><input type="image" name="saveCoApplicantQuote" id="saveCoApplicantQuote" class="" src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER}"/>/new-quote.png"/></span>
   			</h6>
   			<div class="msg-war corner-all" id="msg-panel-quote-co-applicant" style="display: none;"></div>
    		<div class="overlay-row3">
    			<div id="coapplicantDataContent">
    				<s:include value="/app/loan/homeloan/homeCoapplicantData.jsp"></s:include>
	    		</div>
    		</div>
    		<div class="clear"></div>
    	</div>
   	</form> 
</div>
