package skaro.pokeaimpi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5076166103373186644L;
	
	public UserNotFoundException(Integer id) {
		super("Could not find user with id " + id);
	}
	
	public UserNotFoundException(String msg) {
		super(msg);
	}

}
