package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ActivityRecordPayload;

@XmlRootElement
public class ActivityRecord extends RequestHead{
	public ActivityRecordPayload payload;
	
	public ActivityRecord() {
		super();
	}

	public ActivityRecord(String stamp, ActivityRecordPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public ActivityRecordPayload getPayload() {
		return payload;
	}
}
