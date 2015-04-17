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
	
	public DriverStatus findById(int driverId) throws TaxisBackendCustomDBException{
		return repository.findById(driverId);
	}
	
	public int upsert(DriverStatus ds) throws TaxisBackendCustomDBException, TaxisBackendCustomDataFormatException{
		if (ds.getLatitude() < -180 || ds.getLatitude() > 180 || ds.getLongitude() < -180 || ds.getLongitude() > 180) {
			throw new TaxisBackendCustomDataFormatException("Invalid latitude/longitude data. Must be between -180 and 180");
		}
		return repository.upsert(ds);
	}

	public List<DriverStatus> findInArea(String sw, String ne, boolean availableOnly) throws TaxisBackendCustomDataFormatException, TaxisBackendCustomDBException {
		List<DriverStatus> result = new ArrayList<DriverStatus>();
		double[] swLatAndLong = TaxisBackendUtils.getPositionFromString(sw);
		double[] neLatAndLong = TaxisBackendUtils.getPositionFromString(ne);
		
		result = repository.findInArea(swLatAndLong[0], swLatAndLong[1], neLatAndLong[0], neLatAndLong[1], availableOnly);
		
		return result;
	}

	public DriverStatus findNearestDriver(double latitude, double longitude, boolean availableOnly) throws TaxisBackendCustomDBException {
		DriverStatus result = null;
		List<DriverStatus> driversFound = new ArrayList<DriverStatus>();
		double searchArea = 1.0;
		while (driversFound.isEmpty() && searchArea <= MAX_SEARCH_AREA) {
			searchArea*=GEOMETRIC_RATIO;
			double[] rectPoints = TaxisBackendUtils.getRectanglePoints(latitude, longitude, searchArea);
			driversFound = repository.findInArea(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3], availableOnly);
			searchArea = TaxisBackendUtils.getRectangleArea(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3]);
		}
		
		if (!driversFound.isEmpty()) {
			result = TaxisBackendUtils.getNearestDriver(driversFound, latitude, longitude);
		}
		
		return result;
		
	}

	public List<DriverStatus> findInAreaWithCenterAndArea(String center, double area, boolean availableOnly) throws TaxisBackendCustomDataFormatException, TaxisBackendCustomDBException {
		List<DriverStatus> result = new ArrayList<DriverStatus>();
		double[] centerXY = TaxisBackendUtils.getPositionFromString(center);
		double[] rectPoints = TaxisBackendUtils.getRectanglePoints(centerXY[0], centerXY[1], area);
		result = repository.findInArea(rectPoints[0], rectPoints[1], rectPoints[2], rectPoints[3], availableOnly);
		return result;
	}

}
