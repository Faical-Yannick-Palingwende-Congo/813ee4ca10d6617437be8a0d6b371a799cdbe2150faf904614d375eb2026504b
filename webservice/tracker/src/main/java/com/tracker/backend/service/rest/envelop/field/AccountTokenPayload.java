package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountTokenPayload {
	public String token;
	
	public boolean sanity() {
		return token != null;
	}
	
	public AccountTokenPayload() {
		
	}

	public AccountTokenPayload(String token) {
		super();
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
}
