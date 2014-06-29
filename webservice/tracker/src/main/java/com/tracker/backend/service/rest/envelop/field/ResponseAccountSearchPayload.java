package com.tracker.backend.service.rest.envelop.field;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ResponseAccountSearchPayload {
	public String length;
	@XmlElement
	public List<ResponseAccountSearchField> context;
	
	public ResponseAccountSearchPayload() {

	}

	public ResponseAccountSearchPayload(List<ResponseAccountSearchField> context) {
		super();
		this.context = context;
		this.length = ""+context.size();
	}
	
	public String getLength() {
		return length;
	}

	public List<ResponseAccountSearchField> getContext() {
		return context;
	}
}
