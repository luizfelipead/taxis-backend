package com.taxis.backend.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.DriverStatus;
import com.taxis.backend.utils.TaxisBackendUtils;

public class TaxiBackendUtilsTest {

	@Test
	public void testGetPositionFromStringWithDecimals() throws TaxisBackendCustomDataFormatException {
		String latLongStr = "10.0920,20.109201";
		double expectedLatitude = 10.0920;
		double expectedLongitude = 20.109201;
		double[] latAndLong = TaxisBackendUtils.getPositionFromString(latLongStr);
		assertEquals(expectedLatitude, latAndLong[0], 0.5);
		assertEquals(expectedLongitude, latAndLong[1], 0.5);
	}
	
	@Test
	public void testGetPositionFromStringWithIntegers() throws TaxisBackendCustomDataFormatException {
		String latLongStr = "10,20";
		double expectedLatitude = 10;
		double expectedLongitude = 20;
		double[] latAndLong = TaxisBackendUtils.getPositionFromString(latLongStr);
		assertEquals(expectedLatitude, latAndLong[0], 0.5);
		assertEquals(expectedLongitude, latAndLong[1], 0.5);
		
	}
	
	@Test
	public void testGetPositionFromStringWithNegatives() throws TaxisBackendCustomDataFormatException {
		String latLongStr = "-10,-20";
		double expectedLatitude = -10;
		double expectedLongitude = -20;
		double[] latAndLong = TaxisBackendUtils.getPositionFromString(latLongStr);
		assertEquals(expectedLatitude, latAndLong[0], 0.5);
		assertEquals(expectedLongitude, latAndLong[1], 0.5);
		
	}
	
	@Test(expected=TaxisBackendCustomDataFormatException.class)
	public void testGetPositionFromStringWithAlphanumericString() throws TaxisBackendCustomDataFormatException {
		String latLongStr = "-10,aa";
		TaxisBackendUtils.getPositionFromString(latLongStr);
	}
	
	@Test(expected=TaxisBackendCustomDataFormatException.class)
	public void testGetPositionFromStringWithEmptyString() throws TaxisBackendCustomDataFormatException {
		String latLongStr = "";
		TaxisBackendUtils.getPositionFromString(latLongStr);
	}
	
	@Test
	public void testGetRectanglePoints(){
		double centerX = 0;
		double centerY = 0;
		double area = 16;
		
		double[] expectedPoints = new double[] {-2, -2, 2, 2};
		double[] points = TaxisBackendUtils.getRectanglePoints(centerX, centerY, area);
		
		Assert.assertArrayEquals(expectedPoints, points, 0.005);
		
	}
	
	@Test
	public void testGetRectanglePointsWithDecimals(){
		double centerX = -2.5;
		double centerY = 7.5;
		double area = 4;
		
		double[] expectedPoints = new double[] {-3.5, 6.5, -1.5, 8.5};
		double[] points = TaxisBackendUtils.getRectanglePoints(centerX, centerY, area);
		
		Assert.assertArrayEquals(expectedPoints, points, 0.005);
		
	}
	
	@Test
	public void testGetRectanglePointsWithNegativeBoundaries(){
		double centerX = -180;
		double centerY = -180;
		double area = 100;
		
		double[] expectedPoints = new double[] {-180, -180, -175, -175};
		double[] points = TaxisBackendUtils.getRectanglePoints(centerX, centerY, area);
		
		Assert.assertArrayEquals(expectedPoints, points, 0.005);
	}
	
	@Test
	public void testGetRectanglePointsWithPositiveBoundaries(){
		double centerX = 180;
		double centerY = 180;
		double area = 100;
		
		double[] expectedPoints = new double[] {175, 175, 180, 180};
		double[] points = TaxisBackendUtils.getRectanglePoints(centerX, centerY, area);
		
		Assert.assertArrayEquals(expectedPoints, points, 0.5);
	}
	
	@Test
	public void testGetRectangleArea(){
		double expectedArea = 10;
		double area = TaxisBackendUtils.getRectangleArea(-5, 5, -10, 7);
		assertEquals(expectedArea, area, 0.005);
	}
	
	@Test
	public void testGetNearestDriverWithOneDriver(){
		List<DriverStatus> drivers = new ArrayList<DriverStatus>();
		DriverStatus driver1 = new DriverStatus();
		driver1.setDriverId(1);
		driver1.setLatitude(0);
		driver1.setLongitude(0);
		drivers.add(driver1);
		
		DriverStatus expectedDriver = driver1;
		DriverStatus driver = TaxisBackendUtils.getNearestDriver(drivers, 0, 0);
		
		assertEquals(expectedDriver.getDriverId(), driver.getDriverId());
	}
	
	@Test
	public void testGetNearestDriverWithMoreDrivers(){
		List<DriverStatus> drivers = new ArrayList<DriverStatus>();
		DriverStatus driver1 = new DriverStatus();
		driver1.setDriverId(1);
		driver1.setLatitude(10);
		driver1.setLongitude(9);
		drivers.add(driver1);
		
		DriverStatus driver2 = new DriverStatus();
		driver2.setDriverId(2);
		driver2.setLatitude(10);
		driver2.setLongitude(12);
		drivers.add(driver2);
		
		DriverStatus expectedDriver = driver2;
		DriverStatus driver = TaxisBackendUtils.getNearestDriver(drivers, 10, 11);
		
		assertEquals(expectedDriver.getDriverId(), driver.getDriverId());
	}
	
	
	
}
