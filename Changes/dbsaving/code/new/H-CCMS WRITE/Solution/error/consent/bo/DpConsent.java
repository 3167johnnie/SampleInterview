package com.mintstreet.consent.bo;

import java.util.List;

public class DpConsent {
	
	private String containerDescription;
	
	private String containerTitle;
	
	private boolean isContainer;
	
	private List<Purposes> purposes;

	public String getContainerDescription() {
		return containerDescription;
	}

	public void setContainerDescription(String containerDescription) {
		this.containerDescription = containerDescription;
	}

	public String getContainerTitle() {
		return containerTitle;
	}

	public void setContainerTitle(String containerTitle) {
		this.containerTitle = containerTitle;
	}

	public boolean isContainer() {
		return isContainer;
	}

	public void setContainer(boolean isContainer) {
		this.isContainer = isContainer;
	}

	public List<Purposes> getPurposes() {
		return purposes;
	}

	public void setPurposes(List<Purposes> purposes) {
		this.purposes = purposes;
	} 
	
	

}
