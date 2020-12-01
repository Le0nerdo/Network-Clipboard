package dao;

/**
 * Class to access a database.
 * 
 * @author Le0nerdo
 */
public interface DatabaseAccess {

	/**
	 * Writes text to database.
	 * @param text Text to be written to database.
	 */
	void write(String text);

	/**
	 * Gets last texts from the database.
	 * @return Array with texts.
	 */
	String[] read();

	/**
	 * Closes the connection to the database.
	 */
	void close();

	/**
	 * Checks if the DatabaseAccess is connected.
	 * @return boolean telling if the DatabaseAccess is connected.
	 */
	Boolean isConnected();

	/**
	 * Reconnects database to a new uniform resource
	 * identifier(URI). 
	 * @param uri the uniform resource identifier(URI).
	 */
	void reconnectTo(String uri);
}
