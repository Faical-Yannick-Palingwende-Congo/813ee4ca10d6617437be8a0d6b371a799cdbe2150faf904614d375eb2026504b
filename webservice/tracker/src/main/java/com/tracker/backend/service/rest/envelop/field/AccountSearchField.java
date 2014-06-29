package com.tracker.backend.service.rest.envelop.field;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="pattern")
public class AccountSearchField {
	public String field;
	public String value;
	
	public AccountSearchField() {
		
	}

	public AccountSearchField(String field, String value) {
		super();
		this.field = field;
		this.value = value;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}
}
