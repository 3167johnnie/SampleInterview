package com.mintstreet.callback.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "register_OCAS_INSERT_CCDBResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CallBackResponse implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "ns1:register_OCAS_INSERT_CCDBReturn")
    protected int register_OCAS_INSERT_CCDBReturn;

	public int getRegister_OCAS_INSERT_CCDBReturn(){
		return register_OCAS_INSERT_CCDBReturn;
	}

	public void setRegister_OCAS_INSERT_CCDBReturn(int register_OCAS_INSERT_CCDBReturn) {
		this.register_OCAS_INSERT_CCDBReturn = register_OCAS_INSERT_CCDBReturn;
	}
     
}
	
