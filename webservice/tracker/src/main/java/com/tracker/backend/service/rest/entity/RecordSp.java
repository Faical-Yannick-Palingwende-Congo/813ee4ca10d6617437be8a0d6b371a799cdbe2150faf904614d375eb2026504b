package com.tracker.backend.service.rest.entity;

public class RecordSp extends AbstractRecord{
	public String sp;
	
	
	public RecordSp() {
		
	}

	public RecordSp(String id, String activity, String step, String sp) {
		super(id, activity, step);
		this.sp = sp;
	}
	
	public String getSp() {
		return sp;
	}
}
