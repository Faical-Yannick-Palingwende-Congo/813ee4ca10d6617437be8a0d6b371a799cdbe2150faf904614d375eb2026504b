package com.tracker.backend.service.rest.envelop.field;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ResponseSpotSearchPayload {
	public String length;
	@XmlElement
	public List<ResponseSpotSearchField> context;
	
	public ResponseSpotSearchPayload() {

	}

	public ResponseSpotSearchPayload(List<ResponseSpotSearchField> context) {
		super();
		this.context = context;
		this.length = ""+context.size();
	}

	public List<ResponseSpotSearchField> getContext() {
		return context;
	}
}
