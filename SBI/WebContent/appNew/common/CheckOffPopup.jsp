<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="modal fade otp-box" id="checkoff" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-body">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="false"><img src="<s:property value="%{@com.mintstreet.common.util.Constants@BANK_IMAGE_FOLDER_NEWUI}"/>/closelight.png" alt="" /></span>
				</button>
				<div class="f-otp-pop-content">
					
						<h3>1) FULL CHECK OFF :</h3>
						<ul >
							<li>(a) The process by which the salary disbursing officer
								undertakes to deduct loan installments from the salary of the
								borrower and remits the same to the Bank for credit to the loan
								account.</li>
							<p style="text-align:center; font-weight:bold; padding:10px; padding-bottom:0px;">or</p>
							<li >(b) (i) The employer pays
								the borrower's salary into his Savings/Current Account with
								our Bank.</li>
							<li >(ii) The borrower gives an
								irrevocable Standing Instruction (SI) for recovery of the loan
								installments from his aforesaid account with the SI being
								synchronised with the date of credit of salary in the
								borrower's Savings/Current Account.</li>
							<p style="text-align:center; font-weight:bold; padding:10px; padding-bottom:0px;">and</p>
						</ul>
						<ul >
							
							<li>(a) Undertaking from the employer to obtain a NOC from
								the Bank before settling the dues of the borrower on transfer,
								resignation, retirement etc.</li>
						</ul>

						<h3>2) PARTIAL CHECK OFF :</h3>
						<ul >
							<li>(a) The process by which the salary disbursing officer
								undertakes to deduct loan installments from the salary of the
								borrower and remits the same to the Bank for credit to the loan
								account.</li>
							<p style="text-align:center; font-weight:bold; padding:10px; padding-bottom:0px;">or</p>
							<li >(b) (i) The employer pays
								the borrower's salary into his Savings/Current Account with
								our Bank.</li>
							<li >(ii) The borrower gives an
								irrevocable Standing Instruction (SI) for recovery of the loan
								installments from his aforesaid account with the SI being
								synchronised with the date of credit of salary in the
								borrower's Savings/Current Account.</li>
							<p style="text-align:center; font-weight:bold; padding:10px; padding-bottom:0px;">and</p>
						</ul>
						<ul>
							
							<li>The employer undertakes to inform the Bank if and when
								there is a severance due to borrower's transfer, resignation,
								retirement etc.</li>
						</ul>
						<h3>3) CATEGORY III: NO Check Off</h3>
						<ul>
							<li>The employer pays the borrower's salary into the
								Savings/Current Account with our Bank.</li>
							<li>The borrower gives an irrevocable PDCs/ECS/ SI for
								recovery of the loan installments The SI should be synchronised
								with the date of credit of salary in the borrower's
								Savings/Current Account.</li>
						</ul>
				</div>
			</div>
		</div>
	</div>
</div>
