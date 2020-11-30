package domain;

import org.junit.Before;
import org.junit.Test;

import dao.DBAccess;

import static org.mockito.Mockito.*;

import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ClipboardManipulatorTest {
	DBAccess dao;
	ClipboardAccess clipboard;
	ClipboardManipulator manipulator;
	String[] aStrings = {"a1,", "a2," , "a3,", "a4,", "a5,", "a6,", "a7,", "a8,", "a9,", "a10,"};

	@Before
	public void setUp() throws UnsupportedFlavorException, IOException {
		this.dao = mock(DBAccess.class);
		this.clipboard = mock(ClipboardAccess.class);

		when(this.dao.read()).thenReturn(this.aStrings);
		when(this.dao.isConnected()).thenReturn(true);
		when(this.clipboard.readText()).thenReturn("c1,");
		when(this.clipboard.isString()).thenReturn(true);

		this.manipulator = new ClipboardManipulator(clipboard, dao);
	}


	@Test
	public void updateClipboardUpdatesClipboardFromDatabase() {
		this.manipulator.updateClipboard();
		verify(clipboard).setText(this.aStrings[0]);
	}

	@Test
	public void updateClipboardDoesNotUpdateClipboardWithSameText() throws UnsupportedFlavorException, IOException {
		when(this.clipboard.readText()).thenReturn("a1,");
		when(this.clipboard.readText()).thenReturn("a1,");
		this.manipulator.updateClipboard();
		verify(clipboard, times(1)).setText(anyString());
	}

	@Test
	public void clipboardListenerUpdatesDatabase() {
		FlavorListener clipboardListener = manipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		clipboardListener.flavorsChanged(event);
		verify(dao).write("c1,");
	}

	@Test
	public void clipboardListenerDoesNotUpdateDatabaseWhitRecentText() throws UnsupportedFlavorException, IOException {
		when(this.clipboard.readText()).thenReturn("a3,");

		FlavorListener clipboardListener = manipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		clipboardListener.flavorsChanged(event);
		clipboardListener.flavorsChanged(event);
		verify(dao, times(1)).write(anyString());
	}

	@Test
	public void clipboardListenerDoesNotUpdateDataBaseWhenNoTextOnClipboard() throws UnsupportedFlavorException, IOException {
		when(this.clipboard.isString()).thenReturn(false);
		
		FlavorListener clipboardListener = manipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		clipboardListener.flavorsChanged(event);
		verify(dao, times(0)).write(anyString());
	}

	@Test
	public void setTemporaryClipboardTextSetsTextOnClipboard() {
		this.manipulator.setTemporaryClipboardText("d1,");
		verify(clipboard).setText("d1,");
	}

}
