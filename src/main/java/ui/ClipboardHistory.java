package ui;

import domain.ClipboardManipulator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * A Class to maintan a JavaFX VBox that contains the clipboard history.
 * @author Le0nerdo
 */
public class ClipboardHistory {
    /**
     * Instance of ClipboardManipulator used to put text from history to the
     * clipboard.
     */
    private ClipboardManipulator clipboardManipulator;
    /**
     * Main VBox that contains the header and clipboard history.
     */
    private VBox vBox = new VBox();
    /**
     * VBox that contains only the clipboard history.
     */
    private VBox historyBox = new VBox();

    /**
     * Constructor that creates the initial VBox to show clipboard history.
     * @param clipboardmanipulator A ClipboardManipulator to put text from
     * history to the clipboard.
     * @param currentHistory Current history of the clipboard.
     */
    public ClipboardHistory(
        final ClipboardManipulator clipboardmanipulator,
        final String[] currentHistory
    ) {
        final int padding = 10;
        this.vBox.setPadding(new Insets(padding));
        this.clipboardManipulator = clipboardmanipulator;

        HBox historyHeader = new HBox();
        historyHeader.setAlignment(Pos.CENTER);
        historyHeader.getChildren().add(new Text(
            "Click on text in history below to apply it to clipbord."));

        this.vBox.getChildren().add(historyHeader);
        this.vBox.getChildren().add(historyBox);
        update(currentHistory);
    }

    /**
     * Updates the shown history according to given history.
     * @param history History to be shown.
     */
    public void update(final String[] history) {
        this.historyBox.getChildren().setAll(createElements(history));
    }

    private Text[] createElements(final String[] history) {
        final int maxLineLenght = 83;
        final int lengthBeforeDots = 80;
        Text[] elements = new Text[history.length];
        for (int i = 0; i < history.length; i++) {
            String original = history[i];
            String cleaned = original
                .replace("\n", "").replace("\r", "").replace("\t", " ");
            if (cleaned.length() > maxLineLenght) {
                cleaned = cleaned.substring(0, lengthBeforeDots) + "...";
            }
            Text newElement = new Text((i + 1) + ": " + cleaned);
            newElement.setOnMouseClicked((MouseEvent event) -> {
                clipboardManipulator.setTemporaryClipboardText(original);
            });
            this.styleElement(newElement);
            elements[i] = newElement;
        }

        return elements;
    }

    private void styleElement(final Text element) {
        StringProperty style = new SimpleStringProperty();
        style.setValue("-fx-cursor: hand;");
        element.styleProperty().bind(style);
        final int bottomPadding = 6;
        VBox.setMargin(element, new Insets(0, 0, bottomPadding, 0));
    }

    /**
     * Return the VBox that contains the clipboard history.
     * @return A VBox showing the clipboard history.
     */
    public VBox getVBox() {
        return this.vBox;
    }
}
