package com.tracker.backend.service.rest.entity;

public class Activity {
	public String id;
	public String account;
	public String type;
	public String start;
	public String end;
	
	public Activity() {
		
	}

	public Activity(String id, String account, String type, String start, String end) {
		super();
		this.id = id;
		this.account = account;
		this.type = type;
		this.start = start;
		this.end = end;
	}

	public String getId() {
		return id;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getType() {
		return type;
	}
	
	public String getStart() {
		return start;
	}
	
	public String getEnd() {
		return end;
	}
}
