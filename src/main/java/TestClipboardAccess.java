import java.util.ArrayList;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class TestClipboardAccess implements ClipboardAccess {
	private ArrayList<String> written;
	private String[] toRead;
	private int index;
	private Boolean thereIsString;
	
	public TestClipboardAccess(String[] toRead, Boolean thereIsString) {
		this.written = new ArrayList<String>();
		this.toRead = toRead;
		this.index =  0;
		this.thereIsString = thereIsString;
	}

	public void setText(String  text) throws IllegalStateException {
		this.written.add(text);
	}

	public String readText() throws UnsupportedFlavorException, IOException {
		this.index += 1;
		return this.toRead[this.index - 1];
	}

	public Boolean isString() {
		if (this.index >= this.toRead.length) return false;
		return this.thereIsString;
	}

	public ArrayList<String> getWritten() {
		return this.written;
	}
}
