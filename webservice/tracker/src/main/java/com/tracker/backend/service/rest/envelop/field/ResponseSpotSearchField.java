package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResponseSpotSearchField {
	public String id;
	public String longitude;
	public String latitude;
	public String altitude;
	public String status;
	
	public ResponseSpotSearchField() {

	}

	public ResponseSpotSearchField(String id, String longitude, String latitude, String altitude,
			String status) {
		super();
		this.id = id;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}

	public String getLongitude() {
		return longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getAltitude() {
		return altitude;
	}

	public String getStatus() {
		return status;
	}
}
