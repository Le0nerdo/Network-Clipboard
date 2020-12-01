package domain;

import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.util.Arrays;

import dao.DatabaseAccess;

/**
 * A Class for connectiong a {@link DatabaseAccess} and {@link ClipboardAccess}. This
 * is the heart of the project Network Clipboard.
 * @author Le0nerdo
 * @see ClipboardAccess
 * @see DatabaseAccess
 */
public class ClipboardManipulator {
	private final ClipboardAccess clipboardAccess;
	private final DatabaseAccess databaseAccess;
	private String[] history;
	private Boolean stopped;
	private Boolean paused;

	/**
	 * Connect {@link DatabaseAccess} and {@link ClipboardAccess} with a new
	 * {@link ClipboardManipulator}.
	 * @param clipboardAccess the {@link ClipboardAccess} to be connected.
	 * @param databaseAccess the {@link DatabaseAccess} to be connected.
	 */
	public ClipboardManipulator(final ClipboardAccess clipboardAccess, final DatabaseAccess databaseAccess) {
		this.clipboardAccess = clipboardAccess;
		this.databaseAccess = databaseAccess;
		this.history = new String[10];
		Arrays.fill(this.history, "");
		this.paused = false;
		this.stopped = false;
	}

	/**
	 * Connects the connected {@link DatabaseAccess} to a new uniform resource
	 * identifier(URI).
	 * @param uri the uniform resource identifier(URI). For MongoDB Atlas the URI
	 * starts with {@code mongodb+srv://}.
	 */
	public void connectDatabaseTo(final String uri) {
		this.databaseAccess.reconnectTo(uri);
	}

	/**
	 * Disconnects the connected {@link DatabaseAccess}. This should only be
	 * used when this {@link ClipboardManipulator} is not needed anymore.
	*/
	public void disconnectDatabase() {
		this.databaseAccess.close();
	}

	/**
	 * Updates the connected {@link ClipboardAccess} according to the
	 * connected {@link DatabaseAccess}.
	 * @return a histoy of the last {@link String}s on
	 * {@link ClipboardAccess}/{@link DatabaseAccess}.
	 */
	public String[] updateClipboard() {
		if (this.stopped || !this.isConnected() || !clipboardAccess.containsString()) {
			return this.history;
		}
		this.history = this.databaseAccess.read();
		try {
			if (this.paused) {
				return this.history;
			}
			this.clipboardAccess.setString(this.history[0]);
		} catch (Exception e) {
			System.out.println(
							"ERROR ClipboardManipulator/updateclipboard: " + e);
		}

		return this.history;
	}

	/**
	 * Updates the the connected {@link ClipboardAccess} according to the given
	 * {@link String}, but does not inform the connected
	 * {@link DatabaseAccess}.
	 * @param text the {@link String} to be set on the connected
	 * {@link ClipboardAccess}.
	 */
	public void setTemporaryClipboardText(final String text) {
		try {
			this.clipboardAccess.setString(text);
		} catch (Exception e) {
			System.out.println(
							"ERROR ClipboardManipulator/setTemporaryClipboardText: " + e);
		}
	}

	/**
	 * Creates a {@link FlavorListener} that updates the connected
	 * {@link DatabaseAccess} and after that uses
	 * {@link ClipboardManipulator#updateClipboard()} to make sure that the
	 * local program is up to date.
	 * @return A FlavorListener that reacts to {@link FlavorEvent}s.
	 */
	public FlavorListener createClipboardListener() {
		return new FlavorListener() {
			@Override
			public void flavorsChanged(final FlavorEvent flavorEvent) {
				try {
					if (!stopped && clipboardAccess.containsString() && isConnected()) {
						final String text = clipboardAccess.getString();
						if (Arrays.asList(history).contains(text)) {
							updateClipboard();
							return;
						}
						databaseAccess.write(text);
						updateClipboard();
					}
				} catch (Exception e) {
					System.out.println(
									"ERROR ClipboardManipulator/FlavorListener: " + e);
				}
			}
		};
	}

	/**
	 * Sets the {@link ClipboardManipulator} to a stopped or not stopped state.
	 * @param stopped true for stopped state and false for not stopped state.
	 */
	public void setStopped(final Boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * Gets the status of {@link ClipboardManipulator} being stoppped.
	 * @return true for stopped state and false for not stopped state.
	 */
	public Boolean getStopped() {
		return this.stopped;
	}

	/**
	 * Sets the {@link ClipboardManipulator} to a paused or not paused state.
	 * @param paused true for paused state and false for not paused state.
	 */
	public void setPaused(final Boolean paused) {
		this.paused = paused;
	}

	/**
	 * Gets the status of {@link ClipboardManipulator} being paused.
	 * @return true for paused state and false for not paused state.
	 */
	public Boolean getPaused() {
		return this.paused;
	}

	/**
	 * Checks if the connected {@link DatabaseAccess} is connected.
	 * @return true if is connected and false if is not connected.
	 */
	public Boolean isConnected() {
		return this.databaseAccess.isConnected();
	}
}
