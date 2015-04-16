package com.taxis.backend.utils;

import java.util.List;

import org.apache.log4j.Logger;

import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.DriverStatus;

public class TaxisBackendUtils {

	public static double[] getPositionFromString(String  position) throws TaxisBackendCustomDataFormatException {
		double[] result = new double[2];
		String[] latAndLong = position.split(",");
		TaxisBackendCustomDataFormatException e = new TaxisBackendCustomDataFormatException("Syntax error in position given when splitting string ("+position+") into latitude and longitude. It should be: -10.0202,30.02020");
		if (latAndLong.length != 2){
			Logger.getLogger(TaxisBackendUtils.class).error(e);
			throw e;
		}
		try {
			result[0] = Double.parseDouble(latAndLong[0]);
			result[1] = Double.parseDouble(latAndLong[1]);
		} catch (NumberFormatException e2){
			Logger.getLogger(TaxisBackendUtils.class).error(e);
			throw e;
		}
		
		return result;
	}

	public static double[] getRectanglePoints(double centerX,
			double centerY, double area) {
		double[] result = new double[4];
		double side = Math.sqrt(area);
		
		//min e max: -180 e 180 graus de latitude e longitude.
		//x1
		result[0] = Math.max(-180, centerX - side/2.0);
		//y1
		result[1] = Math.max(-180, centerY - side/2.0);
		//x2
		result[2] = Math.min(180, centerX + side/2.0);
		//y2
		result[3] = Math.min(180, centerY + side/2.0);
		
		return result;
	}
	
	public static double getRectangleArea(double x1, double y1, double x2, double y2) {
		return Math.abs(x1 - x2) * Math.abs(y1 - y2);
	}

	public static DriverStatus getNearestDriver(List<DriverStatus> drivers, double x, double y) {
		double lowerDistance = Double.MAX_VALUE;
		DriverStatus nearestDriver = null;
		for (DriverStatus ds : drivers) {
			double xDist = Math.abs(x - ds.getLatitude());
			double yDist = Math.abs(y - ds.getLongitude());
			double totalDist = xDist + yDist;
			if (totalDist < lowerDistance) {
				lowerDistance = totalDist;
				nearestDriver = ds;
			}
		}
		return nearestDriver;
	}
	
}
