package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegativeBalanceException extends Exception {
	public NegativeBalanceException(String errorMessage) {
		  super (errorMessage); 
	}

}

//if balance < 0 