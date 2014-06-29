package com.tracker.backend.service.rest.entity;

public class AbstractRecord {
	public String id;
	public String activity;
	public String step;
	
	
	public AbstractRecord() {
		
	}

	public AbstractRecord(String id, String activity, String step) {
		super();
		this.id = id;
		this.activity = activity;
		this.step = step;
	}

	public String getId() {
		return id;
	}
	
	public String getActivity() {
		return activity;
	}
	
	public String getStep() {
		return step;
	}
}
