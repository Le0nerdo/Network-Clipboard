package domain;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A Class to interact with the clipboard.
 * @author Le0nerdo
 */
public interface ClipboardAccess {

    /**
     * Writes text to the clipboard.
     * @param text The text to be written onto clipboard.
     * @throws IllegalStateException
     */
    void setText(String  text) throws IllegalStateException;

    /**
     * Reads text from the clipboard.
     * @return Current text on clipboard.
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    String readText() throws UnsupportedFlavorException, IOException;

    /**
     * Checks if the content on the clipboard is a string.
     * @return Boolean telling if the content on the clipboard is a string.
     */
    Boolean isString();
}
