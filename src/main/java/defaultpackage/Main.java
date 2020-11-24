package defaultpackage;

import ui.UserInterface;

/**
 * Main class of Network Clipboard that is used to start the application.
 * @author Le0nerdo
 */
public final class Main {

    private Main() {

    }
    /**
     * Starts the application.
     * @param args command-line arguments.
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        UserInterface.main(args);
    }
}
