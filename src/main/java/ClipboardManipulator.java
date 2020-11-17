import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;

public class ClipboardManipulator {
	private DBAccess dao;
	private String[] last;
	private ClipboardAccess clipboardAccess;

	public ClipboardManipulator(ClipboardAccess clipboardAccess, DBAccess dao, String[] last) {
		this.dao = dao;
		this.last = last;
		this.clipboardAccess = clipboardAccess;
	}

	public void updateClipboard() {
		String text = this.dao.read();
		if (text.equals(this.last[0])) return;
		try {
			this.last[0] = text;
			this.clipboardAccess.setText(text);
			System.out.println("Downloaded: \"" + text + "\"");
		} catch (Exception e) {
			System.out.println("Write on clipboard Error: " + e);
		}
	}

	public static FlavorListener createClipboardListener(ClipboardAccess clipboardAccess, DBAccess dao, String[] last) {
		return new FlavorListener() {
			@Override 
			public void flavorsChanged(FlavorEvent e) {

				try {
					if (clipboardAccess.isString()) {
						String text = clipboardAccess.readText();
						if (text.equals(last[0])) return;
						last[0] = text;
						dao.write(text);
						System.out.println("Uploaded: \"" + text + "\"");
						clipboardAccess.setText(text);
					}
				} catch (Exception ee) {
					System.out.println("FlavorListener Error: " + ee);
				}
			}
		};
	}
}
