package networkclipboard.domain;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Tests for the {@link ClipboardAccess} class.
 */
public class ClipboardAccessTest {
	private Clipboard clipboard;
	private ClipboardAccess clipboardAccess;

	/**
	 * Prepares the environment for testin the {@link ClipboardAccess} class.
	 */
	@Before
	public void setUp() {
		this.clipboard = mock(Clipboard.class);
		this.clipboardAccess = new ClipboardAccess(clipboard);
	}

	/**
	 * Tests if the {@link ClipboardAccess#setString(String)} method gives a
	 * {@link StringSelection} with the right {@link String} to the wrapped
	 * {@link Clipboard}.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Test
	public void setStringSetsRightStringOntoClipboard() throws UnsupportedFlavorException, IOException {
		this.clipboardAccess.setString("test");
		final ArgumentCaptor<StringSelection> captor = ArgumentCaptor.forClass(StringSelection.class);
		verify(this.clipboard).setContents(captor.capture(), eq(null));
		final StringSelection stringSelection = captor.getValue();
		String given = stringSelection.getTransferData(DataFlavor.stringFlavor).toString();
		assertEquals("test", given);
	}

	/**
	 * Tests if the {@link ClipboardAccess#getString()} method returns the
	 * {@link String} that is on the wrapped {@link Clipboard}.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Test
	public void getStringGetsRightStringFromClipboard() throws UnsupportedFlavorException, IOException {
		when(this.clipboard.getData(DataFlavor.stringFlavor)).thenReturn("test");
		final String result = this.clipboardAccess.getString();
		assertEquals("test", result);
	}

	/**
	 * Test if the {@link ClipboardAccess#containsString()} method returns
	 * {@code true} when the wrapped {@link Clipboard} tells that 
	 * {@link DataFlavor#stringFlavor} is available.
	*/
	@Test
	public void containsStringReturnsTrueWhenStringFlavorAvailabel() {
		when(this.clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)).thenReturn(true);
		final Boolean result = this.clipboardAccess.containsString();
		assertEquals(true, result);
	}

	/**
	 * Test if the {@link ClipboardAccess#containsString()} method returns
	 * {@code false} when the wrapped {@link Clipboard} tells that 
	 * {@link DataFlavor#stringFlavor} is not available.
	*/
	@Test
	public void containsStringReturnsFalseWhenStringFlavorNotAvailabel() {
		when(this.clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)).thenReturn(false);
		final Boolean result = this.clipboardAccess.containsString();
		assertEquals(false, result);
	}
}
