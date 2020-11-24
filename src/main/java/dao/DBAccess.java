package dao;

public interface DBAccess {
	void write(String text);
	String[] read();
	void close();
}
