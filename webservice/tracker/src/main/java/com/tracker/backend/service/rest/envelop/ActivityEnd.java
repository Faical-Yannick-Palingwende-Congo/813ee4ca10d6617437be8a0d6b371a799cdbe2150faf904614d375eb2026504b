package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ActivityEndPayload;

@XmlRootElement
public class ActivityEnd extends RequestHead{
	public ActivityEndPayload payload;
	
	public ActivityEnd() {
		super();
	}

	public ActivityEnd(String stamp, ActivityEndPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public ActivityEndPayload getPayload() {
		return payload;
	}
}
