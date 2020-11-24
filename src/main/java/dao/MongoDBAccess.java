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

public class MongoDBAccess implements DBAccess {
	private MongoClient mongoClient;
	private MongoCollection<Document> collection;

	public MongoDBAccess(String connString) throws UnknownHostException {
		MongoClientSettings settings = MongoClientSettings.builder()
			.applyConnectionString(new ConnectionString(connString))
			.retryWrites(true)
			.build();
		this.mongoClient = MongoClients.create(settings);
		MongoDatabase database = this.mongoClient.getDatabase("test");
		this.collection = database.getCollection("test");
	}

	public void write(String text) {
		Document newText = new Document("text", text);
		this.collection.insertOne(newText);
	}

	public String[] read() {
		FindIterable<Document> res = this.collection.find().sort(new Document("_id", -1)).limit(10);
		Object[] clean = StreamSupport.stream(res.spliterator(), false).map(e -> e.get("text")).toArray();
		String[] returnable = new String[10];
		Arrays.fill(returnable, "");

		for (int i = 0; i < clean.length; i++) {
			returnable[i] = (String) clean[i];
		}
		//for (String i: returnable) {
		//	System.out.println("R@R: " + i);
		//}

		return returnable;
	}

	public void close() {
		this.mongoClient.close();
	}

}
