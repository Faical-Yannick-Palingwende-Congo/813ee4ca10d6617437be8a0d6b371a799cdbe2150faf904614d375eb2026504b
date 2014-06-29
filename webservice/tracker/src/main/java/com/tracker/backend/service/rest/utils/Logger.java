package com.tracker.backend.service.rest.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Logger {
	private LinkedList<String> errors;
	private LinkedList<String> warnings;
	private LinkedList<String> debugs;
	private LinkedList<String> infos;
	private int errorsNumber;
	private int infosNumber;
	private int debugsNumber;
	private int warningsNumber;
	public DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy [HH:mm:ss:SSS]");

	public Logger() {
		this.infos = new LinkedList<String>();
		this.warnings = new LinkedList<String>();
		this.errors = new LinkedList<String>();
		this.debugs = new LinkedList<String>();

		this.infosNumber = 0;
		this.warningsNumber = 0;
		this.errorsNumber = 0;
		this.debugsNumber = 0;

	}

	public void pushInfos(String details) {
		Date date = new Date();
		this.infos.add("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Infos: " + details);
		System.out.println("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Infos: " + details);
		this.infosNumber++;
	}

	public void pushWarnings(Exception exception, String details) {
		Date date = new Date();
		this.warnings.add("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Warnings: " + details
				+ " Exception: " + exception.getClass());
		System.out.println("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Warnings: " + details
				+ " Exception: " + exception.getClass());
		this.warningsNumber++;
	}

	public void pushErrors(Exception exception, String details) {
		Date date = new Date();
		this.errors.add("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Errors: " + details
				+ " Exception: " + exception.getClass());
		System.out.println("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Errors: " + details
				+ " Exception: " + exception.getClass());
		this.errorsNumber++;
	}

	public void pushDebugs(String details) {
		Date date = new Date();
		this.debugs.add("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Debugs: " + details);
		System.out.println("<<<<" + this.dateFormat.format(date)
				+ " ### TRACKER_SERVICE_REST - Logging Debugs: " + details);
		this.debugsNumber++;
	}

	public String getInfos(int index) {
		return this.infos.get(index);
	}

	public String getWarnings(int index) {
		return this.warnings.get(index);
	}

	public String getErrors(int index) {
		return this.errors.get(index);
	}

	public String getDebugs(int index) {
		return this.debugs.get(index);
	}

	public int infosLength() {
		return this.infosNumber;
	}

	public int warningsLength() {
		return this.warningsNumber;
	}

	public int errorsLength() {
		return this.errorsNumber;
	}

	public int debugsLength() {
		return this.debugsNumber;
	}
}
