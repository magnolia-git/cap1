package Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TooManyAccountsException extends Exception{
	public TooManyAccountsException(String errorMessage){
			super(errorMessage);
		}

	}

