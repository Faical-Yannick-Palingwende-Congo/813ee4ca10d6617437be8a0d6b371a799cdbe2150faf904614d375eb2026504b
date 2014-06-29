package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

import com.tracker.backend.service.rest.envelop.field.ResponseBasicPayload;

@XmlRootElement
public class BasicResponse extends ResponseHead{
	public ResponseBasicPayload payload;
	
	public BasicResponse() {
		super();
	}

	public BasicResponse(String stamp, String code, ResponseBasicPayload payload) {
		super(stamp, code);
		this.payload = payload;
	}
	
	public ResponseBasicPayload getPayload() {
		return payload;
	}
}
