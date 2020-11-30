package dao;

/**
 * Class to access a database.
 * 
 * @author Le0nerdo
 */
public interface DBAccess {

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

    Boolean isConnected();

    void reconnectTo(String uri);
}
