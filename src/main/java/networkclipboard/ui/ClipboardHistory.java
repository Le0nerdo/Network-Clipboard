package networkclipboard.ui;

import networkclipboard.domain.ClipboardManipulator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * A Class to maintan a JavaFX {@link VBox} that contains the clipboard history.
 * @author Le0nerdo
 */
public class ClipboardHistory {
	private ClipboardManipulator clipboardManipulator;
	private VBox mainBox = new VBox();
	private VBox historyBox = new VBox();
	private Text connected = new Text("");

	/**
	 * Constructor that creates the initial {@link VBox} to show clipboard history.
	 * @param clipboardmanipulator A {@link ClipboardManipulator} to read
	 * the current history and put text from history to clipboard.
	 */
	public ClipboardHistory(final ClipboardManipulator clipboardmanipulator) {
		final int padding = 10;
		this.mainBox.setPadding(new Insets(padding));
		this.clipboardManipulator = clipboardmanipulator;

		HBox historyHeader = new HBox();
		historyHeader.setAlignment(Pos.CENTER);
		historyHeader.getChildren().add(new Text(
						"Click on text in history below to apply it to clipbord."));
		this.connected.setStroke(Color.rgb(255, 0, 0));
		historyHeader.getChildren().add(this.connected);
		

		this.mainBox.getChildren().add(historyHeader);
		this.mainBox.getChildren().add(historyBox);
		update();
	}

	/**
	 * Updates the shown history according to curren history on
	 * {@link ClipboardManipulator}.
	 */
	public void update() {
		String[] history = this.clipboardManipulator.updateClipboard();
		this.historyBox.getChildren().setAll(createElements(history));
		this.updateConnected();
	}

	/**
	 * Creates the shown elements for each history entry. 
	 * @param history array of history entries.
	 * @return array of elements for showing history entries.
	 */
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

	/**
	 * Makes history entry elements look slightly less ugly.
	 * @param element the element to get a new style.
	 */
	private void styleElement(final Text element) {
		StringProperty style = new SimpleStringProperty();
		style.setValue("-fx-cursor: hand;");
		element.styleProperty().bind(style);
		final int bottomPadding = 6;
		VBox.setMargin(element, new Insets(0, 0, bottomPadding, 0));
	}

	/**
	 * Shows {@code Not Connected!} when {@link ClipboardManipulator} is not
	 * connected to a database.
	 */
	private void updateConnected() {
		if (this.clipboardManipulator.isConnected()) {
			this.connected.setText("");
		} else {
			this.connected.setText(" Not Connected!");
		}
	}

	/**
	 * Return a {@link VBox} that contains the clipboard history.
	 * @return A {@link VBox} showing the clipboard history.
	 */
	public VBox getElement() {
		return this.mainBox;
	}
}
