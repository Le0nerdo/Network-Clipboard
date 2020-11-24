package domain;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public interface ClipboardAccess {
	void setText(String  text) throws IllegalStateException;
	String readText() throws UnsupportedFlavorException, IOException;
	Boolean isString();
}
