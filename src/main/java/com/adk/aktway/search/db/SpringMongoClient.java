package com.adk.aktway.search.db;

/**
 * Created by rames on 13-05-2019.
 */

import com.adk.aktway.search.config.GlobalConstants;
import com.adk.aktway.search.config.PropertiesLoader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public enum SpringMongoClient {

    INSTANCE;
    private MongoCursor<Document> cursor;

     SpringMongoClient() {
         MongoClient mongoClient = new MongoClient(PropertiesLoader.getProperty(GlobalConstants.MONGO_HOST), Integer.parseInt(PropertiesLoader.getProperty(GlobalConstants.MONGO_PORT)));
         MongoDatabase database = mongoClient.getDatabase(PropertiesLoader.getProperty(GlobalConstants.MONGO_DB));
         MongoCollection<Document> col = database.getCollection(PropertiesLoader.getProperty(GlobalConstants.MONGO_COLLECTION));
         cursor = col.find().iterator();
    }

    public MongoCursor<Document> getMongoCursor() {
        return cursor;
    }

}



