package com.mintstreet.consent.bo;

import java.util.List;

public class ConsentResponsePurposeEisBody {

	private String containerTitle;
	private String containerDescription;
	private List<ConsentResponsePurposes> purposes;
	private String isContainer;
	
	public String getContainerTitle() {
		return containerTitle;
	}
	
	public void setContainerTitle(String containerTitle) {
		this.containerTitle = containerTitle;
	}
	
	public String getContainerDescription() {
		return containerDescription;
	}
	
	public void setContainerDescription(String containerDescription) {
		this.containerDescription = containerDescription;
	}
	
	public List<ConsentResponsePurposes> getPurposes() {
		return purposes;
	}
	
	public void setPurposes(List<ConsentResponsePurposes> purposes) {
		this.purposes = purposes;
	}
	
	public String getIsContainer() {
		return isContainer;
	}
	
	public void setIsContainer(String isContainer) {
		this.isContainer = isContainer;
	}

	@Override
	public String toString() {
		return "ConsentResponseEisBody [containerTitle=" + containerTitle + ", containerDescription="
				+ containerDescription + ", purposes=" + purposes + ", isContainer=" + isContainer + "]";
	}

}
