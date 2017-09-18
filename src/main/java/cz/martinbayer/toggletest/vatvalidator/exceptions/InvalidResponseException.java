package cz.martinbayer.toggletest.vatvalidator.exceptions;

public class InvalidResponseException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1083662728464937275L;

	public InvalidResponseException(final String message) {
		super(message);
	}
}
