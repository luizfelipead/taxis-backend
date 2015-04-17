package com.taxis.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.Driver;
import com.taxis.backend.repository.DriverRepository;

@Service
public class DriverService {
	
	@Autowired
	protected DriverRepository repository;
	
	public int insert(Driver driver) throws TaxisBackendCustomDBException, TaxisBackendCustomDataFormatException{
		String carPlatePattern = "[a-zA-Z]{3}[-][0-9]{4}";
		boolean isPlateValid = driver.getCarPlate().matches(carPlatePattern);
		if (!isPlateValid) {
			throw new TaxisBackendCustomDataFormatException("Wrong format data for car plate ("+driver.getCarPlate()+"). Try ABC-1234.");
		}
		
		return repository.insert(driver);
	}
	
	public Driver find(Driver driver) throws TaxisBackendCustomDBException {
		return repository.find(driver);
	}
	
	

}
