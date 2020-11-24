package ui;

import java.awt.Toolkit;
import java.util.Arrays;

import dao.DBAccess;
import dao.MongoDBAccess;
import domain.ClipboardAccess;
import domain.ClipboardManipulator;
import domain.SystemClipboard;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UserInterface extends Application {
	private DBAccess dao;
	private ClipboardManipulator clipboardManipulator;
	private String[] last = new String[10];
	private Timeline update;

	@Override
	public void init() throws Exception {
		Dotenv dotenv = Dotenv.load();
		String db_uri = dotenv.get("DB_URI");
		ClipboardAccess clipboardAccess = new SystemClipboard();
		Arrays.fill(this.last, "");
		dao = new MongoDBAccess(db_uri);
		clipboardManipulator = new ClipboardManipulator(clipboardAccess, dao, last);
		
		try {
			clipboardManipulator.updateClipboard();
		} catch (Exception e) {
			System.out.println(e);
		}

		while (true) {
			try {
				Toolkit.getDefaultToolkit().getSystemClipboard()
						.addFlavorListener(clipboardManipulator.createClipboardListener());
				break;
			} catch (Exception e) {
				System.out.println(e);
			}
		}


	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Network Clipboard");

		Button button = new Button("exit");
		button.setOnAction(e -> stage.close());

		BorderPane mainLayout = new BorderPane();
		mainLayout.setBottom(button);

		VBox center = new VBox();
		HBox historyHeader = new HBox();
		historyHeader.setAlignment(Pos.CENTER);

		historyHeader.getChildren().add(new Text("Click on text in history below to apply it to clipbord."));
		ClipboardHistory clipboardHistory = new ClipboardHistory(clipboardManipulator, last);
		center.getChildren().add(historyHeader);
		center.getChildren().add(clipboardHistory.getVBox());

		mainLayout.setCenter(center);

		update = new Timeline(
			new KeyFrame(Duration.seconds(1),
			new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					last = clipboardManipulator.updateClipboard();
					clipboardHistory.update(last);
				}
			}
		));

		update.setCycleCount(Timeline.INDEFINITE);
		update.play();

		Scene scene = new Scene(mainLayout, 500, 500);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		System.out.println("Closing Program");
		update.stop();
		dao.close();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
