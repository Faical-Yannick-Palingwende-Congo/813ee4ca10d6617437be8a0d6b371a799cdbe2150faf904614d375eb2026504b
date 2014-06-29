package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ResponseAccountSearchPayload;

@XmlRootElement
public class AccountSearchResponse extends ResponseHead{
	public ResponseAccountSearchPayload payload;
	
	public AccountSearchResponse() {
		super();
	}

	public AccountSearchResponse(String stamp, String code, ResponseAccountSearchPayload payload) {
		super(stamp, code);
		this.payload = payload;
	}
	
	public ResponseAccountSearchPayload getPayload() {
		return payload;
	}
}
