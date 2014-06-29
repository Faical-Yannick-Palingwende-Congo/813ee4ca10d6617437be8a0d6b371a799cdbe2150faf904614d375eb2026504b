package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.AccountSearchPayload;

@XmlRootElement
public class AccountSearch extends RequestHead{
	public AccountSearchPayload payload;
	
	public AccountSearch() {
		super();
	}

	public AccountSearch(String stamp, AccountSearchPayload payload) {
		super(stamp);
		this.payload = payload;
	}
	
	public AccountSearchPayload getPayload() {
		return payload;
	}
}
