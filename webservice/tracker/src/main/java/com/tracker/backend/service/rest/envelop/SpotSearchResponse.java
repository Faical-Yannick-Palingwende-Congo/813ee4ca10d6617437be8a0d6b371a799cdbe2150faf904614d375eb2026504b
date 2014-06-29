package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ResponseSpotSearchPayload;

@XmlRootElement
public class SpotSearchResponse extends ResponseHead{
	public ResponseSpotSearchPayload payload;
	
	public SpotSearchResponse() {
		super();
	}

	public SpotSearchResponse(String stamp, String code, ResponseSpotSearchPayload payload) {
		super(stamp, code);
		this.payload = payload;
	}
	
	public ResponseSpotSearchPayload getPayload() {
		return payload;
	}
}
