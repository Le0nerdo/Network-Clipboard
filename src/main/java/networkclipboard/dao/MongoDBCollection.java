package networkclipboard.dao;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class MongoDBCollection {
	private MongoClient mongoClient;
	private MongoCollection<Document> collection;
	private Boolean connected = false;

	/**
	 * Makes a new wrappedMongoClient that helps accessing a database collection.
	 * @param uri the uniform resource identifier(URI). For MongoDB Atlas the URI
	 * starts with {@code mongodb+srv://}.
	 */
	public MongoDBCollection(final String uri) {
		this.reconnectTo(uri);
	}

	/**
	 * Reconnects {@link MongoDBCollection} to a new uniform resource identifier(URI).
	 * @param uri the uniform resource identifier(URI). For MongoDB Atlas the URI
	 * starts with {@code mongodb+srv://}.
	 */
	public void reconnectTo(final String uri) {
		try {
			changeClient(uri);
			this.connected = true;
		} catch (Exception e) {
			this.connected = false;
		}
	}

	/**
	 * Connects {@link MongoDBCollection} to a uniform resource identifier(URI). 
	 * @param uri the uniform resource identifier(URI). For MongoDB Atlas the URI
	 * starts with {@code mongodb+srv://}.
	 * @throws IllegalArgumentException when the mongo uri is not valid. Also
	 * throws MongoConfigurationException when no internetconnection, but the
	 * MongoDB system does not hande connection issues well so the error is "hidden".
	 */
	private void changeClient(final String uri) throws IllegalArgumentException {
		if (this.mongoClient == null) {
			this.close();
		}
		final MongoClientSettings settings = MongoClientSettings.builder()
						.applyConnectionString(new ConnectionString(uri))
						.retryWrites(true)
						.build();
		this.mongoClient = MongoClients.create(settings);
		final MongoDatabase database = this.mongoClient
						.getDatabase("NetworkClipboard");
		this.collection = database.getCollection("text");
	}

	/**
	 * Closes the connection to the MongoDB Atlas database.
	 */
	public void close() {
		if (this.connected) {
			this.connected = false;
			this.mongoClient.close();
		}
	}

	/**
	 * Uses MongoCollection.find().
	 * @return same as MongoCollection.find().
	 * @see {@link MongoCollection#find()}
	 */
	public FindIterable<Document> find() {
		return this.collection.find();
	}

	/**
	 * Uses MongoCollection.insertOne().
	 * @param document to be inserted.
	 * @see {@link MongoCollection#insertOne()}
	 */
	public void insertOne(Document document) {
		this.collection.insertOne(document);
	}

	/**
	 * Checks if the {@link MongoDBCollection} is connected.
	 * @return boolean telling if the {@link MongoDBCollection} is connected.
	 */
	public Boolean isConnected() {
		return this.connected;
	}
}
