package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ActivityStartPayload {
	public String token;
	public String type;
	
	public boolean sanity() {
		return token != null && type != null;
	}
	
	public ActivityStartPayload() {
		
	}

	public ActivityStartPayload(String token, String type) {
		super();
		this.token = token;
		this.type = type;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getType() {
		return type;
	}
}
