package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountRecoverPayload;

@XmlRootElement
public class AccountRecover extends RequestHead{
	public AccountRecoverPayload payload;
	
	public AccountRecover() {
		super();
	}

	public AccountRecover(String stamp, AccountRecoverPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountRecoverPayload getPayload() {
		return payload;
	}
}
