package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExceedsCombinedBalanceLimitException extends Exception{
	
	public ExceedsCombinedBalanceLimitException(String errorMessage){
		super(errorMessage);
	}

}

//if balance > 25000