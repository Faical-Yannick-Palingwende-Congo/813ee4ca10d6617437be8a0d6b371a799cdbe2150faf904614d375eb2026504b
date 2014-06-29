package com.tracker.backend.service.rest.entity;

public class RecordPo extends AbstractRecord{
	public String la;
	public String lo;
	
	
	public RecordPo() {
		
	}

	public RecordPo(String id, String activity, String step, String la, String lo) {
		super(id, activity, step);
		this.la = la;
		this.lo = lo;
	}
	
	public String getLa() {
		return la;
	}
	
	public String getLo() {
		return lo;
	}
}
