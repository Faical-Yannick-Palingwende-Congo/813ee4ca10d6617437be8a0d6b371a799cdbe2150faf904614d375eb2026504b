package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountUpdatePayload {
	public String token;
	public String newName;
	public String newPassword;
	
	public boolean sanity() {
		return token != null;
	}
	
	public AccountUpdatePayload() {
		
	}

	public AccountUpdatePayload(String token, String newName, String newPassword) {
		super();
		this.token = token;
		this.newName = newName;
		this.newPassword = newPassword;
	}

	public String getToken() {
		return token;
	}
	
	public String getNewName() {
		return newName;
	}

	public String getNewPassword() {
		return newPassword;
	}
}
