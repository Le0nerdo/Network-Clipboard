package ui;

import java.awt.Toolkit;
import java.util.prefs.Preferences;

import dao.DatabaseAccess;
import dao.MongoDBAccess;
import domain.ClipboardAccess;
import domain.ClipboardManipulator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A Class that runs the application using javaFX.
 * @author Le0nerdo
 */
public class UserInterface extends Application {
	private ClipboardManipulator clipboardManipulator;
	private Timeline update;
	private Preferences pref;
	/**
	 * Initialises the application.
	 */
	@Override
	public void init() {
		pref = Preferences.userRoot().node("/user/network_clipboard");
		ClipboardAccess clipboardAccess = new ClipboardAccess(
							Toolkit.getDefaultToolkit().getSystemClipboard()
		);
		DatabaseAccess database = new MongoDBAccess(pref.get("ATLAS_URI", "hups"));
		this.clipboardManipulator = new ClipboardManipulator(clipboardAccess, database);

		while (true) {
			try {
				this.clipboardManipulator.updateClipboard();
				Toolkit.getDefaultToolkit().getSystemClipboard()
								.addFlavorListener(
								this.clipboardManipulator.createClipboardListener()
				);
				break;
			} catch (Exception e) {
				System.out.println("UserInterface/init" + e);
			}
		}
	}

	/**
	 * Creates the userinterface and opens it.
	 * @param stage JavaFX main stage of the application.
	 */
	@Override
	public void start(final Stage stage) {
		stage.setTitle("Network Clipboard");
		BorderPane mainLayout = new BorderPane();

		ClipboardHistory clipboardHistory =
							new ClipboardHistory(this.clipboardManipulator);
		mainLayout.setCenter(clipboardHistory.getVBox());

		Controls controls = new Controls(clipboardManipulator, pref);
		mainLayout.setBottom(controls.getElement());

		this.update = this.createUpdateTimeline(clipboardHistory);

		this.update.setCycleCount(Timeline.INDEFINITE);
		this.update.play();

		final int windowHeight = 300;
		final int windowWIdth = 500;
		stage.setScene(new Scene(mainLayout, windowWIdth, windowHeight));
		controls.setDefaultFocus();
		stage.show();
	}

	private Timeline createUpdateTimeline(ClipboardHistory clipboardHistory) {
		return new Timeline(
			new KeyFrame(Duration.seconds(1),
			new EventHandler<ActionEvent>() {
					@Override
					public void handle(final ActionEvent actionEvent) {
						clipboardHistory.update();
					}
				}
		));
	}

	/**
	 * Stops the application.
	 */
	@Override
	public void stop() {
		this.update.stop();
		this.clipboardManipulator.disconnectDatabase();
	}

	/**
	 * Utility class to start the application.
	 * @param args command-line arguments.
	 */
	public static void main(final String[] args) {
		launch(args);
	}

}
