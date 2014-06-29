package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="context")
public class ResponseAccountSateField {
	public String status;
	
	public ResponseAccountSateField() {

	}

	public ResponseAccountSateField( String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}
}
