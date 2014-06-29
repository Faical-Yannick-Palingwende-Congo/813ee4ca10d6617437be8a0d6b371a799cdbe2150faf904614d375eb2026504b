package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ResponseSpotLockPayload;

@XmlRootElement
public class SpotLockResponse extends ResponseHead{
	public ResponseSpotLockPayload payload;
	
	public SpotLockResponse() {
		super();
	}

	public SpotLockResponse(String stamp, String code, ResponseSpotLockPayload payload) {
		super(stamp, code);
		this.payload = payload;
	}
	
	public ResponseSpotLockPayload getPayload() {
		return payload;
	}
}
