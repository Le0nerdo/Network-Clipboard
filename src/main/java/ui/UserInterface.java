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
     * Instance of DBAccess that give access to a database.
     */
    private DBAccess databaseAccess;
    /**
     * Instance of ClipboardManipulator, that allows manipulation of a
     * clipboard and dabase.
     */
    private ClipboardManipulator clipboardManipulator;
    /**
     * Lenght of the history.
     */
    private final int historyLenght = 10;
    /**
     * History of the last texts on clipboard/database.
     */
    private String[] history = new String[historyLenght];
    /**
     * A Timeline to update the userinterface.
     */
    private Timeline update;

    /**
     * Initialises the application.
     * @throws Exception
     */
    @Override
    public void init() throws Exception {
        Dotenv dotenv = Dotenv.load();
        String dbUri = dotenv.get("DB_URI");
        ClipboardAccess clipboardAccess = new SystemClipboard();
        Arrays.fill(this.history, "");
        this.databaseAccess = new MongoDBAccess(dbUri);
        this.clipboardManipulator = new ClipboardManipulator(
            clipboardAccess,
            this.databaseAccess,
            this.history
        );

        while (true) {
            try {
                this.clipboardManipulator.updateClipboard();
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .addFlavorListener(
                            this.clipboardManipulator.createClipboardListener()
                        );
                break;
            } catch (Exception e) {
                System.out.println(e);
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
            new ClipboardHistory(this.clipboardManipulator, this.history);
        mainLayout.setCenter(clipboardHistory.getVBox());

        this.update = new Timeline(
            new KeyFrame(Duration.seconds(1),
            new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent actionEvent) {
                        history = clipboardManipulator.updateClipboard();
                        clipboardHistory.update(history);
                    }
                }
        ));

        this.update.setCycleCount(Timeline.INDEFINITE);
        this.update.play();

        final int windowHeight = 500;
        final int windowWIdth = 250;
        stage.setScene(new Scene(mainLayout, windowHeight, windowWIdth));
        stage.show();
    }

    /**
     * Stops the application.
     */
    @Override
    public void stop() {
        this.update.stop();
        this.databaseAccess.close();
    }

    /**
     * Utility class to start the application.
     * @param args command-line arguments.
     */
    public static void main(final String[] args) {
        launch(args);
    }

}
