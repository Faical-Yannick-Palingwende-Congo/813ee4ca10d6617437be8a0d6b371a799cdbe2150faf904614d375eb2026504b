package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class AccountContactUsPayload {
	public String email;
	public String title;
	public String content;
	
	public boolean sanity() {
		return email != null && title != null && content != null;
	}
	
	public AccountContactUsPayload() {
		
	}

	public AccountContactUsPayload(String email, String title, String content) {
		super();
		this.email = email;
		this.title = title;
		this.content = content;
	}

	public String getEmail() {
		return email;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		return content;
	}
}
