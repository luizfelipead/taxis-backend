package com.taxis.backend.model.rest;

public class TaxisBackendRestResponse<T> {

	private int statusHTTP;
	private boolean success;
	private String message = "";
	private T object;
	
	public int getStatusHTTP() {
		return statusHTTP;
	}
	public void setStatusHTTP(int statusHTTP) {
		this.statusHTTP = statusHTTP;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}
	
	

}
