package Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InterestRateOutOfBoundsException extends Exception{
	
	public InterestRateOutOfBoundsException(String errorMessage){
		super(errorMessage);
	}

}