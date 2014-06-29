package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountSearchPayload {
	public String token;
	public AccountSearchField pattern;
	
	public boolean sanity() {
		return token != null;
	}
	
	public AccountSearchPayload() {
		
	}

	public AccountSearchPayload(String token, AccountSearchField pattern) {
		super();
		this.token = token;
		this.pattern = pattern;
	}

	public String getToken() {
		return token;
	}
	
	public AccountSearchField getPattern() {
		return pattern;
	}
}
