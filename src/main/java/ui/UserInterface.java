package ui;

import java.awt.Toolkit;
import java.util.Arrays;
import java.util.prefs.Preferences;

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
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A Class that runs the application using javaFX.
 * @author Le0nerdo
 */
public class UserInterface extends Application {
    /**
     * Instance of ClipboardManipulator, that allows manipulation of a
     * clipboard and dabase.
     */
    private ClipboardManipulator clipboardManipulator;
    /**
     * A Timeline to update the userinterface.
     */
    private Timeline update;
    private Preferences pref;
    /**
     * Initialises the application.
     * @throws Exception
     */
    @SuppressWarnings("checkstyle:methodlength")
    @Override
    public void init() throws Exception {
        pref = Preferences.userRoot()
            .node("/user/network_clipboard");
        String dbUri = pref.get("ATLAS_URI", "hups");
        ClipboardAccess clipboardAccess = new SystemClipboard();
        DBAccess database = new MongoDBAccess(dbUri);
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
     * @throws Exception
     */
    @SuppressWarnings("checkstyle:methodlength")
    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Network Clipboard");
        BorderPane mainLayout = new BorderPane();

        ClipboardHistory clipboardHistory =
            new ClipboardHistory(this.clipboardManipulator);
        mainLayout.setCenter(clipboardHistory.getVBox());

        Controls controls = new Controls(clipboardManipulator, pref);
        mainLayout.setBottom(controls.getElement());

        this.update = new Timeline(
            new KeyFrame(Duration.seconds(1),
            new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent actionEvent) {
                        clipboardHistory.update();
                    }
                }
        ));

        this.update.setCycleCount(Timeline.INDEFINITE);
        this.update.play();

        final int windowHeight = 300;
        final int windowWIdth = 500;
        stage.setScene(new Scene(mainLayout, windowWIdth, windowHeight));
        controls.setDefaultFocus();
        stage.show();
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
