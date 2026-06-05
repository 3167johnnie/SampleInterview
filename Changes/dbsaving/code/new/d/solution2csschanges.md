Use this CSS below your existing page CSS.

```css
/* Privacy Consent Modal Responsive CSS */

#consentHomeLoan .modal-dialog {
	max-width: 760px;
	width: 92%;
	margin: 30px auto;
}

#consentHomeLoan .modal-content {
	border-radius: 12px;
	overflow: hidden;
}

#consentHomeLoan .privacy-modal-body {
	position: relative;
	padding: 24px;
	background: #fff;
}

#consentHomeLoan .close.clo {
	position: absolute;
	top: 12px;
	right: 12px;
	border: none;
	background: transparent;
	padding: 4px;
	z-index: 10;
}

#consentHomeLoan .close.clo img {
	width: 18px;
	height: 18px;
}

.privacy-consent-dropdown {
	width: 100%;
	margin-bottom: 14px;
}

#privacyLocaleDropdown {
	width: 100%;
	max-width: 240px;
	height: 40px;
	padding: 8px 12px;
	border: 1px solid #ccc;
	border-radius: 6px;
	font-size: 14px;
	background: #fff;
}

.privacy-consent-pop-content {
	max-height: 55vh;
	min-height: 260px;
	overflow-y: auto;
	padding: 16px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background: #fafafa;
	font-size: 14px;
	line-height: 1.6;
	color: #333;
	word-break: break-word;
}

.privacy-consent-pop-content p,
.privacy-consent-pop-content div,
.privacy-consent-pop-content span,
.privacy-consent-pop-content li {
	font-size: inherit;
	line-height: inherit;
}

#acceptConsentBtn {
	min-width: 130px;
	height: 42px;
	border-radius: 6px;
	font-size: 15px;
	font-weight: 500;
}

/* Tablet */
@media screen and (max-width: 768px) {
	#consentHomeLoan .modal-dialog {
		width: 94%;
		margin: 20px auto;
	}

	#consentHomeLoan .privacy-modal-body {
		padding: 20px 16px;
	}

	#privacyLocaleDropdown {
		max-width: 100%;
		font-size: 14px;
	}

	.privacy-consent-pop-content {
		max-height: 58vh;
		min-height: 240px;
		font-size: 13.5px;
		padding: 14px;
	}

	#acceptConsentBtn {
		width: 100%;
	}
}

/* Mobile */
@media screen and (max-width: 480px) {
	#consentHomeLoan .modal-dialog {
		width: 96%;
		margin: 12px auto;
	}

	#consentHomeLoan .privacy-modal-body {
		padding: 18px 12px 14px;
	}

	#consentHomeLoan .close.clo {
		top: 8px;
		right: 8px;
	}

	#consentHomeLoan .close.clo img {
		width: 16px;
		height: 16px;
	}

	#privacyLocaleDropdown {
		height: 38px;
		font-size: 13px;
	}

	.privacy-consent-pop-content {
		max-height: 62vh;
		min-height: 220px;
		padding: 12px;
		font-size: 13px;
		line-height: 1.55;
	}

	#acceptConsentBtn {
		width: 100%;
		height: 40px;
		font-size: 14px;
	}
}

/* Very small mobile */
@media screen and (max-width: 360px) {
	.privacy-consent-pop-content {
		max-height: 60vh;
		min-height: 200px;
		font-size: 12.5px;
	}

	#consentHomeLoan .privacy-modal-body {
		padding: 16px 10px 12px;
	}
}

/* Large desktop */
@media screen and (min-width: 1200px) {
	#consentHomeLoan .modal-dialog {
		max-width: 820px;
	}

	.privacy-consent-pop-content {
		max-height: 60vh;
		font-size: 15px;
	}
}
```

Also fix this button line. Your current `onclick` quote is broken.

Replace:

```jsp
<button type="button" id="acceptConsentBtn"class="btn btn-primary" disabled="disabled" onclick="acceptPrivacyConsent(); style="opacity: 0.6; cursor: not-allowed;">Accept</button>
```

With:

```jsp
<button type="button"
	id="acceptConsentBtn"
	class="btn btn-primary"
	disabled="disabled"
	onclick="acceptPrivacyConsent();"
	style="opacity: 0.6; cursor: not-allowed;">
	Accept
</button>
```

Also better to change this select class because both parent div and select have same class:

```jsp
<select id="privacyLocaleDropdown" class="privacy-locale-select"
	onchange="loadPrivacyByLocale();">
```

Then CSS will still work because it targets `#privacyLocaleDropdown`.
