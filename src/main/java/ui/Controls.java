package ui;

import java.util.prefs.Preferences;

import domain.ClipboardManipulator;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Controls {
	private ClipboardManipulator clipboardManipulator;
	private Preferences pref;
	private HBox hBox = new HBox();
	private TextField inpuField = new TextField();
	private Button connectButton = new Button("Connect");
	private Button pauseButton = new Button("Pause");

	@SuppressWarnings("checkstyle:methodlength")
	public Controls(
		final ClipboardManipulator clipboardmanipulator,
		final Preferences pref
		) {
		this.clipboardManipulator = clipboardmanipulator;
		this.pref = pref;
		final int padding = 10;
		this.hBox.setPadding(new Insets(padding));
		this.inpuField.setPromptText("MongoDB Atlas URI");
		this.connectButton.setOnAction((ActionEvent e) -> {
			String newUri = inpuField.getText();
			clipboardManipulator.reconnectDatabaseTo(newUri);
			pref.put("ATLAS_URI", newUri);
			inpuField.setText("");
		});

		Button stopButton = new Button("Stop");
		stopButton.setOnAction((ActionEvent e) -> {
			if (clipboardmanipulator.getStopped()) {
				clipboardmanipulator.unStop();
				stopButton.setText("Stop");
			} else {
				clipboardmanipulator.stop();
				stopButton.setText("Unstop");
			}
		});

		pauseButton.setOnAction((ActionEvent e) -> {
			if (clipboardmanipulator.getPaused()) {
				clipboardmanipulator.unPause();
				pauseButton.setText("Pause");
			} else {
				clipboardmanipulator.pause();
				pauseButton.setText("Unpause");
			}
		});
		
		this.hBox.getChildren().add(this.inpuField);
		this.hBox.getChildren().add(this.connectButton);
		this.hBox.getChildren().add(stopButton);
		this.hBox.getChildren().add(pauseButton);
	}
	
	public void setDefaultFocus() {
		this.pauseButton.requestFocus();
	}


	public HBox getElement() {
		return this.hBox;
	}


}
