package networkclipboard.domain;

import org.junit.Before;
import org.junit.Test;

import networkclipboard.dao.DatabaseAccess;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;

/**
 * Tests for the {@link ClipboardManipulator} class.
 */
public class ClipboardManipulatorTest {
	DatabaseAccess databaseAccess;
	ClipboardAccess clipboardAccess;
	ClipboardManipulator clipboardManipulator;
	String[] aStrings = {"a1,", "a2," , "a3,", "a4,", "a5,", "a6,", "a7,", "a8,", "a9,", "a10,"};

	/**
	 * Prepares the environment for testin the {@link ClipboardManipulator} class.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Before
	public void setUp() throws UnsupportedFlavorException, IOException {
		this.databaseAccess = mock(DatabaseAccess.class);
		this.clipboardAccess = mock(ClipboardAccess.class);
		
		this.clipboardManipulator = new ClipboardManipulator(clipboardAccess, databaseAccess);

		when(this.databaseAccess.read()).thenReturn(this.aStrings);
		when(this.databaseAccess.isConnected()).thenReturn(true);
		when(this.clipboardAccess.getString()).thenReturn("c1,");
		when(this.clipboardAccess.containsString()).thenReturn(true);

	}

	/**
	 * Tests that {@link ClipboardManipulator#connectDatabaseTo(String)} tells
	 * the {@link DatabaseAccess} to connect to the right URI.
	 */
	@Test
	public void connectDatabaseToGivesRightURIToDatabaseAccess() {
		this.clipboardManipulator.connectDatabaseTo("test.uri");
		verify(this.databaseAccess).reconnectTo("test.uri");
	}

	/**
	 * Test that {@link ClipboardManipulator#disconnectDatabase()} tells the
	 * {@link DatabaseAccess} to disconnect.
	 */
	@Test
	public void disconnectDatabaseTellsDatabaseToDisconnect() {
		this.clipboardManipulator.disconnectDatabase();
		verify(this.databaseAccess, times(1)).close();
	}


	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does not
	 * interact with {@link DatabaseAccess#read()} or {@link ClipboardAccess}
	 * when the {@link DatabaseAccess} is not connected.
	 */
	@Test
	public void updateClipboardDoesNothingWhenDatabaseIsNotConnected() {
		when(this.databaseAccess.isConnected()).thenReturn(false);
		this.clipboardManipulator.updateClipboard();
		verify(this.databaseAccess, times(0)).read();
		verifyNoInteractions(this.clipboardAccess);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does not interact
	 * with {@link DatabaseAccess} or {@link ClipboardAccess} when
	 * {@link ClipboardManipulator} is stopped.
	 * 
	 * @throws IOException if something goes wrong.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 */
	@Test
	public void updateClipboardDoesNothingWhenClipboadManipulatorIsStopped() throws UnsupportedFlavorException, IOException {
		this.clipboardManipulator.setStopped(true);
		this.clipboardManipulator.updateClipboard();
		verifyNoInteractions(this.clipboardAccess);
		verifyNoInteractions(this.databaseAccess);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} returns
	 * correct history when stopped.
	 */
	@Test
	public void updateClipboardReturnsRighHistoryWhenStopped() {
		this.clipboardManipulator.updateClipboard();
		this.clipboardManipulator.setStopped(true);
		final String[] result = this.clipboardManipulator.updateClipboard();
		assertArrayEquals(this.aStrings, result);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does not
	 * use {@link ClipboardAccess#setString()} when the clipboard already
	 * constains the right {@link String}.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Test
	public void updateClipboardUpdatesClipboardWithSameText() throws UnsupportedFlavorException, IOException {
		this.clipboardManipulator.updateClipboard();
		this.clipboardManipulator.updateClipboard();
		verify(clipboardAccess, times(2)).setString(anyString());
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does not
	 * use {@link ClipboardAccess#setString(String)} when paused.
	 * @throws IOException if something goes wrong.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 */
	@Test
	public void updateClipboardDoesNotUpdateClipboardWhenPaused() throws UnsupportedFlavorException, IOException {
		this.clipboardManipulator.setPaused(true);
		this.clipboardManipulator.updateClipboard();
		verify(this.clipboardAccess, times(0)).setString(anyString());
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does not
	 * try to read {@link String} from {@link ClipboardAccess} when no
	 * {@link String} is available.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Test
	public void updateClipboardDoesNotGetClipboardAccessStringWhenNotAvailable() throws UnsupportedFlavorException, IOException {
		when(this.clipboardAccess.containsString()).thenReturn(false);
		this.clipboardManipulator.updateClipboard();
		verify(this.clipboardAccess, times(0)).getString();
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} returns
	 * correct history when paused.
	 */
	@Test
	public void updateClipboardReturnsRighHistoryWhenPaused() {
		this.clipboardManipulator.setPaused(true);
		final String[] result = this.clipboardManipulator.updateClipboard();
		assertArrayEquals(this.aStrings, result);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} uodates
	 * the {@link ClipboardAccess} with the newest {@link String} from
	 * {@link DatabaseAccess}.
	 */
	@Test
	public void updateClipboardUpdatesClipboardCorrectlyFromDatabase() {
		this.clipboardManipulator.updateClipboard();
		verify(this.clipboardAccess).setString(this.aStrings[0]);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} retursn
	 * the same array of {@link String}s that the {@link DatabaseAccess}
	 * provides.
	 */
	@Test
	public void updateClipboardReturnsRightHistoryFromDatabase() {
		final String[] result = this.clipboardManipulator.updateClipboard();
		assertArrayEquals(this.aStrings, result);
	}

	/**
	 * Test that {@link ClipboardManipulator#updateClipboard()} does no write
	 * to clipboard when latest history element is not changed and a temporary
	 * text is on the clipboard.
	 */
	@Test
	public void updateClipboardDoesDoesNotChangeTemporeryTextWhenNoNewText() {
		this.clipboardManipulator.updateClipboard();
		this.clipboardManipulator.setTemporaryClipboardText("text");
		this.clipboardManipulator.updateClipboard();
		verify(this.clipboardAccess, times(2)).setString(anyString());
	}

	/**
	 * Test that {@link ClipboardManipulator#setTemporaryClipboardText(String)}
	 * provides {@link ClipboardAccess} with the right {@link String}.
	 */
	@Test
	public void setTemporaryClipboardTextSetsTextOnClipboard() {
		this.clipboardManipulator.setTemporaryClipboardText("d1,");
		verify(clipboardAccess).setString("d1,");
	}

	/**
	 * Test that {@link ClipboardManipulator#setTemporaryClipboardText(String)}
	 * does not interact with {@link DatabaseAccess}.
	 */
	@Test
	public void setTemporaryClipboardTextDoesNotInteractWithDatabaseAccess() {
		this.clipboardManipulator.setTemporaryClipboardText("d1,");
		verifyNoInteractions(this.databaseAccess);
	}

	/**
	 * Test that the {@link FlavorListener} created by
	 * {@link ClipboardManipulator#createClipboardListener()} does nothing when
	 * {@link ClipboardManipulator} is stopped.
	 */
	@Test
	public void clipboardListenerDoesNothingWhenStopped() {
		FlavorListener clipboardListener = clipboardManipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		this.clipboardManipulator.setStopped(true);
		clipboardListener.flavorsChanged(event);
		verifyNoInteractions(this.clipboardAccess);
		verifyNoInteractions(this.databaseAccess);
	}

	/**
	 * Test that the {@link FlavorListener} created by
	 * {@link ClipboardManipulator#createClipboardListener()} does nothing after
	 * {@link ClipboardAccess} contains no string.
	 * @throws IOException if something goes wrong.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 */
	@Test
	public void clipboardListenerDoesNothingWhenNoStringOnClipboard() throws UnsupportedFlavorException, IOException {
		FlavorListener clipboardListener = clipboardManipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		when(this.clipboardAccess.containsString()).thenReturn(false);
		clipboardListener.flavorsChanged(event);
		verify(this.clipboardAccess, times(0)).getString();
		verifyNoInteractions(this.databaseAccess);
	}

	/**
	 * Test that the {@link FlavorListener} created by
	 * {@link ClipboardManipulator#createClipboardListener()} does nothing after
	 * {@link DatabaseAccess} informs to not be connected.
	 * @throws IOException if something goes wrong.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 */
	@Test
	public void clipboardListenerDoesNothingWhenDatabaseIsNotConnected() throws UnsupportedFlavorException, IOException {
		FlavorListener clipboardListener = clipboardManipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		when(this.databaseAccess.isConnected()).thenReturn(false);
		clipboardListener.flavorsChanged(event);
		verify(this.clipboardAccess, times(0)).getString();
		verify(databaseAccess, times(0)).write(anyString());
	}

	/**
	 * Test that the {@link FlavorListener} created by
	 * {@link ClipboardManipulator#createClipboardListener()} does not update
	 * the {@link DatabaseAccess} with a {@link String} that is already the
	 * last one in history.
	 * @throws UnsupportedFlavorException if something goes wrong.
	 * @throws IOException if something goes wrong.
	 */
	@Test
	public void clipboardListenerDoesNotUpdateDatabaseWhitLastText() throws UnsupportedFlavorException, IOException {
		this.clipboardManipulator.updateClipboard();
		when(this.clipboardAccess.getString()).thenReturn("a1,");

		FlavorListener clipboardListener = clipboardManipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		clipboardListener.flavorsChanged(event);
		verify(databaseAccess, times(0)).write(anyString());
	}

	/**
	 * Test that the {@link FlavorListener} created by
	 * {@link ClipboardManipulator#createClipboardListener()} updates the
	 * {@link DatabaseAccess} with the correct {@link String}. 
	 */
	@Test
	public void clipboardListenerUpdatesDatabaseWithCorrectString() {
		FlavorListener clipboardListener = clipboardManipulator.createClipboardListener();
		FlavorEvent event = new FlavorEvent(new Clipboard("fake"));
		clipboardListener.flavorsChanged(event);
		verify(databaseAccess).write("c1,");
	}

	/**
	 * Test that {@link ClipboardManipulator#setStopped(Boolean)} and
	 * {@link ClipboardManipulator#getStopped()} work right in related to each
	 * other.
	 */
	@Test
	public void clipboardAccessStoppedSetterGetterTest() {
		this.clipboardManipulator.setStopped(false);
		assertEquals(false, this.clipboardManipulator.getStopped());
		this.clipboardManipulator.setStopped(true);
		assertEquals(true, this.clipboardManipulator.getStopped());
	}

	/**
	 * Test that {@link ClipboardManipulator#setPaused(Boolean)} and
	 * {@link ClipboardManipulator#getPaused()} work right in related to each
	 * other.
	 */
	@Test
	public void clipboardAccessPausedSetterGetterTest() {
		this.clipboardManipulator.setPaused(false);
		assertEquals(false, this.clipboardManipulator.getPaused());
		this.clipboardManipulator.setPaused(true);
		assertEquals(true, this.clipboardManipulator.getPaused());
	}

	/**
	 * Test that {@link ClipboardManipulator#isConnected()} returns
	 * {@code true} when {@link DatabaseAccess} informs to be connected.
	 */
	@Test
	public void isConnectedRetursTrueWhenDatabaseIsConnected() {
		assertEquals(true, this.clipboardManipulator.isConnected());
	}

	/**
	 * Test that {@link ClipboardManipulator#isConnected()} returns
	 * {@code false} when {@link DatabaseAccess} informs to not be connected.
	 */
	@Test
	public void isConnectedRetursFalseWhenDatabaseIsNotConnected() {
		when(this.databaseAccess.isConnected()).thenReturn(false);
		assertEquals(false, this.clipboardManipulator.isConnected());
	}

	/**
	 * Test that {@link ClipboardManipulator#clear()} changes the local history
	 * to contain only empty {@link String}s.
	 */
	@Test
	public void clearClearsHistory() {
		this.clipboardManipulator.updateClipboard();
		when(this.databaseAccess.isConnected()).thenReturn(false);
		this.clipboardManipulator.clear();
		final String[] emtyStrings = new String[10];
		Arrays.fill(emtyStrings, "");
		assertArrayEquals(emtyStrings, this.clipboardManipulator.updateClipboard());
	}
}
