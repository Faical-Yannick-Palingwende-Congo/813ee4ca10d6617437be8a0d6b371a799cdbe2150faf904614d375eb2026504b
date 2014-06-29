package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ResponseAccountStatePayload {
	public ResponseAccountSateField context;
	
	public ResponseAccountStatePayload() {

	}

	public ResponseAccountStatePayload( ResponseAccountSateField context) {
		super();
		this.context = context;
	}

	public ResponseAccountSateField getContext() {
		return context;
	}
}
