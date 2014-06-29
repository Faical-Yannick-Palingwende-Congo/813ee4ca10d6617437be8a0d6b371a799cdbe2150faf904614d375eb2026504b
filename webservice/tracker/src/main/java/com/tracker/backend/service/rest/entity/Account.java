package com.tracker.backend.service.rest.entity;

public class Account {
	public String id;
	public String name;
	public String email;
	public String password;
	
	public Account() {
		
	}

	public Account(String id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
