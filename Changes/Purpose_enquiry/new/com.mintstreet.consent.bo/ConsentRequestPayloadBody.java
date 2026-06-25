package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentRequestPayloadBody {

	private String touchPointId;
	private String purposeSetId;
	private String dpCif;
	private String ntbId;
	
	public String getNtbId() {
		return ntbId;
	}

	public void setNtbId(String ntbId) {
		this.ntbId = ntbId;
	}

	private ConsentRequestDpData dpData;
	private List<ConsentRequestConsent> consents;
	
	public String getTouchPointId() {
		return touchPointId;
	}
	
	public void setTouchPointId(String touchPointId) {
		this.touchPointId = touchPointId;
	}
	
	public String getPurposeSetId() {
		return purposeSetId;
	}
	
	public void setPurposeSetId(String purposeSetId) {
		this.purposeSetId = purposeSetId;
	}

	public String getDpCif() {
		return dpCif;
	}

	public void setDpCif(String dpCif) {
		this.dpCif = dpCif;
	}

	public ConsentRequestDpData getDpData() {
		return dpData;
	}

	public void setDpData(ConsentRequestDpData dpData) {
		this.dpData = dpData;
	}

	public List<ConsentRequestConsent> getConsents() {
		return consents;
	}

	public void setConsents(List<ConsentRequestConsent> consents) {
		this.consents = consents;
	}
	
}
