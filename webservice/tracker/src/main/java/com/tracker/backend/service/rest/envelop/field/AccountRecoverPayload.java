package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountRecoverPayload {
	public String token;
	public String newPassword;
	
	public boolean sanity() {
		return token != null && newPassword != null;
	}
	
	public AccountRecoverPayload() {
		
	}

	public AccountRecoverPayload(String token, String newPassword) {
		super();
		this.token = token;
		this.newPassword = newPassword;
	}

	public String getToken() {
		return token;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
