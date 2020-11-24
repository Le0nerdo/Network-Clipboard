package dao;

import java.util.ArrayList;


public class TestDBAccess implements DBAccess{
	private ArrayList<String> written;
	private String[] toRead;

	public TestDBAccess(String toRead[]) {
		this.written = new ArrayList<String>();
		this.toRead = toRead;
	}

	public void write(String text) {
		this.written.add(text);
	}

	public String[] read() {
		return this.toRead;
	}

	public void close() {}

	public ArrayList<String> getWritten() {
		return this.written;
	}
}
