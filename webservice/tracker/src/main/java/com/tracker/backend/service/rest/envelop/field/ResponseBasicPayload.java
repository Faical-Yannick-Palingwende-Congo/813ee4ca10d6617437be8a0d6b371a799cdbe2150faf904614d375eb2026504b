package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ResponseBasicPayload {
	public String details;
	
	public ResponseBasicPayload() {

	}

	public ResponseBasicPayload(String details) {
		super();
		this.details = details;
	}

	public String getDetails() {
		return details;
	}
}
