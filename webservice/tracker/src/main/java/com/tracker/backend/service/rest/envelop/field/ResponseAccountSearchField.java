package com.tracker.backend.service.rest.envelop.field;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseAccountSearchField {
	public String email;
	public String status;
	
	public ResponseAccountSearchField() {

	}

	public ResponseAccountSearchField(String email, String status) {
		super();
		this.email = email;
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public String getStatus() {
		return status;
	}
}
