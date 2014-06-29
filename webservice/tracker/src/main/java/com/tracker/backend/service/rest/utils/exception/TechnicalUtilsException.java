package com.tracker.backend.service.rest.utils.exception;


public class TechnicalUtilsException extends Exception
{
	private static final long serialVersionUID = -9140726020170336484L;

	public TechnicalUtilsException(String message){
		super(message);
	}
	
	public TechnicalUtilsException(String message, Throwable cause){
		super(message, cause);
	}
}
