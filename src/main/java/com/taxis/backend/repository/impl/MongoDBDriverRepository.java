package com.taxis.backend.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.taxis.backend.exception.TaxisBackendCustomDBException;
import com.taxis.backend.model.Driver;
import com.taxis.backend.model.DriverStatus;
import com.taxis.backend.repository.DriverRepository;

@Repository
public class MongoDBDriverRepository implements DriverRepository{
	
	DBCollection driverCollection;
	DBCollection driverCounterCollection;
	Gson gson = new Gson();
	Logger logger = Logger.getLogger(MongoDBDriverRepository.class);
	
	@PostConstruct
	public void postConstruct(){
		driverCollection = MongoDBCentralRepository.getInstance().getMongoClient().getDB(MongoDBConstants.DB_NAME).getCollection(MongoDBConstants.DRIVER_COLLECTION_NAME);
		driverCounterCollection = MongoDBCentralRepository.getInstance().getMongoClient().getDB(MongoDBConstants.DB_NAME).getCollection(MongoDBConstants.DRIVER_COUNTER_COLLECTION_NAME);
	}

	public Driver find(Driver driver) throws TaxisBackendCustomDBException {
		Driver result = null;
		BasicDBObject dbObject = new BasicDBObject();
		if (driver.getDriverId() > 0){
			dbObject.append(MongoDBConstants.ID_KEY, driver.getDriverId());
		}
		if (driver.getCarPlate() != null && driver.getCarPlate().length() > 0){
			dbObject.append(MongoDBConstants.DRIVER_CAR_PLATE_KEY, driver.getCarPlate());
		}
		if (driver.getName() != null && driver.getName().length() > 0){
			dbObject.append(MongoDBConstants.DRIVER_NAME_KEY, driver.getName());
		}
		
		try{
			DBCursor cursor = driverCollection.find(dbObject);
			DBObject objFound = cursor.next();
			if (objFound != null){
				result = new Driver(objFound);
			}
			cursor.close();
			return result;
		} catch (NoSuchElementException e) {
			logger.error(e);
			throw new TaxisBackendCustomDBException("Couldnt find driver for driver ("+driver+")");
		}
	}
	
	public int insert(Driver driver) throws TaxisBackendCustomDBException {
		int newDriverId = getNextSequence();
		BasicDBObject doc = new BasicDBObject(MongoDBConstants.ID_KEY, newDriverId)
			.append(MongoDBConstants.DRIVER_CAR_PLATE_KEY, driver.getCarPlate().toUpperCase())
			.append(MongoDBConstants.DRIVER_NAME_KEY, driver.getName());
		
		int result = 0;
		try {
			result = driverCollection.save(doc).getN();
		} catch (MongoException e) {
			logger.error(e);
			throw new TaxisBackendCustomDBException("Could not insert or update for driver ("+driver+")");
		}
		
		return result;
	}
	
	//workaround para auto-increment do id no MongoDB
	private int getNextSequence() {
		int result = 0;
		
		DBObject obj = driverCounterCollection.findAndModify(new BasicDBObject(MongoDBConstants.ID_KEY, "driverId"), 
				new BasicDBObject("$inc", new BasicDBObject("seq", 1)));
		result = (Integer) obj.get("seq");
		
		System.out.println("ID RESULT ->"+result);
		
		return result;
	}
}
