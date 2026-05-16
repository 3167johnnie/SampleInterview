<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!--approved Project -->
	<div class="modal fade otp-box" id="approvedProject" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-body">
					<button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
						<span aria-hidden="true"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt="" /></span>
					</button>
					<div class="otp-pop-content">
						<h2>Disclaimer</h2>
						<p>By clicking "Proceed" button, you will be redirected to
							the resources located on servers maintained and operated by
							third parties. SBI doesn’t take any responsibility for the
							images, pictures, plan, layout, size, cost, materials shown in
							the site. Please take decision judiciously at your own risk
							and responsibility and SBI will not be responsible for any
							loss, damage, costs & charges, direct or indirect incurred by
							you, arising out of or in connection with your decision for
							purchasing a property from the list of approved projects</p>
						<p><a href="https://www.sbirealty.in" target="_blank" onClick="$('#approvedProject').modal('hide');" class="submit-btn">Proceed</a></p>
					</div>
				</div>
			</div>
		</div>
	</div>
<!--approved project popup-->
