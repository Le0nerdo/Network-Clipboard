package dao;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import com.mongodb.client.FindIterable;

import org.bson.Document;

/**
 * Class to access mongoDB (atlas) database that does not work as intendet and
 * breaks the whole application.
 * @author Le0nerdo
 */
public class MongoDBAccess implements DatabaseAccess {
	final private MongoDBCollection collection;

	/**
	 * Opens a new connection to a MongoDB Atlas database.
	 * @param collection MongoDPClient to connect to database.
	 */
	public MongoDBAccess(MongoDBCollection collection) {
		this.collection = collection;
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
		return this.collection.isConnected();
	}

	/**
	 * Closes the connection to the MongoDB Atlas database.
	 */
	public void close() {
		this.collection.close();
	}

	/**
	 * Reconnects database to a new uniform resource identifier(URI).
	 * @param uri the uniform resource identifier(URI). For MongoDB Atlas the URI
	 * starts with {@code mongodb+srv://}.
	 */
	@Override
	public void reconnectTo(String uri) {
		this.collection.reconnectTo(uri);
	}

}
