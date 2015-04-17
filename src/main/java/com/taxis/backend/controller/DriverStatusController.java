package com.taxis.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.DriverStatus;
import com.taxis.backend.model.rest.TaxisBackendRestResponse;
import com.taxis.backend.service.DriverStatusService;

@Controller
@RequestMapping("/drivers")
public class DriverStatusController { 

	public static double INVALID_POSITION_VALUE = 999;

	Gson gson = new Gson();
	
	@Autowired
	protected DriverStatusService service;
	
	@ResponseBody
    @RequestMapping(value = "/status", method=RequestMethod.POST)
    public String upsert(@RequestParam("driverId") int driverId,
    		@RequestParam("available") boolean available, @RequestParam("latitude") double latitude, 
    		@RequestParam("longitude") double longitude, Model model) {
		TaxisBackendRestResponse<DriverStatus> response = new TaxisBackendRestResponse<DriverStatus>();
    	try {
    		DriverStatus ds = new DriverStatus(driverId, available, latitude, longitude);
    		service.upsert(ds);
    		response.setStatusHTTP(200);
    		response.setSuccess(true);
    	} catch (TaxisBackendCustomDBException e) {
    		response.setObject(null);
    		response.setStatusHTTP(500);
    		response.setSuccess(false);
    		response.setMessage(e.getMessage());
    	} catch (TaxisBackendCustomDataFormatException e) {
			response.setStatusHTTP(400);
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
    	return gson.toJson(response);
    }
    
    @RequestMapping(value = "/status", method=RequestMethod.GET)
    @ResponseBody
    public String findById(@RequestParam(value="driverId") int driverId, Model model) {
    	DriverStatus ds = new DriverStatus();
    	TaxisBackendRestResponse<DriverStatus> response = new TaxisBackendRestResponse<DriverStatus>();
		try {
			ds = service.findById(driverId);
			response.setObject(ds);
			response.setStatusHTTP(200);
			response.setSuccess(true);
		} catch (TaxisBackendCustomDBException e) {
			//Internal server http error
			response.setStatusHTTP(500);
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
    	return gson.toJson(response);
    }
    
    @RequestMapping(value = "/inArea", method=RequestMethod.GET)
    @ResponseBody
    public String findInAreaWithDiagonalPoints(@RequestParam(value="sw") String sw, 
    		@RequestParam(value="ne") String ne, 
    		@RequestParam(value="availableOnly", required = false, defaultValue = "false") boolean availableOnly, Model model ){
    	TaxisBackendRestResponse<List<DriverStatus>> response = new TaxisBackendRestResponse<List<DriverStatus>>();
			try {
				response.setObject(service.findInArea(sw, ne, availableOnly));
				response.setStatusHTTP(200);
				response.setSuccess(true);
			} catch (TaxisBackendCustomDataFormatException e) {
				//Syntax http error code
				response.setStatusHTTP(400);
				response.setSuccess(false);
				response.setMessage(e.getMessage());
			} catch (TaxisBackendCustomDBException e) {
				//Internal server http error
				response.setStatusHTTP(500);
				response.setSuccess(false);
				response.setMessage(e.getMessage());
			}
		return gson.toJson(response);
    }
    
    //center = latitude,longitude
    @RequestMapping(value = "/inRect", method=RequestMethod.GET)
    @ResponseBody
    public String findInAreaWithCenterAndArea(@RequestParam(value="center") String center, @RequestParam(value="area") double area,
    		@RequestParam(value="availableOnly", required = false, defaultValue = "false") boolean availableOnly, Model model ){
    	TaxisBackendRestResponse<List<DriverStatus>> response = new TaxisBackendRestResponse<List<DriverStatus>>();
			try {
				response.setObject(service.findInAreaWithCenterAndArea(center, area, availableOnly));
				response.setStatusHTTP(200);
				response.setSuccess(true);
			} catch (TaxisBackendCustomDataFormatException e) {
				//Syntax http error code
				response.setStatusHTTP(400);
				response.setSuccess(false);
				response.setMessage(e.getMessage());
			} catch (TaxisBackendCustomDBException e) {
				//Internal server http error
				response.setStatusHTTP(500);
				response.setSuccess(false);
				response.setMessage(e.getMessage());
			}
		return gson.toJson(response);
    }
    
    @RequestMapping(value = "/nearest", method=RequestMethod.GET)
    @ResponseBody
    public String nearestDriver(@RequestParam(value="latitude") double latitude, @RequestParam(value="longitude") double longitude,
    		@RequestParam(value="availableOnly", required = false, defaultValue = "false") boolean availableOnly, Model model){
    	TaxisBackendRestResponse<DriverStatus> response = new TaxisBackendRestResponse<DriverStatus>();
		DriverStatus driver;
		try {
			driver = service.findNearestDriver(latitude, longitude, availableOnly);
			if (driver == null) {
				response.setStatusHTTP(200);
				response.setSuccess(false);
				response.setMessage("No drivers found around this position. "+DriverStatusService.MAX_SEARCH_AREA+" degrees^2 was searched.");
			} else {
				response.setObject(driver);
				response.setStatusHTTP(200);
				response.setSuccess(true);
			}
		} catch (TaxisBackendCustomDBException e) {
			response.setStatusHTTP(500);
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}
		
		return gson.toJson(response);
    }
    
}
