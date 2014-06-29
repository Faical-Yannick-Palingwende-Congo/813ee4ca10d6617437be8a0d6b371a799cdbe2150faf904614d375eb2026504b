package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ResponseAccountStatePayload;

@XmlRootElement
public class AccountStateResponse extends ResponseHead{
	public ResponseAccountStatePayload payload;
	
	public AccountStateResponse() {
		super();
	}

	public AccountStateResponse(String stamp, String code, ResponseAccountStatePayload payload) {
		super(stamp, code);
		this.payload = payload;
	}
	
	public ResponseAccountStatePayload getPayload() {
		return payload;
	}
}
