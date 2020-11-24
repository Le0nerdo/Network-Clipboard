package domain;

import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.util.Arrays;

import dao.DBAccess;

/**
 * A Class that manipulates a clipoard according to a database, and the
 * database according to the clipboard.
 * @author Le0nerdo
 */
public class ClipboardManipulator {
    /**
     * Instance of ClipboardAccess to access a clipboard.
     */
    private ClipboardAccess clipboardAccess;
    /**
     * Instance of DBAccees to access a database.
     */
    private DBAccess databaseAccess;
    /**
     * List of the last texts on clipboard/database.
     */
    private String[] history;

    /**
     * Constructor that connects the needed instances to the
     * ClipboardManipulatro.
     * @param accessToClipboard Instance of ClipboardAccess to access a
     * clipboard.
     * @param accessToDatabase Instance of DBAccees to access a database.
     * @param currentClipboardHistory List of the last texts on
     * clipboard/database.
     */
    public ClipboardManipulator(
        final ClipboardAccess accessToClipboard,
        final DBAccess accessToDatabase,
        final String[] currentClipboardHistory) {
        this.clipboardAccess = accessToClipboard;
        this.databaseAccess = accessToDatabase;
        this.history = currentClipboardHistory;
    }

    /**
     * Updates the clipboard according to the database.
     * @return List of the last texts on clipboard/database.
     */
    public String[] updateClipboard() {
        String[] texts = this.databaseAccess.read();
        if (texts[0].equals(this.history[0])) {
            this.history = texts;
            return this.history;
        }
        try {
            this.history = texts;
            this.clipboardAccess.setText(texts[0]);
        } catch (Exception e) {
            System.out.println(
                "ERROR ClipboardManipulator/updateclipboard: " + e);
        }

        return this.history;
    }

    /**
     * Updates the clipboard according to the given text, but does not inform
     * the database.
     * @param text The text to be put on the clipboard.
     */
    public void setTemporaryClipboardText(final String text) {
        try {
            this.clipboardAccess.setText(text);
        } catch (Exception e) {
            System.out.println(
                "ERROR ClipboardManipulator/setTemporaryClipboardText: " + e);
        }
    }

    /**
     * Creates a FlavorListener that updates the database when text is copied
     * to the clipboard.
     * @return A FlavorListener that updates the database when text is copied
     * to the clipboard.
     */
    public FlavorListener createClipboardListener() {
        return new FlavorListener() {
            @Override
            public void flavorsChanged(final FlavorEvent flavorEvent) {
                try {
                    if (clipboardAccess.isString()) {
                        String text = clipboardAccess.readText();
                        if (Arrays.asList(history).contains(text)) {
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
}
