package Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ToManyAccountsException extends Exception{
	public ToManyAccountsException(String errorMessage){
			super(errorMessage);
		}

	}

