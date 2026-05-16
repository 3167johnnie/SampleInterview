$(document)
		.ready(
				function() {
					$("form#otpApplication")
							.validate(
									{
										onkeyup : false,
										onfocusout : function(b) {
											jQuery(b).valid()
										},
										onblur : function(b) {
											jQuery(b).valid()
										},
										focusInvalid : false,
										invalidHandler : function(h, f) {
											var g = f.numberOfInvalids();
											if (g) {
												var e = g == 1 ? "You missed 1 field. It has been highlighted"
														: "You missed "
																+ g
																+ " fields. They have been highlighted";
												showPOPMsg("errorOTPMsg", e,
														false)
											}
										},
										errorElement : "span",
										errorPlacement : function(d, c) {
											if ($("#" + c.attr("id")).is(
													"select")) {
												d.insertAfter($(c).parent())
											} else {
												if (c.attr("type") == "checkbox") {
													return true
												} else {
													d.insertAfter(c)
												}
											}
										}
									});
					jQuery("#confirmOtp")
							.on(
									"click",
									function() {
										removeDynamicLoader("confirmOtp");
										showDynamicLoader("confirmOtp");
										hidePOPMsg("errorOTPMsg");
										var h = fetch_validation_object(16, 1);
										addValidationsRules(
												document.otpApplication, h);
										if ($("form#otpApplication").valid()) {
											$("#appApplyingFromIndiaYes")
													.removeAttr("disabled");
											$("#appApplyingFromIndiaNo")
													.removeAttr("disabled");
											var e = getRequest(
													"otpApplication", 0);
											if (!e) {
												var g = "Sorry for the inconvenience, please try again later.";
												showPOPMsg("errorOTPMsg", g,
														false);
												return false
											}
											var f = "params=" + e;
											f += "&requestIndex=16&requestType=2";
											jQuery("#appApplyingFromIndiaYes")
													.attr("disabled",
															"disabled");
											jQuery("#appApplyingFromIndiaNo")
													.attr("disabled",
															"disabled");
											if (uiType != undefined
													&& uiType != "") {
												f += "&uiType=" + uiType
											}
											jQuery
													.ajax({
														type : "POST",
														url : ajaxPostUrl,
														data : f,
														success : function(a) {
															removeDynamicLoader("confirmOtp");
															if (a == null
																	|| a == undefined) {
																showPOPMsg(
																		"errorOTPMsg",
																		SORRY_FOR_INCONVENIENCE,
																		false);
																return false
															}
															var b = [];
															b = a.split("|");
															if (b[0] == "success") {
																isMobileVerified = "Y";
																jQuery(
																		"#overlay-row_otp_hl")
																		.hide();
																jQuery(
																		"#overlay-row_otp_hl")
																		.remove();
																jQuery(
																		"#otp_row_confirm")
																		.hide();
																showPOPMsg(
																		"errorOTPMsg",
																		'<img src="'
																				+ BANK_IMAGE_FOLDER
																				+ '/tick.png"> OTP verified successfully',
																		true);
																jQuery(
																		"a.call_us")
																		.unbind(
																				"click");
																setTimeout(
																		function() {
																			$(
																					"#OTP")
																					.modal(
																							"hide")
																		}, 2000)
															} else {
																if (b[0] == "error") {
																	showPOPMsg(
																			"errorOTPMsg",
																			b[1],
																			false);
																	jQuery(
																			"input[name='"
																					+ b[2]
																					+ "']")
																			.addClass(
																					"error");
																	return false
																}
															}
														},
														error : function(b, a) {
															showPOPMsg(
																	"errorOTPMsg",
																	SORRY_FOR_INCONVENIENCE,
																	false);
															return false
														}
													})
										} else {
											$("#confirmOtp").removeAttr(
													"disabled");
											removeDynamicLoader("confirmOtp")
										}
										return false
									});
					jQuery("#resendOtp")
							.on(
									"click",
									function() {
										removeDynamicLoader("resendOtp");
										showDynamicLoader("resendOtp");
										hidePOPMsg("errorOTPMsg");
										var h = fetch_validation_object(15, 1);
										addValidationsRules(
												document.otpApplication, h);
										if ($("form#otpApplication").valid()) {
											$("#appApplyingFromIndiaYes")
													.removeAttr("disabled");
											$("#appApplyingFromIndiaNo")
													.removeAttr("disabled");
											var e = getRequest(
													"otpApplication", 0);
											if (!e) {
												var g = "Sorry for the inconvenience, please try again later.";
												showPOPMsg("errorOTPMsg", g,
														false);
												return false
											}
											var f = "params=" + e;
											f += "&requestIndex=15&requestType=2";
											jQuery("#appApplyingFromIndiaYes")
													.attr("disabled",
															"disabled");
											jQuery("#appApplyingFromIndiaNo")
													.attr("disabled",
															"disabled");
											if (uiType != undefined
													&& uiType != "") {
												f += "&uiType=" + uiType
											}
											jQuery
													.ajax({
														type : "POST",
														url : ajaxPostUrl,
														data : f,
														success : function(a) {
															removeDynamicLoader("resendOtp");
															if (a == null
																	|| a == undefined) {
																showPOPMsg(
																		"errorOTPMsg",
																		SORRY_FOR_INCONVENIENCE,
																		false);
																return false
															}
															var b = [];
															b = a.split("|");
															if (b[0] == "success") {
																removeDynamicLoader("resendOtp");
																refreshCaptcha("CommonOtpcapImage");
																showPOPMsg(
																		"errorOTPMsg",
																		'<img src="'
																				+ BANK_IMAGE_FOLDER
																				+ '/tick.png">'
																				+ b[1],
																		true);
																show_hide_toggle(
																		"#otp_row_confirm",
																		1);
																show_hide_toggle(
																		"#overlay-row_otp_hl",
																		0)
															} else {
																if (b[0] == "error") {
																	refreshCaptcha("CommonOtpcapImage");
																	if (b[2] != undefined
																			&& b[2] != null) {
																		if (b[2] > 4) {
																			jQuery(
																					"#resendOtp")
																					.remove()
																		}
																	}
																	showPOPMsg(
																			"errorOTPMsg",
																			b[1],
																			false)
																}
															}
														},
														error : function(b, a) {
															showPOPMsg(
																	"errorOTPMsg",
																	SORRY_FOR_INCONVENIENCE,
																	false);
															return false
														}
													})
										} else {
											$("#resendOtp").removeAttr(
													"disabled");
											removeDynamicLoader("resendOtp")
										}
										return false
									});
					jQuery("#sendOpt")
							.unbind("click")
							.bind(
									"click",
									function() {
										hidePOPMsg("errorOTPMsg");
										if ($("#appApplyingFromIndiaNo").is(
												":checked")) {
											$("#mobileotp").remove();
											$("#emailidotp").remove();
											if (!M
													.isMobile($(
															"#appNRIMobileNo")
															.val())
													&& !M
															.isEmail($(
																	"#emailOTP")
																	.val())) {
												M.byId("appNRIMobileNo").style.borderColor = "red";
												showPOPMsg(
														"errorOTPMsg",
														"Invalid mobile number",
														false);
												$("#appNRIMobileNo").focus();
												$("#emailOTP")
														.after(
																"<div class='clear error' id='emailidotp'> Invalid email id1.</div>");
												M.byId("emailOTP").style.borderColor = "red";
												showPOPMsg("errorOTPMsg",
														"Invalid email id",
														false);
												$("#emailOTP").focus();
												return false
											}
											if ($("#appOTPVerifyTypeNo").is(
													":checked")) {
												$("#emailidotp").remove();
												if (!M.isEmail($("#emailOTP")
														.val())) {
													$("#emailOTP")
															.after(
																	"<div class='clear error' id='emailidotp'> Invalid email id2.</div>");
													M.byId("emailOTP").style.borderColor = "red";
													return false
												}
											}
										} else {
											var j = $("#mobile").val();
											$("#mobileotp").remove();
											$("#emailidotp").remove()
										}
										var i = fetch_validation_object(14, 1);
										addValidationsRules(
												document.otpApplication, i);
										if ($("form#otpApplication").valid()) {
											showDynamicLoader("sendOpt");
											$(".disabledFields").each(
													function() {
														$(this).removeAttr(
																"disabled")
													});
											var f = getRequest(
													"otpApplication", 0);
											if (!f) {
												var h = "Sorry for the inconvenience, please try again later.";
												showPOPMsg("errorOTPMsg", h,
														false);
												return false
											}
											var g = "params=" + f;
											g += "&requestIndex=14&requestType=2";
											if (uiType != undefined
													&& uiType != "") {
												g += "&uiType=" + uiType
											}
											$
													.ajax({
														type : "POST",
														url : ajaxPostUrl,
														data : g,
														success : function(a) {
															removeDynamicLoader("sendOpt");
															$(
																	"#opt-loader-application")
																	.hide();
															if (a == null
																	|| a == undefined) {
																showPOPMsg(
																		"errorOTPMsg",
																		SORRY_FOR_INCONVENIENCE,
																		false);
																return false
															}
															var b = [];
															b = a.split("|");
															if (b[0] == "success") {
																showPOPMsg(
																		"errorOTPMsg",
																		'<img src="'
																				+ BANK_IMAGE_FOLDER
																				+ '/tick.png">'
																				+ b[1],
																		true);
																show_hide_toggle(
																		"#otp_row_confirm",
																		1);
																show_hide_toggle(
																		"#overlay-row_otp_hl",
																		0);
																if ($(
																		"#appApplyingFromIndiaYes")
																		.is(
																				":checked")) {
																	jQuery(
																			"#otp_verify_type")
																			.hide()
																} else {
																	if ($(
																			"#appApplyingFromIndiaNo")
																			.is(
																					":checked")) {
																		jQuery(
																				"#otp_verify_type")
																				.show()
																	}
																}
																jQuery(
																		"#appApplyingFromIndiaYes")
																		.attr(
																				"disabled",
																				"disabled");
																jQuery(
																		"#appApplyingFromIndiaYes")
																		.addClass(
																				"disabledFields");
																jQuery(
																		"#appApplyingFromIndiaNo")
																		.addClass(
																				"disabledFields");
																jQuery(
																		"#appApplyingFromIndiaNo")
																		.attr(
																				"disabled",
																				"disabled");
																refreshCaptcha("CommonOtpcapImage")
															} else {
																if (b[0] == "error") {
																	refreshCaptcha("CommonOtpcapImage");
																	showPOPMsg(
																			"errorOTPMsg",
																			b[1],
																			false);
																	return false
																}
															}
														},
														error : function(b, a) {
															removeDynamicLoader("sendOpt");
															showPOPMsg(
																	"errorOTPMsg",
																	SORRY_FOR_INCONVENIENCE,
																	false);
															return false
														}
													})
										}
										return false
									});
					jQuery(document)
							.on(
									"click",
									"#appApplyingFromIndiaNo",
									function() {
										var d = "";
										if (!isDsrPage) {
											d = "M.toolTip_TM('Enter mobile no. without ISD code or &#8216;0&#8217; in prefix.', event, this, 130, 25, 0);"
										}
										var c = '<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label><input type="text" placeholder="Mobile no. without ISD code" onkeydown="return M.digit(event);" onfocus="'
												+ d
												+ '" class="form-control" id="appNRIMobileNo" value="" maxlength="12" name="nriMobileNo"></li>';
										jQuery("#mobileDetailsOTP").html(c);
										jQuery("#appOTPVerifyTypeYes").trigger(
												"click");
										$("#isdCode").val(91)
									});
					jQuery(document)
							.on(
									"click",
									"#appApplyingFromIndiaYes",
									function() {
										var b = '<li><label>Enter your Mobile No.&nbsp;<b class="req">*</b></label><input type="text" placeholder="Mobile no." onkeydown="return M.digit(event);" id="mobile" value="" maxlength="10" name="mobile" class="form-control"></li>';
										jQuery("#mobileDetailsOTP").html(b);
										jQuery("#appOTPVerifyTypeYes").trigger(
												"click");
										$("#isdCode").val(91)
									});
					jQuery(document)
							.on(
									"click",
									"#appOTPVerifyTypeYes",
									function() {
										var b = '<li><label><span id="otpLabel">Enter code sent by SMS<b class="req"> *</b></span></label><input id="inputOtp" name="inputOtp" class="form-control" maxlength="6" type="text" onkeydown="return M.digit(event);" class="form-control"></li>';
										jQuery("#otp_row_confirm_html").html(b)
									});
					jQuery(document)
							.on(
									"click",
									"#appOTPVerifyTypeNo",
									function() {
										var b = '<li><label><span id="otpLabel">Enter code sent by Email<b class="req"> *</b></span></label><input id="inputOtpEmail" name="inputOtpEmail" type="text" class="form-control" maxlength="6" onkeydown="return M.isAlphaNumeric(event);" class="form-control"></li>';
										jQuery("#otp_row_confirm_html").html(b)
									})
				});
