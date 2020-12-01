package domain;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * A Class that wraps a {@link Clipboard} to make accessing it easier.
 * @author Le0nerdo
 */
public class ClipboardAccess {
	private final Clipboard clipboard;

	/**
	 * Wraps a {@link Clipboard} into a new {@link ClipboardAccess}.
	 * @param clipboard the {@link Clipboard} to be wrapped.
	 */
	public ClipboardAccess(final Clipboard clipboard) {
		this.clipboard = clipboard;
	}

	/**
	 * Writes a {@link String} to the wrapped {@link Clipboard}.
	 * @param text the {@link String} to be written onto the wrapped
	 * {@link Clipboard}.
	 * @throws IllegalStateException if the {@link Clipboard} is currently
	 * unavailable.
	 */
	public void setString(final String text) throws IllegalStateException {
		final StringSelection stringSelection = new StringSelection(text);
		this.clipboard.setContents(stringSelection, null);
	}

	/**
	 * Reads the curren {@link String} from the wrapped {@link Clipboard}.
	 * @return current {@link String} on the wrapped {@link Clipboard}.
	 * @throws UnsupportedFlavorException  if there is no {@link String}
	 * available.
	 * @throws IOException if the {@link String} can not be retrieved.
	 */
	public String getString() throws UnsupportedFlavorException, IOException {
		return (String) this.clipboard.getData(DataFlavor.stringFlavor);
	}

	/**
	 * Checks if the content on the wrapped {@link Clipboard} is a
	 * {@link String}.
	 * @return a boolean telling if the content on the {@link Clipboard} is a
	 * {@link String}.
	 */
	public Boolean containsString() {
		return this.clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
	}
}
