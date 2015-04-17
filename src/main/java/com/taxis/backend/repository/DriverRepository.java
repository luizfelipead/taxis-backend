package com.taxis.backend.repository;

import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.model.Driver;

public interface DriverRepository {
	public int insert(Driver driver) throws TaxisBackendCustomDBException;
	public Driver find(Driver driver) throws TaxisBackendCustomDBException;
}
