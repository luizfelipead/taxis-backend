package com.taxis.backend.model;

import com.mongodb.DBObject;
import com.taxis.backend.repository.impl.MongoDBConstants;

public class DriverStatus {

	private long driverId;
	private boolean available;
	private double latitude;
	private double longitude;
	
	public DriverStatus(){
		super();
	}
	
	public DriverStatus(DBObject objFound) {
		super();
		this.driverId = (Long) objFound.get(MongoDBConstants.ID_KEY);
		this.available = (Boolean) objFound.get(MongoDBConstants.DRIVER_STATUS_AVAILABLE_KEY);
		this.latitude = (Double) objFound.get(MongoDBConstants.DRIVER_STATUS_LATITUDE_KEY);
		this.longitude = (Double) objFound.get(MongoDBConstants.DRIVER_STATUS_LONGITUDE_KEY);
	}

	public long getDriverId() {
		return driverId;
	}
	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
