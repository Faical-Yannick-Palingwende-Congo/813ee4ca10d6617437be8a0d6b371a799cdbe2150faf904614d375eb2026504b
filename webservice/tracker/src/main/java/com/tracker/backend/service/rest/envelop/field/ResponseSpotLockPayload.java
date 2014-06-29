package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ResponseSpotLockPayload {
	public String spot;
	public String interaction;
	public String message;
	
	public ResponseSpotLockPayload() {

	}

	public ResponseSpotLockPayload(String spot, String interaction, String message) {
		super();
		this.spot = spot;
		this.interaction = interaction;
		this.message = message;
	}

	public String getSpot() {
		return spot;
	}
	
	public String getInteraction() {
		return interaction;
	}

	public String getMessage() {
		return message;
	}
}
