package com.taxis.backend.repository.impl;

import java.net.UnknownHostException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDBCentralRepository {

	private static MongoDBCentralRepository instance;
	private final static Logger logger = Logger.getLogger(MongoDBCentralRepository.class);
	
	private MongoClient mongoClient;
	
	public static MongoDBCentralRepository getInstance(){
		if (instance == null) {
			instance = new MongoDBCentralRepository();
		}
		return instance;
	}
	
	private MongoDBCentralRepository(){
		try {
			MongoCredential credential = MongoCredential.createCredential(MongoDBConstants.ADMIN_USERNAME, MongoDBConstants.DB_NAME, MongoDBConstants.ADMIN_PWD.toCharArray());
			setMongoClient(new MongoClient(new ServerAddress(MongoDBConstants.DB_HOST, MongoDBConstants.DB_PORT), Arrays.asList(credential)));
		} catch (UnknownHostException e) {
			logger.error("Couldnt connect to MongoDB! UnknowHostException thrown", e);
			//will try to connect again when getInstance() is called
			instance = null;
		}
	}

	public MongoClient getMongoClient() {
		return mongoClient;
	}

	public void setMongoClient(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}
	
	
}
