package com.taxis.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.exception.TaxisBackendCustomDataFormatException;
import com.taxis.backend.model.Driver;
import com.taxis.backend.model.rest.TaxisBackendRestResponse;
import com.taxis.backend.service.DriverService;

@Controller
public class DriverController {

	Gson gson = new Gson();
	
	@Autowired
	protected DriverService service;
	
	@ResponseBody
    @RequestMapping(value = "/drivers", method=RequestMethod.POST)
    public String insert(@ModelAttribute Driver driver, Model model) {
		TaxisBackendRestResponse<Driver> response = new TaxisBackendRestResponse<Driver>();
    	try {
    		service.insert(driver);
    		response.setStatusHTTP(200);
    		response.setSuccess(true);
    	} catch (TaxisBackendCustomDBException e) {
    		//Internal server http error
    		response.setStatusHTTP(500);
    		response.setSuccess(false);
    		response.setMessage(e.getMessage());
    	} catch (TaxisBackendCustomDataFormatException e) {
    		//Internal server http error
    		response.setStatusHTTP(400);
    		response.setSuccess(false);
    		response.setMessage(e.getMessage());
    	}
    	return gson.toJson(response);
    }
	
	@ResponseBody
    @RequestMapping(value = "/drivers", method=RequestMethod.GET)
    public String find(@ModelAttribute Driver driver, Model model) {
		TaxisBackendRestResponse<Driver> response = new TaxisBackendRestResponse<Driver>();
    	try {
    		response.setObject(service.find(driver));
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
	
	
}
