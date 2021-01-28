package Exceptions;
public class ExceedsFraudSuspicionLimitException extends Exception {

	public ExceedsFraudSuspicionLimitException() {
		super("Error Found");
	}
	
	public ExceedsFraudSuspicionLimitException(String errorMessage) {
		
		super(errorMessage);
		
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
