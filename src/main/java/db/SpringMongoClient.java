package db;

/**
 * Created by rames on 13-05-2019.
 */
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.net.UnknownHostException;

public class SpringMongoClient  extends AbstractMongoConfiguration {
    MongoClient client;
    @Override
    public String getDatabaseName() {
        return "aktway";
    }

   public Mongo mongo() throws UnknownHostException {
       client = new MongoClient("localhost");
       return client;
    }
}



