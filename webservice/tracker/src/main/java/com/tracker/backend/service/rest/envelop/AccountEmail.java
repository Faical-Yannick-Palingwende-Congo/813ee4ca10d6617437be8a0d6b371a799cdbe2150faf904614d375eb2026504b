package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountEmailPayload;


@XmlRootElement
public class AccountEmail extends RequestHead{
	public AccountEmailPayload payload;
	
	public AccountEmail() {
		super();
	}

	public AccountEmail(String stamp, AccountEmailPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountEmailPayload getPayload() {
		return payload;
	}
}
