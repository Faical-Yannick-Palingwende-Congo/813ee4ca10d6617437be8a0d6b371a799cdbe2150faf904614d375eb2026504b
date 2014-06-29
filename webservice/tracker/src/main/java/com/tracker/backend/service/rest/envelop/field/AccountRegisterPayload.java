package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload") 
public class AccountRegisterPayload{
	public String name;
	public String email;
	public String password;
	
	public boolean sanity() {
		return name != null && email != null && password != null ;
	}
	
	public AccountRegisterPayload() {
		
	}

	public AccountRegisterPayload(String name, String email, String password) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
