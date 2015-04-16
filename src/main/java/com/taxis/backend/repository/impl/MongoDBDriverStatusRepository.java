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
import com.taxis.backend.model.DriverStatus;
import com.taxis.backend.repository.DriverStatusRepository;

@Repository
public class MongoDBDriverStatusRepository implements DriverStatusRepository{
	
	DBCollection collection;
	Gson gson = new Gson();
	Logger logger = Logger.getLogger(MongoDBDriverStatusRepository.class);
	
	@PostConstruct
	public void postConstruct(){
		collection = MongoDBCentralRepository.getInstance().getMongoClient().getDB(MongoDBConstants.DB_NAME).getCollection(MongoDBConstants.DRIVER_STATUS_COLLECTION_NAME);
	}

	public DriverStatus findById(long driverId) throws TaxisBackendCustomDBException {
		DriverStatus result = null;
		
		try{
			DBCursor cursor = collection.find(new BasicDBObject(MongoDBConstants.ID_KEY, driverId));
			DBObject objFound = cursor.next();
			if (objFound != null){
				result = new DriverStatus(objFound);
			}
			cursor.close();
			return result;
		} catch (NoSuchElementException e) {
			logger.error(e);
			throw new TaxisBackendCustomDBException("Couldnt find driver for driverId ("+driverId+")");
		}
		
		
	}

	public int upsert(DriverStatus ds) throws TaxisBackendCustomDBException {
		BasicDBObject doc = new BasicDBObject(MongoDBConstants.ID_KEY, ds.getDriverId())
		.append(MongoDBConstants.DRIVER_STATUS_AVAILABLE_KEY, ds.isAvailable())
		.append(MongoDBConstants.DRIVER_STATUS_LATITUDE_KEY, ds.getLatitude())
		.append(MongoDBConstants.DRIVER_STATUS_LONGITUDE_KEY, ds.getLongitude());
		int result = 0;
		try {
			result = collection.save(doc).getN();
		} catch (MongoException e) {
			logger.error(e);
			throw new TaxisBackendCustomDBException("Could not insert or update for driverId ("+ds.getDriverId()+")");
		}
		
		return result;
	}

	public List<DriverStatus> findInArea(double swLat, double swLong,
			double neLat, double neLong) throws TaxisBackendCustomDBException {
		
		List<DriverStatus> result = new ArrayList<DriverStatus>();
		BasicDBObject query = new BasicDBObject(MongoDBConstants.DRIVER_STATUS_LATITUDE_KEY, 
				new BasicDBObject("$gt", swLat)
				.append("$lt", neLat))
		.append(MongoDBConstants.DRIVER_STATUS_LONGITUDE_KEY, 
				new BasicDBObject("$gt", swLong)
				.append("$lt", neLong));
		
		DBCursor cursor = collection.find(query);
		try {
			while (cursor.hasNext()){
				DriverStatus ds = new DriverStatus(cursor.next());
				result.add(ds);
			}
		} catch (MongoException e) {
			logger.error(e);
			throw new TaxisBackendCustomDBException("Error finding taxis in area "+swLat+","+swLong+" and "+neLat+","+neLong);
		} finally {
			cursor.close();
		}
		
		return result;
	}
	 
}
