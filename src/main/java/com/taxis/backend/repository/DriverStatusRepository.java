package com.taxis.backend.repository;

import java.util.List;

import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.model.DriverStatus;

public interface DriverStatusRepository {

	public DriverStatus findById(int driverId) throws TaxisBackendCustomDBException;
	public int upsert(DriverStatus ds) throws TaxisBackendCustomDBException;
	public List<DriverStatus> findInArea(double swLat, double swLong, double neLat, double neLong, boolean availableOnly) throws TaxisBackendCustomDBException;
	
}
