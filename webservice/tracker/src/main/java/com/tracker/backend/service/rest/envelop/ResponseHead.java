package com.tracker.backend.service.rest.envelop;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseHead {
	public String stamp;
	public String code;
	
	public ResponseHead() {

	}
	
	public ResponseHead(String stamp, String code) {
		super();
		this.stamp = stamp;
		this.code = code;
	}

	public String getStamp() {
		return stamp;
	}
	
	public String getCode() {
		return code;
	}
}
