package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountTokenPayload;

@XmlRootElement
public class AccountToken extends RequestHead{
	public AccountTokenPayload payload;
	
	public AccountToken() {
		super();
	}

	public AccountToken(String stamp, AccountTokenPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountTokenPayload getPayload() {
		return payload;
	}
}
