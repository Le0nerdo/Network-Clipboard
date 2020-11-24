package dao;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import com.mongodb.MongoClientSettings;

/**
 * Class to access mongoDB (atlas) database.
 * @author Le0nerdo
 */
public class MongoDBAccess implements DBAccess {
    /**
     * Instance of MongoClient used to communicate with the database.
     */
    private MongoClient mongoClient;
    /**
     * Instance of MongoCollection that is connected to the database.
     */
    private MongoCollection<Document> collection;

    /**
     * Opens a new connection to a MongoDB Atlas database.
     * @param uri Uniform Resource Identifier for MongoDB Atlas.
     * @throws UnknownHostException
     */
    public MongoDBAccess(final String uri) throws UnknownHostException {
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(uri))
            .retryWrites(true)
            .build();
        this.mongoClient = MongoClients.create(settings);
        MongoDatabase database = this.mongoClient.getDatabase("test");
        this.collection = database.getCollection("test");
    }

    /**
     * Writes text to database.
     * @param text Text to be written to database.
     */
    public void write(final String text) {
        Document newText = new Document("text", text);
        this.collection.insertOne(newText);
    }

    /**
     * Gets last texts from the database.
     * @return Array with texts.
     */
    public String[] read() {
        final int documentLimit = 10;
        FindIterable<Document> res = this.collection.find()
            .sort(new Document("_id", -1))
            .limit(documentLimit);
        Object[] clean = StreamSupport.stream(res.spliterator(), false)
            .map(e -> e.get("text")).toArray();
        String[] returnable = new String[documentLimit];
        Arrays.fill(returnable, "");

        for (int i = 0; i < clean.length; i++) {
            returnable[i] = (String) clean[i];
        }

        return returnable;
    }

    /**
     * Closes the connection to the MongoDB Atlas database.
     */
    public void close() {
        this.mongoClient.close();
    }

}
