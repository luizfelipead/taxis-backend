package com.taxis.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.DriverStatus;
import com.taxis.backend.repository.DriverStatusRepository;
import com.taxis.backend.utils.TaxisBackendUtils;

@Service
public class DriverStatusService {
	
	public static double MAX_SEARCH_AREA = 1000.0;
	private static double GEOMETRIC_RATIO = 2;
	
	@Autowired
	protected DriverStatusRepository repository;
	
	public DriverStatus findById(long driverId) throws TaxisBackendCustomDBException{
		return repository.findById(driverId);
	}
	
	public int upsert(DriverStatus ds) throws TaxisBackendCustomDBException{
		return repository.upsert(ds);
	}

	public List<DriverStatus> findInArea(String sw, String ne) throws TaxisBackendCustomDataFormatException, TaxisBackendCustomDBException {
		List<DriverStatus> result = new ArrayList<DriverStatus>();
		double[] swLatAndLong = TaxisBackendUtils.getPositionFromString(sw);
		double[] neLatAndLong = TaxisBackendUtils.getPositionFromString(ne);
		
		result = repository.findInArea(swLatAndLong[0], swLatAndLong[1], neLatAndLong[0], neLatAndLong[1]);
		
		return result;
	}

	public DriverStatus findNearestDriver(double latitude, double longitude) throws TaxisBackendCustomDBException {
		DriverStatus result = null;
		List<DriverStatus> driversFound = new ArrayList<DriverStatus>();
		double searchArea = 1.0;
		while (driversFound.isEmpty() && searchArea <= MAX_SEARCH_AREA) {
			searchArea*=GEOMETRIC_RATIO;
			double[] rectPoints = TaxisBackendUtils.getRectanglePoints(latitude, longitude, searchArea);
			driversFound = repository.findInArea(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3]);
			searchArea = TaxisBackendUtils.getRectangleArea(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3]);
		}
		
		if (!driversFound.isEmpty()) {
			result = TaxisBackendUtils.getNearestDriver(driversFound, latitude, longitude);
		}
		
		return result;
		
	}

}
