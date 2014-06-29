package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ActivityStartPayload;

@XmlRootElement
public class ActivityStart extends RequestHead{
	public ActivityStartPayload payload;
	
	public ActivityStart() {
		super();
	}

	public ActivityStart(String stamp, ActivityStartPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public ActivityStartPayload getPayload() {
		return payload;
	}
}
