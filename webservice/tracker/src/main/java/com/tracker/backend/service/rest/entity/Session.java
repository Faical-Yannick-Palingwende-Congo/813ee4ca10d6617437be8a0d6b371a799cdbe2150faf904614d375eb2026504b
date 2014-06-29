package com.tracker.backend.service.rest.entity;

public class Session {
	public String account;
	public String status;
	public String token;
	public String stamp;
	
	public Session() {
		
	}

	public Session(String account, String status, String token,
			String stamp) {
		super();
		this.account = account;
		this.status = status;
		this.token = token;
		this.stamp = stamp;
	}

	public String getAccount() {
		return account;
	}

	public String getStatus() {
		return status;
	}

	public String getToken() {
		return token;
	}
	
	public String getStamp() {
		return stamp;
	}
}
