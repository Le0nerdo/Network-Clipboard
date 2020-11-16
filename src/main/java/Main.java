import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.concurrent.TimeUnit;

import io.github.cdimascio.dotenv.Dotenv;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main {
	public static void main(String[] args) throws InterruptedException, IOException, GeneralSecurityException {
		Dotenv dotenv = Dotenv.load();
		String db_uri = dotenv.get("DB_URI");
		MongoDBAccees dao = new MongoDBAccees(db_uri);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		String[] last = {""};

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				dao.close();
			}
		});

		try {
			Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(new FlavorListener() { 
				@Override 
				public void flavorsChanged(FlavorEvent e) {

					try {
						if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
							String text = (String) clipboard.getData(DataFlavor.stringFlavor);
							if (text.equals(last[0])) return;
							last[0] = text;
							dao.write(text);
							System.out.println(text);
							StringSelection test = new StringSelection(text);
							Toolkit.getDefaultToolkit().getSystemClipboard().setContents(test, null);
						}
					} catch (Exception ee) {
						System.out.println("FlavorListener Error: " + ee);
					}
				}
			}); 
		} catch (Exception e) {
			System.out.println(e);
		}

		while(true) {
			TimeUnit.MILLISECONDS.sleep(1000);
			String text = dao.read();
			if (text.equals(last[0])) continue;
			last[0] = text;
			StringSelection test = new StringSelection(text);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(test, null);
		}
	}
}
