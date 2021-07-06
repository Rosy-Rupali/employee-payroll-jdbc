package exception;

public class DatabaseConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	public DatabaseConnectionException(String errorMessage) {
		super(errorMessage);

	}

}
