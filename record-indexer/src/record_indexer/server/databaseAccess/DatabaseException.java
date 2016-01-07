package record_indexer.server.databaseAccess;

@SuppressWarnings("serial")
public class DatabaseException extends Exception{

	public DatabaseException() {
		return;
	}

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable throwable) {
		super(throwable);
	}

	public DatabaseException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
