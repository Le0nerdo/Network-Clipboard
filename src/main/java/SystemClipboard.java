import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class SystemClipboard implements ClipboardAccess {
	private Clipboard clipboard;

	public SystemClipboard() {
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public void setText(String text) throws IllegalStateException {
		StringSelection stringSelection = new StringSelection(text);
		this.clipboard.setContents(stringSelection, null);
	}

	public String readText() throws UnsupportedFlavorException, IOException {
		return (String) clipboard.getData(DataFlavor.stringFlavor);
	}

	public Boolean isString() {
		return clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor);
	}
}
