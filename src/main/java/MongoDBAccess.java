import java.net.UnknownHostException;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

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
		Document newText = new Document("_id", "1")
			.append("text", text);
		ReplaceOptions wtf = new ReplaceOptions();
		wtf.upsert(true);
		collection.replaceOne(Filters.eq("_id", "1"), newText, wtf);
	}

	public String read() {
		FindIterable<Document> res = this.collection.find(Filters.eq("_id", "1"));
		return (String) res.first().get("text");
	}

	public void close() {
		this.mongoClient.close();
	}
}
