package com.mintstreet.common.bo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ApiObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiObject {
	
	@XmlElement(name = "objectKey")
	private ObjectKey objectKey;

	public ObjectKey getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(ObjectKey objectKey) {
		this.objectKey = objectKey;
	}	
}
