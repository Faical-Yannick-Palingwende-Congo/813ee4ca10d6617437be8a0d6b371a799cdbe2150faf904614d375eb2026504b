package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ActivityEndPayload {
	public String token;
	public String activity;
	
	public boolean sanity() {
		return token != null && activity != null;
	}
	
	public ActivityEndPayload() {
		
	}

	public ActivityEndPayload(String token, String acttivity) {
		super();
		this.token = token;
		this.activity = acttivity;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getActivity() {
		return activity;
	}
}
