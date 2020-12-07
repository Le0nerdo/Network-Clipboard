package dao;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import com.mongodb.client.FindIterable;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.bson.Document;
public class MongoDBAccessTest {
	MongoDBCollection collection;
	MongoDBAccess mongoDBAccess;


	/**
	 * Sets up MongoDBAccess tests.
	 */
	@Before
	public void setUp() {
		this.collection = mock(MongoDBCollection.class);
		this.mongoDBAccess = new MongoDBAccess(collection);
	}

	/**
	 * Tests that write() gives text as document to collection.
	 */
	@Test
	public void writeTestWritesRightString() {
		this.mongoDBAccess.write("test");
		final ArgumentCaptor<Document> captor = ArgumentCaptor.forClass(Document.class);
		verify(this.collection).insertOne(captor.capture());
		final Document document = captor.getValue();
		final String given = (String) document.get("text");
		assertEquals("test", given);
	}

	/**
	 * Tests that read gives right array according to collection.
	 */
	@Test
	public void readGivesRightArray() {
		Document[] testArray = new Document[10];
		for (int i = 0; i < 10; i++) {
			testArray[i] = new Document("text", "test" + i);
		}
		FindIterable<Document> iterator = mock(FindIterable.class);
		when(iterator.sort(any())).thenReturn(iterator);
		when(iterator.limit(anyInt())).thenReturn(iterator);
		when(iterator.spliterator()).thenReturn(Arrays.spliterator(testArray));
		when(this.collection.find()).thenReturn(iterator);

		String[] stringArray = new String[10];
		for (int i = 0; i < 10; i++) {
			stringArray[i] = "test" + i;
		}
		assertArrayEquals(stringArray, this.mongoDBAccess.read());
	}

	/**
	 * Test that isConnected returns the right value.
	 */
	@Test
	public void testIsConnected() {
		when(this.collection.isConnected()).thenReturn(true).thenReturn(false);
		assertEquals(true, this.mongoDBAccess.isConnected());
		assertEquals(false, this.mongoDBAccess.isConnected());
	}

	/**
	 * Test that close closes the CollectionConnection.
	 */
	@Test
	public void closeClosesCollecttion() {
		this.mongoDBAccess.close();
		verify(this.collection, times(1)).close();
	}

	/**
	 * Test that reconnectTo passes string to collection.
	 */
	public void reconnectToPassesString() {
		this.mongoDBAccess.reconnectTo("mongodb+srv://test");
		verify(this.collection, times(1)).reconnectTo("mongodb+srv://test");
	}
}
