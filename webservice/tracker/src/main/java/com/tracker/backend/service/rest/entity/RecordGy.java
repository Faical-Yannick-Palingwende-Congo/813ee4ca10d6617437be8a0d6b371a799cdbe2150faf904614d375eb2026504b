package com.tracker.backend.service.rest.entity;

public class RecordGy extends AbstractRecord{
	public String pitch;
	public String roll;
	public String azimuth;
	
	
	public RecordGy() {
		
	}

	public RecordGy(String id, String activity, String step, String pitch, String roll, String azimuth) {
		super(id, activity, step);
		this.pitch = pitch;
		this.roll = roll;
		this.azimuth = azimuth;
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
