import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;

import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException, GeneralSecurityException {
		Dotenv dotenv = Dotenv.load();
		String db_uri = dotenv.get("DB_URI");
		ClipboardAccess clipboardAccess = new SystemClipboard();
		DBAccess dao = new MongoDBAccess(db_uri);
		String[] last = {""};
		ClipboardManipulator clipboardManipulator = new ClipboardManipulator(clipboardAccess, dao, last);

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Closing Program");
				dao.close();
			}
		});

		try {
			Toolkit.getDefaultToolkit().getSystemClipboard()
				.addFlavorListener(ClipboardManipulator.createClipboardListener(clipboardAccess, dao, last));
		} catch (Exception e) {
			System.out.println(e);
		}

		clipboardManipulator.updateClipboard();

		System.out.println("Program started");

		while(true) {
			TimeUnit.MILLISECONDS.sleep(1000);
			clipboardManipulator.updateClipboard();
		}
	}
}
