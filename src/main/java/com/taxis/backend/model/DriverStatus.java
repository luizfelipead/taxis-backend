package com.taxis.backend.model;

import com.mongodb.DBObject;
import com.taxis.backend.repository.impl.MongoDBConstants;

public class DriverStatus {
	
	private int driverId;
	private Boolean available;
	private Double latitude;
	private Double longitude;
	
	public DriverStatus(){
		super();
	}
	
	public DriverStatus(DBObject objFound) {
		super();
		this.driverId = (Integer) objFound.get(MongoDBConstants.ID_KEY);
		this.available = (Boolean) objFound.get(MongoDBConstants.DRIVER_STATUS_AVAILABLE_KEY);
		this.latitude = (Double) objFound.get(MongoDBConstants.DRIVER_STATUS_LATITUDE_KEY);
		this.longitude = (Double) objFound.get(MongoDBConstants.DRIVER_STATUS_LONGITUDE_KEY);
	}

	
	
	public DriverStatus(int driverId, Boolean available, Double latitude,
			Double longitude) {
		super();
		this.driverId = driverId;
		this.available = available;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public int getDriverId() {
		return driverId;
	}
	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}
	public Boolean isAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
