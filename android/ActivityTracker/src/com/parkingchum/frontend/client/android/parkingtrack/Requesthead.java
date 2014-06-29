package com.parkingchum.frontend.client.android.parkingtrack;

import org.json.JSONException;
import org.json.JSONObject;

public class Requesthead {
	
	private String stamp;
	private String activity;
	private String step;
	private String ax;
	private String ay;
	private String az;
	private String pitch;
	private String roll;
	private String azimuth;
	private String speed;
	private String longitude;
	private String latitude;
	private String altitude;
	
	public Requesthead(String stamp, String activity,
			String step, String ax, String ay, String az, String pitch, String roll, String azimuth, String speed,
			String longitude, String latitude, String altitude) {
		super();
		this.stamp = stamp;
		this.activity = activity;
		this.step = step;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
		this.pitch = pitch;
		this.roll = roll;
		this.azimuth = azimuth;
		this.speed = speed;
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}
	
	JSONObject toJson(){
		JSONObject searchRequest = new JSONObject();
		try {
			searchRequest.put("stamp", this.stamp);
			JSONObject payloadRequest = new JSONObject();
			payloadRequest.put("token", SplashScreen.token);
		    payloadRequest.put("activity", this.activity);
		    payloadRequest.put("step", this.step);
			payloadRequest.put("ax", this.ax);
			payloadRequest.put("ay", this.ay);
			payloadRequest.put("az", this.az);
			payloadRequest.put("pitch", this.ax);
			payloadRequest.put("roll", this.ay);
			payloadRequest.put("azimuth", this.az);
			payloadRequest.put("speed", this.speed);
			payloadRequest.put("longitude", this.longitude);
			payloadRequest.put("latitude", this.latitude);
			payloadRequest.put("altitude", this.altitude);
			searchRequest.put("payload", payloadRequest);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchRequest;
	}

	public String getStamp() {
		return stamp;
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
	
	public String getPitch() {
		return pitch;
	}
	
	public String getRoll() {
		return roll;
	}
	
	public String getAzimuth() {
		return azimuth;
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

	public String getAltitude() {
		return altitude;
	}
}
