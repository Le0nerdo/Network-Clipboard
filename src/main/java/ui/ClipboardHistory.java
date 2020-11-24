package ui;

import domain.ClipboardManipulator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ClipboardHistory {
	private ClipboardManipulator clipboardManipulator;
	private VBox vBox = new VBox();

	public ClipboardHistory(ClipboardManipulator clipboardManipulator, String[] history) {
		vBox.setPadding(new Insets(10));
		this.clipboardManipulator = clipboardManipulator;
		update(history);
	}

	public void update(String[] history) {
		vBox.getChildren().setAll(createElements(history));
	}

	private Text[] createElements(String[] history) {
		Text[] elements = new Text[history.length];
		for (int i = 0; i < history.length; i++) {
			String original = history[i];
			String cleaned = original.replace("\n", "").replace("\r", "").replace("\t", " ");
			if (cleaned.length() > 83) {
				cleaned = cleaned.substring(0, 80) + "...";
			}
			Text newElement = new Text(i + ": " + cleaned);
			newElement.setOnMouseClicked((MouseEvent event) -> {
				clipboardManipulator.setTemporaryClipboardText(original);
			});

			StringProperty style = new SimpleStringProperty();
			style.setValue("-fx-cursor: hand;");
			newElement.styleProperty().bind(style);
			VBox.setMargin(newElement, new Insets(0, 0, 6, 0));
			elements[i] = newElement;
		}

		return elements;
	}

	public VBox getVBox() {
		return this.vBox;
	}

	public static VBox create(String[] history) {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));

		for (String text: history) {
			Button jumpTo = new Button(text);
			VBox.setMargin(jumpTo, new Insets(10));
			vbox.getChildren().addAll(jumpTo);
		}

		return vbox;
	}
}
