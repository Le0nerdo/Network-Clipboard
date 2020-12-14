package networkclipboard.ui;

import java.util.prefs.Preferences;

import networkclipboard.domain.ClipboardManipulator;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * Provides the userinterface component for controlling the state of
 * ClipboardManipulator.
 */
public class Controls {
	private ClipboardManipulator clipboardManipulator;
	private HBox hBox = new HBox();
	private TextField inpuField = new TextField();
	private Button connectButton = new Button("Connect");
	private Button pauseButton = new Button("Pause");

	/**
	 * Creates all the control elements.
	 * @param clipboardmanipulator ClipboardManipulator to be controlled.
	 * @param pref element for controllin user preferences (saving URI).
	 */
	public Controls(final ClipboardManipulator clipboardmanipulator, final Preferences pref) {
		this.clipboardManipulator = clipboardmanipulator;
		final int padding = 10;
		this.hBox.setPadding(new Insets(padding));
		this.inpuField.setPromptText("MongoDB Atlas URI");
		this.connectButton.setOnAction((ActionEvent e) -> {
			String newUri = inpuField.getText();
			clipboardManipulator.connectDatabaseTo(newUri);
			pref.put("ATLAS_URI", newUri);
			inpuField.setText("");
		});

		Button stopButton = new Button("Stop");
		stopButton.setOnAction((ActionEvent e) -> {
			if (clipboardmanipulator.getStopped()) {
				clipboardmanipulator.setStopped(false);
				stopButton.setText("Stop");
			} else {
				clipboardmanipulator.setStopped(true);
				stopButton.setText("Unstop");
			}
		});

		pauseButton.setOnAction((ActionEvent e) -> {
			if (clipboardmanipulator.getPaused()) {
				clipboardmanipulator.setPaused(false);
				pauseButton.setText("Pause");
			} else {
				clipboardmanipulator.setPaused(true);
				pauseButton.setText("Unpause");
			}
		});

		Button clearButton = new Button("Clear");
		clearButton.setOnAction((ActionEvent e) -> {
			pref.remove("ATLAS_URI");
			clipboardManipulator.connectDatabaseTo("");
			clipboardManipulator.clear();
		});

		this.hBox.getChildren().add(this.inpuField);
		this.hBox.getChildren().add(this.connectButton);
		this.hBox.getChildren().add(stopButton);
		this.hBox.getChildren().add(pauseButton);
		this.hBox.getChildren().add(clearButton);
	}

	/**
	 * Sets focus onto the pause button.
	 */
	public void setDefaultFocus() {
		this.pauseButton.requestFocus();
	}

	/**
	 * Gets the JavaFX element for controlling the ClipboardManipulator.
	 * @return JavaFX element with controls
	 */
	public HBox getElement() {
		return this.hBox;
	}

}
