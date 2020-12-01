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
public class MongoDBAccess implements DatabaseAccess {
	/**
	 * Instance of MongoClient used to communicate with the database.
	 */
	private MongoClient mongoClient;
	/**
	 * Instance of MongoCollection that is connected to the database.
	 */
	private MongoCollection<Document> collection;

	private Boolean connected = false;

	/**
	 * Opens a new connection to a MongoDB Atlas database.
	 * @param uri Uniform Resource Identifier for MongoDB Atlas.
	 * @throws UnknownHostException
	 */
	public MongoDBAccess(final String uri) {
		this.reconnectTo(uri);
	}

	/**
	 * Reconnects database to a new uniform resource
	 * identifier(URI). 
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

	private void changeClient(final String uri) {
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
	 * Writes text to database.
	 * @param text Text to be written to database.
	 */
	public void write(final String text) {
		Document newText = new Document("text", text);
		try {
			this.collection.insertOne(newText);
		} catch (Exception e) {
			System.out.println("ERROR MongoDBAccess/write\n" + e);
		}
	}

	/**
	 * Gets last texts from the database.
	 * @return Array with texts.
	 */
	public String[] read() {
		try {
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
		} catch (Exception e) {
			String[] emptyArray = new String[10];
			Arrays.fill(emptyArray, "");
			return emptyArray;
		}
	}

	/**
	 * Checks if the DatabaseAccess is connected.
	 * @return boolean telling if the DatabaseAccess is connected.
	 */
	public Boolean isConnected() {
		return this.connected;
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

}
