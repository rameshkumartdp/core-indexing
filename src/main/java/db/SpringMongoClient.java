package db;

/**
 * Created by rames on 13-05-2019.
 */

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public enum SpringMongoClient {

    INSTANCE;
    private MongoCursor<Document> cursor;

     SpringMongoClient() {
         MongoClient mongoClient = new MongoClient("localhost", 27017);
         MongoDatabase database = mongoClient.getDatabase("aktway");
         MongoCollection<Document> col = database.getCollection("activity");
         cursor = col.find().iterator();
    }

    public MongoCursor<Document> getMongoCursor() {
        return cursor;
    }

}



