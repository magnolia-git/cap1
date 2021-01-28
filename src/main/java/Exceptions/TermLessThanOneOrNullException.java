package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TermLessThanOneOrNullException extends Exception {

	public TermLessThanOneOrNullException(String errorMessage) {
		super(errorMessage);
	}

}
