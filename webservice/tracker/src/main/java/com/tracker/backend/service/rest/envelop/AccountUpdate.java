package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountUpdatePayload;

@XmlRootElement
public class AccountUpdate extends RequestHead{
	public AccountUpdatePayload payload;
	
	public AccountUpdate() {
		super();
	}

	public AccountUpdate(String stamp, AccountUpdatePayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountUpdatePayload getPayload() {
		return payload;
	}
}
