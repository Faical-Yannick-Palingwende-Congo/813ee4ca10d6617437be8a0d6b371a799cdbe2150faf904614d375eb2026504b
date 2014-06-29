package com.parkingchum.frontend.client.android.parkingtrack;

public class JsonBlock {
	private String parameter;
	private String value;
	public JsonBlock(String parameter, String value) {
		super();
		this.parameter = parameter;
		this.value = value;
	}
	public String getParameter() {
		return parameter;
	}
	public String getValue() {
		return value;
	}
}
