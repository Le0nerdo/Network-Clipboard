package domain;

import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.util.Arrays;

import dao.DBAccess;

public class ClipboardManipulator {
	private DBAccess dao;
	private String[] last;
	private ClipboardAccess clipboardAccess;
	//private Boolean ignore = false;

	public ClipboardManipulator(ClipboardAccess clipboardAccess, DBAccess dao, String[] last) {
		this.dao = dao;
		this.last = last;
		this.clipboardAccess = clipboardAccess;
	}

	public String[] updateClipboard() {
		String[] texts = this.dao.read();
		if (texts[0].equals(this.last[0])) {
			this.last = texts;
			return last;
		}
		try {
			this.last = texts;
			this.clipboardAccess.setText(texts[0]);
		} catch (Exception e) {
			System.out.println("Write on clipboard Error: " + e);
		}

		return last;
	}

	public void setTemporaryClipboardText(String text) {
		try {
			//ignore = true;
			this.clipboardAccess.setText(text);
		} catch (Exception e) {
			System.out.println("setTemporaryClipboardText Error: " + e);
		}
	}

	public FlavorListener createClipboardListener() {
		return new FlavorListener() {
			@Override 
			public void flavorsChanged(FlavorEvent e) {
				//if (ignore) {
				//	ignore = false;
				//	return;
				//}
				try {
					if (clipboardAccess.isString()) {
						String text = clipboardAccess.readText();
						if (Arrays.asList(last).contains(text)) return;
						dao.write(text);
						updateClipboard();
					}
				} catch (Exception ee) {
					System.out.println("FlavorListener Error: " + ee);
				}
			}
		};
	}
}
