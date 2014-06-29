package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="payload")
public class ActivityRecordPayload {
	public String token;
	public String activity;
	public String step;
	public String ax;
	public String ay;
	public String az;
	public String speed;
	public String latitude;
	public String longitude;
	public String pitch;
	public String roll;
	public String azimuth;
	
	public boolean sanity() {
		return token != null && activity != null && step != null && ax != null && ay != null && az != null && speed != null && longitude != null && latitude != null && pitch != null && roll != null && azimuth != null;
	}
	
	public ActivityRecordPayload() {
		
	}

	public ActivityRecordPayload(String token, String activity, String step, String ax, String ay, String az, String speed, String longitude, String latitude, String altitude, String pitch, String roll, String azimuth) {
		super();
		this.token = token;
		this.activity = activity;
		this.step = step;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.speed = speed;
		this.longitude = longitude;
		this.latitude = latitude;
		this.pitch = pitch;
		this.roll = roll;
	}
	
	public String getToken() {
		return token;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public String getStep() {
		return step;
	}
	
	public String getAx() {
		return ax;
	}

	public String getAy() {
		return ay;
	}

	public String getAz() {
		return az;
	}

	public String getSpeed() {
		return speed;
	}

	public String getLongitude() {
		return longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getPitch() {
		return pitch;
	}
	
	public String getRoll() {
		return roll;
	}
	
	public String getAzimuth() {
		return azimuth;
	}
}
