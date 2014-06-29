package com.tracker.backend.service.rest.entity;

public class RecordAc extends AbstractRecord{
	public String ax;
	public String ay;
	public String az;
	
	
	public RecordAc() {
		
	}

	public RecordAc(String id, String activity, String step, String ax, String ay, String az) {
		super(id, activity, step);
		this.ax = ax;
		this.ay = ay;
		this.az = az;
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
}
