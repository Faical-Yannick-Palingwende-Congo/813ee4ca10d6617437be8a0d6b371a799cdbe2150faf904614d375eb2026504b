package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountEmailPayload {
	public String email;
	
	public boolean sanity() {
		return email != null;
	}
	
	public AccountEmailPayload() {
		
	}

	public AccountEmailPayload(String email) {
		super();
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
}
