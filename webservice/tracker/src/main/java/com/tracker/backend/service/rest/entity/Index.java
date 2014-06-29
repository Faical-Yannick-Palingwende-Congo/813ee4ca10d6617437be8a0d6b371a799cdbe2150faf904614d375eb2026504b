package com.tracker.backend.service.rest.entity;

public class Index {
	public String id;
	public String table;
	public String next;
	
	public Index() {
		
	}

	public Index(String id, String table, String next) {
		super();
		this.id = id;
		this.table = table;
		this.next = next;
	}

	public String getId() {
		return id;
	}

	public String getTable() {
		return table;
	}

	public String getNext() {
		return next;
	}
}
