package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountContactUsPayload;

@XmlRootElement
public class AccountContactUs extends RequestHead{
	public AccountContactUsPayload payload;
	
	public AccountContactUs() {
		super();
	}

	public AccountContactUs(String stamp, AccountContactUsPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountContactUsPayload getPayload() {
		return payload;
	}
}
