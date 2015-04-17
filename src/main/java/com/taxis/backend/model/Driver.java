package com.taxis.backend.model;

import com.mongodb.DBObject;
import com.taxis.backend.repository.impl.MongoDBConstants;

public class Driver {
	
	private int driverId;
	private String name;
	private String carPlate;
	
	public Driver(){
		
	}
	
	public Driver(int driverId, String name, String carPlate) {
		super();
		this.driverId = driverId;
		this.name = name;
		this.carPlate = carPlate;
	}

	public Driver(DBObject objFound) {
		super();
		this.driverId = (Integer) objFound.get(MongoDBConstants.ID_KEY);
		this.carPlate = (String) objFound.get(MongoDBConstants.DRIVER_CAR_PLATE_KEY);
		this.name = (String) objFound.get(MongoDBConstants.DRIVER_NAME_KEY);
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCarPlate() {
		return carPlate;
	}

	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}

	@Override
	public String toString() {
		return "Driver [driverId=" + driverId + ", name=" + name
				+ ", carPlate=" + carPlate + "]";
	}
	
	
	
	
	
	
}
