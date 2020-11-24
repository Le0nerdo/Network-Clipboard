package domain;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A Class to interact with the system clipboard.
 * @author Le0nerdo
 */
public class SystemClipboard implements ClipboardAccess {
    /**
     * Variable to access system clipboard.
     */
    private Clipboard clipboard;

    /**
     * Gets clipboard from system.
     */
    public SystemClipboard() {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * Writes text to system clipboard.
     * @param text The text to be written onto system clipboard.
     * @throws IllegalStateException
     */
    public void setText(final String text) throws IllegalStateException {
        StringSelection stringSelection = new StringSelection(text);
        this.clipboard.setContents(stringSelection, null);
    }

    /**
     * Reads text from the system clipboard.
     * @return Current text on system clipboard.
     * @throws UnsupportedFlavorException
     * @throws IOException
     */
    public String readText() throws UnsupportedFlavorException, IOException {
        return (String) this.clipboard.getData(DataFlavor.stringFlavor);
    }

    /**
     * Checks if the content on the system clipboard is a string.
     * @return Boolean telling if the content on the system clipboard is a
     * string.
     */
    public Boolean isString() {
        return this.clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
    }
}
