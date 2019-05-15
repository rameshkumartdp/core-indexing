package com.adk.aktway.search.db;

/**
 * Created by rames on 13-05-2019.
 */

import com.adk.aktway.search.config.GlobalConstants;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public enum SpringMongoClient {

    INSTANCE;
    private MongoCursor<Document> cursor;

     SpringMongoClient() {
         MongoClient mongoClient = new MongoClient(GlobalConstants.MONGO_HOST, Integer.parseInt(GlobalConstants.MONGO_PORT));
         MongoDatabase database = mongoClient.getDatabase(GlobalConstants.MONGO_DB);
         MongoCollection<Document> col = database.getCollection(GlobalConstants.MONGO_COLLECTION);
         cursor = col.find().iterator();
    }

    public MongoCursor<Document> getMongoCursor() {
        return cursor;
    }

}



